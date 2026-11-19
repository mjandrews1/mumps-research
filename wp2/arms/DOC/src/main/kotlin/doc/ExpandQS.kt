package doc

// Unit 80 (B3 tranche 2): compiler-parse.c::ExpandQS (batch 3, core
// stratum; LOC 48, score 23.0). The indirection statement expander of
// the SAME coded-payload family as ExpandGet (unit 16, B1) and
// ExpandNext (unit 11): walks the #G#-/#S#-prefixed coded reference,
// collects the two #name# #target# fields, resolves the CodedComma
// (208, Carriers const) tail into the third argument - either a
// nested coded reference routed through Extract2 (unit 15, the
// Stubs.Extract2 delegate seat, offset contract of the &v1d[i]
// idiom) or the raw sub-128 byte copy, defaulting to the literal
// NULL - emits the GlobalQS statement into Out byte-verbatim per
// H-001 via CompilerState.emit (String.format free; no format
// specifiers in this emission), and leaves the target field in the
// bp out parameter. Vendor quirks kept and noted: the #G# and #S#
// branches are byte-identical in logic (the same both-arms-same-code
// family as unit 65 global::Concat; both arms rendered verbatim, not
// refactored); the tail test in each branch checks #G#-vs-#S# in the
// SAME order for both branches; the sub-128 test reads the unsigned
// byte domain (v1d[i]<128, H-012); the arg1/arg2/arg3 char[32]
// carriers ride fresh CharArray scratch per call (H-007, the vendor
// locals folded); the caller's v1d/i advanced by this function are
// the offset-in carrier walk (the offset does NOT propagate back -
// the vendor int i is a by-value copy; the render mirrors this).
// Naming per H-017 (vendor name kept verbatim: no snake case).

fun ExpandQS(v1d: CharArray, iIn: Int, bp: CharArray) {
    val arg1 = CharArray(32)
    val arg2 = CharArray(32)
    val arg3 = CharArray(32)
    var i = iIn

    i++
    if (CstringOps.strncmpAt(v1d, i, "#G#", 3) == 0) {
        i = i + 3
        var j = 0
        while (v1d[i] != '#') {
            arg1[j] = v1d[i]
            j++
            i++
        }
        i++
        j = 0
        while (v1d[i] != '#') {
            arg2[j] = v1d[i]
            j++
            i++
        }
        i = i + 1
        expandQsTail(v1d, i, arg3)
        expandQsEmit(arg1, arg2, arg3)
        CstringOps.copyAt(bp, 0, arg2)
        return
    }

    if (CstringOps.strncmpAt(v1d, i, "#S#", 3) == 0) {
        i = i + 3
        var j = 0
        while (v1d[i] != '#') {
            arg1[j] = v1d[i]
            j++
            i++
        }
        i++
        j = 0
        while (v1d[i] != '#') {
            arg2[j] = v1d[i]
            j++
            i++
        }
        i = i + 1
        expandQsTail(v1d, i, arg3)
        expandQsEmit(arg1, arg2, arg3)
        CstringOps.copyAt(bp, 0, arg2)
        return
    }
}

// The CodedComma tail shared verbatim by both arms: Extract2 for a
// nested coded reference, the raw sub-128 copy otherwise, and the
// literal NULL when no CodedComma is present.
private fun expandQsTail(v1d: CharArray, iIn: Int, arg3: CharArray) {
    var i = iIn
    if (v1d[i] == CodedComma.toChar()) {
        i++
        if (CstringOps.strncmpAt(v1d, i, "#G#", 3) == 0 ||
            CstringOps.strncmpAt(v1d, i, "#S#", 3) == 0
        ) {
            CstringOps.copyAt(arg3, 0, Stubs.Extract2(v1d, i))
        } else {
            var j = 0
            while (v1d[i] < Char(128) && v1d[i] != NUL) {
                arg3[j] = v1d[i]
                j++
                i++
            }
        }
    } else {
        CstringOps.strcpyS(arg3, "NULL")
    }
}

// The GlobalQS emission shared verbatim by both arms.
private fun expandQsEmit(arg1: CharArray, arg2: CharArray, arg3: CharArray) {
    CompilerState.emit(
        "\tGlobalQS( (unsigned char *) " + CstringOps.cstring(arg1) +
            ", (unsigned char *) " + CstringOps.cstring(arg2) +
            ", (unsigned char *) " + CstringOps.cstring(arg3) + ", svPtr); \n"
    )
}
