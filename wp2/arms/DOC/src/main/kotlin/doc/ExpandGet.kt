package doc

// Unit 16 (B1 tranche 3): compiler-parse.c::ExpandGet (batch 1, core
// stratum; units.csv row 16: loc 47, score 22.9, rank 178). The
// statement expander for indirect GET targets: same coded #G#/#S#
// payload walk as ExpandNext (unit 11), plus the CodedComma tail that
// selects the default-slot third argument - either a nested coded
// reference routed through Extract2 (unit 15, now the real function)
// or the raw sub-128 byte copy, defaulting to "NULL". Emit text into
// Out byte-verbatim per H-001 via CompilerState.emit. Vendor quirks
// kept and noted: in the #S# branch the tail test compares strncmp
// (&v1d[i],"#S#",3) against itself twice (the vendor's second operand
// was surely meant to be "#G#"); both operands are kept verbatim so
// the study record preserves the vendor's exact condition. The
// CodedComma byte (208) rides the Carriers const. Naming per H-017.

fun ExpandGet(v1d: CharArray, iIn: Int, bp: CharArray) {
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
        CompilerState.emit("\tGlobalGet( (unsigned char *) " + a1 +
                ", (unsigned char *) " + a2 + ", (unsigned char *) " + a3 +
                ", svPtr); \n")
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
            // vendor quirk: both strncmp operands are "#S#" here
            // (likely meant "#G#" / "#S#"); kept verbatim
            if (CstringOps.strncmpAt(v1d, i, "#S#", 3) == 0 ||
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
        CompilerState.emit("\tLocalGet( (unsigned char *) " + a1 +
                ", (unsigned char *) " + a2 + ", (unsigned char *) " + a3 +
                ", svPtr); \n")
        CstringOps.strcpyC(bp, arg2)
        return
    }
}
