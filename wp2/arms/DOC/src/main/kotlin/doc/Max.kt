package doc

// Unit 68 (B3 tranche 1): libmpscpp.cpp::Max (batch 3, simple
// stratum; LOC 17, score 8.1) - the free max-intrinsic wrapper. The
// vendor signature keeps the MSV pointer and ep parameters dead for
// call-shape fidelity (H-013); arg1 is mutated in place (truncated at
// the SOH). VENDOR UB EDGE kept in shape with a note: when i==1 the
// trailing-pair test reads arg1[i-2] == arg1[-1] in C (undefined);
// the render keeps the vendor expression (the unit-44 GlobalMin
// trailing-trim precedent), the edge being observable only on a
// degenerate 1-char control-prefixed call. The C static char
// tmp[32] folds to a fresh String return per H-008 (no caller can
// alias the old buffer). TAG CORRECTION with unit 171 (B6 tranche
// 2): the class member global::Max (libmpscpp.cpp:1651) IS a
// corpus unit after all - the batch-6 roster row (loc 26, score
// 14.8); its GlobalMax.kt unit-171 render now carries the real
// XNEXT walk and the Stubs.globalMax seat delegates to it. The
// earlier "NOT a corpus unit" note was a roster mis-read,
// journaled with the unit-171 render.
// sprintf "%g" renders as String.format Locale.US (H-009: the JVM
// %g shape is a documented adaptation - C exponent/trailing-zero
// rendering differs).

fun Max(svptr: Msv, ep: String, arg1: CharArray): String {
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
        Stubs.globalMax(a(""))
    } else {
        Stubs.globalMax(a(CstringOps.cstringAt(arg1, i + 1)))
    }

    return String.format(java.util.Locale.US, "%g", x)
}
