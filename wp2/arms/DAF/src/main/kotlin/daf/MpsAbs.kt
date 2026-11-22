package daf

// Unit 165 (DAF B6 T2, core): interp-arith.cpp::mps_abs
// (34 LOC, score 11.6, rank 115): the \$ZABS arithmetic intrinsic -
// the fourth ifdef-web twin of the mps_asin/log10/log2/sin family
// in the same file, with the widest lattice so far (a three-way
// MULTI_PRECISION-BIGFLOAT web plus an in-branch strchr split over
// the mpf/mpz gmp containers).
// Consults (2026-11-22T16:00:00Z, nominal): BASE-log rows 39/40
// hit-reuse (the config ifdef web resolves to the reference set
// (the same-file twin resolutions): MULTI_PRECISION false, BIGFLOAT
// false, so the live body is gcvt_wrapper(fabs(strtod(in, NULL)))
// with the vendor return 0 folded away; the BIGFLOAT fabsl branch
// and the whole #else arm (the mpf_f: strchr(\'.\') split with the
// static init-once x, gmp_sscanf %Fg, mpf_abs, %.Fg print; and the
// mpz_t branch with %Zd parse, mpz_abs, %Zd print) are dead on the
// reference build, carried as the comment fold below), row 40
// hit-reuse (strtod -> DafCRuntime.strtod), rows 41+43 hit-reuse
// (gcvt_wrapper on the reference build is plain %.8g with no
// appended decimal point, the row-43 verification), rows 99/100
// hit-reuse (the out-param folds to the String return; the int
// return value is the always-0 success code of the intrinsic
// family and folds away exactly as at the log twins).
// Vendor quirk kept: the gmp mpz branch rebinds a failed parse to
// literal 0 (gmp_sscanf(\"0\",...)) - dead on the reference build,
// ledger-only.
// Dafny: EXEMPT (transcendent-family intrinsic; the mps_log10.dfy
// declared-assumption pattern has no honest range fact for abs over
// the full parse domain beyond tautologies; journal row).

fun mpsAbsD(inBuf: CharArray): String =
    DafCRuntime.fmt8g(kotlin.math.abs(DafCRuntime.strtod(DafCRuntime.cstring(inBuf))))

// Reference-build config note (row 39): the C
//   #ifndef MULTI_PRECISION / #ifdef BIGFLOAT fabs strtold / #else
//   and the #else mpf/mpz arms
// lattice selects the fabs(strtod(b, NULL)) branch with return 0;
// the fabsl branch, the mpf_t %.Fg arm and the mpz_t %Zd arm are
// dead on the reference build and carried as this comment fold
// only.
