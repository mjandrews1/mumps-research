package mumps.mdh

/**
 * PRO-arm translation of rdbms-if.c::sql (B5 unit 125, rank 276,
 * complex stratum, loc 209, units.csv row 126). Translated
 * independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), vendor lines
 * 81-388 (the whole function body).
 *
 * IDENTITY NOTE (correction to the B4 unit-115 roster): this is the
 * RDBMS-interface sql(char-star DataSourceName, char-star User,
 * char-star Password, char-star Command, char-star MumpsArray) - NOT
 * the interp-sql.cpp::sql(op, svPtr, ...) that the InterpStubs
 * owner-named stub carries (batch-9 unit, prototype
 * include/mumpsc/btree.h:37, called by the u94 fcn dispatcher).
 * The u115 roster conflated the two; this render lands in the mdh
 * package so there is no Kotlin collision with mumps.interp.sql.
 *
 * THE WHOLE FUNCTION IS A PREPROCESSOR TRICHOTOMY (h018):
 *  - #ifdef SQL        : the Windows ODBC interface (SQLAllocEnv/
 *    SQLConnect/SQLExecDirect/SQLFetch loop writing result rows
 *    into the named Mumps global, one row per subscript, columns
 *    joined with DELIM);
 *  - #ifdef POSTGRES   : the PostgreSQL interface (PQsetdbLogin/
 *    PQexec/PQgetvalue loop, same global-array writeback, ierr=0,
 *    returns nt);
 *  - #else             : return 0 - "Function is empty and returns
 *    0 if both SQL and POSTGRES are 0".
 * The reference build config (vendor build-mumps.script defaults)
 * defines NEITHER SQL NOR POSTGRES - consistent with the u115 residue
 * finding (no sql.h/sqlext.h, no libpq-fe.h includes) - so the
 * resolved arm is the #else body below; the excluded arms are
 * documented as comments per the h018 action (u40/u41 precedent).
 *
 * Excluded-arm content (roster notes, not translated code):
 *  - ODBC arm (vendor 85-280): connects and binds cols via the ODBC
 *    C API; result rows land in Mglobal(1, gbl, val, svPtr) with the
 *    coded-delimiter subscripts gbl = MumpsArray + 0x01 + row number
 *    + 0x01; vendor QUIRKS kept on record: the ODBC bind uses buffer
 *    length 50 into 512-byte mallocs (x[90] unbounded against cols);
 *    sprintf(ct, percent-d + embedded NUL, tpl) embeds a NUL in the
 *    format string; the row-length guard reads strlen(gbl + j) with
 *    j left at cols from the bind loop (a past-the-content read);
 *    the exit path sets bd[1]=0 and frees x[j] only when cols > 0;
 *    the goto exit (empty-array fast path after SQLTransact COMMIT)
 *    is a forward goto out of the row loop.
 *  - POSTGRES arm (vendor 282-360): same writeback shape over
 *    PQntuples/PQnfields; sets ierr=0 (the Msv.ierr slot per the
 *    u115 roster) and returns nt; the vendor comment block documents
 *    the dollar-zodbc contract (rows into successive subscripts,
 *    columns DELIM-joined).
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h014 out_of_scope_call : nothing - the resolved arm has no
 *    calls; the excluded arms' C API edges stay on record above.
 *  - h015 grammar_collision : the vendor parameters are all
 *    String carriers (char-star collapse); pre-push scan clean; the
 *    top-level name sql is new in the mdh package (no conflict with
 *    mumps.interp.sql - see identity note).
 *  - h017 naming            : sql keeps the vendor spelling
 *    (lowercase, matching the vendor function name).
 *  - h018 preprocessor_conditional: first exercise of the SQL /
 *    POSTGRES trichotomy; resolves to the empty arm under the
 *    reference config (MULTI_PRECISION/BIGFLOAT family precedent,
 *    rules_v0.pl h018; reuses the B1-captured rule).
 */

// The reference build resolves BOTH the SQL and POSTGRES arms out
// (see KDoc): the function is empty and returns 0.
fun sql(
    DataSourceName: String,
    User: String,
    Password: String,
    Command: String,
    MumpsArray: String
): Int {
    return 0 // vendor 366: the preprocessor-else arm
}
