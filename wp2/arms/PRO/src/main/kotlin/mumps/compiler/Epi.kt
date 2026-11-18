package mumps.compiler

import java.io.Writer
import mumps.interp.cstringAt
import mumps.interp.writeCstring

/**
 * PRO-arm translation of compiler-epi.c::epi (B7 tranche 2, unit
 * 189, rank 279). Translated independently from the pristine vendor
 * source (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe),
 * reference source lines 39-202 (definition at line 39).
 *
 * Identity verified against the study tool: compiler-epi.c::epi, c,
 * loc 135, comment_ratio 0.015, pointer_marks 25, control_marks 29,
 * score 111.3 - exact match to wp1/study-subset/units.csv row 189
 * (batch 7). RESOLVES the Stubs.kt epi h014 stub (same signature,
 * call sites bind unchanged).
 *
 * epi(short ExecName, long pid): emits the generated-C function
 * epilogue onto Out - the try-block close, the catch ladder (the
 * literal #include <mumpsc/errors.h> pulls the nine
 * GlobalExceptionCode fprintf arms, INLINED here; errors.h::<misc>
 * is roster row 296, batch 10 - it lands as a verification unit
 * over these arms, u150 precedent), the FCN_flg terminate split,
 * the label/text lookup emitters over the LabelsStart/TextStart
 * lists, and the per-function header file (Main_.pid /
 * FunctionName.pid) with the for/arg/tmp/cvar declarations.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h001 io_stream_param   : FILE* Out -> the CompilerState.out
 *    Writer (established convention); FILE* hfile -> a local
 *    java.io.FileWriter. The fopen==NULL abort() arm is
 *    unreachable in the Writer carrier (the ctor throws; the
 *    vendor's exit path is superseded - journaled).
 *  - h002 output_macro     : fprintf(Out, ...) -> out.write(...)
 *    with the emitted C text verbatim; the linepad(n, comment)
 *    call shape keeps the vendor's fprintf return value: the
 *    private fw2 helper writes and returns the length (the
 *    compiler-read.c linepad stub keeps its h014 seat).
 *  - h008 static_scratch   : none (no statics; fn/nme are stack
 *    buffers - fresh Strings here).
 *  - h009 format_string    : the %s/%d renderings are plain
 *    concatenations (no sprintf format carries a locale).
 *  - h011 goto             : none (no gotos).
 *  - h012 unsigned_byte_domain : FunctionName (char[256]) and
 *    cvars[i].name (char[32]) are byte-domain CharArrays read via
 *    cstringAt (u131 convention).
 *  - h013 dead_code        : the label block's int t=0 is dead
 *    (only l is used) and the text block's l is dead (only t is
 *    used) - kept as comment-only notes; the local int i in the
 *    hfile block re-declares the outer i (C scoping artifact,
 *    noted).
 *  - h014 out_of_scope_call: linepad (Stubs.kt, owner
 *    compiler-read.c, not a corpus unit) reused unmodified.
 *  - h015 grammar_collision: emitted-C identifiers __label_lookup/
 *    __text_function/__Arg are VENDOR names inside emitted string
 *    literals (documented scan-gate exceptions, the __finish
 *    family); the p1/p3 walkers use nullable locals with the
 *    error() null-carrier idiom (u137 precedent - NO double
 *    bang); new top-level epi re-binds the stub seat.
 *  - h017 naming           : epi keeps the vendor name.
 *  - h018 reference_build_visibility: the #include
 *    <mumpsc/errors.h> is LIVE in the reference build - the nine
 *    arms emit in the catch ladder exactly as compiled there.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The label table emits the first entry OUTSIDE the walk
 *    (before the while), so a one-label function emits with no
 *    leading comma - kept.
 *  - The text table's closer is double-tab ("\t\t};") while the
 *    label table's is single-tab ("\t};") - kept verbatim.
 *  - The emitted "Interprerer Error" text is the vendor's typo
 *    (cf. u158) - kept in the emitted C.
 *  - free() over the label/text lists has no JVM analog - the
 *    references are dropped and the Start pointers nulled (GC).
 *  - The final resets: IF_flg_max=forxmax=0 ride the hfile block;
 *    the tail re-issues IF_flg=forxmax=forx=0, zeroes ForBlocks
 *    to -1 (all FBMAX entries) and sets MaxTmp=2 - all kept.
 */

private const val EPI_SEPARATOR = "//=================================================================================" // vendor line 67 verbatim (81 '=')

/** h002: write + return the emitted length (linepad's fprintf arg). */
private fun fw2(out: Writer, s: String): Int {
    out.write(s)
    return s.length
}

/**
 * vendor 39: void epi(short ExecName, long pid). Emits the
 * generated-C epilogue; signature per the resolved Stubs.kt seat.
 */
