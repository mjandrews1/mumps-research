package doc

// UNIT 308 RENDER - batch 10 DOC tranche T3 (complex).
// libmpscpp.cpp::global::DocCorrelate - roster row 288, loc 41.
// The document correlation driver: the nested Order walks over
// the THIS global, the four scorer arms and the symmetric
// threshold write into B.
//
// ADJUDICATIONS:
// - The C++ member rides the GlobalRef extension seat with the
//   docCorrelateStr name (the Misc5-landed char*/mstring DocCorrelate
//   delegate twins already own the DocCorrelate name; the H-017
//   rename avoids the redeclaration clash, the string-twin
//   carries the working body per the unit-154 front-run ledger).
// - The string fcn ladder (Cosine/Sim1/Dice/Jaccard with the
//   default 1) and the double c residue render verbatim; the C
//   c-uninitialized-else-arm-invariant is banner (H-013: the
//   vendor default arm sets s=1 so c is always written before
//   the threshold test).
// - (*this)(doc1).Order(1) rides Stubs.globalOrderN(this, key);
//   B().Kill() rides Stubs.globalKill(B); B(doc1,doc2)=c rides
//   Stubs.setGlobalDoubleKeyed(B, c, doc1, doc2) (the vararg-key
//   invoke shapes); the scorer arguments (*this)(doc1) ride the
//   NEW Stubs.globalInvokeKeyed(this, doc1) H-014 seat (the
//   subscripted operator() owner family; raised throwing in
//   Stubs next to the double-keyed seats).
// - The while(1)/Order driver is the vendor scan shape: empty
//   string is the guard on both loops (verbatim).
//
// Consult row logged 2026-11-22T06:30:00Z (H-014 hit-reuse, the
// keyed global seat family; the Stubs.globalDocCorrelate seat
// flips to delegate here).

fun GlobalRef.docCorrelateStr(B: GlobalRef, fcn: String, threshold: Double) {
    var s = 1
    if (fcn == "Cosine") s = 1
    else if (fcn == "Sim1") s = 2
    else if (fcn == "Dice") s = 3
    else if (fcn == "Jaccard") s = 4
    else s = 1 // vendor default arm

    Stubs.globalKill(B)
    var doc1 = ""

    while (true) {
        doc1 = Stubs.globalOrderN(this, doc1)

        if (doc1 == "") break

        var doc2 = doc1

        while (true) {
            doc2 = Stubs.globalOrderN(this, doc2)

            if (doc2 == "") break

            var c = 0.0
            when (s) {
                1 -> c = Cosine(
                    Stubs.globalInvokeKeyed(this, doc1),
                    Stubs.globalInvokeKeyed(this, doc2)
                )
                2 -> c = Sim1(
                    Stubs.globalInvokeKeyed(this, doc1),
                    Stubs.globalInvokeKeyed(this, doc2)
                )
                3 -> c = Dice(
                    Stubs.globalInvokeKeyed(this, doc1),
                    Stubs.globalInvokeKeyed(this, doc2)
                )
                4 -> c = Jaccard(
                    Stubs.globalInvokeKeyed(this, doc1),
                    Stubs.globalInvokeKeyed(this, doc2)
                )
            }

            if (c > threshold) {
                Stubs.setGlobalDoubleKeyed(B, c, doc1, doc2)
                Stubs.setGlobalDoubleKeyed(B, c, doc2, doc1)
            }
        }
    }
}
