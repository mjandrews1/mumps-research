package doc

import kotlin.math.pow

// Unit 139 (B5 tranche 2): interp-arith.cpp::expx (batch 5, core
// stratum; LOC 48, score 22.4) - the general power a**b runtime.
// Same reference-config resolution as the exp/log family per H-018
// (no MULTI_PRECISION, no BIGFLOAT: the plain-double arm is live,
// the strtold/powl and MPFR/GMP arms documentary). The excluded
// MULTI_PRECISION arm's structure is kept as comments, including the
// integer-path floatstring gate over both operands and the gmp
// init-once statics (H-008 static carrier note: the carry
// disappears with the excluded arm - no live seat). Both live-arm
// returns render; the wrapper's Unit return maps the vendor void.
// gcvt_wrapper (GCVT live macro) rides the H-014 stub seat; strtod
// is the arm decimal-prefix helper (unit-40 seat, H-010). Naming per
// H-017 (expx stays lowercase vendor name; Kotlin camel form expx).

fun expx(a: String, b: String, c: CharArray) {
    // BIGFLOAT arm (excluded by the reference config):
    //   long double fa=strtold(a,NULL); long double fb=strtold(b,NULL);
    //   gcvt_wrapper(powl(fa,fb),c); return;
    // MULTI_PRECISION arm (excluded): if (floatstring(a) ||
    // floatstring(b)) { static mpfr_t fa,fb,fc; init-once;
    //   mpfr_set_str(fa,a,10,MPFR_RNDN); mpfr_set_str(fb,b,10,MPFR_RNDN);
    //   mpfr_pow(fc,fa,fb,MPFR_RNDN); mpfr_sprintf(c,"%.8RNg",fc); return; }
    // else { static mpz_t na,nb,nc; init-once; powr=atoll(b);
    //   gmp_sscanf(a,"%Zd",na); gmp_sscanf(b,"%Zd",nb);
    //   mpz_pow_ui(nc,na,powr); gmp_sprintf(c,"%Zd",nc); }
    val fa = strtod(a)
    val fb = strtod(b)
    Stubs.gcvtWrapper(fa.pow(fb), c)
    return
}
