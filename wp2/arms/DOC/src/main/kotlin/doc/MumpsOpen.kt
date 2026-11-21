package doc

// Unit 242 (B8 tranche 3): compiler-open.c::mumps_open (batch 8,
// complex stratum; LOC 53, score 72.7, rank 256; units.csv row
// 222). The OPEN command code-emitter: the openf-label loop is a
// Kotlin while (each comma-arg arm falls back to the loop head),
// the vendor-controlled walker state rides the CompilerState
// seats (t2, ierr, xd/xpx, pd1/sdlim, POST_flg per the landed
// read_code/write_code walker idiom), and every fprintf(Out,...)
// maps to CompilerState.emit with the emitted text byte-verbatim
// (the Extract2 %s payloads bind through the landed extract2
// seat, whose own #S#/#G# ternary emission runs as a side effect
// exactly as in the vendor). Consult H-015 hit-adapt: the
// emit-text render pattern of the arm (ExpandText unit-228
// precedent). Naming per H-017 (vendor mumps_open verbatim).

private const val PARSE_ERROR_MO = 1 // compiler-macros.h:69
private const val ARG_LIST_ERROR_MO = 2 // compiler-macros.h:70

fun mumps_open(): Int {

    // openf: (loop head; the trailing goto openf renders as continue)
    while (true) {
        CompilerState.t2 = 1
        CompilerState.ierr = Stubs.parse_()
        if (CompilerState.ierr != 0) return PARSE_ERROR_MO // goto parse_err;

        CompilerState.emit(
            "\t_i = atoi( CSTR " +
                    CstringOps.cstring(Stubs.Extract2(CompilerState.pd1, CompilerState.sdlim)) +
                    ");\n"
        )

        if (CompilerState.xd[CompilerState.xpxIdx()] != ':')
            return ARG_LIST_ERROR_MO // arg_list_error;

        CompilerState.xpxInc()
        CompilerState.t2 = 1
        CompilerState.ierr = Stubs.parse_()
        if (CompilerState.ierr != 0) return PARSE_ERROR_MO // goto parse_err;

        if (CstringOps.cstringAt(CompilerState.pd1, CompilerState.sdlim) != "tmp0")
            CompilerState.emit(
                "\tstrmove((UNS) tmp0, (UNS) " +
                        CstringOps.cstring(Stubs.Extract2(CompilerState.pd1, CompilerState.sdlim)) +
                        ");\n"
            )

        CompilerState.emit("\tfor(_j=0; tmp0[_j]!=0 && tmp0[_j]!=',';_j++);\n")

        CompilerState.emit("\tif (tmp0[_j]!=',') ")
        CompilerState.emit("\tErrorMessage(\"File open error\",svPtr->LineNumber);\n")

        CompilerState.emit("\ttmp0[_j++]=0;\n")
        CompilerState.emit("\tstrmove((UNS) gtmp, (UNS) tmp0);\n")

        CompilerState.emit(
            "\tif (strcmp( (CCHR) &tmp0[_j], (CCHR) \"old\") == 0 || \n" +
                    "\t\tstrcmp( (CCHR) &tmp0[_j], (CCHR) \"OLD\") == 0) _new = 0; \n"
        )
        CompilerState.emit(
            "\telse if (strcmp( (CCHR) &tmp0[_j], (CCHR) \"new\") == 0 || \n" +
                    "\t\tstrcmp((CCHR) &tmp0[_j], (CCHR) \"NEW\") == 0) _new = 1; \n"
        )
        CompilerState.emit(
            "\telse if (strcmp( (CCHR) &tmp0[_j], (CCHR) \"append\") == 0 || \n" +
                    "\t\tstrcmp((CCHR) &tmp0[_j], (const char *) \"APPEND\") == 0) _new=2; \n"
        )
        CompilerState.emit("\telse ErrorMessage(\"File open error\",svPtr->LineNumber);\n")

        CompilerState.emit("\tif (svPtr->out_file[_i] != NULL || svPtr->in_file[_i] != NULL) \n")
        CompilerState.emit("\t\tErrorMessage(\"File already open\", svPtr->LineNumber); \n")

        CompilerState.emit("\tif (_new==1) { /* output */ \n")
        CompilerState.emit("\t\tsvPtr->out_file[_i] = fopen((const char *) gtmp, \"w\"); \n")
        CompilerState.emit("\t\tif(svPtr->out_file[_i] == NULL) svPtr->tpx = 0; \n")
        CompilerState.emit("\t\telse svPtr->tpx = 1; \n")
        CompilerState.emit("\t\t} \n")
        CompilerState.emit("\telse if (_new == 2) { /* append */ \n")
        CompilerState.emit("\t\tsvPtr->out_file[_i] = fopen((const char *) gtmp, \"a\"); \n")
        CompilerState.emit("\t\tif(svPtr->out_file[_i] == NULL) svPtr->tpx = 0; \n")
        CompilerState.emit("\t\telse svPtr->tpx = 1; \n")
        CompilerState.emit("\t\t} \n")
        CompilerState.emit("\telse { /* input */ \n")
        CompilerState.emit("\t\tsvPtr->in_file[_i] = fopen((const char *) gtmp, \"r\"); \n")
        CompilerState.emit("\t\tif(svPtr->in_file[_i] == NULL) svPtr->tpx = 0; \n")
        CompilerState.emit("\t\telse svPtr->tpx = 1; \n")
        CompilerState.emit("\t\t}\n")

        CompilerState.xpxInc() // xd[++xpx]
        if (CompilerState.xd[CompilerState.xpxIdx()] != ',') {

            if (CompilerState.POST_flg != 0) {
                CompilerState.emit("\t} /* post conditional open */\n")
                CompilerState.POST_flg = 0
            }
            return 0
        }

        CompilerState.xpxInc() // xpx++
        // goto openf;
    }
}
