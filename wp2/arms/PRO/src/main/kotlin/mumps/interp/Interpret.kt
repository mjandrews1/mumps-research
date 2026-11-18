package mumps.interp

import mumps.mdh.Mltoa

/**
 * PRO-arm translation of interp-interp.cpp::Interpret (batch 6,
 * unit 155 - the deferred big-three opener; vendor reference lines
 * 133-2617, physLOC 1581, units.csv rank 308, score 2921.7).
 * Translated independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe).
 *
 * The interpreter command master loop: prologue environment init,
 * the restart/next_line/next_cmnd re-entry cycle, the command
 * dispatch (CMMNT, FCN, BREAK, FILELOCK, READ, FILEOPEN, ZCMDS, IF,
 * WRITE, JOB, GOTO, MERGE, SET, QUIT, XECUTE, FOR, HALT, ELSE, DO,
 * NEW, DATAKILL, FILECLOSE, USE), the remote LHS-reference block
 * (lhsref), the remote DO/GOTO label resolution block (lbl), and
 * the error cascade (parse_err .. abrt1 -> abrt).
 *
 * RESOLVES the InterpStubs interpret(cmd,svPtr) stub (u159-era
 * mis-tag corrected at u171: this IS roster row 156); the call
 * sites (RunMumps, SQLTable, MumpsMain, Fcn, MumpsMainUnit130,
 * ServerMain) bind unchanged on the stub signature.
 *
 * Goto linearization (h011, u95 label-dispatch / u158 mpstime
 * precedents): the vendor's ~55-label control web is rendered as a
 * single state machine over the Lbl enum - every vendor goto X
 * becomes lab = Lbl.X (with continue@sm when mid-arm), and every
 * label X becomes a when-arm. Vendor label names are kept in the
 * enum and in per-arm anchors.
 *
 * Reference-build preprocessor resolutions (h018):
 *  - SQLITE is defined (vendor configure: mumps-sqlite3 build) -
 *    the sql auto-connect prologue block and the JOB sql(CLOSE)
 *    close are IN; the DATAKILL sibling-kill k1 loop
 *    (#if !(defined(SQLITE))) is OUT (excluded arm below as a
 *    comment).
 *  - NATIVE_SHARED is NOT defined - PulseCount, the READ/hang
 *    Mglobal(PULSE) deadlock-prevention blocks are OUT (comments).
 *  - TERMINATE_ON_ERROR is NOT defined - the abrt label renders
 *    its #else arm: return 99.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h001 C-idiom adaptation : interior-pointer call sites get
 *    copy-in/call/copy-write-back adapters (getstr2At,
 *    TimeOut_getstr1At); Reader/Writer carriers at the file edges.
 *  - h002 C-library edge : putchar/printf/fprintf edges rendered
 *    as print/println/System.err; sleep -> Thread.sleep.
 *  - h004 globals : the file-scope static char-star-swap array
 *    swp[SWAP] persists across calls - InterpretState object.
 *  - h008 carrier : forv/forin/forlv rows; the nmes except-list
 *    nodes (struct nmes, sym.h) render as the per-file Nmes class;
 *    svPtr.nstart stays the opaque Any? carrier.
 *  - h009 collapse : sprintf percent-d renders as
 *    writeCstring(dst, n.toString(), off).
 *  - h010 promotion : the vendor short locals (pd1cur, cmdx, gsbx,
 *    forx, ibr, i, j, k, l, New, retz, icmnd, p2px, ernbr, pd1Last,
 *    swapextent, shold, gosub[][9]) render as Int - every value
 *    fits the short domain (offsets <= PS=32000), so the render is
 *    value-identical; svPtr.pd1len stays Long (vendor long).
 *  - h011 goto : see the state machine above.
 *  - h012 byte domain : unsigned char buffers render as CharArray
 *    with NUL sentinels; unsigned-char reads stored to Int mask
 *    with 0xFF (vendor EOF -1 as unsigned char).
 *  - h013 dead code : cod206/cod207/cod208/cod209/cod210 are
 *    declared and never used (dropped, note); the never-referenced
 *    error labels ref_err (18), symtab_err (23), program_size
 *    (31), sql_error (33) are kept as roster notes only; the
 *    unreachable return EXIT_SUCCESS after _exit in HALT dropped.
 *  - h014 out_of_scope_call : new stubs raised - Merge (owner
 *    interp-sysfunc.cpp::Merge, batch-8 corpus unit) and inout
 *    (owner interp-parse.cpp::inout, batch-10 corpus unit); the
 *    OS edges fork/system/popen/the fdes pipe write render as
 *    per-file private stubs (u158/u161 precedent); the BuildGlobal/
 *    BuildLocal stack-cleaner NULL key widens the stub key
 *    parameter to CharArray? (call sites bind unchanged).
 *  - h015 Kotlin identity : the raw dollar byte (FCN, the
 *    label-argument dollar) is carried by DOLLAR; the shadowed
 *    local pd1cur blocks in the SET shell and HALT html arms
 *    render as pd1curL (vendor comment kept); the shadowed
 *    unsigned-char i in the READ arms renders as iC.
 *  - h017 naming : constants keep vendor names (interp.h anchors
 *    38-60, defines.h anchors); interpret is the stub's
 *    lower-camel name.
 *
 * Vendor quirks kept verbatim: the lbloff[20] under-sized label
 * offset table (vendor declares 20 slots but indexes to LBLS-1 -
 * widened to LBLS here, UB quantized by widening, value-identical
 * for <= 20 labels); the FILELOCK double semicolon after
 * LockDone; argv-style scan fossils; the one-past-NUL extra NUL
 * write in the xd copy (*to=0 after the NUL already copied).
 */

// --- interp.h command characters (vendor include/mumpsc/interp.h:38-60;
//     InterpH.kt keeps the header copy; per-file privates per u171 rule) ---

/** interp.h:38 - the BREAK command char. */
private const val BREAK = 'b'
/** interp.h:39 - comment command char. */
private const val CMMNT = ';'
/** interp.h:40 - kill command char. */
private const val DATAKILL = 'k'
/** interp.h:41 - do command char. */
private const val DO = 'd'
/** interp.h:42 - else command char. */
private const val ELSE = 'e'
/** interp.h:44 - file lock command char. */
private const val FILELOCK = 'l'
/** interp.h:45 - file open command char. */
private const val FILEOPEN = 'o'
/** interp.h:46 - for command char. */
private const val FOR = 'f'
/** interp.h:47 - goto command char. */
private const val GOTO = 'g'
/** interp.h:48 - halt command char. */
private const val HALT = 'h'
/** interp.h:50 - if command char. */
private const val IF = 'i'
/** interp.h:51 - job command char. */
private const val JOB = 'j'
/** interp.h:52 - quit command char. */
private const val QUIT = 'q'
/** interp.h:53 - read command char. */
private const val READ = 'r'
/** interp.h:54 - set command char. */
private const val SET = 's'
/** interp.h:55 - merge command char. */
private const val MERGE = 'm'
/** interp.h:56 - tab. */
private const val TAB = '\t'
/** interp.h:57 - use command char. */
private const val USE = 'u'
/** interp.h:58 - write command char. */
private const val WRITE = 'w'
/** interp.h:59 - xecute command char. */
private const val XECUTE = 'x'
/** interp.h:60 - zcommand char. */
private const val ZCMDS = 'z'
/** interp.h:61 - new command char. */
private const val NEW = 'n'
/** interp.h:63 - quote. */
private const val QUOTE = '\''
/** interp.h:82 - predicate var char. */
private const val PREDICATEVAR = '~'

/** The FCN command char (interp.h:42) - raw dollar byte carried (h015). */
private val DOLLAR = 36.toChar()

// --- defines.h anchors (file-private; Mglobal.kt and siblings keep
//     their own copies per the per-file const rule) ---

/** defines.h:174 - SymRetrieve. */
private const val SymRetrieve = 1

/** defines.h:167 - GKILL. */
private const val GKILL = 3

/** defines.h:87 - CLOSE. */
private const val CLOSE = 4

/** defines.h:166 - INIT. */
private const val INIT = 6

/** defines.h:88 - XNEXT. */
private const val XNEXT = 8

/** defines.h:204 - RETRIEVE. */
private const val RETRIEVE = 0

/** defines.h:206 - STORE. */
private const val STORE = 1

/** defines.h:169 - OPEN (206, the coded open paren byte). */
private const val OPEN = 206

// --- size constants (live carriers per u177 MemsizeH note) ---

/** sysparms.h:74 - PS. */
private const val PS = 32000

/** sysparms.h:75 - PS1 = PS + 1. */
private const val PS1 = 32001

/** strmax.h:3 - STR_MAX. */
private const val STR_MAX = 32000

// NUL: the u19 public (defines.h NUL byte) is reused; no per-file copy.

/**
 * h004: the vendor file-scope static char-star swap array
 * (interp-interp.cpp:143, static char *swp[SWAP]) persists across
 * Interpret calls - object carrier. Entries are the swapped-out
 * program-partition snapshots (ByteArrays); free() renders as the
 * null-out.
 */
private object InterpretState {
    /** vendor: static char *swp[SWAP] - SWAP is the u19 public const (30). */
    val swp: Array<ByteArray?> = arrayOfNulls(SWAP)
}

/**
 * h008 carrier: vendor struct nmes (sym.h:29-31, the new/kill
 * except-list node). The private per-file copy is REMOVED and the
 * name now binds to the promoted internal seat in Sym_.kt (roster
 * row 280, batch 10); the svPtr.nstart opaque Any? carrier and all
 * casts in this file bind unchanged.
 */

/**
 * h011: state-machine labels. Names are the vendor goto labels;
 * CMD_x states are the switch(icmnd) case entries.
 */
private enum class Lbl {
    INIT, RESTART, NEXT_LINE, NEXT_LINE_XECUTE, NEXT_CMND,
    CMD_CMMNT, CMD_FCN, CMD_BREAK, CMD_FILELOCK, READ_CMD, START_IO,
    STORE, CONT_READ, OPENF, OPN1, CMD_ZCMDS, CMD_IF, NEXT_IF_ARG,
    CMD_WRITE, WRITEPROC, IOSUB, IOSUB1, CMD_JOB, CMD_GOTO,
    CMD_MERGE, CMD_SET, SET_BEGIN, S1, SET_MID, S2, SET_RETURN, QUIT, QUIT1,
    CMD_XECUTE, XECUTE, XECUTE_RETURN, CMD_FOR, NXT_ARG,
    INIT_LOOP_VAR, ARGLESS, BYP, FOR_END, FOR_QUIT, CMD_HALT, HANG,
    CMD_ELSE, CMD_DO, DO_RET1, DO_RET, CMD_NEW, NEW_AGAIN, NEW2, NEW_TAIL,
    CMD_KILL, KILL_AGAIN, KILL2, KILL_TAIL, CMD_FILECLOSE, NEXT_CLOSE_ARG,
    CMD_USE, LHSREF, LHSREF1, LBL, GOTO_RETURN, OFFSET,
    ERR_CALL, PARSE_ERR, POST_COND_ERROR, LBL_ERROR, VAR_NOT_FOUND,
    DEFAULT_ERR, ARG_LIST_ERROR, FILE_ERROR, IO_ERROR, STACK_OVERFLOW,
    USE_NOT_OPEN, ABRT1, ABRT, RESTORE
}

/**
 * h009 collapse: the vendor's repeated assign-and-test
 * if ((svPtr->ierr = parse_(svPtr))) goto parse_err; renders as
 * this helper - the assignment to svPtr.ierr is preserved at every
 * site (43 occurrences, interp-interp.cpp throughout).
 */
private fun parseErr(svPtr: Msv): Boolean {
    svPtr.ierr = parse_(svPtr)
    return svPtr.ierr != 0
}

/** h002: vendor putchar(c). */
private fun putcharC(c: Char) {
    print(c)
}

/**
 * h002/h014: vendor strcasecmp (case-insensitive compare) over the
 * byte-domain buffer at off.
 */
private fun strcasecmpC(b: CharArray, off: Int, s: String): Int {
    val have = cstringAt(b, off)
    return if (have.equals(s, ignoreCase = true)) 0 else 1
}

/** h002/h014: vendor strncasecmp(b, s, n). */
private fun strncasecmpC(b: CharArray, off: Int, s: String, n: Int): Int {
    val have = cstringAt(b, off)
    val cut = if (have.length <= n) have else have.substring(0, n)
    return if (cut.equals(s.substring(0, minOf(n, s.length)), ignoreCase = true)) 0 else 1
}

/** h002: vendor isprint (printable ASCII domain). */
private fun isprintC(c: Char): Boolean = c.code in 0x20..0x7E

/** h002: vendor tolower over the byte domain. */
private fun tolowerC(c: Char): Char =
    if (c.code in 65..90) (c.code + 32).toChar() else c

/** h002: vendor toupper over the byte domain. */
private fun toupperC(c: Char): Char =
    if (c.code in 97..122) (c.code - 32).toChar() else c

/** h002: vendor isalpha over the byte domain. */
private fun isalphaC(c: Char): Boolean = c.code in 65..90 || c.code in 97..122

