package mumps.mdh

import mumps.interp.Msv
import mumps.interp.cstringAt
import mumps.interp.mglobal
import mumps.interp.writeCstring

/**
 * PRO-arm translation of libmpscpp.cpp::global::Concat(global)
 * (B3 unit 91, rank 31). Translated independently from the
 * pristine vendor source (mumps-14-may-2026.tgz, sha256
 * 621deb25...ffdbe), reference source lines 4049-4077.
 *
 * Identity verified against the study tool (tools/select_study_
 * subset.py): libmpscpp.cpp::global::Concat, cpp, loc 18,
 * comment_ratio 0.053, pointer_marks 0, control_marks 2, score
 * 5.4, batch 3 - exact match to wp1/study-subset/units.csv row
 * 31. Overload disambiguation: the global-argument member (loc
 * 18) - the double/long/int/char-ptr/string/mstring members (loc 13
 * and 12) and the mstring::Concat family are separate vendor
 * overloads not in this batch's corpus rows.
 *
 * global::Concat(global b): concatenate the values of two
 * globals - retrieve this global's value and b's value with
 * Mglobal(RETRIEVE,...) (throwing MumpsGlobalException when
 * either is absent), and return the string concatenation.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h007 char_buffer     : gtmp/gtmp1[STR_MAX] Mglobal out
 *    buffers (C-string domains).
 *  - h012 unsigned_byte_domain: buffers are Char(0..255) domains.
 *  - h014 out_of_scope_call: Mglobal (InterpStubs stub, owner
 *    global-array machinery; RETRIEVE = 0, defines.h:83) - reused
 *    unmodified; GlobalExceptionCode/throw MumpsGlobalException
 *    -> MdhState (unit 31); GlobalExceptionCode = GLOBAL_NOT_
 *    FOUND = 2 (libmpscpp.h:71); strcat is the plain string
 *    concatenation of the retrieved values.
 *  - h015 grammar_collision: pre-push scan incl. cross-file
 *    top-level names: new top-level Concat extension on Global -
 *    no collisions (Mstring.Concat owners are untranslated).
 *  - h017 naming          : Concat keeps the vendor name;
 *    emitted as an extension on the Global carrier (unit-14
 *    precedent, cf. Fnumber unit 84).
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The vendor retrieves THIS global's ref first, then b's ref;
 *    when the first retrieval succeeds but the second fails, the
 *    exception still carries GLOBAL_NOT_FOUND (both arms set the
 *    same code) - kept.
 *  - Unlike the char-pointer-typed Concat members (which declare
 *    an rslt mstring up front), this overload declares gtmp1
 *    only after the first success check - kept.
 *  - The ref member is the carrier's ref String; the Mglobal key
 *    buffers are rebuilt per call (unit-14 conflation, cf. unit
 *    44).
 */

private const val STR_MAX = 32000 // vendor strmax.h:3 (file-private, cf. unit 22)
private const val RETRIEVE = 0 // vendor defines.h:83
private const val GLOBAL_NOT_FOUND = 2 // vendor libmpscpp.h:71

fun Global.Concat(b: Global): Mstring {
    val svPtr: Msv = MdhState.svPtr
    val gtmp = CharArray(STR_MAX)
    val gtmp1 = CharArray(STR_MAX)

    // vendor: Mglobal(RETRIEVE, (unsigned char *) ref, gtmp, svPtr)
    val refBuf = CharArray(STR_MAX)
    writeCstring(refBuf, ref)
    var f = mglobal(RETRIEVE, refBuf, gtmp, svPtr)

    if (f == 0) {
        MdhState.globalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }

    // vendor: Mglobal(RETRIEVE, (unsigned char *) b.ref, gtmp1, svPtr)
    val bRefBuf = CharArray(STR_MAX)
    writeCstring(bRefBuf, b.ref)
    f = mglobal(RETRIEVE, bRefBuf, gtmp1, svPtr)

    if (f == 0) {
        MdhState.globalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }

    // vendor: strcat(gtmp,gtmp1); rslt=gtmp;
    return Mstring(cstringAt(gtmp) + cstringAt(gtmp1))
}
