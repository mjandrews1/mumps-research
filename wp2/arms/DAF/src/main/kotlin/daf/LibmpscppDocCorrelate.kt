package daf

// Unit 287 (DAF B10 T3, core): libmpscpp.cpp::global::DocCorrelate
// (units.csv row 287: loc 41, the string-threshold member body of
// the DocCorrelate triple; the 4-line char*/mstring overload twins
// are the docCorrelateD relays of LibmpscppMisc5, the unit-154 seat
// release): the fcn selector (Cosine 1 / Sim1 2 / Dice 3 / Jaccard
// 4, else 1 kept verbatim), the tree of sibling Order(1) walks
// over the receiver children, the per-pair kernel switch, the
// threshold gate, and the symmetric store into the output global
// B(doc1,doc2) / B(doc2,doc1).
// Consults (2026-11-25T15:04:30Z, nominal): rows 54/68 hit-adapt
// (the vendor '*this' receiver and the output 'B' fold onto the
// ONE node seat the free-call relay passes - the unit-120/154
// quirk kept verbatim: the member call shape DocCorrelate(B, s,
// threshold) drops the C++ receiver, so the walks and the stores
// both bind the b carrier; DISCLOSED DIVERGENCE ledger of that
// raise), row 56 hit-reuse (string subscript arms over the String
// child-ref fold), row 105 family hit-reuse (the child node = the
// FCN_MARK1_D-packed ref of the walking precedent: cOrderD Order(1)
// sibling walks over fresh MpsGlobalNode(ref + MARK1 + doc) per
// the rows 54/68 immutable-node rule), row 26 hit-adapt (the
// double threshold compare kept verbatim; the store value rides
// the fmtG carrier of the mstring operator=(double) fold), row 92
// hit-reuse kernel call shapes: nodeCosineD (RAISED batch-3 cell,
// Cosine global/global, the stub-mediated honesty note - flagged
// for PI: no DAF row owns Cosine(global,global) to date), sim1Daf
// (unit-204 release), diceD (unit-175 release), jaccardD (unit-176
// kin release), row 103 hit-adapt (B().Kill() binds killDaf(b.ref)
// (DOC unit-37 kin named stub; no myname carrier exists on
// MpsGlobalNode, so the kill folds onto the packed b.ref subtree -
// DISCLOSED DIVERGENCE vs the vendor Kill() which clears the
// myname-rooted tree).
// VENDOR QUIRKS kept: (1) the selector default s=1 (unknown fcn
// silently Cosine); (2) doc2 starts at doc1 (the self-pair is
// compared against itself); (3) the empty-return Order walk ends
// the loop; (4) the store writes BOTH symmetric cells inside the
// threshold gate. Dafny: EXEMPT (global-access walk over stub and
// released seats; journal row).

// void global::DocCorrelate(global B, string fcn, double threshold)
// - the row-287 RELEASE of the docCorrelateFreeD named stub
// (LibmpscppMisc5.kt:405; the stub body deleted there with this
// release note).
fun docCorrelateFreeD(b: MpsGlobalNode, s: String, threshold: Double) {
    val svPtr = DafInterpState.svPtr

    // if (fcn=="Cosine") s=1; else if (fcn=="Sim1") s=2;
    // else if (fcn=="Dice") s=3; else if (fcn=="Jaccard") s=4;
    // else s=1;
    var sSel = 1 // int s
    if (s == "Cosine") sSel = 1
    else if (s == "Sim1") sSel = 2
    else if (s == "Dice") sSel = 3
    else if (s == "Jaccard") sSel = 4
    else sSel = 1

    // B().Kill();
    killDaf(b.ref.toCharArray(), svPtr) // disclosed fold (header)

    var doc1 = "" // string doc1,doc2
    var doc2 = ""
    var c = 0.0    // double c

    while (true) { // while (1)
        // doc1 = (string) (*this)(doc1).Order(1);
        doc1 = cOrderD(MpsGlobalNode(b.ref + FCN_MARK1_D + doc1), 1, svPtr)

        if (doc1 == "") break

        doc2 = doc1

        while (true) { // inner while (1)
            // doc2 = (string) (*this)(doc2).Order(1);
            doc2 = cOrderD(MpsGlobalNode(b.ref + FCN_MARK1_D + doc2), 1, svPtr)

            if (doc2 == "") break

            // the per-pair pair nodes (fresh nodes per walk,
            // rows 54/68 immutable-node rule)
            val n1 = MpsGlobalNode(b.ref + FCN_MARK1_D + doc1)
            val n2 = MpsGlobalNode(b.ref + FCN_MARK1_D + doc2)

            when (sSel) { // switch (s)
                1 -> c = nodeCosineD(n1, n2) // Cosine RAISED seat
                2 -> c = sim1Daf(n1, n2, svPtr)
                3 -> c = diceD(n1, n2)
                4 -> c = jaccardD(n1, n2)
            }

            if (c > threshold) { // if (c>threshold)
                // B(doc1,doc2)=c; B(doc2,doc1)=c;
                storeSymPairD(b.ref, doc1, doc2, c)
            }
        }
    }
}

// the symmetric-store helper: both cells write inside the vendor's
// single threshold gate; the value rides the fmtG carrier (the
// mstring operator=(double) %g fold; row 26).
private fun storeSymPairD(baseRef: String, doc1: String, doc2: String,
    c: Double) {
    val svPtr = DafInterpState.svPtr

    val key1 = baseRef + FCN_MARK1_D + doc1 + FCN_MARK1_D + doc2
    val key2 = baseRef + FCN_MARK1_D + doc2 + FCN_MARK1_D + doc1
    val s = DafCRuntime.fmtG(c)
    val buf = CharArray(STR_MAX_DAF)
    for (q in s.indices) buf[q] = s[q]

    MglobalDaf(STORE_D, key1.toCharArray(), buf, svPtr)
    MglobalDaf(STORE_D, key2.toCharArray(), buf, svPtr)
}
