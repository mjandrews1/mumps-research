package doc

// Unit 119 (B4 tranche 3): interp-sym.cpp::GlobalQuery (batch 4,
// complex stratum; LOC 91, score 89.3, rank 269). TAG CORRECTION
// on the Stubs.GlobalQuery seat ("not a roster unit" mis-tag):
// row 119 IS this batch-4 unit; the seat is removed with this
// note (the query family binds through the Qsub/Qlength seats;
// no in-arm direct call site exists today). The dollar-Query
// walker: pushes the state vector (AllocSV is the NEW H-014 stub
// seat - interp-sysfunc.cpp::AllocSV is the batch-8 roster row
// 219, correctly tagged), copies the old state vector over it
// (the vendor's own "inefficient" comment kept; the copy renders
// as the copyMsv field adapter - the memcpy is the H-004 one-
// owner observation of this file), fills the pushed xd with the
// query middle text, parses the global name through the real
// unit-93 parse_ render, keyfixes v1d at its offset-1 pointer,
// appends the SOH tail when the length probe misses, then walks
// Mglobal(XNEXT,...) against the v1d offset-1 key. The dollar
// byte of the error banner is built per H-015 (the file-private
// const). H-018: the #if defined(SQLITE) cut is LIVE per the
// reference build config - the early free/return fires, and
// the post-#endif tail (the strncmp of p1, the tmp3 tok==0
// parenthesization double render) is DEAD in the reference
// build: journaled here, NOT rendered. free is a JVM GC no-op
// (comment ledger). Vendor quirks kept and noted: the memcpy
// overwrites the just-assigned LineNumber with the source's
// identical value (no-op, kept for shape); the SOH-tail probe
// reads v1d[strlen-of-name] at ABSOLUTE index while the string
// starts at offset 1 - the vendor's off-by-one reads mid-name
// (usually not SOH, so the tail appends at ++i positions);
// the append writes BYTE-valued 1s (SOH), kept on the Char
// domain; the k reassignment from Mglobal; p1 folds to the
// String render per H-008 (dies before aliasing). printf maps
// to System.out (H-001); sigint/Mglobal/keyfix are the H-014
// seats (keyfix takes the offset slice copy - in-place write
// back is the stub owner's future business, the slice keeps
// the vendor's pointer INTO v1d without aliasing the carrier).
// Naming per H-017.

private const val DOLLAR_GQ = '\u0024'

fun GlobalQuery(
    tmp0: CharArray,
    tmp1: CharArray,
    tok: Char,
    svPtrX: Msv
) {
    var i: Int
    var oflg = 0

    i = svPtrX.LineNumber.toInt()
    val svPtr = Stubs.AllocSV() /* push statevector */
    svPtr.LineNumber = i.toLong()

    // memcpy(svPtr, svPtrX, sizeof(struct MSV)); /* copy old state
    // vector - inefficient (\u0027copy old state vector -
    // inefficient\u0027 vendor comment tail kept verbatim in
    // prose; ASCII only)
    copyMsv(svPtr, svPtrX)
    svPtr.LineNumber = i.toLong()

    svPtr.t0px = 1
    svPtr.t2 = 1

    CstringOps.strcpyS(svPtr.xd, CstringOps.cstring(tmp0))

    svPtr.xpx = 0
    i = parse_(svPtr)

    if (i != 0) {
        System.out.print("** " + DOLLAR_GQ + "Query error\n")
        Stubs.sigint(100)
    }

    // keyfix(&svPtr->v1d[1]) - the offset-1 slice
    val v1do = svPtr.v1d.copyOfRange(1, svPtr.v1d.size)
    Stubs.keyfix(v1do)

    // if (svPtr->v1d[i = strlen(&svPtr->v1d[1])] != 1) - the
    // absolute-index probe quirk, see header
    i = CstringOps.cstring(svPtr.v1d.copyOfRange(1, svPtr.v1d.size)).length
    if (svPtr.v1d[i] != CHAR_1) { // ^a case - no parens
        svPtr.v1d[++i] = CHAR_1
        svPtr.v1d[++i] = CHAR_1
        svPtr.v1d[++i] = NUL
    }

    val p1 = CstringOps.cstringAt(svPtr.v1d, 1)

    i = 1
    while (svPtr.v1d[i] != CHAR_1 && svPtr.v1d[i] != NUL) i++

    // Mglobal(XNEXT, &svPtr->v1d[1], tmp1, svPtr) - offset-1 key
    val key = svPtr.v1d.copyOfRange(1, svPtr.v1d.size)
    val k = Stubs.Mglobal(XNEXT, key, tmp1, svPtr)
    if (k == 0) {
        CstringOps.strcpyS(tmp1, "")
        // free (svPtr); free (p1);
        return
    }

    // #if defined(SQLITE) - LIVE per the reference build config:
    // free (svPtr); free (p1); return;
    // The post-#endif tail (strncmp(p1,tmp1,i) probe, the tmp3
    // parenthesization for tok==0, the tok-char fold) is DEAD in
    // the reference build - journaled in the header, not rendered.
    @Suppress("UNUSED_VARIABLE")
    oflg = oflg // vendor oflg lives in the dead tail (H-013 ledger)
}

// Msv field-copy adapter: the vendor memcpy of the whole state
// vector over the AllocSV copy. The bounded field set is the Msv
// carrier surface observed today; this file owns the adapter.
private fun copyMsv(dst: Msv, src: Msv) {
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
    CstringOps.strcpyC(dst.v1d, src.v1d)
    CstringOps.strcpyC(dst.bd, src.bd)
    CstringOps.strcpyC(dst.setname, src.setname)
    System.arraycopy(src.start, 0, dst.start, 0, src.start.size)
    System.arraycopy(src.hor, 0, dst.hor, 0, src.hor.size)
    System.arraycopy(src.ver, 0, dst.ver, 0, src.ver.size)
    System.arraycopy(src.in_file, 0, dst.in_file, 0, src.in_file.size)
    System.arraycopy(src.out_file, 0, dst.out_file, 0, src.out_file.size)
    dst._Sym = src._Sym
    dst.kflg = src.kflg
    dst.CurrentFile = src.CurrentFile
    dst.symlen = src.symlen
}
