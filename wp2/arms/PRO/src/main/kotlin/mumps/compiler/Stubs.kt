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

// (B4 unit 108) atTest landed in AtTest.kt: its h014 stub is REMOVED
// (cleanLocks precedent, unit 56); the real atTest reads the caller's
// pd1Last (imbalanced-quotes diagnostic) and echoes it back in the
// AtTestResult bundle (h005 shape kept; the old stub's "not in
// corpus" owner note was wrong - scope note corrected).

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
 * compiler-set.c::set_code (B4 unit 96) was stubbed here until
 * SetCode.kt landed; the owner-level definition now lives in
 * SetCode.kt (fun setCode(entry_pt: Int)) and the stub has been
 * removed. The NewCode.kt and LockCode.kt call sites were updated
 * in the same tranche.
 */

/**
 * Owner: compiler-parse.c:2074 cvar (not a corpus unit). Raised by
 * B4 units 95/96 (for_code/set_code); scans the compiler's declared
 * C-variable table (cvars/cvarx, compiler-parse.c:187-189).
 */
fun cvar(str: String): Boolean =
    TODO("h014 stub: owner compiler-parse.c:2074 cvar (not a corpus unit)")

// --- added by B3 unit 68 (compiler-sys1.c::patrn) ---

/**
 * cannon (compiler-sys1.c::cannon) was stubbed here until B3 unit
 * 72 landed; the owner-level definition now lives in Cannon.kt and
 * the stub has been removed.
 */

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

// --- added by B4 unit 100 (compiler-doty.c::doty) ---

/** Owner: compiler-mumps.c:1949 LevCheck (not a corpus unit). */
fun levCheck(ExecName: Short, DoRetX: Short, DoRet: ShortArray): Int {
    TODO("h014 stub: owner compiler-mumps.c:1949 LevCheck (not a corpus unit)")
}
