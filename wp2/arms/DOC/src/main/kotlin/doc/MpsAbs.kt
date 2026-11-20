package doc

// Unit 165 (B6 tranche 2): interp-arith.cpp::mps_abs (batch 6, core
// stratum; LOC 34, score 11.6). The abs intrinsic over decimal
// strings - the mps_log10 template (unit 132) with the fabs fold.
// H-018 trichotomy journaled per the unit-132/139 precedent (same
// file, same structure): MULTI_PRECISION out (the strchr period
// dispatch with the mpf/mpz static init-once arms documentary),
// BIGFLOAT out (the fabsl/strtold long-double arm documentary),
// the plain-double fabs/strtod arm IN - the live reference build.
// The strtod fold reuses the MpsExp seat; Stubs.gcvtWrapper is the
// H-014 seat (numSetup owner, batch 10); JVM kotlin.math.abs is the
// C fabs in the plain-double domain. h015: vendor parameter name
// in -> in_ (Kotlin keyword). The two return-0 statements fold to
// the single tail return (h013 note: the vendor returns 0 from
// each arm; the Kotlin render keeps one). Naming per H-017
// (mps_abs -> mpsAbs, mpsLog10 camel precedent).

fun mpsAbs(in_: String, out: CharArray): Int {
    // BIGFLOAT arm (excluded by the reference config):
    //   gcvt_wrapper(fabsl(strtold(in_, NULL)), out)
    // MULTI_PRECISION arms (excluded):
    //   if strchr(in_, '.'): static mpf_t x; mpf_init2(x,
    //   FLT_PRECISION); gmp_sscanf(in_, "%Fg", x); mpf_abs(x,x);
    //   gmp_sprintf(out, "%.Fg", x)
    //   else: static mpz_t na; mpz_init(na); if (gmp_sscanf(in_,
    //   "%Zd", na) == 0) gmp_sscanf("0","%Zd", na); mpz_abs(na,na);
    //   gmp_sprintf(out, "%Zd", na)
    val x = kotlin.math.abs(strtod(in_))
    Stubs.gcvtWrapper(x, out)
    return 0
}
