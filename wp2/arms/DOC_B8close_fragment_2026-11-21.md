### DOC B8 close - coverage checklist (units 216-246) and events verification

Per the DOC arm discipline: construct list from the batch-8 unit
texts, normalized per section 2.4; for each canonical construct,
at least one cookbook entry matched. Counts are batch-8 unit
journal mentions; the per-unit consult rows live in the arm
journal sections of 2026-11-21 (one consult per unit per the
C-008 rule; no rows are duplicated here per the B6-close
splice-repair lesson). Batch 8 opens at units.csv row 217 (the
batch column == 8 roster block, 31 rows); PILOT_TRACKING does
not tabulate batch 8+ (the no-PILOT batch-8 rule; rows track
here only).

- Canonical IO type: H-001 matched in 1 batch-8 unit journal(s)
- File-scope globals: H-004 matched in 7 batch-8 unit journal(s)
- Buffer role classification: H-007 matched in 2 batch-8 unit journal(s)
- sprintf format-string idiom: H-009 matched in 1 batch-8 unit journal(s)
- Out-of-scope C function -> stub: H-014 matched in 10 batch-8 unit journal(s)
- Kotlin grammar vs quoted C source: H-015 matched in 4 batch-8 unit journal(s)
- Statement-level comma operator: H-016 matched in 1 batch-8 unit journal(s)
- Preprocessor conditional arms: H-018 matched in 5 batch-8 unit journal(s)

Constructs not arising in the batch-8 unit texts (H-002, H-003,
H-005, H-006, H-008, H-010, H-011, H-012, H-013, H-017): no
matched rows for these entries at the batch-8 consult; many
appear inside the batch-8 render bodies' KDoc ledgers (the H-012
domain notes and H-013 vendor quirks of the T3 renders carry
entry references) but the C-008 one-consult-per-unit ledger
counts only the unit consults above; noted for the checklist
record, no action.

Consult outcome totals for the batch: 20 hit-reuse, 11
hit-adapt, 0 miss (31 consults, one per (unit, entry) per C-005;
every row carries a true in-session timestamp; roster id
strings per the batch-6 close repair 2 discipline; the batch-8
Unit-vs-row conventions verified per the 2cfddec T1/T2 repair).

Per-unit consult coverage (unit -> entry, outcome):

- include/mumpsc/builtinTable.h::<misc>: H-015, hit-adapt
- libmpscpp.cpp::mstring::Find: H-014, hit-reuse
- compiler-kill.c::<misc>: H-004, hit-reuse
- compiler-lock.c::<misc>: H-004, hit-reuse
- compiler-set.c::<misc>: H-004, hit-reuse
- include/mumpsc/zfcns.h::<misc>: H-018, hit-reuse
- compiler-mumps.c::atCopy: H-014, hit-adapt
- libmpscpp.cpp::IDF: H-009, hit-reuse
- libmpscpp.cpp::global::Piece: H-007, hit-reuse
- libmpscpp.cpp::GlobalData: H-004, hit-adapt
- libmpscpp.cpp::mdh_tree_level_add: H-015, hit-reuse
- libmpscpp.cpp::global::ReadLine: H-016, hit-adapt
- compiler-parse.c::ExpandText: H-015, hit-adapt
- interp-sym.cpp::ScanParse: H-007, hit-reuse
- interp-parse.cpp::BuildGlobal: H-014, hit-reuse
- interp-sym.cpp::BuildLocal: H-014, hit-reuse
- compiler-parse.c::Extract2a: H-014, hit-reuse
- interp-zfcns.cpp::<misc>: H-018, hit-adapt
- compiler-sys1.c::getstr1: H-001, hit-adapt
- interp-sql.cpp::<misc>: H-018, hit-adapt
- include/mumpsc/compiler-macros.h::<misc>: H-004, hit-reuse
- interp-sql.cpp::btree: H-018, hit-adapt
- libmpscpp.cpp::<misc-7>: H-014, hit-reuse
- interp-sysfunc.cpp::AllocSV: H-018, hit-adapt
- compiler-parse.c::<misc>: H-014, hit-reuse
- include/mumpsc/compiler-globals.h::<misc>: H-004, hit-reuse
- compiler-open.c::mumps_open: H-015, hit-adapt
- interp-parse.cpp::cannon: H-014, hit-reuse
- include/mumpsc/builtin.h::<misc>: H-004, hit-reuse
- interp-bifs.cpp::GtkTreeAdd: H-014, hit-reuse
- interp-sysfunc.cpp::Merge: H-014, hit-reuse

