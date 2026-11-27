# Pilot (B1) Tracking

Order within batch: complexity_rank ascending. Status: todo / translating / graded-ok / graded-deviation / deferred. Simple stratum COMPLETE (units 1-10, all builds green; unit 10 gcvt semantics verified against the reference binary). Core and complex strata COMPLETE for batch 1 (B1 closed 2026-11-15: 31/31 translated, all builds green; coverage_report: 17 rules, 179 triggering situations, 97.2% hit rate, 95.4% reuse rate; the 5 preprocessor_conditional misses captured as rule h018). Translator units integrate against the translator oracle; interpreter/library units against the interpreter oracle.

**Per-unit checks (added 2026-11-16).** The four per-unit verification columns
introduced with batch 2 (code-check, output-check, runtime-check,
target-check) apply to the B1 table from this date as well, so both batches
share one tracking schema and cross-batch comparisons remain like-for-like.
The columns were added retroactively: at B1 close (2026-11-15) no per-unit
check pass had been executed, so all B1 entries are initialized `todo` —
nothing is backfilled. The four checks are
defined in `wp2/PER_UNIT_CHECKS.md` (registry entry C-002,
`wp2/PROTOCOL_CLARIFICATIONS.md`).

**DOC B1 opened 2026-11-19** (registry entry C-007: the DOC arm
completed B2 before B1; the inversion is a logged deviation, and B1
proceeds in phased tranches under the unchanged fixed discipline.
The DOC column below updates per tranche.) DOC B1 closed 2026-11-19: 31/31 translated (check columns todo per the C-001 cycle-10 backfill decision); gradle BUILD SUCCESSFUL on m1-build; events CSV regenerated under the C-008 export key (commit 45df0fe); coverage checklist and both close decisions recorded in the arm journal (commit ebea35f). As in batch 2, the check columns track the BASE
reference translation; check results for the other arms' B1 translations
are recorded in the per-arm logs under `arms/` as those arms complete the
pilot. Checks use the same status vocabulary as the arm columns.

| # | unit_id | lang | stratum | BASE | DOC | PRO | DAF | BOTH | code-check | output-check | runtime-check | target-check |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| 1 | compiler-copyright-notice.c::<misc> | c | simple | graded-ok | translated | translated | todo | todo | todo | todo | todo | todo |
| 2 | compiler-if.c::<misc> | c | simple | graded-ok | translated | translated | todo | todo | todo | todo | todo | todo |
| 3 | compiler-open.c::<misc> | c | simple | graded-ok | translated | translated | todo | todo | todo | todo | todo | todo |
| 4 | interp-native-btree.cpp::btree | cpp | simple | graded-ok | translated | translated | todo | todo | todo | todo | todo | todo |
| 5 | libmpscpp.cpp::EncodeHTML | cpp | simple | graded-ok | translated | translated | todo | todo | todo | todo | todo | todo |
| 6 | interp-sym.cpp::_find | cpp | simple | graded-ok | translated | translated | todo | todo | todo | todo | todo | todo |
| 7 | compiler-parse.c::fixstr | c | simple | graded-ok | translated | translated | todo | todo | todo | todo | todo | todo |
| 8 | libmpscpp.cpp::mdh_tree_level_add | cpp | simple | graded-ok | translated | translated | todo | todo | todo | todo | todo | todo |
| 9 | include/mumpsc/libmpsrdbms.h::<misc> | c | simple | graded-ok | translated | translated | todo | todo | todo | todo | todo | todo |
| 10 | interp-arith.cpp::mps_acos | cpp | simple | graded-ok | translated | translated | todo | todo | todo | todo | todo | todo |
| 11 | compiler-parse.c::ExpandNext | c | core | graded-ok | translated | translated | todo | todo | todo | todo | todo | todo |
| 12 | mumps.cpp::<misc> | cpp | core | graded-ok | translated | translated | todo | todo | todo | todo | todo | todo |
| 13 | interp-sysfunc.cpp::GlobalName | cpp | core | graded-ok | translated | translated | todo | todo | todo | todo | todo | todo |
| 14 | libmpscpp.cpp::global::TermCorrelate | cpp | core | graded-ok | translated | translated | todo | todo | todo | todo | todo | todo |
| 15 | compiler-parse.c::Extract2 | c | core | graded-ok | translated | translated | todo | todo | todo | todo | todo | todo |
| 16 | compiler-parse.c::ExpandGet | c | core | graded-ok | translated | translated | todo | todo | todo | todo | todo | todo |
| 17 | interp-bmgsubs.c::gotamatch | c | core | graded-ok | translated | translated | todo | todo | todo | todo | todo | todo |
| 18 | compiler-abrtx.c::abrtx | c | core | graded-ok | translated | translated | todo | todo | todo | todo | todo | todo |
| 19 | interp-interp.cpp::<misc> | cpp | core | graded-ok | translated | translated | todo | todo | todo | todo | todo | todo |
| 20 | interp-arith.cpp::numcomp | cpp | core | graded-ok | translated | translated | todo | todo | todo | todo | todo | todo |
| 21 | compiler-localarray.c::LocalArray | c | complex | graded-ok | translated | translated | todo | todo | todo | todo | todo | todo |
| 22 | interp-bstring.cpp::<misc> | cpp | complex | graded-ok | translated | translated | todo | todo | todo | todo | todo | todo |
| 23 | libmpscpp.cpp::Input | cpp | complex | graded-ok | translated | translated | todo | todo | todo | todo | todo | todo |
| 24 | interp-sysfunc.cpp::DirectRead | cpp | complex | graded-ok | translated | translated | todo | todo | todo | todo | todo | todo |
| 25 | interp-sysfunc.cpp::<misc> | cpp | complex | graded-ok | translated | translated | todo | todo | todo | todo | todo | todo |
| 26 | compiler-sys1.c::patrn1 | c | complex | graded-ok | translated | translated | todo | todo | todo | todo | todo | todo |
| 27 | compiler-sys1.c::MyFunc | c | complex | graded-ok | translated | translated | todo | todo | todo | todo | todo | todo |
| 28 | interp-sym.cpp::_SymFree | cpp | complex | graded-ok | translated | translated | todo | todo | todo | todo | todo | todo |
| 29 | include/mumpsc/numSetup.h::<misc> | c | complex | graded-ok | translated | translated | todo | todo | todo | todo | todo | todo |
| 30 | interp-parse.cpp::patrn1 | cpp | complex | graded-ok | translated | translated | todo | todo | todo | todo | todo | todo |
| 31 | libmpscpp.cpp::<misc-1> | cpp | complex | graded-ok | translated | translated | todo | todo | todo | todo | todo | todo |

## Batch 2 (units 32-62, opened 2026-11-15)

| # | Unit | Lang | Class | Status | code-check | output-check | runtime-check | target-check |
|---|------|------|-------|--------|-------------|--------------|---------------|--------------|
| 32 | libmpscpp.cpp::global::Extract | cpp | simple | graded-ok | todo | todo | todo | todo |
| 33 | libmpscpp.cpp::global::Find | cpp | simple | graded-ok | todo | todo | todo | todo |
| 34 | libmpscpp.cpp::SQL_Table | cpp | simple | graded-ok | todo | todo | todo | todo |
| 35 | compiler-abrtx.c::<misc> | c | simple | graded-ok | todo | todo | todo | todo |
| 36 | compiler-doty.c::<misc> | c | simple | graded-ok | todo | todo | todo | todo |
| 37 | libmpscpp.cpp::KiLL | cpp | simple | graded-ok | todo | todo | todo | todo |
| 38 | libmpscpp.cpp::global::Piece | cpp | simple | graded-ok | todo | todo | todo | todo |
| 39 | libmpscpp.cpp::GlobalOrder | cpp | simple | graded-ok | todo | todo | todo | todo |
| 40 | interp-arith.cpp::mps_exp | cpp | simple | graded-ok | todo | todo | todo | todo |
| 41 | interp-arith.cpp::mps_exp10 | cpp | simple | graded-ok | todo | todo | todo | todo |
| 42 | libmpscpp.cpp::mstring::Fnumber | cpp | core | graded-ok | todo | todo | todo | todo |
| 43 | libmpscpp.cpp::mdh_tree_level_add | cpp | core | graded-ok | todo | todo | todo | todo |
| 44 | libmpscpp.cpp::global::Min | cpp | core | graded-ok | todo | todo | todo | todo |
| 45 | libmpscpp.cpp::prnt | cpp | core | graded-ok | todo | todo | todo | todo |
| 46 | libmpscpp.cpp::Multiply | cpp | core | graded-ok | todo | todo | todo | todo |
| 47 | compiler-copyright-notice.c::copyright_notice | c | core | graded-ok | todo | todo | todo | todo |
| 48 | interp-parse.cpp::getstr2 | cpp | core | graded-ok | todo | todo | todo | todo |
| 49 | libmpscpp.cpp::global::IDF | cpp | core | graded-ok | todo | todo | todo | todo |
| 50 | interp-native-btree.cpp::scanblocks | cpp | core | graded-ok | graded-ok | graded-ok | graded-ok | graded-ok |
| 51 | interp-sql.cpp::embed | cpp | core | graded-ok | graded-ok | graded-ok | graded-ok | graded-ok |
| 52 | compiler-globalarray.c::GlobalArray | c | complex | todo | todo | todo | todo | todo |
| 53 | libmpscpp.cpp::global::Multiply | cpp | complex | todo | todo | todo | todo | todo |
| 54 | compiler-if.c::if_code | c | complex | todo | todo | todo | todo | todo |
| 55 | include/mumpsc/btree.h::<misc> | c | complex | todo | todo | todo | todo | todo |
| 56 | interp-lock.cpp::<misc> | cpp | complex | todo | todo | todo | todo | todo |
| 57 | include/mumpsc/arith.h::<misc> | c | complex | todo | todo | todo | todo | todo |
| 58 | interp-parse.cpp::prnterr | cpp | complex | todo | todo | todo | todo | todo |
| 59 | compiler-sys1.c::inout | c | complex | todo | todo | todo | todo | todo |
| 60 | libmpscpp.cpp::global::TreePrint | cpp | complex | todo | todo | todo | todo | todo |
| 61 | libmpscpp.cpp::sw | cpp | complex | todo | todo | todo | todo | todo |
| 62 | libmpscpp.cpp::<misc-2> | cpp | complex | todo | todo | todo | todo | todo |

## Batch 3 (units 63-93, opened 2026-11-19; DOC cycle-2 cell per the C-001 map: DOC's committed row order is B2, B3, ... so B3 follows the closed B2 with no inversion to log)

**DOC B3 closed 2026-11-19**: 31/31 translated in tranches T1-T3. Coverage
checklist in the arm journal (commit d0339fb, line repair 4b040e7): 140
consults, 125 hit-reuse / 15 hit-adapt / 0 miss; 100.0% hit, 89.3% reuse;
cookbook stays H-001..H-018 (no capture candidate under T1/T2/T3). Gradle
gate on m1-build BUILD SUCCESSFUL (build only; the detekt rig is
PRO-only). Events CSV regenerated under the C-008 export key: 320 consult
rows -> 638 event rows (the B3 delta over the B1-close export is exactly
140 reuse+hit pairs). Check columns track the BASE reference translation
(C-002) and stay todo per the C-001 cycle-10 backfill decision.

DOC B5 tranche 2 (units 134-143, the core stratum) translated 2026-11-20:
ten units, whole-arm kotlinc gate 0/0 at 144 files; the ten DOC rows
below marked translated. Gradle gate on m1-build pending at batch close.

DOC B5 tranche 3 (units 144-154, the complex stratum, batch close) translated
2026-11-20: eleven units pushed (sym.h misc, Qlength, SynInit, scan_token,
bmg_search, mstringClass misc, mumps main, compiler-globals-ext misc, rpm
misc, sql, libmpscpp misc-5), whole-arm kotlinc gate 0/0 at 155 files; the
eleven DOC rows below marked translated and batch 5 is complete
(31/31 translated). Gradle gate on m1-build pending at batch close review.

