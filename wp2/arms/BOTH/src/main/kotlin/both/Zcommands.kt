package both

// Unit 191 render: compiler-zcommands.c::zcommands_code (roster
// 191, batch 7, 109 LOC). The Z-command dispatcher: ZNOP-style
// single-letter arms over the lower-cased xd[p2px] byte, each
// emitting generated-C text to Out and returning the
// compiler-macros control codes (NEXT_LINE / PARSE_ERROR /
// ABRTX / 0).
//
// Consults (rule base wp2/arms/BOTH/rules_v0.pl):
//   h014 out_of_scope_call - reused: linepad stub (owner
//     compiler-sys1.c:949 sys1 helper, not a corpus unit,
//     WriteCode seat) and parse_ stub (owner
//     compiler-parse.c::parse_, roster 247 per units.csv row -
//     the WriteCode pin says 248, which is the RANK column;
//     same rank/unit mislabel family as the sigint stub,
//     cosmetic fix queued) called direct. NEW TODO stubs
//     raised here: EPZb (owner compiler-mumps.c::EP via the
//     compiler-mumps.c::<misc> roster unit 195, batch 7) and
//     fcninitZb (owner compiler-fcninit.c::fcninit, roster
//     unit 194, batch 7 - units.csv rank 230). epi called
//     direct (roster-188 render).
//   h015 grammar-collision - reused: no backticks, no raw
//     comment dollars, ASCII only, trailing newline.
//   h017 naming - reused: zcommands_code -> zcommandsCode; EP/
//     fcninit stubs carry the Zb qualifier (no vendor-name
//     collision exists yet, but both owners land in this same
//     batch; the qualifier keeps the release-at-render swap
//     mechanical).
//   h019 class_member_carrier - reused: all state rides the
//     CompilerGlobalsExt extern seats (xdG/xpxG/p2px/t2G/ierr/
//     sdlim/pd1/MAIN_flg/FCN_flg/F_flg/ExitCode/lines/ExecName/
//     pid/output_file/cvarx - the collision-renamed G-family
//     where pinned).
//   h020 pointer_handle - reused: the &xd[xpx] offsets seat as
//     xdStrAt(xpxG) string extractions to the NUL.
//
// Quirks kept (ledgered):
//   - xd[p2px] += 32 lowercase fold kept exact (<= 90 guard,
//     +32); the vendor relies on the char being a letter.
//   - Sysout(x) is the compiler-sys1.c:43 MACRO fprintf(Out,x):
//     x IS a format string with zero varargs. The %% pairs
//     process to single %; the stray %d inside the argv-set-up
//     banner has NO corresponding argument (UB in the vendor;
//     gcc reads a garbage int into the emitted text). QUANTIZED
//     (ADJ FLAG, journaled): %d is kept literal - the emission
//     is sprintf(t,"%%%d", i); building the %0..%n variable
//     names the generated program sym-puts argv into (with the
//     $SymPut("%",argv[0]) hard seat). This is the only
//     reading under which the generated program is coherent.
//   - zseek (arm 's'): the long i takes atol(&pd1[sdlim]) and
//     is then dead (h013 dead_store, kept as an assignment with
//     the fold journaled); t2G = 1 side effect kept.
//   - zexit (arm 'e') falls through the arm chain end to the
//     shared return 0 (no NEXT_LINE) - exact.
//   - the 'f' FCN-flg catch arm drops the same errors.h
//     include as epi: ONE paste, TWO consumers (EPI_ERR_HEAD
//     widened internal at this render; duplicate-emission
//     fold, journaled).
//   - the '//=====' separator is 77 '=' (counted) behind the
//     leading //.

// compiler-macros.h:76 (NEXT_LINE; same header family as the
// WriteCode PARSE_ERROR/ABRTX seats)
internal const val ZC_NEXT_LINE = 8

// void EP(int flg, short ExecName, long pid) - RELEASED at the
// roster-195 render (CompilerMumpsMisc.kt); the EPZb TODO stub
// here was removed and the call sites below call EP direct.

// void fcninit(char *output_file, long pid) - RELEASED at the
// roster-194 render (Fcninit.kt); the fcninitZb TODO stub here
// was removed and the call sites below call fcninit direct.

// fprintf(Out, ...) fold returning the written count (the
// linepad sent argument shape; same fold family as epiOutZb)
private fun zcOutZb(s: String): Int {
    try {
        CompilerOutState.out.write(s)
    } catch (e: java.io.IOException) {
        // vendor ignores the fprintf return (failure is silent)
    }
    return s.length
}

// Sysout(x) macro fold - compiler-sys1.c:43 fprintf(Out,x); the
// %% pairs process to % (the %d UB quantization is journaled in
// the file header)
private fun zcSysoutZb(x: String) {
    zcOutZb(x.replace("%%", "%"))
}

// &xd[xpx] seat: the NUL-terminated string at xdG[from]
private fun xdStrAt(from: Int): String {
    val g = CompilerGlobalsExt
    val sb = StringBuilder()
    var i = from
    while (i < g.xdG.size && g.xdG[i] != '\u0000') {
        sb.append(g.xdG[i])
        i++
    }
    return sb.toString()
}

// &pd1[sdlim] seat: the NUL-terminated string at pd1[off]
private fun pd1StrAtZb(p: CharArray, off: Int): String {
    val sb = StringBuilder()
    var i = off
    while (i < p.size && p[i] != '\u0000') {
        sb.append(p[i])
        i++
    }
    return sb.toString()
}

