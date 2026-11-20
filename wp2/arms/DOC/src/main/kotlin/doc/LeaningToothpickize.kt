package doc

// Unit 157 (B6 tranche 1): interp-rpm.cpp::leaning_toothpickize
// (batch 6, simple stratum; LOC 17, score 7.7). The pattern-rule
// unescaper of the rpm layer: walks the unmangled bstring and
// re-escapes each punctuation byte with a backslash, mapping the
// DOUBLE_QUOTE sentinel byte back to a literal quote, silently
// dropping literal ASCII quotes, and passing every other byte
// through. Resolves the Stubs.leaning_toothpickize seat raised at
// the batch-5 interp-rpm.cpp::<misc> render (unit 152; the stub
// now delegates here, the parse_pattern_atom_list delegate
// precedent). The parse_pattern_element call site (unit 146
// render, ParsePatternElement.kt:95) binds unchanged.
//
// interp-rpm.cpp:58 defines DOUBLE_QUOTE 0xd2 as a file-scope
// macro; it is seated here file-private because this unit is the
// first corpus render to read it (the batch-9 perl_pm owner
// reads it too - the unit-152 ledger phrase "stays with their
// batch-9 owner unit" is corrected by ownership order: first
// corpus reader seats it, the batch-9 render reuses or
// re-derives; noted here per H-004). The vendor literal-quote
// drop quirk: an unmangled ASCII double quote (the '"' else arm)
// is emitted nowhere - kept verbatim with a note at the shape.
// ispunct renders per H-012 as the ASCII C-locale punctuation
// domain (33-47, 58-64, 91-96, 123-126); signed-char UB of the
// C ispunct domain is absent in the Char carrier, divergence
// noted. The C for loop over unmangled.length renders as the
// index while walk (whole-token copies precluded; carrier
// indexed in place). H-008: the result is a fresh Bstring via
// newBstring - no static carrier. Naming per H-017
// (leaning_toothpickize is a Kotlin-safe lexeme, kept).

// interp-rpm.cpp:58: define DOUBLE_QUOTE 0xd2 (the embedded-quote
// sentinel of the rpm pattern layer; first corpus reader here,
// the batch-9 perl_pm row reuses the byte value).
private const val DOUBLE_QUOTE: Char = '\u00D2'

// int ispunct(int) - C locale ASCII punctuation domain (H-012).
private fun isPunctByte(c: Char): Boolean {
    val v = c.code
    return (v in 33..47) || (v in 58..64) ||
        (v in 91..96) || (v in 123..126)
}

// struct bstring leaning_toothpickize(const struct bstring);
// interp-rpm.cpp:484.
fun leaning_toothpickize(unmangled: Bstring): Bstring {
    val result = newBstring("".toCharArray())

    var i = 0
    while (i < unmangled.length) {
        val c = unmangled.string[i]
        if (c == DOUBLE_QUOTE) {
            bcatchr(result, '"')
            i++
            continue
        }
        if (c != '"') {
            // ispunct(...) -> bcatchr(&result, '\\');
            if (isPunctByte(c)) bcatchr(result, '\\')
            bcatchr(result, c)
        }
        // vendor literal ASCII '"' arm: emits nothing (quirks kept)
        i++
    }
    return result
}
