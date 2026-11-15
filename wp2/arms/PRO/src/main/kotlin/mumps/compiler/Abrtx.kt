package mumps.compiler

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.io.IOException

/**
 * PRO-arm translation of compiler-abrtx.c::abrtx (pilot B1, unit 18).
 * Translated independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), reference source
 * lines 39-112.
 *
 * abrtx is the compiler abort/close-out: emits the epilogue, re-opens
 * the emitted intermediate file, splices INCLUDE files into the final
 * .cpp, removes the intermediates, and returns ExitCode.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h001 io_stream_param     : Out/Final Writer edges are real File
 *    I/O here (fclose(Out) then re-open for reading).
 *  - h004 globals_ext         : MAIN_flg/FCN_flg/F_flg/ExitCode/
 *    ExecName/pid/output_file/Final on CompilerState (header
 *    lines 51/117/141/107/128/158/170/106).
 *  - h012 unsigned_byte_domain: output_file byte domain; the
 *    output_file name surgery (chop-2 + strcat) is done with
 *    strcatC exactly as the vendor does.
 *  - h014 out_of_scope_call   : epi (owner compiler-epi.c::epi) and
 *    EP (owner compiler-mumps.c::EP) -> Stubs.kt, owners named.
 *  - h017 naming              : abrtx keeps the vendor name.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The empty else { } of the MAIN_flg/FCN_flg cascade is kept.
 *  - The final remove() targets output_file after the .cpp surgery
 *    (chop-2 of "...x.cpp" then strcat ".m" gives "...x.cm") - the
 *    intermediate .m file name is no longer in output_file at that
 *    point; the vendor removal thus targets the post-surgery name.
 *    The identical string surgery is performed here.
 *
 * Documented divergence: vendor abort() on Final-open failure raises
 * SIGABRT; Kotlin throws IllegalStateException (no JVM process-abort
 * analog short of exitProcess, which would skip stack unwinding).
 */
@Throws(IOException::class)
fun abrtx(): Int {
    val st = CompilerState

    if (st.MAIN_flg == 0.toShort() && st.FCN_flg == 0.toShort()) {
        print("\n*** Warning: No Mumps Zmain or Zfunction\n")
        st.ExitCode = 0
    }

    if (st.MAIN_flg != 0.toShort()) {
        epi(st.ExecName, st.pid) // h014 stub
        st.MAIN_flg = 0
    } else {
        if (st.FCN_flg != 0.toShort()) {
            EP(1, st.ExecName, st.pid) // h014 stub
            st.F_flg = 0
            st.FCN_flg = 0
        } else {
            // vendor: empty else branch
        }
    }

    // C: fclose(Out); then Out = fopen(output_file, "r");
    st.out.close()

    print((st.lines - 1).toString() + " lines of Mumps;\n")

    val outPath = cstringAt(st.output_file)
    val outReader = BufferedReader(FileReader(outPath))

    // output_file[strlen(output_file) - 2] = '\0';
    // strcat(output_file,".cpp");
    val len = strlenC(st.output_file)
    st.output_file[len - 2] = NUL
    strcatC(st.output_file, ".cpp")

    val finalPath = cstringAt(st.output_file)
    val finalFile = File(finalPath)
    val finalWriter = try {
        FileWriter(finalFile)
    } catch (e: IOException) {
        // vendor: printf + abort()
        print("Unable to open " + finalPath + ". Check protections.\n")
        throw IllegalStateException("abort(): unable to open " + finalPath)
    }
    st.Final = finalWriter

    var i = 0
    while (true) {
        // while (fgets(tmp2, 1024, Out) != NULL)
        val line = outReader.readLine() ?: break
        i++
        if (line.length >= 7 && strncmpStr(line, "INCLUDE", 7) == 0) {
            // char file[64]; tmp2[strlen-1]='\0'; strcpy(file,&tmp2[8]);
            val file = line.substring(8)
            val incl = File(file)
            if (incl.exists()) {
                val inclReader = BufferedReader(FileReader(incl))
                while (true) {
                    val l2 = inclReader.readLine() ?: break
                    i++
                    st.Final.write(l2)
                    st.Final.write("\n") // fgets keeps the newline
                }
                inclReader.close()
                incl.delete() // remove(file)
            }
            continue
        }
        st.Final.write(line)
        st.Final.write("\n") // fputs carries the fgets-kept newline
    }

    outReader.close()
    st.Final.close()

    // output_file[strlen(output_file)-2] = '\0';
    // strcat(output_file,".m");
    // remove(output_file);   /* remove intermediate .m file */
    val len2 = strlenC(st.output_file)
    st.output_file[len2 - 2] = NUL
    strcatC(st.output_file, ".m")
    File(cstringAt(st.output_file)).delete()

    print(i.toString() + " lines of C++ generated\n")

    return st.ExitCode.toInt()
}

/** strncmp(s, lit, n) for a Kotlin string against a literal. */
private fun strncmpStr(s: String, lit: String, n: Int): Int {
    for (k in 0 until n) {
        val sc = if (k < s.length) s[k] else NUL
        val lc = if (k < lit.length) lit[k] else NUL
        if (sc != lc) return sc.code - lc.code
        if (sc == NUL) return 0
    }
    return 0
}
