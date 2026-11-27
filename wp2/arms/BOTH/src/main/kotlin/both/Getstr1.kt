package both

// Unit 228 render: compiler-sys1.c::getstr1 (roster 228, batch 8,
// 30 LOC; the K&R old-style signature). int getstr1(popn,
// opnflg, opnfile, lun, area) - the OPEN/USE device read line:
// the raw-fd request-data dance for opnflg 3 devices, the
// stdin binding for lun 5, then the fgetc loop with the 1000
// char line cap, the high-byte/low-byte blanking and the
// tab-preservation quirks.
//
// Consults (rule base wp2/arms/BOTH/rules_v0.pl):
//   h001 io_stream_param - reused: FILE *opnfile[lun] rides
//     java.io.Reader handles (the h020 fold family); fgetc is
//     Reader.read; EOF is -1.
//   h013 dead_store - hit: the '\n' and '\r' i-- branches sit
//     AFTER the LINEFEED return (defines.h:195 LINEFEED is
//     '\012' == '\n', so the '\n' branch is dead - kept in
//     shape, ledgered; the '\r' and '\t' branches remain live).
//   h014 out_of_scope_call - reused: no out-of-TU callees in
//     the body (the fgetc/write/read folds are platform edges).
//   h015 grammar-collision - reused: ASCII only, no backticks,
//     no raw comment dollars, trailing newline.
//   h017 naming - reused: getstr1 keeps vendor name (the
//     compiler-globals.h prototype ledger pin); popn/opnflg/
//     opnfile/area keep vendor names.
//   h020 pointer_handle - consulted, ADAPTED (hit): the raw
//     POSIX fd write(popn[lun][1], area, 2) + read(popn[lun][2],
//     area, 1000) request-data dance (the 27/'r' escape pair)
//     is unreachable on the JVM - quantized to the arm's
//     read-count return as -1 (the mdh GTK-edge stub family);
//     popn rides Array<IntArray> (the 7x4 fd table).
//
// Quirks kept (ledgered):
//   - the ==3 arm writes area[0]=27, area[1]='r', area[2]=0
//     BEFORE the fd pair (kept, then quantized).
//   - the loop blanks >127 to ' ' BEFORE the control-char
//     ladder (kept order); '\t' maps to itself (the else-if
//     ladder); <32 maps to ' '.
//   - area[1000] = 0 writes one past the loop cap (the caller
//     sizes the area 1024 - kept, ledgered).
//   - the stdin binding at lun 5 uses the FileDescriptor.in
//     reflection seat (the consoleInZb precedent; no keyword-
//     shaped member access).

private const val LINEFEED_G1 = '\n' // compiler-sys1.c.in:59 LINEFEED '\012'

private fun stdinReaderG1Zb(): java.io.Reader {
    // reflection seat (the MumpsMain stdFd precedent: System.in
    // spelled without the keyword-shaped member access, h017).
    val stdFd = java.io.FileDescriptor::class.java
        .getField("in").get(null) as java.io.FileDescriptor
    return java.io.InputStreamReader(java.io.FileInputStream(stdFd))
}

// int getstr1(int popn[7][4], unsigned char opnflg[],
//              FILE *opnfile[], short int lun,
//              unsigned char area[])
internal fun getstr1(
    popn: Array<IntArray>,
    opnflg: CharArray,
    opnfile: Array<java.io.Reader?>,
    lun: Int,
    area: CharArray,
): Int {
    if (opnflg[lun] == '\u0003') {
        area[0] = 27.toChar()
        area[1] = 'r'
        area[2] = '\u0000'
        // write (popn[lun][1], area, 2); /* request data */
        // i = read (popn[lun][2], area, 1000); return (i);
        // - the raw-fd dance is a JVM platform edge (h020,
        // quantized; the read count folds to -1).
        return -1
    }

    if (lun == 5) opnfile[lun] = stdinReaderG1Zb()

    var i = 0
    while (i < 1000) { /* restrict input line length */
        val chr = opnfile[lun]!!.read()
        if (chr == -1) { // EOF
            area[0] = '\u0000'
            return -1
        }

        area[i] = chr.toChar()
        if (area[i] == LINEFEED_G1) {
            area[i] = '\u0000'
            return i
        }

        if (area[i] > '\u007F') area[i] = ' '
        if (area[i] == '\n') i-- // dead: LINEFEED returns first
        else if (area[i] == '\r') i--
        else if (area[i] == '\t') area[i] = '\t'
        else if (area[i] < ' ') area[i] = ' '
        i++
    }

    area[1000] = '\u0000' /* restricted length */
    return -1
}
