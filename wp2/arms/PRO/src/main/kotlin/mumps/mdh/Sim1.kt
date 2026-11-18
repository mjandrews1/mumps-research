package mumps.mdh

import mumps.interp.NUL
import mumps.interp.mglobal
import mumps.interp.strlenC
import mumps.interp.strcpyC
import mumps.interp.writeCstring

/**
 * PRO-arm translation of libmpscpp.cpp::Sim1 (B7 tranche 2,
 * unit 199, rank 187). Translated independently from the
 * pristine vendor source (mumps-14-may-2026.tgz, sha256
 * 621deb25...ffdbe), reference source lines 1565-1621.
 *
 * Identity verified against the study tool:
 * libmpscpp.cpp::Sim1, cpp, loc 40, comment_ratio 0.024,
 * pointer_marks 3, control_marks 9, score 24.2 - exact match
 * to wp1/study-subset/units.csv row 199 (batch 7). Overload
 * disambiguation: the class-global double Sim1(global,global)
 * (loc 40) IS this unit; the MSV char* wrapper Sim1(svptr, ep,
 * arg1, arg2) (vendor 10431-10495) rides
 * LibmpscppMisc9.kt:427 and binds unchanged (Dice unit-165
 * twin precedent). RESOLVES the MdhStubs Sim1(Global,Global)
 * stub (units.csv row 199 owner tag) - stub removed.
 *
 * Sim1(A, B): the inner product of two term-weight globals
 * (friend of class global, Cosine unit-71 family, minus the
 * cosine's second walk and the length normalization): one
 * A-walk (XNEXT over A.ref), and for each A child the paired
 * B value (B.ref restored from ctmp, suffixed with the child
 * key) contributes tot += val*val1 - returns tot.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h007 char_buffer        : ttt/ctmp/btmp/tmp/tmp1/rslt are
 *    STR_MAX C-string buffers (Cosine/Dice precedent).
 *  - h012 unsigned_byte_domain: all buffers Char(0..255)
 *    domains; the SOH sentinel tests compare against Char(1).
 *  - h014 out_of_scope_call : Mglobal (mglobal InterpStubs
 *    seat) with XNEXT=8 (defines.h:88); get_global (MdhStubs
 *    stub, Cosine precedent); B.Data() rides the GlobalRef
 *    Data stub on an unsubscripted reference (Kill unit-14
 *    precedent; Dice unit-165 extension); strtod -> per-file
 *    strtodC prefix parse; strncmp/strcat -> per-file helpers
 *    (Dice file-private precedent).
 *  - h015 grammar_collision : pre-push scan incl. cross-file
 *    top-level names: the class-global Sim1(Global,Global)
 *    replaces the removed MdhStubs stub one-for-one and is a
 *    distinct overload from the MSV wrapper's Sim1(Msv,
 *    CharArray?, CharArray, CharArray) - no collisions.
 *  - h017 naming           : Sim1 keeps the vendor name. The
 *    vendor mutates B.ref in place (strcpy/strcat on the
 *    global's ref buffer); the carrier's ref is the immutable
 *    name (unit-14 shape), so the port keeps a local refB
 *    buffer with the same mutate flow (Cosine unit-71 note).
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The SOH-pair strip (trailing Char(1) pair from the
 *    MakeGlobal x01 separator) runs on B first, then A - kept.
 *  - The btmp save (strcpy(btmp,B.ref) "in case this and B are
 *    the same") is kept: btmp carries the paired reference
 *    into the second get_global.
 *  - The B.Data() skip (continue) leaves tot untouched for
 *    missing B children - kept.
 *  - The strncmp(A.ref,tmp,i) walk-end break runs on the
 *    ORIGINAL A.ref (i from the pre-strip strlen, re-applied
 *    after the A strip like Dice) - kept.
 *  - No totD/totQ, no division: Sim1 returns the raw inner
 *    product tot (unlike Dice/Cosine) - kept.
 */
private const val STR_MAX = 32000 // vendor strmax.h:3 (file-private, cf. unit 22)
private const val XNEXT = 8 // vendor defines.h:88
private const val GLOBAL_NOT_FOUND = 2 // vendor libmpscpp.h:71

private val SOH = 1.toChar() // the vendor's x01 separator byte

fun Sim1(A: Global, B: Global): Double {
    val tmp = CharArray(STR_MAX)
    val ctmp = CharArray(STR_MAX)
    val btmp = CharArray(STR_MAX)
    val ttt = CharArray(STR_MAX)
    val tmp1 = CharArray(STR_MAX)
    val rslt = CharArray(STR_MAX)
    val refA = CharArray(STR_MAX)
    val refB = CharArray(STR_MAX) // local stand-in for B.ref

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

        strcpyC(refB, ctmp) // strcpy(B.ref,ctmp)
        strcatOff(refB, tmp, i) // strcat(B.ref,&tmp[i])
        strcpyC(btmp, refB) // strcpy(btmp,B.ref)

        if (GlobalRef(B, emptyList()).Data() == 0.0) continue // !B.Data()

        strcpyC(tmp1, tmp) // strcpy(tmp1,tmp)
        get_global(tmp1, rslt)

        if (MdhState.svPtr.tpx == 0L) {
            MdhState.globalExceptionCode = GLOBAL_NOT_FOUND
            throw MumpsGlobalException()
        }

        val v = strtodC(rslt) // val=strtod(rslt,NULL)

        get_global(btmp, rslt)

        if (MdhState.svPtr.tpx == 0L) {
            MdhState.globalExceptionCode = GLOBAL_NOT_FOUND
            throw MumpsGlobalException()
        }

        val v1 = strtodC(rslt) // val1=strtod(rslt,NULL)

        tot = tot + v * v1
    }

    return tot
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
    return if (s.isEmpty()) 0.0 else s.toString().toDouble()
}
