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

    // --- scanner / parse state ---
    var xd: String = ""            // source line buffer (scan-only role)
    var xpx: Long = 0              // index into xd (C: long)
    var pd1: StringBuilder = StringBuilder()  // parse workspace (mutable-scratch role)
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
