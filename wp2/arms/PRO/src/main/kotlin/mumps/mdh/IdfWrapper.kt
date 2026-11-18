package mumps.mdh

import mumps.interp.Msv
import mumps.interp.cstringAt
import mumps.interp.sigint

/**
 * PRO-arm translation of libmpscpp.cpp::IDF
 * (batch-8 unit 240, units.csv data line 240, rank 42).
 * Translated independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), reference
 * source lines 10370-10388.
 *
 * Identity verified against the study tool (tools/select_study_
 * subset.py): libmpscpp.cpp::IDF, cpp, loc 15,
 * comment_ratio 0.062, pointer_marks 0, control_marks 3,
 * score 6.7, rank 42, batch 8 - exact match to
 * wp1/study-subset/units.csv line 240.
 *
 * IDF(svptr, ep, arg1, arg2): the $zzIDF intrinsic wrapper -
 * split the ^-prefixed global name at its first SOH (a SOH
 * means the reference has subscripts: fatal), take the
 * remainder after the split as the DC subscript, and call the
 * global::IDF(DC) machinery (the pilot Idf.kt seat, unit
 * GlobalRef.IDF).
 *
 * This unit RESOLVES the MdhStubs IDF h014 stub raised at u187
 * (the INVDF ServiceInterface relay is the only in-arm caller;
 * it binds unchanged).
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h004 globals_ext: file-scope svPtr arrives as the
 *    svptr parameter (the wrapper passes it through to the
 *    member machinery via MdhState-agnostic seats).
 *  - h012 unsigned_byte_domain: arg1/arg2 are Char(0..255)
 *    domains; the SOH test reads the raw byte.
 *  - h014 out_of_scope_call: sigint (unit 191) reused; the
 *    sscanf("%lf") folds to the Double parse (u131 atof_
 *    precedent); Global.invoke (unit-31 vararg seat) carries
 *    the operator() subscript fold.
 *  - h015 grammar_collision: top-level IDF (stub removed) -
 *    no collisions (GlobalRef.IDF is receiver-distinct).
 *  - h017 naming      : IDF keeps the vendor name; ep is dead
 *    in the vendor body (kept in the signature).
 *  - h018 preprocessor_conditional: none in this body.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The static char tmp[32] is dead in the wrapper (never
 *    written) - kept out with a note.
 *  - j is declared and never used (dead, kept out).
 *  - The SOH check runs AFTER the scan loop (arg1[i]==1 tests
 *    the byte the loop stopped on; a NUL stop falls through
 *    to the good path) - kept.
 *  - The wrapper returns the EMPTY string (not the member's
 *    result) - kept.
 */

private const val SOH = 1.toChar() // vendor: arg1[i]==1

fun IDF(svptr: Msv, ep: CharArray?, arg1: CharArray, arg2: CharArray): CharArray {
    // vendor: static char tmp[32];  [dead - never written]
    // vendor: int i,j; double x;   [j dead]

    // vendor: for (i=0; arg1[i]!=0; i++) if (arg1[i]==1) break;
    var i = 0
    while (arg1[i] != 0.toChar()) {
        if (arg1[i] == SOH) break
        i++
    }

    if (arg1[i] == SOH) {
        println("\n*** Arg 1 must be top level global")
        sigint(100)
    }

    arg1[i] = 0.toChar() // vendor: arg1[i]=0;
    val a = Global(cstringAt(arg1, 1)) // vendor: global a(&arg1[1]); // bypass ^
    val x = cstringAt(arg2).toDouble() // vendor: sscanf(arg2,"%lf",&x);
    a(cstringAt(arg1, i + 1)).IDF(x) // vendor: a(&arg1[i+1]).IDF(x);

    val r = CharArray(1) // vendor: return (char *) "";
    r[0] = 0.toChar()
    return r
}
