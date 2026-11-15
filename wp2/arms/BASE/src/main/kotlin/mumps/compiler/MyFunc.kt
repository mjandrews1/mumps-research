package mumps.compiler

import mumps.compiler.CompilerState.Out

/**
 * Kotlin translation of compiler-sys1.c::MyFunc (Study Subset unit 27,
 * Batch 1, arm BASE; translated 2026-11-14).
 *
 * C: int MyFunc(unsigned char v1d[], int iiitmp) — extrinsic function
 * call compilation ($$name(args) and DO ^rtn(label)).
 *
 * The C #includes builtinTable.h INSIDE the function body; the table is
 * a file-level array here (heuristic 74). Dead artifacts preserved as
 * comments: bd[1024] is strcpy'd "gtmp" but never read; j = v1d[i] is
 * assigned but never used. Extract1 is stubbed (compiler-parse.c:2119,
 * not a selected unit).
 */
private val BUILT_IN = arrayOf(
    "^Cosine", "^Avg", "^Count", "^Max", "^Min", "^Multiply", "^Sum",
    "^Transpose", "^IDF", "^TermCorrelate", "^DocCorrelate", "^Sim1",
    "^Dice", "^Jaccard", "^Centroid", "^Scan", "^ScanAlnum", "^Input"
) // C: include/mumpsc/builtinTable.h (terminating "" entry implicit)

fun myFunc(v1d: StringBuilder, iiitmp: Int): Int {
    val S = CompilerState

    // C: for (i=0; v1d[i]!=0 && v1d[i]<127 && v1d[i]!=' '; i++) name[i]=v1d[i];
    val nameB = StringBuilder()
    var i = 0
    while (i < v1d.length && v1d[i].code < 127 && v1d[i] != ' ') {
        nameB.append(v1d[i]); i++
    }
    val rest = if (i < v1d.length) v1d.substring(i + 1) else "" // C: strcpy(v1d, &v1d[i+1])
    v1d.setLength(0); v1d.append(rest)
    var name = nameB.toString()

    var bFlg = false // C: BFlg — check against BuiltIn[]
    for (b in BUILT_IN) if (name == b) { bFlg = true; break }

    var k = 0
    while (v1d.length > 0) { // C: while (strlen(v1d) > 0)
        if (k > 29) {
            print("*** Too Many Extrinsic Function Arguments in or near line " + S.lines + "\n")
            S.ExitCode = 99
            k = 9 // C: k = 9 — faithful
        }

        var p = 0 // C: for (i=0; v1d[i]!=0 && v1d[i]<127; i++);
        while (p < v1d.length && v1d[p].code < 127) p++
        // C: j = v1d[i]; — dead assignment (never read), noted
        val token = v1d.substring(0, p) // C: v1d[i] = 0 truncation

        if (bFlg && !token.regionMatches(0, "#S#", 0, 3)) {
            Out.write("\t strmove((unsigned char *) __Arg" + k + ", (unsigned char *) " +
                    extract1(token) + "); // arg setup D1002\n") // Extract1 stubbed
        } else {
            Out.write("\t strmove((unsigned char *) __Arg" + k + ", (unsigned char *) " +
                    extract2(token) + "); // arg setup D1003\n")
        }

        val rest2 = if (p < v1d.length) v1d.substring(p + 1) else "" // C: strcpy(v1d, &v1d[i+1])
        v1d.setLength(0); v1d.append(rest2)
        k++
    }

    if (k > S.ArgMax) S.ArgMax = k.toShort() // C: extern short ArgMax

    // C: strcpy(bd, "gtmp"); — bd[1024] never read afterwards; dead, noted
    Out.write("\t _ArgCount=" + k + ";\n")

    var ci = 0 // find first '^' in name
    while (ci < name.length && name[ci] != '^') ci++

    if (ci >= name.length) { // C: name[i] != '^' — a local reference DO
        Out.write("\t _doStx++;\n")
        // C passes k as an unused vararg to this fprintf — no %d in the format
        Out.write("\t if (_doStx > 127 ) ErrorMessage(\"Too many function calls\",svPtr->LineNumber);\n")
        Out.write("\t sym_(99, (unsigned char *) \"\", (unsigned char *) \"\", svPtr); \n")
        if (name.isNotEmpty() && name[0].isDigit()) // C: isdigit(name[0])
            Out.write("\t if (setjmp(_doStack[_doStx])==0) goto _" + name + ";")
        else
            Out.write("\t if (setjmp(_doStack[_doStx])==0) goto " + name + ";")
        Out.write("  /* Jump to function */\n")
        Out.write("\t _doStx--; \n")
        if (iiitmp >= 0)
            Out.write("\t strmove((unsigned char *) tmp" + iiitmp + ",(unsigned char *) _ReturnVal); // Copy result\n")
        return 0
    }

    var lbl = "\"\"" // C: char lbl[128] = "\"\""
    if (ci != 0) { // external name contains label: LBL^ROUTINE
        lbl = "\"" + name.substring(0, ci) + "\""
        name = name.substring(ci) // C: name[i]='^'; strcpy(name, &name[i]) -> "^ROUTINE"
    }

    if (name.length >= 2 && name[0] == '^' && name[1] == '^') { // double ^
        Out.write("\t_jj=Interpret(\"x \\\"do ^" + name.substring(1) + "\\\"\",svPtr);\n")
        Out.write("\tsvPtr->ERROR=_jj;\n")
        Out.write("\tif (!svPtr->NOERR && _jj<0) ErrorMessage(\"Indirection error\",svPtr->LineNumber);")
        return 0
    }

    if (name.isNotEmpty() && name[0] == '^') { // single ^
        for (c in lbl) { // valid label chars only
            if (c.isLetterOrDigit()) continue
            if (c == '%') continue
            if (c == '"') continue
            return 9002
        }
        Out.write("\t_jj=Interpret(\"x \\\"do ^" + name.substring(1) + "\\\"\",svPtr);\n")
        Out.write("\tsvPtr->ERROR=_jj;\n")
        Out.write("\tif (!svPtr->NOERR && _jj<0) ErrorMessage(\"Indirection error\",svPtr->LineNumber);")
        return 0
    }

    return 9003
}
