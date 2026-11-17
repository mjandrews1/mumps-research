package mumps.interp

/**
 * PRO-arm translation of interp-bmgsubs.c::bmg_search (B5 unit 132,
 * rank 233). Translated independently from the pristine vendor
 * source (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), reference
 * source lines 134-216 (K&R-style definition at line 134).
 *
 * Identity verified against the study tool (tools/select_study_
 * subset.py): interp-bmgsubs.c::bmg_search, c, loc 60,
 * comment_ratio 0.189, pointer_marks 10, control_marks 17, score
 * 47.0 - exact match to wp1/study-subset/units.csv row 131.
 *
 * bmg_search(buffer, buflen, action): the Boyer-Moore-Gosper
 * buffer search - the same delta0/cmap scan as bmg_fsearch (unit
 * 70) but over a caller-owned buffer, chunked by BUFSIZE so the
 * LARGE hack works; each hit line goes through gotamatch (unit
 * 17) with the caller's action callback; returns nsuccess (-1 if
 * the action declines).
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h004 globals_ext        : delta0/cmap/pattern/patlen/nsuccess
 *    (#define aliases of the bmdata members, interp-bmgsubs.c:
 *    84-99) -> the BmgData carrier (Gotamatch.kt); this unit is
 *    the second consumer (fsearch was first); bmg_setup (not a
 *    corpus unit) still owns the table contents.
 *  - h011 goto               : the K&R for(;;) scan loop's breaks
 *    are direct; the k-advance is the do/while; the chunk loop's
 *    punt is a continue.
 *  - h012 unsigned_byte_domain: buffer/pattern byte domains;
 *    cmap/delta0 index by Char.code (ASCII tables, vendor
 *    comment "ascii only").
 *  - h013 dead_store         : "int byte_offset = 0;" (vendor 142)
 *    is never read - dropped with a comment; the register
 *    declaration zero-inits (k/strend/s/j) are C declaration
 *    artifacts - the Kotlin locals are assigned before use.
 *  - h014 out_of_scope_call  : gotamatch (unit 17, translated -
 *    delegates); no stub raised or removed - this is the first
 *    bmg_search seat (bmg_fullsearch's MdhStubs stub stays - its
 *    owner is libmpscpp.cpp, a different file).
 *  - h015 grammar_collision : pre-push scan incl. cross-file
 *    top-level names: new top-level bmg_search - no collisions
 *    (BmgFsearch/Gotamatch carry the neighbors).
 *  - h017 naming          : bmg_search keeps the vendor name; the
 *    pointer locals (k, strend, s, t, strbegin) become Int
 *    offsets into buffer (pointer-arithmetic convention, cf.
 *    gotamatch/bmg_fsearch); the action callback keeps the
 *    unit-17 shape ((String, Int) -> Boolean?, null = print).
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The insurance NL: a buffer not ending in NL gets its last
 *    byte CLOBBERED to NL (vendor 143-146) and restored from
 *    last_byte at the exit (vendor 213-214) - kept verbatim: the
 *    caller's final byte is temporarily destroyed, an observable
 *    side effect on the input buffer.
 *  - The chunk-end walk-back "while ((strend[-1]) != NL &&
 *    (strend > s)) strend--;" tests strend[-1] BEFORE the bound;
 *    the port swaps to bound-safe order - same outcomes for all
 *    in-bounds inputs (u70 precedent note).
 *  - The pattern compare "while (cmap[*s--] == pattern[--j]);"
 *    indexes pattern[-1] when the full pattern matches (vendor
 *    OOB read); the port stops at j<0 leaving j==-1 - the same
 *    exit state the vendor reaches when the OOB byte mismatches
 *    (u70 precedent).
 *  - The line-start walk-back "do ; while (*s != NL && --s >=
 *    strbegin);" - the port keeps the do-shape: the buffer byte is
 *    tested, then s decrements with the strbegin bound.
 *  - The vendor comment blocks inside the scan loop (the "we
 *    beat existing microcode" and "delta-less shortcut for
 *    literati" notes) are the same as fsearch's - preserved in
 *    bmg_fsearch; the anchors here stay in the code below.
 */

// interp-bmgsubs.c:75/80/81 (same values as the file-private copies
// in Gotamatch.kt/BmgFsearch.kt; restated here - BUFSIZE/LARGE/NL).
private const val BUFSIZE = 50000
private const val LARGE = BUFSIZE + 9000 // :80 (BUFSIZE + PATSIZE)
private const val NL = '\n'              // :81

/** vendor 134-216: int bmg_search(char *buffer, int buflen,
 *  int (*action)()). */
fun bmg_search(buffer: CharArray, buflen: Int, action: ((String, Int) -> Boolean)?): Int {
    val delta0 = BmgData.delta0
    val cmap = BmgData.cmap
    val pattern = BmgData.pattern

    var k: Int
    var strend: Int
    var j: Int
    var s: Int
    var t: Int
    // h013: vendor 142 - int byte_offset = 0; never read - dropped.

    val lastByte = buffer[buflen - 1] // char last_byte (vendor 141)

    BmgData.nsuccess = 0

    // make sure that buffer ends with NL
    if (lastByte != NL) buffer[buflen - 1] = NL

    // We must process the buffer in chunks of BUFSIZE to make
    // "LARGE" hack work right.
    var strbegin = 0
    while (strbegin < buflen) { // for (strbegin = buffer; strbegin < &buffer[buflen]; strbegin += BUFSIZE)
        strend = strbegin + BUFSIZE

        if (strend > buflen) strend = buflen // stay inside buffer

        // find end of last full line in this chunk of the buffer
        s = strbegin
        while (buffer[strend - 1] != NL && strend > s) strend--

        if (strend <= strbegin) { // line longer than BUFSIZE, punt
            strbegin += BUFSIZE
            continue
        }

        k = strbegin + BmgData.patlen - 1

        scan@ while (true) {
            // for a large class of patterns, upwards of 80% of
            // match time is spent on the next line.
            // we beat existing microcode (vax 'matchc') this way.
            do {
                k += delta0[buffer[k].code]
            } while (k < strend)
            if (k < strbegin + LARGE) break
            k -= LARGE

            j = BmgData.patlen - 1
            s = k - 1
            // delta-less shortcut for literati, but
            // short shrift for genetic engineers.
            // vendor: while (cmap[*s--] == pattern[--j]);
            while (true) {
                j--
                if (j < 0) break // vendor OOB pattern[-1]; port stops here
                if (cmap[buffer[s].code] != pattern[j]) break
                s--
            }
            if (j >= 0) {
                k++
            } else { // submatch
                t = k
                s = k - BmgData.patlen + 1
                // do ; while (*s != NL && --s >= strbegin);
                while (buffer[s] != NL) {
                    s--
                    if (s < strbegin) break
                }
                k = s + 1 // at line start

                val gk = gotamatch(buffer, k, t, action)
                if (gk == -1) return -1 // NULL
                k = gk
                if (k >= strend) break@scan
            }
        }

        strbegin += BUFSIZE
    }

    if (buffer[buflen - 1] != lastByte) buffer[buflen - 1] = lastByte
    return BmgData.nsuccess
}
