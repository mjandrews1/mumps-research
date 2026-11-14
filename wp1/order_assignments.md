# Pre-Registered Latin-Square Order Assignments (M1 commitment)

**Committed: 2026-11-14, before the pilot batch. Do not revise after the pilot
begins.** Executive deviations (blocked progress, unavoidable rework) are
logged as data in the per-arm work journals, never silently repaired here.
Source: `order_assignments.json` (machine-readable twin of this file);
construction defined in RESEARCH_DESIGN §12.3.

Batch count is now fixed at **10**, tied to the committed sampling protocol
(`tools/examples/EXAMPLE_protocol_seed-20261113.json`, seed 20261113, 308
function units, strata 102/102/104).

| Cycle → | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 | 10 |
|---------|---|---|---|---|---|---|---|---|---|----|
| BASE | B1 | B2 | B3 | B4 | B5 | B6 | B7 | B8 | B9 | B10 |
| DOC  | B2 | B3 | B4 | B5 | B6 | B7 | B8 | B9 | B10 | B1 |
| PRO  | B3 | B4 | B5 | B6 | B7 | B8 | B9 | B10 | B1 | B2 |
| DAF  | B4 | B5 | B6 | B7 | B8 | B9 | B10 | B1 | B2 | B3 |
| BOTH | B5 | B6 | B7 | B8 | B9 | B10 | B1 | B2 | B3 | B4 |

**Fresh-attention rotation:** the arm receiving the researcher's first
working session of each cycle rotates BASE → DOC → PRO → DAF → BOTH → BASE
across cycles 1–10, so no arm systematically gets the freshest hours.

**Rationale (from the design):** with the previous sequential schedule the
Prolog factor was perfectly correlated with calendar time. The cyclic
construction makes every arm encounter every batch at every stage of the
learning curve, on average identically.

## Interpretation

- "Cycle" = one monthly cadence unit; within a cycle each arm advances one
  batch along its assigned order.
- Each batch is one block in the analysis; per-batch metrics are recorded
  identically in every arm (§7).
