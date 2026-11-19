package doc

// Unit 20 (B1 tranche 3): interp-arith.cpp::numcomp (batch 1, core
// stratum; units.csv row 13: loc 49, score 28.3, rank 202). strcmp-
// style numeric comparison of two digit strings: if either operand
// looks like a float the values compare as doubles (strtod; the
// BIGFLOAT long double arm is the H-009/H-018 reference-build ledger
// note - the reference build compiles without BIGFLOAT), otherwise
// the operands compare as parsed integers (atoll narrowed to int per
// the H-010 promotion note - the vendor writes the atoll result into
// an int; the private atoll helper parses the C atoll leading-decimal
// domain). The MULTI_PRECISION (GMP mpf/mpz) arm is excluded per the
// reference build (H-018 arms ledger). floatstring is a below-roster
// helper (interp-arith.cpp:597 static inline) carried as an H-014
// Stubs seat; strtod reuses the MpsExp.kt internal helper. Return
// domain verbatim: 0 equal, 1 aa greater, -1 aa smaller. Naming per
// H-017.

fun numcomp(aa: CharArray, bb: CharArray): Int {
    if (Stubs.floatstring(aa) || Stubs.floatstring(bb)) {
        // BIGFLOAT arm: the reference build compiles the strtod
        // (double) body; the long double strtold arm is the ledger
        val fa = strtod(CstringOps.cstring(aa))
        val fb = strtod(CstringOps.cstring(bb))
        if (fa == fb) return 0
        if (fa > fb) return 1 else return -1
    } else {
        val fa = atollShared(CstringOps.cstring(aa)).toInt()
        val fb = atollShared(CstringOps.cstring(bb)).toInt()
        if (fa == fb) return 0
        if (fa > fb) return 1 else return -1
    }
}

// C atoll leading-integer domain: optional sign then decimal digits,
// stopping at the first non-digit; no overflow domain guard (the
// vendor relies on atoll's unspecified saturation, noted). Shared
// with the unit-29 numSetup render (below-roster helper seat).
internal fun atollShared(s: String): Long {
    var i = 0
    while (i < s.length && (s[i] == ' ' || s[i] == '\t' ||
            s[i] == '\n' || s[i] == '\r')) i++
    var sign = 1L
    if (i < s.length && (s[i] == '+' || s[i] == '-')) {
        if (s[i] == '-') sign = -1L
        i++
    }
    var v = 0L
    while (i < s.length && s[i] in '0'..'9') {
        v = v * 10 + (s[i] - '0')
        i++
    }
    return sign * v
}
