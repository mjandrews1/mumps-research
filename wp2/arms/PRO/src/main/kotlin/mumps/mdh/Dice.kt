package mumps.mdh

import mumps.interp.NUL
import mumps.interp.mglobal
import mumps.interp.strlenC
import mumps.interp.strcpyC
import mumps.interp.writeCstring

/**
 * PRO-arm translation of libmpscpp.cpp::Dice (B6 unit 165,
 * rank 194). Translated independently from the pristine vendor
 * source (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe),
 * reference source lines 1312-1373.
 *
 * Identity verified against the study tool
 * (tools/select_study_subset.py): libmpscpp.cpp::Dice, cpp,
 * loc 44, comment_ratio 0.022, pointer_marks 4,
 * control_marks 9, score 26.8 - exact match to
 * wp1/study-subset/units.csv row 166 (batch 6). Overload
 * disambiguation: the class-global double Dice(global,global)
 * (loc 44) IS this unit; the MSV char* wrapper (vendor
 * 10475-10495, loc 14) rides LibmpscppMisc9.kt:495 and binds
 * unchanged. RESOLVES the MdhStubs Dice(Global,Global) stub
 * (raised by u85 LibmpscppMisc9 with the correct batch-6
 * owner tag) - stub removed.
 *
 * Dice(A, B): the Dice coefficient of two term-weight globals
 * (friend of class global, Cosine unit-71 family): one
 * A-walk (XNEXT over A.ref), totD accumulates A's weights,
 * and for each A child the paired B value (B.ref restored
 * from ctmp, suffixed with the child key) contributes
 * totQ and tot += val*val1 - returns 2*tot/(totD+totQ).
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h007 char_buffer        : ttt/ctmp/btmp/tmp/tmp1/rslt are
 *    STR_MAX C-string buffers (Cosine unit-71 precedent).
 *  - h010 numeric_promotion  : none needed (double domain).
 *  - h012 unsigned_byte_domain: all buffers Char(0..255)
 *    domains; the SOH sentinel tests compare against Char(1).
 *  - h014 out_of_scope_call : Mglobal (mglobal InterpStubs
 *    stub) with XNEXT=8 (defines.h:88); get_global (MdhStubs
 *    stub, Cosine precedent); B.Data() rides the GlobalRef
 *    Data stub on an unsubscripted reference (Kill unit-14
 *    precedent); GlobalExceptionCode/throw
 *    MumpsGlobalException -> MdhState (unit 31); strtod ->
 *    per-file strtodC prefix parse; strncmp/strcat -> per-file
 *    helpers (Cosine file-private precedent).
 *  - h015 grammar_collision : pre-push scan incl. cross-file
 *    top-level names: new top-level Dice replaces the removed
 *    stub one-for-one - no collisions.
 *  - h017 naming           : Dice keeps the vendor name. The
 *    vendor mutates B.ref in place (strcpy/strcat on the
 *    global's ref buffer) - the carrier's ref is the immutable
 *    name (unit-14 shape), so the port keeps a local refB
 *    buffer with the same mutate flow (Cosine unit-71 note).
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The SOH-pair strip (trailing Char(1) pair from the
 *    MakeGlobal x01 separator) runs on B first, then A - kept.
 *  - Dice has NO second walk (unlike Cosine): totQ accumulates
 *    only the paired B values inside the A-walk - kept.
 *  - The B.Data() skip (continue) leaves totQ/tot untouched
 *    for missing B children - kept.
 *  - val==0.0 (totD+totQ == 0.0) returns 0.0 - kept.
 *  - The MSV wrapper's Jaccard/Dice sibling text (vendor
 *    10475) mentions no restore here - the vendor never
 *    restores B.ref in Dice (unlike Cosine) - kept: the local
 *    refB mutation is simply re-done each iteration.
 */

private const val STR_MAX = 32000 // vendor strmax.h:3 (file-private, cf. unit 22)
private const val XNEXT = 8 // vendor defines.h:88
private const val GLOBAL_NOT_FOUND = 2 // vendor libmpscpp.h:71

private val SOH = 1.toChar() // the vendor's x01 separator byte

