package both

// Unit 271 (BOTH B9, core): interp-sql.cpp::sql (121 LOC).
// Batch_plan ordinal 271: core, LOC 121. The SQLITE connection
// trunk of the reference tree (the "mumps.sqlite" database; the
// symbol the Btree arms have called through the 6-arg stub
// since unit 216). The vendor body is the connection state
// machine over the raw sqlite3 C API (open/close/exec/
// busy_timeout plus the rslt_callback trampoline landed at
// unit 199): adjudged per h018 - there is NO JVM sqlite3
// carrier in the reference compile-only study build (pure
// kotlinc/gradle, zero external dependencies; the sqlite-jdbc
// family is out of scope), so every sqlite3_* call site folds
// to its documented inert arm riding the FAILURE side
// (SQLITE_NO_CARRIER -1; the wlock precedent, the DOC-arm
// SqlIsc adjudication of the same unit).
//
// What stays FULLY live: the mumps.sqlite existence probe
// (fopen("r") folds to the java.io.File test), the missing-
// database banner trio + sigint(100) (the landed unit-190
// seat), the Col_Names clear, the ConOpen/tpx carriers, the
// SqlRuntime callBack reset (the unit-199 holder), and the
// full CODE-family decision tail (the defines.h sql codes
// 25-29 BEGIN/COMMIT/SAVEPOINT/ROLLBACK/PRAGMA family - the
// Zfcn dispatch values - plus GLOBAL 23, STORE 1, CLOSE 4,
// OPEN 206, and the OK 1 / BAD 0 file-local defines at
// interp-sql.cpp:38-39).
//
// The vendor "if (0) { MEMORY-mode open ladder } else {
// READWRITE open }" fossil is kept as the comment ledger (the
// MEMORY arm is vendor-dead, h013 kept-code; the create-table
// mumps/a1..a10 and index MI exec texts preserved verbatim
// inside it). The static access counter (the commented
// contention-throttle block, access++/usleep) is a dead-store
// fossil, h013, ledger-only but seated (SqlRuntime.access, the
// :54 verified fold). max/i/j/nt/nf are scratch locals unused
// by the live arms (h013, ledger). TOK rides the signature
// unused (h013 dead-param; the XTAB/XCMD/XNULL/X01 tails are
// call-site conventions per the Btree X* seats). The
// sqlite3_close non-OK arm prints the close-error banner and
// FALLS THROUGH to the reopen family (the vendor has no
// return on that path - the fall-through quirk kept and
// ledgered).
//
// Quirks kept (ledgered):
//   - the CLOSE arm returns 1 (not OK) when the database was
//     never open; kept verbatim.
//   - the OPEN arm is a pure no-op pass-through when already
//     open (returns OK without touching state).
//   - the default-open family sets open=1/tpx and returns OK
//     ONLY on the live vendor arms; on the h018 fold the
//     pragma ladder reads its error side and the family
//     returns BAD before those stores (unreachable, ledger).
//   - an empty Command returns the raw open flag (1/0), not
//     OK/BAD. Kept.
//   - the GLOBAL arm's no-callback success writes rslt[0]=0
//     and returns OK. Kept.
//   - the tail ladder is kept verbatim: call_back==0 -> BAD,
//     then sql3==0 -> OK, else BAD (on the fold every
//     exec-arm return is BAD; journaled).
//
// BOTH consults (find_rule before render, events logged):
//   h001 io_stream_param - reused: the stderr fprintfs ride
//     System.err (the vendor stderr channel kept); the fopen
//     probe folds to java.io.File (the read-mode existence
//     test).
//   h004 include_only - reused: the sqlite3.h include and the
//     SQLITE/CSTR define block are the unit-227 ledger (no
//     seats).
//   h007 char_buffer - reused: Col_Names rides the Msv
//     CharArray (writeC clear); Command/rslt keep the
//     CharArray contract of the Btree call sites.
//   h013 dead_store - hit: access/max/i/j/nt/nf/TOK ledgered
//     above.
//   h014 out_of_scope_call - reused: this render RELEASES the
//     Btree.kt 6-arg sql stub (release-at-render); sigint
//     rides the landed unit-190 seat; rslt_callback rides the
//     unit-199 render (the exec trampoline, inert on the
//     fold).
//   h015 grammar_collision - reused: ASCII only, no backticks,
//     no raw comment dollars, trailing newline.
//   h017 naming - hit: the vendor lexeme sql keeps verbatim
//     (the unit-153 rdbms-if.c::sql render is the 5-arg
//     String overload at Sql.kt; the same-name overload family
//     resolves by arity at the call sites). Journaled.
//   h018 preprocessor_conditional - REUSED (the deciding
//     rule): the SQLITE arm is live (the unit-227 define
//     ledger; the reference build config), and the sqlite3 C
//     API itself has no JVM carrier in the compile-only study
//     build - the runtime API folds inert (SQLITE_NO_CARRIER
//     -1 rides the failure side; the vendor SQLITE_OK is 0).
//   h019 class_member_carrier - reused: the TU statics open
//     (:53) and access (:54) seat in the SqlRuntime object
//     (the unit-199 carrier; callBack the reset seat).
//   h020 pointer_handle - reused: static sqlite3 *ppDb is the
//     inert-arm ledger (no handle exists to carry; the SQLITE
//     handle domain has no JVM seat).

// sqlite3 fold value: the inert-arm adjudication result of
// every sqlite3_* site in this render (no JVM carrier; the
// vendor SQLITE_OK is 0, so the fold rides the FAILURE side)
private const val SQLITE_NO_CARRIER = -1

