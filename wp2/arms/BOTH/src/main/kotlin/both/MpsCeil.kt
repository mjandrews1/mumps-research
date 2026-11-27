package both

// Unit 285 (BOTH B10, simple): interp-arith.cpp::mps_ceil
// (26 LOC). Batch_plan ordinal 285; units.csv row 302. The
// mps_floor family shape (the unit-257 precedent): a three-way
// preprocessor conditional resolved against the frozen
// reference build config (h018) - MULTI_PRECISION and
// BIGFLOAT both undefined, so the live arm is
// gcvt_wrapper(ceil(strtod(in,NULL)),out); return 0. The
// vendor returns int 0 on every path; the Kotlin seat returns
// Int to keep the C-visible contract.
//
// Excluded arms (vendor interp-arith.cpp, translated only for
// the record as comments):
//   #ifdef BIGFLOAT (undefined):  gcvt_wrapper(ceill(
//     strtold(in,NULL)),out); return 0;
//   #else (live):                 gcvt_wrapper(ceil(
//     strtod(in,NULL)),out); return 0;
//   #ifdef MULTI_PRECISION (undefined): the mpf block - the
//     strchr(in,'.') guard; when no '.' is present the vendor
//     short-circuits strcpy(out,in) + return 0 (the
//     already-integral fast path); with '.' present: static
//     mpf_t x (init-once f=1, FLT_PRECISION), gmp_sscanf(in,
//     "%Fg",x), mpf_ceil(x,x), gmp_sprintf(out,"%Fg",x),
//     return 0.
//
// BOTH consults (find_rule before render, events logged):
//   h018 preprocessor_conditional - reused and hit: live-arm
//     resolution against the reference build config; the
//     excluded arms documented here only.
//   h009 format_string - reused: the gcvtWrapper seat (%g
//     shape, precision 8, BothNumRuntime.kt).
//   h017 naming - reused: mps_ceil -> mpsCeil; out/in keep
//     vendor names.
//   Dafny: EXEMPT - family lattice (spec/mps_log10.dfy); the
//     ceil call adds no decidable surface beyond the family
//     envelope (the mps_floor precedent, journaled).

// int mps_ceil(char * out, char * in) - interp-arith.cpp
internal fun mpsCeil(out: CharArray, inStr: String): Int {
    gcvtWrapper(kotlin.math.ceil(strtodC(inStr)), out)
    return 0
}
