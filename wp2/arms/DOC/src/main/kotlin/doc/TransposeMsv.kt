package doc

// Unit 137 (B5 tranche 2): libmpscpp.cpp::Transpose (batch 5, core
// stratum; LOC 23, score 14.1) - the MSV intrinsic WRAPPER, distinct
// from the batch-4 unit-100 std::string overload and from the
// char* / mstring overloads (overload multiplicity ledgered;
// units.csv disambiguates by loc 23 vs 15). ep is a dead parameter
// (H-013, signature kept). VENDOR COPY-PASTE QUIRK kept verbatim and
// ledgered: the second top-level guard tests arg2 at index i (the
// FIRST argument's scan index), not j - not fixed. The name-only
// arms write the NUL one past the scan index before the SOH is
// cleared, then hand the bytes after the SOH (possibly the empty
// first subscript just written) to the global ctor/invoke pair
// (carrier from index 1 bypasses the caret, Count/Max family
// precedent). printf maps to the JVM console (println, documented
// divergence per H-001); sigint rides the H-014 stub; the
// Transpose(global) class member is NOT a roster unit: the new
// H-014 seat Stubs.globalRefTranspose carries it (owner named).
// The empty-string return renders as the constant "" fresh per call
// (H-008). Naming per H-017.

fun Transpose(svptr: Msv, ep: CharArray, arg1: CharArray, arg2: CharArray): String {
    var i = 0
    while (arg1[i] != NUL) {
        if (arg1[i] == CHAR_1) break
        i++
    }

    if (arg1[i] == NUL) arg1[i + 1] = NUL // array name only - no indices

    if (arg1[i] == CHAR_1) {
        println()
        println("*** Arg 1 must be top level global")
        Stubs.sigint(100)
    }

    arg1[i] = NUL
    val a = GlobalRef(CstringOps.cstringAt(arg1, 1)) // bypass caret

    var j = 0
    while (arg2[j] != NUL) {
        if (arg2[j] == CHAR_1) break
        j++
    }

    if (arg2[j] == NUL) arg2[j + 1] = NUL // array name only - no indices

    // vendor quirk kept: this guard tests arg2 at i, not j (see header)
    if (arg2[i] == CHAR_1) {
        println()
        println("*** Arg 2 must be top level global")
        Stubs.sigint(100)
    }

    arg2[j] = NUL
    val b = GlobalRef(CstringOps.cstringAt(arg2, 1)) // bypass caret

    Stubs.globalRefTranspose(
        a(CstringOps.cstringAt(arg1, i + 1)),
        b(CstringOps.cstringAt(arg2, j + 1)))

    return ""
}
