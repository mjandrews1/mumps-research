package mumps.compiler

import java.io.Writer
import mumps.interp.cstringAt

/**
 * PRO-arm translation of compiler-zcommands.c::zcommands_code (B7
 * tranche 2, unit 192, rank 251). Translated independently from
 * the pristine vendor source (mumps-14-may-2026.tgz, sha256
 * 621deb25...ffdbe), reference source lines 39-172 (definition at
 * line 39).
 *
 * Identity verified against the study tool: compiler-zcommands.c::
 * zcommands_code, c, loc 109, comment_ratio 0.009,
 * pointer_marks 4, control_marks 31, score 64.5 - exact match to
 * wp1/study-subset/units.csv row 192 (batch 7).
 *
 * zcommands_code(): the z-command dispatcher - the second char of
 * the zcommand (lowercased in place) selects the arm: n/a set the
 * btree numeric-pad flag, m emits the C main-program prologue, f
 * the function-module prologue/epilogue, d the literal-line pass-
 * through, r zreturn, s zseek, c zclose, e zexit.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h002 simple_output_macro : Sysout(x) -> out.write(x) via the
 *    private Sysout helper; fprintf(Out, ...) verbatim; the
 *    linepad(fprintf(...), c) call shape keeps the fprintf return
 *    (private fw2, Epi.kt precedent).
 *  - h009 format_string      : the Sysout format strings are
 *    processed at COMPILE time (fprintf's %% pairs collapse
 *    before emission): Sysout("$SymPut(\"%%\",argv[0]);") emits
 *    $SymPut("%",argv[0]); and the sprintf(t,\"%%%%%%d\", i)
 *    emission emits sprintf(t,"%%d", i); - kept, journaled.
 *  - h012 unsigned_byte_domain: xd/pd1 ride the CompilerState
 *    byte-domain CharArrays; &xd[xpx]/&pd1[sdlim] fold to
 *    cstringAt/atoiC offsets.
 *  - h013 dead_store         : the zseek arm's long i =
 *    atol(&pd1[sdlim]) is assigned and never read (the vendor
 *    parses the seek offset and discards it) - comment-only note;
 *    atol has no side effect, the call is not rendered.
 *  - h014 out_of_scope_call  : epi (Epi.kt, B7 unit 189, real
 *    seat); EP (Stubs.kt, owner compiler-mumps.c, batch-7 unit
 *    196); fcninit (Stubs.kt, owner compiler-fcninit.c::fcninit,
 *    batch-7 unit 195 - raised by this unit); parse_ (Stubs.kt,
 *    owner compiler-parse.c, batch-8); linepad (Stubs.kt, owner
 *    compiler-read.c, not a corpus unit).
 *  - h015 grammar_collision  : the emitted-C identifiers
 *    __label_lookup/__text_function are vendor names inside
 *    emitted string literals (documented scan-gate exceptions,
 *    the __finish family, cf. Epi.kt); the $SymPut emission is
 *    built from DOLLAR = 36.toChar() (no raw dollar in the
 *    literal); new top-level zcommandsCode - the hyphenated C
 *    name maps per house convention.
 *  - h017 naming             : zcommands_code -> zcommandsCode
 *    (house C-identifier convention).
 *  - h018 reference_build_visibility: the #include
 *    <mumpsc/errors.h> in the f arm is LIVE in the reference
 *    build - the nine arms emit inside the function epilogue
 *    exactly as compiled there (Epi.kt inline precedent).
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The lowercase fold tests xd[p2px] <= 90 (not 'A'..'Z'), so
 *    any char at or below 'Z' - including digits, NUL (0 -> 32,
 *    space) - shifts by +32; kept verbatim over the byte domain.
 *  - The m arm resets cvarx=0 BEFORE fcninit (the declared-
 *    variable table is per-function; epi re-zeros it at the close
 *    too - kept).
 *  - The e arm's else/else prints the vendor's mixed epilogue
 *    spellings (return EXIT_SUCCESS"") - kept verbatim in the
 *    emitted C.
 */

