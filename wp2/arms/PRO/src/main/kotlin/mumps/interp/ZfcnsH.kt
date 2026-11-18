package mumps.interp

/**
 * PRO-arm translation of include/mumpsc/zfcns.h::<misc>
 * (batch-8 unit 242, units.csv data line 242, rank 38).
 * Translated independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), the
 * header's full text (misc physical loc 18, comment_ratio
 * 0.633: the LGPL banner, the bifs block comment, the
 * prototype roster).
 *
 * Identity verified against the study tool (tools/select_study_
 * subset.py): include/mumpsc/zfcns.h::<misc>, c, loc 18,
 * comment_ratio 0.633, pointer_marks 0, control_marks 2,
 * score 6.2, rank 38, batch 8 - exact match to
 * wp1/study-subset/units.csv line 242.
 *
 * zfcns.h (self-labeled "bifs.h - Mumps runtime library") is
 * the PROTOTYPE ROSTER of the runtime builtin family.  Every
 * prototype already has a rendered seat; this residue
 * documents the roster and emits no new declarations:
 *  - _ascii -> InterpBifs/Zfcn family seat (ascii carrier);
 *  - _horolog -> the _horolog seat raised at u188 (owner
 *    interp-sym.cpp, batch-9 residue);
 *  - _justify -> the justify family seat;
 *  - _length -> the _length family seat (MstringLength/
 *    GlobalLength distinct mdh siblings);
 *  - _piece -> InterpPiece.kt:93 _piece (the real seat; the
 *    MdhStubs _piece entry folds there);
 *  - _random -> the random family seat;
 *  - zfcn -> guarded by #ifndef _INTERP_ here, but the
 *    ZfcnBifs (mdh) and Zfcn (interp) seats own the render -
 *    h018: on the reference interpreter build _INTERP_ is
 *    defined inside the interpreter TU, so this declaration
 *    is EXCLUDED there (the Zfcn.kt seat binds instead);
 *  - ServiceInterface -> ServiceInterface.kt (u187);
 *  - the commented-out BuildGlobal/BuildLocal prototype pair
 *    (vendor 65-66) - both RESOLVED this batch (rows 232/233,
 *    BuildLocal.kt/BuildGlobal.kt); kept commented out as in
 *    the vendor.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h013 dead_store : nothing executable in the header.
 *  - h014 out_of_scope_call: no stubs introduced; all
 *    prototypes bind to rendered seats.
 *  - h015 grammar_collision: this file introduces no new
 *    top-level Kotlin declarations - no collisions.
 *  - h017 naming     : header residue; no vendor names are
 *    rendered as new Kotlin identifiers here.
 *  - h018 reference_build_visibility: the #ifndef _INTERP_
 *    zfcn arm resolves per-TU as the vendor does (excluded in
 *    the interpreter TU); the #ifndef BIFS_H include guard
 *    is a no-op in Kotlin.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The file self-identifies as "bifs.h" in its own block
 *    comment while living at zfcns.h - kept.
 *  - The copyright banner is the LGPL variant (2001/2002/
 *    2006 A.D. years) - distinct from the GPL banners of the
 *    .cpp/.c files; kept in the roster.
 */
// Header residue: no Kotlin declarations emitted (see KDoc).
