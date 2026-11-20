package doc

// Unit 130 (B5 tranche 1): libmpscpp.cpp::Avg (batch 5, simple
// stratum; LOC 17, score 8.1) - the free avg-intrinsic wrapper, the
// unit-98 Count / unit-68 Max sibling. The vendor signature keeps the
// MSV pointer and ep parameters dead for call-shape fidelity (H-013);
// arg1 is mutated in place (trailing SOH pair strip then SOH scan,
// the family quirk kept: the i==1 edge reads arg1[-1] in C - vendor
// expression kept with a note, observable only on a degenerate 1-char
// control-prefixed call). The C static char tmp[32] folds to a fresh
// String return per H-008. The class member global::Avg IS a corpus
// unit (roster row 262, batch 9): this unit rides the owner-named
// H-014 stub Stubs.globalAvg, which the batch-9 unit will delegate
// to when it lands. sprintf "%g" renders as String.format Locale.US
// per H-009 (the C exponent/trailing-zero shape is a documented
// adaptation).

fun Avg(svptr: Msv, ep: String, arg1: CharArray): String {
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
        Stubs.globalAvg(a(""))
    } else {
        Stubs.globalAvg(a(CstringOps.cstringAt(arg1, i + 1)))
    }

    return String.format(java.util.Locale.US, "%g", x)
}
