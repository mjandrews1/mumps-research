package both

// Unit 252 (BOTH B9, simple): interp-arith.cpp::mps_log (22
// LOC). Batch_plan ordinal 252: simple, score 9.8. Three-way
// preprocessor conditional (the mps_log10/mps_log2/mps_sin twin
// shape); the frozen reference build config resolves it (h018):
// MULTI_PRECISION and BIGFLOAT are both undefined, so the live
// arm is gcvt_wrapper(log(strtod(b,NULL)),a).
//
// Excluded arms (vendor interp-arith.cpp, translated only for
// the record as comments):
//   #ifdef BIGFLOAT (undefined):  gcvt_wrapper(logl(strtold(b,NULL)),a)
//   #else (live):                 gcvt_wrapper(log(strtod(b,NULL)),a)
//   #ifdef MULTI_PRECISION (undefined): the mpfr block - static
//     mpfr_t fa,fb (init-once guard f=1, FLT_PRECISION
//     precision), mpfr_set_str(fb,b,10,MPFR_RNDN),
//     mpfr_log(fa,fb,MPFR_RNDN), mpfr_sprintf(a,"%.8RNg",fa).
//
// Parity note (unit-132 precedent): the C library log vs
// Kotlin's kotlin.math.ln is the same libm-family parity the
// mps_log10 render journaled (edge spelling rides the
// gcvtWrapper seat); log() is the C natural log.
//
// Dafny: EXEMPT - the live-arm resolution lattice and the
// digit-prefix bounds are specified once for the family
// (spec/mps_log10.dfy and the later family twins); the log
// transcendental core adds no decidable surface beyond that
// lattice, so no new spec is added (journaled).
//
// BOTH consults (find_rule before render, events logged):
//   h018 preprocessor_conditional - reused and hit: live-arm
//     resolution against the reference build config; excluded
//     arms + the FLT_PRECISION macro kept as comments.
//   h009 format_string - reused via the gcvtWrapper seat (%g
//     shape, Locale.US; BothNumRuntime.kt).
//   h017 naming - reused and hit: mps_log -> mpsLog.

internal fun mpsLog(a: CharArray, b: String) {
    gcvtWrapper(kotlin.math.ln(strtodC(b)), a)
}
