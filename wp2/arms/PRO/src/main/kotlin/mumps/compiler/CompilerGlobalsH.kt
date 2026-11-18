package mumps.compiler

/**
 * PRO-arm translation of include/mumpsc/compiler-globals.h::<misc>
 * (batch-8 unit 221, units.csv data line 221, rank 266).
 * Translated independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), the
 * header's full text (misc physical loc 148; a pure
 * declaration header - no functions).
 *
 * Identity verified against the study tool (tools/select_study_
 * subset.py): include/mumpsc/compiler-globals.h::<misc>, c,
 * loc 148, comment_ratio 0.081, pointer_marks 31,
 * control_marks 2, score 84.0, rank 266, batch 8 - exact
 * match to wp1/study-subset/units.csv line 221.
 *
 * compiler-globals.h is the FILE-SCOPE GLOBALS OWNER of the
 * compiler side: every declaration in it maps to a CompilerState
 * member.  Almost all members were already carried per owning
 * unit under the one-owner policy (h004); this unit closes the
 * three residual initializer divergences and takes ownership of
 * the roster documentation:
 *  - for_mult initializer -1 (was carried as 0) - fixed.
 *  - MaxTmp initializer 2 (was carried as 0) - fixed.
 *  - the char *except[] exception-string table ("InputLength
 *    Exception", "InterpreterException", "") - RESOLVED (was
 *    an unbound null carrier; the initializer list now binds).
 *  - int flg initializer 1 (was carried as 0) - fixed.
 *
 * Roster documentation (vendor lines, all carried):
 *  - 28-45: setpiece, setname[128], bd[2048], tpx, t2, pd1len,
 *    iorg, xpx, t0px, ierr, symlen, sdlim, pd1[PS1], xd[2048],
 *    v1d[2048], ctrlc, gpad, ierr1 - CompilerState (PS1/FBMAX
 *    consts on CompilerState, memsize.h owner).
 *  - 46-49: struct nmes + nstart - the Nmes carrier (Compiler
 *    State.kt); 51-55: struct txt + LabelsStart/TextStart/p1/
 *    p3 - the Txt carrier.
 *  - 61-68: CVARMAX, struct dcs cvars[CVARMAX], cvarx - Dcs
 *    carrier; 70-99: the prototype roster (cvar, Extract2,
 *    Extract1, ScanPastPostCond, xindex, errmod_, readf_,
 *    inout, pindex, add, numcomp, linepad, linepad2, fcninit,
 *    epi, EP, getstr1, LevCheck, doty, LocalArray, GlobalArray,
 *    atTest, atCopy) - rendered seats bind (Epi u189, Fcninit
 *    u195, EP/LevCheck/atCopy u196, Extract2 u..., Readf,
 *    LocalArray, GlobalArray, AtTest, Cannon, MyFunc...); the
 *    unrendered prototypes keep their Stubs.kt seats with
 *    owners named (h014; ScanPastPostCond/pindex/linepad/
 *    linepad2/getstr1 = batch-9/10 residue).
 *  - 101-120: FILE *Out, *Final; ExitCode; MaxTmp; iiitmp;
 *    forxmax; forxFLG/forLine/forx; IF_flg/IF_flg_max;
 *    for_fcn_flg/for_fcn_flg2; ForBlocks/Blocks[FBMAX];
 *    FCN_flg; lines - CompilerState (Out/Final Writer edges,
 *    h001).
 *  - 122-146: pd1cur/i/j/k; l; cmdx/pd1Last/POST_flg; GBL_flg;
 *    exportflag; ExecLevel/ExecName/ExecStack/ExecSym/ExecX;
 *    DoRet/DoRetX; CX/EmitLabel; JOB_flg; at_flg/Set_flg;
 *    LockFlag; SubFlag; SymIncrFlg; for_mult; F_flg;
 *    SinglesFlg; MergeFlg/MergeTmp; TryFlg/CatchFlg.
 *  - 148-166: opnfile[7]; in; forv[256]; FOR_limit; dosave[64];
 *    cur_line[512]; lhs[25]; lhsGF[25]; lhsx/lhsf; pid (the
 *    getpid initializer is commented out in the vendor - kept
 *    out; the Epi/EP seats set pid).
 *  - 172-176: retz/icmnd/p2px/ernbr; output_file/tmp2/tmp3/
 *    dirflg/vd; pl; p2; except[]; flg.
 *  - 178-184: the cod206-210 marker comment block (206 open,
 *    207 close, 208 comma, 209 comma, 210 colon) - the
 *    CodedOpen/CodedClose carriers of units 188/190 - kept as
 *    documentation.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h004 globals_ext: this header IS the globals owner; the
 *    four initializer fixes above are the only uncarried
 *    content (journaled).
 *  - h013 dead_store: nothing executable in the header.
 *  - h014 out_of_scope_call: unrendered prototypes keep their
 *    named stub seats.
 *  - h015 grammar_collision: this file introduces no new
 *    top-level Kotlin declarations - no collisions.
 *  - h017 naming: header residue; the CompilerState member
 *    names keep the vendor names.
 *  - h018 preprocessor_conditional: none in this header (no
 *    #if arms).
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The "temp strrings" typo in the MaxTmp comment is the
 *    vendor's (kept).
 *  - The cod209 comment repeats "comma" after cod208's (kept).
 *  - The except[] table's trailing "" slot is part of the
 *    initializer (kept in the carrier).
 *  - The commented-out pid=getpid() initializer (vendor 167)
 *    stays commented out.
 */
// Header residue: no Kotlin declarations emitted (see KDoc).
