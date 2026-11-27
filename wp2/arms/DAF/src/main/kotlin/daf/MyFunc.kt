package daf

// R1 stub-completion (2026-11-27, C-020 erratum session, tranche 2):
// compiler-sys1.c::MyFunc (89 LOC physical, roster row 6,
// complex, tier 1): the compile-time extrinsic-function fold of
// the emitted-C back end. The seat parses the incoming
// name-and-arguments text (the fold of the vendor v1d carrier:
// the call site passes strAt(C.v1d, 3), the String content of
// &v1d[3]), cuts the function name, checks it against the
// builtinTable.h roster (BuiltInTableD, the unit-216 seat), then
// emits the __ArgN setup lines (Extract1 relay for builtins with
// plain args, Extract2 relay otherwise - both roster-b1 seats,
// extract1Daf landed, extract2Daf raised and called exactly as
// the landed Expand* siblings call it), tracks ArgMax
// (argMaxD, the unit-213 epi seat), and emits the local-DO jump
// lines or the single-/double-caret external Interpret lines.
// The in-place v1d name-stripping strcpy(v1d,&v1d[i+1]) renders
// as local String re-slicing (row 45 family hit-adapt). The
// emitted-C quote characters are built with the DQ constant (a
// source-shaping fold of this render; the emitted text itself is
// the vendor text verbatim).
// Consults (2026-11-27T00:00:00Z, nominal): BASE-log row 2
// family hit-reuse (fprintf(Out,...) rides DafCompilerState.emit
// with the emitted-C text kept VERBATIM: the tab prefixes, the
// __Arg%d shapes, the D1002/D1003 setup comments, the setjmp/
// _doStack/_doStx/sym_(99,...)/Interpret/ErrorMessage lines and
// their quote escapes, and the vendor trailing-newline shapes -
// the !NOERR check lines carry NO trailing newline, kept);
// row 16 hit-reuse (adjacent C literals fold into one Kotlin
// template); rows 111/68 hit-reuse (BuiltInTableD, argMaxD,
// exitCodeD, DafCompilerState.lines seats; the too-many-args
// printf pair renders as the vendor-message println precedent of
// the MpsSqrt fold); row 30 hit-adapt (the char name[128]/
// lbl[128] buffers render as String slices).
// Vendor quirks kept: (1) the too-many-args path prints the
// message, sets ExitCode 99 and RESETS k to 9 (the counter
// restarts rather than aborting; k then re-climbs - kept
// verbatim); (2) the k>29 test sits at loop top so a long arg
// list can fire it more than once; (3) the local-DO emission
// writes the goto line and the Jump comment as two separate
// fprintf calls - kept as two emit calls; (4) j = v1d[i] and
// strcpy(bd,"gtmp") are dead stores (the bd/j fossils are not
// carried, disclosed); (5) the stray k argument of the
// _doStx>127 fprintf (no matching format) - the emission text
// kept without it; (6) the local path emits the result copy only
// when iiitmp >= 0; (7) the vendor label-validation loop only
// accepts alnum, % and double-quote bytes in lbl; anything else
// returns 9002; (8) unmatched shapes return 9003 (the call site
// folds that as the parse_ 9002 return); (9) the name scan stops
// at a char >= 127 (the coded-marker domain) or a space.
// Dafny: EXEMPT (emission-shaping into the Out writer; journal
// row).

private const val DQ = "\"" // the emitted-C double-quote character

