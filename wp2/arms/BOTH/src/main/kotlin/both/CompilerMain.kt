package both

// Unit 308 render: compiler-mumps.c::main (roster 308, batch 10,
// 1372 LOC, score 1125.2, the BOTH roster's final unit; the old
// prose pin in CompilerMumpsMisc.kt said "roster-303/304", a
// both_seq-range label, corrected at the C-015 sweep). The
// compiler driver: argument and file setup, the output prologue
// emission (includes, defines, the floatstring/numcomp2/strxcat/
// strcatz inline emission, SyncClock, set_alarm), the leading
// plus-define scan, the next_line label machine (inline C,
// comments, function/label entry processing, the declared-label
// chain, doty indents, the xd copy), and the next_cmnd command
// dispatcher (command-word spelling check, postconditionals, the
// full command switch, the error label family).
//
// The vendor goto lattice folds to a when-state machine (the
// ParseCPCtx precedent): the labels next_line/next_cmnd/pstrtn/
// do_entry/another_do/job_return/cntnue/ctch/gexit/next_goto are
// the machine states; the error labels converge on abrt1 (the
// return-9 exit); abrt_exit folds to the abrtx owner stub.
//
// Consults (rule base wp2/arms/BOTH/rules_v0.pl):
//   h001 io_stream_param - reused: fopen "w"/"r" fold to the
//     java.io.FileWriter/FileReader seats (g.Out the :106 Writer
//     seat, CompilerOutState.out registered with it; the input
//     rides a PushbackReader over g.inFile the :149 Reader seat -
//     the getc/ungetc pair of the plus-define scan is the
//     PushbackReader.unread fold); printf folds to System.out.
//   h004 globals_ext - reused: the driver globals ride the
//     CompilerGlobalsExt singleton (pd1/pd1cur/pd1len/pd1Last/
//     cur_line/tmp2G/dosave/xdG/xpxG/t2G/t0pxG/ierr/sdlim the
//     extern pins; Out/output_file/pid/lines the :106/:118/:158
//     seats).
//   h007 char_buffer - reused: tmp/tmp4096/ttt/str/word are
//     scan-only local CharArrays; the C string ops fold to
//     cStringOf/writeC/strcatC helpers.
//   h009 format_string - reused: every fprintf emission keeps the
//     exact tab/quote/percent shape; the percent chars in the
//     dead main-emission block stay token-shaped.
//   h011 goto - reused: the label machine folds to the when-state
//     loop (forward gotos only; the back edges next_line/
//     next_cmnd are the loop states).
//   h013 dead_store - hit: the two-scan TAB probe before
//     pd1cur=i is overwritten dead code (ledgered); the
//     if(!MAIN_flg&&0) and if(0) main-emission blocks are dead
//     (dropped, ledgered); the unreachable while(1) parse_ tail
//     after the DO next_cmnd goto is dead (dropped, ledgered);
//     the case FCN 'D' arm is unreachable through the tolower
//     of icmnd (kept as a when arm, ledgered); the dosave[i]
//     == '(' test cannot fire (kept, ledgered).
//   h014 out_of_scope_call - hit: new_code, lock_code, if_code,
//     for_code, errmod_, doty, copyright_notice and abrtx are
//     pending roster owners - TODO stubs raised below with
//     crosswalk-true pins; set_code/read_code/write_code/
//     kill_code/mumps_open/zcommands_code/parse_/Extract2/
//     atTest/atCopy/fcninit/linepad/EP call their landed owners
//     direct; atTest is the roster-109 stub (its pd1Last
//     pointer-out rides the owner render - the pointer seat is
//     ledgered here).
//   h015 grammar-collision - reused: ASCII only, no backticks,
//     no raw comment dollars, trailing newline.
//   h017 naming - hit: compilerMain keeps the vendor main name
//     shaped per the mumpsMain precedent; the raised stubs keep
//     vendor names (new_code, lock_code, if_code, for_code,
//     errmod_, doty, copyright_notice, abrtx); the command chars
//     ride private CMD_ consts (the command-char block of
//     compiler-macros.h:90-121; SET is 's' - the macros header
//     redefines SET 0 at :83 then 's' at :91, the LAST define
//     wins, ledgered).
//   h019 class_member_carrier - reused: the LabelsStart/TextStart
//     chains ride the Txt seats (unit 151 owners); the cvars
//     table rides the Dcs seats (unit 151).
//   h020 pointer_handle - reused: the doty pointer-out family
//     rides the owner stub (ledgered); &pd1[sdlim] folds to the
//     pd1Str slice idiom.
//
// Quirks kept (ledgered):
//   - the no-extension branch copies argv[1], not argv[argc-1],
//     into tmp2G (vendor bug kept).
//   - the successful fgets sets pd1len=2048 although the fgets
//     limit is 512 (kept); EOF sets pd1cur=9999/pd1len=1 so the
//     pd1cur>pd1len test fires.
//   - the trailing <32 strips index the LAST character (the
//     strlen(&pd1[1]) arithmetic lands on the final char), so the
//     CR/LF strips are live, then the tabs-to-spaces sweep is
//     bounded at 512.
//   - the plus-define scan fprintf adds a second newline when the
//     fgets string kept its own (kept byte-faithful).
//   - TryFlg is reset to 1 (not 0) in the end-of-line TryFlg
//     close (the vendor self-assignment quirk kept).
//   - the C-subfunction '+' parameter scan and the sym_(100)
//     argument scans keep their exact delimiter splices,
//     including the strlen==0 break that leaves pd1[j] nulled.
//   - the HTML emission keeps the C escape-doubling verbatim
//     (backslash-n as two chars, percent doubling).
//   - the SET entry passes 's' (115) to set_code - the macros
//     header SET redefinition, last define wins.

// command-char block (compiler-macros.h:90-121; private - the
// interp-side CMMNT/XECUTE/ZCMDS char consts of InterpHMisc are
// a different seat family)
private const val CMD_CLSQ = '}'
private const val CMD_OPSQ = '{'
private const val CMD_TRY = 't'
private const val CMD_CMMNT = ';'
private const val CMD_NEW = 'n'
private const val CMD_BREAK = 'b'
private const val CMD_LOCK = 'l'
private const val CMD_READ = 'r'
private const val CMD_OPEN = 'o'
private const val CMD_ZCMDS = 'z'
private const val CMD_IF = 'i'
private const val CMD_WRITE = 'w'
private const val CMD_JOB = 'j'
private const val CMD_MERGE = 'm'
private const val CMD_SET = 's'
private const val CMD_QUIT = 'q'
private const val CMD_XECUTE = 'x'
private const val CMD_FOR = 'f'
private const val CMD_HALT = 'h'
private const val CMD_ELSE = 'e'
private const val CMD_DO = 'd'
private const val CMD_KILL = 'k'
private const val CMD_CLOSE = 'c'
private const val CMD_USE = 'u'
private const val CMD_GOTO = 'g'
private const val CMD_FCN = 'D'

// NEXT_LINE 8 / BAD_LOCK 9 (compiler-macros.h:76-77)
private const val NEXT_LINE_CM = 8
private const val BAD_LOCK_CM = 9

// KILL_DCL_ERROR 7 (compiler-macros.h:75; the CompilerKillCode
// seat is file-private, so this driver carries its own pin)
private const val KILL_DCL_ERROR_CM = 7

private val NUL_CM = 0.toChar()
private val TAB_CM = 9.toChar() // compiler-macros.h:129 (default build)

// the state machine labels (h011)
private const val L_NEXT_LINE = 1
private const val L_NEXT_CMND = 2
private const val L_PSTRTN = 3
private const val L_DO_ENTRY = 4
private const val L_ANOTHER_DO = 5
private const val L_JOB_RETURN = 6
private const val L_CNTNUE = 7
private const val L_CTCH = 8
private const val L_GEXIT = 9
private const val L_NEXT_GOTO = 10

private val CM_CTABLE = arrayOf(
    "r", "read",
    "w", "write", "while",
    "i", "if",
    "o", "open",
    "s", "set", "sql", "sql", "shell",
    "j", "job",
    "g", "goto",
    "m", "merge",
    "n", "new",
    "q", "quit",
    "x", "xecute",
    "f", "for",
    "h", "halt", "hang", "html",
    "e", "else", "export",
    "d", "do", "declare", "database",
    "k", "kill",
    "c", "close", "continue", "catch",
    "u", "use",
    "l", "lock",
    "t", "try",
    ";",
    "zascii", "zmain", "znumber", "zd", "zexit", "zfunction",
    "b", "break",
    "",
)

private fun isAlphaC(c: Char): Boolean = (c in 'a'..'z') || (c in 'A'..'Z')

private fun isAlnumC(c: Char): Boolean = isAlphaC(c) || isDigitC(c)

private fun isDigitC(c: Char): Boolean = c in '0'..'9'

private fun toLowerC(c: Char): Char = if (c in 'A'..'Z') (c + ' '.code) else c

private fun toUpperC(c: Char): Char = if (c in 'a'..'z') (c - ' '.code) else c

// fprintf(Out, ...) fold returning the written count (the
// sys1OutZb precedent)
private fun cmOutZb(s: String): Int {
    try {
        CompilerOutState.out.write(s)
    } catch (e: java.io.IOException) {
        // vendor ignores the fprintf return (failure is silent)
    }
    return s.length
}

// Sysout(x) macro fold - compiler-macros.h:52
private fun sysoutCm(x: String) {
    cmOutZb(x)
}

// INTERPRET macro fold - compiler-macros.h:37-42 (the five-
// fprintf emission over tmp)
private fun interpretCm(tmp: String) {
    cmOutZb("\t_jj = Interpret( (CCHR) \n")
    cmOutZb("\t\t\"" + tmp + "\", svPtr); \n")
    cmOutZb("\tsvPtr->ERROR = _jj; \n")
    cmOutZb("\tif (!svPtr->NOERR && _jj < 0) \n")
    cmOutZb("\t\tErrorMessage(\"Indirection error\", svPtr->LineNumber); \n")
}

