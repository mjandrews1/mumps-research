package daf

// Unit 281 (DAF B10 T3, ledger): include/mumpsc/libmpscpp.h::<misc>
// (units.csv row 281: 369 physical LOC, ledger-heavy per the
// roster note): the C++ library header: the MPSCPP define, the
// include deck (readline/history, gtk/gtkx - GUI residue), the
// _MDH_ label-lookup default arms, the GlobalExceptionCode macro
// ladder, the get_global/set_global/BTREE/ltoa/ftoa/string macros,
// the mstring/global/indirect class decls, and the C++ overload
// PROTOTYPES of every library service. Proto-only bag: every
// declared body is owned by an already-drawn libmpscpp.cpp/.h
// unit - the header adds no model content of its own.
// Consults (2026-11-25T15:08:00Z, nominal): row 37 hit-reuse (the
// residue-disposition ledger of the MemsizeHMisc precedent),
// rows 39/78 hit-reuse (FTELL/FSEEK -> fseeko/ftello; the errors.h
// ladder at :70-80 pins BASE-seated GlobalExceptionCode consts:
// UNSPECIFIED 0, INVALID_CONVERSION 1 (LibmpscppMisc5.kt), GLOBAL_
// NOT_FOUND 2 (MstringLength.kt), VARIABLE_NOT_FOUND 3, DATA_RANGE
// _ERROR 4, NUMERIC_RANGE 5 (MpstimeMain.kt), INPUT_ERROR 6,
// INTERPRETER_ERROR 8, SYMTAB_ERROR 9, INPUT_LENGTH 10 - all
// SEATED, LEDGER), rows 6/22 hit-reuse (the PATTERN_ERROR 7 gap:
// no DAF reader - ledger note), row 98 hit-reuse (the C++ overload
// lattices LEDGER to their rendered kin seats: Xecute/write/
// WriteMumps1 (row 280 writeMumps1D), __command__ (row 280), bmg_
// setup/fullsearch (row 284), Score/nodash (row 280), xecute triple
// (row 280), Shred/ShredQuery (row 280/176), Justify (unit 194/
// row 308), pm/pmD (Pattern kin), _find/_extract/_piece/
// _horolog/stem/wstem (row 292), KiLL/Ztell/Zseek, Query/Qlength/
// Qsubscript, SymGet/SymPut __symput/__symget, SQL_* family
// (Rdbms seats), Cost/sw (SmithWaterman kin), MumpsGlobalException
// class (DafInterpStubsT3.kt MumpsGlobalExceptionD seat)),
// row 103 hit-reuse (get_global/set_global macros LEDGER to the
// MglobalDaf call fold used across rows 280/282).
// VENDOR QUIRKS ledgered: (1) the gtk mdh_* decls are GUI residue,
// outside the CLI model; (2) GCVT defaults per-platform; (3) the
// header re-declares mdh_tree_level_add five ways (the row-297
// 9-col overload ledger note); (4) the LOG2 log2 alias for old
// compilers; (5) BIGLONG is a typedef no-op (Int64 fold).
// Dafny: EXEMPT (proto/de macro ledger; journal row).

// (no Kotlin emitted: the unit's model bag is prototypes and
// macros whose bodies are owned by already-drawn units; the
// macro-lattice pins LEDGER at their BASE seats above)
