package daf

// Unit 256 (DAF B9 T1, simple): interp-arith.cpp::mps_exp2
// (26 LOC, score 10.2): the \u0024ZEXP2 arithmetic intrinsic, the
// ifdef-web twin of the unit-163 mps_sin family (the same
// MULTI_PRECISION / BIGFLOAT lattice in the same file).
// Consults (2026-11-24T22:34:00Z, nominal): BASE-log rows 39/40
// hit-reuse (the config ifdef web resolves to the reference set:
// MULTI_PRECISION false, BIGFLOAT false, so the MPFR and
// long-double branches are dead and carried as the comment fold
// below), row 40 hit-reuse (strtod -> DafCRuntime.strtod), rows
// 41+43 hit-reuse (gcvt_wrapper on the reference build is plain
// %.8g), rows 99/100 hit-adapt (the out-param c folds to the
// String return; base-2 exponentiation: the JVM exposes no
// direct exp2, so 2.0.pow(x) carries it - a possible last-ulp
// difference from the C library exp2, DOCUMENTED DIVERGENCE).
// Dafny: EXEMPT (the sin-family journal reason: the exp2 result
// overflows IEEE range for large parsed inputs; the vendor feeds
// it verbatim, kept).

fun mpsExp2D(a: CharArray): String =
    DafCRuntime.fmt8g(Math.pow(2.0, DafCRuntime.strtod(DafCRuntime.cstring(a))))

// Reference-build config note (row 39): the C
//   #ifndef MULTI_PRECISION / #ifdef BIGFLOAT / #else double
// aa=exp2(strtod(a,NULL)) lattice selects the exp2(strtod)
// branch; the bigfloat exp2l branch and the MPFR mpfr_exp2 branch
// are dead on the reference build, carried as this comment fold
// only.
