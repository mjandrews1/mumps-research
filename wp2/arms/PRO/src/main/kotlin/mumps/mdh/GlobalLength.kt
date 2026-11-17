package mumps.mdh

import mumps.interp.Msv
import mumps.interp.cstringAt
import mumps.interp.mglobal
import mumps.interp.writeCstring

/**
 * PRO-arm translation of libmpscpp.cpp::global::Length(global
 * needle) (B5 tranche C, unit 145, rank 95). Translated
 * independently from the pristine vendor source (mumps-14-may-
 * 2026.tgz, sha256 621deb25...ffdbe), reference source lines
 * 3274-3304.
 *
 * Identity verified against the study tool
 * (tools/select_study_subset.py): global::Length, cpp, loc 23,
 * comment_ratio 0.042, pointer_marks 0, control_marks 5,
 * score 9.9 - exact match to wp1/study-subset/units.csv row 146
 * (batch 5). Overload disambiguation journaled: this is the
 * global-needle form (:3274); the no-arg form (:3261) and the
 * mstring/string/char-star needle forms (:3306/:3330/:3354) are
 * separate vendor overloads not in this batch's corpus rows
 * (the mstring::Length family :3257/:3378... is roster unit
 * 149, a later tranche-C row).
 *
 * global::Length(global needle): piece count of this global's
 * value under the needle global's value - retrieves both
 * values with Mglobal(RETRIEVE,...) (throwing on either
 * absence), then counts the needle occurrences the vendor's
 * strstr walk finds, starting from 1 and returning 0 when the
 * haystack is empty (the vendor's NOT FOUND convention here is
 * a plain 0 return, not the exception).
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h007 char_buffer     : gtmp/gtmp1[STR_MAX] Mglobal out
 *    buffers (C-string domains).
 *  - h012 unsigned_byte_domain: buffers are Char(0..255)
 *    domains; the strstr/strlen walk is over the C byte
 *    buffers via per-file private helpers (strstrC has no
 *    shared seat - new per-file private, MpsAcos regex-helper
 *    precedent).
 *  - h014 out_of_scope_call: Mglobal (InterpStubs stub, owner
 *    global-array machinery; RETRIEVE = 0, defines.h:83) -
 *    reused unmodified; GlobalExceptionCode/throw
 *    MumpsGlobalException -> MdhState (unit 31);
 *    GLOBAL_NOT_FOUND = 2 (libmpscpp.h:71).
 *  - h015 grammar_collision: pre-push scan incl. cross-file
 *    top-level names: new top-level Length extension on the
 *    Global carrier - the Kotlin String/Mstring length seats
 *    are library properties (distinct), no collisions.
 *  - h017 naming          : Length keeps the vendor name;
 *    emitted as an extension on the Global carrier (GlobalConcat
 *    unit-53 precedent).
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The i counter starts at 1 BEFORE the walk: an empty
 *    haystack returns 0 (early return), but a non-empty haystack
 *    with zero needle hits returns 1 - "one piece" (vendor
 *    comment) - kept verbatim.
 *  - The walk advances by the needle LENGTH (p1+strlen(gtmp1)),
 *    NOT past the match - overlapping occurrences are counted
 *    (strstr semantics from the match start would re-find
 *    within the needle) - kept verbatim.
 */
private const val STR_MAX = 32000 // vendor strmax.h:3 (file-private, cf. unit 22)
private const val RETRIEVE = 0 // vendor defines.h:83
private const val GLOBAL_NOT_FOUND = 2 // vendor libmpscpp.h:71

fun Global.Length(needle: Global): Int {
    val svPtr: Msv = MdhState.svPtr
    val gtmp = CharArray(STR_MAX)
    val gtmp1 = CharArray(STR_MAX)

    // vendor: Mglobal(RETRIEVE,(unsigned char *)ref,gtmp,svPtr)
    val refBuf = CharArray(STR_MAX)
    writeCstring(refBuf, ref)
    var f = mglobal(RETRIEVE, refBuf, gtmp, svPtr)

    if (f == 0) {
        MdhState.globalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }

    // vendor: Mglobal(RETRIEVE,(unsigned char *)needle.ref,...)
    val needleRefBuf = CharArray(STR_MAX)
    writeCstring(needleRefBuf, needle.ref)
    f = mglobal(RETRIEVE, needleRefBuf, gtmp1, svPtr)

    if (f == 0) {
        MdhState.globalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }

    // vendor: p1=(char *)gtmp; if (strlen(p1)==0) return 0;
    val hay = cstringAt(gtmp)
    if (hay.isEmpty()) return 0

    // vendor: while(1){ if ((p1=strstr(p1,gtmp1))==NULL) return i;
    //                 i++; p1=p1+strlen(gtmp1); }
    val ndl = cstringAt(gtmp1)
    var i = 1
    var p = 0
    while (true) {
        val hit = hay.indexOf(ndl, p)
        if (hit == -1) return i // one piece
        i++
        p = hit + ndl.length
    }
}