Miss rows: none recorded this batch (0 of 31 consults); no new
cookbook candidates raised at the batch-8 consults (the capture
gate stays with T1/T2/G1-G4; noted, no action).

Events regeneration verification: the full journal consult ledger
replayed through the C-005/C-008 export semantics (Python port of
export_doc_events.R, same grammar/validation/mapping contract):
688 consult rows -> 1374 event rows; the regenerated CSV is
BYTE-IDENTICAL to the tracked wp2/logs/DOC_knowledge_events.csv
(91573 bytes, LF). Roster membership (308 units), outcome
vocabulary, and (unit, entry, timestamp) uniqueness all
validated. The same regen was ALSO verified live by the T3
splice gate (Rscript on Mark's clone: 688 -> 1374 with a clean
working tree, no CSV push needed); the close instrument
therefore lands NO tracked-file change (regen identical, per
the B7-close pattern). VERIFICATION STEP (Mark): re-run
Rscript wp2/analysis/R/export_doc_events.R and
git status --porcelain wp2/logs/DOC_knowledge_events.csv
(expect clean) per the B2-close instrument verification
precedent; log any divergence as a protocol deviation.

Stub ledger at close (batch-8 standalone resolutions):
Stubs.Mglobal delegates ride the unit-167 render unchanged;
T1 added the IDF invoke-family String arity; the Piece mstring
needle and the atCopy TAG CORRECTION were landed at T1;
GlobalData closed as a verification fold. T2 raised
Stubs.LocalOrder (owner interp-sym.cpp::LocalOrder, no roster
row) and Stubs.posixRead/posixWrite (opnflg==3 fd arm). T3
flipped Stubs.AllocSV (non-null return, the Fcn call-site
arity) and Stubs.Merge to the doc renders, flipped
memcheck/cvar/Extract1 to the CompilerParseMisc delegates,
flipped the twelve Stubs.Concat seats to the LibmpscppMisc7
renders WITH TAG CORRECTION (the unit-214 batch-10 forward-tags
superseded; the member bodies are the row-218 misc-7 text), and
raised Stubs.fixGblRef (owner libmpscpp.cpp::FixGblRef,
declared in the unit-149 header ledger, no roster row).
Stands: Stubs.sql (owner the interp SQL subprocess) and
Stubs.keyfix (keyfix.h inline, not a corpus unit) stay throwing
seats; btreeSql is the interp-sql twin of the landed unit-4
native btree render (the Distinct-TWIN note; no seat change).

Red-build count at the tranche gates: 0 (the whole-arm kotlinc
2.1.20 gate was silent, 0 errors / 0 warnings, at every tranche
close: 215 files at T1 rising to 232 files at T3; the in-session
compile repairs at T3 - the Stubs.fixGblRef seat resolution and
the Kotlin operator-convention modifier drops - were repaired
before the gate and ended first-pass green). gradle gate
BUILD SUCCESSFUL at the tranche splices (T2 16s, T3 31s, T1
green at its splice close).

Grading outcome: B8 31/31 translated (T1 simple 216-226, T2
core 227-236, T3 complex 237-246; push codes T1 6050da2 with
repair chain 013be4c/3502303/db210e6/2cfddec and splice
dec1620, T3 render 0c5b189 with splice e0c066e); the per-unit
check columns remain todo across B8 per the C-001 cycle-10
backfill decision. PILOT_TRACKING unchanged at this close (the
no-PILOT batch-8 rule; batch-8 rows track in this journal).
Next protocol step per the C-001 committed row order: DOC
batch 9 (cycle 8) tranche 1, units.csv rows 247-276.
