package daf

import kotlin.math.pow

// Unit: interp-arith.cpp::mps_exp10 (roster row 55; DAF R1, the
// C-019 Option A render-close): the \\$ZEXP10 arithmetic intrinsic,
// the ifdef-web twin of the unit-103 mps_asin and unit-163 mps_sin
// renders (the same MULTI_PRECISION / BIGFLOAT lattice in the same
// file).
// Consults (2026-11-27, nominal): BASE-log rows 39/40 hit-reuse (the
// config ifdef web resolves to the reference set - MULTI_PRECISION
// false, BIGFLOAT false - so the MPFR mpfr_exp2 branch and the
// long-double exp10l branch are dead and carried as the comment fold
// below), row 40 hit-reuse (strtod -> DafCRuntime.strtod), rows
// 41+43 hit-reuse (gcvt_wrapper on the reference build is plain
// %.8g with no appended decimal point, the row-43 verification),
// rows 99/100 hit-reuse (the out-param c folds to the String
// return; the vendor's exp10(x) is the 10^x power, so the render
// folds it as kotlin.math.pow(10.0, x), the C library identity on
// the hardware-double path).
// Vendor quirk kept: the vendor computes aa=exp10(strtod(a,NULL))
// into a stack double then formats; the render composes the same
// two steps (the fold documented at the ZEXP10 call site).
// Dafny: EXEMPT (the exp/transcendent family reason; the
// declared-assumption pattern has no honest range fact for 10^x
// over the full input domain; journal row).
// Release note: the Zfcns.kt ZEXP10 call site and this render
// replace the mpsExp10D carrier stub (out-of-scope owner note
// there; stub deleted this session per the C-019 R1 close).

fun mpsExp10D(b: CharArray): String =
    DafCRuntime.fmt8g(10.0.pow(DafCRuntime.strtod(DafCRuntime.cstring(b))))

// Reference-build config note (row 39): the C
//   #ifndef MULTI_PRECISION / #ifdef BIGFLOAT / #else gcvt exp10
// lattice selects the double aa=exp10(strtod(a,NULL)) branch; the
// bigfloat exp10l branch and the MPFR mpfr_exp2 branch (the static
// init-once flag f and the %.8RNg mpfr_sprintf) are dead on the
// reference build, carried as this comment fold only.
