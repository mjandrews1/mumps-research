package mumps.mdh

import mumps.interp.GlobalExceptionCode
import mumps.interp.MumpsGlobalException
import mumps.interp.MsvState
import mumps.interp.STR_MAX

/**
 * Kotlin translation of libmpscpp.cpp::global::Piece (Study Subset unit
 * 38, Batch 2, arm BASE; translated 2026-11-15).
 *
 * C: mstring global::Piece(const char *pat, int ii, int jj)
 * (libmpscpp.cpp:7181-7202) — $PIECE on a global node: retrieve the
 * node's data, then apply the library's _piece() with ii/jj stringified
 * by sprintf; jj<0 collapses to ii (C mutates its by-value param).
 *
 * OVERLOAD SET COLLAPSE (heuristic 93): the C++ dispatch set has four
 * entries — Piece(const char*,int,int) (translated here) and
 * Piece(global,int,int) / Piece(string,int,int) / Piece(mstring&,int,int)
 * (7204-7283). The string/mstring forwarders collapse since Kotlin has
 * one String type; the `global pat` overload retrieves the PATTERN
 * from a global before calling the same core, which needs mstring/
 * global-arg dispatch that does not exist in Kotlin — not translated;
 * callers retrieve the pattern themselves and pass String.
 *
 * Buffers: tmp[STR_MAX] (the _piece out-param) -> return value
 * (heuristic 56); gtmp[STR_MAX] (the Mglobal out-param) -> StringBuilder
 * holder (heuristic 66); cc1[65]/cc2[64] sprintf targets -> Kotlin
 * integer-to-string. Retrieve-then-throw reused from unit 32.
 */

/** C: void _piece(unsigned char *rslt, unsigned char *source,
 *  unsigned char *pat, unsigned char *ii, unsigned char *jj, int flag,
 *  unsigned char *extra, MSV *) (include/mumpsc/libmpscpp.h:213) —
 *  $PIECE core; the out-param becomes the return value (heuristic 56);
 *  body outside batch scope. */
fun _piece(source: String, pat: String, ii: String, jj: String, flag: Int, extra: String, svPtr: MsvState): String =
    TODO("_piece not yet translated (lives outside batch scope)")

/** C: mstring global::Piece(const char *pat, int ii, int jj) */
fun MdhGlobal.piece(pat: String, ii: Int, jj: Int): String {
    // C: unsigned char gtmp[STR_MAX]; int f = Mglobal(RETRIEVE, ref, gtmp, svPtr);
    val gtmp = StringBuilder(STR_MAX)
    val f = mglobal(RETRIEVE, ref, gtmp, MdhRuntime.svPtr)

    if (f == 0) { // C: if (f==0) { GlobalExceptionCode=GLOBAL_NOT_FOUND; throw ...; }
        GlobalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }

    // C: if (jj<0) jj=ii;
    val jj2 = if (jj < 0) ii else jj

    // C: sprintf(cc1,"%d",ii); sprintf(cc2,"%d",jj);
    val cc1 = ii.toString()
    val cc2 = jj2.toString()

    // C: _piece(tmp, gtmp, pat, cc1, cc2, 0, "", svPtr); tmp1=tmp; return tmp1;
    return _piece(gtmp.toString(), pat, cc1, cc2, 0, "", MdhRuntime.svPtr)
}
