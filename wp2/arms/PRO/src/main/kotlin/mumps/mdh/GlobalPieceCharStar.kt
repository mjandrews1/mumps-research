package mumps.mdh

import mumps.interp.cstringAt
import mumps.interp.mglobal
import mumps.interp.writeCstring

/**
 * PRO-arm translation of libmpscpp.cpp::global::Piece(const
 * char *pat, int ii, int jj) (batch-8 unit 239, units.csv data
 * line 239, rank 48).
 * Translated independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), reference
 * source lines 7181-7203.
 *
 * Identity verified against the study tool (tools/select_study_
 * subset.py): libmpscpp.cpp::global::Piece, cpp, loc 17,
 * comment_ratio 0.056, pointer_marks 1, control_marks 2,
 * score 7.3, rank 48, batch 8 - exact match to
 * wp1/study-subset/units.csv line 239.  Overload
 * disambiguation: the const char* member (loc 17, ptr 1 - the
 * FIRST of the four global::Piece overloads); the global-arg
 * member is B2 unit 38 (GlobalPiece.kt, GlobalRef receiver)
 * and the string sibling is its later-batch fold (the mstring
 * sibling is batch-10 residue).
 *
 * The MUMPS dollar-Piece intrinsic over the global's value:
 * retrieve this global, then _piece with the literal pattern
 * and the ii/jj bounds (a negative jj folds to ii).
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h007 char_buffer : tmp[STR_MAX] result, cc1[65]/cc2[64]
 *    argument buffers, gtmp[STR_MAX] Mglobal out buffer.
 *  - h009 format_string: sprintf "%d" collapses to the exact
 *    decimal strings of ii/jj.
 *  - h012 unsigned_byte_domain: buffers are Char(0..255)
 *    domains.
 *  - h014 out_of_scope_call: Mglobal (InterpStubs stub,
 *    owner global-array machinery) and _piece (InterpPiece
 *    seat, the MdhStubs entry folds there) reused unmodified.
 *  - h015 grammar_collision: new top-level Piece extension on
 *    the Global carrier - no collisions (the u38/u-sibling
 *    seats are GlobalRef-receiver-distinct; Mstring.Piece is
 *    receiver-distinct).
 *  - h017 naming      : Piece keeps the vendor name; emitted
 *    on the Global carrier (the vendor receiver of THIS
 *    overload).
 *  - h018 preprocessor_conditional: none in this body.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The pattern is used LITERALLY (char* pat, no second
 *    retrieval) - unlike the global-arg overload u38.
 *  - jj is reassigned when negative (jj = ii) before use - a
 *    local var here since Kotlin parameters are vals.
 *  - The 6th _piece argument is 0 and the 7th the empty
 *    string (kept verbatim).
 */

private const val STR_MAX = 32000 // vendor strmax.h:3 (file-private, cf. unit 22)
private const val RETRIEVE = 0 // vendor defines.h:83
private const val GLOBAL_NOT_FOUND = 2 // vendor libmpscpp.h:71

fun Global.Piece(pat: String, ii: Int, jj: Int): Mstring {
    val tmp = CharArray(STR_MAX)
    val cc1 = CharArray(65)
    val cc2 = CharArray(64)
    val gtmp = CharArray(STR_MAX)
    var jjv = jj // vendor: if (jj<0) jj=ii;

    // vendor: Mglobal(RETRIEVE, (unsigned char *) ref, gtmp, svPtr)
    val refBuf = CharArray(STR_MAX)
    writeCstring(refBuf, ref)

    val f = mglobal(RETRIEVE, refBuf, gtmp, MdhState.svPtr)
    if (f == 0) {
        MdhState.globalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }

    if (jjv < 0) jjv = ii

    writeCstring(cc1, ii.toString()) // h009: sprintf(cc1,"%d",ii)
    writeCstring(cc2, jjv.toString()) // h009: sprintf(cc2,"%d",jj)

    val patBuf = CharArray(STR_MAX)
    writeCstring(patBuf, pat) // vendor: pat (const char *)
    val lhs = CharArray(1) // vendor: (unsigned char *) ""
    _piece(tmp, gtmp, patBuf, cc1, cc2, 0, lhs, MdhState.svPtr)

    return Mstring(cstringAt(tmp)) // vendor: tmp1 = tmp; return tmp1
}
