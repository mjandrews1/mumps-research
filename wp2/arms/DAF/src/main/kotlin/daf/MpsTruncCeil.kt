package daf

/*
 * DAF batch-10 units 301/302 (roster rows 301, 302;
 * interp-arith.cpp::mps_trunc and mps_ceil, LOC 26 each, ptr 0,
 * score 7.6 each). Consult 2026-11-25T15:11:30Z and
 * 2026-11-25T15:12:00Z.
 *
 * Reference-build quantization: MULTI_PRECISION false, BIGFLOAT
 * false (the MpsFloor.kt unit-17 precedent); the GMP/BIGFLOAT
 * arms of the vendor bodies ride below as comment folds.
 *
 * Fold notes: the trunc() libc call folds onto the
 * if (v<0) ceil else floor pair (DOCUMENTED: the
 * kotlin.math.trunc symbol does not exist on kotlinc 2.1.20 for
 * this build; the pair is the exact trunc definition over IEEE
 * doubles); strtod rides DafCRuntime.strtod; gcvt_wrapper on the
 * reference build is plain percent-8g (row 43 verification;
 * DafCRuntime.fmt8g).
 *
 * H-015: ASCII; no raw dollars; trailing newline.
 * Dafny: EXEMPT (numeric-format seat family; journal row).
 */

// int mps_trunc(char *out, char *in) - interp-arith.cpp
fun mpsTruncD(out: CharArray, in_: CharArray): Int {

    // reference build: gcvt_wrapper(trunc(strtod(in)), out)
    val d = DafCRuntime.strtod(DafCRuntime.cstring(in_))
    val v = if (d < 0) Math.ceil(d) else Math.floor(d) // trunc
    val res = DafCRuntime.fmt8g(v)
    var i = 0
    while (i < res.length && i < out.size - 1) { out[i] = res[i]; i++ }
    out[i] = NUL_D

    // (MULTI_PRECISION arm: the gmp_sscanf/mpf_trunc/
    // gmp_sprintf triple - dead in the reference build, folded
    // per the MpsFloor.kt unit-17 quantization)

    return 0
}

// int mps_ceil(char *out, char *in) - interp-arith.cpp
fun mpsCeilD(out: CharArray, in_: CharArray): Int {

    // reference build: gcvt_wrapper(ceil(strtod(in)), out)
    val d = DafCRuntime.strtod(DafCRuntime.cstring(in_))
    val res = DafCRuntime.fmt8g(Math.ceil(d))
    var i = 0
    while (i < res.length && i < out.size - 1) { out[i] = res[i]; i++ }
    out[i] = NUL_D

    // (MULTI_PRECISION arm: mpf_ceil triple - dead, comment fold)

    return 0
}
