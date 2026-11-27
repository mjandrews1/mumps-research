package both

// Unit 186 render: interp-service.cpp::ServiceInterface (roster 186,
// batch 7, 279 LOC). The dispatched C_* service face of the zz zfcn
// lattice: the b6 renders (BifsZfcn.kt / ZfcnsZfcn.kt) call this
// seat through zzSvc1Zb / zzSvc2Zb / zzScanZb / zzDocCorrelateZb /
// zzMultiplyZb with the token codes ZC_* (the previous TODO stub in
// BifsZfcn.kt is REMOVED by this render, h014 release-at-render).
//
// Consults (rule base wp2/arms/BOTH/rules_v0.pl):
//   h014 out_of_scope_call - reused: every pending-owner call below
//     is seated as a named TODO stub pinning its roster owner
//     (Scan 75, ScanAlnum 77, Input 9, Cosine 85, Sim1 208,
//     Jaccard 255, Centroid 210, TermCorrelate 36, IDF 45,
//     DocCorrelate 287, Count 120, Max 68, Min 69; the Dice service
//     face is roster 158, member face rendered at B6).
//   h015 grammar-collision - reused: no backticks, no raw comment
//     dollars (star not followed by slash), ASCII only, trailing
//     newline.
//   h021 struct_carrier - reused: the allocSV state push rides the
//     Qlength shallow-fold precedent (the pushed carrier shares the
//     donor arrays; writes happen before any read on this path).
//   FOLDS (disclosed): the vendor repeats the six-line parse ladder
//     (t0px=1; t2=1; strcpy(xd, expr); xpx=0; i=parse_(svPtr);
//     keyfix(&v1d[2])) at every arm; folded into svcEvalZb. The
//     GBLNME arm skips keyfix in the vendor and is kept inline for
//     that reason. int i is dead past the ladder (dead store kept on
//     the record); j, k, oflg are declared and never read.
//   free(svPtr) at the tail has no JVM face; GC owns it.

// defines.h:245 GBLNME 20 - the Mglobal name-query op (the arm's
// existing Mglobal op consts do not yet include it; no collision).
private const val SVC_GBLNME = 20

// strcpy(dst, src) over C-strings
private fun svcCpyZb(dst: CharArray, src: CharArray) {
    var i = 0
    while (i < src.size && src[i] != '\u0000') { dst[i] = src[i]; i++ }
    if (i < dst.size) dst[i] = '\u0000'
}

// the C-string of a tmp buffer (src face of tmp0/tmp1/tmp4/tmp5)
private fun svcStrZb(a: CharArray): String {
    val sb = StringBuilder()
    var i = 0
    while (i < a.size && a[i] != '\u0000') { sb.append(a[i]); i++ }
    return sb.toString()
}

// the six-line vendor ladder, folded per arm (see header consults)
private fun svcEvalZb(sv: Msv, expr: CharArray) {
    sv.t0px = 1
    sv.t2 = 1
    svcCpyZb(sv.xd, expr) // strcpy((char *) svPtr->xd, expr)
    sv.xpx = 0
    parse_(sv) // i = parse_(svPtr); i unused past (dead store)
    // keyfix(&svPtr->v1d[2]): the offset-2 twin of the Qlength
    // offset-1 seat; the whole tail rides out and back so trailing
    // residue survives (the keyfixC observable-key note).
    val off2 = sv.v1d.copyOfRange(2, sv.v1d.size)
    keyfixC(off2)
    var i = 0
    while (i < off2.size) { sv.v1d[2 + i] = off2[i]; i++ }
}

// strcpy((char *) tmpX, (char *) &svPtr->v1d[1])
private fun svcLoadV1dZb(sv: Msv, dst: CharArray) {
    svcCpyZb(dst, sv.v1d.copyOfRange(1, sv.v1d.size))
}

