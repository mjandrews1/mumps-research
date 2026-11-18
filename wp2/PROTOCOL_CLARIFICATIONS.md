# Protocol Clarifications Registry

Pre-registered instruments — `wp1/order_assignments.md`,
`wp1/capture_criteria.md`, and the oracle manifests — are committed
before the pilot and never revised afterward. Questions, clarifications,
and deviations that arise during execution are logged here **as data**,
per the design (§7.4: deviations from the pre-assigned order are logged
as data, not silently repaired) and `wp1/order_assignments.md`'s own
instruction. Each entry states the observation, the clarification, the
effect on the committed schedule, and the methodological consequences.

| # | Date | Title | Status |
|---|------|-------|--------|
| C-001 | 2026-11-16 | Pilot B1 vs. Latin-square slots | Resolved (before cycle 1) |
| C-002 | 2026-11-16 | Per-unit check definitions formalized | Resolved (definition written) |
| C-003 | 2026-11-16 | PRO early B2 execution | Active (deviation logged) |
| C-004 | 2026-12-04 | Statistical Analysis Plan specified before pending-arm outcomes | Registered (SAP committed) |

---

## C-001 — Pilot B1 vs. Latin-square slots (resolved 2026-11-16)

**Observation.** WP2 (design §8; `wp2/README.md`) pre-registered the
pilot as batch **B1 translated in all five arms** (Nov 2026); B1 has been
executed in BASE and PRO (31/31 each; DOC, DAF, BOTH pending). The
committed Latin square (`wp1/order_assignments.md`) places each arm's B1
at a different cycle position: BASE cycle 1, BOTH cycle 7, DAF cycle 8,
PRO cycle 9, DOC cycle 10. As written, four arms would either
re-translate B1 at their assigned slot (duplicating pilot work) or leave
their row incomplete. Flagged in `CADENCE.md` as an open protocol
question to resolve before cycle 1.

**Clarification (logged as data; `order_assignments.md` unchanged).**

1. The pilot's B1 execution **credits each arm's B1 cell** in its
   committed row. No arm re-translates B1 in a later cycle.
2. Each arm executes its **committed row order with B1 removed**. This
   preserves each arm's relative batch order exactly; the only deviation
   is B1's position, hoisted from its assigned cycle to the pilot
   (position 0) in four arms (DOC, PRO, DAF, BOTH). BASE's B1 sits at its
   assigned position (first in its row) and is therefore not a deviation.
3. Nine batches remain per arm, so translation completes in
   **cycles 1-9 (Dec 2026 - Aug 2027)**. **Cycle 10 (Sep 2027) becomes a
   contingency/buffer cycle**: deferred-unit catch-up, micro-replication
   preparation, and per-unit check backfill. The analysis window
   (Oct-Dec 2027, Q5) is unchanged.

**Resulting per-cycle batch map (cycles 1-9):**

| Arm | C1 | C2 | C3 | C4 | C5 | C6 | C7 | C8 | C9 | C10 |
|-----|----|----|----|----|----|----|----|----|----|-----|
| BASE | B2 | B3 | B4 | B5 | B6 | B7 | B8 | B9 | B10 | buffer |
| DOC | B2 | B3 | B4 | B5 | B6 | B7 | B8 | B9 | B10 | buffer |
| PRO | B3 | B4 | B5 | B6 | B7 | B8 | B9 | B10 | B2 | buffer |
| DAF | B4 | B5 | B6 | B7 | B8 | B9 | B10 | B2 | B3 | buffer |
| BOTH | B5 | B6 | B7 | B8 | B9 | B10 | B2 | B3 | B4 | buffer |

(B1: all arms, pilot, Nov 2026 = position 0 for every arm.)

**Methodological consequences (recorded for the analysis phase).**

- **Latin property preserved per arm:** each arm still translates each
  batch exactly once, in committed relative order. No batch is dropped or
  duplicated.
- **B1 becomes a shared-prior batch:** all five arms encounter B1 at the
  same, earliest experience level (position 0). Cross-arm comparisons on
  B1 are therefore a fully paired baseline — cleaner than the square
  intended — at the cost that B1 no longer spans learning positions.
- **Batch-position covariate:** the H11 learning-curve analysis and the
  batch-position covariate (§10.3) use each arm's *actual* ordinal
  position: B1 = 0 (pilot) for every arm; remaining batches = cycle
  number (1-9) per the map above. The covariate, not the square's nominal
  positions, carries the learning-curve adjustment.
- **Position balance is degraded, not destroyed:** with B1 fixed at
  position 0, the remaining nine batches no longer occupy each position
  exactly once across arms (e.g., B2 is translated by BASE and DOC in
  cycle 1 but by PRO in cycle 9; BASE and DOC rows coincide once B1 is
  removed, an unavoidable consequence of crediting the pilot). The
  imbalance is logged here; the pre-registered batch-position covariate
  is the instrument for absorbing it. The fresh-attention rotation
  (BASE -> DOC -> PRO -> DAF -> BOTH) is unaffected and applies as
  pre-registered.
- **BASE's early B2 opening (Nov 2026)** is a schedule fact recorded in
  `wp2/PILOT_TRACKING.md`; actual start/close dates are logged per unit
  and the batch-position covariate uses actual completion ordinals.

**Files touched by this entry:** `CADENCE.md` (open-question section
resolved; cycle mapping updated). No pre-registered file was modified.


---

## C-002 — Per-unit check definitions formalized (2026-11-16)

**Observation.** The four per-unit verification columns (code-check,
output-check, runtime-check, target-check) entered use with batch 2 on
2026-11-15 without a written definition; units 50-51 were graded
against the operative informal practice. On 2026-11-16 the B1 table was
retrofitted with the same columns, making the definition gap material
for both batches and the pending B1 backfill pass.

