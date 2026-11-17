package mumps.interp

// ----------------------------------------------------------------------------
// (B4 unit 121) interp-stem.cpp::escan (vendor reference lines 425-450).
// Identity: units.csv row 122 - interp-stem.cpp::escan, cpp, loc 17,
// comment_ratio 0.056, pointer_marks 3, control_marks 1, score 7.9,
// rank 61 - exact match.
// ----------------------------------------------------------------------------

/**
 * int escan(char word[], int wl, const struct ends endings[]): the
 * Porter-style suffix scan behind $ZALTER/$ZNORMAL stemming - walk the
 * endings table, and whenever the word is longer than the stem floor
 * ("wl - offset > 3") and ends with the candidate ending, splice the
 * replacement in (destructively, via strmove) and restart the scan
 * (or break out when the table entry flags a goto).
 *
 * The ends struct itself is declared in include/mumpsc/stem.h
 * (lines 31-38); it is carried here as the Ends class. The ending
 * TABLES (d_endings, s_endings, g_endings, ...) are vendor
 * interp-stem.cpp::<misc> data - batch-6 unit, not yet translated;
 * this function is their only consumer (interp-stem.cpp:435-462).
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h007 char_buffer        : word/ending/replace are C-string
 *    CharArray buffers, NUL-terminated.
 *  - h012 unsigned_byte_domain: unsigned char domain kept; ending[0]
 *    NUL test is the table terminator convention.
 *  - h014 out_of_scope_call  : strmove resolves to the existing
 *    InterpStubs strmove stub (owner interp-strmanip.cpp::strmove,
 *    not a corpus unit - TODO at runtime, compiles clean; the
 *    established pattern).
 *  - h015 grammar_collision  : the vendor's strcmp suffix test has no
 *    shared helper in this package (Parse_.kt's strcmpAC is
 *    file-private); an inline file-private suffix compare is used -
 *    no public signature collisions.
 *  - h017 naming             : escan/Ends keep the vendor spellings.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The strcmp(&word[wl-offset], ending) suffix test compares to the
 *    END of word - equivalent to word.substring(wl-offset) equals
 *    ending; rendered as an inline file-private compare over the
 *    CharArrays (endsWithC) without materializing substrings.
 *  - next is a goto flag: 0 means "jump back to table entry 0 and
 *    keep scanning", nonzero means "stop after this replacement" -
 *    the vendor if (endings[i].next) break; else i = 0; shape is
 *    kept verbatim, including the restart-on-zero.
 *  - The replacement splices at word[wl-offset] with no bound check
 *    against the word buffer size - vendor trusts the tables; kept.
 *  - wl is updated by replen - offset AFTER the splice, so the
 *    returned length reflects the replacement's length, not the
 *    ending's; kept exactly.
 */

/** vendor include/mumpsc/stem.h:31-38 struct ends carrier. */
class Ends(
    /** vendor: const char *ending - the candidate suffix (NUL-terminated). */
    val ending: CharArray,
    /** vendor: int offset - length of the ending. */
    val offset: Int,
    /** vendor: const char *replace - the replacement (NUL-terminated). */
    val replace: CharArray,
    /** vendor: int replen - length of the replacement. */
    val replen: Int,
    /** vendor: int next - goto flag (0 = restart scan at entry 0). */
    val next: Int,
)

/**
 * File-private suffix compare (h015 note): returns true when
 * word[wl - offset ..] equals the NUL-terminated ending - the
 * vendor's strcmp(&word[wl-offset], ending) == 0 shape.
 */
private fun endsWithC(word: CharArray, start: Int, ending: CharArray): Boolean {
    var i = 0
    while (true) {
        val w = word[start + i]
        val e = ending[i]
        if (e == NUL) return w == NUL
        if (w != e) return false
        i++
    }
}

fun escan(word: CharArray, wl: Int, endings: Array<Ends>): Int {
    var wl = wl
    var i = 0

    // vendor: while (endings[i].ending[0]) { ... }
    while (endings[i].ending[0] != NUL) {

        // vendor: if (wl - endings[i].offset > 3 &&
        //         strcmp(&word[wl - endings[i].offset],
        //                endings[i].ending) == 0) { ... }
        if (wl - endings[i].offset > 3 &&
            endsWithC(word, wl - endings[i].offset, endings[i].ending)
        ) {
            // vendor: strmove((unsigned char*) &word[wl - offset],
            //                 (unsigned char*) endings[i].replace);
            strmove(word, wl - endings[i].offset, endings[i].replace, 0)
            // vendor: wl += replen - offset;
            wl += endings[i].replen - endings[i].offset

            // vendor: if (endings[i].next) break; else i = 0;
            if (endings[i].next != 0) break
            i = 0
        } else {
            // vendor: else i++;
            i++
        }
    }

    // vendor: return (wl);
    return wl
}
