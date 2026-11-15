package mumps.interp

//----------------------------------------------------
//  interp-sql.cpp — fix embedded single quote marks in sql command
//  Unit 51 of the replication pilot.
//
//  C (interp-sql.cpp:115-142):
//      char* embed(char *in) — if in contains no '\'' k stays 1 and in is
//      returned as-is; otherwise every '\'' is doubled into the static
//      buffer tmp[STR_MAX]. NULL in yields the empty string.
//
//  Divergence: the C static buffer is a single shared scratch area; Kotlin
//  returns a fresh String per call. No caller in the C source relies on
//  aliasing the static buffer (all uses are immediate copy/strcat), so a
//  fresh String is behavior-preserving.
//----------------------------------------------------

/**
 * Doubles embedded single quote marks so the result is safe to splice into
 * an SQL string literal.
 *
 * Returns [in] itself (no copy) when it contains no quote — matching the C
 * fast path, which also returns the original pointer.
 */
fun embed(inStr: String?): String {
    // C: if (in == NULL) return tmp;  — tmp[0] == 0 here, i.e. "".
    if (inStr == null) return ""

    // C: scan for a quote; k stays 1 (return in) if none found.
    if ('\'' !in inStr) return inStr

    val tmp = StringBuilder(STR_MAX)
    for (ch in inStr) {
        if (ch != '\'') {
            tmp.append(ch)
        } else {
            // C: *(p2++) = '\''; *(p2++) = '\'';  — double the quote.
            tmp.append('\'').append('\'')
        }
    }
    return tmp.toString()
}
