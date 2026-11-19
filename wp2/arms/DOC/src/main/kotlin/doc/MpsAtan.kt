package doc

// Unit 71 (B3 tranche 1): interp-arith.cpp::mps_atan (batch 3, simple
// stratum; LOC 22, score 9.8). mps_acos sibling (pilot unit 10,
// line-for-line shape). H-018 ledger: the reference-build live arm
// is the plain-double gcvt path; the BIGFLOAT arm
// (gcvt_wrapper(atanl(strtold(b)),a)) and the MULTI_PRECISION arm
// (static mpfr_t fa/fb initialized once; mpfr_set_str(fb,b,10,
// MPFR_RNDN); mpfr_atan; mpfr_sprintf "%.8RNg") are compiled out per
// the reference build config. The vendor's trailing return on the
// void body is a no-op (kept by the Kotlin unit type). atan is
// kotlin.math.atan (numeric intrinsic per H-010); strtod reuse
// (MpsExp.kt); gcvt_wrapper is the H-014 stub. Naming per H-017.

fun mps_atan(a: CharArray, b: CharArray) {
    Stubs.gcvtWrapper(kotlin.math.atan(strtod(CstringOps.cstring(b))), a)
}
