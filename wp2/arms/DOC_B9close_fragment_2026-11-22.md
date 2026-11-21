# DOC B9 close journal fragment, 2026-11-22

### DOC B9 close - coverage checklist (units 247-277) and events verification

Per the DOC arm discipline: construct list from the batch-9 unit
texts, normalized per section 2.4; for each canonical construct,
at least one cookbook entry matched. Counts are batch-9 unit
consult coverage (one consult per unit per the C-008 rule; the
per-unit consult rows live in the tranche sections of 2026-11-22
above; no rows are duplicated here per the B6-close
splice-repair lesson). Batch 9 sits at the units.csv
batch-column-9 block rows 248-278 (31 rows: 10 complex / 11
core / 10 simple); the DOC sequential ids run 247-277 in the
committed stratum order (simple first, per the B8 Unit-vs-row
conventions of the 2cfddec repair). PILOT_TRACKING does not
tabulate batch 8+ (the no-PILOT batch-8 rule; rows track here
only).

- File-scope globals: H-004 matched in 6 batch-9 unit consult(s)
- sprintf format-string idiom: H-009 matched in 1 batch-9 unit consult(s)
- Numeric promotion / truncation: H-010 matched in 2 batch-9 unit consult(s)
- Unsigned char domain mapping: H-012 matched in 1 batch-9 unit consult(s)
- Out-of-scope C function -> stub: H-014 matched in 12 batch-9 unit consult(s)
- Kotlin grammar vs quoted C source: H-015 matched in 2 batch-9 unit consult(s)
- Preprocessor conditional arms: H-018 matched in 7 batch-9 unit consult(s)

Constructs not arising in the batch-9 unit consults (H-001, H-002,
H-003, H-005, H-006, H-007, H-008, H-011, H-013, H-016, H-017):
no matched rows for these entries at the batch-9 consults; several
appear inside the batch-9 render bodies' KDoc ledgers (the parse_
goto-web fold of H-011, the H-012 byte-domain seats of btreeX, the
H-013 dead-tail ledgers of the T3 renders carry entry references
in the tranche paragraphs above) but the C-008 one-consult-per-unit
ledger counts only the unit consults; noted for the checklist
record, no action.

Consult outcome totals for the batch: 21 hit-reuse, 10
hit-adapt, 0 miss (31 consults, one per (unit, entry) per C-005;
every row carries a true in-session timestamp; roster id strings
per the batch-6 close repair 2 discipline; the batch-9
Unit-vs-row conventions held throughout - the T1 H-015
consult-gate repair lesson applied at all three tranches).

Per-unit consult coverage (unit -> entry, outcome; DOC
sequential id order):

- interp-arith.cpp::mps_exp2: H-018, hit-reuse
- libmpscpp.cpp::global::Translate: H-014, hit-reuse
- interp-arith.cpp::mps_log: H-018, hit-reuse
- interp-arith.cpp::mps_cos: H-018, hit-reuse
- interp-arith.cpp::mps_tan: H-018, hit-reuse
- libmpscpp.cpp::GlobalOrder: H-014, hit-reuse
- interp-sym.cpp::_random: H-010, hit-adapt
- compiler-write.c::<misc>: H-004, hit-reuse
- interp-bstring.cpp::bstrcat: H-014, hit-reuse
- interp-sym.cpp::_horolog: H-009, hit-adapt
- compiler-sys1.c::<misc>: H-004, hit-reuse
- interp-sym.cpp::<misc>: H-004, hit-reuse
- compiler-parse.c::ExpandName: H-014, hit-reuse
- interp-sym.cpp::Eval: H-014, hit-adapt
- libmpscpp.cpp::global::Avg: H-014, hit-reuse
- libmpscpp.cpp::mstring::FixGblRef: H-012, hit-reuse
- interp-service.cpp::<misc>: H-004, hit-reuse
- interp-strmanip.cpp::xindex: H-010, hit-reuse
- include/mumpsc/globalOrder.h::GlobalOrder: H-014, hit-reuse
- libmpscpp.cpp::mdh_tree_level_add: H-014, hit-reuse
- interp-arith.cpp::mps_floor: H-018, hit-reuse
- compiler-parse.c::parse_: H-014, hit-adapt
- libmpscpp.cpp::<misc-3>: H-004, hit-reuse
- interp-native-btree.cpp::btreeX: H-014, hit-adapt
- compiler-kill.c::kill_code: H-014, hit-reuse
- interp-native-btree.cpp::<misc>: H-004, hit-reuse
- mpstime.cpp::<misc>: H-018, hit-adapt
- interp-sql.cpp::sql: H-018, hit-adapt
- include/mumpsc/cgi.h::<misc>: H-014, hit-adapt
- libmpscpp.cpp::Jaccard: H-015, hit-adapt
- interp-rpm.cpp::perl_pm: H-015, hit-adapt

Miss rows: none recorded this batch (0 of 31 consults); no new
cookbook candidates raised at the batch-9 consults (the capture
gate stays with T1/T2/G1-G4; noted, no action).

Both-tranche review note (the close review of the three tranche
sections above): ONE prose-vs-consult divergence found, ledgered
here per the B9 T1 consult-gate discipline. The tranche-2 render
paragraph for libmpscpp.cpp::mdh_tree_level_add (unit 266) opens
with the entry mention H-015, while the unit's registered consult
row (2026-11-22T05:19:00Z, spliced with the T2 fragment and
carried by the events CSV) reads entry H-014, outcome hit-reuse.
The registered consult row stands: the events CSV row, the R
regen grammar and the C-008 ledger all key on the consult row,
and the 2026-11-21T02:58:00Z H-015 consult of the B8-era mdh
verification row remains the H-015 hit for this id string's
history. The paragraph's entry mention is a prose slip only (the
render body itself cites the MdhTreeLevelAdd6 template and the
roster row; no tracked-file content depends on it); noted, no
action. No other divergences: the remaining 30 unit paragraphs
match their consult rows on entry, outcome and roster id exactly
(the T3 consult-gate consistency check of the SqlIsc timestamp
collision repair held - the sql consult sits at 05:31Z by the
free-slot repair, ledgered in the T3 fragment).

