package daf

// Unit 215 (DAF B7 T3, complex): interp-service.cpp::ServiceInterface
// (roster row 187, units.csv row 215: loc 279, score 403.7) - THIS
// RENDER RESOLVES the serviceInterfaceDaf seat raised at Zfcns.kt
// (the unit-184 zglobal GBLNME relay arms and the zz-family call
// sites bind unchanged; the seat's (a,b,c,d,e,f,sig) carrier order
// IS the vendor tmp0/tmp1/tmp4/tmp5/tmp3/tok/svPtrX order). The
// service entry: the early-return token arms (GETENV 18 through
// GetenvD out-then-in order: the vendor passes out=tmp3 result,
// in=tmp0 name; SCANALNUM 5 / SCAN 6 / INPUT 19 through the
// SV-bound wrappers, NOT drawn on this arm: named stub seats), then
// the state-vector push (AllocSV the allocSVDaf roster-b8 stub; the
// copyState carrier shape of the unit-208 Qsub precedent: the
// vendor memcpy copied over the fresh AllocSV so the render copies
// per-field in place; the LineNumber re-set after the copy is the
// vendor verbatim line, kept), then the parse-and-keyfix prologue
// repeated VERBATIM per token arm (t0px=1, t2=1, xd strcpy, xpx=0,
// parse_ through the parseDaf roster-b9 seat, keyfix at the
// &v1d[2] offset-2 slice, the tmpA/tmpB/tmpC captures from the
// &v1d[1] offset-1 tail) - the repetition is the vendor's own
// copy-paste, kept; each arm then hands the parsed carriers to the
// wrapper battery: CosineW/Sim1W/JaccardW/CentroidW/DiceW/
// TermCorrelateW/DocCorrelateW (LibmpscppMisc9), SumW (SumW.kt),
// Min/Count/Transpose (the landed free-fn renders), AvgD (Avg.kt;
// the vendor ep "" is the dead-param, dropped at that seat's
// signature - H-013 note). The DocCorrelate tmp4/tmp5 nesting
// carriers are nullable in the seat (the zcn NULL x3/x4 callers):
// the DOCCORRELATE arm dereferences both in the vendor, so the
// render raises the missing-array case (the T2 RsltCallback
// name0 backstop-binding precedent). MMULTIPLY/MAX wrappers
// are rows not drawn on this arm: named stub seats; the IDF
// wrapper seat was RELEASED at unit 223 (B8, Idf.kt) The INVDF arm
// keeps the vendor quirk verbatim: tmpA[strlen(tmpA)+1]=0 writes
// the second NUL one PAST the terminator (the strlen+1 index quirk
// ledgered, not fixed), and tmpB takes tmp1 UNPARSED (no parse/
// keyfix on that side). The GBLNME arm skips the keyfix, drives
// the Mglobal GBLNME walk with the rslt fresh scratch and the
// tmp3 write-back; f is the dead capture (H-013, assigned never
// read). The trailing svPtrX->tpx = svPtr->tpx hand-off and
// free(svPtr) (JVM GC no-op ledger) kept. H-013: k/oflg are
// declared and never used in the vendor - dead locals kept as the
// zero seats. H-010: tok rides the Char carrier of the vendor char
// (the defines.h:226-245 token ladder, 1..20, per-file const list
// below); i re-based per arm.
//
// Consults (nominal UTC 2026-11-24T20:14:30Z): BASE-log rows 22/18
// hit-reuse (the push-state AllocSV + copyState carrier of the
// unit-119/Qsub precedents), row 33 hit-adapt (memcpy/strcpy/
// strmove fold onto carrier loops - strmove has no roster owner,
// the unit-144 fold cite), row 81 hit-reuse (the &v1d[1]/&v1d[2]
// offset tail/slice reads), row 56 hit-reuse (the CharArray
// scratch carriers tmpA/tmpB/tmpC), row 91 hit-adapt (the
// (unsigned char *) readability fold: the sv-bound wrappers ride
// their named-owner seats), manual-decision (the nullable c/d
// carriers; the free()/GC fold; the DAf seat-release chain note:
// serviceInterfaceDaf released per the levCheck precedent).
// H-015 hygiene: no raw dollars ride the comments.
// Dafny: EXEMPT (state-push service relay over stub seats and
// wrapper battery; journal row).

