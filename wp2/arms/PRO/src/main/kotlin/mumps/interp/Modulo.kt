package mumps.interp

/**
 * PRO-arm translation of interp-arith.cpp::modulo (B6 tranche B,
 * unit 166, rank 191). Translated independently from the pristine
 * vendor source (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe),
 * reference source lines 695-762.
 *
 * Identity verified against the study tool (tools/select_study_
 * subset.py): modulo, cpp, loc 56, comment_ratio 0.034,
 * pointer_marks 3, control_marks 6, score 25.6 - exact match to
 * wp1/study-subset/units.csv row 167 (batch 6).
 *
 * modulo(a, b, c): the MUMPS modulo operator arm - a mod b as
 * decimal strings, the result landing in c. The vendor banner
 * comment (697-698) is kept: this function is not invoked in
 * zfcns.cpp.in - it is called by the modulo operator (the
 * Parse_.kt:1074 MODULO seat).
 *
 * h018 reference_build resolution (three preprocessor levels):
 *  - Level 1, pair-ifndef MULTI_PRECISION: NOT defined in the
 *    reference build (numSetup unit-29 resolution, the u139 expx
 *    precedent) - the ifndef arm is IN.
 *  - Level 2, pair-ifdef BIGFLOAT inside it: OUT (floatSize
 *    double, u130/u139 precedent) - the else arm
 *    fmod(strtod(a),strtold(b)) is IN.
 *  - Level 3, pair-ifdef intLong in the integer section: IN -
 *    interp-arith.cpp:59 defines intLong UNCONDITIONALLY in the
 *    file itself (the line-57 comment "defined next if 32 bit
 *    ints wanted" is a vendor fossil; the define stands whatever
 *    the configure options), so the long aa=atoi(a) / percent-d
 *    arm is the compiled arm in every build of this unit. The
 *    excluded long-long atoll / percent-lld arm is journaled
 *    here, not rendered.
 *  The MULTI_PRECISION mpz arm (floatstring reject with the
 *  "Modulo permited only on integers." message, NUMERIC_RANGE
 *  exception, the static init-once mpz pair) is excluded at
 *  level 1 - journaled, not rendered.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h012 unsigned_byte_domain: a/b are C string byte domains
 *    (CharArray + offset carriers); c is the NUL-terminated
 *    output buffer at rOff.
 *  - h013 dead_code        : the vendor integer arm ends with
 *    TWO consecutive return statements (720-721, 727-728) - the
 *    second is unreachable; Kotlin flags unreachable code as a
 *    warning, so only the first is rendered, the fossil noted
 *    here.
 *  - h014 out_of_scope_call : this unit RESOLVES the InterpStubs
 *    modulo stub (the Parse_.kt:1074 MODULO call site binds
 *    unchanged on the offset signature); floatstring (ArithMisc
 *    real seat, u66), gcvtWrapper (NumSetup.kt:114 owner seat,
 *    unit 29) and cstringAt/strcpyC/writeCstring (CstringOps)
 *    are DONE seats - direct calls (atoiC is the shared
 *    CstringOps.kt:35 offset-carrying byte-domain seat); fmod is
 *    the JVM percent
 *    operator (Java percent for doubles IS C fmod semantics -
 *    result carries the sign of the dividend); strtod is the
 *    per-file private
 *    strtodC(b, off) regex helper (u139 Expx.kt precedent).
 *  - h015 grammar_collision  : pre-push scan incl. cross-file
 *    top-level names: the new top-level modulo - the removed
 *    InterpStubs stub clears its seat; no other collisions.
 *  - h017 naming             : modulo keeps the vendor name.
 *  - h018 if_else_resolved  : the three-level ifdef trichotomy
 *    above (reference build: double/long arms in; the BIGFLOAT
 *    long-double, the MULTI_PRECISION mpz, and the long-long
 *    atoll arms out - the mpz static edges and gmp_sscanf live
 *    only in the excluded arms).
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The float arm parses a with strtod but b with strtold
 *    (707) - a mixed-precision vendor slip; both arguments
 *    reach fmod as doubles anyway (the long double narrows at
 *    the call), so the render quantizes both parses to Double
 *    (the JVM has no long double; strtodC is the carrier).
 *  - sprintf(c, percent-d, cc) passes a LONG to a percent-d
 *    conversion (719) - the low-32-bit varargs read is exact
 *    here: aa and bb are atoi results (int range) and the C
 *    remainder is bounded by abs(bb), so cc always fits in int;
 *    the render writes cc.toString() with the same value.
 *  - aa percent bb with bb == 0 is C UB (the hardware raises
 *    SIGFPE); Kotlin quantizes it to an ArithmeticException at
 *    the same seat (u128 deterministic-UB precedent) - the
 *    float arm is the reachable one for zero divisors (fmod by
 *    0.0 is NaN, gcvt renders "nan").
 *  - gcvt_wrapper resolves to plain gcvt in the reference build
 *    (GCVT defined, unit-31 journal) - the gcvtWrapper seat
 *    writes index 0 of its buffer, so the result rides scratch
 *    + strcpyC to the caller's offset carrier (u98/u139
 *    precedent); the integer arm writes rOff directly via
 *    writeCstring (sprintf's index-0 contract on c).
 */
fun modulo(a: CharArray, aOff: Int, b: CharArray, bOff: Int,
           r: CharArray, rOff: Int) {
    // reference arm: #ifndef MULTI_PRECISION
    if (floatstring(a, aOff) || floatstring(b, bOff)) {
        // #else of BIGFLOAT: gcvt_wrapper(fmod(strtod(a),strtold(b)),c)
        val fa = strtodC(a, aOff)
        val fb = strtodC(b, bOff) // vendor strtold on b - quantized to Double
        // JVM percent == C fmod (sign of the dividend; 0.0 divisor -> NaN)
        val scratch = CharArray(64)
        gcvtWrapper(fa % fb, scratch)
        strcpyC(r, cstringAt(scratch), rOff)
        return
    } else {
        // #ifdef intLong (interp-arith.cpp:59, unconditional)
        val aa = atoiC(a, aOff).toLong() // long aa = atoi(a)
        val bb = atoiC(b, bOff).toLong() // long bb = atoi(b)
        val cc = aa % bb // bb == 0: C UB quantized to ArithmeticException
        writeCstring(r, cc.toString(), rOff) // sprintf(c, "%d", cc)
        return
        // vendor's second return (720-721) is dead code - h013 note
    }
}

/** C strtod from an offset into the byte domain (per-file private,
 *  u139 Expx.kt precedent - the shared internal seat has no offset
 *  carrier). */
private fun strtodC(b: CharArray, off: Int): Double {
    val s = cstringAt(b, off)
    val m = Regex("^[+-]?(\\d+\\.?\\d*|\\.\\d+)([eE][+-]?\\d+)?").find(s)
        ?: return 0.0
    return m.value.toDouble()
}
