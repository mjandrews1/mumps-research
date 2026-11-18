package mumps.compiler

import java.io.IOException

/**
 * PRO-arm translation of compiler-parse.c::ExpandName (roster row
 * 260, batch 9; units.csv: loc 47, comment_ratio 0.021,
 * pointer_marks 0, control_marks 12, score 21.6, rank 175).
 * Translated independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), reference
 * source lines 2409-2466 (prototype at vendor :175; the Expand
 * family sibling seat precedent u11/u15/u16/u76).
 *
 * ExpandName(v1d, i, bp): the coded-name expander for the
 * GlobalName/LocalName emissions - after i++, a "#G#" token
 * emits the GlobalName(...) line, a "#S#" token the LocalName
 * line; arg1/arg2 are the two hash-delimited fields, arg3 is
 * either the nested coded name (after CodedComma, via Extract2)
 * or the <128-byte run, or "NULL"; bp receives arg2.
 *
 * No prior Kotlin seat (the ParseMisc prototype roster documented
 * the seat at vendor :175; the compiler/putEdge call sites bind
 * at render); first Kotlin render (h015 clean).
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h007 char_buffer         : v1d/bp and the three char[32]
 *                               arg buffers are NUL-terminated
 *                               CharArrays; the vendor's char
 *                               argN[32] zero-fill gives NUL
 *                               termination for free; the
 *                               unindexed j++ copy loops keep
 *                               the explicit j/i pair (u11/
 *                               u143 precedent; ExpandOrder
 *                               template sibling).
 *  - h014 out_of_scope_call   : Extract2 resolves to the real
 *                               unit-15 seat (Extract2.kt,
 *                               in-scope); strncmpC/strcpyC/
 *                               cstringAt are the
 *                               CompilerCstring shared seats;
 *                               CodedComma = 208 is the
 *                               per-file const (defines.h:40,
 *                               u16/u76 precedent).
 *  - h015 grammar_collision   : new top-level compiler
 *                               expander; no cross-file
 *                               collision (scan verified).
 *  - h017 naming              : ExpandName keeps the vendor
 *                               name.
 *  - h018 preprocessor_conditional: none in this body.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The GlobalName emission carries the vendor's trailing space
 *    before the newline ("svPtr); \n") and NO unsigned-char
 *    casts - both byte-identical to the vendor fprintf; the
 *    LocalName emission HAS the (unsigned char *) casts - kept
 *    per-arm, kept verbatim.
 *  - The #S# arm's nested marker test is "#S#" twice (a copy/
 *    paste bug in the vendor - the #G# twin arm tests "#G#"
 *    then "#S#"); kept verbatim, noted.
 *  - The hash-delimited copy loops run without bounds checks
 *    (a >31-byte run overruns the 32-byte arg buffers, vendor
 *    UB) - kept, CharArray access quantizes the overrun to an
 *    IndexOutOfBounds carrier (u143 precedent).
 *  - If neither marker matches, the vendor falls out of both
 *    arms and returns with bp untouched (no else arm) - kept.
 */

private const val CODED_COMMA = 208 // defines.h:40 CodedComma (u16 precedent)


@Throws(IOException::class)
fun ExpandName(v1d: CharArray, iIn: Int, bp: CharArray) {
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

        // vendor: fprintf(Out,"\tGlobalName(%s, %s, %s, svPtr); \n",...)
        st.out.write(
            "\tGlobalName(" + cstringAt(arg1) + ", " + cstringAt(arg2) +
                ", " + cstringAt(arg3) + ", svPtr); \n"
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
            if (strncmpC(v1d, i, "#S#", 3) == 0 || // vendor: "#S#" twice
                strncmpC(v1d, i, "#S#", 3) == 0 // (copy/paste bug, kept)
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

        // vendor: fprintf(Out,"\tLocalName( (unsigned char *) %s,
        //         (unsigned char *) %s, (unsigned char *) %s, svPtr); \n",...)
        st.out.write(
            "\tLocalName( (unsigned char *) " + cstringAt(arg1) +
                ", (unsigned char *) " + cstringAt(arg2) +
                ", (unsigned char *) " + cstringAt(arg3) + ", svPtr); \n"
        )
        strcpyC(bp, arg2)
        return
    }
}
