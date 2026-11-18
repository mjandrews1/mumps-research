package mumps.mdh

// ----------------------------------------------------------------------------
// (B10 unit 293) interp-stem.cpp::wstem (vendor reference lines 425-462).
// Identity: units.csv row 293 - interp-stem.cpp::wstem, cpp, loc 28,
// comment_ratio 0.176, pointer_marks 1, control_marks 12, score 19.2 -
// exact match.
// ----------------------------------------------------------------------------

/**
 * int wstem(char *word, int wl): the ZALTER/ZNORMAL stem dispatcher -
 * look at the word's final letter and hand the word to escan with the
 * matching endings table; any other final letter means the word is
 * returned unchanged.
 *
 * Seat: the raised prototype stub lived in MdhStubs (batch-5 unit),
 * so the function sits in the mdh package per the h015 fold note -
 * vendor body unchanged; it delegates to the batch-4 escan seat and
 * the B6 interp-stem.cpp::<misc> endings tables.
 * TAG CORRECTION: the old MdhStubs stub comment said wstem itself is
 * "not a corpus unit" - superseded, wstem IS roster row 293 (batch 10);
 * InterpStemMisc.kt imports this seat via mumps.mdh.wstem.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h007 char_buffer        : word is a C-string CharArray buffer.
 *  - h014 out_of_scope_call  : escan and the endings tables are DONE
 *    seats in mumps.interp (escan = batch-4 row 122 in Escan.kt;
 *    d/s/g/n/e/l/y/v/t/r_endings = B6 unit 163 row 164 in
 *    InterpStemMisc.kt) - cross-package imports, no stubs left.
 *  - h015 grammar_collision  : same name as the removed MdhStubs stub;
 *    public signature kept so existing import mumps.mdh.wstem
 *    call sites bind unchanged.
 *  - h017 naming             : wstem keeps the vendor spelling.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The unused "int i;" local and the two sentence comment above the
 *    switch are kept as this note's shape (h013: dead declaration not
 *    rendered).
 *  - The switch reads word[wl-1]; the vendor c_endings table exists
 *    but has NO case here - words ending in "c" fall to default and
 *    return wl; kept exactly.
 *  - The default arm returns the input length untouched - a word with
 *    an unrecognized final letter is its own stem; kept.
 */

import mumps.interp.d_endings
import mumps.interp.e_endings
import mumps.interp.escan
import mumps.interp.g_endings
import mumps.interp.l_endings
import mumps.interp.n_endings
import mumps.interp.r_endings
import mumps.interp.s_endings
import mumps.interp.t_endings
import mumps.interp.v_endings
import mumps.interp.y_endings

fun wstem(word: CharArray, len: Int): Int {
    // vendor: i = wl - 1; switch (word[i]) { ... default: return wl; }
    return when (word[len - 1]) {
        'd' -> escan(word, len, d_endings)
        's' -> escan(word, len, s_endings)
        'g' -> escan(word, len, g_endings)
        'n' -> escan(word, len, n_endings)
        'e' -> escan(word, len, e_endings)
        'l' -> escan(word, len, l_endings)
        'y' -> escan(word, len, y_endings)
        'v' -> escan(word, len, v_endings)
        't' -> escan(word, len, t_endings)
        'r' -> escan(word, len, r_endings)
        else -> len
    }
}