// the svc kin whose roster owners are pending in this arm (h014
// TODO seats; compile-green bodies, owner release replaces them -
// the BifsZfcn stub-removal precedent)
private fun scanSvcZb(sv: Msv): String {
    TODO("stub: owner libmpscpp.cpp::Scan (roster 75, batch 3)")
}
private fun scanAlnumSvcZb(sv: Msv, ep: String): String {
    TODO("stub: owner libmpscpp.cpp::ScanAlnum (roster 77, batch 3)")
}
private fun inputSvcZb(sv: Msv, ep: String, name1: String): String {
    TODO("stub: owner libmpscpp.cpp::Input (roster 9, batch 1)")
}
private fun cosineSvcZb(sv: Msv, ep: String, a: String, b: String): String {
    TODO("stub: owner libmpscpp.cpp::Cosine (roster 85, batch 3)")
}
// Sim1 service face - RELEASED at the roster-198 render
// (Sim1.kt: sim1FaceZb, the non-unit :10431 wrapper). The old
// stub pin said "roster 208" - that number was THIS file's
// vendor :208 FACE-ARM LINE, not a roster unit; the corpus
// Sim1 row is unit 198 (rank 187). The call site below calls
// sim1FaceZb direct.
private fun jaccardSvcZb(sv: Msv, ep: String, a: String, b: String): String {
    TODO("stub: owner libmpscpp.cpp::Jaccard (roster 255, batch 9)")
}
private fun centroidSvcZb(sv: Msv, ep: String, a: String, b: String): String {
    TODO("stub: owner libmpscpp.cpp::global::Centroid (roster 210, this batch)")
}
private fun termCorrelateSvcZb(sv: Msv, ep: String, a: String, b: String): String {
    TODO("stub: owner libmpscpp.cpp::global::TermCorrelate (roster 36, batch 1)")
}
private fun docCorrelateSvcZb(
    sv: Msv, ep: String, a: String, b: String, fcn: String, threshold: String
): String {
    TODO("stub: owner libmpscpp.cpp::global::DocCorrelate (roster 287, batch 10)")
}
private fun idfSvcZb(sv: Msv, ep: String, a: String, b: String): String {
    TODO("stub: owner libmpscpp.cpp::global::IDF (roster 45, batch 2)")
}
private fun countSvcZb(sv: Msv, ep: String, a: String): String {
    TODO("stub: owner libmpscpp.cpp::Count (roster 120, batch 4)")
}
private fun maxSvcZb(sv: Msv, ep: String, a: String): String {
    TODO("stub: owner libmpscpp.cpp::Max (roster 68, batch 3)")
}
private fun minSvcZb(sv: Msv, ep: String, a: String): String {
    TODO("stub: owner libmpscpp.cpp::Min (roster 69, batch 3)")
}
private fun diceSvcZb(sv: Msv, ep: String, a: String, b: String): String {
    TODO("stub: owner libmpscpp.cpp::Dice service face (roster 158, batch 6; the B6 render seats the GlobalRef member face)")
}

