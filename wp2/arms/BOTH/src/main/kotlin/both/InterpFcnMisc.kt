package both

// Unit 201 render: interp-fcn.cpp::<misc> (roster 201,
// batch 7, 60 LOC per units.csv; the 75-line vendor residue).
// The interp-fcn.cpp TU residue: the O'Kane banner (lines
// 1-27), the Dec 13 2025 / SQLITE / SYMDEBUG comment block,
// the include block, and the TU-local forward declarations.
// NO function bodies ride this residue (the unit-155
// compiler-fcninit precedent: the banner and the include
// block emit no symbols; the ledger records the provenance so
// later units citing this TU resolve cleanly).
//
// Consults (rule base wp2/arms/BOTH/rules_v0.pl):
//   h014 out_of_scope_call - reused: every declaration in the
//     residue resolves to an existing seat or a pending roster
//     owner: Interpret (unit-rendered seat), Eval (owner
//     interp-sym.cpp::Eval, roster 260, batch 9 pending),
//     ScanParse (owner interp-sym.cpp::ScanParse, roster 233,
//     batch 8 pending), fcn (owner interp-fcn.cpp::fcn,
//     roster 94, batch 4 pending), __label_lookup/
//     __text_function (the MpstimeMain dead-code reseat note),
//     _getpid/time (the MumpsMain/MpstimeMain getpid folds),
//     SYMSTORE/SYMRETRIEVE (the sym consts already seated).
//     NO new TODO stubs are raised: the residue references
//     nothing callable at render time.
//   h015 grammar-collision - reused: ASCII only, no backticks,
//     no raw comment dollars, trailing newline.
//
// Quirks kept (ledgered):
//   - #define SYMSTORE 0 / SYMRETRIEVE 1 duplicate the sym.h
//     seats (the residue redefines locally; the arm seats
//     once - noted, not re-seated).
//   - #define intLong: the 64-bit arm already seats the long
//     tpx discipline (h009/h019 notes); no action.
//   - the banner's monetary.h/locale.h includes have no
//     emitted symbols in this arm (the Locale.US h009 pin
//     family covers the locale seats).
//
// No declarations ride the residue; nothing is emitted here.
