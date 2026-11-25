# DAF Batch 10 Close (units 278-308, 2026-11-25, ONE session per C-011)

Render arm of the five-arm translation study (Mark Andrews, PI solo).
Batch 10 covers roster units 278-308 drawn from the study subset of the
O'Kane MUMPS-MDH library (mumps-14-may-2026 vendor drop). Consults used
the nominal band 2026-11-25T15:00:00Z through 15:11:30Z plus 30 s per
row above 278. Whole-arm kotlinc gate exit 0 after every stage (gate
plus repo copies; only the accepted Interpret.kt vendor-lattice
warnings). A mid-batch safety push (B9 repair precedent, PI-approved)
landed commit 08d37a0 after the fifth environment wipe so the rebuild
rode on master state directly.

## Units rendered

- 278 compiler-mumps.c::main: CompilerMumpsMain.kt mainCDaf (the
  1372-loc giant; MLblM label machine; FBMAX_D bounds fix).
- 279 interp-sym.cpp::sym_: InterpSym.kt (full dispatcher; Interpret
  nmesChain delegate + the java.io stream webhook).
- 280 libmpscpp.cpp::<misc-8>: LibmpscppMisc8.kt (globalShr* >> family,
  xecute/StopLookup/SYN/ShredQuery releases, writeMumps1, Score carrier).
- 281 include/mumpsc/libmpscpp.h::<misc>: LibmpscppHMisc.kt (KDoc-only
  proto/macro ledger).
- 282 libmpscpp.cpp::<misc-4>: LibmpscppMisc4.kt (mstring operator
  lattice; add1Daf/add3Daf/divxDaf raised).
- 283 interp-select.cpp::<misc>: InterpSelectMisc.kt selectDaf (the
  generated-code \u0024select runtime twin; first binding).
- 284 interp-bmgsubs.c::<misc>: InterpBmgsubs.kt (DafBmg carrier,
  bmg_setup, bmgFullsearchD release with TAG CORRECTION, bmgSearchD
  raised).
- 285 interp-parse.cpp::inout: InterpParseInout.kt (pre-session).
- 286 interp-globalb.cpp::<misc>: InterpGlobalbMisc.kt mglobalNativeD
  (the native #else arm, VERBATIM-DEAD under SQLITE_D).
- 287 libmpscpp.cpp::global::DocCorrelate: LibmpscppDocCorrelate.kt.
- 288 interp-sysfunc.cpp::unpad: InterpSysfuncUnpad.kt (pre-session).
- 289 include/mumpsc/defines.h::<misc>: DefinesMisc.kt (macro ledger +
  new pins; CRTN_D/FLUSH_D dup pins caught and ledgered to their
  BASE seats).
- 290 libmpscpp.cpp::line1149: LibmpscppLine1149.kt hashOpConstCharD
  (the Hash probe; DafHashFile carrier raised for the ctor open).
- 291 interp-arith.cpp::Mltoa: InterpArithMltoa.kt (pre-session).
- 292 interp-stem.cpp::wstem: InterpStemWstem.kt (wstemD release, the
  endsToEscan adapter).
- 293 include/mumpsc/sysparms.h::<misc>: SysparmsMisc.kt (KDoc-only
  config lattice ledger).
- 294 interp-fnumber.cpp::<misc>: InterpFnumberMisc.kt (KDoc-only
  residue ledger; the TU body is the unit-111 seat).
- 295 include/mumpsc/errors.h::<misc>: ErrorsMisc.kt (KDoc-only
  include-fragment ledger; ladder already emits at both site owners).
- 296 compiler-sys1.c::xindex: CompilerSys1Xindex.kt (pre-session).
- 297 libmpscpp.cpp::mdh_tree_level_add: LEDGER-ONLY (MdhTreeLevelAdd
  seat exists; GUI residue).
- 298/300 libmpscpp.cpp::global::Length: LEDGER-ONLY (GlobalLengthM
  seats; row-300 needle-collapse note).
- 299 interp-sym.cpp::SysLoad: InterpSymSysload.kt (pre-session).
- 301/302 interp-arith.cpp::mps_trunc/mps_ceil: MpsTruncCeil.kt
  (pre-session).
- 303 libmpscpp.cpp::global::Piece: LEDGER-ONLY (GlobalPiece seat).
- 304 libmpscpp.cpp::mstring::Piece: MstringPiece.kt (pre-session).
- 305 interp-sym.cpp::_length: InterpSymLength.kt (pre-session).
- 306 libmpscpp.cpp::global::Pattern: LibmpscppGlobalPattern.kt
  (pre-session).
- 307 compiler-at-test.c::<misc>: CompilerAtTestMisc.kt (KDoc-only
  residue ledger; banner not emitted).
- 308 libmpscpp.cpp::global::Justify: LibmpscppGlobalJustify.kt
  (pre-session).

Seat ledger: releases xecuteDaf (GlobalQS), stopLookupD/synD/
shredQueryD (Misc9), bmgFullsearchD (Misc9), docCorrelateFreeD +
wstemD (Misc5), the zseek rebind to parseCDaf (row 277, PI-ruled).
Raises: add1Daf/add3Daf/divxDaf, bmgSearchD, ifCodeDaf/newCodeDaf/
abrtxDaf/copyrightNoticeDaf, DafHashFile, DafSwScore. New seats:
selectDaf, mglobalNativeD (dead arm), hashOpConstCharD, DafBmg.

Gates: kotlinc whole-arm compile exit 0 (gate + repo copies; accepted
Interpret.kt warnings only); dafny suite run at close (expected 70
verified / 0 errors; all batch-10 units EXEMPT or residue, no new
spec); H-015 hygiene swept on all new/edited files (ASCII only, no
backticks, no raw comment dollars, trailing newlines).

PI adjudication flags: Sim1W dual-seat anomaly; nodeCosineD batch-3
raise; killDaf raise + B().Kill myname-fold divergence in DocCorrelate;
row-300 needle-collapse ledger; NOERR short-vs-int header quirk
(compiler-globals-ext.h:122); sym_ stub tag correction (row 279 owns
sym_); zseek rebind PI-ruled per C-012; four unrendered roster rows on
the b10 driver (if_code r41, new_code r65, copyright_notice r47,
abrtx) raised as named stubs; ForBlocks 64->FBMAX_D bounds fix; the 302
u-short "-4" latitude(2) vendor ODDBALL; env-reset recovery ledger
(six wipes this session, no data loss to pushed state; renders
recovered via the knowledge b10-kit + re-application).

Files pushed: commit 08d37a0 (mid-batch safety push, 23 files: 14 new
+ 9 edited, 326,283 bytes) with the 278/287/292 renders and the
in-flight batch-10 state; the batch-close push adds the 280-307
renders plus the heuristic-log rows 291-323, the PILOT_TRACKING.md
Batch 10 section, and this fragment.

Next: DAF batch 11 per the committed row order (one session per
C-011); PI review of the adjudication flags at the batch close.
