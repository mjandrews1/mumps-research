package mumps.interp

/**
 * PRO-arm translation of include/mumpsc/arith.h::<misc> (pilot B2,
 * unit 57, provisional under deviation C-003). Translated
 * independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), whole-header
 * residual (units.csv: 47 LOC, score 82.9, rank 263): the Mumps
 * runtime arithmetic library's declaration header - prototypes for
 * the string-operand math functions plus the FLT_PRECISION macro.
 * The header has no function bodies, so the Kotlin rendering is the
 * macro constant plus this owner roster for every prototype.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h014 out_of_scope_call : every prototype resolves to its
 *    interp-arith.cpp owner (all are corpus units there, none a
 *    non-corpus function); already translated or stubbed here:
 *    numcomp (B1 unit 20, Numcomp.kt), mps_acos (B1 unit 10,
 *    MpsAcos.kt), mps_exp (B2 unit 40, MpsExp.kt), mps_exp10 (B2
 *    unit 41, MpsExp10.kt), Mltoa (InterpStubs-independent MdhStubs
 *    stub, batch-10 owner). The remaining roster is future batch
 *    units: add/add1/add3/sub/mult/mult1/divx/divi/modulo/expx,
 *    mps_abs/mps_sqrt/mps_floor/mps_ceil/mps_trunc/mps_log2/
 *    mps_log10/mps_log/mps_cos/mps_tan/mps_sin/mps_asin/mps_atan/
 *    mps_exp2/mps_pow/mps_justify (batch 3+), numcomp1/numcomp2/
 *    cannon/itoa (interp-arith.cpp residues) - no stubs are created
 *    for them here; stubs appear only when a translated caller
 *    needs one, per house convention.
 *  - h017 naming           : arith.h -> ArithH wrapper file.
 *
 * MISSED TRIGGER (preprocessor_conditional, journal-logged, 7th as
 * in unit 29): the MULTI_PRECISION block (HAVE_GMP==YES and
 * HAVE_MPFR==YES -> gmp.h/mpfr.h) is compiled out at the reference
 * configuration (the numSetup.h unit-29 resolution - hardware
 * double branches; no rule family existed to guide this).
 */

// arith.h:52 - default floating formatting precision.
private const val FLT_PRECISION = 72
