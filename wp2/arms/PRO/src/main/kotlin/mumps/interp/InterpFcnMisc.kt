package mumps.interp

/**
 * PRO-arm translation of interp-fcn.cpp::<misc> (B7 tranche 2,
 * unit 202, rank 143). Translated independently from the
 * pristine vendor source (mumps-14-may-2026.tgz, sha256
 * 621deb25...ffdbe); the study-tool residual chunk (file
 * header/banner, the Dec 13 2025 dateline, the SQLITE define,
 * the SYMDEBUG commented define, the _INTERP_/CSTR/USTR
 * macros, the include block, the forward declarations of
 * Interpret/ScanParse/Eval, the __label_lookup/__text_function
 * extern pair, the extern "C" _getpid/time pair, and the
 * SYMSTORE/SYMRETRIEVE defines; loc 60, comment_ratio 0.2,
 * pointer_marks 2, control_marks 3, score 14.8 - exact match
 * to wp1/study-subset/units.csv row 202; every other function
 * in interp-fcn.cpp exceeds min_loc and is claimed by its own
 * units.csv row - fcn is B4 unit 94 (Fcn.kt), not redeclared.
 *
 * Header residue: no Kotlin declarations emitted.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h002 simple_output_macro : no output macros in the chunk;
 *    noted for the roster (none emitted).
 *  - h013 dead_store          : none in the chunk.
 *  - h014 out_of_scope_call   : the declaration roster resolves
 *    each declaration to its arm status - Interpret is the
 *    DONE MumpsMain family seat (Interpret.kt, B6 unit 155),
 *    ScanParse is an interp-parse.cpp::ScanParse batch-10
 *    corpus unit (InterpStubs stub, no new seat here), Eval is
 *    a batch-10 owner (Eval stub in InterpStubs, no new seat),
 *    sym_ machinery rides the existing InterpStubs seat; the
 *    SYMSTORE/SYMRETRIEVE defines are already carried
 *    file-private at their use sites (Fcn.kt:1367 vendor 91-92
 *    precedent, MpstimeMain.kt:100 defines.h side) - not
 *    redeclared.
 *  - h018 preprocessor_conditional: SQLITE is defined in the
 *    reference build (LIVE arm note; it arms fcn's SQL paths,
 *    whose owner Fcn.kt documents it); the commented-out
 *    SYMDEBUG stays a comment, as in the vendor; _INTERP_ is a
 *    translation-unit selector with no Kotlin analog (the arm
 *    splits per C translation unit by file, the h004
 *    convention).
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The __label_lookup/__text_function extern pair are the
 *    DOCUMENTED gate-exception double-underscore vendor names
 *    (roster text only; no Kotlin identifier emitted - the
 *    scan gate whitelists them in comments, the ZcommandsCode
 *    unit-192 precedent).
 *  - The extern "C" _getpid/time pair are libc edges already
 *    carried at their use sites (ProcessHandlePid/time() seats;
 *    no new declarations).
 *  - CSTR/USTR are cast macros with no Kotlin analog
 *    (ByteArray/CharArray domain notes, h012).
 */
