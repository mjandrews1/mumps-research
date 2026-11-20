package doc

// Unit 174 (B6 tranche 2): interp-arith.cpp::modulo (batch 6,
// core stratum; LOC 56, score 25.6) - the modulo operator arm
// (called by the modulo operator; "not invoked in zfcns" per
// the vendor's own file-top comment, kept). Render in the
// unit-139 expx / mps_log10 family: the floatstring gate (the
// Stubs.floatstring seat, interp-arith.cpp:597 inline, not a
// corpus unit) dispatches the float arm via strtod and the
// gcvt_wrapper H-014 seat; the integer arm folds the atoi pair
// into the Long pair. H-018 trichotomy journaled per the expx
// precedent: MULTI_PRECISION out (the mpz/static-init arms and
// the floatstring-reject throw documentary), BIGFLOAT out (the
// fmodl long-double arm documentary), and the intLong ifdef
// resolves IN because interp-arith.cpp:59 defines it
// UNCONDITIONALLY in the file itself (the line-57 "if 32 bit
// wanted" comment is a vendor fossil); the excluded long-long
// atoll/sprintf-percent-lld arm is journaled. Vendor notes kept:
// the strtod/strtold MIX on the second operand (a precision
// slip that narrows to double at the fmod call anyway,
// quantized to Double; JVM percent == C fmod with the sign of
// the dividend); the dead second return of both integer arms
// (h013, dropped with this note); the bb==0 integer divisor is
// C UB/SIGFPE, quantized to ArithmeticException (the float arm
// is the reachable zero-divisor path and fmod-by-0 is NaN);
// sprintf percent d renders as the long toString fold (H-009;
// atoi operands bound the remainder to the int range).
// Naming per H-017 (modulo kept verbatim).

fun modulo(a: CharArray, b: CharArray, c: CharArray) {
    // MULTI_PRECISION arms (excluded by the reference config):
    //   float reject: printf("\nModulo permited only on
    //   integers.\n"); NUMERIC_RANGE throw
    //   mpz: static na/nb/nc/zero; gmp_sscanf; the zero-divisor
    //   printf + NUMERIC_RANGE throw; mpz_mod; gmp_sprintf %Zd
    if (Stubs.floatstring(a) || Stubs.floatstring(b)) {
        // BIGFLOAT arm (excluded): fmodl(strtold(a), strtold(b))
        val x = strtod(CstringOps.cstring(a)) %
            strtod(CstringOps.cstring(b)) // JVM percent == C fmod
        Stubs.gcvtWrapper(x, c)
        return
    }

    // intLong arm (IN; the long-long arm journaled excluded)
    val aa = atoi(CstringOps.cstring(a)).toLong()
    val bb = atoi(CstringOps.cstring(b)).toLong()

    val cc = aa % bb

    CstringOps.strcpyS(c, cc.toString())
    return
}
