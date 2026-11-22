package both

// Unit 153 (BOTH B5 T3, complex): rdbms-if.c::sql (209 LOC).
// Batch_plan ordinal 153: complex, LOC 209, score 21.6. The
// database bridge entry: five string arguments (DataSourceName,
// User, Password, Command, MumpsArray) and an int row count.
//
// Vendor resolution (h018): the whole file body is a three-way
// preprocessor switch and the reference build config defines
// NEITHER SQL NOR POSTGRES (rdbms-if.c does not appear in the
// reference Makefile object set and the CFLAGS define neither
// macro), so the LIVE arm is the #else tail: an empty function
// that returns 0. The two excluded arms (rendered below as a
// documented comment ledger, not code):
//
//   #ifdef SQL - the WindowsXX ODBC section: SQLAllocEnv/
//   SQLAllocConnect/SQLConnect/SQLSetConnectOption(SQL_AUTOCOMMIT)/
//   SQLAllocStmt/SQLExecDirect, the diagnostic family printing
//   against LineNumber with SQLError into a 160-byte err buffer,
//   the no-array-name commit-and-skip path (SQLTransact
//   SQL_COMMIT then goto exit), SQLNumResultCols plus per-column
//   512-byte malloc + SQLBindCol binds, the fetch loop building
//   byte-1-coded element numbers onto the MumpsArray global
//   name (strmove gbl, sprintf counter with the \x00 NUL fold,
//   gbl[ii]=1 coded delimiters, the 500-byte row overflow
//   diagnostic, the DELIM-joined column concatenation) storing
//   each row with Mglobal(1,...) i.e. RETRIEVE-side store, then
//   bd[1]=0, return r (row count). Exit tail: SQLFreeStmt
//   SQL_DROP/SQLDisconnect/SQLFreeConnect/SQLFreeEnv plus the
//   conditional frees.
//
//   #ifdef POSTGRES - the PostgreSQL section: PQsetdbLogin on
//   the DataSourceName as dbname, PQexec on the Command, the
//   PGRES_TUPLES_OK arm copying PQntuples x PQnfields rows
//   into the byte-1-coded MumpsArray elements (same coded-
//   delimiter build as the ODBC arm) with PQgetvalue + DELIM
//   joins, PQfinish, ierr=0, return nt.
//
// Both excluded arms ride vendor-only ODBC/libpq surface that
// the reference binary never links; they stay in this ledger
// (the excluded-arm fold is journaled, same shape as the mumps
// main database-banner resolution).
//
// BOTH consults (find_rule before render, events logged):
//   h018 preprocessor_conditional - REUSED (the deciding rule):
//     the reference build config was re-verified for this unit
//     (Makefile object set + CFLAGS) before the arm choice;
//     both SQL and POSTGRES are exclusion-verified, so the
//     render keeps only the #else contract.
//   h017 naming - hit: sql and the five vendor argument names
//     keep their shapes; no multiword identifiers occur in the
//     live arm.
//   h007 char_buffer - reused: the excluded arms' buffer work
//     is ledgered, not seated, so no runtime seat exists in the
//     live render.
internal fun sql(
    DataSourceName: String,
    User: String,
    Password: String,
    Command: String,
    MumpsArray: String,
): Int {
    // #else arm: the function is empty and returns 0 when both
    // SQL and POSTGRES are 0 (the reference build config).
    return 0
}
