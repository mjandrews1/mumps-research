package doc

// Unit 98 (B4 tranche 1): libmpscpp.cpp::Count (batch 4, simple
// stratum; LOC 17, score 8.1) - the free count-intrinsic wrapper, the
// unit-68 Max sibling. The vendor signature keeps the MSV pointer and
// ep parameters dead for call-shape fidelity (H-013); arg1 is mutated
// in place (trailing SOH pair strip then SOH scan, the Max quirk kept:
// the i==1 edge reads arg1[-1] in C - vendor expression kept with a
// note, observable only on a degenerate 1-char control-prefixed call).
// The C static char tmp[32] folds to a fresh String return per H-008
// (no caller can alias the old buffer). The class member
// global::Count (libmpscpp.cpp:2257) is NOT a corpus unit: it rides
// the new owner-named H-014 stub Stubs.globalCount (raised by this
// unit, sibling of globalMax unit 68 and of the REAL unit-44
// GlobalRef.Min render). sprintf "%g" renders as String.format
// Locale.US per H-009 (the C exponent/trailing-zero shape is a
// documented adaptation).

fun Count(svptr: Msv, ep: String, arg1: CharArray): String {
    var i = CstringOps.strlenC(arg1)

    // trailing SOH pair strip (the i==1 edge reads index -1 in C;
    // vendor expression kept, see header)
    if (i > 0 && arg1[i - 1] == CHAR_1 && arg1[i - 2] == CHAR_1) {
        arg1[i - 2] = NUL
    }

    i = 0
    while (arg1[i] != NUL) {
        if (arg1[i] == CHAR_1) break
        i++
    }

    val j = arg1[i]
    arg1[i] = NUL

    val a = GlobalRef(CstringOps.cstringAt(arg1, 1)) // bypass caret

    val x = if (j == NUL) {
        Stubs.globalCount(a(""))
    } else {
        Stubs.globalCount(a(CstringOps.cstringAt(arg1, i + 1)))
    }

    return String.format(java.util.Locale.US, "%g", x)
}
