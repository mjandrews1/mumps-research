package mumps.mdh

import mumps.interp.Msv
import mumps.interp.NUL
import mumps.interp.cstringAt
import mumps.interp.strlenC

/**
 * PRO-arm translation of libmpscpp.cpp::Count (B4 unit 118,
 * rank 66). Translated independently from the pristine vendor
 * source (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe),
 * reference source lines 10141-10165.
 *
 * Identity verified against the study tool (tools/select_study_
 * subset.py): libmpscpp.cpp::Count, cpp, loc 17, comment_ratio
 * 0.056, pointer_marks 0, control_marks 4, score 8.1 - exact
 * match to wp1/study-subset/units.csv row 119.
 *
 * The free MSV wrapper for the MUMPS count-over-children
 * intrinsic (the Max unit-88 wrapper shape): split the coded
 * global reference at the SOH separator (bypassing the caret),
 * then call global::Count() over the unsubscripted or
 * subscripted reference and render the double through sprintf
 * "%g" into the static 32-byte tmp.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h008 static_scratch    : vendor "static char tmp[32]"
 *    persists across calls - carried on the CountScratch
 *    object (MaxScratch precedent, unit 88).
 *  - h009 format_string     : sprintf(tmp,"%g",x) via the JVM
 *    %g shape over the double (Max precedent).
 *  - h012 unsigned_byte_domain: arg1 is a Char(0..255) C-string
 *    domain; the SOH sentinel compares against the Char(1)
 *    literal.
 *  - h014 out_of_scope_call : global::Count (libmpscpp.cpp:1811,
 *    not a corpus unit) -> NEW owner-named MdhStubs stub raised
 *    by this unit; the global ctor and invoke ride the u31/u14
 *    carriers.
 *  - h015 grammar_collision : pre-push scan incl. cross-file
 *    top-level names: new top-level Count (the GlobalRef member
 *    stub is a receiver-scoped name) - no collisions.
 *  - h017 naming            : Count keeps the vendor spelling.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The leading double-SOH strip ("if (i>0 && arg1[i-1]==1
 *    && arg1[i-2]==1) arg1[i-2]=0;") mutates the caller's buffer
 *    in place - kept (the Max wrapper's identical strip).
 *  - If no SOH is found the loop exits at the NUL, so
 *    j = arg1[i] = 0 and the unsubscripted arm a("").Count()
 *    runs - kept.
 */
/** vendor: static char tmp[32] - persists across calls (h008). */
object CountScratch {
    val tmp = CharArray(32)
}

fun Count(svptr: Msv, ep: CharArray?, arg1: CharArray): CharArray {
    val tmp = CountScratch.tmp

    var i = strlenC(arg1)

    // vendor: if (i> 0 && arg1[i-1]==1 && arg1[i-2]==1) arg1[i-2]=0;
    if (i > 0 && arg1[i - 1] == 1.toChar() && arg1[i - 2] == 1.toChar()) {
        arg1[i - 2] = NUL
    }

    i = 0
    while (arg1[i] != NUL) { // vendor: for (i=0; arg1[i]!=0; i++)
        if (arg1[i] == 1.toChar()) break
        i++
    }

    val j = arg1[i]
    arg1[i] = NUL

    val a = Global(cstringAt(arg1, 1)) // vendor: global a(&arg1[1]); // bypass ^

    val x = if (j == NUL) {
        a("").Count() // vendor: x = a("").Count();
    } else {
        a(cstringAt(arg1, i + 1)).Count() // vendor: x = a(&arg1[i+1]).Count();
    }

    // vendor: sprintf(tmp,"%g",x);
    val s = String.format("%g", x) // h009: JVM %g shape
    for (k in s.indices) tmp[k] = s[k]
    tmp[s.length] = NUL

    return tmp
}
