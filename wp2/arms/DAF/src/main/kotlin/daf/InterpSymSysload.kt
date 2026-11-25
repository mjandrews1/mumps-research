package daf

/*
 * DAF batch-10 unit 299 (roster row 299; interp-sym.cpp::SysLoad,
 * LOC 18, ptr 1, score 8.0). Consult 2026-11-25T15:10:30Z.
 *
 * The pid-named symbol-table READ side of the SysDump/SysLoad
 * pair (the WRITE side is the unit-138 SysDumpD render).
 *
 * Fold notes: FILE* fopen/fgets/fclose fold onto java.io
 * File/BufferedReader/readLines (the SynInit readLines
 * precedent, row 60 family); the fopen NULL check is the
 * try/catch guard (file-missing returns 0); the fgets PAIRS walk
 * (name line then data line) keeps the vendor interleaving and
 * the newline truncation (inbuf[strlen-1] = 0 folds onto the
 * readLines strings, which carry no trailing newlines - the
 * strip is this note); sym_(0, inbuf, inbuf2, svPtr) rides the
 * symDaf String store seat (the unit-279 release; FIRST REAL
 * CALLER of the store arm); the 2048 buffer domain folds onto
 * the reader lines; return 1 loaded / 0 no file, verbatim.
 *
 * H-015: ASCII; no raw dollars in comments; trailing newline.
 * Dafny: EXEMPT (IO-bound loader; journal row).
 */

// int SysLoad(long pid, struct MSV *) - interp-sym.cpp
fun sysLoadD(pid: Long, svPtr: DafMsv): Int {

    val inbuf = CharArray(2048) // unsigned char inbuf[2048]
    mltoaDaf(pid, inbuf) // Mltoa(pid, inbuf)

    // indump = fopen(inbuf, "r");
    val dump = java.io.File(DafCRuntime.cstring(inbuf))
    if (!dump.exists()) return 0 // if (indump != NULL) walk, else 0

    val lines = try {
        java.io.BufferedReader(java.io.FileReader(dump)).readLines()
    } catch (e: java.io.IOException) {
        return 0 // fopen NULL fold
    }

    // while (fgets(inbuf,...) != NULL) { fgets(inbuf2,...); ...
    //         sym_(0, inbuf, inbuf2, svPtr); }
    var i = 0
    while (i < lines.size) {
        val name = lines[i]
        val value = if (i + 1 < lines.size) lines[i + 1] else ""
        i += 2

        // the fgets newlines are stripped by readLines (the
        // inbuf[strlen-1]=0 vendor folds ride the pair walk)
        symDaf(0, name, value) /* store */ // sym_(0, ...)
    }

    // fclose(indump); return 1;
    return 1
}
