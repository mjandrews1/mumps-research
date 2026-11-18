package mumps.interp

import kotlin.math.ceil

/**
 * PRO-arm translation of interp-arith.cpp::mps_ceil (roster row
 * 303, batch 10; units.csv: loc 26, comment_ratio 0.037,
 * pointer_marks 0, control_marks 2, score 7.6, rank 52).
 * Translated independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256
 * 621deb2586f27e741ce6db1a2a86fbddf4e4da40c9c6bb363f84c814f53ffdbe),
 * reference source lines 506-534 - RESOLVES the InterpStubs mpsCeil
 * stub raised at u327 (the batch-10 tag superseded, u141 family
 * precedent; the Zfcn caller binds unchanged).
 *
 * mps_ceil(out, in): Int - the ceil intrinsic over decimal
 * strings; the plain-double arm gcvt-renders ceil(strtod(in)).
 *
 * h018 TWO-LEVEL trichotomy (u139/u146/u178 precedent, same file
 * shape as mpsFloor row 268 / mpsTrunc row 302): pair-ifndef
 * MULTI_PRECISION IN (the MPFR/GMP exclusions journaled); inside
 * it, BIGFLOAT OUT (ceill/strtold arm journaled), ceil/strtod
 * else arm IN. The MULTI_PRECISION arm further splits on
 * strchr(.,.) - the excluded GMP arm (static mpf_t,
 * gmp_sscanf/mpf_ceil/gmp_sprintf, the trailing-zero NUL chop
 * walk) is journaled, not rendered; the vendor's non-'.'
 * strcpy(out,in) fallback belongs to that excluded arm.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h009 format_string     : gcvtWrapper (NumSetup.kt:114
 *    shared seat) delegates the %.8g-class conversion.
 *  - h012 unsigned_byte_domain: out/in NUL-terminated byte
 *    domains; strtodC shared regex seat (MpsAcos.kt:34).
 *  - h014 out_of_scope_call : gcvtWrapper/strtodC seats reused;
 *    the InterpStubs stub REMOVED (resolved here).
 *  - h017 naming            : mps_ceil -> mpsCeil (u10 mpsAcos
 *    camel precedent); ceil is the JVM intrinsic (the C ceil);
 *    Int return per the vendor int signature (return 0).
 */
fun mpsCeil(out: CharArray, in_: CharArray): Int {
    gcvtWrapper(ceil(strtodC(in_)), out)
    return 0 // vendor: return 0 (the plain-double arm)
}
