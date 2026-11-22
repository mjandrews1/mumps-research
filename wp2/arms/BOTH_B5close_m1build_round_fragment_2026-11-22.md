### BOTH B5-close m1-build gate round + coverage_report verification - 2026-11-22

The deferred B5-close m1-build round (recorded at the B5 close as
"REPORTED BY THE PI AS NOT YET RUN") is now RUN AND GREEN, Mark's
m1-build (LXD container on the container host), user builder:

- git pull dec1620..7a900e8 fast-forward (185 files, +37714/-320;
  the tree advanced past the B5 close through B6 T1, B6 T2 and
  their splices - the round gates the whole current BOTH tree).
- gradle --no-daemon build in wp2/arms/BOTH: BUILD SUCCESSFUL in
  15s, 5 actionable tasks, zero fix rounds (Gradle 9.9.1 at
  ~/builder/gradle-9.9.1; single-use daemon per the flag).

The deferred swipl coverage_report verification of the B5-close
hand-computed checklist (run from the repo root, consult goals
from stdin per the tooling pitfall rules, knowledge_tooling.pl +
rules_v0.pl under wp2/arms/BOTH/):

- rules in base: 21; triggering situations: 174 (hits 160,
  misses 14); hit rate 92.0%; reuse rate 100.0%.
- VERIFICATION RESULT: the 14 misses reconcile EXACTLY with the
  B5-close hand-computed checklist ledger - class_member_carrier
  x9 (units 129/131/133/134/136/137/138/149/154), pointer_handle
  x3 (the mdh_tree_level_add overloads, units 126-128),
  struct_carrier x2 (units 142 SysDump stab chain, 144 sym.h
  misc) - every one captured at the B5 close (h019/h020/h021
  promotions) and every subsequent consult answered reuse+hit;
  the tool reports each as "captured -> rule hXX; miss is
  pre-capture history". NO new miss rows in the B6 T1/T2 bands
  (the 21:40 and 22:10 bands are pure reuse+hit).
- The hit/reuse counts absorb the post-B5 event rows (B6 T1 + T2)
  per the tool's situation arithmetic; no rule-base change is
  warranted at this round (the h018-h021 verification upgrade
  discussion stays queued to the batch-close protocol as before).

The B5 m1-build round and swipl coverage_report pending items
are CLOSED. Both remaining tranche-level gates for B6 T1/T2
(macBook + m1-build) are green. B6 T3 (units 176-182) follows
next per the committed order.
