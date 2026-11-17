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

/** Owner: compiler-parse.c::Extract2 (B1 unit 15 - translated; delegates). */
fun extract2(pd1: CharArray, sdlim: Int): String = Extract2(pd1, sdlim)

/** Owner: compiler-read.c (not in the B1/B2 corpus). */
fun linepad(n: Int, comment: String) {
    TODO("h014 stub: owner compiler-read.c (not in corpus)")
}

/** Owner: compiler-epi.c::epi (not in the B1/B2 corpus). */
fun epi(ExecName: Short, pid: Long) {
    TODO("h014 stub: owner compiler-epi.c::epi (not in corpus)")
}

/** Owner: compiler-mumps.c::EP (not in the B1/B2 corpus). */
fun EP(flg: Int, ExecName: Short, pid: Long) {
    TODO("h014 stub: owner compiler-mumps.c::EP (not in corpus)")
}

/** Owner: compiler-parse.c::Extract1 (compiler-parse.c:2119; not a corpus unit). */
fun Extract1(str: CharArray): String {
    TODO("h014 stub: owner compiler-parse.c::Extract1 (not a corpus unit)")
}

/** Owner: compiler-parse.c:2068 memcheck (not a corpus unit). */
fun memcheck(iiitmp: Int) {
    TODO("h014 stub: owner compiler-parse.c:2068 memcheck (not a corpus unit)")
}

/**
 * Owner: compiler-set.c::set_code (batch-4 corpus unit, 256 LOC).
 * Added by B3 unit 65 (compiler-new.c::new_code).
 */
fun set_code(cmd: Char): Int {
    TODO("h014 stub: owner compiler-set.c::set_code (batch-4 unit)")
}

// --- added by B3 unit 68 (compiler-sys1.c::patrn) ---

/**
 * Owner: compiler-sys1.c::cannon (B3 unit 72 - this batch); prototype
 * compiler-sys1.c:63, definition compiler-sys1.c:971.
 */
fun cannon(a: CharArray) {
    TODO("h014 stub: owner compiler-sys1.c::cannon (B3 unit 72)")
}

/** Owner: compiler-sys1.c::substr (compiler-sys1.c:396, batch-7 unit). */
fun substr(src: CharArray, srcOff: Int, dst: CharArray, start: Int, len: Int) {
    TODO("h014 stub: owner compiler-sys1.c::substr (batch-7 unit)")
}

/**
 * Owner: compiler-sys1.c::match (compiler-sys1.c:453, batch-7 unit);
 * rslt is the short* out-parameter (h006 holder).
 */
fun match(pts: Array<IntArray>, lx: Int, ct: IntArray, ad: CharArray, rslt: ShortArray) {
    TODO("h014 stub: owner compiler-sys1.c::match (batch-7 unit)")
}
