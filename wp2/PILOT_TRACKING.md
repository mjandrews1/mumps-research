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

| # | Unit | Lang | Class | Status | DOC | code-check | output-check | runtime-check | target-check |
|---|------|------|-------|--------|-----|-------------|--------------|---------------|--------------|
| 94 | compiler-globalarray.c::<misc> | c | simple | todo | translated | todo | todo | todo | todo |
| 95 | compiler-new.c::<misc> | c | simple | todo | translated | todo | todo | todo | todo |
| 96 | interp-stem.cpp::escan | cpp | simple | todo | translated | todo | todo | todo | todo |
| 97 | libmpscpp.cpp::mstring::Fnumber | cpp | simple | todo | translated | todo | todo | todo | todo |
| 98 | libmpscpp.cpp::Count | cpp | simple | todo | translated | todo | todo | todo | todo |
| 99 | libmpscpp.cpp::Min | cpp | simple | todo | translated | todo | todo | todo | todo |
| 100 | libmpscpp.cpp::Transpose | cpp | simple | todo | translated | todo | todo | todo | todo |
| 101 | server.cpp::<misc> | cpp | simple | todo | translated | todo | todo | todo | todo |
| 102 | rdbms-if.c::<misc> | c | simple | todo | translated | todo | todo | todo | todo |
| 103 | interp-arith.cpp::mps_asin | cpp | simple | todo | translated | todo | todo | todo | todo |
| 104 | libmpscpp.cpp::ScanAlnum | cpp | core | todo | translated | todo | todo | todo | todo |
| 105 | interp-sysfunc.cpp::LocalName | cpp | core | todo | translated | todo | todo | todo | todo |
| 106 | interp-native-btree.cpp::pb | cpp | core | todo | translated | todo | todo | todo | todo |
| 107 | include/mumpsc/sysfunc.h::<misc> | c | core | todo | translated | todo | todo | todo | todo |
| 108 | libmpscpp.cpp::init | cpp | core | todo | translated | todo | todo | todo | todo |
| 109 | compiler-at-test.c::atTest | c | core | todo | translated | todo | todo | todo | todo |
| 110 | interp-bifs.cpp::<misc> | cpp | core | todo | translated | todo | todo | todo | todo |
| 111 | interp-fnumber.cpp::_fnumber | cpp | core | todo | translated | todo | todo | todo | todo |
| 112 | libmpscpp.cpp::global::Centroid | cpp | core | todo | translated | todo | todo | todo | todo |
| 113 | interp-rpm.cpp::parse_pattern_element | cpp | core | todo | translated | todo | todo | todo | todo |
| 114 | interp-piece.cpp::<misc> | cpp | complex | todo | translated | todo | todo | todo | todo |
| 115 | interp-parse.cpp::readf_ | cpp | complex | todo | translated | todo | todo | todo | todo |
| 116 | compiler-lock.c::lock_code | c | complex | todo | translated | todo | todo | todo | todo |
| 117 | compiler-doty.c::doty | c | complex | todo | translated | todo | todo | todo | todo |
| 118 | include/pcre.h::<misc> | c | complex | todo | translated | todo | todo | todo | todo |
| 119 | interp-sym.cpp::GlobalQuery | cpp | complex | todo | translated | todo | todo | todo | todo |
| 120 | libmpscpp.cpp::<misc-9> | cpp | complex | todo | translated | todo | todo | todo | todo |
| 121 | compiler-set.c::set_code | c | complex | todo | translated | todo | todo | todo | todo |
| 122 | compiler-for.c::for_code | c | complex | todo | translated | todo | todo | todo | todo |
| 123 | interp-fcn.cpp::fcn | cpp | complex | todo | translated | todo | todo | todo | todo |

## Batch 5 (units 124-154, opened 2026-11-20; DOC follows the closed B4 in the committed row order, no inversion to log)

DOC B5 tranche 1 (units 124-133, the simple stratum) translated 2026-11-20: ten units pushed, whole-arm kotlinc gate 0/0 at 135 files; gradle gate on m1-build pending at tranche close. Rows follow the pre-registered batch_plan.md order (simple, core, complex; complexity rank ascending within each stratum; the commit-02aff5f first push of this table had listed the rows in units.csv line order - repaired in place in this commit, values unchanged apart from the corrected row order and the DOC column carrying the tranche-1 translated marks). Check columns track the BASE reference translation (C-002) and stay todo per the C-001 cycle-10 backfill decision.

