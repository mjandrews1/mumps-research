package daf

// Unit 221 (DAF B8 T1, simple): include/mumpsc/zfcns.h::<misc>
// (18 LOC, score 6.2): the zfcns.h (bifs.h) header residual:
// license banner, the BIFS_H include guard, the stateVector.h
// include (the unit-172 StateVectorHMisc render), and the
// runtime-library prototype roster. All derived from the pristine
// vendor source this session (vendor tarball sha256 621deb...ffdbe;
// physically verified; the nonblank 18 matches the roster row).
// Consults (2026-11-24T21:12:30Z, nominal): the prototype dispatch
// ledger of the include convention, each named seat citing its
// landed or future owner row.
//
// Prototype ledger (disposition per line):
// - void _ascii(unsigned char *, unsigned char *, unsigned char *):
//   RELEASED batch 9 unit 266 (asciiDaf at InterpSymMisc.kt; the
//   interp-sym.cpp file-prelude residue chunk owns the body).
// - void _horolog(unsigned char *): named stub seat horologDaf
//   (LibmpscppMisc6.kt; owner libmpscpp.cpp:3446; the mstring
//   Horolog relays through it). RELEASED batch 9 unit 247 (the
//   _horolog roster row body landed at the seat; real body).
// - void _justify(...): RENDERED as the unit-194 seat
//   (InterpJustify.kt, interp-sym.cpp::_justify).
// - void _length(...): RENDERED as the unit-305 seat
//   (InterpSymLength.kt, lengthSymD; the sym_-native length
//   counter over the xindexDaf walk; release record this row).
// - void _piece(..., struct MSV *): RENDERED as the unit-114 real
//   (InterpPieceMisc.kt; the pieceDaf seat).
// - void _random(unsigned char *, unsigned char *): not yet
//   rendered; forward-tag seat rides a later-cycle roster row.
// - zfcn: the include guard pair is the _INTERP_ arm - the compiler
//   build (NOT the interpreter build, hence the #ifndef _INTERP_)
//   declares zfcn(unsigned char *, unsigned char *, struct MSV *);
//   the seat is the landed zfcnDaf real (Zfcns.kt, the
//   interp-zfcns.cpp::zfcn roster render; the private zfcnDaf stub
//   of MdhTreeLevelAdd.kt predates it and stays shadowed).
// - void ServiceInterface(...): RENDERED as the unit-215 seat
//   (InterpService.kt, serviceInterfaceD).
//
// The two commented vendor lines // int BuildGlobal(...) and
// // int BuildLocal(...) are units 230/231 of THIS batch (renders of
// this push): BuildGlobal is the interp-parse.cpp row, BuildLocal
// the interp-sym.cpp row. Kept verbatim as header comments per the
// dead-code discipline; their owner note flips from commented to
// rendered in this batch without touching this ledger.
//
// No runtime seat: the chunk is a header (banner, guard, include
// and prototypes only - every function body belongs to the owner
// rows noted above).
// Dafny: EXEMPT (declaration residue; journal row).
