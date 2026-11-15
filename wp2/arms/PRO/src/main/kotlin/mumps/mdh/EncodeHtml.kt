package mumps.mdh

import java.util.Locale

/**
 * PRO-arm translation of libmpscpp.cpp::EncodeHTML (pilot B1, unit 5 —
 * both overloads). Translated independently from the pristine vendor
 * source (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe).
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h007 char_buffer         : ADAPTED. v1d/t2 are mutable scratch, but
 *    h007's StringBuilder default cannot preserve byte-domain fidelity
 *    (chars >= 0x80, NUL-terminated C-string surgery by index); both are
 *    implemented as explicit CharArrays. Logged hit, not reuse.
 *  - h008 static_scratch      : vendor's static char v1d[STR_MAX] is
 *    re-initialized by the entry strcpy and never escapes, so no caller
 *    aliases it across calls; fresh local per call.
 *  - h009 format_string       : sprintf(t1, "%%%2X", v1d[i]) — exact
 *    formatter with Locale.US; width-2 space padding preserved.
 *  - h010 numeric_promotion   : char is signed on the reference platform;
 *    bytes >= 0x80 sign-extend to int before the hex format (documented).
 *  - h017 naming              : EncodeHTML keeps the vendor name.
 */

private const val STR_MAX = 32000 // include/mumpsc/strmax.h:3
private const val NUL = '\u0000'

fun EncodeHTML(str: Mstring): Mstring = EncodeHTML(str.value)

fun EncodeHTML(str: String): Mstring {
    // h008: fresh per call — the C static is fully re-seeded by strcpy.
    val v1d = CharArray(STR_MAX)

    // strcpy(v1d, str); inputs are bounded by library usage (the C static
    // would overflow past STR_MAX — same precondition).
    for (k in str.indices) v1d[k] = str[k]

    var i = 0
    while (v1d[i] != NUL) {
        if (v1d[i] == ' ') v1d[i] = '+'
        else if (!isAlnum(v1d[i])) {

            // h009 + h010: sprintf(t1, "%%%2X\0", v1d[i]); signed char
            // sign-extends to int, so bytes >= 0x80 format as FFFFFFxx.
            val cv = if (v1d[i].code > 127) v1d[i].code - 256 else v1d[i].code
            val t1 = "%" + String.format(Locale.US, "%2X", cv)

            v1d[i] = NUL // v1d[i] = 0

            val t2 = CharArray(STR_MAX)
            strmove(t2, 0, v1d, 1)      // strmove(t2, &v1d[1])
            strcat(t2, t1)              // strcat(t2, t1)
            strcatCstring(t2, v1d, i + 1) // strcat(t2, &v1d[i + 1])
            strmove(v1d, 1, t2, 0)       // strmove(&v1d[1], t2)

            i += 2
        }
        i++
    }

    return Mstring(cstringAt(v1d, 0))
}

/**
 * h012-family byte-domain helper: vendor isalnum over char. The
 * reference locale is en_US.UTF-8, where single bytes >= 0x80 are not
 * alnum (they are %-encoded by the caller's logic).
 */
private fun isAlnum(c: Char): Boolean =
    c in '0'..'9' || c in 'A'..'Z' || c in 'a'..'z'

/** C-string length of a NUL-terminated region. */
private fun strLen(b: CharArray, off: Int = 0): Int {
    var n = off
    while (b[n] != NUL) n++
    return n - off
}

/** C-string at offset (up to the first NUL). */
private fun cstringAt(b: CharArray, off: Int): String {
    val sb = StringBuilder()
    var k = off
    while (b[k] != NUL) {
        sb.append(b[k])
        k++
    }
    return sb.toString()
}

/** strmove(dst, dstOff, src, srcOff): copies src string incl. NUL. */
private fun strmove(dst: CharArray, dstOff: Int, src: CharArray, srcOff: Int) {
    var s = srcOff
    var d = dstOff
    while (src[s] != NUL) {
        dst[d] = src[s]
        s++
        d++
    }
    dst[d] = NUL
}

/** strcat(dst, s): append a Kotlin string at dst's NUL. */
private fun strcat(dst: CharArray, s: String) {
    var d = strLen(dst)
    for (c in s) {
        dst[d] = c
        d++
    }
    dst[d] = NUL
}

/** strcat(dst, src, srcOff): append src's C-string at dst's NUL. */
private fun strcatCstring(dst: CharArray, src: CharArray, srcOff: Int) {
    var d = strLen(dst)
    var s = srcOff
    while (src[s] != NUL) {
        dst[d] = src[s]
        s++
        d++
    }
    dst[d] = NUL
}
