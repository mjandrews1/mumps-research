package doc

// Unit 132 (B5 tranche 1): interp-arith.cpp::mps_log10 (batch 5,
// simple stratum; LOC 22, score 9.8). Same reference-config
// resolution as the unit-40/41 mps_exp family per H-018 (no
// MULTI_PRECISION, no BIGFLOAT: the plain-double arm is live, the
// log10l/strtold and MPFR arms documentary). The mps Acos (unit 10)
// and mps_exp family seat shapes are reused: strtod fold and the
// gcvt_wrapper H-014 stub seat. Naming per H-017 (mps_log10 ->
// mpsLog10).

fun mpsLog10(a: CharArray, b: String) {
    // BIGFLOAT arm (excluded by the reference config):
    //   gcvt_wrapper(log10l(strtold(b, NULL)), a)
    // MULTI_PRECISION arm (excluded):
    //   static mpfr_t fa,fb; mpfr_init2(...); mpfr_set_str(fb, b, 10,
    //   MPFR_RNDN); mpfr_log10(fa, fb, MPFR_RNDN); mpfr_sprintf(a,
    //   "%.8RNg", fa)
    val x = kotlin.math.log10(strtod(b))
    Stubs.gcvtWrapper(x, a)
    return
}
