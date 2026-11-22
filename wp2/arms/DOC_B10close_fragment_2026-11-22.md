# DOC B10 close journal fragment, 2026-11-22

### DOC B10 close - coverage checklist (units 278-308) and events verification

Per the DOC arm discipline: construct list from the batch-10 unit
texts, normalized per section 2.4; for each canonical construct,
at least one cookbook entry matched. Counts are batch-10 unit
consult coverage (one consult per unit per the C-008 rule; the
per-unit consult rows live in the tranche sections of 2026-11-22
above; no rows are duplicated here per the B6-close
splice-repair lesson). Batch 10 sits at the units.csv
batch-column-10 block rows 279-309 (31 rows: 10 complex / 10
core / 11 simple); the DOC sequential ids run 278-308 in the
committed stratum order (simple first, per the B8 Unit-vs-row
conventions of the 2cfddec repair). PILOT_TRACKING does not
tabulate batch 8+ (the no-PILOT batch-8 rule; rows track in
this journal only). With this batch the units.csv roster is
exhausted: 308 data rows, every one carrying a DOC arm
disposition.

- Canonical IO type: H-001 matched in 1 batch-10 unit consult(s)
- File-scope globals / banner ledger rows: H-004 matched in 7
  batch-10 unit consult(s)
- Char buffer / needle walk: H-007 matched in 3 batch-10 unit
  consult(s)
- Unsigned char domain mapping: H-012 matched in 2 batch-10
  unit consult(s)
- Out-of-scope C function -> stub: H-014 matched in 15 batch-10
  unit consult(s)
- Preprocessor conditional arms: H-018 matched in 3 batch-10
  unit consult(s)

Constructs not arising in the batch-10 unit consults (H-002,
H-003, H-005, H-006, H-008, H-009, H-010, H-011, H-013, H-015,
H-016, H-017): no matched rows for these entries at the
batch-10 consults; several appear inside the batch-10 render
bodies' KDoc ledgers (the H-010 Int-narrowing sites of the T1
SysLoad/LengthSym renders, the H-015 overload pairs of the T2
line1149/Mltoa/wstem renders and the T3 select/misc-4 twins, the
H-013 goto-web fold fossils and dead-tail ledgers of the
CompilerMumpsMain driver, the H-017 name-seat conventions of
the T3 renders carry entry references in the tranche paragraphs
above) but the C-008 one-consult-per-unit ledger counts only
the unit consults; noted for the checklist record, no action.

Consult outcome totals for the batch: 28 hit-reuse, 3
hit-adapt, 0 miss (31 consults, one per (unit, entry) per
C-005; every row carries a true in-session timestamp; roster
id strings per the batch-6 close repair 2 discipline; the
batch-10 Unit-vs-row conventions held throughout - the T3
complex rows 279-288 map to DOC ids 299-308, the reverse-order
block of the B10 roster, one row per unit, the T1 grammar
repair applied by construction in all three tranche
fragments).

Per-unit consult coverage (unit -> entry, outcome; DOC
sequential id order 278-308):

- libmpscpp.cpp::global::Length (unit 278, the :3330 std::string
  needle fold): H-007, hit-reuse
- interp-sym.cpp::SysLoad: H-001, hit-adapt
- libmpscpp.cpp::global::Length (unit 280, the :3354 char-star
  needle fold): H-012, hit-reuse
