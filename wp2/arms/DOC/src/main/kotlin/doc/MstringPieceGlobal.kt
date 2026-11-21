package doc

// Unit 284 (B10 tranche 1): libmpscpp.cpp::mstring::Piece (batch
// 10, simple stratum; units.csv row 305: loc 17, cr 0.056, ptr
// 0, ctl 2, score 5.5, rank 34; physically verified this
// session at vendor libmpscpp.cpp:7332-7354, physLOC 17) - the
// RECEIVER-REVERSED twin of the unit-224/188/38 GlobalRef.Piece
// family (the same-name member on the mstring receiver: the
// key comes from the database, the SOURCE is THIS mstring's
// value). Single Mglobal(RETRIEVE) over the KEY global's ref -
// pat.ref, the u38 conflation convention (the RECEIVER is a
// plain mstring, no second retrieval) - GLOBAL_NOT_FOUND throw
// on f==0 (MumpsGlobalException, the family carrier). The
// _piece call swaps the source/needle pair vs the global-
// receiver family: the receiver's own data.c_str() value rides
// the str carrier into the CharArray SOURCE seat, the retrieved
// key is the NEEDLE (H-007 buffer roles, the reversal IS this
// unit's quirk ledger). jj<0 collapses to ii (the family local
// val); sprintf %d decimals via strcpyS (H-009); the empty
// modes CharArray; _piece real (unit-114); Mstring return.
// Naming per H-017 (Piece -> piece; the Mstring receiver
// keeps the overload set clear of the GlobalRef family).

fun Mstring.piece(pat: GlobalRef, ii: Int, jjIn: Int): Mstring {
    val tmp = CharArray(STR_MAX)
    val cc1 = CharArray(65)
    val cc2 = CharArray(64)
    val gtmp = CharArray(STR_MAX)

    // the receiver's value is the SOURCE: data.c_str()
    val src = CharArray(STR_MAX)
    CstringOps.strcpyS(src, str)

    val f = Stubs.Mglobal(RETRIEVE, pat.ref, gtmp, Globals.svPtr)

    if (f == 0) {
        Globals.GlobalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }

    val jj = if (jjIn < 0) ii else jjIn

    // sprintf(cc1, "%d", ii) and sprintf(cc2, "%d", jj): plain decimals.
    CstringOps.strcpyS(cc1, ii.toString())
    CstringOps.strcpyS(cc2, jj.toString())

    // the reversed pair vs the global family: source is this
    // mstring, needle is the retrieved key
    _piece(tmp, src, gtmp, cc1, cc2, 0, CharArray(0), Globals.svPtr)
    val tmp1 = Mstring(CstringOps.cstring(tmp))
    return tmp1
}
