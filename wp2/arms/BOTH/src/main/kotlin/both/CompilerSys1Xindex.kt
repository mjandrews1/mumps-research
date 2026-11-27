package both

// Unit 289 (BOTH B10, core): compiler-sys1.c::xindex (21
// LOC). Batch_plan ordinal 289; units.csv row 296. The
// compiler-TU twin of the unit-260 xindex engine: the vendor
// seats the same index-family search body in two TUs
// (compiler-sys1.c, K&R declarations, and interp-strmanip.cpp
// :87, ANSI declarations) - the two bodies are the same
// algorithm with a cosmetic control-flow difference only
// (the compiler TU's goto outer vs the interp TU's flg flag;
// the vendor loops walk, clamp istrt = strt - 1 with the
// strt <= 1 clamp, and return k + 1 on the first full match,
// 0 on no match, identically).
//
// SAME-BODY FOLD (the Sim1W standing rule): the unit-260 seat
// (Xindex.kt, xindex(source: CharArray, ptrn: CharArray,
// strt: Int)) already renders the walk; the compiler-TU row
// binds to that seat with NO new Kotlin symbol - the JVM
// signature would collide, and the fold is
// behavior-identical (the goto/flag difference is not
// observable).
//
// BOTH consults (find_rule before render, events logged):
//   h011 goto - reused and hit: the vendor's goto outer (the
//     only goto in the body) folds to the flg-flag continue
//     of the unit-260 seat - the same action the unit-260
//     render took; no new mechanism.
//   h014 out_of_scope_call - reused: the fold binds this row
//     to the unit-260 seat (Xindex.kt); the B9 unit-260
//     release note in CompilerParseMisc (the "xindex roster
//     107" mislabel fixed to 289 at the B9 close) is the
//     same twin family.
//   h015 grammar_collision - reused: the K&R parameter
//     declaration list renders as the seat's named params;
//     no quoted-C collision occurs in the fold.
//   h017 naming - reused: the vendor name xindex is kept by
//     the seat; the ledger face keeps the TU provenance
//     (CompilerSys1Xindex).
//   Dafny: EXEMPT (index-family string search; the strmanip
//     spec family carries the shape - the unit-260 record).

// (no new Kotlin: the compiler-TU twin folds to the unit-260
// seat xindex(source: CharArray, ptrn: CharArray, strt: Int)
// - see the same-body fold above)
