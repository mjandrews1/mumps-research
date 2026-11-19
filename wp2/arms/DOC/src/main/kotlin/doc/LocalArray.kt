package doc

// Unit 21 (B1 tranche 4): compiler-localarray.c::LocalArray (batch 1,
// complex stratum; units.csv row 21: loc 55, score, rank per
// units.csv). Expands an LHS local array reference: scans vd for
// coded delimiters and emits the tmp-building C fragment into Out;
// the Extract2 call at the index seats rides the real unit-15
// function. The goto has_array linearizes per H-011 as a scan flag.
// The extern v1d (compiler-globals-ext.h extern unsigned char
// v1d[]) rides CompilerState; Set_flg is the short* out-param seat
// (ShortArray(1), H-006). Emission text byte-verbatim per H-001 via
// CompilerState.emit; the &vd[i+1] literal-index emissions copy the
// index bytes into the emitted %s. Vendor quirks kept: the k =
// vd[j] at the sentinel walk may capture the NUL (the emitted
// tmp[i]=0 then writes a NUL byte - kept verbatim); i = j - 1
// rewinds the outer walk after each index segment. Naming per H-017.

// H-013 dead code note: the vendor's local char tmp[1024] is never
// referenced in the body (the emitted fragment owns its own tmp);
// dropped here.

fun LocalArray(vd: CharArray, Set_flg: ShortArray) {
    if (CompilerState.v1d[1] == '^' || CompilerState.v1d[1] == '$') return

    var has_array = false
    var i = 0
    while (vd[i] != NUL) {
        if (vd[i] > 127.toChar()) {
            has_array = true
            break
        }
        i++
    }
    if (!has_array) return // not an array reference - no open paren

    CompilerState.emit("\t{\tchar tmp[STR_MAX];  // expand lhs array reference\n" +
            "\t\tint i;\n")

    i = 0
    while (vd[i] != NUL) {
        if (vd[i] == CodedOpen.toChar()) {
            vd[i] = NUL
            CompilerState.emit("\t\tstrmove((UNS) tmp, (UNS) \"" +
                    CstringOps.cstring(vd) + "\"); // array name\n")
            CompilerState.emit("\t\ti=strlen(tmp);\n")
            CompilerState.emit("\t\ttmp[i]=206; // coded open paren\n")
            CompilerState.emit("\t\ttmp[i+1]=0;\n")
            vd[i] = CodedOpen.toChar()

            var j = i + 1
            while (vd[j] != NUL) {
                if (vd[j] == CodedClose.toChar() ||
                        vd[j] == CodedComma.toChar()) break
                j++
            }
            val k = vd[j]
            vd[j] = NUL
            if (CstringOps.strncmpAt(vd, i + 1, "#G#", 3) == 0 ||
                    CstringOps.strncmpAt(vd, i + 1, "#S#", 3) == 0) {
                CompilerState.emit("\t")
                CompilerState.emit("\tstrcat((char *) tmp,(char *) " +
                        CstringOps.cstring(extract2(vd, i + 1)) +
                        "); // index\n")
            } else {
                CompilerState.emit("\tstrcat((char *) tmp,(char *) " +
                        CstringOps.cstringAt(vd, i + 1) + "); // index\n")
            }
            CompilerState.emit("\t\ti=strlen(tmp);\n")
            CompilerState.emit("\t\ttmp[i]=" + k.code.toString() +
                    "; // coded comma or close paren\n")
            CompilerState.emit("\t\ttmp[i+1]=0;\n")
            vd[j] = k
            i = j - 1
        } else if (vd[i] == CodedClose.toChar()) {
            CompilerState.emit("\tstrmove((UNS) atmp, (UNS) tmp); " +
                    "// preserve expanded array reference\n")
            Set_flg[0] = 1
            CompilerState.emit("\t\t}\n")
            CstringOps.strcpyS(vd, "atmp")
            break
        } else if (vd[i] == CodedComma.toChar()) {
            var j = i + 1
            while (vd[j] != NUL) {
                if (vd[j] == CodedClose.toChar() ||
                        vd[j] == CodedComma.toChar()) break
                j++
            }
            val k = vd[j]
            vd[j] = NUL
            if (CstringOps.strncmpAt(vd, i + 1, "#G#", 3) == 0 ||
                    CstringOps.strncmpAt(vd, i + 1, "#S#", 3) == 0) {
                CompilerState.emit("\t")
                CompilerState.emit("\t\tstrcat((char *) tmp,(char *) " +
                        CstringOps.cstring(extract2(vd, i + 1)) +
                        "); // index\n")
            } else {
                CompilerState.emit("\t\tstrcat((char *) tmp,(char *) " +
                        CstringOps.cstringAt(vd, i + 1) + "); // index\n")
            }
            CompilerState.emit("\t\ti=strlen((const char *) tmp);\n")
            CompilerState.emit("\t\ttmp[i]=" + k.code.toString() +
                    "; // coded comma or close paren\n")
            CompilerState.emit("\t\ttmp[i+1]=0;\n")
            vd[j] = k
            i = j - 1
        }
        i++
    }
}
