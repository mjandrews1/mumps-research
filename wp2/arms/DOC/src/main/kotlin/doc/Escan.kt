package doc

// Unit 96 (B4 tranche 1): interp-stem.cpp::escan (batch 4, simple
// stratum; LOC 15, score 5.9). The Porter-style stem ending scanner.
// Carrier: the vendor struct ends (include/mumpsc/stem.h:31) renders as
// the data class Ends (ending/offset/replace/replen/next fields in the
// header's order); the ending table itself lives with its batch-6 owners
// (interp-stem.cpp:65 stems table) and is passed in by callers.
// H-007 adaptation: word is a pointer-walked C buffer MUTATED IN PLACE by
// strmove (real write at wl-offset), so the render keeps the CharArray
// plus offset and never folds to String. H-012: the strcmp tail compares
// the unsigned-char domain exactly. H-014: strmove is the shared stub.
// Vendor quirks kept: the length guard is strictly "wl - offset > 3";
// the table terminator is the first entry whose ending is the empty
// string (the C reads ending[0]==0; the size guard is the array-bounds
// quantization of the same terminator); after a replacement with
// next==0 the scan restarts at entry 0 (only next!=0 breaks).

data class Ends(
    val ending: String,   // stem.h:32 const char *ending
    val offset: Int,     // stem.h:33 int offset (length)
    val replace: String, // stem.h:34 const char *replace
    val replen: Int,     // stem.h:35 int replen (length)
    val next: Int,       // stem.h:36 int next (goto: 0 restart, nonzero break)
)

fun escan(word: CharArray, wl: Int, endings: List<Ends>): Int {
    var wl = wl
    var i = 0

    outer@ while (i < endings.size && endings[i].ending.isNotEmpty()) {
        val e = endings[i]

        if (wl - e.offset > 3 &&
                word[wl - e.offset + e.ending.length] == NUL &&
                CstringOps.strncmpAt(word, wl - e.offset, e.ending,
                        e.ending.length) == 0) {
            // strmove((unsigned char*)&word[wl-offset], replace): NUL-
            // terminated in-place strcpy at the tail position.
            for (k in e.replace.indices) {
                word[wl - e.offset + k] = e.replace[k]
            }
            word[wl - e.offset + e.replace.length] = NUL

            wl += e.replen - e.offset

            if (e.next != 0) break@outer

            i = 0
        } else {
            i++
        }
    }

    return wl
}
