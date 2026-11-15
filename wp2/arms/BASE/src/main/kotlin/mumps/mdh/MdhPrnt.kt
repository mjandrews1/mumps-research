package mumps.mdh

/**
 * Kotlin translation of libmpscpp.cpp::prnt (Study Subset unit 45,
 * Batch 2, arm BASE; translated 2026-11-15).
 *
 * C: int prnt(int ss, char *s, char *t, int imax, int jmax)
 * (libmpscpp.cpp:8057-8098) — Smith-Waterman alignment traceback: walk
 * the __a score matrix from the bottom-right corner, emitting aligned
 * character pairs into the global S_RESULT (diagonal = match/mismatch,
 * '-' = gap in s or t).
 *
 * QUIRKS PRESERVED: the parameter ss is UNUSED in the C (kept in the
 * Kotlin signature, documented — faithful-bugs policy, row 98 family).
 * The C reads __a[..] without NULL/length guards; init() (which
 * allocates __a, lines 8105-8131) is NOT a selected unit, so __a
 * starts as an empty array — a read through it throws in Kotlin where
 * C would segfault on the NULL short**; equivalent failure mode,
 * documented here.
 *
 * SUPPORT ENTITIES (libmpscpp.cpp:8028-8055, same TU): the file-scope
 * alignment globals (__a, S_RESULT, Size/Size_s/Size_t, GAP/MISMATCH/
 * MATCH, sx, MAX_ALIGNS, EMPTY_CELL) are translated here since prnt is
 * their first consumer in the subset; Score(char,char) is a 3-line
 * inline in the same file and is translated in place (keyfix heuristic-
 * 96 precedent for small inline dependencies).
 *
 * BUFFERS: tmp[16] (two chars + NUL appended to S_RESULT) -> direct
 * String concatenation (heuristic 56 value-role). C mutates its imax/
 * jmax params -> Kotlin immutable params imax0/jmax0 with local vars
 * (row 95 param-mutation precedent). short **__a -> Array<ShortArray?>
 * (rows allocated by init(), outside batch scope). Char params of
 * Score -> Char.
 */
const val MAX_ALIGNS = 5000000   // C: #define MAX_ALIGNS 5000000 (line 8028)
const val EMPTY_CELL: Short = -32768 // C: #define EMPTY_CELL -32768 (line 8029)

/** C: short **__a (line 8031) — score matrix; rows allocated by init(). */
var __a: Array<ShortArray?> = arrayOfNulls(0)

/** C: string S_RESULT (line 8032) — accumulated alignment traceback. */
var S_RESULT: String = ""

/** C: int Size, Size_s, Size_t, GAP, MISMATCH, MATCH, sx (lines 8033-8038). */
var Size: Int = 0
var Size_s: Int = 0
var Size_t: Int = 0
var GAP: Int = 0        // gap penalty
var MISMATCH: Int = 0   // mis-match penalty
var MATCH: Int = 0      // match reward
var sx: Int = 0

/** C: inline int Score(char a, char b) (libmpscpp.cpp:8049-8055) —
 *  3-line inline: gap penalty, then match reward, else mismatch. */
fun score(a: Char, b: Char): Int {
    if (a == '-' || b == '-') return GAP
    if (a == b) return MATCH
    return MISMATCH
}

/** C: int prnt(int ss, char *s, char *t, int imax, int jmax) */
fun prnt(ss: Int, s: String, t: String, imax0: Int, jmax0: Int): Int {
    var imax = imax0
    var jmax = jmax0

    while (true) { // C: while(1) {
        if (imax <= 0 || jmax <= 0) break

        if (__a[imax]!![jmax].toInt() == 0) break

        if (__a[imax]!![jmax] == EMPTY_CELL) break

        // C: if (Score(s[imax],t[jmax])+__a[imax-1][jmax-1] == __a[imax][jmax])
        if (score(s[imax], t[jmax]) + __a[imax - 1]!![jmax - 1] == __a[imax]!![jmax].toInt()) { // on the diagonal
            // C: tmp[0]=s[imax]; tmp[1]=t[jmax]; tmp[2]=0; S_RESULT=S_RESULT+tmp;
            S_RESULT += s[imax].toString() + t[jmax]
            imax--
            jmax--
            continue
        }

        // C: if (Score(s[imax],'-')+__a[imax-1][jmax] == __a[imax][jmax])
        if (score(s[imax], '-') + __a[imax - 1]!![jmax] == __a[imax]!![jmax].toInt()) {
            S_RESULT += s[imax].toString() + "-"
            imax--
            continue
        }

        // C: if (Score('-',t[jmax])+__a[imax][jmax-1] == __a[imax][jmax])
        if (score('-', t[jmax]) + __a[imax]!![jmax - 1] == __a[imax]!![jmax].toInt()) {
            S_RESULT += "-" + t[jmax]
            jmax--
            continue
        }
    }

    return 0
}
