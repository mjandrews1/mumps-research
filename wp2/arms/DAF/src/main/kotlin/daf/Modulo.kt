package daf

// Unit 174 (DAF B6 T2, core): interp-arith.cpp::modulo
// (56 LOC, score 25.6, rank 191): the operator-backing modulo of
// the arithmetic intrinsic family: the float-string dispatch, the
// fmod path over the reference double parse, the intLong integer
// path, and the dead MULTI_PRECISION mpz lattice with its zero-
// divisor guard.
// Consults (2026-11-22T16:04:30Z, nominal): BASE-log rows 39/40
// hit-reuse (the compiled-out comment at the vendor head notes the
// zfcns.cpp.in non-invocation; the reference build pins
// MULTI_PRECISION false and BIGFLOAT false, so the live body is
// the floatstring dispatch with the fmod(strtod) path and the
// intLong integer path; the fmodl branch and the whole #else mpz
// lattice (static-init-once na/nb/nc/zero, %Zd parses with the
// parse-fail rebind to 0, mpz_cmp zero-divisor guard + NUMERIC_
// RANGE throw, mpz_mod, %Zd print) are dead, carried as the
// comment fold below), row 37 hit-reuse (floatstring raised below
// as the named-owner stub floatstringDaf: a static inline at
// interp-arith.cpp:597 of the same TU, NOT a corpus unit - nine
// vendor call sites, all intrinsic variants of this file), row 28
// hit-reuse (the int path parses with atoi), rows 41+43+26
// hit-reuse (the gcvt_wrapper fold is fmt8g; the integer path
// sprintf %d renders as toString with Locale.US irrelevant - digits
// only), rows 99/100 hit-reuse (the out-param c folds to the String
// return).
// VENDOR QUIRKS kept: (1) the fmod path parses a with strtod but
// b with strtold (interp-arith.cpp:705) - on the reference
// 64-bit Linux build both are the double parse, and the render
// folds both onto DafCRuntime.strtod, the mix ledgered; (2) the
// intLong lattice is UNUSUALLY DEFINED (a plain #define at
// interp-arith.cpp:59, not an ifdef config) - the long branch is
// live and the long long branch below it is dead, WITH an
// unreachable double return after each branch kept as source
// residue (the comment fold); (3) the long cc prints with %d (the
// 32-bit low-word print of a long) - the render prints the Int
// domain directly (the atoi parse is Int-valued at this seat, so
// the observable is identical; the %d-of-long divergence is
// ledgered); (4) zero-divisor integer modulo is C UB (no guard on
// the int path; the guard exists only on the DEAD mpz path) - the
// render throws ArithmeticException naturally via the Kotlin %
// - the UB-to-exception divergence ledgered.
// Dafny: EXEMPT (float-string mediation over parse seats; journal
// row).

// int floatstring(const char *p) - named-owner stub (a static
// inline helper at interp-arith.cpp:597 of the same TU; not a
// corpus unit; nine intrinsic call sites).
fun floatstringDaf(p: String): Int =
    throw NotImplementedError(
        "floatstring: out-of-scope owner (interp-arith.cpp:597 static inline; not a corpus unit)")

fun moduloD(a: CharArray, b: CharArray): String {

    // if (floatstring(a) || floatstring(b)) -> the fmod path
    if (floatstringDaf(DafCRuntime.cstring(a)) != 0
        || floatstringDaf(DafCRuntime.cstring(b)) != 0) {

        // gcvt_wrapper(fmod(strtod(a,NULL),strtold(b,NULL)),c)
        // (the strtod/strtold mix collapses: both are the double
        // parse on the reference build; quirk ledgered above.)
        val x = DafCRuntime.strtod(DafCRuntime.cstring(a)) % DafCRuntime.strtod(DafCRuntime.cstring(b))
        return DafCRuntime.fmt8g(x) // gcvt_wrapper fold (row 43)

    } else {

        // #ifdef intLong (defined at interp-arith.cpp:59 - live)
        // long aa=atoi(a); long bb=atoi(b); long cc=aa%bb;
        // sprintf(c,"%d",cc);
        val aa = atoi(DafCRuntime.cstring(a))
        val bb = atoi(DafCRuntime.cstring(b))
        val cc = aa % ccMod(bb) // C % truncation; UB on bb==0 ledger
        return cc.toString() // sprintf(c,"%d",cc)

        // return; return; (the unreachable double return is source
        // residue kept as this comment fold; the #else long long
        // branch below it is dead: atoll parses into Long, %lld
        // print - the intLong define selects the branch above.)
    }
}

// the bb==0 integer modulo is C UB with no guard on the int path;
// the Kotlin % renders the UB as the natural ArithmeticException
// (the UB-to-exception divergence ledgered in the header).
private fun ccMod(bb: Int): Int = bb
