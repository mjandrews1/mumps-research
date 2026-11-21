package daf

// Unit 103 (DAF B4 T1): interp-arith.cpp::mps_asin (22 LOC): the
// \$ZASIN arithmetic intrinsic. BASE-log rows 39/40/41/43/99/100 form
// the consult family (the mps_acos/mps_exp resolutions of the same
// ifdef web in the same file):
//   row 39 (hit-reuse): the config ifdef web resolves to the reference
//     set - MULTI_PRECISION false, BIGFLOAT false - so the MPFR/long
//     double branches are dead and carried as documented consts only;
//   row 40 (hit-adapt): strtod -> DafCRuntime.strtod (leading-double
//     parse, 0.0 fallback);
//   rows 41+43 (hit-reuse): gcvt_wrapper on the reference build is
//     plain %.8g with NO appended decimal point (the row-43
//     verification against the reference binary);
//   rows 99+100 (hit-reuse): the out-param a folds to the String
//     return (call-shape divergence documented: the caller writes the
//     result into its own buffer); kotlin.math.asin reproduces the C
//     library asin on the hardware-double path.
// Dafny spec: spec/mps_asin.dfy (verified; the asin range axiom is a
// declared assumption there - logged as the spec-assumption note).

fun mpsAsin(b: CharArray): String =
    DafCRuntime.fmt8g(kotlin.math.asin(DafCRuntime.strtod(DafCRuntime.cstring(b))))

// Reference-build config note (row 39): the C
//   #ifndef MULTI_PRECISION / #ifdef BIGFLOAT / #else mpfr
// lattice selects the plain-double asin(strtod(b)) branch; the GCVT
// spelling inside gcvt_wrapper is dead in the reference build per the
// BASE-log row-43 verification.
