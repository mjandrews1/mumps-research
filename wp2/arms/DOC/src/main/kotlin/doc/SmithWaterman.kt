package doc

// Unit 158 (B6 tranche 1): libmpscpp.cpp::SmithWaterman (batch 6,
// simple stratum; LOC 17, score 7.7). The free Smith-Waterman
// alignment wrapper over the sw core (unit 42 render, Sw.kt):
// copies both subjects into STR_MAX carriers with a leading
// blank (the vendor padding, kept), parses the five int
// parameters via sscanf and packs the score via sprintf percent
// d. The C static char tmp[32] folds to a fresh String return
// per H-008. The ep and MSV parameters are dead (H-013, kept
// in shape).
//
// Vendor quirk kept and noted: the C call passes sax and smx
// straight into the third and fourth int parameters of sw, which
// the sw render (Sw.kt) binds as ShowAlign and ShowMatrix - the
// wrapper thus feeds its alignment/matrix toggles from parsed
// user strings; the render forwards the nonzero test of the
// vendor call shape (sax != 0, smx != 0) into the Boolean
// carriers. sscanf percent d renders as the atoi prefix-scan
// seat (Find.kt family; the malformed-string UB of C sscanf
// quantizes to 0 identically under atoi, divergence noted).
// sprintf percent d renders as Int.toString (H-009; no Locale
// dependence in the d conversion). Naming per H-017.

fun SmithWaterman(
    svptr: Msv, ep: String, s1: CharArray, s2: CharArray,
    sa: CharArray, sm: CharArray, gap: CharArray,
    mm: CharArray, ma: CharArray
): String {
    val s1a = CharArray(STR_MAX)
    val s2a = CharArray(STR_MAX)

    s1a[0] = ' '
    s2a[0] = ' '
    CstringOps.copyAt(s1a, 1, s1)
    CstringOps.copyAt(s2a, 1, s2)

    val sax = atoi(CstringOps.cstring(sa))
    val smx = atoi(CstringOps.cstring(sm))
    val gapx = atoi(CstringOps.cstring(gap))
    val mmx = atoi(CstringOps.cstring(mm))
    val max = atoi(CstringOps.cstring(ma))

    // vendor: sax = sw(s1a, s2a, sax, smx, gapx, mmx, max);
    // the third/fourth int args land in the ShowAlign/ShowMatrix
    // seats (vendor quirk kept; nonzero test forward).
    val r = sw(s1a, s2a, sax != 0, smx != 0, gapx, mmx, max)

    return r.toString()
}
