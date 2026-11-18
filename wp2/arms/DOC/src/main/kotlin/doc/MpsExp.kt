package doc

// Unit 40: interp-arith.cpp::mps_exp (batch 2, simple stratum).
// Conditional-compilation is resolved against the vendor reference build
// configuration (no MULTI_PRECISION, no BIGFLOAT): the plain double arm is
// the live code; the excluded arms are kept as documentary comments in
// emission order. The double parse is the C strtod decimal-prefix
// contract. The preprocessor_conditional construct has no cookbook entry
// (consult miss on the closest entry, H-003); noted as a capture
// candidate for the batch-close checklist.

fun mpsExp(c: CharArray, a: String) {
    // BIGFLOAT arm (excluded by the reference config):
    //   long double aa = expl(strtold(a, NULL)); gcvt_wrapper(aa, c)
    // MULTI_PRECISION arm (excluded):
    //   mpfr statics; mpfr_set_str(fa, a, 10, MPFR_RNDN); mpfr_exp(...);
    //   mpfr_sprintf(c, "%.8RNg", fc)
    val aa = kotlin.math.exp(strtod(a))
    Stubs.gcvtWrapper(aa, c)
    return
}

// C strtod contract: optional sign, decimal digits with optional fraction
// and exponent, leading whitespace skipped; the unparsed suffix is not
// needed by these units (a NULL endptr call).
internal fun strtod(a: String): Double {
    var i = 0
    while (i < a.length && (a[i] == ' ' || a[i] == '\t' || a[i] == '\n' ||
                a[i] == '\r' || a[i] == '\u000c')) i++
    val start = i
    if (i < a.length && (a[i] == '+' || a[i] == '-')) i++
    var sawDigit = false
    while (i < a.length && a[i] in '0'..'9') { i++; sawDigit = true }
    if (i < a.length && a[i] == '.') {
        i++
        while (i < a.length && a[i] in '0'..'9') { i++; sawDigit = true }
    }
    if (!sawDigit) return 0.0
    if (i < a.length && (a[i] == 'e' || a[i] == 'E')) {
        var j = i + 1
        var expOk = false
        if (j < a.length && (a[j] == '+' || a[j] == '-')) j++
        var any = false
        while (j < a.length && a[j] in '0'..'9') { j++; any = true }
        if (any) { expOk = true; i = j }
        // invalid exponent: the whole exponent part is unread (strtod would
        // parse only the mantissa; the unparsed tail is irrelevant here)
    }
    return a.substring(start, i).toDouble()
}
