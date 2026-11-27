package both

// Unit 249 (BOTH B9, simple): compiler-write.c::<misc> (30 LOC,
// batch_plan ordinal 249; units.csv row 275). Include-residue
// unit: the dump is the GPL banner plus the include ladder
// (stdio.h, mumpsc/memsize.h, mumpsc/compiler-macros.h,
// mumpsc/compiler-globals-ext.h) and no function bodies; the
// file-head residue convention (the B8 CompilerKill/Lock/SetMisc
// precedent). The macro family ledger seats already rendered:
// compiler-macros.h::<misc> is the B8 CompilerMacrosHMisc.kt
// render (unit 226, the macro-family ledger), and
// compiler-globals-ext.h::<misc> is the CompilerGlobalsExtMisc.kt
// render (the initializer-completion seat flipped at the B8
// unit-220 close). write_code (the compiler-write.c body unit,
// units.csv row 177 family) is NOT part of this residue and is
// not seated here; no new carrier, const, or stub is raised by
// this unit.
//
// Consults (rule base wp2/arms/BOTH/rules_v0.pl):
//   h003 state_macro - reused: no state macro beyond the
//     already-ledgered ext family; nothing new to seat.
//   h017 naming - reused: the file ledger keeps the vendor
//     include names verbatim in the KDoc.
//   Dafny: exempt (no body; ledger-only unit).
//
// Quirks kept (ledgered): the banner copyright years
// (2003-2024) and the May 10, 2024 dateline ride the vendor
// header verbatim in the dump; not re-emitted in Kotlin.
