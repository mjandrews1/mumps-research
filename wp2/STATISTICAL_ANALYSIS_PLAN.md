# Statistical Analysis Plan (SAP): WP2 Five-Arm Study

Status: **Pre-registered amendment**, logged as registry entry C-004 in
`PROTOCOL_CLARIFICATIONS.md` (dated 2026-12-04). This document adds
concrete estimands, test statistics, exclusion rules, and multiplicity
control to the analysis intentions already committed in
`RESEARCH_DESIGN_C_To_Kotlin.md` §11.2 (hypotheses H2-H5), §11.1
(dependent variables and instruments), and the pre-registered subgroup
plan S1-S3 (§11.2). It does **not** add, remove, or re-aim any contrast.
Where this document and §11.2 differ in specificity, this document
governs; where they differ in substance, §11.2 governs and the conflict
must be logged as a new registry entry.

## 0. Timing and blinding statement

At registration of this SAP:

- **BASE:** 308/308 graded (B1 31 units; per-unit check columns pending).
- **PRO:** 308/308 translated; study closed (hit rate 99.7%, reuse rate
  99.0%); grading data and event log committed.
- **DOC, DAF, BOTH:** no translation work has been performed; `todo`
  in `PILOT_TRACKING.md`.

The pairwise arm-level statistics in §3 are therefore registered before
any DOC/DAF/BOTH outcome exists. The PRO-vs-BASE contrast is registered
after PRO's completion: it is classified as **confirmatory for the
un-blinded arm pair only in the replication** (RSM/GT.M, per
`docs/REPLICATION_ROADMAP.md`), and as **specified exploratory** in this
study. This asymmetry is stated rather than hidden.

Single-researcher caveat (design §11.2 note) carries over: per-module
measures are the unit of analysis; a single researcher and single
codebase bound the strength of inference, and effect estimates with
confidence intervals are the primary reported quantity, p-values
secondary.

## 1. Units of analysis, pairing, and blocking

- **Unit of analysis:** the corpus unit (`roster/units.csv` row), N = 308.
- **Pairing:** every arm translates the same 308 units under Latin-square
  counterbalancing (`wp1/order_assignments.md`), so each unit provides a
  matched observation per arm. Missing cells (deferred units, per design
  "per-module deferrals reported") break matching; §4 defines how.
- **Blocking factors (pre-specified):** stratum (simple / core / complex),
  source language (`lang`: C compiler vs. C++ interpreter), and batch
  position (learning-curve covariate, H11).
- **Arm order:** batch position per arm per the committed Latin square;
  the B1 shared-prior arrangement (C-001) is accounted for by including
  a shared-batch indicator in the blocked models.

## 2. Endpoints (all map to §11.1 instruments)

| # | Endpoint | Type | Source instrument |
|---|----------|------|-------------------|
| E1 | Oracle correctness per unit (graded-ok vs. graded-deviation) | binary | translator/interpreter oracles |
| E2 | Per-unit check status (code / output / runtime / target) | binary per check | `PER_UNIT_CHECKS.md` (C-002) |
| E3 | Deviation count per unit | count (0, 1, 2, ...) | decision/deviation logs |
| E4 | Hit rate (hits / triggering situations, per batch close) | proportion paired with miss count | `coverage_report` (capture_criteria §4-5) |
| E5 | Reuse rate (reuses / hits, per batch close) | proportion | `coverage_report` |
| E6 | Effort per unit (wall-clock, journal-timestamped) | continuous, skewed | `arms/ARM_WORKING_NOTES.md` timestamps |
| E7 | Consistency of decisions per unit (decision-log agreement within arm, per §11.1 "Statistical test on decision logs") | count/proportion | per-arm decision logs |

## 3. Test statistics (binding)

### 3.1 Primary contrasts (H2-H5), binary and count endpoints

For each primary contrast on a binary endpoint (E1, E2):

- **Within-pair test:** McNemar's test (exact binomial variant when the
  discordant-pair count is < 25).
- **Effect estimate:** odds ratio with exact (Clopper-Pearson-based)
  95% CI from the matched pairs; risk difference reported alongside.

For count/ordinal endpoints (E3, E7):

- **Within-pair test:** Wilcoxon signed-rank on per-unit matched pairs;
  exact-null handling for tied cohorts (> 95% ties triggers reporting of
  the sign test alongside, not instead).

For continuous skewed endpoints (E6):

- **Within-pair test:** Wilcoxon signed-rank on paired differences;
  Hodges-Lehmann estimate with CI. Paired t on log(effort) reported as
  robustness.

For H5 (interaction, difference-of-differences on E3/E6):

