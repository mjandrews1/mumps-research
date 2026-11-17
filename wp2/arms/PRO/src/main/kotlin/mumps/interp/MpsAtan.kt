package mumps.interp

import kotlin.math.atan

/**
 * PRO-arm translation of interp-arith.cpp::mps_atan (B3 unit 85,
 * rank 93). Translated independently from the pristine vendor
 * source (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe),
 * reference source lines 344-370.
 *
 * Identity verified against the study tool (tools/select_study_
 * subset.py): interp-arith.cpp::mps_atan, cpp, loc 22,
 * comment_ratio 0.043, pointer_marks 2, control_marks 1, score
 * 9.8, batch 3 - exact match to wp1/study-subset/units.csv row
 * 93. Single overload in the vendor.
 *
 * void mps_atan(char* a, char* b): the MUMPS (dollar)atan intrinsic
 * - arc tangent of the parsed value, formatted into a.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h009 format_string     : the conversion is delegated to
 *    gcvt_wrapper (real, NumSetup unit 29) - reused unmodified
 *    (mpsAcos precedent, pilot unit 10).
 *  - h012 unsigned_byte_domain: a/b are NUL-terminated byte
 *    domains; strtod resolves to the real strtodC (unit 10).
 *  - h015 grammar_collision : pre-push scan incl. cross-file
 *    top-level names: new top-level mpsAtan - no collisions.
 *  - h017 naming            : mps_atan -> mpsAtan (mpsAcos
 *    precedent).
 *
 * MISSED TRIGGER (record_miss, preprocessor_conditional family):
 * the vendor body is a 3-way #ifdef (MULTI_PRECISION / BIGFLOAT /
 * default). The reference build (vendor configure defaults: no
 * --with-long-double, no multi-precision) selects the plain
 * double branch: gcvt_wrapper(atan(strtod(b,NULL)), a). The MPFR
 * and long-double branches are compiled out of the reference
 * binary and omitted here. (Family precedent: unit 4 btree,
 * first miss; unit 10 mpsAcos, same shape.)
 */
fun mpsAtan(a: CharArray, b: CharArray) {
    gcvtWrapper(atan(strtodC(b)), a)
}
