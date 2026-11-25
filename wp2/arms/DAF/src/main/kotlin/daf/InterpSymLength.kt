package daf

/*
 * DAF batch-10 unit 305 (roster row 305; interp-sym.cpp::_length,
 * LOC 16, ptr 0, score 5.4). Consult 2026-11-25T15:13:30Z.
 *
 * RELEASES the ZfcnsH.kt pending note (void _length(...): not
 * yet rendered rides THIS roster row; the note edit is there).
 *
 * Fold notes: the xindex(in, key, i) calls bind the interp-TU
 * released seat InterpStrmanipXindex.kt xindexDaf(s, sOff, pat,
 * patOff, from) with 0/0 offsets; the zero-needle arm is the
 * plain strlen fold (DafCRuntime.cstring); sprintf %d folds onto
 * the Locale.US pin (asciiDaf precedent).
 *
 * H-015: ASCII; no raw dollars; trailing newline.
 * Dafny: EXEMPT (index-family count; journal row).
 */

// void _length(unsigned char out[], unsigned char in[], unsigned char key[])
fun lengthSymD(out: CharArray, in_: CharArray, key: CharArray) {

    // if (strlen(key)) counting arm
    val keyS = DafCRuntime.cstring(key)

    if (keyS.isNotEmpty()) {
        var i = 1
        var j = 0
        val k = keyS.length // k = strlen(key)

        // while ((i = xindex(in, key, i)) != 0) { j++; i += k; }
        while (true) {
            val r = xindexDaf(in_, 0, key, 0, i)
            if (r == 0) break
            j++
            i = r + k
        }

        val s = String.format(java.util.Locale.US, "%d", j + 1)
        var q = 0
        while (q < s.length) { out[q] = s[q]; q++ }
        out[q] = NUL_D
        return
    }

    // else sprintf(out, "%d", strlen(in))
    var n = 0
    while (n < in_.size && in_[n] != NUL_D) n++
    val s2 = String.format(java.util.Locale.US, "%d", n)
    var q2 = 0
    while (q2 < s2.length) { out[q2] = s2[q2]; q2++ }
    out[q2] = NUL_D
}
