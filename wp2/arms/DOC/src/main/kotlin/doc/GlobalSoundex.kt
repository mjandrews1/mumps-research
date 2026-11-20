package doc

// Unit 193 (B7 tranche 1): libmpscpp.cpp::global::Soundex (batch
// 7, simple stratum; LOC 17, score 8.7, rank 80) - the GLOBAL
// member (vendor libmpscpp.cpp:4200, physically verified this
// session; nonblank 17 matches the roster row; the mstring
// member sibling at :4182 is a later-batch row - the roster id's
// global::Soundex is this member). Body: Mglobal(RETRIEVE) over
// the global's own ref, GLOBAL_NOT_FOUND throw, then the unit-131
// command-build fold: the dollar-z-z-soundex intrinsic text
// (H-015: no raw dollars in source; DOLLAR_SX const carrier),
// commerce-e open, the retrieved value, commerce-f close, through
// the fcn H-014 stub at v1d offset 1, result read from bd at
// offset 1 (the unit-131/72 offset-1 convention). Naming per
// H-017 (Soundex -> soundex; the dispatch-side strcasecmpC
// zzsoundex seats of units 183/184 are the interpreter twins of
// this intrinsic call).

private const val DOLLAR_SX = '$'

fun GlobalRef.soundex(): Mstring {
    val open = Char(0x00ce)
    val close = Char(0x00cf)
    val tmp = CharArray(STR_MAX)
    val gtmp = CharArray(STR_MAX)

    var f = Stubs.Mglobal(RETRIEVE, ref, gtmp, Globals.svPtr)

    if (f == 0) {
        Globals.GlobalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }

    // m = DOLLAR_SX + "zzsoundex"; m = m.Concat(open); m = m.Concat(gtmp);
    // m = m.Concat(close);
    CstringOps.strcpyS(tmp, DOLLAR_SX + "zzsoundex")
    CstringOps.catAt(tmp, 0, open)
    CstringOps.catAt(tmp, 0, CstringOps.cstring(gtmp))
    CstringOps.catAt(tmp, 0, close)

    // strcpy((char *) &svPtr->v1d[1], m.data.c_str());
    CstringOps.copyAt(Globals.svPtr.v1d, 1, tmp)

    Stubs.fcn(Globals.svPtr)

    val m = Mstring(CstringOps.cstringAt(Globals.svPtr.bd, 1))
    return m
}
