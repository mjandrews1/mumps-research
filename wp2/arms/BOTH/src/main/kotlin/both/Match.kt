package both

// Unit 181 (BOTH B6 T3, complex): interp-parse.cpp::match
// (86 LOC). Batch_plan ordinal 181: complex, LOC 86, rank 261.
// The MUMPS pattern-match classifier: the pts/ct walk over the
// data string ad against the pattern classes (C control, N
// numeric, P punctuation, A alpha, L lowercase, U uppercase, E
// ascii-extend, the double-quote literal family).
//
// Vendor behavior: ptr starts at -1 (the first ptr++ lands ad
// index 0 - the 1-based C walk over the 0-based array, kept
// exact so the ptr+1==strlen close mirrors); *rslt = 0 until the
// FULL walk passes AND ptr+1 == strlen(ad). Rows i in 1..lx
// with ct[i]==0 are skipped entirely; the class character
// reads bd[pts[1][i]] (upper or lower - both arms merge). The
// class walks: C consumes ct[i] chars that are < 32 or 127 (the
// 127 DEL seat of the vendor <32 continue + ==127 continue
// pair); N consumes '0'..'9'; P consumes chars in the xindex
// punctuation literal through tmp[0] (the literal contains the
// quote, the dollar, the backtick and the backslash bytes -
// kept byte-faithful below through the escape seats); A
// consumes 65..90 or 97..122; L consumes 97..122; U consumes
// 65..90; E consumes <= 127 (the >127 return arm); the
// double-quote class: ct[i]==0 consumes nothing (continue);
// else the substr pair (ad at ptr, bd at pts[1][i]+1, span
// pts[2][i]-1 each) must strcmp equal or the match fails; the
// walk advances ptr by the literal length - 1 (the consumed
// span folds); the default class returns (unknown class fails
// the whole match). Every bounds miss (ptr > strlen(ad)) and
// every class miss returns with *rslt unchanged at 0. The
// strlen((char *)ad) recompute per check folds to the one
// adLen seat (the strlen is pure; journaled).
//
// Quirks kept (ledgered):
//   - ptr+1 != strlen(ad) close: the full-string requirement
//     (a trailing partial class walk never sets rslt even
//     when all consumed chars matched - the vendor close).
//   - the E class break falls through WITH the last ptr++ kept
//     (the loop shape: no else-continue arm - the final j
//     iteration's ptr++ counts, unlike the other classes whose
//     failures return immediately).
//   - the double-quote class consumes ptr BEFORE the substr
//     (ad[ptr] at the post-increment position) and the ptr =
//     ptr + strlen(tmp1) - 1 fold lands the next class at the
//     next unconsumed byte. BODIES kept exact.
//   - the P punctuation literal keeps the vendor double
//     single-quote ('') pair byte-faithful (two apostrophes).
//
// BOTH consults (find_rule before render, events logged):
//   h007 scratch_collapse - reused: tmp[2]/tmp1[1024]/tmp2[1024]
//     fold to the local seats.
//   h011 goto - reused: the switch break/continue ladder folds
//     to the Kotlin return/break shapes (no goto family).
//   h014 out_of_scope_call - reused: xindex stub (owner
//     interp-strmanip.cpp::xindex, roster 265 batch 9), substr
//     stub (owner interp-strmanip.cpp::substr, roster 212 batch
//     7).
//   h015 wrap - hit: this file.
//   h017 naming - hit: match keeps the vendor name (the Kotlin
//     stdlib name colision avoided: no stdlib member shadows a
//     top-level fun; journal note only).
//   h020 pointer_handle - hit: the short pts[3][20]/ct[] matrix
//     seats on Array<IntArray>/IntArray carriers; the short*
//     rslt out-param folds to the Int return (the only caller
//     semantics are the 0/1 seat; journaled); the &ad[ptr]/
//     &bd[1] substr offsets seat through the offset pairs.

