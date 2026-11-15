package mumps.interp

/**
 * Kotlin translation of interp-sysfunc.cpp::<misc> — the READ machinery
 * cluster: getstr1 + TimeOut_getstr1 (Study Subset unit 25, Batch 1,
 * arm BASE; translated 2026-11-14).
 *
 * getstr1: unsigned char** area -> StringBuilder out param; the C++ throw
 * is followed by UNREACHABLE code (area[i]=0; return i; sigint(100);
 * return -1) — preserved as a comment (heuristic 72, dead-after-throw).
 * The consume loop's assignment-in-condition (while (chr = fgetc(...)))
 * is falsy on a NUL byte AND on nothing else — both exits preserved.
 *
 * TimeOut_getstr1: POSIX select()/FD_* timed poll on stdin has no JVM
 * analog (heuristic 72): console-present -> readable; no console ->
 * timeout path (return 0). File-redirected stdin is NOT supported for
 * timed reads — documented divergence, interactive-only path unreachable
 * by the frozen oracle. stty raw/echo dropped (heuristic 71).
 */

// --- C: libmpscpp.h exception codes ---
const val INPUT_ERROR = 6
const val INPUT_LENGTH = 10

/** C: int GlobalExceptionCode (libmpscpp.cpp global). */
var GlobalExceptionCode: Int = 0

/** C: class MumpsGlobalException (libmpscpp.h:332) */
class MumpsGlobalException : Exception()

/** C: int getstr1(FILE *opnfile, unsigned char **area) */
fun getstr1(opnfile: java.io.Reader, area: StringBuilder): Int {
    val instr = StringBuilder()
    var i = 0
    while (i < STR_MAX) { // C: for (i=0; i<STR_MAX; i++)
        val chr = opnfile.read() // C: fgetc(opnfile)
        if (chr == -1) { // C: EOF
            area.setLength(0)
            return -1
        }
        if (chr == 10) break // C: '\n'
        else if (chr > 127) instr.append(' ')
        else if (chr == 13) { i++; continue } // C: '\r' — skipped, loop count preserved
        else if (chr < 32) instr.append(' ')
        else instr.append(chr.toChar())

        if (instr.length >= STR_MAX - 1) { // C: input line length error
            System.err.print("*** Input line length error detected\n")
            System.err.print("*** " + instr.substring(0, minOf(69, instr.length)) + " ...\n\n")
            while (true) { // C: while (chr = fgetc(opnfile)) if (chr=='\n' || chr==EOF) break;
                val c2 = opnfile.read()
                if (c2 == 0) break // assignment-in-condition: falsy only on NUL
                if (c2 == 10 || c2 == -1) break
            }
            break
        }
        i++
    }

    area.setLength(0) // C: strncpy((char *) *area, instr.c_str(), STR_MAX)
    area.append(instr)

    if (instr.length < STR_MAX) return instr.length

    // C: GlobalExceptionCode = INPUT_LENGTH; throw MumpsGlobalException();
    // UNREACHABLE after the throw, preserved: area[i]=0; return(i); sigint(100); return(-1);
    GlobalExceptionCode = INPUT_LENGTH
    throw MumpsGlobalException()
}

/** C: int TimeOut_getstr1(int io, unsigned char area[], char *timeout, int dirflg) */
fun timeOutGetstr1(io: Int, area: StringBuilder, timeout: String, dirflg: Int): Int {
    if (io != 5) {
        print("*** Timed read only permitted to unit 5\n")
        sigint(100)
    }

    // C: select(1, &rfds, NULL, NULL, &tv) with tv_sec = atoi(timeout) —
    // JVM substitute (heuristic 72); atoi(timeout) result is unused either
    // way under this mapping and is noted, not evaluated.
    val console = System.console()
    if (console == null) { // no interactive stdin -> timeout path
        area.setLength(0)
        return 0
    }

    if (dirflg != 0) {
        // C: system("stty raw"); i = getchar(); area[0]=i; area[1]=0; stty -raw
        val i = console.reader().read()
        area.setLength(0)
        area.append(if (i == -1) ' ' else i.toChar())
    } else {
        // C: fgets(area, STR_MAX, stdin); area[strlen-1]=0 — readLine strips the newline
        val line = java.io.BufferedReader(console.reader()).readLine()
        area.setLength(0)
        if (line != null) area.append(line)
    }
    return 1
}
