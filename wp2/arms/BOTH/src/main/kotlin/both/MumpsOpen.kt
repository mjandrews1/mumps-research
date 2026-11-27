package both

// Unit 221 render: compiler-open.c::mumps_open (roster 221,
// batch 8, 75 LOC). int mumps_open() - the OPEN command
// emitter: the openf loop (the vendor goto), the parse_ pair
// around the colon check, the tmp0 strmove, and the
// old/new/append fopen dispatch emission with the
// already-open guard and the postconditional close.
//
// Consults (rule base wp2/arms/BOTH/rules_v0.pl):
//   h004 globals_ext - reused: t2/ierr/xpx/xd/pd1/sdlim/
//     POST_flg ride the CompilerGlobalsExt seats (t2G/ierr/
//     xpxG/xdG/pd1/sdlim/POST_flg); the FILE* Out rides the
//     CompilerOutState.out singleton (the outp fold).
//   h009 format_string - reused: the emissions keep the
//     vendor fprintf shapes exactly (the atoi CSTR splice,
//     the for(_j...) walk line, the old/OLD, new/NEW,
//     append/APPEND strcmp ladders with the (CCHR) casts; the
//     "w"/"a"/"r" fopen modes).
//   h014 out_of_scope_call - reused: parse_ is a NEW no-arg
//     TODO stub raised here (owner compiler-parse.c::parse_,
//     roster 277 batch 9 - RELEASED at the unit-277 render,
//     CompilerParseMain.kt; the old pin said "roster 248", the
//     DOC-arm roster number - C-015; the no-arg compiler
//     OVERLOAD, distinct from the seated parse_(sv: Msv)
//     roster-303 stub; the WriteCode ledger pin); Extract2 is
//     a NEW TODO stub raised here (owner compiler-parse.c::
//     Extract2, roster 15 batch 1 pending - the old pin said
//     "roster b2/b3", C-015); PARSE_
//     ERROR 1 / ARG_LIST_ERROR 2 seat locally (the
//     compiler-macros.h error block, record 226 ledger).
//   h015 grammar-collision - reused: ASCII only, no
//     backticks, no raw comment dollars, trailing newline.
//   h017 naming - reused: mumps_open keeps vendor name (the
//     C identifier; the Kotlin open keyword does not collide
//     as a function name suffix).
//
// Quirks kept (ledgered):
//   - the vendor openf: label + goto tail is a while(true)
//     with continue (the loop head re-seats t2=1 each pass).
//   - the "tmp0" strcmp compares BEFORE the strmove emission
//     (the caller may already have landed tmp0 - kept).
//   - the postconditional block closes ONLY on the non-comma
//     exit (the comma path loops without closing - kept).
//   - the goto-openf re-entry point is AFTER the t2=1 store
//     (the second parse_ call sits inside the loop, not at
//     the label - kept exact).
//   - the vendor // goto parse_err / // arg_list_error
//     comment-only gotos kept as the return comment notes.

// parse_() - the no-arg parse_() (owner roster 277) RELEASED at
// the unit-277 render, CompilerParseMain.kt; called direct, NOT
// re-raised here (the h014 release discipline).

// char * Extract2(unsigned char *) - owner:
// compiler-parse.c::Extract2 (roster 15, batch 1 pending -
// the old pin said "roster b2/b3", C-015; the Extract1/
// Extract2a twins are rendered - records 219/230).
// TODO-stub (h014).
internal fun Extract2(s: String): String {
    TODO("stub: owner compiler-parse.c::Extract2 (roster 15, batch 1 pending)")
}

// PARSE_ERROR 1 / ARG_LIST_ERROR 2 - the seated package consts
// (WriteCode:82 / ReadCode:80, the compiler-macros.h pins); used
// direct.

// The fprintf(Out, ...) fold (the WriteCode outp precedent).
private fun outpMo(s: String): Int {
    try {
        CompilerOutState.out.write(s)
    } catch (e: java.io.IOException) {
        throw RuntimeException(e)
    }
    return s.length
}

// &pd1[sdlim] .. NUL (the WriteCode pd1strAt seat shape).
private fun pd1strAtMo(pd1: CharArray, off: Int): String {
    val sb = StringBuilder()
    var p = off
    while (pd1[p] != '\u0000') {
        sb.append(pd1[p])
        p++
    }
    return sb.toString()
}

