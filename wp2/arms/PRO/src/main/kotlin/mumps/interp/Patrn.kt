package mumps.interp

import mumps.mdh.MdhState // (build fix) the file-scope globals ride the MdhState object (MdhStubs.kt), not an MdhStubs object

/**
 * PRO-arm translation of interp-parse.cpp::patrn (pilot B3, unit 64,
 * rank 275, provisional under deviation C-003). Translated
 * independently from the pristine vendor source (mumps-14-may-2026.tgz,
 * sha256 621deb25...ffdbe), reference source lines 1381-1451 (study
 * tool: loc 71, score 106.9, exact units.csv match).
 *
 * patrn(ad, svPtr): the fixed-string pattern matcher's argument
 * scanner - walks svPtr.bd building the pts/count/high/low tables for
 * match(), then either delegates (definite patterns) or runs the
 * indefinite-pattern odometer loop over ct2/stk retrying match()
 * until success or exhaustion. Results report via the libmpscpp.cpp
 * file-scope global ierr1 (h004: MdhState.ierr1, B1 unit 31 owner).
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h011 goto : the vendor's a108 label becomes an inner while over
 *    the quoted-literal scan; the retry/retry1/recount odometer web
 *    becomes one loop whose head increments ct2[stk[1]] (the retry:
 *    entry) with a first-iteration skip for the goto retry1 entry;
 *    recount is an inner while. The C for-continue semantics (the
 *    loop increment runs on continue) are made explicit with
 *    i++ before each continue.
 *  - h010 promotions : vendor short locals (iptr,k,j,i,m,lx,stx,
 *    lad, the pts/count/ct2/stk/high/low tables) are Int.
 *  - h012 unsigned_byte_domain : bd/ad/tmp byte domains; the 210
 *    doubled-quote marker compares as 210.toChar().
 *  - h008 static_scratch : the xindex literal arguments "cnpalue"/
 *    "CNPALUE" become NUL-terminated top-level char arrays.
 *  - h013 dead_store : iptr = stx is stored and never read (kept as
 *    a comment, vendor line 1457).
 *  - h014 out_of_scope_call : match (interp-parse.cpp::match,
 *    batch-6 unit), substr (interp-strmanip.cpp::substr, batch-7
 *    unit), cannon (interp-parse.cpp::<misc>, B3 unit 75 - same
 *    tranche; owner lands with this push) - InterpStubs owner-named
 *    stubs; xindex is the existing stub; atoi -> atoiC (CstringOps).
 *  - h015 grammar_collision : private helpers; no dollar bytes.
 *  - h017 naming : patrn keeps the vendor name.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The scan reads bd[0] too (the patrn1 append base), so the loop
 *    bound starts at i = 0 and the substr calls read &bd[1] with
 *    original indices j..i-j - both kept exactly.
 *  - The vendor continues the for loop after the xindex letter test
 *    without consuming bd[i]; the next iteration re-reads the same
 *    character class check on the SAME bd[i] (the operator character
 *    is rescanned as the next token's start) - rendered by the
 *    explicit i++ before continue.
 */

// xindex literal arguments (vendor line 1426-1427), h008
private val CNPALUE = "cnpalue".toCharArray() + NUL
private val CNPALUE_U = "CNPALUE".toCharArray() + NUL
// doubled-quote marker from patrn1 (vendor 210)
private val MARK210 = 210.toChar()

fun patrn(ad: CharArray, svPtr: Msv) {
    var indef: Int // vendor unsigned char flag
    val tmp = CharArray(10)
    var iptr: Int
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
        if (i > strlenC(svPtr.bd, 0)) break

        lx++
        if (svPtr.bd[i] < '0' || svPtr.bd[i] > '9') {
            if (svPtr.bd[i] != '.') i--
            count[lx] = -1
            indef = 1
            i++
            if (svPtr.bd[i] >= '0' && svPtr.bd[i] <= '9') {
                j = i
                while (svPtr.bd[i] >= '0' && svPtr.bd[i] <= '9') i++
                substr(svPtr.bd, 1, tmp, j, i - j)
                cannon(tmp)
                high[lx] = atoiC(tmp)
            }
        } else {
            j = i
            while (svPtr.bd[i] >= '0' && svPtr.bd[i] <= '9') i++
            substr(svPtr.bd, 1, tmp, j, i - j)
            cannon(tmp)
            count[lx] = atoiC(tmp)
            if (svPtr.bd[i] == '.') {
                low[lx] = count[lx]
                count[lx] = -1
                indef = 1
                i++
                if (svPtr.bd[i] >= '0' && svPtr.bd[i] <= '9') {
                    j = i
                    while (svPtr.bd[i] >= '0' && svPtr.bd[i] <= '9') i++
                    substr(svPtr.bd, 1, tmp, j, i - j)
                    cannon(tmp)
                    high[lx] = atoiC(tmp)
                }
            }
        }

        if (svPtr.bd[i] != '"') {
            tmp[0] = svPtr.bd[i]
            tmp[1] = NUL
            if (xindex(CNPALUE, tmp, 1) == 0 &&
                xindex(CNPALUE_U, tmp, 1) == 0
            ) {
                // goto err
                MdhState.ierr1 = 99
                return
            }
            pts[1][lx] = i
            pts[2][lx] = 1
            i++ // C for-continue runs the increment
            continue@scan
        }

        j = i
        if (svPtr.bd[j + 1] == '"') {
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
            if (svPtr.bd[j] == NUL) {
                // goto err
                MdhState.ierr1 = 99
                return
            }
            if (svPtr.bd[j] != '"') {
                if (svPtr.bd[j] == MARK210) svPtr.bd[j] = '"'
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
        match(pts, lx, count, ad, jH, svPtr.bd)
        j = jH[0].toInt()
        MdhState.ierr1 = j.toShort()
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
    // iptr = stx; // h013: stored and never read (vendor line 1457)

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
                MdhState.ierr1 = 0
                return
            }
            ct2[stk[m]]++

            // recount:
            while (ct2[stk[m]] > lad || ct2[stk[m]] > high[stk[m]]) {
                if (m >= stx) {
                    MdhState.ierr1 = 0
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

        match(pts, lx, ct2, ad, jH, svPtr.bd)
        j = jH[0].toInt()
        MdhState.ierr1 = j.toShort()
        if (j == 1) return
        // goto retry
    }
}
