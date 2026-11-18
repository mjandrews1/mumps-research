package mumps.mdh

import mumps.interp.cstringAt
import mumps.interp.mglobal
import mumps.interp.writeCstring

// ----------------------------------------------------------------------------
// (B10 unit 305) libmpscpp.cpp::mstring::Piece(global, int, int)
// (vendor reference lines 7332-7349).
// Identity: units.csv row 305 - libmpscpp.cpp::mstring::Piece, cpp,
// loc 17, comment_ratio 0.056, pointer_marks 0, control_marks 2,
// score 5.5, rank 34 - exact match; the global-key twin completing
// the mstring::Piece family (the CharStar/string/mstring seats
// live in LibmpscppMisc8.kt, batch-9 row 280; no redeclaration).
// ----------------------------------------------------------------------------

/**
 * mstring mstring::Piece(global pat, int ii, int jj): the dollar-
 * Piece intrinsic over THIS mstring's value with the delimiter
 * retrieved from another global: fetch pat's global into gtmp,
 * gate, clamp jj, collapse the %d pair, then _piece with this
 * mstring's data as the source and the fetched global as the
 * delimiter pattern.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h007 char_buffer        : tmp[STR_MAX] result, cc1[65]/cc2[64]
 *    argument buffers, gtmp[STR_MAX] Mglobal out buffer.
 *  - h009 format_string     : both sprintf(..., "%d") calls collapse
 *    to the exact decimal strings of ii/jj.
 *  - h012 unsigned_byte_domain: buffers are Char(0..255) domains.
 *  - h014 out_of_scope_call  : mglobal (InterpStubs owner seat) and
 *    _piece (MdhStubs stub, owner interp-piece.cpp:97, kept per the
 *    stubs ledger; row 304 does not resolve it) - direct calls.
 *  - h015 grammar_collision  : Global-key overload is new; no
 *    signature collision with Mstring.Piece(CharArray/String/
 *    Mstring) from LibmpscppMisc8.kt.
 *  - h017 naming             : Piece keeps the vendor name.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The pattern global is fetched with pat.ref as the Mglobal key
 *    (unit-14 conflation; the GlobalPiece.kt pattern-global fetch
 *    is the mirror image - there PAT was the global and HERE the
 *    fetched global IS the delimiter; kept exactly).
 *  - This mstring's data.c_str() is the _piece SOURCE and the
 *    fetched gtmp is the DELIMITER - the reverse of the family's
 *    other members (the Mstring.Pattern(global) seat is the same
 *    reversed-fetch shape); kept verbatim.
 *  - RETRIEVE = 0 (defines.h:83); GLOBAL_NOT_FOUND = 2
 *    (libmpscpp.h:71) - both file-private here (u44 precedent).
 */
private const val STR_MAX = 32000 // vendor strmax.h:3 (file-private, u44 precedent)
private const val RETRIEVE = 0 // vendor defines.h:83
private const val GLOBAL_NOT_FOUND = 2 // vendor libmpscpp.h:71

fun Mstring.Piece(pat: Global, ii: Int, jj: Int): Mstring {
    val tmp = CharArray(STR_MAX)
    val cc1 = CharArray(65)
    val cc2 = CharArray(64)
    val gtmp = CharArray(STR_MAX)
    var jjv = jj // vendor: if (jj<0) jj=ii;

    // vendor: Mglobal(RETRIEVE, (unsigned char *) pat.ref, gtmp, svPtr)
    val refBuf = CharArray(STR_MAX)
    writeCstring(refBuf, pat.ref)

    val f = mglobal(RETRIEVE, refBuf, gtmp, MdhState.svPtr)

    if (f == 0) {
        MdhState.globalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }

    if (jjv < 0) jjv = ii

    writeCstring(cc1, ii.toString()) // h009: sprintf "%d" collapse
    writeCstring(cc2, jjv.toString()) // h009

    // vendor: _piece(tmp, (unsigned char *) data.c_str(), gtmp, ...)
    val srcBuf = CharArray(STR_MAX)
    writeCstring(srcBuf, value) // data.c_str()
    val lhs = CharArray(1) // vendor: (unsigned char *) ""
    _piece(tmp, srcBuf, gtmp, cc1, cc2, 0, lhs, MdhState.svPtr)

    val tmp1 = Mstring()
    tmp1.value = cstringAt(tmp) // vendor: tmp1=tmp
    return tmp1
}
