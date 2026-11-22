package doc

// Unit 299 (B10 tranche 3): compiler-mumps.c::main (batch 10,
// complex stratum; units.csv row 279: loc 1372, the file-top
// banner unit) - THE MUMPS COMPILER DRIVER. The command line
// parse (the -v version probe, the -SUB option, the
// dot-extension strip of the program name), the output .m and
// input program opens, the copyright notice, the leading
// '+'-define copy block (the getc/ungetc pair), the include/
// defines/global-defs banner with the floatstring/numcomp2/
// strxcat/strcatz inline-function emits and the SyncClock/
// set_alarm emits, the compiler-start init block, and the
// ENTIRE next_line/next_cmnd/pstrtn/ctch/cntnue/dcl/do_entry/
// another_do/next_goto/gexit/job_return/abrt_exit goto web:
// the next_line label and function-ep scan (the ^^ and ^
// extrinsic entries, the subfunction _Argkk parameter blocks
// with the EP1001-EP1005 emissions, the LabelsStart linked
// list), the doty indent/un-indent ladder, the command-word
// ctable spell check, the ':' postconditional (atTest/atCopy
// INTERPRET arm and the parse_ atoi arm), the full command
// dispatch switch (CLSQ/OPSQ/TRY with the NoMessages probe/
// CMMNT/FCN/NEW/BREAK/LOCK/READ/OPEN/ZCMDS/IF/WRITE/JOB/
// MERGE-into-SET with the shell 'H' and sql 'Q' INTERPRETs/
// QUIT with the Rargs call-by-reference epilogue/XECUTE with
// the quote escaping/FOR/HALT with the HTML &~ and &! arms
// and the HANG loop/ELSE with the export 'x' probe/DO with
// the shell 'A' and declare 'e' arms, the argumentless DO
// setjmp block, the another_do label colon scan with the
// '@' indirection, the \u0024\u0024 inject and the postcond
// ',' recursion/KILL/CLOSE with the 'o' continue and 'a'
// catch jumps/USE/GOTO with the dosave label split and the
// Interpret goto ^fcn emit), and the gexit/next_goto loop.
// The 25 error labels (kill_dcl_err through goto_in_block and
// the abrt1 tail, abrt_exit = the abrtx return) ride the web
// as state hops. Derived from the pristine vendor source this
// session (units.csv row 279; the dump phys-verified above
// the render).
//
// GOTO WEB FOLD: private ctx class per the ParseCPCtx/
// CompilerParseMain.kt precedent - every vendor label is an
// enum state with a label function, the master dispatch loop
// hops states exactly as the vendor gotos do; the C returns
// (99/9/abrtx()) surface as the ctx returnCode; DONE ends
// the loop. Consult H-014 hit-adapt (the long arms bind the
// landed handler renders - ifCode/set_code/kill_code/
// lock_code/read_code/write_code/for_code/new_code/
// mumps_open/zcommands_code - plus the Stubs.parse_/
// atTest/atCopy/Extract2 seats and the EP/fcninit/
// linepad/copyrightNotice/doty/abrtx/errmod_ owners;
// fold ledgered per arm), timestamp 2026-11-22T06:28:00Z.
//
// SEATS (all cited): CompilerState (Out/emit/interpret, pd1/
// xd/tmp2/cur_line/dosave, xpx + xpxIdx(), ExitCode, ernbr,
// pd1cur/pd1len/pd1Last, FunctionName, output_file, pid,
// LabelsStart/TextStart/p1 TxtEntry, cvars/cvarx, ForBlocks/
// Blocks/FBMAX, ExecLevel/ExecName/ExecX/ExecStack/ExecSym/
// DoRet/DoRetX, EmitLabel, MAIN_flg/FCN_flg/F_flg/SubFlag/
// TryFlg/CatchFlg/MergeFlg/JOB_flg/POST_flg/IF_flg/
// IF_flg_max, forx/forxFLG/forxmax/forLine, retz, ArgMax,
// symlen, sdlim, t0px/t2, at_flg, ierr, lines, the except[]
// carrier - this unit ALSO table-fills except[0]/except[1]
// from the compiler-globals.h:174 static initializer, the
// InputLengthException/InterpreterException/\u0024-me table);
// PS the Carriers const; NUL/TAB the Carriers/Getstr2 consts;
// the command chars BREAK/CMMNT/DO/ELSE/FOR/GOTO/HALT/IF/
// JOB/MERGE/NEW/QUIT/READ/SET/USE/WRITE/XECUTE/ZCMDS are the
// InterpH.kt landed set (compiler-macros.h:91-113 family;
// OPEN 'o' rides the OPEN const of the same file); this
// unit's CLSQ/OPSQ/TRY/LOCK/CLOSE/FBMAX/FCN macros are
// file-private with the _CM suffix (compiler-macros.h:109-
// 124 per the CompilerMacrosH.kt ledger; the FCN 'D' macro
// quirk: the case is DEAD anyway - icmnd is lowercased
// before the switch and 'D' never matches; kept verbatim in
// the ledger); the return codes PARSE_ERROR 1 through
// BAD_LOCK 9 (compiler-macros.h:69-77) are file-private
// with the _CM suffix per the KillCode _KC/MumpsOpen _MO
// precedent.
//
// STREAM ADAPTERS (H-001/H-019): stdout printf renders as
// System.out.print (the svPtr banner precedent); FILE *Out
// is the CompilerState.Out Writer (assigned here by the
// output-file open); FILE *in is a private BufferedReader
// with a one-char pushback for the getc/ungetc pair; fgets
// drops its newline byte on read - the vendor <cr>/<lf>
// strip pair is a self-assign NO-OP anyway (pd1[strlen] is
// the NUL byte, always < 32, assigned to itself; the two
// vendor lines ledger here as dead stores H-013, only the
// comment marks remain); the fopen probes render as
// try/catch with the same printf tails; strcmp folds to
// String equals; isalpha/isalnum/isdigit fold to the
// Character class probes on the ASCII domain.
//
// DEAD TEXT LEDGER (H-013, cited not rendered): the
// if (!MAIN_flg && 0) prologue-emission block after the
// comment arm (the main-routine banner cook and the cgi.h
// emission are dead by the 0 constant); the if (0) zmain
// block after pstrtn's arrow (same shape); the unreachable
// while (1) parse tail of another_do (every path above it
// jumps away; the last four lines of the argumented-DO arm)
// ; the error labels indirect_err 72, do_error 8, quit_err
// 99, Ext_file 8, go_err 99 and ref_err 18 are dead in this
// chunk (no goto reaches them from the main body - their
// printf texts ledger in the errLedger list below); the
// int i, j dead locals of the ':' postconditional block;
// the vendor TryFlg=1 self-store in the line-end close
// (TryFlg is only ever 0/1; kept verbatim); the vendor
// ctable double-"sql" row and the ";" spell-check quirk
// ride CTABLE_CM verbatim.

private const val CLSQ_CM: Char = '}' // compiler-macros.h:119
private const val OPSQ_CM: Char = '{' // compiler-macros.h:120
private const val TRY_CM: Char = 't' // compiler-macros.h:121
private const val LOCK_CM: Char = 'l' // compiler-macros.h:112
private const val CLOSE_CM: Char = 'c' // compiler-macros.h:109
private const val OPEN_CM: Char = 'o' // compiler-macros.h:98, the
// OPEN command char (no InterpH seat; per-unit consumer rule)
private const val KILL_CM: Char = 'k' // compiler-macros.h:108, the
// KILL command char (no InterpH seat; per-unit consumer rule)
private const val FBMAX_CM = 4000 // compiler-macros.h:124
private const val FCN_CM: Char = 'D' // compiler-macros.h:117,
// the dead 'D' case - icmnd is lowercased before the switch

private const val PARSE_ERROR_CM = 1 // compiler-macros.h:69
private const val ARG_LIST_ERROR_CM = 2 // compiler-macros.h:70
private const val NO_GLOBAL_PERMITTED_CM = 3 // compiler-macros.h:71
private const val MEM_ERROR_CM = 4 // compiler-macros.h:72
private const val TOO_MANY_LHS_CM = 5 // compiler-macros.h:73
private const val KILL_DCL_ERROR_CM = 7 // compiler-macros.h:75
private const val NEXT_LINE_CM = 8 // compiler-macros.h:76
private const val BAD_LOCK_CM = 9 // compiler-macros.h:77

