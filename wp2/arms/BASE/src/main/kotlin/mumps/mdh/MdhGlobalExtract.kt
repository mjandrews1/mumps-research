package mumps.mdh

import mumps.interp.CRuntime
import mumps.interp.GlobalExceptionCode
import mumps.interp.MumpsGlobalException
import mumps.interp.MsvState
import mumps.interp.STR_MAX

/**
 * Kotlin translation of libmpscpp.cpp::global::Extract (Study Subset
 * unit 32, Batch 2, arm BASE; translated 2026-11-15).
 *
 * C: mstring global::Extract(int st, int ln) (libmpscpp.cpp:7546-7565) —
 * $EXTRACT on a global node: retrieve the node's data, then apply the
 * interpreter's _extract() with st/ln stringified via Mltoa.
 *
 * Buffers: tmp[STR_MAX] (the Mglobal out-param) -> StringBuilder holder
 * (heuristic 66, btreeX precedent); r[STR_MAX] (the _extract out-param)
 * -> return value (heuristic 56, static-buffer->value). Mltoa(st,a) ->
 * CRuntime.mltoa(st) returning the String. The C's `rr=r; return rr;`
 * mstring round-trip collapses to returning the String directly.
 */
const val GLOBAL_NOT_FOUND = 2 // C: include/mumpsc/libmpscpp.h:71
const val RETRIEVE = 0        // C: include/mumpsc/defines.h:83

/** C: int Mglobal(int op, unsigned char *name, unsigned char *bd, MSV *)
 *  — the DB API entry (native btree/SQLite); body outside batch scope.
 *  bd is the retrieved-value holder (StringBuilder, btreeX precedent). */
fun mglobal(op: Int, name: String, bd: StringBuilder, svPtr: MsvState): Int =
    TODO("Mglobal not yet translated (native DB API, outside batch scope)")

/** C: void _extract(unsigned char *out, unsigned char *in, unsigned char *a,
 *  unsigned char *b) (interp-sym.cpp:1235) — $EXTRACT core; out-param
 *  becomes the return value (heuristic 56); body outside batch scope. */
fun _extract(input: String, a: String, b: String): String =
    TODO("_extract not yet translated (lives in interp-sym.cpp, outside batch scope)")

/** C: mstring global::Extract(int st, int ln) */
fun MdhGlobal.extract(st: Int, ln: Int): String {
    // C: unsigned char tmp[STR_MAX]; int f = Mglobal(RETRIEVE, ref, tmp, svPtr);
    val tmp = StringBuilder(STR_MAX)
    val f = mglobal(RETRIEVE, ref, tmp, MdhRuntime.svPtr)

    if (f == 0) { // C: if (f==0) { GlobalExceptionCode=GLOBAL_NOT_FOUND; throw ...; }
        GlobalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }

    // C: Mltoa(st,(unsigned char *) a); Mltoa(ln,(unsigned char *) b);
    val a = CRuntime.mltoa(st.toLong())
    val b = CRuntime.mltoa(ln.toLong())

    // C: _extract((unsigned char *) r, (unsigned char *) tmp, a, b); rr=r; return rr;
    return _extract(tmp.toString(), a, b)
}
