package daf

// Unit 254 (DAF B9 T1, simple): interp-arith.cpp::mps_tan
// (22 LOC, score 9.8): the \u0024ZTAN arithmetic intrinsic, the
// ifdef-web twin of the unit-163 mps_sin / unit-253 mps_cos
// renders (the same MULTI_PRECISION / BIGFLOAT lattice in the
// same file).
// Consults (2026-11-24T22:33:30Z, nominal): BASE-log rows 39/40
// hit-reuse (the config ifdef web resolves to the reference set:
// MULTI_PRECISION false, BIGFLOAT false, so the MPFR and
// long-double branches are dead and carried as the comment fold
// below), row 40 hit-reuse (strtod -> DafCRuntime.strtod), rows
// 41+43 hit-reuse (gcvt_wrapper on the reference build is plain
// %.8g), rows 99/100 hit-reuse (the out-param a folds to the
// String return; kotlin.math.tan reproduces the C library tan on
// the hardware-double path).
// Vendor quirk kept: tan at the odd multiples of pi/2 is the C
// library's huge-value pathology, kept (the sin-family ledger
// disposition; the parsed-double parse feeds it verbatim).
// Dafny: EXEMPT (the sin-family journal reason).

fun mpsTanD(b: CharArray): String =
    DafCRuntime.fmt8g(kotlin.math.tan(DafCRuntime.strtod(DafCRuntime.cstring(b))))

// Reference-build config note (row 39): the C
//   #ifndef MULTI_PRECISION / #ifdef BIGFLOAT / #else gcvt tan
// lattice selects the gcvt_wrapper(tan(strtod(b, NULL)), a)
// branch; the bigfloat tanl branch and the MPFR mpfr_tan branch
// are dead on the reference build, carried as this comment fold
// only.