| # | Unit | Lang | Class | Status | DOC | code-check | output-check | runtime-check | target-check |
|---|------|------|-------|--------|-----|-------------|--------------|---------------|--------------|
| 124 | compiler-for.c::<misc> | c | simple | todo | translated | todo | todo | todo | todo |
| 125 | compiler-localarray.c::<misc> | c | simple | todo | translated | todo | todo | todo | todo |
| 126 | libmpscpp.cpp::mdh_tree_level_add | cpp | simple | todo | translated | todo | todo | todo | todo |
| 127 | libmpscpp.cpp::mdh_tree_level_add | cpp | simple | todo | translated | todo | todo | todo | todo |
| 128 | libmpscpp.cpp::mdh_tree_level_add | cpp | simple | todo | translated | todo | todo | todo | todo |
| 129 | libmpscpp.cpp::mstring::Length | cpp | simple | todo | translated | todo | todo | todo | todo |
| 130 | libmpscpp.cpp::Avg | cpp | simple | todo | translated | todo | todo | todo | todo |
| 131 | libmpscpp.cpp::global::Translate | cpp | simple | todo | translated | todo | todo | todo | todo |
| 132 | interp-arith.cpp::mps_log10 | cpp | simple | todo | translated | todo | todo | todo | todo |
| 133 | libmpscpp.cpp::global::Length | cpp | simple | todo | translated | todo | todo | todo | todo |
| 134 | libmpscpp.cpp::GlobalQS | cpp | core | todo | translated | todo | todo | todo | todo |
| 135 | compiler-parse.c::ExpandData | c | core | todo | translated | todo | todo | todo | todo |
| 136 | libmpscpp.cpp::global::Sum | cpp | core | todo | translated | todo | todo | todo | todo |
| 137 | libmpscpp.cpp::Transpose | cpp | core | todo | translated | todo | todo | todo | todo |
| 138 | libmpscpp.cpp::Multiply | cpp | core | todo | translated | todo | todo | todo | todo |
| 139 | interp-arith.cpp::expx | cpp | core | todo | translated | todo | todo | todo | todo |
| 140 | interp-sysfunc.cpp::getstr1 | cpp | core | todo | translated | todo | todo | todo | todo |
| 141 | interp-strmanip.cpp::<misc> | cpp | core | todo | translated | todo | todo | todo | todo |
| 142 | interp-sym.cpp::SysDump | cpp | core | todo | translated | todo | todo | todo | todo |
| 143 | interp-rpm.cpp::parse_count | cpp | core | todo | translated | todo | todo | todo | todo |
| 144 | include/mumpsc/sym.h::<misc> | c | complex | todo | translated | todo | todo | todo | todo |
| 145 | interp-sym.cpp::Qlength | cpp | complex | todo | translated | todo | todo | todo | todo |
| 146 | libmpscpp.cpp::SynInit | cpp | complex | todo | translated | todo | todo | todo | todo |
| 147 | interp-rpm.cpp::scan_token | cpp | complex | todo | translated | todo | todo | todo | todo |
| 148 | interp-bmgsubs.c::bmg_search | c | complex | todo | translated | todo | todo | todo | todo |
| 149 | include/mumpsc/mstringClass.h::<misc> | c | complex | todo | translated | todo | todo | todo | todo |
| 150 | mumps.cpp::main | cpp | complex | todo | translated | todo | todo | todo | todo |
| 151 | include/mumpsc/compiler-globals-ext.h::<misc> | c | complex | todo | translated | todo | todo | todo | todo |
| 152 | interp-rpm.cpp::<misc> | cpp | complex | todo | translated | todo | todo | todo | todo |
| 153 | rdbms-if.c::sql | c | complex | todo | translated | todo | todo | todo | todo |
| 154 | libmpscpp.cpp::<misc-5> | cpp | complex | todo | translated | todo | todo | todo | todo

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