| # | Unit | Lang | Class | Status | DOC | code-check | output-check | runtime-check | target-check |
|---|------|------|-------|--------|-----|-------------|--------------|---------------|--------------|
| 63 | compiler-epi.c::<misc> | c | simple | todo | translated | todo | todo | todo | todo |
| 64 | compiler-read.c::<misc> | c | simple | todo | translated | todo | todo | todo | todo |
| 65 | libmpscpp.cpp::global::Concat | cpp | simple | todo | translated | todo | todo | todo | todo |
| 66 | libmpscpp.cpp::mdh_tree_level_add | cpp | simple | todo | translated | todo | todo | todo | todo |
| 67 | interp-arith.cpp::mps_sqrt | cpp | simple | todo | translated | todo | todo | todo | todo |
| 68 | libmpscpp.cpp::Max | cpp | simple | todo | translated | todo | todo | todo | todo |
| 69 | libmpscpp.cpp::GlobalOrder | cpp | simple | todo | translated | todo | todo | todo | todo |
| 70 | libmpscpp.cpp::GlobalData | cpp | simple | todo | translated | todo | todo | todo | todo |
| 71 | interp-arith.cpp::mps_atan | cpp | simple | todo | translated | todo | todo | todo | todo |
| 72 | libmpscpp.cpp::global::Fnumber | cpp | simple | todo | translated | todo | todo | todo | todo |
| 73 | interp-arith.cpp::mps_justify | cpp | core | todo | translated | todo | todo | todo | todo |
| 74 | libmpscpp.cpp::Cost | cpp | core | todo | translated | todo | todo | todo | todo |
| 75 | libmpscpp.cpp::Scan | cpp | core | todo | translated | todo | todo | todo | todo |
| 76 | include/mumpsc/cppsysfunc.h::<misc> | c | core | todo | translated | todo | todo | todo | todo |
| 77 | libmpscpp.cpp::ScanAlnum | cpp | core | todo | translated | todo | todo | todo | todo |
| 78 | compiler-sys1.c::readf_ | c | core | todo | translated | todo | todo | todo | todo |
| 79 | libmpscpp.cpp::mstring::Eval | cpp | core | todo | translated | todo | todo | todo | todo |
| 80 | compiler-parse.c::ExpandQS | c | core | todo | translated | todo | todo | todo | todo |
| 81 | interp-parse.cpp::<misc> | cpp | core | todo | translated | todo | todo | todo | todo |
| 82 | libmpscpp.cpp::RunMumps | cpp | core | todo | translated | todo | todo | todo | todo |
| 83 | interp-zfcns.cpp::GtkTreeAdd | cpp | complex | todo | translated | todo | todo | todo | todo |
| 84 | compiler-sys1.c::cannon | c | complex | todo | translated | todo | todo | todo | todo |
| 85 | libmpscpp.cpp::Cosine | cpp | complex | todo | translated | todo | todo | todo | todo |
| 86 | interp-bmgsubs.c::bmg_fsearch | c | complex | todo | translated | todo | todo | todo | todo |
| 87 | compiler-sys1.c::errmod_ | c | complex | todo | translated | todo | todo | todo | todo |
| 88 | compiler-sys1.c::patrn | c | complex | todo | translated | todo | todo | todo | todo |
| 89 | include/mumpsc/globalClass.h::<misc> | c | complex | todo | translated | todo | todo | todo | todo |
| 90 | interp-arith.cpp::<misc> | cpp | complex | todo | translated | todo | todo | todo | todo |
| 91 | compiler-new.c::new_code | c | complex | todo | translated | todo | todo | todo | todo |
| 92 | interp-parse.cpp::patrn | cpp | complex | todo | translated | todo | todo | todo | todo |
| 93 | interp-parse.cpp::parse_ | cpp | complex | todo | translated | todo | todo | todo | todo |

## Batch 4 (units 94-123, opened 2026-11-19; DOC follows the closed B3 in the committed row order, no inversion to log)

**DOC B4 closed 2026-11-19**: 30/30 translated in tranches T1-T3. Coverage
checklist in the arm journal: 147 consults, 127 hit-reuse / 20 hit-adapt /
0 miss; 100.0% hit, 86.4% reuse; cookbook stays H-001..H-018 (no capture
candidate under T1/T2/T3). Gradle gate on m1-build BUILD SUCCESSFUL
(build only; the detekt rig is PRO-only). Events CSV regenerated under
the C-008 export key: 467 consult rows -> 932 event rows (the B4 delta
over the B3-close export is exactly 147 reuse+hit pairs). Close-sequence
line repair recorded in the arm journal: the 146 bare-unit-number B4
consult rows rewritten in place to the registered roster-id grammar
(with the tranche timestamps each fragment already declared; one missing
u119 H-018 row restored). Check columns track the BASE reference
translation (C-002) and stay todo per the C-001 cycle-10 backfill
decision.

| # | Unit | Lang | Class | Status | DOC | DAF | code-check | output-check | runtime-check | target-check |
|---|------|------|-------|--------|-----|-----|-------------|--------------|---------------|--------------|
| 94 | compiler-globalarray.c::<misc> | c | simple | todo | translated | translated | todo | translated | todo | todo |
| 95 | compiler-new.c::<misc> | c | simple | todo | translated | translated | todo | translated | todo | todo |
| 96 | interp-stem.cpp::escan | cpp | simple | todo | translated | translated | todo | translated | todo | todo |
| 97 | libmpscpp.cpp::mstring::Fnumber | cpp | simple | todo | translated | translated | todo | translated | todo | todo |
| 98 | libmpscpp.cpp::Count | cpp | simple | todo | translated | translated | todo | translated | todo | todo |
| 99 | libmpscpp.cpp::Min | cpp | simple | todo | translated | translated | todo | translated | todo | todo |
| 100 | libmpscpp.cpp::Transpose | cpp | simple | todo | translated | translated | todo | translated | todo | todo |
| 101 | server.cpp::<misc> | cpp | simple | todo | translated | translated | todo | translated | todo | todo |
| 102 | rdbms-if.c::<misc> | c | simple | todo | translated | translated | todo | translated | todo | todo |
| 103 | interp-arith.cpp::mps_asin | cpp | simple | todo | translated | translated | todo | translated | todo | todo |
| 104 | libmpscpp.cpp::ScanAlnum | cpp | core | todo | translated | translated | todo | todo | todo | todo |
| 105 | interp-sysfunc.cpp::LocalName | cpp | core | todo | translated | translated | todo | todo | todo | todo |
| 106 | interp-native-btree.cpp::pb | cpp | core | todo | translated | translated | todo | todo | todo | todo |
| 107 | include/mumpsc/sysfunc.h::<misc> | c | core | todo | translated | translated | todo | todo | todo | todo |
| 108 | libmpscpp.cpp::init | cpp | core | todo | translated | translated | todo | todo | todo | todo |
| 109 | compiler-at-test.c::atTest | c | core | todo | translated | translated | todo | todo | todo | todo |
| 110 | interp-bifs.cpp::<misc> | cpp | core | todo | translated | translated | todo | todo | todo | todo |
| 111 | interp-fnumber.cpp::_fnumber | cpp | core | todo | translated | translated | todo | todo | todo | todo |
| 112 | libmpscpp.cpp::global::Centroid | cpp | core | todo | translated | translated | todo | todo | todo | todo |
| 113 | interp-rpm.cpp::parse_pattern_element | cpp | core | todo | translated | translated | todo | todo | todo | todo |
| 114 | interp-piece.cpp::<misc> | cpp | complex | todo | translated | translated | todo | todo | todo | todo |
| 115 | interp-parse.cpp::readf_ | cpp | complex | todo | translated | translated | todo | todo | todo | todo |
| 116 | compiler-lock.c::lock_code | c | complex | todo | translated | translated | todo | todo | todo | todo |
| 117 | compiler-doty.c::doty | c | complex | todo | translated | translated | todo | todo | todo | todo |
| 118 | include/pcre.h::<misc> | c | complex | todo | translated | translated | todo | todo | todo | todo |
| 119 | interp-sym.cpp::GlobalQuery | cpp | complex | todo | translated | translated | todo | todo | todo | todo |
| 120 | libmpscpp.cpp::<misc-9> | cpp | complex | todo | translated | translated | todo | todo | todo | todo |
| 121 | compiler-set.c::set_code | c | complex | todo | translated | translated | todo | todo | todo | todo |
| 122 | compiler-for.c::for_code | c | complex | todo | translated | translated | todo | todo | todo | todo |
| 123 | interp-fcn.cpp::fcn | cpp | complex | todo | translated | translated | todo | todo | todo | todo |

## Batch 5 (units 124-154, opened 2026-11-20; DOC follows the closed B4 in the committed row order, no inversion to log)

DOC B5 tranche 1 (units 124-133, the simple stratum) translated 2026-11-20: ten units pushed, whole-arm kotlinc gate 0/0 at 135 files; gradle gate on m1-build pending at tranche close. Rows follow the pre-registered batch_plan.md order (simple, core, complex; complexity rank ascending within each stratum; the commit-02aff5f first push of this table had listed the rows in units.csv line order - repaired in place in this commit, values unchanged apart from the corrected row order and the DOC column carrying the tranche-1 translated marks). Check columns track the BASE reference translation (C-002) and stay todo per the C-001 cycle-10 backfill decision.


| # | Unit | Lang | Class | Status | DOC | DAF | BOTH | code-check | output-check | runtime-check | target-check |
|---|------|------|-------|--------|-----|-----|------|-------------|--------------|---------------|--------------|
| 124 | compiler-for.c::<misc> | c | simple | todo | translated | translated | translated | todo | todo | todo | todo |
| 125 | compiler-localarray.c::<misc> | c | simple | todo | translated | translated | translated | todo | todo | todo | todo |
| 126 | libmpscpp.cpp::mdh_tree_level_add | cpp | simple | todo | translated | translated | translated | todo | todo | todo | todo |
| 127 | libmpscpp.cpp::mdh_tree_level_add | cpp | simple | todo | translated | translated | translated | todo | todo | todo | todo |
| 128 | libmpscpp.cpp::mdh_tree_level_add | cpp | simple | todo | translated | translated | translated | todo | todo | todo | todo |
| 129 | libmpscpp.cpp::mstring::Length | cpp | simple | todo | translated | translated | translated | todo | todo | todo | todo |
| 130 | libmpscpp.cpp::Avg | cpp | simple | todo | translated | translated | translated | todo | todo | todo | todo |
| 131 | libmpscpp.cpp::global::Translate | cpp | simple | todo | translated | translated | translated | todo | todo | todo | todo |
| 132 | interp-arith.cpp::mps_log10 | cpp | simple | todo | translated | translated | translated | todo | todo | todo | todo |
| 133 | libmpscpp.cpp::global::Length | cpp | simple | todo | translated | translated | translated | todo | todo | todo | todo |
| 134 | libmpscpp.cpp::GlobalQS | cpp | core | todo | translated | translated | translated | todo | todo | todo | todo |
| 135 | compiler-parse.c::ExpandData | c | core | todo | translated | translated | translated | todo | todo | todo | todo |
| 136 | libmpscpp.cpp::global::Sum | cpp | core | todo | translated | translated | translated | todo | todo | todo | todo |
| 137 | libmpscpp.cpp::Transpose | cpp | core | todo | translated | translated | translated | todo | todo | todo | todo |
| 138 | libmpscpp.cpp::Multiply | cpp | core | todo | translated | translated | translated | todo | todo | todo | todo |
| 139 | interp-arith.cpp::expx | cpp | core | todo | translated | translated | translated | todo | todo | todo | todo |
| 140 | interp-sysfunc.cpp::getstr1 | cpp | core | todo | translated | translated | translated | todo | todo | todo | todo |
| 141 | interp-strmanip.cpp::<misc> | cpp | core | todo | translated | translated | translated | todo | todo | todo | todo |
| 142 | interp-sym.cpp::SysDump | cpp | core | todo | translated | translated | translated | todo | todo | todo | todo |
| 143 | interp-rpm.cpp::parse_count | cpp | core | todo | translated | translated | translated | todo | todo | todo | todo |
| 144 | include/mumpsc/sym.h::<misc> | c | complex | todo | translated | translated | translated | todo | todo | todo | todo |
| 145 | interp-sym.cpp::Qlength | cpp | complex | todo | translated | translated | translated | todo | todo | todo | todo |
| 146 | libmpscpp.cpp::SynInit | cpp | complex | todo | translated | translated | translated | todo | todo | todo | todo |
| 147 | interp-rpm.cpp::scan_token | cpp | complex | todo | translated | translated | translated | todo | todo | todo | todo |
| 148 | interp-bmgsubs.c::bmg_search | c | complex | todo | translated | translated | translated | todo | todo | todo | todo |
| 149 | include/mumpsc/mstringClass.h::<misc> | c | complex | todo | translated | translated | translated | todo | todo | todo | todo |
| 150 | mumps.cpp::main | cpp | complex | todo | translated | translated | translated | todo | todo | todo | todo |
| 151 | include/mumpsc/compiler-globals-ext.h::<misc> | c | complex | todo | translated | translated | translated | todo | todo | todo | todo |
| 152 | interp-rpm.cpp::<misc> | cpp | complex | todo | translated | translated | translated | todo | todo | todo | todo |
| 153 | rdbms-if.c::sql | c | complex | todo | translated | translated | translated | todo | todo | todo | todo |
| 154 | libmpscpp.cpp::<misc-5> | cpp | complex | todo | translated | translated | translated | todo | todo | todo | todo | todo

