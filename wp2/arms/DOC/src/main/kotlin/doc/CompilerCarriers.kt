package doc

// Compiler-side extern carriers (arm scaffolding, opened with batch-2
// unit 52 and extended by unit 54). One owner object per the cookbook
// one-owner-per-C-entity rule (H-004); each property names its vendor
// source line.
//
// Emission helper: the vendor fprintf returns the number of chars
// written and the compiler code passes that count to linepad
// (compiler-if.c:70 shape); emit() returns the char count for that
// idiom.

object CompilerState {
    // extern short ArgMax (compiler-globals.h:53); int seat per the
    // H-010 promotion note (short ext carrier in Kotlin Int renders)
    var ArgMax: Int = 0

    // extern FILE *Out (compiler-parse.c:166); the Writer is assigned by
    // the owning initialization units of later batches (H-001 FILE*
    // to Writer).
    var Out: java.io.Writer = java.io.PrintWriter(java.io.OutputStreamWriter(
        java.lang.System.out), true)

    // fprintf(Out, ...) emission with the fprintf char-count return.
    fun emit(text: String): Int {
        Out.write(text)
        return text.length
    }

    // interpret(tmp): the INTERPRET stateful macro
    // (include/mumpsc/compiler-macros.h:39) - five sequential
    // emissions reading Out and tmp (cookbook H-003 example itself).
    fun interpret(tmp: String) {
        Out.write("\t_jj = Interpret( (CCHR) \n")
        Out.write("\t\t\"" + tmp + "\", svPtr); \n")
        Out.write("\tsvPtr->ERROR = _jj; \n")
        Out.write("\tif (!svPtr->NOERR && _jj < 0) \n")
        Out.write("\t\tErrorMessage(\"Indirection error\", svPtr->LineNumber); \n")
    }

    // include/mumpsc/compiler-globals-ext.h:109 - count on temp strings
    // used so far in this statement (initializer 0, vendor
    // compiler-globals.h:109).
    var iiitmp: Int = 0

    // compiler-globals-ext.h:38 extern long xpx (Kotlin array indexing
    // takes Int; the narrowing helper documents each use).
    var xpx: Long = 0
    fun xpxIdx(): Int = xpx.toInt()
    fun xpxInc() { xpx++ }

    // compiler-globals-ext.h:35 extern short t2 (H-010 explicit widening)
    var t2: Int = 0
    // compiler-globals-ext.h:40 extern short ierr
    var ierr: Int = 0
    // compiler-globals-ext.h:42 extern short sdlim
    var sdlim: Int = 0
    // compiler-globals-ext.h:36 extern short pd1len (line length of
    // the pd1 read; int seat per the H-010 promotion note)
    var pd1len: Int = 0
    // compiler-globals-ext.h:43 extern unsigned char pd1[PS1]
    // (PS1 32001, sysparms.h:75 - the live sizing)
    val pd1 = CharArray(PS1)
    // compiler-globals.h:124 extern short pd1Last (atTest out-param
    // contract, compiler-globals.h:103 - H-006 holder array)
    val pd1Last = ShortArray(1)
    // compiler-globals-ext.h:44 extern unsigned char xd[2048]
    val xd = CharArray(2048)
    // compiler-globals-ext.h:112 extern int IF_flg
    var IF_flg: Int = 0
    // compiler-globals.h:124 extern short POST_flg
    var POST_flg: Int = 0
    // compiler-globals.h:136 extern short at_flg
    var at_flg: Int = 0
    // compiler-globals-ext.h:118 extern int lines
    var lines: Int = 0
    // compiler-globals-ext.h:115/116 extern short ForBlocks[FBMAX],
    // Blocks[FBMAX] (FBMAX 4000, compiler-macros.h:124)
    val ForBlocks = ShortArray(4000)
    val Blocks = CharArray(4000)
    // compiler-globals-ext.h:34 extern short int tpx
    var tpx: Int = 0

    // compiler-globals externs used by the abrtx epilogue
    // (compiler-abrtx.c; MAIN_flg/FCN_flg/F_flg/ExitCode externs,
    // ExecName extern char *, pid extern int), added by batch-1
    // unit 18 (owner named per H-004 single-owner rule).
    var MAIN_flg: Int = 0
    var FCN_flg: Int = 0
    var F_flg: Int = 0
    var ExitCode: Int = 0
    var ExecName: String = ""
    var pid: Int = 0
    // compiler-globals-ext.h extern unsigned char v1d[] (the vendor
    // extern scanned by LocalArray; global-array parsing buffer).
    val v1d = CharArray(STR_MAX)
    // compiler-globals-ext.h extern unsigned char bd[] (the pattern
    // argument scratch, appended at offset 1 by patrn1).
    val bd = CharArray(STR_MAX)
    // compiler-globals-ext.h:126 extern short exportflag (def
    // compiler-globals.h:125; unit 91 seat;
    // set by the SET pass, read by the NEW mark-copy emission).
    var exportflag: Int = 0
    // compiler-globals-ext.h:130 extern short ExecSym[25] (the
    // per-level Exec mark table of the command compilers; unit 91
    // seat, H-010 promotion note: short array rides ShortArray).
    val ExecSym = ShortArray(25)
    // compiler-globals-ext.h:131 extern short ExecX (the Exec level
    // index paired with ExecSym; unit 91 seat, H-010 Int).
    var ExecX: Int = 0
    // compiler-globals-ext.h:169 extern short retz (the set entry-point
    // command token staged for the SET call; unit 91 seat, H-010 Int).
    var retz: Int = 0
    // compiler-globals-ext.h:39 extern short t0px (the set-call tail
    // flag; unit 91 seat, H-010 Int).
    var t0px: Int = 0
    // compiler-globals-ext.h:170 extern unsigned char vd[1024] (the
    // set-code variable-name buffer; H-007 sizing: carried at STR_MAX
    // like bd/v1d, the vendor 1024 carries only fixed-name loads -
    // divergence ledgered here, unit 91 seat).
    val vd = CharArray(STR_MAX)
    // compiler output_file name carrier (the abrtx name surgery
    // drop-last-2 + suffix append renders over the String content).
    var output_file: String = ""
    // compiler-globals.h:136 extern short LockFlag (set by the
    // LOCK command compiler; unit-116 lock_code seat, H-010 Int).
    var LockFlag: Int = 0
    // compiler-globals.h:170 extern unsigned char dirflg (the
    // directive direction flag; unit-116 lock_code seat, Char
    // render of the byte domain)
    var dirflg: Char = NUL
}

// sysparms.h:74-75 live PS/PS1 sizes (the memsize.h 4096 twins are the
// vendor FIXME mismatch, kept as a roster note)
const val PS: Int = 32000
const val PS1: Int = 32001

// compiler-macros.h:95 define IF - the reference-build opcode for the
// IF command block table (Blocks assignment of unit 54)
const val IF_OPCODE: Char = 'i'
