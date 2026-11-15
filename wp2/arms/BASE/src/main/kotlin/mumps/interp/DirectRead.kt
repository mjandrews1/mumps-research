package mumps.interp

/**
 * Kotlin translation of interp-sysfunc.cpp::DirectRead (Study Subset unit 24,
 * Batch 1, arm BASE; translated 2026-11-14).
 *
 * C: int DirectRead(int in, unsigned char *to) — READ *X single-char read.
 * to is a one-char out holder (heuristic 66 pattern); the C reads the
 * file-scope svPtr global — passed explicitly here.
 *
 * JVM divergences (heuristic 71):
 *  - fstat/S_ISCHR/S_ISFIFO/stty are POSIX-only. The C's fstat(...)==1
 *    bad-file branch is DEAD CODE anyway (fstat returns 0 or -1, never 1)
 *    and is preserved as a comment (heuristic 20 family).
 *  - Terminal detection: System.console() != null replaces S_ISCHR.
 *  - stty -echo raw / stty echo -raw around getchar() cannot be reproduced;
 *    the reads happen in the JVM's default mode.
 *  - FIFO stdin (S_ISFIFO -> return 0) is undetectable on the JVM and
 *    falls into the regular-file path — documented divergence.
 */
fun directRead(svPtr: MsvState, inUnit: Int, to: CharArray): Int {
    if (svPtr.io != 5) {
        // C: if (fstat(fileno(...)) == 1) { *to=0; return 0; } — dead branch
        // (fstat never returns 1); the null-reader check plays the bad-file role
        val r = svPtr.inFile.getOrNull(inUnit)
        if (r == null) { to[0] = ' '; return 0 }
        val k = r.read() // C: fgetc
        if (k == -1) { to[0] = ' '; return 0 } // C: EOF
        to[0] = k.toChar()
        return 1
    }

    // io == 5: C stats stdin to distinguish terminal / pipe / regular file
    val console = System.console()
    if (console != null) { // C: S_ISCHR — stdin is a terminal
        // C: system("stty -echo raw"); getchar(); system("stty echo -raw");
        val k = console.reader().read()
        if (k == -1) return 0
        to[0] = k.toChar()
        return 1
    }

    // C: S_ISFIFO (pipe) returns 0 — undetectable on the JVM; falls through
    // to the regular-file behavior (documented divergence, heuristic 71)
    val r = svPtr.inFile.getOrNull(inUnit)
    if (r == null) { to[0] = ' '; return 0 } // C: fstat failure tail
    val k = r.read()
    if (k == -1) return 0 // C: EOF — no *to write in this branch
    to[0] = k.toChar()
    return 1
}
