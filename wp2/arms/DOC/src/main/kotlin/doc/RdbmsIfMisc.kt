package doc

// Unit 102 (B4 tranche 1): rdbms-if.c::<misc> (batch 4, simple stratum;
// LOC 32, score 9.6). The file-residue chunk: the A.D. 2001-2002 LGPL
// banner (kept verbatim), the rdbms.c block comment (PostgreSQL/ODBC
// interface), the include block, and the extern declaration. The
// file's central body sql is a batch-5 corpus unit and is NOT rendered
// here (u115 RdbmsIfMisc precedent style: declaration-only ledger).
//
// H-018 (preprocessor arms, resolved against the reference build - the
// mumps-sqlite3 reference configuration): the #ifdef SQL arm
// (windows.h/sql.h/sqlext.h) is EXCLUDED (SQL undefined); the
// POSTGRES define chain (ALLOW_POSTGRES/HAVE_PGSQL_H, the two libpq
// include variants) is EXCLUDED (autotools did not enable Postgres in
// the reference build); the _MSC_VER pair resolves to the live
// non-MSVC arm (unistd.h; no JVM analog, ledger only). The
// _XOPEN_SOURCE expose has no effect in the render (noted, not
// silently dropped).
//
// H-004: extern int ierr (rdbms-if.c:62, "the only wonderful global")
// binds the arm carrier Msv.ierr (Carriers.kt, stateVector.h:50) - the
// field already exists from the batch-1 renders; binding noted here.
//
// include ledger (vendor residual chunk): stdio/stdlib/string ride the
// arm CstringOps carriers; mumpsc/config.h rides H-018 resolution;
// mumpsc/fcns.h and mumpsc/global.h are the fdMacros/global carriers
// (below-roster owners, ledger only).

private const val RDBMS_IF_ONLY_GLOBAL_NOTE = "extern int ierr binds Msv.ierr (H-004 carrier)"