// strcat fold onto a CharArray buffer
private fun strcatC(dst: CharArray, src: String) {
    var off = 0
    while (off < dst.size && dst[off] != NUL_CM) off++
    for (idx in src.indices) {
        if (off >= dst.size) break
        dst[off++] = src[idx]
    }
    if (off < dst.size) dst[off] = NUL_CM
}

// strcpy-at-offset fold (the &pd1[1] family)
private fun writeStr(dst: CharArray, off: Int, src: String) {
    var p = off
    for (idx in src.indices) {
        dst[p++] = src[idx]
    }
    dst[p] = NUL_CM
}

// the banner emission (the repeated \n//--------------\n block)
private fun bannerCm(name: String) {
    cmOutZb("\n//--------------\n")
    cmOutZb("//       " + name + "\n")
    cmOutZb("\n//--------------\n\n")
}

// c-string slice of pd1 from an offset (the &pd1[..] fold)
private fun pd1Str(off: Int): String {
    val sb = StringBuilder()
    var p = off
    while (p < CompilerGlobalsExt.pd1.size && CompilerGlobalsExt.pd1[p] != NUL_CM) {
        sb.append(CompilerGlobalsExt.pd1[p])
        p++
    }
    return sb.toString()
}

// c-string slice of xd from an offset
private fun xdStr(off: Int): String {
    val sb = StringBuilder()
    var p = off
    while (p < CompilerGlobalsExt.xdG.size && CompilerGlobalsExt.xdG[p] != NUL_CM) {
        sb.append(CompilerGlobalsExt.xdG[p])
        p++
    }
    return sb.toString()
}

// fgets(str, n, in) fold over the PushbackReader input seat
private fun fgetsC(buf: CharArray, off: Int, max: Int): Boolean {
    val rd = CompilerGlobalsExt.inFile as? java.io.PushbackReader ?: return false
    var p = off
    var got = false
    while (p - off < max - 1) {
        val c = rd.read()
        if (c < 0) break
        buf[p++] = c.toChar()
        got = true
        if (c.toChar() == '\n') break
    }
    if (p < buf.size) buf[p] = NUL_CM
    return got
}

// abrt1 fold: the shared error exit - printf(" in or near line
// %d\n"), printf("***\t%s \n\n", &pd1[pd1Last]), ExitCode=ernbr,
// return 9
private fun abrt1Cm(ernbrVal: Int): Int {
    val g = CompilerGlobalsExt
    System.out.print(" in or near line " + g.lines + "\n")
    System.out.print("***\t" + pd1Str(g.pd1Last.toInt()) + " \n\n")
    g.ernbr = ernbrVal.toShort()
    g.ExitCode = ernbrVal.toShort()
    return 9
}

// int new_code() - compiler-new.c. Owner: compiler-new.c::new_code
// (roster 91, batch 3, pending). TODO stub (h014).
internal fun new_code(): Int {
    TODO("stub: owner compiler-new.c::new_code (roster 91, batch 3)")
}

// int lock_code() - compiler-lock.c. Owner: compiler-lock.c::
// lock_code (roster 116, batch 4, pending). TODO stub (h014).
internal fun lock_code(): Int {
    TODO("stub: owner compiler-lock.c::lock_code (roster 116, batch 4)")
}

// int if_code() - compiler-if.c. Owner: compiler-if.c::if_code
// (roster 54, batch 2, pending). TODO stub (h014).
internal fun if_code(): Int {
    TODO("stub: owner compiler-if.c::if_code (roster 54, batch 2)")
}

// int for_code() - compiler-for.c. Owner: compiler-for.c::for_code
// (roster 122, batch 4, pending). TODO stub (h014).
internal fun for_code(): Int {
    TODO("stub: owner compiler-for.c::for_code (roster 122, batch 4)")
}

// void errmod_(int err) - compiler-sys1.c. Owner:
// compiler-sys1.c::errmod_ (roster 87, batch 3, pending).
// TODO stub (h014).
internal fun errmod_(err: Int) {
    TODO("stub: owner compiler-sys1.c::errmod_ (roster 87, batch 3)")
}

// int doty(...) - compiler-doty.c. Owner: compiler-doty.c::doty
// (roster 117, batch 4, pending). The vendor pointer-out family
// (ExecLevel/ExecName/pd1Last/ExecX) rides the owner render.
// TODO stub (h014, h020).
internal fun doty(j: Int): Int {
    TODO("stub: owner compiler-doty.c::doty (roster 117, batch 4)")
}

// void copyright_notice(FILE *) - compiler-copyright-notice.c.
// Owner: compiler-copyright-notice.c::copyright_notice (roster
// 47, batch 2, pending). The FILE* seat is the Writer edge.
// TODO stub (h014, h001).
internal fun copyright_notice(out: java.io.Writer) {
    TODO("stub: owner compiler-copyright-notice.c::copyright_notice (roster 47, batch 2)")
}

// int abrtx() - compiler-abrtx.c. Owner: compiler-abrtx.c::abrtx
// (roster 18, batch 1, pending). TODO stub (h014).
internal fun abrtx(): Int {
    TODO("stub: owner compiler-abrtx.c::abrtx (roster 18, batch 1)")
}

