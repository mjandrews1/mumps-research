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
