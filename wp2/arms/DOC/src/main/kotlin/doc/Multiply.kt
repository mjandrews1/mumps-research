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

// Unit 138 (B5 tranche 2): libmpscpp.cpp::Multiply (vendor ~2211-2250,
// physical 26 loc) - the free three-std::string overload. Unlike the
// batch-2 five-argument MSV wrapper (unit 46, above), this overload
// constructs the three class-global references directly from
// std::string and then performs the full matrix-product walk inline,
// which duplicates the member body of unit 53 (kept verbatim per the
// copy-paste policy; ledger note in the journal). The class-global
// seats (Kill/Order/Data plus the double fetch/store) are the same
// H-014 out-of-scope stubs as unit 53, rendered through the arm
// strtod decimal-prefix contract. No commented-out printf fossils in
// this vendor body; nothing dropped. Naming per H-017.

fun Multiply(a: String, b: String, c: String) {
    val A = GlobalRef(a)
    val B = GlobalRef(b)
    val C = GlobalRef(c)

    Stubs.globalKill(C)
    var i = ""

    while (true) {

        i = Stubs.globalOrder(A, i) // each row

        if (i == "") break

        var j = ""

        while (true) {
            j = Stubs.globalOrder2(A, i, j) // each column

            if (j == "") break

            val k = j

            if (!Stubs.globalData(B, k)) continue

            var m = ""

            while (true) {
                m = Stubs.globalOrderB(B, k, m)

                if (m == "") break

                if (!Stubs.globalData2(C, i, m)) {
                    val x = Stubs.getGlobalDouble(A, i, j) *
                            Stubs.getGlobalDouble(B, k, m)
                    Stubs.setGlobalDouble(C, x, i, m)
                } else {
                    val x = Stubs.getGlobalDouble(C, i, m) +
                            Stubs.getGlobalDouble(A, i, j) *
                            Stubs.getGlobalDouble(B, k, m)
                    Stubs.setGlobalDouble(C, x, i, m)
                }
            }
        }
    }
}


// Unit 201 (B7 tranche 2): libmpscpp.cpp::Multiply (batch 7, core
// stratum; LOC 26, score 20.8, rank 169; vendor
// libmpscpp.cpp:2252, physically verified this session; nonblank 26
// matches the roster row) - the free CHAR-STAR overload, third of
// the three same-named free functions (overload family ledger: the
// unit-46 five-arg MSV wrapper and the unit-138 std::string
// overload are the earlier rows; the char - star triple signature
// resolves apart in Kotlin by the CharArray carriers). The body
// duplicates the unit-138 string-overload walk verbatim (the
// vendor's own copy-paste, kept per the copy-paste policy and the
// batch-5 unit-138 journal note: the global reference construction
// is the only delta - global A((char *) a) renders as the GlobalRef
// over this function's CstringOps.cstring(a) domain seat).
// Consult H-014 hit-reuse: every class-global member call
// (Kill/Order x3/Data x2/double fetch/store) rides the SAME
// existing member seats that units 53/138 bound; Stubs.sigint is
// not reached from this overload. Naming per H-017.

fun Multiply(a: CharArray, b: CharArray, c: CharArray) {
    val A = GlobalRef(CstringOps.cstring(a))
    val B = GlobalRef(CstringOps.cstring(b))
    val C = GlobalRef(CstringOps.cstring(c))

    Stubs.globalKill(C)
    var i = ""

    while (true) {

        i = Stubs.globalOrder(A, i) // each row

        if (i == "") break

        var j = ""

        while (true) {
            j = Stubs.globalOrder2(A, i, j) // each column

            if (j == "") break

            val k = j

            if (!Stubs.globalData(B, k)) continue

            var m = ""

            while (true) {
                m = Stubs.globalOrderB(B, k, m)

                if (m == "") break

                if (!Stubs.globalData2(C, i, m)) {
                    val x = Stubs.getGlobalDouble(A, i, j) *
                            Stubs.getGlobalDouble(B, k, m)
                    Stubs.setGlobalDouble(C, x, i, m)
                } else {
                    val x = Stubs.getGlobalDouble(C, i, m) +
                            Stubs.getGlobalDouble(A, i, j) *
                            Stubs.getGlobalDouble(B, k, m)
                    Stubs.setGlobalDouble(C, x, i, m)
                }
            }
        }
    }
}
