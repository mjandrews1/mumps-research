package mumps.mdh

import mumps.interp.NUL
import mumps.interp.mglobal
import mumps.interp.strlenC
import mumps.interp.strcpyC
import mumps.interp.writeCstring
import kotlin.math.sqrt

/**
 * PRO-arm translation of libmpscpp.cpp::Cosine (B3 unit 71, rank
 * 226). Translated independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), reference
 * source lines 2756-2853.
 *
 * Identity verified against the study tool (tools/select_study_
 * subset.py): libmpscpp.cpp::Cosine, cpp, loc 62, comment_ratio
 * 0.016, pointer_marks 7, control_marks 14, score 42.4, batch 3 -
 * exact match to wp1/study-subset/units.csv row 226.
 *
 * Cosine(A, B): the document-cosine similarity of two term-weight
 * globals (friend of class global, declared globalClass.h:436):
 * walks A's children (XNEXT over A.ref), accumulates sum a-a
 * (totD), then B's children (totQ), returning tot / sqrt(totD*totQ).
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h007 char_buffer        : ttt/ctmp/btmp/tmp/tmp1/rslt are
 *    STR_MAX C-string buffers; x01[2] is the vendor's {1,0} pair.
 *  - h010 numeric_promotion  : none needed (double domain).
 *  - h012 unsigned_byte_domain: all buffers Char(0..255) domains;
 *    the SOH sentinel tests compare against Char(1).
 *  - h013 dead_store         : x01[2] = {1,0} is declared and never
 *    used in the body - kept as a comment, not a live allocation.
 *  - h014 out_of_scope_call : Mglobal (mglobal InterpStubs stub,
 *    global-array machinery) with XNEXT=8 (defines.h:88);
 *    get_global (libmpscpp.cpp::get_global, not a corpus unit) ->
 *    MdhStubs stub added by this unit; B.Data() (global::Data,
 *    owner libmpscpp.cpp machinery) rides the existing GlobalRef
 *    Data stub on an unsubscripted reference (Kill precedent,
 *    unit 14); GlobalExceptionCode/throw MumpsGlobalException ->
 *    MdhState (unit 31); strtod -> strtodC prefix parse.
 *  - h015 grammar_collision : pre-push scan incl. cross-file
 *    top-level names: new top-level Cosine - no collisions.
 *  - h017 naming            : Cosine keeps the vendor name. The
 *    vendor mutates B.ref in place (strcpy/strcat on the global's
 *    ref buffer) - the carrier's ref is the immutable name
 *    (unit-14 shape), so the port keeps a local refB buffer with
 *    the same mutate-then-restore flow (values identical).
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The SOH-pair strip (ctmp[j-2]==1 && ctmp[j-1]==1 -> j--,
 *    ctmp[j]=0) trims the vendor's x01 subscript separator pair
 *    that MakeGlobal appends - kept exactly, on both A and B.
 *  - Mglobal(XNEXT,...) advances the key buffer in place (tmp /
 *    ctmp) - the stub preserves that call shape.
 *  - The strncmp(A.ref,tmp,i) prefix test ends the A-walk when the
 *    next key leaves A's subtree; i is A's stripped ref length.
 *  - The vendor restores B.ref from ctmp before the B-walk and
 *    re-tests the prefix with j (B's stripped length) - kept.
 *  - tot==0.0 and val==0.0 (sqrt of a zero product) both return
 *    0.0 without the restore in the early-out case - kept (the
 *    first early-out skips the restore line, vendor behavior).
 */

private const val STR_MAX = 32000 // vendor strmax.h:3 (file-private, cf. unit 22)
private const val XNEXT = 8       // vendor defines.h:88
private const val GLOBAL_NOT_FOUND = 2 // vendor libmpscpp.h:71

private val SOH = 1.toChar() // the vendor's x01 separator byte

fun Cosine(A: Global, B: Global): Double {
    val tmp = CharArray(STR_MAX)
    val ctmp = CharArray(STR_MAX)
    val btmp = CharArray(STR_MAX)
    val ttt = CharArray(STR_MAX)
    val tmp1 = CharArray(STR_MAX)
    val rslt = CharArray(STR_MAX)
    val refA = CharArray(STR_MAX)
    val refB = CharArray(STR_MAX) // local stand-in for B.ref
    // char x01[2] = {1,0}; // h013: declared, never used in the body

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

        val v = strtodC(rslt) // first operand
        totD = totD + v * v

        strcpyC(refB, ctmp) // strcpy(B.ref,ctmp)
        strcatOff(refB, tmp, i) // strcat(B.ref,&tmp[i])
        strcpyC(btmp, refB) // strcpy(btmp,B.ref)

        if (GlobalRef(B, emptyList()).Data() == 0.0) continue // !B.Data()

        get_global(btmp, rslt)

        if (MdhState.svPtr.tpx == 0L) {
            MdhState.globalExceptionCode = GLOBAL_NOT_FOUND
            throw MumpsGlobalException()
        }

        val v1 = strtodC(rslt)

        tot = tot + v * v1
    }

    if (tot == 0.0) return 0.0

    strcpyC(refB, ctmp) // restore
    val bflg = 1 // vendor: int bflg=1 (never read; kept per source)
    ttt[0] = NUL // strcpy(ttt,"")

    while (true) {
        if (mglobal(XNEXT, ctmp, ttt, MdhState.svPtr) == 0) break

        if (strncmpOff(refB, ctmp, j) != 0) break // strncmp(B.ref,ctmp,j)

        strcpyC(tmp1, ctmp) // strcpy(tmp1,ctmp)
        get_global(tmp1, rslt)

        if (MdhState.svPtr.tpx == 0L) {
            MdhState.globalExceptionCode = GLOBAL_NOT_FOUND
            throw MumpsGlobalException()
        }

        val v1 = strtodC(rslt)
        totQ = totQ + v1 * v1
    }

    val v = sqrt(totD * totQ)

    if (v == 0.0) return 0.0

    return tot / v
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
