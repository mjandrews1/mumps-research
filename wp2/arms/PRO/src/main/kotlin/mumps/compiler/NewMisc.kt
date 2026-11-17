package mumps.compiler

/**
 * PRO-arm translation of compiler-new.c::<misc> (B4 unit 123, rank
 * 23). Translated independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), the file's
 * full residue after the copyright banner (misc physical loc 30;
 * the sole function in the file exceeds the chunk_loc 600 threshold,
 * so the residue is the header alone).
 *
 * Identity verified against the study tool (tools/select_study_
 * subset.py): compiler-new.c::<misc>, c, loc 30, comment_ratio
 * 0.062, pointer_marks 0, control_marks 2, score 5.2, batch 4 -
 * exact match to wp1/study-subset/units.csv row 124.
 *
 * compiler-new.c - "Mumps Bioinformatics Software Library": the
 * file head is the May 10, 2024 copyright banner plus three
 * includes (stdio.h, mumpsc/memsize.h, mumpsc/compiler-macros.h,
 * mumpsc/compiler-globals-ext.h). The sole function, new_code
 * (loc 106, units.csv row 66), was translated as the B3 unit 65
 * corpus unit (NewCode.kt) - not part of this residue.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h013 dead_store       : nothing emitted - the residue is
 *    declarations/includes with no executable code.
 *  - h014 out_of_scope_call: the include roster resolves to the
 *    arm's established carriers: memsize.h -> CompilerState
 *    buffer sizes and the memsize constants documented on their
 *    owning units; compiler-macros.h -> the macro expansions
 *    inlined at their owning units; compiler-globals-ext.h ->
 *    the CompilerState object (B1/B2 file-scope globals owner).
 *  - h015 grammar_collision: pre-push scan incl. cross-file
 *    top-level names: this file introduces no new top-level
 *    Kotlin declarations - no collisions (new_code's Kotlin
 *    translation lives in NewCode.kt, B3 unit 65).
 *  - h017 naming          : header residue; no vendor names are
 *    rendered as Kotlin identifiers here.
 *  - h018 reference_build_visibility: the includes are live in
 *    the reference build (compiler-new.c is built into the
 *    reference compiler); the GPL banner is documentation.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The banner copyright years list (2003, 2004, 2008, 2015, 2017,
 *    2022, 2024) and the May 10, 2024 file date are kept verbatim
 *    in this roster; the GPL text itself is the standard library
 *    banner (cf. the CopyrightNotice carrier, B1).
 */
// Header residue: no Kotlin declarations emitted (see KDoc).