**DOC B5 closed 2026-11-20**: 31/31 translated in tranches T1-T3
(simple/core/complex, batch_plan order). Coverage checklist in
the arm journal: 129 consults, 109 hit-reuse / 20 hit-adapt /
0 miss; 100.0% hit, 84.5% reuse (the complex stratum carries the
heaviest adaptation load of the study so far: T3 75.5% reuse);
cookbook stays H-001..H-018 (no capture candidate under
T1/T2/T3). Gradle gate on m1-build BUILD SUCCESSFUL at every
tranche (12s/15s/19s, all first-pull, zero fix rounds). Events
CSV regenerated under the C-008 export key: 596 consult rows ->
1190 event rows (the B5 delta over the B4-close export is
exactly 129 reuse+hit pairs). The T3 journal-push truncation
incident is repaired and ledgered in the arm journal (commits
82651bd5 + 8197a54: fragment re-carry + restore from the
pre-tranche blob). Check columns track the BASE reference
translation (C-002) and stay todo per the C-001 cycle-10
backfill decision. DOC B6 follows per the committed row order.

## Batch 6 (units 155-185, opened 2026-11-20; DOC follows the closed B5 in the committed row order)

Shaping per the PRO B6 precedent: T1 simple (155-164), T2 core (165-175), T3 complex minus the big three (176-182), and the three ~1600-LOC giants deferred to a dedicated session PI decision (183 interp-bifs.cpp::zfcn, 184 interp-zfcns.cpp::zfcn, 185 interp-interp.cpp::Interpret).

DOC B6 tranche 1 (units 155-164, the simple stratum) translated 2026-11-20: ten units pushed, whole-arm kotlinc gate 0/0 at 162 files; gradle gate on m1-build pending at tranche close. Rows follow the pre-registered batch_plan.md order (simple stratum, complexity rank ascending). The tranche also carries the unit-164 memsize.h config-correction edit to the unit-129 readf render (NOTABS live, TAB-to-space arm restored - vendor compiler-sys1.c includes memsize.h) and the Stubs.leaning_toothpickize flip to delegate. Check columns track the BASE reference translation (C-002) and stay todo per the C-001 cycle-10 backfill decision.

DAF B6 tranche 1 (units 155-164, the simple stratum) translated 2026-11-22: ten units pushed, whole-arm kotlinc gate 0/0 at 75 files (both the gate copy and the fresh repo-copy submission); DAF consults ledgered in the DAF heuristic log ids 148-160; dafny suite 55 verified 0 errors (50 baseline + the new spec/mps_log2.dfy at 5). QUEUE NOTE (PI decision of 2026-11-22, logged against the committed order): DAF runs ahead of BOTH per the PI instruction to finish all of the DAF tranches before proceeding to BOTH; BOTH B5 T2 stays queued next after the DAF B6 tranches and the 183-185 big-three PI session. The tranche resolves the leaning_toothpickize stub seat (the unit-157 render releases it; the prior not-a-roster-unit header blame was a TAG CORRECTION to units.csv batch-6 row 157), disambiguates the unit-161 ScanAlnum overload against the vendor source (the istream overload, not the unit-104 FILE* body the dump4 first-loc extraction returned), and seats the memsize.h Linux lattice at the unit-164 ledger. Check columns track the BASE reference translation (C-002) and stay todo per the C-001 cycle-10 backfill decision.

BOTH B6 tranche 1 (units 155-164, the simple stratum) translated 2026-11-22: ten units pushed, whole-arm kotlinc gate SILENT EXIT=0 at 41 files (both the gate copy and the fresh repo-copy submission); dafny suite 35 verified 0 errors (30 baseline + the new arm-own spec/mps_log2.dfy at 5). Rows follow the pre-registered batch_plan.md order (simple, complexity rank ascending). The tranche flips the unit-157 leaning_toothpickize stub to the real body in RpmMisc.kt (h014 delegate flip), disambiguates the unit-159 global::Length overload set against the units.csv duplicate-id ledger (the std::string overload at libmpscpp.cpp:3330, derived via the batch-row metrics + stable file-order score sort; the mstring twin seats at the batch-10 rank-64 row, the char twin at the batch-10 rank-56 row), resolves the unit-161 ScanAlnum overload the same way (the istream body at 10086 is the zero-pointer-marks batch-6 rank-73 row), seats the unit-164 memsize.h config lattice (GCVT live via __APPLE__ per h018, NOTABS_ACTIVE, PS1 verified-equal and reused from the unit-151 seat), and rides the first consults-as-rule of the h019/h020 promotions (globalFind, globalLength, Sum renders; ScanAlnum Reader handle). Seat edits: BothStubs.kt gains the sw (roster 287) and _find (roster 6) named-owner stubs; LibmpscppMisc5.kt atolC promoted internal for the atoi/sscanf quiet-parse seats. Check columns track the BASE reference translation (C-002) and stay todo per the C-001 cycle-10 backfill decision.

| # | Unit | Lang | Class | Status | DOC | DAF | BOTH | code-check | output-check | runtime-check | target-check |
|---|------|------|-------|--------|-----|-----|------|-------------|--------------|---------------|--------------|
| 155 | compiler-fcninit.c::<misc> | c | simple | todo | translated | translated | translated | todo | todo | todo | todo |
| 156 | libmpscpp.cpp::global::Find | cpp | simple | todo | translated | translated | translated | todo | todo | todo | todo |
| 157 | interp-rpm.cpp::leaning_toothpickize | cpp | simple | todo | translated | translated | translated | todo | todo | todo | todo |
| 158 | libmpscpp.cpp::SmithWaterman | cpp | simple | todo | translated | translated | translated | todo | todo | todo | todo |
| 159 | libmpscpp.cpp::global::Length | cpp | simple | todo | translated | translated | translated | todo | todo | todo | todo |
| 160 | libmpscpp.cpp::Sum | cpp | simple | todo | translated | translated | translated | todo | todo | todo | todo |
| 161 | libmpscpp.cpp::ScanAlnum | cpp | simple | todo | translated | translated | translated | todo | todo | todo | todo |
| 162 | interp-arith.cpp::mps_log2 | cpp | simple | todo | translated | translated | translated | todo | todo | todo | todo |
| 163 | interp-arith.cpp::mps_sin | cpp | simple | todo | translated | translated | translated | todo | todo | todo | todo |
| 164 | include/mumpsc/memsize.h::<misc> | c | simple | todo | translated | translated | translated | todo | todo | todo | todo |

DOC B6 tranche 2 (units 165-175, the core stratum) translated 2026-11-21: eleven units pushed, whole-arm kotlinc gate 0/0 at 171 files; gradle gate on m1-build pending at tranche close. Rows follow the pre-registered batch_plan.md order (core stratum, complexity rank ascending). The tranche resolves four H-014 stub seats (Stubs.Mglobal CharArray overload, Stubs.Mextract, Stubs.globalMax, Stubs.dice) plus Stubs.stopInit, carries two TAG CORRECTIONS (the global::Max roster mis-read at units 68/171; the _extract batch-10 mis-tag at unit 168), and seats the unit-172 stateVector.h residual field roster into the Msv carrier. Check columns track the BASE reference translation (C-002) and stay todo per the C-001 cycle-10 backfill decision.

DAF B6 tranche 2 (units 165-175, the core stratum) translated 2026-11-22: eleven units pushed, whole-arm kotlinc gate 0/0 at 86 files (both the gate copy and the fresh repo-copy submission, 75 base + 11 tranche renders); DAF consults ledgered in the DAF heuristic log ids 161-174; dafny suite 70 verified 0 errors (55 baseline + the new spec/extract.dfy at 15). The tranche releases the three queued named-owner stubs (MglobalDaf of DafInterpStubsT3, released by the unit-167 render with the KEY FINDING that interp-globalb.cpp:45 defines SQLITE so the SQLITE arm is the live arm and IS the roster row; stopInitD of LibmpscppMisc9 with a TAG CORRECTION - the batch-6 row 166 IS the string overload at libmpscpp.cpp:7729, the prior not-a-roster-unit blame was wrong; diceD of LibmpscppMisc9, released by the unit-175 render keeping the two-arg wrapper signature), raises floatstringDaf/btreeDaf/selectStdinDaf/sttyDaf as named-owner platform stubs, extends the DafMsv carrier with the unit-172 stateVector.h field universe, and carries the three queued T1 rider wording fixes (Leaning Toothpickize, MpsLog2, MemsizeHMisc; no semantic change). The queue order stands per the PI decision logged at tranche 1: T3 (176-182) next, then the 183-185 big-three PI session, then BOTH B5 T2. Check columns track the BASE reference translation (C-002) and stay todo per the C-001 cycle-10 backfill decision.

BOTH B6 tranche 2 (units 165-175, the core stratum) translated 2026-11-22: eleven units pushed, whole-arm kotlinc gate SILENT EXIT=0 at 50 files (both the gate copy and the fresh repo-copy submission, 41 base + 9 tranche renders; the unit-167 Mglobal flip and the unit-172 Msv extension land in the existing files); dafny suite 40 verified 0 errors (35 baseline + the new arm-own spec/extract.dfy at 5). Rows follow the pre-registered batch_plan.md order (core stratum, complexity rank ascending; unit 172 is the complex family member). The tranche flips the unit-167 Mglobal stub to the real SQLITE-arm routing (interp-globalb.cpp:45 defines SQLITE; CLOSE returns 1, RETRIEVE/STORE seat keyfix real at keyfix.h:7, every op delegates to the raised btreeC named-owner stub, roster 29), seats the unit-166 StopInit string overload (libmpscpp.cpp:7729; the file-scope StopList lands as the stopWords set), seats floatstringC real at the unit-174 modulo render (same-include static inline, keyfix precedent; the intLong arm is live per defines.h:31), raises the Extract2 (roster 171), GlobalData (roster 76/78/79), selectStdinC/sttyRaw/sttyRawOff platform stubs, extends the Msv carrier with the unit-172 stateVector.h residual field universe (h021 FIRST CONSULTS-AS-RULE; in_file/out_file over the h020 RandomAccessFile fold; grslts/grsltsx excluded arms on the record; the pd1 32001 literal journaled against the compiler-side PS1 const), and rides the h019 carrier seats at the unit-171/175 renders (globalMax/Dice two-global receivers). The unit-168 _extract render quantizes two vendor UB seats (the start-of-0 negative index and the through-N copy clamp, both journaled); the unit-175 Dice render journals the val-receiver caller-mutation divergence (the rewritten B reference lands on a local copy). Check columns track the BASE reference translation (C-002) and stay todo per the C-001 cycle-10 backfill decision.

