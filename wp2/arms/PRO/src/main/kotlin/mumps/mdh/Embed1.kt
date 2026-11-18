package mumps.mdh

import mumps.interp.NUL

private const val STR_MAX = 32000 // vendor strmax.h:3 (unit-44 precedent)

/**
 * PRO-arm translation of libmpscpp.cpp::embed1 (B7 tranche 2,
 * unit 197, rank 198). Translated independently from the
 * pristine vendor source (mumps-14-may-2026.tgz, sha256
 * 621deb25...ffdbe), reference source lines 4151-4175 (the
 * fix-embedded-double-quote-marks helper between
 * mstring::Concat and mstring::Soundex).
 *
 * Identity verified against the study tool: libmpscpp.cpp::
 * embed1, cpp, loc 19, comment_ratio 0.136, pointer_marks 9,
 * control_marks 6, score 27.5 - exact match to
 * wp1/study-subset/units.csv row 197 (batch 7).
 *
 * Rules exercised:
 *  - h006 low_level_pointer : the char-star walk p1 over the
 *    input; the p2 write cursor into tmp.
 *  - h007 char_buffer      : the static char tmp[STR_MAX]
 *    accumulator.
 *  - h012 data_dependent   : the two-pass shape (scan for a
 *    quote, then only re-copy if one was found) so the no-quote
 *    fast path returns the input unchanged.
 *
 * Divergences:
 *  - The C helper returns a file-scope static buffer; the
 *    Kotlin helper returns a fresh CharArray per call (the
 *    u44 STR_MAX precedent; no caller may hold the C buffer
 *    across a second embed1 call, and none does).
 *  - The no-quote aliasing return (C returns the input pointer
 *    itself) is preserved: the input array is returned as-is.
 *  - The null input contract (C returns the empty static
 *    buffer) maps to a fresh empty CharArray (NUL-only cstring
 *    shape, NUL is the 0 char).
 */
fun embed1(inp: CharArray?): CharArray {
    // vendor: static char tmp[STR_MAX]; fresh per call (u44).
    val tmp = CharArray(STR_MAX)
    tmp[0] = NUL

    if (inp == null) return tmp

    // pass 1: scan for an embedded double quote (vendor k flag).
    var hasQuote = false
    for (i in inp.indices) {
        if (inp[i] == '"') {
            hasQuote = true
            break
        }
    }
    if (!hasQuote) return inp // vendor: return (char*)in alias

    // pass 2: copy, doubling every embedded double quote.
    var j = 0
    for (i in inp.indices) {
        if (inp[i] == '"') {
            tmp[j++] = '"'
            tmp[j++] = '"'
        } else {
            tmp[j++] = inp[i]
        }
        if (inp[i] == NUL) break // stop at the cstring terminator
    }
    tmp[j] = NUL
    return tmp
}
