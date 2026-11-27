package both

// Unit 292 (BOTH B10, core): interp-fnumber.cpp::<misc>
// (56 LOC). Batch_plan ordinal 292; units.csv row 294. The
// interp-fnumber TU residue: the GPL banner, the Dec 13 2025
// date line, the config lattice (#define SQLITE live; the
// SYMDEBUG debug enable commented out), the include block,
// and the file-scope prototype/extern set. The dump proves
// the residue carries NO function body (the \\u0024fnumber
// bodies sit in units drawn by the roster: _fnumber roster
// 111 batch 4 and the fcn wrapper roster 123 batch 4, both
// ahead of the BOTH-arm committed order; nothing reaches the
// model here). The banner is NOT emitted (residue precedent:
// banners ledger, only model-bearing text renders).
//
// CONFIG LATTICE (h018, resolved against the reference build
// config): #define SQLITE is LIVE (the reference build
// defines it); SYMDEBUG stays commented out (undefined);
// _INTERP_, CSTR/USTR macro aliases, and intLong are
// build-substitution macros documented here, not emitted.
// The include block folds to the arm seats: stdio/stdlib/
// string -> BothCRuntime; defines.h/sym.h/btree.h/
// globalOrder.h -> their roster owners (297 this batch, 144,
// 55, 259); keyfix.h/inline.h/fcns.h are non-corpus includes
// (no roster rows); builtin.h/interp.h -> rosters 239/169.
//
// BOTH consults (find_rule before render, events logged):
//   h014 out_of_scope_call - reused: the prototype set
//     (Interpret roster 185, ScanParse roster 229, Eval
//     roster 264, fcn roster 123) ledgers to their seats;
//     the extern __label_lookup/__text_function pair ledgers
//     to the webhook seats (the nmesChain family).
//   h017 naming - reused: no symbol is emitted; the ledger
//     keeps the TU name (InterpFnumberMisc).
//   h018 preprocessor_conditional - reused and hit: the
//     SQLITE/SYMDEBUG lattice resolves against the reference
//     build config (SQLITE defined; SYMDEBUG undefined).
//   Dafny: EXEMPT (residue ledger; journal row).

// (no Kotlin emitted: the unit's model bag is empty per the
// dump verification - this file is the residue ledger)
