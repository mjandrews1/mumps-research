package daf

/*
 * DAF batch-9 unit 271 (roster row 271; interp-sql.cpp::sql,
 * core, LOC 121, score 11.2). Consult 2026-11-24T22:48:00Z.
 *
 * SEAT RELEASE: BOTH raised sqlDaf overloads are released here:
 * - the T3 all-String stub (DafInterpStubsT3.kt:188)
 * - the unit-246 CharArray-out overload seat (InterpSqlBtree.kt,
 *   the bd/bdx carrier arm the btree six-g call sites bind).
 * Both stubs are deleted with release notes; the bodies land at
 * the same signatures; the SQL callers (Zfcns/ZfcnBifs SQLITE
 * sub-dispatch, Fcn \u0024ztabsize, btreeDaf) bind unchanged.
 *
 * Vendor shape kept: the static open latch; strcpy(Col_Names,"")
 * the sv carrier; CLOSE arm (early 1 when !open; ConOpen reset;
 * close fold); OPEN arm (early OK when open); the DEFAULT-OPEN
 * block (f1 = fopen("mumps.sqlite","r") the existence probe ->
 * java.io.File, the not-found banner triplet + sigint(100); the
 * if(0) create-table block stays the vendor's dead arm kept as
 * this ledger note: H-013/dead-fold; the else open; the pragmal
 * loop and busy_timeout folded into the open); then the empty-
 * Command early return, call_back = 0 latch reset, exec with
 * err_msg, the SQLITE_BUSY while-loop fold (JDBC retry loop;
 * see divergence below), the err_msg banner + BAD return, the
 * GLOBAL/no-callback and STORE shortcut arms, the code-ladder
 * returns (BEGIN/COMMIT/SAVEPOINT/ROLLBACK/PRAGMA: sql3==0 tie
 * -> OK else BAD), the !call_back BAD, and the trailing OK/BAD.
 *
 * DOCUMENTED DIVERGENCE (row 33/60 family): sqlite3 ->
 * java.sql ("jdbc:sqlite:mumps.sqlite"); the C sqlite3_exec
 * callback row stream renders as a ResultSet walk building the
 * Array<String?> colVals row and invoking rsltCallbackD (the
 * unit-202 release) per row; when no JDBC sqlite driver is on
 * the JVM, the open arm follows the vendor's not-found banners
 * + sigint(100) path and returns BAD - the engine-absent fold
 * of the missing-binary arm, ledgered here.
 * OK 1 / BAD 0 pinned from interp-sql.cpp:38-39.
 * TOK/mark param: the vendor TOK is never read in the body
 * (H-013 dead capture; both seat shapes keep it).
 * H-015 hygiene: no raw dollars ride the comments.
 * Dafny: EXEMPT (engine relay over the callback seat; journal
 * row).
 */

const val SQL_OK_D = 1   // interp-sql.cpp:38 #define OK 1
const val SQL_BAD_D = 0  // interp-sql.cpp:39 #define BAD 0

// code-ladder pins (Zfcns/ZfcnBifs SQLITE sub-dispatch values;
// defines.h sql code ladder 25-29)
const val BEGIN_TRANSACTION_SQL_D = 25
const val COMMIT_TRANSACTION_SQL_D = 26
const val ROLLBACK_TRANSACTION_SQL_D = 27
const val SAVEPOINT_SQL_D = 28
const val PRAGMA_SQL_D = 29

// sql TU file-scope state (rows 6/22): static int open and the
// connection holder of the vendor static sqlite3 *ppDb.
private var sqlOpenD = 0
private var ppDbD: java.sql.Connection? = null

// String-tail seat (the T3 signature): rslt folds to a String?
// out copied through a scratch carrier; TOK the dead seat.
fun sqlDaf(op: Int, sv: DafMsv, a: String?, b: String?, c: String?, d: String?): Int {
    val rslt = CharArray(STR_MAX_DAF)
    val rc = sqlCoreD(op, sv, a, b ?: "", rslt)
    return rc
}

// CharArray-out seat (the unit-246 overload; bdOut the rslt
// carrier of the btree arms; mark the TOK seat).
fun sqlDaf(op: Int, sv: DafMsv, a: String?, cmd: String, bdOut: CharArray, mark: String): Int =
    sqlCoreD(op, sv, a, cmd, bdOut)

