package daf

// Unit 149 (DAF B5 T3, complex): include/mumpsc/mstringClass.h::<misc>
// (units.csv row 130: loc 331, cr 0.006, ptr 0, ctl 2, score 51.1,
// rank 234, complex): the mstring wrapper-class declaration residue:
// the full method/operator prototype lattice of the C++ mstring class
// (ctors over char*/long/int/double/float/char/mstring/string, the
// friend declarations, Eval, Translate/Soundex/replace/decorate/
// ends/begins, Assign/Concat sets, Reverse, ReadLine, the numeric
// conversion operators, Pattern, Query/Qlength/Qsubscript/FixGblRef,
// Justify, Fnumber, Length, Ascii, zBlanks, Extract, Find, Piece,
// the friend operator families over int/long/double/float/string/
// global/mstring/char*, the relational operator sets, the stream
// operators, and the private data/name string pair).
// Consults: BASE-log row 101 hit-adapt (no Kotlin mstring class: the
// receiver (this->data) and args become explicit String seats at
// every render site; this residue unit therefore carries the
// DISPOSITION LEDGER of the whole class, and each body renders at its
// owning roster unit - exactly the unit-97/120 wrapper precedent),
// row 98 hit-reuse (the overload lattice collapses onto the String /
// Long / Double families per the row-98/99 collapse fold: e.g. the 8
// operator< and 8 operator> sets collapsed at unit 120), row 6/22
// hit-reuse (the private data/name carrier pair folds onto the String
// seats; no singleton - mstring instances are values), row 37
// hit-adapt (friend decls and ctors are call-shaping only: no code
// emitted, the carrier seats bind at call sites).
// Disposition ledger (member group -> render home):
//   relational/equality operator bodies (==/!=/<=/>= sets, the
//     LibmpscppMisc5 chunk) - rendered THIS tranche (unit 154,
//     LibmpscppMisc5.kt, explicit-name seats)
//   operator* family, operator</> sets, operator/ assignments,
//     Concat/Assign partial bodies - b1/b2/b4 chunk homes (unit 120
//     LibmpscppMisc9.kt carried the b4 slice; the rest pending)
//   Length() - rendered: MstringLength.kt (unit 129, b5 T1)
//   Fnumber - rendered: InterpFnumber.kt / MstringFnumber.kt kin
//   Justify - the two-arg Justify wrapper body lives in a later
//     libmpscpp chunk home (pending; owner-named)
//   Query/Qlength/Qsubscript/FixGblRef - the \\$query service family;
//     Qlength rendered THIS tranche (unit 145, Qlength.kt);
//     Qsubscript stub qsubDaf (b7); Query/FixGblRef owner-named
//     (not corpus units; global-class service family)
//   Eval - stub evalDaf (b9)
//   Translate/Soundex/replace/decorate/ends/begins - the wrapper
//     service bodies live in the libmpscpp chunks (b6-b10 homes;
//     pending), owner-named
//   Reverse, zBlanks, Extract(1,=-1), Find, Piece - the mstring
//     method bodies ride the \\$reverse/\\$zblanks/\\$extract/\\$find/\\$piece
//     service renders of the interp TU (b6+/later cells; pending;
//     the _piece->pieceDaf/pieceBody seat precedent of unit 114)
//   ReadLine (3 overloads) - stdio/istream seats (row 60 family);
//     pending, owner-named (file input service not consumed by any
//     in-scope DAF render)
//   Pattern - the ?-operator class bridge: owner-named (the rpm
//     bridge renders at unit 152 this tranche, RpmMisc.kt, which
//     carries the pm() service instead of this class method)
//   numeric conversion operators - the explicit conversion folds of
//     row 98 (atol/atof seats at call sites; disposition-only here)
//   stream << / >> operators - the print/read service seats render
//     at call sites (row 26 fprintf / row 60 stdio folds; no code
//     here)
//   ctors - value-construction folds (row 33 malloc family); the
//     mcvt kin renders at the mcvt owner (chunk-boundary carry,
//     unit 154 ledger).
// Dafny: EXEMPT (declaration residue; journal row).
//
// DAF consult: BASE-log row 101, outcome hit-adapt
// (2026-11-22T15:13:00Z). DAF consult: BASE-log row 98, outcome
// hit-reuse (2026-11-22T15:13:30Z). DAF consult: rows 6/22, outcome
// hit-reuse (2026-11-22T15:14:00Z). DAF consult: row 37, outcome
// hit-adapt (2026-11-22T15:14:30Z).
// Dafny: EXEMPT (residue; journal row).
