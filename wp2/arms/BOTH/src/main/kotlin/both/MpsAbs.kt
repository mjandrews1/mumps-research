package both

// Unit 165 (BOTH B6 T2, core): interp-arith.cpp::mps_abs (34 LOC).
// Batch_plan ordinal 165: core, LOC 34, score 11.6, complexity
// rank 115. Three-way preprocessor conditional (the mps_log10
// family shape); the frozen reference build config resolves it
// (h018): MULTI_PRECISION and BIGFLOAT are both undefined, so the
// live arm is gcvt_wrapper(fabs(strtod(in,NULL)),out); return 0.
//
// Excluded arms (vendor interp-arith.cpp:557-592, translated only
// for the record as comments):
//   #ifdef BIGFLOAT (undefined):
//     gcvt_wrapper(fabsl(strtold(in,NULL)),out); return 0;
//   #else (live):
//     gcvt_wrapper(fabs(strtod(in,NULL)),out); return 0;
//   #ifdef MULTI_PRECISION (undefined): the gmp block - the
//     decimal-string split on strchr(in,'.'): the mpf_t static x
//     branch (mpf_init2 FLT_PRECISION, gmp_sscanf "%Fg", mpf_abs,
//     gmp_sprintf "%.Fg") and the mpz_t static na branch (mpz_init,
//     gmp_sscanf "%Zd" with the "0" fallback on parse failure,
//     mpz_abs, gmp_sprintf "%Zd"), each with the init-once guard
//     f=1 idiom and the @FLT_PRECISION@ build substitution.
//
// Vendor note kept: the int return is a fixed 0 in both live C
// arms (the fold returns 0 verbatim).
//
// Parity note (T1 mps_log10 precedent): the C library fabs vs
// Kotlin's kotlin.math.abs is the same libm-family parity the
// unit-132 render journaled for log10 (edge spelling rides the
// gcvtWrapper seat).
//
// BOTH consults (find_rule before render, events logged):
//   h018 preprocessor_conditional - reused and hit: live-arm
//     resolution against the reference build config; excluded
//     arms + the @FLT_PRECISION@ substitution kept as comments.
//   h009 format_string - reused and hit via the gcvtWrapper seat
//     (%g shape, Locale.US; BothNumRuntime.kt).
//   h017 naming - hit: mps_abs -> mpsAbs (the unit-132
//     mps_log10 -> mpsLog10 fold).

internal fun mpsAbs(inName: String, out: CharArray): Int {
    gcvtWrapper(kotlin.math.abs(strtodC(inName)), out)
    return 0
}
