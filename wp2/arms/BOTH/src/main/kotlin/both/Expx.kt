package both

// Unit 139 (BOTH B5 T2, core): interp-arith.cpp::expx (48 LOC).
// Batch_plan ordinal 139: core, LOC 48, score 22.4. The exponent
// intrinsic's three-way preprocessor conditional, resolved against
// the frozen reference build config (h018): MULTI_PRECISION is
// undefined, so the #ifndef is live; inside it, BIGFLOAT is also
// undefined, so the live arm is the plain-double
// gcvt_wrapper(pow(strtod(a,NULL),strtod(b,NULL)),c).
//
// Excluded arms (vendor, on the record as comments only):
//   #ifdef BIGFLOAT (undefined, dead):
//     long double fa=strtold(a,NULL); long double fb=strtold(b,NULL);
//     gcvt_wrapper(powl(fa,fb),c);
//   #ifdef MULTI_PRECISION (undefined, dead): the floatstring branch
//     (static mpfr_t fa,fb,fc, init-once guard f=1, FLT_PRECISION,
//     mpfr_set_str(...,10,MPFR_RNDN), mpfr_pow(fc,fa,fb,MPFR_RNDN),
//     mpfr_sprintf(c,"%.8RNg",fc)) and the integer branch below it
//     (static mpz_t na,nb,nc, atoll(b), gmp_sscanf "%Zd" zero-on-
//     fail, mpz_pow_ui, gmp_sprintf(c,"%Zd",nc)).
//
// Parity note (T1 mps_log10 precedent): C pow vs Kotlin's Math.pow
// agree on the finite/NAN/INF contour the reference build observes
// (nan/inf spelling handled once in the gcvt seat); the C library's
// invalid-domain errno behavior is unobservable through the char
// buffer and is out of scope.
//
// BOTH consults (find_rule before render, events logged):
//   h018 preprocessor_conditional - reused: resolve against the
//     reference build config; translate only the live arm; excluded
//     arms documented as comments (recurring from T1 unit 132).
//   h009 format_string - reused: the result rides the 8-digit
//     gcvtWrapper seat (T1, BothNumRuntime; %.8g-equivalent with
//     trailing mantissa zeros stripped, NaN/Inf per C99).
//   h017 naming - hit: expx name kept; strtod folds to the strtodC
//     seat (prefix regex parse, T1).
internal fun expx(a: String, b: String, c: CharArray) {
    // live arm: pow(fa,fb) over gcvt_wrapper's 8-digit seat
    gcvtWrapper(Math.pow(strtodC(a), strtodC(b)), c)
}