fun epi(ExecName: Short, pid: Long) {
    val st = CompilerState
    val out = st.out

    out.write("\n")
    out.write(EPI_SEPARATOR + "\n")
    linepad(fw2(out, "_epilogue: tmp0[0]=0; tmp1[0]=0;"), " Function Epilogue")
    out.write("\tMltoa(getpid(),atmp);\n")
    out.write("\tif (svPtr->LockDone) cleanLocks((char *) atmp);\n")
    linepad(fw2(out, "\tMglobal(CLOSE,tmp0,tmp1,svPtr);"), "Close Globals")

    out.write("\n} // end of try block \n\n")
    out.write("catch(...) { \n")

    // vendor 59: #include <mumpsc/errors.h> - the nine arms, in
    // order, verbatim (h018; row 296 verification target).
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

    out.write("\tcout << \"Uncaught error in or near line \" \n" +
        "\t<< svPtr->LineNumber << endl;\n")
    linepad(fw2(out, "\tMglobal(CLOSE,tmp0,tmp1,svPtr);"), "Close Globals")
    out.write("\t} \n\n")

    if (st.FCN_flg != 0.toShort()) {
        out.write("return \"\";} /* Terminate */\n")
    } else {
        linepad(fw2(out, "return EXIT_SUCCESS; }"), "Terminate")
    }

    out.write(EPI_SEPARATOR + "\n")

    // --- vendor 75-104: the label lookup emitter ---

    if (st.LabelsStart != null) {
        // vendor: int t=0,l=0; - t is DEAD in this block (h013 note)
        var l = 0
        val fn = cstringAt(st.FunctionName)

        out.write("\n\nint " + fn + "_label_lookup(char *label) {\n\n")
        out.write("static struct lab {\n\tconst char *text;\n" +
            "\tint offset;\n\t}\n\tL[] = {\n")

        var cur: Txt? = st.LabelsStart
        out.write("\t\t\"" + (cur?.name ?: error("LabelsStart null carrier")) +
            "\", " + (cur?.lnbr ?: 0))
        cur = cur?.next
        l++
        while (cur != null) {
            l++
            out.write(",\n\t\t\"" + cur.name + "\", " + cur.lnbr)
            cur = cur.next
        }
        out.write("\n\t};\n\n")

        out.write("int lx = -1, i, Lx=" + l + ";\n\n")
        out.write("\tfor (i=0; i<Lx; i++) {\n")
        out.write("\t\tif (strcmp((const char *) L[i].text,(const char *) label)==0) {\n")
        out.write("\t\t\tlx=L[i].offset;\n")
        out.write("\t\t\tbreak;\n")
        out.write("\t\t}\n")
        out.write("\t}\n")
        out.write("\treturn lx;\n")
        out.write(" }\n")

        // vendor: free() walk over the label list (p1/p3) - GC
        st.LabelsStart = null
    } else {
        out.write("\n\nint " + cstringAt(st.FunctionName) +
            "_label_lookup(char *label) { return -1; }\n")
    }

    // --- vendor 106-134: the text lookup emitter ---

    if (st.TextStart != null) {
        // vendor: int t=0,l=0; - l is DEAD in this block (h013 note)
        var t = 0
        val fn = cstringAt(st.FunctionName)

        out.write("\n\nconst char * " + fn + "_text_function(int disp) {\n\n")
        out.write("static struct txt {\n\tconst char *text;\n" +
            "\tint offset;\n\t}\n\tT[] = {\n")

        var cur: Txt? = st.TextStart
        out.write("\t\t\"" + (cur?.name ?: error("TextStart null carrier")) +
            "\", " + (cur?.lnbr ?: 0))
        cur = cur?.next
        t++
        while (cur != null) {
            t++
            out.write(",\n\t\t\"" + cur.name + "\", " + cur.lnbr)
            cur = cur.next
        }
        out.write("\n\t\t};\n\n")

        out.write("int tx,i,Tx=" + t + ";\n\n")
        out.write("\tfor (i=0; i<Tx; i++)\n")
        out.write("\t\tif (disp==T[i].offset) return T[i].text;\n")
        out.write("\treturn NULL;\n")
        out.write("}\n")

        // vendor: free() walk over the text list (p1/p3) - GC
        st.TextStart = null
    } else {
        out.write("const char * " + cstringAt(st.FunctionName) +
            "_text_function(int disp) { return NULL; }\n\n")
    }

    // --- vendor 136-180: the per-function header file ---

    run {
        val fname = cstringAt(st.FunctionName)
        val nme: String
        if (fname.isEmpty()) {
            nme = "Main_." + pid // vendor: sprintf(nme,"Main_.%d", pid)
        } else {
            nme = fname + "." + pid // vendor: sprintf(nme,"%s.%d", ...)
            // vendor: if (hfile==NULL) abort(); - unreachable in
            // the Writer carrier (the ctor throws; journaled)
        }
        val hfile = java.io.FileWriter(nme)

        for (i in 1..st.forxmax) {
            hfile.write("char for" + i + "_incr[FORPARM], for" + i +
                "_lim[FORPARM], for" + i + "_init[FORPARM];\n" +
                "int for" + i + "F; " +
                "int for" + i + "T; \n")
        }

        for (i in 0..st.forx) hfile.write("jmp_buf for" + i + "J;\n")
        for (i in 0 until st.ArgMax) {
            hfile.write("char *__Arg" + i + " = new char[STR_MAX];\n")
        }
        for (i in 0..ExecName) {
            hfile.write("jmp_buf Efor" + i + "J;\n")
            hfile.write("int tpx" + i + ";\n")
        }
        for (i in 0 until st.MaxTmp) {
            hfile.write("unsigned char *tmp" + i +
                " = new unsigned char[STR_MAX];\n")
        }
        for (i in 0 until st.cvarx) {
            hfile.write("char " + cstringAt(st.cvars[i].name) +
                "[STR_MAX]; // declared variable\n")
        }
        st.cvarx = 0

        st.IF_flg_max = 0
        st.forxmax = 0
        st.ArgMax = 0
        hfile.close()
    }

    // --- vendor 182-201: the tail resets ---

    writeCstring(st.FunctionName, "")
    st.IF_flg = 0
    st.forxmax = 0
    st.forx = 0
    for (i in 0 until CompilerState.FBMAX) st.ForBlocks[i] = -1
    st.MaxTmp = 2
    return
}
