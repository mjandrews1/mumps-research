package doc

// Unit 100 (B4 tranche 1): libmpscpp.cpp::Transpose (batch 4, simple
// stratum; LOC 15, score 8.5) - the std::string OVERLOAD, distinct from
// the batch-5 MSV intrinsic wrapper and from the char* / mstring
// overloads (overload multiplicity ledgered; units.csv disambiguates
// by loc 15). Copies global A (source) into global B transposed at one
// subscript level: rows become columns. H-007: the carriers are clean
// Strings (scan-only; no in-place buffer surgery). H-013: the vendor
// double store bb="" is a dead store (no read between them) - one is
// dropped with this comment, the live one kept. H-014: the class-member
// Order(1) calls and the subscripted read/assignment ride the existing
// seats (globalOrder/globalOrder2 raised by the unit-69 family;
// getGlobalString by unit 31; globalSetKeyed by unit 31) - all
// out-of-scope or below-roster owners. Vendor loop shape kept exactly.

fun Transpose(a: String, b: String) {
    val aRef = GlobalRef(a)
    val bRef = GlobalRef(b)

    var aa = ""
    var bb = ""

    while (true) {
        aa = Stubs.globalOrder(aRef, aa)

        if (aa == "") break

        bb = "" // vendor stores twice; the twin dead store is dropped (H-013)

        while (true) {
            bb = Stubs.globalOrder2(aRef, aa, bb)

            if (bb == "") break

            Stubs.globalSetKeyed(bRef,
                    Stubs.getGlobalString(aRef, aa, bb), bb, aa)
        }
    }
}
