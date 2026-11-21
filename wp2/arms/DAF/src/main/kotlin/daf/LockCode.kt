package daf

// Unit 116 (DAF B4 T3, complex): compiler-lock.c::lock_code (62 LOC):
// the LOCK command emitter: the argumentless form emits the job-lock
// cleanup prologue; the argument form switches on +/- lock flavor,
// delegates the argument parse to set_code (unit 121 of THIS tranche:
// the named-owner seat setCodeDaf resolves here), then emits the
// keyfix call, the optional timeout parse, and the makeLock comparison
// against the current pid.
// Consults: BASE-log rows 2/26 hit-reuse (Sysout/fprintf(Out,...) ->
// DafCompilerState.emit with the emitted C text kept verbatim incl.
// the vendor format quirk: the LOCK store line's format string cites
// THREE conversions but passes ONE argument (lhs[i]) - vendor bug kept
// verbatim and ledgered, no repair), row 10 hit-adapt (NxtLckArg ->
// labeled loop; goto lock_return/lck1 folds), row 15 family (the
// timeout ':' probe), row 28 (atoi not here; the timeout parse rides
// parse_), rows 6/22 (compiler TU globals in DafCompilerState), row 81
// (xd[] cursor reads via the at() end-guard), row 30 family (the
// return-code cascade of set_code results rendered as the vendor
// early-return chain), row 33 family (getpid() inside the EMITTED text
// stays text; nothing is formatted by the render itself).
// Seats: setCodeDaf (RESOLVES to the unit-121 render in this tranche),
// parse_daf (roster b9), extract2Daf (roster b1), Mltoa rides the
// emitted C text; sigintDaf not used here; cvarDaf not used here.
// Dafny: EXEMPT (emitter over stub seats; journal row).

fun lockCodeDaf(): Int {

    val C = DafCompilerState

    var Lf = ' ' // char Lf
    val to = StringBuilder() // char to[32]

    run { // lock group
        if (at(C.xd, C.xpx) == ' ' || at(C.xd, C.xpx) == NUL_D) {
            // no argument form
            C.emit("\tMltoa(getpid(),(char *) atmp);\n")
            C.emit("\tif (svPtr->LockDone) cleanLocks((char *) atmp); " +
                    "/* remove locks for this job */\n")
            C.emit("\tsvPtr->LockDone = 0; /* indicates a lock has been " +
                    "performed */\n")
            return lockLck1() // goto lck1
        }

        while (true) { // NxtLckArg:
            C.emit("\tsvPtr->LockDone = 1; /* indicates a lock has been " +
                    "performed */\n")
            C.LockFlag = 1
            C.retz = CMD_LOCK_D
            if (at(C.xd, C.xpx) == '+' || at(C.xd, C.xpx) == '-') {
                Lf = C.xd[C.xpx]
                C.xpx++
            } else {
                Lf = ' '
            }
            if (at(C.xd, C.xpx) != '^') return BAD_LOCK_D

            // return_code = set_code(SET); // process item to read
            val returnCode = setCodeDaf(SET_CODE_SET_D)

            if (returnCode == 0) return lockLck1()       // goto lock_return
            if (returnCode == PARSE_ERROR_D) return PARSE_ERROR_D
            if (returnCode == ARG_LIST_ERROR_D) return ARG_LIST_ERROR_D
            if (returnCode == NO_GLOBAL_PERMITTED_D) return NO_GLOBAL_PERMITTED_D
            if (returnCode == MEM_ERROR_D) return MEM_ERROR_D
            if (returnCode == TOO_MANY_LHS_D) return TOO_MANY_LHS_D
            if (returnCode == ABRTX_D) return ABRTX_D

            // lock_return:
            C.emit("\tkeyfix(${DafCRuntime.cstring(C.vd)});\n")

            to.setLength(0)
            to.append("NULL")
            if (at(C.xd, C.xpx + 1) == ':') { // timeout
                C.t2 = 1
                C.xpx += 2
                C.ierr = parseDaf(DafInterpState.svPtr)
                if (C.ierr != 0) return PARSE_ERROR_D
                // " // TIMEOUT REQUESTED FOR %s SECONDS\n" - the %s reads
                // the pd1 tail from sdlim (row 81 slice fold)
                C.emit(" // TIMEOUT REQUESTED FOR " +
                        "${DafCRuntime.cstringAt(C.pd1, C.sdlim)} SECONDS\n")
                to.setLength(0)
                to.append(DafCRuntime.cstringAt(C.pd1, C.sdlim))
                C.xpx--
            }

            C.emit("\tMltoa(getpid(),atmp);\n")
            C.emit("\tif (strcmp((const char *) makeLock((char *) " +
                    "${DafCRuntime.cstring(C.vd)},(char *) \\\"1\\\", " +
                    "(char *) atmp,\\'$Lf\\',(char *) " +
                    "${to}),(const char *) \\\"1\\\")==0) " +
                    "svPtr->tpx=1; else svPtr->tpx=0;\n")

            // lck1: folded below (row 10)
            C.xpx++
            dirflgDaf()
            if (at(C.xd, C.xpx) == ',') {
                C.xpx++
                continue // goto NxtLckArg
            }
            break
        }

        if (at(C.xd, C.xpx) == NUL_D || at(C.xd, C.xpx) == ' ' ||
            at(C.xd, C.xpx) == TAB_D) {
            if (C.POST_flg != 0) {
                C.emit("\t} /* post conditional in break */\n")
                C.POST_flg = 0
            }
            return 0 // goto next_cmnd
        }

        return 0 // goto next_cmnd
    } /* lock group */
}

// lck1 target fold (shared by the argumentless and argument paths)
private fun lockLck1(): Int {
    val C = DafCompilerState
    C.xpx++ // lck1:
    dirflgDaf()
    if (at(C.xd, C.xpx) == ',') {
        C.xpx++
        // fall into the NxtLckArg loop of the argument form
        return lockCodeDaf()
    }
    if (at(C.xd, C.xpx) == NUL_D || at(C.xd, C.xpx) == ' ' ||
        at(C.xd, C.xpx) == TAB_D) {
        if (C.POST_flg != 0) {
            C.emit("\t} /* post conditional in break */\n")
            C.POST_flg = 0
        }
        return 0
    }
    return 0
}

// dirflg = 0 (compiler TU global folded into the emitter state object)
private fun dirflgDaf() {
    DafCompilerState.dirflg = 0
}