package doc

// Unit 190 (B7 tranche 1): interp-strmanip.cpp::substr (batch 7,
// simple stratum; LOC 19, score 7.1, rank 45; physLOC 59,
// physically verified this session). The interpreter-side
// substring twin: len 0 writes the C empty string, negative len
// clamps to 255 (this twin clamps only on the negative side),
// the STR_MAX overrun arm calls realloc on the out pointer
// (H-001 quantize note: the Kotlin fixed CharArray carrier has
// no realloc; the vendor's out-growth request is the caller's
// buffer contract, kept as this note and the else-if chain
// quantizes to no-op - the subsequent walk is unchanged, and
// the landed callers' carriers are compile-fixed exactly like
// the C callers' allocations), then the 1-based start/len walk
// copies and NUL-terminates (short i per H-010, widened at the
// index arithmetic only). DISTINCT-TWIN NOTE (the unit-59 inout
// precedent): the compiler-sys1.c twin (batch 7 row 189) is a
// separate roster row rendering with the vendor SHORT parameter
// domain in CompilerSubstr.kt; THIS seat carries the int-domain
// stub signature of Stubs.substr and FLIPS that seat to this
// real in this push (the past tag "interp substr helper" was
// owner-correct, the owner is this row). The PatrnInterp.kt
// call sites bind through the flipped stub; the compiler-side
// call sites rebind to the short twin (Match.kt/PatrnSys1.kt,
// edits landed in this push).
// Naming kept (substr is the C symbol; H-017 has no camel form).

fun substr(in_: CharArray, out: CharArray, start: Int, len: Int) {
    var startV = start
    var lenV = len
    var i: Short = 0

    if (lenV == 0) {
        out[0] = '\u0000'
        return
    }

    if (lenV < 0) {
        lenV = 255
    } else if (lenV - startV + 1 > STR_MAX) {
        // out = (unsigned char *) realloc(out, ...): H-001 quantize
        // note in the header; no-op on the fixed carrier
    }

    startV--
    lenV--
    while (i <= lenV) {
        out[i.toInt()] = in_[startV]
        startV++
        i++
    }
    out[lenV + 1] = '\u0000'
}
