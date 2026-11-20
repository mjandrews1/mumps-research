package doc

// Unit 180 (B6 tranche 3): interp-parse.cpp::match (batch 6,
// complex stratum; roster row 180: loc 86, score 82.0, batch_plan
// strata order). The MUMPS pattern-match prover - this render
// RESOLVES the 6-arg Stubs.match seat (owner
// interp-parse.cpp::match; the patrn seat call binds
// unchanged). The outer walk runs i from 1 to lx, skipping
// zero-count pattern atoms, and switches on bd[pts[1][i]]:
// the C/c arm counts the vendor's NON-printable bytes only
// (the <32 and ==127 continues pass, anything else returns -
// vendor quirk kept verbatim), N/n counts digits, P/p counts
// the vendor punctuation charset via the xindex seat (the
// charset literal carries the raw dollar and backtick bytes -
// it is built by punctSet() with the DOLLAR and BTICK const
// carriers inserted after the hash and the underscore so the
// runtime charset stays byte-identical while the arm source
// keeps the no-raw-sigil rule; the vendor double-apostrophe
// row kept verbatim), A/a alpha, L/l lower, U/u upper, E/e the
// strictly-ASCII reject-and-count arm, and the quoted-literal
// arm pulling the source and pattern fragments through the
// substr seat (the ampersand-offset args ride the slice
// carriers, the H-012 offset fold). ptr starts at -1 and
// pre-increments before each compare; the strictly-greater
// ptr-above-strlen bounds guard, the zero-length-literal
// continue, the ptr-plus-strlen(tmp1)-minus-1 advance, the
// default return, and the final ptr+1 != strlen general
// exact-consumption check all kept verbatim. *rslt is the
// ShortArray(1) out-param (H-006); tmp/tmp1/tmp2 are fresh
// per-call scratch (H-008); strlen rides CstringOps.strlenC;
// strcmp folds to the per-file NUL-terminated compare (the
// Parse_.kt strcmpAC precedent, H-015). Naming per H-017.

private const val DOLLAR_MT = 36.toChar() // the raw-sigil carrier
private const val BTICK_MT = 96.toChar() // the raw-sigil carrier

// the vendor punctuation charset with the two raw-sigil rows
// inserted at their vendor positions (after the hash, after
// the underscore); the double-apostrophe is vendor-verbatim
private fun punctSet(): CharArray {
    val base = " !\"#%&''()*+,-./:;<=>?@[\\\\]^_{|}~"
    val out = StringBuilder()
    for (c in base) {
        out.append(c)
        if (c == '#') out.append(DOLLAR_MT)
        if (c == '_') out.append(BTICK_MT)
    }
    return out.toString().toCharArray()
}

// strcmp fold: the per-file NUL-terminated compare (Parse_.kt
// strcmpAC precedent)
private fun strcmpC(a: CharArray, b: CharArray): Int {
    val la = CstringOps.strlenC(a)
    val lb = CstringOps.strlenC(b)
    val n = minOf(la, lb)
    for (k in 0 until n) {
        if (a[k] != b[k]) return a[k] - b[k]
    }
    return la - lb
}

fun match(
    pts: Array<ShortArray>,
    lx: Int,
    ct: ShortArray,
    ad: CharArray,
    rslt: ShortArray,
    bd: CharArray
) {
    val tmp = CharArray(2)      // tmp[1]=0 sentinel (vendor)
    val tmp1 = CharArray(1024)  // the source fragment
    val tmp2 = CharArray(1024) // the pattern fragment
    var ptr = -1
    var i: Int
    var j: Int

    rslt[0] = 0
    tmp[1] = NUL

    i = 1
    while (i <= lx) {
        if (ct[i].toInt() == 0) {
            i++
            continue
        }
        when (bd[pts[1][i].toInt()]) {
            'C', 'c' -> {
                j = 1
                while (j <= ct[i].toInt()) {
                    ptr++
                    if (ptr > CstringOps.strlenC(ad)) return
                    if (ad[ptr] < 32.toChar()) {
                        j++
                        continue
                    }
                    if (ad[ptr] == 127.toChar()) {
                        j++
                        continue
                    }
                    return
                }
            }

            'N', 'n' -> {
                j = 1
                while (j <= ct[i].toInt()) {
                    ptr++
                    if (ptr > CstringOps.strlenC(ad)) return
                    if (ad[ptr] >= '0' && ad[ptr] <= '9') {
                        j++
                        continue
                    }
                    return
                }
            }

            'P', 'p' -> {
                j = 1
                while (j <= ct[i].toInt()) {
                    ptr++
                    if (ptr > CstringOps.strlenC(ad)) return
                    tmp[0] = ad[ptr]
                    if (Stubs.xindex(punctSet(), tmp, 1.toShort()) > 0) {
                        j++
                        continue
                    }
                    return
                }
            }

            'A', 'a' -> {
                j = 1
                while (j <= ct[i].toInt()) {
                    ptr++
                    if (ptr > CstringOps.strlenC(ad)) return
                    if (ad[ptr] >= 65.toChar() && ad[ptr] <= 90.toChar()) {
                        j++
                        continue
                    }
                    if (ad[ptr] >= 97.toChar() && ad[ptr] <= 122.toChar()) {
                        j++
                        continue
                    }
                    return
                }
            }

            'L', 'l' -> {
                j = 1
                while (j <= ct[i].toInt()) {
                    ptr++
                    if (ptr > CstringOps.strlenC(ad)) return
                    if (ad[ptr] >= 97.toChar() && ad[ptr] <= 122.toChar()) {
                        j++
                        continue
                    }
                    return
                }
            }

            'U', 'u' -> {
                j = 1
                while (j <= ct[i].toInt()) {
                    ptr++
                    if (ptr > CstringOps.strlenC(ad)) return
                    if (ad[ptr] >= 65.toChar() && ad[ptr] <= 90.toChar()) {
                        j++
                        continue
                    }
                    return
                }
            }

            'E', 'e' -> {
                j = 1
                while (j <= ct[i].toInt()) {
                    ptr++
                    if (ptr > CstringOps.strlenC(ad)) return
                    if (ad[ptr] > 127.toChar()) return
                    j++
                }
            }

            '"' -> { // the quoted-literal arm
                j = 1
                while (j <= ct[i].toInt()) {
                    if (pts[2][i].toInt() == 0) {
                        j++
                        continue
                    }
                    if (ptr + pts[2][i].toInt() - 1 > CstringOps.strlenC(ad)) return
                    ptr++
                    // substr(&ad[ptr], tmp1, 1, pts[2][i]-1): the
                    // ampersand-offset rides the slice carrier
                    substr(sliceTailMT(ad, ptr), tmp1, 1.toShort(), (pts[2][i].toInt() - 1).toShort())
                    substr(sliceTailMT(bd, 1), tmp2, (pts[1][i].toInt() + 1).toShort(),
                            (pts[2][i].toInt() - 1).toShort())
                    if (strcmpC(tmp2, tmp1) != 0) return
                    ptr = ptr + CstringOps.strlenC(tmp1) - 1
                    j++
                }
            }

            else -> return
        }
        i++
    }

    if (ptr + 1 != CstringOps.strlenC(ad)) return
    rslt[0] = 1
}

// the &buf[offset] pointer carrier (H-012 offset fold)
private fun sliceTailMT(buf: CharArray, offset: Int): CharArray =
    buf.copyOfRange(offset, buf.size)
