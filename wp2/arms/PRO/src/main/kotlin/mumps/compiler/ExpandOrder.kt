package mumps.compiler

import java.io.IOException

/**
 * PRO-arm translation of compiler-parse.c::ExpandOrder (B6 tranche
 * B, unit 167, rank 185). Translated independently from the
 * pristine vendor source (mumps-14-may-2026.tgz, sha256
 * 621deb25...ffdbe), reference source lines 2208-2265.
 *
 * Identity verified against the study tool
 * (tools/select_study_subset.py): ExpandOrder, c, loc 50,
 * comment_ratio 0.02, pointer_marks 0, control_marks 13,
 * score 24.2 - exact match to wp1/study-subset/units.csv row 168
 * (batch 6).
 *
 * ExpandOrder(v1d, i, bp): the dollar-order function compiler
 * expander - the u143 ExpandData / u16 ExpandGet sibling over the
 * same #G#/#S# two-token shape. The i-th cursor is bumped one
 * byte past the function sigil, the marker selects the
 * GlobalOrder vs LocalOrder emission into the compiler's Out
 * stream, the two hash-delimited argument tokens are copied into
 * fixed 32-byte scratch, the optional third token after
 * CodedComma fills arg3 (a nested #G#/#S# coded name through
 * Extract2, else the plain-copy loop bounded by the
 * v1d[i]<128 domain test, else the literal "NULL"), and arg2 is
 * echoed into the caller's bp buffer (the result-name contract;
 * the vendor 2236 comment "arg1 returns the global for the next
 * element" kept). Like ExpandData, the fcn_proc driver that
 * calls this expander is not yet rendered - call-site binding is
 * a downstream unit, and no stub was raised.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h001 io_stream_param     : the compiler's FILE* Out
 *                               emissions go through
 *                               CompilerState.out (Writer); the
 *                               emitted text keeps its literal C
 *                               form (translated compiler source).
 *  - h012 unsigned_byte_domain: v1d/bp/arg1-3 byte domains map to
 *                               NUL-terminated CharArrays; the C
 *                               char argN[32] zero-fill gives NUL
 *                               termination for free; the vendor's
 *                               unindexed j++ copy loops keep the
 *                               explicit j/i index pair (u11/u143
 *                               precedent).
 *  - h014 out_of_scope_call   : Extract2 resolves to the real
 *                               unit-15 seat (same file,
 *                               in-scope); strncmpC/strcpyC/
 *                               cstringAt are the CompilerCstring
 *                               shared seats (u11 ExpandNext
 *                               precedent); CodedComma = 208 is
 *                               the per-file const (vendor
 *                               defines.h:40, u16 ExpandGet /
 *                               u76 ExpandQS precedent).
 *  - h015 grammar_collision   : pre-push scan incl. cross-file
 *                               top-level names: ExpandOrder is a
 *                               new top-level compiler expander;
 *                               the mdh GlobalOrder(String, Int)
 *                               seats live in the mumps.mdh
 *                               package - no collision.
 *  - h017 naming              : ExpandOrder keeps the vendor name.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The emitted GlobalOrder text carries the vendor's own
 *    debug fossil "// p2236" (source line 2235) and the split
 *    two-line continuation; the LocalOrder emission has no such
 *    fossil and a trailing space before the newline - both
 *    byte-identical to the vendor fprintf strings.
 *  - The nested marker tests differ per arm: the #G# arm tests
 *    "#G#" then "#S#", the #S# arm tests "#S#" then "#G#" - kept
 *    in the vendor's order.
 *  - The hash-delimited scan loops copy without bounds checking:
 *    a run of more than 31 non-hash bytes overruns the 32-byte
 *    arg buffers (vendor UB) - kept verbatim, CharArray access
 *    quantizes the overrun to an IndexOutOfBounds error carrier
 *    (u143 precedent).
 *  - If neither marker matches, the vendor falls out of both
 *    arms and returns with bp untouched (no else arm) - kept.
 */
private const val CODED_COMMA = 208 // defines.h:40 CodedComma (u16 precedent)

@Throws(IOException::class)
fun ExpandOrder(v1d: CharArray, iIn: Int, bp: CharArray) {
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
            "\tGlobalOrder((unsigned char *) " + cstringAt(arg1) +
                ", (unsigned char *) \n\t\t(unsigned char *) " +
                cstringAt(arg2) + ", svPtr); // p2236\n"
        ) // arg1 returns the global for the next element
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
            if (strncmpC(v1d, i, "#S#", 3) == 0 ||
                strncmpC(v1d, i, "#G#", 3) == 0
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
            "\tLocalOrder((unsigned char *) " + cstringAt(arg1) +
                ", (unsigned char *) " + cstringAt(arg2) +
                ", (unsigned char *) " + cstringAt(arg3) + ", svPtr); \n"
        )
        strcpyC(bp, arg2)
        return
    }
}
