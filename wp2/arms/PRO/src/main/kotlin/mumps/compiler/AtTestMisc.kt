package mumps.compiler

// ----------------------------------------------------------------------------
// (B11 unit 308) compiler-at-test.c::<misc> (vendor lines 2-32). Identity:
// units.csv row 308 - compiler-at-test.c::<misc>, c, loc 27,
// comment_ratio 0.069, pointer_marks 0, control_marks 2, score 4.9,
// rank 10, simple - exact match (the file-residual chunk of
// compiler-at-test.c, the banner-residue template of the
// u243/u244/u245/276 family).
// ----------------------------------------------------------------------------

/**
 * PRO-arm translation of compiler-at-test.c::<misc> (roster row 308,
 * batch 11). Translated independently from the pristine vendor
 * source (mumps-14-may-2026.tgz, sha256
 * 621deb2586f27e741ce6db1a2a86fbddf4e4da40c9c6bb363f84c814f53ffdbe),
 * reference source lines 2-32 - the residual chunk of
 * compiler-at-test.c ahead of the atTest() unit: the GPL banner,
 * the May 5 2024 date comment, and the single include directive.
 * No executable code; the banner is accounted below and the
 * include maps onto existing carriers. atTest() itself is the DONE
 * B4 unit-108 seat (AtTest.kt; rendered ahead of its own batch
 * rows per the family convention - this file's only function
 * had its own units.csv row), and its callers (IfCode.kt unit 2,
 * KillCode.kt unit 56, the Parse_ phase cursor) bind unchanged.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h013 vendor_quirk       : the banner carries the vendor's
 *    tab-indented date comment ("//\tMay 5, 2024") exactly as
 *    written; no quirk is fixed.
 *  - h014 out_of_scope_call   : the single include resolves to an
 *    existing implementation - nothing new stubbed:
 *    stdio.h -> the printf tails of atTest are already rendered
 *    as System.out.print writes with byte-identical messages
 *    (AtTest.kt h002; the CompilerState Out carriers, the
 *    CopyrightNotice/Epi writer edges - the same stdio ledger as
 *    the CompilerWriteMisc.kt row-276 disposition).
 *  - h015 dead_code           : no dead arm occurs in this chunk
 *    (the atTest body itself is AtTest.kt, rendered). The row's
 *    two control_marks are the GPL banner's own words "for" ("
 *    License for more details") and "if" ("if not, write to the
 *    Free Software Foundation") - metric echoes of banner text,
 *    not code, per the banner-residue convention.
 *  - h017 naming              : the banner and date comment are
 *    kept verbatim; the file is named after the vendor source
 *    file (AtTest + Misc, the CompilerWriteMisc precedent).
 *  - h018 preprocessor_conditional : no #ifdef/#ifndef occurs in
 *    this chunk, so h018 is not triggered here (conditionals
 *    inside stdio.h belong to libc, not the corpus).
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - This file's only content besides the banner is the one
 *    include line; the vendor's atTest() follows in AtTest.kt
 *    (B4 unit 108; the imbalanced-quote printf tail renders
 *    byte-identically there).
 *  - The banner text is not re-emitted as comment text per the
 *    established banner-residue convention (CompilerWriteMisc
 *    row 276, FnumberMisc row 295): the GPL banner is repository
 *    metadata, kept in the vendor source and the LICENSE ledger.
 */
