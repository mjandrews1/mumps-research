package mumps.mdh

import mumps.interp.CRuntime
import mumps.interp.GlobalExceptionCode
import mumps.interp.MumpsGlobalException
import mumps.interp.MsvState
import mumps.interp.STR_MAX

/**
 * Kotlin translation of libmpscpp.cpp::global::Find (Study Subset unit 33,
 * Batch 2, arm BASE; translated 2026-11-15).
 *
 * C: int global::Find(char * key, int st) (libmpscpp.cpp:7566-7586) —
 * $FIND on a global node: retrieve the node's data, then apply the
 * library's _find() with st stringified via Mltoa; the character index
 * comes back in r and is converted with atoi.
 *
 * Buffers: tmp[STR_MAX] (the Mglobal out-param) -> StringBuilder holder
 * (heuristic 66, btreeX precedent; same retrieve-then-throw pattern as
 * unit 32, global::Extract); r[20] (the _find out-param) -> return value
 * (heuristic 56, static-buffer->value); a[40] (the Mltoa target) ->
 * CRuntime.mltoa(st) returning the String. Mglobal/RETRIEVE/
 * GLOBAL_NOT_FOUND/svPtr live in MdhGlobalExtract.kt (unit 32) per the
 * one-owner-per-C-entity rule.
 */

/** C: void _find(unsigned char *r, unsigned char *in, unsigned char *key,
 *  unsigned char *a) (include/mumpsc/libmpscpp.h:238) — $FIND core; the
 *  out-param becomes the return value (heuristic 56); body outside
 *  batch scope. */
fun _find(input: String, key: String, a: String): String =
    TODO("_find not yet translated (lives outside batch scope)")

/** C: int global::Find(char * key, int st) */
fun MdhGlobal.find(key: String, st: Int): Int {
    // C: unsigned char tmp[STR_MAX]; int f = Mglobal(RETRIEVE, ref, tmp, svPtr);
    val tmp = StringBuilder(STR_MAX)
    val f = mglobal(RETRIEVE, ref, tmp, MdhRuntime.svPtr)

    if (f == 0) { // C: if (f==0) { GlobalExceptionCode=GLOBAL_NOT_FOUND; throw ...; }
        GlobalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }

    // C: char a[40]; Mltoa(st,(unsigned char *) a);
    val a = CRuntime.mltoa(st.toLong())

    // C: char r[20]; _find((unsigned char *) r,(unsigned char *) tmp,
    //     (unsigned char *) key,(unsigned char *) a);
    val r = _find(tmp.toString(), key, a)

    // C: rr=atoi((char *) r); return rr;
    return CRuntime.atoi(r)
}
