package doc

// Unit 49: libmpscpp.cpp::global::IDF (batch 2, core stratum) - the
// inverse-document-frequency transform over XNEXT siblings of the
// invoking reference. LOG2 is kotlin.math.log2 (numeric intrinsic per
// cookbook H-010); the vendor sscanf "%lf" contract is rendered with the
// arm-wide C-protocol strtod (MpsExp.kt) plus a semantic check that the
// parse consumed a value (INVALID_CONVERSION raised otherwise -
// documented divergence: strtod never signals failure itself); printf
// "%.1lf" becomes String.format with Locale.US (locale-fixed per H-009).
// Mglobal/get_global/set_global are out-of-scope stubs per H-014.

fun GlobalRef.IDF(dcIn: Double) {
    var DC = dcIn
    val ttt = CharArray(STR_MAX)
    val tmp = CharArray(STR_MAX)
    val tmp1 = CharArray(STR_MAX)
    val rslt = CharArray(STR_MAX)

    if (DC <= 0.0) {
        Globals.GlobalExceptionCode = DATA_RANGE_ERROR
        throw MumpsGlobalException()
    }

    DC = kotlin.math.log2(DC)
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

        val rsltStr = CstringOps.cstring(rslt)
        if (rsltStr.isEmpty()) {
            // sscanf would report 0 conversions on an empty field;
            // strtod yields 0.0 silently instead - checked explicitly.
            Globals.GlobalExceptionCode = INVALID_CONVERSION
            throw MumpsGlobalException()
        }

        var v = strtod(rsltStr)

        if (v <= 0.0) {
            Globals.GlobalExceptionCode = DATA_RANGE_ERROR
            throw MumpsGlobalException()
        }

        v = DC - kotlin.math.log2(v) + 1.0
        CstringOps.strcpyS(rslt, String.format(java.util.Locale.US, "%.1f", v))
        Stubs.set_global(tmp1, rslt)
    }
}
