package daf

import kotlin.math.pow

// Unit 140 (DAF B5 T2, core): interp-arith.cpp::expx
// (units.csv row 140: loc 48, ptr 3, score 22.4, rank 176, core): the
// runtime exponent intrinsic (the \$\$ exponent of the arithmetic
// set), the third member of the interp-arith.cpp ifdef-lattice family
// already rendered as units 103 (mps_asin) and 132 (mps_log10).
// Consults: BASE-log row 39 hit-reuse (the compiled-out lattice:
// MULTI_PRECISION false and BIGFLOAT false on the reference build,
// so the MPFR mpz/mpfr_t ladder AND the long-double strtold/
// powl/qgcvt branch are DEAD, carried as documented notes only -
// same-file verification against the asin/log10 resolutions), row 40
// hit-reuse (strtod -> DafCRuntime.strtod x2; the leading-double
// parse with the 0.0 fallback), rows 41+43 hit-reuse
// (gcvt_wrapper(x,a) on the reference build resolves to gcvt(x,8),
// i.e. the plain %.8g family with NO appended decimal point per the
// row-43 verification; the wrapper's second argument is ignored on
// the glibc macro arm - logged manual-decision, no BASE row for the
// ignored-arc quirk), row 99 hit-reuse (the mps_exp/mps_exp10 family
// precedent: pow renders as kotlin.math.pow over the hardware-double
// path).
// Fold: the vendor out-param c (char*, written by gcvt_wrapper)
// folds to the String return per the mps_log10 precedent of this
// batch (BASE-log rows 99/100 family).
// Dafny spec: spec/expx.dfy (the pow-is-positivity carrier as a
// DECLARED {:axiom} assumption mirroring mps_log10.dfy; the %.8g
// text format out of scope there - spec-assumption divergence note
// in the journal).

fun expxD(a: CharArray, b: CharArray): String =
    DafCRuntime.fmt8g(
        DafCRuntime.strtod(DafCRuntime.cstring(a))
            .pow(DafCRuntime.strtod(DafCRuntime.cstring(b))))

// Reference-build config note (row 39): the C lattice
//   #ifndef MULTI_PRECISION
//     #ifdef BIGFLOAT  -> strtold/powl/qgcvt
//     #else           -> strtod/pow/gcvt_wrapper   <- reference arm
//   #else (MPFR mpz ladder + floatstring fast path)
// selects the plain-double branch; the MPFR ladder (mpz_pow_ui,
// gmp_sscanf on the operand strings) and the BIGFLOAT long-double
// branch are dead on the reference build and carried as this comment
// fold per the T1 asin/log10 precedents.
