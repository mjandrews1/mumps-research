package mumps.interp

import kotlin.math.pow

/**
 * Kotlin translation of interp-arith.cpp::mps_exp10 (Study Subset unit 41,
 * Batch 2, arm BASE; translated 2026-11-15).
 *
 * C: void mps_exp10(char *c, char *a) (interp-arith.cpp:853-882) —
 * $ZEXP10: base-10 exponentiation (10**a) of the numeric string a.
 *
 * CONFIG BRANCHES (heuristic 39 family, unit 40 precedent): reference
 * build defines neither MULTI_PRECISION nor BIGFLOAT — MPFR branch is
 * an InterpState.MULTI_PRECISION TODO guard, BIGFLOAT documented as
 * compile-time-dead. Kotlin has no exp10(); 10.0.pow(x) reproduces the
 * C library's exp10 for the hardware-double path.
 *
 * QUIRK PRESERVED: the C MPFR branch calls mpfr_exp2 (base-2), not
 * mpfr_exp10 — an apparent copy-paste bug carried in the TODO message
 * so the divergence is on record if that branch is ever enabled.
 *
 * gcvt_wrapper -> fmt8g (%.8g, GCVT undefined); strtod ->
 * CRuntime.strtod; char* out-param c -> return value (heuristic 56).
 */
fun mpsExp10(a: String): String {
    if (InterpState.MULTI_PRECISION) {
        TODO("MPFR branch: reference build does not define MULTI_PRECISION (heuristic 39); note C's MPFR branch calls mpfr_exp2 (base-2), not mpfr_exp10 — quirk preserved on record")
    }

    // C: double aa=exp10(strtod(a,NULL)); gcvt_wrapper(aa,c); return;
    // reference build: hardware double, no BIGFLOAT (exp10l)
    val aa = 10.0.pow(CRuntime.strtod(a))
    return fmt8g(aa)
}
