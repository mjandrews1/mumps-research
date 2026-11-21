package daf

// Unit 96 (DAF B4 T1): interp-stem.cpp::escan (15 LOC, simple stratum):
// the word-ending scan/replace of the stemming engine. The C struct
// "ends" (ending/replace/offset/replen/next) and the table contract
// (last entry = empty ending string) are kept; strmove is the vendor
// in-place string move (strcpy semantics on the buffer slice), and the
// suffix strcmp reads word[wl-offset+k] against ending bytes until a
// mismatch (BASE-log row 15 pointer-into-buffer-compare adapted to a
// byte-domain helper; row 81 end-guard family noted: the vendor reads
// remain inside the caller-provided word buffer whose capacity exceeds
// wl). The word buffer is MUTATED in place (BASE-log row 31
// inplace-rebuild-and-replace consulted; applied WITHOUT the String
// rebuild because the caller's CharArray must observe the replacement
// - a StringBuilder rebuild would drop the alias, so this is the
// hit-adapt divergence note).
//
// DAF consults: BASE-log row 31 (hit-adapt), row 15 (hit-adapt),
// row 81 (hit-reuse). Dafny spec: spec/Escan.dfy (verified; the wl
// monotonicity precondition is the spec-assumption divergence logged
// in the DAF journal: the C has no such guarantee and may in principle
// not terminate on adversarial tables).

class EndsEntry(val ending: String, val replace: String, val offset: Int,
                 val replen: Int, val next: Int)

fun escan(word: CharArray, wlIn: Int, endings: Array<EndsEntry>): Int {
    var wl = wlIn
    var i = 0

    while (endings[i].ending.isNotEmpty()) {
        if (wl - endings[i].offset > 3 &&
                escanStrcmpAt(word, wl - endings[i].offset, endings[i].ending) == 0) {
            escanStrmoveAt(word, wl - endings[i].offset, endings[i].replace)
            wl += endings[i].replen - endings[i].offset

            if (endings[i].next != 0) break

            i = 0
        } else {
            i++
        }
    }

    return wl
}

// strcmp(&word[at], s): unsigned byte compare until mismatch; equal NULs
// end equal. Index reads stay inside word.size (caller contract note).
private fun escanStrcmpAt(word: CharArray, at: Int, s: String): Int {
    var k = 0
    while (k < s.length) {
        val a = word[at + k]
        val b = s[k]
        if (a != b) return if (a.code < b.code) -1 else 1
        k++
    }
    return 0
}

// strmove(&word[at], s): in-place string move onto the slice.
private fun escanStrmoveAt(word: CharArray, at: Int, s: String) {
    var k = 0
    while (k < s.length) {
        word[at + k] = s[k]
        k++
    }
    word[at + k] = '\u0000'
}
