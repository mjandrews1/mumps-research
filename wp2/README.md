
# WP2: Pilot Batch in All Arms

Pilot batch: **B1** (31 units, 1,944 LOC, 14 C / 17 C++, strata 10 simple /
21 core / 0 complex — per `wp1/study-subset/units.csv`, seed 20261113).

## Grading rules (fixed before the pilot)

1. **Interpreter-side units** (C++ interpreter + compiler units on the
   interpreter path): graded against the frozen interpreter oracle
   (`wp1/oracle/expected/`, commit 090a82f).
2. **Translator-side units** (C compiler sources: the `mumps2c` translator
   machinery): graded against the frozen **translator oracle**
   (`wp2/translator-oracle/`): the C output `mumps2c` produces for the 15
   oracle `.mps` cases plus characterization probes. A translated unit is
   equivalent if the Kotlin translation, integrated at the batch level,
   produces byte-equivalent translator output for all oracle inputs.
3. Byte-equivalence is required where the reference output is deterministic
   (it is: same input, same build). Any intentional non-equivalence must be
   logged as a protocol deviation with rationale BEFORE proceeding.

Both oracles are frozen before any pilot translation begins.

## Working structure

- `arms/BASE/`, `arms/DOC/`, `arms/PRO/`, `arms/DAF/`, `arms/BOTH/` —
  per-arm translations and logs. BASE runs first and seeds the heuristic
  log that later seeds the cookbook (DOC) and rule base (PRO/BOTH).
- `logs/heuristic_log_TEMPLATE.csv` — the decision-log schema, identical
  for all arms (construct, decision, rationale, unit, date, reused_from).
- `PILOT_TRACKING.md` — per-unit status per arm.

## Pilot sequence (per design §8 WP2)

1. Translate B1 in BASE; every heuristic decision logged.
2. Seed cookbook v0 (DOC) and rule base v0 (PRO) from the pilot log under
   the pre-registered capture criteria (`wp1/capture_criteria.md`).
3. Translate B1 in DOC, PRO, DAF, BOTH per arm-specific workflow.
4. Build Prolog query tooling (find_rule/1, record_reuse/2,
   coverage_report/0); instrument cookbook lookup logging.
5. Calibrate per-batch time tracking; refine schemas; write the
   instrumentation report.

Per-unit order within a batch: by `complexity_rank` ascending (simple
first), so the pilot exercises the easy end of the corpus first.
