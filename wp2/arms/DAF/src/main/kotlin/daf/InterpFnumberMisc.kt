package daf

// Unit 294 (DAF B10 T3, ledger): interp-fnumber.cpp::<misc> (units.csv
// row 294: 56 physical LOC): the TU residue around the unit-111
// _fnumber row: the GPL banner, the SQLITE/SYMDEBUG defines, the
// sym.c head comment, the include deck (stdio/monetary/locale/
// math, mumpsc defines/sym/btree/globalOrder/keyfix/inline,
// fcns/builtin/interp), the intLong/BIGFLOAT configure notes, and
// the extern decl head (Interpret/ScanParse/Eval/__label_lookup/
// __text_function/fcn). The dump proves the model bag is EMPTY:
// the only function body in the TU is _fnumber, already rendered
// as the unit-111 seat fnumberD (InterpFnumber.kt; same TU, the
// real-body owner). The "\u0024fnumber() wrapper for compiler"
// scaffold comment above the body LEDGERS to that render.
// Consults (2026-11-25T15:10:00Z, nominal): row 37 hit-reuse (the
// residue-disposition ledger of the MemsizeHMisc precedent), rows
// 39/78 hit-reuse (the SQLITE pin LEDGERS to SQLITE_D; intLong to
// the DafCRuntime 64-bit int fold), rows 6/22 hit-reuse (the TU's
// file-scope state lives in the already-rendered unit-111
// neighborhood: nothing new), rows 18/87 hit-reuse (the Interpret/
// fcn extern decls LEDGER to the InterpretDaf/fcnD seats).
// VENDOR QUIRK kept: the head comment block still reads "sym.c -
// Mumps symbol table management" although the file is the
// _fnumber wrapper TU (the vendor's own mislabel, verbatim
// ledger); the monetary.h include is a no-op on Linux builds.
// Dafny: EXEMPT (residue ledger; journal row).

// (no Kotlin emitted: the unit's model bag is empty per the dump
// verification; the TU's single body is the unit-111 render)
