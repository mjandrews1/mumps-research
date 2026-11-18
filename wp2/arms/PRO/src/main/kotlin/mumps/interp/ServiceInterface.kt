package mumps.interp

import mumps.mdh.Avg
import mumps.mdh.Centroid
import mumps.mdh.Cosine
import mumps.mdh.Count
import mumps.mdh.Dice
import mumps.mdh.DocCorrelate
import mumps.mdh.Getenv
import mumps.mdh.IDF
import mumps.mdh.Input
import mumps.mdh.Jaccard
import mumps.mdh.Max
import mumps.mdh.Min
import mumps.mdh.Multiply
import mumps.mdh.Scan
import mumps.mdh.ScanAlnum
import mumps.mdh.Sim1
import mumps.mdh.Sum
import mumps.mdh.TermCorrelate
import mumps.mdh.Transpose

/**
 * PRO-arm translation of interp-service.cpp::ServiceInterface (B7
 * unit 187, units.csv data row 187: loc 347, comment_ratio 0.185,
 * pointer_marks 22, control_marks 26, score 316.5,
 * complexity_rank 299, complex stratum; vendor
 * interp-service.cpp:87-445). RESOLVES the SymHeaderMisc h014
 * ServiceInterface stub (raised by u165, widened CharArray? by
 * u156 - the Zfcn.kt GBLNME-relay call sites at :1228-1352 bind
 * UNCHANGED on the (x1, x1Off, x2, x2Off, x3, x3Off, x4, x4Off,
 * x5, x5Off, c, svPtr) signature; the vendor parameter order is
 * (tmp0, tmp1, tmp4, tmp5, tmp3, tok, svPtrX) = (x4, x5, x1, x2,
 * x3, c) per sym.h:80-81).
 *
 * Vendor shape: the tok dispatcher over the pushed state vector.
 * The four early arms run BEFORE the push (GETENV relays to
 * Getenv, SCANALNUM/SCAN/INPUT relay to the mdh seats); every
 * other arm pushes a state vector (AllocSV + the whole-struct
 * memcpy), then parses its argument references via the
 * t0px=1/t2=1/xd/xpx=0/parse_/keyfix(&v1d[2])/strcpy-from-v1d
 * idiom (one parse per argument; MMULTIPLY parses three,
 * DOCCORRELATE two plus the x4/x5 pass-through), and relays to
 * the mdh service seats; svPtrX->tpx inherits the pushed svPtr
 * tpx, the pushed vector is freed (GC here) on return.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h003 state_macro      : the push/copy/inherit block rides
 *    the unit 98 GlobalQuery seat: allocSV (h014 stub) +
 *    msvCopyInto (u187: EXTENDED here to the full-field copy -
 *    prior seat copied the head fields only; the vendor memcpy
 *    copies the whole struct; all prior call sites bind
 *    unchanged, copied-field superset is observably identical).
 *  - h007 char_buffer      : tmpA/tmpB/tmpC/xd/v1d/rslt are
 *    NUL-terminated byte buffers (h012 domain).
 *  - h009 format_string    : none (no sprintf in this body).
 *  - h012 unsigned_byte_domain: the char-code tok compares ride
 *    defines.h code points (COSINE 1 .. GBLNME 20).
 *  - h013 dead_store       : the GETENV arm's tmp0 read and the
 *    oflg/k locals - k/oflg are the GlobalQuery-inherited
 *    declarations, never assigned in this body; kept as the
 *    vendor's own declarations (roster note).
 *  - h014 out_of_scope_call: Getenv/ScanAlnum/Scan/Input/
 *    Cosine/Sim1/Jaccard/Transpose/Multiply/Centroid/
 *    TermCorrelate/DocCorrelate/IDF/Avg/Count/Max/Min/Sum/Dice -
 *    the mdh seats; IDF is a NEW MdhStubs stub (owner
 *    libmpscpp.cpp::IDF, vendor 10370, units.csv data row 240 -
 *    not yet a corpus unit); parse_ (unit 63 real seat);
 *    keyfix(&v1d[2]) via the copy/write-back keyfixAt adapter
 *    (MdhStubs keyfix takes a whole buffer, GlobalQuery
 *    precedent); Mglobal GBLNME -> mglobal (InterpStubs stub);
 *    allocSV/free per the GlobalQuery seat; strmove -> the
 *    writeCstring fold where the source is a seat String.
 *  - h015 grammar_collision: pre-push scan clean - no backtick,
 *    double-underscore, em dash or !! in this file; the vendor
 *    char codes are file-private consts (Zfcn.kt keeps its own
 *    _C set, per-file private precedent).
 *  - h017 naming           : ServiceInterface keeps the vendor
 *    name; the stub signature binds unchanged.
 *  - h018 reference_build_visibility: SQLITE defined at the
 *    vendor file head (:32) - the GBLNME arm is LIVE in the
 *    reference build.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The INVDF arm's tmpA[strlen+1]=0 double-NUL store (vendor
 *    311) writes one PAST the terminator - kept verbatim (a
 *    second NUL after the string terminator; no reader in this
 *    file distinguishes it).
 *  - The GBLNME arm skips the keyfix (no &v1d[2] call) - kept.
 *  - The early arms return WITHOUT the push and WITHOUT the
 *    svPtrX->tpx inheritance - kept (the vendor's own control
 *    shape).
 */
