package doc

// Unit 185 (batch-6 big three): interp-interp.cpp::Interpret (batch 6
// pending; complex stratum; physLOC 1581, score 2921.7, rank 308, the
// biggest single unit of the arm). The interpreter execution
// nucleus: program-area init, the line/command decode ladder, the
// command switch (comment/break/lock/read/open/z/if/write/job/goto/
// merge/set/quit/xecute/for/halt/else/do/new/kill/close/use), the
// gosub/for stack, label resolution, argument passing, and the
// error-exit family. Resolves the Stubs.Interpret seat (TAG
// CORRECTION there: the old tag read "not a corpus unit"; the
// roster row is units.csv 185).
//
// TU-LEVEL BUILD-DEFINE NOTE (H-018 analog): the vendor TT defines
// SQLITE in-file (interp-interp.cpp:60), so the SQLITE arms are
// live everywhere (the btree OPEN startup connect, the JOB-arm
// sql(CLOSE)). NATIVE_SHARED is NOT defined in the reference
// Makefile, so PulseCount and the Mglobal(PULSE) deadlock arms
// drop per H-018 with this note; TERMINATE_ON_ERROR is NOT
// defined either, so the abrt: label renders the #else return 99
// (the #ifdef arm drops with this note).
//
// H-011 (hit-adapt consult 2026-11-21T18:03Z): the ~64 goto labels
// render as the ILblI enum state machine (label per goto target,
// one when-arm per vendor label, one continue@machine per goto,
// plain return per C return). The vendor switch(icmnd) command
// cases map to CMD_* machine entries set by a post-decode
// when(icmnd); the case JOB fall-through into case GOTO renders as
// the lab = CMD_GOTO transition (the C fall-through enters the
// GOTO body at its top); the case MERGE arm is the set: label
// entry with MergeFlag=1 (lab = SET_LBL). Labels that also serve
// as loop-back entries rename per machine (read_command ->
// CMD_READ, writeproc -> CMD_WRITE, quit -> QUIT_LBL, xecute ->
// CMD_XECUTE, openf -> CMD_FILEOPEN, kill_again -> KILL_AGAIN,
// new_again -> NEW_AGAIN, do/d entry -> CMD_DO, hang -> HANG_LBL,
// lbl -> LBL_LBL, store -> STORE_I, set -> SET_LBL).
//
// H-004: the vendor statics render per liveness. The static
// char *swp[SWAP] swap-file arena persists across Interpret calls
// (restart frees it late) so it rides the private InterpretState
// object with the srand seed. The static to/from pointer pair of
// the line-copy idiom is assigned fresh before every use (no
// cross-call state) and renders as Int index locals per the
// liveness rule. The dead-target error labels ref_err/symtab_err/
// program_size/sql_error keep verbatim machine arms with nothing
// jumping to them (grep-verified in the vendor; H-013 dead-target
// note). The HyperTalk note below on dead locals covers the rest.
//
// H-012: the C short scalars widen to Int (the whole-arm widening
// precedent); pd1len stays the Msv Long seat with toInt() at the
// comparison sites. The Msv in_file/out_file NULL pattern maps the
// C FILE* arms: fopen renders as java.io.FileReader/FileWriter
// construction in try/catch with the NULL arm quantized to the
// catch (the readf_ unit-115 H-001 precedent), fclose to
// .close(), and popen("r"/"w") to the file-private Process
// carriers (Runtime exec sh -c, the DirectRead/unit-170 area
// precedent). getpid/sleep/fflush/_exit/setlocale/srand/time are
// the file-private OS quantizations below (H-001); the to/from
// pointer copy loop renders as the index walk (pd1 indices, no
// pointer carriers). The C pointer-into-buffer callee arguments
// (&pd1[sdlim] to getstr2/TimeOut_getstr1, &v1d[1] etc to sym_)
// render as scratch-copy + copy-back at the call site with this
// note; the printf family maps to System.out (H-001).
//
// H-014: the out-of-scope C seats (keyfix, Mltoa, sym_, add,
// TimeOut_getstr1, BuildGlobal/BuildLocal widened here for the
// stack-cleaner NULL call, the NEW seat inout, the NEW seat Merge,
// Stubs.sql, Stubs.sigint, Mglobal family codes) ride the Stubs
// object or the landed reals called directly (parse_, prnterr,
// getstr2, readf_, TimeOut_getstr1, directRead, fcn, Mglobal,
// btree, MakeLock/cleanLocks, numcomp, atoi, ErrorMessage not
// needed).
//
// WIDENT LEDGER (MDH arithmetic ledger fact, no MDH code read or
// copied): the vendor declares lbloff[20] against lblx bounded by
// LBLS-1 = 79 (the search loop caches labels at lblx up to LBLS-1)
// - the under-sized wident is kept verbatim here as IntArray(20)
// with this note (C would overflow; the render keeps the vendor
// bound and lets Kotlin throw, the made-faithful choice). The
// same-family quirk of the vendor's local shadow (int pd1cur =
// pd1Last + svPtr->xpx - 2;) inside the shell/halt arms is kept
// as the arm-local shadow pd1curShell.
//
// locals dropped dead with H-013 notes (grep-verified in the
// vendor body): ilong/jlong (declared, never read), cod206[2]/
// cod207/cod208/cod209/cod210 (declared, never referenced; the
// OPEN_PAREN/CodedClose/CodedComma CONSTS stay in use), XTAB[2]
// (only the JOB arm resets it in place before passing "\t" - the
// render passes the literal "\t" String with this note), the
// malloc-NULL OOM arms of the NEW/KILL except-lists (allocation
// failure is not observable in Kotlin), the int getche/sigc dead
// prototypes, the short getstr2/readf_/inout local prototypes
// (they exist in this TT's body only as forward declarations;
// the renders bind at the call sites), the Lf FILELOCK local
// rides the arm (live), and merge etc ride arm scopes.
//
// H-017: the machine enum names ILblI (the B/Z initials collide:
// unit 183 ZLblB exists at package scope under privacy); naming
// file-private carriers with the I suffix for the same reason.

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
    // static char *swp[SWAP] (the DO-swap arena; restart frees it late)
    val swp = arrayOfNulls<CharArray>(SWAP_I)

    // srand seed store (vendor srand(time(NULL)) init; rand never
    // called in this TU, grep-verified)
    var srandSeedI = 0
}

// vendor struct nmes carrier of the NEW/KILL except lists (the
// stateVector nstart opaque seat; Fcn.kt copies it opaquely)
private class NmesI(val name: String, val next: NmesI?)

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
// fresh NUL-terminated CharArray copy (scratch-copy, H-012 note)
// strcmp(3) over C-string char arrays: walks both to the first
// mismatch or shared NUL and returns the char-code difference
// (negative, zero, or positive), matching libc's contract.
private fun strcmpI(a: CharArray, b: CharArray): Int {
    var i = 0
    while (a[i] != NUL && a[i] == b[i]) i++
    return a[i] - b[i]
}

private fun sliceAtI(buf: CharArray, off: Int): CharArray {
    var n = 0
    while (buf[off + n] != NUL) n++
    val out = CharArray(n + 1)
    System.arraycopy(buf, off, out, 0, n)
    out[n] = NUL
    return out
}

