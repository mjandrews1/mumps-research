package mumps.interp

/**
 * PRO-arm translation of interp-arith.cpp::numcomp (pilot B1, unit 20).
 * Translated independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), reference source
 * lines 980-1043.
 *
 * numcomp(aa,bb): strcmp()-style numeric comparison (-1 / 0 / 1).
 * If either operand looks like a float, compare as doubles; else
 * compare as integers.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h010 numeric_promotion   : C 'int fa = atoll(...)' truncates the
 *    long long return to int - explicit toInt() (via atoiC, the same
 *    prefix parse; C overflow is UB and library values stay in range).
 *  - h012 unsigned_byte_domain: aa/bb NUL-terminated byte domains.
 *  - h014 out_of_scope_call   : floatstring (static inline helper,
 *    interp-arith.cpp:597, owner of the B3 interp-arith.cpp::<misc>
 *    residual unit) -> stub; delegate when that unit lands.
 *  - h017 naming              : numcomp keeps the vendor name;
 *    strtod/atoll libc calls map to prefix parses (strtodC/atoiC).
 *
 * MISSED TRIGGER (record_miss, preprocessor_conditional, 4th
 * occurrence): the body is a #ifndef MULTI_PRECISION block with an
 * inner #ifdef BIGFLOAT; the reference build (vendor configure:
 * no --with-long-double, no multi-precision) selects the plain
 * double branch, translated here. The MULTI_PRECISION branch
 * (GMP mpf_/mpz_ comparisons) is omitted.
 */
fun numcomp(aa: CharArray, bb: CharArray): Int {

    // #ifndef MULTI_PRECISION (reference build: taken)
    if (floatstring(aa) || floatstring(bb)) { // h014 stub

        // #ifdef BIGFLOAT -> long double strtold; reference build:
        // #else -> double strtod
        val fa = strtodC(aa)
        val fb = strtodC(bb)

        if (fa == fb) return 0
        if (fa > fb) return 1
        return -1
    } else {
        // C: int fa = atoll((char *) aa); — long long to int truncation
        val fa = atoiC(aa) // h010: same prefix parse, int domain
        val fb = atoiC(bb)

        if (fa == fb) return 0
        if (fa > fb) return 1
        return -1
    }
}

/**
 * strtod analog over the byte domain: parses the leading numeric
 * prefix (the floatstring gate already established numeric shape);
 * unparseable input yields 0.0 like strtod's failure return.
 */
private fun strtodC(b: CharArray, off: Int = 0): Double =
    cstringAt(b, off).toDoubleOrNull() ?: 0.0

/**
 * floatstring (interp-arith.cpp:597) was stubbed here until B3 unit
 * 66 (interp-arith.cpp::<misc>) landed; the owner-level definition
 * now lives in ArithMisc.kt and the stub has been removed.
 */
