package mumps.interp

import kotlin.math.exp

/**
 * Kotlin translation of interp-arith.cpp::mps_exp (Study Subset unit 40,
 * Batch 2, arm BASE; translated 2026-11-15).
 *
 * C: void mps_exp(char *c, char *a) (interp-arith.cpp:783-813) —
 * $ZEXP: exponentiate the numeric string a, format into c.
 *
 * CONFIG BRANCHES (heuristic 39 family, NumSetup precedent): the C
 * three-way #if selects MPFR (MULTI_PRECISION), long double (BIGFLOAT),
 * or hardware double. The reference build defines none of
 * MULTI_PRECISION/BIGFLOAT — InterpState.MULTI_PRECISION stays false
 * and the MPFR branch is a TODO guard exactly as in numSetup; the
 * BIGFLOAT #ifdef is compile-time-dead in the reference build and is
 * documented rather than emitted.
 *
 * gcvt_wrapper(aa,c) -> fmt8g("%.8g") per the reference-build expansion
 * (InterpState.kt: GCVT is NOT defined). strtod(a,NULL) ->
 * CRuntime.strtod (hex-prefix divergence documented there). The char*
 * out-param c becomes the return value (heuristic 56).
 */
fun mpsExp(a: String): String {
    if (InterpState.MULTI_PRECISION) {
        TODO("MPFR branch: reference build does not define MULTI_PRECISION (heuristic 39)")
    }

    // C: double aa=exp(strtod(a,NULL)); gcvt_wrapper(aa,c); return;
    // reference build: hardware double, no BIGFLOAT (long double)
    val aa = exp(CRuntime.strtod(a))
    return fmt8g(aa)
}
