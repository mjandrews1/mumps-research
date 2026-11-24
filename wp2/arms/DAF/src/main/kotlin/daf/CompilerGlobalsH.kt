package daf

// Unit 242 (DAF B8 T3, complex): include/mumpsc/compiler-globals.h::
// <misc> (148 LOC, score 84.0): pure disposition ledger, no
// executable code (the unit-236 CompilerMacrosH precedent: a
// header chunk renders as per-item ownership notes). The header
// defines the COMPILER (mumpsc) file-scope state, the struct
// types, and the file-wide prototype roster; vendor text
// physically verified this session (May 10 2024 date line).
// Consults (2026-11-24T21:23:00Z, nominal): every item names its
// landed seat on THIS arm; no new bodies or consts opened.
//
// VARIABLE DEFINITIONS (the compiler globals): ALL carried by the
// DafCompilerState object (the landed compiler walkers read them:
// t2/ierr/sdlim/pd1/xd/xpx/lines/forx/forxmax/iiitmp/IF_flg/
// POST_flg/LockFlag/MergeFlg/MergeTmp/Set_flg/GBL_flg/lhsx/
// dirflg/at_flg/PD1/ExecLevel/ExecName/ExecStack/ExecSym/ExecX/
// DoRet/DoRetX/forLine/forxFLG/for_fcn_flg/for_fcn_flg2/
// FOR_limit/for_mult/SinglesFlg/ForBlocks/Blocks/setname); the
// 213 raises (maxTmpD/argMaxD, DafInterpStubsT3 consts) and the
// file-scope D seats (functionNameD at CompilerEpi,
// pidD/cvarxD/outputFileD file-scope of the fcninit/epi renders,
// p2pxD, exitCodeD) seat the remaining file-scope names; the
// CODED punct comment block (cod206-210) is the
// CODED_OPEN/COMMA/CLOSE family note seated at
// MstringFnumber/InterpBifs; the exception-name roster and the
// trailing flg/except pair ride the sigint/stateVector ledgers
// of the landed renders.
//
// STRUCT DEFINITIONS: struct nmes and struct txt are the
// NmesNodeD/TxtNodeD carrier types seated at the unit-206
// CompilerMumpsMisc render (TxtNodeD corrected per the struct txt
// vendor chains; LabelsStart/TextStart/p1/p3 ride the
// labelsStartD/textStartD heads of that render); struct dcls is
// the DclsEntryD carrier of the unit-213 epi render; CVARMX ->
// CVARMAX_D, the cvars/cvarx seats there.
//
// PROTOTYPE ROSTER, per owner on this arm:
// - cvar(char*) - the cvarDaf named stub (DafInterpStubsT3; no
//   landed body; owner named, batch-pending)
// - Extract2/Extract1 - the extract2Daf/extract1Daf named stubs
//   (roster b1/b1-kernel owner cells)
// - ScanPastPostCond - declaration-only fossil: no defining body
//   has landed on this arm and no seat exists (the dead-code
//   ledger)
// - xindex - xindexDaf named stub (DafInterpStubsT3:114); the
//   punct-table data live at the unit-212 Match render
// - errmod_ - not a corpus unit; owner named (compiler err
//   service, the unit-151 ext-header ledger note)
// - readf_ - Readf.kt readfD (landed)
// - inout - inoutDaf (WriteCode.kt, landed)
// - pindex - declaration fossil, no defining body landed (dead-
//   code ledger)
// - add - addDaf (MpstimeMain.kt, landed)
// - numcomp - numcompDaf (MpstimeMain.kt, landed)
// - linepad/linepad2 - linepadDaf (ForCode.kt, landed) and
//   linepad2Daf (the unit-207 raise)
// - fcninit - CompilerFcninit.kt fcninitDaf (unit-207 render)
// - epi - CompilerEpi.kt epiD (unit-213 render of THIS arm's
//   batch 7) / EP - the EpiD-adjacent ep seat of the unit-206
//   CompilerMumpsMisc render
// - getstr1(FILE*, char[]) - the unit-234 render of THIS batch
//   (CompilerGetstr1.kt; the interp twin Getstr1.kt u139 is the
//   domain twin - overload arity resolves the name)
// - LevCheck - CompilerMumpsMisc.kt levCheckD (landed)
// - doty - Doty.kt dotyDaf (landed)
// - LocalArray/GlobalArray - the localArrayDaf/globalArrayDaf
//   named stubs
// - atTest - AtTest.kt (landed); atCopy - the unit-222 render of
//   THIS batch (CompilerAtCopy.kt)
// - FILE *Out/*Final/ExitCode/MaxTmp/iiitmp and the walker-local
//   shorts - the C.emit writer seat, the Final carrier note, and
//   the DafCompilerState/is-file locals per the landed walkers
//   (the readCode SetCode writeCode zcommandsCode family).
//
// No runtime seat is opened by this chunk (banner, definitions
// and prototypes only).
// Dafny: EXEMPT (declaration residue; journal row).
