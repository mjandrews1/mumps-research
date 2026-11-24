package daf

// Unit 253 (DAF B9 T1, simple): interp-arith.cpp::mps_cos
// (22 LOC, score 9.8): the \u0024ZCOS arithmetic intrinsic, the
// ifdef-web twin of the unit-163 mps_sin render (the same
// MULTI_PRECISION / BIGFLOAT lattice in the same file).
// Consults (2026-11-24T22:33:00Z, nominal): BASE-log rows 39/40
// hit-reuse (the config ifdef web resolves to the reference set:
// MULTI_PRECISION false, BIGFLOAT false, so the MPFR and
// long-double branches are dead and carried as the comment fold
// below), row 40 hit-reuse (strtod -> DafCRuntime.strtod), rows
// 41+43 hit-reuse (gcvt_wrapper on the reference build is plain
// %.8g), rows 99/100 hit-reuse (the out-param a folds to the
// String return; kotlin.math.cos reproduces the C library cos on
// the hardware-double path).
// Vendor quirk kept: the vendor feeds EVERY parsed double to cos
// with no irrational-domain guard; the result outside IEEE range
// is the C library's range pathology, kept (the sin-family
// ledger disposition).
// Dafny: EXEMPT (the sin-family journal reason: no honest range
// fact to declare for cos over the full input domain).

fun mpsCosD(b: CharArray): String =
    DafCRuntime.fmt8g(kotlin.math.cos(DafCRuntime.strtod(DafCRuntime.cstring(b))))

// Reference-build config note (row 39): the C
//   #ifndef MULTI_PRECISION / #ifdef BIGFLOAT / #else gcvt cos
// lattice selects the gcvt_wrapper(cos(strtod(b, NULL)), a)
// branch; the bigfloat cosl branch and the MPFR mpfr_cos branch
// are dead on the reference build, carried as this comment fold
// only.
