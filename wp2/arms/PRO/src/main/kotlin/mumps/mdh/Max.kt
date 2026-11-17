package mumps.mdh

import mumps.interp.Msv
import mumps.interp.NUL
import mumps.interp.cstringAt
import mumps.interp.strlenC

/**
 * PRO-arm translation of libmpscpp.cpp::Max (B3 unit 88, rank 67).
 * Translated independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), reference
 * source lines 10167-10190.
 *
 * Identity verified against the study tool (tools/select_study_
 * subset.py): libmpscpp.cpp::Max, cpp, loc 17, comment_ratio
 * 0.056, pointer_marks 0, control_marks 4, score 8.1, batch 3 -
 * exact match to wp1/study-subset/units.csv row 67. Overload
 * disambiguation: the free intrinsic wrapper Max (loc 17) - the
 * class member global::Max (libmpscpp.cpp:1651) is NOT a corpus
 * unit (the sibling global::Min was B2 unit 44) and rides a new
 * owner-named MdhStubs stub raised by this unit.
 *
 * char* Max(MSV* svptr, char* ep, char* arg1): the MUMPS max
 * intrinsic over a global's children - strips the trailing SOH
 * pair quirk, splits arg1 at the first SOH into name and
 * subscript, then delegates to global::Max on the (optionally
 * subscripted) reference and formats the result with %g.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h008 static_scratch    : vendor "static char tmp[32]"
 *    persists across calls - carried on the MaxScratch object
 *    (ScanAlnumScratch precedent, unit 79).
 *  - h009 format_string     : sprintf(tmp,"%g",x) rendered as
 *    java.util.String.format over the %g conversion (the JVM %g
 *    shape; the vendor's C-library %g is the same idiom -
 *    adaptation, cf. ShowAlign unit 61).
 *  - h012 unsigned_byte_domain: arg1 is a C-string domain; the
 *    SOH sentinel tests compare against Char(1) (SOH pair quirk,
 *    Min unit 44 precedent).
 *  - h013 dead_store        : the parameters svptr and ep are
 *    never used in the vendor body - kept in the signature for
 *    call-shape fidelity, documented.
 *  - h014 out_of_scope_call : global::Max (owner libmpscpp.cpp:
 *    1651, not a corpus unit; sibling of unit 44's global::Min)
 *    -> new MdhStubs stub raised by this unit; strlen resolves
 *    to the real B1 strlenC helper - reused unmodified.
 *  - h015 grammar_collision : pre-push scan incl. cross-file
 *    top-level names: new top-level Max - no collisions (the
 *    carrier's GlobalRef.Max member extension is a distinct
 *    package-level name).
 *  - h017 naming           : Max keeps the vendor name; the
 *    char* return is the scratch CharArray (NUL-terminated).
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The trailing-SOH strip reads arg1[i-2] when i==1 (only
 *    arg1[0] is in bounds) - the vendor relies on the ^-bypass
 *    layout keeping at least two bytes; kept faithful (a one-
 *    byte buffer would be out of bounds in C too).
 *  - The SOH-split loop scans for the FIRST Char(1); when none is
 *    found the loop exits at the NUL, so j = arg1[i] = 0 and the
 *    unsubscripted arm a("").Max() runs - kept.
 *  - arg1 is MUTATED in place (SOH replaced by NUL at index i) -
 *    the caller's buffer is truncated after the call; kept.
 */

/** vendor: static char tmp[32] - persists across calls (h008). */
object MaxScratch {
    val tmp: CharArray = CharArray(32)
}

fun Max(svptr: Msv, ep: CharArray?, arg1: CharArray): CharArray {
    val tmp = MaxScratch.tmp

    var i = strlenC(arg1)

    // vendor: if (i>0 && arg1[i-1]==1 && arg1[i-2]==1) arg1[i-2]=0;
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
        a("").Max() // vendor: x = a("").Max();
    } else {
        a(cstringAt(arg1, i + 1)).Max() // vendor: x = a(&arg1[i+1]).Max();
    }

    // vendor: sprintf(tmp,"%g",x);
    val s = String.format("%g", x) // h009: JVM %g shape
    for (k in s.indices) tmp[k] = s[k]
    tmp[s.length] = NUL

    return tmp
}
