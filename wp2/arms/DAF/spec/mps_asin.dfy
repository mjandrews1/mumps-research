// DAF B4 T1 spec: interp-arith.cpp::mps_asin (unit 103), roster rows
// 39/40/41/43/99/100 form the BASE-log consult family. Modeled against
// the DAF Kotlin render MpsAsin.kt (reference configuration:
// MULTI_PRECISION false, BIGFLOAT false, so the reference arm is
// gcvt_wrapper(asin(strtod(b, NULL))), a) and the MPFR/long-double
// branches are dead, carried as documented consts only).
//
// SPEC-ASSUMPTION DIVERGENCE (logged in ARM_WORKING_NOTES DAF B4 T1):
// this spec models the reference arm's mathematical contract but does
// NOT model the %.8g text format: the vendor output is a decimal string
// produced by gcvt; refloating that string is not round-trip exact, and
// the format itself is out of scope of the consulted BASE-log anchor
// (row 26: format strings kept verbatim with Locale.US). The asin range
// property is therefore stated over reals, not over printed text.

// Axiomatized asin range (declared assumption, not proved here):
// asin maps [-1, 1] into [-pi/2, pi/2].
const PI_2: real := 1.570796326794896619231321691639751442

function Asin(x: real): real
  requires -1.0 <= x <= 1.0
  ensures -PI_2 <= Asin(x) <= PI_2

// The reference arm's contract on reals:
// parse (strtod semantics: a leading-double parse, 0.0 on no parse),
// transform (asin), format (out of scope here, see divergence note).
function MpsAsinSpec(x: real): real
  requires -1.0 <= x <= 1.0
  ensures -PI_2 <= MpsAsinSpec(x) <= PI_2
{
  Asin(x)
}

// Out-of-domain behavior, kept honest to the C artifact: vendor does
// NOT guard the domain; NaN pathology (asin of |x|>1) is a real-vs-NaN
// modeling boundary. This lemma records what the spec does NOT claim.
lemma MpsAsinDomainIsAssumed()
  ensures true
{
}
