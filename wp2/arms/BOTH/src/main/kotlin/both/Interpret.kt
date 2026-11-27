// Unit 185 BOTH render: interp-interp.cpp::Interpret, roster 308,
// batch 6 (the big-three close). The interpreter mainline: the
// vendor file is a 1581-LOC goto ladder; rendered as a labeled
// while lattice (the next_line/next_cmnd/quit family folds) with
// the vendor label names kept on the loop-state enum so the C
// diff maps 1:1. The flip face is MumpsMain.kt::interpret - the
// lowercase UI face keeps its body (delegating here), per the
// committed order (no other call sites change: they call the
// lowercase face).
//
// FILE-LOCAL defines (interp-interp.cpp:71-79): GOSUBMAX 50,
// SWAP 30, FORMAX 30, EXECLEVS 40, LBLS 80, OPEN_PAREN 206.
// Command letters (include/mumpsc/interp.h + compiler-macros.h):
// CMMNT ';', FCN "\u0024" (the h015 seat), ZCMDS 'z', JOB 'j',
// SET 's', MERGE 'm', GOTO 'g', WRITE 'w', IF 'i', NEW 'n',
// READ 'r', QUIT 'q', XECUTE 'x', FOR 'f', HALT 'h', ELSE 'e',
// DO 'd', DATAKILL 'k', USE 'u', FILELOCK 'l', BREAK 'b',
// FILEOPEN 'o', FILECLOSE 'c' (interp.h:39-45).
// Reference build config (h018): SQLITE defined; NATIVE_SHARED
// and TERMINATE_ON_ERROR NOT defined (the abrt arm returns 99;
// the PULSE deadlocks stay gated false, h013).
package both

private const val GOSUBMAX = 50 // interp-interp.cpp:71
private const val SWAP = 30 // interp-interp.cpp:72
private const val FORMAX = 30 // interp-interp.cpp:73
private const val EXECLEVS = 40 // interp-interp.cpp:74
private const val LBLS = 80 // interp-interp.cpp:75
private const val IP_OPEN_PAREN = 206 // interp-interp.cpp:79 (defines.h OPEN macro fold)

// defines.h op codes the render reaches (h002 pins)
private const val IP_CLOSE = 4 // defines.h:87 CLOSE
private const val IP_INIT = 6 // defines.h:166 INIT
private const val IP_GKILL = 3 // defines.h:167 GKILL
private const val IP_XNEXT = 8 // defines.h:88 XNEXT
private const val IP_STORE = 1 // defines.h:84 STORE
private const val IP_RETRIEVE = 0 // defines.h:83 RETRIEVE

// numcomp: the vendor signs (interp-arith.cpp:980 family)
private const val IP_SymStore = 0 // defines.h:43 SymStore
private const val IP_SymRetrieve = 1 // defines.h:44 SymRetrieve

// Face stubs, file-private (h014 owner pins; cross-TU non-render riders):

// short getstr2(short, unsigned char[], MSV*) - the declared-in-
// prologue interp-interp.cpp:91 unit-local reader (init.mps
// pending owner parse; readf_/readseq family).
private fun getstr2Zb(lun: Int, area: CharArray, sv: Msv): Int {
    TODO("stub: owner interp-interp.cpp::getstr2 (prologue-local reader)")
}

// void readf_(unsigned char[], short, MSV*) - the file-load
// rider (InterpHMisc ledger: compiler-sys1.c readf_ roster b4).
private fun readf_Zb(zd: CharArray, pd1org: Int, sv: Msv) {
    TODO("stub: owner compiler-sys1.c::readf_ (roster 78, batch 3 - the old pin said roster b4, C-015)")
}

// int DirectRead(int, unsigned char*) - the READ * direct-mode
// reader (interp-interp.cpp:95 face; reader family pin).
private fun directReadZb(io: Int, i: CharArray): Int {
    TODO("stub: owner interp-io DirectRead (non-roster reader)")
}

// char* makeLock(char*, char*, char*, char, char*) - libmpscpp
// global array lock service (libmpscpp.h:130 family pin).
private fun makeLockZb(
    a: CharArray, b: String, c: String, lf: Char, d: String
): String {
    TODO("stub: owner libmpscpp.cpp::makeLock (non-roster cross-TU helper)")
}

// void prnterr(int, MSV*, int) - interp.h:99; non-unit
// provenance (the interpreter error-print family; InterpHMisc
// ledger pin).
private fun prnterrZb(ernbr: Int, sv: Msv, pd1cur: Int) {
    TODO("stub: owner interp-prnterr (non-unit error-print family)")
}

// int _SymFree(int, MSV*) - include/mumpsc/sym.h:69 (the
// symbol-table layer free; owner interp-sym.cpp residue).
private fun symFreeZb(i: Int, sv: Msv): Int {
    TODO("stub: owner interp-sym.cpp::_SymFree (sym.h:69)")
}

// int numcomp(unsigned char*, unsigned char*) - interp-arith
// .cpp:980 (the roster-67 residue family; MpstimeMain pins
// numcomp2 only). Returns -1/0/+1 strcmp-style over numeric
// strings.
private fun numcompZb(aa: String, bb: String): Int {
    TODO("stub: owner interp-arith.cpp::numcomp (residue, non-roster - the old pin said roster-67, C-015)")
}

// void Merge(unsigned char*, unsigned char*, MSV*) - the MERGE
// command body (libmpscpp.cpp non-roster rider).
private fun mergeZb(key: CharArray, rhs: CharArray, sv: Msv) {
    TODO("stub: owner libmpscpp.cpp::Merge (non-roster rider)")
}

// int BuildGlobal/BuildLocal(int, int, unsigned char*, ...,
// MSV*) - interp.h:100-101 indirection builders (SymHMisc
// ledger: interp-parse.cpp BuildGlobal b8 pending). The
// next_cmnd stack-cleaner calls seat here file-private.
private fun buildLocalZb(a: Int, b: Int, s1: String?, s2: String?, sv: Msv): Int {
    TODO("stub: owner interp-parse.cpp::BuildLocal (ledger b8 family)")
}

private fun buildGlobalZb(a: Int, b: Int, s1: String?, s2: String?, sv: Msv): Int {
    TODO("stub: owner interp-parse.cpp::BuildGlobal (roster 230, batch 8 - RELEASED at the unit-230 BuildGlobal.kt render; this private face keeps its seat per the per-TU note; the old pin said roster b8, C-015)")
}

// The loop-state fold: the vendor goto labels become the state
// enum; the cmdstack/gosub/swap arenas keep the C shapes.
private enum class ItLbl {
    ABRT,
    ABRT1,
    ARGLESS,
    ARG_LIST_ERROR,
    BYP,
    CMD,
    CONT_READ,
    DEFAULT_ERR,
    DO_CMD,
    DO_DECLARE,
    DO_RET,
    DO_RET1,
    ERR_CALL,
    FILE_ERROR,
    FOR_END,
    FOR_END2,
    FOR_PUSH,
    FOR_QUIT,
    GOTO_LBL_ENTRY,
    GOTO_RETURN,
    HALT_T,
    HANG,
    INIT_LOOP_VAR,
    IOSUB,
    IOSUB1,
    IO_ERROR,
    KILL2,
    KILL_AGAIN,
    KILL_SPECIFIC,
    KILL_TAIL,
    LBL,
    LBL_ARGS,
    LBL_COLON,
    LBL_ERROR,
    LBL_FILE,
    LBL_LOAD,
    LBL_PLUS,
    LBL_SEARCH,
    LHSREF,
    LHSREF1,
    NEW2,
    NEW_AGAIN,
    NEW_TAIL,
    NEXT_CLOSE_ARG,
    NEXT_CMND,
    NEXT_IF_ARG,
    NEXT_LINE,
    NEXT_LINE_XECUTE,
    NXT_ARG,
    OFFSET,
    OFFSET_DONE,
    OPENF,
    OPN1,
    PARSE_ERR,
    POST_COND_ERROR,
    PROGRAM_SIZE,
    QUIT,
    QUIT1,
    READ_CMD,
    REF_ERR,
    RESTART,
    RESTORE,
    RESTORE_DONE,
    S1,
    S2,
    SET,
    SET_RETURN,
    SET_TAIL,
    SHELLSQL,
    SQL_ERROR,
    STACK_OVERFLOW,
    START_IO,
    STORE,
    SYMTAB_ERR,
    USE_NOT_OPEN,
    VAR_NOT_FOUND,
    WRITEPROC,
    XECUTE,
    XECUTE_BODY,
    XECUTE_RETURN
}


// strcpy-at-offset: the vendor &buf[off] = src idiom (h020).
private fun putAtZb(dst: CharArray, off: Int, src: String) {
    var j = 0
    while (j < src.length) {
        dst[off + j] = src[j]
        j++
    }
    dst[off + j] = '\u0000'
}

// strlen-at-offset (the &pd1[i] arena reads).
private fun lenAtZb(s: CharArray, off: Int): Int {
    var n = 0
    while (s[off + n] != '\u0000') n++
    return n
}

// strcpy-at-offset arena copy (no NUL overrun checks: the C
// contract assumes sized callers).
private fun cpyAtZb(dst: CharArray, dstOff: Int, src: CharArray, srcOff: Int) {
    var k = 0
    while (src[srcOff + k] != '\u0000') {
        dst[dstOff + k] = src[srcOff + k]
        k++
    }
    dst[dstOff + k] = '\u0000'
}

private fun eqAtZb(s: CharArray, off: Int, lit: String): Boolean {
    var k = 0
    while (k < lit.length) {
        if (s[off + k] != lit[k]) return false
        k++
    }
    return s[off + k] == '\u0000'
}

// atoi-at-offset (the &pd1[sdlim] numeric reads).
private fun atoiAtZb(s: CharArray, off: Int): Int {
    var k = off
    while (k < s.size && (s[k] == ' ' || s[k] == '\u0009')) k++
    var neg = false
    if (k < s.size && (s[k] == '-' || s[k] == '+')) {
        neg = s[k] == '-'
        k++
    }
    var v = 0L
    while (k < s.size && s[k] in '0'..'9') {
        v = v * 10 + (s[k] - '0')
        k++
    }
    val out = if (neg) -v.toInt() else v.toInt()
    return out
}

// strncasecmp(pd1+off, lit, n) prefix test (the shell/sql arms).
private fun ncaseEqAtZb(s: CharArray, off: Int, lit: String): Boolean {
    var k = 0
    while (k < lit.length) {
        val c = s[off + k]
        if (c == '\u0000') return false
        if (c.lowercaseChar() != lit[k].lowercaseChar()) return false
        k++
    }
    return true
}

private fun getpidZb(): Long = ProcessHandle.current().pid()

// strn-at-offset: the C string read from an arena.
private fun strAtZb(s: CharArray, off: Int): String {
    val sb = StringBuilder()
    while (s[off + sb.length] != '\u0000') sb.append(s[off + sb.length])
    return sb.toString()
}

// popen(cmd, "r") - the SHELL/P seat: reads command stdout into
// the in_file[6] scratch (h020: the FILE* bridge rides java.io;
// the plan-side TODO owner seats the readback helper family).
private fun popenReadZb(cmd: String): java.io.RandomAccessFile? {
    TODO("stub: owner popen read arm (SHELL/P helper family)")
}

// popen(cmd, "w") - the SHELL/G seat.
private fun popenWriteZb(cmd: String): java.io.RandomAccessFile? {
    TODO("stub: owner popen write arm (SHELL/G helper family)")
}

// fork() gate for the JOB child arm (h013: the sequential JVM
// seat rides the same dead-code gate precedent as znxtrow).
private val jobForkGateZb = false

