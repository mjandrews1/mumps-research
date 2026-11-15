package mumps.compiler

import java.io.IOException

/**
 * PRO-arm translation of compiler-parse.c::ExpandNext (pilot B1,
 * unit 11). Translated independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), reference source
 * lines 2294-2318.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h001 io_stream_param     : the compiler's FILE* Out emissions go
 *                               through CompilerState.out (Writer).
 *  - h012 unsigned_byte_domain: v1d/bp/arg1/arg2 byte domains map to
 *                               NUL-terminated CharArrays; the C
 *                               char arg1[32]="" zero-fill gives the
 *                               NUL termination for free.
 *  - h017 naming              : ExpandNext keeps the vendor name;
 *                               strncmp/strcpy helpers carry the C
 *                               suffix (strncmpC/strcpyC).
 */
@Throws(IOException::class)
fun ExpandNext(v1d: CharArray, iIn: Int, bp: CharArray) {
    val st = CompilerState

    // h012: char arg1[32]=""; char arg2[32]="" - zero-filled = "".
    val arg1 = CharArray(32)
    val arg2 = CharArray(32)
    var i = iIn + 1 // C: i++;

    if (strncmpC(v1d, i, "#G#", 3) == 0) {
        i += 3
        var j = 0
        while (v1d[i] != '#') {
            arg1[j] = v1d[i]
            i++; j++
        }
        i++
        j = 0
        while (v1d[i] != '#') {
            arg2[j] = v1d[i]
            i++; j++
        }
        st.out.write(
            "\tGlobalOrder(" + cstringAt(arg1) + ", " + cstringAt(arg2) +
                ", (unsigned char *) \"1\", svPtr); //2306\n"
        )
        st.out.write(
            "\tif (strlen((char *)" + cstringAt(arg2) +
                ")==0) strcpy((char *)" + cstringAt(arg2) + ",\"-1\");\n"
        )
        strcpyC(bp, arg2)
        return
    }

    if (strncmpC(v1d, i, "#S#", 3) == 0) {
        i += 3
        var j = 0
        while (v1d[i] != '#') {
            arg1[j] = v1d[i]
            i++; j++
        }
        i++
        j = 0
        while (v1d[i] != '#') {
            arg2[j] = v1d[i]
            i++; j++
        }
        st.out.write(
            "\tLocalNext((unsigned char *) " + cstringAt(arg1) +
                ",(unsigned char *) " + cstringAt(arg2) + ",svPtr);\n"
        )
        strcpyC(bp, arg2)
        return
    }
}
