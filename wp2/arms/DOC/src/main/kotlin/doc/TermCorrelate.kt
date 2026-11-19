package doc

// Unit 14 (B1 tranche 3): libmpscpp.cpp::global::TermCorrelate
// (batch 1, core stratum; units.csv row 19: loc 29, score 20.1,
// rank 168). The term-correlation writer: walks this global's doc
// level, then its term level under each doc, then the co-occurring
// term pair level, and symmetrizes the pair counts into global B. All
// class-global member seats are out-of-scope H-014 stubs on Stubs
// (the C++ class-global operator()/Order/Data members are not roster
// units): the Order walks use globalOrderN (vararg keys, last key
// walks - the TreePrint precedent), the Data probe uses globalDataN,
// the keyed read-modify-write uses the new globalPlusIntKeyed and
// globalSetKeyed seats (subscripted global operator family). Vendor
// quirks kept: the int xi declaration sits inside the term2 loop,
// mid-function (kept as a local with that note); the second
// assignment B(term2,term1)=B(term1,term2) reads the ALREADY
// INCREMENTED B(term1,term2), so the incremented value is captured
// once and written to both seats in order. Loop shapes kept verbatim
// (no helper refactoring). Naming per H-017: globalTermCorrelate.

fun GlobalRef.globalTermCorrelate(B: GlobalRef) {
    Stubs.globalKill(B)

    var doc = ""

    while (true) {
        doc = Stubs.globalOrderN(this, doc)

        if (doc == "") break

        var term1 = ""

        while (true) {
            term1 = Stubs.globalOrderN(this, doc, term1)

            if (term1 == "") break

            var term2 = term1

            while (true) {
                term2 = Stubs.globalOrderN(this, doc, term2)

                if (term2 == "") break

                // vendor quirk: int xi is declared here, inside the
                // innermost loop, after the break test
                val xi = Stubs.globalDataN(B, term1, term2)

                if (xi != 0) {
                    // B(term1,term2)=B(term1,term2)+1
                    val incremented = Stubs.globalPlusIntKeyed(B, 1, term1, term2)
                    Stubs.globalSetKeyed(B, incremented, term1, term2)
                    // B(term2,term1)=B(term1,term2) - reads the
                    // already-incremented value
                    Stubs.globalSetKeyed(B, incremented, term2, term1)
                } else {
                    Stubs.globalSetKeyed(B, "1", term1, term2)
                    Stubs.globalSetKeyed(B, "1", term2, term1)
                }
            }
        }
    }
}
