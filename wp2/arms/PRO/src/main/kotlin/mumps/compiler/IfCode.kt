package mumps.compiler

import java.io.IOException

/**
 * PRO-arm translation of compiler-if.c (pilot B1, unit 2; the file's
 * single function if_code is re-listed as B2 unit 54). Translated
 * independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe).
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h001 io_stream_param     : Out emitted via java.io.Writer.
 *  - h003 state_macro         : INTERPRET macro -> CompilerState.interpret.
 *  - h004 globals_ext         : file-scope globals on CompilerState.
 *  - h005 out_param_bundle    : atTest pd1Last out-param -> AtTestResult.
 *  - h007 char_buffer         : char tmp[1024] -> StringBuilder scratch.
 *  - h010 numeric_promotion   : C's int-to-short assignment of atTest's
 *                               return becomes an explicit toShort().
 *  - h011 goto                : forward goto if_structure -> flag;
 *                               backward goto if_again -> while(true).
 *  - h012 unsigned_byte_domain: xd/pd1 bytes map to Char(0..255).
 *  - h014 out_of_scope_call   : atTest, atCopy, parse_, extract2,
 *                               linepad -> Stubs.kt, owners named.
 *  - h015 grammar_collision   : dollar signs in emitted C text escaped.
 *  - h017 naming              : if_code -> ifCode.
 */
object IfCode {

    /** compiler-macros.h:74. */
    private const val ABRTX = 6

    /** compiler-macros.h:69. */
    private const val PARSE_ERROR = 1

    /** compiler-macros.h:95 — vendor define IF is the character 'i'. */
    private val IF: Short = 'i'.code.toShort()

    @Throws(IOException::class)
    fun ifCode(): Int {
        val st = CompilerState

        // no argument form
        if (st.xd[st.xpx.toInt()] == ' ' || st.xd[st.xpx.toInt()] == '\u0000') {
            st.xpx++
            st.out.write("\tif (svPtr->tpx) {\n")
            st.IF_flg++
            return 0 // goto next_cmnd
        }

        // h005 + h010: C is at_flg = atTest(xd, pd1, &pd1Last, lines, xpx);
        // the int return is assigned to short at_flg. (B4 u108: the real
        // atTest landed; the caller's pd1Last rides the parameter.)
        val r = atTest(st.xd, st.pd1, st.pd1Last, st.lines, st.xpx)
        st.at_flg = r.rc.toShort()
        st.pd1Last = r.pd1Last
        if (st.at_flg == (-1).toShort()) return ABRTX // goto abrtx

        var gotoIfStructure = false // h011: forward goto if_structure
        if (st.at_flg != 0.toShort()) {

            // h007: char tmp[1024]; strcpy(tmp, "i "); — mutable scratch
            val tmp = StringBuilder("i ")
            atCopy(st.at_flg.toInt(), tmp) // h014 stub

            CompilerState.interpret(tmp.toString()) // h003: INTERPRET

            st.xpx = st.at_flg.toLong()

            if (st.POST_flg != 0.toShort()) {
                st.out.write("\t} /* post conditional 1856*/\n")
                st.POST_flg = 0
            }

            val line = "\tif (svPtr->tpx) {"
            st.out.write(line)
            linepad(line.length, "Test if expression true") // h014 stub

            gotoIfStructure = true
        }

        // h011: backward goto if_again -> while(true) loop
        while (true) {
            if (!gotoIfStructure) {
                // if_again:
                st.t2 = 1
                st.ierr = parse_().toShort() // h014 stub; h010 widening
                if (st.ierr != 0.toShort()) return PARSE_ERROR // goto parse_err

                st.out.write("\tsvPtr->tpx = 0; //Default \$Test indicator\n")

                st.out.write(
                    "\tif (numcomp2((UNS) " +
                        extract2(st.pd1, st.sdlim.toInt()) + // h014 stub (unit 15)
                        " )) { // Test if true \n"
                )

                st.out.write("\tsvPtr->tpx = 1; // \$test becomes true\n")
            }
            gotoIfStructure = false

            // if_structure:
            st.tpx = 1
            if (st.xd[(st.xpx + 1).toInt()] != '{') st.IF_flg++

            if (st.IF_flg > 0) st.ForBlocks[st.IF_flg] = st.ForBlocks[st.IF_flg - 1]

            st.Blocks[st.IF_flg] = IF

            if (st.xd[st.xpx.toInt()] == ',') {
                st.xpx++
                continue // goto if_again
            }

            return 0 // goto next_cmnd
        }
    }
}
