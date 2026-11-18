package mumps.interp

/**
 * PRO-arm translation of interp-sym.cpp::Eval (roster row 261,
 * batch 9; units.csv: loc 15, comment_ratio 0.062,
 * pointer_marks 9, control_marks 0, score 21.1, rank 170).
 * Translated independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), reference
 * source lines 1192-1214 - RESOLVES the InterpStubs Eval stub
 * (TAG CORRECTION, u141/u148/u169/u172 family: the "not a
 * corpus unit" tag was a mis-tag, roster row 261 IS the owner,
 * correct batch-9 tag; superseded note kept in InterpStubs;
 * the Fcn.kt call sites bind unchanged).
 *
 * Eval(tmp0, tmp2, svPtrX): Int - evaluate one expression on a
 * PUSHED state vector: AllocSV() a fresh MSV, copy the caller's
 * whole struct in (u187 msvCopyInto full-field), set t2=1, load
 * xd from tmp0 at offset 0, parse_ it, and copy pd1 at sdlim
 * into tmp2; return parse_'s i.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h013 dead_store         : the vendor sets
 *    svPtr->LineNumber=i BEFORE the memcpy that overwrites it -
 *    a dead store, kept as a comment, not rendered.
 *  - h014 out_of_scope_call : AllocSV (u219 seat), msvCopyInto
 *    (u187 GlobalQuery internal full-field), parse_ (the
 *    interp-side real seat Parse_.kt:267), writeCstring/
 *    cstringAt (CstringOps seats) - all reused unmodified;
 *    free(svPtr) -> GC (fresh per-call allocation, vendor free
 *    is a malloc-family no-op analog, u44 precedent).
 *  - h015 grammar_collision : Eval keeps the vendor name;
 *    tmp0/tmp2/svPtrX names unchanged.
 *  - h017 naming            : Eval keeps the vendor spelling;
 *    the memcpy is the vendor's own "copy old state vector -
 *    inefficient" (kept, the comment rides in the vendor notes).
 */
fun Eval(tmp0: CharArray, tmp2: CharArray, svPtrX: Msv): Int {
    val i = svPtrX.LineNumber // h013: dead in the vendor (overwritten below)

    val svPtr = AllocSV() // vendor: svPtr=AllocSV(); push statevector
    // vendor: svPtr->LineNumber=i; - dead store, the memcpy below
    // overwrites LineNumber (kept as comment, h013)
    msvCopyInto(svPtr, svPtrX) // memcpy - the "inefficient" whole-struct copy (u187)
    svPtr.t2 = 1 // vendor: svPtr->t2=1;

    writeCstring(svPtr.xd, cstringAt(tmp0)) // strcpy(svPtr->xd, tmp0)
    svPtr.xpx = 0 // vendor: svPtr->xpx=0;

    val i2 = parse_(svPtr) // i=parse_(svPtr);

    writeCstring(tmp2, cstringAt(svPtr.pd1, svPtr.sdlim)) // strcpy(tmp2,&svPtr->pd1[svPtr->sdlim])
    // free(svPtr) -> GC (fresh per-call allocation, u44 divergence note)

    return i2 // vendor: return i;
}
