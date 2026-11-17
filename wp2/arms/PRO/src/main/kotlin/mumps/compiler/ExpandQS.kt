package mumps.compiler

import mumps.interp.NUL

/**
 * PRO-arm translation of compiler-parse.c::ExpandQS (B3 unit 76,
 * rank 181). Translated independently from the pristine vendor
 * source (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), reference
 * source lines 2160-2212.
 *
 * Identity verified against the study tool (tools/select_study_
 * subset.py): compiler-parse.c::ExpandQS, c, loc 48, comment_ratio
 * 0.02, pointer_marks 0, control_marks 12, score 23.0, batch 3 -
 * exact match to wp1/study-subset/units.csv row 181.
 *
 * ExpandQS(v1d, i, bp): expands the compiler's #G#/#S# quick-sort
 * argument triplets into a GlobalQS(...) emission on Out, leaving
 * arg2 in bp.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h002 output_macro : fprintf(Out, ...) -> CompilerState.out
 *    write with the same tab/newline escapes and %s splices.
 *  - h007 char_buffer : arg1/arg2/arg3 are 32-byte C-string
 *    buffers; the NUL terminators are set by the copy loops' end
 *    pass (the vendor relies on the global "" zero-init - the port
 *    writes an explicit NUL after each copy loop).
 *  - h012 unsigned_byte_domain: v1d/bp/argN byte domains.
 *  - h014 out_of_scope_call: Extract2 (B1 unit 15 - the real
 *    implementation, delegates via the Stubs alias) reused;
 *    strncmp -> local helper over the byte domain.
 *  - h015 grammar_collision: pre-push scan incl. cross-file
 *    top-level names: new top-level ExpandQS - no collisions.
 *  - h017 naming: ExpandQS keeps the vendor name; the CodedComma
 *    constant (defines.h:40: 208) is file-private here.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The two arms are structurally identical except for the marker
 *    order tested (#G# arm checks #G#/#S# in the arg3 probe, #S#
 *    arm checks #S#/#G#) - kept as two arms, not merged.
 *  - The arg3 plain-copy loop condition "v1d[i]<128 && v1d[i]!=0"
 *    is copied verbatim (high-bit bytes stop the copy).
 *  - If neither marker matches, the function falls through having
 *    consumed the leading i++ - vendor behavior kept.
 */
private const val CodedComma = 208 // vendor defines.h:40

fun ExpandQS(v1d: CharArray, iStart: Int, bp: CharArray) {
    val arg1 = CharArray(32)
    val arg2 = CharArray(32)
    val arg3 = CharArray(32)
    var j: Int

    var i = iStart
    i++
    if (strncmpC(v1d, i, "#G#", 3) == 0) {
        i += 3
        j = 0
        while (v1d[i] != '#') {
            arg1[j] = v1d[i]
            j++
            i++
        }
        arg1[j] = NUL
        i++
        j = 0
        while (v1d[i] != '#') {
            arg2[j] = v1d[i]
            j++
            i++
        }
        arg2[j] = NUL
        i += 1
        if (v1d[i] == CodedComma.toChar()) {
            i++
            if (strncmpC(v1d, i, "#G#", 3) == 0 ||
                strncmpC(v1d, i, "#S#", 3) == 0
            ) {
                strcpyC(arg3, Extract2(v1d, i)) // strcpy(arg3, Extract2(&v1d[i]))
            } else {
                j = 0
                while (v1d[i] < '\u0080' && v1d[i] != NUL) {
                    arg3[j] = v1d[i]
                    j++
                    i++
                }
                arg3[j] = NUL
            }
        } else {
            strcpyC(arg3, "NULL")
        }

        CompilerState.out.write(
            "\tGlobalQS( (unsigned char *) " + cstring(arg1) +
                ", (unsigned char *) " + cstring(arg2) +
                ", (unsigned char *) " + cstring(arg3) + ", svPtr); \n"
        )
        strcpyC(bp, cstring(arg2))
        return
    }

    if (strncmpC(v1d, i, "#S#", 3) == 0) {
        i += 3
        j = 0
        while (v1d[i] != '#') {
            arg1[j] = v1d[i]
            j++
            i++
        }
        arg1[j] = NUL
        i++
        j = 0
        while (v1d[i] != '#') {
            arg2[j] = v1d[i]
            j++
            i++
        }
        arg2[j] = NUL
        i += 1
        if (v1d[i] == CodedComma.toChar()) {
            i++
            if (strncmpC(v1d, i, "#S#", 3) == 0 ||
                strncmpC(v1d, i, "#G#", 3) == 0
            ) {
                strcpyC(arg3, Extract2(v1d, i))
            } else {
                j = 0
                while (v1d[i] < '\u0080' && v1d[i] != NUL) {
                    arg3[j] = v1d[i]
                    j++
                    i++
                }
                arg3[j] = NUL
            }
        } else {
            strcpyC(arg3, "NULL")
        }

        CompilerState.out.write(
            "\tGlobalQS( (unsigned char *) " + cstring(arg1) +
                ", (unsigned char *) " + cstring(arg2) +
                ", (unsigned char *) " + cstring(arg3) + ", svPtr); \n"
        )

        strcpyC(bp, cstring(arg2))
        return
    }
}

// (build fix) the private strncmpC mirror was REMOVED: the
// package-level compiler strncmpC (CompilerCstring.kt, B1) has the
// same shape - it is now the single definition (conflicting
// overloads, cleanLocks precedent).

/** C strcpy(dst, src) over the byte domain from offset 0. */
private fun strcpyC(dst: CharArray, src: String) {
    for (k in src.indices) dst[k] = src[k]
    dst[src.length] = NUL
}

/** cstring read over the byte domain. */
private fun cstring(b: CharArray): String {
    val sb = StringBuilder()
    var i = 0
    while (b[i] != NUL) {
        sb.append(b[i])
        i++
    }
    return sb.toString()
}
