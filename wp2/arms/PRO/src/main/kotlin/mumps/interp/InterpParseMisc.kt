package mumps.interp

/**
 * PRO-arm translation of interp-parse.cpp::<misc> (B3 unit 75, rank
 * 192). Translated independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe).
 *
 * Identity verified against the study tool (tools/select_study_
 * subset.py): the misc unit is the file residue (unclaimed lines in
 * file order; no small-fn bodies append - every function in
 * interp-parse.cpp exceeds 15 physical loc or is a claimed unit) -
 * interp-parse.cpp::<misc>, cpp, loc 59, comment_ratio 0.473,
 * pointer_marks 6, control_marks 3, score 25.7, batch 3 - exact
 * match to wp1/study-subset/units.csv row 192. The residue is the
 * file header: the GPL banner, the include block, the local #define
 * block, the forward-prototype block, the extern ierr1 declaration,
 * and the function banner comments (strmove/parse/patrn1/patrn/
 * getstr2/readf/inout/prnterr), plus the #include of inline.h.
 *
 * The translatable content is the local #define block; the rest is
 * an owner roster (every prototype resolves to an existing carrier
 * or an owner-named stub - none is raised here).
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h013 dead_store        : CSTR/USTR are cast macros
 *    ((char*)/(unsigned char*)) with no Kotlin equivalent - noted,
 *    not carried.
 *  - h014 out_of_scope_call : the prototype roster below names each
 *    owner; no new stubs (all resolve to existing implementations
 *    or stubs).
 *  - h015 grammar_collision : OPEN_PAREN (206) is already a private
 *    const in Parse_.kt (unit 63, same vendor line 80); restated
 *    here as a file-private const for this unit's scope - no
 *    package collisions.
 *  - h017 naming            : GOSUBMAX/SWAP/FORMAX/EXECLEVS/LBLS/
 *    OPEN_PAREN/COMMA/CLOSE_PAREN keep vendor names.
 *  - h018 preprocessor_conditional: #define SQLITE (line 54) and
 *    #define _INTERP_ (line 55) are live in the reference build -
 *    the SQLITE arm is already translated as live in unit 63
 *    (parse_ (dollar)data child check); _INTERP_ gates global.h
 *    declaration selection, owner of that header's units.
 *
 * OWNER ROSTER (prototype -> carrier/owner):
 *  - lpad, substr (interp-strmanip.cpp; substr is batch-7),
 *    sigint, TimeOut, TimeOut_getstr1, GlobalQuery, Qsub, Qlength:
 *    not yet translated / later batches.
 *  - LocalName: DONE (LocalName.kt, B1). DirectRead: DONE (unit
 *    24). _SymFree: DONE (unit 28). zfcn/fcn/xindex/sql: existing
 *    InterpStubs stubs (owners interp-bifs.cpp / interp-fcn.cpp
 *    batch-4 / interp-strmanip.cpp / interp-sql.cpp batch-9).
 *  - makeLock/cleanLocks: DONE (B2 unit 56, LockMisc.kt).
 *  - patrn1: DONE (B1 unit 30). patrn: DONE (B3 unit 64, Patrn.kt).
 *    static patrn1 prototype's file-scope staticness noted.
 *  - ErrorMessage: InterpStubs owner comment (Prnterr.kt edge,
 *    unit 58).
 *  - extern short ierr1 (libmpscpp.cpp:80): MdhStubs.ierr1.
 *  - #include <mumpsc/inline.h>: strmove (InterpStubs stub, owner
 *    interp-strmanip.cpp::strmove, raised by unit 63).
 *  - cannon (interp twin): its prototype is NOT in this residue
 *    (claimed as its own corpus unit, interp-parse.cpp::cannon,
 *    batch-8); the InterpStubs cannon stub raised by unit 64 names
 *    this misc unit - the correct owner is the batch-8 unit (noted
 *    in the journal; the stub stays until that unit lands).
 */

private const val GOSUBMAX = 50   // vendor interp-parse.cpp:66
private const val SWAP = 30       // :67
private const val FORMAX = 30     // :68
private const val EXECLEVS = 40   // :69
private const val LBLS = 80       // :70
private const val OPEN_PAREN = 206 // :76 (cf. unit 63 Parse_.kt)
private const val COMMA = 207     // :77
private const val CLOSE_PAREN = 209 // :78
