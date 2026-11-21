package both

// Unit 140 (BOTH B5 T2, core): interp-sysfunc.cpp::getstr1 (30 LOC).
// Batch_plan ordinal 140: core, LOC 30, score 24.4. The STR_MAX
// line reader: ch characters up to end-of-line from a FILE*, with
// the control-byte wash (bytes >127 and <32 become spaces, CR is
// skipped), the STR_MAX-1 length-error path on stderr, and the
// INPUT_LENGTH throw when the line overflows the buffer.
//
// Vendor behavior: fgetc in a for-loop capped at STR_MAX; EOF lands
// an empty string in the caller's buffer and returns -1; a newline
// breaks; \r continues WITHOUT counting (kept via the Kotlin for-
// loop increment itself); instr accumulates through std::string;
// the overflow arm prints the two stderr lines with the 69-char
// preview, drains the physical line, and breaks. Then strncpy lands
// instr in the caller's buffer; len < STR_MAX returns the length,
// else GlobalExceptionCode=INPUT_LENGTH (10, libmpscpp.h:79) and the
// MumpsGlobalException throw (the T1 roster-305 seats).
//
// Quirks kept (ledgered):
//   - VENDOR BUG: the drain loop is an ASSIGNMENT in the condition
//     ("while (chr = fgetc(opnfile)) if (chr == '\n' || chr == EOF)
//     break;") - a NUL byte in the input stops the drain early
//     (the condition's falsiness). Kept exactly and journaled.
//   - h010: the vendor declares chr short int while fgetc returns
//     int; the values the loop sees (0..255, EOF -1) fit short, so
//     the fold keeps Int with the narrowing note (no observable
//     truncation at these values).
//   - h013: the vendor tail past the unconditional throw
//     (area[i]=0; return(i); sigint(100); return(-1);) is dead
//     code, recorded here only.
//   - the length arm's instr.substr(0,69) preview is exact.
//
// BOTH consults (find_rule before render, events logged):
//   h001 io_stream_param - reused: the FILE* input is the Java
//     Reader edge via the fgetcC seat (IOException quantized to
//     EOF like C's error return; not a program edge).
//   h007 char_buffer - reused: std::string instr is the String
//     accumulator; the caller's area is a CharArray via strncpyC.
//   h012 unsigned_byte_domain - reused: the >127 / <32 numeric
//     compares stay in the byte domain.
//   h013 dead_store - hit: the dead throw-tail dropped.
//   h009 format_string - reused: the two stderr literals exact
//     (System.err, the "..." and "\n\n" tails verbatim).
//   h014 out_of_scope_call - reused: INPUT_LENGTH + the exception
//     ride the T1 seats (libmpscpp.h pins).
//   h010 numeric_promotion - hit: the short-int note above.
//   h017 naming - hit: getstr1/opnfile shaped per vendor.
internal fun getstr1(opnFile: java.io.Reader, area: CharArray): Int {
    var instr = "" // std::string instr = "";

    for (i in 0 until STR_MAX) {
        val chr = fgetcC(opnFile) // short int chr = fgetc(...) (h010 note)

        if (chr == -1) { // EOF
            writeC(area, "") // strcpy((char *) *area, "");
            return -1
        }

        if (chr == '\n'.code) break
        else if (chr > 127) instr += ' '
        else if (chr == '\r'.code) continue
        else if (chr < 32) instr += ' '
        else instr += chr.toChar()

        if (instr.length >= STR_MAX - 1) {
            System.err.print("*** Input line length error detected\n")
            System.err.print("*** " + instr.substring(0, 69) + " ...\n\n")
            // vendor drain: assignment-in-condition (quirk kept) -
            // NUL stops the drain; \n or EOF stops it physically.
            while (true) {
                val ch = fgetcC(opnFile)
                if (ch == 0) break // condition falsiness (vendor bug)
                if (ch == '\n'.code || ch == -1) break
            }
            break
        }
    }

    strncpyC(area, instr, STR_MAX) // strncpy((char *) *area, ..., STR_MAX)

    if (instr.length < STR_MAX) return instr.length

    // GlobalExceptionCode=INPUT_LENGTH; throw MumpsGlobalException();
    // past-throw tail (area[i]=0; return(i); sigint; return(-1)):
    // dead code (h013), not reproduced.
    GlobalExceptionCode = INPUT_LENGTH
    throw MumpsGlobalException()
}
