package both

// Unit 162 (BOTH B6 T1, simple): interp-arith.cpp::mps_log2 (22
// LOC). Batch_plan ordinal 162: simple, score 9.8. Three-way
// preprocessor conditional (the mps_log10 unit-132 twin shape);
// the frozen reference build config resolves it (h018):
// MULTI_PRECISION and BIGFLOAT are both undefined, so the
// live arm is gcvt_wrapper(log2(strtod(b,NULL)),a). The excluded
// arms are documented below as comments per the h018 action.
//
// Excluded arms (vendor interp-arith.cpp:132-155, translated
// only for the record as comments):
//   #ifdef BIGFLOAT (undefined):  gcvt_wrapper(log2l(strtold(b,NULL)),a)
//   #else (live):                 gcvt_wrapper(log2(strtod(b,NULL)),a)
//   #ifdef MULTI_PRECISION (undefined): the mpfr block - static
//     mpfr_t fa,fb (init-once guard f=1, FLT_PRECISION precision),
//     mpfr_set_str(fb,b,10,MPFR_RNDN), mpfr_log2(fa,fb,MPFR_RNDN),
//     mpfr_sprintf(a,"%.8RNg",fa).
//
// Parity note (T1 mps_log10 precedent): the C library log2 vs
// Kotlin's kotlin.math.log2 is the same libm-family parity the
// unit-132 render journaled for log10 (edge spelling rides the
// gcvtWrapper seat); kotlin.math.log2 (JVM Math.log-based, not
// Math.log(x)/Math.log(2) restructuring) is the seat call.
//
// BOTH consults (find_rule before render, events logged):
//   h018 preprocessor_conditional - reused and hit: live-arm
//     resolution against the reference build config; excluded
//     arms + the @FLT_PRECISION@ build-substitution macro kept
//     as comments.
//   h009 format_string - reused via the gcvtWrapper seat (%g
//     shape, Locale.US; BothNumRuntime.kt).
//   h017 naming - reused and hit: mps_log2 -> mpsLog2.
//   Dafny: the live-arm resolution and the digit-prefix parse
//     are specified in BOTH spec/mps_log2.dfy (this tranche's
//     new spec, modeled on the arm-own mps_log10.dfy; verified 0
//     errors).

internal fun mpsLog2(a: CharArray, b: String) {
    gcvtWrapper(kotlin.math.log2(strtodC(b)), a)
}
