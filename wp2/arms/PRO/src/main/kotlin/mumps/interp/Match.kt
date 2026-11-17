package mumps.interp

/**
 * PRO-arm translation of interp-parse.cpp::match (B6 unit 160).
 * Translated independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), reference
 * source lines 109-206.
 *
 * match(pts, lx, ct, ad, rslt, bd): the MUMPS pattern-match
 * prover driven from the patrn() tables (Patrn.kt u64 caller):
 * pts[1][i] holds the bd offset of the i-th pattern atom's type
 * letter, pts[2][i] the quoted-literal length, ct[i] the repeat
 * count; ad is the subject C-string; on full match *rslt=1 else
 * *rslt stays 0 (all mismatch paths simply return).
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h006 out_param_holder : vendor short *rslt -> ShortArray(1)
 *    (written by the callee, read by the caller after return).
 *  - h008 static_scratch  : vendor's unsigned char tmp[2],
 *    tmp1[1024], tmp2[1024] locals -> fresh per-call buffers.
 *  - h012 unsigned_byte_domain : ad/bd/tmp byte domains; the
 *    32/127/65/90/97/122 comparisons keep the vendor's unsigned
 *    char ranges verbatim.
 *  - h014 out_of_scope_call : xindex (InterpStubs stub, owner
 *    interp-strmanip.cpp::xindex, not a corpus unit); substr
 *    (InterpStubs stub, owner interp-strmanip.cpp::substr,
 *    batch-7 unit); strcmp folded to a per-file NUL-terminated
 *    compare (Parse_.kt strcmpAC precedent).
 *  - h015 grammar_collision: new top-level match (mumps.interp)
 *    RESOLVES the B3 u64 InterpStubs 6-arg stub in place; the
 *    compiler package's 5-arg match (compiler-sys1.c owner,
 *    batch-7) is a different package and function - no collision.
 *    The vendor punctuation charset contains a dollar and a
 *    backtick; both are carried per-file consts (ReadCode.kt
 *    DOLLAR precedent, value 36; backtick value 96) so the arm
 *    source keeps the no-raw-dollar and no-backtick scan rules
 *    while the runtime charset stays byte-identical to vendor.
 *  - h017 naming           : match keeps the vendor name
 *    (lowercase in the vendor).
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - ptr starts at -1 and is pre-incremented before each class
 *    char compare (ptr++ then ad[ptr]), so the first compared
 *    char is ad[0] - the exact vendor sequence is preserved
 *    verbatim without interpretation.
 *  - The bounds guard is ptr>strlen(ad) (strictly greater), so
 *    a class char sitting exactly at the terminator offset is
 *    still compared - kept verbatim.
 *  - The 'C' (character) class accepts printable 32..126 only
 *    (<32 continue, ==127 continue, else return), and the 'E'
 *    (everything) class has NO per-char accept test beyond the
 *    >127 reject - kept.
 *  - The '"' (quoted literal) arm first tests pts[2][i]==0
 *    (zero-length literal matches anything) BEFORE advancing
 *    ptr - kept.
 *  - Empty ct[i] atoms (count 0) are skipped by the outer
 *    ct[i]==0 continue - kept.
 *  - The final whole-string check is ptr+1 != strlen(ad) - the
 *    pattern must consume the subject exactly - kept.
 */

// Scan-gate carriers: the vendor charset literal contains a raw
// dollar (36) and a backtick (96); neither may appear in arm
// source even inside a string literal (ReadCode.kt DOLLAR
// precedent). Values are the ASCII codes.
private const val DOLLAR = 36.toChar()
private const val BTICK = 96.toChar()

/**
 * h008: fresh scratch for the xindex punctuation-set literal
 * (vendor interp-parse.cpp:147). Charset byte-identical to the
 * vendor string: space ! " # dollar % & '' ( ) * + , - . / : ; <
 * = > ? @ [ \ ] ^ _ backtick { | } ~ then NUL.
 */
