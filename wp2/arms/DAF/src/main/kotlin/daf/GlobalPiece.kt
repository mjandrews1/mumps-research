package daf

// Unit 188 (DAF B7 T1, simple): libmpscpp.cpp::global::Piece
// (17 LOC, score 5.5): the global-class Piece member: retrieve the
// receiver ref, then relay to the _piece service kernel with the
// pattern, the two integer bounds sprintf'd into C-string carriages
// and setpiece 0 (pure read); the out value returns as the mstring.
// Consults (2026-11-23T18:31:00Z, nominal): BASE-log row 97
// hit-reuse (the \u0024piece service-call shape over the _piece kernel
// with setpiece 0: the out-buffer of the read path folds onto the
// returned String per the row-97 BASE fold; the kernel seat is the
// unit-114 T3 pieceDaf already live - this render is the first
// in-arm call of the read path), row 92 hit-reuse (the
// Mglobal(RETRIEVE) retrieve-then-throw on the ref with
// GlobalExceptionCode = GLOBAL_NOT_FOUND), row 54 hit-reuse (the
// node receiver ref String through the name-carriage carrier),
// rows 33 hit-adapt (the char tmp[STR_MAX]/cc1[65]/cc2[64] scratch
// and the two sprintf("%d") passes: the integer bounds fold onto
// Locale.US String.format digits per the unit-152 LibmpscppMisc9
// sprintf("%d") precedent; CC1 65/CC2 64 sizes ride the String
// digits, no fixed carriers needed), row 8 family hit-reuse (gtmp
// the buffer-role read: Mglobal out-carriage only).
// Vendor quirk kept: jj<0 rebinding to ii happens BEFORE the
// sprintf carriages (the bounds the kernel sees are the rebound
// pair).
// Dafny: EXEMPT (service call over the piece kernel seat; journal
// row).

fun globalPieceD(n: MpsGlobalNode, pat: String, ii: Int, jjIn: Int, sv: DafMsv): String {

    val gtmp = CharArray(STR_MAX_DAF)

    val refCarriage = piece186Name(n.ref)
    val f = MglobalDaf(RETRIEVE_D, refCarriage, gtmp, sv)
    if (f == 0) {
        GlobalExceptionCodeD = GLOBAL_NOT_FOUND_D
        throw MumpsGlobalExceptionD("global.Piece: global not found")
    }

    var jj = jjIn
    if (jj < 0) jj = ii // if (jj<0) jj=ii; - quirk order kept

    // sprintf(cc1,"%d",ii); sprintf(cc2,"%d",jj); - the row-33 digit
    // carriers fold onto formatted digit Strings
    val cc1 = String.format(java.util.Locale.US, "%d", ii)
    val cc2 = String.format(java.util.Locale.US, "%d", jj)

    // _piece(tmp, gtmp, pat, cc1, cc2, 0, "", svPtr) - setpiece 0
    // read path; the row-97 out-buffer fold returns the piece
    return pieceDaf("", DafCRuntime.cstring(gtmp), pat, cc1, cc2, 0, "", sv)
}

private fun piece186Name(src: String): CharArray {
    val out = CharArray(STR_MAX_DAF)
    val s = src.toCharArray()
    var i = 0
    while (i < s.size) {
        out[i] = s[i]
        i++
    }
    out[i] = NUL_D
    return out
}

// ---------------- Unit 224 row-note (DAF B8 T1) -----------------
//
// Unit 224 (DAF B8 T1, simple): libmpscpp.cpp::global::Piece
// (17 LOC, score 7.3; units.csv row 239; vendor :7181, physically
// verified this session) - the CONST-CHAR-STAR needle overload.
// VERIFICATION row (the unit-214 carrier-fold convention): the
// :7181 body verified line-for-line against the landed unit-188
// globalPieceD String seat above - single Mglobal(RETRIEVE) over
// the global's own ref, GLOBAL_NOT_FOUND retrieve-then-throw, the
// jj<0 collapse to ii BEFORE the sprintf carriages, sprintf %d
// decimals, the _piece setpiece-0 read path with the empty modes
// string, the mstring return fold. The ONLY difference is the
// needle carrier ((const char *)pat direct vs the string/mstring
// c_str() forms); the landed String parameter is the fold of all
// the C++ needle carriers (the same audit the DOC arm recorded for
// its row-239/u214 family). THIS ROW RENDERS NO NEW BODY: the
// unit-188 seat carries it, each roster row keeps its seat.
// Consults (2026-11-24T21:14:00Z, nominal): verification-only
// ledger append (the u186 fold precedent of the Find family).
// Dafny: EXEMPT (verification row; journal row).
