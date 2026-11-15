package mumps.interp

/**
 * Kotlin translation of interp-parse.cpp::getstr2 (Study Subset unit 48,
 * Batch 2, arm BASE; translated 2026-11-15).
 *
 * C: short getstr2(short int lun, unsigned char area[], struct MSV *
 * svPtr) (interp-parse.cpp:1480-1512) — line read from io unit lun into
 * area: EOF -> area empty, -1; LINEFEED -> line, its length; bytes >127
 * and <32 become ' '; CR is DROPPED (i-- rewinds the slot); TAB stored
 * verbatim (the C's area[i]=TAB branch is a no-op assignment); line
 * length restricted to STR_MAX.
 *
 * DEAD BRANCH PRESERVED (documented, faithful-bugs): the C's
 * if (area[i]=='\n') i-- can never run — LINEFEED is '\012' and returns
 * first. Kept as a comment.
 *
 * FILE MODEL: svPtr->in_file[lun] is MsvState.inFile (unit-23 model).
 * C's lun==5 rebind to stdin has no JVM FILE analog (heuristic 71
 * family): a lazily-created singleton console reader is bound on first
 * use — the C reassigns stdin on EVERY call, but a fresh JVM wrapper
 * would drop read-ahead buffered bytes between calls, so the singleton
 * is the faithful behavior (documented divergence).
 *
 * area[] -> StringBuilder out-param (getstr1 precedent, heuristic 56
 * holder form): the C mutates area[i] progressively, but that is only
 * observable at a return point, so the line is built locally and moved
 * into area at each return (EOF -> setLength(0), matching area[0]=0).
 */
private val stdinReader: java.io.BufferedReader by lazy {
    System.`in`.bufferedReader()
}

/** C: short getstr2(short int lun, unsigned char area[], struct MSV * svPtr) */
fun getstr2(lun: Short, area: StringBuilder, svPtr: MsvState): Short {
    // C: if (lun == 5) svPtr->in_file[lun] = stdin;
    if (lun.toInt() == 5 && svPtr.inFile[5] == null) svPtr.inFile[5] = stdinReader

    val f = svPtr.inFile[lun.toInt()]!! // C: fgetc(svPtr->in_file[lun]) — NULL deref equivalent
    val sb = StringBuilder()

    var i = 0
    while (i < STR_MAX) { // C: for (i = 0; i < STR_MAX; i++) — restrict input line length
        val chr = f.read() // C: chr = fgetc(...);

        if (chr == -1) { // C: if (chr == EOF) { area[0] = 0; return (-1); }
            area.setLength(0)
            return -1
        }

        var c = chr.toChar() // C: area[i] = chr;

        if (c == Char(10)) { // C: if (area[i] == LINEFEED) { area[i] = 0; return (i); }
            area.setLength(0)
            area.append(sb)
            return i.toShort()
        }

        if (c.code > 127) c = ' ' // C: if (area[i] > 127) area[i] = ' ';

        // C: if (area[i] == '\n') i--; — DEAD: LINEFEED ('\012') returns above.
        if (c == '\r') { // C: else if (area[i] == '\r') i--; — CR slot rewound, byte dropped
            // no store; i++ below cancels the C's i--
        } else if (c == Char(9)) { // C: else if (area[i] == TAB) area[i] = TAB; — no-op store
            sb.append(c)
        } else if (c.code < 32) { // C: else if (area[i] < 32) area[i] = ' ';
            sb.append(' ')
        } else {
            sb.append(c)
        }
        i++
    }

    area.setLength(0) // C: area[STR_MAX] = 0; — restricted length
    area.append(sb)
    return -1
}
