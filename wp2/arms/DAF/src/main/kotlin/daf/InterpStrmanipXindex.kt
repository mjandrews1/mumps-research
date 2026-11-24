package daf

/*
 * DAF batch-9 unit 260 (roster row 260; interp-strmanip.cpp::xindex,
 * core, LOC 25, score 11.5). Consult 2026-11-24T22:37:00Z.
 *
 * SEAT RELEASE (DafInterpStubsT3.kt:119): the xindexDaf named stub
 * is deleted with this release note; the real body lands at the
 * same name and signature, and the existing caller (Match.kt:82,
 * the unit-212 punct probe xindexDaf(punct, 0, tmp, 0, 1) > 0)
 * resolves unchanged. The compiler-sys1.c::xindex twin keeps its
 * own roster row (b10) with its own seat when drawn - the
 * strmanip body form is 1-based from the vendor short strt,
 * with the trailing-past-NUL reads diverging to a clean bounds
 * check (see below).
 *
 * Vendor shape kept: istrt = strt - 1 (0 when strt <= 1); scan
 * k from istrt over the source; first k where the pattern
 * matches at k returns the 1-based position k + 1; 0 otherwise.
 *
 * Fold note (row 33 kin): the vendor inner compare reads
 * source[k+l] even past the source NUL when the pattern is
 * longer than the remaining source; the C reads then see the NUL
 * (or stack garbage past it) and mismatch. The Kotlin body
 * treats an out-of-range read as the NUL value (a mismatch when
 * the pattern byte is nonzero), DOCUMENTED DIVERGENCE: no stack
 * read hazard.
 * Dafny: EXEMPT (index-family string search; the strmanip spec
 * family carries the declared-assumption shape, no new spec).
 */

fun xindexDaf(s: CharArray, sOff: Int, pat: CharArray, patOff: Int, from: Int): Int {
    // pattern length: for (i=0; ptrn[i]; i++); --i  (last index)
    var i = 0
    while (pat[patOff + i] != NUL_D) i++
    if (i > 0) i--
    // source last index j
    var j = 0
    while (s[sOff + j] != NUL_D) j++
    j--
    // istrt = strt - 1; 0 when strt <= 1
    var istrt = from - 1
    if (from <= 1) istrt = 0

    var k = istrt
    while (k <= j) {
        var flg = false
        var l = 0
        while (l <= i) {
            val sc = if (sOff + k + l < s.size) s[sOff + k + l] else NUL_D
            if (pat[patOff + l] != sc) {
                flg = true
                break
            }
            l++
        }
        if (!flg) return k + 1
        k++
    }
    return 0
}
