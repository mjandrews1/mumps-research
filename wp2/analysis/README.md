# WP2 Statistical Analysis (wp2/analysis/)

Implementation of the registered Statistical Analysis Plan
(`wp2/STATISTICAL_ANALYSIS_PLAN.md`, registry entry C-004). Committed per
SAP §5: all scripts run against committed data only; no out-of-band
data. Any deviation discovered during analysis is logged as a new
PROTOCOL_CLARIFICATIONS entry, not silently applied.

## Layout

- `R/sap_stats.R`: matched-pair test implementations (SAP §3):
  exact McNemar with Clopper-Pearson-based odds-ratio CI, Wilcoxon
  signed-rank with Hodges-Lehmann estimate and CI plus the >95%-ties
  sign-test rule, rank-sum difference-of-differences for H5,
  stratum-paired Fisher with Holm correction and Mantel-Haenszel
  pooling for E4/E5, Holm-Bonferroni across the four primary contrasts.
- `R/endpoints.R`: endpoint readers and derived quantities (SAP §2),
  including the pre-capture-history exclusion (§3.2) and the row-count
  semantics of the events log.
- `run_analysis.R`: orchestrator; fails loudly when a required input
  is missing (per the coverage_report hardening precedent).
- `selftest.R`: known-answer checks for the test statistics; run
  before any confirmatory use; exits nonzero on failure.
- `renv.lock`: bootstrap lockfile (R version + CRAN repository pinned;
  package hashes finalized by `renv::snapshot()` at first run; do not
  hand-edit).
- `data/`: per-arm pre-capture seed lists are committed here
  (e.g. `precapture_pro_seed.csv`); the per-unit check and deviation
  exports are generated here at each arm's close and committed once
  exported (`per_unit_checks.csv`, `deviations.csv`).
- `R/export_per_unit_checks.R`: generator that seeds
  `data/per_unit_checks.csv` from the roster (one row per roster
  unit per arm, keyed by `roster_row`, all four checks `todo`);
  statuses are filled by the per-unit check backfill pass
  (C-001 cycle-10 buffer, per `PER_UNIT_CHECKS.md`) and committed
  once evaluated.
