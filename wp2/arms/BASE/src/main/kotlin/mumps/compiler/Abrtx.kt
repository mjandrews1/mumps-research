package mumps.compiler

import java.io.File
import java.io.Writer
import kotlin.system.exitProcess

/**
 * Kotlin translation of compiler-abrtx.c::abrtx (Study Subset unit 18,
 * Batch 1, arm BASE; translated 2026-11-14).
 *
 * C: int abrtx() — compiler wrap-up: emit epilogue, splice INCLUDE files
 * into the final .cpp, remove intermediates, return ExitCode.
 *
 * C stdio (fopen/fgets/fputs/fclose/remove) -> java.io with line-terminator
 * fidelity: lines are emitted with their original trailing newline, or
 * without one for a final unterminated line (heuristic 60). fgets's 1024-
 * byte chunking is not reproduced (whole-line reads; safe-widening like
 * heuristic 45). abort() -> exitProcess (heuristic 61).
 */
private fun Writer.copyLinesFrom(f: File, count: () -> Unit) {
    val text = f.readText()
    var idx = 0
    while (idx < text.length) {
        val nl = text.indexOf('\n', idx)
        val end = if (nl == -1) text.length else nl
        count()
        if (nl == -1) write(text.substring(idx, end)) else write(text.substring(idx, end) + "\n")
        idx = end + 1
    }
}

fun abrtx(): Int {
    val S = CompilerState

    if (S.MAIN_flg.toInt() == 0 && S.FCN_flg.toInt() == 0) {
        print("\n*** Warning: No Mumps Zmain or Zfunction\n")
        S.ExitCode = 0
    }

    if (S.MAIN_flg.toInt() != 0) {
        epi(S.ExecName, S.pid)
        S.MAIN_flg = 0
    } else if (S.FCN_flg.toInt() != 0) {
        EP(1, S.ExecName, S.pid)
        S.F_flg = 0
        S.FCN_flg = 0
    }

    // C: fclose(Out); printf(...); Out = fopen(output_file, "r");
    (S.Out as? java.io.Closeable)?.close()
    print((S.lines - 1).toString() + " lines of Mumps;\n")

    // C: output_file[strlen-2]=0; strcat(".cpp"); Final = fopen(..., "w");
    var of = S.outputFile
    val cppFile = File(of.substring(0, of.length - 2) + ".cpp")
    val finalW = try {
        cppFile.bufferedWriter()
    } catch (e: Exception) {
        print("Unable to open " + cppFile.path + ". Check protections.\n")
        exitProcess(1) // C: abort() — heuristic 61
    }

    var i = 0
    val outText = File(of).readText() // C: while (fgets(tmp2, 1024, Out) != NULL)
    var idx = 0
    while (idx < outText.length) {
        val nl = outText.indexOf('\n', idx)
        val end = if (nl == -1) outText.length else nl
        val line = outText.substring(idx, end)
        i++
        if (line.startsWith("INCLUDE")) { // C: strncmp(tmp2,"INCLUDE",7)==0
            val incl = File(line.substring(8)) // C: strcpy(file,&tmp2[8])
            if (incl.exists()) {
                finalW.copyLinesFrom(incl) { i++ } // C: fgets+fputs+fclose
                incl.delete() // C: remove(file)
            }
        } else {
            if (nl == -1) finalW.write(line) else finalW.write(line + "\n") // C: fputs
        }
        idx = end + 1
    }

    finalW.close() // C: fclose(Out); fclose(Final);

    // C: output_file[strlen-2]=0; strcat(".m"); remove(output_file);
    File(of.substring(0, of.length - 2) + ".m").delete()
    print(i.toString() + " lines of C++ generated\n")

    return S.ExitCode
}
