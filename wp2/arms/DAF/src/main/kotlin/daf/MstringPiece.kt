package daf

/*
 * DAF batch-10 unit 304 (roster row 304; libmpscpp.cpp::mstring::
 * Piece, LOC 17, ptr 0, score 5.5). Consult 2026-11-25T15:16:00Z.
 *
 * The receiver-swap twin of the unit-188 globalPieceD
 * (GlobalPiece.kt): the mstring receiver keeps data local - NO
 * retrieve; the global twin retrieves first. The C++
 * mstring::Piece overload lattice (const char star, string and
 * mstring twins) collapses onto this one body (the c_str() relay
 * ledger note of the Pattern family precedent).
 *
 * Fold notes: if (jj<0) jj=ii quirk kept; the sprintf %d digit
 * carriers fold onto Locale.US Strings (row 33 family); the
 * _piece(tmp, data.c_str(), pat, cc1, cc2, 0, "", svPtr) call is
 * VERBATIM the pieceDaf kernel bind of the unit-114 seat
 * (InterpPieceMisc.kt), setpiece 0 read path.
 *
 * H-015: ASCII; no raw dollars; trailing newline.
 * Dafny: EXEMPT (service call over the piece kernel; journal
 * row).
 */

// mstring mstring::Piece(const char *pat, int ii, int jj)
fun mstringPieceD(data: String, pat: String, ii: Int, jjIn: Int,
        sv: DafMsv): String {

    var jj = jjIn
    if (jj < 0) jj = ii // if (jj<0) jj=ii;

    val cc1 = String.format(java.util.Locale.US, "%d", ii)
    val cc2 = String.format(java.util.Locale.US, "%d", jj)

    // _piece(tmp, data.c_str(), pat, cc1, cc2, 0, "", svPtr)
    return pieceDaf("", data, pat, cc1, cc2, 0, "", sv)
}