- `R/export_doc_events.R`: regenerates
  `wp2/logs/DOC_knowledge_events.csv` from the DOC consult rows in
  the arm journal per the C-005 mapping (see "DOC consult rows in
  the arm journal" below). Idempotent; run at each DOC batch close
  and before analysis.
- `R/backfill_checks.R`: guarded stamping helper for the backfill
  pass (see "Backfill tooling" below).
- `oracle_verify.sh`: verifies both frozen oracles against their
  committed manifests (integrity prerequisite; not by itself
  per-unit evidence).

## Input contracts (all committed data; readers fail loudly otherwise)

1. **Events CSVs**: `wp2/logs/<ARM>_knowledge_events.csv`
   (LF endings), columns exactly: `kind,target,unit,timestamp`, with
   kind in {reuse, hit, miss}. PRO's contract is verified (3690 rows);
   OTHER event-producing arms (BOTH; DOC cookbook-consult logs) must
   match this schema or a registry entry must be logged before analysis.
   DOC is registered by C-005
   (`wp2/PROTOCOL_CLARIFICATIONS.md`): `DOC_knowledge_events.csv` is
   generated at each DOC batch close from the journal consult rows,
   with hit-reuse and hit-adapt each emitting a reuse+hit pair
   (adaptation counts as reuse), miss emitting a miss row, and
   target = cookbook entry id (H-nnn).
2. **Roster**: `wp1/study-subset/units.csv` (CRLF endings) as sampled and
   committed in WP1; the readers strip CR. Row 1 is the header; rows
   2-309 are the 308 units.
3. **Per-unit checks**: `wp2/analysis/data/per_unit_checks.csv`,
   columns: `roster_row,unit_id,arm,code_check,output_check,runtime_check,target_check`.
   `roster_row` is the authoritative unit key (units.csv physical
   row, header = line 1, units = rows 2-309): roster `id` strings are
   file-level and not unique (e.g. one id appears on 9 rows), so
   `unit_id` is descriptive only and never a join key (C-005).
   Each status is {todo, evaluated-pass, evaluated-fail} per
   PER_UNIT_CHECKS.md vocabulary. E2 tests run on evaluated units
   only (SAP §4) and report the evaluated fraction.
4. **Deviation exports**: `wp2/analysis/data/deviations.csv`,
   columns: `unit_id,arm,count,notes` (E3; count >= 0).
5. **Pre-capture exclusion list**: `wp2/analysis/data/precapture_<arm>_seed.csv`,
   columns: `unit,rule[,note]`, one row per (unit, rule) key to exclude;
   unit/rule values are exactly the strings in the arm's events CSV.
   The PRO seed (`data/precapture_pro_seed.csv`) carries the five
   h018 / preprocessor_conditional situations (roster units 4, 10, 12,
   20, 29; the `interp-arith.cpp` file-level string covers both roster
   units 10 and 20, both pre-capture). Arms with no such situations
   file their seed as a header-only file, never by omitting it.
   Note on counting: the committed events log's unit strings are
   file-level, so one (unit, rule) string pair can carry several roster
   units; rows are counted as-is (the log already holds one pair per
   (roster unit, rule) per the capture protocol), and string-level
   dedup is forbidden (it would collapse distinct units).

## DOC consult rows in the arm journal (C-005 tooling contract)

DOC consults stay journal rows (pre-registered practice; C-005 maps
them at close). To make the mapping mechanical, each DOC consult row
in `wp2/arms/ARM_WORKING_NOTES.md` uses this exact single-line
grammar (one line per consult; registered here before any DOC
consult exists, so no recorded value is touched):

```
- DOC consult: unit <unit_id>, entry <H-nnn>, outcome <hit-reuse|hit-adapt|miss>, timestamp <ISO8601>
```

`unit_id` is the roster id string; `entry` is the cookbook entry id;
`timestamp` is `YYYY-MM-DDTHH:MM[:SS]...`. `export_doc_events.R`
selects rows by the `- DOC consult:` prefix and fails loudly on any
prefix-matching line that does not match the full grammar. The
regenerated events CSV is header-only when no consults exist
(present, never omitted).

## Backfill tooling (cycle-10 buffer; C-002 discipline)

`R/backfill_checks.R` is the stamping helper for the per-unit check
backfill pass:

```
Rscript wp2/analysis/R/backfill_checks.R report
Rscript wp2/analysis/R/backfill_checks.R verify-roster
Rscript wp2/analysis/R/backfill_checks.R set <arm> <check> <roster_row> <evaluated-pass|evaluated-fail> [--force]
```

Scope rules, enforced in code where possible:

- The tool STAMPS evidence a human produced; it never infers a
  check result from a grade, an oracle run, or a build status. A
  `graded-ok` unit is not by itself `evaluated-pass` (C-002: the
  checks run in the verification pass; nothing is backfilled from
  grades).
- For output-check, the workflow per unit is: `oracle_verify.sh`
  (oracle integrity), then a per-unit matched-input byte comparison
  against the arm build, run and read by the researcher, THEN
  `set`. The arm-side oracle runners (design section 9.3, the
  finish-line tests) are not yet built; until they exist, the
  comparison is the manual harness exactly as `PER_UNIT_CHECKS.md`
  section 2 defines it.
- Refuses to overwrite a non-todo cell without `--force`;
  corrections that change an evaluated value are journal events,
  not silent edits.

## Run

```r
renv::restore()        # first run: renv::snapshot() after renv::install("coin")
Rscript selftest.R     # must pass before confirmatory use
Rscript run_analysis.R # writes results/ tables + result log
```

R 4.x, packages: `coin` (exact Wilcoxon/McNemar infrastructure);
everything else is base R. `run_analysis.R` emits for each computed
test: test name, raw p, adjusted p where SAP §3.4 applies, effect
estimate with 95% CI, N analyzed, N excluded, N deferred (SAP §5).

## Arm coverage at commit time

BASE and PRO data exist (grading; PRO events closed at 3690 rows).
DOC, DAF, BOTH: no outcomes yet; the scripts gate their comparisons on
arm data being present and otherwise report themselves as not-run,
never as null results.
