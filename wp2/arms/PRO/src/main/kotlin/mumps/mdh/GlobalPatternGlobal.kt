package mumps.mdh

import mumps.interp.mglobal
import mumps.interp.writeCstring

// ----------------------------------------------------------------------------
// (B11 FRONT-RUN, roster row 307; rendered at batch-10 close) libmpscpp.cpp::global::Pattern(global, vendor
// 7103-7125). Identity: units.csv row 307 -
// libmpscpp.cpp::global::Pattern, cpp, loc 15, comment_ratio 0.062,
// pointer_marks 0, control_marks 2, score 5.1, rank 12 - exact
// match; the Pattern x String/mstring/char-star twin seats live in
// LibmpscppMisc8.kt (batch-9 row 280), the mstring::Pattern(global)
// twin likewise - no redeclaration of those; THIS render is the
// global-needle Pattern arm missing from this family.
// ----------------------------------------------------------------------------

/**
 * int global::Pattern(global p): the pattern-match intrinsic over
 * two GLOBALS - retrieve THIS global's value, retrieve the
 * pattern global p's value, then pm(this, pattern). The family
 * twins fetch only this global and take the pattern from the
 * argument; here BOTH sides come off the database.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h007 char_buffer        : tmp/tmp1[STR_MAX] are the two
 *    Mglobal out buffers (the vendor declares tmp1 after the
 *    first gate - kept as the same mid-body declaration shape).
 *  - h012 unsigned_byte_domain: buffers are Char(0..255) domains.
 *  - h014 out_of_scope_call  : mglobal is the DONE interp owner
 *    seat (a RETRIEVE-only call, the row-303/304 twin shape); pm
 *    keeps its raised MdhStubs h014 stub (owner interp-pm.cpp:43,
 *    not a corpus unit, kept per the stubs ledger) - direct call.
 *  - h015 grammar_collision  : Global receiver, Global-key
 *    overload - NEW; no collision with the Global.Pattern(String/
 *    Mstring/CharArray) seats of LibmpscppMisc8.kt.
 *  - h017 naming             : Pattern keeps the vendor name.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - BOTH gates set GlobalExceptionCode=GLOBAL_NOT_FOUND then
 *    throw - the family shape; the second gate fires only when
 *    the pattern global is missing while this global was found.
 *  - RETRIEVE = 0 (defines.h:83); GLOBAL_NOT_FOUND = 2
 *    (libmpscpp.h:71) - both file-private here (u44 precedent).
 */
private const val STR_MAX = 32000 // vendor strmax.h:3 (file-private, u44 precedent)
private const val RETRIEVE = 0 // vendor defines.h:83
private const val GLOBAL_NOT_FOUND = 2 // vendor libmpscpp.h:71

fun Global.Pattern(p: Global): Int {
    val svPtr = MdhState.svPtr
    val tmp = CharArray(STR_MAX) // vendor: unsigned char tmp[STR_MAX]

    // vendor: Mglobal(RETRIEVE, (unsigned char *) ref, tmp, svPtr)
    val refBuf = CharArray(STR_MAX)
    writeCstring(refBuf, ref)
    var f = mglobal(RETRIEVE, refBuf, tmp, svPtr)

    if (f == 0) {
        MdhState.globalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }

    val tmp1 = CharArray(STR_MAX) // vendor: declared here, mid-body

    // vendor: Mglobal(RETRIEVE, (unsigned char *) p.ref, tmp1, svPtr)
    val pBuf = CharArray(STR_MAX)
    writeCstring(pBuf, p.ref)
    f = mglobal(RETRIEVE, pBuf, tmp1, svPtr)

    if (f == 0) {
        MdhState.globalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }

    return pm(tmp, tmp1, svPtr)
}
