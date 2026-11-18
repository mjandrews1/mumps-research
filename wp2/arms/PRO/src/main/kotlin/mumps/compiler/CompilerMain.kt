package mumps.compiler

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.io.IOException
import java.io.PushbackReader

/**
 * PRO-arm translation of compiler-mumps.c::main (roster row 279,
 * batch 10, rank 304). Translated independently from the pristine
 * vendor source (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe),
 * reference source lines 42-1945 (1904 physical lines).
 *
 * Identity verified against the study tool (tools/select_study_
 * subset.py): compiler-mumps.c::main, c, loc 1372, comment_ratio
 * 0.041, pointer_marks 106, control_marks 593, score 1125.2,
 * rank 304, batch 10 - exact match to wp1/study-subset/units.csv
 * row 279. The LevCheck (vendor 1949) / atCopy (vendor 1956) / EP
 * (vendor 1978) tail is owned by B7 unit 196 (CompilerMumpsMisc.kt);
 * the atCopy corpus unit row 241 verified against the same seat.
 *
 * The compiler driver: opens the source and the generated C file,
 * emits the include/define prologue and the inline helper family
 * (floatstring/numcomp2/strxcat/strcatz/SyncClock/set_alarm), then
 * runs the line/command loop (labels, postconditionals, the
 * ctable command-word check, the command dispatch switch) and the
 * error ladder into abrt1/abrt_exit. Rendered as a phase-cursor
 * loop (mumpsMain + phase constants) exactly like the B9
 * compiler-parse.c::parse_ unit (Parse_.kt precedent).
 *
 * RESOLVES: the driver seat was never stubbed (no earlier unit
 * claimed main); this render binds the existing fleet: parse_
 * (Parse_.kt), doty (Doty.kt), atTest/atCopy (AtTest.kt/
 * CompilerMumpsMisc.kt), EP/LevCheck (CompilerMumpsMisc.kt),
 * fcninit (Fcninit.kt), errmod_ (Errmod.kt), epi (Epi.kt via
 * abrtx), newCode/killCode/lockCode/readCode/mumpsOpen/
 * zcommandsCode/ifCode/writeCode/setCode/forCode, Extract2,
 * copyrightNotice, linepad (Stubs.kt h014 stub owner
 * compiler-read.c - KEPT as stub, batch-10+ ledger), _horolog
 * and bstrcat (B10 rows 277/278, resolved seats).
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h001 edge_stdio: fopen/fgets/getc/ungetc ride
 *    File/FileWriter/BufferedReader/PushbackReader; fgets keeps
 *    the trailing newline which the vendor's two <32 clears then
 *    strip - readLine folds both (identical pd1 content at the
 *    first read); the 512-byte fgets cap is kept (511 chars +
 *    NUL at pd1[1]). The <32 clear pair is rendered verbatim over
 *    the strlen recomputation (vendor recomputes after each
 *    write; both renders kept).
 *  - h002 simple_output_macro: Sysout(x)/Sysout_skip expanded
 *    (fprintf(Out,x) - the live x strings carry no format
 *    directives; the %% folds of the dead zmain blocks are noted
 *    under h013). printf/banner strings verbatim.
 *  - h003 state_macro: all extern globals ride CompilerState
 *    (pd1/xd/pd1cur/xpx/t2/t0px/POST_flg/ForBlocks/Blocks/
 *    IF_flg/EmitLabel/TryFlg/CatchFlg/SubFlag/ernbr/ierr/
 *    MAIN_flg/FCN_flg/F_flg/FunctionName/pid/output_file/tmp2/
 *    cur_line/dosave/except/LabelsStart/TextStart/DoRet/DoRetX/
 *    ExecLevel/ExecName/ExecX/ExecStack/cvars/cvarx/ArgMax/
 *    SymIncrFlg/MergeFlg/JOB_flg/forxFLG/forx/lines/icmnd/p2px/
 *    at_flg/retz/ExitCode/symlen); xpx Long -> Int index
 *    narrowing (h010 note, B7 precedent).
 *  - h004 globals_array: no new file-scope globals; the two
 *    file-local labels (ctable/except) ride the state carriers
 *    (except:) and a file-private const array (CTABLE).
 *  - h012 unsigned_byte_domain: pd1/xd/dosave/cur_line reads are
 *    byte-domain Char reads; control (<32, TAB 9) tests exact.
 *  - h013 dead_store: the vendor "if (!MAIN_flg && 0)" and
 *    "if (0)" zmain blocks (compiler-mumps.c:344-367, 882-903)
 *    are UNREACHABLE (compile-time false) - NOT rendered, noted
 *    here (B9 Parse_ dead-block precedent); so is the
 *    unreachable while(1) tail after the DO-case "goto
 *    next_cmnd" ladder (vendor 1626-1638); the "ArgMax=kk+1"
 *    comment is kept as a comment; at_flg==99 on the ':' arm is
 *    never returned by atTest (-1 is) - comparison kept.
 *  - h014 out_of_scope_call: linepad remains the Stubs.kt TODO
 *    stub (owner compiler-read.c, not a corpus unit); epi/EP/
 *    LevCheck bind their B7 seats; Interpret emission rides
 *    CompilerState.interpret (INTERPRET macro carrier).
 *  - h015 grammar_collision: main -> mumpsMain (JVM entry
 *    point conflict + reserved-name family); the input stream
 *    FILE *in -> local input (state carrier in_ kept in sync);
 *    'in' keyword precedent u124.
 *  - h017 naming: getpid() -> ProcessHandle.current().pid();
 *    fopen/fgets -> File/BufferedReader (h001); command codes
 *    keep their vendor macros as *_C consts.
 *  - h018 reference_build_visibility: TAB is 9 (NOTABS excluded
 *    on the reference build, compiler-macros.h:127-130 note).
 *
 *  - The jumpless vendor error labels quit_err (vendor 1888),
 *    Ext_file (1898), go_err (1904), do_error (1873),
 *    indirect_err (1857) and ref_err (1909) have NO gotos in
 *    the vendor body - documented here, not rendered (h013
 *    dead-label convention; goto census run against vendor
 *    42-1945).
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The two <32 trailing-char clears recompute strlen after
 *    each store (the first clear can shorten the line the
 *    second test sees) - both kept verbatim.
 *  - The filename dot-scan copies argv[1] (not argv[argc-1])
 *    on the no-dot path - vendor original, kept (argc==2 makes
 *    them identical; argc==3 -SUB makes it the option string).
 *  - "if (!MAIN_flg && 0)" / "if (0)" dead zmain blocks and the
 *    unreachable DO tail are vendor-dead (kept as notes only,
 *    per the B9 dead-block convention).
 *  - The vendor reuse of SET for both entry point (0) and
 *    command char is resolved per KillCode precedent (SET_C
 *    = 's' is what retz receives here - compiler-macros.h:91
 *    last-definition-wins).
 *  - The argc==2 "-v" check returns 99 before any file opens.
 *  - CRLF input: fgets keeps "\r\n"; the <32 pair strips both;
 *    readLine strips both - same pd1 content either way.
 *  - The QUIT argless arm emits sym_/SYMSTORE copies to Rargs
 *    reference params verbatim; Blocks[IF_flg]==IF close dance
 *    kept as-is.
 *  - The HTML arm writes "%%" for '%' and four backslashes for
 *    a backslash (C %% fold) - emitted literally.
 */

// --- command codes (compiler-macros.h:90-121 effective defines) ---
private const val JOB_C = 'j'.code
private const val SET_C = 's'.code
private const val MERGE_C = 'm'.code
private const val GOTO_C = 'g'.code
private const val WRITE_C = 'w'.code
private const val IF_C = 'i'.code
private const val NEW_C = 'n'.code
private const val OPEN_C = 'o'.code
private const val ZCMDS_C = 'z'.code
private const val READ_C = 'r'.code
private const val QUIT_C = 'q'.code
private const val XECUTE_C = 'x'.code
private const val FOR_C = 'f'.code
private const val HALT_C = 'h'.code
private const val ELSE_C = 'e'.code
private const val DO_C = 'd'.code
private const val KILL_C = 'k'.code
private const val CLOSE_C = 'c'.code
private const val USE_C = 'u'.code
private const val LOCK_C = 'l'.code
private const val CMMNT_C = ';'.code
private const val BREAK_C = 'b'.code
private const val FCN_C = 'D'.code
private const val CLSQ_C = '}'.code
private const val OPSQ_C = '{'.code
private const val TRY_C = 't'.code

// --- return codes (compiler-macros.h:69-77) ---
private const val PARSE_ERROR = 1
private const val ARG_LIST_ERROR = 2
private const val NO_GLOBAL_PERMITTED = 3
private const val MEM_ERROR = 4
private const val TOO_MANY_LHS = 5
private const val ABRTX = 6
private const val KILL_DCL_ERROR = 7
private const val NEXT_LINE = 8
private const val BAD_LOCK = 9

// --- misc vendor constants ---
private const val PS = 4096 // memsize.h:32 (symlen)
private const val TAB = 9.toChar() // compiler-macros.h:127-130
private const val DOLLAR = 36.toChar() // gate rule: no raw dollar
private const val FOR_CMD = 'f'.code // Blocks[] test value (FOR)
private const val IF_CMD = 'i'.code // Blocks[] test value (IF)

private val CTABLE = arrayOf(
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
    ""
)

// --- phase-cursor constants (vendor labels) ---
private const val P_NEXT_LINE = 1
private const val P_NEXT_CMND = 2
private const val P_CTCH = 3
private const val P_CNTNUE = 4
private const val P_DO_ENTRY = 5
private const val P_DCL = 6
private const val P_ANOTHER_DO = 7
private const val P_NEXT_GOTO = 8
private const val P_GEXIT = 9
private const val P_JOB_RETURN = 10
private const val P_ABRT1 = 11
private const val P_PARSE_ERR = 12
private const val P_ARG_LIST_ERROR = 13
private const val P_NOGBL = 14
private const val P_MEM_ERR = 15
private const val P_TOO_LHS = 16
private const val P_COMMAND = 17

/** H002: the Sysout(x) macro - fprintf(Out, x). */
private fun Sysout(s: String) {
    CompilerState.out.write(s)
}

