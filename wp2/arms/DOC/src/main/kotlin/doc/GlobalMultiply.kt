package doc

// Unit 53: libmpscpp.cpp::global::Multiply (batch 2, complex stratum)
// - the three-reference class-global member: C = this x B (matrix
// product keyed by shared column/level structure). The vendor control
// flow is kept verbatim: the row/column/level Order walks, the B(k)
// Data() guard continue, and the first-time vs. accumulate branches.
// The class-global member seats called here are all out-of-scope:
// their C++ member functions are not roster units (units.csv has no
// global::Kill/Order/Data/invoke rows), so each is an H-014 stub
// naming its vendor owner; the double-valued global fetch and the
// numeric global store render through the arm strtod decimal-prefix
// contract plus the H-014 store stub (documented adaptation: the
// C++ operator support is not reproduced, only its observable
// flow). The vendor commented-out debug printf fossils (the C = 1
// line and the nine printf comment lines) are kept verbatim as
// comments. Naming per H-017: globalMultiply for the member.

fun GlobalRef.globalMultiply(B: GlobalRef, C: GlobalRef) {
    Stubs.globalKill(C)
    var i = ""

    while (true) {
        // each row
        i = Stubs.globalOrder(this, i)
        if (i == "") break

        var j = ""
        while (true) {
            // each column
            j = Stubs.globalOrder2(this, i, j)
            if (j == "") break

            val k = j

            if (!Stubs.globalData(B, k)) continue
            var m = ""

            while (true) {
                m = Stubs.globalOrderB(B, k, m)
                if (m == "") break

                if (!Stubs.globalData2(C, i, m)) {
                    val x = Stubs.getGlobalDouble(this, i, j) *
                            Stubs.getGlobalDouble(B, k, m)
                    Stubs.setGlobalDouble(C, x, i, m)
                } else {
                    val x = Stubs.getGlobalDouble(this, i, j) *
                            Stubs.getGlobalDouble(B, k, m)
                    val y = Stubs.getGlobalDouble(C, i, m) + x
                    Stubs.setGlobalDouble(C, y, i, m)
                }

//C(i,m)=1;
//printf("C(%s,%s) = ", i.c_str(), m.c_str());
//printf("C(%s,%s) + ", i.c_str(), m.c_str());
//printf("This(%s,%s) * ", i.c_str(), j.c_str());
//printf("B(%s,%s) -> ", k.c_str(), m.c_str());
//printf("C=%s this=%s B=%s\n", (char *)C(i, m) , (char *)(*this)(i, j) ,(char *) B(k, m));
//printf("C=%s this=%f \n", (char *)C(i, m) , ((double)(*this)(i, j) * (double)B(k, m)));
            }
        }
    }
}
