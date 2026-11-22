package daf

// Unit 151 (DAF B5 T3, complex): include/mumpsc/compiler-globals-ext.h
// ::<misc> (units.csv row 128: loc 144, cr 0.077, ptr 31, ctl 2,
// score 83.4, rank 265, complex): the compiler TU extension-header
// residue: the extern declaration block of the compiler state (the
// scrstate/buffer set), the struct nmes/struct txt carrier pair, the
// CVARMAX/cvars declared-variable block, the function prototype set
// over the ext header (Extract2/Extract1/ScanPastPostCond/xindex/
// errmod_/readf_/inout/pindex/add/numcomp/linepad/linepad2/fcninit/
// epi/EP/getstr1/LevCheck/doty/LocalArray/GlobalArray/atTest/atCopy)
// and the May 6 2024 + cod208/207/209/210/206 comment lines.
// Consults: BASE-log rows 6/22 hit-reuse (file-scope compiler globals
// -> the DafCompilerState singleton exactly as raised by the batch-4
// T3 renders; the externs ALREADY seated there are listed by their
// seat names; the externs NOT yet seated are ledgered as pending
// carriers of their owner units below), row 37 hit-adapt (the
// extern prototype block -> named-owner seat ledger, the unit-94/95/
// 107 disposition precedent), row 18 hit-reuse (struct nmes / struct
// txt -> class carriers; no compiler-globals-ext consumer renders
// in this tranche, so the carriers stay declaration-only, raised by
// their first DAF render call sites).
// Disposition ledger (extern -> seat home):
//   setpiece, tpx, t2, pd1len, t0px, ierr, symlen, sdlim, xpx,
//   setname, pd1, xd, v1d, lines, lhs/lhsGF/lhsx/lhsf, forv, forx,
//   forxmax, for_mult, SinglesFlg, ForBlocks/Blocks (FBMAX),
//   IF_flg/for_fcn_flg/for_fcn_flg2, FOR_limit, dirflg, at_flg,
//   Set_flg/GBL_flg/POST_flg, pd1cur, pd1Last, ExecLevel/ExecName/
//   ExecStack/ExecSym/ExecX, DoRet/DoRetX, t2/retz, MergeFlg/MergeTmp,
//   iiitmp, sdlim - ALL seated on DafCompilerState (b4 T3 seat file;
//   field-for-field the vendor externs, noted there).
//   symlen/tpx/pd1len/ierr/start[]/nstart/_Sym - also carried on the
//   DafMsv interpreter vector (DafCRuntime.kt; the interpreter-side
//   folds of the shared std externs).
//   Extract1/Extract2 - stubs extract1Daf/extract2Daf (b1 owner cells)
//   ScanPastPostCond - the unit-2x compiler-globals-ext family; not
//                  consumed by a rendered DAF call site yet; owner
//                  named (same-TU compiler service, not a corpus unit)
//   xindex - stub xindexDaf (b9/b10)
//   errmod_ - not a corpus unit; owner named (compiler err service)
//   readf_ - rendered: Readf.kt (unit 109, b4 T2)
//   inout - the compiler TU io service; not a corpus unit; not
//                  consumed by a rendered DAF call site yet; owner
//                  named (compiler-io family)
//   add - the compiler BOOL sum service; not consumed here; owner
//                  named (not a corpus unit)
//   numcomp - compiler numeric-compare service; B1-core corpus unit
//                  outside the DAF batch cells; owner named (the
//                  interpreter mps numcomp kin is the mps_* family)
//   linepad/linepad2 - stub linepad seats raised per call site (the
//                  u162 journal ledger: linepad owner compiler-read.c)
//   fcninit/epi/EP - the compiler fcn table service; not consumed by
//                  any DAF-rendered call site in scope; owner named
//   getstr1 - rendered: Getstr1.kt (unit 139, this-arm b5 T2)
//   LevCheck - stub levCheckDaf (DafInterpStubsT3.kt)
//   doty - rendered: Doty.kt (unit 111, b4 T2)
//   LocalArray - stub localArrayDaf; GlobalArray - stub
//                  globalArrayDaf (both DafInterpStubsT3.kt, b1/b2)
//   atTest - rendered: AtTest.kt (unit 108, b4 T2)
//   atCopy - stub atCopyDaf (DafInterpStubsT3.kt)
//   struct nmes {name,next} - declaration-only carrier (LabelsStart
//                  text-label head family); raised by its call sites
//   struct txt {name,lnbr,next}; LabelsStart/TextStart/p1/p3 - same
//                  declaration-only carrier discipline (b8+ owner
//                  cells pending)
//   cvars[CVARMAX]/cvarx - stub cvarDaf (DafInterpStubsT3.kt);
//   CVARMAX 100 pinned per vendor
//   ExitCode, MaxTmp, cmdx, Fpbits-shaped consts, p2px, ernbr, pl,
//   p2, except[], output_file: not consumed by any rendered DAF unit;
//   owner-named pending carriers (function main + errmod_ kin)
//   gpad, iorg, ctrlc, ierr1, Jflg, MAIN_flg, FunctionName, ArgMax,
//   ErrChar, ICMND-shaped icmnd, pid - same pending-carrier ledger
//   opnfile[7]/in - the FILE* seat arrays (row 60 stdio API family);
//   pending carriers (no DAF render consumes them in scope).
//   The cod206-210 comment block - the closed-quote/comma/colon
//   open/close coder values - ALREADY seated as CODED_CLOSE_D
//   (208) / CODED_COMMA_D (207) / CODED_OPEN_D (206) in the T2
//   raised consts (MstringFnumber.kt / InterpBifsMisc.kt); the
//   cod209 (209 comma) and cod210 (210 colon) kin ride the
//   SAME code-point carriers noted there (209 is the doubled-quote
//   marker family of row 80; not redefined here).
// Dafny: EXEMPT (declaration residue; journal row).
//
// DAF consult: BASE-log rows 6, 22, outcome hit-reuse
// (2026-11-22T15:11:30Z). DAF consult: BASE-log row 37, outcome
// hit-adapt (2026-11-22T15:12:00Z). DAF consult: BASE-log row 18,
// outcome hit-reuse (2026-11-22T15:12:30Z).
// Dafny: EXEMPT (residue; journal row).
