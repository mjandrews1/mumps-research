package doc

// Unit 86 (B3 tranche 3 / sub-push A): interp-bmgsubs.c::bmg_fsearch
// (batch 3, complex stratum; LOC 68, score 43.0). The
// Boyer-Moore-Gosper file search: reads BUFSIZE chunks from the fd,
// keeps the partial last line in linetemp, and per chunk runs the
// delta-table scan k += delta0[str[k]] to the chunk end, confirms
// candidate matches with the cmap/pattern equality loop, and hands
// confirmed lines to gotamatch (unit 17, the landed render; the
// action callback is its higher-order parameter, NULL sentinel -1
// preserved). The bmdata struct carriers (delta0/cmap/str/linetemp/
// pattern/patlen behind the vendor macros) render in
// BmgFsearchStatic per H-004, with the documented one-owner split:
// the struct member nsuccess already lives in the unit-17 BmgStatic
// owner object, and a Kotlin object cannot grow across files - the
// split is the H-004 adaptation ledgered here; bmg_setup (the delta
// table builder) is NOT a corpus unit: pattern/patlen arrive primed
// from out of scope, and bmg_search (the in-memory twin) is the
// batch-5 unit - neither is rendered here. The POSIX read(fd,...)
// is the entry's hard divergence: raw file descriptors do not exist
// in the JVM; fd 0 routes to the arm's STDIN_READER carrier (the
// getstr2 unit-48 precedent) with each read returning
// char-count-not-byte-count (H-012 byte-domain stand-in, chars for
// bytes, noted), and fd != 0 rides the H-014 Stubs.posixRead seat
// naming the POSIX read path as owner. Register-qualified pointer
// locals (k, strend, s, t) render as Int offsets into the str
// carrier; the unsigned char index domain of delta0/cmap narrows to
// the 0..255 code mask (H-012). Vendor quirks kept and noted: the
// cmap/pattern equality loop can run the j index BELOW zero in C
// (out-of-bounds pattern read, UB) - guarded here with the divergence
// noted (H-007 bounds-walk precedent); the scan pointer k can pass
// the str carrier end before the LARGE cutoff fires (the vendor
// trusts the delta table to land within str + LARGE); the
// str[count++] = NL "insurance for broken last line" is skipped for
// fd==0 exactly as the vendor has it; nleftover is UNINITIALIZED in
// the buffer-carrier domain on entry to the final strncpy (the
// vendor nleftover is set every iteration but the pre-loop state is
// used only if the read loop never runs - here 0-initialized).
// Naming per H-017.

private const val NL_B: Char = '\n'
private const val BUFSIZE_B = 50000
private const val PATSIZE_B = 9000
// vendor define LARGE = BUFSIZE + PATSIZE
private const val LARGE_B = BUFSIZE_B + PATSIZE_B

object BmgFsearchStatic {
    // interp-bmgsubs.c struct bmg_data behind the vendor macros
    // (H-004 owner; nsuccess stays in the unit-17 BmgStatic)
    val delta0 = IntArray(256)
    val cmap = CharArray(256)
    val str = CharArray(BUFSIZE_B + 2)
    val linetemp = CharArray(BUFSIZE_B)
    val pattern = CharArray(PATSIZE_B)
    var patlen: Int = 0
}

fun bmg_fsearch(fd: Int, action: ((CharArray, Int) -> Int)?): Int {
    val delta0 = BmgFsearchStatic.delta0
    val cmap = BmgFsearchStatic.cmap
    val str = BmgFsearchStatic.str
    val linetemp = BmgFsearchStatic.linetemp
    val pattern = BmgFsearchStatic.pattern
    val patlen = BmgFsearchStatic.patlen

    var nleftover = 0
    var fileOffset = 0
    BmgStatic.nsuccess = 0

    while (true) {
        var count = Stubs.posixRead(fd, str, nleftover, BUFSIZE_B - nleftover)
        if (count <= 0) break
        count += nleftover
        if (count != BUFSIZE_B && fd != 0) {
            str[count] = NL_B // insurance for broken last line
            count++
        }
        str[count] = NUL

        var j = count - 1
        while (j >= 0 && str[j] != NL_B) j--
        var strend: Int
        if (j < 0) { // break up long line
            str[count] = NL_B
            count++
            str[count] = NUL
            strend = count
            linetemp[0] = NUL
            nleftover = 0
        } else { // save partial line
            strend = j
            nleftover = count - j - 1
            // strncpy(linetemp, str + j + 1, nleftover)
            if (nleftover < linetemp.size) {
                var c = 0
                while (c < nleftover) {
                    linetemp[c] = str[j + 1 + c]
                    c++
                }
            }
        }
        var k = patlen - 1

        scan@ while (true) {
            // while ((k += delta0[*(unsigned char *) k]) < strend);
            while (true) {
                k += delta0[str[k].code and 0xFF]
                if (k >= strend) break
            }
            // if (k < (str + LARGE)) break;
            if (k < LARGE_B) break
            k -= LARGE_B

            var jj = patlen - 1
            var s = k - 1
            // while (cmap[*s--] == pattern[--j]); - the vendor first
            // compares pattern[patlen-2] (the --j predecrement skips
            // the last pattern byte, the "delta-less shortcut" quirk,
            // kept), and on a full run of equal bytes it keeps going
            // BELOW zero (pattern[-1] read, vendor UB): guarded here -
            // a full run of real equal comparisons takes the vendor
            // submatch path (H-007 bounds-walk divergence note).
            var allMatch = true
            while (jj > 0) {
                val pc = pattern[jj - 1]
                val sc = cmap[str[s].code and 0xFF]
                jj--
                s--
                if (sc != pc) {
                    allMatch = false
                    break
                }
            }
            if (!allMatch) {
                k++
            } else { // submatch
                val t = k
                var sidx = k - patlen + 1
                // do ; while (*s != NL && --s >= str);
                while (true) {
                    if (str[sidx] == NL_B) break
                    if (sidx - 1 < 0) {
                        sidx = -1
                        break
                    }
                    sidx--
                }
                k = sidx + 1 // at line start

                val gi = gotamatch(str, k, fileOffset + (t), action)
                if (gi == -1) return -1
                k = gi
                if (k >= strend) break@scan
            }
        }
        fileOffset += 1 + strend
        // strncpy(str, linetemp, nleftover)
        if (nleftover in 0..linetemp.size && nleftover <= str.size) {
            var c = 0
            while (c < nleftover) {
                str[c] = linetemp[c]
                c++
            }
        }
    }
    return BmgStatic.nsuccess
}