// defines.h:226-245 token ladder (vendor values; per-file seats)
private const val GETENV_SI_D = 18
private const val SCANALNUM_SI_D = 5
private const val SCAN_SI_D = 6
private const val INPUT_SI_D = 19
private const val COSINE_SI_D = 1
private const val SIM1_SI_D = 8
private const val JACCARD_SI_D = 9
private const val TRANSPOSE_SI_D = 2
private const val MMULTIPLY_SI_D = 15
private const val CENTROID_SI_D = 10
private const val TERMCORRELATE_SI_D = 11
private const val DOCCORRELATE_SI_D = 17
private const val INVDF_SI_D = 3
private const val AVG_SI_D = 7
private const val GBLNME_SI_D = 20
private const val COUNT_SI_D = 12
private const val MAX_SI_D = 13
private const val MIN_SI_D = 14
private const val SUM_SI_D = 16
private const val DICE_SI_D = 4

// SV-bound early-return wrappers (rows not drawn on this arm)
fun scanAlnumSvDaf(svPtrX: DafMsv, ep: CharArray): String =
    throw NotImplementedError(
        "ScanAlnum (SV-bound wrapper): out-of-scope owner (interp-parse.cpp Scan family; roster row of a later cycle)")

fun scanSvDaf(svPtrX: DafMsv): String =
    throw NotImplementedError(
        "Scan (SV-bound wrapper): out-of-scope owner (interp-parse.cpp::Scan; roster row of a later cycle)")

fun inputSvDaf(svPtrX: DafMsv, ep: CharSequence, name: CharArray): String =
    throw NotImplementedError(
        "Input (SV-bound wrapper): out-of-scope owner (interp-io.cpp::Input; roster row of a later cycle)")

// Wrapper rows not drawn on this arm (MMULTIPLY x3-form, MAX);
// idfW RELEASED at unit 223 (B8): the real body landed at Idf.kt
// under this same name and signature; the stub is deleted and the
// dispatcher call site below resolves unchanged (the levCheck
// delegate precedent).
fun multiplyW(svptr: DafMsv, ep: CharArray, arg1: CharArray, arg2: CharArray,
        arg3: CharArray): String =
    throw NotImplementedError(
        "Multiply (SV-bound wrapper): out-of-scope owner (libmpscpp.cpp::Multiply x3 wrapper; kin of the landed unit-141/201 bodies; roster row of a later cycle)")

fun maxW(svptr: DafMsv, ep: CharArray, arg1: CharArray): String =
    throw NotImplementedError(
        "Max (SV-bound wrapper): out-of-scope owner (libmpscpp.cpp Max wrapper; kin of the landed GlobalMax node member; roster row of a later cycle)")

