package doc

import kotlin.math.floor

// Unit 267 (B9 tranche 2): interp-arith.cpp::mps_floor (batch 9,
// core stratum; units.csv row 268: loc 30, ptr 0, ctl 4, score
// 8.1, rank 103; physLOC 30 physically verified this session).
// Reference-config resolution per H-018 (no MULTI_PRECISION, no
// BIGFLOAT: the plain double arm is live). VENDOR DIVERGENCE vs
// the mps_exp2 family shape: the excluded MULTI_PRECISION arm
// here is a GMP mpf_t ladder with a NONTRIVIAL live-side gate -
// it is taken only when the argument contains a '.'; the plain
// strcpy(out,in) tail of THAT arm is documentary. strtod fold
// (MpsExp.kt seat) and gcvt_wrapper H-014 stub seat per the
// family; floor is the numeric intrinsic kotlin.math.floor
// (H-010 adjacent). The C int return is the vendor 0 constant
// on the live arm - FOLDED to the Kotlin Unit return per the
// T1 family-delegate convention (MpsLog.kt/MpsExp2.kt precede:
// the Stubs seats are Unit and no caller reads the constant),
// the literal 0 kept as the comment fossil below. Naming per
// H-017 (mps_floor -> mpsFloor). The Stubs.mpsFloor seat flips
// to delegate this push (the unit-183 tag correction).

fun mpsFloor(out: CharArray, in_: String) {
    // BIGFLOAT arm (excluded by the reference config):
    //   gcvt_wrapper(floorl(strtold(in, NULL)), out); return 0
    // MULTI_PRECISION arm (excluded; the GMP ladder - documentary
    // only on the reference config; its strchr('.')-then-mpf
    // body and its strcpy(out,in) integer-arg tail noted):
    //   static mpf_t x; mpf_init2(x, FLT_PRECISION); gmp_sscanf
    //   (in, "%Fg", x); mpf_floor(x, x); gmp_sprintf(out,
    //   "%Fg", x); trailing-zero strip loop; else strcpy(out, in)
    Stubs.gcvtWrapper(floor(strtod(in_)), out)
    // return 0 - the vendor constant, folded to Unit (header)
}
