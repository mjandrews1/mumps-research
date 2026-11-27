package both

// Unit 266 (BOTH B9, core): interp-sym.cpp::<misc> (75 LOC,
// batch_plan ordinal 266; units.csv row 258). Include-residue
// unit (the banner+include-ledger convention): the dump is the
// GPL banner (Copyright 2003-2025, Dec 13 2025 dateline), the
// build defines (SQLITE live; SYMDEBUG commented out;
// _INTERP_; the CSTR/USTR cast macros; intLong), the include
// ladder (the interp-sym set: defines.h, sym.h, btree.h,
// globalOrder.h, keyfix.h, inline.h, fcns.h, builtin.h,
// interp.h, then global.h and the extern "C" pair
// _getpid/time), the file-tail declarations, and the
// function-banner COMMENT LADDER (BuildLocal, sym_, _SymFree,
// the evaluate-an-expression banner, _ascii, _extract - banner
// comments only; the bodies are their own roster units).
//
// Declaration ledger (the externs name their arm seats):
//   - Interpret: rendered (Interpret.kt).
//   - ScanParse: rendered at the B8 unit-233 close.
//   - Eval: rendered THIS BATCH (unit 264, Eval.kt).
//   - __label_lookup / __text_function: the vendor extern
//     function pointers (the InterpFcnMisc ledger note,
//     roster-94 owner pending; the MpstimeMain dead-code
//     reseat precedent) - no new seat raised.
//   - extern "C" long _getpid(void) / time_t time(time_t *):
//     the C library faces; time rides the currentTimeMillis
//     seat (the _horolog render this batch), getpid is the
//     process-id edge not yet seated (no corpus consumer in
//     this batch; no seat raised).
//   - BuildLocal: rendered at the B8 unit-231 close.
//   - _SymFree: rendered (units.csv row 66 family, the
//     batch-2 close).
//   - sym_: the symtab family banner (owner interp-sym.cpp
//     bodies, future batches).
//   - _ascii / _extract: banner comments only (the _extract
//     owner is the batch-2 _extract family, rendered; _ascii
//     is a future-batch unit).
//
// BOTH consults (find_rule before render, events logged):
//   h003 state_macro - reused: SQLITE is the frozen
//     reference-build contract; _INTERP_/intLong are TU-scoped
//     compile flags, noted only.
//   h014 out_of_scope_call - reused: every declaration names a
//     seated or pending arm owner (ledger above).
//   h018 preprocessor_conditional - reused: the commented-out
//     SYMDEBUG and wait.h lines stay commented in the ledger.
//   h017 naming - reused: the include and declaration names
//     stay verbatim in the KDoc ledger.
//   Dafny: exempt (no body; ledger-only unit).
//
// Quirks kept (ledgered): the duplicated banner-block comment
// style (===...=== frames) is the vendor file's section
// scaffolding; nothing to emit in Kotlin.
