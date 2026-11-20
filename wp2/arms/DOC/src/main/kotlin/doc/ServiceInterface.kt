package doc

// Unit 215 (B7 tranche 3): interp-service.cpp::ServiceInterface
// (batch 7, complex stratum; units.csv row 215: loc 279, score
// 403.7) - this render RESOLVES the Stubs.ServiceInterface seat
// (raised by the unit-144 sym.h misc chunk; the zglobal GBLNME
// 20 relay of Zfcn.kt binds unchanged; the seat's (a..e, f, sv)
// carrier order IS the vendor tmp0/tmp1/tmp4/tmp5/tmp3/tok
// order). The service entry: the early-return token arms
// (GETENV 18 through Getenv out=in order flip note: the vendor
// passes out=tmp3 result, in=tmp0 name; SCANALNUM 5 and SCAN 6
// through the unit-77/75 renders; INPUT 19 through the unit-48
// Input render, the tmp0 name argument read as cstring), then
// the state-vector push (AllocSV the batch-8 H-014 seat;
// the unit-119 copyMsv shape DUPLICATED per the vendor verbatim
// memcpy repetition, u141 dup-def ledger, H-013 family), then
// the parse-and-keyfix prologue repeated VERBATIM per token arm
// (t0px=1, t2=1, xd strcpy, xpx=0, parse_ through the unit-93
// render, keyfix at the &v1d[2] offset-2 slice, the tmpA/tmpB
// captures from the &v1d[1] offset-1 tail) - the repetition is
// the vendor's own copy-paste, kept per the u141 precedent; each
// arm then hands the parsed carriers to the MSV-intrinsic
// wrapper battery of the unit-120 SymOpsMisc render (Cosine,
// Sim1, Jaccard, TermCorrelate, DocCorrelate, Centroid, Dice)
// and the unit-46/75-family renders (Msv Multiply three-array,
// TransposeMsv, Avg, Count, Max, Min, Sum), with strmove folding
// to strcpyS String writes (the unit-144 strmove has no roster
// owner; the CstringOps.cstring read is the contract). The
// DocCorrelate tmp4/tmp5 nesting carriers are nullable in the
// seat (the zcn NULL x3/x4 callers, unit-183 ledger): the
// DOCCORRELATE arm dereferences both in the vendor, so the
// render raises the missing-array case (the T2 RsltCallback
// name0 backstop-binding precedent). IDF is the free
// libmpscpp.cpp:10370 wrapper, an unrendered misc-chunk body -
// the Stubs.IDF seat is raised here (H-014, owner named, the
// batch-10 chunk). The INVDF arm keeps the vendor quirk
// verbatim: tmpA[strlen(tmpA)+1]=0 writes the second NUL one
// PAST the terminator (the strlen+1 index quirk ledgered, not
// fixed), and tmpB takes tmp1 UNPARSED (no keyfix on that side).
// The GBLNME arm skips the keyfix and drives the Mglobal
// GBLNME walk with the rslt fresh scratch and the tmp3 write-
// back; f is the dead capture (H-013, assigned never read). The
// trailing svPtrX->tpx = svPtr->tpx state hand-off and free(svPtr)
// (JVM GC no-op ledger) kept verbatim. H-013: k/oflg are declared
// and never used in the vendor - dead locals kept as the zero
// seats. H-010: tok is the Char carrier of the vendor char (the
// defines.h:226-245 token ladder, 1..20, const list below); i
// re-based per arm; H-015: the token ids ride the defines.h
// ladder (MAX/MIN etc. are C macros there, not Kotlin keywords).
// Naming per H-017 (ServiceInterface verbatim).

// defines.h:226-245 token ladder (vendor values)
private const val GETENV_SI = 18
private const val SCANALNUM_SI = 5
private const val SCAN_SI = 6
private const val INPUT_SI = 19
private const val COSINE_SI = 1
private const val SIM1_SI = 8
private const val JACCARD_SI = 9
private const val TRANSPOSE_SI = 2
private const val MMULTIPLY_SI = 15
private const val CENTROID_SI = 10
private const val TERMCORRELATE_SI = 11
private const val DOCCORRELATE_SI = 17
private const val INVDF_SI = 3
private const val AVG_SI = 7
private const val GBLNME_SI = 20
private const val COUNT_SI = 12
private const val MAX_SI = 13
private const val MIN_SI = 14
private const val SUM_SI = 16
private const val DICE_SI = 4

