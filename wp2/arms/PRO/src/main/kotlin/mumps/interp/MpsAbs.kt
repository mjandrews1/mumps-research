package mumps.interp

import kotlin.math.abs

/**
 * PRO-arm translation of interp-arith.cpp::mps_abs (B6 tranche
 * C, unit 176, rank 115). Translated independently from the
 * pristine vendor source (mumps-14-may-2026.tgz, sha256
 * 621deb25...ffdbe), reference source lines 557-595.
 *
 * Identity verified against the study tool
 * (tools/select_study_subset.py): mps_abs, cpp, loc 34,
 * comment_ratio 0.029, pointer_marks 0, control_marks 4,
 * score 11.6 - exact match to wp1/study-subset/units.csv row
 * 176 (batch 6).
 *
 * mps_abs(in, out): the abs intrinsic over decimal strings - a
 * mod b's unary cousin in the same file: parse in as a double,
 * fabs it, gcvt-render into out, return 0.
 *
 * h018 TWO-LEVEL trichotomy (u139 expx / u146 mps_log10
 * precedent, same file, same structure - journaled):
 *  - Level 1, pair-ifndef MULTI_PRECISION: the reference build
 *    does NOT define MULTI_PRECISION (numSetup unit-29
 *    hardware-double resolution; Float: double per u130) - the
 *    ifndef arm is IN. The excluded mpz/mpf arm (the strchr '.'
 *    dispatch with the static mpf_t x / mpz_t na init-once
 *    flags, gmp_sscanf/gmp_sprintf) is journaled, not rendered.
 *  - Level 2, pair-ifdef BIGFLOAT inside it: BIGFLOAT is OUT
 *    (floatSize double, u130 announce precedent) - the fabs /
 *    strtod else arm is IN; the excluded fabsl/strtold
 *    long-double arm is journaled, not rendered.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h009 format_string     : conversion is delegated to
 *    gcvtWrapper (NumSetup.kt:114 shared seat, h014 owner
 *    include/mumpsc/numSetup.h, B1 unit 29).
 *  - h012 unsigned_byte_domain: in/out are NUL-terminated byte
 *    domains; strtod over the input via the shared strtodC
 *    regex seat (MpsAcos.kt:34, internal - reused unmodified).
 *  - h015 grammar_collision : pre-push scan incl. cross-file
 *    top-level names: new top-level mpsAbs; the vendor
 *    parameter name in collides with the Kotlin keyword and
 *    renders as in_ (u172 _extract precedent) - no collisions.
 *  - h017 naming            : mps_abs -> mpsAbs (vendor name
 *    in the header comment, mpsAcos u10 / mpsLog10 u146
 *    precedent); fabs is the JVM abs intrinsic (the C fabs).
 */
fun mpsAbs(in_: CharArray, out: CharArray): Int {
    gcvtWrapper(abs(strtodC(in_)), out)
    return 0
}
