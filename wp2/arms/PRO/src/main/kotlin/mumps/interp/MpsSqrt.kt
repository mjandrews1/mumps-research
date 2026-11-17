package mumps.interp

import kotlin.math.sqrt

/**
 * PRO-arm translation of interp-arith.cpp::mps_sqrt (B3 unit 89,
 * rank 60). Translated independently from the pristine vendor
 * source (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe),
 * reference source lines 519-553.
 *
 * Identity verified against the study tool (tools/select_study_
 * subset.py): interp-arith.cpp::mps_sqrt, cpp, loc 27,
 * comment_ratio 0.036, pointer_marks 0, control_marks 2, score
 * 7.9, batch 3 - exact match to wp1/study-subset/units.csv row
 * 60. Single overload in the vendor.
 *
 * int mps_sqrt(char* out, char* in): the MUMPS (dollar)sqrt
 * intrinsic - square root of the parsed value, formatted into
 * out; returns 0.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h009 format_string     : the conversion is delegated to
 *    gcvt_wrapper (real, NumSetup unit 29) - reused unmodified.
 *  - h012 unsigned_byte_domain: out/in are NUL-terminated byte
 *    domains; strtod resolves to the real strtodC (unit 10).
 *  - h015 grammar_collision : pre-push scan incl. cross-file
 *    top-level names: new top-level mpsSqrt - no collisions; the
 *    vendor parameter name "in" is a Kotlin hard keyword and is
 *    rendered as inCh (vendor name in the header comment).
 *  - h017 naming            : mps_sqrt -> mpsSqrt (mpsAcos
 *    precedent); the Int return is the vendor's literal 0.
 *  - h018 preprocessor_conditional: the 3-way #ifdef below is
 *    resolved against the reference build config (B1-close rule;
 *    see the paragraph at the end of this KDoc).
 *
 * h018 preprocessor_conditional (CORRECTED at B3 close, was first
 * written as a record_miss): the vendor body is a 3-way #ifdef
 * (MULTI_PRECISION / BIGFLOAT / default). Rule h018 has existed
 * since the B1 close (captured from units 4/10/12/20/29, upgraded
 * unit_tested at the B2 close exercising units 40/41) - so a miss
 * could not apply. The reference build (MULTI_PRECISION and
 * BIGFLOAT undefined) selects the plain double branch
 * gcvt_wrapper(sqrt(strtod(in,NULL)), out); return 0 - translated
 * here. The MPFR arm - including the negative-input numcomp gate
 * with its printf and the NUMERIC_RANGE throw - is compiled out
 * of the reference binary and documented as the excluded arm, per
 * the h018 action (the plain branch has no negative check at all;
 * C sqrt of a negative yields NaN through the same path).
 */
fun mpsSqrt(out: CharArray, inCh: CharArray): Int {
    gcvtWrapper(sqrt(strtodC(inCh)), out)
    return 0
}

