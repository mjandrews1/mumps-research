package doc

// Unit 67 (B3 tranche 1): interp-arith.cpp::mps_sqrt (batch 3, simple
// stratum; LOC 27, score 7.9). H-018 application: the reference-build
// live arm is the plain-double gcvt path; the BIGFLOAT arm
// (gcvt_wrapper(sqrtl(strtold)),out)) and the MULTI_PRECISION arm
// (static mpf_t initialized once; the negative-input numcomp gate,
// the numcomp printf and the NUMERIC_RANGE throw; gmp_sscanf
// "%Fg"; mpf_sqrt; gmp_sprintf "%Fg") are compiled out per the
// reference build config. Quirk of the live arm, kept: the plain
// double branch has NO negative-input check at all (C sqrt of a
// negative yields NaN through the same path) - the guard exists
// only in the excluded MP arm, so mps_sqrt renders without one.
// The vendor parameter name "in" is a Kotlin hard keyword: it is
// renamed inCh per H-015/H-017 (keyword-collision rename note).
// sqrt is kotlin.math.sqrt (numeric intrinsic per H-010); the parse
// uses the arm-wide C-protocol strtod (MpsExp.kt); gcvt_wrapper is
// the H-014 stub (numSetup.h residue owner). Naming per H-017.

fun mps_sqrt(out: CharArray, inCh: CharArray): Int {
    Stubs.gcvtWrapper(kotlin.math.sqrt(strtod(CstringOps.cstring(inCh))), out)
    return 0
}
