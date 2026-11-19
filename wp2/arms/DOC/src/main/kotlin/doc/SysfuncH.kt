package doc

// Unit 107 (B4 tranche 2): include/mumpsc/sysfunc.h::<misc>
// (batch 4, core stratum; LOC 26, score 14.6, rank 141). Whole-file
// residual: the O'Kane LGPL banner, the stdio.h/signal.h/
// stateVector.h include ledger, and the declaration surface of the
// OS-specific runtime support functions. Declaration-only ledger
// (ReadMisc/EpiMisc sibling shape): no executable code is in this
// chunk; every prototype maps to its seat below. H-018 note: the
// #ifndef _INTERP_ / #else pair guards the SECOND block of
// prototypes - the pair's arms DIFFER (the #ifndef arm declares
// DirectRead/ErrorMessage/GlobalNext alongside LocalNext/
// LocalOrder; the _INTERP_ arm declares LocalNext/LocalOrder only).
// _INTERP_ is defined per-TU by the interpreter modules
// (interp-*.cpp define it before including the header;
// libmpscpp.cpp:36 likewise), so both arms are live in the
// reference build depending on the including TU - the ifndef arm
// is visible to the non-interpreter TUs (mpstime.cpp et al.), the
// else arm to the interpreter TUs. Rendered as the UNION ledger;
// the commented-out VariableNotFound prototype (vendor fossil)
// rides as a roster note per H-013.
//
// Declaration-to-seat roster:
//   getstr1            Done: unit 48 (Getstr2.kt family render)
//   TimeOut_getstr1    Stubs.TimeOut_getstr1 (not a roster unit)
//   DirectRead         Done: unit 24 (DirectRead delegate seat)
//   ErrorMessage       delegate seat (interp-sysfunc.cpp owner)
//   GlobalNext         Stubs.Mglobal XNEXT walk family (GlobalMin
//                      unit-44 precedent: XNEXT against Mglobal)
//   LocalNext          Stubs.Qlength-adjacent; not a roster unit
//   LocalOrder         not a roster unit
//   VariableNotFound1  not a roster unit (fossil comment ABOVE it:
//                      the commented-out VariableNotFound proto)
//   Merge              not a roster unit
//   GlobalData         Done: unit 45-adjacent (GlobalData.kt)
//   GlobalName         Done (GlobalName.kt)
//   LocalGet           not a roster unit
//   GlobalGet          not a roster unit
//   LocalQuery         Stubs.GlobalQuery family, local side
//   fullUnpad          Stubs.unpad batch-10 owner (seat raised at
//                      the unit-93 bind)
//
// The include ledger:
//   stdio.h              JVM console/file IO per unit (H-001)
//   signal.h             sigint rides the Stubs.sigint seat
//   mumpsc/stateVector.h the Msv carrier (Carriers.kt, H-004)

private val includeLedger107 = listOf(
    "stdio",
    "signal.h",
    "stateVector.h",
)

private val protoLedger107 = listOf(
    "getstr1",
    "TimeOut_getstr1",
    "DirectRead",
    "ErrorMessage",
    "GlobalNext",
    "LocalNext",
    "LocalOrder",
    "VariableNotFound1",
    "Merge",
    "GlobalData",
    "GlobalName",
    "LocalGet",
    "GlobalGet",
    "LocalQuery",
    "fullUnpad",
)
