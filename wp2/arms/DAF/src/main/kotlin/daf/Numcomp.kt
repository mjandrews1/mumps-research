package daf

// R1 stub-completion (2026-11-28, C-020 erratum session, tranche 6):
// interp-arith.cpp::numcomp (roster row 13, loc 49, core): the
// numeric string comparator of the interpreter arithmetic
// family: if either operand parses as a float-shaped string
// (floatstring: any '.' or 'e'/'E' among digits/signs), the
// double compare (0/1/-1); else the atoll int compare
// (truncated to the C int domain, the vendor int fa/fb).
// The numcomp1/numcomp2 siblings are DIFFERENT functions (not
// this seat; numcomp2D is rendered elsewhere).
// Consults (2026-11-28T00:00:00Z, nominal): BASE-log rows 39/40
// hit-reuse (the config ifdef web resolves to the reference set -
// MULTI_PRECISION false, BIGFLOAT false - so the MPFR/GMP
// lattice is dead and carried as the comment fold below); row
// 37 hit-reuse (floatstring resolves to the raised
// floatstringDaf seat of Modulo.kt - a static inline at
// interp-arith.cpp:597 of the same TU, NOT a corpus unit; called
// exactly as the landed Modulo render calls it); row 40
// hit-reuse (strtod -> DafCRuntime.strtod); row 28 hit-reuse
// (the int path parses with atoll - the C long long parse
// assigned to a C int, the truncation kept via toInt()).
// Vendor quirks kept: (1) the float-shaped branch is taken when
// EITHER operand is float-shaped (the vendor || - an int-shaped
// aa compares as double when bb is float-shaped); (2) the int
// path uses atoll truncated to int (a >2^31 value wraps in C;
// the Kotlin toInt() reproduces the wrap).
// Dafny: EXEMPT (numeric comparator over the raised relay; the
// Modulo family reason; journal row).
// Release note: the MpstimeMain.kt stub is deleted this
// session; the Interpret.kt FOR-loop call sites, the MpsSqrt.kt
// negative guard and the MpstimeMain row already resolve here.

fun numcompDaf(aa: String, bb: String): Int {

    // if (floatstring(aa) || floatstring(bb)) - the float path
    if (floatstringDaf(aa) != 0 || floatstringDaf(bb) != 0) {

        // double fa=strtod(aa,NULL); double fb=strtod(bb,NULL);
        val fa = DafCRuntime.strtod(aa)
        val fb = DafCRuntime.strtod(bb)

        if (fa == fb) return 0
        if (fa > fb) return 1
        return -1
    }

    // int fa=atoll(aa); int fb=atoll(bb) - the C int truncation
    val fa = atollTruncD(aa)
    val fb = atollTruncD(bb)

    if (fa == fb) return 0
    if (fa > fb) return 1
    return -1
}

// long long atoll(const char *) assigned to a C int: the
// truncating parse (the vendor int fa path)
private fun atollTruncD(s: String): Int {
    var i = 0
    while (i < s.length && (s[i] == ' ' || s[i] == '\t')) i++
    var neg = false
    if (i < s.length && (s[i] == '-' || s[i] == '+')) {
        neg = s[i] == '-'
        i++
    }
    var v = 0L
    while (i < s.length && s[i] in '0'..'9') {
        v = v * 10 + (s[i] - '0')
        i++
    }
    return (if (neg) -v else v).toInt()
}

// Reference-build config note (row 39): the C
//   #ifndef MULTI_PRECISION / #ifdef BIGFLOAT / #else strtod
// lattice selects the strtod/strtold double compare (strtold
// collapses onto the double on the reference build); the GMP
// mpf_sscanf/mpz_cmp lattice of the #else arm is dead on the
// reference build, carried as this comment fold only.
