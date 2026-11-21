// expx.dfy - BOTH-arm spec of batch-5 tranche-2 unit 139
// (interp-arith.cpp::expx). The unit's two decidable cores are
// specified here:
//   1. the preprocessor_conditional resolution: with the frozen
//      reference build config (EXPX_MULTI_PRECISION and EXPX_BIGFLOAT both
//      undefined - same lattice family as mps_log10.dfy, h018), the
//      live arm is gcvt_wrapper(pow(strtod(a,NULL),strtod(b,NULL)),c);
//   2. the iterated-product shape of the excluded EXPX_MULTI_PRECISION
//      integer arm (mpz_pow_ui) - the decidable echo of the
//      exponentiation the live arm hands to the library pow.
// Written after the Kotlin render; verification edits never change
// graded behavior. The %g text format and the float pow are out of
// scope (T1 precedent).

datatype ExpxArmMode = ExpxMP | ExpxBigFloatMode | ExpxPlainDouble

const EXPX_MULTI_PRECISION := false // reference build: undefined
const EXPX_BIGFLOAT := false // reference build: undefined

// The h018 resolution lattice, as a total function of the flags.
method ResolveExpxArm(multiPrecision: bool, bigfloat: bool) returns (mode: ExpxArmMode)
    ensures mode == ExpxPlainDouble <==> (!multiPrecision && !bigfloat)
    ensures mode == ExpxBigFloatMode <==> (!multiPrecision && bigfloat)
    ensures mode == ExpxMP <==> multiPrecision
{
    if multiPrecision {
        mode := ExpxMP;
    } else if bigfloat {
        mode := ExpxBigFloatMode;
    } else {
        mode := ExpxPlainDouble;
    }
}

// The frozen reference build selects the plain-double arm.
method LiveArmIsExpxPlainDouble()
{
    var live := ResolveExpxArm(EXPX_MULTI_PRECISION, EXPX_BIGFLOAT);
    assert live == ExpxPlainDouble;
    var excludedMp := ResolveExpxArm(true, false);
    var excludedBf := ResolveExpxArm(false, true);
    assert excludedMp == ExpxMP;
    assert excludedBf == ExpxBigFloatMode;
}

// The iterated-product core (the mpz_pow_ui echo): ExpxPowInt model.
function ExpxPowInt(base: int, n: nat): int
    decreases n
{
    if n == 0 then 1 else base * ExpxPowInt(base, n - 1)
}

// The integer-exponent accumulation: a nonnegative base iterated up
// product equals the model, and the sign contour is proven.
method ExpxPowIter(base: int, n: nat) returns (v: int)
    requires base >= 0
    ensures v == ExpxPowInt(base, n)
    ensures v >= 0
{
    v := 1;
    var k := 0;
    while k < n
        invariant 0 <= k <= n
        invariant v == ExpxPowInt(base, k)
        invariant v >= 0
        decreases n - k
    {
        v := base * v;
        assert step: ExpxPowInt(base, k + 1) == base * ExpxPowInt(base, k);
        k := k + 1;
    }
}
