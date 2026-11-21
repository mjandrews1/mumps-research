package doc

// Unit 288 (B10 tranche 1): libmpscpp.cpp::global::Justify
// (batch 10, simple stratum; units.csv row 309: loc 18, cr
// 0.052, ptr 0, ctl 2, score 4.2, rank 6; physically verified
// this session at vendor libmpscpp.cpp:7372-7400, physLOC 18)
// - the JUSTIFY MSV member (the unit-89 GlobalClassMisc
// declaration-row Justify(int, int=-1) has its body owner here,
// superseding the plain ledger note). The sprintf %d decimal
// pair (cc1/cc2 via strcpyS, H-009 plain decimals per the
// Piece-family precedent), the single Mglobal(RETRIEVE) of this
// global's own value with the GLOBAL_NOT_FOUND throw, then the
// REAL unit-194 _justify seat (InterpJustify.kt, H-014
// hit-reuse: _justify(tmp, gtmp, cc1, cc2)) over the retrieved
// value, the justified text wrapped in the Mstring return
// tmp1=tmp. Dead locals per H-013: the vendor declares i,
// j=1, k, x UNREFERENCED (kept as this fossil note, dropped
// from the body). buf roles per H-007 (the STR_MAX gtmp/tmp
// carrier pair, the cc1[32]/cc2[32] decimal scratches kept at
// their vendor sizes). Naming per H-017 (Justify -> justify).

fun GlobalRef.justify(c1: Int, c2: Int): Mstring {
    val cc1 = CharArray(32)
    val cc2 = CharArray(32)
    val gtmp = CharArray(STR_MAX)
    val tmp = CharArray(STR_MAX)

    // sprintf(cc1, "%d", c1); sprintf(cc2, "%d", c2)
    CstringOps.strcpyS(cc1, c1.toString())
    CstringOps.strcpyS(cc2, c2.toString())

    val f = Stubs.Mglobal(RETRIEVE, ref, gtmp, Globals.svPtr)

    if (f == 0) {
        Globals.GlobalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }

    _justify(tmp, gtmp, cc1, cc2)

    return Mstring(CstringOps.cstring(tmp))
}
