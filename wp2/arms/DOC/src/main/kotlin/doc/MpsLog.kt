package doc

import kotlin.math.ln

// Unit 249 (B9 tranche 1): interp-arith.cpp::mps_log (batch 9,
// simple stratum; units.csv row 271: loc 22, ptr 2, ctl 1, score
// 8.4, rank 88; physLOC 22 physically verified this session).
// Same reference-config resolution as the unit-40/41/162 family
// per H-018 (no MULTI_PRECISION, no BIGFLOAT: the plain double
// arm is live, the logl/strtold and MPFR arms documentary). The
// strtod fold (MpsExp.kt seat) and the gcvt_wrapper H-014 stub
// seat ride the family shape verbatim; natural log renders as
// the numeric intrinsic kotlin.math.ln (H-010 adjacent, the
// MpsLog2 log2 precedent). Naming per H-017 (mps_log -> mpsLog).

fun mpsLog(a: CharArray, b: String) {
    // BIGFLOAT arm (excluded by the reference config):
    //   gcvt_wrapper(logl(strtold(b, NULL)), a)
    // MULTI_PRECISION arm (excluded):
    //   static mpfr_t fa,fb; mpfr_init2(...); mpfr_set_str(fb, b, 10,
    //   MPFR_RNDN); mpfr_log(fa, fb, MPFR_RNDN); mpfr_sprintf(a,
    //   "%.8RNg", fa)
    val x = ln(strtod(b))
    Stubs.gcvtWrapper(x, a)
    return
}
