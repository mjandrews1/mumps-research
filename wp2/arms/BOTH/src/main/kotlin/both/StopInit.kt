package both

// Unit 166 (BOTH B6 T2, core): libmpscpp.cpp::StopInit (24 LOC) -
// the std::string overload. Batch_plan ordinal 166: core, LOC 24,
// score 12.2, complexity rank 118. OVERLOAD note: the vendor
// carries three delegating heads - StopInit(char *) at line 7720
// and StopInit(mstring) at line 7725 both fold to this string
// body (libmpscpp.cpp:7729-7752); the encoded corpus row 166 is
// the string overload (loc 24, single pointer mark = c_str()),
// and the delegating heads are ledgered here, not rendered
// separately.
//
// Vendor behavior: the file-scope stop list (StopList stop at
// libmpscpp.cpp:7718; typedef set<string> at libmpscpp.h:377) is
// cleared; the stoplist file is opened (fopen "r"); a missing
// file prints "Stoplist file not found." on stdout and exits
// EXIT_FAILURE; each fgets line (char line[512]) is walked: the
// FIRST space truncates the word (line[i]=0, with the vendor
// post-increment skip), other bytes are tolowerseated, then the
// last byte (the newline) is stripped (line[i-1]=0) and the word
// inserted into the set.
//
// Quirks kept (ledgered):
//   - the first-space truncation ALSO post-increments i, so the
//     line[i-1] strip lands on the separator byte, not the last
//     letter: "word other" inserts "word" (verified by the trace);
//     tolower only acts on bytes through the terminator.
//   - tolower is the C-locale seat: ASCII letters fold; high bytes
//     pass through unchanged (quantized, journaled).
//   - fgets caps at 511 bytes: a longer line continues as the next
//     fgets chunk (vendor segmentation kept).
//   - the strip reads line[i-1] for an empty line (fgets returned
//     only a newline is fine, but a zero-length chunk cannot occur
//     from fgets); the fold guards the empty accumulator to the
//     same observable effect.
//
// BOTH consults (find_rule before render, events logged):
//   h001 io_stream_param - reused and hit: the FILE* read edge is
//     the java Reader via the fgetcC seat (fopen "r" folds onto
//     the java.io.FileReader edge; not a program edge).
//   h015 grammar_collision - reused and hit: the stop-list seat
//     is named stopWords (vendor carrier name stop collides with
//     nothing at package scope but the seat keeps the plural fold
//     per the collision-scan documentation rule).
//   h017 naming - hit: StopInit keeps vendor case; the file/word
//     locals shaped per vendor (file, line).

// StopList stop (libmpscpp.cpp:7718; typedef libmpscpp.h:377):
// the file-scope stoplist. h004 singleton fold with provenance.
internal val stopWords = mutableSetOf<String>()

internal fun StopInit(file: String) {
    stopWords.clear()

    val u: java.io.Reader = try {
        java.io.FileReader(file)
    } catch (e: java.io.IOException) {
        println("Stoplist file not found.")
        kotlin.system.exitProcess(1) // exit(EXIT_FAILURE)
    }

    while (true) {
        // fgets(line,512,fp): at most 511 bytes, stop after newline.
        val line = StringBuilder()
        while (line.length < 511) {
            val ch = fgetcC(u)
            if (ch == -1) break // EOF: fgets returns NULL when empty
            line.append(ch.toChar())
            if (ch == '\n'.code) break
        }
        if (line.isEmpty()) break // fgets == NULL
        if (line.length == 511 && line.last() != '\n') {
            // fgets stopped at the cap without the newline: keep the
            // chunk (the rest of the physical line is the next chunk).
        }

        var i = 0
        while (i < line.length && line[i] != '\u0000') {
            if (line[i] == ' ') {
                i++ // vendor post-increment: the strip lands past
                break
            }
            line[i] = lowerC(line[i]) // tolower (C-locale seat)
            i++
        }
        // line[i-1]='\0': strip the last byte (the newline)
        val end = (i - 1).coerceAtLeast(0)
        if (end < line.length) line[end] = '\u0000'
        stopWords.add(line.substring(0, line.indexOf('\u0000').takeIf { it >= 0 } ?: line.length))
    }
    try { u.close() } catch (e: java.io.IOException) { /* fclose */ }
}

// C tolower seat (locale quantization; ASCII letters only).
private fun lowerC(c: Char): Char =
    if (c in 'A'..'Z') (c + ('a' - 'A')) else c
