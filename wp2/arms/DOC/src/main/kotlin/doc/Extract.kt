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

// Unit 168 (B6 tranche 2): interp-sym.cpp::_extract (batch 6, core
// stratum; LOC 34, score 13.0) - the one-based range copy, the
// dollars-extract core behind the unit-32 global::Extract member
// of this file. RESOLVES the Stubs.Mextract seat raised at unit
// 32 with a TAG CORRECTION: the stub's "batch-10 corpus" owner tag
// was a mis-tag - this IS roster row 168, batch 6; the superseded
// note is kept verbatim in Stubs. The unit-32 call site binds
// unchanged on the 4-CharArray signature (out, in, start, len).
//
// Semantics kept verbatim: start/len are 1-based decimal strings
// via the atoi seat; negative start clamps to 0 else decrements,
// negative len means to-the-end (j=i) else decrements; the
// empty-result test is j<i not j<=i (a one-element request falls
// through to the copy); the i==0 && j==0 arm copies the single
// first byte; the bounds test is i>n not i>=n (i==n copies the
// NUL terminator into out[0] - an empty output produced by
// copying the terminator, in-bounds both languages); j>n clamps
// AFTER the i>n rejection so the copy may read the NUL as the
// last byte. The empty-result arms ride the Stubs.strmove stub
// seat (inline.h:38, not a corpus unit) with the one-NUL
// empty-literal carrier - the H-014 note of unit 37 applies.
// h015: vendor parameter name in -> in_ (Kotlin keyword).
// Naming per H-017 (_extract kept verbatim, leading underscore).

fun _extract(out: CharArray, in_: CharArray, start: CharArray, len: CharArray) {
    var i = atoi(CstringOps.cstring(start))
    var j = atoi(CstringOps.cstring(len))

    if (i < 0) i = 0 else i-- // 1-based to 0-based

    if (j < 0) j = i else j-- // negative len: to the end

    if (j < i) {
        Stubs.strmove(out, charArrayOf(NUL))
        return
    }

    if (i == 0 && j == 0) {
        out[0] = in_[0]
        out[1] = NUL
        return
    }

    val n = CstringOps.strlenC(in_)

    if (i > n) {
        Stubs.strmove(out, charArrayOf(NUL))
        return
    }

    if (j > n) j = n

    var k = 0
    while (i <= j) {
        out[k] = in_[i]
        k++
        i++
    }
    out[k] = NUL
    return
}
