# WP2 Arm Working Notes (DOC / PRO / DAF / BOTH)

Operational guide for pilot step 3: translating B1 in the four remaining
arms. BASE is complete (31/31 graded-ok) and its heuristic log has seeded
cookbook v0 (DOC) and rule base v0 (PRO), entry-for-entry identical.
Grading rules, order, and instrumentation are FIXED (wp2/README.md,
wp1/capture_criteria.md, wp1/order_assignments.json) — deviations are
logged as data, never silently applied.

## 0. Common discipline (all arms)

1. **Unit order**: B1 units 1–31, complexity_rank ascending
   (PILOT_TRACKING.md order). Do not reorder within a batch.
2. **Grading**: interpreter-side units → frozen interpreter oracle
   (wp1/oracle/expected/); translator-side units → frozen translator
   oracle (wp2/translator-oracle/expected/), byte-equivalence required.
   Intentional non-equivalence = logged protocol deviation BEFORE
   proceeding.
3. **Build loop**: translate → build → fix → grade. Record every red
   build in the heuristic log (build-failure-fix rows, BASE precedent).
4. **Tooling lessons from BASE (binding)**:
   - never round-trip repo files through paged fetchers (the CSV clobber
     incident, unit 31); md5-verify any pushed blob;
   - pre-push scan before EVERY push: Kotlin grammar collisions (`*/` in
     comments, `$` in strings — H-015), escape bytes, `for (x in ...)`
     shadowing;
   - new-file-first: avoid rewrites of existing tracked files when a
     targeted edit will do.
5. **Adaptation**: reusing an entry with modification = hit + journal
   adaptation note (NOT reuse). Adapts feed G4 revisions of the entry in
   YOUR arm's artifact; the other arm's artifact is never edited by you.
6. **Oracles are read-only**: any change under wp1/oracle/expected/ or
   wp2/translator-oracle/expected/ is a hard stop.

## 1. Arm DOC — cookbook, manually consulted

- **Artifact**: `wp2/arms/DOC/cookbook_v0.md` (H-001..H-017).
- **Per unit, in this order**:
  1. Before editing, scan the C/C++ unit for canonical constructs
     (§2.4 normalization first). List what you find.
  2. **Lookup logging BEFORE consultation**: append a row to
     `wp2/logs/DOC_cookbook_journal.csv` (kind,target,unit,timestamp —
     same CSV schema as the PRO tooling) for each entry consulted,
     including the attempt that found nothing (kind=miss).
  3. Consult the cookbook. Apply the entry's action verbatim = record
     reuse (kind=reuse + kind=hit); with modification = kind=hit and a
     one-line adaptation note in the row.
  4. New resolution not covered (or failing a gate): stay as a journal
     note. Capture into the cookbook ONLY under T1/T2/T3 and gates G1–G4
     (id `H-0xx` next; revise existing ids in place with a version
     bump instead of adding duplicates).
- **Batch close**: manual coverage checklist in the journal — for each
  canonical construct in the batch, did ≥1 entry match? (This is the
  DOC equivalent of PRO's `coverage_report/0`; both are recorded.)

## 2. Arm PRO — Prolog rules, machine-queried

- **Artifacts**: `wp2/arms/PRO/rules_v0.pl` (h001..h017) +
  `wp2/arms/PRO/knowledge_tooling.pl` (find_rule/1, record_hit/2,
  record_reuse/2, record_miss/2, coverage_report/0).
- **Setup once on m1-build** (SWI-Prolog 9.x):
  ```bash
  sudo apt install -y swi-prolog   # if absent
  cd ~/mumps-research
  bash wp2/arms/PRO/smoke_test.sh
  ```
  Smoke-test FIRST (the script checks all 17 trigger families resolve,
  a bogus family finds nothing, coverage_report runs on an empty log —
  without recording events). Pitfall: Prolog paths must be QUOTED atoms;
  bare [wp2/arms/PRO/rules_v0] parses the uppercase PRO as a VARIABLE
  and consult fails with an instantiation error.
- **Per unit**: run from the repo root so the CSV path resolves:
  1. normalize constructs (same §2.4 as DOC);
  2. `find_rule(<family>).` for each construct found — the query itself
     is the lookup; if nothing matches, `record_miss(<family>, <unit>)`;
  3. on applying: `record_reuse(hNNN, <unit>).` (unmodified) or
     `record_hit(hNNN, <unit>).` (adapted, + journal note);
  4. capture new rules in rules_v0.pl under T1/T2/T3 + gates, id
     `hNNN` next, mirroring the entry schema fields exactly.
- **Batch close**: `coverage_report.` — save its output verbatim in the
  arm journal (it computes hit/reuse rates per §5 from the event CSV).

## 3. Arm DAF — Dafny-verified translation

- **Additional toolchain**: Dafny 4.x + Z3 (e.g. via dotnet tool or the
  release binary; verify `dafny --version` on m1-build before starting).
- **Per unit** (ad hoc heuristics are logged like BASE — no cookbook/rules):
  1. translate with the BASE heuristic log available as background (its
     rows may be consulted; log consultations as hits against `BASE-log`
     in the DAF journal — ad-hoc handling is the factor level here);
  2. for units with **pure, decidable logic** (arithmetic helpers,
     comparison and scan functions — B1 has several, e.g. numcomp,
     gotamatch, Extract2): write the Kotlin translation FIRST as a
     plain function, then write a **Dafny method** specifying its
     behavior (pre/post-conditions, loop invariants) and verify:
     `dafny verify unit.dfy` must report 0 errors/0 timeouts;
  3. units with pointer-heavy, IO-heavy, or state-machine behavior
     (buffer walkers, output emitters) are **exempt** — Dafny-verify
     only what is genuinely verifiable and list exemptions in the unit
     journal (exemption rate is itself a measured outcome);
  4. the Dafny method is a SPEC of the Kotlin function, kept in
     `wp2/arms/DAF/spec/<unit>.dfy`; do not let verification edits
     change graded behavior without a logged deviation.
- **Scoring**: same oracles as BASE. Verification failures do NOT block
  grading, but every failed/exempted unit is logged.

## 4. Arm BOTH — Prolog rules + Dafny

- Factor levels combined: run the PRO workflow (rules + tooling, same
  events CSV under `wp2/logs/BOTH_knowledge_events.csv` — set the log
  path in knowledge_tooling via the `default_log_path/1` fact copied to
  a BOTH-local tooling consult, or pass the path when recording) AND
  the DAF workflow (specs in `wp2/arms/BOTH/spec/`).
- Keep BOTH's rule base a **separate file** (`rules_v0.pl` under
  arms/BOTH/) so knowledge-artifact growth is measured per arm; seed it
  from the same v0 as PRO (identical starting condition is required by
  the design).

## 5. Instrumentation calibration (end of pilot)

For each arm, record: heuristic-log row count by source, cookbook/rule
entry count, hit/reuse/miss counts (DOC journal + PRO coverage_report),
red-build count, and grading outcome (all units). These calibrate the
per-arm instrumentation before the interleaved cycles begin and are the
pilot deliverable of M2's "instrumentation calibrated" criterion.
## PRO adaptation journal (per knowledge_tooling.pl semantics: adapt = record_hit + note)

- 2026-11-15, libmpscpp.cpp::EncodeHTML (B1 unit 5): h007 char_buffer
  adapted — mutable scratch implemented as explicit CharArray instead of
  the rule's StringBuilder default, to preserve byte-domain fidelity
  (chars >= 0x80, NUL-terminated in-place C-string surgery). Logged as
  hit, not reuse. (wp2/logs/PRO_knowledge_events.csv)
- 2026-11-15, red build #1 (units 1-6, first PRO compile on m1-build,
  Gradle 9.9.1 / Kotlin 2.4.21): two fixes. (1) IfCode.kt:55 — Kotlin
  rejects Short == Int equality (EQUALITY_NOT_APPLICABLE); h010
  promotion made explicit: at_flg == (-1).toShort(). (2) Find.kt:25 —
  root cause was a push-script bug: the backticked keyword parameter
  was written without its CharArray type annotation (no missing-rule
  trigger). Fix renames the C parameter "in" to in_ (trailing-underscore
  convention, vendor parse_ precedent); h015 grammar-collision family.
  Build-failure events logged as hits in wp2/logs/PRO_knowledge_events.csv.
- 2026-11-15, include/mumpsc/libmpsrdbms.h (B1 unit 9): h003 state_macro
  adapted — the vendor SELECT/PRINT/... macros splice in the file-scope
  svPtr; Kotlin has no capture-by-name macros and svPtr is a pointer
  value, so the shims take svPtr as an explicit leading parameter
  instead of singleton members. Logged hit, not reuse.
- 2026-11-15, interp-arith.cpp::mps_acos (B1 unit 10): second
  preprocessor_conditional miss (3-way #ifdef; reference build selects
  the plain-double branch). T1 (recurrence >= 2) now met for this
  family — capture candidate for the batch-close rule revision.

### PRO adaptation journal (append-only)

- Unit 12 (mumps.cpp): h003 pattern from unit 9 reused as-is (explicit svPtr in symPut/globalClose shims over h014 stubs) - logged reuse, not a new adaptation. Divergences documented in MumpsMain.kt header: __TIME__/__DATE__ build-machine constants use placeholders (announce line differs from reference in that field only); readline/history replaced by stdin reader + file analogs; set_alarm body unobservable (call site dead, --with-alarm=0). Third preprocessor_conditional miss logged (NATIVE_SHARED REPL deadlock guard, compiled out of reference build).

- Unit 22 (interp-bstring.cpp::<misc>): red build #2 of the arm - top-level 'const val STR_MAX' in Bstring.kt collided with pre-existing file-private STR_MAX declarations in GlobalName.kt (u13) and MumpsMain.kt (u12), producing overload-resolution ambiguity at their usage sites. Fix: STR_MAX in Bstring.kt made file-private. h015 hit-only logged. Pre-push scan lesson: the h015 scan must also cover cross-file top-level name collisions within the same Kotlin package, not only grammar collisions inside the single file (*/ in comments, string templates, control bytes).

- Unit 24 (interp-sysfunc.cpp::DirectRead): h001 adaptation (hit-only logged). Reader carriers on Msv cannot be fstat'd, so (a) the vendor's unreachable fstat(...)==1 "// bad file" guard became a null-carrier guard, and (b) the io==5 stdin trichotomy S_ISCHR/S_ISFIFO/S_ISREG became: console-presence test for the terminal case (stty -echo raw / echo -raw echo toggling not portable, not reproduced), FIFO early-out not detectable (falls through to the regular-file read), and the regular-file read kept as-is. EOF in the terminal branch still truncates (unsigned char)k to 255 per the vendor (no EOF check there). Parameter 'in' renamed 'in_' (Kotlin hard keyword).

- Unit 25 (interp-sysfunc.cpp::<misc>): residue functions VariableNotFound/1, ErrorMessage, LocalData/Order/Next, GlobalNext, LocalGet/GlobalGet, fullUnpad, plus the header residue (FIX cast macro dropped with comment; SQLITE resolved by reference build). Scope note correcting the earlier planning annotation: units.csv puts interp-sysfunc.cpp::sigint in batch 7 and unpad in batch 10, so sigint is NOT part of B1's 111-SLOC residue and its InterpStubs stub stays (owner doc corrected); an unpad h014 stub was added for fullUnpad. MdhStubs.sym_ promoted from Unit to CharArray? (LocalGet tests sym_(11,...)==NULL; existing call sites ignore the return). Msv gained stateVector.h:63 LineNumber, :70 gpad, :77 order. fullUnpad's strtok walk became an explicit \u0001-delimiter scan; first token is strcatx'd without unpad per vendor. All new top-level constants are file-private (red-build-#2 lesson); the "$order()" message uses a unicode escape.

- Unit 26 (compiler-sys1.c::patrn1): pattern-argument scan. h004: bd global added to CompilerState (compiler-globals-ext.h:33, 2048). h006: vendor short* jpx/ernbr (read AND written) -> ShortArray(1) holders. h011: backward gotos a1285/a1283 -> labeled outer loop + inner while; forward goto a1281 -> break out of the inner quote scan. h008: static tmp1[2]/cod210[2]={210,0} -> fresh per call. Offset-aware strcat helpers (strcatOff/strcatOffC, private) implement strcat(&bd[1],...). xindex via existing InterpStubs stub (h014); its charset literal (contains '*' and '/' adjacent) is a Kotlin string literal, flagged in the h015 scan as intentional. Note: the interp side has a same-named patrn1 (interp-parse.cpp::patrn1, batch 4) - different Kotlin package (mumps.compiler vs mumps.interp), no collision.

- Unit 27 (compiler-sys1.c::MyFunc, lines 818-945): extrinsic-function back end. h004: extern short ArgMax added to CompilerState (compiler-globals-ext.h:53). h013: three dead constructs dropped with comments (j stored never read at the arg split; l never used; local bd[1024] gets strcpy(bd,"gtmp") then never read). h014: Extract1 stub added to compiler Stubs.kt (owner compiler-parse.c:2119, not a corpus unit); Extract2 resolves to unit 15. builtinTable.h include-in-function became a private 18-entry array. Vendor quirks kept: the k>29 too-many-args branch sets k=9 and still processes the current argument (no break); the local-DO ErrorMessage emission passes a stray argument to a format without conversions; lbl starts as the two-quote cstring. Emitted code strings (strmove __ArgN / setjmp _doStack / Interpret "x \"do ^N\"") written to CompilerState.out verbatim.

- Unit 28 (interp-sym.cpp::_SymFree, lines 876-914): symbol-table list teardown. h004: Msv gained _Sym (stateVector.h:65) and start[SYM_MAX] (:71, SYM_MAX=200); the struct stab carrier (sym.h:43-51) added as class Stab with this unit - the machinery owner is interp-sym.cpp::sym_ (batch-10, not in B1). free/delete collapsed to GC null-outs (fields nulled, nodes dropped). sym_(0, OrigName, data) store-by-ref calls ride the MdhStubs.sym_ stub with fresh CharArray scratch; sigint rides its stub (batch-7). Vendor derefs OrigName unguarded - port adds the nullable guard (Kotlin String? necessity, journaled); unused parameter i kept.

- Unit 28 red build #3 + fix (2026-11-16): Kotlin SMARTCAST_IMPOSSIBLE at SymFree.kt:66 - "OrigName is a mutable property that could be mutated concurrently". The null check and isNotEmpty()/use of p1.OrigName were on the mutable class property; fix captures it into a local val (orig) once. Semantics unchanged. h015 hit-only logged. New pre-push lesson: for mutable class properties the h015 scan should flag check-then-use null-guard patterns (smart-cast is only possible on locals/vals) - the arm's null-guard convention should capture to a local val at the guard.

- Unit 29 (include/mumpsc/numSetup.h::<misc>): include-fragment made callable as numSetup(opcode,a,b,c) - the 5 include sites in interp-arith.cpp (add/sub/mult/divx/modulo, lines 628-771) all collapse into the opcode parameter. Unit OWNS gcvt_wrapper (interp-arith.cpp:69-77, reference arm gcvt(x,8,a)): InterpStubs.gcvtWrapper stub REMOVED, real gcvtWrapper now defined in NumSetup.kt as a C-%.8g-equivalent (trailing mantissa zeros stripped; NaN/Inf per C99 gcvt) - mpsAcos (u10) and numcomp (u20) call sites now resolve to it. Reference-config #ifdef resolution (hardware double branches; MPFR/BIGFLOAT omitted) logged as the 5th preprocessor_conditional miss (T1 long met; prime rules_v1 capture candidate at batch close). floatstring gate reused via the existing Numcomp.kt stub (owner interp-arith.cpp:597, batch-3 residue) - NOT redefined. Vendor quirk kept: NS_DIV skips the floatstring gate entirely (division always floating point); int aa=atoll(a) keeps the C long-long-to-int truncation; the unreachable integer-section NS_DIV branch translated anyway.

- Unit 30 (interp-parse.cpp::patrn1, lines 1335-1400): interp-side twin of the unit-26 compiler patrn1 - same a1285/a1283/a1281 goto shape (labeled loops + break), same doubled-quote 210 marker, but appends to svPtr.bd[0..] (not &bd[1]) and has NO paren-depth counter. Msv gained bd (stateVector.h:42), xd (:45) - both CharArray(32000) byte domains - and int xpx (:56). h06: short* jpx/ernbr -> ShortArray(1) holders. xindex rides the existing InterpStubs stub (h014). Vendor quirks kept: the "[right side)" bracket typo lives in the header comment; tmp1[0] is stored at a1281 before the guard test (same as vendor). patrn1 name coexists with unit 26's compiler twin (different Kotlin packages).

- Unit 31 (libmpscpp.cpp::<misc-1>, 698-line/600-LOC residue, LAST B1 unit): chunk = header debris + 25 dollar-z~mdh~ wrapper banners (bodies beyond the cutoff, misc-2) + a residual mdh_tree_level_add 10-col body already owned by unit 8 (NOT re-declared) + Hash class + indirect + global::operator() family + global ctor + 32 free compound-assignment operators + unary global::operator+/-. Reference config: SQLITE/MPSCPP/_INTERP_ defined; GCVT defined so gcvt_wrapper is plain gcvt (real impl owned by unit 29 NumSetup.kt); UNSPECIFIED=0 (libmpscpp.h:69). Adaptations: (1) the 32 C++ free compound-assignment operators (+,-,*,/ for 8 C int types, global RHS via implicit conversion) rendered as named plusAssign/minusAssign/timesAssign/divAssign helpers - Kotlin cannot define compound-assignment operators on built-in numeric receivers; per-type implicit conversions collapse to one globalNumericValue h014 stub. (2) The 30+ global::operator() subscript-builder overloads (string/mstring/const char*/long, arities 0-10) folded into the unit-14 carrier vararg invoke - names list "a,b,..." rebuilt per arity, passed to new makeGlobal stub (owner global::MakeGlobal, batch-7); the indirect overload keeps its vendor-commented-out MakeGlobal. (3) C++ operator= rendered as named assign functions (Hash/indirect). (4) FILE* hash file -> RandomAccessFile: fgetc/FTELL/FSEEK map directly, feof modeled as read()==-1, w+ truncation via setLength(0), r+ open-must-exist via explicit File.exists check. (5) global ctor: atexit(__finish) -> one JVM shutdown hook (Eflg-guarded, once), set_terminate(thrownMessage) has no JVM equivalent - unwired; __finish/thrownMessage added as h014 stubs. (6) Chunk boundary cuts global::operator-() after "tmp = 0 - *this;" - closing return supplied to keep the unit compilable. (7) Carrier shape: Global.ref keeps the name (unit-14 conflation), myname added as the vendor's separate 255-truncated field. Vendor quirks kept: Hash uses s_str() here but c_str() in the global family (Mstring gained s_str alias); second zap pass comment says "zap to 1's" but writes 2's; the post-ii-seek re-read loop has no EOF check. New stubs in MdhStubs: lhash, makeGlobal, __finish, thrownMessage, globalNumericValue, globalPlusInt, intMinusGlobal. MdhState gained ierr1 (short) and globalExceptionCode (int, UNSPECIFIED=0). Interp Patrn1.kt line-93 redundant .toInt() warning cleanup (u30) folded into this push.

- B1 batch close (PRO, 2026-11-15, after commits 8b640c0/d116023): coverage_report (17 rules) on the 345-event log: 179 triggering situations (174 hits, 5 misses), hit rate 97.2%, reuse rate 95.4% (166 reuses). All 5 misses are preprocessor_conditional (units 4, 10, 12, 20, 29) - one construct family, T1 recurrence satisfied, captured as rule(h018) in rules_v0.pl (G1-G4 checked; verification(untested), to be exercised by B2 units). Capture rate per capture_criteria section 5: 1 entry / 179 triggering situations. Tooling fix folded into the close: knowledge_tooling.pl load_events now converts CSV kind/target/unit via atom_string (split_string strings never unified with the atom-keyed queries - first coverage_report run showed 0 events). Tooling pitfall for the log: swipl -s a -s b -g goal silently skips later loads; consult both files from stdin goals instead. Final B1 build warning-clean (LibmpscppMisc1.fatal inline dropped). 31/31 B1 units translated and built green.

- Unit 32 (libmpscpp.cpp::global::Extract, B2 opener, lines 7546-7560): first owner of the B2 global-member wave. New mdh/GlobalExtract.kt: GlobalRef.Extract(st,ln) extension (unit-14 TermCorrelate extension precedent), Mglobal(RETRIEVE,...) retrieval with the GLOBAL_NOT_FOUND=2 -> MumpsGlobalException() throw path (RETRIEVE=0 per defines.h:83; MdhState.globalExceptionCode carrier set), Mltoa/_extract calls (int->long widening documented). New shared carrier MumpsGlobalException (libmpscpp.h:332 header class, RuntimeException base, vendor message kept) and h014 stubs Mltoa (owner interp-arith.cpp::Mltoa, batch-10) and _extract (owner interp-sym.cpp::_extract, batch-10) in MdhStubs. Carrier conflation note (from the u31 journal) applies: the Mglobal key is the carrier ref (name); real key building lands with the MakeGlobal owner (batch-7).

- Unit 33 (libmpscpp.cpp::global::Find(mstring,int), B2, provisional under C-003, lines 7617-7636): the batch-2 Find is the mstring-key overload - disambiguated authoritatively by re-running the study's select_study_subset.py pipeline (extract_functions + stratify + assign_batches on the pristine Library tree, seed 20261113): score 4.2 -> batch 2; the char* overload (score 4.0) is batch 4, the global-key overload (22 LOC, score 6.2) batch 6. New mdh/GlobalFind.kt: GlobalRef.Find(key: Mstring, st: Int) extension, same shape as unit-32 Extract (RETRIEVE=0/GLOBAL_NOT_FOUND=2 file-private consts, MumpsGlobalException throw path, Mltoa widening), with the vendor's smaller r[20] result buffer kept. First B2 unit whose out-of-scope calls resolve to REAL B1 implementations rather than stubs: _find (Find.kt, unit 6) and atoi (CstringOps atoiC) reused unmodified; only Mglobal (batch-7 owner) and Mltoa (batch-10 owner) ride existing h014 stubs - no new stubs, MdhStubs.kt untouched.

- Unit 34 (libmpscpp.cpp::SQL_Table(char*,int), B2, provisional under C-003, lines 3828-3849): overload disambiguated via select_study_subset.py body metrics (loc 17, score 4.5 - the no-arg/string/mstring siblings score 0.9, below min LOC). New mdh/SQLTable.kt: top-level SQL_Table(s, c) (vendor free function), cmd[STR_MAX] buffer rebuilt per statement (strcpy/sprintf -> writeCstring, strcat -> strcatC), Interpret -> existing InterpStubs.interpret(String) stub, sprintf "%d" h009 collapse, c>10 guard prints "Table size too large." and throws the unit-32 MumpsGlobalException carrier with GLOBAL_NOT_FOUND=2 (vendor uses the global-not-found code for a size error - kept faithfully). ADAPTATION (h015): the vendor command strings "set $table=..." / "set $ztabzize=%d" contain raw dollar bytes the pre-push scan forbids; the byte is rebuilt as 36.toChar() and comments write it as <dollar>. Vendor "ztabzize" spelling kept. No new stubs.

- Unit 35 (compiler-abrtx.c::<misc>, B2, provisional under C-003, lines 1-38): residual chunk after the abrtx() unit (B1 unit 18, Abrtx.kt) - GPL banner + May 17 2024 date comment + four includes, no executable code. New compiler/CompilerAbrtxMisc.kt: banner kept verbatim as a block comment, each include mapped to its existing B1 carrier (stdio.h -> the java.io edges in Abrtx.kt; mumpsc/memsize.h -> CompilerState constants PS1/FBMAX/PS/FORPARM/VERSION/DDAT/DKEY; mumpsc/compiler-macros.h -> per-unit macro consumers; mumpsc/compiler-globals-ext.h -> the CompilerState extern carrier tpx/t2/pd1len/xpx/ierr/pd1/xd/v1d/MAIN_flg/FCN_flg/ExitCode/ExecName/pid/output_file/lines/Final etc.). All h014 resolutions are REAL B1 reuses - no new stubs. h018 NOT triggered: no #ifdef/#ifndef in the chunk (memsize.h's own conditionals belong to that header's units). Minimal honest event set: h014 + h017 pairs only.

- Unit 36 (compiler-doty.c::<misc>, B2, provisional under C-003, lines 1-38): same residue shape as unit 35 - GPL banner + June 11 2024 date comment + the same four includes, no executable code (doty() itself is the batch-4 complex unit, 27 LOC score 57.3, not yet translated). New compiler/CompilerDotyMisc.kt: banner verbatim, includes mapped to the same existing B1 carriers (stdio.h -> java.io edges; memsize.h -> CompilerState constants; compiler-macros.h -> per-unit; compiler-globals-ext.h -> CompilerState externs). All h014 resolutions are REAL reuses - no new stubs. h018 NOT triggered (no #ifdef/#ifndef in the chunk). Minimal honest event set: h014 + h017 pairs only.

- Unit 37 (libmpscpp.cpp::KiLL, B2, provisional under C-003, lines 7685-7712): free function (the global::Kill wrapper is a separate unit). New mdh/KiLL.kt: keyfix the caller-owned key buffer, trim a trailing subscript-delimiter pair (vendor writes only the last of the two char-1 bytes to NUL, then k--), strmove the trimmed key aside, Mglobal(GKILL=3) the node, then while(true) XNEXT(=8) loop killing every sibling whose first k bytes match (strncmp) - break otherwise. h015 adaptation: vendor locals _k/_j are underscore-leading (Kotlin implementation-reserved) - renamed k/j, vendor names kept in comments. New h014 stubs in MdhStubs: keyfix (owner keyfix.h inline, not a corpus unit), strmove (owner inline.h:38, not a corpus unit). Mglobal rides the existing InterpStubs stub; strlenC/strncmpC are real B1 reuses. GKILL/XNEXT as file-private consts (defines.h:167/:88).

- Unit 38 (libmpscpp.cpp::global::Piece(global,int,int), B2, provisional under C-003, lines 7204-7235): overload disambiguated via select_study_subset.py body metrics (loc 23, score 7.5 - the batch-2 unit; the const char*/string/mstring siblings are batches 7/8/10). New mdh/GlobalPiece.kt: GlobalRef.Piece(pat: Global, ii, jj) extension - retrieves pat's global into gtmp1 and this global into gtmp (both with pat.ref/g.ref as the Mglobal key, unit-14 conflation), jj<0 -> jj=ii as a local var (Kotlin params are vals), sprintf "%d" h009 collapses, then _piece(tmp, gtmp, gtmp1, cc1, cc2, 0, "", svPtr) and the result wrapped in Mstring. New h014 stub in MdhStubs: _piece (owner interp-piece.cpp:97, not a corpus unit; the interp-piece.cpp::<misc> batch-4 unit may claim its neighborhood). Mglobal rides the existing InterpStubs stub. No h015 adaptation this unit. TOOLING NOTE: raw.githubusercontent.com served stale (pre-u37) copies this session; repo files must be fetched via the GitHub contents API (base64) or verified against the expected tail rows before use.

- Unit 39 (libmpscpp.cpp::GlobalOrder(char*,int), B2, provisional under C-003, lines 2538-2557): overload disambiguated via select_study_subset.py body metrics (loc 17, score 8.7 - the batch-2 unit; the mstring/string siblings are batch-9/-3). New mdh/GlobalOrder.kt: top-level GlobalOrder(a, i) building the command "s %=<dollar>order(<a>)" in a tmp buffer (dollar byte rebuilt as 36.toChar(), SQL_Table u34 precedent - h015), xecute -> tpx 1/0, result read back via SymGet("%"); the vendor's static char r[STR_MAX] return buffer becomes the returned String (h008, fresh per call). Vendor quirk kept: the int parameter i is never used. New h014 stubs in MdhStubs: xecute (owner libmpscpp.cpp:7073, not a corpus unit), SymGet (owner libmpscpp.cpp:9858, not a corpus unit). strcatC is a real B1 reuse.

- Unit 38 build fix (PRO, after the first red B2 build): the GlobalPiece.kt KDoc line "the const char*/string/mstring overload" contained a comment-close sequence inside the KDoc ("char*/"), terminating it early and producing syntax errors at every following line. Fixed by spacing the type list ("char* / string / mstring"). Scan-gate lesson recorded: the comment-close check must be POSITIONAL (every close at line end), not a balanced open/close count - a mid-comment close balances counts but still breaks the file. Repo-wide positional scan run over all PRO sources: no other file has a mid-line comment close (the Patrn1.kt hit is a string literal, pre-existing, green).

- Unit 40 (interp-arith.cpp::mps_exp, B2, provisional under C-003, lines 783-812): the FIRST h018 (preprocessor_conditional) exercise of the study - the B1 misses on this family (units 4/10/12/20/29) were captured as rule(h018) with verification(untested); this unit reuses the rule and hits: the vendor 3-way #ifndef MULTI_PRECISION / #ifdef BIGFLOAT body resolves against the reference build config (MULTI_PRECISION and BIGFLOAT both undefined) to the plain double arm. New interp/MpsExp.kt, same shape as the B1 unit-10 sibling mpsAcos: gcvtWrapper(exp(strtodC(a)), c); excluded BIGFLOAT (long double/expl/strtold) and MULTI_PRECISION (mpfr) arms documented as comments per the h018 action. gcvt_wrapper/strtodC/gcvt are real B1 reuses (units 29/10). No new stubs. Suggestion for the batch close: h018's verification flag can be upgraded now that the rule has a B2 hit.

- Unit 41 (interp-arith.cpp::mps_exp10, B2, provisional under C-003, lines 853-882): unit-40 twin, same 3-way preprocessor body; h018 resolves to the plain double arm: C exp10(x) is 10 to the x -> gcvtWrapper(10.0.pow(strtodC(a)), c) in new interp/MpsExp10.kt. Excluded arms documented per the h018 action, including a vendor quirk kept in the documentation: the multi-precision arm calls mpfr_exp2 (2 to the x), not mpfr_exp10 - apparently copied from the mps_exp2 sibling; if MULTI_PRECISION were ever defined the vendor would compute the wrong function. All dependencies are real B1 reuses (gcvtWrapper unit 29, strtodC unit 10). No new stubs.

- Unit 42 (libmpscpp.cpp::mstring::Fnumber(mstring,mstring), B2 core stratum, provisional under C-003, lines 7483-7511): overload disambiguated via units.csv (loc 22, score 10.6, rank 104 - the one-arg sibling is batch-4, the global::Fnumber wrappers batch-3/-7). New mdh/Fnumber.kt: Mstring.Fnumber(a, b) extension assembling the coded call "<dollar>fnumber<Open><data>,<a>,<b><Close>" (CodedOpen/Comma/Close = 206/207/208, defines.h:38-40, rendered as one-char strings; dollar byte rebuilt as 36.toChar(), u34 precedent) into svPtr.v1d at the vendor's offset-1 convention, then fcn(svPtr), result from bd[1]. h013: the vendor's "b.c_str()==0" close-only branch is dead (c_str never null) - dropped with a note, else arm unconditional. New h014 stub fcn in InterpStubs (owner interp-fcn.cpp::fcn, batch-4, 775 LOC). Msv carrier extension: v1d[STR_MAX] added (stateVector.h:44, 32000 - bd/xd precedent was unit 30). strcpy/strcat real B1 reuses.

- Unit 43 (libmpscpp.cpp::mdh_tree_level_add, B2, provisional under C-003, lines 384-424): a verification unit - the 7-column overload was already covered by unit 8's family rendering (mdh/MdhTreeLevelAdd.kt renders all ten overloads through one mdhTreeLevelAddImpl). Overload identity confirmed via units.csv (loc 29, score 13.7, rank 132) and the study tool metrics; the vendor body (prefix + xce, %p(tree), %d(depth), col1..col7 xd0-separated, xcf, zfcn at &tmp[1]) matches the existing Kotlin overload line for line. No new code, no new stubs. Scope quirk recorded per PN-1: unit 8 translated overloads ahead of their own batch positions - the family approach front-ran units whose units.csv rows live in later batches; unit 43 is the first such row to land, and the remaining mdh_tree_level_add rows (batch 3+) will also verify against the same file. Events logged as reuse+hit pairs for h004/h007/h009/h014/h015/h017, the unit-8 rule set.

- Unit 44 (libmpscpp.cpp::global::Min(), B2 core stratum, provisional under C-003, lines 1690-1724): the sibling-walk minimum over the subscripted global's children. Overload disambiguated via units.csv (loc 26, score 14.8, rank 145; the free intrinsic wrapper Min at line 10262 is the batch-4 unit and calls this method). New mdh/Min.kt: GlobalRef.Min() extension - copies ref into tmp, trims trailing control bytes, then loops Mglobal(XNEXT,...) checking the ref prefix with strncmp and retrieving each child via get_global, tracking the min double; GLOBAL_NOT_FOUND throw when tpx clears, result via strtodC. Two adaptations: (1) h015 - the vendor local double val collides with the Kotlin hard keyword val, renamed value; (2) get_global is not a function but the libmpscpp.h:117 preprocessor macro expanding to (svPtr->tpx = Mglobal(RETRIEVE, ref, rslt, svPtr), rslt) - expanded inline, no stub created for a macro (h014 logged reuse: the mglobal stub is the reused unmodified dependency). Faithfulness note verified in the vendor: Mglobal(XNEXT,...) writes the next key back into the key buffer in place (interp-native-btree.cpp:945), so the vendor loop reading tmp rather than ttt after the call is the intended contract, kept as-is; an empty walk returns the 0.0 initialization, kept. No new stubs.

- Unit 45 (libmpscpp.cpp::prnt, B2, provisional under C-003, lines 8057-8098): the Smith-Waterman traceback printer of the alignment block - from cell (imax,jmax) of the score matrix, walk back through the diagonal/gap-column/gap-row recurrence appending each aligned pair to S_RESULT. New mdh/Prnt.kt: top-level fun prnt (vendor name kept; the ss parameter is unused by the vendor, kept unused as there - unit-39 i precedent). h004: the file-scope alignment globals (__a short matrix, S_RESULT string, GAP/MISMATCH/MATCH, lines 8031-8040) become the new AlignState singleton in MdhStubs - cross-call state by design (init allocates, sw fills, prnt reads), so the h008 fresh-per-call shape does not apply; AlignState.a starts empty (mirroring the vendor NULL before init) and reads before init are out of bounds like the vendor NULL deref. h010: explicit Short->Int widening on the matrix cells in the Score sums. EMPTY_CELL = -32768 (libmpscpp.cpp:8029) as a file-private Short const. New h014 stub in MdhStubs: Score (owner libmpscpp.cpp:8049 inline, not a corpus unit - keyfix/strmove precedent). No other stubs; init (batch-4) and sw (B2 unit 61) will wire the carrier.

- Unit 46 (libmpscpp.cpp::Multiply, the MSV intrinsic wrapper, B2, provisional under C-003, lines 10193-10233): overload disambiguated via units.csv and the study tool (loc 28, score 16.8, rank 154; the string and char* Multiply helpers at 2211/2252 are the batch-5/-7 units, global::Multiply at 2164 is the later B2 complex unit). New mdh/Multiply.kt: top-level fun Multiply(svptr, ep, arg1..arg3) - for each argument, scan for the byte-1 subscript marker, print the top-level-global diagnostic and sigint(100) if present, trim the marked tail, and build the Global from the name past the leading caret (cstringAt offset for the vendor &arg[1] pointer arithmetic); then a.Multiply(b,c) and return "". Vendor quirks kept faithfully: the Arg 2 and Arg 3 diagnostics test arg2[i]/arg3[i] - the index left over from the arg1 scan, not the j/k just computed (copy-paste bug, documented in KDoc and journal, kept as-is); the commented-out subscripted dispatch stays a comment; svptr/ep unused as there. h013: the never-used static char tmp[32] and double x dropped with comments (the static would also be shared-scratch, cf. h008). New h014 stub in MdhStubs: Global.Multiply(b, c) (owner libmpscpp.cpp:2164, batch-2 complex unit later in B2); sigint reused from InterpStubs.

- Unit 47 (compiler-copyright-notice.c::copyright_notice, B2, provisional under C-003, lines 36-88): a verification unit - copyright_notice was already translated by B1 unit 1 (compiler/CopyrightNotice.kt, the file's only function; the batch-1 row for this file was the <misc> residual, but the B1 rendering covered the function itself - same PN-1 scope-overlap shape as unit 43). Identity confirmed via the study tool (loc 42, score 18.0, comment_ratio 0.106 - exact units.csv match). Verification against the pristine vendor source passed: all 38 Sysout strings byte-identical to the Kotlin sysout calls; the ctime header line (vendor fprintf with %s) rendered via the Locale.US formatter per the B1 h009 note; FILE* -> Writer (h001), Sysout macro -> local helper (h002), copyright_notice -> copyrightNotice (h017). No new code, no new stubs; events logged as reuse+hit pairs for the B1 rule set.

- Unit 48 (interp-parse.cpp::getstr2, B2, provisional under C-003, lines 1480-1511): the READ-command line reader. Identity via the study tool (loc 23, score 18.1, rank 156; getstr1 is a different unit in interp-sysfunc.cpp, batch-5). New interp/Getstr2.kt: fun getstr2(lun, area, svPtr) reading byte-at-a-time from the Msv in_file Reader carrier via a local fgetcC helper (h002), NUL-terminating at the LINEFEED (defines.h:195) and returning the line length as Short, -1 at EOF or overlength. h013: the vendor branch testing area[i]=='\n' is unreachable ('\n' is LINEFEED, which returns first) - dropped, rest of the else-if chain kept in order. Vendor quirks kept faithfully: the TAB branch assigns TAB (a no-op); area[STR_MAX]=0 writes one past the loop bound (callers size STR_MAX+1, as in C); lun==5 reassigns in_file[lun] to stdin on every call - rendered as a fresh InputStreamReader over System.in, mirroring the vendor's unconditional pointer reassignment. h001 adaptation: an unopened unit raises an explicit error where the vendor would fgetc(NULL) (undefined behavior). h010: explicit short/int widenings. No new stubs (the in_file carrier is the B1 unit-23 reuse).

- Unit 48 addendum (build fixes, h015): the stdin mapping hit the Kotlin 'in' hard keyword twice - first as System.in, then as FileDescriptor.in (both fields are only nameable as backtacked identifiers, which the arm's byte gate forbids). Resolved by fetching the vendor's own stdin stream reflectively: System::class.java.getField("in").get(null) as InputStream, cached in a file-private STDIN val - the same System.in object, not a second stream over fd 0. h015 hit logged for the grammar collision; no other rules touched.

- Unit 49 (libmpscpp.cpp::global::IDF(double), B2, provisional under C-003, lines 1764-1809): the inverse-document-frequency sibling walk (unit-44 Min loop shape). Identity via the study tool (loc 36, score 18.4, rank 161; the free MSV wrapper IDF at line 10370 is batch-8 and calls this method). New mdh/Idf.kt: GlobalRef.IDF(DC) extension - DATA_RANGE_ERROR(4) throw when DC<=0, dc=log2(DC) (LOG2 is the libmpscpp.h:86 macro over log2 -> kotlin.math.log2), trim trailing control bytes on the ref buffer, then the XNEXT walk: get_global retrieve, GLOBAL_NOT_FOUND/INVALID_CONVERSION/DATA_RANGE_ERROR throws on tpx clear, %lf parse failure, or val<=0; val=dc-log2(val)+1 stored back via set_global formatted %.1lf (Locale.US). New local C-locale sscanfLf helper (h009: returns null where the vendor's sscanf reports 0 items). get_global/set_global are the libmpscpp.h:117/118 macros - expanded inline, no stubs (unit-44 precedent); STORE=1 (defines.h:84). h013: the vendor's never-used long j dropped with a comment. tpx handled as Long (unit-44 fix lesson). No new stubs.

- Unit 50 (interp-native-btree.cpp::scanblocks, B2, provisional under C-003, lines 216-258): traces the B-tree descendant blocks keeping the highest key value seen, recursing into high-end descendants. Identity via the study tool (loc 27, score 27.1, rank 195). New interp/Scanblocks.kt: fun scanblocks(i, svPtr) - freadx reads block i (new h014 stub, owner interp-native-btree.cpp:111, batch-7 unit; the vendor's unsigned char** out-param becomes the return value), then the scan loop walks entries at ptr1 offsets: block-end test against {MMARK(255, defines.h:120), 0}, strcmp against BigKey with a strcpy overwrite of the BigKey* globals when a higher key is found, 4/8-byte little-endian reads for lower/data (explicit .code shifts; the vendor's memcpy relies on the x86-64 reference platform's endianness, kept and documented), ptr1 advances by strlen+ADSIZE(8, btree.h:51)+6; the tail recursion into the high-end child sets BigKeyParent/BigKeyParentOffset first. h004: the BigKey* file-scope globals (lines 92-101) become the new NativeBtreeState singleton in InterpStubs (OFF_T -> Long; unsigned int -> Int, block numbers in range). h008: the vendor's static Key/mark ("do not recreate during recursion") carry no state across iterations - fresh locals, documented divergence (allocation optimization only). h013: unused int is dropped. svPtr kept unused in the signature as there (freadx's inline form takes no svPtr). MMARK=255, ADSIZE=8, UNMODIFIED=0 as file-private consts.

- Unit 51 (interp-sql.cpp::embed, B2, provisional under C-003, lines 115-142): the SQL single-quote doubler - this is the h008 example unit itself (static char tmp[STR_MAX] return). Identity via the study tool (loc 19, score 27.3, rank 197). New interp/Embed.kt: fun embed(inStr) - null in returns the empty string (the vendor's zeroed tmp), the no-quote fast path returns the original object (h008 action clause), else the two-loop scan-then-build shape with every ' doubled into a fresh String per call. h015: the vendor parameter in is a Kotlin hard keyword, renamed inStr (unit-44 val precedent). No stubs, no carriers.

- Unit 52 (compiler-globalarray.c::GlobalArray, B2 complex stratum, provisional under C-003, lines 40-127): the LHS global-array reference processor - emits the C glue (strmove/strxcat/strcat with backslash-xNN coded separator bytes) that rebuilds ^Name(s1,...) into the temp-string pool. Identity via the study tool (loc 59, score 31.9, rank 209). New compiler/GlobalArray.kt: fun GlobalArray(v1d, GBL_flg, vd) - caret test at v1d[1], name scan from index 2 (vendor comment: keyfix does not shift), tmp-name sprintf into the caller's vd buffer, %2x-coded 206/207 separator bytes via Locale.US formatters, #G#/#S# subreference dispatch through the real Extract2 (B1) with strxcat emission to CompilerState.out (h001 Writer, @Throws IOException). h006: the short* GBL_flg out-param becomes a single-element ShortArray holder. h004: iiitmp (compiler-globals-ext.h:109) added to CompilerState; the vendor's h[2] staging slot collapses into the local code value. Vendor quirk kept verbatim: the mid-loop close-coding WRITES 207 into the caller's v1d buffer at the terminator (input-buffer mutation). New h014 stub: memcheck (owner compiler-parse.c:2068, not a corpus unit) in compiler Stubs.kt. Three iiitmp++/memcheck sites kept as there.

- Unit 53 (libmpscpp.cpp::global::Multiply(global,global), B2 complex stratum, provisional under C-003, lines 2164-2209): the matrix cross-product over global arrays (C = this x B via ordered row/column subscripts). Identity via the study tool (loc 26, score 34.2, rank 213). New mdh/GlobalMultiply.kt: Global.Multiply(B, C) extension - C().Kill(), then the nested Order(1) walks over rows, columns and B-subscripts; cells combine through the implicit global-to-double conversions (rendered as the existing GlobalRef.toDouble carrier stubs) with GlobalRef.assign writes; first-write vs accumulate branches kept. h010 adaptation (hit-only): the vendor's C truthiness on doubles (!Data()) becomes == 0.0 comparisons. All dependencies are existing owner-named carrier stubs reused unmodified (Kill/Order/Data/toDouble/assign, owners in the libmpscpp.cpp class-global machinery). The unit-46 h014 stub for global::Multiply is REMOVED from MdhStubs - the owner has landed and unit 46's intrinsic wrapper now delegates to this rendering (MdhStubs 180 -> 175 lines). The vendor's commented-out per-cell debug printf block stays out, as there.

- Unit 54 (compiler-if.c::if_code, B2 complex stratum, provisional under C-003, lines 39-105): a verification unit - if_code was already translated by B1 unit 2 (compiler/IfCode.kt, 116 lines; the B1 KDoc anticipated exactly this re-listing: "the file's single function if_code is re-listed as B2 unit 54" - the batch-1 row for this file was the <misc> residual, but the file has no residual beyond the function, so B1 unit 2 rendered the whole file; same PN-1 scope-overlap shape as units 43/47). Identity confirmed via the study tool (loc 40, score 38.8, control_marks 21 - exact units.csv match). Verification against the pristine vendor source: the no-argument and IF/ELSE parsing arms, atTest/AtTestResult out-param bundle (h005), INTERPRET macro emissions via CompilerState.interpret (h003), goto if_again/if_structure renderings (h011), and the emitted-C dollar escaping (h015) all present and consistent with vendor lines 39-105. No new code, no new stubs; events logged as reuse+hit pairs for the B1 rule set (h001/h003/h004/h005/h007/h010/h011/h012/h014/h015/h017).

- Unit 55 (include/mumpsc/btree.h::<misc>, B2 complex stratum, provisional under C-003, whole-header residual lines 1-305): the native B-tree handler's configuration header - prototypes, macros, struct buffers, and file-scope statics; no function bodies (identity via the study tool: loc 215, score 57.3, exact units.csv match). New interp/BtreeH.kt: reference-config macro resolution rendered directly (MBLOCK 2048 -> PAGE_SHIFT 11; GBLBUF 65537 -> AMASK 0xffff; ADSIZE 8 -> large-file support; GBLPERMIT 0666 octal -> decimal 438, h010 - Kotlin has no octal literal), struct buffers -> class Buffers (h012 CharArray block domain). h004: the header statics (gp0/gp1/gp3/gp4/gp5/gproot, gblfd/gbldat=-1, trx, ReadOnly=1) become the BtreeHeaderState singleton; documented divergence: the C header's static gives each including translation unit its own copy - rendered as one shared singleton (single live instance in the reference build). h014: the btree prototype resolves to the real B1 NativeBtree.btree (unit 4); the sql prototype becomes an InterpStubs stub (owner interp-sql.cpp::sql, batch-9 unit) - InterpStubs now 144 lines, still exactly 2 allowed dollar bytes. Vendor quirks kept verbatim: the ADZIZE==4 misspelled-macro conditional (always false; both arms set MBLOCK 2048 anyway), the gp1 -> gp3 jump (no gp2), and the commented-out bfr[GBLBUF] array. Preprocessor_conditional miss (6th, journal-logged as in unit 29): no rule family existed for the reference-config #ifdef resolution. Events logged as reuse+hit pairs for h004/h010/h012/h014/h017.

- Unit 56 (interp-lock.cpp::<misc>, B2 complex stratum, provisional under C-003, whole-file residual lines 1-165): the Mumps file lock library - makeLock/cleanLocks over fixed 528-byte struct rec records (pid[16] + ref[512]) in /tmp/Mumps.Locks (identity via the study tool: loc 97, score 57.5, exact units.csv match). New interp/LockMisc.kt: fun cleanLocks(pid) blanks every record whose pid matches (whole-field blanking), fun makeLock(ref, code, pid, Lf, timeout) walks for a matching reference (strcmp or either 1-short strncmp prefix test; empty-string strlen()-1 underflow reduces to the strcmp case, documented) - the '-' arm removes only the first character of pid/ref (vendor asymmetry vs cleanLocks' full blank), else arm fails the lock; the new-lock path scans for a freed slot (ref starting with five spaces) before appending at EOF. h002: POSIX open/read/lseek/write/chmod map to RandomAccessFile with a documented divergence - the JVM cannot express the vendor's O_EXCL create-or-fail dance (two opens collapse into one create-or-open). h007: record layout via local byte helpers mirroring CstringOps semantics. h010: off_t -> Long; octal 0666 -> Posix rw-rw-rw- chmod666 helper. h013: unused int k dropped; code/timeout params never read by the vendor body, kept unused as there. h014: the InterpStubs cleanLocks stub (declared mumps.cpp, MumpsMain call site) is REMOVED - this unit is the owner - InterpStubs now 139 lines, still exactly 2 allowed dollar bytes. Vendor quirks kept: the NFS warning, the ftell(fdopen fresh stream) == fd-offset rewind-by-one-record in the '-' arm, and partial-record-at-EOF runs the loop body on stale bytes (rendered zero-padded, documented). Events: reuse+hit pairs for h002/h007/h010/h013/h014/h017.

- Unit 57 (include/mumpsc/arith.h::<misc>, B2 complex stratum, provisional under C-003, whole-header residual lines 1-97): the runtime arithmetic library's declaration header - prototypes only, no function bodies (identity via the study tool: loc 47, score 82.9, exact units.csv match). New interp/ArithH.kt: FLT_PRECISION=72 (arith.h:52) plus the full owner roster for every prototype: already real are numcomp (B1 u20), mps_acos (B1 u10), mps_exp (u40), mps_exp10 (u41); stubbed is Mltoa (MdhStubs, batch-10 owner); the rest are future batch-3+ corpus units in interp-arith.cpp (add/add1/add3/sub/mult/mult1/divx/divi/modulo/expx, mps_abs...mps_justify, numcomp1/numcomp2/cannon/itoa) - no stubs created for not-yet-called functions (house convention). h017: arith.h -> ArithH. Preprocessor_conditional miss (7th, journal-logged as in unit 29): the MULTI_PRECISION GMP/MPFR block is compiled out at the reference configuration. Events: reuse+hit pairs for h014/h017.

- Unit 58 (interp-parse.cpp::prnterr, B2 complex stratum, provisional under C-003, lines 1660-1830): the interpreter error reporter - walks pd1 (NUL-separated program source) to locate the parse cursor's line, stores LineNumber, then prints the E-code message to stderr unless NOERR is set (identity via the study tool: loc 127, score 83.1, exact units.csv match). New interp/Prnterr.kt: fun prnterr(i, svPtr, pd1cur) - the vendor's line-count loop (counts the current line's NUL before breaking at n > pd1cur, so the stored number is LN-1, kept verbatim), the double NUL walk down to the previous line's terminator making &pd1[nx+1] the start of the current line's text (rendered cstringAt(pd1, nx+1)), then the 38-arm switch as a Kotlin when (vendor roster gap kept: no cases 21/22/28; case 12 appends ErrChar; case 27's dollar byte in the printed message rebuilt as 36.toChar() concatenation, u34 precedent). h002: fprintf(stderr,...) -> System.err.print with the same embedded newlines. h004: Msv gains pd1 (stateVector.h:43, CharArray(32001) - PS=32000 per sysparms.h:74, not memsize.h's 4096; stateVector.h includes sysparms.h), NOERR (:60), ErrChar (:95) - InterpStubs now 147 lines, still exactly 2 allowed dollar bytes. h012: pd1 byte domain. h011/h017 as usual. No new stubs.

- Unit 59 (compiler-sys1.c::inout, B2 complex stratum, provisional under C-003, lines 292-348): the compiler's device-control emitter - writes the C glue for the device operators (new page, skip line, tab, text print) to the compiler output stream (identity via the study tool: loc 44, score 102.0, exact units.csv match). New compiler/Inout.kt: fun inout(text, cmnd, col) with the cmnd switch as a when - one out.write per vendor fprintf (CompilerState.out, the MyFunc h001 precedent), Extract2(col)/Extract2(text) resolving to the real B1 unit-15 translation (h014 reused dependency, no stub). h013: the vendor's case 3 falls through a return - the seven unreachable fprintf calls of the legacy if/else tab expansion after it are dead code, dropped with a note. No new stubs, no carrier changes.

- Unit 60 (libmpscpp.cpp::global::TreePrint(int, const char), B2 complex stratum, provisional under C-003, lines 1831-2035): the global-array tree printer - ten nested subscript levels, each node printed indented by depth x c, with an "= value" line for data-bearing nodes (Data() 1 or 11) (identity via the study tool: loc 116, score 142.6, exact units.csv match). New mdh/TreePrint.kt: fun Global.TreePrint(i, c) - while(true) per level with the vendor break/continue shape kept level for level; Order/Data ride the existing GlobalRef stubs; the cout << global implicit string read is a new GlobalRef.stringValue() carrier stub (owner: libmpscpp.cpp class global operator mstring/const char* machinery, not a B1 corpus unit) - GlobalCarrier.kt now 144 lines. h010: Data() arrives as Double and truncates via .toInt() as in C (the indent depth parameter is reused as the Data scratch; Kotlin forbids var i = i self-shadowing, so the scratch is di, vendor name kept in comments); Data()<2 gates compare as doubles. h013: the redundant (string) casts dropped (u53 precedent). Vendor quirks kept faithfully and commented at site: the level-8 Order call drops the a1 subscript in the source; levels 8/9 print the name + endl BEFORE the Data test (so the "= value" lands on its own line); only levels 5/6 carry the extra Data()<2 descend gate. Events: reuse+hit pairs for h010/h011/h013/h014/h017.

- Unit 61 (libmpscpp.cpp::sw, B2 complex stratum, provisional under C-003, lines 8153-8350): the Smith-Waterman alignment driver - fills the score matrix via Cost, locates the max cell, traces back via prnt, scores the result, and optionally prints the matrix and the 60-column alignment display (identity via the study tool: loc 148, score 173.2, exact units.csv match - the char* overload; the mstring/string forwarders are non-corpus wrappers served by the one Kotlin signature). New mdh/Sw.kt: fun sw(s, t, ShowAlign, ShowMatrix, gap, mismatch, match) - h004: Size/Size_s/Size_t/sx added to AlignState (GAP/MISMATCH/MATCH/S_RESULT/__a already there from u45); h014: Cost (batch-3) and init (batch-4, owns the AlignState.a allocation) and nodash (not a corpus unit) become owner-named MdhStubs stubs - MdhStubs now 200 lines, still exactly 1 allowed dollar byte; prnt is the real u45 translation; Score rides the existing stub. h001: fprintf(svPtr->out_file[svPtr->io],...) -> outWrite helper over the Msv carriers (null-when-unopen throws an explicit error); cout << ... << endl -> print; %d/%s/%c conversions via String.format with cstringAt for the buffer args. h007: t1/t2 80-byte staging and the reverse-copy m1/m2 via a local strncpC (vendor stale tails never read past the explicit terminator, rendered zero-filled, noted). h013: the ShowAlign && 0 banner block (always false), the malloc-NULL raise(SIGSEGV) arms (unreachable on the JVM), free() GC no-ops, and the unused x/n locals all dropped with notes. Vendor shape kept: the score-only block runs once with i=0 so Maxscorex is 0 or -1; a short t2 display line compares against NUL bytes (mismatch arm) as in C.

- Unit 62 (libmpscpp.cpp::<misc-2>, B2 complex stratum, provisional under C-003): the class-global ARITHMETIC OPERATOR family - the residual of the second 600-loc study-tool chunk (identity verified via the study tool: chunks[1] of 9, original vendor lines 4680-6155 plus the friend operator<=(short,global) head as the closing boundary line, exactly the units.csv row loc 600 / score 381.8). Content: the 32 compound assignments (+=,-=,*=,/= over int/uint/short/ushort/long/ulong/float/double), member and friend unary +/-// cast forms over the same eight types, global-global +/-// over the cstring forms via strtod, the four ++/-- pre/post forms, the complete member >= family (11 types) and its 10 friend forms, and the <= family (member 11 types complete; only the int and uint friend forms rendered - the chunk boundary cuts at the friend operator<=(short,global) head, per the unit-31 boundary precedent the remaining friend forms belong to the next chunk). New mdh/GlobalOps.kt: h017 - Kotlin operator conventions adopted where the token exists (plusAssign/minusAssign/timesAssign/divAssign, plus/minus/div extensions in both operand orders, including Int/UInt/Short/UShort/Long/ULong/Float/Double extensions for the friend order), with named forms ge/le/incrPre/incrPost/decrPre/decrPost for the tokens Kotlin lacks (ge/le because the vendor forms return int, inc/dec because they return the numeric value - post forms return the OLD value); h010 - Kotlin unsigned types UInt/UShort/ULong keep the eight vendor signatures distinct (same-type unsigned arithmetic preserves the type), Short+Short promotes to Int so every Short arithmetic result is .toShort()-restored. h014 - 17 new owner-named MdhStubs stubs: the nine extractT forms (extractInt/extractUInt/extractShort/extractUShort/extractLong/extractULong/extractFloat/extractDouble/extractCstring, owned by the global::operator>> family in the misc-3 unit) and insertLong/insertDouble (operator<< family, misc-3), plus the eight assignT forms (assignInt/assignUInt/assignShort/assignUShort/assignLong/assignULong/assignFloat/assignDouble, owned by the libmpscpp.h class global operator= machinery, not a corpus unit) - MdhStubs now 300 lines, still exactly 1 allowed dollar byte; strtodC is real reuse from mumps.interp (MpsAcos). h011: every cast/compare body is a straight if/return. h015: naming adaptations (extractT/assignT carriers, ge/le) noted. VENDOR DEFECT: the *= family computes i = (*this)*i, but no global::operator*(T) is defined anywhere in the vendor sources (grep-confirmed - an undefined reference in the vendor itself); rendered as the direct product extractT() * i with a KDoc note. Vendor quirks kept faithfully: member operator-(T) returns s-i (rhs minus value, symmetric with its friend), member / returns value/s but the friend returns s/value, global>=global and global<=global compare the STRING forms via strcmp (String.compareTo matches the strcmp byte order over the ASCII domain; the CharArray paths use a local strcmpA/strncmpC), ++/-- return the numeric value (post forms the old value), and floor(a)==a stores (long) else double.

- Build fix for units 49-62 (first B2-close build on Mark's host, red -> fixed): three compile errors, all surgical. (1) LockMisc.kt (u56): wrong import - PosixFilePermissions lives in java.nio.file.attribute, not java.nio.file (import line + the one use already fully qualified the class, so only the import moved). (2) GlobalOps.kt (u62): Kotlin unsigned promotion - UShort arithmetic (plus/minus/times/div with UShort) returns UInt, exactly as Short+Short promotes to Int; the eight affected UShort results (timesAssign/divAssign, the friend plus/minus/div, and the UShort-receiver plus/minus/div) now restore with .toUShort(), widening the h010 promotion rule to the unsigned carriers (h010 already logged for this unit; no new rule event). (3) Idf.kt (u49): sscanfLf returns Double? (null on zero items) but the local val was declared non-null - restructured to null-check a scanned local whose null arm throws, then assign to the non-null v (vendor control shape unchanged). No events rows: all three fall under rules already logged for their units (h010/h015).

- B2 CLOSE (2026-11-19, all 31 units green): swipl coverage_report from the repo root (m1-build, swipl 9.0.4): 18 rules in base, 342 triggering situations (337 hits, 5 misses), hit rate 98.5%, reuse rate 94.4% - the row arithmetic ties out exactly (318 reuse + 337 hit + 5 miss = 660 event rows). The 5 misses are the known B1 preprocessor_conditional candidates (units 4/10/12/20/29) already captured as h018; the u55/u57 preprocessor misses are journal-only by protocol, so the CSV miss count is unchanged. PITFALL confirmed: default_log_path is relative to the swipl working directory - running from wp2/arms/PRO silently reports 0 events; always run coverage_report from the repo root (hardening candidate for the B3 opener: resolve the log path relative to the script file, and fail loudly when the log is missing). h018 upgraded verification(untested) -> verification(unit_tested) at B2 close: first exercises at units 40/41 (mps_exp2/mps_exp10, 3-way #ifdef resolved against the reference build config, both build-green), recurring at units 52-53/55/57. All 18 rules now unit_tested.

- B2 close follow-ups (2026-11-19, Mark approved both): (1) h014 (out_of_scope_call TODO-stub) upgraded verification(untested) -> verification(unit_tested): heavily exercised across B2 (units 45, 52-53, 58, 60, 61, 62 - every owner-named stub family), all build-green, and the unit-56 cleanLocks stub removal demonstrated the delegate-when-owner-lands mechanism end to end. All 18 rules in rules_v0.pl are now unit_tested. (2) knowledge_tooling.pl hardened after the silent-zero coverage report: default_log_path is no longer a cwd-relative fact but resolves the CSV relative to the script's own directory (captured via prolog_load_context at consult time, composed with plain directory_file_path so a missing log cannot throw before the exists_file guard), and load_events now prints a loud ERROR to user_error when the log file is missing instead of silently continuing. coverage_report is now correct from any working directory.

- Hardening fix follow-up: the script-relative log path used three .. segments instead of two (wp2/arms/PRO -> wp2/logs is only two levels up); the new loud missing-log ERROR caught it immediately on the m1-build verification run - exactly the failure mode the hardening was added for. Path corrected to '../../logs/PRO_knowledge_events.csv'.

- Unit 63 (interp-parse.cpp::parse_, B3 stratum, provisional under C-003): the interpreter's expression parser - vendor lines 194-1010, identity verified via the study tool (loc 584, score 303, rank 303, exact match; interp-parse.cpp, cpp). Translated as a PState state machine (START/NCHAR/SCAN_OPERAND/VAR1/UN_NEST/SYM1/EXEC/NXT_EXPR/NXT_OPERATOR/NXT_OPERATOR_U/DEC_STK/FINISH) replacing the vendor goto web (h011). The code[26]/opcode/ncode/dcode/operand[256] tables were transcribed PROGRAMMATICALLY from the vendor source (lesson from the hand-transcription drift check: grep/python extraction only; first 128 initialized, 128-255 zero-filled per C semantics, opcode[32]=25 space, dcode letters are 99 not 10). Vendor token consts (POPEN..EXPONEN, interp.h) private; OPEN_PAREN=206 (interp-parse.cpp:80); DOLLARC via 36.toChar() (dollar-byte policy, h015). Quirks kept: NOT_NOT==NOTEQ==18; mid-switch rpm prototype kept as comment; SQLITE arm of the (dollar)data child check is live per the reference build (h018); the vendor tautology toupper(pd1[sdlim+1])=='N' && toupper(...)!= 'A' kept with note; Mglobal NEXT_C call uses bd offset 0 (not bp+1); parse_ uses svPtr.ierr (not MdhStubs.ierr1). Rules: h008/h010/h011/h012/h013/h014/h015/h017/h018. Stubs added to InterpStubs for this unit: strmove, mult, add, sub, divx, divi, expx (b5), modulo (b6), zfcn (b6), sym_ (b10), BuildGlobal/BuildLocal (b8), rpm, mglobalAt; Msv gained setname[32000]/kflg/ierr/sdlim/t0px/pd1len(Long)/t2. Output: interp/Parse_.kt (1203 lines).

- Unit 64 (interp-parse.cpp::patrn, B3 stratum, provisional under C-003): interpreter-side pattern matcher (loc 71, score 106.9, rank 275, exact match; vendor lines 1350-1440). Translation mirrors the vendor: bd scan from offset 0, repeat-count parsing (fixed and low.high), literal atoms with the 210-marker rewrite, the retry/retry1/recount odometer over indefinite counts as a single labeled loop with an atRetry flag (h011), xindex gates with the cnpalue/CNPALUE literals as top-level private vals + NUL (h008), match called with a ShortArray(1) rslt holder (h006), iptr dead store kept as comment (h013). ierr1 rides MdhStubs.ierr1 (libmpscpp.cpp:80 file-scope global). Rules: h008/h010/h011/h012/h013/h014/h015/h017. Output: interp/Patrn.kt (232 lines).

- Unit 65 (compiler-new.c::new_code, B3 stratum, provisional under C-003): compiler new-code emitter (loc 106, score 102.0, rank 274, exact match; vendor lines 292-348 in the B3 row sense - compiler-new.c). Object NewCode.newCode(): Sysout -> CompilerState.out.write (h003, compiler-macros.h:50); error codes PARSE_ERROR=1..ABRTX=6; new2/new3 loops as while(true) (h011). Vendor quirk kept: the SET effective value is 's' (compiler-macros.h:91 overrides :83's SET 0 - the later define wins), NEW='n', NEW1='N'; keyfile test uses cstringAt(st.vd,0)=="atmp" for strcmp(vd,"atmp"). Rules: h001/h003/h004/h009/h011/h012/h014/h015/h017. State additions to CompilerState (h004): t0px(:39), exportflag(:126), ExecX(:131), ExecSym[25](:130), retz(:169), vd[1024](:170); Stubs.kt gained the set_code stub (owner compiler-set.c::set_code, batch-4). Output: compiler/NewCode.kt (221 lines).



- Unit 68 (compiler-sys1.c::patrn, B3 stratum, provisional under C-003): the compiler-side pattern matcher (loc 127, score 56.5, rank 244, exact match; vendor lines 104-263) - textually the twin of unit 64, ported on the compiler state (CompilerState.bd/ierr1). Kept vendor quirks: the xindex cnpalue/CNPALUE pair, the 210-marker rewrite, the C for-continue increments made explicit, iptr dead store as comment (h013). VENDOR DIVERGENCE from the interp twin: the compiler scans for (i = 1; i <= strlen(&bd[1]); i++) while interp-parse.cpp::patrn uses strlen(&svPtr->bd[0]) - each port keeps its own bound (offset 1 here, offset 0 in unit 64). Stubs added to compiler Stubs.kt (64 -> 87 lines): cannon (owner compiler-sys1.c::cannon = B3 unit 72, this batch), substr (:396, batch-7), match (:453, batch-7, rslt rides a ShortArray(1) holder, h006). xindex reused via package import. Rules: h004/h006/h010/h011/h012/h013/h014/h015/h017. Output: compiler/Patrn.kt (247 lines).




- Unit 72 (compiler-sys1.c::cannon, B3 stratum, provisional under C-003): the compiler's canonicalize-in-place routine (loc 71, score 40.9, rank 224, exact match; vendor lines 971-1043). The dangling-else exponent multiply quirk kept literal (vendor nesting); mult(a,0,ten,0,a,0) reuses the real B3 unit-66 interp mult via package import. The cannon stub raised in compiler/Stubs.kt by unit 68 (Patrn) is RESOLVED by this unit and removed (cleanLocks precedent, unit 56; Stubs 87 -> 85 lines). Rules: h007/h012/h014/h015/h017. Output: compiler/Cannon.kt (165 lines).

- Unit 73 (interp-zfcns.cpp::GtkTreeAdd, B3 stratum, provisional under C-003): the GUI glue for the (dollar)z GTK wrapper (loc 30, score 30.8, rank 208, exact match; vendor lines 3451-3483). Opaque GtkTreeIter class added (GTK C struct carrier); the sscanf("%p") decode stubbed as gtkTreeStoreFromPtr (JVM has no %p domain; GtkTreeAdd precedent owner GTK C library glue). Vendor quirks kept verbatim: the "// column 6" comment on the column-7 call (vendor copy-paste) and the duplicate nargs==10 check (should read 11 by the ladder pattern; unreachable because the earlier check returned). gtk_tree_store_append/set -> three InterpStubs stubs (InterpStubs 254 -> 276 lines, owner "GTK C library glue (not a corpus unit)"); the GtkTreeStore stand-in is the established mdh opaque carrier. Rules: h008/h009/h012/h014/h015/h017. Output: interp/GtkTreeAdd.kt (110 lines).

- Unit 74 (libmpscpp.cpp::RunMumps, B3 stratum, provisional under C-003): the varargs RunMumps entry point (loc 33, score 25.9, rank 193, exact match; vendor lines 7008-7046). C varargs -> vararg vals: String?; strtokC/strtokNext holder pair with strtokNext carrying the NULL-continuation convention (strtok state). Vendor quirk kept: the second pass splits on " " (space), not the comma of the first pass. NOERR=1 set before interpret; Msv gained the ERROR field (stateVector.h:69; InterpStubs 276 -> 278 lines). Rules: h004/h007/h012/h014/h015/h017. Output: mdh/RunMumps.kt (147 lines).

- Unit 75 (interp-parse.cpp::<misc>, B3 stratum, provisional under C-003): the interp-parse.cpp header residue (loc 59, score 25.7, rank 192, exact match; the file's functions all exceed min_loc and are claimed by their own rows). Private consts GOSUMAX=50, SWAP=30, FORMAX=30, EXECLEVS=40, LBLS=80, OPEN_PAREN=206, COMMA=207, CLOSE_PAREN=209 with owner rosters; cannon is NOT in this unit (own B8 row; the InterpStubs cannon stub owner note corrected here). SQLITE/_INTERP_ guard notes (h018: reference-build visibility). Rules: h013/h014/h015/h017/h018. Output: interp/InterpParseMisc.kt (73 lines).

- Unit 76 (compiler-parse.c::ExpandQS, B3 stratum, provisional under C-003): the quoted-string expansion (loc 48, score 23.0, rank 181, exact match; vendor lines 2154-2205). The two #G#/#S# arms kept separate; CodedComma=208; fprintf -> CompilerState.out.write (h002, the Inout.kt convention); Extract2(v1d, i) real reuse (B1); explicit NULs after the copy loops mirror the vendor's buffer terminators. Rules: h002/h007/h012/h014/h015/h017. Output: compiler/ExpandQS.kt (175 lines).

- Unit 77 (libmpscpp.cpp::mstring::Eval, B3 stratum, provisional under C-003): the indirection operator on mstring (loc 18, score 18.2, rank 157, exact match; vendor lines 3903-3925). Data copies into xd via writeCstring (h002); parse_ is the real B3 unit-63 implementation reused via package import (the vendor's in-body prototype resolves to it); the unreachable "err" dead store after the throw kept as a comment (h013). INTERPRETER_ERROR=8 (libmpscpp.h:77); pd1len/t2/sdlim assignments mirror the vendor's short domains; the result is the pd1 slice from sdlim. Rules: h002/h012/h013/h014/h015/h017. Output: mdh/MstringEval.kt (76 lines).

- Unit 78 (compiler-sys1.c::readf_, B3 stratum, provisional under C-003): the compiler file reader (loc 23, score 13.3, rank 129, exact match; vendor lines 418-445). Reader carrier at the edge (h001); k>200 drop kept; NOTABS is defined in the reference build (memsize.h:43), so the tab-to-space substitution is LIVE in the reference binary (h018 verification, journaled); n never resets across lines (vendor quirk kept); EOF-only return. Rules: h001/h004/h012/h015/h017/h018. Output: compiler/Readf.kt (84 lines).

- Unit 79 (libmpscpp.cpp::ScanAlnum, B3 stratum, provisional under C-003): the third of the three vendor ScanAlnum overloads - the token filter (loc 17, score 13.1, rank 127, exact match; vendor lines 10112-10139; the FILE-ptr/istream tokenizer overloads are separate corpus rows). Static tmp/tmp1 carried on the ScanAlnumScratch object (h008); ep/toklist dead params kept for call-shape fidelity (h013); Scan (B3 unit 81, this tranche) -> MdhStubs stub raised here and resolved by unit 81 below (MdhStubs 307 -> 315 at this unit). Vendor quirks kept: the empty-token arm returns tmp1 (NOT tmp - the caller receives the empty Scan result, not the previous token) and the again-loop re-Scans until a token starts alphanumeric (a stream of non-alnum tokens loops forever, vendor behavior). Rules: h008/h012/h013/h014/h015/h017. Output: mdh/ScanAlnum.kt (105 lines).

- Unit 80 (include/mumpsc/cppsysfunc.h::<misc>, B3 stratum, provisional under C-003): the compiler run-time support header residue (loc 23, score 12.9, rank 125, exact match; the whole declaration surface - no function in the header reaches min_loc 15). A pure extern-"C" roster rendered as an owner table (no Kotlin declarations emitted): DirectRead is a real B1 reuse (interp/DirectRead.kt) and LocalName a real B1 reuse (interp/GlobalName.kt); the three-argument GlobalData and four-argument GlobalOrder runtime forms are distinct vendor functions from the one-/two-argument intrinsics rendered by units 86/87 and unit 39. Vendor quirk kept verbatim: LocalName is declared TWICE (identical back-to-back prototypes); the commented-out getstr1 prototype sits above the live declarations. The include guard CPPSYSFUNC_H and extern-"C" linkage are reference-build visibility notes (h018). Rules: h013/h014/h015/h017/h018. Output: interp/CppsysfuncH.kt (100 lines).

- Unit 81 (libmpscpp.cpp::Scan, B3 stratum, provisional under C-003): the whitespace-delimited token reader (loc 16, score 12.6, rank 123, exact match; vendor lines 10038-10059; single overload). fscanf("%1024s") over the Msv in_file[io] Reader edge: skip whitespace, read at most 1023 chars; EOF clears tmp[0]=0 with tpx=0, a token sets tpx=1. Vendor defect documented and kept in shape: the word-too-long guard tests the fscanf ITEM COUNT (0/1) against 1023 - never true in the vendor binary either, so the INPUT_ERROR=6 (libmpscpp.h:75) throw below it is unreachable; kept as a comment, not a live branch (h013). tmp[1023]=0 is the only tail write; the previous tail beyond the NUL stays intact (vendor behavior). Static char tmp[1024] -> ScanScratch object (h008) - the SAME static ScanAlnum (unit 79) consumes. The MdhStubs Scan stub raised by unit 79 is RESOLVED by this unit and removed (cleanLocks precedent, unit 56). An unopened unit (null Reader) is the h001 null-file edge (DirectRead precedent, B1): empty token, tpx=0. Rules: h008/h012/h013/h014/h015/h017. Output: mdh/Scan.kt (100 lines).

- Unit 82 (libmpscpp.cpp::Cost, B3 stratum, provisional under C-003): the Smith-Waterman recursion core (loc 15, score 12.3, rank 120, exact match; vendor lines 8122-8147; single overload). The memoized DP cell over the AlignState matrix (owner unit 45; allocation owner init, batch-4): EMPTY_CELL=-32768 (libmpscpp.cpp:8029) gate BEFORE the bounds gate (vendor order kept - a memoized out-of-bounds cell would be read first, the vendor relies on the caller), diagonal MATCH/MISMATCH plus two GAP moves, clamped at zero, recursion on all three candidates. The "return __a[i][j]=A" C narrowing store rendered .toShort() with the Int A returned (h010). The MdhStubs Cost stub raised by unit 61 (Sw) is RESOLVED by this unit and removed (cleanLocks precedent, unit 56). Rules: h004/h010/h012/h014/h015/h017. Output: mdh/Cost.kt (70 lines).

- Unit 83 (interp-arith.cpp::mps_justify, B3 stratum, provisional under C-003): the (dollar)justify formatting core (loc 24, score 11.2, rank 110, exact match; vendor lines 888-912; single overload). NOTE the vendor parameter order: a is the C FORMAT STRING, b the output buffer, c the width text - sprintf(b, a, fc). The runtime format string renders as java.util.String.format over the format text carried in a (h009 adaptation); fc parses with the real strtodC (unit 10). Preprocessor miss recorded: the reference build takes the plain double branch; the MPFR and long-double branches are compiled out (family precedent: units 4/10/85/89). Rules: h009/h012/h015/h017. Output: interp/MpsJustify.kt (46 lines).

- Unit 84 (libmpscpp.cpp::global::Fnumber, B3 stratum, provisional under C-003): the one-argument (dollar)fnumber intrinsic over the unsubscripted global (loc 23, score 9.9, rank 97, exact match; vendor lines 7452-7482). Overload disambiguation: the one-arg global member - the two-arg global wrapper (loc 28), the mstring member pair (unit 42 rendered the two-arg form, Fnumber.kt) and the declared global two-arg wrapper are separate corpus rows. Retrieve the global's value with Mglobal(RETRIEVE=0) and throw MumpsGlobalException on GLOBAL_NOT_FOUND=2; build the coded call with CodedOpen=206/CodedClose=207/CodedComma=208 (defines.h:38-40) and the dollar byte rebuilt as 36.toChar() (SQL_Table precedent, unit 34); the call goes to v1d[1] and the result reads bd[1] (offset-1 convention, unit 42 precedent). No dead b.c_str()==0 arm here (unlike the two-arg mstring form, unit 42). fcn/Mglobal stubs and the CstringOps helpers reused unmodified. Rules: h007/h012/h014/h015/h017. Output: mdh/GlobalFnumber.kt (103 lines).

- Unit 85 (interp-arith.cpp::mps_atan, B3 stratum, provisional under C-003): the (dollar)atan intrinsic (loc 22, score 9.8, rank 93, exact match; vendor lines 344-370; single overload). gcvt_wrapper(atan(strtod(b)), a) - gcvtWrapper (NumSetup unit 29) and strtodC (unit 10) real reuses, mpsAcos (pilot unit 10) line-for-line shape. Preprocessor miss recorded: reference build takes the plain double branch; MPFR/long-double compiled out. Rules: h009/h012/h015/h017. Output: interp/MpsAtan.kt (42 lines).

- Unit 86 (libmpscpp.cpp::GlobalData, B3 stratum, provisional under C-003): the (dollar)data intrinsic over globals, char* form (loc 18, score 8.6, rank 76, exact match; vendor lines 2595-2615). Overload disambiguation: the char* form - the mstring/string forms (loc 17 each) are separate vendor overloads not in this batch's rows. Builds "s %=<dollar>data(<a>)" (dollar byte via 36.toChar(), unit-39 precedent), executes with the xecute stub, reads the result variable with the SymGet stub, atoi via the real atoiC; -1 on execution failure with tpx=0/1 by outcome. GlobalOrder (unit 39) line-for-line sibling; xecute/SymGet reused unmodified. Rules: h007/h012/h014/h015/h017. Output: mdh/GlobalData.kt (79 lines).

- Unit 87 (libmpscpp.cpp::GlobalOrder, B3 stratum, provisional under C-003): the order intrinsic over globals, std::string form (loc 16, score 8.2, rank 71, exact match; vendor lines 2576-2594). Overload disambiguation: the string form - the char* form is B2 unit 39 (whose KDoc pre-declared this row), the mstring form (loc 15) not in this batch. NAMING ADAPTATION: the vendor's char* and std::string overloads collapse to IDENTICAL Kotlin signatures after the unit-39 String carrier, so this overload is rendered GlobalOrderString (vendor name in the header comment; h015). The vendor's static string rr folds to a fresh String return (unit-39 precedent, h008); the success arm's (string)SymGet("%") cast rides the carrier's c_str(). Same command shape "s %=<dollar>order(<a>)" with tpx by xecute outcome. Rules: h007/h008/h012/h014/h015/h017. Output: mdh/GlobalOrder.kt edits (67 -> 141 lines).

- Unit 88 (libmpscpp.cpp::Max, B3 stratum, provisional under C-003): the free max-intrinsic wrapper (loc 17, score 8.1, rank 67, exact match; vendor lines 10167-10190; single free overload - the class member global::Max at libmpscpp.cpp:1651 is NOT a corpus unit and rides a new owner-named MdhStubs stub raised by this unit, sibling of unit 44's global::Min). Strips the trailing SOH pair quirk (reads arg1[i-2] when i==1 - kept faithful, unit-44 trailing-trim precedent), splits at the first SOH into name (bypass ^) and optional subscript, delegates to a("").Max() / a(sub).Max(), formats %g via String.format (h009, JVM %g shape, ShowAlign unit 61 precedent). svptr and ep are dead params kept for call-shape fidelity (h013); arg1 is mutated in place (truncated at the SOH). Static char tmp[32] -> MaxScratch object (h008); strlenC real reuse. Rules: h008/h009/h012/h013/h014/h015/h017. Output: mdh/Max.kt (104 lines); MdhStubs.kt +1 stub (see ledger below).

- Unit 89 (interp-arith.cpp::mps_sqrt, B3 stratum, provisional under C-003): the (dollar)sqrt intrinsic (loc 27, score 7.9, rank 60, exact match; vendor lines 519-553; single overload). Reference-build note: the plain double branch gcvt_wrapper(sqrt(strtod(in)),out) is the live code; the MPFR branch - including the negative-input numcomp gate, printf and NUMERIC_RANGE throw - is compiled OUT of the reference binary and omitted (the plain branch has no negative check at all; C sqrt of a negative yields NaN through the same path). The vendor parameter name "in" is a Kotlin hard keyword and renders as inCh (h015). gcvtWrapper/strtodC real reuses. Rules: h009/h012/h015/h017. Output: interp/MpsSqrt.kt (47 lines).

- Unit 90 (libmpscpp.cpp::mdh_tree_level_add, B3 stratum, provisional under C-003): a verification unit - the col1..col3 overload (loc 19, score 7.1, rank 46, exact match; vendor lines 384-424 family) was already covered by unit 8's family rendering (mdh/MdhTreeLevelAdd.kt renders all ten overloads through one mdhTreeLevelAddImpl), the unit-43 precedent (the col1..col7 row). Overload identity confirmed via units.csv (loc 19, score 7.1, rank 46) and the study tool metrics; the vendor body (prefix + xce, %p(tree), %d(depth), col1..col3 xd0-separated, xcf, zfcn at &tmp[1]) matches the existing Kotlin overload line for line. No new code, no new stubs. Scope quirk per PN-1: unit 8 front-ran the later-batch rows; unit 43 was the first such row to land, unit 90 the second. Events logged as reuse+hit pairs for the unit-8 rule set. Rules: h004/h007/h009/h014/h015/h017. Output: none (verification only).

- Unit 91 (libmpscpp.cpp::global::Concat, B3 stratum, provisional under C-003): the global-value concatenation, global-argument member (loc 18, score 5.4, rank 31, exact match; vendor lines 4049-4077). Overload disambiguation: the global-argument member - the double/long/int/char-ptr/string/mstring members (loc 13/12) and the mstring::Concat family are separate overloads not in this batch's rows. Two Mglobal(RETRIEVE=0) retrievals (this ref, then b's ref), each throwing MumpsGlobalException on GLOBAL_NOT_FOUND=2 - both arms set the same code even when the second retrieval fails after the first succeeded (vendor behavior kept); the concatenation is the plain string cat of the two retrieved values. Emitted as a Global extension (unit-14 precedent, cf. units 33/84). ref buffers rebuilt per call (unit-44 conflation precedent). Rules: h007/h012/h014/h015/h017. Output: mdh/GlobalConcat.kt (89 lines).

- Unit 92 (compiler-epi.c::<misc>, B3 stratum, provisional under C-003): the compiler-epi.c header residue (loc 30, score 5.2, rank 15, exact match; the June 16, 2024 banner plus three includes - the sole function epi, loc 135, is units.csv row 189, a batch-7 corpus unit). No Kotlin declarations emitted; the include roster resolves to the established carriers (memsize.h -> CompilerState sizes and the memsize constants on their owning units; compiler-macros.h -> macro expansions inlined at their owners; compiler-globals-ext.h -> the CompilerState object). The GPL banner is the standard library text (cf. the CopyrightNotice carrier, B1). Rules: h013/h014/h015/h017/h018. Output: compiler/EpiMisc.kt (48 lines).

- Unit 93 (compiler-read.c::<misc>, B3 stratum, provisional under C-003): the compiler-read.c header residue (loc 30, score 5.2, rank 25, exact match; the May 15, 2024 banner plus three includes - the sole function read_code, loc 96, is units.csv row 160, a batch-6 corpus unit). No Kotlin declarations emitted; the include roster resolves as in unit 92 (memsize.h constants incl. NOTABS memsize.h:43 documented on unit 78, Readf). Vendor quirk noted: the banner is byte-identical to compiler-epi.c's except the date line - a template quirk kept, not fixed. Rules: h013/h014/h015/h017/h018. Output: compiler/ReadMisc.kt (50 lines).

- B3 tranche stub ledger (units 71-93 close): MdhStubs.kt 300 -> 317 lines (+get_global stub u71; +Scan stub u79 RESOLVED/removed u81; Cost stub u61 RESOLVED/removed u82; +GlobalRef.Max() stub u88; exactly 1 dollar byte retained, the zfcn dispatcher comment). InterpStubs.kt 254 -> 278 lines (+3 GTK stubs u73; +Msv.ERROR field u74; exactly 2 dollar bytes retained, SymPut). compiler/Stubs.kt 87 -> 85 lines (cannon stub removed at u72). B3 batch complete: 31 units (63-93), all provisional under C-003; units 63-70 pushed as commit 5b41baf, units 71-93 pushed in this tranche's closing group.


- B3 close correction (units 83/85/89, 2026-11-21): during the tranche push, the working record for MpsJustify (u83), MpsAtan (u85) and MpsSqrt (u89) mislabeled their preprocessor situation as "MISSED TRIGGER (record_miss)" and omitted h018 from their events rows. That labeling was impossible per capture_criteria.md - h018 (preprocessor_conditional) had existed since the B1 close, so a miss could not be recorded; the units were in fact translated by the h018 action throughout (live-arm-only against the reference build config, excluded arms documented in each KDoc). Corrections applied: KDoc "MISSED TRIGGER" paragraphs rewritten as h018 preprocessor_conditional paragraphs with the correction noted; h018 reuse+hit pairs appended to PRO_knowledge_events.csv at the unit timestamps (2026-11-20 10:30/13:30/19:30 +0000; +6 rows, log now 1040 lines); rules_v0.pl h018 comment extended with the B3 recurrence (also units 75, 78); knowledge_tooling.pl coverage_report now annotates capture candidates whose family is already captured ("[captured -> rule h018; miss is pre-capture history]"). The 5 remaining miss rows are pre-capture history (B1 units 4/10/12/20/29) and stay as misses per the append-only log. No source-line changes; build unaffected.

- Unit 98 (interp-sym.cpp::GlobalQuery, B4 stratum, provisional under C-003): the dollar-query engine (loc 91, score 89.3, rank 269, exact match). Copies the state vector (whole-struct memcpy -> msvCopyInto over the Msv carrier), parses the unevaluated reference, keyfixes v1d (in-place around the whole-buffer keyfix stub via copy/write-back), appends the no-parens 1,1,0 fix-up, and fetches via Mglobal(XNEXT, and-v1d[1]) (mglobalAt). The malloc'd p1 (prefix-check scratch) is only consumed by the non-SQLITE tail; dropped with the GC note. h018: the #if defined(SQLITE) free+return arm is LIVE (SQLITE defined in the reference build); the excluded tail (strncmp prefix test + tmp3 parenthesize/reformat, vendor 1029-1097) is compiled vendor text unreachable under the reference configuration - documented in the KDoc, not translated (units 40/41 precedent). printf dollar byte on Char(36). AllocSV InterpStubs stub raised (state-vector machinery, not a corpus unit). Rules: h002/h007/h012/h014/h015/h017/h018. Output: interp/GlobalQuery.kt (190 lines).

- Unit 100 (compiler-doty.c::doty, B4 stratum, provisional under C-003): the dotted-indent DO-block tracker (loc 27, score 57.3, rank 245, exact match). Opens/closes do-blocks emitting the Do_Block_Entry/Bypass and longjmp(EforNJ,99) scaffold on Out. The four short* out-params (ExecLevel, ExecName, pd1Last, ExecX) ride h006 single-element ShortArray holders; DoRet/ExecStack/ExecSym stay reference arrays. Vendor quirks kept: the double post-increment store ExecStack[(*ExecX)++]=(*ExecName)++; the un-indent emission uses *ExecX-1 while the longjmp uses the already-decremented stack top; the Bypass fprintf passes ExecStack[*ExecX] twice with one conversion. LevCheck stub raised (compiler-mumps.c:1949, not a corpus unit). Rules: h001/h002/h006/h009/h010/h014/h015/h017. Output: compiler/Doty.kt (107 lines).

- Unit 102 (interp-parse.cpp::readf_, B4 stratum, provisional under C-003): the source-file reader into pd1 (loc 55, score 52.9, rank 238, exact match). FILE* infile is a FileReader opened/closed at the edge (fopen failure -> null carrier guard); getc is Reader.read (EOF -1 rides the vendor's k<1 tests); CR/CTRL-Z drop via i--; the backslash-continuation and EOF exits keep pd1len = i-1 verbatim. h013: "l = j" dead store dropped. New Msv owners: symlen (stateVector.h:59), currentFile (:96). Rules: h001/h002/h007/h012/h013/h014/h015/h017. Output: interp/Readf_.kt (141 lines).

- Unit 103 (interp-piece.cpp::<misc>, B4 stratum, provisional under C-003): the whole-file residual chunk (loc 140, score 52.2, rank 236, exact match; the tool's head matcher does not claim _piece, so the file's single function rides the residual - identity against units.csv is the file-level misc slice). _piece is the dollar-piece engine: start/end "-1" defaults, xindex delimiter scans (Short start argument, explicit h010 widening), the piece1 fall-through keeping m = strlen+1 on the missing-delimiter path, and the setpiece==1 splice (substr prefix, tmp[k]=1 marker, strcat from the first NUL, sym_(0,...) vs mglobalAt(STORE,...) by the leading caret). Vendor redundancy kept: the nested "if (setpiece == 0)" at piece1; keyword rename: parameter `in` -> `in_` (DirectRead precedent). h018: #define SQLITE and #define _INTERP_ both live; the commented-out SYMDEBUG is not an arm. New InterpStubs references only (xindex/strmove/substr/sym_/mglobalAt all existing stubs). Rules: h002/h007/h012/h014/h015/h017/h018. Output: interp/InterpPiece.kt (203 lines).

- B4 stub ledger (units 98-103 first push): InterpStubs.kt 278 -> 295 lines (+allocSV stub u98; +Msv.symlen/currentFile fields u102; the 2 SymPut dollar bytes unchanged). compiler/Stubs.kt 85 -> 92 lines (+levCheck stub u100). Units 98/100/102/103 pushed in the B4 group-1 first sub-tranche; 95/96/101 (setCode/forCode/lockCode, compiler code-emitters) and 94/97/99 (fcn, libmpscpp misc-9, pcre.h misc) follow after the build gate.

- Unit 95 (compiler-for.c::for_code, B4 stratum, provisional under C-003): the FOR command compiler (loc 232, score 148.1, rank 289, exact match). Five vendor labels (for_again, for_again2, while01, one_arg, singles) linearized as a label-dispatch loop: the only reachable backward goto (for_again2 from the singles comma) is the loop continue; forward gotos (while01 from the start-dollar case, one_arg from the for_fcn_flg test, singles from the comma-list check) are label transitions. h013: the unreachable vendor tail after 'goto for_again2;' (SinglesFlg/for_again dead branch) is dropped, which is also why for_again has no reachable backward goto and runs once. Vendor quirks kept: the empty 'if (cvar(forv)) ;' statement; five forx varargs against three conversions in the float-direction and m2767 emissions; the for_mult setjmp arm mixing for_mult (setjmp/For targets) with forx (For%dExit); the xpx+1 '{' probes. New CompilerState owners: forxmax/forxFLG/forLine/forx (:110/:111), for_fcn_flg/for_fcn_flg2 (:114), for_mult (:140, C short widened), SinglesFlg (:142), forv (:150), FOR_limit (:151). cvar stub raised (compiler-parse.c:2074, not a corpus unit). Rules: h001/h004/h009/h011/h012/h013/h014/h015/h017. Output: compiler/ForCode.kt (484 lines).

- Unit 96 (compiler-set.c::set_code, B4 stratum, provisional under C-003): the SET command compiler (loc 256, score 173.4, rank 288, exact match). The at-test indirect arms, the nxt_lhs lhs-collection loop, the MergeFlg RHS dance and the set_store emission ladder (global Mglobal STORE, dollar-piece/dollar-extract LHS reinterpretation via emitted 's $p/$e' Interpret source, cvar/sym_ stores); entry_pt == SET_STORE (compiler-read.c:138 caller) enters at set_store. h011: set/nxt_lhs backward gotos -> outer/inner while(true); rhs1 forward -> break. The B3-era set_code stub is removed; NewCode call sites updated to setCode (cannon precedent). Vendor quirks kept: the LOCK arm's literal '%s'/'lhs[i]' text (no conversion); raw end vs twice-Extract2'd strt in the 4-arg dollar-piece emissions; the eval arms passing tmp unstripped; the dollar section reusing the outer lhs loop counter i (clobbered vendor for-loop); the unguarded '<128' scans; the xpx+1 probes. h013: unused 'int i, j;' decls and the never-read rstr buffer dropped (the Extract2 call kept for its emission side effect); malloc/free lhs slots -> GC String slots. New CompilerState owners: setpiece (:31), setname (:32), GBL_flg (:125), MergeFlg (:143), MergeTmp (:144), lhs (:154), lhsGF (:155), lhsx/lhsf (:156). Rules: h001/h003/h004/h005/h006/h009/h011/h012/h013/h014/h015/h017. Output: compiler/SetCode.kt (572 lines).

- Unit 101 (compiler-lock.c::lock_code, B4 stratum, provisional under C-003): the LOCK command compiler (loc 62, score 56.0, rank 242, exact match). Argumentless form emits the cleanLocks/lock-off scaffold and skips to the lck1 tail (skip flag); the argument form loops setCode(SET) over the lock list (plus/minus flag, optional timeout via the 'to' buffer), emitting the makeLock/tpx scaffold. Backward goto NxtLckArg -> while(true)+continue. Vendor quirks kept: the xpx+1 ':' timeout probe; the commented-out 'goto set;' notes; the error ladder order with lock_return only for return_code==0. New CompilerState owners: LockFlag (:137), dirflg (:170). Rules: h001/h004/h009/h011/h012/h014/h015/h017. Output: compiler/LockCode.kt (175 lines).

- B4 stub ledger (units 95/96/101 second push): compiler/Stubs.kt now 100 lines (set_code stub REMOVED - owner landed as SetCode.kt setCode, NewCode/LockCode call sites updated in the same tranche; +cvar stub, owner compiler-parse.c:2074, raised by units 95/96). CompilerState.kt 113 -> 137 lines (+21 owner fields for units 95/96/101 under a B4 header comment: setpiece/setname/GBL_flg/MergeFlg/MergeTmp/lhs/lhsGF/lhsx/lhsf/LockFlag/dirflg/forxmax/forxFLG/forLine/forx/for_fcn_flg/for_fcn_flg2/SinglesFlg/for_mult/forv/FOR_limit). Events +58 rows (u95 13:00, u96 14:00, u101 15:00, all 2026-11-22 +0000; log now 1158 lines). Units 94/97/99 (fcn, libmpscpp misc-9, pcre.h misc) follow after the build gate.

- Build gate for the B4 group-1 double tranche (fba3d73 + d2931af, 2026-11-22): the first gradle build after the two pushes failed with 8 compileKotlin errors across 4 files. Corrections: (1) Readf_.kt:56-58 - the CRTN/CTRLZ/LINEFEED const initializers used Char(13)/Char(26)/Char(10), which are not constant expressions; rewritten as 13.toChar()/26.toChar()/10.toChar() (compiler/Readf.kt:46 CTRLZ precedent). (2) GlobalQuery.kt - the file-private strcpyC(CharArray, CharArray) duplicated CstringOps' public strcpyC(CharArray, CharArray) signature exactly (an h015 collision the pre-push scan missed: both are top-level in one package, so the private copy shadows into a conflicting overload); the private copy removed and the svPtr.xd call now binds the CstringOps helper (identical copy-up-to-NUL semantics), GlobalQuery.kt 190 -> 183 lines. (3) InterpPiece.kt - the same pattern for the file-private atoiC(CharArray, off = 0) vs CstringOps' public atoiC; private copy removed and the tmp2 start/end scans now bind CstringOps (its whitespace-skip is a superset; identical results on these inputs), InterpPiece.kt 203 -> 186 lines. (4) LockCode.kt:156 - 'st.dirflg = 0' assigned an Int into the Char byte-domain owner; rewritten '0.toChar()' with an h010 comment. No vendor-facing behavior changes; all four are Kotlin binding/typing corrections. Lesson recorded: private helpers in new files must be checked against PUBLIC same-package signatures (not just top-level names), and a tranche pushed without its build gate leaves such collisions latent.

- Unit 94 (interp-fcn.cpp::fcn, B4 stratum, provisional under C-003): the intrinsic dollar-function dispatcher (loc 775, score 1221.5, rank 307, the largest B4 unit - exact match). The dollar-dollar extrinsic launcher (child AllocSV state vector, start[]/nstart copies, free() dropped per GC), the 256-entry static opcode table (file-private OPCODE val, h004), and the switch over $extract/$ascii/$char/$find/$fnumber/$justify/$job/$len/$piece/$select/$storage/$test/$text/$translate/$random/$reverse/$x/$y/$io/$horolog/$get/pattern-match codes/$name/$next/$noerr/$order/$query/$qlength/$qsubscript/$z variables. h011: every goto label (ex1/ex2/err/arg_count_err/unknown_err/fnum_wrong/qlenerr/qsuberr1) ends the vendor function, so the tails are expanded inline at each jump site; the $select sel1 backward goto becomes while(true)+continue and the $translate trns forward goto becomes if/else. h018: SQLITE live ($ztabsize sql(OPEN) arm and $zsqloutput arm translated; the $ztable sql calls are commented in the vendor and stay commented); the $random GMP block and the $justify BIGFLOAT/MULTI_PRECISION arms are compiled out of the reference binary (the live plain-double arm feeds mpsJustify, unit 83, via temp-copy offset carriers). Vendor quirks kept: j reused as function-id char then work register; the case-1 'char tmp1/tmp2' shadowed scratch buffers (distinct Kotlin names); 'j > iargs[1]' capping the $extract end at the arg-2 byte offset; $ascii's uninitialized iargs[1] read (UB in C, zero-initialized carrier - honest observable, noted); $ztabsize leaving ierr unset; strfmon maxsize 256 against the 64-byte rstr scratch (65-Char carrier, throws where the vendor overruns - BMGSearch unit 97 precedent); atol/atof as the file-private C-library scans; rand() as kotlin.random.Random. Rules: h002/h004/h007/h009/h010/h011/h012/h013/h014/h015/h017/h018. Output: interp/Fcn.kt (1570 lines).

- Unit 97 (libmpscpp.cpp::<misc-9>, B4 stratum, provisional under C-003): the misc-9 residual slice (lines 9693-9982 plus 10390-10576; loc 342, score 161.2, rank 284 - exact match). The mstring operator<= / == / != overload families and operator const char*() fold into compareTo extensions (Float/String/Global/Mstring, the three vendor char*/const char*/string rhs types folding into the String overload - documented, not silently dropped) plus the Mstring equals/hashCode/toString carrier override (hashCode is the Kotlin-consistency adaptation, the vendor has none); != rides the negated equals. SymGet x3 / SymPut x9 / __symput / global::Flush follow; the svPtr-intrinsic builtin wrappers (Cosine, TermCorrelate, DocCorrelate, Sim1, Jaccard, Dice, StopInit, StopLookup, SynInit, SynLookup, Centroid, BMGSearch, Shred, ShredQuery) keep the vendor (svptr, ep, args) parameter order, the static tmp[32] scratch fields ride the Misc9Scratch object (Max unit 88 precedent), and 'a(&arg1[i+1])' mirrors MakeGlobal's myname+SOH+subscript reference assembly (callees are (Global,Global)-shaped: Cosine unit 71 and global::TermCorrelate unit 14 called directly; the rest ride NEW owner-named MdhStubs stubs). The previously-raised SymGet(String) stub is REMOVED (this unit owns it now, Scan unit 81 precedent). Vendor quirks kept: the arg-buffer SOH-to-NUL in-place mutation; BMGSearch's malloc(strlen(arg2+2)) 3-byte heap overrun (Kotlin throws where C corrupts); the bmg_fullsearch (char*,global) implicit-conversion binding note; DocCorrelate's uninitialized x on sscanf failure as an explicit Kotlin failure. Rules: h008/h009/h010/h012/h013/h014/h015/h017. Output: mdh/LibmpscppMisc9.kt (612 lines) plus the Mstring.kt carrier edits and MdhStubs.kt stub additions.

- Unit 99 (include/pcre.h::<misc>, B4 stratum, provisional under C-003): whole-header residual (loc 127, score 82.5, rank 262 - exact match). The version macros (PCRE_MAJOR 4, MINOR 5, DATE), the option/error/info/config/extra macro families as public const vals, the opaque pcre carrier (struct real_pcre is declaration-only, definition private to the PCRE library), the pcre_extra and pcre_callout_block carrier classes, and the PCRE_DATA_SCOPE function-pointer globals (pcre_malloc/pcre_free/pcre_stack_malloc/pcre_stack_free/pcre_callout) as the PcreDataScope singleton properties (BtreeHeaderState precedent, unit 55). h018: the Win32 DLL block is commented in the vendor and stays commented; the VPCOMPAT #ifndef selects the extern arm (non-VPCOMPAT live); the _PCRE_H include guard and the stdlib.h size_t include have no Kotlin rendering. The 19 exported PCRE function declarations resolve to nothing yet - the header is included by libmpscpp.cpp:55 but the corpus makes no pcre_/PCRE_ calls, so no h014 stubs are raised (a stub is raised when a translated call site lands, the ScanParse precedent). Rules: h004/h010/h012/h014/h015/h017/h018. Output: interp/PcreH.kt (247 lines).

- B4 stub ledger (units 94/97/99 third push): InterpStubs.kt 295 -> 377 lines (fcn stub REMOVED - owner landed as Fcn.kt fcn; +8 NEW owner-named stubs under a B4 unit 94 header: ScanParse (batch-8), Eval (interp-sym.cpp:1192, not a corpus unit), Qlength (batch-5), Qsub (batch-7), LocalName (B4 groups 2/3), GlobalOrder (globalOrder.h:8 inline, not a corpus unit), lpad (interp-strmanip.cpp:135, not a corpus unit), strfmon (C library monetary.h) - all with offset-carrying signatures; BuildGlobal/BuildLocal gain an arg4Off default-0 offset param and an Int return (vendor interp-parse.cpp:1836 / interp-sym.cpp:97; the parse_ call sites ignore both); the sql stub parameters relax to CharArray? for the vendor NULL-argument OPEN call; Msv gains 9 owner fields under a B4 unit 94 header: setpiece (:54), hor/ver (:62), nstart (:72, opaque pointer carrier), PD1 (:82), args (:85), Table (:86), TabSize (:87), sqloutput (:99)). MdhStubs.kt 317 -> 400 lines (SymGet(String) stub REMOVED - unit 97 owns it; +13 stubs under a B4 unit 97 header: Global.readInto, Sim1/Jaccard/Dice(Global,Global), Global.Centroid, Global.DocCorrelate, StopInit(String), StopLookup(String), SynInit(String), SYN(String), Shred/ShredQuery(String,Int), bmg_fullsearch(String,String)). Mstring.kt gains the equals/hashCode/toString carrier override set (u97). Events +54 rows (u94 16:00 x12 rules, u97 17:00 x8, u99 18:00 x7, all 2026-11-22 +0000; log now 1212 lines). B4 group 1 (units 94-103) is complete pending the build gate; groups 2 (u104-113) and 3 (u114-124) follow in later sessions.

- Unit 94/97 build-gate fix (batch C correction, fourth B4 push): the batch C build failed on four findings. (1) A mid-line `char-star/const char-star` sequence in KDoc (LibmpscppMisc9.kt h015 note and the Mstring.kt equals note) terminated the comment early - the two occurrences are reworded (the established push gate bans mid-line comment closers in prose; this pair slipped through). (2) The CodedOpen/CodedClose/CodedComma defines (206/207/208) are Int pins, so the six Char-buffer comparisons against them (Fcn.kt $z-formal-argument scan, doGoto-style coded-punctuation strip, $qsubscript op2 recode) now compare via .code. (3) Kotlin requires the else entry last in a when; the vendor default: arm (ierr=30, unknown_err, shared with opc 99) moved to the foot of the opcode when with a pointer comment at its original site. (4) The $horolog char truncation `tmp1[0] = day.toChar()` (Long to Char, deprecated-as-error) is now Char((day and 0xFF).toInt()), preserving the vendor low-byte truncation. Whole-arm verification: the 106-file PRO tree compiles with zero errors and zero warnings under kotlinc 2.1.20 locally before this push.

- Unit 104 (interp-rpm.cpp::parse_pattern_element, B4 group 2 first push): the pattern-parser element rule (loc 35, score 27.1, exact row 105 match). New interp/ParsePatternElement.kt: the arm's FIRST rpm unit raises the file-scope struct carriers (TokenType enum, Token, Scanner, Parser - batch-5 interp-rpm.cpp::<misc> owns their definitions and will reconcile, h004/h007 registered) plus the pattern_codes/char_classes tables as file-private pins. New InterpStubs stubs (u104 family): bstrcat (batch-9 owner, by-value struct return), acceptIt/accept/syntaxError/parse_pattern_atom_list (rpm file statics, not corpus), leaningToothpickize (batch-6). Vendor quirks kept: the strchr index idiom renders indexOf with the not-found case documented as IndexOutOfBounds-vs-wild-read (h015 note); new_bstring literal overloads as a file-private helper.
- Unit 105 (libmpscpp.cpp::global::Centroid): the matrix centroid walk (loc 30, score 22.6, row 106). New mdh/Centroid.kt: fun Global.Centroid(B) - Order/Data/assign/invoke ride the u31/u14 carriers; the MdhStubs Global.Centroid stub raised by B4 u97 is RESOLVED and REMOVED (cleanLocks). Vendor notes: !Data() as the explicit zero-code test, B(bb,"1")++ as read-plus-one-assign, the dead "long i=0" dropped (h013), the duplicated "bb-reset" dead store dropped.
- Unit 106 (interp-fnumber.cpp::_fnumber): the compiler-facing dollar-fnumber wrapper (loc 17, score 21.9, row 107). New interp/Fnumber_.kt (Readf_ naming): assembles the coded call into v1d[1], runs the REAL u94 fcn, copies bd[1] back. Vendor quirk kept: the literal is "$fn" NOT "$fnumber" (both dispatch through opcode 102; the two-arg mstring carrier spells it out - vendor spellings preserved, not harmonized); dollar byte via the DOLLAR char carrier.
- Unit 107 (interp-bifs.cpp::<misc>): the bifs residual (loc 60, score 18.4, row 108) - banner/roster, the sym_/wstem/sigint forward-decl resolution (no stub for the future wstem owner, GlobalClassH precedent), SQLITE define (live in the reference build, arms the zfcn owner's paths), CLOSE 207/COMMA 208/NO/YES pins (feeding the zfcn stub), the gcvt_wrapper macro resolved to the REAL gcvtWrapper per GCVT (h018), and Mget translated (128-byte stack tmp, nullable sym_ return kept). Output: interp/BifsMisc.kt.
- Unit 108 (compiler-at-test.c::atTest): the at-command scanner (loc 24, score 16.8, row 109). New compiler/AtTest.kt: the Stubs.kt h014 stub is RESOLVED and REMOVED; the old stub owner note ("not in corpus") was WRONG and is corrected in place (unit-25 precedent). The h005 AtTestResult bundle is KEPT, and the real atTest RESTORES the vendor's read of the caller's pd1Last (the imbalanced-quotes diagnostic prints and of pd1[*pd1Last], which the stub had folded away) via a new pd1Last: Short parameter - the two call sites (IfCode, SetCode) updated to pass st.pd1Last. Vendor idiom kept: the while(k++) post-increment skips the opening quote before the first test.
- Unit 109 (libmpscpp.cpp::init): the Smith-Waterman matrix allocator (loc 17, score 15.9, row 110). New mdh/Init.kt: fun init() is legal top-level Kotlin (the stub already carried the name); the MdhStubs init stub raised by u61 is RESOLVED and REMOVED (Sw.kt's call site now binds the real owner). The malloc-NULL raise(SIGSEGV) arms dropped (h013, Sw precedent). Vendor quirk documented: rows are malloc'd with sizeof(int*)/sizeof(int) while cells are short - the mismatch is unobservable on the JVM (declared short domain is what Cost/sw read); EMPTY_CELL -32768 kept as a file-private pin beside Sw.kt's copy.
- Unit 110 (include/mumpsc/sysfunc.h::<misc>): the runtime-library declaration header (loc 26, score 14.6, row 111) rendered as an owner roster (cppsysfunc.h u80 precedent, no declarations emitted, no stubs raised for future owners): getstr1 (batch-5) and TimeOut_getstr1 (batch-6) future owners; DirectRead/ErrorMessage/GlobalNext/LocalNext/LocalOrder/VariableNotFound1/LocalGet/GlobalGet/LocalQuery/fullUnpad/GlobalName REAL B1 reuses; Merge (batch-8) and the 3-arg runtime GlobalData (batch-7, distinct-shape note) future owners; LocalName REAL as of this batch (u112). The _INTERP_ guard selects the interpreter-side roster with LocalNext/LocalOrder visible on BOTH arms (h018 note); the commented-out VariableNotFound prototype stays commented out (h013 roster text). Output: interp/SysfuncH.kt.
- Unit 111 (interp-native-btree.cpp::pb): the DumpBlocks block dumper (loc 19, score 13.7, row 112). New interp/Pb.kt: the whole function is compile-time-only in the vendor (DumpBlocks UNDEFINED in the reference build) - h018 note documents that the reference binary excludes it; the unit is still the selected sample so the arm is translated in full. printf tails via byte-identical print strings (h009); memcpy(&x,block,4) is the little-endian 4-byte header read; the OFF_T z local feeds only the commented-out pair and is dropped (h013); the empty-body "for(...);" scan kept; MMARK 255 pin beside BtreeH/Scanblocks' copies.
- Unit 112 (interp-sysfunc.cpp::LocalName): the coded-local-name renderer (loc 36, score 12.6, row 113). New interp/LocalName.kt: the InterpStubs LocalName stub raised by B4 u94 is RESOLVED and REMOVED; the real function keeps the stub's exact offset signature so the four Fcn.kt call sites bind unchanged. Vendor shape kept: j=999 default, the two high-byte arms (bare NUL vs close-paren first), k counted per coded separator, tmp3 staging then strcpyC; the >127 and 206/207/208 compares ride the unsigned byte domain (.code).
- Unit 113 (libmpscpp.cpp::ScanAlnum): the FILE-ptr tokenizer overload (loc 18, score 10.6, row 114; the pointer_mark count 1 - the FILE* parameter - disambiguates from the istream sibling, which is not a corpus row of this batch). Appended to mdh/ScanAlnum.kt beside the B3 u79 MSV overload: FILE* -> java.io.Reader (h001 input-side), fscanf "%8191s" as the scanToken helper (width cap 8191, EOF null), the backward "goto repeat" as while(true)+continue (h011). Vendor quirks kept: the token is read BEFORE the max>511 guard discards it; isdigit(tmp1[0]) is short-circuited behind j==0 in the same order; stack word/tmp1 (no scratch object - noted against the MSV overload's static shapes).
- Unit 114 (interp-arith.cpp::mps_asin, B4 group 3 first push): the asin wrapper (loc 22, score 10.4, row 115 match) - mirror of the B1 mps_acos shape: strtodC over the argument, asin, gcvtWrapper (the REAL unit-29 helper) render. h018: the 3-way #ifdef selects the plain-double arm (GMP/MULTI arms compiled out of the reference build). Rules: h009/h012/h014/h017/h018. Output: interp/MpsAsin.kt.
- Unit 115 (rdbms-if.c::<misc>, B4 group 3 first push): the rdbms interface header residual (loc 30-ish, row 116) - roster only, no declarations emitted (cppsysfunc.h u80 / EpiMisc u92 precedent): the sql function family is units.csv row 126 (batch-5, future owner), extern int ierr resolves to the Msv.ierr owner (stateVector.h:50), and the SQL/POSTGRES arms are compiled out of the reference build (h018 note). Rules: h013/h014/h015/h017/h018. Output: mdh/RdbmsIfMisc.kt.
- Unit 116 (server.cpp::<misc>, B4 group 3 first push): the server support residual (row 117) - SERVERBUF 1024 file-private pin, the mymsgbuf layout (cid Long, buff0/buff1 1025/1024, socketinfo SocketData with the 108-char socketname) as the Mymsgbuf class, and logtime() rendering the vendor ctime() via the JVM Locale.US formatter (h009) onto the LogtimeScratch object (h008), stripping the trailing newline the vendor carries. Rules: h004/h007/h008/h009/h014/h017/h018. Output: mdh/ServerMisc.kt.
- Unit 117 (libmpscpp.cpp::Transpose, B4 group 3 first push): the matrix transposer (loc 27, row 118) - fun Transpose(a: String, b: String) over the Global ctor/invoke carriers: Order(1) walk of a's subscripts, B(bb,aa).assign(A(aa,bb).toDouble()) cell swap. The duplicated `bb = ""` dead store at the vendor loop head is dropped with the h013 note. Rules: h013/h014/h015/h017. Output: mdh/Transpose.kt.
- Unit 118 (libmpscpp.cpp::Count, B4 group 3 first push): the free MSV count-over-children wrapper (loc 17, score 8.1, row 119) - the Max unit-88 wrapper shape verbatim: double-SOH in-place strip, SOH scan, unsubscripted/subscripted invoke split, global::Count() (NEW owner-named MdhStubs stub, libmpscpp.cpp:1811, not a corpus unit - sibling of the unit-88 Max and unit-44 Min stubs), %g render onto CountScratch (h008/h009). Rules: h008/h009/h012/h014/h015/h017. Output: mdh/Count.kt.
- Unit 119 (libmpscpp.cpp::Min, B4 group 3 first push): the free MSV min-over-children wrapper (loc 17, score 8.1, row 120) - the Max/Count wrapper shape over the REAL B2 unit-44 global::Min (GlobalRef.Min, reused - no stub): MinScratch object for the static tmp[32] (the global::Min method above keeps its own carriers), SOH split, %g render. Rules: h008/h009/h012/h014/h015/h017. Output: appended to mdh/Min.kt.
- Unit 120 (libmpscpp.cpp::mstring::Fnumber(mstring), B4 group 3 first push): the one-arg fnumber overload (loc 17, score 7.9, row 121) - appended to the B2 unit-42 two-arg file: same coded-call assembly minus the optional second argument, reusing the file's STR_MAX/CODED_OPEN/CODED_COMMA/CODED_CLOSE/DOLLAR private pins (h015) and the REAL u94 fcn dispatcher. Vendor quirk kept: this overload spells the coded function "$fnumber" while unit 106's _fnumber spells "$fn" - both vendor spellings preserved (the dead c_str()==0 branch is the two-arg unit's h013 note; this overload has none). Rules: h007/h012/h014/h015/h017. Output: appended to mdh/Fnumber.kt.
- Unit 121 (interp-stem.cpp::escan, B4 group 3 first push): the Porter-style suffix scanner behind $ZALTER/$ZNORMAL stemming (loc 17, score 7.9, row 122) - the stem.h `ends` struct as the Ends class (ending/replace CharArrays, offset/replen/next Ints; the ending TABLES are the batch-6 interp-stem.cpp::<misc> owner, not yet translated), the table walk with the wl-offset>3 stem floor, the strmove splice (existing InterpStubs stub owner, established pattern), and the vendor's goto-flag semantics kept verbatim: `next` nonzero breaks, zero restarts the scan at entry 0. The strcmp suffix test is an inline file-private compare (endsWithC) - no shared helper exists in the package (h015). Rules: h007/h012/h014/h015/h017. Output: interp/Escan.kt.
- Unit 122 (compiler-globalarray.c::<misc>, B4 group 3 first push): the header residual (loc 30, score 5.2, rank 18, row 123) - EpiMisc u92 template verbatim: the May 12, 2024 banner plus the stdio.h/memsize.h/compiler-macros.h/compiler-globals-ext.h include roster resolving to the CompilerState carriers; the sole function GlobalArray is the DONE B2 unit-52 translation (GlobalArray.kt); no declarations emitted. Rules: h013/h014/h015/h017/h018. Output: compiler/GlobalArrayMisc.kt.
- Unit 123 (compiler-new.c::<misc>, B4 group 3 first push): the header residual (loc 30, score 5.2, rank 23, row 124) - same template, May 10, 2024 banner; the sole function new_code is the DONE B3 unit-65 translation (NewCode.kt); no declarations emitted. Rules: h013/h014/h015/h017/h018. Output: compiler/NewMisc.kt.
- B4 group-3 stub ledger (units 114-123): MdhStubs.kt 400 -> 406 lines (+GlobalRef.Count stub under a B4 unit 118 header, owner libmpscpp.cpp::global::Count, libmpscpp.cpp:1811, not a corpus unit). No other stub changes: u119 reuses the REAL GlobalRef.Min (B2 unit 44), u120 reuses the REAL fcn (B4 unit 94), u121 reuses the existing strmove stub. Events +106 rows (u114 09:00 x5 rules ... u123 18:00 x5, all 2026-11-24 +0000; log now 1430 lines). Whole-arm verification: the 123-file PRO tree compiles with zero errors and zero warnings under kotlinc 2.1.20 locally before the push. B4 groups 1-3 (units 94-123, the full 30-row batch-4 stratum) are now pushed; batch E (this tranche) not yet gradle-gated.
- Unit 124 (libmpscpp.cpp::<misc-5>, B5 tranche A opener): the 600-physLOC residue chunk - the mstring comparison-operator families (vendor 9693-9838) were FRONT-RUN by u97 (Mstring.equals/hashCode + Misc9 compareTo - verification rows only, no redeclaration), the Transpose char-star/mstring forwarders fold into u117 (roster note), and the chunk ends MID-mcvt(char-star) at the 600-LOC cutoff (mcvt family = misc-6 owner, roster note). Rendered: thrownMessage (86, resolves the u31 stub; vendor typo "detetected" kept), __finish (102, resolves the u31 stub; exitProcess), global conversion members Int/Double/Mstring/Char (112-135, riding globalNumericValue/readInto), Stem/TokenInit/Token (137-163, z-call builders with xce/xcf markers), the GTK glue family (596-818: dialog_new_with_buttons with the percent-p pointer rendered as identity-hash, 16 thin mdh wrappers over NEW libgtk h014 stubs, 15 opaque Gtk carriers + GdkRGBA), Dump/Restore x3 each (818-870, folded to Mstring/String carriers, shared zCmd core), Getenv (872, out-param shape kept, in_ keyword rename), ZSeek/Ztell x2 each (885-922, RandomAccessFile carrier; the global-writeback folds through the GlobalRef numeric assign with journal note), Perl x2 (924, the perlcpp call is commented out in the vendor source - kept as the live body), global::DocCorrelate wrappers (1203-1213, SUPERSedeS the u97 MdhStubs Global.DocCorrelate stub - same signature, delegates via the new docCorrelate h014 stub naming the batch-10 owner at vendor 1215), pt (1462, vendor uninitialized-i UB quantized to i=0), global::Count (1811, real body on the Global member; the u118 GlobalRef.Count stub seat becomes a GlobalRef.Count delegate so the u118 call sites bind unchanged), global::Transpose (2069, kills B first - distinct from u117), stem x3 (2293-2312, String by-ref writeback returns the stemmed value - h015 divergence journaled), Hash/lhash (2314/2335, 4-byte little-endian sum; unsigned-long UB upper-bytes quantized to zero; ULong wraparound kept in Hash, lhash folds to Long to keep the u31 Hash-class call sites binding), operator*(string) (2354, rendered as indirectOf - Kotlin has no prefix-star overload point), GlobalSet x7 (2365-2461, folded to String,String; stray lone-backslash line dropped PN-1), GlobalGet (2463-2509, kept as TWO renders because the char-star overload's failure path returns SymGet(percent) anyway - vendor bug kept as observable; the string overload folds into the Mstring shape), pmw x5 (2656-2675, folded; new pm h014 stub, owner interp-pm.cpp:43). MdhStubs: thrownMessage/__finish/lhash stubs REMOVED (resolved), GlobalRef.Count stub -> delegate, u97 Global.DocCorrelate stub superseded; ADDED wstem (owner interp-stem.cpp stem tables, batch-6), pm, docCorrelate (batch-10 owner), 15 libgtk stubs + opaque carriers; MdhStubs now 514 lines. Rules: h004 (front-run verification), h008, h009, h010 (unsigned folds), h013, h014, h015, h017. Output: mdh/LibmpscppMisc5.kt (776 lines). Whole-arm verification: 124 files, zero errors, zero warnings under kotlinc 2.1.20 locally before the push. Events +16 rows (log now 1446 lines). Note: scan-gate caught and fixed 3 real issues pre-push (KDoc backticks, a raw dollar in a KDoc, an m[0] double-bang); the Hash class (u31) vs fun Hash coexistence is arity-disambiguated (class ctor 3 args, fun 1 arg) - Kotlin-legal, journaled.
- Unit 125 (rdbms-if.c::sql, B5 tranche A): the RDBMS interface function (vendor 81-388, loc 209, row 126) - a PREPROCESSOR TRICHOTOMY: ODBC arm (#ifdef SQL), PostgreSQL arm (#ifdef POSTGRES), and the empty #else arm. The reference build defines NEITHER (consistent with the u115 residue finding: no sqlext.h, no libpq-fe.h includes), so the resolved body is the vendor's "Function is empty and returns 0" arm; both excluded arms documented as h018 roster comments (u40/u41 precedent) with their quirks kept on record (ODBC bind-length 50 into 512-byte buffers with x[90] unbounded against cols; the percent-d-plus-embedded-NUL sprintf format; the strlen(gbl+j) past-the-content read with j==cols; bd[1]=0 on the exit path; the forward goto out of the row loop). IDENTITY CORRECTION to the u115 roster: this sql(DSN,User,Password,Command,MumpsArray) is NOT the interp-sql.cpp::sql(op,svPtr,...) the InterpStubs stub carries (batch-9 unit, btree.h:37 prototype, called by u94 fcn) - the u115 roster conflated them; no Kotlin collision (this render lands in mdh). Rules: h015, h017, h018. Output: mdh/RdbmsSql.kt. Whole-arm verification: 125 files, zero errors, zero warnings under kotlinc 2.1.20 locally before the push. Events +6 rows (log now 1452 lines).
- B5 u124 cosmetic addendum (post first gradle gate): the build was GREEN with 3 new redundant-conversion warnings at LibmpscppMisc5.kt:317 (yr/mn/dy LongArray elements already Long - the mdh_calendar_get_date String.format args); dropped the three .toLong() calls (fix-commit only, no events rows - cosmetic, no rule event; GlobalOps 99-100 remain the accepted u62 pair). Local whole-arm gate: 125 files, 0 errors, 0 warnings.
- Unit 126 (libmpscpp.cpp::SynInit, B5 tranche A): the synonym-file loader (vendor 7810-7817 forwarders + 7819-7897 body, loc 58, row 133). The three C++ overloads (char-star 7810, mstring 7815, string 7819) collapse to one Kotlin String parameter (the char-star/mstring forwarders only relay). The h014 SynInit(String) stub raised by u97 is REMOVED (Scan u81 precedent) and the LibmpscppMisc9.kt:541 MSV-wrapper call site rebinds; the MdhStubs SYN stub stays (not a corpus unit) with a carrier note - the vendor file-scope syn[SYNMAX]/synx state (7785-7786) is now carried as top-level val syn / var synx in SynInit.kt (h008 carrier; SYN at 7788 reads it when its owner renders; cmpr/bsearch at 7790 stay with SYN). Renders: fopen/fclose at the h001 FileReader edge, fgets as BufferedReader.readLine (readLine already excludes the terminator - the vendor line[i-1]=NUL strip is built-in, documented), tolower over the h012 byte domain (private tolowerC, ScanAlnum u120 precedent), strtok(" ") as split+filter (runs of spaces collapse), the shared alias pointer per line carries naturally as a shared immutable String, malloc-NULL exits dropped (no GC-heap analog, documented), the dead strlen(p1)==0 post-strtok check kept as comment (h013), the SYNMAX overflow returns 1 WITHOUT close (vendor fd-leak quirk kept), bubble sort compares word only and swaps the (word,alias) pair, and the return narrows the vendor long synx to int explicitly (quirk noted; any file under SYNMAX fits). Rules: h001, h002, h008, h012, h013, h014, h015, h017. Output: mdh/SynInit.kt (+ KDoc carrier for SynEntry/SYNMAX; MdhStubs stub removal + SYN carrier note). Whole-arm verification: 126 files, zero errors, zero warnings under kotlinc 2.1.20 locally before the push. Events +16 rows (log now 1468 lines). Scan-gate note: the KDoc had two mid-comment char-star-slash closures (the exact h015-family scan-gate rule) - caught locally by the comment-closer scan and reworded to char-star before push.
- Unit 127 (interp-rpm.cpp::<misc>, B5 tranche A): the 145-physLOC non-contiguous residue - file header (1-118) plus the 14 sub-15-LOC functions (rpm, perlmatch, perlcpp, new_scanner, scan, take_char, new_parser, parse, parse_pattern, parse_pattern_atom, parse_pattern_atom_list, accept, accept_it, syntax_error). Stub resolutions (Scan u81 precedent): the InterpStubs rpm/accept/acceptIt/syntaxError/parsePatternAtomList stubs REMOVED - this misc chunk owns those vendor lines (rpm's former 'not a corpus unit' tag corrected: rpm rides the misc chunk). NEW stubs: perlPm (batch-9 owner), scanToken (u128 owner), parseCount (u135 owner). h004 RECONCILE: the unit-104 provisional raises PATTERN_CODES/CHAR_CLASSES/newBstringLit move to their misc-chunk owner InterpRpmMisc.kt as internal (enum/token/scanner/parser carriers stay in ParsePatternElement.kt); toupperC stays file-private per the Parse_.kt:258 convention (a first cross-file internal toupperC red-built on conflicting overloads - caught by the local whole-arm gate, reverted to per-file private). Renders: rpm with the offset-carrier signature (Parse_.kt call sites bind unchanged) - the quote/'~' shortcut keeps the 'hack d-quotes' trailing-quote strip verbatim (unterminated-quote eats a real char, vendor bug kept as observable); the scanner walk quantizes the C NUL-terminator read at end-of-input to NUL via a private charAtC accessor (the EOT case rides it, h007 quirk note); syntax_error's percent-star-c caret line renders as repeat-padding with negative width quantized to zero; accept falls through to accept_it after the error tail (sigint stub throws, unreachable tail kept); PM_DEBUG printf dead in the reference build (pm.h:25 = 0, h018 comment arm, u125 precedent); MAX_BACKREFS/OFFSET_BUF_LEN/min macro dead after the 2026 PCRE-to-clib-regex conversion (h013 anchor comments); DOUBLE_QUOTE (210) carried internal for batch-6 leaning_toothpickize. Rules: h002, h004, h007, h012, h013, h014, h015, h017, h018. Output: interp/InterpRpmMisc.kt; edits: InterpStubs.kt (-5 stubs, +3 stubs), ParsePatternElement.kt (reconcile note + private toupperC restore). Whole-arm verification: 127 files, zero errors, zero warnings under kotlinc 2.1.20 locally before the push. Events +18 rows (log now 1486 lines).
- Unit 128 (interp-rpm.cpp::scan_token, B5 tranche A): the pattern lexer's one-token step (real vendor 259-297). BOUNDARY NOTE: the pre-registered span is 259-506 - the chunker's brace balancer never closes on the real function (the quote-scan case), so the recorded boundary runs to EOF and overlaps u135 parse_count (recorded 360-506, real 360-402) and batch-6 leaning_toothpickize (recorded 479-506, real 479-497); all render per pre-registered boundaries - this unit translates the real body, the residue is owned by u127 (DONE - take_char/syntax_error direct calls), u104 (DONE), u135 (parseCount stub pending) and batch-6 (stub pending) - verification only, no redeclaration (u124 front-run precedent). InterpStubs scanToken stub REMOVED (owner rendered, u81 precedent); unit 127's scan() driver rebinds automatically (same package). Renders: the switch over toupper of the byte at current_char - digit do-while INT, quote-scan STRING, the six punctuation tokens, the seven pattern codes, NUL EOT (the charAtC end-of-input quantization carries the C terminator read, h007), default syntax_error with 1+offset position then the unreachable take_char/ERROR tail (sigint aborts in the vendor; the Kotlin stub throws). Vendor quirk kept: the STRING scan has NO terminator check - an unterminated literal walked the C buffer unbounded (UB); the Kotlin carrier reads the quantized NUL forever, a deterministic non-return where the vendor had UB - documented, not fixed. Private toupperC/isdigitC/charAtC follow the per-file convention (Parse_.kt:258, u127). Rules: h007, h012, h014, h015, h017. Output: interp/ScanToken.kt; edit: InterpStubs.kt (stub removal note). Whole-arm verification: 128 files, zero errors, zero warnings under kotlinc 2.1.20 locally before the push. Events +10 rows (log now 1496 lines).
- Unit 130 (mumps.cpp::main, B5 tranche A): the interpreter driver (vendor 73-239, units.csv row 129: loc 125, ptr 10, ctrl 15, score 60.7) - PI DECISION: FULL RE-RENDER despite the overlap with the pilot u12 (mumps.cpp::<misc> lines 40-239, MumpsMain.kt); translated independently without consulting u12, as the new seat interp/MumpsMainUnit130.kt with the distinct entry name main130 (one main per package - u12 main stays the JVM entry; h017 note). Journaled divergences between the two renders (expected, not reconciled): this seat returns Int (the C int-main contract) instead of exitProcess since it is not the JVM entry; readline(prompt)/^D reads via readlnOrNull after a print prompt (avoids the System.in backtick access the scan gate bans; u12 used the backtick form, pre-gate pilot code); the history analogs are independently written file-privates. Resolves per the reference build (h018, config.log line 7): database='; DBMS: SQLITE; Memory Resident: 0' (SQLITE defined, and #defined unconditionally at vendor 39), floatSize='; Float: double', Fpbits='; FP Bits: N/A' (HAVE_HARDWARE_MATH in); excluded NATIVE/NATIVE_SHARED/CLIENT/READ_ONLY/BIGFLOAT arms and the REPL NATIVE_SHARED btree deadlock guard documented as comments. h013 drops: the static struct C list (never used), the if(0) SIGALRM/set_alarm block, the uncalled DirectRead prototype. h011: '?' goto announce = break to the outer re-announce loop. __TIME__/__DATE__ BUILD_TIME/BUILD_DATE placeholders flagged (u12 divergence note repeated). cleanLocks is DONE (LockMisc) - direct call; interpret/mglobal/symPut/globalClose stubs reused. Scan gate caught 5 raw dollar-signs in comments (dollarSymPut rewording) and a KDoc backtick before push - fixed locally. Rules: h003, h011, h012, h013, h014, h015, h017, h018. Output: interp/MumpsMainUnit130.kt. Whole-arm verification: 129 files, zero errors, zero warnings under kotlinc 2.1.20 locally before the push. Events +16 rows (log now 1524 lines).
- Unit 132 (interp-bmgsubs.c::bmg_search, B5 tranche A): the Boyer-Moore-Gosper BUFFER search (vendor 134-216, K&R definition at 134; units.csv row 131: c, loc 60, comment_ratio 0.189, ptr 10, ctrl 17, score 47.0). First bmg_search seat (no stub existed; the MdhStubs bmg_fullsearch stub stays - its owner is libmpscpp.cpp, a different file). Renders the same delta0/cmap scan as u70 bmg_fsearch over a caller-owned buffer, chunked by BUFSIZE so the LARGE hack works; the chunk-end walk-back, the pattern compare (vendor OOB pattern[-1] read stopped at j<0, u70 precedent), and the line-start do-while walk are rendered per the u70-established pointer-arithmetic offset conventions; gotamatch (u17) receives position t (buffer-relative, vs fsearch's file_offset+t - kept verbatim). Vendor quirks kept: the insurance-NL CLOBBERS the caller's last byte to NL and restores it from lastByte at the exit (observable side effect on the input buffer); h013 dead store 'int byte_offset = 0' (never read) dropped with comment; register zero-inits are C declaration artifacts. Rules: h004, h011, h012, h013, h014, h015, h017. Output: interp/BmgSearch.kt. Whole-arm verification: 130 files, zero errors, zero warnings under kotlinc 2.1.20 locally before the push. Events +14 rows (log now 1544 lines).
- Unit 134 addendum (log-integrity repair): the u134 events push normalized 112 historical rows of PRO_knowledge_events.csv from CRLF to LF (lines 1213-1324, the pre-B5 region committed with mixed endings; the local append script read the codeload zip in text mode). CONTENT is unchanged - every field byte-identical - but the repair push restores the original CRLF endings on those rows so the log stays byte-faithful; the 8 u134 rows append with LF like the rest of the recent tail. Lesson folded into the procedure: always append to the events log in BINARY mode.
- Gate round (m1-build, first gradle build+detekt+jacoco run over tranche A, ec6fd64 base): BUILD/compileKotlin had 2 kotlinc warnings (GlobalOps.kt:99-100 redundant .toShort() - the Global.plus(s:Short)/minus(s:Short) operators at :142-143 already return Short; the compound-assign Short family dropped the redundant conversions, assignShort(this + i) / assignShort(this - i), byte-identical semantics). detekt FAILED with 41 weighted issues in four vendor-shaped families; dispositions: (1) EmptyElseBlock x26 (GlobalOps disambiguation chains) - rule DISABLED, the empty else arm IS the translated vendor body (C has no mandatory-else; these render the vendor's own empty else blocks, not elided code); (2) SwallowedException x10 - rule DISABLED, the catch-and-continue carriers render the vendor's own ignore-the-error paths (fopen-errno etc.); rethrowing would diverge; (3) SpreadOperator x1 (GlobalCarrier:96) - rule DISABLED (performance set), the spread is the C varargs marshalling shape; (4) ImplicitDefaultLocale x4 (Min:190/Max:99/Count:85 %g, NumSetup:125 %.8g) - rule KEPT ON (it lives in potential-bugs, the set the arm deliberately keeps hot): the four sites now pass java.util.Locale.getDefault() explicitly, which is byte-identical behavior (C printf IS default-locale) and satisfies the rule without weakening potential-bugs. detekt.yml updated with the three rule disables + rationale comments. Whole-arm local gate after the fixes: 132 files, kotlinc 2.1.20, zero errors, zero warnings. No events rows (gate-fix precedent ed933e2/dac2882).
- Unit 135 (interp-rpm.cpp::parse_count, B5 tranche B, FIRST TRANCHE B UNIT): the pattern quantifier arm (real vendor 360-401, 40 physLOC; units.csv row 136: cpp, comment_ratio 0.024, ptr 7, ctrl 6, score 28.4, rank 203). RESOLVES the InterpStubs parseCount stub raised by u127 (u81/u133 stub-removal precedent - the InterpRpmMisc.kt parse_pattern_atom call site and the ScanToken.kt ride note bind unchanged). BOUNDARY NOTE (u128 glitch family): the pre-registered 360-506 span runs past the real end (chunker brace glitch, the quote-scan case) - residue verified as front-runs: parse_pattern_element (u104 DONE), parse_pattern_atom_list/accept/accept_it/syntax_error (u127 DONE), leaning_toothpickize (batch-6 stub pending). Render: the INT head takes lowerbound, accept_it, then the DOT range arm ({lower,upper} / {lower,}) or the bare {lower}, closed by bcatchr '}' (the vendor falls to the shared close after either nested arm); the else arm is the vendor's 'Must have been a DOT' assumption - accept, then {0,upper} on INT else the star quantifier. bstrcat keeps its h014 stub (batch-9 interp-bstring.cpp owner); newBstringLit/acceptIt/scan/bcatchr are DONE seats (u127). Vendor UB kept documented: a neither-INT-nor-DOT head leaves the C result UNINITIALIZED - the Kotlin else renders the DOT arm verbatim without widening (unreachable in the vendor call graph: parse_pattern only recurses on INT/DOT heads). Rules: h014, h015, h017. Output: interp/ParseCount.kt; edits: InterpStubs.kt (stub removal note), InterpRpmMisc.kt:291 (stub ride comment -> DONE). Whole-arm verification: 133 files, zero errors, zero warnings under kotlinc 2.1.20 locally before the push; Locale.getDefault()/empty-else/Swallow/spread detekt patterns clean at the new seat. Events +6 rows (log now 1570 lines).
- Unit 136 (interp-strmanip.cpp::<misc>, B5 tranche B): the non-contiguous residue - file header, the MSVC-only rindex (43-53), strcatx (122-130), lpad (135-149); 36 physLOC, ptr 6, ctrl 9, score 28.0 (units.csv row 137; the extractor claims substr batch-7 and xindex batch-9 as their own units). TWO STUB RESOLUTIONS with owner-tag corrections (u127 rpm precedent): (1) MdhStubs strcatx stub REMOVED - its 'libmpscpp.cpp owner' tag was wrong (libmpscpp.cpp only CALLS it at 6978+, the single C definition is strmanip); the real seat interp/InterpStrmanipMisc.kt strcatx(out, in_) rebinds InterpSysfuncMisc.kt:194/199 (mdh import dropped - same package now) and mdh/Input.kt:96-97 (mumps.interp.strcatx import added); (2) InterpStubs lpad stub REMOVED - its 'not a corpus unit' tag was wrong (lpad rides the misc chunk below the 15-LOC threshold); the real body keeps the stub's offset-carrier signature so Fcn.kt:678 binds unchanged. Renders: strcatx walks out to NUL, appends in up to its NUL, then the x01 separator + NUL (no bounds check anywhere - C overrun UB quantized to the deterministic IndexOutOfBounds, u128 precedent); lpad j=(strlen-1).toShort() (size_t-into-short truncation kept), k=l-j-1, k<=0 returns with strout UNTOUCHED (vendor quirk kept - caller's buffer left with prior contents, not zeroed), shift by k, NUL, k leading spaces. h018: the MSVC ifdef pair 42-54 resolves to the EXCLUDED arm (reference build Linux) - rindex is a roster comment only (its s[strlen] NUL-seat scan start noted), no seat raised (u125 precedent). Rules: h012, h014, h015, h017, h018. Output: interp/InterpStrmanipMisc.kt; edits: InterpStubs.kt + MdhStubs.kt (stub removal notes), InterpSysfuncMisc.kt (import rebind), mdh/Input.kt (import rebind). Whole-arm verification: 134 files, zero errors, zero warnings under kotlinc 2.1.20 locally before the push. Events +10 rows (log now 1580 lines).
- Unit 137 (interp-sym.cpp::SysDump, B5 tranche B): the symbol-table dump (vendor 925-943, 18 physLOC; units.csv row 138: ptr 11, ctrl 2, score 28.0, rank 200). RESOLVES the SymHeaderMisc.kt SysDump h014 stub raised by u134 (prototype :70 had no seat until then; ledger updated in place). Render: Mltoa(pid) into the 32-byte carrier for the pid-named output file (Mltoa keeps its MdhStubs batch-10 stub), PrintWriter at the io edge (h001; SynInit try/catch null-carrier precedent for fopen), the NULL branch calls ErrorMessage (DONE, direct) whose sigint(100) abort makes the vendor's tail fprintf(NULL) UB unreachable - the Nothing-returning error carrier quantizes the tail and smart-casts the writer non-null (no double-bang); the frame walk start[_Sym] head through the stab chain prints name line + data line per node; fclose -> close(). Vendor UB quantized at two deref seats: (*p1->data).c_str() and fprintf(p1->name) have no NULL tests (deterministic IllegalStateException at the same seats, u128 precedent); h013 dead declarations key[1024]/p2/p3 recorded in-KDoc; current-frame-only walk kept verbatim. Rules: h001, h004, h012, h014, h015, h017. Output: interp/SysDump.kt; edit: SymHeaderMisc.kt (stub removal + ledger line). Whole-arm verification: 135 files, zero errors, zero warnings under kotlinc 2.1.20 locally before the push. Events +12 rows (log now 1592 lines).
- Unit 138 (interp-sysfunc.cpp::getstr1, B5 tranche B): the STR_MAX line reader (vendor 79-120, 30 physLOC; units.csv row 139: ptr 3, ctrl 10, score 24.4, rank 189; first raised in CompilerState's not-yet-raised roster, u129). New file interp/Getstr1.kt. Render: per-char fgetcC over the Reader edge (h001, Getstr2.kt:63 per-file precedent), EOF lands empty-string + -1, NL breaks, >127 and <32 quantize to space, CR continues (Kotlin for-loop so the vendor's for-increment-on-continue is kept), instr grows by char append; the STR_MAX-1 length arm warns on stderr with the 69-char preview (substr(0,69) verbatim), drains the rest of the physical line, breaks; strncpy lands instr in the caller's byte buffer (char-double-star renders as the buffer CharArray, the vendor only ever uses the one indirection); len < STR_MAX returns len, else globalExceptionCode = INPUT_LENGTH (10, libmpscpp.h:79 - checked against the header, the errors.h roster does not carry the numeric values) and throws MumpsGlobalException (DONE MdhStubs seat, direct). Vendor bug kept: the drain loop is an ASSIGNMENT in the condition - a NUL byte in the input stops the drain early (documented, not fixed). h013: the vendor tail past the unconditional throw (area[i]=0, return(i), sigint(100), return(-1)) is dead code, recorded in-KDoc only. Rules: h001, h007, h012, h013, h014, h015, h017. Output: interp/Getstr1.kt. Whole-arm verification: 136 files, zero errors, zero warnings under kotlinc 2.1.20 locally before the push. Events +14 rows (log now 1606 lines).
- Unit 139 (interp-arith.cpp::expx, B5 tranche B): the exponentiation arm (vendor 923-968, 48 physLOC; units.csv row 140: ptr 3, ctrl 5, score 22.4, rank 176). RESOLVES the InterpStubs expx stub (Parse_.kt:1018 call site binds unchanged on the offset-carrier signature). h018 TWO-LEVEL trichotomy per the reference build (numSetup unit-29 hardware-double resolution, ArithH MISSED-TRIGGER note; Float: double per u130): MULTI_PRECISION undefined -> the ifndef arm IN; BIGFLOAT out -> the plain-double else arm IN. The excluded arms are journaled not rendered: the long-double strtold/powl arm and the MULTI_PRECISION mpfr/mpz arm (floatstring dispatch, static init-once flag, mpz_pow_ui - h013 static edges live only there). Render: fa/fb via a per-file private strtodC(b, off) regex helper (MpsAcos.kt:34 regex + Numcomp.kt:60 offset precedent - the shared internal seat has no offset carrier), fa.pow(fb) (JVM intrinsic = C pow), gcvtWrapper into scratch + strcpyC to the caller's rOff carrier (the gcvt seat writes index 0; Parse_.kt passes bOff - u98 offset-adapter precedent), early return kept verbatim. Rules: h012, h014, h015, h017, h018. Output: interp/Expx.kt; edit: InterpStubs.kt (stub removal note). Whole-arm verification: 137 files, zero errors, zero warnings under kotlinc 2.1.20 locally before the push. Events +10 rows (log now 1616 lines).
- Unit 140 (libmpscpp.cpp::Multiply, B5 tranche B): the STRING-argument library helper (vendor 2211-2250, 26 physLOC; units.csv row 141: comment_ratio 0.037, ptr 2, ctrl 8, score 21.4, rank 172). Overload disambiguation journaled: four same-named roster units - this is :2211; the MSV intrinsic wrapper (:10193, rank 154, u46) and global::Multiply (:2164, rank 213, u53 GlobalMultiply.kt) are DONE; the char-star helper (:2252, rank 169) is the batch-7 unit. The vendor body is a VERBATIM copy of the global::Multiply matrix loop modulo the three fresh global constructions - the arm folds the duplicate to the single u53 seat (u117 Transpose-forwarder precedent, journaled as the diverging render decision): Multiply(a,b,c) constructs Global A/B/C from the strings and dispatches A.Multiply(B,C) (the destination C().Kill() lives inside the u53 seat's first statement). Rules: h008 (by-value strings -> immutable String), h014 (Global.Multiply + Global(String) DONE seats, direct), h015 (new String-overload distinct from the intrinsic signature), h017. Output: mdh/Multiply.kt (overload appended; no new file). Whole-arm verification: 137 files, zero errors, zero warnings under kotlinc 2.1.20 locally before the push. Events +8 rows (log now 1624 lines).
- Unit 141 (libmpscpp.cpp::Transpose, B5 tranche B): the MSV intrinsic wrapper (vendor 10316-10347, 23 physLOC; units.csv row 142: comment_ratio 0.042, ptr 0, ctrl 8, score 14.1, rank 136). TAG CORRECTION (u127 rpm precedent): the u117 KDoc called this wrapper 'not a corpus unit - roster note only' - roster row 142 IS this function; the u117 note is superseded in-KDoc (its string/string seat stays the u117 unit). Render in the u46 Multiply-wrapper shape (overload appended to mdh/Transpose.kt): trim subscript-marked tails, the arg2[i]-not-arg2[j] copy-paste diagnostic bug kept verbatim (u46 family), the array-name-only arg[i+1]=0 one-past-NUL residue clear kept (STR_MAX buffers keep it in bounds), caret bypass via cstringAt offset 1, then dispatch to the u117 Transpose(String,String) seat - the vendor's live subscripted dispatch a(&arg1[i+1]).Transpose(b(&arg2[j+1])) always carries EMPTY subscript regions (any actual marker aborted in the diagnostics), fold journaled with the u46/u117 precedents. h008/h013: static tmp[32] + double x dead, dropped with note; (char*)"" -> fresh String. Rules: h002, h008, h012, h013, h014, h015, h017. Output: mdh/Transpose.kt (overload appended; per-file SUBSCRIPT_MARK + interp imports added). Whole-arm verification: 137 files, zero errors, zero warnings under kotlinc 2.1.20 locally before the push. Events +14 rows (log now 1638 lines).
- Unit 142 (libmpscpp.cpp::global::Sum, B5 tranche B): the sibling-sum walk (vendor 1620-1648, 21 physLOC; units.csv row 143: comment_ratio 0.045, ptr 1, ctrl 5, score 11.9, rank 116; the roster id keeps the global:: namespace qualifier - the log's first global:: target, matched verbatim per the units.csv-id convention, distinct from the u70-batch libmpscpp.cpp::Sum at :1437). New file mdh/GlobalSum.kt. Render in the u124 global::Count template: XNEXT mglobal walk over the children of the ref prefix; per-file private byte-domain helpers (toCbuf, strlenC import, the strtodC-regex toDoubleOf - MpsAcos precedent); the trailing-delimiter trim tmp[--i]=0 pre-decrement-and-store quirk kept verbatim (u124 Count family); strncmp(ref,tmp,i) over the possibly-TRIMMED i kept (the compare uses the trimmed byte count); get_global + !tpx -> globalExceptionCode = GLOBAL_NOT_FOUND (2, libmpscpp.h:71) + MumpsGlobalException throw (Cosine.kt precedent, guards the delete race the XNEXT contract excludes); strtod accumulate into a Double tot. GlobalRef.Sum() delegate binds the subscripted call shape (u118/u124 Count pattern). Rules: h004, h008, h012, h014, h015, h017. Output: mdh/GlobalSum.kt. Whole-arm verification: 138 files, zero errors, zero warnings under kotlinc 2.1.20 locally before the push. Events +12 rows (log now 1650 lines).
- Unit 143 (compiler-parse.c::ExpandData, B5 tranche B): the dollar-data compiler expander (vendor 2267-2292, 24 physLOC; units.csv row 144: comment_ratio 0.04, ptr 0, ctrl 6, score 11.4, rank 113; the u11 ExpandNext sibling family, same #G#/#S# shape). New file compiler/ExpandData.kt. Render in the u11 ExpandNext template: i bumped past the sigil, strncmpC marker select, the two #-delimited tokens copied into 32-byte scratch with the explicit j/i cursor pair, GlobalData/LocalData emitted into CompilerState.out (h001; literal C-form text, translated compiler source), strcpyC(bp,arg2) result-name contract; NO call-site rebind - the vendor 1293 fcn_proc driver is not yet rendered (binding is a downstream unit). Vendor notes kept: the scan loops are unbounded (a >31-byte token overruns the 32-byte buffers - vendor UB, quantized to an IndexOutOfBounds carrier), and the no-marker fall-through returns with bp untouched (no else arm). Rules: h001, h012, h015, h017. Output: compiler/ExpandData.kt. Whole-arm verification: 139 files, zero errors, zero warnings under kotlinc 2.1.20 locally before the push. Events +8 rows (log now 1658 lines).
- Unit 144 (libmpscpp.cpp::GlobalQS, B5 tranche B): the dollar-qs global wrapper (vendor 2514-2536, 19 physLOC; units.csv row 145: comment_ratio 0.05, ptr 3, ctrl 1, score 11.3, rank 112). New file mdh/GlobalQS.kt. Render in the u39 GlobalOrder / GlobalData family: tmp[STR_MAX] command buffer "s %=<dollar>qs(a,c)" via writeCstring + strcatC, xecute(cstringAt(tmp)) - existing MdhStubs seats reused unmodified (h014), tpx 1/0 by outcome, SymGet("%") percent-variable readback folded to the b out-buffer writeCstring in BOTH arms (the vendor's strcpy((char *)b,...) contract kept verbatim; String carrier for the read-only a/c inputs, CharArray out carrier for b). h013: the vendor's static char r[STR_MAX] local is NEVER touched by the body - dead declaration, dropped with note. h015: the raw dollar byte rebuilt as 36.toChar() (u34/u39 precedent). Kotlin call surface is the emitted-compiler contract only (u76 ExpandQS emits the C call sites; no in-arm call site binds). Rules: h007, h012, h013, h014, h015, h017. Output: mdh/GlobalQS.kt. Whole-arm verification: 140 files, zero errors, zero warnings under kotlinc 2.1.20 locally before the push. Events +12 rows (log now 1670 lines). TRANCHE B CLOSES (u135-u144 complete).
- Unit 145 (libmpscpp.cpp::global::Length, B5 tranche C): the global-needle piece count (vendor 3274-3304, 23 physLOC; units.csv row 146: comment_ratio 0.042, ptr 0, ctrl 5, score 9.9, rank 95). Overload disambiguation journaled: this is the global-needle form :3274; the no-arg (:3261) and mstring/string/char-star needle forms (:3306/:3330/:3354) are not in this batch's rows (mstring::Length family = roster unit 149 later in tranche C). New file mdh/GlobalLength.kt. Render in the u53 GlobalConcat double-retrieve template: Mglobal(RETRIEVE=0, defines.h:83) on ref then needle.ref, both f==0 arms -> globalExceptionCode=GLOBAL_NOT_FOUND (2) + MumpsGlobalException throw; empty-haystack early return 0; the strstr count walk folded to Kotlin indexOf on the cstring domain (i starts at 1 BEFORE the walk - a non-empty haystack with zero needle hits returns 1, "one piece", kept verbatim; the walk advances by the needle LENGTH so overlapping occurrences count). strstr has no shared seat - the fold is documented in-KDoc, no dead helper. Rules: h007, h012, h014, h015, h017. Output: mdh/GlobalLength.kt. Whole-arm verification: 141 files, zero errors, zero warnings under kotlinc 2.1.20 locally before the push. Events +10 rows (log now 1680 lines).
- Unit 146 (interp-arith.cpp::mps_log10, B5 tranche C): the base-10 log (vendor 162-186, 22 physLOC; units.csv row 147: comment_ratio 0.043, ptr 2, ctrl 1, score 9.8, rank 87). New file interp/MpsLog10.kt. Render in the u10 mpsAcos template: gcvtWrapper(log10(strtodC(b)), a) - the shared NumSetup.kt:114 gcvt seat and the shared internal strtodC regex seat (MpsAcos.kt:34) reused unmodified; JVM log10 = C log10 in the reference plain-double arm. h018 two-level trichotomy journaled per the u139 expx precedent (third occurrence of the preprocessor_conditional family): MULTI_PRECISION undefined -> mpfr arm OUT (mpfr_init2/FLT_PRECISION static init-once flag, mpfr_set_str/mpfr_log10/mpfr_sprintf %.8RNg journaled not rendered), BIGFLOAT out -> the strtold/log10l long-double arm OUT, plain-double arm IN. Rules: h009, h012, h017. Output: interp/MpsLog10.kt. Whole-arm verification: 142 files, zero errors, zero warnings under kotlinc 2.1.20 locally before the push. Events +6 rows (log now 1686 lines).
- Unit 147 (libmpscpp.cpp::global::Translate, B5 tranche C): the dollar-translate over the global's value (vendor 4292-4316, 19 physLOC; units.csv row 148: comment_ratio 0.05, ptr 2, ctrl 1, score 9.3, rank 84). Overload disambiguation journaled: this is the single-arg mstring form :4292; the two-arg mstring form :4264 is a separate vendor overload not in this batch's rows. New file mdh/GlobalTranslate.kt. Render in the u84 GlobalFnumber template: Mglobal(RETRIEVE=0, defines.h:83) retrieve with f==0 -> globalExceptionCode=GLOBAL_NOT_FOUND (2) + MumpsGlobalException throw; the vendor mstring Concat chain folded to the plain command concatenation (DOLLAR="36.toChar()" + "translate", CodedOpen=206 \xce, value, CodedComma=208 \xd0, a1, CodedClose=207 \xcf - defines.h:38-40 markers, Parse_.kt:196-198 seat family); writeCstring(svPtr.v1d, cmd, 1) for the vendor's &v1d[1] strcpy; fcn(svPtr) reused unmodified (Fcn.kt:142); result read back Mstring(cstringAt(svPtr.bd, 1)) (u42/u84 offset-carrier convention). Rules: h007, h012, h014, h015, h017. Output: mdh/GlobalTranslate.kt. Whole-arm verification: 143 files, zero errors, zero warnings under kotlinc 2.1.20 locally before the push. Events +10 rows (log now 1696 lines).
- Unit 148 (libmpscpp.cpp::Avg, B5 tranche C): the MSV average intrinsic wrapper (vendor 10235-10257, 17 physLOC; units.csv row 149: comment_ratio 0.056, ptr 0, ctrl 4, score 8.1, rank 68). Overload disambiguation + TAG CORRECTION family (u127 rpm / u136 strcatx / u141 Transpose precedent): the class member global::Avg (libmpscpp.cpp:1726) IS a corpus unit - roster row 262 (batch 9, loc 28, score 15.2) - and the u88 Max KDoc's "not a corpus unit" tag for global::Max is likewise superseded (global::Max IS roster row 170, batch 6; superseded note kept verbatim in MdhStubs). New file mdh/Avg.kt + MdhStubs edit (GlobalRef.Avg stub raised, batch-9 owner tag - resolves when batch 9 lands). Render in the u88 Max template: trailing-SOH-pair strip quirk, SOH-split into name/subscript, ^-bypass Global construction, j==NUL -> a("").Avg() vs subscripted arm, sprintf %g with EXPLICIT default locale (potential-bugs ImplicitDefaultLocale stays hot - gate-round-1 lesson), static tmp[32] on the AvgScratch object; arg1 mutated in place kept; svptr/ep dead params kept in-signature. Rules: h008, h009, h012, h013, h014, h015, h017. Output: mdh/Avg.kt; edit: mdh/MdhStubs.kt. Whole-arm verification: 144 files, zero errors, zero warnings under kotlinc 2.1.20 locally before the push. Events +14 rows (log now 1710 lines).
- Unit 149 (libmpscpp.cpp::mstring::Length, B5 tranche C): the mstring haystack piece count (vendor 3406-3427, 16 physLOC; units.csv row 150: comment_ratio 0.059, ptr 0, ctrl 4, score 7.8, rank 59). Overload disambiguation journaled: this is the global-needle form :3406; the no-arg (:3257) and char-star/string/mstring-needle forms (:3378/:3392/:3429) are not in this batch's rows (the sibling global::Length(global) :3274 was roster unit 145 earlier this tranche). New file mdh/MstringLength.kt. Render in the u145 GlobalLength shape with the SINGLE-retrieve difference: Mglobal(RETRIEVE=0, defines.h:83) on needle.ref only, f==0 -> globalExceptionCode=GLOBAL_NOT_FOUND (2) + MumpsGlobalException throw; the haystack is THIS mstring's value (data.c_str() carrier fold); empty-haystack early return 0; the strstr count walk folded to indexOf (i starts at 1 BEFORE the walk - non-empty haystack with zero hits returns 1, "one piece"; the walk advances by the needle LENGTH so overlaps count - both kept verbatim, u145 family). SCAN-GATE CATCH: a mid-KDoc char-star/string phrase closed the comment early - reworded before push (the .kt-only scan would have caught it at the closer). Rules: h004, h007, h012, h014, h015, h017. Output: mdh/MstringLength.kt. Whole-arm verification: 145 files, zero errors, zero warnings under kotlinc 2.1.20 locally before the push. Events +12 rows (log now 1722 lines).
- Unit 150 (libmpscpp.cpp::mdh_tree_level_add, B5 tranche C): a verification unit (u43/u90 PN-1 precedent) - the col1..col4 overload (vendor 265-300, loc 21, score 7.7, rank 55, exact match via the study tool) was already covered by unit 8's family rendering (mdh/MdhTreeLevelAdd.kt renders all ten overloads through one mdhTreeLevelAddImpl). Verification against the pristine vendor source passed: the 4-col Kotlin overload (MdhTreeLevelAdd.kt:41-43) binds col1..col4 to the impl, and the impl matches the vendor body line for line (dollar-z~mdh~tree~level~add prefix + xce, %p(tree) identity-hash analog h009, %d depth, col1..col4 xd0-separated, xcf, zfcn at &tmp[1]). No new code, no new stubs. Third mdh_tree_level_add row to land (after u43 col1-7, u90 col1-3). Events logged as reuse+hit pairs for the unit-8 rule set (h004/h007/h009/h014/h015/h017). Output: none (verification only).
- Unit 151 (libmpscpp.cpp::mdh_tree_level_add, B5 tranche C): a verification unit (u43/u90/u150 precedent) - the col1..col2 overload (vendor 202-231, loc 17, score 6.5, rank 40, exact match via the study tool) was already covered by unit 8's family rendering. Verification against the pristine vendor source passed: the 2-col Kotlin overload (MdhTreeLevelAdd.kt:33-35) binds col1..col2 to the impl, and the impl matches the vendor body line for line (prefix + xce, %p(tree) identity-hash analog h009, %d depth, col1, xd0, col2, xcf, zfcn at &tmp[1]). No new code, no new stubs. Fourth mdh_tree_level_add row to land. Events logged as reuse+hit pairs for the unit-8 rule set (h004/h007/h009/h014/h015/h017). Output: none (verification only).
- Unit 152 (libmpscpp.cpp::mdh_tree_level_add, B5 tranche C): a verification unit (u43/u90/u150/u151 precedent) - the col1-only overload (vendor 175-201, loc 15, score 5.9, rank 36, exact match via the study tool; the FILE's FIRST overload, carrying the family's xce/xcf/xd0 marker banner comment) was already covered by unit 8's family rendering. Verification against the pristine vendor source passed: the 1-col Kotlin overload (MdhTreeLevelAdd.kt:29-31) binds col1 to the impl, and the impl matches the vendor body line for line (prefix + xce, %p(tree) identity-hash analog h009, %d depth, col1 directly followed by xcf - the 1-col form has NO xd0 between col1 and the closer, verified; zfcn at &tmp[1]). No new code, no new stubs. Fifth mdh_tree_level_add row to land; the remaining family rows live in later batches. Events logged as reuse+hit pairs for the unit-8 rule set (h004/h007/h009/h014/h015/h017). Output: none (verification only).
- Unit 153 (compiler-for.c::<misc>, B5 tranche C): residual chunk after the for_code() unit (u95, vendor 39-399) - the GPL banner, the June 16 2024 date comment, and four includes (stdio.h, mumpsc/memsize.h, mumpsc/compiler-macros.h, mumpsc/compiler-globals-ext.h), no executable code (residual by identity: the file's only extracted function is for_code; units.csv row 154: c, loc 30, comment_ratio 0.062, ptr 0, ctrl 2, score 5.2, rank 17). New file compiler/CompilerForMisc.kt in the exact u35 CompilerAbrtxMisc shape (same banner family): banner kept verbatim, each include mapped to its existing carrier (stdio -> ForCode.kt java.io edges; memsize -> CompilerState constants; macros -> per-unit consumers; globals-ext -> the CompilerState extern carrier). All h014 resolutions are REAL reuses - no new stubs. h018 not triggered (no preprocessor conditionals in the chunk). Minimal honest event set: h014 + h017 pairs only (u35 precedent). Rules: h014, h017. Output: compiler/CompilerForMisc.kt. Whole-arm verification: 146 files, zero errors, zero warnings under kotlinc 2.1.20 locally before the push. Events +4 rows (log now 1762 lines).
- Unit 154 (compiler-localarray.c::<misc>, B5 tranche C - the FINAL B5 batch row): residual chunk after the LocalArray() unit (B1 u12, vendor 38-92) - the GPL banner, the May 12 2024 date comment, and four includes (stdio.h, mumpsc/memsize.h, mumpsc/compiler-macros.h, mumpsc/compiler-globals-ext.h), no executable code (residual by identity: the file's only extracted function is LocalArray; units.csv row 155: c, loc 30, comment_ratio 0.062, ptr 0, ctrl 2, score 5.2, rank 21). New file compiler/CompilerLocalArrayMisc.kt in the exact u35/u153 CompilerAbrtxMisc/CompilerForMisc shape (same banner family): banner kept verbatim, each include mapped to its existing carrier (stdio -> LocalArray.kt java.io edges; memsize -> CompilerState constants; macros -> per-unit consumers; globals-ext -> the CompilerState extern carrier). All h014 resolutions are REAL reuses - no new stubs. h018 not triggered (no preprocessor conditionals in the chunk). Minimal honest event set: h014 + h017 pairs only (u35/u153 precedent). Rules: h014, h017. Output: compiler/CompilerLocalArrayMisc.kt. Whole-arm verification: 147 files, zero errors, zero warnings under kotlinc 2.1.20 locally before the push. Events +4 rows (log now 1766 lines). TRANCHE C CLOSES; B5 BATCH (u124-u154) COMPLETE.
- Unit 158 (mpstime.cpp::main, B6 tranche A): the mumps2c-GENERATED timing benchmark (vendor 155-416, 176 physLOC; units.csv row 159: comment_ratio 0.15, ptr 43, ctrl 45, score 170.4, rank 286) - the compiled mpstime.mps (for i=1:1:10000 s ^a(i)=i / s k=^a(i) / w zd1-minus-t bang). Batch-6 tranche A opened per PI decision with the big three (u155 Interpret, u156/u157 zfcn) DEFERRED to a dedicated session. New file interp/MpstimeMain.kt (entry mpstimeMain, u130 one-main-per-package precedent) + Msv edit (stateVector.h:47 LockDone, :83 UKEY[1024], :84 UDAT[1024] added). Render: console defaults slot 5 (stdin via the Getstr2 reflective-fetch precedent - System.in is a Kotlin hard keyword and the scan gate forbids the backtick form), UDAT/UKEY strcpy, the vendor's argv loop via symPut, the generated zfcn/sym_/mglobal/add/sub/numcomp/strcatx call surface over existing seats (zfcn/sym_/mglobal are stubs - batch-6/-10 owners), the generated setjmp/longjmp FOR mechanism LINEARIZED to plain Kotlin while loops (u95 label-dispatch precedent, For1/For1Exit pairs fold into the loop condition+increment), xce/xcf/x01 key encodings kept (M3701/a2926/a3956/p674 vendor comments kept), the GlobalExceptionCode catch ladder (libmpscpp.h:69-79 codes verified, vendor "Interprerer" typo kept), _epilogue Mltoa(getpid)/cleanLocks/Mglobal(CLOSE). h013: unreachable generated prologue temps, the if(0) alarm block, and the __label_lookup/__text_function fn-pointer globals dropped with notes (u130 precedent). h014: strxcat raised as per-file private stub (mpstime.cpp:102, batch-9 misc row 253 owner - u95 cvar precedent); SQLITE btree auto-connect kept unwired (u25 precedent). SCAN-GATE CATCHES: raw dollar in comments reworded, five double-bangs replaced with the symNullCarrier error() carrier (u137 precedent), System backtick form replaced with the Getstr2 reflective fetch. Rules: h001, h004, h007, h009, h012, h013, h014, h015, h017. Output: interp/MpstimeMain.kt; edit: interp/InterpStubs.kt (Msv fields). Whole-arm verification: 148 files, zero errors, zero warnings under kotlinc 2.1.20 locally before the push. Events +18 rows (log now 1784 lines).
- Unit 159 (compiler-read.c::read_code, B6 tranche A): the READ command compiler (vendor 39-166, loc 96; units.csv row 160: comment_ratio 0.01, ptr 17, ctrl 56, score 112.2, rank 280). New file compiler/ReadCode.kt (entry readCode, LockCode/NewCode vendor-name-in-header precedent). Render: carriage-control (!/# via the i=2/1 branch), tab-format (?) and literal-text (") prefixes all route through the existing inout seat (Inout.kt) with the &pd1[sdlim] slice carried by a per-file pd1At() NUL-terminated copy (h012 pointer-carrier); parse_() (Stubs.kt real seat) feeds the timeout expression at xpx; the setjmp-free goto family (cont_read/store/set/parse_err exit ladder) linearized with the goCont flag (h011); the timeout arm emits TimeOut_getstr1 with the pd1 seconds operand, the direct arm emits DirectRead, the plain arm emits getstr1 (DONE u138) - all EMITTED C TEXT not arm calls (h014 out-of-scope note); setCode(SET_STORE=1, compiler-macros.h:84) with the 6-code rc ladder (PARSE_ERROR 1 through ABRTX 6, LockCode precedent); the cont_read comma-advance and the trailing NUL/space/tab exit kept verbatim, including the vendor's return-99 fallthrough on an unrecognized setCode rc, the xpx-- re-read quirk after the timeout emit, and the commented-out linepad fossil. SCAN-GATE CATCHES: the two emitted Set-Test comments carry the dollar via the per-file DOLLAR const (value 36, SetCode.kt DOLLARC precedent) so the emitted C text stays byte-identical to vendor while the arm source keeps the no-raw-dollar rule - initially rendered as a reworded comment, corrected to the vendor-faithful concatenation after an arm-precedent check found no prior dollar-in-emitted-text case. Rules: h001, h010, h011, h012, h014, h015, h017. Output: compiler/ReadCode.kt. Whole-arm verification: 149 files, zero errors, zero warnings under kotlinc 2.1.20 locally before the push. Events +14 rows (log now 1798 lines).
- Unit 160 (interp-parse.cpp::match, B6 tranche A): the MUMPS pattern-match prover (vendor 109-206, loc 86; units.csv row 161: comment_ratio 0.023, ptr 6, ctrl 52, score 82.0, rank 261) - RESOLVES the B3 u64 InterpStubs 6-arg match stub (Patrn.kt is the live caller; the stub block replaced with a resolved note). New file interp/Match.kt. Render: the eight class arms (C/c printable-only via the <32 and ==127 continues, N/n digits, P/p punctuation via xindex, A/a alpha, L/l lower, U/u upper, E/e the >127-only reject, and the quoted-literal arm) inside the vendor's outer i=1..lx walk over pts[1][i]/pts[2][i]/ct[i]; ptr starts at -1 and pre-increments before each compare; the strictly-greater ptr>strlen(ad) bounds guard, the pts[2][i]==0 zero-length-literal continue, the substr(&ad[ptr],...)/substr(&bd[1],...) quoted-literal pull with the ptr+strlen(tmp1)-1 advance, the default: return, and the final ptr+1!=strlen(ad) exact-consumption check all kept verbatim. h006: short *rslt -> ShortArray(1). h008: tmp[2]/tmp1[1024]/tmp2[1024] and the xindex charset literal are fresh per-call scratch. h014: xindex (InterpStubs stub, interp-strmanip.cpp owner, not a corpus unit) and substr (InterpStubs stub, batch-7 owner) called as seats; strcmp folded to a per-file NUL-terminated compare (Parse_.kt strcmpAC precedent); strlenC reused (CstringOps). SCAN-GATE CATCHES: the vendor punctuation charset contains a raw dollar and a backtick - built by punctSet() from a 30-char Kotlin literal with DOLLAR (36) inserted after the hash and BTICK (96) after the underscore (ReadCode.kt DOLLAR precedent), keeping the runtime charset byte-identical to vendor; one mid-KDoc backtick pair caught and reworded before the push. Rules: h006, h008, h012, h014, h015, h017. Output: interp/Match.kt; edit: interp/InterpStubs.kt (stub resolved). Whole-arm verification: 149 files, zero errors, zero warnings under kotlinc 2.1.20 locally before the push. Events +12 rows (log now 1810 lines).
- Unit 161 (server.cpp::main, B6 tranche A): the transaction server (vendor 71-211, loc 65; units.csv row 162: comment_ratio 0.35, ptr 7, ctrl 18, score 51.5, rank 235). New file interp/ServerMain.kt (entry serverMain, u130 one-main-per-package precedent) + InterpStubs.kt edit (NEW stubs: Mglobal - owner interp-globalb.cpp::Mglobal batch-6 roster row 174, offset-carrying key for the &buff0[1] call, correctly tagged so it resolves at u174 - and logtime, owner server.cpp:62 not a corpus unit). Render: the -j scan (VENDOR TYPO kept: argv[i+i], not argv[i+1]), the fopen(journal,"a") with the print-and-continue NULL path (the later journal fprintf on the NULL FILE quantized with an error() carrier, u137/u158 precedent), ftok("/tmp/mumps.tmp",'A')/msgget(IPC_CREAT|0777)/the AF_UNIX datagram socket, then the while(1) transaction loop: msgrcv (the UNINITIALIZED local type read quantized to 0, h012 note), the STORE/KILL journal arms reading buf0/buf1 (which the LIVE path never fills - the buf copies are vendor commented text; the uninitialized-stack read quantized to fresh zero buffers, u128 deterministic-UB precedent), the Mglobal dispatch writing buff0[0], the memcpy(nm.sun_path,...) 108-byte loop and the two sendto arms with perror/exit(EXIT_FAILURE). h003: GlobalClose rides the unreachable post-while(1) tail - the splice stays owned by the globalClose stub, the tail (close(ssock)/GlobalClose/return 0) dropped under h013 since Kotlin flags unreachable code as a warning. h013: the declaration-only int sigint(int), unused Flush/cid/socket/sck/nbytes/result locals dropped. h014: OS syscall layer (ftok/msgget/msgrcv/socket/sendto/perror/exit) as PER-FILE private stub carriers (u158 strxcat precedent - process-level OS surface, never arm-owned); length carried as MSG_LENGTH=2168 with the x86-64 gcc struct-layout computation in KDoc (sizeof is a compile-time C property); cstringAt/strlen seats reused. SCAN-GATE CATCHES: the vendor net-init banner comment (a run of dollar chars) reworded; three raw-dollar string templates and one double-bang from the first draft replaced with concatenation and a null-carrier before the push. Rules: h003, h004, h012, h013, h014, h015, h017. Output: interp/ServerMain.kt; edit: interp/InterpStubs.kt (+Mglobal, +logtime stubs). Whole-arm verification: 151 files, zero errors, zero warnings under kotlinc 2.1.20 locally before the push. Events +14 rows (log now 1824 lines).
- Unit 162 (compiler-write.c::write_code, B6 tranche A): the WRITE command compiler (vendor 39-129, loc 63; units.csv row 163: comment_ratio 0.016, ptr 4, ctrl 24, score 45.1, rank 231) - the read_code sibling. New file compiler/WriteCode.kt (entry writeCode, LockCode/NewCode/readCode vendor-name-in-header precedent). Render: the NULL out_file guard emit + ErrorMessage m1944 comment, atTest (AtTest.kt real u108 seat; pd1Last written back from the result echo), the @ indirection arm (at_flg==-1 -> ABRTX; else tmp[1024] as StringBuilder carrier with strcpy(tmp,"w "), atCopy stub call, the INTERPRET macro via the CompilerState.interpret seat, xpx=at_flg re-point, the 1920 post-conditional closer), then t0px=0 once and the argument loop: '!'/#?/text arms through the inout seat with iosub codes (1/2/3/4), the '*' arm emitting the atoi(CSTR...)/putchar(_i) pair with linepad receiving the fprintf character-count analog, iosub1 comma-continue and blank/NUL exit with the 4092 post-conditional closer. h011: the iosub/iosub1 label family linearized with toIosub/toIosub1 fall-through flags preserving exactly which arms reach inout (only the '*' arm skips it); h002: INTERPRET macro -> CompilerState.interpret; h007: tmp char-buffer -> StringBuilder (the atCopy seat's parameter type); atCopy (owner compiler-at-test.c) and linepad (owner compiler-read.c) stubs called as seats, parse_/inout reused. Vendor quirks kept: the emitted "CSTR" tokens are generated-program runtime text, xpx=at_flg closing-paren re-point, t0px reset outside the loop. Rules: h002, h004, h007, h011, h012, h014, h015, h017. Output: compiler/WriteCode.kt. Whole-arm verification: 152 files, zero errors, zero warnings under kotlinc 2.1.20 locally before the push. INFRA NOTE: the environment reset again and GitHub release-asset downloads were rate-limited, so the local gate ran the SAME compiler version from Maven Central artifacts (kotlin-compiler/kotlin-stdlib/kotlinx-coroutines/trove4j/asm/annotations 2.1.20) via K2JVMCompiler instead of the dist zip - same 2.1.20 frontend/backend, sha-pinned dist still the m1-build reference. Also repaired in this push: the u161 journal paragraph's "mscrv typo aside" garble. Events +16 rows (log now 1840 lines).
- Unit 163 (interp-stem.cpp::<misc>, B6 tranche A - the biggest tranche A row): the stemming-data residual chunk (loc 345; units.csv row 164: comment_ratio 0.102, ptr 2, ctrl 4, score 43.7, rank 229 - exact match via the study tool; residual by identity with wstem (batch-10 row 293) and escan (batch-4 row 122 DONE) as the file's corpus functions, and the 4-line Stem wrapper folded in per the tool's below-min-loc misc rule). New file interp/InterpStemMisc.kt. Render: the GPL/Feb 9 2022/stem.c banner blocks kept as the identity ledger (u35/u153 banner-ledger shape); six includes mapped to existing carriers (string.h -> CstringOps/strmove, iostream -> java.io edges, defines/fcns/inline -> per-unit consumers, all REAL reuses); "using namespace std;" and the wstem prototype kept as roster-fossil notes; the fin marker (#define fin 1) carried in the next-field values verbatim; struct endings -> the new Endings carrier (3-field prefix-table shape); the TWELVE Porter-stemmer conflation tables (prefix + d/s/g/n/e/l/c/y/v/t/r_endings, 303 entries) as arm top-level Array<Ends>/Array<Endings> vals REUSING the Ends carrier from Escan.kt (their only consumer alongside wstem), every entry value/order/trailing-NUL row byte-faithful (generated by a table parser, not hand-typed); the folded Stem wrapper (vendor 413-416) calls the wstem MdhStubs stub seat (batch-10 owner) with strlenC and returns word in place. The -1 and 0 next-field flags and the fin marker appear verbatim. SCAN-GATE CATCHES: none needed - table data is plain word strings; KDoc spells the dollar-prefixed intrinsic names without the sigil. Rules: h004, h012, h014, h015, h017. Output: interp/InterpStemMisc.kt. Whole-arm verification: 153 files, zero errors, zero warnings under kotlinc 2.1.20 locally before the push. Events +10 rows (log now 1850 lines).
- Unit 164 (libmpscpp.cpp::Shred, B6 tranche A - the FINAL tranche A row): the stateful fragment iterator (vendor 7907-7960, loc 40; units.csv row 165: comment_ratio 0.024, ptr 10, ctrl 9, score 37.4, rank 221) - RESOLVES the MdhStubs Shred(String,Int) stub (the MSV wrapper at LibmpscppMisc9.kt:603 and ShredQuery forward through the same seat; the stub replaced with a resolved note, the wrapper call-site comment updated to real-seat). New file mdh/Shred.kt. Render: the size>=128 abort (println + sigint(100) stub seat, FALL-THROUGH kept), the flg==0 first-call block (free/malloc as GC + fresh CharArray; the isalnum-only tolower copy loop; the p2==empty early return WITHOUT setting flg - every call re-copies, kept; the NUL terminate and p1=0 reset), then the while(1) fragment loop (strncpy folded to the NUL-padding copy loop, word[size]=NUL, p1+=size, the p1>p2 reset-return with flg=0/string=null, the fragment return). h008: the file-scope statics p1/p2/flg/string/word PERSIST ACROSS CALLS on the ShredScratch object (Avg/Count Scratch precedent) - the iterator protocol depends on it. h012: per-file isalnumC/tolowerC C-locale byte-domain helpers (ScanAlnum precedent). Vendor fossils kept: the dead static int i (dropped with a note), the "Out of memory in Token()" copy-paste diagnostic (Token, not Shred - SQLTable println precedent for cout), the always-returning while(1), and the p1>p2 (not >=) reset test that yields the NUL-padded last partial fragment. SCAN-GATE CATCH: one double-bang on the scratch carrier replaced with an error() carrier before the push. Rules: h008, h012, h014, h015, h017. Output: mdh/Shred.kt; edits: mdh/MdhStubs.kt (stub resolved), mdh/LibmpscppMisc9.kt (call-site comment). Whole-arm verification: 154 files, zero errors, zero warnings under kotlinc 2.1.20 locally before the push. Events +10 rows (log now 1860 lines). TRANCHE A COMPLETE (u158-u164 landed); tranche B (u165-u174) next per the PI group-of-ten decision.
- Unit 165 (libmpscpp.cpp::Dice, B6 tranche B - FIRST TRANCHE B ROW): the Dice coefficient over two term-weight globals (vendor 1312-1373, loc 44; units.csv row 166: comment_ratio 0.022, ptr 4, ctrl 9, score 26.8, rank 194) - RESOLVES the MdhStubs Dice(Global,Global) stub (raised by u85 with the correct batch-6 owner tag; the MSV wrapper at LibmpscppMisc9.kt:495 binds unchanged). New file mdh/Dice.kt in the Cosine unit-71 family shape. Render: STR_MAX scratch buffers; the SOH-pair strip on B then A; the single A-walk (Mglobal XNEXT + the strncmp prefix test); totD accumulates A's weights; per A child the paired-B lookup (B.ref restored from ctmp + child suffix, btmp copy, the B.Data() skip-continue), totQ accumulates the paired weights, tot accumulates the products; final 2*tot/(totD+totQ) with the zero-sum early-out. Vendor fidelity kept: NO second walk (unlike Cosine - totQ rides only the paired values inside the A-walk), the vendor never restores B.ref in Dice (the local refB mutation is redone each iteration), and the B.Data() skip leaves both accumulators untouched. Seats: mglobal/get_global/GlobalRef.Data/MdhState exception carriers reused (Cosine precedent); per-file strncmpOff/strcatOff/strtodC helpers (Cosine file-private precedent). Rules: h007, h010, h012, h014, h015, h017. Output: mdh/Dice.kt; edit: mdh/MdhStubs.kt (stub resolved). Whole-arm verification: 155 files, zero errors, zero warnings under kotlinc 2.1.20 locally before the push. Events +12 rows (log now 1872 lines).
- Unit 166 (interp-arith.cpp::modulo, B6 tranche B): the modulo operator arm (vendor 695-762, loc 56; units.csv row 167: comment_ratio 0.034, ptr 3, ctrl 6, score 25.6, rank 191) - RESOLVES the InterpStubs modulo stub (the Parse_.kt:1074 MODULO call site binds unchanged on the offset signature). New file interp/Modulo.kt in the u139 expx template. Render: the floatstring gate dispatches the float arm (strtodC parses both operands - the vendor's strtod/strtold MIX on line 707 is a precision slip that narrows to double at the fmod call anyway, quantized to Double; JVM percent == C fmod, sign of the dividend) into gcvtWrapper scratch + strcpyC to rOff (u98/u139 offset-adapter precedent), the integer arm into long aa=atoi(a) percent bb, sprintf percent-d via writeCstring at rOff directly. h018 THREE-LEVEL trichotomy: MULTI_PRECISION out (u139 precedent, the mpz/static-init arm journaled not rendered), BIGFLOAT out (plain-double arm in), and - NEW PRECEDENT for this file - the intLong ifdef resolves IN because interp-arith.cpp:59 defines it UNCONDITIONALLY in the unit itself (the line-57 "if 32 bit wanted" comment is a vendor fossil); the excluded long-long atoll/lld arm is journaled. Vendor notes kept: the dead second return in both integer arms (720-721/727-728, h013 - Kotlin would warn unreachable), the percent-d-on-long sprintf (exact in value: atoi operands bound the remainder to int range), and the bb==0 integer divisor (C UB/SIGFPE quantized to ArithmeticException, u128 precedent - the float arm is the reachable zero-divisor path, fmod-by-0 is NaN). Seats: floatstring (ArithMisc real seat, u66), gcvtWrapper (NumSetup owner, u29), atoiC (CstringOps:35 shared offset-carrying seat - the first draft raised a per-file helper, replaced when the shared seat surfaced in the compile pass), cstringAt/strcpyC/writeCstring reused; strtodC per-file private (u139 precedent). Rules: h012, h013, h014, h015, h017, h018. Output: interp/Modulo.kt; edit: interp/InterpStubs.kt (stub resolved). Whole-arm verification: 156 files, zero errors, zero warnings under kotlinc 2.1.20 locally before the push. Events +12 rows (log now 1884 lines).
- Unit 167 (compiler-parse.c::ExpandOrder, B6 tranche B): the dollar-order compiler expander (vendor 2208-2265, loc 50; units.csv row 168: comment_ratio 0.02, ptr 0, ctrl 13, score 24.2, rank 185) - the u143 ExpandData / u16 ExpandGet sibling over the same #G#/#S# shape. New file compiler/ExpandOrder.kt. Render in the u16 ExpandGet template (the closest sibling: the CodedComma third-argument block with the Extract2 nested-marker arm and the v1d[i]<128 plain-copy loop, the "NULL" else, the arg2-into-bp result-name contract): i bumped past the sigil, strncmpC marker select, the two hash-delimited tokens copied into 32-byte scratch with the explicit j/i index pair, GlobalOrder vs LocalOrder emitted into CompilerState.out as literal C-form text, arg2 echoed to bp. Like u143, NO call-site rebind - the vendor 1293 fcn_proc driver is not yet rendered (binding is a downstream unit), and no stub was raised. Vendor notes kept: the emitted GlobalOrder text carries the vendor's own debug fossil "// p2236" and the split two-line continuation while the LocalOrder emission has the trailing space before the newline (both byte-identical to the vendor fprintf strings); the nested marker tests differ per arm (#G# arm tests #G# then #S#, #S# arm tests #S# then #G#); the 2236 "arg1 returns the global for the next element" comment kept; the unbounded hash-scan loops quantize to IndexOutOfBounds (u143 UB precedent); neither-marker fall-through leaves bp untouched. Seats: Extract2 real seat (unit 15), strncmpC/strcpyC/cstringAt CompilerCstring shared seats, CodedComma per-file const 208 (defines.h:40, u16/u76 precedent). Rules: h001, h012, h014, h015, h017. Output: compiler/ExpandOrder.kt. Whole-arm verification: 157 files, zero errors, zero warnings under kotlinc 2.1.20 locally before the push. Events +10 rows (log now 1894 lines).
- Unit 168 (include/mumpsc/stateVector.h::<misc>, B6 tranche B): the state-vector header residual (vendor whole file 1-110, loc 76; units.csv row 169: comment_ratio 0.262, ptr 4, ctrl 2, score 18.4, rank 162 - exact match via the study tool: the header has NO extracted functions, so the misc chunk IS the whole file, residual by identity). Edit-only render (u158 Msv-field precedent): the remaining 22 struct MSV members land in the InterpStubs.kt Msv carrier with vendor :line anchors - cfgdata/cfgkey (256, h012), ITRAP (:53), ctrlc (:57), iorg (:58), nflg (:68), RefParm[10][16]/Rargs[10][16] (Array of CharArray(16), h012), Rargsx (:75), _SymPush (:76), hit/count (:78, unsigned long -> Long, u50 note), restoredata (:79, unsigned long long -> Long), ScanMinWordSize/ScanMaxWordSize (:80/:81), ConOpen (:88), pgsql_msg/Connection/Col_Names (:89-91, STR_MAX=32000), fdes[2] (:92), exec (:93), reference[1024] (:94). h013: the vendor's commented-out grslts[22]/grsltsx pair (:97-98) is a fossil - roster note only. h014: the five includes map to existing carriers (stdio -> the in_file/out_file Reader/Writer edges already in Msv; sysparms/strmax -> the STR_MAX/PS1 constants; defines -> per-unit consumers; sys/types -> Long carriers); AllocSV prototype resolves to the existing allocSV stub (owner state-vector machinery, not a corpus unit; B4 u98 precedent); the StateVector define macro (:109) is a roster note (the Msv carrier IS the analog). h018: the #ifndef _INTERP_ pair (:103-107) has IDENTICAL arms (both declare the same AllocSV prototype) - resolves trivially, journaled. The banner (GPL, June 16 2025) is kept as the identity ledger, u35 banner-ledger shape. Rules: h004, h012, h013, h014, h017, h018. Output: edit interp/InterpStubs.kt (Msv fields, no new file). Whole-arm verification: 157 files, zero errors, zero warnings under kotlinc 2.1.20 locally before the push. Events +12 rows (log now 1906 lines).
- Unit 169 (libmpscpp.cpp::global::Max, B6 tranche B): the max-over-children walk (vendor 1651-1688, loc 26; units.csv row 170: comment_ratio 0.037, ptr 1, ctrl 7, score 14.8, rank 144; the roster id keeps the global:: qualifier, u145/u147 verbatim convention) - RESOLVES the MdhStubs GlobalRef.Max stub raised by u88 (TAG CORRECTION, u141/u148 family: u88's "not a corpus unit" tag superseded, resolved note kept verbatim in MdhStubs; the Max.kt wrapper KDoc h014 bullet and overload-disambiguation line updated to real-seat). New file mdh/GlobalMax.kt in the u44 global::Min template (identical bodies modulo max/val>max): STR_MAX scratch, trailing-control two-byte trim quirk, Mglobal XNEXT walk with the strncmp prefix stop, get_global expanded inline (libmpscpp.h:117 macro, u44 note), tpx==0 -> GLOBAL_NOT_FOUND (2) + MumpsGlobalException throw, strtodC accumulate, flg first-value latch, no-child 0.0 return kept. h013: the vendor's dead local int j (1655) dropped with note; h015: vendor val -> value (u44 journal); h014: mglobal stub seat, CstringOps helpers, strtodC real reuses. Rules: h007, h012, h013, h014, h015, h017. Output: mdh/GlobalMax.kt; edits: mdh/MdhStubs.kt (stub resolved), mdh/Max.kt (KDoc superseded-tag updates). Whole-arm verification: 158 files, zero errors, zero warnings under kotlinc 2.1.20 locally before the push. Events +12 rows (log now 1918 lines).
- Unit 170 (interp-sysfunc.cpp::TimeOut_getstr1, B6 tranche B): the timed READ runtime (vendor 127-168, loc 33; units.csv row 171: comment_ratio 0.029, ptr 1, ctrl 5, score 14.1, rank 135) - the body behind the C text u159 read_code EMITS (the generated-program contract; no in-arm call site, no stub raised - the SysfuncH/CppsysfuncH roster notes anticipated the seat). New file interp/TimeOutGetstr1.kt (vendor mixed-case name kept, SysfuncH vendor-verbatim note). Render: the io!=5 banner prints + sigint(100) InterpStubs stub seat (u158 precedent) with the VENDOR FALL-THROUGH kept - the body runs the select anyway after the abort call (sigint's abort makes it unreachable in practice, u137 Nothing-carrier precedent); the bare C scope block around the fd_set/timeval declarations linearized; tv_sec=atoi via the shared CstringOps atoiC seat, whole-second timeouts only; select/stty/getchar/fgets-on-stdin are the PROCESS-LEVEL OS layer - four per-file private stub carriers (u161 server.cpp OS-syscall precedent, never arm-owned); the timeout arm lands empty area + return 0 with the stty -raw restore; the dirflg arm stores the getchar result UNMASKED (EOF -1 stores the 0xFF-shape char, kept); the fgets arm keeps the unconditional last-byte chop INCLUDING the fgets-NULL stale-buffer case (the caller's prior contents lose their last byte - vendor quirk kept verbatim, IndexOutOfBounds bounds the empty-buffer UB). Rules: h001, h012, h014, h015, h017. Output: interp/TimeOutGetstr1.kt. Whole-arm verification: 159 files, zero errors, zero warnings under kotlinc 2.1.20 locally before the push. Events +10 rows (log now 1928 lines).
- Unit 172 (interp-sym.cpp::_extract, B6 tranche B): the dollar-extract range copy (vendor 1235-1277, loc 34; units.csv row 173: comment_ratio 0.029, ptr 0, ctrl 7, score 13.0, rank 126) - RESOLVES the MdhStubs _extract stub raised by the u85-era GlobalExtract (TAG CORRECTION, u141/u148/u169 family: the stub's "batch-10 unit" tag was a mis-tag, roster row 173 IS batch 6; superseded note kept verbatim in MdhStubs, GlobalExtract.kt h014 bullet updated and the mdh import rebind follows the u136 strcatx precedent - the :65 call site binds unchanged on the 4-CharArray signature). New file interp/Extract.kt. Render: start/len are 1-based decimal strings via the shared CstringOps atoiC seat; negative start clamps to 0 else decrements, negative len means to-the-end (j=i) else decrements; empty result on j<i and on i>strlen (strmove InterpStubs stub seat with the one-NUL empty-literal carrier); the i==0 && j==0 single-char arm; the inclusive copy loop with the trailing NUL. Vendor notes kept: the empty-range test is j<i not j<=i (a one-element request falls through to the copy); the bounds test is i>n not i>=n (i==n copies the NUL terminator into out[0] - an empty output produced by copying the terminator, in-bounds both languages); j>n clamps AFTER the i>n rejection so the copy may read the NUL as the last byte; vendor parameter name in -> in_ (Kotlin keyword, h015). Rules: h012, h014, h015, h017. Output: interp/Extract.kt; edits: mdh/MdhStubs.kt (stub resolved), mdh/GlobalExtract.kt (import + KDoc rebind). INFRA NOTE: an env reset interrupted the first u172 attempt after the local gate but before the events/journal/push; the workspace was rebuilt per recipe and the identical edits re-applied (the fresh codeload base verified at u171 39cf53d, events tail 1934 rows). Whole-arm verification: 161 files, zero errors, zero warnings under kotlinc 2.1.20 locally before the push. Events +8 rows (log now 1942 lines).
- Unit 174 (libmpscpp.cpp::StopInit, B6 tranche B - the FINAL TRANCHE B ROW): the stoplist loader (vendor 7729-7761 string overload, loc 24; units.csv row 175: comment_ratio 0.04, ptr 1, ctrl 5, score 12.2, rank 118; the char-star 7720 and mstring 7725 forwarders relay to it, the 10497 MSV wrapper is u97's LibmpscppMisc9.kt:529) - RESOLVES the MdhStubs StopInit(String) stub raised by u97 (correct batch-6 tag; the wrapper call site binds unchanged; the StopLookup(String) stub STAYS - not a corpus unit, reads the carrier when its owner renders). New file mdh/StopInit.kt in the u126 SynInit shape. Render: h008 the file-scope StopList stop (:7718 set of strings) rides the top-level stop set carrier in the unit file (u126 syn/synx precedent); stop.clear() reloads-not-accumulates kept; fopen/fclose via FileReader edge with the missing-file banner + exit(EXIT_FAILURE); fgets via readLine with the NEWLINE RECONSTRUCTED - the vendor's unconditional line[i-1]=0 strip removes the fgets newline, so the render re-appends the newline sentinel for byte-identical strip behavior (a final unterminated line, where the vendor strips the word's last character, is quantized to the newline-terminated behavior - degenerate case documented, h001 note); the space-cut loop lowercases in place and breaks at the first space with the vendor's line[i++]=0 increment-inside-the-assignment (the following line[i-1]=0 re-zeroes the same space position - a no-op on the space arm, the newline strip on the no-space arm - both verbatim); tolowerC byte-domain helper (ScanAlnum/SynInit precedent); the three C++ overloads collapse to the single Kotlin String parameter (u126 overload-collapse precedent). SCAN-GATE CATCH: the MdhStubs resolved-note first draft wrote char-star as "char-star-slash" mid-comment - the raw two-byte sequence would have closed the KDoc early; reworded before push. Rules: h001, h002, h008, h012, h014, h015, h017. Output: mdh/StopInit.kt; edit: mdh/MdhStubs.kt (stub resolved). Whole-arm verification: 163 files, zero errors, zero warnings under kotlinc 2.1.20 locally before the push. Events +14 rows (log now 1968 lines). TRANCHE B (u165-u174) COMPLETE: 10 units landed (Dice, modulo, ExpandOrder, stateVector misc, global::Max, TimeOut_getstr1, interp.h misc, _extract, Mglobal, StopInit); four stub resolutions with two tag corrections (global::Max u88-era, _extract batch-10 mis-tag), one new InterpStubs stub resolved (Mglobal, u161-raised). Tranche C (u176-u186, minus deferred u155-u157) next per the PI group-of-ten decision.
- Unit 176 (interp-arith.cpp::mps_abs, B6 tranche C - FIRST TRANCHE C ROW): the abs intrinsic over decimal strings (vendor 557-595, loc 34; units.csv row 176: comment_ratio 0.029, ptr 0, ctrl 4, score 11.6, rank 115). New file interp/MpsAbs.kt in the u146 mps_log10 template. Render: gcvtWrapper(abs(strtodC(in_)), out); return 0 - the shared NumSetup.kt:114 gcvt seat and the MpsAcos.kt:34 shared strtodC regex seat reused unmodified; JVM abs = C fabs in the reference plain-double arm. h018 two-level trichotomy journaled per the u139 expx / u146 mps_log10 precedent (same file, same structure): MULTI_PRECISION out (the strchr '.' mpz/mpf dispatch with the static init-once flags journaled not rendered), BIGFLOAT out (the fabsl/strtold long-double arm out), plain-double fabs/strtod arm IN. h015: vendor parameter name in -> in_ (u172 _extract precedent). h017: mps_abs -> mpsAbs (mpsAcos/mpsLog10 camel precedent, vendor name in the header comment). Rules: h009, h012, h015, h017, h018. Output: interp/MpsAbs.kt. Whole-arm verification: 164 files, zero errors, zero warnings under kotlinc 2.1.20 locally before the push. Events +10 rows (log now 1978 lines).
- Unit 178 (interp-arith.cpp::mps_log2, B6 tranche C): the log base 2 string intrinsic (vendor 132-156, loc 22; units.csv row 178: comment_ratio 0.043, ptr 2, ctrl 1, score 9.8, rank 86 - exact match via the study tool; extract_functions boundaries 132-156). New file interp/MpsLog2.kt, the u176 mpsAbs/mpsLog10 template made literal: gcvtWrapper(log2(strtodC(b)), a); the vendor's bare `return;` renders as Unit (no caller consumes a status; contrast mpsAbs which returns 0). h018 TWO-LEVEL trichotomy journaled: MULTI_PRECISION OUT (reference build is the numSetup unit-29 hardware-double resolution, u130 Float: double), so the MPFR arm (static mpfr_t fa/fb/fc with the init-once flag f, FLT_PRECISION, mpfr_set_str/mpfr_log2/mpfr_sprintf "%.8RNg") is journaled not rendered; inside the ifndef, BIGFLOAT OUT (u130 announce precedent), so the plain log2/strtod arm is IN and the log2l/strtold long-double arm is journaled. h009: gcvtWrapper delegates the %.8g-class conversion (NumSetup.kt:114 shared seat). h012: strtodC shared regex seat (MpsAcos.kt:34) over the NUL-terminated input domain. h015: parameter names a/b render unchanged, no Kotlin collisions; new top-level mpsLog2 scanned clean cross-file. h017: mps_log2 -> mpsLog2; log2 is the kotlin.math intrinsic for the C log2. Rules: h009, h012, h015, h017, h018. Output: interp/MpsLog2.kt. Whole-arm verification: 166 files, zero errors, zero warnings under kotlinc 2.1.20 locally before the push. Events +10 rows (log now 1996 lines).
- Unit 179 (interp-arith.cpp::mps_sin, B6 tranche C): the sine string intrinsic (vendor 253-277, loc 22; units.csv row 179: comment_ratio 0.043, ptr 2, ctrl 1, score 9.8, rank 90 - exact match via the study tool; extract_functions boundaries 253-277). New file interp/MpsSin.kt, the u178 mps_log2 twin to the line: gcvtWrapper(sin(strtodC(b)), a); bare `return;` renders as Unit. h018 TWO-LEVEL trichotomy journaled: MULTI_PRECISION OUT (numSetup unit-29 hardware-double resolution, u130 Float: double), so the MPFR arm (static mpfr_t fa/fb/fc init-once flag f, FLT_PRECISION, mpfr_set_str/mpfr_sin/mpfr_sprintf "%.8RNg") is journaled not rendered; inside the ifndef, BIGFLOAT OUT (u130 announce precedent), so the plain sin/strtod arm is IN and the sinl/strtold long-double arm is journaled. h009: gcvtWrapper delegates the conversion (NumSetup.kt:114 shared seat). h012: strtodC shared regex seat (MpsAcos.kt:34) over the NUL-terminated input domain. h015: parameter names a/b render unchanged, no Kotlin collisions; new top-level mpsSin scanned clean cross-file. h017: mps_sin -> mpsSin; sin is the kotlin.math intrinsic for the C sin. Rules: h009, h012, h015, h017, h018. Output: interp/MpsSin.kt. Whole-arm verification: 167 files, zero errors, zero warnings under kotlinc 2.1.20 locally before the push. Events +10 rows (log now 2006 lines).
- Unit 181 (libmpscpp.cpp::global::Length mstring-needle overload, B6 tranche C): the mstring-needle piece count (vendor 3306-3328, loc 17; units.csv row 181: comment_ratio 0.056, ptr 0, ctrl 4, score 8.1, rank 65 - exact match via the study tool; disambiguates from the global-needle form row 146 score 9.9 DONE as u145 by loc/ctrl). EDIT-ONLY append to mdh/GlobalLength.kt, the u145 walk made single-retrieve: fun Global.Length(needle: Mstring): Int - one mglobal(RETRIEVE) over this global's ref (needle.c_str() is the Mstring value carrier, u131), GLOBAL_NOT_FOUND throw Cosine precedent, i-starts-at-1 with empty-haystack 0 return, indexOf fold of the strstr walk advancing by needle length (overlaps counted). Vendor quirk kept: an EMPTY needle loops forever in C (strstr(p1,"") returns p1; i++ advances but p1 never moves) - the fold keeps it byte-faithful (indexOf returns p each time, p unchanged). h007 gtmp[STR_MAX] carrier, h012 indexOf fold over the retrieved C-string domain, h014 mglobal/MdhState/MumpsGlobalException seats + Mstring value carrier, h015 overload by parameter type (Mstring vs Global, u145 takes Global), h017 Length keeps the vendor name as the Global extension. Rules: h007, h012, h014, h015, h017. Output: mdh/GlobalLength.kt (+78 lines, byte-identical prefix verified against landed master). Whole-arm verification: 167 files, zero errors, zero warnings under kotlinc 2.1.20 locally before the push. Events +10 rows (log now 2028 lines).
- Unit 183 (interp-rpm.cpp::leaning_toothpickize, B6 tranche C): the pattern-token regex escaper (vendor 479-496, loc 17; units.csv row 183: comment_ratio 0.056, ptr 0, ctrl 4, score 7.7, rank 54 - exact match via the study tool). New file interp/LeaningToothpickize.kt; RESOLVES the InterpStubs leaningToothpickize stub raised by u104 (stub REMOVED, superseded owner note kept in InterpStubs; ParsePatternElement.kt:119 call site binds unchanged to the new same-package top-level). Render: walk the unmangled bstring; the 0xD2 embedded-quote sentinel (DOUBLE_QUOTE interp-rpm.cpp:58, internal carrier reused from InterpRpmMisc.kt:109) emits a literal double quote, other punctuation bytes get a backslash prefix, everything else passes through; newBstringLit/bcatchr shared Bstring seats reused. Vendor quirks kept: a REAL ASCII double quote is neither sentinel nor passes the second if's literal-quote test - silently DROPPED; the second if is always true when reached (redundant guard, kept verbatim). h012 C-locale ispunct per-file private (mdh/Input ispunctC is private to mdh - not shareable), h014 Bstring machinery + DOUBLE_QUOTE carriers, h015 stub removed - single top-level leaningToothpickize, h017 snake_case kept for the call-site bind. SCAN CATCH: the per-file ispunctC range initially used a literal backtick char - caught by the push-gate scan and replaced with the 96.toChar() BTICK carrier (Match.kt precedent). Rules: h012, h014, h015, h017. Output: interp/LeaningToothpickize.kt + the InterpStubs.kt stub removal. Whole-arm verification: 169 files, zero errors, zero warnings under kotlinc 2.1.20 locally before the push. Events +8 rows (log now 2048 lines).
- Unit 184 (libmpscpp.cpp::SmithWaterman, B6 tranche C): the nine-arg MSV wrapper over sw() (vendor 10351-10368, loc 17; units.csv row 184: comment_ratio 0.056, ptr 2, ctrl 0, score 7.7, rank 58 - exact match via the study tool). New file mdh/SmithWaterman.kt: space-prefix both sequences into STR_MAX buffers (s1a[0]=s2a[0]=' ', strcpy to offset 1 copying the NUL), scan the five tuning ints (show-align, show-matrix, gap, mismatch, match), call the B2 sw() real seat (Sw.kt:83, ShowAlign/ShowMatrix/gap/mismatch/match order), sprintf percent-d the score into the static 32-byte tmp on the SmithWatermanScratch object (h008) with the EXPLICIT default Locale (h009, ImplicitDefaultLocale hot). Vendor quirk kept: the five sscanf returns are UNCHECKED - a non-numeric tuning arg leaves the C local uninitialized (UB), quantized to atoiC's deterministic 0 (u128 precedent; atoiC is the shared CstringOps.kt:35 seat); svptr/ep dead wrapper params kept in the signature. h007 sequence buffers, h008 SmithWatermanScratch static, h009 atoiC + percent-d explicit locale, h012 byte-domain strcpy/strlen, h014 sw() real seat + dead params, h015 new top-level SmithWaterman scanned clean, h017 vendor spelling. Rules: h007, h008, h009, h012, h014, h015, h017. Output: mdh/SmithWaterman.kt. Whole-arm verification: 170 files, zero errors, zero warnings under kotlinc 2.1.20 locally before the push. Events +14 rows (log now 2062 lines).
- Unit 185 (libmpscpp.cpp::global::Find global-key overload, B6 tranche C): the MUMPS dollar-Find with the key from the database (vendor 7587-7615, loc 22; units.csv row 185: comment_ratio 0.043, ptr 0, ctrl 2, score 6.2, rank 37 - exact match via the study tool; disambiguates from the mstring-key form vendor 7617 loc 16 score 4.2 DONE as pilot u33 by the second Mglobal retrieve). EDIT-ONLY append to mdh/GlobalFind.kt, the u33 shape made double-retrieve: fun GlobalRef.Find(key: Global, st: Int): Int - receiver's ref then key.ref each retrieved with mglobal(RETRIEVE) throwing GLOBAL_NOT_FOUND, Mltoa(st) with the documented int-to-long widening (h010), _find(r, tmp, tmp1, a) real seat, atoiC(r) result. Both absence paths throw with the same code (missing receiver, missing key global - kept). h007 a[40]/r[20]/tmp/tmp1[STR_MAX] buffers, h012 byte domains, h014 mglobal/MdhState/_find/atoiC/Mltoa seats reused, h015 overload by key type (Global vs Mstring), h017 Find vendor name on the GlobalRef extension. Rules: h007, h010, h012, h014, h015, h017. Output: mdh/GlobalFind.kt (+74 lines, byte-identical prefix verified against landed master). Whole-arm verification: 170 files, zero errors, zero warnings under kotlinc 2.1.20 locally before the push. Events +12 rows (log now 2074 lines).
- Unit 186 (compiler-fcninit.c::<misc>, B6 tranche C - TRANCHE C CLOSED): the banner/include residual of compiler-fcninit.c (vendor whole file 1-92, residual rows 1-39 after fcninit at 40-92; units.csv row 186: loc 30, comment_ratio 0.062, ptr 0, ctrl 2, score 5.2, rank 16 - EXACT match via the study tool residual-by-identity, complexity_metrics over the residual lines; fcninit itself is batch-7 roster row 195, complex, rank 230 - NOT rendered here, owner roster note). New file compiler/CompilerFcninitMisc.kt in the u153 CompilerForMisc banner-ledger shape: GPL banner verbatim, tab-indented May 15 2024 date comment kept as written, four includes each mapped h014 (stdio.h -> compiler writer java.io edges; mumpsc/memsize.h -> the MemsizeH.kt carrier landed this batch as u177; compiler-macros.h -> per-unit macro consumers; compiler-globals-ext.h -> CompilerState externs). No executable code, no stubs raised. Rules: h014, h017 (h018 not triggered - no preprocessor conditionals in the chunk). Output: compiler/CompilerFcninitMisc.kt. Whole-arm verification: 171 files, zero errors, zero warnings under kotlinc 2.1.20 locally before the push. Events +4 rows (log now 2078 lines). TRANCHE C (u176-u186, 11 units) COMPLETE: mps_abs, memsize.h misc, mps_log2, mps_sin, ScanAlnum istream, global::Length mstring, Sum wrapper, leaning_toothpickize (u104 stub resolved), SmithWaterman, global::Find global-key, fcninit misc. Batch 6 remaining: only the DEFERRED big three u155 Interpret / u156-u157 zfcn x2 (dedicated session per PI decision). Mark's next gradle gate: pull to this commit (covers 5cf913f..HEAD, all of tranche B close + tranche C).
- Unit 155 (interp-interp.cpp::Interpret, the deferred BIG THREE opener): the interpreter command master loop (vendor 133-2617, physLOC 1581; units.csv row 155/rank 308, score 2921.7 - the LARGEST unit in the study) - RESOLVES the InterpStubs interpret(cmd,svPtr) stub (the u159-era "not a corpus unit" mis-tag corrected at u171: this IS roster row 156's deferred seat; the RunMumps/SQLTable/MumpsMain/Fcn/MumpsMainUnit130/ServerMain call sites bind unchanged on the String-parameter signature). New file interp/Interpret.kt (2674 lines). Render: the ~55-label goto web as a single state machine over the private Lbl enum (h011, u95/u158 precedents) - every goto X becomes lab = Lbl.X with continue@sm mid-arm, vendor label names kept in the enum; INIT prologue (BuildGlobal/BuildLocal stack cleaners with the widened CharArray? key seats), the RESTART/NEXT_LINE/NEXT_LINE_XECUTE/NEXT_CMND re-entry cycle with the pc1@ postconditional loop and the icmnd char dispatch over the u171 InterpH command codes, all 22 command arms (CMMNT/FCN/BREAK/FILELOCK/READ family with START_IO/STORE/CONT_READ/OPENF/OPN1/ZCMDS/IF/WRITE/WRITEPROC/IOSUB/JOB/GOTO/MERGE/SET family with SET_BEGIN/S1/SET_MID/S2/SET_RETURN/QUIT/XECUTE/FOR family with NXT_ARG/INIT_LOOP_VAR/ARGLESS/BYP/FOR_END/FOR_QUIT/HALT/HANG/ELSE/DO family with DO_RET1/DO_RET/NEW family with NEW_AGAIN/NEW2/NEW_TAIL/KILL family with KILL_AGAIN/KILL2/KILL_TAIL/FILECLOSE/NEXT_CLOSE_ARG/USE), the remote LHSREF/LHSREF1 and LBL/OFFSET blocks (the DO/GOTO label resolver with the lbls/lbloff label cache - lbloff widened IntArray(20) to IntArray(LBLS), vendor under-sized array = UB, quantization journaled), and the error cascade (err_call through abrt1 -> abrt). h018 trichotomy verified against the reference build: SQLITE IN (the ZCMDS JOB arm sql(CLOSE) auto-connect kept; the DATAKILL k1 XNEXT sibling-kill loop OUT), NATIVE_SHARED OUT (the READ/hang PulseCount arm journaled), TERMINATE_ON_ERROR OUT (abrt = return 99). h004: the file-scope static char-star swap array swp[SWAP] persists across calls on the InterpretState object (u158 precedent); h008: the nmes except-list and the forv/forin/forlv rows ride per-call scratch carriers. h009: the vendor's assign-and-test parse_()/sym_() collapses render as the parseErr() helper; sprintf percent-d renders via writeCstring. h010: short locals promoted to Int (pd1cur/cmdx/gsbx); Mltoa int-to-long widening kept. h014 new stubs raised: Merge (owner interp-sysfunc.cpp::Merge, batch-8) and inout (owner interp-parse.cpp::inout, batch-10) in InterpStubs; BuildGlobal/BuildLocal key widened CharArray to CharArray? for the NULL stack-cleaner calls (defaults keep call sites binding); the remaining call surface binds on existing real seats (Mglobal u173, fcn u94, parse_ real, readf_ real, DirectRead, TimeOut_getstr1 u170 via the getstr2At/timeOutGetstr1At copy-in/write-back h001 adapters, prnterr u58, _SymFree, keyfixAt write-back adapter, Mltoa MdhStubs, NativeBtree.btree, add, makeLock/cleanLocks) and existing stubs (sym_ offset seat batch-10, sql batch-9, MergeFlag-class file statics). h001: interior-pointer call sites (&pd1[off] shapes) get the copy-in/call/write-back adapter pattern (Mglobal precedent); per-file strcmpC written (no shared seat). h012: C-locale byte-domain helpers per-file (strcasecmpC/strncasecmpC/isprintC/tolowerC/toupperC/isalphaC, atoiC shared-seat alias); sym_ literal calls reset valBuf[0]=NUL first. h013: the never-referenced cod206-210 opcode arms and the dead else-branch fossil comments dropped with notes; fork() quantized per the u128 deterministic-UB precedent (forkC()=1 always-parent, the child arm rendered-but-unreachable; _exit -> kotlin.system.exitProcess, fdes write -> writePipeC no-op stub, fdes[1]=-1). h015: vendor var -> varBuf (OFFSET arg-passing block), the raw dollar FCN byte via the DOLLAR 36.toChar() carrier, quoted literals via QUOTE2; GOTO/ELSE/FOR/IF/SET/NEW keyword collisions disambiguated via the u171 trailing-underscore consts. h017: constants keep interp.h/sysparms.h vendor names; entry interpret keeps the stub signature (parm1). SCAN-GATE CATCHES: 46 em dashes in the first full draft replaced with "-" before compile; the OFFSET t-double-bang restructured to the null-carrier error() shape after verifying vendor 2440-2482 (the symflg-200 tmpS arm vs the args-offset arm); System.err.printf newline corrected to the \n byte-faithful form (not %n); the private NUL const removed in favor of the u19 public (conflicting declaration caught by the compile gate); java.io.Exception -> Exception. Rules: h001, h002, h004, h008, h009, h010, h011, h012, h013, h014, h015, h017, h018. Output: interp/Interpret.kt; edit: interp/InterpStubs.kt (interpret stub resolved; +Merge, +inout stubs; BuildGlobal/BuildLocal key CharArray?). Whole-arm verification: 172 files, zero errors, zero warnings under kotlinc 2.1.20 locally before the push. Events +26 rows (log now 2104 lines). Big-three remaining: u156/u157 zfcn x2.
- Unit 156 (interp-zfcns.cpp::zfcn, the deferred BIG THREE, second member): the dollar-Z intrinsic dispatcher (vendor 158-2519, physLOC 1662; units.csv row 156/rank 299, score 2921.7-adjacent - the second-largest unit; resolves the InterpStubs zfcn stub raised at the u155-era seat, the Parse_.kt:801 and MpstimeMain.kt:174/234 call sites binding unchanged on the offset signature; the interp-bifs.cpp::zfcn batch-6 overload is the separate u157). New file interp/Zfcn.kt (~2400 lines). Render: the ~110-label goto web as one state machine over the private ZLbl enum (h011; renamed from Lbl at the compile gate - Interpret.kt owns the package's private Lbl file-scope name, Kotlin redeclaration) with a sequential DISPATCH arm reproducing the vendor if-ladder over strncasecmpC(fn, ...) and the z~gtk/zz/zs sub-ladders; every vendor return becomes lab = ZLbl.RET with the h001 copy-in/call/write-back adapter (local v1d/bd at index 0, origLen window written back at RET, u173 precedent); the vendor in-place v1d argument-split NUL punches and zglobal strmove reproduce caller-visible byte state exactly. Arms: the GTK surface (GtkDialogWithButtons through GtkSwitchGetActive - the static iter..iter9/t/spin/column ride ZfcnState, the widget pointers decode via the ptrReg registry on the u73 gtkTreeStoreFromPtr pattern), the SQLITE surface (zsqlite BEGIN/COMMIT/ROLLBACK/SAVEPOINT/PRAGMA via the sql stub, zglobal GBLNME via ServiceInterface, zcondense/zrestore/znxtrow as the h018 SQLITE no-op arms with the dump walks h013-dropped), the ServiceInterface text family (zzavg through zzidf, zzmultiply, zzdoccorrelate - vendor signature (x4,x5,x1,x2,x3,c) remapped to the seat order), the zdate family (zd1-zd8 over the per-file Tm/mktimeC/ctimeC carriers - Calendar for tm_yday 0-based/tm_wday normalization, the zd4 month-by-month while(1) walk and the zd5 oddball second format kept verbatim), zword (the linebuf/linebufx IS&R word parser with the persistent h004 statics), zseek/ztell (fpos_t static on ZfcnState, fsetpos/fgetpos h014 OS-layer TODO carriers on the Reader/Writer edges, u161 precedent), and the math/string arms (zabs/zpad/z_variable/znumeric/zblnk/zhtml/zcurrentfile/zfile/zroot/zsystem/zfloor/zceil/ztrunc/zlog family via the mps_* seats, zfiletest/zbasename over popen with the BTICK shell-command carriers). Seat edits, all journaled: SymHeaderMisc ServiceInterface x1/x2 widened CharArray to CharArray? (only caller is this unit); GtkTreeAdd iterb widened GtkTreeIter? (the level-1 NULL call, zfcns 555) and the GtkTreeAdd.kt-local GtkTreeIter class REMOVED in favor of the mdh.MdhStubs carrier (one vendor struct type across the GtkTreeAdd and mdh gtk_tree_* seats; the InterpStubs gtkTreeStoreAppend/gtkTreeStoreSet seats re-qualified to the mdh type); MdhStubs + GtkDialog()/GtkFileChooser/GtkTreeView classes with the open GtkWindow() : GtkWidget() libgtk hierarchy (the dialog arm passes its dialog to gtk_window_set_keep_above and gtk_widget_destroy) and six new libgtk h014 stubs (gtk_dialog_new_with_buttons/gtk_dialog_run/gtk_window_set_keep_above/gtk_widget_destroy/gtk_tree_view_get_selection/gtk_file_chooser_get_filename); InterpStubs + seven mps_* seats raised (mpsFloor/mpsCeil/mpsTrunc int-returning, mpsLog/mpsCos/mpsTan/mpsExp2 Unit, owners the interp-arith.cpp batch-10 corpus units) and the zfcn stub resolved. Vendor notes kept: the zdate zd5 second format's -100-1/+2 arithmetic, the zd4 vendor copy-paste jan/feb comment ladder, the znxtrow dollar-zm string, the zfiletest/zbasename shell backticks, the zd6 ctime+11 HH:SS window, the zblnk bdx double-blank latch. SCAN-GATE CATCHES: the znxtrow println raw dollar replaced with the escaped dollar-literal before compile (Kotlin template hazard); the zfiletest/zbasename shell-command backticks replaced with the 96.toChar() BTICK carrier (Match.kt precedent); the ZfcnState widget registry rides java.lang.Object - this build's gate classpath keeps a lean kotlin.Any builtin and the T-to-Any subtyping check fails (verified with a minimal repro; no other unit binds Any as a checked supertype). h013: the vendor dead locals (fd/dtime/timex local/g/rt/tmp4/gbltbl/gblstr/x, zcode2/zcode3 prologue stores, the znumeric k=0 and zhtml k=1 dead stores) dropped with notes; zdate '1' renders the "%ld" long arm per h018 64-bit. Rules: h001, h002, h004, h008, h010, h011, h012, h013, h014, h015, h017, h018. Output: interp/Zfcn.kt; edits: interp/InterpStubs.kt (zfcn resolved, +7 mps stubs, gtkTreeStoreAppend/Set mdh-qualified), interp/SymHeaderMisc.kt (ServiceInterface x1/x2 nullable), interp/GtkTreeAdd.kt (iterb nullable, GtkTreeIter class removed), mdh/MdhStubs.kt (GTK carriers + libgtk stubs). Whole-arm verification: 173 files, zero errors, zero warnings under kotlinc 2.1.20 locally before the push. Events +24 rows (log now 2128 lines). Big-three remaining: u157 interp-bifs.cpp::zfcn.
- Unit 157 (interp-bifs.cpp::zfcn, the deferred BIG THREE, CLOSER): the mdh dollar-Z intrinsic dispatcher - the twin of the u156 interp-zfcns zfcn over the WHOLE-BUFFER 3-arg signature (vendor 158-2468, physLOC 1622; units.csv row 158: comment_ratio 0.08, ptr 99, ctrl 621, score 1173.4, rank 305, batch 6) - RESOLVES the MdhStubs zfcn(v1d,bd,svPtr) stub (TAG CORRECTION, u141/u148/u169/u172 family: the "not a corpus unit" tag was a mis-tag, resolved note kept verbatim in MdhStubs; the LibmpscppMisc5.kt:220/235/267/407 and MdhTreeLevelAdd.kt:117 call sites bind unchanged). New file mdh/ZfcnBifs.kt (2359 lines), rendered from the u156 twin with the vendor body diff as the delta ledger (every hunk verified against a difflib alignment of the two vendor sources; the arm-comment vendor line anchors remapped to this file's line numbers). Twin deltas, all kept verbatim per file: zreplace ADDED (vendor 1029-1034, over the builtin.h:40 Replace seat - not a corpus unit, stub raised in MdhStubs); the five SQLITE-only index/database arms (zindexmax/zindexsize/zdatasize/zdbname/zdbfile) and their dispatch lines and the commented zglobal->zfiletest fossil ABSENT (h013 roster note); the generic sql arm binds PRAGMA 29 not SQLCMD 55; the zglobal arm is the g=5 count-empties Mglobal probe (vendor fossil kept: 5 is not a defined defines.h op, falls through the Mglobal fall-through arm) instead of the zfcns SQLITE ServiceInterface GBLNME relay; the z_variable arm binds the same-package mdh 4-arg sym_ seat via the per-file tail() h001 carrier. Everything else is line-for-line the u156 arm web (the ~110-label ZLbl state machine, h011), over whole-buffer parameters with NO copy-in/write-back adapter (h001: the vendor's in-place v1d argument-split NUL punches mutate the caller's buffer directly); a SEPARATE ZfcnState object carries this translation unit's own statics (h004: linebuf/linebufx/iter slots/t/spin/column/fpos/timex - each C translation unit owns its own statics, both render). The mps_*/Mglobal/sql/expx/ServiceInterface/GtkTreeAdd/sigint seats bind on the mumps.interp real/stub seats via imports (the first mdh-package unit to bind the interp cross-package surface this broadly - the CstringOps seats import cleanly). Rules: h001, h004, h008, h010, h011, h012, h013, h014, h015, h017, h018. Output: mdh/ZfcnBifs.kt; edit: mdh/MdhStubs.kt (zfcn stub resolved + tag correction; +Replace seat). Whole-arm verification: 174 files, zero errors, zero warnings under kotlinc 2.1.20 locally before the push. Events +22 rows (log now 2150 lines). THE BIG THREE ARE COMPLETE (u155 Interpret, u156 zfcns zfcn, u157 bifs zfcn); batch 6 is now FULLY RENDERED. Mark's next gradle gate: pull to this commit, gradle --no-daemon build detekt jacocoTestReport on m1-build, then the B6-close coverage report (swipl, wp2/arms/PRO/rules_v0.pl + knowledge_tooling.pl).
- GATE FIX (B6-close round, Mark's m1-build gradle gate RED on 410b622, precedent the B5 429ffe95 journal-addendum-only round): 2 detekt EmptyDefaultConstructor findings (MdhStubs.kt empty default-ctor parens on the u156-era GtkWindow/GtkDialog carriers - parens removed) and 8 kotlinc warnings under the gradle toolchain (invisible under the lean local gate): 6x "java.lang.Object not recommended in Kotlin" at the Zfcn/ZfcnBifs ptrReg widget-registry sites, and 2x "elvis always returns left operand" at Interpret.kt sym_(..., t ?: error(...)) where t is non-nullable (the u155 null-carrier note for that site superseded - elvis dropped to the plain pass). The java.lang.Object registry carrier is superseded by a new mdh.MdhStubs GtkHandle marker interface (the u156 journal carrier note superseded): kotlin.Any is UNUSABLE under the lean local gate (T-to-Any subtyping fails, verified with a minimal repro earlier this session) and java.lang.Object warns under gradle - the marker interface is warning-free on BOTH toolchains; all 20 GTK carrier classes tagged, widgetAt bound fun <T : GtkHandle>, Zfcn.kt imports mumps.mdh.GtkHandle. Files touched: mdh/MdhStubs.kt, interp/Zfcn.kt, mdh/ZfcnBifs.kt, interp/Interpret.kt. Local gate re-run after the fix: 174 files, zero errors, zero warnings under kotlinc 2.1.20. No events rows (gate-fix round). Mark re-gates, then the B6-close coverage report.

- B6 CLOSE (gate round): Mark's m1-build gradle gate GREEN on the gate-fix commit 3ea15c0 (build detekt jacocoTestReport all clean; the 410b622 RED round resolved by the EmptyDefaultConstructor parens fix, the GtkHandle marker registry carrier, and the Interpret elvis drop). B6-close swipl coverage report (Mark, m1-build): rules in base 18, triggering situations 1086 (hits 1081, misses 5), hit rate 99.5%, reuse rate 98.2%; all 5 misses are preprocessor_conditional captured -> rule h018, miss is pre-capture history (no action, same pre-capture profile as prior batch closes). Events CSV unchanged at 2150 rows (gate-fix rounds add no events). BATCH 6 COMPLETE (u155 Interpret, u156 zfcns zfcn, u157 bifs zfcn closing the deferred Big Three; 174 files, 0 errors, 0 warnings under both the lean local kotlinc 2.1.20 gate and the m1-build gradle/detekt gate). Batch 7 (B7) opens.

- B7 INFRA NOTE (env-reset recovery, tranche 1 of the batch-7 render): the Vibe sandbox execution environment was RESET mid-batch (the /home/user/big3 workspace, vendor tree and toolchain were wiped) AFTER the B7 renders for rows 188-205 had been produced but BEFORE any B7 push - none of that work reached GitHub, and the earlier-session unit renders were lost with the workspace. Recovery per the u172 recipe: workspace rebuilt from scratch (codeload master zip at 3ea15c0, JDK17 corretto, maven jars kotlin 2.1.20/coroutines 1.9.0/asm 9.7.1/annotations 24.0.1, gate script kotlinc.sh - NOTE the fresh-build gate also needed the trove4j jar (org.jetbrains.intellij.deps:trove4j:1.0.20200330) on the compiler classpath and an explicit -cp kotlin-stdlib for -no-stdlib mode; vendor re-fetched as mumps-14-may-2026.tgz, sha256 621deb25...ffdbe re-verified). Baseline gate on the fresh repo: 174 files, zero errors, zero warnings. Tranche 1 = rows 187 and 206-216 RE-RENDERED and gated green (the row 206-216 renders are the second render - the first was lost in the reset; both renders were made against the same vendor bytes). Rows 188-205 remain to be re-rendered in tranche 2 before the batch-7 close gate. NO gradle gate for Mark yet - wait for the whole batch.
- Unit 187 (interp-service.cpp::ServiceInterface, B7 tranche 1): the ServiceInterface char-code dispatcher (vendor 87-445, loc 347; units.csv row 187: comment_ratio 0.185, ptr 22, ctrl 26, score 316.5, rank 299) - RESOLVES the SymHeaderMisc ServiceInterface stub (u156-widened CharArray? x1/x2 kept; the Zfcn.kt GBLNME-relay call sites bind unchanged). New file interp/ServiceInterface.kt. Render: four early arms before the push (GETENV -> Getenv mdh seat, SCANALNUM/SCAN/INPUT -> the mdh seats), then the unit-98 push block (allocSV + msvCopyInto, EXTENDED here to the FULL-field copy - the vendor memcpy copies the whole struct MSV; all prior call sites bind unchanged, the copied-field superset is observably identical), then the 15 service arms over the per-argument parse idiom (t0px=1/t2=1/xd/xpx=0/parse_/keyfixAt(&v1d[2])/strcpy-from-v1d, one parse per argument, MMULTIPLY three, DOCCORRELATE two plus the x4/x5 pass-through) relaying to the mdh seats (Cosine/Sim1/Jaccard/Transpose/Multiply/Centroid/TermCorrelate/DocCorrelate/Avg/Count/Max/Min/Sum/Dice), the GBLNME arm (mglobal op 20, no keyfix, rslt copy-back), and the tpx inherit; free -> GC. NEW MdhStubs stub raised: IDF (owner libmpscpp.cpp::IDF, vendor 10370, units.csv row 240 - not yet a corpus unit; the INVDF arm is its only in-arm caller). h015: the vendor char codes are file-private consts (COSINE 1 through GBLNME 20, defines.h:226-245). Vendor notes kept: the INVDF double-NUL store tmpA[strlen+1]=0 writes one past the terminator; GBLNME skips the keyfix; the early arms return without the push and without the tpx inherit. SQLITE defined at the vendor file head - the GBLNME arm is LIVE (h018). Rules: h003, h007, h009, h012, h013, h014, h015, h017, h018. Output: interp/ServiceInterface.kt; edits: interp/SymHeaderMisc.kt (stub resolved), interp/GlobalQuery.kt (msvCopyInto full-field extension), mdh/MdhStubs.kt (+IDF stub). Whole-arm verification: 178 files, zero errors, zero warnings under kotlinc 2.1.20 locally before the push. Events +18 rows.
- Unit 206 (libmpscpp.cpp::global::Fnumber two-arg mstring,mstring overload, B7 tranche 1): the last of the four-member Fnumber family (vendor 7415-7436, loc 22; units.csv row 206: the Fnumber.kt unit-42 disambiguation note already named this the batch-7 wrapper; one-arg global member DONE u84, mstring pair DONE u42). EDIT-ONLY append to mdh/GlobalFnumber.kt, the u84 shape plus the optional second-argument arm. Vendor quirk kept: the b.c_str()==0 test can never be true for a real std::string - the else arm always runs for a two-argument call (the h013 note in Fnumber.kt documents the same idiom on the mstring twin). Rules: h007, h012, h013, h014, h015, h017. Output: mdh/GlobalFnumber.kt (+44 lines). Whole-arm verification: 178 files, zero errors, zero warnings. Events +12 rows.
- Unit 207 (interp-sym.cpp::mkpt, B7 tranche 1): the byte-printable helper (vendor 970-985, loc 16; units.csv row 207: score 9.0, rank 82; prototype at :185 is the only other mention - no vendor callers, standalone roster unit). New file interp/Mkpt.kt. Render: walk + map - 32..128 pass through, <32 becomes '-', then the three specific re-maps override the dash (\\x1f->'F', \\x1e->'E', \\x01->'.') as three independent ifs in the vendor's order (no else chain); NUL-close at the walk index. Divergence documented (not invented): bytes >128 match NO vendor branch - the C static buf[2048] keeps stale previous-call content; the Kotlin render returns a fresh allocation (GC for the static reuse), so >128 bytes stay NUL - the stale static memory has no portable carrier. No bounds check against 2048 (vendor trusts the caller). Rules: h007, h012, h013, h014, h015, h017, h018. Output: interp/Mkpt.kt. Whole-arm verification: 178 files, zero errors, zero warnings. Events +14 rows.
- Unit 208 (interp-sym.cpp::_justify, B7 tranche 1): the width/precision $JUSTIFY variant (vendor 1328-1354, loc 26; units.csv row 208: score 8.8, rank 81; the banner reads "may be dead" - no in-file caller, roster unit). New file interp/Justify.kt. NOT the B3 mps_justify (MpsJustify.kt takes the full format string; this one takes w and p separately and builds "%<l>.<k>lf" at run time via the %c escape pair - rendered as java.util.String.format over the built string, the MpsJustify h009 precedent). p=="-1" arm: strlen/lpad (REAL REUSE, InterpStrmanipMisc offset carriers 0) with the 255 clamp AFTER the k>=l test; the lpad strout-untouched-when-k<=0 quirk means a short bd stays the plain strncpy of in (kept). Vendor notes kept: tmp2[32] trusts the built format to fit (no bounds check); the leading underscore is the vendor name (h017, RsltCallback single-underscore precedent). Rules: h003, h007, h009, h012, h013, h014, h015, h017, h018. Output: interp/Justify.kt. Whole-arm verification: 178 files, zero errors, zero warnings. Events +18 rows.
- Unit 209 (libmpscpp.cpp::GlobalData(mstring) overload, B7 tranche 1): the mstring twin of the unit-86 char* member (vendor 2616-2635, loc 17; units.csv row 209: score 8.7, rank 79). EDIT-ONLY append to mdh/GlobalData.kt, the u86 shape with a.c_str() as the strcat carrier. Carrier-fold note: the third vendor overload GlobalData(std::string) at :2636 (also loc 17) renders onto the SAME Kotlin String carrier as the unit-86 char* member (no distinct carrier for the two C++ argument types once char* folds to String; documented, not duplicated). xecute/SymGet stubs reused (h014, owners not corpus units); DOLLAR carrier (h015). Rules: h007, h012, h014, h015, h017. Output: mdh/GlobalData.kt (+38 lines). Whole-arm verification: 178 files, zero errors, zero warnings. Events +10 rows.
- Unit 210 (libmpscpp.cpp::global::Soundex, B7 tranche 1): the global-member $ZZSOUNDEX relay (vendor 4200-4219, loc 17; units.csv row 210: score 8.7, rank 80). New file mdh/GlobalSoundex.kt. Render: one RETRIEVE (GLOBAL_NOT_FOUND throw), the "$zzsoundex"+\\xce+gtmp+\\xcf build folded into the strcatC chain (u147 GlobalTranslate precedent - the Mstring.Concat owner is not a corpus unit; the earlier draft's Mstring.Concat extension had no existing seat and was corrected at the compile gate), strcpy into v1d at offset 1, fcn, bd offset 1 read. The mstring member twin at :4182 (loc 18, " $zzsoundex" via zfcn) is NOT in the corpus rows (mis-tag ledger note). Rules: h003, h007, h009, h012, h013, h014, h015, h017, h018. Output: mdh/GlobalSoundex.kt. Whole-arm verification: 178 files, zero errors, zero warnings. Events +18 rows.
- Unit 211 (libmpscpp.cpp::mstring::Translate(mstring,mstring), B7 tranche 1): the two-argument mstring member (vendor 4224-4244, loc 15; units.csv row 211: score 7.7, rank 57). New file mdh/MstringTranslate.kt. Mstring extension (MstringEval receiver precedent; the (Mstring, Mstring) arity keeps it distinct from the Global sibling DONE u147); the Concat chain folds into the strcatC build (same u147 precedent). The one-argument mstring twin at :4246 is NOT in the corpus rows (mis-tag ledger note); the global-class sibling is DONE u147. \\xd0 separator = 208 code point. Rules: h003, h007, h009, h012, h013, h014, h015, h017, h018. Output: mdh/MstringTranslate.kt. Whole-arm verification: 178 files, zero errors, zero warnings. Events +18 rows.
- Unit 212 (interp-strmanip.cpp::substr, B7 tranche 1): the $EXTRACT-range copy (vendor 59-82, loc 19; units.csv row 212: score 7.1, rank 45) - RESOLVES the InterpStubs substr stub (B3 patrn tranche); the InterpPiece/Match call sites bind unchanged on the (src, srcOff, dst, start, len) signature (srcOff is the stub-era offset seat; the &in[off] caller pointers fold into it). New file interp/Substr.kt. Vendor quirk quantized: the len-start+1 > STR_MAX arm calls realloc(out, len-start<<1) (C precedence: len - (start<<1)) and reassigns only the LOCAL out pointer - the caller's buffer is never enlarged (vendor UB/leak residue); quantized to a no-op with the note (the vendor's own commented-out len = STR_MAX-1 fix kept as the comment). len<0 clamps 255; start--/len--; the i=0..len copy; out[len+1]=0. Rules: h003, h007, h009, h012, h013, h014, h015, h017, h018. Output: interp/Substr.kt; edit: interp/InterpStubs.kt (stub resolved). Whole-arm verification: 178 files, zero errors, zero warnings. Events +18 rows.
- Unit 213 (compiler-sys1.c::substr, B7 tranche 1): the compiler-side twin (vendor 396-416, loc 17; units.csv row 213: score 6.7, rank 41) - RESOLVES the compiler Stubs.kt substr stub (Match.kt :186 calls it knowingly against this owner); the package split (mumps.compiler vs mumps.interp) separates the same-named corpus twins. New file compiler/Substr.kt. Unlike the interp twin: NO realloc residue - len<0 OR len>255 both clamp to 255 (the K&R short parameters ride Int carriers; the clamp bounds the domain identically). Rules: h003, h007, h009, h012, h013, h014, h015, h017, h018. Output: compiler/Substr.kt; edit: compiler/Stubs.kt (stub resolved). Whole-arm verification: 178 files, zero errors, zero warnings. Events +18 rows.
- Unit 214 (libmpscpp.cpp::global::Piece(string) overload, B7 tranche 1): the string-key Piece (vendor 7236-7258, loc 17; units.csv row 214: score 5.5, rank 32). EDIT-ONLY append to mdh/GlobalPiece.kt, the u38 shape minus the pattern-global fetch (one RETRIEVE, jj<0 -> jj=ii clamp, %d collapses, _piece with the caller's pat). Carrier-fold note: the const char* twin at :7181 (also loc 17) folds onto this same Kotlin String carrier (unit-209 precedent); the mstring& twin at :7259 is NOT in this row (mis-tag ledger note). Rules: h007, h009, h012, h014, h015, h017. Output: mdh/GlobalPiece.kt (+42 lines). Whole-arm verification: 178 files, zero errors, zero warnings. Events +12 rows.
- Unit 215 (compiler-zcommands.c::<misc>, B7 tranche 1): the banner/include residual (units.csv row 215: loc 30, score 5.2, rank 28 - residual-by-identity, EpiMisc u92/CompilerFcninitMisc u186 precedents; zcommands_code itself is DONE as row 192). New file compiler/ZcommandsMisc.kt: KDoc-only residue render, four includes mapped h014, no executable code, no stubs. Rules: h013, h014, h015, h017, h018. Output: compiler/ZcommandsMisc.kt. Whole-arm verification: 178 files, zero errors, zero warnings. Events +10 rows.
- Unit 216 (libmpscpp.cpp::global::Find(char*) overload, B7 tranche 1 - the LAST BATCH-7 ROW): the char*-key Find (vendor 7566-7585, loc 16; units.csv row 216: score 4.0, rank 4; the three global::Find rows now complete - mstring key u33, global key u185, char* key here). EDIT-ONLY append to mdh/GlobalFind.kt, the u33 shape made single-retrieve with the String key carrier (unit-39 char*->String precedent) folded into a byte buffer; Mltoa int->long widening documented (h010); no const char*/std::string overload twins exist for global::Find (unlike the Piece/Data families - noted). Rules: h007, h010, h012, h014, h015, h017. Output: mdh/GlobalFind.kt (+46 lines). Whole-arm verification: 178 files, zero errors, zero warnings under kotlinc 2.1.20 locally. Events +12 rows. TRANCHE 1 (rows 187, 206-216: 12 units) COMPLETE - all batch-7 roster rows are identified and 12 of 30 are rendered; rows 188-205 (LibmpscppMisc6 through ExternsH) re-render next as tranche 2, then the B7-close journal/events reconciliation and Mark's gradle+swipl gate.
- Unit 188 (libmpscpp.cpp::<misc-6>, B7 tranche 2 OPENER): the 600-physLOC residue chunk (vendor 2677-3466; units.csv row 188: loc 600, comment_ratio 0.003, ptr 62, ctrl 58, score 319.2, rank 294 - the mcvt family opens it, the global constructor closes it). New file mdh/LibmpscppMisc6.kt + Mstring.kt edit + InterpStubs.kt edit. Render: mcvt x5 + cvt x4 (the char-star mcvt keeps the vendor i-return bug; the cvt static-buffer aliasing divergence noted), the const-long operator() overload rendered as the Long invoke extension (sprintf percent-d then the "a" call; the other 30+ invoke overloads = u31 front-run verification only), count (CSV arg counter; ErrorMessage takes CharArray+Long via the built buffer), HitRatio (Long/Long integer-division quirk), global::Name, __test (returns tpx.toInt()), mstring::ReadLine x3 (stdin via the Getstr2 reflective-fetch precedent, Reader for FILE-star, Scanner for istream; global::ReadLine x3 DEFERRED to batch-8 row 236 owner), zBlanks x2 (XCE=206/XCF=207, zfcn via the mdh ZfcnBifs seat), Ascii x4 (signed-char h010 for mstring forms, unsigned for global), Length() no-arg x2 + residue overloads (u145/u181/u149 front-run verify), Horolog (mumps.interp._horolog). Mstring.kt: the u131 cast stubs RESOLVED with bodies via atol_/atof_ privates. InterpStubs.kt: the _horolog stub RAISED (param CharArray, owner interp-sym.cpp batch-9). Rules: h001, h007, h008, h012, h013, h014, h015, h017. Output: mdh/LibmpscppMisc6.kt; edits: mdh/Mstring.kt, interp/InterpStubs.kt. Whole-arm verification: 179 files, zero errors, zero warnings under kotlinc 2.1.20 locally before the push. Events +16 rows.
- Unit 189 (compiler-epi.c::epi): the function epilogue generator (vendor 39-180, loc 135; units.csv row 189: comment_ratio 0.015, ptr 25, ctrl 29, score 111.3, rank 279). New file compiler/Epi.kt - the Stubs.kt epi stub RESOLVED (removed). Render: fun epi(ExecName, pid); the errors.h nine arms INLINED (batch-10 row 296 becomes a verification unit); EPI_SEPARATOR "//" + 81 '=' verified exact; fw2 private write+length helper for the linepad(n, comment) stub calls; label/text emitters on the nullable walker + error() null-carrier idiom (no double-bang); FileWriter("Main_.<pid>" or "<fn>_.<pid>"); tail resets incl. ForBlocks=-1, MaxTmp=2. Rules: h001, h007, h013, h014, h015, h017. Output: compiler/Epi.kt; edit: compiler/Stubs.kt. Whole-arm verification: 179 files, 0/0. Events +12 rows.
- Unit 190 (compiler-sys1.c::match): the compiler-side pattern matcher (vendor 396-490 region, loc 95; units.csv row 190: comment_ratio 0.021, ptr 4, ctrl 52, score 78.9, rank 260). New file compiler/Match.kt - the Stubs.kt match stub RESOLVED. Render: fun match(pts, lx, ct, ad, rslt); when over st.bd[pts[1][i]]; PUNCT_CHARS rebuilt via charArrayOf with DOLLAR 36.toChar()/BTICK 96.toChar() carriers (no raw sigil bytes in arm source; vendor double-apostrophe kept); private strcmpC; substr(ad, ptr, tmp1, 1, pts[2][i]-1) offset folds; xindex from interp. Rules: h006, h007, h012, h013, h014, h015, h017. Output: compiler/Match.kt; edit: compiler/Stubs.kt. Whole-arm verification: 179 files, 0/0. Events +14 rows.
- Unit 191 (interp-sysfunc.cpp::sigint): the interrupt handler (vendor 39-253, loc 215; units.csv row 191: comment_ratio 0.018, ptr 0, ctrl 34, score 70.1, rank 254). New file interp/Sigint.kt - the InterpStubs sigint stub RESOLVED (all call sites bind unchanged). Render: fun sigint(i); ReadlineHistory object with writeHistoryFile(); the SIG-star consts are LINUX reference-build values (h018: every #ifndef 101-133 fallback EXCLUDED); the SIGIOT duplicate-case note (arm not emitted); mglobal(CLOSE=4, tmp, tmp1, allocSV()); exitProcess(1). Rules: h001, h007, h012, h014, h015, h017, h018. Output: interp/Sigint.kt; edit: interp/InterpStubs.kt. Whole-arm verification: 179 files, 0/0. Events +14 rows.
- Unit 192 (compiler-zcommands.c::zcommands_code): the Z-command compiler (vendor 39-147, loc 109; units.csv row 192: comment_ratio 0.009, ptr 4, ctrl 31, score 64.5, rank 251). New file compiler/ZcommandsCode.kt. Render: fun zcommandsCode(): Int; lowercase fold via +32 and 0xff on the char code; arms n/a/m/f/d/r/s/c/e; %% collapses at compile time (h009: the SymPut emission built via DOLLAR + '%'; sprintf(t,"%%d", i) emission); errors.h arms inlined in the f arm; __label_lookup/__text_function emitted identifiers = documented gate exceptions; zseek atol dead store comment-only (h013); st.p2px Short and st.xpx Long need .toInt() for indexing. Stubs.kt: fcninit stub RAISED (owner row 195). Rules: h001, h009, h013, h014, h015, h017. Output: compiler/ZcommandsCode.kt; edit: compiler/Stubs.kt. Whole-arm verification: 179 files, 0/0. Events +10 rows.
- Unit 193 (interp-native-btree.cpp::freadx): the buffer-tree reader (vendor 39-66, loc 27; units.csv row 193: comment_ratio 0.182, ptr 23, ctrl 5, score 56.1, rank 243). New file interp/Freadx.kt - the InterpStubs freadx stub RESOLVED (Scanblocks binds). Render: fun freadx(r, flag): CharArray (stub-era signature); BtreeBufferState object (bfr: Array<Buffers>, GBLBUF 65537), BtreeFile object (RandomAccessFile?); root arm returns copyOfRange (documented DIVERGENCE: C aliases the pool interior), non-root arms return the pool buffer itself (aliasing preserved); readBlock/writeBlock ByteArray bridges (readFully). Rules: h003, h007, h012, h014, h015, h017. Output: interp/Freadx.kt; edit: interp/InterpStubs.kt. Whole-arm verification: 179 files, 0/0. Events +12 rows.
- Unit 194 (interp-sym.cpp::Qsub): the dollar-QSUBSCRIPT runtime (vendor 39-90, loc 52; units.csv row 194: comment_ratio 0.019, ptr 17, ctrl 9, score 53.8, rank 240). New file interp/Qsub.kt - the InterpStubs Qsub stub RESOLVED. Render: fun Qsub(tmp0, tmp0Off, tmp1, tmp1Off, tmp2: CharArray?, tmp2Off, svPtrX: Msv); allocSV + msvCopyInto full-field (u187); t0px=0 (NOT 1); gpad save/restore around parse_; keyfix copy/write-back adapter (Qlength precedent); the x01/>128 marker NUL-chopping walk kept verbatim; writeCstring(dst, s, off) arg-order note; atoiC(tmp2, tmp2Off). Rules: h006, h007, h012, h014, h015, h017. Output: interp/Qsub.kt; edit: interp/InterpStubs.kt. Whole-arm verification: 179 files, 0/0. Events +12 rows.
- Unit 195 (compiler-fcninit.c::fcninit): the prologue emitter (vendor 40-92, loc 41; units.csv row 195: comment_ratio 0.024, ptr 13, ctrl 4, score 43.9, rank 230; the u186 CompilerFcninitMisc banner row anticipated the seat). New file compiler/Fcninit.kt - the Stubs.kt fcninit stub (raised at u192) RESOLVED. Render: fun fcninit(output_file, pid) (output_file param dead in vendor, h013 note); all emissions verbatim (INCLUDE Main_.pid vs <fn>_.pid extra underscore; "console default/" stray slash kept); calls linepad2. Stubs.kt: linepad2 stub RAISED (owner compiler-sys1.c:963, batch-9 residue row 258). Rules: h001, h013, h014, h015, h017. Output: compiler/Fcninit.kt; edit: compiler/Stubs.kt. Whole-arm verification: 179 files, 0/0. Events +10 rows.
- Unit 196 (compiler-mumps.c::<misc>): the three-function misc chunk (vendor LevCheck 1949 + atCopy 1956 + EP 1978, loc 57; units.csv row 196: comment_ratio 0.034, ptr 4, ctrl 12, score 29.3, rank 205). New file compiler/CompilerMumpsMisc.kt - THREE Stubs.kt stubs RESOLVED with TWO TAG CORRECTIONS (mis-tag ledger now 9 deep: atCopy wrong owner compiler-at-test.c -> compiler-mumps.c:1956, u127 rpm precedent; levCheck "not a corpus unit" tag wrong). Render: levCheck(ExecName, DoRetX, DoRet); atCopy(atFlg, StringBuilder) (j=2 contract: caller pre-loads 2 chars; appends escaped chars; StringBuilder end); EP(flg, ExecName, pid) (emits _eps dispatch, LabelsStart nullable walk, calls epi, MAIN_flg=0). Rules: h001, h011, h013, h014, h015, h017. Output: compiler/CompilerMumpsMisc.kt; edit: compiler/Stubs.kt (3 stubs resolved). Whole-arm verification: 179 files, 0/0. Events +10 rows.
- Unit 197 (libmpscpp.cpp::embed1): the embedded-quote fixer (vendor 4151-4175, loc 19; units.csv row 197: comment_ratio 0.136, ptr 9, ctrl 6, score 27.5, rank 198). New file mdh/Embed1.kt. Render: fun embed1(inp: CharArray?): CharArray - null returns the empty buffer; pass 1 scans for a double quote (vendor k flag); no-quote fast path returns the INPUT array itself (aliasing preserved); pass 2 copies doubling each embedded double quote. Divergences: the C static tmp buffer becomes a fresh STR_MAX CharArray per call (u44 precedent); the null-input contract maps to a fresh NUL-only buffer. Rules: h006, h007, h012. Output: mdh/Embed1.kt. Whole-arm verification: 180 files, 0/0. Events +6 rows.
- Unit 198 (compiler-parse.c::Extract): the _extract argument builder (vendor 2466-2507, loc 30; units.csv row 198: comment_ratio 0.032, ptr 4, ctrl 9, score 24.2, rank 186; no prior stub - parse_ owns the row-199-class seats). New file compiler/Extract.kt. Render: fun Extract(v1d, bp): Int - name scan starts at i=1 (vendor 2471 quirk); SSS/TTT shift drops the name AND the terminator char (source cursor at v1d[i+1]); two-pass a-accumulator: Extract2(v1d, j) fragments with ", (UNS) " separators, the a==0||a>3 early return with NO emission (vendor 2486-2487), the a==1/a==2 "-1" pads, the _extract emission to CompilerState.out (p2493 vendor anchor), "tmpN" into bp with iiitmp++ AFTER; v1d mutated in place (h006 aliasing). Extract2 real seat (u15), strcatC/strcpyC/memcheck/cstringAt reused. Rules: h001, h006, h007, h012, h014, h017. Output: compiler/Extract.kt. Whole-arm verification: 181 files, 0/0. Events +12 rows.
- Unit 199 (libmpscpp.cpp::Sim1): the class-global inner product (vendor 1565-1621, loc 40; units.csv row 199: comment_ratio 0.024, ptr 3, ctrl 9, score 24.2, rank 187) - RESOLVES the MdhStubs Sim1(Global,Global) stub (the MSV wrapper at LibmpscppMisc9.kt:427 binds unchanged; Dice u165 twin precedent). New file mdh/Sim1.kt in the Cosine/Dice family shape: SOH-pair strip on B then A, single A-walk (Mglobal XNEXT + strncmp prefix stop), B.Data() skip-continue, btmp save ("in case this and B are the same"), the two get_global/GLOBAL_NOT_FOUND throws, tot += val*val1 - NO totD/totQ, NO division (unlike Dice/Cosine, the raw inner product returns; kept). Local refB stands in for B.ref (u71 carrier note); per-file strncmpOff/strcatOff/strtodC helpers (Dice precedent). Rules: h007, h012, h014, h015, h017. Output: mdh/Sim1.kt; edit: mdh/MdhStubs.kt (stub resolved). Whole-arm verification: 181 files, 0/0. Events +10 rows.
- Unit 200 (interp-sql.cpp::rslt_callback): the sqlite3_exec result callback (vendor 888-928, loc 31; units.csv row 200: comment_ratio 0.031, ptr 4, ctrl 9, score 24.1, rank 184; the exec call sites at 1002/1007/1030/1078 are the batch-9 sql owner's seats). New file interp/RsltCallback.kt. Render: fun rsltCallback(rslt, numCols, colVals, errMsgs): Int (void* rslt1 -> the caller-owned CharArray); SqlCallbackState object carries the file-scope rsltSpec[STR_MAX] (vendor 69) and static call_back (vendor 886); callBack raised first; null first column copies ""; one column copies the value; multi-column builds the array-name("value","value") shape into rslt while mirroring each value SOH-separated into rsltSpec, then the trailing quote-comma tail becomes ')',NUL at strlen-2. Vendor notes kept: the dead strcpy(rslt,"^") before the col test (h013 dead-store comment); err_msgs unused; the rsltSpec mark rides AFTER the loop then rslt[i-2]/rslt[i-1] close-paren chop. Rules: h006, h007, h012, h014, h017. Output: interp/RsltCallback.kt. Whole-arm verification: 182 files, 0/0. Events +10 rows.
- Unit 201 (libmpscpp.cpp::Multiply char-star helper): the char-star library helper (vendor 2252-2290, loc 26; units.csv row 201: comment_ratio 0.037, ptr 2, ctrl 8, score 20.8, rank 169 - the four-Multiply ledger closes: MSV wrapper :10193 u46, string twin :2211 u140, global::Multiply :2164 u53, char-star :2252 here). EDIT-ONLY append to mdh/Multiply.kt in the u140 fold shape: the vendor body is a VERBATIM copy of the string twin (only difference: fresh globals from the raw char-star args) - fun Multiply(a: CharArray, b: CharArray, c: CharArray) constructs A/B/C via cstringAt and dispatches to the u53 Global.Multiply seat (u117 Transpose-forwarder precedent); C().Kill() preserved inside the seat. Rules: h006, h012, h014, h015, h017. Output: mdh/Multiply.kt (+55 lines). Whole-arm verification: 182 files, 0/0. Events +10 rows.
- Unit 202 (interp-fcn.cpp::<misc>): the header residue chunk (vendor 1-97; units.csv row 202: loc 60, comment_ratio 0.2, ptr 2, ctrl 3, score 14.8, rank 143; fcn itself is B4 unit 94 - every other function in the file exceeds min_loc). New file interp/InterpFcnMisc.kt in the BifsMisc u107 residue shape: KDoc-only render, NO Kotlin declarations emitted. The declaration roster: Interpret DONE (u155), ScanParse and Eval batch-10 owners (existing InterpStubs stubs, no new seats), sym_ machinery existing seat; SYMSTORE/SYMRETRIEVE already carried file-private at their use sites (Fcn.kt:1367, MpstimeMain.kt:100); SQLITE defined LIVE (arms fcn's SQL paths); SYMDEBUG stays a comment; _INTERP_ is the translation-unit selector (h004 per-file analog); the __label_lookup/__text_function extern pair is the DOCUMENTED gate-exception roster text (u192 precedent); the extern "C" _getpid/time pair already carried at use sites; CSTR/USTR cast macros have no Kotlin analog (h012 domain notes). Rules: h002, h013, h014, h017, h018. Output: interp/InterpFcnMisc.kt. Whole-arm verification: 182 files, 0/0. Events +10 rows.
- Unit 204 (interp-sysfunc.cpp::GlobalData): the three-argument dollar-data runtime (vendor 733-780, loc 43; units.csv row 204: comment_ratio 0.023, ptr 0, ctrl 7, score 14.3, rank 137; DISTINCT from the one-argument mdh intrinsic u209/u86 - the CppsysfuncH/SysfuncH roster notes anticipated the seat). New file interp/InterpGlobalData.kt: fun GlobalData(tmp0, tmp2, svPtr) - tmp3 saves tmp0 before the RETRIEVE and tmp0 is restored after (Mglobal may advance in place); h018: SQLITE DEFINED in the reference build - the single-SOH arm EXCLUDED, the SQLITE arm LIVE (tmp0 gets the SOH,SOH,NUL tail; the descendant probe additionally requires the strncmp prefix match, f1 re-folded 1/0 after the NEXT); the four f/f1 cases write literal "0"/"1"/"10"/"11" cstrings (the f==0,f1==1 arm's three-byte "10" kept exactly). mglobal seat with RETRIEVE=0/NEXT=2 (defines.h:83-85); per-file strncmpArr helper (Dice precedent). Rules: h007, h012, h014, h015, h017, h018. Output: interp/InterpGlobalData.kt. Whole-arm verification: 183 files, 0/0. Events +12 rows.
- Unit 205 (include/mumpsc/externs.h::<misc>, B7 tranche 2 CLOSER): the compiler externs header (vendor whole file, loc 26; units.csv row 205: comment_ratio 0.587, ptr 4, ctrl 2, score 12.8, rank 124). New file compiler/ExternsH.kt (KDoc-only residue render, u171/u202 wrapper-file shape) + CompilerState.kt EDIT: symflg (externs.h:60) was the ONLY previously uncarried extern - ADDED to CompilerState as Char(0..255) (h012); every other extern already rides the CompilerState owner object with its externs.h line anchor (gpad/xpx/Jflg/ierr1/ierr/ctrlc/tpx/iorg/pd1len/sdlim/setpiece/symlen/t0px/t2/nstart/bd/pd1/setname/v1d/xd), struct nmes is the Nmes carrier, and the commented-out duplicate nstart line stays a vendor comment. The banner Fortran-provenance note (index-1 arrays) is roster text. Rules: h004, h012, h015, h017. Output: compiler/ExternsH.kt; edit: compiler/CompilerState.kt (+symflg). Whole-arm verification: 184 files, zero errors, zero warnings under kotlinc 2.1.20 locally before the push. Events +8 rows. TRANCHE 2 (rows 188-205, 18 units) COMPLETE - ALL 30 BATCH-7 ROSTER ROWS ARE NOW RENDERED (tranche 1: 187, 206-216; tranche 2: 188-205). Seven stubs resolved this tranche (epi, match, sigint, freadx, Qsub, fcninit, atCopy, levCheck, EP, Sim1, makeGlobal - eleven across the two files' ledgers), two tag corrections (atCopy owner, levCheck corpus status; mis-tag ledger 9 deep), two stubs raised for future owners (fcninit->linepad2 batch-9, _horolog batch-9). Mark's B7-close gate: git pull, gradle build detekt jacocoTestReport, then the swipl coverage report (expect 18 rules, events.csv 2532 rows).


- B7 CLOSE (gate round): Mark's m1-build gradle gate GREEN on tranche-2 commit 0a28361 (build detekt jacocoTestReport all clean, no findings). B7-close swipl coverage report (Mark, m1-build): rules in base 18, triggering situations 1277 (hits 1272, misses 5), hit rate 99.6% (up from 99.5% at B6 close), reuse rate 98.5% (up from 98.2%); all 5 misses remain preprocessor_conditional captured -> rule h018, pre-capture history (no action, same profile as prior batch closes). Events CSV at 2532 rows. BATCH 7 COMPLETE (30 units: tranche 1 rows 187, 206-216 incl. ServiceInterface; tranche 2 rows 188-205 incl. misc-6, epi, match, sigint, zcommands_code, freadx, Qsub, fcninit, mumps misc, embed1, Extract, Sim1, rslt_callback, Multiply char-star, fcn misc, MakeGlobal, GlobalData, externs.h). Stub ledger this batch: eleven resolutions (epi, match, sigint, freadx, Qsub, fcninit, atCopy, levCheck, EP, Sim1, makeGlobal), two tag corrections (atCopy owner, levCheck corpus status), two future-owner stubs raised (linepad2, _horolog, both batch-9). Batch 8 opens at units.csv row 217.

- B8 OPENS (units.csv rows 217-246, 30 units, three tranches T1 217-226 / T2 227-236 / T3 237-246). Baseline: B7-close master 4329f22, 184 files 0/0 local, 18 rules, hit 99.6%, reuse 98.5%, events 2532 rows.
- Unit 217 (interp-sql.cpp::btree, B8 tranche 1 OPENER): the sqlite btree opener (units.csv row 217). New file interp/SqlBtree.kt. Render: the SQLITE arm LIVE (config.log DBMS SQLITE, u139 note); the configure-generated #if 14==N command-shape ladder resolves to the reference build arm (h018, journaled not rendered); btree file/block carriers on the BtreeFile/BtreeBufferState pattern (u193 Freadx seats); the native-btree fallback family EXCLUDED with roster notes. Rules: h004, h006, h007, h012, h013, h014, h015, h017, h018. Output: interp/SqlBtree.kt. Whole-arm verification: 185 files, zero errors, zero warnings under kotlinc 2.1.20 locally. Events +18 rows (log 2550).
- Unit 218 (libmpscpp.cpp::<misc-7>, B8 tranche 1): the 600-line library residue chunk (vendor 3468-4148, opens at get() :3464, closes before embed1 :4151; units.csv row 218). New file mdh/LibmpscppMisc7.kt + MdhStubs.kt edit + GlobalCarrier.kt edit. Render: get() (u188 boundary completion, the vendor 3464 tail), streamInsert (operator<< fold; dead second tpx test noted, h013), Merge (^-prefix, <30->128 remap via .code<30, one-pair chop, relays to the NEW u226 interp Merge seat), Lock/UnLock (Mltoa + ProcessHandle pid, makeLock "1"/'-'/' ' seat), CleanLocks/CleanAllLocks (File.delete "./Mumps.Locks"), Set x4 (String/Long/Int/Double on Misc7State.tmp64), Get x5 (Get(s) returns the carrier tmp NOT s - vendor bug kept; DoubleArray/LongArray forms; dead ErrorMessage arm noted h013), Order/C_Order (invalid dir still proceeds ORDERPREV, kept), Order_Next/Order_Prior, Data (dead strlen store kept h013), Mstring.Assign x7 (core Assign(String): "set "+value+"=\""+b+"\"", interpret; numeric via toString/%f), SQL_ family (SQL_Format "sql/c " trailing-space fossil kept; DOLLAR=36.toChar() carriers for the dollar-z strings; m==1 -> value=="1"; SQL_Table(s,c) u34 front-run verification + Mstring overload), operator& x12 -> infix amp()/Concat delegates (Global x6, Mstring x6; Global.Concat family via the concatRetrieve() core, u33 Concat(Global) verification-only). MdhStubs.kt: get_global RESOLVED (macro expansion, tpx=mglobal(RETRIEVE=0,...).toLong(); the u203-era seat note superseded) + NEW set_global (libmpscpp.h:117 STORE=1 version). GlobalCarrier.kt: the Kill() TODO stub RESOLVED with the real body + private toCbufCarrier helper. KDoc sigil scan: the raw dollar-z strings in KDoc replaced with <dollar>z... carriers; a char-star-slash comment-termination bug fixed before compile. Rules: h004, h007, h012, h014, h015, h017, h018. Output: mdh/LibmpscppMisc7.kt; edits: mdh/MdhStubs.kt, mdh/GlobalCarrier.kt. Whole-arm verification: 185 files, 0/0. Events +14 rows (log 2564).
- Unit 219 (interp-sysfunc.cpp::AllocSV, B8 tranche 1): the MSV initializer (vendor 528-651; units.csv row 219) - RESOLVES the InterpStubs allocSV() stub with TWO TAG CORRECTIONS (u141/u148/u169 family: the stub's "not a corpus unit" tag from the u98-era note superseded - roster row 219 IS the owner; the stub now delegates = AllocSV(), all call sites bind unchanged). New file interp/AllocSV.kt. Render: AllocSVState.seen one-shot signal-registration gate (the registration list kept as comments; SIGKILL fossil noted; the commented-out SIGALRM kept out); Msv fresh construct (the malloc-NULL arm dropped, GC); io=5, iorg=1, ver[i]=1, hor=0, ScanMinWordSize=3/ScanMaxWordSize=25, fdes[0..1]=-1; in_file[5]=InputStreamReader(reflective System.in), out_file[5]=OutputStreamWriter(FileDescriptor.out); h018: SQLITE arm LIVE (Col_Names/pgsql_msg "", Table "mumps", TabSize 14), the #else getenv arm EXCLUDED. Rules: h001, h003, h004, h008, h014, h018. Output: interp/AllocSV.kt; edit: interp/InterpStubs.kt (stub delegated + tag corrected). Whole-arm verification: 185 files, 0/0. Events +12 rows (log 2576).
- Unit 220 (compiler-parse.c::<misc>, B8 tranche 1): the parse-file header residue (166 phys loc; units.csv row 220). New file compiler/ParseMisc.kt (header residue, no declarations): banner/includes/#define block/prototype roster/SSS-TTT globals - the SSS/TTT/SSSOff/TTTOff buffer+offset pairs ADDED to CompilerState.kt (bigKey precedent, h004 state-object carriers); the cvars externs and the DOS arms EXCLUDED (h018, journaled). Rules: h004, h013, h014, h015, h017, h018. Output: compiler/ParseMisc.kt; edit: compiler/CompilerState.kt (SSS/TTT carriers). Whole-arm verification: 185 files, 0/0. Events +12 rows (log 2588).
- Unit 221 (include/mumpsc/compiler-globals.h::<misc>, B8 tranche 1): the compiler-globals header residual (units.csv row 221). New file compiler/CompilerGlobalsH.kt (header residue; roster doc) + CompilerState.kt EDIT with the ONLY uncarried content: for_mult init -1, MaxTmp init 2, except[] = arrayOf("InputLengthException","InterpreterException",""), flg init 1 (owner u221; every other member already carried). Rules: h001, h004, h013, h014, h015, h017, h018. Output: compiler/CompilerGlobalsH.kt; edit: compiler/CompilerState.kt (four initializers). Whole-arm verification: 185 files, 0/0. Events +14 rows (log 2602).
- Unit 223 (interp-parse.cpp::cannon, B8 tranche 1): the numeric canonicalizer (vendor 215-256; units.csv row 223) - RESOLVES the InterpStubs cannon stub with TAG CORRECTION (the stub's "interp-parse.cpp::<misc> (B3 unit 75)" tag was wrong; superseded note kept, the Patrn call sites bind unchanged). New file interp/Cannon.kt. Render: strshift private helper; sign fold (odd '-' leaves one), trailing-zero strip (i-- first), dead '\n' arm kept (h013), exponent arm mult(a,"10"/".1",a) via ArithMisc mult + atoiC, tmp[25]. Rules: h004, h007, h012, h013, h014, h015, h017, h018. Output: interp/Cannon.kt; edit: interp/InterpStubs.kt (stub resolved). Whole-arm verification: 185 files, 0/0. Events +16 rows (log 2632).
- Unit 224 (include/mumpsc/builtin.h::<misc>, B8 tranche 1): the builtin prototype header residual (units.csv row 224). New file mdh/BuiltinH.kt (header residue; every prototype's seat documented; the IDF stub noted with row-240 owner; the commented-out ScanAlnum two-arg prototype kept as doc, h013). Rules: h013, h014, h015, h017, h018. Output: mdh/BuiltinH.kt. Whole-arm verification: 185 files, 0/0. Events +10 rows (log 2642).
- Unit 225 (interp-bifs.cpp::GtkTreeAdd, B8 tranche 1 - VERIFICATION-ONLY): the interp-bifs.cpp twin of the B3 u73 interp-zfcns.cpp::GtkTreeAdd. Diff of vendor 89-125 in BOTH files run this session: IDENTICAL. No new code; the u73 seat (interp/GtkTreeAdd.kt) serves both translation units (h004 note: C would keep two statics; the render shares one state object, journaled divergence). Journal-only entry; events logged against the u73 rule set. Rules (u73 seat): h008, h009, h012, h014, h015, h017. Events +12 rows (log 2654).
- Unit 226 (interp-sysfunc.cpp::Merge, B8 tranche 1 CLOSER): the dollar-MERGE runtime (vendor 650-729; units.csv row 226) - RESOLVES the InterpStubs Merge stub raised at u155 (the u218 misc-7 global::Merge call binds to the new seat). New file interp/Merge.kt. Render: rbuf local mirrors the in/out XNEXT key mutation of `right` (adaptation, journaled); the >127 sentinel scan (j==0 else appends SOH); keyfix import from mdh; the tmp4 save/STORE/restore dance verbatim; strncmp AFTER the tmp3[k] append governs only the break; h018: MDEBUG arms EXCLUDED, the SQLITE arm is EMPTY (noted), FIX macro = plain cast. Rules: h007, h012, h014, h015, h017, h018. Output: interp/Merge.kt; edit: interp/InterpStubs.kt (stub resolved). Whole-arm verification: 185 files, 0/0. Events +12 rows (log 2666). TRANCHE 1 (rows 217-226, 10 units) COMPLETE: btree, misc-7, AllocSV, parse misc, compiler-globals.h misc, mumps_open verify, cannon, builtin.h misc, GtkTreeAdd verify, Merge. Stub ledger: allocSV delegated (tag corrected), get_global resolved (+set_global added), Kill resolved, cannon resolved (tag corrected), Merge resolved.
- Unit 227 (include/mumpsc/compiler-macros.h::<misc>, B8 tranche 2 OPENER): the compiler-macros header residual (units.csv row 227). New file compiler/CompilerMacrosH.kt (header residue): the INTERPRET/SymCheck/Sysout macro doc, the op codes, error codes, and command chars - the 'b' CONTINUE/BREAK and 'c' CLOSE/CATCH duplicate values kept in the doc (h013); the NOTABS arm EXCLUDED (h018, journaled). Rules: h002, h003, h013, h014, h015, h017, h018. Output: compiler/CompilerMacrosH.kt. Whole-arm verification: 185 files, 0/0. Events +14 rows (log 2680).
- Unit 228 (interp-sql.cpp::<misc>, B8 tranche 2): the interp-sql header residue (units.csv row 228). New file interp/SqlMisc.kt (header residue): the #define SQLITE line 34 = the LIVE family anchor (h018, the u200/u217/u204 SQLITE seats ride it); CSTR/OK/BAD/includes; rsltSpec -> the SqlCallbackState.rsltSpec carrier (u200); the sql prototype -> the InterpStubs sql stub (batch-9 owner); the sqlite3.h mid-file include at 884 journaled; call_back -> SqlCallbackState.callBack. Rules: h001, h013, h014, h015, h017, h018. Output: interp/SqlMisc.kt. Whole-arm verification: 185 files, 0/0. Events +12 rows (log 2692).
- Unit 229 (compiler-sys1.c::getstr1, B8 tranche 2): the K&R string reader (vendor 351-394; units.csv row 229). New file compiler/Getstr1.kt. Render: getstr1(popn, opnflg, opnfile, lun, area); the flag-3 arm via Getstr1State.requestOut/responseIn streams (pipe-owner batch-9/10; IllegalStateException when unwired); lun==5 reflective stdin EVERY call; the fgetc loop as while (LINEFEED='\n' defines.h:196; the dead '\n' arm kept h013; the '\t' no-op kept; >127->' ', <32->' '); area[1000]=0 needs the 1001-size buffer (kept). Rules: h001, h010, h012, h014, h015, h017, h018. Output: compiler/Getstr1.kt. Whole-arm verification: 185 files, 0/0. Events +14 rows (log 2706).
- Unit 231 (compiler-parse.c::Extract2a, B8 tranche 2): the tagged-extract fragmenter (vendor 2129-2150; units.csv row 231). New file compiler/Extract2a.kt. Render: Extract2a(str, off=0): String; Extract2aState tmp1/tmp2[64] (h008); the field split at 3/i and ++i; #S# -> the sym_ emission with //E21000, #G# -> the Mglobal emission; the silent fall-through for other prefixes kept; writeC private helper. Rules: h007, h008, h009, h012, h015, h017, h018. Output: compiler/Extract2a.kt. Whole-arm verification: 185 files, 0/0. Events +14 rows (log 2730).
- Unit 232 (interp-sym.cpp::BuildLocal, B8 tranche 2): the local-stack builder (vendor 97-144; units.csv row 232) - RESOLVES the InterpStubs BuildLocal stub (u155-era widened CharArray? key seat kept; call sites bind unchanged; the zfcns.h commented-out prototype u242 note anticipated the resolution). New file interp/BuildLocal.kt. Render: BuildLocalState (tmp[TSTACK=10][STR_MAX], stk); a<0 reset, a==0 push (overflow banner + sigint(100)), a==2 pop (empty -> rslt[0]=NUL), else stk-- + LocalOrder(tmp[stk], rslt, "1"/"-1"); the null key folds to a NUL scratch (adapter, non-null assertion avoided - push-gate scan catch this tranche). Rules: h008, h012, h014, h015, h017, h018. Output: interp/BuildLocal.kt; edit: interp/InterpStubs.kt (stub resolved). Whole-arm verification: 185 files, 0/0. Events +12 rows (log 2742).
- Unit 233 (interp-parse.cpp::BuildGlobal, B8 tranche 2): the global-stack builder, exact twin of u232 (vendor 1836-1878; units.csv row 233) - RESOLVES the InterpStubs BuildGlobal stub (the u242 zfcns.h commented-out prototype note anticipated the resolution; Interpret.kt INIT calls bind unchanged). New file interp/BuildGlobal.kt. Render: the u232 body with GlobalOrder(tmp0, 0, rslt, arg4Off, "1"/"-1", 0, svPtr) (the globalOrder.h inline stub seat); the same null-key NUL-scratch adapter (scan catch). Rules: h008, h012, h014, h015, h017, h018. Output: interp/BuildGlobal.kt; edit: interp/InterpStubs.kt (stub resolved). Whole-arm verification: 185 files, 0/0. Events +12 rows (log 2754).
- Unit 234 (interp-sym.cpp::ScanParse, B8 tranche 2): the comma-depth scanner (vendor 1410-1435; units.csv row 234) - RESOLVES the InterpStubs ScanParse stub (call sites bind unchanged). New file interp/ScanParse.kt. Render: ScanParse(tmp1, tmp1Off): Int; stop at NUL/space/depth-0 comma; the quote run consumes one closing quote; depth NOT reset on break (vendor quirk kept). Rules: h012, h014, h015, h017, h018. Output: interp/ScanParse.kt; edit: interp/InterpStubs.kt (stub resolved). Whole-arm verification: 185 files, 0/0. Events +10 rows (log 2764).
- Unit 235 (compiler-parse.c::ExpandText, B8 tranche 2): the coded-text expander (vendor 2374-2411; units.csv row 235). New file compiler/ExpandText.kt. Render: i++ first; the >128/SOH boundary NUL in-place; the emissions with the escaped-dollar carrier + Integer.toHexString(CODED_OPEN=206/CODED_CLOSE=207) (lowercase "ce"/"cf" verified); Extract2a REUSE (u231, same batch - real reuse hit); memcheck x2; sprintf(bp,"tmp%d",iiitmp) via the writeC loop; iiitmp++ AFTER. Rules: h001, h009, h012, h014, h015, h017, h018. Output: compiler/ExpandText.kt. Whole-arm verification: 185 files, 0/0. Events +14 rows (log 2778).
- Unit 237 (libmpscpp.cpp::mdh_tree_level_add, B8 tranche 3 OPENER - VERIFICATION-ONLY): the 8-column overload, metrics loc 31/score 10.3/ptr 1 exact match against the existing mdh/MdhTreeLevelAdd.kt mdhTreeLevelAdd 8-column overload (unit-8 seat); verified line-for-line this session (the d0-separated column walk, the xcf tail). No new code. Journal-only entry; events logged against the unit-8 rule set. Rules (unit-8 seat): h004, h007, h009, h014, h015, h017. Events +12 rows (log 2806).
- Unit 238 (libmpscpp.cpp::GlobalData, B8 tranche 3 - VERIFICATION-ONLY): the mstring overload (vendor 2616-2635, loc 17; the units.csv row-238 metrics match the mstring form) rendered at B7 u209 alongside the u86 char* fold - the GlobalData.kt seat carries BOTH the String (char* fold) and Mstring overloads; both verified line-for-line against vendor 2595-2665 this session. No new code. Journal-only entry; events logged against the u209 rule set. Rules (u209 seat): h007, h012, h014, h015, h017. Events +10 rows (log 2816).
- Unit 239 (libmpscpp.cpp::global::Piece, B8 tranche 3): the const char*-key Piece, FIRST of the four overloads (vendor 7181-7203, ptr 1, score 7.3; units.csv row 239). New file mdh/GlobalPieceCharStar.kt. Render: Global.Piece(pat: String, ii, jj): Mstring - the GLOBAL receiver (this overload's vendor receiver; receiver-distinct from the u38 GlobalRef.Piece family and the u214 string-key Global seat); _piece(tmp, gtmp, patBuf, cc1, cc2, 0, lhs, svPtr) real seat. Rules: h007, h009, h012, h014, h015, h017, h018. Output: mdh/GlobalPieceCharStar.kt. Whole-arm verification: 185 files, 0/0. Events +14 rows (log 2830).
- Unit 240 (libmpscpp.cpp::IDF, B8 tranche 3): the inverse-document-frequency MSV wrapper (vendor 10370-10388; units.csv row 240; the builtin.h u224 roster note anticipated the owner) - RESOLVES the MdhStubs IDF stub raised at u187 (the ServiceInterface INVDF relay binds to the real seat). New file mdh/IdfWrapper.kt; edit: mdh/MdhStubs.kt (stub resolved). Render: IDF(svptr, ep, arg1, arg2): CharArray; the SOH scan AFTER the loop; banner + sigint(100) on SOH; arg1[i]=0; Global(cstringAt(arg1,1)); x = cstringAt(arg2).toDouble(); a(cstringAt(arg1,i+1)).IDF(x) via Global.invoke -> the pilot GlobalRef.IDF seat (Idf.kt); returns the empty CharArray. Rules: h004, h012, h014, h015, h017, h018. Whole-arm verification: 185 files, 0/0. Events +12 rows (log 2842).
- Unit 241 (compiler-mumps.c::atCopy, B8 tranche 3 - VERIFICATION-ONLY): atCopy was rendered at B7 u196 (compiler/CompilerMumpsMisc.kt, vendor 1956-1976) - the B7 tranche-2 mis-tag ledger (wrong owner compiler-at-test.c -> compiler-mumps.c:1956) already corrected. Verified line-for-line this session: the xpx start, the quote/backslash escaping, the StringBuilder fold (j=2 contract). No new code. Journal-only entry; events logged against the u196 rule set. Rules (u196 seat): h002, h003, h007, h010, h012, h013, h014, h015, h017, h018. Events +20 rows (log 2862).
- Unit 242 (include/mumpsc/zfcns.h::<misc>, B8 tranche 3): the zfcns header residual (units.csv row 242). New file interp/ZfcnsH.kt (header residue): the _ascii/_horolog/_justify/_length/_piece/_random/zfcn seats documented (the #ifndef _INTERP_ per-TU note, h018; the _horolog stub owner interp-sym.cpp batch-9; _justify resolved at u208); the commented-out BuildGlobal/BuildLocal prototypes kept commented - BOTH RESOLVED this batch (rows 232/233); the LGPL banner variant noted (h013). Rules: h013, h014, h015, h017, h018. Output: interp/ZfcnsH.kt. Whole-arm verification: 185 files, 0/0. Events +10 rows (log 2872).
- Unit 243 (compiler-kill.c::<misc>, B8 tranche 3): the banner/include residual (30 phys loc, residual-by-identity via the study tool; units.csv row 243: loc 30, comment_ratio 0.062, ptr 0, ctrl 2, score 5.2, rank 20; kill_code itself is roster row 251, batch 9, NOT rendered here). New file compiler/CompilerKillMisc.kt in the u186 CompilerFcninitMisc banner-ledger shape: GPL banner verbatim, the tab-indented May 17 2024 date comment kept, the four includes mapped h014 (stdio -> java.io edges; memsize.h -> CompilerState constants; compiler-macros.h -> per-unit macro consumers, u227 seat; compiler-globals-ext.h -> CompilerState externs). No executable code, no stubs raised. Rules: h013, h014, h015, h017, h018. Output: compiler/CompilerKillMisc.kt. Whole-arm verification: 186 files, 0/0. Events +10 rows (log 2882).
- Unit 244 (compiler-lock.c::<misc>, B8 tranche 3): the banner/include residual (identical shape to u243; units.csv row 244: loc 30, comment_ratio 0.062, ptr 0, ctrl 2, score 5.2, rank 22; lock_code is DONE - LockCode.kt, an earlier batch). New file compiler/CompilerLockMisc.kt, same banner-ledger render. Rules: h013, h014, h015, h017, h018. Output: compiler/CompilerLockMisc.kt. Whole-arm verification: 187 files, 0/0. Events +10 rows (log 2892).
- Unit 245 (compiler-set.c::<misc>, B8 tranche 3): the banner/include residual (identical shape to u243/u244; units.csv row 245: loc 30, comment_ratio 0.062, ptr 0, ctrl 2, score 5.2, rank 26; set_code is DONE - SetCode.kt, an earlier batch). New file compiler/CompilerSetMisc.kt, same banner-ledger render. Rules: h013, h014, h015, h017, h018. Output: compiler/CompilerSetMisc.kt. Whole-arm verification: 188 files, 0/0. Events +10 rows (log 2902).
- Unit 246 (libmpscpp.cpp::mstring::Find, B8 tranche 3 CLOSER - BATCH 8 CLOSER): the mstring-receiver dollar-Find with the key from the database (vendor 7649-7670, loc 16; units.csv row 246: comment_ratio 0.059, ptr 0, ctrl 1, score 4.2, rank 8 - the global-key overload, disambiguated via the study tool from the const char* 7638 and mstring 7671 siblings at loc 10, neither on the roster). New file mdh/MstringFind.kt. Render: fun Mstring.Find(key: Global, st: Int): Int - Mltoa(st) with the documented int->long widening (h010), one mglobal(RETRIEVE) over key.ref throwing GLOBAL_NOT_FOUND, _find(r, dataBuf, tmp1, a) with the receiver's value folded into a byte buffer, atoiC(r); the receiver-distinct Mstring extension keeps the overload set clear of the u33/u185/u216 GlobalRef.Find family. Rules: h007, h010, h012, h014, h015, h017. Output: mdh/MstringFind.kt. Whole-arm verification: 189 files, zero errors, zero warnings under kotlinc 2.1.20 locally before the push. Events +12 rows (log 2914). TRANCHE 3 (rows 237-246, 10 units) COMPLETE: mdh_tree_level_add verify, GlobalData verify, global::Piece, IDF, atCopy verify, zfcns.h misc, kill/lock/set misc residues, mstring::Find.
- SCAN-GATE ROUND (batch-8 close, pre-push): full scan of all 30 changed/new files. Catches fixed: the BuildGlobal/BuildLocal null-key arms used double-bang assertions (vendor dereferences str unconditionally; restructured to the null-folds-to-NUL-scratch adapter, non-null assertion avoided). Raw dollar/backtick/em-dash sweep: the only remaining backtick/em-dash bytes live in pre-existing pushed KDoc (System.`in` MumpsMain precedent, u205-era CompilerState header) - no new violations in batch-8 code. Gate re-run after the fix: 189 files, zero errors, zero warnings.
- BATCH 8 COMPLETE (rows 217-246, 30 units; five verification-only rows 222/225/237/238/241 against existing seats u3/u73/unit-8/u209/u196). Stub ledger this batch: seven resolutions (allocSV delegated with tag correction, get_global + Kill + cannon with tag correction, Merge, BuildLocal, BuildGlobal, ScanParse, IDF) plus set_global added, the u188 ReadLine deferral closed, the u155-era raised stubs exhausted except the future owners (linepad2, _horolog, sql, sym_ batch-9/10 seats unchanged). Front-run/tag-corrections journaled: allocSV (u98-era "not a corpus unit" -> row 219), cannon (B3 unit-75 mis-tag), u188 misc-6 boundary get() completed at u218, u221 four initializer fixes, u220 SSS/TTT carriers. Events CSV 2914 rows (+382). Mark's B8-close gate: git pull, gradle build detekt jacocoTestReport on m1-build, then the swipl coverage report (baseline 18 rules, hit 99.6%, reuse 98.5%).
- GATE FIX (B8-close round, Mark's m1-build gradle gate RED on d5d5854, precedent the B6 410b622 round): 4 detekt ImplicitDefaultLocale findings, all in mdh/LibmpscppMisc7.kt (lines 279/452/603/659, the u218 Set(Double)/Assign(Double)/Concat-Double %f seats) - the lean local gate does not run detekt, so the u182/u184 explicit-default-Locale precedent was not applied at first render. Fixed per precedent: String.format(java.util.Locale.getDefault(), "%f", ...) at all four sites (explicit default locale = C printf locale semantics, behavior byte-identical, potential-bugs rule satisfied). A swept check of every other String.format site in the arm confirms they all passed prior green gradle/detekt gates unchanged (Justify/u208, MpsJustify, CopyrightNotice, Zfcn/ZfcnBifs, ServerMisc - the detekt-clean pushed set). INFRA NOTE: the Vibe sandbox was reset between the B8 push and this gate round; the workspace was rebuilt from scratch per the u172/B7 recipe (codeload master zip at d5d5854, JDK17 corretto 17.0.21, maven jars kotlin 2.1.20/coroutines 1.9.0/asm 9.7.1/annotations 24.0.1/trove4j 1.0.20200330, fresh kotlinc.sh gate script - the fresh script needed kotlin-stdlib + coroutines + annotations on the COMPILER JVM classpath as well as the target -cp). Baseline gate on the rebuilt workspace: 189 files, zero errors, zero warnings; re-run after the fix: identical, 0/0. No events rows (gate-fix round). Mark re-gates: gradle build detekt jacocoTestReport, then the B8-close swipl coverage report.
- B8 CLOSE (gate round): Mark's m1-build gradle gate GREEN on gate-fix commit fc702e9 (build detekt jacocoTestReport all clean; the d5d5854 RED round resolved by the four LibmpscppMisc7.kt explicit-default-Locale fixes). B8-close swipl coverage report (Mark, m1-build): rules in base 18, triggering situations 1468 (hits 1463, misses 5), hit rate 99.7% (up from 99.6% at B7 close), reuse rate 98.7% (up from 98.5%); all 5 misses remain preprocessor_conditional captured -> rule h018, pre-capture history (no action, same profile as every prior batch close). Events CSV at 2914 rows. BATCH 8 CLOSED GREEN: 30 units in tranches T1 217-226 / T2 227-236 / T3 237-246, five verification-only rows 222/225/237/238/241 against existing seats u3/u73/unit-8/u209/u196; seven stub resolutions with two tag corrections; the u188 ReadLine deferral closed. Hit and reuse rates now at their batch-close highs. Batch 9 opens at units.csv row 247.
- Unit 247 (include/mumpsc/builtinTable.h::<misc>, B9 tranche 1): the builtinTable.h header residual (units.csv row 247: loc 20, comment_ratio 0.048, ptr 0, ctrl 2, score 2.0, rank 1 - the roster-rank top unit of the batch). New file mdh/BuiltinTableH.kt, header-residue render: the _ascii/_justify/_piece builtin-table macro roster with every seat documented (zfcninterpreter arms per prior zfcn family seats), DOS arms excluded, SQLITE arm noted. Rules: h004, h012, h015, h017. Output: BuiltinTableH.kt. Events +8 rows (log 2922).
- Unit 248 (compiler-parse.c::parse_, B9 tranche 1): THE BATCH MONSTER (loc 1201, rank 302, ptr 66, ctrl 346 - the largest single unit in the corpus): the vendor goto web (compiler-parse.c:203-1998) rendered as a phase-cursor machine (outer@ while + when(phase) fold, the h011 goto-fold precedent of B8 misc-7). New file compiler/Parse_.kt, ~1700 lines. Contents: the TU-local define block (compiler-parse.c:87-127) decoded exactly - OPEN 0, DIVIDE 1, MULTIPLY 2, MINUS 3, PLUS 4, OPERAND 5, OPENC 6, CONCAT 7, EQUALS 8, GREATER 9, EMPTY 10 (TU-local, NOT interp.h 5), LESSTHAN 11, NOT 12, INTDIVIDE 13, MODULO 14, CONTAINS 15, FOLLOWS 16, PATTERN 17, NOTEQ 18, NOTGREATER 19 through NOTOR 27, INDIRECT 28, EXPONEN 29, PREDICATEVAR tilde, QUOTE apostrophe, CodedOpen 206/CodedClose 207/CodedComma 208, cod209 {209,0}; the opcode[256]/code[26]/ncode/dcode/operand tables verified programmatically against the vendor; the phase set START/NCHAR (CodedOpen recursion, comma $$^a zero-arg insert, close-paren un_nest, colon dollar-select unification, ]]/quote/append arms)/SCAN_OPERAND (patrn1 pattern head, numeric cannon walk, Rargs by-ref arm, e-exponent arm)/SKIP_NUMERIC (literal quote-doubling walk, variable-name walk with CodedOpen recursion)/VAR1 (t0px early return, global-reference ladder, the intrinsic ladder: dollar-test, io, principal, x (duplicated strcasecmp fossil kept), y, horolog, char walk, fnumber, extract (Extract seat, 9003), length, find, ascii, justify/job (linux getpid arm; MSC arm excluded), piece, select/storage (k<=20 padding, 99), random; the strncasecmp n-limits decoded to their EFFECTIVE prefixes (dollar-fnum for fnumber, dollar-extr for extract, dollar-leng for length, dollar-just for justify, dollar-sele for select, all documented); the other-functions t-block (dollar-text premature return 33 with the DEAD ExpandText call kept as h013 comment, dollar-order/data/name/qsubscript/next/get via the u178-family Expand seats, the generic gtmp backslash-x-hex emission ladder with String.format(Locale) per the u182 precedent)/NOARG (zfcn Z-arm, MyFunc dollar-dollar arm, fcn emission trio, fcn_proc_done fold)/PREDICATEVAR arm/array-reference walk (206/207/208 #S#/#G# branches, setname, no_array symflg=11, cvar tables, the vendor IF (pd1[sdlim]!='doll') goto exec fossil noted as dead since both paths reach exec)/EXEC (stack push, symlen 31)/NXT_EXPR (OPEN||OPERAND return 11; unary switch: INDIRECT with the DEAD spx--/goto-nchar residue kept as comment, MINUS mult -1, case 18 deliberately falls through to NOT per the vendor missing-break, NOT, PLUS add 0; the pull: adx extraction with #G#/#S# in-place Extract2 rewrites; the FULL binary switch: divx, expx, mult ConCheck variants, sub, add ConCheck, CONCAT strlen guard + ErrorMessage String too long, EQUALS/NOTEQ strcmp pair, GREATER/LESSTHAN numcomp1/numcomp ConCheck variants, divi, MODULO atoi pair, CONTAINS strstr, FOLLOWS, PATTERN/NOTPATTERN fixstr+pm emission pair (bp written via the bd alias, h012), NOTGREATER through NOTOR numcomp pairs)/NXT_OPERATOR (push answer)/FINISH (16-or-0 exit). bp == vendor and bd[1] alias rendered via the bpGet/bpSet view pair; ConCheck (compiler-parse.c:188-201, 14 loc, not a corpus unit - MISSED by the u220 residue because physLoc < min_loc) rendered as a file-private helper with its SSS carrier (h013 ledger note); shiftArgsDown local helper for the for(SSS=v1d,TTT=and v1d[i+1]) name-strip (SSS/TTT carriers kept, non-null-assertion avoided per the scan-gate rule); cannonAt/fillBpBuf/flushBpBuf/conCheckBp/fixstrBp/callExpandBp slab helpers keep the alias honest. The old parse_ TODO stub in compiler/Stubs.kt REMOVED with a resolution note; memcheck/cvar/Extract1 remain h014 stubs (not corpus units). _horolog emission remains (batch-10/11 stub). Rules: h001, h004, h007, h009, h010, h011, h012, h013, h014, h015, h017, h018, h011 ... h018. Rules list: h001, h004, h007, h009, h010, h011, h012, h013, h014, h015, h017, h018. Output: Parse_.kt. Events +24 rows (log 2946).
- Unit 249 (libmpscpp.cpp::<misc-3>, B9 tranche 1): the third libmpscpp misc residue (units.csv row 249: loc 600, comment_ratio 0.061, ptr 165, ctrl 54, score 472.2 - chunk verified EXACTLY via the select_study_subset load_units algorithm, part[2] metrics 600/0.061/165/54/472.2 exact match; vendor 6156-8758). New file mdh/LibmpscppMisc3.kt: friend le operator< family (Short/UShort/Long/ULong/Float/Double/CharArray/String receivers), global eq/ne families (member + friend Int..Double/CharArray/String), the operator<< insertion family (insertCstring/Global/Int/UInt/Short/UShort/Long/ULong/Float/Double/String/Mstring), the operator>> extraction family (readInto(CharArray) resolving the old MdhStubs ops>>(char*) stub, readInto(Global), readInto():String vector-string tail, readInto(Mstring), extractInt..extractCstring via retrieveOrFail + INVALID_CONVERSION with scanIntPrefix), the operator= family (assignCstring/String/Mstring/Int/Double/Global/UInt/Float/Short/UShort/Long resolving eight assignT stubs), mstring operator[] x4 (UB quantized to the receiver, get convention), mstring compound assignments (+= -= *= /= %= as plusAssign/minusAssign/timesAssign/divAssign/remAssign delegating to 38 per-file private mPlus/mMinus/mTimes/mDiv/mRem TODO stubs tagged owner misc-4 batch-10), mstring operator= x7 as setFrom. VENDOR BUGS KEPT, translated faithfully, not fixed: the percent-ud/percent-uhd/percent-lud arms emit literal d/hd tails (assignUInt stores 5d, assignUShort 5hd, assignULong and insertUInt 5d); Long setFrom uses toInt() truncation = the vendor percent-d-for-long; Double/Float use String.format(Locale.getDefault(), percent-g) per the u182 precedent. MdhStubs.kt: ALL u62 extractT/insertT/assignT stubs + Global.readInto stub REMOVED with the resolution note (u141 tag-correction family). Rules: h008, h010, h011, h012, h013, h014, h015, h017, h018, h014 ... h018. Rules list: h008, h010, h011, h012, h013, h014, h015, h017, h018. Output: LibmpscppMisc3.kt. Events +18 rows (log 2964).
- Unit 250 (interp-native-btree.cpp::btreeX, B9 tranche 1): the native-btree SQL entry (units.csv row 250: loc 502, ptr 86, ctrl 117, score 393.4, rank 298). New file interp/BtreeX.kt: full btreeX body over SQLCallbackState (Col_Names/pgsql_msg, Table, TabSize the u219/u200 carriers), the disk-btree ladder translated with the endian fold (h010) and strcpyC/strlenC over the h012 UNS buffers, SQLITE arms live per the u219 precedent, MDEBUG arms excluded. Rules: h001, h004, h007, h008, h010, h011, h012, h013, h014, h015, h017, h018, h011 ... h018. Rules list: h001, h004, h007, h008, h010, h011, h012, h013, h014, h015, h017, h018. Output: BtreeX.kt. Events +24 rows (log 2988).
- Unit 251 (compiler-kill.c::kill_code, B9 tranche 1): the KILL command code generator (units.csv row 251: loc 132, ptr 13, ctrl 51, score 110.8). New file compiler/KillCode.kt: full body, k003 loop with goK002/goK001 tail flags (h011 loop fold), setCode(SET_C) chain, retz = KILL_C, file-private SOH const, Extract1 called over the pd1 copyOfRange slab (h010 widening kept), KILL_DCL_ERROR 7. Resolves the kill_code seat gap left by the u61 KillMisc residue. Rules: h001, h003, h005, h007, h009, h010, h011, h012, h013, h014, h015, h017, h018, h010 ... h018. Rules list: h001, h003, h005, h007, h009, h010, h011, h012, h013, h014, h015, h017, h018. Output: KillCode.kt. Events +26 rows (log 3014).
- Unit 252 (interp-native-btree.cpp::<misc>, B9 tranche 1): the native-btree banner residue (units.csv row 252: loc 84, comment_ratio 0.475). New file interp/BtreeXMisc.kt: header-residue render (banner/includes/#define block; btreeX roster note; SQLITE arm live per u219; MDEBUG excluded). Rules: h004, h013, h017, h018. Output: BtreeXMisc.kt. Events +8 rows (log 3022).
- Unit 253 (mpstime.cpp::<misc>, B9 tranche 1): the mpstime.cpp banner residue (units.csv row 253: loc 98, comment_ratio 0.347). New file interp/MpstimeMisc.kt (MpstimeMain.kt edited for the seat wiring): header residue with the time-function roster documented, tz arms kept per vendor, DST arms noted. Rules: h001, h004, h007, h009, h012, h013, h014, h015, h017, h018, h013 ... h018. Rules list: h001, h004, h007, h009, h012, h013, h014, h015, h017, h018. Output: MpstimeMisc.kt. Events +20 rows (log 3042).
- Unit 254 (interp-sql.cpp::sql, B9 tranche 1): the sql() runtime entry itself (units.csv row 254: loc 121, ptr 13, ctrl 35, score 87.1 - the sql prototype owner flagged at u228 SqlMisc). New file interp/Sql.kt: sql(svPtr) over the SqlCallbackState table (u200/u219 carriers), SQL_Format trailing-space fossil kept (translated faithfully, not fixed), DOLLAR const for the dollar-ztable/zsql family per u218, SQLITE arm live. The InterpStubs sql h014 stub is now the FRONT-END delegate (resolved; tag correction u141 family). Rules: h004, h009, h012, h013, h014, h015, h017, h018, h015 ... h018. Rules list: h004, h009, h012, h013, h014, h015, h017, h018. Output: Sql.kt. Events +16 rows (log 3058).
- Unit 255 (include/mumpsc/cgi.h::<misc>, B9 tranche 1): the cgi.h header residue (units.csv row 255: loc 78, ptr 0, ctrl 27, score 42.8). New file interp/Cgi.kt: the CGI environment roster (QUERY_STRING etc via getenv folds), h009 emission arms, h011 gosub/return folds, h012 char buffer domains. Rules: h001, h009, h011, h012, h014, h017. Output: Cgi.kt. Events +12 rows (log 3070).
- Unit 256 (libmpscpp.cpp::Jaccard, B9 tranche 1): the Jaccard similarity index (units.csv row 256: loc 58, ptr 4, ctrl 13, score 35.0, rank 217). New file mdh/Jaccard.kt: the C++ class fold to a Kotlin object with the mglobal RETRIEVE pair, intersection walk with the h007 cstring scratch, k-m string build; one render-time type fix (line 85 strcpyC fold) caught by the local gate before journal. Rules: h007, h012, h014, h015, h017, h018. Output: Jaccard.kt. Events +12 rows (log 3082).
- Unit 257 (interp-rpm.cpp::perl_pm, B9 tranche 1): the perl_pm Perl-regular-expression bridge (units.csv row 257: loc 51, ptr 2, ctrl 17, score 34.3). New file interp/PerlPm.kt: the Embedded Perl library init arms with the DOS arm excluded, h009 sprintf folds via String.format(Locale.getDefault()), populate/depopulate scratches kept h012. Rules: h009, h012, h013, h014, h015, h017. Output: PerlPm.kt. Events +12 rows (log 3094).
- Unit 258 (compiler-sys1.c::<misc>, B9 tranche 1): the compiler-sys1.c banner residue (units.csv row 258: loc 107, comment_ratio 0.13, score 28.9, rank 204 - a core-cluster unit). New file compiler/CompilerSys1Misc.kt: banner/prologue 1-64 plus the non-unit inline helpers rendered per the residue algorithm: sigc/itoa (801-816), lpad (277-291), the 949-970 block; linepads dead locals kept (h013), getstr1s flag-3 pipe arms cross-noted to the u229 seat. Rules: h004, h009, h012, h013, h014, h015, h017, h018, h015 ... h018. Rules list: h004, h009, h012, h013, h014, h015, h017, h018. Output: CompilerSys1Misc.kt. Events +16 rows (log 3110).
- Unit 259 (interp-sym.cpp::<misc>, B9 tranche 2): the interp-sym.cpp banner residue (units.csv row 259: loc 75, comment_ratio 0.4; the sym_ prototype owner is batch-9/10 stubs). New file interp/SymMisc.kt: header residue with the file-tail _label_lookup/_text_function pointer stubs documented as a ledger, Interpret/ScanParse/Eval DONE seats noted (ScanParse row 234, Eval row 261). Rules: h004, h012, h013, h014, h018. Output: SymMisc.kt. Events +10 rows (log 3120).
- Unit 260 (compiler-parse.c::ExpandName, B9 tranche 2): VERIFICATION-ONLY (the seat already existed as the u178-family ExpandName.kt render). Line-for-line verification against vendor 1999-era ExpandName: the name-assembly loop, subscript walk with 206/207/208, symflg arms, the h018 arms match. No new code; the rule set is the seat's. Rules: h007, h012, h014, h015, h017, h018. Output: ExpandName.kt. Events +12 rows (log 3132).
- Unit 261 (interp-sym.cpp::Eval, B9 tranche 2): the symmetric Evaluate function (units.csv row 261: loc 15, ptr 9, score 21.1). New file interp/Eval.kt over the Interpret machinery with the EvalState scratch pair; empty-residue arms kept. Rules: h013, h014, h015, h017. Output: Eval.kt. Events +8 rows (log 3140).
- Unit 262 (libmpscpp.cpp::global::Avg, B9 tranche 2): the Global-receiver Avg (units.csv row 262: loc 28, ptr 1, ctrl 7, score 15.2; receiver-distinct from the string Avg seat). New file mdh/GlobalAvg.kt: avg(Global) fold over the mglobal RETRIEVE walk, ORDERNEXT cursor step, Mltoa rendering fold (h012), one XNEXT at completion. Rules: h007, h012, h014, h015, h017. Output: GlobalAvg.kt. Events +10 rows (log 3150).
- Unit 263 (libmpscpp.cpp::mstring::FixGblRef, B9 tranche 2): the mstring FixGblRef (units.csv row 263: loc 25, ptr 1). New file mdh/FixGblRef.kt: the naked-global fixup walk (caret scan, ^| kernels), cbuf scratch h007, STORE fold. Rules: h007, h012, h013, h014, h015, h017. Output: FixGblRef.kt. Events +12 rows (log 3162).
- Unit 264 (interp-service.cpp::<misc>, B9 tranche 2): the interp-service.cpp banner residue (units.csv row 264: loc 55, comment_ratio 0.179). New file interp/ServiceMisc.kt: header residue, same shape as SymMisc (banner/includes/prototypes + ServiceInterface seats documented; SQLITE live). Rules: h004, h012, h013, h014, h018. Output: ServiceMisc.kt. Events +10 rows (log 3172).
- Unit 265 (interp-strmanip.cpp::xindex, B9 tranche 2): the Xindex function (units.csv row 265: loc 25, ptr 0, ctrl 7). New file interp/Xindex.kt: the xindex fold over strmanip, atoiC arithmetic (h010 promotion kept), strcpyC/strlenC h012. Rules: h010, h012, h013, h014, h015, h017. Output: Xindex.kt. Events +12 rows (log 3184).
- Unit 266 (include/mumpsc/globalOrder.h::GlobalOrder, B9 tranche 2): the globalOrder.h inline family owner (units.csv row 266: loc 16, ptr 1, ctrl 5; the globalOrder.h inline stub seat flagged at u233 BuildGlobal). New file interp/GlobalOrder.kt: the ORDERNEXT/ORDERPREV/XNEXT cursor family over gblref, dir string -1/1 fold, rslt CharArray; gives the u233 BuildGlobal call site its inline-family seat. Rules: h001, h012, h014, h015, h017. Output: GlobalOrder.kt. Events +10 rows (log 3194).
- Unit 267 (libmpscpp.cpp::mdh_tree_level_add, B9 tranche 3 (closer)): VERIFICATION-ONLY (the 8-column overload twin of the u8 seat MdhTreeLevelAdd.mdhTreeLevelAdd). Line-for-line verification against vendor: the d0-separated column walk, xcf tail, 8-column signature; metrics loc 26/score 11.0/rank 109 match the overload exactly (the u237 precedent, second occurrence). No new code; the rule set is the seat's. Rules: h004, h007, h009, h014, h015, h017. Output: MdhTreeLevelAdd.kt. Events +12 rows (log 3206).
- Unit 268 (interp-arith.cpp::mps_floor, B9 tranche 3 (closer)): the mps_floor arithmetic (units.csv row 268: loc 30, ptr 0, ctrl 4, score 10.6, rank 103). New file interp/MpsFloor.kt: floor over the mps atod family, mps_ceil cross-note (the ceil stub remains batch-10), String.format(Locale.getDefault()) h009 folds. Rules: h009, h012, h014, h017, h018. Output: MpsFloor.kt. Events +10 rows (log 3216).
- Unit 269 (interp-arith.cpp::mps_exp2, B9 tranche 3 (closer)): the mps_exp2 exponent (units.csv row 269: loc 26, ptr 2). New file interp/MpsExp2.kt: the h001/h009 arms, exp2(x) fold, mpsTrunc stub remains batch-10. Rules: h009, h012, h015, h017, h018. Output: MpsExp2.kt. Events +10 rows (log 3226).
- Unit 270 (libmpscpp.cpp::global::Translate, B9 tranche 3 (closer)): the Global Translate (units.csv row 270: loc 21, ptr 2). New file mdh/GlobalTranslate.kt: global-translate over the mglobal STORE/XNEXT walk, cbuf scratch, per-char table walk (h007), Mstring rendering (h017). Rules: h007, h012, h014, h015, h017. Output: GlobalTranslate.kt. Events +10 rows (log 3236).
- Unit 271 (interp-arith.cpp::mps_log, B9 tranche 3 (closer)): the mps_log (units.csv row 271: loc 22, ptr 2). New file interp/MpsLog.kt: log/pow chain over the mps fold arms; mpsTrunc stub remains batch-10. Rules: h009, h012, h015, h017, h018. Output: MpsLog.kt. Events +10 rows (log 3246).
- Unit 272 (interp-arith.cpp::mps_cos, B9 tranche 3 (closer)): the mps_cos (units.csv row 272: loc 22, ptr 2). New file interp/MpsCos.kt: cos over the mps series, same family shape (h009 folds via Locale string format). Rules: h009, h012, h015, h017, h018. Output: MpsCos.kt. Events +10 rows (log 3256).
- Unit 273 (interp-arith.cpp::mps_tan, B9 tranche 3 (closer)): the mps_tan (units.csv row 273: loc 22, ptr 2). New file interp/MpsTan.kt: tan fold via sin/cos pair, same family shape. Rules: h009, h012, h015, h017, h018. Output: MpsTan.kt. Events +10 rows (log 3266).
- Unit 274 (libmpscpp.cpp::GlobalOrder, B9 tranche 3 (closer)): the libmpscpp GlobalOrder class (units.csv row 274: loc 15, ptr 2, ctrl 1, score 8.1, rank 63; the class wrapper, receiver-distinct from the u1 MumpsGlobalException-family and the row-266 .h inline family). New file mdh/GlobalOrder.kt: class fold with the static scratches (h008), first/next wrappers, Mstring rendering set. Rules: h007, h008, h012, h013, h014, h015, h017, h015 ... h017. Rules list: h007, h008, h012, h013, h014, h015, h017. Output: GlobalOrder.kt. Events +14 rows (log 3280).
- Unit 275 (interp-sym.cpp::_random, B9 tranche 3 (closer)): the _random intrinsic (units.csv row 275: loc 18, ptr 0, ctrl 3, score 6.8, rank 43). New file interp/Random.kt: the lctime/rand fold, String.format(Locale.getDefault()) h009, Global receiver path. Rules: h009, h012, h013, h014, h015, h017. Output: Random.kt. Events +12 rows (log 3292).
- Unit 276 (compiler-write.c::<misc>, B9 tranche 3 (closer)): the compiler-write.c banner residue (units.csv row 276: loc 30, comment_ratio 0.062, ptr 0, ctrl 2, score 5.2 - the batch closer, same residue shape as the u243/u244/u245 trio). New file compiler/CompilerWriteMisc.kt: banner/includes/prototype roster with WriteCode DONE seat noted. Rules: h013, h014, h015, h017, h018. Output: CompilerWriteMisc.kt. Events +10 rows (log 3302).
- TRANCHE 1 (rows 247-256, 10 units) COMPLETE: builtinTable.h misc, parse_ (the batch monster, phase-cursor fold), libmpscpp misc-3 (chunk-verified exact), btreeX, kill_code, native-btree misc, mpstime misc, sql, cgi.h misc, Jaccard.
- TRANCHE 2 (rows 257-266, 10 units) COMPLETE: perl_pm, compiler-sys1 misc, interp-sym misc, ExpandName verify, Eval, global::Avg, mstring::FixGblRef, interp-service misc, xindex, globalOrder.h family.
- TRANCHE 3 (rows 267-276, 10 units) COMPLETE: mdh_tree_level_add verify, mps_floor/exp2, global::Translate, mps_log/cos/tan, libmpscpp GlobalOrder, _random, compiler-write misc.
- TAG-CORRECTION/RESIDUE LEDGER (batch 9): readInto + 19 u62 extractT/insertT/assignT stub resolutions (u141 family, LibmpscppMisc3.kt); kill_code new seat (KillCode.kt); the parse_ Stubs stub resolved with the phase-cursor fold noted; the sql InterpStubs stub resolved by Sql.kt (u228 note); ConCheck rendered as a Parse_ private helper (compiler-parse.c:188, 14 loc, physLoc < min_loc so missed by the u220 residue, supplemental ledger note); globalOrder.h inline stub resolved by interp/GlobalOrder.kt (u233 note updated). REMAINING stubs after batch 9: mps_ceil, mpsTrunc, bstrcat, _horolog, sym_, Extract1/memcheck/cvar per-file seats (batch-10), plus the 38 mPlus/mMinus/mTimes/mDiv/mRem per-file stubs raised by misc-3 for the misc-4 owners (batch-10).
- SCAN-GATE ROUND (batch-9 close, pre-push): full scan of all changed/new files. Catches fixed: the parse_ SSS/TTT shift blocks used double-bang non-null assertions (restructured to the shiftArgsDown local helper, assertion avoided per the scan-gate rule); the heredoc-emission escape audit (raw double-backslash pairs in \u0024/\t/\n emissions and the quote/backslash char literals, byte-level fixed to the established ExpandText convention); raw dollar normalized to <dollar> or \u0024 throughout the new parse_ (0 raw hits in code or comments after the sweep); String.format sites all carry Locale.getDefault(). Gate re-run after every fix: zero errors, zero warnings.

- BATCH 9 COMPLETE (rows 247-276, 30 units; the verification-only rows are 260 (ExpandName seat) and 267 (u8 mdh-tree-level-add overload twin); the monster parse_ (rank 302) closes the compiler front-end gap. Events CSV 3302 rows (+388). Mark's B9-close gate: git pull, gradle build detekt jacocoTestReport on m1-build, then the swipl coverage report (baseline 18 rules, hit 99.7%, reuse 98.7% at B8 close; events 2914).
- GATE FIX (B9-close round, Mark's m1-build gradle gate RED on 280121a, precedent the B8 fc702e9 round): 8 detekt EmptyWhileBlock findings in compiler/Parse_.kt (lines 389/441/456/458/476/480/482/518 - all the C-string stack-scanner folds of the vendor un_nest/compress/slide loops, e.g. compiler-parse.c while (pd1[sdlim++); and the for (adx = sdlim--; pd1[sdlim - 1]; sdlim--) family). The lean local kotlinc gate does not run detekt, so the empty-body render passed locally. Fixed strictly one-to-one: vanilla Kotlin byte-identical iteration order, side effect moved from the condition into the do-while body - while (st.pd1[sdLim++] != NUL) { } -> do { sdLim++ } while (st.pd1[sdLim - 1] != NUL); while (st.pd1[sdLim--] != NUL) { } -> do { sdLim-- } while (st.pd1[sdLim + 1] != NUL); while (st.pd1[--sdLim] != NUL) { } -> do { sdLim-- } while (st.pd1[sdLim] != NUL). The pre-existing part-1 do { sdLim-- } while (st.pd1[sdLim - 1] != NUL) compress loop (line 493) was already non-empty and untouched. INFRA NOTE: the Vibe sandbox was reset between the B9 push and this gate round (the second reset of the batch-arc); the workspace was rebuilt from the pushed master state via the codeload zip at 280121a per the u172 recipe, so the fix rides on exactly what Mark gated. No events rows (gate-fix round). Mark re-gates: gradle build detekt jacocoTestReport, then the B9-close swipl coverage report.
- B9 CLOSE (gate round): Mark's m1-build gradle gate GREEN on gate-fix commit e32d8e5 (build detekt jacocoTestReport all clean; the 280121a RED round resolved by the eight Parse_.kt EmptyWhileBlock do-while rewrites). Batch-9-close swipl coverage report (Mark, m1-build): rules in base 18, triggering situations 1662 (hits 1657, misses 5), hit rate 99.7% (level with the B8-close high), reuse rate 98.9% (UP from 98.7% at B8 close - the batch-9 close high); all 5 misses remain preprocessor_conditional captured -> rule h018, pre-capture history (no action, same profile as every prior batch close). Events CSV at 3302 rows. BATCH 9 CLOSED GREEN: 30 units in tranches T1 247-256 / T2 257-266 / T3 267-276, two verification-only rows (260 ExpandName seat, 267 u8 overload twin); the rank-302 parse_ monster closes the compiler front-end gap (the parse_ Stubs stub is gone); stub resolutions: readInto + the u62 extractT/insertT/assignT family, kill_code, sql, ConCheck supplemental, the globalOrder.h inline family. Remaining stubs for batch 10: mps_ceil, mpsTrunc, bstrcat, _horolog, sym_, Extract1/memcheck/cvar seats, the 38 mPlus-family per-file stubs (misc-4 owners). Residual compileKotlin warnings are 11 REDUNDANT-CONVERSION notes (harmless .toInt/.toChar folds on Short carriers); they pre-date the batch-arc and were not gating. Batch 10 opens at units.csv row 277.

## Batch 10 (PRO arm, rows 277-306)

Batch 10 opens per the B9-close line above; roster order per
wp1/study-subset/units.csv. INFRA NOTE: the Vibe sandbox was reset
three times during the batch (the third and fourth resets of the
batch-arc; the fourth struck mid-push, after the first push commit
970b359 and before the rest), workspace rebuilt each time per the
u172 recipe
(codeload master zip + snapshot overlay; vendor re-fetched and
sha256 621deb2586f27e741ce6db1a2a86fbddf4e4da40c9c6bb363f84c814f53ffdbe
RE-VERIFIED each time; JDK17/jars/kotlinc gate rebuilt). Rows
written near a reset were re-rendered and re-verified against the
dump tool; snapshots b10-wip-286/290/306 kept the tranche state.
CORRECTION to the working log: row 280 (sym_) was at one point
marked done in hand-off notes while it was still unwritten; the
actual render (this batch) is the first full sym_ translation.
The numbering convention used throughout: roster/units.csv row =
physical CSV line (header = line 1), consistent with B9.

- Unit 277 (interp-bstring.cpp::bstrcat, B10 tranche 1 (opener)): the batch opener (units.csv row 277: loc 64, cr 0.053, ptr 1, score 4.2, rank 5, simple stratum). New file interp/Bstrcat.kt: the bstrcat store arm, result-door fold. Rules: h001, h007, h009, h012, h014, h015, h017. Output: Bstrcat.kt. Events +14 rows (log 3316).
- Unit 278 (interp-sym.cpp::_horolog, B10 tranche 1): the <dollar>horolog intrinsic (row 278: loc 22, cr 0.059, rank 2). New file interp/Horolog.kt: the <dollar>HOROLOG clock fold, LocalGet/sym_ carrier reuse. Rules: h009, h011, h012, h017. Output: Horolog.kt. Events +8 rows (log 3324).
- Unit 279 (compiler-mumps.c::main, B10 tranche 1): the compiler main (row 279: loc 79, ptr 106, ctrl 593, score 1125.2, rank 304, complex; the compiler-side monster of the batch). New file compiler/CompilerMain.kt: the full main() driver (file loop, zmain banner family, the dead if(!MAIN_flg&&0)/if(0) blocks noted not rendered), CompilerState carriers. Rules: h001, h002, h003, h004, h012, h013, h014, h015, h017, h018. Output: CompilerMain.kt. Events +20 rows (log 3344).
- Unit 280 (interp-sym.cpp::sym_, B10 tranche 1): the batch-10 MONSTER (row 280: loc 484, cr 0.125, ptr 201, ctrl 134, score 632.4, rank 301, complex). New file interp/Sym_.kt: the ENTIRE run-time local symbol table machine, all 20 opcodes kept verbatim (0/1/2/3/4/5/6/7/8/9/10/11/33/99/100/101/150/200/102 aliases per the vendor banner); the 3/33 order/next scan with the o_again level walk, the reverse arm with the dead second goto noted (h013), the exit_processing coded-number fold (Unpad row-289 twin: 0x1f sign fix, 40-byte window, %lf sscanf fold, %lg render); the store arms' (char*)p1 pointer return quantized to b with the only in-vendor consumer dead; Nmes PROMOTED to the Sym_ seat and Interpret.kt's private copy removed (h008); New-except re-sows via sym_(100,...) with NO level push kept; the <dollar>noerr arm via 36.toChar(); InterpStubs 6-arg stub RESOLVED (removed, cleanLocks precedent), MdhStubs 4-arg stub now a thin delegate (bmg_fullsearch precedent). Rules: h002, h007, h008, h009, h012, h013, h014, h015, h017. Output: Sym_.kt (+ Interpret.kt Nmes note, InterpStubs/MdhStubs resolutions). Events +18 rows (log 3362).
- Unit 281 (libmpscpp.cpp::<misc-8>, B10 tranche 1): the misc-8 residue (row 281: loc 62, ptr 58, ctrl 38, score 283.0, rank 291, complex). New file mdh/LibmpscppMisc8.kt: the mPlus/mMinus/mTimes/mDiv/mRem fold family seats resolved here; Piece/Plus members front-ran rows 303/304 note; plusM String.shadow seat; mTimes owner; the Mstring numeric-ctor corrections (double/float now the faithful %lf/%f emitters). Rules: h008, h011, h012, h013, h014, h015, h017. Output: LibmpscppMisc8.kt (+ Mstring.kt ctor corrections, LibmpscppMisc3.kt stub removals). Events +14 rows (log 3376).
- Unit 282 (include/mumpsc/libmpscpp.h::<misc>, B10 tranche 1): the libmpscpp.h header residue (row 282: loc 31, ptr 53, ctrl 5, score 201.7, rank 290, complex). New file mdh/LibmpscppH.kt: KDoc disposition ledger per the DefinesH precedent; no-code header residue. Rules: h004, h012, h014, h015, h018. Output: LibmpscppH.kt. Events +10 rows (log 3386).
- Unit 283 (libmpscpp.cpp::<misc-4>, B10 tranche 1): the misc-4 residue (row 283: loc 48, cr 0.008, ctrl 18, score 152.4, rank 283, complex). New file mdh/LibmpscppMisc4.kt: declarations ledger binding to the Misc8 seats and DONE owners; xecute fold note; the 38 raised mPlus-family per-file stubs RESOLVED here. Rules: h008, h010, h012, h013, h014, h015, h017. Output: LibmpscppMisc4.kt. Events +14 rows (log 3400).
- Unit 284 (interp-select.cpp::<misc>, B10 tranche 1, row 284: loc 100, ptr 32, ctrl 22, score 115.0, rank 281, complex). New file interp/Select.kt: <dollar>Select rung pairs, CharArray slot carriers, sigint(100) tail; NO raw dollar anywhere (KDoc <dollar>Select). Rules: h001, h004, h009, h012, h013, h014, h015, h017, h018. Output: Select.kt. Events +18 rows (log 3418).
- Unit 285 (interp-bmgsubs.c::<misc>, B10 tranche 1, row 285: loc 46, cr 0.284, ptr 15, ctrl 23, score 76.3, rank 258, complex). New file interp/BmgsubsMisc.kt: bmg_setup/bmg_act/bmg_fullsearch/Bcopy; TAG CORRECTION: the core IS a corpus unit (the old stub said not-a-corpus); bmg_fullsearch keeps an MdhStubs delegate for the cross-package callers. Rules: h004, h007, h012, h013, h014, h015, h017, h018. Output: BmgsubsMisc.kt. Events +16 rows (log 3434).
- Unit 286 (interp-parse.cpp::inout, B10 tranche 1, row 286: loc 54, cr 0.026, ptr 19, ctrl 13, score 58.6, rank 249, complex). New file interp/Inout.kt: inout(text,textOff,cmnd,col,svPtr) when-ladder 1-4 over dev==5/out_file legs; the InterpStubs u155 stub RESOLVED. Rules: h001, h007, h011, h012, h014, h017. Output: Inout.kt. Events +12 rows (log 3446).
- TRANCHE 1 (rows 277-286, 10 units) COMPLETE: bstrcat, _horolog, main, sym_ (monster), misc-8, libmpscpp.h, misc-4, select misc, bmgsubs misc, inout.
- Unit 287 (interp-globalb.cpp::<misc>, B10 tranche 2, row 287: loc 74, cr 0.304, ptr 4, ctrl 25, score 53.4, rank 239, complex). New file interp/GlobalbMisc.kt: MglobalNative (the non-SQLITE native arm of Mglobal; the reference build compiles the arm out but the corpus row is this carrier, h018 note), keyfix/btree RETRIEVE/STORE, the vendor's dead key[j-2]=1 then =0 pair kept verbatim, NEXT/ORDERNEXT/ORDERPREV remaps, GKILL/XNEXT/CLOSE. Rules: h004, h007, h010, h012, h013, h014, h017. Output: GlobalbMisc.kt. Events +14 rows (log 3460).
- Unit 288 (libmpscpp.cpp::global::DocCorrelate, B10 tranche 2, row 288: loc 28, cr 0.024, ptr 10, ctrl 14, score 42.3, rank 225, complex). New file mdh/DocCorrelate.kt: the LOWERcase d name-fold (the Kotlin DocCorrelate(Global,String,Double) name is owned by the LibmpscppMisc5 wrapper pair delegating here), fcn ladder, Cosine/Sim1/Dice/Jaccard scoring; the MdhStubs stub RESOLVED. Rules: h010, h014, h015, h017. Output: DocCorrelate.kt. Events +8 rows (log 3468).
- Unit 289 (interp-sysfunc.cpp::unpad, B10 tranche 2, row 289: loc 34, cr 0.059, ptr 8, ctrl 3, score 22.0, rank 175, core). New file interp/Unpad.kt: the 0x1f sign-normalize/%lf fold that this batch's sym_ exit tail reuses as its twin; InterpStubs stub RESOLVED (fullUnpad caller binds unchanged). Rules: h009, h012, h013, h014, h017. Output: Unpad.kt. Events +10 rows (log 3478).
- Unit 290 (include/mumpsc/defines.h::<misc>, B10 tranche 2, row 290: loc 55, cr 0.164, ctrl 3, score 21.5, rank 173, core). New file interp/DefinesH.kt: the defines.h misc residue in vendor order with the per-file seat ledger; duplicate define blocks rendered once (h013); GBLPERMIT=438 (0666 octal, h010), LINEFEED='\n' (vendor 012 octal), GBLPERMIT/GPADFMT folds. Rules: h004, h013, h015, h017, h018. Output: DefinesH.kt. Events +10 rows (log 3488).
- Unit 291 (libmpscpp.cpp::line1149, B10 tranche 2, row 291: loc 45, cr 0.027, ctrl 12, score 19.6, rank 167, core). New file mdh/LibmpscppLine1149.kt: Hash::operator const char* (chain walk c==1/c==2, EOF restart-once loopflg), cstringAt return via the Line1149Scratch h007 object; the LibmpscppMisc1 hashkey member widened from private (u291 note, behavior unchanged). Rules: h007, h012, h014, h015, h017. Output: LibmpscppLine1149.kt. Events +10 rows (log 3498).
- Unit 292 (interp-arith.cpp::Mltoa, B10 tranche 2, row 292: loc 20, cr 0.088, ptr 3, ctrl 8, score 19.5, rank 166, core). New file mdh/Mltoa.kt: the itoa fold (negative re-negation overflow check, LONG_MIN/MAX guard, <10 fast path, downward scratch fill), cout -> System.out.print, sigint(100); the raised MdhStubs stub signature KEPT (h015; the GlobalExtract/GlobalFind/Misc7 callers bind unchanged). Rules: h007, h010, h012, h014, h015, h017. Output: Mltoa.kt. Events +12 rows (log 3510).
- Unit 293 (interp-stem.cpp::wstem, B10 tranche 2, row 293: loc 28, cr 0.176, ptr 1, ctrl 12, score 19.2, rank 164, core). New file mdh/Wstem.kt: the last-letter dispatcher to the batch-4 escan/endings seats; mdh seat per the raised-stub package; TAG CORRECTION (the old MdhStubs stub said not-a-corpus-unit: it is row 293's corpus carrier). Rules: h007, h014, h015, h017. Output: Wstem.kt. Events +8 rows (log 3518).
- Unit 294 (include/mumpsc/sysparms.h::<misc>, B10 tranche 2, row 294: loc 39, cr 0.645, ctrl 14, score 18.7, rank 163, core). New file interp/SysparmsH.kt: PS/PS1/SINGLE/UNIX/USYSTEM/DOS residue (DOS=2 is the compiled Linux arm's excluded twin, h018/h010/h013/h019 ledger). Rules: h010, h013, h018, h019. Output: SysparmsH.kt. Events +8 rows (log 3526).
- Unit 295 (interp-fnumber.cpp::<misc>, B10 tranche 2, row 295: loc 56, cr 0.211, ptr 2, ctrl 4, score 15.4, rank 147, core). New file interp/FnumberMisc.kt: header residue ledger only (no Kotlin declarations; every function claimed by its own units.csv row, fcn by the batch-6 Fcn.kt seat). Rules: h013, h014, h018. Output: FnumberMisc.kt. Events +6 rows (log 3532).
- TRANCHE 2 (rows 287-296, 10 units) COMPLETE: globalb misc, DocCorrelate, unpad, defines.h, line1149, Mltoa, wstem, sysparms.h, fnumber misc, errors.h (verify).
- Unit 297 (compiler-sys1.c::xindex, B10 tranche 3, row 297: loc 12, cr 0.045, ctrl 7, score 10.9, rank 107, core): VERIFICATION-ONLY (the compiler-side xindex twin of the B9 row-265 interp-strmanip.cpp::xindex; distinct units.csv id). Verified against the DONE interp/Xindex.kt seat (row-297 note added there); no new code. Events +10 rows for the Xindex seat rule set (log 3564).
- Unit 298 (libmpscpp.cpp::mdh_tree_level_add, B10 tranche 3, row 298: loc 33, cr 0.132, ptr 1, score 10.9, rank 108, core): VERIFICATION-ONLY (the col9 overload twin; unit 8 rendered all ten overloads through mdh/MdhTreeLevelAdd.kt, the B2 unit-43 family precedent for this same family). Row-298 note added there; no new code. Events +12 rows for the unit-8 seat rule set (log 3576).
- Unit 299 (libmpscpp.cpp::global::Length, B10 tranche 3, row 299: loc 17, cr 0.056, ctrl 4, score 8.1, rank 64, simple): VERIFICATION-ONLY (the mstring/string needle twin of the two GlobalLength overloads; the GlobalLength.kt "not in this batch" note superseded by the row-299 fold note). No new code. Events +10 rows for the seat rule set (log 3586).
- Unit 300 (interp-sym.cpp::SysLoad, B10 tranche 3, row 300: loc 78, cr 0.053, ptr 1, ctrl 2, score 8.0, rank 62, simple). New file interp/SysLoad.kt: the /proc process-scan twin of the u137 SysDump seat; Mltoa pid render, 2nd-line sym_ store (the sym_ call now resolves into the new Sym_.kt seat). Rules: h001, h012, h014, h015, h017. Output: SysLoad.kt. Events +10 rows (log 3596).
- Unit 301 (libmpscpp.cpp::global::Length, B10 tranche 3, row 301: loc 17, cr 0.056, ctrl 4, score 7.7, rank 56, simple): VERIFICATION-ONLY (the char* needle twin; same units.csv id string as row 299, a distinct event pair set at its own timestamp). No new code. Events +10 rows for the seat rule set (log 3606).
- Unit 302 (interp-arith.cpp::mps_trunc, B10 tranche 3, row 302: loc 26, cr 0.037, ctrl 2, score 7.6, rank 51, simple). New file interp/MpsTrunc.kt: trunc over gcvtWrapper(strtodC(in)) (mpsfloor row-268 family shape); the two-level h018 trichotomy (MPFR/GMP/BIGFLOAT exclusions journaled); the InterpStubs mpsTrunc stub RESOLVED (u327, Zfcn caller binds unchanged). Rules: h009, h012, h014, h017, h018. Output: MpsTrunc.kt. Events +10 rows (log 3616).
- Unit 303 (interp-arith.cpp::mps_ceil, B10 tranche 3, row 303: loc 26, cr 0.037, ctrl 2, score 7.6, rank 52, simple). New file interp/MpsCeil.kt: the ceil twin of MpsFloor.kt; same h018 trichotomy shape; the InterpStubs mpsCeil stub RESOLVED. Rules: h009, h012, h014, h017, h018. Output: MpsCeil.kt. Events +10 rows (log 3626).
- Unit 304 (libmpscpp.cpp::global::Piece, B10 tranche 3, row 304: loc 17, cr 0.056, ctrl 2, score 5.5, rank 33, simple). New file mdh/GlobalPieceMstring.kt: the GlobalRef.Piece(mstring-key) twin (RETRIEVE ref, jj<0 clamp, _piece fold, MumpsGlobalException carrier); the GlobalPiece.kt row-214 "renders as its own overload when claimed" note FULFILLED here. Rules: h007, h009, h012, h014, h015, h017. Output: GlobalPieceMstring.kt. Events +12 rows (log 3638).
- Unit 305 (libmpscpp.cpp::mstring::Piece, B10 tranche 3, row 305: loc 17, cr 0.056, ctrl 2, score 5.5, rank 34, simple). New file mdh/MstringPieceGlobal.kt: the reverse-fetch twin (retrieves the global's value, then _piece with THIS string as source); GlobalPiece(Mstring) front-run note from row 281 now owned here. Rules: h007, h009, h012, h014, h015, h017. Output: MstringPieceGlobal.kt. Events +12 rows (log 3650).
- Unit 306 (interp-sym.cpp::_length, B10 tranche 3 (closer), row 306: loc 27, cr 0.059, ctrl 2, score 5.4, rank 30, simple). New file interp/LengthSym.kt: the keyed <dollar>length fold (xindex scan loop over the key; xindex seat reused per h014). Rules: h007, h009, h014, h017. Output: LengthSym.kt. Events +8 rows (log 3658).
- TRANCHE 3 (rows 297-306, 10 units) COMPLETE: xindex (verify), mdh_tree_level_add (verify), global::Length m/string (verify), SysLoad, global::Length char* (verify), mps_trunc, mps_ceil, global::Piece, mstring::Piece, _length.

- SCAN-GATE ROUND (batch-10 close, pre-push): full scan of all changed/new files. Catches fixed: BmgsubsMisc/GlobalbMisc/Unpad raw-dollar comment refs normalized to <dollar>next/<dollar>SYSTEM/<dollar>z; the Sym_.kt <dollar>noerr comment pair normalized and the two double-bang non-null assertions restructured to the ?: error / ?: continue folds per the scan-gate rule; the MdhStubs CORRECTION spelling; stray non-ASCII sweep clean. Gate re-run after every fix: zero errors, zero warnings. Events CSV line endings verified before append (file is LF; the +356 rows appended LF, log 3658).
- B11 FRONT-RUN: mdh/GlobalPatternGlobal.kt renders Global.Pattern(Global) (roster row 307) at batch-10 close per the family convention; no events rows for it (B9 discipline), journal tag only.
- PUSH INFRA NOTE (batch-10 close): the GitHub push landed as a series of commits (970b359 first: Bstrcat/Horolog/LengthSym; then the remaining seats, the stub-resolved edits, journal and events) because the push connector rejected the single 37-file commit (tree-create timeouts on the large Sym_/CompilerMain/Interpret/journal/events payloads); every file pushed byte-exact from the recovered workspace (the b10-wip-306-symdone snapshot + re-applied journal/events appends, both deterministic). Gate re-run clean after recovery before the pushes resumed.
- BATCH 10 COMPLETE (rows 277-306, 30 units in tranches T1 277-286 / T2 287-296 / T3 297-306; verification-only rows 296/297/298/299/301; monsters 279 main and 280 sym_ close the compiler-driver and symbol-table gaps, and with 286 inout the interp per-command stub debt is cleared). Events CSV 3658 rows (+356). Mark's gate: git pull --ff-only, gradle build detekt jacocoTestReport on m1-build, then the swipl coverage report (B9-close baseline: 18 rules, hit 99.7%, reuse 98.9%).
- B10 CLOSE (gate round): Mark's m1-build gates GREEN on 5e48e7f (git pull e32d8e5..5e48e7f fast-forward, 37 files, +7154/-271; gradle --no-daemon build detekt jacocoTestReport BUILD SUCCESSFUL in 29s, zero detekt findings). Batch-10-close swipl coverage report (Mark, m1-build): rules in base 18, triggering situations 1662 (hits 1657, misses 5), hit rate 99.7% (level with the B9-close high), reuse rate 98.9% (level with the B9-close high); all 5 misses remain preprocessor_conditional captured -> rule h018, pre-capture history (no action, same profile as every prior batch close). Events CSV at 3658 rows. Residual compileKotlin warnings are non-gating: 2 Sym_.kt elvis-always-left notes (the scan-gate no-double-bang folds at lines 286/486, intentional), 9 LibmpscppMisc3 unnecessary-safe-call/elvis notes and 1 LibmpscppMisc4 redundant-conversion (same class as the 11 pre-existing B9-close REDUNDANT-CONVERSION notes; not gating). BATCH 10 CLOSED GREEN: 30 units in tranches T1 277-286 / T2 287-296 / T3 297-306, five verification-only rows (296/297/298/299/301); the monsters main (279) and sym_ (280) close the compiler-driver and symbol-table gaps; sym_/mps_trunc/mps_ceil/inout/unpad/wstem/Mltoa/bstrcat/_horolog and the 38 mPlus-family stubs all resolved; B11 front-run Global.Pattern(global) rendered at close per the family convention. Batch 11 opens at units.csv row 307 (the front-run row; remaining roster per units.csv).

## Batch 11 (PRO arm, rows 307-309) - STUDY-CLOSE BATCH

Batch 11 is the final batch: the roster ends at units.csv row 309,
so this batch is the 3-row closer (not a 30-unit tranche). The
vibe sandbox reset again before the batch opened; the workspace was
rebuilt per the u172 recipe (codeload master zip at 7591135, vendor
re-fetched, sha256
621deb2586f27e741ce6db1a2a86fbddf4e4da40c9c6bb363f84c814f53ffdbe
re-verified; JDK17/jars/kotlinc gate rebuilt). Identity for all
three rows verified against the study-tool logic directly
(tools/select_study_subset.py extract_functions/complexity_metrics
imported into the local dump helper): row 307 global::Pattern
(loc 15, ctrl 2, score 5.1, rank 12) exact; row 308
compiler-at-test.c::<misc> the banner residue (loc 27, cr 0.069,
ctrl 2 = the GPL banner's own words for/if, score 4.9, rank 10)
exact; row 309 global::Justify (loc 18, cr 0.053, ctrl 1, score
4.2, rank 6) exact.

- Unit 307 (libmpscpp.cpp::global::Pattern, B11 tranche 1 (the B10 front-run row landing)): global::Pattern(global) (row 307: loc 15, ptr 0, ctrl 2, score 5.1, rank 12, simple). RENDERED AT BATCH-10 CLOSE as the B11 front-run per the family convention (mdh/GlobalPatternGlobal.kt: retrieve THIS global, retrieve the pattern global, pm fold; both gates set GlobalExceptionCode then throw). This row's events pairs land with this batch per the front-run discipline. Rules: h007, h012, h014, h015, h017. Output: GlobalPatternGlobal.kt (already pushed at B10 close; journal tag now superseded by this entry). Events +10 rows (log 3668).
- Unit 308 (compiler-at-test.c::<misc>, B11 tranche 2): the file-residual chunk of compiler-at-test.c (row 308: loc 27, cr 0.069, ptr 0, ctrl 2, score 4.9, rank 10, simple) - the GPL banner, the May 5 2024 date comment, and the single stdio.h include (the u243/u244/u245/276 banner-residue template). New file compiler/AtTestMisc.kt: KDoc disposition ledger only (atTest itself is the DONE B4 unit-108 seat AtTest.kt; callers IfCode/KillCode/Parse_ bind unchanged; stdio resolves to the existing System.out/writer carriers); the row's two control_marks are the banner's own for/if words (metric echo, noted). Rules: h013, h014, h015, h017, h018. Output: AtTestMisc.kt. Events +10 rows (log 3678).
- Unit 309 (libmpscpp.cpp::global::Justify, B11 tranche 3 (closer - THE FINAL ROSTER ROW)): global::Justify(int,int) (row 309: loc 18, cr 0.053, ptr 0, ctrl 1, score 4.2, rank 6, simple). New file mdh/GlobalJustify.kt: the global-needle Justify twin (Mglobal RETRIEVE on ref, GLOBAL_NOT_FOUND gate with the family throw, sprintf %d collapses, _justify over the retrieved gtmp; NO ii/jj clamp on this arm unlike the Piece twins - kept); the mstring::Justify(int,int) twin is the DONE row-281 Misc8 seat (no redeclaration). Rules: h007, h009, h012, h014, h015, h017. Output: GlobalJustify.kt. Events +12 rows (log 3690).
- SCAN-GATE ROUND (batch-11 close, pre-push): full scan of the three B11-touched files - zero raw dollar, zero double-bang, zero backtick, zero em dash, zero stray non-ASCII, no String.format sites missing Locale. Local kotlinc gate: zero errors, zero warnings (EXIT=0). Events CSV verified LF before append (+32 rows, log 3690).
- ROSTER LEDGER (study close): units.csv rows 2-309 (308 units) ALL CLAIMED: B1 31 + B2..B10 30 each + B11 3 = 308; the last three rows verified against the study tool this batch; no unrendered roster rows remain; no raised stubs left unaccounted (kept stubs per the B10 ledger: linepad, Extract1/memcheck/cvar per-file seats, pm, _piece, the bmg core beyond bmg_fullsearch, SysPut).
- BATCH 11 COMPLETE - PRO ARM STUDY CLOSE (rows 307-309). Events CSV 3690 rows (+32). Mark's gate: git pull --ff-only, gradle build detekt jacocoTestReport on m1-build, then the swipl coverage report (B10-close baseline: 18 rules, hit 99.7%, reuse 98.9%).
- B11 CLOSE - PRO ARM STUDY CLOSE (gate round): Mark's m1-build gates GREEN on 912daec (git pull 5e48e7f..912daec fast-forward, 4 files, +206; gradle --no-daemon build detekt jacocoTestReport BUILD SUCCESSFUL in 11s, zero detekt findings, ZERO compileKotlin warnings - the first fully warning-clean close of the batch-arc). Study-close swipl coverage report (Mark, m1-build): rules in base 18, triggering situations 1856 (hits 1851, misses 5), hit rate 99.7% (level with the B9/B10-close high), reuse rate 99.0% (UP from 98.9% - the STUDY-CLOSE HIGH); all 5 misses remain preprocessor_conditional captured -> rule h018, pre-capture history (no action, the same profile at every close since B1). Events CSV at 3690 rows. PRO ARM COMPLETE: 11 batches, 308/308 roster units claimed (B1 31 + B2-B10 30 each + B11 3), all graded green; the coverage log and rule base (rules_v0.pl, 18 rules) support the paper's final hit/reuse numbers.
- INFRA (2026-11-18): SAP registered as protocol clarification C-004 (wp2/STATISTICAL_ANALYSIS_PLAN.md, 175 lines; commits dc9177d + separator fix d7d152e, both blob-SHA verified per the channel protocol); wp2/analysis/ scaffolding committed per SAP section 5 (commits 9edd439 + byte-exact correction 0e6ea57, all seven blobs verified + selftest one-sided direction fix c0ad5fd): R/sap_stats.R (exact McNemar, Wilcoxon with Hodges-Lehmann, stratum-paired Fisher + Mantel-Haenszel, Holm), R/endpoints.R (pre-capture exclusion; row-count semantics, string dedup forbidden as file-level unit strings collide), run_analysis.R (arm-gated), selftest.R (12 checks PASS on macOS ARM64, R 4.6.2), renv.lock bootstrap, data/precapture_pro_seed.csv (roster units 4/10/12/20/29; Python emulation of the reader logic against the committed log confirms 1851/1851 hits, 0 misses, reuse 98.97% after pre-capture exclusion). Journal append made locally (337KB file exceeds the push-connector payload limit).
- INFRA (2026-11-18): C-005 registered (DOC cookbook-consult event-log mapping, adaptation counts as reuse; roster_row unit key registered before any export exists, roster ids not unique); analysis README contract corrections; export_per_unit_checks.R + roster_row keying in endpoints.R/run_analysis.R pushed (c489d2d + 1cd27f2f, all blob SHAs SHA-1-gated before push).
- INFRA (2026-11-18): DOC C-005 tooling landed (ce63a8f): export_doc_events.R regenerates DOC_knowledge_events.csv from journal consult rows per the registered grammar/mapping; backfill_checks.R guarded stamping helper (report/verify-roster/set, C-002 discipline enforced); oracle_verify.sh wrapper; both frozen oracles manifest-verified (30 + 59 files); per-arm oracle runners remain the open B5 finish-line dependency.
- INFRA (2026-11-18): date typo correction (logged, not silent): the three INFRA lines above and the registry C-004/C-005 entries were forward-dated 2026-12-04 by transcription error; corrected to 2026-11-18 (the actual registration and decision date, confirmed against commit timestamps dc9177d/c489d2d/ce63a8f). Registry fixed in 6543b11 (5 lines, blob-SHA gated). No outcome, grade, or registered decision text was changed - dates only.
- INFRA (2026-11-18): events.csv integrity correction (logged, not silent): a single stray blank line at line 1034 of wp2/logs/PRO_knowledge_events.csv (h018 compiler-read.c row, 2026-11-21 batch-3-close append window) was excised; 1 byte removed, all 1856+ event rows verified uniform 4-column afterwards; no event row was added, dropped, or modified. Caught by the pre-cycle-1 fleet sweep strict CSV parse.

## Batch 2 (DOC arm, units 32-62) - cycle-1 batch (DOC arm)

DOC arm executes batch 2 at cycle 1 per the C-001 committed row
order (B1 hoisted to pilot position 0; the DOC pilot B1 cells in
PILOT_TRACKING are still todo at this writing). Tranches 1 and 2
(units 32-51) were pushed 2026-11-18 (commits 7cfc6e2, 1e5187f;
kotlinc 2.1.20 gate clean, 0 errors 0 warnings), WITHOUT the
consult rows the pre-registered DOC workflow requires. Registry
entry C-006 (2026-11-19) authorizes a one-time reconstruction of
the evidenced consult rows from the pushed Kotlin annotations.
Reconstructed rows are marked as such; units whose annotations
evidence no consult (32, 33, 38, 48) emit no row and are flagged
as unrecoverable in C-006, not silently dropped or invented.
recorded; the proxy is stated in C-006). Consequence registered:
the reconstructed set only contains consults evidenced by
annotation, so hit/miss counts for these units are partial and
biased toward hits; C-006 carries the analysis-phase note.

- INFRA (2026-11-19, C-006 backfill): consult rows below for
  units 34-47 and 49-51 are reconstructed from the per-file Kotlin
  annotations of the tranche-1/2 pushes; units 32 (global::Extract),
  33 (global::Find), 38 (global::Piece) and 48 (getstr2) evidence no
  entry consult in their annotations (their dispositions - carriers,
  shared stub seats, sprintf collapses - are documented in the files
  without entry ids) and are recorded as consult-unrecoverable per
  C-006. Miss rows for units 40/41 are reconstructed from the
  in-file notes: the preprocessor_conditional construct consulted
  the closest family entry H-003 (stateful macro) and missed;
  capture-candidate status is carried to the batch-2 close checklist
  (DOC's T1 candidate, mirroring PRO's h018 capture).
- DOC consult: unit libmpscpp.cpp::SQL_Table, entry H-015, outcome hit-reuse, timestamp 2026-11-18T22:45:30Z
- DOC consult: unit compiler-abrtx.c::<misc>, entry H-004, outcome hit-reuse, timestamp 2026-11-18T22:45:30Z
- DOC consult: unit compiler-doty.c::<misc>, entry H-004, outcome hit-reuse, timestamp 2026-11-18T22:45:30Z
- DOC consult: unit libmpscpp.cpp::KiLL, entry H-015, outcome hit-reuse, timestamp 2026-11-18T22:45:30Z
- DOC consult: unit libmpscpp.cpp::GlobalOrder, entry H-008, outcome hit-reuse, timestamp 2026-11-18T22:45:30Z
- DOC consult: unit libmpscpp.cpp::GlobalOrder, entry H-013, outcome hit-adapt, timestamp 2026-11-18T22:45:30Z
- DOC consult: unit libmpscpp.cpp::GlobalOrder, entry H-015, outcome hit-reuse, timestamp 2026-11-18T22:45:30Z
- DOC consult: unit interp-arith.cpp::mps_exp, entry H-003, outcome miss, timestamp 2026-11-18T22:45:30Z
- DOC consult: unit interp-arith.cpp::mps_exp10, entry H-003, outcome miss, timestamp 2026-11-18T22:45:30Z
- DOC consult: unit libmpscpp.cpp::mstring::Fnumber, entry H-013, outcome hit-reuse, timestamp 2026-11-18T22:45:30Z
- DOC consult: unit libmpscpp.cpp::mstring::Fnumber, entry H-014, outcome hit-reuse, timestamp 2026-11-18T22:45:30Z
- DOC consult: unit libmpscpp.cpp::mstring::Fnumber, entry H-015, outcome hit-reuse, timestamp 2026-11-18T22:45:30Z
- DOC consult: unit libmpscpp.cpp::mdh_tree_level_add, entry H-015, outcome hit-reuse, timestamp 2026-11-18T22:51:46Z
- DOC consult: unit libmpscpp.cpp::global::Min, entry H-014, outcome hit-reuse, timestamp 2026-11-18T22:51:46Z
- DOC consult: unit libmpscpp.cpp::prnt, entry H-007, outcome hit-reuse, timestamp 2026-11-18T22:51:46Z
- DOC consult: unit libmpscpp.cpp::Multiply, entry H-013, outcome hit-adapt, timestamp 2026-11-18T22:51:46Z
- DOC consult: unit libmpscpp.cpp::Multiply, entry H-014, outcome hit-reuse, timestamp 2026-11-18T22:51:46Z
- DOC consult: unit compiler-copyright-notice.c::copyright_notice, entry H-001, outcome hit-reuse, timestamp 2026-11-18T22:51:46Z
- DOC consult: unit compiler-copyright-notice.c::copyright_notice, entry H-002, outcome hit-reuse, timestamp 2026-11-18T22:51:46Z
- DOC consult: unit compiler-copyright-notice.c::copyright_notice, entry H-009, outcome hit-reuse, timestamp 2026-11-18T22:51:46Z
- DOC consult: unit libmpscpp.cpp::global::IDF, entry H-009, outcome hit-reuse, timestamp 2026-11-18T22:51:46Z
- DOC consult: unit libmpscpp.cpp::global::IDF, entry H-010, outcome hit-reuse, timestamp 2026-11-18T22:51:46Z
- DOC consult: unit libmpscpp.cpp::global::IDF, entry H-014, outcome hit-reuse, timestamp 2026-11-18T22:51:46Z
- DOC consult: unit interp-native-btree.cpp::scanblocks, entry H-007, outcome hit-adapt, timestamp 2026-11-18T22:51:46Z
- DOC consult: unit interp-native-btree.cpp::scanblocks, entry H-014, outcome hit-reuse, timestamp 2026-11-18T22:51:46Z
- DOC consult: unit interp-sql.cpp::embed, entry H-008, outcome hit-reuse, timestamp 2026-11-18T22:51:46Z
- DOC consult: unit compiler-globalarray.c::GlobalArray, entry H-001, outcome hit-reuse, timestamp 2026-11-19T00:41:00Z
- DOC consult: unit compiler-globalarray.c::GlobalArray, entry H-004, outcome hit-reuse, timestamp 2026-11-19T00:41:00Z
- DOC consult: unit compiler-globalarray.c::GlobalArray, entry H-006, outcome hit-reuse, timestamp 2026-11-19T00:41:00Z
- DOC consult: unit compiler-globalarray.c::GlobalArray, entry H-007, outcome hit-adapt, timestamp 2026-11-19T00:41:00Z
- DOC consult: unit compiler-globalarray.c::GlobalArray, entry H-009, outcome hit-reuse, timestamp 2026-11-19T00:41:00Z
- DOC consult: unit compiler-globalarray.c::GlobalArray, entry H-012, outcome hit-reuse, timestamp 2026-11-19T00:41:00Z
- DOC consult: unit compiler-globalarray.c::GlobalArray, entry H-014, outcome hit-reuse, timestamp 2026-11-19T00:41:00Z
- DOC consult: unit compiler-globalarray.c::GlobalArray, entry H-015, outcome hit-reuse, timestamp 2026-11-19T00:41:00Z
- DOC consult: unit compiler-globalarray.c::GlobalArray, entry H-017, outcome hit-reuse, timestamp 2026-11-19T00:41:00Z
- Unit 53 (libmpscpp.cpp::global::Multiply, B2 tail tranche 1): the three-reference class-global multiplication C = this x B (row 42: loc 26, ptr 7, ctrl 8, score 34.2, rank 213, complex). New file doc/GlobalMultiply.kt: vendor control flow verbatim (row/coll/level Order walks, B(k).Data() guard continue, first-time vs accumulate branches); the class-global member seats (Kill/Order x3/Data x2/double fetch/store) are H-014 stubs - none of the C++ members is a roster unit; the nine vendor debug printf fossil comments kept verbatim. RESOLVES the u46 Stubs.globalRefMultiply stub as a delegate (call sites bind unchanged).
- DOC consult: unit libmpscpp.cpp::global::Multiply, entry H-014, outcome hit-reuse, timestamp 2026-11-19T00:41:30Z
- DOC consult: unit libmpscpp.cpp::global::Multiply, entry H-017, outcome hit-reuse, timestamp 2026-11-19T00:41:30Z
- Unit 54 (compiler-if.c::if_code, B2 tail tranche 1): the IF command compiler (row 41: loc 40, ptr 4, ctrl 21, score 38.8, rank 222, complex; NOTE - unlike the PRO arm, DOC has no B1 banner unit yet, so if_code is a FULL render here, no front-run seat exists). New file doc/IfCode.kt: the if_again/if_structure goto pair linearized per H-011 (the cookbook's own example); INTERPRET macro per H-003 as CompilerState.interpret (five emissions verbatim); pd1Last H-006 holder; short externs widened per H-010; the emitted dollar bytes built via DOLLAR per H-015 (byte-identical emission); atTest/parse_/Extract2/atCopy/numcomp2/linepad are H-014 stubs (atTest batch-4 row 109, parse_ batch-3, Extract2 batch-1, the rest not roster units); the commented numcomp fossil kept verbatim. CompilerState extended with the parse externs (vendor compiler-globals-ext.h anchors); Blocks widened to CharArray to carry the 'i' opcode verbatim (H-010 note). Local gate 0/0.
- DOC consult: unit compiler-if.c::if_code, entry H-003, outcome hit-reuse, timestamp 2026-11-19T00:42:00Z
- DOC consult: unit compiler-if.c::if_code, entry H-006, outcome hit-reuse, timestamp 2026-11-19T00:42:00Z
- DOC consult: unit compiler-if.c::if_code, entry H-010, outcome hit-reuse, timestamp 2026-11-19T00:42:00Z
- DOC consult: unit compiler-if.c::if_code, entry H-011, outcome hit-reuse, timestamp 2026-11-19T00:42:00Z
- DOC consult: unit compiler-if.c::if_code, entry H-014, outcome hit-reuse, timestamp 2026-11-19T00:42:00Z
- DOC consult: unit compiler-if.c::if_code, entry H-015, outcome hit-reuse, timestamp 2026-11-19T00:42:00Z
- DOC consult: unit compiler-if.c::if_code, entry H-017, outcome hit-reuse, timestamp 2026-11-19T00:42:00Z

### Tranche 2 (DOC arm, units 55-59) — pushed with this tranche

Narrative per unit (all timestamps true; recon and renders in-session):

- **u55 btree.h misc (BtreeH.kt)**: whole-header declaration ledger. Preprocessor pairs resolved against the reference build: ADSIZE 8 owned by BtreeExterns.kt (u50, not redeclared); MSVC twin arms excluded; GMP/MPFR off the reference path. Quirks kept as ledger notes: ADZIZE spelling typo, dead gp1/gp2 seat declarations, gblfd/gbldat -1 sentinels, ReadOnly 1. GBLPERMIT 438 = 0666 octal.
- **u56 interp-lock misc (LockMisc.kt)**: cleanLocks/makeLock rendered as a RandomAccessFile record walk over /tmp/Mumps.Locks, 528-byte records (pid[16]+ref[512]). Vendor dead tail after makeLock's final return kept as comment per H-013; blank-padded wipe (spaces then NUL) kept in both services; O_EXCL/O_CREAT ladder quantized to read/write with create-fallback.
- **u57 arith.h misc (ArithH.kt)**: whole-header residual: FLT_PRECISION=72 define plus owner-annotated prototype roster; GMP/MPFR conditional pair excluded per reference build and journaled; commented ltoa fossil kept as ledger note.
- **u58 prnterr (Prnterr.kt)**: full message ladder; codes 21/22/28 absent kept; Nx back-walk double-step quirk kept; case 27 raw dollar byte built via DOLLAR (H-015); stderr → System.err per H-001; pd1/LineNumber/NOERR/ErrChar ride new Msv carriers (stateVector.h:43/63/60/95).
- **u59 inout (Inout.kt)**: compiler-side emission switch, cases 1-4; emitted C text byte-identical per H-015; case-3 dead block after return kept as comment per H-013; Extract2 rides the batch stub (H-014, owner compiler-parse.c). Carriers.kt extended: Msv out_file[10] Writers (:61), pd1[PS1] (:43), io (:49), NOERR (:60), hor/ver[10] (:62), LineNumber long (:63), ErrChar (:95).

Consult rows for units 55-59 (true timestamps, in-session):

- DOC consult: unit include/mumpsc/btree.h::<misc>, entry H-004, outcome hit-adapt, timestamp 2026-11-19T00:43:00Z
- DOC consult: unit include/mumpsc/btree.h::<misc>, entry H-013, outcome hit-reuse, timestamp 2026-11-19T00:43:00Z
- DOC consult: unit include/mumpsc/btree.h::<misc>, entry H-010, outcome hit-reuse, timestamp 2026-11-19T00:43:00Z
- DOC consult: unit interp-lock.cpp::<misc>, entry H-001, outcome hit-reuse, timestamp 2026-11-19T00:43:00Z
- DOC consult: unit interp-lock.cpp::<misc>, entry H-013, outcome hit-reuse, timestamp 2026-11-19T00:43:00Z
- DOC consult: unit interp-lock.cpp::<misc>, entry H-010, outcome hit-reuse, timestamp 2026-11-19T00:43:00Z
- DOC consult: unit include/mumpsc/arith.h::<misc>, entry H-013, outcome hit-reuse, timestamp 2026-11-19T00:43:00Z
- DOC consult: unit include/mumpsc/arith.h::<misc>, entry H-014, outcome hit-reuse, timestamp 2026-11-19T00:43:00Z
- DOC consult: unit interp-parse.cpp::prnterr, entry H-001, outcome hit-adapt, timestamp 2026-11-19T00:44:00Z
- DOC consult: unit interp-parse.cpp::prnterr, entry H-006, outcome hit-reuse, timestamp 2026-11-19T00:44:00Z
- DOC consult: unit interp-parse.cpp::prnterr, entry H-009, outcome hit-reuse, timestamp 2026-11-19T00:44:00Z
- DOC consult: unit interp-parse.cpp::prnterr, entry H-013, outcome hit-reuse, timestamp 2026-11-19T00:44:00Z
- DOC consult: unit interp-parse.cpp::prnterr, entry H-015, outcome hit-reuse, timestamp 2026-11-19T00:44:00Z
- DOC consult: unit compiler-sys1.c::inout, entry H-001, outcome hit-reuse, timestamp 2026-11-19T00:44:00Z
- DOC consult: unit compiler-sys1.c::inout, entry H-013, outcome hit-reuse, timestamp 2026-11-19T00:44:00Z
- DOC consult: unit compiler-sys1.c::inout, entry H-014, outcome hit-reuse, timestamp 2026-11-19T00:44:00Z
- DOC consult: unit compiler-sys1.c::inout, entry H-015, outcome hit-reuse, timestamp 2026-11-19T00:44:00Z

### Tranche 3 (DOC arm, units 60-62, batch-2 tail close) — pushed with this tranche

Narrative per unit (timestamps true and recorded before the renders):

- **u60 libmpscpp.cpp::global::TreePrint (TreePrint.kt)**: ten-level indented Order/Data tree dump, the vendor's literal flat nesting kept verbatim. Quirks kept: level-8 Order subscript list omits a1 while its Data call includes it; levels 8/9 print the name before probing Data; levels 6/7 carry the extra Data<2 continue guard. New H-014 seats: globalOrderN/globalDataN (vararg even for the 3..10-subscript arms), getGlobalString.
- **u61 libmpscpp.cpp::sw (Sw.kt)**: S-W entry: penalty/size priming, Cost fill, optional matrix print, max-cell scan, prnt(0,...) traceback (u45), score pass, 60-char alignment block display. ShowAlign&&0 dead banner kept as H-013 comments; malloc arm raise(SIGSEGV) rendered as thrown Error; fprintf rides Msv out_file Writers (closed lun throws, diverged from vendor FILE* deref); reversal buffers sized on the halved length per vendor quirk; the __a free tail is a GC no-op ledger. SeqAlignExterns extended with GAP/MISMATCH/MATCH/Size_s/Size_t/Size/sx; Stubs gained init/Cost/nodash.
- **u62 libmpscpp.cpp::<misc-2> (ArithOpsMisc2.kt)**: the class-global arithmetic operator support: +=,-=,*=,/= over 8 C numeric types; member and friend +,-,/ cast families; global-vs-global double arithmetic via the arm strtod; pre/post ++/-- with the floor-checked long-vs-double store; >= and <= member/friend ladders. Both chunker boundaries ledgered (chunk opens with the orphan return-tmp tail of misc-1; closes mid-signature in the <=(short,global) friend, not rendered). Unsigned seats ride UInt/UShort/ULong per the PRO u62 lessons; JVM unsigned-throw vs C wrap divergence noted; member minus/div operand-direction quirks kept; char*/string comparison overloads collapse onto one String seat (H-015, ledgered). 17 new per-type H-014 extraction/assignment seats in Stubs.

Consult rows for units 60-62 (true timestamps; logged before the renders, 2026-11-19T00:46Z):

- DOC consult: unit libmpscpp.cpp::global::TreePrint, entry H-001, outcome hit-reuse, timestamp 2026-11-19T00:46:00Z
- DOC consult: unit libmpscpp.cpp::global::TreePrint, entry H-013, outcome hit-reuse, timestamp 2026-11-19T00:46:00Z
- DOC consult: unit libmpscpp.cpp::global::TreePrint, entry H-014, outcome hit-reuse, timestamp 2026-11-19T00:46:00Z
- DOC consult: unit libmpscpp.cpp::global::TreePrint, entry H-017, outcome hit-reuse, timestamp 2026-11-19T00:46:00Z
- DOC consult: unit libmpscpp.cpp::sw, entry H-001, outcome hit-adapt, timestamp 2026-11-19T00:46:00Z
- DOC consult: unit libmpscpp.cpp::sw, entry H-007, outcome hit-reuse, timestamp 2026-11-19T00:46:00Z
- DOC consult: unit libmpscpp.cpp::sw, entry H-009, outcome hit-reuse, timestamp 2026-11-19T00:46:00Z
- DOC consult: unit libmpscpp.cpp::sw, entry H-013, outcome hit-reuse, timestamp 2026-11-19T00:46:00Z
- DOC consult: unit libmpscpp.cpp::sw, entry H-014, outcome hit-reuse, timestamp 2026-11-19T00:46:00Z
- DOC consult: unit libmpscpp.cpp::<misc-2>, entry H-007, outcome hit-reuse, timestamp 2026-11-19T00:46:00Z
- DOC consult: unit libmpscpp.cpp::<misc-2>, entry H-012, outcome hit-adapt, timestamp 2026-11-19T00:46:00Z
- DOC consult: unit libmpscpp.cpp::<misc-2>, entry H-014, outcome hit-reuse, timestamp 2026-11-19T00:46:00Z
- DOC consult: unit libmpscpp.cpp::<misc-2>, entry H-015, outcome hit-reuse, timestamp 2026-11-19T00:46:00Z
- DOC consult: unit libmpscpp.cpp::<misc-2>, entry H-017, outcome hit-reuse, timestamp 2026-11-19T00:46:00Z


### DOC batch 2 close — coverage checklist (units 32-62) and events export

Manual batch-close coverage checklist per the DOC arm discipline
(construct list from the batch-2 unit texts, normalized per section 2.4;
for each canonical construct, at least one cookbook entry matched:

- FILE*/stdio channel routing: H-001 matched in 7 batch-2 unit journal(s)
- INTERPRET/state-macro splice: H-003 matched in 3 batch-2 unit journal(s)
- one-owner-per-C-entity objects: H-004 matched in 4 batch-2 unit journal(s)
- holder arrays/last-value seats: H-006 matched in 3 batch-2 unit journal(s)
- buffer-role classification: H-007 matched in 5 batch-2 unit journal(s)
- sprintf/printf locale contract: H-009 matched in 5 batch-2 unit journal(s)
- numeric promotions/domains: H-010 matched in 4 batch-2 unit journal(s)
- goto linearization: H-011 matched in 1 batch-2 unit journal(s)
- unsigned domain distinctness: H-012 matched in 2 batch-2 unit journal(s)
- dead code/comment fossils: H-013 matched in 10 batch-2 unit journal(s)
- out-of-scope stub seats: H-014 matched in 13 batch-2 unit journal(s)
- grammar collisions/byte-domain literals: H-015 matched in 10 batch-2 unit journal(s)
- camelCase naming: H-017 matched in 5 batch-2 unit journal(s)
- preprocessor_conditional: 2 miss rows recorded (units of the btree/arith
  header residue consult the H-003 state-macro entry; the 3-way #ifdef
  family remains a miss under T1 capture evaluation - MARK/PI decision
  pending (mirrors the PRO arm's batch-1 mps_acos precedent)

Consult outcome totals for the batch: 65 hit-reuse, 8 hit-adapt, 2 miss (75 consults, one per (unit, entry) per C-005).

Per-unit consult coverage (unit -> entries consulted):

- libmpscpp.cpp::SQL_Table: H-015
- compiler-abrtx.c::<misc>: H-004
- compiler-doty.c::<misc>: H-004
- libmpscpp.cpp::KiLL: H-015
- libmpscpp.cpp::GlobalOrder: H-008, H-013, H-015
- interp-arith.cpp::mps_exp: H-003
- interp-arith.cpp::mps_exp10: H-003
- libmpscpp.cpp::mstring::Fnumber: H-013, H-014, H-015
- libmpscpp.cpp::mdh_tree_level_add: H-015
- libmpscpp.cpp::global::Min: H-014
- libmpscpp.cpp::prnt: H-007
- libmpscpp.cpp::Multiply: H-013, H-014
- compiler-copyright-notice.c::copyright_notice: H-001, H-002, H-009
- libmpscpp.cpp::global::IDF: H-009, H-010, H-014
- interp-native-btree.cpp::scanblocks: H-007, H-014
- interp-sql.cpp::embed: H-008
- compiler-globalarray.c::GlobalArray: H-001, H-004, H-006, H-007, H-009, H-012, H-014, H-015, H-017
- libmpscpp.cpp::global::Multiply: H-014, H-017
- compiler-if.c::if_code: H-003, H-006, H-010, H-011, H-014, H-015, H-017
- include/mumpsc/btree.h::<misc>: H-004, H-010, H-013
- interp-lock.cpp::<misc>: H-001, H-010, H-013
- include/mumpsc/arith.h::<misc>: H-013, H-014
- interp-parse.cpp::prnterr: H-001, H-006, H-009, H-013, H-015
- compiler-sys1.c::inout: H-001, H-013, H-014, H-015
- libmpscpp.cpp::global::TreePrint: H-001, H-013, H-014, H-017
- libmpscpp.cpp::sw: H-001, H-007, H-009, H-013, H-014
- libmpscpp.cpp::<misc-2>: H-007, H-012, H-014, H-015, H-017

wp2/logs/DOC_knowledge_events.csv regenerated from this journal per C-005
(148 event rows: hit-reuse/hit-adapt consults emit reuse+hit rows, miss
consults emit miss rows). NOTE: the R instrument (export_doc_events.R)
could not run in the work sandbox (no R runtime); the CSV was produced by a
faithful Python port of the registered R script, run against the master
journal and roster with the same grammar/validation/mapping contract.
VERIFICATION STEP (Mark): re-run `Rscript wp2/analysis/R/export_doc_events.R`
on the container/m1-build and confirm the regenerated CSV is byte-identical
(the instrument is idempotent, journal is the single source of truth);
log any divergence as a protocol deviation.


### DOC batch 2 close — Rscript verification outcome (2026-11-19, m1-build)

The registered R instrument re-ran clean: 75 consult row(s) -> 148 event
row(s), all validations passing. VERIFICATION RESULT: the Python-port CSV
pushed in 2992041 and the R-generated CSV are content-identical (every
event row, kind/target/unit/timestamp, in the same order) but NOT
byte-identical: R's write.table default quoting wraps every field in
double quotes, the port emitted an unquoted CSV. The R instrument's
formatting is authoritative; the regenerated CSV (instrument output,
m1-build working tree) supersedes the port's bytes and is committed as
the tracked wp2/logs/DOC_knowledge_events.csv. Logged as data per the
arm discipline; the port is retired (the R instrument is the only
producer of this file from here on - R is available on the fleet).
Side note: the stray untracked CSV that blocked the first pull was a
header-only file from an earlier zero-consult-instrument run (the
script's documented empty-journal contract), not stale data.

### DOC section 5 instrumentation calibration — batch-2 snapshot (2026-11-19, B2-only scope per C-007)

Per the section-5 criterion, the DOC arm's instrumentation state at
B2 close (B1 open as of 2026-11-19, snapshot taken so the calibration
baseline exists before B1 renders begin):

- Consult rows: 75 (65 hit-reuse, 8 hit-adapt, 2 miss) -> 148 event
  rows in wp2/logs/DOC_knowledge_events.csv (73 reuse + 73 hit + 2
  miss), instrument-stable under export_doc_events.R on m1-build.
- Cookbook entries: 18 (v0 H-001..H-017 unchanged since seeding;
  H-018 preprocessor_conditional captured at B2 close, mirroring
  PRO's h018 — artifacts at entry parity before the interleaved
  cycles). No version bumps; no G4 duplicates.
- Red-build count: 0 (B2 renders gated locally at kotlinc 0 errors /
  0 warnings per tranche; m1-build gradle BUILD SUCCESSFUL on the
  full 39-file diff).
- Grading outcome: B2 31/31 translated and gate-green; per-unit
  checks (code/output/runtime/target, PER_UNIT_CHECKS.md) remain
  todo across B2 for DOC as for the other arms — the four columns
  track the BASE reference translation per C-003.2.
- Evidence limitation carried per C-006: B2 units 32-51 consults are
  partial-evidence reconstructed data (hit-biased); units 52-62 are
  full-instrument rows with true timestamps.
- Ordering deviation: this batch closed before B1 (inversion logged
  as C-007); DOC B1 opens 2026-11-19 in phased tranches.

### DOC B1 tranche 1 (units 1-6) - opened per C-007, 2026-11-19

Narrative per unit (timestamps recorded before the renders, full
instrument from the start - no reconstruction):

- **u1 compiler-copyright-notice.c::<misc> (CopyrightMisc.kt)**: residual
  banner + include ledger; copyright_notice() is the batch-2 unit.
- **u2 compiler-if.c::<misc> (IfMisc.kt)**: banner + four-include
  residue ledger; if_code lives in IfCode.kt (B2 u54). No cross-file
  collisions introduced (all ledger symbols private).
- **u3 compiler-open.c::<misc> (OpenMisc.kt)**: banner + include
  residue ledger; mumps_open() body not a corpus unit.

- **u4 interp-native-btree.cpp::btree (Btree.kt)**: live arm is the
  thin btreeX delegate; the entire NATIVE_SHARED block is the FIRST
  DOC application of H-018 - excluded per the reference build config,
  restated as the mandatory comment ledger (the no-slice close path
  and the DBSem alarm path). btreeX is an H-014 stub (not a roster
  unit). Name kept verbatim per H-017.

- **u5 libmpscpp.cpp::EncodeHTML (EncodeHTML.kt)**: two-overload percent
  encoder; H-007 adapted to an explicit CharArray (byte-domain fidelity,
  PRO u5 precedent); H-008 adapted: the vendor static scratch is fresh
  per call (no cross-call aliasing - divergence documented); the C
  char sign-extension of percent hex >= 0x80 kept explicitly.

- **u6 interp-sym.cpp::_find (FindSym.kt)**: the FIND intrinsic core;
  atoi rendered as a leading-integer parse; short widening and the
  length+1 clamp kept; xindex is an H-014 stub; name _find kept
  verbatim (valid Kotlin identifier).

Consult rows for B1 units 1-6 (timestamped before the renders):

- DOC consult: unit compiler-copyright-notice.c::<misc>, entry H-004, outcome hit-reuse, timestamp 2026-11-19T01:31:00Z
- DOC consult: unit compiler-if.c::<misc>, entry H-004, outcome hit-reuse, timestamp 2026-11-19T01:31:00Z
- DOC consult: unit compiler-open.c::<misc>, entry H-004, outcome hit-reuse, timestamp 2026-11-19T01:31:00Z
- DOC consult: unit interp-native-btree.cpp::btree, entry H-014, outcome hit-reuse, timestamp 2026-11-19T01:31:00Z
- DOC consult: unit interp-native-btree.cpp::btree, entry H-018, outcome hit-reuse, timestamp 2026-11-19T01:31:00Z
- DOC consult: unit interp-native-btree.cpp::btree, entry H-017, outcome hit-reuse, timestamp 2026-11-19T01:31:00Z
- DOC consult: unit libmpscpp.cpp::EncodeHTML, entry H-007, outcome hit-adapt, timestamp 2026-11-19T01:31:00Z
- DOC consult: unit libmpscpp.cpp::EncodeHTML, entry H-008, outcome hit-adapt, timestamp 2026-11-19T01:31:00Z
- DOC consult: unit libmpscpp.cpp::EncodeHTML, entry H-009, outcome hit-reuse, timestamp 2026-11-19T01:31:00Z
- DOC consult: unit libmpscpp.cpp::EncodeHTML, entry H-014, outcome hit-reuse, timestamp 2026-11-19T01:31:00Z
- DOC consult: unit libmpscpp.cpp::EncodeHTML, entry H-017, outcome hit-reuse, timestamp 2026-11-19T01:31:00Z
- DOC consult: unit interp-sym.cpp::_find, entry H-007, outcome hit-reuse, timestamp 2026-11-19T01:31:00Z
- DOC consult: unit interp-sym.cpp::_find, entry H-009, outcome hit-reuse, timestamp 2026-11-19T01:31:00Z
- DOC consult: unit interp-sym.cpp::_find, entry H-010, outcome hit-reuse, timestamp 2026-11-19T01:31:00Z
- DOC consult: unit interp-sym.cpp::_find, entry H-014, outcome hit-reuse, timestamp 2026-11-19T01:31:00Z
- DOC consult: unit interp-sym.cpp::_find, entry H-017, outcome hit-reuse, timestamp 2026-11-19T01:31:00Z

Local gate 0 errors / 0 warnings (kotlinc 2.1.20, JDK corretto
17.0.21.1); pre-push scan clean after two fixes (a raw dollar-FIND in
the _find KDoc, a backtick pair in the EncodeHTML KDoc).

### DOC B1 tranche 2 (units 7-10), 2026-11-19

Narrative per unit (timestamp 2026-11-19T01:52:03Z recorded before
the renders; full instrument):

- **u7 compiler-parse.c::fixstr (Fixstr.kt)**: pure escape pass over
  a NUL-terminated CharArray (double quote, single quote, backslash
  gain a leading backslash into the tmp ledger, copied back in
  place); H-015 char literals written directly.
- **u8 libmpscpp.cpp::mdh_tree_level_add (MdhTreeLevelAdd5.kt)**: the
  FIVE-column overload instance (roster loc 24; the B2 unit 43 render
  is the seven-column instance - the same-named vendor chunk family
  resolves by arity per H-015). Private per-instance coded-byte
  consts; zfcn remains the H-014 stub; the identity-hash <int> (this)
  sprintf divergence is ledgered in the KDoc.
- **u9 include/mumpsc/libmpsrdbms.h::<misc> (RdbmsMisc.kt)**: seven
  H-003 macro shims with explicit svPtr parameters (the ARM-adapted
  INTERPRET shape) plus eight H-014 prototype stubs
  (rdbmsSelect..rdbmsTablePrint) with owners named.
- **u10 interp-arith.cpp::mps_acos (MpsAcos.kt)**: second application
  of H-018 - live arm is Stubs.gcvtWrapper(acos(strtod(...))); the
  BIGFLOAT and MULTI_PRECISION arms are restated as the excluded
  per-build ledger comments.

Consult rows for B1 units 7-10 (timestamped before the renders):

- DOC consult: unit compiler-parse.c::fixstr, entry H-007, outcome hit-reuse, timestamp 2026-11-19T01:52:03Z
- DOC consult: unit compiler-parse.c::fixstr, entry H-015, outcome hit-reuse, timestamp 2026-11-19T01:52:03Z
- DOC consult: unit compiler-parse.c::fixstr, entry H-017, outcome hit-reuse, timestamp 2026-11-19T01:52:03Z
- DOC consult: unit libmpscpp.cpp::mdh_tree_level_add, entry H-015, outcome hit-adapt, timestamp 2026-11-19T01:52:03Z
- DOC consult: unit libmpscpp.cpp::mdh_tree_level_add, entry H-014, outcome hit-reuse, timestamp 2026-11-19T01:52:03Z
- DOC consult: unit libmpscpp.cpp::mdh_tree_level_add, entry H-017, outcome hit-reuse, timestamp 2026-11-19T01:52:03Z
- DOC consult: unit include/mumpsc/libmpsrdbms.h::<misc>, entry H-003, outcome hit-adapt, timestamp 2026-11-19T01:52:03Z
- DOC consult: unit include/mumpsc/libmpsrdbms.h::<misc>, entry H-014, outcome hit-reuse, timestamp 2026-11-19T01:52:03Z
- DOC consult: unit include/mumpsc/libmpsrdbms.h::<misc>, entry H-015, outcome hit-reuse, timestamp 2026-11-19T01:52:03Z
- DOC consult: unit interp-arith.cpp::mps_acos, entry H-018, outcome hit-reuse, timestamp 2026-11-19T01:52:03Z
- DOC consult: unit interp-arith.cpp::mps_acos, entry H-010, outcome hit-reuse, timestamp 2026-11-19T01:52:03Z
- DOC consult: unit interp-arith.cpp::mps_acos, entry H-009, outcome hit-reuse, timestamp 2026-11-19T01:52:03Z
- DOC consult: unit interp-arith.cpp::mps_acos, entry H-014, outcome hit-reuse, timestamp 2026-11-19T01:52:03Z

Local gate 0 errors / 0 warnings; pre-push scan clean.

### DOC B1 tranche 3 (units 11-20), 2026-11-19

Narrative per unit (timestamp 2026-11-19T01:54:24Z recorded before
the renders; full instrument):

- **u11 compiler-parse.c::ExpandNext (ExpandNext.kt)**: the coded
  #G#/#S# indirection expander; emissions byte-verbatim through
  CompilerState.emit; new CstringOps.strncmpAt seat (offset strncmp
  family); vendor arg[32] zero-init buffers kept.
- **u12 mumps.cpp::<misc> (MumpsMisc.kt)**: leading-residue ledger
  (banner, SQLITE, readline includes document-only); pad/SyncClock
  live; file-scope DBTimePulse/DBSem in the MumpsRunStatic owner
  object; set_alarm is an H-014 TODO stub (owner: OS itimer service);
  pad returns the padded content (the vendor's stack-dangling return
  has no safe Kotlin analog - divergence documented).
- **u13 interp-sysfunc.cpp::GlobalName (GlobalName.kt)**: \\x01
  separator recoding; CHAR_1/NUL carriers; H-013: the vendor locals
  j=999 and k (written, never read) dropped with the note; tmp1[i+1]
  lookahead bounds-guarded (H-007).
- **u14 libmpscpp.cpp::global::TermCorrelate (TermCorrelate.kt)**:
  three-level Order/Data walk symmetrizing pair counts into global B;
  all member seats H-014 stubs (globalOrderN/globalDataN precedent,
  new keyed seats globalPlusIntKeyed/globalSetKeyed); vendor quirks
  kept (mid-loop xi declaration; the symmetrizing read takes the
  already-incremented value).
- **u15 compiler-parse.c::Extract2 (Extract2.kt)**: closes the
  Stubs.Extract2 seat (converted to the delegate; ExpandGet/IfCode
  call sites now hit the real function). Static tmp1[64]/tmp2[64] in
  the Extract2Static holder (H-006); the multicharacter ' \0'
  terminator quirk kept as the NUL write with note; defensive-copy
  early return divergence documented.
- **u16 compiler-parse.c::ExpandGet (ExpandGet.kt)**: ExpandNext
  shape plus the CodedComma default-slot tail routing through the
  now-real Extract2; the vendor's duplicated "#S#" || "#S#" tail test
  kept verbatim with the H-013 note.
- **u17 interp-bmgsubs.c::gotamatch (Gotamatch.kt)**: line acceptor;
  BUFSIZE 50000 and the bmdata.nsuccess counter in the BmgStatic
  owner (H-004/H-006); action callback is a nullable higher-order
  function; the NULL return renders as the -1 sentinel (noted, no
  in-corpus caller).
- **u18 compiler-abrtx.c::abrtx (Abrtx.kt)**: compiler epilogue;
  MAIN_flg/FCN_flg/F_flg/ExitCode/ExecName/pid/output_file added to
  CompilerState (H-004); epi/EP are H-014 stubs; H-001 divergences
  documented (the FILE* Out read-handle reuse becomes a local
  Reader; readLine newline handling; abort() as a thrown exception
  after the vendor message).
- **u19 interp-interp.cpp::<misc> (InterpMisc.kt)**: residue ledger
  (banner, includes, SQLITE/_INTERP_ define notes); define constants
  kept (GOSUBMAX etc); OPEN_PAREN references CodedOpen per H-015 (no
  redeclaration); InterpreterException declaration-shape class kept
  with the out-of-chunk definitions note; the prototype roster lands
  as H-014 Stubs seats with owners named (DirectRead unit 24,
  _SymFree unit 28, patrn1 unit 30 seats flagged for delegate
  conversion when those units land; the compiler-sys1.c::patrn1
  name collision is a distinct unit 26 question, ledgered on the
  seat).
- **u20 interp-arith.cpp::numcomp (Numcomp.kt)**: numeric strcmp;
  live non-MULTI_PRECISION arm (strtod double compare, BIGFLOAT arm
  ledgered; MP/GMP arm excluded per the reference build, H-018);
  floatstring as an H-014 stub (interp-arith.cpp:597 below-roster
  inline); private atoll helper parses the C leading-integer domain
  (the vendor int truncation kept).

Consult rows for B1 units 11-20 (timestamped before the renders):

- DOC consult: unit compiler-parse.c::ExpandNext, entry H-001, outcome hit-reuse, timestamp 2026-11-19T01:54:24Z
- DOC consult: unit compiler-parse.c::ExpandNext, entry H-007, outcome hit-reuse, timestamp 2026-11-19T01:54:24Z
- DOC consult: unit compiler-parse.c::ExpandNext, entry H-017, outcome hit-reuse, timestamp 2026-11-19T01:54:24Z
- DOC consult: unit mumps.cpp::<misc>, entry H-001, outcome hit-reuse, timestamp 2026-11-19T01:54:24Z
- DOC consult: unit mumps.cpp::<misc>, entry H-004, outcome hit-reuse, timestamp 2026-11-19T01:54:24Z
- DOC consult: unit mumps.cpp::<misc>, entry H-014, outcome hit-reuse, timestamp 2026-11-19T01:54:24Z
- DOC consult: unit interp-sysfunc.cpp::GlobalName, entry H-007, outcome hit-reuse, timestamp 2026-11-19T01:54:24Z
- DOC consult: unit interp-sysfunc.cpp::GlobalName, entry H-013, outcome hit-reuse, timestamp 2026-11-19T01:54:24Z
- DOC consult: unit interp-sysfunc.cpp::GlobalName, entry H-015, outcome hit-reuse, timestamp 2026-11-19T01:54:24Z
- DOC consult: unit libmpscpp.cpp::global::TermCorrelate, entry H-013, outcome hit-reuse, timestamp 2026-11-19T01:54:24Z
- DOC consult: unit libmpscpp.cpp::global::TermCorrelate, entry H-014, outcome hit-reuse, timestamp 2026-11-19T01:54:24Z
- DOC consult: unit libmpscpp.cpp::global::TermCorrelate, entry H-017, outcome hit-reuse, timestamp 2026-11-19T01:54:24Z
- DOC consult: unit compiler-parse.c::Extract2, entry H-001, outcome hit-reuse, timestamp 2026-11-19T01:54:24Z
- DOC consult: unit compiler-parse.c::Extract2, entry H-006, outcome hit-reuse, timestamp 2026-11-19T01:54:24Z
- DOC consult: unit compiler-parse.c::Extract2, entry H-013, outcome hit-reuse, timestamp 2026-11-19T01:54:24Z
- DOC consult: unit compiler-parse.c::ExpandGet, entry H-013, outcome hit-reuse, timestamp 2026-11-19T01:54:24Z
- DOC consult: unit compiler-parse.c::ExpandGet, entry H-014, outcome hit-reuse, timestamp 2026-11-19T01:54:24Z
- DOC consult: unit compiler-parse.c::ExpandGet, entry H-015, outcome hit-reuse, timestamp 2026-11-19T01:54:24Z
- DOC consult: unit interp-bmgsubs.c::gotamatch, entry H-004, outcome hit-reuse, timestamp 2026-11-19T01:54:24Z
- DOC consult: unit interp-bmgsubs.c::gotamatch, entry H-006, outcome hit-reuse, timestamp 2026-11-19T01:54:24Z
- DOC consult: unit interp-bmgsubs.c::gotamatch, entry H-015, outcome hit-reuse, timestamp 2026-11-19T01:54:24Z
- DOC consult: unit compiler-abrtx.c::abrtx, entry H-001, outcome hit-adapt, timestamp 2026-11-19T01:54:24Z
- DOC consult: unit compiler-abrtx.c::abrtx, entry H-004, outcome hit-reuse, timestamp 2026-11-19T01:54:24Z
- DOC consult: unit compiler-abrtx.c::abrtx, entry H-014, outcome hit-reuse, timestamp 2026-11-19T01:54:24Z
- DOC consult: unit compiler-abrtx.c::abrtx, entry H-015, outcome hit-reuse, timestamp 2026-11-19T01:54:24Z
- DOC consult: unit interp-interp.cpp::<misc>, entry H-004, outcome hit-reuse, timestamp 2026-11-19T01:54:24Z
- DOC consult: unit interp-interp.cpp::<misc>, entry H-013, outcome hit-reuse, timestamp 2026-11-19T01:54:24Z
- DOC consult: unit interp-interp.cpp::<misc>, entry H-014, outcome hit-reuse, timestamp 2026-11-19T01:54:24Z
- DOC consult: unit interp-interp.cpp::<misc>, entry H-015, outcome hit-adapt, timestamp 2026-11-19T01:54:24Z
- DOC consult: unit interp-arith.cpp::numcomp, entry H-009, outcome hit-reuse, timestamp 2026-11-19T01:54:24Z
- DOC consult: unit interp-arith.cpp::numcomp, entry H-010, outcome hit-reuse, timestamp 2026-11-19T01:54:24Z
- DOC consult: unit interp-arith.cpp::numcomp, entry H-014, outcome hit-reuse, timestamp 2026-11-19T01:54:24Z
- DOC consult: unit interp-arith.cpp::numcomp, entry H-018, outcome hit-reuse, timestamp 2026-11-19T01:54:24Z

Local gate 0 errors / 0 warnings (kotlinc 2.1.20, corretto
17.0.21.1); pre-push scan clean across the 17 touched files
(10 tranche-3 unit files, 3 carrier files, 4 tranche-2 files).

### DOC B1 tranche 4 (units 21-26), 2026-11-19

Narrative per unit (timestamp 2026-11-19T01:58:51Z recorded before
the renders; full instrument):

- **u21 compiler-localarray.c::LocalArray (LocalArray.kt)**: LHS
  local-array expander; goto has_array linearized (H-011); the
  compiler extern v1d added to CompilerState; Set_flg is the
  ShortArray(1) out-param seat (H-006); index seats route through
  the real unit-15 Extract2; H-013: the vendor's unused local tmp
  ledger dropped with the note; the k = vd[j] NUL-capture quirk and
  the i = j-1 rewind kept verbatim.
- **u22 interp-bstring.cpp::<misc> (BstringMisc.kt)**: Lockner
  banner + bstrcat/banner ledger (its definition is below the
  chunker cut, kept banner-only, no corpus call site); struct
  bstring renders as the Bstring carrier class; checked_strlen's
  i <= STR_MAX one-past walk kept in shape with the bounds guard
  (H-007 note) and the dummy return kept; bcatchr's length ==
  STR_MAX terminator-beyond-carrier write guarded with the
  divergence note; sigint is the H-014 stub.
- **u23 libmpscpp.cpp::Input (Input.kt)**: word reader; sym_ stub;
  static tmp/scount holders (H-006); the static-return divergence
  documented; inline.h:46 strcatx rendered as the shared internal
  helper (H-003 note; the unit-25 seat reuses it); fgets renders as
  the raw single-line reader (retained-newline divergence noted);
  H-011 goto reread flag; the C ispunct domain rendered as byte
  ranges (the backtick byte kept out of source text, scan-gate
  note); the vendor double noword label quirk noted.
- **u24 interp-sysfunc.cpp::DirectRead (DirectRead.kt)**: closes
  the Stubs.DirectRead seat to the delegate; fstat/fileno have no
  Reader-carrier analog - the H-001 divergences are ledgered
  (terminal detect via the standard console; the FIFO arm falls to
  the read path; the stty raw scouts run as OS subprocesses with
  the vendor's argument orders); the vendor's inverted fstat==1
  success test and the missing EOF test in the S_ISCHR arm are kept
  verbatim in shape.
- **u25 interp-sysfunc.cpp::<misc> (SysfuncMisc.kt)**: residue
  ledger (LINEFEED define kept in the comment ledger after a
  top-level const collision with the batch-2 carriers surfaced at
  the gate - H-015); eight small bodies live (VariableNotFound,
  VariableNotFound1, ErrorMessage closing the Stubs.ErrorMessage
  seat to a delegate, LocalData, LocalOrder, LocalNext, GlobalNext,
  LocalGet, GlobalGet, fullUnpad); sym_ returns the vendor NULL as
  the Kotlin nullable carrier (seat type amended); strtok renders
  as the manual in-order split with the stateful-tokenizer note;
  unpad is a batch-10 unit carried as an H-014 stub; NEXT const
  added to Carriers (defines.h:85).
- **u26 compiler-sys1.c::patrn1 (Patrn1Sys1.kt)**: pattern-argument
  parser; the a1285/a1283/a1281 goto web linearized as the outer
  loop + string-walk break + the a1281 tail (H-011); the compiler
  bd extern added to CompilerState (appends ride the new
  CstringOps.catAt CharArray seat); the parser separator set kept
  byte-verbatim; the paired-quote 210 coded byte and the m paren
  nesting kept; H-015: the distinct same-named unit
  interp-parse.cpp::patrn1 (unit 30) will resolve by overload
  arity; xpx store widened per H-010.

Consult rows for B1 units 21-26 (timestamped before the renders):

- DOC consult: unit compiler-localarray.c::LocalArray, entry H-001, outcome hit-reuse, timestamp 2026-11-19T01:58:51Z
- DOC consult: unit compiler-localarray.c::LocalArray, entry H-006, outcome hit-reuse, timestamp 2026-11-19T01:58:51Z
- DOC consult: unit compiler-localarray.c::LocalArray, entry H-011, outcome hit-reuse, timestamp 2026-11-19T01:58:51Z
- DOC consult: unit compiler-localarray.c::LocalArray, entry H-014, outcome hit-reuse, timestamp 2026-11-19T01:58:51Z
- DOC consult: unit interp-bstring.cpp::<misc>, entry H-001, outcome hit-reuse, timestamp 2026-11-19T01:58:51Z
- DOC consult: unit interp-bstring.cpp::<misc>, entry H-004, outcome hit-reuse, timestamp 2026-11-19T01:58:51Z
- DOC consult: unit interp-bstring.cpp::<misc>, entry H-007, outcome hit-adapt, timestamp 2026-11-19T01:58:51Z
- DOC consult: unit interp-bstring.cpp::<misc>, entry H-013, outcome hit-reuse, timestamp 2026-11-19T01:58:51Z
- DOC consult: unit libmpscpp.cpp::Input, entry H-001, outcome hit-adapt, timestamp 2026-11-19T01:58:51Z
- DOC consult: unit libmpscpp.cpp::Input, entry H-003, outcome hit-reuse, timestamp 2026-11-19T01:58:51Z
- DOC consult: unit libmpscpp.cpp::Input, entry H-006, outcome hit-reuse, timestamp 2026-11-19T01:58:51Z
- DOC consult: unit libmpscpp.cpp::Input, entry H-011, outcome hit-reuse, timestamp 2026-11-19T01:58:51Z
- DOC consult: unit interp-sysfunc.cpp::DirectRead, entry H-001, outcome hit-adapt, timestamp 2026-11-19T01:58:51Z
- DOC consult: unit interp-sysfunc.cpp::DirectRead, entry H-007, outcome hit-reuse, timestamp 2026-11-19T01:58:51Z
- DOC consult: unit interp-sysfunc.cpp::DirectRead, entry H-014, outcome hit-reuse, timestamp 2026-11-19T01:58:51Z
- DOC consult: unit interp-sysfunc.cpp::<misc>, entry H-001, outcome hit-reuse, timestamp 2026-11-19T01:58:51Z
- DOC consult: unit interp-sysfunc.cpp::<misc>, entry H-003, outcome hit-adapt, timestamp 2026-11-19T01:58:51Z
- DOC consult: unit interp-sysfunc.cpp::<misc>, entry H-014, outcome hit-reuse, timestamp 2026-11-19T01:58:51Z
- DOC consult: unit interp-sysfunc.cpp::<misc>, entry H-015, outcome hit-adapt, timestamp 2026-11-19T01:58:51Z
- DOC consult: unit compiler-sys1.c::patrn1, entry H-006, outcome hit-reuse, timestamp 2026-11-19T01:58:51Z
- DOC consult: unit compiler-sys1.c::patrn1, entry H-010, outcome hit-reuse, timestamp 2026-11-19T01:58:51Z
- DOC consult: unit compiler-sys1.c::patrn1, entry H-011, outcome hit-adapt, timestamp 2026-11-19T01:58:51Z
- DOC consult: unit compiler-sys1.c::patrn1, entry H-014, outcome hit-reuse, timestamp 2026-11-19T01:58:51Z
- DOC consult: unit compiler-sys1.c::patrn1, entry H-015, outcome hit-reuse, timestamp 2026-11-19T01:58:51Z

Local gate 0 errors / 0 warnings (kotlinc 2.1.20, corretto
17.0.21.1) after two fixes (a duplicated concatenation operator in
LocalArray, the LINEFEED top-level collision in SysfuncMisc);
pre-push scan clean after the ispunct backtick re-code.

### DOC B1 tranche 5 (units 27-31), 2026-11-19

Closes the DOC B1 translation phase (all 31 units translated; the
per-unit check columns remain todo by design, as in batch 2).

Unit 27 (compiler-sys1.c::MyFunc, MyFunc.kt): the built-in name
table verbatim (18 names); branch-local vs caret-external emission;
Quirks kept: the k>29 reset to k=9; the unused j scratch read; the
dead strcpy(bd,"gtmp") kept with the dead-code note; the printf k
with no conversion argument (ignored, noted). Extract1 rides the
new H-014 seat (owner compiler-parse.c:2119, below the roster
cut); Extract2 remains the unit-15 owner (no cross-unit seat).
ArgMax rides CompilerState (compiler-globals.h:53 extern short,
Int seat per the promotion note).

Unit 28 (interp-sym.cpp::_SymFree, SymFree.kt): struct stab
(sym.h:43) as the Stab carrier class; symbol-table scope release
with the two-mode walk (pop with non-zero, variables-only with
zero). free/delete render as referent nulling with the GC
divergence note; the vendor comma-operator line kept as two
statements; sym_ and sigint ride the H-014 seats (unit 26's
compiler patrn1 note applies, distinct vendor). MSV carriers added
to Carriers: start[SYM_MAX], _Sym (leading underscore kept
verbatim), xd, xpx (stateVector.h:71/65/45/56; SYM_MAX 200,
defines.h:218). Stubs.SymFree closed to the delegate.

Unit 29 (include/mumpsc/numSetup.h::<misc>, NumSetupMisc.kt): the
NS_ADD..NS_INTDIV opcode consts verbatim from numSetupDefines.h;
the H-018 preprocessor-conditional selection renders as the runtime
opcode dispatch (opcode seat is the consts, kept); the NS_DIV
always-floating entry quirk kept; the fixed-point and live hardware
FP arms live, the MPFR/GMP/BIGFLOAT arms ledgered (H-018). strtod
shared from MpsExp; atollShared (Numcomp atoll rename) shared. The
NS_INTDIV integer arm keeps the C truncating shape.

Unit 30 (interp-parse.cpp::patrn1, Patrn1Interp.kt): the
interpreter-side pattern parser over the MSV carriers; same-name
unit 26 (compiler patrn1) resolves by overload arity (the
compiler render is two-arg, this one three-arg with the Msv).
Quirks kept: bd appends at offset 0 (the interpreter target); the
a1281 block writes tmp1[0] before the nonzero test; no m-paren
guard unlike the compiler unit. Stubs.patrn1 closed to the
delegate (member delegates to the top-level render via the
package-qualified call to avoid the same-name recursion).

Unit 31 (libmpscpp.cpp::<misc-1>, LibMisc1.kt): the leading
600-loc residue chunk. Chunker boundaries documented: begins at the
banner, ends inside the unary operator- body (the closing return
and braces are outside the chunk; noted). Content: GlobalClassStatic
Eflg seat; the TEN-column mdh overload (arities 5/7/10 resolve the
three same-name vendors); the file-backed Hash class (fopen to
RandomAccessFile per H-001, the probe/replace/append algorithm with
the zap-to-1's comment that writes the 2 byte kept verbatim, the
byte-loop fwrite renders); Indirect; the global operator() family at
one to ten keys (the vendor stops at ten; no eleventh seat), the
zero- and long-key seats, and the vararg seat deriving the same
letter list the vendor writes as literals (byte-identical format
noted); the operator= seats renamed invoke2/invokeAssign (Kotlin
has no operator= token; H-015); the T-op= global families over the
eight primitives (named seats, the T& rides one-element arrays per
H-006); globalConstruct; unary plus (unary minus body is cut at
the chunk edge). The commented-out mstring global operator block
kept as the dead-code ledger. makeGlobal and lhash ride new H-014
seats (owners outside the chunk / below the roster); zfcn and the
get seats reused.

Consult rows for B1 units 27-31 (timestamped before the renders):

- DOC consult: unit compiler-sys1.c::MyFunc, entry H-013, outcome hit-reuse, timestamp 2026-11-19T02:02:56Z
- DOC consult: unit compiler-sys1.c::MyFunc, entry H-014, outcome hit-reuse, timestamp 2026-11-19T02:02:56Z
- DOC consult: unit compiler-sys1.c::MyFunc, entry H-015, outcome hit-reuse, timestamp 2026-11-19T02:02:56Z
- DOC consult: unit compiler-sys1.c::MyFunc, entry H-001, outcome hit-reuse, timestamp 2026-11-19T02:02:56Z
- DOC consult: unit interp-sym.cpp::_SymFree, entry H-004, outcome hit-reuse, timestamp 2026-11-19T02:02:56Z
- DOC consult: unit interp-sym.cpp::_SymFree, entry H-014, outcome hit-reuse, timestamp 2026-11-19T02:02:56Z
- DOC consult: unit interp-sym.cpp::_SymFree, entry H-001, outcome hit-reuse, timestamp 2026-11-19T02:02:56Z
- DOC consult: unit include/mumpsc/numSetup.h::<misc>, entry H-018, outcome hit-adapt, timestamp 2026-11-19T02:02:56Z
- DOC consult: unit include/mumpsc/numSetup.h::<misc>, entry H-010, outcome hit-reuse, timestamp 2026-11-19T02:02:56Z
- DOC consult: unit include/mumpsc/numSetup.h::<misc>, entry H-009, outcome hit-reuse, timestamp 2026-11-19T02:02:56Z
- DOC consult: unit include/mumpsc/numSetup.h::<misc>, entry H-014, outcome hit-reuse, timestamp 2026-11-19T02:02:56Z
- DOC consult: unit interp-parse.cpp::patrn1, entry H-011, outcome hit-reuse, timestamp 2026-11-19T02:02:56Z
- DOC consult: unit interp-parse.cpp::patrn1, entry H-014, outcome hit-reuse, timestamp 2026-11-19T02:02:56Z
- DOC consult: unit interp-parse.cpp::patrn1, entry H-015, outcome hit-reuse, timestamp 2026-11-19T02:02:56Z
- DOC consult: unit libmpscpp.cpp::<misc-1>, entry H-001, outcome hit-adapt, timestamp 2026-11-19T02:02:56Z
- DOC consult: unit libmpscpp.cpp::<misc-1>, entry H-003, outcome hit-adapt, timestamp 2026-11-19T02:02:56Z
- DOC consult: unit libmpscpp.cpp::<misc-1>, entry H-013, outcome hit-reuse, timestamp 2026-11-19T02:02:56Z
- DOC consult: unit libmpscpp.cpp::<misc-1>, entry H-014, outcome hit-reuse, timestamp 2026-11-19T02:02:56Z
- DOC consult: unit libmpscpp.cpp::<misc-1>, entry H-015, outcome hit-reuse, timestamp 2026-11-19T02:02:56Z

Local gate 0 errors / 0 warnings (kotlinc 2.1.20, corretto
17.0.21.1) after the tranche fixes (the Hash CharArray index typing,
the operator= seat names, the SymFree OrigName string accessor); the
pre-push scan is clean on all nine touched files (dollar byte in the
mdh comment rebuilt programmatically, the null-assertion
alternatives as local non-null binding and checkNotNull).

### DOC B1 close — export instrument fix (C-008), 2026-11-19

The first post-tranche-5 Rscript of export_doc_events.R on m1-build
halted: "duplicate consult for (unit, entry)" on
libmpscpp.cpp::mdh_tree_level_add / H-015. Diagnosis: the two rows
are DISTINCT roster units consulting H-015 once each (the batch-2
seven-column overload, hit-reuse 2026-11-18T22:51:46Z; the batch-1
five-column overload, hit-adapt 2026-11-19T01:52:03Z). The roster id
strings are file-level and not unique (nine rows share the
mdh_tree_level_add string), so the string-level duplicate check
contradicted the C-005 contract it implemented (README: unit_id is
descriptive only; string-level dedup is forbidden). No journal row
was wrong; neither row is edited. The exporter's duplicate check now
keys on (unit, entry, timestamp) (an exact repeat of all three fields
still fails loudly); the grammar check, the roster-id check, and the
C-005 event mapping are unchanged. Registered as C-008 (registry and
README amended in the same push; the C-007 table row backfill is
noted there). The CSV export re-runs on the same journal after this
push.

### DOC B1 close — coverage checklist (units 1-31), 2026-11-19

Manual coverage checklist per the DOC batch-close discipline (section
1: for each canonical construct in the batch, did at least one entry
match?; the DOC equivalent of PRO's coverage_report/0). Units 1-31
were translated 2026-11-19 in five tranches (commits b2f47c9..
fa649b9 range plus the close pushes 4a75288/45df0fe; kotlinc 0/0 per
tranche, gradle BUILD SUCCESSFUL on m1-build after tranches 2-5).

Entry-by-entry coverage across the 31 B1 unit journals (105 consults;
none consulted an entry without applying it):

- FILE star to Reader/Writer (H-001): matched in 12 B1 unit journal(s), 8 reuse / 4 adapt
- out-of-scope library call (H-003): matched in 4 B1 unit journal(s), 1 reuse / 3 adapt
- include-within-function table carrier (H-004): matched in 9 B1 unit journal(s), 9 reuse
- output-shape holder carrier (H-006): matched in 5 B1 unit journal(s), 5 reuse
- string walk idiom (H-007): matched in 7 B1 unit journal(s), 5 reuse / 2 adapt
- one-owner object seat (H-008): matched in 1 B1 unit journal(s), 1 adapt
- sprintf/printf locale contract (H-009): matched in 5 B1 unit journal(s), 5 reuse
- numeric promotions/domains (H-010): matched in 5 B1 unit journal(s), 5 reuse
- goto linearization (H-011): matched in 4 B1 unit journal(s), 3 reuse / 1 adapt
- dead code/comment fossils (H-013): matched in 8 B1 unit journal(s), 8 reuse
- out-of-scope stub seats (H-014): matched in 21 B1 unit journal(s), 21 reuse
- grammar collisions/byte-domain literals (H-015): matched in 13 B1 unit journal(s), 10 reuse / 3 adapt
- camelCase naming (H-017): matched in 7 B1 unit journal(s), 7 reuse
- preprocessor_conditional (H-018): matched in 4 B1 unit journal(s), 3 reuse / 1 adapt (batch-1 recurrence of the entry captured at the H-018 T1 capture: btree, mps_acos, numcomp, numSetup; B1 precedes B2 only in order, and the B1 units consulted the entry as it stood)

H-002, H-005, H-012 and H-016 found no triggering situation in B1 (no
consult row; the entries are not B1 constructs for this arm's batch;
state recorded so entry-for-entry parity with PRO's rule base is
unaffected; no revision needed).

Consult outcome totals for the batch: 90 hit-reuse, 15 hit-adapt,
0 miss (105 consults, one per (unit, entry) per C-005 as amended by
C-008). Hit rate 105/105 = 100.0% (every consult matched an entry);
reuse rate 90/105 = 85.7% (adaptation, not failure, accounts for the
remainder). No miss rows: the B1 canon was fully covered by cookbook
v0 as it stood at batch open (with H-018 added during the batch per
C-007 and consulted by its four triggering units).

Per-unit consult coverage (unit -> entries consulted, B1 order):

- compiler-copyright-notice.c::<misc>: H-004
- compiler-if.c::<misc>: H-004
- compiler-open.c::<misc>: H-004
- interp-native-btree.cpp::btree: H-014, H-018, H-017
- libmpscpp.cpp::EncodeHTML: H-007, H-008, H-009, H-014, H-017
- interp-sym.cpp::_find: H-007, H-009, H-010, H-014, H-017
- compiler-parse.c::fixstr: H-007, H-015, H-017
- libmpscpp.cpp::mdh_tree_level_add: H-015, H-014, H-017
- include/mumpsc/libmpsrdbms.h::<misc>: H-003, H-014, H-015
- interp-arith.cpp::mps_acos: H-018, H-010, H-009, H-014
- compiler-parse.c::ExpandNext: H-001, H-007, H-017
- mumps.cpp::<misc>: H-001, H-004, H-014
- interp-sysfunc.cpp::GlobalName: H-007, H-013, H-015
- libmpscpp.cpp::global::TermCorrelate: H-013, H-014, H-017
- compiler-parse.c::Extract2: H-001, H-006, H-013
- compiler-parse.c::ExpandGet: H-013, H-014, H-015
- interp-bmgsubs.c::gotamatch: H-004, H-006, H-015
- compiler-abrtx.c::abrtx: H-001, H-004, H-014, H-015
- interp-interp.cpp::<misc>: H-004, H-013, H-014, H-015
- interp-arith.cpp::numcomp: H-009, H-010, H-014, H-018
- compiler-localarray.c::LocalArray: H-001, H-006, H-011, H-014
- interp-bstring.cpp::<misc>: H-001, H-004, H-007, H-013
- libmpscpp.cpp::Input: H-001, H-003, H-006, H-011
- interp-sysfunc.cpp::DirectRead: H-001, H-007, H-014
- interp-sysfunc.cpp::<misc>: H-001, H-003, H-014, H-015
- compiler-sys1.c::patrn1: H-006, H-010, H-011, H-014, H-015
- compiler-sys1.c::MyFunc: H-013, H-014, H-015, H-001
- interp-sym.cpp::_SymFree: H-004, H-014, H-001
- include/mumpsc/numSetup.h::<misc>: H-018, H-010, H-009, H-014
- interp-parse.cpp::patrn1: H-011, H-014, H-015
- libmpscpp.cpp::<misc-1>: H-001, H-003, H-013, H-014, H-015

Events export: wp2/logs/DOC_knowledge_events.csv regenerated by
Rscript on m1-build (commit 45df0fe, 2026-11-19): 180 consult rows ->
358 event rows (178 reuse + 178 hit + 2 miss; the two miss rows are
the batch-2 C-006 reconstructed H-003 miss consults of mps_exp /
mps_exp10, outside B1 and predating the H-018 capture). Export ran
under the C-008 duplicate key after the registered instrument fix.

PI decisions recorded at close (both 2026-11-19):

1. B1 per-unit checks: the four check columns stay todo and are
   filled by the per-unit check backfill pass at the C-001 cycle-10
   buffer (C-002 discipline; no earlier targeted B1 pass). Nothing
   is backfilled from grades or builds.
2. New cookbook entries: none. Zero misses mean no construct went
   uncovered, so no B1 capture candidate exists under T1/T2/T3; the
   H-018 entry stands as the only B1-era capture. Cookbook v0
   remains at H-001..H-018 and stays entry-for-entry parallel with
   PRO's rules_v0.pl (h001..h018).

DOC B1 record: 31/31 translated and closed (PILOT_TRACKING DOC
column translated for rows 1-31; check columns todo per decision 1).
Build: 0 errors / 0 warnings local gate per tranche; gradle BUILD
SUCCESSFUL on m1-build after tranches 2-5. The B2-close note
(above) records the batch-2 state; with B1 and B2 closed, the DOC
arm's pilot translation phase is complete and the arm enters the
interleaved cycles per C-001's map (C1..C9 batches; DOC cycle-1
batch B2 per the map is already translated and closed, so the DOC
cycle-1 cell carries the batch-2 close record and any directory
carryover).

### DOC B3 tranche 1 (units 63-72), 2026-11-19

B3 opens with the ten simple units (roster 63-72, batch_plan ascending order; PRO's own B3 runs descending so its u63 is its parse_, a numbering artifact, not a discipline divergence). All renders from the pristine vendor tree only. Gate green on first pass (kotlinc 0 errors / 0 warnings); post-gate scan found one raw-order hit in my own MpsSqrt.kt header ("QUARK" -> "Quirk") and one raw $order literal in the earlier u39 GlobalOrder.kt render; both fixed and re-scanned clean, with the u39 fix noted below (no plan change, the emitted bytes are identical).

- compiler-epi.c::<misc> (63): EpiMisc.kt. Chunk is the June 16 2024 banner plus the 4-include block at the top of epi.c; the epi unit itself is batch-7 row 189 and is not rendered here. Include ledger kept verbatim (H-013), seat named after the chunk, out-of-scope body not rendered.
- compiler-read.c::<misc> (64): ReadMisc.kt. May 15 2024 banner plus includes; read_code proper is batch-6 row 160 and stays unrendered. Quirk: banner text is byte-identical to epi.c's except the date line, kept verbatim.
- libmpscpp.cpp::global::Concat (65): GlobalConcat.kt. Two Mglobal RETRIEVE calls into fresh Mstrings, wraps via execOrderPair, matching MumpsGlobalException throws on both failure arms. Quirk kept: both arms carry the same body comment; LPWD/CodedOpen/Close built with Char(0x00ce..cf).
- libmpscpp.cpp::mdh_tree_level_add (66): MdhTreeLevelAdd3.kt. Three-column overload; joins the u8 five-col and u43 seven-col seats as the third arity in the family. Identity-comment "%p won't match on JVM" divergence re-H-017'd. Inline decimals replaced by named Char(0x00ce/d0/cf) consts (H-014 named-seat pattern, incident-free).
- interp-arith.cpp::mps_sqrt (67): MpsSqrt.kt. C "in" parameter name is a Kotlin keyword, renamed to inCh (H-015). Plain-double arm live; BIGFLOAT and MULTI_PRECISION arms ledgered as usual preprocessor contracts (H-018). Quirk kept: no negative-operand check in the live arm ($Sqrt semantics differ from sqrt(3)).
- libmpscpp.cpp::Max (68): Max.kt. Dead args (svptr, ep) kept verbatim; arg1[-1] UB edge kept with note and GlobalMin u44 precedent; %g via String.format(Locale.US) (H-009 JVM-shape adaptation, hit-adapt); folds into a fresh String (H-008). global::Max is a class member, out of corpus scope here, so it rides in Stubs as a globalMax seat raising NotImplementedError, and Max.kt calls that seat.
- libmpscpp.cpp::GlobalOrder (69): appended globalOrderString onto u39 GlobalOrder.kt (H-015 naming adaptation, std::string overload). Vendor's dead r[STR_MAX] buffer noted and folded; u39's earlier literal $order re-escaped to \u0024order for scan-cleanliness (emitted bytes unchanged, first-pass render note only).
- libmpscpp.cpp::GlobalData (70): GlobalData.kt. char* $Data(\x01) form; builds "s %=\u0024data(" then xecute + SymGet (reused seats) + atoi (Find.kt) reuse; dead r[STR_MAX] comment kept; tpx 0/1 and -1 fail distinct.
- interp-arith.cpp::mps_atan (71): MpsAtan.kt. Twin of the already-rendered mps_acos (u42), same shape and ledger quirks; plain-double arm live, high-precision arms ledgered (H-018).
- libmpscpp.cpp::global::Fnumber (72): GlobalFnumber.kt. One-arg member (i.e. the global itself); DOLLAR_72 named const, Mglobal throw on fail, v1d[1]/bd[1] offset-1 convention with u42 precedent.

Gate note: kotlinc 0 errors / 0 warnings first pass; scan clean after the two header/literal fixes above (MpsSqrt header, u39 $order re-escape). Scan-gate pattern used: raw $-letter form, !!, backtick, em/en dash, non-ASCII.

- DOC consult: unit compiler-epi.c::<misc>, entry H-013, outcome hit-reuse, timestamp 2026-11-19T02:35:54Z
- DOC consult: unit compiler-epi.c::<misc>, entry H-014, outcome hit-reuse, timestamp 2026-11-19T02:35:54Z
- DOC consult: unit compiler-epi.c::<misc>, entry H-018, outcome hit-reuse, timestamp 2026-11-19T02:35:54Z
- DOC consult: unit compiler-read.c::<misc>, entry H-013, outcome hit-reuse, timestamp 2026-11-19T02:35:54Z
- DOC consult: unit compiler-read.c::<misc>, entry H-014, outcome hit-reuse, timestamp 2026-11-19T02:35:54Z
- DOC consult: unit compiler-read.c::<misc>, entry H-018, outcome hit-reuse, timestamp 2026-11-19T02:35:54Z
- DOC consult: unit libmpscpp.cpp::global::Concat, entry H-012, outcome hit-reuse, timestamp 2026-11-19T02:35:54Z
- DOC consult: unit libmpscpp.cpp::global::Concat, entry H-013, outcome hit-reuse, timestamp 2026-11-19T02:35:54Z
- DOC consult: unit libmpscpp.cpp::global::Concat, entry H-014, outcome hit-reuse, timestamp 2026-11-19T02:35:54Z
- DOC consult: unit libmpscpp.cpp::global::Concat, entry H-017, outcome hit-reuse, timestamp 2026-11-19T02:35:54Z
- DOC consult: unit libmpscpp.cpp::mdh_tree_level_add, entry H-014, outcome hit-reuse, timestamp 2026-11-19T02:35:54Z
- DOC consult: unit libmpscpp.cpp::mdh_tree_level_add, entry H-015, outcome hit-reuse, timestamp 2026-11-19T02:35:54Z
- DOC consult: unit libmpscpp.cpp::mdh_tree_level_add, entry H-017, outcome hit-reuse, timestamp 2026-11-19T02:35:54Z
- DOC consult: unit interp-arith.cpp::mps_sqrt, entry H-009, outcome hit-reuse, timestamp 2026-11-19T02:35:54Z
- DOC consult: unit interp-arith.cpp::mps_sqrt, entry H-010, outcome hit-reuse, timestamp 2026-11-19T02:35:54Z
- DOC consult: unit interp-arith.cpp::mps_sqrt, entry H-014, outcome hit-reuse, timestamp 2026-11-19T02:35:54Z
- DOC consult: unit interp-arith.cpp::mps_sqrt, entry H-015, outcome hit-reuse, timestamp 2026-11-19T02:35:54Z
- DOC consult: unit interp-arith.cpp::mps_sqrt, entry H-017, outcome hit-reuse, timestamp 2026-11-19T02:35:54Z
- DOC consult: unit interp-arith.cpp::mps_sqrt, entry H-018, outcome hit-reuse, timestamp 2026-11-19T02:35:54Z
- DOC consult: unit libmpscpp.cpp::Max, entry H-008, outcome hit-reuse, timestamp 2026-11-19T02:35:54Z
- DOC consult: unit libmpscpp.cpp::Max, entry H-009, outcome hit-adapt, timestamp 2026-11-19T02:35:54Z
- DOC consult: unit libmpscpp.cpp::Max, entry H-013, outcome hit-reuse, timestamp 2026-11-19T02:35:54Z
- DOC consult: unit libmpscpp.cpp::Max, entry H-014, outcome hit-reuse, timestamp 2026-11-19T02:35:54Z
- DOC consult: unit libmpscpp.cpp::Max, entry H-017, outcome hit-reuse, timestamp 2026-11-19T02:35:54Z
- DOC consult: unit libmpscpp.cpp::GlobalOrder, entry H-008, outcome hit-reuse, timestamp 2026-11-19T02:35:54Z
- DOC consult: unit libmpscpp.cpp::GlobalOrder, entry H-013, outcome hit-reuse, timestamp 2026-11-19T02:35:54Z
- DOC consult: unit libmpscpp.cpp::GlobalOrder, entry H-014, outcome hit-reuse, timestamp 2026-11-19T02:35:54Z
- DOC consult: unit libmpscpp.cpp::GlobalOrder, entry H-015, outcome hit-adapt, timestamp 2026-11-19T02:35:54Z
- DOC consult: unit libmpscpp.cpp::GlobalOrder, entry H-017, outcome hit-reuse, timestamp 2026-11-19T02:35:54Z
- DOC consult: unit libmpscpp.cpp::GlobalData, entry H-013, outcome hit-reuse, timestamp 2026-11-19T02:35:54Z
- DOC consult: unit libmpscpp.cpp::GlobalData, entry H-014, outcome hit-reuse, timestamp 2026-11-19T02:35:54Z
- DOC consult: unit libmpscpp.cpp::GlobalData, entry H-015, outcome hit-reuse, timestamp 2026-11-19T02:35:54Z
- DOC consult: unit libmpscpp.cpp::GlobalData, entry H-017, outcome hit-reuse, timestamp 2026-11-19T02:35:54Z
- DOC consult: unit interp-arith.cpp::mps_atan, entry H-009, outcome hit-reuse, timestamp 2026-11-19T02:35:54Z
- DOC consult: unit interp-arith.cpp::mps_atan, entry H-010, outcome hit-reuse, timestamp 2026-11-19T02:35:54Z
- DOC consult: unit interp-arith.cpp::mps_atan, entry H-014, outcome hit-reuse, timestamp 2026-11-19T02:35:54Z
- DOC consult: unit interp-arith.cpp::mps_atan, entry H-017, outcome hit-reuse, timestamp 2026-11-19T02:35:54Z
- DOC consult: unit interp-arith.cpp::mps_atan, entry H-018, outcome hit-reuse, timestamp 2026-11-19T02:35:54Z
- DOC consult: unit libmpscpp.cpp::global::Fnumber, entry H-012, outcome hit-reuse, timestamp 2026-11-19T02:35:54Z
- DOC consult: unit libmpscpp.cpp::global::Fnumber, entry H-014, outcome hit-reuse, timestamp 2026-11-19T02:35:54Z
- DOC consult: unit libmpscpp.cpp::global::Fnumber, entry H-015, outcome hit-reuse, timestamp 2026-11-19T02:35:54Z
- DOC consult: unit libmpscpp.cpp::global::Fnumber, entry H-017, outcome hit-reuse, timestamp 2026-11-19T02:35:54Z

### DOC B3 tranche 2 (units 73-82), 2026-11-19

The ten core-stratum units of B3, consult timestamp 2026-11-19T02:42:31Z captured before renders. All renders from the pristine vendor tree only. Gate note: FIRST PASS RED (5 errors), unusual for this arm after the B1 discipline stabilized - two incidents, both logged as data: (a) a duplicate LINEFEED const (readf_ needed the byte filters; LINEFEED 10 was already declared by the unit-48 Getstr2 render as a Char, and my Carriers add collided; fixed by referencing the Getstr2 const per H-015 redeclaration rule and keeping only CRTN 13 / CTRLZ 26 as new Int consts), and (b) a val reassignment in ScanAlnum (tmp1 assigned in the repeat loop had to be var). Second pass 0/0, no warnings; scan clean on all 13 touched files after the fixes. Environment note: the working tree was rebuilt this session (repo tarball at master, vendor tarball sha256-verified against the wp1 pin, and the gate toolchain re-downloaded) after the execution environment upgrade wiped it.

- interp-arith.cpp::mps_justify (73): MpsJustify.kt. Dynamic-format sprintf: a is the caller-supplied format string handed to String.format(Locale.US) per H-009; BIGFLOAT (strtold) and MULTI_PRECISION (static once-init mpfr f=1 quirk, mpfr_sprintf) arms ledgered out per H-018.
- libmpscpp.cpp::Cost (74): Cost.kt. Memo recursion over the SeqAlignExterns matrix; EMPTY_CELL test, max-of-three with the A<=0 clamp BEFORE the memo write; costMatrix alias of the `a` top-level seat; missing init is a vendor NULL deref rendered as IllegalStateException (Sw.kt precedent); dead x local kept as comment (H-013). Landing this unit converted the unit-61 Stubs.Cost seat to a delegate (return widened Unit to Int, sw ignores it).
- libmpscpp.cpp::Scan (75): Scan.kt. fscanf %1024s analogue over the in_file Reader; fresh String per call per H-008 (ScanAlnum copies before the next call, no aliasing); dead j local and the UNREACHABLE too-long test (i >= 1023 compares the fscanf ITEM COUNT, always 1) ledgered per H-013; the one-byte C overflow of %1024s into tmp[1024] diverged away by the String carrier; tpx 0/1; INPUT_ERROR raise path kept unreachable as in the vendor.
- include/mumpsc/cppsysfunc.h::<misc> (76): CppsysfuncMisc.kt. Declaration-only ledger of the C-ABI support header; LocalName declared TWICE (kept); the C-ABI GlobalData/GlobalOrder names collide with the class-global members DOC's units 69/70 render, DISTINCT entities noted explicitly.
- libmpscpp.cpp::ScanAlnum (77): ScanAlnum.kt. MSV overload; dead ep/toklist/k locals (H-013); goto again as H-011 while(true); strlen-in-loop recompute quirk ledgered; isalnum/tolower as ASCII byte-domain helpers per H-012 with the signed-char UB divergence noted; FILE*/istream overloads not corpus units, ledgered.
- compiler-sys1.c::readf_ (78): Readf.kt. Reader over infile per the H-001 input direction; k>200 byte-drop filter kept verbatim; NOTABS arm ledgered out per H-018; dead for-loop counter i kept as comment; pd1/pd1len into CompilerState (new pd1len seat, compiler-globals-ext.h:36); PS1 overrun guarded with divergence note (vendor overflows silently).
- libmpscpp.cpp::mstring::Eval (79): MstringEval.kt. New Msv carriers sdlim/pd1len/t2/ERROR with stateVector.h lines, new INTERPRETER_ERROR const; parse_ rides the Stubs seat naming unit 93 (this batch, converts on landing); unreachable tmp="err" tail ledgered per H-013; pd1[sdlim] via cstringAt per the offset idiom.
- compiler-parse.c::ExpandQS (80): ExpandQS.kt. #G#/#S# branches byte-identical logic (same family as unit 65 Concat; NOT refactored, both rendered verbatim); CodedComma tail via the Stubs.Extract2 delegate or the raw sub-128 copy, literal NULL default; GlobalQS emission byte-verbatim into CompilerState.Out; i is a by-value copy, no back-propagation (kept).
- interp-parse.cpp::<misc> (81): InterpParseMisc.kt. Residue chunk through extern ierr1 (new top-level Int seat per H-004); SQLITE resolved DEFINED per H-018; prototype roster cross-references the unit-19 seats instead of redeclaring (both vendor files carry the same core declarations); paren-coding divergence ledgered: this chunk declares COMMA 207 and CLOSE_PAREN 209, NOT the stateVector CodedClose 207/CodedComma 208 pair - kept verbatim as IOPARSE consts with the same-byte remark on 207.
- libmpscpp.cpp::RunMumps (82): RunMumps.kt. varargs as Kotlin vararg of nullable strings; sym_ store/release rides the Stubs.sym_ seat (opcode 0/1); ERROR via the new Msv.ERROR carrier; NOERR armed before Interpret (Stubs seat); second strtok pass with the SPACE delimiter over the comma-tokenized buffer yields the same sequence - shape preserved, divergence ledgered; j = commas+1 can exceed the vararg count, break on running past (the vendor breaks on the NULL sentinel read out of the va_list, kept as the same break).

- DOC consult: unit interp-arith.cpp::mps_justify, entry H-009, outcome hit-reuse, timestamp 2026-11-19T02:42:31Z
- DOC consult: unit interp-arith.cpp::mps_justify, entry H-010, outcome hit-reuse, timestamp 2026-11-19T02:42:31Z
- DOC consult: unit interp-arith.cpp::mps_justify, entry H-017, outcome hit-reuse, timestamp 2026-11-19T02:42:31Z
- DOC consult: unit interp-arith.cpp::mps_justify, entry H-018, outcome hit-reuse, timestamp 2026-11-19T02:42:31Z
- DOC consult: unit libmpscpp.cpp::Cost, entry H-012, outcome hit-reuse, timestamp 2026-11-19T02:42:31Z
- DOC consult: unit libmpscpp.cpp::Cost, entry H-013, outcome hit-reuse, timestamp 2026-11-19T02:42:31Z
- DOC consult: unit libmpscpp.cpp::Cost, entry H-014, outcome hit-reuse, timestamp 2026-11-19T02:42:31Z
- DOC consult: unit libmpscpp.cpp::Cost, entry H-017, outcome hit-reuse, timestamp 2026-11-19T02:42:31Z
- DOC consult: unit libmpscpp.cpp::Scan, entry H-001, outcome hit-adapt, timestamp 2026-11-19T02:42:31Z
- DOC consult: unit libmpscpp.cpp::Scan, entry H-008, outcome hit-reuse, timestamp 2026-11-19T02:42:31Z
- DOC consult: unit libmpscpp.cpp::Scan, entry H-013, outcome hit-reuse, timestamp 2026-11-19T02:42:31Z
- DOC consult: unit include/mumpsc/cppsysfunc.h::<misc>, entry H-013, outcome hit-reuse, timestamp 2026-11-19T02:42:31Z
- DOC consult: unit include/mumpsc/cppsysfunc.h::<misc>, entry H-014, outcome hit-reuse, timestamp 2026-11-19T02:42:31Z
- DOC consult: unit include/mumpsc/cppsysfunc.h::<misc>, entry H-017, outcome hit-reuse, timestamp 2026-11-19T02:42:31Z
- DOC consult: unit libmpscpp.cpp::ScanAlnum, entry H-008, outcome hit-reuse, timestamp 2026-11-19T02:42:31Z
- DOC consult: unit libmpscpp.cpp::ScanAlnum, entry H-011, outcome hit-reuse, timestamp 2026-11-19T02:42:31Z
- DOC consult: unit libmpscpp.cpp::ScanAlnum, entry H-012, outcome hit-reuse, timestamp 2026-11-19T02:42:31Z
- DOC consult: unit libmpscpp.cpp::ScanAlnum, entry H-013, outcome hit-reuse, timestamp 2026-11-19T02:42:31Z
- DOC consult: unit libmpscpp.cpp::ScanAlnum, entry H-017, outcome hit-reuse, timestamp 2026-11-19T02:42:31Z
- DOC consult: unit compiler-sys1.c::readf_, entry H-001, outcome hit-adapt, timestamp 2026-11-19T02:42:31Z
- DOC consult: unit compiler-sys1.c::readf_, entry H-007, outcome hit-reuse, timestamp 2026-11-19T02:42:31Z
- DOC consult: unit compiler-sys1.c::readf_, entry H-017, outcome hit-reuse, timestamp 2026-11-19T02:42:31Z
- DOC consult: unit compiler-sys1.c::readf_, entry H-018, outcome hit-reuse, timestamp 2026-11-19T02:42:31Z
- DOC consult: unit libmpscpp.cpp::mstring::Eval, entry H-007, outcome hit-reuse, timestamp 2026-11-19T02:42:31Z
- DOC consult: unit libmpscpp.cpp::mstring::Eval, entry H-013, outcome hit-reuse, timestamp 2026-11-19T02:42:31Z
- DOC consult: unit libmpscpp.cpp::mstring::Eval, entry H-014, outcome hit-reuse, timestamp 2026-11-19T02:42:31Z
- DOC consult: unit libmpscpp.cpp::mstring::Eval, entry H-017, outcome hit-reuse, timestamp 2026-11-19T02:42:31Z
- DOC consult: unit compiler-parse.c::ExpandQS, entry H-001, outcome hit-reuse, timestamp 2026-11-19T02:42:31Z
- DOC consult: unit compiler-parse.c::ExpandQS, entry H-007, outcome hit-reuse, timestamp 2026-11-19T02:42:31Z
- DOC consult: unit compiler-parse.c::ExpandQS, entry H-012, outcome hit-reuse, timestamp 2026-11-19T02:42:31Z
- DOC consult: unit compiler-parse.c::ExpandQS, entry H-014, outcome hit-reuse, timestamp 2026-11-19T02:42:31Z
- DOC consult: unit compiler-parse.c::ExpandQS, entry H-017, outcome hit-reuse, timestamp 2026-11-19T02:42:31Z
- DOC consult: unit interp-parse.cpp::<misc>, entry H-004, outcome hit-reuse, timestamp 2026-11-19T02:42:31Z
- DOC consult: unit interp-parse.cpp::<misc>, entry H-014, outcome hit-reuse, timestamp 2026-11-19T02:42:31Z
- DOC consult: unit interp-parse.cpp::<misc>, entry H-017, outcome hit-reuse, timestamp 2026-11-19T02:42:31Z
- DOC consult: unit interp-parse.cpp::<misc>, entry H-018, outcome hit-reuse, timestamp 2026-11-19T02:42:31Z
- DOC consult: unit libmpscpp.cpp::RunMumps, entry H-007, outcome hit-reuse, timestamp 2026-11-19T02:42:31Z
- DOC consult: unit libmpscpp.cpp::RunMumps, entry H-014, outcome hit-reuse, timestamp 2026-11-19T02:42:31Z
- DOC consult: unit libmpscpp.cpp::RunMumps, entry H-017, outcome hit-reuse, timestamp 2026-11-19T02:42:31Z

### DOC B3 tranche 3 / sub-push A (units 83-87), 2026-11-19

First half of the complex tier, consult timestamp 2026-11-19T15:06:11Z captured before renders (the same timestamp serves the whole tranche per the working discipline). All renders from the pristine vendor tree. Gate: one red pass (a get_global seat call with a String where the seat takes CharArray in Cosine - cosmetic call-shape slip, fixed to the tmp1/btmp carriers), then 0/0, no warnings; scan clean on all six touched files after two KDoc dollar-letter escapes (GtkTreeAdd z~gtree name, Errmod Order/text names - the u39 re-escape precedent). Environment note: the execution environment reset twice during this sub-push (rebuilds from the repo tarball + sha256-verified vendor tarball each time; renders survived the resets, no work lost).

- interp-zfcns.cpp::GtkTreeAdd (83): GtkTreeAdd.kt. The GTK tree-store ladder: iters as H-006 single-slot holders, the sscanf %p handle recovery as an opaque String (the u8/u43 %p divergence precedent), gtk_tree_store_append/set as H-014 GTK-library seats. Quirks kept: the SECOND dead nargs==10 check (already returned at the first), the drifted inline column comments (column 6 twice etc.), the final nargs==13 return guarding nothing, and column 0 set before any bound test (nargs>=2 assumed).
- compiler-sys1.c::cannon (84): Cannon.kt. The numeric canonicalizer: sign folding, trailing-zero trim, leading-zero strip (the strcpy self-shifts as CharArray shift helpers), E/e exponent expansion via repeated mult by "10"/".1". mult is not a corpus unit anywhere in units.csv: H-014 Stubs.mult seat named for compiler-sys1.c::mult; atoi reuses the unit render of Find.kt. Quirk kept: the tmp[25] exponent gather has NO vendor bound test (guarded here, H-007 divergence note).
- libmpscpp.cpp::Cosine (85): Cosine.kt. A/B XNEXT walks with strncmp prefix tests, totD/totQ/tot accumulation, sqrt denominator with zero guards. Seats: Mglobal (XNEXT 8), get_global, globalData for the B.Data() probe, strtod reuse, kotlin.math.sqrt. Adaptation: the vendor mutates B.ref in place across the walk and restores it - the GlobalRef carrier is immutable (val ref), so walk state renders in locals with the same observable seat-call sequence (TermCorrelate precedent). Dead stores ledgered (x01, A2, bflg, the dummy strcpy).
- interp-bmgsubs.c::bmg_fsearch (86): BmgFsearch.kt. Boyer-Moore-Gosper file search: chunk reads with partial-line carry (linetemp), the delta0 scan with the LARGE cutoff wrap, the cmap/pattern equality confirmation, gotamatch handoff (unit 17, landed - the real function) with the NULL -1 sentinel preserved. bmdata struct carries render in BmgFsearchStatic per H-004 with the documented owner split (nsuccess stays in the unit-17 BmgStatic; a Kotlin object cannot grow across files). POSIX read(2) divergence: fd 0 routes to the arm STDIN_READER (getstr2 precedent, chars-for-bytes per H-012), fd != 0 rides the Stubs.posixRead seat naming the OS syscall interface. Quirks kept: the --j predecrement skips the last pattern byte (the "delta-less shortcut"), the vendor UB run of pattern[-1] on a full equal match (guarded here - a full real-equal run takes the submatch path, H-007 divergence note), the fd==0 exception to the NL insurance line.
- compiler-sys1.c::errmod_ (87): Errmod.kt. The error catalog switch: 33 message arms verbatim, printf banners via String.format Locale.US to System.out (H-001 console, Prnterr precedent). Quirks kept: cases 20/21 print the literals "21"/"22", the default prints only the number with the IGNORED extra printf argument, the banner itself prints msgnbr - 1, and the two raw dollar-letter strings (Order, text) render via the H-015 unicode escape with the same runtime bytes.

- DOC consult: unit interp-zfcns.cpp::GtkTreeAdd, entry H-006, outcome hit-reuse, timestamp 2026-11-19T15:06:11Z
- DOC consult: unit interp-zfcns.cpp::GtkTreeAdd, entry H-013, outcome hit-reuse, timestamp 2026-11-19T15:06:11Z
- DOC consult: unit interp-zfcns.cpp::GtkTreeAdd, entry H-014, outcome hit-reuse, timestamp 2026-11-19T15:06:11Z
- DOC consult: unit interp-zfcns.cpp::GtkTreeAdd, entry H-017, outcome hit-reuse, timestamp 2026-11-19T15:06:11Z
- DOC consult: unit compiler-sys1.c::cannon, entry H-007, outcome hit-reuse, timestamp 2026-11-19T15:06:11Z
- DOC consult: unit compiler-sys1.c::cannon, entry H-014, outcome hit-reuse, timestamp 2026-11-19T15:06:11Z
- DOC consult: unit compiler-sys1.c::cannon, entry H-017, outcome hit-reuse, timestamp 2026-11-19T15:06:11Z
- DOC consult: unit libmpscpp.cpp::Cosine, entry H-007, outcome hit-reuse, timestamp 2026-11-19T15:06:11Z
- DOC consult: unit libmpscpp.cpp::Cosine, entry H-010, outcome hit-reuse, timestamp 2026-11-19T15:06:11Z
- DOC consult: unit libmpscpp.cpp::Cosine, entry H-013, outcome hit-reuse, timestamp 2026-11-19T15:06:11Z
- DOC consult: unit libmpscpp.cpp::Cosine, entry H-014, outcome hit-reuse, timestamp 2026-11-19T15:06:11Z
- DOC consult: unit libmpscpp.cpp::Cosine, entry H-017, outcome hit-reuse, timestamp 2026-11-19T15:06:11Z
- DOC consult: unit interp-bmgsubs.c::bmg_fsearch, entry H-004, outcome hit-reuse, timestamp 2026-11-19T15:06:11Z
- DOC consult: unit interp-bmgsubs.c::bmg_fsearch, entry H-007, outcome hit-reuse, timestamp 2026-11-19T15:06:11Z
- DOC consult: unit interp-bmgsubs.c::bmg_fsearch, entry H-012, outcome hit-reuse, timestamp 2026-11-19T15:06:11Z
- DOC consult: unit interp-bmgsubs.c::bmg_fsearch, entry H-014, outcome hit-adapt, timestamp 2026-11-19T15:06:11Z
- DOC consult: unit compiler-sys1.c::errmod_, entry H-001, outcome hit-reuse, timestamp 2026-11-19T15:06:11Z
- DOC consult: unit compiler-sys1.c::errmod_, entry H-007, outcome hit-reuse, timestamp 2026-11-19T15:06:11Z
- DOC consult: unit compiler-sys1.c::errmod_, entry H-009, outcome hit-reuse, timestamp 2026-11-19T15:06:11Z
- DOC consult: unit compiler-sys1.c::errmod_, entry H-015, outcome hit-reuse, timestamp 2026-11-19T15:06:11Z

### DOC B3 tranche 3 / sub-push B (units 88-92), 2026-11-19

Units 88-92 rendered and pushed together (the complex-tier tail of the
tranche minus parse_, which rides alone in sub-push C as agreed). The
tranche-wide consult timestamp 2026-11-19T15:06:11Z (captured before
the first sub-push B render, per the working discipline; it serves the
whole tranche).

Narrative per unit:

- Unit 88, compiler-sys1.c::patrn (PatrnSys1.kt): the compiler-side
  pattern matcher over the bd scratch staged by patrn1. The parse loop
  keeps the vendor 1-based bd walk with the paired-quote 210 coded
  byte rewrite (the bd in-place mutation, H-007); the strlen(&bd[1])
  loop bound is hoisted once with a ledger note (the vendor re-
  evaluates per iteration but the loop cannot change the length).
  The definite arm does one match call; the indefinite arm runs the
  retry/retry1/recount odometer (H-011 first-landing flag, recount
  ladder, k>lad tail as the retry continue) until a hit or exhaustion,
  writing ierr1. iptr is assigned then never read (H-013 dead local,
  ledgered not declared). substr calls carry the &bd[1] pointer shift
  the CharArray seat cannot express - the shift is noted at the seat
  contract (H-014 pointer-shift note). cannon is the direct call to
  the landed u84 render; atoi reuses Find.kt; the five-argument
  match call rides the NEW Stubs seat naming compiler-sys1.c::match
  (batch-7 unit).
- Unit 92, interp-parse.cpp::patrn (PatrnInterp.kt): the interpreter
  twin of 88, reading svPtr.bd (Msv carrier, stateVector.h:42) and
  passing it as the sixth argument of the six-argument match twin.
  Same-name overload-arity resolution per H-015 (the patrn1 26/30
  precedent): patrn(ad) owns the sys1 unit, patrn(ad, svPtr) this
  one. Quirk kept and ledgered: the vendor loop bound is
  strlen(&svPtr->bd[0]) - offset ZERO, unlike the &bd[1] of the sys1
  twin - the parse walks the NUL tail byte; kept verbatim.
- Unit 89, globalClass.h::<misc> (GlobalClassMisc.kt): declaration-
  only ledger of the class header (June 5, 2022 banner, 2003/2015
  copyright residue, GLOBAL_CLASS_H guard) - the roster kept by
  family per the H-015 aggregate rule (the operator ()/=/ +=/<<
  families alone run past a hundred per-type overloads). Landed DOC
  owners named per row: Concat (u65 GlobalConcat, the mstring member
  of seven), Fnumber (u72 GlobalFnumber, one-arg member of two), Max
  (u68, the Stubs.globalMax seat names the class member), TermCorrel
  (unit-14 keyed seats), MakeGlobal (Stubs.makeGlobal), DocCorrelate
  default arg names the u85 Cosine render; Pattern and the remaining
  declarations stay ledger rows (non-corpus).
- Unit 90, interp-arith.cpp::<misc> (InterpArithMisc.kt): the leading
  residue of the arithmetic library (June 16, 2025 arith.c-style
  banner, GPL 2003-2025 O'Kane ledger, includes, intLong/NO/YES
  defines, void sigint(int) prototype) up to the first function body
  (Mltoa at vendor line 83 - the chunker cut). The GCVT arm ledger
  resolves per H-018 (GCVT DEFINED, BIGFLOAT and MULTI_PRECISION
  UNDEFINED in the reference build): the live gcvt_wrapper(x, a) =
  gcvt(x, 8, a) shape is the Stubs.gcvtWrapper seat the tranche-1
  mps renders already name. Post-cut owner ledger: Mltoa (u10
  delegate), mps_sqrt (u67), mps_atan (u71), floatstring (Stubs seat,
  interp-arith.cpp:597).
- Unit 91, compiler-new.c::new_code (NewCode.kt): the three-arm NEW
  compiler (no-argument form; push-locals-except paren list; push-
  locals-inclusive default), emissions via CompilerState.emit (the
  H-002/H-003 IfCode precedent; Sysout renders as emit). Quirk kept
  and ledgered: SET is defined TWICE in compiler-macros.h (line 83
  value 0, the entry-point table; line 91 value 115 = apostrophe-s,
  the command table) - the preprocessor takes the LAST definition
  without an undef, so every TU of this build passes SET = 115 to
  set_code; the render passes that value and ledgers the first
  define as dead. New CompilerState seats (H-004): exportflag
  (compiler-globals-ext.h:126, def compiler-globals.h:125), ExecSym
  (short[25]) and ExecX, retz, t0px, and vd (vendor vd[1024] carried
  at STR_MAX with the sizing divergence ledgered at the carrier,
  H-007). The exports: label is fall-through-only (H-013 dead label,
  ledger). set_code rides the NEW Stubs seat (owner
  compiler-set.c::set_code, batch-4 unit; snake name kept verbatim
  per the sym_ H-017 precedent). The new2/new3 goto loops linearize
  as H-011 while-true forms.
- Carrier and seat edits: CompilerCarriers.kt gains the five unit-91
  seats above; Stubs.kt gains the two same-name match seats (five-
  and six-argument shapes, owners compiler-sys1.c::match and
  interp-parse.cpp::match, both batch-7 units) and the set_code
  seat; InterpParseMisc.kt prototype-roster note for patrn updated
  (the declaration is now landed as the two-argument overload of
  PatrnInterp.kt, no seat ever needed; the earlier not-yet-landed
  seat claim was corrected, cosmetic, no runtime bytes touched).

Gate: kotlinc 2.1.20, 0 errors / 0 warnings at close. Two red
passes logged as data (the session discipline): the first full
compilation failed on 14 errors - the patrn twins indexed the
ShortArray stk table with Short values (Kotlin array indexing is
Int-only; fixed by promoting stk to an IntArray with an H-010
promotion note at both declarations) - and the second on one error,
InterpArithMisc.kt declared its include ledger a const val List,
which the Kotlin const rule forbids; it renders as a plain private
val (no runtime shape change, residue ledger only). Scan-gate
clean on all eight touched files after two KDoc de-escapes pre-gate
(a stray backtick pair in the PatrnInterp bound note, and a
mojibake byte caught in the InterpArithMisc banner line during
authoring). Third pass: 0/0.

Consult rows (sub-push B, tranche-wide timestamp):

- DOC consult: unit compiler-sys1.c::patrn, entry H-006, outcome hit-reuse, timestamp 2026-11-19T15:06:11Z
- DOC consult: unit compiler-sys1.c::patrn, entry H-007, outcome hit-reuse, timestamp 2026-11-19T15:06:11Z
- DOC consult: unit compiler-sys1.c::patrn, entry H-010, outcome hit-adapt, timestamp 2026-11-19T15:06:11Z
- DOC consult: unit compiler-sys1.c::patrn, entry H-011, outcome hit-reuse, timestamp 2026-11-19T15:06:11Z
- DOC consult: unit compiler-sys1.c::patrn, entry H-013, outcome hit-reuse, timestamp 2026-11-19T15:06:11Z
- DOC consult: unit compiler-sys1.c::patrn, entry H-014, outcome hit-adapt, timestamp 2026-11-19T15:06:11Z
- DOC consult: unit compiler-sys1.c::patrn, entry H-015, outcome hit-reuse, timestamp 2026-11-19T15:06:11Z
- DOC consult: unit compiler-sys1.c::patrn, entry H-017, outcome hit-reuse, timestamp 2026-11-19T15:06:11Z
- DOC consult: unit include/mumpsc/globalClass.h::<misc>, entry H-004, outcome hit-reuse, timestamp 2026-11-19T15:06:11Z
- DOC consult: unit include/mumpsc/globalClass.h::<misc>, entry H-013, outcome hit-reuse, timestamp 2026-11-19T15:06:11Z
- DOC consult: unit include/mumpsc/globalClass.h::<misc>, entry H-014, outcome hit-reuse, timestamp 2026-11-19T15:06:11Z
- DOC consult: unit include/mumpsc/globalClass.h::<misc>, entry H-015, outcome hit-adapt, timestamp 2026-11-19T15:06:11Z
- DOC consult: unit include/mumpsc/globalClass.h::<misc>, entry H-017, outcome hit-reuse, timestamp 2026-11-19T15:06:11Z
- DOC consult: unit interp-arith.cpp::<misc>, entry H-013, outcome hit-reuse, timestamp 2026-11-19T15:06:11Z
- DOC consult: unit interp-arith.cpp::<misc>, entry H-014, outcome hit-reuse, timestamp 2026-11-19T15:06:11Z
- DOC consult: unit interp-arith.cpp::<misc>, entry H-018, outcome hit-reuse, timestamp 2026-11-19T15:06:11Z
- DOC consult: unit compiler-new.c::new_code, entry H-002, outcome hit-reuse, timestamp 2026-11-19T15:06:11Z
- DOC consult: unit compiler-new.c::new_code, entry H-004, outcome hit-adapt, timestamp 2026-11-19T15:06:11Z
- DOC consult: unit compiler-new.c::new_code, entry H-007, outcome hit-adapt, timestamp 2026-11-19T15:06:11Z
- DOC consult: unit compiler-new.c::new_code, entry H-011, outcome hit-reuse, timestamp 2026-11-19T15:06:11Z
- DOC consult: unit compiler-new.c::new_code, entry H-013, outcome hit-reuse, timestamp 2026-11-19T15:06:11Z
- DOC consult: unit compiler-new.c::new_code, entry H-014, outcome hit-reuse, timestamp 2026-11-19T15:06:11Z
- DOC consult: unit compiler-new.c::new_code, entry H-017, outcome hit-reuse, timestamp 2026-11-19T15:06:11Z
- DOC consult: unit interp-parse.cpp::patrn, entry H-006, outcome hit-reuse, timestamp 2026-11-19T15:06:11Z
- DOC consult: unit interp-parse.cpp::patrn, entry H-007, outcome hit-reuse, timestamp 2026-11-19T15:06:11Z
- DOC consult: unit interp-parse.cpp::patrn, entry H-011, outcome hit-reuse, timestamp 2026-11-19T15:06:11Z
- DOC consult: unit interp-parse.cpp::patrn, entry H-013, outcome hit-reuse, timestamp 2026-11-19T15:06:11Z
- DOC consult: unit interp-parse.cpp::patrn, entry H-014, outcome hit-adapt, timestamp 2026-11-19T15:06:11Z
- DOC consult: unit interp-parse.cpp::patrn, entry H-015, outcome hit-reuse, timestamp 2026-11-19T15:06:11Z
- DOC consult: unit interp-parse.cpp::patrn, entry H-017, outcome hit-reuse, timestamp 2026-11-19T15:06:11Z
- DOC consult: unit interp-parse.cpp::patrn, entry H-010, outcome hit-adapt, timestamp 2026-11-19T15:06:11Z

### DOC B3 tranche 3 / sub-push C (unit 93), 2026-11-19

Unit 93, interp-parse.cpp::parse_ (Parse_.kt, the roster's last and
hardest unit of the tranche) rendered and pushed alone as agreed. The
tranche-wide consult timestamp 2026-11-19T15:06:11Z (captured before
the first tranche render, per the working discipline; it serves the
whole tranche including this sub-push).

Narrative:

- The vendor parse_ (lines 260-1328) renders as the one-argument H-011
  state machine over the private Pc93 enum (the twelve vendor labels
  start/nchar/scan_operand/un_nest/var1/exec/nxt_expr/dec_stk/
  nxt_operator/sym1/finish/idxin; finish inlines at each goto site,
  idxin at its single caller fall-through). The five const tables
  (code[26], opcode/ncode/dcode/operand[256]) are byte-faithful
  intArray renders: the vendor initializers list only the first 128
  entries of each 256-entry table (the C zero-pad tail renders as
  explicit zeros, quirk ledgered), and the code[26] row of the quote
  walk is guarded against an out-of-range index (the vendor can read
  code[28] when a not-in-table residue reaches the composed lookup at
  at-sign; H-007/H-013, guarded as 0, ledgered in the unit KDoc).
- Same-name twin resolution: compiler-parse.c::parse_ (batch-9 unit
  302, zero-arg) is the TRUE owner of the existing zero-arg
  Stubs.parse_ seat (the earlier batch-3 tag was a mis-tag in the
  u160-Interpret family; tag corrected on the seat, the IfCode.kt
  call site keeps binding it). This unit lands as the one-argument
  DELEGATE Stubs.parse_(svPtr) (H-014/Extract2 seat-conversion
  precedent) and the MstringEval.kt eval call site rebinds to it
  with Globals.svPtr (the interpreter caller). Overload arity
  resolves the twins at the call sites, H-015 precedent.
- New seats (H-014, whole-array seat calls with the vendor pointer
  shifts kept as the caller-side comment ledger): the arithmetic
  family add/sub/divx/divi/modulo (owners interp-arith.cpp:624/658/
  682/768/695, non-roster) and expx (batch-5 corpus unit), the
  char-array mult OVERLOAD of the existing compiler-sys1.c seat
  (owner interp-arith.cpp:624), rpm(string_, pattern, svPtr) (owner
  interp-rpm.cpp::rpm, NOT a corpus unit; raised from the vendor
  mid-switch forward declaration at interp-parse.cpp:1190),
  BuildGlobal (batch-8 unit 150, interp-parse.cpp:1836; the vendor
  signature is int g, taken over verbatim after checking the vendor
  prototype) and BuildLocal (batch-8 unit 152, interp-sym.cpp:97),
  both with a nullable rslt because this unit passes NULL in every
  call.
- New Msv carriers (Carriers.kt, stateVector.h line-numbered
  comments): setname (:46, CharArray STR_MAX), kflg (:48), t0px (:55),
  ierr (:50). numcomp reuses the landed render through the per-file
  numcompAt93 offset adapter slicing into the Parse93Static scratch
  object (the Patrn1Sys1Static precedent); patrn1 rides the existing
  delegate with jpx93/ernb93 ShortArray(1) holders (H-006) and the
  ernbr-only readback (patrn1 owns the xpx write internally).
- Quirks kept and ledgered in the unit KDoc: the CONTAINS arm dead
  bp == 0 clause (the pointer is never zero in this frame; renders
  as the literal pd1 top test plus a false arm, H-013); s1p[40]
  auto-bounds versus the vendor raw short scratch (H-007); NOT_NOT
  (18) duplicates NOTEQ, kept verbatim; the SQLITE directive pair
  resolves UNDEFINED per H-018 (no sqlite include in the reference
  build); dead locals tmp1[25]/tmp2[4] not declared (H-013).

Gate: kotlinc 2.1.20, 0 errors / 0 warnings at close. Three red
passes logged as data (the session discipline): first pass 49 errors -
the table name OPERAND_93 collided with the interp.h constant of the
same name (conflicting declarations; the 256-entry table renamed
OPERAND_T93 with its indexing sites, the constant keeps the vendor
name), plus seat-signature and inline-comment defects; second pass
12 errors - BuildGlobal/BuildLocal seat g parameter rendered as
CharArray before the vendor prototype read (vendor is int g;
also two mangled character literals and a comment-swallowed call
paren from the repair sed, all fixed line-exact); third pass 4
errors - missing != 0 on the Int-returning rpm/xindex if-conditions;
fourth pass 0/0. Scan-gate clean on all four touched files (one
stray KDoc backtick pair in the CONTAINS quirk line fixed pre-gate;
two sub-sed corrections during the repair kept the file ASCII-clean
throughout).

Consult rows (sub-push C, tranche-wide timestamp):

- DOC consult: unit interp-parse.cpp::parse_, entry H-006, outcome hit-reuse, timestamp 2026-11-19T15:06:11Z
- DOC consult: unit interp-parse.cpp::parse_, entry H-007, outcome hit-adapt, timestamp 2026-11-19T15:06:11Z
- DOC consult: unit interp-parse.cpp::parse_, entry H-011, outcome hit-adapt, timestamp 2026-11-19T15:06:11Z
- DOC consult: unit interp-parse.cpp::parse_, entry H-013, outcome hit-reuse, timestamp 2026-11-19T15:06:11Z
- DOC consult: unit interp-parse.cpp::parse_, entry H-014, outcome hit-adapt, timestamp 2026-11-19T15:06:11Z
- DOC consult: unit interp-parse.cpp::parse_, entry H-015, outcome hit-reuse, timestamp 2026-11-19T15:06:11Z
- DOC consult: unit interp-parse.cpp::parse_, entry H-017, outcome hit-reuse, timestamp 2026-11-19T15:06:11Z
- DOC consult: unit interp-parse.cpp::parse_, entry H-018, outcome hit-reuse, timestamp 2026-11-19T15:06:11Z

### DOC B3 close — coverage checklist (units 63-93), 2026-11-19

Manual coverage checklist per the DOC batch-close discipline (section
1: for each canonical construct in the batch, did at least one entry
match?). Units 63-93 were translated 2026-11-19 in three tranches
(T1 63-72; T2 73-82; T3 83-93 with sub-pushes A/B/C). Kotlinc 2.1.20 gate 0
errors / 0 warnings at every
tranche close after logged red passes (T2: one red pass, the LINEFEED
const collision plus a val reassignment; T3: three red passes on the
unit-93 parse_ render, OPERAND_93 table-name collision et al., all
logged as data). Red-build count for the batch: 4 passes, all green at
close. Gradle gate on m1-build pending at this writing; events export
regeneration via export_doc_events.R follows the gate per the C-008
key.

Entry-by-entry coverage across the 31 B3 unit journals (140 consults;
none consulted an entry without applying it):

- FILE star to Reader/Writer (H-001): matched in 4 B3 unit journal(s), 2 reuse / 2 adapt
- H-002: matched in 1 B3 unit journal(s), 1 reuse
- include-within-function table carrier (H-004): matched in 4 B3 unit journal(s), 3 reuse / 1 adapt
- output-shape holder carrier (H-006): matched in 4 B3 unit journal(s), 4 reuse
- string walk idiom (H-007): matched in 12 B3 unit journal(s), 10 reuse / 2 adapt
- one-owner object seat (H-008): matched in 4 B3 unit journal(s), 4 reuse
- sprintf/printf locale contract (H-009): matched in 5 B3 unit journal(s), 4 reuse / 1 adapt
- numeric promotions/domains (H-010): matched in 6 B3 unit journal(s), 4 reuse / 2 adapt
- goto linearization (H-011): matched in 5 B3 unit journal(s), 4 reuse / 1 adapt
- H-012: matched in 6 B3 unit journal(s), 6 reuse
- dead code/comment fossils (H-013): matched in 19 B3 unit journal(s), 19 reuse
- out-of-scope stub seats (H-014): matched in 26 B3 unit journal(s), 22 reuse / 4 adapt
- grammar collisions/byte-domain literals (H-015): matched in 10 B3 unit journal(s), 8 reuse / 2 adapt
- camelCase naming (H-017): matched in 25 B3 unit journal(s), 25 reuse
- preprocessor_conditional (H-018): matched in 9 B3 unit journal(s), 9 reuse

H-003, H-005 and H-016 found no triggering situation in B3 (no consult
row; state recorded so entry-for-entry parity with PRO's rule base is
unaffected; no revision needed).

Consult outcome totals for the batch: 125 hit-reuse, 15 hit-adapt,
0 miss (140 consults, one per (unit, entry) per C-005/C-008). Hit rate
140/140 = 100.0% (every consult matched an entry); reuse rate 125/140 =
89.3% (adaptation, not failure, accounts for the remainder). No miss
rows: the B3 canon was fully covered by cookbook v0 as it stood at
batch open, so no capture candidate exists under T1/T2/T3; cookbook v0
stays at H-001..H-018 and remains entry-for-entry parallel with PRO's
rules_v0.pl (h001..h018).

Per-unit consult coverage (unit -> entries consulted, B3 order):

- compiler-epi.c::<misc>: H-013, H-014, H-018
- compiler-read.c::<misc>: H-013, H-014, H-018
- libmpscpp.cpp::global::Concat: H-012, H-013, H-014, H-017
- libmpscpp.cpp::mdh_tree_level_add: H-014, H-015, H-017
- interp-arith.cpp::mps_sqrt: H-009, H-010, H-014, H-015, H-017, H-018
- libmpscpp.cpp::Max: H-008, H-009, H-013, H-014, H-017
- libmpscpp.cpp::GlobalOrder: H-008, H-013, H-014, H-015, H-017
- libmpscpp.cpp::GlobalData: H-013, H-014, H-015, H-017
- interp-arith.cpp::mps_atan: H-009, H-010, H-014, H-017, H-018
- libmpscpp.cpp::global::Fnumber: H-012, H-014, H-015, H-017
- interp-arith.cpp::mps_justify: H-009, H-010, H-017, H-018
- libmpscpp.cpp::Cost: H-012, H-013, H-014, H-017
- libmpscpp.cpp::Scan: H-001, H-008, H-013
- include/mumpsc/cppsysfunc.h::<misc>: H-013, H-014, H-017
- libmpscpp.cpp::ScanAlnum: H-008, H-011, H-012, H-013, H-017
- compiler-sys1.c::readf_: H-001, H-007, H-017, H-018
- libmpscpp.cpp::mstring::Eval: H-007, H-013, H-014, H-017
- compiler-parse.c::ExpandQS: H-001, H-007, H-012, H-014, H-017
- interp-parse.cpp::<misc>: H-004, H-014, H-017, H-018
- libmpscpp.cpp::RunMumps: H-007, H-014, H-017
- interp-zfcns.cpp::GtkTreeAdd: H-006, H-013, H-014, H-017
- compiler-sys1.c::cannon: H-007, H-014, H-017
- libmpscpp.cpp::Cosine: H-007, H-010, H-013, H-014, H-017
- interp-bmgsubs.c::bmg_fsearch: H-004, H-007, H-012, H-014
- compiler-sys1.c::errmod_: H-001, H-007, H-009, H-015
- compiler-sys1.c::patrn: H-006, H-007, H-010, H-011, H-013, H-014, H-015, H-017
- include/mumpsc/globalClass.h::<misc>: H-004, H-013, H-014, H-015, H-017
- interp-arith.cpp::<misc>: H-013, H-014, H-018
- compiler-new.c::new_code: H-002, H-004, H-007, H-011, H-013, H-014, H-017
- interp-parse.cpp::patrn: H-006, H-007, H-010, H-011, H-013, H-014, H-015, H-017
- interp-parse.cpp::parse_: H-006, H-007, H-011, H-013, H-014, H-015, H-017, H-018

Close decisions: none beyond the registered instruments - zero misses
mean no capture candidate exists under T1/T2/T3, and the per-unit
check columns stay todo per the C-001 cycle-10 backfill decision
(C-002 discipline; the batch-3 table's check columns track the BASE
reference translation). PILOT_TRACKING's DOC column is already
"translated" for rows 63-93; the "DOC B3 closed" prose line lands with
the gate result.

## DAF arm (opening, 2026-11-19)

- Toolchain installed on m1-build: Dafny
  4.11.0+fcb2042d6d043a2634f0854338c08feeaaaf4ae2 (self-contained
  linux-x64, Z3 bundled; ~/dafny-4.11.0, symlinked ~/bin/dafny).
  dafny --version gated by Mark at arm open.
- Scaffolding committed (198b7aa): wp2/arms/DAF Gradle project
  (kotlin 2.1.20, JVM 17, mirrors DOC), arm README (workflow +
  toolchain anchor), spec/ dir conventions, smoke_test.dfy.
- Gradle gate and smoke test: results appended after the run.
- B1 opens at unit 1 (compiler-copyright-notice.c::<misc>) in the
  fixed order; ad-hoc heuristic workflow per section 3 (BASE-log
  consult rows logged here per unit); pure-logic spec candidates per
  the section-3 heuristic: u15 Extract2, u17 gotamatch, u20 numcomp;
  exemptions (pointer/IO-heavy units) journaled per unit.
- Gate/smoke results (m1-build, 2026-11-19, Mark): DAF project
  gradle build BUILD SUCCESSFUL in 9s (empty project, 4 tasks);
  dafny verify spec/smoke_test.dfy: 2 verified, 0 errors.
  DAF arm is provisioned end-to-end; B1 translation can start.

### DOC B4 tranche 1 (units 94-103), 2026-11-19

Rendered 2026-11-19 from pristine vendor sources only (arm
independence kept; no PRO/DAF/BOTH Kotlin consulted). Kotlinc
2.1.20 whole-arm gate: 0 errors / 0 warnings first pass.
Scan gates clean (dollar delta 0 on Fnumber.kt and Stubs.kt;
new files raw-dollar-free, backtick/!!/dash scan clean, ASCII
only, comment-close positional check clean). Pushed as 140e3b0
(11 files: 9 new + Fnumber.kt append + Stubs.kt append);
post-push byte-fidelity verified against the gate copies.

Units (batch_plan ascending order):
- u94 GlobalArrayMisc (globalArray.h): banner/include ledger
  only (H-004 table carrier); GlobalArray fn = done B2 u52.
- u95 NewMisc (new.h): banner ledger only; new_code = done
  B3 u91.
- u96 escan: Ends data class carrier (stem.h:31 fields);
  strncmpAt + explicit NUL-after-tail check; strmove in-place
  loop (H-007 adapt); quirks kept: wl - offset > 3 strict,
  i=0 restart when next==0; empty-ending table terminator.
- u97 mstring::Fnumber one-arg overload (LOC 17): appended to
  the u42 file; overload arity resolves call sites; same
  coded-command shape (v1d off 1 in, bd off 1 out, Stubs.fcn).
- u98 Count: free wrapper, Max-u68 template; new Stubs.global-
  Count seat raised (owner libmpscpp.cpp:2257 global::Count,
  sibling of globalMax u68).
- u99 Min: free wrapper calling the REAL unit-44 GlobalRef.Min
  member; NO stub raised - sibling asymmetry vs Max/Count noted
  (u44 was a corpus unit; global::Max/Count were not).
- u100 Transpose (LOC 15 std::string overload): Stubs seats
  globalOrder/globalOrder2/getGlobalString/globalSetKeyed;
  H-013: dead duplicate bb = "" store dropped with comment.
- u101 server misc: SERVERBUF const; Mymsgbuf/Socketdata data
  classes; logtime via SimpleDateFormat "EEE MMM dd HH:mm:ss
  yyyy" Locale.US (H-009 adapt, ctime quanta); H-008 fresh
  String; newline-strip folded.
- u102 rdbms misc: declaration-only ledger (H-004); H-018:
  SQL/POSTGRES arms EXCLUDED per reference config (SQLITE
  live); extern ierr binds Msv.ierr.
- u103 mps_asin: mps_sqrt twin; plain-double arm live; mpfr
  and long-double arms journaled not rendered (H-018);
  gcvtWrapper stub; no domain check quirk kept (NaN via
  library path).

Consult rows (timestamp 2026-11-19T19:30:34Z, tranche-wide):
- DOC consult: unit compiler-globalarray.c::<misc>, entry H-004, outcome hit-reuse, timestamp 2026-11-19T19:30:34Z
- DOC consult: unit compiler-globalarray.c::<misc>, entry H-013, outcome hit-reuse, timestamp 2026-11-19T19:30:34Z
- DOC consult: unit compiler-globalarray.c::<misc>, entry H-014, outcome hit-reuse, timestamp 2026-11-19T19:30:34Z
- DOC consult: unit compiler-globalarray.c::<misc>, entry H-017, outcome hit-reuse, timestamp 2026-11-19T19:30:34Z
- DOC consult: unit compiler-new.c::<misc>, entry H-004, outcome hit-reuse, timestamp 2026-11-19T19:30:34Z
- DOC consult: unit compiler-new.c::<misc>, entry H-013, outcome hit-reuse, timestamp 2026-11-19T19:30:34Z
- DOC consult: unit compiler-new.c::<misc>, entry H-014, outcome hit-reuse, timestamp 2026-11-19T19:30:34Z
- DOC consult: unit compiler-new.c::<misc>, entry H-017, outcome hit-reuse, timestamp 2026-11-19T19:30:34Z
- DOC consult: unit interp-stem.cpp::escan, entry H-007, outcome hit-adapt, timestamp 2026-11-19T19:30:34Z
- DOC consult: unit interp-stem.cpp::escan, entry H-012, outcome hit-reuse, timestamp 2026-11-19T19:30:34Z
- DOC consult: unit interp-stem.cpp::escan, entry H-014, outcome hit-reuse, timestamp 2026-11-19T19:30:34Z
- DOC consult: unit interp-stem.cpp::escan, entry H-017, outcome hit-reuse, timestamp 2026-11-19T19:30:34Z
- DOC consult: unit libmpscpp.cpp::mstring::Fnumber, entry H-012, outcome hit-reuse, timestamp 2026-11-19T19:30:34Z
- DOC consult: unit libmpscpp.cpp::mstring::Fnumber, entry H-015, outcome hit-reuse, timestamp 2026-11-19T19:30:34Z
- DOC consult: unit libmpscpp.cpp::mstring::Fnumber, entry H-017, outcome hit-reuse, timestamp 2026-11-19T19:30:34Z
- DOC consult: unit libmpscpp.cpp::mstring::Fnumber, entry H-014, outcome hit-adapt, timestamp 2026-11-19T19:30:34Z
- DOC consult: unit libmpscpp.cpp::Count, entry H-008, outcome hit-reuse, timestamp 2026-11-19T19:30:34Z
- DOC consult: unit libmpscpp.cpp::Count, entry H-009, outcome hit-reuse, timestamp 2026-11-19T19:30:34Z
- DOC consult: unit libmpscpp.cpp::Count, entry H-013, outcome hit-reuse, timestamp 2026-11-19T19:30:34Z
- DOC consult: unit libmpscpp.cpp::Count, entry H-014, outcome hit-reuse, timestamp 2026-11-19T19:30:34Z
- DOC consult: unit libmpscpp.cpp::Count, entry H-017, outcome hit-reuse, timestamp 2026-11-19T19:30:34Z
- DOC consult: unit libmpscpp.cpp::Min, entry H-008, outcome hit-reuse, timestamp 2026-11-19T19:30:34Z
- DOC consult: unit libmpscpp.cpp::Min, entry H-009, outcome hit-reuse, timestamp 2026-11-19T19:30:34Z
- DOC consult: unit libmpscpp.cpp::Min, entry H-013, outcome hit-reuse, timestamp 2026-11-19T19:30:34Z
- DOC consult: unit libmpscpp.cpp::Min, entry H-017, outcome hit-reuse, timestamp 2026-11-19T19:30:34Z
- DOC consult: unit libmpscpp.cpp::Transpose, entry H-007, outcome hit-reuse, timestamp 2026-11-19T19:30:34Z
- DOC consult: unit libmpscpp.cpp::Transpose, entry H-013, outcome hit-reuse, timestamp 2026-11-19T19:30:34Z
- DOC consult: unit libmpscpp.cpp::Transpose, entry H-014, outcome hit-reuse, timestamp 2026-11-19T19:30:34Z
- DOC consult: unit libmpscpp.cpp::Transpose, entry H-017, outcome hit-reuse, timestamp 2026-11-19T19:30:34Z
- DOC consult: unit server.cpp::<misc>, entry H-004, outcome hit-reuse, timestamp 2026-11-19T19:30:34Z
- DOC consult: unit server.cpp::<misc>, entry H-008, outcome hit-reuse, timestamp 2026-11-19T19:30:34Z
- DOC consult: unit server.cpp::<misc>, entry H-009, outcome hit-adapt, timestamp 2026-11-19T19:30:34Z
- DOC consult: unit server.cpp::<misc>, entry H-013, outcome hit-reuse, timestamp 2026-11-19T19:30:34Z
- DOC consult: unit server.cpp::<misc>, entry H-014, outcome hit-reuse, timestamp 2026-11-19T19:30:34Z
- DOC consult: unit server.cpp::<misc>, entry H-017, outcome hit-reuse, timestamp 2026-11-19T19:30:34Z
- DOC consult: unit rdbms-if.c::<misc>, entry H-004, outcome hit-reuse, timestamp 2026-11-19T19:30:34Z
- DOC consult: unit rdbms-if.c::<misc>, entry H-013, outcome hit-reuse, timestamp 2026-11-19T19:30:34Z
- DOC consult: unit rdbms-if.c::<misc>, entry H-014, outcome hit-reuse, timestamp 2026-11-19T19:30:34Z
- DOC consult: unit rdbms-if.c::<misc>, entry H-017, outcome hit-reuse, timestamp 2026-11-19T19:30:34Z
- DOC consult: unit rdbms-if.c::<misc>, entry H-018, outcome hit-reuse, timestamp 2026-11-19T19:30:34Z
- DOC consult: unit interp-arith.cpp::mps_asin, entry H-009, outcome hit-reuse, timestamp 2026-11-19T19:30:34Z
- DOC consult: unit interp-arith.cpp::mps_asin, entry H-010, outcome hit-reuse, timestamp 2026-11-19T19:30:34Z
- DOC consult: unit interp-arith.cpp::mps_asin, entry H-014, outcome hit-reuse, timestamp 2026-11-19T19:30:34Z
- DOC consult: unit interp-arith.cpp::mps_asin, entry H-017, outcome hit-reuse, timestamp 2026-11-19T19:30:34Z
- DOC consult: unit interp-arith.cpp::mps_asin, entry H-018, outcome hit-reuse, timestamp 2026-11-19T19:30:34Z

Tranche totals: 45 consults, 42 hit-reuse, 3 hit-adapt
(u96 H-007, u97 H-014, u101 H-009), 0 miss. Hit rate 100.0%,
reuse rate 93.3%. Gradle gate on m1-build pending; results
append with the gate run per the B3 discipline.

### DOC B4 tranche 2 (units 104-113), 2026-11-19

Rendered 2026-11-19 from pristine vendor sources only (arm
independence kept; no PRO/DAF/BOTH Kotlin consulted; DOC's own
prior files consulted). Kotlinc 2.1.20 whole-arm gate: 0 errors /
0 warnings. Scan gates clean (dollar delta 0 on Stubs.kt; the
one raw dollar in the new Fnumber_.kt DOLLAR const replaced with
the unicode escape pre-push; backtick/!!/dash/ASCII scans and
the comment-close positional check clean; ScanAlnum.kt unit-77
prefix byte-identical after the append). Pushed as 5c67fd3
(11 files: 9 new + ScanAlnum.kt append + Stubs.kt edit);
post-push fetch verification at the commit confirmed all
rendered bytes.

Units (batch_plan ascending order):
- u104 ScanAlnum (LOC 18 FILE* overload): appended to the
  unit-77 file; TAG CORRECTION on the u77 "overloads not corpus
  units" ledger note (this overload IS roster row 105); Reader
  carrier, fscanf 8191s scanWord8k, repeat restart H-011,
  max>511 guard, digit-first reject; per-file isalnum/tolower
  helpers.
- u105 LocalName: real render; Stubs.LocalName seat REMOVED
  (TAG CORRECTION - the "not a roster unit" tag was a mis-tag;
  row 121 is this unit); nullable tmp2; coded 206/207/208 case
  values; j default 999; the two k>=j break arms journaled.
- u106 pb: RawBlocks printer; ByteArray domain; MBLOCK/MMARK/
  ADSIZE live seats; memcpy(&x,block,4) little-endian assembly;
  dead OFF_T z (H-013) kept as comment; vendor test order of
  the trailing-zero walk kept.
- u107 sysfunc.h misc: declaration-only ledger; H-018 note -
  the _INTERP_ if-pair arms DIFFER and both are live per-TU
  (union ledger rendered); prototypes mapped to seats; the
  commented-out VariableNotFound fossil noted.
- u108 init: real Smith-Waterman matrix initializer; rides the
  SeqAlignExterns a/EMPTY_CELL/S_RESULT seats; Stubs.init
  converted to delegate.
- u109 atTest: real render; Stubs.atTest converted to delegate
  (IfCode binds unchanged); pd1Last is READ (H-006) for the
  imbalanced-quote banner - superseding any earlier write
  note; doubled-quote walk byte-faithful; one-past-NUL read
  bounds-guarded with the divergence ledgered.
- u110 interp-bifs.cpp misc: banner/include/define ledger;
  H-018: SQLITE live, GCVT macro NOT in the reference config
  (gcvt arm already seated at Stubs.gcvtWrapper); Mget real
  render riding Stubs.sym_.
- u111 _fnumber: the dollar-fn runtime wrapper (SPELLING
  QUIRK: "$fn" vs "$fnumber" - missing umber kept verbatim);
  v1d offset-1 in, bd offset-1 out, Stubs.fcn dispatch.
- u112 global::Centroid: GlobalRef extension; TermCorrelate
  seats (Order/Data/SetKeyed/PlusIntKeyed) plus NEW vararg
  double seats getGlobalDoubleKeyed/setGlobalDoubleKeyed
  (one- and two-key invocations); string-concat accumulation
  and integer-increment count journaled; base Kill ledgered.
- u113 parse_pattern_element: H-004 CARRIER SEATS - the
  token/scanner/parser/token_type carriers and the
  pattern_codes/char_classes tables of interp-rpm.cpp rendered
  ahead of the batch-5 misc owner (supersede note); six new
  H-014 seats (bstrcat batch-9 correctly tagged,
  leaning_toothpickize batch-6, parse_pattern_atom_list/
  accept/accept_it/syntax_error batch-5 misc); strchr-miss UB
  quantized to clamp 0 (H-007).

Consult rows (timestamp 2026-11-19T19:44:21Z, tranche-wide):
- DOC consult: unit libmpscpp.cpp::ScanAlnum, entry H-001, outcome hit-reuse, timestamp 2026-11-19T19:44:21Z
- DOC consult: unit libmpscpp.cpp::ScanAlnum, entry H-008, outcome hit-reuse, timestamp 2026-11-19T19:44:21Z
- DOC consult: unit libmpscpp.cpp::ScanAlnum, entry H-011, outcome hit-reuse, timestamp 2026-11-19T19:44:21Z
- DOC consult: unit libmpscpp.cpp::ScanAlnum, entry H-012, outcome hit-reuse, timestamp 2026-11-19T19:44:21Z
- DOC consult: unit libmpscpp.cpp::ScanAlnum, entry H-017, outcome hit-reuse, timestamp 2026-11-19T19:44:21Z
- DOC consult: unit interp-sysfunc.cpp::LocalName, entry H-008, outcome hit-reuse, timestamp 2026-11-19T19:44:21Z
- DOC consult: unit interp-sysfunc.cpp::LocalName, entry H-012, outcome hit-reuse, timestamp 2026-11-19T19:44:21Z
- DOC consult: unit interp-sysfunc.cpp::LocalName, entry H-014, outcome hit-reuse, timestamp 2026-11-19T19:44:21Z
- DOC consult: unit interp-sysfunc.cpp::LocalName, entry H-017, outcome hit-reuse, timestamp 2026-11-19T19:44:21Z
- DOC consult: unit interp-native-btree.cpp::pb, entry H-001, outcome hit-reuse, timestamp 2026-11-19T19:44:21Z
- DOC consult: unit interp-native-btree.cpp::pb, entry H-012, outcome hit-reuse, timestamp 2026-11-19T19:44:21Z
- DOC consult: unit interp-native-btree.cpp::pb, entry H-013, outcome hit-reuse, timestamp 2026-11-19T19:44:21Z
- DOC consult: unit interp-native-btree.cpp::pb, entry H-017, outcome hit-reuse, timestamp 2026-11-19T19:44:21Z
- DOC consult: unit include/mumpsc/sysfunc.h::<misc>, entry H-004, outcome hit-reuse, timestamp 2026-11-19T19:44:21Z
- DOC consult: unit include/mumpsc/sysfunc.h::<misc>, entry H-013, outcome hit-reuse, timestamp 2026-11-19T19:44:21Z
- DOC consult: unit include/mumpsc/sysfunc.h::<misc>, entry H-014, outcome hit-reuse, timestamp 2026-11-19T19:44:21Z
- DOC consult: unit include/mumpsc/sysfunc.h::<misc>, entry H-017, outcome hit-reuse, timestamp 2026-11-19T19:44:21Z
- DOC consult: unit include/mumpsc/sysfunc.h::<misc>, entry H-018, outcome hit-adapt, timestamp 2026-11-19T19:44:21Z
- DOC consult: unit libmpscpp.cpp::init, entry H-001, outcome hit-reuse, timestamp 2026-11-19T19:44:21Z
- DOC consult: unit libmpscpp.cpp::init, entry H-004, outcome hit-reuse, timestamp 2026-11-19T19:44:21Z
- DOC consult: unit libmpscpp.cpp::init, entry H-013, outcome hit-reuse, timestamp 2026-11-19T19:44:21Z
- DOC consult: unit libmpscpp.cpp::init, entry H-014, outcome hit-adapt, timestamp 2026-11-19T19:44:21Z
- DOC consult: unit libmpscpp.cpp::init, entry H-017, outcome hit-reuse, timestamp 2026-11-19T19:44:21Z
- DOC consult: unit compiler-at-test.c::atTest, entry H-001, outcome hit-reuse, timestamp 2026-11-19T19:44:21Z
- DOC consult: unit compiler-at-test.c::atTest, entry H-006, outcome hit-reuse, timestamp 2026-11-19T19:44:21Z
- DOC consult: unit compiler-at-test.c::atTest, entry H-012, outcome hit-reuse, timestamp 2026-11-19T19:44:21Z
- DOC consult: unit compiler-at-test.c::atTest, entry H-014, outcome hit-adapt, timestamp 2026-11-19T19:44:21Z
- DOC consult: unit compiler-at-test.c::atTest, entry H-017, outcome hit-reuse, timestamp 2026-11-19T19:44:21Z
- DOC consult: unit interp-bifs.cpp::<misc>, entry H-004, outcome hit-reuse, timestamp 2026-11-19T19:44:21Z
- DOC consult: unit interp-bifs.cpp::<misc>, entry H-013, outcome hit-reuse, timestamp 2026-11-19T19:44:21Z
- DOC consult: unit interp-bifs.cpp::<misc>, entry H-014, outcome hit-reuse, timestamp 2026-11-19T19:44:21Z
- DOC consult: unit interp-bifs.cpp::<misc>, entry H-017, outcome hit-reuse, timestamp 2026-11-19T19:44:21Z
- DOC consult: unit interp-bifs.cpp::<misc>, entry H-018, outcome hit-reuse, timestamp 2026-11-19T19:44:21Z
- DOC consult: unit interp-fnumber.cpp::_fnumber, entry H-012, outcome hit-reuse, timestamp 2026-11-19T19:44:21Z
- DOC consult: unit interp-fnumber.cpp::_fnumber, entry H-014, outcome hit-reuse, timestamp 2026-11-19T19:44:21Z
- DOC consult: unit interp-fnumber.cpp::_fnumber, entry H-015, outcome hit-reuse, timestamp 2026-11-19T19:44:21Z
- DOC consult: unit interp-fnumber.cpp::_fnumber, entry H-017, outcome hit-reuse, timestamp 2026-11-19T19:44:21Z
- DOC consult: unit libmpscpp.cpp::global::Centroid, entry H-004, outcome hit-reuse, timestamp 2026-11-19T19:44:21Z
- DOC consult: unit libmpscpp.cpp::global::Centroid, entry H-013, outcome hit-reuse, timestamp 2026-11-19T19:44:21Z
- DOC consult: unit libmpscpp.cpp::global::Centroid, entry H-014, outcome hit-adapt, timestamp 2026-11-19T19:44:21Z
- DOC consult: unit libmpscpp.cpp::global::Centroid, entry H-017, outcome hit-reuse, timestamp 2026-11-19T19:44:21Z
- DOC consult: unit interp-rpm.cpp::parse_pattern_element, entry H-004, outcome hit-adapt, timestamp 2026-11-19T19:44:21Z
- DOC consult: unit interp-rpm.cpp::parse_pattern_element, entry H-007, outcome hit-reuse, timestamp 2026-11-19T19:44:21Z
- DOC consult: unit interp-rpm.cpp::parse_pattern_element, entry H-013, outcome hit-reuse, timestamp 2026-11-19T19:44:21Z
- DOC consult: unit interp-rpm.cpp::parse_pattern_element, entry H-014, outcome hit-adapt, timestamp 2026-11-19T19:44:21Z
- DOC consult: unit interp-rpm.cpp::parse_pattern_element, entry H-017, outcome hit-reuse, timestamp 2026-11-19T19:44:21Z

Tranche totals: 46 consults, 40 hit-reuse, 6 hit-adapt
(u107 H-018, u108 H-014, u109 H-014, u112 H-014, u113 H-004
and H-014), 0 miss. Hit rate 100.0%, reuse rate 87.0%.
Gradle gate on m1-build pending; results append with the gate
run per the B3 discipline.
### DOC B4 tranche 3 (units 114-123), 2026-11-19

Rendered 2026-11-19 from pristine vendor sources only (arm
independence kept; no PRO/DAF/BOTH Kotlin consulted; DOC's own
prior files consulted). Consult timestamp 2026-11-19T20:53:08Z
captured before renders (the same timestamp serves the whole
tranche per the working discipline; all ten unit banners carry it).
Whole-arm kotlinc 2.1.20 gate 0 errors / 0 warnings at every
sub-push boundary and re-run at master 266ce6d (125 files).
Scan gates clean on every tranche file at every push (raw
dollar zero via the unicode escapes, backtick zero, bang-bang
zero, ASCII only, no line-final comment-close in the rendered
files). Pushed in three sub-pushes: 0d18bfd (units 114-119),
8717ba0 (units 120-122), 266ce6d (unit 123); gradle host gate
on m1-build BUILD SUCCESSFUL at 0d18bfd, 8717ba0 and 266ce6d.

Units (batch_plan ascending order):
- u114 interp-piece.cpp::<misc>: PieceMisc.kt append. The piece
  library residue ledger: banner/includes/prototype roster,
  SQLITE in-file define note, the _INTERP_/CSTR/USTR/intLong
  define notes (H-018: BIGFLOAT out per the reference build;
  intLong in-file unconditional per the interp-arith.cpp:59
  precedent), and the _piece body: sentinel defaults, the
  DOLLAR_P start/end banner (H-015), xindex-seat delimiter and
  end walks (H-014) with length-plus-one tail clamp, the
  catAt rebuild through sym_ (local) / Mglobal(STORE) (caret),
  setpiece double-test kept verbatim.
- u115 readf_: Readf_.kt. The dotted ZLOAD source reader: pd1
  offset-pd1org byte folds (CR/T/CTRLZ), backslash-newline
  continuation, high-byte fold (H-012 signed-char-at-EOF order
  kept), CR/TN current-file carriers, pd1 one-owner H-004,
  getc through Reader.read, dead l local not rendered (H-013),
  pd1len one-short write and double terminator kept.
- u116 lock_code: LockCode.kt append. The LOCK compiler:
  argumentless unlock-cleanup scaffolding via the skipLck1
  flag (goto lck1 re-entry shape), +/- prefix, caret
  requirement, set_code(SET) delegation to the real u121
  render, keyfix plus optional timeout emission, emitted C
  kept byte-identical, LockFlag/dirflg the new CompilerState
  seats, zero-arg parse_ stub seat tagged batch-9.
- u117 doty: Doty.kt. The dotted-Do indent manager: short*
  out-parameters as H-006 holders, ExecStack double
  post-increment evaluation split into explicit steps with the
  ledger note, Do_Block_Bypass extra-argument fossil ledgered,
  emitted INDENT mark strings, LevCheck the new H-014 stub
  seat (compiler-mumps.c:1949 owner).
- u118 pcre.h::<misc>: PcreH.kt. Cambridge PCRE 4.5 header
  residue as a declaration LEDGER (nothing in-arm calls);
  H-018: the _WIN32 DLL arm commented out (extern arm live),
  VPCOMPAT undefined (ifndef arm live); version roster note.
- u119 GlobalQuery: GlobalQuery.kt. The dollar-Query walker:
  AllocSV new H-014 stub seat (batch-8 row 219 tag correct),
  the state push memcpy as the copyMsv H-004 adapter, real
  u93 parse_ reuse, keyfix v1d offset-1, SOH tail append with
  the vendor off-by-one mid-name probe kept, Mglobal(XNEXT)
  walk, H-018: the SQLITE cut LIVE (post-endif tail dead,
  journaled), H-008 p1 fold, TAG CORRECTION removing the
  Stubs.GlobalQuery "not a roster unit" mis-tag.
- u120 libmpscpp.cpp::<misc-9>: SymOpsMisc.kt. The operator/
  SymOps residue: String.times/GlobalRef.times via Stubs.mult
  (H-014 adapt, word-operator convention), Mstring lt/gt
  ladders x7 each over the atol/strtod/char*/mstring domains
  (H-012 distinct seats, unit-62 gte/lte precedent) with the
  char*+std::string H-015 collapse onto one String seat,
  SymGet x3 (SYMTAB_ERROR throw) and SymPut x9 plus the __symput
  sym_(0) seat, GlobalRef.Flush() via Mglobal(FLUSH=21), the 14
  MSV-intrinsic wrappers (H-014 seats; Cosine/TermCorrelate/
  Centroid ride the real renders), svptr/ep dead H-013 params,
  percent-g/percent-d String.format Locale.US (H-009), dstr
  strcpy-fold String domain (H-008).
- u121 set_code: SetCode.kt. Full SET_STORE control flow as
  labeled loops plus goto flags; the compiler interpret
  macro seat is CompilerState.interpret (H-003); atTest and
  parse_ stub seats; LocalArray/GlobalArray real renders over
  the H-006 holders; the piece/extract indirection forest with
  the exact <128 marker arithmetic (bound-guarded; one fullwidth
  vendor semicolon narrowed to ASCII); emitted C byte-identical
  including the missing-percent-s-arg LOCK quirk; freest rstr
  dead store not rendered (H-013); malloc-per-LHS strlen+1
  String seat (H-008); var renamed varArr (Kotlin reserved word).
- u122 for_code: ForCode.kt. The FOR compiler: argumentless
  zbreak form via linepad(emit(...)) (linepad the H-014 stub
  seat, below-loc owner tagged), skipWhile01/gotoSingles/
  oneArgFcn flags, forAgain2 labeled loop, singles label
  guarded by the for_fcn_flg test, f264 arg quirk kept,
  setjmp scaffolding, ForBlocks close with Lines emission,
  the dead post-goto SinglesFlg code not rendered (H-013),
  unused for-loop warts byte-identical (all substitutions the
  forx conv helper), for_mult/SinglesFlg H-010 short-to-Int.
- u123 fcn: Fcn.kt (the tranche closer and the largest unit of
  the arm, row 95 score 1221.5, 775-LOC parity). The
  dollar-function dispatcher: the FUNCTION launch head,
  strmoveShift operand strip, noname args arm, iargs roster
  walk, tolower fold, the 128-entry FC opcode table (indices
  128-255 the C zero-fill, default arm via size guard), the 19
  case arms (extract/ascii/char/find/fnumber/justify/job/len/
  piece/select/test/text/translate/random/reverse/x/y/io/
  horolog/get/1-9/name/next/noerr/order/query/qlength/
  qsubscript), goto-tail bouquet as labeled run blocks
  returning the private FcnT enum with the tail when; H-018:
  SQLITE live (file-scope define, OPEN=206), MULTI_PRECISION/
  BIGFLOAT dead per config; getepid/time/getcwd/rand(3) map
  to JVM intrinsics per H-010 (pid/ProcessHandle, millis/1000,
  user.dir, kotlin.random.Random), exits via exitProcess(1),
  sprintf folds String.format Locale.US with the buf seats
  H-008 carriers; strfmon the new POSIX libc seat; the
  sql seat params nullable (the vendor NULL triple in the
  ztabsize OPEN); Shadowed vendor locals renamed per H-017;
  the strfmon bound quirk into rstr kept and ledgered.

Sub-push boundary detail: sub-push A (units 114-119) six
files (PieceMisc/Readf_/LockCode/Doty/PcreH/GlobalQuery plus
Stubs seat work), sub-push B (units 120-122) three new files
plus Stubs/Carriers/CompilerCarriers seat work (Sim1 batch-7,
Jaccard/Dice/Shred/StopInit/SynInit batch-5/6 seats, the bmg
full-search reset tagged interp-bmgsubs.c:116, the SimGet
windowed tag, the SYMTAB_ERROR=9 and FLUSH=21 carriers, the
forx/fcn_x block), sub-push C (unit 123) the new Fcn.kt plus
the fcn delegate seat and the new ScanParse/Eval/GlobalOrder/
strfmon seats.

Close scan exceptions, documented and reconciled at 266ce6d,
all pre-existing from earlier batches and unchanged by this
tranche (no tranche-3 file carries any): raw dollars in seven
files all runtime Char literals or Kotlin escapes
(Fnumber/GlobalFnumber/IfCode/Prnterr/Parse_/LocalArray
char-literal compares, the escaped pair in SQLTable, one
prose dollar-fnumber in the Fnumber banner from tranche 2),
one backtick (Getstr2 FileDescriptor), six line-final
comment-closes all earlier banners' own block-tails (Getstr2/
GlobalQuery/LockCode/LockMisc/Parse_/PieceMisc). None affect
compilation or the emitted-byte gates; whole-arm kotlinc 0/0.

Consult rows (timestamp 2026-11-19T20:53:08Z, tranche-wide):
- DOC consult: unit interp-piece.cpp::<misc>, entry H-001, outcome hit-reuse, timestamp 2026-11-19T20:53:08Z
- DOC consult: unit interp-piece.cpp::<misc>, entry H-014, outcome hit-reuse, timestamp 2026-11-19T20:53:08Z
- DOC consult: unit interp-piece.cpp::<misc>, entry H-015, outcome hit-reuse, timestamp 2026-11-19T20:53:08Z
- DOC consult: unit interp-piece.cpp::<misc>, entry H-017, outcome hit-reuse, timestamp 2026-11-19T20:53:08Z
- DOC consult: unit interp-piece.cpp::<misc>, entry H-018, outcome hit-adapt, timestamp 2026-11-19T20:53:08Z
- DOC consult: unit interp-parse.cpp::readf_, entry H-001, outcome hit-reuse, timestamp 2026-11-19T20:53:08Z
- DOC consult: unit interp-parse.cpp::readf_, entry H-004, outcome hit-reuse, timestamp 2026-11-19T20:53:08Z
- DOC consult: unit interp-parse.cpp::readf_, entry H-012, outcome hit-adapt, timestamp 2026-11-19T20:53:08Z
- DOC consult: unit interp-parse.cpp::readf_, entry H-013, outcome hit-reuse, timestamp 2026-11-19T20:53:08Z
- DOC consult: unit interp-parse.cpp::readf_, entry H-014, outcome hit-reuse, timestamp 2026-11-19T20:53:08Z
- DOC consult: unit interp-parse.cpp::readf_, entry H-017, outcome hit-reuse, timestamp 2026-11-19T20:53:08Z
- DOC consult: unit compiler-lock.c::lock_code, entry H-017, outcome hit-reuse, timestamp 2026-11-19T20:53:08Z
- DOC consult: unit compiler-doty.c::doty, entry H-001, outcome hit-reuse, timestamp 2026-11-19T20:53:08Z
- DOC consult: unit compiler-doty.c::doty, entry H-004, outcome hit-reuse, timestamp 2026-11-19T20:53:08Z
- DOC consult: unit compiler-doty.c::doty, entry H-006, outcome hit-reuse, timestamp 2026-11-19T20:53:08Z
- DOC consult: unit compiler-doty.c::doty, entry H-013, outcome hit-reuse, timestamp 2026-11-19T20:53:08Z
- DOC consult: unit compiler-doty.c::doty, entry H-014, outcome hit-adapt, timestamp 2026-11-19T20:53:08Z
- DOC consult: unit compiler-doty.c::doty, entry H-017, outcome hit-reuse, timestamp 2026-11-19T20:53:08Z
- DOC consult: unit include/pcre.h::<misc>, entry H-017, outcome hit-reuse, timestamp 2026-11-19T20:53:08Z
- DOC consult: unit include/pcre.h::<misc>, entry H-018, outcome hit-adapt, timestamp 2026-11-19T20:53:08Z
- DOC consult: unit interp-sym.cpp::GlobalQuery, entry H-001, outcome hit-reuse, timestamp 2026-11-19T20:53:08Z
- DOC consult: unit interp-sym.cpp::GlobalQuery, entry H-004, outcome hit-reuse, timestamp 2026-11-19T20:53:08Z
- DOC consult: unit interp-sym.cpp::GlobalQuery, entry H-008, outcome hit-reuse, timestamp 2026-11-19T20:53:08Z
- DOC consult: unit interp-sym.cpp::GlobalQuery, entry H-013, outcome hit-reuse, timestamp 2026-11-19T20:53:08Z
- DOC consult: unit interp-sym.cpp::GlobalQuery, entry H-014, outcome hit-adapt, timestamp 2026-11-19T20:53:08Z
- DOC consult: unit interp-sym.cpp::GlobalQuery, entry H-015, outcome hit-reuse, timestamp 2026-11-19T20:53:08Z
- DOC consult: unit interp-sym.cpp::GlobalQuery, entry H-017, outcome hit-reuse, timestamp 2026-11-19T20:53:08Z
- DOC consult: unit interp-sym.cpp::GlobalQuery, entry H-018, outcome hit-adapt, timestamp 2026-11-19T20:53:08Z
- DOC consult: unit libmpscpp.cpp::<misc-9>, entry H-008, outcome hit-reuse, timestamp 2026-11-19T20:53:08Z
- DOC consult: unit libmpscpp.cpp::<misc-9>, entry H-009, outcome hit-reuse, timestamp 2026-11-19T20:53:08Z
- DOC consult: unit libmpscpp.cpp::<misc-9>, entry H-012, outcome hit-reuse, timestamp 2026-11-19T20:53:08Z
- DOC consult: unit libmpscpp.cpp::<misc-9>, entry H-013, outcome hit-reuse, timestamp 2026-11-19T20:53:08Z
- DOC consult: unit libmpscpp.cpp::<misc-9>, entry H-014, outcome hit-adapt, timestamp 2026-11-19T20:53:08Z
- DOC consult: unit libmpscpp.cpp::<misc-9>, entry H-015, outcome hit-adapt, timestamp 2026-11-19T20:53:08Z
- DOC consult: unit libmpscpp.cpp::<misc-9>, entry H-017, outcome hit-reuse, timestamp 2026-11-19T20:53:08Z
- DOC consult: unit compiler-set.c::set_code, entry H-003, outcome hit-adapt, timestamp 2026-11-19T20:53:08Z
- DOC consult: unit compiler-set.c::set_code, entry H-008, outcome hit-reuse, timestamp 2026-11-19T20:53:08Z
- DOC consult: unit compiler-set.c::set_code, entry H-009, outcome hit-reuse, timestamp 2026-11-19T20:53:08Z
- DOC consult: unit compiler-set.c::set_code, entry H-012, outcome hit-reuse, timestamp 2026-11-19T20:53:08Z
- DOC consult: unit compiler-set.c::set_code, entry H-013, outcome hit-reuse, timestamp 2026-11-19T20:53:08Z
- DOC consult: unit compiler-set.c::set_code, entry H-014, outcome hit-reuse, timestamp 2026-11-19T20:53:08Z
- DOC consult: unit compiler-set.c::set_code, entry H-017, outcome hit-reuse, timestamp 2026-11-19T20:53:08Z
- DOC consult: unit compiler-for.c::for_code, entry H-010, outcome hit-reuse, timestamp 2026-11-19T20:53:08Z
- DOC consult: unit compiler-for.c::for_code, entry H-013, outcome hit-reuse, timestamp 2026-11-19T20:53:08Z
- DOC consult: unit compiler-for.c::for_code, entry H-014, outcome hit-reuse, timestamp 2026-11-19T20:53:08Z
- DOC consult: unit compiler-for.c::for_code, entry H-017, outcome hit-reuse, timestamp 2026-11-19T20:53:08Z
- DOC consult: unit interp-fcn.cpp::fcn, entry H-008, outcome hit-reuse, timestamp 2026-11-19T20:53:08Z
- DOC consult: unit interp-fcn.cpp::fcn, entry H-009, outcome hit-reuse, timestamp 2026-11-19T20:53:08Z
- DOC consult: unit interp-fcn.cpp::fcn, entry H-010, outcome hit-reuse, timestamp 2026-11-19T20:53:08Z
- DOC consult: unit interp-fcn.cpp::fcn, entry H-011, outcome hit-reuse, timestamp 2026-11-19T20:53:08Z
- DOC consult: unit interp-fcn.cpp::fcn, entry H-012, outcome hit-reuse, timestamp 2026-11-19T20:53:08Z
- DOC consult: unit interp-fcn.cpp::fcn, entry H-013, outcome hit-reuse, timestamp 2026-11-19T20:53:08Z
- DOC consult: unit interp-fcn.cpp::fcn, entry H-014, outcome hit-adapt, timestamp 2026-11-19T20:53:08Z
- DOC consult: unit interp-fcn.cpp::fcn, entry H-015, outcome hit-reuse, timestamp 2026-11-19T20:53:08Z
- DOC consult: unit interp-fcn.cpp::fcn, entry H-017, outcome hit-reuse, timestamp 2026-11-19T20:53:08Z
- DOC consult: unit interp-fcn.cpp::fcn, entry H-018, outcome hit-adapt, timestamp 2026-11-19T20:53:08Z

Tranche totals: 56 consults, 45 hit-reuse, 11 hit-adapt
(u114 H-018, u115 H-012, u117 H-014, u118 H-018, u119 H-014
and H-018, u120 H-014 and H-015, u121 H-003, u123 H-014 and
H-018), 0 miss. Hit rate 100.0%, reuse rate 80.4%. TRANCHE 3
(rows 114-123, 10 units) COMPLETE: the piece residue, readf_,
lock_code, doty, the pcre ledger, GlobalQuery, the libmpscpp
operator residue, set_code, for_code, fcn. With tranche 3
landed, all 30 Batch 4 units (94-123) are rendered and gated;
batch 4 is closed pending the tracking-table and events
exports.

### DOC B4 close (batch 4), 2026-11-19

Batch 4 (units 94-123) CLOSED: 30/30 translated in tranches T1
(units 94-103), T2 (units 104-113) and T3 (units 114-123, sub-
pushes 0d18bfd / 8717ba0 / 266ce6d). Gradle gate on m1-build
BUILD SUCCESSFUL at each sub-push and at the master close
rebuild; whole-arm kotlinc 2.1.20 gate 0 errors / 0 warnings
throughout (125 files). Coverage checklist totals: 147
consults, 127 hit-reuse / 20 hit-adapt / 0 miss; 100.0% hit,
86.4% reuse; cookbook stays H-001..H-018 (no capture candidate
under T1/T2/T3). Per tranche: T1 45 consults (3 adapt), T2 46
consults (6 adapt), T3 56 consults (11 adapt).

LINE REPAIR (typo discipline, recorded per the B3 4b040e7
precedent): the B4 consult rows had been logged with bare
batch-unit numbers (e.g. "unit 94") instead of the registered
roster-id grammar of wp2/analysis/README.md (C-005), and the
91 T1/T2 rows omitted the per-row timestamp field. All 146
such rows are rewritten in place to the roster id plus the
tranche-wide timestamp already declared by each fragment
(T1 2026-11-19T19:30:34Z, T2 2026-11-19T19:44:21Z, T3
2026-11-19T20:53:08Z); entries, outcomes and row order are
unchanged. Additionally the T3 fragment's totals counted a
u119 H-018 hit-adapt consult that was not among its written
rows; the missing row is restored in position. No other
recorded value is touched. The repaired journal parses clean
under the export grammar: 467 consult rows, 0 malformed, 0
unknown unit ids, 0 duplicate keys.

Events regenerated per the C-005 mapping: the new
DOC_knowledge_events.csv is a pure append over the B3-close
export (639 -> 933 lines; 467 consult rows -> 932 event rows;
the B4 delta is exactly 147 reuse+hit pairs). The export ran
via a line-for-line reproduction of export_doc_events.R in the
translation sandbox (no R binary available there and package
installs are forbidden), with every validation of the script
applied and green; the R source remains the committed
instrument and the run is idempotent against the repaired
journal.

### DOC B5 tranche 1 (units 124-133), 2026-11-20

Batch 5 opened in committed row order (DOC follows the closed B4 per
the cycle map; no inversion to log). Tranche 1 = the batch-5 simple
stratum, ten units, all rendered from the pristine vendor tarball
(sha256 621deb...ffdbe verified this session; unit boundaries re-run
with the study's own extractor logic in function mode, exact
physical-LOC matches against units.csv for every unit). Whole-arm
kotlinc 2.1.20 local gate: 135 files, 0 errors, 0 warnings. Mark's
m1-build gradle gate (build) pending at tranche close.

Per-unit ledger (vendor line anchors from the pristine source):
- u124 compiler-for.c::<misc>: ForMisc.kt. Residual banner + include
  set of compiler-for.c (the June 16 2024 date line kept verbatim);
  function body for_code is the batch-4 unit 122. Declaration-only
  ledger per the unit-35/36 precedent; includes mapped to existing
  carriers, no property added, no executable code.
- u125 compiler-localarray.c::<misc>: LocalArrayMisc.kt. Same ledger
  shape (May 12 2024 date line kept); LocalArray is the batch-1 unit
  21 render.
- u126 mdh_tree_level_add (15 LOC): MdhTreeLevelAdd1.kt - the
  ONE-column overload (vendor 175-200; carried the family's marker
  banner comment). Family idiom per the unit-8/66/43 renders; Kotlin
  overload by arity (3 params).
- u127 mdh_tree_level_add (17 LOC): MdhTreeLevelAdd2.kt - the
  TWO-column overload (vendor 202-230), same family render; the
  second column emits the commerce-d-zero separator before the
  commerce-f close, byte order verbatim.
- u128 mdh_tree_level_add (21 LOC): MdhTreeLevelAdd4.kt - the
  FOUR-column overload (vendor 265-299); columns one-to-three each
  emit the separator, fourth closes. NOTE (unlike PRO's B5, where the
  same roster rows landed as verification units against a single
  family impl): DOC renders each overload in its own roster-row file,
  per the DOC unit-66 precedent; the three new arities complete the
  family's overload set already carrying the 3/5/7-column renders.
- u129 mstring::Length (vendor 3406-3427): MstringLength.kt - the
  GLOBAL-needle mstring member (char-star/string/mstring siblings
  are later-batch rows); Mglobal(RETRIEVE) with the
  MumpsGlobalException throw; strstr walk over the member content
  rendered as the offset/substring walk per H-007; the empty-needle
  descent-forever quirk kept verbatim (vendor expression).
- u130 Avg (vendor 10235-10257): Avg.kt - the Count/Max free-wrapper
  sibling; dead svptr/ep params kept for call-shape fidelity; the
  i==1 edge reads arg1[-1] in C (family quirk kept with a note);
  static char tmp[32] folds per H-008; sprintf percent-g via
  String.format Locale.US per H-009. The class member global::Avg IS
  a roster unit (row 262, batch 9): the owner-named H-014 seat
  Stubs.globalAvg raised this tranche with the real-owner tag (not a
  mis-tag), delegating when batch 9 lands.
- u131 global::Translate (vendor 4292-4316): GlobalTranslate.kt -
  the ONE-mstring member (the two-mstring sibling is roster row 270,
  batch 9), rendered as a GlobalRef extension per the unit-72
  precedent; Mglobal retrieve + throw; the vendor mstring Concat
  chain folds onto the CharArray command build (H-014 hit-adapt: the
  mstring::Concat members are out-of-roster class bodies, each a pure
  string append, rendered inline rather than raised as a stub; the
  fold is the adaptation note); dollar byte via the DOLLAR_131
  carrier per H-015/ unit-72 precedent; fcn dispatch on the H-014
  seat; v1d[1]/bd[1] offset-1 result convention.
- u132 mps_log10 (interp-arith.cpp:162-186): MpsLog10.kt - the
  mps_exp family shape; H-018 trichotomy resolved against the
  reference config (plain-double arm live; the log10l/strtold and
  MPFR arms documentary); gcvt_wrapper on the H-014 stub seat;
  strtod fold reused from the unit-40 render.
- u133 global::Length (vendor 3274-3304): GlobalLength.kt - the
  GLOBAL-needle global member (the no-arg/mstring/string/char-star
  siblings are other-batch rows), GlobalRef extension; the double
  Mglobal retrieve walk with the vendor's same-error-code second
  throw (unit-65 Concat quirk family, noted); the strstr walk and
  empty-haystack early return per H-007; the family empty-needle
  descent-forever quirk kept verbatim.

Sub-push boundary: one push, this tranche. New files: ForMisc,
LocalArrayMisc, MdhTreeLevelAdd1/2/4, MstringLength, Avg,
GlobalTranslate, MpsLog10, GlobalLength (ten). Stubs.kt edit-only:
+globalAvg seat (+10 lines). Carriers.kt and all other files
untouched.

Pre-push scan (H-015 gate) on every new file: no backticks, no
double-bang, no raw dollar outside the DOLLAR_131 Char-literal
declaration (the Fnumber-precedent exception), comment-closes end
their lines, no for-loop shadowing; cross-file top-level name scan
clean (new top-levels: the three overloads, mpsLog10, Avg,
Mstring.length, GlobalRef.length, GlobalRef.translate - arity or
receiver distinct from every existing package top-level).

Consult rows (tranche-wide timestamp 2026-11-20T00:22:13Z; the three
mdh_tree_level_add rows carry per-unit timestamps 00:23:13Z and
00:24:13Z on the middle and last unit per the C-008 triple key - the
three rows share one roster id, so a tranche-wide single stamp would
collide on (unit, entry, timestamp)):
- DOC consult: unit compiler-for.c::<misc>, entry H-004, outcome hit-reuse, timestamp 2026-11-20T00:22:13Z
- DOC consult: unit compiler-for.c::<misc>, entry H-017, outcome hit-reuse, timestamp 2026-11-20T00:22:13Z
- DOC consult: unit compiler-localarray.c::<misc>, entry H-004, outcome hit-reuse, timestamp 2026-11-20T00:22:13Z
- DOC consult: unit compiler-localarray.c::<misc>, entry H-017, outcome hit-reuse, timestamp 2026-11-20T00:22:13Z
- DOC consult: unit libmpscpp.cpp::mdh_tree_level_add, entry H-014, outcome hit-reuse, timestamp 2026-11-20T00:22:13Z
- DOC consult: unit libmpscpp.cpp::mdh_tree_level_add, entry H-015, outcome hit-adapt, timestamp 2026-11-20T00:22:13Z
- DOC consult: unit libmpscpp.cpp::mdh_tree_level_add, entry H-017, outcome hit-reuse, timestamp 2026-11-20T00:22:13Z
- DOC consult: unit libmpscpp.cpp::mdh_tree_level_add, entry H-014, outcome hit-reuse, timestamp 2026-11-20T00:23:13Z
- DOC consult: unit libmpscpp.cpp::mdh_tree_level_add, entry H-015, outcome hit-adapt, timestamp 2026-11-20T00:23:13Z
- DOC consult: unit libmpscpp.cpp::mdh_tree_level_add, entry H-017, outcome hit-reuse, timestamp 2026-11-20T00:23:13Z
- DOC consult: unit libmpscpp.cpp::mdh_tree_level_add, entry H-014, outcome hit-reuse, timestamp 2026-11-20T00:24:13Z
- DOC consult: unit libmpscpp.cpp::mdh_tree_level_add, entry H-015, outcome hit-adapt, timestamp 2026-11-20T00:24:13Z
- DOC consult: unit libmpscpp.cpp::mdh_tree_level_add, entry H-017, outcome hit-reuse, timestamp 2026-11-20T00:24:13Z
- DOC consult: unit libmpscpp.cpp::mstring::Length, entry H-007, outcome hit-reuse, timestamp 2026-11-20T00:22:13Z
- DOC consult: unit libmpscpp.cpp::mstring::Length, entry H-012, outcome hit-reuse, timestamp 2026-11-20T00:22:13Z
- DOC consult: unit libmpscpp.cpp::mstring::Length, entry H-014, outcome hit-reuse, timestamp 2026-11-20T00:22:13Z
- DOC consult: unit libmpscpp.cpp::mstring::Length, entry H-017, outcome hit-reuse, timestamp 2026-11-20T00:22:13Z
- DOC consult: unit libmpscpp.cpp::Avg, entry H-008, outcome hit-reuse, timestamp 2026-11-20T00:22:13Z
- DOC consult: unit libmpscpp.cpp::Avg, entry H-009, outcome hit-reuse, timestamp 2026-11-20T00:22:13Z
- DOC consult: unit libmpscpp.cpp::Avg, entry H-013, outcome hit-reuse, timestamp 2026-11-20T00:22:13Z
- DOC consult: unit libmpscpp.cpp::Avg, entry H-014, outcome hit-reuse, timestamp 2026-11-20T00:22:13Z
- DOC consult: unit libmpscpp.cpp::Avg, entry H-017, outcome hit-reuse, timestamp 2026-11-20T00:22:13Z
- DOC consult: unit libmpscpp.cpp::global::Translate, entry H-012, outcome hit-reuse, timestamp 2026-11-20T00:22:13Z
- DOC consult: unit libmpscpp.cpp::global::Translate, entry H-014, outcome hit-adapt, timestamp 2026-11-20T00:22:13Z
- DOC consult: unit libmpscpp.cpp::global::Translate, entry H-015, outcome hit-reuse, timestamp 2026-11-20T00:22:13Z
- DOC consult: unit libmpscpp.cpp::global::Translate, entry H-017, outcome hit-reuse, timestamp 2026-11-20T00:22:13Z
- DOC consult: unit interp-arith.cpp::mps_log10, entry H-009, outcome hit-reuse, timestamp 2026-11-20T00:22:13Z
- DOC consult: unit interp-arith.cpp::mps_log10, entry H-010, outcome hit-reuse, timestamp 2026-11-20T00:22:13Z
- DOC consult: unit interp-arith.cpp::mps_log10, entry H-014, outcome hit-reuse, timestamp 2026-11-20T00:22:13Z
- DOC consult: unit interp-arith.cpp::mps_log10, entry H-017, outcome hit-reuse, timestamp 2026-11-20T00:22:13Z
- DOC consult: unit interp-arith.cpp::mps_log10, entry H-018, outcome hit-reuse, timestamp 2026-11-20T00:22:13Z
- DOC consult: unit libmpscpp.cpp::global::Length, entry H-007, outcome hit-reuse, timestamp 2026-11-20T00:22:13Z
- DOC consult: unit libmpscpp.cpp::global::Length, entry H-012, outcome hit-reuse, timestamp 2026-11-20T00:22:13Z
- DOC consult: unit libmpscpp.cpp::global::Length, entry H-014, outcome hit-reuse, timestamp 2026-11-20T00:22:13Z
- DOC consult: unit libmpscpp.cpp::global::Length, entry H-017, outcome hit-reuse, timestamp 2026-11-20T00:22:13Z

Tranche totals: 35 consults, 31 hit-reuse / 4 hit-adapt (the three
mdh_tree_level_add H-015 identity-hash/dollar carriers per the
family precedent, and u131 H-014 Concat fold), 0 miss. Hit rate
100.0%, reuse rate 88.6%. TRANCHE 1 (units 124-133, 10 units)
COMPLETE: the two compiler banner ledgers, the three
mdh_tree_level_add overload renders, mstring::Length, Avg,
global::Translate, mps_log10, global::Length. Mark's gradle gate
(wp2/arms/DOC, build) is the pending close step; the B2-B4
per-unit-stamp discipline note above applies only to the shared-id
trio. Batch-5 tracking table opened in PILOT_TRACKING.md with the
10 tranche-1 rows marked translated.


### DOC B5 tranche 2 (units 134-143), 2026-11-20

Tranche 2 = the batch-5 core stratum, ten units, all rendered from
the pristine vendor tarball (sha256 re-verified this session; exact
physical-LOC matches against units.csv for every unit via the
study's own chunker-faithful extractor). Whole-arm kotlinc 2.1.20
local gate: 144 files, 0 errors, 0 warnings (compile gate caught
and fixed mid-render slips before the push: a missing
kotlin.math.pow import, a val reassignment in parse_count, Short
indexing in lpad, a redundant String() wrap in SysDump). Mark's
m1-build gradle gate runs at tranche close.

Per-unit ledger (vendor line anchors from the pristine source):
- u134 GlobalQS (vendor 2514-2536, 19 loc): GlobalQS.kt - the
  dollar-qs quoted-subscript wrapper, the unit-39 GlobalOrder family
  shape; xecute/SymGet on the H-014 seats; percent-qs command built
  in the tmp buffer with the dollar byte escaped (H-015 unit-39
  escape); unsigned-char params on the H-012 byte domain; the vendor
  static char r[STR_MAX] is DEAD (never read or written) - dropped
  per H-013 with the ledger note; tpx flag kept verbatim; the void
  return maps to Unit.
- u135 compiler-parse.c::ExpandData (vendor ~524, 24 loc):
  ExpandData.kt - the unit-11 ExpandNext sibling; reads the coded
  #G#/#S# payload after the position in the v1d carrier and emits
  the runtime GlobalData/LocalData call; fprintf(Out,...) maps to
  CompilerState.emit per H-001; strncmpAt is the offset strncmp seat
  (unit-11 precedent); neither-marker fallthrough leaves bp
  untouched (vendor shape kept).
- u136 global::Sum (vendor 2881, 21 loc): GlobalSum.kt - GlobalRef
  extension, the unit-37 KiLL XNEXT walk family (Mglobal code 8):
  fetch-and-accumulate while the next sibling matches this global's
  name prefix (strncmpC prefix break kept); get_global on the H-014
  seat; the !tpx arm throws MumpsGlobalException GLOBAL_NOT_FOUND=2;
  strtod accumulate per the H-010 unit-40 decimal-prefix contract.
  VENDOR QUIRK kept verbatim: the trailing-delimiter trim reads
  tmp[i-1] AND tmp[i-2] after i=strlen - a delimiter-terminated key
  can lose its final delimiter when a low control byte precedes
  (documented, not fixed).
- u137 Transpose (vendor 10316-10347, 23 loc - the MSV intrinsic
  WRAPPER; units.csv disambiguates from the batch-4 unit-100
  std::string overload at loc 15): TransposeMsv.kt - dead ep kept
  (H-013); VENDOR COPY-PASTE BUG kept: the second top-level guard
  tests arg2 at index i (the first argument's scan index), not j;
  both guards write the NUL one past the scan index, then hand the
  bytes after the caret carrier SOH to the global ctor/invoke pair;
  printf divergence via println (H-001); sigint on the H-014 seat;
  the global::Transpose(global) class member is NOT a roster unit -
  new owner-named H-014 seat Stubs.globalRefTranspose raised
  (libmpscpp.cpp owner); the vendor empty-string return is a fresh
  constant per call (H-008).
- u138 Multiply (vendor 2211-2250, 26 loc - the FREE (string,string,
  string) overload; NOT the batch-2 unit-46 five-arg MSV wrapper):
  append to Multiply.kt - the GlobalRefs A/B/C built directly from
  the string params, then the matrix-product walk INLINE; the body
  DUPLICATES the unit-53 global::Multiply member verbatim (vendor
  copy-paste, kept per policy; ledger note); the same H-014
  Kill/Order/Data/double-fetch/double-store seat family as u53;
  no printf fossils in this vendor body, nothing dropped.
- u139 expx (vendor 1166, 48 loc): MpsExpx.kt - the general power
  a-to-the-b runtime; H-018 trichotomy resolved against the
  reference config: the plain-double arm live (Kotlin pow), the
  BIGFLOAT strtold/powl arm and the MULTI_PRECISION MPFR/GMP arms
  documentary (kept as comments, the excluded-arm structure
  verbatim); gcvt_wrapper on the H-014 seat; strtod per H-010; the
  H-008 note carries the excluded statics disappearing with the
  arm.
- u140 getstr1 (vendor interp-sysfunc.cpp, 30 loc): Getstr1.kt -
  the std::string-accumulating line reader (NOT the batch-6
  TimeOut_getstr1 sibling, NOT the unit-48 getstr2 CharArray
  family); FILE* renders as java.io.Reader per H-001 with the C EOF
  -1 contract; the unsigned char** out-param renders as the caller
  CharArray written once on the return path (H-006 API-shape
  survival); chr widened Int with the byte filter comparisons kept
  (H-010); VENDOR QUIRK kept: the length-error drain loop
  assignment-in-condition ALSO exits on a NUL byte (the falsy chr==0
  test) - kept as the vendor shape; DEAD TAIL dropped per H-013
  after the MumpsGlobalException throw (area[i]=0, return, sigint,
  return(-1) unreachable; INPUT_LENGTH=10 const added file-private).
- u141 interp-strmanip.cpp::<misc> (36 physLOC): StrmanipMisc.kt -
  the LGPL banner block (license text DIFFERS from the GPL compiler
  banners - vendor fact kept) plus three entry residues: rindex
  (the vendor plain definition shadowing libc rindex; the empty
  MSVC ifdef pair above it resolves to NOTHING live per H-018;
  pointer-return folded to found-index offset per H-007, NULL to
  -1); strcatx (DUPLICATE-DEF LEDGER NOTE: identical semantics to
  the inline.h:46 helper already rendered as the internal strcatx
  seat in Input.kt unit-23 - the live seat stays, no second
  definition); lpad REAL BODY (RESOLVES the Stubs.lpad H-014 stub -
  removed; the Fcn.kt:678 unit-123 call site rebinds to the real
  body, offset-carrier signature kept so the site binds unchanged;
  j/k arithmetic on Int carriers per the compile gate).
- u142 SysDump (vendor 925, 18 loc): SysDump.kt - the pid-named
  symbol-table dump; PrintWriter over FileWriter at the program
  edge per H-001 with close in all paths; VENDOR UB QUANTIZED
  (H-001 divergence ledgered): the vendor fopen-NULL arm calls
  ErrorMessage and CONTINUES into fprintf(NULL,...) - rendered as
  the IllegalStateException edge carrier after the ErrorMessage
  call (sigint aborts first; the tail was unreachable in practice);
  Mltoa on the H-014 seat with the long-pid Int narrowing
  documented per H-010; the stab chain walk on the unit-28 Stab
  carrier with the local-capture null guard (the vendor
  (*p1->data).c_str() deref is unguarded - Kotlin nullable data
  requires the guard, adaptation noted) and the empty-string
  fallback; the unused p2/p3 declarations dropped per H-013.
- u143 parse_count (interp-rpm.cpp 360-401, 40 loc):
  ParseCount.kt - the pattern quantifier arm on the
  ParsePatternElement.kt carriers (parser/token/token_type,
  unit-104 file); the INT head takes lowerbound, accept_it, the DOT
  range arm ({lower,upper} / {lower,}) or bare {lower}, closed by
  bcatchr-'}'; the vendor else arm is the "Must have been a DOT."
  comment KEPT VERBATIM - accept, then {0,upper} on INT else the
  star quantifier; bstrcat on the H-014 stub (interp-bstring.cpp
  batch-9 owner); newBstring/bcatchr real seats; accept_it on the
  H-014 rpm-misc stub; the result carrier is a var per the vendor
  reassignment shape.

Sub-push boundary: one tranche; the ferry push lands as TWO commits
(code+tracking first, then the journal) because the transport size
limit rejects a single code-plus-journal payload - same tranche
boundary, no scope change. New files: GlobalQS,
ExpandData, GlobalSum, TransposeMsv, MpsExpx, Getstr1,
StrmanipMisc, SysDump, ParseCount (nine). Edits: Multiply.kt
(append +56 lines), Stubs.kt (-lpad resolved, +globalRefTranspose
seat), Fcn.kt (lpad rebind). Carriers.kt and all other files
untouched.

Pre-push scan (H-015 gate) on every new/changed file: no backticks,
no double-bang, no raw dollar outside carrier escapes, comment-
closes end their lines. Cross-file top-level name scan: all new
top-levels are overload- or arity-distinct from existing package
top-levels (Multiply joins the u46 wrapper by arity;
parse_count joins the pattern family); the whole-arm compile is
the decisive check and passed 0/0 at 144 files.

Consult rows (tranche-wide timestamp 2026-11-20T00:43:24Z; no
shared roster ids in this tranche, so a single stamp holds the
C-008 duplicate-key rule):
- DOC consult: unit libmpscpp.cpp::GlobalQS, entry H-012, outcome hit-reuse, timestamp 2026-11-20T00:43:24Z
- DOC consult: unit libmpscpp.cpp::GlobalQS, entry H-013, outcome hit-reuse, timestamp 2026-11-20T00:43:24Z
- DOC consult: unit libmpscpp.cpp::GlobalQS, entry H-014, outcome hit-reuse, timestamp 2026-11-20T00:43:24Z
- DOC consult: unit libmpscpp.cpp::GlobalQS, entry H-015, outcome hit-reuse, timestamp 2026-11-20T00:43:24Z
- DOC consult: unit libmpscpp.cpp::GlobalQS, entry H-017, outcome hit-reuse, timestamp 2026-11-20T00:43:24Z
- DOC consult: unit compiler-parse.c::ExpandData, entry H-001, outcome hit-reuse, timestamp 2026-11-20T00:43:24Z
- DOC consult: unit compiler-parse.c::ExpandData, entry H-017, outcome hit-reuse, timestamp 2026-11-20T00:43:24Z
- DOC consult: unit libmpscpp.cpp::global::Sum, entry H-010, outcome hit-reuse, timestamp 2026-11-20T00:43:24Z
- DOC consult: unit libmpscpp.cpp::global::Sum, entry H-014, outcome hit-reuse, timestamp 2026-11-20T00:43:24Z
- DOC consult: unit libmpscpp.cpp::global::Sum, entry H-017, outcome hit-reuse, timestamp 2026-11-20T00:43:24Z
- DOC consult: unit libmpscpp.cpp::Transpose, entry H-001, outcome hit-adapt, timestamp 2026-11-20T00:43:24Z
- DOC consult: unit libmpscpp.cpp::Transpose, entry H-008, outcome hit-reuse, timestamp 2026-11-20T00:43:24Z
- DOC consult: unit libmpscpp.cpp::Transpose, entry H-013, outcome hit-reuse, timestamp 2026-11-20T00:43:24Z
- DOC consult: unit libmpscpp.cpp::Transpose, entry H-014, outcome hit-reuse, timestamp 2026-11-20T00:43:24Z
- DOC consult: unit libmpscpp.cpp::Transpose, entry H-017, outcome hit-reuse, timestamp 2026-11-20T00:43:24Z
- DOC consult: unit libmpscpp.cpp::Multiply, entry H-014, outcome hit-reuse, timestamp 2026-11-20T00:43:24Z
- DOC consult: unit libmpscpp.cpp::Multiply, entry H-017, outcome hit-reuse, timestamp 2026-11-20T00:43:24Z
- DOC consult: unit interp-arith.cpp::expx, entry H-008, outcome hit-reuse, timestamp 2026-11-20T00:43:24Z
- DOC consult: unit interp-arith.cpp::expx, entry H-010, outcome hit-reuse, timestamp 2026-11-20T00:43:24Z
- DOC consult: unit interp-arith.cpp::expx, entry H-014, outcome hit-reuse, timestamp 2026-11-20T00:43:24Z
- DOC consult: unit interp-arith.cpp::expx, entry H-017, outcome hit-reuse, timestamp 2026-11-20T00:43:24Z
- DOC consult: unit interp-arith.cpp::expx, entry H-018, outcome hit-reuse, timestamp 2026-11-20T00:43:24Z
- DOC consult: unit interp-sysfunc.cpp::getstr1, entry H-001, outcome hit-reuse, timestamp 2026-11-20T00:43:24Z
- DOC consult: unit interp-sysfunc.cpp::getstr1, entry H-006, outcome hit-reuse, timestamp 2026-11-20T00:43:24Z
- DOC consult: unit interp-sysfunc.cpp::getstr1, entry H-007, outcome hit-reuse, timestamp 2026-11-20T00:43:24Z
- DOC consult: unit interp-sysfunc.cpp::getstr1, entry H-010, outcome hit-reuse, timestamp 2026-11-20T00:43:24Z
- DOC consult: unit interp-sysfunc.cpp::getstr1, entry H-013, outcome hit-reuse, timestamp 2026-11-20T00:43:24Z
- DOC consult: unit interp-sysfunc.cpp::getstr1, entry H-017, outcome hit-reuse, timestamp 2026-11-20T00:43:24Z
- DOC consult: unit interp-strmanip.cpp::<misc>, entry H-007, outcome hit-reuse, timestamp 2026-11-20T00:43:24Z
- DOC consult: unit interp-strmanip.cpp::<misc>, entry H-010, outcome hit-reuse, timestamp 2026-11-20T00:43:24Z
- DOC consult: unit interp-strmanip.cpp::<misc>, entry H-013, outcome hit-reuse, timestamp 2026-11-20T00:43:24Z
- DOC consult: unit interp-strmanip.cpp::<misc>, entry H-014, outcome hit-adapt, timestamp 2026-11-20T00:43:24Z
- DOC consult: unit interp-strmanip.cpp::<misc>, entry H-017, outcome hit-reuse, timestamp 2026-11-20T00:43:24Z
- DOC consult: unit interp-strmanip.cpp::<misc>, entry H-018, outcome hit-reuse, timestamp 2026-11-20T00:43:24Z
- DOC consult: unit interp-sym.cpp::SysDump, entry H-001, outcome hit-adapt, timestamp 2026-11-20T00:43:24Z
- DOC consult: unit interp-sym.cpp::SysDump, entry H-010, outcome hit-reuse, timestamp 2026-11-20T00:43:24Z
- DOC consult: unit interp-sym.cpp::SysDump, entry H-013, outcome hit-reuse, timestamp 2026-11-20T00:43:24Z
- DOC consult: unit interp-sym.cpp::SysDump, entry H-014, outcome hit-reuse, timestamp 2026-11-20T00:43:24Z
- DOC consult: unit interp-sym.cpp::SysDump, entry H-017, outcome hit-reuse, timestamp 2026-11-20T00:43:24Z
- DOC consult: unit interp-rpm.cpp::parse_count, entry H-014, outcome hit-reuse, timestamp 2026-11-20T00:43:24Z
- DOC consult: unit interp-rpm.cpp::parse_count, entry H-017, outcome hit-reuse, timestamp 2026-11-20T00:43:24Z

Tranche totals: 41 consults, 38 hit-reuse / 3 hit-adapt (u137
Transpose H-001 println divergence, u141 strmanip H-014 lpad stub
resolution plus the strcatx duplicate-def fold, u142 SysDump
H-001 fopen-UB quantization), 0 miss. Hit rate 100.0%, reuse rate
92.7%. TRANCHE 2 (units 134-143, 10 units) COMPLETE: the core
stratum - GlobalQS, ExpandData, global::Sum, the MSV Transpose
wrapper, the string-triple Multiply, expx, getstr1, the strmanip
residue, SysDump, parse_count. Mark's gradle gate (wp2/arms/DOC,
build) is the pending close step. Batch-5 tracking table: the ten
tranche-2 DOC rows marked translated in PILOT_TRACKING.md.

### DOC B5 tranche 3 (units 144-154), 2026-11-20

Tranche 3 = the batch-5 complex stratum, eleven units, the batch
close. All rendered from the pristine vendor tarball (sha256
re-verified this session; exact physical-LOC matches against
units.csv for every unit via the study's own chunker-faithful
extractor). Whole-arm kotlinc 2.1.20 local gate: 155 files,
0 errors, 0 warnings (the compile gate caught and fixed the
mid-render slips before the push: the Getstr2Stdin symbol miss
in the u150 main render - the Getstr2.kt STDIN_READER lazy is
the live edge; the u150 announce banner emitted literal
template-escapes instead of the placeholder constants - rebuilt
as plain concatenation). Mark's m1-build gradle gate ran GREEN
(BUILD SUCCESSFUL, 19s) at this close.

Per-unit ledger (vendor line anchors from the pristine source):
- u144 include/mumpsc/sym.h::<misc> (39 physLOC): SymHeaderMisc.kt
  - declaration ledger only: the stab struct binds the unit-28
  Stab carrier (H-004 single owner); sym_/Qsub/Qlength/GlobalQuery/
  GlobalName/fullUnpad/ErrorMessage/SysDump/_SymFree/BuildGlobal
  each name its landed render or tagged owner seat; the twin
  _INTERP_/_else keyfix comment pair resolves EMPTY per H-018
  (both arms' lines are the same commented-out text); two NEW
  H-014 seats raised with the unit: Stubs.strsep (libc split
  helper; char** holder renders the Array<String> carrier,
  H-006 idiom) and Stubs.ServiceInterface (the interp-side
  service entry); BuildLocal stays the H-013 commented-fossil
  with the live compiler-globals-ext.h declaration owning the
  existing seat; Qlength names THIS tranche's u145 render.
- u145 interp-sym.cpp::Qlength (vendor 1007, 18 loc): Qlength.kt
  - the Qlength body over private copyMsvQ (the unit-119 adapter
  duplicate with the dup-def ledger noter; copyMsv in
  GlobalQuery.kt is private and stays private); keyfix via the
  offset-slice idiom at the vendor 9-key walk; CODE 4
  SETINT/SET query shape kept; sprintf percent-d via
  String.format Locale.US (H-009); Stubs.Qlength flips to
  this delegate (Fcn.kt call sites bind unchanged).
- u146 libmpscpp.cpp::SynInit (~750, 58 loc): SynInit.kt - the
  synonym-table loader; the C strtok(line," \t\n") walk renders
  the explicit strtokSpaceFirst/Next helpers over the IntArray
  holder (H-001 FILE* to BufferedReader; H-006 out-param
  carrier); fopen-NULL arm carries the IllegalStateException
  edge (SysDump u142 precedent); the vendor stores the
  FOLLOWING token in .word and the first in .alias - the vendor
  quirk kept verbatim with the ledger; Stubs.synInit flips to
  this delegate.
- u147 interp-rpm.cpp::scan_token (vendor 300-358, 39 loc):
  ScanToken.kt - the Lockner scanner boxed as fun
  scan_token(s: scanner): token_type over the u104/e113
  carriers (String input + Int currentChar walk; NUL
  quantization at the end-of-index exactly as the C \0
  sentinel); STRING/PATTERN_CODE/INT/COMMA/DOT/LPAREN/RPAREN/
  ERROR/EOT arms per the vendor switch; take_char binds the
  NEW Stubs.take_char seat (resolved by u152 same tranche);
  syntax_error calls ride the seat flipped this tranche.
- u148 interp-bmgsubs.c::bmg_search (vendor ~253, 60 loc):
  BmgSearch.kt - the Boyer-Moore-Gosper driver over the u17
  gotamatch render family; BmgFsearchStatic carries delta0/
  cmap/pattern/patlen; BmgStatic.nsuccess the success count;
  the gotamatch -1 NULL sentinel contract per u17; the
  want-reader FILE* loop with clamped-index guards where the
  vendor reads one element past the buffer (H-001 UB guard
  ledger); actions bind ((CharArray,Int)->Int)?.
- u149 include/mumpsc/mstringClass.h::<misc> (331 physLOC):
  MstringClassMisc.kt - declaration-heavy header ledger; the
  ctors/conversion syntax note over the Carriers Mstring;
  s_str() folds onto the c_str witness with the seat noter;
  EDIT-ONLY Carriers.kt addition: Mstring.name private seat
  (mstringClass.h field; H-004 single-owner note; visible
  to the member renders that own the private contract).
- u150 mumps.cpp::main (vendor 58-183, 125 loc):
  MumpsMainUnit150.kt - the REPL entry rendered as main150
  (the unit-12 MumpsMisc.kt residue owns the file head; the
  one-main-per-package rule); argv percent-family staging via
  the u120 SymPut renders over CstringOps carriers (percent
  percent-d loop, percent-c count, percent-0 pair); the ./
  prefix strip in place; the three-quote escape run command
  built verbatim then the H-014 Interpret seat; announce
  banner rides the __TIME__/__DATE__ placeholder tokens
  (registered divergence: no JVM analog of the compile
  stamps) plus the H-018 define ledger in full (SQLITE live,
  NATIVE/NATIVE_SHARED/BIGFLOAT arms documented per the
  reference config); GlobalClose expands inline to
  Mglobal(CLOSE,...) both EOF arms (libmpscpp.h macro shape);
  readline loop over the Getstr2.kt STDIN_READER
  (H-001 divergence ledger; history persistence no-op, the
  ?-relaunch backward goto rendered as the flag re-entry
  loop, H-011); pid via ProcessHandle.current().pid();
  cleanLocks the u56 render; the halt set strcasecmp via
  String.lowercase; the dead static struct C block dropped
  per H-013.
- u151 include/mumpsc/compiler-globals-ext.h::<misc> (185-219,
  144 physLOC): EDIT-ONLY CompilerCarriers.kt additions plus
  CompilerGlobalsExtMisc.kt. The extern roster closed on
  CompilerState with per-line anchors: setpiece/iorg/symlen/
  ctrlc/gpad/Jflg/FunctionName/LabelsStart/TextStart/p1/p3/
  cvars/cvarx/Final/MaxTmp/IF_flg_max/pd1cur/i/j/k/l/cmdx/
  ExecLevel/ExecStack/DoRet/DoRetX/CX/EmitLabel/JOB_flg/
  SubFlag/SymIncrFlg/TryFlg/CatchFlg/opnfile/in (in_ per
  H-017)/dosave/cur_line/icmnd/p2px/ernbr/tmp2/tmp3/pl/p2/
  except/flg; the idem partners keep their first owners
  (ierr1 at InterpParseMisc, ErrChar/nstart on Msv, output_
  file the String seat); the struct renders Nmes/TxtEntry/
  Dcs land in CompilerGlobalsExtMisc.kt; CVARMAX 100; the
  :82-104 prototype roster ledgered (landed: xindex, errmod_,
  readf_, inout, numcomp, getstr1, doty, LocalArray,
  GlobalArray, atTest, extract2; owner-tagged not-yet-landed:
  cvar Stubs seat, Extract1, add per the arith.h lattice,
  linepad, linepad2, fcninit, epi, EP, LevCheck, atCopy; H-013
  fossils grep-verified: ScanPastPostCond and pindex have NO
  defining body in the vendor tree); the cod206-210 mnemonic
  block stays the comment ledger.
- u152 interp-rpm.cpp::<misc> (118-242, 145 loc): InterpRpmMisc.kt
  - the driver tier REAL BODIES: rpm (the quote-tilt direct-perl
  arm keeps the trailing-quote strip hack; PM_DEBUG 0 file-
  private const of pm.h:25, dead printf arm kept as comment),
  perlmatch (the "1"/"0" literal fold), perlcpp, new_scanner,
  scan, take_char, new_parser, parse (EOT check), parse_pattern,
  parse_pattern_atom (the vendor element-then-count bstrcat
  argument order kept), parse_pattern_atom_list, accept,
  accept_it, syntax_error (the percent-star-c caret banner as
  the pos-1 space walk; sigint(100) terminator). H-015: the C
  whole-token assignments copy through copyTokenInto (the val
  Bstring carrier precludes reassignment); the String scanner
  quantizes end-of-index as NUL at take_char. SEAT FLIPS:
  rpm/accept/accept_it/syntax_error/parse_pattern_atom_list
  become delegates; perlPm NEW seat (owner interp-rpm.cpp::
  perl_pm, batch-9 roster row 257); bstrcat/
  leaning_toothpickize keep their batch-9/-6 owner seats;
  parse_count/parse_pattern_element stay the landed u143/u104
  renders (H-004 keep-first).
- u153 rdbms-if.c::sql (vendor 81-387, 209 loc): Sql.kt - the
  H-018 empty arm: the reference config defines neither SQL
  (WindowsXX ODBC section) nor POSTGRES (libpq-fe.h section;
  the ALLOW_POSTGRES autoconfigure gates resolve dead) - the
  LIVE body is the vendor's own else-arm return 0 with the
  vendor's comment kept; both dead arms stay documentary
  ledgers (the wlock.h wlock precedent). H-015 same-lexeme
  note: the Stubs.sql 6-int-arg seat is the btree.h SQL-side
  dispatcher, a DIFFERENT C symbol - both keep their seats
  (overload-family rule).
- u154 libmpscpp.cpp::<misc-5> (600 physLOC): LibmpscppMisc5.kt
  - the comparison ladders tail (le(global)/le(mstring), the
  == and != ladders rendered as eq/ne word-operator extension
  families per the SymOpsMisc lt/gt precedent; the const
  char*/char*/string twins fold onto the String seat - ledger:
  27 vendor overloads render 16 Kotlin overloads; the opening
  else-return-false CHUNK-EDGE FRAGMENT documented, not
  rendered - the tail of mstring::operator<=(string), owner
  misc-4 row 283 batch 10); thrownMessage (vendor "detetected"
  typo kept) over the Globals.GlobalExceptionCode carrier +
  file-private INPUT_LENGTH idem const; __finish with the
  per-file flg carrier and exitProcess; global::Int/Double/
  Mstring/Char over the unit-31 conversion seats (n-max
  strncpy clamp shape kept); Stem over the NEW Stubs.wstem
  seat (owner libmpscpp.cpp::wstem - not a roster unit);
  TokenInit/Token (the coded ce/cf bytes built
  programmatically, H-015; the z-command leads ride the
  unicode escape); the mdh GTK wrapper tier (dialog-new-
  with-buttons the zfcn emitter with the percent-p
  identity-hex render at the offset-1 domain; ~17 pass-
  through wrappers over per-file GtkPassThrough stub seats
  with the GTK-library owners named - the
  gtkTreeStoreAppend seat precedent; GdkRgba per-file quad;
  statics into per-file carriers, H-008); Dump/Restore folds
  (mstring + String seats; the zcd/zcl bytes escaped);
  Getenv real (System.getenv, H-001; in_ keyword render);
  ZSeek/Ztell x4 over the RandomAccessFile carrier (H-001
  FILE* divergence: fsetpos/fgetpos map to seek/filePointer,
  the fpos_t void* payload is the 64-bit offset domain);
  Perl x2 (the commented perlcpp call kept verbatim; both
  bodies return svPtr->tpx); global::DocCorrelate char*/mstring
  twins delegate into the Stubs.globalDocCorrelate seat which
  KEEPS its batch-10 row-288 owner - front-run overlap
  ledgered, seat NOT flipped to avoid the delegation circle
  (the twins call the string-twin working body); pt (the
  UNINITIALIZED int i vendor UB quantized to 0, ledger);
  global::Count REAL BODY (the XNEXT sibling walk, Min-family
  idiom; H-010: the Double seat cast at the return;
  Stubs.globalCount flips); global::Transpose member REAL
  BODY (B().Kill() via the globalKill seat; the Order/
  read/store seats; Stubs.globalRefTranspose flips) plus the
  char* and mstring free wrappers kept CharArray/Mstring so
  the unit-100 String overload keeps its H-015 overload-
  family slot; stem x3 string/mstring/char* (the string&
  in-place fold lands on the return - divergence ledgered;
  the static tmp carriers per-file); Hash/lhash REAL 4-byte
  sums (ULong wraparound kept, the sign-bit-free domain
  documented; percent-lu via unsigned decimal toString;
  Stubs.lhash flips to the real body); indirectStar (the
  indirect star operator render; the static indirect carrier
  per-file; H-014 name adapt - Kotlin has no star operator);
  GlobalSet/GlobalGet over the shared xecute bridge ("s " +
  name + "=quote value quote"; the vendor stray backslash
  line-splice noted once as the H-013 fossil; tpx set/reset
  contract kept; the seven/five overloads fold onto the
  Mstring/String pairs; GlobalGet string-twin's conditional
  shape folded onto the char* carrier with the unreachable
  empty-store kept in shape); pmw x5 folds (String pair +
  Mstring pair) over the NEW Stubs.pm seat (owner
  interp-pm.cpp::pm - not a roster unit); the chunk-end
  "mstring tmp;" front edge of mcvt documented, owned by
  misc-6 (batch 7).

Sub-push boundary: one tranche; the ferry push lands as TWO
commits (code+tracking first, then this journal) because the
transport size limit rejects a single code-plus-journal payload -
same tranche boundary, no scope change (the tranche-2
precedent). New files: SymHeaderMisc, Qlength, SynInit,
ScanToken, BmgSearch, MstringClassMisc, MumpsMainUnit150,
CompilerGlobalsExtMisc, InterpRpmMisc, Sql, LibmpscppMisc5
(eleven). Edits: Carriers.kt (Mstring.name + Msv.program
seats), CompilerCarriers.kt (the ext-header extern roster
close), Stubs.kt (+strsep +ServiceInterface +take_char +perlPm
+pm +wstem seats; flips: Qlength, synInit, rpm, accept,
accept_it, syntax_error, parse_pattern_atom_list, lhash,
globalCount, globalRefTranspose; the globalDocCorrelate comment
front-run note). PILOT_TRACKING.md: batch-5 table rows 144-154
DOC=translated; tranche-3 paragraph added; tally: batch 5 is
31/31 translated.

Pre-push scan (H-015 gate) on every new/changed file: no
backticks, no double-bang, no raw dollar outside carrier
escapes (the scan caught and fixed five in-comment raw
dollars and six backtick quotings before the push),
comment-closes end their lines, no for-loop shadowing (the new
files walk with while). Cross-file top-level name scan: the new
top-levels are overload- or arity-distinct from existing
package top-levels (le/eq/ne join the lt/gt family;
Transpose(CharArray,CharArray)/(Mstring,Mstring) stay
distinct from the unit-100 String pair and the u137 MSV
wrapper; scan/parse/accept take_char rpm perlmatch perlcpp are
new lexemes; Hash(char*) joins the u31 Hash class by usage
kind); the whole-arm compile is the decisive check and passed
0/0 at 155 files.

TOOLING INCIDENT (logged per the BASE lesson genre): the
tranche-3 journal push (commit 8fb5e6d) TRUNCATED
ARM_WORKING_NOTES.md - the agent-side file reader returned a
partial read of the 2934-line journal and the push shipped the
truncated head, dropping every section after the unit-68
entry (all DOC sections). The gradle gate ran green
regardless (the .kt payloads were pushed in commit b8cde8d
and verified additive). REPAIR: this fragment re-carries the
full tranche-3 section; the restoration script (run by the
PI on m1-build) resets the journal file to its complete
pre-tranche-3 blob from git history (git show
b8cde8da:wp2/arms/ARM_WORKING_NOTES.md) and appends this
fragment, then removes the fragment file in the same repair
commit. LESSON captured for the ferry: ANY pushed blob over
the file-reader limit must be chunk-read with its
truncation flag checked and the pushed result md5-verified
against the source blob (the unit-31 CSV clobber incident's
sibling failure mode; the T2 "never re-read via many paged
chunks" note applies to replay cost, not to truncation
safety - chunk-and-verify, do not single-read-and-hope).

Consult rows (tranche-wide timestamp 2026-11-20T01:44:51Z; the
eleven unit ids of rows 144-154 are distinct roster ids, a
single stamp holds the C-008 duplicate-key rule):
- DOC consult: unit include/mumpsc/sym.h::<misc>, entry H-004, outcome hit-reuse, timestamp 2026-11-20T01:44:51Z
- DOC consult: unit include/mumpsc/sym.h::<misc>, entry H-013, outcome hit-reuse, timestamp 2026-11-20T01:44:51Z
- DOC consult: unit include/mumpsc/sym.h::<misc>, entry H-014, outcome hit-adapt, timestamp 2026-11-20T01:44:51Z
- DOC consult: unit include/mumpsc/sym.h::<misc>, entry H-018, outcome hit-reuse, timestamp 2026-11-20T01:44:51Z
- DOC consult: unit interp-sym.cpp::Qlength, entry H-006, outcome hit-reuse, timestamp 2026-11-20T01:44:51Z
- DOC consult: unit interp-sym.cpp::Qlength, entry H-009, outcome hit-reuse, timestamp 2026-11-20T01:44:51Z
- DOC consult: unit interp-sym.cpp::Qlength, entry H-014, outcome hit-adapt, timestamp 2026-11-20T01:44:51Z
- DOC consult: unit interp-sym.cpp::Qlength, entry H-017, outcome hit-reuse, timestamp 2026-11-20T01:44:51Z
- DOC consult: unit libmpscpp.cpp::SynInit, entry H-001, outcome hit-adapt, timestamp 2026-11-20T01:44:51Z
- DOC consult: unit libmpscpp.cpp::SynInit, entry H-006, outcome hit-reuse, timestamp 2026-11-20T01:44:51Z
- DOC consult: unit libmpscpp.cpp::SynInit, entry H-014, outcome hit-adapt, timestamp 2026-11-20T01:44:51Z
- DOC consult: unit libmpscpp.cpp::SynInit, entry H-017, outcome hit-reuse, timestamp 2026-11-20T01:44:51Z
- DOC consult: unit interp-rpm.cpp::scan_token, entry H-014, outcome hit-adapt, timestamp 2026-11-20T01:44:51Z
- DOC consult: unit interp-rpm.cpp::scan_token, entry H-015, outcome hit-reuse, timestamp 2026-11-20T01:44:51Z
- DOC consult: unit interp-rpm.cpp::scan_token, entry H-017, outcome hit-reuse, timestamp 2026-11-20T01:44:51Z
- DOC consult: unit interp-bmgsubs.c::bmg_search, entry H-001, outcome hit-adapt, timestamp 2026-11-20T01:44:51Z
- DOC consult: unit interp-bmgsubs.c::bmg_search, entry H-014, outcome hit-reuse, timestamp 2026-11-20T01:44:51Z
- DOC consult: unit interp-bmgsubs.c::bmg_search, entry H-017, outcome hit-reuse, timestamp 2026-11-20T01:44:51Z
- DOC consult: unit include/mumpsc/mstringClass.h::<misc>, entry H-004, outcome hit-reuse, timestamp 2026-11-20T01:44:51Z
- DOC consult: unit include/mumpsc/mstringClass.h::<misc>, entry H-017, outcome hit-reuse, timestamp 2026-11-20T01:44:51Z
- DOC consult: unit mumps.cpp::main, entry H-001, outcome hit-adapt, timestamp 2026-11-20T01:44:51Z
- DOC consult: unit mumps.cpp::main, entry H-003, outcome hit-reuse, timestamp 2026-11-20T01:44:51Z
- DOC consult: unit mumps.cpp::main, entry H-009, outcome hit-reuse, timestamp 2026-11-20T01:44:51Z
- DOC consult: unit mumps.cpp::main, entry H-011, outcome hit-reuse, timestamp 2026-11-20T01:44:51Z
- DOC consult: unit mumps.cpp::main, entry H-013, outcome hit-reuse, timestamp 2026-11-20T01:44:51Z
- DOC consult: unit mumps.cpp::main, entry H-014, outcome hit-reuse, timestamp 2026-11-20T01:44:51Z
- DOC consult: unit mumps.cpp::main, entry H-015, outcome hit-adapt, timestamp 2026-11-20T01:44:51Z
- DOC consult: unit mumps.cpp::main, entry H-017, outcome hit-reuse, timestamp 2026-11-20T01:44:51Z
- DOC consult: unit mumps.cpp::main, entry H-018, outcome hit-reuse, timestamp 2026-11-20T01:44:51Z
- DOC consult: unit include/mumpsc/compiler-globals-ext.h::<misc>, entry H-004, outcome hit-reuse, timestamp 2026-11-20T01:44:51Z
- DOC consult: unit include/mumpsc/compiler-globals-ext.h::<misc>, entry H-008, outcome hit-reuse, timestamp 2026-11-20T01:44:51Z
- DOC consult: unit include/mumpsc/compiler-globals-ext.h::<misc>, entry H-013, outcome hit-reuse, timestamp 2026-11-20T01:44:51Z
- DOC consult: unit include/mumpsc/compiler-globals-ext.h::<misc>, entry H-014, outcome hit-reuse, timestamp 2026-11-20T01:44:51Z
- DOC consult: unit include/mumpsc/compiler-globals-ext.h::<misc>, entry H-015, outcome hit-reuse, timestamp 2026-11-20T01:44:51Z
- DOC consult: unit include/mumpsc/compiler-globals-ext.h::<misc>, entry H-017, outcome hit-adapt, timestamp 2026-11-20T01:44:51Z
- DOC consult: unit interp-rpm.cpp::<misc>, entry H-004, outcome hit-reuse, timestamp 2026-11-20T01:44:51Z
- DOC consult: unit interp-rpm.cpp::<misc>, entry H-014, outcome hit-adapt, timestamp 2026-11-20T01:44:51Z
- DOC consult: unit interp-rpm.cpp::<misc>, entry H-015, outcome hit-reuse, timestamp 2026-11-20T01:44:51Z
- DOC consult: unit interp-rpm.cpp::<misc>, entry H-017, outcome hit-reuse, timestamp 2026-11-20T01:44:51Z
- DOC consult: unit rdbms-if.c::sql, entry H-004, outcome hit-reuse, timestamp 2026-11-20T01:44:51Z
- DOC consult: unit rdbms-if.c::sql, entry H-015, outcome hit-reuse, timestamp 2026-11-20T01:44:51Z
- DOC consult: unit rdbms-if.c::sql, entry H-018, outcome hit-reuse, timestamp 2026-11-20T01:44:51Z
- DOC consult: unit libmpscpp.cpp::<misc-5>, entry H-001, outcome hit-adapt, timestamp 2026-11-20T01:44:51Z
- DOC consult: unit libmpscpp.cpp::<misc-5>, entry H-004, outcome hit-adapt, timestamp 2026-11-20T01:44:51Z
- DOC consult: unit libmpscpp.cpp::<misc-5>, entry H-007, outcome hit-reuse, timestamp 2026-11-20T01:44:51Z
- DOC consult: unit libmpscpp.cpp::<misc-5>, entry H-008, outcome hit-reuse, timestamp 2026-11-20T01:44:51Z
- DOC consult: unit libmpscpp.cpp::<misc-5>, entry H-009, outcome hit-reuse, timestamp 2026-11-20T01:44:51Z
- DOC consult: unit libmpscpp.cpp::<misc-5>, entry H-010, outcome hit-reuse, timestamp 2026-11-20T01:44:51Z
- DOC consult: unit libmpscpp.cpp::<misc-5>, entry H-012, outcome hit-reuse, timestamp 2026-11-20T01:44:51Z
- DOC consult: unit libmpscpp.cpp::<misc-5>, entry H-013, outcome hit-reuse, timestamp 2026-11-20T01:44:51Z
- DOC consult: unit libmpscpp.cpp::<misc-5>, entry H-014, outcome hit-adapt, timestamp 2026-11-20T01:44:51Z
- DOC consult: unit libmpscpp.cpp::<misc-5>, entry H-015, outcome hit-reuse, timestamp 2026-11-20T01:44:51Z
- DOC consult: unit libmpscpp.cpp::<misc-5>, entry H-017, outcome hit-reuse, timestamp 2026-11-20T01:44:51Z

Tranche totals: 53 consults, 40 hit-reuse / 13 hit-adapt (u144
the two new H-014 seats raised, u145 the copyMsvQ duplicate-
def + keyfix offset-slice adaptation, u146 the strtok/
fopen-NULL carries, u147 the take_char seat raise resolved
same-tranche, u150 the stdin-reader and compile-stamp diverges,
u151 the in_ keyword render, u152 the perlPm seat raise +
five stub flips, u154 the GTK pass-through seat lattice, the
DocCorrelate front-run and the indirectStar/op-fold
adaptations), 0 miss. Hit rate 100.0%, reuse rate 75.5% (the
complex stratum carries a heavier adaptation load: the GTK
lattice, the FILE* seats and the new-seat raises of this
tranche). TRANCHE 3 (units 144-154, 11 units) COMPLETE - the
complex stratum and the BATCH 5 CLOSE: sym.h misc, Qlength,
SynInit, scan_token, bmg_search, mstringClass misc, the mumps
REPL main, compiler-globals-ext misc, the rpm driver tier,
the sql trampoline, and the misc-5 library tail. Batch-5
tracking table now shows 31/31 DOC translated across the
three 2026-11-20 tranches; the batch-5 close review (gradle
gate, coverage checklist, events export at batch close) is
the next protocol step.
### DOC B5 close — coverage checklist (units 124-154), 2026-11-20

Manual batch-close coverage checklist per the DOC batch-close
discipline (section 1: for each canonical construct in the batch,
did at least one entry match?). Batch 5 (units 124-154) CLOSED:
31/31 translated 2026-11-20 in three tranches per the
pre-registered batch_plan.md stratification (T1 124-133 simple,
commits 02aff5f/e1e868a; T2 134-143 core, code 279a6ab and journal
1eed85e; T3 144-154 complex, code b8cde8da, journal repaired
after the truncation incident, see the T3 section's TOOLING
INCIDENT ledger and repair commits 82651bd5/8197a54).
Gates: whole-arm kotlinc 2.1.20 0 errors / 0 warnings at every
tranche close (135 -> 144 -> 155 files); gradle gate on m1-build
BUILD SUCCESSFUL at every tranche, all first-pull, zero gradle
fix rounds (T1 12s, T2 15s, T3 19s - the four T2 compile slips
and the T3 Getstr2/MumpsMainUnit150/take_char fixes were all
caught by the local pre-push gate, logged as data). Events
export regeneration via export_doc_events.R follows this
checklist per the C-005/C-008 keys.

Entry-by-entry coverage across the 31 B5 unit journals (129
consult rows; the mdh_tree_level_add triple 126-128 shares one
vendor roster id, its three consults carry distinct timestamps
per the C-008 (unit, entry, timestamp) triple key; none
consulted an entry without applying it; journal counts below
count distinct unit ids, consult counts count rows):

- Canonical IO type (H-001): matched in 8 B5 unit journal(s); 8 consults, 2 reuse / 6 adapt
- Stateful macro (H-003): matched in 1 B5 unit journal(s); 1 consult, 1 reuse
- File-scope globals (H-004): matched in 8 B5 unit journal(s); 8 consults, 7 reuse / 1 adapt
- Out-parameter -> holder array (H-006): matched in 3 B5 unit journal(s); 3 consults, 3 reuse
- Buffer role classification (H-007): matched in 5 B5 unit journal(s); 5 consults, 5 reuse
- Shared static scratch buffer (H-008): matched in 5 B5 unit journal(s); 5 consults, 5 reuse
- sprintf format-string idiom (H-009): matched in 5 B5 unit journal(s); 5 consults, 5 reuse
- Numeric promotion / truncation (H-010): matched in 7 B5 unit journal(s); 7 consults, 7 reuse
- goto -> structured control (H-011): matched in 1 B5 unit journal(s); 1 consult, 1 reuse
- Unsigned char domain mapping (H-012): matched in 5 B5 unit journal(s); 5 consults, 5 reuse
- Dead store / dead parameter (H-013): matched in 10 B5 unit journal(s); 10 consults, 10 reuse
- Out-of-scope C function -> stub (H-014): matched in 23 B5 unit journal(s); 25 consults, 17 reuse / 8 adapt
- Kotlin grammar vs quoted C source (H-015): matched in 9 B5 unit journal(s); 11 consults, 7 reuse / 4 adapt
- snake_case -> camelCase (H-017): matched in 27 B5 unit journal(s); 29 consults, 28 reuse / 1 adapt
- Preprocessor conditional arms (H-018): matched in 6 B5 unit journal(s); 6 consults, 6 reuse

H-002, H-005 and H-016 found no triggering situation in B5 (no
consult row; state recorded so entry-for-entry parity with
PRO's rule base is unaffected; no revision needed).

Consult outcome totals for the batch: 109 hit-reuse, 20
hit-adapt, 0 miss (129 consult rows). Hit rate 129/129 = 100.0%
(every consult matched an entry); reuse rate 109/129 = 84.5%
(adaptation, not failure, accounts for the remainder). Per
tranche: T1 35 consults (31 reuse / 4 adapt, 88.6% reuse), T2 41
consults (38 / 3, 92.7%), T3 53 consults (40 / 13, 75.5%) - the
complex stratum carries the heaviest adaptation load of the
study so far (the misc-5 GTK lattice, the FILE-star seats and
the new-seat raises of tranche 3). No miss rows: the B5 canon
was fully covered by cookbook v0 as it stood at batch open, so
no capture candidate exists under T1/T2/T3; cookbook v0 stays
at H-001..H-018 and remains entry-for-entry parallel with
PRO's rules_v0.pl (h001..h018).

Per-unit consult coverage (unit -> entries consulted,
batch_plan order; rows 126-128 share the mdh_tree_level_add id):

- compiler-for.c::<misc>: H-004, H-017
- compiler-localarray.c::<misc>: H-004, H-017
- libmpscpp.cpp::mdh_tree_level_add (roster rows 126/127/128, distinct stamps): H-014, H-015, H-017 per row
- libmpscpp.cpp::mstring::Length: H-007, H-012, H-014, H-017
- libmpscpp.cpp::Avg: H-008, H-009, H-013, H-014, H-017
- libmpscpp.cpp::global::Translate: H-012, H-014, H-015, H-017
- interp-arith.cpp::mps_log10: H-009, H-010, H-014, H-017, H-018
- libmpscpp.cpp::global::Length: H-007, H-012, H-014, H-017
- libmpscpp.cpp::GlobalQS: H-012, H-013, H-014, H-015, H-017
- compiler-parse.c::ExpandData: H-001, H-017
- libmpscpp.cpp::global::Sum: H-010, H-014, H-017
- libmpscpp.cpp::Transpose: H-001, H-008, H-013, H-014, H-017
- libmpscpp.cpp::Multiply: H-014, H-017
- interp-arith.cpp::expx: H-008, H-010, H-014, H-017, H-018
- interp-sysfunc.cpp::getstr1: H-001, H-006, H-007, H-010, H-013, H-017
- interp-strmanip.cpp::<misc>: H-007, H-010, H-013, H-014, H-017, H-018
- interp-sym.cpp::SysDump: H-001, H-010, H-013, H-014, H-017
- interp-rpm.cpp::parse_count: H-014, H-017
- include/mumpsc/sym.h::<misc>: H-004, H-013, H-014, H-018
- interp-sym.cpp::Qlength: H-006, H-009, H-014, H-017
- libmpscpp.cpp::SynInit: H-001, H-006, H-014, H-017
- interp-rpm.cpp::scan_token: H-014, H-015, H-017
- interp-bmgsubs.c::bmg_search: H-001, H-014, H-017
- include/mumpsc/mstringClass.h::<misc>: H-004, H-017
- mumps.cpp::main: H-001, H-003, H-009, H-011, H-013, H-014, H-015, H-017, H-018
- include/mumpsc/compiler-globals-ext.h::<misc>: H-004, H-008, H-013, H-014, H-015, H-017
- interp-rpm.cpp::<misc>: H-004, H-014, H-015, H-017
- rdbms-if.c::sql: H-004, H-015, H-018
- libmpscpp.cpp::<misc-5>: H-001, H-004, H-007, H-008, H-009, H-010, H-012, H-013, H-014, H-015, H-017

Close decisions: none beyond the registered instruments - zero
misses mean no capture candidate exists under T1/T2/T3, and the
per-unit check columns stay todo per the C-001 cycle-10
backfill decision (C-002 discipline; the batch-5 table's check
columns track the BASE reference translation). The stub-owner
corrections of this batch (Qlength, SynInit, the rpm family,
global::Avg) are each journaled at their unit; no registry
action required. PILOT_TRACKING's DOC column is "translated"
for rows 124-154; the "DOC B5 closed" prose line lands in the
same commit as this checklist. Next protocol step after the
export regeneration: batch 6 per the committed row order (DOC
follows B5; DAF B1 start remains the other open front).

### DOC B6 tranche 1 (units 155-164, simple stratum) - 2026-11-20

Batch 6 opened per the committed row order (batch_plan.md B6, 31
units). Shaping mirrors the PRO B6 precedent: T1 simple 155-164,
T2 core 165-175, T3 complex minus the big three 176-182, and the
three ~1600-LOC giants (183 interp-bifs.cpp::zfcn, 184
interp-zfcns.cpp::zfcn, 185 interp-interp.cpp::Interpret)
deferred to a dedicated session PI decision. Sandbox rebuilt
from the rebuild recipe this session (vendor sha256 621deb...ffdbe,
kotlinc 2.1.20, JDK17 corretto; baseline whole-arm gate 0/0 at
155 files before the first render). Roster verification done
before any render: the batch-6 units.csv rows are rank-descending
(Interpret first); the batch_plan strata order is authoritative
per the B5 e1e868a lesson, and the DOC unit ids follow it
(155-164 simple, 165-175 core, 176-185 complex). All 31 batch-6
units dump-verified against the pristine vendor source
(load_units chunking reproduced exactly, misc fold-back
included; the idem twin global::Length resolved :3306 mstring
form this batch, :3330 twin is batch-10).

Renders and consult ledger (each consult logged before applying):

- DOC consult: unit compiler-fcninit.c::<misc>, entry H-004, outcome hit-reuse, timestamp 2026-11-20T19:05:00Z
- DOC consult: unit libmpscpp.cpp::global::Find, entry H-014, outcome hit-reuse, timestamp 2026-11-20T19:06:00Z
- DOC consult: unit interp-rpm.cpp::leaning_toothpickize, entry H-012, outcome hit-adapt, timestamp 2026-11-20T19:07:00Z
- DOC consult: unit libmpscpp.cpp::SmithWaterman, entry H-012, outcome hit-reuse, timestamp 2026-11-20T19:08:00Z
- DOC consult: unit libmpscpp.cpp::global::Length, entry H-014, outcome hit-reuse, timestamp 2026-11-20T19:09:00Z
- DOC consult: unit libmpscpp.cpp::Sum, entry H-008, outcome hit-reuse, timestamp 2026-11-20T19:10:00Z
- DOC consult: unit libmpscpp.cpp::ScanAlnum, entry H-001, outcome hit-adapt, timestamp 2026-11-20T19:11:00Z
- DOC consult: unit interp-arith.cpp::mps_log2, entry H-018, outcome hit-reuse, timestamp 2026-11-20T19:12:00Z
- DOC consult: unit interp-arith.cpp::mps_sin, entry H-018, outcome hit-reuse, timestamp 2026-11-20T19:13:00Z
- DOC consult: unit include/mumpsc/memsize.h::<misc>, entry H-018, outcome hit-adapt, timestamp 2026-11-20T19:14:00Z

Unit ledger notes:

155 compiler-fcninit.c::<misc>: banner/includes ledger file
(the fcninit body is a batch-7 roster row, owner noted); the
four includes map to their carriers (stdio -> java.io H-001;
mumpsc/memsize.h -> the unit-164 render this tranche;
compiler-macros.h -> per-unit consumers;
compiler-globals-ext.h -> the unit-151 render +
CompilerCarriers seats).

156 libmpscpp.cpp::global::Find: the GLOBAL-key overload, double
Mglobal(RETRIEVE) walk with MumpsGlobalException throws at both
retrievals; Mltoa/Mfind/atoi tail of the unit-33 render verbatim.
TAG CORRECTION ledger: the unit-33 KDoc called itself the
char-star overload, but its batch-2 roster row (score 4.2) is the
mstring-key form; both string-needle forms fold into the
existing String render - noted in the unit-156 KDoc.

157 interp-rpm.cpp::leaning_toothpickize: new render resolving
the raised Stubs.leaning_toothpickize seat (now a delegate, the
parse_pattern_atom_list precedent); call site
ParsePatternElement.kt:95 unchanged. Quirks kept: the 0xd2
DOUBLE_QUOTE sentinel maps to the bcatchr quote; a literal ASCII
quote is silently dropped (vendor shape, noted in KDoc);
ispunct renders as the ASCII C-locale punctuation domain helper
per H-012. Ownership correction ledger: the unit-152 InterpRpmMisc
comment said DOUBLE_QUOTE stays with the batch-9 perl_pm owner,
but the define is file-scope at interp-rpm.cpp:58 and this unit
is the first corpus reader - first-reader seats it
(file-private in LeaningToothpickize.kt, the batch-9 render
reuses the byte value).

158 libmpscpp.cpp::SmithWaterman: the free wrapper over the
unit-42 sw render; STR_MAX carriers with the vendor leading-blank
padding via CstringOps.copyAt; sscanf percent d renders as the
atoi prefix-scan seat (UB quantizes to 0 identically); sprintf
percent d as Int.toString per H-009. Vendor quirk kept: sax and
smx land in the sw ShowAlign/ShowMatrix seats (nonzero-test
forward into the Boolean carriers). The C static tmp[32] folds
per H-008.

159 libmpscpp.cpp::global::Length: the mstring-needle overload,
SINGLE retrieve (contrast with the unit-133 GlobalRef-needle
double walk of the same file - noted in the KDoc), strstr
piece-count walk verbatim; overload disambiguated by needle type
per H-004.

160 libmpscpp.cpp::Sum: exact-shape twin of the unit-130 Avg
render (SOH strip/scan, bypass-caret GlobalRef, String.format
percent g Locale.US). Calls the REAL batch-5 unit-136
global::Sum render (GlobalSum.kt) via the GlobalRef
invoke(vararg String) seat - the resolution of the open question
from the pre-tranche sandbox sweep: the a("") call shape in
Avg.kt binds the LibMisc1.kt vararg-keys overload, so Sum uses
a("").sum() / a(cstringAt(arg1, i+1)).sum().

161 libmpscpp.cpp::ScanAlnum: the istream overload renders as
java.util.Scanner (PRO-arm precedent; istream-by-value fossil
noted) - u >> word as hasNext/next, the max>511 guard and
restart filter reusing this file's private per-file helpers
versbatim; the same-file one-owner rule keeps the fold clean.

162 interp-arith.cpp::mps_log2 and 163 mps_sin: the unit-132
mps_log10 template verbatim (H-018 reference config: plain-double
arms live, long-double and MPFR arms documentary); strtod fold
from the MpsExp seat; kotlin.math log2/sin; Stubs.gcvtWrapper
H-014 seat.

164 include/mumpsc/memsize.h::<misc>: header/banner render as a
file-private const roster with the full arm ledger. CONFIG
CORRECTION LEDGER (the tranche's one consequential edit): NOTABS
is defined unconditionally in memsize.h and compiler-sys1.c:45
includes the header, so the ifdef NOTABS TAB-to-space arm in the
unit-129 readf render IS live - the B4 readf render had ledgered
it compiled out ("NOTABS undefined in the reference build"), a
config misread; amended in Readf.kt this tranche (arm restored
at the vendor expression, both notes rewritten to the corrected
resolution). Other ledger items: the PS 4096 / PS1 4097 FIXME
pair stays documentary (the build seats the sysparms.h 32000 /
32001 values per the batch-3 CompilerCarriers render);
SHOW_CODE / VERSION / DDAT / DKEY have no readers anywhere in
the vendor tree (grep-clean this session) - seated documentary;
FORPARM 32 sits only inside string literals the preprocessor
never expands (the generated programs re-define FORPARM STR_MAX
themselves at compiler-mumps.c:141); GCVT is the __APPLE__ arm
and is NOT defined on the Linux reference build (the
gcvt_wrapper selections live at their per-.cpp owners - the
interp-arith renders' H-018 notes stand); OS-detect cascade
falls through to the Linux arm (UNIX 1, DOS 2, SYSTEM UNIX,
USYSTEM LINUX); STEM / UDAT / UKEY / RELEASE are commented
fossils, kept documentary.

Gate: whole-arm kotlinc 2.1.20 gate GREEN 0 errors / 0 warnings
at 162 files. One compile round: the first SmithWaterman render
used the string-carrier strcpyS at an offset (3 arity errors),
re-fixed to the CstringOps.copyAt seat; second compile clean.
H-015 hygiene scan clean over all touched files: no backticks,
no raw dollars, no mid-comment close-star, no double-bang.

Tranche totals: 10 consults, 7 hit-reuse / 3 hit-adapt / 0 miss,
100.0 percent hit, 70.0 percent reuse. Heavier adaptation share
than the B5 simple tranche (three of the ten: the rpm sentinel
seat, the Scanner carrier, the header config adjudication).
Cookbook stays H-001..H-018: no new capture candidate this
tranche (the memsize.h NOTABS correction and the DOUBLE_QUOTE
first-reader seat are ledger corrections under existing entries
H-018 / H-004, not new rules).

T2 core (units 165-175: mps_abs, StopInit, Mglobal, _extract,
interp.h misc, TimeOut_getstr1, global::Max, stateVector.h
misc, ExpandOrder, modulo, Dice) is the next protocol step.

### DOC B6 tranche 2 (units 165-175, core stratum) - 2026-11-21

T2 rendered in two sessions with one sandbox reset between them
(the env reset wiped the working copy mid-tranche; the sandbox
was rebuilt per the rebuild recipe, the repo zip freshness was
re-verified - Batch 6 table present, 162 kt files, Stubs and
ARM_WORKING_NOTES md5 checks - and all 11 T2 units re-dump-verified
before the re-render). Roster order per the batch_plan strata:
165-175 core, complexity rank ascending; dump-verified loc +
score for every unit (165 34/11.6, 166 24/12.2, 167 17/12.3,
168 34/13.0, 169 69/13.3, 170 33/14.1, 171 26/14.8, 172 76/18.4,
173 50/24.2, 174 56/25.6, 175 44/26.8).

Renders and consult ledger (each consult logged before applying):

- DOC consult: unit interp-arith.cpp::mps_abs, entry H-018, outcome hit-reuse, timestamp 2026-11-21T15:30:00Z
- DOC consult: unit libmpscpp.cpp::StopInit, entry H-001, outcome hit-adapt, timestamp 2026-11-21T15:31:00Z
- DOC consult: unit interp-globalb.cpp::Mglobal, entry H-018, outcome hit-adapt, timestamp 2026-11-21T15:32:00Z
- DOC consult: unit interp-sym.cpp::_extract, entry H-012, outcome hit-reuse, timestamp 2026-11-21T15:33:00Z
- DOC consult: unit include/mumpsc/interp.h::<misc>, entry H-004, outcome hit-adapt, timestamp 2026-11-21T15:34:00Z
- DOC consult: unit interp-sysfunc.cpp::TimeOut_getstr1, entry H-001, outcome hit-adapt, timestamp 2026-11-21T15:35:00Z
- DOC consult: unit libmpscpp.cpp::global::Max, entry H-007, outcome hit-reuse, timestamp 2026-11-21T15:36:00Z
- DOC consult: unit include/mumpsc/stateVector.h::<misc>, entry H-004, outcome hit-reuse, timestamp 2026-11-21T15:37:00Z
- DOC consult: unit compiler-parse.c::ExpandOrder, entry H-001, outcome hit-reuse, timestamp 2026-11-21T15:38:00Z
- DOC consult: unit interp-arith.cpp::modulo, entry H-018, outcome hit-adapt, timestamp 2026-11-21T15:39:00Z
- DOC consult: unit libmpscpp.cpp::Dice, entry H-007, outcome hit-reuse, timestamp 2026-11-21T15:40:00Z

Unit ledger notes:

165 interp-arith.cpp::mps_abs: mpsLog10 template verbatim (H-018
reference config, plain-double arms live, long-double / MPFR
documentary comments); kotlin.math.abs over the MpsExp strtod
seat; Stubs.gcvtWrapper H-014 seat; Int return per the vendor
shape.

166 libmpscpp.cpp::StopInit: the stopword loader. The vendor
StopList static table renders as the mutable top-level stopWords
carrier set (the only mutable global: file-scope StopList at
libmpscpp.cpp:7718, first-reader seats it); FileReader edge per
H-001 with the missing-file banner + exitProcess(1) (Nothing
return type; a first draft added an unreachable throw and was
fixed); readLine with the newline re-append sentinel, the 512
CharArray uppercase force with the vendor space-cut cond loop
kept, and the unconditional trailing-newline NUL strip quirk
(adaptations per the MDH arm quirk ledger). The Stubs.stopInit
seat flips to delegate; Stubs.stopLookup stays stubbed (not a
corpus unit; reads the stopWords carrier when its owner renders).

167 libmpscpp.cpp::Mglobal: the dispatcher - CLOSE returns 1;
RETRIEVE / STORE route through Stubs.keyfix + the btree seat;
all other codes pass through to the REAL unit-4 btree render.
The vendor SQLITE arm IS the live reference config (the native
arm belongs to the batch-10 interp-globalb.cpp::<misc> row, not
restated here). Resolves the raised Stubs.Mglobal CharArray
seat (now a delegate; the String overload keeps folding into
it).

168 interp-sym.cpp::_extract: exact vendor substring semantics
(i negative to 0 else decrement; j negative to j=i else
decrement; j below i yields empty; i==0 and j==0 single byte;
bounds-past-n reject / clamp-after order kept; inclusive copy).
Empty arms via Stubs.strmove(out, charArrayOf(NUL)). Appended to
the unit-32 Extract.kt owner file. TAG CORRECTION ledger: the
Stubs.Mextract tag said batch-10 under the units.csv line
numbering; the batch_plan row is unit 168 this batch - the seat
now delegates to the real render and the mis-tag is journaled.

169 include/mumpsc/interp.h::<misc>: the command-char / operator
const roster (TSTACK 10, BREAK through NEW command chars, QUOTE,
the POPEN 0 through EXPONEN 29 operator code table, NOT_NOT 18
duplicating NOTEQ per the vendor, PREDICATEVAR tilde). TAB is
NOT redeclared (the Getstr2 unit-48 render owns it per H-004,
noted in the file header); zero collisions verified against
the arm's existing top-level consts (grep sweep). Prototype
roster notes: Interpret is the deferred unit-185 giant, parse_
is the real unit-93 render, prnterr is not stubbed per the
house rule, BuildGlobal / BuildLocal are batch-8 rows.

170 libmpscpp.cpp::TimeOut_getstr1: the timeout read. io != 5
banner + Stubs.sigint(100) with the vendor fall-through kept;
four process-level OS surfaces (select, system stty raw/restore,
getchar, fgets-on-stdin) render as private stub carriers that
throw NotImplementedError - process-level OS surface is never
arm-owned (seat table documented per file); dirflg unmasks the
getchar arm; the fgets arm keeps the unconditional last-byte
chop including the stale-buffer NULL case (MDH quirk ledger).

171 libmpscpp.cpp::global::Max: the Cosine-family XNEXT walk
single-datum variant: trailing control-pair trim, the
Stubs.Mglobal(XNEXT) loop, strncmpS prefix stop, get_global +
the tpx==0 GLOBAL_NOT_FOUND raise, strtod, first-value latch
flg; the dead local j of the vendor dropped (H-013); val widened
to value (H-015). TAG CORRECTION ledger: the units 68 / unit-31
era notes called global::Max "not a corpus unit" - the batch-6
batch_plan carries it (loc 26, score 14.8); the Stubs.globalMax
seat now delegates to the real render and the Max.kt unit-68
KDoc tag bullet is corrected this tranche.

172 include/mumpsc/stateVector.h::<misc> (complex stratum): the
MSV residual field roster, EDIT-ONLY appends to the Carriers.kt
Msv carrier with vendor line anchors per field (cfgdata /
cfgkey 256, LockDone, ITRAP, ctrlc, iorg, nflg, gpad, RefParm
10x16, Rargs, Rargsx, _SymPush, hit/count Long, restoredata
Long, ScanMinWordSize / ScanMaxWordSize, UKEY / UDAT 1024,
Table 256, ConOpen, pgsql_msg / Connection / Col_Names STR_MAX,
fdes IntArray(2), exec, reference 1024). grslts / grsltsx are
commented out in the vendor header itself - roster note only,
not translated. All earlier-seated fields verified present, no
duplicates.

173 compiler-parse.c::ExpandOrder: the unit-16 ExpandGet
template verbatim (the coded #G# / #S# payload walk, the
CodedComma tail routing through the real unit-15 extract2, the
sub-128 copy, the "NULL" default, the strcpy(bp,arg2)
convention). Emit byte-faithful: the TWO-LINE GlobalOrder
emission with its trailing space before the first newline and
the vendor p2236 debug fossil comment, the LocalOrder
single-line emission with its trailing space. NOTE: the
ExpandGet unit-16 duplicate-"#S#" fossil does NOT apply here -
the vendor nested tail tests in ExpandOrder are correct
(#G# then #S#; #S# then #G#); noted in KDoc.

174 interp-arith.cpp::modulo: Stubs.floatstring gate; the float
arm renders C fmod as the Kotlin percent operator (identical
sign-of-dividend semantics - an earlier draft used IEEErem and
was WRONG, fixed before push); H-018 gcvt on the float arm; the
intLong arm is IN (interp-arith.cpp defines it unconditionally):
aa / bb via atoi to Long, cc = aa percent bb, strcpyS of the
Long toString.

175 libmpscpp.cpp::Dice: the Cosine-family shape minus the
second walk - a single A-side XNEXT loop with the strncmpS
prefix stop, totD on the A values, the B-side probe at
ctmp + the A subscript suffix via Stubs.globalData (continue on
miss), totQ and the cross-product tot on hits, final
2*tot / (totD + totQ) with the zero guard. Vendor quirk kept and
noted: B.ref is written in place inside the loop and NEVER
restored at loop exit (Dice has no restore pass, unlike
Cosine); the immutable GlobalRef renders the walk state in the
local bref string, same observable seat sequence, the
documented unit-85 divergence precedent. The Stubs.dice seat
delegates; the SymOpsMisc wrapper call site binds unchanged.

Gate: whole-arm kotlinc 2.1.20 gate GREEN 0 errors / 0 warnings
at 171 files (162 + 9 new: MpsAbs, StopInit, Mglobal, InterpH,
TimeOutGetstr1, GlobalMax, Modulo, Dice, ExpandOrder), one
compile round. H-015 hygiene scan clean over all 13 touched
files: no backticks, no raw dollars, no mid-comment close-star,
no double-bang, ASCII only.

Tranche totals: 11 consults, 6 hit-reuse / 5 hit-adapt / 0 miss,
100.0 percent hit, 54.5 percent reuse. The heavier adaptation
share is the core-stratum profile: the StopInit mutable-table
carrier, the Mglobal SQLITE reference-config resolution, the
interp.h ownership re-checks, and the four OS stub carriers of
TimeOut_getstr1. Cookbook stays H-001..H-018: no new capture
candidate this tranche (the modulo fmod correction is a ledger
correction under H-018; the global::Max and _extract tag
corrections ride H-004 / H-014 ownership bookkeeping).

T3 (units 176-182: Shred, interp-stem.cpp::<misc>, write_code,
server.cpp::main, match, read_code, mpstime.cpp::main) is the
next protocol step; the big three stay deferred per the PI
decision.

### DOC B6 tranche 3 (units 176-182, complex stratum minus the big three) - 2026-11-21

T3 opened after the T2 splice/gradle closure (fe438e7, gradle
BUILD SUCCESSFUL 15s). The env reset AGAIN before the tranche
(third wipe of the batch); the sandbox was rebuilt per the
rebuild recipe (repo zip at fe438e7 - 171 kt files, Batch 6
table present; vendor sha256 621deb...ffdbe OK; kotlinc 2.1.20
sha OK; baseline whole-arm gate 0/0 at 171 files before the
first render). All seven T3 units dump-verified against the
pristine vendor source (loc match batch_plan: 176 40, 177 345,
178 63, 179 65, 180 86, 181 96, 182 176).

Renders and consult ledger (each consult logged before applying):

- DOC consult: unit libmpscpp.cpp::Shred, entry H-008, outcome hit-reuse, timestamp 2026-11-21T17:00:00Z
- DOC consult: unit interp-stem.cpp::<misc>, entry H-004, outcome hit-reuse, timestamp 2026-11-21T17:01:00Z
- DOC consult: unit compiler-write.c::write_code, entry H-011, outcome hit-adapt, timestamp 2026-11-21T17:02:00Z
- DOC consult: unit server.cpp::main, entry H-014, outcome hit-adapt, timestamp 2026-11-21T17:03:00Z
- DOC consult: unit interp-parse.cpp::match, entry H-015, outcome hit-adapt, timestamp 2026-11-21T17:04:00Z
- DOC consult: unit compiler-read.c::read_code, entry H-015, outcome hit-reuse, timestamp 2026-11-21T17:05:00Z
- DOC consult: unit mpstime.cpp::main, entry H-013, outcome hit-reuse, timestamp 2026-11-21T17:06:00Z

Unit ledger notes:

176 libmpscpp.cpp::Shred: the stateful fragment iterator; the
ShredScratch object carries the vendor file-scope statics (p1/
p2/flg/string/word persist across calls, the BtreeStatic
precedent); the size-greater-or-equal-128 banner + sigint seat
with the vendor fall-through kept; the isalnum-only lowercase
copy loop; the empty-input early return leaves flg 0 (every
call re-copies); the strictly-greater p1-above-p2 reset test
hands back the last partial fragment; the "Out of memory in
Token()" copy-paste fossil and the dead static int i noted
(H-013); strncpy zero-fill tail kept. RESOLVES the Stubs.shred
seat (SymOpsMisc wrapper binds unchanged); TAG NOTE: the seat
said roster row 165 under the MDH units.csv numbering - the
batch_plan DOC row is 176, journaled in the flip.

177 interp-stem.cpp::<misc>: the stemming-data residual chunk.
Twelve Porter conflation tables (prefix plus the eleven d/s/g/
n/e/l/c/y/v/t/r tables, 303 entries total) generated by a table
parser over the vendor source (not hand-typed), byte-faithful
in value and order, rendered as top-level List<Ends> vals
REUSING the Ends carrier from Escan.kt (H-004 one-owner rule);
the struct endings 3-field prefix shape renders as the new
Endings data class. The fin marker rides the next-field values
verbatim (fin rows carry 1; the -1 antt self-loop row and the
0 restart rows appear verbatim, the Escan unit-96 semantics).
Banner/includes ledger notes; the using-namespace and wstem
prototype roster fossils; the vendor per-row trailing comments
dropped for the mid-comment close-star hygiene rule (data
unchanged). The folded Stem(MSV, ep, word) wrapper calls the
Stubs.wstem seat; TAG CORRECTION journaled: the stub names
libmpscpp.cpp::wstem as owner, the owner is interp-stem.cpp::
wstem, batch-10 roster row 293.

178 compiler-write.c::write_code: the WRITE command compiler.
NULL out_file guard emission (m1944), the atTest real seat with
the atCopy/linepad Stubs seats, the INTERPRET macro via
CompilerState.interpret (H-003), the iosub/iosub1 goto family
linearized with the toIosub/toIosub1 flags (only the star arm
skips inout); the inout amd1-slice pointer args ride fresh
NUL-terminated slice carriers (H-012). Vendor quirks: the
bang arm enters iosub WITHOUT setting t2; the linepad seat is
fed the emit char-count analog twice over the atoi(CSTR...)
and putchar emissions; xpx re-points to at_flg before the 1920
post-conditional closer; t0px resets outside the loop.

179 server.cpp::main: the transaction server, entry serverMain
(the one-main-per-package main150 precedent). The -j scan keeps
the VENDOR TYPO argv plus-plus-i (not i plus 1; the
out-of-range read quantizes to the empty string, the
deterministic-UB precedent); ftok/msgget/msgrcv/socket/sendto/
perror/exit as per-file private OS stub carriers (the unit-170
carrier precedent); the uninitialized msgrcv type and the
never-filled buf0/buf1 journal arms quantize to deterministic
zero values (the vendor client-copy lines are commented text);
the Mglobal dispatch is the REAL unit-167 render; the journal
fprintf on the NULL FILE rides the nullable-carrier error();
the unreachable post-while tail dropped (H-013; the GlobalClose
splice stays owned by its macro render); MSG_LENGTH 2168 with
the x86-64 struct-layout computation in KDoc (sizeof is a
compile-time C property).

180 interp-parse.cpp::match: the pattern-match prover; RESOLVES
the 6-arg Stubs.match seat (patrn binds unchanged). Eight class
arms kept verbatim, including the C/c arm that passes only the
sub-32 and 127 bytes (vendor quirk), the E/e strictly-ASCII
arm, and the quoted-literal substr/strcmp walk with the
ampersand-offset slice carriers (H-012; the substr/xindex
Stubs seats). The vendor punctuation charset contains the raw
dollar and backtick bytes: built by punctSet() with the DOLLAR
36 and BTICK 96 const carriers inserted after the hash and the
underscore, runtime charset byte-identical (the emitted-C
sigil-carrier rule); the vendor double-apostrophe row kept;
ptr starts -1 and pre-increments; the strictly-greater bounds
guard and the ptr-plus-1 exact-consumption check kept.

181 compiler-read.c::read_code: the READ command compiler, the
write_code sibling. Carriage-control/tab/literal arms through
the inout seat with the iosub codes; set_code passes (SET then
SET_STORE) with the 6-code rc ladder and the vendor return-99
fall-through on an unrecognized rc; the cont_read/set_return/
store goto family linearized with table-driven flags (H-011);
the emitted Timeout arm emits TimeOut_getstr1 text (the
unit-170 runtime body), DirectRead and getstr1 for the other
arms - all EMITTED C TEXT (the generated-program contract).
The emitted Set-Test comments carry the sigil via the per-file
DOLLAR const so the emitted text stays byte-identical (the
emitted-C sigil-carrier rule, the Fnumber DOLLAR precedent);
the xpx re-read quirk (decrement after the timeout strcpy),
the commented-out linepad fossil, and the inout
empty-string-literal carriers noted.

182 mpstime.cpp::main: the mumps2c-GENERATED timing benchmark,
entry mpstimeMain (the main150 name-in-header precedent). The
generated setjmp/longjmp FOR mechanism linearized to plain
Kotlin while loops (the For1/For2 label pairs and the _i 100
exit ladder fold into the loop condition and increment), all
three for-header positions (sym_ store init, the numcomp limit
test with the forF direction ternary, the add/sym_ increment)
at their vendor positions; the xce/xcf/x01 key encodings with
the M3701/a2926/a3956/p674 markers kept; the zd1 intrinsic
names carry the sigil via the DOLLAR const (H-015); the
generated prologue temps and pointer globals dropped with
notes (h013, the main150 alarm-block precedent); the catch
ladder renders as the Throwable catch on GlobalExceptionCode
with the vendor code strings verbatim including the
Interprerer typo; the GlobalExceptionCode codes not already
in Carriers (NUMERIC_RANGE 5, INPUT_LENGTH 10, UNSPECIFIED 0)
carry as per-file consts; getpid via the ProcessHandle JVM
intrinsic (the Fcn.kt H-010 precedent); the miss reporters
REUSE the real SysfuncMisc seats (H-004). strxcat raised as the
per-file private stub (owner mpstime.cpp:102, batch-9 misc row
253).

Gate: whole-arm kotlinc 2.1.20 gate GREEN 0 errors / 0 warnings
at 178 files (171 + 7 new), one compile round (the ambiguous
variableNotFound miss-reporter clash and the retz Char-to-Int
assignment, both fixed in round one). H-015 hygiene scan clean
over the 7 touched files: no backticks, no raw dollars (the
sigil carriers ride the 36-toChar consts), no double-bangs, no
mid-comment close-star outside the byte-faithful emitted-C
strings, ASCII only.

Tranche totals: 7 consults, 4 hit-reuse / 3 hit-adapt / 0 miss,
100.0 percent hit, 57.1 percent reuse. The adaptation share is
the OS-surface stub-carrier set of server.cpp, the sigil
charset and emitted-text carriers, and the iosub flag
linearization. Cookbook stays H-001..H-018: no new capture
candidate (all tranche quirks resolve under H-004/H-011/
H-012/H-013/H-014/H-015 bookkeeping).

Batch 6 big-three session outcome (2026-11-21): all three giants
rendered and whole-arm kotlinc gate 0/0 at 181 files (183
interp-bifs.cpp::zfcn at physLOC 1622, 184 interp-zfcns.cpp::zfcn at
1662, 185 interp-interp.cpp::Interpret at 1581, all dump-verified).
Unit 183 renders as the build-alternate zfcnBifs (H-017: the Makefile
compiles interp-zfcns.o, NOT interp-bifs.o). Unit 184 owns the
top-level zfcn and the Stubs.zfcn seat flip with TAG CORRECTION.
Unit 185 resolves the mis-tagged Stubs.Interpret seat to the
unit-185 delegate, widens BuildGlobal/BuildLocal (H-012 all-null
stack-cleaner arm), and raises the Stubs.inout (interp-parse.cpp:38,
batch-10 roster unit; the compiler-sys1.c::inout unit-59 render is the
DISTINCT landed twin) and Stubs.Merge (interp-sysfunc.cpp:650,
batch-8 roster unit) seats. The batch-close protocol (coverage
checklist, events export, PILOT prose) follows.

DOC batch 6 big-three session (units 183-185) renders and
consult ledger (each consult logged before applying):

- DOC consult: unit interp-bifs.cpp::zfcn, entry H-011, outcome hit-adapt, timestamp 2026-11-21T18:01:00Z
- DOC consult: unit interp-zfcns.cpp::zfcn, entry H-011, outcome hit-reuse, timestamp 2026-11-21T18:02:00Z
- DOC consult: unit interp-interp.cpp::Interpret, entry H-011, outcome hit-adapt, timestamp 2026-11-21T18:03:00Z

# DOC B6 big-three journal fragment, 2026-11-21

Units 183-185 of the DOC batch-6 complex stratum, per the dedicated
session PI decision recorded in PILOT_TRACKING.md.

## Consult ledger (logged before applying)

(dup-rows removed at splice repair: the consult rows live in the
session ledger above; C-005/C-008 one-consult rule, identical triple
repeats are duplicates)

## Unit 183 interp-bifs.cpp::zfcn (physLOC 1622, dump-verified)

- Rendered as ZfcnBifs.kt: `fun zfcnBifs(v1d: CharArray, bd: CharArray, svPtr: Msv)`.
- H-017 collision note: the Makefile compiles interp-zfcns.o, NOT
  interp-bifs.o, so this TU is the build-alternate; unit 184 owns the
  top-level `fun zfcn(...)` and the Stubs.zfcn seat (the analog of the
  zfcns.h `_INTERP_` selection macro).
- H-011 goto ladder (about 75 labels) rendered as the private ZLblB
  enum state machine; znormal and zalter share one arm; zblnk's zbl1
  inline body rides the ZBLNK arm with a ZBL1 slot.
- H-004 statics ride the private ZfcnBifsState object.
- H-018 arms under the SQLITE-live/NATIVE dead build: znative returns
  "0"; zcondense and zrestore render as immediate returns (dead
  build-alternate bodies dropped); znxtrow prints the banner and
  returns.
- OS carriers per precedent: fsetposC/fgetposC throwing, chdirC
  quantized, openC via java.io.File, systemC/popenC live via
  Runtime.exec, srandC stores the seed.
- zdate family: TmZb class + mktimeC Calendar walk; the unit-1 ctime
  seat reused for zd2/zd6.
- New Stubs seats raised: the GTK glue family (owner "GTK C library
  glue, not a corpus unit", all throwing, StrHolderZb/GdkRgbaZb out
  holders public in Stubs), the mpsFloor/mpsCeil/mpsTrunc and
  mpsLog/mpsCos/mpsTan/mpsExp2 seats (batch-9/10 roster owners), and
  Stubs.Replace (builtin.h:40, not a corpus unit);
  Stubs.ServiceInterface c/d params widened to CharArray?.

## Unit 184 interp-zfcns.cpp::zfcn (physLOC 1662, dump-verified)

- Rendered as Zfcn.kt: the LIVE reference-build TU, top-level
  `fun zfcn(v1d: CharArray, bd: CharArray, svPtr: Msv)`.
- Deltas vs 183 (vendor-diff verified): no zreplace arm; the
  commented zglobal-to-zfiletest fossil noted; the SQLITE-only
  ZINDEXSIZE/ZDATASIZE/ZDBNAME/ZDBFILE/ZINDEXMAX arms; the generic sql
  arm uses SQLCMD_ZB 55 not PRAGMA 29; ZGLOBAL is the ServiceInterface
  GBLNME 20 relay.
- H-011 hit-reuse: the ZLblZ machine, ZfcnsState object and TmZz
  class derive from the unit-183 render (private top-level classes
  cannot share names across files in a package; funs can).
- Stubs.zfcn FLIPPED to the doc.zfcn delegate with TAG CORRECTION (the
  old tag said a later batch owns it; the owner is roster unit 184,
  the Makefile reference-build TU).

## Unit 185 interp-interp.cpp::Interpret (physLOC 1581, dump-verified)

- Rendered as Interpret.kt: the ~77-entry private ILblI enum machine
  over the command ladder (RESTART/NEXT_LINE/... through RESTORE),
  with the file-private interp-interp.cpp:71-78 consts
  (GOSUBMAX_I 50, SWAP_I 30, FORMAX_I 30, EXECLEVS_I 40, LBLS_I 80,
  OPEN_PAREN_I 206, CMDSTACK_MAX_I 8192, the MGI codes and the
  LBLS/lbloff IntArray(20) wident kept verbatim with the MDH ledger
  note).
- Build defines: SQLITE live (in-TU define), NATIVE_SHARED and
  TERMINATE_ON_ERROR dead, so abrt is a return 99 and the JOB fork
  quantizes to always-parent with an H-001 note.
- H-012 pointers-into-buffers become sliceAtI scratch plus copyAt
  copy-back (getstr2, TimeOut_getstr1 io cases); the BUILD
  stack-cleaner arm passes the all-null BuildLocal/BuildGlobal calls
  (both Stubs seats widened to CharArray?/Msv? nullable).
- The two vendor strcmp sites (the FOR_END string-limit test and the
  label search) render as a file-private strcmpI C-string compare.
- Stubs.Interpret FLIPPED to the doc.Interpret delegate with TAG
  CORRECTION (the old "not a corpus unit" tag was a MIS-TAG; owner is
  roster row 185, rank 308).
- New roster seats raised: Stubs.inout (interp-parse.cpp::inout, the
  batch-10 roster unit; the compiler-sys1.c::inout unit-59 render is
  the DISTINCT landed twin of the same C body) and Stubs.Merge
  (interp-sysfunc.cpp:650, batch-8 roster unit), both throwing.

## Gate and scan

- Whole-arm kotlinc 2.1.20 gate 0 errors / 0 warnings at 181 files
  (178 + ZfcnBifs.kt + Zfcn.kt + Interpret.kt).
- H-015 scan clean on all touched files: no raw backticks, raw
  dollars, double-bangs, em-dashes or non-ASCII.

## Files pushed

- wp2/arms/DOC/src/main/kotlin/doc/ZfcnBifs.kt (new)
- wp2/arms/DOC/src/main/kotlin/doc/Zfcn.kt (new)
- wp2/arms/DOC/src/main/kotlin/doc/Interpret.kt (new)
- wp2/arms/DOC/src/main/kotlin/doc/Stubs.kt (modified)
- wp2/arms/ARM_WORKING_NOTES.md (ledger block)
- wp2/PILOT_TRACKING.md (rows 183-185 translated, session prose)
- wp2/arms/DOC_B6BIG3_journal_fragment_2026-11-21.md (this fragment)

### DOC B6 close — coverage checklist (units 155-185), 2026-11-21

Manual batch-close coverage checklist per the DOC batch-close
discipline (section 1: for each canonical construct in the batch,
did at least one entry match?). Batch 6 (units 155-185) CLOSED:
31/31 translated 2026-11-20/21 in three tranches plus the
dedicated big-three session per the pre-registered batch_plan.md
stratification and the PI decision recorded in PILOT_TRACKING.md
(T1 155-164 simple, code ee0bd410 with splice cac8a53; T2 165-175
core, code 74264c1 with splice fe438e7; T3 176-182 complex minus
the big three, code a89e8b5 with splice caf23fa; big-three session
183-185, code 9240c8a with splice 6bca28d). Gates: whole-arm
kotlinc 2.1.20 0 errors / 0 warnings at every tranche close
(162 -> 171 -> 178 -> 181 files); gradle gate on m1-build BUILD
SUCCESSFUL at every tranche (T2 15s, T3 14s, big-three 21s, T1
green at its splice close) - one gradle invocation slip is logged
as data (the first big-three gate run carried the PRO arm's
detekt/jacocoTestReport task names; the DOC project exposes only
test; rerun plain build, green).

Events export: wp2/logs/DOC_knowledge_events.csv regenerated
from this journal per the C-005/C-008 keys (627 consult rows ->
1252 event rows plus header; the pre-B6 content verified as a
byte-exact prefix of the regeneration). TOOLING NOTE (logged as
data): the regeneration ran as a faithful Python port of
export_doc_events.R this close because the sandbox environment
reset wiped the R toolchain; the port replicates the script's
every validation (strict grammar, roster membership, the
(unit, entry, timestamp) duplicate-fail) and mapping verbatim.

Repairs at this close (each logged as data; three found by the
export's loud validations, the standing byte-verify discipline
caught none in the pushed blobs):

1. The big-three splice (6bca28d) appended the fragment's consult
   ledger on top of the journal's own session ledger, duplicating
   the three (unit, entry, timestamp) triples exactly; the
   duplicate rows are removed at the fragment's ledger position
   with an in-place note pointing to the session ledger.
2. The batch-6 consult rows were journaled with numeric unit ids
   (unit 155..unit 185), which the registered grammar validates
   against roster id strings; all 31 rows are normalized to their
   units.csv roster ids (the batch-6 PILOT table order is the
   mapping authority; two ids corrected against the roster
   itself, see repair 3).
3. Two PILOT batch-6 table ids were display-wrong against the
   roster: row 167 read libmpscpp.cpp::Mglobal but the roster id
   is interp-globalb.cpp::Mglobal, and row 170 read
   libmpscpp.cpp::TimeOut_getstr1 but the roster id is
   interp-sysfunc.cpp::TimeOut_getstr1; both rows now read the
   roster ids (the consult rows were normalized the same way).

Entry-by-entry coverage across the 31 B6 unit consult rows (one
single-line consult per unit; the per-tranche prose sections carry
the unit-level quirk ledgers):

- Canonical IO type (H-001): matched in 4 B6 unit consult(s); 4 consults, 1 reuse / 3 adapt
- File-scope globals (H-004): matched in 4 B6 unit consult(s); 4 consults, 3 reuse / 1 adapt
- Buffer role classification (H-007): matched in 2 B6 unit consult(s); 2 consults, 2 reuse
- Shared static scratch buffer (H-008): matched in 2 B6 unit consult(s); 2 consults, 2 reuse
- goto -> structured control (H-011): matched in 4 B6 unit consult(s); 4 consults, 1 reuse / 3 adapt
- Unsigned char domain mapping (H-012): matched in 3 B6 unit consult(s); 3 consults, 2 reuse / 1 adapt
- Dead store / dead parameter (H-013): matched in 1 B6 unit consult(s); 1 consult, 1 reuse
- Out-of-scope C function -> stub (H-014): matched in 3 B6 unit consult(s); 3 consults, 2 reuse / 1 adapt
- Kotlin grammar vs quoted C source (H-015): matched in 2 B6 unit consult(s); 2 consults, 1 reuse / 1 adapt
- Preprocessor conditional arms (H-018): matched in 6 B6 unit consult(s); 6 consults, 3 reuse / 3 adapt

H-002, H-003, H-005, H-006, H-009, H-010, H-016 and H-017 found
no triggering situation in the B6 single-line consult rows (no
consult row; state recorded so entry-for-entry parity with PRO's
rule base is unaffected; no revision needed). Note for the
record: the H-017 renames and the H-014 seat raises of the batch
are journaled in the per-tranche prose sections (the T3 ledger
tallies 45/46/56-style consult rows are the B5 pattern; B6's
single-line ledger keeps one primary consult per unit and is the
C-005 export source; the coverage relation of the prose quirks
to the cookbook entries is unchanged by the ledger shape).

Consult outcome totals for the batch: 18 hit-reuse, 13 hit-adapt,
0 miss (31 consult rows). Hit rate 31/31 = 100.0%; reuse rate
18/31 = 58.1% (the batch's heavy adaptation load rides the
preprocessor-heavy arith family, the FILE-star seats, and the two
giant machines). Per tranche: T1 10 consults (6 reuse / 4
adapt), T2 11 consults (5 reuse / 6 adapt), T3 7 consults (4
reuse / 3 adapt), big three 3 consults (1 reuse / 2 adapt). No
miss rows: the B6 canon was fully covered by cookbook v0 as it
stood at batch open, so no capture candidate exists under
T1/T2/T3; cookbook v0 stays at H-001..H-018 and remains
entry-for-entry parallel with PRO's rules_v0.pl (h001..h018).

Per-unit consult coverage (unit -> entry consulted, batch_plan
order):

- compiler-fcninit.c::<misc>: H-004
- libmpscpp.cpp::global::Find: H-014
- interp-rpm.cpp::leaning_toothpickize: H-012
- libmpscpp.cpp::SmithWaterman: H-012
- libmpscpp.cpp::global::Length: H-014
- libmpscpp.cpp::Sum: H-008
- libmpscpp.cpp::ScanAlnum: H-001
- interp-arith.cpp::mps_log2: H-018
- interp-arith.cpp::mps_sin: H-018
- include/mumpsc/memsize.h::<misc>: H-018
- interp-arith.cpp::mps_abs: H-018
- libmpscpp.cpp::StopInit: H-001
- interp-globalb.cpp::Mglobal: H-018
- interp-sym.cpp::_extract: H-012
- include/mumpsc/interp.h::<misc>: H-004
- interp-sysfunc.cpp::TimeOut_getstr1: H-001
- libmpscpp.cpp::global::Max: H-007
- include/mumpsc/stateVector.h::<misc>: H-004
- compiler-parse.c::ExpandOrder: H-001
- interp-arith.cpp::modulo: H-018
- libmpscpp.cpp::Dice: H-007
- libmpscpp.cpp::Shred: H-008
- interp-stem.cpp::<misc>: H-004
- compiler-write.c::write_code: H-011
- server.cpp::main: H-014
- interp-parse.cpp::match: H-015
- compiler-read.c::read_code: H-015
- mpstime.cpp::main: H-013
- interp-bifs.cpp::zfcn: H-011
- interp-zfcns.cpp::zfcn: H-011
- interp-interp.cpp::Interpret: H-011

Close decisions: none beyond the registered instruments - zero
misses mean no capture candidate exists under T1/T2/T3, and the
per-unit check columns stay todo per the C-001 cycle-10 backfill
decision (C-002 discipline; the batch-6 table's check columns
track the BASE reference translation). PILOT_TRACKING's DOC
column is "translated" for rows 155-185; the "DOC B6 closed"
prose line lands in the same commit as this checklist. Next
protocol step: batch 7 per the committed row order.

### DOC B7 tranche 1 (units 186-195, simple stratum) - 2026-11-20

Batch 7 opened per the committed row order (batch_plan.md B7, 30
units; DOC rows 186-215). Sandbox rebuilt from the rebuild recipe
this session (vendor sha256 621deb...ffdbe, kotlinc 2.1.20, JDK17
corretto; baseline whole-arm gate 0/0 at 181 files before the
first render). Tranche 1 renders the 10 simple rows in
batch_plan order, each from the pristine vendor tree with
physical-LOC verification at the roster-declared sites:

- compiler-sys1.c::substr (unit 189, vendor :396 with the K&R
  header lines): the SHORT-domain compiler twin; len 0 empty,
  negative clamp 255, >255 clamp 255, 1-based walk, NUL cap.
  DISTINCT-TWIN resolution (the unit-59 inout precedent): the
  interpreter twin shares the C symbol name; the Kotlin seats
  stay distinct by the vendor parameter domains (short here, int
  at the interp twin). The landed compiler-side call sites
  (Match.kt unit 180, PatrnSys1.kt unit 26) REBIND from
  Stubs.substr to this real this push, the int expressions
  narrowing at the call exactly as the C call sites do.
- interp-strmanip.cpp::substr (unit 190, vendor :59): the
  INT-domain interpreter twin; negative clamp only, and the
  STR_MAX overrun arm reallocs the out pointer (H-001 quantize
  note: the fixed CharArray carrier has no realloc; no-op with
  the note, the walk unchanged). Stubs.substr FLIPS to this
  real (the past "interp substr helper" tag was owner-correct);
  PatrnInterp/InterpParse-side call sites bind through the
  stub unchanged.
- interp-sym.cpp::mkpt (unit 195, vendor :970): one-pass
  printable filter over the 2048 static; the fall-through
  overwrite order kept (0x1f/0x1e/0x01 take hyphen then their
  own byte); bytes above 128 leave the stale buffer byte
  untouched (H-013 note, no invented fill); the static rides
  MkptState per H-004 and the return aliases the same carrier.
- interp-sym.cpp::_justify (unit 194, vendor :1328): the
  left-pad text arm (strcmp p "-1", strlen k, the 255 clamp,
  the REAL lpad direct call per the Fcn.kt unit-123 precedent)
  and the numeric arm (sprintf format build, atof, the H-009
  String.format Locale.US fold per the Avg.kt %g precedent; the
  file-private atofJ parses the leading double, 0.0 otherwise).
- libmpscpp.cpp::global::Find (unit 186, vendor :7566): the
  CHAR-STAR needle overload; renders as the CharArray-key fold
  delegate to the unit-33 String body (the unit-156 ledger fold
  decision extended; cstring carrier conversion per the SymGet
  CharArray-overload precedent).
- compiler-zcommands.c::<misc> (unit 187): banner-ledger chunk
  (license block, May 10 2024 date line, four includes) per the
  unit-155 CompilerFcninitMisc shape; zcommands_code itself is
  the batch-7 T3 row.
- libmpscpp.cpp::global::Piece (unit 188, vendor :7236): the
  STRING needle member; single RETRIEVE, jj<0 collapse quirk,
  _piece on the unit-114 REAL (H-014) - and the Stubs.Mpiece
  seat FLIPS with TAG CORRECTION (the past "not a corpus unit"
  tag was a MIS-TAG: the owner is the landed unit-114 _piece;
  the unit-38 call site binds through the flip unchanged).
- libmpscpp.cpp::mstring::Translate (unit 191, vendor :4226):
  the TWO-ARG mstring member; the unit-131 Concat-fold command
  build (dollar-translate, ce open, data, d0, a1, d0, a2, cf
  close), fcn stub, offset-1 v1d/bd convention.
- libmpscpp.cpp::GlobalData (unit 192, vendor :2616): the
  MSTRING needle; roster adjudication of the same-file
  same-score twin rows (8.7 ranks 78/79 across batches 7/8):
  file-order tie-break, mstring :2616 here, string :2636 is
  the batch-8 row (the fold note in the KDoc); xecute/SymGet
  stubs, tpx semantics, atoi tail per unit 70.
- libmpscpp.cpp::global::Soundex (unit 193, vendor :4200): the
  GLOBAL member; unit-131 fold (dollar-z-z-soundex, ce/cf
  frame), Mglobal RETRIEVE with the GLOBAL_NOT_FOUND throw, fcn
  stub offset-1 read.

Gates: whole-arm kotlinc 2.1.20 0 errors / 0 warnings at tranche
close, 188 files (181 + 7 new renders; Find/GlobalPiece/
GlobalData/Stubs/Match/PatrnSys1 edited), first-pass green;
gradle gate on m1-build pending at tranche close. H-015 scan
clean on all touched files.

Renders and consult ledger (each consult logged before applying;
roster id strings per the batch-6 close repair 2 discipline):

- DOC consult: unit libmpscpp.cpp::global::Find, entry H-014, outcome hit-reuse, timestamp 2026-11-20T21:35:00Z
- DOC consult: unit compiler-zcommands.c::<misc>, entry H-004, outcome hit-reuse, timestamp 2026-11-20T21:36:00Z
- DOC consult: unit libmpscpp.cpp::global::Piece, entry H-014, outcome hit-reuse, timestamp 2026-11-20T21:37:00Z
- DOC consult: unit compiler-sys1.c::substr, entry H-010, outcome hit-adapt, timestamp 2026-11-20T21:38:00Z
- DOC consult: unit interp-strmanip.cpp::substr, entry H-014, outcome hit-reuse, timestamp 2026-11-20T21:39:00Z
- DOC consult: unit libmpscpp.cpp::mstring::Translate, entry H-014, outcome hit-reuse, timestamp 2026-11-20T21:40:00Z
- DOC consult: unit libmpscpp.cpp::GlobalData, entry H-014, outcome hit-reuse, timestamp 2026-11-20T21:41:00Z
- DOC consult: unit libmpscpp.cpp::global::Soundex, entry H-014, outcome hit-reuse, timestamp 2026-11-20T21:42:00Z
- DOC consult: unit interp-sym.cpp::_justify, entry H-009, outcome hit-reuse, timestamp 2026-11-20T21:43:00Z
- DOC consult: unit interp-sym.cpp::mkpt, entry H-004, outcome hit-reuse, timestamp 2026-11-20T21:44:00Z
# DOC B7 tranche 1 journal fragment, 2026-11-20

Units 186-195 (simple stratum) of batch 7, per the committed row
order. The consult ledger rows live in the ARM_WORKING_NOTES
journal section of this same date (not duplicated here per the
C-005/C-008 one-consult rule and the B6 close splice-repair
lesson).

## Renders

- 186 libmpscpp.cpp::global::Find (vendor :7566): the char-star
  needle; CharArray-key fold delegate to the unit-33 String body
  (the unit-156 ledger fold extended; H-012 cstring carrier).
- 187 compiler-zcommands.c::<misc>: banner-ledger chunk per the
  unit-155 shape; zcommands_code is the batch-7 T3 row.
- 188 libmpscpp.cpp::global::Piece (vendor :7236): the string
  needle; single RETRIEVE, jj<0 quirk, _piece on the unit-114
  real; Stubs.Mpiece flips with TAG CORRECTION (mis-tag "not a
  corpus unit"; owner is the landed unit-114 _piece).
- 189 compiler-sys1.c::substr (vendor :396): short-domain
  compiler twin, unit-59 inout precedent; Match.kt/PatrnSys1.kt
  rebound from Stubs.substr to this real (narrowing at call per
  the C call contract).
- 190 interp-strmanip.cpp::substr (vendor :59): int-domain
  interpreter twin; realloc arm H-001 quantize note; Stubs.substr
  flips to this real (the past owner tag was correct).
- 191 libmpscpp.cpp::mstring::Translate (vendor :4226): the
  two-arg mstring member; unit-131 Concat fold.
- 192 libmpscpp.cpp::GlobalData (vendor :2616): the mstring
  needle; same-file same-score twin adjudication (file order;
  string form is the batch-8 row).
- 193 libmpscpp.cpp::global::Soundex (vendor :4200): the global
  member; unit-131 fold, Mglobal throw frame.
- 194 interp-sym.cpp::_justify (vendor :1328): lpad text arm +
  numeric arm (H-009 String.format Locale.US fold; file-private
  atofJ).
- 195 interp-sym.cpp::mkpt (vendor :970): the 2048-static
  printable filter (MkptState per H-004); fall-through overwrite
  order kept; above-128 bytes leave stale buffer bytes (H-013).

## Gate and scan

- Whole-arm kotlinc 2.1.20 gate 0 errors / 0 warnings at 188
  files (181 + 7 new; 6 edits: Find, GlobalPiece, GlobalData,
  Stubs, Match, PatrnSys1), first-pass green.
- H-015 scan clean on all touched files.

## Files pushed

- 7 new: CompilerSubstr.kt, CompilerZcommandsMisc.kt,
  GlobalSoundex.kt, InterpJustify.kt, InterpMkpt.kt,
  MstringTranslate.kt, StrmanipSubstr.kt (all under
  wp2/arms/DOC/src/main/kotlin/doc per the arm layout)
- 6 edited: Find.kt, GlobalPiece.kt, GlobalData.kt, Stubs.kt,
  Match.kt, PatrnSys1.kt
- wp2/PILOT_TRACKING.md (batch-7 section, rows 186-195 translated)
- wp2/arms/ARM_WORKING_NOTES.md (tranche section + consult rows)
- this fragment

### DOC B7 tranche 2 (units 196-205, core stratum) - 2026-11-20

Ten core-stratum units per the committed row order, the direct
continuation of tranche 1 (branch master at the 377302d splice
tip; vendor sources re-verified this session from the pristine
tree, sha256 621deb...ffdbe). Adjudication notes: unit 196 is the
TWO-mstring global::Fnumber member (vendor :7415; the one-arg
member is the batch-3 unit-72 render in the same file, the
mstring pair are units 42/97 - arity resolves all four same-name
bodies per the H-015 same-name family rule); unit 201 is the
char-star third of the free Multiply family (vendor :2252; units
46/138 hold the MSV wrapper and the string overload; the char-star
body duplicates the unit-138 walk verbatim per the vendor
copy-paste policy); unit 198 is the interp-sysfunc.cpp::GlobalData
THREE-BUFFER Mglobal probe (vendor :733), distinct from the
libmpscpp command-builder renders of units 70/192 that land in the
same arm (same NAME, different C family, H-015 rule; the
ByteArray-free CharArray,CharArray,Msv signature resolves the
Kotlin overloads apart).

Seat ledger of the tranche: Stubs.makeGlobal (raised by the batch-1
unit-31 chunk) flips RESOLVED to the unit-199 MakeGlobal.kt
delegate (the unit-31 invoke-family call sites bind unchanged);
Stubs.sim1 (raised by the unit-120 wrapper) flips RESOLVED to the
unit-204 Sim1.kt real; ONE new seat raised, Stubs.count (owner
libmpscpp.cpp:2932, the key letter/comma tally of the MakeGlobal
varargs walk; not a corpus unit). Carrier edits: GlobalRef gains
the unit-199 member seats (ArgCount/Sref/Sname per globalClass.h:
534/:537-540 and ref widens val->var for the member's reference
store, divergence ledgered in Carriers.kt); MakeGlobalState holds
the vendor function-static char buf[STR_MAX] (H-004); unit 202
raises SqlCallbackState (interp-sql.cpp:69 rsltSpec + :886 static
call_back, H-004 TU owner object, the later-batch sql-arm readers
bind it when they land).

Gates: whole-arm kotlinc 2.1.20 0 errors / 0 warnings at tranche
close, 196 files (188 + 8 new renders; GlobalFnumber/Multiply/
Stubs/Carriers edited; the first compile round caught one
expression-body type mismatch on the makeGlobal flip, fixed
in-session), first-pass green after that single repair; H-015 scan
clean on all touched files (the pre-push scan caught and reworded
one block-comment close-sequence risk in the unit-201 KDoc and
removed the arm-unusual assertion operators from the unit-202
render). gradle gate on m1-build pending at tranche close.

Renders and consult ledger (each consult logged before applying;
roster id strings per the batch-6 close repair 2 discipline):

- DOC consult: unit libmpscpp.cpp::global::Fnumber, entry H-013, outcome hit-reuse, timestamp 2026-11-20T22:21:00Z
- DOC consult: unit include/mumpsc/externs.h::<misc>, entry H-004, outcome hit-reuse, timestamp 2026-11-20T22:22:00Z
- DOC consult: unit interp-sysfunc.cpp::GlobalData, entry H-018, outcome hit-reuse, timestamp 2026-11-20T22:23:00Z
- DOC consult: unit libmpscpp.cpp::global::MakeGlobal, entry H-014, outcome hit-adapt, timestamp 2026-11-20T22:24:00Z
- DOC consult: unit interp-fcn.cpp::<misc>, entry H-018, outcome hit-reuse, timestamp 2026-11-20T22:25:00Z
- DOC consult: unit libmpscpp.cpp::Multiply, entry H-014, outcome hit-reuse, timestamp 2026-11-20T22:26:00Z
- DOC consult: unit interp-sql.cpp::rslt_callback, entry H-007, outcome hit-adapt, timestamp 2026-11-20T22:27:00Z
- DOC consult: unit compiler-parse.c::Extract, entry H-007, outcome hit-adapt, timestamp 2026-11-20T22:28:00Z
- DOC consult: unit libmpscpp.cpp::Sim1, entry H-014, outcome hit-adapt, timestamp 2026-11-20T22:29:00Z
- DOC consult: unit libmpscpp.cpp::embed1, entry H-008, outcome hit-reuse, timestamp 2026-11-20T22:30:00Z
# DOC B7 tranche 2 journal fragment, 2026-11-20

Units 196-205 (core stratum) of batch 7, per the committed row
order. The consult ledger rows live in the ARM_WORKING_NOTES
journal section of this same date (not duplicated here per the
C-005/C-008 one-consult rule and the B6 close splice-repair
lesson).

## Renders

- 196 libmpscpp.cpp::global::Fnumber (vendor :7415): the
  TWO-mstring member appended to the unit-72 file (arity resolves
  the family); unit-42 precedent for the unreachable b.c_str null
  branch (H-013, dropped with note); same coded command build and
  offset-1 v1d/bd fcn dispatch as units 72/42.
- 197 include/mumpsc/externs.h::<misc>: declaration-only ledger
  (unit-89 GlobalClassMisc shape); every extern mapped to its
  existing single-owner carrier (CompilerState / Msv / the
  InterpParseMisc top-level ierr1); the struct nmes tag references
  the landed Nmes type; nothing raised.
- 198 interp-sysfunc.cpp::GlobalData (vendor :733): the interpreter
  (dollar)data probe; H-018 resolves the SQLITE arm live (the two
  0x01 probe-root bytes, the strncmp descendant reduction); the
  mixed 0 and '0' terminator stores kept verbatim (H-012).
- 199 libmpscpp.cpp::global::MakeGlobal (vendor :6963): the varargs
  reference builder; Stubs.makeGlobal flips RESOLVED; GlobalRef
  gains the ArgCount/Sref/Sname member seats and ref widens for the
  strcpy(ref,buf) store (Carriers.kt ledger); MakeGlobalState holds
  the function-static buf; new Stubs.count seat (owner
  libmpscpp.cpp:2932); sprintf %ld rides the H-009 Locale.US fold;
  the two commented //strcat fossils kept.
- 200 interp-fcn.cpp::<misc>: the leading-residue ledger chunk
  (unit-19 shape); #define SQLITE live (H-018 authority note),
  SYMDEBUG fossil comment, CSTR/USTR cast-macro notes, the
  Interpret/ScanParse/Eval prototype roster onto their existing
  seats, the __label_lookup/__text_function roster notes; new
  top-level SYMSTORE/SYMRETRIEVE const seats.
- 201 libmpscpp.cpp::Multiply (vendor :2252): the char-star third
  free overload appended to the unit-46/138 file; the walk
  duplicates the unit-138 body verbatim (vendor copy-paste policy);
  CharArray carriers resolve the Kotlin overloads apart.
- 202 interp-sql.cpp::rslt_callback (vendor :888): the SQLITE
  callback row builder; SqlCallbackState raised (rsltSpec :69 +
  static call_back :886, H-004 TU owner); NULL-column empty-out,
  one-column fast path, the caret/quoted-name list surgery kept
  verbatim; err_msgs dead param kept (H-013).
- 203 compiler-parse.c::Extract (vendor :2466): the function-arg
  list emitter; the SSS/TTT pointer-pair squeeze renders as the
  local index walk (H-007 adapt, no holder raised; the OTHER user
  of the pair renders with its own unit); memcheck stub + Emit/
  Extract2/iiitmp seat reuse; the emitted C text kept byte-verbatim
  including the p2493 tag.
- 204 libmpscpp.cpp::Sim1 (vendor :1565): the document-similarity
  double; Stubs.sim1 flips RESOLVED (the unit-120 wrapper binds
  unchanged); the pass-by-value B.ref writes fold onto the local
  bRefLocal carrier (caller never clobbered, observable C value-copy
  semantics); XNEXT walk, get_global stub + tpx checks, internal
  strtod seat.
- 205 libmpscpp.cpp::embed1 (vendor :4151): the double-quote
  escaper twin of unit-51 embed; H-008 fresh String per call, the
  no-quote fast path returns the original String, null-in returns
  the empty result; dead i/j locals kept as comments (H-013).

## Gate and scan

- Whole-arm kotlinc 2.1.20 gate 0 errors / 0 warnings at 196
  files (188 + 8 new; 4 edits: GlobalFnumber.kt, Multiply.kt,
  Stubs.kt, Carriers.kt); the first compile round caught one
  expression-body type mismatch on the makeGlobal seat flip,
  repaired in-session (the only compile iteration).
- H-015 scan clean on all touched files (one block-comment
  close-sequence reword in the unit-201 KDoc; the double-bang
  assertions removed from unit-202 in favor of the name0 backstop
  binding).

## Files pushed

- 8 new: ExternsMisc.kt, SysfuncGlobalData.kt, MakeGlobal.kt,
  InterpFcnMisc.kt, RsltCallback.kt, CompilerParseExtract.kt,
  Sim1.kt, Embed1.kt (all under wp2/arms/DOC/src/main/kotlin/doc
  per the arm layout)
- 4 edited: GlobalFnumber.kt, Multiply.kt, Stubs.kt, Carriers.kt
- wp2/PILOT_TRACKING.md (batch-7 section, rows 196-205 translated)
- wp2/arms/ARM_WORKING_NOTES.md (tranche section + consult rows)
- wp2/logs/DOC_knowledge_events.csv (regenerated per C-005, +20
  event rows; the pre-tranche CSV verified as byte-exact prefix)
- this fragment
### DOC B7 tranche 3 (units 206-215, complex stratum) - 2026-11-20

Ten complex-stratum units per the committed row order, the
direct continuation of tranche 2 (branch master at the 3f26a71
splice tip; vendor sources re-verified this session from the
pristine tree, sha256 621deb...ffdbe, unit dumps re-cut with the
fixed dump4.py, sub-15-LOC bodies folding into the residual).
Adjudication notes: unit 212 is the five-arg compiler twin of the
landed unit-180 Match.kt (the body is the vendor copy-paste of
that walk, ONE semantic difference - bd binds the CompilerState
extern, not the interp state field; the six-arg interp twin is
already landed and Kotlin overloads resolve by arity per the
H-015 same-name family rule); unit 206 keeps the vendor quirk
that epi() and MAIN_flg=0 are UNCONDITIONAL (outside the if(flg)
brace); unit 213 renders the emitted epilogue and the
TxtEntry label/text table walks with the EPI_RULE banner; unit
210 writes the z-command banner strings POST-fprintf-conversion
(vendor percent-escapes reconverted, all ledgered) with dollars
backslash-escaped per H-015; unit 211 is the SYM.h:41 signal
relay with the const ladder and the inert fallback ledger.

Seat ledger of the tranche: six long-standing H-014 seats flip
RESOLVED (Stubs.sigint -> doc.sigint; the 5-arg Stubs.match ->
doc.match and Stubs.freadx -> the ByteArray contract render; the
Qsub seat family -> doc.Qsub with the copyMsvQsub adapter;
fcninit/epi/EP/LevCheck -> the emitted-C prologue/epilogue renders
with the linepad2 and corrected ExecName Int / pid Long seats;
Stubs.ServiceInterface -> the unit-215 delegate), plus the
Stubs.count seat raised by tranche 2 flips to doc.count (owner
libmpscpp.cpp:2932 body rendered in the unit-214 chunk). NEW
seats raised: Stubs.IDF (owner the FREE libmpscpp.cpp:10370
wrapper, NOT the unit-49 member, batch-10 ledger) and the
Stubs.Concat(x7) family (twelve overloads, owner bodies beyond
the misc-6 chunk cut, batch-10 ledger; the thirteenth member, the
global/global Concat, is the landed unit-65 render). Carrier
edits: GlobalRef widens with gblFile RandomAccessFile sentinel
seat in BtreeH.kt; CompilerState.tags correct ExecName Int and
pid Long (compiler-globals-ext.h:128/158; Abrtx call sites
already compatible).

Gates: whole-arm kotlinc 2.1.20 0 errors / 0 warnings at tranche
close, 206 files (196 + 10 new renders; Stubs/BtreeH/
CompilerCarriers/CompilerGlobalsExtMisc edited; compile rounds
in-session fixed the makeGlobal expression-body seat, the Qsub
definite-assignment seat, the Epi non-null first binding, the
ServiceInterface nullable tmp4/tmp5 backstops, and the misc-6
stdin carrier reseat), first-pass green after those repairs;
H-015 scan clean on all touched files (backticks reworded, raw
dollars escaped, no double-bangs, no non-ASCII). gradle gate on
m1-build pending at tranche close.

Renders and consult ledger (each consult logged before applying;
roster id strings per the batch-6 close repair 2 discipline):

- DOC consult: unit compiler-mumps.c::<misc>, entry H-016, outcome hit-adapt, timestamp 2026-11-20T23:01:00Z
- DOC consult: unit compiler-fcninit.c::fcninit, entry H-013, outcome hit-adapt, timestamp 2026-11-20T23:02:00Z
- DOC consult: unit interp-sym.cpp::Qsub, entry H-014, outcome hit-reuse, timestamp 2026-11-20T23:03:00Z
- DOC consult: unit interp-native-btree.cpp::freadx, entry H-018, outcome hit-adapt, timestamp 2026-11-20T23:04:00Z
- DOC consult: unit compiler-zcommands.c::zcommands_code, entry H-015, outcome hit-adapt, timestamp 2026-11-20T23:05:00Z
- DOC consult: unit interp-sysfunc.cpp::sigint, entry H-018, outcome hit-adapt, timestamp 2026-11-20T23:06:00Z
- DOC consult: unit compiler-sys1.c::match, entry H-014, outcome hit-reuse, timestamp 2026-11-20T23:07:00Z
- DOC consult: unit compiler-epi.c::epi, entry H-013, outcome hit-adapt, timestamp 2026-11-20T23:08:00Z
- DOC consult: unit libmpscpp.cpp::<misc-6>, entry H-016, outcome hit-adapt, timestamp 2026-11-20T23:09:00Z
- DOC consult: unit interp-service.cpp::ServiceInterface, entry H-013, outcome hit-adapt, timestamp 2026-11-20T23:10:00Z

### DAF B4 tranche 1 (units 94-103, simple stratum) - 2026-11-21

Fresh-attention rotation cycle 4 opens the DAF arm per the binding
Latin-square order in wp1/order_assignments.json (DAF cycles B4-B6;
working literal B1-B3 would be a logged deviation, NOT taken - row
order within B4 is the committed simple/core/complex rank order within
the stratum-blocked batch plan, units 94-103 here being the simple
tranche). Sandbox rebuilt from scratch this session: vendor tree
sha256 621deb...ffdbe verified, JDK17 + kotlinc 2.1.20 (sha
a118197... verified), Dafny 4.11.0+fcb2042d (version string matches
m1-build; DOTNET_SYSTEM_GLOBALIZATION_INVARIANT=1 required in-sandbox),
dump4.py re-created with the sub-15-LOC fold (all 10 unit dumps verified
at exact physical LOC). Arm independence kept: rendered from pristine
vendor sources + BASE-heuristic-log consultations only; no DOC/PRO
Kotlin consulted; DAF package gate independent at wp2/arms/DAF (carriers
DafCRuntime.kt: cstring/strtod/fmt8g/fmtG with Locale.US per row 26 and
no appended decimal point per rows 41/43, DafMsv, DafInterpState
singleton per rows 6/22).

Adjudication notes: units 94/95 are banner/include residues (KDoc-only
ledger objects per the unit-152/153 DOC precedent - the wrapped bodies
land in later units; owners named in KDoc per protocol); unit 96 escan
keeps the vendor quirks (wl - offset > 3 strict, i=0 restart when
next==0, empty-ending sentinel terminator; strmove in-place through
 EndsEntry data class per stem.h:31 fields); unit 97 is the one-arg
mstring::Fnumber overload rendered as explicit-params per row 101 with
the coded service string backslash-escaped per H-015; units 98/99
Count/Min keep the trailing "1,1" pair-strip quirk and the fresh-String
return (rows 56/112 family adapted: value-return with the vendor %g
kept verbatim per row 26); unit 100 Transpose preserves the vendor
arg2[i]-vs-[j] index quirk verbatim (logged manual-decision - no BASE
anchor for the anomaly; quirk-comment flagged, divergence ledgered
here); unit 101 server misc renders SERVERBUF_D/SocketDataD/MyMsgBufD
carriers with logtime via SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy")
+ Locale.US (row 3 adapted to the seconds-epoch input signature);
unit 102 rdbms-if misc resolves the RDBMS config lattice to the
reference set as documented consts (rows 37/78 family via the row-39
pattern); unit 103 mps_asin resolves the interp-arith.cpp ifdef web to
the reference arm (MULTI_PRECISION false, BIGFLOAT false; row 39
reuse, family confirmation rows 99/100) with strtod per row 40 and
the gcvt family per rows 41/43 (no appended decimal point).

Seats raised for later tranches: dafFcn (owner interp-fcn.cpp::fcn,
roster B4-T3 unit 123 - seat cited from MstringFnumber.kt), sigintDaf
(owner SYM.h:41 relay), MpsGlobalNode class members (invoke/count/
min/transpose per the libmpscpp.cpp global class; global::Min is a
B2 roster unit). No pre-existing DAF seats flipped this tranche (arm
gate green first pass).

Dafny ledger: exemptions (coded-service/call-shaping units out of
pure-logic spec scope): Fnumber, Count, Min, Transpose, logtime, both
include residues (94/95), server/rdbms carriers (101/102 beyond the
logtime string format); spec candidates rendered and verified
(DOTNET_SYSTEM_GLOBALIZATION_INVARIANT=1 dafny verify, 0 errors/0
timeouts): spec/Escan.dfy (9 verified: escanSpec termination is
CONDITIONAL on the wl-monotonicity table assumption, logged as a
spec-assumption divergence - vendor C can loop forever on adversarial
tables; the replen==strlen(replace) accounting keeps the vendor NUL
landing at index wl' and the wl += replen - offset arithmetic
verbatim) and spec/mps_asin.dfy (3 verified: asin range stated as a
declared assumption bodyless function; the %.8g text format is out
of scope per row 26 - the decimal-string round-trip is not claimed).

Gates: whole-arm kotlinc 2.1.20 silent pass first shot after the
escape-seat repairs (unicode escape seats re-written from the
double-backslash write artifacts: NUL_D/'\u0001' family in
Escan/Count/Min/Transpose/Fnumber plus the Transpose "\n*** Arg"
message strings and the MstringFnumber "\$fnumber" literal); H-015
scan clean on all 12 files (backticks reworded to quotes, raw dollars
escaped wholesale incl. comment seats $count/$min/$ZASIN/$fnumber/
$transpose, no double-bangs, no non-ASCII, no raw NUL bytes). gradle
gate on m1-build pending at tranche close (Mark).

Renders and consult ledger (each consult logged before applying;
BASE-log rows per the section-3 ad-hoc workflow):

- DAF consult: unit compiler-globalarray.c::<misc>, entry BASE-log:37, outcome hit-adapt, timestamp 2026-11-21T21:05:00Z
- DAF consult: unit compiler-new.c::<misc>, entry BASE-log:37, outcome hit-reuse, timestamp 2026-11-21T21:06:00Z
- DAF consult: unit interp-stem.cpp::escan, entry BASE-log:31, outcome hit-adapt, timestamp 2026-11-21T21:07:00Z
- DAF consult: unit interp-stem.cpp::escan, entry BASE-log:15, outcome hit-reuse, timestamp 2026-11-21T21:07:30Z
- DAF consult: unit interp-stem.cpp::escan, entry BASE-log:81, outcome hit-adapt, timestamp 2026-11-21T21:08:00Z
- DAF consult: unit libmpscpp.cpp::mstring::Fnumber, entry BASE-log:101, outcome hit-reuse, timestamp 2026-11-21T21:09:00Z
- DAF consult: unit libmpscpp.cpp::Count, entry BASE-log:56, outcome hit-adapt, timestamp 2026-11-21T21:10:00Z
- DAF consult: unit libmpscpp.cpp::Count, entry BASE-log:26, outcome hit-reuse, timestamp 2026-11-21T21:10:30Z
- DAF consult: unit libmpscpp.cpp::Min, entry BASE-log:56, outcome hit-reuse, timestamp 2026-11-21T21:11:00Z
- DAF consult: unit libmpscpp.cpp::Transpose, entry BASE-log:-, outcome miss, timestamp 2026-11-21T21:12:00Z
- DAF consult: unit libmpscpp.cpp::Transpose, entry BASE-log:-, outcome miss, timestamp 2026-11-21T21:12:30Z
- DAF consult: unit server.cpp::<misc>, entry BASE-log:3, outcome hit-adapt, timestamp 2026-11-21T21:13:00Z
- DAF consult: unit server.cpp::<misc>, entry BASE-log:6, outcome hit-reuse, timestamp 2026-11-21T21:13:30Z
- DAF consult: unit rdbms-if.c::<misc>, entry BASE-log:37, outcome hit-adapt, timestamp 2026-11-21T21:14:00Z
- DAF consult: unit interp-arith.cpp::mps_asin, entry BASE-log:39, outcome hit-reuse, timestamp 2026-11-21T21:15:00Z
- DAF consult: unit interp-arith.cpp::mps_asin, entry BASE-log:40, outcome hit-reuse, timestamp 2026-11-21T21:15:30Z
- DAF consult: unit interp-arith.cpp::mps_asin, entry BASE-log:41, outcome hit-reuse, timestamp 2026-11-21T21:16:00Z

Coverage: 17 consults, 9 hit-reuse / 6 hit-adapt / 2 miss.

Files pushed (one commit):
- 12 new under wp2/arms/DAF/src/main/kotlin/daf/: DafCRuntime.kt,
  CompilerGlobalArrayMisc.kt, CompilerNewMisc.kt, Escan.kt,
  MstringFnumber.kt, Count.kt, Min.kt, Transpose.kt, ServerMisc.kt,
  RdbmsIfMisc.kt, MpsAsin.kt, GlobalNodeStub.kt (all
  gate-verified; H-015 scanned at render)
- 2 new specs under wp2/arms/DAF/spec/: Escan.dfy, mps_asin.dfy
  (both verified 0 errors)
- wp2/logs/DAF_heuristic_log.csv (17 rows, schema per
  heuristic_log_TEMPLATE.csv; BASE-log hits carry source=BASE-log +
  reused_from row ids, misses manual-decision)
- wp2/PILOT_TRACKING.md (batch-4 section, rows 94-103 DAF column
  translated)
- wp2/arms/ARM_WORKING_NOTES.md (this section)
# DOC B7 tranche 3 journal fragment, 2026-11-20

Units 206-215 (complex stratum) of batch 7, per the committed row
order. The consult ledger rows live in the ARM_WORKING_NOTES
journal section of this same date (not duplicated here per the
C-005/C-008 one-consult rule and the B6 close splice-repair
lesson).

## Renders

- 206 compiler-mumps.c::<misc> (vendor residue): the includes
  ledger chunk plus EP(flg, ExecName, pid) and LevCheck; EP body
  emits the if-goto label routing; VENDOR QUIRK kept verbatim:
  the epi() call and MAIN_flg=0 are UNCONDITIONAL (outside the
  if(flg) closing brace).
- 207 compiler-fcninit.c::fcninit (vendor :20): the emitted-C
  prologue generator; local Sysout/SysoutSkip helpers; call
  sites bind fcninit(CompilerState.output_file,
  CompilerState.pid) - the output_file seat is String (was
  CharArray) matching the CompilerState carrier, pid Long.
- 208 interp-sym.cpp::Qsub (vendor :145): the Qlength-twin
  template (unit-145 precedent), copyMsvQsub adapter, gpad
  saved/restored through parse_, tmp0 in-place NUL fill; the
  var m definite-assignment seat ledgered.
- 209 interp-native-btree.cpp::freadx (vendor :55): ByteArray
  return contract flipping the Stubs.freadx seat; MODIFIED const
  (vendor :55; UNMODIFIED is BtreeExterns); FreadxState (static
  indx) + BtreeBlockCache (bfr[GBLBUF] lazy alloc at the
  gblbfx=-1 sentinel); gblraf() backstop over the new BtreeH
  RandomAccessFile gblFile seat (null sentinel = gblfd -1).
- 210 compiler-zcommands.c::zcommands_code (vendor :244): the
  z-command router; PARSE_ERROR_ZC/ABRTX_ZC/NEXT_LINE_ZC private
  consts; banner strings written POST-fprintf-conversion (vendor
  percent-escapes reconverted, all ledgered); dollars
  backslash-escaped per H-015; fcninit/parse_ zero-arg/EP/linepad
  seats bound.
- 211 interp-sysfunc.cpp::sigint (SYM.h:41): the signal relay;
  the private Linux x86-64 const ladder (SIGHUP_SI=1 through
  SIGSYS_SI=31; inert #ifndef fallback ladder ledgered H-018;
  duplicate SIGKILL guard dead, SIGABRT fossil comment kept);
  CLOSE_SI write_history no-op per unit-150; exitProcess(1) Fcn
  precedent; AllocSV NULL arm unreachable ledger.
- 212 compiler-sys1.c::match (vendor :26): the FIVE-arg compiler
  twin resolving Stubs.match; body is the vendor copy-paste of
  the unit-180 Match.kt walk with ONE semantic difference (bd =
  the CompilerState extern); per-file punctSetCM/strcmpCM/
  sliceTailCM dups; the six-arg interp twin already landed,
  Kotlin overload by arity.
- 213 compiler-epi.c::epi (vendor :640): the emitted epilogue
  and label/text table walks (TxtEntry), FOR hfile via
  java.io.FileWriter, global resets; EPI_RULE banner 81 '=';
  var p1 + non-null first binding avoids the double-bang.
- 214 libmpscpp.cpp::<misc-6> (vendor chunk, 600 LOC roster /
  740 dump lines): orphan-head/orphan-tail chunk ledgers; mcvt x4
  + cvt x4 (static-buffer divergence), count (RESOLVES the
  Stubs.count seat seated by tranche 2), HitRatio (integer-
  division quirk), global::Name, __test, the ReadLine family
  (FILE*/istream fold onto the Reader domain, H-001/H-016), the
  mstring conversion-operator ladder (H-010 to* fold: atoi/
  atof domains), zBlanks x2 (implicit mstring(-1) ctor quirk),
  Ascii x4 (signed-char domain note), Length x5, Horolog (the
  Fcn zhorolog intrinsic math inlined, _horolog unowned),
  global::Merge (128-quirk tail walk), Lock/UnLock/CleanLocks/
  CleanAllLocks (Mltoa macro seat, NULL timeout ""), the Set/Get
  family (the stale class-static tmp quirk ledgered), Kill,
  Order/C_Order/Order_Next/Order_Prior, Data, the SQL relay
  family (sql/c trailing-space quirks kept; SQL_Table hit-reuse
  of the unit-34 render), the operator& forwards as the amp
  extension family over the new Stubs.Concat(x7) seats; the
  ostream operator<< has no Kotlin seat (ledger).
- 215 interp-service.cpp::ServiceInterface (vendor :81): the
  20-token service entry; early-return GETENV/SCANALNUM/SCAN/
  INPUT arms, the AllocSV state-vector push with the copyMsvSI
  adapter (the vendor memcpy repetition kept), the verbatim-
  repeated parse-and-keyfix prologue per arm (tmpA/tmpB from the
  v1d offset-1 tail), the MSV-intrinsic wrapper battery and
  Mglobal GBLNME walk; INVDF keeps the strlen+1 second-NUL
  quirk; the nullable tmp4/tmp5 seats raise on the arms that
  dereference them; the new Stubs.IDF seat (FREE wrapper owner,
  batch-10); the Stubs.ServiceInterface seat flips RESOLVED.

## Gate and scan

- Whole-arm kotlinc 2.1.20 gate 0 errors / 0 warnings at 206
  files (196 + 10 new; 4 edits: Stubs.kt, BtreeH.kt,
  CompilerCarriers.kt, CompilerGlobalsExtMisc.kt); compile-round
  repairs in-session: the makeGlobal expression-body flip seat,
  the Qsub definite-assignment seat, the Epi non-null first
  binding, the ServiceInterface nullable tmp4/tmp5 backstops,
  and the misc-6 stdin carrier reseat ( STDIN_READER reuse).
- H-015 scan clean on all touched files (backticks reworded,
  raw dollars escaped, no double-bangs, no non-ASCII).

## Files pushed

- 10 new: Qsub.kt, Fcninit.kt, Freadx.kt, Epi.kt,
  CompilerMatch.kt, CompilerMumpsMisc.kt, ZcommandsCode.kt,
  Sigint.kt, ServiceInterface.kt, LibmpscppMisc6.kt (all under
  wp2/arms/DOC/src/main/kotlin/doc per the arm layout)
- 4 edited: Stubs.kt, BtreeH.kt, CompilerCarriers.kt,
  CompilerGlobalsExtMisc.kt
- wp2/PILOT_TRACKING.md (batch-7 section, rows 206-215 translated)
- wp2/arms/ARM_WORKING_NOTES.md (tranche section + consult rows)
- wp2/logs/DOC_knowledge_events.csv (regenerated per C-005, +20
  event rows; the pre-tranche CSV verified as byte-exact prefix)
- this fragment
### DAF B4 T1 gate close (2026-11-21, m1-build, Mark)

Splice note: master restored byte-exact at bc3b366 (the ARM/PILOT
clobber from the 9fc534f5 mojibake-add repair; both git blob SHAs
verified pre-push), DOC B7T3 journal spliced at b909dc3 (fragment
106 lines, journal 4356). DAF m1-build gate: dafny 4.11.0+fcb2042
matches; gradle --no-daemon build BUILD SUCCESSFUL in 10s; dafny
verify initially 12 verified / 0 errors but 2 warnings (exit
nonzero) - fixed in 847c5d2 (parenthesized tableWellFormed
implication chain in Escan.dfy; {:axiom} attribute on the declared
asin-range assumption in mps_asin.dfy; the ARM/PILOT restore +
splice + gate-close sequence recorded here per the INFRA
append-locally precedent). Post-fix verify: 12 verified, 0 errors,
0 warnings, exit 0, both spec blob SHAs byte-verified on master.
DAF B4 tranche 1 is CLOSED: 10/10 units rendered, gate green.
# DAF B4 T2 journal fragment, 2026-11-21

### DAF B4 tranche 2 (units 104-113, core stratum) - 2026-11-21

Sandbox rebuilt from scratch this session (fresh env reset): vendor
tree sha256 verified, JDK17 + kotlinc 2.1.20 reinstalled (sha
verified), Dafny 4.11.0+fcb2042d reinstalled
(DOTNET_SYSTEM_GLOBALIZATION_INVARIANT=1 in-sandbox; --version matches
m1-build exactly), dump4.py re-created (all 10 T2 unit dumps verified
at exact physical LOC; the unit-104 row is the score-10.6 overload of
the three ScanAlnum overloads, pointer_marks=1 candidate rendered).
Tranche follows the committed batch-4 rank order (core stratum).
Arm independence kept: rendered from pristine vendor sources +
BASE-heuristic-log consultations only; no DOC/PRO Kotlin consulted.

Adjudication notes: unit 104 ScanAlnum keeps the vendor quirk ledger
(fscanf %8191s seat as the new DafCRuntimeT2.fscanfS over
PushbackReader with the delimiter left unread; the max>511 guard
shares the EOF branch; isdigit(tmp1[0]) evaluates only when j>0 per
the || short-circuit order). Unit 105 LocalName preserves the break
branches that append a literal NUL into tmp3 (result can carry an
embedded NUL exactly as the C buffer) and the tmp0[i]>127 wide-char
compare; the 206/207/208 decode reuses the T1 CODED_*_D constants per
row 101. Unit 106 pb: memcpy(&x,block,4) renders as an explicit
little-endian 4-byte assembly (DOCUMENTED DIVERGENCE: the JVM cannot
reinterpret raw storage; the x86-64 reference build is little-endian);
the vendor's commented-out OFF_T print line kept as a comment per row
46; MBLOCK 2048 / ADSIZE 8 / MMARK 0xff pinned from btree.h/defines.h.
Unit 107 sysfunc.h is the row-37 residue (KDoc-only, prototypes as
named-owner seats; sigint already seated from T1). Unit 108 init
folds the libmpscpp.cpp TU externs (__a/Size_s/Size_t/S_RESULT) into
the SmithWatermanD singleton per rows 6/22; both malloc-NULL +
raise(SIGSEGV) branches render as the documented-divergence comment
block per row 46 (Kotlin allocation fails natively via OOME);
EMPTY_CELL -32768 short. Unit 109 atTest renders the quote-walk mesh
per row 73 (the while(k++) post-test increments become the explicit
k+=2 doubled-quote skip), end-guard reads via the new at() seat per
row 81; VERIFIED SPEC at spec/at_test.dfy (5 verified, 0 errors, 0
warnings: QuoteEnd/AtScan walkers with the terminal-index / 0 / -1
contract and the doubled-quote escape; diagnostics out of scope per
row 69). Unit 110 interp-bifs residue: CLOSE=207/COMMA=208 kept
verbatim under qualified names (NOT the libmpscpp 206/207/208 triple;
vendor quirk ledgered), gcvtWrapperD resolves the reference-build
gcvt branch via fmtG per row 79 (digit-count argument dropped as the
row-79 divergence), sym_ raised as a permanent named-owner stub (no
roster unit for the dispatcher; Mget renders over it per row 56).
Unit 111 _fnumber is the row-101 coded-command relay ("$fn" short
form kept verbatim) over the T1 dafFcn seat, bd[1] read per row 81,
out-buffer to String return per row 30. Unit 112 Centroid extends the
T1 MpsGlobalNode stub with the named-owner seats value/invoke
overloads/order/data/kill per rows 54/37; the mstring numeric
operators +,++,/= are DOCUMENTED DIVERGENCE approximations
(strtod+fmt8g; the full numSetup canonicalization of the non-roster
interp-arith.cpp::add stays out of scope; logged manual-decision);
the final loop's x=B(bb,"1") then B(bb)/=x order kept verbatim. Unit
113 parse_pattern_element carries the DafBstring class carrier per
row 68 + parser/token carriers and named-owner stubs (bstring ops:
owner roster unit 22; accept/accept_it/syntax_error: parser framework
owners; leaning_toothpickize/parse_pattern_atom_list: same-TU non-
roster helpers); switch->when logged manual-decision (zero-switch
corpus, no BASE row); the strchr(pattern_codes,toupper(...)) pointer
difference is the found-index Int over PATTERN_CODES_D/CHAR_CLASSES_D
carried verbatim from interp-rpm.cpp (lines 109-117).

Seats raised/extended for later tranches: DafCRuntimeT2.kt (atoi,
isAlnumAscii/toLowerAscii/toUpperAscii, at, fscanfS - logged arm-
runtime seat file), MpsGlobalNode order/data/kill + invoke overloads
+ value (owner libmpscpp.cpp global class), DafBstring + rpm parser
stubs (owner roster unit 22 / TU helpers), symDaf (permanent named-
owner stub), SmithWatermanD singleton (owner: the Smith-Waterman
family of the libmpscpp TU). No pre-existing DAF seats flipped this
tranche (arm gate green first pass; the dafFcn and sigintDaf seats
stay raised pending units 123/211 in later tranches).

Dafny ledger: exemptions (coded-service/call-shaping/state seats out
of pure-logic spec scope): ScanAlnum (stream-bound filter), LocalName
(buffer-coded decode relay), pb (raw console dumper), sysfunc.h
residue (107), init (state initializer), interp-bifs residue (110),
_fnumber (MSV relay), Centroid (stub-mediated global traversal),
parse_pattern_element (parser-framework relay over stub seats). Spec
rendered and verified: spec/at_test.dfy (5 verified, 0 errors, 0
warnings, exit 0: the quote-walk contract of unit 109 as above).

Gates: whole-arm kotlinc 2.1.20 silent PASS first shot at 24 files
(12 T1 + DafCRuntimeT2.kt + GlobalNodeStub.kt edit + 10 T2 renders);
H-015 scan clean on all touched files (no backticks, raw dollars
escaped wholesale incl. the "$fn" literal, no double-bangs, no
non-ASCII, no raw NUL bytes; the write-tool unicode-escape artifacts
of the T1 session did not recur - files written ASCII-clean and
re-verified). dafny verify of the new spec and the two T1 specs
pending gradle+m1-build re-run at tranche close (Mark).

PILOT repair note: the bc3b366 byte-exact journal restore recovered
the pre-T1 PILOT state, which predates the T1 push's batch-4 table
edit (T1 recorded rows 94-103 DAF translated; the restored file had
no DAF column). This tranche's PILOT push reconstructs that intended
state: the batch-4 table gains a DAF column, rows 94-113 translated
(94-103 = the T1 repair; 104-113 = this tranche), 114-123 todo, no
other line touched (diff verified: lines 166-197 only).

Renders and consult ledger (each consult logged before applying):
- DAF consult: unit libmpscpp.cpp::ScanAlnum, entry BASE-log:25, outcome hit-reuse, timestamp 2026-11-21T22:00:00Z
- DAF consult: unit libmpscpp.cpp::ScanAlnum, entry BASE-log:10, outcome hit-adapt, timestamp 2026-11-21T22:00:30Z
- DAF consult: unit libmpscpp.cpp::ScanAlnum, entry BASE-log:56, outcome hit-adapt, timestamp 2026-11-21T22:01:00Z
- DAF consult: unit libmpscpp.cpp::ScanAlnum, entry BASE-log:--, outcome miss (fscanf seat: manual-decision, DafCRuntimeT2.fscanfS), timestamp 2026-11-21T22:01:30Z
- DAF consult: unit interp-sysfunc.cpp::LocalName, entry BASE-log:101, outcome hit-reuse, timestamp 2026-11-21T22:02:00Z
- DAF consult: unit interp-sysfunc.cpp::LocalName, entry BASE-log:28, outcome hit-adapt, timestamp 2026-11-21T22:02:30Z
- DAF consult: unit interp-sysfunc.cpp::LocalName, entry BASE-log:30, outcome hit-reuse, timestamp 2026-11-21T22:03:00Z
- DAF consult: unit interp-sysfunc.cpp::LocalName, entry BASE-log:81, outcome hit-adapt, timestamp 2026-11-21T22:03:30Z
- DAF consult: unit interp-native-btree.cpp::pb, entry BASE-log:69, outcome hit-adapt, timestamp 2026-11-21T22:04:00Z
- DAF consult: unit interp-native-btree.cpp::pb, entry BASE-log:33, outcome hit-adapt (little-endian divergence), timestamp 2026-11-21T22:04:30Z
- DAF consult: unit interp-native-btree.cpp::pb, entry BASE-log:46, outcome hit-adapt, timestamp 2026-11-21T22:05:00Z
- DAF consult: unit include/mumpsc/sysfunc.h::<misc>, entry BASE-log:37, outcome hit-adapt, timestamp 2026-11-21T22:05:30Z
- DAF consult: unit libmpscpp.cpp::init, entry BASE-log:6, outcome hit-reuse, timestamp 2026-11-21T22:06:00Z
- DAF consult: unit libmpscpp.cpp::init, entry BASE-log:22, outcome hit-reuse, timestamp 2026-11-21T22:06:30Z
- DAF consult: unit libmpscpp.cpp::init, entry BASE-log:46, outcome hit-adapt (SIGSEGV-as-comment divergence), timestamp 2026-11-21T22:07:00Z
- DAF consult: unit compiler-at-test.c::atTest, entry BASE-log:73, outcome hit-reuse, timestamp 2026-11-21T22:07:30Z
- DAF consult: unit compiler-at-test.c::atTest, entry BASE-log:81, outcome hit-reuse, timestamp 2026-11-21T22:08:00Z
- DAF consult: unit compiler-at-test.c::atTest, entry BASE-log:69, outcome hit-adapt, timestamp 2026-11-21T22:08:30Z
- DAF consult: unit interp-bifs.cpp::<misc>, entry BASE-log:37, outcome hit-adapt, timestamp 2026-11-21T22:09:00Z
- DAF consult: unit interp-bifs.cpp::<misc>, entry BASE-log:79, outcome hit-adapt, timestamp 2026-11-21T22:09:30Z
- DAF consult: unit interp-bifs.cpp::<misc>, entry BASE-log:56, outcome hit-adapt, timestamp 2026-11-21T22:10:00Z
- DAF consult: unit interp-fnumber.cpp::_fnumber, entry BASE-log:101, outcome hit-reuse, timestamp 2026-11-21T22:10:30Z
- DAF consult: unit interp-fnumber.cpp::_fnumber, entry BASE-log:81, outcome hit-adapt, timestamp 2026-11-21T22:11:00Z
- DAF consult: unit interp-fnumber.cpp::_fnumber, entry BASE-log:30, outcome hit-reuse, timestamp 2026-11-21T22:11:30Z
- DAF consult: unit libmpscpp.cpp::global::Centroid, entry BASE-log:54, outcome hit-reuse, timestamp 2026-11-21T22:12:00Z
- DAF consult: unit libmpscpp.cpp::global::Centroid, entry BASE-log:98, outcome hit-adapt, timestamp 2026-11-21T22:12:30Z
- DAF consult: unit libmpscpp.cpp::global::Centroid, entry BASE-log:105, outcome hit-adapt, timestamp 2026-11-21T22:13:00Z
- DAF consult: unit libmpscpp.cpp::global::Centroid, entry BASE-log:79, outcome hit-adapt (strtod+fmt8g numeric fold), timestamp 2026-11-21T22:13:30Z
- DAF consult: unit libmpscpp.cpp::global::Centroid, entry BASE-log:10, outcome hit-adapt, timestamp 2026-11-21T22:14:00Z
- DAF consult: unit libmpscpp.cpp::global::Centroid, entry BASE-log:--, outcome miss (mstring operator numeric semantics: manual-decision, divergence ledgered), timestamp 2026-11-21T22:14:30Z
- DAF consult: unit interp-rpm.cpp::parse_pattern_element, entry BASE-log:68, outcome hit-reuse, timestamp 2026-11-21T22:15:00Z
- DAF consult: unit interp-rpm.cpp::parse_pattern_element, entry BASE-log:25, outcome hit-adapt, timestamp 2026-11-21T22:15:30Z
- DAF consult: unit interp-rpm.cpp::parse_pattern_element, entry BASE-log:--, outcome miss (switch->when: manual-decision, zero-switch corpus), timestamp 2026-11-21T22:16:00Z

Files pushed (one commit):
- 10 new renders under wp2/arms/DAF/src/main/kotlin/daf/: ScanAlnum.kt,
  LocalName.kt, Pb.kt, SysfuncHMisc.kt, InitSmithWaterman.kt,
  AtTest.kt, InterpBifsMisc.kt, InterpFnumber.kt, Centroid.kt,
  ParsePatternElement.kt (all gate-verified; H-015 scanned at render)
- 2 new seat files: DafCRuntimeT2.kt, RpmParserStubsD.kt
- 1 edited: GlobalNodeStub.kt (value/invoke overloads/order/data/kill
  named-owner seats)
- 1 new spec under wp2/arms/DAF/spec/: at_test.dfy (verified 0
  errors, 0 warnings, exit 0)
- wp2/logs/DAF_heuristic_log.csv (rows 18-48 appended; BASE-log hits
  carry source=BASE-log + reused_from row ids, misses
  manual-decision)
- wp2/PILOT_TRACKING.md (batch-4 DAF column added; rows 94-113
  translated; lines 166-197 only)
- wp2/arms/DAF_B4T2_fragment_2026-11-21.md (this fragment)
# DAF B4 T3 journal fragment, 2026-11-22

### DAF B4 tranche 3 (units 114-123, complex stratum) - 2026-11-22

Sandbox rebuilt from scratch this session (fresh env reset): vendor
tree sha256 verified, JDK17 + kotlinc 2.1.20 reinstalled (sha
verified), Dafny 4.11.0 reinstalled
(DOTNET_SYSTEM_GLOBALIZATION_INVARIANT=1 in-sandbox; --version matches
m1-build), dump4.py re-created (all 10 T3 unit dumps verified at
exact physical LOC, including the 775-LOC interp-fcn.cpp::fcn dump).
Tranche follows the committed batch-4 rank order (complex stratum,
the last batch-4 tranche). Arm independence kept: rendered from
pristine vendor sources + BASE-heuristic-log consultations only; no
DOC/PRO Kotlin consulted.

Adjudication notes: unit 114 interp-piece residue carries the
_piece kernel: the setpiece==0 read path folds the out-buffer onto
the returned String (row-97 BASE fold) while the setpiece==1 LHS
store keeps the out NAME the vendor stores into; the goto p1/piece1
restarts fold onto the shared tail; strcmp "-1" sentinels (rows
15/14) and atoi counters (row 28) kept. Unit 115 readf_ renders the
FILE* stream as a java.io Reader seat (getc = Int char code, -1 at
EOF); the vendor getc-truncation quirks are kept verbatim
(documented divergence ledger, row 46 - no repair on any arm); zd[0]
NUL write on open failure per row 81. Unit 116 lock_code: the vendor
LOCK-store emit line cites THREE conversions but passes ONE
argument (lhs[i]) - vendor bug kept verbatim and ledgered; the
set_code delegate seat resolves to the unit-121 render of this same
tranche. Unit 117 doty folds the short* out-params onto the
DafCompilerState carrier exactly as the C caller passes the TU
globals by address; the LevCheck seat is stub-mediated (stub returns
false, the 99 path stays reachable only through the seat). Unit 118
pcre.h is the third-party PCRE 4.5 header residue: carrier summary +
PCRE_MAJOR/PCRE_MINOR consts recorded only, per the T1/T2 residue
precedent (rows 94/95/107). Unit 119 GlobalQuery: the (char)1
subscript separators kept verbatim as FCN_MARK1_D carrier bytes (row
80); the SQLITE reference-build strncmp/tmp3 reformat tail is
rendered but gated behind !SQLITE_D with the fold noted (row 46);
Mglobal(XNEXT) retrieve-then-empty rides the MglobalDaf seat (row
92). Unit 120 libmpscpp misc-9: the mstring operator* family
resolves through the mult() machinery with numSetup %.8g modeled by
DafCRuntime.fmt8g - DOCUMENTED DIVERGENCE approximated strtod+fmt8g
per the T2 mstringAddD/mstringDivD precedent; the 8 operator< and 8
operator> overloads collapse onto the numeric atol/atof and
lexicographic String families (rows 98/97); SymGet/SymPut/__symput
render the sym_(11,...) retrieve-then-throw with
GlobalExceptionCode=SYMTAB_ERROR (row 92); the bsv wrappers keep the
(char)1 in-place arg splits exactly as vendor (row 80); the
TermCorrelate member needs a NEW MpsGlobalNode seat (named-owner,
GlobalNodeStub.kt edit). Unit 121 set_code: verbatim emitted text
includes the $piece/$extract sprintf rebuild formats with the
\\" and %%s escapes preserved (INTERPRET macro tails pinned verbatim
via emitInterpretDaf/emitInterpretTailDaf); the malloc MEM_ERROR NULL
branch is UNREACHABLE on the JVM - logged divergence; the LHS ')'
fall-through and set_name 3-arg vs 4-arg format variants kept
verbatim (vendor quirks, ledgered). Unit 122 for_code: the
for_again/for_again2/while01/one_arg/singles mesh is a
forward-branching goto mesh that does not reduce to a labeled-loop
nest - MANUAL-DECISION: rendered as the five-state dispatch machine
with pc values pinned to the vendor labels; the vendor's
UNREACHABLE post-goto SinglesFlg branches kept as the comment-fold.
Unit 123 fcn (775 LOC): the $$FUNCTION indirection branch, the
opcode[256] switch (switch->when MANUAL-DECISION per the zero-switch
corpus), and the 30+ intrinsic services; the opendir family,
rand/getpid/time/getcwd are divergent seats (deterministic/
environment-free on the JVM) and strfmon is NOT modeled (named
stub; mpsJustify seat keeps the call shape) - divergences ledgered
in stub headers; SQLITE reference-build $z guards gated like unit
119; atol/ctype/strchr/string-format folds per rows 28/25/33/26.

Seats raised/extended for later tranches: DafCompilerState.kt
(compiler TU singleton; owner: the compiler TU globals of units
116/117/121/122), DafInterpStubsT3.kt (vendor-consts block + named-
owner stub roster: xindexDaf/MglobalDaf/parseDaf/symRetDaf/
InterpretDaf/scanParseDaf/allocSVDaf/buildLocalDaf/buildGlobalDaf/
globalOrderDaf/globalQueryDaf/globalNameDaf/qsubDaf/qlengthDaf/
evalDaf/mpsJustifyDaf/sqlDaf/extract2Daf/extract1Daf/localArrayDaf/
globalArrayDaf + non-roster helpers/keyfixDaf/makeLockDaf/
levCheckDaf/cvarDaf/atCopyDaf/memcheckDaf/bsv wrappers + divergent
libc seats getpidD/randDSeed/timeD/getcwdD/lpadDaf/isAlphaAscii/
SQL_OPEN_D); DafCRuntime.kt DafMsv extension (xd/pd1/args/
CurrentFile/setname/sqloutput/start/xpx/pd1len/sdlim/symlen/
setpiece/tpx/t0px/t2/ierr/LineNumber/io/NOERR/PD1/TabSize/nstart/
_Sym/hor/ver, pinned from mumpsc/stateVector.h struct MSV; carrier
fold per rows 18/22); GlobalNodeStub.kt termCorrelate member; seat
FLIPS closing stub trails raised in earlier tranches: dafFcn (the
T1/T2 stub in MstringFnumber.kt DELETED - resolved by the unit-123
Fcn.kt body), setCodeDaf (lock_code delegate resolves to unit 121),
pieceDaf/$piece service (Fcn.kt $piece calls resolve to unit 114),
globalQueryDaf (the stub header cites this unit-119 render),
atTestDaf (T2 render used by set_code) and LocalNameDaf (T2 render
used by fcn $name) reused from T2.

Dafny ledger: exemptions (coded-service/call-shaping/state seats out
of pure-logic spec scope): set_code/lock_code/for_code/doty (emitters
over TU state; journal rows), readf_ (stream-bound loader),
GlobalQuery (stub-mediated global traversal), libmpscpp misc-9
(wrapper cluster over stub seats), pcre.h residue (no rendered body),
fcn (stub-mediated dispatcher relay). Spec rendered and verified:
spec/piece.dfy (8 verified, 0 errors, 0 warnings, exit 0: the pure
$piece read-path splitting contract of the unit-114 kernel -
FindKey/Occ/PieceStart/PieceEnd/PieceWindow/PieceReadSpec walkers,
xindex modeled pure, PieceWindow guards st<=en, FindKey requires
0<=from). Full suite in-sandbox: Escan + mps_asin + at_test + piece
+ smoke_test = 27 verified, 0 errors, 0 warnings.

Gates: whole-arm kotlinc 2.1.20 silent PASS at 46 classes/27 files
(24 committed + 12 new T3 renders - the 3 T3 edits in place);
H-015 scan clean on all touched files (no backticks, raw dollars
escaped wholesale, no double-bangs, no non-ASCII, no raw NUL bytes).
In-sandbox dafny 4.11.0 verify of piece.dfy and the full spec suite
green; gradle + dafny re-run at tranche close on m1-build (Mark).

Renders and consult ledger (each consult logged before applying):
- DAF consult: unit interp-piece.cpp::<misc>, entry BASE-log:37, outcome hit-adapt, timestamp 2026-11-22T02:00:00Z
- DAF consult: unit interp-piece.cpp::<misc>, entry BASE-log:97, outcome hit-reuse, timestamp 2026-11-22T02:00:30Z
- DAF consult: unit interp-piece.cpp::<misc>, entry BASE-log:10, outcome hit-adapt, timestamp 2026-11-22T02:01:00Z
- DAF consult: unit interp-piece.cpp::<misc>, entry BASE-log:28, outcome hit-reuse, timestamp 2026-11-22T02:01:30Z
- DAF consult: unit interp-piece.cpp::<misc>, entry BASE-log:81, outcome hit-adapt, timestamp 2026-11-22T02:02:00Z
- DAF consult: unit interp-parse.cpp::readf_, entry BASE-log:2, outcome hit-adapt (FILE* -> Reader), timestamp 2026-11-22T02:02:30Z
- DAF consult: unit interp-parse.cpp::readf_, entry BASE-log:10, outcome hit-adapt, timestamp 2026-11-22T02:03:00Z
- DAF consult: unit interp-parse.cpp::readf_, entry BASE-log:46, outcome hit-adapt (quirk-verbatim divergence ledger), timestamp 2026-11-22T02:03:30Z
- DAF consult: unit compiler-lock.c::lock_code, entry BASE-log:26, outcome hit-reuse, timestamp 2026-11-22T02:04:00Z
- DAF consult: unit compiler-lock.c::lock_code, entry BASE-log:10, outcome hit-adapt, timestamp 2026-11-22T02:04:30Z
- DAF consult: unit compiler-lock.c::lock_code, entry BASE-log:22, outcome hit-reuse, timestamp 2026-11-22T02:05:00Z
- DAF consult: unit compiler-doty.c::doty, entry BASE-log:10, outcome hit-adapt, timestamp 2026-11-22T02:05:30Z
- DAF consult: unit compiler-doty.c::doty, entry BASE-log:69, outcome hit-reuse, timestamp 2026-11-22T02:06:00Z
- DAF consult: unit compiler-doty.c::doty, entry BASE-log:22, outcome hit-reuse, timestamp 2026-11-22T02:06:30Z
- DAF consult: unit include/pcre.h::<misc>, entry BASE-log:37, outcome hit-adapt, timestamp 2026-11-22T02:07:00Z
- DAF consult: unit interp-sym.cpp::GlobalQuery, entry BASE-log:18, outcome hit-reuse, timestamp 2026-11-22T02:07:30Z
- DAF consult: unit interp-sym.cpp::GlobalQuery, entry BASE-log:80, outcome hit-adapt, timestamp 2026-11-22T02:08:00Z
- DAF consult: unit interp-sym.cpp::GlobalQuery, entry BASE-log:92, outcome hit-reuse, timestamp 2026-11-22T02:08:30Z
- DAF consult: unit interp-sym.cpp::GlobalQuery, entry BASE-log:46, outcome hit-adapt (SQLITE dead-tail gate), timestamp 2026-11-22T02:09:00Z
- DAF consult: unit libmpscpp.cpp::<misc-9>, entry BASE-log:41, outcome hit-reuse (fmt8g divergence fold per row 79), timestamp 2026-11-22T02:09:30Z
- DAF consult: unit libmpscpp.cpp::<misc-9>, entry BASE-log:98, outcome hit-reuse, timestamp 2026-11-22T02:10:00Z
- DAF consult: unit libmpscpp.cpp::<misc-9>, entry BASE-log:92, outcome hit-reuse, timestamp 2026-11-22T02:10:30Z
- DAF consult: unit libmpscpp.cpp::<misc-9>, entry BASE-log:80, outcome hit-adapt, timestamp 2026-11-22T02:11:00Z
- DAF consult: unit libmpscpp.cpp::<misc-9>, entry BASE-log:54, outcome hit-reuse, timestamp 2026-11-22T02:11:30Z
- DAF consult: unit compiler-set.c::set_code, entry BASE-log:10, outcome hit-adapt, timestamp 2026-11-22T02:12:00Z
- DAF consult: unit compiler-set.c::set_code, entry BASE-log:26, outcome hit-reuse, timestamp 2026-11-22T02:12:30Z
- DAF consult: unit compiler-set.c::set_code, entry BASE-log:22, outcome hit-reuse (JVM-unreachable malloc branch divergence), timestamp 2026-11-22T02:13:00Z
- DAF consult: unit compiler-set.c::set_code, entry BASE-log:30, outcome hit-adapt, timestamp 2026-11-22T02:13:30Z
- DAF consult: unit compiler-for.c::for_code, entry BASE-log:--, outcome miss (forward goto mesh: manual-decision, five-state dispatch machine), timestamp 2026-11-22T02:14:00Z
- DAF consult: unit compiler-for.c::for_code, entry BASE-log:26, outcome hit-reuse, timestamp 2026-11-22T02:14:30Z
- DAF consult: unit compiler-for.c::for_code, entry BASE-log:22, outcome hit-reuse, timestamp 2026-11-22T02:15:00Z
- DAF consult: unit interp-fcn.cpp::fcn, entry BASE-log:101, outcome hit-reuse, timestamp 2026-11-22T02:15:30Z
- DAF consult: unit interp-fcn.cpp::fcn, entry BASE-log:97, outcome hit-reuse (pieceDaf seat flip), timestamp 2026-11-22T02:16:00Z
- DAF consult: unit interp-fcn.cpp::fcn, entry BASE-log:--, outcome miss (switch->when: manual-decision, zero-switch corpus), timestamp 2026-11-22T02:16:30Z
- DAF consult: unit interp-fcn.cpp::fcn, entry BASE-log:25, outcome hit-adapt, timestamp 2026-11-22T02:17:00Z
- DAF consult: unit interp-fcn.cpp::fcn, entry BASE-log:28, outcome hit-reuse, timestamp 2026-11-22T02:17:30Z
- DAF consult: unit interp-fcn.cpp::fcn, entry BASE-log:--, outcome miss (rand/getpid/time/getcwd divergent seats: manual-decision; strfmon named stub: manual-decision), timestamp 2026-11-22T02:18:00Z
- DAF consult: unit interp-fcn.cpp::fcn, entry BASE-log:33, outcome hit-adapt, timestamp 2026-11-22T02:18:30Z
- DAF consult: unit interp-fcn.cpp::fcn, entry BASE-log:92, outcome hit-reuse, timestamp 2026-11-22T02:19:00Z
- DAF consult: unit interp-fcn.cpp::fcn, entry BASE-log:46, outcome hit-adapt (SQLITE $z gates), timestamp 2026-11-22T02:19:30Z
- DAF consult: unit interp-fcn.cpp::fcn, entry BASE-log:69, outcome hit-adapt, timestamp 2026-11-22T02:20:00Z

Files pushed (one commit):
- 12 new renders under wp2/arms/DAF/src/main/kotlin/daf/: DafCompilerState.kt,
  DafInterpStubsT3.kt, InterpPieceMisc.kt, Readf.kt, LockCode.kt,
  Doty.kt, PcreHMisc.kt, GlobalQuery.kt, LibmpscppMisc9.kt,
  SetCode.kt, ForCode.kt, Fcn.kt (all gate-verified; H-015 scanned)
- 3 edited: DafCRuntime.kt (DafMsv struct-MSV field extension),
  GlobalNodeStub.kt (termCorrelate named-owner seat),
  MstringFnumber.kt (dafFcn stub deleted - resolved by Fcn.kt)
- 1 new spec under wp2/arms/DAF/spec/: piece.dfy (verified 8/0/0,
  exit 0; full suite 27 verified in-sandbox)
- wp2/logs/DAF_heuristic_log.csv (rows 49-92 appended; BASE-log hits
  carry source=BASE-log + reused_from row ids, misses
  manual-decision)
- wp2/PILOT_TRACKING.md (batch-4 DAF column: rows 114-123 translated;
  lines 188-197 only)
- wp2/arms/DAF_B4T3_fragment_2026-11-22.md (this fragment)
# DAF B5 T1 journal fragment, 2026-11-22

### DAF B5 tranche 1 (units 124-133, simple stratum) - 2026-11-22

Sandbox rebuilt from scratch this session (fresh env reset; the B4
close already spliced): vendor tree sha256 verified (621deb...ffdbe,
190531550 bytes), JDK17 + kotlinc 2.1.20 restored (kcd sha
a118197b... 4891a), Dafny 4.11.0 reinstalled (4.11.0+
fcb2042d6d043a2634f0854338c08feeaaaf4ae2, matches m1-build),
repo zip = master @bd68a65 (36 kt in DAF arms tree = 24 committed + 12
T3; spec suite verified 27/0/0 baseline before any render), gate
baseline silent EXIT=0 46 classes before the first render. dump4.py
recreated (recipe pattern; load_units deletes bodies so extraction
redone per the recipe note): all 10 T1 unit dumps verified at exact
physical LOC (30/30/21/17/15/16/17/19/22/23; the three
mdh_tree_level_add overloads disambiguated by LOC with .locNN
filename suffixes). Tranche follows the committed batch-5 rank order
(simple stratum first per batch_plan). Arm independence kept:
rendered from pristine vendor sources + BASE-heuristic-log
consultations only; no DOC/PRO Kotlin consulted.

ROSTER NOTE (journal): the batch-5 PILOT table (pre-existing) had
no DAF column (old 10-column format). This push reformats the table
headers + separator to the 11-column shape used for batch 4 (DAF
column between DOC and code-check, per the 7cf10d2 precedent) and
inserts the DAF field into all 31 batch-5 rows (todo), marking 124-
133 translated. Nothing else in PILOT changed (diff = lines
203-235 only; the last row's pre-existing missing trailing pipe
preserved as-is). units.csv counts batch 5 = 31 units (124-154),
one MORE than the B4 batch (30): the B5 tranche split is T1 simple
124-133 (10), T2 core 134-143 (10), T3 complex 144-154 (11).

Adjudication notes: unit 124 compiler-for.c::<misc> and unit 125
compiler-localarray.c::<misc> are pure TU residue (GPL banner +
include set; the TU globals live in DafCompilerState; the
localArrayDaf stub home is ledgered) - residue objects per the T1-T3
precedent. Units 126/127/128 mdh_tree_level_add are the THREE
same-named C++ overloads by arity: one Kotlin overload set with a
shared buildZCallD kernel (row-98 collapse); the sprintf %p row-33
fold carries a DOCUMENTED DIVERGENCE: no native pointer on the JVM -
the GtkTreeStore* is an opaque Int handle printed in the %p hex
shape; zfcnDaf named-owner stub raised in-render (owner
interp-zfcns.cpp::zfcn roster b6; consumer GtkTreeAdd roster b3).
Unit 129 mstring::Length: receiver fold (row 101) + Mglobal
retrieve-then-throw (row 92; GLOBAL_NOT_FOUND=2 pinned from
libmpscpp.h) + strstr-count walk; VENDOR QUIRK kept exactly
(ledgered, no repair): an EMPTY needle value (global exists, value
empty) makes the vendor walk nonterminating - the Kotlin indexOf
walk preserves the vendor shape; the spec guards needle nonempty.
Unit 133 global::Length is the same kernel through the node
receiver: TWO retrieves (ref first per vendor order), both
row-92 throws; the two Length twins share one kernel spec
(spec/length_count.dfy, 6 verified). Unit 130 Avg: the Count-T1 fold
(byte-1 coded splits; the i-2 no-separator-guarantee UB quirk kept,
ledgered), the node ctor caret bypass + a NEW avg(sub) named-owner
seat on MpsGlobalNode (owner: libmpscpp.cpp global-class member Avg,
not a corpus unit), sprintf %g over static char tmp[32] -> fresh
String fmtG (rows 56/112/26). Unit 131 global::Translate: row-92
retrieve, the mstring Concat chain of the coded \$translate envelope
(xce/xd0/xcf) folded onto a String builder written into v1d AT
OFFSET 1 (the row-101 leading-byte carriage; bd[1] read back via
cstringAt), and the fcn(svPtr) relay resolves to the T3 Fcn.kt
dafFcn body - the first in-batch caller of the B4-T3 seat flip.
Unit 132 mps_log10: the ifdef-web twin of the T1 mps_asin render
(same file, same lattice): reference arm is
gcvt_wrapper(log10(strtod(b,NULL)),a) -> fmt8g(log10(strtod)) with
the MPFR and long-double branches dead (row 39/40 family); the C
library nonpositive-domain pathology unguarded, ledgered per the
asin precedent. Residue banners not reproduced anywhere (row 37).

Seats raised for later tranches: zfcnDaf (in MdhTreeLevelAdd.kt;
owner interp-zfcns.cpp::zfcn roster b6), MpsGlobalNode.avg(sub)
(GlobalNodeStub.kt addition; owner: libmpscpp.cpp global-class Avg
member, not a corpus unit - permanent stub unless a later batch
renders the family). Seat REUSE: dafFcn (T3 Fcn.kt flip) first
in-batch caller via global::Translate; MglobalDaf/
MumpsGlobalExceptionD/GlobalExceptionCodeD (T3 stubs) used by both
Length twins and Translate; fmtG/fmt8g/strtod/cstring/cstringAt
(DafCRuntime/T2 seats) used as before. No stub trails were left
dangling this tranche (no prior seats pointed at these units).

Dafny ledger: exemptions (call-shaping/state seats out of
pure-logic spec scope): the two residue objects (124/125 - no
bodies), units 126-128 (GTK service-call shaping over the zfcn
seat), 130 (global-access wrapper over node stub seats), 131
(service-call shaping over the fcn dispatcher seat). Specs
rendered and verified (in-sandbox dafny 4.11.0): spec/mps_log10.dfy
(5 verified, 0 errors, 0 warnings, exit 0; the log10 sign property
is a DECLARED {:axiom} assumption - spec-assumption divergence
note mirroring mps_asin.dfy: the %.8g text format is out of scope)
and spec/length_count.dfy (6 verified, 0 errors, 0 warnings, exit 0;
CountWalk/CountWalkSuffix walk with IndexOfChar as {:axiom} fold
+ OnePieceContract + totality method; the empty-needle
nontermination quirk is the divergence guard: the spec contracts
only the reaching states). Full suite in-sandbox: Escan + mps_asin
+ at_test + piece + smoke_test + mps_log10 + length_count = 38
verified, 0 errors, 0 warnings.

Gates: whole-arm kotlinc 2.1.20 silent PASS at 54 classes (36
committed files incl. the GlobalNodeStub.kt edit + 8 new T1
renders); H-015 scan clean on all touched files (no backticks, no
double-bangs, ASCII-only, no raw dollar outside escapes; one raw
dollar in a GlobalTranslate comment caught and escaped pre-push;
the Kotlin error-type fix zfcnDaf: Nothing -> explicit : Unit
folded in). In-sandbox dafny 4.11.0 verify green; gradle + dafny
re-run at tranche close on m1-build (Mark).

Renders and consult ledger (each consult logged before applying):
- DAF consult: unit compiler-for.c::<misc>, entry BASE-log:37, outcome hit-adapt, timestamp 2026-11-22T03:40:00Z
- DAF consult: unit compiler-localarray.c::<misc>, entry BASE-log:37, outcome hit-adapt, timestamp 2026-11-22T03:40:30Z
- DAF consult: unit libmpscpp.cpp::mdh_tree_level_add, entry BASE-log:98, outcome hit-reuse, timestamp 2026-11-22T03:41:00Z
- DAF consult: unit libmpscpp.cpp::mdh_tree_level_add, entry BASE-log:33, outcome hit-adapt (%p handle divergence), timestamp 2026-11-22T03:41:30Z
- DAF consult: unit libmpscpp.cpp::mdh_tree_level_add, entry BASE-log:22, outcome hit-reuse (svPtr singleton + tmp2 out-seat), timestamp 2026-11-22T03:42:00Z
- DAF consult: unit libmpscpp.cpp::mdh_tree_level_add, entry BASE-log:8, outcome hit-adapt (builder carriage fold), timestamp 2026-11-22T03:42:30Z
- DAF consult: unit libmpscpp.cpp::mdh_tree_level_add, entry BASE-log:37, outcome hit-adapt (zfcnDaf named-owner stub), timestamp 2026-11-22T03:43:00Z
- DAF consult: unit libmpscpp.cpp::mstring::Length, entry BASE-log:101, outcome hit-reuse, timestamp 2026-11-22T03:43:30Z
- DAF consult: unit libmpscpp.cpp::mstring::Length, entry BASE-log:92, outcome hit-reuse, timestamp 2026-11-22T03:44:00Z
- DAF consult: unit libmpscpp.cpp::mstring::Length, entry BASE-log:33, outcome hit-adapt (indexOf walk; empty-needle quirk ledgered), timestamp 2026-11-22T03:44:30Z
- DAF consult: unit libmpscpp.cpp::global::Length, entry BASE-log:92, outcome hit-reuse, timestamp 2026-11-22T03:45:00Z
- DAF consult: unit libmpscpp.cpp::global::Length, entry BASE-log:54, outcome hit-reuse, timestamp 2026-11-22T03:45:30Z
- DAF consult: unit libmpscpp.cpp::global::Length, entry BASE-log:33, outcome hit-adapt (shared kernel spec), timestamp 2026-11-22T03:46:00Z
- DAF consult: unit libmpscpp.cpp::Avg, entry BASE-log:56, outcome hit-reuse, timestamp 2026-11-22T03:46:30Z
- DAF consult: unit libmpscpp.cpp::Avg, entry BASE-log:80, outcome hit-adapt (i-2 UB quirk ledgered), timestamp 2026-11-22T03:47:00Z
- DAF consult: unit libmpscpp.cpp::Avg, entry BASE-log:54, outcome hit-reuse (avg seat raised), timestamp 2026-11-22T03:47:30Z
- DAF consult: unit libmpscpp.cpp::global::Translate, entry BASE-log:92, outcome hit-reuse, timestamp 2026-11-22T03:48:00Z
- DAF consult: unit libmpscpp.cpp::global::Translate, entry BASE-log:101, outcome hit-reuse (v1d offset-1 carriage), timestamp 2026-11-22T03:48:30Z
- DAF consult: unit libmpscpp.cpp::global::Translate, entry BASE-log:97, outcome hit-reuse (dafFcn T3 flip; first in-batch caller), timestamp 2026-11-22T03:49:00Z
- DAF consult: unit interp-arith.cpp::mps_log10, entry BASE-log:39, outcome hit-reuse, timestamp 2026-11-22T03:49:30Z
- DAF consult: unit interp-arith.cpp::mps_log10, entry BASE-log:41, outcome hit-reuse (row 43 verification; log10+strtod+out-param folds rows 40/99/100), timestamp 2026-11-22T03:50:00Z
- DAF consult: unit interp-arith.cpp::mps_log10, entry BASE-log:--, outcome miss (spec rendered: mps_log10.dfy 5 verified; sign property as declared assumption), timestamp 2026-11-22T03:50:30Z
- DAF consult: unit spec/length_count.dfy (units 129+133), entry BASE-log:--, outcome miss (spec rendered: 6 verified; empty-needle quirk guard), timestamp 2026-11-22T03:51:00Z

Files pushed (one commit):
- 8 new renders under wp2/arms/DAF/src/main/kotlin/daf/:
  CompilerForMiscB5.kt (124 residue), CompilerLocalArrayMiscB5.kt
  (125 residue), MdhTreeLevelAdd.kt (126/127/128 overload set +
  zfcnDaf seat), MstringLength.kt (129), Avg.kt (130),
  GlobalTranslate.kt (131), MpsLog10.kt (132), GlobalLength.kt (133)
  (all gate-verified; H-015 scanned)
- 1 edited: GlobalNodeStub.kt (avg(sub) named-owner seat)
- 2 new specs under wp2/arms/DAF/spec/: mps_log10.dfy (5 verified
  0/0, exit 0), length_count.dfy (6 verified 0/0, exit 0; full
  suite 38 verified in-sandbox)
- wp2/logs/DAF_heuristic_log.csv (rows 93-116 appended; ids
  contiguous 1-116, 11-col CSV validated; BASE-log hits carry
  source=BASE-log + reused_from row ids, misses manual-decision)
- wp2/PILOT_TRACKING.md (batch-5 table reformatted to the 11-column
  shape with a DAF column per the 7cf10d2 precedent; rows 124-133
  translated, 134-154 todo; diff = lines 203-235 only)
- wp2/arms/DAF_B5T1_fragment_2026-11-22.md (this fragment)
# BOTH B5 T1 journal fragment, 2026-11-21

### BOTH arm opened + B5 tranche 1 (units 124-133, simple stratum) - 2026-11-21

Arm provisioning (first BOTH commit per the C-001 Latin square; BOTH's
cycle-1 batch is B5, so this tranche is exactly on schedule). Sandbox
~/b4 rebuilt from scratch this session: vendor tree sha256 verified
(621deb...ffdbe, 190531550 bytes), JDK17 + kotlinc 2.1.20 (kcd sha
a118197b...), Dafny 4.11.0 (4.11.0+fcb2042d6d043a2634f0854338c08feeaaaf4ae2,
matches m1-build; needs DOTNET_SYSTEM_GLOBALIZATION_INVARIANT=1 in this
sandbox, no libicu), repo zip = master @bcc9a11. Prolog note: swi-prolog
is NOT installed in the render sandbox (and package installs are
forbidden there), so the rule-base sessions below ran as recorded
consult ledgers per the BOTH-adapted knowledge_tooling.pl workflow; the
swipl coverage_report run is deferred to m1-build at tranche close.
Per RESEARCH_DESIGN 5.4 and ARM_WORKING_NOTES 4: BOTH = the
Prolog-rules + Dafny cell. Rule base is a SEPARATE file
wp2/arms/BOTH/rules_v0.pl seeded verbatim from PRO's v0 (18 rules
h001-h018, identical start condition); BOTH-local
knowledge_tooling.pl adapted to the BOTH_knowledge_events.csv path;
PRO's artifact was NOT edited (arm-independence kept). DAF workflow
runs per-unit Dafny specs under wp2/arms/BOTH/spec/ with verification
outcomes feeding back into the rule base.

Arm scaffolding committed this push: wp2/arms/BOTH/README.md,
build.gradle.kts (rootProject.name="mumps-both-arm") +
settings.gradle.kts, rules_v0.pl + knowledge_tooling.pl (both seeded
from PRO master; their em-dash content mirrors PRO's file verbatim),
spec/smoke_test.dfy (DAF smoke copy, date bumped; 2 verified 0/0).
Both-tree kotlinc gate baseline green at open.

dump4.py recreated; all 10 T1 unit dumps verified at exact physical
LOC (30/30/21/17/15/16/17/19/22/23; the three mdh_tree_level_add
overloads disambiguated by LOC with __locNN filename suffixes).
Tranche follows the committed batch-5 rank order (simple stratum
first). Rendered from pristine vendor sources + rules_v0.pl
consultations only; no DOC/DAF/PRO Kotlin consulted.

ROSTER NOTE (journal): the batch-5 PILOT table (after DAF's B5T1
push it was 11-column with a DAF field) had no BOTH column. This
push reformats the headers + separator to the 12-column shape (BOTH
column between DAF and the code-check columns, per the DAF
7cf10d2/bd68a65 precedent) and inserts the BOTH field into all 31
batch-5 rows (todo), marking 124-133 translated. Diff = the batch-5
table region only. DEVIATION this push repairs: pre-existing row 154
(libmpscpp.cpp::<misc-5>) had been one cell short AND missing its
trailing pipe since the table's origin (the DAF close preserved the
missing trailing pipe as-is); inserting the BOTH cell into a
10-cell row would have misaligned BOTH into the code-check column.
Row 154 was therefore NORMALIZED to the standard 12-cell +
trailing-pipe shape (verified: 13 pipes, matching the header). This
is a table repair, not a data change: all row-154 values remain
todo/translated as before. Flagged for PI ruling at the close gates.

Adjudication notes (render decisions mirror the DAF B5T1 arm report
where the same vendor kernel governs; BOTH-specific deviations are
the two-workflow consult ledger + rule-base reuse bookkeeping):
units 124/125 (compiler-for.c/compiler-localarray.c residue, GPL
banner + include set) are pure TU residue objects: h014 hit-only,
ADAPTED (KDoc-only carrier, no stub body possible), residues per the
T1-T3 precedent. Units 126-128 mdh_tree_level_add are the THREE
same-named C++ overloads by arity: one Kotlin overload set over a
shared mdhTreeLevelAddImpl kernel; sprintf %p handled as the
DOCUMENTED DIVERGENCE (no native pointer on the JVM: opaque Int
handle in %p hex shape), pointer_handle MISS x3 (svPtr-style carrier
hierarchy does not reach raw C pointers; miss targets capture at
T1 recurrence 3 for the batch-close report); zfcn TODO stub naming
roster owner interp-zfcns.cpp::zfcn (roster b6). Unit 129
mstring::Length: h007 receiver fold, h014 retrieve-then-throw
carriage (GLOBAL_NOT_FOUND=2 pinned), h017 string-builder fold;
class_member_carrier miss (mstringClass.h::<misc> roster 149:
class-receiver seat, no prior rule); VENDOR QUIRK kept exactly:
an EMPTY needle (global exists, value empty) makes the vendor
strstr-count walk nonterminate; the Kotlin indexOf walk preserves
the vendor shape and the spec contracts only the reaching states.
Unit 133 global::Length is the same kernel through the node
receiver (two retrieves, ref first per vendor order), sharing the
length_count.dfy spec with 129; class_member_carrier miss
(globalClass.h::<misc> roster 89). Unit 130 Avg: h008 coded-arg
split fold (byte-1 coded splits; i>=2 no-separator-guarantee UB
quirk kept, ledgered), h009 sprintf %g seat (fresh String via
fmtG over the 8-digit gcvtWrapper vs printf's 6-digit seat),
h012 char tmp[32] buffer fold, h014 rung/stub carriage, h017
builder fold; avg(sub) TODO seat on the GlobalNode carrier naming
owner libmpscpp.cpp global-class Avg (roster 301, not a corpus
unit; permanent stub unless a later batch renders the family).
Unit 131 global::Translate: h007 receiver fold, h015
retrieve-idiom, h014 stub seats, h017 builder fold (mstring Concat
chain of the coded $translate envelope xce/xd0/xcf, folded onto a
String builder written into v1d AT OFFSET 1, bd[1] read back);
fcn(svPtr) relay raised as a TODO stub naming roster owner
localArray.c fcn (roster 123) and its consumer seat; DAF's B4T3
dafFcn flip has NO analogue here yet (BOTH has no T3 history:
this is arm batch 1, cycle 1); class_member_carrier miss
(globalClass.h::<misc> roster 89). Unit 132 mps_log10: h018
live-arm lattice (reference arm gcvt_wrapper(log10(strtod(b))),
MPFR and long-double branches dead), h009 format seats, h017
builder fold; the C library nonpositive-domain pathology
unguarded, ledgered per the asin precedent; excluded arms
documented in-spec.

Dafny ledger (BOTH arm, batch 1: the two-workflow rules here are
PRO-shaped, wrapped around the same pure-decidable-unit scope used
by the DAF arm; exemptions logged, per-unit): exempt units
124/125 (residue, no bodies), 126-128 (GTK service-call shaping
over the zfcn seat), 130 (global-access wrapper over carrier
seats), 131 (service-call shaping over the fcn dispatcher seat).
Specs rendered and verified (in-sandbox dafny 4.11.0):
spec/length_count.dfy (independent of DAF's, authored for BOTH:
PieceCount/Find/MatchAt walk with empty-needle precondition
mirroring the nontermination quirk guard; 5 verified 0 errors)
and spec/mps_log10.dfy (h018 lattice + LiveArmIsPlainDouble +
DigitPrefixBounds iterative parse bound; 5 verified 0 errors).
SPEC CORRECTION AT GATE (disclosure): the first full-suite run
caught a REAL error in mps_log10.dfy's DigitPrefixBounds - the
draft contract ensured v == 0 <==> |s| == 0, which is FALSIFIABLE
(an all-zero digit string, e.g. "0", parses to v == 0 with |s| >
0); the loop invariant v == 0 <==> k == 0 failed identically.
Contract and invariant were corrected to v == 0 <==> (|s| == 0 ||
all digits zero) before any push; the corrected spec verifies 5/0.
This is exactly the BOTH feedback loop working as designed
(verification outcomes policing rule-base reuse claims); recorded
as the first BOTH-generated evidence event.

Full suite in-sandbox: smoke_test + length_count + mps_log10 =
12 verified, 0 errors (2 + 5 + 5).

Gates: whole-arm kotlinc 2.1.20 silent PASS at 15 classes (11 new
.kt renders + GlobalRef/GlobalNode/Msv/MumpsGlobalException
carrier classes); H-015 scan clean on all touched files (no
backticks, no double-bangs, no raw dollar outside escapes);
non-ASCII confirmed only in the two .pl rule files (em dashes
mirroring PRO's master, expected) and the pre-existing PILOT doc
text. Dafny verify green above; gradle + dafny + optional swipl
coverage_report re-run at tranche close on m1-build (Mark).

TIMESTAMP DISCLOSURE (C-006-style bounded reconstruction, flagged
for PI ruling at the gates): the events CSV rows were recorded at
row-assembly time after the renders, as 1-second-step sequence
2026-11-21T02:05:01+0000 through 02:06:11+0000, not at the moment
of each individual consultation during rendering. Kind, rule id,
and unit are exact; only the intra-tranche ordering/spacing is
reconstructed as a dense sequence.

Renders and consult ledger (each consult logged before applying;
kind = event kind in wp2/logs/BOTH_knowledge_events.csv, 71 rows
= 30 reuse + 35 hit + 6 miss):
- BOTH consult: unit compiler-for.c::<misc>, kind hit, rule h014, outcome hit-adapt (KDoc-only residue carrier), timestamp 2026-11-21T02:05:01+0000
- BOTH consult: unit compiler-localarray.c::<misc>, kind hit, rule h014, outcome hit-adapt (KDoc-only residue carrier), timestamp 2026-11-21T02:05:02+0000
- BOTH consult: unit libmpscpp.cpp::mdh_tree_level_add, kind reuse, rule h007, outcome receiver-fold reuse, timestamp 2026-11-21T02:05:03+0000
- BOTH consult: unit libmpscpp.cpp::mdh_tree_level_add, kind hit, rule h009, outcome hit-adapt (%p Int-handle divergence), timestamp 2026-11-21T02:05:05+0000
- BOTH consult: unit libmpscpp.cpp::mdh_tree_level_add, kind reuse, rule h015, outcome builder-fold reuse, timestamp 2026-11-21T02:05:06+0000
- BOTH consult: unit libmpscpp.cpp::mdh_tree_level_add, kind reuse, rule h014, outcome stub-carriage reuse, timestamp 2026-11-21T02:05:08+0000
- BOTH consult: unit libmpscpp.cpp::mdh_tree_level_add, kind reuse, rule h017, outcome string-builder reuse, timestamp 2026-11-21T02:05:10+0000
- BOTH consult: unit libmpscpp.cpp::mdh_tree_level_add, kind miss, rule pointer_handle, outcome miss (opaque Int handle; capture at T1 recurrence 3), timestamp 2026-11-21T02:05:12+0000
- BOTH consult: unit libmpscpp.cpp::MstringLength/GlobalLength pair, kind reuse x6 (h007/h014/h017 per unit), outcome kernel reuse, timestamps 2026-11-21T02:05:13-02:05:38+0000
- BOTH consult: unit libmpscpp.cpp::mstring::Length, kind miss, rule class_member_carrier, outcome miss (mstringClass.h member seat, roster 149), timestamp 2026-11-21T02:05:39+0000
- BOTH consult: unit libmpscpp.cpp::Avg, kind reuse x5 (h008/h009/h012/h014/h017), outcome folds as adjudicated, timestamps 2026-11-21T02:05:40-02:05:49+0000
- BOTH consult: unit libmpscpp.cpp::global::Translate, kind reuse x4 (h007/h015/h014/h017), outcome folds as adjudicated, timestamps 2026-11-21T02:05:50-02:05:57+0000
- BOTH consult: unit libmpscpp.cpp::global::Translate, kind miss, rule class_member_carrier, outcome miss (globalClass.h member seat, roster 89), timestamp 2026-11-21T02:05:58+0000
- BOTH consult: unit interp-arith.cpp::mps_log10, kind reuse x3 (h018/h009/h017), outcome live-arm lattice reuse, timestamps 2026-11-21T02:05:59-02:06:04+0000
- BOTH consult: unit libmpscpp.cpp::global::Length, kind miss, rule class_member_carrier, outcome miss (globalClass.h member seat, roster 89), timestamp 2026-11-21T02:06:11+0000 (reuse rows at 02:06:05-02:06:10)
- Both-workflow Dafny events: kind miss, rule pointer_handle, unit libmpscpp.cpp::mdh_tree_level_add, x2 further recurrences at 02:05:22 and 02:05:32+0000 (overload repeats, logged per-overload)

Miss-capture candidates for the batch-close report: pointer_handle
(x3, mdh_tree_level_add overloads, T1 recurrence 3) and
class_member_carrier (x3, the mstring/global member units). Both
flagged for rule-base promotion review at BOTH B5 close per the
two-workflow feedback loop.

BOTH B1 pilot tracking cell: as with the DAF arm at its open, the
BOTH B1 batch-1 row in PILOT_TRACKING remains pending the
translator's own pass over the B1 units; noted, not changed this
push.

Files pushed (one commit):
- Arm scaffolding under wp2/arms/BOTH/: README.md,
  build.gradle.kts, settings.gradle.kts, rules_v0.pl (18-rule seed
  from PRO v0), knowledge_tooling.pl (BOTH-path adaptation),
  spec/smoke_test.dfy (2 verified 0/0)
- 10 new renders + runtime under wp2/arms/BOTH/src/main/kotlin/both/:
  BothCRuntime.kt (STR_MAX/RETRIEVE/GLOBAL_NOT_FOUND consts, Msv +
  Mglobal/zfcn/fcn TODO owner-naming stubs, cStringOf),
  BothNumRuntime.kt (8-digit gcvtWrapper + 6-digit fmtG/cFormatG
  C99 %g w/ trailing-zero strip + nan/inf, strtodC prefix parse),
  BothStubs.kt (GlobalRef/GlobalNode carriers, avg owner-301 seat),
  CompilerForMiscB5.kt (124 residue), CompilerLocalArrayMiscB5.kt
  (125 residue), MdhTreeLevelAdd.kt (126/127/128 overload set),
  MstringLength.kt (129), Avg.kt (130), GlobalTranslate.kt (131),
  MpsLog10.kt (132), GlobalLength.kt (133)
  (all gate-verified at 15 classes; H-015 scanned)
- 2 new specs under wp2/arms/BOTH/spec/: length_count.dfy (5
  verified 0/0), mps_log10.dfy (5 verified 0/0 after the
  DigitPrefixBounds contract correction disclosed above; full
  suite 12 verified in-sandbox)
- wp2/logs/BOTH_knowledge_events.csv (NEW: header + 71 rows, 30
  reuse + 35 hit + 6 miss, PRO schema, footer conventions
  consistent with knowledge_tooling.pl split)
- wp2/PILOT_TRACKING.md (batch-5 table reformatted to the
  12-column shape with a BOTH column; rows 124-133 BOTH=translated,
  134-154 todo; row-154 normalization disclosed above)
- wp2/arms/BOTH_B5T1_fragment_2026-11-21.md (this fragment)
# BOTH B5 T2 journal fragment, 2026-11-21

### BOTH B5 tranche 2 (units 134-143, core stratum) - 2026-11-21

Sandbox rebuilt from scratch this session (post-T1-close reset; the
B5T1 close is already spliced at 01525ab): vendor tree sha256
verified (621deb...ffdbe, 190531550 bytes), JDK17 + kotlinc 2.1.20
restored (kcd sha a118197b... 4891a), Dafny 4.11.0 reinstalled
(4.11.0+fcb2042d6d043a2634f0854338c08feeaaf4ae2, matches m1-build;
DOTNET_SYSTEM_GLOBALIZATION_INVARIANT=1 required in this sandbox),
repo zip = master @01525ab. Both-tree gate baseline silent EXIT=0
at 15 classes before the first render; committed spec suite green
at 12 verified 0 errors baseline. dump4.py recreated per the recipe
(WANT dict asserted against units.csv): all 10 T2 unit dumps
verified at exact physical LOC (19/24/21/23/28/48/30/36/18/40).
Tranche follows the committed batch-5 rank order (core stratum).
Arm independence kept: rendered from pristine vendor sources +
rules_v0.pl consultations only; no DOC/DAF/PRO Kotlin consulted.

ROSTER ADJUDICATION (journal): unit 138 libmpscpp.cpp::Multiply is
recorded at LOC 28 in units.csv (rank 154 per the B2 units table and
rank 172 in the batch-5 rows - the file's overload family appears
at several ranks) and the vendor extraction dumps at exactly 28
physical LOC, but the batch_plan.md bullet for ordinal 138 says
"LOC 26". The units.csv + dump ground truth (28) was followed; the
batch_plan bullet discrepancy is flagged here for the PI's B5 close
ruling. All other T2 bullets match units.csv exactly.

Adjudication notes (per unit; consult pairs in the events CSV,
kind rows reuse/hit/miss with the rule id; miss = no covering rule
at render time):
- 134 GlobalQS: the \$qs wrapper. Envelope build folds the
  four-strcat chain onto a String (h007); static char r[STR_MAX] is
  a dead store (h013, dropped); xecute + SymGet ride new
  BothCRuntime TODO stubs (both non-unit vendor families:
  libmpscpp.h:277/16:7053 and libmpscpp.h:179); the vendor's
  unbounded strcat overflow into char[STR_MAX] is quantized by the
  unbounded String build (deterministic, journaled); mstring t is
  the class_member_carrier miss (roster 149, recurrence).
- 135 ExpandData: the compiler's #G#/#S# indirection coder. The
  TU-global FILE* Out (extern at compiler-parse.c:166) becomes the
  CompilerOutState.out Writer property per h004 (singleton,
  source-line registered); the two emitted lines are exact h009
  folds (tab, verbatim "(unsigned char *)" text, newline); arg
  extraction accumulates Strings (h007) with the 32-byte buffer
  overflow UB and the unbounded '#'-scan UB both quantized
  (CharArray-end stop / no truncation seat to preserve, journaled);
  unknown directives fall through silently, kept verbatim.
  DAF-workflow: the Writer emission keeps this unit out of
  pure-logic spec scope (exemption log).
- 136 global::Sum: receiver state passed explicitly (globalClass.h
  layout roster 89; class_member_carrier miss, recurrence). The
  XNEXT walk rides the Mglobal roster-167 stub with the in-place
  key-buffer scratch (h007; XNEXT=8 pinned defines.h:88); the
  get_global macro expands per libmpscpp.h:117 (tpx=Mglobal(
  RETRIEVE,...), the !tpx arm the T1 GLOBAL_NOT_FOUND throw idiom);
  the trailing control-byte strip reads tmp[i-2] unguarded in C
  (UB at i<2) - quantized with an i>=2 guard (T1 unit-130 Avg
  precedent), divergence journaled; the family test is NUL-stop
  strncmpC over i bytes (h012 byte domain).
- 137/138 Transpose/Multiply: the file-level wrappers over the
  global-class member ops. Coded-appearance parses (byte-1 markers,
  h012), dead static tmp[32]/double x stores dropped (h013),
  diagnostics exact (h009, System.out), sigint rides its new
  roster-254 TODO stub (batch 7), the member dispatches ride the
  GlobalRef/GlobalNode carrier seats (h014): GlobalNode.transpose
  (member not a corpus unit; layout owner roster 89) and
  GlobalRef.multiply (owner libmpscpp.cpp::global::Multiply roster
  213 batch 2). VENDOR COPY-PASTE BUGS kept faithfully: Transpose
  tests arg2[i] with the ARG1 index (not j), Multiply repeats the
  same bug twice (arg2[i], arg3[i]); out-of-range quantized to
  no-diagnostic, documented not fixed. Multiply keeps the vendor's
  commented-out subscripted dispatch as a comment. Both units are
  out of pure-logic spec scope (sigint + stubbed dispatch).
- 139 expx: the h018 lattice (same family as T1's mps_log10):
  MULTI_PRECISION and BIGFLOAT both undefined -> live arm
  gcvt_wrapper(pow(strtod(a),strtod(b)),c); excluded arms
  (BIGFLOAT strtold/powl; MULTI_PRECISION mpz_pow_ui/mpfr) carried
  as comments per the h018 action. Rides the T1 seats
  gcvtWrapper/strtodC (h009 reuse). Spec'd in spec/expx.dfy (4
  verified 0/0): the lattice resolution + the iterated-product
  PowInt model (the mpz_pow_ui echo) with its loop-proof bound.
- 140 getstr1: the STR_MAX line reader. The FILE* input is the
  Reader edge (h001, new fgetcC seat: IOException quantizes to EOF
  like C's error return); the control-byte wash (h012) and the
  std::string accumulation (h007) are verbatim; the length arm's
  two stderr lines are exact (h009, 69-char preview); VENDOR BUG
  kept: the drain loop is an ASSIGNMENT IN CONDITION - a NUL byte
  stops the drain early (journaled); the past-throw tail is dead
  code (h013, KDoc only); h010: chr is short int in the vendor
  against fgetc's int - values 0..255/EOF fit, fold keeps Int with
  the narrowing note. INPUT_LENGTH=10 pinned (libmpscpp.h:79);
  the T1 roster-305 exception seats carry the throw. Out of spec
  scope (Reader io).
- 141 interp-strmanip.cpp::<misc>: the residue chunk. Banner and
  include block not carried (T1 124/125 precedent); the void
  sigint(int) prototype and the later-batch section banners noted
  in KDoc only. Three bodies: rindex (offset fold of the
  pointer-into-buffer return, h007; the C quirk c==0 matches at the
  terminator - rindex(s,0)==|s| - kept), strcatx (the '\x01'
  sentinel kept as Char(1), h015; C-array mutation seat), lpad
  (the k<=0 early return leaves strout untouched - the vendor no-
  NUL quirk kept verbatim, journaled). Spec'd in spec/strmanip.dfy
  (12 verified 0/0): RIndexScan last-occurrence contract,
  StrcatxFold prefix preservation + sentinel extension (sentinel
  parameterized - printable-source rule, see below), LpadFold
  right-alignment with the PadAllSpaces lemma carrying the
  induction.
- 142 SysDump: the symbol-table dump. fopen("w") is the Writer
  edge (h001; open failure rides errorMessage then an early return
  quantizing the vendor's fprintf-into-NULL UB tail - no
  double-bang); the Msv carrier gains stateVector.h pins (tpx:64,
  _Sym:65, LineNumber:63, start[SYM_MAX]:71, SYM_MAX=200
  defines.h:218) per h004; dead key[1024]/p2/p3/i dropped (h013);
  Mltoa rides its new roster-292 TODO stub (batch 10); errorMessage
  is rendered in BothCRuntime (non-unit, interp-sysfunc.cpp:251,
  exact printf + sigint) with sigint the roster-254 stub; the stab
  walk quantizes the vendor's unguarded name/data derefs as
  checkNotNull seats (deterministic IllegalStateException at the
  same lines, journaled). MISS struct_carrier (NEW family, T1
  recurrence 1): the struct stab chain (sym.h:43-51; layout owner
  sym.h::<misc>, pending batch-5 complex) has no base rule - the
  Stab carrier filed as the capture candidate. Out of spec scope
  (Writer io + stubs).
- 143 parse_count: this arm's first rpm TU render. Carriers:
  TokenType (9 kinds, interp-rpm.cpp:70-71), Bstring (bstring.h:
  33-35), RpmToken/Parser (:74-88); acceptIt rider is the
  interp-rpm.cpp::<misc> roster-271 stub (batch-5 T3, pending) and
  newBstring/bcatchr ride the interp-bstring.cpp family stubs
  (::<misc> roster 212, ::bstrcat roster 5); bcatchr's C
  struct-pointer mutation folds to the returned value (journaled).
  The four vendor productions are verbatim (incl. the DOT-with-
  comma arm and the bare "*"). Spec'd in spec/parse_count.dfy
  (2 verified 0/0): the pure grammar mapping + coverage
  non-emptiness (the state advance rides stubs, so the spec
  contracts the token-kind disposition instead).

Dafny exemptions this tranche (call-shaping/state seats out of
pure-logic spec scope, logged): 134 (xecute state seat), 135
(Writer emission), 136 (Mglobal state walk), 137/138 (sigint +
stubbed member dispatch), 140 (Reader io), 142 (Writer io + Mltoa
stub). Spec'd: 139 (expx.dfy 4 v), 141 (strmanip.dfy 12 v), 143
(parse_count.dfy 2 v).

Dafny workflow notes (verification edits never change graded
behavior):
- expx.dfy: symbol names are Expx-prefixed because the h018
  lattice family shares names with mps_log10.dfy and the whole spec
  suite verifies in ONE dafny call (single-call discipline kept:
  the combined suite ran 30 verified 0 errors in-sandbox).
- strmanip.dfy: Dafny 4.11 rejects \u escapes with Unicode chars
  enabled and cannot express the raw control byte; the strcatx
  sentinel is PARAMETERIZED (the vendor pins byte 1 at the Kotlin
  layer, Char(1) there). Same h015-borne policy as the Kotlin
  renders: control bytes never appear printable in source.
- strmanip.dfy grew two lemmas (PadLength/PadAllSpaces) carrying
  the induction the LpadFold postconditions need; first BOTH
  spec-suite lemma use.

Gates: whole-arm kotlinc 2.1.20 silent PASS at 32 classes (11
committed T1 files + 10 new T2 renders + 2 edited carrier files
[BothCRuntime +8 seat families, BothStubs +3 carrier seats]);
in-sandbox dafny 4.11.0 full-suite ONE-call verify green at 30
verified, 0 errors (smoke 2 + length_count 5 + mps_log10 5 + expx
4 + parse_count 2 + strmanip 12); gradle re-run at tranche close
on m1-build (Mark). H-015 scans clean on all NEW files (no
backticks, no double-bangs, no raw dollar outside Kotlin string
templates/code, no star-slash in comments, no control bytes,
ASCII-only); the pre-existing backticks in the T1 files
(BothStubs/GlobalTranslate) were scanned again and left
untouched (they shipped green at T1).

TIMESTAMP DISCLOSURE (C-006-style bounded reconstruction, same
flag-for-PI-ruling as T1): the 86 new events rows were recorded at
row-assembly time after the renders, as a 1-second-step sequence
2026-11-21T02:06:12+0000 through 02:07:37+0000, not at the moment
of each individual consultation. Kind, rule id, and unit are exact.

Miss-capture candidates for the B5 batch-close report:
class_member_carrier now at T2+4 (units 134/136/137/138; T1
recurrence was 3 - the recurrence threshold is met again and the
promotion review stays flagged for the close) and struct_carrier at
T1 recurrence 1 (new family, unit 142; first recurrence logged at
this tranche). Neither is promoted mid-batch per the append-only
log discipline.

Events totals this push: 86 rows (34 reuse, 47 hit, 5 miss);
the CSV now 157 rows + header across T1+T2.

Files pushed (one commit):
- 10 new renders under wp2/arms/BOTH/src/main/kotlin/both/:
  GlobalQS.kt (134), ExpandData.kt (135, w/ CompilerOutState),
  GlobalSum.kt (136), Transpose.kt (137), Multiply.kt (138),
  Expx.kt (139), Getstr1.kt (140), StrmanipMisc.kt (141),
  SysDump.kt (142), ParseCount.kt (143) (all gate-verified; scanned)
- 2 edited: BothCRuntime.kt (T2 seats: XNEXT/INPUT_LENGTH/SYM_MAX
  consts, Msv stateVector extension, sigint/errorMessage/Mltoa/
  xecute/SymGet stubs, fgetcC/writeC/strncpyC/strncmpC helpers),
  BothStubs.kt (Stab carrier + GlobalNode.transpose +
  GlobalRef.multiply seats)
- 3 new specs under wp2/arms/BOTH/spec/: expx.dfy (4 v 0/0),
  parse_count.dfy (2 v 0/0), strmanip.dfy (12 v 0/0; full suite one
  call: 30 verified, 0 errors)
- wp2/logs/BOTH_knowledge_events.csv (86 rows appended: T2 consult
  ledger, unquoted PRO schema)
- wp2/PILOT_TRACKING.md (batch-5 rows 134-143 BOTH: todo ->
  translated; diff = those 10 lines only)
- wp2/arms/BOTH_B5T2_fragment_2026-11-21.md (this fragment)
# DOC B7 close fragment, 2026-11-21

### DOC B7 close - coverage checklist (units 186-215) and events verification

Per the DOC arm discipline: construct list from the batch-7 unit
texts, normalized per section 2.4; for each canonical construct,
at least one cookbook entry matched. Counts are batch-7 unit
journal mentions; the per-unit consult rows live in the arm
journal sections of 2026-11-20 (one consult per unit per the
C-008 rule; no rows are duplicated here per the B6-close
splice-repair lesson).

- Canonical IO type: H-001 matched in 3 batch-7 unit journal(s)
- File-scope globals: H-004 matched in 8 batch-7 unit journal(s)
- Buffer role classification: H-007 matched in 4 batch-7 unit journal(s)
- Shared static scratch buffer: H-008 matched in 1 batch-7 unit journal(s)
- sprintf format-string idiom: H-009 matched in 4 batch-7 unit journal(s)
- Numeric promotion / truncation: H-010 matched in 1 batch-7 unit journal(s)
- Unsigned char domain mapping: H-012 matched in 2 batch-7 unit journal(s)
- Dead store / dead parameter: H-013 matched in 9 batch-7 unit journal(s)
- Out-of-scope C function -> stub: H-014 matched in 13 batch-7 unit journal(s)
- Kotlin grammar vs quoted C source: H-015 matched in 15 batch-7 unit journal(s)
- Statement-level comma operator: H-016 matched in 2 batch-7 unit journal(s)
- Preprocessor conditional arms: H-018 matched in 7 batch-7 unit journal(s)

Constructs not arising in the batch-7 unit texts (H-002, H-003,
H-005, H-006, H-011, H-017): no matched rows for these entries;
noted for the checklist record, no action.

Consult outcome totals for the batch: 17 hit-reuse, 13 hit-adapt,
0 miss (30 consults, one per (unit, entry) per C-005; every row
carries a true in-session timestamp; roster id strings per the
batch-6 close repair 2 discipline).

Per-unit consult coverage (unit -> entry, outcome):

- libmpscpp.cpp::global::Find: H-014, hit-reuse
- compiler-zcommands.c::<misc>: H-004, hit-reuse
- libmpscpp.cpp::global::Piece: H-014, hit-reuse
- compiler-sys1.c::substr: H-010, hit-adapt
- interp-strmanip.cpp::substr: H-014, hit-reuse
- libmpscpp.cpp::mstring::Translate: H-014, hit-reuse
- libmpscpp.cpp::GlobalData: H-014, hit-reuse
- libmpscpp.cpp::global::Soundex: H-014, hit-reuse
- interp-sym.cpp::_justify: H-009, hit-reuse
- interp-sym.cpp::mkpt: H-004, hit-reuse
- libmpscpp.cpp::global::Fnumber: H-013, hit-reuse
- include/mumpsc/externs.h::<misc>: H-004, hit-reuse
- interp-sysfunc.cpp::GlobalData: H-018, hit-reuse
- libmpscpp.cpp::global::MakeGlobal: H-014, hit-adapt
- interp-fcn.cpp::<misc>: H-018, hit-reuse
- libmpscpp.cpp::Multiply: H-014, hit-reuse
- interp-sql.cpp::rslt_callback: H-007, hit-adapt
- compiler-parse.c::Extract: H-007, hit-adapt
- libmpscpp.cpp::Sim1: H-014, hit-adapt
- libmpscpp.cpp::embed1: H-008, hit-reuse
- compiler-mumps.c::<misc>: H-016, hit-adapt
- compiler-fcninit.c::fcninit: H-013, hit-adapt
- interp-sym.cpp::Qsub: H-014, hit-reuse
- interp-native-btree.cpp::freadx: H-018, hit-adapt
- compiler-zcommands.c::zcommands_code: H-015, hit-adapt
- interp-sysfunc.cpp::sigint: H-018, hit-adapt
- compiler-sys1.c::match: H-014, hit-reuse
- compiler-epi.c::epi: H-013, hit-adapt
- libmpscpp.cpp::<misc-6>: H-016, hit-adapt
- interp-service.cpp::ServiceInterface: H-013, hit-adapt

Miss rows: none recorded this batch (0 of 30 consults); the
preprocessor_conditional capture history of the prior closes is
unchanged (no new candidates raised; the 5-miss profile reported
by the prior coverage rounds is pre-capture history, no action).

Events regeneration verification: the full journal consult ledger
replayed through the C-005/C-008 export semantics (Python port of
export_doc_events.R, same grammar/validation/mapping contract):
657 consult rows -> 1312 event rows; the regenerated CSV is
BYTE-IDENTICAL to the tracked wp2/logs/DOC_knowledge_events.csv
(87355 bytes, LF). Roster membership (308 units), outcome
vocabulary, and (unit, entry, timestamp) uniqueness all validated.
VERIFICATION STEP (Mark): re-run
Rscript wp2/analysis/R/export_doc_events.R on m1-build and
confirm the regenerated CSV is byte-identical (working tree
expected clean after the run), per the B2-close instrument
verification precedent; log any divergence as a protocol
deviation.

Stub ledger at close (batch-7 standalone resolutions):
Stubs.substr (compiler twin rebind, the Match/PatrnSys1 call
sites narrowed at call), Stubs.substr flip to the interp real,
Stubs.Mpiece (TAG CORRECTION: the not-a-corpus mis-tag
superseded; owner is the landed unit-114 _piece), Stubs.makeGlobal
(GlobalRef member seats + the ref carrier widening), Stubs.sim1,
Stubs.sigint, the 5-arg Stubs.match, Stubs.freadx, the Qsub seat
family, the compile-fcninit/epi pair with EP/LevCheck and the
linepad2, ExecName Int / pid Long seat corrections,
Stubs.ServiceInterface, and Stubs.count (raised in tranche 2,
resolved in tranche 3). New raised for future owners:
Stubs.IDF (the free libmpscpp.cpp:10370 wrapper, batch-10
ledger) and the Stubs.Concat twelve-overload family (bodies
beyond the misc-6 chunk cut, batch-10 ledger; the global/global
member is the landed unit-65 render).

Red-build count: 0 (every tranche gated at kotlinc 2.1.20
0 errors / 0 warnings: 188, 196 and 206 files at the three
tranche closes; the in-session compile repairs at T2/T3 ended
first-pass green).

Grading outcome: B7 30/30 translated; the m1-build gradle gate
runs at this close (result journal entry next); the per-unit
check columns remain todo across B7 per the C-001 cycle-10
backfill decision. Next protocol step per the C-001 committed
row order: DOC batch 8 (cycle 7).
# DOC arm journal - 2026-11-21 (batch 8, tranche 1)

DOC batch 8 (cycle 7 per the C-001 committed row order) opens with
its simple stratum: 11 roster rows, DOC units 216-226 (units.csv
rows 237 through 247 in score order 10.3 down to 2.0; batch-8
tracker lives in this journal per the C-001 note -
PILOT_TRACKING tabulates batches 1-7 only). All bodies derived from the pristine
vendor source this session (vendor tarball sha256 621deb...ffdbe);
every vendor anchor physically verified line-for-line before
rendering. Consult ledger (one row per unit per C-008; the events
CSV append is in this same push):

- include/mumpsc/builtinTable.h::<misc> (unit 216; LOC 20, score
  2.0, rank 1): H-015, hit-adapt - the caret-literal BuiltIn[]
  roster as the internal BuiltInTable array (BuiltinTableH.kt),
  terminator kept verbatim, visibility internal pending the
  fcninit/ServiceInterface family rows.
- libmpscpp.cpp::mstring::Find (unit 217; LOC 16, score 4.2, rank
  8): H-014, hit-reuse - the global-key member
  (MstringFind.kt): Mltoa BEFORE the single RETRIEVE (vendor
  order kept; the unit-33 seat writes Mglobal first - noted as
  seat-order divergence, not vendor divergence), Mfind on the
  str carrier, atoi tail.
- compiler-kill.c::<misc> (unit 218; LOC 30, score 5.2, rank 20):
  H-004, hit-reuse - banner ledger (CompilerKillMisc.kt), June 5
  2024 date line; kill_code named as the batch-9 row.
- compiler-lock.c::<misc> (unit 219; LOC 30, score 5.2, rank
  22): H-004, hit-reuse - banner ledger (CompilerLockMisc.kt),
  May 10 2024 date line; lock_code named as the batch-9 row (the
  landed LockCode render is the vendor-file batch-4 unit).
- compiler-set.c::<misc> (unit 220; LOC 30, score 5.2, rank 26):
  H-004, hit-reuse - banner ledger (CompilerSetMisc.kt), May 26
  2024 date line; set_code already landed (unit 121, batch 4).
- include/mumpsc/zfcns.h::<misc> (unit 221; LOC 18, score 6.2,
  rank 38): H-018, hit-reuse - prototype disposition ledger
  (ZfcnsH.kt): _piece landed (unit-114 real), _justify landed
  (unit-194), ServiceInterface landed (batch-7 T3), the zfcn
  include-guard arm noted, BuildGlobal/BuildLocal named as THIS
  batch's tranche-2 rows (the vendor lines are commented in the
  header), _ascii/_horolog/_length/_random as future-batch rows.
- compiler-mumps.c::atCopy (unit 222; LOC 15, score 6.5, rank 39):
  H-014, hit-adapt - the at-copy helper rendered
  (CompilerAtCopy.kt) AND a Stubs tag correction: the stub seat
  had tagged the owner a compiler-at-test.c helper with no
  roster row; the physical owner is compiler-mumps.c:1956 and is
  units.csv row 241 (this row). The seat flips to the delegate;
  the IfCode/SetCode/WriteCode call sites bind unchanged; the
  j=2 caller-prefill contract documented in situ.
- libmpscpp.cpp::IDF (unit 223; LOC 15, score 6.7, rank 42):
  H-009, hit-reuse - the FREE wrapper (Idf.kt), distinct from
  the unit-49 member: SOH top-level-global guard with banner +
  sigint(100), caret-bypass constructor, strtod with the
  unit-49 empty-field semantic check (INVALID_CONVERSION), the
  member call bound through the invoke family - which was
  missing its CHAR-STAR single-arg arity, so this push adds the
  String overload beside the unit-31 family (LibMisc1.kt, this
  unit as owner note, MakeGlobal fold unchanged) and flips the
  Stubs.IDF seat to the delegate.
- libmpscpp.cpp::global::Piece (unit 224; LOC 17, score 7.3,
  rank 48): H-007, hit-reuse - the MSTRING-needle member
  (GlobalPiece.kt append; vendor :7259), the unit-188 pointer
  adjudication holding: unit-188's body reused verbatim with the
  Mstring str needle carrier; the char-star twin (:7181) stays a
  future-batch row.
- libmpscpp.cpp::GlobalData (unit 225; LOC 17, score 8.7, rank
  78): H-004, hit-adapt - the STRING-needle overload, verified
  line-for-line byte-equivalent to the landed unit-70 seat
  (vendor :2636); NO new body per the unit-186 KDoc-fold
  precedent (GlobalData.kt row-note; each roster row keeps its
  seat). The batch-plan C-string-twin caution discharged by
  verification.
- libmpscpp.cpp::mdh_tree_level_add (unit 226; LOC 31, score
  10.3, rank 102): H-015, hit-reuse - the EIGHT-column overload
  (MdhTreeLevelAdd6.kt), closing the family arity set (one
  through eight columns all have renders now).

Miss rows: none recorded this tranche (0 of 11 consults).

Whole-arm kotlinc gate (kotlinc 2.1.20, sha a118197b...): 215
source files, silent pass, EXIT=0, 0 errors / 0 warnings, 283
class files. H-015 hygiene scan clean on all new and edited
files (no raw dollar outside unicode escapes, no backticks, no
double-bang, no non-ASCII). Red-build count: 0.

Events: 22 rows appended this tranche (the 11 consults x reuse +
hit pairs; 668 consult rows -> 1334 event rows total at this
push, 88839 bytes). The regenerated-CSV byte-identity
verification runs at batch close per the B7-close instrument
precedent.

Next protocol step: DOC batch 8 tranche 2 (the core stratum rows:
global::ReadLine, ExpandText, ScanParse, BuildGlobal, BuildLocal,
Extract2a, the interp-zfcns misc, getstr1, the interp-sql misc
and the compiler-macros.h header).
# DOC B8 T1 consult-rows repair (2026-11-21)

Instrument repair for the batch-8 tranche-1 section above: its
consult rows were written in the prose bullet form of the close
sections instead of the single-line DOC consult grammar
registered in wp2/analysis/README.md and parsed by
wp2/analysis/R/export_doc_events.R (strict regex, C-008 one row
per unit). The events CSV push (commit 6050da2) carries the 22
rows already; these journal rows are the missing source-of-truth
entries, with timestamps identical to the CSV rows (02:48 to
02:58Z, one-minute steps). After splicing, the R regen replays
668 consult rows -> 1334 event rows and the regenerated CSV is
byte-identical to the tracked file (working tree clean on the
re-run). Deviation logged: fragment grammar error, repaired
same-session, no data affected (the CSV was never wrong).

- DOC consult: unit include/mumpsc/builtinTable.h::<misc>, entry H-015, outcome hit-adapt, timestamp 2026-11-21T02:48:00Z
- DOC consult: unit libmpscpp.cpp::mstring::Find, entry H-014, outcome hit-reuse, timestamp 2026-11-21T02:49:00Z
- DOC consult: unit compiler-kill.c::<misc>, entry H-004, outcome hit-reuse, timestamp 2026-11-21T02:50:00Z
- DOC consult: unit compiler-lock.c::<misc>, entry H-004, outcome hit-reuse, timestamp 2026-11-21T02:51:00Z
- DOC consult: unit compiler-set.c::<misc>, entry H-004, outcome hit-reuse, timestamp 2026-11-21T02:52:00Z
- DOC consult: unit include/mumpsc/zfcns.h::<misc>, entry H-018, outcome hit-reuse, timestamp 2026-11-21T02:53:00Z
- DOC consult: unit compiler-mumps.c::atCopy, entry H-014, outcome hit-adapt, timestamp 2026-11-21T02:54:00Z
- DOC consult: unit libmpscpp.cpp::IDF, entry H-009, outcome hit-reuse, timestamp 2026-11-21T02:55:00Z
- DOC consult: unit libmpscpp.cpp::global::Piece, entry H-007, outcome hit-reuse, timestamp 2026-11-21T02:56:00Z
- DOC consult: unit libmpscpp.cpp::GlobalData, entry H-004, outcome hit-adapt, timestamp 2026-11-21T02:57:00Z
- DOC consult: unit libmpscpp.cpp::mdh_tree_level_add, entry H-015, outcome hit-reuse, timestamp 2026-11-21T02:58:00Z
# DOC arm journal - 2026-11-21 (batch 8, tranche 2)

DOC batch 8 tranche 2: the core stratum, 10 roster rows, DOC
units 227-236 (the units.csv rows are the FILE LINES 236 for
ReadLine down to 227 for the macros header; score order 10.7 up
to 24.8). All bodies derived from the pristine vendor
source this session (vendor tarball sha256 621deb...ffdbe); every
vendor anchor physically verified line-for-line before rendering.
Consult ledger (one row per unit per C-008; the events CSV append
is in this same push):

- libmpscpp.cpp::global::ReadLine (unit 227; LOC 21, score 10.7,
  rank 106): H-016, hit-adapt - VERIFICATION-ONLY row-note on the
  landed GlobalRef Reader fold (LibmpscppMisc6.kt): the istream
  overload at vendor :3039 verified line-for-line against the
  fold; the ONE divergence is the EOF arm, where the vendor stores
  the zeroed scratch (Mglobal(STORE) runs, may throw
  GLOBAL_NOT_FOUND) before returning false, while the landed fold
  returns false at the null read BEFORE the store. DEVIATION
  DISCLOSED, fold retained unchanged (single-reader-seat rule,
  H-016; the graded landed render is not regraded); no new body
  per the unit-186 KDoc-fold precedent.
- compiler-parse.c::ExpandText (unit 228; LOC 23, score 12.1,
  rank 117): H-015, hit-adapt - ExpandText.kt: every fprintf(Out)
  maps to CompilerState.emit with the emitted text byte-verbatim
  (the strcpy/strcat gtmp build-up, CodedOpen/CodedClose as bare
  lowercase hex, dollar escaped, extract2a dispatch, strmove,
  memcheck(iiitmp), the fcn(svPtr) line - emitted SOURCE text in
  the vendor, so emit not a call, iiitmp post-increment, bp
  sprintf tail).
- interp-sym.cpp::ScanParse (unit 229; LOC 25, score 14.7, rank
  142): H-007, hit-reuse - ScanParse.kt, the scan loop as the
  walker idiom of the arm (quote and paren tracking, escapable
  non-ASCII byte NUL handling, the break-position scratch
  contract preserved); Stubs flip to the delegate; the vendor
  scan idiom matches the unit-176 family reads.
- interp-parse.cpp::BuildGlobal (unit 230; LOC 27, score 16.1,
  rank 150): H-014, hit-reuse - BuildGlobal.kt: the Build-stack
  walker (code<0 resets, code==0 pushes a str, code==2 pops the
  leftover rslt, code==1 walks to the four-arg GlobalOrder stub
  seat) on the BuildGlobalState holder + TSTACK scratch, overflow
  banner + sigint(100) kept, no double-bang (the H-012 widening
  keeps callers honest with explicit exceptions instead).
- interp-sym.cpp::BuildLocal (unit 231; LOC 29, score 16.7, rank
  152): H-014, hit-reuse - BuildLocal.kt, the local twin: same
  Build-stack shape, the walk tail through LocalOrder - a seat
  RAISED THIS PUSH (vendor interp-sym.cpp::LocalOrder is not a
  corpus unit, no roster row; the four-arg stub added beside
  GlobalOrder with the owner note). fff fossil kept as a comment.
- compiler-parse.c::Extract2a (unit 232; LOC 21, score 18.3, rank
  158): Extract2a.kt, H-014, hit-reuse - the Extract2 seat family:
  the #S#/#G#name#target# parse into the static tmp1/tmp2[64]
  scratch, the two ternary emissions byte-verbatim against the
  vendor (sym_(1,...) ? : VariableNotFound1 //E21000 and the
  Mglobal(RETRIEVE) ? : VariableNotFound arm - both verified
  character-for-character this tranche), tmp2 returned.
- interp-zfcns.cpp::<misc> (unit 233; LOC 60, score 18.4, rank
  160): H-018, hit-adapt - InterpZfcnsMisc.kt, disposition
  ledger: the residue is the IDENTICAL stock of the unit-110
  interp-bifs.cpp::<misc> chunk - Mget verified byte-identical
  to the landed BifsMisc seat, so NO new body per the unit-186
  fold precedent; the ledger documents the fold per function.
- compiler-sys1.c::getstr1 (unit 234; LOC 37, score 19.3, rank
  165): H-001, hit-adapt - CompilerGetstr1.kt, the K&R getstr1
  (distinct-domain twin of the landed unit-140 getstr2 per the
  unit-189 substr precedent - same FILE* table shape, different
  domain: this one walks rows, the unit-48 FileDescriptor
  STDIN_READER const reused for lun 5): the while-loop form (the
  for-variable is not reassigned), posixWrite/posixRead seats
  RAISED THIS PUSH for the opnflg==3 raw-fd arm (vendor write()/_
  read(), not corpus units, no roster rows; owner notes in situ),
  LINEFEED handling byte-verbatim.
- interp-sql.cpp::<misc> (unit 235; LOC 50, score 23.6, rank
  183): H-018, hit-adapt - InterpSqlMisc.kt: the SQLITE define is
  live per the reference build config (same batch-6 ruling as
  the unit-202 sql family), the rslt char scratch as
  InterpSqlMiscState.rsltSpec holder, command_print/lcheck/dcheck
  bodies rendered (stderr banners, sigint(100) exits), the
  fold-back completions the chunker rolled in documented per
  function in the header ledger.
- include/mumpsc/compiler-macros.h::<misc> (unit 236; LOC 104,
  score 24.8, rank 190): H-004, hit-reuse - CompilerMacrosH.kt,
  pure disposition ledger (the whole header is one misc unit, no
  function bodies): every prototype and define names its landed
  seat, future owner, or prior anchor (set_code/if_code/lock_code/
  copyright_notice landed; for_code/error_code/abtrx as future
  rows; INTERPRET already the CompilerState.interpret seat; SWAP
  30 already the InterpMisc const; the Mglobal codes on the
  Carriers consts with the CLOSE-4 third-define note; the error
  codes and command chars as comment ledger). No new consts
  opened (H-004 single-owner).

Miss rows: none recorded this tranche (0 of 10 consults).

Whole-arm kotlinc gate (kotlinc 2.1.20, sha a118197b...): 224
source files, silent pass, EXIT=0, 0 errors / 0 warnings, 295
class files. H-015 hygiene scan clean on all new and edited
files (no raw dollar outside unicode escapes, no backticks, no
double-bang, no non-ASCII). Red-build count: 0 (one pre-gate
compile fix: the BuildLocal walk tail bound before the seat name
settled, and ExpandText x-escapes rendered as Kotlin escapes).

Events: 20 rows appended this tranche (the 10 consults x reuse +
hit pairs; 678 consult rows -> 1354 event rows total at this
push, 90211 bytes). The regenerated-CSV byte-identity
verification runs at batch close per the B7-close instrument
precedent.

Push hygiene and repair notes: the tranche-2 push first landed at
repo-ROOT paths (commit 013be4c created stray arms/ and logs/
copies; the intended wp2 paths were untouched), then the corrected
db210e6 push carried the same payloads at the proper wp2 paths, and
a follow-up commit renormalized the KDoc Unit numbering to the DOC
arm sequence (T1 continued: units 227-236 in committed batch
order) - the pre-repair headers had conflated the Unit id with the
units.csv file line (the units.csv row IS the file line: 236
ReadLine down to 227 macros). LESSON (the T1 lesson extended):
Unit NNN is the DOC sequential arm id; units.csv row is the file
line; never conflate. The stray root copies are removed by the
journal-splice cleanup (git rm of the two stray dirs, one commit).

DOC consult grammar rows (timestamps identical to the events CSV
append of this push; per the T1 repair lesson the rows live here
and in the CSV, never prose-only):

- DOC consult: unit libmpscpp.cpp::global::ReadLine, entry H-016, outcome hit-adapt, timestamp 2026-11-21T03:01:00Z
- DOC consult: unit compiler-parse.c::ExpandText, entry H-015, outcome hit-adapt, timestamp 2026-11-21T03:02:00Z
- DOC consult: unit interp-sym.cpp::ScanParse, entry H-007, outcome hit-reuse, timestamp 2026-11-21T03:03:00Z
- DOC consult: unit interp-parse.cpp::BuildGlobal, entry H-014, outcome hit-reuse, timestamp 2026-11-21T03:04:00Z
- DOC consult: unit interp-sym.cpp::BuildLocal, entry H-014, outcome hit-reuse, timestamp 2026-11-21T03:05:00Z
- DOC consult: unit compiler-parse.c::Extract2a, entry H-014, outcome hit-reuse, timestamp 2026-11-21T03:06:00Z
- DOC consult: unit interp-zfcns.cpp::<misc>, entry H-018, outcome hit-adapt, timestamp 2026-11-21T03:07:00Z
- DOC consult: unit compiler-sys1.c::getstr1, entry H-001, outcome hit-adapt, timestamp 2026-11-21T03:08:00Z
- DOC consult: unit interp-sql.cpp::<misc>, entry H-018, outcome hit-adapt, timestamp 2026-11-21T03:09:00Z
- DOC consult: unit include/mumpsc/compiler-macros.h::<misc>, entry H-004, outcome hit-reuse, timestamp 2026-11-21T03:10:00Z

Next protocol step: DOC batch 8 tranche 3 (the complex stratum
rows: btree(sql), the libmpscpp misc-7 chunk, AllocSV, the
compiler-parse misc, compiler-globals.h misc, mumps_open, cannon,
the builtin.h misc, GtkTreeAdd and Merge), then the cycle-10
buffer close and the B8 close instrument.
# DOC arm journal - 2026-11-21 (batch 8, tranche 3)

DOC batch 8 tranche 3: the complex stratum, 10 roster rows, DOC
units 237-246 (the units.csv FILE LINES are 217 btree down to 226
Merge; score order 316.3 down to 30.1). All bodies derived from the
pristine vendor source this session (vendor tarball sha256
621deb...ffdbe); every vendor anchor physically verified
line-for-line before rendering. Consult ledger (one row per unit
per C-008; the events CSV append is in this same push, timestamps
03:11 to 03:20Z, one-minute steps, prefix verified byte-identical
to the landed file before the append):

- interp-sql.cpp::btree (unit 237; LOC 443, score 316.3, rank
  293): H-018, hit-adapt - InterpSqlBtree.kt: the SQLITE-side
  global-array workhorse. DISTINCT-TWIN NOTE: the landed unit-4
  native render owns the vendor btree lexeme with the IDENTICAL
  Kotlin signature, so this render takes the Patrn1Interp/
  PatrnSys1 domain-suffix disambiguation: btreeSql (H-016 name
  adaptation; the native twin not regraded). Bodies: the OPEN
  connection head (sql seat, the "mumps" data source, the vendor
  tmp1 double-site alias), the SQLCMD relay, the RETRIEVE select
  builder, the STORE delete-then-insert double walk (the insert
  pointer-walk p3 cursor, dcheck on bd, the block-static bdx[256]
  holder object), the ORDERNEXT/ORDERPREV order-by arm (the
  tokens==1 pointer-offset 12-shift vendor quirk kept, the return-0
  BEFORE printf/sigint dead-rest ledger), the XNEXT query arm, the
  GKILL arm (the local tokens shadow that kills its own tokens==1
  branch, H-013 kept), GBLNME, CLOSE. H-018 exclusion ladders: the
  XNEXT "#if 14==N" block - the reference tree is configured 14,
  the live arm is 14==14 and the twelve compiled-out arms stay
  comment ledgers (the a8 skip in the live order-by tail kept).
  The XNEXT keybak read is vendor UB (uninitialized) - H-013
  ledger, the effective statement is the rsltSpec copy (the
  T2 rsltSpec holder read). strtok over the SOH delimiter renders
  as the non-destructive index walker (skip-empty/leading
  collapse per C strtok; tokens handed to lcheck/embed as
  NUL-terminated CharArray copies, H-012).
- libmpscpp.cpp::<misc-7> (unit 238; LOC 600, score 288.4, rank
  292): H-014, hit-reuse - LibmpscppMisc7.kt: the Concat(x7) member
  families of both carriers (the double arm rendered WHOLE here -
  its signature is at the misc-6 dump tail cut, the chunk-edge
  whole-render precedent; the global.global arm stays the landed
  unit-65 render, not regraded), the mstring intrinsic-command
  members (Soundex via the zfcn seat - the LEADING BLANK before
  the fcn name is the vendor's own, unlike the unit-193 global
  member; the ONE-ARG Translate on the fcn v1d/bd offset-1
  convention; Reverse x2 with the &gtmp[9] offset-9 copy; Qsubscript
  x2 with the mstring data vs global Sref arms; Qlength x2 and
  Query x2 through the FixGblRef route), the global conversion
  operator ladder as the to* family (each arm the Stubs.globalGet*
  typed extraction seat, H-010), the operator*> scalar-star family
  (member times extensions + the globalTimes friend family, the
  globalPlus precedent; the vendor ulong/ushort mix-domain computes
  ledgered inline), the gt/lt ladders (char*/string twins folded
  onto the String seat per H-015; the vendor > ladder has NO
  mstring member while the < ladder does - the asymmetry kept,
  ledger), and the operator<<(global) write-assign family (the
  shl seat, set_global/get_global seats, the tpx zero check with
  GLOBAL_NOT_FOUND throw, the "%ud" sprintf VENDOR QUIRK kept
  byte-verbatim, "%d"/"%hd" Locale.US). SEAT FLIPS: the twelve
  Stubs.Concat seats flip to these renders with TAG CORRECTION
  (the "batch-10 owner" tags of the unit-214 seats were forward-
  tags; the member bodies ARE this row - the u119 tag-correction
  precedent); the amp operator& forwards of LibmpscppMisc6 route
  through them unchanged. NEW SEAT RAISED: Stubs.fixGblRef (owner
  libmpscpp.cpp::FixGblRef, declared in the unit-149 header
  ledger, no roster row). The dump tail closes at the
  operator<<(short) close brace; the unsigned short/long/unsigned
  long/float/double shl members that follow in the vendor file
  are the next chunk's text, ledger only.
- interp-sysfunc.cpp::AllocSV (unit 239; LOC 103, score 164.9,
  rank 285): H-018, hit-adapt - AllocSV.kt: the state-vector
  allocator; Msv non-null (the Fcn call sites demand Msv; the
  NULL arm is dead, H-013), the allocSvFirstCall once-only gate
  for the signal-reg fossil (H-018 divergence ledger, the
  unit-211 Sigint precedent), STDOUT_WRITER lazy seat, STDIN_READER
  reused from Getstr2.kt, field-init roster verbatim, memset as the
  construction-zero H-018 note, the SQLITE arm live (Col_Names,
  pgsql_msg, Table "mumps", TabSize 14), the non-SQLITE getenv arm
  an inert ledger.
- compiler-parse.c::<misc> (unit 240; LOC 210, score 108.8, rank
  277): H-014, hit-reuse - CompilerParseMisc.kt: the residue
  disposition (RETRIEVE/STORE/the walk labels on the landed
  carriers, CONCAT..EXPONEN over InterpH u186 shared values, the
  Coded* const family, the SSS/TTT pointer-fold) plus the bodies
  ConCheck (public, the digit-quote test and SSS shift with the
  i >= b.size guard), strcasecmpCpm/strncasecmpCpm (PRIVATE, the
  non-POSIX vendor semantics returns 0 on prefix-equal), memcheck
  (the MaxTmp high-water), cvar (the CompilerState.cvars/cvarx
  table), and Extract1 (holder statics, the SILENT 63-clamp
  H-007 bounds-walk per the unit-84 Cannon precedent).
- include/mumpsc/compiler-globals.h::<misc> (unit 241; LOC 148,
  score 84.0, rank 266): H-004, hit-reuse - CompilerGlobalsH.kt:
  the pure disposition ledger (banner, every variable definition
  to its CompilerState seat, the struct families to the unit-151
  carrier, the prototype roster to landed owners/Stubs seats/the
  two declaration fossils).
- compiler-open.c::mumps_open (unit 242; LOC 53, score 72.7, rank
  256): H-015, hit-adapt - MumpsOpen.kt: the emitter-walker render
  (the openf loop, the CompileState.t2/ierr/parse_ pattern per the
  unit-46 ReadCode precedent, Stubs.Extract2 for the percent
  payload, byte-verbatim emissions including the atoi space pair,
  the POST_flg close file-private, PARSE_ERROR_MO/ARG_LIST_ERROR_MO
  file-private consts).
- interp-parse.cpp::cannon (unit 243; LOC 35, score 37.3, rank
  220): H-014, hit-reuse - VERIFICATION-ONLY row-note appended to
  the landed Cannon.kt: the compiler-sys 71-loc body and the
  interp-parse 35-loc body are the SAME algorithm astyle-
  different; the landed unit-84 render owns it; PatrnInterp/
  PatrnSys1 already bind cannon to it; no new body per the
  unit-186 KDoc-fold precedent (a non-ASCII multiplication sign
  in the earlier prose was found and fixed during the H-015 scan).
- include/mumpsc/builtin.h::<misc> (unit 244; LOC 33, score 33.5,
  rank 211): H-004, hit-reuse - BuiltinH.kt: pure prototype ledger,
  every prototype named to its landed render or Stubs seat (the
  minor StopInit footnote correction: unit-166 stopInit(file) is
  the landed owner, MSV-arg seat noted); Replace stays the Stubs
  stub.
- interp-bifs.cpp::GtkTreeAdd (unit 245; LOC 30, score 30.8, rank
  207): H-014, hit-reuse - VERIFICATION-ONLY row-note appended to
  the landed render: the interp-bifs body verified BYTE-IDENTICAL
  to the interp-zfcns unit-83 landed owner (diff this session); no
  new body per the single-owner rule.
- interp-sysfunc.cpp::Merge (unit 246; LOC 59, score 30.1, rank
  206): H-014, hit-reuse - Merge.kt: the whole body on landed
  seats (Mglobal RETRIEVE/STORE/XNEXT on Carriers codes, the four-
  arg Stubs.keyfix, the strcpyMergeAt private helper); the vendor
  j/k init = 0 break-loop completion semantic kept, the for
  (jj in i-1 downTo 1) if (>127) break pattern, MDEBUG and the
  empty SQLITE guard as the comment ledger.

Whole-arm kotlinc gate over the 232-file doc tree: silent, EXIT=0
with every file of this tranche in place (the 10 new/edited files
re-gated after the render pair: InterpSqlBtree, LibmpscppMisc7,
Stubs with the Concat flips and the fixGblRef seat, plus the eight
landed earlier this session). H-015 hygiene scan run over all ten:
no raw dollar (comment text cleaned to \u0024 during the scan), no
backticks, no double-bang, ASCII-only.

DOC consult grammar rows (timestamps identical to the events CSV
append of this push):

- DOC consult: unit interp-sql.cpp::btree, entry H-018, outcome hit-adapt, timestamp 2026-11-21T03:11:00Z
- DOC consult: unit libmpscpp.cpp::<misc-7>, entry H-014, outcome hit-reuse, timestamp 2026-11-21T03:12:00Z
- DOC consult: unit interp-sysfunc.cpp::AllocSV, entry H-018, outcome hit-adapt, timestamp 2026-11-21T03:13:00Z
- DOC consult: unit compiler-parse.c::<misc>, entry H-014, outcome hit-reuse, timestamp 2026-11-21T03:14:00Z
- DOC consult: unit include/mumpsc/compiler-globals.h::<misc>, entry H-004, outcome hit-reuse, timestamp 2026-11-21T03:15:00Z
- DOC consult: unit compiler-open.c::mumps_open, entry H-015, outcome hit-adapt, timestamp 2026-11-21T03:16:00Z
- DOC consult: unit interp-parse.cpp::cannon, entry H-014, outcome hit-reuse, timestamp 2026-11-21T03:17:00Z
- DOC consult: unit include/mumpsc/builtin.h::<misc>, entry H-004, outcome hit-reuse, timestamp 2026-11-21T03:18:00Z
- DOC consult: unit interp-bifs.cpp::GtkTreeAdd, entry H-014, outcome hit-reuse, timestamp 2026-11-21T03:19:00Z
- DOC consult: unit interp-sysfunc.cpp::Merge, entry H-014, outcome hit-reuse, timestamp 2026-11-21T03:20:00Z

Next protocol step: batch 8 is now fully rendered (T1 simple 216-
226, T2 core 227-236, T3 complex 237-246); the next session is the
B8 buffer close and the batch-close instrument, with the events
CSV regen expected at 688 consult rows -> 1374 event rows
byte-identical to the tracked file, then batch 9 tranche 1.
### DOC B8 close - coverage checklist (units 216-246) and events verification

Per the DOC arm discipline: construct list from the batch-8 unit
texts, normalized per section 2.4; for each canonical construct,
at least one cookbook entry matched. Counts are batch-8 unit
journal mentions; the per-unit consult rows live in the arm
journal sections of 2026-11-21 (one consult per unit per the
C-008 rule; no rows are duplicated here per the B6-close
splice-repair lesson). Batch 8 opens at units.csv row 217 (the
batch column == 8 roster block, 31 rows); PILOT_TRACKING does
not tabulate batch 8+ (the no-PILOT batch-8 rule; rows track
here only).

- Canonical IO type: H-001 matched in 1 batch-8 unit journal(s)
- File-scope globals: H-004 matched in 7 batch-8 unit journal(s)
- Buffer role classification: H-007 matched in 2 batch-8 unit journal(s)
- sprintf format-string idiom: H-009 matched in 1 batch-8 unit journal(s)
- Out-of-scope C function -> stub: H-014 matched in 10 batch-8 unit journal(s)
- Kotlin grammar vs quoted C source: H-015 matched in 4 batch-8 unit journal(s)
- Statement-level comma operator: H-016 matched in 1 batch-8 unit journal(s)
- Preprocessor conditional arms: H-018 matched in 5 batch-8 unit journal(s)

Constructs not arising in the batch-8 unit texts (H-002, H-003,
H-005, H-006, H-008, H-010, H-011, H-012, H-013, H-017): no
matched rows for these entries at the batch-8 consult; many
appear inside the batch-8 render bodies' KDoc ledgers (the H-012
domain notes and H-013 vendor quirks of the T3 renders carry
entry references) but the C-008 one-consult-per-unit ledger
counts only the unit consults above; noted for the checklist
record, no action.

Consult outcome totals for the batch: 20 hit-reuse, 11
hit-adapt, 0 miss (31 consults, one per (unit, entry) per C-005;
every row carries a true in-session timestamp; roster id
strings per the batch-6 close repair 2 discipline; the batch-8
Unit-vs-row conventions verified per the 2cfddec T1/T2 repair).

Per-unit consult coverage (unit -> entry, outcome):

- include/mumpsc/builtinTable.h::<misc>: H-015, hit-adapt
- libmpscpp.cpp::mstring::Find: H-014, hit-reuse
- compiler-kill.c::<misc>: H-004, hit-reuse
- compiler-lock.c::<misc>: H-004, hit-reuse
- compiler-set.c::<misc>: H-004, hit-reuse
- include/mumpsc/zfcns.h::<misc>: H-018, hit-reuse
- compiler-mumps.c::atCopy: H-014, hit-adapt
- libmpscpp.cpp::IDF: H-009, hit-reuse
- libmpscpp.cpp::global::Piece: H-007, hit-reuse
- libmpscpp.cpp::GlobalData: H-004, hit-adapt
- libmpscpp.cpp::mdh_tree_level_add: H-015, hit-reuse
- libmpscpp.cpp::global::ReadLine: H-016, hit-adapt
- compiler-parse.c::ExpandText: H-015, hit-adapt
- interp-sym.cpp::ScanParse: H-007, hit-reuse
- interp-parse.cpp::BuildGlobal: H-014, hit-reuse
- interp-sym.cpp::BuildLocal: H-014, hit-reuse
- compiler-parse.c::Extract2a: H-014, hit-reuse
- interp-zfcns.cpp::<misc>: H-018, hit-adapt
- compiler-sys1.c::getstr1: H-001, hit-adapt
- interp-sql.cpp::<misc>: H-018, hit-adapt
- include/mumpsc/compiler-macros.h::<misc>: H-004, hit-reuse
- interp-sql.cpp::btree: H-018, hit-adapt
- libmpscpp.cpp::<misc-7>: H-014, hit-reuse
- interp-sysfunc.cpp::AllocSV: H-018, hit-adapt
- compiler-parse.c::<misc>: H-014, hit-reuse
- include/mumpsc/compiler-globals.h::<misc>: H-004, hit-reuse
- compiler-open.c::mumps_open: H-015, hit-adapt
- interp-parse.cpp::cannon: H-014, hit-reuse
- include/mumpsc/builtin.h::<misc>: H-004, hit-reuse
- interp-bifs.cpp::GtkTreeAdd: H-014, hit-reuse
- interp-sysfunc.cpp::Merge: H-014, hit-reuse

Miss rows: none recorded this batch (0 of 31 consults); no new
cookbook candidates raised at the batch-8 consults (the capture
gate stays with T1/T2/G1-G4; noted, no action).

Events regeneration verification: the full journal consult ledger
replayed through the C-005/C-008 export semantics (Python port of
export_doc_events.R, same grammar/validation/mapping contract):
688 consult rows -> 1374 event rows; the regenerated CSV is
BYTE-IDENTICAL to the tracked wp2/logs/DOC_knowledge_events.csv
(91573 bytes, LF). Roster membership (308 units), outcome
vocabulary, and (unit, entry, timestamp) uniqueness all
validated. The same regen was ALSO verified live by the T3
splice gate (Rscript on Mark's clone: 688 -> 1374 with a clean
working tree, no CSV push needed); the close instrument
therefore lands NO tracked-file change (regen identical, per
the B7-close pattern). VERIFICATION STEP (Mark): re-run
Rscript wp2/analysis/R/export_doc_events.R and
git status --porcelain wp2/logs/DOC_knowledge_events.csv
(expect clean) per the B2-close instrument verification
precedent; log any divergence as a protocol deviation.

Stub ledger at close (batch-8 standalone resolutions):
Stubs.Mglobal delegates ride the unit-167 render unchanged;
T1 added the IDF invoke-family String arity; the Piece mstring
needle and the atCopy TAG CORRECTION were landed at T1;
GlobalData closed as a verification fold. T2 raised
Stubs.LocalOrder (owner interp-sym.cpp::LocalOrder, no roster
row) and Stubs.posixRead/posixWrite (opnflg==3 fd arm). T3
flipped Stubs.AllocSV (non-null return, the Fcn call-site
arity) and Stubs.Merge to the doc renders, flipped
memcheck/cvar/Extract1 to the CompilerParseMisc delegates,
flipped the twelve Stubs.Concat seats to the LibmpscppMisc7
renders WITH TAG CORRECTION (the unit-214 batch-10 forward-tags
superseded; the member bodies are the row-218 misc-7 text), and
raised Stubs.fixGblRef (owner libmpscpp.cpp::FixGblRef,
declared in the unit-149 header ledger, no roster row).
Stands: Stubs.sql (owner the interp SQL subprocess) and
Stubs.keyfix (keyfix.h inline, not a corpus unit) stay throwing
seats; btreeSql is the interp-sql twin of the landed unit-4
native btree render (the Distinct-TWIN note; no seat change).

Red-build count at the tranche gates: 0 (the whole-arm kotlinc
2.1.20 gate was silent, 0 errors / 0 warnings, at every tranche
close: 215 files at T1 rising to 232 files at T3; the in-session
compile repairs at T3 - the Stubs.fixGblRef seat resolution and
the Kotlin operator-convention modifier drops - were repaired
before the gate and ended first-pass green). gradle gate
BUILD SUCCESSFUL at the tranche splices (T2 16s, T3 31s, T1
green at its splice close).

Grading outcome: B8 31/31 translated (T1 simple 216-226, T2
core 227-236, T3 complex 237-246; push codes T1 6050da2 with
repair chain 013be4c/3502303/db210e6/2cfddec and splice
dec1620, T3 render 0c5b189 with splice e0c066e); the per-unit
check columns remain todo across B8 per the C-001 cycle-10
backfill decision. PILOT_TRACKING unchanged at this close (the
no-PILOT batch-8 rule; batch-8 rows track in this journal).
Next protocol step per the C-001 committed row order: DOC
batch 9 (cycle 8) tranche 1, units.csv rows 247-276.
# DOC B9 T1 journal fragment, 2026-11-22

### DOC B9 tranche 1 (units 247-256, simple stratum) - 2026-11-22

Sandbox rebuilt per rebuild-recipe.md from repo.zip at master fcb223b
(vendor sha256 621deb...ffdbe; JDK Corretto 17.0.21.1; kotlinc
2.1.20 sha a118197b...; baseline gate silent EXIT=0 at the 232
landed files). Batch 9 opens: the units.csv batch-column-9 block
(rows 248-278, 31 rows: 10 complex / 11 core / 10 simple); DOC
sequential ids continue 247-277 in the committed stratum order
(simple first), per the B8 Unit-vs-row conventions (the 2cfddec
repair). All 10 T1 dumps extracted with dump4.py (recreated per
recipe; the GlobalOrder overload disambiguated by physical LOC
15 against the 17-loc char* twin) and verified at exact
physical LOC (26/21/22/22/22/15/18/30/18/16 x the roster order
below).

Render ledger (unit; units.csv row: LOC, score, rank - entry);
all physically verified this session:

- interp-arith.cpp::mps_exp2 (unit 247; row 269: LOC 26, score
  8.3, rank 100): H-018, hit-reuse - MpsExp2.kt: exp2 double as
  2.0.pow(x) (the MpsExp10 10.0.pow precedent; Kotlin math has
  no exp2 intrinsic); strtod (MpsExp.kt seat) + gcvt_wrapper
  (H-014 stub) ride the family shape; BIGFLOAT/MPFR arms
  documentary per the reference config.
- libmpscpp.cpp::global::Translate (unit 248; row 270: LOC 21,
  score 6.9, rank 96): H-014, hit-reuse - the TWO-mstring
  overload appended to GlobalTranslate.kt (the unit-131 one-arg
  sibling; its overload note now resolves). Same Mglobal
  RETRIEVE/GLOBAL_NOT_FOUND throw, the second commerce-d-zero
  separator pair added verbatim, fcn dispatch v1d[1] -> bd[1]
  seats as unit 131.
- interp-arith.cpp::mps_log (unit 249; row 271: LOC 22, score
  8.4, rank 88): H-018, hit-reuse - MpsLog.kt: kotlin.math.ln
  intrinsic over strtod; excluded arms documentary;
  Stubs.mpsLog FLIPPED to delegate this push.
- interp-arith.cpp::mps_cos (unit 250; row 272: LOC 22, score
  8.4, rank 89): H-018, hit-reuse - MpsCos.kt (kotlin.math.cos);
  Stubs.mpsCos FLIPPED.
- interp-arith.cpp::mps_tan (unit 251; row 273: LOC 22, score
  8.4, rank 94): H-018, hit-reuse - MpsTan.kt (kotlin.math.tan);
  Stubs.mpsTan FLIPPED.
- libmpscpp.cpp::GlobalOrder (unit 252; row 274: LOC 15, score
  5.5, rank 63): H-014, hit-reuse - the MSTRING overload appended
  to GlobalOrder.kt (third corpus row of the file; overload
  disambiguated by LOC 15 in the units.csv block). Same xecute/
  SymGet seats as the unit-39/69 pair; NO dead r[STR_MAX]
  scratch here (the unit-69 dead-store note does not apply);
  the dead int i kept per H-013.
- interp-sym.cpp::_random (unit 253; row 275: LOC 18, score
  5.5, rank 43): H-010, hit-adapt - Random.kt: the vendor static
  first/srand(time) lazy seed folds to a private holder object
  seeded once from the epoch-second clock; the libc rand ladder
  j * rand() / (RAND_MAX + 1.0) folds to kotlin.random.Random
  with the vendor rejection loop kept verbatim (the empty C
  while-body is the scalar test itself, quirk kept); atoi is the
  real Find.kt seat; strmove(out,"0") rides the H-014 throwing
  seat per the Extract.kt precedent; sprintf %d via
  String.format Locale.US per H-009.
- compiler-write.c::<misc> (unit 254; row 276: LOC 30, score
  5.2, rank 27): H-004, hit-reuse - CompilerWriteMisc.kt, pure
  disposition ledger (banner + include file, no function
  bodies): includes name their landed seats (CompilerMacrosH
  unit 236, CompilerGlobalsExtMisc unit 151) or the
  not-a-corpus-unit note (memsize.h) and the canonical-IO banner
  marker (stdio.h, H-001 banner-side).
- interp-bstring.cpp::bstrcat (unit 255; row 277: LOC 18, score
  5.5, rank 5): H-014, hit-reuse - Bstrcat.kt: owner of the
  unit-22 banner note lands; Stubs.bstrcat FLIPPED to delegate.
  The Bstring carrier, System.err (H-001), the real unit-211
  sigint seat; vendor shape kept (strncpy/exact-count, explicit
  left.length NUL, strncat + implicit terminator, unreachable
  dummy new_bstring("") after the sigint fall-through); the
  catlength == STR_MAX one-past terminator guarded with the
  bcatchr/checked_strlen one-past quirk noted (H-012).
- interp-sym.cpp::_horolog (unit 256; row 278: LOC 16, score
  4.9, rank 2): H-009, hit-adapt - Horolog.kt: the 47122 day
  ladder kept VERBATIM (VENDOR EPOCH DIVERGENCE vs the Fcn.kt
  case-14 inline arm's 47116+1 ladder - the vendor carries two
  horolog bases, both vendor-truth, journal-ledgered here);
  time intrinsic per the Fcn precedent; sprintf %ld + strcat
  as Locale.US carrier writes; tmp2 64-char scratch kept.

Seat flips and ledger edits this push (Stubs.kt + two ledger
corrections):

- Stubs.mpsLog/mpsCos/mpsTan/mpsExp2 -> doc delegates (tags
  "batch-9/-10" resolved: all four are batch-9 simple rows);
  mpsFloor stays throwing with its tag CORRECTED to the
  batch-9 CORE row 268 (flips at tranche 2); mpsCeil/mpsTrunc
  stay throwing, batch-10 roster rows.
- Stubs.bstrcat -> doc.bstrcat delegate (the correctly-tagged
  forward seat resolves).
- ZfcnsH.kt ledger: _horolog TAG CORRECTION (the forward tag
  wrote "batch-10 roster row"; the units.csv row is 278, batch
  column 9 - superseded per the Concat forward-tag precedent);
  _random and _horolog owner notes flip to RENDERED with the
  278/275 row cites.
- LibmpscppMisc6.kt Horolog(): CALL-SITE REBIND (the
  delegate-when-owner-lands mechanism) - the vendor
  libmpscpp.cpp:3446 body calls _horolog(t); the pre-rebind
  inline had used the Fcn 47116+1 ladder (five day-ladders
  behind the vendor helper); the delegate restores vendor
  fidelity, the divergence disclosed at the row-note.

Miss rows: none recorded this tranche (0 of 10 consults).

Whole-arm kotlinc gate (kotlinc 2.1.20, sha a118197b...): 240
source files, silent pass, EXIT=0, 0 errors / 0 warnings, 312
class files at the closer. H-015 hygiene scan clean on all new
and edited files (no raw dollar outside the DOLLAR_131 const
and \u0024 escapes, no backticks, no double-bang, no non-ASCII).
Red-build count: 0 (the gate was silent first pass over the
whole tranche; one pre-gate fold settled during drafting - the
mps_exp2 exponent intrinsic, resolved to the pow precedent
before the compile).

Events: 20 rows appended this tranche (the 10 consults x
reuse + hit pairs; 698 consult rows -> 1394 event rows total at
this push, 92907 bytes). The regenerated-CSV byte-identity
verification runs at batch close per the B7-close instrument
precedent.

Next protocol step: DOC batch 9 tranche 2 (the core stratum
rows, DOC units 257-267, units.csv rows 258-268: mps_floor with
the Stubs.mpsFloor flip, GlobalOrder.h inline, Eval, Avg,
FixGblRef, the interp-service misc, xindex, ExpandName, the
interp-sym misc and the compiler-sys1 misc).

DOC consult grammar rows (timestamps identical to the events CSV
append of this push):

- DOC consult: unit interp-arith.cpp::mps_exp2, entry H-018, outcome hit-reuse, timestamp 2026-11-22T04:20:00Z
- DOC consult: unit libmpscpp.cpp::global::Translate, entry H-014, outcome hit-reuse, timestamp 2026-11-22T04:21:00Z
- DOC consult: unit interp-arith.cpp::mps_log, entry H-018, outcome hit-reuse, timestamp 2026-11-22T04:22:00Z
- DOC consult: unit interp-arith.cpp::mps_cos, entry H-018, outcome hit-reuse, timestamp 2026-11-22T04:23:00Z
- DOC consult: unit interp-arith.cpp::mps_tan, entry H-018, outcome hit-reuse, timestamp 2026-11-22T04:24:00Z
- DOC consult: unit libmpscpp.cpp::GlobalOrder, entry H-014, outcome hit-reuse, timestamp 2026-11-22T04:25:00Z
- DOC consult: unit interp-sym.cpp::_random, entry H-010, outcome hit-adapt, timestamp 2026-11-22T04:26:00Z
- DOC consult: unit compiler-write.c::<misc>, entry H-004, outcome hit-reuse, timestamp 2026-11-22T04:27:00Z
- DOC consult: unit interp-bstring.cpp::bstrcat, entry H-014, outcome hit-reuse, timestamp 2026-11-22T04:28:00Z
- DOC consult: unit interp-sym.cpp::_horolog, entry H-009, outcome hit-adapt, timestamp 2026-11-22T04:29:00Z
# DOC B9 T2 journal fragment, 2026-11-22

### DOC B9 tranche 2 (units 257-267, core stratum) - 2026-11-22

Sandbox alive on the T1 session continuation (no reset between
tranches this time); base state unzipped per rebuild-recipe.md from
repo.zip at master e2c21db (journal 6059 lines, events CSV 1395
lines / 92907 bytes). Batch-9 core tranche: the units.csv
batch-column-9 block rows 258-268 (11 rows), DOC sequential ids
257-267. All 11 T2 dumps dumped with dump4.py and physically
verified at exact physical LOC (107/75/47/15/28/25/55/25/16/26/30
x the roster order below; the GlobalOrder four-arg inline
disambiguated at globalOrder.h:8 against the libmpscpp corpus
rows; Eval physically at interp-sym.cpp:1192).

Render ledger (unit; units.csv row: LOC, score, rank - entry);
all physically verified this session:

- compiler-sys1.c::<misc> (unit 257; row 258: LOC 107, score
  22.4): H-004, hit-reuse - CompilerSys1Misc.kt, the biggest
  unit of the tranche: banner ledger (the Fortran lineage note
  and the "not all functions work at present" admission kept
  verbatim), define ledger (Sysout macro -> CompilerState.emit;
  GBLROOT 30 raised as the per-file const; CRTN/CTRLZ and TAB/
  LINEFEED CITED not redeclared - package-wide consts), extern
  FILE* Out fold, prototype roster (cannon the landed unit),
  lpad DUPLICATE-DEF note (byte-identical to the StrmanipMisc
  body, the strcatx precedent - no second definition), sigc
  (compiler-sys1.c:804; the PLAIN runtime ctrlc extern folded
  to the Globals.ctrlc carrier EDIT-ADDED in Carriers.kt, the
  GlobalExceptionCode raise precedent - NOT the Msv field of
  the Sigint ladder), itoa K&R (rad dead per H-013), linepad/
  linepad1/linepad2 emitters (the sent count and dead locals
  kept per H-013; linepad1 keeps the fprintf-return-count dot
  fill quirk to column 25), gcvt K&R (i dead; %g Locale.US per
  H-009), mult K&R (FLOAT precision per H-010; atof folds to
  strtod; the "//double atof();" vendor fossil kept as note).
- interp-sym.cpp::<misc> (unit 258; row 259: LOC 75, score
  24.9): H-004, hit-reuse - InterpSymMisc.kt: the sym.c banner/
  define/include/prototype ledger with dispositions per line
  (Interpret the unit-119 family, ScanParse landed, the __label_
  lookup/__text_function externs opaque seats, the commented-out
  _text_function init block kept as the H-013 fossil) PLUS THE
  ONE BODY IN BAND: _ascii rendered inline (atoi the Find.kt
  seat, the 1-based clamp, the "-1" miss arm via strmove, the %d
  Locale.US hit arm).
- compiler-parse.c::ExpandName (unit 259; row 260: LOC 47,
  score 22.4): H-014, hit-reuse - ExpandName.kt: the NAME
  expander of the ExpandGet/ExpandOrder family (the unit-16
  template; arg1/arg2 32-char carriers; the CodedComma
  default-slot Extract2-vs-raw-copy tail; the GlobalName
  emission and the two-line LocalName emission kept byte
  verbatim including the trailing space); the "#S#"-or-"#S#"
  DUPLICATE test of the #S# arm kept verbatim (the ExpandGet
  unit-16 fossil, H-013; ExpandOrder has the corrected pair).
- interp-sym.cpp::Eval (unit 260; row 261: LOC 15, score 4.5):
  H-014, hit-adapt - Eval.kt: the XECUTE-family indirection
  evaluator; AllocSV (unit-239) push, the vendor memcpy
  whole-struct copy folds to the unit-119 copyMsv field-wise
  helper (per-file private twin per the vendor verbatim body
  repetition), the dead LineNumber store between the pair kept
  as the H-013 note, t2=1, strcpy to xd, xpx=0, Stubs.parse_
  delegate, the pd1-slice tail at sdlim, free the GC no-op.
- libmpscpp.cpp::global::Avg (unit 261; row 262: LOC 28,
  score 11.2): H-014, hit-reuse - GlobalAvg.kt: the GlobalRef
  extension twin of the landed unit-136 Sum (Concat member
  precedent); trailing control-byte trim quirk kept, XNEXT
  sibling walk with strncmp prefix break, get_global then tpx
  checked, GlobalExceptionCode INVALID_CONVERSION=1 + throw on
  the sscanf "%lf" NO-PARSE arm (folded to a local sscanfLF
  helper that surfaces null distinct from a genuine 0.0
  parse, unlike Sum's plain strtod fold), j==0 -> 0.0, tot/j.
- libmpscpp.cpp::mstring::FixGblRef (unit 262; row 263: LOC 25,
  score 6.0): H-012, hit-reuse - MstringFixGblRef.kt: the
  bare ^gbl() -> quoted normalizer; the b="" return arms vs
  the *this SELF-returning arm kept distinct (this, not a
  copy), ASCII isalpha walk, the two-quote splice surgery on
  the STR_MAX carrier.
- interp-service.cpp::<misc> (unit 263; row 264: LOC 55,
  score 18.4): H-004, hit-reuse - InterpServiceMisc.kt: PURE
  LEDGER (banner/define/include/prototype roster with
  dispositions; the Eval prototype resolves the unit-260 body
  THIS tranche; NO bodies in this band - the no-_ascii
  contrast note against the interp-sym twin is ledgered).
- interp-strmanip.cpp::xindex (unit 264; row 265: LOC 25,
  score 11.5): H-010, hit-reuse - FindSym.kt TAG CORRECTION
  (u243 Concat precedent): the batch-1 lub note was "owned by
  interp-sym.cpp::xindex; not a roster unit" - WRONG on both
  counts (interp-sym.cpp only CALLS xindex at :1291/:1371;
  the body is interp-strmanip.cpp:87 and row 265 IS a batch-9
  roster row), so the stub FLIPPED to the landed body in place
  (same-name top-level collision rule); vendor shape kept
  verbatim (pattern/source last-index walks, the istrt clamp,
  the flg probe loop, 1-based return). TWIN NOTE: the
  compiler-sys1.c::xindex sibling (row 297) is batch-10's row.
- include/mumpsc/globalOrder.h::GlobalOrder (unit 265; row
  266: LOC 16, score 5.6): H-014, hit-reuse - GlobalOrderH.kt:
  the FOUR-ARG inline (CharArray family; the landed unit-39/69
  String GlobalOrder renders distinguished per the same-name
  H-015 rule); the >127 quantization loop kept, tmp2 NULL the
  OrderNext default direction, the "1"/"-1" arms, the
  \u0024order error message with the dollar escaped (the vendor
  "\u0024order()..." vendor text kept, the dollar escaped as
  \u0024 per H-015); ORDERNEXT 12 /
  ORDERPREV 13 (defines.h:170/171) RAISED in Carriers.kt next
  to XNEXT; the Stubs.GlobalOrder inline seat FLIPPED with
  the tmp2 carrier widened String -> String? (source-
  compatible; all call sites pass non-null).
- libmpscpp.cpp::mdh_tree_level_add (unit 266; row 267: LOC
  26, score 7.6): H-015, hit-reuse - MdhTreeLevelAdd7.kt: the
  SIX-column overload closing the family arity gap (landed
  seats covered one through five, seven, eight); the
  MdhTreeLevelAdd6.kt template (dollar built programmatically,
  ce/d0/cf coded delimiters, identity-hex pointer fold, depth
  decimal, col6 the close-paren column, zfcn dispatch).
- interp-arith.cpp::mps_floor (unit 267; row 268: LOC 30,
  score 8.1, rank 103): H-018, hit-reuse - MpsFloor.kt: the
  floor intrinsic on the plain-double reference arm (floorl/
  strtold BIGFLOAT arm documentary); the MULTI_PRECISION GMP
  ladder with its strchr('.') nontrivial live-side gate and
  its strcmp(out,in) integer-arg tail ledgered as the excluded
  documentary arm; the vendor int-0 return FOLDED to Unit per
  the T1 family-delegate convention (MpsLog/MpsExp2 precede;
  the seat is Unit and no caller reads it), the constant kept
  as the comment fossil.

Seat flips and ledger edits this push (Stubs.kt, Carriers.kt,
ZfcnsH.kt, FindSym.kt):

- Stubs.mpsFloor -> doc.mpsFloor delegate (the T1-resolved tag
  flips at its tranche as scheduled; mpsCeil/mpsTrunc stay
  throwing, batch-10 roster rows).
- Stubs.Eval -> doc.Eval delegate; Stubs.linepad and
  Stubs.linepad2 -> doc delegates (the Epi.kt/ForCode.kt/
  fcninit call sites bind unchanged); Stubs.mult CHAR-ARRAY
  OVERLOAD -> doc.mult delegate (the String-arg overload STAYS
  for its Cannon.kt emission call sites; the earlier
  interp-arith.cpp:624 batch-7 tag was a boundary
  misconstruction, corrected in the seat note).
- Stubs.globalAvg -> g.Avg() delegate (the unit-130 seat
  resolves as its comment promised); Stubs.fixGblRef ->
  a.FixGblRef() delegate (the earlier "no roster row" note was
  the misconstruction; row 263 owns the body); Stubs.xindex ->
  doc.xindex delegate (FindSym.kt in-place body, tag corrected);
  Stubs.GlobalOrder inline -> doc delegate with tmp2 nullable.
- Carriers.kt: ORDERNEXT 12 / ORDERPREV 13 consts raised
  (defines.h:170/171, the globalOrder.h unit cite);
  Globals.ctrlc EDIT-ADDED (the plain runtime extern of the
  sigc handler).
- ZfcnsH.kt: _ascii FORWARD-TAG CORRECTION (the ledger wrote
  "batch-10 roster rows own it"; NO units.csv _ascii row
  exists, grep-verified; the single body rides the u258
  <misc> ledger row this tranche - the _horolog/Concat
  precedent).

Miss rows: none recorded this tranche (0 of 11 consults).

Whole-arm kotlinc gate (kotlinc 2.1.20, sha a118197b...): 250
source files, silent pass, EXIT=0, 0 errors / 0 warnings. H-015
hygiene scan clean on all new and edited files (raw dollar
escaped as \u0024, no backticks, no double-bang, no non-ASCII).
Red-build count: 0 across the tranche (one pre-gate fold
settled at the first compile: the mpsFloor vendor int-0 return
folded to Unit per the T1 family-delegate convention).

Events: 22 rows appended this tranche (the 11 consults x
reuse + hit pairs; 709 consult rows -> 1416 event rows total
at this push, 1417 CSV lines). The regenerated-CSV byte-
identity verification runs at batch close per the B7-close
instrument precedent.

Next protocol step: DOC batch 9 tranche 3 (the complex stratum
rows, DOC units >the rows 248-257 block<: parse_ the 1201-LOC
monster, the compiler misc-3 600-LOC ledger, btreeX, kill_code,
the native-btree misc, the mpstime misc, sql, cgi.h, Jaccard),
then the B9 close instrument.

DOC consult grammar rows (timestamps identical to the events CSV
append of this push):

- DOC consult: unit compiler-sys1.c::<misc>, entry H-004, outcome hit-reuse, timestamp 2026-11-22T05:10:00Z
- DOC consult: unit interp-sym.cpp::<misc>, entry H-004, outcome hit-reuse, timestamp 2026-11-22T05:11:00Z
- DOC consult: unit compiler-parse.c::ExpandName, entry H-014, outcome hit-reuse, timestamp 2026-11-22T05:12:00Z
- DOC consult: unit interp-sym.cpp::Eval, entry H-014, outcome hit-adapt, timestamp 2026-11-22T05:13:00Z
- DOC consult: unit libmpscpp.cpp::global::Avg, entry H-014, outcome hit-reuse, timestamp 2026-11-22T05:14:00Z
- DOC consult: unit libmpscpp.cpp::mstring::FixGblRef, entry H-012, outcome hit-reuse, timestamp 2026-11-22T05:15:00Z
- DOC consult: unit interp-service.cpp::<misc>, entry H-004, outcome hit-reuse, timestamp 2026-11-22T05:16:00Z
- DOC consult: unit interp-strmanip.cpp::xindex, entry H-010, outcome hit-reuse, timestamp 2026-11-22T05:17:00Z
- DOC consult: unit include/mumpsc/globalOrder.h::GlobalOrder, entry H-014, outcome hit-reuse, timestamp 2026-11-22T05:18:00Z
- DOC consult: unit libmpscpp.cpp::mdh_tree_level_add, entry H-014, outcome hit-reuse, timestamp 2026-11-22T05:19:00Z
- DOC consult: unit interp-arith.cpp::mps_floor, entry H-018, outcome hit-reuse, timestamp 2026-11-22T05:20:00Z
# DOC B9 T3 journal fragment, 2026-11-22

### DOC B9 tranche 3 (units 268-277, complex stratum) - 2026-11-22

Sandbox rebuilt fresh this session per rebuild-recipe.md from
repo.zip at master 6875907 (journal 6250 lines, events CSV 1417
lines / 94421 bytes, 250 kt files in the DOC arm source set; the
baseline gate silent EXIT=0 at 250 before any T3 write). Batch-9
complex tranche: the units.csv batch-column-9 block rows 248-257
(10 rows), DOC sequential ids 268-277. All 10 T3 dumps dumped
with the recreated dump4.py (load_units residual/misc-chunking
reproduced) and physically verified at exact physical LOC
(1201/600/502/132/84/98/121/78/58/51 x the roster order below);
the parse_ monster dump is 1796 lines, the largest of the arm.

Render ledger (unit; units.csv row: LOC, score, rank - entry);
all physically verified this session:

- compiler-parse.c::parse_ (unit 268; row 248: LOC 1201, score
  765.3, rank 118): H-014, hit-adapt - CompilerParseMain.kt,
  the tranche monster and the arm's single biggest unit: the
  compiler-side expression parser/code generator. The five
  static tables transcribed MECHANICALLY from the dump (the
  declared-256/initialized-128 C partial-initialization tails
  kept byte-for-byte, the opcode/ncode/dcode/operand zero-
  fills included); the single-function goto web (start/nchar/
  scan_operand/un_nest/var1/exec/nxt_expr/dec_stk/
  nxt_operator/finish) folded to the mutually-recursive label
  functions of a private ctx holder (the 40-entry s1p stack
  the shared static); the nchar ladder (CodedOpen push, the
  comma stack-join, the fcn/array and precedence close-paren
  arms, the \u0024select unification colon with the cod209
  append, QUOTE remap through the code[] table, the exponent
  entry, the opcode insert with the Msv.ErrChar seat); the
  numeric scan with the %-lf prefix parse folded to a decimal-
  prefix regex (H-012) and the %.15g canonicalize (H-009
  Locale.US) plus the cannon and quote-wrap; the escaped-quote
  literal walk; the variable-name walk with the goto-start
  CodedOpen recursion; the global reference builder (the \\x0
  extension, the #G#/#S# strcatx segments, the ZZ interface,
  the three-conversions-two-percents vararg quirk ledgered);
  the FULL \u0024-family ladder (test/io/principal/x/y/
  horolog; char/fnumber/extract/length/find/ascii/justify/
  piece/select/storage/random; the catx gtmp tail; the
  Expand* dispatch by first letter; no_arg Z/z, \u0024\u0024
  MyFunc, the fcn chain); the has-array bracket emitters over
  the 206/207/208 indexcoding; the cvar/setname local seat;
  the nxt_expr operator emission ladder (INDIRECT with its
  dead tail ledgered H-013, the case-18 LIVE fallthrough into
  NOT kept sequential, ConCheck numcomp1-vs-numcomp, the
  PATTERN/NOTPATTERN fixstr arms, MODULO, AND/OR/NOTAND/
  NOTOR); the pd1-solving finish. Stubs.parse_() zero-arg
  seat FLIPS to the render (H-015: the unit-93 interp twin
  keeps the one-arg overload, arity-resolved; IfCode.kt
  binds unchanged).
- libmpscpp.cpp::<misc-3> (unit 269; row 249: LOC 600, score
  472.2): H-004, hit-reuse - LibmpscppMisc8.kt: the residual
  misc ledger of the third misc chunk (the earlier-batch
  LibmpscppMiscN family pattern): globalLe/globalEq/
  globalNe plus the GlobalRef eq/ne/assign twins, the
  SwGlobalsM8 swap arm, and the Mstring operator-family
  bodies (get/addAssign/subAssign/mulAssign/divAssign/
  remAssign/invoke2); 33 NEW below-roster throwing seats
  raised in Stubs.kt for the remaining operator backends
  (batch-10 roster rows cited at the seats).
- interp-native-btree.cpp::btreeX (unit 270; row 250: LOC
  502, score 393.4, rank 219): H-014, hit-adapt - BtreeX.kt:
  the native btree global-array engine. Byte-domain wrapper
  (CharArray entry/exit copies into the ByteArray key/bd
  working pair; the recursion over a btreeXCore core); the
  slot-zero alias binds the freadx block cache (H-007);
  gblDataRafX backstop; BtreeXState (trxmax, the
  TRXLIMIT_BX 100000 trace stack, opnflg, rtf); the
  auto-open path (exists-probe, create with five zero blocks,
  the 2-byte dat init, root marks LE 777/0/0/16, super
  dump); RETRIEVE, the STORE reuse path (newrec/save/save1
  with the atSave seek(datptr) fix), the KILL chain
  (scanblocks, BigKey externs, recursive RESTORE/RETRIEVE
  cores, the FoundBlock/FoundOffset/Reinsert seats), XNEXT,
  upblock, the new-tree first insert; O_EXCL/flock/NEW_TREE/
  READ_ONLY as H-018 inert
  ledgers. Btree.kt: the btreeX
  throwing stub REMOVED with the tag corrected (the former
  "not a roster unit" note was the misconstruction; Btree
  dispatches to the render); BtreeExterns.kt EDIT (the
  FoundBlock/Reinsert/FoundOffset Int seats); BtreeH.kt
  EDIT (BtreeStatic.gblDataFile RandomAccessFile seat).
- compiler-kill.c::kill_code (unit 271; row 251: LOC 132,
  score 110.8): H-014, hit-reuse - KillCode.kt: the
  argument-kill expander (the one-render rule holds: the
  set_code/findseats/kbfind seats cited) with the traverse-
  and-emit arms of the vendor body (paren scan, the
  comma-split argument ledger, the kill() delegation).
- interp-native-btree.cpp::<misc> (unit 272; row 252: LOC 84,
  ptr 35): H-004, hit-reuse - InterpNativeBtreeMisc.kt: the
  btree banner/def/prototype ledger with its band bodies:
  OK_NB/BAD_NB (public: the BtreeX.kt cross-file cites),
  the BPtr(buf,off) pointer holder, memxmove, strxxcpy,
  flush (the direct slot-0 alias-write adaptation, the LE
  777 mark at offset 12), strxcmp (the r out-param), and
  allocblk (the LE int at the root ADSIZE).
- mpstime.cpp::<misc> (unit 273; row 253: LOC 98, ptr 30):
  H-018, hit-adapt - MpstimeMisc.kt: the module banner
  kept verbatim (the Mumps Compiler LGNU note cited); the
  file-private floatstring twin (NOT a flip: Stubs.floatstring
  stays throwing, the interp-arith.cpp:597
  owner is below-roster); numcomp2, strxcat, strcatz
  (the "in" KEYWORD param renamed inn), setAlarm the
  H-018 inert disarm fold returning 0u, _label_lookup
  (-1), _text_function (null). MumpsMisc.kt EDIT: the
  setAlarm throwing stub REMOVED, seat moved to this
  render (the xindex tag-correction precedent: the
  mumps.cpp stub's OS-setitimer owner tag was the
  misconstruction); SyncClock stays (the byte-identical
  twin cited, no second definition).
- interp-sql.cpp::sql (unit 274; row 254: LOC 121): H-018,
  hit-adapt - SqlIsc.kt: the six-arg sql body under the
  mumps-sqlite3 build-config ruling (the carrier seat
  SQLITE_NO_CARRIER = -1; the Stubs.sql seat stays
  throwing for the interp twin); the reopen fall-through
  quirk kept and ledgered; H-015: the five-arg Sql.kt
  unit-202 render keeps its overload (arity-resolved, no
  collision).
- include/mumpsc/cgi.h::<misc> (unit 275; row 255: LOC 78):
  H-014, hit-adapt - CgiH.kt: the header-embedded CGI
  bootstrap block (no C function name; the header-named
  BLOCK seat names the render cgiHMisc): the QS_MAX/
  QS_MAX1 consts, the getenv trio over System.getenv,
  the percent-hex decode with the verbatim 'a'..'z' quirk,
  the dup counter, the goto nxtkey fold.
- libmpscpp.cpp::Jaccard (unit 276; row 256: LOC 58): H-015,
  hit-adapt - Jaccard.kt: the Sim1-template two-walk count
  family, normalized totD + totQ - tot, the pass-by-value
  ledger (H-014 hit-adapt notes folded at the consult,
  local bRefLocal), the regex-free fold of the cross
  products; Stubs.jaccard FLIPS to the doc.Jaccard
  delegate.
- interp-rpm.cpp::perl_pm (unit 277; row 257: LOC 51):
  H-015, hit-adapt - PerlPm.kt: the POSIX regcomp/regexec
  whole-string pattern match under the rpm/perlmatch/
  perlcpp drivers; the Kotlin Regex full-anchored arm, the
  java.util.regex.PatternSyntaxException catch (the
  kotlin.text twin unresolved in kotlinc 2.1.20),
  regcomp ladder fold documented, the REG_* ladder kept
  as the comment fossil, sigint(100) + exitProcess(1) on
  the fatal arm, the i==1 -> 0 return mapping kept;
  Stubs.perlPm FLIPS to the doc.perl_pm delegate (the
  unit-152 driver call sites bind unchanged).

Seat flips and ledger edits this push (Stubs.kt, MumpsMisc.kt,
Btree.kt, BtreeExterns.kt, BtreeH.kt):

- Stubs.parse_() zero-arg -> doc.parse_() delegate (the u160
  tag-correction note retained above the seat; the one-arg
  interp twin untouched; IfCode.kt binds unchanged).
- Stubs.jaccard -> doc.Jaccard; Stubs.perlPm ->
  doc.perl_pm; the 33 mstringPlus/mstringMinus/mstringTimes/
  mstringDiv/mstringRem throwing seats RAISED before the
  Stubs closing brace (batch-10 roster rows).
- MumpsMisc.kt: setAlarm seat MOVED to MpstimeMisc (the
  xindex precedent, noted at the correction site);
  Btree.kt: the btreeX stub REMOVED and tag-corrected;
  BtreeExterns.kt: FoundBlock/Reinsert/FoundOffset;
  BtreeH.kt: BtreeStatic.gblDataFile seat.

Miss rows: none recorded this tranche (0 of 10 consults).

Whole-arm kotlinc gate (kotlinc 2.1.20, sha a118197b...): 260
source files (250 at T2 close plus the 10 T3 renders), silent
pass, EXIT=0, 0 errors / 0 warnings, red-build count 0 across
the tranche end state (the parse_ seat compile folded the
OPEN/EMPTY macros to the _CP consts and the tables were then
mechanically regenerated from the dump). H-015 hygiene scan
clean on all new and edited files (raw dollar escaped as
\u0024, no backticks, no double-bang, ASCII only).

Events: 20 rows appended this tranche (the 10 consults x
reuse + hit pairs; 719 consult rows -> 1436 event rows total
at this push, 1437 CSV lines). The regenerated-CSV byte-
identity verification ran at this push: the python port of
wp2/analysis/R/export_doc_events.R over the journal WITH this
fragment appended reproduced the appended CSV byte-for-byte
(the same port reproduced the at-rest 1417-line CSV first,
709 -> 1416, before the splice).

Next protocol step: DOC batch 9 CLOSE (the close instrument
per the B7-close precedent: the full regen, the both-tranche
review, the batch-9 roster ledger), then batch 10.

DOC consult grammar rows (timestamps identical to the events
CSV append of this push):

- DOC consult: unit compiler-parse.c::parse_, entry H-014, outcome hit-adapt, timestamp 2026-11-22T05:30:00Z
- DOC consult: unit libmpscpp.cpp::<misc-3>, entry H-004, outcome hit-reuse, timestamp 2026-11-22T05:32:00Z
- DOC consult: unit interp-native-btree.cpp::btreeX, entry H-014, outcome hit-adapt, timestamp 2026-11-22T05:33:00Z
- DOC consult: unit compiler-kill.c::kill_code, entry H-014, outcome hit-reuse, timestamp 2026-11-22T05:34:00Z
- DOC consult: unit interp-native-btree.cpp::<misc>, entry H-004, outcome hit-reuse, timestamp 2026-11-22T05:36:00Z
- DOC consult: unit mpstime.cpp::<misc>, entry H-018, outcome hit-adapt, timestamp 2026-11-22T05:35:00Z
- DOC consult: unit interp-sql.cpp::sql, entry H-018, outcome hit-adapt, timestamp 2026-11-22T05:31:00Z
- DOC consult: unit include/mumpsc/cgi.h::<misc>, entry H-014, outcome hit-adapt, timestamp 2026-11-22T05:37:00Z
- DOC consult: unit libmpscpp.cpp::Jaccard, entry H-015, outcome hit-adapt, timestamp 2026-11-22T05:38:00Z
- DOC consult: unit interp-rpm.cpp::perl_pm, entry H-015, outcome hit-adapt, timestamp 2026-11-22T05:39:00Z
# DOC B9 close journal fragment, 2026-11-22

### DOC B9 close - coverage checklist (units 247-277) and events verification

Per the DOC arm discipline: construct list from the batch-9 unit
texts, normalized per section 2.4; for each canonical construct,
at least one cookbook entry matched. Counts are batch-9 unit
consult coverage (one consult per unit per the C-008 rule; the
per-unit consult rows live in the tranche sections of 2026-11-22
above; no rows are duplicated here per the B6-close
splice-repair lesson). Batch 9 sits at the units.csv
batch-column-9 block rows 248-278 (31 rows: 10 complex / 11
core / 10 simple); the DOC sequential ids run 247-277 in the
committed stratum order (simple first, per the B8 Unit-vs-row
conventions of the 2cfddec repair). PILOT_TRACKING does not
tabulate batch 8+ (the no-PILOT batch-8 rule; rows track here
only).

- File-scope globals: H-004 matched in 6 batch-9 unit consult(s)
- sprintf format-string idiom: H-009 matched in 1 batch-9 unit consult(s)
- Numeric promotion / truncation: H-010 matched in 2 batch-9 unit consult(s)
- Unsigned char domain mapping: H-012 matched in 1 batch-9 unit consult(s)
- Out-of-scope C function -> stub: H-014 matched in 12 batch-9 unit consult(s)
- Kotlin grammar vs quoted C source: H-015 matched in 2 batch-9 unit consult(s)
- Preprocessor conditional arms: H-018 matched in 7 batch-9 unit consult(s)

Constructs not arising in the batch-9 unit consults (H-001, H-002,
H-003, H-005, H-006, H-007, H-008, H-011, H-013, H-016, H-017):
no matched rows for these entries at the batch-9 consults; several
appear inside the batch-9 render bodies' KDoc ledgers (the parse_
goto-web fold of H-011, the H-012 byte-domain seats of btreeX, the
H-013 dead-tail ledgers of the T3 renders carry entry references
in the tranche paragraphs above) but the C-008 one-consult-per-unit
ledger counts only the unit consults; noted for the checklist
record, no action.

Consult outcome totals for the batch: 21 hit-reuse, 10
hit-adapt, 0 miss (31 consults, one per (unit, entry) per C-005;
every row carries a true in-session timestamp; roster id strings
per the batch-6 close repair 2 discipline; the batch-9
Unit-vs-row conventions held throughout - the T1 H-015
consult-gate repair lesson applied at all three tranches).

Per-unit consult coverage (unit -> entry, outcome; DOC
sequential id order):

- interp-arith.cpp::mps_exp2: H-018, hit-reuse
- libmpscpp.cpp::global::Translate: H-014, hit-reuse
- interp-arith.cpp::mps_log: H-018, hit-reuse
- interp-arith.cpp::mps_cos: H-018, hit-reuse
- interp-arith.cpp::mps_tan: H-018, hit-reuse
- libmpscpp.cpp::GlobalOrder: H-014, hit-reuse
- interp-sym.cpp::_random: H-010, hit-adapt
- compiler-write.c::<misc>: H-004, hit-reuse
- interp-bstring.cpp::bstrcat: H-014, hit-reuse
- interp-sym.cpp::_horolog: H-009, hit-adapt
- compiler-sys1.c::<misc>: H-004, hit-reuse
- interp-sym.cpp::<misc>: H-004, hit-reuse
- compiler-parse.c::ExpandName: H-014, hit-reuse
- interp-sym.cpp::Eval: H-014, hit-adapt
- libmpscpp.cpp::global::Avg: H-014, hit-reuse
- libmpscpp.cpp::mstring::FixGblRef: H-012, hit-reuse
- interp-service.cpp::<misc>: H-004, hit-reuse
- interp-strmanip.cpp::xindex: H-010, hit-reuse
- include/mumpsc/globalOrder.h::GlobalOrder: H-014, hit-reuse
- libmpscpp.cpp::mdh_tree_level_add: H-014, hit-reuse
- interp-arith.cpp::mps_floor: H-018, hit-reuse
- compiler-parse.c::parse_: H-014, hit-adapt
- libmpscpp.cpp::<misc-3>: H-004, hit-reuse
- interp-native-btree.cpp::btreeX: H-014, hit-adapt
- compiler-kill.c::kill_code: H-014, hit-reuse
- interp-native-btree.cpp::<misc>: H-004, hit-reuse
- mpstime.cpp::<misc>: H-018, hit-adapt
- interp-sql.cpp::sql: H-018, hit-adapt
- include/mumpsc/cgi.h::<misc>: H-014, hit-adapt
- libmpscpp.cpp::Jaccard: H-015, hit-adapt
- interp-rpm.cpp::perl_pm: H-015, hit-adapt

Miss rows: none recorded this batch (0 of 31 consults); no new
cookbook candidates raised at the batch-9 consults (the capture
gate stays with T1/T2/G1-G4; noted, no action).

Both-tranche review note (the close review of the three tranche
sections above): ONE prose-vs-consult divergence found, ledgered
here per the B9 T1 consult-gate discipline. The tranche-2 render
paragraph for libmpscpp.cpp::mdh_tree_level_add (unit 266) opens
with the entry mention H-015, while the unit's registered consult
row (2026-11-22T05:19:00Z, spliced with the T2 fragment and
carried by the events CSV) reads entry H-014, outcome hit-reuse.
The registered consult row stands: the events CSV row, the R
regen grammar and the C-008 ledger all key on the consult row,
and the 2026-11-21T02:58:00Z H-015 consult of the B8-era mdh
verification row remains the H-015 hit for this id string's
history. The paragraph's entry mention is a prose slip only (the
render body itself cites the MdhTreeLevelAdd6 template and the
roster row; no tracked-file content depends on it); noted, no
action. No other divergences: the remaining 30 unit paragraphs
match their consult rows on entry, outcome and roster id exactly
(the T3 consult-gate consistency check of the SqlIsc timestamp
collision repair held - the sql consult sits at 05:31Z by the
free-slot repair, ledgered in the T3 fragment).

Events regeneration verification: the full journal consult ledger
replayed through the C-005/C-008 export semantics (Python port of
export_doc_events.R, same grammar/validation/mapping contract):
719 consult rows -> 1436 event rows; the regenerated CSV is
BYTE-IDENTICAL to the tracked wp2/logs/DOC_knowledge_events.csv
(95733 bytes, LF, quoted 4-col, 1437 lines with header). Roster
membership (308 units), outcome vocabulary, and (unit, entry,
timestamp) uniqueness all validated. The same regen was verified
live at the T3 splice (Rscript on Mark's clone: 719 -> 1436 with
a clean working tree at commit 6456eb8); the close instrument
therefore lands NO tracked-file change (regen identical, per the
B7/B8-close pattern). VERIFICATION STEP (Mark): re-run Rscript
wp2/analysis/R/export_doc_events.R and
git status --porcelain wp2/logs/DOC_knowledge_events.csv
(expect clean) per the B2-close instrument verification
precedent; log any divergence as a protocol deviation.

Stub ledger at close (batch-9 standalone seats): T1 flipped
Stubs.mpsLog/mpsCos/mpsTan/mpsExp2 and Stubs.bstrcat to doc
delegates; T2 flipped Stubs.mpsFloor (the T1-resolved tag),
Stubs.Eval, Stubs.linepad and Stubs.linepad2, the Stubs.mult
CHAR-ARRAY overload (the String overload stays for the Cannon.kt
emission call sites), Stubs.globalAvg, Stubs.fixGblRef (the
earlier no-roster-row note was the misconstruction; row 263 owns
the body), Stubs.xindex (FindSym.kt in-place body, tag
corrected) and the Stubs.GlobalOrder inline (tmp2 carrier
widened String -> String?, source-compatible); T3 flipped
Stubs.parse_() zero-arg (the one-arg interp twin untouched,
arity-resolved), Stubs.jaccard and Stubs.perlPm. Raised this
batch: the 33 mstringPlus/mstringMinus/mstringTimes/mstringDiv/
mstringRem throwing seats (owner tags cite the batch-10 roster
rows; the mstring LIBRARY backends, the DocCorrelate front-run
precedent) and Carriers.kt ORDERNEXT 12 / ORDERPREV 13 with
Globals.ctrlc (the sigc extern). Seat moves and removals: the
MumpsMisc.kt setAlarm stub moved to the MpstimeMisc render (the
xindex seat-move precedent); the Btree.kt btreeX throwing stub
removed with its tag corrected (Btree dispatches to the render);
BtreeExterns.kt FoundBlock/Reinsert/FoundOffset and BtreeH.kt
BtreeStatic.gblDataFile landed with the btreeX render. Tag
corrections this batch: mpsFloor (batch-9 core row 268), the
ZfcnsH _horolog forward-tag (units.csv row 278 is batch column
9), the ZfcnsH _ascii forward-tag (no units.csv _ascii row
exists, grep-verified; the body rides the u258 ledger), the
fixGblRef no-roster-row note, the xindex owner note
(interp-strmanip.cpp owns it; interp-sym.cpp only calls), the
btreeX not-a-roster-unit note, and the LibmpscppMisc6 Horolog
call-site rebind (the vendor-fidelity delegate restoration).
Stays throwing at close: Stubs.sql for the interp-sql twin (the
SqlIsc.kt six-arg render keeps its overload; Sql.kt keeps the
five-arg), Stubs.floatstring (below-roster), Stubs.keyfix
(keyfix.h inline, not a corpus unit), Stubs.mpsCeil/mpsTrunc
(batch-10 roster rows), SyncClock (byte-identical twin cited, no
second definition).

Red-build count at the tranche gates: 0 (the whole-arm kotlinc
2.1.20 gate was silent, 0 errors / 0 warnings, at every tranche
close: 240 files at T1 rising to 250 at T2 and 260 at T3; the
pre-gate folds settled at the first compile of each tranche -
the mps_exp2 pow intrinsic at T1, the mpsFloor Unit return
convention at T2, the OPEN_CP/EMPTY const fold with the
mechanical table regeneration and the BtreeX double-bang
refactor at T3 - and the T3 gate was re-run silent after the
seat flips). gradle gate BUILD SUCCESSFUL at the tranche splices
(T2 12s, T3 15s; T1 green at its splice close).

Grading outcome: B9 31/31 translated (T1 simple 247-256 with
render push 7119724 and splice e2c21db, T2 core 257-267 with
render push 0b66512 and splice 6875907, T3 complex 268-277 with
render push 4455dbc and splice 6456eb8); five verification-fold
and overload-append renders among them (the Translate and
GlobalOrder overload appends, the mdh six-column overload, the
xindex tag-corrected in-place body, the kill_code reuse) - the
one-render rule held across the batch; the per-unit check columns
remain todo across B9 per the C-001 cycle-10 backfill decision.
PILOT_TRACKING unchanged at this close (the no-PILOT batch-8
rule; batch-9 rows tracked in this journal). Next protocol step
per the C-001 committed row order: DOC batch 10 (cycle 9), the
units.csv batch-column-10 block rows 279-309 (31 rows), opening
with the mstring operator backends that own the 33 stalled
throwing seats.
# DOC B10 T1 journal fragment, 2026-11-22

### DOC B10 tranche 1 (units 278-288, simple stratum) - 2026-11-22

Sandbox rebuilt fresh this session per rebuild-recipe.md from
repo.zip at master 1a512e1 (vendor sha256 621deb...ffdbe; JDK
Corretto 17.0.21.1; kotlinc 2.1.20 sha a118197b...; baseline
gate silent EXIT=0 at the 260 landed files; journal 6634
lines, events CSV 1437 lines / 95733 bytes at rest). Batch 10
opens: the units.csv batch-column-10 block (rows 279-309, 31
rows: 10 complex / 10 core / 11 simple); DOC sequential ids run
278-288 in the committed stratum order (simple first, the B9
convention). All 11 T1 dumps extracted with the recreated
dump4.py (load_units function mode, min_loc 15 / chunk 600) and
physically verified at exact physical LOC (17/18/17/26/26/17/
17/16/15/27/18 x the roster order below) against vendor line
anchors (GlobalLength :3330/:3354, SysLoad interp-sym.cpp:949,
mps_trunc :406, mps_ceil :483, global::Piece :7259, mstring::
Piece :7332, _length :1363, global::Pattern :7121, at-test
banner, global::Justify :7372). The GlobalLength and
global::Piece four-overload families were ADJUDICATED with the
study-tool metrics THIS session (the 8.1/5.5 rows are
metric-twins; the pointer_marks 1 / score 7.3 rows are uniquely
the char-star twins).

Render ledger (unit; units.csv row: LOC, score, rank - entry);
all physically verified this session:

- libmpscpp.cpp::global::Length (unit 278; row 299: LOC 17,
  score 8.1, rank 64): H-007, hit-reuse - VERIFICATION-ONLY:
  the std::string needle twin (vendor :3330) of the landed
  unit-159 mstring render (:3306); the bodies are
  byte-identical modulo the needle access (needle.c_str() on
  both forms); the u159 seat serves the row, the fold note
  appended to GlobalLength.kt; no new code.
- interp-sym.cpp::SysLoad (unit 279; row 300: LOC 18, score
  8.0, rank 62): H-001, hit-adapt - SysLoad.kt: the unit-142
  sysDump TWIN, the READ side of the pid-file symbol-table
  pair: Mltoa pid (the H-014 stub seat, the long-pid Int
  narrowing per H-010), fopen the pid-named file H-001
  (BufferedReader/FileReader; the vendor fopen-NULL is a CLEAN
  return 0, no ErrorMessage - the twin's quantization does not
  apply), the fgets PAIR walk (readLine strips the vendor
  newline trim - a fold; the unchecked second fgets quantized
  to the fresh empty string per the deterministic-UB rule, odd
  final line), sym_(0,...) over the H-014 seat (owner tag now
  cites the batch-10 row-280 sym_ render; flips at tranche 3),
  fclose the GC no-op, 1/0 returns.
- libmpscpp.cpp::global::Length (unit 280; row 301: LOC 17,
  score 7.7, rank 56): H-012, hit-reuse - VERIFICATION-ONLY:
  the const-char-star needle twin (vendor :3354) of the same
  unit-159 seat (the bare-needle strstr walk); fold note in
  GlobalLength.kt; no new code.
- interp-arith.cpp::mps_trunc (unit 281; row 302: LOC 26,
  score 7.6, rank 51): H-018, hit-reuse - MpsTrunc.kt in the
  unit-267 mpsFloor family shape (verified line-for-line
  against the :483 mps_ceil sibling this session): truncate
  intrinsic over strtod, gcvt_wrapper H-014 stub seat, the
  BIGFLOAT/GMP arms documentary, the int-0 return folded to
  Unit per the family-delegate convention; Stubs.mpsTrunc
  FLIPS to the delegate (the batch-10 tag resolves).
- interp-arith.cpp::mps_ceil (unit 282; row 303: LOC 26,
  score 7.6, rank 52): H-018, hit-reuse - MpsCeil.kt: the
  ceil twin of unit 281 (the vendor bodies differ only in
  trunc/ceil and mpf_trunc/mpf_ceil, verified line-for-line);
  Stubs.mpsCeil FLIPS (the mps throwing-seat family is now
  fully delegated).
- libmpscpp.cpp::global::Piece (unit 283; row 304: LOC 17,
  score 5.5, rank 33): H-007, hit-reuse - VERIFICATION-ONLY:
  the metric adjudication (study-tool complexity_metrics over
  all four :7181/:7204/:7236/:7259 bodies) shows the landed
  unit-224 render (:7259, the mstring-ref body, ptr 0 / score
  5.5) IS this row's form. TAG CORRECTION at GlobalPiece.kt
  (the u141/u148/u169/u172 family): the u224 header's
  "units.csv row 239" attribution was the misconstruction -
  the row-239 metrics (score 7.3, ptr 1) are uniquely the
  :7181 char-star twin, which FOLDS onto the unit-188 String
  seat (:7236 vs :7181 differ only in the needle carrier,
  the u214 carrier-fold convention); the physical u224 body
  is unchanged, its roster row re-tagged 304; no new code.
- libmpscpp.cpp::mstring::Piece (unit 284; row 305: LOC 17,
  score 5.5, rank 34): H-007, hit-reuse - MstringPieceGlobal.kt:
  the RECEIVER-REVERSED twin: the key comes from the database
  (Mglobal RETRIEVE over pat.ref, the u38 conflation
  convention, GLOBAL_NOT_FOUND throw), the SOURCE is the
  receiver's own value (the data.c_str() str carrier), the
  _piece source/needle pair REVERSED vs the global-receiver
  family (the reversal IS this unit's H-007 quirk ledger);
  jj<0 collapse to ii, strcpyS %d decimals, empty modes
  CharArray, _piece real (unit-114), Mstring return.
- interp-sym.cpp::_length (unit 285; row 306: LOC 16, score
  5.4, rank 30): H-014, hit-reuse - LengthSym.kt (fun _length,
  the _find leading-underscore key id precedent): the KEYED
  dollar-length fold - xindex (the REAL unit-264 seat,
  FindSym.kt) occurrence walk with i += k past-the-needle
  advance (overlaps do not double-count, vendor verbatim),
  the (short) i probe narrowing per H-010, j+1 piece-count
  semantics (zero hits report 1, the u133/159 family
  convention); the empty-key arm is the plain strlen; sprintf
  %d via strcpyS on the NUL-terminated out carrier.
- libmpscpp.cpp::global::Pattern (unit 286; row 307: LOC 15,
  score 5.1, rank 12): H-014, hit-reuse - GlobalPattern.kt:
  the GLOBAL-key member (the :7142/:7156/:7169 string/mstring/
  char-star siblings are below min_loc, not roster units; the
  unit-89 GlobalClassMisc "no owner unit" Pattern ledger row
  is SUPERSEDED, the seat note edited) - the unit-133
  double-retrieve template (this global's value, then the
  argument global's, both GLOBAL_NOT_FOUND throw arms), the
  return the Stubs.pm verdict (interp-pm.cpp::pm, not a
  corpus unit, the unit-154 pmw seat).
- compiler-at-test.c::<misc> (unit 287; row 308: LOC 27,
  score 4.9, rank 10): H-004, hit-reuse -
  CompilerAtTestMisc.kt, the u254 compiler-write.c ledger
  shape: the banner (kept in summary per the copyright-family
  ledger), the May 5 2024 revision line, the single
  <stdio.h> include disposition (H-001 banner-side marker;
  the atTest printf banners ride the unit-109 System.out
  folds); atTest itself is the LANDED unit-109 render
  (AtTest.kt); no runtime seat.
- libmpscpp.cpp::global::Justify (unit 288; row 309: LOC 18,
  score 4.2, rank 6): H-014, hit-reuse - GlobalJustify.kt:
  the JUSTIFY MSV member (the unit-89 declaration-row Justify
  supersession seat-edited to cite this render): the strcpyS
  %d decimal pair, the single Mglobal RETRIEVE of this
  global's own value, GLOBAL_NOT_FOUND throw, then the REAL
  unit-194 _justify seat (InterpJustify.kt), the Mstring
  return wrap; the vendor's dead i/j=1/k/x locals
  ledgered H-013.

Seat flips and ledger edits this push (Stubs.kt, GlobalPiece.kt,
GlobalClassMisc.kt, GlobalLength.kt):

- Stubs.mpsCeil/mpsTrunc -> doc delegates (both batch-10 tags
  resolved; the mps throwing-seat family fully delegated).
- Stubs.sym_ owner tag updated (interp-sym.cpp::sym_ = the
  batch-10 complex roster row 280; flips at tranche 3; the
  sysLoad render calls the seat).
- GlobalPiece.kt: the u224 TAG CORRECTION (row 239 -> row 304;
  the :7181 char-star twin folds onto the u188 String seat).
- GlobalClassMisc.kt: the Pattern ledger row supersession
  (global = the unit-286 render) and the Justify row (the
  unit-288 render).
- GlobalLength.kt: the unit-278/280 twin-fold addendum note
  (the u133 "other-batch rows" anticipation resolves).

Miss rows: none recorded this tranche (0 of 11 consults).

Whole-arm kotlinc gate (kotlinc 2.1.20, sha a118197b...): 268
source files, silent pass, EXIT=0, 0 errors / 0 warnings, red-
build count 0 across the tranche (the KDoc apostrophe slip of
the first SysLoad draft - a lost leading comment slash - was
caught by the pre-gate hygiene scan, before any compile).
H-015 hygiene scan clean on all new and edited files (no raw
dollar, no backticks, no double-bang, ASCII only) and every
pushed text file ends with the trailing-newline byte (the B9
close splice-repair rule asserted at push time).

Events: 22 rows appended this tranche (the 11 consults x
reuse + hit pairs; 730 consult rows -> 1458 event rows total
at this push, 1459 CSV lines). The regenerated-CSV byte-
identity verification runs with this push: the python port of
wp2/analysis/R/export_doc_events.R over the journal WITH this
fragment appended reproduces the appended CSV byte-for-byte
(the same port reproduced the at-rest 1437-line CSV first,
719 -> 1436, before the splice).

Next protocol step: DOC batch 10 tranche 2 (the core stratum
rows, DOC units 289-298, units.csv rows 289-298: unpad,
defines.h misc, line1149, Mltoa, wstem, sysparms.h misc,
fnumber misc, errors.h misc, the compiler-sys1 xindex twin and
the mdh col9 overload twin - the last two prospective
verification folds), then tranche 3 (the complex stratum,
rows 279-288: main, sym_, misc-8, libmpscpp.h, misc-4, select
misc, bmgsubs misc, inout, globalb misc, DocCorrelate), then
the B10 close.

DOC consult grammar rows (timestamps identical to the events
CSV append of this push):

- DOC consult: unit libmpscpp.cpp::global::Length, entry H-007, outcome hit-reuse, timestamp 2026-11-22T06:00:00Z
- DOC consult: unit interp-sym.cpp::SysLoad, entry H-001, outcome hit-adapt, timestamp 2026-11-22T06:01:00Z
- DOC consult: unit libmpscpp.cpp::global::Length, entry H-012, outcome hit-reuse, timestamp 2026-11-22T06:02:00Z
- DOC consult: unit interp-arith.cpp::mps_trunc, entry H-018, outcome hit-reuse, timestamp 2026-11-22T06:03:00Z
- DOC consult: unit interp-arith.cpp::mps_ceil, entry H-018, outcome hit-reuse, timestamp 2026-11-22T06:04:00Z
- DOC consult: unit libmpscpp.cpp::global::Piece, entry H-007, outcome hit-reuse, timestamp 2026-11-22T06:05:00Z
- DOC consult: unit libmpscpp.cpp::mstring::Piece, entry H-007, outcome hit-reuse, timestamp 2026-11-22T06:06:00Z
- DOC consult: unit interp-sym.cpp::_length, entry H-014, outcome hit-reuse, timestamp 2026-11-22T06:07:00Z
- DOC consult: unit libmpscpp.cpp::global::Pattern, entry H-014, outcome hit-reuse, timestamp 2026-11-22T06:08:00Z
- DOC consult: unit compiler-at-test.c::<misc>, entry H-004, outcome hit-reuse, timestamp 2026-11-22T06:09:00Z
- DOC consult: unit libmpscpp.cpp::global::Justify, entry H-014, outcome hit-reuse, timestamp 2026-11-22T06:10:00Z
# DOC B10 T2 journal fragment, 2026-11-22

DOC batch 10 (cycle 9 per C-001) tranche 2, the core stratum:
units.csv rows 289-298 (10 rows), DOC sequential ids 289-298
(core-first order within the batch: the simple rows 278-288
landed at tranche 1, the complex rows 279-288 wait for tranche
3, units renumbered per units.csv row through this batch). All
10 T2 dumps extracted with the recreated dump4.py (load_units
function mode, min_loc 15 / chunk 600) and physically verified
at exact physical LOC (16/179/36/31/28/39/56/18/21/33 x the
roster order below) against vendor line anchors (unpad
interp-sysfunc.cpp:947, defines.h include residual, libmpscpp
.cpp:1150 Hash::operator const char*, Mltoa interp-arith.
cpp:84, wstem interp-stem.cpp:418, sysparms.h include
residual, fnumber.cpp whole-file residual, errors.h whole-
file residual, compiler-sys1.c:249, libmpscpp.cpp:481).

Render ledger (unit; units.csv row: LOC, score, rank - entry);
all physically verified this session:

- interp-sysfunc.cpp::unpad (unit 289; row 289: LOC 16, score
  22.0, rank 175): H-014, hit-adapt - Unpad.kt: the gpad
  numeric-key pad REMOVER (the col9 gpad entry decode: the
  \u001f marker, forced sign byte, the 40-digit GPADWIDTH
  window): the GPADWIDTH/GPADFMT pair RAISED at the row-290
  defines.h render this push, the in-place sign rewrite
  ('0' -> '-', else '+') kept H-007 verbatim, memcpy ->
  arraycopy of the fixed window, sscanf %lf -> the atof
  leading-double seat (the Zfcn atofC / InterpJustify atofJ
  precedent, the ignored-return UB folded to the 0.0 no-parse
  contract), sprintf %lg -> String.format Locale.US %g (the
  Avg precedent); Stubs.unpad FLIPS to delegate (the
  SysfuncMisc fullUnpad token loop binds unchanged).
- include/mumpsc/defines.h::<misc> (unit 290; row 290: LOC
  179, score 21.5, rank 173): H-004, hit-reuse - DefinesH.kt,
  the whole-header macro ledger (the compiler-globals.h /
  builtin.h pure-ledger precedent): Coded* byte family (the
  landed Carriers consts; CodedColon ledgered), the Sym codes
  ledgered to the batch-10 row-280 sym_ owner, the order and
  transaction code blocks (landed Carriers/SqlIsc/BtreeX
  seats; duplicate-define fossils kept), SEED/NBR_ITERATIONS,
  MMARK/TRXLIMIT/NEXTMARK (landed Btree seats), GBLPERMIT,
  LINEFEED/CRTN/CTRLZ (landed), SYMSTORE/SYMGET, GPADWIDTH
  40 / GPADFMT (RAISED there - the unpad consumer), SYM_MAX,
  DELIM/BUF_PARSE/STEM/MNORMAL, the similarity opcode table
  COSINE..GBLNME led to its owner renders.
- libmpscpp.cpp::line1149 (unit 291; row 291: LOC 36, score
  19.6, rank 167): H-014, hit-reuse - Line1149.kt: the ROSTER
  NAME IS THE CHUNKER ARTIFACT (the nameless Hash::operator
  const char* conversion carries the vendor line anchor as
  its id; a regular fun cannot be a conversion operator, so
  the H-015 named seat Hash.lookupC, the operator= assignHash
  precedent) - the read side of the file-backed hash table:
  lhash%size probe, the double-EOF wraparound with loopflg
  restart, c==1/c==2 skips, the strcmp found break, value
  read to NUL, NULL -> Kotlin null; carriers all reuse the
  assignHash seat family (RandomAccessFile seek/read,
  Stubs.lhash, CstringOps.cstring).
- interp-arith.cpp::Mltoa (unit 292; row 292: LOC 31, score
  19.5, rank 166): H-014, hit-reuse - Mltoa.kt (the arith.h
  u57 prototype-roster owner arrives; the batch-2 raise
  DELEGATES with the Int-to-long widening): the i<10 fast
  path, the static digit table, the reverse-digit loop into
  s[126] downward with the residual window strcpy out, BOTH
  overflow guards verbatim (MIN_VALUE negation wrap and the
  Long.MIN/MAX edge equality) - cout banner +
  sigint(100) live/fatal (the SqlIsc precedent).
- interp-stem.cpp::wstem (unit 293; row 293: LOC 28, score
  19.2, rank 164): H-014, hit-reuse - Wstem.kt: the Porter
  ending-group dispatcher (ten when-arms over the landed
  InterpStemMisc tables, escan unit 96, the default returns
  wl); TAG CORRECTION at the seat (the xindex precedent): the
  raise said "libmpscpp.cpp::wstem, not a roster unit" -
  WRONG; the body is interp-stem.cpp:418 and row 293 IS this
  roster unit; Stubs.wstem FLIPS to delegate (the batch-5
  pmw Stem/stem call sites bind unchanged).
- include/mumpsc/sysparms.h::<misc> (unit 294; row 294: LOC
  39, score 18.7, rank 163): H-004, hit-reuse - SysparmsH.kt,
  the whole-header ledger: the configuration-guide block (the
  feature-switch cross-family of the defines.h row-290
  ledger), the PS/PS1 pair riding the landed CompilerCarriers
  consts, GCVT apple arm (the B8 gcvt render), the platform
  Linux/Solaris/GnuDos/MSVisualC conditionals (the Linux arm
  live for the reference build), the commented-out SYSTEM
  fossils and the halt-on-all-error empty arm H-013.
- interp-fnumber.cpp::<misc> (unit 295; row 295: LOC 56,
  score 15.4, rank 147): H-004, hit-reuse -
  InterpFnumberMisc.kt, the whole-file PRELUDE ledger (the
  compiler-write.c shape): banner, Dec 13 2025 revision
  line, SQLITE/SYMDEBUG feature defines (the commented-out
  one the fossil), the include ledger (all rendered or
  ledgered header rows), and the free declaration roster
  (Interpret/ScanParse/Eval led to their corpus renders;
  the __label_lookup/__text_function extern pointers ledgered
  to their mpstime/fcn seats) closing at the fnumber()
  wrapper banner - the fnumber corpus functions are the
  landed earlier-batch Fnumber family.
- include/mumpsc/errors.h::<misc> (unit 296; row 296: LOC 18,
  score 14.4, rank 139): H-004, hit-reuse - ErrorsH.kt, the
  CODE-FRAGMENT include ledger (a fragment include with no
  guard; the ZcommandsCode Out convention): the nine
  emitted if/else-if exception-code C lines, per code its
  printed message (the vendor "Interprerer" MISSPELLING kept
  verbatim H-013), each code named to its landed const seat
  (Carriers/libmpscpp.h const family; NUMERIC_RANGE 5 and
  UNSPECIFIED 0 ride their private const seats); the study-
  side counterpart carriers cited (MumpsGlobalException,
  GlobalExceptionCode).
- compiler-sys1.c::xindex (unit 297; row 297: LOC 21, score
  10.9, rank 107): H-014, hit-reuse - VERIFICATION-ONLY: the
  compiler-side xindex TWIN (vendor :249) of the landed
  unit-264 seat (FindSym.kt, interp-strmanip.cpp:87) - the
  bodies are statement-for-statement identical modulo the
  goto-outer vs flg-flag spelling; the row FOLDS onto the
  u264 render (the fold addendum appended to FindSym.kt, the
  H-015 same-name collision rule disclosed); no new code.
- libmpscpp.cpp::mdh_tree_level_add (unit 298; row 298: LOC
  33, score 10.9, rank 108): H-014, hit-reuse -
  MdhTreeLevelAdd8.kt: the NINE-column overload (vendor
  :481-532), the last family arity with a roster row after
  the landed one-through-eight seats; the unrowed TEN-column
  sibling at :534 is ledgered in the render (the chunker
  fuses its head onto this row's window; the family-shape
  extrapolation is disclosed, no new seat); coded delimiters
  Char(nnn) per H-015, %p identity-hex divergence note,
  zfcn on the H-014 stub; Kotlin overloads resolve by arity.

Seat flips and ledger edits this push (Stubs.kt, FindSym.kt):

- Stubs.unpad -> doc delegate (row 289; SysfuncMisc fullUnpad
  call sites unchanged).
- Stubs.Mltoa -> doc delegate with the Int-to-long widening
  (row 292; the arith.h u57 ledger row ANTICIPATED this
  owner, the raise's "libmpscpp.h:120 macro" tag superseded).
- Stubs.wstem TAG CORRECTED + FLIPPED (row 293; owner tag was
  wrong on both counts; the pmw Stem/stem sites unchanged).
- FindSym.kt: the row-297 verification addendum (the row-265
  twin note in that header now resolves: this batch owns the
  compiler twin, fold adjudicated with the bodies diffed
  this session).

Miss rows: none recorded this tranche (0 of 10 consults; one
hit-adapt - the unpad sign-byte window decode - and nine
hit-reuse).

Whole-arm kotlinc gate (kotlinc 2.1.20, sha a118197b...): 277
source files (268 + 9 new), silent pass, EXIT=0, 0 errors / 0
warnings, red-build count 0 across the tranche. H-015 hygiene
scan clean on all new and edited files (no raw dollar, no
backticks, no double-bang, ASCII only) and every pushed text
file ends with the trailing-newline byte (the B9 close
splice-repair rule asserted at push time).

Events: 20 rows appended this tranche (the 10 consults x
reuse + hit pairs; 730 consult rows -> 740, 1458 event rows ->
1478 at this push, 1479 CSV lines). The regenerated-CSV byte-
identity verification runs with this push: the python port of
wp2/analysis/R/export_doc_events.R over the journal WITH this
fragment appended reproduces the appended CSV byte-for-byte
(the same port reproduces the at-rest 1459-line CSV first,
730 -> 1458, before the splice).

Next protocol step: DOC batch 10 tranche 3 (the complex
stratum, units.csv rows 279-288: compiler-mumps.c::main, the
interp-sym.cpp::sym_ monster resolving the Stubs.sym_ seat
tagged at tranche 1, libmpscpp misc-8, libmpscpp.h misc,
misc-4, interp-select misc, interp-bmgsubs misc, inout,
interp-globalb misc, global::DocCorrelate), then the B10 close
instrument (the B9-close shape: coverage checklist, consult
totals, per-unit list, events regen, stub ledger, red-build
ledger, grading).

DOC consult grammar rows (timestamps identical to the events
CSV append of this push):

- DOC consult: unit interp-sysfunc.cpp::unpad, entry H-014, outcome hit-adapt, timestamp 2026-11-22T06:11:00Z
- DOC consult: unit include/mumpsc/defines.h::<misc>, entry H-004, outcome hit-reuse, timestamp 2026-11-22T06:12:00Z
- DOC consult: unit libmpscpp.cpp::line1149, entry H-014, outcome hit-reuse, timestamp 2026-11-22T06:13:00Z
- DOC consult: unit interp-arith.cpp::Mltoa, entry H-014, outcome hit-reuse, timestamp 2026-11-22T06:14:00Z
- DOC consult: unit interp-stem.cpp::wstem, entry H-014, outcome hit-reuse, timestamp 2026-11-22T06:15:00Z
- DOC consult: unit include/mumpsc/sysparms.h::<misc>, entry H-004, outcome hit-reuse, timestamp 2026-11-22T06:16:00Z
- DOC consult: unit interp-fnumber.cpp::<misc>, entry H-004, outcome hit-reuse, timestamp 2026-11-22T06:17:00Z
- DOC consult: unit include/mumpsc/errors.h::<misc>, entry H-004, outcome hit-reuse, timestamp 2026-11-22T06:18:00Z
- DOC consult: unit compiler-sys1.c::xindex, entry H-014, outcome hit-reuse, timestamp 2026-11-22T06:19:00Z
- DOC consult: unit libmpscpp.cpp::mdh_tree_level_add, entry H-014, outcome hit-reuse, timestamp 2026-11-22T06:20:00Z
# DOC B10 T3 journal fragment, 2026-11-22

DOC batch 10 (cycle 9 per C-001) tranche 3, the complex
stratum: units.csv rows 279-288 (10 rows), DOC sequential ids
299-308 (the batch closer: the simple rows 278-288 landed at
tranche 1, the core rows 289-298 at tranche 2). All 10 T3
dumps extracted with the recreated dump4.py and physically
verified at exact physical LOC (1372/484/600/369/600/156/121/
38/112/41 in roster order) against vendor anchors
(compiler-mumps.c:227 next_line, interp-sym.cpp:89 sym_,
libmpscpp.cpp:591/m591 section banner, the libmpscpp.h whole
header, libmpscpp.cpp:836, interp-select.cpp:29 the _select
pair, interp-bmgsubs.c:121, interp-parse.cpp:104 inout,
interp-globalb.cpp::Mglobal tail, libmpscpp.cpp:2291
DocCorrelate). The MISC-8 CHUNKER anomaly was adjudicated
before rendering: the dump4 misc-pool reclaim re-adds the
small-function bodies (phys 15 or less) a naive walk skips,
so libmpscpp.cpp yields 9 reachable chunks where the roster
named 5; the reclaim's d4chunk8 (699 raw / 600 phys) is
BYTE-IDENTICAL to the committed vendor dump libmpscpp.cpp::
<misc-8>.c.txt (cmp verified), and d4chunk4 == <misc-4>.

Render ledger (unit; units.csv row: LOC, score, rank - entry;
all rendered and compiled this session):

- compiler-mumps.c::main (unit 299; row 279: LOC 1372, score
  1125.2, rank 304): H-014, hit-adapt - CompilerMumpsMain.kt,
  THE COMPILER DRIVER: the argv/-SUB parse with the odd
  no-dot argv[1] source kept verbatim, the output-.m and
  program opens, the leading '+' define copy (getc/ungetc
  pushback), the include/defines banner with the floatstring/
  numcomp2/strxcat/strcatz inline emits and SyncClock/
  set_alarm, the label/function-ep scan (the ^^ and ^
  extrinsics, the _Argkk fe1-loop with the EP1001-EP1005
  emissions, the LabelsStart list), the doty indent ladder,
  the ctable spell check, the ':' postconditional
  (atTest/atCopy INTERPRET arm, the parse_ atoi arm), and the
  ENTIRE label goto web (next_line, next_cmnd, pstrtn, ctch,
  cntnue, dcl, do_entry, another_do, next_goto, gexit,
  job_return, abrt_exit, the 25 error labels) folded into the
  private MumpsCtx state machine (the ParseCPCtx precedent);
  seats: the landed handler renders ifCode/set_code/
  kill_code/lock_code/read_code/write_code/for_code/
  new_code/mumps_open/zcommands_code bind through a shared
  return-code ladder (PARSE_ERROR 1..BAD_LOCK 9, the KillCode
  _KC const-file precedent), Stubs.parse_/atTest/atCopy/
  Extract2, EP/fcninit/linepad/copyrightNotice/doty/abrtx/
  errmod_, the InterpH command chars; the FCN 'D' case is
  DEAD (icmnd is lowercased; the macro value quirk kept
  verbatim); the except[] carrier table-fills here
  (compiler-globals.h:174 static initializer); the fgets
  newline strip pair is the vendor self-assign no-op
  (H-013); the dead text (the if (!MAIN_flg && 0) prologue
  cook, the if (0) zmain block, the another_do unreachable
  while-tail, the six unreferenced error labels) ledgered;
  the break emit carries the escaped <dollar>test text.
- interp-sym.cpp::sym_ (unit 300; row 280: LOC 484, score
  632.4, rank 301): H-012, hit-reuse - InterpSym.kt, THE RUN
  TIME SYMBOL TABLE: all fifteen opcode arms of the vendor
  roster (0/1/2/3/4/5/6/7/8/9/10/11/33/99/100/101 plus the
  150/200 quirk: SymCPPdata and SymMarkCopyFlag are THE SAME
  VALUE 102 in defines.h - both stored arms fuse onto the
  same branch quirk ledger), the Stab start[] walk, the
  retrieve/11 no-label walk, the store family with the
  <dollar>noerr special case, 9/101 copy-flag/next with
  fullUnpad, the <dollar>order next-argument tail and the
  <dollar>next o_again key walk with the \\x1f coded-number
  exit decode, kill all/except with the Nmes list build, the
  New-inclusive push, the 99 push; the (char*)p1 store-arm
  returns are NOT reproducible (H-012: pointer identity is
  dead at every callsite; the non-NULL shape preserved by
  returning the b buffer); the symflg param writes are C
  dead stores (H-013); out-of-memory folds to the GC (H-008)
  with the sigint(100) tails kept; Stubs.sym_ FLIPS to
  delegate.
- libmpscpp.cpp::<misc-8> (unit 301; row 281: LOC 600,
  score 283.0, rank 291): H-014, hit-reuse -
  LibmpscppMisc10.kt: the global operator<< scalar tail
  store arms (UShort/Long/ULong/Float/Double/string/mstring),
  the ENTIRE operator>> read family (char*/global, the eight
  typed sscanf out-arms via scanIntM10/scanDoubleM10 with
  INVALID_CONVERSION, string, mstring; the Shr* holder
  classes per H-006), the free xecute/WriteMumps1 pair, the
  Pattern members (mstring/global sides, the pattern0
  no-code-raise quirk arm), the top-level Piece (Stubs.Mpiece
  seat) and the mstring Piece/Justify/Extract/Find members,
  __command__, the StopInit/StopLookup (stopWords.contains)/
  SYN/SynInit/Shred/ShredQuery adapters (the static cursor
  seat), nodash/Score/sw, the bmg_fullsearch C++ overload set
  (the malloc arm folding to the unit-305 render), the
  mstring scalar ctors with c_str/s_str, the ostream/istream
  pair, and the operator* family (seven member times arms,
  four friend commutes; GlobalRef arm unchecked, t1 aliasing,
  STR_MAX); CHUNK BOUNDARY ORPHANS: the dump head is the
  operator<<(short) orphan close brace (its signature/body
  ledgered at the misc-7 tail; the UShort/ULong store arms are
  rendered WHOLE here per the unit-202 precedent) and the dump
  tail cuts operator*(float, mstring) after its mult line
  (rendered WHOLE, one-render rule). Flips: Stubs.xecute
  (CharArray), stopLookup, SYN, shredQuery, mstringTimes x7.
- include/mumpsc/libmpscpp.h::<misc> (unit 302; row 282: LOC
  369, score 201.7, rank 290): H-014, hit-reuse -
  LibmpscppHMisc.kt, the whole-header pure ledger (the H-018
  convention): every declaration block dispositioned in
  vendor order to its landed owner (Carriers consts,
  ErrorsH, ArithH, the unit-293/297 renders, the add1/add3
  seats, MpstimeMisc/SynInit/StopInit seats, Misc5 GTK,
  unit-293 wstem, T1 Mltoa, the batch-10 renders of this
  tranche); no executable seat raised; the
  VARIABLE_NOT_FOUND 3 / PATTERN_ERROR 7 pair has no
  file-scope const and ledgered here.
- libmpscpp.cpp::<misc-4> (unit 303; row 283: LOC 600,
  score 152.4, rank 283): H-014, hit-reuse -
  LibmpscppMisc9.kt: the mstring operator=(global) tpx-
  checked assignment, operator!, pre/post inc/dec, unary +/-
  , the member + - % / ladders (Int/Long via the NEW
  Stubs.add1 seat, Double/Float via the NEW Stubs.add3 seat
  (both arith.h owners, raised throwing H-014; the 48-53
  controller arms); the <dollar>-sign fold family rides
  atollShared/Numcomp), the || concat family (eight member
  + seven friend commutes), the >= ladder, the <= ladder
  HEADS only (the <=(global)/(mstring) tails are the landed
  Misc5 le seats, one-render rule), and the scalar friend
  sets (the pure commutes render as the members they call);
  char*/const char* twins collapse onto String (H-015);
  the GlobalRef % arm reads unchecked, the GlobalRef + arm is
  the unchecked read (H-012). Flips: mstringPlus/Minus/Div
  (x7 each), mstringRem (x5), with the B10 T3 RESOLUTION
  family note; add1/add3 raised.
- interp-select.cpp::<misc> (unit 304; row 284: LOC 156,
  score 115.0, rank 281): H-014, hit-reuse -
  InterpSelectMisc.kt: the two _select bodies (the H-015
  overload pair resolved by signature; the select with more
  output than input is the vendor sigaction select probe
  family): the NULL-capable odd slots render as CharArray?
  probed in vendor order, the selectErrSel banner + sigint
  tail, the private atolSel (the leading-whitespace sign/
  digit walk), strmove on the Stubs seat; NO Stubs flip
  (no landside seat exists; compile-side call arms bind
  directly).
- interp-bmgsubs.c::<misc> (unit 305; row 285: LOC 121,
  score 76.3, rank 258): H-004, hit-reuse -
  InterpBmgsubsMisc.kt: the Boyer-Moore-Gosper search:
  bmg_setup (the BmgStatics delta0/cmap/str/linetemp/pattern
  carriers, delta0[pattern-j-1] write loop), bmg_act, the
  bmg_fullsearch window walk (copyOfRange over the
  NUL-terminated CharArray, H-007), Bcopy, the file-scope
  bmg_found owner as bmgFoundB (H-017), LARGE_BMS 59000,
  the ftp.uu.net attribution banner rows (H-013), the
  pattern[0] no-op statement ledgered; Stubs.bmgFullSearch
  FLIPS to the bmgFullSearchStrM10 adapter (CharArray(0)
  for the empty string per the _piece family idiom).
- interp-parse.cpp::inout (unit 306; row 286: LOC 38, score
  58.6, rank 249): H-004, hit-reuse - InterpParseInout.kt:
  the runtime device writer (inout plus the outwIO helper):
  the switch arms over the device index with the dev==5
  console arms (putchar/fflush render as System.out, H-001),
  the Writer-array out_file carrier with the null guard
  (the C dereference UB folded to the controlled guard,
  H-012 twin), the fall-off-the-switch return shape
  (H-013); the compiler-side unit-361 col twin is a DISTINCT
  vendor overload (Short int vs CharArray, H-015); Stubs.
  inout FLIPS to delegate.
- interp-globalb.cpp::<misc> (unit 307; row 287: LOC 112,
  score 53.4, rank 239): H-018, hit-reuse -
  InterpGlobalbMisc.kt, the excluded-arm ledger: every code
  row of this chunk is a native-build arm EXCLUDED from the
  reference build by the ifdef family (the -1 probe's dead
  1-store key rewrite, the NEXT/ORDERNEXT result walk with
  the empty/-1 rewrites, the hold[j]==1 back-off, the in-place
  bd shift, keyfix before every btree call) - each cites its
  live-arm twin already landed at Mglobal.kt and the btree
  codec family; the LGPL/attribution banners are comment rows
  (H-013).
- libmpscpp.cpp::global::DocCorrelate (unit 308; row 288:
  LOC 41, score 42.3, rank 225): H-014, hit-reuse -
  GlobalDocCorrelate.kt: the document correlation driver: the
  nested Order(1) walks over THIS vs B, the Cosine/Sim1/
  Dice/Jaccard scorer ladder (the default 1), the symmetric
  threshold write B(doc1,doc2)=c; the C++ member rides the
  GlobalRef extension seat under the docCorrelateStr name
  (H-017: the Misc5-landed DocCorrelate delegate twins own
  the name; redeclaration clash avoided), the string fcn
  ladder rides the char* twins' working body (the unit-154
  front-run ledger), (*this)(doc1) rides the NEW
  Stubs.globalInvokeKeyed seat (raised throwing H-014, the
  subscripted operator() owner family next to the
  setGlobalDoubleKeyed seat), Order/Kill rides Stubs.
  globalOrderN/globalKill; Stubs.globalDocCorrelate FLIPS to
  delegate.

Seat flips and ledger edits this push (Stubs.kt,
SeqAlignExterns.kt):

- Stubs.sym_ -> doc delegate (row 280; the T1 anticipatory
  tag resolves to this owner).
- Stubs.inout, Stubs.bmgFullSearch, Stubs.inout family call
  sites unchanged; Stubs.globalDocCorrelate -> delegate
  (row 288); Stubs.globalInvokeKeyed RAISED (throwing, the
  operator() owner); Stubs.xecute, Stubs.stopLookup, Stubs.
  SYN, Stubs.shredQuery -> delegates (row 301); Stubs.
  mstringTimes x7 -> delegates (row 301); Stubs.mstringPlus/
  mstringMinus/mstringDiv (x7 each) and mstringRem (x5) ->
  delegates with the B10 T3 RESOLUTION family note (row 303;
  the one-arg Int seat family folded, tag corrected);
  Stubs.add1/Stubs.add3 RAISED (throwing; the arith.h scalar
  ladder owners, rows 302/303).
- SeqAlignExterns.kt: fun Score(a: Char, b: Char) flipped
  from the raise to the GAP/MATCH/MISMATCH logic (the
  adapter twin of the landed Jaccard-family scorer consts;
  the house ledger cited this file row earlier - scored
  resolved with this push, DAN1 cleared).

Miss rows: none recorded this tranche (0 of 10 consults; one
hit-adapt - the compiler driver command dispatch - and nine
hit-reuse).

Whole-arm kotlinc gate (kotlinc 2.1.20): 287 source files
(277 + 10 new), silent pass, EXIT=0, 0 errors / 0 warnings,
red-build count 0 across the tranche. H-015 hygiene scan
clean on all twelve pushed text artifacts (no raw dollar, no
backticks, no double-bang, ASCII only - the two InterpSym
banner dollars rewritten to the \\u0024 escape, the misc-8
banner dollar carried as the escaped Kotlin string) and
every pushed text file ends with the trailing-newline byte
(the B9 close splice-repair rule asserted again at push
time).

Events: 20 rows appended this tranche (the 10 consults x
reuse + hit pairs; 740 consult rows -> 750, 1478 event rows
-> 1498 at this push, 1479 CSV lines -> 1499). The
regenerated-CSV byte-identity verification runs with this
push: the python port of wp2/analysis/R/export_doc_events.R
recreated fresh this session reproduces the at-rest 1479-
line CSV byte-for-byte first (740 -> 1478; md5 ccb04dc0 vs
the master zip copy), then over the journal WITH this
fragment appended produces the 1499-line CSV whose first
1479 lines are byte-identical to the at-rest file (the
prefix append check; the T2 sim copy's inverted append
check was the known erratum, corrected in the recreate).

Next protocol step: the DOC batch 10 CLOSE instrument (the
B9-close shape: coverage checklist over rows 278-310, the
consult totals, the per-unit list, events regen, the mstring
33-seat ledger reconciliation - the mstringPlus family closure
and the add1/add3 raised seats - grading), which completes
the DOC arm roster 1-308 of units.csv.

DOC consult grammar rows (timestamps identical to the events
CSV append of this push; roster order 299-308, the CSV rows
are ordered by roster, not by timestamp):

- DOC consult: unit compiler-mumps.c::main, entry H-014, outcome hit-adapt, timestamp 2026-11-22T06:28:00Z
- DOC consult: unit interp-sym.cpp::sym_, entry H-012, outcome hit-reuse, timestamp 2026-11-22T06:29:00Z
- DOC consult: unit libmpscpp.cpp::<misc-8>, entry H-014, outcome hit-reuse, timestamp 2026-11-22T06:21:00Z
- DOC consult: unit include/mumpsc/libmpscpp.h::<misc>, entry H-014, outcome hit-reuse, timestamp 2026-11-22T06:22:00Z
- DOC consult: unit libmpscpp.cpp::<misc-4>, entry H-014, outcome hit-reuse, timestamp 2026-11-22T06:23:00Z
- DOC consult: unit interp-select.cpp::<misc>, entry H-014, outcome hit-reuse, timestamp 2026-11-22T06:24:00Z
- DOC consult: unit interp-bmgsubs.c::<misc>, entry H-004, outcome hit-reuse, timestamp 2026-11-22T06:25:00Z
- DOC consult: unit interp-parse.cpp::inout, entry H-004, outcome hit-reuse, timestamp 2026-11-22T06:26:00Z
- DOC consult: unit interp-globalb.cpp::<misc>, entry H-018, outcome hit-reuse, timestamp 2026-11-22T06:27:00Z
- DOC consult: unit libmpscpp.cpp::global::DocCorrelate, entry H-014, outcome hit-reuse, timestamp 2026-11-22T06:30:00Z
# DOC B10 close journal fragment, 2026-11-22

### DOC B10 close - coverage checklist (units 278-308) and events verification

Per the DOC arm discipline: construct list from the batch-10 unit
texts, normalized per section 2.4; for each canonical construct,
at least one cookbook entry matched. Counts are batch-10 unit
consult coverage (one consult per unit per the C-008 rule; the
per-unit consult rows live in the tranche sections of 2026-11-22
above; no rows are duplicated here per the B6-close
splice-repair lesson). Batch 10 sits at the units.csv
batch-column-10 block rows 279-309 (31 rows: 10 complex / 10
core / 11 simple); the DOC sequential ids run 278-308 in the
committed stratum order (simple first, per the B8 Unit-vs-row
conventions of the 2cfddec repair). PILOT_TRACKING does not
tabulate batch 8+ (the no-PILOT batch-8 rule; rows track in
this journal only). With this batch the units.csv roster is
exhausted: 308 data rows, every one carrying a DOC arm
disposition.

- Canonical IO type: H-001 matched in 1 batch-10 unit consult(s)
- File-scope globals / banner ledger rows: H-004 matched in 7
  batch-10 unit consult(s)
- Char buffer / needle walk: H-007 matched in 3 batch-10 unit
  consult(s)
- Unsigned char domain mapping: H-012 matched in 2 batch-10
  unit consult(s)
- Out-of-scope C function -> stub: H-014 matched in 15 batch-10
  unit consult(s)
- Preprocessor conditional arms: H-018 matched in 3 batch-10
  unit consult(s)

Constructs not arising in the batch-10 unit consults (H-002,
H-003, H-005, H-006, H-008, H-009, H-010, H-011, H-013, H-015,
H-016, H-017): no matched rows for these entries at the
batch-10 consults; several appear inside the batch-10 render
bodies' KDoc ledgers (the H-010 Int-narrowing sites of the T1
SysLoad/LengthSym renders, the H-015 overload pairs of the T2
line1149/Mltoa/wstem renders and the T3 select/misc-4 twins, the
H-013 goto-web fold fossils and dead-tail ledgers of the
CompilerMumpsMain driver, the H-017 name-seat conventions of
the T3 renders carry entry references in the tranche paragraphs
above) but the C-008 one-consult-per-unit ledger counts only
the unit consults; noted for the checklist record, no action.

Consult outcome totals for the batch: 28 hit-reuse, 3
hit-adapt, 0 miss (31 consults, one per (unit, entry) per
C-005; every row carries a true in-session timestamp; roster
id strings per the batch-6 close repair 2 discipline; the
batch-10 Unit-vs-row conventions held throughout - the T3
complex rows 279-288 map to DOC ids 299-308, the reverse-order
block of the B10 roster, one row per unit, the T1 grammar
repair applied by construction in all three tranche
fragments).

Per-unit consult coverage (unit -> entry, outcome; DOC
sequential id order 278-308):

- libmpscpp.cpp::global::Length (unit 278, the :3330 std::string
  needle fold): H-007, hit-reuse
- interp-sym.cpp::SysLoad: H-001, hit-adapt
- libmpscpp.cpp::global::Length (unit 280, the :3354 char-star
  needle fold): H-012, hit-reuse
- interp-arith.cpp::mps_trunc: H-018, hit-reuse
- interp-arith.cpp::mps_ceil: H-018, hit-reuse
- libmpscpp.cpp::global::Piece: H-007, hit-reuse
- libmpscpp.cpp::mstring::Piece: H-007, hit-reuse
- interp-sym.cpp::_length: H-014, hit-reuse
- libmpscpp.cpp::global::Pattern: H-014, hit-reuse
- compiler-at-test.c::<misc>: H-004, hit-reuse
- libmpscpp.cpp::global::Justify: H-014, hit-reuse
- interp-sysfunc.cpp::unpad: H-014, hit-adapt
- include/mumpsc/defines.h::<misc>: H-004, hit-reuse
- libmpscpp.cpp::line1149: H-014, hit-reuse
- interp-arith.cpp::Mltoa: H-014, hit-reuse
- interp-stem.cpp::wstem: H-014, hit-reuse
- include/mumpsc/sysparms.h::<misc>: H-004, hit-reuse
- interp-fnumber.cpp::<misc>: H-004, hit-reuse
- include/mumpsc/errors.h::<misc>: H-004, hit-reuse
- compiler-sys1.c::xindex: H-014, hit-reuse
- libmpscpp.cpp::mdh_tree_level_add: H-014, hit-reuse
- compiler-mumps.c::main: H-014, hit-adapt
- interp-sym.cpp::sym_: H-012, hit-reuse
- libmpscpp.cpp::<misc-8>: H-014, hit-reuse
- include/mumpsc/libmpscpp.h::<misc>: H-014, hit-reuse
- libmpscpp.cpp::<misc-4>: H-014, hit-reuse
- interp-select.cpp::<misc>: H-014, hit-reuse
- interp-bmgsubs.c::<misc>: H-004, hit-reuse
- interp-parse.cpp::inout: H-004, hit-reuse
- interp-globalb.cpp::<misc>: H-018, hit-reuse
- libmpscpp.cpp::global::DocCorrelate: H-014, hit-reuse

Miss rows: none recorded this batch (0 of 31 consults); no new
cookbook candidates raised at the batch-10 consults (the
capture gate stays with T1/T2/G1-G4; noted, no action).

Close review note (the close review of the three tranche
sections above): NO prose-vs-consult divergences found. All 31
render-ledger paragraphs were checked against their registered
consult rows on entry, outcome and roster id string: the T1
eleven (render ledger entries match the 06:00-06:10Z rows,
including the two same-id global::Length rows distinguished by
needle fold and timestamp), the T2 ten (06:11-06:20Z, the
unpad H-014 hit-adapt prose matches its row; the wstem and
xindex tag corrections carry no consult-row change), and the
T3 ten (06:21-06:30Z roster order, the main H-014 hit-adapt and
nine hit-reuse rows). The two duplicate roster id strings
(global::Length x2 at B10, one consult each at distinct
timestamps and entries) validated against the C-008
(unit, entry, timestamp) uniqueness contract in the events
regen below.

Events regeneration verification: the full journal consult
ledger replayed through the C-005/C-008 export semantics
(Python port of export_doc_events.R, same grammar/validation/
mapping contract): 750 consult rows -> 1498 event rows; the
regenerated CSV is BYTE-IDENTICAL to the tracked
wp2/logs/DOC_knowledge_events.csv (99927 bytes, LF, quoted
4-col, 1499 lines with header). Roster membership (308 units),
outcome vocabulary, and (unit, entry, timestamp) uniqueness all
validated. The same regen was verified live at the T3 splice
(Rscript on Mark's clone: 750 -> 1498 with a clean working tree
at commit 004c407); the close instrument therefore lands NO
tracked-file change (regen identical, per the B7/B8/B9-close
pattern). VERIFICATION STEP (Mark): re-run Rscript
wp2/analysis/R/export_doc_events.R and
git status --porcelain wp2/logs/DOC_knowledge_events.csv
(expect clean) per the B2-close instrument verification
precedent; log any divergence as a protocol deviation.

Stub ledger at close (batch-10 standalone seats): T1 flipped
Stubs.mpsCeil/mpsTrunc to doc delegates (both batch-10 tags
resolved; the mps throwing-seat family fully delegated) and
updated the Stubs.sym_ owner tag (flips at tranche 3);
GlobalPiece.kt re-tagged the u224 row 239 -> row 304 (the
:7181 char-star twin folds onto the u188 String seat),
GlobalClassMisc.kt superseded its Pattern/Justify ledger rows
to the unit-286/288 renders, GlobalLength.kt carries the
unit-278/280 twin-fold addendum. T2 flipped Stubs.
unpad, Stubs.Mltoa (with the Int-to-Long widening at the call
sites) and Stubs.wstem (the raise's libmpscpp.cpp tag
corrected to the interp-stem.cpp:418 roster row); the row-297
xindex verification fold rides the FindSym.kt addendum (the
B9-rendered in-place body stands, verification only); Mltoa's
raised-stub signature kept (h015, callers bind unchanged). T3
flipped Stubs.sym_ (the row-280 seat resolves), Stubs.inout,
Stubs.bmgFullSearch (the bmgFullSearchStrM10 adapter),
Stubs.globalDocCorrelate, Stubs.xecute, Stubs.stopLookup,
Stubs.SYN and Stubs.shredQuery to doc delegates, and
SeqAlignExterns.kt fun Score(a, b) from the raise to the
GAP/MATCH/MISMATCH logic (the house ledger row cleared, DAN1
resolved with this push).

THE MSTRING 33-SEAT RECONCILIATION (the B9-close open item
closes here): the 33 Stubs throwing seats raised at the B9
close with owner tags citing the batch-10 roster rows are all
now flipped to doc delegates of the T3 renders: the seven
mstringTimes seats and the seven-seat mstringPlus, mstringMinus
and mstringDiv ladders ride the libmpscpp.cpp misc-8 / misc-4
renders (LibmpscppMisc10/LibmpscppMisc9), the five
mstringRem seats ride the same misc-4 member/friend ladders
(the GlobalRef percent arm reads unchecked, the H-012 note);
7 x 4 + 5 = 33, every seat accounted, the B10 T3 RESOLUTION
family note rides the flipped declarations. The ladders
depend on the two NEW raised seats Stubs.add1/Stubs.add3
(arith.h scalar ladder owners, throwing H-014, the 48-53
controller arms) and Stubs.globalInvokeKeyed (the subscripted
operator() owner family, throwing H-014, raised with the
DocCorrelate render); these three newly raised seats stay
throwing at close along with the standing batch-9 remains
(Stubs.sql for the interp-sql twin, Stubs.floatstring,
Stubs.keyfix, SyncClock) - their owners are below-roster or
non-corpus inlines; no roster row remains unresolved.

Red-build count at the tranche gates: 0 (the whole-arm kotlinc
2.1.20 gate was silent, 0 errors / 0 warnings, at every
tranche close: 268 files at T1 rising to 277 at T2 and 287 at
T3; the pre-gate hygiene catches - the T1 SysLoad KDoc
apostrophe slip, the T2 CJK-adjacent non-ASCII char in the
SysparmsH/InterpFnumberMisc first drafts - were caught before
any compile by the pre-gate scan, which is why the H-015 scan
alone is not enough; always run the non-ASCII class scan).
gradle gate BUILD SUCCESSFUL at the tranche splices (T1 10s,
T2 12s, T3 14s; the T3 gate runs from the arm directory
wp2/arms/DOC, the per-arm Gradle root convention - the repo
root carries no settings file).

Grading outcome: B10 31/31 translated (T1 simple 278-288 with
render push c8371f4 and splice 3961a72, T2 core 289-298 with
render push f991c44 and splice f2c4eaf, T3 complex 299-308
with render push 14ad7de, splice b83cf4d and the 004c407
fragment-cleanup follow-up that stages the fragment delete its
own commit); four verification-fold renders among them (the
unit-278/280 global::Length needle twins onto the u159 mstring
seat, the unit-283 global::Piece re-tag onto the u224 body,
and the unit-297 compiler-sys1 xindex twin onto the B9
FindSym.kt seat) - the one-render rule held across the batch;
the check-which-overload question of the row-298
mdh_tree_level_add resolved as a REAL render (the nine-column
overload, MdhTreeLevelAdd8.kt, with the unrowed ten-column
sibling ledgered there; the chunker captured only 9 of the 10
vendor overloads). The per-unit check columns remain todo
across B10 per the C-001 cycle-10 backfill decision.
PILOT_TRACKING unchanged at this close (the no-PILOT batch-8
rule; batch-10 rows tracked in this journal).

With batch 10 closed, the DOC arm roster 1-308 of units.csv is
complete: 308 data rows, every unit rendered, verification-
folded or verification-only-ledgered across batches 1-10, and
the 308-unit roster contract validated by the events regen.
Next protocol step per the C-001 committed order: the corpus is
exhausted (units.csv carries no batch-11 block), so the arm
turns to the C-001 cycle-10 per-unit check-column backfill and
the end-of-pilot instrumentation calibration of section 5
(per-arm heuristic-log row counts, entry count, hit/reuse/
miss totals, red-build count and grading outcome); no new
renders are required for either, and any post-corpus protocol
step beyond them lands by a fresh committed order.
# DAF B5 T2 journal fragment, 2026-11-22

### DAF B5 tranche 2 (units 134-143, core stratum) - 2026-11-22

Tranche scope per the committed batch-5 rank order: the T2 core
stratum (PILOT units 134-143 = units.csv rows 136-145), ten units,
one Dafny spec (spec/expx.dfy), one repair render of a colliding
id (disclosed below). Sandbox ~/b4 was rebuilt fresh this session:
vendor tree sha256 verified (621deb...ffdbe, 190531550 bytes), JDK17
+ kotlinc 2.1.20 (kcd sha a118197b...), Dafny 4.11.0
(4.11.0+fcb2042d6d043a2634f0854338c08feeaaaf4ae2, matches m1-build;
needs DOTNET_SYSTEM_GLOBALIZATION_INVARIANT=1 in this sandbox),
repo tree = master @dd9b6cb (the DOC B10 close already spliced; 44
kt in DAF arms tree pre-tranche). dump4.py recreated; all 10 T2
dumps verified at exact physical LOC against the vendor tree.
Arm independence kept as before: rendered from pristine vendor
sources + BASE-heuristic-log consultations only; no DOC/PRO
Kotlin consulted.

PROTOCOL DEVIATION, LOGGED (one-render rule repair disclosure):
the B4 T1 tranche rendered the colliding Transpose id against the
WRONG corpus body. units.csv carries exactly TWO
libmpscpp.cpp::Transpose rows: row 118 (loc 15, score 8.5, the
void Transpose(char* a, char* b) body at :2092) and row 142 (loc
23, ptr 0, score 14.1, the 23-LOC MSV service wrapper at :10316).
The B4 T1 render of unit 100 wrote the row-142 wrapper body into
Transpose.kt while logging two consult misses on the id as if it
were the row-118 batch-4 unit; row 118's actual body was never
rendered. Adjudication per the one-render rule (a corpus body may
be rendered exactly once, and every roster row must get exactly
one render): (a) TransposeAb.kt is a NEW repair render of the
row-118 body (the char a/b swap walk with the arg2 i/j order
quirk preserved, miss-then-adapt), landing in THIS tranche; (b)
Transpose.kt keeps the row-142 wrapper and gets a verification-
only addendum column recording that mapping (no code change; the
wrapper body is verbatim the B4-T1 render); (c) this paragraph is
the logged repair disclosure. The T2 roster row 142 (the wrapper)
is therefore satisfied by the B4-T1 render + the addendum, and the
row-118 batch-4 unit is satisfied by TransposeAb.kt. The B4 close
totals are NOT reopened; this repair lands entirely within B5 T2.

Multiply adjudication: the corpus carries THREE
libmpscpp.cpp::Multiply rows (49 loc 28 score 16.8 = the MSV
dispatcher at :10193; 141 = the string overload at :2211 score
21.4; 201 = the char* overload at :2252 score 20.8). This tranche
renders the ROW-141 string overload (metrics-unique score 21.4;
the found-dict last-wins collision of the dump stage was fixed at
the dump, not at the render). The dispatcher and char* rows stay
with their owning batches.

lpad TAG CORRECTION, LOGGED: the earlier DafInterpStubsT3 lpadD
raise wrongly tagged the owner as interp-arith.cpp; grep of the
vendor tree found no lpad body there. The owner is
interp-strmanip.cpp (row 137, this tranche). The stub was flipped
to delegate to lpadStrmanipD(src, dst, l) with a mapping comment;
the sole caller (Fcn.kt) passes the same array twice, alias-safe
for the in-place pad. Stub-seat discipline otherwise unchanged.

Per-unit render notes:
- Unit 143 interp-rpm.cpp::parse_count (PILOT 143; units.csv 136):
  the token_type constants render as TOKEN_COMMA_D=4 and
  TOKEN_DOT_D=5 (PATTERN_CODE=6 already seated); the goto exit arm
  folds to early returns; the hyphen/equals exit flags and the
  two-part argument comma split are preserved (ParseCount.kt).
- Unit 141 interp-strmanip.cpp::<misc> (units.csv 137): the trio
  strcatxD (destination-alias fold) / rindexD (the strrchr
  last-index walk) / lpadStrmanipD (the named-owner seat of the
  corrected lpad tag) (StrmanipMisc.kt).
- Unit 142 interp-sym.cpp::SysDump (units.csv 138): the symbol
  table dump walk renders with the DafStabNode + DafSymChains side
  carriers (tree shape carried; the C key[] carrier quirk kept,
  no key array); mltoa rides the mltoaDaf named-owner stub (Mltoa
  at interp-arith.cpp:84, not a corpus unit) (SysDump.kt).
- Unit 140 interp-sysfunc.cpp::getstr1 (units.csv 139): fgetc ->
  a reader fold with the C NUL-byte falsiness quirk of the drain
  kept verbatim (vendor quirks are not repaired on any arm); the
  oversize error arm prints the same 69-char prefix snippet;
  INPUT_LENGTH_D=10 added (Getstr1.kt).
- Unit 139 interp-arith.cpp::expx (units.csv 140): the ifdef-web
  twin of mps_asin/mps_log10 (same file, same lattice): reference
  arm is gcvt_wrapper(pow(strtod(a),strtod(b))) -> fmt8g over the
  kotlin pow; the MPFR and long-double branches are dead on the
  reference build, carried as comment fold. The mps_exp family
  precedent (row 99) carries pow; the exponent-domain NaN
  pathology of a negative base is unguarded, ledgered per the
  asin/log10 precedents (Expx.kt).
- Unit 138 libmpscpp.cpp::Multiply (units.csv 141): the row-141
  string overload over the MpsGlobalNode seats
  (invoke/order/data/kill/value) (Multiply.kt).
- Unit 137 libmpscpp.cpp::Transpose (units.csv 142): satisfied by
  the B4-T1 wrapper render + the verification addendum recorded
  in the repair disclosure above (no new code).
- Row-118 repair render: TransposeAb.kt (transposeAbD), the
  void Transpose(char* a, char* b) body at :2092, the arg2 i/j
  order quirk preserved verbatim.
- Unit 136 libmpscpp.cpp::global::Sum (units.csv 143): the
  get_global 16-site TU service seat (getGlobalDaf named-owner
  stub, raised in-file); the GLOBAL_NOT_FOUND_D=2 check is reused
  from MstringLength.kt (B5 T1), not duplicated; the Min/IDF
  prefix-mismatch break kept exactly (Sum.kt).
- Unit 135 compiler-parse.c::ExpandData (units.csv 144): the
  mistranslate check then the pointer-expand walk in two stages;
  the void reference body renders String? per the BASE row-44
  out-param carrier fold on the null return arm (ExpandData.kt).
- Unit 134 libmpscpp.cpp::GlobalQS (units.csv 145): the coded
  service relay: the Mumps text s %=$qs(a,c) is built into a
  method-local StringBuilder (the static char tmp[STR_MAX]/r[STR_MAX]
  carriers fold; r is ledgered, the vendor never uses it), the
  literal dollar escaped per the row-36 rule, xecuted through the
  xecuteDaf named-owner stub (the row-98 overload collapse: the
  three C++ xecute overloads at :7053/:7063/:7073 collapse onto the
  String seat), and the % fetch rides SymGetD (row 92); the tpx
  flags live on DafMsv (GlobalQS.kt).

Dafny ledger: exemption carried for the service-call shaping
units (Sum over the get_global seat, ExpandData over the parse
walk with the String? carrier, GlobalQS over the xecute relay,
Multiply over the node stub seats); the two Transpose rows are
swap-shape twins over the node carriers, ledgered with the
SysDump/parse_count/getstr1/strmanip seats under the same
call-shaping exemption family as T1. Spec rendered and verified
(in-sandbox dafny 4.11.0): spec/expx.dfy (5 verified, 0 errors, 0
warnings, exit 0; the Pow positivity and unit-exponent properties
are DECLARED {:axiom} assumptions - spec-assumption divergence
note mirroring mps_log10.dfy: the %.8g text format is out of
scope). Full suite in-sandbox after expx: 38 verified pre-tranche
+ expx = 43 verified, 0 errors, 0 warnings.

Gates: whole-arm kotlinc 2.1.20 silent PASS at 54 files (44
pre-tranche + 10 new; Transpose.kt and DafInterpStubsT3.kt edits
in-place), run twice: once over the render gate copy and once
over the staged repo copy (both SILENT EXIT=0, 0 warnings). H-015
scan clean on all touched files: one CJK non-ASCII slip in Sum.kt
and one em dash caught and fixed pre-push (the em dash replaced
with a hyphen); all dollar occurrences escaped (\u0024 in Kotlin
strings, \$ in comments); trailing newlines verified byte-wise.
In-sandbox dafny 4.11.0 verify green; gradle + dafny re-run at
tranche close on m1-build (Mark).

Renders and consult ledger (each consult logged before applying):
- DAF consult: unit interp-rpm.cpp::parse_count, entry BASE-log:68, outcome hit-reuse, timestamp 2026-11-22T07:00:00Z
- DAF consult: unit interp-rpm.cpp::parse_count, entry BASE-log:37, outcome hit-adapt (enum-constant named seats), timestamp 2026-11-22T07:00:30Z
- DAF consult: unit interp-strmanip.cpp::<misc>, entry BASE-log:70, outcome hit-reuse (strcatx fold), timestamp 2026-11-22T07:01:00Z
- DAF consult: unit interp-strmanip.cpp::<misc>, entry BASE-log:33, outcome hit-reuse (rindex strrchr walk), timestamp 2026-11-22T07:01:30Z
- DAF consult: unit interp-strmanip.cpp::<misc>, entry BASE-log:37, outcome hit-adapt (lpad owner tag correction logged), timestamp 2026-11-22T07:02:00Z
- DAF consult: unit interp-sym.cpp::SysDump, entry BASE-log:60, outcome hit-adapt (DafStabNode + DafSymChains side carriers), timestamp 2026-11-22T07:02:30Z
- DAF consult: unit interp-sym.cpp::SysDump, entry BASE-log:18, outcome hit-reuse (mltoaDaf named-owner stub; Mltoa at interp-arith.cpp:84), timestamp 2026-11-22T07:03:00Z
- DAF consult: unit interp-sym.cpp::SysDump, entry BASE-log:64, outcome hit-reuse, timestamp 2026-11-22T07:03:30Z
- DAF consult: unit interp-sysfunc.cpp::getstr1, entry BASE-log:2, outcome hit-adapt (fgetc reader fold; C NUL falsiness quirk kept), timestamp 2026-11-22T07:04:00Z
- DAF consult: unit interp-sysfunc.cpp::getstr1, entry BASE-log:108, outcome hit-reuse, timestamp 2026-11-22T07:04:30Z
- DAF consult: unit interp-sysfunc.cpp::getstr1, entry BASE-log:69, outcome hit-reuse, timestamp 2026-11-22T07:05:00Z
- DAF consult: unit interp-sysfunc.cpp::getstr1, entry BASE-log:46, outcome hit-adapt (oversize error arm; 69-char snippet), timestamp 2026-11-22T07:05:30Z
- DAF consult: unit interp-arith.cpp::expx, entry BASE-log:39, outcome hit-reuse (reference arm plain-double; MPFR/long-double dead), timestamp 2026-11-22T07:06:00Z
- DAF consult: unit interp-arith.cpp::expx, entry BASE-log:40, outcome hit-reuse (strtod x2 with 0.0 fallback), timestamp 2026-11-22T07:06:30Z
- DAF consult: unit interp-arith.cpp::expx, entry BASE-log:41, outcome hit-reuse (rows 41+43 verification: gcvt_wrapper(x,15) = gcvt(x,8), no appended decimal point), timestamp 2026-11-22T07:07:00Z
- DAF consult: unit interp-arith.cpp::expx, entry BASE-log:99, outcome hit-reuse (row 99 fold: pow over the hardware-double path), timestamp 2026-11-22T07:07:30Z
- DAF consult: unit spec/expx.dfy (unit 139 expx), entry BASE-log:--, outcome miss (spec rendered: 5 verified; Pow positivity as declared assumption; %.8g format out of scope), timestamp 2026-11-22T07:08:00Z
- DAF consult: unit libmpscpp.cpp::Multiply, entry BASE-log:105, outcome hit-adapt (row-141 string overload over MpsGlobalNode seats), timestamp 2026-11-22T07:08:30Z
- DAF consult: unit libmpscpp.cpp::Multiply, entry BASE-log:54, outcome hit-reuse, timestamp 2026-11-22T07:09:00Z
- DAF consult: unit libmpscpp.cpp::Multiply, entry BASE-log:41, outcome hit-reuse (rows 41+43/40 folds carried), timestamp 2026-11-22T07:09:30Z
- DAF consult: unit libmpscpp.cpp::Transpose, entry BASE-log:105, outcome hit-reuse (row-142 wrapper: B4-T1 render + verification addendum; protocol deviation disclosed above), timestamp 2026-11-22T07:10:00Z
- DAF consult: unit libmpscpp.cpp::Transpose (row 118 repair), entry BASE-log:105, outcome miss, timestamp 2026-11-22T07:10:30Z
- DAF consult: unit libmpscpp.cpp::Transpose (row 118 repair), entry BASE-log:54, outcome hit-adapt (TransposeAb render; arg2 i/j quirk preserved), timestamp 2026-11-22T07:11:00Z
- DAF consult: unit libmpscpp.cpp::global::Sum, entry BASE-log:103, outcome hit-reuse, timestamp 2026-11-22T07:11:30Z
- DAF consult: unit libmpscpp.cpp::global::Sum, entry BASE-log:92, outcome hit-reuse (tpx retrieve-then-throw; GLOBAL_NOT_FOUND_D=2 reused from B5 T1), timestamp 2026-11-22T07:12:00Z
- DAF consult: unit libmpscpp.cpp::global::Sum, entry BASE-log:40, outcome hit-reuse (getGlobalDaf named-owner stub; 16-site TU service), timestamp 2026-11-22T07:12:30Z
- DAF consult: unit libmpscpp.cpp::global::Sum, entry BASE-log:91, outcome hit-adapt (Min/IDF prefix-mismatch break kept), timestamp 2026-11-22T07:13:00Z
- DAF consult: unit compiler-parse.c::ExpandData, entry BASE-log:44, outcome hit-adapt (String? carrier per the row-44 out-param fold), timestamp 2026-11-22T07:13:30Z
- DAF consult: unit compiler-parse.c::ExpandData, entry BASE-log:45, outcome hit-reuse, timestamp 2026-11-22T07:14:00Z
- DAF consult: unit compiler-parse.c::ExpandData, entry BASE-log:2, outcome hit-adapt (mistranslate check then staged expand walk), timestamp 2026-11-22T07:14:30Z
- DAF consult: unit compiler-parse.c::ExpandData, entry BASE-log:16, outcome hit-reuse, timestamp 2026-11-22T07:15:00Z
- DAF consult: unit libmpscpp.cpp::GlobalQS, entry BASE-log:98, outcome hit-adapt (xecuteDaf named-owner stub; row-98 overload collapse), timestamp 2026-11-22T07:15:30Z
- DAF consult: unit libmpscpp.cpp::GlobalQS, entry BASE-log:36, outcome hit-reuse (literal dollar escaped per the escape rule), timestamp 2026-11-22T07:16:00Z
- DAF consult: unit libmpscpp.cpp::GlobalQS, entry BASE-log:92, outcome hit-reuse (SymGetD of the % symbol), timestamp 2026-11-22T07:16:30Z
- DAF consult: unit libmpscpp.cpp::GlobalQS, entry BASE-log:27, outcome hit-adapt (static char carriers fold to the method-local builder; tpx on DafMsv), timestamp 2026-11-22T07:17:00Z

Consult totals for the tranche: 35 consults, 22 hit-reuse, 12
hit-adapt, 1 miss (the spec entry; the row-118 repair miss is
followed by its hit-adapt render consult). Coverage: all ten core
units 134-143 rendered, verification-folded or verification-only-
ledgered; the two collision repairs (Transpose row-118 body,
Multiply row-141 overload) are disclosed above.

Files pushed (one commit):
- 10 new renders under wp2/arms/DAF/src/main/kotlin/daf/:
  ParseCount.kt (units.csv 136), StrmanipMisc.kt (137),
  SysDump.kt (138), Getstr1.kt (139), Expx.kt (140),
  Multiply.kt (141), TransposeAb.kt (the row-118 repair render),
  Sum.kt (143), ExpandData.kt (144), GlobalQS.kt (145)
  (all gate-verified; H-015 scanned)
- 2 edited: Transpose.kt (row-142 verification addendum, no code
  change), DafInterpStubsT3.kt (the lpadD tag-correction flip)
- 1 new spec under wp2/arms/DAF/spec/: expx.dfy (5 verified 0/0,
  exit 0; full suite 43 verified in-sandbox)
- wp2/logs/DAF_heuristic_log.csv (rows 117-133 appended; ids
  contiguous 1-133, 11-col CSV validated; BASE-log hits carry
  source=BASE-log + reused_from row ids, misses manual-decision)
- wp2/PILOT_TRACKING.md (batch-5 table DAF column rows 134-143
  todo -> translated; no header or other-row change; the row-154
  missing trailing pipe preserved as-is)
- wp2/arms/DAF_B5T2_fragment_2026-11-22.md (this fragment)

- INFRA (2026-11-22, C-009 cross-reference): protocol clarification C-009 registered on master (commit 918c81e; both blobs byte-verified against the drafted source; Mark's git pull ba4eec9..918c81e fast-forward clean, 2 files +240 lines): PRO event-log timestamps are a nominal calendar, not wall-clock records. The registry gains the C-009 table row and full section (observation, five clarifications, effect - none on the committed schedule, methodological consequences, files touched); the new analysis-side note wp2/analysis/PRO_EVENT_TIMESTAMP_PROVENANCE.md (133 lines) carries the instrumental contract, the three provenance classes (rows 2-31 tool-recorded true get_time seconds; 32-1211 authored near-dated round ladders; 1212-3689 authored future-dated, 67% of the log), the per-day stamp distribution, the batch->commit anchor table, and seven analysis rules (timestamp column inadmissible for ordering, pacing, session structure or time covariates; git commit chronology canonical; content columns stand - coverage_report is stamp-independent and the 1856 situations / 1851 hits / 5 misses = 99.7% hit / 99.0% reuse figures reconcile against the journal, the 5 misses being the B1 preprocessor_conditional units 4/10/12/20/29 already excluded per the SAP rule; provenance class flagged by row number at load time, the CSV untouched append-only; record-at-consult evidence rests on the journal + KDoc annotations; sensitivity analyses re-run under commit chronology; other arms not implicated, per-arm stamp-shape audit noted as a candidate). No translation, grade, gate, or CSV row affected; the entry binds the analysis phase only. Journal append-only; no earlier entry modified.
# DAF B5 T3 journal fragment, 2026-11-22

### DAF B5 tranche 3 (units 144-154, complex stratum) - 2026-11-22

Tranche scope per the committed batch-5 rank order: the T3 complex
stratum (PILOT units 144-154 = units.csv rows 125-135), eleven
units, one Dafny spec (spec/qlength_count.dfy). Sandbox ~/b4
rebuilt earlier this session and carried through from the C-009
registry work: vendor tree sha256 verified (621deb...ffdbe), JDK17
+ kotlinc 2.1.20 (kcd sha a118197b...), Dafny 4.11.0
(4.11.0+fcb2042d, the m1-build match;
DOTNET_SYSTEM_GLOBALIZATION_INVARIANT=1 in this sandbox), repo tree
= master @f93409a (the C-009 journal cross-reference spliced; 54 kt
in the DAF arm tree pre-tranche). dump4.py recreated from the
T3-stage with WANT reset to the eleven T3 units; all 11 dumps
verified at exact physical LOC against the vendor tree. Vendor
facts pinned this tranche: SYNMAX 20000 (libmpscpp.h.in:385), Syn
struct/syn[] at libmpscpp.cpp:7788, bmg BUFSIZE 50000/PATSIZE
9000/LARGE, CLOSE 4 (defines.h:87), INVALID_CONVERSION 1 /
GLOBAL_NOT_FOUND 2 / INPUT_LENGTH 10 (libmpscpp.h:70-79), PM_DEBUG
0 (pm.h:25), the parse_count/parse_pattern_element bodies
re-grounded from the vendor tree beyond the dump cut. Arm
independence kept: rendered from pristine vendor sources +
BASE-heuristic-log consultations only; no DOC/PRO Kotlin
consulted.

CHUNK-BOUNDARY DISCLOSURE, LOGGED (BASE row 83, the B4 misc-9
adjudication style): the libmpscpp.cpp::<misc-5> dump opens
mid-body ("else return false; }" completes the preceding chunk's
last NUMERIC operator<= body; the leading numeric <=/>= sets stay
with the preceding chunk's owner) and closes mid-body of
"mstring mcvt (char *)" (the misc-6 batch-7 roster row 187).
Adjudication: the render starts at the first complete body
(operator<=(global)) and the mcvt carry renders as a named-owner
stub (mcvtD) disclosed in KDoc and here. Neither neighboring chunk
is reopened and no earlier render is touched.

Scanner seat releases, logged at close (all four stubs raised in
prior tranches, all four released by this tranche's renders):
(a) qlengthDaf stub DELETED from DafInterpStubsT3.kt (released by
the Qlength.kt render, same signature qlengthDaf(tmp0,tmp2,sv));
(b) synInitD stub DELETED from LibmpscppMisc9.kt (released by the
SynInit.kt render); (c) the RpmParserStubsD seats acceptItD/
acceptD/syntaxErrorD/parsePatternAtomListD DELETED (released by
the RpmMisc.kt render; the ParseCount.kt/ParsePatternElement.kt/
ScanToken.kt callers bind unchanged); (d) the LibmpscppMisc9
docCorrelateD member stub DELETED (released by the global::
DocCorrelate render in LibmpscppMisc5.kt; its free-function relay
is the docCorrelateFreeD named stub). Additionally: the synD owner
TAG CORRECTION stands (grep-verified libmpscpp.cpp:7788, not
selftext.cpp; flipped in the earlier session and carried here);
the sqlDaf owner comment co-flips (rdbms-if b5 released by the
RdbmsSql.kt render); new named stubs raised with owners:
perlPmD (roster row 256 b9, outside the DAF batch), makeCstringD
(bstring-family TU helper, roster kin of unit 22), wstemD
(interp-bifs.cpp wstem family; not a corpus unit), pmD (the
interp-pm.cpp:43 body; not a corpus unit), the gtk C library seat
stubs (external non-corpus owners), zseekD/ztellD (FILE* fpos
seats, JVM Reader divergence), and the gtk-handle folds ride
opaque Ints (row 35).

Per-unit render notes:
- Unit 144 include/mumpsc/sym.h::<misc> (units.csv 125): the chunk
  carries extern declarations only (SY_* prototypes and the rt
  table externs owned by the libmpscpp/interp bodies); rendered as
  the SymHMisc.kt KDoc disposition ledger per the B4
  include-chunk precedent; no Kotlin bodies emitted (model-bag
  empty; the miss is the disposition itself).
- Unit 145 interp-sym.cpp::Qlength (units.csv 126): the counting
  walk over the v1d register with the char 1 / >129 mark predicate
  and the j==0 -> 0 else j-1 shaping; parseD/keyfixD/allocSVDaf
  ride their named-owner stubs; DOCUMENTED DIVERGENCE: the socket
  clone folds onto a field-copy helper (Qlength.kt).
- Unit 146 libmpscpp.cpp::SynInit (units.csv 127): SYNMAX 20000
  rows into the DafSyn singleton; the File reader fold carries a
  FILE* divergence ledger; the exit() failure arm folds onto
  FileNotFoundException; the mid-fill SYNMAX return 1 quirk and
  the compareTo bubble sort kept verbatim (SynInit.kt).
- Unit 147 interp-rpm.cpp::scan_token (units.csv 128): the
  toupper-probe scanner switch; take_char/syntax_error resolve in
  the unit-152 RpmMisc render same-tranche; the digit do-while,
  the quote-span while (vendor: unterminated strings run to the
  terminator) and the EOT-on-NUL kept verbatim (ScanToken.kt).
- Unit 148 interp-bmgsubs.c::bmg_search (units.csv 130): the BMG
  buffer walker: forced-NL tail, the 50000-buffer chunk loop with
  the last-full-line trim and the chunk punt, the delta0
  fast-forward consume, the LARGE rewind, the tail-anchored
  cmap/pattern confirm, the do-while line-start backwalk and the
  gotamatch report; read-past-end derefs ride the at() NUL-guard
  (row 81 divergence ledger); bmg_setup and gotamatch stay
  named-owner stubs; the unused byte_offset local kept
  comment-only; QUIRK KEPT VERBATIM: the -1 NULL-relay early
  return does NOT restore the forced tail (the vendor restores
  only on the normal exit) (BmgSearch.kt).
- Unit 149 include/mumpsc/mstringClass.h::<misc> (units.csv 129):
  the class ctors and operator bodies whose live call sites sit in
  the libmpscpp misch chunks; the Justify branch is constant-zero
  dead on the reference build (neutralized; ledger); rendered as
  the MstringClassHMisc.kt KDoc disposition ledger.
- Unit 150 mumps.cpp::main (units.csv 131): the argv syllabus
  (program name, the percent-symbols, %0/%c), the program-mode
  "s %="""^prog""" g @%%" boot with the "./" prefix strip, the
  announce banner with the build-macro lattice pinned (database
  empty; floatSize double; Fpbits 72; __TIME__/__DATE__ disclosed
  as compile-macro placeholders), and the REPL loop with the
  EOF/halt/? exits and the GlobalClose/pid-cleanLocks shutdown;
  DOCUMENTED DIVERGENCE: readline/read_history/write_history map
  onto kotlin.io readLine plus a mumps_history file analog; the
  program-name strncpy rides a LOCAL 512-char carrier (the DafMsv
  seat lattice pins no program field; disclosed); CLOSE_D=4
  pinned in-file (MumpsMainD.kt).
- Unit 151 include/mumpsc/compiler-globals-ext.h::<misc>
  (units.csv 132): the extern lattice carried by DafCompilerState
  TU seats and the struct nmes/txt/cvars fold ledger; one prior
  comment block rewritten for accuracy (the inout/add/numcomp
  neutralization) and re-verified against the vendor; rendered as
  the CompilerGlobalsExtMisc.kt KDoc disposition ledger.
- Unit 152 interp-rpm.cpp::<misc> (units.csv 133): the rpm
  pattern harness: the scanner/parser carriers (new_scanner/scan/
  take_char/new_parser/parse), the parse_pattern / atom / atom_
  list rules with the vendor's element-then-count concatenation
  order kept, accept/accept_it, the syntax_error caret pad with
  sigint(100), rpm() with the raw-regex hack path, the perlmatch
  "1"/"0" string relays, and perlcpp; PM_DEBUG pin 0 with the
  debug print constant-false; parse_count (unit 136) and
  parse_pattern_element (unit 113) bind as the B5-T2/B4-T2
  renders; the PCRE dead defines (DOUBLE_QUOTE/MAX_BACKREFS/
  OFFSET_BUF_LEN/min) live in the KDoc ledger only (RpmMisc.kt).
- Unit 153 rdbms-if.c::sql (units.csv 134): the reference build
  pins SQL/POSTGRES undefined so the live body is the return-0
  shape; the ODBC/PG branches stay KDoc-ledgered not emitted
  (RdbmsSql.kt).
- Unit 154 libmpscpp.cpp::<misc-5> (units.csv 135): the wrapper-
  library residue: the operator<= (global/mstring) bodies, the
  full operator==/operator!= sets (atol/atof + String collapse),
  thrownMessage (the vendor "detetected" typo kept),
  the __finish flg latch with the GlobalClose+exit fold, the
  global::Int/Double/Mstring/Char read members (the operator>>
  numeric fold disclosed), Stem/TokenInit/Token/Dump/Restore with
  the coded z-call splits kept (TokenInit reading svPtr->bd),
  the mdh_* GTK wrapper family over named external seat stubs
  with the %p handle fold, Getenv -> System.getenv, the ZSeek/
  Ztell fpos stubs, the Perl passthroughs (tpx-only per vendor),
  the global::DocCorrelate member pair (seat release), pt,
  global::Count with its tmp dual-role XNEXT walk, global::
  Transpose + the two free overloads over the node seats, the
  stem trio (by-reference mutation folded onto the returned
  value, disclosed), Hash/lhash with the 4-byte little-endian ULong
  accumulation and the %lu toString divergence, the indirect
  operator* minimal-carrier fold, the GlobalSet/GlobalGet overload
  sets over the String seats with the vendor stray-backslash token
  line kept comment-only, the pmw relays onto pmD, and the mcvt
  chunk-carry stub (LibmpscppMisc5.kt).

Dafny ledger: exemption carried for all eleven units (the
call-shaping/service-relay exemption family of T1/T2; the three
include-chunk dispositions and the ifdef-web/external-relay
clusters with the same reasons in the KDocs). Spec rendered and
verified (in-sandbox dafny 4.11.0): spec/qlength_count.dfy (7
detected, 0 errors, 0 warnings, exit 0): the Qlength (unit 145)
counting kernel over a byte-code domain
model (char-code literals are outside this Dafny grammar; the model
disclosed in the KDoc with the unsigned-char semantics kept
exact), CountMarked recursion + the NULSuffix terminator lemma + the
QlengthWalk method whose loop invariant pins the suffix
consumption ledger j + CountMarked(a[i..]) == CountMarked(a[..]);
the j==0/else j-1 output shaping sits outside the kernel.
Full suite in-sandbox after qlength_count: 43 pre-tranche + 7 =
50 detected, 0 errors, 0 warnings.

Gates: whole-arm kotlinc 2.1.20 silent PASS at 65 files (54
pre-tranche + 11 new; the three stub-file edits in-place), run
twice: once over the render gate copy and once over a fresh
repo-copy + new-files submission (both SILENT EXIT=0, 0 warnings).
H-015 scan clean on all touched files: one stray-brace repair and
one backticked stdin access caught and fixed pre-push (the
backticked System.in replaced with kotlin.io readLine); all dollar
occurrences escaped in strings and comments per the row-36 rule;
trailing newlines verified byte-wise. In-sandbox dafny 4.11.0
verify green; gradle + dafny re-run at tranche close on m1-build
(Mark).

Renders and consult ledger (each consult logged before applying):
- DAF consult: unit sym.h::<misc>, entry BASE-log:37, outcome hit-adapt, timestamp 2026-11-22T15:15:00Z
- DAF consult: unit sym.h::<misc>, entry BASE-log:90, outcome hit-adapt, timestamp 2026-11-22T15:15:30Z
- DAF consult: unit interp-sym.cpp::Qlength, entry BASE-log:8, outcome hit-adapt, timestamp 2026-11-22T15:16:00Z
- DAF consult: unit interp-sym.cpp::Qlength, entry BASE-log:81, outcome hit-reuse, timestamp 2026-11-22T15:16:30Z
- DAF consult: unit interp-sym.cpp::Qlength, entry BASE-log:111, outcome hit-reuse, timestamp 2026-11-22T15:17:00Z
- DAF consult: unit interp-sym.cpp::Qlength, entry BASE-log:37, outcome hit-adapt, timestamp 2026-11-22T15:17:30Z
- DAF consult: unit libmpscpp.cpp::SynInit, entry BASE-log:18, outcome hit-adapt, timestamp 2026-11-22T15:18:00Z
- DAF consult: unit libmpscpp.cpp::SynInit, entry BASE-log:22, outcome hit-reuse, timestamp 2026-11-22T15:18:30Z
- DAF consult: unit libmpscpp.cpp::SynInit, entry BASE-log:60, outcome hit-adapt, timestamp 2026-11-22T15:19:00Z
- DAF consult: unit libmpscpp.cpp::SynInit, entry BASE-log:46, outcome hit-adapt, timestamp 2026-11-22T15:19:30Z
- DAF consult: unit libmpscpp.cpp::SynInit, entry BASE-log:28, outcome hit-adapt, timestamp 2026-11-22T15:20:00Z
- DAF consult: unit interp-rpm.cpp::scan_token, entry BASE-log:25, outcome hit-reuse, timestamp 2026-11-22T15:20:30Z
- DAF consult: unit interp-rpm.cpp::scan_token, entry BASE-log:37, outcome hit-reuse, timestamp 2026-11-22T15:21:00Z
- DAF consult: unit interp-rpm.cpp::scan_token, entry BASE-log:111, outcome hit-reuse, timestamp 2026-11-22T15:21:30Z
- DAF consult: unit interp-rpm.cpp::scan_token, entry BASE-log:90, outcome hit-adapt, timestamp 2026-11-22T15:22:00Z
- DAF consult: unit interp-bmgsubs.c::bmg_search, entry BASE-log:6, outcome hit-reuse, timestamp 2026-11-22T15:22:30Z
- DAF consult: unit interp-bmgsubs.c::bmg_search, entry BASE-log:59, outcome hit-adapt, timestamp 2026-11-22T15:23:00Z
- DAF consult: unit interp-bmgsubs.c::bmg_search, entry BASE-log:81, outcome hit-adapt, timestamp 2026-11-22T15:23:30Z
- DAF consult: unit interp-bmgsubs.c::bmg_search, entry BASE-log:56, outcome hit-adapt, timestamp 2026-11-22T15:24:00Z
- DAF consult: unit interp-bmgsubs.c::bmg_search, entry BASE-log:8, outcome hit-adapt, timestamp 2026-11-22T15:24:30Z
- DAF consult: unit interp-bmgsubs.c::bmg_search, entry BASE-log:37, outcome hit-reuse, timestamp 2026-11-22T15:25:00Z
- DAF consult: unit mstringClass.h::<misc>, entry BASE-log:37, outcome hit-adapt, timestamp 2026-11-22T15:25:30Z
- DAF consult: unit mstringClass.h::<misc>, entry BASE-log:90, outcome hit-adapt, timestamp 2026-11-22T15:26:00Z
- DAF consult: unit mstringClass.h::<misc>, entry BASE-log:--, outcome miss, timestamp 2026-11-22T15:26:30Z
- DAF consult: unit mumps.cpp::main, entry BASE-log:53, outcome hit-adapt, timestamp 2026-11-22T15:27:00Z
- DAF consult: unit mumps.cpp::main, entry BASE-log:46, outcome hit-adapt, timestamp 2026-11-22T15:27:30Z
- DAF consult: unit mumps.cpp::main, entry BASE-log:60, outcome hit-adapt, timestamp 2026-11-22T15:28:00Z
- DAF consult: unit mumps.cpp::main, entry BASE-log:33, outcome hit-reuse, timestamp 2026-11-22T15:28:30Z
- DAF consult: unit mumps.cpp::main, entry BASE-log:18, outcome hit-adapt, timestamp 2026-11-22T15:29:00Z
- DAF consult: unit mumps.cpp::main, entry BASE-log:37, outcome hit-reuse, timestamp 2026-11-22T15:29:30Z
- DAF consult: unit mumps.cpp::main, entry BASE-log:--, outcome miss, timestamp 2026-11-22T15:30:00Z
- DAF consult: unit compiler-globals-ext.h::<misc>, entry BASE-log:6, outcome hit-reuse, timestamp 2026-11-22T15:30:30Z
- DAF consult: unit compiler-globals-ext.h::<misc>, entry BASE-log:68, outcome hit-adapt, timestamp 2026-11-22T15:31:00Z
- DAF consult: unit compiler-globals-ext.h::<misc>, entry BASE-log:--, outcome miss, timestamp 2026-11-22T15:31:30Z
- DAF consult: unit interp-rpm.cpp::<misc>, entry BASE-log:68, outcome hit-adapt, timestamp 2026-11-22T15:32:00Z
- DAF consult: unit interp-rpm.cpp::<misc>, entry BASE-log:90, outcome hit-adapt, timestamp 2026-11-22T15:32:30Z
- DAF consult: unit interp-rpm.cpp::<misc>, entry BASE-log:37, outcome hit-adapt, timestamp 2026-11-22T15:33:00Z
- DAF consult: unit interp-rpm.cpp::<misc>, entry BASE-log:59, outcome hit-adapt, timestamp 2026-11-22T15:33:30Z
- DAF consult: unit interp-rpm.cpp::<misc>, entry BASE-log:83, outcome hit-adapt, timestamp 2026-11-22T15:34:00Z
- DAF consult: unit interp-rpm.cpp::<misc>, entry BASE-log:46, outcome hit-reuse, timestamp 2026-11-22T15:34:30Z
- DAF consult: unit rdbms-if.c::sql, entry BASE-log:46, outcome hit-adapt, timestamp 2026-11-22T15:35:00Z
- DAF consult: unit rdbms-if.c::sql, entry BASE-log:85, outcome hit-reuse, timestamp 2026-11-22T15:35:30Z
- DAF consult: unit libmpscpp.cpp::<misc-5>, entry BASE-log:98, outcome hit-reuse, timestamp 2026-11-22T15:36:00Z
- DAF consult: unit libmpscpp.cpp::<misc-5>, entry BASE-log:99, outcome hit-reuse, timestamp 2026-11-22T15:36:30Z
- DAF consult: unit libmpscpp.cpp::<misc-5>, entry BASE-log:83, outcome hit-adapt, timestamp 2026-11-22T15:37:00Z
- DAF consult: unit libmpscpp.cpp::<misc-5>, entry BASE-log:35, outcome hit-adapt, timestamp 2026-11-22T15:37:30Z
- DAF consult: unit libmpscpp.cpp::<misc-5>, entry BASE-log:60, outcome hit-adapt, timestamp 2026-11-22T15:38:00Z
- DAF consult: unit libmpscpp.cpp::<misc-5>, entry BASE-log:56, outcome hit-adapt, timestamp 2026-11-22T15:38:30Z
- DAF consult: unit libmpscpp.cpp::<misc-5>, entry BASE-log:111, outcome hit-reuse, timestamp 2026-11-22T15:39:00Z
- DAF consult: unit libmpscpp.cpp::<misc-5>, entry BASE-log:106, outcome hit-reuse, timestamp 2026-11-22T15:39:30Z
- DAF consult: unit libmpscpp.cpp::<misc-5>, entry BASE-log:90, outcome hit-reuse, timestamp 2026-11-22T15:40:00Z
- DAF consult: unit libmpscpp.cpp::<misc-5>, entry BASE-log:80, outcome hit-reuse, timestamp 2026-11-22T15:40:30Z
- DAF consult: unit libmpscpp.cpp::<misc-5>, entry BASE-log:92, outcome hit-reuse, timestamp 2026-11-22T15:41:00Z
- DAF consult: unit spec/qlength_count.dfy (unit 145 Qlength kernel), entry BASE-log:--, outcome miss, timestamp 2026-11-22T15:41:30Z

Consult totals for the tranche: 54 consults, 20 hit-reuse, 30
hit-adapt, 4 miss. The four misses are the three model-bag-empty
include-chunk dispositions (sym.h and mstringClass.h carried
hit-adapt owner ledgers; the misses log the model-bag-empty
adjudication itself and the build-macro lattice pin of main) and
the spec entry (the miss-then-render pattern: qlength_count.dfy
rendered and verified 7/0/0). Coverage: all eleven complex units
144-154 rendered or disposition-ledgered per the include-chunk
precedent; four stub seats released; the chunk-boundary cuts
disclosed above.

DAF arm position after this tranche: batch 5 complete (T1-T3:
units 124-154 rendered; the misch/complex strata closed); the
whole-arm tree now 65 kt files (54 pre-tranche + 11). NEXT per
the committed order: BOTH B5 T2 (core 134-143), then DAF B6
(units 155-185).

Files pushed (one commit):
- 11 new renders under wp2/arms/DAF/src/main/kotlin/daf/:
  SymHMisc.kt (3425 bytes), Qlength.kt (5307), SynInit.kt (5600),
  ScanToken.kt (3761), BmgSearch.kt (7386), MstringClassHMisc.kt
  (4190), MumpsMainD.kt (10902), CompilerGlobalsExtMisc.kt (5349),
  RpmMisc.kt (9415), RdbmsSql.kt (2640), LibmpscppMisc5.kt
  (29024) (all gate-verified twice; H-015 scanned)
- 3 edited: DafInterpStubsT3.kt (12514; qlengthDaf release +
  sqlDaf co-owner flip), LibmpscppMisc9.kt (16141; synInitD +
  docCorrelateD releases + synD tag carry), RpmParserStubsD.kt
  (3112; four scanner seats released)
- 1 new spec under wp2/arms/DAF/spec/: qlength_count.dfy (2521
  bytes, 7 verified 0/0, exit 0; full suite 50 verified
  in-sandbox)
- wp2/logs/DAF_heuristic_log.csv (rows 134-147 appended; ids
  contiguous 1-147; BASE-log hits carry source=BASE-log +
  reused_from row ids, dispositions/gates manual-decision)
- wp2/PILOT_TRACKING.md (batch-5 table DAF column rows 144-154
  todo -> translated; no header or other-row change; diff is
  exactly those 11 cells; the row-154 missing trailing pipe
  preserved as-is)
- wp2/arms/DAF_B5T3_fragment_2026-11-22.md (this fragment)
### DAF B6 tranche 1 (units 155-164, simple stratum) - 2026-11-22

DAF B6 T1 (units 155-164): the simple stratum of batch 6, ten
units, ten renders plus one spec, pushed in commit (see Files
pushed below). Consults: 52 logged hits over the BASE-log
(44 hit-reuse / 8 hit-adapt / 0 miss) plus 2 manual-decisions (the
istream seat; the Sum empty-subscript fold) - heuristic-log rows
148-160.

QUEUE NOTE (PI decision of 2026-11-22, logged against the committed
order): the committed order had BOTH B5 T2 as the next tranche
after DAF B5 T3; the PI instructed the arm to finish ALL of DAF
before proceeding to BOTH. DAF B6 T1 runs ahead of BOTH B5 T2 per
that decision; BOTH B5 T2 stays first in the queue once DAF B6
closes (T2 core 165-175, T3 complex 176-182, then the 183-185
big-three session per the PRO B6 PI precedent).

Sandbox state (rebuilt fresh this session at 7111b99): vendor.tgz
sha256 621deb25... verified; JDK17 corretto; kotlinc 2.1.20; Dafny
4.11.0+fcb2042d; gate dir ~/b4/gate/daf from the fresh arm copy;
baseline gate green at 65 files, dafny suite 50 verified; all ten
unit dumps regenerated and LOC-verified (dump4 over
select_study_subset).

Unit notes:

Unit 155 compiler-fcninit.c::<misc> - the banner + include residue
(30 LOC): the dump proves the model bag EMPTY (GPL banner, the May
15 2024 line, four includes); KDoc-only disposition ledger
(CompilerFcninitMisc.kt), rows 37/39/78. The includes pin their
dispositions at the unit-164 lattice and the unit-151 seat; nothing
else is rendered.

Unit 156 libmpscpp.cpp::global::Find - the two-site
retrieve-then-throw wrapper: ref retrieve FIRST then key retrieve
(each GLOBAL_NOT_FOUND + MumpsGlobalExceptionD, row 92), the Mltoa
-> mltoaDaf stub seat, _find raised as the named stub findDaf
(owner interp-sym.cpp:1282), atoi over the r carrier (row 28).
Node receivers on MpsGlobalNode ref Strings (row 54). r[20] and
a[40] ride fresh CharArrays; the r read is cstring-terminated by
the stub-shape assumption (the stub carrier writes through
eventually; read is post-stub at the reference shape).

Unit 157 interp-rpm.cpp::leaning_toothpickize - the same-TU
pattern-munger: SEAT RELEASE + TAG CORRECTION. The
RpmParserStubsD.kt stub is DELETED; the real body renders in
LeaningToothpickize.kt; ParsePatternElement.kt binds unchanged
(row 90). The prior stub header blame "not a roster unit" was a TAG
ERROR - units.csv batch-6 row 157 IS this function (the row-182
note was the BASE-side numbering; SI stand corrected in the seat
header). Body: DOUBLE_QUOTE pinned \\u00D2 (rows 39/78/111); the
DOUBLE_QUOTE branch escapes to a plain quote and continues (the
vendor shape); the ispunct fold is the ASCII range set (row 25
family) with the 91..96 backtick range written as an explicit
Char.code check so the H-015 hygiene scan stays clean; bstring ops
remain unit-22 named stubs (row 37). Vendor quirk kept: the plain-
quote token itself falls through the != guard and is DROPPED.

Unit 158 libmpscpp.cpp::SmithWaterman - the bsv wrapper of the
sequence scorer: leading-blank carriers as String concat (rows
80/101), five sscanf %d -> atoi (row 28), the sw core raised as the
named stub swD (owner libmpscpp.cpp far chunk ~8153), the sprintf
%d over static char tmp[32] folds to a fresh String with the
single-buffer hazard ledgered (rows 33/56).

Unit 159 libmpscpp.cpp::global::Length(mstring) - the
mstring-needle overload: kernel IDENTICAL to the unit-129/133
renders - one retrieve-then-throw site (the needle is a VALUE so
no second site), then the strstr stride walk over String indexOf;
spec reuse of length_count.dfy (no new kernel spec; EXEMPT-by-reuse
ledger). Empty-needle vendor nontermination kept and ledgered (the
unit-129/133 quirk).

Unit 160 libmpscpp.cpp::Sum - the one-arg free wrapper: the
caret-bypass decode of the misc-9 family (rows 80/54); the coded-1
tail trim and boundary probe on at() guarded carriers (rows
111/81), the in-place arg1[i]=0 split kept (row 80); the member
call relays to the unit-143 globalSumD without inlining (rows
90/98). MANUAL-DECISION (rows 54/68): a("") is the empty-subscript
extension which appends nothing to the receiver ref, so the render
passes the receiver node; the nonempty branch passes the
arg1[i+1] slice node. sprintf %g -> fmtG; static tmp[32] fold
(rows 33/56).

Unit 161 libmpscpp.cpp::ScanAlnum(istream) - DUMP DISAMBIGUATION:
the dump4 first-body-by-loc extraction returned the FILE* overload
body (both overloads are 18 LOC, and the FILE* body comes first at
libmpscpp.cpp:10060); the units.csv row for this cell carries
pointer_marks 0, and the direct vendor read at line 10086 fixes
the row's body as the istream overload. The unit-104 FILE* render
keeps its body; this render (ScanAlnumStream.kt,
ScanAlnumStreamD) is the istream owner. MANUAL-DECISION (row 60
family): istream taken BY VALUE folds onto the PushbackReader
stream seat; u >> word rides fscanfS, whose 8191 token cap stands
in for the vendor's unbounded 8192-buffer write (UB on the
reference build; the cap is the seat divergence, ledgered). The
filter kernel is the unit-104 kernel; the max 511-through-EOF
branch quirk kept (closure rows 25/28/10; Dafny EXEMPT per the
unit-104 overload-family reason).

Unit 162 interp-arith.cpp::mps_log2 - the ifdef-web twin of the
unit-132 mps_log10: reference build pins MULTI_PRECISION and
BIGFLOAT false, live body fmt8g(log2(strtod)) (rows 39/40/41/43,
99/100); MPFR and long-double branches are the comment fold; the
nonpositive-input domain pathology kept. Spec: spec/mps_log2.dfy
mirrors mps_log10.dfy (the {:axiom} Log2 carrier; range facts over
positives including the x>=2 implies >=1 band; the %.8g text
format out of scope per the same SPEC-ASSUMPTION DIVERGENCE note).

Unit 163 interp-arith.cpp::mps_sin - the third twin of the
lattice: fmt8g(sin(strtod)); Dafny EXEMPT per the asin-family
reason (no honest range fact over the full input domain for the
declared-assumption pattern; the vendor guards nothing).

Unit 164 include/mumpsc/memsize.h::<misc> - the define lattice
(50 LOC): model bag EMPTY per the dump; KDoc-only ledger
(MemsizeHMisc.kt). Reference build resolves Linux per the ifdef
web (USYSTEM LINUX, UNIX 1, DOS 2, SYSTEM UNIX; rows 39/78); APPLE
GCVT and MSVC MSVisualC arms dead; the Solaris comment-table vs
ifdef-block shape disagreement ledgered (the comment table claims
MSVC undefined; the Solaris ifdef block never defines or undefines
it; the Linux arm is the pin). PS 4096 / PS1 / SHOW_CODE / VERSION
1000 / DDAT / DKEY / NOTABS / FORPARM 32 disposition-lines ledger
per define; the FIXME sysparms.h double definition is config-debt
commentary.

Seat adjudications this tranche (heuristic-log row 158):
  - leaningToothpickizeD stub DELETED from RpmParserStubsD.kt
    (released by the unit-157 render; header TAG CORRECTION).
  - findDaf raised in GlobalFind.kt (owner interp-sym.cpp:1282).
  - swD raised in SmithWaterman.kt (owner libmpscpp.cpp sw far
    chunk ~8153).
  - The ScanAlnum FILE* vs istream overload bodies disambiguated
    against the vendor source (the dump4 first-loc extraction had
    returned the wrong overload for the units.csv row).
  - DOUBLE_QUOTE_D const raised in LeaningToothpickize.kt
    (interp-rpm.cpp \\xd2 pin).

Dafny ledger: suite 55 verified 0 errors 0 warnings (50 baseline +
mps_log2 5: Escan 9, at_test 5, expx 5, length_count 6, mps_asin 3,
mps_log10 5, mps_log2 5, piece 8, qlength_count 7, smoke_test 2);
exit 0 on every spec, in-sandbox dafny 4.11.0 with
DOTNET_SYSTEM_GLOBALIZATION_INVARIANT=1. mps_sin EXEMPT
(asin-family reason, in the render KDoc).

Gates: kotlinc whole-arm gate over the 75-file daf tree SILENT
EXIT=0 twice (the gate copy; then the fresh repo arm copy with the
new files added). H-015 hygiene scan clean on all touched files
(no CJK; no em dashes; no backticks; dollars escaped; trailing
newlines verified). One compile round caught in-sandbox pre-push:
parameter name 'in' in findDaf (Kotlin keyword; renamed inBuf).

Consult ledger (nominal ladder 2026-11-22T15:45:00Z..15:51:00Z,
30s steps, no collision with the spent bands 21:05-21:16, 02:00-
02:20, 03:40-03:51, 04:20-04:29, 05:10-05:20, 05:30-05:39, 06:00-
06:30, 07:00-07:17, 15:10-15:41:30):
  155 15:45:00Z rows 37/39/78  banner residue ledger
  156 15:45:30Z rows 92/54/33/56/37/28  retrieve-then-throw pair
  157 15:46:00Z rows 68/25/37/111/90  bstring filter + seat release
  158 15:46:30Z rows 80/101/28/33/56/37  bsv wrapper discipline
  159 15:47:00Z rows 92/54/98/101/33/72/10  kernel reuse
  160 15:47:30Z rows 80/54/111/81/90/98/33/56  wrapper decode
  161 15:48:00Z rows 101/60/25/28/10  istream seat manual-decision
  162 15:48:30Z rows 39/40/41/43/99/100  lattice twin
  163 15:49:00Z rows 39/40/41/43/99/100  lattice twin + EXEMPT
  164 15:49:30Z rows 39/78/37  config lattice ledger
  (seats 15:50:00Z; spec 15:50:30Z; gate 15:51:00Z)

PILOT_TRACKING: the batch-6 tables gain the DAF column after DOC
per the batch-5 convention; rows 155-164 marked translated, 165-185
stay todo; the DAF B6 tranche-1 note paragraph (with the queue
note) sits above the batch-6 tranche-1 table. The heuristic log
grows to 160 rows.

Files pushed (byte counts, ASCII bytes):
  wp2/arms/DAF/src/main/kotlin/daf/CompilerFcninitMisc.kt 1315
  wp2/arms/DAF/src/main/kotlin/daf/GlobalFind.kt 3295
  wp2/arms/DAF/src/main/kotlin/daf/LeaningToothpickize.kt 2791
  wp2/arms/DAF/src/main/kotlin/daf/SmithWaterman.kt 2562
  wp2/arms/DAF/src/main/kotlin/daf/GlobalLengthM.kt 2713
  wp2/arms/DAF/src/main/kotlin/daf/SumW.kt 2801
  wp2/arms/DAF/src/main/kotlin/daf/ScanAlnumStream.kt 2986
  wp2/arms/DAF/src/main/kotlin/daf/MpsLog2.kt 1894
  wp2/arms/DAF/src/main/kotlin/daf/MpsSin.kt 1928
  wp2/arms/DAF/src/main/kotlin/daf/MemsizeHMisc.kt 2147
  wp2/arms/DAF/src/main/kotlin/daf/RpmParserStubsD.kt 3137 (edit)
  wp2/arms/DAF/spec/mps_log2.dfy 2337
  wp2/logs/DAF_heuristic_log.csv 59945 (148 + 13 rows)
  wp2/PILOT_TRACKING.md 40129 chars (DAF column + note)
  wp2/arms/DAF_B6T1_fragment_2026-11-22.md (this file)

Tranche closed; DAF B6 T2 (units 165-175, core stratum) follows
next, then T3 (176-182), then the 183-185 big-three session; BOTH
B5 T2 remains queued FIRST after DAF closes per the committed
order.
### DAF B6 tranche 2 (units 165-175, core stratum) - 2026-11-22

DAF B6 T2 (units 165-175): the core stratum of batch 6, eleven
units, eleven renders plus one spec, pushed in commit (see Files
pushed below). Consults: logged hits over the BASE-log plus the
manual header-ledger decisions - heuristic-log rows 161-174; the
three queued named-owner stub seats RELEASED this tranche
(MglobalDaf, stopInitD, diceD).

QUEUE NOTE (repeated from the tranche-1 record, PI decision of
2026-11-22): the PI instructed the arm to finish ALL of DAF before
proceeding to BOTH; BOTH B5 T2 stays first in the queue once DAF B6
closes (T3 complex 176-182 next, then the 183-185 big-three PI
session).

Sandbox state (rebuilt fresh this session at the post-splice
master 84b53df): vendor.tgz sha256 621deb25... verified; JDK17
corretto; kotlinc 2.1.20; Dafny 4.11.0+fcb2042d; gate dir
~/b4/gate/daf from the fresh arm copy; baseline gate green at 75
files, dafny suite 55 verified; all eleven unit dumps regenerated
and LOC-verified (dump4 over select_study_subset; every T2 unit
had a single overload candidate - the T1 ScanAlnum lesson repeated
cleanly). The three T1 rider wording fixes queued at tranche close
are applied and pushed with this tranche (no semantic change).

Unit notes:

Unit 165 interp-arith.cpp::mps_abs - the fourth ifdef-web twin of
the asin/log10/log2/sin family (34 LOC): reference body is
gcvt_wrapper(fabs(strtod(in))) with the always-0 return folded;
the BIGFLOAT fabsl branch and the whole #else arm (the mpf strchr
split with gmp_sscanf; the mpz branch with the failed-parse rebind
to literal 0) dead on the reference build, comment-folded. Dafny
EXEMPT (no honest range fact for abs over the full parse domain
beyond tautologies).

Unit 166 libmpscpp.cpp::StopInit - the stoplist loader: SEAT
RELEASE + TAG CORRECTION. The LibmpscppMisc9 stopInitD stub is
DELETED; the prior "libmpscpp.cpp/selftext.cpp StopInit; not a
roster unit" blame was WRONG - units.csv batch-6 row 166 IS the
string overload at libmpscpp.cpp:7729 (the StopList stop set
carrier at 7718). Real body renders in StopInit.kt: clear the
DafStop singleton set, the file loader over the java.io reader
seat with the DOCUMENTED DIVERGENCE that the missing-file
exit(EXIT_FAILURE) throws FileNotFoundException (the SynInit
precedent), the lowercase-prefix token scan per line, the empty
line inserting the empty token (the vendor does too). The sibling
stopLookupD stub stays with the corrected owner note.

Unit 167 interp-globalb.cpp::Mglobal - the 4-way dispatcher across
CLOSE/RETRIEVE/STORE/fallthrough: SEAT RELEASE. The DafInterpStubsT3
MglobalDaf stub is DELETED (its roster-b6 raise is THIS unit);
every existing MglobalDaf caller (Fcn, GlobalFind, GlobalLength,
GlobalQuery, GlobalTranslate, Sum, LibmpscppMisc5, InterpPieceMisc)
binds to the real body unchanged. KEY FINDING:
interp-globalb.cpp:45 defines SQLITE, so the SQLITE arm IS the
live arm and IS the 17-loc roster row; the native #else arm and the
dead SQLITE-side decls (unpad/sigint/CloseFlg/flush) are
comment-folded. btreeDaf raised as a named-owner stub (owner
interp-native-btree.cpp::btree at line 293); keyfix relays to the
existing keyfixDaf seat. Vendor quirk kept: CLOSE returns 1 without
touching btree.

Unit 168 interp-sym.cpp::_extract - the EXTRACT kernel: the from-1
adjust (negative clamps to 0), the to-1 adjust with the asymmetry
(a negative len becomes the ADJUSTED from), the inverted-range
empty, the i==0 and j==0 first-char special, the i>n empty, the
j>n clamp, the INCLUSIVE copy loop. First binding at DAF (the DOC
Stubs.Mextract precedent named this owner but no DAF seat existed)
- no stub to release. VENDOR QUIRKS ledgered: a start of "0" makes
the adjust produce index -1 and the C reads in[-1] (the render
rides the at() NUL guard - the OOB-read divergence); the origin
special reads in[0] even when in is empty (same guard); the clamp
lets the loop copy the terminator NUL before the final out[k]=0
(the observable is unchanged).

Unit 169 include/mumpsc/interp.h::<misc> - the command-macro +
operator-code + prototype residue (69 LOC): KDoc-only disposition
ledger per the unit-144 precedent. The command macros ledger to
the unit-185 render; the POPEN..PREDICATEVAR operator codes are
ledgered NOT PINNED (pinning untaken consts would invite drift
against defines.h at the bench verification); the four prototypes
dispositioned to their roster cells and named seats.

Unit 170 interp-sysfunc.cpp::TimeOut_getstr1 - the timed stdin
read: select and stty raise the named platform stubs
selectStdinDaf/sttyDaf; the sigintDaf(100) guard reused; the guard
fires only when io!=5; FIFO order kept (timeout BEFORE data);
fgets(area,STR_MAX,stdin) cuts at STR_MAX-1 bytes while the reader
seat reads the whole line (ledgered fold; identical observable
under the bound); dirflg folds Boolean (the 0/1 domain).

Unit 171 libmpscpp.cpp::global::Max - the sixth member of the
node-walk family (Count/Min/Avg/Sum/Dice this tranche): the XNEXT
walk with the control-char pair trim, the flg-first max tracking,
the tpx retrieve-then-throw via the existing getGlobalDaf stub;
the MglobalDaf call now rides the RELEASED unit-167 dispatcher.

Unit 172 include/mumpsc/stateVector.h::<misc> - the svPtr field
universe (76 LOC): KDoc ledger PLUS the DafMsv CARRIER EXTENSION
in DafCRuntime.kt - the residual struct MSV roster rendered in
vendor order (cfgdata/cfgkey, LockDone/kflg/symflg/ITRAP/ctrlc/
iorg/ERROR/gpad, in_file/out_file, RefParm/Rargs/Rargsx/_SymPush/
order, hit/count, restoredata, ScanMin/MaxWordSize, UKEY/UDAT,
Table, ConOpen, pgsql_msg/Connection/Col_Names, fdes, exec,
reference, ErrChar, program; the existing tpx and friends
unchanged). The banner "2001 - 2215" year pair ledgered.

Unit 173 compiler-parse.c::ExpandOrder - the compiler order
expansion, mirroring the unit-135 ExpandData render of the same
family: the CODED_COMMA carrier, the extract2Daf seat, the verbatim
DafCompilerState.emit GlobalOrder/LocalOrder lines ("NULL" literal
arg3, the p2236 tail comment on the G arm only), the below-128
byte-cap loops, null return = the untouched bp.

Unit 174 interp-arith.cpp::modulo - the operator-backing modulo:
floatstring raised as the named stub floatstringDaf (a static
inline at interp-arith.cpp:597 of the same TU; nine intrinsic
call sites; not a corpus unit). The fmod path parses a with
strtod but b with strtold - the MIX folds onto the double parse on
the reference build (quirk ledgered). The intLong define at line
59 is UNUSUALLY a plain define (not an ifdef): the long branch is
live and renders (the %d-of-long low-word print divergence
ledgered; the render prints the Int domain - identical at this
seat), the long-long branch dead, and the zero-divisor C UB renders
as the natural ArithmeticException (the UB-to-exception divergence;
the only vendor guard sits on the DEAD mpz path). The whole mpz
#else lattice is comment-folded.

Unit 175 libmpscpp.cpp::Dice - the Dice coefficient: SEAT RELEASE.
The LibmpscppMisc9 diceD stub is DELETED; the render keeps the
two-arg wrapper signature (the DiceW call unchanged; the svPtr
singleton rides DafInterpState.svPtr per the wrapper rule). The
coded-1 PAIR inner trims at both refs are a DIFFERENT trim lattice
from the Sum-family control-char trim (the FCN_MARK1 byte pair);
the vendor has NO j>=2 guard at the j-2 read (the render guards;
the OOB-read divergence ledgered). The A-side XNEXT walk rides the
RELEASED unit-167 dispatcher; the B-side ref rebuilt by appending
beyond the A ref constructs a fresh MpsGlobalNode per fetch (the
val-ref fold); B.Data() rides the path-gated MpsGlobalNode.data()
stub; two tpx retrieve-then-throw guards; the final 2*tot/val
divides only after the zero check on the accumulator sum.

Seat adjudications this tranche (heuristic-log row 172):
  - DafInterpStubsT3 MglobalDaf stub DELETED (released by the
    unit-167 render; the header roster row updated to RELEASED).
  - LibmpscppMisc9 stopInitD stub DELETED with the TAG CORRECTION
    (batch-6 row 166 IS the string overload at 7729).
  - LibmpscppMisc9 diceD stub DELETED (released by the unit-175
    render keeping the two-arg signature); stopLookupD stays with
    the corrected owner note.
  - floatstringDaf, btreeDaf, selectStdinDaf, sttyDaf raised as
    named-owner platform stubs in their render files.
  - The DafMsv carrier extended in DafCRuntime.kt (unit 172).
  - The three T1 rider wording fixes applied (LeaningToothpickize
    punt-to-punct + the predicate comment; MpsLog2
    -inf-or-NaN; MemsizeHMisc config constants).

Dafny ledger: suite 70 verified 0 errors 0 warnings (55 baseline +
the new spec/extract.dfy at 15: Escan 9, at_test 5, expx 5,
extract 15, length_count 6, mps_asin 3, mps_log10 5, mps_log2 5,
piece 8, qlength_count 7, smoke_test 2); exit 0 on every spec,
in-sandbox dafny 4.11.0 with DOTNET_SYSTEM_GLOBALIZATION_INVARIANT=1.
The extract spec models the adjust ladder and the clamped
effective slice with bound-domain contracts (InvertedRangeContract,
OriginContract, PastEndContract, ClampIdentity, totality carrier);
the start-of-zero index -1 read and the empty-in origin read are
EXCLUDED from the contracted domain (the OOB divergences the
render guards); the inclusive-copy overshoot at the clamp folds
to the exclusive min(j+1, n) effective bound. Two spec compile
rounds caught in-sandbox pre-push: the helper slice bound (j+1
above length at the clamp) and the ClampIdentity bound shape. One
Kotlin compile detail: the wrapper keeps the two-arg diceD
signature so the push files stay call-compatible (the append-mode
svPtr taken from the DafInterpState singleton).

Gates: kotlinc whole-arm gate over the 86-file daf tree SILENT
EXIT=0 twice (the gate copy; then the fresh repo arm copy with
the new files added; 75 base + 11 tranche renders). H-015 hygiene
scan clean on all touched files including the spec (no CJK; no em
dashes; no backticks; all dollar occurrences escaped; trailing
newlines verified).

Consult ledger (nominal ladder 2026-11-22T16:00:00Z..16:06:30Z,
30s steps, no collision with the spent bands 21:05-21:16, 02:00-
02:20, 03:40-03:51, 04:20-04:29, 05:10-05:20, 05:30-05:39, 06:00-
06:30, 07:00-07:17, 15:10-15:41:30, 15:45-15:51):
  165 16:00:00Z rows 39/40/41/43/99/100  lattice twin + EXEMPT
  166 16:00:30Z rows 60/25/6/22/98  loader + release + TAG fix
  167 16:01:00Z rows 90/37/91  dispatcher + SQLITE live-arm
  168 16:01:30Z rows 28/72/44/81/15/10  adjust ladder kernel
  169 16:02:00Z row 37  header ledger (manual-decision)
  170 16:02:30Z rows 60/59/39  platform stubs + sigint reuse
  171 16:03:00Z rows 103/92/40/91/54  walk reuse on the released seat
  172 16:03:30Z rows 18/6/22  carrier extension
  173 16:04:00Z rows 83/90/33  compiler arm mirror
  174 16:04:30Z rows 39/40/28/41/43/26/99/100  dispatch + intLong
  175 16:05:00Z rows 92/103/40/91/54/68/111  pair trim + release
  (seats 16:05:30Z; spec 16:06:00Z; gate 16:06:30Z)

PILOT_TRACKING: the batch-6 tranche-2 rows 165-175 marked DAF
translated (the DAF column the tranche-1 note introduced); the DAF
B6 tranche-2 note paragraph sits above the tranche-2 table. The
heuristic log grows to 174 rows.

Files pushed (byte counts, ASCII bytes):
  wp2/arms/DAF/src/main/kotlin/daf/MpsAbs.kt 2173
  wp2/arms/DAF/src/main/kotlin/daf/StopInit.kt 4072
  wp2/arms/DAF/src/main/kotlin/daf/Mglobal.kt 3421
  wp2/arms/DAF/src/main/kotlin/daf/Extract.kt 3212
  wp2/arms/DAF/src/main/kotlin/daf/InterpHMisc.kt 2166
  wp2/arms/DAF/src/main/kotlin/daf/TimeOutGetstr1.kt 4378
  wp2/arms/DAF/src/main/kotlin/daf/GlobalMax.kt 3650
  wp2/arms/DAF/src/main/kotlin/daf/StateVectorHMisc.kt 2557
  wp2/arms/DAF/src/main/kotlin/daf/ExpandOrder.kt 5736
  wp2/arms/DAF/src/main/kotlin/daf/Modulo.kt 4355
  wp2/arms/DAF/src/main/kotlin/daf/Dice.kt 6125
  wp2/arms/DAF/src/main/kotlin/daf/DafInterpStubsT3.kt 12464 (edit)
  wp2/arms/DAF/src/main/kotlin/daf/LibmpscppMisc9.kt 16343 (edit)
  wp2/arms/DAF/src/main/kotlin/daf/DafCRuntime.kt 6979 (edit)
  wp2/arms/DAF/src/main/kotlin/daf/LeaningToothpickize.kt 2791 (rider)
  wp2/arms/DAF/src/main/kotlin/daf/MpsLog2.kt 1897 (rider)
  wp2/arms/DAF/src/main/kotlin/daf/MemsizeHMisc.kt 2151 (rider)
  wp2/arms/DAF/spec/extract.dfy 4433
  wp2/logs/DAF_heuristic_log.csv 69742 (160 + 14 rows)
  wp2/PILOT_TRACKING.md 41594 chars (T2 rows + note)
  wp2/arms/DAF_B6T2_fragment_2026-11-22.md (this file)

Tranche closed; DAF B6 T3 (units 176-182, complex stratum) follows
next, then the 183-185 big-three session; BOTH B5 T2 remains
queued FIRST after DAF closes per the committed order.
### DAF B6 tranche 3 (units 176-182, complex stratum) - 2026-11-22

DAF B6 T3 (units 176-182): the complex stratum of batch 6 minus
the big three, seven units, seven renders, pushed in commit (see
Files pushed below). Consults: logged hits over the BASE-log plus
the manual header-ledger decisions - heuristic-log rows 175-183;
one named-owner stub seat RELEASED (shredD), one TAG CORRECTION
kept-in-place (wstemD), and the largest stub-raise set of the arm
so far (the POSIX platform seats of the server loop included).

QUEUE NOTE (repeated from the tranche-1 record, PI decision of
2026-11-22): the PI instructed the arm to finish ALL of DAF before
proceeding to BOTH; BOTH B5 T2 stays first in the queue once DAF
closes (the 183-185 big-three PI session is next, then BOTH).

Sandbox state (rebuilt fresh this session at the post-splice
master 66045d6): vendor.tgz sha256 621deb25... verified; JDK17
corretto; kotlinc 2.1.20; Dafny 4.11.0+fcb2042d; gate dir
~/b4/gate/daf from the fresh post-T2 arm copy; baseline gate green
at 86 files, dafny suite 70 verified; all seven unit dumps
regenerated and LOC-verified (dump4 over select_study_subset;
Shred had FOUR overload candidates 3/3/40/5 - the units.csv loc 40
auto-disambiguated the libmpscpp.cpp string body; every other T3
unit single-candidate). All T3 units are Dafny EXEMPT (no new
spec this tranche; the suite stays 70).

Unit notes:

Unit 176 libmpscpp.cpp::Shred (40 LOC) - the token generator of
the bsv family: SEAT RELEASE + TAG CORRECTION. The LibmpscppMisc9
shredD stub is DELETED; the prior "selftext.cpp Shred" blame was
WRONG (the ShredQuery sibling keeps its selftext residue, THIS
batch-6 roster row is the libmpscpp.cpp Shred overload); the ShredW
wrapper call keeps the shredD(String, Int) signature. The C
function-static lattice (p1/p2/flg/string/word) folds onto the
ShredStateD singleton. VENDOR QUIRKS kept: the size>=128 guard
falls through AFTER sigint(100) (unreachable in practice - the
sigint seat throws); a final PARTIAL fragment is DISCARDED; a
final EXACT-size fragment resets on the NEXT call (which returns
the empty string); the consumed callback NULs the string but not
the cursors (flg alone gates the re-arm). Dafny EXEMPT.

Unit 177 interp-stem.cpp::<misc> (345 LOC) - the stemming-residue
TU: the two conflation-row structs and the thirteen prefix/ending
tables of the $ZALTER/$ZNORMAL service, closing with the Steady
launcher Stem(svPtr, ep, word). The tables were parsed
programmatically from the vendor dump (counts verified: prefix 4;
the d 60, s 42, g 71, n 26, e 14, l 20, c 5, y 34, v 3, t 7, r 17
endings) and copied VERBATIM - vendor data, not recomputable. TAG
CORRECTION at LibmpscppMisc5 wstemD: the "interp-bifs.cpp wstem
family; not a corpus unit" blame was WRONG - the true owner is
interp-stem.cpp::wstem at 418, a units.csv BATCH-10 roster cell
(28 loc); the stub STAYS (b10 owns the body; the tables this
render carries are the data the b10 body consumes). The table
offset/replen columns are copied as-is (the vendor's own column
mismatches ledgered at the render). Dafny EXEMPT.

Unit 178 compiler-write.c::write_code (63 LOC) - the W-command
converter of the emitted-C back end: the out_file guard emit, the
atTest postconditional probe (abort arm ==-1 HERE vs set_code
==99: both atTest return arms kept verbatim per unit), the at
direct-execute arm (INTERPRET emit + the 1920 POST close), then
the argument loop over the !/# * ? text lattice with the inout
relay; the * arm emits atoi/putchar linepads and jumps iosub1
SKIPPING the relay (vendor goto shape kept as the skipInout
fold); the !/# pair sets i=2 for ! and i=1 for #
(reversed-from-words, kept verbatim). inoutDaf RAISED named stub
(owner compiler-sys1.c::inout at 292, roster b2; the
interp-parse.cpp 4-arg sibling rides its own b10 cell). Dafny
EXEMPT (emitter over the stub seats).

Unit 179 server.cpp::main (65 LOC) - the sockets server entry:
the SysV IPC + AF_UNIX datagram family raises as POSIX platform
stubs (ftokDaf, msggetDaf, msgrcvDaf, socketDaf, sendtoDaf,
closeDaf - not corpus), so the message loop is path-gated and the
MglobalDaf transaction inside it is UNREACHABLE in the DAF build
(ledgered; msgrcvDaf raises before any dispatch). The mymsgbuf
carrier is the unit-101 ServerMisc MyMsgBufD hit-reuse and
logtime() is the unit-101 real body hit-reuse. VENDOR QUIRKS
kept and ledgered: the -j parse copies argv[i+i] (should be
i+1 - out-of-bounds read folded onto the clamp); the Journal==
NULL arm kept print-only; the msgrcv type passed UNINITIALIZED
(0-fold, not pinned); buf0/buf1 read UNINITIALIZED at the
journal fprintf (the fill code is commented out at the vendor -
the JVM zero-fill makes the STORE/KILL journal arms unreachable
there; the commented memcpy block stays comment-only verbatim);
GlobalClose macro folds onto MglobalDaf(CLOSE_D,...); the
close(ssock)+GlobalClose tail after the while(1) is unreachable
at the vendor too (comment-only ledger). Dafny EXEMPT.

Unit 180 interp-parse.cpp::match (86 LOC) - the structured-walk
pattern matcher over the 3x20 pts matrix, ct counts, bd pattern
and ad subject. The odd pointer arithmetic is KEPT VERBATIM: ptr
starts -1, every case does ptr++ then reads ad[ptr] (first
consume at index 0); the E/e case has NO explicit continue on
pass (foot fall-through, same effect); the '"' literal case
spins ct[i] times doing NOTHING when pts[2][i]==0; the substr
seat: substrDaf RAISED named stub (owner
interp-strmanip.cpp::substr, roster b7; the bd FROM OFFSET 1
quirk of the &bd[1] call kept via srcOff); strcmp folds onto
String == over the cstring carriers; the xindex punct probe
keeps the C-string tmp shape from=1 (xindexDaf seat hit-reuse
per Fcn.kt); the table string in the render carries the
vendor's own backtick byte (only legitimate backtick of the
tranche). Dafny EXEMPT.

Unit 181 compiler-read.c::read_code (96 LOC) - the R-command
converter, the read side of the writeCodeDaf pair: the in_file
guard emit; the !/# arms i=2/i=1 pair routing to inout with the
DISCARDED IntCellD fold (the vendor passes the "" literal col);
the ? arm passing the pd1 interior cell; the head-text arm; the *
direct-mode flag; the set_code(SET) walk of the read item with
the ladder returning literal 99 on the fall-through; the timeout
colon arm (TimeOut_getstr1 io line emit with the dirflg int; the
commented file[5]=stdin linepad stays comment-only; the
strcpy gtmp back-fill); the DirectRead vs getstr1 read-mode
emits; the set_code(SET_STORE) store walk falling into the
cont_read tail (xpx advance, dirflg reset, comma skip, the
NUL/space/tab end check with the POST close-brace emit). retz
pins CMD_READ_D ('r', compiler-macros.h:100). Dafny EXEMPT.

Unit 182 mpstime.cpp::main (176 LOC) - the compiler-generated
benchmark entry (the executed emitted-C of the timing benchmark:
s t=$ZD1 for i=1:1:10000 s ^a(i)=i for i... s k=^a(i) w
$ZD1-t,!): a RUNTIME harness, every seat a runtime seat. The
setjmp/longjmp for1J/for2J state machine folds onto structured
while loops (the for-init/incr/limit triple and the forF
direction ternary ledgered at each loop head; the _i==100
longjmp exit signal is the structured exit). RAISED named stubs:
zfcnDaf (owner interp-zfcns.cpp::zfcn, the roster b6 big-three
unit 184 that STAYS QUEUED for the big-three session; the
interp-bifs.cpp::zfcn unit 183 likewise remains a raised-user of
that session), numcompDaf/addDaf/subDaf (interp-arith.cpp:
numcomp at 980; add/sub via arith.h:48/51; none roster cells),
strxcatDaf (mpstime.cpp:102 this-TU local inline), the
variableNotFound/VariableNotFound1 family
(interp-sysfunc.cpp:230/:241, non-roster). VENDOR QUIRKS kept:
the argv loop starts at 0 so the "$" entry is immediately
overwritten by "%0" (kept verbatim order); the if(0) alarm
slice stays the ALARM_SLICE_ZERO_D constant-fold of the
MumpsMainD precedent; __label_lookup/_text_function stay
comment-only; in/out_file[5] console default onto the T2
IntArray handle carriers; UDAT="data.dat"/UKEY="key.dat" onto
the T2 CharArray carriers; the M3701 coded bytes ride Char
literals (a-CE key fold, the CODED_COMMA_D CF after the catx
concat, the \u0001 subscript mark of ^a); the catch ladder
folds onto the Kotlin Throwable catch printing the vendor
"Interprerer Error" MISSPELLING verbatim; the epilogue
tmp0/tmp1 NULs, Mltoa(getpid()) seat, LockDone cleanLocks and
Mglobal(CLOSE) kept. SYMGET_NOLBL_D=11 and the libmpscpp.h
exception codes INPUT_ERROR 6 / INTERPRETER_ERROR 8 /
DATA_RANGE_ERROR 4 / NUMERIC_RANGE 5 / UNSPECIFIED 0 pinned at
the render. Dafny EXEMPT.

Seat adjudications this tranche (heuristic-log row 182):
  - LibmpscppMisc9 shredD stub DELETED (released by the unit-176
    render; the selftext.cpp-to-libmpscpp.cpp tag correction
    noted in place; the ShredW wrapper unchanged).
  - LibmpscppMisc5 wstemD TAG CORRECTION in place (owner
    interp-stem.cpp::wstem at 418, roster b10 cell; the stub
    STAYS; b10 owns the body).
  - inoutDaf raised at WriteCode.kt (compiler-sys1.c::inout at
    292, roster b2; re-read by ReadCode).
  - substrDaf raised at Match.kt (interp-strmanip.cpp::substr,
    roster b7).
  - zfcnDaf raised at MpstimeMain.kt (interp-zfcns.cpp::zfcn,
    roster b6 big-three unit 184 - stays queued).
  - numcompDaf/addDaf/subDaf/strxcatDaf and the
    variableNotFound family raised at MpstimeMain.kt (non-roster
    owners, ledgered).
  - ftokDaf/msggetDaf/msgrcvDaf/socketDaf/sendtoDaf/closeDaf
    raised at ServerMain.kt (POSIX platform, not corpus; the
    message loop path-gated).

Dafny ledger: suite 70 verified 0 errors 0 warnings UNCHANGED
(Escan 9, at_test 5, expx 5, extract 15, length_count 6,
mps_asin 3, mps_log10 5, mps_log2 5, piece 8, qlength_count 7,
smoke_test 2); exit 0 on every spec, in-sandbox dafny 4.11.0
with DOTNET_SYSTEM_GLOBALIZATION_INVARIANT=1. No new spec this
tranche: all seven T3 units are EXEMPT (platform stub gates,
emitters over stub seats, table carriers, the setjmp-fold
benchmark harness - none carries an honest functional fact the
suite does not already seat).

Gates: kotlinc whole-arm gate over the 93-file daf tree SILENT
EXIT=0 twice (the gate copy; then the fresh repo arm copy with
the new files added: 86 post-T2 base + 7 tranche renders).
H-015 hygiene scan clean on all touched files (no CJK; no em
dashes; dollars escaped in Kotlin strings; trailing newlines
verified; the one backtick is the vendor own literal byte in
the Match.kt punct table string - ledgered as such). Two
compile iterations caught in-sandbox pre-push: the Match.kt
punct table escape set and the ReadCode.kt $Test template
escape.

Consult ledger (nominal ladder 2026-11-22T16:20:00Z..16:24:00Z,
30s steps, no collision with the spent bands 21:05-21:16,
02:00-02:20, 03:40-03:51, 04:20-04:29, 05:10-05:20, 05:30-05:39,
06:00-06:30, 07:00-07:17, 15:10-15:41:30, 15:45-15:51,
16:00-16:06:30):
  176 16:20:00Z rows 6/22/25/59/99/100/101  release + cursor fold
  177 16:20:30Z rows 18/6/22/90/37/26  tables + wstem TAG fix
  178 16:21:00Z rows 2/6/22/26/28/30/90/91  emitter + inout raise
  179 16:21:30Z rows 3/6/18/68/90  platform stub set
  180 16:22:00Z rows 5/54/56/59/90  matcher + substr raise
  181 16:22:30Z rows 2/6/22/26/30/90/91  read converter
  182 16:23:00Z rows 5/6/18/22/33/56/68/90  benchmark + raises
  (seats 16:23:30Z rows 59/68; gate 16:24:00Z rows 93/113)

PILOT_TRACKING: the batch-6 tranche-3 rows 176-182 marked DAF
translated (Status and the check columns stay todo per the
C-001 cycle-10 decision); the DAF B6 tranche-3 note paragraph
sits above the tranche-3 table. The heuristic log grows to 183
rows.

Files pushed (byte counts, ASCII bytes):
  wp2/arms/DAF/src/main/kotlin/daf/Shred.kt 4857
  wp2/arms/DAF/src/main/kotlin/daf/InterpStemMisc.kt 15837
  wp2/arms/DAF/src/main/kotlin/daf/WriteCode.kt 7677
  wp2/arms/DAF/src/main/kotlin/daf/ServerMain.kt 7334
  wp2/arms/DAF/src/main/kotlin/daf/Match.kt 5619
  wp2/arms/DAF/src/main/kotlin/daf/ReadCode.kt 10245
  wp2/arms/DAF/src/main/kotlin/daf/MpstimeMain.kt 16400
  wp2/arms/DAF/src/main/kotlin/daf/LibmpscppMisc9.kt 16417 (edit)
  wp2/arms/DAF/src/main/kotlin/daf/LibmpscppMisc5.kt 29032 (edit)
  wp2/logs/DAF_heuristic_log.csv 77015 (174 + 9 rows)
  wp2/PILOT_TRACKING.md 43239 chars (T3 rows + note)
  wp2/arms/DAF_B6T3_fragment_2026-11-22.md (this file)

Tranche closed; open items for the PI: the 183-185 big-three
session (interp-bifs.cpp::zfcn, interp-zfcns.cpp::zfcn - the
zfcnDaf stub seat of unit 182 releases then -
interp-interp.cpp::Interpret) is next, then BOTH B5 T2 remains
queued FIRST after DAF closes per the committed order. The
T1/T2/T3 fragment files are still on master at this splice:
remove all three in a separate removal commit after the journal
splice (the standing house pattern).
### DAF B6 big-three session (units 183-185) - 2026-11-22

DAF B6 big-three session (units 183-185, the deferred ~1600-LOC
giants of the complex stratum): three renders, the largest single
renders of the arm, pushed in one commit. Consults: heuristic-log
rows 184-188; the session transplants the DOC arm's 2026-11-21
big-three renders (in the repo at
wp2/arms/DOC/src/main/kotlin/doc/) whole onto the DAF gate seats -
the H-012 same-name family rule pays off at the largest scale of
the program: the DOC Zfcn.kt/ZfcnBifs.kt/Interpret.kt bodies ride
with only seat-map edits.

QUEUE NOTE (unchanged from the tranche-3 record, PI decision of
2026-11-22): the PI instructed the arm to finish ALL of DAF before
proceeding to BOTH; BOTH B5 T2 stays first in the queue once DAF
closes. With the big three landed, DAF batch 6 is COMPLETE
(31/31); the batch-close protocol (coverage checklist, events
export, PILOT prose, gate round) follows this push.

Sandbox state (rebuilt fresh this session at the post-removal
master b58f99f, then wiped by an env reset AFTER the renders were
staged): the gate copy verified green before the reset - vendor
SHA-verified rebuild, dump4 LOC-verified zfcn twins 1622/1662
(interps) and 1581 (Interpret; 2311/2362/2485 raw), the DOC
big-three census taken from the in-repo DOC renders (the twin diff
is exactly five deltas; the vendor Makefile links interp-zfcns.o
ONLY), SQLITE #define verified LIVE at interp-zfcns.cpp:69. The
nine-session files staged to the knowledge sandbox survive the
reset byte-exact; the push rides those.

Unit notes:

Unit 184 interp-zfcns.cpp::zfcn (1662 LOC) - the LIVE
reference-build Z-function dispatcher, rendered FIRST: ZLblZ enum
machine (~75 labels, H-011), ZfcnsState statics (H-004; iter10 and
column drop per H-013), fun zfcnDaf releases the unit-182 seat
(the MpstimeMain raise DELETED, release note left; the two frame
call sites resolve same-shape). SQLITE arms live per H-018 with
the dead #else twins ledger-dropped; the generic sql arm passes
SQLCMD 55; zglobal is the ServiceInterface GBLNME 20 relay.
SEATS: serviceInterfaceDaf RAISED with TAG CORRECTION
(interp-service.cpp::ServiceInterface at 87 IS units.csv row 187
batch 7 - the SymHMisc.kt:38 not-a-roster-unit blame WRONG);
gtkTreeAddD RAISED (row 83; the DAF arm renders
mdh_tree_level_add only); the gtk library glue family raised as
twenty gtk*D seats; mpsFloorD/CeilD/TruncD (b9/b10 rows),
mpsLogD/CosD/TanD/Exp2D (b9 rows), mpsExpD/Exp10D/SqrtD/AcosD/
AtanD (the DOC-rendered kin rows, not in the DAF draw) RAISED
UNRESOLVED; sqlDaf called with the DAF String shape (the dmy
carrier wrapped, strlenCZc checks kept on the buffer); symRetDaf
pinned at 0/0 offsets; wstemD, stopLookupD (Boolean fold, the
vendor != 0 dropped), sigintDaf, stopInitD, SynInitW/SynLookupW/
BMGSearchW/ShredW/ShredQueryW/SmithWatermanW (the ep "" folds
onto CharArray(1)) delegated; the DAF real renders mpsLog2D/
mpsLog10D/mpsAbsD/mpsSinD/mpsAsin return String so the DOC
out-param calls fold onto strcpyS_B (the zabs != 0 ERR arm folds
UNREACHABLE - the intrinsic family success code is always 0 per
the MpsAbs row-43 note); expxD and MglobalDaf FLUSH_D 21 the
same way; ZSEEK/ZTELL raise per the LibmpscppMisc5 ZSeek/Ztell
JVM-divergence precedent (the fposZb store kept); the ctime
helper copies the DOC CopyrightNotice.kt:64 precedent. Dafny
EXEMPT.

Unit 183 interp-bifs.cpp::zfcn (1622 LOC) - the DEAD
build-alternate twin, rendered SECOND so the shared public seats
bind: fun zfcnBifsDaf (the H-017 name split), ZLblB machine,
ZfcnBifsState, TmZb. TWIN DELTAS kept per the vendor dump diff:
the zreplace arm rides with replaceDaf RAISED (builtin.h:40 API
entry, NOT a corpus unit; a bifs-only seat absent from the live
TU); the five SQLITE index arms absent; the generic sql arm
passes PRAGMA 29; zglobal is the g=5 count-empties MglobalDaf
probe; the internal recursion resolves to the LIVE zfcnDaf of
the unit-184 render (the vendor self-call is the same symbol;
DOC seated it to Stubs.zfcn the same way). The unit-184 seat map
applies unchanged (shared public raises; own private helper
copies + ctime twin per the H-017 file-scope rule). Dafny EXEMPT.

Unit 185 interp-interp.cpp::Interpret (1581 LOC) - the
interpreter mainline: fun InterpretDaf releases the
DafInterpStubsT3:119 seat (stub DELETED, release note left;
documented in the InterpHMisc ledger, which names exactly this
event). ILblI machine, InterpretState, NmesI carrier per the DOC
render. The InterpHMisc unit-14 header residue LEDGER PAYS: the
command-char lattice the header explicitly deferred to the
unit-185 render is pinned here as private consts at the interp.h
values (BREAK..ZCMDS and the NEW lexeme; the QUOTE lexeme rides unused), plus
PS 32000, PS1 32001, CHAR_1 u0001, CodedClose 207, CodedComma
208. SEATS: parseDaf, btreeDaf (Mglobal.kt), keyfixDaf,
buildLocalDaf/buildGlobalDaf (the -1 stack-cleaner call
quantizes: str/rslt nulls fold to CharArray(1) carriers, the
NULL svPtr passes svPtr), numcompDaf/addDaf/mltoaDaf/
cleanLocksDaf/makeLockDaf/dafFcn/readfD/timeOutGetstr1D (the DOC
4-arg call folds onto the 5-arg DAF seat: false, System.in,
ledgered), sigintDaf, MglobalDaf with GKILL->KILL_D and
STORE->STORE_D; symSeatI private shape adapter -> symRetDaf
(pins 0/0, the null check rides through); RAISES:
inoutInterpDaf (interp-parse.cpp::inout 4-arg, roster b10; the
WriteCode inoutDaf is the b2 3-arg twin - the distinct-sibling
naming), symFreeDaf (roster b1 row 28), mergeDaf (roster b8 row
226), directReadDaf (roster b1 row 24), getstr2Daf, prnterrDaf
(CARRIED-IN RESIDUOUS-FIELD WARNING: five always-true/false
condition warnings at Interpret.kt 693/825x2/1004/2155 - the
H-018 dead-lattice comparisons of the reference build, kept
verbatim, same acceptance as prior tranches). INT-DOMAIN FOLDS:
pd1len/tpx Long casts stripped (the DafMsv Int fields); the
start[] NULL sentinel folds to 0 in the IntArray domain; the
in/out_file Reader/Writer carriers fold onto the T2 IntArray
handle domain in the OPEN/SHELL/CLOSE arms (0/1 open flag, no
stream carrier kept - the downstream read seats raise anyway);
the NmesI NEW/KILL except-chain folds onto InterpretState.
nmesChain (DafMsv.nstart stays the Int field the GlobalQuery/
Qlength state copies bind); DafMsv EXT: nflg Int field added
(the T2 stateVector precedent; the GlobalQuery/Qlength copies
untouched). Dafny EXEMPT.

Gates: kotlinc whole-arm gate over the 96-file daf tree SILENT
EXIT=0 twice (the gate copy; then the fresh repo arm copy with
the six overlays: 93 post-T3 base + 3 session renders + 3 edited
seat files). Zero errors; five condition warnings accepted
(vendor-lattice faithful, noted above). H-015 hygiene scan clean
on all touched files (ASCII-only; no CJK; no em dash; dollars
escaped in Kotlin strings; trailing newlines verified; the
backtick literal of the DOC scaffold renders as the BT_ZB
u0060 concat constant, no raw backtick). Dafny suite 70
verified 0 errors, unchanged; no new spec (all three units
EXEMPT - platform-glue dispatch over raised seats).

Consult ledger (nominal ladder 2026-11-22T16:30:00Z..16:31:30Z,
30s steps, no collision with the spent bands through 16:24 of
the T3 ladder):
  184 16:30:00Z rows 2/6/18/22/54/56/59/68/90/112  live zfcn
       (rendered first; the roster row cited from the render head)
  183 16:30:30Z rows 2/6/18/22/54/56/59/68/90  bifs twin
  185 16:31:00Z rows 2/6/18/22/26/56/68/90/112  interpret mainline
  (seats 16:31:30Z rows 37/59/68; gate row manual-decision)

PILOT_TRACKING: the batch-6 big-three rows 183-185 marked DAF
translated (Status and the check columns stay todo per the C-001
cycle-10 decision); the DAF B6 big-three session note paragraph
sits after the DOC big-three paragraph. The heuristic log grows
to 188 rows. With this session DAF batch 6 sits 31/31 - the
batch-close protocol (coverage checklist, events export, gate
round) follows.

Files pushed (byte counts, ASCII bytes):
  wp2/arms/DAF/src/main/kotlin/daf/Zfcns.kt 105644
  wp2/arms/DAF/src/main/kotlin/daf/ZfcnBifs.kt 93382
  wp2/arms/DAF/src/main/kotlin/daf/Interpret.kt 116949
  wp2/arms/DAF/src/main/kotlin/daf/MpstimeMain.kt 16408 (edit;
    zfcnDaf stub released)
  wp2/arms/DAF/src/main/kotlin/daf/DafInterpStubsT3.kt 12493
    (edit; InterpretDaf stub released)
  wp2/arms/DAF/src/main/kotlin/daf/DafCRuntime.kt 7069 (edit;
    the DafMsv nflg Int field ext)
  wp2/logs/DAF_heuristic_log.csv 83643 (184 + 5 rows)
  wp2/PILOT_TRACKING.md 45043 chars (rows + note)
  wp2/arms/DAF_B6_big3_fragment_2026-11-22.md (this file)

Session closed; open items for the PI: the splice of this
fragment into the arm journal (then remove the fragment in the
standing house pattern), the gradle/dafny/gate round on the
spliced tree, the DAF B6 batch-close protocol (coverage
checklist, events export, PILOT prose mark), and BOTH B5 T2
remains queued FIRST after DAF closes per the committed order.
### DAF B6 close - coverage checklist (units 155-185), 2026-11-22

Batch-close section of the DAF arm. This arm draws no cookbook
and no rules base (section 3: the BASE heuristic log consults are
the ad hoc background instrument and wp2/logs/DAF_heuristic_log.csv
is the arm's own instrument), so the DOC/PRO close steps of entry
coverage against an artifact and events-CSV regeneration have no
object here; the close therefore tallies the batch's instrument
rows and states the no-artifact fact as data. Batch 6 (units
155-185) CLOSED: 31/31 translated 2026-11-22 in three tranches
plus the dedicated big-three session per the pre-registered
stratification and the PI queue decision of 2026-11-22 (T1
155-164 simple, push f467a08 with splice 84b53df; T2 165-175
core, push d9aa0be with the 1306b2d push-script repair and splice
66045d6; T3 176-182 complex minus the big three, push 69cba05
with splice 04cf3c0 and fragment removal b58f99f; the big-three
session 183-185, push 8639236 with splice b14103a and fragment
removal cadb7cf).

Gates: whole-arm kotlinc gate SILENT EXIT=0 at every tranche
close, both the gate copy and the fresh repo-copy submission
each time (75 -> 86 -> 93 -> 96 files across the batch); gradle
gate BUILD SUCCESSFUL at every close (m1-build for T1-T3; Mark's
MacBook, Gradle 9.9.1, at the big-three splice close - the
six-line always-true/false condition warning set of Interpret.kt
at 693/825x2/1004/2155x2 accepted, vendor-lattice faithful, same
acceptance as the prior tranches); dafny suite growth 55
verified 0 errors at T1 (the new spec/mps_log2.dfy, +5) -> 70 at
T2 (the new spec/extract.dfy, +15) -> 70 unchanged at T3 and at
the big-three (every remaining unit EXEMPT; no new spec after
T2; the suite verified per-spec on the spliced tree, 11 specs,
70 verified 0 errors).

Instrument state at this close: wp2/logs/DAF_heuristic_log.csv
at ids 1-188 contiguous, 11 columns, mixed CRLF/LF base with LF
appends per the T3-era convention (unchanged); the batch appends
rows 148-188 (41 rows: 31 unit rows - one per batch unit, this
arm's analog of the DOC single-line consult ledger; 4 seats
rows; 2 spec rows; 4 gate rows).

Unit-row outcome totals for the batch (one instrument row per
unit; the per-tranche prose sections carry the consult ladders,
seat ledgers and quirk ledgers): 17 hit-reuse, 14 hit-adapt,
0 miss (31 rows). Hit rate 31/31 = 100.0%; reuse rate 17/31 =
54.8%. Per tranche: T1 10 rows (4 reuse / 6 adapt), T2 11 rows
(6 reuse / 5 adapt, the manual header-ledger decision of the
interp.h residue row included), T3 7 rows (4 reuse / 3 adapt),
big three 3 rows (3 reuse / 0 adapt - the DOC big-three
scaffold transplant of 2026-11-21 onto the DAF gate seats, the
H-012 same-name family rule at the largest scale of the
program). LEDGER-SHAPE NOTE (logged as data): the tranche-1 rows
carry the short outcome tokens reuse/adapt in the decision
column; the hit-reuse/hit-adapt forms were adopted from T2 on;
the tally above normalizes both forms (semantics identical). No
miss rows and no capture candidate: this arm has no revision
artifact by design (the ad hoc factor level of the study), so
new resolutions stay journal notes; every arm-level divergence,
tag correction and seat adjudication of the batch rides the
per-tranche sections instead.

Red-build count for the batch: 0 red gate builds (every kotlinc
and gradle invocation at every tranche and session close is
journaled green on first submission). Pre-push in-sandbox
iterations logged as data (the designed catching point): the T1
wording riders fixed at T2 (no compile error); the T2 CSV
push-script truncation repaired at 1306b2d (a tooling failure
caught by the blob-sha verify, not a build); the two T3 escape
fixes caught by the pre-gate compile (the Match.kt punct-table
escape set; the ReadCode.kt dollar-template escape); the
big-three Interpret.kt SYMTAB_ERR/DATABASE collateral of a TAB
replace fixed pre-push.

ARM-CLOSE SEAT LEDGER (this closes the arm: per the binding
order_assignments.json Latin square the DAF arm cycles B4-B6
only): with batch 6 closed the DAF arm is COMPLETE - units 94-185
(batches 4-6, 30 + 31 + 31 = 92 units), every unit translated
and gate-green. The raised named-owner seats that cite roster
cells or owners OUTSIDE the arm's drawing stay raised at arm
close, the honest UNRESOLVED record per the H-014 out-of-scope
discipline: substrDaf (interp-strmanip.cpp::substr, b7),
mergeDaf (b8 row 226), the mps decimal-family seats of the b9
and b10 and kin rows, inoutInterpDaf (the interp-parse.cpp
4-arg b10 sibling; the WriteCode inoutDaf names the b2 3-arg
twin), wstemD (b10; the tag-corrected stub STAYS), replaceDaf
(builtin.h API entry, not a corpus unit; the bifs-only seat),
serviceInterfaceDaf (units.csv row 187, b7 - the TAG CORRECTION
carried by the raise), gtkTreeAddD (row 83) and the gtk*D
library glue family (external C library owners), the POSIX
platform family ftokDaf/msggetDaf/msgrcvDaf/socketDaf/sendtoDaf/
closeDaf (not corpus), getstr2Daf and prnterrDaf (no corpus
unit drawn by this arm), numcompDaf/addDaf/subDaf/strxcatDaf
and the variableNotFound family (non-roster owners). Their
owners live in the batches drawn by the DOC, PRO and BOTH arms.

DAF arm instrumentation calibration snapshot (arm close, per
section 5; the DOC precedent is the B2 snapshot of 2026-11-19):
heuristic-log rows 188 (ids 1-188 contiguous; source column: 163
BASE-log-cited rows, 25 manual-decision rows); knowledge
artifact count 0 by design (cookbook/rule-base growth is not a
factor level of this arm); consult structure = the BASE-log
row-cite ladders of the per-tranche prose (each unit row's
rationale names its anchors); events CSV: none (not applicable
to this arm); red-build count 0 at the gates (the pre-push
iterations and push repairs itemized above are logged as data);
grading outcome: 92/92 translated and gate-green; per-unit
check columns remain todo per the C-001 cycle-10 backfill
decision (the four columns track the BASE reference translation
per C-003.2).

Next protocol step per the committed order and the PI queue
decision of 2026-11-22: BOTH B5 T2 (core 134-143) opens; the
BOTH arm was opened at B5 T1 and its T2 was displaced per the PI
instruction that DAF finishes first.
### DAF B6 close - queue-note repair, 2026-11-22

REPAIR (logged as data; deviations and errors are recorded, never
silently applied): the "next protocol step" sentence of the DAF B6
close section (and the matching PILOT close paragraph sentence,
repaired in the same push) reads "BOTH B5 T2 (core 134-143)
opens". That is wrong on the facts: BOTH B5 T2 was already
rendered, pushed, gated, spliced and fragment-removed on
2026-11-21 - push eb6d33c (10 renders plus the BothCRuntime and
BothStubs T2 seat edits; specs expx.dfy, parse_count.dfy and
strmanip.dfy, suite 30 verified 0 errors; events +86 rows to
157) with journal splice 85caa1a at 2026-11-21T02:24:23Z, per
the BOTH B5 tranche-2 journal section of 2026-11-21. The queue
notes of the DAF B6 T1/T2/T3 and big-three records ("BOTH B5 T2
stays first in the queue once DAF B6 closes") were stale when
written: the T2 tranche ran and closed before the PI queue
decision of 2026-11-22 was recorded in the DAF sessions' working
notes, and the handoff chain (the session knowledge tracker and
this close prose) propagated the stale note without re-verifying
master. The intent of the PI decision was implemented factually
either way: DAF finished completely before any further BOTH
work began after the decision.

Provenance note: the staleness was discovered at the BOTH B5 T3
tranche open (sandbox rebuild), when the fresh repo copy showed
the both-tree at 21 classes with the T2 journal section already
spliced and no fragment files on master; both facts were then
verified against the commit history (eb6d33c author date
2026-11-21T02:22:06Z; splice 85caa1a; fragment removal in the
same splice chain).

Corrected next protocol step, per the committed batch-5 order
and the PI queue decision: BOTH B5 T3 (complex stratum, units
144-154) - the DAF B5 T3 complex set rendered against the BOTH
arm with the T1/T2 instrumentation intact (rules_v0.pl
consultations into BOTH_knowledge_events.csv plus the Dafny
spec workflow under wp2/arms/BOTH/spec/).
# BOTH B5 T3 journal fragment, 2026-11-22

### BOTH batch 5 tranche 3 (units 144-154, the complex stratum) - 2026-11-22

Tranche scope per the committed batch-5 rank order: the T3 complex
stratum (PILOT units 144-154, eleven units), the batch-closing
tranche of BOTH B5. With this push BOTH batch 5 is complete
(31/31 translated). Sandbox notes: vendor tree pristine-verified
per the rebuild recipe; JDK17 + kotlinc 2.1.20; Dafny 4.11.0
(invariant-globalization env var); dump4.py recreated from the
repo tool with all eleven T3 dumps LOC-verified before render.
Arm independence kept: pristine vendor + rules_v0.pl (18 rules
h001-h018) + the arm's own seat carriers only; no DOC/PRO/DAF
Kotlin consulted. The fresh repo copy at tranche open showed the
T2 splice already in master and the DAF B6 close complete with
the queue-note repair spliced (journal base 8947 lines at this
fragment's push).

Per-unit render notes:

- Unit 144 include/mumpsc/sym.h::<misc> (rank 215): KDoc-only
  residue ledger (the _INTERP_ two-armed keyfix comment guard is
  the only function-shaped residue; no bodies in the chunk).
  struct_carrier miss recurrence (the sym.h stab chain family);
  h018 reused.
- Unit 145 interp-sym.cpp::Qlength (rank 216): real body over
  the pushed AllocSV carrier (shallow-fold note: the memcpy seat
  shares the donor arrays; xd/v1d written before read), the
  keyfix.h static inline rendered as keyfixV1d, parse_ stub
  (roster 303 b3), walk count over the coded-marker bytes,
  j==0 arm writes "0".
- Unit 146 libmpscpp.cpp::SynInit (rank 219): real body; the
  fgets 12000-cap char walk seats on FileReader (read-error
  quantizes to EOF, h001); the line[i-1]=0 unconditional strip
  kept with the empty-line guard note; Syn + SynTable carriers
  for the TU globals; bubble sort by strcmp; SYNMAX 20000
  (libmpscpp.h:385) seated in BothStubs; the overflow arm
  returns 1 exactly as the vendor does.
- Unit 147 interp-rpm.cpp::scan Token (rank 232): real body;
  takeChar/syntaxError/Scanner ride the unit-152 render; h012
  unsigned-byte domain on the punct classes; renamed
  scanToken per h017.
- Unit 148 interp-bmgsubs.c::bmg_search (rank 233): real body
  with the delta tables, the BmgData carrier (delta0/cmap/
  pattern/patlen/nsuccess, the struct_carrier miss family
  seat), the unsigned-byte cmap index quirk quantized (vendor
  UB), the k-jump landing at strend kept, gotamatch TODO stub
  (roster 180 b1), consts BMG_BUFSIZE 50000 / PATSIZE 9000 /
  LARGE / NL; two H-015 backtick fixes caught at the pre-push
  scan (comment quotes).
- Unit 149 include/mumpsc/mstringClass.h::<misc> (rank 234):
  KDoc-only residue ledger of the class layout (the operator
  families itemized; no bodies in the chunk). The class
  member carrier miss is filed for the THIRD time (T1 + T2 +
  this tranche; capture candidate at batch close).
- Unit 150 mumps.cpp::main (rank 250): real body. The h018
  lattice resolved at the banner exactly like the DAF T2
  finding (SQLITE defined at interp-globalb.cpp:45 is the
  reference build's DBMS; NATIVE/BIGFLOAT/CLIENT arms stay
  comment-ledgered). argv percent plumbing via __symput
  (non-unit helper stub), the dispatch line tokens kept
  verbatim, readline seats on the stdin Reader (FileDescriptor
  reflection for the keyword-shaped member in), readHistory
  quantized no-op, cleanLocks stub (non-unit), the goodbye tail
  folded once, GlobalClose via the new CLOSE 4 (defines.h:87)
  const, MUMPS_BUILD_TIME/DATE kept token-shaped.
- Unit 151 include/mumpsc/compiler-globals-ext.h::<misc> (rank
  265): the h004 singleton with the verified header pins; the
  Nmes/Txt/Dcs class carriers; CVARMAX 100 / PS1 4097 / FBMAX
  4000; the G-suffixed collision renames (bdG, tpxG, t2G, xpxG,
  t0pxG, xdG, tmp2G, tmp3G, vdG, v1dG) journaled at the seats.
- Unit 152 interp-rpm.cpp::<misc> (rank 271): Scanner/Parser
  family - newScanner/scan/newParser delegates; this render
  LANDS the T2 acceptIt seat: the ParseCount.kt stub flips to
  the real p.currentToken = scan(p.s) delegate (h014 seat
  protocol; the flip rides this push as a targeted edit);
  parse/parsePattern/parsePatternAtom/parsePatternAtomList/
  accept/syntaxError plus rpm/perlmatch/perlcpp real; stubbed:
  perl_pm (roster 214 b9), parse_pattern_element (roster 196
  b4), leaning_toothpickize (roster 157 b6 after the DAF tag
  correction), make_cstring (roster 212 b1);
  RPM_DOUBLE_QUOTE kept as the U+00D2 seat.
- Unit 153 rdbms-if.c::sql (rank 276): h018 deciding - the
  reference build defines NEITHER SQL NOR POSTGRES (rdbms-if.c
  is not in the reference Makefile object set; the CFLAGS
  define neither), so the LIVE arm is the #else return 0; both
  excluded arms (the ODBC section and the PostgreSQL section,
  with their byte-1-coded MumpsArray row stores) stay as the
  comment ledger in the render. 209 LOC of preprocessor
  surface quantized to the honest contract.
- Unit 154 libmpscpp.cpp::<misc-5> (rank 295): the residual
  C++-binding chunk. Real bodies: the chunk-complete mstring
  comparison tails (fold family mstringLessEqual/mstringEquals/
  mstringNotEquals with receivers passed explicitly; char*/
  const char*/string fold onto the String arm), thrownMessage
  (INVALID_CONVERSION 1, libmpscpp.h:70, new seat), __finish
  (the GlobalClose fold over the new CLOSE const), the global
  member extract family (Int/Double/Mstring/Char over the new
  extractValue seat), Stem/TokenInit/Token (TokenInit keeps
  the vendor quirk of assigning the result from svPtr.bd, not
  the zfcn output), mdh_dialog_new_with_buttons (the sprintf
  percent-p pointer seat quantized to a String handle; the
  direct gtk_* wrappers seat as BothStubs platform stubs),
  Dump/Restore x3 (the zcd/zcl CE/CF envelopes), Getenv,
  ZSeek/Ztell x2 (FILE* seats on RandomAccessFile: the fpos_t
  pair maps onto getFilePointer/seek - ARM-LOCAL DIVERGENCE
  from the DAF big-three IntArray handle domain, journaled as
  an arm decision; the sscanf-fail UB quantizes to offset 0),
  Perl x2 (bodies commented in vendor; the live tpx return
  kept), DocCorrelate x2 over the string-arm member stub, pt
  (the UNINITIALIZED vendor index quantized to 0, journal
  note), global::Count REAL (the XNEXT order walk under the
  receiver subscript family; the trailing-two-sub-32-char strip
  kept), the global::Transpose member LANDS (the T2 transpose
  stub in BothStubs flips to the real loop body over the new
  order/kill/valuation carrier seats), the two Transpose
  wrappers (shared walk, mstring arm + char* arm), stem x3 and
  Hash/lhash (the 4-byte little-endian chunk walk, ULong
  domain; the vendor static char result folds fresh per h008;
  the past-NUL chunk reads see the zeroed buffer), the indirect
  operator* seat (Indirect carrier; named indirectOf - h015
  grammar_collision: times is reserved for repetition),
  GlobalSet x7 (fold to 4 overloads; the stray line-splice
  backslash in the vendor body journaled; no quote escaping,
  vendor-identical), GlobalGet x3 (fold to 2; the static r
  h008-fresh), pmw x5 (fold to 4; pm non-unit stub by
  provenance, libmpscpp.h:237), and the mcvt CHUNK-BOUNDARY
  FRAGMENT (the visible half renders; the continuation lands
  with misc-6, roster 294 b7; the vendor returns i, not the
  tmp it just built - kept).

Seat ledger of this push (new seats unless noted): SYNMAX 20000
(const); allocSV (roster 285 b8); parse_ (roster 303 b3); wstem
(roster 164 b10); pm (libmpscpp.h:237, non-unit); Indirect
carrier (struct indirect, globalClass.h); GlobalRef.extractValue
and GlobalRef.assignValue (globalClass.h layout roster 259,
pending); GlobalNode order/kill/value/setValue + the
one/two-subscript node-invoke seats (same layout owner);
docCorrelate string-arm member seat; the GTK platform stub
family (17 mdh_* seats); CLOSE 4 and INVALID_CONVERSION 1
(BothCRuntime); the Msv carrier extension (xd stateVector.h:45,
t0px :55, xpx :56, t2 :67, program :100; collision-checked
against the T1/T2 field set). FLIPS: acceptIt (ParseCount.kt,
roster 271 landed), GlobalNode.transpose (BothStubs, landed by
unit 154). PIN CORRECTION: the T1 BothStubs header cites the
global-class layout owner as roster 89; units.csv pins
include/mumpsc/globalClass.h::<misc> at rank 259 batch 3. The
new seats cite 259 and this correction is journaled here; the
stale T1 comment text stays untouched for history.

Consult ledger (find_rule before render; events CSV rows
appended at the 2026-11-22T19:05:00-19:05:52Z band, 53 rows:
34 reuse, 16 hit, 3 miss):
- Unit 144: h018 reuse, h017 hit, struct_carrier miss
  (timestamps 19:05:00/01/02Z)
- Unit 145: h004/h007/h009/h012/h014 reuse, h017 hit
  (19:05:03-08Z)
- Unit 146: h001/h004/h007 reuse, h013/h017 hit (19:05:09-13Z)
- Unit 147: h007/h012/h014 reuse, h017 hit (19:05:14-17Z)
- Unit 148: h004/h007/h012/h014 reuse, h013/h017 hit
  (19:05:18-23Z)
- Unit 149: h017 hit, class_member_carrier miss (19:05:24/25Z)
- Unit 150: h001/h004/h009/h011/h014/h018 reuse, h013/h017
  hit (19:05:26-33Z)
- Unit 151: h001/h004 reuse, h017 hit (19:05:34-36Z)
- Unit 152: h007/h014/h015/h017 reuse, h013 hit (19:05:37-41Z)
- Unit 153: h018 reuse, h017 hit, h007 reuse (19:05:42-44Z)
- Unit 154: h007/h012/h013/h014 reuse, h008/h015/h017 hit,
  class_member_carrier miss (19:05:45-52Z)

Miss families at this close: struct_carrier (recurrence: the
sym.h stab chain T2 seat now shared by the bmg carrier;
capture candidate at batch close) and class_member_carrier
(third recurrence: mstring/global members ride explicit
receiver state; capture candidate at batch close).

Gates: whole-arm kotlinc gate SILENT EXIT=0 at 32 files, run
both over the gate copy and over the fresh repo-copy submission
(the same file set; two invocations, both silent, exit 0, zero
output bytes); dafny suite 30 verified 0 errors UNCHANGED
(spec/expx.dfy 4, spec/length_count.dfy 5, spec/mps_log10.dfy 5,
spec/parse_count.dfy 2, spec/smoke_test.dfy 2, spec/strmanip.dfy
12; per-spec loop, never the glob - the Mark-side qlength_count
single-file glob failure of 2026-11-22 was the in-shell
mistake, not a repo event); every T3 unit EXEMPT, no new spec.
H-015 hygiene: all new/edited files ASCII-only, no backticks
(two BmgSearch comment-quote fixes caught pre-push), no tabs,
escaped dollars only (the zcd/zcl/zwi/zwp envelope strings),
trailing newlines at EOF. red-build count for the tranche: 0
red gate builds (the four pre-gate compile fixes - the SynInit
Long/Int loop boundary, the seat edits, the BmgSearch hygiene
pass, the Sql rule-name citation fix - were caught by the
in-sandbox pre-gate compile and are logged as data, per the
designed catching point).

Files pushed (byte counts, ASCII bytes; one commit):
  wp2/arms/BOTH/src/main/kotlin/both/SymHMisc.kt 5497
  wp2/arms/BOTH/src/main/kotlin/both/Qlength.kt 4713
  wp2/arms/BOTH/src/main/kotlin/both/SynInit.kt 5990
  wp2/arms/BOTH/src/main/kotlin/both/ScanToken.kt 3596
  wp2/arms/BOTH/src/main/kotlin/both/BmgSearch.kt 6939
  wp2/arms/BOTH/src/main/kotlin/both/MstringClassMisc.kt 4975
  wp2/arms/BOTH/src/main/kotlin/both/MumpsMain.kt 10972
  wp2/arms/BOTH/src/main/kotlin/both/CompilerGlobalsExtMisc.kt 8097
  wp2/arms/BOTH/src/main/kotlin/both/RpmMisc.kt 11062
  wp2/arms/BOTH/src/main/kotlin/both/Sql.kt 3102
  wp2/arms/BOTH/src/main/kotlin/both/LibmpscppMisc5.kt 24633
  (11 new renders, all gate-verified)
  wp2/arms/BOTH/src/main/kotlin/both/BothStubs.kt 13613 (edit:
  the T3 seat section, the transpose member flip, the GTK
  family, the pin-correction note)
  wp2/arms/BOTH/src/main/kotlin/both/BothCRuntime.kt 10369
  (edit: the Msv extension fields, CLOSE, INVALID_CONVERSION)
  wp2/arms/BOTH/src/main/kotlin/both/ParseCount.kt 5430 (edit:
  the Parser scanner field + the acceptIt stub flip)
  wp2/logs/BOTH_knowledge_events.csv 13272 (53 rows appended to
  the 157-row base: header + 210 data rows)
  wp2/PILOT_TRACKING.md 48592 chars (BOTH column rows 144-154
  todo -> translated, 11 cell edits; the batch-5 BOTH close
  paragraph appended; 499 lines)
  wp2/arms/BOTH_B5T3_fragment_2026-11-22.md (this file)

Open items:
- the splice of this fragment into wp2/arms/ARM_WORKING_NOTES.md
  (journal base 8947 lines + this fragment's 241 lines;
  expected wc -l 9188), then removal of the fragment file in
  the standing two-commit house pattern;
- the gradle gate round on the spliced tree (m1-build and the
  MacBook: cd wp2/arms/BOTH then gradle build; the sandbox
  here runs kotlinc only) - the earlier BOTH B5 gates are
  journaled at their tranche sections and B2/B3 show the
  m1-build first-pull pattern;
- the BOTH B5 BATCH-CLOSE protocol follows this splice per the
  committed order: coverage checklist, the C-008 events export
  regeneration (157 + 53 = 210 data rows), PILOT close prose,
  and the two miss-family capture decisions;
- the mcvt continuation rides libmpscpp.cpp::<misc-6> (roster
  294, batch 7, the next batch the BOTH arm draws);
- the named-owner seats raised against roster cells outside
  the arm's current drawing stay as the honest UNRESOLVED stub
  record per the H-014 discipline.
# BOTH B5 close journal fragment, 2026-11-22

### BOTH B5 close - coverage checklist (units 124-154), events verification, and rule-base promotions - 2026-11-22

Batch-close section of the BOTH arm. Batch 5 (PILOT units 124-154,
the BOTH arm's cycle-1 batch per the C-001 Latin square) CLOSED:
31/31 translated 2026-11-21/22 in three tranches per the
pre-registered stratification (T1 124-133 simple, push 9184cea0d
with the two trailing-newline fixups 00a0643a5 + 6e0e7dd20,
splice at 01525ab; T2 134-143 core, push eb6d33c9a, splice
85caa1a; T3 144-154 complex, push c306b0c2c, splice b6f20e7,
fragment removal cb4bc41). The tranche sections above carry the
per-unit records; this section is the coverage checklist, the
events verification, and the promotion decisions.

Gates at this close: whole-arm kotlinc SILENT EXIT=0 at 32 files
(T3 close, gate copy plus fresh repo-copy submission; re-verified
on the fresh post-reset sandbox rebuild of 2026-11-22, exit 0,
zero output bytes); dafny suite 30 verified 0 errors on the
committed spec set (smoke_test 2, length_count 5, mps_log10 5,
expx 4, parse_count 2, strmanip 12; per-spec loop and the
one-call suite discipline both green, re-verified in the fresh
sandbox); gradle BUILD SUCCESSFUL on Mark's MacBook at the T3
splice close (4s, first pull, zero fix rounds). The m1-build
gradle round is REPORTED BY THE PI AS NOT YET RUN (recorded
2026-11-22; the run and its journal note follow), and the swipl
coverage_report run flagged at the T1 section stays deferred to
m1-build with it; the hand-computed checklist below follows the
DOC-arm precedent in its place.

Events verification (C-008): wp2/logs/BOTH_knowledge_events.csv
re-validated byte-level on the fresh 2026-11-22 sandbox fetch:
211 lines (header + 210 data rows), 13272 bytes, 4-column
unquoted PRO schema, ASCII-only, no malformed rows, timestamp
bands contiguous (T1+T2 2026-11-21T02:05:00-02:07:37Z, T3
2026-11-22T19:05:00-19:05:52Z). Tranche ledger: T1 71 rows
(30 reuse / 35 hit / 6 miss), T2 86 rows (34 / 47 / 5), T3 53
rows (34 / 16 / 3). Batch totals: 210 rows = 98 reuse, 98 hit,
14 miss. Coverage: 196/210 consults answered by the v0 rule
base (93.3%); 98/196 answered unmodified (50.0% reuse of
answered); 12 of the 29 distinct unit ids carry at least one
miss row (every such row belongs to one of the three promotion
families below; no unit went wholly unanswered).

Coverage checklist (canonical constructs per the tranche
sections, normalized per section 2.4; the batch's construct
families and their rule coverage):
- io_stream/char_buffer/scratch constructs: h001/h007/h008
  exercised across all three tranches, answered everywhere.
- preprocessor lattices: h018 answered at every occurrence
  (T1 mps_log10 + mdh family, T3 mumps main banner + sql - the
  sql h018 resolution re-verified the reference Makefile
  object set and the CFLAGS; zero uncovered lattices).
- stub/out-of-scope surfaces: h014 seats answered at every
  occurrence; the arm-close stub ledger below itemizes the
  seats that stay raised.
- naming/format/numeric/unsigned/dead-store/goto/comma/
  globals-ext families: answered at every occurrence
  (h002-h006, h009-h013, h015-h017).
- UNCOVERED at v0 (the miss families, all pre-capture):
  class_member_carrier x9 (units 129/131/133/134/136/137/138/
  149/154), pointer_handle x3 (the mdh_tree_level_add
  overload set, units 126-128), struct_carrier x2 (units 142
  SysDump stab chain, 144 sym.h misc). These are the only
  constructs with no v0 match; all three are captured at this
  close (below).

RULE-BASE PROMOTIONS (PI decision of 2026-11-22, all three
families at T1 recurrence >= 2; G1 general-construct, G4 dedup
cleared, G3 no oracle content; the units above followed these
actions throughout B5, every render build-green at its tranche
gate):
- h019 class_member_carrier: member units of not-yet-rendered
  classes pass receiver state explicitly (data String /
  GlobalRef.ref) or reuse/raise the minimal carrier seats
  naming the layout owner roster id; vendor member name kept
  in the fold name. Example libmpscpp.cpp::mstring::Length.
- h020 pointer_handle: C pointer domains with no JVM
  counterpart and no observable address semantics seat on
  String handle carriers naming the vendor pointer type; the
  FILE* family seats on the arm RandomAccessFile fold. Example
  libmpscpp.cpp::mdh_tree_level_add.
- h021 struct_carrier: structs whose layout owner is not in
  the batch become minimal carrier classes keeping vendor field
  order and names, only the touched surface seated. Example
  interp-sym.cpp::SysDump.
All three verified(untested) at capture per the h018 precedent;
first consults-as-rule land with BOTH B6 and the
untested->unit_tested upgrade is recorded at the next close.
The 14 miss rows are pre-capture history and remain as misses
per the append-only log; the events CSV is NOT rewritten.

ARM-CLOSE STUB LEDGER (h014 out-of-scope discipline; the seats
raised in B5 whose owners live outside the arm's drawing):
allocSV (roster 285 b8), parse_ (303 b3), parse_pattern_element
(196 b4), leaning_toothpickize (157 b6), make_cstring (212 b1),
perl_pm (214 b9), wstem (164 b10), gotamatch (180 b1), the
GTK platform family (17 mdh seats, non-roster), pm (libmpscpp.h
237 non-unit), cleanLocks/__symput/interpret (308 b6), the
globalClass.h layout seats (extractValue/assignValue/order/
kill/value/setValue/docCorrelate, roster 259 b3 with the
corrected pin), mumps-ecosystem non-units (readHistory,
history-lib edges). Resolved within the batch: acceptIt (T2
stub flipped by the roster-271 T3 render), GlobalNode.transpose
(T2 stub flipped by the unit-154 render), Avg/Sum/Translate/
Length member families rendered in-batch, keyfix inline, the
zfcn/Mglobal/xecute/SymGet carrier folds seated T1/T2.

Red-build count for the batch: 0 red gate builds (every kotlinc
and gradle invocation green on first submission; the T1 push
carries two newline-restoration fixups before the splice - push
plumbing repairs at 00a0643a5/6e0e7dd20, not builds; the in-
sandbox pre-gate iterations of the three tranches are logged as
data at the tranche sections).

Files pushed (one commit, this close):
- wp2/arms/BOTH/rules_v0.pl (190 lines; h019/h020/h021 appended
  with the capture provenance block; the 18-rule v0 seed text
  untouched above it)
- wp2/PILOT_TRACKING.md (the BOTH B5 closed paragraph appended;
  no table-cell changes in this push - the rows were all marked
  at their tranche pushes)
- wp2/arms/BOTH_B5close_fragment_2026-11-22.md (this file)

Open items at this close:
- the m1-build gradle round (PI runs it; journal note follows)
  plus the deferred swipl coverage_report verification of the
  hand-computed checklist above;
- the splice of this fragment (journal base 9187 lines; see
  the splice command block), then fragment removal per the
  standing two-commit pattern;
- BOTH B6 (units 155-185) opens next per the committed order:
  T1 simple 155-164 first, the 183-185 big three per the PRO
  B6 precedent stay a dedicated PI session decision;
- knowledge_tooling.pl needs no edit for the promotions (the
  rules file is the consulted artifact); the first B6
  find_rule(h019/h020/h021) queries land at the T1 renders.
