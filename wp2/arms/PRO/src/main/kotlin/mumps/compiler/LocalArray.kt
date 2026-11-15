package mumps.compiler

import java.io.IOException

/**
 * PRO-arm translation of compiler-localarray.c::LocalArray (pilot B1,
 * unit 21). Translated independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), reference source
 * lines 40-110.
 *
 * LocalArray scans a destination token vd for a coded local array
 * reference (206/207/208 = coded open paren / close paren / comma)
 * and, if found, emits the C prologue that expands it into a local
 * tmp buffer (strmove/strcat emission), replacing vd with "atmp" on
 * the close paren and setting Set_flg.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h001 io_stream_param     : Out emissions via CompilerState.out.
 *  - h004 globals_ext         : v1d (compiler-globals-ext.h:45,
 *    unsigned char[2048]) added to CompilerState this unit.
 *  - h006 out_param_holder    : C 'short *Set_flg' -> ShortArray
 *    holder, read/write [0] at call sites (vendor callers in
 *    compiler-set.c pass &Set_flg, the file-scope global).
 *  - h009 format_string       : emitted C text byte-exact (tab runs,
 *    // comments, casts preserved verbatim).
 *  - h011 goto                : forward goto has_array -> flag
 *    fallthrough; scan loop preserved.
 *  - h012 unsigned_byte_domain: vd/v1d byte domains (Char(0..255)).
 *  - h013 dead_store          : vendor 'char tmp[1024];' (line 45) is
 *    never used - dropped with this comment, not initialized.
 *  - h017 naming              : LocalArray/vd/Set_flg keep vendor
 *    names (Set_flg is the C parameter name; the holder param keeps
 *    it).
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The entry guard (line 47) tests v1d[1] (the file-scope GLOBAL
 *    from compiler-globals-ext.h:45), not vd[1] - apparently the
 *    previous parsed token; kept exactly as written.
 *  - The 206 branch emits 'i=strlen(tmp);' (line 64/79) while the
 *    208 branch emits 'i=strlen((const char *) tmp);' (line 104) -
 *    the cast difference is preserved verbatim.
 */
private const val CODED_OPEN = 206  // vendor literal in emissions
private const val CODED_CLOSE = 207
private const val CODED_COMMA = 208

@Throws(IOException::class)
fun LocalArray(vd: CharArray, Set_flg: ShortArray) {
    val st = CompilerState

    //#+ Process array reference
    // vendor line 47: v1d[1] is the file-scope global (see header note)
    if (st.v1d[1] == '^' || st.v1d[1] == '$') return

    // for (i = 0; vd[i] != 0; i++) if (vd[i] > 127) goto has_array;
    // h011: forward goto -> flag; return falls through otherwise.
    var hasArray = false
    var i = 0
    while (vd[i] != NUL) {
        if (vd[i].code > 127) {
            hasArray = true
            break
        }
        i++
    }
    if (!hasArray) return // vendor: Not an array reference - no open paren

    //#+ Process LHS style local array reference
    // has_array:
    st.out.write(
        "\t{\tchar tmp[STR_MAX];  // expand lhs array reference\n" +
            "\t\tint i;\n"
    )

    i = 0
    while (vd[i] != NUL) {
        if (vd[i].code == CODED_OPEN) { // Open
            vd[i] = NUL
            st.out.write("\t\tstrmove((UNS) tmp, (UNS) \"" + cstringAt(vd, 0) + "\"); // array name\n")
            st.out.write("\t\ti=strlen(tmp);\n")
            st.out.write("\t\ttmp[i]=206; // coded open paren\n")
            st.out.write("\t\ttmp[i+1]=0;\n")
            vd[i] = CODED_OPEN.toChar()

            // for (j = i + 1; vd[j] != 0; j++) if (207||208) break;
            var j = i + 1
            while (vd[j] != NUL) {
                if (vd[j].code == CODED_CLOSE || vd[j].code == CODED_COMMA) break
                j++
            }
            val k = vd[j].code
            vd[j] = NUL
            if (strncmpC(vd, i + 1, "#G#", 3) == 0 ||
                strncmpC(vd, i + 1, "#S#", 3) == 0
            ) {
                st.out.write("\t")
                st.out.write("\tstrcat((char *) tmp,(char *) " + Extract2(vd, i + 1) + "); // index\n")
            } else {
                st.out.write("\tstrcat((char *) tmp,(char *) " + cstringAt(vd, i + 1) + "); // index\n")
            }
            st.out.write("\t\ti=strlen(tmp);\n")
            st.out.write("\t\ttmp[i]=" + k + "; // coded comma or close paren\n")
            st.out.write("\t\ttmp[i+1]=0;\n")
            vd[j] = k.toChar()
            i = j - 1
        } else if (vd[i].code == CODED_CLOSE) { // Close
            st.out.write("\tstrmove((UNS) atmp, (UNS) tmp); // preserve expanded array reference\n")
            Set_flg[0] = 1
            st.out.write("\t\t}\n")
            strcpyC(vd, "atmp")
            break
        } else if (vd[i].code == CODED_COMMA) { // comma
            var j = i + 1
            while (vd[j] != NUL) {
                if (vd[j].code == CODED_CLOSE || vd[j].code == CODED_COMMA) break
                j++
            }
            val k = vd[j].code
            vd[j] = NUL
            if (strncmpC(vd, i + 1, "#G#", 3) == 0 ||
                strncmpC(vd, i + 1, "#S#", 3) == 0
            ) {
                st.out.write("\t")
                st.out.write("\t\tstrcat((char *) tmp,(char *) " + Extract2(vd, i + 1) + "); // index\n")
            } else {
                st.out.write("\t\tstrcat((char *) tmp,(char *) " + cstringAt(vd, i + 1) + "); // index\n")
            }
            st.out.write("\t\ti=strlen((const char *) tmp);\n")
            st.out.write("\t\ttmp[i]=" + k + "; // coded comma or close paren\n")
            st.out.write("\t\ttmp[i+1]=0;\n")
            vd[j] = k.toChar()
            i = j - 1
        }
        i++
    }
}
