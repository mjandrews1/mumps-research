package mumps.mdh

import mumps.interp.cstringAt
import mumps.interp.mglobal
import mumps.interp.writeCstring

// ----------------------------------------------------------------------------
// (B10 unit 304) libmpscpp.cpp::global::Piece(mstring &, int, int)
// (vendor reference lines 7259-7281).
// Identity: units.csv row 304 - libmpscpp.cpp::global::Piece, cpp,
// loc 17, comment_ratio 0.056, pointer_marks 0, control_marks 2,
// score 5.5, rank 33 - exact match; the mstring-key twin that
// GlobalPiece.kt's unit-214 mis-tag ledger note said "renders as
// its own overload when claimed" - THIS is that render (the B7
// unit-214 seat owns the String/char-star folds; no redeclaration).
// ----------------------------------------------------------------------------

/**
 * mstring global::Piece(mstring &pat, int ii, int jj): the MUMPS
 * dollar-Piece intrinsic over this global's value with the
 * DELIMITER carried by an mstring (no pattern-global fetch here):
 * one RETRIEVE of this global, the jj<0 -greater jj=ii clamp,
 * the %d collapses, then _piece with pat.data.c_str() as the
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
 *    stubs ledger; row 303 does not resolve it) - direct calls.
 *  - h015 grammar_collision  : GlobalRef receiver mirrors the
 *    unit-38/unit-214 seats; Mstring-key overload is new, no
 *    signature collision with Piece(Global)/Piece(String).
 *  - h017 naming             : Piece keeps the vendor name.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The vendor passes pat.data.c_str() - the mstring's data
 *    member - as the delimiter; rendered via the pat.value carrier.
 *  - RETRIEVE = 0 (defines.h:83); GLOBAL_NOT_FOUND = 2
 *    (libmpscpp.h:71) - both file-private here (u44 precedent).
 *  - The exception gate sets GlobalExceptionCode THEN throws,
 *    the family shape (unit-38 seat identical).
 */
private const val STR_MAX = 32000 // vendor strmax.h:3 (file-private, u44 precedent)
private const val RETRIEVE = 0 // vendor defines.h:83
private const val GLOBAL_NOT_FOUND = 2 // vendor libmpscpp.h:71

private fun toCbuf(s: String): CharArray {
    val b = CharArray(s.length + 1)
    for (i in s.indices) b[i] = s[i]
    b[s.length] = 0.toChar() // NUL terminator
    return b
}

fun GlobalRef.Piece(pat: Mstring, ii: Int, jj: Int): Mstring {
    val tmp = CharArray(STR_MAX)
    val cc1 = CharArray(65)
    val cc2 = CharArray(64)
    val gtmp = CharArray(STR_MAX)
    var jjv = jj // vendor: if (jj<0) jj=ii;

    // vendor: Mglobal(RETRIEVE, (unsigned char *) ref, gtmp, svPtr)
    val refBuf = CharArray(STR_MAX)
    writeCstring(refBuf, g.ref)

    val f = mglobal(RETRIEVE, refBuf, gtmp, MdhState.svPtr)

    if (f == 0) {
        MdhState.globalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }

    if (jjv < 0) jjv = ii

    writeCstring(cc1, ii.toString()) // h009: sprintf "%d" collapse
    writeCstring(cc2, jjv.toString()) // h009

    val patBuf = CharArray(STR_MAX)
    writeCstring(patBuf, pat.value) // vendor: pat.data.c_str()
    val lhs = CharArray(1) // vendor: (unsigned char *) ""
    _piece(tmp, gtmp, patBuf, cc1, cc2, 0, lhs, MdhState.svPtr)

    val tmp1 = Mstring()
    tmp1.value = cstringAt(tmp) // vendor: tmp1=tmp
    return tmp1
}
