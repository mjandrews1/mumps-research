package doc

// Unit 296 (B10 tranche 2): include/mumpsc/errors.h::<misc>
// (batch 10, core stratum; units.csv row 296: loc 18, cr 0.053,
// ptr 0, ctl 9, score 14.4, rank 139; physically verified this
// session at vendor include/mumpsc/errors.h:1-18, the WHOLE
// FILE residual physLOC 18). NOT a banner/prologue header: the
// file body is a CODE-FRAGMENT include - nine fprintf(Out, ...)
// statements that EMIT the runtime error-fragment C source into
// the compiler's generated program (the CompilerState.Out
// codegen-fragment convention of the ZcommandsCode renders; a
// code-fragment include has no include guard, the vendored
// fragment text is the body). NO new body in this render: the
// emission family is led as a fragment ledger (the pure-ledger
// convention: the generated C text is the H-013 vendor-truth
// quote below, each line's exception code names its landed
// const - INPUT_ERROR 6, INPUT_LENGTH 10, SYMTAB_ERROR 9,
// GLOBAL_NOT_FOUND 2, INTERPRETER_ERROR 8,
// INVALID_CONVERSION 1, DATA_RANGE_ERROR 4, and NUMERIC_RANGE
// 5 / UNSPECIFIED 0 whose landed seats are the
// MpstimeMain/Getstr1/LibmpscppMisc5 private consts - the
// Carriers.kt/libmpscpp.h const family). Consult H-004
// hit-reuse: code-fragment ledger convention. Naming per
// H-017.
// Consult: unit 296, entry H-004, outcome hit-reuse,
// timestamp 2026-11-22T06:18:00Z.
//
// Fragment ledger (the nine emitted lines, vendor order):
// - the if/else-if CHAIN template emitted around the Global
//   ExceptionCode test, per code the printed message text:
//   INPUT_ERROR -> Input Error, INPUT_LENGTH -> Input Length
//   Error, SYMTAB_ERROR -> Symbol Table Error, GLOBAL_NOT_
//   FOUND -> Global Not Found Error, INTERPRETER_ERROR ->
//   Interprerer Error (the VENDOR MISSPELLING "Interprerer" is
//   kept verbatim, H-013), INVALID_CONVERSION -> Invalid
//   Conversion Error, DATA_RANGE_ERROR -> Data Range Error,
//   NUMERIC_RANGE -> Numeric Range Error, UNSPECIFIED ->
//   Unspecified Error; the "cout << ... << endl" emitted text
//   is the fragment's inner line.
// The emitted chain is the runtime MumpsGlobalException
// handler skeleton of the generated program; the render family
// cites the MumpsGlobalException/GlobalExceptionCode carriers
// (Carriers.kt:222-246) as the study-side counterpart seats.
