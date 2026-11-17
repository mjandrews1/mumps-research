package mumps.mdh

import mumps.interp.Msv
import mumps.interp.NUL
import mumps.interp.cstringAt
import mumps.interp.strlenC

/**
 * PRO-arm translation of libmpscpp.cpp::Sum (B6 tranche C, unit
 * 182, rank 70). Translated independently from the pristine
 * vendor source (mumps-14-may-2026.tgz, sha256
 * 621deb25...ffdbe), reference source lines 10289-10314.
 *
 * Identity verified against the study tool
 * (tools/select_study_subset.py): Sum, cpp, loc 17,
 * comment_ratio 0.056, pointer_marks 0, control_marks 4,
 * score 8.1 - exact match to wp1/study-subset/units.csv row 182
 * (batch 6).
 *
 * char* Sum(MSV* svptr, char* ep, char* arg1): the MSV wrapper
 * over global::Sum() (the XNEXT sibling-sum walk, B5 unit 142,
 * GlobalSum.kt) - strips the trailing double-SOH, splits the
 * wrapper arg at the first SOH into the global ref (bypassing
 * the leading caret) and the subscript tail, sums the node
 * (empty-subscript arm when no SOH was found), and sprintf
 * "%g"-renders the double into the static 32-byte tmp.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h008 static_scratch    : vendor "static char tmp[32]"
 *    persists across calls - carried on the SumScratch object
 *    (Count u118 precedent, identical shape).
 *  - h009 format_string    : sprintf(tmp,"%g",x) renders via
 *    String.format with the EXPLICIT default Locale (the C
 *    printf locale semantics; ImplicitDefaultLocale
 *    potential-bugs rule satisfied, Count precedent).
 *  - h012 unsigned_byte_domain: arg1/tmp NUL-terminated byte
 *    domains; the SOH scan and strlen ride the C byte domain
 *    (strlenC seat).
 *  - h014 out_of_scope_call : global::Sum() -> Global.Sum()
 *    (B5 unit 142 real seat, GlobalRef.Sum() delegate); the
 *    global constructor / invoke-operator carrier (unit 31 /
 *    unit 67 family); ep is the vendor's dead entry-point
 *    parameter - kept in the signature, unused.
 *  - h015 grammar_collision : pre-push scan incl. cross-file
 *    top-level names: new top-level Sum (the Global/GlobalRef
 *    members are receiver-scoped; no Kotlin java.lang.Math
 *    collision - no top-level sum in the arm) - clean.
 *  - h017 naming            : Sum keeps the vendor spelling.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The leading double-SOH strip ("if (i> 0 && arg1[i-1]==1
 *    && arg1[i-2]==1) arg1[i-2]=0;") mutates the caller's
 *    buffer in place - kept (the Count/Max wrappers' identical
 *    strip).
 *  - If no SOH is found the scan loop exits at the NUL, so
 *    j = arg1[i] = 0 and the unsubscripted arm a("").Sum()
 *    runs - kept.
 */
/** vendor: static char tmp[32] - persists across calls (h008). */
object SumScratch {
    val tmp = CharArray(32)
}

fun Sum(svptr: Msv, ep: CharArray?, arg1: CharArray): CharArray {
    val tmp = SumScratch.tmp

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
        a("").Sum() // vendor: x = a("").Sum();
    } else {
        a(cstringAt(arg1, i + 1)).Sum() // vendor: x = a(&arg1[i+1]).Sum();
    }

    // vendor: sprintf(tmp,"%g",x);
    val s = String.format(java.util.Locale.getDefault(), "%g", x) // h009: JVM %g shape; explicit default locale = C printf locale semantics (potential-bugs rule satisfied, behavior byte-identical)
    for (k in s.indices) tmp[k] = s[k]
    tmp[s.length] = NUL

    return tmp
}
