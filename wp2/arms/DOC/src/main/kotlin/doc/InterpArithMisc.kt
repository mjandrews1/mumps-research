package doc

// Unit 90 (B3 tranche 3 / sub-push B): interp-arith.cpp::<misc> (batch
// 3, complex stratum; LOC 118, score 27.5). The leading residue of the
// Mumps runtime arithmetic library: the June 16, 2025 arith.c-style
// banner ("Mumps Bioinformatics Software Library" residue, GPL
// 2003-2025 O'Kane ledger), the astyle-format note, the arith.c
// purpose prose, the six plain includes (iostream stdio stdlib math
// string limits) plus the four mumpsc includes (defines.h, fcns.h,
// libmpscpp.h, numSetupDefines.h), the using-declaration, and the
// preprocessor/prototype residue up to the first function body
// (Mltoa at vendor line 83 - the chunker cut). Residue render, no
// executable code (EpiMisc unit-63, ReadMisc unit-64, CppsysfuncMisc
// unit-76 precedents). The residue ledger kept verbatim below:
//   floatSize comment (BIGFLOAT if present - resolved UNDEFINED per
//     the reference build, cookbook H-018 arm-resolution rule)
//   #define intLong (32-bit int arm, kept as ledger)
//   void sigint(int) prototype (shutdown path; CppsysfuncMisc roster
//     names the same C-ABI entity)
//   #define NO 0 / #define YES 1
//   the GCVT arms: #ifdef GCVT opens the reference-build DEFINED arm
//     (gcvt_wrapper(x,a) = qgcvt(x, 8, a) under BIGFLOAT, else
//     gcvt(x, 8, a)); the #else arm closes with sprintf forms
//     (qgcvt UNDEFINED shape) and the BIGFLOAT/MULTI_PRECISION
//     sandwich (multi-precision arms resolve UNDEFINED per H-018;
//     live arm is gcvt(x, 8, a), already the Stubs.gcvtWrapper
//     seat shape named by the tranche-1 mps renders).
// Post-cut owner ledger (per-function entries of this file are
// separate corpus units; several already landed): Mltoa = unit 10
// (Stubs delegate seat), the mps_* arithmetic family = the tranche-1
// renders MpsSqrt (u67) and MpsAtan (u71) plus the batch-1 renders,
// the static inline floatstring below-roster helper rides the
// Stubs.floatstring seat naming interp-arith.cpp:597. Naming per
// H-017 (no code rendered, names cited verbatim).

private const val INTERP_ARITH_BANNER_DATE = "June 16, 2025"

private val INTERP_ARITH_INCLUDE_LEDGER = listOf(
    "iostream",
    "stdio.h",
    "stdlib.h",
    "math.h",
    "string.h",
    "limits.h",
    "mumpsc/defines.h",
    "mumpsc/fcns.h",
    "mumpsc/libmpscpp.h",
    "mumpsc/numSetupDefines.h",
)

// Preprocessor residue ledger (verbatim order, no value reads at
// render time):
//   intLong - defined (32-bit int arm, ledger)
//   sigint(int) - prototype only
//   NO = 0, YES = 1
//   GCVT arm - reference build: GCVT DEFINED, BIGFLOAT UNDEFINED,
//     live shape gcvt(x, 8, a) = the Stubs.gcvtWrapper seat (H-018)
//   bigfloat/multi-precision arms - both UNDEFINED per the reference
//     build; the mps_ex/mps_exp10 ledgered misses of unit C-006 note
//     the only place a resolved BIGFLOAT arm would change a render
private val interpArith90Residue = listOf(
    "intLong",
    "sigint(int)",
    "NO 0",
    "YES 1",
    "GCVT gcvt_wrapper",
    "BIGFLOAT",
    "MULTI_PRECISION",
)
