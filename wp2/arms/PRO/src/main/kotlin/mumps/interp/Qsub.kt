package mumps.interp

/**
 * PRO-arm translation of interp-sym.cpp::Qsub (B7 tranche 2, unit
 * 194, rank 240). Translated independently from the pristine
 * vendor source (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe),
 * reference source lines 1126-1180 (definition at line 1126).
 *
 * Identity verified against the study tool: interp-sym.cpp::Qsub,
 * cpp, loc 52, comment_ratio 0.019, pointer_marks 17,
 * control_marks 9, score 53.8 - exact match to
 * wp1/study-subset/units.csv row 194 (batch 7). RESOLVES the
 * InterpStubs.kt Qsub h014 stub (same signature; the Fcn.kt call
 * sites at :1215/:1341 bind unchanged).
 *
 * Qsub(tmp0, tmp1, tmp2, svPtrX): the dollar-qsubscript function -
 * re-parses tmp0's name on a pushed state vector, rewrites tmp0
 * with the \x01-subscript-encoded form, then extracts the j-th
 * subscript piece into tmp1 (j from tmp2; -1 empties tmp1; -2
 * copies the whole encoded name).
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h003 state_macro      : the AllocSV push + full-struct
 *    memcpy ride the allocSV/msvCopyInto seats (u187 extended the
 *    copy to the FULL field set; GlobalQuery precedent).
 *  - h007 char_buffer     : tmp0/tmp1/tmp2 are NUL-terminated
 *    byte buffers (h012 domain).
 *  - h012 unsigned_byte_domain: the \x01 (1) and > 128 subscript
 *    marker tests are byte-domain comparisons, kept verbatim.
 *  - h013 dead_store      : none (i/j/k/m/n all live; the vendor
 *    inits j/k/m at declaration - kept as Kotlin var inits).
 *  - h014 out_of_scope_call: allocSV (InterpStubs, state-vector
 *    machinery); parse_ (Parse_.kt:267 real seat, return value
 *    DISCARDED as the vendor discards it - Qlength precedent);
 *    keyfix (MdhStubs stub, owner keyfix.h inline) through the
 *    local copy/write-back adapter (u98/Qlength precedent); atoiC
 *    (CstringOps.kt:35).
 *  - h015 grammar_collision: new top-level Qsub re-binds the stub
 *    seat - no package collisions (the mdh Qsubscript family is
 *    receiver-scoped and differently named).
 *  - h017 naming          : Qsub keeps the vendor name.
 *  - h018 reference_build_visibility: no ifdefs in this body.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The pushed vector's t0px is 0 here (Qlength uses 1) and the
 *    caller's gpad is saved around the parse (parse side effects
 *    on gpad must not leak) - both kept verbatim.
 *  - gblflg writes the encoded name at &tmp0[1] (behind the caret)
 *    while the local arm writes at tmp0 - kept.
 *  - The piece walk NULs the separator IN tmp0 (the caller's
 *    buffer is destructively chopped) - kept: the walk mutates the
 *    buffer exactly as the C does.
 *  - The vendor's tmp2==NULL arm (j stays 0) is carried by the
 *    nullable tmp2 parameter (the stub-era call sites all pass
 *    buffers, so the arm is dormant there - it stays available for
 *    owner units that pass NULL, unlike the stub signature).
 *  - free(svPtr) at each exit - GC, comment only (u133 precedent).
 */

/** vendor interp-sym.cpp:1126-1180 - void Qsub(unsigned char *
 *  tmp0, unsigned char * tmp1, unsigned char * tmp2, struct MSV *
 *  svPtrX); the stub-era offset carriers keep the Fcn.kt call
 *  shapes. */
fun Qsub(tmp0: CharArray, tmp0Off: Int, tmp1: CharArray, tmp1Off: Int,
         tmp2: CharArray?, tmp2Off: Int, svPtrX: Msv) {
    var i: Int
    var j = 0
    var k = 0
    var m = 0
    var n: Int
    var gblflg = 0

    if (tmp0[tmp0Off] == '^') gblflg = 1

    i = svPtrX.LineNumber.toInt()
    val qsvPtr = allocSV() // push statevector (h014 stub)
    qsvPtr.LineNumber = i.toLong()

    msvCopyInto(qsvPtr, svPtrX) // memcpy: copy old state vector - inefficient

    qsvPtr.t0px = 0
    qsvPtr.t2 = 1
    writeCstring(qsvPtr.xd, cstringAt(tmp0, tmp0Off)) // strcpy(svPtr->xd, tmp0)
    qsvPtr.xpx = 0
    j = qsvPtr.gpad
    qsvPtr.gpad = 0
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

    qsvPtr.gpad = j // restore the caller's gpad

    if (gblflg != 0) {
        writeCstring(tmp0, cstringAt(qsvPtr.v1d, 1), tmp0Off + 1)
    } else {
        writeCstring(tmp0, cstringAt(qsvPtr.v1d, 1), tmp0Off)
    }

    if (tmp2 != null) j = atoiC(tmp2, tmp2Off)

    if (j == -1) {
        writeCstring(tmp1, "", tmp1Off)
        return // free(svPtr) - GC
    }

    if (j == -2) {
        writeCstring(tmp1, cstringAt(tmp0, tmp0Off), tmp1Off)
        return // free(svPtr) - GC
    }

    n = strlenC(tmp0, tmp0Off)
    i = 0
    for (kk2 in 0..j) {
        m = i
        if (m >= n) {
            writeCstring(tmp1, "", tmp1Off)
            return // free(svPtr) - GC
        }
        while (tmp0[tmp0Off + i] != NUL) {
            if (tmp0[tmp0Off + i] == 1.toChar() || tmp0[tmp0Off + i] > 128.toChar()) {
                tmp0[tmp0Off + i] = NUL
                break
            }
            i++
        }
        i++
    }
    writeCstring(tmp1, cstringAt(tmp0, tmp0Off + m), tmp1Off)

    // free(svPtr) - GC
    return
}
