package mumps.compiler

/**
 * PRO-arm translation of compiler-sys1.c::getstr1
 * (batch-8 unit 229, units.csv data line 229, rank 165).
 * Translated independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), reference
 * source lines 351-394 (K&R definition; the compiler-globals.h
 * prototype "int getstr1(FILE *, char[])" is the vendor's own
 * abbreviated forward declaration - the real signature is the
 * five-parameter definition, kept here).
 *
 * Identity verified against the study tool (tools/select_study_
 * subset.py): compiler-sys1.c::getstr1, c, loc 37,
 * comment_ratio 0.026, pointer_marks 1, control_marks 10,
 * score 19.3, rank 165, batch 8 - exact match to
 * wp1/study-subset/units.csv line 229.
 *
 * getstr1(popn, opnflg, opnfile, lun, area): read one input
 * line from the device wired at LUN `lun` into `area` - the
 * ESC-r pipe-request arm for device flag 3, stdin for LUN 5,
 * and the fgetc loop over the open file otherwise.
 *
 * No prior stub: the ReadCode emissions reference getstr1 as
 * EMITTED C TEXT only (unit-138 KDoc); this is the first
 * Kotlin seat (h015 clean).
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h001 io_stream_param: FILE* -> java.io.Reader (Readf
 *    precedent); the raw-int pipe descriptors of the flag-3
 *    arm have no JVM analog - the request/response streams
 *    ride the Getstr1State carrier below, to be wired by the
 *    pipe-owning unit (batch-9/10 residue, journaled); an
 *    unwired arm fails loudly.
 *  - h010 numeric_promotion: short int chr/fgetc result
 *    becomes Int; lun stays Int (C short at the seat edge).
 *  - h012 unsigned_byte_domain: area/opnflg are Char(0..255)
 *    domains; the >127 and <32 folds read raw bytes.
 *  - h014 out_of_scope_call: stdin via the reflective
 *    System.in fetch (unit-188 precedent).
 *  - h015 grammar_collision: new top-level getstr1 - no
 *    collisions (the emitted-C references in ReadCode are
 *    string literals).
 *  - h017 naming      : getstr1 keeps the vendor name.
 *  - h018 preprocessor_conditional: none in this body.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The flag-3 arm writes ESC 'r' then read(fd, area, 1000)
 *    - the read length 1000 matches the loop bound below
 *    (kept).
 *  - if (lun == 5) opnfile[lun] = stdin; happens EVERY call
 *    (no one-shot gate) - kept.
 *  - The dead checks: after `if (area[i] == LINEFEED)`, the
 *    later `if (area[i]=='\n') i--;` arm is unreachable
 *    (LINEFEED IS '\n', defines.h:196) - kept verbatim.
 *  - `if (area[i]=='\t') area[i]='\t';` is a vendor no-op
 *    (kept).
 *  - area[1000]=0 writes past the i<1000 loop bound (the
 *    caller's buffer must be >= 1001; vendor shape kept).
 *  - The K&R parameter order in the definition (popn, opnflg,
 *    opnfile, lun, area) differs from the K&R declarations'
 *    order - the definition's order is kept.
 */

private const val ESC = 27 // vendor: area[0] = 27
private const val LINEFEED = '\n' // vendor defines.h:196 ('\012')

/**
 * Carrier for the flag-3 pipe arm (h001): the vendor writes
 * the 2-byte ESC-r request to the raw descriptor popn[lun][1]
 * and reads the response from popn[lun][2].  The JVM cannot
 * address a raw int fd; the pipe-owning unit (batch-9/10
 * residue) wires the two streams here.
 */
private object Getstr1State {
    /** vendor: write(popn[lun][1], area, 2) - request side. */
    var requestOut: java.io.OutputStream? = null

    /** vendor: read(popn[lun][2], area, 1000) - response side. */
    var responseIn: java.io.InputStream? = null
}

fun getstr1(popn: Array<IntArray>, opnflg: CharArray, opnfile: Array<java.io.Reader?>,
            lun: Int, area: CharArray): Int {
    // vendor: if (opnflg[lun] == 3) { area[0]=27; area[1]='r';
    //         area[2]=0; write(popn[lun][1], area, 2);
    //         i = read(popn[lun][2], area, 1000); return (i); }
    if (opnflg[lun] == 3.toChar()) {
        area[0] = ESC.toChar()
        area[1] = 'r'
        area[2] = 0.toChar()
        val out = Getstr1State.requestOut
            ?: throw IllegalStateException(
                "getstr1 flag-3 arm: request pipe not wired (owner: pipe machinery, batch-9/10 residue)"
            )
        out.write(area[0].code)
        out.write(area[1].code) // 2-byte request
        val inp = Getstr1State.responseIn
            ?: throw IllegalStateException(
                "getstr1 flag-3 arm: response pipe not wired (owner: pipe machinery, batch-9/10 residue)"
            )
        var n = 0
        while (n < 1000) { // vendor: read(popn[lun][2], area, 1000)
            val c = inp.read()
            if (c < 0) break
            area[n] = c.toChar()
            n++
        }
        return n
    }

    // vendor: if (lun == 5) opnfile[lun] = stdin;
    if (lun == 5) {
        val stdin = System::class.java.getField("in").get(null) as java.io.InputStream
        opnfile[lun] = java.io.InputStreamReader(stdin)
    }

    val reader = opnfile[lun]
        ?: throw IllegalStateException("getstr1: LUN " + lun + " not open (vendor reads a NULL FILE*)")

    var i = 0
    while (i < 1000) { // vendor: for (i = 0; i < 1000; i++) - restrict input line length
        val chr = reader.read() // vendor: chr = fgetc(opnfile[lun])
        if (chr == -1) { // vendor: chr == EOF
            area[0] = 0.toChar()
            return -1
        }

        area[i] = chr.toChar()
        if (area[i] == LINEFEED) {
            area[i] = 0.toChar()
            return i
        }

        if (area[i] > '\u007F') area[i] = ' ' // vendor: >127 -> ' '
        if (area[i] == '\n') { // vendor dead arm (LINEFEED is '\n')
            i--
        } else if (area[i] == '\r') {
            i--
        } else if (area[i] == '\t') {
            area[i] = '\t' // vendor no-op, kept
        } else if (area[i] < ' ') {
            area[i] = ' ' // vendor: <32 -> ' '
        }
        i++
    }

    area[1000] = 0.toChar() // vendor: restricted length
    return -1
}
