package doc

// Unit 259 (B9 tranche 2): compiler-parse.c::ExpandName (batch
// 9, core stratum; units.csv row 260: loc 47, ptr 0, ctl 12,
// score 22.4, rank 179; physLOC 47 physically verified this
// session). The NAME expander of the ExpandGet/ExpandOrder
// family (unit 16 the render template, the ExpandOrder unit-173
// sibling): same #G#/#S# payload walk, same CodedComma
// default-slot Extract2-vs-raw-copy tail (extract2 the unit-15
// real; CodedComma the Carriers const), the bp strcpy(arg2)
// result convention kept. EMIT DIFFERENCE: the vendor emissions
// are the plain GlobalName / LocalName calls (no unsigned-char
// casts in the vendor strings here) - GlobalName one line, the
// LocalName emission split over the two-line continuation kept
// byte-verbatim including the trailing space before the
// newline. VENDOR FOSSIL kept and noted (H-013): the #S# arm
// tail test is the DUPLICATE "#S#"-or-"#S#" of the ExpandGet
// unit-16 fossil (written correctly as "#S#" or "#G#" only in
// ExpandOrder); kept verbatim here - the second compare of the
// pair is dead but its evaluation order is harmless. Consult
// H-014 hit-reuse (emit seat + the two real seats). Naming per
// H-017.

fun ExpandName(v1d: CharArray, iIn: Int, bp: CharArray) {
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
        // fprintf(Out, "\tGlobalName(%s, %s, %s, svPtr); \n", ...)
        CompilerState.emit("\tGlobalName(" + a1 + ", " + a2 + ", " +
                a3 + ", svPtr); \n")
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
            // vendor duplicate-"#S#" fossil (the ExpandGet
            // unit-16 pair, NOT the corrected ExpandOrder tests):
            // strncmp(&v1d[i], "#S#", 3) || strncmp(&v1d[i], "#S#", 3)
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
        // the vendor two-line split emission kept byte-faithful
        // including the trailing space before the
        // newline of the first fragment
        CompilerState.emit("\tLocalName( (unsigned char *) " + a1 +
                ", (unsigned char *) " + a2 + ", " +
                "(unsigned char *) " + a3 + ", svPtr); \n")
        CstringOps.strcpyC(bp, arg2)
        return
    }
}
