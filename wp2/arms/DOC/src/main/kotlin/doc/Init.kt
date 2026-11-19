package doc

// Unit 108 (B4 tranche 2): libmpscpp.cpp::init (batch 4, core
// stratum; LOC 17, score 15.9, rank 148) - the Smith-Waterman cost
// matrix initializer of the sequence-alignment scope opened by
// unit 61 (sw) with its seats in SeqAlignExterns (EMPTY_CELL at
// libmpscpp.cpp:8026, the matrix a at :8031, S_RESULT at :8032;
// Size_s/Size_t the sw-assigned dimensions). Landing this unit
// converts the Stubs.init seat to a delegate (H-014 Extract2
// delegate precedent). cout maps to System.out per H-001; the
// malloc-NULL raise(SIGSEGV) of the vendor renders per the Sw.kt
// precedent: the JVM allocation failure analogue is the caught
// OutOfMemoryError re-thrown as the banner+Error carrier (the C
// abort is the observed behaviour, the Error is the JVM shape).
// Allocation note: the vendor allocates the row-pointer array then
// each row, filling every cell EMPTY_CELL via the short int*
// walk - the render allocates Array<IntArray> with the fill in
// the constructor domain (the pointer walk is the allocation
// itself here; the H-013 note: i/j/p locals of the vendor fold
// into the loops). S_RESULT="" is the shared SeqAlignExterns
// seat. The short int domain rides the Int cells with EMPTY_CELL
// -32768 (SeqAlignExterns const, the unit-45/61/74 precedent).
// Naming per H-017 (top-level fun init is legal Kotlin).

fun init() {
    val matrix = try {
        Array(Size_s) { IntArray(Size_t) }
    } catch (e: OutOfMemoryError) {
        // cout << "Out of memory allocating Smith-Waterman matrix"
        //      << endl; raise(SIGSEGV);
        System.out.println("Out of memory allocating Smith-Waterman matrix")
        throw Error("init: allocation abort (vendor raise SIGSEGV)")
    }

    // for (p = __a[i], j = 0; j < Size_t; j++) *(p++) = EMPTY_CELL;
    for (i in 0 until Size_s) {
        for (j in 0 until Size_t) {
            matrix[i][j] = EMPTY_CELL
        }
    }

    // libmpscpp.cpp:8031 short ** __a extern seat
    a = matrix
    S_RESULT = ""
}
