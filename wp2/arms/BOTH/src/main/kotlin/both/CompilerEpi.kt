package both

// Unit 188 render: compiler-epi.c::epi (roster 188, batch 7,
// 135 LOC). The C-side Function Epilogue code generator of the
// MUMPS Compiler: the try-block closing emission with the lock
// cleanup and global-close calls, the catch(...) block with the
// mumpsc/errors.h paste (the ten GlobalExceptionCode cout lines,
// emitted verbatim per the vendor #include), the FCN return arm,
// the %s_label_lookup / %s_text_function emitters over the
// LabelsStart/TextStart chains, the pid-suffixed header file
// emission (the for/jmp_buf/Arg/tmp/cvars declarations), and the
// compiler-state resets.
//
// Consults (rule base wp2/arms/BOTH/rules_v0.pl):
//   h004 globals_ext - reused: LabelsStart/TextStart/p1/p3/
//     FunctionName/Out/MaxTmp/forxmax/forx/IF_flg/IF_flg_max/
//     FCN_flg/ForBlocks/ArgMax/cvars/cvarx ride the
//     CompilerGlobalsExt singleton (the unit-151 seats; this is
//     the first consumer render of the label/text chain externs).
//   h007 char_buffer - reused: the emitted text rides exact
//     String shapes; the hfile emissions loop-print.
//   h009 format_string - reused: every %d/%s emission is exact
//     (the Main_.%d / %s.%d filenames, the for%d family).
//   h013 dead_store - reused: the label-block t=0 never
//     increments (kept in the comment below); the top-level int i
//     is dead past the loops it feeds; linepad's sent/i/j/f are
//     dead inside the vendor body (the stub keeps the signature).
//   h014 out_of_scope_call - reused: linepad TODO stub pins its
//     pending owner; the errors.h paste is emission text, not a
//     seat (the header's roster unit 295 batches 10 ledger it).
//   h015 grammar-collision - reused: no backticks, no raw comment
//     dollars, ASCII only, trailing newline.
//   h017 naming - reused: epi/linepad/FunctionName/LabelsStart/
//     TextStart/forxmax/forx/ArgMax/MaxTmp/cvars keep vendor case.
//
// Quirks kept (ledgered):
//   - abort(): the hfile==NULL else-arm guard rides the SIGABRT
//     convention fold (kotlin.system.exitProcess(134)); in the
//     empty-FunctionName arm the vendor does NOT null-check fopen
//     (fprintf on NULL = UB); quantized to the same abort fold,
//     journaled.
//   - free(p1) on the label/text chain clears: GC owns the nodes;
//     the chain heads reset to null exactly (the observable state).
//   - fopen/fclose ride java.io.PrintWriter close (flush folds).

// fprintf(Out, ...) fold returning the written count (the linepad
// sent argument; the exact fprintf return over ASCII text)
private fun epiOutZb(s: String): Int {
    try {
        CompilerOutState.out.write(s)
    } catch (e: java.io.IOException) {
        // vendor ignores the fprintf return (failure is silent)
    }
    return s.length
}


// the mumpsc/errors.h paste: the vendor #include drops these ten
// fprintf lines verbatim into the emitted catch block (the
// GlobalExceptionCode cout ladder; emitted text, not seats).
// Visibility widened private->internal at the roster-191 render
// (zcommands_code's FCN-flg catch arm drops the same errors.h
// include; one paste, two consumers - the duplicate-emission
// fold is journaled there).
internal const val EPI_ERR_HEAD =
    "     if (GlobalExceptionCode == INPUT_ERROR) cout << \"Input Error\" << endl;\n" +
    "     else if (GlobalExceptionCode == INPUT_LENGTH) cout << \"Input Length Error\" << endl;\n" +
    "     else if (GlobalExceptionCode == SYMTAB_ERROR) cout << \"Symbol Table Error\" << endl;\n" +
    "     else if (GlobalExceptionCode == GLOBAL_NOT_FOUND) cout << \"Global Not Found Error\" << endl;\n" +
    "     else if (GlobalExceptionCode == INTERPRETER_ERROR) cout << \"Interprerer Error\" << endl;\n" +
    "     else if (GlobalExceptionCode == INVALID_CONVERSION) cout << \"Invalid Conversion Error\" << endl;\n" +
    "     else if (GlobalExceptionCode == DATA_RANGE_ERROR) cout << \"Data Range Error\" << endl;\n" +
    "     else if (GlobalExceptionCode == NUMERIC_RANGE) cout << \"Numeric Range Error\" << endl;\n" +
    "     else if (GlobalExceptionCode == UNSPECIFIED) cout << \"Unspecified Error\" << endl;\n"

