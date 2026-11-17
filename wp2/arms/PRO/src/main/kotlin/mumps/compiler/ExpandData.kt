package mumps.compiler

import java.io.IOException

/**
 * PRO-arm translation of compiler-parse.c::ExpandData (B5 tranche
 * B, unit 143, rank 113). Translated independently from the
 * pristine vendor source (mumps-14-may-2026.tgz, sha256
 * 621deb25...ffdbe), reference source lines 2267-2292.
 *
 * Identity verified against the study tool
 * (tools/select_study_subset.py): ExpandData, c, loc 24,
 * comment_ratio 0.04, pointer_marks 0, control_marks 6,
 * score 11.4 - exact match to wp1/study-subset/units.csv row 144
 * (batch 5).
 *
 * ExpandData(v1d,i,bp): the dollar-data function compiler expander - the i-th
 * cursor is bumped one byte past the function sigil, the #G# /
 * #S# marker selects the global vs local emission
 * (GlobalData/LocalData into the compiler's Out stream), the two
 * #-delimited argument tokens are copied into fixed 32-byte
 * scratch, and arg2 is echoed into the caller's bp buffer (the
 * result-name contract the fcn_proc driver consumes at vendor
 * 1293; that driver is not yet rendered - call-site binding is a
 * downstream unit).
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h001 io_stream_param     : the compiler's FILE* Out
 *                               emissions go through
 *                               CompilerState.out (Writer); the
 *                               emitted text keeps its literal C
 *                               form (translated compiler source).
 *  - h012 unsigned_byte_domain: v1d/bp/arg1/arg2 byte domains map
 *                               to NUL-terminated CharArrays; the
 *                               C char arg1[32]="" zero-fill gives
 *                               the NUL termination for free; the
 *                               vendor's unindexed j++ loop
 *                               cursor is kept as an explicit
 *                               index pair (j and i advance
 *                               together, u11 ExpandNext
 *                               precedent).
 *  - h015 grammar_collision   : pre-push scan incl. cross-file
 *                               top-level names: ExpandData is a
 *                               new top-level; no collision.
 *  - h017 naming              : ExpandData keeps the vendor name;
 *                               strncmpC/strcpyC/cstringAt carry
 *                               the C suffix (CompilerCstring.kt
 *                               shared seats, u11 ExpandNext
 *                               precedent).
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The #G#/#S# scan loops copy without bounds checking: a run
 *    of more than 31 non-# bytes overruns the 32-byte arg buffers
 *    (vendor UB); the loop terminates only on the '#' delimiter
 *    or an out-of-bounds read - kept verbatim, CharArray access
 *    quantizes the overrun to an IndexOutOfBounds error carrier.
 *  - If neither marker matches, the vendor falls out of both
 *    arms and returns with bp untouched (no else arm) - kept.
 */
@Throws(IOException::class)
fun ExpandData(v1d: CharArray, iIn: Int, bp: CharArray) {
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
            "\tGlobalData(" + cstringAt(arg1) + "," + cstringAt(arg2) + ",svPtr);\n"
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
            "\tLocalData((unsigned char *) " + cstringAt(arg1) +
                ",(unsigned char *) " + cstringAt(arg2) + ",svPtr);\n"
        )
        strcpyC(bp, arg2)
        return
    }
}
