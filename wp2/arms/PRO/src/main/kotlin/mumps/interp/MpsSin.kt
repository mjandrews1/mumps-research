package mumps.interp

import kotlin.math.sin

/**
 * PRO-arm translation of interp-arith.cpp::mps_sin (B6 tranche
 * C, unit 179, rank 90). Translated independently from the
 * pristine vendor source (mumps-14-may-2026.tgz, sha256
 * 621deb25...ffdbe), reference source lines 253-277.
 *
 * Identity verified against the study tool
 * (tools/select_study_subset.py): mps_sin, cpp, loc 22,
 * comment_ratio 0.043, pointer_marks 2, control_marks 1,
 * score 9.8 - exact match to wp1/study-subset/units.csv row
 * 179 (batch 6).
 *
 * mps_sin(a, b): the sine intrinsic over decimal strings -
 * parse b as a double, sin it, gcvt-render into a, return
 * (void, no caller consumes a status).
 *
 * h018 TWO-LEVEL trichotomy (u139 expx / u146 mps_log10 /
 * u176 mps_abs / u178 mps_log2 precedent, same file, same
 * structure - journaled):
 *  - Level 1, pair-ifndef MULTI_PRECISION: the reference build
 *    does NOT define MULTI_PRECISION (numSetup unit-29
 *    hardware-double resolution; Float: double per u130) - the
 *    ifndef arm is IN. The excluded MPFR arm (the static
 *    mpfr_t fa/fb/fc with the init-once flag, mpfr_set_str /
 *    mpfr_sin / mpfr_sprintf) is journaled, not rendered.
 *  - Level 2, pair-ifdef BIGFLOAT inside it: BIGFLOAT is OUT
 *    (floatSize double, u130 announce precedent) - the sin /
 *    strtod else arm is IN; the excluded sinl/strtold
 *    long-double arm is journaled, not rendered.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h009 format_string     : conversion is delegated to
 *    gcvtWrapper (NumSetup.kt:114 shared seat, h014 owner
 *    include/mumpsc/numSetup.h, B1 unit 29).
 *  - h012 unsigned_byte_domain: a/b are NUL-terminated byte
 *    domains; strtod over the input via the shared strtodC
 *    regex seat (MpsAcos.kt:34, internal - reused unmodified).
 *  - h015 grammar_collision : pre-push scan incl. cross-file
 *    top-level names: new top-level mpsSin; the vendor
 *    parameter names a/b render unchanged (no Kotlin
 *    collisions) - none found.
 *  - h017 naming            : mps_sin -> mpsSin (vendor name
 *    in the header comment, mpsAcos u10 / mpsLog10 u146 /
 *    mpsAbs u176 / mpsLog2 u178 precedent); sin is the JVM
 *    intrinsic (the C sin).
 */
fun mpsSin(a: CharArray, b: CharArray) {
    gcvtWrapper(sin(strtodC(b)), a)
}
