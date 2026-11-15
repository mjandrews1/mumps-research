package mumps.compiler

import java.io.IOException

/**
 * PRO-arm translation of compiler-parse.c::Extract2 (pilot B1, unit 15).
 * Translated independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), reference source
 * lines 2082-2127.
 *
 * Extract2(str): if str is a #S#/#G# coded name, splits the two
 * #-delimited fields, emits the sym_/Mglobal retrieval code (the
 * sym_/VariableNotFound names inside the emitted text are C output
 * strings, not Kotlin calls), and returns the second field; otherwise
 * returns str unchanged.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h001 io_stream_param     : Out emissions via CompilerState.out.
 *  - h007 static_scratch      : vendor static char tmp1[64],tmp2[64]
 *    persist across calls (tmp2 is returned); carried on the
 *    Extract2State object so the returned content stays stable.
 *  - h012 unsigned_byte_domain: str/tmp1/tmp2 byte domains.
 *  - h014 out_of_scope_call   : resolves the compiler Stubs.kt extract2
 *    stub (this unit is its owner; stub now delegates here).
 *  - h017 naming              : Extract2 keeps the vendor name.
 *
 * Vendor note (translated faithfully, not "fixed"): source line 2104
 * is tmp2[i - j] =' \0'; - a two-character constant (space + NUL).
 * On gcc the int value is 0x2000 and the assignment to char keeps the
 * low byte, which is 0 - i.e. a NUL terminator; translated as NUL.
 */
object Extract2State {
    /** h007: vendor static char tmp1[64]. */
    val tmp1 = CharArray(64)

    /** h007: vendor static char tmp2[64] (returned). */
    val tmp2 = CharArray(64)
}

@Throws(IOException::class)
fun Extract2(str: CharArray, off: Int = 0): String {
    val st = CompilerState
    val tmp1 = Extract2State.tmp1
    val tmp2 = Extract2State.tmp2

    if (str[off] != '#') return cstringAt(str, off) // C: return str

    var i = off + 3
    while (str[i] != '#' && str[i] != NUL) i++

    // memcpy(tmp1, &str[3], i - 3); tmp1[i - 3] = '\0';
    var k = 0
    while (k < i - 3 - off) {
        tmp1[k] = str[off + 3 + k]
        k++
    }
    tmp1[i - 3 - off] = NUL

    // for (j = ++i; str[i] != '#' && str[i] != '\0'; i++);
    i++
    val j = i
    while (str[i] != '#' && str[i] != NUL) i++

    // memcpy(tmp2, &str[j], i - j); tmp2[i - j] = '\0';
    k = 0
    while (k < i - j) {
        tmp2[k] = str[j + k]
        k++
    }
    tmp2[i - j] = NUL // vendor ' \0' two-char constant - low byte 0 (see header)

    val s1 = cstringAt(tmp1)
    val s2 = cstringAt(tmp2)

    if (strncmpC(str, off, "#S#", 3) == 0) {
        st.out.write(
            "\tif (sym_(SYMGET, (UNS) " + s1 + ", (UNS) " + s2 +
                ", svPtr) == NULL) // get value \n"
        )
        st.out.write(
            "\t\tVariableNotFound1(svPtr->LineNumber, (UNS) " + s1 +
                "); // not found\n"
        )
    } else if (strncmpC(str, off, "#G#", 3) == 0) {
        st.out.write(
            "\tif (!Mglobal(RETRIEVE, " + s1 + ", " + s2 +
                ", svPtr)) // retrieve global\n" +
                "\t\tVariableNotFound(svPtr->LineNumber, " + s1 + ");\n"
        )
    }
    return s2
}
