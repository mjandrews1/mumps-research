package doc

import kotlin.math.ceil

// Unit 282 (B10 tranche 1): interp-arith.cpp::mps_ceil (batch
// 10, simple stratum; units.csv row 303: loc 26, cr 0.037, ptr 0,
// ctl 2, score 7.6, rank 52; physically verified this session at
// vendor interp-arith.cpp:483-510, physLOC 26) - the CEIL twin
// of the unit-281 mps_trunc render landed this same tranche
// (identical vendor shape at interp-arith.cpp:406 vs :483, the
// family-diff verified line-for-line this session). Reference-
// config resolution per H-018 (no MULTI_PRECISION, no BIGFLOAT:
// the plain double arm is live; the excluded GMP ladder is
// documentary with its strchr('.') gate and strcpy(out,in)
// integer-arg tail). strtod fold (MpsExp.kt seat) and
// gcvt_wrapper H-014 stub seat per the family; ceil is the
// numeric intrinsic (H-010 adjacent). The C int return is the
// vendor 0 constant on the live arm - FOLDED to the Kotlin Unit
// return per the family-delegate convention (the Stubs seats are
// Unit and no caller reads the constant), the literal 0 kept as
// the comment fossil below. Naming per H-017 (mps_ceil ->
// mpsCeil). The Stubs.mpsCeil seat flips to delegate this push
// (the batch-10 tag resolves at its own row).

fun mpsCeil(out: CharArray, in_: String) {
    // BIGFLOAT arm (excluded by the reference config):
    //   gcvt_wrapper(ceill(strtold(in, NULL)), out); return 0
    // MULTI_PRECISION arm (excluded; the GMP ladder - documentary
    // only on the reference config; the strchr('.')-then-mpf
    // gate with mpf_ceil and its strcpy(out,in) integer-arg tail
    // noted):
    //   static mpf_t x; mpf_init2(x, FLT_PRECISION); gmp_sscanf
    //   (in, "%Fg", x); mpf_ceil(x, x); gmp_sprintf(out,
    //   "%Fg", x); else strcpy(out, in)
    Stubs.gcvtWrapper(ceil(strtod(in_)), out)
    // return 0 - the vendor constant, folded to Unit (header)
}
