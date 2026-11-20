package doc

// Unit 148 (B5 tranche 3): interp-bmgsubs.c::bmg_search (batch 5,
// complex stratum; units.csv row 148: loc 60, score 47.0, rank
// 233). The in-memory sibling of the unit-86 bmg_fsearch: the
// caller-supplied buffer (not a file read) is forced to end on
// a NL byte, walked in BUFSIZE chunks trimmed back to the last
// full line, and scanned with the shared Boyer/Moore/Gosper
// delta0 table; candidates confirmed by the cmap/pattern loop
// hand their line to the unit-17 gotamatch render (the action
// callback rides the same nullable higher-order shape as unit
// 86; the vendor NULL return maps to the -1 sentinel).
//
// H-004 one-owner split identical to unit 86: the bmdata struct
// members delta0/cmap/pattern/patlen already live in the
// BmgFsearchStatic owner (uploaded by bmg_setup from out of
// scope), and the nsuccess counter lives in the unit-17 BmgStatic
// owner; this render binds those carriers (no redeclaration).
// The pointer locals k/strend/s/t/strbegin render as Int offsets
// into the buffer (H-007); the unsigned char index domain of
// delta0 narrows to the 0..255 code mask (H-012). Vendor quirks
// kept and noted: byte_offset is a DEAD store (never read -
// dropped per H-013 with this note); the pre-scan
// while(cmap[*s--]==pattern[--j]) confirm loop can run j BELOW
// zero and s out of the region in C (UB) - guarded here with the
// divergence ledgered (u86 precedent); the k += delta0[...] scan
// can step k past the buffer end before the strend bound catches
// it (the vendor trusts the delta table) - the index read is
// clamped to the NUL analog at/after the buffer end, the loop
// still exits on the strend bound exactly as written; the
// do-while line-start walk can decrement s below strbegin (the
// leading line can start before the chunk) - guarded, same
// quantization; the last-byte restore writes back only when the
// forced NL overwrote a non-NL, exactly as written. gotamatch
// returns the -1 NULL sentinel: the infile return -1 short-arm
// is kept. printf: none in this body. Naming per H-017
// (bmg_search kept snake per vendor-name fidelity).

private const val NL_BS: Char = '\n'

fun bmg_search(
    buffer: CharArray,
    buflen: Int,
    action: ((CharArray, Int) -> Int)?
): Int {
    var k: Int
    var strend: Int
    var s: Int
    var j: Int
    var t: Int
    var strbegin: Int

    // char last_byte - the forced-NL rollback token.
    val lastByte = buffer[buflen - 1]

    BmgStatic.nsuccess = 0

    // make sure that buffer ends with NL
    if (lastByte != NL_BS)
        buffer[buflen - 1] = NL_BS

    // We must process the buffer in chunks of BUFSIZE to make
    // "LARGE" hack work right.
    strbegin = 0
    while (strbegin < buflen) {
        strend = strbegin + BUFSIZE_BS

        if (strend > buflen)  // stay inside buffer
            strend = buflen

        // find end of last full line in this chunk of the buffer
        s = strbegin
        while (buffer[strend - 1] != NL_BS && strend > s)
            strend--
        if (strend <= strbegin)  // line longer than BUFSIZE, punt
        {
            strbegin += BUFSIZE_BS
            continue
        }

        k = strbegin + BmgFsearchStatic.patlen - 1

        while (true) {
            // the hot loop: k rides the delta0 table to strend.
            while (true) {
                val code = bsRead(buffer, k)
                k += BmgFsearchStatic.delta0[code.code and 0xff]
                if (k >= strend) break
            }
            if (k < (strbegin + LARGE_BS)) break
            k -= LARGE_BS

            j = BmgFsearchStatic.patlen - 1
            s = k - 1
            // while (cmap[*s--] == pattern[--j]); - the guards are
            // the quantization of the vendor's below-zero UB; the
            // compare order (cmap read, s--, pattern read, test)
            // is the vendor order.
            while (s >= 0 && j > 0) {
                val cs = if (s < buffer.size)
                    BmgFsearchStatic.cmap[buffer[s].code and 0xff] else NUL
                val pj = BmgFsearchStatic.pattern[--j]
                s--
                if (cs != pj) break
            }
            // delta-less shortcut for literati, but
            // short shrift for genetic engineers.
            if (j >= 0) k++
            else {  // submatch
                t = k
                s = k - BmgFsearchStatic.patlen + 1
                do {
                    // do ; while (*s != NL && --s >= strbegin);
                } while (bsRead(buffer, s) != NL_BS && --s >= strbegin)
                k = s + 1  // at line start

                val g = gotamatch(buffer, k, (t - 0), action)
                if (g == -1) return -1
                k = g
                if (k >= strend) break
            }
        }

        strbegin += BUFSIZE_BS
    }

    if (buffer[buflen - 1] != lastByte)
        buffer[buflen - 1] = lastByte
    return BmgStatic.nsuccess
}

// interp-bmgsubs.c:75 BUFSIZE (file-local twin of the u86 render)
internal const val BUFSIZE_BS = 50000
// vendor define LARGE = BUFSIZE + PATSIZE
internal const val LARGE_BS = 50000 + 9000

// Clamped buffer read: the C pointer walk reads through the NUL
// boundary into UB when the delta table steps past the buffer
// end; the render reads the NUL analog for out-of-range offsets
// (deterministic quantization, header note).
private fun bsRead(buffer: CharArray, i: Int): Char =
    if (i in buffer.indices) buffer[i] else NUL
