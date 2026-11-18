package mumps.compiler

/**
 * PRO-arm translation of include/mumpsc/compiler-macros.h::<misc>
 * (batch-8 unit 227, units.csv data line 227, rank 190).
 * Translated independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), the
 * header's full text (misc physical loc 104: the May 15, 2024
 * banner, the prototype roster, the emission macros, the
 * constant blocks).
 *
 * Identity verified against the study tool (tools/select_study_
 * subset.py): include/mumpsc/compiler-macros.h::<misc>, c,
 * loc 104, comment_ratio 0.096, pointer_marks 3,
 * control_marks 4, score 24.8, rank 190, batch 8 - exact
 * match to wp1/study-subset/units.csv line 227.
 *
 * Roster documentation (vendor lines, all carried by existing
 * seats):
 *  - 30-36: prototypes set_code/for_code/error_code/abtrx/
 *    if_code/lock_code/copyright_notice - all rendered units
 *    (SetCode/ForCode/Errmod/Abrtx/IfCode/LockCode/
 *    CopyrightNotice; copyright_notice re-listed and verified
 *    as B2 unit 47).
 *  - 38-49: the INTERPRET emission macro - inlined at its
 *    call sites as CompilerState.interpret (h003 state_macro
 *    carrier; unit-2 IfCode precedent).
 *  - 51-53: the SymCheck macro (ExecSym[ExecX] guard) -
 *    inlined at its owning units with the ExecSym/ExecX
 *    CompilerState members.
 *  - 55/57: Sysout(x) / Sysout_skip - h002 simple_output_macro,
 *    expanded at the owning units (CopyrightNotice carrier
 *    family, Fcninit unit 195).
 *  - 59-68: the Mglobal op codes SWAP 30/STORE 1/RETRIEVE 0/
 *    NEXT 2/XNEXT 8/GKILL 3/INIT 6/GOPEN 7/EMPTY 5/CLOSE 4 -
 *    per-unit file-private const precedent (defines.h family).
 *  - 72-81: the error return codes PARSE_ERROR 1..BAD_LOCK 9 -
 *    carried per owning unit (MumpsOpen unit 3 has
 *    PARSE_ERROR/ARG_LIST_ERROR; the rest name their owners).
 *  - 86-89: SET 0 / SET_STORE 1 (entry points in set).
 *  - 93-123: the command character codes JOB 'j' .. CATCH 'c'
 *    (and the numeric KILL2 20/LBL 21) - per-unit constants;
 *    NOTE the vendor reuses 'b' for BOTH CONTINUE and BREAK,
 *    and 'c' for BOTH CLOSE and CATCH (kept in the roster;
 *    call sites disambiguate by parse state).
 *  - 125: FBMAX 4000 - CompilerState.FBMAX.
 *  - 127-130: TAB - h018: the NOTABS arm (TAB ' ') is EXCLUDED
 *    on the reference build (NOTABS undefined); TAB = 9.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h002 simple_output_macro: the emission macros are already
 *    expanded at their owning units (no new seats).
 *  - h013 dead_store : nothing executable in the header.
 *  - h014 out_of_scope_call: no stubs; all prototypes bind to
 *    rendered seats.
 *  - h015 grammar_collision: this file introduces no new
 *    top-level Kotlin declarations - no collisions.
 *  - h017 naming     : header residue; no vendor names are
 *    rendered as new Kotlin identifiers here.
 *  - h018 reference_build_visibility: the NOTABS/TAB ' ' arm
 *    is dead on the reference build (excluded).
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The duplicate command characters ('b' CONTINUE/BREAK,
 *    'c' CLOSE/CATCH, and the numeric SET 0 vs char SET 's'
 *    overloads) are vendor originals - kept in documentation.
 *  - The "#define SET 0" (entry point) and "#define SET 's'"
 *    (command) reuse the same token in different blocks - the
 *    C preprocessor takes the LAST definition in scope; kept
 *    as note (Kotlin call sites use per-unit constants).
 */
// Header residue: no Kotlin declarations emitted (see KDoc).
