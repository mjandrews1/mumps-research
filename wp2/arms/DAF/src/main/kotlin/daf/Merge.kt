package daf

// Unit 237 (DAF B8 T3, complex): interp-sysfunc.cpp::Merge
// (59 LOC, score 30.1): the MERGE primitive: copies the top-level
// reference, then walks the right subtree level by level (XNEXT),
// keyfix-mangling both sides into the padded-key domain, and
// stores each node into the left target (vendor
// interp-sysfunc.cpp:719 area, physically verified this session).
// SEAT RELEASE + TAG CORRECTION: the mergeDaf named stub of
// Interpret.kt is released - the real body lands here under the
// stub's own name and signature (the Interpret.kt:2171 dispatcher
// call site and the LibmpscppMisc6 global::Merge relay resolve
// unchanged), the stub is deleted; the stub's "not drawn by the
// DAF arm" tag is superseded - the units.csv row 226 IS THIS
// batch's row (unit 237 per the plan-position numbering).
// Consults (2026-11-24T21:20:30Z, nominal): all Mglobal rides go
// through the MglobalDaf real with the STORE_D/RETRIEVE_D/XNEXT_D
// codes; keyfix rides its keyfixDaf named stub seat (the
// keyfix.h inline, out-of-scope owner - another in-arm caller,
// seat goes home unchanged); the strcpy/strlen/strncmp folds ride
// file-local helpers (the per-file private-helper convention);
// H-018 preprocessor ledger: the MDEBUG banner block is INERT on
// the reference build (kept as the comment fossil below); the
// SQLITE guard pair is EMPTY in the vendor (matched-arm ledger
// note, SQLITE live per the reference build config); the FIX
// (char *) casts of the tail strcpy are the plain offset splice
// (row-30 pointer fold). The 128-quirk tail walk, the SOH-pad
// else arms and the handle-first-case retrieve-then-store swap
// over tmp4 are verbatim.
// Dafny: EXEMPT (global-tree walk over Mglobal seats; journal
// row).

// MDEBUG is off on the reference build; the vendor printf banner
// block (MERGE left=... right=... with the byte-dump loops) is
// dead - inert-arm ledger.

// #if defined(SQLITE) : empty matched arm in the vendor - ledger.

fun mergeDaf(left: CharArray, right: CharArray, svPtr: DafMsv): Int {

    // unsigned char tmp1..tmp4[STR_MAX] function scratch
    val tmp1 = CharArray(STR_MAX_DAF)
    val tmp2 = CharArray(STR_MAX_DAF)
    val tmp3 = CharArray(STR_MAX_DAF)
    val tmp4 = CharArray(STR_MAX_DAF)
    var i: Int
    var j = 0
    var k = 0

    strcpyMergeD(tmp2, right)
    strcpyMergeD(tmp3, left)

    i = MglobalDaf(RETRIEVE_D, tmp2, tmp1, svPtr) /* copy top refs */

    if (i != 0) MglobalDaf(STORE_D, tmp3, tmp1, svPtr)

    strcpyMergeD(tmp2, right)
    i = strlenMergeD(tmp2)

    // for (j=i-1; j>0; j--) if (tmp2[j]>127) break;
    for (jj in i - 1 downTo 1) {
        if (tmp2[jj] > Char(127)) {
            j = jj
            break
        }
    }

    if (j != 0) {
        tmp2[j + 1] = NUL_D
    } else {
        tmp2[i++] = '\u0001'
        tmp2[i] = NUL_D
        j = i
    }

    keyfixDaf(tmp2)
    j = strlenMergeD(tmp2)

    strcpyMergeD(tmp3, left)
    i = strlenMergeD(tmp3)

    // for (k=i-1; k>0; k--) if (tmp3[k]>127) break;
    for (kk in i - 1 downTo 1) {
        if (tmp3[kk] > Char(127)) {
            k = kk
            break
        }
    }

    if (k != 0) {
        tmp3[k + 1] = NUL_D
    } else {
        tmp3[i++] = '\u0001'
        tmp3[i] = NUL_D
        k = i
    }

    keyfixDaf(tmp3)
    k = strlenMergeD(tmp3)

    if (MglobalDaf(RETRIEVE_D, right, tmp1, svPtr) != 0) { // handle first case - may not exist
        strcpyMergeD(tmp4, tmp3)
        MglobalDaf(STORE_D, tmp3, tmp1, svPtr)
        strcpyMergeD(tmp3, tmp4)
    }

    while (true) {

        if (MglobalDaf(XNEXT_D, right, tmp1, svPtr) == 0) break

        strcpyMergeAtD(tmp3, k, right, j) // strcpy(FIX &tmp3[k], FIX &right[j]);

        if (strncmpMergeD(right, tmp2, j) != 0) break

        MglobalDaf(RETRIEVE_D, right, tmp1, svPtr)

        strcpyMergeD(tmp4, tmp3)
        MglobalDaf(STORE_D, tmp3, tmp1, svPtr)
        strcpyMergeD(tmp3, tmp4)
    }

    return 0
}

// file-local strcpy/strlen/strncmp/splice folds (the per-file
// private-helper convention)
private fun strcpyMergeD(dst: CharArray, src: CharArray) {
    var q = 0
    while (src[q] != NUL_D) {
        dst[q] = src[q]
        q++
    }
    dst[q] = NUL_D
}

private fun strlenMergeD(buf: CharArray): Int {
    var q = 0
    while (buf[q] != NUL_D) q++
    return q
}

private fun strncmpMergeD(a: CharArray, b: CharArray, n: Int): Int {
    var q = 0
    while (q < n) {
        val x = a[q]
        val y = b[q]
        if (x != y) return x - y
        if (x == NUL_D) return 0
        q++
    }
    return 0
}

// strcpy((char *) &dst[doff], (const char *) &src[soff])
private fun strcpyMergeAtD(dst: CharArray, doff: Int, src: CharArray, soff: Int) {
    var q = 0
    while (src[soff + q] != NUL_D) {
        dst[doff + q] = src[soff + q]
        q++
    }
    dst[doff + q] = NUL_D
}
