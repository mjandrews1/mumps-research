package both

import kotlin.math.pow

// Unit 256 (BOTH B9, simple): interp-arith.cpp::mps_exp2 (26
// LOC). Batch_plan ordinal 256; units.csv row 268. Three-way
// preprocessor conditional (the mps_sin twin shape); the frozen
// reference build config resolves it (h018): MULTI_PRECISION
// and BIGFLOAT are both undefined, so the live arm is
// double aa=exp2(strtod(a,NULL)); gcvt_wrapper(aa,c).
//
// Excluded arms (vendor interp-arith.cpp, translated only for
// the record as comments):
//   #ifdef BIGFLOAT (undefined):  long double aa=exp2l(strtold(a,NULL));
//     gcvt_wrapper(aa,c); return;
//   #else (live):                  double aa=exp2(strtod(a,NULL));
//     gcvt_wrapper(aa,c); return;
//   #ifdef MULTI_PRECISION (undefined): the mpfr block - static
//     mpfr_t fa,fc (init-once guard f=1, FLT_PRECISION),
//     mpfr_set_str(fa,a,10,MPFR_RNDN), mpfr_exp2(fc,fa,MPFR_RNDN),
//     mpfr_sprintf(c,"%.8RNg",fc).
//     The unused static power is the vendor's dead declaration
//     (h013 family note, excluded arm only).
//
// Parity note: kotlin.math has no exp2; the render seats
// exp2(x) as 2.0.pow(x) (the C99 exp2 definition, the
// kotlin.math.pow extension). glibc's exp2 and pow(2,x) agree
// within one ulp on the double range; the digit shape rides
// the gcvtWrapper seat either way - journaled quantization.
//
// Dafny: EXEMPT - family lattice (spec/mps_log10.dfy); no new
// spec (journaled).
//
// BOTH consults (find_rule before render, events logged):
//   h018 preprocessor_conditional - reused and hit: live-arm
//     resolution against the reference build config.
//   h009 format_string - reused via the gcvtWrapper seat.
//   h013 dead_store - reused (the excluded-arm power
//     declaration; the live arm's local aa is consumed by
//     gcvt_wrapper, kept as the named local for the vendor
//     shape).
//   h017 naming - reused and hit: mps_exp2 -> mpsExp2.

internal fun mpsExp2(c: CharArray, a: String) {
    // double aa=exp2(strtod(a,NULL)); gcvt_wrapper(aa,c); return;
    val aa = 2.0.pow(strtodC(a))
    gcvtWrapper(aa, c)
}
