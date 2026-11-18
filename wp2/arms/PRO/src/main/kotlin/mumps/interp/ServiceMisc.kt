package mumps.interp

/**
 * PRO-arm translation of interp-service.cpp::<misc> (batch 9
 * unit, roster row 264, file rank 134). Translated independently
 * from the pristine vendor source (mumps-14-may-2026.tgz,
 * sha256 621deb25...ffdbe); the study-tool residual chunk
 * (2008-style GPL banner dated 2026, the SQLITE define, the
 * _INTERP_/CSTR/USTR macros, the include block, the
 * using-namespace edge, the forward declarations of Interpret/
 * ScanParse/Eval, the __label_lookup/__text_function extern
 * pair, the intLong define, and the ServiceInterface
 * prototype); loc 55, comment_ratio 0.179, pointer_marks 2,
 * control_marks 3, score 13.9 - exact match to
 * wp1/study-subset/units.csv row 264; the one other function in
 * interp-service.cpp (ServiceInterface itself, vendor line 87+)
 * exceeds min_loc and is claimed by its own units.csv row, not
 * redeclared.
 *
 * Header residue: no Kotlin declarations emitted.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h013 dead_store      : none in the chunk.
 *  - h014 out_of_scope_call: the declaration roster resolves
 *    each declaration to its arm status - Interpret is the
 *    DONE MumpsMain family seat (Interpret.kt, B6 unit 155),
 *    ScanParse is DONE (ScanParse.kt, batch-8 unit 234), Eval
 *    is DONE (Eval.kt, batch-9 unit 261), ServiceInterface
 *    rides the existing ServiceInterface.kt seat; sym fcn
 *    rides the existing seats per the fcns.h ledger.
 *  - h018 preprocessor_conditional: SQLITE is defined in the
 *    reference build (LIVE arm note; it arms the SQL paths of
 *    the ServiceInterface owner); _INTERP_ is a
 *    translation-unit selector with no Kotlin analog (the arm
 *    splits per C translation unit by file, the h004
 *    convention - it is why zfcn.h seats one declaration set
 *    per TU, the u242 note).
 *
 * Vendor notes (translated faithfully, not \"fixed\"):
 *  - The __label_lookup/__text_function extern pair is the
 *    DOCUMENTED gate-exception double-underscore vendor name
 *    (roster text only; no Kotlin identifier emitted - the
 *    scan gate whitelists them in comments, the InterpFcnMisc
 *    u202 and ZcommandsCode u192 precedents); ServiceInterface
 *    binds its zfcn callables through the arm's own
 *    concretions, so the pointers stay roster text here.
 *  - intLong is an empty textual define (no expansion); it is
 *    kept in this ledger for roster completeness only.
 *  - CSTR/USTR are cast macros with no Kotlin analog
 *    (CharArray byte-domain notes, h012).
 */
