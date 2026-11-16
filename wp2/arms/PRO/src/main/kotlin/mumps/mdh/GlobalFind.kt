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
