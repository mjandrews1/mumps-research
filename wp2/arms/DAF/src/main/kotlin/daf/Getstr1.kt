package daf

// Unit 139 (DAF B5 T2, core): interp-sysfunc.cpp::getstr1
// (units.csv row 139: loc 30, ptr 3, score 24.4, rank 189, core):
// the run-time line reader of the terminal/service layer: read from
// the open FILE one line up to STR_MAX-1 chars with the
// >127/<32-to-space and \\r-skip filters, the length-error
// diagnostic + rest-of-line drain, the strncpy into the caller's
// area, and the INPUT_LENGTH throw past the STR_MAX boundary.
// Consults: BASE-log row 2 family hit-adapt (FILE* -> the open
// java.io.Reader seat; fgetc -> reader.read() returning the Int
// char code, -1 at EOF exactly like the vendor contract - the
// readf_ precedent of B4 T3), row 108 hit-reuse (the getstr1 BASE
// precedent itself: the area out-param folds onto the caller
// StringBuilder - the C unsigned char** deref-write becomes
// setLength(0)+append), row 69 hit-reuse (the two stderr
// diagnostics keep the message text verbatim through System.err),
// row 40 family hit-adapt (instr.length() reads replace the
// strlen calls), row 46 family (the dead post-throw tail kept as
// comment ledger - vendor quirks are not repaired), manual-
// decision logged: the INPUT_LENGTH/throw boundary (const 10
// pinned from libmpscpp.h; MumpsGlobalExceptionD carrier).
// Dafny: EXEMPT (stream-bound reader + throw path; journal row).

const val INPUT_LENGTH_D = 10 // #define INPUT_LENGTH 10 (libmpscpp.h)

fun getstr1D(opnfile: java.io.Reader, area: StringBuilder): Int {

    var instr = "" // string instr = "";

    for (i in 0 until STR_MAX_DAF) { // for (i = 0; i < STR_MAX; i++)

        val chr = opnfile.read() // fgetc(opnfile)

        if (chr == -1) { // chr == EOF
            area.setLength(0) // strcpy((char *)*area, "")
            return -1
        }

        if (chr == '\n'.code) break // '\n' ends the read
        else if (chr > 127) instr += ' ' // chr > 127 -> ' '
        else if (chr == '\r'.code) continue // '\r' skipped
        else if (chr < 32) instr += ' ' // control chars -> ' '
        else instr += chr.toChar()

        if (instr.length >= STR_MAX_DAF - 1) {
            System.err.print("*** Input line length error detected\n")
            System.err.print("*** " + instr.substring(0, 69) + " ...\n\n")
            var c2 = opnfile.read() // while (chr = fgetc(opnfile))
            while (c2 != -1) {
                // the C truthiness of the while(chr = fgetc(...)) test
                // also stops the drain on a NUL byte (0 is falsy); the
                // quirk is kept verbatim here - vendor quirks are not
                // repaired on any arm.
                if (c2 == '\n'.code || c2 == 0 || c2 == -1) break
                c2 = opnfile.read()
            }
            break
        }
    }

    area.setLength(0)
    area.append(instr.substring(0, minOf(instr.length, STR_MAX_DAF)))
    // strncpy((char *)*area, instr.c_str(), STR_MAX)

    if (instr.length < STR_MAX_DAF) return instr.length

    // truncate lines > STR_MAX bytes in length (vendor comment)

    GlobalExceptionCodeD = INPUT_LENGTH_D
    throw MumpsGlobalExceptionD(
        "getstr1: input line length " + GlobalExceptionCodeD)

    // The vendor tail below the throw is unreachable and ledgered
    // (H-013 family): area[i]=0; return(i); sigint(100); return(-1).
}
