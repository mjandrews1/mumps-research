package doc

import kotlin.math.cos

// Unit 250 (B9 tranche 1): interp-arith.cpp::mps_cos (batch 9,
// simple stratum; units.csv row 272: loc 22, ptr 2, ctl 1, score
// 8.4, rank 89; physLOC 22 physically verified this session).
// Same reference-config resolution as the unit-10 mps_acos shape
// per H-018 (no MULTI_PRECISION, no BIGFLOAT: the plain double
// arm is live, the cosl/strtold and MPFR arms documentary). The
// strtod fold (MpsExp.kt seat) and the gcvt_wrapper H-014 stub
// seat ride the family shape verbatim; cos is the numeric
// intrinsic kotlin.math.cos per H-010. Naming per H-017
// (mps_cos -> mpsCos).

fun mpsCos(a: CharArray, b: String) {
    // BIGFLOAT arm (excluded by the reference config):
    //   gcvt_wrapper(cosl(strtold(b, NULL)), a)
    // MULTI_PRECISION arm (excluded):
    //   static mpfr_t fa,fb; mpfr_init2(...); mpfr_set_str(fb, b, 10,
    //   MPFR_RNDN); mpfr_cos(fa, fb, MPFR_RNDN); mpfr_sprintf(a,
    //   "%.8RNg", fa)
    val x = cos(strtod(b))
    Stubs.gcvtWrapper(x, a)
    return
}