// void ServiceInterface(...) - interp-service.cpp roster 186. Vendor
// signature (tmp0, tmp1, tmp4, tmp5, tmp3, tok, svPtrX); the b6 zz
// wrappers call it positionally.
internal fun ServiceInterface(
    tmp0: CharArray, tmp1: CharArray, tmp4: CharArray?, tmp5: CharArray?,
    tmp3: CharArray, tok: Int, svPtrX: Msv
): Int {
    // int i, j; int k=0, oflg=0 - vendor declarations; j, k, oflg
    // are never read (kept on the record in this comment).
    val tmpA = CharArray(STR_MAX)
    val tmpB = CharArray(STR_MAX)
    val tmpC = CharArray(STR_MAX)

    tmpA[0] = '\u0000'
    tmpB[0] = '\u0000'

    if (tok == ZC_GETENV) {
        Getenv(tmp3, svcStrZb(tmp0)) // Getenv(tmp3, tmp0)
        return 0
    }

    if (tok == ZC_SCANALNUM) {
        // strmove(tmp3, ScanAlnum(svPtrX, ""))
        writeC(tmp3, scanAlnumSvcZb(svPtrX, ""))
        return 0
    }

    if (tok == ZC_SCAN) {
        // strmove(tmp3, Scan(svPtrX))
        writeC(tmp3, scanSvcZb(svPtrX))
        return 0
    }

    if (tok == ZC_INPUT) {
        // strmove(tmp3, Input(svPtrX, "", tmp0))
        writeC(tmp3, inputSvcZb(svPtrX, "", svcStrZb(tmp0)))
        return 0
    }

    val i0 = svPtrX.LineNumber // int i = svPtrX->LineNumber
    val svPtr = allocSV() // push statevector (stub-blocked, owner 285)
    svPtr.LineNumber = i0
    // memcpy(svPtr, svPtrX, sizeof(struct MSV)) - shallow fold per
    // the Qlength carrier precedent: re-bind the donor buffers onto
    // the pushed carrier; every arm below writes before any read.
    // The vendor comments itself "inefficient" on this copy. The
    // svcEvalZb ladder arms also tuck the LineNumber the vendor
    // pre-sets here (subsumed by the re-bind, kept in ordering).
    svcBindSvZb(svPtr, svPtrX)

    if (tok == ZC_COSINE) {
        svcEvalZb(svPtr, tmp0)
        svcLoadV1dZb(svPtr, tmpA)
        svcEvalZb(svPtr, tmp1)
        svcLoadV1dZb(svPtr, tmpB)
        // strmove(tmp3, Cosine(svPtr, "", tmpA, tmpB))
        writeC(tmp3, cosineSvcZb(svPtr, "", svcStrZb(tmpA), svcStrZb(tmpB)))
    } else if (tok == ZC_SIM1) {
        svcEvalZb(svPtr, tmp0)
        svcLoadV1dZb(svPtr, tmpA)
        svcEvalZb(svPtr, tmp1)
        svcLoadV1dZb(svPtr, tmpB)
        // strmove(tmp3, Sim1(svPtr, "", tmpA, tmpB))
        writeC(tmp3, sim1FaceZb(svPtr, "", svcStrZb(tmpA), svcStrZb(tmpB)))
    } else if (tok == ZC_JACCARD) {
        svcEvalZb(svPtr, tmp0)
        svcLoadV1dZb(svPtr, tmpA)
        svcEvalZb(svPtr, tmp1)
        svcLoadV1dZb(svPtr, tmpB)
        // strmove(tmp3, Jaccard(svPtr, "", tmpA, tmpB))
        writeC(tmp3, jaccardSvcZb(svPtr, "", svcStrZb(tmpA), svcStrZb(tmpB)))
    } else if (tok == ZC_TRANSPOSE) {
        svcEvalZb(svPtr, tmp0)
        svcLoadV1dZb(svPtr, tmpA)
        svcEvalZb(svPtr, tmp1)
        svcLoadV1dZb(svPtr, tmpB)
        // strmove(tmp3, Transpose(svPtr, "", tmpA, tmpB)) - rendered
        // owner (roster 136 seat, unit-207 batch-7 render pending)
        writeC(tmp3, Transpose(svPtr, "", svcStrZb(tmpA), svcStrZb(tmpB)))
    } else if (tok == ZC_MMULTIPLY) {
        svcEvalZb(svPtr, tmp0)
        svcLoadV1dZb(svPtr, tmpA)
        svcEvalZb(svPtr, tmp1)
        svcLoadV1dZb(svPtr, tmpB)
        svcEvalZb(svPtr, tmp4!!)
        svcLoadV1dZb(svPtr, tmpC)
        // strmove(tmp3, Multiply(svPtr, "", tmpA, tmpB, tmpC)) -
        // rendered owner (the b6 MMULTIPLY row seat)
        writeC(tmp3, Multiply(svPtr, "", svcStrZb(tmpA), svcStrZb(tmpB), svcStrZb(tmpC)))
    } else if (tok == ZC_CENTROID) {
        svcEvalZb(svPtr, tmp0)
        svcLoadV1dZb(svPtr, tmpA)
        svcEvalZb(svPtr, tmp1)
        svcLoadV1dZb(svPtr, tmpB)
        // strmove(tmp3, Centroid(svPtr, "", tmpA, tmpB))
        writeC(tmp3, centroidSvcZb(svPtr, "", svcStrZb(tmpA), svcStrZb(tmpB)))
    } else if (tok == ZC_TERMCORRELATE) {
        svcEvalZb(svPtr, tmp0)
        svcLoadV1dZb(svPtr, tmpA)
        svcEvalZb(svPtr, tmp1)
        svcLoadV1dZb(svPtr, tmpB)
        // strmove(tmp3, TermCorrelate(svPtr, "", tmpA, tmpB))
        writeC(tmp3, termCorrelateSvcZb(svPtr, "", svcStrZb(tmpA), svcStrZb(tmpB)))
    } else if (tok == ZC_DOCCORRELATE) {
        svcEvalZb(svPtr, tmp0)
        svcLoadV1dZb(svPtr, tmpA)
        svcEvalZb(svPtr, tmp1)
        svcLoadV1dZb(svPtr, tmpB)
        // strmove(tmp3, DocCorrelate(svPtr, "", tmpA, tmpB, tmp4, tmp5))
        writeC(tmp3, docCorrelateSvcZb(svPtr, "", svcStrZb(tmpA), svcStrZb(tmpB), svcStrZb(tmp4!!), svcStrZb(tmp5!!)))
    } else if (tok == ZC_INVDF) {
        svcEvalZb(svPtr, tmp0)
        svcLoadV1dZb(svPtr, tmpA)
        // tmpA[strlen(tmpA)+1]=0 - the vendor double-NUL wart kept
        // byte-faithful (the strlen tail sits inside svcStrLen)
        svcDoubleNulZb(tmpA)
        // strcpy(tmpB, tmp1)
        svcCpyZb(tmpB, tmp1)
        // strmove(tmp3, IDF(svPtr, "", tmpA, tmpB))
        writeC(tmp3, idfSvcZb(svPtr, "", svcStrZb(tmpA), svcStrZb(tmpB)))
    } else if (tok == ZC_AVG) {
        svcEvalZb(svPtr, tmp0)
        svcLoadV1dZb(svPtr, tmpA)
        // strmove(tmp3, Avg(svPtr, "", tmpA)) - rendered owner
        writeC(tmp3, Avg(svPtr, "", svcStrZb(tmpA)))
    } else if (tok == SVC_GBLNME) {
        // vendor ladder WITHOUT keyfix (kept inline for that reason)
        svPtr.t0px = 1
        svPtr.t2 = 1
        svcCpyZb(svPtr.xd, tmp0)
        svPtr.xpx = 0
        parse_(svPtr)
        // strcpy(tmp3, &svPtr->v1d[1])
        svcLoadV1dZb(svPtr, tmp3)
        // unsigned char rslt[STR_MAX]; int f = Mglobal(GBLNME, ...)
        val rslt = CharArray(STR_MAX)
        Mglobal(SVC_GBLNME, tmp3, rslt, svPtr) // f dead store
        svcCpyZb(tmp3, rslt) // strcpy(tmp3, rslt)
    } else if (tok == ZC_COUNT) {
        svcEvalZb(svPtr, tmp0)
        svcLoadV1dZb(svPtr, tmpA)
        // strmove(tmp3, Count(svPtr, "", tmpA))
        writeC(tmp3, countSvcZb(svPtr, "", svcStrZb(tmpA)))
    } else if (tok == ZC_MAX) {
        svcEvalZb(svPtr, tmp0)
        svcLoadV1dZb(svPtr, tmpA)
        // strmove(tmp3, Max(svPtr, "", tmpA))
        writeC(tmp3, maxSvcZb(svPtr, "", svcStrZb(tmpA)))
    } else if (tok == ZC_MIN) {
        svcEvalZb(svPtr, tmp0)
        svcLoadV1dZb(svPtr, tmpA)
        // strmove(tmp3, Min(svPtr, "", tmpA))
        writeC(tmp3, minSvcZb(svPtr, "", svcStrZb(tmpA)))
    } else if (tok == ZC_SUM) {
        svcEvalZb(svPtr, tmp0)
        svcLoadV1dZb(svPtr, tmpA)
        // strmove(tmp3, Sum(svPtr, "", tmpA)) - rendered owner (the
        // b6 file-level Sum dropped the unused sv/ep pair)
        writeC(tmp3, Sum(svcStrZb(tmpA)))
    } else if (tok == ZC_DICE) {
        svcEvalZb(svPtr, tmp0)
        svcLoadV1dZb(svPtr, tmpA)
        svcEvalZb(svPtr, tmp1)
        svcLoadV1dZb(svPtr, tmpB)
        // strmove(tmp3, Dice(svPtr, "", tmpA, tmpB))
        writeC(tmp3, diceSvcZb(svPtr, "", svcStrZb(tmpA), svcStrZb(tmpB)))
    }

    svPtrX.tpx = svPtr.tpx
    // free(svPtr): GC fold (no JVM face)
    return 0
}