fun Interpret(parm1: String, svPtr: Msv): Int {
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
    var dirflg: Char = NUL
    val gosub = Array(GOSUBMAX_I) { IntArray(9) }
    val cmdstack = CharArray(CMDSTACK_MAX_I)
    val lbls = Array(LBLS_I) { CharArray(20) }
    val lbloff = IntArray(20) // wident ledger note in the file header
    val ExecLevNew = IntArray(EXECLEVS_I)
    val forv = Array(FORMAX_I) { CharArray(256) }
    val forit = CharArray(FORMAX_I)
    val forlt = CharArray(FORMAX_I)
    val ftmp = CharArray(STR_MAX)
    val forin = Array(FORMAX_I) { CharArray(STR_MAX) }
    val forlv = Array(FORMAX_I) { CharArray(256) }
    val dosave = CharArray(STR_MAX)
    val tmp2 = CharArray(STR_MAX)
    val tmp3 = CharArray(STR_MAX)
    val vd = CharArray(STR_MAX)
    val MergeRHS = CharArray(STR_MAX)
    val zeroArrI = charArrayOf('0', NUL) // numcomp "0" literal seat
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
    svPtr.pd1len = 0L
    srandI(System.currentTimeMillis().toInt()) // srand (time (NULL)), H-001 note
    svPtr.bd[0] = NUL
    svPtr.bd[1] = NUL
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
    btree(OPEN_MGI, keyI, bdI, svPtr)

    // set trap banner: environment re-entry points
    pd1cur = 1
    svPtr.pd1[(svPtr.pd1len + 1).toInt()] = NUL
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
                CstringOps.copyAtS(svPtr.pd1, 2, parm1)
                svPtr.pd1[1] = TAB
                svPtr.pd1len = (CstringOps.cstringAt(svPtr.pd1, 1).length + 1).toLong()
                BreakFlag = 0
                pd1cur = 1
                cmdx = 0
                svPtr.setpiece = 0
                svPtr.LineNumber = 0L
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
                if (pd1cur > svPtr.pd1len.toInt()) {
                    svPtr.xd[svPtr.xpx] = NUL
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
                    while (svPtr.pd1[pd1cur] != NUL) pd1cur++
                    pd1cur++
                    lab = ILblI.NEXT_LINE
                    continue@machine
                }
                if (svPtr.pd1[pd1cur] == NUL) {
                    pd1cur++
                    lab = ILblI.NEXT_LINE
                    continue@machine
                }
                i = pd1cur // remember starting place
                // skip label: find 1st blank or TAB
                while (svPtr.pd1[pd1cur] != NUL && svPtr.pd1[pd1cur] != TAB &&
                        svPtr.pd1[pd1cur] != ' ') pd1cur++
                // skip multiple TABs
                while (svPtr.pd1[pd1cur] == TAB || svPtr.pd1[pd1cur] == ' ') pd1cur++
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
                        if (CstringOps.strncmpAt(svPtr.pd1, pd1cur, ". ", 2) == 0) {
                            if (ExecLevel != 2) {
                                while (svPtr.pd1[pd1cur] != NUL) pd1cur++
                                pd1cur++
                                lab = ILblI.NEXT_LINE
                                continue@machine
                            }
                            pd1cur += 2
                        } else if (CstringOps.strncmpAt(svPtr.pd1, pd1cur, ".. ", 3) == 0) {
                            if (ExecLevel != 3) {
                                while (svPtr.pd1[pd1cur] != NUL) pd1cur++
                                pd1cur++
                                lab = ILblI.NEXT_LINE
                                continue@machine
                            }
                            pd1cur += 3
                        } else if (CstringOps.strncmpAt(svPtr.pd1, pd1cur, "... ", 4) == 0) {
                            if (ExecLevel != 4) {
                                while (svPtr.pd1[pd1cur] != NUL) pd1cur++
                                pd1cur++
                                lab = ILblI.NEXT_LINE
                                continue@machine
                            }
                            pd1cur += 4
                        } else if (CstringOps.strncmpAt(svPtr.pd1, pd1cur, ".... ", 5) == 0) {
                            if (ExecLevel != 5) {
                                while (svPtr.pd1[pd1cur] != NUL) pd1cur++
                                pd1cur++
                                lab = ILblI.NEXT_LINE
                                continue@machine
                            }
                            pd1cur += 5
                        } else if (CstringOps.strncmpAt(svPtr.pd1, pd1cur, "..... ", 6) == 0) {
                            if (ExecLevel != 6) {
                                while (svPtr.pd1[pd1cur] != NUL) pd1cur++
                                pd1cur++
                                lab = ILblI.NEXT_LINE
                                continue@machine
                            }
                            pd1cur += 6
                        } else if (CstringOps.strncmpAt(svPtr.pd1, pd1cur, "...... ", 7) == 0) {
                            if (ExecLevel != 7) {
                                while (svPtr.pd1[pd1cur] != NUL) pd1cur++
                                pd1cur++
                                lab = ILblI.NEXT_LINE
                                continue@machine
                            }
                            pd1cur += 7
                        } else if (CstringOps.strncmpAt(svPtr.pd1, pd1cur, "....... ", 8) == 0) {
                            if (ExecLevel != 8) {
                                while (svPtr.pd1[pd1cur] != NUL) pd1cur++
                                pd1cur++
                                lab = ILblI.NEXT_LINE
                                continue@machine
                            }
                            pd1cur += 8
                        } else if (CstringOps.strncmpAt(svPtr.pd1, pd1cur, "........ ", 9) == 0) {
                            if (ExecLevel != 9) {
                                while (svPtr.pd1[pd1cur] != NUL) pd1cur++
                                pd1cur++
                                lab = ILblI.NEXT_LINE
                                continue@machine
                            }
                            pd1cur += 9
                        } else if (CstringOps.strncmpAt(svPtr.pd1, pd1cur, "......... ", 10) == 0) {
                            if (ExecLevel != 10) {
                                while (svPtr.pd1[pd1cur] != NUL) pd1cur++
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
                    if (chI == NUL) break
                }
                pd1cur = fromI
                lab = ILblI.NEXT_CMND
                continue@machine
            }

            ILblI.NEXT_CMND -> {
                // stack cleaners
                Stubs.BuildLocal(-1, 0, null, null, null)
                Stubs.BuildGlobal(-1, 0, null, null, null)
                var brokeI = false
                while (true) {
                    if (svPtr.xd[svPtr.xpx] == NUL || svPtr.xpx >= STR_MAX) {
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
                    if (svPtr.xd[svPtr.xpx] != ' ' && svPtr.xd[svPtr.xpx] != TAB) {
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
                        svPtr.ierr = parse_(svPtr)
                        if (svPtr.ierr != 0) {
                            lab = ILblI.PARSE_ERR
                            continue@machine
                        }
                        if (atoi(CstringOps.cstringAt(svPtr.pd1, svPtr.sdlim)) == 0) {
                            var pcDone = false
                            while (!pcDone) {
                                val curI = svPtr.xd[svPtr.xpx]
                                svPtr.xpx++
                                if (curI == NUL) pcDone = true
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
                    if (svPtr.xd[svPtr.xpx] != NUL) svPtr.xpx++
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
                while (svPtr.xd[svPtr.xpx] != '"' && svPtr.xd[svPtr.xpx] != NUL) svPtr.xpx++
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
                    if (curI == NUL) pcDone = true
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
                while (svPtr.xd[svPtr.xpx] != NUL) {
                    svPtr.xpx++
                    if (svPtr.xd[svPtr.xpx - 1] == NUL) break
                }
                svPtr.xpx = STR_MAX
                lab = ILblI.NEXT_CMND
                continue@machine
            }

            ILblI.CMD_FCN -> {
                // direct function execution
                svPtr.xpx = p2px
                svPtr.t0px = 0
                svPtr.t2 = 1
                svPtr.ierr = parse_(svPtr)
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
                if (svPtr.xd[svPtr.xpx] == ' ' || svPtr.xd[svPtr.xpx] == NUL) {
                    lab = ILblI.NEXT_CMND
                    continue@machine
                }
                var Lf: Char = ' ' // type of lock
                if (svPtr.xd[svPtr.xpx] == '-' || svPtr.xd[svPtr.xpx] == '+') {
                    Lf = svPtr.xd[svPtr.xpx]
                    svPtr.xpx++
                }
                svPtr.t0px = 1
                svPtr.ierr = parse_(svPtr)
                if (svPtr.ierr != 0) {
                    lab = ILblI.PARSE_ERR
                    continue@machine
                }
                Stubs.keyfix(sliceAtI(svPtr.v1d, 1))
                CstringOps.copyAt(tmp2, 0, sliceAtI(svPtr.v1d, 1)) // lock reference
                CstringOps.copyAtS(dosave, 0, "0") // timeout
                svPtr.tpx = 1L
                if (svPtr.xd[svPtr.xpx + 1] == ':') {
                    svPtr.t2 = 1
                    svPtr.xpx += 2
                    svPtr.ierr = parse_(svPtr)
                    if (svPtr.ierr != 0) {
                        lab = ILblI.PARSE_ERR
                        continue@machine
                    }
                    svPtr.xpx++
                    j = atoi(CstringOps.cstringAt(svPtr.pd1, svPtr.sdlim))
                    CstringOps.copyAt(dosave, 0, sliceAtI(svPtr.pd1, svPtr.sdlim)) // timeout
                    svPtr.tpx = 0L
                    lab = ILblI.NEXT_CMND
                    continue@machine
                }
                Stubs.Mltoa(getpidI(), tmp3) // process id
                if (makeLock(CstringOps.cstring(tmp2), "1", CstringOps.cstring(tmp3),
                        Lf, CstringOps.cstring(dosave)) == "1") svPtr.tpx = 1L else svPtr.tpx = 0L
                svPtr.LockDone = 1 // (the vendor double semicolon quirk)
                svPtr.xpx++
                lab = ILblI.NEXT_CMND
                continue@machine
            }

            ILblI.CMD_READ, ILblI.CONT_READ -> {
                if (lab == ILblI.CONT_READ) {
                    svPtr.xpx++
                    dirflg = NUL
                    if (svPtr.xd[svPtr.xpx] == ',') svPtr.xpx++
                    if (svPtr.xd[svPtr.xpx] == ' ' || svPtr.xd[svPtr.xpx] == NUL ||
                            svPtr.xd[svPtr.xpx] == TAB) {
                        lab = ILblI.NEXT_CMND
                        continue@machine
                    }
                }
                if (svPtr.io != 5 && svPtr.in_file[svPtr.io] == null) {
                    lab = ILblI.IO_ERROR
                    continue@machine
                }
                // NATIVE_SHARED Mglobal(PULSE) deadlock arm dead (H-018 note)
                dirflg = NUL
                if (svPtr.xd[svPtr.xpx] == '@') {
                    // indirect
                    svPtr.t0px = 1
                    svPtr.ierr = parse_(svPtr)
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
                    svPtr.ierr = parse_(svPtr)
                    if (svPtr.ierr != 0) {
                        lab = ILblI.PARSE_ERR
                        continue@machine
                    }
                    j = atoi(CstringOps.cstringAt(svPtr.pd1, svPtr.sdlim))
                    i = 3
                    lab = ILblI.START_IO
                    continue@machine
                }
                if (svPtr.xd[svPtr.xpx] == '"') { // text to be printed first
                    svPtr.t2 = 1
                    svPtr.ierr = parse_(svPtr)
                    if (svPtr.ierr != 0) {
                        lab = ILblI.PARSE_ERR
                        continue@machine
                    }
                    i = 4
                    // falls into start_io with the inout call inline
                    Stubs.inout(sliceAtI(svPtr.pd1, svPtr.sdlim), i.toShort(), j.toShort(), svPtr)
                    lab = ILblI.CONT_READ
                    continue@machine
                }
                if (svPtr.xd[svPtr.xpx] == '*') { // direct mode
                    svPtr.xpx++
                    dirflg = '1'
                }
                svPtr.t0px = 1 // extract variable name to be read
                svPtr.setname[0] = NUL
                svPtr.ierr = parse_(svPtr)
                if (svPtr.ierr != 0) {
                    lab = ILblI.PARSE_ERR
                    continue@machine
                }
                CstringOps.copyAt(vd, 1, sliceAtI(svPtr.v1d, 1))
                // timeout
                if (svPtr.xd[svPtr.xpx + 1] == ':') {
                    svPtr.t2 = 1
                    svPtr.xpx += 2
                    svPtr.ierr = parse_(svPtr)
                    if (svPtr.ierr != 0) {
                        lab = ILblI.PARSE_ERR
                        continue@machine
                    }
                    j = atoi(CstringOps.cstringAt(svPtr.pd1, svPtr.sdlim))
                    svPtr.xpx--
                    if (j <= 0) {
                        svPtr.xpx--
                        svPtr.tpx = 0L
                        lab = ILblI.CONT_READ
                        continue@machine
                    }
                    if (dirflg == NUL) {
                        val timeoutI = CharArray(j.toString().length + 1)
                        CstringOps.copyAtS(timeoutI, 0, j.toString())
                        val tAreaI = CharArray(STR_MAX) // &pd1[sdlim] scratch (H-012 note)
                        svPtr.tpx = TimeOut_getstr1(svPtr.io, tAreaI, timeoutI, 0).toLong()
                        CstringOps.copyAt(svPtr.pd1, svPtr.sdlim, tAreaI) // copy-back
                    } else {
                        val iChI = CharArray(1) // unsigned char i
                        svPtr.tpx = directRead(svPtr.io, iChI).toLong()
                        CstringOps.copyAtS(svPtr.pd1, svPtr.sdlim, (iChI[0].code).toString())
                    }
                    lab = ILblI.STORE_I
                    continue@machine
                }
                svPtr.hor[svPtr.io] = 0
                svPtr.ver[svPtr.io]++
                i = svPtr.io
                svPtr.sdlim = svPtr.pd1len.toInt() + 2
                if (dirflg == '1') {
                    val iChI = CharArray(1)
                    svPtr.tpx = directRead(svPtr.io, iChI).toLong()
                    CstringOps.copyAtS(svPtr.pd1, svPtr.sdlim, (iChI[0].code).toString())
                } else {
                    val gAreaI = CharArray(STR_MAX) // &pd1[sdlim] scratch (H-012 note)
                    svPtr.tpx = getstr2(i.toShort(), gAreaI, svPtr).toLong()
                    if (svPtr.tpx >= 0) svPtr.tpx = 1L else svPtr.tpx = 0L
                    CstringOps.copyAt(svPtr.pd1, svPtr.sdlim, gAreaI) // copy-back
                }
                lab = ILblI.STORE_I
                continue@machine
            }

            ILblI.START_IO -> {
                Stubs.inout(sliceAtI(svPtr.pd1, svPtr.sdlim), i.toShort(), j.toShort(), svPtr)
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
                svPtr.ierr = parse_(svPtr)
                if (svPtr.ierr != 0) {
                    lab = ILblI.PARSE_ERR
                    continue@machine
                }
                k = atoi(CstringOps.cstringAt(svPtr.pd1, svPtr.sdlim))
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
                svPtr.ierr = parse_(svPtr)
                if (svPtr.ierr != 0) {
                    lab = ILblI.PARSE_ERR
                    continue@machine
                }
                i = 0
                while (svPtr.pd1[svPtr.sdlim + i] != NUL && svPtr.pd1[svPtr.sdlim + i] != ',') {
                    tmp2[i] = svPtr.pd1[svPtr.sdlim + i]
                    i++
                }
                if (svPtr.pd1[svPtr.sdlim + i] != ',') {
                    i = 0
                    while (svPtr.pd1[svPtr.sdlim + i] != NUL && svPtr.pd1[svPtr.sdlim + i] != '/') {
                        tmp2[i] = svPtr.pd1[svPtr.sdlim + i]
                        i++
                    }
                }
                tmp2[i] = NUL
                i++
                val newStrI = CstringOps.cstringAt(svPtr.pd1, svPtr.sdlim + i)
                if (newStrI.equals("old", ignoreCase = true)) New = 0
                else if (newStrI.equals("new", ignoreCase = true)) New = 1
                else if (newStrI.equals("append", ignoreCase = true)) New = 2
                else {
                    lab = ILblI.ARG_LIST_ERROR
                    continue@machine
                }
                if (New == 1) { /* output new */
                    svPtr.out_file[k] = fopenWriteI(CstringOps.cstring(tmp2), false)
                    if (svPtr.out_file[k] == null) svPtr.tpx = 0L else svPtr.tpx = 1L
                    lab = ILblI.OPN1
                    continue@machine
                }
                if (New == 2) { /* output append */
                    svPtr.out_file[k] = fopenWriteI(CstringOps.cstring(tmp2), true)
                    if (svPtr.out_file[k] == null) svPtr.tpx = 0L else svPtr.tpx = 1L
                    lab = ILblI.OPN1
                    continue@machine
                }
                svPtr.in_file[k] = fopenReadI(CstringOps.cstring(tmp2))
                if (svPtr.in_file[k] == null) svPtr.tpx = 0L else svPtr.tpx = 1L
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
                    svPtr.v1d[0] = NUL
                    Mglobal(INIT_MGI, svPtr.v1d, svPtr.bd, svPtr)
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
                    svPtr.ierr = parse_(svPtr)
                    if (svPtr.ierr != 0) {
                        lab = ILblI.PARSE_ERR
                        continue@machine
                    }
                    j = atoi(CstringOps.cstringAt(svPtr.pd1, svPtr.sdlim))
                    System.out.print("\n")
                    Mglobal(CLOSE_MGI, tmp2, tmp3, svPtr) /* Close Globals */
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
                    if (svPtr.tpx != 0L) {
                        lab = ILblI.NEXT_CMND
                        continue@machine
                    }
                    svPtr.xpx = STR_MAX
                    lab = ILblI.NEXT_CMND
                    continue@machine
                }
                lab = ILblI.NEXT_IF_ARG
                continue@machine
            }

            ILblI.NEXT_IF_ARG -> {
                svPtr.t2 = 1
                svPtr.ierr = parse_(svPtr)
                if (svPtr.ierr != 0) {
                    lab = ILblI.PARSE_ERR
                    continue@machine
                }
                // (the vendor's commented-out numcomp arm drops)
                if (atoi(CstringOps.cstringAt(svPtr.pd1, svPtr.sdlim)) == 0) {
                    svPtr.tpx = 0L
                    svPtr.xpx = STR_MAX
                    lab = ILblI.NEXT_CMND
                    continue@machine
                }
                svPtr.tpx = 1L
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
                if (svPtr.tpx == 0L) {
                    lab = ILblI.NEXT_CMND
                    continue@machine
                }
                svPtr.xpx = STR_MAX
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
                    svPtr.ierr = parse_(svPtr)
                    if (svPtr.ierr != 0) {
                        lab = ILblI.PARSE_ERR
                        continue@machine
                    }
                    svPtr.pd1[svPtr.sdlim] =
                        atoi(CstringOps.cstringAt(svPtr.pd1, svPtr.sdlim)).toChar()
                    svPtr.pd1[svPtr.sdlim + 1] = NUL
                    System.out.print(svPtr.pd1[svPtr.sdlim]) // putchar
                    i = 4
                    lab = ILblI.IOSUB1
                    continue@machine
                }
                if (svPtr.xd[svPtr.xpx] == '?') { // tab
                    svPtr.t2 = 1
                    svPtr.xpx++
                    svPtr.ierr = parse_(svPtr)
                    if (svPtr.ierr != 0) {
                        lab = ILblI.PARSE_ERR
                        continue@machine
                    }
                    j = atoi(CstringOps.cstringAt(svPtr.pd1, svPtr.sdlim))
                    i = 3
                    lab = ILblI.IOSUB
                    continue@machine
                }
                svPtr.t2 = 1
                svPtr.ierr = parse_(svPtr)
                if (svPtr.ierr != 0) {
                    lab = ILblI.PARSE_ERR
                    continue@machine
                }
                i = 4
                lab = ILblI.IOSUB
                continue@machine
            }

            ILblI.IOSUB -> {
                Stubs.inout(sliceAtI(svPtr.pd1, svPtr.sdlim), i.toShort(), j.toShort(), svPtr)
                lab = ILblI.IOSUB1
                continue@machine
            }

            ILblI.IOSUB1 -> {
                if (svPtr.xd[svPtr.xpx] == ',') {
                    svPtr.xpx++
                    lab = ILblI.CMD_WRITE
                    continue@machine
                }
                if (svPtr.xd[svPtr.xpx] == NUL || svPtr.xd[svPtr.xpx] == ' ' ||
                        svPtr.xd[svPtr.xpx] == TAB) {
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
                val bufI = CharArray(STR_MAX)
                val filenameI = CharArray(256)
                var bxI = 0
                var pflagI = 0
                bufI[0] = NUL
                filenameI[0] = NUL
                if (toupperI(svPtr.xd[p2px + 1]) == 'Q') {
                    pflagI = -1 // SQL undifferentiated
                    svPtr.bd[0] = NUL
                    lab = ILblI.NEXT_LINE
                    continue@machine
                }
                // sql codes go here
                // strncasecmp(&pd1[pd1Last],"shell",5) rendered as the
                // lowercase take(5) compare
                if (CstringOps.cstringAt(svPtr.pd1, pd1Last).lowercase().take(5) != "shell") {
                    prnterr(37, svPtr, pd1cur) // unrecognized command
                    lab = ILblI.ABRT
                    continue@machine
                }
                // shell and sql command decode - comparable setups
                if (CstringOps.cstringAt(svPtr.pd1, pd1Last).lowercase().take(7) == "shell/p") pflagI = 1
                if (CstringOps.cstringAt(svPtr.pd1, pd1Last).lowercase().take(7) == "shell/g") pflagI = 2
                var pd1curShell = pd1Last + svPtr.xpx - 2 // vendor local shadow
                while (svPtr.pd1[pd1curShell] != NUL && svPtr.pd1[pd1curShell] != ' ') pd1curShell++
                if (svPtr.pd1[pd1curShell] != NUL) pd1curShell++
                var shellDone = false
                while (!shellDone) {
                    if (svPtr.pd1[pd1curShell] == NUL) {
                        shellDone = true
                        continue
                    }
                    if (svPtr.pd1[pd1curShell] == '&' && svPtr.pd1[pd1curShell + 1] == '~') {
                        pd1curShell += 2
                        i = 1
                        while (svPtr.pd1[pd1curShell] != '~' && svPtr.pd1[pd1curShell] != NUL) {
                            svPtr.xd[i] = svPtr.pd1[pd1curShell]
                            pd1curShell++
                            i++
                        }
                        svPtr.xd[i] = NUL
                        if (svPtr.pd1[pd1curShell] == '~') pd1curShell++
                        svPtr.t2 = 1
                        svPtr.xpx = 1
                        svPtr.ierr = parse_(svPtr)
                        if (svPtr.ierr != 0) {
                            lab = ILblI.PARSE_ERR
                            continue@machine
                        }
                        CstringOps.catAt(bufI, 0, sliceAtI(svPtr.pd1, svPtr.sdlim))
                        bxI = CstringOps.strlenC(bufI)
                    } else {
                        bufI[bxI] = svPtr.pd1[pd1curShell]
                        bxI++
                        bufI[bxI] = NUL
                        pd1curShell++
                    }
                }
                pd1curShell++
                bufI[bxI] = NUL
                if (pflagI == 0) { // SHELL command with no options
                    if (systemI(CstringOps.cstring(bufI)) < 0) svPtr.tpx = 0L else svPtr.tpx = 1L
                } else if (pflagI == 1) { // SHELL/P command
                    svPtr.in_file[6] = popenReadI(CstringOps.cstring(bufI))
                } else { // SHELL/G
                    svPtr.out_file[6] = popenWriteI(CstringOps.cstring(bufI))
                    if (svPtr.out_file[6] == null) svPtr.tpx = 0L else svPtr.tpx = 1L
                }
                lab = ILblI.NEXT_LINE
                continue@machine
            }

            ILblI.SET_LBL -> {
                if (svPtr.xd[svPtr.xpx] == '@') { // indirection
                    svPtr.kflg = 1
                    svPtr.t0px = 1
                    svPtr.ierr = parse_(svPtr)
                    if (svPtr.ierr != 0) {
                        lab = ILblI.PARSE_ERR
                        continue@machine
                    }
                    svPtr.kflg = 0
                }
                ftmp[0] = NUL
                if (svPtr.xd[svPtr.xpx] == '(') { // group LHS
                    svPtr.xpx++
                    vd[1] = NUL
                    lab = ILblI.S1
                    continue@machine
                } else {
                    svPtr.t0px = 1
                    svPtr.setname[0] = NUL
                    svPtr.ierr = parse_(svPtr)
                    if (svPtr.ierr != 0) {
                        lab = ILblI.PARSE_ERR
                        continue@machine
                    }
                    CstringOps.copyAt(vd, 1, sliceAtI(svPtr.v1d, 1))
                }
                svPtr.xpx += 2
                if (MergeFlag != 0) svPtr.t0px = 1 else svPtr.t0px = 0
                svPtr.t2 = 1
                svPtr.ierr = parse_(svPtr)
                if (svPtr.ierr != 0) {
                    lab = ILblI.PARSE_ERR
                    continue@machine
                }
                if (MergeFlag != 0) {
                    CstringOps.copyAt(MergeRHS, 1, sliceAtI(svPtr.v1d, 1))
                    svPtr.xpx++
                } else MergeRHS[1] = NUL
                retz = SET.code
                k = 0
                if (vd[1] != NUL) {
                    lab = ILblI.LHSREF
                    continue@machine
                }
                lab = ILblI.S2
                continue@machine
            }

            ILblI.S1 -> {
                svPtr.t0px = 1
                svPtr.ierr = parse_(svPtr)
                if (svPtr.ierr != 0) {
                    lab = ILblI.PARSE_ERR
                    continue@machine
                }
                CstringOps.catAt(ftmp, 0, sliceAtI(svPtr.v1d, 1))
                CstringOps.catAt(ftmp, 0, " ")
                svPtr.xpx++
                if (svPtr.xd[svPtr.xpx] != ')' && svPtr.xd[svPtr.xpx] != NUL) {
                    svPtr.xpx++
                    lab = ILblI.S1
                    continue@machine
                }
                // fall through to the tail of SET_LBL (the vendor
                // falls off the if/else)
                svPtr.xpx += 2
                if (MergeFlag != 0) svPtr.t0px = 1 else svPtr.t0px = 0
                svPtr.t2 = 1
                svPtr.ierr = parse_(svPtr)
                if (svPtr.ierr != 0) {
                    lab = ILblI.PARSE_ERR
                    continue@machine
                }
                if (MergeFlag != 0) {
                    CstringOps.copyAt(MergeRHS, 1, sliceAtI(svPtr.v1d, 1))
                    svPtr.xpx++
                } else MergeRHS[1] = NUL
                retz = SET.code
                k = 0
                if (vd[1] != NUL) {
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
                vd[j - 1] = NUL
                lab = ILblI.LHSREF
                continue@machine
            }

            ILblI.SET_RETURN -> {
                if (ftmp[k] != NUL) {
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
                    svPtr.pd1[gosub[gsbx][4] + 1] = NUL
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
                    svPtr.xpx = STR_MAX
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
                    svPtr.xd[1] = NUL
                } else {
                    CstringOps.copyAt(svPtr.xd, 1, sliceAtI(cmdstack, cmdx))
                }
                svPtr.pd1len = gosub[gsbx][4].toLong()
                if (forx > 0) { // patch to fix FOR after DO fixes
                    svPtr.xpx = 1
                }
                retz = gosub[gsbx][3]
                svPtr.tpx = gosub[gsbx][7].toLong()
                // evaluate return args before killing symbol table
                svPtr.args[0] = NUL
                if (retz != FOR.code && svPtr.xd[svPtr.xpx] != ' ' &&
                        svPtr.xd[svPtr.xpx] != NUL) {
                    svPtr.t2 = 1
                    svPtr.ierr = parse_(svPtr)
                    if (svPtr.ierr != 0) {
                        lab = ILblI.PARSE_ERR
                        continue@machine
                    }
                    // copy processed return arg to state vector
                    CstringOps.copyAt(svPtr.args, 0, sliceAtI(svPtr.pd1, svPtr.sdlim))
                    if (svPtr.fdes[0] >= 0) {
                        // the write/close pipe carrier is the Msv
                        // fdes pair; the JVM render skips the fd IO
                        // (H-001 note)
                        svPtr.fdes[1] = -1
                    }
                }
                if (retz != FOR.code && retz != XECUTE.code) {
                    ExecLevNew[ExecLevel] = 0 // prevent 2 trips
                    Stubs.SymFree(1, svPtr)
                }
                gsbx--
                if (forx > 0 && retz == FOR.code) {
                    lab = ILblI.FOR_END
                    continue@machine
                }
                if (retz == DO.code && ExecLevel > 1) {
                    if (ExecLevNew[ExecLevel] != 0) {
                        Stubs.SymFree(1, svPtr) /* undo New */
                    }
                    ExecLevel--
                }
                if (gosub[gsbx + 1][6] != 0) {
                    ExecLevel = gosub[gsbx + 1][6]
                    gosub[gsbx + 1][6] = 0
                }
                // old - permitted extra blank at EOL to cause loop
                if (svPtr.xd[svPtr.xpx] == NUL ||
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
                svPtr.ierr = parse_(svPtr)
                if (svPtr.ierr != 0) {
                    lab = ILblI.PARSE_ERR
                    continue@machine
                }
                ftmp[0] = TAB
                CstringOps.copyAt(ftmp, 1, sliceAtI(svPtr.pd1, svPtr.sdlim))
                if (svPtr.xd[svPtr.xpx] == ':') {
                    svPtr.xpx++
                    svPtr.t2 = 1
                    svPtr.ierr = parse_(svPtr)
                    if (svPtr.ierr != 0) {
                        lab = ILblI.PARSE_ERR
                        continue@machine
                    }
                    k = atoi(CstringOps.cstringAt(svPtr.pd1, svPtr.sdlim))
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
                l = svPtr.pd1len.toInt()
                CstringOps.copyAt(svPtr.pd1, svPtr.pd1len.toInt() + 2, ftmp)
                svPtr.pd1len = (svPtr.pd1len + 2 + CstringOps.strlenC(ftmp)).toLong()
                gsbx++
                if (gsbx >= GOSUBMAX_I) {
                    lab = ILblI.STACK_OVERFLOW
                    continue@machine
                }
                gosub[gsbx][0] = 0 /* swap origin */
                gosub[gsbx][1] = pd1cur /* restore pd1cur */
                gosub[gsbx][2] = cmdx /* command stack address */
                CstringOps.copyAt(cmdstack, cmdx, sliceAtI(svPtr.xd, svPtr.xpx))
                cmdx = cmdx + CstringOps.cstringAt(svPtr.xd, svPtr.xpx).length + 1
                gosub[gsbx][4] = l /* restore pd1len */
                gosub[gsbx][3] = XECUTE.code
                pd1cur = l + 2
                lab = ILblI.NEXT_LINE_XECUTE
                continue@machine
            }

            ILblI.XECUTE_RETURN -> {
                // return point - environment restored
                if (svPtr.xd[svPtr.xpx] == ' ' || svPtr.xd[svPtr.xpx] == NUL) {
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
                forit[forx] = NUL
                forlt[forx] = NUL
                forv[forx][0] = NUL
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
                    CstringOps.copyAtS(forv[forx], 0, "_dummy")
                    CstringOps.copyAtS(forin[forx], 0, "0")
                    CstringOps.copyAtS(forlv[forx], 0, "100")
                    svPtr.symflg = 0 /* store */
                    Stubs.sym_(0, sliceAtI(forv[forx], 0),
                        charArrayOf('1', NUL), svPtr)
                    forit[forx] = '1'
                    lab = ILblI.ARGLESS_LBL
                    continue@machine
                }
                // find local variable
                svPtr.t0px = 1
                svPtr.ierr = parse_(svPtr)
                if (svPtr.ierr != 0) {
                    lab = ILblI.PARSE_ERR
                    continue@machine
                }
                svPtr.xpx += 2
                CstringOps.copyAt(forv[forx], 0, sliceAtI(svPtr.v1d, 1))
                svPtr.symflg = 0 /* store */
                dosave[0] = NUL
                Stubs.sym_(0, sliceAtI(forv[forx], 0), dosave, svPtr)
                lab = ILblI.NXT_ARG
                continue@machine
            }

            ILblI.NXT_ARG -> {
                forit[forx] = NUL
                forlt[forx] = NUL
                svPtr.t2 = 1
                if (svPtr.xd[svPtr.xpx] == '$') { /* function */
                    forit[forx] = '1'
                    forlt[forx] = '1'
                    forlv[forx][0] = NUL // function
                    CstringOps.copyAt(forin[forx], 0, sliceAtI(svPtr.xd, svPtr.xpx))
                }
                svPtr.ierr = parse_(svPtr)
                if (svPtr.ierr != 0) {
                    lab = ILblI.PARSE_ERR
                    continue@machine
                }
                forit[forx] = NUL // initial value
                CstringOps.copyAt(ftmp, 0, sliceAtI(svPtr.pd1, svPtr.sdlim))
                if (svPtr.xd[svPtr.xpx] == ',' || svPtr.xd[svPtr.xpx] == ' ' ||
                        svPtr.xd[svPtr.xpx] == NUL) {
                    lab = ILblI.INIT_LOOP_VAR
                    continue@machine
                }
                if (svPtr.xd[svPtr.xpx] != ':') {
                    lab = ILblI.ARG_LIST_ERROR
                    continue@machine
                }
                // increment arm block
                svPtr.symflg = 0 /* store */
                Stubs.sym_(0, sliceAtI(forv[forx], 0), ftmp, svPtr)
                svPtr.t2 = 1
                svPtr.xpx++
                val xdtmpI = CharArray(STR_MAX)
                CstringOps.copyAt(xdtmpI, 0, sliceAtI(svPtr.xd, svPtr.xpx))
                var xdtmpxI = svPtr.xpx
                svPtr.ierr = parse_(svPtr)
                if (svPtr.ierr != 0) {
                    lab = ILblI.PARSE_ERR
                    continue@machine
                }
                xdtmpxI = svPtr.xpx - xdtmpxI
                xdtmpI[xdtmpxI] = NUL
                forit[forx] = '1'
                /* increment */
                CstringOps.copyAt(forin[forx], 0, sliceAtI(xdtmpI, 0))
                if (svPtr.xd[svPtr.xpx] == ',' || svPtr.xd[svPtr.xpx] == NUL ||
                        svPtr.xd[svPtr.xpx] == ' ' || svPtr.xd[svPtr.xpx] == TAB) {
                    lab = ILblI.INIT_LOOP_VAR
                    continue@machine
                }
                if (svPtr.xd[svPtr.xpx] != ':') {
                    lab = ILblI.ARG_LIST_ERROR
                    continue@machine
                }
                svPtr.t2 = 1
                svPtr.xpx++
                svPtr.ierr = parse_(svPtr)
                if (svPtr.ierr != 0) {
                    lab = ILblI.PARSE_ERR
                    continue@machine
                }
                forlt[forx] = '1' // limit
                CstringOps.copyAt(forlv[forx], 0, sliceAtI(svPtr.pd1, svPtr.sdlim))
                if (svPtr.xd[svPtr.xpx] == ',' || svPtr.xd[svPtr.xpx] == ' ' ||
                        svPtr.xd[svPtr.xpx] == NUL || svPtr.xd[svPtr.xpx] == TAB) {
                    lab = ILblI.INIT_LOOP_VAR
                    continue@machine
                }
                lab = ILblI.ARG_LIST_ERROR
                continue@machine
            }

            ILblI.INIT_LOOP_VAR -> {
                svPtr.symflg = 0 /* store */
                // set value
                Stubs.sym_(0, sliceAtI(forv[forx], 0), ftmp, svPtr)
                if (forin[forx][0] == '$') {
                    if (CstringOps.cstring(ftmp).isEmpty()) i = 1 else i = 0
                    forit[forx] = '1'
                    lab = ILblI.ARGLESS_LBL
                    continue@machine
                } else i = 0
                if (forlt[forx] == '1') {
                    if (numcomp(forin[forx], zeroArrI) >= 0) {
                        if (numcomp(ftmp, forlv[forx]) > 0) i = 1
                    } else {
                        if (numcomp(ftmp, forlv[forx]) < 0) i = 1
                    }
                }
                lab = ILblI.ARGLESS_LBL
                continue@machine
            }

            ILblI.ARGLESS_LBL -> {
                CstringOps.copyAt(cmdstack, cmdx, sliceAtI(svPtr.xd, svPtr.xpx))
                cmdx = cmdx + CstringOps.cstringAt(svPtr.xd, svPtr.xpx).length + 1
                lab = ILblI.BYP
                continue@machine
            }

            ILblI.BYP -> {
                while (svPtr.xd[svPtr.xpx] != NUL && svPtr.xd[svPtr.xpx] != ' ') {
                    svPtr.t2 = 1
                    svPtr.xpx++
                    parse_(svPtr)
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
                val itmpI = CharArray(STR_MAX)
                svPtr.symflg = 1 /* retrieve */
                Stubs.sym_(11, sliceAtI(forv[forx], 0), itmpI, svPtr)
                val oldI = CharArray(STR_MAX)
                var oldxpxI = 0
                CstringOps.copyAt(oldI, 0, sliceAtI(svPtr.xd, 1))
                oldxpxI = svPtr.xpx
                CstringOps.copyAt(svPtr.xd, 0, sliceAtI(forin[forx], 0))
                svPtr.xpx = 0
                svPtr.t2 = 1
                svPtr.ierr = parse_(svPtr)
                if (svPtr.ierr != 0) {
                    lab = ILblI.PARSE_ERR
                    continue@machine // increment amount
                }
                // dsve = &pd1[sdlim]: the parse result slice
                val dsveI = sliceAtI(svPtr.pd1, svPtr.sdlim)
                CstringOps.copyAt(svPtr.xd, 1, sliceAtI(oldI, 0))
                svPtr.xpx = oldxpxI
                if (forin[forx][0] != '$') Stubs.add(itmpI, dsveI, ftmp)
                else CstringOps.copyAt(ftmp, 0, sliceAtI(dsveI, 0))
                if (BreakFlag != 0) {
                    BreakFlag = 0
                    lab = ILblI.FOR_QUIT
                    continue@machine
                }
                if (forlt[forx] != '1') {
                    svPtr.symflg = 0 /* store */
                    Stubs.sym_(0, sliceAtI(forv[forx], 0), ftmp, svPtr)
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
                Stubs.sym_(0, sliceAtI(forv[forx], 0), ftmp, svPtr)
                if (forin[forx][0] == '$') {
                    if (forlv[forx][0] == NUL) { // special case null string limit
                        if (CstringOps.cstring(ftmp).isEmpty()) {
                            lab = ILblI.FOR_QUIT
                            continue@machine
                        }
                    } else {
                        if (strcmpI(ftmp, forlv[forx]) >= 0) {
                            lab = ILblI.FOR_QUIT
                            continue@machine
                        }
                    }
                } else if (numcomp(forin[forx], zeroArrI) >= 0) {
                    if (numcomp(ftmp, forlv[forx]) > 0) {
                        lab = ILblI.FOR_QUIT
                        continue@machine
                    }
                } else {
                    if (numcomp(ftmp, forlv[forx]) < 0) {
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
                svPtr.xpx = STR_MAX
                lab = ILblI.NEXT_CMND
                continue@machine
            }

            ILblI.CMD_HALT -> {
                if ((svPtr.xd[svPtr.xpx] == ' ' || svPtr.xd[svPtr.xpx] == NUL)
                        && toupperI(svPtr.xd[p2px + 1]) != 'T') {
                    Stubs.Mltoa(getpidI(), tmp2)
                    if (svPtr.LockDone != 0) cleanLocks(CstringOps.cstring(tmp2))
                    Mglobal(CLOSE_MGI, tmp2, tmp3, svPtr) /* Close Globals */
                    fflushI()
                    exitI(0) // EXIT_SUCCESS
                    return 0
                }
                if (toupperI(svPtr.xd[p2px + 1]) == 'T') { /* html command */
                    var pd1curShell = pd1Last + svPtr.xpx - 2 // vendor shadow
                    while (svPtr.pd1[pd1curShell] != NUL && svPtr.pd1[pd1curShell] != ' ') pd1curShell++
                    if (svPtr.pd1[pd1curShell] != NUL) pd1curShell++
                    var htmlDone = false
                    while (!htmlDone) {
                        if (svPtr.pd1[pd1curShell] == NUL) {
                            htmlDone = true
                            continue
                        }
                        if (svPtr.pd1[pd1curShell] == '&' && svPtr.pd1[pd1curShell + 1] == '~') {
                            pd1curShell += 2
                            i = 1
                            while (svPtr.pd1[pd1curShell] != '~' && svPtr.pd1[pd1curShell] != NUL) {
                                svPtr.xd[i] = svPtr.pd1[pd1curShell]
                                pd1curShell++
                                i++
                            }
                            svPtr.xd[i] = NUL
                            if (svPtr.pd1[pd1curShell] == '~') pd1curShell++
                            svPtr.t2 = 1
                            svPtr.xpx = 1
                            svPtr.ierr = parse_(svPtr)
                            if (svPtr.ierr != 0) {
                                lab = ILblI.PARSE_ERR
                                continue@machine
                            }
                            System.out.print(CstringOps.cstringAt(svPtr.pd1, svPtr.sdlim))
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
                svPtr.ierr = parse_(svPtr)
                if (svPtr.ierr != 0) {
                    lab = ILblI.PARSE_ERR
                    continue@machine
                }
                j = atoi(CstringOps.cstringAt(svPtr.pd1, svPtr.sdlim))
                if (j > 0) {
                    // NATIVE_SHARED Mglobal(PULSE) deadlock arm dead (H-018)
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
                    while (svPtr.xd[svPtr.xpx] != NUL) {
                        if (svPtr.xd[svPtr.xpx] != ' ') break
                        svPtr.xpx++
                    }
                    // the vendor double ierr assignment quirk kept
                    svPtr.ierr = parse_(svPtr)
                    svPtr.ierr = svPtr.ierr
                    if (svPtr.ierr != 0) {
                        lab = ILblI.PARSE_ERR
                        continue@machine
                    }
                    if (CstringOps.cstringAt(svPtr.pd1, svPtr.sdlim).length > 1000) {
                        lab = ILblI.FILE_ERROR
                        continue@machine
                    }
                    val udatI = CstringOps.cstringAt(svPtr.pd1, svPtr.sdlim)
                    CstringOps.copyAtS(svPtr.UDAT, 0, udatI + ".dat")
                    CstringOps.copyAtS(svPtr.UKEY, 0, udatI + ".key")
                    lab = ILblI.NEXT_LINE
                    continue@machine
                }
                lab = ILblI.DO_RET1
                continue@machine
            }

            ILblI.DO_RET1 -> {
                argless = 0
                NewFlag = 0
                if (svPtr.xd[svPtr.xpx] == ' ' || svPtr.xd[svPtr.xpx] == NUL) {
                    val tmpI = CharArray(1024)
                    CstringOps.copyAtS(tmpI, 0, "+0  ")
                    ExecLevel++
                    ExecLevNew[ExecLevel] = 0
                    CstringOps.catAt(tmpI, 0, sliceAtI(svPtr.xd, svPtr.xpx))
                    CstringOps.copyAt(svPtr.xd, 1, sliceAtI(tmpI, 0))
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
                CstringOps.copyAt(cmdstack, cmdx, sliceAtI(svPtr.xd, svPtr.xpx))
                cmdx = cmdx + CstringOps.cstringAt(svPtr.xd, svPtr.xpx).length + 1
                gosub[gsbx][3] = DO.code
                gosub[gsbx][4] = pd1hold1
                pd1cur = ibr
                gosub[gsbx][5] = 0
                gosub[gsbx][7] = svPtr.tpx.toInt()
                if (argless == 0) {
                    gosub[gsbx][6] = ExecLevel
                    ExecLevel = 1
                }
                if (SymInc == 0) { // flag indicates inc was already done
                    svPtr._Sym++
                    svPtr.start[svPtr._Sym] = null
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
                        svPtr.xd[svPtr.xpx] == NUL ||
                        svPtr.xd[svPtr.xpx] == TAB) {
                    /* new except none */
                    svPtr.symflg = 7
                    svPtr.nstart = null /* list freed in sym_() */
                    Stubs.sym_(7, charArrayOf(NUL), charArrayOf(NUL), svPtr)
                    svPtr.nstart = null /* list freed in sym_() */
                    ExecLevNew[ExecLevel] = 1
                    lab = ILblI.NEXT_CMND
                    continue@machine
                }
                /* New indirect */
                if (svPtr.xd[svPtr.xpx] == '@') {
                    svPtr.kflg = 1
                    svPtr.t0px = 1
                    svPtr.ierr = parse_(svPtr)
                    if (svPtr.ierr != 0) {
                        lab = ILblI.PARSE_ERR
                        continue@machine
                    }
                    svPtr.kflg = 0
                }
                /* New specific. new a,b,c */
                if (svPtr.xd[svPtr.xpx] != '(') {
                    svPtr.t0px = 1
                    svPtr.ierr = parse_(svPtr)
                    if (svPtr.ierr != 0) {
                        lab = ILblI.PARSE_ERR
                        continue@machine
                    }
                    Stubs.sym_(100, sliceAtI(svPtr.v1d, 1), charArrayOf(NUL), svPtr)
                } else {
                    /* New all locals except: linked list of var
                       names not to be newed (the malloc-NULL OOM
                       arms drop per the file-header H-013 note) */
                    svPtr.nstart = null
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
                svPtr.ierr = parse_(svPtr)
                if (svPtr.ierr != 0) {
                    lab = ILblI.PARSE_ERR
                    continue@machine
                }
                Stubs.keyfix(sliceAtI(svPtr.v1d, 1)) /* in case its an array */
                val nmI = CstringOps.cstringAt(svPtr.v1d, 1)
                svPtr.nstart = NmesI(nmI, svPtr.nstart as NmesI?)
                svPtr.xpx++
                if (svPtr.xd[svPtr.xpx] != ')' && svPtr.xd[svPtr.xpx] != NUL) {
                    svPtr.xpx++
                    lab = ILblI.NEW2
                    continue@machine
                }
                svPtr.symflg = 7
                /* new except */
                Stubs.sym_(7, charArrayOf(NUL), charArrayOf(NUL), svPtr)
                svPtr.nstart = null /* list freed in sym_() */
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
                if (svPtr.xd[svPtr.xpx] == ' ' || svPtr.xd[svPtr.xpx] == NUL ||
                        svPtr.xd[svPtr.xpx] == TAB) {
                    svPtr.symflg = 4
                    Stubs.sym_(4, charArrayOf(NUL), charArrayOf(NUL), svPtr)
                    lab = ILblI.NEXT_CMND
                    continue@machine
                }
                // Kill indirect
                if (svPtr.xd[svPtr.xpx] == '@') {
                    svPtr.kflg = 1
                    svPtr.t0px = 1
                    svPtr.ierr = parse_(svPtr)
                    if (svPtr.ierr != 0) {
                        lab = ILblI.PARSE_ERR
                        continue@machine
                    }
                    svPtr.kflg = 0
                }
                // Kill specific - local or global
                if (svPtr.xd[svPtr.xpx] != '(') {
                    svPtr.t0px = 1
                    svPtr.ierr = parse_(svPtr)
                    if (svPtr.ierr != 0) {
                        lab = ILblI.PARSE_ERR
                        continue@machine
                    }
                    if (svPtr.v1d[1] != '^') { /* kill a local */
                        svPtr.symflg = 2 /* kill */
                        Stubs.sym_(2, sliceAtI(svPtr.v1d, 1), charArrayOf(NUL), svPtr)
                    } else { /* globals */
                        Stubs.keyfix(sliceAtI(svPtr.v1d, 1))
                        // original length - comp for start
                        k = CstringOps.cstringAt(svPtr.v1d, 1).length + 1
                        if (svPtr.v1d[k - 1] != CHAR_1) {
                            svPtr.v1d[k] = CHAR_1
                            k++
                        }
                        svPtr.v1d[k] = NUL
                        k--
                        CstringOps.copyAt(tmp2, 0, sliceAtI(svPtr.v1d, 1))
                        /* kill first */
                        Mglobal(GKILL, sliceAtI(svPtr.v1d, 1), svPtr.bd, svPtr)
                        // the #if !(SQLITE) XNEXT kill-others loop
                        // (k1:) is dead in the reference build
                        // (SQLITE live, H-018 note)
                    }
                } else {
                    // Kill all locals except: the except-list
                    svPtr.nstart = null
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
                svPtr.ierr = parse_(svPtr)
                if (svPtr.ierr != 0) {
                    lab = ILblI.PARSE_ERR
                    continue@machine
                }
                Stubs.keyfix(sliceAtI(svPtr.v1d, 1)) /* in case its an array */
                val nmI = CstringOps.cstringAt(svPtr.v1d, 1)
                svPtr.nstart = NmesI(nmI, svPtr.nstart as NmesI?)
                svPtr.xpx++
                if (svPtr.xd[svPtr.xpx] != ')' && svPtr.xd[svPtr.xpx] != NUL) {
                    svPtr.xpx++
                    lab = ILblI.KILL2
                    continue@machine
                }
                svPtr.symflg = 5
                Stubs.sym_(5, charArrayOf(NUL), charArrayOf(NUL), svPtr)
                svPtr.nstart = null
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
                svPtr.ierr = parse_(svPtr)
                if (svPtr.ierr != 0) {
                    lab = ILblI.PARSE_ERR
                    continue@machine
                }
                i = atoi(CstringOps.cstringAt(svPtr.pd1, svPtr.sdlim))
                if (svPtr.in_file[i] == null && svPtr.out_file[i] == null) {
                    lab = ILblI.FILE_ERROR
                    continue@machine
                }
                val inChI = svPtr.in_file[i]
                val outChI = svPtr.out_file[i]
                if (inChI != null) inChI.close()
                else outChI?.close()
                svPtr.in_file[i] = null
                svPtr.out_file[i] = null
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
                svPtr.ierr = parse_(svPtr)
                if (svPtr.ierr != 0) {
                    lab = ILblI.PARSE_ERR
                    continue@machine
                }
                svPtr.io = atoi(CstringOps.cstringAt(svPtr.pd1, svPtr.sdlim))
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
                    CstringOps.copyAt(svPtr.v1d, 0, sliceAtI(vd, 1))
                    CstringOps.copyAt(svPtr.bd, 0, sliceAtI(svPtr.pd1, svPtr.sdlim))
                    if (MergeFlag == 0) {
                        Mglobal(STORE, svPtr.v1d, svPtr.bd, svPtr)
                    } else {
                        Stubs.Merge(svPtr.v1d, sliceAtI(MergeRHS, 1), svPtr)
                    }
                } else {
                    if (vd[1] == '$' && (vd[2] == 'p' || vd[2] == 'P')) { // piece
                        CstringOps.copyAt(tmp3, 0, sliceAtI(svPtr.pd1, svPtr.sdlim))
                        CstringOps.copyAt(svPtr.v1d, 1, sliceAtI(vd, 1))
                        svPtr.setpiece = 1
                        fcn(svPtr)
                        svPtr.setpiece = 0
                        lab = ILblI.LHSREF1
                        continue@machine
                    } else if (vd[1] == '$' && (vd[2] == 'e' || vd[2] == 'E')) { // extract
                        CstringOps.copyAt(tmp3, 0, sliceAtI(svPtr.pd1, svPtr.sdlim))
                        CstringOps.copyAt(svPtr.v1d, 1, sliceAtI(vd, 1))
                        svPtr.setpiece = 1
                        fcn(svPtr)
                        svPtr.setpiece = 0
                        lab = ILblI.LHSREF1
                        continue@machine
                    } else if (vd[1] == '$' && (vd[2] == 'z' || vd[2] == 'Z')) { // z var
                        CstringOps.copyAt(tmp3, 0, sliceAtI(svPtr.pd1, svPtr.sdlim))
                        CstringOps.copyAt(svPtr.v1d, 1, sliceAtI(vd, 1))
                        svPtr.setpiece = 1
                        fcn(svPtr)
                        svPtr.setpiece = 0
                        lab = ILblI.LHSREF1
                        continue@machine
                    } else {
                        svPtr.symflg = 0 /* store */
                        Stubs.sym_(0, sliceAtI(vd, 1),
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
                if (svPtr.xd[svPtr.xpx] == NUL) {
                    lab = ILblI.LBL_ERROR
                    continue@machine
                }
                // indirect argument
                if (svPtr.xd[svPtr.xpx] == '@') {
                    svPtr.t0px = 1
                    svPtr.t2 = 2
                    svPtr.ierr = parse_(svPtr)
                    if (svPtr.ierr != 0) {
                        lab = ILblI.LBL_ERROR
                        continue@machine
                    }
                    if (svPtr.t0px == 0) {
                        // copy label part to do save
                        var iLabelI = 1
                        while (svPtr.v1d[iLabelI] != NUL && svPtr.v1d[iLabelI] < '\u007F') iLabelI++
                        CstringOps.copyAt(dosave, 0, sliceAtI(svPtr.v1d, 1).copyOf(iLabelI - 1).also { it[iLabelI - 2] = NUL })
                        if (svPtr.v1d[iLabelI] != NUL) {
                            // arguments follow the label
                            CstringOps.copyAt(svPtr.args, 0, sliceAtI(svPtr.v1d, iLabelI + 1))
                        }
                    } else {
                        CstringOps.copyAt(dosave, 0, sliceAtI(svPtr.pd1, svPtr.sdlim))
                    }
                    if (svPtr.t0px == 0) svPtr.xpx++
                } else if (svPtr.xd[svPtr.xpx] == '$') { // builtin function
                    svPtr.t2 = 1
                    svPtr.ierr = parse_(svPtr)
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
                    while (svPtr.xd[svPtr.xpx] != NUL) {
                        if (svPtr.xd[svPtr.xpx] == ',' || svPtr.xd[svPtr.xpx] == ' ' ||
                                svPtr.xd[svPtr.xpx] == NUL || svPtr.xd[svPtr.xpx] == '+' ||
                                svPtr.xd[svPtr.xpx] == '(' ||
                                svPtr.xd[svPtr.xpx] == '^' || svPtr.xd[svPtr.xpx] == ':') break
                        dosave[i] = svPtr.xd[svPtr.xpx]
                        i++
                        svPtr.xpx++
                    }
                    dosave[i] = NUL
                }
                // handles do xxx(a,b,c) - not do xxx^yyy(a,b,c)
                // fcn call with args
                if (svPtr.xd[svPtr.xpx] == '(' || svPtr.xd[svPtr.xpx] == OPEN_PAREN_I) {
                    if (svPtr.xd[svPtr.xpx + 1] == ')') {
                        // empty argument list case ()
                        svPtr.args[0] = NUL
                        svPtr.args[1] = NUL
                        svPtr.args[2] = NUL
                        svPtr.xpx += 2
                    } else {
                        svPtr.t0px = 1
                        svPtr.t2 = 1
                        svPtr.xpx--
                        svPtr.xd[svPtr.xpx] = '$'
                        svPtr.ierr = parse_(svPtr)
                        if (svPtr.ierr != 0) {
                            lab = ILblI.PARSE_ERR
                            continue@machine
                        }
                        svPtr.xpx++
                        CstringOps.copyAt(svPtr.args, 0, sliceAtI(svPtr.v1d, 3))
                    }
                }
                k = -1
                if (svPtr.xd[svPtr.xpx] == '+') {
                    svPtr.t2 = 2
                    svPtr.ierr = parse_(svPtr)
                    if (svPtr.ierr != 0) {
                        lab = ILblI.PARSE_ERR
                        continue@machine
                    }
                    k = atoi(CstringOps.cstringAt(svPtr.pd1, svPtr.sdlim))
                    if (k < 0) {
                        lab = ILblI.LBL_ERROR
                        continue@machine
                    }
                }
                svPtr.v1d[1] = NUL
                if (svPtr.xd[svPtr.xpx] != ' ' &&
                        svPtr.xd[svPtr.xpx] != ',' &&
                        svPtr.xd[svPtr.xpx] != NUL &&
                        svPtr.xd[svPtr.xpx] != ':') {
                    svPtr.t0px = 1
                    svPtr.t2 = 1
                    if (svPtr.xd[svPtr.xpx] == '^' && svPtr.xd[svPtr.xpx + 1] == '@') {
                        svPtr.xpx++
                        fileflg = 1
                    } else fileflg = 0
                    svPtr.ierr = parse_(svPtr)
                    if (svPtr.ierr != 0) {
                        lab = ILblI.FILE_ERROR
                        continue@machine
                    }
                    if (fileflg == 1) {
                        CstringOps.copyAt(ftmp, 0, sliceAtI(svPtr.v1d, 1))
                        svPtr.v1d[1] = '^'
                        CstringOps.copyAt(svPtr.v1d, 2, sliceAtI(ftmp, 0))
                    }
                    if (svPtr.xd[svPtr.xpx] != ':') svPtr.xpx++
                }
                if (svPtr.xd[svPtr.xpx] == ':') {
                    svPtr.xpx++
                    svPtr.t2 = 1
                    svPtr.t0px = 0
                    CstringOps.copyAt(ftmp, 0, sliceAtI(svPtr.v1d, 1))
                    svPtr.ierr = parse_(svPtr)
                    if (svPtr.ierr != 0) {
                        lab = ILblI.POST_COND_ERROR
                        continue@machine
                    }
                    CstringOps.copyAt(svPtr.v1d, 1, sliceAtI(ftmp, 0))
                    if (atoi(CstringOps.cstringAt(svPtr.pd1, svPtr.sdlim)) == 0) {
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
                    CstringOps.copyAt(svPtr.v1d, 1, sliceAtI(dosave, 0))
                    dosave[0] = NUL
                }
                if (svPtr.v1d[1] != NUL) {
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
                    ftmp1I[iFileI] = NUL
                    /* load file */
                    readf_(ftmp1I, svPtr.iorg.toShort(), svPtr)
                    if (ftmp1I[0] == NUL) {
                        lab = ILblI.FILE_ERROR
                        continue@machine
                    }
                    // remove file name leaving args if any
                    val xI = 1
                    val yI = iFileI + 2
                    var xIdxI = xI
                    var yIdxI = yI
                    while (svPtr.v1d[yIdxI] != NUL) {
                        svPtr.v1d[xIdxI] = svPtr.v1d[yIdxI]
                        xIdxI++
                        yIdxI++
                    }
                    svPtr.v1d[xIdxI] = NUL
                    // processed args: copy already processed args
                    CstringOps.copyAt(svPtr.args, 0, sliceAtI(svPtr.v1d, 2))
                    svPtr.LineNumber = 0L
                    lblx = 0
                    if (svPtr.v1d[2] == NUL) {
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
                if (dosave[0] != NUL) {
                    var searchDoneI = false
                    while (!searchDoneI && ibr < svPtr.pd1len.toInt()) {
                        if (svPtr.pd1[ibr] == TAB || svPtr.pd1[ibr] == ' ' ||
                                svPtr.pd1[ibr] == '#' || svPtr.pd1[ibr] == ';') { // no label
                            while (svPtr.pd1[ibr] != NUL && ibr < svPtr.pd1len.toInt()) ibr++
                            if (svPtr.pd1[ibr] == NUL && ibr < svPtr.pd1len.toInt()) ibr++
                        } else { // something in column 1
                            j = 0
                            while (svPtr.pd1[ibr] == dosave[j] && dosave[j] != NUL) {
                                // compare dosave label with text
                                j++
                                ibr++
                            }
                            if ((svPtr.pd1[ibr] == TAB ||
                                        svPtr.pd1[ibr] == ' ' ||
                                        svPtr.pd1[ibr] == '(' ||
                                        svPtr.pd1[ibr] == NUL) && dosave[j] == NUL) {
                                // label found
                                if (lblx < LBLS_I - 1 && j < 10 && svPtr.pd1[ibr] != '(') {
                                    if (lblx < 20) { // wident guard note
                                        lblx++
                                        CstringOps.copyAt(lbls[lblx], 0, sliceAtI(dosave, 0))
                                        lbloff[lblx] = ibr
                                    }
                                }
                                lab = ILblI.OFFSET
                                searchDoneI = true
                                continue
                            }
                            while (svPtr.pd1[ibr] != NUL && ibr < svPtr.pd1len.toInt()) ibr++
                            if (svPtr.pd1[ibr] == NUL && ibr < svPtr.pd1len.toInt()) ibr++
                        }
                    }
                    if (!searchDoneI) {
                        lab = ILblI.LBL_ERROR
                        continue@machine
                    }
                } else {
                    // not do xxx^yyy but may be do xxx^yy(123)
                    while (svPtr.pd1[ibr] != ' ' &&
                            svPtr.pd1[ibr] != TAB &&
                            svPtr.pd1[ibr] != NUL &&
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
                    svPtr.start[svPtr._Sym] = null
                    SymInc = 1 // flag prevents inc again
                    viI = 0
                    while (svPtr.args[viI] != NUL) {
                        if (svPtr.args[viI] == CodedClose.toChar() ||
                                svPtr.args[viI] == CodedComma.toChar()) {
                            svPtr.args[viI] = NUL
                            nargsI++
                            argoffsI[nargsI] = viI + 1
                        }
                        viI++
                    }
                    viI = 0
                    var argDoneI = false
                    while (!argDoneI) {
                        if (svPtr.pd1[vjI] == ' ' || svPtr.pd1[vjI] == NUL) {
                            argDoneI = true
                            continue
                        }
                        if (svPtr.pd1[vjI] == NUL) {
                            lab = ILblI.LBL_ERROR
                            continue@machine
                        }
                        if (svPtr.pd1[vjI] == ',' || svPtr.pd1[vjI] == ')') {
                            varI[viI] = NUL
                            if (countI >= nargsI) {
                                // arg count mismatch - pass zeros
                                varI[viI] = NUL
                            } else {
                                varI[viI] = NUL
                                // code 100 stores at this level ->
                                // parameter
                                var symflgI = 100
                                var tI: CharArray
                                val argFirstI = argoffsI[countI]
                                if (svPtr.args[argFirstI] == '.') {
                                    CstringOps.copyAt(svPtr.reference, 0,
                                        sliceAtI(svPtr.args, argFirstI + 1))
                                    val tmpSymI = CharArray(STR_MAX)
                                    if (Stubs.sym_(SYM_RETRIEVE_I,
                                            sliceAtI(svPtr.args, argFirstI + 1),
                                            tmpSymI, svPtr) == null) {
                                        System.out.print("*** Var: " +
                                            CstringOps.cstringAt(svPtr.args, argFirstI + 1) + "\n")
                                        lab = ILblI.VAR_NOT_FOUND
                                        continue@machine
                                    }
                                    tI = tmpSymI
                                    symflgI = 200
                                } else {
                                    tI = sliceAtI(svPtr.args, argFirstI)
                                }
                                Stubs.sym_(symflgI, sliceAtI(varI, 0), tI, svPtr)
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
                        while (svPtr.pd1[ibr] != NUL && ibr < svPtr.pd1len.toInt()) ibr++
                        if (svPtr.pd1[ibr] == NUL && ibr < svPtr.pd1len.toInt()) ibr++
                        if (ibr >= svPtr.pd1len.toInt()) {
                            lab = ILblI.LBL_ERROR
                            continue@machine
                        }
                        i++
                    }
                    while (svPtr.pd1[ibr] != TAB && svPtr.pd1[ibr] != ' ') ibr++
                    if (ibr >= svPtr.pd1len.toInt()) {
                        lab = ILblI.LBL_ERROR
                        continue@machine
                    }
                } else if (k == 0) ibr = pd1cur
                // else pd1cur = ibr (the commented-out else drops)
                if (svPtr.xd[svPtr.xpx] != NUL && svPtr.xd[svPtr.xpx] != ' ') svPtr.xpx++
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
                prnterr(ernbr, svPtr, pd1cur)
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
                prnterr(ernbr, svPtr, pd1cur)
                lab = ILblI.ABRT1
                continue@machine
            }

            ILblI.LBL_ERROR -> {
                ernbr = 8
                prnterr(ernbr, svPtr, pd1cur)
                System.out.print(" " + CstringOps.cstring(dosave) + "\n")
                lab = ILblI.ABRT
                continue@machine
            }

            ILblI.VAR_NOT_FOUND -> {
                ernbr = 17
                prnterr(ernbr, svPtr, pd1cur)
                lab = ILblI.ABRT
                continue@machine
            }

            ILblI.CMD_DEFAULT -> {
                ernbr = 13
                prnterr(ernbr, svPtr, pd1cur)
                lab = ILblI.ABRT1
                continue@machine
            }

            ILblI.ARG_LIST_ERROR -> {
                ernbr = 14
                prnterr(ernbr, svPtr, pd1cur)
                lab = ILblI.ABRT1
                continue@machine
            }

            ILblI.REF_ERR -> {
                // dead target (H-013 note): nothing jumps here in
                // the vendor body
                ernbr = 18
                prnterr(ernbr, svPtr, pd1cur)
                lab = ILblI.ABRT1
                continue@machine
            }

            ILblI.SYMTAB_ERR -> {
                // dead target (H-013 note)
                ernbr = 23
                prnterr(ernbr, svPtr, pd1cur)
                lab = ILblI.ABRT1
                continue@machine
            }

            ILblI.FILE_ERROR -> {
                ernbr = 26
                prnterr(ernbr, svPtr, pd1cur)
                lab = ILblI.ABRT1
                continue@machine
            }

            ILblI.IO_ERROR -> {
                ernbr = 20
                prnterr(ernbr, svPtr, pd1cur)
                lab = ILblI.ABRT1
                continue@machine
            }

            ILblI.PROGRAM_SIZE -> {
                // dead target (H-013 note)
                ernbr = 31
                prnterr(ernbr, svPtr, pd1cur)
                lab = ILblI.ABRT1
                continue@machine
            }

            ILblI.STACK_OVERFLOW -> {
                ernbr = 32
                prnterr(ernbr, svPtr, pd1cur)
                lab = ILblI.ABRT1
                continue@machine
            }

            ILblI.SQL_ERROR -> {
                // dead target (H-013 note)
                ernbr = 33
                prnterr(ernbr, svPtr, pd1cur)
                lab = ILblI.ABRT1
                continue@machine
            }

            ILblI.USE_NOT_OPEN -> {
                ernbr = 36
                prnterr(ernbr, svPtr, pd1cur)
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
