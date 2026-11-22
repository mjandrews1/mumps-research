package both

// Unit 163 (BOTH B6 T1, simple): interp-arith.cpp::mps_sin (22
// LOC). Batch_plan ordinal 163: simple, score 9.8. Three-way
// preprocessor conditional (the mps_log10/mps_log2 twin shape);
// the frozen reference build config resolves it (h018):
// MULTI_PRECISION and BIGFLOAT are both undefined, so the
// live arm is gcvt_wrapper(sin(strtod(b,NULL)),a). The excluded
// arms are documented below as comments per the h018 action.
//
// Excluded arms (vendor interp-arith.cpp:253-276, translated
// only for the record as comments):
//   #ifdef BIGFLOAT (undefined):  gcvt_wrapper(sinl(strtold(b,NULL)),a)
//   #else (live):                 gcvt_wrapper(sin(strtod(b,NULL)),a)
//   #ifdef MULTI_PRECISION (undefined): the mpfr block - static
//     mpfr_t fa,fb (init-once guard f=1, FLT_PRECISION precision),
//     mpfr_set_str(fb,b,10,MPFR_RNDN), mpfr_sin(fa,fb,MPFR_RNDN),
//     mpfr_sprintf(a,"%.8RNg",fa).
//
// Parity note (T1 mps_log10 precedent): the C library sin vs
// Kotlin's kotlin.math.sin is the same libm-family parity the
// unit-132 render journaled for log10 (edge spelling rides the
// gcvtWrapper seat).
//
// Dafny: EXEMPT - the live-arm resolution lattice and the
// digit-prefix bounds are already specified once for the family
// (spec/mps_log10.dfy; the new mps_log2.dfy twin of this
// tranche); the sin transcendental core adds no decidable
// surface beyond that same lattice, so no new spec is added
// (journaled).
//
// BOTH consults (find_rule before render, events logged):
//   h018 preprocessor_conditional - reused and hit: live-arm
//     resolution against the reference build config; excluded
//     arms + the @FLT_PRECISION@ build-substitution macro kept
//     as comments.
//   h009 format_string - reused via the gcvtWrapper seat (%g
//     shape, Locale.US; BothNumRuntime.kt).
//   h017 naming - reused and hit: mps_sin -> mpsSin.

internal fun mpsSin(a: CharArray, b: String) {
    gcvtWrapper(kotlin.math.sin(strtodC(b)), a)
}
