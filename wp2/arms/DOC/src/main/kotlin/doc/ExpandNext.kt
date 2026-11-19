package doc

// Unit 11 (B1 tranche 3): compiler-parse.c::ExpandNext (batch 1, core
// stratum; units.csv row 22: loc 25, score 13.3, rank 128). The
// compiler statement expander for indirection targets: reads the coded
// #G#/#S# payload after the current position in the v1d carrier and
// emits the runtime GlobalOrder or LocalNext call into Out. The vendor
// arg1/arg2 are char[32] zero-initialized buffers ("" initializer);
// the copy loops fill them in place and the NUL terminator of the last
// copied byte ends the run. fprintf(Out,...) maps to CompilerState.emit
// per H-001 (the emitted text is kept byte-verbatim, including the
// (unsigned char *) casts and the //2306 comment marker). strncmp with
// an offset argument maps to CstringOps.strncmpAt (new seat; the same
// carrier-shape strncmp family). svPtr is emitted by name inside the
// generated call text, not passed in the vendor signature either.
// Naming per H-017.

fun ExpandNext(v1d: CharArray, iIn: Int, bp: CharArray) {
    val arg1 = CharArray(32)
    val arg2 = CharArray(32)
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
        val a1 = CstringOps.cstring(arg1)
        val a2 = CstringOps.cstring(arg2)
        CompilerState.emit("\tGlobalOrder(" + a1 + ", " + a2 +
                ", (unsigned char *) \"1\", svPtr); //2306\n")
        CompilerState.emit("\tif (strlen((char *)" + a2 +
                ")==0) strcpy((char *)" + a2 + ",\"-1\");\n")
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
        val a1 = CstringOps.cstring(arg1)
        val a2 = CstringOps.cstring(arg2)
        CompilerState.emit("\tLocalNext((unsigned char *) " + a1 +
                ",(unsigned char *) " + a2 + ",svPtr);\n")
        CstringOps.strcpyC(bp, arg2)
        return
    }
}
