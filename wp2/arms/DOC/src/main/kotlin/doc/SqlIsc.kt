package doc

// Unit 274 (B9 tranche 3): interp-sql.cpp::sql (batch 9, complex
// stratum; units.csv row 254: loc 121, score 87.1, rank 268;
// physLOC 121 physically verified this session) - the SQLITE
// connection trunk of the reference tree (the "mumps.sqlite"
// database; the symbol the btreeSql arms have called through
// the Stubs.sql 6-arg seat since unit 202). The vendor body is
// the connection state machine over the raw sqlite3 C API
// (open/close/exec/busy_timeout plus the rslt_callback trampoline
// landed at RsltCallback.kt): the arm adjudges that API per
// H-018 - there is NO JVM sqlite3 carrier in the reference
// compile-only study build (pure kotlinc/gradle, zero external
// dependencies; the sqlite-jdbc family is out of scope), so
// every sqlite3_* call site folds to its documented inert arm
// below (the wlock.h wlock precedent, the B1 domain ruling).
// What stays FULLY live: the mumps.sqlite existence probe
// (fopen("r") folds to the java.io.File test), the missing-
// database banner trio + sigint(100) (the landed unit-211
// render, exitProcess(1) terminal), the svPtr->Col_Names clear,
// the svPtr ConOpen/tpx carriers, the SqlCallbackState
// call_back reset (the unit-235 holder, H-004), and the full
// CODE-family decision tail (the defines.h sql codes 25-29
// BEGIN/COMMIT/SAVEPOINT/ROLLBACK/PRAGMA family - the Zfcn
// dispatch values cited - plus GLOBAL 23, STORE 1, CLOSE 4,
// OPEN 206, the OK 1 / BAD 0 file-local defines at
// interp-sql.cpp:38-39, the OK_BT precedent).
//
// The vendor "if (0) { MEMORY-mode open ladder } else {
// READWRITE open }" fossil is kept as the comment ledger (the
// MEMORY arm is vendor-dead, H-013 kept-code; the create-table
// mumps/a1..a10 and index MI exec texts preserved verbatim
// inside it). The static access counter (the commented
// contention-throttle block, access++ / usleep) is a dead
// store fossil, H-013, ledger-only. The commented "uncomment
// to reduce contention" and busy-detection banners keep
// verbatim. max/i/j/nt/nf are scratch locals unused by the
// live arms (H-013, ledger). TOK rides the signature unused
// (H-013 dead-param; the XTAB/XCMD tails are call-site
// conventions per the InterpSqlBtree X*_BT seats). The
// sqlite3_close non-OK arm prints the close-error banner and
// FALLS THROUGH to the reopen family (the vendor has no
// return on that path - the fall-through quirk kept and
// ledgered). Consult row logged 2026-11-22T05:31:00Z (H-018
// hit-adapt, the mumps-sqlite3 build-config ruling applied
// to the runtime API). Naming per H-017: the vendor lexeme
// sql keeps verbatim (no top-level same-signature twin; the
// unit-153 rdbms-if.c::sql render is the String-arg overload
// at Sql.kt; the H-015 same-name overload family).

// function-static keep (H-004, the InterpSqlMiscState/
// SqlCallbackState precedent)
private object SqlIscState {
    // interp-sql.cpp:53 static int open
    var open = 0
    // :54 static long long access - dead store (the commented
    // contention-throttle block is its only reader); ledger-only
    // AND rendered for the seat: the JVM long carrier (H-012).
    var access = 0L
}

// sqlite3_ok fold value: the inert-arm adjudication result of
// every sqlite3_* site in this render (no JVM carrier; the
// vendor SQLITE_OK is 0, so the fold rides the FAILURE side)
private const val SQLITE_NO_CARRIER = -1

private const val OK_SQL = 1 // interp-sql.cpp:38
private const val BAD_SQL = 0 // interp-sql.cpp:39
private const val CLOSE_SQL = 4 // defines.h:85 CLOSE (Mglobal close code)
private const val GLOBAL_SQL = 23 // defines.h:93
private const val OPEN_SQL = 206 // defines.h:169
private const val BEGIN_TRANSACTION_SQL = 25 // defines.h sql codes (Zfcn twin)
private const val COMMIT_TRANSACTION_SQL = 26
private const val ROLLBACK_TRANSACTION_SQL = 27
private const val SAVEPOINT_SQL = 28
private const val PRAGMA_SQL = 29

