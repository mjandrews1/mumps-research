package daf

// Unit 185 (batch-6 big three): interp-interp.cpp::Interpret
// (physLOC 1581; core-adjacent stratum; score and rank ride the
// units.csv row). The interpreter mainline: the command loop over
// the pd1 program buffer with the IND command dispatch, the
// set/do/xec/goto/if/for/read/write/set case arms, the MERGE
// command wiring, the gosub frame stack and the NEXT_CMND
// cleaners. Renders as fun InterpretDaf (the vendor C symbol
// Interpret; releases the InterpretDaf seat raised at
// DafInterpStubsT3).
//
// H-011: the goto ladder renders as the ILblI enum state machine
// (label per goto target, one when-arm per vendor label, one
// continue@machine per goto, plain return per C return); the C
// statics render as InterpretState (H-004); NmesI is the private
// alias of the shared DafNmesD carrier (the unit-279 rename).
//
// H-014 DAF seat map: gate delegations - symRetDaf (via the
// per-file symSeatI shape adapter below: the DAF gate seat
// flattens the C (char*, int) name-offset pair onto one CharArray
// plus a 0 offset), keyfixDaf, buildLocalDaf/buildGlobalDaf
// (DafInterpStubsT3), addDaf (the String-shape gate seat of
// MpstimeMain), mltoaDaf (SysDump), sigintDaf, MglobalDaf.
// Raises at this file: inoutInterpDaf (interp-parse.cpp::inout
// 4-arg, roster b10; the DAF WriteCode inoutDaf names the b2
// 3-arg twin), symFreeDaf (interp-sym.cpp::_SymFree, roster b1
// row 28, not drawn by DAF), mergeDaf (interp-sysfunc.cpp::Merge,
// roster b8 row 226 - RELEASED at unit 237, real at Merge.kt).
//
// Consult: units.csv roster 185 (2026-11-22T16:31:00Z), the
// big-three session band.

// interp-interp.cpp:71-78 file-private defines (per-file seats)
private const val GOSUBMAX_I = 50
private const val SWAP_I = 30 // also compiler-macros.h:54
private const val FORMAX_I = 30
private const val EXECLEVS_I = 40
private const val LBLS_I = 80
private const val OPEN_PAREN_I: Char = '\u00CE' // :78 OPEN_PAREN 206 (defines.h:169)
private const val CMDSTACK_MAX_I = 8192 // the local unsigned char cmdstack[8192]

// defines.h Mglobal/sql codes (per-file seats, the same-name family
// rule; Zcn/ZcnBifs carry the _ZB coded twins 206/207/208)
private const val CLOSE_MGI = 4 // defines.h:87 CLOSE (Mglobal/btree code)
private const val OPEN_MGI = 206 // defines.h:169 OPEN (btree code)
private const val INIT_MGI = 6 // defines.h:166 INIT (zg reset)
private const val PULSE_MGI = 100 // defines.h:205 PULSE (dead NATIVE_SHARED arms)
private const val SYM_RETRIEVE_I = 1 // defines.h:44 SymRetrieve

// vendor statics, H-004 notes in the file header
private object InterpretState {
    // the NEW/KILL except-list chain renamed onto the shared
    // DafNmesState carrier at the unit-279 release (InterpSym.kt
    // owner); the assignments bind DafNmesState.chain directly
    // static char *swp[SWAP] (the DO-swap arena; restart frees it late)
    val swp = arrayOfNulls<CharArray>(SWAP_I)

    // srand seed store (vendor srand(time(NULL)) init; rand never
    // called in this TU, grep-verified)
    var srandSeedI = 0
}

// vendor struct nmes carrier of the NEW/KILL except lists (the
// stateVector nstart opaque seat; Fcn.kt copies it opaquely)
// struct nmes node: renamed onto the shared carrier at the
// unit-279 release (the InterpSym.kt DafNmesD class; kept as the
// private alias so the walk sites below stay verbatim)
private typealias NmesI = DafNmesD

// vendor goto labels and command cases, H-011 machine states
private enum class ILblI {
    RESTART, NEXT_LINE, NEXT_LINE_XECUTE, NEXT_CMND, PC1,
    CMD_COMMENT, CMD_FCN, CMD_BREAK, CMD_FILELOCK, CMD_READ,
    CONT_READ, START_IO, STORE_I, CMD_FILEOPEN, OPN1, CMD_ZCMDS,
    ABRT, CMD_IF, NEXT_IF_ARG, CMD_ELSE, CMD_WRITE, IOSUB, IOSUB1, GOTO_RETURN,
    CMD_GOTO, CMD_MERGE, CMD_SET, SET_LBL, S1, S2, SET_RETURN,
    QUIT_LBL, QUIT1, CMD_XECUTE, XECUTE_RETURN, CMD_FOR, NXT_ARG,
    INIT_LOOP_VAR, ARGLESS_LBL, BYP, FOR_END, FOR_QUIT, CMD_HALT,
    CMD_JOB_I, HANG_LBL, DO_RET1, DO_RET, CMD_DO, CMD_NEW, NEW_AGAIN, NEW2,
    CMD_DATAKILL, KILL_AGAIN, KILL2, CMD_FILECLOSE, NEXT_CLOSE_ARG,
    CMD_USE, LHSREF, LHSREF1, LBL_LBL, OFFSET, ERR_CALL, PARSE_ERR,
    POST_COND_ERROR, LBL_ERROR, VAR_NOT_FOUND, CMD_DEFAULT,
    ARG_LIST_ERROR, REF_ERR, SYMTAB_ERR, FILE_ERROR, IO_ERROR,
    PROGRAM_SIZE, STACK_OVERFLOW, SQL_ERROR, USE_NOT_OPEN, ABRT1,
    RESTORE
}

// --- file-private OS quantizations (H-001; the unit-170/DirectRead
// area precedent) ---

// getpid() of the lock/halt arms: JVM process id
private fun getpidI(): Int =
    ProcessHandle.current().pid().toInt()

// _exit(int) of the zhalt/halt arms: the process-local C surface;
// quantized to System.exit (the only process-affine carrier)
private fun exitI(code: Int) {
    System.out.flush()
    System.exit(code)
}

// sleep(int) of the hang arm
private fun sleepI(secs: Int) {
    try {
        Thread.sleep(secs * 1000L)
    } catch (e: InterruptedException) {
        Thread.currentThread().interrupt()
    }
}

// fflush(NULL): the C flush-all-open-streams is a no-op carrier
// here (the JVM streams flush at exit; note kept)
private fun fflushI() {}

// setlocale(LC_ALL, "en_US.UTF-8"): the JVM default locale stays;
// quantized no-op with this note (H-001)
private fun setlocaleI() {}

// srand(time(NULL)): seed store only (rand never called, H-013)
private fun srandI(seed: Int) {
    InterpretState.srandSeedI = seed
}

// system() of the SHELL arm
private fun systemI(cmd: String): Int {
    return try {
        val p = ProcessBuilder("sh", "-c", cmd).start()
        p.waitFor()
        p.exitValue()
    } catch (e: Exception) {
        -1
    }
}

// popen(buf,"r") of SHELL/P (the in_file[6] seat)
private fun popenReadI(cmd: String): java.io.Reader? {
    return try {
        java.io.BufferedReader(java.io.InputStreamReader(
            ProcessBuilder("sh", "-c", cmd).start().inputStream))
    } catch (e: Exception) {
        null
    }
}

// popen(buf,"w") of SHELL/G (the out_file[6] seat)
private fun popenWriteI(cmd: String): java.io.Writer? {
    return try {
        java.io.OutputStreamWriter(
            ProcessBuilder("sh", "-c", cmd).start().outputStream)
    } catch (e: Exception) {
        null
    }
}

// fopen(name,"r") of the FILEOPEN arm: NULL maps to the catch
// (the readf_ unit-115 H-001 precedent)
private fun fopenReadI(name: String): java.io.Reader? {
    return try {
        java.io.FileReader(name)
    } catch (e: Exception) {
        null
    }
}

// fopen(name,"w"/"a")
private fun fopenWriteI(name: String, append: Boolean): java.io.Writer? {
    return try {
        java.io.FileWriter(name, append)
    } catch (e: Exception) {
        null
    }
}

// isprint() of the label-file-name scan
private fun isprintI(c: Char): Boolean =
    c >= '\u0020' && c <= '\u007E' || c >= '\u00A0'

// tolower() of the command decode and zg/zh arms
private fun tolowerI(c: Char): Char =
    if (c in 'A'..'Z') c + 32 else c

// isalpha() of the command-word decode loop
private fun isalphaI(c: Char): Boolean =
    c in 'A'..'Z' || c in 'a'..'z'

// toupper() of the SET Q/H and HALT T arms
private fun toupperI(c: Char): Char =
    if (c in 'a'..'z') c - 32 else c

// slice helper: the C &buf[off] pointer argument rendered as a
// fresh NUL_D-terminated CharArray copy (scratch-copy, H-012 note)
// strcmp(3) over C-string char arrays: walks both to the first
// mismatch or shared NUL_D and returns the char-code difference
// (negative, zero, or positive), matching libc's contract.
private fun strcmpI(a: CharArray, b: CharArray): Int {
    var i = 0
    while (a[i] != NUL_D && a[i] == b[i]) i++
    return a[i] - b[i]
}

private fun sliceAtI(buf: CharArray, off: Int): CharArray {
    var n = 0
    while (buf[off + n] != NUL_D) n++
    val out = CharArray(n + 1)
    System.arraycopy(buf, off, out, 0, n)
    out[n] = NUL_D
    return out
}

