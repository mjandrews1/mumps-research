package mumps.interp

/**
 * Interpreter state: Kotlin translation of the C++ interpreter's file-scope
 * globals (the ones NOT carried in struct MSV / svPtr). Interpreter-side
 * analog of mumps.compiler.CompilerState (heuristic 6). Grows per unit.
 */
object InterpState {

    // C: interp-native-btree.cpp: extern int DBSem (defined in mpstime.cpp)
    /** deadlock prevention flag (C: int DBSem = 0) */
    var DBSem: Int = 0

    // --- build-configuration flags (C: #ifdef in interp-arith.cpp) ---
    // Reference build (/usr/bin/mumps, glibc/Linux), VERIFIED against the
    // reference binary 2026-11-14 ($zacos(1) -> "0", no appended decimal
    // point): GCVT is NOT defined, so gcvt_wrapper(x,a) = sprintf(a, "%.8g", x).
    // No BIGFLOAT (long double), no MULTI_PRECISION (MPFR).
    const val GCVT = false
    const val BIGFLOAT = false
    const val MULTI_PRECISION = false

    // --- interpreter-side macro constants (C: include/mumpsc/defines.h) ---
    // NOTE macro shadowing (heuristic 12): CLOSE is 4 in defines.h (interpreter
    // scope, used here) and 'c' in compiler-macros.h (compiler scope).
    const val CLOSE = 4
}
