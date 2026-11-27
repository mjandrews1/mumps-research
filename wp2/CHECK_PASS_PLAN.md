# Per-Unit Check Pass Plan (C-002 instrument execution)

**Status:** Plan of work, PI-approved 2026-11-27 (all four scoping
decisions ruled same-day; registry entry C-016 carries the instrument
amendment). This plan executes the checks defined authoritatively in
`wp2/PER_UNIT_CHECKS.md` (C-002). It does not redefine any check.

## 1. Scope and rulings (PI, 2026-11-27)

- **Instrument:** `wp2/analysis/data/per_unit_checks.csv` is extended
  from two arms (BASE, PRO) to all five arms (BASE, DOC, PRO, DAF,
  BOTH), 308 rows per arm, 1540 data rows total. The extension is
  logged as SAP Amendment 2 / registry entry C-016. Rows for DOC,
  DAF, and BOTH start `todo` on all four checks, identical to the
  registered rows.
- **Structure:** check-type sub-passes, not per-unit passes. All
  arms' code-checks first, then output-checks, then runtime-checks,
  then target-checks. Rationale: each check type shares one method,
  so a sub-pass holds method constant and lets the PI audit one
  method at a time.
- **Executor:** agent-executed, with PI spot audits (S5). The
  operative precedent is units 50-51 (scanblocks, embed), graded-ok
  on all four checks under the practice later formalized in
  `PER_UNIT_CHECKS.md`.
- **BASE rows 52-308:** leave `todo` (no BASE render exists to
  check; the BASE arm translated only the B1-B2 rows, 55 kt files).
  Addressed in SAP Amendment 2, not silently.

## 2. Sub-pass order

### S1 code-check (opens the pass)
Side-by-side structural/semantic review of each arm's render against
the pristine C/C++ vendor unit, per the faithful-quirk discipline:
every branch, operator, side effect, and vendor quirk accounted
for; comment/code mismatches, uninitialized reads, silent
promotions, dead parameters preserved deliberately and noted. The
arm must build warning-clean. Missing or invented behavior =
`graded-deviation` with the arm journal entry.

- Unit set per arm: units.csv rows 2-309 (308 units), matched by
  unit id string, never by journal ordinal (the C-015 lesson; PRO
  journal numbers are crosswalk ids).
- Method: render file(s) read against the vendor source region
  cited in the arm journal's unit entry; the journal entry's own
  claims (seats, quirks, h-rule notes) are verified, not trusted.
- PILOT and CSV flips: `graded-ok` / `graded-deviation` per unit.

### S2 output-check
Byte-equivalent outputs vs. the frozen oracles (interpreter units ->
`wp1/oracle/expected/`, 15 cases; translator units ->
`wp2/translator-oracle/expected/`, 16 cases). Oracles are immutable
since M1; they are characterization suites over whole-program runs,
so:

- **Opener deliverable: the unit-to-case coverage map**
  (`wp2/analysis/data/unit_case_coverage.csv`): for each roster
  unit, the oracle cases that exercise it (from the case sources
  and the run logs), or `none`.
- Units covered by at least one case: run the arm build against the
  case set, byte-compare against `expected/`, grade per unit.
- Units with no case coverage: `deferred`, reason "oracle coverage
  gap" logged in the arm journal. Deferrals are reported data; the
  oracle is NOT extended (immutability rule).

### S3 runtime-check
Edge and error-path exercise per design section 9.3: boundary and
degenerate inputs, error paths, state interactions; unit,
regression, and property tests. No crash, hang, or uncaught
exception on inputs the original handles. Divergence at an edge =
`graded-deviation` with the edge case recorded.

- Tests are committed per arm under the arm's test tree with the
  arm's existing build wiring (gradle test task for DOC/BOTH; the
  PRO detekt/jacoco rig already runs test tasks on m1-build; DAF
  per its rig).
- Oracle-covered units get regression tests derived from the case
  inputs; uncovered units get agent-designed edge suites (seeded,
  journal-noted).

### S4 target-check
Kotlin target-spec compliance per design section 7.2: idiomatic
Kotlin, null safety, immutable data where appropriate, no
transliterated C idioms. Functional equivalence alone does not
pass. Graded with a per-unit note; `graded-deviation` requires the
specific idiom violation recorded.

## 3. Session discipline

- One sub-pass arm-tranche per session (roughly 30-60 units per
  working session, batch-aligned where natural), each closing with
  the arm journal entries, the CSV/PILOT flips, and a push.
- Push discipline unchanged: fragments + splice for existing large
  files (chunk-verified reads, blob-SHA spot checks); new files
  direct.
- No silent changes: any check outcome that contradicts a prior
  journal claim is parked to the PI before the flip, per the
  adjudication convention.

## 4. Recording rules

- PILOT columns track BASE only (rows 1-51 eligible; 52-308 stay
  `todo` per the ruling). Each other arm's results are recorded in
  `per_unit_checks.csv` (the SAP's committed instrument) and in the
  arm's journal at tranche close.
- Vocabulary per C-002: `todo` / `graded-ok` / `graded-deviation` /
  `deferred` (reason mandatory in the arm journal).
- Every flip is by roster row + unit id string.

## 5. PI spot audits

- Per sub-pass tranche close, the PI audits a blind sample (suggested
  10 percent, seeded draw from the roster; seed and draw recorded in
  the arm journal) re-checking the agent's grade. Disagreements
  override the agent grade and are logged as adjudication items.
- The PI may at any time re-grade any unit; later grade wins and the
  change is logged.

## 6. Sequencing summary

S1 code-check (all five arms, tranche by tranche) -> S2 opener
(unit-to-case map), then S2 output-check -> S3 runtime-check -> S4
target-check -> the five-arm write-up/analysis per the SAP.

## History

- 2026-11-27: plan drafted and PI-approved (four scoping rulings);
  instrument extension to five arms pushed as C-016 / SAP Amendment 2.
