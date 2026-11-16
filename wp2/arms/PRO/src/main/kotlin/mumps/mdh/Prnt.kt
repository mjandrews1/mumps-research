package mumps.mdh

import mumps.interp.NUL
import mumps.interp.cstringAt

/**
 * PRO-arm translation of libmpscpp.cpp::prnt (pilot B2, unit 45,
 * provisional under deviation C-003). Translated independently from
 * the pristine vendor source (mumps-14-may-2026.tgz, sha256
 * 621deb25...ffdbe), reference source lines 8057-8098 (units.csv:
 * 34 LOC, score 16.6, rank 151; single definition).
 *
 * The Smith-Waterman traceback printer of the alignment block: from
 * cell (imax,jmax) of the score matrix __a, walk back while the
 * current cell explains itself via the diagonal / left / up
 * recurrence, appending each aligned character pair to S_RESULT.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h004 globals_ext        : the file-scope __a / S_RESULT and the
 *    GAP/MISMATCH/MATCH globals become the AlignState singleton
 *    (MdhStubs), registered with source header lines.
 *  - h007 char_buffer        : tmp[16] pair buffer, NUL-terminated,
 *    appended to S_RESULT up to the terminator.
 *  - h010 numeric_promotion  : the vendor implicitly widens the
 *    short matrix cells to int for the Score sums - explicit
 *    .toInt() here.
 *  - h012 unsigned_byte_domain: s/t/tmp are Char(0..255) domains.
 *  - h014 out_of_scope_call  : Score (owner libmpscpp.cpp:8049
 *    inline, not a corpus unit) -> new MdhStubs stub; AlignState.a
 *    is owned by init (libmpscpp.cpp:8100, batch-4 unit) - the
 *    carrier starts empty and reads before init are out of bounds,
 *    mirroring the vendor's NULL deref.
 *  - h017 naming             : prnt keeps the vendor name.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The int parameter ss is never used by the vendor - kept in the
 *    signature, unused here as there (unit-39 i precedent).
 *  - A walk that falls off the recurrence (no branch matches) simply
 *    exits the loop - the vendor has no error path, kept.
 */

private const val EMPTY_CELL: Short = -32768 // vendor libmpscpp.cpp:8029

fun prnt(ss: Int, s: CharArray, t: CharArray, imax: Int, jmax: Int): Int {
    var imax = imax
    var jmax = jmax
    val tmp = CharArray(16)
    val a = AlignState.a // vendor: __a (must follow init, see header)

    while (true) {
        // vendor: if (imax<=0 || jmax<=0) break;
        if (imax <= 0 || jmax <= 0) break

        // vendor: if (__a[imax][jmax]==0) break;
        if (a[imax][jmax] == 0.toShort()) break

        // vendor: if (__a[imax][jmax]==EMPTY_CELL) break;
        if (a[imax][jmax] == EMPTY_CELL) break

        // vendor: on the diagonal
        if (Score(s[imax], t[jmax]) + a[imax - 1][jmax - 1].toInt() == a[imax][jmax].toInt()) {
            tmp[0] = s[imax]
            tmp[1] = t[jmax]
            tmp[2] = NUL
            AlignState.sResult += cstringAt(tmp) // vendor: S_RESULT=S_RESULT+tmp
            imax--
            jmax--
            continue
        }

        // vendor: match against the gap column
        if (Score(s[imax], '-') + a[imax - 1][jmax].toInt() == a[imax][jmax].toInt()) {
            tmp[0] = s[imax]
            tmp[1] = '-'
            tmp[2] = NUL
            AlignState.sResult += cstringAt(tmp)
            imax--
            continue
        }

        // vendor: match against the gap row
        if (Score('-', t[jmax]) + a[imax][jmax - 1].toInt() == a[imax][jmax].toInt()) {
            tmp[0] = '-'
            tmp[1] = t[jmax]
            tmp[2] = NUL
            AlignState.sResult += cstringAt(tmp)
            jmax--
            continue
        }
    }

    return 0
}
