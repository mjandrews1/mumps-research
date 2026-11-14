# Study Subset Batch Plan (seed 20261113)
Generated: 2026-11-13T20:28:41
Subject: Mumps II Interpreter-Compiler Library
Unit granularity: function; units: 308; batches: 10
Excludes: *.in, */usr/*, */DEBIAN/*, */autom4te.cache/*, */Checkout/*

| Batch | Units | Strata (s/c/x) | Total LOC | C / C++ |
|---|---|---|---|---|
| B1 | 31 | 10/21/0 | 1944 | 14 / 17 |
| B2 | 31 | 10/21/0 | 2023 | 8 / 23 |
| B3 | 31 | 10/21/0 | 2327 | 11 / 20 |
| B4 | 30 | 10/20/0 | 2644 | 10 / 20 |
| B5 | 31 | 10/21/0 | 2263 | 8 / 23 |
| B6 | 31 | 10/21/0 | 6431 | 7 / 24 |
| B7 | 30 | 10/20/0 | 2141 | 9 / 21 |
| B8 | 31 | 11/20/0 | 2350 | 14 / 17 |
| B9 | 31 | 10/21/0 | 3584 | 7 / 24 |
| B10 | 31 | 11/20/0 | 4564 | 8 / 23 |

## Batch contents

### B1
- `compiler-copyright-notice.c::<misc>` [c] — simple, LOC 29, score 5.1
- `compiler-if.c::<misc>` [c] — simple, LOC 30, score 5.2
- `compiler-open.c::<misc>` [c] — simple, LOC 30, score 5.2
- `interp-native-btree.cpp::btree` [cpp] — simple, LOC 18, score 5.4
- `libmpscpp.cpp::EncodeHTML` [cpp] — simple, LOC 21, score 7.3
- `interp-sym.cpp::_find` [cpp] — simple, LOC 15, score 7.5
- `compiler-parse.c::fixstr` [c] — simple, LOC 25, score 7.7
- `libmpscpp.cpp::mdh_tree_level_add` [cpp] — simple, LOC 24, score 8.4
- `include/mumpsc/libmpsrdbms.h::<misc>` [c] — simple, LOC 18, score 8.4
- `interp-arith.cpp::mps_acos` [cpp] — simple, LOC 22, score 9.8
- `compiler-parse.c::ExpandNext` [c] — core, LOC 25, score 13.3
- `mumps.cpp::<misc>` [cpp] — core, LOC 58, score 13.8
- `interp-sysfunc.cpp::GlobalName` [cpp] — core, LOC 48, score 16.0
- `libmpscpp.cpp::global::TermCorrelate` [cpp] — core, LOC 29, score 20.1
- `compiler-parse.c::Extract2` [c] — core, LOC 25, score 21.3
- `compiler-parse.c::ExpandGet` [c] — core, LOC 47, score 22.9
- `interp-bmgsubs.c::gotamatch` [c] — core, LOC 19, score 22.9
- `compiler-abrtx.c::abrtx` [c] — core, LOC 60, score 23.2
- `interp-interp.cpp::<misc>` [cpp] — core, LOC 67, score 28.1
- `interp-arith.cpp::numcomp` [cpp] — core, LOC 49, score 28.3
- `compiler-localarray.c::LocalArray` [c] — complex, LOC 55, score 33.3
- `interp-bstring.cpp::<misc>` [cpp] — complex, LOC 41, score 33.9
- `libmpscpp.cpp::Input` [cpp] — complex, LOC 40, score 35.2
- `interp-sysfunc.cpp::DirectRead` [cpp] — complex, LOC 31, score 39.9
- `interp-sysfunc.cpp::<misc>` [cpp] — complex, LOC 111, score 55.7
- `compiler-sys1.c::patrn1` [c] — complex, LOC 37, score 57.7
- `compiler-sys1.c::MyFunc` [c] — complex, LOC 89, score 64.7
- `interp-sym.cpp::_SymFree` [cpp] — complex, LOC 35, score 69.9
- `include/mumpsc/numSetup.h::<misc>` [c] — complex, LOC 212, score 71.2
- `interp-parse.cpp::patrn1` [cpp] — complex, LOC 34, score 74.8
- `libmpscpp.cpp::<misc-1>` [cpp] — complex, LOC 600, score 390.4

### B2
- `libmpscpp.cpp::global::Extract` [cpp] — simple, LOC 16, score 4.0
- `libmpscpp.cpp::global::Find` [cpp] — simple, LOC 16, score 4.2
- `libmpscpp.cpp::SQL_Table` [cpp] — simple, LOC 17, score 4.5
- `compiler-abrtx.c::<misc>` [c] — simple, LOC 30, score 5.2
- `compiler-doty.c::<misc>` [c] — simple, LOC 30, score 5.2
- `libmpscpp.cpp::KiLL` [cpp] — simple, LOC 17, score 6.9
- `libmpscpp.cpp::global::Piece` [cpp] — simple, LOC 23, score 7.5
- `libmpscpp.cpp::GlobalOrder` [cpp] — simple, LOC 17, score 8.7
- `interp-arith.cpp::mps_exp` [cpp] — simple, LOC 26, score 10.2
- `interp-arith.cpp::mps_exp10` [cpp] — simple, LOC 26, score 10.2
- `libmpscpp.cpp::mstring::Fnumber` [cpp] — core, LOC 22, score 10.6
- `libmpscpp.cpp::mdh_tree_level_add` [cpp] — core, LOC 29, score 13.7
- `libmpscpp.cpp::global::Min` [cpp] — core, LOC 26, score 14.8
- `libmpscpp.cpp::prnt` [cpp] — core, LOC 34, score 16.6
- `libmpscpp.cpp::Multiply` [cpp] — core, LOC 28, score 16.8
- `compiler-copyright-notice.c::copyright_notice` [c] — core, LOC 42, score 18.0
- `interp-parse.cpp::getstr2` [cpp] — core, LOC 23, score 18.1
- `libmpscpp.cpp::global::IDF` [cpp] — core, LOC 36, score 18.4
- `interp-native-btree.cpp::scanblocks` [cpp] — core, LOC 27, score 27.1
- `interp-sql.cpp::embed` [cpp] — core, LOC 19, score 27.3
- `compiler-globalarray.c::GlobalArray` [c] — complex, LOC 59, score 31.9
- `libmpscpp.cpp::global::Multiply` [cpp] — complex, LOC 26, score 34.2
- `compiler-if.c::if_code` [c] — complex, LOC 40, score 38.8
- `include/mumpsc/btree.h::<misc>` [c] — complex, LOC 215, score 57.3
- `interp-lock.cpp::<misc>` [cpp] — complex, LOC 97, score 57.5
- `include/mumpsc/arith.h::<misc>` [c] — complex, LOC 47, score 82.9
- `interp-parse.cpp::prnterr` [cpp] — complex, LOC 127, score 83.1
- `compiler-sys1.c::inout` [c] — complex, LOC 44, score 102.0
- `libmpscpp.cpp::global::TreePrint` [cpp] — complex, LOC 116, score 142.6
- `libmpscpp.cpp::sw` [cpp] — complex, LOC 148, score 173.2
- `libmpscpp.cpp::<misc-2>` [cpp] — complex, LOC 600, score 381.8

### B3
- `compiler-epi.c::<misc>` [c] — simple, LOC 30, score 5.2
- `compiler-read.c::<misc>` [c] — simple, LOC 30, score 5.2
- `libmpscpp.cpp::global::Concat` [cpp] — simple, LOC 18, score 5.4
- `libmpscpp.cpp::mdh_tree_level_add` [cpp] — simple, LOC 19, score 7.1
- `interp-arith.cpp::mps_sqrt` [cpp] — simple, LOC 27, score 7.9
- `libmpscpp.cpp::Max` [cpp] — simple, LOC 17, score 8.1
- `libmpscpp.cpp::GlobalOrder` [cpp] — simple, LOC 16, score 8.2
- `libmpscpp.cpp::GlobalData` [cpp] — simple, LOC 18, score 8.6
- `interp-arith.cpp::mps_atan` [cpp] — simple, LOC 22, score 9.8
- `libmpscpp.cpp::global::Fnumber` [cpp] — simple, LOC 23, score 9.9
- `interp-arith.cpp::mps_justify` [cpp] — core, LOC 24, score 11.2
- `libmpscpp.cpp::Cost` [cpp] — core, LOC 15, score 12.3
- `libmpscpp.cpp::Scan` [cpp] — core, LOC 16, score 12.6
- `include/mumpsc/cppsysfunc.h::<misc>` [c] — core, LOC 23, score 12.9
- `libmpscpp.cpp::ScanAlnum` [cpp] — core, LOC 17, score 13.1
- `compiler-sys1.c::readf_` [c] — core, LOC 23, score 13.3
- `libmpscpp.cpp::mstring::Eval` [cpp] — core, LOC 18, score 18.2
- `compiler-parse.c::ExpandQS` [c] — core, LOC 48, score 23.0
- `interp-parse.cpp::<misc>` [cpp] — core, LOC 59, score 25.7
- `libmpscpp.cpp::RunMumps` [cpp] — core, LOC 33, score 25.9
- `interp-zfcns.cpp::GtkTreeAdd` [cpp] — complex, LOC 30, score 30.8
- `compiler-sys1.c::cannon` [c] — complex, LOC 71, score 40.9
- `libmpscpp.cpp::Cosine` [cpp] — complex, LOC 62, score 42.4
- `interp-bmgsubs.c::bmg_fsearch` [c] — complex, LOC 68, score 43.0
- `compiler-sys1.c::errmod_` [c] — complex, LOC 109, score 52.5
- `compiler-sys1.c::patrn` [c] — complex, LOC 127, score 56.5
- `include/mumpsc/globalClass.h::<misc>` [c] — complex, LOC 485, score 76.7
- `interp-arith.cpp::<misc>` [cpp] — complex, LOC 118, score 85.2
- `compiler-new.c::new_code` [c] — complex, LOC 106, score 102.8
- `interp-parse.cpp::patrn` [cpp] — complex, LOC 71, score 106.9
- `interp-parse.cpp::parse_` [cpp] — complex, LOC 584, score 1110.0

### B4
- `compiler-globalarray.c::<misc>` [c] — simple, LOC 30, score 5.2
- `compiler-new.c::<misc>` [c] — simple, LOC 30, score 5.2
- `interp-stem.cpp::escan` [cpp] — simple, LOC 15, score 5.9
- `libmpscpp.cpp::mstring::Fnumber` [cpp] — simple, LOC 17, score 7.9
- `libmpscpp.cpp::Count` [cpp] — simple, LOC 17, score 8.1
- `libmpscpp.cpp::Min` [cpp] — simple, LOC 17, score 8.1
- `libmpscpp.cpp::Transpose` [cpp] — simple, LOC 15, score 8.5
- `server.cpp::<misc>` [cpp] — simple, LOC 60, score 9.2
- `rdbms-if.c::<misc>` [c] — simple, LOC 32, score 9.6
- `interp-arith.cpp::mps_asin` [cpp] — simple, LOC 22, score 9.8
- `libmpscpp.cpp::ScanAlnum` [cpp] — core, LOC 18, score 10.6
- `interp-sysfunc.cpp::LocalName` [cpp] — core, LOC 36, score 12.6
- `interp-native-btree.cpp::pb` [cpp] — core, LOC 19, score 13.7
- `include/mumpsc/sysfunc.h::<misc>` [c] — core, LOC 26, score 14.6
- `libmpscpp.cpp::init` [cpp] — core, LOC 17, score 15.9
- `compiler-at-test.c::atTest` [c] — core, LOC 24, score 16.8
- `interp-bifs.cpp::<misc>` [cpp] — core, LOC 60, score 18.4
- `interp-fnumber.cpp::_fnumber` [cpp] — core, LOC 17, score 21.9
- `libmpscpp.cpp::global::Centroid` [cpp] — core, LOC 30, score 22.6
- `interp-rpm.cpp::parse_pattern_element` [cpp] — core, LOC 35, score 27.1
- `interp-piece.cpp::<misc>` [cpp] — complex, LOC 140, score 52.2
- `interp-parse.cpp::readf_` [cpp] — complex, LOC 55, score 52.9
- `compiler-lock.c::lock_code` [c] — complex, LOC 62, score 56.0
- `compiler-doty.c::doty` [c] — complex, LOC 27, score 57.3
- `include/pcre.h::<misc>` [c] — complex, LOC 127, score 82.5
- `interp-sym.cpp::GlobalQuery` [cpp] — complex, LOC 91, score 89.3
- `libmpscpp.cpp::<misc-9>` [cpp] — complex, LOC 342, score 161.2
- `compiler-set.c::set_code` [c] — complex, LOC 256, score 173.4
- `compiler-for.c::for_code` [c] — complex, LOC 232, score 175.2
- `interp-fcn.cpp::fcn` [cpp] — complex, LOC 775, score 1221.5

### B5
- `compiler-for.c::<misc>` [c] — simple, LOC 30, score 5.2
- `compiler-localarray.c::<misc>` [c] — simple, LOC 30, score 5.2
- `libmpscpp.cpp::mdh_tree_level_add` [cpp] — simple, LOC 15, score 5.9
- `libmpscpp.cpp::mdh_tree_level_add` [cpp] — simple, LOC 17, score 6.5
- `libmpscpp.cpp::mdh_tree_level_add` [cpp] — simple, LOC 21, score 7.7
- `libmpscpp.cpp::mstring::Length` [cpp] — simple, LOC 16, score 7.8
- `libmpscpp.cpp::Avg` [cpp] — simple, LOC 17, score 8.1
- `libmpscpp.cpp::global::Translate` [cpp] — simple, LOC 19, score 9.3
- `interp-arith.cpp::mps_log10` [cpp] — simple, LOC 22, score 9.8
- `libmpscpp.cpp::global::Length` [cpp] — simple, LOC 23, score 9.9
- `libmpscpp.cpp::GlobalQS` [cpp] — core, LOC 19, score 11.3
- `compiler-parse.c::ExpandData` [c] — core, LOC 24, score 11.4
- `libmpscpp.cpp::global::Sum` [cpp] — core, LOC 21, score 11.9
- `libmpscpp.cpp::Transpose` [cpp] — core, LOC 23, score 14.1
- `libmpscpp.cpp::Multiply` [cpp] — core, LOC 26, score 21.4
- `interp-arith.cpp::expx` [cpp] — core, LOC 48, score 22.4
- `interp-sysfunc.cpp::getstr1` [cpp] — core, LOC 30, score 24.4
- `interp-strmanip.cpp::<misc>` [cpp] — core, LOC 36, score 28.0
- `interp-sym.cpp::SysDump` [cpp] — core, LOC 18, score 28.0
- `interp-rpm.cpp::parse_count` [cpp] — core, LOC 40, score 28.4
- `include/mumpsc/sym.h::<misc>` [c] — complex, LOC 39, score 34.3
- `interp-sym.cpp::Qlength` [cpp] — complex, LOC 18, score 35.0
- `libmpscpp.cpp::SynInit` [cpp] — complex, LOC 58, score 36.4
- `interp-rpm.cpp::scan_token` [cpp] — complex, LOC 39, score 45.3
- `interp-bmgsubs.c::bmg_search` [c] — complex, LOC 60, score 47.0
- `include/mumpsc/mstringClass.h::<misc>` [c] — complex, LOC 331, score 51.1
- `mumps.cpp::main` [cpp] — complex, LOC 125, score 60.7
- `include/mumpsc/compiler-globals-ext.h::<misc>` [c] — complex, LOC 144, score 83.4
- `interp-rpm.cpp::<misc>` [cpp] — complex, LOC 145, score 92.5
- `rdbms-if.c::sql` [c] — complex, LOC 209, score 107.3
- `libmpscpp.cpp::<misc-5>` [cpp] — complex, LOC 600, score 333.6

### B6
- `compiler-fcninit.c::<misc>` [c] — simple, LOC 30, score 5.2
- `libmpscpp.cpp::global::Find` [cpp] — simple, LOC 22, score 6.2
- `interp-rpm.cpp::leaning_toothpickize` [cpp] — simple, LOC 17, score 7.7
- `libmpscpp.cpp::SmithWaterman` [cpp] — simple, LOC 17, score 7.7
- `libmpscpp.cpp::global::Length` [cpp] — simple, LOC 17, score 8.1
- `libmpscpp.cpp::Sum` [cpp] — simple, LOC 17, score 8.1
- `libmpscpp.cpp::ScanAlnum` [cpp] — simple, LOC 18, score 8.4
- `interp-arith.cpp::mps_log2` [cpp] — simple, LOC 22, score 9.8
- `interp-arith.cpp::mps_sin` [cpp] — simple, LOC 22, score 9.8
- `include/mumpsc/memsize.h::<misc>` [c] — simple, LOC 50, score 10.0
- `interp-arith.cpp::mps_abs` [cpp] — core, LOC 34, score 11.6
- `libmpscpp.cpp::StopInit` [cpp] — core, LOC 24, score 12.2
- `interp-globalb.cpp::Mglobal` [cpp] — core, LOC 17, score 12.3
- `interp-sym.cpp::_extract` [cpp] — core, LOC 34, score 13.0
- `include/mumpsc/interp.h::<misc>` [c] — core, LOC 69, score 13.3
- `interp-sysfunc.cpp::TimeOut_getstr1` [cpp] — core, LOC 33, score 14.1
- `libmpscpp.cpp::global::Max` [cpp] — core, LOC 26, score 14.8
- `include/mumpsc/stateVector.h::<misc>` [c] — core, LOC 76, score 18.4
- `compiler-parse.c::ExpandOrder` [c] — core, LOC 50, score 24.2
- `interp-arith.cpp::modulo` [cpp] — core, LOC 56, score 25.6
- `libmpscpp.cpp::Dice` [cpp] — core, LOC 44, score 26.8
- `libmpscpp.cpp::Shred` [cpp] — complex, LOC 40, score 37.4
- `interp-stem.cpp::<misc>` [cpp] — complex, LOC 345, score 43.7
- `compiler-write.c::write_code` [c] — complex, LOC 63, score 45.1
- `server.cpp::main` [cpp] — complex, LOC 65, score 51.5
- `interp-parse.cpp::match` [cpp] — complex, LOC 86, score 82.0
- `compiler-read.c::read_code` [c] — complex, LOC 96, score 112.2
- `mpstime.cpp::main` [cpp] — complex, LOC 176, score 170.4
- `interp-bifs.cpp::zfcn` [cpp] — complex, LOC 1622, score 1173.4
- `interp-zfcns.cpp::zfcn` [cpp] — complex, LOC 1662, score 1192.0
- `interp-interp.cpp::Interpret` [cpp] — complex, LOC 1581, score 2921.7

### B7
- `libmpscpp.cpp::global::Find` [cpp] — simple, LOC 16, score 4.0
- `compiler-zcommands.c::<misc>` [c] — simple, LOC 30, score 5.2
- `libmpscpp.cpp::global::Piece` [cpp] — simple, LOC 17, score 5.5
- `compiler-sys1.c::substr` [c] — simple, LOC 17, score 6.7
- `interp-strmanip.cpp::substr` [cpp] — simple, LOC 19, score 7.1
- `libmpscpp.cpp::mstring::Translate` [cpp] — simple, LOC 15, score 7.7
- `libmpscpp.cpp::GlobalData` [cpp] — simple, LOC 17, score 8.7
- `libmpscpp.cpp::global::Soundex` [cpp] — simple, LOC 17, score 8.7
- `interp-sym.cpp::_justify` [cpp] — simple, LOC 26, score 8.8
- `interp-sym.cpp::mkpt` [cpp] — simple, LOC 16, score 9.0
- `libmpscpp.cpp::global::Fnumber` [cpp] — core, LOC 28, score 12.6
- `include/mumpsc/externs.h::<misc>` [c] — core, LOC 26, score 12.8
- `interp-sysfunc.cpp::GlobalData` [cpp] — core, LOC 43, score 14.3
- `libmpscpp.cpp::global::MakeGlobal` [cpp] — core, LOC 38, score 14.6
- `interp-fcn.cpp::<misc>` [cpp] — core, LOC 60, score 14.8
- `libmpscpp.cpp::Multiply` [cpp] — core, LOC 26, score 20.8
- `interp-sql.cpp::rslt_callback` [cpp] — core, LOC 31, score 24.1
- `compiler-parse.c::Extract` [c] — core, LOC 30, score 24.2
- `libmpscpp.cpp::Sim1` [cpp] — core, LOC 40, score 24.2
- `libmpscpp.cpp::embed1` [cpp] — core, LOC 19, score 27.5
- `compiler-mumps.c::<misc>` [c] — complex, LOC 57, score 29.3
- `compiler-fcninit.c::fcninit` [c] — complex, LOC 41, score 43.9
- `interp-sym.cpp::Qsub` [cpp] — complex, LOC 52, score 53.8
- `interp-native-btree.cpp::freadx` [cpp] — complex, LOC 27, score 56.1
- `compiler-zcommands.c::zcommands_code` [c] — complex, LOC 109, score 64.5
- `interp-sysfunc.cpp::sigint` [cpp] — complex, LOC 215, score 70.1
- `compiler-sys1.c::match` [c] — complex, LOC 95, score 78.9
- `compiler-epi.c::epi` [c] — complex, LOC 135, score 111.3
- `libmpscpp.cpp::<misc-6>` [cpp] — complex, LOC 600, score 319.2
- `interp-service.cpp::ServiceInterface` [cpp] — complex, LOC 279, score 403.7

### B8
- `include/mumpsc/builtinTable.h::<misc>` [c] — simple, LOC 20, score 2.0
- `libmpscpp.cpp::mstring::Find` [cpp] — simple, LOC 16, score 4.2
- `compiler-kill.c::<misc>` [c] — simple, LOC 30, score 5.2
- `compiler-lock.c::<misc>` [c] — simple, LOC 30, score 5.2
- `compiler-set.c::<misc>` [c] — simple, LOC 30, score 5.2
- `include/mumpsc/zfcns.h::<misc>` [c] — simple, LOC 18, score 6.2
- `compiler-mumps.c::atCopy` [c] — simple, LOC 15, score 6.5
- `libmpscpp.cpp::IDF` [cpp] — simple, LOC 15, score 6.7
- `libmpscpp.cpp::global::Piece` [cpp] — simple, LOC 17, score 7.3
- `libmpscpp.cpp::GlobalData` [cpp] — simple, LOC 17, score 8.7
- `libmpscpp.cpp::mdh_tree_level_add` [cpp] — simple, LOC 31, score 10.3
- `libmpscpp.cpp::global::ReadLine` [cpp] — core, LOC 21, score 10.7
- `compiler-parse.c::ExpandText` [c] — core, LOC 23, score 12.1
- `interp-sym.cpp::ScanParse` [cpp] — core, LOC 25, score 14.7
- `interp-parse.cpp::BuildGlobal` [cpp] — core, LOC 27, score 16.1
- `interp-sym.cpp::BuildLocal` [cpp] — core, LOC 29, score 16.7
- `compiler-parse.c::Extract2a` [c] — core, LOC 21, score 18.3
- `interp-zfcns.cpp::<misc>` [cpp] — core, LOC 60, score 18.4
- `compiler-sys1.c::getstr1` [c] — core, LOC 37, score 19.3
- `interp-sql.cpp::<misc>` [cpp] — core, LOC 50, score 23.6
- `include/mumpsc/compiler-macros.h::<misc>` [c] — core, LOC 104, score 24.8
- `interp-sysfunc.cpp::Merge` [cpp] — complex, LOC 59, score 30.1
- `interp-bifs.cpp::GtkTreeAdd` [cpp] — complex, LOC 30, score 30.8
- `include/mumpsc/builtin.h::<misc>` [c] — complex, LOC 33, score 33.5
- `interp-parse.cpp::cannon` [cpp] — complex, LOC 35, score 37.3
- `compiler-open.c::mumps_open` [c] — complex, LOC 53, score 72.7
- `include/mumpsc/compiler-globals.h::<misc>` [c] — complex, LOC 148, score 84.0
- `compiler-parse.c::<misc>` [c] — complex, LOC 210, score 108.8
- `interp-sysfunc.cpp::AllocSV` [cpp] — complex, LOC 103, score 164.9
- `libmpscpp.cpp::<misc-7>` [cpp] — complex, LOC 600, score 288.4
- `interp-sql.cpp::btree` [cpp] — complex, LOC 443, score 316.3

### B9
- `interp-sym.cpp::_horolog` [cpp] — simple, LOC 16, score 2.8
- `interp-bstring.cpp::bstrcat` [cpp] — simple, LOC 18, score 4.2
- `compiler-write.c::<misc>` [c] — simple, LOC 30, score 5.2
- `interp-sym.cpp::_random` [cpp] — simple, LOC 18, score 6.8
- `libmpscpp.cpp::GlobalOrder` [cpp] — simple, LOC 15, score 8.1
- `interp-arith.cpp::mps_log` [cpp] — simple, LOC 22, score 9.8
- `interp-arith.cpp::mps_cos` [cpp] — simple, LOC 22, score 9.8
- `interp-arith.cpp::mps_tan` [cpp] — simple, LOC 22, score 9.8
- `libmpscpp.cpp::global::Translate` [cpp] — simple, LOC 21, score 9.9
- `interp-arith.cpp::mps_exp2` [cpp] — simple, LOC 26, score 10.2
- `interp-arith.cpp::mps_floor` [cpp] — core, LOC 30, score 10.6
- `libmpscpp.cpp::mdh_tree_level_add` [cpp] — core, LOC 26, score 11.0
- `include/mumpsc/globalOrder.h::GlobalOrder` [c] — core, LOC 16, score 11.2
- `interp-strmanip.cpp::xindex` [cpp] — core, LOC 25, score 11.5
- `interp-service.cpp::<misc>` [cpp] — core, LOC 55, score 13.9
- `libmpscpp.cpp::mstring::FixGblRef` [cpp] — core, LOC 25, score 14.3
- `libmpscpp.cpp::global::Avg` [cpp] — core, LOC 28, score 15.2
- `interp-sym.cpp::Eval` [cpp] — core, LOC 15, score 21.1
- `compiler-parse.c::ExpandName` [c] — core, LOC 47, score 22.9
- `interp-sym.cpp::<misc>` [cpp] — core, LOC 75, score 24.3
- `compiler-sys1.c::<misc>` [c] — core, LOC 107, score 28.9
- `interp-rpm.cpp::perl_pm` [cpp] — complex, LOC 51, score 34.3
- `libmpscpp.cpp::Jaccard` [cpp] — complex, LOC 58, score 35.0
- `include/mumpsc/cgi.h::<misc>` [c] — complex, LOC 78, score 42.8
- `interp-sql.cpp::sql` [cpp] — complex, LOC 121, score 87.1
- `mpstime.cpp::<misc>` [cpp] — complex, LOC 98, score 90.4
- `interp-native-btree.cpp::<misc>` [cpp] — complex, LOC 84, score 98.4
- `compiler-kill.c::kill_code` [c] — complex, LOC 132, score 110.8
- `interp-native-btree.cpp::btreeX` [cpp] — complex, LOC 502, score 393.4
- `libmpscpp.cpp::<misc-3>` [cpp] — complex, LOC 600, score 472.2
- `compiler-parse.c::parse_` [c] — complex, LOC 1201, score 765.3

### B10
- `libmpscpp.cpp::global::Justify` [cpp] — simple, LOC 18, score 4.2
- `compiler-at-test.c::<misc>` [c] — simple, LOC 27, score 4.9
- `libmpscpp.cpp::global::Pattern` [cpp] — simple, LOC 15, score 5.1
- `interp-sym.cpp::_length` [cpp] — simple, LOC 16, score 5.4
- `libmpscpp.cpp::global::Piece` [cpp] — simple, LOC 17, score 5.5
- `libmpscpp.cpp::mstring::Piece` [cpp] — simple, LOC 17, score 5.5
- `interp-arith.cpp::mps_trunc` [cpp] — simple, LOC 26, score 7.6
- `interp-arith.cpp::mps_ceil` [cpp] — simple, LOC 26, score 7.6
- `libmpscpp.cpp::global::Length` [cpp] — simple, LOC 17, score 7.7
- `interp-sym.cpp::SysLoad` [cpp] — simple, LOC 18, score 8.0
- `libmpscpp.cpp::global::Length` [cpp] — simple, LOC 17, score 8.1
- `compiler-sys1.c::xindex` [c] — core, LOC 21, score 10.9
- `libmpscpp.cpp::mdh_tree_level_add` [cpp] — core, LOC 33, score 10.9
- `include/mumpsc/errors.h::<misc>` [c] — core, LOC 18, score 14.4
- `interp-fnumber.cpp::<misc>` [cpp] — core, LOC 56, score 15.4
- `include/mumpsc/sysparms.h::<misc>` [c] — core, LOC 39, score 18.7
- `interp-stem.cpp::wstem` [cpp] — core, LOC 28, score 19.2
- `interp-arith.cpp::Mltoa` [cpp] — core, LOC 31, score 19.5
- `libmpscpp.cpp::line1149` [cpp] — core, LOC 36, score 19.6
- `include/mumpsc/defines.h::<misc>` [c] — core, LOC 179, score 21.5
- `interp-sysfunc.cpp::unpad` [cpp] — core, LOC 16, score 22.0
- `libmpscpp.cpp::global::DocCorrelate` [cpp] — complex, LOC 41, score 42.3
- `interp-globalb.cpp::<misc>` [cpp] — complex, LOC 112, score 53.4
- `interp-parse.cpp::inout` [cpp] — complex, LOC 38, score 58.6
- `interp-bmgsubs.c::<misc>` [c] — complex, LOC 121, score 76.3
- `interp-select.cpp::<misc>` [cpp] — complex, LOC 156, score 115.0
- `libmpscpp.cpp::<misc-4>` [cpp] — complex, LOC 600, score 152.4
- `include/mumpsc/libmpscpp.h::<misc>` [c] — complex, LOC 369, score 201.7
- `libmpscpp.cpp::<misc-8>` [cpp] — complex, LOC 600, score 283.0
- `interp-sym.cpp::sym_` [cpp] — complex, LOC 484, score 632.4
- `compiler-mumps.c::main` [c] — complex, LOC 1372, score 1125.2
