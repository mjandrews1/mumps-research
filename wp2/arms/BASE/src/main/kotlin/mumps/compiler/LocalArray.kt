package mumps.compiler

import mumps.compiler.CompilerState.Out

/**
 * Kotlin translation of compiler-localarray.c::LocalArray (Study Subset
 * unit 21, Batch 1, arm BASE; translated 2026-11-14).
 *
 * C: void LocalArray(unsigned char vd[], short *Set_flg)
 * Expands an LHS local array reference into inline C++ code.
 *
 * Mapping: vd mutable -> StringBuilder; *Set_flg -> BooleanArray[0] holder
 * (heuristic 66); the C truncate-and-restore idiom (vd[i]=0 ... print ...
 * vd[i]=206) becomes substring slices with no escaping mutation
 * (heuristic 67); goto has_array -> scan-then-flag (heuristics 10/21).
 * C's unused local char tmp[1024] is noted, not reproduced.
 */
fun localArray(vd: StringBuilder, setFlg: BooleanArray) {
    val v1d = CompilerState.v1d
    val c1 = if (v1d.length > 1) v1d[1] else '\u0000'
    if (c1 == '^' || c1 == '$') return

    // C: for (i=0; vd[i]!=0; i++) if (vd[i]>127) goto has_array; return;
    var hasArray = false
    for (i in 0 until vd.length) if (vd[i].code > 127) { hasArray = true; break }
    if (!hasArray) return

    // has_array:
    Out.write("\t{\tchar tmp[STR_MAX];  // expand lhs array reference\n\t\tint i;\n")

    var i = 0
    while (i < vd.length) { // C: for (i=0; vd[i]!=0; i++)
        when (vd[i].code) {
            206 -> { // Open
                // C: vd[i]=0; fprintf(...vd...); vd[i]=206; -> slice (heuristic 67)
                Out.write("\t\tstrmove((UNS) tmp, (UNS) \"" + vd.substring(0, i) + "\"); // array name\n")
                Out.write("\t\ti=strlen(tmp);\n")
                Out.write("\t\ttmp[i]=206; // coded open paren\n")
                Out.write("\t\ttmp[i+1]=0;\n")
                var j = i + 1
                while (j < vd.length && vd[j].code != 207 && vd[j].code != 208) j++
                val k = if (j < vd.length) vd[j].code else 0 // C: k = vd[j] (0 at NUL)
                val idx = vd.substring(i + 1, j) // C: &vd[i+1] under vd[j]=0 truncation
                if (idx.regionMatches(0, "#G#", 0, 3) || idx.regionMatches(0, "#S#", 0, 3)) {
                    // C: separate fprintf("\t") + "\tstrcat..." — net "\t\t", same as else
                    Out.write("\t")
                    Out.write("\tstrcat((char *) tmp,(char *) " + extract2(idx) + "); // index\n")
                } else {
                    Out.write("\t\tstrcat((char *) tmp,(char *) " + idx + "); // index\n")
                }
                Out.write("\t\ti=strlen(tmp);\n")
                Out.write("\t\ttmp[i]=" + k + "; // coded comma or close paren\n")
                Out.write("\t\ttmp[i+1]=0;\n")
                i = j - 1 // C: i = j - 1
            }
            207 -> { // Close
                Out.write("\tstrmove((UNS) atmp, (UNS) tmp); // preserve expanded array reference\n")
                setFlg[0] = true // C: *Set_flg = 1
                Out.write("\t\t}\n")
                vd.setLength(0) // C: strcpy(vd, "atmp")
                vd.append("atmp")
                return // C: break
            }
            208 -> { // comma
                var j = i + 1
                while (j < vd.length && vd[j].code != 207 && vd[j].code != 208) j++
                val k = if (j < vd.length) vd[j].code else 0
                val idx = vd.substring(i + 1, j)
                if (idx.regionMatches(0, "#G#", 0, 3) || idx.regionMatches(0, "#S#", 0, 3)) {
                    Out.write("\t")
                    Out.write("\tstrcat((char *) tmp,(char *) " + extract2(idx) + "); // index\n")
                } else {
                    Out.write("\t\tstrcat((char *) tmp,(char *) " + idx + "); // index\n")
                }
                Out.write("\t\ti=strlen((const char *) tmp);\n")
                Out.write("\t\ttmp[i]=" + k + "; // coded comma or close paren\n")
                Out.write("\t\ttmp[i+1]=0;\n")
                i = j - 1
            }
        }
        i++
    }
}
