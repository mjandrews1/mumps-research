package daf

// R1 stub-completion (2026-11-27, C-020 erratum session, tranche 4):
// libmpscpp.cpp::prnt (34 LOC, roster row 50, core): the
// Smith-Waterman traceback walker: from the (imax,jmax) peak
// cell, walks the __a matrix backwards, prepending each aligned
// pair onto S_RESULT (the diagonal arm appends s[imax]t[jmax],
// the s-gap arm appends s[imax]-, the t-gap arm appends
// -t[jmax]) until the score chain leaves the matrix (a 0 or
// EMPTY_CELL boundary).
// NOTE (C-020 erratum): the C-018 ledger had misattributed this
// row's DAF evidence to the prnterrDaf stub (name-matcher
// substring pathology, prnt vs prnterr); the claim-based
// re-verification demoted it to stub-only with no prior DAF
// seat - THIS render is the first DAF seat of the unit.
// Consults (2026-11-27T00:00:00Z, nominal): BASE-log rows 6/22
// hit-reuse (the __a matrix and S_RESULT ride the
// SmithWatermanD singleton of InitSmithWaterman.kt - the
// file-scope extern fold; EMPTY_CELL_D is the same-seat
// constant); row 2 family hit-reuse (the inline Score helper
// resolves to scoreCharD of LibmpscppMisc8.kt, the released
// seat reading the DafSwScore carrier - the raised swD stub is
// the assigner, called exactly as the landed Smith-Waterman
// siblings call it); rows 81/56 hit-adapt (char *s/char *t
// fold onto the String carriers of the sw call domain - the
// SmithWatermanW seat passes its 1-padded s1a/s2a strings).
// Vendor quirks kept: (1) the ss parameter is dead in the body
// (the sw caller passes 0) - the parameter stays; (2) the
// traceback PREPENDS onto S_RESULT by string concatenation
// (tmp[0]/tmp[1] then S_RESULT=S_RESULT+tmp) - the Kotlin
// fold appends to the running string in the same order, the
// vendor character order preserved; (3) the char tmp[16]
// staging buffer folds onto the string template (no fixed
// bound - the two-char staging never overflowed it vendor-
// side); (4) the loop has no bounds guard beyond the 0 and
// EMPTY_CELL cell tests - kept verbatim (the init() fill
// guarantees a boundary hit; an inconsistent hand-built matrix
// spins, the vendor loop spins identically, smoke-verified).
// Dafny: EXEMPT (matrix-walk over the singleton carrier; the
// sw family is IO-adjacent; journal row).

fun prntD(ss: Int, s: String, t: String, imaxIn: Int, jmaxIn: Int): Int {

    var imax = imaxIn
    var jmax = jmaxIn

    while (true) {

        if (imax <= 0 || jmax <= 0) break

        val cell = SmithWatermanD.a[imax][jmax] // __a[imax][jmax]

        if (cell == 0.toShort()) break
        if (cell == EMPTY_CELL_D) break

        // Score(s[imax],t[jmax]) + __a[imax-1][jmax-1] == __a[imax][jmax]
        if (scoreCharD(s[imax], t[jmax]) +
                SmithWatermanD.a[imax - 1][jmax - 1] == cell.toInt()) {

            // tmp[0]=s[imax]; tmp[1]=t[jmax]; S_RESULT=S_RESULT+tmp
            SmithWatermanD.sResult = SmithWatermanD.sResult +
                s[imax].toString() + t[jmax].toString()

            imax--
            jmax--
            continue
        }

        // Score(s[imax],'-') + __a[imax-1][jmax] == __a[imax][jmax]
        if (scoreCharD(s[imax], '-') +
                SmithWatermanD.a[imax - 1][jmax] == cell.toInt()) {

            // tmp[0]=s[imax]; tmp[1]='-'
            SmithWatermanD.sResult = SmithWatermanD.sResult +
                s[imax].toString() + "-"

            imax--
            continue
        }

        // Score('-',t[jmax]) + __a[imax][jmax-1] == __a[imax][jmax]
        if (scoreCharD('-', t[jmax]) +
                SmithWatermanD.a[imax][jmax - 1] == cell.toInt()) {

            // tmp[0]='-'; tmp[1]=t[jmax]
            SmithWatermanD.sResult = SmithWatermanD.sResult +
                "-" + t[jmax].toString()

            jmax--
            continue
        }
    }

    return 0
}
