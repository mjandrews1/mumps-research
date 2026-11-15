# Pilot (B1) Tracking

Order within batch: complexity_rank ascending. Status: todo / translating / graded-ok / graded-deviation / deferred. Simple stratum COMPLETE (units 1-10, all builds green; unit 10 gcvt semantics verified against the reference binary). Core stratum in progress. Translator units integrate against the translator oracle; interpreter/library units against the interpreter oracle.

| # | unit_id | lang | stratum | BASE | DOC | PRO | DAF | BOTH |
|---|---|---|---|---|---|---|---|---|
| 1 | compiler-copyright-notice.c::<misc> | c | simple | graded-ok | todo | todo | todo | todo |
| 2 | compiler-if.c::<misc> | c | simple | graded-ok | todo | todo | todo | todo |
| 3 | compiler-open.c::<misc> | c | simple | graded-ok | todo | todo | todo | todo |
| 4 | interp-native-btree.cpp::btree | cpp | simple | graded-ok | todo | todo | todo | todo |
| 5 | libmpscpp.cpp::EncodeHTML | cpp | simple | graded-ok | todo | todo | todo | todo |
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
| 22 | interp-bstring.cpp::<misc> | cpp | complex | translating | todo | todo | todo | todo |
| 23 | libmpscpp.cpp::Input | cpp | complex | todo | todo | todo | todo | todo |
| 24 | interp-sysfunc.cpp::DirectRead | cpp | complex | todo | todo | todo | todo | todo |
| 25 | interp-sysfunc.cpp::<misc> | cpp | complex | todo | todo | todo | todo | todo |
| 26 | compiler-sys1.c::patrn1 | c | complex | todo | todo | todo | todo | todo |
| 27 | compiler-sys1.c::MyFunc | c | complex | todo | todo | todo | todo | todo |
| 28 | interp-sym.cpp::_SymFree | cpp | complex | todo | todo | todo | todo | todo |
| 29 | include/mumpsc/numSetup.h::<misc> | c | complex | todo | todo | todo | todo | todo |
| 30 | interp-parse.cpp::patrn1 | cpp | complex | todo | todo | todo | todo | todo |
| 31 | libmpscpp.cpp::<misc-1> | cpp | complex | todo | todo | todo | todo | todo |
