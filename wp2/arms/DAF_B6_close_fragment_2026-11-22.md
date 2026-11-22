### DAF B6 close - coverage checklist (units 155-185), 2026-11-22

Batch-close section of the DAF arm. This arm draws no cookbook
and no rules base (section 3: the BASE heuristic log consults are
the ad hoc background instrument and wp2/logs/DAF_heuristic_log.csv
is the arm's own instrument), so the DOC/PRO close steps of entry
coverage against an artifact and events-CSV regeneration have no
object here; the close therefore tallies the batch's instrument
rows and states the no-artifact fact as data. Batch 6 (units
155-185) CLOSED: 31/31 translated 2026-11-22 in three tranches
plus the dedicated big-three session per the pre-registered
stratification and the PI queue decision of 2026-11-22 (T1
155-164 simple, push f467a08 with splice 84b53df; T2 165-175
core, push d9aa0be with the 1306b2d push-script repair and splice
66045d6; T3 176-182 complex minus the big three, push 69cba05
with splice 04cf3c0 and fragment removal b58f99f; the big-three
session 183-185, push 8639236 with splice b14103a and fragment
removal cadb7cf).

Gates: whole-arm kotlinc gate SILENT EXIT=0 at every tranche
close, both the gate copy and the fresh repo-copy submission
each time (75 -> 86 -> 93 -> 96 files across the batch); gradle
gate BUILD SUCCESSFUL at every close (m1-build for T1-T3; Mark's
MacBook, Gradle 9.9.1, at the big-three splice close - the
six-line always-true/false condition warning set of Interpret.kt
at 693/825x2/1004/2155x2 accepted, vendor-lattice faithful, same
acceptance as the prior tranches); dafny suite growth 55
verified 0 errors at T1 (the new spec/mps_log2.dfy, +5) -> 70 at
T2 (the new spec/extract.dfy, +15) -> 70 unchanged at T3 and at
the big-three (every remaining unit EXEMPT; no new spec after
T2; the suite verified per-spec on the spliced tree, 11 specs,
70 verified 0 errors).

Instrument state at this close: wp2/logs/DAF_heuristic_log.csv
at ids 1-188 contiguous, 11 columns, mixed CRLF/LF base with LF
appends per the T3-era convention (unchanged); the batch appends
rows 148-188 (41 rows: 31 unit rows - one per batch unit, this
arm's analog of the DOC single-line consult ledger; 4 seats
rows; 2 spec rows; 4 gate rows).

Unit-row outcome totals for the batch (one instrument row per
unit; the per-tranche prose sections carry the consult ladders,
seat ledgers and quirk ledgers): 17 hit-reuse, 14 hit-adapt,
0 miss (31 rows). Hit rate 31/31 = 100.0%; reuse rate 17/31 =
54.8%. Per tranche: T1 10 rows (4 reuse / 6 adapt), T2 11 rows
(6 reuse / 5 adapt, the manual header-ledger decision of the
interp.h residue row included), T3 7 rows (4 reuse / 3 adapt),
big three 3 rows (3 reuse / 0 adapt - the DOC big-three
scaffold transplant of 2026-11-21 onto the DAF gate seats, the
H-012 same-name family rule at the largest scale of the
program). LEDGER-SHAPE NOTE (logged as data): the tranche-1 rows
carry the short outcome tokens reuse/adapt in the decision
column; the hit-reuse/hit-adapt forms were adopted from T2 on;
the tally above normalizes both forms (semantics identical). No
miss rows and no capture candidate: this arm has no revision
artifact by design (the ad hoc factor level of the study), so
new resolutions stay journal notes; every arm-level divergence,
tag correction and seat adjudication of the batch rides the
per-tranche sections instead.

Red-build count for the batch: 0 red gate builds (every kotlinc
and gradle invocation at every tranche and session close is
journaled green on first submission). Pre-push in-sandbox
iterations logged as data (the designed catching point): the T1
wording riders fixed at T2 (no compile error); the T2 CSV
push-script truncation repaired at 1306b2d (a tooling failure
caught by the blob-sha verify, not a build); the two T3 escape
fixes caught by the pre-gate compile (the Match.kt punct-table
escape set; the ReadCode.kt dollar-template escape); the
big-three Interpret.kt SYMTAB_ERR/DATABASE collateral of a TAB
replace fixed pre-push.

ARM-CLOSE SEAT LEDGER (this closes the arm: per the binding
order_assignments.json Latin square the DAF arm cycles B4-B6
only): with batch 6 closed the DAF arm is COMPLETE - units 94-185
(batches 4-6, 30 + 31 + 31 = 92 units), every unit translated
and gate-green. The raised named-owner seats that cite roster
cells or owners OUTSIDE the arm's drawing stay raised at arm
close, the honest UNRESOLVED record per the H-014 out-of-scope
discipline: substrDaf (interp-strmanip.cpp::substr, b7),
mergeDaf (b8 row 226), the mps decimal-family seats of the b9
and b10 and kin rows, inoutInterpDaf (the interp-parse.cpp
4-arg b10 sibling; the WriteCode inoutDaf names the b2 3-arg
twin), wstemD (b10; the tag-corrected stub STAYS), replaceDaf
(builtin.h API entry, not a corpus unit; the bifs-only seat),
serviceInterfaceDaf (units.csv row 187, b7 - the TAG CORRECTION
carried by the raise), gtkTreeAddD (row 83) and the gtk*D
library glue family (external C library owners), the POSIX
platform family ftokDaf/msggetDaf/msgrcvDaf/socketDaf/sendtoDaf/
closeDaf (not corpus), getstr2Daf and prnterrDaf (no corpus
unit drawn by this arm), numcompDaf/addDaf/subDaf/strxcatDaf
and the variableNotFound family (non-roster owners). Their
owners live in the batches drawn by the DOC, PRO and BOTH arms.

DAF arm instrumentation calibration snapshot (arm close, per
section 5; the DOC precedent is the B2 snapshot of 2026-11-19):
heuristic-log rows 188 (ids 1-188 contiguous; source column: 163
BASE-log-cited rows, 25 manual-decision rows); knowledge
artifact count 0 by design (cookbook/rule-base growth is not a
factor level of this arm); consult structure = the BASE-log
row-cite ladders of the per-tranche prose (each unit row's
rationale names its anchors); events CSV: none (not applicable
to this arm); red-build count 0 at the gates (the pre-push
iterations and push repairs itemized above are logged as data);
grading outcome: 92/92 translated and gate-green; per-unit
check columns remain todo per the C-001 cycle-10 backfill
decision (the four columns track the BASE reference translation
per C-003.2).

Next protocol step per the committed order and the PI queue
decision of 2026-11-22: BOTH B5 T2 (core 134-143) opens; the
BOTH arm was opened at B5 T1 and its T2 was displaced per the PI
instruction that DAF finishes first.