// int main(int argc, char *argv[]) - compiler-mumps.c:2
internal fun compilerMain(argc: Int, argv: Array<String>): Int {
    val g = CompilerGlobalsExt
    var returnCode: Int

    g.symlen = PS.toShort()
    g.TextStart = null
    writeC(g.FunctionName, "")
    g.pid = ProcessHandle.current().pid()

    System.out.print("The Mumps Compiler\n")

    if (argc == 2 && argv[1] == "-v") return 99

    if (argc == 1) {
        System.out.print("*** \u0007 Name of program to compile must be given\n\n")
        g.ExitCode = 99
        return 99
    }

    if (argc == 3) {
        if (argv[1] == "-SUB") {
            g.SubFlag = 1
        } else {
            System.out.print("*** Unrecognized option: " + argv[1] + "\n\n")
            g.ExitCode = 99
            return 99
        }
    }

    // for (i = strlen(argv[argc-1]); i >= 0 && argv[argc-1][i]
    // != '.'; i--);  - the extension dot scan
    var di = argv[argc - 1].length // C reads the NUL at [len] first
    while (di >= 0 && !(di < argv[argc - 1].length && argv[argc - 1][di] == '.')) di--

    if (di < 0) {
        // vendor copies argv[1] here, not argv[argc-1] (bug kept)
        writeC(g.tmp2G, argv[1])
    } else {
        writeC(g.tmp2G, argv[argc - 1].substring(0, di))
    }

    strcatC(g.tmp2G, ".m")

    g.Out = java.io.FileWriter(cStringOf(g.tmp2G))
    CompilerOutState.out = g.Out!!

    writeC(g.output_file, cStringOf(g.tmp2G))

    val inPush = java.io.PushbackReader(java.io.FileReader(argv[argc - 1]))
    g.inFile = inPush

    System.out.print("Translating " + argv[argc - 1] + " to C++:\n")

    copyright_notice(g.Out!!)

    // Check for leading #define symbols to be included first
    run {
        val str = CharArray(512)
        while (true) {
            val ci = inPush.read()
            if (ci != '+'.code) {
                inPush.unread(ci)
                break
            }
            if (!fgetsC(str, 0, 512)) {
                System.out.print("Unexpected end of file\n")
                g.ExitCode = 99
                return abrtx() // goto abrtx;
            }
            cmOutZb(cStringOf(str) + "\n")
        }
    }

    // Write Include, Defines and Global Defs to Output File
    sysoutCm("#undef _MDH_\n")
    sysoutCm("#include <stdio.h>\n")
    sysoutCm("#include <string.h>\n")
    sysoutCm("#include <stdlib.h>\n")
    sysoutCm("#include <setjmp.h>\n")
    sysoutCm("#include <unistd.h>\n")
    sysoutCm("#include <signal.h>\n")
    sysoutCm("#include <sys/time.h>\n")
    sysoutCm("#include <mumpsc/inline.h>\n")
    sysoutCm("#include <mumpsc/defines.h>\n")
    sysoutCm("#include <mumpsc/strmax.h>\n")
    sysoutCm("#include <mumpsc/libmpscpp.h>\n")
    sysoutCm("#include <mumpsc/sym.h>\n")
    sysoutCm("#include <mumpsc/builtin.h>\n")
    sysoutCm("#include <mumpsc/compiler.h>\n")
    sysoutCm("#define FORPARM STR_MAX  /* Size of For loop parameter */\n")
    sysoutCm("#define CSTR (char *)\n")
    sysoutCm("#define UNS unsigned char *\n")
    sysoutCm("#define uns unsigned char *\n")
    sysoutCm("#define CCHR const char *\n")
    sysoutCm("#include <mumpsc/keyfix.h>\n")
    sysoutCm("#include <mumpsc/globalOrder.h>\n")

    if (g.SubFlag.toInt() != 0) sysoutCm("#define SUB\n")

    sysoutCm("#include <mumpsc/stateVector.h>\n")
    sysoutCm("int (*__label_lookup)(char *);\n")
    sysoutCm("const char * (*__text_function)(int);\n")

    sysoutCm("void fcn(MSV *);\n")
    cmOutZb("void zfcn(unsigned char *, unsigned char *, struct MSV *); /* z functions */\n")

    cmOutZb("char * sym_(int, unsigned char *, unsigned char *, struct MSV *); /* symbol table */\n")

    cmOutZb("char * makeLock(char *, char *, char *, char, char *); /* global array lock service */\n")
    cmOutZb("char * cleanLocks(char *); /* global array lock service */\n")
    cmOutZb("char * releaseLock(char *, char *, char *); /* global array lock service */\n")
    cmOutZb("char * eraseLocks(char *); /* global array lock service */\n")
    cmOutZb("void add(const char *a,const char *b,char *c); /* math function */\n")
    cmOutZb("void sub(const char *a,const char *b,char *c); /* math function */\n")
    cmOutZb("void mult(const char *a,const char *b,char *c); /* math function */\n")
    cmOutZb("void divx(const char *a,const char *b,char *c); /* math function */\n")
    cmOutZb("void divi(const char *a,const char *b,char *c); /* math function */\n")
    cmOutZb("void _fnumber(unsigned char *,unsigned char *,\n")
    cmOutZb("       unsigned char *,unsigned char *, MSV *); /* math function */\n")
    cmOutZb("int DBSem = 0; /* deadlock prevention */\n")
    cmOutZb("int pm (unsigned char [], const unsigned char pattern[], struct MSV * svPtr); /* pattern match */\n")

    // functions
    sysoutCm("\nstatic inline int floatstring(const char * p) { \n")
    sysoutCm(" int f=0;  \n")
    sysoutCm(" for (; *p!=0; p++) {  \n")
    sysoutCm("\t if (    isdigit(*p) || \n")
    sysoutCm("\t \t*p == '-' ||  \n")
    sysoutCm("\t\t*p == '+' ) continue;  \n")
    sysoutCm(" \tif (    *p=='.' ||  \n")
    sysoutCm("\t\ttolower(*p)=='e' ) { \n")
    sysoutCm("\t\tf=1;  \n")
    sysoutCm("\t\tcontinue;  \n")
    sysoutCm("\t\t} \n")
    sysoutCm(" return 0; \n")
    sysoutCm(" }  \n")
    sysoutCm(" return f;  \n")
    sysoutCm(" } \n ")

    sysoutCm("\ninline int numcomp2(unsigned char *aa) { \n")
    sysoutCm(" if (floatstring((char *) aa)) return (atof( (char *) aa) != 0.0);  \n")
    sysoutCm(" return atoi((char *) aa);  \n")
    sysoutCm(" }\n")

    sysoutCm("\ninline char * strxcat(char * a, char *b)  \n")
    sysoutCm("{ while (*a) a++; while( (*(a) = *(b)) ) { a++; b++; } return a; }\n")

    sysoutCm("\ninline char * strcatz(char * out, char *in)  \n")
    sysoutCm("{ while (*out) out++; while (*(out) = *(in)) { out++; in++; } *(out++) = '\u0001';  \n")
    sysoutCm("*out = 0; return (char *) out; } \n\n")

    cmOutZb("void SyncClock() {\n")
    cmOutZb("    DBSem = 1;\n")
    cmOutZb("    }\n\n")

    cmOutZb("unsigned int set_alarm (unsigned int seconds) {\n")
    cmOutZb("    struct itimerval old, xnew;\n")
    cmOutZb("    xnew.it_interval.tv_usec = 0;\n")
    cmOutZb("    xnew.it_interval.tv_sec = 0 ; // interval between alarms\n")
    cmOutZb("    xnew.it_value.tv_usec = 0;\n")
    cmOutZb("    xnew.it_value.tv_sec = (long int) 0;\n")
    cmOutZb("    if (setitimer (ITIMER_REAL, &xnew, &old) < 0)\n")
    cmOutZb("        return 0;\n")
    cmOutZb("    else\n")
    cmOutZb("        return old.it_value.tv_sec;\n")
    cmOutZb("    }\n\n")

    linepad(cmOutZb("#include <mumpsc/global.h>"), "Global array definitions")

    linepad(0, "Interface to Mglobal():")
    linepad(0, "g = command")
    linepad(0, "key = key sought or stored")
    linepad(0, "bd = value returned or stored")
    linepad(0, "svPtr = address of state vector")

    sysoutCm("#ifndef SUB\n")
    linepad(cmOutZb("#include <mumpsc/fcns.h>"), "System support functions")
    linepad(cmOutZb("#include <mumpsc/sysfunc.h>"), "Many common global variables")
    sysoutCm("#endif\n")

    // Compiler Start
    g.pd1cur = 1
    g.symlen = PS.toShort()
    g.t0pxG = 0
    g.t2G = 0
    g.cmdx = 0
    g.setpiece = 0
    g.forxmax = 0
    g.forx = 0
    g.forxFLG = 0
    g.forLine = 0
    g.pd1Last = 0
    g.ExecLevel = 0
    g.SymIncrFlg = 0
    g.ArgMax = 0
    g.IF_flg_max = 0
    g.cvarx = 0

    for (fbi in 0 until FBMAX) g.ForBlocks[fbi] = -1

    var pc = L_NEXT_LINE
    var xpx: Long = 0
    var p2px = 0
    var icmnd = ' '
    var n = 0
    var cntnueEntry = false

    machine@ while (true) {
        when (pc) {

        L_NEXT_LINE -> {
            g.EmitLabel = 0
            g.iiitmp = 0 // new temp pool for each line
            g.forLine = 0
            g.CatchFlg = 0

            writeStr(g.pd1, 1, "") // array content begins at 1, not 0

            if (!fgetsC(g.pd1, 1, 512)) { // input line
                g.pd1cur = 9999
                g.pd1len = 1
                writeStr(g.pd1, 1, "")
            } else {
                g.pd1cur = 1 // start
                g.pd1len = 2048
            }

            // the two <32 strips land on the LAST character
            var sl = pd1Str(1).length
            if (g.pd1[sl] < ' ') g.pd1[sl] = NUL_CM // <cr>
            sl = pd1Str(1).length
            if (g.pd1[sl] < ' ') g.pd1[sl] = NUL_CM // <lf>

            var ti = 1
            while (g.pd1[ti] != NUL_CM && ti < 512) {
                if (g.pd1[ti] == '\t') g.pd1[ti] = ' ' // tabs to blanks
                ti++
            }

            g.lines++

            while (g.IF_flg > 0) { // closing braces
                cmOutZb("\t}\n")
                g.IF_flg--
            }

            if (g.pd1cur.toInt() > g.pd1len.toInt()) {
                if (g.ExecLevel.toInt() > 0) {
                    while (g.ExecLevel > 0) {
                        cmOutZb("\t} while (0);   // dotted un-indent " + (g.ExecX.toInt() - 1) + "\n")
                        g.ExecX--
                        cmOutZb("\tlongjmp(Efor" + g.ExecStack[g.ExecX.toInt()] + "J,99);    // return \n")
                        cmOutZb("\nDo_Block_Bypass" + g.ExecStack[g.ExecX.toInt()] + ": ; //A541 \n")
                        g.ExecLevel--
                    }
                    g.ExecLevel = 0
                }
                return abrtx() // abrt_exit
            }

            if (g.pd1[g.pd1cur.toInt()] == NUL_CM) {
                g.pd1cur++
                pc = L_NEXT_LINE
                continue@machine
            }

            writeC(g.cur_line, pd1Str(g.pd1cur.toInt())) // copy next program line

            // inline C code - emit as is
            if (g.pd1[g.pd1cur.toInt()] == '%' || g.pd1[g.pd1cur.toInt()] == '+') {
                cmOutZb("\n//--------------\n")
                cmOutZb("//       Inline\n")
                cmOutZb("\n//--------------\n\n")

                cmOutZb(pd1Str(g.pd1cur.toInt() + 1) + "\n")
                while (g.pd1[g.pd1cur.toInt()] != NUL_CM) g.pd1cur++
                g.pd1cur++
                pc = L_NEXT_LINE
                continue@machine
            }

            if (g.pd1[g.pd1cur.toInt()] == '#' ||
                (g.pd1[g.pd1cur.toInt()] == '/' && g.pd1[g.pd1cur.toInt() + 1] == '/')
            ) { // comment
                if (g.pd1[g.pd1cur.toInt()] == '/') g.pd1cur++
                cmOutZb("\n// " + pd1Str(g.pd1cur.toInt() + 1) + "\n")
                g.pd1cur++
                while (g.pd1[g.pd1cur.toInt()] != NUL_CM) g.pd1cur++
                g.pd1cur++

                // the vendor if (!MAIN_flg && 0) main-emission
                // block here is dead (h013, dropped)

                pc = L_NEXT_LINE
                continue@machine
            }

            // ---- label field scan ----
            var li = g.pd1cur.toInt()
            // (the vendor two-scan TAB probe before the pd1cur=i
            // restore is overwritten dead code - h013, dropped)
            g.pd1cur = li.toShort()

            if (g.pd1[g.pd1cur.toInt()] != TAB_CM) {
                while (g.pd1[g.pd1cur.toInt()] != TAB_CM && g.pd1[g.pd1cur.toInt()] != NUL_CM) {
                    if (g.pd1[g.pd1cur.toInt()] == '(') {
                        while (g.pd1[g.pd1cur.toInt()] != ')' && g.pd1[g.pd1cur.toInt()] != NUL_CM) {
                            g.pd1cur++
                        }
                    }
                    g.pd1cur++
                }
                g.pd1[g.pd1cur.toInt()] = NUL_CM

                run {
                    var lflg = 1
                    var ii: Int
                    var jj: Int
                    var k: Int
                    var kk = 0
                    n = 0
                    var thistime = 0

                    if (g.pd1[li] == '^' && g.pd1[li + 1] == '^') { // function start
                        li += 2
                        g.F_flg = 1
                        if (g.MAIN_flg.toInt() != 0) EP(0, g.ExecName, g.pid) // entry point router
                        if (g.FCN_flg.toInt() != 0) EP(1, g.ExecName, g.pid) // entry point router
                        g.FCN_flg = 1 // am in subfunction
                        thistime = 1
                    } else if (g.pd1[li] == '^' && g.pd1[li + 1] != '^') { // function start
                        li += 1
                        g.F_flg = 1
                        if (g.MAIN_flg.toInt() != 0) EP(0, g.ExecName, g.pid)
                        if (g.FCN_flg.toInt() != 0) EP(1, g.ExecName, g.pid)
                        g.FCN_flg = 1
                        thistime = 1
                    }

                    ii = li
                    while (g.pd1[li] != NUL_CM) {
                        // open paren in label - must be function ep
                        if (g.pd1[li] == '(') {
                            if (lflg != 0) {
                                sysoutCm("\n//=================================================================================\n")
                                cmOutZb("//\tEntry Point " + pd1Str(ii) + " + + + + + + + \n")
                                sysoutCm("\n//=================================================================================\n")
                                lflg = 0 // arguments found
                            }

                            g.pd1[li] = NUL_CM

                            if (g.F_flg == 0 && g.MAIN_flg.toInt() == 0) {
                                System.out.print("*** 92 Missing zmain")
                                return abrt1Cm(92) // Missing_Zmain
                            }

                            if (g.F_flg != 0 && thistime != 0) { // this is a C subfunction
                                var ff = 0
                                g.cvarx = 0
                                writeC(g.FunctionName, pd1Str(ii))

                                cmOutZb("char * " + pd1Str(ii) + "(struct MSV * svPtr, char * offset")

                                if (g.pd1[li + 1] != ')') cmOutZb(",")

                                jj = li + 1
                                while (g.pd1[jj] != NUL_CM) {
                                    // non mumpsc parameter
                                    if (g.pd1[jj] == '+') {
                                        jj++
                                        while (true) {
                                            cmOutZb(g.pd1[jj].toString())
                                            jj++
                                            if (g.pd1[jj] == ',' || g.pd1[jj] == ')') break
                                        }
                                        if (g.pd1[jj] == ')') {
                                            ff = 0
                                        } else {
                                            cmOutZb(",")
                                            jj++
                                            continue
                                        }
                                    } else {
                                        if (isAlphaC(g.pd1[jj])) ff = 1
                                        if ((g.pd1[jj] == ',' || g.pd1[jj] == ')') && ff != 0) {
                                            cmOutZb("const char * _Arg" + kk + g.pd1[jj])
                                            kk++
                                        }
                                    }
                                    // fe1:
                                    if (g.pd1[jj] == ')') {
                                        if (ff == 0) cmOutZb(") ")
                                        cmOutZb("{\n\n")
                                        fcninit(g.output_file, g.pid)
                                        break
                                    }
                                    jj++
                                }

                                cmOutZb("__label_lookup=" + cStringOf(g.FunctionName) + "_label_lookup; \n")
                                cmOutZb("__text_function=" + cStringOf(g.FunctionName) + "_text_function; \n\n")

                                cmOutZb("try { // try001 \n ")

                                sysoutCm("svPtr->_SymPush++; \n")
                                sysoutCm("_SymPush++; \n")
                                sysoutCm("svPtr->start[svPtr->_Sym]=NULL; \n")
                                g.SymIncrFlg = 1
                                cmOutZb("if (svPtr->_Sym > SYM_MAX) ")
                                cmOutZb("ErrorMessage(\"Too many function calls\",svPtr->LineNumber); \n")
                                cmOutZb("_doStackN[_doStx-1] = 1; // flag to restore namespace\n")
                                sysoutCm("\tif (svPtr->Rargsx) { // EP1005\n")
                                sysoutCm("\t\tfor (_i = 0; _i < svPtr->Rargsx; _i++) strcpy(_Refs[_i], svPtr->Rargs[_i]); \n")
                                sysoutCm("\t\t_Refsx = svPtr->Rargsx; \n")
                                sysoutCm("\t\t} \n")
                            } else {
                                if (!isAlnumC(g.pd1[ii])) {
                                    System.out.print("*** Labels must begin with and alphabetic character")
                                    return abrt1Cm(22) // lab_err
                                }
                                if (isDigitC(g.pd1[ii])) cmOutZb("_")
                                cmOutZb(pd1Str(ii) + ": \n") // source code Label
                                sysoutCm("svPtr->start[svPtr->_Sym] = NULL; \n")
                                g.SymIncrFlg = 1
                                sysoutCm("_SymPush++; \n")
                                cmOutZb("if (svPtr->_Sym > SYM_MAX) ")
                                cmOutZb("ErrorMessage(\"Too many function calls\",svPtr->LineNumber); \n")
                                cmOutZb("_doStackN[_doStx-1]=1; // flag to restore namespace \n")
                            }

                            g.pd1[li] = '('
                            jj = li + 1
                            while (g.pd1[jj] != NUL_CM) {
                                if (g.pd1[jj] == '+') {
                                    while (true) {
                                        if (g.pd1[jj] == ',' || g.pd1[jj] == ')') break
                                        jj++
                                    }
                                    if (g.pd1[jj] == ')') {
                                        jj++
                                        break
                                    }
                                    li = jj
                                    jj++
                                    continue
                                }
                                if (g.pd1[jj] == ',' || g.pd1[jj] == ')') {
                                    k = g.pd1[jj].code
                                    g.pd1[jj] = NUL_CM
                                    if (pd1Str(li + 1).isEmpty()) break

                                    if (g.F_flg != 0) {
                                        // C function
                                        cmOutZb(
                                            "\tif (_Arg" + n + "!=NULL) \n\t\t" +
                                                "sym_(100, (UNS) \"" + pd1Str(li + 1) + "\", (UNS) _Arg" + n + ", svPtr);" +
                                                " // Store Arguments EP1001\n",
                                        )
                                        cmOutZb(
                                            "\tstrcpy (svPtr->RefParm[" + n + "], \"" + pd1Str(li + 1) + "\" ); // EP1002\n",
                                        )
                                    } else {
                                        cmOutZb(
                                            "\tif (__Arg" + n + "!=NULL) sym_(100, (UNS) \"" + pd1Str(li + 1) + "\", (UNS) " +
                                                "__Arg" + n + ",svPtr); // Store Arguments EP1003 \n",
                                        )
                                        cmOutZb(
                                            "\tstrcpy (svPtr->RefParm[" + n + "], \"" + pd1Str(li + 1) + "\" ); // EP1004 \n",
                                        )
                                    }

                                    n++
                                    if (n > g.ArgMax.toInt()) g.ArgMax = n.toShort()
                                    g.pd1[jj] = k.toChar()
                                    if (k == ')'.code) break
                                    li = jj
                                }
                                jj++
                            }

                            if (g.F_flg != 0 && thistime != 0) {
                                cmOutZb("\tgoto _eps;  // entry point label router \n")
                                cmOutZb("_fcn: \n")
                            }
                        }
                        li++
                    }

                    if (lflg == 0 && g.F_flg == 0) {
                        cmOutZb(
                            "\tif (_ArgCount < " + n + ") ErrorMessage(\"Bad argument count\", svPtr->LineNumber); \n",
                        )
                    }

                    if (lflg != 0) {
                        var si = ii
                        while (g.pd1[si] != NUL_CM) {
                            if (g.pd1[si] != ' ') { // label
                                g.EmitLabel = 1

                                if (!isAlnumC(g.pd1[ii])) {
                                    System.out.print("*** Labels must begin with and alphabetic character")
                                    return abrt1Cm(22) // lab_err
                                }

                                val nm = (if (isDigitC(g.pd1[ii])) "_" else "") + pd1Str(ii)
                                if (g.LabelsStart == null) { // linked list setup
                                    g.LabelsStart = Txt(nm, g.lines, null)
                                } else { // existing linked list
                                    g.p1 = Txt(nm, g.lines, g.LabelsStart)
                                    g.LabelsStart = g.p1
                                }
                                break
                            }
                            si++
                        }
                    }

                    g.pd1[g.pd1cur.toInt()] = TAB_CM // replace
                }
            }

            while (g.pd1[g.pd1cur.toInt()] == TAB_CM) g.pd1cur++

            // Doty Indents
            if (g.pd1[g.pd1cur.toInt()] == '.') {
                var dj = 0
                var dscan = g.pd1cur.toInt()
                while (g.pd1[dscan] == '.' || g.pd1[dscan] == ' ') {
                    if (g.pd1[dscan] == '.') dj++ // how many levels?
                    dscan++
                }

                if (dj > 10) {
                    System.out.print("*** Too many block indents\n")
                    return abrt1Cm(99) // abrt1 via ernbr=99
                }

                if (dj > g.ExecLevel.toInt() + 1) { // skipped a level
                    System.out.print("*** Code level skip\n")
                    return abrt1Cm(99)
                }

                // ExecLevel set to j in function
                returnCode = doty(dj)
                if (returnCode != 0) {
                    // the vendor ernbr = doty(...) assignment quirk
                    return abrt1Cm(returnCode)
                }

                g.pd1cur = dscan.toShort()
            } else if (g.ExecLevel.toInt() > 0) {
                while (g.ExecLevel > 0) {
                    cmOutZb("\t} while(0); // dotted un-indent " + (g.ExecX.toInt() - 1) + " \n")
                    g.ExecX--
                    cmOutZb("\tlongjmp(Efor" + g.ExecStack[g.ExecX.toInt()] + "J,99); // return \n")
                    cmOutZb("\nDo_Block_Bypass" + g.ExecStack[g.ExecX.toInt()] + ": ; //B848 \n\n")
                    g.ExecLevel--
                }
                g.ExecLevel = 0
            }

            if (g.ExecLevel.toInt() == 0) g.forxFLG = 0

            g.xpxG = 1

            // copy source line to xd
            g.pd1Last = g.pd1cur

            var xj = 1
            while (true) {
                g.xdG[xj] = g.pd1[g.pd1cur.toInt()]
                g.pd1cur++
                val done = g.xdG[xj] == NUL_CM
                xj++
                if (done) break
            }

            sysoutCm("\n//================================================================================= \n")

            if ((g.MAIN_flg.toInt() != 0 || g.FCN_flg.toInt() != 0) && g.TryFlg == 0) {
                cmOutZb("svPtr->LineNumber=" + g.lines + "; // " + cStringOf(g.cur_line) + " \n")
            } else {
                cmOutZb("// " + cStringOf(g.cur_line) + " \n")
            }

            sysoutCm("//================================================================================= \n")

            // write the label
            if (g.EmitLabel.toInt() != 0) {
                if (g.ExecLevel.toInt() > 0) {
                    System.out.print("*** 125 Label not allowed in block")
                    return abrt1Cm(125) // label_in_block
                }
                cmOutZb("\n" + (g.LabelsStart?.name ?: "") + ": \n")
                g.EmitLabel = 0
            }

            g.xdG[xj] = NUL_CM

            pc = L_NEXT_CMND
            continue@machine
        }

        L_NEXT_CMND -> {
            if (g.IF_flg > g.IF_flg_max) g.IF_flg_max = g.IF_flg

            xpx = g.xpxG

            while (true) {
                if (xpx >= 1024 || g.xdG[xpx.toInt()] == NUL_CM) {
                    // the order of these tests is important
                    if (g.IF_flg > 0) {
                        var ei = g.IF_flg
                        while (ei > 0) {
                            cmOutZb("\t} ")
                            if (g.ForBlocks[ei] >= 0) {
                                cmOutZb(
                                    "while (0); longjmp(for" + g.ForBlocks[ei] + "J, _i); " +
                                        "// FOR loop " + ei + " close \n",
                                )
                                if (g.Blocks[ei].toInt() == CMD_FOR.code) {
                                    cmOutZb("\nFor" + g.ForBlocks[ei] + "Exit: ; // exit for loop \n\n")
                                }
                                g.ForBlocks[ei] = -1
                            } else {
                                cmOutZb("\n")
                            }
                            ei--
                        }
                        g.IF_flg = 0
                    }

                    if (g.TryFlg != 0) {
                        cmOutZb("\t} \n")
                        g.TryFlg = 1 // the vendor self-assignment quirk kept
                    }

                    if (g.CatchFlg != 0) {
                        cmOutZb("\t} \n")
                        g.CatchFlg = 0
                    }

                    pc = L_NEXT_LINE
                    continue@machine
                }

                if (g.xdG[xpx.toInt()] != ' ' && g.xdG[xpx.toInt()] != '\t') break

                xpx++
            }

            g.xpxG = xpx

            p2px = xpx.toInt()
            g.p2px = p2px.toShort()
            g.t2G = 0
            g.t0pxG = 0
            g.JOB_flg = 0
            g.POST_flg = 0

            if (xdStr(xpx.toInt()).startsWith("//")) {
                xpx = 9999
                g.xpxG = xpx
                pc = L_NEXT_CMND
                continue@machine // c++ style comment found
            }

            icmnd = toLowerC(g.xdG[xpx.toInt()])
            g.icmnd = icmnd.code.toShort()

            // check command word
            run {
                val word = CharArray(25)
                var wx = 1
                var wflg = 1
                g.MergeFlg = 0
                word[0] = icmnd

                var wscan = xpx.toInt()
                while (true) {
                    wscan++
                    if (wscan >= g.xdG.size || !isAlphaC(g.xdG[wscan])) break
                    if (wx < word.size - 1) {
                        word[wx] = toLowerC(g.xdG[wscan])
                        wx++
                    }
                }
                xpx = wscan.toLong()
                g.xpxG = xpx
                word[wx] = NUL_CM

                val wstr = cStringOf(word)
                for (cidx in CM_CTABLE.indices) {
                    if (CM_CTABLE[cidx].isEmpty()) break
                    if (wstr == CM_CTABLE[cidx]) {
                        wflg = 0
                        break
                    }
                }

                if (icmnd == CMD_CMMNT) {
                    pc = L_PSTRTN
                    return@run
                }

                if (wflg != 0) {
                    System.out.print("*** Warning, possible command spelling error: " + wstr + " ")
                    System.out.print("in or near line " + g.lines + "\n")
                    System.out.print("*** " + pd1Str(g.pd1Last.toInt()) + "\n")
                }
            }

            if (pc != L_PSTRTN) {
                if (g.xdG[xpx.toInt()] == ':') { // post conditionals
                    xpx++
                    g.xpxG = xpx

                    g.at_flg = atTest(g.xdG, g.pd1, g.lines, xpx.toInt()).toShort()

                    if (g.at_flg.toInt() == 99) return abrtx() // abrt_exit

                    if (g.at_flg.toInt() != 0) {
                        val tmp = CharArray(1024)
                        writeC(tmp, "i ")
                        atCopy(g.at_flg.toInt(), tmp)
                        interpretCm(cStringOf(tmp))
                        xpx = g.at_flg.toLong()
                        g.xpxG = xpx
                        cmOutZb("\tif (svPtr->tpx) { // m853 postconditional \n")
                        g.POST_flg = 1
                    } else {
                        g.ierr = parse_().toShort()
                        if (g.ierr.toInt() != 0) {
                            // parse_err
                            return parseErrCm()
                        }
                        cmOutZb("\tif (atoi( CSTR " + Extract2(pd1Str(g.sdlim.toInt())) + ")) { // m859 postconditional\n")
                        g.POST_flg = 1
                    }
                }
            }

            pc = L_PSTRTN
            continue@machine
        }

        L_PSTRTN -> {
            xpx = g.xpxG
            if (cntnueEntry) {
                cntnueEntry = false // goto cntnue skips the increment
            } else if (g.xdG[xpx.toInt()] != NUL_CM) {
                xpx++
                g.xpxG = xpx
            }

            // the vendor if (0) main-emission block is dead
            // (h013, dropped)

            when (icmnd) {

            CMD_CLSQ -> {
                sysoutCm("\t} /* user close */\n")
                pc = L_NEXT_CMND
                continue@machine
            }

            CMD_OPSQ -> {
                pc = L_NEXT_CMND
                continue@machine
            }

            CMD_TRY -> {
                cmOutZb("try { ")
                g.TryFlg = 1
                if (g.xdG[xpx.toInt()] == ' ' || g.xdG[xpx.toInt()] == NUL_CM) {
                    pc = L_NEXT_CMND
                    continue@machine
                }
                var te = xpx.toInt()
                while (g.xdG[te] != ' ' && g.xdG[te] != NUL_CM) te++
                val tj = g.xdG[te]
                g.xdG[te] = NUL_CM
                if (xdStr(xpx.toInt()) == "NoMessages") {
                    cmOutZb("svPtr->NOERR=1; ")
                } else {
                    System.out.print("*** 124 Unknown option in try")
                    return abrt1Cm(124) // try_unknown
                }
                g.xdG[te] = tj
                xpx = te.toLong()
                g.xpxG = xpx
                pc = L_NEXT_CMND
                continue@machine
            }

            CMD_CMMNT -> {
                p2px++
                if (g.xdG[p2px] == ';') {
                    val nm = xdStr(p2px + 1)
                    if (g.TextStart == null) {
                        g.TextStart = Txt(";" + nm, g.lines, null)
                    } else {
                        g.p1 = Txt(";" + nm, g.lines, g.TextStart)
                        g.TextStart = g.p1
                    }
                    pc = L_NEXT_LINE
                    continue@machine
                }
                pc = L_NEXT_LINE
                continue@machine
            }

            CMD_FCN -> {
                // unreachable through the tolower of icmnd
                // (the vendor case 'D' - kept, ledgered, h013)
                System.out.print("*** Not supported\n")
                System.out.print("*** " + cStringOf(g.cur_line) + "\n")
                g.ExitCode = 99
                return abrtx()
            }

            CMD_NEW -> {
                bannerCm("New")
                returnCode = new_code()
                if (returnCode == 0) {
                    pc = L_NEXT_CMND
                    continue@machine
                }
                if (returnCode == PARSE_ERROR) return parseErrCm()
                if (returnCode == ARG_LIST_ERROR) return argListErrCm()
                if (returnCode == NO_GLOBAL_PERMITTED) return noGblPermittedCm()
                if (returnCode == MEM_ERROR) {
                    System.out.print("\n*** 19 Out of memory")
                    return abrt1Cm(19) // mem_err
                }
                if (returnCode == TOO_MANY_LHS) return tooLhsCm()
                if (returnCode == ABRTX) return abrtx()
                return commandErrCm() // command error
            }

            CMD_BREAK -> {
                bannerCm("Break")
                if (g.forLine != 0) {
                    System.out.print("\n*** Break not permitted here")
                    return abrt1Cm(99) // nobrk_err
                }

                cmOutZb(
                    "     svPtr->tpx=tpx" + g.ExecStack[g.ExecX.toInt() - 1] +
                        "; // break code: restore \u0024test\n  longjmp(Efor" +
                        g.ExecStack[g.ExecX.toInt() - 1] + "J,100); \n",
                )

                if (g.POST_flg.toInt() != 0) {
                    cmOutZb(" } /* post conditional after break */\n")
                    g.POST_flg = 0
                }

                pc = L_NEXT_CMND
                continue@machine
            }

            CMD_LOCK -> {
                bannerCm("Lock")
                returnCode = lock_code()
                if (returnCode == 0) {
                    pc = L_NEXT_CMND
                    continue@machine
                }
                if (returnCode == PARSE_ERROR) return parseErrCm()
                if (returnCode == ARG_LIST_ERROR) return argListErrCm()
                if (returnCode == NO_GLOBAL_PERMITTED) return noGblPermittedCm()
                if (returnCode == MEM_ERROR) {
                    System.out.print("\n*** 19 Out of memory")
                    return abrt1Cm(19)
                }
                if (returnCode == TOO_MANY_LHS) return tooLhsCm()
                if (returnCode == ABRTX) return abrtx()
                if (returnCode == BAD_LOCK_CM) {
                    System.out.print("\n*** Lock error")
                    return abrt1Cm(99) // bad_lock
                }
                return commandErrCm()
            }

            CMD_READ -> {
                bannerCm("Read")
                returnCode = read_code()
                if (returnCode == 0) {
                    pc = L_NEXT_CMND
                    continue@machine
                }
                if (returnCode == PARSE_ERROR) return parseErrCm()
                if (returnCode == ARG_LIST_ERROR) return argListErrCm()
                if (returnCode == NO_GLOBAL_PERMITTED) return noGblPermittedCm()
                if (returnCode == MEM_ERROR) {
                    System.out.print("\n*** 19 Out of memory")
                    return abrt1Cm(19)
                }
                if (returnCode == TOO_MANY_LHS) return tooLhsCm()
                if (returnCode == ABRTX) return abrtx()
                return commandErrCm()
            }

            CMD_OPEN -> {
                bannerCm("Open")
                returnCode = mumps_open()
                if (returnCode == 0) {
                    pc = L_NEXT_CMND
                    continue@machine
                }
                if (returnCode == PARSE_ERROR) return parseErrCm()
                if (returnCode == ARG_LIST_ERROR) return argListErrCm()
                return commandErrCm() // default
            }

            CMD_ZCMDS -> {
                returnCode = zcommandsCode()
                if (returnCode == 0) {
                    pc = L_NEXT_CMND
                    continue@machine
                }
                if (returnCode == PARSE_ERROR) return parseErrCm()
                if (returnCode == ARG_LIST_ERROR) return argListErrCm()
                if (returnCode == NO_GLOBAL_PERMITTED) return noGblPermittedCm()
                if (returnCode == MEM_ERROR) {
                    System.out.print("\n*** 19 Out of memory")
                    return abrt1Cm(19)
                }
                if (returnCode == TOO_MANY_LHS) return tooLhsCm()
                if (returnCode == ABRTX) return abrtx()
                if (returnCode == NEXT_LINE_CM) {
                    pc = L_NEXT_LINE
                    continue@machine
                }
                return commandErrCm()
            }

            CMD_IF -> {
                bannerCm("If")
                returnCode = if_code()
                if (returnCode == 0) {
                    pc = L_NEXT_CMND
                    continue@machine
                }
                if (returnCode == PARSE_ERROR) return parseErrCm()
                if (returnCode == ARG_LIST_ERROR) return argListErrCm()
                if (returnCode == NO_GLOBAL_PERMITTED) return noGblPermittedCm()
                if (returnCode == MEM_ERROR) {
                    System.out.print("\n*** 19 Out of memory")
                    return abrt1Cm(19)
                }
                if (returnCode == TOO_MANY_LHS) return tooLhsCm()
                if (returnCode == ABRTX) return abrtx()
                return commandErrCm()
            }

            CMD_WRITE -> {
                bannerCm("Write")
                returnCode = write_code()
                if (returnCode == 0) {
                    pc = L_NEXT_CMND
                    continue@machine
                }
                if (returnCode == PARSE_ERROR) return parseErrCm()
                if (returnCode == ARG_LIST_ERROR) return argListErrCm()
                if (returnCode == NO_GLOBAL_PERMITTED) return noGblPermittedCm()
                if (returnCode == MEM_ERROR) {
                    System.out.print("\n*** 19 Out of memory")
                    return abrt1Cm(19)
                }
                if (returnCode == TOO_MANY_LHS) return tooLhsCm()
                if (returnCode == ABRTX) return abrtx()
                return commandErrCm()
            }

            CMD_JOB -> {
                bannerCm("Job")
                linepad(cmOutZb("\tif (fork()!=0) {"), "I am child process if zero")
                g.JOB_flg = 1
                pc = L_DO_ENTRY
                continue@machine
            }

            CMD_MERGE, CMD_SET -> {
                if (icmnd == CMD_MERGE) {
                    bannerCm("Merge")
                    g.MergeFlg = 1
                }

                if (toUpperC(g.xdG[p2px + 1]) == 'H') { // SHELL command
                    bannerCm("Shell")
                    val tmp4096 = CharArray(4096)
                    writeC(tmp4096, xdStr(p2px))
                    interpretCm(cStringOf(tmp4096))
                    pc = L_NEXT_LINE
                    continue@machine
                }

                if (toUpperC(g.xdG[p2px + 1]) == 'Q') { // SQL
                    val tmp4096 = CharArray(4096)
                    writeC(tmp4096, xdStr(p2px))
                    interpretCm(cStringOf(tmp4096))
                    pc = L_NEXT_LINE
                    continue@machine
                }

                bannerCm("Set")

                g.retz = CMD_SET.code.toShort()

                returnCode = set_code(CMD_SET.code)
                if (returnCode == 0) {
                    pc = L_NEXT_CMND
                    continue@machine
                }
                if (returnCode == PARSE_ERROR) return parseErrCm()
                if (returnCode == ARG_LIST_ERROR) return argListErrCm()
                if (returnCode == NO_GLOBAL_PERMITTED) return noGblPermittedCm()
                if (returnCode == MEM_ERROR) {
                    System.out.print("\n*** 19 Out of memory")
                    return abrt1Cm(19)
                }
                if (returnCode == TOO_MANY_LHS) return tooLhsCm()
                if (returnCode == ABRTX) return abrtx()
                return commandErrCm()
            }

            CMD_QUIT -> {
                // cntnue label target
                bannerCm("Quit")

                if (g.POST_flg.toInt() != 0) {
                    if (g.ForBlocks[g.IF_flg] <= 0) {
                        cmOutZb("\tbreak; } // post conditional quit m2386 \n")
                    } else {
                        cmOutZb("\tlongjmp(for" + g.ForBlocks[g.IF_flg] + "J,100); /* m1146 quit*/ } // m1146 quit\n\n")
                    }
                    g.POST_flg = 0
                    pc = L_NEXT_CMND
                    continue@machine
                }

                if (g.ExecLevel.toInt() == 0 && g.forxFLG == 0) {
                    sysoutCm("\tif (svPtr->Rargsx) { // call-by-reference? \n")
                    sysoutCm("\t\tint i; \n")
                    sysoutCm("\t\tunsigned char *tmp = new unsigned char[STR_MAX]; \n")
                    sysoutCm("\t\tfor (i = 0; i < svPtr->Rargsx; i++) { \n")
                    sysoutCm("\t\tsym_(1, (UNS) svPtr->RefParm[i], (UNS) tmp,svPtr); \n")
                    sysoutCm("\t\tsym_(SYMSTORE, (UNS) svPtr->Rargs[i], (UNS) tmp, svPtr); \n")
                    sysoutCm("\t\t} \n")
                    sysoutCm("\t\t} \n")

                    cmOutZb("\tstrmove((UNS) _ReturnVal, (UNS) \"\"); \n")

                    xpx = g.xpxG
                    if (g.xdG[xpx.toInt()] != ' ' && g.xdG[xpx.toInt()] != NUL_CM) {
                        g.t2G = 1
                        g.ierr = parse_().toShort()
                        if (g.ierr.toInt() != 0) return parseErrCm()
                        cmOutZb("\tstrmove((UNS) _ReturnVal, (UNS) " + Extract2(pd1Str(g.sdlim.toInt())) + "); \n")
                    }

                    if (g.F_flg != 0) {
                        cmOutZb("\treturn (char *) _ReturnVal; \n")
                    } else {
                        cmOutZb("\tif (_doStackN[_doStx]) {  _doStackN[_doStx] = 0; } // Q1001 \n")
                        cmOutZb("\tif (_doStx > 0) { longjmp(_doStack[_doStx],99); } \n")
                        cmOutZb("\telse ErrorMessage(\"Too Many Quits\", svPtr->LineNumber); \n")
                    }
                } else {
                    if (g.IF_flg == 0 || g.ForBlocks[g.IF_flg] < 0) {
                        cmOutZb("\tbreak; \n")
                    } else {
                        cmOutZb("\tgoto For" + g.ForBlocks[g.IF_flg] + "Exit; \n")
                    }

                    if (g.Blocks[g.IF_flg].toInt() == CMD_IF.code) {
                        g.Blocks[g.IF_flg] = 0
                        g.IF_flg--
                        cmOutZb("\t} // if block 2389\n")
                    }
                }

                pc = L_NEXT_CMND
                continue@machine
            }

            CMD_XECUTE -> {
                bannerCm("Xecute")
                run {
                    val tmp = CharArray(1024)
                    writeC(tmp, "x ")
                    var tj = 2
                    var ti = xpx.toInt()
                    while (g.xdG[ti] != NUL_CM) {
                        if (g.xdG[ti] != '"') {
                            tmp[tj++] = g.xdG[ti]
                        } else {
                            tmp[tj++] = '\\'
                            tmp[tj++] = g.xdG[ti]
                        }
                        ti++
                    }
                    tmp[tj] = NUL_CM
                    interpretCm(cStringOf(tmp))

                    if (g.POST_flg.toInt() != 0) {
                        cmOutZb("\t} // post conditional xecute\n")
                        g.POST_flg = 0
                    }
                }
                g.xpxG = 1024
                pc = L_NEXT_CMND
                continue@machine
            }

            CMD_FOR -> {
                bannerCm("For")
                returnCode = for_code()
                if (returnCode == 0) {
                    pc = L_NEXT_CMND
                    continue@machine
                }
                if (returnCode == PARSE_ERROR) return parseErrCm()
                if (returnCode == ARG_LIST_ERROR) return argListErrCm()
                if (returnCode == NO_GLOBAL_PERMITTED) return noGblPermittedCm()
                if (returnCode == MEM_ERROR) {
                    System.out.print("\n*** 19 Out of memory")
                    return abrt1Cm(19)
                }
                if (returnCode == TOO_MANY_LHS) return tooLhsCm()
                if (returnCode == ABRTX) return abrtx()
                return commandErrCm()
            }

            CMD_HALT -> {
                if ((toUpperC(g.xdG[p2px + 1]) != 'T' && g.xdG[xpx.toInt()] == ' ') ||
                    g.xdG[xpx.toInt()] == NUL_CM
                ) { // halt
                    bannerCm("Halt")

                    if (g.MAIN_flg.toInt() != 0) cmOutZb("\tgoto _epilogue; \n")

                    if (g.FCN_flg.toInt() != 0) cmOutZb("\treturn \"\"; \n")

                    if (g.POST_flg.toInt() != 0) {
                        cmOutZb("\t} /* post conditional halt*/\n")
                        g.POST_flg = 0
                    }

                    pc = L_NEXT_CMND
                    continue@machine
                }

                if (toUpperC(g.xdG[p2px + 1]) == 'T') { // HTML command
                    cmOutZb(" /* HTML */ printf(\"")

                    while (g.xdG[xpx.toInt()] != NUL_CM) {
                        if (g.xdG[xpx.toInt()] == '&' && g.xdG[xpx.toInt() + 1] == '~') {
                            xpx += 2
                            g.xpxG = xpx
                            cmOutZb("\");\n")
                            g.t2G = 1

                            var hj = xpx.toInt()
                            while (g.xdG[hj] != '~' && g.xdG[hj] != NUL_CM) hj++

                            g.xdG[hj] = ' '

                            g.ierr = parse_().toShort()
                            if (g.ierr.toInt() != 0) return parseErrCm()
                            cmOutZb("\tprintf(\"%s\", " + Extract2(pd1Str(g.sdlim.toInt())) + "); \n")
                            cmOutZb("\tprintf(\"")
                            xpx++
                            g.xpxG = xpx
                            continue
                        }

                        if (g.xdG[xpx.toInt()] == '&' && g.xdG[xpx.toInt() + 1] == '!') {
                            xpx += 2
                            g.xpxG = xpx
                            cmOutZb("\\n")
                            continue
                        }

                        when (g.xdG[xpx.toInt()]) {
                            '"' -> cmOutZb("\\\"")
                            '\'' -> cmOutZb("\\'")
                            '%' -> cmOutZb("%%")
                            '\\' -> cmOutZb("\\\\")
                            else -> cmOutZb(g.xdG[xpx.toInt()].toString())
                        }
                        xpx++
                        g.xpxG = xpx
                    }

                    cmOutZb("\");\n")
                    pc = L_NEXT_LINE
                    continue@machine
                }

                while (true) { // Hang
                    bannerCm("Hang")
                    g.t2G = 1
                    g.ierr = parse_().toShort()
                    if (g.ierr.toInt() != 0) return parseErrCm()
                    cmOutZb("\t_i = atoi( CSTR " + Extract2(pd1Str(g.sdlim.toInt())) + ");\n")
                    cmOutZb("\tif (_i > 0) sleep(_i);\n")

                    xpx = g.xpxG
                    if (g.xdG[xpx.toInt()] == ',') {
                        xpx++
                        g.xpxG = xpx
                        continue
                    }

                    if (g.POST_flg.toInt() != 0) {
                        cmOutZb("\t} /* post conditional hang*/\n")
                        g.POST_flg = 0
                    }

                    pc = L_NEXT_CMND
                    continue@machine
                }
            }

            CMD_ELSE -> {
                if (toLowerC(g.xdG[p2px + 1]) == 'x') {
                    g.exportflag = 1
                }

                bannerCm("Else")

                xpx = g.xpxG
                if (g.xdG[xpx.toInt()] == ' ') {
                    xpx++
                    g.xpxG = xpx
                }

                cmOutZb("\tif (svPtr->tpx == 0) { \n")

                g.IF_flg++

                pc = L_NEXT_CMND
                continue@machine
            }

            CMD_DO -> { // declare
                if (toUpperC(g.xdG[p2px + 1]) == 'A') { // DA command
                    val tmp4096 = CharArray(4096)
                    var di2 = 0
                    var dp = p2px
                    while (g.xdG[dp] != NUL_CM) {
                        if (g.xdG[dp] == '"') {
                            tmp4096[di2++] = '\\'
                        }
                        tmp4096[di2] = g.xdG[dp]
                        di2++
                        dp++
                    }
                    tmp4096[di2] = NUL_CM
                    interpretCm(cStringOf(tmp4096))
                    pc = L_NEXT_LINE
                    continue@machine
                }

                if (toLowerC(g.xdG[p2px + 1]) == 'e') { // declare
                    while (true) { // dcl
                        if (g.cvarx > CVARMAX) {
                            System.out.print("\n*** 91 Too many C variables")
                            return abrt1Cm(91) // too_many_vars
                        }
                        xpx = g.xpxG
                        val nm = StringBuilder()
                        while (g.xdG[xpx.toInt()] != ' ' && g.xdG[xpx.toInt()] != NUL_CM &&
                            g.xdG[xpx.toInt()] != ','
                        ) {
                            nm.append(g.xdG[xpx.toInt()])
                            xpx++
                        }
                        g.xpxG = xpx
                        g.cvars[g.cvarx] = Dcs(nm.toString(), 0)
                        g.cvarx++
                        if (g.xdG[xpx.toInt()] == ',') {
                            xpx++
                            g.xpxG = xpx
                            continue // goto dcl
                        }
                        break
                    }
                    pc = L_NEXT_CMND
                    continue@machine
                }

                pc = L_DO_ENTRY
                continue@machine
            }

            CMD_KILL -> {
                bannerCm("Kill")
                returnCode = kill_code()
                if (returnCode == 0) {
                    pc = L_NEXT_CMND
                    continue@machine
                }
                if (returnCode == PARSE_ERROR) return parseErrCm()
                if (returnCode == ARG_LIST_ERROR) return argListErrCm()
                if (returnCode == NO_GLOBAL_PERMITTED) return noGblPermittedCm()
                if (returnCode == MEM_ERROR) {
                    System.out.print("\n*** 19 Out of memory")
                    return abrt1Cm(19)
                }
                if (returnCode == TOO_MANY_LHS) return tooLhsCm()
                if (returnCode == KILL_DCL_ERROR_CM) {
                    System.out.print("\n*** Attempt to Kill declared variable\n")
                    return abrt1Cm(99) // kill_dcl_err
                }
                if (returnCode == ABRTX) return abrtx()
                return commandErrCm()
            }

            CMD_CLOSE -> {
                bannerCm("Close")
                p2px++
                if (g.xdG[p2px].code <= 90) {
                    g.xdG[p2px] = (g.xdG[p2px].code + 32).toChar() // lower case
                }
                if (g.xdG[p2px] == 'o') {
                    pc = L_CNTNUE // continue
                    continue@machine
                }
                if (g.xdG[p2px] == 'a') {
                    pc = L_CTCH // catch
                    continue@machine
                }

                while (true) {
                    g.t2G = 1
                    g.ierr = parse_().toShort()
                    if (g.ierr.toInt() != 0) return parseErrCm()
                    cmOutZb("\t_i=atoi( CSTR " + Extract2(pd1Str(g.sdlim.toInt())) + ");\n")
                    cmOutZb("\tif (svPtr->in_file[_i] == NULL && svPtr->out_file[_i] == NULL) \n")
                    cmOutZb("\t\tErrorMessage(\"Attempt to close unopened file\", svPtr->LineNumber); \n")
                    cmOutZb("\tif (svPtr->in_file[_i]!=NULL) { \n\tfclose(svPtr->in_file[_i]); svPtr->in_file[_i]=NULL; }\n")
                    cmOutZb("\telse { fclose(svPtr->out_file[_i]); svPtr->out_file[_i]=NULL; }\n")

                    xpx = g.xpxG
                    if (g.xdG[xpx.toInt()] != ',') {
                        if (g.POST_flg.toInt() != 0) {
                            cmOutZb("\t} /* post conditional 3240*/\n")
                            g.POST_flg = 0
                        }
                        pc = L_NEXT_CMND
                        continue@machine
                    }
                    xpx++
                    g.xpxG = xpx
                }
            }

            CMD_USE -> {
                bannerCm("Use")
                g.t2G = 1
                g.ierr = parse_().toShort()
                if (g.ierr.toInt() != 0) return parseErrCm()
                cmOutZb("\tsvPtr->io=atoi( CSTR " + Extract2(pd1Str(g.sdlim.toInt())) + ");\n")

                if (g.POST_flg.toInt() != 0) {
                    cmOutZb("\t} /* post conditional 3245*/\n")
                    g.POST_flg = 0
                }

                pc = L_NEXT_CMND
                continue@machine
            }

            CMD_GOTO -> {
                bannerCm("Goto")

                xpx = g.xpxG
                if (g.xdG[xpx.toInt()] == NUL_CM) {
                    System.out.print("\n*** Label or file not found")
                    return abrt1Cm(8) // lbl_error
                }

                if (g.ExecLevel.toInt() > 0) {
                    System.out.print("*** 126 Goto not allowed in block")
                    return abrt1Cm(126) // goto_in_block
                }

                pc = L_NEXT_GOTO
                continue@machine
            }

            else -> {
                System.out.print("\n*** 13 Command Error")
                return abrt1Cm(13) // command
            }
            } // when (icmnd)
        }

        L_CTCH -> {
            cmOutZb("catch(")
            g.CatchFlg = 1
            g.TryFlg = 0
            xpx = g.xpxG
            if (g.xdG[xpx.toInt()] == ' ' || g.xdG[xpx.toInt()] == NUL_CM) {
                System.out.print("*** 122 Missing catch argument")
                return abrt1Cm(122) // catch_error
            }
            var ce = xpx.toInt()
            while (g.xdG[ce] != ' ' && g.xdG[ce] != NUL_CM) ce++
            val cj = g.xdG[ce]
            g.xdG[ce] = NUL_CM
            var cflg = 1
            for (ck in g.except.indices) {
                val ex = g.except[ck] ?: break
                if (ex.isEmpty()) break
                if (xdStr(xpx.toInt()) == ex) {
                    cflg = 0
                    break
                }
            }
            if (cflg != 0) {
                System.out.print("*** 123 Unknown exception in catch")
                return abrt1Cm(123) // catch_unknown
            }
            cmOutZb(xdStr(xpx.toInt()) + " ) {\n")
            g.xdG[ce] = cj
            xpx = ce.toLong()
            g.xpxG = xpx
            pc = L_NEXT_CMND
            continue@machine
        }

        L_CNTNUE -> {
            pc = L_PSTRTN // the QUIT case label
            icmnd = CMD_QUIT
            cntnueEntry = true
            continue@machine
        }

        L_JOB_RETURN -> {
            cmOutZb("\t}")
            pc = L_NEXT_LINE
            continue@machine
        }

        L_DO_ENTRY -> {
            bannerCm("Do")

            xpx = g.xpxG

            // Argumentless DO
            if (g.xdG[xpx.toInt()] == NUL_CM || g.xdG[xpx.toInt()] == ' ') {
                if (g.JOB_flg.toInt() != 0) { // is this really a JOB command?
                    System.out.print("*** JOB requires argument\n")
                    return argListErrCm()
                }

                cmOutZb("\t\ttpx" + g.ExecName + " = svPtr->tpx; \n")
                cmOutZb("\t\tif ( (_i = setjmp(Efor" + g.ExecName + "J)) == 0) ")
                cmOutZb("goto Do_Block_Entry" + g.ExecName + "; //else continue \n")

                if (g.IF_flg > 0) {
                    cmOutZb("\t\telse if (_i == 100) goto Do_Block_Bypass" + g.ExecName + "; // IF_flg m1402\n")
                }

                g.forx++
                g.DoRet[g.DoRetX.toInt()] = g.ExecName
                g.DoRetX++

                if (g.POST_flg.toInt() != 0) {
                    cmOutZb("\t} // post conditional do\n")
                    g.POST_flg = 0
                }

                pc = L_NEXT_CMND
                continue@machine
            }

            pc = L_ANOTHER_DO
            continue@machine
        }

        L_ANOTHER_DO -> {
            cmOutZb("\t_ArgCount=0;  //section D1000 \n")
            sysoutCm("\tsvPtr->Rargsx=0;\n")

            xpx = g.xpxG

            val startXpx = xpx
            run {
                val ttt = CharArray(2048)
                System.arraycopy(g.xdG, 0, ttt, 0, 2048)

                var dj = xpx.toInt()
                var k = 0
                while (true) { // scan through label for non-quoted colon
                    if (g.xdG[dj] == NUL_CM) break
                    if (g.xdG[dj] == ',' && k == (k / 2) * 2) break
                    if (g.xdG[dj] == ':' && k == (k / 2) * 2) break
                    if (g.xdG[dj] == '"') {
                        if (g.xdG[dj + 1] == '"') {
                            dj++
                        } else {
                            k++
                        }
                    }
                    dj++
                }

                if (g.xdG[dj] == ':' && k == (k / 2) * 2) {
                    xpx = (dj + 1).toLong()
                    g.xpxG = xpx
                    g.t0pxG = 0
                    g.t2G = 1
                    g.ierr = parse_().toShort()
                    if (g.ierr.toInt() != 0) return parseErrCm()
                    cmOutZb("\tif (atoi( CSTR " + Extract2(pd1Str(g.sdlim.toInt())) + ")) { // postconditional \n")
                    g.t0pxG = 0
                    g.t2G = 0
                    xpx = startXpx
                    g.xpxG = xpx
                    System.arraycopy(ttt, 0, g.xdG, 0, 2048)
                } else {
                    cmOutZb("\t{\n")
                }
            }

            xpx = g.xpxG

            if (g.xdG[xpx.toInt()] == '@') { // indirection in DO argument
                g.at_flg = atTest(g.xdG, g.pd1, g.lines, xpx.toInt()).toShort()

                if (g.at_flg.toInt() == -1) return abrtx() // abrt_exit

                if (g.at_flg.toInt() != 0) {
                    val tmp = CharArray(1024)
                    writeC(tmp, "d ")
                    atCopy(g.at_flg.toInt(), tmp)
                    interpretCm(cStringOf(tmp))

                    xpx = g.at_flg.toLong()
                    g.xpxG = xpx
                    cmOutZb("       } /* post conditional 1920*/\n")

                    if (g.POST_flg.toInt() != 0) {
                        cmOutZb("       } /* post conditional 1920*/\n")
                        g.POST_flg = 0
                    }
                    pc = L_NEXT_CMND
                    continue@machine
                }
            } else if (g.xdG[xpx.toInt()] == '\u0024' && isAlnumC(g.xdG[xpx.toInt() + 1])) {
                g.t0pxG = 0
                g.t2G = 1
                g.ierr = parse_().toShort()
                if (g.ierr.toInt() != 0) return parseErrCm()
                cmOutZb("\t} \n")
            } else {
                g.xdG[xpx.toInt() - 2] = '\u0024'
                g.xdG[xpx.toInt() - 1] = '\u0024'
                xpx -= 2
                g.xpxG = xpx
                g.t2G = 1
                g.ierr = parse_().toShort()
                if (g.ierr.toInt() != 0) return parseErrCm()
                g.t2G = 0
                cmOutZb("\t} \n ")
            }

            xpx = g.xpxG

            if (g.xdG[xpx.toInt()] == ':') { // bypass post-cond
                var dk = 0
                var dm = 0
                xpx++
                var dj = xpx.toInt()
                while (true) {
                    if (g.xdG[dj] == NUL_CM) break
                    if (g.xdG[dj] == ',' && dm == 0 && dk == (dk / 2) * 2) break
                    if (g.xdG[dj] == ' ' && dm == 0 && dk == (dk / 2) * 2) break
                    if (g.xdG[dj] == '(' && dk == (dk / 2) * 2) dm++
                    if (g.xdG[dj] == ')' && dk == (dk / 2) * 2) dm--
                    if (g.xdG[dj] == '"') {
                        if (g.xdG[dj + 1] == '"') dj++
                        else dk++
                    }
                    dj++
                }
                if (g.xdG[dj] == ',' && dm == 0 && dk == (dk / 2) * 2) {
                    xpx = (dj + 1).toLong()
                    g.xpxG = xpx
                    pc = L_ANOTHER_DO
                    continue@machine
                }
                xpx = dj.toLong()
                g.xpxG = xpx
            }

            xpx = g.xpxG
            if (g.xdG[xpx.toInt()] == ',') {
                xpx++
                g.xpxG = xpx
                pc = L_ANOTHER_DO
                continue@machine
            }

            if (g.POST_flg.toInt() != 0) { // end of post-cond
                cmOutZb("\t} /* post conditional 3001*/\n")
                g.POST_flg = 0
            }

            if (g.JOB_flg.toInt() != 0) {
                pc = L_JOB_RETURN
                continue@machine
            }

            pc = L_NEXT_CMND
            continue@machine
            // the vendor's unreachable while(1) parse_ tail after
            // this goto is dead (h013, dropped)
        }

        L_NEXT_GOTO -> {
            xpx = g.xpxG
            var gi = 0

            while (g.xdG[xpx.toInt()] != NUL_CM) {
                if (g.xdG[xpx.toInt()] == ',' || g.xdG[xpx.toInt()] == ' ' ||
                    g.xdG[xpx.toInt()] == NUL_CM || g.xdG[xpx.toInt()] == '+' ||
                    g.xdG[xpx.toInt()] == ':'
                ) break
                g.dosave[gi++] = g.xdG[xpx.toInt()]
                xpx++
            }
            g.xpxG = xpx

            if (g.xdG[xpx.toInt()] == ':') { // post conditionals
                xpx++
                g.xpxG = xpx
                g.t0pxG = 0
                g.t2G = 1
                g.ierr = parse_().toShort()
                if (g.ierr.toInt() != 0) return parseErrCm()
                cmOutZb("\tif (atoi((char *) " + Extract2(pd1Str(g.sdlim.toInt())) + ")) /* m1635 postconditional*/")
                g.t0pxG = 0
                g.t2G = 0
            }

            g.dosave[gi] = NUL_CM

            var gscan = 0
            while (g.dosave[gscan] != NUL_CM && g.dosave[gscan] != '^' && g.dosave[gscan] != '+') gscan++

            if (g.dosave[gscan] == '+' || g.dosave[gscan] == '(') {
                System.out.print("\n*** Label or file not found")
                return abrt1Cm(8) // lbl_error (the '(' arm cannot
                // fire - dosave stops at '^'/'+' - kept, ledgered)
            }

            if (g.dosave[gscan] == NUL_CM) { // simple goto
                if (isDigitC(g.dosave[0])) {
                    cmOutZb("\tgoto _" + cStringOf(g.dosave) + ";\n")
                } else {
                    cmOutZb("\tgoto " + cStringOf(g.dosave) + ";\n")
                }
                pc = L_GEXIT
                continue@machine
            }

            if (g.dosave[0] != '^') { // label appears first in a ^ ref
                g.dosave[gscan] = NUL_CM
                writeC(g.tmp2G, cStringOf(g.dosave))
                System.arraycopy(g.dosave, gscan + 1, g.dosave, 1, g.dosave.size - gscan - 1)
                g.dosave[0] = '^'
            } else {
                writeC(g.tmp2G, "")
            }

            var gname = StringBuilder()
            var gnp = 1
            while (gnp < g.dosave.size && g.dosave[gnp] != NUL_CM) {
                gname.append(g.dosave[gnp])
                gnp++
            }
            cmOutZb("\t_jj=Interpret(\"x \\\"goto ^" + gname.toString() + "\\\"\",svPtr); \n")

            sysoutCm("\tgoto _epilogue; /* if fcn returns, this is end program */\n")

            pc = L_GEXIT
            continue@machine
        }

        L_GEXIT -> {
            xpx = g.xpxG
            if (g.xdG[xpx.toInt()] == ',') {
                xpx++
                g.xpxG = xpx
                pc = L_NEXT_GOTO
                continue@machine
            }

            if (g.POST_flg.toInt() != 0) {
                cmOutZb("\t} /* post conditional 3431*/\n")
                g.POST_flg = 0
            }

            pc = L_NEXT_CMND
            continue@machine
        }
        } // when (pc)
    } // machine loop
}

// the error-label family folds (each keeps the exact printf
// shape before the abrt1 exit)

private fun parseErrCm(): Int {
    val g = CompilerGlobalsExt
    g.ernbr = g.ierr
    errmod_(g.ierr.toInt())
    if (g.ernbr.toInt() == 9001) System.out.print("*** Invalid global array reference")
    if (g.ernbr.toInt() == 9002) System.out.print("*** Invalid routine reference")
    if (g.ernbr.toInt() == 9003) System.out.print("*** Wrong number of args to \u0024extract()")
    if (g.ernbr.toInt() == 12) System.out.print("Invalid operator: " + g.ErrChar)
    return abrt1Cm(g.ernbr.toInt())
}

private fun argListErrCm(): Int {
    System.out.print("\n*** 14 Argument list error")
    return abrt1Cm(14)
}

private fun noGblPermittedCm(): Int {
    System.out.print("\n*** 92 Global not permitted")
    return abrt1Cm(92)
}

private fun tooLhsCm(): Int {
    System.out.print("\n*** 9033 Too many variables in LHS")
    return abrt1Cm(9033)
}

private fun commandErrCm(): Int {
    System.out.print("\n*** 13 Command Error")
    return abrt1Cm(13)
}
