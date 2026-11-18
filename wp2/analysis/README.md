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

## Input contracts (all committed data; readers fail loudly otherwise)

1. **Events CSVs**: `wp2/logs/<ARM>_knowledge_events.csv`
   (LF endings), columns exactly: `kind,target,unit,timestamp`, with
   kind in {reuse, hit, miss}. PRO's contract is verified (3690 rows);
   OTHER event-producing arms (BOTH; DOC cookbook-consult logs) must
   match this schema or a registry entry must be logged before analysis.
2. **Roster**: `roster/units.csv` (CRLF endings) as sampled and
   committed in WP1; the readers strip CR. Row 1 is the header; rows
   2-309 are the 308 units.
3. **Per-unit checks**: `wp2/analysis/data/per_unit_checks.csv`,
   columns: `unit_id,arm,code_check,output_check,runtime_check,target_check`,
   each status in {todo, evaluated-pass, evaluated-fail} or unevaluated
   per PER_UNIT_CHECKS.md vocabulary. E2 tests run on evaluated units
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
