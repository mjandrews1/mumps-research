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
