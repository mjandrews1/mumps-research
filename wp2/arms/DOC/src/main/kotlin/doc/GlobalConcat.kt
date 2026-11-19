package doc

// Unit 65 (B3 tranche 1): libmpscpp.cpp::global::Concat (batch 3,
// simple stratum; LOC 18, score 5.4) - the global-argument member of
// the concatenation family, rendered as a GlobalRef extension (the
// batch-2 class-global member pattern, Extract precedent). Two
// Mglobal(RETRIEVE) walks, each throwing MumpsGlobalException on
// GLOBAL_NOT_FOUND=2; vendor quirk kept: both arms set the same code
// even when the SECOND retrieval fails after the first succeeded
// (the first retrieved value is then discarded by the throw; noted).
// The plain string cat of the two retrieved values is the body.
// Mglobal is the H-014 stub; unsigned char* domains per H-012.

fun GlobalRef.concat(b: GlobalRef): Mstring {
    val gtmp = CharArray(STR_MAX)
    val gtmp1 = CharArray(STR_MAX)

    var f = Stubs.Mglobal(RETRIEVE, ref, gtmp, Globals.svPtr)

    if (f == 0) {
        Globals.GlobalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }

    f = Stubs.Mglobal(RETRIEVE, b.ref, gtmp1, Globals.svPtr)

    if (f == 0) { // quirk: same code as the first arm (noted above)
        Globals.GlobalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }

    CstringOps.catAt(gtmp, 0, gtmp1) // strcat(gtmp,gtmp1)

    val rslt = Mstring(CstringOps.cstring(gtmp))
    return rslt
}
