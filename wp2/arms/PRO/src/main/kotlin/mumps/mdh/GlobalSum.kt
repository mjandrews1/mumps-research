package mumps.mdh

import mumps.interp.NUL
import mumps.interp.mglobal
import mumps.interp.strlenC

/**
 * PRO-arm translation of libmpscpp.cpp::global::Sum (B5 tranche
 * B, unit 142, rank 116). Translated independently from the
 * pristine vendor source (mumps-14-may-2026.tgz, sha256
 * 621deb25...ffdbe), reference source lines 1620-1648.
 *
 * Identity verified against the study tool (tools/select_study_
 * subset.py): global::Sum, cpp, loc 21, comment_ratio 0.045,
 * pointer_marks 1, control_marks 5, score 11.9 - exact match to
 * wp1/study-subset/units.csv row 143 (batch 5).
 *
 * global::Sum(): the sibling-sum walk - XNEXT over the children of
 * this reference's prefix, summing each child's global value as a
 * double while the first i bytes of the returned key still match
 * ref. The !tpx arm after get_global throws the
 * GLOBAL_NOT_FOUND exception (Cosine.kt precedent).
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h004 globals_ext       : svPtr rides the MdhState carrier
 *    (u31; the vendor's this->svPtr member access - the Global
 *    carrier has no svPtr field, MdhState.svPtr is the single
 *    state vector, established Count/Cosine convention).
 *  - h008 static_scratch    : the four STR_MAX char buffers are
 *    auto locals in the vendor (no statics) - fresh CharArray
 *    scratch here.
 *  - h012 unsigned_byte_domain: tmp/ttt/tmp1/rslt are C byte
 *    buffers; the <32 trailing-delimiter trim is a byte-domain
 *    comparison kept verbatim (u124 global::Count precedent).
 *  - h014 out_of_scope_call  : mglobal(XNEXT,...) is the
 *    InterpStubs stub (owner global machinery); get_global is
 *    the MdhStubs stub (owner interp global read); both reused
 *    unmodified (Cosine.kt:104/110 precedent); the throw arm is
 *    the DONE MumpsGlobalException + globalExceptionCode carrier.
 *  - h015 grammar_collision  : pre-push scan incl. cross-file
 *    top-level names: new top-levels Sum on the Global and
 *    GlobalRef carriers (member seats) - the Mstring.sum member
 *    families are distinct; no collisions.
 *  - h017 naming             : Sum keeps the vendor name; the
 *    GlobalRef delegate follows the u118/u124 Count pattern.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The trailing-delimiter trim tmp[--i]=0 pre-decrements and
 *    stores in one motion (the u124 Count quirk family): i shrinks
 *    only when the write happens - rendered verbatim.
 *  - strncmp(ref,tmp,i) over the possibly-TRIMMED length i (after
 *    the trim) - kept; the ref compare uses the trimmed byte count.
 *  - The !tpx check follows get_global's MUMPS convention: tpx==0
 *    means the read failed - the vendor throws, it does not skip
 *    (the sibling already exists by the XNEXT contract, so the
 *    throw guards a corrupt/delete race - kept verbatim).
 */

private const val STR_MAX = 32000 // vendor strmax.h:3 (file-private, cf. unit 22/97)
private const val XNEXT = 8 // vendor defines.h:88 (Mglobal order-next)
private const val GLOBAL_NOT_FOUND = 2 // vendor libmpscpp.h:71

private fun toCbuf(s: String): CharArray {
    val b = CharArray(s.length + 1)
    for (i in s.indices) b[i] = s[i]
    b[s.length] = NUL
    return b
}

/**
 * vendor 1620-1648: double global::Sum() - the sibling-sum walk on
 * the Global carrier (the vendor's *this; the ref member is the
 * carrier's n, the u124 global::Count template).
 */
fun Global.Sum(): Double {
    val ttt = CharArray(STR_MAX)
    val tmp = CharArray(STR_MAX)
    val tmp1 = CharArray(STR_MAX)
    val rslt = CharArray(STR_MAX)
    var tot = 0.0

    val refB0 = toCbuf(ref)
    refB0.copyInto(tmp)
    var i = strlenC(tmp)

    // vendor: if (tmp[i-1] <32 && tmp[i-2] <32 ) tmp[--i] = 0;
    if (tmp[i - 1] < ' ' && tmp[i - 2] < ' ') tmp[--i] = NUL

    val refB = toCbuf(ref)
    while (true) {
        if (mglobal(XNEXT, tmp, ttt, MdhState.svPtr) == 0) break

        var same = true
        for (k in 0 until i) {
            if (refB[k] != tmp[k]) { same = false; break }
        }
        if (!same) break // vendor: strncmp(ref,tmp,i)!=0

        tmp.copyInto(tmp1, 0, 0, strlenC(tmp) + 1) // vendor: strcpy(tmp1,tmp);
        get_global(tmp1, rslt)

        if (MdhState.svPtr.tpx == 0L) {
            MdhState.globalExceptionCode = GLOBAL_NOT_FOUND
            throw MumpsGlobalException()
        }

        val v = toDoubleOf(rslt) // vendor: val=strtod(rslt,NULL);
        tot += v
    }

    return tot
}

/** Call-shape delegate (u118/u124 Count pattern): subscripted call
 *  sites bind the member on GlobalRef. */
fun GlobalRef.Sum(): Double = g.Sum()

/** vendor strtod over the byte domain (per-file private, MpsAcos
 *  precedent). */
private fun toDoubleOf(b: CharArray): Double {
    val s = mumps.interp.cstringAt(b)
    val m = Regex("^[+-]?(\\d+\\.?\\d*|\\.\\d+)([eE][+-]?\\d+)?").find(s)
        ?: return 0.0
    return m.value.toDouble()
}
