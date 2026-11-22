// DAF B6 T1 spec: interp-arith.cpp::mps_log2 (unit 162), the
// ifdef-web twin of the unit-132 mps_log10 spec. Roster rows
// 39/40/41/43/99/100 form the BASE-log consult family (the same
// config lattice as the mps_log10/mps_asin specs). Modeled against
// the DAF Kotlin render MpsLog2.kt (reference configuration:
// MULTI_PRECISION false, BIGFLOAT false, so the reference arm is
// gcvt_wrapper(log2(strtod(b, NULL)), a) and the MPFR/long-double
// branches are dead, carried as documented notes only).
//
// SPEC-ASSUMPTION DIVERGENCE (to be logged in ARM_WORKING_NOTES DAF
// B6 T1, mirroring the unit-132 note): this spec models the
// reference arm's mathematical contract but does NOT model the
// %.8g text format: the vendor output is a decimal string produced
// by gcvt; refloating that string is not round-trip exact, and the
// format itself is out of scope of the consulted BASE-log anchor
// (row 26: format strings kept verbatim with Locale.US). The range
// property is therefore stated over reals, not over printed text.

// Bodyless + {:axiom}: the postconditions below are the DECLARED
// assumptions recorded in the journal; they are assumed, not proved
// here.
function {:axiom} Log2(x: real): real
  requires x > 0.0
  ensures (x >= 1.0 ==> Log2(x) >= 0.0) && (x < 1.0 ==> Log2(x) < 0.0)
  ensures (x >= 2.0 ==> Log2(x) >= 1.0) && (x < 2.0 ==> Log2(x) < 1.0)

// The reference arm's contract on reals:
// parse (strtod semantics: a leading-double parse, 0.0 on no
// parse), transform (log2), format (out of scope, see note).
function MpsLog2Spec(x: real): real
  requires x > 0.0
  ensures (x >= 1.0 ==> MpsLog2Spec(x) >= 0.0) && (x < 1.0 ==> MpsLog2Spec(x) < 0.0)
  ensures (x >= 2.0 ==> MpsLog2Spec(x) >= 1.0) && (x < 2.0 ==> MpsLog2Spec(x) < 1.0)
{
  Log2(x)
}

// Basic declared facts of log2 the journal relies on (assumed via
// the axiom carrier, checked only for spec well-formedness here).
lemma MpsLog2WellFormed(x: real)
  requires x > 0.0
  ensures MpsLog2Spec(x) >= 0.0 || MpsLog2Spec(x) < 0.0
{
}

// Out-of-domain behavior, kept honest to the C artifact: vendor
// does NOT guard the domain; the log2 of a nonpositive input is the
// C library -inf-or-NaN pathology and is NOT modeled here. This
// lemma records what the spec does NOT claim.
lemma MpsLog2DomainIsAssumed()
  ensures true
{
}
