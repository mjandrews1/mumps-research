### BOTH B6-close m1-build gate round + coverage_report verification - 2026-11-23

The batch-6 close PI rides are RUN AND GREEN, Mark's m1-build
(LXD container on the container host), user builder:

- git pull 7a900e8..9f46c11 fast-forward (16 files changed,
  +8487/-27; the container tree advanced from its post-T2
  splice base straight to the close-committed master - the
  round gates the whole current BOTH tree including the T3
  renders, the big-three renders, the rules_v0.pl upgrades, and
  the close-spliced journal).
- gradle --no-daemon build in wp2/arms/BOTH: BUILD SUCCESSFUL
  in 20s, 5 actionable tasks, zero fix rounds (Gradle 9.9.1 at
  the builder gradle path, single-use daemon per the flag).

The swipl coverage_report verification of the B6-close
hand-computed checklist (from the repo root, consult goals
from stdin per the tooling pitfall rules, SWI-Prolog 9.0.4):

- rules in base: 21; triggering situations: 258 (hits 244,
  misses 14); hit rate 94.6%; reuse rate 100.0%.
- VERIFICATION RESULT: the 14 misses are the SAME pre-capture
  history reconciled at the B5 close (pointer_handle x3,
  class_member_carrier x9, struct_carrier x2 - every one
  reported captured -> rule h019/h020/h021, miss is
  pre-capture history). ZERO new misses through the entire
  batch-6 ledger: every batch-6 band consult answered
  reuse+hit, per the close section's per-band ledger. The
  tool's situation arithmetic reconciles with the close
  verification exactly - 244 hits vs the cumulative
  BOTH_knowledge_events.csv (502 data rows = 244 reuse /
  244 hit / 14 miss). No rule-base change is warranted beyond
  the h019/h020/h021 untested -> unit_tested upgrades already
  landed in the close commit 5484c688 (PI decision of
  2026-11-23).

BOTH batch 6 is now FULLY CLOSED across all gates: renders
pushed and spliced, the close commit 5484c688, the
close-splice 9f46c11 (journal verified 10242 lines exact),
MacBook gradle green at every splice close, m1-build gradle
green at the close, coverage_report green. Next protocol step
per the committed row order: BOTH batch 7.
