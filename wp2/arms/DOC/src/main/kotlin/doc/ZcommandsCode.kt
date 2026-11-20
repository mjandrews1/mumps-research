package doc

// Unit 210 (B7 tranche 3): compiler-zcommands.c::zcommands_code
// (batch 7, complex stratum; units.csv row 210: loc 109, score
// 64.5). The Z-command post-compiler: advances p2px into the
// compiler xd carrier, lower-cases the command letter (the
// vendor <=90 filter kept verbatim - it folds upper-case but
// never touches the digits row), and switches on the Z-command
// arms: n/a set the btree numeric pad emission and NEXT_LINE;
// m is the ZMAIN prologue (the multiple-main probe printing the
// *** in line %d Multiple Main programs warning and ABRTX exit
// when MAIN_flg is already set, the FCN_flg pre-close through
// the EP(1,...) router, the int-main emission, cvarx reset,
// fcninit prologue (unit 207, Fcninit.kt), the __label_lookup
// bindings, the argv SymPut emissions whose %% pairs ride the
// post-conversion render - the Sysout macro is fprintf(Out,x),
// so the vendor %% emits % and %%%%%%d emits %%%d in the
// generated C; the strings here are written post-conversion
// (H-002 inline-macro precedent), and the raw dollars of
// SymPut ride backslash-dollar escapes (H-015 the unit-31
// escape rule)), the try { opener, MAIN_flg=1; f is the
// ZFUNCTION prologue (the MAIN pre-close through epi (unit 213,
// direct call), the FCN pre-close emission block with the
// Terminate Function linepad, the try/catch frame, the errors.h
// include ledger (EpiMisc unit-63 precedent: a compiler-TU
// include, not emitted text), the Close Globals linepad, the
// rule banner, the char * %s() header over the xd-from-xpx
// function name, the unverified-code banner, fcninit, FCN_flg=1);
// d/r emit the remainder / return of xd-from-xpx; s is the
// zseek parse re-entry (t2=1, the Stubs.parse_() compiler-twin
// seat, PARSE_ERROR on non-zero ierr; the atol(&pd1[sdlim])
// local is dead in the vendor (H-013 dead local: assigned,
// never read - kept as the assignment-side ledger)); c is the
// zclose epilogue-emission pair; e is zexit (MAIN close via
// epi, FCN close via EP, the bare Function Epilogue banner and
// the _epilogue return emission, F_flg/MAIN_flg resets). The
// trailing return 0 is the vendor's next_cmnd fall-through.
// H-010: p2px/xpx Int reads (the xpx Long seat narrows at the
// xd index); lines is the printf count; ExitCode=99 kept.
// linepad rides the Stubs.linepad seat over CompilerState.emit
// (the freadx unit-115 emit precedent). The printf warning maps
// to System.out (H-001). Naming per H-017 (zcommands_code
// verbatim).

private const val PARSE_ERROR_ZC = 1 // compiler-macros.h:69
private const val ABRTX_ZC = 6       // compiler-macros.h:74
private const val NEXT_LINE_ZC = 8   // compiler-macros.h:76