fun sql(
    CODE: Int,
    svPtr: Msv,
    DataSourceName: CharArray,
    Command: CharArray,
    rslt: CharArray,
    TOK: String
): Int {
    // static sqlite3 *ppDb: no JVM carrier (the H-018 fold;
    // the handle is the inert-arm ledger below)

    CstringOps.strcpyS(svPtr.Col_Names, "")

    // int max; i, j, nt = 0, nf = 0; - scratch locals unused by
    // the live arms (H-013, ledger)
    // char tmp[256] - folded into the arms that need it

    if (CODE == CLOSE_SQL) {
        if (SqlIscState.open == 0) return 1
        svPtr.ConOpen = 0
        SqlIscState.open = 0
        // if (sqlite3_close(ppDb) == SQLITE_OK) return OK;
        // - the fold: no handle to close; the vendor arms
        // START from the non-OK side (the FALL-THROUGH quirk:
        // the vendor prints the banner and drops into the
        // reopen family below - kept verbatim)
        System.err.print("*** Sql database close error\n")
        // (fall-through, no return on this path)
    }

    if (CODE == OPEN_SQL) {
        if (SqlIscState.open != 0) return OK_SQL
    }

    // default open
    if (SqlIscState.open == 0) { // database is not open

        // FILE *f1; f1 = fopen(\"mumps.sqlite\", \"r\") - the
        // read-mode probe folds to the existence test (H-001)
        val f1 = java.io.File("mumps.sqlite")
        if (!f1.exists()) { // f1 == NULL
            System.err.print("\n\n*** Sqlite3 database \"mumps.sqlite\" not found.\n")
            System.err.print("*** Database operations not available.\n")
            System.err.print("*** Please run mumps-sql-db-create.script\n")
            sigint(100)
        }

        // fclose(f1) - the probe leaf folds (GC no-op)

        // if (0) {
        //   sql3 = sqlite3_open_v2(\"mumps.sqlite\", &ppDb,
        //       SQLITE_OPEN_READWRITE | SQLITE_OPEN_CREATE |
        //       SQLITE_OPEN_MEMORY, NULL);
        //   while ((sqlite3_exec(ppDb,
        //       \"create table mumps (a1 varchar(256), a2 varchar(256),
        //       a3 varchar(256), a4 varchar(256), a5 varchar(256),
        //       a6 varchar(256), a7 varchar(256), a8 varchar(256),
        //       a9 varchar(256), a10 varchar(512));\",
        //       rslt_callback, (void *)rslt, NULL)) == SQLITE_BUSY);
        //   sql3 = sqlite3_exec(ppDb,
        //       \"create index MI on mumps (a1, a2);\",
        //       rslt_callback, (void *)rslt, NULL);
        //   } - the vendor-dead MEMORY-mode ladder (the literal
        //   if (0) fossil, H-013 kept-code; bytes preserved)
        // else {
        //   sql3 = sqlite3_open_v2(\u0022mumps.sqlite\u0022, &ppDb,
        //       SQLITE_OPEN_READWRITE, NULL);
        //   } - the live vendor open
        //
        // H-018 FOLD: no JVM sqlite3 carrier - the open fails
        // inert; the pragma ladder below then reads its error
        // side uniformly

        // while ((sql3 = sqlite3_exec(ppDb,
        //     \"pragma main.journal_mode = OFF;\",
        //     rslt_callback, (void *)rslt, NULL)) == SQLITE_BUSY);
        // - the fold resolves the loop immediately (non-OK,
        // non-BUSY)
        val sql3 = SQLITE_NO_CARRIER

        if (sql3 != 0) { // sql3 != SQLITE_OK
            System.err.print(
                "\n\n*** Sqlite3 database open error while setting options.\n"
            )
            System.err.print("*** Database operations not available.\n")
            return BAD_SQL
        }

        // sqlite3_busy_timeout(ppDb, 20000); // busy handler
        // - the fold (no handle); ledger

        // unreachable on the fold (the quirk-keeper notes the
        // LIVE vendor arms below for the ledger):
        // open = 1;
        // svPtr->tpx = open;
        // if (CODE == OPEN ) return OK;
    }

    if (CstringOps.cstring(Command).length == 0) return SqlIscState.open

    SqlCallbackState.call_back = 0 // static :886

    // int sql3; (the commented contention blocks and
    // busy-detection banners kept verbatim as the vendor
    // comment ledger, H-013:
    // - access++; if (access % 1000 == 0) usleep(100000);
    //   the dead-store throttle
    // - char *err_msg = NULL; sqlite3_exec will not return
    //   until any and all callbacks have been executed
    // - if (sql3 == SQLITE_BUSY) printf busy banner
    // )

    // while (sql3 = sqlite3_exec(ppDb, Command, rslt_callback,
    //     (void *)rslt, &err_msg) == SQLITE_BUSY);
    // - the H-018 fold: the exec surfaces the failure side
    val sql3 = SQLITE_NO_CARRIER

    // if (err_msg) { stderr \"*** Sqlite Error msg: %d %s\\n***
    // %s\\n\"; sqlite3_free(err_msg); *rslt = 0; return BAD; }
    // - the fold runs with no message (err_msg stays NULL);
    // ledger only

    if (sql3 == 0 && CODE == GLOBAL_SQL && SqlCallbackState.call_back == 0) {
        rslt[0] = NUL
        return OK_SQL
    }

    if (sql3 == 0 && CODE == STORE) return OK_SQL

    if (CODE == BEGIN_TRANSACTION_SQL) {
        return if (sql3 == 0) OK_SQL else BAD_SQL
    }

    if (CODE == COMMIT_TRANSACTION_SQL) {
        return if (sql3 == 0) OK_SQL else BAD_SQL
    }

    if (CODE == SAVEPOINT_SQL) {
        return if (sql3 == 0) OK_SQL else BAD_SQL
    }

    if (CODE == ROLLBACK_TRANSACTION_SQL) {
        return if (sql3 == 0) OK_SQL else BAD_SQL
    }

    if (CODE == PRAGMA_SQL) {
        return if (sql3 == 0) OK_SQL else BAD_SQL
    }

    if (SqlCallbackState.call_back == 0) return BAD_SQL

    if (sql3 == 0) return OK_SQL

    return BAD_SQL
}
