package daf

// Unit 210 (DAF B7 T3, complex): compiler-zcommands.c::zcommands_code
// (109 LOC, score 64.5): the Z-command directive router of the
// compiler - the lower-cased xd[p2px] dispatch over n/a/m/f/d/r/s/
// c/e with the MAIN/FCN function-boundary emit arms, the fcninit
// preamble calls, the catch-ladder emission (the errors.h block
// expanded: the emit-side text of the mumpsc/errors.h ladder,
// each arm emitting verbatim; the vendor misspelling
// "Interprerer Error" kept), and the zseek/zclose/zexit tails.
// Same-TU kin of the unit-187 residue (rendered B7 T1: the TU
// banner/include set; this row IS the zcommands_code body that
// residue ledgered).
// Pending carriers RESOLVED at this consuming row (the
// CompilerGlobalsExtMisc ledger): p2pxD (p2px), fcnFlgD (FCN_flg),
// fFlgD (F_flg), exitCodeD (ExitCode), cvarxD (cvarx), pidD (pid),
// outputFileD (output_file). MAIN_flg already raised (unit-206
// mainFlgD; the ExternsHMisc note rides). lines/ierr/t2/sdlim/
// xpx/xd/pd1 are the DafCompilerState fields; ExecName the state
// field. Return codes pinned from compiler-macros.h:69-76:
// PARSE_ERROR 1 (seat const), ABRTX 6 (seat const), NEXT_LINE 8
// (raised NEXT_LINE_D here).
// Seats called: epDaf (unit-206 raise), epiDaf (unit-206 raise;
// releases at the unit-213 render), fcninitDaf (unit-207 render),
// linepadDaf (ForCode seat), parseDaf (DafInterpStubsT3:109 - the
// zseek arm's bare parse_() folds onto the state svPtr drive of
// the GlobalQuery precedent; ierr/t2 on the compiler state),
// at() end-guards.
// Emitted-text ledger (rows 2/26: emitted C kept verbatim at the
// OUTPUT level; the vendor's %% printf escapes decode to the
// emitted %): the \u0024SymPut arms and the sprintf(t, \u0025
// escapes) - the vendor "%\u0025" lattices decode exactly; zseek's
// the long i = atol(&pd1[sdlim]) (unused) stays a KDoc note (the
// vendor computes a value it discards; the call is side-effect
// free, kept comment-plus-verbatim), row 46 dead-code discipline.
// Consults (2026-11-24T20:12:00Z, nominal): rows 6/22 hit-reuse
// (the pending-carrier raises; the state-field folds), rows 2/26
// hit-reuse (Sysout/fprintf(Out) emit verbatim at output level;
// the errors.h ladder text verbatim incl. the misspelling), row 27
// hit-reuse (the printf/line/%d stdout diagnostic), row 37
// hit-reuse (the linepadDaf named-owner seat), rows 14/15
// hit-reuse (the == 'n'/'a'/... char compares and the MAIN_flg/
// FCN_flg gates), row 33 hit-reuse (the string/%% decodes), row
// 46 hit-adapt (the unused-atol dead-code discipline;
// the return-code ladder), manual-decision (no BASE row: the
// bare parse_() sv drive fold; the NEXT_LINE_D raise).
// Dafny: EXEMPT (emit-side compiler plumbing; journal row).

const val NEXT_LINE_D = 8 // compiler-macros.h:76 NEXT_LINE

var p2pxD = 0        // int p2px (pending carrier, resolved here)
var fcnFlgD = 0     // FCN_flg
var fFlgD = 0        // F_flg
var exitCodeD = 0    // ExitCode
var cvarxD = 0       // cvarx (the cvars[CVARMAX] cursor)
var pidD: Long = 0   // pid
var outputFileD = "" // char *output_file (fcninit arg seat)

