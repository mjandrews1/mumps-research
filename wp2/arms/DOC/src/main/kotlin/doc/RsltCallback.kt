package doc

// Unit 202 (B7 tranche 2): interp-sql.cpp::rslt_callback (batch 7,
// core stratum; LOC 31, score 24.1, rank 184; vendor
// interp-sql.cpp:888-925, the SQLITE callback interface section,
// physically verified this session; nonblank 31 matches the roster
// row). Consult H-007 hit-adapt: the buffer roles classify as rslt
// (caller's out-buffer, mutable scratch via the CharArray carrier),
// rsltSpec (the TU-scope result-spec scratch, raised here as the
// H-004 owner object SqlCallbackState together with the static
// call_back flag at :886), col_vals (the NULL-able column array),
// and the static char mark[2] = {1,0} literal constant (immutable
// per call; a fresh val, the H-008 shape). err_msgs is never read
// (H-013 dead parameter, kept for the sqlite3 callback signature
// fidelity; the vendor passes it unchanged to sqlite3_exec through
// the sql arm). NULL column -> empty result; the one-column fast
// path; else the caret + name + parenthesized quoted list with the
// trailing quote-pair overwritten to a close paren (the vendor
// rslt[i-2]=')' / rslt[i-1]=0 surgery kept verbatim); the mark-
// sep MDH result spec builds into SqlCallbackState.rsltSpec in the
// vendor order. Naming per H-017 (vendor name kept, underscore per
// the unit-111 _fnumber precedent).

// interp-sql.cpp TU externs named by this unit (H-004 owner
// object): :69 unsigned char rsltSpec[STR_MAX] (result interface)
// and :886 static int call_back. The other interp-sql readers
// of rsltSpec (the :778 key copy and the sql-arm tests at
// :1057/:1094) are out-of-scope corpus units of later batches and
// bind here when they land.
object SqlCallbackState {
    var call_back: Int = 0
    val rsltSpec = CharArray(STR_MAX)
}

fun rslt_callback(
    rslt1: CharArray,
    num_cols: Int,
    col_vals: Array<CharArray?>,
    err_msgs: Array<CharArray?>?
): Int {
    val rslt = rslt1
    val mark = charArrayOf(CHAR_1, NUL) // static char mark[2] = {1,0}

    SqlCallbackState.call_back = 1 // we've been called

    val name0 = col_vals[0]
    if (name0 == null) { // null
        CstringOps.strcpyS(rslt, "")
        return 0
    }

    if (num_cols == 1) {
        CstringOps.strcpyC(rslt, name0) // one column answer
    } else {
        CstringOps.strcpyS(rslt, "^")
        if (col_vals[0] != null) {
            CstringOps.strcatS(rslt, CstringOps.cstring(name0)) // array name
        } else {
            CstringOps.strcpyS(rslt, "")
        }
        // vendor quirk kept: the name0 backstop branch above is
        // dead here (the null guard returned) - both stores run
        CstringOps.strcpyC(SqlCallbackState.rsltSpec, name0) // for MDH result reporting
        CstringOps.strcatS(rslt, "(\"")

        var i = 1
        while (i < num_cols) { // 0 is array name
            val col = col_vals[i]
            if (col == null || CstringOps.strlenC(col) == 0) {
                break // empty string or null
            }
            CstringOps.strcatS(rslt, CstringOps.cstring(col))
            CstringOps.strcatS(SqlCallbackState.rsltSpec, CstringOps.cstring(mark)) // for MDH result reporting
            CstringOps.strcatS(SqlCallbackState.rsltSpec, CstringOps.cstring(col)) // for MDH result reporting
            CstringOps.strcatS(rslt, "\",\"")
            i++
        }

        val il = CstringOps.strlenC(rslt)
        CstringOps.strcatS(SqlCallbackState.rsltSpec, CstringOps.cstring(mark)) // for MDH result reporting
        rslt[il - 2] = ')'
        rslt[il - 1] = NUL
    }

    return 0
}