fun InterpretDaf(parm1: String, svPtr: DafMsv): Int {
    // C locals (vendor block above; shorts widen to Int per H-012)
    var i = 0
    var j = 0
    var k = 0
    var New = 0
    var l = 0
    var argless = 0
    var pd1cur = 0
    var pd1hold1 = 0
    var pd1hold2 = 0
    var ibr = 0
    var lblx = 0
    var gsbx = 0
    var cmdx = 0
    var forx = 0
    var cmdxold = 0
    var pd1Last = 0
    var NewFlag = 0
    var SymInc = 0
    var swapextent = 0
    var shold = 0
    var ExecLevel = 1
    var BreakFlag = 0
    var retz = 0
    var icmnd = 0
    var p2px = 0
    var ernbr = 0
    var fileflg = 0
    var MergeFlag = 0
    var dotcount = 0
    var dirflg: Char = NUL_D
    val gosub = Array(GOSUBMAX_I) { IntArray(9) }
    val cmdstack = CharArray(CMDSTACK_MAX_I)
    val lbls = Array(LBLS_I) { CharArray(20) }
    val lbloff = IntArray(20) // wident ledger note in the file header
    val ExecLevNew = IntArray(EXECLEVS_I)
    val forv = Array(FORMAX_I) { CharArray(256) }
    val forit = CharArray(FORMAX_I)
    val forlt = CharArray(FORMAX_I)
    val ftmp = CharArray(STR_MAX_DAF)
    val forin = Array(FORMAX_I) { CharArray(STR_MAX_DAF) }
    val forlv = Array(FORMAX_I) { CharArray(256) }
    val dosave = CharArray(STR_MAX_DAF)
    val tmp2 = CharArray(STR_MAX_DAF)
    val tmp3 = CharArray(STR_MAX_DAF)
    val vd = CharArray(STR_MAX_DAF)
    val MergeRHS = CharArray(STR_MAX_DAF)
    val zeroArrI = charArrayOf('0', NUL_D) // numcomp "0" literal seat
    var lab = ILblI.RESTART

    // init environment (vendor lines 4-110)
    // pd1p = &svPtr->pd1[1]: the one-past base of the swap arena;
    // renders as the offset-1 copyAt calls (H-012 pointer note)
    for (xI in 0 until SWAP_I) InterpretState.swp[xI] = null
    svPtr.io = 5
    pd1cur = 0
    while (pd1cur < PS) {
        svPtr.pd1[pd1cur] = ' '
        pd1cur++
    }
    svPtr.symlen = PS
    pd1cur = 1
    svPtr.pd1len = 0
    srandI(System.currentTimeMillis().toInt()) // srand (time (NULL)), H-001 note
    svPtr.bd[0] = NUL_D
    svPtr.bd[1] = NUL_D
    var iI = 0
    while (iI < GOSUBMAX_I) {
        var jI = 0
        while (jI < 9) {
            gosub[iI][jI] = 0
            jI++
        }
        iI++
    }
    setlocaleI()
    // SQLITE live: the btree OPEN auto connect
    val keyI = CharArray(2)
    val bdI = CharArray(2)
    btreeDaf(OPEN_MGI, keyI, bdI, svPtr)

    // set trap banner: environment re-entry points
    pd1cur = 1
    svPtr.pd1[svPtr.pd1len + 1] = NUL_D
    svPtr.ctrlc = 0

    machine@ while (true) {
        when (lab) {
            ILblI.RESTART -> {
                BreakFlag = 0
                ExecLevel = 1
                i = 0
                while (i < EXECLEVS_I) {
                    ExecLevNew[i] = 0
                    i++
                }
                svPtr.ierr = 0
                svPtr.io = 5
                svPtr.t0px = 0
                svPtr.t2 = 0
                // clear the swap area
                i = 0
                while (i < SWAP_I) {
                    if (InterpretState.swp[i] != null) InterpretState.swp[i] = null // free()
                    i++
                }
                swapextent = 1
                lblx = 0
                // initialize execution buffer
                copyAtSI(svPtr.pd1, 2, parm1)
                svPtr.pd1[1] = TAB_D
                svPtr.pd1len = DafCRuntime.cstringAt(svPtr.pd1, 1).length + 1
                BreakFlag = 0
                pd1cur = 1
                cmdx = 0
                svPtr.setpiece = 0
                svPtr.LineNumber = 0
                lblx = 0
                forx = 0
                gsbx = 0
                svPtr.kflg = 0
                pd1Last = 0
                lab = ILblI.NEXT_LINE
                continue@machine
            }

            ILblI.NEXT_LINE -> {
                BreakFlag = 0
                if (pd1cur > svPtr.pd1len) {
                    svPtr.xd[svPtr.xpx] = NUL_D
                    lab = ILblI.QUIT_LBL
                    continue@machine
                }
                svPtr.PD1 = PS1 - pd1cur
                svPtr.LineNumber++
                // comments or special code
                if (svPtr.pd1[pd1cur] == '#' || svPtr.pd1[pd1cur] == '%' ||
                        svPtr.pd1[pd1cur] == '+' || svPtr.pd1[pd1cur] == ';' ||
                        (svPtr.pd1[pd1cur] == '/' && svPtr.pd1[pd1cur + 1] == '/')) {
                    pd1cur++
                    while (svPtr.pd1[pd1cur] != NUL_D) pd1cur++
                    pd1cur++
                    lab = ILblI.NEXT_LINE
                    continue@machine
                }
                if (svPtr.pd1[pd1cur] == NUL_D) {
                    pd1cur++
                    lab = ILblI.NEXT_LINE
                    continue@machine
                }
                i = pd1cur // remember starting place
                // skip label: find 1st blank or TAB_D
                while (svPtr.pd1[pd1cur] != NUL_D && svPtr.pd1[pd1cur] != TAB_D &&
                        svPtr.pd1[pd1cur] != ' ') pd1cur++
                // skip multiple TAB_Ds
                while (svPtr.pd1[pd1cur] == TAB_D || svPtr.pd1[pd1cur] == ' ') pd1cur++
                // (comment banners drop; the copy source line to xd body below)
                argless = 0
                dotcount = 0
                while (svPtr.pd1[pd1cur + dotcount] == '.') dotcount++
                if (dotcount + 1 < ExecLevel) {
                    lab = ILblI.QUIT1
                    continue@machine
                }
                if (dotcount != 0) {
                    if (svPtr.pd1[pd1cur] == '.') {
                        if (strncmpAtI(svPtr.pd1, pd1cur, ". ", 2) == 0) {
                            if (ExecLevel != 2) {
                                while (svPtr.pd1[pd1cur] != NUL_D) pd1cur++
                                pd1cur++
                                lab = ILblI.NEXT_LINE
                                continue@machine
                            }
                            pd1cur += 2
                        } else if (strncmpAtI(svPtr.pd1, pd1cur, ".. ", 3) == 0) {
                            if (ExecLevel != 3) {
                                while (svPtr.pd1[pd1cur] != NUL_D) pd1cur++
                                pd1cur++
                                lab = ILblI.NEXT_LINE
                                continue@machine
                            }
                            pd1cur += 3
                        } else if (strncmpAtI(svPtr.pd1, pd1cur, "... ", 4) == 0) {
                            if (ExecLevel != 4) {
                                while (svPtr.pd1[pd1cur] != NUL_D) pd1cur++
                                pd1cur++
                                lab = ILblI.NEXT_LINE
                                continue@machine
                            }
                            pd1cur += 4
                        } else if (strncmpAtI(svPtr.pd1, pd1cur, ".... ", 5) == 0) {
                            if (ExecLevel != 5) {
                                while (svPtr.pd1[pd1cur] != NUL_D) pd1cur++
                                pd1cur++
                                lab = ILblI.NEXT_LINE
                                continue@machine
                            }
                            pd1cur += 5
                        } else if (strncmpAtI(svPtr.pd1, pd1cur, "..... ", 6) == 0) {
                            if (ExecLevel != 6) {
                                while (svPtr.pd1[pd1cur] != NUL_D) pd1cur++
                                pd1cur++
                                lab = ILblI.NEXT_LINE
                                continue@machine
                            }
                            pd1cur += 6
                        } else if (strncmpAtI(svPtr.pd1, pd1cur, "...... ", 7) == 0) {
                            if (ExecLevel != 7) {
                                while (svPtr.pd1[pd1cur] != NUL_D) pd1cur++
                                pd1cur++
                                lab = ILblI.NEXT_LINE
                                continue@machine
                            }
                            pd1cur += 7
                        } else if (strncmpAtI(svPtr.pd1, pd1cur, "....... ", 8) == 0) {
                            if (ExecLevel != 8) {
                                while (svPtr.pd1[pd1cur] != NUL_D) pd1cur++
                                pd1cur++
                                lab = ILblI.NEXT_LINE
                                continue@machine
                            }
                            pd1cur += 8
                        } else if (strncmpAtI(svPtr.pd1, pd1cur, "........ ", 9) == 0) {
                            if (ExecLevel != 9) {
                                while (svPtr.pd1[pd1cur] != NUL_D) pd1cur++
                                pd1cur++
                                lab = ILblI.NEXT_LINE
                                continue@machine
                            }
                            pd1cur += 9
                        } else if (strncmpAtI(svPtr.pd1, pd1cur, "......... ", 10) == 0) {
                            if (ExecLevel != 10) {
                                while (svPtr.pd1[pd1cur] != NUL_D) pd1cur++
                                pd1cur++
                                lab = ILblI.NEXT_LINE
                                continue@machine
                            }
                            pd1cur += 10
                        }
                    }
                }
                lab = ILblI.NEXT_LINE_XECUTE
                continue@machine
            }

            ILblI.NEXT_LINE_XECUTE -> {
                pd1Last = pd1cur
                svPtr.xpx = 1
                // to/from static pair as index locals (H-004 note)
                var toI = 1
                var fromI = pd1cur
                while (true) {
                    val chI = svPtr.pd1[fromI]
                    svPtr.xd[toI] = chI
                    toI++
                    fromI++
                    if (chI == NUL_D) break
                }
                pd1cur = fromI
                lab = ILblI.NEXT_CMND
                continue@machine
            }

            ILblI.NEXT_CMND -> {
                // stack cleaners
                buildLocalDaf(-1, 0, CharArray(1), CharArray(1), svPtr)
                buildGlobalDaf(-1, 0, CharArray(1), CharArray(1), svPtr)
                var brokeI = false
                while (true) {
                    if (svPtr.xd[svPtr.xpx] == NUL_D || svPtr.xpx >= STR_MAX_DAF) {
                        if (forx > 0 && gosub[gsbx][3] == FOR.code) {
                            if (svPtr.ctrlc != 0) {
                                svPtr.ctrlc = 0
                                ernbr = 29
                                lab = ILblI.ERR_CALL
                                continue@machine
                            }
                            lab = ILblI.QUIT1
                            continue@machine
                        } else {
                            lab = ILblI.NEXT_LINE
                            continue@machine
                        }
                    }
                    if (svPtr.xd[svPtr.xpx] != ' ' && svPtr.xd[svPtr.xpx] != TAB_D) {
                        brokeI = true
                        break
                    }
                    svPtr.xpx++
                }
                if (brokeI) {
                    // decode command word
                    p2px = svPtr.xpx
                    svPtr.t2 = 0
                    svPtr.t0px = 0
                    icmnd = tolowerI(svPtr.xd[svPtr.xpx]).code // command letter
                    svPtr.xpx++
                    while (isalphaI(svPtr.xd[svPtr.xpx])) svPtr.xpx++ // Ignore rest of word
                    if (svPtr.xd[svPtr.xpx] == ':') {
                        // Postconditional
                        svPtr.xpx++
                        svPtr.ierr = parseDaf(svPtr)
                        if (svPtr.ierr != 0) {
                            lab = ILblI.PARSE_ERR
                            continue@machine
                        }
                        if (atoi(DafCRuntime.cstringAt(svPtr.pd1, svPtr.sdlim)) == 0) {
                            var pcDone = false
                            while (!pcDone) {
                                val curI = svPtr.xd[svPtr.xpx]
                                svPtr.xpx++
                                if (curI == NUL_D) pcDone = true
                                if (svPtr.xd[svPtr.xpx] == '"') {
                                    svPtr.xpx++
                                    lab = ILblI.PC1
                                    continue@machine
                                }
                                if (svPtr.xd[svPtr.xpx] == ' ') {
                                    lab = ILblI.NEXT_CMND
                                    continue@machine
                                }
                            }
                            svPtr.xpx--
                            lab = ILblI.NEXT_CMND
                            continue@machine
                        }
                    }
                    // post conditional return
                    if (svPtr.xd[svPtr.xpx] != NUL_D) svPtr.xpx++
                    // branch to correct command handler
                    lab = when (icmnd) {
                        CMMNT.code, '/'.code -> ILblI.CMD_COMMENT
                        FCN.code -> ILblI.CMD_FCN
                        BREAK.code -> ILblI.CMD_BREAK
                        FILELOCK.code -> ILblI.CMD_FILELOCK
                        READ.code -> ILblI.CMD_READ
                        FILEOPEN.code -> ILblI.CMD_FILEOPEN
                        ZCMDS.code -> ILblI.CMD_ZCMDS
                        IF.code -> ILblI.CMD_IF
                        WRITE.code -> ILblI.CMD_WRITE
                        JOB.code -> ILblI.CMD_JOB_I
                        GOTO.code -> ILblI.CMD_GOTO
                        MERGE.code -> ILblI.CMD_MERGE
                        SET.code -> ILblI.CMD_SET
                        QUIT.code -> ILblI.QUIT_LBL
                        XECUTE.code -> ILblI.CMD_XECUTE
                        FOR.code -> ILblI.CMD_FOR
                        HALT.code -> ILblI.CMD_HALT
                        ELSE.code -> ILblI.CMD_ELSE
                        DO.code -> ILblI.CMD_DO
                        NEW.code -> ILblI.CMD_NEW
                        DATAKILL.code -> ILblI.CMD_DATAKILL
                        FILECLOSE.code -> ILblI.CMD_FILECLOSE
                        USE.code -> ILblI.CMD_USE
                        else -> ILblI.CMD_DEFAULT
                    }
                    continue@machine
                }
                // (the non-break path is the next_line transition above)
                lab = ILblI.NEXT_LINE
                continue@machine
            }

            ILblI.PC1 -> {
                while (svPtr.xd[svPtr.xpx] != '"' && svPtr.xd[svPtr.xpx] != NUL_D) svPtr.xpx++
                svPtr.xpx++
                if (svPtr.xd[svPtr.xpx] == '"') {
                    svPtr.xpx++
                    lab = ILblI.PC1
                    continue@machine
                }
                // falls back into the skip loop (the vendor loops by
                // goto; the machine re-enters the loop body inline)
                var pcDone = false
                while (!pcDone) {
                    val curI = svPtr.xd[svPtr.xpx]
                    svPtr.xpx++
                    if (curI == NUL_D) pcDone = true
                    if (svPtr.xd[svPtr.xpx] == '"') {
                        svPtr.xpx++
                        lab = ILblI.PC1
                        continue@machine
                    }
                    if (svPtr.xd[svPtr.xpx] == ' ') {
                        lab = ILblI.NEXT_CMND
                        continue@machine
                    }
                }
                svPtr.xpx--
                lab = ILblI.NEXT_CMND
                continue@machine
            }

            ILblI.CMD_COMMENT -> {
                // comment command - do nothing
                while (svPtr.xd[svPtr.xpx] != NUL_D) {
                    svPtr.xpx++
                    if (svPtr.xd[svPtr.xpx - 1] == NUL_D) break
                }
                svPtr.xpx = STR_MAX_DAF
                lab = ILblI.NEXT_CMND
                continue@machine
            }

            ILblI.CMD_FCN -> {
                // direct function execution
                svPtr.xpx = p2px
                svPtr.t0px = 0
                svPtr.t2 = 1
                svPtr.ierr = parseDaf(svPtr)
                if (svPtr.ierr != 0) {
                    lab = ILblI.PARSE_ERR
                    continue@machine
                }
                svPtr.xpx++
                lab = ILblI.NEXT_CMND
                continue@machine
            }

            ILblI.CMD_BREAK -> {
                // non-standard use - end a FOR loop from a DO group
                BreakFlag = 1
                lab = ILblI.QUIT_LBL
                continue@machine
            }

            ILblI.CMD_FILELOCK -> {
                // not meaningful - all files are locked
                if (svPtr.xd[svPtr.xpx] == ' ' || svPtr.xd[svPtr.xpx] == NUL_D) {
                    lab = ILblI.NEXT_CMND
                    continue@machine
                }
                var Lf: Char = ' ' // type of lock
                if (svPtr.xd[svPtr.xpx] == '-' || svPtr.xd[svPtr.xpx] == '+') {
                    Lf = svPtr.xd[svPtr.xpx]
                    svPtr.xpx++
                }
                svPtr.t0px = 1
                svPtr.ierr = parseDaf(svPtr)
                if (svPtr.ierr != 0) {
                    lab = ILblI.PARSE_ERR
                    continue@machine
                }
                keyfixDaf(sliceAtI(svPtr.v1d, 1))
                copyAtI(tmp2, 0, sliceAtI(svPtr.v1d, 1)) // lock reference
                copyAtSI(dosave, 0, "0") // timeout
                svPtr.tpx = 1
                if (svPtr.xd[svPtr.xpx + 1] == ':') {
                    svPtr.t2 = 1
                    svPtr.xpx += 2
                    svPtr.ierr = parseDaf(svPtr)
                    if (svPtr.ierr != 0) {
                        lab = ILblI.PARSE_ERR
                        continue@machine
                    }
                    svPtr.xpx++
                    j = atoi(DafCRuntime.cstringAt(svPtr.pd1, svPtr.sdlim))
                    copyAtI(dosave, 0, sliceAtI(svPtr.pd1, svPtr.sdlim)) // timeout
                    svPtr.tpx = 0
                    lab = ILblI.NEXT_CMND
                    continue@machine
                }
                mltoaDaf(getpidI().toLong(), tmp3) // process id
                if (makeLockDaf(DafCRuntime.cstring(tmp2), "1", DafCRuntime.cstring(tmp3),
                        Lf, DafCRuntime.cstring(dosave)) == "1") svPtr.tpx = 1 else svPtr.tpx = 0
                svPtr.LockDone = 1 // (the vendor double semicolon quirk)
                svPtr.xpx++
                lab = ILblI.NEXT_CMND
                continue@machine
            }

            ILblI.CMD_READ, ILblI.CONT_READ -> {
                if (lab == ILblI.CONT_READ) {
                    svPtr.xpx++
                    dirflg = NUL_D
                    if (svPtr.xd[svPtr.xpx] == ',') svPtr.xpx++
                    if (svPtr.xd[svPtr.xpx] == ' ' || svPtr.xd[svPtr.xpx] == NUL_D ||
                            svPtr.xd[svPtr.xpx] == TAB_D) {
                        lab = ILblI.NEXT_CMND
                        continue@machine
                    }
                }
                if (svPtr.io != 5 && svPtr.in_file[svPtr.io] == null) {
                    lab = ILblI.IO_ERROR
                    continue@machine
                }
                // NATIVE_SHARED MglobalDaf(PULSE) deadlock arm dead (H-018 note)
                dirflg = NUL_D
                if (svPtr.xd[svPtr.xpx] == '@') {
                    // indirect
                    svPtr.t0px = 1
                    svPtr.ierr = parseDaf(svPtr)
                    if (svPtr.ierr != 0) {
                        lab = ILblI.PARSE_ERR
                        continue@machine
                    }
                }
                if (svPtr.xd[svPtr.xpx] == '!' || svPtr.xd[svPtr.xpx] == '#') {
                    // carriage control
                    if (svPtr.xd[svPtr.xpx] != '#') i = 2 else i = 1
                    lab = ILblI.START_IO
                    continue@machine
                }
                if (svPtr.xd[svPtr.xpx] == '?') { // tab format
                    svPtr.t2 = 1
                    svPtr.xpx--
                    svPtr.ierr = parseDaf(svPtr)
                    if (svPtr.ierr != 0) {
                        lab = ILblI.PARSE_ERR
                        continue@machine
                    }
                    j = atoi(DafCRuntime.cstringAt(svPtr.pd1, svPtr.sdlim))
                    i = 3
                    lab = ILblI.START_IO
                    continue@machine
                }
                if (svPtr.xd[svPtr.xpx] == '"') { // text to be printed first
                    svPtr.t2 = 1
                    svPtr.ierr = parseDaf(svPtr)
                    if (svPtr.ierr != 0) {
                        lab = ILblI.PARSE_ERR
                        continue@machine
                    }
                    i = 4
                    // falls into start_io with the inout call inline
                    inoutInterpDaf(sliceAtI(svPtr.pd1, svPtr.sdlim), i.toShort(), j.toShort(), svPtr)
                    lab = ILblI.CONT_READ
                    continue@machine
                }
                if (svPtr.xd[svPtr.xpx] == '*') { // direct mode
                    svPtr.xpx++
                    dirflg = '1'
                }
                svPtr.t0px = 1 // extract variable name to be read
                svPtr.setname[0] = NUL_D
                svPtr.ierr = parseDaf(svPtr)
                if (svPtr.ierr != 0) {
                    lab = ILblI.PARSE_ERR
                    continue@machine
                }
                copyAtI(vd, 1, sliceAtI(svPtr.v1d, 1))
                // timeout
                if (svPtr.xd[svPtr.xpx + 1] == ':') {
                    svPtr.t2 = 1
                    svPtr.xpx += 2
                    svPtr.ierr = parseDaf(svPtr)
                    if (svPtr.ierr != 0) {
                        lab = ILblI.PARSE_ERR
                        continue@machine
                    }
                    j = atoi(DafCRuntime.cstringAt(svPtr.pd1, svPtr.sdlim))
                    svPtr.xpx--
                    if (j <= 0) {
                        svPtr.xpx--
                        svPtr.tpx = 0
                        lab = ILblI.CONT_READ
                        continue@machine
                    }
                    if (dirflg == NUL_D) {
                        val timeoutI = CharArray(j.toString().length + 1)
                        copyAtSI(timeoutI, 0, j.toString())
                        val tAreaI = CharArray(STR_MAX_DAF) // &pd1[sdlim] scratch (H-012 note)
                        svPtr.tpx = timeOutGetstr1D(svPtr.io, tAreaI, timeoutI, false, System.`in`)
                        copyAtI(svPtr.pd1, svPtr.sdlim, tAreaI) // copy-back
                    } else {
                        val iChI = CharArray(1) // unsigned char i
                        svPtr.tpx = directReadDaf(svPtr.io, iChI)
                        copyAtSI(svPtr.pd1, svPtr.sdlim, (iChI[0].code).toString())
                    }
                    lab = ILblI.STORE_I
                    continue@machine
                }
                svPtr.hor[svPtr.io] = 0
                svPtr.ver[svPtr.io]++
                i = svPtr.io
                svPtr.sdlim = svPtr.pd1len + 2
                if (dirflg == '1') {
                    val iChI = CharArray(1)
                    svPtr.tpx = directReadDaf(svPtr.io, iChI)
                    copyAtSI(svPtr.pd1, svPtr.sdlim, (iChI[0].code).toString())
                } else {
                    // &pd1[sdlim] scratch (H-012 note); +1 slot: the
                    // vendor writes area[STR_MAX] (one past the cap
                    // index) at the restricted-length exit - the H-
                    // quantization of the released Getstr2.kt seat
                    val gAreaI = CharArray(STR_MAX_DAF + 1)
                    svPtr.tpx = getstr2Daf(i.toShort(), gAreaI, svPtr)
                    if (svPtr.tpx >= 0) svPtr.tpx = 1 else svPtr.tpx = 0
                    copyAtI(svPtr.pd1, svPtr.sdlim, gAreaI) // copy-back
                }
                lab = ILblI.STORE_I
                continue@machine
            }

            ILblI.START_IO -> {
                inoutInterpDaf(sliceAtI(svPtr.pd1, svPtr.sdlim), i.toShort(), j.toShort(), svPtr)
                lab = ILblI.CONT_READ
                continue@machine
            }

            ILblI.STORE_I -> {
                retz = READ.code
                lab = ILblI.LHSREF
                continue@machine
            }

            ILblI.CMD_FILEOPEN -> {
                svPtr.t2 = 1
                svPtr.ierr = parseDaf(svPtr)
                if (svPtr.ierr != 0) {
                    lab = ILblI.PARSE_ERR
                    continue@machine
                }
                k = atoi(DafCRuntime.cstringAt(svPtr.pd1, svPtr.sdlim))
                if (k <= 0 || k >= 5) {
                    lab = ILblI.FILE_ERROR
                    continue@machine
                }
                if (svPtr.in_file[k] != null || svPtr.out_file[k] != null) {
                    lab = ILblI.FILE_ERROR
                    continue@machine
                }
                if (svPtr.xd[svPtr.xpx] != ':') {
                    lab = ILblI.ARG_LIST_ERROR
                    continue@machine
                }
                svPtr.xpx++
                svPtr.t2 = 1
                svPtr.ierr = parseDaf(svPtr)
                if (svPtr.ierr != 0) {
                    lab = ILblI.PARSE_ERR
                    continue@machine
                }
                i = 0
                while (svPtr.pd1[svPtr.sdlim + i] != NUL_D && svPtr.pd1[svPtr.sdlim + i] != ',') {
                    tmp2[i] = svPtr.pd1[svPtr.sdlim + i]
                    i++
                }
                if (svPtr.pd1[svPtr.sdlim + i] != ',') {
                    i = 0
                    while (svPtr.pd1[svPtr.sdlim + i] != NUL_D && svPtr.pd1[svPtr.sdlim + i] != '/') {
                        tmp2[i] = svPtr.pd1[svPtr.sdlim + i]
                        i++
                    }
                }
                tmp2[i] = NUL_D
                i++
                val newStrI = DafCRuntime.cstringAt(svPtr.pd1, svPtr.sdlim + i)
                if (newStrI.equals("old", ignoreCase = true)) New = 0
                else if (newStrI.equals("new", ignoreCase = true)) New = 1
                else if (newStrI.equals("append", ignoreCase = true)) New = 2
                else {
                    lab = ILblI.ARG_LIST_ERROR
                    continue@machine
                }
                if (New == 1) { /* output new */
                    // DAF in/out_file are the Int handle domain
                    // (DafCRuntime B6 T2 ext); the JVM Writer
                    // carrier folds onto the 0/1 open flag (the
                    // papering transports no stream through this
                    // arm - downstream reads raise at their seats)
                    fopenWriteI(DafCRuntime.cstring(tmp2), false)
                        ?.let { svPtr.out_file[k] = 1
                                if (k in 0..9) DafFileHandles.outW[k] = it }
                        ?: run { svPtr.out_file[k] = 0
                                 if (k in 0..9) DafFileHandles.outW[k] = null }
                    if (svPtr.out_file[k] == 0) svPtr.tpx = 0 else svPtr.tpx = 1
                    lab = ILblI.OPN1
                    continue@machine
                }
                if (New == 2) { /* output append */
                    fopenWriteI(DafCRuntime.cstring(tmp2), true)
                        ?.let { svPtr.out_file[k] = 1
                                if (k in 0..9) DafFileHandles.outW[k] = it }
                        ?: run { svPtr.out_file[k] = 0
                                 if (k in 0..9) DafFileHandles.outW[k] = null }
                    if (svPtr.out_file[k] == 0) svPtr.tpx = 0 else svPtr.tpx = 1
                    lab = ILblI.OPN1
                    continue@machine
                }
                // same handle-domain fold (note above)
                fopenReadI(DafCRuntime.cstring(tmp2))
                    ?.let { svPtr.in_file[k] = 1
                            if (k in 0..9) DafFileHandles.inR[k] = it }
                    ?: run { svPtr.in_file[k] = 0
                             if (k in 0..9) DafFileHandles.inR[k] = null }
                if (svPtr.in_file[k] == 0) svPtr.tpx = 0 else svPtr.tpx = 1
                lab = ILblI.OPN1
                continue@machine
            }

            ILblI.OPN1 -> {
                svPtr.xpx++
                if (svPtr.xd[svPtr.xpx] != ',') {
                    lab = ILblI.NEXT_CMND
                    continue@machine
                }
                svPtr.xpx++
                lab = ILblI.CMD_FILEOPEN
                continue@machine
            }

            ILblI.CMD_ZCMDS -> {
                p2px++
                if (svPtr.xd[p2px] <= '\u005A') svPtr.xd[p2px] += 32 // lower case
                if (svPtr.xd[p2px] == 'm') { // zmain
                    lab = ILblI.NEXT_CMND
                    continue@machine
                }
                if (svPtr.xd[p2px] == 'g') { // zg
                    svPtr.v1d[0] = NUL_D
                    MglobalDaf(INIT_MGI, svPtr.v1d, svPtr.bd, svPtr)
                    lab = ILblI.NEXT_CMND
                    continue@machine
                }
                if (svPtr.xd[p2px] == 'a') {
                    svPtr.gpad = 0
                    lab = ILblI.NEXT_CMND
                    continue@machine
                }
                if (svPtr.xd[p2px] == 'n') {
                    svPtr.gpad = 1
                    lab = ILblI.NEXT_CMND
                    continue@machine
                }
                if (tolowerI(svPtr.xd[p2px]) == 'h') { // zhalt
                    svPtr.t2 = 1
                    svPtr.ierr = parseDaf(svPtr)
                    if (svPtr.ierr != 0) {
                        lab = ILblI.PARSE_ERR
                        continue@machine
                    }
                    j = atoi(DafCRuntime.cstringAt(svPtr.pd1, svPtr.sdlim))
                    System.out.print("\n")
                    MglobalDaf(CLOSE_MGI, tmp2, tmp3, svPtr) /* Close Globals */
                    fflushI()
                    exitI(j)
                }
                if (svPtr.xd[p2px] == 'e') { // zexit
                    return 1
                    // abrt: rides here (the err family jumps to it);
                    // TERMINATE_ON_ERROR is dead in the reference
                    // build, so the #else body lands (H-018 note)
                    // the fall-through to next_line never fires
                }
                lab = ILblI.NEXT_LINE // all others
                continue@machine
            }

            ILblI.ABRT -> {
                // vendor abrt: under TERMINATE_ON_ERROR (dead) or
                // return 99 (#else, live)
                return 99
            }

            ILblI.CMD_IF -> {
                if (svPtr.xd[svPtr.xpx] == ' ') { // no argument form
                    svPtr.xpx++
                    if (svPtr.tpx != 0) {
                        lab = ILblI.NEXT_CMND
                        continue@machine
                    }
                    svPtr.xpx = STR_MAX_DAF
                    lab = ILblI.NEXT_CMND
                    continue@machine
                }
                lab = ILblI.NEXT_IF_ARG
                continue@machine
            }

            ILblI.NEXT_IF_ARG -> {
                svPtr.t2 = 1
                svPtr.ierr = parseDaf(svPtr)
                if (svPtr.ierr != 0) {
                    lab = ILblI.PARSE_ERR
                    continue@machine
                }
                // (the vendor's commented-out numcomp arm drops)
                if (atoi(DafCRuntime.cstringAt(svPtr.pd1, svPtr.sdlim)) == 0) {
                    svPtr.tpx = 0
                    svPtr.xpx = STR_MAX_DAF
                    lab = ILblI.NEXT_CMND
                    continue@machine
                }
                svPtr.tpx = 1
                if (svPtr.xd[svPtr.xpx] == ',') {
                    svPtr.xpx++
                    lab = ILblI.NEXT_IF_ARG
                    continue@machine
                }
                lab = ILblI.NEXT_CMND
                continue@machine
            }

            ILblI.CMD_ELSE -> {
                if (svPtr.xd[svPtr.xpx] == ' ') svPtr.xpx++
                if (svPtr.tpx == 0) {
                    lab = ILblI.NEXT_CMND
                    continue@machine
                }
                svPtr.xpx = STR_MAX_DAF
                lab = ILblI.NEXT_CMND
                continue@machine
            }

            ILblI.CMD_WRITE -> {
                if (svPtr.io != 5 && svPtr.out_file[svPtr.io] == null) {
                    lab = ILblI.IO_ERROR
                    continue@machine
                }
                // writeproc
                if (svPtr.xd[svPtr.xpx] == '!' || svPtr.xd[svPtr.xpx] == '#') {
                    // new line or page
                    i = 1
                    if (svPtr.xd[svPtr.xpx] != '#') i = 2
                    svPtr.xpx++
                    lab = ILblI.IOSUB
                    continue@machine
                }
                if (svPtr.xd[svPtr.xpx] == '*') { // star output
                    svPtr.xpx++
                    svPtr.t2 = 1
                    svPtr.ierr = parseDaf(svPtr)
                    if (svPtr.ierr != 0) {
                        lab = ILblI.PARSE_ERR
                        continue@machine
                    }
                    svPtr.pd1[svPtr.sdlim] =
                        atoi(DafCRuntime.cstringAt(svPtr.pd1, svPtr.sdlim)).toChar()
                    svPtr.pd1[svPtr.sdlim + 1] = NUL_D
                    System.out.print(svPtr.pd1[svPtr.sdlim]) // putchar
                    i = 4
                    lab = ILblI.IOSUB1
                    continue@machine
                }
                if (svPtr.xd[svPtr.xpx] == '?') { // tab
                    svPtr.t2 = 1
                    svPtr.xpx++
                    svPtr.ierr = parseDaf(svPtr)
                    if (svPtr.ierr != 0) {
                        lab = ILblI.PARSE_ERR
                        continue@machine
                    }
                    j = atoi(DafCRuntime.cstringAt(svPtr.pd1, svPtr.sdlim))
                    i = 3
                    lab = ILblI.IOSUB
                    continue@machine
                }
                svPtr.t2 = 1
                svPtr.ierr = parseDaf(svPtr)
                if (svPtr.ierr != 0) {
                    lab = ILblI.PARSE_ERR
                    continue@machine
                }
                i = 4
                lab = ILblI.IOSUB
                continue@machine
            }

            ILblI.IOSUB -> {
                inoutInterpDaf(sliceAtI(svPtr.pd1, svPtr.sdlim), i.toShort(), j.toShort(), svPtr)
                lab = ILblI.IOSUB1
                continue@machine
            }

            ILblI.IOSUB1 -> {
                if (svPtr.xd[svPtr.xpx] == ',') {
                    svPtr.xpx++
                    lab = ILblI.CMD_WRITE
                    continue@machine
                }
                if (svPtr.xd[svPtr.xpx] == NUL_D || svPtr.xd[svPtr.xpx] == ' ' ||
                        svPtr.xd[svPtr.xpx] == TAB_D) {
                    lab = ILblI.NEXT_CMND
                    continue@machine
                }
                lab = ILblI.CMD_WRITE
                continue@machine
            }

            ILblI.CMD_JOB_I -> {
                // we are parent - continue executing - globals remain
                // open; the JVM render has no fork(), quantized to
                // the always-parent arm (the child drop-into GOTO
                // with its SQLITE sql(CLOSE) close-globals arm is
                // unreachable in a single process, H-001 note; the
                // vendor child arm drops with this note)
                lab = ILblI.NEXT_LINE
                continue@machine
            }

            ILblI.CMD_GOTO -> {
                svPtr.iorg = 1
                retz = GOTO.code
                while (gosub[gsbx][3] == FOR.code) {
                    cmdx = gosub[gsbx][2]
                    gsbx--
                }
                // zap all stacking
                forx = 0
                lab = ILblI.LBL_LBL
                continue@machine
            }

            ILblI.GOTO_RETURN -> {
                forx = 0
                pd1cur = ibr
                ExecLevel = 1
                gsbx = 0
                lab = ILblI.NEXT_LINE
                continue@machine
            }

            ILblI.CMD_MERGE -> {
                MergeFlag = 1
                lab = ILblI.SET_LBL
                continue@machine
            }

            ILblI.CMD_SET -> {
                // SET: SQL: and SHELL: commands too
                MergeFlag = 0
                if (toupperI(svPtr.xd[p2px + 1]) != 'Q' &&
                        toupperI(svPtr.xd[p2px + 1]) != 'H') { // SET
                    lab = ILblI.SET_LBL
                    continue@machine
                }
                // toupper == Q or H: SHELL and SQL commands
                val bufI = CharArray(STR_MAX_DAF)
                val filenameI = CharArray(256)
                var bxI = 0
                var pflagI = 0
                bufI[0] = NUL_D
                filenameI[0] = NUL_D
                if (toupperI(svPtr.xd[p2px + 1]) == 'Q') {
                    pflagI = -1 // SQL undifferentiated
                    svPtr.bd[0] = NUL_D
                    lab = ILblI.NEXT_LINE
                    continue@machine
                }
                // sql codes go here
                // strncasecmp(&pd1[pd1Last],"shell",5) rendered as the
                // lowercase take(5) compare
                if (DafCRuntime.cstringAt(svPtr.pd1, pd1Last).lowercase().take(5) != "shell") {
                    prnterrDaf(37, svPtr, pd1cur) // unrecognized command
                    lab = ILblI.ABRT
                    continue@machine
                }
                // shell and sql command decode - comparable setups
                if (DafCRuntime.cstringAt(svPtr.pd1, pd1Last).lowercase().take(7) == "shell/p") pflagI = 1
                if (DafCRuntime.cstringAt(svPtr.pd1, pd1Last).lowercase().take(7) == "shell/g") pflagI = 2
                var pd1curShell = pd1Last + svPtr.xpx - 2 // vendor local shadow
                while (svPtr.pd1[pd1curShell] != NUL_D && svPtr.pd1[pd1curShell] != ' ') pd1curShell++
                if (svPtr.pd1[pd1curShell] != NUL_D) pd1curShell++
                var shellDone = false
                while (!shellDone) {
                    if (svPtr.pd1[pd1curShell] == NUL_D) {
                        shellDone = true
                        continue
                    }
                    if (svPtr.pd1[pd1curShell] == '&' && svPtr.pd1[pd1curShell + 1] == '~') {
                        pd1curShell += 2
                        i = 1
                        while (svPtr.pd1[pd1curShell] != '~' && svPtr.pd1[pd1curShell] != NUL_D) {
                            svPtr.xd[i] = svPtr.pd1[pd1curShell]
                            pd1curShell++
                            i++
                        }
                        svPtr.xd[i] = NUL_D
                        if (svPtr.pd1[pd1curShell] == '~') pd1curShell++
                        svPtr.t2 = 1
                        svPtr.xpx = 1
                        svPtr.ierr = parseDaf(svPtr)
                        if (svPtr.ierr != 0) {
                            lab = ILblI.PARSE_ERR
                            continue@machine
                        }
                        catAtI(bufI, 0, sliceAtI(svPtr.pd1, svPtr.sdlim))
                        bxI = strlenCI(bufI)
                    } else {
                        bufI[bxI] = svPtr.pd1[pd1curShell]
                        bxI++
                        bufI[bxI] = NUL_D
                        pd1curShell++
                    }
                }
                pd1curShell++
                bufI[bxI] = NUL_D
                if (pflagI == 0) { // SHELL command with no options
                    if (systemI(DafCRuntime.cstring(bufI)) < 0) svPtr.tpx = 0 else svPtr.tpx = 1
                } else if (pflagI == 1) { // SHELL/P command
                    // popen carrier folds the same way (the pipe
                    // handle quantizes to the open flag)
                    popenReadI(DafCRuntime.cstring(bufI))
                        ?.let { svPtr.in_file[6] = 1
                                DafFileHandles.inR[6] = it }
                        ?: run { svPtr.in_file[6] = 0
                                 DafFileHandles.inR[6] = null }
                } else { // SHELL/G
                    popenWriteI(DafCRuntime.cstring(bufI))
                        ?.let { svPtr.out_file[6] = 1
                                DafFileHandles.outW[6] = it }
                        ?: run { svPtr.out_file[6] = 0
                                 DafFileHandles.outW[6] = null }
                    if (svPtr.out_file[6] == 0) svPtr.tpx = 0 else svPtr.tpx = 1
                }
                lab = ILblI.NEXT_LINE
                continue@machine
            }

            ILblI.SET_LBL -> {
                if (svPtr.xd[svPtr.xpx] == '@') { // indirection
                    svPtr.kflg = 1
                    svPtr.t0px = 1
                    svPtr.ierr = parseDaf(svPtr)
                    if (svPtr.ierr != 0) {
                        lab = ILblI.PARSE_ERR
                        continue@machine
                    }
                    svPtr.kflg = 0
                }
                ftmp[0] = NUL_D
                if (svPtr.xd[svPtr.xpx] == '(') { // group LHS
                    svPtr.xpx++
                    vd[1] = NUL_D
                    lab = ILblI.S1
                    continue@machine
                } else {
                    svPtr.t0px = 1
                    svPtr.setname[0] = NUL_D
                    svPtr.ierr = parseDaf(svPtr)
                    if (svPtr.ierr != 0) {
                        lab = ILblI.PARSE_ERR
                        continue@machine
                    }
                    copyAtI(vd, 1, sliceAtI(svPtr.v1d, 1))
                }
                svPtr.xpx += 2
                if (MergeFlag != 0) svPtr.t0px = 1 else svPtr.t0px = 0
                svPtr.t2 = 1
                svPtr.ierr = parseDaf(svPtr)
                if (svPtr.ierr != 0) {
                    lab = ILblI.PARSE_ERR
                    continue@machine
                }
                if (MergeFlag != 0) {
                    copyAtI(MergeRHS, 1, sliceAtI(svPtr.v1d, 1))
                    svPtr.xpx++
                } else MergeRHS[1] = NUL_D
                retz = SET.code
                k = 0
                if (vd[1] != NUL_D) {
                    lab = ILblI.LHSREF
                    continue@machine
                }
                lab = ILblI.S2
                continue@machine
            }

            ILblI.S1 -> {
                svPtr.t0px = 1
                svPtr.ierr = parseDaf(svPtr)
                if (svPtr.ierr != 0) {
                    lab = ILblI.PARSE_ERR
                    continue@machine
                }
                catAtI(ftmp, 0, sliceAtI(svPtr.v1d, 1))
                catAtI(ftmp, 0, " ")
                svPtr.xpx++
                if (svPtr.xd[svPtr.xpx] != ')' && svPtr.xd[svPtr.xpx] != NUL_D) {
                    svPtr.xpx++
                    lab = ILblI.S1
                    continue@machine
                }
                // fall through to the tail of SET_LBL (the vendor
                // falls off the if/else)
                svPtr.xpx += 2
                if (MergeFlag != 0) svPtr.t0px = 1 else svPtr.t0px = 0
                svPtr.t2 = 1
                svPtr.ierr = parseDaf(svPtr)
                if (svPtr.ierr != 0) {
                    lab = ILblI.PARSE_ERR
                    continue@machine
                }
                if (MergeFlag != 0) {
                    copyAtI(MergeRHS, 1, sliceAtI(svPtr.v1d, 1))
                    svPtr.xpx++
                } else MergeRHS[1] = NUL_D
                retz = SET.code
                k = 0
                if (vd[1] != NUL_D) {
                    lab = ILblI.LHSREF
                    continue@machine
                }
                lab = ILblI.S2
                continue@machine
            }

            ILblI.S2 -> {
                j = 1
                while (true) {
                    vd[j] = ftmp[k]
                    j++
                    k++
                    if (vd[j - 1] == ' ') break
                }
                vd[j - 1] = NUL_D
                lab = ILblI.LHSREF
                continue@machine
            }

            ILblI.SET_RETURN -> {
                if (ftmp[k] != NUL_D) {
                    lab = ILblI.S2
                    continue@machine
                }
                if (svPtr.xd[svPtr.xpx] == ',') {
                    svPtr.xpx++
                    lab = ILblI.SET_LBL
                    continue@machine
                }
                lab = ILblI.NEXT_CMND
                continue@machine
            }

            ILblI.QUIT_LBL -> {
                if (gsbx <= 0) {
                    lab = ILblI.RESTORE
                    continue@machine
                }
                if (gosub[gsbx][0] > 0) {
                    lblx = 0 /* zap labels */
                    // memcpy (pd1p, swp[...], len): offset 1 base
                    val srcI = InterpretState.swp[gosub[gsbx][0]]
                    if (srcI != null) {
                        var mI = 0
                        while (mI < gosub[gsbx][4]) {
                            svPtr.pd1[1 + mI] = srcI[mI]
                            mI++
                        }
                    }
                    svPtr.pd1[gosub[gsbx][4] + 1] = NUL_D
                    swapextent = gosub[gsbx][0]
                    var qI = swapextent
                    while (qI < SWAP_I) {
                        if (InterpretState.swp[qI] != null) {
                            InterpretState.swp[qI] = null // free
                        }
                        qI++
                    }
                    if (gosub[gsbx][5] > 0) svPtr.symlen = gosub[gsbx][5]
                }
                if (forx > 0 && gosub[gsbx][3] == FOR.code) {
                    cmdx = gosub[gsbx][2]
                    gsbx--
                    forx--
                    svPtr.xpx = STR_MAX_DAF
                    lab = ILblI.NEXT_CMND
                    continue@machine
                }
                lab = ILblI.QUIT1
                continue@machine
            }

            ILblI.QUIT1 -> {
                pd1cur = gosub[gsbx][1]
                if (gsbx <= 0) {
                    System.out.print("exit\n")
                    return 1
                }
                cmdxold = cmdx
                cmdx = gosub[gsbx][2]
                if (gosub[gsbx][3] == DO.code && BreakFlag != 0) {
                    svPtr.xd[1] = NUL_D
                } else {
                    copyAtI(svPtr.xd, 1, sliceAtI(cmdstack, cmdx))
                }
                svPtr.pd1len = gosub[gsbx][4]
                if (forx > 0) { // patch to fix FOR after DO fixes
                    svPtr.xpx = 1
                }
                retz = gosub[gsbx][3]
                svPtr.tpx = gosub[gsbx][7]
                // evaluate return args before killing symbol table
                svPtr.args[0] = NUL_D
                if (retz != FOR.code && svPtr.xd[svPtr.xpx] != ' ' &&
                        svPtr.xd[svPtr.xpx] != NUL_D) {
                    svPtr.t2 = 1
                    svPtr.ierr = parseDaf(svPtr)
                    if (svPtr.ierr != 0) {
                        lab = ILblI.PARSE_ERR
                        continue@machine
                    }
                    // copy processed return arg to state vector
                    copyAtI(svPtr.args, 0, sliceAtI(svPtr.pd1, svPtr.sdlim))
                    if (svPtr.fdes[0] >= 0) {
                        // the write/close pipe carrier is the DafMsv
                        // fdes pair; the JVM render skips the fd IO
                        // (H-001 note)
                        svPtr.fdes[1] = -1
                    }
                }
                if (retz != FOR.code && retz != XECUTE.code) {
                    ExecLevNew[ExecLevel] = 0 // prevent 2 trips
                    symFreeDaf(1, svPtr)
                }
                gsbx--
                if (forx > 0 && retz == FOR.code) {
                    lab = ILblI.FOR_END
                    continue@machine
                }
                if (retz == DO.code && ExecLevel > 1) {
                    if (ExecLevNew[ExecLevel] != 0) {
                        symFreeDaf(1, svPtr) /* undo New */
                    }
                    ExecLevel--
                }
                if (gosub[gsbx + 1][6] != 0) {
                    ExecLevel = gosub[gsbx + 1][6]
                    gosub[gsbx + 1][6] = 0
                }
                // old - permitted extra blank at EOL to cause loop
                if (svPtr.xd[svPtr.xpx] == NUL_D ||
                        (svPtr.xd[svPtr.xpx] == ' ' &&
                                svPtr.xd[svPtr.xpx + 1] == ' ')) {
                    lab = ILblI.NEXT_CMND
                    continue@machine
                }
                // are we at end of line?
                if (retz == XECUTE.code) {
                    lab = ILblI.XECUTE_RETURN
                    continue@machine
                }
                lab = ILblI.DO_RET1
                continue@machine
            }

            ILblI.CMD_XECUTE -> {
                svPtr.t2 = 1
                svPtr.ierr = parseDaf(svPtr)
                if (svPtr.ierr != 0) {
                    lab = ILblI.PARSE_ERR
                    continue@machine
                }
                ftmp[0] = TAB_D
                copyAtI(ftmp, 1, sliceAtI(svPtr.pd1, svPtr.sdlim))
                if (svPtr.xd[svPtr.xpx] == ':') {
                    svPtr.xpx++
                    svPtr.t2 = 1
                    svPtr.ierr = parseDaf(svPtr)
                    if (svPtr.ierr != 0) {
                        lab = ILblI.PARSE_ERR
                        continue@machine
                    }
                    k = atoi(DafCRuntime.cstringAt(svPtr.pd1, svPtr.sdlim))
                    if (k == 0) {
                        if (svPtr.xd[svPtr.xpx] == ',') {
                            svPtr.xpx++
                            lab = ILblI.CMD_XECUTE
                            continue@machine
                        } else {
                            lab = ILblI.NEXT_CMND
                            continue@machine
                        }
                    }
                }
                // move line to be executed into code space above
                l = svPtr.pd1len
                copyAtI(svPtr.pd1, svPtr.pd1len + 2, ftmp)
                svPtr.pd1len = svPtr.pd1len + 2 + strlenCI(ftmp)
                gsbx++
                if (gsbx >= GOSUBMAX_I) {
                    lab = ILblI.STACK_OVERFLOW
                    continue@machine
                }
                gosub[gsbx][0] = 0 /* swap origin */
                gosub[gsbx][1] = pd1cur /* restore pd1cur */
                gosub[gsbx][2] = cmdx /* command stack address */
                copyAtI(cmdstack, cmdx, sliceAtI(svPtr.xd, svPtr.xpx))
                cmdx = cmdx + DafCRuntime.cstringAt(svPtr.xd, svPtr.xpx).length + 1
                gosub[gsbx][4] = l /* restore pd1len */
                gosub[gsbx][3] = XECUTE.code
                pd1cur = l + 2
                lab = ILblI.NEXT_LINE_XECUTE
                continue@machine
            }

            ILblI.XECUTE_RETURN -> {
                // return point - environment restored
                if (svPtr.xd[svPtr.xpx] == ' ' || svPtr.xd[svPtr.xpx] == NUL_D) {
                    lab = ILblI.NEXT_CMND
                    continue@machine
                }
                lab = ILblI.CMD_XECUTE
                continue@machine
            }

            ILblI.CMD_FOR -> {
                // stack set up
                forx++
                if (forx >= FORMAX_I) {
                    System.out.print("OVERFLOW\n")
                    lab = ILblI.STACK_OVERFLOW
                    continue@machine
                }
                forit[forx] = NUL_D
                forlt[forx] = NUL_D
                forv[forx][0] = NUL_D
                gsbx++
                if (gsbx >= GOSUBMAX_I) {
                    lab = ILblI.STACK_OVERFLOW
                    continue@machine
                }
                gosub[gsbx][0] = 0 /* swap */
                gosub[gsbx][1] = pd1cur
                gosub[gsbx][2] = cmdx
                gosub[gsbx][3] = FOR.code
                gosub[gsbx][4] = svPtr.pd1len.toInt()
                if (svPtr.xd[svPtr.xpx] == ' ') {
                    i = 1
                    copyAtSI(forv[forx], 0, "_dummy")
                    copyAtSI(forin[forx], 0, "0")
                    copyAtSI(forlv[forx], 0, "100")
                    svPtr.symflg = 0 /* store */
                    symSeatI(0, sliceAtI(forv[forx], 0),
                        charArrayOf('1', NUL_D), svPtr)
                    forit[forx] = '1'
                    lab = ILblI.ARGLESS_LBL
                    continue@machine
                }
                // find local variable
                svPtr.t0px = 1
                svPtr.ierr = parseDaf(svPtr)
                if (svPtr.ierr != 0) {
                    lab = ILblI.PARSE_ERR
                    continue@machine
                }
                svPtr.xpx += 2
                copyAtI(forv[forx], 0, sliceAtI(svPtr.v1d, 1))
                svPtr.symflg = 0 /* store */
                dosave[0] = NUL_D
                symSeatI(0, sliceAtI(forv[forx], 0), dosave, svPtr)
                lab = ILblI.NXT_ARG
                continue@machine
            }

            ILblI.NXT_ARG -> {
                forit[forx] = NUL_D
                forlt[forx] = NUL_D
                svPtr.t2 = 1
                if (svPtr.xd[svPtr.xpx] == '$') { /* function */
                    forit[forx] = '1'
                    forlt[forx] = '1'
                    forlv[forx][0] = NUL_D // function
                    copyAtI(forin[forx], 0, sliceAtI(svPtr.xd, svPtr.xpx))
                }
                svPtr.ierr = parseDaf(svPtr)
                if (svPtr.ierr != 0) {
                    lab = ILblI.PARSE_ERR
                    continue@machine
                }
                forit[forx] = NUL_D // initial value
                copyAtI(ftmp, 0, sliceAtI(svPtr.pd1, svPtr.sdlim))
                if (svPtr.xd[svPtr.xpx] == ',' || svPtr.xd[svPtr.xpx] == ' ' ||
                        svPtr.xd[svPtr.xpx] == NUL_D) {
                    lab = ILblI.INIT_LOOP_VAR
                    continue@machine
                }
                if (svPtr.xd[svPtr.xpx] != ':') {
                    lab = ILblI.ARG_LIST_ERROR
                    continue@machine
                }
                // increment arm block
                svPtr.symflg = 0 /* store */
                symSeatI(0, sliceAtI(forv[forx], 0), ftmp, svPtr)
                svPtr.t2 = 1
                svPtr.xpx++
                val xdtmpI = CharArray(STR_MAX_DAF)
                copyAtI(xdtmpI, 0, sliceAtI(svPtr.xd, svPtr.xpx))
                var xdtmpxI = svPtr.xpx
                svPtr.ierr = parseDaf(svPtr)
                if (svPtr.ierr != 0) {
                    lab = ILblI.PARSE_ERR
                    continue@machine
                }
                xdtmpxI = svPtr.xpx - xdtmpxI
                xdtmpI[xdtmpxI] = NUL_D
                forit[forx] = '1'
                /* increment */
                copyAtI(forin[forx], 0, sliceAtI(xdtmpI, 0))
                if (svPtr.xd[svPtr.xpx] == ',' || svPtr.xd[svPtr.xpx] == NUL_D ||
                        svPtr.xd[svPtr.xpx] == ' ' || svPtr.xd[svPtr.xpx] == TAB_D) {
                    lab = ILblI.INIT_LOOP_VAR
                    continue@machine
                }
                if (svPtr.xd[svPtr.xpx] != ':') {
                    lab = ILblI.ARG_LIST_ERROR
                    continue@machine
                }
                svPtr.t2 = 1
                svPtr.xpx++
                svPtr.ierr = parseDaf(svPtr)
                if (svPtr.ierr != 0) {
                    lab = ILblI.PARSE_ERR
                    continue@machine
                }
                forlt[forx] = '1' // limit
                copyAtI(forlv[forx], 0, sliceAtI(svPtr.pd1, svPtr.sdlim))
                if (svPtr.xd[svPtr.xpx] == ',' || svPtr.xd[svPtr.xpx] == ' ' ||
                        svPtr.xd[svPtr.xpx] == NUL_D || svPtr.xd[svPtr.xpx] == TAB_D) {
                    lab = ILblI.INIT_LOOP_VAR
                    continue@machine
                }
                lab = ILblI.ARG_LIST_ERROR
                continue@machine
            }

            ILblI.INIT_LOOP_VAR -> {
                svPtr.symflg = 0 /* store */
                // set value
                symSeatI(0, sliceAtI(forv[forx], 0), ftmp, svPtr)
                if (forin[forx][0] == '$') {
                    if (DafCRuntime.cstring(ftmp).isEmpty()) i = 1 else i = 0
                    forit[forx] = '1'
                    lab = ILblI.ARGLESS_LBL
                    continue@machine
                } else i = 0
                if (forlt[forx] == '1') {
                    if (numcompDaf(DafCRuntime.cstring(forin[forx]), DafCRuntime.cstring(zeroArrI)) >= 0) {
                        if (numcompDaf(DafCRuntime.cstring(ftmp), DafCRuntime.cstring(forlv[forx])) > 0) i = 1
                    } else {
                        if (numcompDaf(DafCRuntime.cstring(ftmp), DafCRuntime.cstring(forlv[forx])) < 0) i = 1
                    }
                }
                lab = ILblI.ARGLESS_LBL
                continue@machine
            }

            ILblI.ARGLESS_LBL -> {
                copyAtI(cmdstack, cmdx, sliceAtI(svPtr.xd, svPtr.xpx))
                cmdx = cmdx + DafCRuntime.cstringAt(svPtr.xd, svPtr.xpx).length + 1
                lab = ILblI.BYP
                continue@machine
            }

            ILblI.BYP -> {
                while (svPtr.xd[svPtr.xpx] != NUL_D && svPtr.xd[svPtr.xpx] != ' ') {
                    svPtr.t2 = 1
                    svPtr.xpx++
                    parseDaf(svPtr)
                }
                if (i == 0) {
                    lab = ILblI.NEXT_CMND
                    continue@machine
                } else {
                    lab = ILblI.QUIT1
                    continue@machine
                }
            }

            ILblI.FOR_END -> {
                // end of for return
                if (forit[forx] != '1') {
                    lab = ILblI.FOR_QUIT
                    continue@machine
                }
                // increment loop variable
                val itmpI = CharArray(STR_MAX_DAF)
                svPtr.symflg = 1 /* retrieve */
                symSeatI(11, sliceAtI(forv[forx], 0), itmpI, svPtr)
                val oldI = CharArray(STR_MAX_DAF)
                var oldxpxI = 0
                copyAtI(oldI, 0, sliceAtI(svPtr.xd, 1))
                oldxpxI = svPtr.xpx
                copyAtI(svPtr.xd, 0, sliceAtI(forin[forx], 0))
                svPtr.xpx = 0
                svPtr.t2 = 1
                svPtr.ierr = parseDaf(svPtr)
                if (svPtr.ierr != 0) {
                    lab = ILblI.PARSE_ERR
                    continue@machine // increment amount
                }
                // dsve = &pd1[sdlim]: the parse result slice
                val dsveI = sliceAtI(svPtr.pd1, svPtr.sdlim)
                copyAtI(svPtr.xd, 1, sliceAtI(oldI, 0))
                svPtr.xpx = oldxpxI
                if (forin[forx][0] != '$') addDaf(DafCRuntime.cstring(itmpI), DafCRuntime.cstring(dsveI), ftmp)
                else copyAtI(ftmp, 0, sliceAtI(dsveI, 0))
                if (BreakFlag != 0) {
                    BreakFlag = 0
                    lab = ILblI.FOR_QUIT
                    continue@machine
                }
                if (forlt[forx] != '1') {
                    svPtr.symflg = 0 /* store */
                    symSeatI(0, sliceAtI(forv[forx], 0), ftmp, svPtr)
                    gsbx++
                    if (gsbx >= GOSUBMAX_I) {
                        lab = ILblI.STACK_OVERFLOW
                        continue@machine
                    }
                    cmdx = cmdxold
                    if (svPtr.xd[svPtr.xpx] == ',') {
                        i = 0
                        lab = ILblI.BYP
                        continue@machine
                    }
                    lab = ILblI.NEXT_CMND
                    continue@machine
                }
                // limit test
                svPtr.symflg = 0 /* store */
                symSeatI(0, sliceAtI(forv[forx], 0), ftmp, svPtr)
                if (forin[forx][0] == '$') {
                    if (forlv[forx][0] == NUL_D) { // special case null string limit
                        if (DafCRuntime.cstring(ftmp).isEmpty()) {
                            lab = ILblI.FOR_QUIT
                            continue@machine
                        }
                    } else {
                        if (strcmpI(ftmp, forlv[forx]) >= 0) {
                            lab = ILblI.FOR_QUIT
                            continue@machine
                        }
                    }
                } else if (numcompDaf(DafCRuntime.cstring(forin[forx]), DafCRuntime.cstring(zeroArrI)) >= 0) {
                    if (numcompDaf(DafCRuntime.cstring(ftmp), DafCRuntime.cstring(forlv[forx])) > 0) {
                        lab = ILblI.FOR_QUIT
                        continue@machine
                    }
                } else {
                    if (numcompDaf(DafCRuntime.cstring(ftmp), DafCRuntime.cstring(forlv[forx])) < 0) {
                        lab = ILblI.FOR_QUIT
                        continue@machine
                    }
                }
                gsbx++
                if (gsbx >= GOSUBMAX_I) {
                    lab = ILblI.STACK_OVERFLOW
                    continue@machine
                }
                cmdx = cmdxold
                if (svPtr.xd[svPtr.xpx] == ',') {
                    i = 0
                    lab = ILblI.BYP
                    continue@machine
                }
                lab = ILblI.NEXT_CMND
                continue@machine
            }

            ILblI.FOR_QUIT -> {
                if (svPtr.xd[svPtr.xpx] == ',') {
                    svPtr.xpx++
                    gsbx++
                    if (gsbx >= GOSUBMAX_I) {
                        lab = ILblI.STACK_OVERFLOW
                        continue@machine
                    }
                    lab = ILblI.NXT_ARG
                    continue@machine
                }
                forx--
                svPtr.xpx = STR_MAX_DAF
                lab = ILblI.NEXT_CMND
                continue@machine
            }

            ILblI.CMD_HALT -> {
                if ((svPtr.xd[svPtr.xpx] == ' ' || svPtr.xd[svPtr.xpx] == NUL_D)
                        && toupperI(svPtr.xd[p2px + 1]) != 'T') {
                    mltoaDaf(getpidI().toLong(), tmp2)
                    if (svPtr.LockDone != 0) cleanLocksDaf(DafCRuntime.cstring(tmp2))
                    MglobalDaf(CLOSE_MGI, tmp2, tmp3, svPtr) /* Close Globals */
                    fflushI()
                    exitI(0) // EXIT_SUCCESS
                    return 0
                }
                if (toupperI(svPtr.xd[p2px + 1]) == 'T') { /* html command */
                    var pd1curShell = pd1Last + svPtr.xpx - 2 // vendor shadow
                    while (svPtr.pd1[pd1curShell] != NUL_D && svPtr.pd1[pd1curShell] != ' ') pd1curShell++
                    if (svPtr.pd1[pd1curShell] != NUL_D) pd1curShell++
                    var htmlDone = false
                    while (!htmlDone) {
                        if (svPtr.pd1[pd1curShell] == NUL_D) {
                            htmlDone = true
                            continue
                        }
                        if (svPtr.pd1[pd1curShell] == '&' && svPtr.pd1[pd1curShell + 1] == '~') {
                            pd1curShell += 2
                            i = 1
                            while (svPtr.pd1[pd1curShell] != '~' && svPtr.pd1[pd1curShell] != NUL_D) {
                                svPtr.xd[i] = svPtr.pd1[pd1curShell]
                                pd1curShell++
                                i++
                            }
                            svPtr.xd[i] = NUL_D
                            if (svPtr.pd1[pd1curShell] == '~') pd1curShell++
                            svPtr.t2 = 1
                            svPtr.xpx = 1
                            svPtr.ierr = parseDaf(svPtr)
                            if (svPtr.ierr != 0) {
                                lab = ILblI.PARSE_ERR
                                continue@machine
                            }
                            System.out.print(DafCRuntime.cstringAt(svPtr.pd1, svPtr.sdlim))
                        } else if (svPtr.pd1[pd1curShell] == '&' && svPtr.pd1[pd1curShell + 1] == '!') {
                            pd1curShell += 2
                            System.out.print("\n") // putchar
                        } else {
                            System.out.print(svPtr.pd1[pd1curShell]) // putchar
                            pd1curShell++
                        }
                    }
                    pd1curShell++
                    lab = ILblI.NEXT_LINE
                    continue@machine
                }
                lab = ILblI.HANG_LBL
                continue@machine
            }

            ILblI.HANG_LBL -> {
                svPtr.t2 = 1
                svPtr.ierr = parseDaf(svPtr)
                if (svPtr.ierr != 0) {
                    lab = ILblI.PARSE_ERR
                    continue@machine
                }
                j = atoi(DafCRuntime.cstringAt(svPtr.pd1, svPtr.sdlim))
                if (j > 0) {
                    // NATIVE_SHARED MglobalDaf(PULSE) deadlock arm dead (H-018)
                }
                sleepI(j)
                if (svPtr.xd[svPtr.xpx] == ',') {
                    svPtr.xpx++
                    lab = ILblI.HANG_LBL
                    continue@machine
                }
                lab = ILblI.NEXT_CMND
                continue@machine
            }

            ILblI.CMD_DO -> { // or DATABASE
                if (toupperI(svPtr.xd[p2px + 1]) == 'E') { /* DECLARE command */
                    lab = ILblI.NEXT_LINE // ignore
                    continue@machine
                }
                if (toupperI(svPtr.xd[p2px + 1]) == 'A') { /* DATABASE command */
                    svPtr.t2 = 1
                    while (svPtr.xd[svPtr.xpx] != NUL_D) {
                        if (svPtr.xd[svPtr.xpx] != ' ') break
                        svPtr.xpx++
                    }
                    // the vendor double ierr assignment quirk kept
                    svPtr.ierr = parseDaf(svPtr)
                    svPtr.ierr = svPtr.ierr
                    if (svPtr.ierr != 0) {
                        lab = ILblI.PARSE_ERR
                        continue@machine
                    }
                    if (DafCRuntime.cstringAt(svPtr.pd1, svPtr.sdlim).length > 1000) {
                        lab = ILblI.FILE_ERROR
                        continue@machine
                    }
                    val udatI = DafCRuntime.cstringAt(svPtr.pd1, svPtr.sdlim)
                    copyAtSI(svPtr.UDAT, 0, udatI + ".dat")
                    copyAtSI(svPtr.UKEY, 0, udatI + ".key")
                    lab = ILblI.NEXT_LINE
                    continue@machine
                }
                lab = ILblI.DO_RET1
                continue@machine
            }

            ILblI.DO_RET1 -> {
                argless = 0
                NewFlag = 0
                if (svPtr.xd[svPtr.xpx] == ' ' || svPtr.xd[svPtr.xpx] == NUL_D) {
                    val tmpI = CharArray(1024)
                    copyAtSI(tmpI, 0, "+0  ")
                    ExecLevel++
                    ExecLevNew[ExecLevel] = 0
                    catAtI(tmpI, 0, sliceAtI(svPtr.xd, svPtr.xpx))
                    copyAtI(svPtr.xd, 1, sliceAtI(tmpI, 0))
                    svPtr.xpx = 1
                    argless = 1
                }
                pd1hold1 = svPtr.pd1len.toInt()
                pd1hold2 = pd1cur
                svPtr.iorg = 1
                shold = 0
                retz = DO.code
                lab = ILblI.LBL_LBL
                continue@machine
            }

            ILblI.DO_RET -> {
                gsbx++
                if (gsbx >= GOSUBMAX_I) {
                    lab = ILblI.STACK_OVERFLOW
                    continue@machine
                }
                if (shold > 0) gosub[gsbx][0] = shold else gosub[gsbx][0] = 0
                gosub[gsbx][1] = pd1hold2
                gosub[gsbx][2] = cmdx
                gosub[gsbx][8] = NewFlag
                copyAtI(cmdstack, cmdx, sliceAtI(svPtr.xd, svPtr.xpx))
                cmdx = cmdx + DafCRuntime.cstringAt(svPtr.xd, svPtr.xpx).length + 1
                gosub[gsbx][3] = DO.code
                gosub[gsbx][4] = pd1hold1
                pd1cur = ibr
                gosub[gsbx][5] = 0
                gosub[gsbx][7] = svPtr.tpx
                if (argless == 0) {
                    gosub[gsbx][6] = ExecLevel
                    ExecLevel = 1
                }
                if (SymInc == 0) { // flag indicates inc was already done
                    svPtr._Sym++
                    svPtr.start[svPtr._Sym] = 0 // the C NULL sentinel folds to 0 in the Int domain
                }
                lab = ILblI.NEXT_LINE
                continue@machine
            }

            ILblI.CMD_NEW -> {
                svPtr.nflg = 0
                lab = ILblI.NEW_AGAIN
                continue@machine
            }

            ILblI.NEW_AGAIN -> {
                if (svPtr.xd[svPtr.xpx] == ' ' ||
                        svPtr.xd[svPtr.xpx] == NUL_D ||
                        svPtr.xd[svPtr.xpx] == TAB_D) {
                    /* new except none */
                    svPtr.symflg = 7
                    DafNmesState.chain = null /* list freed in sym_() */
                    symSeatI(7, charArrayOf(NUL_D), charArrayOf(NUL_D), svPtr)
                    DafNmesState.chain = null /* list freed in sym_() */
                    ExecLevNew[ExecLevel] = 1
                    lab = ILblI.NEXT_CMND
                    continue@machine
                }
                /* New indirect */
                if (svPtr.xd[svPtr.xpx] == '@') {
                    svPtr.kflg = 1
                    svPtr.t0px = 1
                    svPtr.ierr = parseDaf(svPtr)
                    if (svPtr.ierr != 0) {
                        lab = ILblI.PARSE_ERR
                        continue@machine
                    }
                    svPtr.kflg = 0
                }
                /* New specific. new a,b,c */
                if (svPtr.xd[svPtr.xpx] != '(') {
                    svPtr.t0px = 1
                    svPtr.ierr = parseDaf(svPtr)
                    if (svPtr.ierr != 0) {
                        lab = ILblI.PARSE_ERR
                        continue@machine
                    }
                    symSeatI(100, sliceAtI(svPtr.v1d, 1), charArrayOf(NUL_D), svPtr)
                } else {
                    /* New all locals except: linked list of var
                       names not to be newed (the malloc-NULL OOM
                       arms drop per the file-header H-013 note) */
                    DafNmesState.chain = null
                    svPtr.xpx++
                    lab = ILblI.NEW2
                    continue@machine
                }
                svPtr.xpx++
                if (svPtr.xd[svPtr.xpx] == ',') {
                    svPtr.xpx++
                    lab = ILblI.NEW_AGAIN
                    continue@machine
                }
                lab = ILblI.NEXT_CMND
                continue@machine
            }

            ILblI.NEW2 -> {
                svPtr.t0px = 1
                svPtr.ierr = parseDaf(svPtr)
                if (svPtr.ierr != 0) {
                    lab = ILblI.PARSE_ERR
                    continue@machine
                }
                keyfixDaf(sliceAtI(svPtr.v1d, 1)) /* in case its an array */
                val nmI = DafCRuntime.cstringAt(svPtr.v1d, 1)
                DafNmesState.chain = NmesI(nmI, DafNmesState.chain)
                svPtr.xpx++
                if (svPtr.xd[svPtr.xpx] != ')' && svPtr.xd[svPtr.xpx] != NUL_D) {
                    svPtr.xpx++
                    lab = ILblI.NEW2
                    continue@machine
                }
                svPtr.symflg = 7
                /* new except */
                symSeatI(7, charArrayOf(NUL_D), charArrayOf(NUL_D), svPtr)
                DafNmesState.chain = null /* list freed in sym_() */
                ExecLevNew[ExecLevel] = 1
                svPtr.xpx++
                if (svPtr.xd[svPtr.xpx] == ',') {
                    svPtr.xpx++
                    lab = ILblI.NEW_AGAIN
                    continue@machine
                }
                lab = ILblI.NEXT_CMND
                continue@machine
            }

            ILblI.CMD_DATAKILL -> {
                // Kill all local variables
                lab = ILblI.KILL_AGAIN
                continue@machine
            }

            ILblI.KILL_AGAIN -> {
                if (svPtr.xd[svPtr.xpx] == ' ' || svPtr.xd[svPtr.xpx] == NUL_D ||
                        svPtr.xd[svPtr.xpx] == TAB_D) {
                    svPtr.symflg = 4
                    symSeatI(4, charArrayOf(NUL_D), charArrayOf(NUL_D), svPtr)
                    lab = ILblI.NEXT_CMND
                    continue@machine
                }
                // Kill indirect
                if (svPtr.xd[svPtr.xpx] == '@') {
                    svPtr.kflg = 1
                    svPtr.t0px = 1
                    svPtr.ierr = parseDaf(svPtr)
                    if (svPtr.ierr != 0) {
                        lab = ILblI.PARSE_ERR
                        continue@machine
                    }
                    svPtr.kflg = 0
                }
                // Kill specific - local or global
                if (svPtr.xd[svPtr.xpx] != '(') {
                    svPtr.t0px = 1
                    svPtr.ierr = parseDaf(svPtr)
                    if (svPtr.ierr != 0) {
                        lab = ILblI.PARSE_ERR
                        continue@machine
                    }
                    if (svPtr.v1d[1] != '^') { /* kill a local */
                        svPtr.symflg = 2 /* kill */
                        symSeatI(2, sliceAtI(svPtr.v1d, 1), charArrayOf(NUL_D), svPtr)
                    } else { /* globals */
                        keyfixDaf(sliceAtI(svPtr.v1d, 1))
                        // original length - comp for start
                        k = DafCRuntime.cstringAt(svPtr.v1d, 1).length + 1
                        if (svPtr.v1d[k - 1] != CHAR_1) {
                            svPtr.v1d[k] = CHAR_1
                            k++
                        }
                        svPtr.v1d[k] = NUL_D
                        k--
                        copyAtI(tmp2, 0, sliceAtI(svPtr.v1d, 1))
                        /* kill first */
                        MglobalDaf(KILL_D, sliceAtI(svPtr.v1d, 1), svPtr.bd, svPtr)
                        // the #if !(SQLITE) XNEXT kill-others loop
                        // (k1:) is dead in the reference build
                        // (SQLITE live, H-018 note)
                    }
                } else {
                    // Kill all locals except: the except-list
                    DafNmesState.chain = null
                    svPtr.xpx++
                    lab = ILblI.KILL2
                    continue@machine
                }
                svPtr.xpx++
                if (svPtr.xd[svPtr.xpx] == ',') {
                    svPtr.xpx++
                    lab = ILblI.KILL_AGAIN
                    continue@machine
                }
                lab = ILblI.NEXT_CMND
                continue@machine
            }

            ILblI.KILL2 -> {
                svPtr.t0px = 1
                svPtr.ierr = parseDaf(svPtr)
                if (svPtr.ierr != 0) {
                    lab = ILblI.PARSE_ERR
                    continue@machine
                }
                keyfixDaf(sliceAtI(svPtr.v1d, 1)) /* in case its an array */
                val nmI = DafCRuntime.cstringAt(svPtr.v1d, 1)
                DafNmesState.chain = NmesI(nmI, DafNmesState.chain)
                svPtr.xpx++
                if (svPtr.xd[svPtr.xpx] != ')' && svPtr.xd[svPtr.xpx] != NUL_D) {
                    svPtr.xpx++
                    lab = ILblI.KILL2
                    continue@machine
                }
                svPtr.symflg = 5
                symSeatI(5, charArrayOf(NUL_D), charArrayOf(NUL_D), svPtr)
                DafNmesState.chain = null
                svPtr.xpx++
                if (svPtr.xd[svPtr.xpx] == ',') {
                    svPtr.xpx++
                    lab = ILblI.KILL_AGAIN
                    continue@machine
                }
                lab = ILblI.NEXT_CMND
                continue@machine
            }

            ILblI.CMD_FILECLOSE -> {
                p2px++
                if (svPtr.xd[p2px] <= '\u005A') svPtr.xd[p2px] += 32 // lower case
                if (svPtr.xd[p2px] == 'o') {
                    lab = ILblI.QUIT_LBL
                    continue@machine
                }
                lab = ILblI.NEXT_CLOSE_ARG
                continue@machine
            }

            ILblI.NEXT_CLOSE_ARG -> {
                svPtr.t2 = 1
                svPtr.ierr = parseDaf(svPtr)
                if (svPtr.ierr != 0) {
                    lab = ILblI.PARSE_ERR
                    continue@machine
                }
                i = atoi(DafCRuntime.cstringAt(svPtr.pd1, svPtr.sdlim))
                // handle-domain fold: 0 is the closed flag (the
                // OPEN/SHELL arms folded the JVM carriers; no
                // stream carrier exists to close here)
                if (svPtr.in_file[i] == 0 && svPtr.out_file[i] == 0) {
                    lab = ILblI.FILE_ERROR
                    continue@machine
                }
                svPtr.in_file[i] = 0
                svPtr.out_file[i] = 0
                // the unit-285 fold: the JVM stream carriers ride
                // DafFileHandles; CLOSE closes and clears them
                if (i in 0..9) {
                    DafFileHandles.inR[i]?.close(); DafFileHandles.inR[i] = null
                    DafFileHandles.outW[i]?.close(); DafFileHandles.outW[i] = null
                }
                if (svPtr.xd[svPtr.xpx] != ',') {
                    lab = ILblI.NEXT_CMND
                    continue@machine
                }
                svPtr.xpx++
                lab = ILblI.NEXT_CLOSE_ARG
                continue@machine
            }

            ILblI.CMD_USE -> {
                svPtr.t2 = 1
                svPtr.ierr = parseDaf(svPtr)
                if (svPtr.ierr != 0) {
                    lab = ILblI.PARSE_ERR
                    continue@machine
                }
                svPtr.io = atoi(DafCRuntime.cstringAt(svPtr.pd1, svPtr.sdlim))
                if (svPtr.io <= 0 || svPtr.io > 10) {
                    lab = ILblI.ARG_LIST_ERROR
                    continue@machine
                }
                if (svPtr.in_file[svPtr.io] == null && svPtr.out_file[svPtr.io] == null) {
                    lab = ILblI.USE_NOT_OPEN
                    continue@machine
                }
                lab = ILblI.NEXT_CMND
                continue@machine
            }

            // remote block to process lhs type references
            ILblI.LHSREF -> {
                if (vd[1] == '^') {
                    copyAtI(svPtr.v1d, 0, sliceAtI(vd, 1))
                    copyAtI(svPtr.bd, 0, sliceAtI(svPtr.pd1, svPtr.sdlim))
                    if (MergeFlag == 0) {
                        MglobalDaf(STORE_D, svPtr.v1d, svPtr.bd, svPtr)
                    } else {
                        mergeDaf(svPtr.v1d, sliceAtI(MergeRHS, 1), svPtr)
                    }
                } else {
                    if (vd[1] == '$' && (vd[2] == 'p' || vd[2] == 'P')) { // piece
                        copyAtI(tmp3, 0, sliceAtI(svPtr.pd1, svPtr.sdlim))
                        copyAtI(svPtr.v1d, 1, sliceAtI(vd, 1))
                        svPtr.setpiece = 1
                        dafFcn(svPtr)
                        svPtr.setpiece = 0
                        lab = ILblI.LHSREF1
                        continue@machine
                    } else if (vd[1] == '$' && (vd[2] == 'e' || vd[2] == 'E')) { // extract
                        copyAtI(tmp3, 0, sliceAtI(svPtr.pd1, svPtr.sdlim))
                        copyAtI(svPtr.v1d, 1, sliceAtI(vd, 1))
                        svPtr.setpiece = 1
                        dafFcn(svPtr)
                        svPtr.setpiece = 0
                        lab = ILblI.LHSREF1
                        continue@machine
                    } else if (vd[1] == '$' && (vd[2] == 'z' || vd[2] == 'Z')) { // z var
                        copyAtI(tmp3, 0, sliceAtI(svPtr.pd1, svPtr.sdlim))
                        copyAtI(svPtr.v1d, 1, sliceAtI(vd, 1))
                        svPtr.setpiece = 1
                        dafFcn(svPtr)
                        svPtr.setpiece = 0
                        lab = ILblI.LHSREF1
                        continue@machine
                    } else {
                        svPtr.symflg = 0 /* store */
                        symSeatI(0, sliceAtI(vd, 1),
                            sliceAtI(svPtr.pd1, svPtr.sdlim), svPtr)
                    }
                }
                lab = ILblI.LHSREF1
                continue@machine
            }

            ILblI.LHSREF1 -> {
                if (retz == READ.code) {
                    lab = ILblI.CONT_READ
                } else {
                    lab = ILblI.SET_RETURN
                }
                continue@machine
            }

            // remote block to process DO and GOTO arguments
            ILblI.LBL_LBL -> {
                SymInc = 0
                if (svPtr.xd[svPtr.xpx] == NUL_D) {
                    lab = ILblI.LBL_ERROR
                    continue@machine
                }
                // indirect argument
                if (svPtr.xd[svPtr.xpx] == '@') {
                    svPtr.t0px = 1
                    svPtr.t2 = 2
                    svPtr.ierr = parseDaf(svPtr)
                    if (svPtr.ierr != 0) {
                        lab = ILblI.LBL_ERROR
                        continue@machine
                    }
                    if (svPtr.t0px == 0) {
                        // copy label part to do save
                        var iLabelI = 1
                        while (svPtr.v1d[iLabelI] != NUL_D && svPtr.v1d[iLabelI] < '\u007F') iLabelI++
                        copyAtI(dosave, 0, sliceAtI(svPtr.v1d, 1).copyOf(iLabelI - 1).also { it[iLabelI - 2] = NUL_D })
                        if (svPtr.v1d[iLabelI] != NUL_D) {
                            // arguments follow the label
                            copyAtI(svPtr.args, 0, sliceAtI(svPtr.v1d, iLabelI + 1))
                        }
                    } else {
                        copyAtI(dosave, 0, sliceAtI(svPtr.pd1, svPtr.sdlim))
                    }
                    if (svPtr.t0px == 0) svPtr.xpx++
                } else if (svPtr.xd[svPtr.xpx] == '$') { // builtin function
                    svPtr.t2 = 1
                    svPtr.ierr = parseDaf(svPtr)
                    if (svPtr.ierr != 0) {
                        lab = ILblI.PARSE_ERR
                        continue@machine
                    }
                    lab = ILblI.NEXT_CMND
                    continue@machine
                } else {
                    // extract label and offset from xd
                    i = 0
                    j = svPtr.xpx
                    while (svPtr.xd[svPtr.xpx] != NUL_D) {
                        if (svPtr.xd[svPtr.xpx] == ',' || svPtr.xd[svPtr.xpx] == ' ' ||
                                svPtr.xd[svPtr.xpx] == NUL_D || svPtr.xd[svPtr.xpx] == '+' ||
                                svPtr.xd[svPtr.xpx] == '(' ||
                                svPtr.xd[svPtr.xpx] == '^' || svPtr.xd[svPtr.xpx] == ':') break
                        dosave[i] = svPtr.xd[svPtr.xpx]
                        i++
                        svPtr.xpx++
                    }
                    dosave[i] = NUL_D
                }
                // handles do xxx(a,b,c) - not do xxx^yyy(a,b,c)
                // fcn call with args
                if (svPtr.xd[svPtr.xpx] == '(' || svPtr.xd[svPtr.xpx] == OPEN_PAREN_I) {
                    if (svPtr.xd[svPtr.xpx + 1] == ')') {
                        // empty argument list case ()
                        svPtr.args[0] = NUL_D
                        svPtr.args[1] = NUL_D
                        svPtr.args[2] = NUL_D
                        svPtr.xpx += 2
                    } else {
                        svPtr.t0px = 1
                        svPtr.t2 = 1
                        svPtr.xpx--
                        svPtr.xd[svPtr.xpx] = '$'
                        svPtr.ierr = parseDaf(svPtr)
                        if (svPtr.ierr != 0) {
                            lab = ILblI.PARSE_ERR
                            continue@machine
                        }
                        svPtr.xpx++
                        copyAtI(svPtr.args, 0, sliceAtI(svPtr.v1d, 3))
                    }
                }
                k = -1
                if (svPtr.xd[svPtr.xpx] == '+') {
                    svPtr.t2 = 2
                    svPtr.ierr = parseDaf(svPtr)
                    if (svPtr.ierr != 0) {
                        lab = ILblI.PARSE_ERR
                        continue@machine
                    }
                    k = atoi(DafCRuntime.cstringAt(svPtr.pd1, svPtr.sdlim))
                    if (k < 0) {
                        lab = ILblI.LBL_ERROR
                        continue@machine
                    }
                }
                svPtr.v1d[1] = NUL_D
                if (svPtr.xd[svPtr.xpx] != ' ' &&
                        svPtr.xd[svPtr.xpx] != ',' &&
                        svPtr.xd[svPtr.xpx] != NUL_D &&
                        svPtr.xd[svPtr.xpx] != ':') {
                    svPtr.t0px = 1
                    svPtr.t2 = 1
                    if (svPtr.xd[svPtr.xpx] == '^' && svPtr.xd[svPtr.xpx + 1] == '@') {
                        svPtr.xpx++
                        fileflg = 1
                    } else fileflg = 0
                    svPtr.ierr = parseDaf(svPtr)
                    if (svPtr.ierr != 0) {
                        lab = ILblI.FILE_ERROR
                        continue@machine
                    }
                    if (fileflg == 1) {
                        copyAtI(ftmp, 0, sliceAtI(svPtr.v1d, 1))
                        svPtr.v1d[1] = '^'
                        copyAtI(svPtr.v1d, 2, sliceAtI(ftmp, 0))
                    }
                    if (svPtr.xd[svPtr.xpx] != ':') svPtr.xpx++
                }
                if (svPtr.xd[svPtr.xpx] == ':') {
                    svPtr.xpx++
                    svPtr.t2 = 1
                    svPtr.t0px = 0
                    copyAtI(ftmp, 0, sliceAtI(svPtr.v1d, 1))
                    svPtr.ierr = parseDaf(svPtr)
                    if (svPtr.ierr != 0) {
                        lab = ILblI.POST_COND_ERROR
                        continue@machine
                    }
                    copyAtI(svPtr.v1d, 1, sliceAtI(ftmp, 0))
                    if (atoi(DafCRuntime.cstringAt(svPtr.pd1, svPtr.sdlim)) == 0) {
                        if (svPtr.xd[svPtr.xpx] == ',') {
                            svPtr.xpx++
                            lab = ILblI.LBL_LBL
                            continue@machine
                        } else {
                            lab = ILblI.NEXT_CMND
                            continue@machine
                        }
                    }
                }
                if (dosave[0] == '^') {
                    copyAtI(svPtr.v1d, 1, sliceAtI(dosave, 0))
                    dosave[0] = NUL_D
                }
                if (svPtr.v1d[1] != NUL_D) {
                    if (retz == DO.code) {
                        svPtr.iorg = 1
                        shold = svPtr.pd1len.toInt()
                        if (shold > 0) {
                            val swpNewI = CharArray(shold)
                            System.arraycopy(svPtr.pd1, 1, swpNewI, 0, shold)
                            InterpretState.swp[swapextent] = swpNewI
                        } else {
                            // the malloc(0) NULL arm quantizes to a
                            // non-null empty carrier (H-001 note)
                            InterpretState.swp[swapextent] = CharArray(0)
                        }
                        shold = swapextent
                        swapextent++
                        if (swapextent >= SWAP_I) {
                            lab = ILblI.STACK_OVERFLOW
                            continue@machine
                        }
                    }
                    // extract file name from line - avoids args
                    var iFileI = 0
                    val ftmp1I = CharArray(512) // limit on file name size
                    while (iFileI < 511 && isprintI(svPtr.v1d[iFileI + 2])) {
                        ftmp1I[iFileI] = svPtr.v1d[iFileI + 2]
                        iFileI++
                    }
                    ftmp1I[iFileI] = NUL_D
                    /* load file */
                    readfD(ftmp1I, svPtr.iorg.toShort(), svPtr)
                    if (ftmp1I[0] == NUL_D) {
                        lab = ILblI.FILE_ERROR
                        continue@machine
                    }
                    // remove file name leaving args if any
                    val xI = 1
                    val yI = iFileI + 2
                    var xIdxI = xI
                    var yIdxI = yI
                    while (svPtr.v1d[yIdxI] != NUL_D) {
                        svPtr.v1d[xIdxI] = svPtr.v1d[yIdxI]
                        xIdxI++
                        yIdxI++
                    }
                    svPtr.v1d[xIdxI] = NUL_D
                    // processed args: copy already processed args
                    copyAtI(svPtr.args, 0, sliceAtI(svPtr.v1d, 2))
                    svPtr.LineNumber = 0
                    lblx = 0
                    if (svPtr.v1d[2] == NUL_D) {
                        lab = ILblI.FILE_ERROR
                        continue@machine
                    }
                }
                var ibrFoundI = false
                for (ixLbI in 0 until lblx) {
                    if (strcmpI(sliceAtI(lbls[ixLbI], 0), dosave) == 0) {
                        ibr = lbloff[ixLbI]
                        ibrFoundI = true
                        break
                    }
                }
                if (ibrFoundI) {
                    lab = ILblI.OFFSET
                    continue@machine
                }
                // search for label in code
                ibr = 1
                if (dosave[0] != NUL_D) {
                    var searchDoneI = false
                    while (!searchDoneI && ibr < svPtr.pd1len.toInt()) {
                        if (svPtr.pd1[ibr] == TAB_D || svPtr.pd1[ibr] == ' ' ||
                                svPtr.pd1[ibr] == '#' || svPtr.pd1[ibr] == ';') { // no label
                            while (svPtr.pd1[ibr] != NUL_D && ibr < svPtr.pd1len.toInt()) ibr++
                            if (svPtr.pd1[ibr] == NUL_D && ibr < svPtr.pd1len.toInt()) ibr++
                        } else { // something in column 1
                            j = 0
                            while (svPtr.pd1[ibr] == dosave[j] && dosave[j] != NUL_D) {
                                // compare dosave label with text
                                j++
                                ibr++
                            }
                            if ((svPtr.pd1[ibr] == TAB_D ||
                                        svPtr.pd1[ibr] == ' ' ||
                                        svPtr.pd1[ibr] == '(' ||
                                        svPtr.pd1[ibr] == NUL_D) && dosave[j] == NUL_D) {
                                // label found
                                if (lblx < LBLS_I - 1 && j < 10 && svPtr.pd1[ibr] != '(') {
                                    if (lblx < 20) { // wident guard note
                                        lblx++
                                        copyAtI(lbls[lblx], 0, sliceAtI(dosave, 0))
                                        lbloff[lblx] = ibr
                                    }
                                }
                                lab = ILblI.OFFSET
                                searchDoneI = true
                                continue
                            }
                            while (svPtr.pd1[ibr] != NUL_D && ibr < svPtr.pd1len.toInt()) ibr++
                            if (svPtr.pd1[ibr] == NUL_D && ibr < svPtr.pd1len.toInt()) ibr++
                        }
                    }
                    if (!searchDoneI) {
                        lab = ILblI.LBL_ERROR
                        continue@machine
                    }
                } else {
                    // not do xxx^yyy but may be do xxx^yy(123)
                    while (svPtr.pd1[ibr] != ' ' &&
                            svPtr.pd1[ibr] != TAB_D &&
                            svPtr.pd1[ibr] != NUL_D &&
                            svPtr.pd1[ibr] != '(') ibr++
                }
                lab = ILblI.OFFSET
                continue@machine
            }

            ILblI.OFFSET -> {
                // pass arguments to subroutine
                if (svPtr.pd1[ibr] == '(') {
                    val varI = CharArray(256)
                    var vjI = ibr + 1
                    var viI: Int
                    val argoffsI = IntArray(10) { 0 }
                    var nargsI = 0
                    var countI = 0
                    svPtr.exec++ // subroutine call level
                    // advance symbol table level
                    svPtr._Sym++
                    svPtr.start[svPtr._Sym] = 0 // the C NULL sentinel folds to 0 in the Int domain
                    SymInc = 1 // flag prevents inc again
                    viI = 0
                    while (svPtr.args[viI] != NUL_D) {
                        if (svPtr.args[viI] == CodedClose.toChar() ||
                                svPtr.args[viI] == CodedComma.toChar()) {
                            svPtr.args[viI] = NUL_D
                            nargsI++
                            argoffsI[nargsI] = viI + 1
                        }
                        viI++
                    }
                    viI = 0
                    var argDoneI = false
                    while (!argDoneI) {
                        if (svPtr.pd1[vjI] == ' ' || svPtr.pd1[vjI] == NUL_D) {
                            argDoneI = true
                            continue
                        }
                        if (svPtr.pd1[vjI] == NUL_D) {
                            lab = ILblI.LBL_ERROR
                            continue@machine
                        }
                        if (svPtr.pd1[vjI] == ',' || svPtr.pd1[vjI] == ')') {
                            varI[viI] = NUL_D
                            if (countI >= nargsI) {
                                // arg count mismatch - pass zeros
                                varI[viI] = NUL_D
                            } else {
                                varI[viI] = NUL_D
                                // code 100 stores at this level ->
                                // parameter
                                var symflgI = 100
                                var tI: CharArray
                                val argFirstI = argoffsI[countI]
                                if (svPtr.args[argFirstI] == '.') {
                                    copyAtI(svPtr.reference, 0,
                                        sliceAtI(svPtr.args, argFirstI + 1))
                                    val tmpSymI = CharArray(STR_MAX_DAF)
                                    if (symSeatI(SYM_RETRIEVE_I,
                                            sliceAtI(svPtr.args, argFirstI + 1),
                                            tmpSymI, svPtr) == null) {
                                        System.out.print("*** Var: " +
                                            DafCRuntime.cstringAt(svPtr.args, argFirstI + 1) + "\n")
                                        lab = ILblI.VAR_NOT_FOUND
                                        continue@machine
                                    }
                                    tI = tmpSymI
                                    symflgI = 200
                                } else {
                                    tI = sliceAtI(svPtr.args, argFirstI)
                                }
                                symSeatI(symflgI, sliceAtI(varI, 0), tI, svPtr)
                            }
                            viI = 0
                            vjI++
                            countI++
                            if (svPtr.pd1[vjI] == ')') break
                            continue
                        }
                        varI[viI] = svPtr.pd1[vjI]
                        viI++
                        vjI++
                    }
                    // end argument passing section
                }
                if (k > 0) { // offset from label
                    i = 0
                    while (i < k) {
                        while (svPtr.pd1[ibr] != NUL_D && ibr < svPtr.pd1len.toInt()) ibr++
                        if (svPtr.pd1[ibr] == NUL_D && ibr < svPtr.pd1len.toInt()) ibr++
                        if (ibr >= svPtr.pd1len.toInt()) {
                            lab = ILblI.LBL_ERROR
                            continue@machine
                        }
                        i++
                    }
                    while (svPtr.pd1[ibr] != TAB_D && svPtr.pd1[ibr] != ' ') ibr++
                    if (ibr >= svPtr.pd1len.toInt()) {
                        lab = ILblI.LBL_ERROR
                        continue@machine
                    }
                } else if (k == 0) ibr = pd1cur
                // else pd1cur = ibr (the commented-out else drops)
                if (svPtr.xd[svPtr.xpx] != NUL_D && svPtr.xd[svPtr.xpx] != ' ') svPtr.xpx++
                if (retz == GOTO.code) {
                    lab = ILblI.GOTO_RETURN
                } else {
                    lab = ILblI.DO_RET
                }
                continue@machine
            }

            ILblI.ERR_CALL -> {
                ernbr = 0
                if (svPtr.NOERR != 0) {
                    lab = ILblI.ABRT1
                    continue@machine
                }
                System.err.print("\n*** " + ernbr + " Control-C\n")
                lab = ILblI.ABRT1
                continue@machine
            }

            ILblI.PARSE_ERR -> {
                ernbr = svPtr.ierr
                prnterrDaf(ernbr, svPtr, pd1cur)
                if (svPtr.NOERR != 0) {
                    svPtr.NOERR = -1
                    lab = ILblI.NEXT_CMND
                    continue@machine
                }
                lab = ILblI.ABRT1
                continue@machine
            }

            ILblI.POST_COND_ERROR -> {
                ernbr = 6
                prnterrDaf(ernbr, svPtr, pd1cur)
                lab = ILblI.ABRT1
                continue@machine
            }

            ILblI.LBL_ERROR -> {
                ernbr = 8
                prnterrDaf(ernbr, svPtr, pd1cur)
                System.out.print(" " + DafCRuntime.cstring(dosave) + "\n")
                lab = ILblI.ABRT
                continue@machine
            }

            ILblI.VAR_NOT_FOUND -> {
                ernbr = 17
                prnterrDaf(ernbr, svPtr, pd1cur)
                lab = ILblI.ABRT
                continue@machine
            }

            ILblI.CMD_DEFAULT -> {
                ernbr = 13
                prnterrDaf(ernbr, svPtr, pd1cur)
                lab = ILblI.ABRT1
                continue@machine
            }

            ILblI.ARG_LIST_ERROR -> {
                ernbr = 14
                prnterrDaf(ernbr, svPtr, pd1cur)
                lab = ILblI.ABRT1
                continue@machine
            }

            ILblI.REF_ERR -> {
                // dead target (H-013 note): nothing jumps here in
                // the vendor body
                ernbr = 18
                prnterrDaf(ernbr, svPtr, pd1cur)
                lab = ILblI.ABRT1
                continue@machine
            }

            ILblI.SYMTAB_ERR -> {
                // dead target (H-013 note)
                ernbr = 23
                prnterrDaf(ernbr, svPtr, pd1cur)
                lab = ILblI.ABRT1
                continue@machine
            }

            ILblI.FILE_ERROR -> {
                ernbr = 26
                prnterrDaf(ernbr, svPtr, pd1cur)
                lab = ILblI.ABRT1
                continue@machine
            }

            ILblI.IO_ERROR -> {
                ernbr = 20
                prnterrDaf(ernbr, svPtr, pd1cur)
                lab = ILblI.ABRT1
                continue@machine
            }

            ILblI.PROGRAM_SIZE -> {
                // dead target (H-013 note)
                ernbr = 31
                prnterrDaf(ernbr, svPtr, pd1cur)
                lab = ILblI.ABRT1
                continue@machine
            }

            ILblI.STACK_OVERFLOW -> {
                ernbr = 32
                prnterrDaf(ernbr, svPtr, pd1cur)
                lab = ILblI.ABRT1
                continue@machine
            }

            ILblI.SQL_ERROR -> {
                // dead target (H-013 note)
                ernbr = 33
                prnterrDaf(ernbr, svPtr, pd1cur)
                lab = ILblI.ABRT1
                continue@machine
            }

            ILblI.USE_NOT_OPEN -> {
                ernbr = 36
                prnterrDaf(ernbr, svPtr, pd1cur)
                lab = ILblI.ABRT1
                continue@machine
            }

            ILblI.ABRT1 -> {
                // if (!svPtr->NOERR) printf ("*** %s\n", &svPtr->pd1[pd1Last]);
                // the vendor's commented-out printf drops
                lab = ILblI.ABRT
                continue@machine
            }

            ILblI.RESTORE -> {
                var rI = swapextent
                while (rI < SWAP_I) {
                    if (InterpretState.swp[rI] != null) {
                        InterpretState.swp[rI] = null // free
                    }
                    rI++
                }
                return 0
            }
        }
    }
}

