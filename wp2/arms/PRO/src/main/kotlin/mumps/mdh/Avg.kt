package mumps.mdh

import mumps.interp.Msv
import mumps.interp.NUL
import mumps.interp.cstringAt
import mumps.interp.strlenC

/**
 * PRO-arm translation of libmpscpp.cpp::Avg (B5 tranche C, unit
 * 148, rank 68). Translated independently from the pristine
 * vendor source (mumps-14-may-2026.tgz, sha256
 * 621deb25...ffdbe), reference source lines 10235-10257.
 *
 * Identity verified against the study tool
 * (tools/select_study_subset.py): Avg, cpp, loc 17,
 * comment_ratio 0.056, pointer_marks 0, control_marks 4,
 * score 8.1 - exact match to wp1/study-subset/units.csv row 149
 * (batch 5). Overload disambiguation: the free intrinsic
 * wrapper Avg (loc 17) - the class member global::Avg
 * (libmpscpp.cpp:1726) IS a corpus unit, roster row 262 (batch
 * 9, loc 28, score 15.2) - TAG CORRECTION family: the u88 Max
 * KDoc called its sibling global::Max "not a corpus unit", but
 * global::Max IS roster row 170 (batch 6); this unit raises the
 * GlobalRef.Avg stub with the correct batch-9 owner tag (u127
 * rpm / u141 Transpose precedent), it resolves when batch 9
 * lands.
 *
 * char* Avg(MSV* svptr, char* ep, char* arg1): the MUMPS
 * average intrinsic over a global's children - strips the
 * trailing SOH pair quirk, splits arg1 at the first SOH into
 * name and subscript, then delegates to global::Avg on the
 * (optionally subscripted) reference and formats the result
 * with %g.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h008 static_scratch    : vendor "static char tmp[32]"
 *    persists across calls - carried on the AvgScratch object
 *    (ScanAlnumScratch precedent, unit 79; Max unit 88).
 *  - h009 format_string     : sprintf(tmp,"%g",x) rendered as
 *    java.util.String.format over the %g conversion with an
 *    EXPLICIT default locale (the potential-bugs
 *    ImplicitDefaultLocale rule stays hot - unit-88 gate-round
 *    lesson; behavior byte-identical to C printf).
 *  - h012 unsigned_byte_domain: arg1 is a C-string domain; the
 *    SOH sentinel tests compare against Char(1) (SOH pair
 *    quirk, Min unit 44 / Max unit 88 precedent).
 *  - h013 dead_store        : the parameters svptr and ep are
 *    never used in the vendor body - kept in the signature for
 *    call-shape fidelity, documented.
 *  - h014 out_of_scope_call : global::Avg (owner libmpscpp.cpp:
 *    1726, corpus unit roster row 262, BATCH 9 - out of this
 *    batch's scope) -> new MdhStubs stub raised by this unit
 *    (GlobalRef.Avg seat, u88 Max precedent); strlen resolves
 *    to the real B1 strlenC helper - reused unmodified.
 *  - h015 grammar_collision : pre-push scan incl. cross-file
 *    top-level names: new top-level Avg - no collisions (the
 *    new GlobalRef.Avg stub extension is a distinct carrier
 *    member).
 *  - h017 naming           : Avg keeps the vendor name; the
 *    char* return is the scratch CharArray (NUL-terminated).
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The trailing-SOH strip reads arg1[i-2] when i==1 (only
 *    arg1[0] is in bounds) - the vendor relies on the ^-bypass
 *    layout keeping at least two bytes; kept faithful (Max
 *    unit-88 note).
 *  - The SOH-split loop scans for the FIRST Char(1); when none
 *    is found the loop exits at the NUL, so j = 0 and the
 *    unsubscripted arm a("").Avg() runs - kept.
 *  - arg1 is MUTATED in place (SOH replaced by NUL at index i) -
 *    the caller's buffer is truncated after the call; kept.
 */
/** vendor: static char tmp[32] - persists across calls (h008). */
object AvgScratch {
    val tmp: CharArray = CharArray(32)
}

fun Avg(svptr: Msv, ep: CharArray?, arg1: CharArray): CharArray {
    val tmp = AvgScratch.tmp

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
        a("").Avg() // vendor: x = a("").Avg();
    } else {
        a(cstringAt(arg1, i + 1)).Avg() // vendor: x = a(&arg1[i+1]).Avg();
    }

    // vendor: sprintf(tmp,"%g",x);
    val s = String.format(java.util.Locale.getDefault(), "%g", x) // h009: explicit default locale = C printf locale semantics (potential-bugs rule satisfied, behavior byte-identical)
    for (k in s.indices) tmp[k] = s[k]
    tmp[s.length] = NUL

    return tmp
}
