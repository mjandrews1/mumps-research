package doc

// Unit 285 (B10 tranche 1): interp-sym.cpp::_length (batch 10,
// simple stratum; units.csv row 306: loc 16, cr 0.059, ptr 0,
// ctl 2, score 5.4, rank 30; physically verified this session
// at vendor interp-sym.cpp:1361-1381, physLOC 16) - the KEYED
// dollar-length fold (the zfcns _length seat of the
// ZfcnsH.kt roster: this render is the first interp-sym.cpp
// body member of that family). With a NONEMPTY key the walk is
// the occurrence count of the key in the input: i starts at 1,
// the xindex probe (the REAL unit-264 seat, FindSym.kt, H-014
// hit-reuse; the vendor (short) i narrowing kept per H-010 -
// the probe position is a short in the C signature) returns
// each hit position or 0 at the end; j counts the hits and
// i += k advances PAST the needle length, so OVERLAPPING
// occurrences do NOT double-count (vendor quirks kept
// verbatim); the emitted count is j + 1 (the piece-count
// semantics: a non-empty input with zero hits reports 1, the
// family convention of u133/159). With an EMPTY key the walk is
// the plain strlen of the input. sprintf %d via strcpyS
// (H-009; plain decimals, no locale surface - the GlobalPiece
// precedent) on the NUL-terminated out carrier (H-007).
// Naming per H-017 (the leading underscore kept verbatim, the
// _find key id precedent).

fun _length(out: CharArray, in_: CharArray, key: CharArray) {
    val keyText = CstringOps.cstring(key)

    if (keyText.isNotEmpty()) { // if (strlen(key))
        var i = 1
        var j = 0
        val k = keyText.length

        // while ((i = xindex(in, key, (short) i)) != 0)
        while (true) {
            val hit = xindex(in_, key, i.toShort())
            if (hit == 0) break
            i = hit
            j++
            i += k
        }

        CstringOps.strcpyS(out, (j + 1).toString())
    } else {
        CstringOps.strcpyS(out, CstringOps.strlenC(in_).toString())
    }
}