// int zcommands_code() - compiler-zcommands.c
internal fun zcommandsCode(): Int {
    val g = CompilerGlobalsExt

    g.p2px++
    val idx = g.p2px.toInt()
    if (g.xdG[idx].code <= 90) {
        g.xdG[idx] = (g.xdG[idx].code + 32).toChar() // lower case
    }

    if (g.xdG[idx] == 'n') {
        zcOutZb("\tsvPtr->gpad=1; // pad btree numeric subscript to the left\n")
        return ZC_NEXT_LINE // goto next_line;
    }

    if (g.xdG[idx] == 'a') {
        zcOutZb("\tsvPtr->gpad=0; // nopad btree numeric subscript to the left\n")
        return ZC_NEXT_LINE // goto next_line;
    }

    if (g.xdG[idx] == 'm') { // ZMAIN subroutine
        if (g.MAIN_flg.toInt() != 0) {
            System.out.print(
                "*** in line " + g.lines + " Multiple Main programs\n",
            )
            g.ExitCode = 99.toShort()
            return ABRTX // goto abrtx;
        } else if (g.FCN_flg.toInt() != 0) {
            EP(1, g.ExecName, g.pid)
            g.F_flg = 0
            g.FCN_flg = 0.toShort()
        }

        zcSysoutZb("\nint main(int argc, char *argv[]) {\n\n")

        g.cvarx = 0
        fcninit(g.output_file, g.pid)

        zcOutZb("__label_lookup = _label_lookup;\n")
        zcOutZb("__text_function = _text_function;\n\n")

        zcSysoutZb("\u0024SymPut(\"%\",argv[0]); \n\n")

        zcSysoutZb("if (argc > 0) { \n")
        zcSysoutZb("\tfor (int i = 0; i < argc; i++) { \n")
        zcSysoutZb("\t\tchar t[STR_MAX]; \n")
        zcSysoutZb("\t\tsprintf(t,\"%%%d\", i); \n")
        zcSysoutZb("\t\t\u0024SymPut(t, argv[i]); \n")
        zcSysoutZb("\t\t} \n")
        zcSysoutZb("\t} \n")

        zcOutZb("\ntry { \n")

        g.MAIN_flg = 1.toShort()

        return ZC_NEXT_LINE // goto next_line;
    }

    if (g.xdG[idx] == 'f') { // function subroutine
        if (g.MAIN_flg.toInt() != 0) {
            epi(g.ExecName, g.pid)
            g.MAIN_flg = 0.toShort()
        } else if (g.FCN_flg.toInt() != 0) {
            zcOutZb("\n\n")
            linepad(
                zcOutZb("\treturn EXIT_SUCCESS; }"),
                "Terminate Function",
            )

            zcOutZb("\n} // end of try block\n")
            zcOutZb("\tcatch(...) { \n")

            // #include <mumpsc/errors.h> - the EPI_ERR_HEAD paste
            // (one paste, two consumers; CompilerEpi seat)
            zcOutZb(EPI_ERR_HEAD)

            zcOutZb(
                "\t\tcout << \"Uncaught error in or near line \" " +
                    "<< svPtr->LineNumber << endl;\n",
            )

            linepad(
                zcOutZb("\tMglobal(CLOSE,tmp0,tmp1,svPtr);"),
                "Close Globals",
            )
            zcOutZb("\t\t}\n")

            g.FCN_flg = 0.toShort()
            g.F_flg = 0
            zcSysoutZb("//" + "=".repeat(77) + "\n\n")
        }

        zcOutZb("char * " + xdStrAt(g.xpxG.toInt()) + "() {\n\n")
        zcSysoutZb("//*** This module generated by Mumps Compiler ")
        zcSysoutZb("\n")
        zcSysoutZb("/*** This code is unverified - use at your own risk */\n\n")

        fcninit(g.output_file, g.pid)

        g.FCN_flg = 1.toShort()
        return ZC_NEXT_LINE // goto next_line;
    }

    if (g.xdG[idx] == 'd') { // write remainder of line to output
        zcOutZb("\t" + xdStrAt(g.xpxG.toInt()) + "\n")
        return ZC_NEXT_LINE // goto next_line;
    }

    if (g.xdG[idx] == 'r') { // zreturn
        zcOutZb("\treturn " + xdStrAt(g.xpxG.toInt()) + ";\n")
        return ZC_NEXT_LINE // goto next_line;
    }

    if (g.xdG[idx] == 's') { // zseek
        // long i; (dead store after the atol - h013, journaled)
        g.t2G = 1
        g.ierr = parse_().toShort()
        if (g.ierr.toInt() != 0) return PARSE_ERROR // goto parse_err;
        val i = atolC(pd1StrAtZb(g.pd1, g.sdlim.toInt())) // dead
        return 0 // goto next_cmnd;
    }

    if (g.xdG[idx] == 'c') { // zclose
        linepad(
            zcOutZb("\ttmp0[0]=0; tmp1[0]=0;"),
            "Function Epilogue",
        )
        linepad(
            zcOutZb("\tMglobal(CLOSE,tmp0,tmp1,svPtr);"),
            "Close Globals",
        )
        return 0 // goto next_cmnd;
    }

    if (g.xdG[idx] == 'e') { // zexit
        if (g.MAIN_flg.toInt() != 0) {
            epi(g.ExecName, g.pid)
            g.MAIN_flg = 0.toShort()
        } else {
            if (g.FCN_flg.toInt() != 0) {
                EP(1, g.ExecName, g.pid)
                g.F_flg = 0
                g.FCN_flg = 0.toShort()
            } else {
                zcOutZb(
                    "\n\n/********************** Function Epilogue ********************/\n",
                )
                g.F_flg = 0
                zcOutZb("\t_epilogue: return EXIT_SUCCESS\"\";\n}\n")
                g.MAIN_flg = 0.toShort()
            }
        }
    }
    return 0 // goto next_cmnd;
}
