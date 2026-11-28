package daf

// Unit: interp-arith.cpp::mps_sqrt (roster row 90; DAF R1, the
// C-019 Option A render-close): the \\$ZROOT / ZROOT arithmetic
// intrinsic, the ifdef-web twin of the unit-103 mps_asin render
// (the same MULTI_PRECISION / BIGFLOAT lattice in the same file).
// Consults (2026-11-27, nominal): BASE-log rows 39/40 hit-reuse (the
// config ifdef web resolves to the reference set - MULTI_PRECISION
// false, BIGFLOAT false - so the GMP mpf branch is dead and carried
// as the comment fold below), row 40 hit-reuse (strtod ->
// DafCRuntime.strtod; the numcomp(in,"0")<0 negative guard folds to
// the same parsed value compared numerically against zero -
// numcompDaf, the MpstimeMain seat, is the numeric-compare carrier),
// rows 41+43 hit-reuse (gcvt_wrapper on the reference build is
// plain %.8g with no appended decimal point, the row-43
// verification), rows 99/100 hit-adapt (the out-param out folds to
// the String return; the vendor int return is the constant 0 on
// every live path and folds away; kotlin.math.sqrt reproduces the
// C library sqrt on the hardware-double path).
// Vendor quirks kept: the negative-input guard prints the vendor
// message, sets the exception code, and throws - kept verbatim in
// order (GlobalExceptionCodeD = NUMERIC_RANGE_D, then
// MumpsGlobalExceptionD, the DafInterpStubsT3 carriers); the
// sqrt(0.0) path returns the formatted zero like the vendor.
// Dafny: EXEMPT (the transcendent is irrational-domain on the
// reference path; the negative guard alone has no honest
// decidable core worth a spec; journal row).
// Release note: the Zfcns.kt ZROOT and ZfcnBifs.kt ZROOT call
// sites and this render replace the mpsSqrtD carrier stub
// (out-of-scope owner note there; stub deleted this session per
// the C-019 R1 close).

fun mpsSqrtD(in_: CharArray): String {
    val x = DafCRuntime.strtod(DafCRuntime.cstring(in_))
    if (numcompDaf(DafCRuntime.cstring(in_), "0") < 0) {
        println("Square root of a negative number not permitted.")
        GlobalExceptionCodeD = NUMERIC_RANGE_D
        throw MumpsGlobalExceptionD("mps_sqrt: negative radicand")
    }
    return DafCRuntime.fmt8g(kotlin.math.sqrt(x))
}

// Reference-build config note (row 39): the C
//   #ifndef MULTI_PRECISION / #ifdef BIGFLOAT / #else gcvt sqrt
// lattice selects the gcvt_wrapper(sqrt(strtod(in, NULL)), out)
// branch; the bigfloat sqrtl branch and the GMP mpf_sqrt branch
// (the static init-once flag f, the gmp_sscanf/gmp_sprintf pair,
// and the NUMERIC_RANGE throw) are dead on the reference build,
// carried as this comment fold only - the negative guard itself is
// NOT ifdef-dead and renders live above.
