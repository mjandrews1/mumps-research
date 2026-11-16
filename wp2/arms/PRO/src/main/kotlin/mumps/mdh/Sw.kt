package mumps.mdh

import mumps.interp.NUL
import mumps.interp.cstringAt
import mumps.interp.strlenC
import mumps.interp.strcpyC
import java.io.Writer
import kotlin.io.print

/**
 * PRO-arm translation of libmpscpp.cpp::sw (pilot B2, unit 61,
 * provisional under deviation C-003). Translated independently from
 * the pristine vendor source (mumps-14-may-2026.tgz, sha256
 * 621deb25...ffdbe), reference source lines 8153-8350 (units.csv:
 * 148 LOC, score 173.2, rank 287 - the char* overload; the
 * mstring/mstring and string/string forwarders at lines 8145/8149
 * are non-corpus wrappers served by this single Kotlin signature,
 * the c_str() reads being the CharArray form here).
 *
 * The Smith-Waterman alignment driver: fills the score matrix via
 * Cost, locates the maximum cell, traces the alignment back via
 * prnt (unit 45), scores the result, and optionally prints the
 * matrix and the 60-column alignment display to the current output
 * unit.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h001 Writer            : fprintf(svPtr->out_file[svPtr->io],
 *    ...) maps to outWrite (the Msv out_file/io carriers); the C
 *    %d/%s/%c conversions render via String.format; cout << ... <<
 *    endl maps to kotlin.io.print.
 *  - h004 globals_ext       : Size/Size_s/Size_t/sx (libmpscpp.cpp:
 *    8033-8039) are added to the AlignState singleton (GAP/MISMATCH/
 *    MATCH/S_RESULT/__a already there from unit 45).
 *  - h007 char_buffer        : the t1[80]/t2[80] staging buffers and
 *    the reverse-copy m1/m2 buffers render as CharArrays through a
 *    local strncpC helper (copy up to n bytes, stop at NUL; the
 *    vendor's stale-tail bytes are never read past the explicit
 *    terminator and render zero-filled, noted).
 *  - h013 dead_branches     : the ShowAlign && 0 banner block is
 *    always false and dropped with this note; the malloc-NULL
 *    raise(SIGSEGV) arms are unreachable on the JVM (allocation
 *    throws OutOfMemoryError) and dropped with this note; free()
 *    is a GC no-op; the unused locals (x, n) are dropped.
 *  - h014 out_of_scope_call : Cost (batch-3 unit), init (batch-4
 *    unit, owns the AlignState.a allocation) and nodash (not a
 *    corpus unit) become owner-named MdhStubs stubs; prnt is the
 *    real unit-45 translation; Score rides the existing stub.
 *  - h017 naming            : sw keeps its name.
 *
 * Vendor notes (translated faithfully, not fixed):
 *  - the score-only block runs once with i = 0 (the vestigial loop
 *    variable), so Maxscorex is 0 or -1 exactly as there;
 *  - the vendor reads s[i]/t[j] with NUL termination;
 *  - in the 60-column display a short t2 line compares against NUL
 *    bytes (the CharArray 0 codes fall to the mismatch arm), as in
 *    C;
 *  - imax/jmax stay zero until a matrix cell beats top=0, and
 *    top==0 returns before any use of them.
 */

private const val EMPTY_CELL: Short = -32768 // vendor libmpscpp.cpp:8029

/** fprintf(svPtr->out_file[svPtr->io], ...) carrier (h001). */
private fun outWrite(s: String) {
    val w: Writer = MdhState.svPtr.out_file[MdhState.svPtr.io]
        ?: throw IllegalStateException("out_file[io] not open") // h001 adaptation
    w.write(s)
}

/** strncpy(dst, src+off, n) - copy up to n chars, stop at NUL. */
private fun strncpC(dst: CharArray, src: CharArray, off: Int, n: Int) {
    var k = 0
    while (k < n && off + k < src.size && src[off + k] != NUL) {
        dst[k] = src[off + k]
        k++
    }
    while (k < n) { // vendor tail stays stale; never read past the
        dst[k] = NUL // explicit terminator - rendered zero-filled
        k++
    }
}

