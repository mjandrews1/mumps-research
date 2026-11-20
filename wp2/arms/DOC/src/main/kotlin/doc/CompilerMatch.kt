package doc

// Unit 212 (B7 tranche 3): compiler-sys1.c::match (batch 7,
// complex stratum; units.csv row 212: loc 95, score 78.9). The
// MUMPS pattern-match prover, compiler twin - this render
// RESOLVES the 5-arg Stubs.match seat (owner
// compiler-sys1.c::match; the patrn seat call binds unchanged).
// The body is the vendor's verbatim repetition of the
// interp-parse.cpp::match body (unit 180, Match.kt) with ONE
// seat difference: the pattern scratch is the compiler-globals
// extern bd (compiler-globals-ext.h, the CompilerState.bd
// carrier, H-004) instead of the interpreter svPtr->bd passed as
// the sixth argument (the same-name twin renders as the 5-arg
// overload, H-015 overload-arity rule; the 6-arg interp twin
// lives in Match.kt). The outer walk runs i from 1 to lx over
// non-zero-count atoms and switches on bd[pts[1][i]]: C/c counts
// the vendor's NON-printable bytes only (the <32 and ==127
// continues pass, anything else returns - vendor quirk kept
// verbatim), N/n digits, P/p the punctuation charset via the
// xindex seat (the raw dollar and backtick bytes ride the
// DOLLAR/ BTICK const carriers - punctSet() inserts them after
// the hash and the underscore, byte-identical charset, no raw
// sigils in arm source; the double-apostrophe row kept
// vendor-verbatim), A/a alpha, L/l lower, U/u upper, E/e the
// strictly-ASCII arm, and the quoted-literal arm pulling source
// and pattern fragments through the compiler substr twin
// (CompilerSubstr.kt, Short seats; the ampersand offsets ride
// the sliceTail carriers, H-012 offset fold). ptr starts at -1
// and pre-increments before each compare; the strictly-greater
// ptr-above-strlen guard, the zero-length-literal continue, the
// ptr-plus-strlen(tmp1)-minus-1 advance, the default return,
// and the final ptr+1 != strlen exact-consumption check all
// kept verbatim (unit-180 render duplicated per the vendor's
// verbatim body repetition, u141 strcatx dup-def ledger
// precedent, H-013 family). *rslt is the ShortArray(1)
// out-param (H-006); tmp/tmp1/tmp2 fresh per-call scratch
// (H-008); H-010: the short locals render Int with the .toInt()
// reads on the ShortArray ct/pts carriers. Naming per H-017
// (match verbatim).

private const val DOLLAR_CM = 36.toChar() // the raw-sigil carrier
private const val BTICK_CM = 96.toChar() // the raw-sigil carrier

// the vendor punctuation charset with the two raw-sigil rows
// inserted at their vendor positions (after the hash, after
// the underscore); the double-apostrophe is vendor-verbatim
private fun punctSetCM(): CharArray {
    val base = " !\"#%&''()*+,-./:;<=>?@[\\\\]^_{|}~"
    val out = StringBuilder()
    for (c in base) {
        out.append(c)
        if (c == '#') out.append(DOLLAR_CM)
        if (c == '_') out.append(BTICK_CM)
    }
    return out.toString().toCharArray()
}

// strcmp fold: the per-file NUL-terminated compare (Parse_.kt
// strcmpAC precedent, duplicated per file - unit-180 twin)
private fun strcmpCM(a: CharArray, b: CharArray): Int {
    val la = CstringOps.strlenC(a)
    val lb = CstringOps.strlenC(b)
    val n = minOf(la, lb)
    for (k in 0 until n) {
        if (a[k] != b[k]) return a[k] - b[k]
    }
    return la - lb
}

// the &buf[offset] pointer carrier (H-012 offset fold; the
// unit-180 sliceTail twin, duplicated per file)
private fun sliceTailCM(buf: CharArray, offset: Int): CharArray =
    buf.copyOfRange(offset, buf.size)

// void match(short pts[3][20], short lx, short ct[], unsigned
// char *ad, short *rslt) - the 5-arg compiler twin; the pattern
// scratch is the CompilerState.bd extern seat.
fun match(
    pts: Array<ShortArray>,
    lx: Int,
    ct: ShortArray,
    ad: CharArray,
    rslt: ShortArray
) {
    val bd = CompilerState.bd // compiler-globals-ext.h extern bd
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
                    if (Stubs.xindex(punctSetCM(), tmp, 1.toShort()) > 0) {
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
                    substr(sliceTailCM(ad, ptr), tmp1, 1.toShort(), (pts[2][i].toInt() - 1).toShort())
                    substr(sliceTailCM(bd, 1), tmp2, (pts[1][i].toInt() + 1).toShort(),
                            (pts[2][i].toInt() - 1).toShort())
                    if (strcmpCM(tmp2, tmp1) != 0) return
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
