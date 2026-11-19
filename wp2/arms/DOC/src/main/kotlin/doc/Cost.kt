package doc

// Unit 74 (B3 tranche 2): libmpscpp.cpp::Cost (batch 3, core stratum;
// LOC 15, score 12.3). The Smith-Waterman cost-matrix cell recursion
// for the alignment scope opened by unit 61 (sw, batch 2): with the
// penalties GAP/MISMATCH/MATCH and the matrix __a seated in
// SeqAlignExterns (one owner per the H-004/H-014 alignment seats).
// The memo cell test reads EMPTY_CELL (-32768, SeqAlignExterns const),
// the diagonal/gap recurrence selects the max of A/b/c with the vendor
// clamp A<=0 to 0 BEFORE the memo write, and the recursion indexes the
// bytes s[i]==t[j] over the Char carriers (H-012 byte-domain compare).
// Vendor quirks kept and noted: the local x is declared and never
// used (H-013 dead local, kept as a comment); the C idiom
// "return __a[i][j]=A" (assignment in return) renders as the stored
// value returned explicitly; the matrix remains allocator-owned by the
// init seat - a missing init is a vendor NULL dereference, here a
// raised IllegalStateException (the observed JVM analogue; Sw.kt
// precedent). Landing this unit converts the unit-61 Stubs.Cost seat
// to a delegate (H-014 Extract2 precedent). Naming per H-017.

fun cost(s: CharArray, t: CharArray, i: Int, j: Int): Int {
    val costMatrix = a
        ?: throw IllegalStateException("Cost: cost matrix not initialized (init not run)")

    if (costMatrix[i][j] != EMPTY_CELL) return costMatrix[i][j]

    if (i <= 0 || j <= 0) {
        costMatrix[i][j] = 0
        return 0
    }

    // vendor local: int A,b,c,x; x is dead (H-013, never read)
    var A = cost(s, t, i - 1, j - 1) + (if (s[i] == t[j]) MATCH else MISMATCH)
    val b = cost(s, t, i - 1, j) + GAP
    val c = cost(s, t, i, j - 1) + GAP

    if (b > A) A = b

    if (c > A) A = c

    if (A <= 0) A = 0

    costMatrix[i][j] = A
    return A
}
