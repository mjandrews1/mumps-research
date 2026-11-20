package doc

// Unit 171 (B6 tranche 2): libmpscpp.cpp::global::Max (batch 6,
// core stratum; LOC 26, score 14.8; the roster id keeps the
// global:: qualifier) - the max-over-children walk, the unit-44
// global::Min sibling (identical bodies modulo the max/val>max
// accumulate). RESOLVES the Stubs.globalMax stub raised by the
// unit-68 free Max wrapper with a TAG CORRECTION: the unit-68 KDoc
// called the member "NOT a corpus unit" - roster row 171 IS batch
// 6; the superseded note is kept verbatim in Stubs and the
// Max.kt KDoc bullet updates with this render. The unit-68 call
// sites bind unchanged (Stubs.globalMax delegates to this seat).
//
// Render in the Cosine/KiLL family shape: STR_MAX scratch, the
// trailing-control two-byte trim quirk (the i-1/i-2 reads on the
// zero-padded carrier, unit-68 note applies), the Mglobal XNEXT
// walk with the strncmp prefix stop, get_global on the tmp1 copy
// with the tpx==0 GLOBAL_NOT_FOUND throw, strtod accumulate, the
// flg first-value latch, and the no-child 0.0 return of the max
// initialization kept verbatim. h013: the vendor's dead local
// int j is dropped with this note. h015: vendor local val is a
// Kotlin hard keyword - rendered value. Naming per H-017
// (global::Max -> GlobalRef.max).

fun GlobalRef.max(): Double {
    val ttt = CharArray(STR_MAX)
    val tmp = CharArray(STR_MAX)
    val tmp1 = CharArray(STR_MAX)
    val rslt = CharArray(STR_MAX)

    var flg = 1
    var max = 0.0

    CstringOps.strcpyS(tmp, ref)
    var i = CstringOps.strlenC(tmp)

    // trailing-control pair trim (the tmp[i-1]/tmp[i-2] reads on
    // the zero-padded carrier; vendor expression kept)
    if (tmp[i - 1] < ' ' && tmp[i - 2] < ' ') {
        i--
        tmp[i] = NUL
    }

    while (true) {
        if (Stubs.Mglobal(XNEXT, tmp, ttt, Globals.svPtr) == 0) break

        if (CstringOps.strncmpS(ref, tmp, i) != 0) break

        CstringOps.copyAt(tmp1, 0, tmp)
        Stubs.get_global(tmp1, rslt)

        if (Globals.svPtr.tpx == 0L) {
            Globals.GlobalExceptionCode = GLOBAL_NOT_FOUND
            throw MumpsGlobalException()
        }

        val value = strtod(CstringOps.cstring(rslt))

        if (flg != 0) {
            max = value
            flg = 0
            continue
        }

        if (value > max) max = value
    }

    return max
}
