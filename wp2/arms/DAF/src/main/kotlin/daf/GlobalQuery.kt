package daf

// Unit 119 (DAF B4 T3, complex): interp-sym.cpp::GlobalQuery (91 LOC):
// the \\$Query service: pushes a copy of the state vector, parses the
// name argument, asks the global for the XNEXT sibling, then reformats
// the (char)1-coded subscripts back into parenthesized/quoted (or
// tok-delimited) display form.
// Consults: BASE-log rows 22/18 hit-reuse (the compiled-in TU svPtr
// copy: AllocSV pushes a fresh DafMsv and the memcpy state copy folds
// onto the copyState carrier below), row 2/69 (the "** \\$Query
// error" stdout printf println fold + sigint relay), row 33 family
// (the memcpy/strlen/malloc/strcpy libcs fold onto String/CharArray
// carriers; the malloc'd p1 folds onto a String snapshot), row 81
// (NUL-terminated buffer reads at &v1d[1] offsets via cstringAt), row
// 80 hit-adapt (the 1-sentinel bytes of v1d: the (char)1 subscript
// separators kept verbatim as FCN_MARK1_D carrier bytes in place of
// NUL-terminated reads where the vendor mixes both), row 92
// hit-reuse (Mglobal(XNEXT,...) retrieve-then-empty: the k==0 call
// returns the empty tmp1 exactly as vendor; the MumpsGlobalExceptionD
// pattern rides the MglobalDaf seat), row 46 hit-adapt (SQLITE
// reference-build guard: the SQLITE_D=true const makes the
// strncmp/tmp3 reformat tail dead code on the reference build; the
// tail is rendered but gated behind !SQLITE_D with the fold noted).
// Seats: allocSVDaf (roster b8), parseDaf (roster b9), keyfixDaf
// (roster b3), MglobalDaf XNEXT (roster b6), sigintDaf (T1).
// Note: the stub header in DafInterpStubsT3.kt cites globalQueryDaf as
// the unit-119 render seat - this file is that render (seat flip).
// Dafny: EXEMPT (stub-mediated global service; journal row).

// SQLITE reference-build guard: const SQLITE_D seated by the T2
// InterpBifsMisc render (unit 110) - reused; not redeclared here.

// Unit 119 note: keyfix receives &v1d[1] - the CharArray-offset fold
// is carried by copying v1d[1..] into a name String snapshot (row 81
// cstringAt read; the keyfixDaf seat takes the name buffer).

