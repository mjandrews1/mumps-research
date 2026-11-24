package daf

// Unit 252 (DAF B9 T1, simple): interp-arith.cpp::mps_log
// (22 LOC, score 9.8): the \u0024ZLOG arithmetic intrinsic, the
// ifdef-web twin of the unit-132 mps_log10 / unit-162 mps_log2 /
// unit-163 mps_sin renders (the same MULTI_PRECISION / BIGFLOAT
// lattice in the same file).
// Consults (2026-11-24T22:32:30Z, nominal): BASE-log rows 39/40
// hit-reuse (the config ifdef web resolves to the reference set:
// MULTI_PRECISION false, BIGFLOAT false, so the MPFR and
// long-double branches are dead and carried as the comment fold
// below), row 40 hit-reuse (strtod -> DafCRuntime.strtod), rows
// 41+43 hit-reuse (gcvt_wrapper on the reference build is plain
// %.8g with no appended decimal point, the row-43 verification),
// rows 99/100 hit-reuse (the out-param a folds to the String
// return; kotlin.math.ln reproduces the C library log on the
// hardware-double path).
// Vendor quirk kept: log of a nonpositive parsed input is the C
// library's -inf-or-NaN pathology; the render does not guard it
// (the log10-family ledger disposition).
// Dafny: EXEMPT (the log10/log2 spec family covers the declared
// range assumption; this row adds no new spec, the sin-family
// journal reason).

fun mpsLogD(b: CharArray): String =
    DafCRuntime.fmt8g(kotlin.math.ln(DafCRuntime.strtod(DafCRuntime.cstring(b))))

// Reference-build config note (row 39): the C
//   #ifndef MULTI_PRECISION / #ifdef BIGFLOAT / #else gcvt log
// lattice selects the gcvt_wrapper(log(strtod(b, NULL)), a)
// branch; the bigfloat logl branch and the MPFR mpfr_log branch
// (the static init-once flag f and the %.8RNg mpfr_sprintf) are
// dead on the reference build, carried as this comment fold only.
