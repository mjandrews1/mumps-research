package daf

// Unit 257 (DAF B9 core stratum opener): interp-arith.cpp::mps_floor
// (30 LOC, score 10.6): the \u0024ZFLOOR arithmetic intrinsic, the
// ifdef-web twin of the unit-163 mps_sin family (the same
// MULTI_PRECISION / BIGFLOAT lattice in the same file).
// Consults (2026-11-24T22:34:30Z, nominal): BASE-log rows 39/40
// hit-reuse (the config ifdef web resolves to the reference set:
// MULTI_PRECISION false, BIGFLOAT false, so the GMP branch (the
// strchr('.') guard with the mpf static init and the %Fg
// trailing-zero strip) is dead on the reference build and carried
// as the comment fold below), row 40 hit-reuse (strtod ->
// DafCRuntime.strtod), rows 41+43 hit-reuse (gcvt_wrapper on the
// reference build is plain %.8g), rows 99/100 hit-adapt (the int
// return is the constant 0 on every reference path; the out-param
// folds to the String return).
// Dafny: EXEMPT (the sin-family journal reason: floor is exact on
// the declared subdomain but the %.8g text format is out of spec
// scope; the mps_log spec family carries the format exclusion).

fun mpsFloorD(inArr: CharArray): String =
    DafCRuntime.fmt8g(kotlin.math.floor(DafCRuntime.strtod(DafCRuntime.cstring(inArr))))

// Reference-build config note (row 39): the C
//   #ifndef MULTI_PRECISION / #ifdef BIGFLOAT / #else
// gcvt floor(strtod(in, NULL)) lattice selects the floor branch
// returning 0; the BIGFLOAT floorl branch and the GMP branch (the
// '.'-guard, the mpf_init2 static, the gmp_sscanf %Fg read, the
// gmp_sprintf write and the trailing-zero strip loop) are dead on
// the reference build, carried as this comment fold only.
