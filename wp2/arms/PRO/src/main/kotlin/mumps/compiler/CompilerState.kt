package mumps.compiler

import java.io.Writer

/**
 * PRO-arm owner object for the compiler's file-scope globals
 * (h004 globals_ext; source header include/mumpsc/compiler-globals-ext.h
 * — one owner object per C translation unit's global set). Each property
 * records its header line; byte-domain arrays map to Char(0..255)
 * per h012.
 *
 * Independent translation from the pristine vendor source
 * (mumps-14-may-2026.tgz); not derived from any other arm's output.
 */
object CompilerState {

    /** memsize.h:32-33 — PS=4096, PS1=PS+1. */
    const val PS1 = 4096 + 1

    /** compiler-macros.h:124. */
    const val FBMAX = 4000

    /** compiler-globals-ext.h:106 — FILE* Out; PrintWriter only at edges (h001). */
    lateinit var out: Writer

    var tpx: Short = 0                            // :34
    var t2: Short = 0                             // :35
    var pd1len: Short = 0                         // :36
    var xpx: Long = 0                             // :38
    var ierr: Short = 0                           // :40
    var sdlim: Short = 0                          // :42
    var pd1: CharArray = CharArray(PS1)           // :43 (h012 byte domain)
    var xd: CharArray = CharArray(2048)           // :44 (h012 byte domain)

    /** :45 - extern unsigned char v1d[2048] (h012 byte domain; owner added by unit 21). */
    var v1d: CharArray = CharArray(2048)
    var ierr1: Short = 0                          // :48
    var IF_flg: Int = 0                           // :112
    var IF_flg_max: Int = 0                       // :113
    var ForBlocks: ShortArray = ShortArray(FBMAX) // :115
    var Blocks: ShortArray = ShortArray(FBMAX)    // :116
    var lines: Int = 0                            // :118
    var pd1cur: Short = 0                         // :121
    var i: Short = 0                              // :121
    var j: Short = 0                              // :121
    var k: Short = 0                              // :121
    var cmdx: Short = 0                           // :124
    var pd1Last: Short = 0                        // :124
    var POST_flg: Short = 0                       // :124
    var at_flg: Short = 0                         // :136 (C: short at_flg = int atTest)
    var Set_flg: Short = 0                        // :136

    // --- compiler-abrtx.c unit 18 additions (h004; header lines) ---

    /** compiler-globals-ext.h:106 - FILE *Final (Writer at edges, h001). */
    lateinit var Final: java.io.Writer

    /** :51 - extern short MAIN_flg. */
    var MAIN_flg: Short = 0

    /** :117 - extern short FCN_flg. */
    var FCN_flg: Short = 0

    /** :141 - extern int F_flg. */
    var F_flg: Int = 0

    /** :107 - extern short ExitCode. */
    var ExitCode: Short = 0

    /** :128 - extern short ExecName. */
    var ExecName: Short = 0

    /** :158 - extern long pid. */
    var pid: Long = 0

    /** :170 - extern unsigned char output_file[1024] (h012). */
    var output_file: CharArray = CharArray(1024)

    /**
     * h003 state-macro: vendor INTERPRET (compiler-macros.h:39-44),
     * a multi-emission macro writing via the shared Out stream, with the
     * caller's local tmp spliced into the second emission. Member of the
     * state singleton; one-line call sites preserved.
     */
    fun interpret(tmp: String) {
        out.write("\t_jj = Interpret( (CCHR) \n")
        out.write("\t\t\"" + tmp + "\", svPtr); \n")
        out.write("\tsvPtr->ERROR = _jj; \n")
        out.write("\tif (!svPtr->NOERR && _jj < 0) \n")
        out.write("\t\tErrorMessage(\"Indirection error\", svPtr->LineNumber); \n")
    }
}
