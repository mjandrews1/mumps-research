package daf

// Unit 162 (DAF B6 T1, simple): interp-arith.cpp::mps_log2
// (22 LOC, score 9.8, rank 86): the \$ZLOG2 arithmetic intrinsic, the
// ifdef-web twin of the unit-132 mps_log10 render of B5 T1 (the same
// MULTI_PRECISION / BIGFLOAT lattice in the same file).
// Consults (2026-11-22T15:48:30Z, nominal): BASE-log rows 39/40
// hit-reuse (the config ifdef web resolves to the reference set (the
// same-file log10/asin resolutions): MULTI_PRECISION false, BIGFLOAT
// false, so the MPFR and long-double branches are dead and carried
// as the comment fold below), row 40 hit-reuse (strtod ->
// DafCRuntime.strtod, the leading-double parse with the 0.0
// fallback), rows 41+43 hit-reuse (gcvt_wrapper on the reference
// build is plain %.8g with NO appended decimal point, the row-43
// verification), rows 99/100 hit-reuse (the out-param a folds to the
// String return; kotlin.math.log2 reproduces the C library log2 on
// the hardware-double path).
// Vendor quirk kept: log2 of a nonpositive parsed input is the C
// library's -inf-or-NaN pathology; the render does not guard it (the
// domain pathology is the vendor shape, ledgered per the log10
// precedent).
// Dafny spec: spec/mps_log2.dfy (mirrors spec/mps_log10.dfy: the
// range property over positives is the declared assumption; the
// %.8g text format is out of scope there - spec-assumption note in
// the journal).

fun mpsLog2D(b: CharArray): String =
    DafCRuntime.fmt8g(kotlin.math.log2(DafCRuntime.strtod(DafCRuntime.cstring(b))))

// Reference-build config note (row 39): the C
//   #ifndef MULTI_PRECISION / #ifdef BIGFLOAT / #else gcvt log2l
// lattice selects the gcvt_wrapper(log2(strtod(b, NULL)), a) branch;
// the bigfloat log2l branch and the MPFR mpfr_log2 branch (with the
// static init-once flag f and the %.8RNg mpfr_sprintf) are dead on
// the reference build, carried as this comment fold only.
