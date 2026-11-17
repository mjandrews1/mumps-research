package mumps.mdh

/**
 * PRO-arm translation of libmpscpp.cpp::Cost (B3 unit 82, rank 120).
 * Translated independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), reference source
 * lines 8122-8147.
 *
 * Identity verified against the study tool (tools/select_study_
 * subset.py): libmpscpp.cpp::Cost, cpp, loc 15, comment_ratio
 * 0.062, pointer_marks 2, control_marks 5, score 12.3, batch 3 -
 * exact match to wp1/study-subset/units.csv row 120. Single
 * overload in the vendor.
 *
 * int Cost(char* s, char* t, int i, int j): the Smith-Waterman
 * recursion core - memoized DP cell (i,j) over the alignment
 * matrix __a, diagonal MATCH/MISMATCH plus two GAP moves, clamped
 * at zero.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h004 globals_ext     : the file-scope alignment matrix __a
 *    and the GAP/MISMATCH/MATCH scores ride the AlignState object
 *    (owner unit 45; the matrix allocation owner is init, batch-4
 *    unit); EMPTY_CELL (-32768, libmpscpp.cpp:8029) is this file's
 *    private constant.
 *  - h010 numeric_promotion: vendor "return __a[i][j]=A" stores
 *    int A into the short matrix (implicit C narrowing) and
 *    returns the int A - explicit .toShort() store, Int return
 *    kept.
 *  - h012 unsigned_byte_domain: s/t are C-string domains (Char
 *    comparison per byte).
 *  - h014 out_of_scope_call: the MdhStubs Cost stub raised by
 *    unit 61 (Sw) is resolved by this unit and removed
 *    (cleanLocks precedent, unit 56); the recursion is the
 *    function itself.
 *  - h015 grammar_collision: pre-push scan incl. cross-file
 *    top-level names: new top-level Cost - no collisions.
 *  - h017 naming          : Cost keeps the vendor name.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The EMPTY_CELL memo gate runs BEFORE the bounds gate, so a
 *    memoized negative index cell would be read first - the
 *    vendor relies on the matrix never being queried below the
 *    origin (kept; the i<=0||j<=0 arm follows).
 *  - The A/b/c candidates are int locals; the stored short is
 *    re-promoted on read (kept via .toInt() comparisons, the
 *    -32768 EMPTY_CELL sentinel survives the round trip).
 */

private const val EMPTY_CELL: Short = -32768 // vendor libmpscpp.cpp:8029

fun Cost(s: CharArray, t: CharArray, i: Int, j: Int): Int {
    if (AlignState.a[i][j] != EMPTY_CELL) return AlignState.a[i][j].toInt()

    if (i <= 0 || j <= 0) {
        AlignState.a[i][j] = 0
        return 0
    }

    var A = Cost(s, t, i - 1, j - 1) + if (s[i] == t[j]) AlignState.match else AlignState.mismatch
    val b = Cost(s, t, i - 1, j) + AlignState.gap
    val c = Cost(s, t, i, j - 1) + AlignState.gap

    if (b > A) A = b
    if (c > A) A = c
    if (A <= 0) A = 0

    AlignState.a[i][j] = A.toShort() // h010: int -> short store (C narrowing)
    return A
}
