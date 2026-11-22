### DAF B6 close - queue-note repair, 2026-11-22

REPAIR (logged as data; deviations and errors are recorded, never
silently applied): the "next protocol step" sentence of the DAF B6
close section (and the matching PILOT close paragraph sentence,
repaired in the same push) reads "BOTH B5 T2 (core 134-143)
opens". That is wrong on the facts: BOTH B5 T2 was already
rendered, pushed, gated, spliced and fragment-removed on
2026-11-21 - push eb6d33c (10 renders plus the BothCRuntime and
BothStubs T2 seat edits; specs expx.dfy, parse_count.dfy and
strmanip.dfy, suite 30 verified 0 errors; events +86 rows to
157) with journal splice 85caa1a at 2026-11-21T02:24:23Z, per
the BOTH B5 tranche-2 journal section of 2026-11-21. The queue
notes of the DAF B6 T1/T2/T3 and big-three records ("BOTH B5 T2
stays first in the queue once DAF B6 closes") were stale when
written: the T2 tranche ran and closed before the PI queue
decision of 2026-11-22 was recorded in the DAF sessions' working
notes, and the handoff chain (the session knowledge tracker and
this close prose) propagated the stale note without re-verifying
master. The intent of the PI decision was implemented factually
either way: DAF finished completely before any further BOTH
work began after the decision.

Provenance note: the staleness was discovered at the BOTH B5 T3
tranche open (sandbox rebuild), when the fresh repo copy showed
the both-tree at 21 classes with the T2 journal section already
spliced and no fragment files on master; both facts were then
verified against the commit history (eb6d33c author date
2026-11-21T02:22:06Z; splice 85caa1a; fragment removal in the
same splice chain).

Corrected next protocol step, per the committed batch-5 order
and the PI queue decision: BOTH B5 T3 (complex stratum, units
144-154) - the DAF B5 T3 complex set rendered against the BOTH
arm with the T1/T2 instrumentation intact (rules_v0.pl
consultations into BOTH_knowledge_events.csv plus the Dafny
spec workflow under wp2/arms/BOTH/spec/).
