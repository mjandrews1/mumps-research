package both

// Unit 189 render: compiler-sys1.c::match (roster 189, batch 7,
// 95 LOC). The compiler-side MUMPS pattern-match classifier.
//
// SAME-BODY DUAL SEAT: the vendor carries a second, effectively
// identical copy of match() in compiler-sys1.c alongside the
// interp-parse.cpp copy seated at unit 181 (Match.kt). The two
// bodies differ ONLY in whitespace and (const char *) casts
// (verified by source diff this render; no behavioral
// difference). Per the arm's duplicate-body fold precedent
// (the mcvt tail / macro folds), the unit-181 kernel stays the
// single implementation and THIS render owns the sys1 roster
// seat pin as a thin delegate; the admissible-equivalence
// disclosure is journaled as an ADJ flag for PI review.
//
// Consults (rule base wp2/arms/BOTH/rules_v0.pl):
//   h014 out_of_scope_call - reused: the kernel match/substr/
//     xindex/strcmpC seats (Match.kt, unit-181 owner) called
//     direct; no stub raised (the seat is already owned).
//   h015 grammar-collision - reused: no backticks, no raw comment
//     dollars, ASCII only, trailing newline.
//   h017 naming - reused: matchSys1 keeps the vendor match name
//     with the sys1 file qualifier (the package already seats an
//     internal fun match; the JVM signature would collide).
//   h013 dead_store - reused: the sys1 signature declares the
//     rslt out-param; the delegate returns the value directly
//     (the *rslt fold, same as the unit-181 kernel close).
//
// Quirks kept (ledgered):
//   - the sys1 copy reads the pattern table bd[] exactly as the
//     parse copy does (the shared extern bd seat); nothing in
//     the sys1 copy observes compiler state.
//   - the 95 vs 86 LOC delta is formatting only (brace style).

// void match(short pts[3][20], short lx, short ct[],
//            unsigned char *ad, short *rslt) - compiler-sys1.c
internal fun matchSys1(
    pts: Array<IntArray>, // short pts[3][20]
    lx: Int,
    ct: IntArray, // short ct[]
    ad: CharArray,
    bd: CharArray,
): Int { // the *rslt fold (0 fail / 1 match)
    // same-body delegate to the unit-181 kernel (owner pin:
    // this roster seat 189; kernel owner roster 181)
    return match(pts, lx, ct, ad, bd)
}