fun ServiceInterface(tmp0: CharArray, tmp1: CharArray, tmp4: CharArray?,
                     tmp5: CharArray?, tmp3: CharArray, tok: Char,
                     svPtrX: Msv) {
    var i: Int
    val k = 0 // H-013 dead local (declared, never used)
    val oflg = 0 // H-013 dead local (declared, never used)
    val tmpA = CharArray(STR_MAX)
    val tmpB = CharArray(STR_MAX)
    val tmpC = CharArray(STR_MAX)

    tmpA[0] = NUL
    tmpB[0] = NUL

    if (tok == GETENV_SI.toChar()) {
        Getenv(tmp3, tmp0)
        return
    }

    if (tok == SCANALNUM_SI.toChar()) {
        // ScanAlnum(svPtrX, "") - the ep dead-param seat (H-013):
        // the fresh NUL CharArray(1) carrier.
        CstringOps.strcpyS(tmp3, ScanAlnum(svPtrX, CharArray(1), null))
        return
    }

    if (tok == SCAN_SI.toChar()) {
        CstringOps.strcpyS(tmp3, Scan(svPtrX))
        return
    }

    if (tok == INPUT_SI.toChar()) {
        CstringOps.strcpyS(tmp3, Input(svPtrX, "", CstringOps.cstring(tmp0)))
        return
    }

    i = svPtrX.LineNumber.toInt()
    val svPtr = Stubs.AllocSV() /* push statevector */
    svPtr.LineNumber = i.toLong()

    // memcpy(svPtr,svPtrX,sizeof(struct MSV)); /* copy old state
    // vector - inefficient */ - unit-119 copyMsv shape, duplicated
    // per the vendor verbatim body repetition (dup ledger).
    copyMsvSI(svPtr, svPtrX)
    svPtr.LineNumber = i.toLong()
    if (tok == COSINE_SI.toChar()) {
            svPtr.t0px = 1
            svPtr.t2 = 1
            CstringOps.strcpyC(svPtr.xd, tmp0)
            svPtr.xpx = 0
            i = Stubs.parse_(svPtr)
            // keyfix(&svPtr->v1d[2]) - the offset-2 slice.
            Stubs.keyfix(svPtr.v1d.copyOfRange(2, svPtr.v1d.size))
            // strcpy(tmpA, &svPtr->v1d[1]) - the offset-1 tail.
            CstringOps.copyAt(tmpA, 0, svPtr.v1d.copyOfRange(1, svPtr.v1d.size))

            svPtr.t0px = 1
            svPtr.t2 = 1
            CstringOps.strcpyC(svPtr.xd, tmp1)
            svPtr.xpx = 0
            i = Stubs.parse_(svPtr)
            // keyfix(&svPtr->v1d[2]) - the offset-2 slice.
            Stubs.keyfix(svPtr.v1d.copyOfRange(2, svPtr.v1d.size))
            // strcpy(tmpB, &svPtr->v1d[1]) - the offset-1 tail.
            CstringOps.copyAt(tmpB, 0, svPtr.v1d.copyOfRange(1, svPtr.v1d.size))

            CstringOps.strcpyS(tmp3, Cosine(svPtr, "", tmpA, tmpB))
    }

    else if (tok == SIM1_SI.toChar()) {
            svPtr.t0px = 1
            svPtr.t2 = 1
            CstringOps.strcpyC(svPtr.xd, tmp0)
            svPtr.xpx = 0
            i = Stubs.parse_(svPtr)
            // keyfix(&svPtr->v1d[2]) - the offset-2 slice.
            Stubs.keyfix(svPtr.v1d.copyOfRange(2, svPtr.v1d.size))
            // strcpy(tmpA, &svPtr->v1d[1]) - the offset-1 tail.
            CstringOps.copyAt(tmpA, 0, svPtr.v1d.copyOfRange(1, svPtr.v1d.size))

            svPtr.t0px = 1
            svPtr.t2 = 1
            CstringOps.strcpyC(svPtr.xd, tmp1)
            svPtr.xpx = 0
            i = Stubs.parse_(svPtr)
            // keyfix(&svPtr->v1d[2]) - the offset-2 slice.
            Stubs.keyfix(svPtr.v1d.copyOfRange(2, svPtr.v1d.size))
            // strcpy(tmpB, &svPtr->v1d[1]) - the offset-1 tail.
            CstringOps.copyAt(tmpB, 0, svPtr.v1d.copyOfRange(1, svPtr.v1d.size))

            CstringOps.strcpyS(tmp3, Sim1(svPtr, "", tmpA, tmpB))
    }

    else if (tok == JACCARD_SI.toChar()) {
            svPtr.t0px = 1
            svPtr.t2 = 1
            CstringOps.strcpyC(svPtr.xd, tmp0)
            svPtr.xpx = 0
            i = Stubs.parse_(svPtr)
            // keyfix(&svPtr->v1d[2]) - the offset-2 slice.
            Stubs.keyfix(svPtr.v1d.copyOfRange(2, svPtr.v1d.size))
            // strcpy(tmpA, &svPtr->v1d[1]) - the offset-1 tail.
            CstringOps.copyAt(tmpA, 0, svPtr.v1d.copyOfRange(1, svPtr.v1d.size))

            svPtr.t0px = 1
            svPtr.t2 = 1
            CstringOps.strcpyC(svPtr.xd, tmp1)
            svPtr.xpx = 0
            i = Stubs.parse_(svPtr)
            // keyfix(&svPtr->v1d[2]) - the offset-2 slice.
            Stubs.keyfix(svPtr.v1d.copyOfRange(2, svPtr.v1d.size))
            // strcpy(tmpB, &svPtr->v1d[1]) - the offset-1 tail.
            CstringOps.copyAt(tmpB, 0, svPtr.v1d.copyOfRange(1, svPtr.v1d.size))

            CstringOps.strcpyS(tmp3, Jaccard(svPtr, "", tmpA, tmpB))
    }

    else if (tok == TRANSPOSE_SI.toChar()) {
            svPtr.t0px = 1
            svPtr.t2 = 1
            CstringOps.strcpyC(svPtr.xd, tmp0)
            svPtr.xpx = 0
            i = Stubs.parse_(svPtr)
            // keyfix(&svPtr->v1d[2]) - the offset-2 slice.
            Stubs.keyfix(svPtr.v1d.copyOfRange(2, svPtr.v1d.size))
            // strcpy(tmpA, &svPtr->v1d[1]) - the offset-1 tail.
            CstringOps.copyAt(tmpA, 0, svPtr.v1d.copyOfRange(1, svPtr.v1d.size))

            svPtr.t0px = 1
            svPtr.t2 = 1
            CstringOps.strcpyC(svPtr.xd, tmp1)
            svPtr.xpx = 0
            i = Stubs.parse_(svPtr)
            // keyfix(&svPtr->v1d[2]) - the offset-2 slice.
            Stubs.keyfix(svPtr.v1d.copyOfRange(2, svPtr.v1d.size))
            // strcpy(tmpB, &svPtr->v1d[1]) - the offset-1 tail.
            CstringOps.copyAt(tmpB, 0, svPtr.v1d.copyOfRange(1, svPtr.v1d.size))

            // Transpose(svPtr, "", tmpA, tmpB) - the Msv twin's ep
            // is the dead CharArray seat (H-013; fresh NUL carrier).
            CstringOps.strcpyS(tmp3, Transpose(svPtr, CharArray(1), tmpA, tmpB))
    }

    else if (tok == MMULTIPLY_SI.toChar()) {
            svPtr.t0px = 1
            svPtr.t2 = 1
            CstringOps.strcpyC(svPtr.xd, tmp0)
            svPtr.xpx = 0
            i = Stubs.parse_(svPtr)
            // keyfix(&svPtr->v1d[2]) - the offset-2 slice.
            Stubs.keyfix(svPtr.v1d.copyOfRange(2, svPtr.v1d.size))
            // strcpy(tmpA, &svPtr->v1d[1]) - the offset-1 tail.
            CstringOps.copyAt(tmpA, 0, svPtr.v1d.copyOfRange(1, svPtr.v1d.size))

            svPtr.t0px = 1
            svPtr.t2 = 1
            CstringOps.strcpyC(svPtr.xd, tmp1)
            svPtr.xpx = 0
            i = Stubs.parse_(svPtr)
            // keyfix(&svPtr->v1d[2]) - the offset-2 slice.
            Stubs.keyfix(svPtr.v1d.copyOfRange(2, svPtr.v1d.size))
            // strcpy(tmpB, &svPtr->v1d[1]) - the offset-1 tail.
            CstringOps.copyAt(tmpB, 0, svPtr.v1d.copyOfRange(1, svPtr.v1d.size))

            svPtr.t0px = 1
            svPtr.t2 = 1
            CstringOps.strcpyC(svPtr.xd, tmp4 ?: throw IllegalStateException(
                "ServiceInterface: MMULTIPLY requires tmp4"))
            svPtr.xpx = 0
            i = Stubs.parse_(svPtr)
            // keyfix(&svPtr->v1d[2]) - the offset-2 slice.
            Stubs.keyfix(svPtr.v1d.copyOfRange(2, svPtr.v1d.size))
            // strcpy(tmpC, &svPtr->v1d[1]) - the offset-1 tail.
            CstringOps.copyAt(tmpC, 0, svPtr.v1d.copyOfRange(1, svPtr.v1d.size))

            // Multiply(svPtr, "", tmpA, tmpB, tmpC) - the unit-46
            // ep is the dead CharArray seat (H-013).
            CstringOps.strcpyS(tmp3, Multiply(svPtr, CharArray(1), tmpA, tmpB, tmpC))
    }

    else if (tok == CENTROID_SI.toChar()) {
            svPtr.t0px = 1
            svPtr.t2 = 1
            CstringOps.strcpyC(svPtr.xd, tmp0)
            svPtr.xpx = 0
            i = Stubs.parse_(svPtr)
            // keyfix(&svPtr->v1d[2]) - the offset-2 slice.
            Stubs.keyfix(svPtr.v1d.copyOfRange(2, svPtr.v1d.size))
            // strcpy(tmpA, &svPtr->v1d[1]) - the offset-1 tail.
            CstringOps.copyAt(tmpA, 0, svPtr.v1d.copyOfRange(1, svPtr.v1d.size))

            svPtr.t0px = 1
            svPtr.t2 = 1
            CstringOps.strcpyC(svPtr.xd, tmp1)
            svPtr.xpx = 0
            i = Stubs.parse_(svPtr)
            // keyfix(&svPtr->v1d[2]) - the offset-2 slice.
            Stubs.keyfix(svPtr.v1d.copyOfRange(2, svPtr.v1d.size))
            // strcpy(tmpB, &svPtr->v1d[1]) - the offset-1 tail.
            CstringOps.copyAt(tmpB, 0, svPtr.v1d.copyOfRange(1, svPtr.v1d.size))

            CstringOps.strcpyS(tmp3, Centroid(svPtr, "", tmpA, tmpB))
    }

    else if (tok == TERMCORRELATE_SI.toChar()) {
            svPtr.t0px = 1
            svPtr.t2 = 1
            CstringOps.strcpyC(svPtr.xd, tmp0)
            svPtr.xpx = 0
            i = Stubs.parse_(svPtr)
            // keyfix(&svPtr->v1d[2]) - the offset-2 slice.
            Stubs.keyfix(svPtr.v1d.copyOfRange(2, svPtr.v1d.size))
            // strcpy(tmpA, &svPtr->v1d[1]) - the offset-1 tail.
            CstringOps.copyAt(tmpA, 0, svPtr.v1d.copyOfRange(1, svPtr.v1d.size))

            svPtr.t0px = 1
            svPtr.t2 = 1
            CstringOps.strcpyC(svPtr.xd, tmp1)
            svPtr.xpx = 0
            i = Stubs.parse_(svPtr)
            // keyfix(&svPtr->v1d[2]) - the offset-2 slice.
            Stubs.keyfix(svPtr.v1d.copyOfRange(2, svPtr.v1d.size))
            // strcpy(tmpB, &svPtr->v1d[1]) - the offset-1 tail.
            CstringOps.copyAt(tmpB, 0, svPtr.v1d.copyOfRange(1, svPtr.v1d.size))

            CstringOps.strcpyS(tmp3, TermCorrelate(svPtr, "", tmpA, tmpB))
    }

    else if (tok == DOCCORRELATE_SI.toChar()) {
            svPtr.t0px = 1
            svPtr.t2 = 1
            CstringOps.strcpyC(svPtr.xd, tmp0)
            svPtr.xpx = 0
            i = Stubs.parse_(svPtr)
            // keyfix(&svPtr->v1d[2]) - the offset-2 slice.
            Stubs.keyfix(svPtr.v1d.copyOfRange(2, svPtr.v1d.size))
            // strcpy(tmpA, &svPtr->v1d[1]) - the offset-1 tail.
            CstringOps.copyAt(tmpA, 0, svPtr.v1d.copyOfRange(1, svPtr.v1d.size))

            svPtr.t0px = 1
            svPtr.t2 = 1
            CstringOps.strcpyC(svPtr.xd, tmp1)
            svPtr.xpx = 0
            i = Stubs.parse_(svPtr)
            // keyfix(&svPtr->v1d[2]) - the offset-2 slice.
            Stubs.keyfix(svPtr.v1d.copyOfRange(2, svPtr.v1d.size))
            // strcpy(tmpB, &svPtr->v1d[1]) - the offset-1 tail.
            CstringOps.copyAt(tmpB, 0, svPtr.v1d.copyOfRange(1, svPtr.v1d.size))

            // tmp4/tmp5 nullable seats: the vendor dereferences both
            // on this arm - the backstop raises the missing case (T2
            // RsltCallback name0 precedent).
            CstringOps.strcpyS(tmp3, DocCorrelate(svPtr, "", tmpA, tmpB,
                tmp4 ?: throw IllegalStateException(
                    "ServiceInterface: DOCCORRELATE requires tmp4"),
                tmp5 ?: throw IllegalStateException(
                    "ServiceInterface: DOCCORRELATE requires tmp5")))
    }

    else if (tok == INVDF_SI.toChar()) {
            svPtr.t0px = 1
            svPtr.t2 = 1
            CstringOps.strcpyC(svPtr.xd, tmp0)
            svPtr.xpx = 0
            i = Stubs.parse_(svPtr)
            // keyfix(&svPtr->v1d[2]) - the offset-2 slice.
            Stubs.keyfix(svPtr.v1d.copyOfRange(2, svPtr.v1d.size))
            // strcpy(tmpA, &svPtr->v1d[1]) - the offset-1 tail.
            CstringOps.copyAt(tmpA, 0, svPtr.v1d.copyOfRange(1, svPtr.v1d.size))

            // tmpA[strlen(tmpA)+1]=0 - the vendor strlen+1 quirk
            // writes a second NUL one PAST the terminator; kept.
            tmpA[CstringOps.strlenC(tmpA) + 1] = NUL

            CstringOps.strcpyC(tmpB, tmp1)

            CstringOps.strcpyS(tmp3, Stubs.IDF(svPtr, "", tmpA, tmpB))
    }

    else if (tok == AVG_SI.toChar()) {
            svPtr.t0px = 1
            svPtr.t2 = 1
            CstringOps.strcpyC(svPtr.xd, tmp0)
            svPtr.xpx = 0
            i = Stubs.parse_(svPtr)
            // keyfix(&svPtr->v1d[2]) - the offset-2 slice.
            Stubs.keyfix(svPtr.v1d.copyOfRange(2, svPtr.v1d.size))
            // strcpy(tmpA, &svPtr->v1d[1]) - the offset-1 tail.
            CstringOps.copyAt(tmpA, 0, svPtr.v1d.copyOfRange(1, svPtr.v1d.size))

            CstringOps.strcpyS(tmp3, Avg(svPtr, "", tmpA))
    }

    else if (tok == GBLNME_SI.toChar()) {

            svPtr.t0px = 1
            svPtr.t2 = 1
            CstringOps.strcpyC(svPtr.xd, tmp0)
            svPtr.xpx = 0
            i = Stubs.parse_(svPtr)
            // no keyfix on this arm (vendor); tmp3 takes the
            // &v1d[1] offset-1 tail directly.
            CstringOps.copyAt(tmp3, 0, svPtr.v1d.copyOfRange(1, svPtr.v1d.size))

            val rslt = CharArray(STR_MAX)
            // int f - H-013 dead capture (assigned, never read).
            val f = Stubs.Mglobal(GBLNME_SI, tmp3, rslt, svPtr)
            CstringOps.strcpyC(tmp3, rslt)
    }

    else if (tok == COUNT_SI.toChar()) {
            svPtr.t0px = 1
            svPtr.t2 = 1
            CstringOps.strcpyC(svPtr.xd, tmp0)
            svPtr.xpx = 0
            i = Stubs.parse_(svPtr)
            // keyfix(&svPtr->v1d[2]) - the offset-2 slice.
            Stubs.keyfix(svPtr.v1d.copyOfRange(2, svPtr.v1d.size))
            // strcpy(tmpA, &svPtr->v1d[1]) - the offset-1 tail.
            CstringOps.copyAt(tmpA, 0, svPtr.v1d.copyOfRange(1, svPtr.v1d.size))

            CstringOps.strcpyS(tmp3, Count(svPtr, "", tmpA))
    }

    else if (tok == MAX_SI.toChar()) {
            svPtr.t0px = 1
            svPtr.t2 = 1
            CstringOps.strcpyC(svPtr.xd, tmp0)
            svPtr.xpx = 0
            i = Stubs.parse_(svPtr)
            // keyfix(&svPtr->v1d[2]) - the offset-2 slice.
            Stubs.keyfix(svPtr.v1d.copyOfRange(2, svPtr.v1d.size))
            // strcpy(tmpA, &svPtr->v1d[1]) - the offset-1 tail.
            CstringOps.copyAt(tmpA, 0, svPtr.v1d.copyOfRange(1, svPtr.v1d.size))

            CstringOps.strcpyS(tmp3, Max(svPtr, "", tmpA))
    }

    else if (tok == MIN_SI.toChar()) {
            svPtr.t0px = 1
            svPtr.t2 = 1
            CstringOps.strcpyC(svPtr.xd, tmp0)
            svPtr.xpx = 0
            i = Stubs.parse_(svPtr)
            // keyfix(&svPtr->v1d[2]) - the offset-2 slice.
            Stubs.keyfix(svPtr.v1d.copyOfRange(2, svPtr.v1d.size))
            // strcpy(tmpA, &svPtr->v1d[1]) - the offset-1 tail.
            CstringOps.copyAt(tmpA, 0, svPtr.v1d.copyOfRange(1, svPtr.v1d.size))

            CstringOps.strcpyS(tmp3, Min(svPtr, "", tmpA))
    }

    else if (tok == SUM_SI.toChar()) {
            svPtr.t0px = 1
            svPtr.t2 = 1
            CstringOps.strcpyC(svPtr.xd, tmp0)
            svPtr.xpx = 0
            i = Stubs.parse_(svPtr)
            // keyfix(&svPtr->v1d[2]) - the offset-2 slice.
            Stubs.keyfix(svPtr.v1d.copyOfRange(2, svPtr.v1d.size))
            // strcpy(tmpA, &svPtr->v1d[1]) - the offset-1 tail.
            CstringOps.copyAt(tmpA, 0, svPtr.v1d.copyOfRange(1, svPtr.v1d.size))

            CstringOps.strcpyS(tmp3, Sum(svPtr, "", tmpA))
    }

    else if (tok == DICE_SI.toChar()) {
            svPtr.t0px = 1
            svPtr.t2 = 1
            CstringOps.strcpyC(svPtr.xd, tmp0)
            svPtr.xpx = 0
            i = Stubs.parse_(svPtr)
            // keyfix(&svPtr->v1d[2]) - the offset-2 slice.
            Stubs.keyfix(svPtr.v1d.copyOfRange(2, svPtr.v1d.size))
            // strcpy(tmpA, &svPtr->v1d[1]) - the offset-1 tail.
            CstringOps.copyAt(tmpA, 0, svPtr.v1d.copyOfRange(1, svPtr.v1d.size))

            svPtr.t0px = 1
            svPtr.t2 = 1
            CstringOps.strcpyC(svPtr.xd, tmp1)
            svPtr.xpx = 0
            i = Stubs.parse_(svPtr)
            // keyfix(&svPtr->v1d[2]) - the offset-2 slice.
            Stubs.keyfix(svPtr.v1d.copyOfRange(2, svPtr.v1d.size))
            // strcpy(tmpB, &svPtr->v1d[1]) - the offset-1 tail.
            CstringOps.copyAt(tmpB, 0, svPtr.v1d.copyOfRange(1, svPtr.v1d.size))

            CstringOps.strcpyS(tmp3, Dice(svPtr, "", tmpA, tmpB))
    }

    svPtrX.tpx = svPtr.tpx

    // free (svPtr); - JVM GC no-op (ledger).
}

// Msv field-copy adapter: the vendor memcpy of the whole state
// vector over the AllocSV copy (the unit-119 private adapter
// duplicated for this file's owner scope per the vendor's
// verbatim body repetition; bounded to the Msv carrier surface).
private fun copyMsvSI(dst: Msv, src: Msv) {
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
