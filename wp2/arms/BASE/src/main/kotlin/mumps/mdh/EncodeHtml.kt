package mumps.mdh

/**
 * Kotlin translation of libmpscpp.cpp::EncodeHTML (Study Subset unit 5,
 * Batch 1, arm BASE; translated 2026-11-14).
 *
 * URL-encoding: space -> '+', non-alphanumeric ASCII -> %XX with the C
 * format's %2X space-padding quirk preserved verbatim (single-digit hex
 * codes emit a SPACE, not a zero — e.g. tab becomes "% 9").
 *
 * The C++ overload pair EncodeHTML(mstring&) / EncodeHTML(char*) collapses
 * into this single function: Kotlin String covers both call shapes
 * (mstring maps to String, heuristic 24).
 */
fun encodeHtml(str: String): String {
    val out = StringBuilder() // C: static char v1d[STR_MAX] + t2 buffer surgery
    var i = 0
    while (i < str.length) {
        val c = str[i]
        when {
            c == ' ' -> out.append('+')
            // C: isalnum() — C-locale semantics, ASCII only.
            // Kotlin's isLetterOrDigit() is Unicode-aware and would diverge.
            c.code < 128 && (c in 'a'..'z' || c in 'A'..'Z' || c in '0'..'9') -> out.append(c)
            else -> {
                // C: sprintf(t1, "%%%2X", v1d[i]) — width-2, space-padded.
                out.append(String.format("%%%2X", c.code))
                // C: after in-place splice, i = i + 2 then loop i++ -> skip 3
                // chars (the '%' plus the two-char hex field).
                i += 3
                continue
            }
        }
        i++
    }
    return out.toString()
}
