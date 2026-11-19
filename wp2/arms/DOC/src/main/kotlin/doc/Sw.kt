package doc

import java.util.Locale

// Unit 61: libmpscpp.cpp::sw (batch 2, complex stratum; row 34:
// loc 148, ptr 54, ctrl 32, score 173.2, rank 287). The Smith-
// Waterman entry point: primes the penalties/sizes, fills the cost
// matrix, optionally prints the matrix, locates the max cell, runs
// the traceback (prnt of unit 45), scores the result, and prints
// the alignment blocks. Adaptations per cookbook: fprintf(svPtr-
// >out_file[svPtr->io],...) rides the Msv out_file Writers on
// Globals.svPtr via the outw helper (a closed lun throws - the
// vendor would dereference a closed FILE*, documented divergence);
// printf formats collapse to String.format Locale.US per H-009;
// malloc/free ride CharArray (with the out-of-memory raise(SIGSEGV)
// rendered as a thrown Error - the vendor aborts, the JVM throw
// is the observed analogue); the trailing free loop of the matrix
// is a JVM GC no-op kept as a comment ledger; the ShowAlign && 0
// header banner block is vendor-dead (H-013) and kept as comments.
// cout maps to System.out per H-001. init/Cost/nodash are H-014
// stubs (not roster units); prnt (unit 45) and Score (stub),
// EMPTY_CELL/__a/S_RESULT/GAP/MISMATCH/MATCH/Size_s/Size_t/Size/sx
// are the SeqAlignExterns seats. Vendor quirks kept: the i=0/i=
// Maxscorex ghost assignments around the score-only block; the
// buffer sizing at half the result length (2*(len/2)+1); the
// reverse-copy index walks; m1x/m2x base computations via nodash.

private fun outw(): java.io.Writer =
    Globals.svPtr.out_file[Globals.svPtr.io]
        ?: throw MumpsGlobalException("sw: out_file[io] not open")

// strncpy(t1, src+al, 60); t1[60]=0 - the 60-char window slice.
private fun slice60(src: CharArray, al: Int): CharArray {
    val out = CharArray(80)
    var i = 0
    while (i < 60 && al + i < src.size && src[al + i] != NUL) {
        out[i] = src[al + i]
        i++
    }
    out[60] = NUL
    return out
}

