package daf

// Unit 99 (DAF B4 T1): libmpscpp.cpp::Min (17 LOC): the free (char*)
// wrapper of the \$min service entry. Identical walk to the unit-98
// Count wrapper (vendor copy-paste pair; one body difference: the
// global-class member invoked is Min). Same consult anchors:
// BASE-log rows 56+112 (hit-reuse, fresh-String return), row 26
// (hit-adapt, Locale.US %g), row 15 (hit-reuse, slice reads); the
// member it reaches is the rostered libmpscpp.cpp::global::Min (the
// DAF stub names the owner; the BASE row-103 XNEXT pattern for that
// member is noted but NOT consulted beyond the row anchor).
// Dafny: EXEMPT (global-access state machine; journal row).

fun Min(svptr: DafMsv?, ep: CharArray, arg1: CharArray): String {

    var i = 0
    while (arg1[i] != NUL_D) i++

    if (i > 0 && arg1[i - 1] == '\u0001' && arg1[i - 2] == '\u0001') arg1[i - 2] = NUL_D

    i = 0
    while (arg1[i] != NUL_D) {
        if (arg1[i] == '\u0001') break
        i++
    }

    val j = arg1[i]
    arg1[i] = NUL_D

    val a = MpsGlobalNode(DafCRuntime.cstringAt(arg1, 1)) // bypass the caret

    val x = if (j == NUL_D) {
        a("").min()
    } else {
        a(DafCRuntime.cstringAt(arg1, i + 1)).min()
    }

    return DafCRuntime.fmtG(x)
}
