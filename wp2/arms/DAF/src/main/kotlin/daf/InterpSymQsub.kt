package daf

// Unit 208 (DAF B7 T3, complex): interp-sym.cpp::Qsub (52 LOC,
// score 53.8): the single-subscript extraction service on a
// possibly-global name: the state-vector push-copy (AllocSV +
// memcpy), parse_ over the name via keyfix, gpad save/restore,
// then the j-th \u0001-subscript slice walk into tmp1 with the
// j==-1 ("" empty) and j==-2 (whole-name) early arms. Same TU
// and same scaffold as the unit-119 GlobalQuery (B4 T3): the
// memcpy state copy folds onto the copyState carrier exactly as
// there (rows 22/18), and the free(svPtr) folds onto GC (the
// JVM carrier; GlobalQuery precedent, ledger note).
// Divergences ledgered: (1) the gpad save/restore is Qsub's own
// (j = svPtr->gpad; svPtr->gpad = 0; restore after parse_) - not
// in GlobalQuery - kept verbatim; (2) the vendor's \x01-or->128
// subscript split: the >128 arm compares the SIGNED char
// (negative on this build) - the render keeps the >128
// comparison on the Kotlin Char code point (the vendor's
// unsigned-deref read is the same domain), manual-decision
// ledger; (3) the &tmp0[1]/tmp0 caret-targeted copy-back of the
// keyfixed name rides the gblflg gate (the ^-bypass read
// cstringAt fold of the Avg/Count family).
// Seats: allocSVDaf (DafInterpStubsT3:127), parseDaf
// (DafInterpStubsT3:109), keyfixDaf (DafInterpStubsT3:191), atoi
// (DafCRuntimeT2:21), NUL/carrier folds DafCRuntime.
// Consults (2026-11-24T20:11:00Z, nominal): BASE-log rows 22/18
// hit-reuse (the push-state AllocSV + field-copyState carrier of
// the unit-119 precedent), row 33 hit-reuse (memcpy/strcpy/strlen
// libc folds onto carrier loops + String), row 81 hit-reuse
// (the &v1d[1]/&tmp0[1] offset reads via cstringAt), row 80
// hit-adapt (the (char)1 subscript separators kept as
// FCN_MARK1_D carrier bytes), row 28 hit-reuse (the atoi tail on
// the tmp2 order arg), rows 14/15 hit-reuse (the j==-1/-2
// sentinel compare family), row 56 hit-reuse (the CharArray
// scratch carriers), manual-decision (the >128 signed-char
// domain note above; the free()/GC fold).
// Dafny: EXEMPT (state-push service over stub seats; journal row).

fun qsubDaf(tmp0: CharArray, tmp1: CharArray, tmp2: CharArray?, svPtrX: DafMsv) {

    var i: Int
    var j = 0
    var k: Int
    var m = 0
    val gblflg = tmp0[0] == '^' // if (tmp0[0]=='^') gblflg=1;

    val lineNumber0 = svPtrX.LineNumber
    val svPtr = allocSVDaf() // push statevector
    svPtr.LineNumber = lineNumber0

    // memcpy(svPtr, svPtrX, ...) - copyState carrier (unit-119 fold)
    var jx = 0
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

    svPtr.t0px = 0 // svPtr->t0px = 0
    svPtr.t2 = 1   // svPtr->t2 = 1

    // strcpy(svPtr->xd, tmp0)
    jx = 0
    while (at(tmp0, jx) != NUL_D) { svPtr.xd[jx] = tmp0[jx]; jx++ }
    svPtr.xd[jx] = NUL_D

    svPtr.xpx = 0
    j = svPtr.gpad          // j = svPtr->gpad
    svPtr.gpad = 0
    i = parseDaf(svPtr)     // i = parse_(svPtr)
    keyfixDaf(qsubJavaCopy(svPtr.v1d, 1)) // keyfix(&svPtr->v1d[1])
    svPtr.gpad = j          // svPtr->gpad = j

    if (gblflg) { // strcpy(&tmp0[1], &svPtr->v1d[1])
        copyAtOff(tmp0, 1, svPtr.v1d, 1)
    } else {      // strcpy(tmp0, &svPtr->v1d[1])
        copyAtOff(tmp0, 0, svPtr.v1d, 1)
    }
    var job = 0
    if (tmp2 != null) { // if (tmp2 != NULL) j = atoi(tmp2)
        job = atoi(DafCRuntime.cstring(tmp2))
    }

    if (job == -1) { // j==-1: empty-name arm
        tmp1[0] = NUL_D // strcpy(tmp1, "")
        // free(svPtr); - GC fold (unit-119 precedent)
        return
    }

    if (job == -2) { // j==-2: whole-name arm
        jx = 0
        while (at(tmp0, jx) != NUL_D) { tmp1[jx] = tmp0[jx]; jx++ }
        if (jx < tmp1.size) tmp1[jx] = NUL_D
        // free(svPtr); - GC fold
        return
    }

    val n = qsubStrlen(tmp0) // n = strlen(tmp0)
    i = 0
    for (k in 0..job) { // for (k=0; k<=j; k++)
        m = i
        if (m >= n) { // m>=n: exhausted-subscript arm
            tmp1[0] = NUL_D
            // free(svPtr); - GC fold
            return
        }
        while (at(tmp0, i) != NUL_D) { // for (; tmp0[i]!=0; i++)
            if (tmp0[i] == FCN_MARK1_D || tmp0[i] > '\u0080') {
                tmp0[i] = NUL_D // tmp0[i]='\0'; break
                break
            }
            i++
        }
        i++
    }
    // strcpy(tmp1, &tmp0[m])
    jx = m
    var w = 0
    while (jx < tmp1.size && at(tmp0, jx) != NUL_D) { tmp1[w] = tmp0[jx]; jx++; w++ }
    if (w < tmp1.size) tmp1[w] = NUL_D

    // free(svPtr); - GC fold
}

private fun qsubJavaCopy(buf: CharArray, off: Int): CharArray {
    val out = CharArray(buf.size - off)
    var i = 0
    while (off + i < buf.size && buf[off + i] != NUL_D) { out[i] = buf[off + i]; i++ }
    if (i < out.size) out[i] = NUL_D
    return out
}

private fun qsubStrlen(buf: CharArray): Int {
    var n = 0
    while (n < buf.size && buf[n] != NUL_D) n++
    return n
}

// strcpy(dst+dstOff, src+srcOff) carrier fold
private fun copyAtOff(dst: CharArray, dstOff: Int, src: CharArray, srcOff: Int) {
    var r = srcOff
    var w = dstOff
    while (r < src.size && src[r] != NUL_D) {
        if (w < dst.size - 1) dst[w] = src[r]
        r++; w++
    }
    if (w < dst.size) dst[w] = NUL_D
}