// int mumps_open() - compiler-open.c
internal fun mumps_open(): Int {
    val g = CompilerGlobalsExt

    while (true) { // openf:

        g.t2G = 1
        g.ierr = parse_().toShort()
        if (g.ierr.toInt() != 0) return PARSE_ERROR // goto parse_err;
        outpMo(
            "\t_i = atoi( CSTR " +
                Extract2(pd1strAtMo(g.pd1, g.sdlim.toInt())) + ");\n"
        )
        if (g.xdG[g.xpxG.toInt()] != ':') return ARG_LIST_ERROR // arg_list_error;
        g.xpxG++
        g.t2G = 1
        g.ierr = parse_().toShort()
        if (g.ierr.toInt() != 0) return PARSE_ERROR // goto parse_err;

        if (pd1strAtMo(g.pd1, g.sdlim.toInt()) != "tmp0") {
            outpMo(
                "\tstrmove((UNS) tmp0, (UNS) " +
                    Extract2(pd1strAtMo(g.pd1, g.sdlim.toInt())) + ");\n"
            )
        }

        outpMo("\tfor(_j=0; tmp0[_j]!=0 && tmp0[_j]!=',';_j++);\n")

        outpMo("\tif (tmp0[_j]!=',') ")
        outpMo("\tErrorMessage(\"File open error\",svPtr->LineNumber);\n")

        outpMo("\ttmp0[_j++]=0;\n")
        outpMo("\tstrmove((UNS) gtmp, (UNS) tmp0);\n")

        outpMo(
            "\tif (strcmp( (CCHR) &tmp0[_j], (CCHR) \"old\") == 0 || \n" +
                "\t\tstrcmp( (CCHR) &tmp0[_j], (CCHR) \"OLD\") == 0) _new = 0; \n"
        )

        outpMo(
            "\telse if (strcmp( (CCHR) &tmp0[_j], (CCHR) \"new\") == 0 || \n" +
                "\t\tstrcmp((CCHR) &tmp0[_j], (CCHR) \"NEW\") == 0) _new = 1; \n"
        )

        outpMo(
            "\telse if (strcmp( (CCHR) &tmp0[_j], (CCHR) \"append\") == 0 || \n" +
                "\t\tstrcmp( (CCHR) &tmp0[_j], (const char *) \"APPEND\") == 0) _new=2; \n"
        )

        outpMo(
            "\telse ErrorMessage(\"File open error\",svPtr->LineNumber);\n"
        )

        outpMo(
            "\tif (svPtr->out_file[_i] != NULL || svPtr->in_file[_i] != NULL) \n"
        )
        outpMo(
            "\t\tErrorMessage(\"File already open\", svPtr->LineNumber); \n"
        )

        outpMo("\tif (_new==1) { /* output */ \n")

        outpMo(
            "\t\tsvPtr->out_file[_i] = fopen((const char *) gtmp, \"w\"); \n"
        )

        outpMo("\t\tif(svPtr->out_file[_i] == NULL) svPtr->tpx = 0; \n")
        outpMo("\t\telse svPtr->tpx = 1; \n")
        outpMo("\t\t} \n")
        outpMo("\telse if (_new == 2) { /* append */ \n")

        outpMo(
            "\t\tsvPtr->out_file[_i] = fopen((const char *) gtmp, \"a\"); \n"
        )

        outpMo("\t\tif(svPtr->out_file[_i] == NULL) svPtr->tpx = 0; \n")
        outpMo("\t\telse svPtr->tpx = 1; \n")
        outpMo("\t\t} \n")
        outpMo("\telse { /* input */ \n")

        outpMo(
            "\t\tsvPtr->in_file[_i] = fopen((const char *) gtmp, \"r\"); \n"
        )

        outpMo("\t\tif(svPtr->in_file[_i] == NULL) svPtr->tpx = 0; \n")
        outpMo("\t\telse svPtr->tpx = 1; \n")
        outpMo("\t\t}\n")

        g.xpxG++
        if (g.xdG[g.xpxG.toInt()] != ',') {

            if (g.POST_flg.toInt() != 0) {
                outpMo("\t} /* post conditional open */\n")
                g.POST_flg = 0
            }
            return 0
        }

        g.xpxG++
        // goto openf;
    }
}
