package daf

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import kotlin.system.exitProcess

// Unit 14 (DAF supplementary render session, 2026-11-27, per the
// 2026-11-26 PI adjudication of the B10 flags):
// compiler-abrtx.c::abrtx (units.csv record 14, grep line 15 - the
// B10 seats ledger cited the vendor family "not drawn on this
// arm"; 60 LOC, batch-1 core stratum; the named stub abrtxDaf
// raised at B10 is RELEASED by this render, same-name body here,
// release note at the stub site).
// The compiler exit cell: the Zmain/Zfunction warning arm
// (MAIN_flg/FCN_flg both 0 -> warning + ExitCode 0), the
// epi(ExecName,pid) / EP(1,ExecName,pid) epilogue pair (epiDaf /
// epDaf seats, CompilerMumpsMisc) with the F_flg/FCN_flg/
// MAIN_flg clears and the vendor EMPTY else block, then the
// output-file close/conversion cell: fclose(Out), the "%d lines
// of Mumps;" count, the INCLUDE-line expansion from the emitted
// text into the .cpp Final (each INCLUDE line's file, when
// openable, is spliced line-for-line with the per-line counter
// and removed; the INCLUDE line itself never reaches Final -
// the vendor continue skips the fputs), the double fclose, the
// .m intermediate remove cell and the "%d lines of C++ generated"
// banner.
//
// Consults (BASE heuristic rows): 2/26 hit-adapt (the physical C
// text collects in DafCompilerState.out - the emit-builder fold
// of the B10 driver; this render OWNS the fclose/conversion cell
// the B10 header deferred to the raised abrtx seat: the .m
// physical file never exists on this arm, so the fclose(Out) +
// fopen(output_file,"r") read-back rides C.out line iteration
// with fgets 1024-chunk semantics, disclosed); 57 hit-adapt
// (FILE* Out/Final/incl: the read side is the C.out StringReader
// fold, the write side is FileWriter; fgets 1023-char chunking
// kept verbatim); 56 hit-reuse (strncmp/strcmp over String
// startsWith/equals); 6/22/62 hit-reuse (MAIN_flg/FCN_flg/F_flg/
// ExitCode ride the raised file-scope seats mainFlgD/fcnFlgD/
// fFlgD/exitCodeD (CompilerZcommandsCode/Misc), ExecName/pid/
// lines ride C.ExecName/pidD/C.lines, output_file rides
// outputFileD (unit-178 fcninit arg seat)); 69 hit-reuse (printf
// stdout -> Kotlin print); 22 hit-reuse (malloc NULL arms are
// unreachable on the JVM - GC ledger); 99 hit-reuse (return-fold:
// the vendor returns ExitCode, rendered as the exitCodeD seat
// read); 117 hit-reuse (the tmp2[1024] carrier is the per-render
// local buffer - main's tmp2M basename carrier is a distinct
// cell, no shared read here, disclosed).
// Vendor quirks kept: (1) the EMPTY else block of the epilogue
// pair is comment-folded residue; (2) the trailing remove cell is
// the vendor's mangled arithmetic: after strcat(".cpp") the
// [strlen-2]=0 chops "pp" leaving base+".c", strcat(".m") makes
// base+".c.m" and remove() deletes NOTHING - the intended
// intermediate base+".m" is never removed (vendor quirk kept
// verbatim with the comment; on this arm the .m file never
// exists, so the cell is inert either way); (3) the INCLUDE
// splice reads the filename at offset 8 (after the 7-char
// "INCLUDE" marker plus one space, vendor &tmp2[8] cell); (4)
// abort() on the unopenable Final renders as the epi precedent
// fold (print + exitProcess(134), the abort() signal fold).
// Dafny: EXEMPT (file-side procedural exit cell, the T2 exemption
// family; journal row).

fun abrtxDaf(): Int {

    val C = DafCompilerState

    // if (MAIN_flg == 0 && FCN_flg == 0) - the warning arm
    if (mainFlgD == 0 && fcnFlgD == 0) {
        print("\n*** Warning: No Mumps Zmain or Zfunction\n")
        exitCodeD = 0
    }

    if (mainFlgD != 0) {
        epiDaf(C.ExecName, pidD)
        mainFlgD = 0
    } else {
        if (fcnFlgD != 0) {
            epDaf(1, C.ExecName, pidD)
            fFlgD = 0
            fcnFlgD = 0
        } else {
            // vendor empty else block (residue, comment-folded)
        }
    }

    // { int i=0; fclose(Out); ... } - the close/conversion cell.
    // The fclose(Out) + fopen(output_file,"r") read-back rides
    // the C.out emit-builder (the .m physical file never exists
    // on this arm; fgets 1024-chunk semantics kept verbatim).
    var i = 0

    print("${C.lines - 1} lines of Mumps;\n")

    // output_file[strlen(output_file) - 2] = '\0'; strcat(".cpp")
    val cppFile = outputFileD.substring(0, outputFileD.length - 2) + ".cpp"

    val final: FileWriter
    try {
        final = FileWriter(cppFile) // Final = fopen(output_file, "w")
    } catch (e: Exception) {
        print("Unable to open $cppFile. Check protections.\n")
        exitProcess(134) // abort() signal fold (epi precedent)
    }

    final.use { fw ->

        val reader = C.out.toString()
        var pos = 0
        val tmp2 = StringBuilder()

        // while (fgets(tmp2, 1024, Out) != NULL)
        while (pos < reader.length) {

            tmp2.setLength(0)
            var n = 0
            while (n < 1023 && pos < reader.length) {
                val ch = reader[pos]
                pos++
                tmp2.append(ch)
                n++
                if (ch == '\n') break // fgets keeps the newline
            }
            if (n == 0) break

            i++

            if (tmp2.startsWith("INCLUDE")) { // strncmp(...,7)==0

                // tmp2[strlen(tmp2)-1] = '\0'; - strip the newline
                val line = tmp2.substring(0, tmp2.length - 1)

                // strcpy(file, &tmp2[8]); - the offset-8 cell
                val inclFile = line.substring(8)

                val incl = File(inclFile)
                if (incl.isFile) { // if ((incl = fopen(...)) != NULL)
                    BufferedReader(FileReader(incl)).use { br ->
                        while (true) {
                            val inclLine = br.readLine() ?: break
                            i++
                            fw.write(inclLine + "\n") // fputs(tmp2, Final)
                        }
                    }
                    incl.delete() // remove(file)
                }
                continue // the INCLUDE line never reaches Final
            }

            fw.write(tmp2.toString()) // fputs(tmp2, Final)
        }
    }

    // fclose(Out); fclose(Final); - the use{} folds close Final;
    // the Out side is the C.out builder (no physical handle).

    // output_file[strlen(output_file) - 2] = '\0';
    // strcat(output_file, ".m"); remove(output_file);
    // The vendor mangled-remove quirk kept verbatim: the current
    // name is base+".cpp", so this cell computes base+".c.m" and
    // removes nothing (the intended base+".m" never existed on
    // this arm - inert either way; disclosed).
    val mRemove = cppFile.substring(0, cppFile.length - 2) + ".m"
    File(mRemove).delete()

    print("$i lines of C++ generated\n")

    return exitCodeD // return ExitCode;
}
