package both

// Unit 253 (BOTH B9, simple): interp-arith.cpp::mps_cos (22
// LOC). Batch_plan ordinal 253: simple, score 9.8. Three-way
// preprocessor conditional (the mps_sin twin shape, same
// score); the frozen reference build config resolves it (h018):
// MULTI_PRECISION and BIGFLOAT are both undefined, so the live
// arm is gcvt_wrapper(cos(strtod(b,NULL)),a).
//
// Excluded arms (vendor interp-arith.cpp, translated only for
// the record as comments):
//   #ifdef BIGFLOAT (undefined):  gcvt_wrapper(cosl(strtold(b,NULL)),a)
//   #else (live):                 gcvt_wrapper(cos(strtod(b,NULL)),a)
//   #ifdef MULTI_PRECISION (undefined): the mpfr block - static
//     mpfr_t fa,fb,fc (init-once guard f=1, FLT_PRECISION
//     precision), mpfr_set_str(fb,b,10,MPFR_RNDN),
//     mpfr_cos(fa,fb,MPFR_RNDN), mpfr_sprintf(a,"%.8RNg",fa).
//     The unused static fc is the vendor's dead declaration
//     (h013 family note, excluded arm only).
//
// Parity note (mps_sin precedent): the C library cos vs
// Kotlin's kotlin.math.cos is the same libm-family parity.
//
// Dafny: EXEMPT - family lattice (spec/mps_log10.dfy); no new
// spec (journaled).
//
// BOTH consults (find_rule before render, events logged):
//   h018 preprocessor_conditional - reused and hit: live-arm
//     resolution against the reference build config.
//   h009 format_string - reused via the gcvtWrapper seat.
//   h013 dead_store - reused (the excluded-arm fc declaration).
//   h017 naming - reused and hit: mps_cos -> mpsCos.

internal fun mpsCos(a: CharArray, b: String) {
    gcvtWrapper(kotlin.math.cos(strtodC(b)), a)
}
