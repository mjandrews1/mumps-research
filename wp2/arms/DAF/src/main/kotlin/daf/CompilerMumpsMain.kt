package daf

import java.io.BufferedReader
import java.io.File
import java.io.FileReader

// Unit 278 (DAF B10 T3, giant): compiler-mumps.c::main (units.csv row
// 278: loc 1372, ptr 106, score 1125.2, the largest single render of
// the arm): the mumps2c compiler driver: the -v/-SUB option parse,
// the tmp2 = basename + ".m" output open and the fopen(in,"r") test,
// printf banners and ExitCode 99 arms, copyright_notice(Out), the
// leading '+' include-line getc/ungetc loop, the giant Sysout/fprintf
// include and define emit ladder (the emitted C text verbatim), the
// compiler state re/init (pd1cur 1 ... cvarx 0), the FBMAX ForBlocks
// -1 fill, then the two nested label machines: next_line (fgets line
// intake, cr/lf strip x2, tab-to-space, IF_flg close-brace flush, eof
// -> dotted unwind + abrt_exit, blank skip, inline-C and comment arms
// incl. the two verbatim-dead if(0)/if(!MAIN_flg&&0) main-gen blocks,
// tab walks, label/function-entry-point lattice, doty indents, xd
// copy, banner emit, EmitLabel write) and next_cmnd (command decode
// over the ctable word scan with the spelling warning, ':' post-
// conditional via atTest/atCopy + INTERPRET, the ';' pstrtn cut, the
// icmnd switch over ALL command arms with the return-code ladder to
// the error labels, and the error label block itself).
// Consults (2026-11-25T15:00:00Z nominal): rows 2/26 hit-reuse (Out
// -> DafCompilerState.emit, emitted C text verbatim), row 6/22/62
// hit-reuse (compiler TU state rides DafCompilerState + the raised
// file-scope seats: p2pxD/fcnFlgD/fFlgD/exitCodeD/cvarxD/pidD/
// outputFileD (unit-178), mainFlgD (unit-206), functionNameD (unit-
// 207), argMaxD/iflgMaxD/FBMAX_D (unit-213), symlen the svPtr.symlen
// extern fold), row 56 hit-reuse (char* compare/lattice over String),
// row 57 family hit-adapt (FILE* in/out: BufferedReader line/
// char-stream analog + the emit-builder Out; the physical output
// file write stays the raised abrtx owner seat's cell below), row 30
// hit-adapt (&pd1[sdlim] Extract2 through the extract2Daf seat, the
// String side), row 69 hit-reuse (printf stdout -> Kotlin print),
// row 99 return-fold, H-010/H-011 hit-reuse (the goto ladder renders
// as the MLblM machine; label per goto target, one when-arm per
// vendor label, plain return per C return), row 117 doty seat
// (dotyDaf: the vendor's short*-out lattice already folds onto
// DafCompilerState), row 116/121/122 and family hit-reuse (command
// seats: lockCodeD/lockCodeDaf, setCodeDaf, forCodeDaf, readCodeDaf,
// writeCodeDaf, killCodeD, zcommandsCodeD, mumpsOpenD; each return
// checked against the PARSE_ERROR_D..BAD_LOCK_D..NEXT_LINE_D ladder
// consts of DafInterpStubsT3/CompilerZcommandsCode/KillCode).
// Named raises (PERMANENT for this batch, disclosure): ifCodeDaf /
// newCodeDaf / abrtxDaf / copyrightNoticeDaf (DafInterpStubsT3):
// owners compiler-if.c::if_code, compiler-new.c::new_code,
// compiler-abrtx.c::abrtx, compiler-copyright-notice.c::copyright_
// notice are roster rows never opened on the DAF arm (the unit-245
// macros ledger named them to raised stubs); call sites cite the
// seats (stub-mediated honesty). INTERPRET macro -> the seated
// emitInterpretDaf (SetCode.kt, unit-209 pin). errmod_ folds to the
// emitInterpretDaf error ladder (unit-243 ledger). Extract2 through
// extract2Daf. atTest/atCopyDaf are the unit-165/166 bodies.
// Dispositions/quirks kept verbatim or ledgered: (1) the no-'.'-
// basename fallback copies argv[1] not argv[argc-1] (vendor quirk
// kept); (2) the cr/lf strip indexes pd1[strlen(&pd1[1])] which at
// eof hits pd1[0] (kept verbatim, quirk comment at the cell); (3) the
// two dead main-gen blocks ( if(0) and if(!MAIN_flg && 0) ) are kept
// as constant-false guards with comment-folded bodies (residue
// precedent of units 95/187/206); (4) unreferenced vendor error
// cells (indirect_err/do_error/quit_err/Ext_file/go_err/ref_err)
// never carry goto traffic on any arm path - comment-only ledger;
// (5) malloc NULL arms are unreachable on the JVM (GC ledger); (6)
// the '+' for-loop memcpy over ttt[2048] in the DO postconditional
// scan folds onto the CharArray copy with the vendor scan verbatim;
// (7) PS quirk: memsize.h:32 pins PS 4096 for this TU (sysparms.h
// carries an unselected twin 32000); svPtr.symlen loads 4096 here
// (dual-define disclosure); (8) vendor TextStart malloc(size+2)
// versus strcpy(name,&xd[p2px+1]) off-by-one seat kept as the String
// fold; (9) the unit-213 epi ForBlocks -1 fill rides this session's
// DafCompilerState.ForBlocks/Blocks FBMAX_D resize (the 64-cell
// carrier would bounds-fault the vendor 4000 fill; corrected here,
// disclosure; vendor globals.h:115-116 short ForBlocks[FBMAX]/
// Blocks[FBMAX]); (10) JOB/goto flows keep the vendor label web via
// the machine (job_return/do_entry/next_goto/gexit/cntnue/ctch
// arms); (11) fcns.h/sysfunc.h includes gate on SubFlag via
// #ifndef SUB - the emitted text gates on !subFlagD verbatim.
// Dafny: EXEMPT (1300-LOC procedural driver over emit/TU state; the
// T2 exemption family; journal row).

// goto-target machine labels (H-011): one per vendor jump target;
// the unreferenced vendor error cells ride the comment ledger above.
private enum class MLblM {
    NEXT_LINE, NEXT_CMND, PSTRTN, CNTNUE, CTCH, DO_ENTRY, JOB_RETURN,
    NEXT_GOTO, COMMAND, PARSE_ERR, ARG_LIST_ERROR, NOGBLPERMITTED,
    MEM_ERR, TOO_LHS, TOO_MANY_VARS, MISSING_ZMAIN, KILL_DCL_ERR,
    NOBRK_ERR, LAB_ERR, LBL_ERROR, BAD_LOCK, CATCH_ERROR, CATCH_UNKNOWN,
    TRY_UNKNOWN, LABEL_IN_BLOCK, GOTO_IN_BLOCK, ABRT1, ABRT_EXIT
}

// short SubFlag - file-scope pending carrier resolved here.
private var subFlagD = 0

// short JOB_flg, TryFlg, CatchFlg, EmitLabel, ernbr, exportflag -
// main-TU file-scope carriers (this row's cells; Zcommands raised
// the shared TUs, these ride the main render per the per-unit rule).
private var jobFlgMD = 0
private var tryFlgMD = 0
private var catchFlgMD = 0
private var emitLabelMD = 0
private var ernbrMD = 0
// exportflag was a main-TU file-scope carrier here; the
// supplementary new_code render (2026-11-27, PI adjudication)
// reads the shared vendor global, so the seat is consolidated to
// the public exportFlagD in CompilerNewCode.kt; this render's
// ELSE-X write cell (below) rebinds to it.

// static char *ctable[] - the command-word validation table.
private val CTABLE_M = arrayOf(
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
    "")

// char *except[] - compiler-globals.h:174 (the catch() names).
private val EXCEPT_M = arrayOf(
    "InputLengthException",
    "InterpreterException",
    "")

// FILE *in analog: a BufferedReader char stream with one-slot
// pushback (the getc/ungetc head phase) plus the fgets512 line seat.
private class InFileD(private val rd: BufferedReader) {
    private var pushback = -2 // -2 = empty, else the pushed-back int
    fun close() = rd.close()
    fun getc(): Int { // getc(in)
        if (pushback != -2) { val c = pushback; pushback = -2; return c }
        return rd.read()
    }
    fun ungetc(c: Int) { pushback = c } // ungetc(i, in)
    // fgets(&pd1[1], 512, in): at most 511 chars, newline kept,
    // NUL-terminated; null on immediate eof (row 57 analog).
    fun fgets512(dst: CharArray, off: Int): Boolean {
        var n = 0
        while (n < 511) {
            val c = getc()
            if (c < 0) {
                dst[off + n] = NUL_D
                return n != 0
            }
            dst[off + n] = c.toChar()
            n++
            if (c == '\n'.code) break
        }
        dst[off + n] = NUL_D
        return true
    }
}

// strlen(&pd1[1]) local: content length from offset 1.
private fun lenAt1(buf: CharArray): Int {
    var i = 1
    while (buf[i] != NUL_D) i++
    return i - 1
}