// --- seated raises and the sym shape adapter of this render ---

// sym_(opcode, name, rslt, svPtr): the DOC Stubs.sym_ shape of the
// 16 body call sites; the DAF gate seat symRetDaf flattens the C
// (char*, int) name-offset pair onto one CharArray plus an Int
// offset - the per-file adapter pins the 0/0 offsets (every body
// call passes an offset-0 carrier; the null return rides through).
private fun symSeatI(opcode: Int, name: CharArray, rslt: CharArray,
        sv: DafMsv): String? =
    symRetDaf(opcode, name, 0, rslt, 0, sv)

// void inout(char *text, int cmnd, int col, struct MSV *); the
// 4-arg sibling - owner interp-parse.cpp::inout, roster b10; the
// DAF WriteCode.kt inoutDaf names the b2 3-arg twin
// (compiler-sys1.c::inout at 292).
// inoutInterpDaf SEAT RELEASED (unit 285, batch 10): the real
// 4-arg body lands at InterpParseInout.kt (roster row 285,
// interp-parse.cpp::inout; the raise is deleted with this release
// note; the three machine call sites above stay verbatim).

// int _SymFree(int, struct MSV *); SEAT RELEASED (R1
// stub-completion 2026-11-27, tranche 3): the real body lands at
// SymFree.kt (interp-sym.cpp::_SymFree, roster b1 row 5; same
// name and signature; the two call sites above resolve there).