/** h002: vendor atoi over the byte domain (shared seat alias). */
private fun atoiAt(b: CharArray, off: Int): Int = atoiC(b, off)

/**
 * h001 adapter: vendor getstr2(lun, &pd1[off], svPtr) passes an
 * interior pointer; the u48 seat takes a whole buffer. Copy the
 * current tail in (the caller's prior contents are observable -
 * getstr2's stale-buffer chop), call, write the line back.
 */
private fun getstr2At(lun: Int, svPtr: Msv, off: Int): Short {
    val scratch = CharArray(STR_MAX)
    val n = strlenC(svPtr.pd1, off)
    for (q in 0..n) scratch[q] = svPtr.pd1[off + q]
    val r = getstr2(lun.toShort(), scratch, svPtr)
    val m = strlenC(scratch)
    for (q in 0..m) svPtr.pd1[off + q] = scratch[q]
    return r
}

/**
 * h001 adapter: vendor TimeOut_getstr1(io, &pd1[off], timeout, 0)
 * - same interior-pointer copy-in/call/copy-write-back shape as
 * getstr2At (the stale-buffer chop is observable, u170).
 */
private fun timeOutGetstr1At(io: Int, svPtr: Msv, off: Int, timeout: CharArray): Int {
    val scratch = CharArray(STR_MAX)
    val n = strlenC(svPtr.pd1, off)
    for (q in 0..n) scratch[q] = svPtr.pd1[off + q]
    val r = TimeOut_getstr1(io, scratch, timeout, 0)
    val m = strlenC(scratch)
    for (q in 0..m) svPtr.pd1[off + q] = scratch[q]
    return r
}

/** h001/h014: vendor keyfix(&buf[off]) - copy/keyfix/write-back (Mglobal precedent). */
private fun keyfixAt(buf: CharArray, off: Int) {
    val kf = CharArray(strlenC(buf, off) + 1)
    for (q in kf.indices) kf[q] = buf[off + q]
    mumps.mdh.keyfix(kf)
    for (q in kf.indices) buf[off + q] = kf[q]
}

/**
 * h002/h014: vendor fopen(path, mode) at the io edges - the
 * Msv in_file/out_file carriers are Reader/Writer (u23 rule).
 * NULL on failure renders as null.
 */
private fun fopenRead(path: String): java.io.Reader? =
    try { java.io.FileReader(path) } catch (e: java.io.IOException) { null }

/** h002/h014: vendor fopen(path, "w"). */
private fun fopenWrite(path: String): java.io.Writer? =
    try { java.io.FileWriter(path) } catch (e: java.io.IOException) { null }

/** h002/h014: vendor fopen(path, "a"). */
private fun fopenAppend(path: String): java.io.Writer? =
    try { java.io.FileWriter(path, true) } catch (e: java.io.IOException) { null }

/** h002/h014: vendor popen(buf, "r") - shell stdout as a Reader. */
private fun popenRead(cmd: String): java.io.Reader? =
    try {
        ProcessBuilder("sh", "-c", cmd).start().inputStream.reader()
    } catch (e: java.io.IOException) { null }

/** h002/h014: vendor popen(buf, "w") - shell stdin as a Writer. */
private fun popenWrite(cmd: String): java.io.Writer? =
    try {
        ProcessBuilder("sh", "-c", cmd).start().outputStream.writer()
    } catch (e: java.io.IOException) { null }

/** h002/h014: vendor system(buf) - exit status, negative on failure. */
private fun systemC(cmd: String): Int =
    try {
        ProcessBuilder("sh", "-c", cmd).start().waitFor()
    } catch (e: Exception) { -1 }

/**
 * h014/h012 quantization: vendor fork() - the JVM cannot fork a
 * running interpreter state; the call renders as this per-file
 * private stub, deterministically taking the PARENT arm (nonzero).
 * The child arm (close globals, fall into GOTO) is rendered and
 * reachable in source but unreachable at runtime - the divergence
 * is observable only in JOB's child semantics, documented in the
 * journal (u128 deterministic-UB precedent).
 */
private fun forkC(): Int = 1

/** h002: vendor getpid(). */
private fun getpidC(): Long = ProcessHandle.current().pid()

/**
 * h014/h012 quantization: vendor write(fdes[1], svPtr->args,
 * STR_MAX) + close(fdes[1]) - the child-parent result pipe of the
 * JOB fork machinery. With fork quantized to the parent arm the
 * write is unreachable; rendered as a no-op with the close marking
 * the descriptor (u161 OS-syscall-stub precedent).
 */
private fun writePipeC(fd: Int, buf: CharArray, n: Int): Int {
    // no-op: see KDoc - the pipe owner is the fork machinery
    return n
}

/**
 * PRO-arm translation of interp-interp.cpp::Interpret (vendor
 * 133-2617). int Interpret(const char *parm1, struct MSV * svPtr)
 * - the stub-level name interpret binds the existing call sites.
 */
