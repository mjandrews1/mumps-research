package mumps.compiler

import java.io.IOException

/**
 * PRO-arm translation of compiler-parse.c::ExpandGet (pilot B1, unit
 * 16). Translated independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), reference source
 * lines 2322-2373.
 *
 * ExpandGet emits the GlobalGet/LocalGet retrieval code for #G#/#S#
 * coded two-field names, with an optional third (default) argument
 * after CodedComma.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h001 io_stream_param     : Out emissions via CompilerState.out.
 *  - h012 unsigned_byte_domain: v1d/bp/arg1-3 byte domains; the C
 *    char argN[32]="" zero-fill gives NUL termination for free.
 *  - h014 out_of_scope_call   : Extract2 call resolves to the unit 15
 *    translation (in-scope, same file); the plain-copy branch is
 *    bounded by the CodedComma domain test (v1d[i]<128).
 *  - h017 naming              : ExpandGet keeps the vendor name.
 *
 * Vendor note (translated faithfully, not "fixed"): the #S# branch's
 * condition strncmp(&v1d[i],"#S#",3)==0 || strncmp(&v1d[i],"#S#",3)==0
 * duplicates the same test twice (lines 2355-2356); kept as written.
 */
private const val CODED_COMMA = 208 // compiler-parse.c:133 CodedComma

@Throws(IOException::class)
fun ExpandGet(v1d: CharArray, iIn: Int, bp: CharArray) {
    val st = CompilerState

    // h012: char arg1[32]=""; char arg2[32]=""; char arg3[32]="".
    val arg1 = CharArray(32)
    val arg2 = CharArray(32)
    val arg3 = CharArray(32)
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
        i += 1
        if (v1d[i].code == CODED_COMMA) {
            i++
            if (strncmpC(v1d, i, "#G#", 3) == 0 ||
                strncmpC(v1d, i, "#S#", 3) == 0
            ) {
                strcpyC(arg3, Extract2(v1d, i))
            } else {
                j = 0
                while (v1d[i].code < 128 && v1d[i] != NUL) {
                    arg3[j] = v1d[i]
                    i++; j++
                }
            }
        } else {
            strcpyC(arg3, "NULL")
        }

        st.out.write(
            "\tGlobalGet( (unsigned char *) " + cstringAt(arg1) +
                ", (unsigned char *) " + cstringAt(arg2) +
                ", (unsigned char *) " + cstringAt(arg3) + ", svPtr); \n"
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
        i += 1
        if (v1d[i].code == CODED_COMMA) {
            i++
            // vendor: "#S#" == 0 || "#S#" == 0 (duplicated test, kept)
            if (strncmpC(v1d, i, "#S#", 3) == 0 ||
                strncmpC(v1d, i, "#S#", 3) == 0
            ) {
                strcpyC(arg3, Extract2(v1d, i))
            } else {
                j = 0
                while (v1d[i].code < 128 && v1d[i] != NUL) {
                    arg3[j] = v1d[i]
                    i++; j++
                }
            }
        } else {
            strcpyC(arg3, "NULL")
        }

        st.out.write(
            "\tLocalGet( (unsigned char *) " + cstringAt(arg1) +
                ", (unsigned char *) " + cstringAt(arg2) +
                ", (unsigned char *) " + cstringAt(arg3) + ", svPtr); \n"
        )
        strcpyC(bp, arg2)
        return
    }
}
