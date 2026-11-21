package daf

// Unit 130 (DAF B5 T1, simple): libmpscpp.cpp::Avg (17 LOC): the
// free (char*) wrapper of the \$avg service entry: parse the coded
// argument (byte 1 = subscript separator, trailing 1,1-pair quirk
// strip), split name/subscript, then the global-class node invoke +
// avg() member; sprintf(tmp,"%g",x) over the static char tmp[32]
// return becomes a fresh String.
// Consults: BASE-log rows 56/112 hit-reuse (static-return-buffer ->
// value-return: the fresh String return via DafCRuntime.fmtG),
// row 26 hit-reuse (the %g format string kept verbatim, Locale.US),
// row 80 hit-adapt (the (char)1 coded arg split; the trailing
// 1,1 pair strip and the separator walk, per the Count T1 fold),
// row 54 hit-reuse (the global node model: MpsGlobalNode ctor takes
// the caret-bypassed slice; the a(\"\")/a(subscript) invokes are the
// node no-arg/subscript seats), row 41 family hit-adapt (the member
// avg() resolves through the C++ global class; the named-owner stub
// rides MpsGlobalNode per the node-model fold).
// QUIRK preserved (ledgered, no repair; identical to the unit-98
// Count quirk): the i-2 NUL write checks arg1[i-1]==1 &&
// arg1[i-2]==1 with NO preceding separator guarantee (vendor UB on
// 1-byte inputs; the Kotlin bounds-throw is the documented crash
// shape).
// Dafny: EXEMPT (global-access service wrapper over stub seats;
// journal row).

fun AvgD(arg1: CharArray): String {

    var i = 0
    while (arg1[i] != NUL_D) i++ // i = strlen(arg1)

    if (i > 0 && arg1[i - 1] == '\u0001' && arg1[i - 2] == '\u0001') arg1[i - 2] = NUL_D

    i = 0
    while (arg1[i] != NUL_D) {
        if (arg1[i] == '\u0001') break
        i++
    }

    val j = arg1[i]
    arg1[i] = NUL_D

    val a = MpsGlobalNode(DafCRuntime.cstringAt(arg1, 1)) // bypass ^

    val x = if (j == NUL_D) {
        avgOfNode(a, "")       // x = a("").Avg()
    } else {
        avgOfNode(a, DafCRuntime.cstringAt(arg1, i + 1)) // a(&arg1[i+1]).Avg()
    }

    return DafCRuntime.fmtG(x) // sprintf(tmp, "%g", x); return tmp
}

// avg() member seat (row 54 node-model fold; named owner):
// owner: libmpscpp.cpp global-class member Avg - NOT a corpus unit;
// permanent stub unless a later batch renders the family.
private fun avgOfNode(node: MpsGlobalNode, sub: String): Double =
    node.avg(sub)
