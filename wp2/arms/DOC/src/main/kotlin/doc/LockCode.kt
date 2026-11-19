package doc

// Unit 116 (B4 tranche 3): compiler-lock.c::lock_code (batch 4,
// complex stratum; LOC 62, score 56.0, rank 242) - the LOCK
// command compiler: the argumentless form emits the
// unlock-cleanup scaffolding; each lock argument parses the +/-
// prefix, requires the caret, delegates the item to set_code
// (SET = 0, the compiler-macros.h:83 int - distinct from the
// :91 char seat of the SET command), then emits keyfix and the
// optional timeout compare, and chains comma-separated arguments.
// The emitted lines are EMITTED C text kept byte-identical (the
// vendor's split-string fprintf concatenations fold to the same
// bytes). The directive externs ride the CompilerState seats:
// xd/xpx/vd/ierr/t2/POST_flg/retz (unit-91 seats); LockFlag
// (compiler-globals.h:136 short) and dirflg (:170 unsigned char)
// are the NEW CompilerState seats added by this unit (EDIT-ONLY
// addition, vendor anchors). The set_code call binds the REAL
// batch-4 unit-121 render (same tranche, compiler-set.c); its
// Stubs.set_code seat converts to the delegate. The zero-arg
// parse_ is the compiler twin Stubs seat (batch-9 owner, TAG
// correct - throws until then; the vendor calls the same twin).
// makeLock/cleanLocks occur in the EMITTED text only (the real
// unit-56 renders exist in LockMisc.kt; no runtime call here).
// Vendor quirks kept and noted: the goto comment fossils kept;
// the goto lck1 of the argumentless form re-enters at the lck1
// label - rendered as the skipLck1 file-private flag; the
// return-0 fall-through at the end (the POST_flg emission only
// on the spacer-form arm) kept. Naming per H-017.

private const val PARSE_ERROR_LC = 1 // compiler-macros.h:69
private const val ARG_LIST_ERROR_LC = 2 // compiler-macros.h:70
private const val NO_GLOBAL_PERMITTED_LC = 3 // compiler-macros.h:71
private const val MEM_ERROR_LC = 4 // compiler-macros.h:72
private const val TOO_MANY_LHS_LC = 5 // compiler-macros.h:73
private const val ABRTX_LC = 6 // compiler-macros.h:74
private const val BAD_LOCK_LC = 9 // compiler-macros.h:77
private const val SET_LC = 0 // compiler-macros.h:83
private const val LOCK_LC = 'l' // compiler-macros.h:112

// the goto lck1 entry of the argumentless form (file-private flag)
private var gotoLck1 = false

fun lock_code(): Int {
    var return_code: Int

    // lock group
    var Lf: Char = ' '
    var to: String

    if (CompilerState.xd[CompilerState.xpxIdx()] == ' ' ||
        CompilerState.xd[CompilerState.xpxIdx()] == NUL
    ) { /* no argument form */
        CompilerState.emit("\tMltoa(getpid(),(char *) atmp);\n")
        CompilerState.emit(
            "\tif (svPtr->LockDone) cleanLocks((char *) atmp); " +
            "/* remove locks for this job */\n"
        )
        CompilerState.emit(
            "\tsvPtr->LockDone = 0; /* indicates a lock has been " +
            "performed */\n"
        )
        gotoLck1 = true
    }

    while (true) {
        if (gotoLck1) {
            gotoLck1 = false
        } else {
            // NxtLckArg:
            CompilerState.emit(
                "\tsvPtr->LockDone = 1; /* indicates a lock has been " +
                "performed */\n"
            )
            CompilerState.LockFlag = 1
            CompilerState.retz = LOCK_LC.code
            if (CompilerState.xd[CompilerState.xpxIdx()] == '+' ||
                CompilerState.xd[CompilerState.xpxIdx()] == '-'
            ) {
                Lf = CompilerState.xd[CompilerState.xpxIdx()]
                CompilerState.xpxInc()
            } else {
                Lf = ' '
            }
            if (CompilerState.xd[CompilerState.xpxIdx()] != '^') {
                return BAD_LOCK_LC // goto bad_lock;
            }
            //            goto set;

            return_code = Stubs.set_code(SET_LC) // goto set;  // process item to read

            // vendor: if (return_code == 0) goto lock_return;
            // every OTHER known code returns verbatim below.
            if (return_code == PARSE_ERROR_LC) return PARSE_ERROR_LC // goto parse_err;
            if (return_code == ARG_LIST_ERROR_LC) return ARG_LIST_ERROR_LC // goto arg_list_error;
            if (return_code == NO_GLOBAL_PERMITTED_LC) return NO_GLOBAL_PERMITTED_LC
            if (return_code == MEM_ERROR_LC) return MEM_ERROR_LC //goto mem_err;
            if (return_code == TOO_MANY_LHS_LC) return TOO_MANY_LHS_LC // goto too_lhs;
            if (return_code == ABRTX_LC) return ABRTX_LC // goto abrtx;
            // return_code == 0 (or any unlisted code): falls to
            // lock_return below, per the vendor control flow.
        }

        // lock_return:
        CompilerState.emit(
            "\tkeyfix(" + CstringOps.cstring(CompilerState.vd) + ");\n"
        )

        to = "NULL"
        if (CompilerState.xd[CompilerState.xpxIdx() + 1] == ':') { /* timeout */
            CompilerState.t2 = 1
            CompilerState.xpx += 2
            CompilerState.ierr = Stubs.parse_()
            if (CompilerState.ierr != 0) return PARSE_ERROR_LC // goto parse_err;
            CompilerState.emit(
                " // TIMEOUT REQUESTED FOR " +
                CstringOps.cstringAt(CompilerState.pd1, CompilerState.sdlim) +
                " SECONDS\n"
            )
            to = CstringOps.cstringAt(CompilerState.pd1, CompilerState.sdlim)
            CompilerState.xpx--
        }

        CompilerState.emit("\tMltoa(getpid(),atmp);\n")
        CompilerState.emit(
            "\tif (strcmp((const char *) makeLock((char *) " +
            CstringOps.cstring(CompilerState.vd) +
            ",(char *) \"1\", (char *) atmp,'" + Lf + "',(char *) " + to +
            "),(const char *) \"1\")==0) svPtr->tpx=1; else svPtr->tpx=0;\n"
        )

        // lck1:
        CompilerState.xpxInc()
        CompilerState.dirflg = NUL
        if (CompilerState.xd[CompilerState.xpxIdx()] == ',') {
            CompilerState.xpxInc()
            continue // goto NxtLckArg;
        }

        if (CompilerState.xd[CompilerState.xpxIdx()] == NUL ||
            CompilerState.xd[CompilerState.xpxIdx()] == ' ' ||
            CompilerState.xd[CompilerState.xpxIdx()] == '\t'
        ) {
            if (CompilerState.POST_flg != 0) {
                CompilerState.emit("\t} /* post conditional in break */\n")
                CompilerState.POST_flg = 0
            }
            return 0 // goto next_cmnd;
        }

        return 0 // goto next_cmnd;
    }
}