private const val ABRTX = 6 // compiler-macros.h:74
private const val NEXT_LINE = 8 // compiler-macros.h:76
private const val PARSE_ERROR = 1 // compiler-macros.h:69
private const val DOLLAR = 36.toChar() // vendor '$' (h015 carrier)

/** h002: vendor macro Sysout(x) is fprintf(Out, x). */
private fun Sysout(out: Writer, s: String) {
    out.write(s)
}

/** h002: write + return the emitted length (linepad's fprintf arg). */
private fun fw2(out: Writer, s: String): Int {
    out.write(s)
    return s.length
}

/**
 * vendor 39: int zcommands_code(). Returns NEXT_LINE (8) to re-enter
 * the line loop, 0 for next command, ABRTX/PARSE_ERROR on the
 * error arms.
 */
fun zcommandsCode(): Int {
    val st = CompilerState
    val out = st.out

    st.p2px++
    // vendor: if (xd[p2px] <= 90) xd[p2px] += 32; // lower case
    if (st.xd[st.p2px.toInt()] <= 'Z') {
        st.xd[st.p2px.toInt()] = ((st.xd[st.p2px.toInt()].code + 32) and 0xff).toChar()
    }

    if (st.xd[st.p2px.toInt()] == 'n') {
        out.write("\tsvPtr->gpad=1; // pad btree numeric subscript to the left\n")
        return NEXT_LINE // goto next_line;
    }

    if (st.xd[st.p2px.toInt()] == 'a') {
        out.write("\tsvPtr->gpad=0; // nopad btree numeric subscript to the left\n")
        return NEXT_LINE // goto next_line;
    }

    if (st.xd[st.p2px.toInt()] == 'm') { // ZMAIN subroutine

        if (st.MAIN_flg != 0.toShort()) {
            print("*** in line " + st.lines + " Multiple Main programs\n")
            st.ExitCode = 99
            return ABRTX // goto abrtx;
        } else if (st.FCN_flg != 0.toShort()) {
            EP(1, st.ExecName, st.pid)
            st.F_flg = 0
            st.FCN_flg = 0
        }

        Sysout(out, "\nint main(int argc, char *argv[]) {\n\n")

        st.cvarx = 0
        fcninit(st.output_file, st.pid)

        out.write("__label_lookup = _label_lookup;\n")
        out.write("__text_function = _text_function;\n\n")

        // h009: fprintf's %% collapses at compile time
        Sysout(out, DOLLAR.toString() + "SymPut(\"" + '%' + "\",argv[0]); \n\n")

        Sysout(out, "if (argc > 0) { \n")
        Sysout(out, "\tfor (int i = 0; i < argc; i++) { \n")
        Sysout(out, "\t\tchar t[STR_MAX]; \n")
        Sysout(out, "\t\tsprintf(t,\"" + "%%" + "d\", i); \n")
        Sysout(out, "\t\t" + DOLLAR.toString() + "SymPut(t, argv[i]); \n")
        Sysout(out, "\t\t} \n")
        Sysout(out, "\t} \n")

        out.write("\ntry { \n")

        st.MAIN_flg = 1

        return NEXT_LINE // goto next_line;
    }

    if (st.xd[st.p2px.toInt()] == 'f') { // function subroutine

        if (st.MAIN_flg != 0.toShort()) {
            epi(st.ExecName, st.pid)
            st.MAIN_flg = 0
        } else if (st.FCN_flg != 0.toShort()) {
            out.write("\n\n")
            linepad(fw2(out, "\treturn EXIT_SUCCESS; }"), "Terminate Function")

            out.write("\n} // end of try block\n")
            out.write("\tcatch(...) { \n")

            // vendor: #include <mumpsc/errors.h> - the nine arms
            // (h018; Epi.kt inline precedent)
            out.write("     if (GlobalExceptionCode == INPUT_ERROR) " +
                "cout << \"Input Error\" << endl;\n")
            out.write("     else if (GlobalExceptionCode == INPUT_LENGTH) " +
                "cout << \"Input Length Error\" << endl;\n")
            out.write("     else if (GlobalExceptionCode == SYMTAB_ERROR) " +
                "cout << \"Symbol Table Error\" << endl;\n")
            out.write("     else if (GlobalExceptionCode == GLOBAL_NOT_FOUND) " +
                "cout << \"Global Not Found Error\" << endl;\n")
            out.write("     else if (GlobalExceptionCode == INTERPRETER_ERROR) " +
                "cout << \"Interprerer Error\" << endl;\n")
            out.write("     else if (GlobalExceptionCode == INVALID_CONVERSION) " +
                "cout << \"Invalid Conversion Error\" << endl;\n")
            out.write("     else if (GlobalExceptionCode == DATA_RANGE_ERROR) " +
                "cout << \"Data Range Error\" << endl;\n")
            out.write("     else if (GlobalExceptionCode == NUMERIC_RANGE) " +
                "cout << \"Numeric Range Error\" << endl;\n")
            out.write("     else if (GlobalExceptionCode == UNSPECIFIED) " +
                "cout << \"Unspecified Error\" << endl;\n")

            out.write("\t\tcout << \"Uncaught error in or near line \" " +
                "<< svPtr->LineNumber << endl;\n")

            linepad(fw2(out, "\tMglobal(CLOSE,tmp0,tmp1,svPtr);"), "Close Globals")
            out.write("\t\t}\n")

            st.FCN_flg = 0
            st.F_flg = 0
            Sysout(out,
                "//=============================================================================\n\n")
        }

        out.write("char * " + cstringAt(st.xd, st.xpx.toInt()) + "() {\n\n")
        Sysout(out, "//*** This module generated by Mumps Compiler ")
        Sysout(out, "\n")
        Sysout(out, "/*** This code is unverified - use at your own risk */\n\n")

        fcninit(st.output_file, st.pid)

        st.FCN_flg = 1
        return NEXT_LINE // goto next_line;
    }

    if (st.xd[st.p2px.toInt()] == 'd') { // write remainder of line to output
        out.write("\t" + cstringAt(st.xd, st.xpx.toInt()) + "\n")
        return NEXT_LINE // goto next_line;
    }

    if (st.xd[st.p2px.toInt()] == 'r') { // zreturn
        out.write("\treturn " + cstringAt(st.xd, st.xpx.toInt()) + ";\n")
        return NEXT_LINE // goto next_line;
    }

    if (st.xd[st.p2px.toInt()] == 's') { // zseek
        st.t2 = 1
        st.ierr = parse_().toShort()
        if (st.ierr != 0.toShort()) return PARSE_ERROR // goto parse_err;
        // vendor: long i = atol(&pd1[sdlim]); - dead store (h013):
        // the seek offset is parsed and discarded; kept as note.
        return 0 // goto next_cmnd;
    }

    if (st.xd[st.p2px.toInt()] == 'c') { // zclose
        linepad(fw2(out, "\ttmp0[0]=0; tmp1[0]=0;"), "Function Epilogue")
        linepad(fw2(out, "\tMglobal(CLOSE,tmp0,tmp1,svPtr);"), "Close Globals")
        return 0 // goto next_cmnd;
    }

    if (st.xd[st.p2px.toInt()] == 'e') { // zexit

        if (st.MAIN_flg != 0.toShort()) {
            epi(st.ExecName, st.pid)
            st.MAIN_flg = 0
        } else {
            if (st.FCN_flg != 0.toShort()) {
                EP(1, st.ExecName, st.pid)
                st.F_flg = 0
                st.FCN_flg = 0
            } else {
                out.write("\n\n/********************** Function Epilogue ********************/\n")
                st.F_flg = 0
                out.write("\t_epilogue: return EXIT_SUCCESS\"\";\n}\n")
                st.MAIN_flg = 0
            }
        }
    }
    return 0 // goto next_cmnd;
}