// the INVDF double-NUL wart: tmpA[strlen(tmpA)+1]=0
private fun svcDoubleNulZb(a: CharArray) {
    var i = 0
    while (i < a.size && a[i] != '\u0000') i++
    if (i + 1 < a.size) a[i + 1] = '\u0000'
}

// memcpy(svPtr, svPtrX, sizeof(struct MSV)) face: the Msv buffers
// are val carriers, so the vendor whole-struct copy rides as an
// explicit field copy (scalars by value, buffers content-copy, the
// Stab frame table and the FILE-handle rows by reference). Field
// set and order per the unit-172 stateVector residual universe.
// Visibility widened private->internal at the roster-193 render
// (Qsub's memcpy(svPtr,svPtrX,sizeof(struct MSV)) is the same
// whole-struct copy - ONE field universe, TWO consumers; the
// widen is journaled there).
internal fun svcBindSvZb(dst: Msv, src: Msv) {
    dst.tpx = src.tpx
    dst._Sym = src._Sym
    dst.LineNumber = src.LineNumber
    src.start.copyInto(dst.start)
    src.bd.copyInto(dst.bd)
    src.v1d.copyInto(dst.v1d)
    src.xd.copyInto(dst.xd)
    src.program.copyInto(dst.program)
    dst.t0px = src.t0px
    dst.xpx = src.xpx
    dst.t2 = src.t2
    src.cfgdata.copyInto(dst.cfgdata)
    src.cfgkey.copyInto(dst.cfgkey)
    src.pd1.copyInto(dst.pd1)
    src.setname.copyInto(dst.setname)
    dst.LockDone = src.LockDone
    dst.kflg = src.kflg
    dst.io = src.io
    dst.ierr = src.ierr
    dst.sdlim = src.sdlim
    dst.symflg = src.symflg
    dst.ITRAP = src.ITRAP
    dst.setpiece = src.setpiece
    dst.ctrlc = src.ctrlc
    dst.iorg = src.iorg
    dst.symlen = src.symlen
    dst.NOERR = src.NOERR
    src.in_file.copyInto(dst.in_file)
    src.out_file.copyInto(dst.out_file)
}
