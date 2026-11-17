package mumps.mdh

import mumps.interp.Msv
import mumps.interp.cstringAt
import mumps.interp.mglobal
import mumps.interp.writeCstring

/**
 * PRO-arm translation of libmpscpp.cpp::mstring::Length(global
 * needle) (B5 tranche C, unit 149, rank 59). Translated
 * independently from the pristine vendor source (mumps-14-may-
 * 2026.tgz, sha256 621deb25...ffdbe), reference source lines
 * 3406-3427.
 *
 * Identity verified against the study tool
 * (tools/select_study_subset.py): mstring::Length, cpp, loc 16,
 * comment_ratio 0.059, pointer_marks 0, control_marks 4,
 * score 7.8 - exact match to wp1/study-subset/units.csv row 150
 * (batch 5). Overload disambiguation journaled: this is the
 * global-needle form (:3406); the no-arg (:3257) and
 * char-star/string/mstring-needle forms (:3378/:3392/:3429) are
 * separate vendor overloads not in this batch's corpus rows
 * (the sibling global::Length(global) form :3274 was roster
 * unit 145, rendered earlier this tranche).
 *
 * mstring::Length(global needle): piece count of THIS mstring's
 * value under the needle global's value - retrieves the needle
 * with Mglobal(RETRIEVE,...) (throwing on absence), then counts
 * needle occurrences with the vendor's strstr walk over the
 * mstring's own data (the haystack is the receiver, unlike
 * unit 145's double-retrieve).
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h004 globals_ext       : svPtr rides the MdhState carrier
 *    (u31; MdhState.svPtr is the single state vector,
 *    Count/Cosine/GlobalLength convention).
 *  - h007 char_buffer     : gtmp[STR_MAX] Mglobal out buffer
 *    (C-string domain); the haystack p1 is the receiver's
 *    value (String carrier - the vendor's data.c_str()).
 *  - h012 unsigned_byte_domain: the strstr/strlen walk is over
 *    the C byte domain via the cstringAt fold (u145 GlobalLength
 *    precedent); Mglobal retrieves into gtmp.
 *  - h014 out_of_scope_call: Mglobal (InterpStubs stub, owner
 *    global-array machinery; RETRIEVE = 0, defines.h:83) -
 *    reused unmodified; GlobalExceptionCode/throw
 *    MumpsGlobalException -> MdhState (unit 31);
 *    GLOBAL_NOT_FOUND = 2 (libmpscpp.h:71).
 *  - h015 grammar_collision: pre-push scan incl. cross-file
 *    top-level names: new top-level Length extension on the
 *    Mstring carrier - the Global.Length extension (u145) and
 *    the Kotlin String.length property are distinct names; no
 *    collisions.
 *  - h017 naming          : Length keeps the vendor name;
 *    emitted as an extension on the Mstring carrier (one-owner
 *    member-addition policy, Mstring.kt header precedent; new
 *    file per the tranche file-per-unit convention).
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The i counter starts at 1 BEFORE the walk: an empty
 *    haystack returns 0 (early return), but a non-empty haystack
 *    with zero needle hits returns 1 - "one piece" (vendor
 *    comment) - kept verbatim (u145 family).
 *  - The walk advances by the needle LENGTH, NOT past the match
 *    - overlapping occurrences are counted - kept verbatim.
 */
private const val STR_MAX = 32000 // vendor strmax.h:3 (file-private, cf. unit 22)
private const val RETRIEVE = 0 // vendor defines.h:83
private const val GLOBAL_NOT_FOUND = 2 // vendor libmpscpp.h:71

fun Mstring.Length(needle: Global): Int {
    val svPtr: Msv = MdhState.svPtr
    val gtmp = CharArray(STR_MAX)

    // vendor: Mglobal(RETRIEVE,(unsigned char *)needle.ref,gtmp,svPtr)
    val needleRefBuf = CharArray(STR_MAX)
    writeCstring(needleRefBuf, needle.ref)
    val f = mglobal(RETRIEVE, needleRefBuf, gtmp, svPtr)

    if (f == 0) {
        MdhState.globalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }

    // vendor: p1=(char *)data.c_str(); if (strlen(p1)==0) return 0;
    val hay = value
    if (hay.isEmpty()) return 0

    // vendor: while(1){ if ((p1=strstr(p1,gtmp))==NULL) return i;
    //                 i++; p1=p1+strlen(gtmp); }
    val ndl = cstringAt(gtmp)
    var i = 1
    var p = 0
    while (true) {
        val hit = hay.indexOf(ndl, p)
        if (hit == -1) return i // one piece
        i++
        p = hit + ndl.length
    }
}
