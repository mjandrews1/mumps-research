package mumps.compiler

import mumps.interp.NUL
import mumps.interp.atoiC
import mumps.interp.strlenC
import mumps.interp.xindex

/**
 * PRO-arm translation of compiler-sys1.c::patrn (B3 unit 68, rank
 * 244). Translated independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), reference source
 * lines 104-263 (K&R-style definition at line 104).
 *
 * Identity verified against the study tool (tools/select_study_
 * subset.py): compiler-sys1.c::patrn, c, loc 127, comment_ratio
 * 0.008, pointer_marks 0, control_marks 35, score 56.5, batch 3 -
 * exact match to wp1/study-subset/units.csv row 244.
 *
 * patrn(ad): the compiler-side pattern matcher - scans the pattern
 * text in bd[1..] (built by patrn1, unit 26), parses the repeat
 * counts (fixed, low.high ranges) and literal atoms, then matches
 * against ad, setting ierr1 (1 match, 0 no match, 99 syntax error).
 * This is the compiler twin of interp-parse.cpp::patrn (unit 64,
 * translated this batch); the vendor bodies are textually near-
 * identical and the port mirrors the unit-64 shape on the compiler
 * state (CompilerState.bd/ierr1 instead of Msv.bd/MdhStubs.ierr1).
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h004 globals_ext          : bd, ierr1 -> CompilerState
 *    (compiler-globals-ext.h:33/:48; bd owner unit 26).
 *  - h006 out_param_holder     : match's short* rslt -> ShortArray(1)
 *    holder (jH).
 *  - h010 numeric_promotion    : vendor shorts (i, j, k, lx, ct2,
 *    count, stk, high, low, pts) ride Int locals with no practical
 *    overflow; MARK210 = Char(210).
 *  - h011 goto                 : a108 backward goto -> inner while;
 *    retry/retry1/recount odometer -> single labeled loop with an
 *    atRetry flag (first entry at retry1 skips the increment);
 *    goto err -> inline ierr1=99 + return.
 *  - h012 unsigned_byte_domain  : bd/ad/tmp byte domains.
 *  - h013 dead_store           : iptr = stx (vendor line 158) is
 *    stored and never read - kept as a comment, not a live store.
 *  - h014 out_of_scope_call    : substr (compiler-sys1.c:396,
 *    batch-7 unit), match (compiler-sys1.c:453, batch-7 unit) and
 *    cannon (compiler-sys1.c:971 = B3 unit 72, this batch) ->
 *    owner-named stubs in Stubs.kt; xindex (interp-strmanip.cpp,
 *    InterpStubs stub) reused via package import; atoi -> atoiC.
 *  - h015 grammar_collision    : pre-push scan incl. cross-file
 *    top-level names: new top-level patrn in mumps.compiler (the
 *    mumps.interp patrn of unit 64 is a different package member);
 *    private CNPALUE/CNPALUE_U vals are file-local.
 *  - h017 naming              : patrn keeps the vendor name.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The xindex literal "cnpalue"/"CNPALUE" pair (line 157-158) is
 *    tested in that order with the same tmp atom - kept exactly.
 *  - The 210 marker (MARK210) inside a quoted atom is rewritten in
 *    place to '"' before the closing-quote test re-runs - kept.
 *  - The C for-continue semantics (i++ before every continue) are
 *    made explicit before each continue@scan.
 *  - Vendor divergence from the interp twin (unit 64): the compiler
 *    scans "for (i = 1; i <= strlen(&bd[1]); i++)" while interp-
 *    parse.cpp::patrn uses "i <= strlen(&svPtr->bd[0])" - each port
 *    keeps its own vendor bound (offset 1 here, offset 0 in unit 64).
 */
private val CNPALUE = charArrayOf(
    'c', 'n', 'p', 'a', 'l', 'u', 'e', NUL
)

private val CNPALUE_U = charArrayOf(
    'C', 'N', 'P', 'A', 'L', 'U', 'E', NUL
)

private val MARK210 = 210.toChar()

