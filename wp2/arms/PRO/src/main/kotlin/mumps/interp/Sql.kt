package mumps.interp

/**
 * PRO-arm translation of interp-sql.cpp::sql (roster row 254,
 * batch 9; units.csv: loc 121, comment_ratio 0.124,
 * pointer_marks 13, control_marks 35, score 168.6, rank 319).
 * Translated independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), reference
 * source lines 935-1133 (prototype include/mumpsc/btree.h:37).
 * RESOLVES the InterpStubs sql stub (the SqlBtree.kt u217 call
 * sites and the Zfcn.kt/ZfcnBifs.kt arms bind unchanged on the
 * 6-arg signature; the char* parameters stay nullable for the
 * vendor NULL-argument calls, u94 fcn note).
 *
 * sql(op, svPtr, DataSourceName, Command, rslt, TOK): the
 * SQLITE database engine entry - CLOSE closes (1 if not open),
 * OPEN opens (OK if already open), the default-open block
 * verifies "mumps.sqlite" exists (banner + sigint(100) arm),
 * sets the journal_mode OFF option and a 20s busy timeout,
 * the command ride execs through the callback, and the return
 * cascade walks the CODE ladder (GLOBAL/STORE/BEGIN/COMMIT/
 * SAVEPOINT/ROLLBACK/PRAGMA).
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h004 file_scope_static : static open, static long long
 *    access, static sqlite3 *ppDb ride the SqlState object
 *    (one owner object per C translation unit, u158 precedent;
 *    the access counter is the vendor's dead commented-out
 *    contention throttle - kept as the carrier field, unused).
 *  - h009 stdlib_function_semantics: the sqlite3 embedded C API
 *    (sqlite3_open_v2/sqlite3_exec/sqlite3_close/sqlite3_busy_
 *    timeout/sqlite3_free) folds onto java.sql.DriverManager
 *    with the "jdbc:sqlite:mumps.sqlite" URL - the JVM build
 *    carries NO sqlite JDBC driver, so the fold degrades EXACTLY
 *    like a failed sqlite3_open: the option error banner and
 *    BAD (journaled carrier divergence; the code-path structure
 *    is byte-faithful). The while(SQLITE_BUSY) retry loops fold
 *    onto the single driver attempt (a busy retry has no analog
 *    when no driver binds).
 *  - h012 unsigned_byte_domain: rslt/Command/Col_Names are
 *    NUL-terminated byte domains.
 *  - h013 dead_store        : the if(0) create-table arm (the
 *    vendor's own permanent if(0)) kept as a comment, not
 *    rendered; the two commented-out OPEN-only blocks kept as
 *    comments; max/tmp/nt/nf/i/j pronouns dead in this body -
 *    dropped with notes.
 *  - h014 out_of_scope_call : rsltCallback (RsltCallback.kt u200
 *    real seat - the driver-bound exec loop feeds it);
 *    SqlCallbackState.callBack (vendor 886 static); sigint
 *    (Sigint.kt:136 real seat); strcpyC/cstringAt/strlenC
 *    (CstringOps seats).
 *  - h015 grammar_collision : new top-level seat named sql -
 *    replaces the removed InterpStubs stub one-for-one (same
 *    signature); the arg5/arg6 stub parameter names renamed to
 *    the vendor rslt/TOK at the owner seat (roster note).
 *  - h017 naming           : the CODE codes keep defines.h
 *    names as file-private consts (OK 1 / BAD 0 are the vendor
 *    file-local defines at interp-sql.cpp:38-39).
 *  - h018 preprocessor_conditional: the vendor body sits under
 *    no conditional; the surrounding family's #define SQLITE
 *    (line 34) is the LIVE anchor (SqlMisc.kt u228) - the
 *    sqlite paths render as LIVE code.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The not-found arm does NOT return: after the banner and
 *    sigint(100) the vendor runs on (fclose on NULL is vendor
 *    UB, quantized by dropping the close on the null File) into
 *    the open, which fails and returns BAD.
 *  - The err_msg arm prints sql3 + err_msg + Command, zeroes
 *    rslt[0], and returns BAD before the CODE cascade.
 *  - GLOBAL without a callback zeroes rslt[0] and returns OK.
 *  - Every CODE ladder arm is a flat ==-0 test - no else arm
 *    after PRAGMA: the fall-through walk (! call_back -> BAD,
 *    0 -> OK, BAD) kept verbatim.
 */

private const val OK_SQL = 1 // vendor interp-sql.cpp:38
private const val BAD = 0 // vendor interp-sql.cpp:39
private const val RETRIEVE_S = 0 // defines.h:83
private const val STORE_S = 1 // defines.h:84
private const val CLOSE_S = 4 // defines.h:87
private const val GLOBAL_S = 23 // defines.h:93
private const val OPEN_S = 206 // defines.h:169
private const val BEGIN_TRANSACTION = 25 // defines.h:95
private const val COMMIT_TRANSACTION = 26 // defines.h:96
private const val ROLLBACK_TRANSACTION = 27 // defines.h:97
private const val SAVEPOINT = 28 // defines.h:98
private const val PRAGMA = 29 // defines.h:99

private const val NUL_S = '\u0000'

/** Carrier for the vendor statics (h004: open, access, ppDb). */
private object SqlState {
    var open = 0 // vendor: static int open
    var access = 0L // vendor: static long long access (dead throttle)
    var conn: java.sql.Connection? = null // vendor: static sqlite3 *ppDb
}

