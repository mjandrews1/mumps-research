package doc

// Unit 7 (B1 tranche 2): compiler-parse.c::fixstr (batch 1, simple
// stratum; LOC 25, score 7.7). Escape pass over a NUL-terminated
// CharArray (H-007 buffer role: in-place scratch): double quote,
// single quote and backslash each gain a leading backslash into the
// tmp ledger, then the result is copied back in place. Char literals
// per H-015 (quote and backslash bytes written directly, no
// templating needed); loop shape kept verbatim (the vendor's comma-
// free if-continue ladder). Naming per H-017.

fun fixstr(a: CharArray) {
    val tmp = CharArray(1024)
    var i = 0
    var j = 0

    while (a[i] != NUL) {
        if (a[i] == '"') {
            tmp[j++] = '\\'
            tmp[j++] = '"'
            i++
            continue
        }

        if (a[i] == '\'') {
            tmp[j++] = '\\'
            tmp[j++] = '\''
            i++
            continue
        }

        if (a[i] == '\\') {
            tmp[j++] = '\\'
            tmp[j++] = '\\'
            i++
            continue
        }

        tmp[j++] = a[i]
        i++
    }

    tmp[j] = NUL
    CstringOps.strcpyC(a, tmp)
}
