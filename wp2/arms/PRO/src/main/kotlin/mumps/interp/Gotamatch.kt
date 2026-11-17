package mumps.interp

/**
 * PRO-arm translation of interp-bmgsubs.c::gotamatch (pilot B1, unit
 * 17). Translated independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), reference source
 * lines 347-367.
 *
 * gotamatch copies the matched line (through NL) from s into the
 * static result buffer, bumps nsuccess, and either prints it (no
 * action) or applies the caller's action callback; returns the
 * advanced s pointer, or NULL when the action declines.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h004 globals_ext        : vendor nsuccess is bmdata.bmg_nsuccess
 *    reached through #define nsuccess (lines 91/99); carried on the
 *    BmgData carrier.
 *  - h007 static_scratch    : vendor static char result[BUFSIZE]
 *    persists across calls; carried on the GotamatchScratch object.
 *  - h012 unsigned_byte_domain: s/result byte domains.
 *  - h017 naming            : gotamatch keeps the vendor name; the
 *    returned char* becomes an Int offset into s (pointer arithmetic
 *    h017 convention) with -1 as the NULL sentinel (documented).
 */
private const val BUFSIZE = 50000 // interp-bmgsubs.c:75
private const val NL = '\n'       // interp-bmgsubs.c:81

/**
 * h004: interp-bmgsubs.c bmdata carrier (struct bmg_data, lines
 * 84-93; the #define aliases delta0/cmap/str/linetemp/pattern/
 * patlen/nsuccess). nsuccess was added by unit 17; the remaining
 * members were added by B3 unit 70 (bmg_fsearch) in vendor struct
 * order; contents are owned by bmg_setup (not a corpus unit) and
 * bmg_search (batch-5 unit).
 */
object BmgData {
    /** line 85: int bmg_delta0[256] - ascii only. */
    val delta0 = IntArray(256)

    /** line 86: char bmg_cmap[256] - (un)folded characters. */
    val cmap = CharArray(256)

    /** line 87: char bmg_str[BUFSIZE + 2] (BUFSIZE 50000, :75). */
    val str = CharArray(50000 + 2)

    /** line 88: char bmg_linetemp[BUFSIZE]. */
    val linetemp = CharArray(50000)

    /** line 89: char bmg_pattern[PATSIZE] (PATSIZE 9000, :78). */
    val pattern = CharArray(9000)

    /** line 90: int bmg_patlen. */
    var patlen: Int = 0

    /** line 91: int bmg_nsuccess. */
    var nsuccess: Int = 0
}

/** h007: vendor static char result[BUFSIZE] (function-local static). */
object GotamatchScratch {
    val result = CharArray(BUFSIZE)
}

/**
 * Vendor signature: char *gotamatch(char *s, int pos, int (*action)()).
 * Kotlin: s passed with an explicit offset; the advanced pointer
 * returns as the new offset (-1 = NULL).
 */
fun gotamatch(
    s: CharArray,
    sOff: Int,
    pos: Int,
    action: ((String, Int) -> Boolean)?
): Int {
    val result = GotamatchScratch.result
    BmgData.nsuccess++

    // do { *r++ = *s; } while (*s++ != NL);  *r = 0;
    var r = 0
    var p = sOff
    var c: Char
    do {
        c = s[p]
        result[r] = c
        r++
        p++
    } while (c != NL)
    result[r] = NUL

    val line = cstringAt(result)

    if (action == null) {
        print(line) // printf("%s", result)
        return p
    } else if (action(line, pos)) {
        return p
    } else {
        return -1 // NULL
    }
}
