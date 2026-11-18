package mumps.interp

/**
 * PRO-arm translation of interp-sym.cpp::<misc> (batch 9 unit,
 * roster row 259, file rank 188). Translated independently from
 * the pristine vendor source (mumps-14-may-2026.tgz, sha256
 * 621deb25...ffdbe); the study-tool residual chunk (2008-style
 * GPL banner, the SQLITE define, the _INTERP_/CSTR/USTR macros,
 * the include block, the using-namespace edge, the forward
 * declarations of Interpret/ScanParse/Eval, the __label_lookup/
 * __text_function extern pair, the extern \"C\" _getpid/time
 * pair, the intLong define, and the file-tail _text_function/
 * _label_lookup function-pointer stubs with their default
 * bindings; loc 75, comment_ratio 0.4, pointer_marks 4,
 * control_marks 5, score 24.3 - exact match to
 * wp1/study-subset/units.csv row 259; every other function in
 * interp-sym.cpp exceeds min_loc and is claimed by its own
 * units.csv row (BuildLocal u232, ScanParse u234, Eval u261 and
 * the batch-10 machinery owners, not redeclared).
 *
 * Header residue: no Kotlin declarations emitted.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h013 dead_store      : none in the chunk (the file-tail
 *    stubs are live in the vendor - see the vendor notes).
 *  - h014 out_of_scope_call: the declaration roster resolves
 *    each declaration to its arm status - Interpret is the
 *    DONE MumpsMain family seat (Interpret.kt, B6 unit 155),
 *    ScanParse is DONE (ScanParse.kt, batch-8 unit 234), Eval
 *    is DONE (Eval.kt, batch-9 unit 261), sym_ machinery rides
 *    the existing InterpStubs seat (batch-10 owner).
 *  - h018 preprocessor_conditional: SQLITE is defined in the
 *    reference build (LIVE arm note; arms the sym_ data paths
 *    of the batch-10 owners); SYMDEBUG does not appear in this
 *    file (the BLOCK ifdef arms of the sym_ bodies belong to
 *    their owner rows); _INTERP_ is a translation-unit selector
 *    with no Kotlin analog (the arm splits per C translation
 *    unit by file, the h004 convention).
 *
 * Vendor notes (translated faithfully, not \"fixed\"):
 *  - The file tail (lines 1440-1444) defines the two
 *    one-line forwarding stubs and binds the function-pointer
 *    globals: _text_function(int) returns NULL, _label_lookup
 *    (char*) returns -1, then __label_lookup = _label_lookup
 *    and __text_function = _text_function (the documented
 *    gate-exception double-underscore vendor names, roster
 *    text only; no Kotlin identifier emitted - the scan gate
 *    whitelists them in comments, the InterpFcnMisc u202 and
 *    ZcommandsCode u192 precedents). No call site in the arm
 *    dereferences either pointer (SearchLine/funk-style
 *    callers are vendor C text kept inside emitted slices or
 *    commented ledgers), so the tail is carried as this
 *    disposition ledger, not a dead binding.
 *  - The extern \"C\" _getpid/time pair are libc edges already
 *    carried at their use sites (ProcessHandlePid/time()
 *    seats); CSTR/USTR are cast macros with no Kotlin analog
 *    (CharArray byte-domain notes, h012).
 */
