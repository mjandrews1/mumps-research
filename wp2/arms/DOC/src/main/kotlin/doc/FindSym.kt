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

// UNIT 264 (B9 tranche 2, units.csv row 265: loc 25, ptr 0,
// ctl 7, score 11.5, rank 114, core stratum; physLOC 25
// physically verified this session): interp-strmanip.cpp::
// xindex - the substring probe of the dollar-FIND walk. TAG
// CORRECTION (u243 Concat precedent): the batch-1 stub below
// named the owner "interp-sym.cpp::xindex; not a roster unit" -
// WRONG on both counts; interp-sym.cpp only CALLS xindex (its
// :1291/:1371 call sites), the body lives at interp-strmanip.
// cpp:87 and its units.csv row 265 IS a batch-9 roster unit -
// so the stub flips to the landed body this push (no separate
// unit file: the same-name top-level fun lives here since
// batch 1; the H-015 same-name collision rule). TWIN NOTE: the
// compiler-sys1.c::xindex body (units.csv row 297, batch-10
// roster row) is the same-name sibling of this interpreter
// helper - it belongs to the COMPILER xindex family and is NOT
// rendered by this row (same-name overload family, distinct
// vendor site; batch 10 will own it). Consult H-010 hit-reuse:
// the C short/equality carrier idiom over the CharArray domain.
// VERIFICATION ADDENDUM (B10 tranche 2, units.csv row 297):
// the batch-10 roster row compiler-sys1.c::xindex (loc 21,
// score 10.9, rank 107, core stratum; physically verified at
// vendor compiler-sys1.c:249-274 this session - the IDENTICAL
// TWIN of this body: goto-label spelling (mismatch goto outer)
// where this seat uses the flg-flag spelling, same probe
// algorithm, same return 0/k+1 contract. The row is
// VERIFICATION-ONLY (H-014 hit-reuse adjudication): no new
// seat for row 297 - the row folds onto this u264 render
// whose algorithm covers the compiler twin statement-for-
// statement; the vendor operates on the COMPILER domain here,
// which the H-015 same-name collision rule discloses; treated
// as the identical twin of this seat (both vendor bodies
// physically verified this session). Consult row 297: entry
// H-014, outcome hit-reuse, timestamp 2026-11-22T06:19:00Z.

fun xindex(a: CharArray, b: CharArray, i: Short): Int {
    var flg = 0
    // for (i = 0; ptrn[i] != 0; i++); --i; (pattern last index)
    var ii = 0
    while (b[ii] != NUL) ii++
    ii--
    // for (j = 0; source[j] != 0; j++); --j; (source last index)
    var j = 0
    while (a[j] != NUL) j++
    j--
    var istrt = i - 1
    if (i <= 1) istrt = 0

    var k = istrt
    while (k <= j) {
        var l = 0
        while (l <= ii) {
            if (b[l] != a[k + l]) {
                flg = 1
                break
            }
            l++
        }

        if (flg == 1) {
            flg = 0
            k++
            continue
        }

        return k + 1
    }
    return 0
}

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
