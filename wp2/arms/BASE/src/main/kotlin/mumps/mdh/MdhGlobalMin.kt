package mumps.mdh

import mumps.interp.CRuntime
import mumps.interp.GlobalExceptionCode
import mumps.interp.MumpsGlobalException
import mumps.interp.STR_MAX

/**
 * Kotlin translation of libmpscpp.cpp::global::Min (Study Subset unit 44,
 * Batch 2, arm BASE; translated 2026-11-15).
 *
 * C: double global::Min() (libmpscpp.cpp:1690-1716) — $ZMIN over a
 * global's sibling nodes: walk XNEXT siblings sharing the ref key
 * prefix, retrieve each with the get_global macro, strtod the value,
 * track the minimum.
 *
 * BUFFER ROLES: tmp is the XNEXT key buffer (overwritten in place by
 * each Mglobal call) -> StringBuilder (KiLL heuristic-96 dual-role
 * precedent); ttt and rslt are Mglobal holders -> StringBuilder
 * (heuristic 66); tmp1 is a strmove snapshot -> immutable String.
 *
 * MACRO EXPANSION: get_global(ref,rslt) is a #define (libmpscpp.h:117)
 * expanding to (svPtr->tpx = Mglobal(RETRIEVE, ref, rslt, svPtr), rslt)
 * — the comma operator yields rslt, i.e. statement + value; in Kotlin
 * it is the assignment alone (rslt is the StringBuilder read after).
 *
 * QUIRK/SAFETY: C's trailing-control check reads tmp[i-1] and tmp[i-2]
 * without a length guard (UB on short refs) — Kotlin adds i>=2
 * (safe-widening, heuristic 45 family); the truncation drops a final
 * control byte (<32) exactly as C's tmp[--i]=0 does. strncmp(ref,tmp,i)
 * -> tmp.toString().regionMatches(0, ref, 0, i) (KiLL heuristic-96
 * precedent: insufficient length yields false, matching C's
 * NUL-vs-char nonzero). First-value flag (flg) -> Boolean.
 */
fun MdhGlobal.min(): Double {
    val svPtr = MdhRuntime.svPtr

    val ttt = StringBuilder(STR_MAX)
    val rslt = StringBuilder(STR_MAX)
    var flg = true // C: int flg=1
    var min = 0.0
    var v: Double

    // C: strcpy(tmp,ref); i=strlen(tmp); if (tmp[i-1]<32&&tmp[i-2]<32) tmp[--i]=0;
    val tmp = StringBuilder(STR_MAX).append(ref)
    var i = tmp.length
    if (i >= 2 && tmp[i - 1].code < 32 && tmp[i - 2].code < 32) {
        tmp.deleteCharAt(i - 1)
        i--
    }

    while (true) { // C: while(1) {
        if (mglobal(XNEXT, tmp, ttt, svPtr) == 0) break // C: Mglobal(XNEXT, tmp, ttt, svPtr)==0

        // C: if (strncmp(ref,tmp,i)!=0) break;
        if (!tmp.toString().regionMatches(0, ref, 0, i)) break

        // C: strcpy(tmp1,tmp);
        val tmp1 = tmp.toString()

        // C: get_global(tmp1,rslt) — macro: svPtr->tpx=Mglobal(RETRIEVE,tmp1,rslt,svPtr)
        svPtr.tpx = mglobal(RETRIEVE, tmp1, rslt, svPtr).toLong()

        // C: if (!svPtr->tpx) { GlobalExceptionCode=GLOBAL_NOT_FOUND; throw ...; }
        if (svPtr.tpx == 0L) {
            GlobalExceptionCode = GLOBAL_NOT_FOUND
            throw MumpsGlobalException()
        }

        // C: val=strtod(rslt,NULL);
        v = CRuntime.strtod(rslt.toString())

        if (flg) { // C: if (flg) { min=val; flg=0; continue; }
            min = v
            flg = false
            continue
        }

        // C: if (val<min) min=val;
        if (v < min) min = v
    }

    return min
}