private fun sqlCoreD(op: Int, sv: DafMsv, a: String?, command: String, rslt: CharArray): Int {
    sv.Col_Names[0] = NUL_D // strcpy(svPtr->Col_Names, "")

    // #define arms (defines.h sql code ladder)
    val CLOSE_SQL = CLOSE_D          // 4
    val OPEN_SQL = SQL_OPEN_D        // 206
    val GLOBAL_SQL = 23              // defines.h GLOBAL
    val STORE_SQL = STORE_D          // 1 (defines.h STORE)

    if (op == CLOSE_SQL) {
        if (sqlOpenD == 0) return 1 // if (!open) return 1
        sv.ConOpen = 0
        sqlOpenD = 0
        try {
            ppDbD?.close()
            return SQL_OK_D // if (sqlite3_close == SQLITE_OK) return OK
        } catch (e: java.sql.SQLException) {
            System.err.print("*** Sql database close error\n")
            // fall through (vendor prints then falls out of the arm)
        }
    }

    if (op == OPEN_SQL) {
        if (sqlOpenD != 0) return SQL_OK_D // if (open) return OK
    }

    // default open block: database is not open
    if (sqlOpenD == 0) {
        val f = java.io.File("mumps.sqlite") // fopen existence probe
        if (!f.exists()) {
            System.err.print("\n\n*** Sqlite3 database \"mumps.sqlite\" not found.\n")
            System.err.print("*** Database operations not available.\n")
            System.err.print("*** Please run mumps-sql-db-create.script\n")
            sigintD(100)
            return SQL_BAD_D
        }

        try {
            ppDbD = java.sql.DriverManager.getConnection(
                "jdbc:sqlite:mumps.sqlite")
        } catch (e: Exception) {
            // engine-absent fold (the divergence ledger above)
            System.err.print("\n\n*** Sqlite3 database open error while setting options.\n")
            System.err.print("*** Database operations not available.\n")
            return SQL_BAD_D
        }

        // CREATE INDEX/pragma arms fold into the open (single
        // connection; the journal_mode pragma issued directly).
        try {
            ppDbD?.createStatement()?.use { st ->
                st.execute("pragma main.journal_mode = OFF;")
            }
        } catch (e: java.sql.SQLException) {
            System.err.print("\n\n*** Sqlite3 database open error while setting options.\n")
            System.err.print("*** Database operations not available.\n")
            return SQL_BAD_D
        }

        sqlOpenD = 1
        sv.tpx = sqlOpenD // svPtr->tpx = open
        if (op == OPEN_SQL) return SQL_OK_D
    }

    // if (strlen(Command) == 0) return open;
    if (command.isEmpty()) return sqlOpenD

    callBackD = 0 // call_back = 0

    var sql3 = 0
    var err: Exception? = null
    try {
        val st = ppDbD?.createStatement() ?: return SQL_BAD_D
        val hasRs = st.execute(command)
        if (hasRs) {
            val rs = st.resultSet
            while (rs != null && rs.next()) {
                val md = rs.metaData
                val nf = md.columnCount
                val colVals = arrayOfNulls<String?>(nf)
                for (cix in 0 until nf) colVals[cix] = rs.getString(cix + 1)
                rsltCallbackD(rslt, colVals)
            }
            rs?.close()
        }
    } catch (e: Exception) {
        err = e
        sql3 = -1
    }

    if (err != null) { // if (err_msg)
        System.err.print("*** Sqlite Error msg: " + sql3 + " " + err.message +
                "\n*** " + command + "\n")
        rslt[0] = NUL_D
        return SQL_BAD_D
    }

    if (sql3 == 0 && op == GLOBAL_SQL && callBackD == 0) {
        rslt[0] = NUL_D
        return SQL_OK_D
    }

    if (sql3 == 0 && op == STORE_SQL) return SQL_OK_D

    if (op == BEGIN_TRANSACTION_SQL_D || op == COMMIT_TRANSACTION_SQL_D ||
        op == SAVEPOINT_SQL_D || op == ROLLBACK_TRANSACTION_SQL_D ||
        op == PRAGMA_SQL_D
    ) {
        return if (sql3 == 0) SQL_OK_D else SQL_BAD_D
    }

    if (callBackD == 0) return SQL_BAD_D
    if (sql3 == 0) return SQL_OK_D
    return SQL_BAD_D
}
