package both

// Unit 220 render: include/mumpsc/compiler-globals.h::<misc>
// (roster 220, batch 8, 179 LOC dump). The compiler TU DEFINING
// header: every declaration here is already seated in the
// CompilerGlobalsExt object (the compiler-globals-ext.h extern
// twin render, records 165-175 family); this unit is the
// declaration-to-seat verification fold PLUS the initializer
// completion - the ext seat zeroed four members whose vendor
// initializers live in THIS header, and the except[] string
// literals were deferred here. The four flips and the literal
// initialization are applied to the seat (verified this pass;
// gate re-run green).
//
// Consults (rule base wp2/arms/BOTH/rules_v0.pl):
//   h004 include_only - reused: the declarations ride the
//     existing CompilerGlobalsExt seats (the pin map below);
//     NO re-declaration (h015 collision family avoided).
//   h015 grammar-collision - reused: ASCII only, no backticks,
//     no raw comment dollars, trailing newline.
//   h017 naming - reused: the seat renames (bdG/tpxG/t2G/xdG/
//     v1dG/tmp2G/tmp3G/vdG) carry the same collision notes from
//     the ext render; no new names introduced here.
//   h021 struct_carrier - reused: struct nmes -> Nmes(name,
//     next), struct txt -> Txt(name, lnbr, next), struct dcls
//     -> Dcs(name, type) - the ext render seats; the CVARMAX
//     100 / PS1 / FBMAX consts likewise.
//
// Initializer completion (this unit's only seat edits; pins are
// compiler-globals.h lines):
//   ErrChar = ' ' (the ext seat had '\u0000'; vendor
//     char ErrChar=' ')
//   MaxTmp = 2 (vendor short MaxTmp = 2, the "max temp strings"
//     floor; the ext seat had 0)
//   for_mult = -1 (vendor short for_mult = -1; the ext seat
//     had 0)
//   flg = 1 (vendor int flg=1, the last line of the header
//     body; the ext seat had 0)
//   except[] = { "InputLengthException",
//     "InterpreterException", "" } (vendor char *except[];
//     the ext seat was arrayOfNulls(24) - the literals now
//     initialize the first three slots, 21 nulls follow)
//
// Declaration-to-seat pin map (verified line-for-line against
// the dump; ext pins in CompilerGlobalsExtMisc.kt): setpiece
// :31, setname :32, bd->bdG :33, tpx->tpxG :34, t2->t2G :35,
// pd1len :36, iorg :37, xpx->xpxG :38, t0px->t0pxG :39, ierr
// :40, symlen :41, sdlim :42, pd1 :43, xd->xdG :44, v1d->v1dG
// :45, ctrlc :46, gpad :47, ierr1 :48, nstart :49, Jflg :50,
// MAIN_flg=0 :51, FunctionName :52, ArgMax=0 :53, ErrChar=' '
// :54; LabelsStart/TextStart/p1/p3 =NULL :66-67; CVARMAX :71,
// cvars :76, cvarx :78; Out/Final :106, ExitCode=0 :107,
// MaxTmp=2 :108, iiitmp=0 :109, forxmax=0 :110, forxFLG=0/
// forLine=0/forx=0 :111, IF_flg=0 :112, IF_flg_max=0 :113,
// for_fcn_flg=0/for_fcn_flg2=0 :114, ForBlocks :115, Blocks
// :116, FCN_flg=0 :117, lines :118, pd1cur/i/j/k/l :121-122,
// cmdx/pd1Last/POST_flg=0 :124, GBL_flg=0 :125, exportflag=0
// :126, ExecLevel=0 :127, ExecName=0 :128, ExecStack :129,
// ExecSym={0} :130, ExecX=0 :131, DoRet :132, DoRetX=0 :133,
// CX/EmitLabel=0 :134, JOB_flg=0 :135, at_flg=0/Set_flg=0
// :136, LockFlag=0 :137, SubFlag=0 :138, SymIncrFlg=0 :139,
// for_mult=-1 :140, F_flg=0 :141, SinglesFlg=0 :142,
// MergeFlg=0 :143, MergeTmp=0 :144, TryFlg=0 :145,
// CatchFlg=0 :146, opnfile[7] :148, in->inFile :149, forv :150,
// FOR_limit :151, dosave :152, cur_line :153, lhs[25] :154,
// lhsGF :155, lhsx=0 :156, lhsf=0 :157, pid :158; retz/icmnd/
// p2px/ernbr :169, output_file="" :170, tmp2G/tmp3G/dirflg/vdG
// :170, pl :171, p2 :172, except[] :174, flg=1 :176.
//
// Quirks kept (ledgered):
//   - the header declares `short int parse_ ();` inside the
//     local block (the K&R-style declaration quirk; the
//     parse_ seat is the BothStubs roster-93 stub (C-015 renumber from the old roster-303 pin)).
//   - the cod208/207/209/210/206 comment ledger rides the ext
//     seat verbatim.
//   - the commented `// long pid=getpid();` line is dead (the
//     pid seat is the bare Long).
