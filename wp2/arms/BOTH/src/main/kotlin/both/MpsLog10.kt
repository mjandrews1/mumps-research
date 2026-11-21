package both

// Unit 132 (BOTH B5 T1, simple): interp-arith.cpp::mps_log10 (22 LOC).
// Batch_plan ordinal 132: simple, score 9.8. Three-way preprocessor
// conditional; the frozen reference build config resolves it
// (h018): MULTI_PRECISION and BIGFLOAT are both undefined, so the
// live arm is gcvt_wrapper(log10(strtod(b,NULL)),a). The excluded arms
// are documented below as comments per the h018 action.
//
// Excluded arms (vendor, translated only for the record as comments):
//   #ifdef BIGFLOAT (undefined):  gcvt_wrapper(log10l(strtold(b,NULL)),a)
//   #else (live):                 gcvt_wrapper(log10(strtod(b,NULL)),a)
//   #ifdef MULTI_PRECISION (undefined): the mpfr block - static
//     mpfr_t fa,fb (init-once guard f=1, FLT_PRECISION precision),
//     mpfr_set_str(fb,b,10,MPFR_RNDN), mpfr_log10(fa,fb,MPFR_RNDN),
//     mpfr_sprintf(a,"%.8RNg",fa).
//
// BOTH consults (find_rule before render, events logged):
//   h018 preprocessor_conditional - reused: resolve against the
//     reference build config; translate only the live arm; excluded
//     arms + the @FLT_PRECISION@ build-substitution macro documented
//     as comments.
//   h009 format_string - reused via the gcvtWrapper seat (%g shape,
//     Locale.US; BothNumRuntime.kt).
//   h017 naming - reused: mps_log10 -> mpsLog10.
//   Dafny: the live-arm resolution and the decimal-prefix parse are
//     specified in spec/mps_log10.dfy (verified 0 errors).

internal fun mpsLog10(a: CharArray, b: String) {
    gcvtWrapper(kotlin.math.log10(strtodC(b)), a)
}
