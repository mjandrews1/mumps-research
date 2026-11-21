package doc

import kotlin.math.pow

// Unit 247 (B9 tranche 1): interp-arith.cpp::mps_exp2 (batch 9,
// simple stratum; units.csv row 269: loc 26, ptr 2, ctl 1, score
// 8.3, rank 100; physLOC 26 physically verified this session).
// Same reference-config resolution as the unit-40/41 exp family
// per H-018 (no MULTI_PRECISION, no BIGFLOAT: the plain double arm
// is live, the exp2l/strtold and MPFR arms documentary). The
// strtod fold (MpsExp.kt seat) and the gcvt_wrapper H-014 stub
// seat ride the family shape verbatim; exotic-C exp2 double
// renders as 2.0.pow(x) (the MpsExp10 10.0.pow precedent; Kotlin
// math has no exp2 intrinsic). Naming per H-017 (mps_exp2 ->
// mpsExp2).

fun mpsExp2(c: CharArray, a: String) {
    // BIGFLOAT arm (excluded by the reference config):
    //   long double aa = exp2l(strtold(a, NULL)); gcvt_wrapper(aa, c)
    // MULTI_PRECISION arm (excluded):
    //   static mpfr_t fa,fc; static int f=1; unsigned long power;
    //   mpfr_init2(fa, FLT_PRECISION); mpfr_init2(fc, FLT_PRECISION);
    //   mpfr_set_str(fa, a, 10, MPFR_RNDN); mpfr_exp2(fc, fa,
    //   MPFR_RNDN); mpfr_sprintf(c, "%.8RNg", fc)
    val aa = 2.0.pow(strtod(a))
    Stubs.gcvtWrapper(aa, c)
    return
}
