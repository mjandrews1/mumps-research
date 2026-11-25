package daf

// Unit 295 (DAF B10 T3, ledger): include/mumpsc/errors.h::<misc>
// (units.csv row 295: 18 physical LOC): the file is an INCLUDE
// FRAGMENT (not a header guard): 18 physical fprintf(Out, ...)
// statements that TEXTUALLY EXPAND at the include site into the
// emitting TU's output stream. The fragment is included by
// compiler-epi.c:54 and compiler-zcommands.c:110, and BOTH include
// sites have already-rendered DAF units that emit this exact
// ladder verbatim: CompilerEpi.kt:84+ (the unit-210 epi render)
// and CompilerZcommandsCode.kt:126+ (the zcommands render): the
// ladder text appears twice in the DAF model per the vendor's
// double expansion (including the vendor MISSPELLING
// "Interprerer Error", kept at both seats - quirk ledger).
// Consults (2026-11-25T15:11:30Z, nominal): row 37 hit-reuse (the
// include-time paste folds onto the emitting renders; no new
// seat), rows 96/92 hit-reuse (the GlobalExceptionCode comparison
// names LEDGER to the BASE-seated exception-code consts: INPUT_
// ERROR 6, INPUT_LENGTH 10, SYMTAB_ERROR 9, GLOBAL_NOT_FOUND 2,
// INTERPRETER_ERROR 8, INVALID_CONVERSION 1, DATA_RANGE_ERROR 4,
// NUMERIC_RANGE 5, UNSPECIFIED 0 - all seated), row 33 hit-reuse
// (the fprintf(Out, "...\n") emission is the C.emit carrier of
// both owning renders).
// VENDOR QUIRKS ledgered: (1) "Interprerer Error" misspelling
// (verbatim at both expanded seats); (2) the ladder emits C++
// code text (cout <<) INTO the generated C file - the generated
// programs print the exception ladder at run time; (3) the
// fragment begins     fprintf( Out, "     if (GlobalExceptionCode
// == INPUT_ERROR) " - i.e. it emits a C if-arm whose body is the
// paste of the NEXT emission; the ladder is a 10-way if/else-if
// chain in the generated code.
// Dafny: EXEMPT (include-fragment ledger; journal row).

// (no Kotlin emitted: the fragment's expansions are already
// rendered at both include-site owners; this row is the ledger)
