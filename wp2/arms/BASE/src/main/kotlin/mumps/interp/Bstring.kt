package mumps.interp

/**
 * Kotlin translation of interp-bstring.cpp (Study Subset unit 22 — the
 * <misc> region within it; the module is translated whole for coherence:
 * new_bstring, bstrcat, bcatchr, make_cstring, checked_strlen).
 * Batch 1, arm BASE; translated 2026-11-14.
 *
 * C struct bstring { int length; char string[STR_MAX]; } with BY-VALUE
 * returns and POINTER mutation -> Kotlin class instances: fresh instance
 * per new_bstring (the only C construction site), pointer param -> object
 * reference, *str return -> same reference (heuristic 68).
 *
 * STR_MAX is the file-level const from InterpMisc.kt (32000).
 */
class Bstring {
    var length: Int = 0
    val string: StringBuilder = StringBuilder(STR_MAX)
}

/** C: struct bstring new_bstring(const char initial[]) */
fun newBstring(initial: String): Bstring {
    val result = Bstring()
    result.length = checkedStrlen(initial)
    result.string.append(initial.substring(0, result.length)) // C: strncpy
    return result
}

/** C: struct bstring bstrcat(const struct bstring left, const struct bstring right) */
fun bstrcat(left: Bstring, right: Bstring): Bstring {
    val catlength = left.length + right.length
    if (catlength > STR_MAX) {
        System.err.print("String too long - aborting.\n") // C: fprintf(stderr, ...)
        sigint(100)
    } else {
        val result = Bstring()
        result.length = catlength
        result.string.append(left.string.substring(0, minOf(left.length, left.string.length)))
        // C: result.string[left.length] = '\0' — implicit in the builder
        result.string.append(right.string.substring(0, minOf(right.length, right.string.length)))
        return result
    }
    return newBstring("") // C: dummy unreachable return
}

/** C: struct bstring bcatchr(struct bstring *str, const char c) — mutates str. */
fun bcatchr(str: Bstring, c: Char): Bstring {
    str.length++
    if (str.length > STR_MAX) {
        System.err.print("String too long, aborting.\n")
        sigint(100)
    }
    str.string.setCharAt(str.length - 1, c) // C: str->string[str->length - 1] = c
    if (str.string.length < str.length) str.string.append(c) // pad if needed
    return str // C: return *str — same reference (copy semantics diverge only at aliasing)
}

/** C: void make_cstring(char *dest, const struct bstring src) */
fun makeCstring(dest: StringBuilder, src: Bstring) {
    dest.setLength(0)
    dest.append(src.string.substring(0, minOf(src.length, src.string.length)))
    // C: dest[src.length] = '\0' — implicit
}

/** C: static int checked_strlen(const char cstring[]) */
fun checkedStrlen(cstring: String): Int {
    // C: for (i = 0; i <= STR_MAX; i++) if NUL return i — the NUL position
    // analog is the String length; inclusive bound preserved
    if (cstring.length <= STR_MAX) return cstring.length
    System.err.print("String too long - aborting.\n")
    sigint(100)
    return 0 // C: dummy unreachable return
}
