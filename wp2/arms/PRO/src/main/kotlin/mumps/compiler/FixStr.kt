package mumps.compiler

/**
 * PRO-arm translation of compiler-parse.c::fixstr (pilot B1, unit 7).
 * Translated independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe).
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h007 char_buffer         : tmp[1024] is append-only mutable scratch
 *    (no index surgery), so the rule's StringBuilder default applies
 *    unmodified — in contrast to unit 5's EncodeHTML, where byte-domain
 *    surgery forced the CharArray adaptation.
 *  - h012 unsigned_byte_domain: a is a NUL-terminated byte domain.
 *  - h017 naming              : fixstr keeps the vendor name.
 */
object FixStr {

    private const val NUL = '\u0000'

    /** Escapes double quotes, single quotes, and backslashes in place. */
    fun fixstr(a: CharArray) {
        val tmp = StringBuilder()
        var i = 0
        while (a[i] != NUL) {
            when (a[i]) {
                '"' -> { tmp.append('\\'); tmp.append('"') }
                '\'' -> { tmp.append('\\'); tmp.append('\'') }
                '\\' -> { tmp.append('\\'); tmp.append('\\') }
                else -> tmp.append(a[i])
            }
            i++
        }

        // strcpy(a, tmp)
        var d = 0
        for (c in tmp) {
            a[d] = c
            d++
        }
        a[d] = NUL
    }
}
