package daf

// Unit 173 (DAF B6 T2, core): compiler-parse.c::ExpandOrder
// (50 LOC, score 24.2, rank 185): the compiler-TU \$Order expander of
// the emitted-C back end: after the marker, the #G# (global) and
// #S# (subscripted-local) arms cut the two #-delimited argument
// names, the optional CodedComma third argument feeds arg3 (the
// Extract2 relay for nested #G#/#S# else the <128 byte run), the
// GlobalOrder / LocalOrder emission line is written to Out, and bp
// receives the second argument.
// Consults (2026-11-22T16:04:00Z, nominal): BASE-log row 44
// hit-reuse (conditional out-param write: bp receives arg2 only on
// the matched arms; the render returns String? with null meaning
// buffer untouched - the ExpandData unit-135 precedent of this same
// TU family), row 45 family hit-adapt (the char arg1/arg2/arg3[32]
// scan buffers render as StringBuilder appends up to the # or
// <128-terminator; reads ride the at() end-guard of the v1d
// CharArray), row 2 family hit-reuse (the fprintf(Out,...) emission
// rides DafCompilerState.emit with the emitted-C text kept
// VERBATIM: the tab prefixes, the (unsigned char *) casts, the
// svPtr argument, the p2236 comment of the GlobalOrder line and
// the trailing-newline shapes of both vendor lines), row 16
// hit-reuse (adjacent C literals fold into one Kotlin template),
// rows 111/68 hit-reuse (CodedComma is the CODED_COMMA_D const
// (207) seated at the unit-97 MstringFnumber render; Extract2
// relays to the existing extract2Daf seat of roster b1).
// Vendor quirks kept: (1) the arg3 run loop reads v1d[i]<128 -
// a byte-domain cap the Unicode-char carrier models with the same
// <128 test on the Char; (2) the no-comma case writes the literal
// NULL (not the empty string) into arg3 - kept, the emitted lines
// then read GlobalOrder(..., NULL, svPtr); (3) the emission line
// for #G# ends with a // p2236 comment and the #S# line does not -
// both kept verbatim; (4) an unmatched marker (neither #G# nor
// #S#) writes NOTHING to Out and leaves bp untouched (the fall-
// through-to-null return of the unit-135 fold).
// Dafny: EXEMPT (emission-shaping into the Out writer; journal
// row).

fun expandOrderD(v1d: CharArray, i0: Int): String? {

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
        while (at(v1d, i) != '#') { // for (j=0; v1d[i]!='#'; j++)
            a2.append(v1d[i])
            i++
        }
        arg2 = a2.toString()
        i++

        // if (v1d[i]==CodedComma) { i++; ... } else strcpy(arg3,"NULL");
        if (at(v1d, i) == CODED_COMMA_D) {
            i++

            if (i + 2 < v1d.size && ((at(v1d, i) == '#'
                        && (at(v1d, i + 1) == 'G' || at(v1d, i + 1) == 'S')
                        && at(v1d, i + 2) == '#'))) {
                // strcpy(arg3, Extract2(&v1d[i]));
                arg3 = extract2Daf(DafCRuntime.cstringAt(v1d, i))
            } else {
                // for (j=0; v1d[i]<128&&v1d[i]!='\0'; j++)
                //      arg3[j]=v1d[i++];
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

        // fprintf(Out, "\tGlobalOrder(...// p2236\n", ...) - verbatim
        DafCompilerState.emit(
            "\tGlobalOrder((unsigned char *) " + arg1
                + ", (unsigned char *) " + arg2 + " \n"
                + "\t\t(unsigned char *) " + arg3
                + ", svPtr); // p2236\n")

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

            if (i + 2 < v1d.size && ((at(v1d, i) == '#'
                        && (at(v1d, i + 1) == 'S' || at(v1d, i + 1) == 'G')
                        && at(v1d, i + 2) == '#'))) {
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

        // fprintf(Out, "\tLocalOrder(...); \n", ...) - verbatim
        DafCompilerState.emit(
            "\tLocalOrder((unsigned char *) " + arg1
                + ", (unsigned char *) " + arg2
                + ", (unsigned char *) " + arg3 + ", svPtr); \n")

        return arg2 // strcpy(bp, arg2);
    }

    // no marker matched: Out untouched, bp untouched
    return null
}
