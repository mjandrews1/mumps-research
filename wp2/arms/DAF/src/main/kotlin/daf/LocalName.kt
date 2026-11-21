package daf

// Unit 105 (DAF B4 T2, core): interp-sysfunc.cpp::LocalName (36 LOC):
// decodes a coded global/local name form (the 206/207/208 coded
// points, the same code points anchored at BASE-log row 101 and
// rendered as CODED_OPEN_D/CODED_COMMA_D/CODED_CLOSE_D in T1) into
// the parenthesized spelling, optionally truncated by a supplied piece
// limit.
// Consults: BASE-log row 101 hit-reuse (the 206/207/208 -> ( / " ) / " , "
// decode table; the T1 coded constants reused verbatim), row 28
// (atoi -> the DafCRuntimeT2.leading-int seat), row 30 hit-reuse (the
// tmp1 out-buffer becomes the String return), row 81 (NUL-terminated
// reads: tmp0 walked with the at() end-guard; the unnumbered-body
// return is the tmp3 NUL terminator).
// Vendor quirks preserved: j defaults to 999 when tmp2 is NULL; the
// break branches append a literal NUL char into tmp3 (the C '\0'
// writes kept as NUL_D appends, so the returned string can carry an
// embedded NUL exactly as the C buffer did); tmp0[i] > 127 compares
// the wide unsigned-char code (Char.code).
// Dafny: EXEMPT (buffer-coded decode relay; journal row).

fun LocalName(tmp0: CharArray, tmp2: String?): String {

    var j = 999                    // int j=999
    if (tmp2 != null) j = atoi(tmp2)

    val tmp3 = StringBuilder(STR_MAX_DAF) // unsigned char tmp3[STR_MAX]

    var i = 0
    var k = 0
    var m = 0

    loop@ while (at(tmp0, i) != NUL_D) {
        val c = tmp0[i]

        if (k >= j) {
            if (k == 0 && c.code > 127) { // break with bare NUL
                tmp3.append(NUL_D)
                m++
                break@loop
            }
            if (c.code > 127) {           // break with ')'+NUL
                tmp3.append(')')
                tmp3.append(NUL_D)
                m += 2
                break@loop
            }
        }

        when (c) {
            CODED_OPEN_D -> { tmp3.append('('); tmp3.append('"'); k++; m += 2 }
            CODED_COMMA_D -> { tmp3.append('"'); tmp3.append(')'); k++; m += 2 }
            CODED_CLOSE_D -> { tmp3.append('"'); tmp3.append(','); tmp3.append('"'); k++; m += 3 }
            else -> { tmp3.append(c); m++ }
        }

        i++
    }

    return tmp3.toString() // strcpy(tmp1, tmp3); out-buffer -> return
}
