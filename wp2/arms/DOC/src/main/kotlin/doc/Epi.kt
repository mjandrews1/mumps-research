package doc

// Unit 213 (B7 tranche 3): compiler-epi.c::epi (batch 7, complex
// stratum; units.csv row 213: loc 135, score 111.3). The emitted-C
// epilogue generator: closes the try/catch frame (Mglobal CLOSE,
// cleanLocks, the errors.h include is a COMPILER-TU include, not
// emitted text - include ledger, EpiMisc unit-63 precedent), the
// FCN_flg terminate branch vs the EXIT_SUCCESS branch, then the
// label-table and text-table emissions: each turn walk of the
// LabelsStart/TextStart TxtEntry lists (labels emit
// int %s_label_lookup(char*), the L[] struct array, the lx
// linear scan; text emits const char * %s_text_function(int),
// the T[] array, the offset probe) and both vendor clearings
// (the free(p1) walk through p3 - JVM GC no-op, the list heads
// nulled), then the FOR-parameter include file: fopen(nme,"w")
// rides the java.io.FileWriter carrier (H-001; the vendor's
// abort() on hfile NULL is the fopen failure - Java raises
// FileNotFoundException at construction, divergence-faithful),
// the for%d loop bodies emit via String.format Locale.US (H-009),
// the commented-out long/double for-loop emission pair kept
// verbatim (H-013 comment), the FunctionName empty-test drives
// the Main_.%d default name. The trailing compiler-global resets
// (cvarx, IF_flg_max/forxmax, ArgMax, FunctionName empty
// strcpy, IF_flg/forxmax/forx, the FBMAX ForBlocks sweep,
// MaxTmp=2) all kept verbatim. linepad(fprintf(Out,s), label)
// rides Stubs.linepad over the CompilerState.emit char-count
// return (H-014 seat, the emit unit-115 precedent). H-010: the
// short ExecName and long pid params render Int/Long; ExecName
// the vendor short param shadows the extern short ExecName
// seat (CompilerState.ExecName Int since the unit-206/213 TAG
// CORRECTION; shadow ledger). H-013:
// the vendor int i is declared twice (top-level and in the
// hfile block - a C block-scoped redeclare; one Kotlin var
// serves, ledgered); t=0 in the label block is dead (the
// counter read there is l). The emitted getpid/Mltoa text is
// template string, not code. Naming per H-017 (epi verbatim).

private val EPI_RULE = "//" + "=".repeat(81) // vendor banner line

