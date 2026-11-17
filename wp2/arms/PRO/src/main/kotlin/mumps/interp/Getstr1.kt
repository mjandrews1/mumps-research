package mumps.interp

import mumps.mdh.MumpsGlobalException
import mumps.mdh.MdhState
import java.io.Reader
import java.io.PrintStream

/**
 * PRO-arm translation of interp-sysfunc.cpp::getstr1 (B5 tranche B,
 * unit 138, rank 189). Translated independently from the pristine
 * vendor source (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe),
 * reference source lines 79-120.
 *
 * Identity verified against the study tool (tools/select_study_
 * subset.py): getstr1, cpp, loc 30, comment_ratio 0.032,
 * pointer_marks 3, control_marks 10, score 24.4 - exact match to
 * wp1/study-subset/units.csv row 139 (batch 5).
 *
 * getstr1(opnfile, area): the STR_MAX-sized line reader - one char
 * at a time from the caller's FILE, EOF lands an empty string and
 * -1, NL breaks, bytes above 127 and control bytes below 32
 * quantize to space, CR is skipped, and a too-long line warns on
 * stderr, drains the rest of the physical line, and breaks. The
 * accumulated std::string lands via strncpy; a line that filled
 * STR_MAX sets INPUT_LENGTH and throws MumpsGlobalException.
 * First raised in CompilerState's not-yet-raised roster (u129
 * note); this unit is its seat.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h001 io_edge            : FILE * renders Reader (Msv.in_file
 *    carrier precedent); the vendor's fgetc over it renders as a
 *    private fgetcC helper (Getstr2.kt:63 per-file precedent);
 *    fprintf(stderr,...) keeps the stderr sink (PrintStream).
 *  - h007 char_buffer       : *area is the caller's NUL-terminated
 *    byte buffer; the C unsigned char ** double pointer renders
 *    as the buffer CharArray itself (the vendor only ever uses
 *    *area - a plain pointer-to-buffer, u70 offset-convention
 *    family; the getstr2 B1 unit's area[] is the same shape).
 *  - h012 unsigned_byte_domain: chr is the C int byte carrier; the
 *    >127 and <32 tests are byte-domain comparisons kept verbatim.
 *  - h013 dead_code          : the vendor tail after the throw is
 *    UNREACHABLE dead code - area[i]=0, return(i), sigint(100),
 *    return(-1) are all past the unconditional throw; kept as this
 *    note, not rendered.
 *  - h014 out_of_scope_call  : MumpsGlobalException and the
 *    MdhState.globalExceptionCode carrier are DONE (MdhStubs.kt,
 *    B1 unit 31); direct use - no stub involved.
 *  - h015 grammar_collision  : pre-push scan incl. cross-file
 *    top-level names: new top-level getstr1 - no collisions
 *    (getstr2 is a distinct seat; TimeOut_getstr1 remains a
 *    not-yet-raised non-corpus owner per the CppsysfuncH roster).
 *  - h017 naming             : getstr1 keeps the vendor name; the
 *    C short i renders Int with the loop bound verbatim (the
 *    vendor short is a declaration artifact, no truncation is
 *    reachable under STR_MAX).
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The drain loop is while (chr = fgetc(opnfile)) - an
 *    ASSIGNMENT in the condition: a NUL byte in the input stops
 *    the drain early (vendor bug kept verbatim - the loop also
 *    exits on chr==0, not only NL/EOF).
 *  - instr grows by char append; the length check fires at
 *    STR_MAX-1 with a 69-char stderr preview (substr(0,69) kept).
 *  - strncpy's no-NUL edge at exactly STR_MAX: the vendor buffer
 *    copy is size-bounded; the Kotlin render is explicit and the
 *    over-length arm goes to the INPUT_LENGTH throw, so the
 *    missing-terminator edge never escapes this function.
 *  - EOF check precedes every quantization, so EOF never lands a
 *    space (kept order-verbatim).
 */

private const val STR_MAX = 32000 // strmax.h:3
private const val INPUT_LENGTH = 10 // vendor libmpscpp.h:79

private fun fgetcC(r: Reader): Int = r.read()

private val stderrC: PrintStream = System.err

fun getstr1(opnfile: Reader, area: CharArray): Int {
    var instr = ""

    for (i in 0 until STR_MAX) { // vendor: for (i = 0; i < STR_MAX; i++)
        val chr = fgetcC(opnfile)

        // vendor: if (chr == EOF) { strcpy((char *)*area, ""); return -1; }
        if (chr == -1) {
            area[0] = NUL
            return -1
        }

        var c = chr.toChar()
        // vendor chain: NL break; >127 -> ' '; CR continue; <32 -> ' '
        if (c == '\n') break
        else if (c.code > 127) c = ' '
        else if (c == '\r') continue
        else if (c.code < 32) c = ' '

        instr += c
        if (instr.length >= STR_MAX - 1) {
            stderrC.print("*** Input line length error detected\n")
            stderrC.print("*** " + instr.substring(0, 69) + " ...\n\n")
            // vendor: while (chr = fgetc(opnfile)) - assignment in the
            // condition; a NUL byte stops the drain early (kept)
            var d = fgetcC(opnfile)
            while (d != 0) {
                if (d == '\n'.code || d == -1) break
                d = fgetcC(opnfile)
            }
            break
        }
    }

    // vendor: strncpy((char *)*area, instr.c_str(), STR_MAX);
    val bytes = instr.toCharArray()
    bytes.copyInto(area, 0, 0, bytes.size)
    area[bytes.size] = NUL

    if (instr.length < STR_MAX) return instr.length

    /* truncate lines > STR_MAX bytes in length */

    MdhState.globalExceptionCode = INPUT_LENGTH
    throw MumpsGlobalException()
}
