package mumps.compiler

/**
 * PRO-arm translation of compiler-zcommands.c::<misc> (B7 unit
 * 215, units.csv data row 215: loc 30, comment_ratio 0.062,
 * pointer_marks 0, control_marks 2, score 5.2,
 * complexity_rank 28, simple stratum). The file's full residue
 * after the copyright banner: the June 2024 date comment and
 * four includes (stdio.h, mumpsc/memsize.h,
 * mumpsc/compiler-macros.h, mumpsc/compiler-globals-ext.h). The
 * sole function, zcommands_code (loc 143, units.csv data row
 * 192), is a B7 corpus unit (ZcommandsCode.kt) - not part of
 * this residue (EpiMisc unit-92 precedent).
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h013 dead_store       : nothing emitted - the residue is
 *    declarations/includes with no executable code.
 *  - h014 out_of_scope_call: the include roster resolves to the
 *    arm's established carriers: memsize.h -> CompilerState
 *    buffer sizes; compiler-macros.h -> the macro expansions
 *    inlined at their owning units; compiler-globals-ext.h ->
 *    the CompilerState object (B1/B2 file-scope globals owner);
 *    stdio.h -> the CompilerState Out seat.
 *  - h015 grammar_collision: pre-push scan incl. cross-file
 *    top-level names: this file introduces no new top-level
 *    Kotlin declarations - no collisions.
 *  - h017 naming          : header residue; no vendor names are
 *    rendered as Kotlin identifiers here.
 *  - h018 reference_build_visibility: the includes are live in
 *    the reference build (compiler-zcommands.c is built into
 *    the reference compiler); the GPL banner is documentation.
 */

// Header residue: no Kotlin declarations emitted
