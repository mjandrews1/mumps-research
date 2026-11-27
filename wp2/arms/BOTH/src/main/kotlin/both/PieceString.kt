package both

// Unit 282 (BOTH B10, simple): libmpscpp.cpp::global::Piece
// (17 LOC). Batch_plan ordinal 282; units.csv row 303. The
// std::string-needle overload of the piece member face, at
// libmpscpp.cpp:7236 - OVERLOAD DISAMBIGUATION (the units.csv
// duplicate-id ledger, the file-stable score sort): the
// 17-LOC score-5.5 rank-33 batch-10 row is the string-pat
// overload; the const-char* twin (rank 32, batch 7) renders
// at Piece.kt (unit 213), the 23-LOC global-pat twin (rank 50)
// seats at the batch-2 row, and the 17-LOC mstring-and-pat
// twin (score 7.3, pointer mark 1) seats at the batch-8 row.
//
// SAME-BODY FOLD (the Sim1W standing rule, adjudicated at the
// Piece.kt twin ledger): the vendor overloads differ only in
// the pat adapter (char*/mstring/string .c_str()); the
// unit-213 const-char* seat already renders the identical
// body with the String pat carrier, and the std::string
// adapter folds to the same String. THIS row therefore binds
// to the unit-213 seat (GlobalRef.Piece(pat: String, ii, jj))
// with NO new Kotlin symbol - the JVM signature would
// collide, and the fold is behavior-identical.
//
// BOTH consults (find_rule before render, events logged):
//   h014 out_of_scope_call - reused: the same-body fold
//     binds this row to the unit-213 seat (Piece.kt); the
//     _pieceZb engine stub (owner interp-piece.cpp::<misc>,
//     roster 114 - the old pin said roster 103, its units.csv
//     row, a C-015-family mislabel renumbered on this push)
//     stays seated per the h014 action.
//   h017 naming - reused: no symbol is emitted; the ledger
//     keeps the vendor overload name (global::Piece).
//   h019 class_member_carrier - reused: member of the global
//     class (layout owner roster 89, untranslated); the
//     fold carries the receiver state through the unit-213
//     GlobalRef receiver.
//   Dafny: exempt (the piece-count family spec carries the
//     shape; the fold adds no surface - journaled).

// (no new Kotlin: the string-pat overload folds to the
// unit-213 seat GlobalRef.Piece(pat: String, ii: Int, jj: Int)
// - see the same-body fold above)
