package doc

// Unit 260 (B9 tranche 2): interp-sym.cpp::Eval (batch 9, core
// stratum; units.csv row 261: loc 15, ptr 4, ctl 0, score 4.5,
// rank 100; physLOC 15 physically verified this session). The
// XECUTE-family indirection evaluator: push a fresh state
// vector (AllocSV the landed unit-239 constructor), memcpy the
// caller's whole MSV over it (the vendor comment "copy old
// state vector - inefficient" kept; the copy rides the
// unit-119 copyMsv shape, duplicated per the vendor verbatim
// body repetition across the XECUTE/service family - dup
// ledger), set the t2 indirection reentry flag, swap in the
// tmp0 source text, reset xpx, and delegate to parse_ (the
// landed unit-93 parser, Stubs delegate). The result tail
// copies the pd1 slice from the sdlim lower bound into tmp2
// (strcpy(tmp2,&pd1[sdlim]), the offset-slice convention of
// the ServiceInterface XECUTE tail). H-013 vendor fossil: the
// svPtr->LineNumber=i store between AllocSV and the memcpy is
// a DEAD store - the memcpy overwrites it wholesale; kept in
// sequence as the copy helper is entered at once (the vendor
// evaluation order preserved as the note here). free(svPtr) is
// the GC no-op (H-008). The C int return is the parse_ return.
// Consult H-014 hit-adapt (whole-struct memcpy folds to the
// field-wise copy helper; free folds to GC). The Stubs.Eval
// seat flips to delegate this push. Naming per H-017 (Eval
// verbatim; the mstring::Eval unit-79 member is a class
// family, same-name H-015 overload rule).

fun Eval(tmp0: CharArray, tmp2: CharArray, svPtrX: Msv): Int {
    var i = svPtrX.LineNumber.toInt()
    val svPtr = AllocSV() /* push statevector */

    // svPtr.LineNumber = i - the vendor dead store (the memcpy
    // below overwrites it wholesale; H-013 fossil, kept as note)
    copyMsvE(svPtr, svPtrX)
    svPtr.t2 = 1
    CstringOps.strcpyC(svPtr.xd, tmp0)
    svPtr.xpx = 0
    i = Stubs.parse_(svPtr)
    // strcpy(tmp2, &svPtr->pd1[sdlim]) - the offset-slice tail
    CstringOps.copyAt(
        tmp2, 0,
        svPtr.pd1.copyOfRange(svPtr.sdlim, svPtr.pd1.size)
    )
    return i
}

// private whole-struct memcpy shape (unit-119 copyMsvSI twin;
// per-file private raise per the vendor verbatim body
// repetition across the XECUTE/service family)
private fun copyMsvE(dst: Msv, src: Msv) {
    dst.LineNumber = src.LineNumber
    dst.tpx = src.tpx
    dst.t0px = src.t0px
    dst.t2 = src.t2
    dst.io = src.io
    dst.ierr = src.ierr
    dst.NOERR = src.NOERR
    dst.symflg = src.symflg
    dst.order = src.order
    dst.sdlim = src.sdlim
    dst.pd1len = src.pd1len
    dst.ERROR = src.ERROR
    dst.xpx = src.xpx
    dst.ErrChar = src.ErrChar
    dst.gpad = src.gpad
    CstringOps.strcpyC(dst.xd, src.xd)
    CstringOps.strcpyC(dst.pd1, src.pd1)
    CstringOps.strcpyC(dst.bd, src.bd)
    CstringOps.strcpyC(dst.v1d, src.v1d)
    CstringOps.strcpyC(dst.setname, src.setname)
    CstringOps.strcpyC(dst.args, src.args)
}