/** C isdigit on the byte-domain char. */
private fun isdigitC(c: Char): Boolean = c in '0'..'9'

/** C isalpha (ASCII) on the byte-domain char. */
private fun isalphaC(c: Char): Boolean = c in 'a'..'z' || c in 'A'..'Z'

/** C isalnum (ASCII) on the byte-domain char. */
private fun isalnumC(c: Char): Boolean = isalphaC(c) || isdigitC(c)

/** C toupper (ASCII). */
private fun toupperC(c: Char): Char = if (c in 'a'..'z') c - 32 else c

/** C tolower (ASCII). */
private fun tolowerC(c: Char): Char = if (c in 'A'..'Z') c + 32 else c

/**
 * compiler-mumps.c:42 main(argc, argv) - mumpsMain (h015).
 */
@Throws(IOException::class)
fun mumpsMain(args: Array<String>): Int {
    val st = CompilerState
    val argc = args.size
    var return_code: Int
    var phase = P_NEXT_LINE
    var i: Int
    var j: Int
    var k: Int

    st.symlen = PS.toShort()
    st.TextStart = null
    st.FunctionName[0] = NUL
    st.pid = ProcessHandle.current().pid()

    print("The Mumps Compiler\n")

    if (argc == 2 && args[1] == "-v") return 99

    if (argc == 1) {
        print("*** \u0007 Name of program to compile must be given\n\n")
        st.ExitCode = 99
        return 99
    }
        if (argc == 3) {
            if (args[1] == "-SUB") {
                st.SubFlag = 1
            } else {
                print("*** Unrecognized option: " + args[1] + "\n\n")
                st.ExitCode = 99
                return 99
            }
        }

        // for (i = strlen(argv[argc-1]); i >= 0 && argv[argc-1][i] != '.'; i--);
        i = args[argc - 1].length
        while (i >= 0 && (i >= args[argc - 1].length || args[argc - 1][i] != '.')) i--

        if (i < 0) strcpyC(st.tmp2, args[1])
        else {
            // argv[argc-1][i]=0; strcpy(tmp2,argv[argc-1]); argv[argc-1][i]='.';
            val s = args[argc - 1]
            strcpyC(st.tmp2, s.substring(0, i))
        }

        strcatC(st.tmp2, ".m")

        // Out = fopen(tmp2, "w") - h001 edge.
        st.out = java.io.BufferedWriter(FileWriter(File(cstringAt(st.tmp2))))

        strcpyC(st.output_file, cstringAt(st.tmp2))

        // in = fopen(argv[argc-1], "r") - h001 edge; PushbackReader
        // for the getc/ungetc prelude; st.in_ kept in sync (h015).
        val pb = PushbackReader(FileReader(File(args[argc - 1])), 8)
        val input = BufferedReader(pb)
        st.in_ = input

        print("Translating " + args[argc - 1] + " to C++:\n")

    CopyrightNotice.copyrightNotice(st.out)

    // ----------------------------------------------------------------
    // Check for leading #define symbols to be included first
    // ----------------------------------------------------------------
    run {
        while (true) {
            val c = pb.read()
            if (c != '+'.code) {
                if (c != -1) pb.unread(c)
                break
            }
            val ln = input.readLine()
            if (ln == null) {
                print("Unexpected end of file\n")
                st.ExitCode = 99
                return abrtx() // goto abrtx;
            }
            st.out.write(ln + "\n") // fprintf(Out, "%s\n", str)
        }
    }

    // ----------------------------------------------------------------
    // Write Include, Defines and Global Defs to Output File
    // ----------------------------------------------------------------
    Sysout("#undef _MDH_\n")
    Sysout("#include <stdio.h>\n")
    Sysout("#include <string.h>\n")
    Sysout("#include <stdlib.h>\n")
    Sysout("#include <setjmp.h>\n")
    Sysout("#include <unistd.h>\n")
    Sysout("#include <signal.h>\n")
    Sysout("#include <sys/time.h>\n")
    Sysout("#include <mumpsc/inline.h>\n")
    Sysout("#include <mumpsc/defines.h>\n")
    Sysout("#include <mumpsc/strmax.h>\n")
    Sysout("#include <mumpsc/libmpscpp.h>\n")
    Sysout("#include <mumpsc/sym.h>\n")
    Sysout("#include <mumpsc/builtin.h>\n")
    Sysout("#include <mumpsc/compiler.h>\n")
    Sysout("#define FORPARM STR_MAX  /* Size of For loop parameter */\n")
    Sysout("#define CSTR (char *)\n")
    Sysout("#define UNS unsigned char *\n")
    Sysout("#define uns unsigned char *\n")
    Sysout("#define CCHR const char *\n")
    Sysout("#include <mumpsc/keyfix.h>\n")
    Sysout("#include <mumpsc/globalOrder.h>\n")

    if (st.SubFlag != 0.toShort()) Sysout("#define SUB\n")

    Sysout("#include <mumpsc/stateVector.h>\n")
    Sysout("int (*__label_lookup)(char *);\n")
    Sysout("const char * (*__text_function)(int);\n")

    Sysout("void fcn(MSV *);\n")
    st.out.write("void zfcn(unsigned char *, unsigned char *, struct MSV *); /* z functions */\n")

    st.out.write("char * sym_(int, unsigned char *, unsigned char *, struct MSV *); /* symbol table */\n")

    st.out.write("char * makeLock(char *, char *, char *, char, char *); /* global array lock service */\n")
    st.out.write("char * cleanLocks(char *); /* global array lock service */\n")
    st.out.write("char * releaseLock(char *, char *, char *); /* global array lock service */\n")
    st.out.write("char * eraseLocks(char *); /* global array lock service */\n")
    st.out.write("void add(const char *a,const char *b,char *c); /* math function */\n")
    st.out.write("void sub(const char *a,const char *b,char *c); /* math function */\n")
    st.out.write("void mult(const char *a,const char *b,char *c); /* math function */\n")
    st.out.write("void divx(const char *a,const char *b,char *c); /* math function */\n")
    st.out.write("void divi(const char *a,const char *b,char *c); /* math function */\n")
    st.out.write("void _fnumber(unsigned char *,unsigned char *,\n")
    st.out.write("       unsigned char *,unsigned char *, MSV *); /* math function */\n")
    st.out.write("int DBSem = 0; /* deadlock prevention */\n")
    st.out.write("int pm (unsigned char [], const unsigned char pattern[], struct MSV * svPtr); /* pattern match */\n")

    // ----------------- functions -----------------

    Sysout("\nstatic inline int floatstring(const char * p) { \n" +
            " int f=0;  \n" +
            " for (; *p!=0; p++) {  \n" +
            "\t if (    isdigit(*p) || \n" +
            "\t \t*p == '-' ||  \n" +
            "\t\t*p == '+' ) continue;  \n" +
            " \tif (    *p=='.' ||  \n" +
            "\t\ttolower(*p)=='e' ) { \n" +
            "\t\tf=1;  \n" +
            "\t\tcontinue;  \n" +
            "\t\t} \n" +
            "\treturn 0; \n" +
            "\t}  \n" +
            " return f;  \n" +
            " } \n ")

    Sysout("\ninline int numcomp2(unsigned char *aa) { \n" +
            " if (floatstring((char *) aa)) return (atof( (char *) aa) != 0.0);  \n" +
            " return atoi((char *) aa);  \n" +
            " }\n")

    Sysout("\ninline char * strxcat(char * a, char *b)  \n" +
            "{ while (*a) a++; while( (*(a) = *(b)) ) { a++; b++; } return a; }\n")

    Sysout("\ninline char * strcatz(char * out, char *in)  \n" +
            "{ while (*out) out++; while (*(out) = *(in)) { out++; in++; } *(out++) = '\\x01';  \n" +
            "*out = 0; return (char *) out; } \n\n")

    st.out.write("void SyncClock() {\n")
    st.out.write("    DBSem = 1;\n")
    st.out.write("    }\n\n")

    st.out.write("unsigned int set_alarm (unsigned int seconds) {\n")
    st.out.write("    struct itimerval old, xnew;\n")
    st.out.write("    xnew.it_interval.tv_usec = 0;\n")
    st.out.write("    xnew.it_interval.tv_sec = 0 ; // interval between alarms\n")
    st.out.write("    xnew.it_value.tv_usec = 0;\n")
    st.out.write("    xnew.it_value.tv_sec = (long int) 0;\n")
    st.out.write("    if (setitimer (ITIMER_REAL, &xnew, &old) < 0)\n")
    st.out.write("        return 0;\n")
    st.out.write("    else\n")
    st.out.write("        return old.it_value.tv_sec;\n")
    st.out.write("    }\n\n")

    Sysout("#include <mumpsc/global.h>")
    linepad("#include <mumpsc/global.h>".length, "Global array definitions")

    linepad(0, "Interface to Mglobal():")
    linepad(0, "g = command")
    linepad(0, "key = key sought or stored")
    linepad(0, "bd = value returned or stored")
    linepad(0, "svPtr = address of state vector")

    Sysout("#ifndef SUB\n")
    st.out.write("#include <mumpsc/fcns.h>")
    linepad("#include <mumpsc/fcns.h>".length, "System support functions")
    st.out.write("#include <mumpsc/sysfunc.h>")
    linepad("#include <mumpsc/sysfunc.h>".length, "Many common global variables")
    Sysout("#endif\n")

    // ----------------- Compiler Start -----------------

    st.pd1cur = 1
    st.symlen = PS.toShort()
    st.t0px = 0
    st.t2 = 0
    st.cmdx = 0
    st.setpiece = 0
    st.forxmax = 0
    st.forx = 0
    st.forxFLG = 0
    st.forLine = 0
    st.pd1Last = 0
    st.ExecLevel = 0
    st.SymIncrFlg = 0
    st.ArgMax = 0
    st.IF_flg_max = 0
    st.cvarx = 0

    for (idx in 0 until CompilerState.FBMAX) st.ForBlocks[idx] = -1

    // =====================================================================
    // main line/command driver loop (phase cursor over the vendor gotos)
    // =====================================================================

    outer@ while (true) {
    when (phase) {

    // ---------------- vendor next_line (259) ----------------
    P_NEXT_LINE -> {
        st.EmitLabel = 0
        st.iiitmp = 0 /* new temp pool for each line */
        st.forLine = 0
        st.CatchFlg = 0

        st.pd1[1] = NUL // array content begins at 1, not 0

        val ln = input.readLine() // fgets(&pd1[1],512,in) - h001 fold
        if (ln == null) {
            st.pd1cur = 9999
            st.pd1len = 1
            st.pd1[1] = NUL
        } else {
            st.pd1cur = 1 // start
            st.pd1len = 2048
            val n = if (ln.length > 511) 511 else ln.length
            for (idx in 0 until n) st.pd1[1 + idx] = ln[idx]
            st.pd1[1 + n] = NUL
        }

        if (st.pd1[strlenC(st.pd1, 1)] < 32.toChar()) st.pd1[strlenC(st.pd1, 1)] = NUL // <cr>
        if (st.pd1[strlenC(st.pd1, 1)] < 32.toChar()) st.pd1[strlenC(st.pd1, 1)] = NUL // <lf>

        var li = 1
        while (st.pd1[li] != NUL && li < 512) {
            if (st.pd1[li] == TAB) st.pd1[li] = ' ' // convert tabs to blanks
            li++
        }

        st.lines++

        while (st.IF_flg > 0) { // closing braces
            st.out.write("\t}\n")
            st.IF_flg--
        }

        if (st.pd1cur > st.pd1len) {
            if (st.ExecLevel > 0) {
                while (st.ExecLevel > 0) {
                    st.out.write("\t} while (0);   // dotted un-indent " + (st.ExecX - 1) + "\n")
                    st.ExecX--
                    st.out.write("\tlongjmp(Efor" + st.ExecStack[st.ExecX.toInt()] + "J,99);    // return \n")

                    st.out.write("\nDo_Block_Bypass" + st.ExecStack[st.ExecX.toInt()] + ": ; //A541 \n")
                    st.ExecLevel--
                }

                st.ExecLevel = 0
            }

            return abrtx() // goto abrt_exit
        }

        if (st.pd1[st.pd1cur.toInt()] == NUL) {
            st.pd1cur++
            phase = P_NEXT_LINE
            continue@outer
        }

        strcpyC(st.cur_line, cstringAt(st.pd1, st.pd1cur.toInt())) // copy next program line

        // ---------------- inline C code - emit as is ----------------
        if (st.pd1[st.pd1cur.toInt()] == '%' || st.pd1[st.pd1cur.toInt()] == '+') {

            st.out.write("\n//--------------\n")
            st.out.write("//       Inline\n")
            st.out.write("//--------------\n\n")

            st.out.write(cstringAt(st.pd1, st.pd1cur.toInt() + 1) + "\n")
            while (st.pd1[st.pd1cur.toInt()] != NUL) st.pd1cur++
            st.pd1cur++
            phase = P_NEXT_LINE
            continue@outer
        }

        if (st.pd1[st.pd1cur.toInt()] == '#' ||
            (st.pd1[st.pd1cur.toInt()] == '/' && st.pd1[st.pd1cur.toInt() + 1] == '/')) { // comment
            if (st.pd1[st.pd1cur.toInt()] == '/') st.pd1cur++
            st.out.write("\n// " + cstringAt(st.pd1, st.pd1cur.toInt() + 1) + "\n")
            st.pd1cur++
            while (st.pd1[st.pd1cur.toInt()] != NUL) st.pd1cur++
            st.pd1cur++

            // h013: the vendor "if (!MAIN_flg && 0)" zmain block
            // (vendor 344-367) is compile-time dead - not rendered.

            phase = P_NEXT_LINE
            continue@outer
        }

        i = st.pd1cur.toInt()
        while (st.pd1[st.pd1cur.toInt()] != NUL && st.pd1[st.pd1cur.toInt()] != TAB) st.pd1cur++
        if (st.pd1[st.pd1cur.toInt()] != NUL)
            while (st.pd1[st.pd1cur.toInt()] != NUL && st.pd1[st.pd1cur.toInt()] == TAB) st.pd1cur++

        // ************** multiple TABs **************
        st.pd1cur = i.toShort()

        if (st.pd1[st.pd1cur.toInt()] != TAB) {
            while (st.pd1[st.pd1cur.toInt()] != TAB && st.pd1[st.pd1cur.toInt()] != NUL) {
                if (st.pd1[st.pd1cur.toInt()] == '(')
                    while (st.pd1[st.pd1cur.toInt()] != ')' && st.pd1[st.pd1cur.toInt()] != NUL)
                        st.pd1cur++
                st.pd1cur++
            }

            st.pd1[st.pd1cur.toInt()] = NUL

            var perr = 0
            run {
                var flg = 1

                // int ii, j, k, kk = 0, n = 0; int thistime=0;
                var ii = 0
                var kk = 0
                var n = 0
                var thistime = 0
                var j = 0
                var k: Char

                if (st.pd1[i] == '^' && st.pd1[i + 1] == '^') { // function start
                    i = i + 2
                    st.F_flg = 1
                    if (st.MAIN_flg != 0.toShort()) EP(0, st.ExecName, st.pid) // entry point router
                    if (st.FCN_flg != 0.toShort()) EP(1, st.ExecName, st.pid) // entry point router
                    st.FCN_flg = 1 // am in subfunction
                    thistime = 1
                } else if (st.pd1[i] == '^' && st.pd1[i + 1] != '^') { // function start
                    i = i + 1
                    st.F_flg = 1
                    if (st.MAIN_flg != 0.toShort()) EP(0, st.ExecName, st.pid) // entry point router
                    if (st.FCN_flg != 0.toShort()) EP(1, st.ExecName, st.pid) // entry point router
                    st.FCN_flg = 1 // am in subfunction
                    thistime = 1
                }

                ii = i
                while (st.pd1[i] != NUL) {

                    // open paren in label - must be function ep
                    if (st.pd1[i] == '(') {

                        if (flg == 1) {

                            Sysout("\n//=========================================" +
                                  "========================================\n")

                            st.out.write("//\tEntry Point " + cstringAt(st.pd1, ii) + " + + + + + + + \n")

                            Sysout("\n//=========================================" +
                                  "========================================\n")

                            flg = 0 // arguments found
                        }

                        st.pd1[i] = NUL

                        if (st.F_flg == 0 && st.MAIN_flg == 0.toShort()) { // Missing_Zmain
                            st.ernbr = 92
                            print("\n*** 92 Missing zmain")
                            perr = P_ABRT1
                            return@run
                        }

                        if (st.F_flg == 1 && thistime == 1) { // this is a C subfunction
                            var ff = 0
                            st.cvarx = 0
                            strcpyC(st.FunctionName, cstringAt(st.pd1, ii))

                            st.out.write("char * " + cstringAt(st.pd1, ii) +
                                "(struct MSV * svPtr, char * offset")

                            if (st.pd1[i + 1] != ')') st.out.write(",")

                            j = i + 1
                            jloop@ while (st.pd1[j] != NUL) { // separately compiled extrinsic

                                if (st.pd1[j] == '+') { // non mumpsc parameter
                                    j++
                                    while (true) {
                                        st.out.write(st.pd1[j].toString())
                                        j++
                                        if (st.pd1[j] == ',' || st.pd1[j] == ')') break
                                    }

                                    if (st.pd1[j] == ')') { // goto fe1
                                        ff = 0
                                        if (st.pd1[j] == ')') {
                                            if (ff == 0) st.out.write(") ")
                                            st.out.write("{\n\n")
                                            fcninit(st.output_file, st.pid)
                                            break@jloop
                                        }
                                    }

                                    st.out.write(",")
                                    j++
                                    continue@jloop
                                }

                                if (isalphaC(st.pd1[j])) ff = 1

                                if ((st.pd1[j] == ',' || st.pd1[j] == ')') && ff == 1) {
                                    st.out.write("const char * _Arg" + kk + st.pd1[j])
                                    kk++
                                }

                                // fe1:
                                if (st.pd1[j] == ')') {
                                    if (ff == 0) st.out.write(") ")
                                    st.out.write("{\n\n")
                                    fcninit(st.output_file, st.pid)
                                    break@jloop
                                }
                                j++
                            }

                            // ArgMax=kk+1;

                            st.out.write("__label_lookup=" + cstringAt(st.FunctionName) + "_label_lookup; \n")
                            st.out.write("__text_function=" + cstringAt(st.FunctionName) + "_text_function; \n\n")

                            st.out.write("try { // try001 \n ")

                            Sysout("svPtr->_SymPush++; \n")
                            Sysout("_SymPush++; \n")
                            Sysout("svPtr->start[svPtr->_Sym]=NULL; \n")
                            st.SymIncrFlg = 1
                            st.out.write("if (svPtr->_Sym > SYM_MAX) ")
                            st.out.write("ErrorMessage(\"Too many function calls\",svPtr->LineNumber); \n")
                            st.out.write("_doStackN[_doStx-1] = 1; // flag to restore namespace\n")
                            Sysout("\tif (svPtr->Rargsx) { // EP1005\n")
                            Sysout("\t\tfor (_i = 0; _i < svPtr->Rargsx; _i++) " +
                                   "strcpy(_Refs[_i], svPtr->Rargs[_i]); \n")
                            Sysout("\t\t_Refsx = svPtr->Rargsx; \n")
                            Sysout("\t\t} \n")
                        } else {
                            if (!isalnumC(st.pd1[ii])) { // lab_err
                                st.ernbr = 22
                                print("\n*** Labels must begin with and alphabetic character")
                                perr = P_ABRT1
                                return@run
                            }
                            if (isdigitC(st.pd1[ii])) st.out.write("_")
                            st.out.write(cstringAt(st.pd1, ii) + ": \n") // source code Label
                            Sysout("svPtr->start[svPtr->_Sym] = NULL; \n")
                            st.SymIncrFlg = 1
                            Sysout("_SymPush++; \n")
                            st.out.write("if (svPtr->_Sym > SYM_MAX) ")
                            st.out.write("ErrorMessage(\"Too many function calls\",svPtr->LineNumber); \n")
                            st.out.write("_doStackN[_doStx-1]=1; // flag to restore namespace \n")
                        }

                        st.pd1[i] = '('
                        j = i + 1
                        jw@ while (st.pd1[j] != NUL) {

                            if (st.pd1[j] == '+') {
                                while (true) {
                                    if (st.pd1[j] == ',' || st.pd1[j] == ')') break
                                    j++
                                }

                                if (st.pd1[j] == ')') {
                                    j++
                                    break@jw
                                }

                                i = j
                                continue
                            }
                            if (st.pd1[j] == ',' || st.pd1[j] == ')') {
                                k = st.pd1[j]
                                st.pd1[j] = NUL
                                if (strlenC(st.pd1, i + 1) == 0) break@jw

                                if (st.F_flg == 1) {

                                    st.out.write("\tif (_Arg" + n + "!=NULL) \n\t\t" +
                                        "sym_(100, (UNS) \"" + cstringAt(st.pd1, i + 1) + "\", (UNS) _Arg" + n +
                                        ", svPtr);" + " // Store Arguments EP1001\n")

                                    st.out.write("\tstrcpy (svPtr->RefParm[" + n + "], \"" +
                                        cstringAt(st.pd1, i + 1) + "\" ); " + "// EP1002\n")
                                } else {

                                    st.out.write("\tif (__Arg" + n + "!=NULL) sym_(100, (UNS) \"" +
                                        cstringAt(st.pd1, i + 1) + "\", (UNS) " +
                                        "__Arg" + n + ",svPtr); // Store Arguments EP1003 \n")

                                    st.out.write("\tstrcpy (svPtr->RefParm[" + n + "], \"" +
                                        cstringAt(st.pd1, i + 1) + "\" ); // EP1004 \n")
                                }

                                n++
                                if (n > st.ArgMax) st.ArgMax = n.toShort()
                                st.pd1[j] = k
                                if (k == ')') break@jw
                                i = j
                            }
                            j++
                        }
                        if (st.F_flg == 1 && thistime == 1) {
                            st.out.write("\tgoto _eps;  // entry point label router \n")
                            st.out.write("_fcn: \n")
                        }
                    }

                    i++
                }

                if (flg == 0 && st.F_flg == 0)
                    st.out.write("\tif (_ArgCount < " + n +
                        ") ErrorMessage(\"Bad argument count\", svPtr->LineNumber); \n")

                if (flg == 1) {
                    var li2 = ii
                    while (st.pd1[li2] != NUL) {
                        if (st.pd1[li2] != ' ') { // label

                            st.EmitLabel = 1

                            if (!isalnumC(st.pd1[ii])) { // lab_err
                                st.ernbr = 22
                                print("\n*** Labels must begin with and alphabetic character")
                                perr = P_ABRT1
                                return@run
                            }

                            val ls: Txt
                            if (st.LabelsStart == null) { // linked list setup
                                ls = Txt()
                                ls.next = null
                                ls.lnbr = st.lines
                                ls.name =
                                    (if (isdigitC(st.pd1[ii])) "_" else "") + cstringAt(st.pd1, ii)
                                st.LabelsStart = ls
                            } else { // existing linked list
                                ls = Txt()
                                ls.next = st.LabelsStart
                                ls.lnbr = st.lines
                                ls.name =
                                    (if (isdigitC(st.pd1[ii])) "_" else "") + cstringAt(st.pd1, ii)
                                st.LabelsStart = ls
                            }
                            break
                        }
                        li2++
                    }
                }

                st.pd1[st.pd1cur.toInt()] = TAB // replace
            }
            if (perr != 0) {
                phase = perr
                continue@outer
            }
        }

        while (st.pd1[st.pd1cur.toInt()] == TAB) st.pd1cur++

        // ----------------------------------------------------------------
        // Doty Indents
        // ----------------------------------------------------------------
        var dj = 0
        i = st.pd1cur.toInt()
        if (st.pd1[st.pd1cur.toInt()] == '.') {

            // for (i = pd1cur; pd1[i] == '.'||pd1[i]==' '; i++) if (pd1[i]=='.') j++;
            while (st.pd1[i] == '.' || st.pd1[i] == ' ') {
                if (st.pd1[i] == '.') dj++ // how many levels?
                i++
            }

            if (dj > 10) {
                print("*** Too many block indents\n")
                st.ernbr = 99
                phase = P_ABRT1
                continue@outer
            }

            if (dj > st.ExecLevel + 1) { // skipped a level
                print("*** Code level skip\n")
                st.ernbr = 99
                phase = P_ABRT1
                continue@outer
            }

            // ExecLevel set to j in function
            val execLevelA = shortArrayOf(st.ExecLevel)
            val execNameA = shortArrayOf(st.ExecName)
            val pd1LastA = shortArrayOf(st.pd1Last)
            val execXA = shortArrayOf(st.ExecX)
            st.ernbr = doty(dj, execLevelA, execNameA, st.DoRet, st.DoRetX,
                pd1LastA, st.pd1cur, st.ExecStack, execXA, st.ExecSym).toShort()
            st.ExecLevel = execLevelA[0]
            st.ExecName = execNameA[0]
            st.pd1Last = pd1LastA[0]
            st.ExecX = execXA[0]
            if (st.ernbr != 0.toShort()) {
                phase = P_ABRT1
                continue@outer
            }

            st.pd1cur = i.toShort()
        } else if (st.ExecLevel > 0) {
            while (st.ExecLevel > 0) {
                st.out.write("\t} while(0); // dotted un-indent " + (st.ExecX - 1) + " \n")

                st.ExecX--
                st.out.write("\tlongjmp(Efor" + st.ExecStack[st.ExecX.toInt()] + "J,99); // return \n")

                st.out.write("\nDo_Block_Bypass" + st.ExecStack[st.ExecX.toInt()] + ": ; //B848 \n\n")
                st.ExecLevel--
            }
            st.ExecLevel = 0
        }

        if (st.ExecLevel == 0.toShort()) st.forxFLG = 0

        // ----------------------------------------------------------------
        st.xpx = 1
        j = 1

        // copy source line to xd
        st.pd1Last = st.pd1cur

        while (true) {
            val c = st.pd1[st.pd1cur.toInt()]
            st.pd1cur++
            st.xd[j] = c
            j++
            if (c == NUL) break
        }

        Sysout("\n//=============================================" +
              "==================================== \n")

        if ((st.MAIN_flg != 0.toShort() || st.FCN_flg != 0.toShort()) && st.TryFlg == 0)
            st.out.write("svPtr->LineNumber=" + st.lines + "; // " + cstringAt(st.cur_line) + " \n")
        else
            st.out.write("// " + cstringAt(st.cur_line) + " \n")

        Sysout("\n//=============================================" +
              "==================================== \n")

        // write the label
        if (st.EmitLabel != 0.toShort()) {
            if (st.ExecLevel > 0) { // label_in_block
                st.ernbr = 125
                print("\n*** 125 Label not allowed in block")
                phase = P_ABRT1
                continue@outer
            }
            st.out.write("\n" + checkNotNull(st.LabelsStart).name + ": \n")
            st.EmitLabel = 0
        }

        st.xd[j] = NUL

        phase = P_NEXT_CMND
        continue@outer
    }

    // ---------------- vendor next_cmnd (712) ----------------
    P_NEXT_CMND -> {
        if (st.IF_flg > st.IF_flg_max) st.IF_flg_max = st.IF_flg

        while (true) {

            if (st.xpx >= 1024 || st.xd[st.xpx.toInt()] == NUL) { // the order of these tests is important

                if (st.IF_flg > 0) {

                    var li = st.IF_flg
                    while (li > 0) {

                        st.out.write("\t} ")

                        if (st.ForBlocks[li] >= 0) {

                            st.out.write("while (0); longjmp(for" + st.ForBlocks[li] +
                                "J, _i); " + "// FOR loop " + li + " close \n")

                            if (st.Blocks[li] == FOR_CMD.toShort())
                                st.out.write("\nFor" + st.ForBlocks[li] + "Exit: ; // exit for loop \n\n")

                            st.ForBlocks[li] = -1

                        } else st.out.write("\n")
                        li--
                    }

                    st.IF_flg = 0
                }

                if (st.TryFlg != 0) {
                    st.out.write("\t} \n")
                    st.TryFlg = 1
                }

                if (st.CatchFlg != 0) {
                    st.out.write("\t} \n")
                    st.CatchFlg = 0
                }

                phase = P_NEXT_LINE
                continue@outer
            }

            if (st.xd[st.xpx.toInt()] != ' ' && st.xd[st.xpx.toInt()] != TAB)
                break

            st.xpx++
        }

        // #++++ decode command word
        st.p2px = st.xpx.toInt().toShort()
        st.t2 = 0
        st.t0px = 0
        st.JOB_flg = 0
        st.POST_flg = 0

        if (strncmpC(st.xd, st.xpx.toInt(), "//", 2) == 0) {
            st.xpx = 9999
            phase = P_NEXT_CMND
            continue@outer
        }

        st.icmnd = tolowerC(st.xd[st.xpx.toInt()]).code.toShort()

        // ........................ check command word
        run {
            val word = CharArray(25)
            var wx = 1
            var flg = 1
            st.MergeFlg = 0
            word[0] = st.icmnd.toInt().toChar()

            while (true) {
                st.xpx++
                if (isalphaC(st.xd[st.xpx.toInt()])) {
                    word[wx] = tolowerC(st.xd[st.xpx.toInt()])
                    wx++
                } else break
            }

            word[wx] = NUL
            val ws = cstringAt(word)

            var ci = 0
            while (CTABLE[ci].isNotEmpty()) {
                if (ws == CTABLE[ci]) {
                    flg = 0
                    break
                }
                ci++
            }

            // if (icmnd == ';') goto pstrtn; - falls into the
            // pstrtn block below (the ':' postconditional test
            // skips ';' naturally; ';' dispatches CMMNT_C).

            if (flg == 1) {
                print("*** Warning, possible command spelling error: " + ws + " ")
                print("in or near line " + st.lines + "\n")
                print("*** " + cstringAt(st.pd1, st.pd1Last.toInt()) + "\n")
            }
        }
        // ........................
        run { // the pstrtn block (':' postconditional + dispatch)

            // vendor pstrtn block (the ':' postconditional + dispatch)
            if (st.xd[st.xpx.toInt()] == ':') { // post conditionals
                st.xpx++

                val r = atTest(st.xd, st.pd1, st.pd1Last, st.lines, st.xpx)
                st.at_flg = r.rc.toShort()
                st.pd1Last = r.pd1Last

                if (st.at_flg == 99.toShort()) return abrtx() // goto abrt_exit

                if (st.at_flg != 0.toShort()) {

                    val tmp = StringBuilder()
                    tmp.append("i ")
                    atCopy(st.at_flg.toInt(), tmp)

                    st.interpret(tmp.toString())

                    st.xpx = st.at_flg.toLong()
                    st.out.write("\tif (svPtr->tpx) { // m853 postconditional \n")
                    st.POST_flg = 1
                } else {
                    st.ierr = parse_().toShort()
                    if (st.ierr != 0.toShort()) { phase = P_PARSE_ERR; return@run }
                    st.out.write("\tif (atoi( CSTR " + Extract2(st.pd1, st.sdlim.toInt()) +
                        ")) { // m859 postconditional\n")
                    st.POST_flg = 1
                }
            }

            // vendor pstrtn label:
            if (st.xd[st.xpx.toInt()] != NUL) st.xpx++

            // h013: the vendor "if (0)" zmain block (vendor 882-903)
            // is compile-time dead - not rendered.

            // #+++++ branch to correct command handler
            when (st.icmnd.toInt()) {
                CLSQ_C -> {
                    Sysout("\t} /* user close */\n")
                    phase = P_NEXT_CMND
                    return@run
                }

                OPSQ_C -> {
                    phase = P_NEXT_CMND
                    return@run
                }

                TRY_C -> {
                    st.out.write("try { ")
                    st.TryFlg = 1
                    if (st.xd[st.xpx.toInt()] == ' ' || st.xd[st.xpx.toInt()] == NUL) {
                        phase = P_NEXT_CMND
                        return@run
                    }
                    var li = st.xpx.toInt()
                    while (st.xd[li] != ' ' && st.xd[li] != NUL) li++
                    j = st.xd[li].code
                    st.xd[li] = NUL
                    if (cstringAt(st.xd, st.xpx.toInt()) == "NoMessages") {
                        st.out.write("svPtr->NOERR=1; ")
                    } else { // try_unknown
                        st.ernbr = 124
                        print("\n*** 124 Unknown option in try")
                        phase = P_ABRT1
                        return@run
                    }
                    st.xd[li] = j.toChar()
                    st.xpx = li.toLong()
                    phase = P_NEXT_CMND
                    return@run
                }

                CMMNT_C -> {
                    st.p2px++
                    if (st.xd[st.p2px.toInt()] == ';') {
                        if (st.TextStart == null) {
                            val ts = Txt()
                            ts.name = cstringAt(st.xd, st.p2px.toInt() + 1)
                            ts.lnbr = st.lines
                            ts.next = null
                            st.TextStart = ts
                            phase = P_NEXT_LINE
                            return@run
                        }
                        val p1 = Txt()
                        p1.name = cstringAt(st.xd, st.p2px.toInt() + 1)
                        p1.next = st.TextStart
                        p1.lnbr = st.lines
                        st.TextStart = p1
                        phase = P_NEXT_LINE
                        return@run
                    }

                    phase = P_NEXT_LINE
                    return@run
                }

                FCN_C -> {
                    print("*** Not supported\n")
                    print("*** " + cstringAt(st.cur_line) + "\n")
                    st.ExitCode = 99
                    return abrtx() // goto abrtx;
                }

                NEW_C -> {
                    st.out.write("\n//--------------\n")
                    st.out.write("//       New\n")
                    st.out.write("//--------------\n\n")

                    return_code = NewCode.newCode()

                    if (return_code == 0) { phase = P_NEXT_CMND; return@run }
                    if (return_code == PARSE_ERROR) { phase = P_PARSE_ERR; return@run }
                    if (return_code == ARG_LIST_ERROR) { phase = P_ARG_LIST_ERROR; return@run }
                    if (return_code == NO_GLOBAL_PERMITTED) { phase = P_NOGBL; return@run }
                    if (return_code == MEM_ERROR) { phase = P_MEM_ERR; return@run }
                    if (return_code == TOO_MANY_LHS) { phase = P_TOO_LHS; return@run }
                    if (return_code == ABRTX) return abrtx() // goto abrtx;
                    phase = P_COMMAND // command error
                    return@run
                }

                BREAK_C -> {
                    st.out.write("\n//--------------\n")
                    st.out.write("//       Break\n")
                    st.out.write("//--------------\n\n")

                    if (st.forLine != 0) { // nobrk_err
                        st.ernbr = 99
                        print("\n*** Break not permitted here")
                        phase = P_ABRT1
                        return@run
                    }

                    st.out.write("     svPtr->tpx=tpx" + st.ExecStack[st.ExecX.toInt() - 1] +
                        "; // break code: restore \u0024test\n" +
                        "  longjmp(Efor" + st.ExecStack[st.ExecX.toInt() - 1] + "J,100); \n")

                    if (st.POST_flg != 0.toShort()) {
                        st.out.write(" } /* post conditional after break */\n")
                        st.POST_flg = 0
                    }

                    phase = P_NEXT_CMND
                    return@run
                }

                LOCK_C -> {
                    st.out.write("\n//--------------\n")
                    st.out.write("//       Lock\n")
                    st.out.write("//--------------\n\n")

                    return_code = lockCode()

                    if (return_code == 0) { phase = P_NEXT_CMND; return@run }
                    if (return_code == PARSE_ERROR) { phase = P_PARSE_ERR; return@run }
                    if (return_code == ARG_LIST_ERROR) { phase = P_ARG_LIST_ERROR; return@run }
                    if (return_code == NO_GLOBAL_PERMITTED) { phase = P_NOGBL; return@run }
                    if (return_code == MEM_ERROR) { phase = P_MEM_ERR; return@run }
                    if (return_code == TOO_MANY_LHS) { phase = P_TOO_LHS; return@run }
                    if (return_code == ABRTX) return abrtx() // goto abrtx;
                    if (return_code == BAD_LOCK) { // bad_lock
                        st.ernbr = 99
                        print("\n*** Lock error")
                        phase = P_ABRT1
                        return@run
                    }
                    phase = P_COMMAND // command error
                    return@run
                }

                READ_C -> {
                    st.out.write("\n//--------------\n")
                    st.out.write("//       Read\n")
                    st.out.write("//--------------\n\n")

                    return_code = readCode()

                    if (return_code == 0) { phase = P_NEXT_CMND; return@run }
                    if (return_code == PARSE_ERROR) { phase = P_PARSE_ERR; return@run }
                    if (return_code == ARG_LIST_ERROR) { phase = P_ARG_LIST_ERROR; return@run }
                    if (return_code == NO_GLOBAL_PERMITTED) { phase = P_NOGBL; return@run }
                    if (return_code == MEM_ERROR) { phase = P_MEM_ERR; return@run }
                    if (return_code == TOO_MANY_LHS) { phase = P_TOO_LHS; return@run }
                    if (return_code == ABRTX) return abrtx() // goto abrtx;
                    phase = P_COMMAND // command error
                    return@run
                }

                OPEN_C -> {
                    st.out.write("\n//--------------\n")
                    st.out.write("//       Open\n")
                    st.out.write("//--------------\n\n")

                    return_code = MumpsOpen.mumpsOpen()
                    if (return_code == 0) { phase = P_NEXT_CMND; return@run }
                    if (return_code == PARSE_ERROR) { phase = P_PARSE_ERR; return@run }
                    if (return_code == ARG_LIST_ERROR) { phase = P_ARG_LIST_ERROR; return@run }
                    phase = P_COMMAND // default
                    return@run
                }

                ZCMDS_C -> {
                    return_code = zcommandsCode()

                    if (return_code == 0) { phase = P_NEXT_CMND; return@run }
                    if (return_code == PARSE_ERROR) { phase = P_PARSE_ERR; return@run }
                    if (return_code == ARG_LIST_ERROR) { phase = P_ARG_LIST_ERROR; return@run }
                    if (return_code == NO_GLOBAL_PERMITTED) { phase = P_NOGBL; return@run }
                    if (return_code == MEM_ERROR) { phase = P_MEM_ERR; return@run }
                    if (return_code == TOO_MANY_LHS) { phase = P_TOO_LHS; return@run }
                    if (return_code == ABRTX) return abrtx() // goto abrtx;
                    if (return_code == NEXT_LINE) { phase = P_NEXT_LINE; return@run }
                    phase = P_COMMAND // command error
                    return@run
                }

                IF_C -> {
                    st.out.write("\n//--------------\n")
                    st.out.write("//       If\n")
                    st.out.write("//--------------\n\n")

                    return_code = IfCode.ifCode()

                    if (return_code == 0) { phase = P_NEXT_CMND; return@run }
                    if (return_code == PARSE_ERROR) { phase = P_PARSE_ERR; return@run }
                    if (return_code == ARG_LIST_ERROR) { phase = P_ARG_LIST_ERROR; return@run }
                    if (return_code == NO_GLOBAL_PERMITTED) { phase = P_NOGBL; return@run }
                    if (return_code == MEM_ERROR) { phase = P_MEM_ERR; return@run }
                    if (return_code == TOO_MANY_LHS) { phase = P_TOO_LHS; return@run }
                    if (return_code == ABRTX) return abrtx() // goto abrtx;
                    phase = P_COMMAND // command error
                    return@run
                }

                WRITE_C -> {
                    st.out.write("\n//--------------\n")
                    st.out.write("//       Write\n")
                    st.out.write("//--------------\n\n")

                    return_code = writeCode()

                    if (return_code == 0) { phase = P_NEXT_CMND; return@run }
                    if (return_code == PARSE_ERROR) { phase = P_PARSE_ERR; return@run }
                    if (return_code == ARG_LIST_ERROR) { phase = P_ARG_LIST_ERROR; return@run }
                    if (return_code == NO_GLOBAL_PERMITTED) { phase = P_NOGBL; return@run }
                    if (return_code == MEM_ERROR) { phase = P_MEM_ERR; return@run }
                    if (return_code == TOO_MANY_LHS) { phase = P_TOO_LHS; return@run }
                    if (return_code == ABRTX) return abrtx() // goto abrtx;
                    phase = P_COMMAND // command error
                    return@run
                }

                JOB_C -> {
                    st.out.write("\n//--------------\n")
                    st.out.write("//       Job\n")
                    st.out.write("//--------------\n\n")

                    st.out.write("\tif (fork()!=0) {")
                    linepad("\tif (fork()!=0) {".length, "I am child process if zero")
                    st.JOB_flg = 1
                    phase = P_DO_ENTRY
                    return@run
                }

                MERGE_C, SET_C -> {
                    if (st.icmnd.toInt() == MERGE_C) st.MergeFlg = 1

                    if (toupperC(st.xd[st.p2px.toInt() + 1]) == 'H') { // SHELL command

                        st.out.write("\n//--------------\n")
                        st.out.write("//       Shell\n")
                        st.out.write("//--------------\n\n")

                        st.interpret(cstringAt(st.xd, st.p2px.toInt()))
                        phase = P_NEXT_LINE
                        return@run
                    }

                    if (toupperC(st.xd[st.p2px.toInt() + 1]) == 'Q') { // SQL
                        st.interpret(cstringAt(st.xd, st.p2px.toInt()))
                        phase = P_NEXT_LINE
                        return@run
                    }

                    st.out.write("\n//--------------\n")
                    st.out.write("//       Set\n")
                    st.out.write("//--------------\n\n")

                    st.retz = SET_C.toShort()

                    return_code = setCode(SET_C) // goto set; // process item to read

                    if (return_code == 0) { phase = P_NEXT_CMND; return@run }
                    if (return_code == PARSE_ERROR) { phase = P_PARSE_ERR; return@run }
                    if (return_code == ARG_LIST_ERROR) { phase = P_ARG_LIST_ERROR; return@run }
                    if (return_code == NO_GLOBAL_PERMITTED) { phase = P_NOGBL; return@run }
                    if (return_code == MEM_ERROR) { phase = P_MEM_ERR; return@run }
                    if (return_code == TOO_MANY_LHS) { phase = P_TOO_LHS; return@run }
                    if (return_code == ABRTX) return abrtx() // goto abrtx;
                    phase = P_COMMAND // command error
                    return@run
                }

                XECUTE_C -> {
                    st.out.write("\n//--------------\n")
                    st.out.write("//       Xecute\n")
                    st.out.write("//--------------\n\n")

                    val tmp = StringBuilder()
                    tmp.append("x ")
                    var li = st.xpx.toInt()
                    while (st.xd[li] != NUL) {
                        if (st.xd[li] != '"') {
                            tmp.append(st.xd[li])
                        } else {
                            tmp.append('\\')
                            tmp.append(st.xd[li])
                        }
                        li++
                    }

                    st.interpret(tmp.toString())

                    if (st.POST_flg != 0.toShort()) {
                        st.out.write("\t} // post conditional xecute\n")
                        st.POST_flg = 0
                    }
                    st.xpx = 1024
                    phase = P_NEXT_CMND
                    return@run
                }

                FOR_C -> {
                    st.out.write("\n//--------------\n")
                    st.out.write("//       For\n")
                    st.out.write("//--------------\n\n")

                    return_code = forCode()

                    if (return_code == 0) { phase = P_NEXT_CMND; return@run }
                    if (return_code == PARSE_ERROR) { phase = P_PARSE_ERR; return@run }
                    if (return_code == ARG_LIST_ERROR) { phase = P_ARG_LIST_ERROR; return@run }
                    if (return_code == NO_GLOBAL_PERMITTED) { phase = P_NOGBL; return@run }
                    if (return_code == MEM_ERROR) { phase = P_MEM_ERR; return@run }
                    if (return_code == TOO_MANY_LHS) { phase = P_TOO_LHS; return@run }
                    if (return_code == ABRTX) return abrtx() // goto abrtx;
                    phase = P_COMMAND // command error
                    return@run
                }

                HALT_C -> { // and HANG
                    if ((toupperC(st.xd[st.p2px.toInt() + 1]) != 'T' &&
                         st.xd[st.xpx.toInt()] == ' ') || st.xd[st.xpx.toInt()] == NUL) { // halt

                        st.out.write("\n//--------------\n")
                        st.out.write("//       Halt\n")
                        st.out.write("//--------------\n\n")

                        if (st.MAIN_flg != 0.toShort()) st.out.write("\tgoto _epilogue; \n")

                        if (st.FCN_flg != 0.toShort()) st.out.write("\treturn \"\"; \n")

                        if (st.POST_flg != 0.toShort()) {
                            st.out.write("\t} /* post conditional halt*/\n")
                            st.POST_flg = 0
                        }

                        phase = P_NEXT_CMND
                        return@run
                    }

                    if (toupperC(st.xd[st.p2px.toInt() + 1]) == 'T') { // HTML command

                        st.out.write(" /* HTML */ printf(\"")

                        while (st.xd[st.xpx.toInt()] != NUL) {

                            if (st.xd[st.xpx.toInt()] == '&' && st.xd[st.xpx.toInt() + 1] == '~') {
                                st.xpx += 2
                                st.out.write("\\\");\n")
                                st.t2 = 1

                                j = st.xpx.toInt()
                                while (st.xd[j] != '~' && st.xd[j] != NUL) j++

                                st.xd[j] = ' '

                                st.ierr = parse_().toShort()
                                if (st.ierr != 0.toShort()) { phase = P_PARSE_ERR; return@run }
                                st.out.write("\tprintf(\"%s\", " + Extract2(st.pd1, st.sdlim.toInt()) + "); \n")
                                st.out.write("\tprintf(\"")
                                st.xpx++
                                continue
                            }

                            if (st.xd[st.xpx.toInt()] == '&' && st.xd[st.xpx.toInt() + 1] == '!') {
                                st.xpx += 2
                                st.out.write("\\n")
                                continue
                            }

                            if (st.xd[st.xpx.toInt()] == '"') st.out.write("\\\"")
                            else if (st.xd[st.xpx.toInt()] == '\'') st.out.write("\\'")
                            else if (st.xd[st.xpx.toInt()] == '%') st.out.write("%%")
                            else if (st.xd[st.xpx.toInt()] == '\\') st.out.write("\\\\")
                            else st.out.write(st.xd[st.xpx.toInt()].toString())
                            st.xpx++
                        }

                        st.out.write("\");\n")
                        phase = P_NEXT_LINE
                        return@run
                    }

                    while (true) { // Hang
                        st.out.write("\n//--------------\n")
                        st.out.write("//       Hang\n")
                        st.out.write("//--------------\n\n")
                        st.t2 = 1
                        st.ierr = parse_().toShort()
                        if (st.ierr != 0.toShort()) { phase = P_PARSE_ERR; return@run }
                        st.out.write("\t_i = atoi( CSTR " + Extract2(st.pd1, st.sdlim.toInt()) + ");\n")
                        st.out.write("\tif (_i > 0) sleep(_i);\n")

                        if (st.xd[st.xpx.toInt()] == ',') {
                            st.xpx++
                            continue
                        }

                        if (st.POST_flg != 0.toShort()) {
                            st.out.write("\t} /* post conditional hang*/\n")
                            st.POST_flg = 0
                        }

                        // goto next_cmnd
                        break
                    }
                    phase = P_NEXT_CMND
                    return@run
                }

                ELSE_C -> {
                    if (tolowerC(st.xd[st.p2px.toInt() + 1]) == 'x') {
                        st.exportflag = 1
                    }

                    st.out.write("\n//--------------\n")
                    st.out.write("//       Else\n")
                    st.out.write("//--------------\n\n")

                    if (st.xd[st.xpx.toInt()] == ' ') st.xpx++

                    st.out.write("\tif (svPtr->tpx == 0) { \n")

                    st.IF_flg++
                    phase = P_NEXT_CMND
                    return@run
                }

                DO_C -> { // declare
                    if (toupperC(st.xd[st.p2px.toInt() + 1]) == 'A') { // SHELL command

                        val tmp = StringBuilder()
                        var li2 = st.xpx.toInt()
                        var li = 0
                        while (st.xd[li2] != NUL) {
                            if (st.xd[li2] == '"') {
                                tmp.append('\\')
                            }
                            tmp.append(st.xd[li2])
                            li2++
                        }

                        st.interpret(tmp.toString())
                        phase = P_NEXT_LINE
                        return@run
                    }

                    if (tolowerC(st.xd[st.p2px.toInt() + 1]) == 'e') { // declare
                        // int i=0 then the dcl loop - the i
                        // carrier is local to P_DCL (vendor i reset
                        // at the label).
                        phase = P_DCL
                        return@run
                    }

                    phase = P_DO_ENTRY
                    return@run
                }

                KILL_C -> {
                    st.out.write("\n//--------------\n")
                    st.out.write("//       Kill\n")
                    st.out.write("//--------------\n\n")

                    return_code = killCode()

                    if (return_code == 0) { phase = P_NEXT_CMND; return@run }
                    if (return_code == PARSE_ERROR) { phase = P_PARSE_ERR; return@run }
                    if (return_code == ARG_LIST_ERROR) { phase = P_ARG_LIST_ERROR; return@run }
                    if (return_code == NO_GLOBAL_PERMITTED) { phase = P_NOGBL; return@run }
                    if (return_code == MEM_ERROR) { phase = P_MEM_ERR; return@run }
                    if (return_code == TOO_MANY_LHS) { phase = P_TOO_LHS; return@run }
                    if (return_code == KILL_DCL_ERROR) { // kill_dcl_err
                        st.ernbr = 99
                        print("\n*** Attempt to Kill declared variable\n")
                        phase = P_ABRT1
                        return@run
                    }
                    if (return_code == ABRTX) return abrtx() // goto abrtx;
                    phase = P_COMMAND // command error
                    return@run
                }

                CLOSE_C -> {
                    st.out.write("\n//--------------\n")
                    st.out.write("//       Close\n")
                    st.out.write("//--------------\n\n")

                    st.p2px++
                    if (st.xd[st.p2px.toInt()].code <= 90)
                        st.xd[st.p2px.toInt()] = (st.xd[st.p2px.toInt()].code + 32).toChar() // lower case

                    if (st.xd[st.p2px.toInt()] == 'o') { // goto cntnue
                        phase = P_CNTNUE
                        return@run
                    }
                    if (st.xd[st.p2px.toInt()] == 'a') { // goto ctch
                        phase = P_CTCH
                        return@run
                    }

                    while (true) {
                        st.t2 = 1
                        st.ierr = parse_().toShort()
                        if (st.ierr != 0.toShort()) { phase = P_PARSE_ERR; return@run }
                        st.out.write("\t_i=atoi( CSTR " + Extract2(st.pd1, st.sdlim.toInt()) + ");\n")
                        st.out.write("\tif (svPtr->in_file[_i] == NULL && svPtr->out_file[_i] == NULL) \n")
                        st.out.write("\t\tErrorMessage(\"Attempt to close unopened file\", " +
                            "svPtr->LineNumber); \n")

                        st.out.write("\tif (svPtr->in_file[_i]!=NULL) { \n" +
                            "\tfclose(svPtr->in_file[_i]); svPtr->in_file[_i]=NULL; }\n")

                        st.out.write("\telse { fclose(svPtr->out_file[_i]); svPtr->out_file[_i]=NULL; }\n")

                        if (st.xd[st.xpx.toInt()] != ',') {
                            if (st.POST_flg != 0.toShort()) {
                                st.out.write("\t} /* post conditional 3240*/\n")
                                st.POST_flg = 0
                            }
                            break // goto next_cmnd
                        }
                        st.xpx++
                    }
                    phase = P_NEXT_CMND
                    return@run
                }

                USE_C -> {
                    st.out.write("\n//--------------\n")
                    st.out.write("//       Use\n")
                    st.out.write("//--------------\n\n")

                    st.t2 = 1
                    st.ierr = parse_().toShort()
                    if (st.ierr != 0.toShort()) { phase = P_PARSE_ERR; return@run }
                    st.out.write("\tsvPtr->io=atoi( CSTR " + Extract2(st.pd1, st.sdlim.toInt()) + ");\n")

                    if (st.POST_flg != 0.toShort()) {
                        st.out.write("\t} /* post conditional 3245*/\n")
                        st.POST_flg = 0
                    }

                    phase = P_NEXT_CMND
                    return@run
                }

                GOTO_C -> {
                    st.out.write("\n//--------------\n")
                    st.out.write("//       Goto\n")
                    st.out.write("//--------------\n\n")

                    if (st.xd[st.xpx.toInt()] == NUL) { // lbl_error
                        st.ernbr = 8
                        print("\n*** Label or file not found")
                        phase = P_ABRT1
                        return@run
                    }

                    if (st.ExecLevel > 0) { // goto_in_block
                        st.ernbr = 126
                        print("\n*** 126 Goto not allowed in block")
                        phase = P_ABRT1
                        return@run
                    }

                    // next_goto: falls through to the P_NEXT_GOTO body
                    phase = P_NEXT_GOTO
                    return@run
                }

                QUIT_C -> { // entered via the dispatch when spelled 'q'
                    phase = P_CNTNUE
                    return@run
                }

                else -> { // command:
                    st.ernbr = 13
                    print("\n*** 13 Command Error")
                    phase = P_ABRT1
                    return@run
                }
            }
        }
    }

    // ---------------- vendor cntnue: case QUIT (1278) ----------------
    P_CNTNUE -> {
        st.out.write("\n//--------------\n")
        st.out.write("//       Quit\n")
        st.out.write("//--------------\n\n")

        if (st.POST_flg != 0.toShort()) {
            if (st.ForBlocks[st.IF_flg] <= 0)
                st.out.write("\tbreak; } // post conditional quit m2386 \n")
            else
                st.out.write("\tlongjmp(for" + st.ForBlocks[st.IF_flg] +
                    "J,100); /* m1146 quit*/ } // m1146 quit\n\n")
            st.POST_flg = 0
            phase = P_NEXT_CMND
            continue@outer
        }

        if (st.ExecLevel == 0.toShort() && st.forxFLG == 0) {

            Sysout("\tif (svPtr->Rargsx) { // call-by-reference? \n")
            Sysout("\t\tint i; \n")
            Sysout("\t\tunsigned char *tmp = new unsigned char[STR_MAX]; \n")
            Sysout("\t\tfor (i = 0; i < svPtr->Rargsx; i++) { \n")
            Sysout("\t\tsym_(1, (UNS) svPtr->RefParm[i], (UNS) tmp,svPtr); \n")
            Sysout("\t\tsym_(SYMSTORE, (UNS) svPtr->Rargs[i], (UNS) tmp, svPtr); \n")
            Sysout("\t\t} \n")
            Sysout("\t\t} \n")

            st.out.write("\tstrmove((UNS) _ReturnVal, (UNS) \"\"); \n")

            if (st.xd[st.xpx.toInt()] != ' ' && st.xd[st.xpx.toInt()] != NUL) {

                st.t2 = 1
                st.ierr = parse_().toShort()
                if (st.ierr != 0.toShort()) { phase = P_PARSE_ERR; continue@outer }

                st.out.write("\tstrmove((UNS) _ReturnVal, (UNS) " +
                    Extract2(st.pd1, st.sdlim.toInt()) + "); \n")
            }

            if (st.F_flg != 0) {

                st.out.write("\treturn (char *) _ReturnVal; \n")
            } else {

                st.out.write("\tif (_doStackN[_doStx]) {  _doStackN[_doStx] = 0; } // Q1001 \n")

                st.out.write("\tif (_doStx > 0) { longjmp(_doStack[_doStx],99); } \n")
                st.out.write("\telse ErrorMessage(\"Too Many Quits\", svPtr->LineNumber); \n")
            }
        } else {

            if (st.IF_flg == 0 || st.ForBlocks[st.IF_flg] < 0)
                st.out.write("\tbreak; \n")
            else
                st.out.write("\tgoto For" + st.ForBlocks[st.IF_flg] + "Exit; \n")

            if (st.Blocks[st.IF_flg] == IF_CMD.toShort()) {
                st.Blocks[st.IF_flg] = 0
                st.IF_flg--
                st.out.write("\t} // if block 2389\n")
            }
        }

        phase = P_NEXT_CMND
        continue@outer
    }

    // ---------------- vendor ctch label inside case TRY (939) ----------------
    P_CTCH -> {
        st.out.write("catch(")
        st.CatchFlg = 1
        st.TryFlg = 0
        if (st.xd[st.xpx.toInt()] == ' ' || st.xd[st.xpx.toInt()] == NUL) {
            st.ernbr = 122 // catch_error
            print("\n*** 122 Missing catch argument")
            phase = P_ABRT1
            continue@outer
        }
        var li = st.xpx.toInt()
        while (st.xd[li] != ' ' && st.xd[li] != NUL) li++
        j = st.xd[li].code
        st.xd[li] = NUL

        st.flg = 1
        var k = 0
        val ex = checkNotNull(st.except)
        var exk = ex[k] ?: ""
        while (exk.isNotEmpty()) {
            if (cstringAt(st.xd, st.xpx.toInt()) == exk) {
                st.flg = 0
                break
            }
            k++
            exk = ex[k] ?: ""
        }
        if (st.flg == 1) { // catch_unknown
            st.ernbr = 123
            print("\n*** 123 Unknown exception in catch")
            phase = P_ABRT1
            continue@outer
        }
        st.out.write(cstringAt(st.xd, st.xpx.toInt()) + " ) {\n")
        st.xd[li] = j.toChar()
        st.xpx = li.toLong()
        phase = P_NEXT_CMND
        continue@outer
    }

    // ---------------- vendor do_entry label (1430) ----------------
    P_DO_ENTRY -> {
        st.out.write("\n//--------------\n")
        st.out.write("//       Do\n")
        st.out.write("//--------------\n\n")

        // #++++++++++ Argumentless DO
        if (st.xd[st.xpx.toInt()] == NUL || st.xd[st.xpx.toInt()] == ' ') {

            if (st.JOB_flg != 0.toShort()) { // is this really a JOB command?
                print("*** JOB requires argument\n")
                phase = P_ARG_LIST_ERROR
                continue@outer
            }

            st.out.write("\t\ttpx" + st.ExecName + " = svPtr->tpx; \n")

            st.out.write("\t\tif ( (_i = setjmp(Efor" + st.ExecName + "J)) == 0) ")
            st.out.write("goto Do_Block_Entry" + st.ExecName + "; //else continue \n")

            if (st.IF_flg != 0)
                st.out.write("\t\telse if (_i == 100) " +
                    "goto Do_Block_Bypass" + st.ExecName + "; // IF_flg m1402\n") // must be in IF

            st.forx++
            st.DoRet[st.DoRetX.toInt()] = st.ExecName
            st.DoRetX++

            if (st.POST_flg != 0.toShort()) {
                st.out.write("\t} // post conditional do\n")
                st.POST_flg = 0
            }

            phase = P_NEXT_CMND
            continue@outer
        }

        // #++++++++++ Argumented DO -> another_do
        phase = P_ANOTHER_DO
        continue@outer
    }

    // ---------------- vendor dcl label (DO declare arm, 1404) ----------------
    P_DCL -> {
        var li = 0
        while (true) {
            if (st.cvarx > CompilerState.CVARMAX) { // too_many_vars
                st.ernbr = 91
                print("\n*** 91 Too many C variables")
                phase = P_ABRT1
                continue@outer
            }
            while (st.xd[st.xpx.toInt()] != ' ' && st.xd[st.xpx.toInt()] != NUL &&
                   st.xd[st.xpx.toInt()] != ',') {
                st.cvars[st.cvarx].name[li] = st.xd[st.xpx.toInt()]
                li++
                st.xpx++
            }
            st.cvars[st.cvarx].name[li] = NUL
            st.cvarx++
            if (st.xd[st.xpx.toInt()] == ',') {
                li = 0
                st.xpx++
                continue // goto dcl
            }
            break
        }
        phase = P_NEXT_CMND
        continue@outer
    }

    // ---------------- vendor another_do label (1472) ----------------
    P_ANOTHER_DO -> {
        st.out.write("\t_ArgCount=0;  //section D1000 \n")
        Sysout("\tsvPtr->Rargsx=0;\n")

        val iL = st.xpx.toInt()
        var jL = st.xpx.toInt()
        var kL = 0
        val ttt = st.xd.copyOf(2048) // memmove(ttt,xd,2048)

        while (true) { // scan through label for non-quoted colon
            // a non-quoted colon can't exist in the label field
            if (st.xd[jL] == NUL) break
            if (st.xd[jL] == ',' && kL == (kL / 2) * 2) break
            if (st.xd[jL] == ':' && kL == (kL / 2) * 2) break
            if (st.xd[jL] == '"') {
                if (st.xd[jL + 1] == '"') jL++
                else kL++
            }
            jL++
        }

        if (st.xd[jL] == ':' && kL == (kL / 2) * 2) {
            st.xpx = (jL + 1).toLong()
            st.t0px = 0
            st.t2 = 1
            st.ierr = parse_().toShort()
            if (st.ierr != 0.toShort()) { phase = P_PARSE_ERR; continue@outer }
            st.out.write("\tif (atoi( CSTR " + Extract2(st.pd1, st.sdlim.toInt()) +
                ")) { // postconditional \n")
            st.t0px = 0
            st.t2 = 0
            st.xpx = iL.toLong()
            System.arraycopy(ttt, 0, st.xd, 0, 2048) // memmove(xd,ttt,2048)
        } else st.out.write("\t{\n")

        if (st.xd[st.xpx.toInt()] == '@') { // indirection in DO argument

            val r = atTest(st.xd, st.pd1, st.pd1Last, st.lines, st.xpx)
            st.at_flg = r.rc.toShort()
            st.pd1Last = r.pd1Last

            if (st.at_flg == (-1).toShort()) return abrtx() // goto abrt_exit

            if (st.at_flg != 0.toShort()) {

                val tmp = StringBuilder()
                tmp.append("d ")
                atCopy(st.at_flg.toInt(), tmp)

                st.interpret(tmp.toString())

                st.xpx = st.at_flg.toLong()
                st.out.write("       } /* post conditional 1920*/\n")

                if (st.POST_flg != 0.toShort()) {
                    st.out.write("       } /* post conditional 1920*/\n")
                    st.POST_flg = 0
                }
                phase = P_NEXT_CMND
                continue@outer
            }
        } else if (st.xd[st.xpx.toInt()] == DOLLAR && isalnumC(st.xd[st.xpx.toInt() + 1])) {
            st.t0px = 0
            st.t2 = 1
            st.ierr = parse_().toShort()
            if (st.ierr != 0.toShort()) { phase = P_PARSE_ERR; continue@outer }
            st.out.write("\t} \n")
        } else {

            st.xd[st.xpx.toInt() - 2] = DOLLAR
            st.xd[st.xpx.toInt() - 1] = DOLLAR
            st.xpx = st.xpx - 2
            st.t2 = 1
            st.ierr = parse_().toShort()
            if (st.ierr != 0.toShort()) { phase = P_PARSE_ERR; continue@outer }
            st.t2 = 0
            st.out.write("\t} \n ")
        }

        if (st.xd[st.xpx.toInt()] == ':') { // bypass post-cond, look for non-quoted, non-parend comma
            var j2 = 0
            var k2 = 0
            var m = 0
            st.xpx++
            j2 = st.xpx.toInt()
            while (true) {
                if (st.xd[j2] == NUL) break
                if (st.xd[j2] == ',' && m == 0 && k2 == (k2 / 2) * 2) break
                if (st.xd[j2] == ' ' && m == 0 && k2 == (k2 / 2) * 2) break
                if (st.xd[j2] == '(' && k2 == (k2 / 2) * 2) m++
                if (st.xd[j2] == ')' && k2 == (k2 / 2) * 2) m--
                if (st.xd[j2] == '"') {
                    if (st.xd[j2 + 1] == '"') j2++
                    else k2++
                }
                j2++
            }
            if (st.xd[j2] == ',' && m == 0 && k2 == (k2 / 2) * 2) {
                st.xpx = (j2 + 1).toLong()
                phase = P_ANOTHER_DO
                continue@outer
            }
            st.xpx = j2.toLong()
        }

        if (st.xd[st.xpx.toInt()] == ',') {
            st.xpx++
            phase = P_ANOTHER_DO
            continue@outer
        }

        if (st.POST_flg != 0.toShort()) { // end of post-cond
            st.out.write("\t} /* post conditional 3001*/\n")
            st.POST_flg = 0
        }

        // h013: the vendor's unreachable while(1) parse_ tail after
        // the "goto next_cmnd" ladder (vendor 1626-1638) is dead -
        // not rendered.
        if (st.JOB_flg != 0.toShort()) {
            phase = P_JOB_RETURN
            continue@outer
        }

        phase = P_NEXT_CMND
        continue@outer
    }

    // ---------------- vendor job_return label (1129) ----------------
    P_JOB_RETURN -> {
        st.out.write("\t}")
        phase = P_NEXT_LINE
        continue@outer
    }

    // ---------------- vendor next_goto label (GOTO case, 1797) ----------------
    P_NEXT_GOTO -> {
        i = 0

        while (st.xd[st.xpx.toInt()] != NUL) {
            if (st.xd[st.xpx.toInt()] == ',' || st.xd[st.xpx.toInt()] == ' ' ||
                st.xd[st.xpx.toInt()] == NUL ||
                st.xd[st.xpx.toInt()] == '+' || st.xd[st.xpx.toInt()] == ':') break
            st.dosave[i] = st.xd[st.xpx.toInt()]
            i++
            st.xpx++
        }

        if (st.xd[st.xpx.toInt()] == ':') { // post conditionals
            st.xpx++
            st.t0px = 0
            st.t2 = 1
            st.ierr = parse_().toShort()
            if (st.ierr != 0.toShort()) { phase = P_PARSE_ERR; continue@outer }
            st.out.write("\tif (atoi((char *) " + Extract2(st.pd1, st.sdlim.toInt()) +
                ")) /* m1635 postconditional*/")
            st.t0px = 0
            st.t2 = 0
        }

        st.dosave[i] = NUL

        i = 0
        while (st.dosave[i] != NUL && st.dosave[i] != '^' && st.dosave[i] != '+') i++

        if (st.dosave[i] == '+' || st.dosave[i] == '(') { // lbl_error
            st.ernbr = 8
            print("\n*** Label or file not found")
            phase = P_ABRT1
            continue@outer
        }

        if (st.dosave[i] == NUL) { // simple goto
            if (isdigitC(st.dosave[0])) st.out.write("\tgoto _" + cstringAt(st.dosave) + ";\n")
            else st.out.write("\tgoto " + cstringAt(st.dosave) + ";\n")
            phase = P_GEXIT
            continue@outer
        }

        if (st.dosave[0] != '^') { // label appears first in a ^ ref
            st.dosave[i] = NUL
            strcpyC(st.tmp2, cstringAt(st.dosave))
            // strcpy(&dosave[1],&dosave[i+1]); copy fcn name backwards
            val rest = CharArray(64)
            var rx = i + 1
            var wx = 1
            while (st.dosave[rx] != NUL) {
                rest[wx] = st.dosave[rx]
                rx++
                wx++
            }
            System.arraycopy(rest, 0, st.dosave, 0, wx)
            st.dosave[0] = '^'
        } else strcpyC(st.tmp2, "")

        st.out.write("\t_jj=Interpret(\"x \\\"goto ^" + cstringAt(st.dosave, 1) + "\\\"\",svPtr);\n")

        Sysout("\tgoto _epilogue; /* if fcn returns, this is end program */\n")

        phase = P_GEXIT
        continue@outer
    }

    // ---------------- vendor gexit label (1816) ----------------
    P_GEXIT -> {
        if (st.xd[st.xpx.toInt()] == ',') {
            st.xpx++
            phase = P_NEXT_GOTO
            continue@outer
        }

        if (st.POST_flg != 0.toShort()) {
            st.out.write("\t} /* post conditional 3431*/\n")
            st.POST_flg = 0
        }

        phase = P_NEXT_CMND
        continue@outer
    }

    // ---------------- vendor parse_err label (1879) ----------------
    P_PARSE_ERR -> {
        st.ernbr = st.ierr
        errmod_(st.ierr.toInt())
        if (st.ernbr == 9001.toShort()) print("*** Invalid global array reference")
        if (st.ernbr == 9002.toShort()) print("*** Invalid routine reference")
        if (st.ernbr == 9003.toShort()) print("*** Wrong number of args to \u0024extract()")
        if (st.ernbr == 12.toShort()) print("Invalid operator: " + st.ErrChar)
        phase = P_ABRT1
        continue@outer
    }

    // ---------------- the jump-targeted vendor error labels ----------------
    P_ARG_LIST_ERROR -> {
        st.ernbr = 14
        print("\n*** 14 Argument list error")
        phase = P_ABRT1
        continue@outer
    }

    P_NOGBL -> {
        st.ernbr = 92
        print("\n*** 92 Global not permitted")
        phase = P_ABRT1
        continue@outer
    }

    P_MEM_ERR -> {
        st.ernbr = 19
        print("\n*** 19 Out of memory")
        phase = P_ABRT1
        continue@outer
    }

    P_TOO_LHS -> {
        st.ernbr = 9033
        print("\n*** 9033 Too many variables in LHS")
        phase = P_ABRT1
        continue@outer
    }

    P_COMMAND -> {
        st.ernbr = 13
        print("\n*** 13 Command Error")
        phase = P_ABRT1
        continue@outer
    }

    // ---------------- vendor abrt1 (1919) ----------------
    P_ABRT1 -> {
        print(" in or near line " + st.lines + "\n")
        print("***\t" + cstringAt(st.pd1, st.pd1Last.toInt()) + " \n\n")
        st.ExitCode = st.ernbr
        return 9
    }
    }
    }
}