// int Merge(unsigned char *, unsigned char *, struct MSV *);
// owner interp-sysfunc.cpp::Merge, units.csv roster b8 row 226
// (not drawn by the DAF arm; the DOC arm renders it as unit 246).
// mergeDaf RELEASED at unit 237 (B8): real body at Merge.kt (same
// name and signature; the stub deletion is the release note).

// --- private CstringOps-family helper copies of this file ---
// (H-017 same-name private fun precedent; the gate DafCRuntime
// carries cstring/cstringAt only)

private fun strlenCI(buf: CharArray): Int {
    var i = 0
    while (i < buf.size && buf[i] != NUL_D) i++
    return i
}

private fun strcpyCI(dst: CharArray, src: CharArray) {
    var i = 0
    while (i < src.size && src[i] != NUL_D) {
        dst[i] = src[i]
        i++
    }
    dst[i] = NUL_D
}

// strcpy(&dst[offset], src) CharArray form
private fun copyAtI(dst: CharArray, offset: Int, src: CharArray) {
    var i = 0
    while (i < src.size && src[i] != NUL_D) {
        dst[offset + i] = src[i]
        i++
    }
    if (offset + i < dst.size) dst[offset + i] = NUL_D
}

// strcpy(&dst[offset], src) String form
private fun copyAtSI(dst: CharArray, offset: Int, src: String) {
    var i = 0
    while (i < src.length) {
        dst[offset + i] = src[i]
        i++
    }
    dst[offset + i] = NUL_D
}

