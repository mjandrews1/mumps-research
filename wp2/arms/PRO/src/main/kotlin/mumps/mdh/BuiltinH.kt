package mumps.mdh

/**
 * PRO-arm translation of include/mumpsc/builtin.h::<misc>
 * (batch-8 unit 224, units.csv data line 224, rank 211).
 * Translated independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), the
 * header's full text (misc physical loc 33: the June 9, 2024
 * banner, the builtinTable.h include, and the builtin
 * prototype roster).
 *
 * Identity verified against the study tool (tools/select_study_
 * subset.py): include/mumpsc/builtin.h::<misc>, c, loc 33,
 * comment_ratio 0.083, pointer_marks 12, control_marks 0,
 * score 33.5, rank 211, batch 8 - exact match to
 * wp1/study-subset/units.csv line 224.
 *
 * builtin.h is the PROTOTYPE ROSTER of the mdh builtin family
 * (libmpscpp.cpp / mpstime.cpp residents dispatched through
 * ZfcnBifs).  Every prototype already has a rendered seat (the
 * ZfcnBifs call sites bind them); this residue documents the
 * roster and emits no new declarations:
 *  - Getenv -> LibmpscppMisc5 (u59); Cosine -> Cosine.kt (u36);
 *    Sim1 -> Sim1.kt (u199, the June 2024 default-arg seat);
 *    Dice -> Dice.kt; Jaccard -> LibmpscppMisc9 (Msv form) +
 *    the Global overload in MdhStubs; Avg/Count/Max/Min/Sum ->
 *    their own seats; Scan -> Scan.kt (u81); ScanAlnum ->
 *    ScanAlnum.kt (three-arg with the vendor default-arg
 *    "= NULL" fold plus the Reader/Scanner forms); Input ->
 *    Input.kt (u23); Multiply -> Multiply.kt; Transpose,
 *    SmithWaterman -> SmithWaterman.kt (Sw.kt helper);
 *    TermCorrelate/DocCorrelate/StopInit/StopLookup/SynInit/
 *    SynLookup/Centroid/BMGSearch/Shred/ShredQuery/Replace ->
 *    LibmpscppMisc9 (Replace in MdhStubs); IDF -> the MdhStubs
 *    stub raised at u187 - its owner is roster row 240 THIS
 *    batch.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h013 dead_store : nothing executable in the header.
 *  - h014 out_of_scope_call: the IDF prototype keeps its named
 *    stub until row 240 lands (batch-8 sibling).
 *  - h015 grammar_collision: this file introduces no new
 *    top-level Kotlin declarations - no collisions.
 *  - h017 naming     : header residue; no vendor names are
 *    rendered as new Kotlin identifiers here.
 *  - h018 preprocessor_conditional: none in this header.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The commented-out ScanAlnum two-arg prototype (vendor
 *    line 21, superseded by the default-arg three-arg form
 *    directly below it) is kept as documentation only.
 *  - The two StopInit / StopLookup overload rows (Msv form
 *    then char*-only form) and the two Shred families
 *    (Msv form in the roster, char* form in MdhStubs) are
 *    separate C overloads - both seats exist (cf. the
 *    Jaccard Global overload).
 *  - struct MSV forward declaration (line 7) is the Msv
 *    carrier (interp side, unit-23 owner).
 */
// Header residue: no Kotlin declarations emitted (see KDoc).
