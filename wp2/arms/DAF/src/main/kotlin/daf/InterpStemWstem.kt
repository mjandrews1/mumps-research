package daf

// Unit 292 (DAF B10 T3, core): interp-stem.cpp::wstem (units.csv row
// 292: loc 28, score-family cell of the stem engine): the final-char
// dispatcher of the Porter-style stemmer: checks word[wl-1] and
// relays the buffer into the per-letter endings table through the
// single escan walk (the unit-96 Escan.kt seat: the struct ends
// scan/replace loop). Ten table arms d/s/g/n/e/l/y/v/t/r + the
// default wl kept verbatim.
// Consults (2026-11-25T15:07:00Z, nominal): row 105 family
// hit-reuse (the tables ride the InterpStemMisc.kt DafEndsRow
// carriers of the unit-94/95 renders - d_endings_D through
// r_endings_D, prefix_D - so the ONLY lattice here is the adapter:
// List of DafEndsRow -> Array<EndsEntry>, the Escan.kt seat's rust
// - the row-97 residue precedent of seat-shape bridging), row 31
// hit-reuse (word mutates in place; the CharArray plumb is the
// caller's buffer, escan rewrites the suffix per its replacement
// chain), row 15 hit-adapt (the word[i] final-char switch folds
// onto the Kotlin when over Char), row 99 return-fold (the int wl
// return of escan relays verbatim; the default arm returns wl
// untouched).
// VENDOR QUIRKS kept: (1) i = wl - 1 with no wl>0 guard (an empty
// word reads the NUL at index -1-adjacent cell; the render reads
// word[wl-1] only when wl >= 1, bounds-guarded, ledger divergence:
// the vendor OOB read on an empty word); (2) the switch tests the
// LOWER-CASE letters only (any other final char, including upper
// case, takes the default arm and the word is returned unstemmed).
// Dafny: EXEMPT (dispatcher over the exempt escan walk; journal
// row - the unit-96 spec/Escan.dfy covers the terminating kernels).

// int wstem(char *word, int wl) - the row-292 RELEASE of the
// wstemD named stub (LibmpscppMisc5.kt:187; the stub body deleted
// there with this release note; the mstring Stem wrapper callers
// keep their shapes).
fun wstemD(word: CharArray, len: Int): Int {

    val wl = len
    if (wl < 1) return wl // vendor reads word[wl-1] unguarded (quirk 1)

    val i = wl - 1

    return when (word[i]) { // switch (word[i])
        'd' -> escan(word, wl, endsToEscan(d_endings_D))
        's' -> escan(word, wl, endsToEscan(s_endings_D))
        'g' -> escan(word, wl, endsToEscan(g_endings_D))
        'n' -> escan(word, wl, endsToEscan(n_endings_D))
        'e' -> escan(word, wl, endsToEscan(e_endings_D))
        'l' -> escan(word, wl, endsToEscan(l_endings_D))
        'y' -> escan(word, wl, endsToEscan(y_endings_D))
        'v' -> escan(word, wl, endsToEscan(v_endings_D))
        't' -> escan(word, wl, endsToEscan(t_endings_D))
        'r' -> escan(word, wl, endsToEscan(r_endings_D))
        else -> wl // default: return wl
    }
}

// the InterpStemMisc DafEndsRow tables -> the Escan.kt EndsEntry
// seat shape (the field order differs by design: DafEndsRow carries
// ending/offset/replace/replen/next, EndsEntry orders ending/
// replace/offset/replen/next; the adapter pins each field
// positionally - no table content is re-derived).
private fun endsToEscan(rows: List<DafEndsRow>): Array<EndsEntry> =
    rows.map { EndsEntry(it.ending, it.replace, it.offset, it.replen, it.next) }
        .toTypedArray()
