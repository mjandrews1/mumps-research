package both

// Unit 261 (BOTH B9, core): interp-service.cpp::<misc> (55 LOC,
// batch_plan ordinal 261; units.csv row 263). Include-residue
// unit (the banner+include-ledger convention, the B8
// CompilerWrite/CompilerKill family precedent): the dump is
// the GPL banner (Copyright 2003-2026, Jan 10 2026 dateline),
// the build defines (SQLITE live; SYMDEBUG commented out in
// the vendor; _INTERP_; the CSTR/USTR cast macros; intLong),
// the include ladder (stdio/stdlib/string/string/sys/types/
// monetary/locale/math, then the mumpsc set: defines.h,
// sym.h, btree.h, globalOrder.h, keyfix.h, inline.h, unistd,
// time.h, fcns.h, builtin.h, interp.h), and the file-tail
// forward/extern declarations.
//
// Declaration ledger (the externs name their arm seats):
//   - int Interpret(const char *, MSV *): rendered
//     (Interpret.kt, the batch-4 unit).
//   - int ScanParse(char *): rendered at the B8 unit-233 close
//     (ScanParse.kt).
//   - int Eval(unsigned char *, unsigned char *, struct MSV *):
//     the FORWARD DECLARATION of unit 264
//     (interp-sym.cpp::Eval) - this batch, renders at that
//     unit's seat; no stub is raised here (the declaration
//     only; the h014 seat lands with the owner this batch).
//   - int (*__label_lookup)(char *) / char *(*__text_function)
//     (int): the vendor extern function pointers (the
//     InterpFcnMisc ledger note: roster-94 owner pending; the
//     MpstimeMain dead-code reseat precedent) - no new seat
//     raised, the ledger note rides.
//
// BOTH consults (find_rule before render, events logged):
//   h003 state_macro - reused: the SQLITE define is the frozen
//     reference-build contract (the AllocSV SQLITE arm
//     precedent); _INTERP_/intLong are TU-scoped compile
//     flags with no Kotlin surface, noted here only.
//   h014 out_of_scope_call - reused: every extern names a
//     seated or pending arm owner (ledger above); nothing new
//     is called from this residue.
//   h018 preprocessor_conditional - reused: the commented-out
//     SYMDEBUG and wait.h lines stay commented in the ledger
//     (dead in the reference build).
//   h017 naming - reused: the include and declaration names
//     stay verbatim in the KDoc ledger.
//   Dafny: exempt (no body; ledger-only unit).
//
// Quirks kept (ledgered): the monetary.h/locale.h includes
// (dead in the rendered arm; the locale contract rides the
// Locale.US seats), the CSTR/USTR cast macros (fold away in
// Kotlin's typed CharArray domain).
