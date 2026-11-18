package mumps.interp

import kotlin.math.floor

/**
 * PRO-arm translation of interp-arith.cpp::mps_floor (roster row
 * 268, batch 9; units.csv: loc 30, comment_ratio 0.032,
 * pointer_marks 0, control_marks 4, score 10.6, rank 103).
 * Translated independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), reference
 * source lines 442-476 - RESOLVES the InterpStubs mpsFloor stub
 * raised at u156 (the u156 batch-10 tag superseded, u141 family
 * precedent; the Zfcn zfloor arm call site binds unchanged).
 *
 * mps_floor(out, in): Int - the floor intrinsic over decimal
 * strings; the plain-double arm gcvt-renders floor(strtod(in)).
 *
 * h018 TWO-LEVEL trichotomy (u139/u146/u178 precedent, same
 * file): pair-ifndef MULTI_PRECISION IN (the MPFR/GMP exclusions
 * journaled); inside it, BIGFLOAT OUT (floorl/strtold arm
 * journaled), floor/strtod else arm IN. The MULTI_PRECISION arm
 * further splits on strchr(.,.) - the excluded GMP arm (static
 * mpf_t, gmp_sscanf/mpf_floor/gmp_sprintf, the trailing-zero NUL
 * chop walk) is journaled, not rendered; the vendor's non-'.'
 * strcpy(out,in) fallback belongs to that excluded arm.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h009 format_string     : gcvtWrapper (NumSetup.kt:114
 *    shared seat) delegates the %.8g-class conversion.
 *  - h012 unsigned_byte_domain: out/in NUL-terminated byte
 *    domains; strtodC shared regex seat (MpsAcos.kt:34).
 *  - h014 out_of_scope_call : gcvtWrapper/strtodC seats reused;
 *    the InterpStubs stub REMOVED (resolved here).
 *  - h017 naming            : mps_floor -> mpsFloor (u10 mpsAcos
 *    camel precedent); floor is the JVM intrinsic (the C
 *    floor); Int return per the vendor int signature (return 0).
 */
fun mpsFloor(out: CharArray, in_: CharArray): Int {
    gcvtWrapper(floor(strtodC(in_)), out)
    return 0 // vendor: return 0 (the plain-double arm)
}
