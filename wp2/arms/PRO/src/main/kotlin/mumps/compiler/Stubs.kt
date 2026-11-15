package mumps.compiler

/**
 * PRO-arm out-of-scope stubs (h014 out_of_scope_call). Each stub names
 * its owner unit; delegate when the owner lands. Verification: untested
 * (stub — flagged, per rules_v0.pl h014).
 */

/**
 * h005 out_param_bundle: atTest's short out-parameter pd1Last is
 * consumed only via the assignment in the same call, so it rides the
 * return value. Owner: compiler-at-test.c::atTest (not in corpus).
 */
data class AtTestResult(val rc: Int, val pd1Last: Short)

/** Owner: compiler-at-test.c (not in the B1/B2 corpus). */
fun atTest(xd: CharArray, pd1: CharArray, lines: Int, xpx: Long): AtTestResult =
    TODO("h014 stub: owner compiler-at-test.c (not in corpus)")

/** Owner: compiler-at-test.c (not in the B1/B2 corpus). */
fun atCopy(atFlg: Int, tmp: StringBuilder) {
    TODO("h014 stub: owner compiler-at-test.c (not in corpus)")
}

/** Owner: compiler-parse.c (unit not yet translated). */
fun parse_(): Int {
    TODO("h014 stub: owner compiler-parse.c (not yet translated)")
}

/** Owner: compiler-parse.c::Extract2 (B1 unit 15 — delegate when it lands). */
fun extract2(pd1: CharArray, sdlim: Int): String {
    TODO("h014 stub: owner compiler-parse.c::Extract2 (B1 unit 15)")
}

/** Owner: compiler-read.c (not in the B1/B2 corpus). */
fun linepad(n: Int, comment: String) {
    TODO("h014 stub: owner compiler-read.c (not in corpus)")
}
