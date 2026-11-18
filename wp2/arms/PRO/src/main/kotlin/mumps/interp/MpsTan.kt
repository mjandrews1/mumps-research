package mumps.interp

import kotlin.math.tan

/**
 * PRO-arm translation of interp-arith.cpp::mps_tan (roster row
 * 273, batch 9; units.csv: loc 22, comment_ratio 0.043,
 * pointer_marks 2, control_marks 1, score 9.8, rank 94).
 * Translated independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), reference
 * source lines 375-400 - RESOLVES the InterpStubs mpsTan stub
 * raised at u156 (the Zfcn ztan arm binds unchanged).
 *
 * mps_tan(a, b): the tangent intrinsic over decimal strings;
 * bare trailing return renders as Unit (u178 precedent).
 *
 * h018 TWO-LEVEL trichotomy (u179 mps_sin twin, same file):
 * MULTI_PRECISION OUT (the MPFR static fa/fb/fc arm journaled);
 * BIGFLOAT OUT (the tanl/strtold arm journaled); the plain
 * tan/strtod arm IN.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h009 format_string     : gcvtWrapper (NumSetup.kt:114).
 *  - h012 unsigned_byte_domain: a/b NUL-terminated; strtodC
 *    shared regex seat.
 *  - h015 grammar_collision : a/b kept; new top-level mpsTan
 *    scanned clean cross-file.
 *  - h017 naming            : mps_tan -> mpsTan.
 */
fun mpsTan(a: CharArray, b: CharArray) {
    gcvtWrapper(tan(strtodC(b)), a)
}
