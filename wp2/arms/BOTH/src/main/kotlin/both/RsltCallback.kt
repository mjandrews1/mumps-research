package both

// Unit 199 render: interp-sql.cpp::rslt_callback (roster 199,
// batch 7, 31 LOC; vendor body :886-918). The sqlite3_exec
// result callback: builds the ^name("v1","v2") result string
// and the rsltSpec 0x01-separated subscript spec for MDH
// result reporting.
//
// This is the FIRST interp-sql.cpp unit rendered in the arm;
// the TU carrier seats here, owner-pinned to the interp-sql
// misc owner (batch 9): rsltSpec (interp-sql.cpp:69, read by
// the :778 sibling) and call_back (:886, written :894). The
// sqlite3_exec/prepare family stays with the batch-9 misc
// owner; this render writes only the two carrier fields.
//
// Consults (rule base wp2/arms/BOTH/rules_v0.pl):
//   h014 out_of_scope_call - reused: no callables (pure buffer
//     building; strcpy/strcat fold to StringBuilder + writeC).
//   h015 grammar-collision - reused: ASCII only, no backticks,
//     no raw comment dollars, trailing newline.
//   h017 naming - reused: rslt_callback -> rsltCallback; the
//     carrier seats callBack/rsltSpec with the SqlRuntime
//     qualifier.
//   h019 class_member_carrier - hit: the TU globals ride the
//     SqlRuntime object (the BtreeRuntime precedent).
//   h013 dead_store - hit: err_msgs is never read (the
//     sqlite3 callback contract parameter; shape kept,
//     journaled); the inner else strcpy(rslt,"") in the
//     multi-column arm is DEAD CODE (the null col_vals[0]
//     case already returned at the top; journaled, not
//     rendered).
//
// Quirks kept (ledgered):
//   - static char mark[2] = {1,0}: the 0x01 subscript
//     separator; folds to the const char seat.
//   - the close-paren edit rslt[i-2]=')'; rslt[i-1]=0 folds
//     to the truncate-2-append-')' StringBuilder op (the
//     observable buffer content is identical).
//   - an immediate loop break (empty first value column)
//     still lands the close-paren edit on the "(\"" tail -
//     kept exact (the ^name) shape).
//   - rsltSpec is touched ONLY in the multi-column arm (the
//     one-column answer leaves the previous spec in place -
//     kept).

// The interp-sql.cpp TU-static carrier (the BtreeRuntime
// precedent); the sqlite3 driver seats are the batch-9 misc
// owner's.
internal object SqlRuntime {
    var callBack = 0 // static int call_back - interp-sql.cpp:886
    val rsltSpec = CharArray(STR_MAX) // :69 - result interface
    // unit-271 adds (verified folds of the :53/:54 TU statics):
    var open = 0 // static int open - interp-sql.cpp:53
    var access = 0L // static long long access - :54 (dead store,
    // only reader the commented contention-throttle block)
}

// the static char mark[2] = {1,0} seat
private const val RSLT_MARK = '\u0001'

// int rslt_callback(void *rslt1, int num_cols, char *col_vals[],
//                   char **err_msgs) - interp-sql.cpp:886
internal fun rsltCallback(
    rslt: CharArray,
    numCols: Int,
    colVals: Array<String?>,
    @Suppress("UNUSED_PARAMETER") errMsgs: Array<String?>?,
): Int {
    SqlRuntime.callBack = 1 // we've been called

    if (colVals[0] == null) { // null
        writeC(rslt, "")
        return 0
    }

    if (numCols == 1) { // one column answer
        writeC(rslt, colVals[0]!!)
        return 0
    }

    val name = colVals[0]!!
    val sb = StringBuilder()
    sb.append('^')
    sb.append(name) // the dead inner else arm is not rendered (see KDoc)
    // strcpy(rsltSpec, col_vals[0])
    val spec = StringBuilder(name)
    sb.append("(\"")

    for (i in 1 until numCols) { // 0 is array name
        val col = colVals[i]
        // if (!col_vals[i] || !strlen(col_vals[i])) break
        if (col == null || col.isEmpty()) break
        sb.append(col)
        spec.append(RSLT_MARK) // strcat(rsltSpec, mark)
        spec.append(col)
        sb.append("\",\"")
    }

    // strcat(rsltSpec, mark) - the final separator
    spec.append(RSLT_MARK)
    writeC(SqlRuntime.rsltSpec, spec.toString())

    // i = strlen(rslt); rslt[i-2]=')'; rslt[i-1]=0
    sb.setLength(sb.length - 2)
    sb.append(')')
    writeC(rslt, sb.toString())

    return 0
}
