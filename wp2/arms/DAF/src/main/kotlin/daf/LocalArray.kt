package daf

// R1 stub-completion (2026-11-27, C-020 erratum session, tranche 3):
// compiler-localarray.c::LocalArray (55 LOC, roster row 12,
// complex): the compile-time LHS local-array reference expander.
// After the v1d guard (a ^ or \u0024 first token is not a local
// array), the scan looks for coded bytes (>127); the 206/Open
// arm emits the tmp build lines (array name, coded open paren,
// index strcat via the Extract2 relay or the raw segment, coded
// comma/close), the 208/comma arm repeats the index emission
// with the strlen((const char *) variant, and the 207/Close arm
// emits the atmp preserve, sets Set_flg and rewrites vd to
// "atmp" (the String? return fold: null leaves the caller vd
// untouched; "atmp" is the strcpy(vd,"atmp") write-back - the
// call sites in SetCode.kt now write the returned value into
// C.vd, fixing the previously dead atmp read of the Merge arm).
// Consults (2026-11-27T00:00:00Z, nominal): BASE-log row 2
// family hit-reuse (fprintf(Out,...) rides DafCompilerState.emit
// with the emitted-C text kept VERBATIM: the tab prefixes - the
// two-tab strmove/strlen/tmp-write lines, the one-tab strcat
// line of the plain arm, the extra tab prefix the #G#/#S# arm
// writes before the same strcat line, the strmove((UNS) atmp
// preserve line and the closing brace line); row 44 hit-reuse
// (conditional out-param write: vd receives "atmp" only on the
// Close arm - the unit-135/ExpandOrder String? fold); rows 111/68
// hit-reuse (extract2Daf the raised roster-b1 relay, called
// exactly as the landed Expand* siblings call it; the 206/207/
// 208 codes seat beside CODED_COMMA_D); row 30 hit-adapt (the
// char tmp[1024]/vd in-place mutations render as String slices:
// vd[i]=0 / vd[j]=0 become substring cuts, disclosed).
// Vendor quirks kept: (1) the guard reads the TU GLOBAL v1d[1]
// (DafCompilerState.v1d) while the scan walks the vd ARGUMENT -
// kept verbatim; (2) a coded byte OTHER than 206/207/208 (the
// goto has_array trigger) makes the scan loop emit nothing for
// it - the for-loop falls through, kept; (3) the k cursor read
// at the scan end takes vd[j] which may be the NUL (k=0) when
// no 207/208 follows - the emission then writes tmp[i]=0,
// kept; (4) the 206 arm emits strlen(tmp) plain while the 208
// arm emits strlen((const char *) tmp) - both kept verbatim;
// (5) the Close arm breaks the scan (the first close paren
// ends the expansion).
// Dafny: EXEMPT (emission-shaping into the Out writer; journal
// row).

fun localArrayDaf(vdIn: String, flag: IntCellD): String? {

    val C = DafCompilerState

    if (C.v1d[1] == '^' || C.v1d[1] == '\u0024') return null

    var hasArray = false
    for (c in vdIn) if (c.code > 127) { hasArray = true; break }
    if (!hasArray) return null // Not an array reference - no open paren

    // fprintf(Out, "\t{\tchar tmp[STR_MAX];  // expand lhs array
    // reference\n" "\t\tint i;\n") - verbatim
    C.emit("\t{\tchar tmp[STR_MAX];  // expand lhs array reference\n" +
        "\t\tint i;\n")

    val vd = vdIn.toCharArray()
    var i = 0
    while (i < vd.size && vd[i] != NUL_D) { // for (i = 0; vd[i] != 0; i++)

        if (vd[i].code == 206) { // Open

            // vd[i] = 0; the array name is the prefix cut
            val seg = vdIn.substring(0, i)
            C.emit("\t\tstrmove((UNS) tmp, (UNS) \"" + seg +
                "\"); // array name\n")
            C.emit("\t\ti=strlen(tmp);\n")
            C.emit("\t\ttmp[i]=206; // coded open paren\n")
            C.emit("\t\ttmp[i+1]=0;\n")
            // vd[i] = 206 restored by the substring model

            // for (j = i + 1; vd[j] != 0; j++)
            //      if (vd[j] == 207 || vd[j] == 208) break;
            var j = i + 1
            while (j < vd.size && vd[j] != NUL_D &&
                    vd[j].code != 207 && vd[j].code != 208) j++
            // k = vd[j]; (k = 0 at the string end)
            val k = if (j < vd.size) vd[j].code else 0
            // vd[j] = 0; the index segment is the cut
            val idx = vdIn.substring(i + 1, minOf(j, vd.size))

            if (idx.startsWith("#G#") || idx.startsWith("#S#")) {
                C.emit("\t") // fprintf (Out,"	");
                C.emit("\tstrcat((char *) tmp,(char *) " +
                    extract2Daf(idx) + "); // index\n")
            } else {
                C.emit("\tstrcat((char *) tmp,(char *) " + idx +
                    "); // index\n")
            }
            C.emit("\t\ti=strlen(tmp);\n")
            C.emit("\t\ttmp[i]=" + k + "; // coded comma or close paren\n")
            C.emit("\t\ttmp[i+1]=0;\n")
            // vd[j] = k restored by the substring model
            i = j - 1
        } else if (vd[i].code == 207) { // Close

            C.emit("\tstrmove((UNS) atmp, (UNS) tmp); // preserve " +
                "expanded array reference\n")
            flag.v = 1 // *Set_flg = 1
            C.emit("\t\t}\n")
            return "atmp" // strcpy (vd, "atmp"); break;
        } else if (vd[i].code == 208) { // comma

            // for (j = i + 1; vd[j] != 0; j++)
            //      if (vd[j] == 207 || vd[j] == 208) break;
            var j = i + 1
            while (j < vd.size && vd[j] != NUL_D &&
                    vd[j].code != 207 && vd[j].code != 208) j++
            val k = if (j < vd.size) vd[j].code else 0
            val idx = vdIn.substring(i + 1, minOf(j, vd.size))

            if (idx.startsWith("#G#") || idx.startsWith("#S#")) {
                C.emit("\t") // fprintf (Out,"	");
                C.emit("\tstrcat((char *) tmp,(char *) " +
                    extract2Daf(idx) + "); // index\n")
            } else {
                C.emit("\tstrcat((char *) tmp,(char *) " + idx +
                    "); // index\n")
            }
            C.emit("\t\ti=strlen((const char *) tmp);\n")
            C.emit("\t\ttmp[i]=" + k + "; // coded comma or close paren\n")
            C.emit("\t\ttmp[i+1]=0;\n")
            i = j - 1
        }

        i++
    }

    return null
}
