package doc

// Unit 103 (B4 tranche 1): interp-arith.cpp::mps_asin (batch 4, simple
// stratum; LOC 22, score 9.8). H-018 application: the reference-build
// live arm is the plain-double gcvt path (gcvt_wrapper(asin(strtod(b,
// NULL)),a)); the BIGFLOAT arm (gcvt_wrapper(asinl(strtold(...))))
// and the MULTI_PRECISION arm (static mpfr_t fa/fb/fc with the
// once-only mpfr_init2 of FLT_PRECISION, mpfr_set_str, mpfr_asin,
// mpfr_sprintf "%.8RNg") are EXCLUDED per the reference build config
// and documented here, not rendered. Unit-67 mps_sqrt twin (no keyword
// collision this time, so no H-015 consult; the parameter names a/b
// pass through). asin is kotlin.math.asin (numeric intrinsic per
// H-010); the parse uses the arm-wide C-protocol strtod (MpsExp.kt);
// gcvt_wrapper is the H-014 stub seat (numSetup.h residue owner).
// Vendor quirk kept: like the whole mps_* family there is no domain
// check in the live arm (asin of an out-of-range value yields NaN
// through the same path the vendor's C library takes).

fun mps_asin(out: CharArray, b: CharArray): Int {
    Stubs.gcvtWrapper(kotlin.math.asin(strtod(CstringOps.cstring(b))), out)
    return 0
}
