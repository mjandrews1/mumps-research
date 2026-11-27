package both

// Unit 264 (BOTH B9, core): interp-sym.cpp::Eval (15 LOC).
// Batch_plan ordinal 264; units.csv row 260. The expression
// evaluator entry: push a fresh state vector, copy the caller's
// entire state into it, set the t2 eval flag and zero the
// expression cursor, parse the expression from xd, and hand back
// the compiled pd1 slice from sdlim as the value string. The
// unit-261 file-tail ledger seats this declaration
// (interp-service.cpp::<misc> named it).
//
// BOTH consults (find_rule before render, events logged):
//   h007 char_buffer - reused: tmp0/tmp2 stay CharArrays in the
//     C byte domain; the xd copy-in and the pd1-slice copy-out
//     are exact NUL-terminated byte copies.
//   h020 pointer_handle - reused: the vendor push/pop is
//     malloc/free over the state-vector struct - the JVM seat
//     allocates the fresh Msv via allocSV() (unit 218) and the
//     free is a no-op (GC); the state copy rides the
//     struct-copy seat below (h021 carrier family).
//   h021 struct_carrier - reused: memcpy(svPtr, svPtrX,
//     sizeof(struct MSV)) renders as the member-wise copy seat
//     (fixed buffers copy contents; handle/reference members
//     copy references - the C shallow byte copy of pointer
//     members has exactly reference-sharing semantics).
//   h014 out_of_scope_call - reused: parse_ rides the
//     BothStubs TODO seat (owner interp-parse.cpp::parse_,
//     units.csv row 63, batch 3 - a FUTURE BOTH batch in this
//     arm's committed order, the stub stays until then; h014).
//   h017 naming - reused: Eval keeps the vendor name (distinct
//     from libmpscpp.cpp::mstring::Eval, the C-014 render
//     below); tmp0/tmp2/svPtrX/i keep vendor names.
//   Dafny: exempt (state-vector copy + interpreter dispatch).
//
// Vendor quirks kept (ledgered):
//   - svPtr->LineNumber = i is executed BEFORE the memcpy that
//     overwrites it with svPtrX->LineNumber anyway (the vendor
//     comment says "inefficient"); both writes kept verbatim.
//   - strcpy(tmp2, &pd1[sdlim]) copies until the first NUL -
//     the pd1 residue past the NUL is not copied (kept).
//   - free(svPtr) has no JVM seat (GC); noted, not rendered.

internal fun Eval(tmp0: CharArray, tmp2: CharArray, svPtrX: Msv): Int {
    // i = svPtrX->LineNumber; svPtr = AllocSV();
    //   (C truncates the long LineNumber into int i; kept)
    var i = svPtrX.LineNumber.toInt()
    val svPtr = allocSV()

    // svPtr->LineNumber = i;   (overwritten by the memcpy - kept)
    svPtr.LineNumber = i.toLong()

    // memcpy(svPtr, svPtrX, sizeof(struct MSV));  (h021 seat)
    msvMemcpyZb(svPtr, svPtrX)

    // svPtr->t2 = 1; strcpy((char *) svPtr->xd, (const char *) tmp0);
    svPtr.t2 = 1
    val src = cStringOf(tmp0)
    for (k in src.indices) svPtr.xd[k] = src[k]
    svPtr.xd[src.length] = 0.toChar()

    // svPtr->xpx = 0;
    svPtr.xpx = 0

    // i = parse_(svPtr);   (h014 seat; owner row 63, future batch)
    i = parse_(svPtr)

    // strcpy((char *) tmp2, (const char *) &svPtr->pd1[svPtr->sdlim]);
    var k = svPtr.sdlim
    var d = 0
    while (svPtr.pd1[k] != 0.toChar()) {
        tmp2[d] = svPtr.pd1[k]
        k++
        d++
    }
    tmp2[d] = 0.toChar()

    // free(svPtr);  (GC fold)
    return i
}

// memcpy(struct MSV *, const struct MSV *, sizeof(struct MSV)) -
// the state-vector push copy (h021 carrier family; seated real
// at the unit-264 render, the only corpus consumer). Fixed
// buffers copy CONTENTS byte-for-byte; the pointer members
// (start, nstart, in_file/out_file handles) copy REFERENCES -
// the C shallow struct copy shares exactly those.
private fun msvMemcpyZb(dst: Msv, src: Msv) {
    fun cp(d: CharArray, s: CharArray) = System.arraycopy(s, 0, d, 0, s.size)
    cp(dst.bd, src.bd); cp(dst.v1d, src.v1d); cp(dst.xd, src.xd)
    cp(dst.program, src.program); cp(dst.cfgdata, src.cfgdata)
    cp(dst.cfgkey, src.cfgkey); cp(dst.pd1, src.pd1)
    cp(dst.setname, src.setname)
    System.arraycopy(src.start, 0, dst.start, 0, src.start.size)
    dst.tpx = src.tpx; dst._Sym = src._Sym; dst.LineNumber = src.LineNumber
    dst.t0px = src.t0px; dst.xpx = src.xpx; dst.t2 = src.t2
    dst.LockDone = src.LockDone; dst.kflg = src.kflg; dst.io = src.io
    dst.ierr = src.ierr; dst.sdlim = src.sdlim; dst.symflg = src.symflg
    dst.ITRAP = src.ITRAP; dst.setpiece = src.setpiece
    dst.ctrlc = src.ctrlc; dst.iorg = src.iorg; dst.symlen = src.symlen
    dst.NOERR = src.NOERR
    System.arraycopy(src.in_file, 0, dst.in_file, 0, 10)
    System.arraycopy(src.out_file, 0, dst.out_file, 0, 10)
    System.arraycopy(src.hor, 0, dst.hor, 0, 10)
    System.arraycopy(src.ver, 0, dst.ver, 0, 10)
    dst.pd1len = src.pd1len; dst.nflg = src.nflg; dst.ERROR = src.ERROR
    dst.gpad = src.gpad; dst.nstart = src.nstart
    for (k in 0 until 10) {
        cp(dst.RefParm[k], src.RefParm[k]); cp(dst.Rargs[k], src.Rargs[k])
    }
    dst.Rargsx = src.Rargsx; dst._SymPush = src._SymPush
    dst.order = src.order; dst.hit = src.hit; dst.count = src.count
    dst.restoredata = src.restoredata
    dst.ScanMinWordSize = src.ScanMinWordSize
    dst.ScanMaxWordSize = src.ScanMaxWordSize
    dst.PD1 = src.PD1; cp(dst.UKEY, src.UKEY); cp(dst.UDAT, src.UDAT)
    cp(dst.args, src.args); cp(dst.Table, src.Table); dst.TabSize = src.TabSize
    cp(dst.pgsql_msg, src.pgsql_msg); cp(dst.Connection, src.Connection)
    cp(dst.Col_Names, src.Col_Names); System.arraycopy(src.fdes, 0, dst.fdes, 0, 2)
    dst.exec = src.exec; cp(dst.reference, src.reference)
    dst.ErrChar = src.ErrChar; cp(dst.CurrentFile, src.CurrentFile)
    cp(dst.sqloutput, src.sqloutput)
}
