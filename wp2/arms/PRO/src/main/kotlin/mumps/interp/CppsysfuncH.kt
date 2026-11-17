package mumps.interp

/**
 * PRO-arm translation of include/mumpsc/cppsysfunc.h::<misc> (B3
 * unit 80, rank 125). Translated independently from the pristine
 * vendor source (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe),
 * the full header file (misc physical loc 23, raw residue after
 * the copyright banner and include guard).
 *
 * Identity verified against the study tool (tools/select_study_
 * subset.py): include/mumpsc/cppsysfunc.h::<misc>, c, loc 23,
 * comment_ratio 0.5, pointer_marks 2, control_marks 2, score
 * 12.9, batch 3 - exact match to wp1/study-subset/units.csv row
 * 125 (single misc chunk; no function in the header reaches the
 * min_loc 15 threshold, so the whole declaration surface is the
 * unit).
 *
 * cppsysfunc.h - "Mumps Compiler Run-Time Support Functions":
 * the header is a pure extern-"C" declaration surface (the
 * commented-out getstr1 prototype above the live declarations is
 * kept in the vendor file verbatim); no definitions, no
 * constants. The Kotlin rendering is this owner roster; each
 * declaration resolves as noted below. The file lives in package
 * mumps.interp because the declared run-time support functions
 * are interpreter-owned (DirectRead's owner is here; the global
 * wrappers' owners are libmpscpp.cpp/mdh members).
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h014 out_of_scope_call: the declaration roster below names
 *    each function's owner and its arm status (real reuse, stub,
 *    or untranslated owner).
 *  - h015 grammar_collision: pre-push scan incl. cross-file
 *    top-level names: this file introduces no new top-level
 *    Kotlin declarations - no collisions.
 *  - h017 naming          : declaration-only header; no vendor
 *    names are rendered as Kotlin identifiers here (the roster
 *    table is documentation).
 *  - h018 reference_build_visibility: the include guard
 *    CPPSYSFUNC_H is tested in the reference build (every
 *    translation unit including the header sees the guarded
 *    declarations); the extern-"C" linkage specifier is a C++
 *    linkage concern with no JVM rendering - noted.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - LocalName is declared TWICE (the identical extern "C"
 *    prototype appears back-to-back) - a vendor copy-paste
 *    quirk kept verbatim in the roster below.
 *  - The commented-out getstr1 prototype (first line of the
 *    residue) is superseded by the live TimeOut_getstr1
 *    declaration - kept as a roster note.
 *
 * Owner roster (extern "C" declarations, in file order):
 *  - TimeOut_getstr1(int, unsigned char[], char*, int): owner
 *    interp run-time reader (not a corpus unit; the getstr2 B1
 *    unit is the related corpus function).
 *  - DirectRead(int, unsigned char*): REAL REUSE - B1 unit
 *    (interp/DirectRead.kt).
 *  - VariableNotFound(long, char*): owner interp error reporting
 *    (not a corpus unit).
 *  - ErrorMessage(const char*, long): owner interp error
 *    reporting (not a corpus unit; Prnterr B2 unit 58 is the
 *    related corpus function).
 *  - sigint(int): owner interp signal handling (not a corpus
 *    unit).
 *  - Merge(unsigned char*, unsigned char*, MSV*): owner
 *    libmpscpp/interp merge machinery (not a corpus unit).
 *  - GlobalData(unsigned char*, unsigned char*, MSV*): the
 *    THREE-ARGUMENT compiler-runtime form - a DIFFERENT vendor
 *    function from the one-argument GlobalData intrinsic (B3
 *    unit 86, mdh/GlobalData.kt); owner not a corpus unit.
 *  - LocalData(unsigned char*, unsigned char*, MSV*): owner not a
 *    corpus unit.
 *  - GlobalOrder(unsigned char*, unsigned char*, unsigned char*,
 *    MSV*): the four-argument compiler-runtime form (distinct
 *    from the two-argument intrinsic overloads, B2 unit 39 / B3
 *    unit 87); owner not a corpus unit.
 *  - LocalOrder(unsigned char*, unsigned char*, unsigned char*,
 *    MSV*): owner not a corpus unit.
 *  - LocalNext(unsigned char*, unsigned char*, MSV*): owner not
 *    a corpus unit.
 *  - GlobalNext(unsigned char*, unsigned char*, MSV*): owner not
 *    a corpus unit.
 *  - LocalName(unsigned char*, unsigned char*, unsigned char*,
 *    MSV*): REAL REUSE - B1 unit (interp/GlobalName.kt; declared
 *    twice in the vendor header, see the quirk note above).
 *  - GlobalName(unsigned char*, unsigned char*, unsigned char*,
 *    MSV*): owner the B1/B2 global-name family (related corpus
 *    unit in a later batch).
 *  - LocalGet(unsigned char*, unsigned char*, unsigned char*,
 *    MSV*): owner not a corpus unit.
 *  - GlobalGet(unsigned char*, unsigned char*, unsigned char*,
 *    MSV*): owner not a corpus unit.
 *  - GlobalQuery(unsigned char*, unsigned char*, char, MSV*):
 *    owner not a corpus unit.
 *  - LocalQuery(unsigned char*, unsigned char*, MSV*): owner not
 *    a corpus unit.
 *  - fullUnpad(unsigned char*, MSV*): owner not a corpus unit.
 */
// Declaration-only header residue: no Kotlin declarations emitted
// (see the owner roster in the KDoc above).
