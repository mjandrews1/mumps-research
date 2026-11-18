package doc

// Unit 32: libmpscpp.cpp::global::Extract (batch 2, simple stratum).
// Vendor source rendered verbatim below the Kotlin body in spirit: the
// retrieval-then-extract flow and the two failure-identical checks are
// kept exactly as written in the vendor file.

fun GlobalRef.extract(st: Int, ln: Int): Mstring {
    val a = CharArray(40)
    val b = CharArray(40)
    val r = CharArray(STR_MAX)
    val tmp = CharArray(STR_MAX)

    val f = Stubs.Mglobal(RETRIEVE, ref, tmp, Globals.svPtr)

    if (f == 0) {
        Globals.GlobalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }

    Stubs.Mltoa(st, a)
    Stubs.Mltoa(ln, b)
    Stubs.Mextract(r, tmp, a, b)
    val rr = Mstring(CstringOps.cstring(r))
    return rr
}
