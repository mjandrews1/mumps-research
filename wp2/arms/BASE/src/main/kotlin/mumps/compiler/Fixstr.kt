package mumps.compiler

/**
 * Kotlin translation of compiler-parse.c::fixstr (Study Subset unit 7,
 * Batch 1, arm BASE; translated 2026-11-14).
 *
 * Escapes double quote, single quote, and backslash by prefixing each
 * with a backslash. C signature: void fixstr(unsigned char a[]) — mutates
 * the caller's buffer in place (tmp build + strcpy back).
 *
 * Call sites in the C use fixstr(&bd[1]); those belong to regions outside
 * this batch and will adapt via the offset convention (heuristic 8) when
 * translated.
 */
fun fixstr(a: StringBuilder) {
    val tmp = StringBuilder()
    for (i in 0 until a.length) {
        when (a[i]) {
            '"' -> tmp.append("\\\"")   // backslash + "
            '\'' -> tmp.append("\\'")   // backslash + '
            '\\' -> tmp.append("\\\\") // backslash + backslash
            else -> tmp.append(a[i])
        }
    }
    a.setLength(0) // C: strcpy(a, tmp)
    a.append(tmp)
}