// strcat(&dst[offset], src) CharArray form
private fun catAtI(dst: CharArray, offset: Int, src: CharArray) {
    var i = offset
    while (i < dst.size && dst[i] != NUL_D) i++
    var j = 0
    while (j < src.size && src[j] != NUL_D && i < dst.size) {
        dst[i] = src[j]
        i++
        j++
    }
    if (i < dst.size) dst[i] = NUL_D
}

// strcat(&dst[offset], ch): append one char
private fun catAtI(dst: CharArray, offset: Int, ch: Char) {
    var i = offset
    while (i < dst.size && dst[i] != NUL_D) i++
    if (i < dst.size) {
        dst[i] = ch
        if (i + 1 < dst.size) dst[i + 1] = NUL_D
    }
}

// strcat(&dst[offset], src) String form
private fun catAtI(dst: CharArray, offset: Int, src: String) {
    var i = offset
    while (i < dst.size && dst[i] != NUL_D) i++
    var j = 0
    while (j < src.length && i < dst.size) {
        dst[i] = src[j]
        i++
        j++
    }
    if (i < dst.size) dst[i] = NUL_D
}

// strncmp(&buf[offset], s, n)
private fun strncmpAtI(buf: CharArray, offset: Int, s: String, n: Int): Int {
    var i = 0
    while (i < n) {
        val ca = buf[offset + i]
        val cb = if (i < s.length) s[i] else NUL_D
        if (ca != cb) return if (ca < cb) -1 else 1
        if (ca == NUL_D) return 0
        i++
    }
    return 0
}

