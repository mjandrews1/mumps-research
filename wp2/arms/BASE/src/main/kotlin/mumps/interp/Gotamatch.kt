package mumps.interp

/**
 * Kotlin translation of interp-bmgsubs.c::gotamatch (Study Subset unit 17,
 * Batch 1, arm BASE; translated 2026-11-14).
 *
 * C (K&R style): char *gotamatch(register char *s, register int pos,
 *                               int (*action)())
 * s points at a line start inside the scan buffer; the line (through the
 * trailing \n) is copied to a static result buffer; with action == NULL
 * the line is printed and the after-line position returned, otherwise the
 * action callback decides success.
 *
 * Kotlin mapping: s -> (buf, from) offset convention (heuristics 8/57);
 * function-pointer param -> nullable function type with null == C NULL
 * (heuristic 59); static result buffer -> String value (56); pointer
 * return -> index just past the line, Int? with null == C NULL.
 */
fun gotamatch(buf: String, from: Int, pos: Int, action: ((result: String, pos: Int) -> Boolean)?): Int? {
    InterpState.bmgNsuccess++ // C: nsuccess++ (macro -> bmdata.bmg_nsuccess)

    val b = StringBuilder()
    var i = from
    do {
        b.append(buf[i])
        i++
    } while (buf[i - 1] != '\n') // C: do *r++ = *s; while (*s++ != NL);
    val result = b.toString()

    if (action == null) { // C: if (action == NULL) { printf("%s", result); return(s); }
        print(result)
        return i
    }
    return if (action(result, pos)) i else null
}
