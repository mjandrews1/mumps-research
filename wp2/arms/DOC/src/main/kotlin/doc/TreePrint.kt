package doc

// Unit 60: libmpscpp.cpp::global::TreePrint (batch 2, complex
// stratum; row 35: loc 116, ptr 52, ctrl 32, score 142.6, rank 282).
// The indented tree dump of a global down to ten subscript levels:
// each level is an Order walk printing node names, an = when the
// node carries a scalar datum (Data value 1 or 11), and recursive
// descent when the Data value indicates children (i >= 2). The
// vendor's literal ten-deep while nesting is kept verbatim (no
// helper-function refactoring: the flat duplication is the unit's
// observable structure). cout/endl map to System.out per H-001.
// The member seats (Order/Data/invoke) are out-of-scope H-014 stubs
// (Stubs.globalOrder/globalOrder2/globalOrderN, globalDataN,
// getGlobalString): the C++ class-global members are not roster
// units. Vendor quirks kept exactly: at level 8 the Order walk's
// subscript list OMITS a1 ((*this)(a0,a2,a3,a4,a5,a6,a7,a8) - the
// a1 seat is missing in the vendor source), while the Data call at
// the same level includes it; levels 8 and 9 print the node name
// and endl BEFORE probing Data (the other levels print the name,
// then branch on Data afterwards); levels 6 and 7 carry an extra
// Data<2 continue guard after the print ladder. The indent string
// is built by repeated char concat per the vendor string idiom.
// Naming per H-017: globalTreePrint for the member.