fun myFuncDaf(v1dIn: String, iiitmp: Int): Int {

    var v1d = v1dIn // the carrier re-slice fold of the strcpy strips
    var lbl = DQ + DQ // char lbl[128]="\"\""

    // for (i = 0; v1d[i] != 0 && v1d[i] < 127 && v1d[i] != ' ';
    //      i++) name[i] = v1d[i];
    var i = 0
    while (i < v1d.length && v1d[i].code < 127 && v1d[i] != ' ') i++
    var name = v1d.substring(0, i)

    // if (v1d[i] != 0) strcpy (v1d, &v1d[i + 1]);
    // else strcpy (v1d, "");
    v1d = if (i < v1d.length) v1d.substring(i + 1) else ""

    // check if fcn is known builtin function against table of
    // names (strlen(BuiltIn[j])!=0 loop end - the "" sentinel of
    // the BuiltInTableD seat)
    var BFlg = 0
    for (s in BuiltInTableD) {
        if (s.isEmpty()) break
        if (name == s) { BFlg = 1; break }
    }

    var k = 0 // int j = 1 is a dead store; not carried

    // find arguments
    while (v1d.isNotEmpty()) { // while (strlen (v1d) > 0)

        if (k > 29) {
            println("*** Too Many Extrinsic Function Arguments" +
                " in or near line " + DafCompilerState.lines)
            exitCodeD = 99 // ExitCode = 99
            k = 9 // vendor quirk: counter resets to 9, not abort
        }

        // for (i = 0; v1d[i] != 0 && v1d[i] < 127; i++);
        // j = v1d[i]; v1d[i] = 0; (the j dead store not carried)
        i = 0
        while (i < v1d.length && v1d[i].code < 127) i++
        val arg = v1d.substring(0, i) // v1d[i] = 0

        if (BFlg == 1 && !arg.startsWith("#S#")) {
            // fprintf(Out, "\t strmove((unsigned char *) __Arg%d,
            //  (unsigned char *) %s); // arg setup D1002\n",
            //  k, Extract1(v1d)); - verbatim
            DafCompilerState.emit(
                "\t strmove((unsigned char *) __Arg" + k +
                    ", (unsigned char *) " +
                    extract1Daf(arg.toCharArray(), 0) +
                    "); // arg setup D1002\n")
        } else {
            // ... D1003 ... Extract2(v1d) - verbatim
            DafCompilerState.emit(
                "\t strmove((unsigned char *) __Arg" + k +
                    ", (unsigned char *) " +
                    extract2Daf(arg) +
                    "); // arg setup D1003\n")
        }

        // strcpy (v1d, &v1d[i + 1]);
        v1d = if (i < v1d.length) v1d.substring(i + 1) else ""
        k++
    }

    if (k > argMaxD) argMaxD = k // if (k>ArgMax) ArgMax=k;
    // strcpy (bd, "gtmp"); - dead store, not carried

    DafCompilerState.emit("\t _ArgCount=" + k + ";\n")

    // for (i=0; name[i]!=0; i++) if (name[i]=='^') break;
    i = 0
    while (i < name.length) {
        if (name[i] == '^') break
        i++
    }

    // find start of external name
    if (i >= name.length || name[i] != '^') { // a local reference DO

        DafCompilerState.emit("\t _doStx++;\n")
        DafCompilerState.emit(
            "\t if (_doStx > 127 ) ErrorMessage(" + DQ +
                "Too many function calls" + DQ + ",svPtr->LineNumber);\n")

        DafCompilerState.emit(
            "\t sym_(99, (unsigned char *) " + DQ + DQ +
                ", (unsigned char *) " + DQ + DQ + ", svPtr); \n")

        if (name.isNotEmpty() && Character.isDigit(name[0])) {
            DafCompilerState.emit(
                "\t if (setjmp(_doStack[_doStx])==0) goto _" +
                    name + ";")
        } else {
            DafCompilerState.emit(
                "\t if (setjmp(_doStack[_doStx])==0) goto " +
                    name + ";")
        }
        DafCompilerState.emit("  /* Jump to function */\n")

        DafCompilerState.emit("\t _doStx--; \n")

        if (iiitmp >= 0) {
            DafCompilerState.emit(
                "\t strmove((unsigned char *) tmp" + iiitmp +
                    ",(unsigned char *) _ReturnVal); // Copy result\n")
        }
        return 0
    }

    // a non-local reference DO - has one or more ^
    if (i != 0) { // external name contains label
        // name[i]=0; strcpy(lbl,"\""); strcat; name=&name[i]
        lbl = DQ + name.substring(0, i) + DQ
        name = name.substring(i)
    }

    if (name.length >= 2 && name[0] == '^' && name[1] == '^') {
        // double ^
        DafCompilerState.emit(
            "\t_jj=Interpret(" + DQ + "x \\" + DQ + "do ^" +
                name.substring(1) + "\\" + DQ + DQ + ",svPtr);\n")
        DafCompilerState.emit("\tsvPtr->ERROR=_jj;\n")
        DafCompilerState.emit(
            "\tif (!svPtr->NOERR && _jj<0) ErrorMessage(" + DQ +
                "Indirection error" + DQ + ",svPtr->LineNumber);")
        return 0
    }

    if (name.isNotEmpty() && name[0] == '^') { // single ^
        // valid LLL:  LLL^^RRR ?
        var ok = true
        for (c in lbl) {
            if (Character.isLetterOrDigit(c)) continue
            if (c == '%') continue
            if (c == '"') continue
            ok = false
            break
        }
        if (!ok) return 9002

        DafCompilerState.emit(
            "\t_jj=Interpret(" + DQ + "x \\" + DQ + "do ^" +
                name.substring(1) + "\\" + DQ + DQ + ",svPtr);\n")
        DafCompilerState.emit("\tsvPtr->ERROR=_jj;\n")
        DafCompilerState.emit(
            "\tif (!svPtr->NOERR && _jj<0) ErrorMessage(" + DQ +
                "Indirection error" + DQ + ",svPtr->LineNumber);")
        return 0
    }

    return 9003
}
