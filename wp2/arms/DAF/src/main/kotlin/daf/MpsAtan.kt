package daf

// R1 stub-completion (2026-11-28, C-020 erratum session, tranche 5):
// interp-arith.cpp::mps_atan (roster row 86, loc 22, simple):
// the \$ZATAN arithmetic intrinsic, the ifdef-web twin of the
// landed unit-23 mps_acos render (MpsAcos.kt, the same
// MULTI_PRECISION / BIGFLOAT lattice in the same file).
// Consults (2026-11-28T00:00:00Z, nominal): the MpsAcos consult
// set verbatim - BASE-log rows 39/40 hit-reuse (the config
// ifdef web resolves to the reference set - MULTI_PRECISION
// false, BIGFLOAT false - so the MPFR and long-double branches
// are dead and carried as the comment fold below); row 40
// hit-reuse (strtod -> DafCRuntime.strtod); rows 41+43
// hit-reuse (gcvt_wrapper on the reference build is plain %.8g
// with no appended decimal point, the row-43 verification);
// rows 99/100 hit-adapt (the out-param a keeps the (bd, in_)
// CharArray write-back carrier of the Zfcns.kt/ZfcnBifs.kt call
// sites - unlike the MpsSqrt T1 fold this release keeps the
// original Unit signature so both call sites stay unchanged);
// kotlin.math.atan reproduces the C library atan on the
// hardware-double path.
// Vendor quirk kept: the return path is the bare vendor return
// with no domain guard (atan is total on doubles; NaN input
// propagates as the C library pathology, the acos-family
// disposition).
// Dafny: EXEMPT (transcendental; the asin-family reason: no
// honest range fact over the full input domain; journal row).
// Release note: the Zfcns.kt stub (out-of-scope owner note
// there) is deleted this session; the Zfcns.kt:1595 and
// ZfcnBifs.kt:1519 call sites resolve here unchanged.

fun mpsAtanD(bd: CharArray, in_: CharArray): Unit {

    // gcvt_wrapper(atan(strtod(b, NULL)), a)
    val r = DafCRuntime.fmt8g(
        kotlin.math.atan(DafCRuntime.strtod(DafCRuntime.cstring(in_))))

    var w = 0
    while (w < r.length) {
        bd[w] = r[w]
        w++
    }
    bd[w] = NUL_D
}

// Reference-build config note (row 39): the C
//   #ifndef MULTI_PRECISION / #ifdef BIGFLOAT / #else gcvt atan
// lattice selects the gcvt_wrapper(atan(strtod(b, NULL)), a)
// branch; the bigfloat atanl branch and the MPFR mpfr_atan
// branch (the static init-once flag f and the %.8RNg
// mpfr_sprintf) are dead on the reference build, carried as
// this comment fold only.
