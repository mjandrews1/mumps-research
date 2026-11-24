package daf

// Unit 234 (DAF B8 T2, core): compiler-sys1.c::getstr1
// (37 LOC, score 19.3): the compiler-side READ command line reader:
// a DISTINCT-TWIN seat of the unit-139 interp-sysfunc.cpp::getstr1
// render (same C symbol name, different vendor file and parameter
// domain: the u139 seat is the Reader+area pair, THIS seat is the
// LUN-table form with the popn file-descriptor table, per the
// unit-189 substr twin precedent; vendor compiler-sys1.c:351,
// physically verified this session - the dump asserts physical
// loc 37 incl. the six K&R declaration lines).
// Consults (2026-11-24T21:19:00Z, nominal): the carrier contract
// is the Getstr2 domain - the FILE *opnfile[] table renders as
// Array<Reader?> populated by the caller (the lun==5 center binds
// the file-local stdin reader seat, the row-54 object-reference
// fold), fgetc becomes Reader.read with the C EOF -1 contract; the
// K&R shorts/ints widen to the Kotlin Int read domain (the H-010
// note). The opnflg==3 pipe arm (write(popn[lun][1]...) request +
// read(popn[lun][2]) into area, the unix exec-pipe transport) has
// NO JVM-domain seat: rendered through the RAISED posixWriteD/
// posixReadD named stubs (the fd pairs are exec-pipe ends not
// observable in the reference domain; no corpus unit owns them).
// The '\t' self-assignment no-op is kept verbatim (vendor line,
// the dead-code discipline); the 1000-line cap loop keeps the
// vendor index play (the i-- of the '\n' and '\r' skips); the
// trailing area[1000] write is carrier-guarded (the caller buffer
// may be exactly 1000 wide).
// Dafny: EXEMPT (device-table reader over stub seats; journal
// row).

private val SYSIN_READER_234: java.io.Reader =
    java.io.BufferedReader(java.io.InputStreamReader(System.`in`))

fun writePipeD(fd: Int, buf: CharArray, n: Int): Unit =
    throw NotImplementedError(
        "write(fd, buf, n): out-of-scope owner (POSIX exec-pipe write(2); platform seat; no corpus unit)")

fun readPipeD(fd: Int, buf: CharArray, n: Int): Int =
    throw NotImplementedError(
        "read(fd, buf, n): out-of-scope owner (POSIX exec-pipe read(2); platform seat; no corpus unit)")

fun getstr1D(popn: Array<IntArray>, opnflg: CharArray,
        opnfile: Array<java.io.Reader?>, lun: Int, area: CharArray): Int {

    var i: Int
    var chr: Int // short int chr (widened to the Int read domain)

    if (opnflg[lun] == Char(3)) { // if (opnflg[lun] == 3)
        area[0] = Char(27)
        area[1] = 'r'
        area[2] = NUL_D
        writePipeD(popn[lun][1], area, 2) /* request data */
        i = readPipeD(popn[lun][2], area, 1000)
        return i
    }

    if (lun == 5) opnfile[lun] = SYSIN_READER_234 // opnfile[lun] = stdin

    val rdr = opnfile[lun] // fgetc(opnfile[lun])
        ?: throw IllegalArgumentException("getstr1: null stream at lun " + lun)

    i = 0
    while (i < 1000) { /* restrict input line length */
        chr = rdr.read()

        if (chr == -1) { // chr == EOF
            area[0] = NUL_D
            return -1
        }

        area[i] = chr.toChar()
        if (area[i] == LINEFEED_D) {
            area[i] = NUL_D
            return i
        }

        if (area[i] > Char(127)) area[i] = ' '
        if (area[i] == '\n') i--
        else if (area[i] == '\r') i--
        else if (area[i] == '\t') area[i] = '\t' // vendor no-op line
        else if (area[i] < Char(32)) area[i] = ' '

        i++
    }

    if (1000 < area.size) area[1000] = NUL_D /* restricted length */
    return -1
}
