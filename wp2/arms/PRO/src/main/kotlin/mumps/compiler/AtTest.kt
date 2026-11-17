package mumps.compiler

import mumps.interp.NUL
import mumps.interp.cstringAt

/**
 * PRO-arm translation of compiler-at-test.c::atTest (B4 unit 108,
 * rank 153). Translated independently from the pristine vendor
 * source (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe),
 * reference source lines 33-62.
 *
 * Identity verified against the study tool (tools/select_study_
 * subset.py): compiler-at-test.c::atTest, c, loc 24,
 * comment_ratio 0.04, pointer_marks 2, control_marks 8, score
 * 16.8 - exact match to wp1/study-subset/units.csv row 109.
 *
 * atTest(): scan xd from xpx up to the next blank/NUL, setting
 * at_flg if an at-sign appears, skipping over double-quoted runs
 * (with the vendor's doubled-quote escape); on imbalanced quotes
 * print the three diagnostic lines and return -1; otherwise
 * return the stop position k when an at-sign was seen, else 0.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h002 simple_output_macro : the vendor printf tails are
 *    plain stdout writes - System.out.print with byte-identical
 *    messages (h009: no format specifiers except "%d"/"%s",
 *    rendered via concatenation over the line number and the
 *    pd1 substring).
 *  - h005 out_param_bundle    : the h005 shape raised at the B1
 *    stub is KEPT (AtTestResult{rc, pd1Last}): atTest never
 *    writes *pd1Last in the vendor, so the bundle echoes the
 *    input value back unchanged.
 *  - h010 numeric_promotion    : lines is int in the vendor and
 *    rides Int; xpx is the compiler's long global - Long, with
 *    the loop index k widened Int/Long explicitly; the int
 *    return is rc (callers narrow to short at_flg, unchanged).
 *  - h012 unsigned_byte_domain: xd/pd1 are Char(0..255) C-string
 *    domains; the quote compares are against the byte 34.
 *  - h014 out_of_scope_call    : the Stubs.kt h014 stub is
 *    RESOLVED by this unit and REMOVED (cleanLocks precedent,
 *    unit 56); the owner note in the old stub ("not in corpus")
 *    was wrong - this is the batch-4 corpus unit, scope note
 *    corrected per the unit-25 precedent.
 *  - h015 grammar_collision   : pre-push scan clean (no dollar
 *    bytes; the imbalanced-quote message has no comment closer).
 *  - h017 naming              : atTest keeps the vendor spelling.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The inner quote loop "while (k++)" is the vendor's post-
 *    increment idiom: k is bumped BEFORE the first test, so the
 *    quote character itself is skipped and xd[k+1] is reachable
 *    when xd[k] is the last byte - the rendering keeps the
 *    increments in the same order (k advances, then the tests).
 *  - "if (xd[k] == '\\"' && xd[k+1] == '\\"')" is the doubled-
 *    quote escape: k++ skips the second quote and the scan
 *    continues INSIDE the quoted run (kept exactly).
 *  - The NUL test inside the quote loop reports the imbalanced
 *    quotes error; the trailing line prints &pd1[*pd1Last] -
 *    the caller's current source line pointer (rendered via the
 *    pd1Last parameter; the B1 stub had folded this read away).
 *  - Return -1 vs k vs 0 kept; the caller narrows to short.
 *  - The unused "at_flg = 0" initialization is live (the loop
 *    may run zero iterations: xd[xpx] blank/NUL) - kept.
 */
fun atTest(xd: CharArray, pd1: CharArray, pd1Last: Short, lines: Int, xpx: Long): AtTestResult {
    var atFlg = 0

    var k = xpx.toInt()
    while (xd[k] != NUL && xd[k] != ' ') {

        if (xd[k] == '@') atFlg = 1

        if (xd[k] == '"') {

            k++ // vendor: while (k++) - post-increment before test
            while (true) {

                if (xd[k] == NUL) {
                    System.out.print("*** Imbalanced quotes\n")
                    System.out.print("*** In or near line " + lines + "\n")
                    System.out.print("*** " + cstringAt(pd1, pd1Last.toInt()) + "\n")
                    return AtTestResult(-1, pd1Last)
                }

                if (xd[k] == '"' && xd[k + 1] == '"') {
                    k++
                    k++
                    continue
                }

                if (xd[k] == '"') break

                k++
            }
        }

        k++
    }

    if (atFlg == 1) return AtTestResult(k, pd1Last)
    return AtTestResult(0, pd1Last)
}
