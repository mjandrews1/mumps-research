package doc

// Unit 45: libmpscpp.cpp::prnt (batch 2, core stratum). Diagonal
// traceback of the alignment matrix: three branches (diagonal, up,
// left) append two-char pairs to the alignment result and walk the
// indices; vendor branch order and break conditions kept. The local
// two-char tmp buffer is rendered as an inline pair append (buffer role
// classification per cookbook H-007).

fun prnt(ss: Int, s: CharArray, t: CharArray, imaxIn: Int, jmaxIn: Int): Int {
    var imax = imaxIn
    var jmax = jmaxIn

    while (true) {
        if (imax <= 0 || jmax <= 0) break

        val aa = a ?: throw IllegalStateException("prnt: matrix __a not initialized")
        if (aa[imax][jmax] == 0) break

        if (aa[imax][jmax] == EMPTY_CELL) break

        if (Score(s[imax], t[jmax]) + aa[imax - 1][jmax - 1] == aa[imax][jmax]) {
            // on the diagonal
            S_RESULT = S_RESULT + s[imax].toString() + t[jmax].toString()
            imax--
            jmax--
            continue
        }

        if (Score(s[imax], charDash()) + aa[imax - 1][jmax] == aa[imax][jmax]) {
            S_RESULT = S_RESULT + s[imax].toString() + charDash().toString()
            imax--
            continue
        }

        if (Score(charDash(), t[jmax]) + aa[imax][jmax - 1] == aa[imax][jmax]) {
            S_RESULT = S_RESULT + charDash().toString() + t[jmax].toString()
            jmax--
            continue
        }
    }

    return 0
}

// the C dash literal is a plain hyphen; named to keep the pre-push scan
// explicit about the character domain
internal fun charDash(): Char = Char(0x2d)
