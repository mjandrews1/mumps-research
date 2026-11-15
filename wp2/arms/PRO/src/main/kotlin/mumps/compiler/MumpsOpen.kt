package mumps.compiler

import java.io.IOException

/**
 * PRO-arm translation of compiler-open.c::mumps_open (pilot B1, unit 3).
 * Translated independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe).
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h001 io_stream_param     : Out emitted via java.io.Writer.
 *  - h009 format_string       : fprintf "%s" idioms become string
 *                               concatenation with the extracted operand;
 *                               emitted text byte-equivalent.
 *  - h010 numeric_promotion   : C's int-to-short assignments of parse_'s
 *                               return become explicit toShort().
 *  - h011 goto                : backward goto openf -> while(true).
 *  - h012 unsigned_byte_domain: strcmp("tmp0", &pd1[sdlim]) compares the
 *                               byte-domain string from offset to NUL.
 *  - h014 out_of_scope_call   : parse_, extract2 -> Stubs.kt.
 *  - h017 naming              : mumps_open -> mumpsOpen.
 */
object MumpsOpen {

    /** compiler-macros.h:69. */
    private const val PARSE_ERROR = 1

    /** compiler-macros.h:70. */
    private const val ARG_LIST_ERROR = 2

    /**
     * h012: C-string at offset — the domain of &pd1[sdlim] in strcmp is
     * the byte array from the offset up to the terminating NUL.
     */
    private fun cstringAt(buf: CharArray, off: Int): String {
        val sb = StringBuilder()
        var i = off
        while (i < buf.size && buf[i] != '\u0000') {
            sb.append(buf[i])
            i++
        }
        return sb.toString()
    }

    @Throws(IOException::class)
    fun mumpsOpen(): Int {
        val st = CompilerState

        // h011: backward goto openf -> while(true) loop
        while (true) {
            // openf:
            st.t2 = 1
            st.ierr = parse_().toShort() // h014 stub; h010 widening
            if (st.ierr != 0.toShort()) return PARSE_ERROR // goto parse_err

            st.out.write(
                "\t_i = atoi( CSTR " +
                    extract2(st.pd1, st.sdlim.toInt()) + // h014 stub (unit 15)
                    ");\n"
            )

            if (st.xd[st.xpx.toInt()] != ':') return ARG_LIST_ERROR // arg_list_error
            st.xpx++
            st.t2 = 1
            st.ierr = parse_().toShort() // h014 stub; h010 widening
            if (st.ierr != 0.toShort()) return PARSE_ERROR // goto parse_err

            if (cstringAt(st.pd1, st.sdlim.toInt()) != "tmp0") {
                st.out.write(
                    "\tstrmove((UNS) tmp0, (UNS) " +
                        extract2(st.pd1, st.sdlim.toInt()) +
                        ");\n"
                )
            }

            st.out.write("\tfor(_j=0; tmp0[_j]!=0 && tmp0[_j]!=',';_j++);\n")

            st.out.write("\tif (tmp0[_j]!=',') ")
            st.out.write("ErrorMessage(\"File open error\",svPtr->LineNumber);\n")

            st.out.write("\ttmp0[_j++]=0;\n")
            st.out.write("\tstrmove((UNS) gtmp, (UNS) tmp0);\n")

            st.out.write(
                "\tif (strcmp( (CCHR) &tmp0[_j], (CCHR) \"old\") == 0 || \n" +
                    "\t\tstrcmp( (CCHR) &tmp0[_j], (CCHR) \"OLD\") == 0) _new = 0; \n"
            )

            st.out.write(
                "\telse if (strcmp( (CCHR) &tmp0[_j], (CCHR) \"new\") == 0 || \n" +
                    "\t\tstrcmp((CCHR) &tmp0[_j], (CCHR) \"NEW\") == 0) _new = 1; \n"
            )

            st.out.write(
                "\telse if (strcmp( (CCHR) &tmp0[_j], (CCHR) \"append\") == 0 || \n" +
                    "\t\tstrcmp((CCHR) &tmp0[_j], (const char *) \"APPEND\") == 0) _new=2; \n"
            )

            st.out.write("\telse ErrorMessage(\"File open error\",svPtr->LineNumber);\n")

            st.out.write("\tif (svPtr->out_file[_i] != NULL || svPtr->in_file[_i] != NULL) \n")
            st.out.write("\t\tErrorMessage(\"File already open\", svPtr->LineNumber); \n")

            st.out.write("\tif (_new==1) { /* output */ \n")

            st.out.write("\t\tsvPtr->out_file[_i] = fopen((const char *) gtmp, \"w\"); \n")

            st.out.write("\t\tif(svPtr->out_file[_i] == NULL) svPtr->tpx = 0; \n")
            st.out.write("\t\telse svPtr->tpx = 1; \n")
            st.out.write("\t\t} \n")
            st.out.write("\telse if (_new == 2) { /* append */ \n")

            st.out.write("\t\tsvPtr->out_file[_i] = fopen((const char *) gtmp, \"a\"); \n")

            st.out.write("\t\tif(svPtr->out_file[_i] == NULL) svPtr->tpx = 0; \n")
            st.out.write("\t\telse svPtr->tpx = 1; \n")
            st.out.write("\t\t} \n")
            st.out.write("\telse { /* input */ \n")

            st.out.write("\t\tsvPtr->in_file[_i] = fopen((const char *) gtmp, \"r\"); \n")

            st.out.write("\t\tif(svPtr->in_file[_i] == NULL) svPtr->tpx = 0; \n")
            st.out.write("\t\telse svPtr->tpx = 1; \n")
            st.out.write("\t\t}\n")

            st.xpx++ // C: if (xd[++xpx] != ',')
            if (st.xd[st.xpx.toInt()] != ',') {

                if (st.POST_flg != 0.toShort()) {
                    st.out.write("\t} /* post conditional open */\n")
                    st.POST_flg = 0
                }
                return 0
            }

            st.xpx++
            // goto openf
        }
    }
}