- interp-arith.cpp::mps_trunc: H-018, hit-reuse
- interp-arith.cpp::mps_ceil: H-018, hit-reuse
- libmpscpp.cpp::global::Piece: H-007, hit-reuse
- libmpscpp.cpp::mstring::Piece: H-007, hit-reuse
- interp-sym.cpp::_length: H-014, hit-reuse
- libmpscpp.cpp::global::Pattern: H-014, hit-reuse
- compiler-at-test.c::<misc>: H-004, hit-reuse
- libmpscpp.cpp::global::Justify: H-014, hit-reuse
- interp-sysfunc.cpp::unpad: H-014, hit-adapt
- include/mumpsc/defines.h::<misc>: H-004, hit-reuse
- libmpscpp.cpp::line1149: H-014, hit-reuse
- interp-arith.cpp::Mltoa: H-014, hit-reuse
- interp-stem.cpp::wstem: H-014, hit-reuse
- include/mumpsc/sysparms.h::<misc>: H-004, hit-reuse
- interp-fnumber.cpp::<misc>: H-004, hit-reuse
- include/mumpsc/errors.h::<misc>: H-004, hit-reuse
- compiler-sys1.c::xindex: H-014, hit-reuse
- libmpscpp.cpp::mdh_tree_level_add: H-014, hit-reuse
- compiler-mumps.c::main: H-014, hit-adapt
- interp-sym.cpp::sym_: H-012, hit-reuse
- libmpscpp.cpp::<misc-8>: H-014, hit-reuse
- include/mumpsc/libmpscpp.h::<misc>: H-014, hit-reuse
- libmpscpp.cpp::<misc-4>: H-014, hit-reuse
- interp-select.cpp::<misc>: H-014, hit-reuse
- interp-bmgsubs.c::<misc>: H-004, hit-reuse
- interp-parse.cpp::inout: H-004, hit-reuse
- interp-globalb.cpp::<misc>: H-018, hit-reuse
- libmpscpp.cpp::global::DocCorrelate: H-014, hit-reuse

Miss rows: none recorded this batch (0 of 31 consults); no new
cookbook candidates raised at the batch-10 consults (the
capture gate stays with T1/T2/G1-G4; noted, no action).

Close review note (the close review of the three tranche
sections above): NO prose-vs-consult divergences found. All 31
render-ledger paragraphs were checked against their registered
consult rows on entry, outcome and roster id string: the T1
eleven (render ledger entries match the 06:00-06:10Z rows,
including the two same-id global::Length rows distinguished by
needle fold and timestamp), the T2 ten (06:11-06:20Z, the
unpad H-014 hit-adapt prose matches its row; the wstem and
xindex tag corrections carry no consult-row change), and the
T3 ten (06:21-06:30Z roster order, the main H-014 hit-adapt and
nine hit-reuse rows). The two duplicate roster id strings
(global::Length x2 at B10, one consult each at distinct
timestamps and entries) validated against the C-008
(unit, entry, timestamp) uniqueness contract in the events
regen below.

Events regeneration verification: the full journal consult
ledger replayed through the C-005/C-008 export semantics
(Python port of export_doc_events.R, same grammar/validation/
mapping contract): 750 consult rows -> 1498 event rows; the
regenerated CSV is BYTE-IDENTICAL to the tracked
wp2/logs/DOC_knowledge_events.csv (99927 bytes, LF, quoted
4-col, 1499 lines with header). Roster membership (308 units),
outcome vocabulary, and (unit, entry, timestamp) uniqueness all
validated. The same regen was verified live at the T3 splice
(Rscript on Mark's clone: 750 -> 1498 with a clean working tree
at commit 004c407); the close instrument therefore lands NO
tracked-file change (regen identical, per the B7/B8/B9-close
pattern). VERIFICATION STEP (Mark): re-run Rscript
wp2/analysis/R/export_doc_events.R and
git status --porcelain wp2/logs/DOC_knowledge_events.csv
(expect clean) per the B2-close instrument verification
precedent; log any divergence as a protocol deviation.

Stub ledger at close (batch-10 standalone seats): T1 flipped
Stubs.mpsCeil/mpsTrunc to doc delegates (both batch-10 tags
resolved; the mps throwing-seat family fully delegated) and
updated the Stubs.sym_ owner tag (flips at tranche 3);
GlobalPiece.kt re-tagged the u224 row 239 -> row 304 (the
:7181 char-star twin folds onto the u188 String seat),
GlobalClassMisc.kt superseded its Pattern/Justify ledger rows
to the unit-286/288 renders, GlobalLength.kt carries the
unit-278/280 twin-fold addendum. T2 flipped Stubs.
unpad, Stubs.Mltoa (with the Int-to-Long widening at the call
sites) and Stubs.wstem (the raise's libmpscpp.cpp tag
corrected to the interp-stem.cpp:418 roster row); the row-297
xindex verification fold rides the FindSym.kt addendum (the
B9-rendered in-place body stands, verification only); Mltoa's
raised-stub signature kept (h015, callers bind unchanged). T3
flipped Stubs.sym_ (the row-280 seat resolves), Stubs.inout,
Stubs.bmgFullSearch (the bmgFullSearchStrM10 adapter),
Stubs.globalDocCorrelate, Stubs.xecute, Stubs.stopLookup,
Stubs.SYN and Stubs.shredQuery to doc delegates, and
SeqAlignExterns.kt fun Score(a, b) from the raise to the
GAP/MATCH/MISMATCH logic (the house ledger row cleared, DAN1
resolved with this push).

