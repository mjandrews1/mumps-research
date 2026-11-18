package doc

// Unit 46: libmpscpp.cpp::Multiply (batch 2, core stratum) - the free
// five-argument wrapper validating three top-level global references and
// handing them to the class-global Multiply member (owner unit 53 of this
// batch; H-014 stub, delegated when u53 lands). Vendor quirks kept
// exactly and documented: the arg-2 and arg-3 checks test the WRONG
// index (arg2 at i, arg3 at i; vendor copy-paste, not fixed), the ep
// parameter is dead (kept for signature fidelity, H-013 divergence
// note), and printf emission is rendered with println (JVM console
// divergence; no observable difference for the arm build).

fun Multiply(svptr: Msv?, ep: CharArray?, arg1: CharArray, arg2: CharArray,
             arg3: CharArray): String {
    var i = 0
    while (arg1[i] != NUL) {
        if (arg1[i] == CHAR_1) break
        i++
    }

    if (arg1[i] == CHAR_1) {
        println()
        println("*** Arg 1 must be top level global")
        Stubs.sigint(100)
    }

    arg1[i] = NUL
    val a = GlobalRef(CstringOps.cstringAt(arg1, 1)) // bypass ^

    var j = 0
    while (arg2[j] != NUL) {
        if (arg2[j] == CHAR_1) break
        j++
    }

    if (arg2[i] == CHAR_1) {
        println()
        println("*** Arg 2 must be top level global")
        Stubs.sigint(100)
    }

    arg2[j] = NUL
    val b = GlobalRef(CstringOps.cstringAt(arg2, 1)) // bypass ^

    var k = 0
    while (arg3[k] != NUL) {
        if (arg3[k] == CHAR_1) break
        k++
    }

    if (arg3[i] == CHAR_1) {
        println()
        println("*** Arg 3 must be top level global")
        Stubs.sigint(100)
    }

    arg3[k] = NUL
    val c = GlobalRef(CstringOps.cstringAt(arg3, 1)) // bypass ^

    Stubs.globalRefMultiply(a, b, c)

    return ""
}