fun ServiceInterface(x1: CharArray?, x1Off: Int, x2: CharArray?, x2Off: Int,
                     x3: CharArray, x3Off: Int, x4: CharArray, x4Off: Int,
                     x5: CharArray, x5Off: Int, c: Char, svPtrX: Msv) {
    val tmp0: CharArray = x4
    val tmp1: CharArray = x5
    val tmp4: CharArray? = x1
    val tmp5: CharArray? = x2
    val tmp3: CharArray = x3
    val tok = c

    val tmpA = CharArray(STR_MAX)
    val tmpB = CharArray(STR_MAX)
    val tmpC = CharArray(STR_MAX)

    tmpA[0] = NUL
    tmpB[0] = NUL

    if (tok == GETENV_C) {
        Getenv(tmp3, cstringAt(tmp0, x4Off))
        return
    }

    if (tok == SCANALNUM_C) {
        val ep = CharArray(1) // vendor: (char *) ""
        strmove(tmp3, x3Off, ScanAlnum(svPtrX, ep), 0)
        return
    }

    if (tok == SCAN_C) {
        strmove(tmp3, x3Off, Scan(svPtrX), 0)
        return
    }

    if (tok == INPUT_C) {
        val ep = CharArray(1) // vendor: (char *) ""
        writeCstring(tmp3, Input(svPtrX, ep, tmp0), x3Off)
        return
    }

    // vendor: push state vector
    val i0 = svPtrX.LineNumber
    val svPtr = allocSV()
    svPtr.LineNumber = i0
    msvCopyInto(svPtr, svPtrX) // memcpy whole struct

    if (tok == COSINE_C) {
        parseArg(svPtr, tmp0, x4Off, tmpA)
        parseArg(svPtr, tmp1, x5Off, tmpB)
        val ep = CharArray(1) // vendor: (char *) ""
        strmove(tmp3, x3Off, Cosine(svPtr, ep, tmpA, tmpB), 0)
    } else if (tok == SIM1_C) {
        parseArg(svPtr, tmp0, x4Off, tmpA)
        parseArg(svPtr, tmp1, x5Off, tmpB)
        val ep = CharArray(1)
        strmove(tmp3, x3Off, Sim1(svPtr, ep, tmpA, tmpB), 0)
    } else if (tok == JACCARD_C) {
        parseArg(svPtr, tmp0, x4Off, tmpA)
        parseArg(svPtr, tmp1, x5Off, tmpB)
        val ep = CharArray(1)
        strmove(tmp3, x3Off, Jaccard(svPtr, ep, tmpA, tmpB), 0)
    } else if (tok == TRANSPOSE_C) {
        parseArg(svPtr, tmp0, x4Off, tmpA)
        parseArg(svPtr, tmp1, x5Off, tmpB)
        writeCstring(tmp3, Transpose(svPtr, "", tmpA, tmpB), x3Off)
    } else if (tok == MMULTIPLY_C) {
        parseArg(svPtr, tmp0, x4Off, tmpA)
        parseArg(svPtr, tmp1, x5Off, tmpB)
        parseArg(svPtr, tmp4 ?: EMPTY_CARRIER, x1Off, tmpC)
        writeCstring(tmp3, Multiply(svPtr, "", tmpA, tmpB, tmpC), x3Off)
    } else if (tok == CENTROID_C) {
        parseArg(svPtr, tmp0, x4Off, tmpA)
        parseArg(svPtr, tmp1, x5Off, tmpB)
        writeCstring(tmp3, Centroid(svPtr, null, tmpA, tmpB), x3Off)
    } else if (tok == TERMCORRELATE_C) {
        parseArg(svPtr, tmp0, x4Off, tmpA)
        parseArg(svPtr, tmp1, x5Off, tmpB)
        writeCstring(tmp3, TermCorrelate(svPtr, null, tmpA, tmpB), x3Off)
    } else if (tok == DOCCORRELATE_C) {
        parseArg(svPtr, tmp0, x4Off, tmpA)
        parseArg(svPtr, tmp1, x5Off, tmpB)
        writeCstring(
            tmp3,
            DocCorrelate(svPtr, null, tmpA, tmpB, tmp4 ?: EMPTY_CARRIER,
                tmp5 ?: EMPTY_CARRIER),
            x3Off
        )
    } else if (tok == INVDF_C) {
        parseArg(svPtr, tmp0, x4Off, tmpA)
        // vendor: tmpA[strlen(tmpA)+1]=0; - the double-NUL store
        tmpA[strlenC(tmpA) + 1] = NUL
        strncpyC(tmpB, tmp1, x5Off)
        val ep = CharArray(1)
        strmove(tmp3, x3Off, IDF(svPtr, ep, tmpA, tmpB), 0)
    } else if (tok == AVG_C) {
        parseArg(svPtr, tmp0, x4Off, tmpA)
        val ep = CharArray(1)
        strmove(tmp3, x3Off, Avg(svPtr, ep, tmpA), 0)
    } else if (tok == GBLNME_C) {
        svPtr.t0px = 1
        svPtr.t2 = 1
        writeCstring(svPtr.xd, cstringAt(tmp0, x4Off))
        svPtr.xpx = 0
        parse_(svPtr)
        writeCstring(tmp3, cstringAt(svPtr.v1d, 1), x3Off)

        val rslt = CharArray(STR_MAX)
        val f = mglobal(GBLNME, tmp3, rslt, svPtr)
        // vendor: strcpy(tmp3, rslt) - f kept for the roster note
        strncpyC(tmp3, rslt, 0)
    } else if (tok == COUNT_C) {
        parseArg(svPtr, tmp0, x4Off, tmpA)
        val ep = CharArray(1)
        strmove(tmp3, x3Off, Count(svPtr, ep, tmpA), 0)
    } else if (tok == MAX_C) {
        parseArg(svPtr, tmp0, x4Off, tmpA)
        val ep = CharArray(1)
        strmove(tmp3, x3Off, Max(svPtr, ep, tmpA), 0)
    } else if (tok == MIN_C) {
        parseArg(svPtr, tmp0, x4Off, tmpA)
        val ep = CharArray(1)
        strmove(tmp3, x3Off, Min(svPtr, ep, tmpA), 0)
    } else if (tok == SUM_C) {
        parseArg(svPtr, tmp0, x4Off, tmpA)
        val ep = CharArray(1)
        strmove(tmp3, x3Off, Sum(svPtr, ep, tmpA), 0)
    } else if (tok == DICE_C) {
        parseArg(svPtr, tmp0, x4Off, tmpA)
        parseArg(svPtr, tmp1, x5Off, tmpB)
        val ep = CharArray(1)
        strmove(tmp3, x3Off, Dice(svPtr, ep, tmpA, tmpB), 0)
    }

    svPtrX.tpx = svPtr.tpx

    // vendor: free(svPtr) - GC (GlobalQuery precedent)
    return
}

