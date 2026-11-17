package mumps.interp

/**
 * PRO-arm translation of interp-bifs.cpp::<misc> (B4 unit 107,
 * rank 159). Translated independently from the pristine vendor
 * source (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe); the
 * study-tool residual chunk (file header/banner, includes, the
 * forward declarations of sym_/wstem/sigint, the SQLITE define,
 * the CLOSE/COMMA/NO/YES defines, the gcvt_wrapper macro block,
 * and the small Mget function; loc 60, comment_ratio 0.221,
 * pointer_marks 4, control_marks 2, score 18.4 - exact match to
 * wp1/study-subset/units.csv row 108; every other function in
 * interp-bifs.cpp exceeds min_loc and is claimed by its own
 * units.csv row, e.g. zfcn - not a corpus unit, InterpStubs stub).
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h002 simple_output_macro : no output macros in the chunk;
 *    noted for the roster (none emitted).
 *  - h013 dead_store          : none in the chunk (Mget writes
 *    every local it declares).
 *  - h014 out_of_scope_call   : the declaration roster resolves
 *    each forward declaration to its arm status - sym_ is the
 *    InterpStubs owner-named stub (owner symbol-table machinery,
 *    MdhStubs carrier side), wstem is an interp-stem.cpp::wstem
 *    batch-10 corpus unit (no stub raised for a future owner,
 *    GlobalClassH precedent), sigint is the existing InterpStubs
 *    stub (owner interp-sysfunc.cpp, batch 7).
 *  - h015 grammar_collision   : the CLOSE/COMMA pins are numeric
 *    (207/208, defines.h-shaped; cf. Fcn.kt's CODED_* copies) -
 *    no quoting hazards; pre-push scan clean.
 *  - h017 naming              : Mget keeps the vendor spelling;
 *    the file-private defines keep vendor names uppercased.
 *  - h018 preprocessor_conditional: the reference build (cf. unit
 *    31 journal: GCVT defined) resolves gcvt_wrapper to plain
 *    gcvt - i.e. the REAL gcvtWrapper in NumSetup.kt (B1 unit
 *    29), reused, not a new macro helper; SQLITE is defined in
 *    the reference build (live-arm note only - the define guards
 *    no code in this chunk, it arms zfcn's SQL paths, whose owner
 *    stub documents it).
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The vendor comment "TODO: Extern globals prevent compiling
 *    this into a dynamic lib." above the commented-out
 *    __text_function declaration is kept as roster text (the
 *    declaration itself stays commented out, as in the vendor).
 *  - CLOSE 207 / COMMA 208 differ from interp-parse.cpp's
 *    OPEN/COMMA/CLOSE_PAREN trio (206/207/209): here CLOSE is
 *    207 and COMMA is 208 (the fcn/interp-fcn convention); the
 *    defines feed zfcn (owner stub) - kept verbatim, not
 *    harmonized.
 *  - Mget's 128-byte tmp is a stack buffer in the vendor - a
 *    fresh local here per call (h008 fast-path note: Mget has no
 *    static scratch of its own).
 *  - sym_ may return NULL in the vendor (char* return); the
 *    Kotlin carrier returns the nullable buffer unchanged.
 */
private const val CLOSE = 207 // vendor interp-bifs.cpp:74 (used by zfcn, owner stub)
private const val COMMA = 208 // vendor interp-bifs.cpp:75 (used by zfcn, owner stub)
private const val NO = 0 // vendor interp-bifs.cpp:76
private const val YES = 1 // vendor interp-bifs.cpp:77

// vendor interp-bifs.cpp:58-60 - forward declarations resolved per
// the h014 roster above: sym_ (InterpStubs stub), wstem (batch-10
// owner, no stub), sigint (InterpStubs stub).

/** vendor interp-bifs.cpp:116-119 - unsigned char * Mget(unsigned char * s, struct MSV *). */
fun Mget(s: CharArray, svPtr: Msv): CharArray? {
    val tmp = CharArray(128)
    return sym_(1, s, 0, tmp, 0, svPtr)
}
