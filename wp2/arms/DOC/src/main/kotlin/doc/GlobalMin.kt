package doc

// Unit 44: libmpscpp.cpp::global::Min (batch 2, core stratum) - the
// no-argument global member scanning XNEXT siblings of the invoking
// reference for the numeric minimum. Rendered as a GlobalRef extension
// (the vendor class-global member pattern used by batch 2). The vendor
// numeric parse strtod(rslt,NULL) reuses the arm-wide C-protocol strtod
// (MpsExp.kt). Mglobal, get_global and the tpx failure path are
// out-of-scope stubs / shared carriers per H-014.

fun GlobalRef.Min(): Double {
    val ttt = CharArray(STR_MAX)
    val tmp = CharArray(STR_MAX)
    val tmp1 = CharArray(STR_MAX)
    val rslt = CharArray(STR_MAX)
    var flg = 1
    var min = 0.0

    CstringOps.strcpyS(tmp, ref)
    var i = CstringOps.strlenC(tmp)

    if (tmp[i - 1] < Char(32) && tmp[i - 2] < Char(32)) tmp[--i] = NUL

    while (true) {
        if (Stubs.Mglobal(XNEXT, tmp, ttt, Globals.svPtr) == 0) break

        if (CstringOps.strncmpS(ref, tmp, i) != 0) break

        CstringOps.strcpyC(tmp1, tmp)
        Stubs.get_global(tmp1, rslt)

        if (Globals.svPtr.tpx == 0.toLong()) {
            Globals.GlobalExceptionCode = GLOBAL_NOT_FOUND
            throw MumpsGlobalException()
        }

        val v = strtod(CstringOps.cstring(rslt))

        if (flg == 1) {
            min = v
            flg = 0
            continue
        }

        if (v < min) min = v
    }

    return min
}
