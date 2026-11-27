package both

// Unit 233 render: interp-sym.cpp::ScanParse (roster 233,
// batch 8, 27 LOC). int ScanParse(char * tmp1) - the
// argument-list scanner: walks to the end of the current
// argument (space, NUL, or a paren-depth-0 comma), skipping
// quoted spans (the doubled-quote walk) and tracking the paren
// depth.
//
// Consults (rule base wp2/arms/BOTH/rules_v0.pl):
//   h007 char_buffer - reused: tmp1 is a scan-only input
//     buffer; no mutation, no out-buffer.
//   h014 out_of_scope_call - reused: no callees (pure scan).
//   h015 grammar-collision - reused: ASCII only, no backticks,
//     no raw comment dollars, trailing newline.
//   h017 naming - reused: ScanParse keeps vendor name/case
//     (the compiler-globals.h prototype ledger pin; no Kotlin
//     collision).
//
// Quirks kept (ledgered):
//   - the paren counter p is NOT clamped below zero (a closing
//     paren at depth 0 drives p negative; kept exact).
//   - the quoted-span walk consumes the closing quote then
//     breaks (a doubled quote inside the span is not doubled-
//     back over - the vendor treats the FIRST closing quote as
//     the end; kept).
//   - the comma at p==0 breaks; a comma at p>0 is consumed
//     (kept).
//   - the break tests run in the vendor order: NUL, space,
//     comma-at-depth-0, then the paren/quote bookkeeping
//     (kept exact).

// int ScanParse(char * tmp1) - interp-sym.cpp
internal fun ScanParse(tmp1: CharArray): Int {
    var i = 0
    var p = 0

    while (true) {
        if (tmp1[i] == '\u0000') break
        if (tmp1[i] == ' ') break
        if (tmp1[i] == ',' && p == 0) break
        if (tmp1[i] == '(') p++
        if (tmp1[i] == ')') p--
        if (tmp1[i] != '"') {
            ++i
            continue
        }
        ++i
        while (true) {
            if (tmp1[i] == '\u0000') break
            if (tmp1[i] != '"') {
                ++i
                continue
            }
            ++i
            break
        }
    }
    return i
}
