package daf

// Unit 108 (DAF B4 T2, core): libmpscpp.cpp::init (17 LOC): the
// Smith-Waterman matrix initializer: allocates the Size_s x Size_t
// short matrix, fills it with EMPTY_CELL, and resets S_RESULT.
// Consults: BASE-log rows 6/22 hit-reuse (the file-scope externs of the
// libmpscpp TU - __a, Size_s, Size_t, S_RESULT - fold into a singleton
// object; the init() free function renders against that state, as
// with CompilerState/InterpState on the BASE arm), row 46 hit-adapt
// (the malloc-NULL branches print the vendor message and raise
// SIGSEGV; on the JVM the 2D allocation either succeeds or throws
// OOME natively, so both branches render as the documented-divergence
// comment block: no executable POSIX-signal stub, per the row 46
// discipline), row 56 family (the row-fill loop becomes the
// ShortArray per-row initializer).
// Vendor values pinned from libmpscpp.cpp: EMPTY_CELL -32768;
// Size_s/Size_t are assigned by the Length/measure owner units of the
// Smith-Waterman family (not roster units in this batch), so the
// singleton keeps them as var seats.
// Dafny: EXEMPT (state initializer; journal row).

const val EMPTY_CELL_D: Short = -32768 // libmpscpp.cpp #define EMPTY_CELL

// Singleton seat of the libmpscpp.cpp file-scope Smith-Waterman
// externs (BASE-log rows 6/22 fold).
object SmithWatermanD {
    var sizeS = 0   // int Size_s
    var sizeT = 0   // int Size_t
    var a: Array<ShortArray> = arrayOf() // short int **__a
    var sResult = "" // string S_RESULT
}

fun initSmithWaterman() {

    // if ((__a = malloc(Size_s * sizeof(int *))) == NULL) {
    //       cout << "Out of memory allocating Smith-Waterman matrix" << endl;
    //       raise(SIGSEGV);
    //       }
    // DOCUMENTED DIVERGENCE (BASE-log row 46 family): the malloc-NULL
    // guard and raise(SIGSEGV) have no JVM counterpart; the Kotlin
    // allocation below fails through the runtime OOME path natively.

    SmithWatermanD.a = Array(SmithWatermanD.sizeS) {
        ShortArray(SmithWatermanD.sizeT) { EMPTY_CELL_D } // *(p++)=EMPTY_CELL
    }

    SmithWatermanD.sResult = "" // S_RESULT=""
}