fun GlobalRef.globalTreePrint(iIn: Int, c: Char) {
    var i = iIn
    var indent = ""
    val eq = "="

    if (i != 0) {
        for (j in 0 until i) indent = indent + c
    }

    var a0 = ""

    while (true) {
        a0 = Stubs.globalOrder(this, a0)
        if (a0 == "") break

        System.out.print(a0)
        i = Stubs.globalDataN(this, a0)

        if (i == 1 || i == 11) System.out.println(eq + Stubs.getGlobalString(this, a0))
        else System.out.println()

        if (i < 2) continue

        var a1 = ""

        while (true) {
            a1 = Stubs.globalOrder2(this, a0, a1)
            if (a1 == "") break

            for (ii in 0 until 1) System.out.print(indent)

            System.out.print(a1)
            i = Stubs.globalDataN(this, a0, a1)

            if (i == 1 || i == 11) System.out.println(eq + Stubs.getGlobalString(this, a0, a1))
            else System.out.println()

            if (i < 2) continue

            var a2 = ""

            while (true) {
                a2 = Stubs.globalOrderN(this, a0, a1, a2)
                if (a2 == "") break

                for (ii in 0 until 2) System.out.print(indent)

                System.out.print(a2)
                i = Stubs.globalDataN(this, a0, a1, a2)

                if (i == 1 || i == 11) System.out.println(eq + Stubs.getGlobalString(this, a0, a1, a2))
                else System.out.println()

                if (i < 2) continue

                var a3 = ""

                while (true) {
                    a3 = Stubs.globalOrderN(this, a0, a1, a2, a3)
                    if (a3 == "") break

                    for (ii in 0 until 3) System.out.print(indent)

                    System.out.print(a3)
                    i = Stubs.globalDataN(this, a0, a1, a2, a3)

                    if (i == 1 || i == 11) System.out.println(eq + Stubs.getGlobalString(this, a0, a1, a2, a3))
                    else System.out.println()

                    if (i < 2) continue

                    var a4 = ""

                    while (true) {
                        a4 = Stubs.globalOrderN(this, a0, a1, a2, a3, a4)
                        if (a4 == "") break

                        for (ii in 0 until 4) System.out.print(indent)

                        System.out.print(a4)
                        i = Stubs.globalDataN(this, a0, a1, a2, a3, a4)

                        if (i == 1 || i == 11) System.out.println(eq + Stubs.getGlobalString(this, a0, a1, a2, a3, a4))
                        else System.out.println()

                        if (i < 2) continue

                        var a5 = ""

                        while (true) {
                            a5 = Stubs.globalOrderN(this, a0, a1, a2, a3, a4, a5)
                            if (a5 == "") break

                            for (ii in 0 until 5) System.out.print(indent)

                            System.out.print(a5)
                            i = Stubs.globalDataN(this, a0, a1, a2, a3, a4, a5)

                            if (i == 1 || i == 11) System.out.println(eq + Stubs.getGlobalString(this, a0, a1, a2, a3, a4, a5))
                            else System.out.println()

                            if (i < 2) continue

                            if (Stubs.globalDataN(this, a0, a1, a2, a3, a4, a5) < 2) continue

                            var a6 = ""

                            while (true) {
                                a6 = Stubs.globalOrderN(this, a0, a1, a2, a3, a4, a5, a6)
                                if (a6 == "") break

                                for (ii in 0 until 6) System.out.print(indent)

                                System.out.print(a6)
                                i = Stubs.globalDataN(this, a0, a1, a2, a3, a4, a5, a6)

                                if (i == 1 || i == 11) System.out.println(eq + Stubs.getGlobalString(this, a0, a1, a2, a3, a4, a5, a6))
                                else System.out.println()

                                if (i < 2) continue

                                if (Stubs.globalDataN(this, a0, a1, a2, a3, a4, a5, a6) < 2) continue

                                var a7 = ""

                                while (true) {
                                    a7 = Stubs.globalOrderN(this, a0, a1, a2, a3, a4, a5, a6, a7)
                                    if (a7 == "") break

                                    for (ii in 0 until 7) System.out.print(indent)

                                    System.out.print(a7)
                                    i = Stubs.globalDataN(this, a0, a1, a2, a3, a4, a5, a6, a7)

                                    if (i == 1 || i == 11) System.out.println(eq + Stubs.getGlobalString(this, a0, a1, a2, a3, a4, a5, a6, a7))
                                    else System.out.println()

                                    if (i < 2) continue

                                    var a8 = ""

                                    while (true) {
                                        // VENDOR QUIRK: this Order call's
                                        // subscript list omits a1 (the
                                        // vendor reads
                                        // (*this)(a0,a2,a3,a4,a5,a6,a7,a8)
                                        // - a1 missing, kept verbatim).
                                        a8 = Stubs.globalOrderN(this, a0, a2, a3, a4, a5, a6, a7, a8)
                                        if (a8 == "") break

                                        for (ii in 0 until 8) System.out.print(indent)

                                        // VENDOR QUIRK: level 8 prints the
                                        // name and endl BEFORE probing
                                        // Data (kept; the Data include-
                                        // list does carry a1 here).
                                        System.out.println(a8)
                                        i = Stubs.globalDataN(this, a0, a1, a2, a3, a4, a5, a6, a7, a8)

                                        if (i == 1 || i == 11) System.out.println(eq + Stubs.getGlobalString(this, a0, a1, a2, a3, a4, a5, a6, a7, a8))
                                        else System.out.println()

                                        if (i < 2) continue

                                        var a9 = ""

                                        while (true) {
                                            a9 = Stubs.globalOrderN(this, a0, a1, a2, a3, a4, a5, a6, a7, a8, a9)
                                            if (a9 == "") break

                                            for (ii in 0 until 9) System.out.print(indent)

                                            // level 9 likewise prints the
                                            // name before probing Data.
                                            System.out.println(a9)
                                            i = Stubs.globalDataN(this, a0, a1, a2, a3, a4, a5, a6, a7, a8, a9)

                                            if (i == 1 || i == 11) System.out.println(eq + Stubs.getGlobalString(this, a0, a1, a2, a3, a4, a5, a6, a7, a8, a9))
                                            else System.out.println()
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
