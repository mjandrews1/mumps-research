package daf

// Unit 98 (DAF B4 T1): libmpscpp.cpp::Count (17 LOC): the free
// (char*) wrapper of the \$count service entry: parse the coded
// argument (byte 1 = the coded subscript separator; the trailing
// 1,1 pair quirk strips a trailing empty second subscript), split
// name/subscript, then the global-class node invoke + count() member
// (GlobalNodeStub named owners). sprintf(tmp,"%g",x) over the static
// char tmp[32] return becomes a fresh String (BASE-log rows 56/112
// static-return-buffer -> value-return family, adapted per row 26:
// the format string is kept verbatim with Locale.US).
// QUIRK preserved: the i-2 NUL write checks arg1[i-1]==1 && arg1[i-2]==1
// with NO preceding separator guarantee (vendor UB on 1-byte inputs;
// Kotlin bounds-throw is the documented crash-shape).
//
// DAF consults: BASE-log rows 56+112 (hit-reuse, fresh-String return),
// row 26 (hit-adapt, Locale.US %g), row 15 (hit-reuse, slice reads).
// Dafny: EXEMPT (global-access state machine; the exemption row lives
// in the DAF journal).

fun Count(svptr: DafMsv?, ep: CharArray, arg1: CharArray): String {

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
        a("").count() // vendor: a("").Count() on the empty subscript
    } else {
        a(DafCRuntime.cstringAt(arg1, i + 1)).count()
    }

    return DafCRuntime.fmtG(x)
}