| # | Unit | Lang | Class | Status | DOC | code-check | output-check | runtime-check | target-check |
|---|------|------|-------|--------|-----|-------------|--------------|---------------|--------------|
| 155 | compiler-fcninit.c::<misc> | c | simple | todo | translated | todo | todo | todo | todo |
| 156 | libmpscpp.cpp::global::Find | cpp | simple | todo | translated | todo | todo | todo | todo |
| 157 | interp-rpm.cpp::leaning_toothpickize | cpp | simple | todo | translated | todo | todo | todo | todo |
| 158 | libmpscpp.cpp::SmithWaterman | cpp | simple | todo | translated | todo | todo | todo | todo |
| 159 | libmpscpp.cpp::global::Length | cpp | simple | todo | translated | todo | todo | todo | todo |
| 160 | libmpscpp.cpp::Sum | cpp | simple | todo | translated | todo | todo | todo | todo |
| 161 | libmpscpp.cpp::ScanAlnum | cpp | simple | todo | translated | todo | todo | todo | todo |
| 162 | interp-arith.cpp::mps_log2 | cpp | simple | todo | translated | todo | todo | todo | todo |
| 163 | interp-arith.cpp::mps_sin | cpp | simple | todo | translated | todo | todo | todo | todo |
| 164 | include/mumpsc/memsize.h::<misc> | c | simple | todo | translated | todo | todo | todo | todo |

DOC B6 tranche 2 (units 165-175, the core stratum) translated 2026-11-21: eleven units pushed, whole-arm kotlinc gate 0/0 at 171 files; gradle gate on m1-build pending at tranche close. Rows follow the pre-registered batch_plan.md order (core stratum, complexity rank ascending). The tranche resolves four H-014 stub seats (Stubs.Mglobal CharArray overload, Stubs.Mextract, Stubs.globalMax, Stubs.dice) plus Stubs.stopInit, carries two TAG CORRECTIONS (the global::Max roster mis-read at units 68/171; the _extract batch-10 mis-tag at unit 168), and seats the unit-172 stateVector.h residual field roster into the Msv carrier. Check columns track the BASE reference translation (C-002) and stay todo per the C-001 cycle-10 backfill decision.

| # | Unit | Lang | Class | Status | DOC | code-check | output-check | runtime-check | target-check |
|---|------|------|-------|--------|-----|-------------|--------------|---------------|--------------|
| 165 | interp-arith.cpp::mps_abs | cpp | core | todo | translated | todo | todo | todo | todo |
| 166 | libmpscpp.cpp::StopInit | cpp | core | todo | translated | todo | todo | todo | todo |
| 167 | interp-globalb.cpp::Mglobal | cpp | core | todo | translated | todo | todo | todo | todo |
| 168 | interp-sym.cpp::_extract | cpp | core | todo | translated | todo | todo | todo | todo |
| 169 | include/mumpsc/interp.h::<misc> | c | core | todo | translated | todo | todo | todo | todo |
| 170 | interp-sysfunc.cpp::TimeOut_getstr1 | cpp | core | todo | translated | todo | todo | todo | todo |
| 171 | libmpscpp.cpp::global::Max | cpp | core | todo | translated | todo | todo | todo | todo |
| 172 | include/mumpsc/stateVector.h::<misc> | c | complex | todo | translated | todo | todo | todo | todo |
| 173 | compiler-parse.c::ExpandOrder | c | core | todo | translated | todo | todo | todo | todo |
| 174 | interp-arith.cpp::modulo | cpp | core | todo | translated | todo | todo | todo | todo |
| 175 | libmpscpp.cpp::Dice | cpp | core | todo | translated | todo | todo | todo | todo |

DOC B6 tranche 3 (units 176-182, the complex stratum minus the big three) translated 2026-11-21: seven units pushed, whole-arm kotlinc gate 0/0 at 178 files; gradle gate on m1-build pending at tranche close. Rows follow the pre-registered batch_plan.md order. The tranche resolves the remaining raised H-014 seats (Stubs.shred, the 6-arg Stubs.match) and lands the generated-benchmark main (mpstime), the server transaction main, and the read/write compiler pair. Check columns track the BASE reference translation (C-002) and stay todo per the C-001 cycle-10 backfill decision.

