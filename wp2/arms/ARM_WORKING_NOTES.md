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
