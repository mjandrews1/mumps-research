package daf

// Unit 100 (DAF B4 T1): libmpscpp.cpp::Transpose (23 LOC): the free
// 4-arg wrapper of the \$transpose service entry: split both coded
// arguments into top-level global name/subscript pairs, then the
// node-invoke transpose(b) member (GlobalNodeStub named owner).
// VENDOR BUG PRESERVED (logged, not fixed): the second argument's
// top-level check reads arg2[i] with the FIRST argument's index i
// (arg2[j] was intended); the render keeps arg2[i] so the bug stays
// observable exactly as in C. printf banners kept byte-verbatim;
// sigint(100) stub named owner. Returns the literal empty string.
// Same consult anchors as units 98/99: BASE-log rows 56+112 (the
// (char*)"" return folds String), row 15 (slice reads).
// Dafny: EXEMPT (global-access state machine; journal row).

fun Transpose(svptr: DafMsv?, ep: CharArray, arg1: CharArray,
        arg2: CharArray): String {

    var i = 0
    while (arg1[i] != NUL_D) {
        if (arg1[i] == '\u0001') break
        i++
    }

    if (arg1[i] == NUL_D) arg1[i + 1] = NUL_D // array name only - no indices

    if (arg1[i] == '\u0001') {
        println("\n*** Arg 1 must be top level global\n")
        sigintDaf(100)
    }

    arg1[i] = NUL_D
    val a = MpsGlobalNode(DafCRuntime.cstringAt(arg1, 1)) // bypass the caret

    var j = 0
    while (arg2[j] != NUL_D) {
        if (arg2[j] == '\u0001') break
        j++
    }

    if (arg2[j] == NUL_D) arg2[j + 1] = NUL_D // array name only - no indices

    // VENDOR BUG kept: arg2[i], not arg2[j]
    if (arg2[i] == '\u0001') {
        println("\n*** Arg 2 must be top level global\n")
        sigintDaf(100)
    }

    arg2[j] = NUL_D
    val b = MpsGlobalNode(DafCRuntime.cstringAt(arg2, 1)) // bypass the caret

    a(DafCRuntime.cstringAt(arg1, i + 1))
        .transpose(b(DafCRuntime.cstringAt(arg2, j + 1)))

    return ""
}

// ---------------------------------------------------------------------
// DAF B5 T2 ROW-142 VERIFICATION ADDENDUM (logged with the B5 T2
// tranche section; see TransposeAb.kt for the B4-T1 mapping repair
// disclosure): this render (the 23-LOC MSV service wrapper at
// vendor :10316) is the corpus libmpscpp.cpp::Transpose ROW-142
// body (units.csv row 142: loc 23, ptr 0, score 14.1, rank 136,
// core, batch 5 - the row THIS arm reaches at B5 T2). Verified
// line-for-line against the row-142 dump at B5 T2 with the
// per-row study metrics (the same-id adjudication of the corpus
// pair rows 118/142; no new code, verification-only column).
// ---------------------------------------------------------------------