internal fun match(
    pts: Array<IntArray>, // short pts[3][20]
    lx: Int,
    ct: IntArray, // short ct[]
    ad: CharArray,
    bd: CharArray,
): Int { // the *rslt fold (0 fail / 1 match)
    val tmp = CharArray(2)
    val tmp1 = CharArray(1024)
    val tmp2 = CharArray(1024)
    var ptr = -1
    val adLen = strlenC(ad) // the strlen recompute fold (pure)

    for (i in 1..lx) {
        if (ct[i] == 0) continue
        when (bd[pts[1][i]]) {
            'C', 'c' -> {
                for (j in 1..ct[i]) {
                    ptr++
                    if (ptr > adLen) return 0
                    val ch = ad[ptr].code
                    if (ch < 32) continue
                    if (ch == 127) continue
                    return 0
                }
            }
            'N', 'n' -> {
                for (j in 1..ct[i]) {
                    ptr++
                    if (ptr > adLen) return 0
                    val ch = ad[ptr].code
                    if (ch >= '0'.code && ch <= '9'.code) continue
                    return 0
                }
            }
            'P', 'p' -> {
                for (j in 1..ct[i]) {
                    ptr++
                    if (ptr > adLen) return 0
                    tmp[0] = ad[ptr]
                    if (xindex(
                            " !\u0022#\u0024%&''()*+,-./" +
                                ":;<=>?@[\\]^_\u0060{|}~",
                            tmp,
                            1,
                        ) > 0
                    ) {
                        continue
                    }
                    return 0
                }
            }
            'A', 'a' -> {
                for (j in 1..ct[i]) {
                    ptr++
                    if (ptr > adLen) return 0
                    val ch = ad[ptr].code
                    if (ch >= 65 && ch <= 90) continue
                    if (ch >= 97 && ch <= 122) continue
                    return 0
                }
            }
            'L', 'l' -> {
                for (j in 1..ct[i]) {
                    ptr++
                    if (ptr > adLen) return 0
                    val ch = ad[ptr].code
                    if (ch >= 97 && ch <= 122) continue
                    return 0
                }
            }
            'U', 'u' -> {
                for (j in 1..ct[i]) {
                    ptr++
                    if (ptr > adLen) return 0
                    val ch = ad[ptr].code
                    if (ch >= 65 && ch <= 90) continue
                    return 0
                }
            }
            'E', 'e' -> {
                for (j in 1..ct[i]) {
                    ptr++
                    if (ptr > adLen) return 0
                    if (ad[ptr].code > 127) return 0
                    // the vendor break: the final ptr++ counts
                }
            }
            '"' -> {
                for (j in 1..ct[i]) {
                    if (pts[2][i] == 0) continue
                    if (ptr + pts[2][i] - 1 > adLen) return 0
                    ptr++
                    substr(ad, ptr, tmp1, 1, pts[2][i] - 1)
                    substr(bd, 1, tmp2, pts[1][i] + 1, pts[2][i] - 1)
                    if (strcmpC(tmp2, tmp1) != 0) return 0
                    ptr = ptr + strlenC(tmp1) - 1
                }
            }
            else -> return 0 // default: return
        }
    }

    if (ptr + 1 != adLen) return 0
    return 1 // *rslt = 1
}

// int xindex(const unsigned char *, const unsigned char *,
// short) - include/mumpsc/inline.h / strmanip family; body:
// interp-strmanip.cpp (the pattern-literal membership scan).
// Owner: interp-strmanip.cpp::xindex, roster unit 265 (batch 9,
// pending). TODO-stub (h014).
internal fun xindex(patternSet: String, s: CharArray, start: Int): Int {
    TODO("stub: owner interp-strmanip.cpp::xindex (roster 265, batch 9)")
}

// void substr(unsigned char *, unsigned char *, int, int) - the
// 1-based substring extract. Owner: interp-strmanip.cpp::substr,
// roster unit 212 (batch 7, pending). The input offset pairs
// seat the &ad[ptr] / &bd[1] call shapes. TODO-stub (h014).
internal fun substr(
    input: CharArray,
    inOff: Int,
    out: CharArray,
    start: Int,
    len: Int,
) {
    TODO("stub: owner interp-strmanip.cpp::substr (roster 212, batch 7)")
}

// int strcmp(char *, char *) - the fold of the vendor strcmp
// over the NUL-terminated CharArray seats (the strcmpC seat of
// the T1 renders reused).
private fun strcmpC(a: CharArray, b: CharArray): Int {
    var p = 0
    while (a[p] != '\u0000' && b[p] != '\u0000') {
        if (a[p] != b[p]) return a[p].code - b[p].code
        p++
    }
    return a[p].code - b[p].code
}
