package mumps.mdh

import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.FileReader
import mumps.interp.NUL
import kotlin.system.exitProcess

/**
 * PRO-arm translation of libmpscpp.cpp::StopInit (B6 tranche B,
 * unit 174, rank 118 - the FINAL tranche B row). Translated
 * independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), reference
 * source lines 7729-7761 (the string overload that owns the
 * body; the char* forwarder 7720 and the mstring forwarder 7725
 * relay to it, and the MSV wrapper at 10497 was rendered by unit
 * 97 in LibmpscppMisc9.kt).
 *
 * Identity verified against the study tool
 * (tools/select_study_subset.py): StopInit, cpp, loc 24,
 * comment_ratio 0.04, pointer_marks 1, control_marks 5,
 * score 12.2 - exact match to wp1/study-subset/units.csv row
 * 175 (batch 6).
 *
 * StopInit(file): loads the stoplist - clears the file-scope
 * StopList, opens the file (missing file prints the banner and
 * exits EXIT_FAILURE), then per line: lowercase in place, cut at
 * the first space (the cut zeroes the space byte and breaks),
 * strip the last byte, insert the word into the set.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h001 io_stream_param : fopen(file,"r") and fclose live
 *    inside this unit - java.io.FileReader at the edge (fopen
 *    failure -> null carrier check); fgets ->
 *    BufferedReader.readLine with the newline RECONSTRUCTED:
 *    the vendor's unconditional line[i-1]=0 strip removes the
 *    fgets newline, so the render re-appends the newline
 *    sentinel to keep the strip byte-identical (a final line
 *    without a trailing newline - where the vendor would strip
 *    the word's last character instead - is quantized to the
 *    newline-terminated behavior; degenerate case, documented).
 *  - h002 simple_output_macro: cout << "Stoplist file not
 *    found." << endl -> System.out.println.
 *  - h008 static_scratch   : the vendor file-scope StopList
 *    stop (libmpscpp.cpp:7718, a set of strings) persists across
 *    calls - carried as the top-level stop set in this file
 *    (u126 SynInit syn/synx carrier precedent); the
 *    StopLookup(char*) MdhStubs stub (not a corpus unit) reads
 *    it when its owner renders.
 *  - h012 unsigned_byte_domain: tolower over the C unsigned-char
 *    domain - private tolowerC (ScanAlnum/SynInit precedent; the
 *    A-Z fold matches the C locale).
 *  - h014 out_of_scope_call : this unit RESOLVES the MdhStubs
 *    StopInit(String) stub raised by unit 97 (correct batch-6
 *    owner tag; the LibmpscppMisc9.kt:529 MSV wrapper call site
 *    binds unchanged; the MdhStubs StopLookup stub STAYS - not a
 *    corpus unit).
 *  - h015 grammar_collision : the three C++ overloads (char*
 *    7720, mstring 7725, string 7729) collapse to the single
 *    Kotlin String parameter (u126 SynInit overload-collapse
 *    precedent); the removed stub clears the top-level seat.
 *  - h017 naming           : StopInit/stop keep the vendor
 *    names.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The space-cut loop lowercases as it scans and breaks AT the
 *    first space with line[i]=0 and the extra i++ INSIDE the
 *    assignment statement (7749) - the following unconditional
 *    line[i-1]=0 then re-zeroes the same space position (a
 *    no-op on the space arm, the newline strip on the no-space
 *    arm); both kept verbatim.
 *  - stop.clear() empties the set on every call - repeated
 *    StopInit calls reload, not accumulate; kept.
 */
/** vendor libmpscpp.cpp:7718: StopList stop; (h008 carrier -
 *  StopLookup reads this). */
val stop = mutableSetOf<String>()

/** C tolower over the byte domain (C locale ASCII fold; h012). */
private fun tolowerC(c: Char): Char = if (c in 'A'..'Z') c + 32 else c

/** vendor 7729-7761: void StopInit(string file). */
fun StopInit(file: String) {
    stop.clear()

    val fp: BufferedReader? = try {
        BufferedReader(FileReader(file))
    } catch (e: FileNotFoundException) {
        null
    }

    if (fp == null) {
        System.out.println("Stoplist file not found.")
        exitProcess(1) // exit(EXIT_FAILURE)
    }

    while (true) {
        val raw = fp.readLine() ?: break // fgets==NULL -> break

        // fgets contract: the line buffer carries the newline;
        // the vendor's last-byte strip removes it (see h001).
        val buf = CharArray(raw.length + 2)
        for (k in raw.indices) buf[k] = raw[k]
        buf[raw.length] = '\n'
        buf[raw.length + 1] = NUL

        // vendor 7747-7754: lowercase in place, cut at the first
        // space (line[i++]=0 with the increment inside, break)
        var i = 0
        while (buf[i] != NUL) {
            if (buf[i] == ' ') {
                buf[i] = NUL
                i++
                break
            }
            buf[i] = tolowerC(buf[i])
            i++
        }

        // vendor 7756: line[i-1]='\0' - re-zeroes the space
        // position (no-op) or strips the newline.
        buf[i - 1] = NUL

        // vendor 7757: stop.insert(line)
        var w = 0
        val sb = StringBuilder()
        while (buf[w] != NUL) {
            sb.append(buf[w])
            w++
        }
        stop.add(sb.toString())
    }

    fp.close() // fclose(fp)
}