| # | Unit | Lang | Class | Status | DOC | DAF | BOTH | code-check | output-check | runtime-check | target-check |
|---|------|------|-------|--------|-----|-----|------|-------------|--------------|---------------|--------------|
| 165 | interp-arith.cpp::mps_abs | cpp | core | todo | translated | translated | translated | todo | todo | todo | todo |
| 166 | libmpscpp.cpp::StopInit | cpp | core | todo | translated | translated | translated | todo | todo | todo | todo |
| 167 | interp-globalb.cpp::Mglobal | cpp | core | todo | translated | translated | translated | todo | todo | todo | todo |
| 168 | interp-sym.cpp::_extract | cpp | core | todo | translated | translated | translated | todo | todo | todo | todo |
| 169 | include/mumpsc/interp.h::<misc> | c | core | todo | translated | translated | translated | todo | todo | todo | todo |
| 170 | interp-sysfunc.cpp::TimeOut_getstr1 | cpp | core | todo | translated | translated | translated | todo | todo | todo | todo |
| 171 | libmpscpp.cpp::global::Max | cpp | core | todo | translated | translated | translated | todo | todo | todo | todo |
| 172 | include/mumpsc/stateVector.h::<misc> | c | complex | todo | translated | translated | translated | todo | todo | todo | todo |
| 173 | compiler-parse.c::ExpandOrder | c | core | todo | translated | translated | translated | todo | todo | todo | todo |
| 174 | interp-arith.cpp::modulo | cpp | core | todo | translated | translated | translated | todo | todo | todo | todo |
| 175 | libmpscpp.cpp::Dice | cpp | core | todo | translated | translated | translated | todo | todo | todo | todo |

DOC B6 tranche 3 (units 176-182, the complex stratum minus the big three) translated 2026-11-21: seven units pushed, whole-arm kotlinc gate 0/0 at 178 files; gradle gate on m1-build pending at tranche close. Rows follow the pre-registered batch_plan.md order. The tranche resolves the remaining raised H-014 seats (Stubs.shred, the 6-arg Stubs.match) and lands the generated-benchmark main (mpstime), the server transaction main, and the read/write compiler pair. Check columns track the BASE reference translation (C-002) and stay todo per the C-001 cycle-10 backfill decision.

DAF B6 tranche 3 (units 176-182, the complex stratum minus the big three) translated 2026-11-22: seven units pushed, whole-arm kotlinc gate 0/0 at 93 files (both the gate copy and the fresh repo-copy submission, 86 post-T2 base + 7 tranche renders); DAF consults ledgered in the DAF heuristic log ids 175-183; dafny suite 70 verified 0 errors (unchanged; all T3 units EXEMPT). The tranche releases the shredD stub of LibmpscppMisc9 (the prior selftext.cpp blame was wrong: the batch-6 roster row 176 IS the libmpscpp.cpp Shred overload; real body at the unit-176 Shred.kt render), carries the wstemD TAG CORRECTION at LibmpscppMisc5 (owner interp-stem.cpp::wstem at 418, a roster batch-10 cell; the stub STAYS until b10 owns the body), raises inoutDaf (compiler-sys1.c::inout at 292, roster b2), substrDaf (interp-strmanip.cpp::substr, roster b7), zfcnDaf (interp-zfcns.cpp::zfcn, the roster b6 big-three unit 184 that stays queued), numcompDaf/addDaf/subDaf (interp-arith.cpp), strxcatDaf (mpstime.cpp:102 this-TU inline), the variableNotFound family (interp-sysfunc.cpp), and the POSIX platform seats of the server transaction loop (ftokDaf/msggetDaf/msgrcvDaf/socketDaf/sendtoDaf/closeDaf). The mpstime main folds the setjmp/longjmp for-loop state machine onto structured iteration and keeps the M3701 coded bytes (CE, CF comma, 01 mark) as seated Char literals; the queue order stands per the PI decision logged at tranche 1: the 183-185 big-three PI session next, then BOTH B5 T2. Check columns track the BASE reference translation (C-002) and stay todo per the C-001 cycle-10 backfill decision.

BOTH B6 tranche 3 (units 176-182, the complex stratum minus the big three) translated 2026-11-22: seven units pushed, whole-arm kotlinc gate SILENT EXIT=0 at 57 files (50 post-T2 base + 7 tranche renders); dafny suite 40 verified 0 errors (unchanged; all T3 units EXEMPT, journaled - the DAF B6 T3 precedent applies and no new spec was added). Rows follow the pre-registered batch_plan.md order. The tranche lands the generated-benchmark main (mpstime), the server transaction main (the POSIX edges ftok/msgget/msgrcv/socket/sendto/perror seat as named-owner platform stubs, the msgrcv type-passed-uninitialized UB seats at 0, the argv[i+i] index bug kept byte-faithful), the read/write compiler pair, the stem-engine residual, the pattern-match classifier (the P-class punctuation literal kept byte-faithful through the escape seats, the short* rslt out-param folds to the Int return), and the stateful fragment iterator (the h008 static-scratch first consults-as-family at BOTH: the file-scope statics persist across calls as top-level private vars per the DOC ScanScratch precedent shape; the xindex/substr named-owner stubs raised at the match render; the isalnum/tolower C-locale byte-domain helpers seated per-file). Events +118 rows (band 23:30:00-23:31:57, 59 consults each reuse+hit; the complete per-render consult ledger in the journal fragment). Check columns track the BASE reference translation (C-002) and stay todo per the C-001 cycle-10 backfill decision. Next per the committed order: the 183-185 big-three PI session (render-only), then the batch-6 close.

BOTH B6 big three (units 183-185, the complex stratum heads) translated 2026-11-22/23 across the PI render session: three renders plus three existing-file flips pushed in the tranche commit; whole-arm kotlinc gate SILENT EXIT=0 at 60 files (57 post-T3 base + BifsZfcn/ZfcnsZfcn/Interpret; the zcn 184 flip, the zcnBifs 183 build-alternate, and the Interpret 185 MumpsMain flip land in the existing files). The tranche lands the zfcn build-alternate (H-017: the vendor Makefile archives interp-zfcns.o not interp-bifs.o, so zfcnBifs is the named alternate; 22 GTK label bodies over the h020 Long-handle stubs; zsqlite live SQLITE arms over the defines.h 25-29/FLUSH 21/SQLCMD 55 pins; the znxtrow dead arm behind the h013 false gate), the live zfcn reference face (the ZW_GBLNME 20 arm; the TmZb rename ZwTmZb records the JVM facade lesson: private top-level classes clash across TUs, private funs do not; the SQLITE db-info faces with the zindexmax 14-2 fold), and the interpreter mainline (the 1581-LOC goto ladder folded to the 80-state ItLbl loop lattice; the 12 owner-pinned face stubs; the JOB fork, KILL XNEXT, PULSE, and TERMINATE_ON_ERROR arms gated per the h018 reference build config; the abrt NOERR redirect returns 99). Events +50 rows (band 2026-11-23T00:14:00-00:14:49, 25 consults each reuse+hit across the three renders; the per-render consult ledger in the journal fragment). Check columns track the BASE reference translation (C-002) and stay todo per the C-001 cycle-10 backfill decision. Next per the committed order: the batch-6 close (m1-build gradle rides the close).

| # | Unit | Lang | Class | Status | DOC | DAF | BOTH | code-check | output-check | runtime-check | target-check |
|---|------|------|-------|--------|-----|-----|------|-------------|--------------|---------------|--------------|
| 176 | libmpscpp.cpp::Shred | cpp | complex | todo | translated | translated | translated | todo | todo | todo | todo |
| 177 | interp-stem.cpp::<misc> | cpp | complex | todo | translated | translated | translated | todo | todo | todo | todo |
| 178 | compiler-write.c::write_code | c | complex | todo | translated | translated | translated | todo | todo | todo | todo |
| 179 | server.cpp::main | cpp | complex | todo | translated | translated | translated | todo | todo | todo | todo |
| 180 | interp-parse.cpp::match | cpp | complex | todo | translated | translated | translated | todo | todo | todo | todo |
| 181 | compiler-read.c::read_code | c | complex | todo | translated | translated | translated | todo | todo | todo | todo |
| 182 | mpstime.cpp::main | cpp | complex | todo | translated | translated | translated | todo | todo | todo | todo |
| 183 | interp-bifs.cpp::zfcn | cpp | complex | todo | translated | translated | translated | todo | todo | todo | todo |
| 184 | interp-zfcns.cpp::zfcn | cpp | complex | todo | translated | translated | translated | todo | todo | todo | todo |
| 185 | interp-interp.cpp::Interpret | cpp | complex | todo | translated | translated | translated | todo | todo | todo | todo |

DOC B6 big-three session (units 183-185) translated 2026-11-21: three
units pushed, whole-arm kotlinc gate 0/0 at 181 files; gradle gate on
m1-build pending at session close. Zfcns (unit 184) is the Makefile
reference-build TU and owns the top-level zfcn plus the Stubs.zfcn
seat flip; interp-bifs (unit 183) renders as the build-alternate
zfcnBifs with the H-017 collision note (Makefile does NOT compile
interp-bifs.o). Unit 185 resolves the mis-tagged Stubs.Interpret
seat to the unit-185 delegate and widens the BuildGlobal/BuildLocal
seats (the all-null stack-cleaner arm, H-012), plus new
Stubs.inout/Stubs.Merge roster seats. Check columns track the BASE
reference translation (C-002) and stay todo per the C-001 cycle-10
backfill decision. After this session the batch-close protocol
(coverage checklist, events export, PILOT prose) follows.

DAF B6 big-three session (units 183-185) translated 2026-11-22:
three units pushed, whole-arm kotlinc gate 0/0 at 96 files (both
the gate copy and the fresh repo-copy submission, 93 post-T3 base
+ 3 session renders); DAF consults ledgered in the DAF heuristic
log ids 184-188; dafny suite 70 verified 0 errors (unchanged; all
three EXEMPT, no new spec). The session transplants the DOC
big-three scaffold of 2026-11-21 onto the DAF gate seats: Zfcns
(unit 184) is the LIVE reference-build TU rendering the vendor
symbol as fun zfcnDaf (releasing the zfcnDaf seat raised at unit
182) and raising serviceInterfaceDaf with a TAG CORRECTION
(interp-service.cpp::ServiceInterface at 87 IS units.csv row 187
batch 7 - the SymHMisc not-a-roster-unit blame was wrong) plus
gtkTreeAddD (row 83) and the gtk/mps raise families; ZfcnBifs
(unit 183) renders the DEAD build-alternate twin as zfcnBifsDaf
with the zreplace arm (replaceDaf raised at the render, a
bifs-only seat) and resolves its recursion to the live twin;
Interpret (unit 185) releases the InterpretDaf seat of
DafInterpStubsT3, pins the InterpHMisc ledgered interp.h
command-char lattice, raises inoutInterpDaf (the interp-parse.cpp
4-arg b10 sibling; the WriteCode inoutDaf is the b2 3-arg twin),
symFreeDaf (row 28), mergeDaf (row 226), directReadDaf (row 24),
getstr2Daf and prnterrDaf, folds the in/out_file Reader/Writer
carriers onto the T2 IntArray handle domain, folds the NEW/KILL
except-list chain onto the render state, and extends the DafMsv
carrier with nflg. Queue after this session: BOTH B5 T2 stays
first in the queue per the PI instruction that DAF finishes
first; the batch-close protocol follows. Check columns track the
BASE reference translation (C-002) and stay todo per the C-001
cycle-10 backfill decision.

