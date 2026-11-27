package daf

// R1 stub-completion (2026-11-27, C-020 erratum session, tranche 2):
// compiler-parse.c::ExpandGet (47 LOC, roster row 16, core): the
// compiler-TU \\u0024get() expander of the emitted-C back end.
// After the marker, the #G# (global) arm emits the GlobalGet line
// and the #S# (subscripted-local) arm emits the LocalGet line;
// both arms cut the two #-delimited argument names, the optional
// CodedComma third argument feeds arg3 (the Extract2 relay for
// nested markers else the <128 byte run), and bp receives the
// second argument.
// Fold contract: the unit-173 ExpandOrder precedent of this same
// TU family - render returns String? with null meaning buffer
// untouched; the call site does putAt(bp, 0, r).
// Consults (2026-11-27T00:00:00Z, nominal): BASE-log rows
// 44/45/2/16/111/68 hit-reuse (the ExpandOrder family folds:
// conditional bp write, StringBuilder argument scan with the at()
// end-guard, DafCompilerState.emit with the emitted-C text kept
// VERBATIM, adjacent C literals folded into one Kotlin template,
// the CODED_COMMA_D (207) seat, the extract2Daf relay of roster
// b1 - Extract2 is itself a raised seat, called exactly as the
// landed ExpandOrder/ExpandData siblings call it).
// Vendor quirks kept: (1) the #S# arm nested-marker test reads
// "#S#" OR "#S#" - the vendor tests the SAME marker twice (a
// copy-paste bug: a nested #G# arg3 falls to the byte-run path);
// kept verbatim, disclosed; (2) the no-comma case writes the
// literal NULL into arg3; (3) the arg3 run loop reads v1d[i]<128;
// (4) an unmatched marker writes NOTHING to Out and leaves bp
// untouched (the null return).
// Dafny: EXEMPT (emission-shaping into the Out writer; journal
// row).

fun expandGetDaf(v1d: CharArray, i0: Int): String? {

    var arg1: String // char arg1[32]
    var arg2: String // char arg2[32]
    var arg3: String // char arg3[32]
    var i = i0

    i++ // i++;

    // ---- the #G# arm ----
    if (i + 2 < v1d.size && at(v1d, i) == '#' && at(v1d, i + 1) == 'G'
        && at(v1d, i + 2) == '#') { // strncmp(&v1d[i], "#G#", 3) == 0
        i += 3

        val a1 = StringBuilder()
        while (at(v1d, i) != '#') { // for (j=0; v1d[i]!='#'; j++)
            a1.append(v1d[i])
            i++
        }
        arg1 = a1.toString()
        i++

        val a2 = StringBuilder()
        while (at(v1d, i) != '#') {
            a2.append(v1d[i])
            i++
        }
        arg2 = a2.toString()
        i++

        if (at(v1d, i) == CODED_COMMA_D) { // if (v1d[i]==CodedComma)
            i++
            if (i + 2 < v1d.size && ((at(v1d, i) == '#'
                        && (at(v1d, i + 1) == 'G' || at(v1d, i + 1) == 'S')
                        && at(v1d, i + 2) == '#'))) {
                // strcpy(arg3, Extract2(&v1d[i]));
                arg3 = extract2Daf(DafCRuntime.cstringAt(v1d, i))
            } else {
                // for (j=0; v1d[i]<128&&v1d[i]!='\0'; j++)
                val a3 = StringBuilder()
                while (at(v1d, i) < '\u0080' && at(v1d, i) != NUL_D) {
                    a3.append(v1d[i])
                    i++
                }
                arg3 = a3.toString()
            }
        } else {
            arg3 = "NULL" // strcpy(arg3,"NULL")
        }

        // fprintf(Out, "\tGlobalGet( ... svPtr); \n", ...) - verbatim
        DafCompilerState.emit(
            "\tGlobalGet( (unsigned char *) " + arg1
                + ", (unsigned char *) " + arg2 + ", "
                + "(unsigned char *) " + arg3 + ", svPtr); \n")

        return arg2 // strcpy(bp, arg2);
    }

    // ---- the #S# arm ----
    if (i + 2 < v1d.size && at(v1d, i) == '#' && at(v1d, i + 1) == 'S'
        && at(v1d, i + 2) == '#') { // strncmp(&v1d[i], "#S#", 3)==0
        i += 3

        val a1 = StringBuilder()
        while (at(v1d, i) != '#') {
            a1.append(v1d[i])
            i++
        }
        arg1 = a1.toString()
        i++

        val a2 = StringBuilder()
        while (at(v1d, i) != '#') {
            a2.append(v1d[i])
            i++
        }
        arg2 = a2.toString()
        i++

        if (at(v1d, i) == CODED_COMMA_D) {
            i++
            // VENDOR QUIRK KEPT: the nested-marker test reads
            // "#S#" twice (strncmp "=="#S#",3)==0 ||
            // strncmp("=="#S#",3)==0) - only #S# relays through
            // Extract2; a nested #G# takes the byte-run path
            if (i + 2 < v1d.size && (at(v1d, i) == '#'
                        && at(v1d, i + 1) == 'S'
                        && at(v1d, i + 2) == '#')) {
                arg3 = extract2Daf(DafCRuntime.cstringAt(v1d, i))
            } else {
                val a3 = StringBuilder()
                while (at(v1d, i) < '\u0080' && at(v1d, i) != NUL_D) {
                    a3.append(v1d[i])
                    i++
                }
                arg3 = a3.toString()
            }
        } else {
            arg3 = "NULL"
        }

        // fprintf(Out, "\tLocalGet( ... svPtr); \n", ...) - verbatim
        DafCompilerState.emit(
            "\tLocalGet( (unsigned char *) " + arg1
                + ", (unsigned char *) " + arg2 + ", "
                + "(unsigned char *) " + arg3 + ", svPtr); \n")

        return arg2 // strcpy(bp, arg2);
    }

    // no marker matched: Out untouched, bp untouched
    return null
}
