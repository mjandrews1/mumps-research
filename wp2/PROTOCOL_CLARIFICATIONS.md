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
