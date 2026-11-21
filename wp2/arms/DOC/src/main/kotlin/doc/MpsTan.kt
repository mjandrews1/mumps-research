package doc

import kotlin.math.tan

// Unit 251 (B9 tranche 1): interp-arith.cpp::mps_tan (batch 9,
// simple stratum; units.csv row 273: loc 22, ptr 2, ctl 1, score
// 8.4, rank 94; physLOC 22 physically verified this session).
// Same reference-config resolution as the mps_cos sibling per
// H-018 (no MULTI_PRECISION, no BIGFLOAT: the plain double arm
// is live, the tanl/strtold and MPFR arms documentary). The
// strtod fold (MpsExp.kt seat) and the gcvt_wrapper H-014 stub
// seat ride the family shape verbatim; tan is the numeric
// intrinsic kotlin.math.tan per H-010. Naming per H-017
// (mps_tan -> mpsTan).

fun mpsTan(a: CharArray, b: String) {
    // BIGFLOAT arm (excluded by the reference config):
    //   gcvt_wrapper(tanl(strtold(b, NULL)), a)
    // MULTI_PRECISION arm (excluded):
    //   static mpfr_t fa,fb,fc; mpfr_init2(...); mpfr_set_str(fb, b,
    //   10, MPFR_RNDN); mpfr_tan(fa, fb, MPFR_RNDN); mpfr_sprintf(a,
    //   "%.8RNg", fa)
    val x = tan(strtod(b))
    Stubs.gcvtWrapper(x, a)
    return
}
