package mumps.mdh

import mumps.interp._justify
import mumps.interp.cstringAt
import mumps.interp.mglobal
import mumps.interp.writeCstring

// ----------------------------------------------------------------------------
// (B11 unit 309 - STUDY CLOSER) libmpscpp.cpp::global::Justify (vendor
// 7372-7394). Identity: units.csv row 309 - libmpscpp.cpp::global::Justify,
// cpp, loc 18, comment_ratio 0.053, pointer_marks 0, control_marks 1,
// score 4.2, rank 6, simple - exact match (the final roster row).
// ----------------------------------------------------------------------------

/**
 * mstring global::Justify(int c1, int c2): the global-needle
 * Justify twin - retrieve THIS global's value off the database,
 * then _justify it to width c1 / decimals c2 (the <dollar>J
 * rotation: right-justify the value in a c1-wide field, with the
 * fractional part folded to c2 decimals per the _justify seat).
 * The mstring::Justify(int,int) twin is ALREADY DONE
 * (LibmpscppMisc8.kt row 281, vendor 7397 - it _justifies the
 * mstring's own data member; no redeclaration of that seat).
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h007 char_buffer        : tmp/gtmp are the _justify input and
 *    Mglobal out buffers (STR_MAX each); cc1/cc2 are the 32-byte
 *    rendered width/decimals C-strings (kept as small CharArrays).
 *  - h009 format_string      : sprintf(cc1, "%d", c1) /
 *    sprintf(cc2, "%d", c2) collapse to the decimal strings via
 *    writeCstring (the row-304 family precedent).
 *  - h012 unsigned_byte_domain: buffers are Char(0..255) domains;
 *    NUL-closed cstring writes.
 *  - h014 out_of_scope_call  : mglobal is the DONE interp owner
 *    seat (the RETRIEVE-only call, row-303/304 twin shape); the
 *    _justify seat is DONE (interp/Justify.kt, the sysfunc
 *    unpad-just family) - both direct calls, nothing stubbed.
 *  - h015 grammar_collision  : Global receiver Int/Int overload -
 *    NEW; no collision with the Mstring.Justify(int,int) seat of
 *    LibmpscppMisc8.kt nor with the mdh Justify default-parameter
 *    stub ladder folded into LibmpscppH.kt (row 282 ledger).
 *  - h017 naming             : Justify keeps the vendor name;
 *    tmp1=tmp rendered as the Mstring value fold (u44 precedent).
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The unused locals i, j=1, k, x of the vendor declarator
 *    block are kept in this note, not rendered (dead declarations,
 *    h013 shape - the row-304 twin lists them likewise).
 *  - RETRIEVE = 0 (defines.h:83); GLOBAL_NOT_FOUND = 2
 *    (libmpscpp.h:71) - file-private here (u44 precedent).
 *  - The exception gate sets GlobalExceptionCode THEN throws
 *    MumpsGlobalException - the family shape (unit-38 seat
 *    identical to the Piece/Pattern twins).
 *  - Unlike the Piece twins there is NO ii/jj negative clamp
 *    here; the vendor passes cc1/cc2 straight through - kept.
 */
private const val STR_MAX = 32000 // vendor strmax.h:3 (file-private, u44 precedent)
private const val RETRIEVE = 0 // vendor defines.h:83
private const val GLOBAL_NOT_FOUND = 2 // vendor libmpscpp.h:71

fun Global.Justify(c1: Int, c2: Int): Mstring {
    val svPtr = MdhState.svPtr
    val cc1 = CharArray(32) // vendor: char cc1[32]
    val cc2 = CharArray(32) // vendor: char cc2[32]
    val gtmp = CharArray(STR_MAX) // vendor: unsigned char gtmp[STR_MAX]
    val tmp = CharArray(STR_MAX) // vendor: char tmp[STR_MAX]

    writeCstring(cc1, c1.toString()) // h009: sprintf(cc1, "%d", c1)
    writeCstring(cc2, c2.toString()) // h009: sprintf(cc2, "%d", c2)

    // vendor: int f = Mglobal(RETRIEVE, (unsigned char *) ref, gtmp, svPtr)
    val refBuf = CharArray(STR_MAX)
    writeCstring(refBuf, ref)
    val f = mglobal(RETRIEVE, refBuf, gtmp, svPtr)

    if (f == 0) {
        MdhState.globalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }

    // vendor: _justify(tmp, gtmp, cc1, cc2)
    _justify(tmp, gtmp, cc1, cc2)

    val tmp1 = Mstring()
    tmp1.value = cstringAt(tmp) // vendor: tmp1=tmp
    return tmp1
}
