package doc

// Unit 166 (B6 tranche 2): libmpscpp.cpp::StopInit (batch 6, core
// stratum; LOC 24, score 12.2) - the stoplist loader, unit 171's
// StopLookup sibling table seat. RESOLVES the Stubs.stopInit stub
// raised by the unit-120 SymOpsMisc wrapper (correct batch-6 tag;
// the wrapper call site binds unchanged; the Stubs.stopLookup stub
// STAYS - not a corpus unit, it reads the carrier below when its
// owner renders). The vendor string overload is the roster unit;
// the char-star (libmpscpp.cpp:7720) and mstring (:7725) overloads
// are forwarders that collapse into the single Kotlin String
// parameter (the ScanAlnum overload-collapse precedent). h008: the
// file-scope StopList stop set (:7718) rides the top-level carrier
// below - it persists across calls like the vendor static.
//
// Render notes: fopen/fclose via the java.io Reader edge (H-001)
// with the missing-file banner and exit(EXIT_FAILURE) -
// kotlin.system.exitProcess(1); fgets via readLine with the
// NEWLINE RECONSTRUCTED: the vendor's unconditional line[i-1]=0
// strip removes the fgets newline, so the render re-appends a
// newline sentinel for byte-identical strip behavior (a final
// unterminated line, where the vendor would strip the word's own
// last character, is quantized to the newline-terminated behavior;
// degenerate case documented). The space-cut loop lowercases in
// place and breaks at the first space with the vendor's
// line[i++]=0 increment-inside-the-assignment (the following
// line[i-1]=0 re-zeroes the same space position on the space arm -
// a no-op there - and performs the newline strip on the no-space
// arm; both verbatim). tolower renders as the file-private
// C-locale byte-domain helper per H-012. Naming per H-017
// (StopInit -> stopInit seat; the set carrier keeps the lowercase
// vendor name stopWords).

// libmpscpp.cpp:7718: StopList stop - file-scope set of strings;
// the u126 SynInit syn/synx precedent carrier.
val stopWords = mutableSetOf<String>()

// tolower (H-012, C-locale ASCII domain; file-private per the
// one-owner rule)
private fun tolowerC(c: Char): Char =
    if (c.code in 65..90) (c.code + 32).toChar() else c

// void StopInit(string file)
fun stopInit(file: String) {
    stopWords.clear() // reloads, not accumulates (vendor kept)

    val reader = try {
        java.io.BufferedReader(java.io.FileReader(file))
    } catch (e: java.io.IOException) {
        System.err.print("Stoplist file not found.\n")
        kotlin.system.exitProcess(1) // exit(EXIT_FAILURE)
    }

    while (true) {
        // fgets(line,512,fp)==NULL -> break
        val raw = try {
            reader.readLine()
        } catch (e: java.io.IOException) {
            null
        } ?: break

        // vendor strips the fgets newline via line[i-1]=0; the
        // sentinel re-append quantizes the final unterminated line
        val line = CharArray(512)
        val src = (raw + "\n").toCharArray()
        var i = 0
        while (i < 511 && i < src.size) {
            line[i] = src[i]
            i++
        }

        i = 0
        while (line[i] != NUL) {
            if (line[i] == ' ') {
                line[i] = NUL
                i++ // vendor: line[i++]=0 inside the test body
                break
            }
            line[i] = tolowerC(line[i])
            i++
        }

        // the vendor's unconditional strip (no-op on the space arm
        // above; removes the trailing newline sentinel here)
        if (i > 0) line[i - 1] = NUL

        stopWords.add(CstringOps.cstring(line)) // stop.insert(line)
    }

    reader.close() // fclose(fp)
}
