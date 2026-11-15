# Batch 1 Pilot Retrospective (arm BASE)

Date: 2026-11-15. Scope: Study Subset Batch 1, units 1-31 (simple stratum
complete, core complete, complex complete). Status: **31/31 graded-ok,
final build green.**

## Outcome

- 31 units translated C/C++ -> Kotlin, 1,953 physical LOC of pristine
  source (per units.csv), three packages (`mumps.compiler`,
  `mumps.interp`, `mumps.mdh`).
- 89 heuristic rows in `wp2/logs/BASE_heuristic_log.csv`: 77
  manual-decision, 4 empirical-verification (gcvt semantics, banner FP
  Bits, selector rerun, plus the $z escape scan), 3 build-failure-fix
  (rows 76, 82, 89), 1 build-hazard (row 80), remainder cross-cutting
  canonicalizations.
- Faithful-quirk ledger: unexpanded `VERSION` placeholder, comment/code
  mismatches (Hash "zap to 1's" writes 2's), uninitialized reads
  (tmp[0] in zcalls), per-TU macro inversions (gcvt_wrapper), dead
  parameters (SymFree `i`), atoll->int truncations, stored-EOF reads.

## Build-failure record (31 push/build cycles)

| unit | red builds | root cause | fix | log row |
|---|---|---|---|---|
| 28 interp-sym.cpp::_SymFree | 1 | C comma operator written through verbatim at statement level | `,` -> `;` | 76 |
| 30 interp-parse.cpp::patrn1 | 1 | KDoc quoted a C char set containing star-slash; `*/` closed the block comment mid-sentence | reworded comment, reference the constant | 82 |
| 31 libmpscpp.cpp::<misc-1> | 1 (3 simultaneous errors) | `$z` Kotlin template collision in zcall literal; for-loop parameter shadowing `var j`; `UShort + Short` has no overload | escape/rename/widen; also removed duplicate C entities (below) | 89 |
| all others (28 units) | 0 | — | — | — |

First-push yield: 28/31 (90%); every red build was repaired in a single
follow-up push. Prior-session escape incidents (LocalArray.kt NUL,
Input.kt NUL+space, Patrn1.kt raw NUL/Ð, MyFunc multi-escape) were all
caught BEFORE push by programmatic scans — none reached a red build.

## Process incidents (my tooling, not Mark's builds)

1. **Heuristic-log clobber (unit 28 cycle).** The connector's
   file-contents fetch is paged; reading the CSV through it returned a
   fragment, and pushing "fragment + new row" deleted rows 1-75. A
   second repair attempt through `open_url` hard-wrapped long lines.
   Final repair pushed a pristine local mirror (md5-verified against
   the pre-incident blob). Defense: never round-trip repo files through
   those fetchers; maintain a local mirror read through the filesystem
   tool; md5-verify every pushed blob at its commit SHA.
2. **Ref-update lag.** GitHub's refs API lags `push_files` by up to ~60s;
   two "push failed?" false alarms were actually cache staleness.
   Defense: poll `git/ref/heads/master` with sleep-retry, verify file
   content at the returned commit SHA, not at `master`.
3. **Duplicate C entities (unit 31).** I retranslated the 10-column
   mdh_tree_level_add and the svPtr singleton that unit 8 already owns
   (as GtkTreeStore handle + MdhRuntime.svPtr). Caught during red-build
   triage; duplicates deleted, one-owner-per-C-entity rule adopted.

## Recurring hazards (ranked by pilot evidence)

1. **Kotlin grammar vs quoted C source** — C char sets contain `*/`
   (row 82); C zcall names contain `$` (row 89). Both are now caught by
   a pre-push scan (control bytes, unescaped `$` outside comments,
   `*/` inside block comments).
2. **Escape corruption through tool payloads** — raw control/high bytes
   never literal; build `Char(nnn)` programmatically (rows 80, 43).
3. **Per-TU macro scoping** — same `#define` resolving differently per
   translation unit (gcvt_wrapper inversion, row 84); `ierr` vs `ierr1`.
4. **C primitives' silent promotions** — short/int comparisons, atoll
   truncation, comma operators (rows 13, 65, 76).

## Recommendations for Batch 2

- Keep the local-mirror + md5-verify push discipline (zero data-loss
  incidents since adopted).
- The three fix classes of Batch 1 (grammar collisions, overload typing,
  declaration shadowing) are all statically detectable: add a pre-push
  checklist item to scan for `for (x in ...)` shadowing and numeric
  extension-operator types.
- Batch 2 opens with 10 simple units (scores 4.0-10.2); the residual
  <misc-2> chunk (600 LOC, score 381.8) closes it as before.
- extract2 integration decision (units 6/7 stub) is RESOLVED in the
  Batch 2 opening commit: the StringBuilder overload now delegates to
  the unit-15 real translation (safe: Extract2 never writes its
  argument; see Stubs.kt).
