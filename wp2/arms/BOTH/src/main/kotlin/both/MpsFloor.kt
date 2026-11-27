package both

// Unit 257 (BOTH B9, core): interp-arith.cpp::mps_floor (30
// LOC). Batch_plan ordinal 257: core, score 10.6. Three-way
// preprocessor conditional (the mps_sin family shape); the
// frozen reference build config resolves it (h018):
// MULTI_PRECISION and BIGFLOAT are both undefined, so the live
// arm is gcvt_wrapper(floor(strtod(in,NULL)),out); return 0.
// The vendor returns int 0 on every path; the Kotlin seat
// returns Int to keep the C-visible contract.
//
// Excluded arms (vendor interp-arith.cpp, translated only for
// the record as comments):
//   #ifdef BIGFLOAT (undefined):  gcvt_wrapper(floorl(strtold(in,NULL)),out);
//     return 0;
//   #else (live):                 gcvt_wrapper(floor(strtod(in,NULL)),out);
//     return 0;
//   #ifdef MULTI_PRECISION (undefined): the mpf block - the
//     strchr(in,'.') guard; when no '.' is present the vendor
//     short-circuits strcpy(out,in) + return 0 (the
//     already-integral fast path); with '.' present: static
//     mpf_t x (init-once f=1, FLT_PRECISION), gmp_sscanf(in,
//     "%Fg",x), mpf_floor(x,x), gmp_sprintf(out,"%Fg",x), then
//     the trailing-zero strip loop for (i=strlen(out)-1; i>0;
//     i--) if (out[i]=='0') out[i]=0; else break; return 0.
//
// Dafny: EXEMPT - family lattice (spec/mps_log10.dfy); the
// floor call adds no decidable surface beyond the family
// envelope (the digit-prefix bounds are the family spec's); no
// new spec (journaled).
//
// BOTH consults (find_rule before render, events logged):
//   h018 preprocessor_conditional - reused and hit: live-arm
//     resolution against the reference build config; the
//     excluded-arm trailing-zero strip and the '.'-less
//     short-circuit are documented here only.
//   h009 format_string - reused via the gcvtWrapper seat (%g
//     shape, precision 8, BothNumRuntime.kt).
//   h017 naming - reused and hit: mps_floor -> mpsFloor;
//     out/in keep vendor names.

internal fun mpsFloor(out: CharArray, inStr: String): Int {
    gcvtWrapper(kotlin.math.floor(strtodC(inStr)), out)
    return 0
}
