package doc

// Unit 163 (B6 tranche 1): interp-arith.cpp::mps_sin (batch 6,
// simple stratum; LOC 22, score 9.8). The mps_log2 twin (unit
// 162, landed this tranche): same reference-config resolution
// per H-018 (no MULTI_PRECISION, no BIGFLOAT - the plain-double
// arm is live, the sinl/strtold and MPFR arms documentary).
// The strtod fold (MpsExp.kt seat) and the gcvt_wrapper H-014
// stub seat ride the mps_log10 shape verbatim; kotlin.math.sin
// is the C sin double. Naming per H-017 (mps_sin -> mpsSin).

fun mpsSin(a: CharArray, b: String) {
    // BIGFLOAT arm (excluded by the reference config):
    //   gcvt_wrapper(sinl(strtold(b, NULL)), a)
    // MULTI_PRECISION arm (excluded):
    //   static mpfr_t fa,fb; mpfr_init2(...); mpfr_set_str(fb, b, 10,
    //   MPFR_RNDN); mpfr_sin(fa, fb, MPFR_RNDN); mpfr_sprintf(a,
    //   "%.8RNg", fa)
    val x = kotlin.math.sin(strtod(b))
    Stubs.gcvtWrapper(x, a)
    return
}
