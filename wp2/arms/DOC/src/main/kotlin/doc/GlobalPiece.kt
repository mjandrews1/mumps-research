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

// Unit 188 (B7 tranche 1): libmpscpp.cpp::global::Piece (batch 7,
// simple stratum; LOC 17, score 5.5, rank 32) - the STRING-needle
// member overload (vendor libmpscpp.cpp:7236, physically verified
// this session; roster adc clue: pointer_marks 0 - the B8/B10
// sibling rows carry the pointer-bearing char-star/mstring needles,
// and the B8 row pointer_marks 1 is the mstring-ref form; this row
// is the pat.c_str() string form). Single Mglobal(RETRIEVE) over
// the GLOBAL'S OWN ref only (the unit-38 two-retrieve GlobalRef
// needle sibling is the landed seat above), GLOBAL_NOT_FOUND
// throw, the jj<0 collapse to ii kept as the unit-38 local-val
// quirk, sprintf decimals via strcpyS, empty "modes" CharArray,
// _piece on the unit-114 real (H-014; the Stubs.Mpiece seat flips
// to this real in this push - the vendor calls _piece directly).
// Naming per H-017 (Piece -> piece); fold: the Kotlin String
// parameter is the C string form verbatim (no carrier conversion).

fun GlobalRef.piece(pat: String, ii: Int, jjIn: Int): Mstring {
    val tmp = CharArray(STR_MAX)
    val cc1 = CharArray(65)
    val cc2 = CharArray(64)
    val gtmp = CharArray(STR_MAX)
    val patC = CharArray(STR_MAX)
    CstringOps.strcpyS(patC, pat)

    val f = Stubs.Mglobal(RETRIEVE, ref, gtmp, Globals.svPtr)

    if (f == 0) {
        Globals.GlobalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }

    val jj = if (jjIn < 0) ii else jjIn

    // sprintf(cc1, "%d", ii) and sprintf(cc2, "%d", jj): plain decimals.
    CstringOps.strcpyS(cc1, ii.toString())
    CstringOps.strcpyS(cc2, jj.toString())

    _piece(tmp, gtmp, patC, cc1, cc2, 0, CharArray(0), Globals.svPtr)
    val tmp1 = Mstring(CstringOps.cstring(tmp))
    return tmp1
}

// Unit 224 (B8 tranche 1): libmpscpp.cpp::global::Piece (batch 8,
// simple stratum; LOC 17, score 7.3, rank 48; units.csv row 239;
// vendor libmpscpp.cpp:7259, physically verified this session) -
// the MSTRING-needle member overload (mstring & pat; the
// unit-188 roster-pointer adjudication holds: the batch-8 row's
// pointer_marks 1 is this mstring-ref form, the char-star twin at
// :7181 stays a future-batch row). Consult H-007 hit-reuse: the
// body is the unit-188 String render verbatim with the needle
// carrier swapped - pat.data.c_str() rides the Mstring str
// carrier into the same CharArray needle seat, single
// Mglobal(RETRIEVE) over the global's own ref, GLOBAL_NOT_FOUND
// throw, the jj<0 collapse to ii, strcpyS decimals, _piece real
// (unit-114), Mstring return. The Kotlin overload resolves by
// parameter type against the String and GlobalRef needles; each
// roster row keeps its seat. Naming per H-017 (Piece -> piece).

fun GlobalRef.piece(pat: Mstring, ii: Int, jjIn: Int): Mstring {
    val tmp = CharArray(STR_MAX)
    val cc1 = CharArray(65)
    val cc2 = CharArray(64)
    val gtmp = CharArray(STR_MAX)
    val patC = CharArray(STR_MAX)
    CstringOps.strcpyS(patC, pat.str)

    val f = Stubs.Mglobal(RETRIEVE, ref, gtmp, Globals.svPtr)

    if (f == 0) {
        Globals.GlobalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }

    val jj = if (jjIn < 0) ii else jjIn

    // sprintf(cc1, "%d", ii) and sprintf(cc2, "%d", jj): plain decimals.
    CstringOps.strcpyS(cc1, ii.toString())
    CstringOps.strcpyS(cc2, jj.toString())

    _piece(tmp, gtmp, patC, cc1, cc2, 0, CharArray(0), Globals.svPtr)
    val tmp1 = Mstring(CstringOps.cstring(tmp))
    return tmp1
}
