package mumps.interp

import kotlin.math.exp

/**
 * PRO-arm translation of interp-arith.cpp::mps_exp (pilot B2,
 * unit 40, provisional under deviation C-003). Translated
 * independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), reference
 * source lines 783-812 (units.csv: 26 LOC). Same shape as the B1
 * unit-10 sibling mpsAcos (interp-arith.cpp::mps_acos).
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h009 format_string       : conversion is delegated to
 *    gcvt_wrapper (real impl, B1 unit 29 NumSetup.kt; GCVT defined
 *    in the reference config so gcvt_wrapper is plain gcvt).
 *  - h012 unsigned_byte_domain: c/a are NUL-terminated byte domains.
 *  - h017 naming              : mps_exp -> mpsExp (unit-10 precedent).
 *
 * PREPROCESSOR RESOLUTION (h018, first B2 exercise of the rule - the
 * B1 misses on this family were recorded as rule(h018) captures):
 * the vendor body is a 3-way conditional. Reference build config:
 * MULTI_PRECISION NOT defined, BIGFLOAT NOT defined, so the live
 * arm is the plain double branch: gcvt_wrapper(exp(strtod(a,NULL)),
 * c). The compiled-out arms, documented per the h018 action:
 *  - BIGFLOAT arm (not live): long double aa = expl(strtold(a));
 *  - MULTI_PRECISION arm (not live): static mpfr_t fa/fc initialized
 *    once at FLT_PRECISION, mpfr_set_str/fa, mpfr_exp, and
 *    mpfr_sprintf(c, "%.8RNg", fc).
 * No build-substitution macros occur in this body.
 */
fun mpsExp(c: CharArray, a: CharArray) {
    gcvtWrapper(exp(strtodC(a)), c)
}
