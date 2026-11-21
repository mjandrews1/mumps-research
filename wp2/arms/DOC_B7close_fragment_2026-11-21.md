# DOC B7 close fragment, 2026-11-21

### DOC B7 close - coverage checklist (units 186-215) and events verification

Per the DOC arm discipline: construct list from the batch-7 unit
texts, normalized per section 2.4; for each canonical construct,
at least one cookbook entry matched. Counts are batch-7 unit
journal mentions; the per-unit consult rows live in the arm
journal sections of 2026-11-20 (one consult per unit per the
C-008 rule; no rows are duplicated here per the B6-close
splice-repair lesson).

- Canonical IO type: H-001 matched in 3 batch-7 unit journal(s)
- File-scope globals: H-004 matched in 8 batch-7 unit journal(s)
- Buffer role classification: H-007 matched in 4 batch-7 unit journal(s)
- Shared static scratch buffer: H-008 matched in 1 batch-7 unit journal(s)
- sprintf format-string idiom: H-009 matched in 4 batch-7 unit journal(s)
- Numeric promotion / truncation: H-010 matched in 1 batch-7 unit journal(s)
- Unsigned char domain mapping: H-012 matched in 2 batch-7 unit journal(s)
- Dead store / dead parameter: H-013 matched in 9 batch-7 unit journal(s)
- Out-of-scope C function -> stub: H-014 matched in 13 batch-7 unit journal(s)
- Kotlin grammar vs quoted C source: H-015 matched in 15 batch-7 unit journal(s)
- Statement-level comma operator: H-016 matched in 2 batch-7 unit journal(s)
- Preprocessor conditional arms: H-018 matched in 7 batch-7 unit journal(s)

Constructs not arising in the batch-7 unit texts (H-002, H-003,
H-005, H-006, H-011, H-017): no matched rows for these entries;
noted for the checklist record, no action.

Consult outcome totals for the batch: 17 hit-reuse, 13 hit-adapt,
0 miss (30 consults, one per (unit, entry) per C-005; every row
carries a true in-session timestamp; roster id strings per the
batch-6 close repair 2 discipline).

Per-unit consult coverage (unit -> entry, outcome):

- libmpscpp.cpp::global::Find: H-014, hit-reuse
- compiler-zcommands.c::<misc>: H-004, hit-reuse
- libmpscpp.cpp::global::Piece: H-014, hit-reuse
- compiler-sys1.c::substr: H-010, hit-adapt
- interp-strmanip.cpp::substr: H-014, hit-reuse
- libmpscpp.cpp::mstring::Translate: H-014, hit-reuse
- libmpscpp.cpp::GlobalData: H-014, hit-reuse
- libmpscpp.cpp::global::Soundex: H-014, hit-reuse
- interp-sym.cpp::_justify: H-009, hit-reuse
- interp-sym.cpp::mkpt: H-004, hit-reuse
- libmpscpp.cpp::global::Fnumber: H-013, hit-reuse
- include/mumpsc/externs.h::<misc>: H-004, hit-reuse
- interp-sysfunc.cpp::GlobalData: H-018, hit-reuse
- libmpscpp.cpp::global::MakeGlobal: H-014, hit-adapt
- interp-fcn.cpp::<misc>: H-018, hit-reuse
- libmpscpp.cpp::Multiply: H-014, hit-reuse
- interp-sql.cpp::rslt_callback: H-007, hit-adapt
- compiler-parse.c::Extract: H-007, hit-adapt
- libmpscpp.cpp::Sim1: H-014, hit-adapt
- libmpscpp.cpp::embed1: H-008, hit-reuse
- compiler-mumps.c::<misc>: H-016, hit-adapt
- compiler-fcninit.c::fcninit: H-013, hit-adapt
- interp-sym.cpp::Qsub: H-014, hit-reuse
- interp-native-btree.cpp::freadx: H-018, hit-adapt
- compiler-zcommands.c::zcommands_code: H-015, hit-adapt
- interp-sysfunc.cpp::sigint: H-018, hit-adapt
- compiler-sys1.c::match: H-014, hit-reuse
- compiler-epi.c::epi: H-013, hit-adapt
- libmpscpp.cpp::<misc-6>: H-016, hit-adapt
- interp-service.cpp::ServiceInterface: H-013, hit-adapt

Miss rows: none recorded this batch (0 of 30 consults); the
preprocessor_conditional capture history of the prior closes is
unchanged (no new candidates raised; the 5-miss profile reported
by the prior coverage rounds is pre-capture history, no action).

Events regeneration verification: the full journal consult ledger
replayed through the C-005/C-008 export semantics (Python port of
export_doc_events.R, same grammar/validation/mapping contract):
657 consult rows -> 1312 event rows; the regenerated CSV is
BYTE-IDENTICAL to the tracked wp2/logs/DOC_knowledge_events.csv
(87355 bytes, LF). Roster membership (308 units), outcome
vocabulary, and (unit, entry, timestamp) uniqueness all validated.
VERIFICATION STEP (Mark): re-run
Rscript wp2/analysis/R/export_doc_events.R on m1-build and
confirm the regenerated CSV is byte-identical (working tree
expected clean after the run), per the B2-close instrument
verification precedent; log any divergence as a protocol
deviation.

Stub ledger at close (batch-7 standalone resolutions):
Stubs.substr (compiler twin rebind, the Match/PatrnSys1 call
sites narrowed at call), Stubs.substr flip to the interp real,
Stubs.Mpiece (TAG CORRECTION: the not-a-corpus mis-tag
superseded; owner is the landed unit-114 _piece), Stubs.makeGlobal
(GlobalRef member seats + the ref carrier widening), Stubs.sim1,
Stubs.sigint, the 5-arg Stubs.match, Stubs.freadx, the Qsub seat
family, the compile-fcninit/epi pair with EP/LevCheck and the
linepad2, ExecName Int / pid Long seat corrections,
Stubs.ServiceInterface, and Stubs.count (raised in tranche 2,
resolved in tranche 3). New raised for future owners:
Stubs.IDF (the free libmpscpp.cpp:10370 wrapper, batch-10
ledger) and the Stubs.Concat twelve-overload family (bodies
beyond the misc-6 chunk cut, batch-10 ledger; the global/global
member is the landed unit-65 render).

Red-build count: 0 (every tranche gated at kotlinc 2.1.20
0 errors / 0 warnings: 188, 196 and 206 files at the three
tranche closes; the in-session compile repairs at T2/T3 ended
first-pass green).

Grading outcome: B7 30/30 translated; the m1-build gradle gate
runs at this close (result journal entry next); the per-unit
check columns remain todo across B7 per the C-001 cycle-10
backfill decision. Next protocol step per the C-001 committed
row order: DOC batch 8 (cycle 7).
