package mumps.interp

/**
 * PRO-arm translation of interp-sym.cpp::ScanParse
 * (batch-8 unit 234, units.csv data line 234, rank 142).
 * Translated independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), reference
 * source lines 1410-1435.
 *
 * Identity verified against the study tool (tools/select_study_
 * subset.py): interp-sym.cpp::ScanParse, cpp, loc 25,
 * comment_ratio 0.038, pointer_marks 0, control_marks 10,
 * score 14.7, rank 142, batch 8 - exact match to
 * wp1/study-subset/units.csv line 234.
 *
 * ScanParse(char *tmp1): scan one expression/token run - stop
 * at NUL, space, or a depth-0 comma; track parenthesis depth;
 * a double-quote starts an inner run that ends at the NEXT
 * quote (one quote consumed, no escape handling).  Returns
 * the consumed length.
 *
 * This unit RESOLVES the InterpStubs ScanParse h014 stub
 * (raised by unit 94 fcn; the Fcn call site at :161 binds
 * unchanged).
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h012 unsigned_byte_domain: tmp1 is a Char(0..255)
 *    domain; the offset shape keeps the u94 call-site form.
 *  - h015 grammar_collision: top-level ScanParse (stub
 *    removed) - no collisions.
 *  - h017 naming      : ScanParse keeps the vendor name.
 *  - h018 preprocessor_conditional: none in this body.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The quote run: the opening quote is skipped (++i,
 *    continue), the inner loop stops at the first quote
 *    (consuming it, ++i, break) - the CCHR-escaped '\"' in
 *    the vendor is a single double-quote char test (kept).
 *  - The ',' test requires p==0 (depth-0 commas only);
 *    depth is NOT reset on break (kept).
 *  - The trailing commented-out _text_function/
 *    _label_lookup block below the body (vendor 1438-1444)
 *    is dead vendor commentary - kept out (the u202
 *    InterpFcnMisc KDoc documents the __ extern pair).
 */

fun ScanParse(tmp1: CharArray, tmp1Off: Int): Int {
    var i = 0
    var p = 0

    while (true) {
        val c = tmp1[tmp1Off + i]

        if (c == NUL) break
        if (c == ' ') break
        if (c == ',' && p == 0) break
        if (c == '(') p++
        if (c == ')') p--

        if (c != '"') {
            ++i
            continue
        }

        ++i
        while (true) {
            val c2 = tmp1[tmp1Off + i]
            if (c2 == NUL) break
            if (c2 != '"') {
                ++i
                continue
            }
            ++i
            break
        }
    }
    return i
}