fun sql(op: Int, svPtr: Msv, DataSourceName: CharArray?, Command: CharArray?,
        rslt: CharArray?, TOK: CharArray?): Int {
    // vendor: strcpy(svPtr->Col_Names, "");
    var w = 0
    svPtr.Col_Names[w] = NUL_S

    if (op == CLOSE_S) { // vendor: if (CODE == CLOSE)
        if (SqlState.open == 0) return 1
        svPtr.ConOpen = 0
        SqlState.open = 0
        try {
            SqlState.conn?.close() // vendor: sqlite3_close(ppDb)
            return OK_SQL // if (...) == SQLITE_OK return OK
        } catch (e: java.sql.SQLException) {
            System.err.print("*** Sql database close error\n")
        }
    }

    if (op == OPEN_S) { // vendor: if (CODE == OPEN)
        if (SqlState.open != 0) return OK_SQL
    }

    // -------------------- default open --------------------
    if (SqlState.open == 0) { // vendor: if (!open) database is not open
        // vendor: f1 = fopen("mumps.sqlite","r"); test if exists
        val f1 = java.io.File("mumps.sqlite")
        if (!f1.exists()) {
            System.err.print("\n\n*** Sqlite3 database \"mumps.sqlite\" not found.\n")
            System.err.print("*** Database operations not available.\n")
            System.err.print("*** Please run mumps-sql-db-create.script\n")
            sigint(100)
            // vendor runs on (fclose(f1) omitted on the null File,
            // quantized - UB in C, noted)
        }

        // vendor: if (0) { create-table + index execs } - the
        // vendor's OWN permanent if(0) - kept as this comment,
        // not rendered (h013).
        //
        // vendor: else { sql3 = sqlite3_open_v2("mumps.sqlite",
        //          &ppDb, SQLITE_OPEN_READWRITE, NULL); }
        // h009 fold: java.sql.DriverManager - absent a driver
        // the open degrades to the option-error path below.
        val opened = try {
            SqlState.conn = java.sql.DriverManager.getConnection(
                "jdbc:sqlite:mumps.sqlite"
            )
            0
        } catch (e: java.sql.SQLException) {
            SqlState.conn = null
            1
        }

        // vendor: while ((sql3 = sqlite3_exec(ppDb,
        //          "pragma main.journal_mode = OFF;", ...)) == SQLITE_BUSY);
        // (the pragma rides CONNECTION INIT in the fold; the
        // busy-retry loop has no driver analog - h009 note)
        if (opened != 0) {
            System.err.print("\n\n*** Sqlite3 database open error while setting options.\n")
            System.err.print("*** Database operations not available.\n")
            return BAD
        }

        // vendor: sqlite3_busy_timeout(ppDb, 20000); busy handler
        // (driver-owned in the fold)

        SqlState.open = 1
        svPtr.tpx = SqlState.open.toLong()
        if (op == OPEN_S) return OK_SQL
    }
    // -------------------- end default open --------------------

    val cmd = if (Command != null) cstringAt(Command) else ""
    if (cmd.isEmpty()) return SqlState.open // strlen(Command)==0

    SqlCallbackState.callBack = 0 // vendor: call_back = 0;

    // vendor: while (sql3 = sqlite3_exec(ppDb, Command,
    //          rslt_callback, (void*)rslt, &err_msg) == SQLITE_BUSY);
    var sql3: Int
    var errMsg: String? = null
    val target = rslt ?: CharArray(0)
    try {
        val conn = SqlState.conn ?: throw java.sql.SQLException("no connection")
        val st = conn.createStatement()
        val hasRows = st.execute(cmd)
        sql3 = 0
        if (hasRows) {
            val rs = st.resultSet
            val md = rs.metaData
            val n = md.columnCount
            // (md is read only for the column count - the vendor
            // callback receives argc from the C engine; the
            // Col_Names buffer stays as the strcpy("") above)
            while (rs.next()) {
                val colVals = arrayOfNulls<CharArray>(n)
                for (c in 0 until n) {
                    val v = rs.getString(c + 1)
                    colVals[c] = if (v != null) {
                        val cv = CharArray(v.length + 1)
                        for (k in v.indices) cv[k] = v[k]
                        cv[v.length] = NUL_S
                        cv
                    } else null
                }
                rsltCallback(target, n, colVals, arrayOfNulls(n))
            }
        }
        st.close()
    } catch (e: java.sql.SQLException) {
        sql3 = 1
        errMsg = e.message // vendor: &err_msg
    }

    if (errMsg != null) { // vendor: if (err_msg)
        System.err.print(
            "*** Sqlite Error msg: " + sql3 + " " + errMsg + "\n*** " + cmd + "\n"
        ) // vendor: fprintf(stderr, ... %d %s\n*** %s\n, ...)
        // vendor: sqlite3_free(err_msg); - GC
        if (target.isNotEmpty()) target[0] = NUL_S // vendor: *rslt = 0;
        return BAD
    }

    if (sql3 == 0 && op == GLOBAL_S && SqlCallbackState.callBack == 0) {
        if (target.isNotEmpty()) target[0] = NUL_S // vendor: rslt[0]=0
        return OK_SQL
    }

    if (sql3 == 0 && op == STORE_S) return OK_SQL

    if (op == BEGIN_TRANSACTION) return if (sql3 == 0) OK_SQL else BAD
    if (op == COMMIT_TRANSACTION) return if (sql3 == 0) OK_SQL else BAD
    if (op == SAVEPOINT) return if (sql3 == 0) OK_SQL else BAD
    if (op == ROLLBACK_TRANSACTION) return if (sql3 == 0) OK_SQL else BAD
    if (op == PRAGMA) return if (sql3 == 0) OK_SQL else BAD

    if (SqlCallbackState.callBack == 0) return BAD // vendor: if (! call_back)

    if (sql3 == 0) return OK_SQL

    return BAD
}
