package doc

// Unit 112 (B4 tranche 2): libmpscpp.cpp::global::Centroid (batch
// 4, core stratum; LOC 30, score 22.6, rank 177) - the class-global
// Centroid member: walks the invoking reference's two-level
// subscript domain, accumulating the second-level values into the
// argument global B, then divides each accumulated node by its
// count in B(bb, "1"). Rendered as a GlobalRef extension (the
// batch-2 class-global member pattern; unit-44 GlobalMin family).
// The subscripted operator calls ride the H-014 keyed seats raised
// by unit 14 (TermCorrelate): globalOrder/globalOrder2/
// globalData/globalSetKeyed/globalPlusIntKeyed plus the invoked-
// global string conversion getGlobalString; B().Kill() rides the
// globalKill seat (the empty-invoke form of the vendor is the
// base-node kill, ledgered); the double reads/writes of the final
// division ride the one-subscript double conversion seats raised
// here (globalGetDoubleKeyed/globalSetDoubleKeyed, the double
// operator= family of the subscripted globals). Vendor quirks
// kept and noted: B(bb) = ... assignment in the not-Data branch
// stores the raw string value; the else branch concatenates
// B(bb) + (*this)(aa,bb) as STRINGS (the C++ string operator+,
// not numeric addition - observable when the values are
// non-numeric); B(bb, "1")++ is the integer increment operator
// (globalPlusIntKeyed with discarded result); the final walk
// reads x = (double) B(bb, "1") then B(bb) /= x - the DATA node
// is divided as a double; bb="" resets between the outer and
// final walks; the outer aa walk tests aa=="" for the loop exit.
// The (string) casts fold to the String domain of the seats.
// Naming per H-017 (Centroid kept verbatim).

fun GlobalRef.Centroid(B: GlobalRef) {
    var aa = ""
    var bb = ""

    Stubs.globalKill(B)

    while (true) {
        aa = Stubs.globalOrder(this, aa)

        if (aa == "") break

        bb = ""

        while (true) {
            bb = Stubs.globalOrder2(this, aa, bb)

            if (bb == "") break

            if (!Stubs.globalData(B, bb)) {
                Stubs.globalSetKeyed(B, Stubs.getGlobalString(this, aa, bb), bb)
                Stubs.globalSetKeyed(B, "1", bb, "1")
            } else {
                Stubs.globalSetKeyed(
                    B,
                    Stubs.getGlobalString(B, bb) + Stubs.getGlobalString(this, aa, bb),
                    bb
                )
                Stubs.globalPlusIntKeyed(B, 1, bb, "1")
            }
        }
    }

    bb = ""

    while (true) {
        bb = Stubs.globalOrder(B, bb)

        if (bb == "") break

        val x = Stubs.getGlobalDoubleKeyed(B, bb, "1")
        Stubs.setGlobalDoubleKeyed(B, Stubs.getGlobalDoubleKeyed(B, bb) / x, bb)
    }
}
