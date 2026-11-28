package daf

// Unit: interp-arith.cpp::mps_acos (roster row 23; DAF R1, the
// C-019 Option A render-close): the \\$ZACOS arithmetic intrinsic,
// the ifdef-web twin of the unit-103 mps_asin and unit-163 mps_sin
// renders (the same MULTI_PRECISION / BIGFLOAT lattice in the same
// file).
// Consults (2026-11-27, nominal): BASE-log rows 39/40 hit-reuse (the
// config ifdef web resolves to the reference set - MULTI_PRECISION
// false, BIGFLOAT false - so the MPFR and long-double branches are
// dead and carried as the comment fold below), row 40 hit-reuse
// (strtod -> DafCRuntime.strtod), rows 41+43 hit-reuse (gcvt_wrapper
// on the reference build is plain %.8g with no appended decimal
// point, the row-43 verification), rows 99/100 hit-reuse (the
// out-param a folds to the String return; kotlin.math.acos
// reproduces the C library acos on the hardware-double path).
// Vendor quirk kept: the vendor feeds EVERY parsed double to acos
// with no domain guard; the result outside [-1,1] is the C
// library's NaN pathology, kept (the asin-family ledger
// disposition).
// Dafny: EXEMPT (asin-family reason: the transcendent is
// irrational-domain on the reference path; no honest range fact to
// declare for acos over the full input domain, so no spec; journal
// row).
// Release note: the Zfcns.kt ZACOSINE call site and this render
// replace the mpsAcosD carrier stub (out-of-scope owner note there;
// stub deleted this session per the C-019 R1 close).

fun mpsAcosD(b: CharArray): String =
    DafCRuntime.fmt8g(kotlin.math.acos(DafCRuntime.strtod(DafCRuntime.cstring(b))))

// Reference-build config note (row 39): the C
//   #ifndef MULTI_PRECISION / #ifdef BIGFLOAT / #else gcvt acos
// lattice selects the gcvt_wrapper(acos(strtod(b, NULL)), a)
// branch; the bigfloat acosl branch and the MPFR mpfr_acos branch
// (the static init-once flag f and the %.8RNg mpfr_sprintf) are
// dead on the reference build, carried as this comment fold only.
