package mumps.interp

import kotlin.math.floor

/**
 * PRO-arm translation of include/mumpsc/numSetup.h::<misc> (pilot B1,
 * unit 29). Translated independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), reference source
 * numSetup.h (298 physical lines; units.csv 212 SLOC).
 *
 * Unit scope: numSetup.h is an include-fragment - interp-arith.cpp
 * textually includes it inside add/sub/mult/divx/modulo with 'opcode'
 * pre-defined (interp-arith.cpp:628,662,674,686,771). The port makes
 * the fragment a callable unit: numSetup(opcode, a, b, c), with the
 * opcode values from numSetupDefines.h:1-5.
 *
 * This unit OWNS the gcvt_wrapper macro (interp-arith.cpp:69-77; the
 * reference build defines GCVT and not BIGFLOAT, so the selected arm
 * is gcvt(x, 8, a)). The InterpStubs.gcvtWrapper stub is resolved:
 * the definition below is its implementation (mpsAcos, numcomp and
 * future callers now resolve here).
 *
 * MISSED TRIGGER (preprocessor_conditional, 5th occurrence): the
 * vendor fragment is a maze of #if opcode / #ifdef MULTI_PRECISION /
 * #ifdef BIGFLOAT selects. The reference build (no --with-multi-
 * precision, no --with-long-double) selects the hardware double
 * branches: strtod/floor/gcvt for the floating section and
 * atoll/sprintf("%d") for the integer section; the MPFR and
 * long-double branches are compiled out and omitted here.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h002 simple_output_macro : strtod -> strtodC (MpsAcos.kt,
 *    established unit 10); atoll/sprintf("%d") -> local atollC/
 *    writeCstring helpers; the include-site shape (5 fragments, one
 *    per opcode) collapses into the opcode parameter.
 *  - h009 format_string       : the gcvt(x, 8, a) conversion is
 *    implemented here as the %.8g-equivalent formatter (trailing
 *    zeros stripped, C %g/gcvt style; NaN/Inf per C99 gcvt).
 *  - h012 unsigned_byte_domain: a/b/c byte domains (NUL-terminated).
 *  - h014 out_of_scope_call  : floatstring (Numcomp.kt stub, owner
 *    interp-arith.cpp:597 file residue, batch 3) - reused, not
 *    redefined; gcvt_wrapper stub resolved (this unit is the owner).
 *  - h015 grammar_collision  : pre-push scan incl. cross-file top-
 *    level names: new top-level numSetup/gcvtWrapper (the stub moved
 *    out of InterpStubs.kt to its owner file) + private helpers and
 *    constants - no package collisions.
 *  - h017 naming             : numSetup and gcvtWrapper keep arm-wide
 *    names; NS_* constants keep vendor names (private).
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - For opcode == NS_DIV the floatstring gate is compiled out (the
 *    #if opcode == NS_DIV selects the unconditional block): division
 *    is ALWAYS done in floating point. The port mirrors this.
 *  - int aa = atoll(a) truncates atoll's long long to int; atollC
 *    returns Int with the same truncation.
 *  - The integer-section NS_DIV branch is unreachable through the
 *    header (the float section returns first for NS_DIV) but is
 *    translated anyway, per the vendor source.
 */
fun numSetup(opcode: Int, a: CharArray, b: CharArray, c: CharArray) {
    // floating point section
    val goFloat: Boolean
    if (opcode == NS_DIV) {
        // #if opcode == NS_DIV -> unconditional float block
        goFloat = true
    } else {
        goFloat = floatstring(a) || floatstring(b)
    }

    // if either is floating point, operation is done in floating point
    if (goFloat) {
        val af = strtodC(a)
        val bf = strtodC(b)
        var cf = 0.0
        when (opcode) {
            NS_ADD -> cf = af + bf
            NS_SUB -> cf = af - bf
            NS_MULT -> cf = af * bf
            NS_DIV -> cf = af / bf
            NS_INTDIV -> cf = floor(af / bf)
        }
        gcvtWrapper(cf, c)
        return
    }

    // integer section - both operands are integer
    val aa = atollC(a)
    val bb = atollC(b)
    var cc = 0

    when (opcode) {
        NS_ADD -> { cc = aa + bb; writeCstring(c, cc.toString()); return }
        NS_SUB -> { cc = aa - bb; writeCstring(c, cc.toString()); return }
        NS_MULT -> { cc = aa * bb; writeCstring(c, cc.toString()); return }
        NS_DIV -> {
            // unreachable via the header (float section returns first
            // for NS_DIV); kept per the vendor source
            val af = strtodC(a)
            val bf = strtodC(b)
            gcvtWrapper(af / bf, c)
            return
        }
        NS_INTDIV -> { cc = aa / bb; writeCstring(c, cc.toString()); return }
    }
}

/**
 * gcvt_wrapper macro, reference-build arm: gcvt(x, 8, a)
 * (interp-arith.cpp:69-77, GCVT defined, BIGFLOAT not defined).
 * Resolves the former InterpStubs.gcvtWrapper h014 stub - this unit
 * is its owner. C gcvt(x, 8, buf) is equivalent to sprintf(buf,
 * "%.8g", x) with the same trailing-zero/trailing-point stripping.
 */
fun gcvtWrapper(x: Double, a: CharArray) {
    val s = formatG8(x)
    for (i in s.indices) a[i] = s[i]
    a[s.length] = NUL
}

/** C "%.8g" formatting (the gcvt reference arm), trailing zeros stripped. */
private fun formatG8(x: Double): String {
    if (x.isNaN()) return "nan"
    if (x.isInfinite()) return if (x > 0) "inf" else "-inf"

    var s = String.format("%.8g", x)

    // strip trailing zeros in the mantissa only (never the exponent)
    val ei = s.indexOf('e')
    val mantissa = if (ei < 0) s else s.substring(0, ei)
    var stripped = mantissa
    if (stripped.contains('.')) {
        stripped = stripped.trimEnd('0')
        if (stripped.endsWith(".")) stripped = stripped.dropLast(1)
    }
    return if (ei < 0) stripped else stripped + s.substring(ei)
}

/**
 * C atoll over the byte domain, truncated to Int (vendor:
 * int aa = atoll(a)). Parses optional whitespace/sign and the
 * leading digits; no valid prefix -> 0.
 */
private fun atollC(s: CharArray): Int {
    var i = 0
    while (s[i] == ' ' || s[i] == '\t' || s[i] == '\n' || s[i] == '\r') i++
    var neg = false
    if (s[i] == '+' || s[i] == '-') {
        neg = s[i] == '-'
        i++
    }
    var v = 0L
    while (s[i] in '0'..'9') {
        v = v * 10 + (s[i] - '0')
        i++
    }
    if (neg) v = -v
    return v.toInt() // C long long -> int truncation
}

/** numSetupDefines.h:1 - #define NS_ADD 1. */
private const val NS_ADD = 1

/** numSetupDefines.h:2 - #define NS_SUB 2. */
private const val NS_SUB = 2

/** numSetupDefines.h:3 - #define NS_MULT 3. */
private const val NS_MULT = 3

/** numSetupDefines.h:4 - #define NS_DIV 4. */
private const val NS_DIV = 4

/** numSetupDefines.h:5 - #define NS_INTDIV 5. */
private const val NS_INTDIV = 5
