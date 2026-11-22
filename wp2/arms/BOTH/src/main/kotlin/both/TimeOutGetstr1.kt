package both

// Unit 170 (BOTH B6 T2, core): interp-sysfunc.cpp::TimeOut_getstr1
// (33 LOC). Batch_plan ordinal 170: core, LOC 33, score 14.1,
// complexity rank 135. The timed-read service of the READ
// command: a select() probe on stdin with a seconds budget, the
// stty raw-mode single-char arm for the direct flag, and the
// fgets line arm otherwise. Vendor body:
// interp-sysfunc.cpp:127-160.
//
// Vendor behavior: io must be 5 (any other unit prints
// "*** Timed read only permitted to unit 5" and sigint(100)s);
// FD_ZERO/FD_SET build the rfds set on fd 0; tv_sec = atoi
// (timeout), tv_usec = 0; dirflg arms "stty raw"; select(1,
// &rfds, ...) returns 0 on timeout - the empty string lands in
// the caller's area and "stty -raw" restores (dirflg arm) and the
// call returns 0; on data the dirflg arm takes one getchar and
// lands the single byte (plus NUL) with "stty -raw", the else arm
// fgets-es the line into area (STR_MAX) and strips the trailing
// newline byte; the return is 1.
//
// Platform folds (h014/h001, journalseated):
//   - the POSIX select probe seats as the selectStdinC named-
//     stub (BothStubs.kt; POSIX platform edge, not a corpus
//     unit): the Int return contract is select's (0 = timeout,
//     >0 = data ready).
//   - the stty raw/-raw pair seats as the sttyRaw/sttyRawOff
//     named-stubs (POSIX platform edges).
//   - the stdin edges (getchar / fgets) ride the shared
//     stdinReader seat below over System.in (the fgetcC seat
//     precedent; the JVM console edge is the h001 fold).
//
// Quirks kept (ledgered):
//   - the timeout is read via atoi (the atolC fold); a
//     non-numeric budget becomes 0 (immediate timeout), kept.
//   - the fgets arm strips the LAST byte unconditionally
//     (area[strlen-1]=0 - the newline); an empty line reads the
//     C buffer out of bounds (UB). Quantized with the empty-line
//     guard (the divergence journaled). The CR-bytes survive
//     (only the final byte drops, as in C).
//   - readLine was NOT used: it would eat a trailing CR byte -
//     the raw read walk keeps the vendor stream exact.
//   - the single-char arm lands the getchar EOF (-1) verbatim:
//     area[0] = 0xFF domain residue kept as the widened Int
//     (byte-faithful seat, h012).
//
// BOTH consults (find_rule before render, events logged):
//   h001 io_stream_param - reused and hit: stdin is the Reader
//     edge (fgetcC seat).
//   h014 out_of_scope_call - reused and hit: the POSIX platform
//     stubs (selectStdinC, sttyRaw, sttyRawOff) are named-owner
//     seats in BothStubs.kt; sigint rides the roster-254 stub.
//   h017 naming - hit: TimeOut_getstr1/dirflg/tv_sec shaped per
//     vendor (the C-underscore multiword kept per h017).

// The stdin carrier (h001): one Reader over the process stdin
// for the getchar/fgets arms (the vendor's single FILE* stdin).
// The keyword-field fold rides the MumpsMain reflection seat
// (FileDescriptor.in accessed via getField; "in" is a Kotlin
// hard keyword and the hygiene rule keeps the files free of
// backtick escapes).
private val stdinReader by lazy {
    val stdFd = java.io.FileDescriptor::class.java
        .getField("in").get(null) as java.io.FileDescriptor
    java.io.BufferedReader(java.io.InputStreamReader(java.io.FileInputStream(stdFd)))
}

internal fun TimeOut_getstr1(io: Int, area: CharArray, timeout: String,
                             dirflg: Int): Int {
    if (io != 5) {
        print("*** Timed read only permitted to unit 5\n")
        sigint(100)
    }

    val tvSec = atolC(timeout).toInt() // atoi(timeout)

    if (dirflg != 0) sttyRaw()

    val i = selectStdinC(tvSec) // select(1,&rfds,NULL,NULL,&tv)

    if (i == 0) {
        writeC(area, "") // strcpy(area,"")

        if (dirflg != 0) sttyRawOff()

        return 0
    }

    if (dirflg != 0) {
        val ch = stdinReader.read() // getchar()
        area[0] = ch.toChar() // the widened EOF (-1) kept (h012)
        area[1] = '\u0000'
        sttyRawOff()
    } else {
        // fgets(area,STR_MAX,stdin): keep the CR byte if the line
        // carries one (readLine would eat it; the raw walk keeps
        // the vendor byte stream, then strips the last byte).
        val line = StringBuilder()
        while (line.length < STR_MAX) {
            val ch = stdinReader.read()
            if (ch == -1) break // EOF: fgets NULL
            line.append(ch.toChar())
            if (ch == '\n'.code) break
        }
        if (line.isEmpty()) {
            writeC(area, "") // the quantized empty-line guard
        } else {
            // area[strlen(area)-1]=0: strip the trailing newline byte
            writeC(area, line.substring(0, line.length - 1))
        }
    }

    return 1
}
