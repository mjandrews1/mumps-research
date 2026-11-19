package doc

// Unit 72 (B3 tranche 1): libmpscpp.cpp::global::Fnumber (batch 3,
// simple stratum; LOC 23, score 9.9) - the ONE-argument global member
// (mstring a), rendered as a GlobalRef extension. Overload notes: the
// two-argument global wrapper, the mstring member pair (unit 42
// rendered the two-mstring form, Fnumber.kt) and the declared global
// two-arg wrapper are separate corpus rows. Retrieve the global's
// value with Mglobal(RETRIEVE=0) and throw MumpsGlobalException on
// GLOBAL_NOT_FOUND=2; the coded call builds with the CodedOpen/
// CodedComma/CodedClose bytes (defines.h:38-40, carried as Char
// arrays per H-012) and the dollar byte rebuilt programmatically per
// H-015 (unit-42 DOLLAR precedent); the dispatch rides the fcn
// H-014 stub; the call goes to v1d[1] and the result reads bd[1]
// (offset-1 convention, unit 42 precedent). Unlike the two-mstring
// form there is NO dead null-branch here. Naming per H-017.

private const val DOLLAR_72 = '$'

fun GlobalRef.fnumber(a: Mstring): Mstring {
    val tmp = CharArray(STR_MAX)
    val rslt = Mstring("")
    val open = charArrayOf(CodedOpen.toChar(), NUL)
    val comma = charArrayOf(CodedComma.toChar(), NUL)
    val close = charArrayOf(CodedClose.toChar(), NUL)
    val gtmp = CharArray(STR_MAX)

    val f = Stubs.Mglobal(RETRIEVE, ref, gtmp, Globals.svPtr)

    if (f == 0) {
        Globals.GlobalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }

    CstringOps.strcpyS(tmp, DOLLAR_72 + "fnumber")
    CstringOps.strcatS(tmp, CstringOps.cstring(open))

    CstringOps.strcatS(tmp, CstringOps.cstring(gtmp)) // invoking value
    CstringOps.strcatS(tmp, CstringOps.cstring(comma))

    CstringOps.strcatS(tmp, a.c_str()) // first argument required
    CstringOps.strcatS(tmp, CstringOps.cstring(close))

    CstringOps.copyAt(Globals.svPtr.v1d, 1, tmp)
    Stubs.fcn(Globals.svPtr)
    val rr = Mstring(CstringOps.cstringAt(Globals.svPtr.bd, 1))
    return rr
}
