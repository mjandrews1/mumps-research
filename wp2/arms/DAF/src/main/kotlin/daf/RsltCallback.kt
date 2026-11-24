package daf

// Unit 202 (DAF B7 T2, core): interp-sql.cpp::rslt_callback (31 LOC,
// score 24.1): the SQLITE result-row callback of the embedded-SQL
// TU: single-column answers copy through; multi-column rows build
// the global-ref text ("^name(""subs"",""subs"")...) into the rslt
// out carrier and accumulate the mark-separated rsltSpec reporting
// buffer; the loop breaks at the first empty/null column AFTER the
// name (i from 1); the trailing comma pair closes into ")" via the
// i-2/i-1 byte surgery (KEPT VERBATIM - vendor byte arithmetic,
// including the short-row corner where the surgery acts on the
// "(" quote positions).
// Consults (2026-11-24T16:13:00Z, nominal): BASE-log row 8
// hit-reuse (buffer roles: rslt the written scratch out-seat
// (strcpy/strcat chain), colVals the read-only scan), row 62
// hit-adapt (the call_back flag and the rsltSpec extern pointer
// are TU file-scope state: both fold onto top-level Kotlin state
// per the rows 6/22 object-state discipline; rsltSpec's extern
// CHAR POINTER folds onto a carrier + pending marker scheme since
// the C buffer aliases the caller's storage - the divergence
// note: the C pointer identity is replaced by the carrier copy
// rsltSpecD the MDH reporting sites read), rows 6/22 family
// hit-reuse (call_back the callback latch), manual-decision (no
// BASE row: the static char mark[2] = {1,0} folds onto the single
// Char code-point append - the mark byte is the vendor's
// result-report separator, kept as the literal byte 1).
// Dafny: EXEMPT (carrier assembly loop; journal row).

// TU file-scope state (rows 6/22, the row-62 fold)
var callBackD = 0      // int call_back latch
var rsltSpecD = ""     // unsigned char *rsltSpec reporting buffer

private const val RSLT_MARK_D: Char = '\u0001' // static char mark[2]={1,0}

fun rsltCallbackD(rslt: CharArray, colVals: Array<String?>): Int {

    callBackD = 1 // call_back = 1; we've been called

    if (colVals[0] == null) { // if (!col_vals[0]) { null
        // strcpy(rslt, ""); return 0;
        rslt[0] = NUL_D
        return 0
    }

    if (colVals.size == 1) { // if (num_cols == 1)
        // strcpy(rslt, col_vals[0]);
        copyIntoSpec(rslt, colVals[0]!!)
        return 0
    }

    // else: the multi-column lattice
    copyIntoSpec(rslt, "^")                 // strcpy(rslt, "^");
    if (colVals[0] != null) appendSpec(rslt, colVals[0]!!) // strcat(...)
    rsltSpecD = colVals[0]!!                 // strcpy(rsltSpec, col_vals[0])
    appendSpec(rslt, "(\"")                  // strcat(rslt, "(\"");

    var i = 1
    while (i < colVals.size) { // for (i=1; i<num_cols; i++)
        val cv = colVals[i]
        if (cv == null || cv.isEmpty()) break // empty string or null
        appendSpec(rslt, cv)                 // strcat(rslt, col_vals[i]);
        rsltSpecD += RSLT_MARK_D             // strcat(rsltSpec, mark);
        rsltSpecD += cv                      // strcat(rsltSpec, col_vals[i]);
        appendSpec(rslt, "\",\"")            // strcat(rslt, "\",\"");
        i++
    }

    val len = cspecLen(rslt)                 // i = strlen(rslt);
    rsltSpecD += RSLT_MARK_D                 // strcat(rsltSpec, mark);
    rslt[len - 2] = ')'                      // rslt[i - 2] = ')';
    rslt[len - 1] = NUL_D                    // rslt[i - 1] = 0;

    return 0
}

// strcpy/strcat carrier folds (row 27 family)
private fun copyIntoSpec(dst: CharArray, s: String) {
    val cs = s.toCharArray()
    var i = 0
    while (i < cs.size && i < dst.size) { dst[i] = cs[i]; i++ }
    if (i < dst.size) dst[i] = NUL_D
}

private fun appendSpec(dst: CharArray, s: String) {
    var n = 0
    while (n < dst.size && dst[n] != NUL_D) n++
    val cs = s.toCharArray()
    var i = 0
    while (i < cs.size && n < dst.size) { dst[n] = cs[i]; n++; i++ }
    if (n < dst.size) dst[n] = NUL_D
}

private fun cspecLen(buf: CharArray): Int {
    var n = 0
    while (n < buf.size && buf[n] != NUL_D) n++
    return n
}
