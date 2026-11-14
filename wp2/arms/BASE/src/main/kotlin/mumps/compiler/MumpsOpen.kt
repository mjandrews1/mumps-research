package mumps.compiler

import mumps.compiler.CompilerState.Out

/**
 * Kotlin translation of compiler-open.c::mumps_open (Study Subset unit 3,
 * Batch 1, arm BASE; translated 2026-11-14).
 *
 * Emits C code for the Mumps OPEN command: device, then ":mode", with
 * comma-separated OPEN argument pairs looping back to the top (C: openf).
 */
fun mumpsOpen(): Int {
    val c = CompilerState
    fun xd(i: Long): Char = c.xd[i.toInt()]

    while (true) { // C: label openf
        c.t2 = 1
        var rc = parse_()
        c.ierr = rc
        if (rc != 0.toShort()) return CompilerState.PARSE_ERROR // C: goto parse_err

        Out.write("\t_i = atoi( CSTR " + extract2(c.pd1, c.sdlim.toInt()) + ");\n")

        if (xd(c.xpx) != ':') return CompilerState.ARG_LIST_ERROR // C: goto arg_list_error
        c.xpx++

        c.t2 = 1
        rc = parse_()
        c.ierr = rc
        if (rc != 0.toShort()) return CompilerState.PARSE_ERROR

        // C: strcmp("tmp0", &pd1[sdlim]) — raw buffer compare, not Extract2
        if (c.pd1.substring(c.sdlim.toInt()).toString() != "tmp0")
            Out.write("\tstrmove((UNS) tmp0, (UNS) " + extract2(c.pd1, c.sdlim.toInt()) + ");\n")

        Out.write("\tfor(_j=0; tmp0[_j]!=0 && tmp0[_j]!=',';_j++);\n")
        Out.write("\tif (tmp0[_j]!=',') ")
        Out.write("ErrorMessage(\"File open error\",svPtr->LineNumber);\n")

        Out.write("\ttmp0[_j++]=0;\n")
        Out.write("\tstrmove((UNS) gtmp, (UNS) tmp0);\n")

        Out.write("\tif (strcmp( (CCHR) &tmp0[_j], (CCHR) \"old\") == 0 || \n" +
                  "\t\tstrcmp( (CCHR) &tmp0[_j], (CCHR) \"OLD\") == 0) _new = 0; \n")
        Out.write("\telse if (strcmp( (CCHR) &tmp0[_j], (CCHR) \"new\") == 0 || \n" +
                  "\t\tstrcmp((CCHR) &tmp0[_j], (CCHR) \"NEW\") == 0) _new = 1; \n")
        Out.write("\telse if (strcmp( (CCHR) &tmp0[_j], (CCHR) \"append\") == 0 || \n" +
                  "\t\tstrcmp((CCHR) &tmp0[_j], (const char *) \"APPEND\") == 0) _new=2; \n")
        Out.write("\telse ErrorMessage(\"File open error\",svPtr->LineNumber);\n")

        Out.write("\tif (svPtr->out_file[_i] != NULL || svPtr->in_file[_i] != NULL) \n")
        Out.write("\t\tErrorMessage(\"File already open\", svPtr->LineNumber); \n")

        Out.write("\tif (_new==1) { /* output */ \n")
        Out.write("\t\tsvPtr->out_file[_i] = fopen((const char *) gtmp, \"w\"); \n")
        Out.write("\t\tif(svPtr->out_file[_i] == NULL) svPtr->tpx = 0; \n")
        Out.write("\t\telse svPtr->tpx = 1; \n")
        Out.write("\t\t} \n")
        Out.write("\telse if (_new == 2) { /* append */ \n")
        Out.write("\t\tsvPtr->out_file[_i] = fopen((const char *) gtmp, \"a\"); \n")
        Out.write("\t\tif(svPtr->out_file[_i] == NULL) svPtr->tpx = 0; \n")
        Out.write("\t\telse svPtr->tpx = 1; \n")
        Out.write("\t\t} \n")
        Out.write("\telse { /* input */ \n")
        Out.write("\t\tsvPtr->in_file[_i] = fopen((const char *) gtmp, \"r\"); \n")
        Out.write("\t\tif(svPtr->in_file[_i] == NULL) svPtr->tpx = 0; \n")
        Out.write("\t\telse svPtr->tpx = 1; \n")
        Out.write("\t\t}\n")

        c.xpx++ // C: xd[++xpx]
        if (xd(c.xpx) != ',') {
            if (c.POST_flg) {
                Out.write("\t} /* post conditional open */\n")
                c.POST_flg = false
            }
            return 0 // C: goto next_cmnd
        }
        c.xpx++
        // C: goto openf
    }
}