fun sw(
    s: CharArray, t: CharArray, ShowAlign: Boolean, ShowMatrix: Boolean,
    gap: Int, mismatch: Int, match: Int
): Int {
    var Maxscore = 0
    var Maxscorex = -1
    var score: Int
    var top: Int
    var imax = 0
    var jmax = 0

    GAP = gap
    MISMATCH = mismatch
    MATCH = match

    Size_s = CstringOps.strlenC(s)
    Size = Size_s
    Size_t = CstringOps.strlenC(t)

    if (Size_t > Size) Size = Size_t

    Size += 2
    sx = 0
    Stubs.init()

    // VENDOR DEAD BLOCK (ShowAlign && 0, H-013; NOT translated): the
    // "S-W Alignments for:" banner and the two 60-char window dumps
    // of s and t with the "%6d %s" ruler.

    Stubs.Cost(s, t, Size_s - 1, Size_t - 1)

    if (ShowMatrix) {
        top = 0
        outw().write("      ")

        var i = 0
        while (t[i] != NUL) {
            outw().write(String.format(Locale.US, "%5d", i))
            i++
        }

        outw().write("\n      ")

        i = 0
        while (t[i] != NUL) {
            outw().write(String.format(Locale.US, "    %c", t[i]))
            i++
        }

        outw().write("\n")

        i = 0
        while (s[i] != NUL) {
            outw().write(String.format(Locale.US, "%3d %c ", i, s[i]))

            var j = 0
            while (t[j] != NUL) {
                val aa = a ?: throw IllegalStateException("sw: matrix __a not initialized")
                if (aa[i][j] != EMPTY_CELL) outw().write(String.format(Locale.US, "%5d", aa[i][j]))
                else outw().write("   ")

                if (aa[i][j] > top) {
                    top = aa[i][j]
                    imax = i
                    jmax = j
                }
                j++
            }

            outw().write("\n")
            i++
        }
    } else {
        top = 0

        var i = 0
        while (s[i] != NUL) {
            var j = 0
            while (t[j] != NUL) {
                val aa = a ?: throw IllegalStateException("sw: matrix __a not initialized")
                if (aa[i][j] > top) {
                    top = aa[i][j]
                    imax = i
                    jmax = j
                }
                j++
            }
            i++
        }
    }

    if (top == 0) {
        if (ShowAlign) System.out.println("No matches")

        return 0
    }

    prnt(0, s, t, imax, jmax)
    Maxscorex = -1
    score = 0
    run {
        // vendor ghost: i stays 0 in this block
        val i = 0
        val max = S_RESULT.length - 1

        var k = 0
        while (k < max) {
            score = if (S_RESULT[k] == S_RESULT[k + 1]) score + MATCH
            else if (S_RESULT[k] == charDash() || S_RESULT[k + 1] == charDash()) score + GAP
            else score + MISMATCH
            k += 2
        }

        if (score > Maxscore) {
            Maxscore = score
            Maxscorex = i
        }
    }

//--------------------------------------------------
    score = 0
    // vendor: i = Maxscorex (unused below; kept as the ghost ledger)

    if (Maxscorex < 0) {
        System.out.println("No alignments")
    } else if (ShowAlign) {
        var m = 1
        m = S_RESULT.length
        var k = m
        m /= 2
        k = m // vendor: k = m = m/2

        // VENDOR QUIRK: the buffers are sized on the halved length
        // (2*(len/2)+1) - kept. The malloc failure arms print and
        // raise(SIGSEGV); rendered as thrown Errors.
        val m1 = CharArray(2 * m + 1)
        val m2 = CharArray(2 * m + 1)
        if (m1.size != 2 * m + 1 || m2.size != 2 * m + 1) {
            System.out.println("Out of memory")
            throw Error("SIGSEGV")
        }

        m1[k] = NUL; k--
        m2[m] = NUL; m--

        var j = 0
        while (j < S_RESULT.length) { // reverse copy
            m1[k] = S_RESULT[j]; k--
            m2[m] = S_RESULT[j + 1]; m--
            j += 2
        }

        // display alignments and calculate score

        score = 0
        val m1Length = CstringOps.strlenC(m1)
        val m2Length = CstringOps.strlenC(m2)
        var m1x = imax - Stubs.nodash(m1) + 1
        var m2x = jmax - Stubs.nodash(m2) + 1

        var al = 0
        while (al < m1Length) {
            val t1: CharArray
            val t2: CharArray

            if (al >= m2Length) {
                t2 = CharArray(80)
                t2[0] = ' '
                t2[1] = NUL
            } else {
                t2 = slice60(m2, al)
            }

            t1 = slice60(m1, al)

            outw().write(String.format(Locale.US, "%5d %s %d\n", m1x, CstringOps.cstring(t1), m1x + Stubs.nodash(t1)))
            m1x = m1x + Stubs.nodash(t1) + 1
            outw().write("      ")

            var j2 = 0
            while (t1[j2] != NUL) {
                if (t1[j2] == t2[j2] && t1[j2] != charDash()) {
                    outw().write(":")
                    score += MATCH
                } else if (t1[j2] == charDash() || t2[j2] == charDash()) {
                    score += GAP
                    outw().write(" ")
                } else {
                    score += MISMATCH
                    outw().write(" ")
                }
                j2++
            }

            outw().write("\n")
            outw().write(String.format(Locale.US, "%5d %s %d\n\n", m2x, CstringOps.cstring(t2), m2x + Stubs.nodash(t2)))
            m2x = m2x + Stubs.nodash(t2) + 1

            al += 60
        }

        // free(m1)/free(m2): JVM GC no-op
    }

//--------------------------------------------------
    // VENDOR TAIL: for (i=0; i<Size_s; i++) free(__a[i]); free(__a);
    // rendered as a GC no-op ledger (the matrix __a is the
    // SeqAlignExterns seat owned by the alignment initializers).

    return Maxscore
}