**DAF B6 closed 2026-11-22**: 31/31 translated (T1 155-164 push
f467a08/splice 84b53df, T2 165-175 d9aa0be with the 1306b2d
push-repair/splice 66045d6, T3 176-182 69cba05/splice 04cf3c0
with fragment removal b58f99f, big-three session 183-185
8639236/splice b14103a with removal cadb7cf); whole-arm kotlinc
gate SILENT EXIT=0 at every close (75 -> 86 -> 93 -> 96 files,
gate copy plus fresh repo-copy submission each time); gradle
gate BUILD SUCCESSFUL at every close (m1-build for T1-T3; the
MacBook splice close carried the six accepted always-true/false
condition warnings of Interpret.kt, vendor-lattice faithful);
dafny suite 70 verified 0 errors final (the new mps_log2 spec
at +5 in T1 and extract spec at +15 in T2; every other unit
EXEMPT). The DAF arm carries no cookbook, rules base or events
CSV - the heuristic log is the arm's instrument - so the batch
close records instrument-row tallies instead of an events
regeneration: batch rows 148-188 (31 unit rows, outcome totals
17 hit-reuse / 14 hit-adapt / 0 miss, hit rate 100.0%, reuse
rate 54.8%; 4 seats rows, 2 spec rows, 4 gate rows), 0 red
gate builds for the batch, and the arm-close seat ledger, all
in the arm journal close section. With this close the DAF arm
is COMPLETE: batches 4-6, units 94-185, 92/92 translated and
gate-green; the named-owner seats raised against roster cells
outside the arm's drawing (b1/b2/b7/b8/b9/b10 owners, the POSIX
platform and GTK glue families, the non-roster families) stay
as the honest UNRESOLVED stub record per the H-014 discipline -
their owners live in the other arms' drawings. Check columns
stay todo per the C-001 cycle-10 backfill decision. Next
protocol step per the committed order: BOTH B5 T3 (complex
144-154) - BOTH B5 T2 had already closed 2026-11-21 (push
eb6d33c, splice 85caa1a) before this close prose was written;
the queue-note staleness is repaired in the arm journal repair
note of the same date.

**DOC B6 closed 2026-11-21**: 31/31 translated (T1 155-164 commit
ee0bd410/splice cac8a53, T2 165-175 74264c1/fe438e7, T3 176-182
a89e8b5/caf23fa, big-three session 183-185 9240c8a/6bca28d);
whole-arm kotlinc gate 0/0 at every tranche (162 to 181 files);
gradle gate BUILD SUCCESSFUL on m1-build at every tranche (T2
15s, T3 14s, big three 21s, T1 green at its splice close).
Coverage checklist and close repairs recorded in the arm journal
(consult ledger normalized to roster ids; two batch-6 table ids
display-fixed against units.csv, rows 167 and 170). Events CSV
regenerated per the C-005/C-008 keys (627 consult rows -> 1252
event rows; pre-B6 content verified as a byte-exact prefix).
Check columns stay todo per the C-001 cycle-10 backfill decision.
Next protocol step: batch 7 per the committed row order.

## Batch 7 (units 186-215, opened 2026-11-20; DOC follows the closed B6 in the committed row order)

Shaping per the PRO precedent: T1 simple (186-195), T2 core (196-205), T3 complex (206-215).

DOC B7 tranche 1 (units 186-195, the simple stratum) translated
2026-11-20: ten units pushed, whole-arm kotlinc gate 0/0 at 188
files; gradle gate on m1-build pending at tranche close. Rows
follow the pre-registered batch_plan.md order (simple stratum,
complexity rank ascending). The tranche resolves two H-014 stub
seats (Stubs.substr flips to the unit-190 render; Stubs.Mpiece
flips with TAG CORRECTION to the landed unit-114 _piece) and
rebinds the compiler-side substr call sites (Match.kt,
PatrnSys1.kt) to the unit-189 short-domain twin per the unit-59
inout precedent. Check columns track the BASE reference
translation (C-002) and stay todo per the C-001 cycle-10
backfill decision.

DOC B7 tranche 2 (units 196-205, the core stratum) translated
2026-11-20: ten units pushed, whole-arm kotlinc gate 0/0 at 196
files, first-pass green; gradle gate on m1-build pending at
tranche close. Rows follow the pre-registered batch_plan.md order
(core stratum, complexity rank ascending). The tranche closes two
long-standing H-014 seats (Stubs.makeGlobal flips to the unit-199
render with the new GlobalRef member-state carrier seats;
Stubs.sim1 flips to the unit-204 render), raises one new seat
(Stubs.count, libmpscpp.cpp:2932), and carries the first
hit-adapt consults of the batch (199, 202, 203, 204). Check
columns track the BASE reference-translation (C-002) and stay todo
per the C-001 cycle-10 backfill decision.

DOC B7 tranche 3 (units 206-215, the complex stratum) translated
2026-11-20: ten units pushed, whole-arm kotlinc gate 0/0 at 206
files, first-pass green; gradle gate on m1-build pending at
tranche close. Rows follow the pre-registered batch_plan.md
order (complex stratum). The tranche closes six long-standing
H-014 seats (Stubs.sigint, the 5-arg Stubs.match, Stubs.freadx,
the Qsub seat family, the compiler epi/EP/fcninit seats,
Stubs.ServiceInterface) and resolves the Stubs.count seat seated
by tranche 2; it raises the batch-10 Concat(x7) seat family and
the Stubs.IDF seat, and carries one compiler tag correction
(EP ExecName Int / pid Long). Check columns track the BASE
reference-translation (C-002) and stay todo per the C-001
cycle-10 backfill decision.

DAF B7 tranche 1 (units 186-195, the simple stratum) translated
2026-11-23: ten units pushed, whole-arm kotlinc gate SILENT EXIT=0 at
106 files (the gate copy; the fresh repo-copy submission rides this
push); DAF consults ledgered in the DAF heuristic log ids 189-200;
dafny suite 70 verified 0 errors (unchanged; all ten T1 units EXEMPT
or residue). Rows follow the pre-registered batch_plan.md order
(simple stratum, complexity rank ascending). The tranche releases the
substrDaf seat (the B6 T3 raise) at the unit-190 render with the
compiler-side Match.kt call sites REBOUND to the unit-189 TU-local
sys1 twin per the DOC unit-59 inout precedent, carries same-family
overload adjudications (the unit-186 Find char*-key twin; the
unit-192 GlobalData mstring needle at libmpscpp.cpp:2616, dump
loc-verified), and notes the SymHMisc/StrmanipMisc tag corrections
for the T3 sigint/serviceInterface renders. Check columns track the
BASE reference translation (C-002) and stay todo per the C-001
cycle-10 backfill decision.

DAF B7 tranche 2 (units 196-205, the core stratum) translated
2026-11-24: ten units pushed, whole-arm kotlinc gate SILENT EXIT=0 at
116 files (the gate copy plus the fresh repo-copy submission, both
green with only the accepted Interpret.kt vendor-lattice warning set);
DAF consults ledgered in the DAF heuristic log ids 201-212; dafny
suite 70 verified 0 errors (unchanged; all ten T2 units EXEMPT or
residue). Rows follow the pre-registered batch_plan.md order (core
stratum). The tranche carries the unit-201 Multiply carrier-twin
adjudication (the char* body at :2252 verified line-for-line equal
to the landed unit-141 string form; no new body per the DOC unit-225
precedent), the in-file countDaf named-owner raise (owner
libmpscpp.cpp:2932), the rsltSpecD/callBackD top-level state raise,
the unit-198 interp-sysfunc SQLITE both-arms lattice (SQLITE_D gate,
GlobalQuery precedent), and the SSS/TTT in-place compaction fold of
the unit-203 compiler emitter. The SymHMisc/StrmanipMisc tag
corrections remain queued for the T3 sigint/serviceInterface
renders. Check columns track the BASE reference translation (C-002)
and stay todo per the C-001 cycle-10 backfill decision.

DAF B7 tranche 3 (units 206-215, the complex stratum) translated
2026-11-24: ten units pushed, whole-arm kotlinc gate exit 0 at 124 kt
files (the gate copy plus the fresh repo-copy submission, both green
with only the accepted Interpret.kt vendor-lattice warning set of 6
warnings) under the NEW scripted 4-stage gate driver gate.sh (the
C-010 protocol change adopted by the PI this session: gate kotlinc +
repo-copy recompile + dafny per-spec loop with expected-count +
H-015 hygiene, all scripted; the scripted run caught 1 authored
string backtick + 2 authored comment backticks + 2 pre-existing
comment dollars in touched files, all normalized before the green
run). DAF consults ledgered in the DAF heuristic log ids 213-224;
dafny suite 70 verified 0 errors (unchanged; all ten T3 units EXEMPT
or residue). Rows follow the pre-registered batch_plan.md order
(complex stratum). The tranche closes batch 7 for the DAF arm (30/30
batch-7 units across T1/T2/T3). Seat releases this tranche:
levCheckDaf (unit 206, the not-a-corpus-unit blame corrected),
epiDaf (raised then released at the unit-213 epi render),
sigintDaf (unit 211), countDaf (unit 214, the T2 raise now owned
by the misc-6 residual cell), serviceInterfaceDaf (unit 215, the
SymHMisc not-a-roster-unit TAG CORRECTION; the StrmanipMisc:30-32
tag correction rode the unit-211 render). The unit-212 match twin
is a LEDGER APPEND discharge (the sys1 body verified line-for-line
identical to the landed unit-180 modulo casts and the bd
global-vs-param fold); the unit-214 misc-6 chunk keeps both
chunk-boundary orphans ledgered (the mcvt char* twin stays the
Misc-5 carry stub; the Concat x7 bodies beyond the tail cut ride
the concatDaf named stub). CORRECTION of the T2 close prose: the
T2 paragraph below says 116 files; the true post-T2 kt count was
115 (9 new files on the 106 of T1); no CSV cell carries the error
(prose-only); the 124-file post-T3 count of this paragraph is the
corrected arithmetic (115 pre-batch + 10 new T3 files minus the
Match.kt append = 9 new files). Check columns track the BASE
reference translation (C-002) and stay todo per the C-001 cycle-10
backfill decision. Next protocol step: DAF batch 8 per the
committed row order (one session per batch for batches 8-10 per
the C-011 protocol change).

BOTH B7 tranche (units 186-215, 30 units in the committed row
order: ServiceInterface, misc-6, epi, match, sigint, zcommands_code,
freadx, Qsub, fcninit, mumps-misc, embed1, Extract, Sim1,
rslt_callback, Multiply, fcn-misc, MakeGlobal, sysfunc-GlobalData,
externs-misc, Fnumber, mkpt, _justify, libmpscpp-GlobalData, Soundex,
Translate, strmanip-substr, sys1-substr, Piece, zcommands-misc, Find)
translated 2026-11-27: thirty units pushed, whole-arm kotlinc gate
EXIT=0 at 90 files, 0 errors, hygiene clean. Seat releases with
notes: sim1SvcZb (Sim1 owns kernel+face per the Sim1W adjudication:
corpus seat kernel :1565 unit-198-equivalent, face :10431 wrapper;
unit-186 pin corrected), makeGlobalZb, globalDataC (BothStubs fold),
fcninitZb, EPZb, substr stub (realloc arm 211 distinct from 255-clamp
212: separate kernels, not a fold), sigint stub, BifsZfcn stub. New
TODO stubs raised: _pieceZb (owner interp-piece.cpp misc, roster 103
b4), GlobalNode.data() (MultiplyString, roster 259 family),
zero-arg GlobalRef.invoke() placeholder (MultiplyString). Duplicate-
body folds disclosed: matchSys1 (match delegates to Match), Multiply
wrapper. Quantizations disclosed: mkpt static residue kept,
embed1 alias, zcommands Sysout %d UB, btree NPE/stale, ReadLine EOF
UB (carried from misc-6). Session-span disclosure: the batch spanned
multiple resume turns (sandbox wiped 11 times total); continuation
consult rows ride the 2026-11-27 session stamp per the C-011
one-session discipline (186-191 on the nominal 11-26 band,
192-215 on the continuation band). Cosmetic rank-pin mislabels in
stub comments (sigint 254->190, parse_ 248->247, Extract 171->17,
multiply 213->41, Mltoa 292->291, fcn 123->94, substr 212->211)
NOT renumbered pending PI note. Events CSV: 126 consult rows append (rows 504-629,
32 on the 11-26 nominal band, 94 on the 11-27 continuation
band), file 503 -> 629 lines, at
push. Next protocol step: DAF supplementary render session (four
stub rows per the 2026-11-26 PI adjudication), then BOTH batch 8.

