package doc

// Unit 246 (B8 tranche 3): interp-sysfunc.cpp::Merge (batch 8,
// complex stratum; LOC 59, score 30.1, rank 206; units.csv row
// 226). The MERGE primitive: copies the top-level reference, then
// walks the right subtree level by level (XNEXT), keyfix-mangling
// both sides into the padded-key domain (the private scratch of
// the vendor), and stores each node into the left target. All
// Mglobal rides go through the landed Stubs.Mglobal seats with
// the Carriers RETRIEVE/STORE/XNEXT codes; keyfix rides its
// Stubs.keyfix seat (raised earlier, out-of-scope owner the
// keyfix.h inline; single-name seat rule). Consult H-014
// hit-reuse: every seat the body touches is a landed delegate or
// an already-tagged stub, whole-arm pattern. H-018 preprocessor
// ledger: the MDEBUG banner block is INERT on the reference
// build (kept as the comment fossil below); the SQLITE guard
// pair is EMPTY in the vendor (matched-arm ledger note, SQLITE
// live per the reference build config). Naming per H-017
// (Merge verbatim; vendor left/right parameter names kept).

private const val STR_MAX_MG = STR_MAX

// MDEBUG is off on the reference build; the vendor printf banner
// block (MERGE left=... right=... with the byte-dump loops) is
// dead - H-018 inert-arm ledger.

// #if defined(SQLITE) : empty matched arm in the vendor - ledger.

fun Merge(left: CharArray, right: CharArray, svPtr: Msv): Int {
    // unsigned char tmp1..tmp4[STR_MAX] function scratch
    val tmp1 = CharArray(STR_MAX_MG)
    val tmp2 = CharArray(STR_MAX_MG)
    val tmp3 = CharArray(STR_MAX_MG)
    val tmp4 = CharArray(STR_MAX_MG)
    var i: Int
    var j = 0
    var k = 0

    CstringOps.strcpyC(tmp2, right)
    CstringOps.strcpyC(tmp3, left)

    i = Stubs.Mglobal(RETRIEVE, tmp2, tmp1, svPtr) /* copy top refs */

    if (i != 0) Stubs.Mglobal(STORE, tmp3, tmp1, svPtr)

    CstringOps.strcpyC(tmp2, right)
    i = CstringOps.strlenC(tmp2)

    for (jj in i - 1 downTo 1) if (tmp2[jj] > 127.toChar()) { j = jj; break }

    if (j != 0) tmp2[j + 1] = NUL
    else {
        tmp2[i++] = 1.toChar()
        tmp2[i] = NUL
        j = i
    }

    Stubs.keyfix(tmp2)
    j = CstringOps.strlenC(tmp2)

    CstringOps.strcpyC(tmp3, left)
    i = CstringOps.strlenC(tmp3)

    for (kk in i - 1 downTo 1) if (tmp3[kk] > 127.toChar()) { k = kk; break }

    if (k != 0) tmp3[k + 1] = NUL
    else {
        tmp3[i++] = 1.toChar()
        tmp3[i] = NUL
        k = i
    }

    Stubs.keyfix(tmp3)
    k = CstringOps.strlenC(tmp3)

    if (Stubs.Mglobal(RETRIEVE, right, tmp1, svPtr) != 0) { // handle first case - may not exist
        CstringOps.strcpyC(tmp4, tmp3)
        Stubs.Mglobal(STORE, tmp3, tmp1, svPtr)
        CstringOps.strcpyC(tmp3, tmp4)
    }

    while (true) {

        if (Stubs.Mglobal(XNEXT, right, tmp1, svPtr) == 0) break

        strcpyMergeAt(tmp3, k, right, j)

        if (CstringOps.strncmpC(right, tmp2, j) != 0) break

        Stubs.Mglobal(RETRIEVE, right, tmp1, svPtr)

        CstringOps.strcpyC(tmp4, tmp3)
        Stubs.Mglobal(STORE, tmp3, tmp1, svPtr)
        CstringOps.strcpyC(tmp3, tmp4)
    }

    return 0
}

// strcpy((char *) &dst[doff], (const char *) &src[soff])
private fun strcpyMergeAt(dst: CharArray, doff: Int, src: CharArray, soff: Int) {
    var i = 0
    while (soff + i < src.size && src[soff + i] != NUL) {
        dst[doff + i] = src[soff + i]
        i++
    }
    dst[doff + i] = NUL
}