fun zcommands_code(): Int {

    // compiler-macros.h:50 Sysout(x) - fprintf(Out,x) local
    // helper (H-002; strings written post-conversion).
    fun Sysout(x: String) {
        CompilerState.Out.write(x)
    }

    val xd = CompilerState.xd
    var px = CompilerState.p2px
    px++
    if (xd[px] <= 'Z') {
        //lower case
        xd[px] = (xd[px].code + 32).toChar()
    }
    CompilerState.p2px = px

    if (xd[px] == 'n') {
        CompilerState.Out.write("\tsvPtr->gpad=1; // pad btree numeric subscript to the left\n")
        return NEXT_LINE_ZC // goto next_line;
    }

    if (xd[px] == 'a') {
        CompilerState.Out.write("\tsvPtr->gpad=0; // nopad btree numeric subscript to the left\n")
        return NEXT_LINE_ZC // goto next_line;
    }

    if (xd[px] == 'm') { // ZMAIN subroutine

        if (CompilerState.MAIN_flg != 0) {
            System.out.print(String.format(java.util.Locale.US,
                "*** in line %d Multiple Main programs\n", CompilerState.lines))
            CompilerState.ExitCode = 99
            return ABRTX_ZC // goto abrtx;
        } else if (CompilerState.FCN_flg != 0) {
            Stubs.EP(1, CompilerState.ExecName, CompilerState.pid)
            CompilerState.F_flg = 0
            CompilerState.FCN_flg = 0
        }

        Sysout("\nint main(int argc, char *argv[]) {\n\n")

        CompilerState.cvarx = 0
        fcninit(CompilerState.output_file, CompilerState.pid)

        CompilerState.Out.write("__label_lookup = _label_lookup;\n")
        CompilerState.Out.write("__text_function = _text_function;\n\n")

        // Sysout SymPut percent-percent pair - %% emits % under
        // the fprintf(Out,x) macro contract; post-conversion
        // string, dollar escaped per H-015.
        Sysout("\$SymPut(\"%\",argv[0]); \n\n")

        Sysout("if (argc > 0) { \n")
        Sysout("\tfor (int i = 0; i < argc; i++) { \n")
        Sysout("\t\tchar t[STR_MAX]; \n")
        // "%%%%%%d" emits "%%%d" (three surviving percents).
        Sysout("\t\tsprintf(t,\"%%%d\", i); \n")
        Sysout("\t\t\$SymPut(t, argv[i]); \n")
        Sysout("\t\t} \n")
        Sysout("\t} \n")

        CompilerState.Out.write("\ntry { \n")

        CompilerState.MAIN_flg = 1

        return NEXT_LINE_ZC // goto next_line;
    }

    if (xd[px] == 'f') { // function subroutine

        if (CompilerState.MAIN_flg != 0) {
            epi(CompilerState.ExecName, CompilerState.pid)
            CompilerState.MAIN_flg = 0
        } else if (CompilerState.FCN_flg != 0) {
            CompilerState.Out.write("\n\n")
            Stubs.linepad(CompilerState.emit(
                "\treturn EXIT_SUCCESS; }"), "Terminate Function")

            CompilerState.Out.write("\n} // end of try block\n")
            CompilerState.Out.write("\tcatch(...) { \n")

            // #include <mumpsc/errors.h> - a compiler-TU include
            // (not emitted text); include ledger, EpiMisc
            // unit-63 precedent.

            CompilerState.Out.write("\t\tcout << \"Uncaught error in or near line \" " +
                "<< svPtr->LineNumber << endl;\n")

            Stubs.linepad(CompilerState.emit(
                "\tMglobal(CLOSE,tmp0,tmp1,svPtr);"), "Close Globals")
            CompilerState.Out.write("\t\t}\n")

            CompilerState.FCN_flg = 0
            CompilerState.F_flg = 0
            Sysout("//" + "=".repeat(77) + "\n\n")
        }

        CompilerState.Out.write(String.format(java.util.Locale.US,
            "char * %s() {\n\n",
            CstringOps.cstringAt(xd, CompilerState.xpx.toInt())))
        Sysout("//*** This module generated by Mumps Compiler ")
        Sysout("\n")
        Sysout("/*** This code is unverified - use at your own risk */\n\n")

        fcninit(CompilerState.output_file, CompilerState.pid)

        CompilerState.FCN_flg = 1
        return NEXT_LINE_ZC // goto next_line;
    }

    if (xd[px] == 'd') { // write remainder of line to output
        CompilerState.Out.write(String.format(java.util.Locale.US,
            "\t%s\n", CstringOps.cstringAt(xd, CompilerState.xpx.toInt())))
        return NEXT_LINE_ZC // goto next_line;
    }

    if (xd[px] == 'r') { // zreturn
        CompilerState.Out.write(String.format(java.util.Locale.US,
            "\treturn %s;\n", CstringOps.cstringAt(xd, CompilerState.xpx.toInt())))
        return NEXT_LINE_ZC // goto next_line;
    }

    if (xd[px] == 's') { // zseek
        CompilerState.t2 = 1
        CompilerState.ierr = Stubs.parse_()
        if (CompilerState.ierr != 0) return PARSE_ERROR_ZC // goto parse_err;
        // i = atol(&pd1[sdlim]) - H-013 dead local (assigned,
        // never read); the assignment side is the atoi seat.
        val i = atoi(CstringOps.cstringAt(CompilerState.pd1, CompilerState.sdlim))
        return 0 // goto next_cmnd;
    }

    if (xd[px] == 'c') { // zclose
        Stubs.linepad(CompilerState.emit(
            "\ttmp0[0]=0; tmp1[0]=0;"), "Function Epilogue")
        Stubs.linepad(CompilerState.emit(
            "\tMglobal(CLOSE,tmp0,tmp1,svPtr);"), "Close Globals")
        return 0 // goto next_cmnd;
    }

    if (xd[px] == 'e') { // zexit

        if (CompilerState.MAIN_flg != 0) {
            epi(CompilerState.ExecName, CompilerState.pid)
            CompilerState.MAIN_flg = 0
        } else {
            if (CompilerState.FCN_flg != 0) {
                Stubs.EP(1, CompilerState.ExecName, CompilerState.pid)
                CompilerState.F_flg = 0
                CompilerState.FCN_flg = 0
            } else {
                CompilerState.Out.write(
                    "\n\n/********************** Function Epilogue ********************/\n")
                CompilerState.F_flg = 0
                CompilerState.Out.write("\t_epilogue: return EXIT_SUCCESS\"\";\n}\n")
                CompilerState.MAIN_flg = 0
            }
        }
    }
    return 0 // goto next_cmnd
}
