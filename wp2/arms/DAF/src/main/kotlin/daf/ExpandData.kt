package daf

// Unit 144 (DAF B5 T2, core): compiler-parse.c::ExpandData
// (units.csv row 144: loc 24, ptr 0, ctrl 6, score 11.4, rank 113,
// core): the compiler-TU #G#/#S# set-lhs data expander of the
// emitted-C back end: after the marker, the two #-delimited
// argument names are cut, the GlobalData / LocalData emission line
// is written to Out, and bp receives the second argument.
// Consults: BASE-log row 44 hit-reuse (conditional out-param
// write: the bp copy lands only on the matched branches, so the
// render returns String? with null meaning buffer untouched - the
// ExpandNext precedent), row 45 family hit-adapt (the char arg1/
// arg2[32] zero-filled scan buffers render as StringBuilder appends
// up to the # terminator; reads ride the at() end-guard of the
// v1d CharArray at DAF), row 2 family hit-reuse (the fprintf(Out,
// ...) emission rides DafCompilerState.emit with the emitted-C
// text kept verbatim: the tab prefix, the svPtr argument and the
// (unsigned char *) casts of the LocalData line are byte-verbatim),
// row 16 hit-reuse (adjacent C literals fold into one Kotlin
// template). The vendor emits the GlobalData line UNCAST and the
// LocalData line CAST - both kept exactly.
// Dafny: EXEMPT (emission-shaping into the Out writer; journal
// row).

fun expandDataD(v1d: CharArray, i0: Int): String? {

    val arg1 = StringBuilder() // char arg1[32]="";
    val arg2 = StringBuilder() // char arg2[32]="";
    var i = i0 // int i (the call site passes the post-marker index)

    i++ // i++;
    if (i + 2 < v1d.size && at(v1d, i) == '#' && at(v1d, i + 1) == 'G'
        && at(v1d, i + 2) == '#') { // strncmp(&v1d[i],"#G#",3)==0
        i += 3
        while (at(v1d, i) != '#') { // for (j=0; v1d[i]!='#'; j++)
            arg1.append(v1d[i])
            i++
        }
        i++
        while (at(v1d, i) != '#') { // for (j=0; v1d[i]!='#'; j++)
            arg2.append(v1d[i])
            i++
        }
        DafCompilerState.emit(
            "\tGlobalData(" + arg1 + "," + arg2 + ",svPtr);\n")
        // fprintf(Out,"\tGlobalData(%s,%s,svPtr);\n",arg1,arg2);
        return arg2.toString() // strcpy(bp, arg2);
    }

    if (i + 2 < v1d.size && at(v1d, i) == '#' && at(v1d, i + 1) == 'S'
        && at(v1d, i + 2) == '#') { // strncmp(&v1d[i],"#S#",3)==0
        i += 3
        while (at(v1d, i) != '#') {
            arg1.append(v1d[i])
            i++
        }
        i++
        while (at(v1d, i) != '#') {
            arg2.append(v1d[i])
            i++
        }
        DafCompilerState.emit(
            "\tLocalData((unsigned char *) " + arg1 +
            ",(unsigned char *) " + arg2 + ",svPtr);\n")
        // fprintf(Out,"\tLocalData((unsigned char *) %s,
        // (unsigned char *) %s,svPtr);\n",arg1,arg2);
        return arg2.toString() // strcpy(bp, arg2);
    }

    return null // fall-off-the-end: bp untouched
}