// the SIGABRT convention fold (the vendor abort() on fopen NULL)
private fun abortZb() {
    kotlin.system.exitProcess(134)
}

// void epi(short ExecName, long pid) - compiler-epi.c roster 188
internal fun epi(ExecName: Short, pid: Long) {
    // int i (top) - dead past the reset loops below
    epiOutZb("\n")
    epiOutZb("//" + "=".repeat(81) + "\n")
    linepad(
        epiOutZb("_epilogue: tmp0[0]=0; tmp1[0]=0;"),
        " Function Epilogue"
    )
    epiOutZb("\tMltoa(getpid(),atmp);\n")
    epiOutZb("\tif (svPtr->LockDone) cleanLocks((char *) atmp);\n")
    linepad(
        epiOutZb("\tMglobal(CLOSE,tmp0,tmp1,svPtr);"),
        "Close Globals"
    )

    epiOutZb("\n} // end of try block \n\n")
    epiOutZb("catch(...) { \n")

    // #include <mumpsc/errors.h> - the ten-line paste above
    epiOutZb(EPI_ERR_HEAD)

    epiOutZb("\tcout << \"Uncaught error in or near line \" \n")
    epiOutZb("\t<< svPtr->LineNumber << endl;\n")
    linepad(
        epiOutZb("\tMglobal(CLOSE,tmp0,tmp1,svPtr);"),
        "Close Globals"
    )
    epiOutZb("\t} \n\n")

    val fnName = cStringOf(CompilerGlobalsExt.FunctionName)

    if (CompilerGlobalsExt.FCN_flg != 0.toShort()) {
        epiOutZb("return \"\";} /* Terminate */\n")
    } else {
        linepad(epiOutZb("return EXIT_SUCCESS; }"), "Terminate")
    }

    epiOutZb("//" + "=".repeat(81) + "\n")

    if (CompilerGlobalsExt.LabelsStart != null) {
        // int t=0 (never incremented in the vendor label block -
        // dead store kept on the record); l counts the entries
        var l = 0
        epiOutZb("\n\nint " + fnName + "_label_lookup(char *label) {\n\n")
        epiOutZb("static struct lab {\n\tconst char *text;\n\tint offset;\n\t}\n\tL[] = {\n")
        CompilerGlobalsExt.p1 = CompilerGlobalsExt.LabelsStart
        epiOutZb("\t\t\"" + CompilerGlobalsExt.p1!!.name + "\", " +
                 CompilerGlobalsExt.p1!!.lnbr)
        CompilerGlobalsExt.p1 = CompilerGlobalsExt.p1!!.next
        l++
        while (CompilerGlobalsExt.p1 != null) {
            l++
            epiOutZb(",\n\t\t\"" + CompilerGlobalsExt.p1!!.name + "\", " +
                     CompilerGlobalsExt.p1!!.lnbr)
            CompilerGlobalsExt.p1 = CompilerGlobalsExt.p1!!.next
        }
        epiOutZb("\n\t};\n\n")
        epiOutZb("int lx = -1, i, Lx=" + l + ";\n\n")
        epiOutZb("\tfor (i=0; i<Lx; i++) {\n")
        epiOutZb("\t\tif (strcmp((const char *) L[i].text,(const char *) label)==0) {\n")
        epiOutZb("\t\t\tlx=L[i].offset;\n")
        epiOutZb("\t\t\tbreak;\n")
        epiOutZb("\t\t}\n")
        epiOutZb("\t}\n")
        epiOutZb("\treturn lx;\n")
        epiOutZb(" }\n")
        // clear label table: free(p1) folds to GC (the node chain
        // heads reset exactly; the externs p1/p3 walk it first)
        CompilerGlobalsExt.p1 = CompilerGlobalsExt.LabelsStart
        while (CompilerGlobalsExt.p1 != null) {
            CompilerGlobalsExt.p3 = CompilerGlobalsExt.p1!!.next
            CompilerGlobalsExt.p1 = CompilerGlobalsExt.p3 // free(p1)
        }
        CompilerGlobalsExt.LabelsStart = null
    } else {
        epiOutZb("\n\nint " + fnName + "_label_lookup(char *label) { return -1; }\n")
    }

    if (CompilerGlobalsExt.TextStart != null) {
        var t = 0
        epiOutZb("\n\nconst char * " + fnName + "_text_function(int disp) {\n\n")
        epiOutZb("static struct txt {\n\tconst char *text;\n\tint offset;\n\t}\n\tT[] = {\n")
        CompilerGlobalsExt.p1 = CompilerGlobalsExt.TextStart
        epiOutZb("\t\t\"" + CompilerGlobalsExt.p1!!.name + "\", " +
                 CompilerGlobalsExt.p1!!.lnbr)
        CompilerGlobalsExt.p1 = CompilerGlobalsExt.p1!!.next
        t++
        while (CompilerGlobalsExt.p1 != null) {
            t++
            epiOutZb(",\n\t\t\"" + CompilerGlobalsExt.p1!!.name + "\", " +
                     CompilerGlobalsExt.p1!!.lnbr)
            CompilerGlobalsExt.p1 = CompilerGlobalsExt.p1!!.next
        }
        epiOutZb("\n\t\t};\n\n")
        epiOutZb("int tx,i,Tx=" + t + ";\n\n")
        epiOutZb("\tfor (i=0; i<Tx; i++)\n")
        epiOutZb("\t\tif (disp==T[i].offset) return T[i].text;\n")
        epiOutZb("\treturn NULL;\n")
        epiOutZb("}\n")
        CompilerGlobalsExt.p1 = CompilerGlobalsExt.TextStart
        while (CompilerGlobalsExt.p1 != null) {
            CompilerGlobalsExt.p3 = CompilerGlobalsExt.p1!!.next
            CompilerGlobalsExt.p1 = CompilerGlobalsExt.p3 // free(p1)
        }
        CompilerGlobalsExt.TextStart = null
    } else {
        epiOutZb("const char * " + fnName + "_text_function(int disp) { return NULL; }\n\n")
    }

    // the header-file block (int i; FILE *hfile; char fn[256])
    run {
        val nme: String
        if (fnName.isEmpty()) {
            // Main_.%d - the vendor does NOT null-check fopen here
            // (fprintf-on-NULL UB); quantized to the same abort fold
            nme = "Main_." + pid
        } else {
            nme = fnName + "." + pid
        }
        val hfile: java.io.PrintWriter
        try {
            hfile = java.io.PrintWriter(nme) // fopen(nme,"w")
            if (fnName.isNotEmpty()) {
                // the else-arm fopen NULL abort rides IOException
                // failure (the PrintWriter ctor throw)
            } else {
                // empty-arm quirk (header ledger)
            }
        } catch (e: java.io.IOException) {
            abortZb()
            return
        }
        var i = 1
        while (i <= CompilerGlobalsExt.forxmax) {
            hfile.print("char for" + i + "_incr[FORPARM], for" + i +
                        "_lim[FORPARM], for" + i + "_init[FORPARM];\n" +
                        "int for" + i + "F; int for" + i + "T; \n")
            i++
        }
        i = 0
        while (i <= CompilerGlobalsExt.forx) {
            hfile.print("jmp_buf for" + i + "J;\n")
            i++
        }
        i = 0
        while (i < CompilerGlobalsExt.ArgMax) {
            hfile.print("char *__Arg" + i + " = new char[STR_MAX];\n")
            i++
        }
        i = 0
        while (i <= ExecName) {
            hfile.print("jmp_buf Efor" + i + "J;\n")
            hfile.print("int tpx" + i + ";\n")
            i++
        }
        i = 0
        while (i < CompilerGlobalsExt.MaxTmp) {
            hfile.print("unsigned char *tmp" + i + " = new unsigned char[STR_MAX];\n")
            i++
        }
        i = 0
        while (i < CompilerGlobalsExt.cvarx) {
            hfile.print("char " + CompilerGlobalsExt.cvars[i]!!.name +
                        "[STR_MAX]; // declared variable\n")
            i++
        }
        CompilerGlobalsExt.cvarx = 0
        CompilerGlobalsExt.IF_flg_max = 0
        CompilerGlobalsExt.forxmax = 0
        CompilerGlobalsExt.ArgMax = 0
        hfile.close() // fclose(hfile)
    }

    writeC(CompilerGlobalsExt.FunctionName, "") // strcpy(FunctionName,"")
    CompilerGlobalsExt.IF_flg = 0
    CompilerGlobalsExt.forxmax = 0
    CompilerGlobalsExt.forx = 0
    for (i in 0 until FBMAX) CompilerGlobalsExt.ForBlocks[i] = -1
    CompilerGlobalsExt.MaxTmp = 2
    return
}
