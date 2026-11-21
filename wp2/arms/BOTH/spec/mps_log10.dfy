// mps_log10.dfy - BOTH-arm spec of batch-5 tranche-1 unit 132
// (interp-arith.cpp::mps_log10). The unit's two decidable cores are
// specified here:
//   1. the three-way preprocessor_conditional resolution: with the
//      frozen reference build config (MULTI_PRECISION and BIGFLOAT
//      both undefined, the h018 resolution authority), the live arm is
//      the plain-double gcvt_wrapper(log10(strtod(b,NULL)),a);
//   2. the magnitude bound of the digit-prefix parse feeding the
//      numeric conversion (the strtodC seat: digit-prefix parse).
// Written after the Kotlin render; verification edits never change
// graded behavior.

datatype ArmMode = MP | BigFloatMode | PlainDouble

const MULTI_PRECISION := false // reference build: undefined
const BIGFLOAT := false // reference build: undefined

// The h018 resolution of the #ifndef Multi_Precision / #ifdef BIGFLOAT
// lattice, as a total function of the two config flags.
method ResolveArm(multiPrecision: bool, bigfloat: bool) returns (mode: ArmMode)
    ensures mode == PlainDouble <==> (!multiPrecision && !bigfloat)
    ensures mode == BigFloatMode <==> (!multiPrecision && bigfloat)
    ensures mode == MP <==> multiPrecision
{
    if multiPrecision {
        mode := MP;
    } else if bigfloat {
        mode := BigFloatMode;
    } else {
        mode := PlainDouble;
    }
}

// The frozen reference build selects the plain-double arm; the two
// excluded arms resolve to their own modes.
method LiveArmIsPlainDouble()
{
    var live := ResolveArm(MULTI_PRECISION, BIGFLOAT);
    assert live == PlainDouble;
    var excludedMp := ResolveArm(true, false);
    var excludedBf := ResolveArm(false, true);
    assert excludedMp == MP;
    assert excludedBf == BigFloatMode;
}

function Pow10(n: nat): int
    decreases n
{
    if n == 0 then 1 else 10 * Pow10(n - 1)
}

// The iterative digit-prefix parse of the strtodC seat: consuming a
// k-digit prefix yields a value bounded by its place range, and the
// parse of any prefix of digits never leaves that range.
method DigitPrefixBounds(s: seq<int>)
    returns (v: int)
    requires forall i :: 0 <= i < |s| ==> 0 <= s[i] <= 9
    ensures v == 0 <==> (|s| == 0 || forall i :: 0 <= i < |s| ==> s[i] == 0)
    ensures 0 <= v < Pow10(|s|)
{
    v := 0;
    var k := 0;
    while k < |s|
        invariant 0 <= k <= |s|
        invariant 0 <= v < Pow10(k)
        invariant v == 0 <==> (k == 0 || forall j :: 0 <= j < k ==> s[j] == 0)
        decreases |s| - k
    {
        v := v * 10 + s[k];
        assert placeStep: Pow10(k + 1) == 10 * Pow10(k);
        assert boundHold: v < 10 * Pow10(k);
        k := k + 1;
    }
}
