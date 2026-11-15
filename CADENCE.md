# CADENCE — Authoritative Schedule Decision

**Date of decision:** 2026-11-15
**Decision:** Target completion date is **December 31, 2027**.
**Status:** AUTHORITATIVE. This decision supersedes the December 31, 2030
extension recorded in TODAY.md (2026-11-11) and adopted by
research_project_design_urgency_first.md. Those documents remain in the
repository as historical records; their schedules are no longer in force.

---

## Governing plan

- TRACKING_README.md is the governing schedule document: project period
  November 12, 2026 - December 31, 2027 (14 months), 260 target hours,
  quarterly milestones Q1-Q5.
- The urgency-first 50-month reorganization (2030 completion) is retired
  for scheduling purposes. Its methodology discussion (priority matrix,
  contingency planning) remains usable reference material.
- The three design documents (research_project_design.md, _revised.md,
  _urgency-first.md) are still pending consolidation; until that happens,
  schedule questions resolve to this file and TRACKING_README.md.

## Cycle mapping (Latin square)

The pre-registered Latin square (wp1/order_assignments.md) fixes the batch
count at 10 and defines one cycle = one monthly cadence unit; each arm
advances one batch per cycle along its assigned row.

| Period | Work |
|---|---|
| Nov 2026 | Pilot batch B1, all arms (BASE and PRO complete 31/31; DOC, DAF, BOTH pending). B2 opened early in BASE. |
| Dec 2026 | Cycle 1 opens |
| Dec 2026 - Sep 2027 | Cycles 1-10 (one cycle per calendar month) |
| Oct - Dec 2027 | Analysis window (Q5): comparative analysis, documentation, final report |

The fresh-attention rotation (BASE -> DOC -> PRO -> DAF -> BOTH -> BASE
across cycles 1-10) applies exactly as pre-registered; it is not affected
by this decision.

## Open protocol question (log in the work journal, resolve before cycle 1)

The pilot ran B1 in all five arms (per wp2/README.md). The Latin-square
rows assign BASE's cycle-1 batch as B1 but DOC/PRO/DAF/BOTH start at
B2/B3/B4/B5. Record as a protocol clarification whether the pilot
satisfies each arm's first-square-slot obligation (crediting BASE's B1)
and how the remaining rows shift, so per-batch metrics stay
cross-arm comparable. Do not revise order_assignments.md itself; log the
clarification as data, per its own instruction.

## Change protocol

Any future change to the completion date must, in a single commit:

1. update this file,
2. update TRACKING_README.md (period, hours, quarterly milestones),
3. state the impact on the cycle mapping above (batch count is
   pre-registered at 10 and changes to it are protocol deviations),
4. be logged as a protocol event in the relevant arm work journals.
