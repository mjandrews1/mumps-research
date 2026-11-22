package daf

// Unit 163 (DAF B6 T1, simple): interp-arith.cpp::mps_sin
// (22 LOC, score 9.8, rank 90): the \$ZSIN arithmetic intrinsic, the
// next ifdef-web twin of the unit-132 mps_log10 and unit-103 mps_asin
// renders of the T1 strata (the same MULTI_PRECISION / BIGFLOAT
// lattice in the same file).
// Consults (2026-11-22T15:49:00Z, nominal): BASE-log rows 39/40
// hit-reuse (the config ifdef web resolves to the reference set (the
// same-file log10/asin resolutions): MULTI_PRECISION false, BIGFLOAT
// false, so the MPFR and long-double branches are dead and carried as
// the comment fold below), row 40 hit-reuse (strtod ->
// DafCRuntime.strtod), rows 41+43 hit-reuse (gcvt_wrapper on the
// reference build is plain %.8g with no appended decimal point, the
// row-43 verification), rows 99/100 hit-reuse (the out-param a folds
// to the String return; kotlin.math.sin reproduces the C library sin
// on the hardware-double path).
// Vendor quirk kept: the vendor feeds EVERY parsed double to sin
// with no irrational-domain guard; the result outside IEEE range is
// the C library's range pathology, kept (the asin-family ledger
// disposition).
// Dafny: EXEMPT (asin-family reason: the transcendent is
// irrational-domain on the reference path; the mps_log10.dfy and
// mps_log2.dfy declared-assumption pattern has no honest range fact
// to declare for sin over the full input domain, so no spec; journal
// row).

fun mpsSinD(b: CharArray): String =
    DafCRuntime.fmt8g(kotlin.math.sin(DafCRuntime.strtod(DafCRuntime.cstring(b))))

// Reference-build config note (row 39): the C
//   #ifndef MULTI_PRECISION / #ifdef BIGFLOAT / #else gcvt sinl
// lattice selects the gcvt_wrapper(sin(strtod(b, NULL)), a) branch;
// the bigfloat sinl branch and the MPFR mpfr_sin branch (the static
// init-once flag f and the %.8RNg mpfr_sprintf) are dead on the
// reference build, carried as this comment fold only.