fun globalQueryDaf(tmp0: CharArray, tmp1: CharArray, tok: Char, svPtrX: DafMsv) {

    var i: Int
    var k: Int
    var oflg = 0

    val lineNumber0 = svPtrX.LineNumber
    val svPtr = allocSVDaf() // push statevector
    svPtr.LineNumber = lineNumber0

    // memcpy(svPtr, svPtrX, sizeof(struct MSV)) - copied field by
    // field below: v1d/bd, cursors and scalars (the carrier fold)
    var jx: Int
    jx = 0
    while (at(svPtrX.v1d, jx) != NUL_D) { svPtr.v1d[jx] = svPtrX.v1d[jx]; jx++ }
    svPtr.v1d[jx] = NUL_D
    jx = 0
    while (at(svPtrX.bd, jx) != NUL_D) { svPtr.bd[jx] = svPtrX.bd[jx]; jx++ }
    svPtr.bd[jx] = NUL_D
    svPtr.xpx = svPtrX.xpx; svPtr.pd1len = svPtrX.pd1len
    svPtr.sdlim = svPtrX.sdlim; svPtr.symlen = svPtrX.symlen
    svPtr.setpiece = svPtrX.setpiece; svPtr.tpx = svPtrX.tpx
    svPtr.ierr = svPtrX.ierr; svPtr.io = svPtrX.io
    svPtr.NOERR = svPtrX.NOERR; svPtr.PD1 = svPtrX.PD1
    svPtr.TabSize = svPtrX.TabSize; svPtr.nstart = svPtrX.nstart
    svPtr._Sym = svPtrX._Sym
    for (n in svPtr.start.indices) svPtr.start[n] = svPtrX.start[n]
    for (n in svPtr.hor.indices) svPtr.hor[n] = svPtrX.hor[n]
    for (n in svPtr.ver.indices) svPtr.ver[n] = svPtrX.ver[n]

    svPtr.t0px = 1
    svPtr.t2 = 1

    // strcpy(svPtr->xd, tmp0)
    jx = 0
    while (at(tmp0, jx) != NUL_D) { svPtr.xd[jx] = tmp0[jx]; jx++ }
    svPtr.xd[jx] = NUL_D

    svPtr.xpx = 0
    i = parseDaf(svPtr)

    if (i != 0) {
        print("** \\\$Query error\n")
        sigintDaf(100)
    }

    keyfixDaf(javaCopy(svPtr.v1d, 1)) // keyfix(&svPtr->v1d[1])

    // if (v1d[i=strlen(&v1d[1])] != 1) append the 1,1,0 sentinel trio
    i = cstrlenAt(svPtr.v1d, 1)
    if (at(svPtr.v1d, i) != FCN_MARK1_D) { // ^a case - no parens
        svPtr.v1d[++i] = FCN_MARK1_D
        svPtr.v1d[++i] = FCN_MARK1_D
        svPtr.v1d[++i] = NUL_D
    }

    // char *p1 = malloc + strcpy(&v1d[1]) -> String snapshot (row 33/56 fold)
    val p1 = DafCRuntime.cstringAt(svPtr.v1d, 1)

    for (i2 in 1 until svPtr.v1d.size) { // for (i=1; v1d[i]!=1 && v1d[i]!=0; i++)
        if (svPtr.v1d[i2] == FCN_MARK1_D || svPtr.v1d[i2] == NUL_D) { i = i2; break }
    }

    val xnextRes = CharArray(STR_MAX_DAF) // Mglobal writes through this seat copy
    // strcpy(xnextRes, &v1d[1]) so the retrieve reads the same name bytes
    System.arraycopy(svPtr.v1d, 1, xnextRes, 0, p1.length + 1)
    k = MglobalDaf(XNEXT_D, xnextRes, tmp1, svPtr)
    if (k == 0) { // retrieve-then-empty (row 92)
        tmp1[0] = NUL_D
        return // free(svPtr); free(p1); return (JVM heap fold row 22)
    }

    if (SQLITE_D) {
        // reference build: the reformat tail is dead code (row 46 fold)
        return
    }

    if (strncmpDaf(p1, tmp1, i) != 0) {
        tmp1[0] = NUL_D
        return
    }

    // char tmp3[STR_MAX]; tmp3[0]='^'
    val tmp3 = CharArray(STR_MAX_DAF)
    var tx = 1
    tmp3[0] = '^'

    if (tok == NUL_D) { // tok == 0 (DafChar fold: vendor int)
        tmp1[i] = NUL_D // guard the walk below at the NUL the vendor relies on
        for (i2 in 0 until tmp1.size) {
            if (tmp1[i2] == NUL_D) { i = i2; break }
            if (tmp1[i2] == FCN_MARK1_D && oflg == 0) {
                tmp3[tx++] = '('; tmp3[tx++] = '"'; k++; oflg = 1
            } else if (tmp1[i2] == FCN_MARK1_D && oflg != 0 && tmp1[i2 + 1] != NUL_D) {
                tmp3[tx++] = '"'; tmp3[tx++] = ','; tmp3[tx++] = '"'; k++
            } else if (tmp1[i2] == FCN_MARK1_D) {
                tmp3[tx++] = '"'; tmp3[tx++] = ')'; k++
            } else {
                tmp3[tx++] = tmp1[i2]
            }
        }
    } else {
        for (i2 in 0 until tmp1.size) {
            if (tmp1[i2] == NUL_D) { i = i2; break }
            if (tmp1[i2] == FCN_MARK1_D && oflg == 0) {
                tmp3[tx++] = tok; k++; oflg = 1
            } else if (tmp1[i2] == FCN_MARK1_D && oflg != 0 && tmp1[i2 + 1] != NUL_D) {
                tmp3[tx++] = tok; k++
            } else if (tmp1[i2] == FCN_MARK1_D) {
                tmp3[tx++] = tok; k++
            } else {
                tmp3[tx++] = tmp1[i2]
            }
        }
    }
    tmp3[tx] = NUL_D
    // strcpy(tmp1, tmp3)
    jx = 0
    while (at(tmp3, jx) != NUL_D) { tmp1[jx] = tmp3[jx]; jx++ }
    tmp1[jx] = NUL_D
    return
}

// &v1d[1] carrier: NUL-terminated CharArray snapshot from offset off
private fun javaCopy(buf: CharArray, off: Int): CharArray {
    var n = off
    while (n < buf.size && buf[n] != NUL_D) n++
    val r = CharArray(n - off + 1)
    for (ix in 0 until n - off) r[ix] = buf[off + ix]
    return r
}

// strlen at an offset (const char* &buf[off]; row 81 fold)
private fun cstrlenAt(buf: CharArray, off: Int): Int {
    var n = off
    while (n < buf.size && buf[n] != NUL_D) n++
    return n
}

private fun strncmpDaf(a: String, b: CharArray, n: Int): Int {
    for (ix in 0 until n) {
        val ca = if (ix < a.length) a[ix] else NUL_D
        val cb = at(b, ix)
        if (ca != cb) return ca - cb
        if (ca == NUL_D) return 0
    }
    return 0
}