fun sw(
    s: CharArray,
    t: CharArray,
    ShowAlign: Int,
    ShowMatrix: Int,
    gap: Int,
    mismatch: Int,
    match: Int,
): Int {

    var Maxscore = 0
    var Maxscorex = -1
    var score: Int
    var top: Int
    var imax = 0
    var jmax = 0

    AlignState.gap = gap
    AlignState.mismatch = mismatch
    AlignState.match = match

    AlignState.Size = strlenC(s)
    AlignState.Size_s = strlenC(s)
    AlignState.Size_t = strlenC(t)

    if (AlignState.Size_t > AlignState.Size) AlignState.Size = AlignState.Size_t

    AlignState.Size += 2
    AlignState.sx = 0
    init() // h014 stub (batch-4 owner; allocates AlignState.a)

    // h013: the vendor's ShowAlign && 0 banner block (60-column
    // dump of s and t) is always false and is dropped.

    Cost(s, t, AlignState.Size_s - 1, AlignState.Size_t - 1) // h014 stub (batch-3 owner)

    if (ShowMatrix != 0) {
        top = 0
        outWrite("      ")

        for (i in 0 until strlenC(t)) outWrite("%5d".format(i))

        outWrite("\n      ")

        for (i in 0 until strlenC(t)) outWrite("    %c".format(t[i]))

        outWrite("\n")

        for (i in 0 until strlenC(s)) {
            outWrite("%3d %c ".format(i, s[i]))

            for (j in 0 until strlenC(t)) {
                if (AlignState.a[i][j] != EMPTY_CELL) outWrite("%5d".format(AlignState.a[i][j]))
                else outWrite("   ")

                if (AlignState.a[i][j] > top) {
                    top = AlignState.a[i][j].toInt()
                    imax = i
                    jmax = j
                }
            }

            outWrite("\n")
        }
    } else {
        top = 0

        for (i in 0 until strlenC(s)) {
            for (j in 0 until strlenC(t)) {
                if (AlignState.a[i][j] > top) {
                    top = AlignState.a[i][j].toInt()
                    imax = i
                    jmax = j
                }
            }
        }
    }

    if (top == 0) {
        if (ShowAlign != 0) print("No matches\n")
        return 0
    }

    prnt(0, s, t, imax, jmax) // real unit-45 translation
    Maxscorex = -1
    score = 0
    var i = 0
    run {
        // no show alignments - score only
        val max = AlignState.sResult.length - 1

        var k = 0
        while (k < max) {
            if (AlignState.sResult[k] == AlignState.sResult[k + 1]) score += AlignState.match
            else if (AlignState.sResult[k] == '-' || AlignState.sResult[k + 1] == '-') score += AlignState.gap
            else score += AlignState.mismatch
            k += 2
        }

        if (score > Maxscore) {
            Maxscore = score
            Maxscorex = i
        }
    }

    // --------------------------------------------------
    score = 0
    i = Maxscorex

    if (Maxscorex < 0) {
        print("No alignments\n")
    } else if (ShowAlign != 0) {
        var m = 1
        m = AlignState.sResult.length
        var k = m
        m /= 2
        k = m

        // vendor: malloc(2*m+1); the NULL/raise(SIGSEGV) arms are
        // unreachable on the JVM (h013, allocation throws naturally).
        val m1 = CharArray(2 * m + 1)
        val m2 = CharArray(2 * m + 1)

        m1[k] = NUL
        k--
        m2[m] = NUL
        m--

        var j = 0 // reverse copy
        while (j < AlignState.sResult.length) {
            m1[k] = AlignState.sResult[j]
            k--
            m2[m] = AlignState.sResult[j + 1]
            m--
            j += 2
        }

        // display alignments and calculate score

        score = 0
        val m1Length = strlenC(m1)
        val m2Length = strlenC(m2)
        var m1x = imax - nodash(m1) + 1 // h014 stub
        var m2x = jmax - nodash(m2) + 1 // h014 stub

        var al = 0
        while (al < m1Length) {
            val t1 = CharArray(80)
            val t2 = CharArray(80)

            if (al >= m2Length) strcpyC(t2, " ")
            else strncpC(t2, m2, al, 60)

            strncpC(t1, m1, al, 60)
            t1[60] = NUL // 0 to 59
            t2[60] = NUL // 0 to 59

            outWrite("%5d %s %d\n".format(m1x, cstringAt(t1), m1x + nodash(t1)))
            m1x = m1x + nodash(t1) + 1
            outWrite("      ")

            var q = 0
            while (t1[q] != NUL) {
                if (t1[q] == t2[q] && t1[q] != '-') {
                    outWrite(":")
                    score += AlignState.match
                } else if (t1[q] == '-' || t2[q] == '-') {
                    score += AlignState.gap
                    outWrite(" ")
                } else {
                    score += AlignState.mismatch
                    outWrite(" ")
                }
                q++
            }

            outWrite("\n")
            outWrite("%5d %s %d\n\n".format(m2x, cstringAt(t2), m2x + nodash(t2)))
            m2x = m2x + nodash(t2) + 1

            al += 60
        }

        // free(m1); free(m2); - GC no-ops (h013).
    }

    // --------------------------------------------------
    // vendor: for (i=0; i<Size_s; i++) free(__a[i]); free(__a);
    // - GC no-ops (h013).

    return Maxscore
}
