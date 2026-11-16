package mumps.interp

import java.io.IOException
import java.io.InputStreamReader
import java.io.Reader

/**
 * PRO-arm translation of interp-parse.cpp::getstr2 (pilot B2, unit
 * 48, provisional under deviation C-003). Translated independently
 * from the pristine vendor source (mumps-14-may-2026.tgz, sha256
 * 621deb25...ffdbe), reference source lines 1480-1511 (units.csv:
 * 23 LOC, score 18.1, rank 156; getstr1 is a different unit in
 * interp-sysfunc.cpp, batch-5).
 *
 * The MUMPS READ command line reader: reads one byte at a time from
 * the current input unit into the area buffer, stopping at the line
 * feed; returns the line length, or -1 at EOF / overlength.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h001 io_stream_param     : FILE* in_file[lun] -> the Msv Reader
 *    carrier (stateVector.h:61, B1 unit 23); the vendor's
 *    in_file[5]=stdin becomes a fresh InputStreamReader over
 *    System.in on each call, mirroring the vendor's unconditional
 *    pointer reassignment. Adaptation (journal): an unopened unit
 *    (null Reader) raises an explicit error here - the vendor would
 *    fgetc(NULL), undefined behavior.
 *  - h002 simple_output_macro : fgetc becomes the local helper
 *    fgetcC preserving the call shape (Reader.read: -1 at EOF).
 *  - h010 numeric_promotion  : the vendor's short parameters/locals
 *    widen explicitly - lun.toInt() for indexing, i.toShort() on
 *    the return.
 *  - h012 unsigned_byte_domain: area is a Char(0..255) domain; the
 *    vendor byte comparisons (>127, <32) become .code comparisons.
 *  - h013 dead_store         : the vendor branch area[i]=='\n' is
 *    unreachable - '\n' is LINEFEED ('\012', defines.h:195) and the
 *    LINEFEED check above returns first - dropped with this note;
 *    the rest of the else-if chain is kept in order.
 *  - h017 naming             : getstr2 keeps the vendor name.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The else-if branch area[i]==TAB assigns TAB - a no-op kept
 *    verbatim.
 *  - After the loop the vendor writes area[STR_MAX]=0, one byte
 *    past the loop's bound - kept; callers are expected to size the
 *    buffer STR_MAX+1 exactly as in C.
 */

private const val STR_MAX = 32000 // vendor strmax.h:3 (file-private, cf. unit 22)
private const val LINEFEED = '\n' // vendor defines.h:195 ('\012')
private const val TAB = '\t' // vendor interp.h:56

/** h002: vendor fgetc(file) - one byte, or -1 at EOF. */
@Throws(IOException::class)
private fun fgetcC(r: Reader): Int = r.read()

@Throws(IOException::class)
fun getstr2(lun: Short, area: CharArray, svPtr: Msv): Short {
    val lunI = lun.toInt() // h010: short -> int for indexing

    // vendor: if (lun == 5) svPtr->in_file[lun] = stdin;
    if (lunI == 5) svPtr.in_file[lunI] = InputStreamReader(System.in)

    // adaptation: an unopened unit raises here; the vendor would
    // fgetc(NULL) (undefined behavior).
    val infile = svPtr.in_file[lunI] ?: error("getstr2: in_file[" + lunI + "] not open")

    var i = 0
    while (i < STR_MAX) { // vendor: for (i = 0; i < STR_MAX; i++)
        val chr = fgetcC(infile)

        // vendor: if (chr == EOF) { area[0] = 0; return (-1); }
        if (chr == -1) {
            area[0] = NUL
            return -1
        }

        area[i] = chr.toChar()

        // vendor: if (area[i] == LINEFEED) { area[i] = 0; return (i); }
        if (area[i] == LINEFEED) {
            area[i] = NUL
            return i.toShort() // h010
        }

        // vendor: if (area[i] > 127) area[i] = ' ';
        if (area[i].code > 127) area[i] = ' '

        // vendor chain, minus the unreachable '\n' branch (h013):
        if (area[i] == '\r') i--
        else if (area[i] == TAB) area[i] = TAB // vendor no-op, kept
        else if (area[i].code < 32) area[i] = ' '

        i++
    }

    // vendor: area[STR_MAX] = 0; (one past the loop bound, kept)
    area[STR_MAX] = NUL
    return -1 // vendor: return (-1);
}
