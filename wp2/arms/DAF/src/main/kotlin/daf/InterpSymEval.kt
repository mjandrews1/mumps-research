package daf

/*
 * DAF batch-9 unit 264 (roster row 264; interp-sym.cpp::Eval,
 * core, LOC 15, score 11.2). Consult 2026-11-24T22:42:00Z.
 *
 * SEAT RELEASE: the evalDaf named stub (DafInterpStubsT3.kt) is
 * deleted with this release note; the real body lands at the same
 * name and (a, out, sv) signature, so the many call sites (Fcn.kt
 * indirection arms, LibmpscppMisc6/Misc7 Eval folds) resolve
 * unchanged.
 *
 * Vendor shape kept: the push-state body of the unit-215 service
 * render - i carries the caller LineNumber, AllocSV the fresh
 * state vector (allocSVDaf, the unit-244 release), the copy of
 * the old state vector over it (the vendor's own "inefficient"
 * memcpy - the copyStateDaf carrier, promoted from the private
 * siCopyState of the unit-215 render for this fold), then the
 * t2=1 / xd <- tmp0 / xpx=0 parse prologue, parse_ via the
 * parseDaf roster-b3 seat (the interp parse: the stub blame that
 * read "roster b9" is corrected at the DafInterpStubsT3 header;
 * evalDaf rides the interp seat, the b9 compiler-parse.c::parse_
 * twin of unit 277 is a different body with its own seat).
 * The out-copy strcpy(tmp2, &svPtr->pd1[sdlim]) is the offset
 * tail read (BASE-log row 81 precedent); free(svPtr) is the JVM
 * GC no-op ledger (row 22).
 * H-015 hygiene: no raw dollars ride the comments.
 * Dafny: EXEMPT (state-push parse relay over the parseDaf seat;
 * journal row).
 */

fun evalDaf(a: CharArray, out: CharArray, sv: DafMsv): Int {
    var i = sv.LineNumber
    val svPtr = allocSVDaf() // push statevector

    copyStateDaf(svPtr, sv) // memcpy(svPtr,svPtrX,sizeof(struct MSV))

    val q0 = svPtr.LineNumber // i = svPtrX->LineNumber; reset after copy
    svPtr.LineNumber = i
    i = q0 // (the vendor re-reads i before the copy; kept verbatim:
    // i is immediately overwritten below, the H-013 dead capture)

    svPtr.t2 = 1
    var q = 0
    while (q < a.size && a[q] != NUL_D) {
        svPtr.xd[q] = a[q]
        q++
    }
    svPtr.xd[q] = NUL_D
    svPtr.xpx = 0
    i = parseDaf(svPtr) // parse_(svPtr) seat

    // strcpy(tmp2, &svPtr->pd1[svPtr->sdlim]);  (offset tail read)
    val off = svPtr.sdlim
    var t = off
    var w = 0
    while (t < svPtr.pd1.size && svPtr.pd1[t] != NUL_D) {
        out[w] = svPtr.pd1[t]
        t++
        w++
    }
    out[w] = NUL_D

    // free (svPtr); - JVM GC no-op (row 22 fold)
    return i
}
