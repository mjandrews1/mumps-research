package doc

// Unit 153 (B5 tranche 3): rdbms-if.c::sql (batch 5, complex
// stratum; units.csv row 153: loc 209, score 68.3, rank 253).
// The database trampoline: a single entry whose Windows-ODBC
// and PostgreSQL bodies live entirely inside #ifdef SQL
// (rdbms-if.c:40-303) and #ifdef POSTGRES (:304-384). The
// reference build config (H-018, the mumps-sqlite3 tree this
// arm translates) defines neither: config.h carries
// the "undef HAVE_POSTGRESQL" comment pair and no SQL define (the
// embedded engine rides SQLITE instead), so the LIVE body is
// the vendor's own else-arm: "Function is empty and returns 0
// if both SQL and POSTGRES are 0" (rdbms-if.c:386). Both dead
// arms stay documentary ledgers only - H-018 empty arm, the
// wlock.h wlock precedent.
//
// H-004 note: the Stubs.sql seat (6 INT-arg dispatcher of a
// DIFFERENT C symbol - btree.h's SQL-side btree interface) is
// untouched; the two C symbols share only the lexeme; this unit
// owns the 5-char* rdbms-if.c entry (the arm's same-name
// H-015 overload-family rule; distinct Kotlin signatures).
// extern int ierr (rdbms-if.c:76) is the landed CompilerState
// / Msv ierr seat line - cross-referenced, not duplicated.
// All derived from the pristine vendor source this session
// (sha256 621deb...ffdbe).

fun sql(DataSourceName: String, User: String, Password: String,
        Command: String, MumpsArray: String): Int {
    // #ifdef SQL (rdbms-if.c:40-303) - EXCLUDED arm: the
    // WindowsXX SQL ODBC section (SQLAllocEnv/SQLAllocConnect/
    // SQLConnect/SQLExecDirect walk over HENV/HDBC/HSTMT with
    // the x[90] fetch array dropping each row through to the
    // MumpsArray global at column 1..cols; SQLError banners;
    // returns r = number of rows).
    //
    // #ifdef POSTGRES (rdbms-if.c:304-384) - EXCLUDED arm: the
    // PostgreSQL libpq-fe.h interface (PQsetdbLogin/PQexec walk
    // folding result rows to ^Test(1)..^Test(n) MUMPS-side
    // nodes; returns nt rows). The autoconfigure gates
    // (#if ALLOW_POSTGRES==0 #undef POSTGRES, lines 49-57) are
    // likewise documentary: the reference config answers both
    // sides dead.
    
    return 0 // rdbms-if.c:386 - both SQL and POSTGRES are 0
}
