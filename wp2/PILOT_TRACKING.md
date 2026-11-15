# Pilot (B1) Tracking

Order within batch: complexity_rank ascending. Status: todo / translating / graded-ok / graded-deviation / deferred. Simple stratum COMPLETE (units 1-10, all builds green; unit 10 gcvt semantics verified against the reference binary). Core stratum in progress. Translator units integrate against the translator oracle; interpreter/library units against the interpreter oracle.

| # | unit_id | lang | stratum | BASE | DOC | PRO | DAF | BOTH |
|---|---|---|---|---|---|---|---|---|
| 1 | compiler-copyright-notice.c::<misc> | c | simple | graded-ok | todo | translated | todo | todo |
| 2 | compiler-if.c::<misc> | c | simple | graded-ok | todo | translated | todo | todo |
| 3 | compiler-open.c::<misc> | c | simple | graded-ok | todo | translated | todo | todo |
| 4 | interp-native-btree.cpp::btree | cpp | simple | graded-ok | todo | translated | todo | todo |
| 5 | libmpscpp.cpp::EncodeHTML | cpp | simple | graded-ok | todo | translated | todo | todo |
| 6 | interp-sym.cpp::_find | cpp | simple | graded-ok | todo | todo | todo | todo |
| 7 | compiler-parse.c::fixstr | c | simple | graded-ok | todo | todo | todo | todo |
| 8 | libmpscpp.cpp::mdh_tree_level_add | cpp | simple | graded-ok | todo | todo | todo | todo |
| 9 | include/mumpsc/libmpsrdbms.h::<misc> | c | simple | graded-ok | todo | todo | todo | todo |
| 10 | interp-arith.cpp::mps_acos | cpp | simple | graded-ok | todo | todo | todo | todo |
| 11 | compiler-parse.c::ExpandNext | c | core | graded-ok | todo | todo | todo | todo |
| 12 | mumps.cpp::<misc> | cpp | core | graded-ok | todo | todo | todo | todo |
| 13 | interp-sysfunc.cpp::GlobalName | cpp | core | graded-ok | todo | todo | todo | todo |
| 14 | libmpscpp.cpp::global::TermCorrelate | cpp | core | graded-ok | todo | todo | todo | todo |
| 15 | compiler-parse.c::Extract2 | c | core | graded-ok | todo | todo | todo | todo |
| 16 | compiler-parse.c::ExpandGet | c | core | graded-ok | todo | todo | todo | todo |
| 17 | interp-bmgsubs.c::gotamatch | c | core | graded-ok | todo | todo | todo | todo |
| 18 | compiler-abrtx.c::abrtx | c | core | graded-ok | todo | todo | todo | todo |
| 19 | interp-interp.cpp::<misc> | cpp | core | graded-ok | todo | todo | todo | todo |
| 20 | interp-arith.cpp::numcomp | cpp | core | graded-ok | todo | todo | todo | todo |
| 21 | compiler-localarray.c::LocalArray | c | complex | graded-ok | todo | todo | todo | todo |
| 22 | interp-bstring.cpp::<misc> | cpp | complex | graded-ok | todo | todo | todo | todo |
| 23 | libmpscpp.cpp::Input | cpp | complex | graded-ok | todo | todo | todo | todo |
| 24 | interp-sysfunc.cpp::DirectRead | cpp | complex | graded-ok | todo | todo | todo | todo |
| 25 | interp-sysfunc.cpp::<misc> | cpp | complex | graded-ok | todo | todo | todo | todo |
| 26 | compiler-sys1.c::patrn1 | c | complex | graded-ok | todo | todo | todo | todo |
| 27 | compiler-sys1.c::MyFunc | c | complex | graded-ok | todo | todo | todo | todo |
| 28 | interp-sym.cpp::_SymFree | cpp | complex | graded-ok | todo | todo | todo | todo |
| 29 | include/mumpsc/numSetup.h::<misc> | c | complex | graded-ok | todo | todo | todo | todo |
| 30 | interp-parse.cpp::patrn1 | cpp | complex | graded-ok | todo | todo | todo | todo |
| 31 | libmpscpp.cpp::<misc-1> | cpp | complex | graded-ok | todo | todo | todo | todo |

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