fun epi(ExecName: Int, pid: Long) {
    var i: Int

    CompilerState.Out.write("\n")
    CompilerState.Out.write(EPI_RULE + "\n")

    Stubs.linepad(CompilerState.emit(
        "_epilogue: tmp0[0]=0; tmp1[0]=0;"), " Function Epilogue")
    CompilerState.Out.write("\tMltoa(getpid(),atmp);\n")
    CompilerState.Out.write("\tif (svPtr->LockDone) cleanLocks((char *) atmp);\n")
    Stubs.linepad(CompilerState.emit(
        "\tMglobal(CLOSE,tmp0,tmp1,svPtr);"), "Close Globals")

    CompilerState.Out.write("\n} // end of try block \n\n")
    CompilerState.Out.write("catch(...) { \n")

    // #include <mumpsc/errors.h> - a compiler-TU include (not
    // emitted text); include ledger, EpiMisc unit-63 precedent.

    CompilerState.Out.write("\tcout << \"Uncaught error in or near line \" \n" +
        "\t<< svPtr->LineNumber << endl;\n")
    Stubs.linepad(CompilerState.emit(
        "\tMglobal(CLOSE,tmp0,tmp1,svPtr);"), "Close Globals")
    CompilerState.Out.write("\t} \n\n")

    if (CompilerState.FCN_flg != 0) {
        CompilerState.Out.write("return \"\";} /* Terminate */\n")
    } else {
        Stubs.linepad(CompilerState.emit(
            "return EXIT_SUCCESS; }"), "Terminate")
    }

    CompilerState.Out.write(EPI_RULE + "\n")

    val fn = CstringOps.cstring(CompilerState.FunctionName)

    if (CompilerState.LabelsStart != null) {
        var l = 0

        CompilerState.Out.write(String.format(java.util.Locale.US,
            "\n\nint %s_label_lookup(char *label) {\n\n", fn))

        CompilerState.Out.write("static struct lab {\n" +
            "\tconst char *text;\n" +
            "\tint offset;\n\t}\n" +
            "\tL[] = {\n")

        var p1 = CompilerState.LabelsStart
        val first: TxtEntry = CompilerState.LabelsStart ?: return
        CompilerState.Out.write(String.format(java.util.Locale.US,
            "\t\t\"%s\", %d", first.name, first.lnbr))
        p1 = first.next
        l++
        while (p1 != null) {
            l++
            CompilerState.Out.write(String.format(java.util.Locale.US,
                ",\n\t\t\"%s\", %d", p1.name, p1.lnbr))
            p1 = p1.next
        }
        CompilerState.Out.write("\n\t};\n\n")

        CompilerState.Out.write(String.format(java.util.Locale.US,
            "int lx = -1, i, Lx=%d;\n\n", l))

        CompilerState.Out.write("\tfor (i=0; i<Lx; i++) {\n")
        CompilerState.Out.write("\t\tif (strcmp((const char *) L[i].text,(const char *) label)==0) {\n")
        CompilerState.Out.write("\t\t\tlx=L[i].offset;\n")
        CompilerState.Out.write("\t\t\tbreak;\n")
        CompilerState.Out.write("\t\t}\n")
        CompilerState.Out.write("\t}\n")
        CompilerState.Out.write("\treturn lx;\n")
        CompilerState.Out.write(" }\n")
        p1 = CompilerState.LabelsStart //* clear label table
        while (p1 != null) {
            val p3 = p1.next
            // free(p1); - JVM GC no-op (ledger).
            p1 = p3
        }
        CompilerState.LabelsStart = null
    } else {
        CompilerState.Out.write(String.format(java.util.Locale.US,
            "\n\nint %s_label_lookup(char *label) { return -1; }\n", fn))
    }

    if (CompilerState.TextStart != null) {
        var t = 0

        CompilerState.Out.write(String.format(java.util.Locale.US,
            "\n\nconst char * %s_text_function(int disp) {\n\n", fn))

        CompilerState.Out.write("static struct txt {\n" +
            "\tconst char *text;\n" +
            "\tint offset;\n\t}\n" +
            "\tT[] = {\n")

        var p1 = CompilerState.TextStart
        val firstT: TxtEntry = CompilerState.TextStart ?: return
        CompilerState.Out.write(String.format(java.util.Locale.US,
            "\t\t\"%s\", %d", firstT.name, firstT.lnbr))
        p1 = firstT.next
        t++
        while (p1 != null) {
            t++
            CompilerState.Out.write(String.format(java.util.Locale.US,
                ",\n\t\t\"%s\", %d", p1.name, p1.lnbr))
            p1 = p1.next
        }
        CompilerState.Out.write("\n\t\t};\n\n")

        CompilerState.Out.write(String.format(java.util.Locale.US,
            "int tx,i,Tx=%d;\n\n", t))

        CompilerState.Out.write("\tfor (i=0; i<Tx; i++)\n")
        CompilerState.Out.write("\t\tif (disp==T[i].offset) return T[i].text;\n")
        CompilerState.Out.write("\treturn NULL;\n")
        CompilerState.Out.write("}\n")
        p1 = CompilerState.TextStart
        while (p1 != null) {
            val p3 = p1.next
            // free (p1); - JVM GC no-op (ledger).
            p1 = p3
        }
        CompilerState.TextStart = null
    } else {
        CompilerState.Out.write(String.format(java.util.Locale.US,
            "const char * %s_text_function(int disp) { return NULL; }\n\n", fn))
    }

    run {
        // int i; - the vendor block-scoped redeclare (H-013
        // ledger; one Kotlin var serves the block).
        val hfile: java.io.Writer
        if (fn.isNotEmpty()) {
            // vendor quirk: the null-check + abort() sits only on
            // the named branch; the Main_ branch trusts fopen -
            // the FileWriter carrier raises on either failure.
            hfile = java.io.FileWriter(
                String.format(java.util.Locale.US, "%s.%d", fn, pid))
        } else {
            val nme = String.format(java.util.Locale.US, "Main_.%d", pid)
            hfile = java.io.FileWriter(nme)
        }

        i = 1
        while (i <= CompilerState.forxmax) {
            hfile.write(String.format(java.util.Locale.US,
                "char for%d_incr[FORPARM], for%d_lim[FORPARM], for%d_init[FORPARM];\n" +
                    "int for%dF; " +
                    "int for%dT; \n",
                i, i, i, i, i))
            //                          \"long for%d; \"
            //                          \"double for%dD;\\n\",
            i++
        }

        i = 0
        while (i <= CompilerState.forx) {
            hfile.write(String.format(java.util.Locale.US, "jmp_buf for%dJ;\n", i))
            i++
        }
        i = 0
        while (i < CompilerState.ArgMax) {
            hfile.write(String.format(java.util.Locale.US,
                "char *__Arg%d = new char[STR_MAX];\n", i))
            i++
        }
        i = 0
        while (i <= ExecName) {
            hfile.write(String.format(java.util.Locale.US, "jmp_buf Efor%dJ;\n", i))
            hfile.write(String.format(java.util.Locale.US, "int tpx%d;\n", i))
            i++
        }
        i = 0
        while (i < CompilerState.MaxTmp) {
            hfile.write(String.format(java.util.Locale.US,
                "unsigned char *tmp%d = new unsigned char[STR_MAX];\n", i))
            i++
        }

        i = 0
        while (i < CompilerState.cvarx) {
            hfile.write(String.format(java.util.Locale.US,
                "char %s[STR_MAX]; // declared variable\n",
                CstringOps.cstring(CompilerState.cvars[i].name)))
            i++
        }
        CompilerState.cvarx = 0

        CompilerState.IF_flg_max = 0
        CompilerState.forxmax = 0
        CompilerState.ArgMax = 0
        hfile.close() // fclose(hfile)
    }

    // strcpy(FunctionName,"")
    CstringOps.strcpyS(CompilerState.FunctionName, "")

    CompilerState.IF_flg = 0
    CompilerState.forxmax = 0
    CompilerState.forx = 0
    i = 0
    // FBMAX (compiler-macros.h:124, 4000) rides the ForBlocks
    // carrier bound (the ForBlocks FBMAX seat, unit-122).
    while (i < CompilerState.ForBlocks.size) {
        CompilerState.ForBlocks[i] = -1
        i++
    }
    CompilerState.MaxTmp = 2
    // return; - falling off the end.
}
