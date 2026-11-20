package doc

// Unit 145 (B5 tranche 3): interp-sym.cpp::Qlength (batch 5, complex
// stratum; units.csv row 145: loc 18, score 35.0, rank 216).The
// dollar-qlength computation: pushes the state vector (AllocSV the
// batch-8 H-014 seat, correctly tagged), copies the caller's state
// vector over it (the vendor's own "inefficient" comment; the
// copyMsv field adapter is the unit-119 shape DUPLICATED here
// per the vendor's verbatim repetition - the duplicate-def ledger
// of the u141 strcatx precedent applies, H-013 family), parses the
// global name through the real unit-93 parse_ render, keyfixes the
// v1d at its offset-1 pointer (the offset slice copy idiom of the
// unit-119 render: in-place write-back is the stub owner's
// business, the slice keeps the vendor pointer INTO v1d without
// aliasing the carrier), counts the subscripts whose delimiter
// byte is the SOH 1 or above the byte-128 bound, and writes the
// count-minus-one decimal (or "0" when none) into tmp2. The
// leading commented ErrorMessage guard is a vendor comment kept
// verbatim (H-013 comment kept, not revived). RETIRE NOTE: this
// render RESOLVES the unit-119-raised Stubs.Qlength seat - the
// seat flips to the delegate (H-014 delegate precedent,
// Stubs.SymFree). sprintf percent-d renders via String.format
// Locale.US (H-009). H-010: the LineNumber Long narrows to the
// parse index copy (i is int in vendor); tpx is not read here.
// free(svPtr) is a JVM GC no-op (ledger). Naming per H-017.

fun Qlength(tmp0: CharArray, tmp2: CharArray, svPtrX: Msv) {
    var j = 0
    var i: Int

    // if (tmp0[0]!='^') ErrorMessage("...must have global array name
    // as argument"); - vendor comment kept verbatim (H-013).

    i = svPtrX.LineNumber.toInt()
    val svPtr = Stubs.AllocSV() /* push statevector */
    svPtr.LineNumber = i.toLong()

    // memcpy(svPtr,svPtrX,sizeof(struct MSV)); /* copy old state
    // vector - inefficient */ - unit-119 copyMsv shape, duplicated
    // per the vendor's verbatim body repetition (dup ledger).
    copyMsvQ(svPtr, svPtrX)
    svPtr.LineNumber = i.toLong()

    svPtr.t0px = 1
    svPtr.t2 = 1

    CstringOps.strcpyS(svPtr.xd, CstringOps.cstring(tmp0))

    svPtr.xpx = 0
    i = Stubs.parse_(svPtr)

    // keyfix(&svPtr->v1d[1]) - the offset-1 slice (unit-119 idiom).
    val v1do = svPtr.v1d.copyOfRange(1, svPtr.v1d.size)
    Stubs.keyfix(v1do)

    i = 1
    while (svPtr.v1d[i] != NUL) {
        if (svPtr.v1d[i] == CHAR_1 || svPtr.v1d[i] > '\u0080') j++
        i++
    }

    if (j == 0) {
        CstringOps.strcpyS(tmp2, "0")
    } else {
        CstringOps.strcpyS(tmp2, String.format(
            java.util.Locale.US, "%d", j - 1))
    }

    // free (svPtr); - JVM GC no-op (ledger).
}

// Msv field-copy adapter: the vendor memcpy of the whole state
// vector over the AllocSV copy (the unit-119 private adapter
// duplicated for this file's owner scope per the vendor's
// verbatim body repetition; bounded to the Msv carrier surface).
private fun copyMsvQ(dst: Msv, src: Msv) {
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
    CstringOps.strcpyC(dst.xd, src.xd)
    CstringOps.strcpyC(dst.pd1, src.pd1)
    CstringOps.strcpyC(dst.bd, src.bd)
    CstringOps.strcpyC(dst.v1d, src.v1d)
    CstringOps.strcpyC(dst.setname, src.setname)
    CstringOps.strcpyC(dst.args, src.args)
}