| # | Unit | Lang | Class | Status | DOC | code-check | output-check | runtime-check | target-check |
|---|------|------|-------|--------|-----|-------------|--------------|---------------|--------------|
| 176 | libmpscpp.cpp::Shred | cpp | complex | todo | translated | todo | todo | todo | todo |
| 177 | interp-stem.cpp::<misc> | cpp | complex | todo | translated | todo | todo | todo | todo |
| 178 | compiler-write.c::write_code | c | complex | todo | translated | todo | todo | todo | todo |
| 179 | server.cpp::main | cpp | complex | todo | translated | todo | todo | todo | todo |
| 180 | interp-parse.cpp::match | cpp | complex | todo | translated | todo | todo | todo | todo |
| 181 | compiler-read.c::read_code | c | complex | todo | translated | todo | todo | todo | todo |
| 182 | mpstime.cpp::main | cpp | complex | todo | translated | todo | todo | todo | todo |
| 183 | interp-bifs.cpp::zfcn | cpp | complex | todo | translated | todo | todo | todo | todo |
| 184 | interp-zfcns.cpp::zfcn | cpp | complex | todo | translated | todo | todo | todo | todo |
| 185 | interp-interp.cpp::Interpret | cpp | complex | todo | translated | todo | todo | todo | todo |

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

| 186 | libmpscpp.cpp::global::Find | cpp | simple | todo | translated | todo | todo | todo | todo |
| 187 | compiler-zcommands.c::<misc> | c | simple | todo | translated | todo | todo | todo | todo |
| 188 | libmpscpp.cpp::global::Piece | cpp | simple | todo | translated | todo | todo | todo | todo |
| 189 | compiler-sys1.c::substr | c | simple | todo | translated | todo | todo | todo | todo |
| 190 | interp-strmanip.cpp::substr | cpp | simple | todo | translated | todo | todo | todo | todo |
| 191 | libmpscpp.cpp::mstring::Translate | cpp | simple | todo | translated | todo | todo | todo | todo |
| 192 | libmpscpp.cpp::GlobalData | cpp | simple | todo | translated | todo | todo | todo | todo |
| 193 | libmpscpp.cpp::global::Soundex | cpp | simple | todo | translated | todo | todo | todo | todo |
| 194 | interp-sym.cpp::_justify | cpp | simple | todo | translated | todo | todo | todo | todo |
| 195 | interp-sym.cpp::mkpt | cpp | simple | todo | translated | todo | todo | todo | todo |
| 196 | libmpscpp.cpp::global::Fnumber | cpp | core | todo | translated | todo | todo | todo | todo |
| 197 | include/mumpsc/externs.h::<misc> | c | core | todo | translated | todo | todo | todo | todo |
| 198 | interp-sysfunc.cpp::GlobalData | cpp | core | todo | translated | todo | todo | todo | todo |
| 199 | libmpscpp.cpp::global::MakeGlobal | cpp | core | todo | translated | todo | todo | todo | todo |
| 200 | interp-fcn.cpp::<misc> | cpp | core | todo | translated | todo | todo | todo | todo |
| 201 | libmpscpp.cpp::Multiply | cpp | core | todo | translated | todo | todo | todo | todo |
| 202 | interp-sql.cpp::rslt_callback | cpp | core | todo | translated | todo | todo | todo | todo |
| 203 | compiler-parse.c::Extract | c | core | todo | translated | todo | todo | todo | todo |
| 204 | libmpscpp.cpp::Sim1 | cpp | core | todo | translated | todo | todo | todo | todo |
| 205 | libmpscpp.cpp::embed1 | cpp | core | todo | translated | todo | todo | todo | todo |
| 206 | compiler-mumps.c::<misc> | c | complex | todo | todo | todo | todo | todo | todo |
| 207 | compiler-fcninit.c::fcninit | c | complex | todo | todo | todo | todo | todo | todo |
| 208 | interp-sym.cpp::Qsub | cpp | complex | todo | todo | todo | todo | todo | todo |
| 209 | interp-native-btree.cpp::freadx | cpp | complex | todo | todo | todo | todo | todo | todo |
| 210 | compiler-zcommands.c::zcommands_code | c | complex | todo | todo | todo | todo | todo | todo |
| 211 | interp-sysfunc.cpp::sigint | cpp | complex | todo | todo | todo | todo | todo | todo |
| 212 | compiler-sys1.c::match | c | complex | todo | todo | todo | todo | todo | todo |
| 213 | compiler-epi.c::epi | c | complex | todo | todo | todo | todo | todo | todo |
| 214 | libmpscpp.cpp::<misc-6> | cpp | complex | todo | todo | todo | todo | todo | todo |
| 215 | interp-service.cpp::ServiceInterface | cpp | complex | todo | todo | todo | todo | todo | todo |