fun serviceInterfaceD(tmp0: CharArray, tmp1: CharArray, tmp4: CharArray?,
        tmp5: CharArray?, tmp3: CharArray, tok: Char, svPtrX: DafMsv) {

    var i: Int
    val k = 0                                     // H-013 dead local
    val oflg = 0                                  // H-013 dead local
    val tmpA = CharArray(STR_MAX_DAF)
    val tmpB = CharArray(STR_MAX_DAF)
    val tmpC = CharArray(STR_MAX_DAF)

    tmpA[0] = NUL_D // tmpA[0]=0
    tmpB[0] = NUL_D // tmpB[0]=0

    if (tok == GETENV_SI_D.toChar()) {
        GetenvD(tmp3, DafCRuntime.cstring(tmp0)) // Getenv(tmp3,tmp0)
        return
    }

    if (tok == SCANALNUM_SI_D.toChar()) {
        siStrmove(tmp3, scanAlnumSvDaf(svPtrX, CharArray(1))) // ep dead seat
        return
    }

    if (tok == SCAN_SI_D.toChar()) {
        siStrmove(tmp3, scanSvDaf(svPtrX))
        return
    }

    if (tok == INPUT_SI_D.toChar()) {
        siStrmove(tmp3, inputSvDaf(svPtrX, "", tmp0))
        return
    }

    i = svPtrX.LineNumber
    val svPtr = allocSVDaf() /* push statevector */
    svPtr.LineNumber = i

    // memcpy(svPtr,svPtrX,sizeof(struct MSV)); /* copy old state
    // vector - inefficient */ - the copyState carrier (unit-119/
    // Qsub fold); the LineNumber re-set is the vendor line after
    // the memcpy, kept verbatim.
    copyStateDaf(svPtr, svPtrX)
    svPtr.LineNumber = i

    if (tok == COSINE_SI_D.toChar()) {
        svPtr.t0px = 1
        svPtr.t2 = 1
        siStrcpy(svPtr.xd, tmp0)
        svPtr.xpx = 0
        i = parseDaf(svPtr)
        keyfixDaf(svPtr.v1d.copyOfRange(2, svPtr.v1d.size)) // &v1d[2]
        siStrcpyAt(tmpA, svPtr.v1d, 1)                       // &v1d[1]

        svPtr.t0px = 1
        svPtr.t2 = 1
        siStrcpy(svPtr.xd, tmp1)
        svPtr.xpx = 0
        i = parseDaf(svPtr)
        keyfixDaf(svPtr.v1d.copyOfRange(2, svPtr.v1d.size))
        siStrcpyAt(tmpB, svPtr.v1d, 1)

        siStrmove(tmp3, CosineW(svPtr, CharArray(1), tmpA, tmpB))
    }

    else if (tok == SIM1_SI_D.toChar()) {
        svPtr.t0px = 1
        svPtr.t2 = 1
        siStrcpy(svPtr.xd, tmp0)
        svPtr.xpx = 0
        i = parseDaf(svPtr)
        keyfixDaf(svPtr.v1d.copyOfRange(2, svPtr.v1d.size))
        siStrcpyAt(tmpA, svPtr.v1d, 1)

        svPtr.t0px = 1
        svPtr.t2 = 1
        siStrcpy(svPtr.xd, tmp1)
        svPtr.xpx = 0
        i = parseDaf(svPtr)
        keyfixDaf(svPtr.v1d.copyOfRange(2, svPtr.v1d.size))
        siStrcpyAt(tmpB, svPtr.v1d, 1)

        siStrmove(tmp3, Sim1W(svPtr, CharArray(1), tmpA, tmpB))
    }

    else if (tok == JACCARD_SI_D.toChar()) {
        svPtr.t0px = 1
        svPtr.t2 = 1
        siStrcpy(svPtr.xd, tmp0)
        svPtr.xpx = 0
        i = parseDaf(svPtr)
        keyfixDaf(svPtr.v1d.copyOfRange(2, svPtr.v1d.size))
        siStrcpyAt(tmpA, svPtr.v1d, 1)

        svPtr.t0px = 1
        svPtr.t2 = 1
        siStrcpy(svPtr.xd, tmp1)
        svPtr.xpx = 0
        i = parseDaf(svPtr)
        keyfixDaf(svPtr.v1d.copyOfRange(2, svPtr.v1d.size))
        siStrcpyAt(tmpB, svPtr.v1d, 1)

        siStrmove(tmp3, JaccardW(svPtr, CharArray(1), tmpA, tmpB))
    }

    else if (tok == TRANSPOSE_SI_D.toChar()) {
        svPtr.t0px = 1
        svPtr.t2 = 1
        siStrcpy(svPtr.xd, tmp0)
        svPtr.xpx = 0
        i = parseDaf(svPtr)
        keyfixDaf(svPtr.v1d.copyOfRange(2, svPtr.v1d.size))
        siStrcpyAt(tmpA, svPtr.v1d, 1)

        svPtr.t0px = 1
        svPtr.t2 = 1
        siStrcpy(svPtr.xd, tmp1)
        svPtr.xpx = 0
        i = parseDaf(svPtr)
        keyfixDaf(svPtr.v1d.copyOfRange(2, svPtr.v1d.size))
        siStrcpyAt(tmpB, svPtr.v1d, 1)

        // Transpose(svPtr,"",tmpA,tmpB) - the ep dead seat (H-013)
        siStrmove(tmp3, Transpose(svPtr, CharArray(1), tmpA, tmpB))
    }

    else if (tok == MMULTIPLY_SI_D.toChar()) {
        svPtr.t0px = 1
        svPtr.t2 = 1
        siStrcpy(svPtr.xd, tmp0)
        svPtr.xpx = 0
        i = parseDaf(svPtr)
        keyfixDaf(svPtr.v1d.copyOfRange(2, svPtr.v1d.size))
        siStrcpyAt(tmpA, svPtr.v1d, 1)

        svPtr.t0px = 1
        svPtr.t2 = 1
        siStrcpy(svPtr.xd, tmp1)
        svPtr.xpx = 0
        i = parseDaf(svPtr)
        keyfixDaf(svPtr.v1d.copyOfRange(2, svPtr.v1d.size))
        siStrcpyAt(tmpB, svPtr.v1d, 1)

        svPtr.t0px = 1
        svPtr.t2 = 1
        siStrcpy(svPtr.xd, tmp4 ?: throw IllegalStateException(
            "ServiceInterface: MMULTIPLY requires tmp4"))
        svPtr.xpx = 0
        i = parseDaf(svPtr)
        keyfixDaf(svPtr.v1d.copyOfRange(2, svPtr.v1d.size))
        siStrcpyAt(tmpC, svPtr.v1d, 1)

        siStrmove(tmp3, multiplyW(svPtr, CharArray(1), tmpA, tmpB, tmpC))
    }

    else if (tok == CENTROID_SI_D.toChar()) {
        svPtr.t0px = 1
        svPtr.t2 = 1
        siStrcpy(svPtr.xd, tmp0)
        svPtr.xpx = 0
        i = parseDaf(svPtr)
        keyfixDaf(svPtr.v1d.copyOfRange(2, svPtr.v1d.size))
        siStrcpyAt(tmpA, svPtr.v1d, 1)

        svPtr.t0px = 1
        svPtr.t2 = 1
        siStrcpy(svPtr.xd, tmp1)
        svPtr.xpx = 0
        i = parseDaf(svPtr)
        keyfixDaf(svPtr.v1d.copyOfRange(2, svPtr.v1d.size))
        siStrcpyAt(tmpB, svPtr.v1d, 1)

        siStrmove(tmp3, CentroidW(svPtr, CharArray(1), tmpA, tmpB))
    }

    else if (tok == TERMCORRELATE_SI_D.toChar()) {
        svPtr.t0px = 1
        svPtr.t2 = 1
        siStrcpy(svPtr.xd, tmp0)
        svPtr.xpx = 0
        i = parseDaf(svPtr)
        keyfixDaf(svPtr.v1d.copyOfRange(2, svPtr.v1d.size))
        siStrcpyAt(tmpA, svPtr.v1d, 1)

        svPtr.t0px = 1
        svPtr.t2 = 1
        siStrcpy(svPtr.xd, tmp1)
        svPtr.xpx = 0
        i = parseDaf(svPtr)
        keyfixDaf(svPtr.v1d.copyOfRange(2, svPtr.v1d.size))
        siStrcpyAt(tmpB, svPtr.v1d, 1)

        siStrmove(tmp3, TermCorrelateW(svPtr, CharArray(1), tmpA, tmpB))
    }

    else if (tok == DOCCORRELATE_SI_D.toChar()) {
        svPtr.t0px = 1
        svPtr.t2 = 1
        siStrcpy(svPtr.xd, tmp0)
        svPtr.xpx = 0
        i = parseDaf(svPtr)
        keyfixDaf(svPtr.v1d.copyOfRange(2, svPtr.v1d.size))
        siStrcpyAt(tmpA, svPtr.v1d, 1)

        svPtr.t0px = 1
        svPtr.t2 = 1
        siStrcpy(svPtr.xd, tmp1)
        svPtr.xpx = 0
        i = parseDaf(svPtr)
        keyfixDaf(svPtr.v1d.copyOfRange(2, svPtr.v1d.size))
        siStrcpyAt(tmpB, svPtr.v1d, 1)

        // tmp4/tmp5 nullable seats: the vendor dereferences both on
        // this arm - the backstop raises the missing case (the T2
        // RsltCallback name0 precedent).
        siStrmove(tmp3, DocCorrelateW(svPtr, CharArray(1), tmpA, tmpB,
            tmp4 ?: throw IllegalStateException(
                "ServiceInterface: DOCCORRELATE requires tmp4"),
            tmp5 ?: throw IllegalStateException(
                "ServiceInterface: DOCCORRELATE requires tmp5")))
    }

    else if (tok == INVDF_SI_D.toChar()) {
        svPtr.t0px = 1
        svPtr.t2 = 1
        siStrcpy(svPtr.xd, tmp0)
        svPtr.xpx = 0
        i = parseDaf(svPtr)
        keyfixDaf(svPtr.v1d.copyOfRange(2, svPtr.v1d.size))
        siStrcpyAt(tmpA, svPtr.v1d, 1)

        // tmpA[strlen(tmpA)+1]=0 - the vendor strlen+1 quirk writes
        // a second NUL one PAST the terminator; kept.
        tmpA[siStrlen(tmpA) + 1] = NUL_D

        siStrcpy(tmpB, tmp1) // tmpB takes tmp1 UNPARSED (vendor)

        siStrmove(tmp3, idfW(svPtr, CharArray(1), tmpA, tmpB))
    }

    else if (tok == AVG_SI_D.toChar()) {
        svPtr.t0px = 1
        svPtr.t2 = 1
        siStrcpy(svPtr.xd, tmp0)
        svPtr.xpx = 0
        i = parseDaf(svPtr)
        keyfixDaf(svPtr.v1d.copyOfRange(2, svPtr.v1d.size))
        siStrcpyAt(tmpA, svPtr.v1d, 1)

        // Avg(svPtr,"",tmpA) - the landed AvgD seat drops the dead
        // ep param (H-013 note at that render)
        siStrmove(tmp3, AvgD(tmpA))
    }

    else if (tok == GBLNME_SI_D.toChar()) {
        svPtr.t0px = 1
        svPtr.t2 = 1
        siStrcpy(svPtr.xd, tmp0)
        svPtr.xpx = 0
        i = parseDaf(svPtr)
        // no keyfix on this arm (vendor); tmp3 takes the &v1d[1]
        // offset-1 tail directly.
        siStrcpyAt(tmp3, svPtr.v1d, 1)

        val rslt = CharArray(STR_MAX_DAF)
        // int f - H-013 dead capture (assigned, never read).
        val f = MglobalDaf(GBLNME_SI_D, tmp3, rslt, svPtr)
        siStrcpy(tmp3, rslt) // strcpy(tmp3, rslt)
    }

    else if (tok == COUNT_SI_D.toChar()) {
        svPtr.t0px = 1
        svPtr.t2 = 1
        siStrcpy(svPtr.xd, tmp0)
        svPtr.xpx = 0
        i = parseDaf(svPtr)
        keyfixDaf(svPtr.v1d.copyOfRange(2, svPtr.v1d.size))
        siStrcpyAt(tmpA, svPtr.v1d, 1)

        siStrmove(tmp3, Count(svPtr, CharArray(1), tmpA))
    }

    else if (tok == MAX_SI_D.toChar()) {
        svPtr.t0px = 1
        svPtr.t2 = 1
        siStrcpy(svPtr.xd, tmp0)
        svPtr.xpx = 0
        i = parseDaf(svPtr)
        keyfixDaf(svPtr.v1d.copyOfRange(2, svPtr.v1d.size))
        siStrcpyAt(tmpA, svPtr.v1d, 1)

        siStrmove(tmp3, maxW(svPtr, CharArray(1), tmpA))
    }

    else if (tok == MIN_SI_D.toChar()) {
        svPtr.t0px = 1
        svPtr.t2 = 1
        siStrcpy(svPtr.xd, tmp0)
        svPtr.xpx = 0
        i = parseDaf(svPtr)
        keyfixDaf(svPtr.v1d.copyOfRange(2, svPtr.v1d.size))
        siStrcpyAt(tmpA, svPtr.v1d, 1)

        siStrmove(tmp3, Min(svPtr, CharArray(1), tmpA))
    }

    else if (tok == SUM_SI_D.toChar()) {
        svPtr.t0px = 1
        svPtr.t2 = 1
        siStrcpy(svPtr.xd, tmp0)
        svPtr.xpx = 0
        i = parseDaf(svPtr)
        keyfixDaf(svPtr.v1d.copyOfRange(2, svPtr.v1d.size))
        siStrcpyAt(tmpA, svPtr.v1d, 1)

        siStrmove(tmp3, SumW(svPtr, CharArray(1), tmpA))
    }

    else if (tok == DICE_SI_D.toChar()) {
        svPtr.t0px = 1
        svPtr.t2 = 1
        siStrcpy(svPtr.xd, tmp0)
        svPtr.xpx = 0
        i = parseDaf(svPtr)
        keyfixDaf(svPtr.v1d.copyOfRange(2, svPtr.v1d.size))
        siStrcpyAt(tmpA, svPtr.v1d, 1)

        svPtr.t0px = 1
        svPtr.t2 = 1
        siStrcpy(svPtr.xd, tmp1)
        svPtr.xpx = 0
        i = parseDaf(svPtr)
        keyfixDaf(svPtr.v1d.copyOfRange(2, svPtr.v1d.size))
        siStrcpyAt(tmpB, svPtr.v1d, 1)

        siStrmove(tmp3, DiceW(svPtr, CharArray(1), tmpA, tmpB))
    }

    svPtrX.tpx = svPtr.tpx

    // free (svPtr); - JVM GC no-op (ledger).
}

