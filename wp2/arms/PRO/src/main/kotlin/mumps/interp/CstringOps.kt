package mumps.interp

/**
 * PRO-arm shared C-string/byte-domain helpers for interpreter-side
 * units (h012 unsigned_byte_domain: C char domains map to
 * Char(0..255) CharArrays, NUL-terminated).
 */

/** C string terminator in the byte domain. */
const val NUL = '\u0000'

/** strlen over a NUL-terminated region. */
fun strlenC(b: CharArray, off: Int = 0): Int {
    var n = off
    while (b[n] != NUL) n++
    return n - off
}

/** C-string at offset (up to the first NUL). */
fun cstringAt(b: CharArray, off: Int = 0): String {
    val sb = StringBuilder()
    var k = off
    while (b[k] != NUL) {
        sb.append(b[k])
        k++
    }
    return sb.toString()
}

/**
 * C atoi: skip whitespace, optional sign, leading decimal digits;
 * anything else stops the scan. (Overflow is undefined in C; library
 * usage keeps values in range.)
 */
fun atoiC(b: CharArray, off: Int = 0): Int {
    var k = off
    while (b[k] == ' ' || b[k] == '\t' || b[k] == '\n' ||
        b[k] == '\u000B' || b[k] == '\u000C' || b[k] == '\r') k++
    var sign = 1
    if (b[k] == '+' || b[k] == '-') {
        if (b[k] == '-') sign = -1
        k++
    }
    var r = 0
    while (b[k] in '0'..'9') {
        r = r * 10 + (b[k] - '0')
        k++
    }
    return sign * r
}

/** Copies a Kotlin string over dst incl. the terminating NUL. */
fun writeCstring(dst: CharArray, s: String, off: Int = 0) {
    var d = off
    for (c in s) {
        dst[d] = c
        d++
    }
    dst[d] = NUL
}

/** strcpy(dst, src-string): copies src incl. the NUL. */
fun strcpyC(dst: CharArray, src: String, off: Int = 0) = writeCstring(dst, src, off)

/** strcat(dst, s): appends s at dst's NUL. */
fun strcatC(dst: CharArray, s: String) {
    var d = strlenC(dst)
    for (c in s) {
        dst[d] = c
        d++
    }
    dst[d] = NUL
}

/** strncmp(b, off, s, n): 0 iff the first n bytes match s. */
fun strncmpC(b: CharArray, off: Int, s: String, n: Int): Int {
    for (k in 0 until n) {
        val bc = b[off + k]
        val sc = if (k < s.length) s[k] else NUL
        if (bc != sc) return bc.code - sc.code
        if (bc == NUL) return 0
    }
    return 0
}
