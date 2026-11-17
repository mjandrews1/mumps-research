package mumps.mdh

import mumps.interp._find
import mumps.interp.atoiC
import mumps.interp.mglobal
import mumps.interp.writeCstring

/**
 * PRO-arm translation of libmpscpp.cpp::global::Find(mstring, int)
 * (pilot B2, unit 33, provisional under deviation C-003). Translated
 * independently from the pristine vendor source (mumps-14-may-2026.tgz,
 * sha256 621deb25...ffdbe), reference source lines 7617-7636
 * (units.csv: 16 LOC).
 *
 * The MUMPS dollar-Find intrinsic over the subscripted global's value:
 * retrieve the global via Mglobal(RETRIEVE,...), throw
 * MumpsGlobalException when not found, then _find the key's position.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h007 char_buffer        : a[40] argument buffer, r[20] result
 *    buffer, tmp[STR_MAX] Mglobal out buffer (C-string domains).
 *  - h010 numeric_promotion  : vendor Mltoa(long,...) receives the
 *    int st via implicit widening - explicit .toLong() here.
 *  - h012 unsigned_byte_domain: buffers are Char(0..255) domains.
 *  - h014 out_of_scope_call  : Mglobal (InterpStubs stub, owner
 *    global-array machinery) and Mltoa (owner interp-arith.cpp::Mltoa,
 *    batch-10 unit) -> existing MdhStubs.kt; _find and atoi resolve to
 *    the real B1 implementations (Find.kt, CstringOps.kt) - reused
 *    unmodified, no new stubs.
 *  - h017 naming             : Find keeps the vendor name; emitted as
 *    a GlobalRef extension (unit-14 precedent, cf. unit 32 Extract).
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The Mglobal key is the vendor ref buffer (built by MakeGlobal,
 *    batch-7 owner); the carrier's ref keeps the name (unit-14
 *    conflation, journal).
 *  - r[20] is the vendor's smaller result buffer (Extract used
 *    STR_MAX); kept at 20.
 *  - RETRIEVE = 0 (defines.h:83); GLOBAL_NOT_FOUND = 2
 *    (libmpscpp.h:71) - both as file-private constants.
 */

private const val STR_MAX = 32000 // vendor strmax.h:3 (file-private, cf. unit 22)
private const val RETRIEVE = 0 // vendor defines.h:83
private const val GLOBAL_NOT_FOUND = 2 // vendor libmpscpp.h:71

fun GlobalRef.Find(key: Mstring, st: Int): Int {
    val a = CharArray(40)
    val r = CharArray(20)
    val tmp = CharArray(STR_MAX)

    // vendor: Mglobal(RETRIEVE, (unsigned char *) ref, tmp, svPtr)
    val refBuf = CharArray(STR_MAX)
    writeCstring(refBuf, g.ref) // ref buffer, see header note

    val f = mglobal(RETRIEVE, refBuf, tmp, MdhState.svPtr)

    if (f == 0) {
        MdhState.globalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }

    // vendor: _find(r, tmp, (unsigned char *) key.c_str(), a)
    val keyBuf = CharArray(STR_MAX)
    writeCstring(keyBuf, key.c_str()) // mstring.c_str() NUL-terminated

    Mltoa(st.toLong(), a) // h010: int -> long widening documented
    _find(r, tmp, keyBuf, a)
    return atoiC(r) // vendor: rr = atoi((char *) r)
}

// ----------------------------------------------------------------------------
// (B6 unit 185) libmpscpp.cpp::global::Find(global key, int st) - the
// global-key overload (reference lines 7587-7615; the mstring-key overload
// above is pilot B2 unit 33). Identity verified against the study tool
// (tools/select_study_subset.py): global::Find, cpp, loc 22,
// comment_ratio 0.043, pointer_marks 0, control_marks 2, score 6.2 - exact
// match to wp1/study-subset/units.csv row 185 (batch 6); disambiguates from
// the mstring-key form (row 7617, loc 16, score 4.2, DONE as u33) by the
// second Mglobal retrieve.
// ----------------------------------------------------------------------------

/**
 * global::Find(global key, int st): the MUMPS dollar-Find intrinsic with
 * the key as a GLOBAL - retrieves BOTH the receiver's value and the key
 * global's value (each throwing MumpsGlobalException on absence), renders
 * st via Mltoa, then _find's the position and atoi's the result.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h007 char_buffer     : a[40]/r[20] and tmp/tmp1[STR_MAX] Mglobal out
 *    buffers (C-string domains).
 *  - h010 numeric_promotion: vendor Mltoa(long,...) receives the int st
 *    via implicit widening - explicit .toLong() (u33 precedent).
 *  - h012 unsigned_byte_domain: buffers are Char(0..255) domains.
 *  - h014 out_of_scope_call: mglobal/MdhState/MumpsGlobalException seats
 *    as in u33 above; _find/atoiC/Mltoa real or stub seats reused
 *    unmodified; key.ref rides the Global carrier's ref field.
 *  - h015 grammar_collision: overload folds into the Kotlin overload set
 *    by parameter type (Global vs Mstring key); pre-push scan clean.
 *  - h017 naming          : Find keeps the vendor name; GlobalRef
 *    extension receiver (u33 precedent, same file).
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - Unlike the u33 mstring-key form (the key string is the caller's
 *    mstring), here the KEY COMES FROM THE DATABASE - the second
 *    Mglobal retrieve is the whole difference, and a missing key
 *    global throws with the same GLOBAL_NOT_FOUND code as a missing
 *    receiver.
 */
fun GlobalRef.Find(key: Global, st: Int): Int {
    val a = CharArray(40)
    val r = CharArray(20)
    val tmp = CharArray(STR_MAX)
    val tmp1 = CharArray(STR_MAX)

    // vendor: Mglobal(RETRIEVE, (unsigned char *) ref, tmp, svPtr)
    val refBuf = CharArray(STR_MAX)
    writeCstring(refBuf, g.ref)
    var f = mglobal(RETRIEVE, refBuf, tmp, MdhState.svPtr)

    if (f == 0) {
        MdhState.globalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }

    // vendor: Mglobal(RETRIEVE, (unsigned char *) key.ref, tmp1, svPtr)
    val keyRefBuf = CharArray(STR_MAX)
    writeCstring(keyRefBuf, key.ref)
    f = mglobal(RETRIEVE, keyRefBuf, tmp1, MdhState.svPtr)

    if (f == 0) {
        MdhState.globalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }

    Mltoa(st.toLong(), a) // h010: int -> long widening documented
    _find(r, tmp, tmp1, a)
    return atoiC(r) // vendor: rr = atoi((char *) r)
}