/**
 * The vendor's per-argument parse idiom (one occurrence per
 * argument in every arm): t0px=1; t2=1; strcpy(xd, arg); xpx=0;
 * i=parse_(svPtr); keyfix(&v1d[2]); strcpy(dst, &v1d[1]).
 */
private fun parseArg(svPtr: Msv, arg: CharArray, argOff: Int, dst: CharArray) {
    svPtr.t0px = 1
    svPtr.t2 = 1
    writeCstring(svPtr.xd, cstringAt(arg, argOff))
    svPtr.xpx = 0
    parse_(svPtr)
    keyfixAt(svPtr.v1d, 2)
    writeCstring(dst, cstringAt(svPtr.v1d, 1))
}

/** keyfix(&v1d[2]) via the copy/write-back adapter (MdhStubs
 * keyfix takes the whole buffer; GlobalQuery precedent). */
private fun keyfixAt(buf: CharArray, off: Int) {
    val key = CharArray(buf.size - off)
    var i = 0
    while (i < key.size) {
        key[i] = buf[off + i]
        i++
    }
    mumps.mdh.keyfix(key)
    i = 0
    while (i < key.size) {
        buf[off + i] = key[i]
        i++
    }
}

/** strncpy(dst, src+off, STR_MAX) - byte-buffer copy incl. NUL. */
private fun strncpyC(dst: CharArray, src: CharArray, srcOff: Int) {
    var i = 0
    while (i < dst.size) {
        dst[i] = src[srcOff + i]
        if (dst[i] == NUL) return
        i++
    }
}

private val EMPTY_CARRIER = CharArray(1) // NULL stand-in (u156 widened arms)

// vendor defines.h:226-245 ServiceInterface char codes (file-private)
private val COSINE_C = 1.toChar()
private val TRANSPOSE_C = 2.toChar()
private val INVDF_C = 3.toChar()
private val DICE_C = 4.toChar()
private val SCANALNUM_C = 5.toChar()
private val SCAN_C = 6.toChar()
private val AVG_C = 7.toChar()
private val SIM1_C = 8.toChar()
private val JACCARD_C = 9.toChar()
private val CENTROID_C = 10.toChar()
private val TERMCORRELATE_C = 11.toChar()
private val COUNT_C = 12.toChar()
private val MAX_C = 13.toChar()
private val MIN_C = 14.toChar()
private val MMULTIPLY_C = 15.toChar()
private val SUM_C = 16.toChar()
private val DOCCORRELATE_C = 17.toChar()
private val GETENV_C = 18.toChar()
private val INPUT_C = 19.toChar()
private val GBLNME_C = 20.toChar()

private const val GBLNME = 20 // vendor defines.h:245 (Mglobal op)

private const val STR_MAX = 32000 // vendor strmax.h:3 (file-private, cf. Fcn.kt)