| 186 | libmpscpp.cpp::global::Find | cpp | simple | todo | translated | translated | translated | todo | todo |
| 187 | compiler-zcommands.c::<misc> | c | simple | todo | translated | translated | translated | todo | todo |
| 188 | libmpscpp.cpp::global::Piece | cpp | simple | todo | translated | translated | translated | todo | todo |
| 189 | compiler-sys1.c::substr | c | simple | todo | translated | translated | translated | todo | todo |
| 190 | interp-strmanip.cpp::substr | cpp | simple | todo | translated | translated | translated | todo | todo |
| 191 | libmpscpp.cpp::mstring::Translate | cpp | simple | todo | translated | translated | translated | todo | todo |
| 192 | libmpscpp.cpp::GlobalData | cpp | simple | todo | translated | translated | translated | todo | todo |
| 193 | libmpscpp.cpp::global::Soundex | cpp | simple | todo | translated | translated | translated | todo | todo |
| 194 | interp-sym.cpp::_justify | cpp | simple | todo | translated | translated | translated | todo | todo |
| 195 | interp-sym.cpp::mkpt | cpp | simple | todo | translated | translated | translated | todo | todo |
| 196 | libmpscpp.cpp::global::Fnumber | cpp | core | todo | translated | translated | translated | todo | todo |
| 197 | include/mumpsc/externs.h::<misc> | c | core | todo | translated | translated | translated | todo | todo |
| 198 | interp-sysfunc.cpp::GlobalData | cpp | core | todo | translated | translated | translated | todo | todo |
| 199 | libmpscpp.cpp::global::MakeGlobal | cpp | core | todo | translated | translated | translated | todo | todo |
| 200 | interp-fcn.cpp::<misc> | cpp | core | todo | translated | translated | translated | todo | todo |
| 201 | libmpscpp.cpp::Multiply | cpp | core | todo | translated | translated | translated | todo | todo |
| 202 | interp-sql.cpp::rslt_callback | cpp | core | todo | translated | translated | translated | todo | todo |
| 203 | compiler-parse.c::Extract | c | core | todo | translated | translated | translated | todo | todo |
| 204 | libmpscpp.cpp::Sim1 | cpp | core | todo | translated | translated | translated | todo | todo |
| 205 | libmpscpp.cpp::embed1 | cpp | core | todo | translated | translated | translated | todo | todo |
| 206 | compiler-mumps.c::<misc> | c | complex | todo | translated | translated | translated | todo | todo |
| 207 | compiler-fcninit.c::fcninit | c | complex | todo | translated | translated | translated | todo | todo |
| 208 | interp-sym.cpp::Qsub | cpp | complex | todo | translated | translated | translated | todo | todo |
| 209 | interp-native-btree.cpp::freadx | cpp | complex | todo | translated | translated | translated | todo | todo |
| 210 | compiler-zcommands.c::zcommands_code | c | complex | todo | translated | translated | translated | todo | todo |
| 211 | interp-sysfunc.cpp::sigint | cpp | complex | todo | translated | translated | translated | todo | todo |
| 212 | compiler-sys1.c::match | c | complex | todo | translated | translated | translated | todo | todo |
| 213 | compiler-epi.c::epi | c | complex | todo | translated | translated | translated | todo | todo |
| 214 | libmpscpp.cpp::<misc-6> | cpp | complex | todo | translated | translated | translated | todo | todo |
| 215 | interp-service.cpp::ServiceInterface | cpp | complex | todo | translated | translated | translated | todo | todo |

**DOC B7 closed 2026-11-21**: 30/30 translated in tranches T1-T3
(journal splices 377302d, 3f26a71, b909dc3; whole-arm kotlinc gate
0 errors / 0 warnings at 188, 196 and 206 files at the tranche
closes, first-pass green). Coverage checklist and events
verification recorded in the arm journal close fragment (consult
ledger: 30 consults, one per unit per C-008: 17 hit-reuse,
13 hit-adapt, 0 miss; events CSV regenerated under the
C-005/C-008 keys from 657 consult rows -> 1312 event rows,
byte-identical to the tracked file, 87355 bytes - the R
instrument re-verification runs with the m1-build gate round).
Batch-7 stub ledger: eleven resolutions (substr compiler twin
rebind + interp flip, Mpiece with tag correction, makeGlobal,
sim1, sigint, the 5-arg match, freadx, the Qsub family,
fcninit/epi with EP/LevCheck and the ExecName Int / pid Long
corrections, ServiceInterface, count raised-and-resolved); new
batch-10 seats raised: IDF and the Concat twelve-overload family.
Gradle gate on m1-build pending at this close (the gate round
journal entry follows). Check columns track the BASE reference
translation (C-002) and stay todo per the C-001 cycle-10 backfill
decision. Next protocol step: DOC batch 8 per the committed row
order (cycle 7).

## Batch 8 (units 216-246, opened 2026-11-24; DAF runs the batch in ONE session per the C-011 protocol change)

DAF batch 8 (units 216-246) translated 2026-11-24 in ONE session per
C-011: thirty-one units pushed, whole-arm kotlinc gate exit 0 at 151
kt files (gate + repo copies, only the accepted Interpret.kt
vendor-lattice warning set of 6 warnings) under the scripted 4-stage
gate driver (C-010). DAF consults ledgered in the DAF heuristic log
ids 225-257 (roster rows 216-246, seats row 256, gate row 257);
dafny suite 70 verified 0 errors UNCHANGED (all 31 batch-8 units
EXEMPT or residue; no new spec). Rows follow the pre-registered
batch_plan.md order. Seat releases this batch: atCopyDaf (222, the
unit-215 raise resolves), idfW (223), scanParseDaf (229),
buildGlobalDaf (230), buildLocalDaf (231), mergeDaf (237, the
unit-214 raise resolves), gtkTreeAddDaf (238), allocSVDaf (244),
concatDaf (245, the collapsed stub DELETED; the twelve Misc6 amp
relays resolve to real bodies; no external callers existed),
btreeDaf (246, Mglobal stub removed; TAG CORRECTION: the
interp-native-btree blame was wrong), and the unit-243 fold-in
releases memcheckDaf/cvarDaf/extract1Daf. New raises: extract2Daf
(241, named stub), fixGblRefD (245), sprintfInt228D (228),
conCheckD/strcasecmpD/strncasecmpD (243),
copyFromCannonD/shiftLeftCannonD (240). Hygiene probe refinements of
the gate: the Kotlin keyword escape System.`in` is exempt from the
backtick probe (CompilerGetstr1 via the Interpret.kt precedent) and
the dollar probe is comment-dollars only; pre-existing comment
dollars normalized: GlobalPiece.kt:9, GlobalData.kt:4/34,
MdhTreeLevelAdd.kt:5/26/47 (compile-identical). ADJUDICATION
DISCLOSED for the PI: the CODED lattice (all four vendor defines.h
copies set CodedClose 207 / CodedComma 208; DAF's
MstringFnumber.kt:17-19 inverts the two NAMES but keeps the vendor
values and is arm-internal and symmetric) - no code change pending
ruling; and the DOC-vs-DAF fresh tail-id divergence of batch 8
(DAF 237=Merge / 240=cannon / 241=mumps_open / 243=compiler-parse
misc / 244=AllocSV / 245=libmpscpp misc-7 / 246=interp-sql btree);
DOC numbers its own batch-8 tail differently per its committed
order. ENV RESET DISCLOSURE: the sandbox wiped mid-close after the
first green scripted run; the gate re-ran clean from the knowledge
checkpoints with identical counts (151 files, dafny 70/0, hygiene
clean on the 37 rendered/edited files), and the heuristic append
ids 225-257 was rebuilt from the session ledger after the reset.
Check columns track the BASE reference translation (C-002) and stay
todo per the C-001 cycle-10 backfill decision. Next protocol step:
DAF batch 9 per the committed row order (one session per C-011).

BOTH batch 8 (units 216-246) translated 2026-11-27 in ONE session
per C-011: thirty-one units pushed, whole-arm kotlinc gate EXIT=0
at 116 files (0 errors, 0 warnings, hygiene clean: no tabs, ASCII
only, no backticks, comment dollars escaped). Rows follow the BOTH
committed units.csv record order (the table above is DOC's
committed order, the reverse; flips matched BY ID STRING per the
B7 lesson). Seat releases this batch: allocSV (218, BothStubs
raise resolves), atCopy (240, the WriteCode raise resolves),
parse_ zero-arg duplicate removed at gate (221; the WriteCode:274
seat was already called direct), memcheck/cvar/Extract1 (243,
verification fold onto ExtractCompiler.kt:53 and new renders),
rsltSpec/call_back (227, verification folds onto the SqlRuntime
object, RsltCallback.kt:50-51), the 220 initializer completion
(ErrChar, MaxTmp, for_mult, flg and the except literals seated in
CompilerGlobalsExtMisc), and the 234 CodedOpen/CodedClose hex
emission splice to Extract2a direct. New TODO stubs raised:
GlobalNode.IDF (239), mult (222, owner interp-arith roster 67),
Extract2 (221, owner compiler-parse.c::Extract2, batch 2/3
pending), LocalOrder (231), GlobalOrder (232), GTK platform edge
(224, the mdh stub family), embed (216; record 43 COVERAGE
FINDING: no render and no events rows exist on master for the
batch-3 unit, embed1 is the distinct double-quote twin), sql
6-arg (216, the interp-sql.cpp engine face, distinct from the
libmpscpp 5-string-arg Sql.kt face), mstringEvalZb (217; record
77 COVERAGE FINDING: no render and no events rows on master),
mstringFixGblRefZb (217, record 262 batch 9 pending). Verification
twins confirmed line-for-line with no new code: 235 ReadLine
(readLineG, LibmpscppMisc6:178), 236 mdh_tree_level_add
(MdhTreeLevelAdd.kt), 237 GlobalData (GlobalDataMstring.kt,
header pre-adjudicated), 238 global::Piece(pat,ii,jj) (the
Piece.kt GlobalRef overload). Quantizations and quirks disclosed:
getstr1 opnflg==3 raw-fd dance h020 with the dead newline branch
and area[1000] off-by-one kept, ScanParse paren-depth unclamped,
Merge >127 backward terminator scan with SOH fallback and the FIX
macro drops, btree ORDER p1+=12 off-by-one, XNEXT a8-skip typo,
GKILL tokens=0 shadow, STORE xMaximum typo, dead keybak strcpy
and dead printf quantized h013, the 14==14 preprocessor ladder,
misc-7 unsigned/short/float arms folded onto Int/Long/Double
seats h021 with the vendor literal-d typo kept. MISLABEL FAMILY
disclosed, NOT renumbered pending PI note: stub pins cite
complexity ranks or grep lines, not ordinals (allocSV roster 285
is the rank, true ordinal 218; atCopy roster 241 is a grep line,
true ordinal 240; idfSvcZb 45 and the InterpService pins Scan 75,
ScanAlnum 77 are ranks). SESSION-SPAN DISCLOSURE: the sandbox
wiped three times mid-session; every rebuild restored the gate
from the knowledge stage (90 baseline files, vendor/kotlinc/JDK
SHAs re-verified) and re-ran green before rendering resumed; all
31 units and their consult rows are ONE C-011 session on the
2026-11-27 continuation band. Events CSV: 130 consult rows append
(rows 630-759, band opened 2026-11-27T18:30:00Z, +30 s/row; the
nominal 16:00Z band was consumed by the DAF supplementary
continuation rows), file 629 -> 759 lines, at push. Check columns
track the BASE reference translation (C-002) and stay todo per
the C-001 cycle-10 backfill decision. Next protocol step: BOTH
batch 9 per the committed row order (one session per C-011), 93
units remaining to arm completion.

