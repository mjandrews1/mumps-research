package mumps.compiler

/**
 * PRO-arm byte-domain helpers for compiler-side units (h012
 * unsigned_byte_domain: C char domains map to Char(0..255) CharArrays,
 * NUL-terminated).
 */

/** C string terminator in the byte domain. */
const val NUL = '\u0000'

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

/** strcpy(dst, src): copies src (incl. NUL) into dst. */
fun strcpyC(dst: CharArray, src: CharArray) {
    var k = 0
    while (src[k] != NUL) {
        dst[k] = src[k]
        k++
    }
    dst[k] = NUL
}

/** strcpy(dst, src): copies a Kotlin string (incl. NUL) into dst. */
fun strcpyC(dst: CharArray, src: String, off: Int = 0) {
    var d = off
    for (c in src) {
        dst[d] = c
        d++
    }
    dst[d] = NUL
}
