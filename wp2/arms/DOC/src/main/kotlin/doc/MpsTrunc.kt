package doc

import kotlin.math.truncate

// Unit 281 (B10 tranche 1): interp-arith.cpp::mps_trunc (batch
// 10, simple stratum; units.csv row 302: loc 26, cr 0.037, ptr 0,
// ctl 2, score 7.6, rank 51; physically verified this session at
// vendor interp-arith.cpp:406-433, physLOC 26). Reference-config
// resolution per H-018 (no MULTI_PRECISION, no BIGFLOAT: the
// plain double arm is live). VENDOR DIVERGENCE vs the mps_floor
// family shape: the excluded MULTI_PRECISION arm here is a GMP
// mpf_t ladder with the NONTRIVIAL live-side gate of the floor
// twin - taken only when the argument contains a '.'; its plain
// strcpy(out,in) tail is documentary. strtod fold (MpsExp.kt
// seat) and gcvt_wrapper H-014 stub seat per the family;
// truncate is the numeric intrinsic (H-010 adjacent). The C int
// return is the vendor 0 constant on the live arm - FOLDED to
// the Kotlin Unit return per the family-delegate convention
// (MpsLog/MpsExp2/MpsFloor precede: the Stubs seats are Unit
// and no caller reads the constant), the literal 0 kept as the
// comment fossil below. Naming per H-017 (mps_trunc ->
// mpsTrunc). The Stubs.mpsTrunc seat flips to delegate this
// push (the batch-10 tag resolves at its own row).

fun mpsTrunc(out: CharArray, in_: String) {
    // BIGFLOAT arm (excluded by the reference config):
    //   gcvt_wrapper(truncl(strtold(in, NULL)), out); return 0
    // MULTI_PRECISION arm (excluded; the GMP ladder - documentary
    // only on the reference config; the strchr('.')-then-mpf
    // gate with mpf_trunc and its strcpy(out,in) integer-arg tail
    // noted):
    //   static mpf_t x; mpf_init2(x, FLT_PRECISION); gmp_sscanf
    //   (in, "%Fg", x); mpf_trunc(x, x); gmp_sprintf(out,
    //   "%Fg", x); else strcpy(out, in)
    Stubs.gcvtWrapper(truncate(strtod(in_)), out)
    // return 0 - the vendor constant, folded to Unit (header)
}
