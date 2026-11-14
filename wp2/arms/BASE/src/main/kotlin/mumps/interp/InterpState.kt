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

    // --- interpreter-side macro constants (C: include/mumpsc/defines.h) ---
    // NOTE macro shadowing (heuristic 12): CLOSE is 4 in defines.h (interpreter
    // scope, used here) and 'c' in compiler-macros.h (compiler scope).
    const val CLOSE = 4
}
