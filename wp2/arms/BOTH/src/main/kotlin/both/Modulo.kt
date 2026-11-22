package both

// Unit 174 (BOTH B6 T2, core): interp-arith.cpp::modulo (56 LOC).
// Batch_plan ordinal 174: core, LOC 56, score 9.4, complexity
// rank 191. Vendor body interp-arith.cpp:695-751. The modulo
// operator's char-buffer arithmetic: any float operand routes
// to fmod through the gcvt seat, both-integer operands take
// the long remainder arm.
//
// Reference-build preprocessor resolution (h018):
//   - MULTI_PRECISION: UNDEFINED in the reference build -> the
//     mpz/gmp_sscanf/large MP block is excluded, documented
//     below as comments only.
//   - BIGFLOAT: UNDEFINED -> the fmodl/strtold/powl variant of
//     the float arm is excluded; the live float arm is
//     fmod(strtod(a,NULL),strtold(b,NULL)).
//   - intLong: DEFINED (defines.h:31 "#define intLong") ->
//     the LIVE integer arm is long aa/bb via atoi with the
//     "%d" sprintf; the long-long/long-long "%lld" arm is
//     excluded.
//
// floatstring (static inline at interp-arith.cpp:597, NOT a
// corpus unit): seated REAL here per the same-include
// rationale (the unit-167 keyfix precedent: cross-TU-bodies
// are named-owner stubs, same-TU static inline folds are real).
//
// Quirks kept (ledgered):
//   - floatstring accepts only digits, '-', '+', then '.' or
//     'e'/'E' as float markers; any other byte returns 0, plus
//     an empty string returns 0 (f stays 0 across no
//     iterations).
//   - the second return in the vendor else-arm is unreachable
//     dead code (kept on the record, not rendered).
//   - "%d" over a long cc prints the low-order int bytes in C;
//     the Kotlin arm renders the Long remainder and emits the
//     Int-truncated decimal (aa/bb come from atoi so the range
//     is int anyway); the divergence is journaled.
//   - fmod(strtod(a), strtold(b)): strtold narrows through the
//     double argument of fmod; the double-parse parity note of
//     T1 mps_log10 applies (results agree across the finite
//     contour the reference build observes).
//   - C % over negative operands truncates toward zero, which
//     Kotlin's Long % preserves exactly (no quantization).
//   - b == "0" in the integer arm is a vendor SIGFPE UB seat;
//     Kotlin throws ArithmeticException (deterministic
//     quantization, journaled).
//   - NUMERIC_RANGE (libmpscpp.h:74) is thrown only in the
//     excluded MULTI_PRECISION arm; the live arm never raises
//     it, so no const seat is needed here.
//
// Excluded arms (vendor, on the record as comments only):
//   - BIGFLOAT float arm: gcvt_wrapper(fmodl(strtold(a,NULL),
//     strtold(b,NULL)),c).
//   - MULTI_PRECISION float arm: printf "Modulo permited only
//     on integers."; GlobalExceptionCode=NUMERIC_RANGE; throw.
//   - MULTI_PRECISION integer arm: static mpz_t
//     na/nb/nc/zero with the init-once guard, gmp_sscanf "%Zd"
//     zero-on-fail, the zero-divisor printf + NUMERIC_RANGE
//     throw, mpz_mod(nc,na,nb), gmp_sprintf(c,"%Zd",nc).
//
// BOTH consults (find_rule before render, events logged):
//   h018 preprocessor_conditional - reused and hit: three-way
//     resolution (MULTI_PRECISION/BIGFLOAT dead arm, intLong
//     live) with the live arm only rendered, the T1 unit-132
//     expx precedent.
//   h009 format_string - reused and hit: the float result rides
//     the gcvtWrapper seat (T1, BothNumRuntime, %.8g shape);
//     the integer result rides the "%d" fold with the
//     Int-truncation quirk journaled.
//   h014 out_of_scope_call - reused and hit: strtod/atoi fold
//     to the strtodC/atolC seats (T1 promotions), same TU as
//     the vendor static inline floatstring seat.
//   h017 naming - reused and hit: modulo name kept verbatim;
//     floatstring -> floatstringC seat name.

// floatstring: static inline at interp-arith.cpp:597-617 (NOT
// a corpus unit; same-include REAL seat, keyfix precedent).
// Returns 1 only when every byte is a digit, '-' or '+', with
// at least one '.' or 'e'/'E' marker; any other byte gives 0
// immediately.
internal fun floatstringC(p: String): Int {
    var f = 0
    var i = 0
    while (i < p.length) {
        val ch = p[i]
        if (ch in '0'..'9' || ch == '-' || ch == '+') { i++; continue }

        if (ch == '.' || ch == 'e' || ch == 'E') {
            f = 1
            i++
            continue
        }
        return 0
    }
    return f
}

internal fun modulo(a: String, b: String, c: CharArray) {
    // vendor comment kept: this function is not invoked in
    // zfcns.cpp.in; it is called by the modulo operator.

    if (floatstringC(a) != 0 || floatstringC(b) != 0) {
        // live float arm (BIGFLOAT excluded):
        // gcvt_wrapper(fmod(strtod(a,NULL),strtold(b,NULL)),c);
        val fa = strtodC(a)
        val fb = strtodC(b) // strtold narrows into fmod's double arg
        val r = fa % fb // C fmod semantics (sign of dividend)
        gcvtWrapper(r, c)
        return
    } else {
        // live integer arm (intLong DEFINED, defines.h:31):
        // long aa=atoi(a); long bb=atoi(b); long cc=aa%bb;
        // sprintf(c,"%d",cc);
        val aa = atolC(a)
        val bb = atolC(b)
        val cc = aa % bb
        writeC(c, cc.toInt().toString()) // "%d" int-truncation quirk
        return
    }
}
