package doc

import java.util.Locale

// Unit 6 (B1 tranche 1): interp-sym.cpp::_find (batch 1, simple
// stratum; LOC 15, score 7.5). The dollar-FIND intrinsic core: parse the
// begin index (atoi decimal-prefix contract rendered as the leading
// integer parse), clamp negatives to zero, advance by xindex when a
// search key is present, clamp the result to the input length + 1,
// and emit the integer result out by sprintf("%d") (H-009, Locale.US;
// the out param is an NUL-terminated CharArray carrier, H-007). The
// signed short widening of the xindex probe position is kept
// explicitly per H-010 ((short) i). xindex is out of scope per H-014
// (not a roster unit). Naming per H-017: _find kept verbatim (valid Kotlin id, 'find' not used)
// (leading underscore preserved, KeyWordsClean).

// TODO stub: xindex owned by interp-sym.cpp::xindex; not a roster unit.
fun xindex(a: CharArray, b: CharArray, i: Short): Int =
    throw NotImplementedError("xindex: out-of-scope owner (interp-sym.cpp::xindex)")

// atoi(begin): decimal-prefix parse per the C contract (no failure
// signal; garbage yields 0; kept local to the unit's render)
private fun atoiC(begin: CharArray): Int {
    var idx = 0
    while (idx < begin.size && (begin[idx] == ' ' || begin[idx] == '\t' ||
                begin[idx] == '\n' || begin[idx] == '\r')) idx++
    var sign = 1
    if (idx < begin.size && (begin[idx] == '+' || begin[idx] == '-')) {
        if (begin[idx] == '-') sign = -1
        idx++
    }
    var v = 0
    while (idx < begin.size && begin[idx] in '0'..'9') {
        v = v * 10 + (begin[idx] - '0')
        idx++
    }
    return sign * v
}

fun _find(out: CharArray, in_: CharArray, key: CharArray, begin: CharArray) {
    var i = atoiC(begin)
    if (i < 0)
        i = 0

    if (key[0] != NUL) {
        val r = xindex(in_, key, i.toShort())
        if (r > 0)
            i = r + CstringOps.strlenC(key)
    }

    if (i > CstringOps.strlenC(in_) + 1)
        i = 0

    CstringOps.strcpyS(out, String.format(Locale.US, "%d", i))
}
