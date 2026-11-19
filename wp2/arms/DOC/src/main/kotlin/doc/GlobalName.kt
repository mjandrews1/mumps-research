package doc

// Unit 13 (B1 tranche 3): interp-sysfunc.cpp::GlobalName (batch 1,
// core stratum; units.csv row 20: loc 48, score 16.0, rank 149).
// Rewrites a coded global name so the \x01 subscript separator bytes
// become readable delimiters: with tok==0 the name is re-cased into
// quoted comma form "name("a","b")", with any other tok each \x01
// becomes that byte. The \x01 byte rides the CHAR_1 carrier
// (Carriers.kt, H-015 named constant) and NUL rides NUL. tmp3 is the
// STR_MAX scratch carrier; the walk writes the source bytes between
// separators verbatim. Dead code per H-013: the vendor locals j=999
// and k (assigned and incremented, never read) are dropped with this
// note; oflg is the live once-only latch. tmp1[i+1] lookahead is
// bounds-guarded (H-007 carrier access; the guard never fires on a
// NUL-terminated carrier). Naming per H-017.

fun GlobalName(tmp1: CharArray, tok: Char, svPtr: Msv) {
    val tmp3 = CharArray(STR_MAX)
    var oflg = 0
    var tx = 0

    if (tok == NUL) {
        var i = 0
        while (i < tmp1.size && tmp1[i] != NUL) {
            if (tmp1[i] == CHAR_1 && oflg == 0) {
                tmp3[tx++] = '('
                tmp3[tx++] = '"'
                oflg = 1
            } else if (tmp1[i] == CHAR_1 && oflg == 1 &&
                    i + 1 < tmp1.size && tmp1[i + 1] != NUL) {
                tmp3[tx++] = '"'
                tmp3[tx++] = ','
                tmp3[tx++] = '"'
            } else if (tmp1[i] == CHAR_1) {
                tmp3[tx++] = '"'
                tmp3[tx++] = ')'
            } else {
                tmp3[tx++] = tmp1[i]
            }
            i++
        }
    } else {
        var i = 0
        while (i < tmp1.size && tmp1[i] != NUL) {
            if (tmp1[i] == CHAR_1 && oflg == 0) {
                tmp3[tx++] = tok
                oflg = 1
            } else if (tmp1[i] == CHAR_1) {
                tmp3[tx++] = tok
            } else {
                tmp3[tx++] = tmp1[i]
            }
            i++
        }
    }

    tmp3[tx] = NUL
    CstringOps.strcpyC(tmp1, tmp3)
    return
}