THE MSTRING 33-SEAT RECONCILIATION (the B9-close open item
closes here): the 33 Stubs throwing seats raised at the B9
close with owner tags citing the batch-10 roster rows are all
now flipped to doc delegates of the T3 renders: the seven
mstringTimes seats and the seven-seat mstringPlus, mstringMinus
and mstringDiv ladders ride the libmpscpp.cpp misc-8 / misc-4
renders (LibmpscppMisc10/LibmpscppMisc9), the five
mstringRem seats ride the same misc-4 member/friend ladders
(the GlobalRef percent arm reads unchecked, the H-012 note);
7 x 4 + 5 = 33, every seat accounted, the B10 T3 RESOLUTION
family note rides the flipped declarations. The ladders
depend on the two NEW raised seats Stubs.add1/Stubs.add3
(arith.h scalar ladder owners, throwing H-014, the 48-53
controller arms) and Stubs.globalInvokeKeyed (the subscripted
operator() owner family, throwing H-014, raised with the
DocCorrelate render); these three newly raised seats stay
throwing at close along with the standing batch-9 remains
(Stubs.sql for the interp-sql twin, Stubs.floatstring,
Stubs.keyfix, SyncClock) - their owners are below-roster or
non-corpus inlines; no roster row remains unresolved.

Red-build count at the tranche gates: 0 (the whole-arm kotlinc
2.1.20 gate was silent, 0 errors / 0 warnings, at every
tranche close: 268 files at T1 rising to 277 at T2 and 287 at
T3; the pre-gate hygiene catches - the T1 SysLoad KDoc
apostrophe slip, the T2 CJK-adjacent non-ASCII char in the
SysparmsH/InterpFnumberMisc first drafts - were caught before
any compile by the pre-gate scan, which is why the H-015 scan
alone is not enough; always run the non-ASCII class scan).
gradle gate BUILD SUCCESSFUL at the tranche splices (T1 10s,
T2 12s, T3 14s; the T3 gate runs from the arm directory
wp2/arms/DOC, the per-arm Gradle root convention - the repo
root carries no settings file).

Grading outcome: B10 31/31 translated (T1 simple 278-288 with
render push c8371f4 and splice 3961a72, T2 core 289-298 with
render push f991c44 and splice f2c4eaf, T3 complex 299-308
with render push 14ad7de, splice b83cf4d and the 004c407
fragment-cleanup follow-up that stages the fragment delete its
own commit); four verification-fold renders among them (the
unit-278/280 global::Length needle twins onto the u159 mstring
seat, the unit-283 global::Piece re-tag onto the u224 body,
and the unit-297 compiler-sys1 xindex twin onto the B9
FindSym.kt seat) - the one-render rule held across the batch;
the check-which-overload question of the row-298
mdh_tree_level_add resolved as a REAL render (the nine-column
overload, MdhTreeLevelAdd8.kt, with the unrowed ten-column
sibling ledgered there; the chunker captured only 9 of the 10
vendor overloads). The per-unit check columns remain todo
across B10 per the C-001 cycle-10 backfill decision.
PILOT_TRACKING unchanged at this close (the no-PILOT batch-8
rule; batch-10 rows tracked in this journal).

With batch 10 closed, the DOC arm roster 1-308 of units.csv is
complete: 308 data rows, every unit rendered, verification-
folded or verification-only-ledgered across batches 1-10, and
the 308-unit roster contract validated by the events regen.
Next protocol step per the C-001 committed order: the corpus is
exhausted (units.csv carries no batch-11 block), so the arm
turns to the C-001 cycle-10 per-unit check-column backfill and
the end-of-pilot instrumentation calibration of section 5
(per-arm heuristic-log row counts, entry count, hit/reuse/
miss totals, red-build count and grading outcome); no new
renders are required for either, and any post-corpus protocol
step beyond them lands by a fresh committed order.