fun mainCDaf(args: Array<String>): Int {

    val C = DafCompilerState
    val svPtr = DafInterpState.svPtr

    var lab = MLblM.NEXT_LINE
    var returnCode = 0

    // char cur_line[1024]; - the machine-wide source line carrier.
    var curLineM = ""

    // int lines/pd1len - the NEXT_LINE cell locals ride
    // DafCompilerState.lines + a local pd1len fold.

    // ---------------------------------------------------------------
    // head: option parse, output/input open (vendor :42-97)
    // ---------------------------------------------------------------
    svPtr.symlen = 4096 // symlen = PS; memsize.h:32 PS 4096 (quirk 7)
    System.out.flush()
    print("The Mumps Compiler\n")

    if (args.size == 2 && args[1] == "-v") return 99 // -v banner test

    var inF: InFileD? = null
    var tmp2M = ""

    if (args.size == 1) {
        print("*** \u0007 Name of program to compile must be given\n\n")
        exitCodeD = 99
        return 99
    } else {
        if (args.size == 3) {
            if (args[1] == "-SUB") {
                subFlagD = 1
            } else {
                print("*** Unrecognized option: ${args[1]}\n\n")
                exitCodeD = 99
                return 99
            }
        }

        // for (i = strlen(argv[argc-1]); i >= 0 && argv[argc-1][i] != '.'; i--);
        val last = args[args.size - 1]
        val dotI = last.lastIndexOf('.')

        if (dotI < 0) {
            tmp2M = args[1] // vendor quirk: argv[1], not argv[argc-1]
        } else {
            tmp2M = last.substring(0, dotI) // argv[argc-1][i]=0 + copy
        }

        tmp2M = tmp2M + ".m" // strcat(tmp2, ".m")

        // Out = fopen(tmp2, "w"); - the emit-builder fold (rows
        // 2/26): the physical C text collects in DafCompilerState.out
        // (the raised abrtxDaf owner seat carries the fclose/
        // conversion cell; disclosure in the header).
        C.out.setLength(0)

        outputFileD = tmp2M // strcpy(output_file, tmp2)
        pidD = getpidD()    // pid = getpid()

        val inFile = File(args[args.size - 1])
        if (!inFile.isFile) {
            // if ((in = fopen(argv[argc-1], "r")) == NULL)
            print("*** \u0007 File ${args[args.size - 1]} not found.\n\n")
            exitCodeD = 99
            return 99
        }
        inF = InFileD(BufferedReader(FileReader(inFile)))
        print("Translating ${args[args.size - 1]} to C++:\n")
    }

    // copyright_notice(Out); - raised named-seat call (owner
    // roster row never opened on this arm; stub-mediated honesty).
    copyrightNoticeDaf()

    // ---------------------------------------------------------------
    // leading '+include' lines: getc/ungetc loop (vendor :101-116)
    // ---------------------------------------------------------------
    run {
        val str = CharArray(512)
        while (true) {
            val i = inF!!.getc()
            if (i != '+'.code) {
                inF.ungetc(i)
                break
            }
            // fgets(str, 512, in) into the local str carrier
            var n = 0
            var ok = false
            while (n < 511) {
                val c = inF.getc()
                if (c < 0) break
                str[n] = c.toChar()
                n++
                if (c == '\n'.code) { ok = true; break }
            }
            if (!ok && inF.getc() < 0 && n == 0) {
                // fgets == NULL eof
                print("Unexpected end of file\n")
                exitCodeD = 99
                return abrtxDaf()
            }
            str[n] = NUL_D
            C.emit(DafCRuntime.cstringAt(str, 0) + "\n")
        }
    }

    // ---------------------------------------------------------------
    // Include/define/emit ladder (vendor :120-243), text verbatim
    // ---------------------------------------------------------------
    C.emit("#undef _MDH_\n")
    C.emit("#include <stdio.h>\n")
    C.emit("#include <string.h>\n")
    C.emit("#include <stdlib.h>\n")
    C.emit("#include <setjmp.h>\n")
    C.emit("#include <unistd.h>\n")
    C.emit("#include <signal.h>\n")
    C.emit("#include <sys/time.h>\n")
    C.emit("#include <mumpsc/inline.h>\n")
    C.emit("#include <mumpsc/defines.h>\n")
    C.emit("#include <mumpsc/strmax.h>\n")
    C.emit("#include <mumpsc/libmpscpp.h>\n")
    C.emit("#include <mumpsc/sym.h>\n")
    C.emit("#include <mumpsc/builtin.h>\n")
    C.emit("#include <mumpsc/compiler.h>\n")
    C.emit("#define FORPARM STR_MAX  /* Size of For loop parameter */\n")
    C.emit("#define CSTR (char *)\n")
    C.emit("#define UNS unsigned char *\n")
    C.emit("#define uns unsigned char *\n")
    C.emit("#define CCHR const char *\n")
    C.emit("#include <mumpsc/keyfix.h>\n")
    C.emit("#include <mumpsc/globalOrder.h>\n")

    if (subFlagD != 0) C.emit("#define SUB\n")

    C.emit("#include <mumpsc/stateVector.h>\n")
    C.emit("int (*__label_lookup)(char *);\n")
    C.emit("const char * (*__text_function)(int);\n")

    C.emit("void fcn(MSV *);\n")
    C.emit("void zfcn(unsigned char *, unsigned char *, struct MSV *); /* z functions */\n")

    C.emit("char * sym_(int, unsigned char *, unsigned char *, struct MSV *); /* symbol table */\n")

    C.emit("char * makeLock(char *, char *, char *, char, char *); /* global array lock service */\n")
    C.emit("char * cleanLocks(char *); /* global array lock service */\n")
    C.emit("char * releaseLock(char *, char *, char *); /* global array lock service */\n")
    C.emit("char * eraseLocks(char *); /* global array lock service */\n")
    C.emit("void add(const char *a,const char *b,char *c); /* math function */\n")
    C.emit("void sub(const char *a,const char *b,char *c); /* math function */\n")
    C.emit("void mult(const char *a,const char *b,char *c); /* math function */\n")
    C.emit("void divx(const char *a,const char *b,char *c); /* math function */\n")
    C.emit("void divi(const char *a,const char *b,char *c); /* math function */\n")
    C.emit("void _fnumber(unsigned char *,unsigned char *,\n")
    C.emit("       unsigned char *,unsigned char *, MSV *); /* math function */\n")
    C.emit("int DBSem = 0; /* deadlock prevention */\n")
    C.emit("int pm (unsigned char [], const unsigned char pattern[], struct MSV * svPtr); /* pattern match */\n")

    // functions: the floatstring/numcomp2/strxcat/strcatz inline
    // emitters + SyncClock + set_alarm (text verbatim)
    C.emit("\nstatic inline int floatstring(const char * p) { \n" +
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

    C.emit("\ninline int numcomp2(unsigned char *aa) { \n" +
            " if (floatstring((char *) aa)) return (atof( (char *) aa) != 0.0);  \n" +
            " return atoi((char *) aa);  \n" +
            " }\n")

    C.emit("\ninline char * strxcat(char * a, char *b)  \n" +
            "{ while (*a) a++; while( (*(a) = *(b)) ) { a++; b++; } return a; }\n")

    C.emit("\ninline char * strcatz(char * out, char *in)  \n" +
            "{ while (*out) out++; while (*(out) = *(in)) { out++; in++; } *(out++) = '\u0001';  \n" +
            "*out = 0; return (char *) out; } \n\n")

    C.emit("void SyncClock() {\n")
    C.emit("    DBSem = 1;\n")
    C.emit("    }\n\n")

    C.emit("unsigned int set_alarm (unsigned int seconds) {\n")
    C.emit("    struct itimerval old, xnew;\n")
    C.emit("    xnew.it_interval.tv_usec = 0;\n")
    C.emit("    xnew.it_interval.tv_sec = 0 ; // interval between alarms\n")
    C.emit("    xnew.it_value.tv_usec = 0;\n")
    C.emit("    xnew.it_value.tv_sec = (long int) 0;\n")
    C.emit("    if (setitimer (ITIMER_REAL, &xnew, &old) < 0)\n")
    C.emit("        return 0;\n")
    C.emit("    else\n")
    C.emit("        return old.it_value.tv_sec;\n")
    C.emit("    }\n\n")

    linepadDaf(C, "#include <mumpsc/global.h>", "Global array definitions")

    linepadDaf(C, "", "Interface to Mglobal():")
    linepadDaf(C, "", "g = command")
    linepadDaf(C, "", "key = key sought or stored")
    linepadDaf(C, "", "bd = value returned or stored")
    linepadDaf(C, "", "svPtr = address of state vector")

    C.emit("#ifndef SUB\n")
    linepadDaf(C, "#include <mumpsc/fcns.h>", "System support functions")
    linepadDaf(C, "#include <mumpsc/sysfunc.h>", "Many common global variables")
    C.emit("#endif\n")

    // ---------------------------------------------------------------
    // Compiler start: state re/init (vendor :246-263)
    // ---------------------------------------------------------------
    C.pd1cur = 1
    svPtr.symlen = 4096       // symlen = PS;
    C.t0px = 0
    C.t2 = 0
    iflgMaxD = 0              // IF_flg_max = 0 (compi decl col)
    // cmdx = 0; setpiece = 0 - the vendor resets the two parser
    // cells here; both ride the parse_/interpret TU state already
    // zero-initialized this cycle (comment fold, ledgered)
    C.forxmax = 0
    C.forx = 0
    C.forxFLG = 0
    C.forLine = 0
    C.pd1Last = 0
    C.ExecLevel = 0
    symIncrFlgMD = 0          // SymIncrFlg = 0
    argMaxD = 0
    cvarxD = 0
    clearTextStartD()          // TextStart=NULL;
    functionNameD = ""        // strcpy(FunctionName,"")

    for (i in 0 until FBMAX_D) C.ForBlocks[i] = -1
    // (vendor short ForBlocks[FBMAX]; Blocks[FBMAX] - the
    // DafCompilerState arrays resized FBMAX_D this session; the
    // unit-213 epi fill is now in-bounds, disclosure in the header)

    // ===============================================================
    // the label machine (H-011)
    // ===============================================================
    machine@ while (true) {
        when (lab) {
        // -----------------------------------------------------------
        MLblM.NEXT_LINE -> {
        // -----------------------------------------------------------
        // next_line: (vendor :266-324 ... label arms through the
        // xd copy + banner, ending at next_cmnd)
        emitLabelMD = 0
        C.iiitmp = 0 // new temp pool for each line
        C.forLine = 0
        catchFlgMD = 0
        C.pd1[1] = NUL_D // strcpy(&pd1[1],"")

        val hadLine = inF!!.fgets512(C.pd1, 1)
        var pd1lenM: Int
        if (!hadLine) { // input line eof fold
            C.pd1cur = 9999
            pd1lenM = 1
            C.pd1[1] = NUL_D
        } else {
            C.pd1cur = 1 // start
            pd1lenM = 2048
        }

        // cr/lf strip x2: at eof strlen(&pd1[1])==0 indexes pd1[0]
        // (vendor quirk kept verbatim)
        run {
            val k0 = lenAt1(C.pd1)
            if (C.pd1[k0].code < 32) C.pd1[k0] = NUL_D // <cr>
        }
        run {
            val k0 = lenAt1(C.pd1)
            if (C.pd1[k0].code < 32) C.pd1[k0] = NUL_D // <lf>
        }

        var i = 1 // for (i = 1; pd1[i] != 0 && i < 512; i++)
        while (C.pd1[i] != NUL_D && i < 512) {
            if (C.pd1[i] == TAB_D) C.pd1[i] = ' ' // tabs to blanks
            i++
        }

        C.lines++

        while (C.IF_flg != 0) { // closing braces flush
            C.emit("\t}\n")
            C.IF_flg--
        }

        if (C.pd1cur > pd1lenM) {
            if (C.ExecLevel > 0) {
                while (C.ExecLevel > 0) {
                    C.ExecLevel--
                    C.emit("\t} while (0);   // dotted un-indent ${C.ExecX - 1}\n")
                    C.emit("\tlongjmp(Efor${C.ExecStack[--C.ExecX]}J,99);    // return \n")
                    C.emit("\nDo_Block_Bypass${C.ExecStack[C.ExecX]}: ; //A541 \n")
                }
                C.ExecLevel = 0
            }
            lab = MLblM.ABRT_EXIT
            continue@machine
        }

        if (C.pd1[C.pd1cur] == NUL_D) {
            C.pd1cur++
            lab = MLblM.NEXT_LINE
            continue@machine
        }

        curLineM = DafCRuntime.cstringAt(C.pd1, C.pd1cur) // cur_line

        // inline C code - emit as is (vendor :335-343)
        if (C.pd1[C.pd1cur] == '%' || C.pd1[C.pd1cur] == '+') {
            C.emit("\n//--------------\n")
            C.emit("//       Inline\n")
            C.emit("//--------------\n\n")
            C.emit(DafCRuntime.cstringAt(C.pd1, C.pd1cur + 1) + "\n")
            while (C.pd1[C.pd1cur] != NUL_D) C.pd1cur++
            C.pd1cur++
            lab = MLblM.NEXT_LINE
            continue@machine
        }

        // comment arm (vendor :346-381) incl. the verbatim-dead
        // main-gen block (if (!MAIN_flg && 0)); kept note-only
        // (constant-false guard; body folded to comments: vendor
        // emits the int main() preamble, fcninit, __label_lookup/
        // __text_function binds, cgi.h include, the SymPut percent
        // loop over argv, try { open, Sysout_skip newline, and
        // MAIN_flg = 1 - none of these executes: vendor && 0)
        if (C.pd1[C.pd1cur] == '#' ||
            (C.pd1[C.pd1cur] == '/' && C.pd1[C.pd1cur + 1] == '/')) {
            if (C.pd1[C.pd1cur] == '/') C.pd1cur++
            C.emit("\n// " + DafCRuntime.cstringAt(C.pd1, C.pd1cur + 1) + "\n")
            C.pd1cur++
            while (C.pd1[C.pd1cur] != NUL_D) C.pd1cur++
            C.pd1cur++
            if (mainFlgD == 0 && false) { // if (!MAIN_flg && 0)
                // verbatim-dead body: kept note-only (see header)
            }
            lab = MLblM.NEXT_LINE
            continue@machine
        }

        i = C.pd1cur
        while (C.pd1[C.pd1cur] != NUL_D && C.pd1[C.pd1cur] != TAB_D) C.pd1cur++
        if (C.pd1[C.pd1cur] != NUL_D)
            while (C.pd1[C.pd1cur] != NUL_D && C.pd1[C.pd1cur] == TAB_D) C.pd1cur++

        // multiple TABs
        C.pd1cur = i

        if (C.pd1[C.pd1cur] != TAB_D) {
            while (C.pd1[C.pd1cur] != TAB_D && C.pd1[C.pd1cur] != NUL_D) {
                if (C.pd1[C.pd1cur] == '(')
                    while (C.pd1[C.pd1cur] != ')' && C.pd1[C.pd1cur] != NUL_D)
                        C.pd1cur++
                C.pd1cur++
            }
            C.pd1[C.pd1cur] = NUL_D

            var flg = 1
            var thistime = 0
            var kk = 0
            var n = 0
            var iiV: Int
            var jjV: Int

            // function-start arms (^ / ^^)
            if (C.pd1[i] == '^' && C.pd1[i + 1] == '^') {
                i = i + 2
                fFlgD = 1
                if (mainFlgD != 0) epDaf(0, C.ExecName, pidD)
                if (fcnFlgD != 0) epDaf(1, C.ExecName, pidD)
                fcnFlgD = 1
                thistime = 1
            } else if (C.pd1[i] == '^' && C.pd1[i + 1] != '^') {
                i = i + 1
                fFlgD = 1
                if (mainFlgD != 0) epDaf(0, C.ExecName, pidD)
                if (fcnFlgD != 0) epDaf(1, C.ExecName, pidD)
                fcnFlgD = 1
                thistime = 1
            }

            iiV = i
            while (C.pd1[i] != NUL_D) {
                if (C.pd1[i] == '(') {
                    if (flg == 1) {
                        C.emit("\n//=========================================" +
                                "========================================\n")
                        C.emit("//\tEntry Point " +
                                DafCRuntime.cstringAt(C.pd1, iiV) +
                                " + + + + + + + \n")
                        C.emit("\n//=========================================" +
                                "========================================\n")
                        flg = 0
                    }
                    C.pd1[i] = NUL_D

                    if (fFlgD == 0 && mainFlgD == 0) {
                        lab = MLblM.MISSING_ZMAIN
                        continue@machine
                    }

                    if (fFlgD != 0 && thistime != 0) {
                        // this is a C subfunction
                        var ff = 0
                        cvarxD = 0
                        functionNameD = DafCRuntime.cstringAt(C.pd1, iiV)

                        C.emit("char * " + functionNameD +
                                "(struct MSV * svPtr, char * offset")
                        if (C.pd1[i + 1] != ')') C.emit(",")

                        jjV = i + 1
                        feWalk@ while (C.pd1[jjV] != NUL_D) {
                            var gotoFe1 = false
                            if (C.pd1[jjV] == '+') { // non mumpsc param
                                jjV++
                                val tmpEmit = StringBuilder()
                                while (true) {
                                    tmpEmit.append(C.pd1[jjV])
                                    jjV++
                                    if (C.pd1[jjV] == ',' ||
                                        C.pd1[jjV] == ')') break
                                }
                                C.emit(tmpEmit.toString())
                                if (C.pd1[jjV] == ')') {
                                    ff = 0
                                    gotoFe1 = true
                                } else {
                                    C.emit(",")
                                    jjV++
                                    continue@feWalk
                                }
                            }
                            if (C.pd1[jjV].isLetter()) ff = 1
                            if (!gotoFe1) {
                                if ((C.pd1[jjV] == ',' || C.pd1[jjV] == ')')
                                    && ff == 1) {
                                    C.emit("const char * _Arg${kk}${C.pd1[jjV]}")
                                    kk++
                                }
                            }
                            // fe1:
                            if (C.pd1[jjV] == ')') {
                                if (ff == 0) C.emit(") ")
                                C.emit("{\n\n")
                                fcninitDaf(outputFileD, pidD)
                                break@feWalk
                            }
                            jjV++
                        }

                        C.emit("__label_lookup=" + functionNameD + "_label_lookup; \n")
                        C.emit("__text_function=" + functionNameD + "_text_function; \n\n")
                        C.emit("try { // try001 \n ")
                        C.emit("svPtr->_SymPush++; \n")
                        C.emit("_SymPush++; \n")
                        C.emit("svPtr->start[svPtr->_Sym]=NULL; \n")
                        symIncrFlgMD = 1
                        C.emit("if (svPtr->_Sym > SYM_MAX) ")
                        C.emit("ErrorMessage(\"Too many function calls\"," +
                                "svPtr->LineNumber); \n")
                        C.emit("_doStackN[_doStx-1] = 1;" +
                                " // flag to restore namespace\n")
                        C.emit("\tif (svPtr->Rargsx) { // EP1005\n")
                        C.emit("\t\tfor (_i = 0; _i < svPtr->Rargsx; _i++) " +
                                "strcpy(_Refs[_i], svPtr->Rargs[_i]); \n")
                        C.emit("\t\t_Refsx = svPtr->Rargsx; \n")
                        C.emit("\t\t} \n")
                    } else {
                        if (!isAlnumAt(C.pd1, iiV)) {
                            lab = MLblM.LAB_ERR
                            continue@machine
                        }
                        if (C.pd1[iiV].isDigit()) C.emit("_")
                        C.emit(DafCRuntime.cstringAt(C.pd1, iiV) + ": \n")
                        C.emit("svPtr->start[svPtr->_Sym] = NULL; \n")
                        symIncrFlgMD = 1
                        C.emit("_SymPush++; \n")
                        C.emit("if (svPtr->_Sym > SYM_MAX) ")
                        C.emit("ErrorMessage(\"Too many function calls\"," +
                                "svPtr->LineNumber); \n")
                        C.emit("_doStackN[_doStx-1]=1;" +
                                " // flag to restore namespace \n")
                    }

                    C.pd1[i] = '('
                    jjV = i + 1
                    argScan@ while (C.pd1[jjV] != NUL_D) {
                        if (C.pd1[jjV] == '+') {
                            while (true) {
                                if (C.pd1[jjV] == ',' || C.pd1[jjV] == ')') break
                                jjV++
                            }
                            if (C.pd1[jjV] == ')') {
                                jjV++
                                break@argScan
                            }
                            i = jjV
                            continue@argScan
                        }
                        if (C.pd1[jjV] == ',' || C.pd1[jjV] == ')') {
                            val kV = C.pd1[jjV]
                            C.pd1[jjV] = NUL_D
                            if (DafCRuntime.cstringAt(C.pd1, i + 1).isEmpty())
                                break@argScan
                            if (fFlgD != 0) {
                                C.emit("\tif (_Arg${n}!=NULL) \n\t\t" +
                                        "sym_(100, (UNS) \"" +
                                        DafCRuntime.cstringAt(C.pd1, i + 1) +
                                        "\", (UNS) _Arg${n}, svPtr);" +
                                        " // Store Arguments EP1001\n")
                                C.emit("\tstrcpy (svPtr->RefParm[${n}], \"" +
                                        DafCRuntime.cstringAt(C.pd1, i + 1) +
                                        "\" ); // EP1002\n")
                            } else {
                                C.emit("\tif (__Arg${n}!=NULL) sym_(100," +
                                        " (UNS) \"" +
                                        DafCRuntime.cstringAt(C.pd1, i + 1) +
                                        "\", (UNS) __Arg${n},svPtr);" +
                                        " // Store Arguments EP1003 \n")
                                C.emit("\tstrcpy (svPtr->RefParm[${n}], \"" +
                                        DafCRuntime.cstringAt(C.pd1, i + 1) +
                                        "\" ); // EP1004 \n")
                            }
                            n++
                            if (n > argMaxD) argMaxD = n
                            C.pd1[jjV] = kV
                            if (kV == ')') break@argScan
                            i = jjV
                        }
                        jjV++
                    }
                    if (fFlgD != 0 && thistime != 0) {
                        C.emit("\tgoto _eps;  " +
                                "// entry point label router \n")
                        C.emit("_fcn: \n")
                    }
                }
                i++
            }

            // ArgCount check (sav opcode typo _Arg%d! =NULL kept as
            // the vendor emits "\t\tif (_Arg%d!=NULL)" - see the
            // EP1001 note below; the vendor's actual text is at
            // :593-595)
            if (flg == 0 && fFlgD == 0) {
                C.emit("\tif (_ArgCount < ${n}) " +
                        "ErrorMessage(\"Bad argument count\", " +
                        "svPtr->LineNumber); \n")
            }

            if (flg == 1) {
                var iL = iiV
                var labeled = false
                while (C.pd1[iL] != NUL_D) {
                    if (C.pd1[iL] != ' ') { // label
                        emitLabelMD = 1
                        labeled = true
                        if (!isAlnumAt(C.pd1, iiV)) {
                            lab = MLblM.LAB_ERR
                            continue@machine
                        }
                        // LabelsStart malloc walk folds onto the
                        // TxtNodeD prepend (JVM GC ledger; unit-206
                        // chain owner; release-of-access helper)
                        var nm = DafCRuntime.cstringAt(C.pd1, iiV)
                        if (nm.isNotEmpty() && nm[0].isDigit()) nm = "_" + nm
                        pushLabelTxtD(nm, C.lines)
                        break
                    }
                    iL++
                }
                if (labeled) { /* the vendor break; lands here */ }
            }

            C.pd1[C.pd1cur] = TAB_D // replace
        }

        // while (pd1[pd1cur] == TAB) pd1cur++;
        while (C.pd1[C.pd1cur] == TAB_D) C.pd1cur++

        // -------------------------------------------------------
        // Doty indents (vendor :633-668)
        // -------------------------------------------------------
        if (C.pd1[C.pd1cur] == '.') {
            var jL = 0
            var iL = C.pd1cur
            while (C.pd1[iL] == '.' || C.pd1[iL] == ' ') {
                if (C.pd1[iL] == '.') jL++
                iL++
            }
            if (jL > 10) {
                print("*** Too many block indents\n")
                ernbrMD = 99
                lab = MLblM.ABRT1
                continue@machine
            }
            if (jL > C.ExecLevel + 1) { // skipped a level
                print("*** Code level skip\n")
                ernbrMD = 99
                lab = MLblM.ABRT1
                continue@machine
            }
            // ExecLevel set to j in doty (the vendor passes the
            // short* lattice; dotyDaf folds them onto C)
            if (dotyDaf(jL) != 0) {
                lab = MLblM.ABRT1
                continue@machine
            }
            C.pd1cur = iL
        } else if (C.ExecLevel > 0) {
            while (C.ExecLevel > 0) {
                C.ExecLevel--
                C.emit("\t} while(0); // dotted un-indent ${C.ExecX - 1} \n")
                C.emit("\tlongjmp(Efor${C.ExecStack[--C.ExecX]}J,99); // return \n")
                C.emit("\nDo_Block_Bypass${C.ExecStack[C.ExecX]}: ; //B848 \n\n")
            }
            C.ExecLevel = 0
        }

        if (C.ExecLevel == 0) C.forxFLG = 0

        // -------------------------------------------------------
        // copy source line to xd + banner (vendor :672-716)
        // -------------------------------------------------------
        var xpxL = 1
        var jL = 1
        C.pd1Last = C.pd1cur
        while (true) {
            val ch = C.pd1[C.pd1cur]
            C.xd[jL] = ch
            jL++
            C.pd1cur++
            if (ch == NUL_D) break
        }

        C.emit("\n//=============================================" +
                "==================================== \n")
        if ((mainFlgD != 0 || fcnFlgD != 0) && tryFlgMD == 0) {
            C.emit("svPtr->LineNumber=${C.lines}; // $curLineM \n")
        } else {
            C.emit("// $curLineM \n")
        }
        C.emit("//=============================================" +
                "==================================== \n")

        // write the label
        if (emitLabelMD != 0) {
            if (C.ExecLevel > 0) {
                lab = MLblM.LABEL_IN_BLOCK
                continue@machine
            }
            C.emit("\n" + labelsStartD!!.name + ": \n")
            emitLabelMD = 0
        }
        C.xd[jL] = NUL_D
        C.xpx = xpxL

        lab = MLblM.NEXT_CMND
        continue@machine
        }

        // -----------------------------------------------------------
        MLblM.NEXT_CMND -> {
        // -----------------------------------------------------------
        // next_cmnd: (vendor :718-851) decode entry
        if (C.IF_flg > iflgMaxD) iflgMaxD = C.IF_flg

        while (true) {
            // the order of these tests is important
            if (C.xpx >= 1024 || C.xd[C.xpx] == NUL_D) {
                if (C.IF_flg != 0) {
                    var iL = C.IF_flg
                    while (iL > 0) {
                        C.emit("\t} ")
                        if (C.ForBlocks[iL] >= 0) {
                            C.emit("while (0); longjmp(for${C.ForBlocks[iL]}J, _i); " +
                                    "// FOR loop ${iL} close \n")
                            if (C.Blocks[iL] == FOR_D) {
                                C.emit("\nFor${C.ForBlocks[iL]}Exit: ; // exit for loop \n\n")
                            }
                            C.ForBlocks[iL] = -1
                        } else {
                            C.emit("\n")
                        }
                        iL--
                    }
                    C.IF_flg = 0
                }
                if (tryFlgMD != 0) {
                    C.emit("\t} \n")
                    tryFlgMD = 1
                }
                if (catchFlgMD != 0) {
                    C.emit("\t} \n")
                    catchFlgMD = 0
                }
                lab = MLblM.NEXT_LINE
                continue@machine
            }
            if (C.xd[C.xpx] != ' ' && C.xd[C.xpx] != TAB_D) break
            C.xpx++
        }

        // decode command word
        p2pxD = C.xpx
        C.t2 = 0
        C.t0px = 0
        jobFlgMD = 0
        C.POST_flg = 0
        C.MergeFlg = 0

        // // c++ style comment found
        if (C.xd[C.xpx] == '/' && C.xd[C.xpx + 1] == '/') {
            C.xpx = 9999
            lab = MLblM.NEXT_CMND
            continue@machine
        }

        val icmnd = C.xd[C.xpx].lowercaseChar().code // icmnd = tolower

        // check command word (vendor :783-824)
        val word = CharArray(25)
        var wx = 1
        var flg = 1
        word[0] = icmnd.toChar()
        run {
            var iW = C.xpx
            while (true) {
                iW++
                if (!C.xd[iW].isLetter()) break
                if (wx < 25) word[wx] = C.xd[iW].lowercaseChar()
                wx++
            }
            C.xpx = iW
        }
        // vendor notes the word[25] bound; long words truncate at 24
        if (wx >= 25) wx = 24
        word[wx] = NUL_D
        val wordS = DafCRuntime.cstringAt(word, 0)

        for (i in CTABLE_M.indices) {
            if (CTABLE_M[i].isEmpty()) break
            if (wordS == CTABLE_M[i]) { flg = 0; break }
        }

        if (icmnd == ';'.code) { // goto pstrtn
            lab = MLblM.PSTRTN
            continue@machine
        }

        if (flg != 0) {
            print("*** Warning, possible command spelling error: $wordS ")
            print("in or near line ${C.lines}\n")
            print("*** " + DafCRuntime.cstringAt(C.pd1, C.pd1Last) + "\n")
        }

        // ':' post conditionals (vendor :833-849)
        if (at(C.xd, C.xpx) == ':') {
            C.xpx++
            atFlgMD = atTest(C.xd, C.pd1, C.pd1Last, C.lines.toShort(), C.xpx)

            if (atFlgMD == 99) {
                lab = MLblM.ABRT_EXIT
                continue@machine
            }
            if (atFlgMD != 0) {
                val tmpA = CharArray(1024)
                copyStrAt(tmpA, 0, "i ")
                atCopyDaf(atFlgMD, tmpA)
                emitInterpretDaf(DafCRuntime.cstring(tmpA))
                C.xpx = atFlgMD
                C.emit("\tif (svPtr->tpx) { // m853 postconditional \n")
                C.POST_flg = 1
            } else {
                C.ierr = parseCDaf(C, svPtr)
                if (C.ierr != 0) {
                    lab = MLblM.PARSE_ERR
                    continue@machine
                }
                C.emit("\tif (atoi( CSTR " +
                        extract2Daf(DafCRuntime.cstringAt(C.pd1, C.sdlim)) +
                        ")) { // m859 postconditional\n")
                C.POST_flg = 1
            }
        }

        lab = MLblM.PSTRTN
        continue@machine
        }

        // -----------------------------------------------------------
        MLblM.PSTRTN -> {
        // -----------------------------------------------------------
        // pstrtn: (vendor :862-893) branch to command handler
        if (at(C.xd, C.xpx) != NUL_D) C.xpx++

        // if (0) { ... } main-gen block: verbatim-dead (kept
        // note-only; the vendor would emit the int main preamble,
        // fcninit, the label/text binds, try {, Sysout_skip, the
        // cgi.h include and the SymPut percent-argv loop here)
        if (false) {
            // kept verbatim-dead (see header; the if (0) guard)
        }

        val icmnd = at(C.xd, C.xpx).lowercaseChar().code

        when (icmnd) {
            '}'.code -> { // case CLSQ
                C.emit("\t} /* user close */\n")
                lab = MLblM.NEXT_CMND
                continue@machine
            }
            '{'.code -> { // case OPSQ
                lab = MLblM.NEXT_CMND
                continue@machine
            }
            't'.code -> { // case TRY
                C.emit("try { ")
                tryFlgMD = 1
                if (at(C.xd, C.xpx) == ' ' || at(C.xd, C.xpx) == NUL_D) {
                    lab = MLblM.NEXT_CMND
                    continue@machine
                }
                var iL = C.xpx
                while (at(C.xd, iL) != ' ' && at(C.xd, iL) != NUL_D) iL++
                val jS = at(C.xd, iL)
                C.xd[iL] = NUL_D
                if (DafCRuntime.cstringAt(C.xd, C.xpx) == "NoMessages") {
                    C.emit("svPtr->NOERR=1; ")
                } else {
                    lab = MLblM.TRY_UNKNOWN
                    continue@machine
                }
                C.xd[iL] = jS
                C.xpx = iL
                lab = MLblM.NEXT_CMND
                continue@machine
            }
            ';'.code -> { // case CMMNT
                p2pxD++
                if (at(C.xd, p2pxD) == ';') {
                    // TextStart chain: vendor malloc(size+2) with
                    // strcpy(name,&xd[p2px+1]) off-by-one kept as
                    // the String fold (quirk 8)
                    if (textStartD == null) {
                        pushTextTxtD(DafCRuntime.cstringAt(C.xd, p2pxD + 1), C.lines)
                        lab = MLblM.NEXT_LINE
                        continue@machine
                    }
                    pushTextTxtD(DafCRuntime.cstringAt(C.xd, p2pxD + 1), C.lines)
                    lab = MLblM.NEXT_LINE
                    continue@machine
                }
                lab = MLblM.NEXT_LINE
                continue@machine
            }
            'D'.code -> { // case FCN
                print("*** Not supported\n")
                print("*** $curLineM\n")
                exitCodeD = 99
                return abrtxDaf()
            }
            'n'.code, 'N'.code -> { // case NEW / NEW1
                C.emit("\n//--------------\n")
                C.emit("//       New\n")
                C.emit("//--------------\n\n")
                returnCode = ifCodeOrNewD(true) // new_code(); raised seat
                if (returnCode == 0) { lab = MLblM.NEXT_CMND; continue@machine }
                lab = commandErrLadder(returnCode)
                continue@machine
            }
            'b'.code -> { // case BREAK
                C.emit("\n//--------------\n")
                C.emit("//       Break\n")
                C.emit("//--------------\n\n")
                if (C.forLine != 0) {
                    lab = MLblM.NOBRK_ERR
                    continue@machine
                }
                C.emit("     svPtr->tpx=tpx${C.ExecStack[C.ExecX - 1]};" +
                        " // break code: restore \u0024test\n" +
                        "  longjmp(Efor${C.ExecStack[C.ExecX - 1]}J,100); \n")
                if (C.POST_flg != 0) {
                    C.emit(" } /* post conditional after break */\n")
                    C.POST_flg = 0
                }
                lab = MLblM.NEXT_CMND
                continue@machine
            }
            'l'.code -> { // case LOCK
                C.emit("\n//--------------\n")
                C.emit("//       Lock\n")
                C.emit("//--------------\n\n")
                returnCode = lockCodeDaf()
                if (returnCode == 0) { lab = MLblM.NEXT_CMND; continue@machine }
                lab = commandErrLadder(returnCode)
                continue@machine
            }
            'r'.code -> { // case READ
                C.emit("\n//--------------\n")
                C.emit("//       Read\n")
                C.emit("//--------------\n\n")
                returnCode = readCodeDaf()
                if (returnCode == 0) { lab = MLblM.NEXT_CMND; continue@machine }
                lab = commandErrLadder(returnCode)
                continue@machine
            }
            'o'.code -> { // case OPEN
                C.emit("\n//--------------\n")
                C.emit("//       Open\n")
                C.emit("//--------------\n\n")
                returnCode = mumpsOpenD()
                if (returnCode == 0) { lab = MLblM.NEXT_CMND; continue@machine }
                if (returnCode == PARSE_ERROR_D) { lab = MLblM.PARSE_ERR; continue@machine }
                if (returnCode == ARG_LIST_ERROR_D) { lab = MLblM.ARG_LIST_ERROR; continue@machine }
                lab = MLblM.COMMAND
                continue@machine
            }
            'z'.code -> { // case ZCMDS
                returnCode = zcommandsCodeD()
                if (returnCode == 0) { lab = MLblM.NEXT_CMND; continue@machine }
                if (returnCode == PARSE_ERROR_D) { lab = MLblM.PARSE_ERR; continue@machine }
                if (returnCode == ARG_LIST_ERROR_D) { lab = MLblM.ARG_LIST_ERROR; continue@machine }
                if (returnCode == NO_GLOBAL_PERMITTED_D) { lab = MLblM.NOGBLPERMITTED; continue@machine }
                if (returnCode == MEM_ERROR_D) { lab = MLblM.MEM_ERR; continue@machine }
                if (returnCode == TOO_MANY_LHS_D) { lab = MLblM.TOO_LHS; continue@machine }
                if (returnCode == ABRTX_D) return abrtxDaf()
                if (returnCode == NEXT_LINE_D) { lab = MLblM.NEXT_LINE; continue@machine }
                lab = MLblM.COMMAND
                continue@machine
            }
            'i'.code -> { // case IF
                C.emit("\n//--------------\n")
                C.emit("//       If\n")
                C.emit("//--------------\n\n")
                returnCode = ifCodeOrNewD(false) // if_code(); raised seat
                if (returnCode == 0) { lab = MLblM.NEXT_CMND; continue@machine }
                lab = commandErrLadder(returnCode)
                continue@machine
            }
            'w'.code -> { // case WRITE
                C.emit("\n//--------------\n")
                C.emit("//       Write\n")
                C.emit("//--------------\n\n")
                returnCode = writeCodeDaf()
                if (returnCode == 0) { lab = MLblM.NEXT_CMND; continue@machine }
                lab = commandErrLadder(returnCode)
                continue@machine
            }
            'j'.code -> { // case JOB
                C.emit("\n//--------------\n")
                C.emit("//       Job\n")
                C.emit("//--------------\n\n")
                linepadDaf(C, "\tif (fork()!=0) {", "I am child process if zero")
                jobFlgMD = 1
                lab = MLblM.DO_ENTRY
                continue@machine
            }
            'm'.code, 's'.code -> { // case MERGE (falls) / case SET
                if (icmnd == 'm'.code) {
                    C.emit("\n//--------------\n")
                    C.emit("//       Merge\n")
                    C.emit("//--------------\n\n")
                    C.MergeFlg = 1
                }
                // SHELL command (toupper(xd[p2px+1])=='H')
                if (at(C.xd, p2pxD + 1).uppercaseChar() == 'H') {
                    C.emit("\n//--------------\n")
                    C.emit("//       Shell\n")
                    C.emit("//--------------\n\n")
                    emitInterpretDaf(DafCRuntime.cstringAt(C.xd, p2pxD))
                    lab = MLblM.NEXT_LINE
                    continue@machine
                }
                // SQL (toupper(xd[p2px+1])=='Q')
                if (at(C.xd, p2pxD + 1).uppercaseChar() == 'Q') {
                    emitInterpretDaf(DafCRuntime.cstringAt(C.xd, p2pxD))
                    lab = MLblM.NEXT_LINE
                    continue@machine
                }
                C.emit("\n//--------------\n")
                C.emit("//       Set\n")
                C.emit("//--------------\n\n")
                C.retz = SET_CODE_SET_D // retz = SET;
                returnCode = setCodeDaf(SET_CODE_SET_D)
                if (returnCode == 0) { lab = MLblM.NEXT_CMND; continue@machine }
                lab = commandErrLadder(returnCode)
                continue@machine
            }
            'q'.code -> { // case QUIT (cntnue:)
                lab = MLblM.CNTNUE
                continue@machine
            }
            'x'.code -> { // case XECUTE
                C.emit("\n//--------------\n")
                C.emit("//       Xecute\n")
                C.emit("//--------------\n\n")
                run {
                    val tmpX = StringBuilder("x ")
                    var iL = C.xpx
                    while (at(C.xd, iL) != NUL_D) {
                        if (at(C.xd, iL) != '"') {
                            tmpX.append(at(C.xd, iL))
                        } else {
                            tmpX.append('\\').append('"')
                        }
                        iL++
                    }
                    emitInterpretDaf(tmpX.toString())
                    if (C.POST_flg != 0) {
                        C.emit("\t} // post conditional xecute\n")
                        C.POST_flg = 0
                    }
                }
                C.xpx = 1024
                lab = MLblM.NEXT_CMND
                continue@machine
            }
            'f'.code -> { // case FOR
                C.emit("\n//--------------\n")
                C.emit("//       For\n")
                C.emit("//--------------\n\n")
                returnCode = forCodeDaf()
                if (returnCode == 0) { lab = MLblM.NEXT_CMND; continue@machine }
                lab = commandErrLadder(returnCode)
                continue@machine
            }
            'h'.code -> { // case HALT and HANG
                if (at(C.xd, p2pxD + 1).uppercaseChar() != 'T' &&
                    at(C.xd, C.xpx) == ' ' ||
                    at(C.xd, C.xpx) == NUL_D) { // halt
                    C.emit("\n//--------------\n")
                    C.emit("//       Halt\n")
                    C.emit("//--------------\n\n")
                    if (mainFlgD != 0) C.emit("\tgoto _epilogue; \n")
                    if (fcnFlgD != 0) C.emit("\treturn \"\"; \n")
                    if (C.POST_flg != 0) {
                        C.emit("\t} /* post conditional halt*/\n")
                        C.POST_flg = 0
                    }
                    lab = MLblM.NEXT_CMND
                    continue@machine
                }
                if (at(C.xd, p2pxD + 1).uppercaseChar() == 'T') { // HTML
                    C.emit(" /* HTML */ printf(\"")
                    while (at(C.xd, C.xpx) != NUL_D) {
                        if (at(C.xd, C.xpx) == '&' && at(C.xd, C.xpx + 1) == '~') {
                            C.xpx += 2
                            C.emit("\");\n")
                            C.t2 = 1
                            var jL = C.xpx
                            while (at(C.xd, jL) != '~' && at(C.xd, jL) != NUL_D) jL++
                            C.xd[jL] = ' '
                            C.ierr = parseCDaf(C, svPtr)
                            if (C.ierr != 0) {
                                lab = MLblM.PARSE_ERR
                                continue@machine
                            }
                            C.emit("\tprintf(\"%s\", " +
                                    extract2Daf(DafCRuntime.cstringAt(C.pd1, C.sdlim)) +
                                    "); \n")
                            C.emit("\tprintf(\"")
                            C.xpx++
                            continue
                        }
                        if (at(C.xd, C.xpx) == '&' && at(C.xd, C.xpx + 1) == '!') {
                            C.xpx += 2
                            C.emit("\\n")
                            continue
                        }
                        when (at(C.xd, C.xpx)) {
                            '"' -> C.emit("\\\"")
                            '\'' -> C.emit("\\'")
                            '%' -> C.emit("%%")
                            '\\' -> C.emit("\\\\")
                            else -> C.emit(at(C.xd, C.xpx).toString())
                        }
                        C.xpx++
                    }
                    C.emit("\");\n")
                    lab = MLblM.NEXT_LINE
                    continue@machine
                }
                while (true) { // Hang
                    C.emit("\n//--------------\n")
                    C.emit("//       Hang\n")
                    C.emit("//--------------\n\n")
                    C.t2 = 1
                    C.ierr = parseCDaf(C, svPtr)
                    if (C.ierr != 0) {
                        lab = MLblM.PARSE_ERR
                        continue@machine
                    }
                    C.emit("\t_i = atoi( CSTR " +
                            extract2Daf(DafCRuntime.cstringAt(C.pd1, C.sdlim)) +
                            ");\n")
                    C.emit("\tif (_i > 0) sleep(_i);\n")
                    if (at(C.xd, C.xpx) == ',') { C.xpx++; continue }
                    if (C.POST_flg != 0) {
                        C.emit("\t} /* post conditional hang*/\n")
                        C.POST_flg = 0
                    }
                    lab = MLblM.NEXT_CMND
                    continue@machine
                }
                @Suppress("UNREACHABLE_CODE") return 0
            }
            'e'.code -> { // case ELSE
                if (at(C.xd, p2pxD + 1).lowercaseChar() == 'x') {
                    exportFlagD = 1
                }
                C.emit("\n//--------------\n")
                C.emit("//       Else\n")
                C.emit("//--------------\n\n")
                if (at(C.xd, C.xpx) == ' ') C.xpx++
                C.emit("\tif (svPtr->tpx == 0) { \n")
                C.IF_flg++
                lab = MLblM.NEXT_CMND
                continue@machine
            }
            'd'.code -> { // case DO (declares + do_entry fallthrough)
                if (at(C.xd, p2pxD + 1).uppercaseChar() == 'A') {
                    // SHELL command
                    val tmpS = StringBuilder()
                    var iL = p2pxD
                    while (at(C.xd, iL) != NUL_D) {
                        if (at(C.xd, iL) == '"') tmpS.append('\\')
                        tmpS.append(at(C.xd, iL))
                        iL++
                    }
                    emitInterpretDaf(tmpS.toString())
                    lab = MLblM.NEXT_LINE
                    continue@machine
                }
                if (at(C.xd, p2pxD + 1).lowercaseChar() == 'e') { // declare
                    var iL = 0
                    while (true) { // dcl loop
                        if (cvarxD > CVARMAX_D) {
                            lab = MLblM.TOO_MANY_VARS
                            continue@machine
                        }
                        while (at(C.xd, C.xpx) != ' ' &&
                               at(C.xd, C.xpx) != NUL_D &&
                               at(C.xd, C.xpx) != ',') {
                            cvarsD[cvarxD].name = cvarsD[cvarxD].name +
                                    at(C.xd, C.xpx)
                            C.xpx++
                        }
                        cvarxD++
                        if (at(C.xd, C.xpx) == ',') {
                            iL = 0
                            C.xpx++
                            continue
                        }
                        break
                    }
                    lab = MLblM.NEXT_CMND
                    continue@machine
                }
                lab = MLblM.DO_ENTRY
                continue@machine
            }
            'k'.code -> { // case KILL
                C.emit("\n//--------------\n")
                C.emit("//       Kill\n")
                C.emit("//--------------\n\n")
                returnCode = killCodeD()
                if (returnCode == 0) { lab = MLblM.NEXT_CMND; continue@machine }
                if (returnCode == KILL_DCL_ERROR_D) { lab = MLblM.KILL_DCL_ERR; continue@machine }
                if (returnCode == NEXT_LINE_D) { lab = MLblM.NEXT_LINE; continue@machine }
                lab = commandErrLadder(returnCode)
                continue@machine
            }
            'c'.code -> { // case CLOSE / CATCH ('c' both)
                C.emit("\n//--------------\n")
                C.emit("//       Close\n")
                C.emit("//--------------\n\n")
                p2pxD++
                if (at(C.xd, p2pxD).code <= 90)
                    C.xd[p2pxD] = (at(C.xd, p2pxD).code + 32).toChar()
                if (at(C.xd, p2pxD) == 'o') { lab = MLblM.CNTNUE; continue@machine }
                if (at(C.xd, p2pxD) == 'a') { lab = MLblM.CTCH; continue@machine }
                while (true) {
                    C.t2 = 1
                    C.ierr = parseCDaf(C, svPtr)
                    if (C.ierr != 0) { lab = MLblM.PARSE_ERR; continue@machine }
                    C.emit("\t_i=atoi( CSTR " +
                            extract2Daf(DafCRuntime.cstringAt(C.pd1, C.sdlim)) +
                            ");\n")
                    C.emit("\tif (svPtr->in_file[_i] == NULL && " +
                            "svPtr->out_file[_i] == NULL) \n")
                    C.emit("\t\tErrorMessage(\"Attempt to close unopened file\", " +
                            "svPtr->LineNumber); \n")
                    C.emit("\tif (svPtr->in_file[_i]!=NULL) { \n" +
                            "\tfclose(svPtr->in_file[_i]); " +
                            "svPtr->in_file[_i]=NULL; }\n")
                    C.emit("\telse { fclose(svPtr->out_file[_i]); " +
                            "svPtr->out_file[_i]=NULL; }\n")
                    if (at(C.xd, C.xpx) != ',') {
                        if (C.POST_flg != 0) {
                            C.emit("\t} /* post conditional 3240*/\n")
                            C.POST_flg = 0
                        }
                        lab = MLblM.NEXT_CMND
                        continue@machine
                    }
                    C.xpx++
                }
            }
            'u'.code -> { // case USE
                C.emit("\n//--------------\n")
                C.emit("//       Use\n")
                C.emit("//--------------\n\n")
                C.t2 = 1
                C.ierr = parseCDaf(C, svPtr)
                if (C.ierr != 0) { lab = MLblM.PARSE_ERR; continue@machine }
                C.emit("\tsvPtr->io=atoi( CSTR " +
                        extract2Daf(DafCRuntime.cstringAt(C.pd1, C.sdlim)) +
                        ");\n")
                if (C.POST_flg != 0) {
                    C.emit("\t} /* post conditional 3245*/\n")
                    C.POST_flg = 0
                }
                lab = MLblM.NEXT_CMND
                continue@machine
            }
            'g'.code -> { // case GOTO
                C.emit("\n//--------------\n")
                C.emit("//       Goto\n")
                C.emit("//--------------\n\n")
                if (at(C.xd, C.xpx) == NUL_D) {
                    lab = MLblM.LBL_ERROR
                    continue@machine
                }
                if (C.ExecLevel > 0) {
                    lab = MLblM.GOTO_IN_BLOCK
                    continue@machine
                }
                lab = MLblM.NEXT_GOTO
                continue@machine
            }
            else -> {
                lab = MLblM.COMMAND // default: command error
                continue@machine
            }
        }
        }

        // -----------------------------------------------------------
        MLblM.CNTNUE -> {
        // -----------------------------------------------------------
        // cntnue: / case QUIT (vendor :1192-1260)
        C.emit("\n//--------------\n")
        C.emit("//       Quit\n")
        C.emit("//--------------\n\n")

        if (C.POST_flg != 0) {
            if (C.ForBlocks[C.IF_flg] <= 0)
                C.emit("\tbreak; } // post conditional quit m2386 \n")
            else
                C.emit("\tlongjmp(for${C.ForBlocks[C.IF_flg]}J,100); /* m1146 quit*/ } // m1146 quit\n\n")
            C.POST_flg = 0
            lab = MLblM.NEXT_CMND
            continue@machine
        }

        if (C.ExecLevel == 0 && C.forxFLG == 0) {
            C.emit("\tif (svPtr->Rargsx) { // call-by-reference? \n")
            C.emit("\t\tint i; \n")
            C.emit("\t\tunsigned char *tmp = new unsigned char[STR_MAX]; \n")
            C.emit("\t\tfor (i = 0; i < svPtr->Rargsx; i++) { \n")
            C.emit("\t\tsym_(1, (UNS) svPtr->RefParm[i], (UNS) tmp,svPtr); \n")
            C.emit("\t\tsym_(${SYMSTORE_D}, (UNS) svPtr->Rargs[i], (UNS) tmp, svPtr); \n")
            C.emit("\t\t} \n")
            C.emit("\t\t} \n")
            C.emit("\tstrmove((UNS) _ReturnVal, (UNS) \"\"); \n")
            if (at(C.xd, C.xpx) != ' ' && at(C.xd, C.xpx) != NUL_D) {
                C.t2 = 1
                C.ierr = parseCDaf(C, svPtr)
                if (C.ierr != 0) { lab = MLblM.PARSE_ERR; continue@machine }
                C.emit("\tstrmove((UNS) _ReturnVal, (UNS) " +
                        extract2Daf(DafCRuntime.cstringAt(C.pd1, C.sdlim)) +
                        "); \n")
            }
            if (fFlgD != 0) {
                C.emit("\treturn (char *) _ReturnVal; \n")
            } else {
                C.emit("\tif (_doStackN[_doStx]) {  _doStackN[_doStx] = 0; } // Q1001 \n")
                C.emit("\tif (_doStx > 0) { longjmp(_doStack[_doStx],99); } \n")
                C.emit("\telse ErrorMessage(\"Too Many Quits\", svPtr->LineNumber); \n")
            }
        } else {
            if (C.IF_flg == 0 || C.ForBlocks[C.IF_flg] < 0) C.emit("\tbreak; \n")
            else C.emit("\tgoto For${C.ForBlocks[C.IF_flg]}Exit; \n")
            if (C.Blocks[C.IF_flg] == IF_M) {
                C.Blocks[C.IF_flg] = 0
                C.IF_flg--
                C.emit("\t} // if block 2389\n")
            }
        }
        lab = MLblM.NEXT_CMND
        continue@machine
        }

        // -----------------------------------------------------------
        MLblM.CTCH -> {
        // -----------------------------------------------------------
        // ctch: (vendor :939-957) the catch() arm
        C.emit("catch(")
        catchFlgMD = 1
        tryFlgMD = 0
        if (at(C.xd, C.xpx) == ' ' || at(C.xd, C.xpx) == NUL_D) {
            lab = MLblM.CATCH_ERROR
            continue@machine
        }
        var iL = C.xpx
        while (at(C.xd, iL) != ' ' && at(C.xd, iL) != NUL_D) iL++
        val jS = at(C.xd, iL)
        C.xd[iL] = NUL_D
        var flg = 1
        val wd = DafCRuntime.cstringAt(C.xd, C.xpx)
        for (k in EXCEPT_M.indices) {
            if (EXCEPT_M[k].isEmpty()) break
            if (wd == EXCEPT_M[k]) { flg = 0; break }
        }
        if (flg != 0) {
            lab = MLblM.CATCH_UNKNOWN
            continue@machine
        }
        C.emit("$wd ) {\n")
        C.xd[iL] = jS
        C.xpx = iL
        lab = MLblM.NEXT_CMND
        continue@machine
        }

        // -----------------------------------------------------------
        MLblM.DO_ENTRY -> {
        // -----------------------------------------------------------
        // do_entry: (vendor :1449-1630) Do banners, the argumentless
        // Do arm, the another_do argumented loop (the vendor label
        // is the loop head: the D1000 emit re-executes on every
        // another_do re-entry, kept verbatim)
        C.emit("\n//--------------\n")
        C.emit("//       Do\n")
        C.emit("//--------------\n\n")

        if (at(C.xd, C.xpx) == NUL_D || at(C.xd, C.xpx) == ' ') {
            // argumentless DO
            if (jobFlgMD != 0) { // is this really a JOB command?
                print("*** JOB requires argument\n")
                lab = MLblM.ARG_LIST_ERROR
                continue@machine
            }
            C.emit("\t\ttpx${C.ExecName} = svPtr->tpx; \n")
            C.emit("\t\tif ( (_i = setjmp(Efor${C.ExecName}J)) == 0) ")
            C.emit("goto Do_Block_Entry${C.ExecName}; //else continue \n")
            if (C.IF_flg != 0)
                C.emit("\t\telse if (_i == 100) goto Do_Block_Bypass${C.ExecName}; // IF_flg m1402\n")
            C.forx++
            C.DoRet[C.DoRetX] = C.ExecName
            C.DoRetX++
            if (C.POST_flg != 0) {
                C.emit("\t} // post conditional do\n")
                C.POST_flg = 0
            }
            lab = MLblM.NEXT_CMND
            continue@machine
        }

        // argumented DO: another_do loop (label head)
        val ttt = CharArray(2048)
        ado@ while (true) {
            C.emit("\t_ArgCount=0;  //section D1000 \n")
            C.emit("\tsvPtr->Rargsx=0;\n")

            val iS = C.xpx
            java.lang.System.arraycopy(C.xd, 0, ttt, 0,
                2048.coerceAtMost(C.xd.size))

            // scan through label for the non-quoted colon
            var jS = C.xpx
            var kQ = 0
            while (true) {
                if (at(C.xd, jS) == NUL_D) break
                if (at(C.xd, jS) == ',' && kQ == kQ / 2 * 2) break
                if (at(C.xd, jS) == ':' && kQ == kQ / 2 * 2) break
                if (at(C.xd, jS) == '"') {
                    if (at(C.xd, jS + 1) == '"') { jS = jS + 1 } else kQ++
                }
                jS++
            }
            if (at(C.xd, jS) == ':' && kQ == kQ / 2 * 2) {
                C.xpx = jS + 1
                C.t0px = 0
                C.t2 = 1
                C.ierr = parseCDaf(C, svPtr)
                if (C.ierr != 0) { lab = MLblM.PARSE_ERR; break@ado }
                C.emit("\tif (atoi( CSTR " +
                        extract2Daf(DafCRuntime.cstringAt(C.pd1, C.sdlim)) +
                        ")) { // postconditional \n")
                C.t0px = 0
                C.t2 = 0
                C.xpx = iS
                java.lang.System.arraycopy(ttt, 0, C.xd, 0,
                    2048.coerceAtMost(C.xd.size))
            } else {
                C.emit("\t{\n")
            }

            // '@' indirection in DO argument
            if (at(C.xd, C.xpx) == '@') {
                atFlgMD = atTest(C.xd, C.pd1, C.pd1Last,
                    C.lines.toShort(), C.xpx)
                if (atFlgMD == -1) { lab = MLblM.ABRT_EXIT; break@ado }
                if (atFlgMD != 0) {
                    val tmpD = CharArray(1024)
                    copyStrAt(tmpD, 0, "d ")
                    atCopyDaf(atFlgMD, tmpD)
                    emitInterpretDaf(DafCRuntime.cstring(tmpD))
                    C.xpx = atFlgMD
                    C.emit("       } /* post conditional 1920*/\n")
                    if (C.POST_flg != 0) {
                        C.emit("       } /* post conditional 1920*/\n")
                        C.POST_flg = 0
                    }
                    lab = MLblM.NEXT_CMND
                    break@ado
                }
            }

            // the dollar-intrinsic argument arm (xd[xpx]=='\u0024'
            // && isalnum)
            if (at(C.xd, C.xpx) == '\u0024' &&
                at(C.xd, C.xpx + 1).isLetterOrDigit()) {
                C.t0px = 0
                C.t2 = 1
                C.ierr = parseCDaf(C, svPtr)
                if (C.ierr != 0) { lab = MLblM.PARSE_ERR; break@ado }
                C.emit("\t} \n")
            } else if (at(C.xd, C.xpx) != '@') {
                C.xd[C.xpx - 2] = '\u0024'
                C.xd[C.xpx - 1] = '\u0024'
                C.xpx = C.xpx - 2
                C.t2 = 1
                C.ierr = parseCDaf(C, svPtr)
                if (C.ierr != 0) { lab = MLblM.PARSE_ERR; break@ado }
                C.t2 = 0
                C.emit("\t} \n ")
            }

            // bypass post-cond, look for the non-quoted,
            // non-paren comma
            if (at(C.xd, C.xpx) == ':') {
                var jQ: Int
                var kP = 0
                var mM = 0
                C.xpx++
                jQ = C.xpx
                while (true) {
                    if (at(C.xd, jQ) == NUL_D) break
                    if (at(C.xd, jQ) == ',' && mM == 0 && kP == kP / 2 * 2) break
                    if (at(C.xd, jQ) == ' ' && mM == 0 && kP == kP / 2 * 2) break
                    if (at(C.xd, jQ) == '(' && kP == kP / 2 * 2) mM++
                    if (at(C.xd, jQ) == ')' && kP == kP / 2 * 2) mM--
                    if (at(C.xd, jQ) == '"') {
                        if (at(C.xd, jQ + 1) == '"') jQ = jQ + 1 else kP++
                    }
                    jQ++
                }
                if (at(C.xd, jQ) == ',' && mM == 0 && kP == kP / 2 * 2) {
                    C.xpx = jQ + 1
                    continue@ado // goto another_do
                }
                C.xpx = jQ
            }

            if (at(C.xd, C.xpx) == ',') {
                C.xpx++
                continue@ado // goto another_do
            }

            if (C.POST_flg != 0) { // end of post-cond
                C.emit("\t} /* post conditional 3001*/\n")
                C.POST_flg = 0
            }

            if (jobFlgMD != 0) { lab = MLblM.JOB_RETURN; break@ado }

            lab = MLblM.NEXT_CMND
            break@ado
        }
        // (the vendor dead-tail while (1) parse_ loop below the
        // flow above: unreachable on every vendor path ended in
        // goto; comment-only ledger)
        continue@machine
        }

        MLblM.JOB_RETURN -> {
        // -----------------------------------------------------------
        C.emit("\t}")
        lab = MLblM.NEXT_LINE
        continue@machine
        }

        // -----------------------------------------------------------
        MLblM.NEXT_GOTO -> {
        // -----------------------------------------------------------
        // next_goto: (vendor :1720-1769) inside case GOTO
        // hoisted local register (vendor static dosave analog)
        val dosaveG = CharArray(STR_MAX_DAF)
        var iG = 0
        while (at(C.xd, C.xpx) != NUL_D) {
            if (at(C.xd, C.xpx) == ',' || at(C.xd, C.xpx) == ' ' ||
                at(C.xd, C.xpx) == NUL_D || at(C.xd, C.xpx) == '+' ||
                at(C.xd, C.xpx) == ':') break
            dosaveG[iG++] = at(C.xd, C.xpx)
            C.xpx++
        }

        if (at(C.xd, C.xpx) == ':') { // post conditionals
            C.xpx++
            C.t0px = 0
            C.t2 = 1
            C.ierr = parseCDaf(C, svPtr)
            if (C.ierr != 0) { lab = MLblM.PARSE_ERR; continue@machine }
            C.emit("\tif (atoi((char *) " +
                    extract2Daf(DafCRuntime.cstringAt(C.pd1, C.sdlim)) +
                    ")) /* m1635 postconditional*/")
            C.t0px = 0
            C.t2 = 0
        }

        dosaveG[iG] = NUL_D

        var iP = 0
        while (iP < dosaveG.size && dosaveG[iP] != NUL_D &&
               dosaveG[iP] != '^' && dosaveG[iP] != '+') iP++

        if (dosaveG[iP] == '+' || dosaveG[iP] == '(') {
            lab = MLblM.LBL_ERROR
            continue@machine
        }

        var tmp2G = ""
        if (iP >= dosaveG.size || dosaveG[iP] == NUL_D) {
            // simple goto
            val dsv = DafCRuntime.cstring(dosaveG)
            if (dsv.isNotEmpty() && dsv[0].isDigit())
                C.emit("\tgoto _$dsv; \n")
            else
                C.emit("\tgoto $dsv; \n")
            // fall to gexit
        } else {
            if (dosaveG[0] != '^') { // label appears first in ^ ref
                dosaveG[iP] = NUL_D
                tmp2G = DafCRuntime.cstring(dosaveG)
                val tail = DafCRuntime.cstringAt(dosaveG, iP + 1)
                copyStrAt(dosaveG, 1, tail)
                dosaveG[0] = '^'
            }
            val dsv = DafCRuntime.cstring(dosaveG)
            C.emit("\t_jj=Interpret(\"x \\\"goto ^" +
                    DafCRuntime.cstringAt(dosaveG, 1) + "\\\"\",svPtr); \n")
            C.emit("\tgoto _epilogue; /* if fcn returns, this is end program */\n")
        }

        // gexit:
        if (at(C.xd, C.xpx) == ',') {
            C.xpx++
            lab = MLblM.NEXT_GOTO // goto next_goto
            continue@machine
        }
        if (C.POST_flg != 0) {
            C.emit("\t} /* post conditional 3431*/\n")
            C.POST_flg = 0
        }
        lab = MLblM.NEXT_CMND
        continue@machine
        }

        // -----------------------------------------------------------
        // error label block (vendor :1808-1935); each arm sets
        // ernbr, prints exactly the vendor text, falls to abrt1
        // -----------------------------------------------------------
        MLblM.KILL_DCL_ERR -> {
            ernbrMD = 99
            print("\n*** Attempt to Kill declared variable\n")
            lab = MLblM.ABRT1
            continue@machine
        }
        MLblM.PARSE_ERR -> {
            // ernbr = ierr; errmod_(ierr) folds to the seat ledger
            // (unit-243: the emitInterpretDaf error ladder)
            ernbrMD = C.ierr
            if (ernbrMD == 9001) print("*** Invalid global array reference")
            if (ernbrMD == 9002) print("*** Invalid routine reference")
            if (ernbrMD == 9003) print("*** Wrong number of args to \u0024extract()")
            if (ernbrMD == 12) print("Invalid operator: ${svPtr.ErrChar}")
            lab = MLblM.ABRT1
            continue@machine
        }
        MLblM.NOBRK_ERR -> {
            ernbrMD = 99
            print("\n*** Break not permitted here")
            lab = MLblM.ABRT1
            continue@machine
        }
        MLblM.LBL_ERROR -> {
            ernbrMD = 8
            print("\n*** Label or file not found")
            lab = MLblM.ABRT1
            continue@machine
        }
        MLblM.LAB_ERR -> {
            ernbrMD = 22
            print("\n*** Labels must begin with and alphabetic character")
            lab = MLblM.ABRT1
            continue@machine
        }
        MLblM.BAD_LOCK -> {
            ernbrMD = 99
            print("*** Lock error")
            lab = MLblM.ABRT1
            continue@machine
        }
        MLblM.COMMAND -> {
            ernbrMD = 13
            print("\n*** 13 Command Error")
            lab = MLblM.ABRT1
            continue@machine
        }
        MLblM.ARG_LIST_ERROR -> {
            ernbrMD = 14
            print("\n*** 14 Argument list error")
            lab = MLblM.ABRT1
            continue@machine
        }
        MLblM.TOO_MANY_VARS -> {
            ernbrMD = 91
            print("\n*** 91 Too many C variables")
            lab = MLblM.ABRT1
            continue@machine
        }
        MLblM.MISSING_ZMAIN -> {
            ernbrMD = 92
            print("\n*** 92 Missing zmain")
            lab = MLblM.ABRT1
            continue@machine
        }
        MLblM.NOGBLPERMITTED -> {
            ernbrMD = 92
            print("\n*** 92 Global not permitted")
            lab = MLblM.ABRT1
            continue@machine
        }
        MLblM.TOO_LHS -> {
            ernbrMD = 9033
            print("\n*** 9033 Too many variables in LHS")
            lab = MLblM.ABRT1
            continue@machine
        }
        MLblM.MEM_ERR -> {
            ernbrMD = 19
            print("\n*** 19 Out of memory")
            lab = MLblM.ABRT1
            continue@machine
        }
        MLblM.CATCH_ERROR -> {
            ernbrMD = 122
            print("\n*** 122 Missing catch argument")
            lab = MLblM.ABRT1
            continue@machine
        }
        MLblM.CATCH_UNKNOWN -> {
            ernbrMD = 123
            print("\n*** 123 Unknown exception in catch")
            lab = MLblM.ABRT1
            continue@machine
        }
        MLblM.TRY_UNKNOWN -> {
            ernbrMD = 124
            print("\n*** 124 Unknown option in try")
            lab = MLblM.ABRT1
            continue@machine
        }
        MLblM.LABEL_IN_BLOCK -> {
            ernbrMD = 125
            print("\n*** 125 Label not allowed in block")
            lab = MLblM.ABRT1
            continue@machine
        }
        MLblM.GOTO_IN_BLOCK -> {
            ernbrMD = 126
            print("\n*** 126 Goto not allowed in block")
            lab = MLblM.ABRT1
            continue@machine
        }
        MLblM.ABRT1 -> {
            print(" in or near line ${C.lines}\n")
            print("***\t" + DafCRuntime.cstringAt(C.pd1, C.pd1Last) + " \n\n")
            exitCodeD = ernbrMD
            inF?.close()
            return 9
        }
        MLblM.ABRT_EXIT -> {
            return abrtxDaf()
        }
        }
    }
}

// ---------------------------------------------------------------------
// unit-local helpers (comment-headed)
// ---------------------------------------------------------------------

// short SymIncrFlg - main-TU carrier (jit-style raise; the parse_
// family reads it through the shared DafCompilerState fold per the
// unit-209 fcninit ledger; local to this render)
private var symIncrFlgMD = 0

// int at_flg - the atTest/xpx carrier re-used across machine states.
private var atFlgMD = 0

// compiler-macros.h IF 'i' - the Blocks[] type tag of the vendor
// (row-local pin; ForCode pinned the FOR_D twin already).
private const val IF_M = 'i'.code

// isalnum at the CharArray cell.
private fun isAlnumAt(buf: CharArray, off: Int): Boolean {
    if (off < 0 || off >= buf.size) return false
    return buf[off].isLetterOrDigit()
}

// strcpy analog into a CharArray at offset (local, bounds-bounded).
private fun copyStrAt(dst: CharArray, off: Int, s: String) {
    var i = 0
    val n = dst.size - 1 - off
    while (i < s.length && i < n) { dst[off + i] = s[i]; i++ }
    dst[off + i] = NUL_D
}

// the NEW/IF raised-seat splitter: both vendor helpers are named
// raises (new_code/if_code family, never opened on this arm); the
// Boolean selects the seat so the two vendor switch arms stay
// distinguished at the call site.
private fun ifCodeOrNewD(isNew: Boolean): Int =
    if (isNew) newCodeDaf() else ifCodeDaf()

// the common command return-code ladder of the vendor arms (the
// parse/arg/noglob/mem/too-many/abrt ladder; KILL adds
// KILL_DCL_ERROR at the caller).
private fun commandErrLadder(rc: Int): MLblM {
    return when (rc) {
        PARSE_ERROR_D -> MLblM.PARSE_ERR
        ARG_LIST_ERROR_D -> MLblM.ARG_LIST_ERROR
        NO_GLOBAL_PERMITTED_D -> MLblM.NOGBLPERMITTED
        MEM_ERROR_D -> MLblM.MEM_ERR
        TOO_MANY_LHS_D -> MLblM.TOO_LHS
        ABRTX_D -> MLblM.ABRT_EXIT
        else -> MLblM.COMMAND
    }
}
