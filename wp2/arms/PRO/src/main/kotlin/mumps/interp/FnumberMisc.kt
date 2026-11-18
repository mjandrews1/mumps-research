package mumps.interp

/**
 * PRO-arm translation of interp-fnumber.cpp::<misc> (batch 10 unit,
 * roster row 295, file rank 147). Translated independently from
 * the pristine vendor source (mumps-14-may-2026.tgz, sha256
 * 621deb2586f27e741ce6db1a2a86fbddf4e4da40c9c6bb363f84c814f53ffdbe);
 * the study-tool residual chunk (GPL banner, the Dec-13-2025 dateline,
 * the SQLITE define, the commented-out SYMDEBUG complement, the
 * include block incl. monetary.h/locale.h edges, the using-namespace
 * edge, the _INTERP_/CSTR/USTR macro trio, the intLong define, the
 * forward declarations of Interpret/ScanParse/Eval, the extern
 * __label_lookup/__text_function function-pointer pair, the fcn
 * prototype, and the <dollar>fnumber wrapper banner comment; loc 56,
 * comment_ratio 0.211, pointer_marks 2, control_marks 4, score 15.4 -
 * exact match to wp1/study-subset/units.csv row 295; every other
 * function in interp-fnumber.cpp exceeds min_loc and is claimed by
 * its own units.csv row (fcn in Fcn.kt, batch-6 DONE seat, not
 * redeclared).
 *
 * Header residue: no Kotlin declarations emitted.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h013 dead_store      : none in the chunk (pure declarations).
 *  - h014 out_of_scope_call: the declaration roster resolves each
 *    declaration to its arm status - Interpret is the DONE
 *    MumpsMain family seat (Interpret.kt, B6 unit 155), ScanParse
 *    is DONE (ScanParse.kt, batch-8 unit 234), Eval is DONE
 *    (Eval.kt, batch-9 unit 261), and fcn is DONE (Fcn.kt,
 *    batch-6 seat - the <dollar>fnumber() wrapper this file's
 *    banner comment exists to forward; callers bind unchanged).
 *  - h018 preprocessor_conditional: SQLITE is defined in the
 *    reference build (LIVE arm note, SymMisc row-259 precedent);
 *    SYMDEBUG is commented out (EXCLUDED arm note); _INTERP_ is
 *    a translation-unit selector with no Kotlin analog (the arm
 *    splits per C translation unit by file, the h004 convention).
 *
 * Vendor notes (translated faithfully, not \"fixed\"):
 *  - The __label_lookup/__text_function extern pair is the
 *    documented gate-exception double-underscore vendor names
 *    roster text only; no Kotlin identifier emitted (SymMisc
 *    row-259 disposition-ledger precedent; the file tail's
 *    one-line forwarding stubs belong to interp-sym.cpp, already
 *    journaled there).
 *  - monetary.h and locale.h are libc edges already carried at
 *    their use sites (the strfmon/locale seats of the fcn
 *    <dollar>fnumber body in Fcn.kt); CSTR/USTR are cast macros
 *    with no Kotlin analog (CharArray byte-domain notes, h012).
 *  - intLong is defined EMPTY on the reference build (64-bit
 *    intLong path, the comment above it: floatSize/intLong are
 *    configure-time knobs) - carried as this ledger note.
 *  - The Dec-13-2025 dateline and the sym.c banner comment are
 *    kept as roster text (the 2025 copyright line is consistent
 *    with the vendor tazball sha above).
 */
