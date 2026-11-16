package mumps.mdh

import mumps.interp.cstringAt
import mumps.interp.mglobal
import mumps.interp.writeCstring

/**
 * PRO-arm translation of libmpscpp.cpp::global::Extract (pilot B2,
 * unit 32, first B2 unit). Translated independently from the pristine
 * vendor source (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe),
 * reference source lines 7546-7560 (units.csv: 16 LOC).
 *
 * The MUMPS dollar-Extract intrinsic over the subscripted global's
 * value: retrieve the global via Mglobal(RETRIEVE,...), throw
 * MumpsGlobalException when not found, then _extract the range.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h007 char_buffer        : a[40]/b[40] argument buffers, r[STR_MAX]
 *    result buffer, tmp[STR_MAX] Mglobal out buffer (C-string
 *    domains).
 *  - h010 numeric_promotion  : vendor Mltoa(long,...) receives the
 *    int st/ln via implicit widening - explicit .toLong() here.
 *  - h012 unsigned_byte_domain: buffers are Char(0..255) domains.
 *  - h014 out_of_scope_call  : Mglobal (InterpStubs stub, owner
 *    global-array machinery), Mltoa (owner interp-arith.cpp::Mltoa,
 *    batch-10 unit) and _extract (owner interp-sym.cpp::_extract,
 *    batch-10 unit) -> MdhStubs.kt.
 *  - h017 naming             : Extract keeps the vendor name; emitted
 *    as a GlobalRef extension (unit-14 precedent: the carrier stays
 *    minimal; the vendor's operator() result maps to GlobalRef).
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The Mglobal key is the vendor ref buffer (built by MakeGlobal,
 *    batch-7 owner). The carrier's ref keeps the name (unit-14
 *    conflation, journal); the key content upgrades when MakeGlobal
 *    lands.
 *  - RETRIEVE = 0 (defines.h:83); GLOBAL_NOT_FOUND = 2
 *    (libmpscpp.h:71) - both as file-private constants.
 */

private const val STR_MAX = 32000 // vendor strmax.h:3 (file-private, cf. unit 22)
private const val RETRIEVE = 0 // vendor defines.h:83
private const val GLOBAL_NOT_FOUND = 2 // vendor libmpscpp.h:71

fun GlobalRef.Extract(st: Int, ln: Int): Mstring {
    val a = CharArray(40)
    val b = CharArray(40)
    val r = CharArray(STR_MAX)
    val tmp = CharArray(STR_MAX)
    val rr = Mstring()

    // vendor: Mglobal(RETRIEVE, (unsigned char *) ref, tmp, svPtr)
    val key = CharArray(STR_MAX)
    writeCstring(key, g.ref) // ref buffer, see header note

    val f = mglobal(RETRIEVE, key, tmp, MdhState.svPtr)

    if (f == 0) {
        MdhState.globalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }

    Mltoa(st.toLong(), a) // h010: int -> long widening documented
    Mltoa(ln.toLong(), b)
    _extract(r, tmp, a, b)
    rr.value = cstringAt(r) // vendor: rr = r
    return rr
}
