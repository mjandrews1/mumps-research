// DAF B5 T1 spec: interp-arith.cpp::mps_log10 (unit 132), roster rows
// 39/40/41/43/99/100 form the BASE-log consult family (the same
// config lattice as the unit-103 mps_asin spec). Modeled against
// the DAF Kotlin render MpsLog10.kt (reference configuration:
// MULTI_PRECISION false, BIGFLOAT false, so the reference arm is
// gcvt_wrapper(log10(strtod(b, NULL))), a) and the MPFR/long-double
// branches are dead, carried as documented notes only).
//
// SPEC-ASSUMPTION DIVERGENCE (to be logged in ARM_WORKING_NOTES DAF
// B5 T1, mirroring the unit-103 note): this spec models the
// reference arm's mathematical contract but does NOT model the
// %.8g text format: the vendor output is a decimal string produced
// by gcvt; refloating that string is not round-trip exact, and the
// format itself is out of scope of the consulted BASE-log anchor
// (row 26: format strings kept verbatim with Locale.US). The log
// sign property is therefore stated over reals, not over printed
// text.

// Bodyless + {:axiom}: the postconditions below are the DECLARED
// assumptions recorded in the journal; they are assumed, not proved
// here.
function {:axiom} Log10(x: real): real
  requires x > 0.0
  ensures (x >= 1.0 ==> Log10(x) >= 0.0) && (x < 1.0 ==> Log10(x) < 0.0)

// The reference arm's contract on reals:
// parse (strtod semantics: a leading-double parse, 0.0 on no
// parse), transform (log10), format (out of scope, see note).
function MpsLog10Spec(x: real): real
  requires x > 0.0
  ensures (x >= 1.0 ==> MpsLog10Spec(x) >= 0.0) && (x < 1.0 ==> MpsLog10Spec(x) < 0.0)
{
  Log10(x)
}

// Basic declared facts of log10 the journal relies on (assumed via
// the axiom carrier, checked only for spec well-formedness here).
lemma MpsLog10WellFormed(x: real)
  requires x > 0.0
  ensures MpsLog10Spec(x) >= 0.0 || MpsLog10Spec(x) < 0.0
{
}

// Out-of-domain behavior, kept honest to the C artifact: vendor does
// NOT guard the domain; the log10 of a nonpositive input is the C
// library -inf-or-NaN pathology and is NOT modeled here. This lemma
// records what the spec does NOT claim.
lemma MpsLog10DomainIsAssumed()
  ensures true
{
}