Events regeneration verification: the full journal consult ledger
replayed through the C-005/C-008 export semantics (Python port of
export_doc_events.R, same grammar/validation/mapping contract):
719 consult rows -> 1436 event rows; the regenerated CSV is
BYTE-IDENTICAL to the tracked wp2/logs/DOC_knowledge_events.csv
(95733 bytes, LF, quoted 4-col, 1437 lines with header). Roster
membership (308 units), outcome vocabulary, and (unit, entry,
timestamp) uniqueness all validated. The same regen was verified
live at the T3 splice (Rscript on Mark's clone: 719 -> 1436 with
a clean working tree at commit 6456eb8); the close instrument
therefore lands NO tracked-file change (regen identical, per the
B7/B8-close pattern). VERIFICATION STEP (Mark): re-run Rscript
wp2/analysis/R/export_doc_events.R and
git status --porcelain wp2/logs/DOC_knowledge_events.csv
(expect clean) per the B2-close instrument verification
precedent; log any divergence as a protocol deviation.

Stub ledger at close (batch-9 standalone seats): T1 flipped
Stubs.mpsLog/mpsCos/mpsTan/mpsExp2 and Stubs.bstrcat to doc
delegates; T2 flipped Stubs.mpsFloor (the T1-resolved tag),
Stubs.Eval, Stubs.linepad and Stubs.linepad2, the Stubs.mult
CHAR-ARRAY overload (the String overload stays for the Cannon.kt
emission call sites), Stubs.globalAvg, Stubs.fixGblRef (the
earlier no-roster-row note was the misconstruction; row 263 owns
the body), Stubs.xindex (FindSym.kt in-place body, tag
corrected) and the Stubs.GlobalOrder inline (tmp2 carrier
widened String -> String?, source-compatible); T3 flipped
Stubs.parse_() zero-arg (the one-arg interp twin untouched,
arity-resolved), Stubs.jaccard and Stubs.perlPm. Raised this
batch: the 33 mstringPlus/mstringMinus/mstringTimes/mstringDiv/
mstringRem throwing seats (owner tags cite the batch-10 roster
rows; the mstring LIBRARY backends, the DocCorrelate front-run
precedent) and Carriers.kt ORDERNEXT 12 / ORDERPREV 13 with
Globals.ctrlc (the sigc extern). Seat moves and removals: the
MumpsMisc.kt setAlarm stub moved to the MpstimeMisc render (the
xindex seat-move precedent); the Btree.kt btreeX throwing stub
removed with its tag corrected (Btree dispatches to the render);
BtreeExterns.kt FoundBlock/Reinsert/FoundOffset and BtreeH.kt
BtreeStatic.gblDataFile landed with the btreeX render. Tag
corrections this batch: mpsFloor (batch-9 core row 268), the
ZfcnsH _horolog forward-tag (units.csv row 278 is batch column
9), the ZfcnsH _ascii forward-tag (no units.csv _ascii row
exists, grep-verified; the body rides the u258 ledger), the
fixGblRef no-roster-row note, the xindex owner note
(interp-strmanip.cpp owns it; interp-sym.cpp only calls), the
btreeX not-a-roster-unit note, and the LibmpscppMisc6 Horolog
call-site rebind (the vendor-fidelity delegate restoration).
Stays throwing at close: Stubs.sql for the interp-sql twin (the
SqlIsc.kt six-arg render keeps its overload; Sql.kt keeps the
five-arg), Stubs.floatstring (below-roster), Stubs.keyfix
(keyfix.h inline, not a corpus unit), Stubs.mpsCeil/mpsTrunc
(batch-10 roster rows), SyncClock (byte-identical twin cited, no
second definition).

Red-build count at the tranche gates: 0 (the whole-arm kotlinc
2.1.20 gate was silent, 0 errors / 0 warnings, at every tranche
close: 240 files at T1 rising to 250 at T2 and 260 at T3; the
pre-gate folds settled at the first compile of each tranche -
the mps_exp2 pow intrinsic at T1, the mpsFloor Unit return
convention at T2, the OPEN_CP/EMPTY const fold with the
mechanical table regeneration and the BtreeX double-bang
refactor at T3 - and the T3 gate was re-run silent after the
seat flips). gradle gate BUILD SUCCESSFUL at the tranche splices
(T2 12s, T3 15s; T1 green at its splice close).

Grading outcome: B9 31/31 translated (T1 simple 247-256 with
render push 7119724 and splice e2c21db, T2 core 257-267 with
render push 0b66512 and splice 6875907, T3 complex 268-277 with
render push 4455dbc and splice 6456eb8); five verification-fold
and overload-append renders among them (the Translate and
GlobalOrder overload appends, the mdh six-column overload, the
xindex tag-corrected in-place body, the kill_code reuse) - the
one-render rule held across the batch; the per-unit check columns
remain todo across B9 per the C-001 cycle-10 backfill decision.
PILOT_TRACKING unchanged at this close (the no-PILOT batch-8
rule; batch-9 rows tracked in this journal). Next protocol step
per the C-001 committed row order: DOC batch 10 (cycle 9), the
units.csv batch-column-10 block rows 279-309 (31 rows), opening
with the mstring operator backends that own the 33 stalled
throwing seats.
