package doc

// Unit 131 (B5 tranche 1): libmpscpp.cpp::global::Translate (batch 5,
// simple stratum; LOC 19, score 9.3) - the ONE-argument global member
// (mstring a1), rendered as a GlobalRef extension per the batch-2/3
// member pattern (unit-72 Fnumber precedent). Overload note: the
// two-mstring sibling (vendor libmpscpp.cpp:4264, roster row 270,
// batch 9) is a separate corpus row. Retrieve the global's own value
// with Mglobal(RETRIEVE=0) and throw MumpsGlobalException on
// GLOBAL_NOT_FOUND=2; the vendor mstring Concat chain folds onto the
// CharArray command build (H-014 hit-adapt: the mstring::Concat
// members are out-of-roster class bodies, each a pure string append -
// rendered inline rather than stubbed; journal note); the coded bytes
// are commerce-e/commerce-d-zero/commerce-f, built as Char(nnn) raw
// constants per H-012, and the command target's dollar byte built
// programmatically per H-015 (DOLLAR_131 carrier); the dispatch rides
// the fcn H-014 stub, the call goes to v1d[1] and the result reads
// bd[1] (offset-1 convention, unit 42/72 precedent). Naming per
// H-017 (Translate -> translate).

private const val DOLLAR_131 = '$'

fun GlobalRef.translate(a1: Mstring): Mstring {
    val open = Char(0x00ce)
    val comma = Char(0x00d0)
    val close = Char(0x00cf)
    val tmp = CharArray(STR_MAX)
    val gtmp = CharArray(STR_MAX)

    val f = Stubs.Mglobal(RETRIEVE, ref, gtmp, Globals.svPtr)

    if (f == 0) {
        Globals.GlobalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }

    // m = DOLLAR_131 + "translate"; m = m.Concat(open); m = m.Concat(gtmp);
    // m = m.Concat(comma); m = m.Concat(a1); m = m.Concat(close);
    CstringOps.strcpyS(tmp, DOLLAR_131 + "translate")
    CstringOps.catAt(tmp, 0, open) // offset 0: vendor strcat appends at NUL

    CstringOps.catAt(tmp, 0, CstringOps.cstring(gtmp)) // invoking value
    CstringOps.catAt(tmp, 0, comma)

    CstringOps.catAt(tmp, 0, a1.c_str()) // first argument required
    CstringOps.catAt(tmp, 0, close)

    // strcpy((char *) &svPtr->v1d[1], m.data.c_str());
    CstringOps.copyAt(Globals.svPtr.v1d, 1, tmp)
    Stubs.fcn(Globals.svPtr)
    val m = Mstring(CstringOps.cstringAt(Globals.svPtr.bd, 1))
    return m
}
