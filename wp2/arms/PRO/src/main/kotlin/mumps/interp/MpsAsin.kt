package mumps.interp

import kotlin.math.asin

/**
 * PRO-arm translation of interp-arith.cpp::mps_asin (B4 unit 114,
 * rank 91). Translated independently from the pristine vendor
 * source (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe),
 * reference source lines 283-307.
 *
 * Identity verified against the study tool (tools/select_study_
 * subset.py): interp-arith.cpp::mps_asin, cpp, loc 22,
 * comment_ratio 0.043, pointer_marks 2, control_marks 1, score
 * 9.8 - exact match to wp1/study-subset/units.csv row 115.
 *
 * mps_asin(): the arc-sine intrinsic - parse the operand with
 * strtod, take the C asin, render through gcvt_wrapper.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h009 format_string       : conversion is delegated to
 *    gcvt_wrapper - the REAL B1 unit 29 helper (NumSetup.kt),
 *    reused (mps_acos precedent, B1 unit 10).
 *  - h012 unsigned_byte_domain: a/b are NUL-terminated byte
 *    domains; strtod parses over the real MpsAcos strtodC.
 *  - h014 out_of_scope_call   : none new - gcvtWrapper and
 *    strtodC are real reuses; noted for the roster.
 *  - h017 naming              : mps_asin -> mpsAsin.
 *  - h018 preprocessor_conditional: the vendor body is a 3-way
 *    #ifdef (MULTI_PRECISION / BIGFLOAT / default). The
 *    reference build (no --with-long-double, no multi-precision)
 *    selects the plain double branch:
 *    gcvt_wrapper(asin(strtod(b,NULL)), a). The MPFR and
 *    long-double arms are compiled out of the reference binary
 *    and omitted here (mps_acos precedent, same file).
 */
fun mpsAsin(a: CharArray, b: CharArray) {
    gcvtWrapper(asin(strtodC(b)), a)
}