// --- per-file const pins (the InterpHMisc unit-14 ledger: the
// interp.h command-char lattice was ledgered for THIS render, not
// pinned then; values per interp.h:35-59 and Carriers) ---

private const val BREAK: Char = 'b'
private const val CMMNT: Char = ';'
private const val DATAKILL: Char = 'k'
private const val DO: Char = 'd'
private const val ELSE: Char = 'e'
private const val FCN: Char = '$'
private const val FILECLOSE: Char = 'c'
private const val FILELOCK: Char = 'l'
private const val FILEOPEN: Char = 'o'
private const val FOR: Char = 'f'
private const val GOTO: Char = 'g'
private const val HALT: Char = 'h'
private const val HTML: Char = '<'
private const val IF: Char = 'i'
private const val JOB: Char = 'j'
private const val QUIT: Char = 'q'
private const val READ: Char = 'r'
private const val SET: Char = 's'
private const val MERGE: Char = 'm'
private const val USE: Char = 'u'
private const val WRITE: Char = 'w'
private const val XECUTE: Char = 'x'
private const val ZCMDS: Char = 'z'
private const val NEW: Char = 'n'
private const val PS = 32000 // pd1 program-space bound (DOC CompilerCarriers PS)
private const val PS1 = 32001 // PD1 space headroom
private const val CHAR_1: Char = '\u0001' // Carriers.kt CHAR_1
private const val CodedClose = 207 // Carriers.kt CodedClose
private const val CodedComma = 208 // Carriers.kt CodedComma

// ---- raises of this render ----

// void prnterr(int, struct MSV *, int); interpreter parse-error
// printer (the InterpHMisc unit-14 ledger names it: an interpreter
// service with no corpus unit; the seat raises at the unit-185
// session when called - this file).
fun prnterrDaf(ernbr: Int, svPtr: DafMsv, pd1cur: Int): Unit =
    throw NotImplementedError(
        "prnterr: out-of-scope owner (interpreter parse-error printer, no corpus unit)")

// int DirectRead(int, char *); SEAT RELEASED (R1 stub-completion
// 2026-11-27, tranche 3): the real body lands at DirectRead.kt
// (interp-sysfunc.cpp::DirectRead, roster b1 row 9; same name and
// signature; the call sites above resolve there).

// int getstr2(short, char *, struct MSV *); SEAT RELEASED (R1
// stub-completion 2026-11-27, tranche 4): the real body lands at
// Getstr2.kt (interp-parse.cpp::getstr2, roster row 47; same
// name and signature; the call sites above resolve there).