// static char *ctable[] - the command-word spell-check table
// (the vendor double-"sql" rows and the ";" probe kept
// verbatim).
private val CTABLE_CM = arrayOf(
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

// char *except[] of compiler-globals.h:174 - the catch
// spelling table; this consumer copies the static
// initializer values into the CompilerState.except carrier
// (the header initializes the table, so the table-fill seat
// rides this render).
private val EXCEPT_CM = arrayOf(
    "InputLengthException",
    "InterpreterException"
)

// the label states of the goto web.
private enum class CmL {
    START, NEXT_LINE, NEXT_CMND, PSTRTN, CTCH, CNTNUE,
    DCL, DO_ENTRY, ANOTHER_DO, NEXT_GOTO, GEXIT, JOB_RETURN,
    NEXT_LINE_DIRECT, ABRT_EXIT, DONE
}

private class MumpsCtx(private val args: Array<String>) {

    val cs = CompilerState
    val pd1 = cs.pd1
    val xd = cs.xd
    val tmp2 = cs.tmp2

    var ins: java.io.BufferedReader? = null
    private var pushback: Int = -2 // -2 = empty pushback slot

    var returnCode: Int? = null

    fun emit(t: String): Int = cs.emit(t)
    fun Sysout(x: String) { cs.emit(x) } // compiler-macros.h:44

    // the bound-guarded xd read (the vendor walks xd[2048]
    // freely; the guards read NUL past the end, the UB fold
    // of the SetCode precedent).
    fun xdg(i: Int): Char = if (i in xd.indices) xd[i] else NUL

    fun getcIn(): Int {
        if (pushback != -2) {
            val c = pushback
            pushback = -2
            return c
        }
        return ins?.read() ?: -1
    }

    fun ungetcIn(c: Int) { pushback = c }

    // fgets(&pd1[1], 512, in) - the readLine adapter drops the
    // newline byte (the vendor <cr>/<lf> strip pair is the
    // self-assign no-op, ledgered in the banner).
    fun fgetsPd1(): Boolean {
        val line = ins?.readLine() ?: return false
        val cap = if (line.length > 511) line.substring(0, 511) else line
        CstringOps.copyAtS(pd1, 1, cap)
        return true
    }

    // ------------------------------------------------------------------
    // the shared error tails
    // ------------------------------------------------------------------

    // abrt1: the shared error close (every ernbr label funnels
    // here; ExitCode = ernbr, return 9).
    fun abrt1(): CmL {
        System.out.print(" in or near line " + cs.lines + "\n")
        System.out.print("***\t" +
                CstringOps.cstringAt(pd1, cs.pd1Last[0].toInt()) + " \n\n")
        cs.ExitCode = cs.ernbr
        returnCode = 9
        return CmL.DONE
    }

    // an error label: set ernbr, print, close through abrt1.
    fun errLbl(msg: Int, text: String): CmL {
        cs.ernbr = msg
        System.out.print(text)
        return abrt1()
    }

    // the handler return-code ladder (the new_code/lock_code/
    // read_code/if_code/write_code/set_code/for_code/
    // kill_code/zcommands_code/mumps_open tails; the arms
    // select the deltas).
    fun handlerTail(rc: Int, badLock: Boolean = false,
                     zcmds: Boolean = false, killDcl: Boolean = false,
                     openArm: Boolean = false): CmL {
        if (rc == 0) return CmL.NEXT_CMND
        if (rc == PARSE_ERROR_CM) return parseErr()
        if (rc == ARG_LIST_ERROR_CM)
            return errLbl(14, "\n*** 14 Argument list error")
        if (openArm) // the mumps_open truncated ladder
            return errLbl(13, "\n*** 13 Command Error")
        if (rc == NO_GLOBAL_PERMITTED_CM)
            return errLbl(92, "\n*** 92 Global not permitted")
        if (rc == MEM_ERROR_CM)
            return errLbl(19, "\n*** 19 Out of memory")
        if (rc == TOO_MANY_LHS_CM)
            return errLbl(9033, "\n*** 9033 Too many variables in LHS")
        if (killDcl && rc == KILL_DCL_ERROR_CM)
            return errLbl(99, "\n*** Attempt to Kill declared variable\n")
        if (rc == 6) { // ABRTX
            returnCode = abrtx()
            return CmL.DONE
        }
        if (badLock && rc == BAD_LOCK_CM)
            return errLbl(99, "\n*** Lock error")
        if (zcmds && rc == NEXT_LINE_CM) return CmL.NEXT_LINE
        return errLbl(13, "\n*** 13 Command Error")
    }

    // the parse_err label.
    fun parseErr(): CmL {
        cs.ernbr = cs.ierr
        errmod_(cs.ierr)
        if (cs.ernbr == 9001)
            System.out.print("*** Invalid global array reference")
        if (cs.ernbr == 9002)
            System.out.print("*** Invalid routine reference")
        if (cs.ernbr == 9003)
            System.out.print("*** Wrong number of args to \u0024extract()")
        if (cs.ernbr == 12)
            System.out.print("Invalid operator: " + Globals.svPtr.ErrChar)
        return abrt1()
    }

    // ------------------------------------------------------------------
    // start: the command line, the opens, the banner, the init
    // ------------------------------------------------------------------

    fun start(): CmL {
        cs.symlen = PS
        cs.TextStart = null
        CstringOps.copyAtS(cs.FunctionName, 0, "")
        cs.pid = ProcessHandle.current().pid()
        cs.except[0] = EXCEPT_CM[0] // the compiler-globals.h:174
        cs.except[1] = EXCEPT_CM[1] // static initializer table-fill

        System.out.print("The Mumps Compiler\n")

        // argc == 2 && argv[1] == "-v" (args exclude the argv[0]
        // program name)
        if (args.size == 1 && args[0] == "-v") {
            returnCode = 99
            return CmL.DONE
        }

        if (args.size == 0) { // argc == 1
            System.out.print(
                "*** \\a Name of program to compile must be given\n\n")
            cs.ExitCode = 99
            returnCode = 99
            return CmL.DONE
        }

        if (args.size == 2) { // argc == 3: the -SUB option
            if (args[0] == "-SUB") {
                cs.SubFlag = 1
            } else {
                System.out.print("*** Unrecognized option: " + args[0] + "\n\n")
                cs.ExitCode = 99
                returnCode = 99
                return CmL.DONE
            }
        }

        // strip the extension off the program file name; tmp2
        // holds the output .m name. The vendor scans DOWN from
        // the NUL terminator (strlen index), the last-dot probe.
        val prog = args[args.size - 1]
        var dot = -1
        var ix = prog.length
        while (ix >= 0) {
            val c = if (ix < prog.length) prog[ix] else NUL
            if (c == '.') {
                dot = ix
                break
            }
            ix--
        }

        if (dot < 0) {
            // vendor strcpy(tmp2, argv[1]) - kept verbatim, the
            // odd argv[1] (not argv[argc-1]) no-dot source
            CstringOps.copyAtS(tmp2, 0, args[0])
        } else {
            CstringOps.copyAtS(tmp2, 0, prog.substring(0, dot))
        }
        CstringOps.copyAtS(tmp2, 0, CstringOps.cstring(tmp2) + ".m")

        // Out = fopen(tmp2, "w")
        cs.Out = java.io.PrintWriter(
            java.io.FileWriter(java.io.File(CstringOps.cstring(tmp2))))

        cs.output_file = CstringOps.cstring(tmp2)

        // in = fopen(argv[argc-1], "r")
        ins = try {
            java.io.BufferedReader(java.io.FileReader(prog))
        } catch (e: java.io.FileNotFoundException) {
            System.out.print("*** \\a File " + prog + " not found.\n\n")
            cs.ExitCode = 99
            returnCode = 99
            return CmL.DONE
        }

        System.out.print("Translating " + prog + " to C++:\n")

        copyrightNotice(cs.Out)

        // leading '+'-define symbols are copied first (the
        // getc/ungetc pair over the '+' check; fgets pulls the
        // rest of the line).
        while (true) {
            val i = getcIn()
            if (i != '+'.code) {
                ungetcIn(i)
                break
            }
            val str = ins?.readLine()
            if (str == null) {
                System.out.print("Unexpected end of file\n")
                cs.ExitCode = 99
                returnCode = abrtx()
                return CmL.DONE
            }
            emit(str + "\n")
        }

        // the include/defines/global-defs banner
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
        Sysout("#define FORPARM STR_MAX  /* Size for For loop parameter */\n")
        Sysout("#define CSTR (char *)\n")
        Sysout("#define UNS unsigned char *\n")
        Sysout("#define uns unsigned char *\n")
        Sysout("#define CCHR const char *\n")
        Sysout("#include <mumpsc/keyfix.h>\n")
        Sysout("#include <mumpsc/globalOrder.h>\n")

        if (cs.SubFlag != 0) Sysout("#define SUB\n")

        Sysout("#include <mumpsc/stateVector.h>\n")
        Sysout("int (*__label_lookup)(char *);\n")
        Sysout("const char * (*__text_function)(int);\n")

        Sysout("void fcn(MSV *);\n")
        emit("void zfcn(unsigned char *, unsigned char *, struct MSV *);" +
                " /* z functions */\n")

        emit("char * sym_(int, unsigned char *, unsigned char *, " +
                "struct MSV *); /* symbol table */\n")

        emit("char * makeLock(char *, char *, char *, char, char *);" +
                " /* global array lock service */\n")
        emit("char * cleanLocks(char *); " +
                "/* global array lock service */\n")
        emit("char * releaseLock(char *, char *, char *); " +
                "/* global array lock service */\n")
        emit("char * eraseLocks(char *); " +
                "/* global array lock service */\n")
        emit("void add(const char *a,const char *b,char *c);" +
                " /* math function */\n")
        emit("void sub(const char *a,const char *b,char *c);" +
                " /* math function */\n")
        emit("void mult(const char *a,const char *b,char *c);" +
                " /* math function */\n")
        emit("void divx(const char *a,const char *b,char *c);" +
                " /* math function */\n")
        emit("void divi(const char *a,const char *b,char *c);" +
                " /* math function */\n")
        emit("void _fnumber(unsigned char *,unsigned char *,\n")
        emit("       unsigned char *,unsigned char *, MSV *);" +
                " /* math function */\n")
        emit("int DBSem = 0; /* deadlock prevention */\n")
        emit("int pm (unsigned char [], const unsigned char pattern[]," +
                " struct MSV * svPtr); /* pattern match */\n")

        Sysout("\nstatic inline int floatstring(const char * p) { \n" +
                " int f=0;  \n" +
                " for (; *p!=0; p++) {  \n" +
                "  if (    isdigit(*p) || \n" +
                "  \t*p == '-' ||  \n" +
                "\t\t*p == '+' ) continue;  \n" +
                "  if (    *p=='.' ||  \n" +
                "\t\ttolower(*p)=='e' ) { \n" +
                "\t\tf=1;  \n" +
                "\t\tcontinue;  \n" +
                "\t\t} \n" +
                "  return 0; \n" +
                "  }  \n" +
                " return f;  \n" +
                " } \n ")

        Sysout("\ninline int numcomp2(unsigned char *aa) { \n" +
                " if (floatstring((char *) aa)) return " +
                "(atof( (char *) aa) != 0.0);  \n" +
                " return atoi((char *) aa);  \n" +
                " }\n")

        Sysout("\ninline char * strxcat(char * a, char *b)  \n" +
                "{ while (*a) a++; while( (*(a) = *(b)) ) { a++; b++; }" +
                " return a; } \n")

        Sysout("\ninline char * strcatz(char * out, char *in)  \n" +
                "{ while (*out) out++; while (*(out) = *(in)) { out++; in++; }" +
                " *(out++) = '\u0001';  \n" +
                "*out = 0; return (char *) out; } \n\n")

        emit("void SyncClock() {\n")
        emit("    DBSem = 1;\n")
        emit("    }\n\n")

        emit("unsigned int set_alarm (unsigned int seconds) {\n")
        emit("    struct itimerval old, xnew;\n")
        emit("    xnew.it_interval.tv_usec = 0;\n")
        emit("    xnew.it_interval.tv_sec = 0 ; // interval between alarms\n")
        emit("    xnew.it_value.tv_usec = 0;\n")
        emit("    xnew.it_value.tv_sec = (long int) 0;\n")
        emit("    if (setitimer (ITIMER_REAL, &xnew, &old) < 0)\n")
        emit("        return 0;\n")
        emit("    else\n")
        emit("        return old.it_value.tv_sec;\n")
        emit("    }\n\n")

        linepad(emit("#include <mumpsc/global.h>"),
            "Global array definitions")

        linepad(0, "Interface to Mglobal():")
        linepad(0, "g = command")
        linepad(0, "key = key sought or stored")
        linepad(0, "bd = value returned or stored")
        linepad(0, "svPtr = address of state vector")

        Sysout("#ifndef SUB\n")
        linepad(emit("#include <mumpsc/fcns.h>"),
            "System support functions")
        linepad(emit("#include <mumpsc/sysfunc.h>"),
            "Many common global variables")
        Sysout("#endif\n")

        // Compiler Start
        cs.pd1cur = 1
        cs.symlen = PS
        cs.t0px = 0
        cs.t2 = 0
        cs.cmdx = 0
        cs.setpiece = 0
        cs.forxmax = 0
        cs.forx = 0
        cs.forxFLG = 0
        cs.forLine = 0
        cs.pd1Last[0] = 0
        cs.ExecLevel = 0
        cs.SymIncrFlg = 0
        cs.ArgMax = 0
        cs.IF_flg_max = 0
        cs.cvarx = 0

        for (i in 0 until FBMAX_CM) cs.ForBlocks[i] = (-1).toShort()

        return CmL.NEXT_LINE
    }

    // the section banner rule emitter (the fprintf triple).
    fun banner3CM(t: String) {
        emit("\n//--------------\n")
        emit("//       " + t + "\n")
        emit("//--------------\n\n")
    }

    // ------------------------------------------------------------------
    // next_line
    // ------------------------------------------------------------------

    fun nextLine(): CmL {
        cs.EmitLabel = 0
        cs.iiitmp = 0 // new temp pool for each line
        cs.forLine = 0
        cs.CatchFlg = 0

        CstringOps.copyAtS(pd1, 1, "")  // content begins at 1, not 0

        if (!fgetsPd1()) { // input line
            cs.pd1cur = 9999
            cs.pd1len = 1
            CstringOps.copyAtS(pd1, 1, "")
        } else {
            cs.pd1cur = 1 // start
            cs.pd1len = 2048
        }

        // the vendor <cr>/<lf> strip pair is the self-assign
        // no-op (H-013, ledgered in the banner); the readLine
        // adapter already dropped the newline byte.

        var i = 1
        while (pd1[i] != NUL && i < 512) { // tabs to blanks
            if (pd1[i] == TAB) pd1[i] = ' '
            i++
        }

        cs.lines++

        while (cs.IF_flg > 0) { // closing braces
            emit("\t}\n")
            cs.IF_flg--
        }

        if (cs.pd1cur > cs.pd1len) { // end of file
            if (cs.ExecLevel > 0) {
                while (cs.ExecLevel > 0) {
                    emit("\t} while (0);   // dotted un-indent " +
                            (cs.ExecX - 1) + "\n")
                    cs.ExecX--
                    emit("\tlongjmp(Efor" + cs.ExecStack[cs.ExecX] +
                            "J,99);    // return \n")
                    emit("\nDo_Block_Bypass" + cs.ExecStack[cs.ExecX] +
                            ": ; //A541 \n")
                    cs.ExecLevel--
                }
                cs.ExecLevel = 0
            }
            return CmL.ABRT_EXIT // goto abrt_exit
        }

        if (pd1[cs.pd1cur] == NUL) {
            cs.pd1cur++
            return CmL.NEXT_LINE
        }

        CstringOps.copyAtS(cs.cur_line, 0,
            CstringOps.cstringAt(pd1, cs.pd1cur))

        // inline C code - emit as is
        if (pd1[cs.pd1cur] == '%' || pd1[cs.pd1cur] == '+') {
            emit("\n//--------------\n")
            emit("//       Inline\n")
            emit("//--------------\n\n")
            emit(CstringOps.cstringAt(pd1, cs.pd1cur + 1) + "\n")
            while (pd1[cs.pd1cur] != NUL) cs.pd1cur++
            cs.pd1cur++
            return CmL.NEXT_LINE
        }

        if (pd1[cs.pd1cur] == '#' ||
            (pd1[cs.pd1cur] == '/' && pd1[cs.pd1cur + 1] == '/')
        ) { // comment
            if (pd1[cs.pd1cur] == '/') cs.pd1cur++
            emit("\n// " + CstringOps.cstringAt(pd1, cs.pd1cur + 1) + "\n")
            cs.pd1cur++
            while (pd1[cs.pd1cur] != NUL) cs.pd1cur++
            cs.pd1cur++

            // the !MAIN_flg && 0 prologue block is dead vendor
            // text (H-013, ledgered in the banner)
            return CmL.NEXT_LINE
        }

        i = cs.pd1cur
        while (pd1[cs.pd1cur] != NUL && pd1[cs.pd1cur] != TAB) cs.pd1cur++
        if (pd1[cs.pd1cur] != NUL) {
            while (pd1[cs.pd1cur] != NUL && pd1[cs.pd1cur] == TAB) {
                cs.pd1cur++
            }
        }

        // multiple TABs
        cs.pd1cur = i

        if (pd1[cs.pd1cur] != TAB) {
            while (pd1[cs.pd1cur] != TAB && pd1[cs.pd1cur] != NUL) {
                if (pd1[cs.pd1cur] == '(') {
                    while (pd1[cs.pd1cur] != ')' &&
                        pd1[cs.pd1cur] != NUL) cs.pd1cur++
                }
                cs.pd1cur++
            }
            pd1[cs.pd1cur] = NUL

            // the function/label scan block
            var flg = 1
            var ii = i
            var kk = 0
            var n = 0
            var thistime = 0

            if (pd1[i] == '^' && pd1[i + 1] == '^') { // function start
                i += 2
                cs.F_flg = 1
                if (cs.MAIN_flg != 0) EP(0, cs.ExecName, cs.pid)
                if (cs.FCN_flg != 0) EP(1, cs.ExecName, cs.pid)
                cs.FCN_flg = 1 // am in subfunction
                thistime = 1
            } else if (pd1[i] == '^' && pd1[i + 1] != '^') {
                i += 1
                cs.F_flg = 1
                if (cs.MAIN_flg != 0) EP(0, cs.ExecName, cs.pid)
                if (cs.FCN_flg != 0) EP(1, cs.ExecName, cs.pid)
                cs.FCN_flg = 1
                thistime = 1
            }

            ii = i
            iiLoop@ while (pd1[i] != NUL) {

                // open paren in label - must be function ep
                if (pd1[i] == '(') {

                    if (flg != 0) {
                        Sysout("\n//=====================================" +
                                "=======================================\n")
                        emit("//\tEntry Point " +
                                CstringOps.cstringAt(pd1, ii) +
                                " + + + + + + + \n")
                        Sysout("\n//=====================================" +
                                "=======================================\n")
                        flg = 0 // arguments found
                    }

                    pd1[i] = NUL

                    if (cs.F_flg == 0 && cs.MAIN_flg == 0) {
                        // Missing_Zmain
                        return errLbl(92, "\n*** 92 Missing zmain")
                    }

                    if (cs.F_flg != 0 && thistime != 0) {
                        // this is a C subfunction
                        var ff = 0
                        cs.cvarx = 0
                        CstringOps.copyAtS(cs.FunctionName, 0,
                            CstringOps.cstringAt(pd1, ii))

                        emit("char * " + CstringOps.cstringAt(pd1, ii) +
                                "(struct MSV * svPtr, char * offset")

                        if (pd1[i + 1] != ')') emit(",")

                        // separately compiled extrinsic; the fe1
                        // label executes at the tail of every
                        // non-continue iteration (the goto fe1
                        // fold)
                        var j = i + 1
                        feLoop@ while (pd1[j] != NUL) {
                            if (pd1[j] == '+') { // non mumpsc parameter
                                j++
                                while (true) {
                                    emit(pd1[j].toString())
                                    j++
                                    if (pd1[j] == ',' || pd1[j] == ')') break
                                }
                                if (pd1[j] == ')') {
                                    ff = 0 // goto fe1 with ff cleared
                                } else {
                                    emit(",")
                                    j++
                                    continue@feLoop // skips fe1
                                }
                            } else {
                                if (Character.isLetter(pd1[j])) ff = 1
                                if ((pd1[j] == ',' || pd1[j] == ')') &&
                                    ff != 0
                                ) {
                                    emit("const char * _Arg" + kk +
                                            pd1[j] + "")
                                    kk++
                                }
                            }
                            // fe1:
                            if (pd1[j] == ')') {
                                if (ff == 0) emit(") ")
                                emit("{\n\n")
                                fcninit(cs.output_file, cs.pid)
                                break@feLoop
                            }
                            j++
                        }

                        emit("__label_lookup=" +
                                CstringOps.cstring(cs.FunctionName) +
                                "_label_lookup; \n")
                        emit("__text_function=" +
                                CstringOps.cstring(cs.FunctionName) +
                                "_text_function; \n\n")

                        emit("try { // try001 \n ")

                        Sysout("svPtr->_SymPush++; \n")
                        Sysout("_SymPush++; \n")
                        Sysout("svPtr->start[svPtr->_Sym]=NULL; \n")
                        cs.SymIncrFlg = 1
                        emit("if (svPtr->_Sym > SYM_MAX) ")
                        emit("ErrorMessage(\"Too many function calls\"," +
                                "svPtr->LineNumber); \n")
                        emit("_doStackN[_doStx-1] = 1;" +
                                " // flag to restore namespace\n")
                        Sysout("\tif (svPtr->Rargsx) { // EP1005\n")
                        Sysout("\t\tfor (_i = 0; _i < svPtr->Rargsx; _i++) " +
                                "strcpy(_Refs[_i], svPtr->Rargs[_i]); \n")
                        Sysout("\t\t_Refsx = svPtr->Rargsx; \n")
                        Sysout("\t\t} \n")
                    } else {
                        if (!Character.isLetterOrDigit(pd1[ii])) {
                            // lab_err
                            return errLbl(22,
                                "\n*** Labels must begin with and " +
                                "alphabetic character")
                        }
                        if (pd1[ii] in '0'..'9') emit("_")
                        emit(CstringOps.cstringAt(pd1, ii) + ": \n")
                        Sysout("svPtr->start[svPtr->_Sym] = NULL; \n")
                        cs.SymIncrFlg = 1
                        Sysout("_SymPush++; \n")
                        emit("if (svPtr->_Sym > SYM_MAX) ")
                        emit("ErrorMessage(\"Too many function calls\"," +
                                "svPtr->LineNumber); \n")
                        emit("_doStackN[_doStx-1]=1;" +
                                " // flag to restore namespace \n")
                    }

                    pd1[i] = '('
                    var j = i + 1
                    while (pd1[j] != NUL) {
                        if (pd1[j] == '+') {
                            while (true) {
                                if (pd1[j] == ',' || pd1[j] == ')') break
                                j++
                            }
                            if (pd1[j] == ')') {
                                j++
                                break
                            }
                            i = j
                            continue
                        }
                        if (pd1[j] == ',' || pd1[j] == ')') {
                            val k = pd1[j]
                            pd1[j] = NUL
                            if (CstringOps.cstringAt(pd1, i + 1).isEmpty()) {
                                break
                            }

                            if (cs.F_flg != 0) {
                                emit("\tif (_Arg" + n + "!=NULL) \n\t\t" +
                                        "sym_(100, (UNS) \"" +
                                        CstringOps.cstringAt(pd1, i + 1) +
                                        "\", (UNS) _Arg" + n + ", svPtr);" +
                                        " // Store Arguments EP1001\n")
                                emit("\tstrcpy (svPtr->RefParm[" + n +
                                        "], \"" +
                                        CstringOps.cstringAt(pd1, i + 1) +
                                        "\" ); // EP1002\n")
                            } else {
                                emit("\tif (__Arg" + n + "!=NULL) " +
                                        "sym_(100, (UNS) \"" +
                                        CstringOps.cstringAt(pd1, i + 1) +
                                        "\", (UNS) __Arg" + n +
                                        ",svPtr); // Store Arguments " +
                                        "EP1003 \n")
                                emit("\tstrcpy (svPtr->RefParm[" + n +
                                        "], \"" +
                                        CstringOps.cstringAt(pd1, i + 1) +
                                        "\" ); // EP1004 \n")
                            }
                            n++
                            if (n > cs.ArgMax) cs.ArgMax = n
                            pd1[j] = k
                            if (k == ')') break
                            i = j
                        }
                        j++
                    }
                    if (cs.F_flg != 0 && thistime != 0) {
                        emit("\tgoto _eps;  // entry point label router \n")
                        emit("_fcn: \n")
                    }
                    i = j
                    continue@iiLoop
                }
                i++
            }

            if (flg == 0 && cs.F_flg == 0) {
                emit("\tif (_ArgCount < " + n +
                        ") ErrorMessage(\"Bad argument count\", " +
                        "svPtr->LineNumber); \n")
            }

            if (flg != 0) {
                var i3 = ii
                while (pd1[i3] != NUL) {
                    if (pd1[i3] != ' ') { // label
                        cs.EmitLabel = 1

                        if (!Character.isLetterOrDigit(pd1[ii])) {
                            // lab_err
                            return errLbl(22,
                                "\n*** Labels must begin with and " +
                                "alphabetic character")
                        }

                        // the malloc probes fold to the GC (H-008)
                        if (cs.LabelsStart == null) {
                            // linked list setup
                            cs.LabelsStart = TxtEntry()
                            cs.LabelsStart!!.next = null
                            cs.LabelsStart!!.lnbr = cs.lines
                            cs.LabelsStart!!.name =
                                (if (pd1[ii] in '0'..'9') "_" else "") +
                                CstringOps.cstringAt(pd1, ii)
                        } else {
                            // existing linked list
                            val p1t = TxtEntry()
                            p1t.next = cs.LabelsStart
                            cs.LabelsStart = p1t
                            p1t.lnbr = cs.lines
                            cs.LabelsStart!!.name =
                                (if (pd1[ii] in '0'..'9') "_" else "") +
                                CstringOps.cstringAt(pd1, ii)
                        }
                        break
                    }
                    i3++
                }
            }

            pd1[cs.pd1cur] = TAB // replace
        }

        while (pd1[cs.pd1cur] == TAB) cs.pd1cur++

        // Doty Indents
        if (pd1[cs.pd1cur] == '.') {
            var j = 0
            var iv = cs.pd1cur
            while (pd1[iv] == '.' || pd1[iv] == ' ') {
                if (pd1[iv] == '.') j++ // how many levels?
                iv++
            }

            if (j > 10) {
                System.out.print("*** Too many block indents\n")
                return errLbl(99, "") // ernbr already 99
            }

            if (j > cs.ExecLevel + 1) { // skipped a level
                System.out.print("*** Code level skip\n")
                return errLbl(99, "")
            }

            // ExecLevel set to j in function
            val execLevelArr = shortArrayOf(cs.ExecLevel.toShort())
            val execNameArr = shortArrayOf(cs.ExecName.toShort())
            val execXArr = shortArrayOf(cs.ExecX.toShort())
            cs.ernbr = doty(j, execLevelArr, execNameArr, cs.DoRet,
                cs.DoRetX.toShort(), cs.pd1Last,
                cs.pd1cur.toShort(), cs.ExecStack, execXArr, cs.ExecSym)
            cs.ExecLevel = execLevelArr[0].toInt()
            cs.ExecName = execNameArr[0].toInt()
            cs.ExecX = execXArr[0].toInt()
            if (cs.ernbr != 0) return abrt1()

            cs.pd1cur = iv
        } else if (cs.ExecLevel > 0) {
            while (cs.ExecLevel > 0) {
                emit("\t} while(0); // dotted un-indent " +
                        (cs.ExecX - 1) + " \n")
                cs.ExecX--
                emit("\tlongjmp(Efor" + cs.ExecStack[cs.ExecX] +
                        "J,99); // return \n")
                emit("\nDo_Block_Bypass" + cs.ExecStack[cs.ExecX] +
                        ": ; //B848 \n\n")
                cs.ExecLevel--
            }
            cs.ExecLevel = 0
        }

        if (cs.ExecLevel == 0) cs.forxFLG = 0

        cs.xpx = 1L
        var j = 1

        // copy source line to xd
        cs.pd1Last[0] = cs.pd1cur.toShort()

        while (true) {
            val wasNul = pd1[cs.pd1cur] == NUL
            xd[j] = pd1[cs.pd1cur]
            j++
            cs.pd1cur++
            if (wasNul) break
        }

        Sysout("\n//=============================================" +
                "==================================== \n")

        if ((cs.MAIN_flg != 0 || cs.FCN_flg != 0) && cs.TryFlg == 0) {
            emit("svPtr->LineNumber=" + cs.lines + "; // " +
                    CstringOps.cstring(cs.cur_line) + " \n")
        } else {
            emit("// " + CstringOps.cstring(cs.cur_line) + " \n")
        }

        Sysout("//=============================================" +
                "==================================== \n")

        // write the label
        if (cs.EmitLabel != 0) {
            if (cs.ExecLevel > 0) { // label_in_block
                return errLbl(125, "\n*** 125 Label not allowed in block")
            }
            emit("\n" + (cs.LabelsStart!!.name ?: "") + ": \n")
            cs.EmitLabel = 0
        }

        xd[j] = NUL

        return CmL.NEXT_CMND
    }

    // ------------------------------------------------------------------
    // next_cmnd - the continue-line entry point (skip loop,
    // the // comment probe, the command-word decode with the
    // ctable spell check, the ':' postconditional - then
    // pstrtn)
    // ------------------------------------------------------------------

    fun nextCmnd(): CmL {
        if (cs.IF_flg > cs.IF_flg_max) cs.IF_flg_max = cs.IF_flg

        while (true) {
            if (cs.xpxIdx() >= 1024 ||
                xdg(cs.xpxIdx()) == NUL
            ) {
                // the order of these tests is important
                if (cs.IF_flg > 0) {
                    var i = cs.IF_flg
                    while (i > 0) {
                        emit("\t} ")
                        if (cs.ForBlocks[i] >= 0) {
                            emit("while (0); longjmp(for" +
                                    cs.ForBlocks[i] + "J, _i); " +
                                    "// FOR loop " + i + " close \n")
                            if (cs.Blocks[i] == FOR) {
                                emit("\nFor" + cs.ForBlocks[i] +
                                        "Exit: ; // exit for loop \n\n")
                            }
                            cs.ForBlocks[i] = -1
                        } else emit("\n")
                        i--
                    }
                    cs.IF_flg = 0
                }

                if (cs.TryFlg != 0) {
                    emit("\t} \n")
                    // the vendor TryFlg=1 self-store here is the
                    // dead idiom (TryFlg is only ever 0/1),
                    // kept verbatim (H-013)
                }

                if (cs.CatchFlg != 0) {
                    emit("\t} \n")
                    cs.CatchFlg = 0
                }

                return CmL.NEXT_LINE
            }

            if (xdg(cs.xpxIdx()) != ' ' && xdg(cs.xpxIdx()) != TAB) break

            cs.xpx++
        }

        // decode command word
        cs.p2px = cs.xpxIdx()
        cs.t2 = 0
        cs.t0px = 0
        cs.JOB_flg = 0
        cs.POST_flg = 0

        // c++ style comment found (the strncmp fold)
        if (xdg(cs.xpxIdx()) == '/' && xdg(cs.xpxIdx() + 1) == '/') {
            cs.xpx = 9999
            return CmL.NEXT_CMND
        }

        val icmndL = Character.toLowerCase(xdg(cs.xpxIdx())).code
        icmnd = icmndL // member: read by the pstrtn switch

        // check command word (the vendor static ctable; the
        // spell warning)
        run {
            val word = CharArray(25)
            var wx = 1
            var flg = 1
            cs.MergeFlg = 0
            word[0] = icmndL.toChar()

            cs.xpx++
            while (Character.isLetter(xdg(cs.xpxIdx())) &&
                wx < word.size
            ) {
                word[wx++] = Character.toLowerCase(
                    xdg(cs.xpxIdx()))
                cs.xpx++
            }

            word[wx] = NUL

            val wordS = CstringOps.cstring(word)
            for (t in CTABLE_CM) {
                if (t.isEmpty()) break
                if (wordS == t) {
                    flg = 0
                    break
                }
            }

            if (icmnd == ';'.code) return CmL.PSTRTN

            if (flg != 0) {
                System.out.print("*** Warning, possible command " +
                        "spelling error: " + wordS + " ")
                System.out.print("in or near line " + cs.lines + "\n")
                System.out.print("*** " +
                        CstringOps.cstringAt(pd1, cs.pd1Last[0].toInt()) +
                        "\n")
            }
        }

        if (xdg(cs.xpxIdx()) == ':') { // post conditionals
            cs.xpx++

            cs.at_flg = Stubs.atTest(xd, pd1, cs.pd1Last,
                cs.lines, cs.xpx)

            if (cs.at_flg == 99) return CmL.ABRT_EXIT

            if (cs.at_flg != 0) {
                val tmp = CharArray(1024)
                // the vendor int i, j locals here are dead
                // (H-013)
                CstringOps.copyAtS(tmp, 0, "i ")
                Stubs.atCopy(cs.at_flg, tmp)

                cs.interpret(CstringOps.cstring(tmp))

                cs.xpx = cs.at_flg.toLong()
                emit("\tif (svPtr->tpx) { // m853 postconditional \n")
                cs.POST_flg = 1
            } else {
                cs.ierr = Stubs.parse_()
                if (cs.ierr != 0) return parseErr()
                emit("\tif (atoi( CSTR " + CstringOps.cstring(
                    Stubs.Extract2(pd1, cs.sdlim)) +
                        ")) { // m859 postconditional\n")
                cs.POST_flg = 1
            }
        }

        // branch to correct command handler
        return CmL.PSTRTN
    }

    // ------------------------------------------------------------------
    // pstrtn - the command dispatch switch
    // ------------------------------------------------------------------

    fun pstrtn(): CmL {
        if (xdg(cs.xpxIdx()) != NUL) cs.xpx++

        // the if (0) zmain block after the arrow is dead vendor
        // text (H-013, ledgered in the banner)

        when {
        icmndIs(CLSQ_CM) -> {
            Sysout("\t} /* user close */\n")
            return CmL.NEXT_CMND
        }

        icmndIs(OPSQ_CM) -> return CmL.NEXT_CMND

        icmnd == TRY_CM.code -> {
            emit("try { ")
            cs.TryFlg = 1
            if (xdg(cs.xpxIdx()) == ' ' || xdg(cs.xpxIdx()) == NUL) {
                return CmL.NEXT_CMND
            }
            var i = cs.xpxIdx()
            while (xd[i] != ' ' && xd[i] != NUL) i++
            val j = xd[i]
            xd[i] = NUL
            if (CstringOps.cstringAt(xd, cs.xpxIdx()) == "NoMessages") {
                emit("svPtr->NOERR=1; ")
            } else {
                return errLbl(124, "\n*** 124 Unknown option in try")
            }
            xd[i] = j
            cs.xpx = i.toLong()
            return CmL.NEXT_CMND
        }

        icmndIs(CMMNT) -> { // the comment text build
            cs.p2px++
            if (xdg(cs.p2px) == ';') {
                // the malloc probes fold to the GC (H-008)
                if (cs.TextStart == null) {
                    val t = TxtEntry()
                    t.name = CstringOps.cstringAt(xd, cs.p2px + 1)
                    t.lnbr = cs.lines
                    t.next = null
                    cs.TextStart = t
                    return CmL.NEXT_LINE
                }
                val p1t = TxtEntry()
                p1t.name = CstringOps.cstringAt(xd, cs.p2px + 1)
                p1t.next = cs.TextStart
                p1t.lnbr = cs.lines
                cs.TextStart = p1t
                return CmL.NEXT_LINE
            }
            return CmL.NEXT_LINE
        }

        icmnd == FCN_CM.code -> {
            // the dead 'D' case (icmnd is lowercased before the
            // switch; the FCN macro value is 'D'); kept verbatim
            System.out.print("*** Not supported\n")
            System.out.print("*** " + CstringOps.cstring(cs.cur_line) + "\n")
            cs.ExitCode = 99
            returnCode = abrtx()
            return CmL.DONE
        }

        icmndIs(NEW) -> {
            banner3CM("New")
            return handlerTail(new_code())
        }

        icmndIs(BREAK) -> {
            banner3CM("Break")
            if (cs.forLine != 0) {
                return errLbl(99, "\n*** Break not permitted here")
            }
            emit("     svPtr->tpx=tpx" +
                    cs.ExecStack[cs.ExecX - 1] +
                    "; // break code: restore \u0024test\n" +
                    "  longjmp(Efor" + cs.ExecStack[cs.ExecX - 1] +
                    "J,100); \n")
            if (cs.POST_flg != 0) {
                emit(" } /* post conditional after break */\n")
                cs.POST_flg = 0
            }
            return CmL.NEXT_CMND
        }

        icmnd == LOCK_CM.code -> { // lock group
            banner3CM("Lock")
            return handlerTail(lock_code(), badLock = true)
        }

        icmndIs(READ) -> {
            banner3CM("Read")
            return handlerTail(read_code())
        }

        icmnd == OPEN_CM.code -> {
            banner3CM("Open")
            return handlerTail(mumps_open(), openArm = true)
        }

        icmndIs(ZCMDS) -> {
            return handlerTail(zcommands_code(), zcmds = true)
        }

        icmndIs(IF) -> {
            banner3CM("If")
            return handlerTail(ifCode())
        }

        icmndIs(WRITE) -> {
            banner3CM("Write")
            return handlerTail(write_code())
        }

        icmndIs(JOB) -> {
            banner3CM("Job")
            linepad(emit("\tif (fork()!=0) {"),
                "I am child process if zero")
            cs.JOB_flg = 1
            return CmL.DO_ENTRY
        }

        icmndIs(MERGE) -> {
            banner3CM("Merge")
            cs.MergeFlg = 1
            return setArm()
        }

        icmndIs(SET) -> return setArm()

        icmndIs(QUIT) -> return quitBody()

        icmndIs(XECUTE) -> {
            banner3CM("Xecute")
            val tmp = CharArray(1024)
            CstringOps.copyAtS(tmp, 0, "x ")
            var j = 2
            var i = cs.xpxIdx()
            while (xd[i] != NUL && j < tmp.size - 1) {
                if (xd[i] != '"') {
                    tmp[j++] = xd[i]
                } else {
                    tmp[j++] = '\\'
                    if (j < tmp.size - 1) tmp[j++] = xd[i]
                }
                i++
            }
            tmp[j] = NUL

            cs.interpret(CstringOps.cstring(tmp))

            if (cs.POST_flg != 0) {
                emit("\t} // post conditional xecute\n")
                cs.POST_flg = 0
            }
            cs.xpx = 1024
            return CmL.NEXT_CMND
        }

        icmndIs(FOR) -> {
            banner3CM("For")
            return handlerTail(for_code())
        }

        icmndIs(HALT) -> return haltArm()

        icmndIs(ELSE) -> {
            if (Character.toLowerCase(xdg(cs.p2px + 1)) == 'x') {
                cs.exportflag = 1
            }
            banner3CM("Else")
            if (xdg(cs.xpxIdx()) == ' ') cs.xpx++
            emit("\tif (svPtr->tpx == 0) { \n")
            cs.IF_flg++
            return CmL.NEXT_CMND
        }

        icmndIs(DO) -> { // declare
            if (Character.toUpperCase(xdg(cs.p2px + 1)) == 'A') {
                // SHELL command (the quote-escape INTERPRET)
                val tmp = CharArray(4096)
                var i = 0
                var pp = cs.p2px
                while (xdg(pp) != NUL && i < tmp.size - 1) {
                    if (xd[pp] == '"') {
                        tmp[i++] = '\\'
                    }
                    if (i < tmp.size - 1) tmp[i] = xd[pp]
                    i++
                    pp++
                }
                tmp[i] = NUL
                cs.interpret(CstringOps.cstring(tmp))
                return CmL.NEXT_LINE
            }

            if (Character.toLowerCase(xdg(cs.p2px + 1)) == 'e') {
                return CmL.DCL
            }

            return CmL.DO_ENTRY
        }

        icmnd == KILL_CM.code -> {
            banner3CM("Kill")
            return handlerTail(kill_code(), killDcl = true)
        }

        icmnd == CLOSE_CM.code -> {
            banner3CM("Close")
            cs.p2px++
            if (xdg(cs.p2px) <= 'Z') {
                if (cs.p2px in xd.indices) xd[cs.p2px] =
                    (xd[cs.p2px].code + 32).toChar()
            }
            if (xdg(cs.p2px) == 'o') return CmL.CNTNUE // continue
            if (xdg(cs.p2px) == 'a') return CmL.CTCH // catch

            while (true) {
                cs.t2 = 1
                cs.ierr = Stubs.parse_()
                if (cs.ierr != 0) return parseErr()
                emit("\t_i=atoi( CSTR " + CstringOps.cstring(
                    Stubs.Extract2(pd1, cs.sdlim)) + ");\n")
                emit("\tif (svPtr->in_file[_i] == NULL && " +
                        "svPtr->out_file[_i] == NULL) \n")
                emit("\t\tErrorMessage(\"Attempt to close unopened file\", " +
                        "svPtr->LineNumber); \n")
                emit("\tif (svPtr->in_file[_i]!=NULL) { \n" +
                        "\tfclose(svPtr->in_file[_i]); " +
                        "svPtr->in_file[_i]=NULL; }\n")
                emit("\telse { fclose(svPtr->out_file[_i]); " +
                        "svPtr->out_file[_i]=NULL; }\n")

                if (xdg(cs.xpxIdx()) != ',') {
                    if (cs.POST_flg != 0) {
                        emit("\t} /* post conditional 3240*/\n")
                        cs.POST_flg = 0
                    }
                    return CmL.NEXT_CMND
                }
                cs.xpx++
            }
        }

        icmndIs(USE) -> {
            banner3CM("Use")
            cs.t2 = 1
            cs.ierr = Stubs.parse_()
            if (cs.ierr != 0) return parseErr()
            emit("\tsvPtr->io=atoi( CSTR " + CstringOps.cstring(
                Stubs.Extract2(pd1, cs.sdlim)) + ");\n")
            if (cs.POST_flg != 0) {
                emit("\t} /* post conditional 3245*/\n")
                cs.POST_flg = 0
            }
            return CmL.NEXT_CMND
        }

        icmndIs(GOTO) -> {
            banner3CM("Goto")
            if (xdg(cs.xpxIdx()) == NUL) {
                return errLbl(8, "\n*** Label or file not found")
            }
            if (cs.ExecLevel > 0) {
                return errLbl(126, "\n*** 126 Goto not allowed in block")
            }
            return CmL.NEXT_GOTO
        }

        else -> { // command error
            return errLbl(13, "\n*** 13 Command Error")
        }
        }
    }

    // the icmnd holder of the pstrtn switch (set by nextCmnd)
    private var icmnd: Int = 0

    private fun icmndIs(c: Char): Boolean = icmnd == c.code

    // ------------------------------------------------------------------
    // the SET arm (also the MERGE fall-through; the shell 'H'
    // and sql 'Q' INTERPRETs fold here)
    // ------------------------------------------------------------------

    private fun setArm(): CmL {
        if (Character.toUpperCase(xdg(cs.p2px + 1)) == 'H') {
            // SHELL command
            banner3CM("Shell")
            val tmp = CstringOps.cstringAt(xd, cs.p2px)
            cs.interpret(tmp)
            return CmL.NEXT_LINE
        }

        if (Character.toUpperCase(xdg(cs.p2px + 1)) == 'Q') { // SQL
            val tmp = CstringOps.cstringAt(xd, cs.p2px)
            cs.interpret(tmp)
            return CmL.NEXT_LINE
        }

        banner3CM("Set")

        cs.retz = SET.code // SET 's', compiler-macros.h:91

        return handlerTail(set_code(SET.code))
    }

    // ------------------------------------------------------------------
    // the HALT arm (halt/html/hang)
    // ------------------------------------------------------------------

    private fun haltArm(): CmL {
        // the C precedence quirk kept verbatim: (T-probe and
        // space) or end-of-command selects the halt arm
        if ((Character.toUpperCase(xdg(cs.p2px + 1)) != 'T' &&
                xdg(cs.xpxIdx()) == ' ') || xdg(cs.xpxIdx()) == NUL) {
            banner3CM("Halt")
            if (cs.MAIN_flg != 0) emit("\tgoto _epilogue; \n")
            if (cs.FCN_flg != 0) emit("\treturn \"\"; \n")
            if (cs.POST_flg != 0) {
                emit("\t} /* post conditional halt*/\n")
                cs.POST_flg = 0
            }
            return CmL.NEXT_CMND
        }

        if (Character.toUpperCase(xdg(cs.p2px + 1)) == 'T') {
            // HTML command
            emit(" /* HTML */ printf(\"")
            while (xdg(cs.xpxIdx()) != NUL) {

                if (xdg(cs.xpxIdx()) == '&' &&
                    xdg(cs.xpxIdx() + 1) == '~'
                ) {
                    cs.xpx += 2
                    emit("\");\n")
                    cs.t2 = 1
                    var j = cs.xpxIdx()
                    while (xd[j] != '~' && xd[j] != NUL) j++
                    xd[j] = ' '
                    cs.ierr = Stubs.parse_()
                    if (cs.ierr != 0) return parseErr()
                    emit("\tprintf(\"%s\", " + CstringOps.cstring(
                        Stubs.Extract2(pd1, cs.sdlim)) + "); \n")
                    emit("\tprintf(\"")
                    cs.xpx++
                    continue
                }

                if (xdg(cs.xpxIdx()) == '&' &&
                    xdg(cs.xpxIdx() + 1) == '!'
                ) {
                    cs.xpx += 2
                    emit("\\n")
                    continue
                }

                if (xdg(cs.xpxIdx()) == '"') emit("\\\"")
                else if (xdg(cs.xpxIdx()) == '\'') emit("\\'")
                else if (xdg(cs.xpxIdx()) == '%') emit("%%")
                else if (xdg(cs.xpxIdx()) == '\\') emit("\\\\")
                else emit(xdg(cs.xpxIdx()).toString())
                cs.xpx++
            }

            emit("\");\n")
            return CmL.NEXT_LINE
        }

        while (true) { // Hang
            banner3CM("Hang")
            cs.t2 = 1
            cs.ierr = Stubs.parse_()
            if (cs.ierr != 0) return parseErr()
            emit("\t_i = atoi( CSTR " + CstringOps.cstring(
                Stubs.Extract2(pd1, cs.sdlim)) + ");\n")
            emit("\tif (_i > 0) sleep(_i);\n")

            if (xdg(cs.xpxIdx()) == ',') {
                cs.xpx++
                continue
            }

            if (cs.POST_flg != 0) {
                emit("\t} /* post conditional hang*/\n")
                cs.POST_flg = 0
            }

            return CmL.NEXT_CMND
        }
    }

    // ------------------------------------------------------------------
    // the QUIT body (also the cntnue label target of CLOSE 'o')
    // ------------------------------------------------------------------

    fun quitBody(): CmL {
        banner3CM("Quit")

        if (cs.POST_flg != 0) {
            if (cs.ForBlocks[cs.IF_flg] <= 0) {
                emit("\tbreak; } // post conditional quit m2386 \n")
            } else {
                emit("\tlongjmp(for" + cs.ForBlocks[cs.IF_flg] +
                        "J,100); /* m1146 quit*/ } // m1146 quit\n\n")
            }
            cs.POST_flg = 0
            return CmL.NEXT_CMND
        }

        if (cs.ExecLevel == 0 && cs.forxFLG == 0) {
            Sysout("\tif (svPtr->Rargsx) { // call-by-reference? \n")
            Sysout("\t\tint i; \n")
            Sysout("\t\tunsigned char *tmp = new unsigned char[STR_MAX]; \n")
            Sysout("\t\tfor (i = 0; i < svPtr->Rargsx; i++) { \n")
            Sysout("\t\tsym_(1, (UNS) svPtr->RefParm[i], (UNS) tmp,svPtr); \n")
            Sysout("\t\tsym_(SYMSTORE, (UNS) svPtr->Rargs[i], " +
                    "(UNS) tmp, svPtr); \n")
            Sysout("\t\t} \n")
            Sysout("\t\t} \n")

            emit("\tstrmove((UNS) _ReturnVal, (UNS) \"\"); \n")

            if (xdg(cs.xpxIdx()) != ' ' && xdg(cs.xpxIdx()) != NUL) {
                cs.t2 = 1
                cs.ierr = Stubs.parse_()
                if (cs.ierr != 0) return parseErr()
                emit("\tstrmove((UNS) _ReturnVal, (UNS) " +
                        CstringOps.cstring(
                            Stubs.Extract2(pd1, cs.sdlim)) + "); \n")
            }

            if (cs.F_flg != 0) {
                emit("\treturn (char *) _ReturnVal; \n")
            } else {
                emit("\tif (_doStackN[_doStx]) {  _doStackN[_doStx] = 0; }" +
                        " // Q1001 \n")
                emit("\tif (_doStx > 0) " +
                        "{ longjmp(_doStack[_doStx],99); } \n")
                emit("\telse ErrorMessage(\"Too Many Quits\", " +
                        "svPtr->LineNumber); \n")
            }
        } else {
            if (cs.IF_flg == 0 || cs.ForBlocks[cs.IF_flg] < 0) {
                emit("\tbreak; \n")
            } else {
                emit("\tgoto For" + cs.ForBlocks[cs.IF_flg] + "Exit; \n")
            }

            if (cs.Blocks[cs.IF_flg] == IF) {
                cs.Blocks[cs.IF_flg] = NUL
                cs.IF_flg--
                emit("\t} // if block 2389\n")
            }
        }

        return CmL.NEXT_CMND
    }

    // ------------------------------------------------------------------
    // the declare label (the DO 'e' arm)
    // ------------------------------------------------------------------

    fun dcl(): CmL {
        var i = 0
        while (true) {
            if (cs.cvarx > CVARMAX) {
                return errLbl(91, "\n*** 91 Too many C variables")
            }
            // the carrier write bound guard (the vendor walks
            // cvars[CVARMAX] off the domain; the guard folds
            // the UB)
            val slot = if (cs.cvarx < cs.cvars.size) {
                cs.cvars[cs.cvarx]
            } else {
                return errLbl(91, "\n*** 91 Too many C variables")
            }
            while (xdg(cs.xpxIdx()) != ' ' &&
                xdg(cs.xpxIdx()) != NUL &&
                xdg(cs.xpxIdx()) != ','
            ) {
                if (i < slot.name.size) slot.name[i++] =
                    xdg(cs.xpxIdx())
                cs.xpx++
            }
            if (i < slot.name.size) slot.name[i] = NUL
            cs.cvarx++
            if (xdg(cs.xpxIdx()) == ',') {
                i = 0
                cs.xpx++
                continue // goto dcl
            }
            return CmL.NEXT_CMND
        }
    }

    // ------------------------------------------------------------------
    // the do_entry label (the argumentless DO)
    // ------------------------------------------------------------------

    fun doEntry(): CmL {
        banner3CM("Do")

        // Argumentless DO
        if (xdg(cs.xpxIdx()) == NUL || xdg(cs.xpxIdx()) == ' ') {
            if (cs.JOB_flg != 0) { // is this really a JOB command?
                System.out.print("*** JOB requires argument\n")
                return errLbl(14, "") // arg_list_error, ernbr 14
            }

            emit("\t\ttpx" + cs.ExecName + " = svPtr->tpx; \n")

            emit("\t\tif ( (_i = setjmp(Efor" + cs.ExecName + "J)) == 0) ")
            emit("\tgoto Do_Block_Entry" + cs.ExecName +
                    "; //else continue \n")

            if (cs.IF_flg != 0) {
                emit("\t\telse if (_i == 100) " +
                        "goto Do_Block_Bypass" + cs.ExecName +
                        "; // IF_flg m1402\n") // must be in IF
            }

            cs.forx++
            cs.DoRet[cs.DoRetX] = cs.ExecName.toShort()
            cs.DoRetX++

            if (cs.POST_flg != 0) {
                emit("\t} // post conditional do\n")
                cs.POST_flg = 0
            }

            return CmL.NEXT_CMND
        }

        // Argumented DO
        return CmL.ANOTHER_DO
    }

    // ------------------------------------------------------------------
    // the another_do label (the argumented DO, with the ':' scan
    // and the '@' indirection and the \u0024\u0024 inject)
    // ------------------------------------------------------------------

    fun anotherDo(): CmL {
        emit("\t_ArgCount=0;  //section D1000 \n")
        Sysout("\tsvPtr->Rargsx=0;\n")

        val iX = cs.xpxIdx()
        var j = cs.xpxIdx()
        var k = 0
        val ttt = xd.copyOf() // memmove(ttt,xd,2048)

        // scan through label for non-quoted colon: a non-quoted
        // colon can't exist in the label field (the k==k/2*2
        // even-quote test kept verbatim)
        while (true) {
            if (xd[j] == NUL) break
            if (xd[j] == ',' && k == k / 2 * 2) break
            if (xd[j] == ':' && k == k / 2 * 2) break
            if (xd[j] == '"') {
                if (xd[j + 1] == '"') {
                    j = j + 1
                } else k++
            }
            j++
        }

        if (xd[j] == ':' && k == k / 2 * 2) {
            cs.xpx = (j + 1).toLong()
            cs.t0px = 0
            cs.t2 = 1
            cs.ierr = Stubs.parse_()
            if (cs.ierr != 0) return parseErr()
            emit("\tif (atoi( CSTR " + CstringOps.cstring(
                Stubs.Extract2(pd1, cs.sdlim)) +
                    ")) { // postconditional \n")
            cs.t0px = 0
            cs.t2 = 0
            cs.xpx = iX.toLong()
            System.arraycopy(ttt, 0, xd, 0, 2048) // memmove restore
        } else {
            emit("\t{\n")
        }

        if (xdg(cs.xpxIdx()) == '@') { // indirection in DO argument
            cs.at_flg = Stubs.atTest(xd, pd1, cs.pd1Last,
                cs.lines, cs.xpx)

            if (cs.at_flg == -1) return CmL.ABRT_EXIT

            if (cs.at_flg != 0) {
                val tmp = CharArray(1024)
                CstringOps.copyAtS(tmp, 0, "d ")
                Stubs.atCopy(cs.at_flg, tmp)

                cs.interpret(CstringOps.cstring(tmp))

                cs.xpx = cs.at_flg.toLong()
                emit("       } /* post conditional 1920*/\n")

                if (cs.POST_flg != 0) {
                    emit("       } /* post conditional 1920*/\n")
                    cs.POST_flg = 0
                }
                return CmL.NEXT_CMND
            }
        } else if (xdg(cs.xpxIdx()) == '$' &&
            Character.isLetterOrDigit(xdg(cs.xpxIdx() + 1))
        ) {
            cs.t0px = 0
            cs.t2 = 1
            cs.ierr = Stubs.parse_()
            if (cs.ierr != 0) return parseErr()
            emit("\t} \n")
        } else {
            if (cs.xpxIdx() - 2 in xd.indices) {
                xd[cs.xpxIdx() - 2] = '$'
                xd[cs.xpxIdx() - 1] = '$'
            }
            cs.xpx = cs.xpx - 2
            cs.t2 = 1
            cs.ierr = Stubs.parse_()
            if (cs.ierr != 0) return parseErr()
            cs.t2 = 0
            emit("\t} \n ")
        }

        // bypass post-cond, look for non-quoted, non-paren comma
        if (xdg(cs.xpxIdx()) == ':') {
            var m = 0
            k = 0
            cs.xpx++
            j = cs.xpxIdx()
            while (true) {
                if (xd[j] == NUL) break
                if (xd[j] == ',' && m == 0 && k == k / 2 * 2) break
                if (xd[j] == ' ' && m == 0 && k == k / 2 * 2) break
                if (xd[j] == '(' && k == k / 2 * 2) m++
                if (xd[j] == ')' && k == k / 2 * 2) m--
                if (xd[j] == '"') {
                    if (xd[j + 1] == '"') j = j + 1
                    else k++
                }
                j++
            }
            if (xd[j] == ',' && m == 0 && k == k / 2 * 2) {
                cs.xpx = (j + 1).toLong()
                return CmL.ANOTHER_DO
            }
            cs.xpx = j.toLong()
        }

        if (xdg(cs.xpxIdx()) == ',') {
            cs.xpx++
            return CmL.ANOTHER_DO
        }

        if (cs.POST_flg != 0) { // end of post-cond
            emit("\t} /* post conditional 3001*/\n")
            cs.POST_flg = 0
        }

        if (cs.JOB_flg != 0) return CmL.JOB_RETURN

        // the vendor's trailing while (1) parse tail here is
        // unreachable text (H-013, ledgered in the banner)
        return CmL.NEXT_CMND
    }

    // ------------------------------------------------------------------
    // the next_goto and gexit labels
    // ------------------------------------------------------------------

    fun nextGoto(): CmL {
        val dosave = cs.dosave
        var i = 0

        while (xdg(cs.xpxIdx()) != NUL) {
            val c = xdg(cs.xpxIdx())
            if (c == ',' || c == ' ' || c == NUL ||
                c == '+' || c == ':') break
            if (i < dosave.size - 1) dosave[i++] = c
            cs.xpx++
        }

        if (xdg(cs.xpxIdx()) == ':') { // post conditionals
            cs.xpx++
            cs.t0px = 0
            cs.t2 = 1
            cs.ierr = Stubs.parse_()
            if (cs.ierr != 0) return parseErr()
            emit("\tif (atoi((char *) " + CstringOps.cstring(
                Stubs.Extract2(pd1, cs.sdlim)) +
                    ")) /* m1635 postconditional*/")
            cs.t0px = 0
            cs.t2 = 0
        }

        dosave[i] = NUL

        var di = 0
        while (dosave[di] != NUL && dosave[di] != '^' &&
            dosave[di] != '+') di++

        if (dosave[di] == '+' || dosave[di] == '(') {
            // the '(' probe is vendor-verbatim (unreachable:
            // the scan above stops only at ^/+/end)
            return errLbl(8, "\n*** Label or file not found")
        }

        if (dosave[di] == NUL) { // simple goto
            val ds = CstringOps.cstring(dosave)
            if (dosave[0] in '0'..'9') {
                emit("\tgoto _" + ds + ";\n")
            } else {
                emit("\tgoto " + ds + ";\n")
            }
            return CmL.GEXIT
        }

        if (dosave[0] != '^') {
            // label appears first in a ^ ref
            dosave[di] = NUL
            CstringOps.copyAtS(tmp2, 0, CstringOps.cstring(dosave))
            // copy fcn name backwards
            val rest = CstringOps.cstringAt(dosave, di + 1)
            CstringOps.copyAtS(dosave, 1, rest)
            dosave[0] = '^'
        } else {
            CstringOps.copyAtS(tmp2, 0, "")
        }

        emit("\t_jj=Interpret(\"x \\\"goto ^" +
                CstringOps.cstringAt(dosave, 1) + "\\\"\",svPtr); \n")

        Sysout("\tgoto _epilogue; /* if fcn returns, this is end program */\n")

        return CmL.GEXIT
    }

    fun gexit(): CmL {
        if (xdg(cs.xpxIdx()) == ',') {
            cs.xpx++
            return CmL.NEXT_GOTO
        }

        if (cs.POST_flg != 0) {
            emit("\t} /* post conditional 3431*/\n")
            cs.POST_flg = 0
        }

        return CmL.NEXT_CMND
    }

    // ------------------------------------------------------------------
    // the job_return label
    // ------------------------------------------------------------------

    fun jobReturn(): CmL {
        emit("\t}")
        return CmL.NEXT_LINE
    }

    // ------------------------------------------------------------------
    // ctch - the catch label (the CLOSE 'a' target and the TRY
    // continuation)
    // ------------------------------------------------------------------

    fun ctch(): CmL {
        emit("catch(")
        cs.CatchFlg = 1
        cs.TryFlg = 0
        if (xdg(cs.xpxIdx()) == ' ' || xdg(cs.xpxIdx()) == NUL) {
            return errLbl(122, "\n*** 122 Missing catch argument")
        }
        var i = cs.xpxIdx()
        while (xd[i] != ' ' && xd[i] != NUL) i++
        val j = xd[i]
        xd[i] = NUL
        var flg = 1
        // the except[] table walk (the carrier this render
        // table-filled at start)
        for (k in cs.except.indices) {
            val e = cs.except[k]
            if (e.isNullOrEmpty()) continue
            if (CstringOps.cstringAt(xd, cs.xpxIdx()) == e) {
                flg = 0
                break
            }
        }
        if (flg != 0) {
            return errLbl(123, "\n*** 123 Unknown exception in catch")
        }
        emit(CstringOps.cstringAt(xd, cs.xpxIdx()) + " ) {\n")
        xd[i] = j
        cs.xpx = i.toLong()
        return CmL.NEXT_CMND
    }
}

// the master dispatch loop - every label state hops exactly as
// the vendor gotos do; the C returns surface as returnCode.
fun mumpsMain(args: Array<String>): Int {
    val ctx = MumpsCtx(args)
    var st = CmL.START
    loop@ while (true) {
        when (st) {
            CmL.START -> st = ctx.start()
            CmL.NEXT_LINE -> st = ctx.nextLine()
            CmL.NEXT_CMND -> st = ctx.nextCmnd()
            CmL.PSTRTN -> st = ctx.pstrtn()
            CmL.CTCH -> st = ctx.ctch()
            CmL.CNTNUE -> st = ctx.quitBody()
            CmL.DCL -> st = ctx.dcl()
            CmL.DO_ENTRY -> st = ctx.doEntry()
            CmL.ANOTHER_DO -> st = ctx.anotherDo()
            CmL.NEXT_GOTO -> st = ctx.nextGoto()
            CmL.GEXIT -> st = ctx.gexit()
            CmL.JOB_RETURN -> st = ctx.jobReturn()
            CmL.NEXT_LINE_DIRECT -> return abrtx() // unused state
            CmL.ABRT_EXIT -> return abrtx() // abrt_exit label
            CmL.DONE -> return ctx.returnCode ?: 9
        }
    }
}
