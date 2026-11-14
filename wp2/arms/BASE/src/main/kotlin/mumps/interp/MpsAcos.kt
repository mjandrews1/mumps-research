package mumps.interp

import kotlin.math.acos

/**
 * Kotlin translation of interp-arith.cpp::mps_acos (Study Subset unit 10,
 * Batch 1, arm BASE; translated 2026-11-14).
 *
 * C: void mps_acos(char *a, char *b) — writes acos(strtod(b)) into a via
 * gcvt_wrapper. Out-buffer -> return value (heuristic 7); the build-config
 * ifdef web (GCVT / BIGFLOAT / MULTI_PRECISION) -> config consts with the
 * reference configuration set (heuristic 39); MPFR and long-double
 * branches are TODO behind their flags.
 */
fun mpsAcos(b: String): String {
    if (InterpState.MULTI_PRECISION) {
        TODO("MPFR branch: reference build does not define MULTI_PRECISION (heuristic 39)")
    }
    if (InterpState.BIGFLOAT) {
        TODO("long-double branch: reference build does not define BIGFLOAT (heuristic 39)")
    }
    // C: strtod(b, NULL)
    val x = CRuntime.strtod(b)
    // C: acos(x); glibc vs kotlin libm may differ in the last ulp — heuristic 42.
    val r = if (InterpState.GCVT) CRuntime.gcvt(acos(x), 8) else String.format("%.8g", acos(x))
    return r
}
