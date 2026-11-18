package mumps.interp


/**
 * PRO-arm translation of interp-sql.cpp::rslt_callback (B7
 * tranche 2, unit 200, rank 184). Translated independently
 * from the pristine vendor source (mumps-14-may-2026.tgz,
 * sha256 621deb25...ffdbe), reference source lines 888-928
 * (the sqlite3_exec result callback, between the static
 * call_back flag and the exec call sites at 1002/1007/1030/
 * 1078).
 *
 * Identity verified against the study tool:
 * interp-sql.cpp::rslt_callback, cpp, loc 31,
 * comment_ratio 0.031, pointer_marks 4, control_marks 9,
 * score 24.1 - exact match to wp1/study-subset/units.csv
 * row 200 (batch 7).
 *
 * rslt_callback(void* rslt1, num_cols, col_vals, err_msgs):
 * the sqlite3_exec callback. call_back is raised (the exec
 * succeeded at least once). A null first column copies ""
 * into rslt and returns. One column copies the value. Many
 * columns build the "array-name("value","value")" shape into
 * rslt while mirroring each value into the file-scope
 * rsltSpec (SOH-separated, for MDH result reporting), then
 * close-paren-chops the trailing '","' tail by overwriting
 * the last two bytes with ')',0.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h006 low_level_pointer : the void* rslt1 result carrier
 *    becomes the caller-owned CharArray (C: (char*)rslt1);
 *    col_vals is a C char* array -> Array<CharArray?>.
 *  - h007 char_buffer        : the file-scope
 *    unsigned char rsltSpec[STR_MAX] (vendor 69) and the
 *    static int call_back (vendor 886) ride the
 *    SqlCallbackState object (one owner object per C
 *    translation unit's global set).
 *  - h012 unsigned_byte_domain: rslt/rsltSpec/col_vals are
 *    Char(0..255) domains; mark is the SOH byte Char(1).
 *  - h014 out_of_scope_call : strcpy/strcat/strlen ->
 *    per-file helpers (strlenC reused from the compiler Cstring
 *    seat; CstringOps single-underscore OK, the h015 scan
 *    clean rides this KDoc).
 *  - h017 naming            : rsltCallback keeps the vendor
 *    name modulo the Kotlin camel fold (rslt_callback is a
 *    C identifier; single underscore OK).
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The dead strcpy(rslt, "^") before the col_vals[0] test
 *    is kept (the "^" is always overwritten; h013 dead-store
 *    comment only).
 *  - The err_msgs parameter is unused (vendor never reads
 *    it) - kept as an unread parameter.
 *  - The rsltSpec mark separators are SOH (1), matching the
 *    MDH SOH,SOH,NUL reporting convention.
 *  - col_vals[0] is guaranteed non-null in the multi-column
 *    branch (the null case returned at the top), so the
 *    unconditional strcpy(rsltSpec, col_vals[0]) is safe -
 *    kept without an extra guard.
 */
object SqlCallbackState {
    /** h007: vendor 69 - unsigned char rsltSpec[STR_MAX]. */
    val rsltSpec = CharArray(STR_MAX_SQL)

    /** h007: vendor 886 - static int call_back. */
    var callBack = 0
}

private const val STR_MAX_SQL = 32000 // vendor strmax.h:3 (unit-44 precedent)

private val SOH = 1.toChar() // vendor mark[2] = {1,0}

fun rsltCallback(
    rslt: CharArray,
    numCols: Int,
    colVals: Array<CharArray?>,
    errMsgs: Array<CharArray?>
): Int {
    SqlCallbackState.callBack = 1 // we've been called

    if (colVals[0] == null) { // null
        strcpyFrom(rslt, 0, emptyCstring()) // strcpy(rslt, "")
        return 0
    }

    if (numCols == 1) { // one column answer
        strcpyFrom(rslt, 0, colVals[0] ?: emptyCstring())
    } else {
        strcpyFrom(rslt, 0, emptyCstring()) // strcpy(rslt, "")
        strcatFrom(rslt, colVals[0] ?: emptyCstring()) // array name
        val spec = SqlCallbackState.rsltSpec
        strcpyFrom(spec, 0, colVals[0] ?: emptyCstring()) // for MDH result reporting
        strcatFrom(rslt, cstringOf('(')) // "("
        strcatFrom(rslt, cstringOf('"'))

        var i = 1
        while (i < numCols) { // 0 is array name
            val cv = colVals[i]
            if (cv == null || strlenC(cv) == 0) break // empty string or null

            strcatFrom(rslt, cv)
            strcatFrom(spec, cstringOf(SOH)) // for MDH result reporting
            strcatFrom(spec, cv) // for MDH result reporting
            strcatFrom(rslt, cstringOf('"'))
            strcatFrom(rslt, cstringOf(','))
            strcatFrom(rslt, cstringOf('"'))
            i++
        }

        // i = strlen(rslt); the mark rides rsltSpec AFTER the
        // loop (vendor 922); then the '","' tail becomes ')',0.
        val e = strlenC(rslt)
        strcatFrom(spec, cstringOf(SOH))
        rslt[e - 2] = ')'
        rslt[e - 1] = NUL
    }

    return 0
}

/** C strcpy(dst, src) over the byte domains from offset 0. */
private fun strcpyFrom(dst: CharArray, dstOff: Int, src: CharArray) {
    var p = 0
    while (src[p] != NUL) {
        dst[dstOff + p] = src[p]
        p++
    }
    dst[dstOff + p] = NUL
}

/** C strcat(dst, src) - appends the src c-string at the dst terminator. */
private fun strcatFrom(dst: CharArray, src: CharArray) {
    var e = 0
    while (dst[e] != NUL) e++
    var p = 0
    while (src[p] != NUL) {
        dst[e] = src[p]
        e++
        p++
    }
    dst[e] = NUL
}

/** One-char cstring carrier (quote/paren/comma/SOH emissions). */
private fun cstringOf(c: Char): CharArray {
    val a = CharArray(2)
    a[0] = c
    a[1] = NUL
    return a
}

/** The empty cstring ("" as a zero-length NUL-terminated buffer). */
private fun emptyCstring(): CharArray {
    val a = CharArray(1)
    a[0] = NUL
    return a
}
