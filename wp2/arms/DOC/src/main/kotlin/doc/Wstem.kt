package doc

// Unit 293 (B10 tranche 2): interp-stem.cpp::wstem
// (batch 10, core stratum; units.csv row 293: loc 28, cr 0.176,
// ptr 1, ctl 12, score 19.2, rank 164; physically verified this
// session at vendor interp-stem.cpp:418-461, physLOC 28) - the
// Porter-stemmer ending-group DISPATCHER: the last character
// of the word selects the endings table and the shared escan
// comparator does the work; any unlisted tail returns wl
// unchanged (default arm). TAG CORRECTION on the seat (the
// xindex/FindSym precedent): the Stubs.wstem raise below said
// "owner libmpscpp.cpp::wstem; not a roster unit" - WRONG on
// both counts; the body lives in interp-stem.cpp:418 (the
// declaration lands at interp-stem.cpp:43) and units.csv row
// 293 IS this batch-10 roster unit, so the batch-5 unit-154
// Stem/stem seat flips to DELEGATE to this render this push.
// Carriers all landed: the ten tables (dEndings/sEndings/
// gEndings/nEndings/eEndings/lEndings/yEndings/vEndings/
// tEndings/rEndings, the batch-6 InterpStemMisc renders)
// and escan (unit 96, Escan.kt, H-014 hit-reuse: the
// CharArray+wl in-place strmove contract of that seat). The
// word[wl-1] tail read is the vendor index idiom over the
// walked buffer (H-007). Naming per H-017 (wstem verbatim).
// Consult: unit 293, entry H-014, outcome hit-reuse,
// timestamp 2026-11-22T06:15:00Z.

fun wstem(word: CharArray, wl: Int): Int {
    when (word[wl - 1]) {
        'd' -> return escan(word, wl, dEndings)
        's' -> return escan(word, wl, sEndings)
        'g' -> return escan(word, wl, gEndings)
        'n' -> return escan(word, wl, nEndings)
        'e' -> return escan(word, wl, eEndings)
        'l' -> return escan(word, wl, lEndings)
        'y' -> return escan(word, wl, yEndings)
        'v' -> return escan(word, wl, vEndings)
        't' -> return escan(word, wl, tEndings)
        'r' -> return escan(word, wl, rEndings)
        else -> return wl
    }
}
