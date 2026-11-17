package mumps.interp

/**
 * PRO-arm translation of interp-sym.cpp::Qlength (B5 unit 133,
 * rank 216). Translated independently from the pristine vendor
 * source (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), reference
 * source lines 1100-1124.
 *
 * Identity verified against the study tool (tools/select_study_
 * subset.py): interp-sym.cpp::Qlength, cpp, loc 18,
 * comment_ratio 0.1, pointer_marks 13, control_marks 4, score 35.0
 * - exact match to wp1/study-subset/units.csv row 134.
 *
 * Qlength(tmp0, tmp2, svPtrX): the dollar-qlength intrinsic - push
 * a fresh state vector (copy of the caller's), parse the global
 * name from tmp0, keyfix the subscript image, count the subscript
 * markers in v1d, and land "0" or the marker count minus one in
 * tmp2.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h004 globals_ext       : the state-vector machinery rides
 *    the Msv carrier; allocSV (InterpStubs stub, owner state-
 *    vector machinery) pushes the fresh vector; the vendor's
 *    memcpy of the WHOLE struct renders as msvCopyInto (raised
 *    internal by this unit from GlobalQuery's private - the
 *    whole-struct copy convention, u98 precedent); free(svPtr) is
 *    GC.
 *  - h007 char_buffer       : tmp0/tmp2 are NUL-terminated byte
 *    buffers (h012 domain); the stub's offset carriers carry the
 *    vendor pointer offsets (u94 raised the seat with the
 *    svPtr.bd,1 call shape).
 *  - h012 unsigned_byte_domain: v1d subscript image bytes - the
 *    ==1 and >128 marker tests are byte-domain comparisons kept
 *    verbatim (marker chars above 128 per vendor).
 *  - h014 out_of_scope_call : this unit RESOLVES the InterpStubs
 *    Qlength stub raised by u94 (Scan unit 81 stub-removal
 *    precedent - the Fcn.kt call site binds unchanged); parse_ is
 *    DONE (unit 63) - direct call; keyfix rides the MdhStubs stub
 *    (owner keyfix.h inline, not a corpus unit) through the
 *    offset adapter (u98 precedent: the stub takes a whole
 *    buffer; the vendor's &v1d[1] pointer is carried around it);
 *    allocSV keeps its stub (not a corpus unit).
 *  - h015 grammar_collision : pre-push scan incl. cross-file
 *    top-level names: new top-level Qlength - the removed stub
 *    clears its seat (the mdh Mstring.Qlength member seat is a
 *    different vendor function, u131 ledger note); msvCopyInto
 *    internal - no collisions.
 *  - h017 naming          : Qlength keeps the vendor name.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The ErrorMessage guard for non-global arguments is COMMENTED
 *    OUT in the vendor (lines 1105-1106) - kept as a comment, not
 *    resurrected.
 *  - parse_'s return value is DISCARDED here (unlike GlobalQuery's
 *    error arm) - the i local is reused by the marker loop; parse
 *    errors do not surface in this function - kept verbatim.
 *  - j==0 lands "0"; any j>=1 lands j-1 (so j==1 also lands "0")
 *    - the vendor's twin shapes kept verbatim, not merged.
 *  - free(svPtr) at the exit is the C heap release - GC, with a
 *    comment (cf. the vendor's own "inefficient" copy note kept
 *    in GlobalQuery).
 */

/** vendor interp-sym.cpp:1100-1124 - void Qlength(unsigned char *
 *  tmp0, unsigned char * tmp2, struct MSV * svPtrX). The stub seat
 *  (u94) named the result buffer tmp1 with its offset carrier. */
fun Qlength(tmp0: CharArray, tmp0Off: Int, tmp1: CharArray, tmp1Off: Int, svPtr: Msv) {
    var i: Int
    var j = 0

    // vendor 1105-1106 (commented out in the vendor):
    // if (tmp0[0]!='^') ErrorMessage("dollar-qlength() must have
    //     global array name as argument", svPtr->LineNumber);

    i = svPtr.LineNumber.toInt()
    val qsvPtr = allocSV() // push statevector (h014 stub)
    qsvPtr.LineNumber = i.toLong()

    msvCopyInto(qsvPtr, svPtr) // memcpy: copy old state vector - inefficient

    qsvPtr.t0px = 1
    qsvPtr.t2 = 1
    writeCstring(qsvPtr.xd, cstringAt(tmp0, tmp0Off)) // strcpy(svPtr->xd, tmp0)
    qsvPtr.xpx = 0
    i = parse_(qsvPtr) // DONE - unit 63; return DISCARDED (vendor quirk)

    // keyfix(&svPtr->v1d[1]) - in-place fix via a local
    // copy/write-back adapter (the MdhStubs keyfix stub takes a
    // whole buffer; u98 precedent).
    val kf = CharArray(qsvPtr.v1d.size)
    var kn = 0
    var kk = 1
    while (qsvPtr.v1d[kk] != NUL) {
        kf[kn] = qsvPtr.v1d[kk]; kn++; kk++
    }
    kf[kn] = NUL
    mumps.mdh.keyfix(kf) // h014 stub (owner keyfix.h inline)
    kn = 0
    kk = 1
    while (kf[kn] != NUL) {
        qsvPtr.v1d[kk] = kf[kn]; kk++; kn++
    }
    qsvPtr.v1d[kk] = NUL

    // for (i=1; v1d[i]!=0; i++) if (v1d[i]==1 || v1d[i]>128) j++;
    i = 1
    while (qsvPtr.v1d[i] != NUL) {
        if (qsvPtr.v1d[i] == 1.toChar() || qsvPtr.v1d[i] > 128.toChar()) j++
        i++
    }

    if (j == 0) {
        writeCstring(tmp1, "0", tmp1Off) // strcpy(tmp2,"0")
    } else {
        // sprintf(tmp2,"%d",j-1)
        writeCstring(tmp1, (j - 1).toString(), tmp1Off)
    }

    // free (svPtr); - GC
}
