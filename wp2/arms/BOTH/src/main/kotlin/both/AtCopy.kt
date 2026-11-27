package both

// Unit 240 render: compiler-mumps.c::atCopy (roster 240, batch 8,
// 16 LOC; the WriteCode TODO stub carried a "roster 241" pin -
// the grep-line mislabel family, corrected here). void atCopy(int
// at_flg, unsigned char tmp[]) - the @-text copy walk: from xpx
// through at_flg in the compiler xd arena, copying bytes into
// tmp[2..] and doubling the quote and backslash bytes; tmp[0..1]
// belong to the caller's "w " prefix (the atTest seat shape).
//
// Consults (rule base wp2/arms/BOTH/rules_v0.pl):
//   h014 out_of_scope_call - reused: the compiler TU globals ride
//     the CompilerGlobalsExt singleton (xdG the :44 xd seat,
//     xpxG the :38 Long xpx seat - the interp-side Msv xd/xpx
//     pair is the stateVector.h:45/56 collision, kept distinct).
//   h015 grammar-collision - reused: ASCII only, no backticks,
//     no raw comment dollars, trailing newline.
//   h017 naming - reused: atCopy keeps the vendor name and the
//     lowercase carrier call sites (the WriteCode stub seat
//     released - this file owns the body now).
//
// Quirks kept (ledgered):
//   - the loop guard reads xd[i] BEFORE the i<at_flg bound (the
//     vendor tests the byte first, the bound second; the Kotlin
//     && keeps the same order).
//   - the quote/backslash doubling writes the escape byte then
//     the literal (kept as two indexed stores).
//   - the trailing NUL lands at tmp[j] after the walk (the
//     caller's prefix at tmp[0..1] is never re-touched).

internal fun atCopy(atFlg: Int, tmp: CharArray) {
    val g = CompilerGlobalsExt
    var i = g.xpxG.toInt() // for (i = xpx, j = 2; ...)
    var j = 2

    while (g.xdG[i] != '\u0000' && i < atFlg) {
        val c = g.xdG[i]
        if (c != '"' && c != '\\') {
            tmp[j++] = c
        } else if (c == '"') {
            tmp[j++] = '\\'
            tmp[j++] = '"'
        } else {
            tmp[j++] = '\\'
            tmp[j++] = '\\'
        }
        i++
    }
    tmp[j] = '\u0000'
}
