package mumps.interp

import kotlin.math.pow

/**
 * PRO-arm translation of interp-arith.cpp::expx (B5 tranche B,
 * unit 139, rank 176). Translated independently from the pristine
 * vendor source (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe),
 * reference source lines 923-968.
 *
 * Identity verified against the study tool (tools/select_study_
 * subset.py): expx, cpp, loc 48, comment_ratio 0.02,
 * pointer_marks 3, control_marks 5, score 22.4 - exact match to
 * wp1/study-subset/units.csv row 140 (batch 5).
 *
 * expx(a, b, c): the exponentiation arm - a to the power b as
 * decimal strings, the result landing in c. The vendor body is a
 * two-level preprocessor trichotomy; the reference build
 * (config.log: DBMS SQLITE, Float double, hardware math in, GMP
 * out) resolves it to the plain-double arm.
 *
 * h018 reference_build resolution (both levels):
 *  - Level 1, pair-ifndef MULTI_PRECISION: the reference build
 *    does NOT define MULTI_PRECISION (the numSetup.h unit-29
 *    resolution - hardware double branches, ArithH MISSED-TRIGGER
 *    note) - the ifndef arm is IN.
 *  - Level 2, pair-ifdef BIGFLOAT inside it: BIGFLOAT is OUT
 *    (floatSize 'double', u130 announce precedent) - the else arm
 *    is IN.
 *  The excluded arms are journaled, not rendered: the BIGFLOAT
 *  long-double arm (strtold/powl/gcvt_wrapper on long double),
 *  and the MULTI_PRECISION arm (mpfr/mpz with the floatstring
 *  dispatch, the static init-once flag f, mpz_pow_ui on
 *  atoll(b)).
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h012 unsigned_byte_domain: a/b are C string byte domains
 *    (CharArray + offset carriers); c is the NUL-terminated
 *    output buffer.
 *  - h014 out_of_scope_call  : this unit RESOLVES the InterpStubs
 *    expx stub raised by u127-era callers (Parse_.kt binds
 *    unchanged on the offset signature); strtodC and
 *    gcvtWrapper are DONE seats (MpsAcos.kt:34 internal,
 *    NumSetup.kt:114) - direct calls; pow is the JVM math
 *    intrinsic (the C pow).
 *  - h015 grammar_collision  : pre-push scan incl. cross-file
 *    top-level names: new top-level expx - the removed stub
 *    clears its seat; no other collisions.
 *  - h017 naming             : expx keeps the vendor name.
 *  - h018 if_else_resolved  : the two-level ifdef trichotomy
 *    above (reference build: double arm in; the mpz/mpfr and
 *    long-double arms out - h013 static-init flags and
 *    atoll/gmp_sscanf edges live only in the excluded arm).
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The vendor returns early (return) in BOTH inner arms; the
 *    MULTI_PRECISION else tail falls out of the function - the
 *    rendered double arm keeps the early return verbatim.
 *  - gcvt_wrapper resolves to plain gcvt in the reference build
 *    (GCVT defined, NumSetup B1 unit-31 journal) - the
 *    gcvtWrapper seat is that resolution; the seat writes index 0
 *    of its buffer, so the result rides scratch + strcpyC to the
 *    caller's offset carrier (Parse_.kt:1018 passes bOff).
 */

fun expx(a: CharArray, aOff: Int, b: CharArray, bOff: Int,
         r: CharArray, rOff: Int) {
    // reference arm: #ifndef MULTI_PRECISION, #else of BIGFLOAT
    val fa = strtodC(a, aOff)
    val fb = strtodC(b, bOff)
    // gcvt_wrapper(pow(fa,fb), c) - the seat writes index 0, so the
    // result lands in scratch and strcpyC moves it to the caller's
    // offset carrier (u98 offset-adapter precedent)
    val scratch = CharArray(64)
    gcvtWrapper(fa.pow(fb), scratch)
    strcpyC(r, cstringAt(scratch), rOff)
    return
}

/** C strtod from an offset into the byte domain (per-file private,
 *  MpsAcos.kt:34 regex + Numcomp.kt:60 offset precedent - the shared
 *  internal seat has no offset carrier). */
private fun strtodC(b: CharArray, off: Int): Double {
    val s = cstringAt(b, off)
    val m = Regex("^[+-]?(\\d+\\.?\\d*|\\.\\d+)([eE][+-]?\\d+)?").find(s)
        ?: return 0.0
    return m.value.toDouble()
}
