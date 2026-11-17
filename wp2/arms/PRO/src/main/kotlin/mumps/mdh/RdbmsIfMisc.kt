package mumps.mdh

/**
 * PRO-arm translation of rdbms-if.c::<misc> (B4 unit 115, rank
 * 85). Translated independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe); the
 * study-tool residual chunk is the file head: the "//#" banner,
 * the rdbms.c descriptive header, the include set, the SQL /
 * POSTGRES preprocessor block, and the "extern int ierr"
 * forward declaration (misc physical loc 32; the sole function,
 * sql - loc 209, units.csv row 126, a batch-5 corpus
 * unit, NOT part of this residue; the sql carrier already rides
 * the InterpStubs owner-named stub).
 *
 * Identity verified against the study tool (tools/select_study_
 * subset.py): rdbms-if.c::<misc>, c, loc 32, comment_ratio
 * 0.522, pointer_marks 0, control_marks 6, score 9.6 - exact
 * match to wp1/study-subset/units.csv row 116.
 *
 * rdbms-if.c - "Mumps Relational DataBase Management System
 * InterFace": the PostgreSQL/ODBC interface shim. The residue
 * is a pure visibility/declaration surface; the roster below
 * resolves it.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h013 dead_store         : nothing emitted - the residue is
 *    includes/conditionals with no executable code.
 *  - h014 out_of_scope_call  : the declaration roster resolves
 *    each include/forward to its arm status (see below); no
 *    stubs are raised for future owners (GlobalClassH
 *    precedent).
 *  - h015 grammar_collision  : pre-push scan clean; no new
 *    top-level Kotlin declarations.
 *  - h017 naming             : header residue; no vendor names
 *    rendered as Kotlin identifiers here.
 *  - h018 preprocessor_conditional: the reference build
 *    resolves the whole database block OUT: SQL is undefined
 *    (windows.h/sql.h/sqlext.h not included - the ODBC arms are
 *    omitted), and HAVE_PGSQL_H/ALLOW_POSTGRES do not define
 *    POSTGRES (postgresql/libpq-fe.h and pgsql/libpq-fe.h not
 *    included - both include arms omitted; the
 *    HAVE_PGSQL_H == POSTGRESQL / == PGSQL equality tests and
 *    the "@VAR@ build-substitution macros are documented as
 *    comments per the h018 action). _XOPEN_SOURCE and the
 *    _MSC_VER/unistd.h guard are reference-build visibility
 *    notes only.
 *
 * Owner roster (vendor order):
 *  - stdio/stdlib/string.h: the C library edges - resolve to
 *    the arm's established helpers (strlenC/strcpyC/atoiC,
 *    CstringOps; malloc edges on the Hash precedent).
 *  - mumpsc/config.h + mumpsc/fcns.h + mumpsc/global.h: the
 *    interpreter/compiler declaration surfaces - fcns.h and
 *    global.h resolve to their owner units (cppsysfunc.h u80
 *    roster precedent; global.h is the class-global surface,
 *    u67 roster).
 *  - the SQL/POSTGRES block: compiled out of the reference
 *    binary (h018 note above).
 *  - "extern int ierr;" (rdbms-if.c:76): resolves to the
 *    interpreter's global int ierr (stateVector.h:50) - the
 *    Msv.ierr carrier field (raised in B1; the vendor extern
 *    and sql()'s "ierr = 0" ride the same state vector slot).
 *  - sql (declared by the banner comment block, defined below
 *    the residue): InterpStubs owner-named stub - rdbms-if.c::sql,
 *    units.csv row 126, batch-5 corpus unit (future owner).
 */
// Header residue: no Kotlin declarations emitted (see KDoc).
