package mumps.interp

/**
 * PRO-arm translation of interp-interp.cpp::<misc> (pilot B1, unit 19).
 * Translated independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), reference source
 * lines 1-132.
 *
 * Unit scope: per the Study Subset extraction
 * (wp1/study-subset/units.csv, tools/select_study_subset.py), the
 * <misc> unit is the file's residue after the extracted function
 * units — here everything outside Interpret (lines 133-2607, a
 * separate complex unit, batch 6): the file header, includes, the
 * file-scope #defines, the forward-declaration block, and the
 * declaration-only class InterpreterException.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h014 out_of_scope_call : the forward-declared functions are
 *    non-defining declarations owned by other files; they are
 *    recorded below as a manifest with owners (existing arm stubs
 *    noted), not re-declared.
 *  - h017 naming            : constants keep vendor names; the CSTR/
 *    USTR cast macros have no Kotlin analog (typed CharArrays) and
 *    the #define SQLITE / _INTERP_ preprocessor flags are resolved
 *    by the reference build configuration (SQLITE defined;
 *    _INTERP_ is an include-guard macro with no Kotlin analog).
 */

/** interp-interp.cpp:71 — gosub stack depth. */
const val GOSUBMAX = 50

/** interp-interp.cpp:72 — swap pointer array depth. */
const val SWAP = 30

/** interp-interp.cpp:73 — For loop stack depth. */
const val FORMAX = 30

/** interp-interp.cpp:74 — exec level stack depth. */
const val EXECLEVS = 40

/** interp-interp.cpp:75 — label table depth. */
const val LBLS = 80

/** interp-interp.cpp:80 — CodedOpen equivalent in the interp domain. */
const val OPEN_PAREN = 206

/**
 * interp-interp.cpp:107-114 — declaration-only vendor class
 * (constructor and what() are declared but never defined in the file).
 */
class InterpreterException {
    /** vendor: private: char * message; */
    private var message: String? = null

    /** vendor: char * what(); — declared, not defined in this file. */
    fun what(): String {
        TODO("h014 stub: declared-only vendor member (no definition in interp-interp.cpp)")
    }
}

/*
 * Forward-declaration manifest (vendor lines 81-127; declarations are
 * non-defining — the owning file is listed; arm stubs exist where
 * noted):
 *  - lpad, substr            -> interp-strmanip.cpp
 *  - sigint                  -> interp-sysfunc.cpp
 *  - LocalName, GlobalQuery, Qsub, Qlength, getstr1, unpad
 *                            -> interp-sym.cpp / interp-sysfunc.cpp
 *  - ErrorMessage            -> interp-parse.cpp
 *  - zfcn                    -> interp-bifs.cpp
 *  - fcn                     -> interp-fcn.cpp
 *  - xindex                  -> interp-strmanip.cpp (arm stub: InterpStubs.kt)
 *  - TimeOut, DirectRead, TimeOut_getstr1
 *                            -> interp-sysfunc.cpp
 *  - _SymFree                -> interp-sym.cpp (B1 unit 28 owns it)
 *  - sql                     -> interp-sql.cpp / rdbms-if.c
 *  - makeLock, cleanLocks    -> global array lock service
 *    (cleanLocks arm stub: InterpStubs.kt)
 *  - sym_                    -> interp-sym.cpp
 *  - patrn1 (static)         -> interp-interp.cpp (inside the
 *    Interpret unit's scope; the separately selected unit is
 *    interp-parse.cpp::patrn1, batch 1)
 */
