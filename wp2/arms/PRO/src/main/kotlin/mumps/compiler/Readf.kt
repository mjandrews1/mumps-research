package mumps.compiler

/**
 * PRO-arm translation of compiler-sys1.c::readf_ (B3 unit 78, rank
 * 129). Translated independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), reference source
 * lines 431-467.
 *
 * Identity verified against the study tool (tools/select_study_
 * subset.py): compiler-sys1.c::readf_, c, loc 23, comment_ratio
 * 0.042, pointer_marks 1, control_marks 7, score 13.3, batch 3 -
 * exact match to wp1/study-subset/units.csv row 129.
 *
 * readf_(infile): reads the file behind infile into pd1, dropping
 * bytes over 200, carriage returns (CRTN) and CTRLZ; LINEFEED
 * writes a NUL and continues accumulating; returns 1 at EOF.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h001 io_stream_param : FILE* infile -> java.io.Reader carrier
 *    (the DirectRead precedent); getc -> Reader.read.
 *  - h004 globals_ext     : pd1/pd1len -> CompilerState
 *    (compiler-globals-ext.h:43/:36; the vendor's long int i,k,n
 *    ride Int locals - no promotion loss, values are byte-sized or
 *    line lengths).
 *  - h012 unsigned_byte_domain: pd1 byte domain; k is the raw
 *    getc value (-1 EOF rides the k < 1 test).
 *  - h018 preprocessor_conditional: #ifdef NOTABS (memsize.h:43,
 *    DEFINED in the reference build) - the tab-to-blank rewrite is
 *    LIVE, translated; the not-NOTABS build is not referenced.
 *  - h015 grammar_collision : pre-push scan incl. cross-file
 *    top-level names: new top-level readf_ - no collisions.
 *  - h017 naming           : readf_ keeps the vendor name.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The loop never resets n: every LINEFEED writes a NUL at n++
 *    and accumulation continues - the whole file is consumed into
 *    pd1, NUL-separated, one readf_ call per file (vendor
 *    behavior kept).
 *  - The only return path is the EOF arm (return 1); there is no
 *    failure return - kept.
 *  - Bytes k with 1 <= k <= 200 survive; the k > 200 drop happens
 *    BEFORE the NOTABS rewrite and the CRTN/CTRLZ tests - order
 *    kept.
 */
private const val CRTN = '\r'      // vendor defines.h:197 ('\015')
private const val CTRLZ = 26.toChar() // vendor defines.h:198 ('\032')
private const val LINEFEED = '\n' // vendor defines.h:196 ('\012')

fun readf_(infile: java.io.Reader): Int {
    var n = 1

    var i = 1
    while (true) {
        i++
        val k = infile.read()

        if (k > 200) continue

        // #ifdef NOTABS (memsize.h:43 - defined in the reference build)
        var kc = k
        if (kc == '\t'.code) kc = ' '.code

        if (kc == CRTN.code || kc == CTRLZ.code) continue

        if (kc == LINEFEED.code) {
            CompilerState.pd1[n] = '\u0000'
            n++
            continue
        }

        if (kc < 1) {
            CompilerState.pd1[n] = '\u0000'
            CompilerState.pd1[n + 1] = '\u0000'
            CompilerState.pd1len = (n - 1).toShort()
            return 1
        }

        if (kc > 0) {
            CompilerState.pd1[n] = kc.toChar()
            n++
        }
        // the for-loop's i++ rides the while head (vendor: for (i = 1; 1; i++))
    }
}