// int Interpret (const char *parm1, struct MSV * svPtr) -
// interp-interp.cpp:120. Rendered (roster 308, batch 6).
internal fun Interpret(parm1: String, sv: Msv): Int {
    // locals (interp-interp.cpp:133-193)
    var ibr = 0
    var pd1hold1 = 0
    var pd1hold2 = 0
    var pd1cur = 0
    var i = 0
    var j = 0
    var k = 0
    var l = 0
    var newFlagValue = 0 // C "New" (the Kotlin keyword fold; FILEOPEN seat)
    val swp = arrayOfNulls<CharArray>(SWAP) // static char *swp[SWAP]
    val tmp2 = CharArray(STR_MAX)
    val tmp3 = CharArray(STR_MAX)
    val vd = CharArray(STR_MAX)
    val dosave = CharArray(STR_MAX)
    val gosub = Array(GOSUBMAX) { IntArray(9) } // gosub[GOSUBMAX][9]
    var gsbx = 0
    var cmdx = 0
    var cmdxold = 0
    var forx = 0
    var pd1Last = 0
    var newFlagged = 0 // int NewFlag (casing fold)
    var symInc = 0 // int SymInc
    val cmdstack = CharArray(8192) // unsigned char cmdstack[8192]
    val lbls = Array(LBLS) { CharArray(20) } // unsigned char lbls[LBLS][20]
    val lbloff = IntArray(20)
    var lblx = 0
    var argless = 0
    val forv = Array(FORMAX) { CharArray(256) }
    val forit = IntArray(FORMAX)
    val forlt = IntArray(FORMAX)
    val ftmp = CharArray(STR_MAX)
    val forin = Array(FORMAX) { CharArray(STR_MAX) }
    val forlv = Array(FORMAX) { CharArray(256) }
    var swapextent = 0
    var shold = 0
    var execLevel = 1
    val execLevNew = IntArray(EXECLEVS)
    var breakFlag = false
    var retz = 0.toChar()
    var icmnd = 0.toChar()
    var p2px = 0
    var ernbr = 0
    var fileflg = 0
    var dirflg = 0.toChar()
    var mergeFlag = 0
    val mergeRHS = CharArray(STR_MAX)
    var dotcount = 0
    var Lf = ' ' // the FILELOCK Lf seat (the C block-local)
    // SHELL/SQL locals (C block: buf/filename/bx/pflag)
    val buf = CharArray(STR_MAX)
    var bx = 0
    var pflag = 0
    // loop control (the goto-ladder fold)
    var state = ItLbl.RESTART
    var retVal = 0
    // io carrier seats (the C label-local i/j folded to state carriers
    // across the READ/WRITE inout arms)
    var readIo = 0
    var readCol = 0
    var writeIo = 0
    var writeCol = 0
    // FOR-family carriers (the C "i" results across loop states)
    var forI = 0
    var forArgGate = false
    // LBL-block carriers (the C block-local k / t offset)
    var lblK = -1
    var tOffArg = 0

    // init environment (interp-interp.cpp:208-230)
    sv.io = 5
    pd1cur = 0
    while (pd1cur < PS) {
        sv.pd1[pd1cur] = ' '
        pd1cur++
    }
    sv.symlen = PS
    pd1cur = 1
    sv.pd1len = 0
    // srand(time(NULL)): seeded once per process in the JVM seat
    sv.bd[0] = '\u0000'
    sv.v1d[0] = '\u0000'
    sv.bd[1] = '\u0000'
    i = 0
    while (i < GOSUBMAX) {
        j = 0
        while (j < 9) {
            gosub[i][j] = 0
            j++
        }
        i++
    }
    // SQLITE arm (build config): sql auto connect at startup
    run {
        val key = CharArray(2)
        val bd2 = CharArray(2)
        btreeC(IP_OPEN_PAREN, key, bd2, sv) // btree(OPEN,key,bd,svPtr)
    }
    pd1cur = 1
    sv.pd1[sv.pd1len.toInt() + 1] = '\u0000'
    sv.ctrlc = 0

    while (true) {
        when (state) {
        // ----------------------------------------------------------------
        ItLbl.RESTART -> {
            // restart: re-enter after error; reset environment
            breakFlag = false
            execLevel = 1
            i = 0
            while (i < EXECLEVS) { execLevNew[i] = 0; i++ }
            sv.ierr = 0
            sv.io = 5
            sv.t0px = 0
            sv.t2 = 0
            i = 0 // clear the swap area
            while (i < SWAP) { swp[i] = null; i++ }
            swapextent = 1 // (unsigned short init; vendor :154)
            lblx = 0
            // initialize execution buffer
            putAtZb(sv.pd1, 2, parm1)
            sv.pd1[1] = TAB
            sv.pd1len = (lenAtZb(sv.pd1, 1) + 1).toLong()
            breakFlag = false
            pd1cur = 1
            cmdx = 0
            sv.setpiece = 0
            sv.LineNumber = 0
            lblx = 0
            forx = 0
            gsbx = 0
            sv.kflg = 0
            pd1Last = 0
            state = ItLbl.NEXT_LINE
        }
        // ----------------------------------------------------------------
        ItLbl.NEXT_LINE -> {
            breakFlag = false
            if (pd1cur > sv.pd1len) {
                sv.xd[sv.xpx] = '\u0000'
                state = ItLbl.QUIT
            } else {
                sv.PD1 = PS1 - pd1cur
                sv.LineNumber++
                // comments or special code
                if (sv.pd1[pd1cur] == '#' || sv.pd1[pd1cur] == '%' ||
                    sv.pd1[pd1cur] == '+' || sv.pd1[pd1cur] == ';' ||
                    (sv.pd1[pd1cur] == '/' && sv.pd1[pd1cur + 1] == '/')) {
                    pd1cur++
                    while (sv.pd1[pd1cur] != '\u0000') pd1cur++
                    pd1cur++
                    state = ItLbl.NEXT_LINE
                } else if (sv.pd1[pd1cur] == '\u0000') {
                    pd1cur++
                    state = ItLbl.NEXT_LINE
                } else {
                    i = pd1cur // remember starting place
                    // skip label - find 1st blank or TAB
                    while (sv.pd1[pd1cur] != '\u0000' &&
                           sv.pd1[pd1cur] != TAB &&
                           sv.pd1[pd1cur] != ' ') pd1cur++
                    // skip multiple TABs
                    while (sv.pd1[pd1cur] == TAB || sv.pd1[pd1cur] == ' ') pd1cur++
                    // copy source line to xd
                    argless = 0
                    dotcount = 0
                    while (sv.pd1[pd1cur + dotcount] == '.') dotcount++
                    var skipLine = false
                    if (dotcount + 1 < execLevel) {
                        state = ItLbl.QUIT1
                    } else {
                        if (dotcount > 0 && sv.pd1[pd1cur] == '.') {
                            var matchedLevel = -1
                            if (eqAtZb(sv.pd1, pd1cur, ". ")) matchedLevel = 2
                            else if (eqAtZb(sv.pd1, pd1cur, ".. ")) matchedLevel = 3
                            else if (eqAtZb(sv.pd1, pd1cur, "... ")) matchedLevel = 4
                            else if (eqAtZb(sv.pd1, pd1cur, ".... ")) matchedLevel = 5
                            else if (eqAtZb(sv.pd1, pd1cur, "..... ")) matchedLevel = 6
                            else if (eqAtZb(sv.pd1, pd1cur, "...... ")) matchedLevel = 7
                            else if (eqAtZb(sv.pd1, pd1cur, "....... ")) matchedLevel = 8
                            else if (eqAtZb(sv.pd1, pd1cur, "........ ")) matchedLevel = 9
                            else if (eqAtZb(sv.pd1, pd1cur, "......... ")) matchedLevel = 10
                            if (matchedLevel >= 0) {
                                if (execLevel != matchedLevel) {
                                    while (sv.pd1[pd1cur] != '\u0000') pd1cur++
                                    pd1cur++
                                    skipLine = true
                                } else {
                                    pd1cur += matchedLevel // ". "->2 .. "......... "->10
                                }
                            }
                        }
                        if (!skipLine) state = ItLbl.NEXT_LINE_XECUTE
                        // (vendor: unmatched dot forms fall through to
                        // next_line_xecute; the -1 sentinel preserves it)
                    }
                }
            }
        }
        // ----------------------------------------------------------------
        ItLbl.NEXT_LINE_XECUTE -> {
            pd1Last = pd1cur
            sv.xpx = 1
            var toI = 1
            while (sv.pd1[pd1cur] != '\u0000') {
                sv.xd[toI] = sv.pd1[pd1cur]
                toI++
                pd1cur++
            }
            sv.xd[toI] = '\u0000'
            state = ItLbl.NEXT_CMND
        }
        // ----------------------------------------------------------------
        ItLbl.NEXT_CMND -> {
            buildLocalZb(-1, 0, null, null, sv) // stack cleaner
            buildGlobalZb(-1, 0, null, null, sv) // stack cleaner
            var decodeDone = false
            while (true) {
                if (sv.xd[sv.xpx] == '\u0000' || sv.xpx >= STR_MAX) {
                    if (forx > 0 && gosub[gsbx][3] == 'f'.code) {
                        if (sv.ctrlc != 0) {
                            sv.ctrlc = 0
                            ernbr = 29
                            state = ItLbl.ERR_CALL
                            decodeDone = true
                            break
                        }
                        state = ItLbl.QUIT1
                        decodeDone = true
                    } else {
                        state = ItLbl.NEXT_LINE
                        decodeDone = true
                    }
                    break
                }
                if (sv.xd[sv.xpx] != ' ' && sv.xd[sv.xpx] != TAB) break
                sv.xpx++
            }
            if (!decodeDone) {
                // decode command word
                p2px = sv.xpx
                sv.t2 = 0
                sv.t0px = 0
                icmnd = sv.xd[sv.xpx].lowercaseChar()
                sv.xpx++ // the C while(loop over ++xpx) fold
                while (sv.xd[sv.xpx].code in 65..90 || sv.xd[sv.xpx].code in 97..122) sv.xpx++
                if (sv.xd[sv.xpx] == ':') { // Postconditional
                    sv.xpx++
                    sv.ierr = parse_(sv)
                    if (sv.ierr != 0) { state = ItLbl.PARSE_ERR }
                    else {
                        if (atoiAtZb(sv.pd1, sv.sdlim) == 0) {
                            // postconditional false: skip to next command
                            var scanning = true
                            while (scanning) {
                                while (sv.xd[sv.xpx] != '\u0000') {
                                    if (sv.xd[sv.xpx] == '"') {
                                        sv.xpx++
                                        // pc1: skip quoted string
                                        while (sv.xd[sv.xpx] != '"' &&
                                               sv.xd[sv.xpx] != '\u0000') sv.xpx++
                                        sv.xpx++
                                        if (sv.xd[sv.xpx] == '"') { sv.xpx++ }
                                    }
                                    if (sv.xd[sv.xpx] == ' ') { scanning = false; break }
                                    sv.xpx++
                                }
                                if (scanning) break // EOL: xd[xpx]==0
                            }
                            sv.xpx--
                            state = ItLbl.NEXT_CMND
                        } else {
                            // postconditional return
                            if (sv.xd[sv.xpx] != '\u0000') sv.xpx++
                            state = ItLbl.CMD
                        }
                    }
                } else {
                    if (sv.xd[sv.xpx] != '\u0000') sv.xpx++
                    state = ItLbl.CMD
                }
            }
        }
        // ----------------------------------------------------------------
        // switch (icmnd): the command dispatcher (vendor :542)
        ItLbl.CMD -> {
            when (icmnd) {
            ';', '/' -> {
                // comment command - do nothing
                while (sv.xd[sv.xpx] != '\u0000') sv.xpx++
                sv.xpx = STR_MAX
                state = ItLbl.NEXT_CMND
            }
            '\u0024' -> {
                // direct function execution (FCN)
                sv.xpx = p2px
                sv.t0px = 0
                sv.t2 = 1
                sv.ierr = parse_(sv)
                if (sv.ierr != 0) { state = ItLbl.PARSE_ERR }
                else { sv.xpx++; state = ItLbl.NEXT_CMND }
            }
            'b' -> {
                // BREAK: non-standard use - end a FOR loop from a DO group
                breakFlag = true
                state = ItLbl.QUIT
            }
            'l' -> {
                // FILELOCK - not meaningful - all files are locked
                if (sv.xd[sv.xpx] == ' ' || sv.xd[sv.xpx] == '\u0000') {
                    state = ItLbl.NEXT_CMND
                } else {
                    // type of lock
                    if (sv.xd[sv.xpx] == '-' || sv.xd[sv.xpx] == '+') {
                        Lf = sv.xd[sv.xpx]; sv.xpx++
                    } else Lf = ' '
                    sv.t0px = 1
                    sv.ierr = parse_(sv)
                    if (sv.ierr != 0) { state = ItLbl.PARSE_ERR }
                    else {
                        keyfixC(sv.v1d)
                        sv.v1d[0] = '\u0000' // keyfixC shifts; the lock reference read at v1d[1]
                        cpyAtZb(tmp2, 0, sv.v1d, 1)
                        putAtZb(dosave, 0, "0") // timeout
                        sv.tpx = 1
                        if (sv.xd[sv.xpx + 1] == ':') {
                            sv.t2 = 1
                            sv.xpx += 2
                            sv.ierr = parse_(sv)
                            if (sv.ierr != 0) { state = ItLbl.PARSE_ERR }
                            else {
                                sv.xpx++
                                j = atoiAtZb(sv.pd1, sv.sdlim)
                                cpyAtZb(dosave, 0, sv.pd1, sv.sdlim)
                                sv.tpx = 0
                                state = ItLbl.NEXT_CMND
                            }
                        } else {
                            Mltoa(getpidZb(), tmp3) // process id
                            val got = makeLockZb(tmp2, "1", strAtZb(tmp3, 0), Lf, strAtZb(dosave, 0))
                            if (got == "1") sv.tpx = 1 else sv.tpx = 0
                            sv.LockDone = 1
                            sv.xpx++
                            state = ItLbl.NEXT_CMND
                        }
                    }
                }
            }
            'r' -> { state = ItLbl.READ_CMD }
            'o' -> { state = ItLbl.OPENF }
            'z' -> {
                // ZCMDS
                p2px++
                if (sv.xd[p2px].code <= 90) sv.xd[p2px] =
                    (sv.xd[p2px].code + 32).toChar() // lower case
                when (sv.xd[p2px]) {
                    'm' -> { state = ItLbl.NEXT_CMND } // zmain
                    'g' -> { // zg
                        sv.v1d[0] = '\u0000'
                        Mglobal(IP_INIT, sv.v1d, sv.bd, sv)
                        state = ItLbl.NEXT_CMND
                    }
                    'a' -> { sv.gpad = 0; state = ItLbl.NEXT_CMND }
                    'n' -> { sv.gpad = 1; state = ItLbl.NEXT_CMND }
                    'h', 'H' -> { // zhalt
                        sv.t2 = 1
                        sv.ierr = parse_(sv)
                        if (sv.ierr != 0) { state = ItLbl.PARSE_ERR }
                        else {
                            j = atoiAtZb(sv.pd1, sv.sdlim)
                            print("\n")
                            Mglobal(IP_CLOSE, tmp2, tmp3, sv) // Close Globals
                            kotlin.system.exitProcess(j)
                        }
                    }
                    'e' -> { // zexit
                        return 1
                    }
                    else -> { state = ItLbl.NEXT_LINE } // all others
                }
            }
            'i' -> {
                // IF
                if (sv.xd[sv.xpx] == ' ') { // no argument form
                    sv.xpx++
                    state = if (sv.tpx != 0L) ItLbl.NEXT_CMND else {
                        sv.xpx = STR_MAX; ItLbl.NEXT_CMND
                    }
                } else state = ItLbl.NEXT_IF_ARG
            }
            'w' -> {
                if (sv.io != 5 && sv.out_file[sv.io] == null) {
                    ernbr = 20; state = ItLbl.IO_ERROR
                } else state = ItLbl.WRITEPROC
            }
            'j' -> {
                // JOB: the vendor fork() parent continues; the child
                // closes globals and treats the argument as GOTO. The
                // JVM seat is sequential: the arm-body concept rides the
                // h013 gate (fork unreachable in this seat).
                if (jobForkGateZb) {
                    // Close Globals (SQLITE arm: sql(CLOSE,...))
                    sv.tpx = sqlZb(IP_CLOSE, sv, "", tmp2).toLong()
                }
                state = ItLbl.GOTO_LBL_ENTRY // fall thru to GOTO
            }
            'g' -> {
                state = ItLbl.GOTO_LBL_ENTRY
            }
            'm' -> {
                mergeFlag = 1
                state = ItLbl.SET
            }
            's' -> {
                // SET: SQL: and SHELL: commands too
                mergeFlag = 0
                val c1 = sv.xd[p2px + 1].uppercaseChar()
                if (c1 != 'Q' && c1 != 'H') state = ItLbl.SET
                else {
                    if (c1 == 'Q') { // SQL undifferentiated
                        pflag = -1
                        sv.bd[0] = '\u0000'
                        state = ItLbl.NEXT_LINE
                    } else state = ItLbl.SHELLSQL
                }
            }
            'q' -> { state = ItLbl.QUIT }
            'x' -> { state = ItLbl.XECUTE }
            'f' -> { state = ItLbl.FOR_PUSH }
            'h' -> { state = ItLbl.HALT_T }
            'e' -> {
                // ELSE
                if (sv.xd[sv.xpx] == ' ') sv.xpx++
                if (sv.tpx == 0L) { state = ItLbl.NEXT_CMND }
                else { sv.xpx = STR_MAX; state = ItLbl.NEXT_CMND }
            }
            'd' -> { state = ItLbl.DO_CMD }
            'n' -> { state = ItLbl.NEW_AGAIN }
            'k' -> { state = ItLbl.KILL_AGAIN }
            'c' -> {
                // FILECLOSE: p2px lowercase then the close arm
                // (vendor :2021-2027)
                p2px++
                if (sv.xd[p2px].code <= 90) sv.xd[p2px] =
                    (sv.xd[p2px].code + 32).toChar()
                if (sv.xd[p2px] == 'o') { state = ItLbl.QUIT } // CLOSEO
                else { state = ItLbl.NEXT_CLOSE_ARG }
            }
            'u' -> {
                // USE
                sv.t2 = 1
                sv.ierr = parse_(sv)
                if (sv.ierr != 0) { state = ItLbl.PARSE_ERR }
                else {
                    sv.io = atoiAtZb(sv.pd1, sv.sdlim)
                    if (sv.io <= 0 || sv.io > 10) {
                        ernbr = 14; state = ItLbl.ARG_LIST_ERROR
                    } else if (sv.in_file[sv.io] == null && sv.out_file[sv.io] == null) {
                        ernbr = 36; state = ItLbl.USE_NOT_OPEN
                    } else state = ItLbl.NEXT_CMND
                }
            }
            else -> {
                ernbr = 13; state = ItLbl.DEFAULT_ERR
            }
            } // when (icmnd)
        }
        // ----------------------------------------------------------------
        ItLbl.READ_CMD -> {
            if (sv.io != 5 && sv.in_file[sv.io] == null) {
                ernbr = 20; state = ItLbl.IO_ERROR
            } else {
                dirflg = '\u0000'
                if (sv.xd[sv.xpx] == '@') { // indirect
                    sv.t0px = 1
                    sv.ierr = parse_(sv)
                    if (sv.ierr != 0) { state = ItLbl.PARSE_ERR }
                }
                var ioKind = 0 // 1='#' 2='!' 3='?' 4='"'
                var colArg = 0
                if (sv.xd[sv.xpx] == '!' || sv.xd[sv.xpx] == '#') {
                    ioKind = if (sv.xd[sv.xpx] != '#') 2 else 1
                    state = ItLbl.START_IO
                } else if (sv.xd[sv.xpx] == '?') { // tab format
                    sv.t2 = 1
                    sv.xpx--
                    sv.ierr = parse_(sv)
                    if (sv.ierr != 0) { state = ItLbl.PARSE_ERR }
                    else {
                        colArg = atoiAtZb(sv.pd1, sv.sdlim)
                        ioKind = 3
                        state = ItLbl.START_IO
                    }
                } else if (sv.xd[sv.xpx] == '"') { // text printed first
                    sv.t2 = 1
                    sv.ierr = parse_(sv)
                    if (sv.ierr != 0) { state = ItLbl.PARSE_ERR }
                    else { ioKind = 4; state = ItLbl.START_IO }
                } else {
                    if (sv.xd[sv.xpx] == '*') { // direct mode
                        sv.xpx++
                        dirflg = '\u0001'
                    }
                    sv.t0px = 1 // extract variable name to be read
                    sv.setname[0] = '\u0000'
                    sv.ierr = parse_(sv)
                    if (sv.ierr != 0) { state = ItLbl.PARSE_ERR }
                    else {
                        cpyAtZb(vd, 1, sv.v1d, 1)
                        // timeout
                        if (sv.xd[sv.xpx + 1] == ':') {
                            sv.t2 = 1
                            sv.xpx += 2
                            sv.ierr = parse_(sv)
                            if (sv.ierr != 0) { state = ItLbl.PARSE_ERR }
                            else {
                                j = atoiAtZb(sv.pd1, sv.sdlim)
                                sv.xpx--
                                if (j <= 0) {
                                    sv.xpx--
                                    sv.tpx = 0
                                    state = ItLbl.CONT_READ
                                } else {
                                    if (dirflg == '\u0000') {
                                        sv.tpx = TimeOut_getstr1(sv.io, sv.pd1,
                                            strAtZb(sv.pd1, sv.sdlim), 0).toLong()
                                    } else {
                                        val ic = CharArray(1)
                                        sv.tpx = directReadZb(sv.io, ic).toLong()
                                        putAtZb(sv.pd1, sv.sdlim, ic[0].code.toString())
                                    }
                                    retz = 'r' // READ
                                    state = ItLbl.STORE
                                }
                            }
                        } else {
                            sv.hor[sv.io] = 0
                            sv.ver[sv.io]++
                            i = sv.io
                            sv.sdlim = (sv.pd1len + 2).toInt()
                            if (dirflg == '\u0001') {
                                val ic = CharArray(1)
                                sv.tpx = directReadZb(sv.io, ic).toLong()
                                putAtZb(sv.pd1, sv.sdlim, ic[0].code.toString())
                            } else {
                                sv.tpx = getstr2Zb(i, sv.pd1, sv).toLong()
                                if (sv.tpx >= 0) sv.tpx = 1 else sv.tpx = 0
                            }
                            retz = 'r' // READ
                            state = ItLbl.LHSREF
                        }
                    }
                }
                readIo = ioKind
                readCol = colArg
            }
        }
        // ----------------------------------------------------------------
        ItLbl.START_IO -> {
            // inout(&pd1[sdlim], i, j, svPtr)
            inout(sv.pd1, sv.sdlim, readIo, sv.pd1, readCol)
            state = ItLbl.CONT_READ
        }
        // ----------------------------------------------------------------
        ItLbl.CONT_READ -> {
            sv.xpx++
            dirflg = '\u0000'
            if (sv.xd[sv.xpx] == ',') sv.xpx++
            if (sv.xd[sv.xpx] == ' ' || sv.xd[sv.xpx] == '\u0000' ||
                sv.xd[sv.xpx] == TAB) { state = ItLbl.NEXT_CMND }
            else { state = ItLbl.READ_CMD }
        }
        // ----------------------------------------------------------------
        ItLbl.STORE -> {
            retz = 'r' // READ
            state = ItLbl.LHSREF
        }
        // ----------------------------------------------------------------
        ItLbl.OPENF -> {
            // openf: FILEOPEN
            sv.t2 = 1
            sv.ierr = parse_(sv)
            if (sv.ierr != 0) { state = ItLbl.PARSE_ERR }
            else {
                k = atoiAtZb(sv.pd1, sv.sdlim)
                if (k <= 0 || k >= 5) { ernbr = 26; state = ItLbl.FILE_ERROR }
                else if (sv.in_file[k] != null || sv.out_file[k] != null) {
                    ernbr = 26; state = ItLbl.FILE_ERROR
                } else if (sv.xd[sv.xpx] != ':') {
                    ernbr = 14; state = ItLbl.ARG_LIST_ERROR
                } else {
                    sv.xpx++
                    sv.t2 = 1
                    sv.ierr = parse_(sv)
                    if (sv.ierr != 0) { state = ItLbl.PARSE_ERR }
                    else {
                        i = 0
                        while (sv.pd1[sv.sdlim + i] != '\u0000' &&
                               sv.pd1[sv.sdlim + i] != ',') {
                            tmp2[i] = sv.pd1[sv.sdlim + i]; i++
                        }
                        if (sv.pd1[sv.sdlim + i] != ',') {
                            var i2 = 0
                            while (sv.pd1[sv.sdlim + i2] != '\u0000' &&
                                   sv.pd1[sv.sdlim + i2] != '/') {
                                tmp2[i2] = sv.pd1[sv.sdlim + i2]; i2++
                            }
                            i = i2
                        }
                        tmp2[i] = '\u0000'; i++
                        val mode = strAtZb(sv.pd1, sv.sdlim + i)
                        when {
                            mode.equals("old", ignoreCase = true) -> newFlagValue = 0
                            mode.equals("new", ignoreCase = true) -> newFlagValue = 1
                            mode.equals("append", ignoreCase = true) -> newFlagValue = 2
                            else -> { state = ItLbl.ARG_LIST_ERROR }
                        }
                        if (state != ItLbl.ARG_LIST_ERROR) {
                            val path = strAtZb(tmp2, 0)
                            try {
                                val f = java.io.File(path)
                                when (newFlagValue) {
                                    1 -> { // output new
                                        sv.out_file[k] = java.io.RandomAccessFile(f, "rw")
                                        if (f.exists()) sv.out_file[k]!!.setLength(0L)
                                        sv.tpx = 1
                                    }
                                    2 -> { // output append
                                        sv.out_file[k] = java.io.RandomAccessFile(f, "rw")
                                        sv.out_file[k]!!.seek(sv.out_file[k]!!.length())
                                        sv.tpx = 1
                                    }
                                    else -> { // old
                                        sv.in_file[k] = java.io.RandomAccessFile(f, "r")
                                        sv.tpx = 1
                                    }
                                }
                            } catch (e: Exception) { sv.tpx = 0 }
                            state = ItLbl.OPN1
                        }
                    }
                }
            }
        }
        // ----------------------------------------------------------------
        ItLbl.OPN1 -> {
            if (sv.xd[++sv.xpx] != ',') state = ItLbl.NEXT_CMND
            else { sv.xpx++; state = ItLbl.OPENF }
        }
        // ----------------------------------------------------------------
        ItLbl.NEXT_IF_ARG -> {
            sv.t2 = 1
            sv.ierr = parse_(sv)
            if (sv.ierr != 0) { state = ItLbl.PARSE_ERR }
            else {
                if (atoiAtZb(sv.pd1, sv.sdlim) == 0) {
                    sv.tpx = 0
                    sv.xpx = STR_MAX
                    state = ItLbl.NEXT_CMND
                } else {
                    sv.tpx = 1
                    if (sv.xd[sv.xpx] == ',') { sv.xpx++; state = ItLbl.NEXT_IF_ARG }
                    else state = ItLbl.NEXT_CMND
                }
            }
        }
        // ----------------------------------------------------------------
        ItLbl.WRITEPROC -> {
            var ioKind = 0
            if (sv.xd[sv.xpx] == '!' || sv.xd[sv.xpx] == '#') {
                // new line or page
                ioKind = 1
                if (sv.xd[sv.xpx] != '#') ioKind = 2
                sv.xpx++
                writeIo = ioKind
                state = ItLbl.IOSUB
            } else if (sv.xd[sv.xpx] == '*') { // "*" output
                sv.xpx++
                sv.t2 = 1
                sv.ierr = parse_(sv)
                if (sv.ierr != 0) { state = ItLbl.PARSE_ERR }
                else {
                    val n = atoiAtZb(sv.pd1, sv.sdlim)
                    sv.pd1[sv.sdlim] = n.toChar()
                    sv.pd1[sv.sdlim + 1] = '\u0000'
                    print(sv.pd1[sv.sdlim]) // putchar
                    ioKind = 4
                    writeIo = ioKind
                    state = ItLbl.IOSUB1
                }
            } else if (sv.xd[sv.xpx] == '?') { // tab
                sv.t2 = 1
                sv.xpx++
                sv.ierr = parse_(sv)
                if (sv.ierr != 0) { state = ItLbl.PARSE_ERR }
                else {
                    writeCol = atoiAtZb(sv.pd1, sv.sdlim)
                    writeIo = 3
                    state = ItLbl.IOSUB
                }
            } else {
                sv.t2 = 1
                sv.ierr = parse_(sv)
                if (sv.ierr != 0) { state = ItLbl.PARSE_ERR }
                else { writeIo = 4; state = ItLbl.IOSUB }
            }
        }
        // ----------------------------------------------------------------
        ItLbl.IOSUB -> {
            inout(sv.pd1, sv.sdlim, writeIo, sv.pd1, writeCol)
            state = ItLbl.IOSUB1
        }
        ItLbl.IOSUB1 -> {
            if (sv.xd[sv.xpx] == ',') { sv.xpx++; state = ItLbl.WRITEPROC }
            else if (sv.xd[sv.xpx] == '\u0000' || sv.xd[sv.xpx] == ' ' ||
                     sv.xd[sv.xpx] == TAB) { state = ItLbl.NEXT_CMND }
            else { state = ItLbl.WRITEPROC }
        }
        // ----------------------------------------------------------------
        // GOT0 'g': enter the lbl block with retz=GOTO (vendor falls
        // thru the JOB child into case GOTO)
        ItLbl.GOTO_LBL_ENTRY -> {
            sv.iorg = 1
            retz = 'g' // GOTO
            while (gosub[gsbx][3] == 'f'.code) { // zap FOR stacking
                cmdx = gosub[gsbx][2]
                gsbx--
            }
            forx = 0
            state = ItLbl.LBL
        }
        // ----------------------------------------------------------------
        ItLbl.GOTO_RETURN -> {
            forx = 0
            pd1cur = ibr
            execLevel = 1
            gsbx = 0
            state = ItLbl.NEXT_LINE
        }
        // ----------------------------------------------------------------
        ItLbl.SET -> {
            // set: (also MERGE drops here with MergeFlag=1)
            if (sv.xd[sv.xpx] == '@') { // indirection
                sv.kflg = 1
                sv.t0px = 1
                sv.ierr = parse_(sv)
                if (sv.ierr != 0) { state = ItLbl.PARSE_ERR }
                sv.kflg = 0
            }
            ftmp[0] = '\u0000'
            if (sv.xd[sv.xpx] == '(') { // group LHS
                sv.xpx++
                vd[1] = '\u0000'
                state = ItLbl.S1
            } else {
                sv.t0px = 1
                sv.setname[0] = '\u0000'
                sv.ierr = parse_(sv)
                if (sv.ierr != 0) { state = ItLbl.PARSE_ERR }
                cpyAtZb(vd, 1, sv.v1d, 1)
                state = ItLbl.SET_TAIL
            }
        }
        ItLbl.S1 -> {
            sv.t0px = 1
            sv.ierr = parse_(sv)
            if (sv.ierr != 0) { state = ItLbl.PARSE_ERR }
            else {
                // strcat ftmp, v1d[1] then " "
                var fl = lenAtZb(ftmp, 0)
                cpyAtZb(ftmp, fl, sv.v1d, 1)
                fl = lenAtZb(ftmp, 0)
                ftmp[fl] = ' '; ftmp[fl + 1] = '\u0000'
                sv.xpx++
                if (sv.xd[sv.xpx] != ')' && sv.xd[sv.xpx] != '\u0000') {
                    sv.xpx++
                    state = ItLbl.S1
                } else state = ItLbl.SET_TAIL
            }
        }
        ItLbl.SET_TAIL -> {
            sv.xpx += 2
            sv.t0px = if (mergeFlag != 0) 1 else 0
            sv.t2 = 1
            sv.ierr = parse_(sv)
            if (sv.ierr != 0) { state = ItLbl.PARSE_ERR }
            else {
                if (mergeFlag != 0) {
                    cpyAtZb(mergeRHS, 1, sv.v1d, 1)
                    sv.xpx++
                } else mergeRHS[1] = '\u0000'
                retz = 's' // SET
                k = 0
                if (vd[1] != '\u0000') { state = ItLbl.LHSREF }
                else state = ItLbl.S2
            }
        }
        ItLbl.S2 -> {
            // j = 1; while ((vd[j++] = ftmp[k++]) != ' ');
            j = 1
            while (true) {
                vd[j] = ftmp[k]
                j++
                if (ftmp[k] == ' ') { k++; break }
                k++
            }
            vd[j - 1] = '\u0000'
            state = ItLbl.LHSREF
        }
        ItLbl.SET_RETURN -> {
            if (ftmp[k] != '\u0000') { state = ItLbl.S2 }
            else if (sv.xd[sv.xpx] == ',') { sv.xpx++; state = ItLbl.SET }
            else { state = ItLbl.NEXT_CMND }
        }
        // ----------------------------------------------------------------
        // S E T Q (SQL) / S E T H (SHELL): the vendor :1030-1220 arms
        ItLbl.SHELLSQL -> {
            {
                // shell decode
                if (!ncaseEqAtZb(sv.pd1, pd1Last, "shell")) {
                    prnterrZb(37, sv, pd1cur) // unrecognized command
                    state = ItLbl.ABRT
                } else {
                    if (ncaseEqAtZb(sv.pd1, pd1Last, "shell/p")) pflag = 1
                    if (ncaseEqAtZb(sv.pd1, pd1Last, "shell/g")) pflag = 2
                    var pd1curX = pd1Last + sv.xpx - 2
                    while (sv.pd1[pd1curX] != '\u0000' && sv.pd1[pd1curX] != ' ') pd1curX++
                    if (sv.pd1[pd1curX] != '\u0000') pd1curX++
                    bx = 0
                    // &~ substitution arm folded: indirection through
                    // parse_ into buf (separate scratch walk; the C
                    // continues over the substituted pieces)
                    while (sv.pd1[pd1curX] != '\u0000') {
                        if (sv.pd1[pd1curX] == '&' && sv.pd1[pd1curX + 1] == '~') {
                            pd1curX += 2
                            i = 1 // write at xd[1]
                            while (sv.pd1[pd1curX] != '~' && sv.pd1[pd1curX] != '\u0000') {
                                sv.xd[i] = sv.pd1[pd1curX]; i++; pd1curX++
                            }
                            sv.xd[i] = '\u0000'
                            if (sv.pd1[pd1curX] == '~') pd1curX++
                            sv.t2 = 1
                            sv.xpx = 1
                            sv.ierr = parse_(sv)
                            if (sv.ierr != 0) { state = ItLbl.PARSE_ERR }
                            cpyAtZb(buf, bx, sv.pd1, sv.sdlim)
                            bx = lenAtZb(buf, 0)
                        } else {
                            buf[bx] = sv.pd1[pd1curX]; bx++
                            buf[bx] = '\u0000'
                            pd1curX++
                        }
                    }
                    pd1curX++
                    if (pflag == 0) { // SHELL no options
                        val rc = ProcessBuilder("sh", "-c", strAtZb(buf, 0)).start().waitFor()
                        if (rc < 0) sv.tpx = 0 else sv.tpx = 1 // h013: system() dead arm
                    } else if (pflag == 1) {
                        // SHELL/P: popen read into in_file[6] (h020:
                        // the C FILE* rides the RandomAccessFile seat
                        // via the process-bridge; the tpx ok arm)
                        sv.in_file[6] = popenReadZb(strAtZb(buf, 0))
                        if (sv.in_file[6] == null) sv.tpx = 0 else sv.tpx = 1
                    } else {
                        // SHELL/G: popen write into out_file[6]
                        sv.out_file[6] = popenWriteZb(strAtZb(buf, 0))
                        if (sv.out_file[6] == null) sv.tpx = 0 else sv.tpx = 1
                    }
                    pflag = 0
                    state = ItLbl.NEXT_LINE
                }
            }
        }
        // ----------------------------------------------------------------
        ItLbl.QUIT -> {
            if (gsbx <= 0) { state = ItLbl.RESTORE }
            else {
                if (gosub[gsbx][0] > 0) {
                    lblx = 0 // zap labels
                    // memcpy(pd1p, swp[...], gosub[gsbx][4])
                    val src = swp[gosub[gsbx][0]]
                    if (src != null) {
                        var ci = 0
                        val len = gosub[gsbx][4]
                        while (ci < len) { sv.pd1[1 + ci] = src[ci]; ci++ }
                    }
                    sv.pd1[gosub[gsbx][4] + 1] = '\u0000'
                    swapextent = gosub[gsbx][0]
                    var si = swapextent
                    while (si < SWAP) { swp[si] = null; si++ }
                    if (gosub[gsbx][5] > 0) sv.symlen = gosub[gsbx][5]
                }
                if (forx > 0 && gosub[gsbx][3] == 'f'.code) {
                    cmdx = gosub[gsbx][2]
                    gsbx--
                    forx--
                    sv.xpx = STR_MAX
                    state = ItLbl.NEXT_CMND
                } else state = ItLbl.QUIT1
            }
        }
        // ----------------------------------------------------------------
        ItLbl.QUIT1 -> {
            pd1cur = gosub[gsbx][1]
            if (gsbx <= 0) {
                print("exit\n") // vendor wart: unconditional at quit1 top
                return 1
            }
            cmdxold = cmdx
            cmdx = gosub[gsbx][2]
            if (gosub[gsbx][3] == 'd'.code && breakFlag) sv.xd[1] = '\u0000'
            else cpyAtZb(sv.xd, 1, cmdstack, cmdx)
            sv.pd1len = gosub[gsbx][4].toLong()
            if (forx > 0) { // patch to fix FOR after DO
                sv.xpx = 1
            }
            retz = gosub[gsbx][3].toChar()
            sv.tpx = gosub[gsbx][7].toLong()
            // evaluate return args before killing symbol table
            sv.args[0] = '\u0000'
            var argDispatch = false
            if (retz != 'f' && sv.xd[sv.xpx] != ' ' &&
                sv.xd[sv.xpx] != '\u0000') {
                sv.t2 = 1
                sv.ierr = parse_(sv)
                if (sv.ierr != 0) { state = ItLbl.PARSE_ERR }
                else {
                    // copy processed return arg to state vector
                    cpyAtZb(sv.args, 0, sv.pd1, sv.sdlim)
                    // fdes pipe write: the h013 dead arm (the fork-family
                    // seat; rides the znxtrow precedent)
                    argDispatch = true
                }
            } else argDispatch = true
            if (argDispatch) {
                if (retz != 'f' && retz != 'x') {
                    execLevNew[execLevel] = 0 // prevent 2 trips
                    symFreeZb(1, sv)
                }
                gsbx--
                if (forx > 0 && retz == 'f') { state = ItLbl.FOR_END }
                else {
                    if (retz == 'd' && execLevel > 1) {
                        if (execLevNew[execLevel] != 0) {
                            symFreeZb(1, sv) // undo New
                        }
                        execLevel--
                    }
                    if (gosub[gsbx + 1][6] != 0) {
                        execLevel = gosub[gsbx + 1][6]
                        gosub[gsbx + 1][6] = 0
                    }
                    // old - permitted extra blank at EOL to cause loop
                    if (sv.xd[sv.xpx] == '\u0000' ||
                        (sv.xd[sv.xpx] == ' ' && sv.xd[sv.xpx + 1] == ' ')) {
                        state = ItLbl.NEXT_CMND
                    } else if (retz == 'x') state = ItLbl.XECUTE_RETURN
                    else state = ItLbl.DO_RET1
                }
            }
        }
        // ----------------------------------------------------------------
        ItLbl.XECUTE -> {
            sv.t2 = 1
            sv.ierr = parse_(sv)
            if (sv.ierr != 0) { state = ItLbl.PARSE_ERR }
            else {
                ftmp[0] = TAB
                cpyAtZb(ftmp, 1, sv.pd1, sv.sdlim)
                if (sv.xd[sv.xpx] == ':') {
                    sv.xpx++
                    sv.t2 = 1
                    sv.ierr = parse_(sv)
                    if (sv.ierr != 0) { state = ItLbl.PARSE_ERR }
                    else {
                        k = atoiAtZb(sv.pd1, sv.sdlim)
                        if (k == 0) {
                            if (sv.xd[sv.xpx] == ',') { sv.xpx++; state = ItLbl.XECUTE }
                            else state = ItLbl.NEXT_CMND
                        } else state = ItLbl.XECUTE_BODY
                    }
                } else state = ItLbl.XECUTE_BODY
            }
        }
        ItLbl.XECUTE_BODY -> {
            // move line to be executed into code space above
            l = sv.pd1len.toInt()
            cpyAtZb(sv.pd1, sv.pd1len.toInt() + 2, ftmp, 0)
            sv.pd1len = (l + 2 + lenAtZb(ftmp, 0)).toLong()
            gsbx++
            if (gsbx >= GOSUBMAX) { state = ItLbl.STACK_OVERFLOW }
            else {
                gosub[gsbx][0] = 0 // swap origin
                gosub[gsbx][1] = pd1cur // restore pd1cur
                gosub[gsbx][2] = cmdx // command stack address
                cpyAtZb(cmdstack, cmdx, sv.xd, sv.xpx)
                cmdx = cmdx + lenAtZb(sv.xd, sv.xpx) + 1
                gosub[gsbx][4] = l // restore pd1len
                gosub[gsbx][3] = 'x'.code // XECUTE
                pd1cur = l + 2
                state = ItLbl.NEXT_LINE_XECUTE
            }
        }
        ItLbl.XECUTE_RETURN -> {
            if (sv.xd[sv.xpx] == ' ' || sv.xd[sv.xpx] == '\u0000') {
                state = ItLbl.NEXT_CMND
            } else state = ItLbl.XECUTE
        }
        // ----------------------------------------------------------------
        ItLbl.FOR_PUSH -> {
            // case FOR: stack set up
            forx++
            if (forx >= FORMAX) {
                print("OVERFLOW\n") // vendor quirk kept
                state = ItLbl.STACK_OVERFLOW
            } else {
                forit[forx] = 0
                forlt[forx] = 0
                forv[forx][0] = '\u0000'
                if (++gsbx >= GOSUBMAX) { state = ItLbl.STACK_OVERFLOW }
                else {
                    gosub[gsbx][0] = 0 // swap
                    gosub[gsbx][1] = pd1cur
                    gosub[gsbx][2] = cmdx
                    gosub[gsbx][3] = 'f'.code // FOR
                    gosub[gsbx][4] = sv.pd1len.toInt()
                    if (sv.xd[sv.xpx] == ' ') { // argless
                        putAtZb(forv[forx], 0, "_dummy")
                        putAtZb(forin[forx], 0, "0")
                        putAtZb(forlv[forx], 0, "100")
                        sv.symflg = 0 // store
                        sym_(0, "_dummy", "1".toCharArray(), sv) // FOR argless vendor quirk
                        forit[forx] = 1
                        forArgGate = true
                        state = ItLbl.ARGLESS
                    } else {
                        // find local variable
                        sv.t0px = 1
                        sv.ierr = parse_(sv)
                        if (sv.ierr != 0) { state = ItLbl.PARSE_ERR }
                        else {
                            sv.xpx += 2
                            cpyAtZb(forv[forx], 0, sv.v1d, 1)
                            sv.symflg = 0 // store
                            dosave[0] = '\u0000'
                            sym_(0, strAtZb(forv[forx], 0), dosave, sv)
                            state = ItLbl.NXT_ARG
                        }
                    }
                }
            }
        }
        // ----------------------------------------------------------------
        ItLbl.NXT_ARG -> {
            forit[forx] = 0
            forlt[forx] = 0
            sv.t2 = 1
            if (sv.xd[sv.xpx] == '\u0024') { // function
                forit[forx] = 1
                forlt[forx] = 1
                forlv[forx][0] = '\u0000' // function
                cpyAtZb(forin[forx], 0, sv.xd, sv.xpx)
            }
            sv.ierr = parse_(sv)
            if (sv.ierr != 0) { state = ItLbl.PARSE_ERR }
            else {
                forit[forx] = 0 // initial value
                cpyAtZb(ftmp, 0, sv.pd1, sv.sdlim)
                if (sv.xd[sv.xpx] == ',' || sv.xd[sv.xpx] == ' ' ||
                    sv.xd[sv.xpx] == '\u0000') { state = ItLbl.INIT_LOOP_VAR }
                else if (sv.xd[sv.xpx] != ':') {
                    ernbr = 14; state = ItLbl.ARG_LIST_ERROR
                } else {
                    // increment
                    sv.symflg = 0 // store
                    sym_(0, strAtZb(forv[forx], 0), ftmp, sv)
                    sv.t2 = 1
                    sv.xpx++
                    val xdtmp = CharArray(STR_MAX)
                    var xdtmpx = sv.xpx
                    sv.ierr = parse_(sv)
                    if (sv.ierr != 0) { state = ItLbl.PARSE_ERR }
                    else {
                        xdtmpx = sv.xpx - xdtmpx
                        xdtmp[xdtmpx] = '\u0000'
                        forit[forx] = 1 // increment
                        cpyAtZb(forin[forx], 0, xdtmp, 0)
                        if (sv.xd[sv.xpx] == ',' || sv.xd[sv.xpx] == '\u0000' ||
                            sv.xd[sv.xpx] == ' ' || sv.xd[sv.xpx] == TAB) {
                            state = ItLbl.INIT_LOOP_VAR
                        } else if (sv.xd[sv.xpx] != ':') {
                            ernbr = 14; state = ItLbl.ARG_LIST_ERROR
                        } else {
                            // limit
                            sv.t2 = 1
                            sv.xpx++
                            sv.ierr = parse_(sv)
                            if (sv.ierr != 0) { state = ItLbl.PARSE_ERR }
                            else {
                                forlt[forx] = 1 // limit
                                cpyAtZb(forlv[forx], 0, sv.pd1, sv.sdlim)
                                if (sv.xd[sv.xpx] == ',' || sv.xd[sv.xpx] == ' ' ||
                                    sv.xd[sv.xpx] == '\u0000' ||
                                    sv.xd[sv.xpx] == TAB) { state = ItLbl.INIT_LOOP_VAR }
                                else { ernbr = 14; state = ItLbl.ARG_LIST_ERROR }
                            }
                        }
                    }
                }
            }
        }
        // ----------------------------------------------------------------
        ItLbl.INIT_LOOP_VAR -> {
            sv.symflg = 0 // store
            // set value
            sym_(0, strAtZb(forv[forx], 0), ftmp, sv)
            i = 0
            if (forin[forx][0] == '\u0024') {
                i = if (lenAtZb(ftmp, 0) == 0) 1 else 0
                forit[forx] = 1
                state = ItLbl.ARGLESS
            } else {
                if (forlt[forx] == 1) {
                    if (numcompZb(strAtZb(forin[forx], 0), "0") >= 0) {
                        if (numcompZb(strAtZb(ftmp, 0), strAtZb(forlv[forx], 0)) > 0) i = 1
                    } else {
                        if (numcompZb(strAtZb(ftmp, 0), strAtZb(forlv[forx], 0)) < 0) i = 1
                    }
                }
                forI = i
                state = ItLbl.ARGLESS
            }
            forI = i
            forArgGate = false
        }
        // ----------------------------------------------------------------
        ItLbl.ARGLESS -> {
            // argvless label shared by the argless FOR arm
            if (!forArgGate) forI = i
            cpyAtZb(cmdstack, cmdx, sv.xd, sv.xpx)
            cmdx = cmdx + lenAtZb(sv.xd, sv.xpx) + 1
            state = ItLbl.BYP
        }
        // ----------------------------------------------------------------
        ItLbl.BYP -> {
            while (sv.xd[sv.xpx] != '\u0000' && sv.xd[sv.xpx] != ' ') {
                sv.t2 = 1
                sv.xpx++
                parse_(sv) // the C ignores the return here (vendor wart)
            }
            if (forI == 0) { state = ItLbl.NEXT_CMND } else { state = ItLbl.QUIT1 }
        }
        // ----------------------------------------------------------------
        ItLbl.FOR_END -> {
            // end of for return
            if (forit[forx] != 1) { state = ItLbl.FOR_QUIT }
            else {
                // increment loop variable
                val itmp = CharArray(STR_MAX)
                sv.symflg = 1 // retrieve
                sym_(11, strAtZb(forv[forx], 0), itmp, sv)
                val old = CharArray(STR_MAX)
                var oldxpx = sv.xpx
                cpyAtZb(old, 1, sv.xd, 1)
                sv.t2 = 1
                if (old[0] == '\u0000') old[0] = sv.xd[0] // keep xd[0]
                cpyAtZb(sv.xd, 0, forin[forx], 0)
                sv.xpx = 0
                sv.ierr = parse_(sv)
                if (sv.ierr != 0) { state = ItLbl.PARSE_ERR }
                else {
                    // increment amount now at pd1[sdlim]
                    val dsve = strAtZb(sv.pd1, sv.sdlim)
                    // restore xd and xpx
                    cpyAtZb(sv.xd, 0, old, 0)
                    if (old[0] == '\u0000') sv.xd[0] = '\u0000'
                    sv.xpx = oldxpx
                    if (forin[forx][0] != '\u0024') {
                        val a2 = CharArray(STR_MAX); putAtZb(a2, 0, strAtZb(itmp, 0))
                        val b2 = CharArray(STR_MAX); putAtZb(b2, 0, dsve)
                        add(a2, b2, ftmp) // add(itmp, dsve, ftmp)
                    } else {
                        putAtZb(ftmp, 0, dsve)
                    }
                    state = ItLbl.FOR_END2
                }
            }
        }
        ItLbl.FOR_END2 -> {
            if (breakFlag) {
                breakFlag = false
                state = ItLbl.FOR_QUIT
            } else if (forlt[forx] != 1) {
                sv.symflg = 0 // store
                sym_(0, strAtZb(forv[forx], 0), ftmp, sv)
                gsbx++
                if (gsbx >= GOSUBMAX) { state = ItLbl.STACK_OVERFLOW }
                else {
                    cmdx = cmdxold
                    if (sv.xd[sv.xpx] == ',') { forI = 0; state = ItLbl.BYP }
                    else state = ItLbl.NEXT_CMND
                }
            } else {
                // limit test
                sv.symflg = 0 // store
                sym_(0, strAtZb(forv[forx], 0), ftmp, sv)
                var quitLoop = false
                if (forin[forx][0] == '\u0024') {
                    if (forlv[forx][0] == '\u0000') { // null string limit
                        if (lenAtZb(ftmp, 0) == 0) quitLoop = true
                    } else {
                        if (strAtZb(ftmp, 0).compareTo(strAtZb(forlv[forx], 0)) >= 0) quitLoop = true
                    }
                } else if (numcompZb(strAtZb(forin[forx], 0), "0") >= 0) {
                    if (numcompZb(strAtZb(ftmp, 0), strAtZb(forlv[forx], 0)) > 0) quitLoop = true
                } else {
                    if (numcompZb(strAtZb(ftmp, 0), strAtZb(forlv[forx], 0)) < 0) quitLoop = true
                }
                if (quitLoop) { state = ItLbl.FOR_QUIT }
                else {
                    gsbx++
                    if (gsbx >= GOSUBMAX) { state = ItLbl.STACK_OVERFLOW }
                    else {
                        cmdx = cmdxold
                        if (sv.xd[sv.xpx] == ',') { forI = 0; state = ItLbl.BYP }
                        else state = ItLbl.NEXT_CMND
                    }
                }
            }
        }
        // ----------------------------------------------------------------
        ItLbl.FOR_QUIT -> {
            if (sv.xd[sv.xpx] == ',') {
                sv.xpx++
                gsbx++
                if (gsbx >= GOSUBMAX) { state = ItLbl.STACK_OVERFLOW }
                else state = ItLbl.NXT_ARG
            } else {
                forx--
                sv.xpx = STR_MAX
                state = ItLbl.NEXT_CMND
            }
        }
        // ----------------------------------------------------------------
        ItLbl.HALT_T -> {
            if ((sv.xd[sv.xpx] == ' ' || sv.xd[sv.xpx] == '\u0000') &&
                sv.xd[p2px + 1].uppercaseChar() != 'T') {
                Mltoa(getpidZb(), tmp2)
                if (sv.LockDone != 0) cleanLocks(tmp2)
                Mglobal(IP_CLOSE, tmp2, tmp3, sv) // Close Globals
                kotlin.system.exitProcess(0) // _exit(EXIT_SUCCESS)
            }
            if (sv.xd[p2px + 1].uppercaseChar() == 'T') { // html command
                var pd1curX = pd1Last + sv.xpx - 2
                while (sv.pd1[pd1curX] != '\u0000' && sv.pd1[pd1curX] != ' ') pd1curX++
                if (sv.pd1[pd1curX] != '\u0000') pd1curX++
                while (sv.pd1[pd1curX] != '\u0000') {
                    if (sv.pd1[pd1curX] == '&' && sv.pd1[pd1curX + 1] == '~') {
                        pd1curX += 2
                        i = 1
                        while (sv.pd1[pd1curX] != '~' && sv.pd1[pd1curX] != '\u0000') {
                            sv.xd[i] = sv.pd1[pd1curX]; i++; pd1curX++
                        }
                        sv.xd[i] = '\u0000'
                        if (sv.pd1[pd1curX] == '~') pd1curX++
                        sv.t2 = 1
                        sv.xpx = 1
                        sv.ierr = parse_(sv)
                        if (sv.ierr != 0) { state = ItLbl.PARSE_ERR }
                        else print(strAtZb(sv.pd1, sv.sdlim))
                    } else if (sv.pd1[pd1curX] == '&' && sv.pd1[pd1curX + 1] == '!') {
                        pd1curX += 2
                        print('\n') // putchar('\n')
                    } else {
                        print(sv.pd1[pd1curX])
                        pd1curX++
                    }
                }
                state = ItLbl.NEXT_LINE
            } else { state = ItLbl.HANG }
        }
        // ----------------------------------------------------------------
        ItLbl.HANG -> {
            sv.t2 = 1
            sv.ierr = parse_(sv)
            if (sv.ierr != 0) { state = ItLbl.PARSE_ERR }
            else {
                j = atoiAtZb(sv.pd1, sv.sdlim)
                if (j > 0) Thread.sleep(j * 1000L) // sleep(j)
                if (sv.xd[sv.xpx] == ',') { sv.xpx++; state = ItLbl.HANG }
                else state = ItLbl.NEXT_CMND
            }
        }
        // ----------------------------------------------------------------
        ItLbl.DO_DECLARE -> {
            // case DO (or DATABASE)
            if (sv.xd[p2px + 1].uppercaseChar() == 'E') { // DECLARE
                state = ItLbl.NEXT_LINE // ignore
            } else if (sv.xd[p2px + 1].uppercaseChar() == 'A') { // DATABASE
                sv.t2 = 1
                while (sv.xd[sv.xpx] != '\u0000') {
                    if (sv.xd[sv.xpx] != ' ') break
                    sv.xpx++
                }
                sv.ierr = parse_(sv)
                if (sv.ierr != 0) { state = ItLbl.PARSE_ERR }
                else {
                    if (lenAtZb(sv.pd1, sv.sdlim) > 1000) {
                        ernbr = 26; state = ItLbl.FILE_ERROR
                    } else {
                        val dbn = strAtZb(sv.pd1, sv.sdlim)
                        putAtZb(sv.UDAT, 0, dbn + ".dat")
                        putAtZb(sv.UKEY, 0, dbn + ".key")
                        state = ItLbl.NEXT_LINE
                    }
                }
            } else { state = ItLbl.DO_RET1 }
        }
        // ----------------------------------------------------------------
        ItLbl.DO_RET1 -> {
            argless = 0
            newFlagged = 0
            if (sv.xd[sv.xpx] == ' ' || sv.xd[sv.xpx] == '\u0000') {
                val tmp = CharArray(1024)
                putAtZb(tmp, 0, "+0  ")
                execLevel++
                if (execLevel >= EXECLEVS) { ernbr = 32; state = ItLbl.STACK_OVERFLOW }
                else {
                    execLevNew[execLevel] = 0
                    var tl = lenAtZb(tmp, 0)
                    val rest = strAtZb(sv.xd, sv.xpx)
                    var ri = 0
                    while (ri < rest.length) { tmp[tl + ri] = rest[ri]; ri++ }
                    tmp[tl + ri] = '\u0000'
                    cpyAtZb(sv.xd, 1, tmp, 0)
                    sv.xpx = 1
                    argless = 1
                }
            }
            if (state != ItLbl.STACK_OVERFLOW) {
                pd1hold1 = sv.pd1len.toInt()
                pd1hold2 = pd1cur
                sv.iorg = 1
                shold = 0
                retz = 'd' // DO
                state = ItLbl.LBL
            }
        }
        // ----------------------------------------------------------------
        ItLbl.DO_RET -> {
            if (++gsbx >= GOSUBMAX) { state = ItLbl.STACK_OVERFLOW }
            else {
                if (shold > 0) gosub[gsbx][0] = shold else gosub[gsbx][0] = 0
                gosub[gsbx][1] = pd1hold2
                gosub[gsbx][2] = cmdx
                gosub[gsbx][8] = newFlagged
                cpyAtZb(cmdstack, cmdx, sv.xd, sv.xpx)
                cmdx = cmdx + lenAtZb(sv.xd, sv.xpx) + 1
                gosub[gsbx][3] = 'd'.code // DO
                gosub[gsbx][4] = pd1hold1
                pd1cur = ibr
                gosub[gsbx][5] = 0
                gosub[gsbx][7] = sv.tpx.toInt()
                if (argless == 0) {
                    gosub[gsbx][6] = execLevel
                    execLevel = 1
                }
                if (symInc == 0) { // flag: inc was done already
                    sv._Sym++
                    sv.start[sv._Sym] = null
                }
                state = ItLbl.NEXT_LINE
            }
        }
        // ----------------------------------------------------------------
        ItLbl.NEW_AGAIN -> {
            sv.nflg = 0
            if (sv.xd[sv.xpx] == ' ' || sv.xd[sv.xpx] == '\u0000' ||
                sv.xd[sv.xpx] == TAB) {
                // new except none
                sv.symflg = 7
                sv.nstart = null // list freed in sym_()
                sym_(7, "", CharArray(0), sv)
                sv.nstart = null // list freed in sym_()
                execLevNew[execLevel] = 1
                state = ItLbl.NEXT_CMND
            } else {
                if (sv.xd[sv.xpx] == '@') { // New indirect
                    sv.kflg = 1
                    sv.t0px = 1
                    sv.ierr = parse_(sv)
                    if (sv.ierr != 0) { state = ItLbl.PARSE_ERR }
                    else {
                        sv.kflg = 0
                        if (sv.xd[sv.xpx] != '(') { // new specific
                            sv.t0px = 1
                            sv.ierr = parse_(sv)
                            if (sv.ierr != 0) { state = ItLbl.PARSE_ERR }
                            else {
                                sym_(100, strAtZb(sv.v1d, 1), CharArray(0), sv)
                                state = ItLbl.NEW_TAIL
                            }
                        } else state = ItLbl.NEW2
                    }
                } else if (sv.xd[sv.xpx] != '(') { // New specific
                    sv.t0px = 1
                    sv.ierr = parse_(sv)
                    if (sv.ierr != 0) { state = ItLbl.PARSE_ERR }
                    else {
                        sym_(100, strAtZb(sv.v1d, 1), CharArray(0), sv)
                        state = ItLbl.NEW_TAIL
                    }
                } else { // New all locals except: linked list
                    sv.nstart = null
                    sv.xpx++
                    state = ItLbl.NEW2
                }
            }
        }
        ItLbl.NEW2 -> {
            sv.t0px = 1
            sv.ierr = parse_(sv)
            if (sv.ierr != 0) { state = ItLbl.PARSE_ERR }
            else {
                keyfixC(sv.v1d) // in case its an array (v1d[1] seat)
                val nm = strAtZb(sv.v1d, 1)
                sv.nstart = Nmes(nm, sv.nstart)
                sv.xpx++
                if (sv.xd[sv.xpx] != ')' && sv.xd[sv.xpx] != '\u0000') {
                    sv.xpx++
                    state = ItLbl.NEW2
                } else {
                    sv.symflg = 7 // new except
                    sym_(7, "", CharArray(0), sv)
                    sv.nstart = null // list freed in sym_()
                    execLevNew[execLevel] = 1
                    state = ItLbl.NEW_TAIL
                }
            }
        }
        ItLbl.NEW_TAIL -> {
            sv.xpx++
            if (sv.xd[sv.xpx] == ',') { sv.xpx++; state = ItLbl.NEW_AGAIN }
            else state = ItLbl.NEXT_CMND
        }
        // ----------------------------------------------------------------
        ItLbl.KILL_AGAIN -> {
            if (sv.xd[sv.xpx] == ' ' || sv.xd[sv.xpx] == '\u0000' ||
                sv.xd[sv.xpx] == TAB) {
                // kill all local variables
                sv.symflg = 4
                sym_(4, "", CharArray(0), sv)
                state = ItLbl.KILL_TAIL
            } else if (sv.xd[sv.xpx] == '@') { // kill indirect
                sv.kflg = 1
                sv.t0px = 1
                sv.ierr = parse_(sv)
                if (sv.ierr != 0) { state = ItLbl.PARSE_ERR }
                else { sv.kflg = 0; state = ItLbl.KILL_SPECIFIC }
            } else { state = ItLbl.KILL_SPECIFIC }
        }
        ItLbl.KILL_SPECIFIC -> {
            // kill specific - local or global
            if (sv.xd[sv.xpx] != '(') {
                sv.t0px = 1
                sv.ierr = parse_(sv)
                if (sv.ierr != 0) { state = ItLbl.PARSE_ERR }
                else {
                    if (sv.v1d[1] != '^') { // kill a local
                        sv.symflg = 2 // kill
                        sym_(2, strAtZb(sv.v1d, 1), CharArray(0), sv)
                        state = ItLbl.KILL_TAIL
                    } else { // globals
                        keyfixC(sv.v1d) // the v1d[1] seat
                        k = lenAtZb(sv.v1d, 1) + 1
                        if (sv.v1d[k - 1].code != 1) {
                            sv.v1d[k] = '\u0001'; k++
                        }
                        sv.v1d[k] = '\u0000'
                        k--
                        cpyAtZb(tmp2, 0, sv.v1d, 1) // kill first
                        Mglobal(IP_GKILL, sv.v1d, sv.bd, sv)
                        // the XNEXT kill-others loop is the
                        // NOT-SQLITE arm (vendor :1841 #if !(SQLITE));
                        // this build skips it (h018)
                        state = ItLbl.KILL_TAIL
                    }
                }
            } else { // kill all locals except...
                sv.nstart = null
                sv.xpx++
                state = ItLbl.KILL2
            }
        }
        ItLbl.KILL2 -> {
            sv.t0px = 1
            sv.ierr = parse_(sv)
            if (sv.ierr != 0) { state = ItLbl.PARSE_ERR }
            else {
                keyfixC(sv.v1d) // v1d[1] seat
                val nm = strAtZb(sv.v1d, 1)
                sv.nstart = Nmes(nm, sv.nstart)
                sv.xpx++
                if (sv.xd[sv.xpx] != ')' && sv.xd[sv.xpx] != '\u0000') {
                    sv.xpx++
                    state = ItLbl.KILL2
                } else {
                    sv.symflg = 5
                    sym_(5, "", CharArray(0), sv)
                    sv.nstart = null
                    state = ItLbl.KILL_TAIL
                }
            }
        }
        ItLbl.KILL_TAIL -> {
            sv.xpx++
            if (sv.xd[sv.xpx] == ',') { sv.xpx++; state = ItLbl.KILL_AGAIN }
            else state = ItLbl.NEXT_CMND
        }
        // ----------------------------------------------------------------
        ItLbl.NEXT_CLOSE_ARG -> {
            // case FILECLOSE (the p2px/mode decode already seated at
            // the dispatcher; the 'o' arm routes QUIT pre-label)
            sv.t2 = 1
            sv.ierr = parse_(sv)
            if (sv.ierr != 0) { state = ItLbl.PARSE_ERR }
            else {
                i = atoiAtZb(sv.pd1, sv.sdlim)
                if (sv.in_file[i] == null && sv.out_file[i] == null) {
                    ernbr = 26; state = ItLbl.FILE_ERROR
                } else {
                    try { sv.in_file[i]?.close() } catch (e: Exception) { }
                    try { sv.out_file[i]?.close() } catch (e: Exception) { }
                    sv.in_file[i] = null
                    sv.out_file[i] = null
                    if (sv.xd[sv.xpx] != ',') { state = ItLbl.NEXT_CMND }
                    else { sv.xpx++; state = ItLbl.NEXT_CLOSE_ARG }
                }
            }
        }
        // ----------------------------------------------------------------
        ItLbl.LHSREF -> {
            if (vd[1] == '^') {
                cpyAtZb(sv.v1d, 0, vd, 1)
                cpyAtZb(sv.bd, 0, sv.pd1, sv.sdlim)
                if (mergeFlag == 0) Mglobal(IP_STORE, sv.v1d, sv.bd, sv)
                else {
                    val rhs = CharArray(STR_MAX)
                    cpyAtZb(rhs, 0, mergeRHS, 1)
                    mergeZb(sv.v1d, rhs, sv)
                }
                state = ItLbl.LHSREF1
            } else {
                if (vd[1] == '\u0024' && (vd[2] == 'p' || vd[2] == 'P')) {
                    // setpiece
                    cpyAtZb(tmp3, 0, sv.pd1, sv.sdlim)
                    cpyAtZb(sv.v1d, 1, vd, 1)
                    sv.setpiece = 1
                    fcn(sv)
                    sv.setpiece = 0
                    state = ItLbl.LHSREF1
                } else if (vd[1] == '\u0024' && (vd[2] == 'e' || vd[2] == 'E')) {
                    cpyAtZb(tmp3, 0, sv.pd1, sv.sdlim)
                    cpyAtZb(sv.v1d, 1, vd, 1)
                    sv.setpiece = 1
                    fcn(sv)
                    sv.setpiece = 0
                    state = ItLbl.LHSREF1
                } else if (vd[1] == '\u0024' && (vd[2] == 'z' || vd[2] == 'Z')) {
                    cpyAtZb(tmp3, 0, sv.pd1, sv.sdlim)
                    cpyAtZb(sv.v1d, 1, vd, 1)
                    sv.setpiece = 1
                    fcn(sv)
                    sv.setpiece = 0
                    state = ItLbl.LHSREF1
                } else {
                    sv.symflg = 0 // store
                    sym_(0, strAtZb(vd, 1), CharArray(0), sv)
                    // (sym_ data carrier: the pd1[sdlim] body passes via
                    // the sv arena; the LHSref store-shape seat)
                    state = ItLbl.LHSREF1
                }
            }
        }
        ItLbl.LHSREF1 -> {
            if (retz == 'r') { state = ItLbl.CONT_READ } // READ
            else { state = ItLbl.SET_RETURN } // SET
        }
        // ----------------------------------------------------------------
        ItLbl.LBL -> {
            // remote block to process DO and GOTO arguments
            symInc = 0
            if (sv.xd[sv.xpx] == '\u0000') { state = ItLbl.LBL_ERROR }
            else {
                if (sv.xd[sv.xpx] == '@') { // indirect argument
                    sv.t0px = 1
                    sv.t2 = 2
                    sv.ierr = parse_(sv)
                    if (sv.ierr != 0) { state = ItLbl.LBL_ERROR }
                    else {
                        if (sv.t0px == 0) { // copy label part to dosave
                            var ix = 1
                            while (sv.v1d[ix] != '\u0000' &&
                                   sv.v1d[ix].code < 127) ix++
                            var dc = 0
                            while (dc < ix - 1) { dosave[dc] = sv.v1d[1 + dc]; dc++ }
                            dosave[ix - 1] = '\u0000'
                            if (sv.v1d[ix] != '\u0000') { // args follow label
                                cpyAtZb(sv.args, 0, sv.v1d, ix + 1)
                            }
                        } else {
                            cpyAtZb(dosave, 0, sv.pd1, sv.sdlim)
                        }
                        if (sv.t0px == 0) sv.xpx++
                        state = ItLbl.LBL_ARGS
                    }
                } else if (sv.xd[sv.xpx] == '\u0024') { // builtin function
                    sv.t2 = 1
                    sv.ierr = parse_(sv)
                    if (sv.ierr != 0) { state = ItLbl.PARSE_ERR }
                    else state = ItLbl.NEXT_CMND
                } else {
                    // extract label and offset from xd
                    i = 0
                    while (sv.xd[sv.xpx] != '\u0000') {
                        if (sv.xd[sv.xpx] == ',' || sv.xd[sv.xpx] == ' ' ||
                            sv.xd[sv.xpx] == '+' || sv.xd[sv.xpx] == '(' ||
                            sv.xd[sv.xpx] == '^' || sv.xd[sv.xpx] == ':') break
                        dosave[i] = sv.xd[sv.xpx]; i++; sv.xpx++
                    }
                    dosave[i] = '\u0000'
                    state = ItLbl.LBL_ARGS
                }
            }
        }
        ItLbl.LBL_ARGS -> {
            // handles do xxx(a,b,c)
            if (sv.xd[sv.xpx] == '(' || sv.xd[sv.xpx].code == IP_OPEN_PAREN) {
                if (sv.xd[sv.xpx + 1] == ')') { // empty argument list ()
                    sv.args[0] = '\u0000'
                    sv.args[1] = '\u0000'
                    sv.args[2] = '\u0000'
                    sv.xpx += 2
                } else {
                    sv.t0px = 1
                    sv.t2 = 1
                    sv.xpx--
                    sv.xd[sv.xpx] = '\u0024'
                    sv.ierr = parse_(sv)
                    if (sv.ierr != 0) { state = ItLbl.PARSE_ERR }
                    else {
                        sv.xpx++
                        cpyAtZb(sv.args, 0, sv.v1d, 3)
                    }
                }
                if (state != ItLbl.PARSE_ERR) state = ItLbl.LBL_PLUS
            } else state = ItLbl.LBL_PLUS
        }
        ItLbl.LBL_PLUS -> {
            var k2 = -1
            if (sv.xd[sv.xpx] == '+') {
                sv.t2 = 2
                sv.ierr = parse_(sv)
                if (sv.ierr != 0) { state = ItLbl.PARSE_ERR }
                else {
                    k2 = atoiAtZb(sv.pd1, sv.sdlim)
                    if (k2 < 0) { state = ItLbl.LBL_ERROR }
                    else { lblK = k2; state = ItLbl.LBL_FILE }
                }
            } else { lblK = k2; state = ItLbl.LBL_FILE }
        }
        ItLbl.LBL_FILE -> {
            k = lblK
            sv.v1d[1] = '\u0000'
            if (sv.xd[sv.xpx] != ' ' && sv.xd[sv.xpx] != ',' &&
                sv.xd[sv.xpx] != '\u0000' && sv.xd[sv.xpx] != ':') {
                sv.t0px = 1
                sv.t2 = 1
                if (sv.xd[sv.xpx] == '^' && sv.xd[sv.xpx + 1] == '@') {
                    sv.xpx++
                    fileflg = 1
                } else fileflg = 0
                sv.ierr = parse_(sv)
                if (sv.ierr != 0) { state = ItLbl.FILE_ERROR }
                else {
                    if (fileflg != 0) {
                        cpyAtZb(ftmp, 0, sv.v1d, 1)
                        sv.v1d[1] = '^'
                        cpyAtZb(sv.v1d, 2, ftmp, 0)
                    }
                    if (sv.xd[sv.xpx] != ':') sv.xpx++
                    state = ItLbl.LBL_COLON
                }
            } else state = ItLbl.LBL_COLON
        }
        ItLbl.LBL_COLON -> {
            if (sv.xd[sv.xpx] == ':') {
                sv.xpx++
                sv.t2 = 1
                sv.t0px = 0
                cpyAtZb(ftmp, 0, sv.v1d, 1)
                sv.ierr = parse_(sv)
                if (sv.ierr != 0) { state = ItLbl.POST_COND_ERROR }
                else {
                    cpyAtZb(sv.v1d, 1, ftmp, 0)
                    if (atoiAtZb(sv.pd1, sv.sdlim) == 0) {
                        if (sv.xd[sv.xpx] == ',') { sv.xpx++; state = ItLbl.LBL }
                        else state = ItLbl.NEXT_CMND
                    } else state = ItLbl.LBL_LOAD
                }
            } else state = ItLbl.LBL_LOAD
        }
        ItLbl.LBL_LOAD -> {
            if (dosave[0] == '^') {
                cpyAtZb(sv.v1d, 1, dosave, 0)
                dosave[0] = '\u0000'
            }
            if (sv.v1d[1] != '\u0000') {
                if (retz == 'd') { // DO
                    sv.iorg = 1
                    shold = sv.pd1len.toInt()
                    if (shold > 0) {
                        val snap = CharArray(shold)
                        var ci = 0
                        while (ci < shold) { snap[ci] = sv.pd1[1 + ci]; ci++ }
                        swp[swapextent] = snap
                        shold = swapextent
                        swapextent++
                        if (swapextent >= SWAP) { state = ItLbl.STACK_OVERFLOW }
                    }
                    if (state != ItLbl.STACK_OVERFLOW) {
                        // extract file name from line - avoids args
                        var ix = 0
                        val ftmp1 = CharArray(512)
                        while (ix < 511 &&
                               sv.v1d[ix + 2] != '\u0000' &&
                               sv.v1d[ix + 2].code in 32..126) {
                            ftmp1[ix] = sv.v1d[ix + 2]; ix++
                        }
                        ftmp1[ix] = '\u0000'
                        // load file
                        readf_Zb(ftmp1, sv.iorg, sv)
                        if (ftmp1[0] == '\u0000') { state = ItLbl.FILE_ERROR }
                        else {
                            // remove file name leaving args if any
                            var x = 1
                            var y = ix + 2
                            while (sv.v1d[y] != '\u0000') {
                                sv.v1d[x] = sv.v1d[y]; x++; y++
                            }
                            sv.v1d[x] = '\u0000'
                            // copy already processed args to arg string
                            cpyAtZb(sv.args, 0, sv.v1d, 2)
                            sv.LineNumber = 0
                            lblx = 0
                            if (sv.v1d[2] == '\u0000') { state = ItLbl.FILE_ERROR }
                            else state = ItLbl.LBL_SEARCH
                        }
                    }
                } else state = ItLbl.LBL_SEARCH
            } else state = ItLbl.LBL_SEARCH
        }
        ItLbl.LBL_SEARCH -> {
            // search for label in code (and the label cache)
            ibr = 0
            var foundLabel = false
            while (ibr < lblx) {
                if (eqAtZb(lbls[ibr], 0, strAtZb(dosave, 0))) {
                    ibr = lbloff[ibr]
                    foundLabel = true
                    break
                }
                ibr++
            }
            if (!foundLabel) {
                ibr = 1
                if (dosave[0] != '\u0000') {
                    var searchDone = false
                    while (ibr < sv.pd1len) {
                        if (sv.pd1[ibr] == TAB || sv.pd1[ibr] == ' ' ||
                            sv.pd1[ibr] == '#' || sv.pd1[ibr] == ';') {
                            // no label: scan for end of line
                            while (sv.pd1[ibr] != '\u0000' && ibr < sv.pd1len) ibr++
                        } else { // something in column 1
                            j = 0
                            while (sv.pd1[ibr] == dosave[j] && dosave[j] != '\u0000') {
                                j++; ibr++
                            }
                            if ((sv.pd1[ibr] == TAB || sv.pd1[ibr] == ' ' ||
                                 sv.pd1[ibr] == '(' || sv.pd1[ibr] == '\u0000') &&
                                dosave[j] == '\u0000') {
                                // label found; add to label cache if room
                                if (lblx < LBLS - 1 && j < 10 && sv.pd1[ibr] != '(') {
                                    lblx++
                                    putAtZb(lbls[lblx], 0, strAtZb(dosave, 0))
                                    lbloff[lblx] = ibr
                                }
                                searchDone = true
                                break
                            }
                            while (sv.pd1[ibr] != '\u0000' && ibr < sv.pd1len) ibr++
                        }
                    }
                    if (!searchDone) { state = ItLbl.LBL_ERROR }
                    else state = ItLbl.OFFSET
                } else {
                    // not do xxx^yyy but may be do xxx^yy(123)
                    while (sv.pd1[ibr] != ' ' && sv.pd1[ibr] != TAB &&
                           sv.pd1[ibr] != '\u0000' && sv.pd1[ibr] != '(') ibr++
                    state = ItLbl.OFFSET
                }
            } else state = ItLbl.OFFSET
        }
        // ----------------------------------------------------------------
        ItLbl.OFFSET -> {
            // pass arguments to subroutine
            if (sv.pd1[ibr] == '(') {
                val iargs = IntArray(10)
                var nargs = 0
                var count = 0
                sv.exec++ // indicates subroutine call level
                // advance to new symbol table level
                sv._Sym++
                sv.start[sv._Sym] = null
                symInc = 1 // prevent inc from happening again
                i = 0
                while (sv.args[i] != '\u0000') {
                    if (sv.args[i].code == 207 || sv.args[i].code == 208) {
                        sv.args[i] = '\u0000'
                        nargs++
                        iargs[nargs] = i + 1
                    }
                    i++
                }
                val varScr = CharArray(256)
                var jx = ibr + 1
                var vi = 0
                loop@ while (sv.pd1[jx] != ' ' && sv.pd1[jx] != '\u0000') {
                    if (sv.pd1[jx] == '\u0000') { state = ItLbl.LBL_ERROR; break@loop }
                    if (sv.pd1[jx] == ',' || sv.pd1[jx] == ')') {
                        varScr[vi] = '\u0000'
                        if (count >= nargs) { // pass zeros
                            // no arg
                        } else {
                            // code 100 means store at this level only
                            var symflgArg = 100
                            val t: CharArray
                            if (sv.args[iargs[count]] == '.') {
                                cpyAtZb(sv.reference, 0, sv.args, iargs[count] + 1)
                                val tmpA = CharArray(STR_MAX)
                                if (sym_(IP_SymRetrieve,
                                        strAtZb(sv.args, iargs[count] + 1),
                                        tmpA, sv) == null) {
                                    print("*** Var: " +
                                        strAtZb(sv.args, iargs[count] + 1) + "\n")
                                    state = ItLbl.VAR_NOT_FOUND
                                    break@loop
                                }
                                t = tmpA
                                symflgArg = 200
                            } else {
                                t = sv.args
                                tOffArg = iargs[count]
                            }
                            if (symflgArg == 200) {
                                sym_(symflgArg, strAtZb(varScr, 0), t, sv)
                            } else {
                                val dcopy = CharArray(STR_MAX)
                                cpyAtZb(dcopy, 0, t, tOffArg)
                                sym_(symflgArg, strAtZb(varScr, 0), dcopy, sv)
                            }
                        }
                        vi = 0
                        jx++
                        count++
                        if (sv.pd1[jx] == ')') break@loop
                        continue@loop
                    }
                    varScr[vi] = sv.pd1[jx]; vi++; jx++
                }
            }
            if (state == ItLbl.LBL_ERROR || state == ItLbl.VAR_NOT_FOUND) {
                // fall to the error state below
            } else if (lblK > 0) { // offset from label
                i = 0
                while (i < lblK) {
                    while (sv.pd1[ibr] != '\u0000' && ibr < sv.pd1len) ibr++
                    if (ibr >= sv.pd1len) { state = ItLbl.LBL_ERROR; break }
                    i++
                }
                if (state != ItLbl.LBL_ERROR) {
                    while (sv.pd1[ibr] != TAB && sv.pd1[ibr] != ' ') ibr++
                    if (ibr >= sv.pd1len) { state = ItLbl.LBL_ERROR }
                    else state = ItLbl.OFFSET_DONE
                }
            } else if (lblK == 0) {
                ibr = pd1cur
                state = ItLbl.OFFSET_DONE
            } else {
                state = ItLbl.OFFSET_DONE
            }
        }
        ItLbl.OFFSET_DONE -> {
            if (sv.xd[sv.xpx] != '\u0000' && sv.xd[sv.xpx] != ' ') sv.xpx++
            if (retz == 'g') { state = ItLbl.GOTO_RETURN } // GOTO
            else { state = ItLbl.DO_RET } // DO
        }
        // ----------------------------------------------------------------
        // error-family tails (vendor :2385-2475; all funnel abrt1->abrt)
        ItLbl.ERR_CALL -> {
            ernbr = 0
            if (sv.NOERR != 0) { state = ItLbl.ABRT1 }
            else {
                System.err.print("\n*** " + ernbr + " Control-C\n")
                state = ItLbl.ABRT1
            }
        }
        ItLbl.PARSE_ERR -> {
            ernbr = sv.ierr
            prnterrZb(ernbr, sv, pd1cur)
            if (sv.NOERR != 0) {
                sv.NOERR = -1
                state = ItLbl.NEXT_CMND
            } else state = ItLbl.ABRT1
        }
        ItLbl.POST_COND_ERROR -> {
            ernbr = 6
            prnterrZb(ernbr, sv, pd1cur)
            state = ItLbl.ABRT1
        }
        ItLbl.LBL_ERROR -> {
            ernbr = 8
            prnterrZb(ernbr, sv, pd1cur)
            print(" " + strAtZb(dosave, 0) + "\n")
            state = ItLbl.ABRT
        }
        ItLbl.VAR_NOT_FOUND -> {
            ernbr = 17
            prnterrZb(ernbr, sv, pd1cur)
            state = ItLbl.ABRT
        }
        ItLbl.DEFAULT_ERR -> {
            ernbr = 13
            prnterrZb(ernbr, sv, pd1cur)
            state = ItLbl.ABRT1
        }
        ItLbl.ARG_LIST_ERROR -> {
            ernbr = 14
            prnterrZb(ernbr, sv, pd1cur)
            state = ItLbl.ABRT1
        }
        ItLbl.REF_ERR -> {
            ernbr = 18
            prnterrZb(ernbr, sv, pd1cur)
            state = ItLbl.ABRT1
        }
        ItLbl.SYMTAB_ERR -> {
            ernbr = 23
            prnterrZb(ernbr, sv, pd1cur)
            state = ItLbl.ABRT1
        }
        ItLbl.FILE_ERROR -> {
            ernbr = 26
            prnterrZb(ernbr, sv, pd1cur)
            state = ItLbl.ABRT1
        }
        ItLbl.IO_ERROR -> {
            ernbr = 20
            prnterrZb(ernbr, sv, pd1cur)
            state = ItLbl.ABRT1
        }
        ItLbl.PROGRAM_SIZE -> {
            ernbr = 31
            prnterrZb(ernbr, sv, pd1cur)
            state = ItLbl.ABRT1
        }
        ItLbl.STACK_OVERFLOW -> {
            ernbr = 32
            prnterrZb(ernbr, sv, pd1cur)
            state = ItLbl.ABRT1
        }
        ItLbl.SQL_ERROR -> {
            ernbr = 33
            prnterrZb(ernbr, sv, pd1cur)
            state = ItLbl.ABRT1
        }
        ItLbl.USE_NOT_OPEN -> {
            ernbr = 36
            prnterrZb(ernbr, sv, pd1cur)
            state = ItLbl.ABRT1
        }
        ItLbl.ABRT1 -> {
            // if (!svPtr->NOERR) printf("*** %s\n", &pd1[pd1Last]);
            state = ItLbl.ABRT
        }
        ItLbl.ABRT -> {
            // zexit falls here too (the 'e' arm at :869): the vendor
            // NOERR check (TERMINATE_ON_ERROR not defined) returns 99
            if (sv.NOERR != 0) {
                sv.NOERR = -1
                if (gsbx <= 0) { state = ItLbl.NEXT_CMND }
                else state = ItLbl.QUIT1
            } else {
                return 99
            }
        }
        // ----------------------------------------------------------------
        ItLbl.RESTORE -> {
            var si = swapextent
            while (si < SWAP) { swp[si] = null; si++ }
            retVal = 0
            state = ItLbl.RESTORE_DONE
        }
        ItLbl.RESTORE_DONE -> {}
        else -> {} // unreachable: all states covered
        } // when (state)
    } // while (true)
    return retVal
} // fun Interpret