private const val OK_SQL = 1 // interp-sql.cpp:38
private const val BAD_SQL = 0 // interp-sql.cpp:39
private const val GLOBAL_SQL = 23 // defines.h:93
private const val OPEN_SQL = 206 // defines.h:169 (CodedOpen byte)
private const val BEGIN_TRANSACTION_SQL = 25 // defines.h sql codes
private const val COMMIT_TRANSACTION_SQL = 26 // (ZC_ twins, BifsZfcn)
private const val ROLLBACK_TRANSACTION_SQL = 27
private const val SAVEPOINT_SQL = 28
private const val PRAGMA_SQL = 29

// int sql(int CODE, struct MSV * svPtr, char * DataSourceName,
// char * Command, char * rslt, const char * TOK)
internal fun sql(
    CODE: Int, svPtr: Msv, DataSourceName: String, Command: CharArray,
    rslt: CharArray, TOK: String,
): Int {
    // static sqlite3 *ppDb: no JVM carrier (the h018 fold; the
    // handle is the inert-arm ledger above)

    writeC(svPtr.Col_Names, "") // strcpy(svPtr->Col_Names, "")

    // int max; i, j, nt = 0, nf = 0; char tmp[256]; - scratch
    // locals unused by the live arms (h013, ledger)

    if (CODE == CLOSE) {
        if (SqlRuntime.open == 0) return 1
        svPtr.ConOpen = 0
        SqlRuntime.open = 0
        // if (sqlite3_close(ppDb) == SQLITE_OK) return OK;
        // - the fold: no handle to close; the vendor arms START
        // from the non-OK side (the FALL-THROUGH quirk: the
        // vendor prints the banner and drops into the reopen
        // family below - kept verbatim)
        System.err.print("*** Sql database close error\n")
        // (fall-through, no return on this path)
    }

    if (CODE == OPEN_SQL) {
        if (SqlRuntime.open != 0) return OK_SQL
    }

    // default open
    if (SqlRuntime.open == 0) { // database is not open

        // FILE *f1 = fopen("mumps.sqlite","r") - the read-mode
        // probe folds to the existence test (h001)
        val f1 = java.io.File("mumps.sqlite")
        if (!f1.exists()) { // f1 == NULL
            System.err.print("\n\n*** Sqlite3 database \"mumps.sqlite\" not found.\n")
            System.err.print("*** Database operations not available.\n")
            System.err.print("*** Please run mumps-sql-db-create.script\n")
            sigint(100)
        }

        // fclose(f1) - the probe leaf folds (GC no-op)

        // if (0) {
        //   sql3 = sqlite3_open_v2("mumps.sqlite", &ppDb,
        //       SQLITE_OPEN_READWRITE | SQLITE_OPEN_CREATE |
        //       SQLITE_OPEN_MEMORY, NULL);
        //   while ((sqlite3_exec(ppDb,
        //       "create table mumps (a1 varchar(256), a2 varchar(256),
        //       a3 varchar(256), a4 varchar(256), a5 varchar(256),
        //       a6 varchar(256), a7 varchar(256), a8 varchar(256),
        //       a9 varchar(256), a10 varchar(512));",
        //       rslt_callback, (void *)rslt, NULL)) == SQLITE_BUSY);
        //   sql3 = sqlite3_exec(ppDb,
        //       "create index MI on mumps (a1, a2);",
        //       rslt_callback, (void *)rslt, NULL);
        //   } - the vendor-dead MEMORY-mode ladder (the literal
        //   if (0) fossil, h013 kept-code; bytes preserved)
        // else {
        //   sql3 = sqlite3_open_v2("mumps.sqlite", &ppDb,
        //       SQLITE_OPEN_READWRITE, NULL);
        //   } - the live vendor open
        //
        // h018 FOLD: no JVM sqlite3 carrier - the open fails
        // inert; the pragma ladder below then reads its error
        // side uniformly

        // while ((sql3 = sqlite3_exec(ppDb,
        //     "pragma main.journal_mode = OFF;",
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

    if (cStringOf(Command).isEmpty()) return SqlRuntime.open

    SqlRuntime.callBack = 0 // static int call_back = 0 - :894

    // int sql3; (the commented contention blocks and
    // busy-detection banners kept verbatim as the vendor
    // comment ledger, h013:
    // - access++; if (access % 1000 == 0) usleep(100000);
    //   the dead-store throttle
    // - char *err_msg = NULL; sqlite3_exec will not return
    //   until any and all callbacks have been executed
    // - if (sql3 == SQLITE_BUSY) printf busy banner
    // )

    // while (sql3 = sqlite3_exec(ppDb, Command, rslt_callback,
    //     (void *)rslt, &err_msg) == SQLITE_BUSY);
    // - the h018 fold: the exec surfaces the failure side
    val sql3 = SQLITE_NO_CARRIER

    // if (err_msg) { stderr "*** Sqlite Error msg: %d %s\n***
    // %s\n"; sqlite3_free(err_msg); *rslt = 0; return BAD; }
    // - the fold runs with no message (err_msg stays NULL);
    // ledger only

    if (sql3 == 0 && CODE == GLOBAL_SQL && SqlRuntime.callBack == 0) {
        rslt[0] = '\u0000'
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

    if (SqlRuntime.callBack == 0) return BAD_SQL

    if (sql3 == 0) return OK_SQL

    return BAD_SQL
}
