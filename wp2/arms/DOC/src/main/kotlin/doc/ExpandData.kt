package doc

// Unit 135 (B5 tranche 2): compiler-parse.c::ExpandData (batch 5, core
// stratum; LOC 24, score 11.4) - the compiler data-subscript expander,
// the unit-11 ExpandNext sibling: reads the coded #G#/#S# payload
// after the current position in the v1d carrier and emits the runtime
// GlobalData or LocalData call. The vendor arg1/arg2 are char[32]
// buffers filled in place; the NUL terminator of the last copied byte
// ends the run. fprintf(Out,...) maps to CompilerState.emit per
// H-001 (the emitted text kept byte-verbatim, including the
// (unsigned char *) casts). strncmpAt is the offset-argument strncmp
// seat (unit-11 precedent). If neither marker matches the function
// falls through with bp untouched (no default emission - vendor
// shape kept). Naming per H-017.

fun ExpandData(v1d: CharArray, iIn: Int, bp: CharArray) {
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
        CompilerState.emit("\tGlobalData(" + a1 + "," + a2 + ",svPtr);\n")
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
        CompilerState.emit("\tLocalData((unsigned char *) " + a1 +
                ",(unsigned char *) " + a2 + ",svPtr);\n")
        CstringOps.strcpyC(bp, arg2)
        return
    }
}
