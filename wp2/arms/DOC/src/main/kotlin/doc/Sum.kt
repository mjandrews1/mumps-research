package doc

// Unit 160 (B6 tranche 1): libmpscpp.cpp::Sum (batch 6, simple
// stratum; LOC 17, score 8.1). The free sum-intrinsic wrapper,
// the exact-shape twin of the unit-130 Avg render (SOH trailing
// pair strip, SOH scan, bypass-caret GlobalRef over the arg1
// tail, String.format percent g Locale.US per H-009; the i==1
// edge reads arg1[-1] in C - vendor expression kept, the Avg
// family quirk note applies verbatim). Where Avg rode the
// H-014 Stubs.globalAvg seat, this unit calls the REAL batch-5
// unit-136 global::Sum render (GlobalSum.kt, landed batch 5):
// the subscripted global ref is built with the GlobalRef
// invoke(String) seat (LibMisc1.kt vararg keys carrier) and
// .sum() read. The MSV pointer and ep parameters are dead
// (H-013, kept in shape); arg1 mutates in place as in the
// vendor. The C static char tmp[32] folds to a fresh String
// return per H-008. Naming per H-017.

fun Sum(svptr: Msv, ep: String, arg1: CharArray): String {
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

    // if (!j) x = a("").Sum(); else x = a(&arg1[i+1]).Sum();
    val x = if (j == NUL) {
        a("").sum()
    } else {
        a(CstringOps.cstringAt(arg1, i + 1)).sum()
    }

    return String.format(java.util.Locale.US, "%g", x)
}
