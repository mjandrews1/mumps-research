package mumps.mdh

import mumps.interp.cstringAt
import mumps.interp.mglobal
import mumps.interp.writeCstring

/**
 * PRO-arm translation of libmpscpp.cpp::global::Piece(global, int, int)
 * (pilot B2, unit 38, provisional under deviation C-003). Translated
 * independently from the pristine vendor source (mumps-14-may-2026.tgz,
 * sha256 621deb25...ffdbe), reference source lines 7204-7235
 * (units.csv: 23 LOC; the const char*/string/mstring overload
 * siblings are separate units, batches 7/8/10).
 *
 * The MUMPS dollar-Piece intrinsic over the subscripted global's
 * value, with the delimiter pattern itself retrieved from another
 * global: fetch pat's global, fetch this global, then _piece.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h007 char_buffer        : tmp[STR_MAX] result, cc1[65]/cc2[64]
 *    argument buffers, gtmp/gtmp1[STR_MAX] Mglobal out buffers.
 *  - h009 format_string     : both sprintf(..., "%d") calls collapse
 *    to the exact decimal strings of ii/jj (no formatting remainder).
 *  - h012 unsigned_byte_domain: buffers are Char(0..255) domains.
 *  - h014 out_of_scope_call  : Mglobal (InterpStubs stub, owner
 *    global-array machinery) and _piece (owner interp-piece.cpp:97,
 *    not a corpus unit; the interp-piece.cpp::<misc> batch-4 unit may
 *    claim its neighborhood) -> _piece is a new MdhStubs stub.
 *  - h017 naming             : Piece keeps the vendor name; emitted as
 *    a GlobalRef extension (unit-14 precedent, cf. units 32/33).
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The pattern global is fetched with pat.ref as the Mglobal key -
 *    the carrier's ref keeps the name (unit-14 conflation; real key
 *    building lands with the MakeGlobal owner, batch-7).
 *  - jj is reassigned when negative (jj = ii) before use - a local
 *    var here since Kotlin parameters are vals.
 *  - RETRIEVE = 0 (defines.h:83); GLOBAL_NOT_FOUND = 2
 *    (libmpscpp.h:71) - both as file-private constants.
 */

private const val STR_MAX = 32000 // vendor strmax.h:3 (file-private, cf. unit 22)
private const val RETRIEVE = 0 // vendor defines.h:83
private const val GLOBAL_NOT_FOUND = 2 // vendor libmpscpp.h:71

fun GlobalRef.Piece(pat: Global, ii: Int, jj: Int): Mstring {
    val tmp = CharArray(STR_MAX)
    val cc1 = CharArray(65)
    val cc2 = CharArray(64)
    val gtmp = CharArray(STR_MAX)
    val gtmp1 = CharArray(STR_MAX)
    var jjv = jj // vendor: if (jj<0) jj=ii;

    // vendor: Mglobal(RETRIEVE, (unsigned char *) pat.ref, gtmp1, svPtr)
    val patBuf = CharArray(STR_MAX)
    writeCstring(patBuf, pat.ref) // pat's ref buffer, see header note

    var f = mglobal(RETRIEVE, patBuf, gtmp1, MdhState.svPtr)

    if (f == 0) {
        MdhState.globalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }

    // vendor: Mglobal(RETRIEVE, (unsigned char *) ref, gtmp, svPtr)
    val refBuf = CharArray(STR_MAX)
    writeCstring(refBuf, g.ref) // ref buffer, unit-14 conflation note

    f = mglobal(RETRIEVE, refBuf, gtmp, MdhState.svPtr)

    if (f == 0) {
        MdhState.globalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }

    if (jjv < 0) jjv = ii

    writeCstring(cc1, ii.toString()) // h009: sprintf "%d" collapse
    writeCstring(cc2, jjv.toString()) // h009

    val lhs = CharArray(1) // vendor: (unsigned char *) ""
    _piece(tmp, gtmp, gtmp1, cc1, cc2, 0, lhs, MdhState.svPtr)

    val tmp1 = Mstring()
    tmp1.value = cstringAt(tmp) // vendor: tmp1 = tmp
    return tmp1
}