| 216 | include/mumpsc/builtinTable.h::<misc> | c | simple | todo | todo | translated | translated | todo | todo |
| 217 | libmpscpp.cpp::mstring::Find | cpp | simple | todo | todo | translated | translated | todo | todo |
| 218 | compiler-kill.c::<misc> | c | simple | todo | todo | translated | translated | todo | todo |
| 219 | compiler-lock.c::<misc> | c | simple | todo | todo | translated | translated | todo | todo |
| 220 | compiler-set.c::<misc> | c | simple | todo | todo | translated | translated | todo | todo |
| 221 | include/mumpsc/zfcns.h::<misc> | c | simple | todo | todo | translated | translated | todo | todo |
| 222 | compiler-mumps.c::atCopy | c | simple | todo | todo | translated | translated | todo | todo |
| 223 | libmpscpp.cpp::IDF | cpp | simple | todo | todo | translated | translated | todo | todo |
| 224 | libmpscpp.cpp::global::Piece | cpp | simple | todo | todo | translated | translated | todo | todo |
| 225 | libmpscpp.cpp::GlobalData | cpp | simple | todo | todo | translated | translated | todo | todo |
| 226 | libmpscpp.cpp::mdh_tree_level_add | cpp | simple | todo | todo | translated | translated | todo | todo |
| 227 | libmpscpp.cpp::global::ReadLine | cpp | core | todo | todo | translated | translated | todo | todo |
| 228 | compiler-parse.c::ExpandText | c | core | todo | todo | translated | translated | todo | todo |
| 229 | interp-sym.cpp::ScanParse | cpp | core | todo | todo | translated | translated | todo | todo |
| 230 | interp-parse.cpp::BuildGlobal | cpp | core | todo | todo | translated | translated | todo | todo |
| 231 | interp-sym.cpp::BuildLocal | cpp | core | todo | todo | translated | translated | todo | todo |
| 232 | compiler-parse.c::Extract2a | c | core | todo | todo | translated | translated | todo | todo |
| 233 | interp-zfcns.cpp::<misc> | cpp | core | todo | todo | translated | translated | todo | todo |
| 234 | compiler-sys1.c::getstr1 | c | core | todo | todo | translated | translated | todo | todo |
| 235 | interp-sql.cpp::<misc> | cpp | core | todo | todo | translated | translated | todo | todo |
| 236 | include/mumpsc/compiler-macros.h::<misc> | c | core | todo | todo | translated | translated | todo | todo |
| 237 | interp-sysfunc.cpp::Merge | cpp | complex | todo | todo | translated | translated | todo | todo |
| 238 | interp-bifs.cpp::GtkTreeAdd | cpp | complex | todo | todo | translated | translated | todo | todo |
| 239 | include/mumpsc/builtin.h::<misc> | c | complex | todo | todo | translated | translated | todo | todo |
| 240 | interp-parse.cpp::cannon | cpp | complex | todo | todo | translated | translated | todo | todo |
| 241 | compiler-open.c::mumps_open | c | complex | todo | todo | translated | translated | todo | todo |
| 242 | include/mumpsc/compiler-globals.h::<misc> | c | complex | todo | todo | translated | translated | todo | todo |
| 243 | compiler-parse.c::<misc> | c | complex | todo | todo | translated | translated | todo | todo |
| 244 | interp-sysfunc.cpp::AllocSV | cpp | complex | todo | todo | translated | translated | todo | todo |
| 245 | libmpscpp.cpp::<misc-7> | cpp | complex | todo | todo | translated | translated | todo | todo |
| 246 | interp-sql.cpp::btree | cpp | complex | todo | todo | translated | translated | todo | todo |

## Batch 9 (units 247-277, opened 2026-11-24; DAF runs the batch in ONE session per the C-011 protocol)

DAF batch 9 (units 247-277) translated 2026-11-24 in ONE session per
C-011: thirty-one units rendered and passing, whole-arm kotlinc gate
exit 0 at 177 kt files (gate + repo copies, only the accepted
Interpret.kt vendor-lattice warning set of 6 warnings) under the
scripted 4-stage gate driver (C-010). DAF consults ledgered in the
DAF heuristic log ids 258-290 (roster rows 247-277, seats row 289,
gate row 290); dafny suite 70 verified 0 errors UNCHANGED (all 31
batch-9 units EXEMPT or residue; no new spec). Rows follow the
pre-registered batch_plan.md order. Seat releases this batch:
horologDaf (247), xindexDaf (260), fixGblRefD (262, TAG CORRECTION:
row 262 owns it, the not-a-corpus blame superseded), the
GlobalNodeStub avg member (263), evalDaf (264, stub deleted;
the parseDaf b3 interp seat STAYS RAISED), linepadDaf + linepad2Daf
+ multDaf (267, two tag corrections: compiler-sys1 not
compiler-read, and the not-a-corpus blame superseded), perlPmD
(268, TAG CORRECTION: 268 not 256, the java.util.regex fold
disclosed), jaccardD (269), sqlDaf x2 both-arm seats (271, the
InterpSqlBtree raise note superseded; the java.sql JDBC
jdbc:sqlite divergence DOCUMENTED for adjudication), strxcatDaf
(272), and the bfrGetD/bfrEachD/writeBlockD public promotions
(273). New raises: extract2Daf stays raised and is called (265);
six raises for the parse_ render (277): patrn1Daf, fixstrDaf,
expandQSDaf, expandNextDaf, expandGetDaf, myFuncDaf. PI
adjudication flags this batch: the STR_MAX 500-vs-32000 strmax.h
blame inaccuracy (248, no const change), the scanblocksD
chunk-gap ledger (275), the inverted-compare quirks kept verbatim
in the row-98 overload lattice (276), the BtreeX KNOWN
APPROXIMATIONS ledger (275), and the row-277 pair: parse_ renders
on its own seat parseCDaf over the compiler globals with the
CodedOpen/CodedClose/CodedComma family mapped NAME-TO-NAME per
C-012, and the CompilerZcommandsCode zseek call site still binds
the raised interp seat parseDaf - the candidate rebind to
parseCDaf awaits Mark's ruling. Hygiene note: pre-existing comment
dollars normalized compile-identically in SetCode (4), ForCode (2),
LibmpscppMisc9 (2), GlobalOrderH (1), MpstimeMain (9), RpmMisc (1).
Check columns track the BASE reference translation (C-002) and stay
todo per the C-001 cycle-10 backfill decision. Next protocol step:
DAF batch 10 per the committed row order (one session per C-011).

| 247 | interp-sym.cpp::_horolog | cpp | simple | todo | todo | translated | todo | todo | todo |
| 248 | interp-bstring.cpp::bstrcat | cpp | simple | todo | todo | translated | todo | todo | todo |
| 249 | compiler-write.c::<misc> | c | simple | todo | todo | translated | todo | todo | todo |
| 250 | interp-sym.cpp::_random | cpp | simple | todo | todo | translated | todo | todo | todo |
| 251 | libmpscpp.cpp::GlobalOrder | cpp | simple | todo | todo | translated | todo | todo | todo |
| 252 | interp-arith.cpp::mps_log | cpp | simple | todo | todo | translated | todo | todo | todo |
| 253 | interp-arith.cpp::mps_cos | cpp | simple | todo | todo | translated | todo | todo | todo |
| 254 | interp-arith.cpp::mps_tan | cpp | simple | todo | todo | translated | todo | todo | todo |
| 255 | libmpscpp.cpp::global::Translate | cpp | simple | todo | todo | translated | todo | todo | todo |
| 256 | interp-arith.cpp::mps_exp2 | cpp | simple | todo | todo | translated | todo | todo | todo |
| 257 | interp-arith.cpp::mps_floor | cpp | core | todo | todo | translated | todo | todo | todo |
| 258 | libmpscpp.cpp::mdh_tree_level_add | cpp | core | todo | todo | translated | todo | todo | todo |
| 259 | include/mumpsc/globalOrder.h::GlobalOrder | c | core | todo | todo | translated | todo | todo | todo |
| 260 | interp-strmanip.cpp::xindex | cpp | core | todo | todo | translated | todo | todo | todo |
| 261 | interp-service.cpp::<misc> | cpp | core | todo | todo | translated | todo | todo | todo |
| 262 | libmpscpp.cpp::mstring::FixGblRef | cpp | core | todo | todo | translated | todo | todo | todo |
| 263 | libmpscpp.cpp::global::Avg | cpp | core | todo | todo | translated | todo | todo | todo |
| 264 | interp-sym.cpp::Eval | cpp | core | todo | todo | translated | todo | todo | todo |
| 265 | compiler-parse.c::ExpandName | c | core | todo | todo | translated | todo | todo | todo |
| 266 | interp-sym.cpp::<misc> | cpp | core | todo | todo | translated | todo | todo | todo |
| 267 | compiler-sys1.c::<misc> | c | core | todo | todo | translated | todo | todo | todo |
| 268 | interp-rpm.cpp::perl_pm | cpp | complex | todo | todo | translated | todo | todo | todo |
| 269 | libmpscpp.cpp::Jaccard | cpp | complex | todo | todo | translated | todo | todo | todo |
| 270 | include/mumpsc/cgi.h::<misc> | c | complex | todo | todo | translated | todo | todo | todo |
| 271 | interp-sql.cpp::sql | cpp | complex | todo | todo | translated | todo | todo | todo |
| 272 | mpstime.cpp::<misc> | cpp | complex | todo | todo | translated | todo | todo | todo |
| 273 | interp-native-btree.cpp::<misc> | cpp | complex | todo | todo | translated | todo | todo | todo |
| 274 | compiler-kill.c::kill_code | c | complex | todo | todo | translated | todo | todo | todo |
| 275 | interp-native-btree.cpp::btreeX | cpp | complex | todo | todo | translated | todo | todo | todo |
| 276 | libmpscpp.cpp::<misc-3> | cpp | complex | todo | todo | translated | todo | todo | todo |
| 277 | compiler-parse.c::parse_ | c | complex | todo | todo | translated | todo | todo | todo |

BOTH B5 tranche 3 (units 144-154, the complex stratum, batch close) translated 2026-11-22: eleven units pushed (sym.h misc, Qlength, SynInit, scan_token, bmg_search, mstringClass misc, mumps main, compiler-globals-ext misc, rpm misc, sql, libmpscpp misc-5), whole-arm kotlinc gate SILENT EXIT=0 at 32 files (gate copy plus the fresh repo-copy submission); dafny suite 30 verified 0 errors unchanged (all T3 units EXEMPT, no new spec); the eleven BOTH rows below marked translated and batch 5 is complete for the BOTH arm (31/31 translated). BOTH consults ledgered in the events CSV at the 19:05:00-19:05:52 band (53 event rows: 34 reuse, 16 hit, 3 miss). The tranche resolves the acceptIt seat (the roster-271 rpm misc render releases the T2 parse_count stub flip), lands the global-class Transpose member (the T2 transpose stub flip; both carried over the GlobalNode order/kill/valuation seats against the globalClass.h layout owner roster 259 - the T1 roster-89 blame is corrected in the seat header), extends the Msv carrier with the tranche-3 stateVector.h fields (xd/t0px/xpx/t2/program), and seats the GTK platform family plus the CLOSE/INVALID_CONVERSION/SYNMAX pins. OPEN ITEM: the m1-build gradle gate round and the MacBook gate (gradle build from wp2/arms/BOTH) follow the splice at batch close review; the BATCH-CLOSE protocol (coverage checklist, C-008 events export, journal close prose) runs next per the committed order. Check columns track the BASE reference translation (C-002) and stay todo per the C-001 cycle-10 backfill decision.

