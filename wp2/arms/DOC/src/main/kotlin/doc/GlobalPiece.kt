package doc

// Unit 38: libmpscpp.cpp::global::Piece (batch 2, simple stratum). The
// unit is the global-arg member overload Piece(global, int, int). Vendor
// quirk kept: jj<0 collapses to ii via assignment to the parameter (kept
// as a local val); two retrieval checks throw identically.

fun GlobalRef.piece(pat: GlobalRef, ii: Int, jjIn: Int): Mstring {
    val tmp = CharArray(STR_MAX)
    val cc1 = CharArray(65)
    val cc2 = CharArray(64)
    val gtmp = CharArray(STR_MAX)
    val gtmp1 = CharArray(STR_MAX)

    var f = Stubs.Mglobal(RETRIEVE, pat.ref, gtmp1, Globals.svPtr)

    if (f == 0) {
        Globals.GlobalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }

    f = Stubs.Mglobal(RETRIEVE, ref, gtmp, Globals.svPtr)

    if (f == 0) {
        Globals.GlobalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }

    val jj = if (jjIn < 0) ii else jjIn

    // sprintf(cc1, "%d", ii) and sprintf(cc2, "%d", jj): plain decimals.
    CstringOps.strcpyS(cc1, ii.toString())
    CstringOps.strcpyS(cc2, jj.toString())

    Stubs.Mpiece(tmp, gtmp, gtmp1, cc1, cc2, 0, CharArray(0), Globals.svPtr)
    val tmp1 = Mstring(CstringOps.cstring(tmp))
    return tmp1
}