fun Dice(A: Global, B: Global): Double {
    val tmp = CharArray(STR_MAX)
    val ctmp = CharArray(STR_MAX)
    val btmp = CharArray(STR_MAX)
    val ttt = CharArray(STR_MAX)
    val tmp1 = CharArray(STR_MAX)
    val rslt = CharArray(STR_MAX)
    val refA = CharArray(STR_MAX)
    val refB = CharArray(STR_MAX) // local stand-in for B.ref

    var totD = 0.0
    var totQ = 0.0
    var tot = 0.0

    writeCstring(tmp, A.ref) // strcpy(tmp, A.ref)
    writeCstring(refA, A.ref)
    writeCstring(refB, B.ref)
    writeCstring(ctmp, B.ref) // strcpy(ctmp, B.ref)
    var i = strlenC(refA)
    var j = strlenC(refB)

    if (ctmp[j - 2] == SOH && ctmp[j - 1] == SOH) {
        j--
        ctmp[j] = NUL
    }

    if (tmp[i - 2] == SOH && tmp[i - 1] == SOH) {
        i--
        tmp[i] = NUL
    }

    while (true) {
        if (mglobal(XNEXT, tmp, ttt, MdhState.svPtr) == 0) break

        if (strncmpOff(refA, tmp, i) != 0) break // strncmp(A.ref,tmp,i)

        strcpyC(tmp1, tmp) // strcpy(tmp1,tmp)
        get_global(tmp1, rslt)

        if (MdhState.svPtr.tpx == 0L) {
            MdhState.globalExceptionCode = GLOBAL_NOT_FOUND
            throw MumpsGlobalException()
        }

        val v = strtodC(rslt) // val=strtod(rslt,NULL)
        totD = totD + v

        strcpyC(refB, ctmp) // strcpy(B.ref,ctmp)
        strcatOff(refB, tmp, i) // strcat(B.ref,&tmp[i])
        strcpyC(btmp, refB) // strcpy(btmp,B.ref)

        if (GlobalRef(B, emptyList()).Data() == 0.0) continue // !B.Data()

        get_global(btmp, rslt)

        if (MdhState.svPtr.tpx == 0L) {
            MdhState.globalExceptionCode = GLOBAL_NOT_FOUND
            throw MumpsGlobalException()
        }

        val v1 = strtodC(rslt) // val1=strtod(rslt,NULL)
        totQ = totQ + v1

        tot = tot + v * v1
    }

    val valSum = totD + totQ // val=totD+totQ

    if (valSum == 0.0) return 0.0

    return 2 * tot / valSum
}

/** C strncmp(a, b, n) over the byte domains from offset 0. */
private fun strncmpOff(a: CharArray, b: CharArray, n: Int): Int {
    for (k in 0 until n) {
        val ca = a[k]
        val cb = b[k]
        if (ca != cb) return if (ca < cb) -1 else 1
        if (ca == NUL) return 0
    }
    return 0
}

/** C strcat(dst, src+off) - appends the c-string src from offset off. */
private fun strcatOff(dst: CharArray, src: CharArray, off: Int) {
    var e = 0
    while (dst[e] != NUL) e++
    var p = off
    while (src[p] != NUL) {
        dst[e] = src[p]
        e++
        p++
    }
    dst[e] = NUL
}

/** strtod prefix parse over the byte domain (cf. strtodC, unit 10). */
private fun strtodC(b: CharArray): Double {
    val s = StringBuilder()
    var i = 0
    while (b[i] != NUL && b[i] == ' ') i++
    if (b[i] == '+' || b[i] == '-') {
        s.append(b[i])
        i++
    }
    while (b[i] in '0'..'9') {
        s.append(b[i])
        i++
    }
    if (b[i] == '.') {
        s.append(b[i])
        i++
        while (b[i] in '0'..'9') {
            s.append(b[i])
            i++
        }
    }
    if (b[i] == 'e' || b[i] == 'E') {
        s.append(b[i])
        i++
        if (b[i] == '+' || b[i] == '-') {
            s.append(b[i])
            i++
        }
        while (b[i] in '0'..'9') {
            s.append(b[i])
            i++
        }
    }
    return s.toString().toDoubleOrNull() ?: 0.0
}
