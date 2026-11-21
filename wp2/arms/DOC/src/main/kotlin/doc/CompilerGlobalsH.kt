package doc

// Unit 241 (B8 tranche 3): include/mumpsc/compiler-globals.h::<misc>
// (batch 8, complex stratum; LOC 148, score 84.0, rank 266;
// units.csv row 221). Pure disposition ledger, no executable code
// (the CompilerMacrosH unit-236 precedent: a header chunk renders
// as per-item ownership notes). The header defines the COMPILER
// (mumpsc) file-scope state, the struct types, and the file-wide
// prototype roster; every item names its landed seat:
//
// Banner (GPL v2 block, May 10 2024 date line) - kept verbatim in
// the residue note; the compiler-open.c/banner family precedent.
//
// VARIABLE DEFINITIONS (the compiler globals): ALL carried by the
// CompilerState object (CompilerCarriers.kt, raised by the landed
// compiler walkers): setpiece/setname/bd/tpx/t2/pd1len/iorg/xpx/
// t0px/ierr/symlen/sdlim/pd1/xd/v1d/ctrlc/gpad/ierr1/nstart/Jflg/
// MAIN_flg/FunctionName/ArgMax/ErrChar; the struct nmes and struct
// txt DEFINITIONS land in the unit-151 compiler-globals-ext.h
// ledger's TxtEntry/Nmes carrier types (the extern twins there
// bind this definition); LabelsStart/TextStart/p1/p3 ride the
// unit-151 carriers. The declared-variables block (CVARMAX, struct
// dcls, cvars, cvarx, cur_line, dosave, forv, lhs/lhsGF/lhsx/
// lhsf, pid, opnfile[7], in, FOR_limit, dirflg, vd, pl, p2,
// output_file, tmp2, tmp3) all ride CompilerState seats (opnfile
// is the CompilerGetstr1 Array<Reader?> table; in is the ReadCode
// walk reader seat; pid is the Fcn-init getpid carrier).
//
// PROTOTYPE ROSTER, per owner:
// - cvar(char*) - Stubs.cvar H-014 seat (no landed body at this
//   push; owner named, batch-pending)
// - Extract2/Extract1 - Stubs.Extract2 / Stubs.Extract1 seats
// - ScanPastPostCond - declaration-only in this header AND the
//   unit-151 ext header: the unit-151 ledger's :84 declaration
//   fossil note holds; no seat exists (no defining body landed)
// - xindex - FindSym.kt (units of arm) + Stubs delegate
// - errmod_ - Errmod.kt (landed)
// - readf_ - Readf.kt / Readf_.kt (landed)
// - inout - Inout.kt + Stubs.inout seat
// - pindex - declaration fossil, no defining body (the unit-151
//   :89 note); H-013 ledger here
// - add - Stubs.add seat (no landed body at this push)
// - numcomp - Numcomp.kt (landed)
// - linepad/linepad2 - Stubs.linepad / Stubs.linepad2 seats
// - fcninit - Fcninit.kt (landed)
// - epi - Epi.kt (landed) / EP - CompilerMumpsMisc.kt (landed)
// - getstr1(FILE*, char[]) - the UNIT-234 render
//   (CompilerGetstr1.kt, this batch; the interp twin Getstr1.kt
//   is the u245 domain twin - overload arity resolves the name)
// - LevCheck - CompilerMumpsMisc.kt (landed)
// - doty - Doty.kt (landed)
// - LocalArray/GlobalArray - LocalArray.kt/GlobalArray.kt
// - atTest - AtTest.kt (landed); atCopy - CompilerAtCopy.kt
//   (unit-222 render, batch-8 tranche 1)
//
// FILE *Out, *Final - the CompilerState.Out emitter writer and
// the Final carrier seat (Fcninit binds); ExitCode, MaxTmp,
// iiitmp, forxmax, forxFLG/forLine/forx, IF_flg, IF_flg_max,
// for_fcn_flg x2, ForBlocks, Blocks, FCN_flg, lines, and the
// walker-local shorts (pd1cur/i/j/k/l, parse_, cmdx, pd1Last,
// POST_flg, GBL_flg, exportflag, ExecLevel/ExecName/ExecStack/
// ExecSym/ExecX, DoRet/DoRetX, CX/EmitLabel, JOB_flg, at_flg/
// Set_flg, LockFlag, SubFlag, SymIncrFlg, for_mult, F_flg,
// SinglesFlg, MergeFlg, MergeTmp, TryFlg, CatchFlg, retz, icmnd,
// p2px, ernbr, flg) - all CompilerState seats of the landed
// compiler walkers (ReadCode/WriteCode/SetCode/ForCode/IfCode
// et al.).
//
// cod208=208 comma / cod207=207 close / cod209=209 comma /
// cod210=210 colon / cod206=206 open - the Carriers command
// const ladder (the InterpMisc/EmitLabel seats).
//
// char *except[] = {"InputLengthException", "InterpreterException",
// ""} - the emitted C++ exception-name table: the errmod_ /
// SetCode emission family's string payload (the except[] table
// copies of the vendor), carried inline by the emitters.
//
// Consult H-004 hit-reuse: single-owner ledger, no new consts or
// bodies opened. Naming per H-017.
