package mumps.interp

/**
 * PRO-arm translation of interp-bmgsubs.c::bmg_fsearch (B3 unit 70,
 * rank 228). Translated independently from the pristine vendor
 * source (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), reference
 * source lines 117-198 (K&R-style definition at line 117).
 *
 * Identity verified against the study tool (tools/select_study_
 * subset.py): interp-bmgsubs.c::bmg_fsearch, c, loc 68,
 * comment_ratio 0.105, pointer_marks 8, control_marks 16, score 43.0,
 * batch 3 - exact match to wp1/study-subset/units.csv row 228.
 *
 * bmg_fsearch(fd, action): the Boyer-Moore-Gosper search driver -
 * streams the file behind fd through the bmdata window (str), finds
 * pattern occurrences with the delta0/cmap tables, and reports each
 * hit line through gotamatch (unit 17) with the caller's action
 * callback; returns nsuccess (-1 if the action declines).
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h001 io_stream_param     : the vendor's integer fd becomes a
 *    Reader carrier; the fd != 0 (stdin) test rides an isStdin flag
 *    (the carrier cannot be fstat'd - cf. DirectRead's adaptation).
 *  - h002 io_primitives      : read(fd, str+nleftover, ...) ->
 *    Reader.read(str, nleftover, BUFSIZE-nleftover); -1 (EOF) maps
 *    to the vendor's 0 count and ends the loop.
 *  - h004 globals_ext        : str, linetemp, delta0, cmap, pattern,
 *    patlen (#define aliases of bmdata members, interp-bmgsubs.c:
 *    84-99) -> the BmgData carrier (Gotamatch.kt; members added by
 *    this unit, struct order preserved).
 *  - h007 static_scratch     : the persistent window/linetemp state
 *    rides the BmgData carrier (single object).
 *  - h011 goto               : the for(;;) scan loop's breaks are
 *    direct; the k-advance while is a do/while.
 *  - h012 unsigned_byte_domain: str/linetemp/pattern byte domains;
 *    cmap/delta0 index by Char.code (ASCII tables, vendor comment
 *    "ascii only").
 *  - h013 dead_store         : none - every local is consumed.
 *  - h014 out_of_scope_call  : gotamatch (unit 17, translated -
 *    delegates); bmg_setup (interp-bmgsubs.c::bmg_setup, not a
 *    corpus unit) owns the table contents - no stub needed, the
 *    carrier starts zeroed like the vendor's static struct.
 *  - h015 grammar_collision : pre-push scan incl. cross-file
 *    top-level names: new top-level bmg_fsearch - no collisions.
 *  - h017 naming             : bmg_fsearch keeps the vendor name;
 *    pointer locals (k, s, t, strend) become Int offsets into
 *    BmgData.str (pointer-arithmetic convention, cf. gotamatch).
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The insurance NL (broken last line) is skipped for fd==0
 *    (stdin) - kept via the isStdin flag.
 *  - The last-NL scan "for (j = count-1; str[j] != NL && j >= 0;
 *    j--)" tests str[j] BEFORE the j>=0 bound (str[-1] is an OOB
 *    read at j==0 in the vendor); the port swaps the tests to
 *    bound-safe order - same outcomes for all in-bounds inputs.
 *  - The pattern compare "while (cmap[*s--] == pattern[--j]);"
 *    indexes pattern[-1] when the full pattern matches (vendor OOB
 *    read of the preceding struct byte); the port stops the
 *    compare at j<0, which leaves j==-1 - the same exit state the
 *    vendor reaches when that OOB byte mismatches (the typical
 *    case; the submatch arm is taken either way).
 *  - delta0/cmap/pattern/patlen contents are owned by bmg_setup
 *    (not a corpus unit) and by bmg_search (batch-5 unit) - this
 *    unit only consumes them.
 *  - The vendor comment blocks inside the scan loop (the "we beat
 *    existing microcode" and "delta-less shortcut for literati"
 *    notes) are preserved below.
 */

// interp-bmgsubs.c:75/78/80/81/82 (same values as the file-private
// copies in Gotamatch.kt; restated here - BUFSIZE/PATSIZE/LARGE/NL/EOS).
private const val BUFSIZE = 50000
private const val PATSIZE = 9000
private const val LARGE = BUFSIZE + PATSIZE // :80
private const val NL = '\n'                 // :81
private const val EOS = '\u0000'            // :82

fun bmg_fsearch(fd: java.io.Reader, isStdin: Boolean,
               action: ((String, Int) -> Boolean)?): Int {
    val str = BmgData.str
    val linetemp = BmgData.linetemp
    val delta0 = BmgData.delta0
    val cmap = BmgData.cmap
    val pattern = BmgData.pattern

    var k: Int
    var strend: Int
    var j: Int
    var s: Int
    var t: Int
    var nleftover = 0
    var count: Int
    var file_offset = 0

    BmgData.nsuccess = 0

    outer@ while (true) {
        val nread = fd.read(str, nleftover, BUFSIZE - nleftover)
        if (nread <= 0) break // read() == 0 (or Reader EOF -1) ends the loop
        count = nread + nleftover
        if (count != BUFSIZE && !isStdin) { // fd != 0
            str[count] = NL // insurance for broken last line
            count++
        }
        str[count] = EOS
        // for (j = count - 1; str[j] != NL && j >= 0;) j--;
        j = count - 1
        while (j >= 0 && str[j] != NL) j--
        if (j < 0) { // break up long line
            str[count] = NL
            count++
            str[count] = EOS
            strend = count
            linetemp[0] = EOS
            nleftover = 0
        } else { // save partial line
            strend = j
            nleftover = count - j - 1
            for (c in 0 until nleftover) linetemp[c] = str[j + 1 + c] // strncpy
        }
        k = BmgData.patlen - 1

        scan@ while (true) {
            // for a large class of patterns, upwards of 80% of
            // match time is spent on the next line.
            // we beat existing microcode (vax 'matchc') this way.
            do {
                k += delta0[str[k].code]
            } while (k < strend)
            if (k < LARGE) break
            k -= LARGE

            j = BmgData.patlen - 1
            s = k - 1
            // delta-less shortcut for literati, but
            // short shrift for genetic engineers.
            // vendor: while (cmap[*s--] == pattern[--j]);
            while (true) {
                j--
                if (j < 0) break // vendor OOB pattern[-1]; port stops here
                if (cmap[str[s].code] != pattern[j]) break
                s--
            }
            if (j >= 0) {
                k++
            } else { // submatch
                t = k
                s = k - BmgData.patlen + 1
                // do ; while (*s != NL && --s >= str);
                while (str[s] != NL) {
                    s--
                    if (s < 0) break
                }
                k = s + 1 // at line start

                val gk = gotamatch(str, k, file_offset + t, action)
                if (gk == -1) return -1 // NULL
                k = gk
                if (k >= strend) break@scan
            }
        }

        file_offset += 1 + strend
        for (c in 0 until nleftover) str[c] = linetemp[c] // strncpy
    }
    return BmgData.nsuccess
}
