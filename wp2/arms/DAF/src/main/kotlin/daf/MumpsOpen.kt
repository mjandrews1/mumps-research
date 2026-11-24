package daf

// Unit 241 (DAF B8 T3, complex): compiler-open.c::mumps_open
// (53 LOC, score 72.7): the OPEN command code-emitter: the
// openf-label loop is a Kotlin while (each comma-arg arm falls
// back to the loop head via the ++xpx/xd-check tail), the vendor
// walker state rides the DafCompilerState seats (t2/ierr,
// xd/xpx, pd1/sdlim, POST_flg - the landed readCode/writeCode
// walker idiom), and every fprintf(Out,...) maps to C.emit with
// the emitted text byte-verbatim (vendor compiler-open.c,
// physically verified this session).
// Consults (2026-11-24T21:22:30Z, nominal): the emit-text render
// pattern of the arm (the unit-228 ExpandText precedent); the
// Extract2 %s payload binds through the extract2Daf seat (roster
// b1, called - the seat cites its owner; this render is another
// in-arm caller, seat goes home unchanged); parse_ -> the parseDaf
// stub seat (interp-parse.cpp::parse_, roster b9); the atoi of
// the emitted text is EMITTED text, not runtime (the \%s payload
// splices verbatim); ARG_LIST_ERROR/PARSE_ERROR ride their
// DafInterpStubsT3 consts; the svPtr of parseDaf is the
// DafInterpState.svPtr of the compiler-TU convention (the
// readCode precedent); the strcmp("tmp0", &pd1[sdlim]) check
// folds onto the cstringAt string compare.
// Dafny: EXEMPT (emit-side compiler plumbing; journal row).

fun mumpsOpenD(): Int {
    val C = DafCompilerState
    val sv = DafInterpState.svPtr

    // openf: loop head; the trailing goto openf renders as the
    // while continue
    while (true) {
        C.t2 = 1
        C.ierr = parseDaf(sv)
        if (C.ierr != 0) return PARSE_ERROR_D // goto parse_err;

        C.emit("\t_i = atoi( CSTR " +
                extract2Daf(DafCRuntime.cstringAt(C.pd1, C.sdlim)) + ");\n")

        if (C.xd[C.xpx] != ':') return ARG_LIST_ERROR_D // arg_list_error;

        C.xpx++
        C.t2 = 1
        C.ierr = parseDaf(sv)
        if (C.ierr != 0) return PARSE_ERROR_D // goto parse_err;

        if (DafCRuntime.cstringAt(C.pd1, C.sdlim) != "tmp0")
            C.emit("\tstrmove((UNS) tmp0, (UNS) " +
                    extract2Daf(DafCRuntime.cstringAt(C.pd1, C.sdlim)) + ");\n")

        C.emit("\tfor(_j=0; tmp0[_j]!=0 && tmp0[_j]!=',';_j++);\n")

        C.emit("\tif (tmp0[_j]!=',') ")
        C.emit("\tErrorMessage(\"File open error\",svPtr->LineNumber);\n")

        C.emit("\ttmp0[_j++]=0;\n")
        C.emit("\tstrmove((UNS) gtmp, (UNS) tmp0);\n")

        C.emit("\tif (strcmp( (CCHR) &tmp0[_j], (CCHR) \"old\") == 0 || \n" +
                "\t\tstrcmp( (CCHR) &tmp0[_j], (CCHR) \"OLD\") == 0) _new = 0; \n")

        C.emit("\telse if (strcmp( (CCHR) &tmp0[_j], (CCHR) \"new\") == 0 || \n" +
                "\t\tstrcmp((CCHR) &tmp0[_j], (CCHR) \"NEW\") == 0) _new = 1; \n")

        C.emit("\telse if (strcmp( (CCHR) &tmp0[_j], (CCHR) \"append\") == 0 || \n" +
                "\t\tstrcmp((CCHR) &tmp0[_j], (const char *) \"APPEND\") == 0) _new=2; \n")

        C.emit("\telse ErrorMessage(\"File open error\",svPtr->LineNumber);\n")

        C.emit("\tif (svPtr->out_file[_i] != NULL || svPtr->in_file[_i] != NULL) \n")
        C.emit("\t\tErrorMessage(\"File already open\", svPtr->LineNumber); \n")

        C.emit("\tif (_new==1) { /* output */ \n")

        C.emit("\t\tsvPtr->out_file[_i] = fopen((const char *) gtmp, \"w\"); \n")

        C.emit("\t\tif(svPtr->out_file[_i] == NULL) svPtr->tpx = 0; \n")
        C.emit("\t\telse svPtr->tpx = 1; \n")
        C.emit("\t\t} \n")
        C.emit("\telse if (_new == 2) { /* append */ \n")

        C.emit("\t\tsvPtr->out_file[_i] = fopen((const char *) gtmp, \"a\"); \n")

        C.emit("\t\tif(svPtr->out_file[_i] == NULL) svPtr->tpx = 0; \n")
        C.emit("\t\telse svPtr->tpx = 1; \n")
        C.emit("\t\t} \n")
        C.emit("\telse { /* input */ \n")

        C.emit("\t\tsvPtr->in_file[_i] = fopen((const char *) gtmp, \"r\"); \n")

        C.emit("\t\tif(svPtr->in_file[_i] == NULL) svPtr->tpx = 0; \n")
        C.emit("\t\telse svPtr->tpx = 1; \n")
        C.emit("\t\t}\n")

        ++C.xpx
        if (at(C.xd, C.xpx) != ',') { // if (xd[++xpx] != ',')

            if (C.POST_flg != 0) {
                C.emit("\t} /* post conditional open */\n")
                C.POST_flg = 0
            }
            return 0
        }

        C.xpx++ // xpx++; goto openf;
    }
}
