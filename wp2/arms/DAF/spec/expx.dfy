// DAF B5 T2 spec: interp-arith.cpp::expx (unit 140), roster rows
// 39/40/41/43/99 form the BASE-log consult family (the same
// config lattice as the unit-132 mps_log10 and unit-103 mps_asin
// specs). Modeled against the DAF Kotlin render Expx.kt
// (reference configuration: MULTI_PRECISION false, BIGFLOAT
// false, so the reference arm is
// gcvt_wrapper(pow(strtod(a,NULL),strtod(b,NULL))) and the
// MPFR/long-double branches are dead, carried as documented
// notes only).
//
// SPEC-ASSUMPTION DIVERGENCE (to be logged in ARM_WORKING_NOTES DAF
// B5 T2, mirroring the unit-132/103 notes): this spec models the
// reference arm's mathematical contract but does NOT model the
// %.8g text format: the vendor output is a decimal string produced
// by gcvt_wrapper; refloating that string is not round-trip exact,
// and the format itself is out of scope of the consulted BASE-log
// anchor (row 26 family: format strings kept verbatim with
// Locale.US). The pow properties are therefore stated over reals,
// not over printed text.

// Bodyless + {:axiom}: the postconditions below are the DECLARED
// assumptions recorded in the journal; they are assumed, not
// proved here.
function {:axiom} Pow(x: real, y: real): real
  requires y >= 0.0
  ensures x > 0.0 ==> Pow(x, y) > 0.0
  ensures y == 0.0 ==> Pow(x, y) == 1.0

// The reference arm's contract on reals: parse (strtod semantics:
// a leading-double parse, 0.0 on no parse), transform (pow), format
// (out of scope, see note). The positivity contour: a positive
// base raised to any nonnegative exponent stays positive; the
// zero-exponent annihilates to one.
function ExpxSpec(x: real, y: real): real
  requires y >= 0.0
  ensures x > 0.0 ==> ExpxSpec(x, y) > 0.0
  ensures y == 0.0 ==> ExpxSpec(x, y) == 1.0
{
  Pow(x, y)
}

// Basic declared facts of pow the journal relies on (assumed via
// the axiom carrier, checked only for spec well-formedness here).
lemma ExpxWellFormed(x: real, y: real)
  requires y >= 0.0
  ensures ExpxSpec(x, y) > 0.0 || ExpxSpec(x, y) == 1.0 || ExpxSpec(x, y) <= 0.0
{
}

// Out-of-domain behavior, kept honest to the C artifact: vendor does
// NOT guard the domain; the pow of a negative base with a
// fractional exponent is the C library NaN pathology and is NOT
// modeled here. This lemma records what the spec does NOT claim.
lemma ExpxDomainIsAssumed()
  ensures true
{
}
