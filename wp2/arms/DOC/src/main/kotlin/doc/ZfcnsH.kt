package doc

// Unit 221 (B8 tranche 1): include/mumpsc/zfcns.h::<misc> (batch
// 8, simple stratum; LOC 18, score 6.2, rank 38). The zfcns.h
// (bifs.h) header residual: license banner, the BIFS_H include
// guard, the stateVector.h include, and the runtime-library
// prototype roster. All derived from the pristine vendor source
// this session (vendor tarball sha256 621deb...ffdbe; physically
// verified; the nonblank 18 matches the roster row). Consult
// H-018 hit-reuse: the prototype dispatch ledger of the include
// convention, each named seat citing its landed or future owner
// row. Naming per H-017.
//
// Prototype ledger (disposition per line):
// - void _ascii(unsigned char *, unsigned char *, unsigned char
//   *): not yet rendered; the batch-10 roster rows own it.
// - void _horolog(unsigned char *): not yet rendered; the
//   batch-10 roster row owns it.
// - void _justify(unsigned char *, ...): RENDERED as the unit-194
//   InterpJustify seat (batch 7).
// - void _length(unsigned char *, ...): not yet rendered; the
//   batch-10 roster row owns it.
// - void _piece(..., struct MSV *): RENDERED as the unit-114 real
//   (batch 4; the Stubs.Mpiece delegate names this owner since
//   the batch-7 push).
// - void _random(unsigned char *, unsigned char *): not yet
//   rendered; the batch-9 row owns it.
// - zfcn: the include guard pair is the _INTERP_ arm - the
//   compiler build (NOT the interpreter build, hence #ifndef
//   _INTERP_) declares zfcn(unsigned char *, unsigned char *,
//   struct MSV *); the seat named by the whole mdh_tree_level_add
//   family renders (Stubs.zfcn, H-014).
// - void ServiceInterface(...): RENDERED as the batch-7 T3 seat
//   (the unit-214/215 rows of the arm ledger).
//
// The two commented vendor lines // int BuildGlobal(...) and
// // int BuildLocal(...) are batch-8 roster rows: BuildGlobal is
// the interp-parse.cpp row and BuildLocal the interp-sym.cpp row
// (both tranche-2 renders of THIS batch). Kept verbatim as
// comments per H-013; their owner note flips from commented to
// rendered in this batch without touching this header ledger.
//
// No runtime seat: the chunk is a header (banner, guard, include
// and prototypes only - every function body belongs to the owner
// rows noted above).
