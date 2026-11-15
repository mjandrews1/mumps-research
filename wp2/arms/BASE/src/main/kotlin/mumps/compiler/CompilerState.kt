package mumps.compiler

import java.io.Writer

/**
 * Compiler state: Kotlin translation of the C compiler's file-scope globals
 * (include/mumpsc/compiler-globals-ext.h). Grows unit by unit during the
 * pilot; properties are added when first needed (BASE arm, ad hoc).
 */
object CompilerState {

    // --- output streams (C: extern FILE *Out, *Final) ---
    lateinit var Out: Writer
    lateinit var Final: Writer

    // --- abrtx / command flags (C: compiler-globals-ext.h) ---
    var MAIN_flg: Short = 0
    var FCN_flg: Short = 0
    var F_flg: Short = 0
    var ExitCode: Short = 0
    var ExecName: Short = 0
    var pid: Long = 0
    var outputFile: String = ""

    // --- scanner / parse state ---
    var xd: String = ""            // source line buffer (scan-only role)
    var xpx: Long = 0              // index into xd (C: long)
    var pd1: StringBuilder = StringBuilder()  // parse workspace (mutable-scratch role)
    /** C: extern unsigned char bd[] — compiler output workspace. Kotlin bd
     *  models C bd+1 (callers strcat into &bd[1]; bd[0] is a sentinel byte).
     *  First needed by unit 26 patrn1. */
    val bd: StringBuilder = StringBuilder()
    /** C: extern unsigned char v1d[] — expression workspace (compiler-parse.c
     *  globals; first needed by unit 21 LocalArray's v1d[1] check). */
    val v1d: StringBuilder = StringBuilder()
    var pd1Last: Short = 0
    var lines: Short = 0
    var sdlim: Short = 0

    // --- command flags / counters ---
    var IF_flg: Int = 0
    var tpx: Short = 0
    var t2: Short = 0
    var ierr: Short = 0
    var POST_flg: Boolean = false
    var at_flg: Int = 0

    // --- block stack (C: extern short ForBlocks[FBMAX], Blocks[FBMAX]) ---
    val ForBlocks = IntArray(FBMAX)
    val Blocks = IntArray(FBMAX)

    // --- constants (C: compiler-macros.h) ---
    const val ABRTX = 6
    const val PARSE_ERROR = 1
    const val ARG_LIST_ERROR = 2
    const val FBMAX = 4000
    /** C: #define IF 'i' — value seen by compiler-if.c (compiler-macros.h). */
    const val IF_TAG = 'i'.code

    // --- INTERPRET macro (compiler-macros.h), with tmp substituted ---
    fun interpret(tmp: String) {
        val out = Out
        out.write("\t_jj = Interpret( (CCHR) \n")
        out.write("\t\t\"" + tmp + "\", svPtr); \n")
        out.write("\tsvPtr->ERROR = _jj; \n")
        out.write("\tif (!svPtr->NOERR && _jj < 0) \n")
        out.write("\t\tErrorMessage(\"Indirection error\", svPtr->LineNumber); \n")
    }
}
