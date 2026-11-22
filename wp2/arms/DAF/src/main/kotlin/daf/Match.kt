package daf

// Unit 180 (DAF B6 T3): interp-parse.cpp::match (86 LOC) - the pattern
// matcher for the structured walk family. Vendor signature
// void match(short pts[3][20], short lx, short ct[], unsigned char
// *ad, short *rslt, unsigned char *bd): pts is the 3x20 pattern table,
// ct[] the per-row repeat counts, bd the pattern text, ad the subject
// text; the short fold keeps Int rows (BASE-log row 5 consulted,
// hit-reuse: short arithmetic never overflows in pattern rows, the
// Int carrier preserves the -1 ptr sentinel exactly). *rslt folds
// onto IntCellD (row 54 carrier, hit-reuse). The odd ptr arithmetic is
// KEPT VERBATIM: ptr starts -1, every case does ptr++ and then reads
// ad[ptr], so the first consumed subject char is ad[0] (ledger note;
// the C loop shape with the negative sentinel is the vendor's, not
// rewritten to a 0-based walk). The E/e case has NO explicit
// continue on the pass branch - falls off the if to the loop foot,
// same effect as continue; ledger, not pinned. The '"' literal case
// runs the body ct[i] times (the j loop) but only when pts[2][i]!=0;
// with pt2==0 each iteration does NOTHING and the loop still spins
// ct[i] times (vendor quirk, kept). substr and strcmp seats:
// substrDaf raised below as a named-owner stub (interp-strmanip.cpp
// -substr, roster b7; compiler-sys1.c::substr is the b7 cousin);
// strcmp folds onto String == over cstring carriers (row 3 strlen
// family). The substr(&bd[1],...) call passes bd FROM OFFSET 1 (the
// vendor's &bd[1] quirk - bd[0] is skipped in the pattern-side slice
// source); kept via srcOff=1. xindex punct probe keeps the C-string
// shape: tmp CharArray(2) with tmp[1]=0, table walked from=1
// (xindexDaf(table, 0, tmp, 0, 1) > 0 seat, hit-reuse per Fcn.kt
// row 549). Dafny: EXEMPT (pattern-matrix walk over the 3x20 table;
// journal row).

private fun matchLenD(buf: CharArray): Int {
    // strlen fold (row 56 family): chars up to the first NUL
    var n = 0
    while (n < buf.size && buf[n] != '\u0000') n++
    return n
}

fun substrDaf(src: CharArray, srcOff: Int, out: CharArray, from: Int, len: Int): Unit =
    throw NotImplementedError(
        "substr(char *, char *, int, int): out-of-scope owner (interp-strmanip.cpp::substr at 345, roster b7; compiler-sys1.c::substr roster b7 cousin)")

fun matchD(pts: Array<IntArray>, lx: Int, ct: IntArray, ad: CharArray,
           rslt: IntCellD, bd: CharArray): Unit {
    val tmp = CharArray(2)
    val tmp1 = CharArray(1024)
    val tmp2 = CharArray(1024)
    var ptr = -1 // short ptr=(-1); negative sentinel kept verbatim
    rslt.v = 0
    tmp[1] = '\u0000' // vendor NUL
    for (i in 1..lx) {
        if (ct[i] == 0) continue
        when (bd[pts[1][i]]) {
            'C', 'c' -> {
                for (j in 1..ct[i]) {
                    ptr++
                    if (ptr > matchLenD(ad)) return
                    if (ad[ptr] < '\u0020') continue
                    if (ad[ptr] == '\u007F') continue
                    return
                }
            }
            'N', 'n' -> {
                for (j in 1..ct[i]) {
                    ptr++
                    if (ptr > matchLenD(ad)) return
                    if (ad[ptr] >= '0' && ad[ptr] <= '9') continue
                    return
                } // for j=...
            }
            'P', 'p' -> {
                for (j in 1..ct[i]) {
                    ptr++
                    if (ptr > matchLenD(ad)) return
                    tmp[0] = ad[ptr]
                    val punct = " !\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~".toCharArray() // vendor punct table
                    if (xindexDaf(punct, 0, tmp, 0, 1) > 0) continue
                    return
                } // for j=...
            }
            'A', 'a' -> {
                for (j in 1..ct[i]) {
                    ptr++
                    if (ptr > matchLenD(ad)) return
                    if (ad[ptr] >= 'A' && ad[ptr] <= 'Z') continue
                    if (ad[ptr] >= 'a' && ad[ptr] <= 'z') continue
                    return
                } // for j
            }
            'L', 'l' -> {
                for (j in 1..ct[i]) {
                    ptr++
                    if (ptr > matchLenD(ad)) return
                    if (ad[ptr] >= 'a' && ad[ptr] <= 'z') continue
                    return
                } // for j=...
            }
            'U', 'u' -> {
                for (j in 1..ct[i]) {
                    ptr++
                    if (ptr > matchLenD(ad)) return
                    if (ad[ptr] >= 'A' && ad[ptr] <= 'Z') continue
                    return
                } // for j=...
            }
            'E', 'e' -> {
                for (j in 1..ct[i]) {
                    ptr++
                    if (ptr > matchLenD(ad)) return
                    if (ad[ptr] > '\u007F') return
                    // (no explicit continue on pass; foot fall-through)
                }
            }
            '"' -> {
                for (j in 1..ct[i]) {
                    if (pts[2][i] == 0) continue
                    if (ptr + pts[2][i] - 1 > matchLenD(ad)) return
                    ptr++
                    substrDaf(ad, ptr, tmp1, 1, pts[2][i] - 1)
                    substrDaf(bd, 1, tmp2, pts[1][i] + 1, pts[2][i] - 1) // &bd[1] quirk
                    if (DafCRuntime.cstring(tmp2) != DafCRuntime.cstring(tmp1)) return
                    ptr = ptr + matchLenD(tmp1) - 1
                }
            }
            else -> return
        }
    } // for i=...
    if (ptr + 1 != matchLenD(ad)) return
    rslt.v = 1
    return
} // match()