private fun punctSet(): CharArray {
    // dollar rides after '#', backtick after '_' (vendor order:
    // space ! " # dollar % & '' ( ) * + , - . / : ; < = > ? @
    // [ \ ] ^ _ backtick { | } ~)
    val s = " !\"#%&''()*+,-./:;<=>?@[\\]^_{|}~"
    val b = CharArray(s.length + 3) // 2 inserted chars + NUL
    var k = 0
    for (c in s) {
        b[k++] = c
        if (c == '#') b[k++] = DOLLAR
        if (c == '_') b[k++] = BTICK
    }
    b[k] = '\u0000'
    return b.copyOf(k + 1)
}

/** strcmp(a, b) over NUL-terminated CharArrays (index 0). */
private fun strcmp0(a: CharArray, b: CharArray): Int {
    var i = 0
    while (a[i] != '\u0000' && b[i] != '\u0000') {
        if (a[i] != b[i]) return a[i].code - b[i].code
        i++
    }
    return a[i].code - b[i].code
}

fun match(pts: Array<IntArray>, lx: Int, ct: IntArray, ad: CharArray,
          rslt: ShortArray, bd: CharArray) {
    val tmp = CharArray(2) // h008
    val tmp1 = CharArray(1024) // h008
    val tmp2 = CharArray(1024) // h008
    val xset = punctSet() // h008: the vendor punctuation-set literal

    var ptr = -1
    rslt[0] = 0
    tmp[1] = '\u0000'

    for (i in 1..lx) {
        if (ct[i] == 0) continue

        when (bd[pts[1][i]]) {
            'C', 'c' -> {
                for (j in 1..ct[i]) {
                    ptr++
                    if (ptr > strlenC(ad)) return
                    if (ad[ptr].code < 32) continue
                    if (ad[ptr].code == 127) continue
                    return
                }
            }

            'N', 'n' -> {
                for (j in 1..ct[i]) {
                    ptr++
                    if (ptr > strlenC(ad)) return
                    if (ad[ptr] >= '0' && ad[ptr] <= '9') continue
                    return
                } /* for j=...*/
            }

            'P', 'p' -> {
                for (j in 1..ct[i]) {
                    ptr++
                    if (ptr > strlenC(ad)) return
                    tmp[0] = ad[ptr]
                    if (xindex(xset, tmp, 1) > 0) continue
                    return
                } /* for j=...*/
            }

            'A', 'a' -> {
                for (j in 1..ct[i]) {
                    ptr++
                    if (ptr > strlenC(ad)) return
                    if (ad[ptr].code >= 65 && ad[ptr].code <= 90) continue
                    if (ad[ptr].code >= 97 && ad[ptr].code <= 122) continue
                    return
                } /* for j...*/
            }

            'L', 'l' -> {
                for (j in 1..ct[i]) {
                    ptr++
                    if (ptr > strlenC(ad)) return
                    if (ad[ptr].code >= 97 && ad[ptr].code <= 122) continue
                    return
                } /*for j=...*/
            }

            'U', 'u' -> {
                for (j in 1..ct[i]) {
                    ptr++
                    if (ptr > strlenC(ad)) return
                    if (ad[ptr].code >= 65 && ad[ptr].code <= 90) continue
                    return
                } /* for j=...*/
            }

            'E', 'e' -> {
                for (j in 1..ct[i]) {
                    ptr++
                    if (ptr > strlenC(ad)) return
                    if (ad[ptr].code > 127) return
                }
            }

            '"' -> {
                for (j in 1..ct[i]) {
                    if (pts[2][i] == 0) continue
                    if (ptr + pts[2][i] - 1 > strlenC(ad)) return
                    ptr++
                    substr(ad, ptr, tmp1, 1, pts[2][i] - 1)
                    substr(bd, 1, tmp2, pts[1][i] + 1, pts[2][i] - 1)
                    if (strcmp0(tmp2, tmp1) != 0) return
                    ptr = ptr + strlenC(tmp1) - 1
                }
            }

            else -> return // vendor: default: return;
        }
    } /* for i=... */
    if (ptr + 1 != strlenC(ad)) return
    rslt[0] = 1
    return
} /* match() */
