package mumps.mdh

import mumps.interp.NUL
import mumps.interp.mglobal
import mumps.interp.strlenC
import mumps.interp.strcpyC
import mumps.interp.writeCstring

/**
 * PRO-arm translation of libmpscpp.cpp::Jaccard (roster row 256,
 * batch 9; units.csv: loc 58, comment_ratio 0.017, pointer_marks
 * 4, control_marks 13, score 35.0, rank 217). Translated
 * independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), reference
 * source lines 1376-1459 - RESOLVES the MdhStubs Jaccard(Global,
 * Global) stub raised at u187 ServiceInterface (correct
 * batch-9 owner tag; the ServiceInterface JACCARD-arm call site
 * binds unchanged) - stub removed.
 *
 * Jaccard(A, B): the Jaccard coefficient of two term-weight
 * globals (friend of class global, Cosine unit-71 / Dice u165
 * family): one A-walk (XNEXT over A.ref, totD += A's weight,
 * tot += val*val1 for the paired B child), then B.ref restored
 * from OriginalB, then one B-walk (totQ += B's weights), then
 * val = totD+totQ-tot; return tot/val (0.0 on val==0.0).
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h007 char_buffer        : ttt/ctmp/btmp/tmp/tmp1/rslt/
 *    OriginalB are STR_MAX C-string buffers (Dice precedent).
 *  - h012 unsigned_byte_domain: Char(0..255) domains; the SOH
 *    sentinel tests compare against Char(1).
 *  - h014 out_of_scope_call : mglobal with XNEXT=8 (defines.h:
 *    88); get_global (MdhStubs, Cosine precedent); B.Data()
 *    via GlobalRef(B, emptyList()) (Dice precedent);
 *    GlobalExceptionCode/throw -> MdhState; strtod ->
 *    per-file strtodC prefix parse; strncmp/strcat -> per-file
 *    helpers (Dice file-private precedent).
 *  - h015 grammar_collision : new top-level Jaccard replaces
 *    the removed stub one-for-one - no collisions.
 *  - h017 naming           : Jaccard keeps the vendor name; the
 *    vendor mutates B.ref in place - the carrier keeps the
 *    refB local stand-in plus the OriginalB save/restore
 *    (Cosine/Dice unit-71/u165 notes).
 *  - h018 preprocessor_conditional: none in the function body
 *    (the surrounding file arms are owned by the misc units).
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The SOH-pair strip runs on B first, then A (Dice order) -
 *    kept.
 *  - Unlike Dice, the A-walk does NOT accumulate totQ - totQ
 *    comes only from the second B-walk - kept.
 *  - B.ref is restored from OriginalB BEFORE the B-walk
 *    (unlike Cosine which restores at the end) - kept.
 *  - The B.Data() skip (continue) leaves tot untouched for
 *    missing B children - kept.
 *  - val==0.0 returns 0.0 - kept.
 */
fun Jaccard(A: Global, B: Global): Double {
    val tmp = CharArray(STR_MAX)
    val ctmp = CharArray(STR_MAX)
    val btmp = CharArray(STR_MAX)
    val ttt = CharArray(STR_MAX)
    val tmp1 = CharArray(STR_MAX)
    val rslt = CharArray(STR_MAX)
    val originalB = CharArray(STR_MAX)
    val refA = CharArray(STR_MAX)
    val refB = CharArray(STR_MAX) // local stand-in for B.ref

    var totD = 0.0
    var totQ = 0.0
    var tot = 0.0

    writeCstring(refA, A.ref)
    writeCstring(refB, B.ref)
    writeCstring(tmp, A.ref) // strcpy(tmp, A.ref)
    writeCstring(ctmp, B.ref) // strcpy(ctmp, B.ref)
    var i = strlenC(refA)
    var j = strlenC(refB)

    if (ctmp[j - 2] == SOH && ctmp[j - 1] == SOH) {
        j--
        ctmp[j] = NUL
    }

    strcpyC(originalB, ctmp) // strcpy(OriginalB,ctmp)

    if (tmp[i - 2] == SOH && tmp[i - 1] == SOH) {
        i--
        tmp[i] = NUL
    }

    while (true) { // A-walk
        if (mglobal(XNEXT, tmp, ttt, MdhState.svPtr) == 0) break

        if (strncmpOff(refA, tmp, i) != 0) break // strncmp(A.ref,tmp,i)

        strcpyC(tmp1, tmp) // strcpy(tmp1,tmp)
        get_global(tmp1, rslt)

        if (MdhState.svPtr.tpx == 0L) {
            MdhState.globalExceptionCode = GLOBAL_NOT_FOUND
            throw MumpsGlobalException()
        }

        val v = strtodC(rslt) // val=strtod(rslt,NULL)  // A(j,k)
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

        val v1 = strtodC(rslt) // val1=strtod(rslt,NULL) // B(j,k)

        tot = tot + v * v1
    }

    strcpyC(refB, originalB) // strcpy(B.ref,OriginalB) - before the B-walk
    // vendor: ctmp is NOT reset - it carries the stripped B ref and
    // MUTATES in place under XNEXT (the in/out key, Mglobal u173 note)

    while (true) { // B-walk
        if (mglobal(XNEXT, ctmp, ttt, MdhState.svPtr) == 0) break

        if (strncmpOff(refB, ctmp, j) != 0) break // strncmp(B.ref,ctmp,j)

        strcpyC(tmp1, ctmp) // strcpy(tmp1,ctmp)
        get_global(tmp1, rslt)

        if (MdhState.svPtr.tpx == 0L) {
            MdhState.globalExceptionCode = GLOBAL_NOT_FOUND
            throw MumpsGlobalException()
        }

        val v1 = strtodC(rslt) // val1=strtod(rslt,NULL)
        totQ = totQ + v1
    }

    val unionSum = totD + totQ - tot // val=totD+totQ-tot

    if (unionSum == 0.0) return 0.0

    return tot / unionSum
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

/** strtod prefix parse over the byte domain (Dice file-private precedent). */
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
    return if (s.isEmpty()) 0.0 else s.toString().toDouble()
}

private const val STR_MAX = 32000 // vendor strmax.h:3 (file-private, cf. unit 22)
private const val XNEXT = 8 // vendor defines.h:88
private const val GLOBAL_NOT_FOUND = 2 // vendor libmpscpp.h:71
private val SOH = 1.toChar() // the vendor's x01 separator byte
