package mumps.interp

import kotlin.math.log10

/**
 * PRO-arm translation of interp-arith.cpp::mps_log10 (B5 tranche
 * C, unit 146, rank 87). Translated independently from the
 * pristine vendor source (mumps-14-may-2026.tgz, sha256
 * 621deb25...ffdbe), reference source lines 162-186.
 *
 * Identity verified against the study tool
 * (tools/select_study_subset.py): mps_log10, cpp, loc 22,
 * comment_ratio 0.043, pointer_marks 2, control_marks 1,
 * score 9.8 - exact match to wp1/study-subset/units.csv row 147
 * (batch 5).
 *
 * mps_log10(a,b): the MUMPS base-10 log - C log10 over the
 * byte-domain input b, gcvt-formatted into the caller's a
 * buffer.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h009 format_string     : conversion is delegated to
 *    gcvtWrapper (NumSetup.kt:114 shared seat, h014 owner
 *    include/mumpsc/numSetup.h, B1 unit 29).
 *  - h012 unsigned_byte_domain: a/b are NUL-terminated byte
 *    domains; strtod over the input via the shared strtodC
 *    regex seat (MpsAcos.kt:34, internal - reused unmodified).
 *  - h017 naming            : mps_log10 -> mpsLog10 (vendor name
 *    in the header comment, mpsAcos u10 precedent).
 *
 * h018 TWO-LEVEL trichotomy (third occurrence of the
 * preprocessor_conditional family, u139 expx two-level
 * precedent - same structure, journaled):
 *  - Level 1, pair-ifndef MULTI_PRECISION: the reference build
 *    does NOT define MULTI_PRECISION (numSetup unit-29
 *    hardware-double resolution, ArithH MISSED-TRIGGER note;
 *    Float: double per u130) - the ifndef arm is IN, the
 *    mpfr/mpz arm is OUT.
 *  - Level 2, pair-ifdef BIGFLOAT inside it: BIGFLOAT is OUT
 *    (per the reference build config) - the plain-double else
 *    arm is IN.
 *  - Excluded arms journaled, not rendered: the long-double
 *    strtold/log10l arm (BIGFLOAT) and the MPFR arm
 *    (mpfr_init2/FLT_PRECISION static init-once flag,
 *    mpfr_set_str/mpfr_log10/mpfr_sprintf %.8RNg - the h013
 *    static edges live only there).
 */
fun mpsLog10(a: CharArray, b: CharArray) {
    gcvtWrapper(log10(strtodC(b)), a)
}