**BOTH B5 closed 2026-11-22**: 31/31 translated (arm opened + T1 124-133 push 9184cea0d with newline fixups 00a0643a5/6e0e7dd20, T2 134-143 push eb6d33c9a, T3 144-154 push c306b0c2c); whole-arm kotlinc gate SILENT EXIT=0 at every tranche close (15 -> 32 -> 32 files, gate copy plus fresh repo-copy each time); dafny suite 30 verified 0 errors (smoke 2 + length_count 5 + mps_log10 5 + expx 4 + parse_count 2 + strmanip 12); gradle gate BUILD SUCCESSFUL on Mark's MacBook at the T3 splice close (4s). Coverage checklist and events verification recorded in the arm journal close section: 210 events rows (98 reuse / 98 hit / 14 miss; 196/210 answered, 93.3%; 50.0% reuse of answered; the 14 miss rows ride three families). RULE-BASE PROMOTIONS at this close per the PI decision of 2026-11-22 (T1 recurrence met, G1/G3/G4 cleared, verified(untested) at capture): h019 class_member_carrier, h020 pointer_handle, h021 struct_carrier - BOTH rules_v0.pl now 21 rules. The m1-build gradle round is pending the PI (journal note to follow) with the deferred swipl coverage_report verification. Check columns track the BASE reference translation (C-002) and stay todo per the C-001 cycle-10 backfill decision. Next protocol step: BOTH B6 per the committed row order.

**BOTH B6 closed 2026-11-23**: 31/31 translated (T1 155-164 push d05f736/splice a7a84e7, T2 165-175 push e20bab8c/splice e2a521e, T3 176-182 push 86e2521/splice 08e3cd1, big-three session 183-185 push 6595c3d/splice ae548cf); whole-arm kotlinc gate SILENT EXIT=0 at every tranche (41 -> 50 -> 57 -> 60 files, gate copy plus fresh repo-copy each time); dafny suite 35 -> 40 verified 0 errors (mps_log2.dfy and extract.dfy added; T3 and the big three EXEMPT per the DAF B6 T3 precedent); gradle gate BUILD SUCCESSFUL on Mark's MacBook at every splice close (8s T1, 1s T2, 9s T3, 13s big three). Coverage checklist and events verification recorded in the arm journal close section: 292 batch-6 events rows (146 reuse / 146 hit / 0 miss; 292/292 answered, 100.0%; 146/146 reuse of answered, 100.0%; zero miss units - the first close with a fully-covered batch). The T1 prose miscount (26/26 for the 21:40 band; the CSV of record carries 28 consults / 56 rows) is corrected by the close note. NO rule-base promotions (zero miss families); ANNOTATION UPGRADES per the PI decision of 2026-11-23: h019/h020/h021 untested -> unit_tested in rules_v0.pl (first consults-as-rule landed across B6, all reuse+hit, every render build-green; the h018 precedent; h008/h017/h018 were already unit_tested). The 31/31 batch-6 PILOT table ids verified against the roster units.csv (the DOC B6 repair-3 lesson applied, zero mismatches). Splice-chain hygiene logged at close: the ab83779 stray (empty root ARM_WORKING_NOTES.md, removed in ae548cf) and the still-tracked T3 big-three fragment whose removal was never git-recorded at splice (its content is spliced in the journal; the git rm rides the close-splice commit). The m1-build gradle round with the swipl coverage_report verification rides this close (journal note to follow). Check columns track the BASE reference translation (C-002) and stay todo per the C-001 cycle-10 backfill decision. Next protocol step: BOTH batch 7 per the committed row order.



## Batch 10 (units 278-308, opened 2026-11-25; DAF runs the batch in ONE session per the C-011 protocol)

DAF batch 10 (units 278-308) translated 2026-11-25 in ONE session per
C-011: thirty-one units rendered and ledgered (the pre-session rows
279/285/288/291/296/299/301/302/304/305/306/308 + the 278/287/292
big rows landed with the mid-batch safety push, then this session's
280/282/283/284/286/290 + the 281/289/293/294/295/307 ledger files),
whole-arm kotlinc gate exit 0 at every stage (gate + repo copies, only
the accepted Interpret.kt warnings), with a MID-BATCH SAFETY PUSH (B9
repair precedent, Mark-approved): commit 08d37a0 with the 278/287/292
renders + the 23-file batch-10 in-flight state landed after the FIFTH
env reset wiped the sandbox, so the rebuild rode on master state
directly. Seats released this batch: xecuteDaf (GlobalQS)/
stopLookupD/synD/shredQueryD (Misc9, all four row 280 cells, the b4
selftext and not-a-corpus blames superseded), bmgFullsearchD (row 284
TAG CORRECTION: vendor owner interp-bmgsubs.c:117, not selftext.cpp),
docCorrelateFreeD + wstemD (rows 287/292 deleted from Misc5), the
zseek rebind to parseCDaf (row 277, PI-ruled per C-012). Raised this
batch: add1Daf/add3Daf/divxDaf (row 282, owners interp-arith.cpp:
632/647/682), bmgSearchD (row 284, owner interp-bmgsubs.c:134),
ifCodeDaf/newCodeDaf/abrtxDaf/copyrightNoticeDaf (row 278; four
unrendered roster rows on the b10 driver, PI adjudication flags),
DafHashFile (row 290, the ctor open owner :959) and DafSwScore (row
280, the GAP/MATCH/MISMATCH mutable ints) carriers, plus the
mglobalNativeD verbatim-dead render of the native Mglobal arm (row
286, SQLITE_D lattice) and the DafBmg/selectDaf/hashOpConstCharD
first bindings. PI adjudication flags this batch: the Sim1W
dual-seat anomaly (Misc9 Sim1W binds the raised sim1D), the
nodeCosineD batch-3 raise, the killDaf raise + B().Kill myname-fold
divergence in DocCorrelate, the row-300 needle-collapse ledger, the
NOERR short-vs-int header quirk (compiler-globals-ext.h:122), the
sym_ stub tag correction (row 279 owns sym_), the 302 u-short "-4"
latitude(2) vendor ODDBALL, the ForBlocks 64->FBMAX_D bounds fix, and
the env-reset recovery ledger (six wipes this session, no data loss
to pushed state; renders recovered via the knowledge b10-kit +
re-application). Check columns track the BASE reference translation
(C-002) and stay todo per the C-001 cycle-10 backfill decision.
Next protocol step: DAF batch 11 per the committed row order (one
session per C-011).

DAF supplementary render session (2026-11-27, B9-repair
precedent: PI-approved deviation, journaled, separate commute):
the four named-stub roster rows ruled RENDER BEFORE PROCEEDING at
the 2026-11-26 PI adjudication of the B10 flags are closed -
compiler-if.c::if_code (CompilerIfCode.kt), compiler-new.c::
new_code (CompilerNewCode.kt), compiler-copyright-notice.c::
copyright_notice (CompilerCopyrightNotice.kt),
compiler-abrtx.c::abrtx (CompilerAbtx.kt). All four B10 raised
named stubs RELEASED (release notes at the DafInterpStubsT3 stub
block); the exportflag shared seat consolidated to public
exportFlagD (main's private exportFlagMD carrier rebound); abrtx
owns the close/conversion cell the B10 driver header deferred to
its seat (the .m read-back rides the C.out emit-builder fold);
the if_code parse_ call binds the compiler TU seat parseCDaf per
the row-277 PI-ruled rebind precedent. Whole-arm kotlinc gate
exit 0 at 207 kt files (6 accepted Interpret.kt vendor-lattice
warnings only); dafny suite 11 specs / 70 verified / 0 errors
unchanged (all four units EXEMPT); H-015 hygiene clean. Ordinal
disclosure: units.csv records 40/65/47/14 (grep lines 41/66/48/
15; the ledger's r41 pin cites the grep line - the off-by-one
noted, ids not renumbered). The stub asymmetry with DOC/PRO is
resolved: 308/308 DAF rows now carry real renders. Heuristic log
rows 324-329. Check columns track the BASE reference translation
(C-002) and stay todo per the C-001 cycle-10 backfill decision.
Next protocol step: BOTH batch 8 per the committed row order
(DAF batch 11 does not exist - the arm is study-complete pending
the C-001 backfill and grading).
| 278 | compiler-mumps.c::main | c | complex | todo | todo | translated | todo | todo | todo |
| 279 | interp-sym.cpp::sym_ | cpp | complex | todo | todo | translated | todo | todo | todo |
| 280 | libmpscpp.cpp::<misc-8> | cpp | complex | todo | todo | translated | todo | todo | todo |
| 281 | include/mumpsc/libmpscpp.h::<misc> | c | complex | todo | todo | translated | todo | todo | todo |
| 282 | libmpscpp.cpp::<misc-4> | cpp | complex | todo | todo | translated | todo | todo | todo |
| 283 | interp-select.cpp::<misc> | cpp | complex | todo | todo | translated | todo | todo | todo |
| 284 | interp-bmgsubs.c::<misc> | c | complex | todo | todo | translated | todo | todo | todo |
| 285 | interp-parse.cpp::inout | cpp | complex | todo | todo | translated | todo | todo | todo |
| 286 | interp-globalb.cpp::<misc> | cpp | complex | todo | todo | translated | todo | todo | todo |
| 287 | libmpscpp.cpp::global::DocCorrelate | cpp | complex | todo | todo | translated | todo | todo | todo |
| 288 | interp-sysfunc.cpp::unpad | cpp | core | todo | todo | translated | todo | todo | todo |
| 289 | include/mumpsc/defines.h::<misc> | c | core | todo | todo | translated | todo | todo | todo |
| 290 | libmpscpp.cpp::line1149 | cpp | core | todo | todo | translated | todo | todo | todo |
| 291 | interp-arith.cpp::Mltoa | cpp | core | todo | todo | translated | todo | todo | todo |
| 292 | interp-stem.cpp::wstem | cpp | core | todo | todo | translated | todo | todo | todo |
| 293 | include/mumpsc/sysparms.h::<misc> | c | core | todo | todo | translated | todo | todo | todo |
| 294 | interp-fnumber.cpp::<misc> | cpp | core | todo | todo | translated | todo | todo | todo |
| 295 | include/mumpsc/errors.h::<misc> | c | core | todo | todo | translated | todo | todo | todo |
| 296 | compiler-sys1.c::xindex | c | core | todo | todo | translated | todo | todo | todo |
| 297 | libmpscpp.cpp::mdh_tree_level_add | cpp | core | todo | todo | translated | todo | todo | todo |
| 298 | libmpscpp.cpp::global::Length | cpp | simple | todo | todo | translated | todo | todo | todo |
| 299 | interp-sym.cpp::SysLoad | cpp | simple | todo | todo | translated | todo | todo | todo |
| 300 | libmpscpp.cpp::global::Length | cpp | simple | todo | todo | translated | todo | todo | todo |
| 301 | interp-arith.cpp::mps_trunc | cpp | simple | todo | todo | translated | todo | todo | todo |
| 302 | interp-arith.cpp::mps_ceil | cpp | simple | todo | todo | translated | todo | todo | todo |
| 303 | libmpscpp.cpp::global::Piece | cpp | simple | todo | todo | translated | todo | todo | todo |
| 304 | libmpscpp.cpp::mstring::Piece | cpp | simple | todo | todo | translated | todo | todo | todo |
| 305 | interp-sym.cpp::_length | cpp | simple | todo | todo | translated | todo | todo | todo |
| 306 | libmpscpp.cpp::global::Pattern | cpp | simple | todo | todo | translated | todo | todo | todo |
| 307 | compiler-at-test.c::<misc> | c | simple | todo | todo | translated | todo | todo | todo |
| 308 | libmpscpp.cpp::global::Justify | cpp | simple | todo | todo | translated | todo | todo | todo |
