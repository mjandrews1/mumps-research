package doc

// Unit 18 (B1 tranche 3): compiler-abrtx.c::abrtx (batch 1, core
// stratum; units.csv row 15: loc 60, ptr 1, score 23.2, rank 182).
// The compiler epilogue: warns when neither Zmain nor Zfunction was
// seen, runs the epi/EP emission stubs, closes Out, re-reads the
// intermediate output, splices INCLUDE files into the final .cpp,
// removes the intermediate .m file and prints the line counts. The
// file-scope externs MAIN_flg/FCN_flg/F_flg/ExitCode/ExecName/pid and
// the output_file name carrier ride CompilerState (compiler-globals
// family, H-004 single owner; Kotlin array indexing aside, all are
// scalars or a String). epi() and EP() are out-of-scope H-014 stubs
// (compiler epilogue emitters, not roster units). Divergences
// documented per H-001: the vendor reassigns the FILE* Out as a read
// handle for the splice pass - the typed Writer carrier cannot hold a
// Reader, so the splice reads through a local Reader on the same
// file; fgets strips/keeps the newline while BufferedReader readLine
// yields the line without its terminator (the fputs copy appends a
// newline); abort() renders as a thrown RuntimeException after the
// vendor's message; printf maps to System.out. The vendor name
// surgery output_file[strlen-2]=0 then strcat keeps the drop-last-2
// + append shape. Remove maps to File.delete (return value unread in
// the vendor, likewise unread here). Naming per H-017.

fun abrtx(): Int {
    if (CompilerState.MAIN_flg == 0 && CompilerState.FCN_flg == 0) {
        System.out.print("\n*** Warning: No Mumps Zmain or Zfunction\n")
        CompilerState.ExitCode = 0
    }

    if (CompilerState.MAIN_flg != 0) {
        Stubs.epi(CompilerState.ExecName, CompilerState.pid)
        CompilerState.MAIN_flg = 0
    } else {
        if (CompilerState.FCN_flg != 0) {
            Stubs.EP(1, CompilerState.ExecName, CompilerState.pid)
            CompilerState.F_flg = 0
            CompilerState.FCN_flg = 0
        } else {
            // vendor empty else branch, kept
        }
    }

    var i = 0
    CompilerState.Out.close()
    System.out.print((CompilerState.lines - 1).toString() +
            " lines of Mumps;\n")

    // H-001 divergence: the vendor reassigns Out as a read handle;
    // the splice pass reads the same file through a local Reader.
    val outReader = java.io.BufferedReader(java.io.FileReader(
            CompilerState.output_file))
    val cppFile = CompilerState.output_file.dropLast(2) + ".cpp"
    val finalW = java.io.PrintWriter(java.io.FileWriter(cppFile))
    val momFile = java.io.File(CompilerState.output_file)
    if (!momFile.exists()) {
        System.out.print("Unable to open " + CompilerState.output_file +
                ". Check protections.\n")
        throw RuntimeException("abrtx: unable to open " +
                CompilerState.output_file)
    }

    while (true) {
        val tmp2 = outReader.readLine() ?: break
        i++
        if (tmp2.startsWith("INCLUDE")) {
            val incl = java.io.File(tmp2.substring(8))
            if (incl.exists()) {
                val r = java.io.BufferedReader(java.io.FileReader(incl))
                while (true) {
                    val incLine: String? = r.readLine()
                    if (incLine == null) break
                    i++
                    finalW.println(incLine)
                }
                r.close()
                incl.delete()
            }
            continue
        }
        finalW.println(tmp2)
    }

    outReader.close()
    finalW.close()

    val mFile = CompilerState.output_file.dropLast(2) + ".m"
    java.io.File(mFile).delete()
    System.out.print(i.toString() + " lines of C++ generated\n")

    return CompilerState.ExitCode
}
