package mumps.interp

import kotlin.math.pow

/**
 * PRO-arm translation of interp-arith.cpp::mps_exp10 (pilot B2,
 * unit 41, provisional under deviation C-003). Translated
 * independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), reference
 * source lines 853-882 (units.csv: 26 LOC). Same shape as the
 * unit-40 sibling mpsExp (interp-arith.cpp::mps_exp).
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h009 format_string       : conversion is delegated to
 *    gcvt_wrapper (real impl, B1 unit 29 NumSetup.kt; GCVT defined
 *    in the reference config so gcvt_wrapper is plain gcvt).
 *  - h012 unsigned_byte_domain: c/a are NUL-terminated byte domains.
 *  - h017 naming              : mps_exp10 -> mpsExp10 (unit-10
 *    precedent).
 *
 * PREPROCESSOR RESOLUTION (h018, second B2 exercise of the rule -
 * first was unit 40 mps_exp): the vendor body is a 3-way
 * conditional. Reference build config: MULTI_PRECISION NOT defined,
 * BIGFLOAT NOT defined, so the live arm is the plain double branch:
 * gcvt_wrapper(exp10(strtod(a,NULL)), c) - C exp10(x) is 10 to the
 * x, rendered as 10.0.pow(x). The compiled-out arms, documented per
 * the h018 action:
 *  - BIGFLOAT arm (not live): long double aa = exp10l(strtold(a));
 *  - MULTI_PRECISION arm (not live): static mpfr_t fa/fc at
 *    FLT_PRECISION, mpfr_set_str, mpfr_exp2, mpfr_sprintf(c,
 *    "%.8RNg", fc). Vendor quirk kept in this documentation: the
 *    vendor's multi-precision arm calls mpfr_exp2 (2 to the x), not
 *    mpfr_exp10 - apparently copied from the mps_exp2 sibling.
 * No build-substitution macros occur in this body.
 */
fun mpsExp10(c: CharArray, a: CharArray) {
    gcvtWrapper(10.0.pow(strtodC(a)), c)
}
