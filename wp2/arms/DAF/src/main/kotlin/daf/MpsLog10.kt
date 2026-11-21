package daf

// Unit 132 (DAF B5 T1, simple): interp-arith.cpp::mps_log10
// (22 LOC): the \$ZLOG10 arithmetic intrinsic, the ifdef-web twin of
// the unit-103 mps_asin render of the T1 tranche (the same
// MULTI_PRECISION / BIGFLOAT lattice in the same file).
// Consults: BASE-log rows 39/40 hit-reuse (the config ifdef web
// resolves to the reference set - MULTI_PRECISION false, BIGFLOAT
// false - so the MPFR and long-double branches are dead and carried
// as documented notes only; the same-file asin/exp resolutions of
// the lattice are the consulted precedent), row 40 hit-reuse
// (strtod -> DafCRuntime.strtod, the leading-double parse with the
// 0.0 fallback), rows 41+43 hit-reuse (gcvt_wrapper on the reference
// build is plain %.8g with NO appended decimal point, the row-43
// verification), rows 99/100 hit-reuse (the out-param a folds to the
// String return; kotlin.math.log10 reproduces the C library log10 on
// the hardware-double path).
// Vendor quirk kept: log10 of a nonpositive parsed input is the C
// library's -inf/h NaN pathology; the render does not guard it (the
// domain pathology is the vendor shape, ledgered per the asin
// precedent).
// Dafny spec: spec/mps_log10.dfy (mirrors mps_asin.dfy: the log range
// over positives is the declared assumption; the %.8g text format is
// out of scope there - spec-assumption note in the journal).

fun mpsLog10D(b: CharArray): String =
    DafCRuntime.fmt8g(kotlin.math.log10(DafCRuntime.strtod(DafCRuntime.cstring(b))))

// Reference-build config note (row 39): the C
//   #ifndef MULTI_PRECISION / #ifdef BIGFLOAT / #else mpfr
// lattice selects the plain-double log10(strtod(b, NULL)) branch;
// the mpfr_log10/FLT_PRECISION branch and the long-double log10l
// branch are dead on the reference build (carried as this comment
// fold, with the GCVT spelling inside gcvt_wrapper dead per the
// BASE-log row-43 verification).
