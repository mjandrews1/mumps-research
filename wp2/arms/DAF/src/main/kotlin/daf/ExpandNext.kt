package daf

// R1 stub-completion (2026-11-28, C-020 erratum session, tranche 6):
// compiler-parse.c::ExpandNext (roster row 22, core): the
// compiler-TU \u0024next() expander of the emitted-C back end -
// the ExpandQS/ExpandGet sibling of the same TU family. After
// the marker, the #G# (global) arm cuts the two #-delimited
// argument names and emits the GlobalOrder line (with the
// strlen/-1 guard follow-up line), the #S# (subscripted local)
// arm emits the LocalNext line; both arms write arg2 into bp.
// Fold contract: the unit-173 ExpandOrder precedent of this
// same family - render returns String? with null meaning
// buffer untouched (conditional out-param write); the call
// site in CompilerParseC.kt does the putAt(bp, 0, r) write.
// Consults (2026-11-28T00:00:00Z, nominal): BASE-log rows
// 44/45/2/16 hit-reuse (the same family folds the ExpandQS
// render cites: conditional bp write, StringBuilder argument
// scan with the at() end-guard, DafCompilerState.emit with the
// emitted-C text kept VERBATIM - the //2306 and strlen guard
// comment tags; adjacent C literals fold into one Kotlin
// template).
// Vendor quirks kept: (1) the arg scans have NO trailing
// NUL write after arg1/arg2 (the char arg1[32]="" zero init
// covers them) - the Kotlin String cut models the same; (2) an
// unmatched marker writes NOTHING to Out and leaves bp
// untouched (the null return of the unit-135/ExpandOrder
// fold); (3) the #G# arm emits the "-1" guard line UNCONDITION-
// ALLY after the GlobalOrder line (not inside a runtime test) -
// kept verbatim.
// Dafny: EXEMPT (emission-shaping into the Out writer; journal
// row).

fun expandNextDaf(v1d: CharArray, i0: Int): String? {

    var i = i0

    i++ // i++;

    // ---- the #G# arm ----
    if (i + 2 < v1d.size && at(v1d, i) == '#' && at(v1d, i + 1) == 'G'
        && at(v1d, i + 2) == '#') { // strncmp(&v1d[i], "#G#", 3) == 0
        i += 3

        val a1 = StringBuilder() // char arg1[32]
        while (at(v1d, i) != '#') { // for (j=0; v1d[i]!='#'; j++)
            a1.append(v1d[i])
            i++
        }
        val arg1 = a1.toString()
        i++

        val a2 = StringBuilder() // char arg2[32]
        while (at(v1d, i) != '#') {
            a2.append(v1d[i])
            i++
        }
        val arg2 = a2.toString()

        // fprintf(Out, "\tGlobalOrder(%s, %s, (unsigned char *) \"1\",
        //          svPtr); //2306\n", arg1, arg2) - verbatim
        DafCompilerState.emit(
            "\tGlobalOrder(" + arg1 + ", " + arg2 +
                ", (unsigned char *) \"1\", svPtr); //2306\n")

        // fprintf(Out, "\tif (strlen((char *)%s)==0)
        //          strcpy((char *)%s,\"-1\");\n", arg2, arg2)
        // - verbatim (the vendor adjacent C literals fold)
        DafCompilerState.emit(
            "\tif (strlen((char *)" + arg2 +
                ")==0) strcpy((char *)" + arg2 + ",\"-1\");\n")

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
        val arg1 = a1.toString()
        i++

        val a2 = StringBuilder()
        while (at(v1d, i) != '#') {
            a2.append(v1d[i])
            i++
        }
        val arg2 = a2.toString()

        // fprintf(Out, "\tLocalNext((unsigned char *) %s,
        //          (unsigned char *) %s,svPtr);\n", arg1, arg2)
        // - verbatim
        DafCompilerState.emit(
            "\tLocalNext((unsigned char *) " + arg1 +
                ",(unsigned char *) " + arg2 + ",svPtr);\n")

        return arg2 // strcpy(bp, arg2);
    }

    // no marker matched: Out untouched, bp untouched
    return null
}