**Clarification.** The operative practice is now written down unchanged
as `wp2/PER_UNIT_CHECKS.md`: code-check (static equivalence review,
design §9.1 code-review discipline), output-check (byte equivalence vs.
the frozen oracle, per `wp2/arms/ARM_WORKING_NOTES.md` §0.2),
runtime-check (edge/error-path/state exercise per design §9.3),
target-check (Kotlin target-spec compliance per design §7.2). The
researcher confirmed (2026-11-16) that the written definitions match
the practice under which units 50-51 were graded; no graded value is
reinterpreted or changed. The definition governs the B1 backfill pass
and all batches from B2 onward.

**Files touched by this entry:** `wp2/PER_UNIT_CHECKS.md` (new);
`wp2/PILOT_TRACKING.md` (checks note now points at the definition).
No pre-registered file was modified.

---

## C-003 — PRO's early B2 execution (logged 2026-11-16, active)

**Observation.** Per C-001, PRO's committed row order (B1 hoisted to
position 0) is B3, B4, ..., B10, B2; B2 is PRO's cycle-9 batch. On
2026-11-16 the PRO arm translated B2 unit 32
(libmpscpp.cpp::global::Extract; build green) before the C-001 cycle
map was propagated into the arm's working loop. The PI elected
(2026-11-16) to retain the translation and continue B2 as a logged
deviation rather than revert.

**Clarification (logged as data; no pre-registered instrument is
modified).**

1. PRO's early B2 translations are **provisional data**: retained in
   the repo and recorded in the PRO arm journal, and **re-verified at
   PRO's committed B2 position (cycle 9)** - each early unit receives
   its full per-unit check pass (wp2/PER_UNIT_CHECKS.md) there before
   the arm's B2 grading is finalized. The cycle-10 buffer (C-001) is
   the fallback slot if the re-verification pass overruns.
2. The batch-2 table's four check columns track the BASE reference
   translation (C-002); PRO's early work is NOT recorded in them.
   Unit 32's code-check was briefly set to `translated` in error
   (2026-11-16) and is reverted in the same-day registry commit.
   PRO-arm progress lives in wp2/arms/ARM_WORKING_NOTES.md and
   wp2/logs/PRO_knowledge_events.csv.
3. Ordinal accounting follows C-001: the batch-position covariate
   uses actual completion ordinals; each early PRO B2 unit records
   both its actual ordinal and its committed position (cycle 9).

**Methodological consequences (recorded for the analysis phase).**

- The Latin property per arm is preserved in kind - each batch is
  still translated exactly once, and no committed relative order is
  otherwise altered - but B2's position imbalance deepens: PRO now
  encounters B2 at actual ordinal ~1 instead of cycle 9.
- PRO's early-B2 units are flagged for a **practice-effect
  sensitivity analysis** (compare against the arms that meet B2 at
  its committed early position, BASE/DOC): if the study's arm-level
  B2 metrics show a learning-position effect, the PRO early-B2 run
  is the identifiable contaminated cell.
- The re-verification pass at cycle 9 guards against drift; the
  early translations' final graded values are those of the committed
  position, not of the early run.

**Files touched by this entry:** `wp2/PILOT_TRACKING.md` (unit-32
check column reverted; batch-position note), `wp2/arms/ARM_WORKING_NOTES.md`
(PRO journal deviation note). No pre-registered file was modified.
---

## C-004 — Statistical Analysis Plan specified before pending-arm outcomes (registered 2026-12-04)

**Observation.** Design §11.2 pre-specifies the primary contrasts
(H2-H5) and the language subgroups (S1-S3) at the level of intent
("paired per-module comparisons; blocked model with arm term",
"statistical test on decision logs"), but commits no concrete test
statistics, effect estimators, exclusion rules, or multiplicity
control. At this date BASE and PRO are complete and closed; DOC, DAF,
and BOTH have produced no translation outcomes (todo across
PILOT_TRACKING.md).

**Clarification.** A Statistical Analysis Plan is registered as
`wp2/STATISTICAL_ANALYSIS_PLAN.md`, specifying: matched-pair tests
per endpoint type (McNemar exact / Wilcoxon signed-rank with
Hodges-Lehmann / batch-paired Fisher with Mantel-Haenszel pooling for
coverage endpoints); Holm-Bonferroni across the four primary
contrasts; the pre-capture-history exclusion rule (the PRO h018
units 4/10/12/20/29 modeled formally); the treatment of front-run and
verification-only units (PN-1, unit-43 precedent: real outcomes,
one situation per unit-rule); pilot-B1 exclusion from confirmatory
tests with sensitivity appendix; and reporting requirements
(effect estimate with CI, N analyzed/excluded/deferred, per test).

**Effect on the committed schedule.** None. The SAP is registered
before any DOC/DAF/BOTH outcome exists; within-study PRO-vs-BASE
comparisons are classified specified-exploratory in this study and
carry forward as the confirmatory pre-registration basis for the
RSM/GT.M replications (docs/REPLICATION_ROADMAP.md).

**Methodological consequences.** No pre-registered instrument
(order assignments, capture criteria, oracle manifests) is modified.
The SAP binds the analysis of outcomes not yet observed; any
deviation from it during analysis must be logged here with the
analysis not silently re-run. Because PRO is already complete, its
pairwise contrasts are not blinded; the SAP states this openly
(§0) rather than claiming full prospective registration.

**Files touched by this entry:** `wp2/STATISTICAL_ANALYSIS_PLAN.md`
(new). No pre-existing pre-registered file was modified.