// ---- private carriers (row 8/33 folds; per-file scope) ----

// strmove(dst, src): the strcpy-shaped out-write of the service
// results (no roster owner; the unit-144 fold cite).
private fun siStrmove(dst: CharArray, src: String) {
    var q = 0
    while (q < src.length) { dst[q] = src[q]; q++ }
    dst[q] = NUL_D
}

// strcpy(dst, src) over CharArrays.
private fun siStrcpy(dst: CharArray, src: CharArray) {
    var q = 0
    while (q < src.size && src[q] != NUL_D) { dst[q] = src[q]; q++ }
    dst[q] = NUL_D
}

// strcpy(dst, &src[off]) - the offset-tail capture.
private fun siStrcpyAt(dst: CharArray, src: CharArray, off: Int) {
    var q = 0
    while (off + q < src.size && src[off + q] != NUL_D) {
        dst[q] = src[off + q]; q++
    }
    dst[q] = NUL_D
}

// strlen analog.
private fun siStrlen(buf: CharArray): Int {
    var q = 0
    while (q < buf.size && buf[q] != NUL_D) q++
    return q
}

// copyState carrier (the unit-119/Qsub fold; bounded to the MSV
// field surface of the DafMsv class declaration). Shared seat
// rename batch 9 unit 264: the private siCopyState promotes to
// the package-level copyStateDaf so the interp-sym Eval release
// (InterpSymEval.kt) reuses the same memcpy fold; the call site
// here binds unchanged.
fun copyStateDaf(dst: DafMsv, src: DafMsv) {
    var q = 0
    while (src.v1d[q] != NUL_D) { dst.v1d[q] = src.v1d[q]; q++ }
    dst.v1d[q] = NUL_D
    q = 0
    while (src.bd[q] != NUL_D) { dst.bd[q] = src.bd[q]; q++ }
    dst.bd[q] = NUL_D
    q = 0
    while (src.xd[q] != NUL_D) { dst.xd[q] = src.xd[q]; q++ }
    dst.xd[q] = NUL_D
    q = 0
    while (src.pd1[q] != NUL_D) { dst.pd1[q] = src.pd1[q]; q++ }
    dst.pd1[q] = NUL_D
    dst.xpx = src.xpx
    dst.pd1len = src.pd1len
    dst.sdlim = src.sdlim
    dst.symlen = src.symlen
    dst.setpiece = src.setpiece
    dst.tpx = src.tpx
    dst.t0px = src.t0px
    dst.t2 = src.t2
    dst.ierr = src.ierr
    dst.io = src.io
    dst.NOERR = src.NOERR
    dst.PD1 = src.PD1
    dst.TabSize = src.TabSize
    dst.nflg = src.nflg
    dst.nstart = src.nstart
    dst._Sym = src._Sym
    dst.LineNumber = src.LineNumber
    dst.symflg = src.symflg
    dst.order = src.order
    dst.ITRAP = src.ITRAP
    dst.ctrlc = src.ctrlc
    dst.iorg = src.iorg
    dst.ERROR = src.ERROR
    dst.gpad = src.gpad
    dst.LockDone = src.LockDone
    dst.kflg = src.kflg
    dst.hit = src.hit
    dst.count = src.count
    dst.restoredata = src.restoredata
    dst.ScanMinWordSize = src.ScanMinWordSize
    dst.ScanMaxWordSize = src.ScanMaxWordSize
    for (n in src.start.indices) dst.start[n] = src.start[n]
    for (n in src.hor.indices) dst.hor[n] = src.hor[n]
    for (n in src.ver.indices) dst.ver[n] = src.ver[n]
    for (n in src.in_file.indices) dst.in_file[n] = src.in_file[n]
    for (n in src.out_file.indices) dst.out_file[n] = src.out_file[n]
    for (n in src.RefParm.indices) {
        for (m in src.RefParm[n].indices) dst.RefParm[n][m] = src.RefParm[n][m]
    }
    for (n in src.Rargs.indices) {
        for (m in src.Rargs[n].indices) dst.Rargs[n][m] = src.Rargs[n][m]
    }
}
