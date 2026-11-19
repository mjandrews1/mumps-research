package doc

// Unit 27 (B1 tranche 5): compiler-sys1.c::MyFunc (batch 1, complex
// stratum; units.csv row 27: loc 89, score, rank per units.csv).
// The extrinsic-function DO expander: pulls the function name off
// the front of v1d, checks it against the builtin table (the
// vendor #include <mumpsc/builtinTable.h> inside the function body
// renders as the BuiltIn table seat, kept verbatim below), then
// walks the argument seats emitting the __ArgN strmove calls, and
// branches local vs ^-external DO emission. Extract1 is a
// below-roster helper (compiler-parse.c:2119) carried as an H-014
// stub; Extract2 is the real unit-15 function. ArgMax is the
// compiler-globals.h:53 extern short (CompilerState.ArgMax, added
// with this unit); ExitCode and lines already ride CompilerState
// (unit-18 additions). Vendor quirks kept: the k>29 branch resets
// k to 9 (an off-pattern value, noted verbatim); the Too many
// function calls emission is passed k as a printf arg with no
// matching conversion (ignored in C, ignored here); strcpy(bd,
// "gtmp") initializes a local bd that is never read again (the
// dead init is kept with the H-013 note); lbl seeds as the two
// quote chars. Emission text byte-verbatim per H-001. Naming per
// H-017.

// builtinTable.h BuiltIn[] (included inside the vendor function
// body; the table is the 18 builtin names plus the empty stopper).
private val BuiltIn = arrayOf(
    "^Cosine", "^Avg", "^Count", "^Max", "^Min", "^Multiply",
    "^Transpose", "^IDF", "^TermCorrelate", "^DocCorrelate", "^Sim1",
    "^Dice", "^Jaccard", "^Centroid", "^Scan", "^ScanAlnum",
    "^Input", ""
)

fun MyFunc(v1d: CharArray, iiitmp: Int): Int {
    val bd = CharArray(1024)
    val name = CharArray(128)
    var lbl = "\"\""

    var i = 0
    while (v1d[i] != NUL && v1d[i] < 127.toChar() && v1d[i] != ' ') {
        name[i] = v1d[i]
        i++
    }

    if (v1d[i] != NUL) {
        val rest = CstringOps.cstringAt(v1d, i + 1)
        CstringOps.strcpyS(v1d, rest)
    } else {
        CstringOps.strcpyS(v1d, "")
    }

    name[i] = NUL
    val nameS = CstringOps.cstring(name)

    // check if fcn is known builtin function against table of names
    var BFlg = 0
    var j = 0
    while (BuiltIn[j].isNotEmpty()) {
        if (nameS == BuiltIn[j]) {
            BFlg = 1
            break
        }
        j++
    }

    j = 1
    var k = 0

    // find arguments
    while (CstringOps.cstring(v1d).isNotEmpty()) {

        if (k > 29) {
            System.out.print("*** Too Many Extrinsic Function Arguments")
            System.out.print(" in or near line " + CompilerState.lines + "\n")
            CompilerState.ExitCode = 99
            k = 9 // vendor off-pattern reset, kept verbatim
        }

        i = 0
        while (v1d[i] != NUL && v1d[i] < 127.toChar()) i++
        val jj = v1d[i]
        v1d[i] = NUL

        if (BFlg != 0 &&
                CstringOps.strncmpS("#S#", v1d, 3) != 0) {
            val arg = CstringOps.cstring(
                    Stubs.Extract1(v1d.copyOf()))
            CompilerState.emit("\t strmove((unsigned char *) __Arg" + k +
                    ", (unsigned char *) " + arg +
                    "); // arg setup D1002\n")
        } else {
            val arg = CstringOps.cstring(extract2(v1d, 0))
            CompilerState.emit("\t strmove((unsigned char *) __Arg" + k +
                    ", (unsigned char *) " + arg +
                    "); // arg setup D1003\n")
        }
        val rest = CstringOps.cstringAt(v1d, i + 1)
        CstringOps.strcpyS(v1d, rest)
        k++
    }

    if (k > CompilerState.ArgMax) CompilerState.ArgMax = k

    // H-013 note: strcpy(bd, "gtmp") writes a local never read again
    CstringOps.strcpyS(bd, "gtmp")
    CompilerState.emit("\t _ArgCount=" + k + ";\n")

    i = 0
    while (name[i] != NUL) {
        if (name[i] == '^') break
        i++
    }

    // find start of external name

    if (name[i] != '^') { // a local reference DO

        CompilerState.emit("\t _doStx++;\n")
        // vendor passes k as a printf arg with no conversion; ignored
        CompilerState.emit("\t if (_doStx > 127 ) ErrorMessage(" +
                "\"Too many function calls\",svPtr->LineNumber);\n")

        CompilerState.emit("\t sym_(99, (unsigned char *) \"\", " +
                "(unsigned char *) \"\", svPtr); \n")

        if (name[0] in '0'..'9') {
            CompilerState.emit("\t if (setjmp(_doStack[_doStx])==0) " +
                    "goto _" + nameS + ";")
        } else {
            CompilerState.emit("\t if (setjmp(_doStack[_doStx])==0) " +
                    "goto " + nameS + ";")
        }
        CompilerState.emit("  /* Jump to function */\n")

        CompilerState.emit("\t _doStx--; \n")

        if (iiitmp >= 0) {
            CompilerState.emit("\t strmove((unsigned char *) tmp" +
                    iiitmp + ",(unsigned char *) _ReturnVal); " +
                    "// Copy result\n")
        }
        return 0
    }

    // a non-local reference DO - has one or more ^

    if (i != 0) { // external name contains label
        name[i] = NUL
        lbl = "\""
        lbl = lbl + nameS
        lbl = lbl + "\""
        name[i] = '^'
        val tail = CstringOps.cstringAt(name, i)
        CstringOps.strcpyS(name, tail)
    }

    val nameAfter = CstringOps.cstring(name)

    if (nameAfter.startsWith("^^")) { // double ^
        val rest = nameAfter.substring(1)
        CompilerState.emit("\t_jj=Interpret(\"x \\\"do ^" + rest +
                "\\\"\",svPtr);\n")
        CompilerState.emit("\tsvPtr->ERROR=_jj;\n")
        CompilerState.emit("\tif (!svPtr->NOERR && _jj<0) " +
                "ErrorMessage(\"Indirection error\",svPtr->LineNumber);")
        return 0
    }

    if (nameAfter.startsWith("^")) { // single ^
        // valid LLL:  LLL^^RRR ?
        // C isalnum domain (ASCII letters/digits only; the wider
        // Kotlin digit domain would diverge)
        var li = 0
        while (li < lbl.length) {
            val lc = lbl[li]
            if ((lc in 'a'..'z') || (lc in 'A'..'Z') ||
                    (lc in '0'..'9')) {
                li++
                continue
            }
            if (lc == '%') {
                li++
                continue
            }
            if (lc == '"') {
                li++
                continue
            }
            return 9002
        }

        val rest = nameAfter.substring(1)
        CompilerState.emit("\t_jj=Interpret(\"x \\\"do ^" + rest +
                "\\\"\",svPtr);\n")
        CompilerState.emit("\tsvPtr->ERROR=_jj;\n")
        CompilerState.emit("\tif (!svPtr->NOERR && _jj<0) " +
                "ErrorMessage(\"Indirection error\",svPtr->LineNumber);")
        return 0
    }

    return 9003
}
