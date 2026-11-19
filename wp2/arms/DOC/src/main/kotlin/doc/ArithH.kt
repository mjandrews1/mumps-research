package doc

// Unit 57: include/mumpsc/arith.h::<misc> (batch 2, complex stratum;
// row 38: loc 47, ctrl 8, score 82.9, rank 263). The whole-header
// residual of the runtime arithmetic library: profile banner, the
// GMP/MPFR conditional pair (resolved against the reference build:
// both OFF, no MULTI_PRECISION - excluded arms journaled), the
// FLT_PRECISION define, and the prototype roster. Every prototype is
// led to its owner: the translated batch units own their seats
// (mpsExp/mpsExp10 units 40/41; the mps_* family of later batches);
// Mltoa is the Stubs seat raised in batch-2 opening (its owner unit
// arrives in a later batch); add/sub/mult/divx/modulo et al. remain
// H-014 out-of-scope stub seats of their later-batch owners; the
// numcomp family's compiler-side twin numcomp2 was stubbed with unit
// 54. The commented ltoa fossil (the hand-rolled inverses comment) and
// the UNIX==1 itoa declaration arm (in) are ledger notes.

const val FLT_PRECISION = 72 // arith.h:57

// Prototype roster (arith.h:59-101), owner annotations:
//   void Mltoa(long,unsigned char*)          - Stubs.Mltoa seat (raised);
//   add/add1/add3/sub/mult/mult1/divx/modulo/divi/expx - H-014 seats,
//     owners interp-arith.cpp units of later batches;
//   mps_abs..mps_justify                    - mps_* family, owners are
//     the mps_* corpus units (units 40/41 landed; the rest later);
//   numcomp/numcomp1/numcomp2               - numcomp family (numcomp is
//     the batch-1 corpus unit; numcomp2 stubbed with unit 54);
//   cannon                                  - owner units of later
//     batches (cannon H-014 seat);
//   itoa(int, unsigned char*, int)          - UNIX==1 arm (in; the
//     commented void ltoa line kept as the fossil note above).
