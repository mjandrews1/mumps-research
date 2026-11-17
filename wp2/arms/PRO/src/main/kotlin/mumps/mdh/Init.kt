package mumps.mdh

/**
 * PRO-arm translation of libmpscpp.cpp::init (B4 unit 109, rank
 * 148). Translated independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), reference
 * source lines 8100-8120.
 *
 * Identity verified against the study tool (tools/select_study_
 * subset.py): libmpscpp.cpp::init, cpp, loc 17, comment_ratio
 * 0.056, pointer_marks 4, control_marks 4, score 15.9 - exact
 * match to wp1/study-subset/units.csv row 110.
 *
 * init(): allocate and clear the Smith-Waterman score matrix
 * __a (Size_s rows of Size_t short cells, all EMPTY_CELL) and
 * reset the S_RESULT scratch string - the setup step sw() (B2
 * unit 61) calls before each alignment.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h004 globals_ext       : __a/S_RESULT/Size_s/Size_t ride
 *    the AlignState singleton (fields raised by units 45/61;
 *    this unit owns their INITIALIZATION, not their
 *    declaration).
 *  - h013 dead_store         : the two malloc-NULL arms ("Out of
 *    memory allocating Smith-Waterman matrix" + raise(SIGSEGV))
 *    are unreachable on the JVM - Array allocation throws
 *    OutOfMemoryError instead of returning NULL; dropped with
 *    this note (Sw unit-61 precedent, same arms).
 *  - h014 out_of_scope_call  : the MdhStubs init stub (raised by
 *    unit 61) is RESOLVED by this unit and REMOVED (cleanLocks
 *    precedent, unit 56); Sw.kt's call site now binds here.
 *  - h015 grammar_collision  : pre-push scan clean.
 *  - h017 naming             : init keeps the vendor spelling -
 *    a top-level fun init() is legal Kotlin (the MdhStubs stub
 *    already carried the name); EMPTY_CELL (-32768,
 *    libmpscpp.cpp:8029) is a file-private duplicate of Sw.kt's
 *    pin (h015 file-private duplicate precedent).
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The vendor row allocation is malloc(Size_s * sizeof(int*))
 *    but the cell rows are malloc(Size_t * sizeof(int)) - the
 *    classic sizeof mismatch (rows typed short int* but sized
 *    as int); on the JVM the Kotlin ShortArray allocation makes
 *    the mismatch unobservable - the declared short domain is
 *    what the matrix actually uses (Cost/sw read short cells) -
 *    documented, not fixed.
 *  - The fill loop writes EMPTY_CELL through a walking short*
 *    (p); the Kotlin fill is the array-initializer form of the
 *    identical writes.
 *  - "S_RESULT=\"\";" resets the alignment result scratch
 *    (AlignState.sResult), kept last, return void.
 */
private const val EMPTY_CELL: Short = -32768 // vendor libmpscpp.cpp:8029 (cf. Sw.kt)

/** vendor libmpscpp.cpp:8100-8120 - void init(). */
fun init() {

    // vendor: __a = (short int **) malloc((Size_s) * sizeof(int *));
    // NULL check dropped (h013: JVM allocation throws instead).
    // The per-row malloc and the EMPTY_CELL fill loop collapse into
    // the ShortArray initializer (identical writes).
    AlignState.a = Array(AlignState.Size_s) { ShortArray(AlignState.Size_t) { EMPTY_CELL } }

    AlignState.sResult = ""
}
