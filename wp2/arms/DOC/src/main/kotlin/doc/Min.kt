package doc

// Unit 99 (B4 tranche 1): libmpscpp.cpp::Min (batch 4, simple
// stratum; LOC 17, score 8.1) - the free min-intrinsic wrapper, the
// unit-68 Max sibling and byte-identical in shape to unit 98 Count.
// The vendor signature keeps the MSV pointer and ep parameters dead
// for call-shape fidelity (H-013); arg1 is mutated in place (trailing
// SOH pair strip then SOH scan, the Max i==-1 edge quirk kept as in
// Count). The C static char tmp[32] folds to a fresh String return
// per H-008. DIVERGENCE FROM THE SIBLINGS: the class member
// global::Min IS in scope - it is the DONE batch-2 unit-44 render
// (GlobalRef.Min()); this unit therefore makes NO out-of-scope
// consult and calls the REAL member (where Count rides a stub and
// Max raises one - the J-mirror asymmetry is data, kept as-is).
// sprintf "%g" renders as String.format Locale.US per H-009.

fun Min(svptr: Msv, ep: String, arg1: CharArray): String {
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
        a("").Min()
    } else {
        a(CstringOps.cstringAt(arg1, i + 1)).Min()
    }

    return String.format(java.util.Locale.US, "%g", x)
}
