package mumps.compiler

/**
 * PRO-arm out-of-scope stubs (h014 out_of_scope_call). Each stub names
 * its owner unit; delegate when the owner lands. Verification: untested
 * (stub - flagged, per rules_v0.pl h014).
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

/**
 * atCopy (compiler-mumps.c:1956) was stubbed here with a WRONG
 * owner tag (compiler-at-test.c; u127 rpm precedent - the
 * B7 unit 196 misc chunk owns it); the owner-level definition
 * now lives in CompilerMumpsMisc.kt (same StringBuilder shape;
 * the IfCode/SetCode/WriteCode call sites bind unchanged) and
 * the stub has been removed.
 */

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

/**
 * epi (compiler-epi.c::epi) was stubbed here until B7 unit 189
 * landed; the owner-level definition now lives in Epi.kt (same
 * signature; the batch-7 corpus unit, roster row 189) and the stub
 * has been removed.
 */

/**
 * EP (compiler-mumps.c::EP) was stubbed here until B7 unit 196
 * landed; the owner-level definition now lives in
 * CompilerMumpsMisc.kt (same signature; call sites bind
 * unchanged) and the stub has been removed.
 */

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

/**
 * substr (compiler-sys1.c::substr) was stubbed here until B7
 * unit 213 landed; the owner-level definition now lives in
 * Substr.kt and the stub has been removed (Match.kt and the
 * Patrn call sites bind unchanged).
 */

/**
 * match (compiler-sys1.c::match) was stubbed here until B7 unit
 * 190 landed; the owner-level definition now lives in Match.kt
 * (same signature; batch-7 corpus unit, roster row 190) and the
 * stub has been removed (the Patrn call sites bind unchanged).
 */

// --- added by B4 unit 100 (compiler-doty.c::doty) ---

/**
 * levCheck (compiler-mumps.c:1949 LevCheck) was stubbed here
 * until B7 unit 196 landed (TAG CORRECTION: the stub's "not a
 * corpus unit" tag was wrong, u127 precedent - the misc chunk
 * owns it); the owner-level definition now lives in
 * CompilerMumpsMisc.kt (same signature; the Doty call site
 * binds unchanged) and the stub has been removed.
 */

// --- added by B7 unit 192 (compiler-zcommands.c::zcommands_code) ---

/**
 * fcninit (compiler-fcninit.c::fcninit, batch-7 roster row 195)
 * was stubbed here until B7 landed; the owner-level definition
 * now lives in Fcninit.kt (same signature; the ZcommandsCode.kt
 * call sites bind unchanged) and the stub has been removed.
 */

// --- added by B7 unit 195 (compiler-fcninit.c::fcninit) ---

/**
 * Owner: compiler-sys1.c:963 linepad2 (not a corpus unit; the
 * compiler-sys1.c residue is batch-9 row 258). Raised by the B7
 * fcninit render (Fcninit.kt "Temporary variables" line).
 */
fun linepad2(str: String) {
    TODO("h014 stub: owner compiler-sys1.c:963 linepad2 (not a corpus unit)")
}
