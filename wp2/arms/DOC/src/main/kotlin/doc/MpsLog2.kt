package doc

// Unit 162 (B6 tranche 1): interp-arith.cpp::mps_log2 (batch 6,
// simple stratum; LOC 22, score 9.8). Same reference-config
// resolution as the unit-40/41 mps_exp family and the unit-132
// mps_log10 render per H-018 (no MULTI_PRECISION, no BIGFLOAT:
// the plain-double arm is live, the log2l/strtold and MPFR arms
// documentary). The strtod fold (MpsExp.kt seat) and the
// gcvt_wrapper H-014 stub seat ride the mps_log10 shape
// verbatim; kotlin.math.log2 is the sin of the C log2 double.
// Naming per H-017 (mps_log2 -> mpsLog2).

fun mpsLog2(a: CharArray, b: String) {
    // BIGFLOAT arm (excluded by the reference config):
    //   gcvt_wrapper(log2l(strtold(b, NULL)), a)
    // MULTI_PRECISION arm (excluded):
    //   static mpfr_t fa,fb; mpfr_init2(...); mpfr_set_str(fb, b, 10,
    //   MPFR_RNDN); mpfr_log2(fa, fb, MPFR_RNDN); mpfr_sprintf(a,
    //   "%.8RNg", fa)
    val x = kotlin.math.log2(strtod(b))
    Stubs.gcvtWrapper(x, a)
    return
}
