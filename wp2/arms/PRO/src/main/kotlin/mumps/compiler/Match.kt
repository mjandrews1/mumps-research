package mumps.compiler

import mumps.interp.NUL
import mumps.interp.xindex

/**
 * PRO-arm translation of compiler-sys1.c::match (B7 tranche 2,
 * unit 190, rank 260). Translated independently from the pristine
 * vendor source (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe),
 * reference source lines 453-562 (K&R definition at line 453).
 *
 * Identity verified against the study tool: compiler-sys1.c::match,
 * c, loc 95, comment_ratio 0.021, pointer_marks 4,
 * control_marks 52, score 78.9 - exact match to
 * wp1/study-subset/units.csv row 190 (batch 7). RESOLVES the
 * Stubs.kt match h014 stub (raised by B3 unit 68; the Patrn.kt
 * call sites bind unchanged on the same signature).
 *
 * match(pts, lx, ct, ad, rslt): the pattern-match verifier - walks
 * the compiled pattern code table (pts[1] = code positions into
 * bd, pts[2] = literal lengths, ct = repetition counts) against
 * the target string ad; sets rslt[0] = 1 only if every element
 * matches AND the whole string is consumed (ptr + 1 == strlen).
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h003 state_macro      : bd[] is the compiler file-scope
 *    global (compiler-globals-ext.h:33; CompilerState.bd, h012
 *    byte domain).
 *  - h006 out_param_bundle : rslt is the short* out-parameter
 *    (the Patrn jH holder - one ShortArray cell, stub-era shape
 *    kept).
 *  - h007 char_buffer      : ad/tmp/tmp1/tmp2 are NUL-terminated
 *    byte buffers (h012 domain); tmp is the 1-char xindex subject
 *    with tmp[1] = NUL per the vendor.
 *  - h012 unsigned_byte_domain: all ad/bd reads ride Char code
 *    points; the 'E' arm's > 127 test and the control-char < 32
 *    test are byte-domain comparisons.
 *  - h013 dead_store       : none (ptr/i/j all live).
 *  - h014 out_of_scope_call: substr (Substr.kt, B7 unit 213 - the
 *    &ad[ptr]/&bd[1] caller pointers fold into its srcOff seat,
 *    GlobalQuery precedent); xindex (InterpStubs, owner
 *    interp-strmanip.cpp::xindex, not a corpus unit) reused;
 *    strlen via the compiler strlenC (CompilerCstring.kt:55).
 *  - h015 grammar_collision: the vendor punctuation literal
 *    contains raw quote/dollar/backslash/backtick bytes - rebuilt
 *    as charArrayOf with DOLLAR = 36.toChar() and BACKTICK =
 *    96.toChar() (u34/u39 precedent; no raw backtick in the
 *    file); private strcmpC (no shared seat); new top-level match
 *    re-binds the stub seat - Patrn binds unchanged.
 *  - h017 naming           : match keeps the vendor name.
 *  - h018 reference_build_visibility: no ifdefs in this body.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The PUNCT literal carries TWO apostrophes ('' - the vendor's
 *    spelling, kept verbatim in PUNCT_CHARS).
 *  - strlen(ad) is re-evaluated inside every j loop (the vendor
 *    never hoists it) - kept.
 *  - The 'C' arm treats ad[ptr] == 127 (DEL) as control - kept.
 *  - The quote-literal arm does NOT test ptr against strlen before
 *    the first increment (the ptr + pts[2][i] - 1 > strlen test
 *    is the only bound) - kept.
 */

private val DOLLAR = 36.toChar()
private val BACKTICK = 96.toChar()

/** vendor 502: the xindex punctuation set (raw bytes rebuilt, h015). */
private val PUNCT_CHARS = charArrayOf(
    ' ', '!', '"', '#', DOLLAR, '%', '&', '\'', '\'',
    '(', ')', '*', '+', ',', '-', '.', '/', ':', ';',
    '<', '=', '>', '?', '@', '[', '\\', ']', '^', '_',
    BACKTICK, '{', '|', '}', '~'
)

/** private strcmp (no shared compiler seat). */
private fun strcmpC(a: CharArray, b: CharArray): Int {
    var i = 0
    while (a[i] != NUL || b[i] != NUL) {
        if (a[i] != b[i]) return a[i] - b[i]
        i++
    }
    return 0
}

/**
 * vendor 453: void match(pts, lx, ct, ad, rslt). pts is the
 * [3][20] short pattern table (IntArray rows on the stub-era
 * signature); ct the per-element repetition counts; ad the target
 * string; rslt the short out-parameter.
 */
fun match(pts: Array<IntArray>, lx: Int, ct: IntArray, ad: CharArray, rslt: ShortArray) {
    val st = CompilerState
    val tmp = CharArray(2)
    val tmp1 = CharArray(1024)
    val tmp2 = CharArray(1024)
    var ptr = -1
    rslt[0] = 0
    tmp[1] = NUL

    for (i in 1..lx) {
        if (ct[i] == 0) continue

        when (st.bd[pts[1][i]]) {

            'C', 'c' -> {
                for (j in 1..ct[i]) {
                    ptr++
                    if (ptr > strlenC(ad)) return
                    if (ad[ptr] < ' ') continue
                    if (ad[ptr] == 127.toChar()) continue
                    return
                }
            }

            'N', 'n' -> {
                for (j in 1..ct[i]) {
                    ptr++
                    if (ptr > strlenC(ad)) return
                    if (ad[ptr] >= '0' && ad[ptr] <= '9') continue
                    return
                } // for j=...
            }

            'P', 'p' -> {
                for (j in 1..ct[i]) {
                    ptr++
                    if (ptr > strlenC(ad)) return
                    tmp[0] = ad[ptr]
                    if (xindex(PUNCT_CHARS, tmp, 1) > 0) continue
                    return
                } // for j=...
            }

            'A', 'a' -> {
                for (j in 1..ct[i]) {
                    ptr++
                    if (ptr > strlenC(ad)) return
                    if (ad[ptr] >= 'A' && ad[ptr] <= 'Z') continue
                    if (ad[ptr] >= 'a' && ad[ptr] <= 'z') continue
                    return
                } // for j...
            }

            'L', 'l' -> {
                for (j in 1..ct[i]) {
                    ptr++
                    if (ptr > strlenC(ad)) return
                    if (ad[ptr] >= 'a' && ad[ptr] <= 'z') continue
                    return
                } // for j=...
            }

            'U', 'u' -> {
                for (j in 1..ct[i]) {
                    ptr++
                    if (ptr > strlenC(ad)) return
                    if (ad[ptr] >= 'A' && ad[ptr] <= 'Z') continue
                    return
                } // for j=...
            }

            'E', 'e' -> {
                for (j in 1..ct[i]) {
                    ptr++
                    if (ptr > strlenC(ad)) return
                    if (ad[ptr] > 127.toChar()) return
                }
            }

            '"' -> {
                for (j in 1..ct[i]) {
                    if (pts[2][i] == 0) continue
                    if (ptr + pts[2][i] - 1 > strlenC(ad)) return
                    ptr++
                    substr(ad, ptr, tmp1, 1, pts[2][i] - 1)
                    substr(st.bd, 1, tmp2, pts[1][i] + 1, pts[2][i] - 1)
                    if (strcmpC(tmp2, tmp1) != 0) return
                    ptr = ptr + strlenC(tmp1) - 1
                }
            }

            else -> return
        }
    } // for i=...

    if (ptr + 1 != strlenC(ad)) return
    rslt[0] = 1
    return
} // match()
