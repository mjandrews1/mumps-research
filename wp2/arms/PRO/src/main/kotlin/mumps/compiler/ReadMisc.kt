package mumps.compiler

/**
 * PRO-arm translation of compiler-read.c::<misc> (B3 unit 93,
 * rank 25). Translated independently from the pristine vendor
 * source (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), the
 * file's full residue after the copyright banner (misc physical
 * loc 30; the sole function, read_code (loc 96), is claimed by
 * its own units.csv row 160, a batch-6 corpus unit - so the
 * residue is the header alone).
 *
 * Identity verified against the study tool (tools/select_study_
 * subset.py): compiler-read.c::<misc>, c, loc 30, comment_ratio
 * 0.062, pointer_marks 0, control_marks 2, score 5.2, batch 3 -
 * exact match to wp1/study-subset/units.csv row 25.
 *
 * compiler-read.c - "Mumps Bioinformatics Software Library": the
 * file head is the May 15, 2024 copyright banner plus three
 * includes (stdio.h, mumpsc/memsize.h, mumpsc/compiler-macros.h,
 * mumpsc/compiler-globals-ext.h). The sole function, read_code
 * (loc 96, units.csv row 160), is a batch-6 corpus unit - not
 * part of this residue.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h013 dead_store       : nothing emitted - the residue is
 *    declarations/includes with no executable code.
 *  - h014 out_of_scope_call: the include roster resolves to the
 *    arm's established carriers: memsize.h -> CompilerState
 *    buffer sizes and the memsize constants documented on their
 *    owning units (NOTABS memsize.h:43 documented on B3 unit 78,
 *    Readf); compiler-macros.h -> the macro expansions inlined
 *    at their owning units; compiler-globals-ext.h -> the
 *    CompilerState object (B1/B2 file-scope globals owner).
 *  - h015 grammar_collision: pre-push scan incl. cross-file
 *    top-level names: this file introduces no new top-level
 *    Kotlin declarations - no collisions.
 *  - h017 naming          : header residue; no vendor names are
 *    rendered as Kotlin identifiers here.
 *  - h018 reference_build_visibility: the includes are live in
 *    the reference build (compiler-read.c is built into the
 *    reference compiler); the GPL banner is documentation.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The May 15, 2024 file date is kept verbatim in this roster;
 *    the GPL text is the standard library banner (cf. the
 *    CopyrightNotice carrier, B1). The banner is byte-identical
 *    to compiler-epi.c's (unit 92) except the date line - a
 *    vendor template quirk noted, not fixed.
 */
// Header residue: no Kotlin declarations emitted (see KDoc).
