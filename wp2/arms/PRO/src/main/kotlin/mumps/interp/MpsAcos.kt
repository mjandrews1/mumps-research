package mumps.interp

import kotlin.math.acos

/**
 * PRO-arm translation of interp-arith.cpp::mps_acos (pilot B1, unit 10).
 * Translated independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe).
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h009 format_string       : conversion is delegated to gcvt_wrapper
 *    (h014 stub; owner include/mumpsc/numSetup.h, B1 unit 29).
 *  - h012 unsigned_byte_domain: a/b are NUL-terminated byte domains.
 *  - h017 naming              : mps_acos -> mpsAcos.
 *
 * MISSED TRIGGER (record_miss, second occurrence — T1 now met for the
 * preprocessor_conditional family): the vendor body is a 3-way
 * #ifdef (MULTI_PRECISION / BIGFLOAT / default). The reference build
 * (vendor configure defaults: no --with-long-double, no multi-
 * precision) selects the plain double branch:
 * gcvt_wrapper(acos(strtod(b,NULL)), a). The MPFR and long-double
 * branches are compiled out of the reference binary and omitted here.
 * (First occurrence: unit 4, interp-native-btree.cpp::btree.)
 */
fun mpsAcos(a: CharArray, b: CharArray) {
    gcvtWrapper(acos(strtodC(b)), a)
}

/**
 * C strtod over the byte domain: parse the leading decimal float
 * (optional sign, digits with optional fraction, optional exponent);
 * no valid prefix -> 0.0 (strtod sets no error signal here).
 */
internal fun strtodC(b: CharArray): Double {
    val s = cstringAt(b)
    val m = Regex("^[+-]?(\\d+\\.?\\d*|\\.\\d+)([eE][+-]?\\d+)?").find(s)
        ?: return 0.0
    return m.value.toDouble()
}
