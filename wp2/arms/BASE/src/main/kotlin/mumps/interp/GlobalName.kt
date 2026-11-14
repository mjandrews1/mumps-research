package mumps.interp

/**
 * Kotlin translation of interp-sysfunc.cpp::GlobalName (Study Subset unit 13,
 * Batch 1, arm BASE; translated 2026-11-14).
 *
 * C: void GlobalName(unsigned char *tmp1, unsigned char tok, struct MSV *svPtr)
 * Rewrites the \x01 subscript separators inside tmp1: with tok == NUL they
 * become the quoted-paren form (\"(sub1\",\"sub2\")...); otherwise each
 * separator is replaced by the tok character. In-place mutation via the C
 * tmp3 double-buffer -> rebuild-and-replace (heuristic 31).
 *
 * svPtr is declared but unused in the C body; kept for signature fidelity.
 */
fun globalName(tmp1: StringBuilder, tok: Char, @Suppress("UNUSED_PARAMETER") svPtr: MsvState) {
    val tmp3 = StringBuilder()
    val sep = '\u0001'
    val n = tmp1.length
    var k = 0

    if (tok == '\u0000') { // C: tok == 0
        var oflg = false
        for (i in 0 until n) {
            val c = tmp1[i]
            when {
                c == sep && !oflg -> { tmp3.append("(\""); k++; oflg = true }
                c == sep && oflg && i + 1 < n -> { tmp3.append("\",\""); k++ } // C: tmp1[i+1]!='\0'
                c == sep -> { tmp3.append("\")"); k++ }
                else -> tmp3.append(c)
            }
        }
    } else {
        var oflg = false
        for (i in 0 until n) {
            val c = tmp1[i]
            when {
                c == sep && !oflg -> { tmp3.append(tok); k++; oflg = true }
                c == sep && oflg && i + 1 < n -> { tmp3.append(tok); k++ }
                c == sep -> { tmp3.append(tok); k++ }
                else -> tmp3.append(c)
            }
        }
    }

    tmp1.setLength(0) // C: strcpy(tmp1, tmp3)
    tmp1.append(tmp3)
}
