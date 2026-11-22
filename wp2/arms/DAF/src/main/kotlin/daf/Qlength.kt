package daf

// Unit 145 (DAF B5 T3, complex): interp-sym.cpp::Qlength (units.csv
// row 134: loc 18, cr 0.1, ptr 13, ctl 4, score 35.0, rank 216,
// complex): the \\$qlength(x) service: the parse of the global array
// reference name, then the v1d walk counting the coded subscript
// separators, with the j-1 rule (j==0 leader -> "0"; j separators ->
// j-1 subscripts).
// Consults: BASE-log rows 6/22 hit-adapt (the state-vector push:
// AllocSV() allocates and the vendor memcpy(svPtr,svPtrX,sizeof)
// clone; the render clones the DafMsv carrier field-wise - DOCUMENTED
// DIVERGENCE: byte-copy vs field clone, semantics identical for the
// carrier since the copied unit is the state value; the vendor's
// quirk that svPtr->LineNumber=i is assigned BEFORE the memcpy that
// immediately overwrites it is kept in substance: the clone carries
// svPtrX.LineNumber either way, and the pre-assign is a no-op ledger
// note), row 8 hit-reuse (unsigned char[] buffers -> CharArray
// carriers; tmp0/tmp2 keep the NUL-terminated buffer roles), row 90
// family hit-reuse (the parse_/keyfix stub seats of the interpreter
// TU ride the named-owner discipline: parse_ b9 stub parseDaf;
// keyfix stays the permanent keyfixDaf stub; the &v1d[1] pointer
// fold renders as the offset-slice carrier), row 28-kin no consult
// (the sprintf("%d") and strcpy folds render as the String->CharArray
// copies with the trailing NUL, per the row 8/33 family).
// Quirk ledger (kept verbatim): t0px=1 and t2=1 armed, xpx=0; the
// walk counts v1d[i]==1 OR v1d[i]>128 (the two coded-subscript byte
// families; the lower ASCII band is content, not separators); the
// free(svPtr) is the GC no-op divergence ledger (no explicit free on
// the JVM carrier), row 81 hit-reuse (the unbounded vendor read
// svPtr->v1d[i] folds onto the NUL-guard at() seat: past-end reads
// yield the NUL char, the same end-guard discipline as the patrn
// renders).
// SEAT RESOLUTION: this render releases the qlengthDaf named-owner
// stub (DafInterpStubsT3.kt, raised at b4 T3 with owner b5): the
// stub is DELETED and the byte-identical signature carries the real
// body (the Fcn.kt dispatcher call site binds unchanged).
// Dafny: the counting kernel is verified as spec/qlength_count.dfy
// (this tranche's spec): the v1d walk with the 1-or->128 marker rule
// and the j-1 output rule; parse_/keyfix stay stubs (exemption for
// the relay part, per the parser-framework exemption precedent).

fun qlengthDaf(tmp0: CharArray, tmp2: CharArray, svPtrX: DafMsv) {

    var i: Int
    var j = 0

    // int i = svPtrX->LineNumber; svPtr = AllocSV();
    // svPtr->LineNumber = i;  (kept verbatim: assigned, then the
    // memcpy below overwrites it back - no-op vendor quirk, ledger)
    i = svPtrX.LineNumber
    val svPtr = allocSVDaf() // push statevector (b8 stub seat)
    svPtr.LineNumber = i

    // memcpy(svPtr, svPtrX, sizeof(struct MSV)) - field-wise clone
    // (DOCUMENTED DIVERGENCE: byte memcpy -> field clone; the copied
    // unit is the state value, so the observable semantics match)
    cloneMsvInto(svPtrX, svPtr)

    svPtr.t0px = 1
    svPtr.t2 = 1

    // strcpy((char *) svPtr->xd, (const char *) tmp0)
    var k = 0
    while (k < tmp0.size && tmp0[k] != NUL_D) {
        svPtr.xd[k] = tmp0[k]
        k++
    }
    svPtr.xd[k] = NUL_D

    svPtr.xpx = 0

    i = parseDaf(svPtr) // int i = parse_(svPtr) (b9 stub seat)

    // keyfix(&svPtr->v1d[1]) - the offset-slice carrier fold
    keyfixDaf(sliceFrom(svPtr.v1d, 1))

    // for (i=1; svPtr->v1d[i]!=0; i++)
    //     if (svPtr->v1d[i]==1 || svPtr->v1d[i]>128) j++;
    i = 1
    while (at(svPtr.v1d, i) != NUL_D) {
        val c = at(svPtr.v1d, i)
        if (c == '\u0001' || c > '\u0080') j++
        i++
    }

    // if (j==0) strcpy(tmp2,"0"); else sprintf(tmp2,"%d",j-1);
    val out = if (j == 0) "0" else j.toString()
    var w = 0
    while (w < out.length) {
        tmp2[w] = out[w]
        w++
    }
    tmp2[w] = NUL_D

    // free(svPtr) - JVM GC divergence ledger (no explicit free)
}

// struct MSV clone seat (row 18/22 family): copies arrays and scalars
// of the DafMsv carrier; the vendor memcpy byte-copy semantics fold
// onto the field clone (ledger, above).
private fun cloneMsvInto(src: DafMsv, dst: DafMsv) {
    src.v1d.copyInto(dst.v1d)
    src.bd.copyInto(dst.bd)
    src.xd.copyInto(dst.xd)
    src.pd1.copyInto(dst.pd1)
    src.args.copyInto(dst.args)
    src.CurrentFile.copyInto(dst.CurrentFile)
    src.setname.copyInto(dst.setname)
    src.sqloutput.copyInto(dst.sqloutput)
    src.start.copyInto(dst.start)
    src.hor.copyInto(dst.hor)
    src.ver.copyInto(dst.ver)
    dst.xpx = src.xpx
    dst.pd1len = src.pd1len
    dst.sdlim = src.sdlim
    dst.symlen = src.symlen
    dst.setpiece = src.setpiece
    dst.tpx = src.tpx
    dst.t0px = src.t0px
    dst.t2 = src.t2
    dst.ierr = src.ierr
    dst.LineNumber = src.LineNumber
    dst.io = src.io
    dst.NOERR = src.NOERR
    dst.PD1 = src.PD1
    dst.TabSize = src.TabSize
    dst.nstart = src.nstart
    dst._Sym = src._Sym
}

// &v1d[1] pointer fold: the slice-from-offset carrier (fresh copy,
// the stub seat never writes through it today; alias-fold ledger).
private fun sliceFrom(buf: CharArray, off: Int): CharArray =
    buf.copyOfRange(off, buf.size)