fun patrn(ad: CharArray) {
    var indef: Int // vendor unsigned char flag
    val tmp = CharArray(10)
    var k: Int
    var j: Int
    var i: Int
    var m: Int
    var lx: Int
    var stx: Int
    var lad: Int
    val pts = Array(3) { IntArray(20) } // vendor short pts[3][20]
    val count = IntArray(20)
    val ct2 = IntArray(20)
    val stk = IntArray(20)
    val high = IntArray(20)
    val low = IntArray(20)
    val jH = ShortArray(1) // match() rslt out-param holder (h006)

    indef = 0
    lx = 0
    lad = strlenC(ad)
    for (t in 0 until 20) {
        high[t] = lad
        low[t] = 0
    }

    i = 0
    scan@ while (true) {
        if (i > strlenC(CompilerState.bd, 1)) break

        lx++
        if (CompilerState.bd[i] < '0' || CompilerState.bd[i] > '9') {
            if (CompilerState.bd[i] != '.') i--
            count[lx] = -1
            indef = 1
            i++
            if (CompilerState.bd[i] >= '0' && CompilerState.bd[i] <= '9') {
                j = i
                while (CompilerState.bd[i] >= '0' && CompilerState.bd[i] <= '9') i++
                substr(CompilerState.bd, 1, tmp, j, i - j)
                cannon(tmp)
                high[lx] = atoiC(tmp)
            }
        } else {
            j = i
            while (CompilerState.bd[i] >= '0' && CompilerState.bd[i] <= '9') i++
            substr(CompilerState.bd, 1, tmp, j, i - j)
            cannon(tmp)
            count[lx] = atoiC(tmp)
            if (CompilerState.bd[i] == '.') {
                low[lx] = count[lx]
                count[lx] = -1
                indef = 1
                i++
                if (CompilerState.bd[i] >= '0' && CompilerState.bd[i] <= '9') {
                    j = i
                    while (CompilerState.bd[i] >= '0' && CompilerState.bd[i] <= '9') i++
                    substr(CompilerState.bd, 1, tmp, j, i - j)
                    cannon(tmp)
                    high[lx] = atoiC(tmp)
                }
            }
        }

        if (CompilerState.bd[i] != '"') {
            tmp[0] = CompilerState.bd[i]
            tmp[1] = NUL
            if (xindex(CNPALUE, tmp, 1) == 0 &&
                xindex(CNPALUE_U, tmp, 1) == 0
            ) {
                // goto err
                CompilerState.ierr1 = 99
                return
            }
            pts[1][lx] = i
            pts[2][lx] = 1
            i++ // C for-continue runs the increment
            continue@scan
        }

        j = i
        if (CompilerState.bd[j + 1] == '"') {
            j++
            pts[1][lx] = i
            pts[2][lx] = 0
            i = j
            i++ // C for-continue runs the increment
            continue@scan
        }

        // a108:
        var a108 = true
        while (a108) {
            j++
            if (CompilerState.bd[j] == NUL) {
                // goto err
                CompilerState.ierr1 = 99
                return
            }
            if (CompilerState.bd[j] != '"') {
                if (CompilerState.bd[j] == MARK210) CompilerState.bd[j] = '"'
                a108 = true // goto a108
            } else {
                pts[1][lx] = i
                pts[2][lx] = j - i
                i = j
                a108 = false
            }
        }

        i++ // C for-loop increment
    }

    if (indef != 1) {
        match(pts, lx, count, ad, jH)
        j = jH[0].toInt()
        CompilerState.ierr1 = j.toShort()
        return
    }

    stx = 0
    for (t in 1..lx) {
        if (count[t] < 0) {
            stx++
            stk[stx] = t
        }
    }
    for (t in 1..lx) {
        ct2[t] = if (count[t] >= 0) count[t] else low[t]
    }
    // iptr = stx; // h013: stored and never read (vendor line 158)

    // retry:/retry1: odometer - entered at retry1 (no increment on
    // the first pass); every later pass falls in at retry (with the
    // ct2[stk[1]]++ increment)
    var atRetry = false // first entry is goto retry1
    odometer@ while (true) {
        if (atRetry) ct2[stk[1]]++
        atRetry = true

        if (ct2[stk[1]] > lad || ct2[stk[1]] > high[stk[1]]) {
            ct2[stk[1]] = low[stk[1]]
            m = 2
            if (stx == 1) {
                CompilerState.ierr1 = 0
                return
            }
            ct2[stk[m]]++

            // recount:
            while (ct2[stk[m]] > lad || ct2[stk[m]] > high[stk[m]]) {
                if (m >= stx) {
                    CompilerState.ierr1 = 0
                    return
                }
                ct2[stk[m]] = low[stk[m]]
                m++
                ct2[stk[m]]++
            }
        }

        k = 0
        for (t in 1..lx) k = k + ct2[t]
        if (k > lad) continue@odometer // goto retry

        match(pts, lx, ct2, ad, jH)
        j = jH[0].toInt()
        CompilerState.ierr1 = j.toShort()
        if (j == 1) return
        // goto retry
    }
}
