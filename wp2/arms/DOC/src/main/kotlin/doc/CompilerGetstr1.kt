package doc

// Unit 234 (B8 tranche 2): compiler-sys1.c::getstr1 (batch 8, core
// stratum; LOC 37, score 19.3, rank 165; units.csv row 229; vendor
// compiler-sys1.c:351, physically verified this session: the
// dump asserts physical loc 37 = the roster row, the six K&R
// declaration lines included). The compiler-side READ command
// line reader - a DISTINCT-TWIN seat of the unit-140
// interp-sysfunc.cpp::getstr1 render (same C symbol name,
// different vendor file and parameter domain: the u140 seat is
// the Reader+area pair, THIS seat is the LUN-table form with the
// popn file-descriptor table, per the unit-189 substr twin
// precedent). Consult H-001 hit-adapt: the carrier contract is
// the Getstr2 H-001 domain - the FILE *opnfile[] table renders
// as Array<Reader?> populated by the caller (the lun==5 center
// binds STDIN_READER, the unit-48 FileDescriptor.in domain),
// fgetc becomes Reader.read with the C EOF -1 contract; the
// K&R shorts/ints keep their domains (H-010). The opnflg==3
// pipe arm (write(popn[lun][1]...) request + read(popn[lun][2])
// into area, the unix exec-pipe transport) has NO JVM-domain
// seat: rendered through the new Stubs.posixWrite/posixRead
// H-014 seats raising out-of-scope (the fd pairs are exec-pipe
// ends not observable in the reference domain; no corpus unit
// owns them). The '\t' self-assignment no-op is kept verbatim
// (vendor line, H-013); the 1000-line cap loop keeps the vendor
// index play (the i-- of the '\n' and '\r' skips); the trailing
// area[1000] write is carrier-guarded per H-007 (the caller
// buffer may be exactly 1000 wide). Naming per H-017.

fun getstr1(
    popn: Array<IntArray>, opnflg: CharArray,
    opnfile: Array<java.io.Reader?>, lun: Int, area: CharArray
): Int {
    var i: Int

    // chr: short int of the vendor (H-010: widened to the Int
    // read domain; the >127/<32 filters keep the byte comparisons)
    var chr: Int

    if (opnflg[lun] == Char(3)) {
        area[0] = Char(27)
        area[1] = 'r'
        area[2] = NUL
        Stubs.posixWrite(popn[lun][1], area, 2) /* request data */
        i = Stubs.posixRead(popn[lun][2], area, 1000)
        return i
    }

    if (lun == 5) opnfile[lun] = STDIN_READER

    // fgetc(opnfile[lun]): vendor dereference; the carrier keeps
    // callers honest instead of the C UB
    val rdr = opnfile[lun]
        ?: throw IllegalArgumentException("getstr1: null stream at lun " + lun)

    // vendor: for (i = 0; i < 1000; i++) with in-body i-- cancels;
    // rendered as the while form (the for var is not reassignable)
    i = 0
    while (i < 1000) { /* restrict input line length */
        chr = rdr.read()

        if (chr == -1) { // EOF
            area[0] = NUL
            return -1
        }

        area[i] = chr.toChar()
        if (area[i] == LINEFEED) {
            area[i] = NUL
            return i
        }

        if (area[i] > Char(127)) area[i] = ' '
        if (area[i] == '\n') i--
        else if (area[i] == '\r') i--
        else if (area[i] == '\t') area[i] = '\t'
        else if (area[i] < Char(32)) area[i] = ' '

        i++
    }

    if (1000 < area.size) area[1000] = NUL /* restricted length */
    return -1
}