fun zcommandsCodeD(): Int {

    val C = DafCompilerState
    p2pxD++
    if (at(C.xd, p2pxD).code <= 90) // if (xd[p2px] <= 90)
        C.xd[p2pxD] = (C.xd[p2pxD].code + 32).toChar() // xd[p2px] += 32; lower case

    if (at(C.xd, p2pxD) == 'n') {
        C.emit("\tsvPtr->gpad=1; // pad btree numeric subscript to the left\n")
        return NEXT_LINE_D
    }

    if (at(C.xd, p2pxD) == 'a') {
        C.emit("\tsvPtr->gpad=0; // nopad btree numeric subscript to the left\n")
        return NEXT_LINE_D
    }

    if (at(C.xd, p2pxD) == 'm') { // ZMAIN subroutine
        if (mainFlgD != 0) { // if (MAIN_flg)
            println("*** in line " + C.lines + " Multiple Main programs")
            exitCodeD = 99
            return ABRTX_D // goto abrtx
        } else if (fcnFlgD != 0) { // else if (FCN_flg)
            epDaf(1, C.ExecName, pidD)
            fFlgD = 0
            fcnFlgD = 0
        }

        C.emit("\nint main(int argc, char *argv[]) {\n\n") // Sysout

        cvarxD = 0
        fcninitDaf(outputFileD, pidD)

        C.emit("__label_lookup = _label_lookup;\n")
        C.emit("__text_function = _text_function;\n\n")

        C.emit("\u0024SymPut(\"\u0025\",argv[0]); \n\n") // %% -> % (output level)

        C.emit("if (argc > 0) { \n")
        C.emit("\tfor (int i = 0; i < argc; i++) { \n")
        C.emit("\t\tchar t[STR_MAX]; \n")
        // "%%%%%%d" decodes to the emitted three-percent run
        C.emit("\t\tsprintf(t,\"%%\u0025d\", i); \n")
        C.emit("\t\t\u0024SymPut(t, argv[i]); \n")
        C.emit("\t\t} \n")
        C.emit("\t} \n")

        C.emit("\ntry { \n")

        mainFlgD = 1 // MAIN_flg = 1

        return NEXT_LINE_D
    }

    if (at(C.xd, p2pxD) == 'f') { // function subroutine
        if (mainFlgD != 0) { // if (MAIN_flg)
            epiDaf(C.ExecName, pidD) // epi (ExecName, pid)
            mainFlgD = 0
        } else if (fcnFlgD != 0) { // else if (FCN_flg)
            C.emit("\n\n")
            // linepad(fprintf(Out, "\treturn EXIT_SUCCESS; }"), "Terminate Function")
            C.emit("\treturn EXIT_SUCCESS; }")
            linepadDaf(C, "", "Terminate Function")

            C.emit("\n} // end of try block\n")
            C.emit("\tcatch(...) { \n")

            // #include <mumpsc/errors.h> - the emit-side ladder,
            // text verbatim (the misspelling is the vendor's)
            C.emit("     if (GlobalExceptionCode == INPUT_ERROR) " +
                    "cout << \"Input Error\" << endl;\n")
            C.emit("     else if (GlobalExceptionCode == INPUT_LENGTH) " +
                    "cout << \"Input Length Error\" << endl;\n")
            C.emit("     else if (GlobalExceptionCode == SYMTAB_ERROR) " +
                    "cout << \"Symbol Table Error\" << endl;\n")
            C.emit("     else if (GlobalExceptionCode == GLOBAL_NOT_FOUND) " +
                    "cout << \"Global Not Found Error\" << endl;\n")
            C.emit("     else if (GlobalExceptionCode == INTERPRETER_ERROR) " +
                    "cout << \"Interprerer Error\" << endl;\n")
            C.emit("     else if (GlobalExceptionCode == INVALID_CONVERSION) " +
                    "cout << \"Invalid Conversion Error\" << endl;\n")
            C.emit("     else if (GlobalExceptionCode == DATA_RANGE_ERROR) " +
                    "cout << \"Data Range Error\" << endl;\n")
            C.emit("     else if (GlobalExceptionCode == NUMERIC_RANGE) " +
                    "cout << \"Numeric Range Error\" << endl;\n")
            C.emit("     else if (GlobalExceptionCode == UNSPECIFIED) " +
                    "cout << \"Unspecified Error\" << endl;\n")

            C.emit("\t\tcout << \"Uncaught error in or near line \" " +
                    "<< svPtr->LineNumber << endl;\n")

            C.emit("\tMglobal(CLOSE,tmp0,tmp1,svPtr);") // linepad body text
            linepadDaf(C, "", "Close Globals")
            C.emit("\t\t}\n")

            fcnFlgD = 0
            fFlgD = 0
            C.emit("//=============================================================================\n\n")
        }

        C.emit("char * " + DafCRuntime.cstringAt(C.xd, C.xpx) + "() {\n\n")
        C.emit("//*** This module generated by Mumps Compiler ")
        C.emit("\n")
        C.emit("/*** This code is unverified - use at your own risk */\n\n")

        fcninitDaf(outputFileD, pidD)

        fcnFlgD = 1
        return NEXT_LINE_D
    }

    if (at(C.xd, p2pxD) == 'd') { // write remainder of line to output
        C.emit("\t" + DafCRuntime.cstringAt(C.xd, C.xpx) + "\n")
        return NEXT_LINE_D
    }

    if (at(C.xd, p2pxD) == 'r') { // zreturn
        C.emit("\treturn " + DafCRuntime.cstringAt(C.xd, C.xpx) + ";\n")
        return NEXT_LINE_D
    }

    if (at(C.xd, p2pxD) == 's') { // zseek
        C.t2 = 1
        val r = parseCDaf(C, DafInterpState.svPtr)
            // zseek rebind (PI-ruled, C-011): the b2 svPtr-drive
            // fold routes through the b7 parseCDaf gate seat
            // (compiler-parse.c::parse_ owns the kernel)
        C.ierr = r
        if (C.ierr != 0) return PARSE_ERROR_D
        // long i = atol(&pd1[sdlim]); - the vendor computes a value
        // it then discards (side-effect free); kept note-only
        // (row 46 dead-code discipline), the sdlim offset retained
        val sdlimOff = C.sdlim
        return 0 // goto next_cmnd
    }

    if (at(C.xd, p2pxD) == 'c') { // zclose
        C.emit("\ttmp0[0]=0; tmp1[0]=0;") // linepad body text
        linepadDaf(C, "", "Function Epilogue")
        C.emit("\tMglobal(CLOSE,tmp0,tmp1,svPtr);")
        linepadDaf(C, "", "Close Globals")
        return 0 // goto next_cmnd
    }

    if (at(C.xd, p2pxD) == 'e') { // zexit
        if (mainFlgD != 0) { // if (MAIN_flg)
            epiDaf(C.ExecName, pidD)
            mainFlgD = 0
        } else {
            if (fcnFlgD != 0) { // if (FCN_flg)
                epDaf(1, C.ExecName, pidD)
                fFlgD = 0
                fcnFlgD = 0
            } else {
                C.emit("\n\n/********************** Function Epilogue ********************/\n")
                fFlgD = 0
                C.emit("\t_epilogue: return EXIT_SUCCESS\"\";\n}\n")
                mainFlgD = 0
            }
        }
    }
    return 0 // goto next_cmnd
}
