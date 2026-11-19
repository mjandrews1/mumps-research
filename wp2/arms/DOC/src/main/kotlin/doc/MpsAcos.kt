package doc

// Unit 10 (B1 tranche 2): interp-arith.cpp::mps_acos (batch 1, simple
// stratum; LOC 22, score 9.8). H-018 application two: the
// reference-build live arm is the plain-double gcvt path; the BIGFLOAT
// arm (acosl/strtold) and the MULTI_PRECISION arm (the static mpfr_t
// trio initialized once, mpfr_set_str/mpfr_acos/mpfr_sprintf) are
// excluded per the reference build config and restated here per the
// H-018 mandatory comment ledger:
//   ifdef BIGFLOAT: gcvt_wrapper(acosl(strtold(b)),a)
//   else MULTI_PRECISION: mpfr_init2(fa,FLT_PRECISION) x2 once;
//     mpfr_set_str(fb,b,10,MPFR_RNDN); mpfr_acos(fa,fb,MPFR_RNDN);
//     mpfr_sprintf(a,"%.8RNg",fa)
// acos is kotlin.math.acos (numeric intrinsic per H-010); the parse
// uses the arm-wide C-protocol strtod (H-009-adjacent decimal-prefix
// contract, MpsExp.kt); gcvt_wrapper is the H-014 stub (numSetup.h
// residue owner). Naming per H-017.

fun mps_acos(a: CharArray, b: CharArray) {
    Stubs.gcvtWrapper(kotlin.math.acos(strtod(CstringOps.cstring(b))), a)
}
