package daf

/*
 * DAF batch-9 unit 261 (roster row 261; interp-service.cpp::<misc>,
 * residue, LOC 55, score 11.2). Consult 2026-11-24T22:40:00Z.
 *
 * This roster row is the interp-service.cpp FILE PRELUDE only:
 * the Jan 10 2026 date line, the SQLITE define, the sym.c banner
 * comment, the include ladder, the _INTERP_/CSTR/USTR micro-
 * defines, the intLong define (64-bit long arm: the reference
 * build leaves it defined-empty, the 64-bit default; H-018
 * fossil ledger note), and the three prototypes
 * (Interpret/ScanParse/Eval) plus the two extern function-pointer
 * declarations (__label_lookup/__text_function).
 *
 * LEDGER (no new code: every name the prelude declares or
 * includes already rides a landed seat from the unit-215
 * InterpService.kt render and its neighbors):
 * - SQLITE: the once-arm define already live per the unit-152
 *   arm precedent (the allegiance pin; MglobalDaf/interp-sql
 *   seats check it).
 * - SYMDEBUG: vendor comment-only (not defined on the reference
 *   build); kept as this note only.
 * - defines.h/sym.h/btree.h/globalOrder.h/keyfix.h/inline.h/
 *   fcns.h/builtin.h/interp.h: the seat fold of the arm - consts
 *   in DafInterpStubsT3.kt / DafCRuntime.kt, the DafCompilerState
 *   and DafMsv carriers, the MglobalDaf/btreeDaf seats, the
 *   globalOrderHD unit-259 body, the keyfix callers of the
 *   unit-215 parse prologue arms.
 * - int Interpret(const char *, MSV *) - InterpretDaf (unit-208
 *   release, Interpret.kt).
 * - int ScanParse(char *) - scanParseDaf (unit-229 release,
 *   ScanParse.kt).
 * - int Eval(unsigned char *, unsigned char *, struct MSV *) -
 *   evalDaf (landed family: InterpSym* renders; roster row 264
 *   is the interp-sym.cpp::Eval verification row).
 * - extern int (*__label_lookup)(char *) and extern char*
 *   (*__text_function)(int): the label/text indirection seats
 *   carried by the compiler-state renders (the DO/goto family);
 *   named owners on their roster rows, not drawn here.
 * H-015 hygiene: no raw dollars ride the comments.
 * Dafny: EXEMPT (file-prelude residue; journal row).
 */
