package doc

// Unit 173 (B6 tranche 2): compiler-parse.c::ExpandOrder (batch 6,
// core stratum; roster row 173: loc 50, score 24.2, batch_plan
// strata order). The statement expander for $ORDER(...) targets:
// the same coded #G#/#S# payload walk as ExpandGet (unit 16, the
// render template), but the emitted call is GlobalOrder or
// LocalOrder and the default-slot CodedComma tail routes through
// Extract2 (unit 15, the real function) or the raw sub-128 byte
// copy, defaulting to "NULL". Emit text into Out byte-verbatim
// per H-001 via CompilerState.emit, including the TWO-LINE split
// GlobalOrder emission with its trailing space-before-newline and
// the vendor debug fossil comment p2236, and the LocalOrder
// single-line emission with its trailing space before the
// newline; the vendor nested tail tests here are written correctly
// (#G# arm: "#G#" or "#S#"; #S# arm: "#S#" or "#G#"), unlike the
// ExpandGet unit-16 duplicate-"#S#" fossil, which does NOT apply
// to this function. The bp result convention is strcpy(bp,arg2)
// per ExpandGet. Naming per H-017.

fun ExpandOrder(v1d: CharArray, iIn: Int, bp: CharArray) {
    val arg1 = CharArray(32)
    val arg2 = CharArray(32)
    val arg3 = CharArray(32)
    var i = iIn

    i++
    if (CstringOps.strncmpAt(v1d, i, "#G#", 3) == 0) {
        i = i + 3
        var j = 0
        while (v1d[i] != '#') {
            arg1[j] = v1d[i]
            j++
            i++
        }
        i++
        j = 0
        while (v1d[i] != '#') {
            arg2[j] = v1d[i]
            j++
            i++
        }
        i = i + 1
        if (v1d[i] == CodedComma.toChar()) {
            i++
            if (CstringOps.strncmpAt(v1d, i, "#G#", 3) == 0 ||
                    CstringOps.strncmpAt(v1d, i, "#S#", 3) == 0) {
                CstringOps.strcpyC(arg3, extract2(v1d, i))
            } else {
                j = 0
                while (v1d[i] < 128.toChar() && v1d[i] != NUL) {
                    arg3[j] = v1d[i]
                    j++
                    i++
                }
            }
        } else {
            CstringOps.strcpyS(arg3, "NULL")
        }

        val a1 = CstringOps.cstring(arg1)
        val a2 = CstringOps.cstring(arg2)
        val a3 = CstringOps.cstring(arg3)
        // vendor two-line emission, kept byte-faithful including
        // the trailing space before the first newline and the
        // p2236 debug fossil comment
        CompilerState.emit("\tGlobalOrder((unsigned char *) " + a1 +
                ", (unsigned char *) " + a2 + ", \n" +
                "\t\t(unsigned char *) " + a3 + ", svPtr); // p2236\n")
        CstringOps.strcpyC(bp, arg2)
        return
    }

    if (CstringOps.strncmpAt(v1d, i, "#S#", 3) == 0) {
        i = i + 3
        var j = 0
        while (v1d[i] != '#') {
            arg1[j] = v1d[i]
            j++
            i++
        }
        i++
        j = 0
        while (v1d[i] != '#') {
            arg2[j] = v1d[i]
            j++
            i++
        }
        i = i + 1
        if (v1d[i] == CodedComma.toChar()) {
            i++
            if (CstringOps.strncmpAt(v1d, i, "#S#", 3) == 0 ||
                    CstringOps.strncmpAt(v1d, i, "#G#", 3) == 0) {
                CstringOps.strcpyC(arg3, extract2(v1d, i))
            } else {
                j = 0
                while (v1d[i] < 128.toChar() && v1d[i] != NUL) {
                    arg3[j] = v1d[i]
                    j++
                    i++
                }
            }
        } else {
            CstringOps.strcpyS(arg3, "NULL")
        }

        val a1 = CstringOps.cstring(arg1)
        val a2 = CstringOps.cstring(arg2)
        val a3 = CstringOps.cstring(arg3)
        CompilerState.emit("\tLocalOrder((unsigned char *) " + a1 +
                ", (unsigned char *) " + a2 + ", " +
                "(unsigned char *) " + a3 + ", svPtr); \n")
        CstringOps.strcpyC(bp, arg2)
        return
    }
}
