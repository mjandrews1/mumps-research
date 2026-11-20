package doc

// Unit 140 (B5 tranche 2): interp-sysfunc.cpp::getstr1 (batch 5, core
// stratum; LOC 30, score 24.4) - the std::string-accumulating line
// reader (note: NOT the batch-6 TimeOut_getstr1 sibling, whose
// prototype rode the unit-19 roster; the unit-48 getstr2 is the
// CharArray-shaped interpreter reader - different family). The
// vendor FILE* parameter renders as a java.io.Reader per the unit-48
// H-001 carrier contract; fgetc becomes Reader.read with the C EOF
// -1 contract. The unsigned char** area out-parameter renders as
// the caller's CharArray buffer written once at the end
// (strncpy(*area, instr.c_str(), STR_MAX); H-006: the C API shape
// survives without a holder because the buffer is written only on
// the return path). chr is the short int of the vendor (H-010:
// widened to the Int read domain; the >127 and <32 filters keep the
// byte comparisons). The length-error drain loop keeps the vendor
// assignment-in-condition shape: while (chr = fgetc()) - the loop
// also EXITS on a NUL byte read (chr==0 falsy test, vendor shape,
// notable). DEAD TAIL dropped per H-013: after the
// MumpsGlobalException throw of the length arm (INPUT_LENGTH=10,
// libmpscpp.h:79 - const added here, file-private) the vendor's
// area[i]=0, return(i), sigint(100) and return(-1) are unreachable;
// dropped with this note. The instr std::string renders as a
// StringBuilder accumulated per call (H-007 scan-only String fold
// at the copy site). Naming per H-017.

private const val INPUT_LENGTH = 10 // libmpscpp.h:79

fun getstr1(opnfile: java.io.Reader, area: CharArray): Int {
    var chr: Int
    var i = 0

    val instr = StringBuilder()

    while (i < STR_MAX) {
        chr = opnfile.read()

        if (chr == -1) { // EOF
            area[0] = NUL // strcpy(*area, "")
            return -1
        }

        if (chr == LINEFEED.code) break
        else if (chr > 127) chr = ' '.code
        else if (chr == Char(13).code) {
            i++ // continue: the \r is skipped; loop counter kept
            continue
        } else if (chr < 32) chr = ' '.code

        instr.append(chr.toChar())
        if (instr.length >= STR_MAX - 1) {
            System.err.print("*** Input line length error detected\n")
            System.err.print("*** " + instr.substring(0, 69) + " ...\n\n")
            // while (chr = fgetc(opnfile)) if (chr=='\n' || chr==EOF) break;
            // - the falsy-NUL exit kept: the drain also stops on a
            // zero byte read (vendor shape)
            while (true) {
                chr = opnfile.read()
                if (chr == 0) break
                if (chr == LINEFEED.code || chr == -1) break
            }
            break
        }

        i++
    }

    // strncpy((char *) *area, instr.c_str(), STR_MAX)
    val s = instr.toString()
    var k = 0
    while (k < s.length && k < STR_MAX) {
        area[k] = s[k]
        k++
    }
    if (k < area.size) area[k] = NUL

    if (instr.length < STR_MAX) return instr.length

    /* truncate lines > STR_MAX bytes in length */

    Globals.GlobalExceptionCode = INPUT_LENGTH
    throw MumpsGlobalException()
    // dead tail dropped per H-013 (see header)
}