fun interpret(parm1: String, svPtr: Msv): Int {

    // --- vendor locals (133-177) ---

    var pd1cur = 0            // vendor: short pd1cur
    var i = 0                 // vendor: short i
    var j = 0                 // vendor: short j
    var k = 0                 // vendor: short k
    var l = 0                 // vendor: short l
    var New = 0               // vendor: short New
    var pd1Last = 0           // vendor: short pd1Last
    var pd1hold1 = 0          // vendor: short pd1hold1
    var pd1hold2 = 0          // vendor: short pd1hold2
    var ibr = 0               // vendor: short ibr
    var cmdx = 0              // vendor: short cmdx
    var cmdxold = 0           // vendor: short cmdxold
    var forx = 0              // vendor: short forx
    var gsbx = 0              // vendor: short gsbx
    var lblx = 0              // vendor: int lblx
    var argless = 0           // vendor: int argless
    var dotcount = 0          // vendor: int dotcount (declared 307)
    var NewFlag = 0           // vendor: int NewFlag
    var SymInc = 0            // vendor: int SymInc=0
    var MergeFlag = 0         // vendor: int MergeFlag
    var retz = 0              // vendor: short retz
    var icmnd = 0             // vendor: short icmnd
    var p2px = 0              // vendor: short p2px
    var ernbr = 0             // vendor: short ernbr
    var fileflg = 0           // vendor: short fileflg
    var swapextent = 0        // vendor: unsigned short swapextent
    var shold = 0             // vendor: unsigned short shold
    var ExecLevel = 1         // vendor: short ExecLevel = 1
    var BreakFlag = 0         // vendor: short BreakFlag = 0
    var dirflg: Char = 0.toChar() // vendor: unsigned char dirflg
    var iC: Char = 0.toChar()    // vendor: the shadowed unsigned char i (h015)
    var Lf: Char = ' '           // vendor: char Lf

    val gosub = Array(GOSUBMAX) { IntArray(9) }   // vendor: short gosub[GOSUBMAX][9]
    val cmdstack = CharArray(8192)                // vendor: unsigned char cmdstack[8192]
    val lbls = Array(LBLS) { CharArray(20) }      // vendor: unsigned char lbls[LBLS][20]
    val lbloff = IntArray(LBLS)                   // vendor: int lbloff[20] - widened, see KDoc
    val forv = Array(FORMAX) { CharArray(256) }    // vendor: forv[FORMAX][256]
    val forin = Array(FORMAX) { CharArray(STR_MAX) } // vendor: forin[FORMAX][STR_MAX]
    val forlv = Array(FORMAX) { CharArray(256) }  // vendor: forlv[FORMAX][256]
    val forit = IntArray(FORMAX)                  // vendor: unsigned char forit[FORMAX]
    val forlt = IntArray(FORMAX)                  // vendor: unsigned char forlt[FORMAX]
    val ExecLevNew = IntArray(EXECLEVS)           // vendor: short ExecLevNew[EXECLEVS]={0}

    val XTAB = charArrayOf(TAB, NUL)               // vendor: char XTAB[2]
    val dosave = CharArray(STR_MAX)               // vendor: unsigned char dosave[STR_MAX]
    val tmp2 = CharArray(STR_MAX)                 // vendor: unsigned char tmp2[STR_MAX]
    val tmp3 = CharArray(STR_MAX)                 // vendor: unsigned char tmp3[STR_MAX]
    val vd = CharArray(STR_MAX)                   // vendor: unsigned char vd[STR_MAX] (string at [1])
    val ftmp = CharArray(STR_MAX)                 // vendor: unsigned char ftmp[STR_MAX]
    val MergeRHS = CharArray(STR_MAX)             // vendor: char MergeRHS[STR_MAX]
    val valBuf = CharArray(STR_MAX)               // scratch for the sym_ literal values (h009)

    val swp = InterpretState.swp                 // vendor: static char *swp[SWAP]

    var lab = Lbl.INIT

    sm@ while (true) {
        when (lab) {

        Lbl.INIT -> {
            // vendor 190-215: init environment
            svPtr.io = 5
            for (q in 0 until PS) svPtr.pd1[q] = ' '
            svPtr.symlen = PS
            pd1cur = 1
            svPtr.pd1len = 0
            // vendor: srand(time(NULL)) - libc seeding has no JVM
            // carrier; the arm's random surface is empty, no-op
            // render (h002 journal note)
            svPtr.bd[0] = NUL
            svPtr.bd[1] = NUL
            for (q in 0 until GOSUBMAX) for (r in 0 until 9) gosub[q][r] = 0
            // vendor: setlocale(LC_ALL,"en_US.UTF-8") - locale is
            // carried at the arm's io edges (u116 Locale.US
            // precedent), no process-wide carrier
            // SQLITE defined (reference build): sql auto connect
            // at interp startup - vendor 233-236
            val key = CharArray(2) { NUL }
            val bd2 = CharArray(2) { NUL }
            NativeBtree.btree(OPEN, key, bd2, svPtr)
            // vendor 247-249: set trap / install init.mps
            pd1cur = 1
            svPtr.pd1[(svPtr.pd1len + 1).toInt()] = NUL
            svPtr.ctrlc = 0
            lab = Lbl.RESTART
        }

        Lbl.RESTART -> {
            // vendor 265-282: restart
            BreakFlag = 0
            ExecLevel = 1
            for (q in 0 until EXECLEVS) ExecLevNew[q] = 0
            svPtr.ierr = 0
            svPtr.io = 5
            svPtr.t0px = 0
            svPtr.t2 = 0
            for (q in 0 until SWAP) {            // clear the swap area
                swp[q] = null                     // vendor: free(swp[i]); swp[i]=NULL
            }
            swapextent = 1
            lblx = 0
            // vendor 284-286: initialize execution buffer
            writeCstring(svPtr.pd1, parm1, 2)     // strcpy(&pd1[2], parm1)
            svPtr.pd1[1] = TAB
            svPtr.pd1len = (strlenC(svPtr.pd1, 1) + 1).toLong()
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
            lab = Lbl.NEXT_LINE
        }

        Lbl.NEXT_LINE -> {
            // vendor 294-373: next_line
            BreakFlag = 0
            if (pd1cur > svPtr.pd1len) {
                svPtr.xd[svPtr.xpx] = NUL
                lab = Lbl.QUIT
                continue@sm
            }
            svPtr.PD1 = PS1 - pd1cur
            svPtr.LineNumber++
            // comments or special code
            if (svPtr.pd1[pd1cur] == '#' ||
                svPtr.pd1[pd1cur] == '%' ||
                svPtr.pd1[pd1cur] == '+' ||
                svPtr.pd1[pd1cur] == ';' ||
                (svPtr.pd1[pd1cur] == '/' && svPtr.pd1[pd1cur + 1] == '/')) {
                pd1cur++
                while (svPtr.pd1[pd1cur] != NUL) pd1cur++
                pd1cur++
                lab = Lbl.NEXT_LINE
                continue@sm
            }
            if (svPtr.pd1[pd1cur] == NUL) {
                pd1cur++
                lab = Lbl.NEXT_LINE
                continue@sm
            }
            i = pd1cur // remember starting place
            // skip label - find 1st blank or TAB
            while (svPtr.pd1[pd1cur] != NUL &&
                   svPtr.pd1[pd1cur] != TAB &&
                   svPtr.pd1[pd1cur] != ' ') pd1cur++
            // skip multiple TABs
            while (svPtr.pd1[pd1cur] == TAB || svPtr.pd1[pd1cur] == ' ') pd1cur++
            // copy source line to xd (the copy itself is at the
            // next_line_xecute label, vendor 450)
            argless = 0
            dotcount = 0
            while (svPtr.pd1[pd1cur + dotcount] == '.') dotcount++
            if (dotcount + 1 < ExecLevel) {
                lab = Lbl.QUIT1
                continue@sm
            }
            if (dotcount != 0) {
                if (svPtr.pd1[pd1cur] == '.') {
                    // vendor 317-381: the dot-level ladder (". " through
                    // "......... "); each level: not this level -> skip line
                    val ladder = arrayOf(". ", ".. ", "... ", ".... ",
                                         "..... ", "...... ", "....... ",
                                         "........ ", "......... ")
                    var matched = false
                    for (lev in ladder.indices) {
                        if (strncmpC(svPtr.pd1, pd1cur, ladder[lev], ladder[lev].length) == 0) {
                            matched = true
                            if (ExecLevel != lev + 2) {
                                while (svPtr.pd1[pd1cur++] != NUL) { /* scan for end of line */ }
                                lab = Lbl.NEXT_LINE
                            } else {
                                pd1cur += ladder[lev].length
                                lab = Lbl.NEXT_LINE_XECUTE
                            }
                            break
                        }
                    }
                    if (matched) continue@sm
                    // no ladder entry matched: fall through
                    lab = Lbl.NEXT_LINE_XECUTE
                    continue@sm
                }
            }
            lab = Lbl.NEXT_LINE_XECUTE
        }

        Lbl.NEXT_LINE_XECUTE -> {
            // vendor 450-464: next_line_xecute
            pd1Last = pd1cur
            svPtr.xpx = 1
            // vendor: to=&xd[1]; from=&pd1[pd1cur];
            // while (*(to++) = *(from++)); *to=0;
            var clen = 0
            while (svPtr.pd1[pd1cur + clen] != NUL) {
                svPtr.xd[1 + clen] = svPtr.pd1[pd1cur + clen]
                clen++
            }
            svPtr.xd[1 + clen] = NUL
            svPtr.xd[2 + clen] = NUL // vendor: *to=0 - the one-past-NUL write (quirk kept)
            pd1cur = pd1cur + clen + 1
            lab = Lbl.NEXT_CMND
        }

        Lbl.NEXT_CMND -> {
            // vendor 471-545: next_cmnd - continue line entry point
            BuildLocal(-1, 0, null, 0, null, svPtr) // stack cleaner
            BuildGlobal(-1, 0, null, 0, null, svPtr) // stack cleaner
            while (true) {
                if (svPtr.xd[svPtr.xpx] == NUL || svPtr.xpx >= STR_MAX) {
                    if (forx > 0 && gosub[gsbx][3] == FOR.code) {
                        if (svPtr.ctrlc != 0) {
                            svPtr.ctrlc = 0
                            ernbr = 29
                            lab = Lbl.ERR_CALL
                            continue@sm
                        }
                        lab = Lbl.QUIT1
                        continue@sm
                    } else {
                        lab = Lbl.NEXT_LINE
                        continue@sm
                    }
                }
                if (svPtr.xd[svPtr.xpx] != ' ' && svPtr.xd[svPtr.xpx] != TAB) break
                svPtr.xpx++
            }
            // decode command word
            p2px = svPtr.xpx
            svPtr.t2 = 0
            svPtr.t0px = 0
            icmnd = tolowerC(svPtr.xd[svPtr.xpx]).code // command letter
            while (isalphaC(svPtr.xd[++svPtr.xpx])) { /* Ignore rest of word */ }
            if (svPtr.xd[svPtr.xpx] == ':') { // Postconditional
                svPtr.xpx++
                if (parseErr(svPtr)) {
                    lab = Lbl.PARSE_ERR
                    continue@sm
                }
                if (atoiC(svPtr.pd1, svPtr.sdlim) == 0) {
                    // vendor 503-516: scan to end of this command,
                    // skipping quoted strings (the pc1 re-entry
                    // renders as the nested label loop)
                    while (svPtr.xd[svPtr.xpx++] != NUL) {
                        if (svPtr.xd[svPtr.xpx] == QUOTE) {
                            svPtr.xpx++
                            pc1@ while (true) {
                                while (svPtr.xd[svPtr.xpx] != QUOTE &&
                                       svPtr.xd[svPtr.xpx] != NUL) svPtr.xpx++
                                svPtr.xpx++
                                if (svPtr.xd[svPtr.xpx] == QUOTE) {
                                    svPtr.xpx++
                                    continue@pc1 // vendor: goto pc1
                                }
                                break@pc1
                            }
                        }
                    }
                    if (svPtr.xd[svPtr.xpx] == ' ') {
                        lab = Lbl.NEXT_CMND
                        continue@sm
                    }
                }
                svPtr.xpx--
                lab = Lbl.NEXT_CMND
                continue@sm
            }
            if (svPtr.xd[svPtr.xpx] != NUL) svPtr.xpx++ // post conditional return

            // switch (icmnd) - go to command handler (vendor 541)
            lab = when (icmnd.toChar()) {
                CMMNT, '/' -> Lbl.CMD_CMMNT
                DOLLAR -> Lbl.CMD_FCN
                BREAK -> Lbl.CMD_BREAK
                FILELOCK -> Lbl.CMD_FILELOCK
                READ -> Lbl.READ_CMD
                FILEOPEN -> Lbl.OPENF
                ZCMDS -> Lbl.CMD_ZCMDS
                IF -> Lbl.CMD_IF
                WRITE -> Lbl.CMD_WRITE
                JOB -> Lbl.CMD_JOB
                GOTO -> Lbl.CMD_GOTO
                MERGE -> Lbl.CMD_MERGE
                SET -> Lbl.CMD_SET
                QUIT -> Lbl.QUIT
                XECUTE -> Lbl.CMD_XECUTE
                FOR -> Lbl.CMD_FOR
                HALT -> Lbl.CMD_HALT
                ELSE -> Lbl.CMD_ELSE
                DO -> Lbl.CMD_DO
                NEW -> Lbl.CMD_NEW
                DATAKILL -> Lbl.CMD_KILL
                'c' -> Lbl.CMD_FILECLOSE // FILECLOSE 'c'
                USE -> Lbl.CMD_USE
                else -> Lbl.DEFAULT_ERR
            }
        }

        Lbl.CMD_CMMNT -> {
            // vendor 546-549: comment command - do nothing
            while (svPtr.xd[svPtr.xpx++] != NUL) { /* skip to end of line */ }
            svPtr.xpx = STR_MAX
            lab = Lbl.NEXT_CMND
        }

        Lbl.CMD_FCN -> {
            // vendor 551-558: direct function execution
            svPtr.xpx = p2px
            svPtr.t0px = 0
            svPtr.t2 = 1
            if (parseErr(svPtr)) {
                lab = Lbl.PARSE_ERR
                continue@sm
            }
            svPtr.xpx++
            lab = Lbl.NEXT_CMND
        }

        Lbl.CMD_BREAK -> {
            // vendor 560-562: non-standard use - end a FOR loop from a DO group
            BreakFlag = 1
            lab = Lbl.QUIT
        }

        Lbl.CMD_FILELOCK -> {
            // vendor 564-602: FILELOCK - not meaningful, all files are locked
            if (svPtr.xd[svPtr.xpx] == ' ' || svPtr.xd[svPtr.xpx] == NUL) {
                lab = Lbl.NEXT_CMND
                continue@sm
            }
            if (svPtr.xd[svPtr.xpx] == '-' || svPtr.xd[svPtr.xpx] == '+')
                Lf = svPtr.xd[svPtr.xpx++]
            else Lf = ' '
            svPtr.t0px = 1
            if (parseErr(svPtr)) {
                lab = Lbl.PARSE_ERR
                continue@sm
            }
            keyfixAt(svPtr.v1d, 1)
            writeCstring(tmp2, cstringAt(svPtr.v1d, 1)) // lock reference
            strcpyC(dosave, "0") // timeout
            svPtr.tpx = 1
            if (svPtr.xd[svPtr.xpx + 1] == ':') {
                svPtr.t2 = 1
                svPtr.xpx += 2
                if (parseErr(svPtr)) {
                    lab = Lbl.PARSE_ERR
                    continue@sm
                }
                svPtr.xpx++
                j = atoiC(svPtr.pd1, svPtr.sdlim)
                writeCstring(dosave, cstringAt(svPtr.pd1, svPtr.sdlim)) // timeout
                svPtr.tpx = 0
                lab = Lbl.NEXT_CMND
                continue@sm
            }
            Mltoa(getpidC(), tmp3) // process id
            svPtr.tpx =
                if (makeLock(cstringAt(tmp2), "1", cstringAt(tmp3), Lf,
                             cstringAt(dosave)) == "1") 1L else 0L
            svPtr.LockDone = 1 // vendor: double semicolon fossil (kept)
            svPtr.xpx++
            lab = Lbl.NEXT_CMND
        }

        Lbl.READ_CMD -> {
            // vendor 604-708: READ command (read_command label)
            if (svPtr.io != 5 && svPtr.in_file[svPtr.io] == null) {
                lab = Lbl.IO_ERROR
                continue@sm
            }
            // NATIVE_SHARED: the Mglobal(PULSE) deadlock-prevention
            // block is excluded (reference build, h018)
            dirflg = NUL
            if (svPtr.xd[svPtr.xpx] == '@') {
                // indirect
                svPtr.t0px = 1
                if (parseErr(svPtr)) {
                    lab = Lbl.PARSE_ERR
                    continue@sm
                }
            }
            if (svPtr.xd[svPtr.xpx] == '!' || svPtr.xd[svPtr.xpx] == '#') {
                // carriage control
                i = if (svPtr.xd[svPtr.xpx] != '#') 2 else 1
                lab = Lbl.START_IO
                continue@sm
            }
            if (svPtr.xd[svPtr.xpx] == '?') { // tab format
                svPtr.t2 = 1
                svPtr.xpx--
                if (parseErr(svPtr)) {
                    lab = Lbl.PARSE_ERR
                    continue@sm
                }
                j = atoiC(svPtr.pd1, svPtr.sdlim)
                i = 3
                lab = Lbl.START_IO
                continue@sm
            }
            if (svPtr.xd[svPtr.xpx] == QUOTE2) { // text to be printed first
                svPtr.t2 = 1
                if (parseErr(svPtr)) {
                    lab = Lbl.PARSE_ERR
                    continue@sm
                }
                i = 4
                lab = Lbl.START_IO
                continue@sm
            }
            if (svPtr.xd[svPtr.xpx] == '*') { // direct mode
                svPtr.xpx++
                dirflg = 1.toChar()
            }
            svPtr.t0px = 1 // extract variable name to be read
            svPtr.setname[0] = NUL
            if (parseErr(svPtr)) {
                lab = Lbl.PARSE_ERR
                continue@sm
            }
            writeCstring(vd, cstringAt(svPtr.v1d, 1), 1)
            // timeout
            if (svPtr.xd[svPtr.xpx + 1] == ':') {
                svPtr.t2 = 1
                svPtr.xpx += 2
                if (parseErr(svPtr)) {
                    lab = Lbl.PARSE_ERR
                    continue@sm
                }
                j = atoiC(svPtr.pd1, svPtr.sdlim)
                svPtr.xpx--
                if (j <= 0) {
                    svPtr.xpx--
                    svPtr.tpx = 0
                    lab = Lbl.CONT_READ
                    continue@sm
                }
                if (dirflg == NUL) {
                    val timeout = CharArray(64)
                    writeCstring(timeout, j.toString()) // vendor: sprintf(timeout,"%d",j)
                    svPtr.tpx = timeOutGetstr1At(svPtr.io, svPtr, svPtr.sdlim, timeout).toLong()
                } else {
                    val out = ShortArray(1)
                    svPtr.tpx = DirectRead(svPtr.io, out, svPtr).toLong()
                    writeCstring(svPtr.pd1, (out[0].toInt() and 0xFF).toString(), svPtr.sdlim)
                }
                lab = Lbl.STORE
                continue@sm
            }
            svPtr.hor[svPtr.io] = 0
            svPtr.ver[svPtr.io]++
            i = svPtr.io
            svPtr.sdlim = (svPtr.pd1len + 2).toInt()
            if (dirflg == 1.toChar()) {
                val out = ShortArray(1)
                svPtr.tpx = DirectRead(svPtr.io, out, svPtr).toLong()
                writeCstring(svPtr.pd1, (out[0].toInt() and 0xFF).toString(), svPtr.sdlim)
            } else {
                svPtr.tpx = getstr2At(i, svPtr, svPtr.sdlim).toLong()
                if (svPtr.tpx >= 0) svPtr.tpx = 1 else svPtr.tpx = 0
            }
            lab = Lbl.STORE
        }

        Lbl.START_IO -> {
            // vendor 662-663: start_io
            inout(svPtr.pd1, svPtr.sdlim, i, j, svPtr) // InterpStubs stub (batch-10 owner)
            lab = Lbl.CONT_READ
        }

        Lbl.STORE -> {
            // vendor 699-700: store
            retz = READ.code
            lab = Lbl.LHSREF
        }

        Lbl.CONT_READ -> {
            // vendor 702-710: cont_read
            svPtr.xpx++
            dirflg = NUL
            if (svPtr.xd[svPtr.xpx] == ',') svPtr.xpx++
            if (svPtr.xd[svPtr.xpx] == ' ' ||
                svPtr.xd[svPtr.xpx] == NUL ||
                svPtr.xd[svPtr.xpx] == TAB) {
                lab = Lbl.NEXT_CMND
            } else {
                lab = Lbl.READ_CMD
            }
        }

        Lbl.OPENF -> {
            // vendor 748-790: FILEOPEN (openf label)
            svPtr.t2 = 1
            if (parseErr(svPtr)) {
                lab = Lbl.PARSE_ERR
                continue@sm
            }
            k = atoiC(svPtr.pd1, svPtr.sdlim)
            if (k <= 0 || k >= 5) {
                lab = Lbl.FILE_ERROR
                continue@sm
            }
            if (svPtr.in_file[k] != null || svPtr.out_file[k] != null) {
                lab = Lbl.FILE_ERROR
                continue@sm
            }
            if (svPtr.xd[svPtr.xpx] != ':') {
                lab = Lbl.ARG_LIST_ERROR
                continue@sm
            }
            svPtr.xpx++
            svPtr.t2 = 1
            if (parseErr(svPtr)) {
                lab = Lbl.PARSE_ERR
                continue@sm
            }
            i = 0
            while (svPtr.pd1[svPtr.sdlim + i] != NUL &&
                   svPtr.pd1[svPtr.sdlim + i] != ',') {
                tmp2[i] = svPtr.pd1[svPtr.sdlim + i]
                i++
            }
            if (svPtr.pd1[svPtr.sdlim + i] != ',') {
                i = 0
                while (svPtr.pd1[svPtr.sdlim + i] != NUL &&
                       svPtr.pd1[svPtr.sdlim + i] != '/') {
                    tmp2[i] = svPtr.pd1[svPtr.sdlim + i]
                    i++
                }
            }
            tmp2[i] = NUL
            i++
            if (strcasecmpC(svPtr.pd1, svPtr.sdlim + i, "old") == 0) New = 0
            else if (strcasecmpC(svPtr.pd1, svPtr.sdlim + i, "new") == 0) New = 1
            else if (strcasecmpC(svPtr.pd1, svPtr.sdlim + i, "append") == 0) New = 2
            else {
                lab = Lbl.ARG_LIST_ERROR
                continue@sm
            }
            if (New == 1) { // output new
                svPtr.out_file[k] = fopenWrite(cstringAt(tmp2))
                svPtr.tpx = if (svPtr.out_file[k] == null) 0 else 1
                lab = Lbl.OPN1
                continue@sm
            }
            if (New == 2) { // output append
                svPtr.out_file[k] = fopenAppend(cstringAt(tmp2))
                svPtr.tpx = if (svPtr.out_file[k] == null) 0 else 1
                lab = Lbl.OPN1
                continue@sm
            }
            svPtr.in_file[k] = fopenRead(cstringAt(tmp2))
            svPtr.tpx = if (svPtr.in_file[k] == null) 0 else 1
            lab = Lbl.OPN1
        }

        Lbl.OPN1 -> {
            // vendor 792-796: opn1
            if (svPtr.xd[++svPtr.xpx] != ',') {
                lab = Lbl.NEXT_CMND
                continue@sm
            }
            svPtr.xpx++
            lab = Lbl.OPENF
        }

        Lbl.CMD_ZCMDS -> {
            // vendor 798-871: ZCMDS
            p2px++
            if (svPtr.xd[p2px].code <= 90)
                svPtr.xd[p2px] = (svPtr.xd[p2px].code + 32).toChar() // lower case
            if (svPtr.xd[p2px] == 'm') { // zmain
                lab = Lbl.NEXT_CMND
                continue@sm
            }
            if (svPtr.xd[p2px] == 'g') { // zg
                svPtr.v1d[0] = NUL
                Mglobal(INIT, svPtr.v1d, 0, svPtr.bd, svPtr)
                lab = Lbl.NEXT_CMND
                continue@sm
            }
            if (svPtr.xd[p2px] == 'a') {
                svPtr.gpad = 0
                lab = Lbl.NEXT_CMND
                continue@sm
            }
            if (svPtr.xd[p2px] == 'n') {
                svPtr.gpad = 1
                lab = Lbl.NEXT_CMND
                continue@sm
            }
            if (tolowerC(svPtr.xd[p2px]) == 'h') { // zhalt
                svPtr.t2 = 1
                if (parseErr(svPtr)) {
                    lab = Lbl.PARSE_ERR
                    continue@sm
                }
                j = atoiC(svPtr.pd1, svPtr.sdlim)
                println()
                Mglobal(CLOSE, tmp2, 0, tmp3, svPtr) // Close Globals
                System.out.flush() // vendor: fflush(NULL)
                kotlin.system.exitProcess(j)
            }
            if (svPtr.xd[p2px] == 'e') { // zexit
                return 1
                // vendor: the abrt label sits inside this block after
                // the return - the #else arm (TERMINATE_ON_ERROR
                // undefined) is the ABRT state: return 99
            }
            lab = Lbl.NEXT_LINE // all others
        }

        Lbl.CMD_IF -> {
            // vendor 873-897: IF
            if (svPtr.xd[svPtr.xpx] == ' ') { // no argument form
                svPtr.xpx++
                if (svPtr.tpx != 0L) {
                    lab = Lbl.NEXT_CMND
                    continue@sm
                }
                svPtr.xpx = STR_MAX
                lab = Lbl.NEXT_CMND
                continue@sm
            }
            lab = Lbl.NEXT_IF_ARG
        }

        Lbl.NEXT_IF_ARG -> {
            // vendor 884-897: next_if_arg
            svPtr.t2 = 1
            if (parseErr(svPtr)) {
                lab = Lbl.PARSE_ERR
                continue@sm
            }
            // vendor: the numcomp form is commented out in the
            // source (fossil, h013) - the atoi form is live
            if (atoiC(svPtr.pd1, svPtr.sdlim) == 0) {
                svPtr.tpx = 0
                svPtr.xpx = STR_MAX
                lab = Lbl.NEXT_CMND
                continue@sm
            }
            svPtr.tpx = 1
            if (svPtr.xd[svPtr.xpx] == ',') {
                svPtr.xpx++
                lab = Lbl.NEXT_IF_ARG
                continue@sm
            }
            lab = Lbl.NEXT_CMND
        }

        Lbl.CMD_WRITE -> {
            // vendor 899-963: WRITE
            if (svPtr.io != 5 && svPtr.out_file[svPtr.io] == null) {
                lab = Lbl.IO_ERROR
                continue@sm
            }
            lab = Lbl.WRITEPROC
        }

        Lbl.WRITEPROC -> {
            // vendor 903-963: writeproc
            if (svPtr.xd[svPtr.xpx] == '!' || svPtr.xd[svPtr.xpx] == '#') {
                // new line or page
                i = 1
                if (svPtr.xd[svPtr.xpx] != '#') i = 2
                svPtr.xpx++
                lab = Lbl.IOSUB
                continue@sm
            }
            if (svPtr.xd[svPtr.xpx] == '*') { // star output
                svPtr.xpx++
                svPtr.t2 = 1
                if (parseErr(svPtr)) {
                    lab = Lbl.PARSE_ERR
                    continue@sm
                }
                svPtr.pd1[svPtr.sdlim] =
                    atoiC(svPtr.pd1, svPtr.sdlim).toChar()
                svPtr.pd1[svPtr.sdlim + 1] = NUL
                putcharC(svPtr.pd1[svPtr.sdlim])
                i = 4
                lab = Lbl.IOSUB1
                continue@sm
            }
            if (svPtr.xd[svPtr.xpx] == '?') { // tab
                svPtr.t2 = 1
                svPtr.xpx++
                if (parseErr(svPtr)) {
                    lab = Lbl.PARSE_ERR
                    continue@sm
                }
                j = atoiC(svPtr.pd1, svPtr.sdlim)
                i = 3
                lab = Lbl.IOSUB
                continue@sm
            }
            svPtr.t2 = 1
            if (parseErr(svPtr)) {
                lab = Lbl.PARSE_ERR
                continue@sm
            }
            i = 4
            lab = Lbl.IOSUB
        }

        Lbl.IOSUB -> {
            // vendor 938: iosub
            inout(svPtr.pd1, svPtr.sdlim, i, j, svPtr) // InterpStubs stub (batch-10 owner)
            lab = Lbl.IOSUB1
        }

        Lbl.IOSUB1 -> {
            // vendor 940-947: iosub1
            if (svPtr.xd[svPtr.xpx] == ',') {
                svPtr.xpx++
                lab = Lbl.WRITEPROC
                continue@sm
            }
            if (svPtr.xd[svPtr.xpx] == NUL ||
                svPtr.xd[svPtr.xpx] == ' ' ||
                svPtr.xd[svPtr.xpx] == TAB) {
                lab = Lbl.NEXT_CMND
                continue@sm
            }
            lab = Lbl.WRITEPROC
        }

        Lbl.CMD_JOB -> {
            // vendor 949-980: JOB - we are parent: continue
            // executing, globals remain open; the child closes
            // globals and falls into the GOTO case
            if (forkC() != 0) {
                lab = Lbl.NEXT_LINE
                continue@sm
            }
            // treat argument as GOTO otherwise - drop through
            // Close Globals or Disconnect from SQL server (SQLITE arm)
            XTAB[0] = TAB
            XTAB[1] = NUL
            tmp2[0] = NUL // codes to sql()
            svPtr.tpx = sql(CLOSE, svPtr, charArrayOf(NUL), charArrayOf(NUL),
                             tmp2, XTAB).toLong()
            lab = Lbl.CMD_GOTO
        }

        Lbl.CMD_GOTO -> {
            // vendor 981-992: GOTO
            svPtr.iorg = 1
            retz = GOTO.code
            while (gosub[gsbx][3] == FOR.code) {
                cmdx = gosub[gsbx][2]
                gsbx--
            }
            // zap all stacking
            forx = 0
            lab = Lbl.LBL
        }

        Lbl.GOTO_RETURN -> {
            // vendor 994-998: goto_return
            forx = 0
            pd1cur = ibr
            ExecLevel = 1
            gsbx = 0
            lab = Lbl.NEXT_LINE
        }

        Lbl.CMD_MERGE -> {
            // vendor 1000-1001: MERGE
            MergeFlag = 1
            lab = Lbl.SET_BEGIN
        }

        Lbl.CMD_SET -> {
            // vendor 1003-1290: SET (SQL: and SHELL: commands too)
            MergeFlag = 0
            val isQH = toupperC(svPtr.xd[p2px + 1]) == 'Q' ||
                       toupperC(svPtr.xd[p2px + 1]) == 'H'
            if (!isQH) {
                lab = Lbl.SET_BEGIN
                continue@sm
            }
            // SHELL and SQL COMMANDS (vendor 1160-1257)
            val buf = CharArray(STR_MAX)
            val filename = CharArray(256)
            var bx = 0
            var pflag = 0
            buf[0] = NUL
            filename[0] = NUL
            if (toupperC(svPtr.xd[p2px + 1]) == 'Q') {
                pflag = -1 // SQL undefferentiated (vendor spelling)
                svPtr.bd[0] = NUL
                lab = Lbl.NEXT_LINE
                continue@sm
            }
            // Sql Codes go here
            if (strncasecmpC(svPtr.pd1, pd1Last, "shell", 5) != 0) {
                prnterr(37, svPtr, pd1cur) // unrecognized command
                lab = Lbl.ABRT
                continue@sm
            }
            // shell and sql command decode - comparable setups
            if (strncasecmpC(svPtr.pd1, pd1Last, "shell/p", 7) == 0) pflag = 1
            if (strncasecmpC(svPtr.pd1, pd1Last, "shell/g", 7) == 0) pflag = 2
            // vendor: int pd1cur=pd1Last+svPtr->xpx-2 (shadows the
            // outer short pd1cur - h015 rename pd1curL)
            var pd1curL = pd1Last + svPtr.xpx - 2
            while (svPtr.pd1[pd1curL] != NUL && svPtr.pd1[pd1curL] != ' ') pd1curL++
            if (svPtr.pd1[pd1curL] != NUL) pd1curL++
            while (svPtr.pd1[pd1curL] != NUL) {
                if (svPtr.pd1[pd1curL] == '&' && svPtr.pd1[pd1curL + 1] == PREDICATEVAR) {
                    pd1curL += 2
                    i = 1
                    while (svPtr.pd1[pd1curL] != PREDICATEVAR &&
                           svPtr.pd1[pd1curL] != NUL) {
                        svPtr.xd[i] = svPtr.pd1[pd1curL++]
                        i++
                    }
                    svPtr.xd[i] = NUL
                    if (svPtr.pd1[pd1curL] == PREDICATEVAR) pd1curL++
                    svPtr.t2 = 1
                    svPtr.xpx = 1
                    if (parseErr(svPtr)) {
                        lab = Lbl.PARSE_ERR
                        continue@sm
                    }
                    strcatC(buf, cstringAt(svPtr.pd1, svPtr.sdlim))
                    bx = strlenC(buf)
                    continue
                }
                buf[bx++] = svPtr.pd1[pd1curL]
                buf[bx] = NUL
                pd1curL++
            }
            pd1curL++
            buf[bx] = NUL
            if (pflag == 0) { // SHELL command with no options
                if (systemC(cstringAt(buf)) < 0) svPtr.tpx = 0 else svPtr.tpx = 1
            } else if (pflag == 1) { // SHELL/P command
                svPtr.in_file[6] = popenRead(cstringAt(buf))
            } else { // SHELL/G
                svPtr.out_file[6] = popenWrite(cstringAt(buf))
                svPtr.tpx = if (svPtr.out_file[6] == null) 0 else 1
            }
            lab = Lbl.NEXT_LINE
        }

        Lbl.SET_BEGIN -> {
            // vendor 1037-1118: set label
            if (svPtr.xd[svPtr.xpx] == '@') { // indirection
                svPtr.kflg = 1
                svPtr.t0px = 1
                if (parseErr(svPtr)) {
                    lab = Lbl.PARSE_ERR
                    continue@sm
                }
                svPtr.kflg = 0
            }
            ftmp[0] = NUL
            if (svPtr.xd[svPtr.xpx] == '(') { // group LHS
                svPtr.xpx++
                vd[1] = NUL
                lab = Lbl.S1
                continue@sm
            }
            svPtr.t0px = 1
            svPtr.setname[0] = NUL
            if (parseErr(svPtr)) {
                lab = Lbl.PARSE_ERR
                continue@sm
            }
            writeCstring(vd, cstringAt(svPtr.v1d, 1), 1)
            lab = Lbl.SET_MID
        }

        Lbl.S1 -> {
            // vendor 1055-1064: s1
            svPtr.t0px = 1
            if (parseErr(svPtr)) {
                lab = Lbl.PARSE_ERR
                continue@sm
            }
            strcatC(ftmp, cstringAt(svPtr.v1d, 1))
            strcatC(ftmp, " ")
            svPtr.xpx++
            if (svPtr.xd[svPtr.xpx] != ')' && svPtr.xd[svPtr.xpx] != NUL) {
                svPtr.xpx++
                lab = Lbl.S1
                continue@sm
            }
            lab = Lbl.SET_MID
        }

        Lbl.SET_MID -> {
            // vendor 1065-1074 (the group/plain join; unnamed in vendor)
            svPtr.xpx += 2
            if (MergeFlag != 0) svPtr.t0px = 1 else svPtr.t0px = 0
            svPtr.t2 = 1
            if (parseErr(svPtr)) {
                lab = Lbl.PARSE_ERR
                continue@sm
            }
            if (MergeFlag != 0) {
                writeCstring(MergeRHS, cstringAt(svPtr.v1d, 1), 1)
                svPtr.xpx++
            } else MergeRHS[1] = NUL
            retz = SET.code
            k = 0
            if (vd[1] != NUL) {
                lab = Lbl.LHSREF
                continue@sm
            }
            lab = Lbl.S2
        }

        Lbl.S2 -> {
            // vendor 1112-1115: s2
            j = 1
            // vendor: while ((vd[j++] = ftmp[k++]) != ' ');
            while (true) {
                val c = ftmp[k]
                k++
                vd[j] = c
                j++
                if (c == ' ') break
            }
            vd[j - 1] = NUL
            lab = Lbl.LHSREF
        }

        Lbl.SET_RETURN -> {
            // vendor 1117-1125: set_return
            if (ftmp[k] != NUL) {
                lab = Lbl.S2
                continue@sm
            }
            if (svPtr.xd[svPtr.xpx] == ',') {
                svPtr.xpx++
                lab = Lbl.SET_BEGIN
                continue@sm
            }
            lab = Lbl.NEXT_CMND
        }

        Lbl.QUIT -> {
            // vendor 1266-1344: QUIT (quit label)
            if (gsbx <= 0) {
                lab = Lbl.RESTORE
                continue@sm
            }
            if (gosub[gsbx][0] > 0) {
                lblx = 0 // zap labels
                // vendor: memcpy(pd1p, swp[gosub[gsbx][0]], gosub[gsbx][4])
                // pd1p = &pd1[1]
                val src = swp[gosub[gsbx][0]]
                    ?: error("interpret: null swap origin gosub[" + gsbx + "][0]")
                val cnt = gosub[gsbx][4]
                for (q in 0 until cnt) svPtr.pd1[1 + q] =
                    (src[q].toInt() and 0xFF).toChar()
                svPtr.pd1[gosub[gsbx][4] + 1] = NUL
                swapextent = gosub[gsbx][0]
                for (q in swapextent until SWAP) swp[q] = null
                if (gosub[gsbx][5] > 0) svPtr.symlen = gosub[gsbx][5]
            }
            if (forx > 0 && gosub[gsbx][3] == FOR.code) {
                cmdx = gosub[gsbx][2]
                gsbx--
                forx--
                svPtr.xpx = STR_MAX
                lab = Lbl.NEXT_CMND
                continue@sm
            }
            lab = Lbl.QUIT1
        }

        Lbl.QUIT1 -> {
            // vendor 1346-1423: quit1
            pd1cur = gosub[gsbx][1]
            if (gsbx <= 0) {
                println("exit")
                return 1
            }
            cmdxold = cmdx
            cmdx = gosub[gsbx][2]
            if (gosub[gsbx][3] == DO.code && BreakFlag != 0) svPtr.xd[1] = NUL
            else writeCstring(svPtr.xd, cstringAt(cmdstack, cmdx), 1)
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
                if (parseErr(svPtr)) {
                    lab = Lbl.PARSE_ERR
                    continue@sm
                }
                // copy processed return arg to state vector
                writeCstring(svPtr.args, cstringAt(svPtr.pd1, svPtr.sdlim))
                if (svPtr.fdes[0] >= 0) {
                    writePipeC(svPtr.fdes[1], svPtr.args, STR_MAX)
                    svPtr.fdes[1] = -1 // vendor: close(fdes[1])
                }
            }
            if (retz != FOR.code && retz != XECUTE.code) {
                ExecLevNew[ExecLevel] = 0 // prevent 2 trips
                _SymFree(1, svPtr)
            }
            // vendor: //????? fossil comment kept
            gsbx--
            if (forx > 0 && retz == FOR.code) {
                lab = Lbl.FOR_END
                continue@sm
            }
            if (retz == DO.code && ExecLevel > 1) {
                if (ExecLevNew[ExecLevel] != 0) {
                    _SymFree(1, svPtr) // undo New
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
                lab = Lbl.NEXT_CMND
                continue@sm
            }
            // are we at end of line?
            if (retz == XECUTE.code) {
                lab = Lbl.XECUTE_RETURN
                continue@sm
            }
            lab = Lbl.DO_RET1
        }

        Lbl.CMD_XECUTE -> {
            // vendor 1427-1430: XECUTE (xecute label)
            lab = Lbl.XECUTE
        }

        Lbl.XECUTE -> {
            // vendor 1427-1493: xecute
            svPtr.t2 = 1
            if (parseErr(svPtr)) {
                lab = Lbl.PARSE_ERR
                continue@sm
            }
            ftmp[0] = TAB
            writeCstring(ftmp, cstringAt(svPtr.pd1, svPtr.sdlim), 1)
            if (svPtr.xd[svPtr.xpx] == ':') {
                svPtr.xpx++
                svPtr.t2 = 1
                if (parseErr(svPtr)) {
                    lab = Lbl.PARSE_ERR
                    continue@sm
                }
                k = atoiC(svPtr.pd1, svPtr.sdlim)
                if (k == 0) {
                    if (svPtr.xd[svPtr.xpx] == ',') {
                        svPtr.xpx++
                        lab = Lbl.XECUTE
                        continue@sm
                    } else {
                        lab = Lbl.NEXT_CMND
                        continue@sm
                    }
                }
            }
            // move line to be executed into code space above
            l = svPtr.pd1len.toInt()
            writeCstring(svPtr.pd1, cstringAt(ftmp), (svPtr.pd1len + 2).toInt())
            svPtr.pd1len = svPtr.pd1len + 2 + strlenC(ftmp)
            gsbx++
            if (gsbx >= GOSUBMAX) {
                lab = Lbl.STACK_OVERFLOW
                continue@sm
            }
            gosub[gsbx][0] = 0 // swap origin
            gosub[gsbx][1] = pd1cur // restore pd1cur
            gosub[gsbx][2] = cmdx // command stack address
            writeCstring(cmdstack, cstringAt(svPtr.xd, svPtr.xpx), cmdx)
            cmdx = cmdx + strlenC(svPtr.xd, svPtr.xpx) + 1
            gosub[gsbx][4] = l // restore pd1len
            gosub[gsbx][3] = XECUTE.code
            pd1cur = l + 2
            lab = Lbl.NEXT_LINE_XECUTE
        }

        Lbl.XECUTE_RETURN -> {
            // vendor 1495-1499: xecute_return - environment restored
            if (svPtr.xd[svPtr.xpx] == ' ' || svPtr.xd[svPtr.xpx] == NUL) {
                lab = Lbl.NEXT_CMND
                continue@sm
            }
            lab = Lbl.XECUTE
        }

        Lbl.CMD_FOR -> {
            // vendor 1501-1623: FOR - stack set up
            forx++
            if (forx >= FORMAX) {
                println("OVERFLOW")
                lab = Lbl.STACK_OVERFLOW
                continue@sm
            }
            forit[forx] = 0
            forlt[forx] = 0
            forv[forx][0] = NUL
            if (++gsbx >= GOSUBMAX) {
                lab = Lbl.STACK_OVERFLOW
                continue@sm
            }
            gosub[gsbx][0] = 0 // swap
            gosub[gsbx][1] = pd1cur
            gosub[gsbx][2] = cmdx
            gosub[gsbx][3] = FOR.code
            gosub[gsbx][4] = svPtr.pd1len.toInt()
            if (svPtr.xd[svPtr.xpx] == ' ') {
                i = 1
                strcpyC(forv[forx], "_dummy")
                strcpyC(forin[forx], "0")
                strcpyC(forlv[forx], "100")
                svPtr.symflg = 0 // store
                strcpyC(valBuf, "1")
                sym_(0, forv[forx], 0, valBuf, 0, svPtr)
                forit[forx] = 1
                lab = Lbl.ARGLESS
                continue@sm
            }
            // find local variable
            svPtr.t0px = 1
            if (parseErr(svPtr)) {
                lab = Lbl.PARSE_ERR
                continue@sm
            }
            svPtr.xpx += 2
            writeCstring(forv[forx], cstringAt(svPtr.v1d, 1))
            svPtr.symflg = 0 // store
            dosave[0] = NUL
            sym_(0, forv[forx], 0, dosave, 0, svPtr)
            lab = Lbl.NXT_ARG
        }

        Lbl.NXT_ARG -> {
            // vendor 1540-1597: nxt_arg
            forit[forx] = 0
            forlt[forx] = 0
            svPtr.t2 = 1
            if (svPtr.xd[svPtr.xpx] == DOLLAR) { // function
                forit[forx] = 1
                forlt[forx] = 1
                forlv[forx][0] = NUL // function
                writeCstring(forin[forx], cstringAt(svPtr.xd, svPtr.xpx))
            }
            if (parseErr(svPtr)) {
                lab = Lbl.PARSE_ERR
                continue@sm
            }
            forit[forx] = 0 // initial value
            writeCstring(ftmp, cstringAt(svPtr.pd1, svPtr.sdlim))
            if (svPtr.xd[svPtr.xpx] == ',' ||
                svPtr.xd[svPtr.xpx] == ' ' ||
                svPtr.xd[svPtr.xpx] == NUL) {
                lab = Lbl.INIT_LOOP_VAR
                continue@sm
            }
            if (svPtr.xd[svPtr.xpx] != ':') {
                lab = Lbl.ARG_LIST_ERROR
                continue@sm
            }
            // vendor 1559-1573: increment amount
            val xdtmp = CharArray(STR_MAX)
            svPtr.symflg = 0 // store
            sym_(0, forv[forx], 0, ftmp, 0, svPtr)
            svPtr.t2 = 1
            svPtr.xpx++
            writeCstring(xdtmp, cstringAt(svPtr.xd, svPtr.xpx))
            var xdtmpx = svPtr.xpx
            if (parseErr(svPtr)) {
                lab = Lbl.PARSE_ERR
                continue@sm
            }
            xdtmpx = svPtr.xpx - xdtmpx
            xdtmp[xdtmpx] = NUL
            forit[forx] = 1
            // increment
            writeCstring(forin[forx], cstringAt(xdtmp))
            if (svPtr.xd[svPtr.xpx] == ',' ||
                svPtr.xd[svPtr.xpx] == NUL ||
                svPtr.xd[svPtr.xpx] == ' ' ||
                svPtr.xd[svPtr.xpx] == TAB) {
                lab = Lbl.INIT_LOOP_VAR
                continue@sm
            }
            if (svPtr.xd[svPtr.xpx] != ':') {
                lab = Lbl.ARG_LIST_ERROR
                continue@sm
            }
            svPtr.t2 = 1
            svPtr.xpx++
            if (parseErr(svPtr)) {
                lab = Lbl.PARSE_ERR
                continue@sm
            }
            forlt[forx] = 1 // limit
            writeCstring(forlv[forx], cstringAt(svPtr.pd1, svPtr.sdlim))
            if (svPtr.xd[svPtr.xpx] == ',' ||
                svPtr.xd[svPtr.xpx] == ' ' ||
                svPtr.xd[svPtr.xpx] == NUL ||
                svPtr.xd[svPtr.xpx] == TAB) {
                lab = Lbl.INIT_LOOP_VAR
                continue@sm
            }
            lab = Lbl.ARG_LIST_ERROR
        }

        Lbl.INIT_LOOP_VAR -> {
            // vendor 1599-1616: init_loop_var
            svPtr.symflg = 0 // store
            // set value
            sym_(0, forv[forx], 0, ftmp, 0, svPtr)
            if (forin[forx][0] == DOLLAR) {
                i = if (strlenC(ftmp) == 0) 1 else 0
                forit[forx] = 1
                lab = Lbl.ARGLESS
                continue@sm
            } else i = 0
            if (forlt[forx] == 1) {
                val zero = CharArray(2) { NUL }
                writeCstring(zero, "0")
                if (numcomp(forin[forx], zero) >= 0) {
                    if (numcomp(ftmp, forlv[forx]) > 0) i = 1
                } else {
                    if (numcomp(ftmp, forlv[forx]) < 0) i = 1
                }
            }
            lab = Lbl.ARGLESS
        }

        Lbl.ARGLESS -> {
            // vendor 1618-1629: argless
            writeCstring(cmdstack, cstringAt(svPtr.xd, svPtr.xpx), cmdx)
            cmdx = cmdx + strlenC(svPtr.xd, svPtr.xpx) + 1
            lab = Lbl.BYP
        }

        Lbl.BYP -> {
            // vendor 1631-1640: byp
            while (svPtr.xd[svPtr.xpx] != NUL && svPtr.xd[svPtr.xpx] != ' ') {
                svPtr.t2 = 1
                svPtr.xpx++
                parse_(svPtr) // vendor: return value discarded
            }
            if (i == 0) lab = Lbl.NEXT_CMND else lab = Lbl.QUIT1
        }

        Lbl.FOR_END -> {
            // vendor 1643-1740: for_end - end of for return
            if (forit[forx] != 1) {
                lab = Lbl.FOR_QUIT
                continue@sm
            }
            // increment loop variable
            val itmp = CharArray(STR_MAX)
            svPtr.symflg = 1 // retrieve
            sym_(11, forv[forx], 0, itmp, 0, svPtr)
            val old = CharArray(STR_MAX)
            val oldxpx = svPtr.xpx
            writeCstring(old, cstringAt(svPtr.xd, 1))
            writeCstring(svPtr.xd, cstringAt(forin[forx])) // vendor: strcpy(xd, &forin[forx][0])
            svPtr.xpx = 0
            svPtr.t2 = 1
            if (parseErr(svPtr)) {
                lab = Lbl.PARSE_ERR
                continue@sm
            }
            // dsve = &pd1[sdlim]
            if (forin[forx][0] != DOLLAR) {
                add(itmp, 0, svPtr.pd1, svPtr.sdlim, ftmp, 0)
            } else {
                writeCstring(ftmp, cstringAt(svPtr.pd1, svPtr.sdlim))
            }
            writeCstring(svPtr.xd, cstringAt(old), 1)
            svPtr.xpx = oldxpx
            if (BreakFlag != 0) {
                BreakFlag = 0
                lab = Lbl.FOR_QUIT
                continue@sm
            }
            if (forlt[forx] != 1) {
                svPtr.symflg = 0 // store
                sym_(0, forv[forx], 0, ftmp, 0, svPtr)
                gsbx++
                if (gsbx >= GOSUBMAX) {
                    lab = Lbl.STACK_OVERFLOW
                    continue@sm
                }
                cmdx = cmdxold
                if (svPtr.xd[svPtr.xpx] == ',') {
                    i = 0
                    lab = Lbl.BYP
                    continue@sm
                }
                lab = Lbl.NEXT_CMND
                continue@sm
            }
            // limit test
            svPtr.symflg = 0 // store
            sym_(0, forv[forx], 0, ftmp, 0, svPtr)
            if (forin[forx][0] == DOLLAR) {
                if (forlv[forx][0] == NUL) { // special case null string limit
                    if (strlenC(ftmp) == 0) {
                        lab = Lbl.FOR_QUIT
                        continue@sm
                    }
                } else {
                    if (strcmpC(ftmp, forlv[forx]) >= 0) {
                        lab = Lbl.FOR_QUIT
                        continue@sm
                    }
                }
            } else {
                val zero = CharArray(2) { NUL }
                writeCstring(zero, "0")
                if (numcomp(forin[forx], zero) >= 0) {
                    if (numcomp(ftmp, forlv[forx]) > 0) {
                        lab = Lbl.FOR_QUIT
                        continue@sm
                    }
                } else {
                    if (numcomp(ftmp, forlv[forx]) < 0) {
                        lab = Lbl.FOR_QUIT
                        continue@sm
                    }
                }
            }
            gsbx++
            if (gsbx >= GOSUBMAX) {
                lab = Lbl.STACK_OVERFLOW
                continue@sm
            }
            cmdx = cmdxold
            if (svPtr.xd[svPtr.xpx] == ',') {
                i = 0
                lab = Lbl.BYP
                continue@sm
            }
            lab = Lbl.NEXT_CMND
        }

        Lbl.FOR_QUIT -> {
            // vendor 1646-1656: for_quit
            if (svPtr.xd[svPtr.xpx] == ',') {
                svPtr.xpx++
                gsbx++
                if (gsbx >= GOSUBMAX) {
                    lab = Lbl.STACK_OVERFLOW
                    continue@sm
                }
                lab = Lbl.NXT_ARG
                continue@sm
            }
            forx--
            svPtr.xpx = STR_MAX
            lab = Lbl.NEXT_CMND
        }

        Lbl.CMD_HALT -> {
            // vendor 1742-1818: HALT
            if ((svPtr.xd[svPtr.xpx] == ' ' || svPtr.xd[svPtr.xpx] == NUL) &&
                toupperC(svPtr.xd[p2px + 1]) != 'T') {
                Mltoa(getpidC(), tmp2)
                if (svPtr.LockDone != 0) cleanLocks(cstringAt(tmp2))
                Mglobal(CLOSE, tmp2, 0, tmp3, svPtr) // Close Globals
                System.out.flush() // vendor: fflush(NULL)
                kotlin.system.exitProcess(0) // vendor: _exit(EXIT_SUCCESS)
                // vendor: the unreachable return EXIT_SUCCESS dropped (h013)
            }
            if (toupperC(svPtr.xd[p2px + 1]) == 'T') { // html command
                // vendor: int pd1cur=pd1Last+svPtr->xpx-2 (shadows,
                // h015 rename pd1curL)
                var pd1curL = pd1Last + svPtr.xpx - 2
                while (svPtr.pd1[pd1curL] != NUL && svPtr.pd1[pd1curL] != ' ') pd1curL++
                if (svPtr.pd1[pd1curL] != NUL) pd1curL++
                while (svPtr.pd1[pd1curL] != NUL) {
                    if (svPtr.pd1[pd1curL] == '&' && svPtr.pd1[pd1curL + 1] == PREDICATEVAR) {
                        pd1curL += 2
                        i = 1
                        while (svPtr.pd1[pd1curL] != PREDICATEVAR &&
                               svPtr.pd1[pd1curL] != NUL) {
                            svPtr.xd[i] = svPtr.pd1[pd1curL++]
                            i++
                        }
                        svPtr.xd[i] = NUL
                        if (svPtr.pd1[pd1curL] == PREDICATEVAR) pd1curL++
                        svPtr.t2 = 1
                        svPtr.xpx = 1
                        if (parseErr(svPtr)) {
                            lab = Lbl.PARSE_ERR
                            continue@sm
                        }
                        print(cstringAt(svPtr.pd1, svPtr.sdlim))
                        continue
                    }
                    if (svPtr.pd1[pd1curL] == '&' && svPtr.pd1[pd1curL + 1] == '!') {
                        pd1curL += 2
                        putcharC('\n')
                        continue
                    }
                    putcharC(svPtr.pd1[pd1curL])
                    pd1curL++
                }
                pd1curL++
                lab = Lbl.NEXT_LINE
                continue@sm
            }
            lab = Lbl.HANG
        }

        Lbl.HANG -> {
            // vendor 1727-1736: hang
            svPtr.t2 = 1
            if (parseErr(svPtr)) {
                lab = Lbl.PARSE_ERR
                continue@sm
            }
            j = atoiC(svPtr.pd1, svPtr.sdlim)
            if (j > 0) {
                // NATIVE_SHARED: the Mglobal(PULSE) deadlock-prevention
                // block is excluded (reference build, h018)
                Thread.sleep(j * 1000L) // vendor: sleep(j)
            }
            if (svPtr.xd[svPtr.xpx] == ',') {
                svPtr.xpx++
                lab = Lbl.HANG
                continue@sm
            }
            lab = Lbl.NEXT_CMND
        }

        Lbl.CMD_ELSE -> {
            // vendor 1820-1826: ELSE
            if (svPtr.xd[svPtr.xpx] == ' ') svPtr.xpx++
            if (svPtr.tpx == 0L) {
                lab = Lbl.NEXT_CMND
                continue@sm
            }
            svPtr.xpx = STR_MAX
            lab = Lbl.NEXT_CMND
        }

        Lbl.CMD_DO -> {
            // vendor 1828-1870: DO (or DATABASE)
            if (toupperC(svPtr.xd[p2px + 1]) == 'E') { // DECLARE command
                lab = Lbl.NEXT_LINE // ignore
                continue@sm
            }
            if (toupperC(svPtr.xd[p2px + 1]) == 'A') { // DATABASE command
                svPtr.t2 = 1
                while (svPtr.xd[svPtr.xpx] != NUL) {
                    if (svPtr.xd[svPtr.xpx] != ' ') break
                    svPtr.xpx++
                }
                // vendor: the double self-assignment svPtr->ierr =
                // svPtr->ierr = parse_(svPtr) is idempotent (fossil kept)
                if (parseErr(svPtr)) {
                    lab = Lbl.PARSE_ERR
                    continue@sm
                }
                if (strlenC(svPtr.pd1, svPtr.sdlim) > 1000) {
                    lab = Lbl.FILE_ERROR
                    continue@sm
                }
                writeCstring(svPtr.UDAT, cstringAt(svPtr.pd1, svPtr.sdlim))
                strcatC(svPtr.UDAT, ".dat")
                writeCstring(svPtr.UKEY, cstringAt(svPtr.pd1, svPtr.sdlim))
                strcatC(svPtr.UKEY, ".key")
                lab = Lbl.NEXT_LINE
                continue@sm
            }
            lab = Lbl.DO_RET1
        }

        Lbl.DO_RET1 -> {
            // vendor 1872-1891: do_ret1
            argless = 0
            NewFlag = 0
            if (svPtr.xd[svPtr.xpx] == ' ' || svPtr.xd[svPtr.xpx] == NUL) {
                val tmp = CharArray(1024)
                strcpyC(tmp, "+0  ")
                ExecLevel++
                ExecLevNew[ExecLevel] = 0
                strcatC(tmp, cstringAt(svPtr.xd, svPtr.xpx))
                writeCstring(svPtr.xd, cstringAt(tmp), 1)
                svPtr.xpx = 1
                argless = 1
            }
            pd1hold1 = svPtr.pd1len.toInt()
            pd1hold2 = pd1cur
            svPtr.iorg = 1
            shold = 0
            retz = DO.code
            lab = Lbl.LBL
        }

        Lbl.DO_RET -> {
            // vendor 1893-1914: do_ret
            if (++gsbx >= GOSUBMAX) {
                lab = Lbl.STACK_OVERFLOW
                continue@sm
            }
            if (shold > 0) gosub[gsbx][0] = shold else gosub[gsbx][0] = 0
            gosub[gsbx][1] = pd1hold2
            gosub[gsbx][2] = cmdx
            gosub[gsbx][8] = NewFlag
            writeCstring(cmdstack, cstringAt(svPtr.xd, svPtr.xpx), cmdx)
            cmdx = cmdx + strlenC(svPtr.xd, svPtr.xpx) + 1
            gosub[gsbx][3] = DO.code
            gosub[gsbx][4] = pd1hold1
            pd1cur = ibr
            gosub[gsbx][5] = 0
            gosub[gsbx][7] = svPtr.tpx.toInt()
            if (argless == 0) {
                gosub[gsbx][6] = ExecLevel
                ExecLevel = 1
            }
            if (SymInc == 0) { // flag indicates that inc was done already
                svPtr._Sym++
                svPtr.start[svPtr._Sym] = null
            }
            lab = Lbl.NEXT_LINE
        }

        Lbl.CMD_NEW -> {
            // vendor 1916-1918: NEW - case entry (nflg=0 runs once;
            // the new_again re-entry skips it, vendor label position)
            svPtr.nflg = 0
            lab = Lbl.NEW_AGAIN
        }

        Lbl.NEW_AGAIN -> {
            // vendor 1918-1958: new_again
            if (svPtr.xd[svPtr.xpx] == ' ' ||
                svPtr.xd[svPtr.xpx] == NUL ||
                svPtr.xd[svPtr.xpx] == TAB) {
                // new except none
                svPtr.symflg = 7
                svPtr.nstart = null // list freed in sym_() (vendor comment)
                valBuf[0] = NUL // the empty-name/value carrier
                sym_(7, valBuf, 0, valBuf, 0, svPtr) // sym_(7, "", "", svPtr)
                svPtr.nstart = null
                ExecLevNew[ExecLevel] = 1
                lab = Lbl.NEW_TAIL
                continue@sm
            }
            // New indirect
            if (svPtr.xd[svPtr.xpx] == '@') {
                svPtr.kflg = 1
                svPtr.t0px = 1
                if (parseErr(svPtr)) {
                    lab = Lbl.PARSE_ERR
                    continue@sm
                }
                svPtr.kflg = 0
            }
            // New specific: new a,b,c
            if (svPtr.xd[svPtr.xpx] != '(') {
                svPtr.t0px = 1
                if (parseErr(svPtr)) {
                    lab = Lbl.PARSE_ERR
                    continue@sm
                }
                writeCstring(valBuf, "") // the empty-name carrier
                sym_(100, svPtr.v1d, 1, valBuf, 0, svPtr)
                lab = Lbl.NEW_TAIL
                continue@sm
            }
            // New all locals except: linked list of var names
            svPtr.nstart = null
            svPtr.xpx++
            lab = Lbl.NEW2
        }

        Lbl.NEW2 -> {
            // vendor 1937-1955: new2
            svPtr.t0px = 1
            if (parseErr(svPtr)) {
                lab = Lbl.PARSE_ERR
                continue@sm
            }
            val p1 = Nmes(cstringAt(svPtr.v1d, 1), null)
            keyfixAt(svPtr.v1d, 1) // in case its an array
            p1.name = cstringAt(svPtr.v1d, 1)
            p1.next = svPtr.nstart as Nmes?
            svPtr.nstart = p1
            svPtr.xpx++
            if (svPtr.xd[svPtr.xpx] != ')' && svPtr.xd[svPtr.xpx] != NUL) {
                svPtr.xpx++
                lab = Lbl.NEW2
                continue@sm
            }
            svPtr.symflg = 7
            // new except
            valBuf[0] = NUL
            sym_(7, valBuf, 0, valBuf, 0, svPtr) // sym_(7, "", "", svPtr)
            svPtr.nstart = null
            ExecLevNew[ExecLevel] = 1
            lab = Lbl.NEW_TAIL
        }

        Lbl.NEW_TAIL -> {
            // vendor 1956-1960: the new_again re-entry test
            if (svPtr.xd[++svPtr.xpx] == ',') {
                svPtr.xpx++
                lab = Lbl.NEW_AGAIN
                continue@sm
            }
            lab = Lbl.NEXT_CMND
        }

        Lbl.CMD_KILL -> {
            // vendor 1962-2042: DATAKILL (kill_again label)
            lab = Lbl.KILL_AGAIN
        }

        Lbl.KILL_AGAIN -> {
            // vendor 1964-2022: kill_again
            // Kill all local variables
            if (svPtr.xd[svPtr.xpx] == ' ' ||
                svPtr.xd[svPtr.xpx] == NUL ||
                svPtr.xd[svPtr.xpx] == TAB) {
                svPtr.symflg = 4
                valBuf[0] = NUL
                sym_(4, valBuf, 0, valBuf, 0, svPtr) // sym_(4, "", "", svPtr)
                lab = Lbl.KILL_TAIL
                continue@sm
            }
            // Kill indirect
            if (svPtr.xd[svPtr.xpx] == '@') {
                svPtr.kflg = 1
                svPtr.t0px = 1
                if (parseErr(svPtr)) {
                    lab = Lbl.PARSE_ERR
                    continue@sm
                }
                svPtr.kflg = 0
            }
            // Kill specific - local or global
            if (svPtr.xd[svPtr.xpx] != '(') {
                svPtr.t0px = 1
                if (parseErr(svPtr)) {
                    lab = Lbl.PARSE_ERR
                    continue@sm
                }
                if (svPtr.v1d[1] != '^') { // kill a local
                    svPtr.symflg = 2 // kill
                    writeCstring(valBuf, "")
                    sym_(2, svPtr.v1d, 1, valBuf, 0, svPtr)
                } else { // globals
                    keyfixAt(svPtr.v1d, 1)
                    // original lngth - comp for start
                    k = strlenC(svPtr.v1d, 1) + 1
                    if (svPtr.v1d[k - 1] != 1.toChar()) svPtr.v1d[k++] = 1.toChar()
                    svPtr.v1d[k] = NUL
                    k--
                    writeCstring(tmp2, cstringAt(svPtr.v1d, 1))
                    // kill first
                    Mglobal(GKILL, svPtr.v1d, 1, svPtr.bd, svPtr)
                    // the SQLITE reference build EXCLUDES the sibling
                    // XNEXT kill loop (vendor k1 label,
                    // #if !(defined(SQLITE)) - h018 excluded arm)
                }
                lab = Lbl.KILL_TAIL
                continue@sm
            }
            // Kill all locals except: linked list of var names
            svPtr.nstart = null
            svPtr.xpx++
            lab = Lbl.KILL2
        }

        Lbl.KILL2 -> {
            // vendor 2011-2030: kill2
            svPtr.t0px = 1
            if (parseErr(svPtr)) {
                lab = Lbl.PARSE_ERR
                continue@sm
            }
            val p1 = Nmes(cstringAt(svPtr.v1d, 1), null)
            keyfixAt(svPtr.v1d, 1) // in case its an array
            p1.name = cstringAt(svPtr.v1d, 1)
            p1.next = svPtr.nstart as Nmes?
            svPtr.nstart = p1
            svPtr.xpx++
            if (svPtr.xd[svPtr.xpx] != ')' && svPtr.xd[svPtr.xpx] != NUL) {
                svPtr.xpx++
                lab = Lbl.KILL2
                continue@sm
            }
            svPtr.symflg = 5
            valBuf[0] = NUL
            sym_(5, valBuf, 0, valBuf, 0, svPtr) // sym_(5, "", "", svPtr)
            svPtr.nstart = null
            lab = Lbl.KILL_TAIL
        }

        Lbl.KILL_TAIL -> {
            // vendor 2032-2036: the kill_again re-entry test
            if (svPtr.xd[++svPtr.xpx] == ',') {
                svPtr.xpx++
                lab = Lbl.KILL_AGAIN
                continue@sm
            }
            lab = Lbl.NEXT_CMND
        }

        Lbl.CMD_FILECLOSE -> {
            // vendor 2038-2062: FILECLOSE
            p2px++
            if (svPtr.xd[p2px].code <= 90)
                svPtr.xd[p2px] = (svPtr.xd[p2px].code + 32).toChar() // lower case
            if (svPtr.xd[p2px] == 'o') {
                lab = Lbl.QUIT
                continue@sm
            }
            lab = Lbl.NEXT_CLOSE_ARG
        }

        Lbl.NEXT_CLOSE_ARG -> {
            // vendor 2044-2058: next_close_arg
            svPtr.t2 = 1
            if (parseErr(svPtr)) {
                lab = Lbl.PARSE_ERR
                continue@sm
            }
            i = atoiC(svPtr.pd1, svPtr.sdlim)
            if (svPtr.in_file[i] == null && svPtr.out_file[i] == null) {
                lab = Lbl.FILE_ERROR
                continue@sm
            }
            val fr = svPtr.in_file[i]
            val fw = svPtr.out_file[i]
            try {
                if (fr != null) fr.close()
                else if (fw != null) fw.close()
            } catch (e: java.io.IOException) {
                // vendor: fclose rarely fails; the close proceeds (kept)
            }
            svPtr.in_file[i] = null
            svPtr.out_file[i] = null
            if (svPtr.xd[svPtr.xpx] != ',') {
                lab = Lbl.NEXT_CMND
                continue@sm
            }
            svPtr.xpx++
            lab = Lbl.NEXT_CLOSE_ARG
        }

        Lbl.CMD_USE -> {
            // vendor 2064-2078: USE
            svPtr.t2 = 1
            if (parseErr(svPtr)) {
                lab = Lbl.PARSE_ERR
                continue@sm
            }
            svPtr.io = atoiC(svPtr.pd1, svPtr.sdlim)
            if (svPtr.io <= 0 || svPtr.io > 10) {
                lab = Lbl.ARG_LIST_ERROR
                continue@sm
            }
            if (svPtr.in_file[svPtr.io] == null && svPtr.out_file[svPtr.io] == null) {
                lab = Lbl.USE_NOT_OPEN
                continue@sm
            }
            lab = Lbl.NEXT_CMND
        }

        Lbl.LHSREF -> {
            // vendor 2086-2132: lhsref - remote block to process lhs
            // type references
            if (vd[1] == '^') {
                writeCstring(svPtr.v1d, cstringAt(vd, 1)) // vendor: strcpy(v1d, &vd[1])
                writeCstring(svPtr.bd, cstringAt(svPtr.pd1, svPtr.sdlim))
                if (MergeFlag == 0) Mglobal(STORE, svPtr.v1d, 0, svPtr.bd, svPtr)
                else Merge(svPtr.v1d, 0, MergeRHS, 1, svPtr) // InterpStubs stub (batch-8 owner)
            } else {
                if (vd[1] == DOLLAR && (vd[2] == 'p' || vd[2] == 'P')) { // dollar-piece
                    writeCstring(tmp3, cstringAt(svPtr.pd1, svPtr.sdlim))
                    writeCstring(svPtr.v1d, cstringAt(vd, 1), 1)
                    svPtr.setpiece = 1
                    fcn(svPtr)
                    svPtr.setpiece = 0
                    lab = Lbl.LHSREF1
                    continue@sm
                } else if (vd[1] == DOLLAR && (vd[2] == 'e' || vd[2] == 'E')) { // dollar-extract
                    writeCstring(tmp3, cstringAt(svPtr.pd1, svPtr.sdlim))
                    writeCstring(svPtr.v1d, cstringAt(vd, 1), 1)
                    svPtr.setpiece = 1
                    fcn(svPtr)
                    svPtr.setpiece = 0
                    lab = Lbl.LHSREF1
                    continue@sm
                } else if (vd[1] == DOLLAR && (vd[2] == 'z' || vd[2] == 'Z')) { // dollar-z variable
                    writeCstring(tmp3, cstringAt(svPtr.pd1, svPtr.sdlim))
                    writeCstring(svPtr.v1d, cstringAt(vd, 1), 1)
                    svPtr.setpiece = 1
                    fcn(svPtr)
                    svPtr.setpiece = 0
                    lab = Lbl.LHSREF1
                    continue@sm
                } else {
                    svPtr.symflg = 0 // store
                    sym_(0, vd, 1, svPtr.pd1, svPtr.sdlim, svPtr)
                }
            }
            lab = Lbl.LHSREF1
        }

        Lbl.LHSREF1 -> {
            // vendor 2134-2137: lhsref1
            if (retz == READ.code) lab = Lbl.CONT_READ else lab = Lbl.SET_RETURN
        }

        Lbl.LBL -> {
            // vendor 2147-2404: lbl - remote block to process DO and
            // GOTO arguments
            SymInc = 0
            if (svPtr.xd[svPtr.xpx] == NUL) {
                lab = Lbl.LBL_ERROR
                continue@sm
            }
            // indirect argument
            if (svPtr.xd[svPtr.xpx] == '@') {
                svPtr.t0px = 1
                svPtr.t2 = 2
                if (parseErr(svPtr)) {
                    lab = Lbl.LBL_ERROR
                    continue@sm
                }
                if (svPtr.t0px == 0) {
                    // copy label part to do save
                    var q = 1
                    while (svPtr.v1d[q] != NUL && svPtr.v1d[q].code < 127) q++
                    // vendor: strncpy(dosave, &v1d[1], i-1); dosave[i-1]=0
                    for (r in 0 until q - 1) dosave[r] = svPtr.v1d[1 + r]
                    dosave[q - 1] = NUL
                    if (svPtr.v1d[q] != NUL) {
                        // arguments follow the label
                        writeCstring(svPtr.args, cstringAt(svPtr.v1d, q + 1))
                    }
                } else {
                    writeCstring(dosave, cstringAt(svPtr.pd1, svPtr.sdlim))
                }
                if (svPtr.t0px == 0) svPtr.xpx++
            }
            // builtin function
            else if (svPtr.xd[svPtr.xpx] == DOLLAR) {
                svPtr.t2 = 1
                if (parseErr(svPtr)) {
                    lab = Lbl.PARSE_ERR
                    continue@sm
                }
                lab = Lbl.NEXT_CMND
                continue@sm
            }
            // extract label and offset from xd
            else {
                i = 0
                j = svPtr.xpx
                while (svPtr.xd[svPtr.xpx] != NUL) {
                    if (svPtr.xd[svPtr.xpx] == ',' || svPtr.xd[svPtr.xpx] == ' ' ||
                        svPtr.xd[svPtr.xpx] == NUL || svPtr.xd[svPtr.xpx] == '+' ||
                        svPtr.xd[svPtr.xpx] == '(' ||
                        svPtr.xd[svPtr.xpx] == '^' || svPtr.xd[svPtr.xpx] == ':') break
                    dosave[i++] = svPtr.xd[svPtr.xpx++]
                }
                dosave[i] = NUL
            }
            // handles do xxx(a,b,c) - does not handle do xxx^yyy(a,b,c)
            if (svPtr.xd[svPtr.xpx] == '(' || svPtr.xd[svPtr.xpx].code == OPEN_PAREN) {
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
                    svPtr.xd[svPtr.xpx] = DOLLAR
                    if (parseErr(svPtr)) {
                        lab = Lbl.PARSE_ERR
                        continue@sm
                    }
                    svPtr.xpx++
                    writeCstring(svPtr.args, cstringAt(svPtr.v1d, 3))
                }
            }
            k = -1
            if (svPtr.xd[svPtr.xpx] == '+') {
                svPtr.t2 = 2
                if (parseErr(svPtr)) {
                    lab = Lbl.PARSE_ERR
                    continue@sm
                }
                k = atoiC(svPtr.pd1, svPtr.sdlim)
                if (k < 0) {
                    lab = Lbl.LBL_ERROR
                    continue@sm
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
                if (parseErr(svPtr)) {
                    lab = Lbl.FILE_ERROR
                    continue@sm
                }
                if (fileflg != 0) {
                    writeCstring(ftmp, cstringAt(svPtr.v1d, 1))
                    svPtr.v1d[1] = '^'
                    writeCstring(svPtr.v1d, cstringAt(ftmp), 2)
                }
                if (svPtr.xd[svPtr.xpx] != ':') svPtr.xpx++
            }
            if (svPtr.xd[svPtr.xpx] == ':') {
                svPtr.xpx++
                svPtr.t2 = 1
                svPtr.t0px = 0
                writeCstring(ftmp, cstringAt(svPtr.v1d, 1))
                if (parseErr(svPtr)) {
                    lab = Lbl.POST_COND_ERROR
                    continue@sm
                }
                writeCstring(svPtr.v1d, cstringAt(ftmp), 1)
                if (atoiC(svPtr.pd1, svPtr.sdlim) == 0) {
                    if (svPtr.xd[svPtr.xpx] == ',') {
                        svPtr.xpx++
                        lab = Lbl.LBL
                        continue@sm
                    } else {
                        lab = Lbl.NEXT_CMND
                        continue@sm
                    }
                }
            }
            if (dosave[0] == '^') {
                writeCstring(svPtr.v1d, cstringAt(dosave), 1)
                dosave[0] = NUL
            }
            if (svPtr.v1d[1] != NUL) {
                if (retz == DO.code) {
                    svPtr.iorg = 1
                    shold = svPtr.pd1len.toInt()
                    val snap = ByteArray(shold)
                    for (q in 0 until shold) snap[q] = svPtr.pd1[1 + q].code.toByte()
                    swp[swapextent] = snap
                    shold = swapextent
                    swapextent++
                    if (swapextent >= SWAP) {
                        lab = Lbl.STACK_OVERFLOW
                        continue@sm
                    }
                }
                // extract file name from line - avoids args
                var q2 = 0
                val ftmp1 = CharArray(512) // limit on file name size
                while (q2 < 511 && isprintC(svPtr.v1d[q2 + 2])) {
                    ftmp1[q2] = svPtr.v1d[q2 + 2]
                    q2++
                }
                ftmp1[q2] = NUL
                // load file
                readf_(ftmp1, svPtr.iorg.toShort(), svPtr)
                if (ftmp1[0] == NUL) {
                    lab = Lbl.FILE_ERROR
                    continue@sm
                }
                // remove file name leaving args if any
                var x = 1
                var y = q2 + 2
                while (svPtr.v1d[y] != NUL) {
                    svPtr.v1d[x] = svPtr.v1d[y]
                    x++
                    y++
                }
                svPtr.v1d[x] = NUL
                // PROCESSED ARGS: copy already processed args to arg string
                writeCstring(svPtr.args, cstringAt(svPtr.v1d, 2))
                svPtr.LineNumber = 0
                lblx = 0
                if (svPtr.v1d[2] == NUL) {
                    lab = Lbl.FILE_ERROR
                    continue@sm
                }
            }
            var found = false
            ibr = 0
            while (ibr < lblx) {
                if (strcmpC(lbls[ibr], dosave) == 0) {
                    ibr = lbloff[ibr]
                    lab = Lbl.OFFSET
                    found = true
                    break
                }
                ibr++
            }
            if (found) continue@sm
            // search for label in code
            ibr = 1
            if (dosave[0] != NUL) {
                while (ibr < svPtr.pd1len) {
                    if (svPtr.pd1[ibr] == TAB ||
                        svPtr.pd1[ibr] == ' ' ||
                        svPtr.pd1[ibr] == '#' ||
                        svPtr.pd1[ibr] == ';') { // no label
                        while (svPtr.pd1[ibr++] != NUL && ibr < svPtr.pd1len) { /* scan eol */ }
                    } else { // something in column 1
                        j = 0
                        while (svPtr.pd1[ibr] == dosave[j] && dosave[j] != NUL) {
                            // compare dosave label with text
                            j++
                            ibr++
                        }
                        if ((svPtr.pd1[ibr] == TAB || // label found
                             svPtr.pd1[ibr] == ' ' || // delimiter
                             svPtr.pd1[ibr] == '(' || // parameters
                             svPtr.pd1[ibr] == NUL) && // label alone on line
                            dosave[j] == NUL) {
                            // add found label to table if room
                            if (lblx < LBLS - 1 && j < 10 && svPtr.pd1[ibr] != '(') {
                                lblx++
                                writeCstring(lbls[lblx], cstringAt(dosave))
                                lbloff[lblx] = ibr
                            }
                            lab = Lbl.OFFSET
                            found = true
                            break
                        }
                        while (svPtr.pd1[ibr++] != NUL && ibr < svPtr.pd1len) { /* scan eol */ }
                    }
                }
                if (!found) {
                    lab = Lbl.LBL_ERROR
                    continue@sm
                }
            } else {
                // not do xxx^yyy but may be do xxx^yy(123)
                while (svPtr.pd1[ibr] != ' ' &&
                       svPtr.pd1[ibr] != TAB &&
                       svPtr.pd1[ibr] != NUL &&
                       svPtr.pd1[ibr] != '(') ibr++
            }
            lab = Lbl.OFFSET
        }

        Lbl.OFFSET -> {
            // vendor 2420-2516: offset - pass arguments to subroutine
            if (svPtr.pd1[ibr] == '(') {
                val varBuf = CharArray(256)
                var jj = ibr + 1
                val iargs = IntArray(11) // vendor: int iargs[10]={0} (1-based fill)
                var nargs = 0
                var count = 0
                svPtr.exec++ // indicates subroutine call level
                // advance to new symbol table level for subroutine
                svPtr._Sym++
                svPtr.start[svPtr._Sym] = null
                SymInc = 1 // flag to prevent inc from happening again
                var q = 0
                while (svPtr.args[q] != NUL) {
                    if (svPtr.args[q].code == 207 || svPtr.args[q].code == 208) {
                        svPtr.args[q] = NUL
                        nargs++
                        iargs[nargs] = q + 1
                    }
                    q++
                }
                var ii = 0
                while (svPtr.pd1[jj] != ' ' && svPtr.pd1[jj] != NUL) {
                    if (svPtr.pd1[jj] == NUL) {
                        lab = Lbl.LBL_ERROR
                        continue@sm
                    }
                    if (svPtr.pd1[jj] == ',' || svPtr.pd1[jj] == ')') {
                        if (count >= nargs) { // arg count mismatch - pass zeros
                            varBuf[ii] = NUL
                        } else {
                            varBuf[ii] = NUL
                            // code 100 means store at this level only
                            var symflg = 100
                            var t: CharArray? = null
                            val tmpS = CharArray(STR_MAX)
                            if (svPtr.args[iargs[count]] == '.') {
                                writeCstring(svPtr.reference,
                                             cstringAt(svPtr.args, iargs[count] + 1))
                                if (sym_(SymRetrieve, svPtr.args, iargs[count] + 1,
                                         tmpS, 0, svPtr) == null) {
                                    println("*** Var: " +
                                            cstringAt(svPtr.args, iargs[count] + 1))
                                    lab = Lbl.VAR_NOT_FOUND
                                    continue@sm
                                }
                                t = tmpS
                                symflg = 200
                            } else {
                                t = svPtr.args
                                // (the vendor passes the args pointer at
                                // iargs[count]; the offset carries it)
                            }
                            // vendor 2462: t = &args[iargs[count]] in the
                            // else arm; the symflg-200 arm sets t=tmp offset 0
                            if (t === svPtr.args) {
                                sym_(symflg, varBuf, 0, svPtr.args, iargs[count], svPtr)
                            } else {
                                sym_(symflg, varBuf, 0, t, 0, svPtr)
                            }
                        }
                        ii = 0
                        jj++
                        count++
                        if (svPtr.pd1[jj] == ')') break
                        continue
                    }
                    varBuf[ii++] = svPtr.pd1[jj++]
                }
            }
            // end argument passing section
            if (k > 0) { // offset from label
                for (q in 0 until k) {
                    while (svPtr.pd1[ibr++] != NUL && ibr < svPtr.pd1len) { /* scan eol */ }
                    if (ibr >= svPtr.pd1len) {
                        lab = Lbl.LBL_ERROR
                        continue@sm
                    }
                }
                while (svPtr.pd1[ibr] != TAB && svPtr.pd1[ibr] != ' ') ibr++
                if (ibr >= svPtr.pd1len) {
                    lab = Lbl.LBL_ERROR
                    continue@sm
                }
            } else if (k == 0) ibr = pd1cur
            // else pd1cur = ibr (vendor fossil comment kept)
            if (svPtr.xd[svPtr.xpx] != NUL && svPtr.xd[svPtr.xpx] != ' ') svPtr.xpx++
            if (retz == GOTO.code) lab = Lbl.GOTO_RETURN else lab = Lbl.DO_RET
        }

        Lbl.ERR_CALL -> {
            // vendor 2518-2523: err_call
            ernbr = 0
            if (svPtr.NOERR != 0) {
                lab = Lbl.ABRT1
                continue@sm
            }
            System.err.printf("\n*** %d Control-C\n", ernbr)
            lab = Lbl.ABRT1
        }

        Lbl.PARSE_ERR -> {
            // vendor 2525-2533: parse_err
            ernbr = svPtr.ierr
            prnterr(ernbr, svPtr, pd1cur)
            if (svPtr.NOERR != 0) {
                svPtr.NOERR = -1
                lab = Lbl.NEXT_CMND
                continue@sm
            }
            lab = Lbl.ABRT1
        }

        Lbl.POST_COND_ERROR -> {
            // vendor 2535-2538: post_cond_error
            ernbr = 6
            prnterr(ernbr, svPtr, pd1cur)
            lab = Lbl.ABRT1
        }

        Lbl.LBL_ERROR -> {
            // vendor 2540-2544: lbl_error
            ernbr = 8
            prnterr(ernbr, svPtr, pd1cur)
            println(" " + cstringAt(dosave))
            lab = Lbl.ABRT
        }

        Lbl.VAR_NOT_FOUND -> {
            // vendor 2546-2549: var_not_found
            ernbr = 17
            prnterr(ernbr, svPtr, pd1cur)
            lab = Lbl.ABRT
        }

        Lbl.DEFAULT_ERR -> {
            // vendor 2551-2554: the switch default (never-referenced
            // error labels ref_err/symtab_err/program_size/sql_error
            // are vendor roster notes only, h013)
            ernbr = 13
            prnterr(ernbr, svPtr, pd1cur)
            lab = Lbl.ABRT1
        }

        Lbl.ARG_LIST_ERROR -> {
            // vendor 2560-2563: arg_list_error
            ernbr = 14
            prnterr(ernbr, svPtr, pd1cur)
            lab = Lbl.ABRT1
        }

        Lbl.FILE_ERROR -> {
            // vendor 2571-2574: file_error
            ernbr = 26
            prnterr(ernbr, svPtr, pd1cur)
            lab = Lbl.ABRT1
        }

        Lbl.IO_ERROR -> {
            // vendor 2576-2579: io_error
            ernbr = 20
            prnterr(ernbr, svPtr, pd1cur)
            lab = Lbl.ABRT1
        }

        Lbl.STACK_OVERFLOW -> {
            // vendor 2587-2590: stack_overflow
            ernbr = 32
            prnterr(ernbr, svPtr, pd1cur)
            lab = Lbl.ABRT1
        }

        Lbl.USE_NOT_OPEN -> {
            // vendor 2596-2599: use_not_open
            ernbr = 36
            prnterr(ernbr, svPtr, pd1cur)
            lab = Lbl.ABRT1
        }

        Lbl.ABRT1 -> {
            // vendor 2601-2603: abrt1 (the commented-out print kept as note)
            lab = Lbl.ABRT
        }

        Lbl.ABRT -> {
            // vendor 852-866 (inside the ZCMDS zexit block):
            // TERMINATE_ON_ERROR is NOT defined in the reference
            // build - the #else arm renders: return 99
            return 99
        }

        Lbl.RESTORE -> {
            // vendor 2605-2610: restore
            for (q in swapextent until SWAP) swp[q] = null
            return 0
        }
        }
    }
}

/** h002: vendor strcmp over whole C-string carriers (per-file). */
private fun strcmpC(a: CharArray, b: CharArray): Int {
    val sa = cstringAt(a)
    val sb = cstringAt(b)
    return if (sa == sb) 0 else if (sa > sb) 1 else -1
}

/** The double-quote byte (vendor '"' literal; named to keep the
 *  scan gate's comment-close rule simple). */
private const val QUOTE2 = '"'