- Per-unit paired differences computed within each factorial cell, then
  a Wilcoxon rank-sum (Mann-Whitney U) test on the two difference
  distributions, with the Hodges-Lehmann shift as the effect estimate.

### 3.2 Multi-arm endpoints (E4, E5) and the knowledge-reuse family

- Within-arm evolution across batch closes (e.g., PRO's B1 -> study-close
  reuse-rate trajectory): trend described, not tested; the arcs are
  monotone by construction once capture stabilizes, and the informative
  comparison is cross-arm at study close (next bullet).
- **Cross-arm reuse comparison at close (DOC vs. PRO hit/reuse rates,
  H4 instrument):** paired comparison at the stratum level (3 strata x
  2 arms), Fisher exact per stratum with Holm correction across the 3
  strata; pooled Mantel-Haenszel across strata as the summary. PRO-vs-BASE
  hit-rate contrast (E4, exploratory per §0) uses the same stratum-paired
  Fisher scheme.
- **Pre-capture history rule:** triggering situations that pre-date the
  capture of the rule that would have consulted them (the PRO units
  4/10/12/20/29 h018 case) are excluded from E4/E5 numerators and
  denominators in all tests, and reported descriptively in a footnote.
  This is the formalization of the "[captured -> rule h018; miss is
  pre-capture history]" annotation.
- **Verification-only / front-run units (PN-1, unit-43 precedent):**
  for cross-arm tests these units carry their outcomes (they are real
  translation outcomes for the arm); for E4/E5 they enter the coverage
  denominators as triggering situations and the reuse numerators per
  the owning-seat rule set, exactly as logged. No double counting: one
  situation per (unit, rule) per the coverage_report dedup semantics.

### 3.3 Subgroups and covariates (S1-S3, H11)

- S1/S2: primary contrasts re-estimated within `lang` subgroups with
  arm x lang interaction terms; reported regardless of outcome.
- S3: batch c/cpp mix enters the blocked model as a covariate.
- H11: learning-curve regression (effort ~ batch position per arm)
  confirms counterbalancing adequacy; position term tested per arm,
  not pooled.
- Stratum: all primary contrasts also reported within stratum
  (simple/core/complex) as secondary; pooled result stands.

### 3.4 Multiplicity control (binding)

- The four primary contrasts (H2-H5) on their designated primary
  endpoint family (E1/E2 for H2/H3; E4/E5 for H4; E3/E6 for H5) receive
  **Holm-Bonferroni** across the four; each is tested at its step-down
  alpha within its endpoint family.
- Secondary contrasts (H6, remaining pairwise), subgroup analyses, and
  robustness checks are reported with raw p-values and explicitly
  labeled secondary/exploratory; no multiplicity adjustment is applied
  and no secondary result is promoted.
- One-sided tests are pre-specified for the directional H2-H5
  alternatives (improvement); two-sided for secondary contrasts.

## 4. Exclusions, missing data, and analysis population

- **Primary analysis population:** all 308 units. Deferrals reduce the
  matched set for the affected arm pair only; tests are computed on the
  complete pairs, and the deferral count per arm pair is reported
  alongside every test result.
- **Pilot B1 (31 units):** calibration, not confirmatory. B1 is excluded
  from the confirmatory hypothesis tests (instrument seeding occurred
  during B1 by design); B1 is included in per-arm descriptive statistics
  and learning-curve analysis. Sensitivity analysis with B1 included is
  reported in an appendix.
- **Graded-deviation units:** included as failures on the relevant
  endpoint (E1); deviations are data, per the design (§7.4).
- **Verification-only units:** included per §3.2 (outcome real, coverage
  accounted at owning seat).
- **Missing check columns:** `PER_UNIT_CHECKS.md` statuses still `todo`
  at analysis time are reported as unevaluated, not imputed; E2 tests
  run on evaluated units only, with the evaluated fraction reported.
- **Excluded bug-fix events:** gate-fix rounds (detekt/compile fix
  commits) add no event rows and no E4/E5 contributions, per the
  established precedent; they are included in E6 effort if and only if
  they occur within the batch's timestamp window.

## 5. Reporting and reproducibility

- Every test result is reported with: test name, raw p, adjusted p where
  §3.4 applies, effect estimate with 95% CI, N units analyzed, N
  excluded, and N deferrals.
- Software: R 4.x for analysis scripts (coin exact/McNemar infrastructure),
  committed under `wp2/analysis/` with renv lockfile; run instructions in
  the analysis README; committed data (events CSVs, per-unit check
  exports) as the only inputs, no out-of-band data.
- Confidence level: 95% throughout.
- Deviations from this SAP discovered during analysis are logged as new
  PROTOCOL_CLARIFICATIONS entries with the analysis not re-run silently.
