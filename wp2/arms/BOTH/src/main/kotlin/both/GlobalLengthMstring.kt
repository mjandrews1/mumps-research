package both

// Unit 286 (BOTH B10, simple): libmpscpp.cpp::global::Length
// (17 LOC). Batch_plan ordinal 286; units.csv row 298. The
// mstring-needle overload of the global length member face,
// at libmpscpp.cpp:3306 - OVERLOAD DISAMBIGUATION (the
// units.csv duplicate-id ledger, the file-stable score sort,
// reserved by the unit-159 render): the 17-LOC score-8.1
// rank-64 batch-10 row is the mstring-needle twin; the
// 17-LOC score-7.7 rank-56 row is the char-needle twin
// (rendered at the unit-288 ledger); the 23-LOC global-needle
// twin seats at the batch-5 row (unit 133, GlobalLength.kt)
// and the std::string twin at the batch-6 row (unit 159,
// GlobalLengthString.kt); the no-arg overload (:3261) is
// non-roster.
//
// SAME-BODY FOLD (the Sim1W standing rule, the unit-159
// reservation): the vendor overloads differ only in the
// needle adapter (mstring/string .c_str() vs char*); the
// unit-159 std::string seat already renders the identical
// walk with the String needle carrier, and the mstring
// adapter folds to the same String. THIS row therefore binds
// to the unit-159 seat (globalLength(ref: String, needle:
// String)) with NO new Kotlin symbol - the JVM signature
// would collide, and the fold is behavior-identical.
//
// BOTH consults (find_rule before render, events logged):
//   h014 out_of_scope_call - reused: the same-body fold binds
//     this row to the unit-159 seat (GlobalLengthString.kt);
//     the Mglobal RETRIEVE and fail-throw idiom ride the
//     mglobalRetrieve seat inside it.
//   h017 naming - reused: no symbol is emitted; the ledger
//     keeps the vendor overload name (global::Length).
//   h019 class_member_carrier - reused: member of the global
//     class (layout owner roster 89, untranslated); the fold
//     carries the receiver state through the unit-159
//     package-level receiver.
//   Dafny: exempt (the piece-count walk is specified once in
//     spec/length_count.dfy, shared - the unit-159 record).

// (no new Kotlin: the mstring-needle overload folds to the
// unit-159 seat globalLength(ref: String, needle: String)
// - see the same-body fold above)
