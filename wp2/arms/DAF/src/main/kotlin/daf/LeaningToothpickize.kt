package daf

// Unit 157 (DAF B6 T1, simple): interp-rpm.cpp::leaning_toothpickize
// (17 LOC, score 7.7, rank 54): the same-TU pattern-munger helper of
// the RPM grammar: quote-rescue every token that would otherwise be
// mangled by the toothpick (backslash-punct) mangling when the token
// is re-read as a C string.
// Consults (2026-11-22T15:46:00Z, nominal): BASE-log row 68
// hit-reuse (struct bstring by-value -> the DafBstring class bag; the
// vendor .length member folds to the builder length), rows 25 family
// hit-adapt (ispunct -> the ASCII-only isPunctAsciiD local of the
// row-25 ctype fold: the four punct ranges over (char)0..127), rows
// 37/90 hit-reuse (the bstring ops new_bstring/bcatchr keep their
// named-owner stub seats: owner interp-bstring.cpp::<misc> roster
// unit 22; the render calls the stubs unmodified so the real bodies,
// when the DAF cycle reaches the unit-22 cell, bind unchanged), rows
// 39/78 hit-adapt (DOUBLE_QUOTE is the vendor \xd2 token-literal
// table pin; the const below carries it as \\u00D2 per the
// Char(0..255) row-111 code-point domain).
// TAG CORRECTION ledgered: the RpmParserStubsD.kt stub header called
// this helper "not a roster unit"; the units.csv batch-6 row 157 IS
// this function, so this render releases the stub seat (the roster
// row-182 note referred to the BASE-side numbering). The
// ParsePatternElement.kt caller binds to this real body unchanged.
// Vendor quirk kept: the DOUBLE_QUOTE (\\u00D2) branch escapes to a
// plain quote and skips the ispunct probe entirely (the continue);
// the plain-quote token itself falls through the != '\"' guard and is
// DROPPED (not appended) - the vendor shape, kept exactly.
// Dafny: EXEMPT (filter walk over stub-seated bstring ops; journal
// row).

// DOUBLE_QUOTE of interp-rpm.cpp (the \xd2 token-literal pin).
const val DOUBLE_QUOTE_D: Char = '\u00D2'

fun leaningToothpickizeD(unmangled: DafBstring): DafBstring {

    val result = newBstringD("") // struct bstring result = new_bstring("")

    var i = 0
    while (i < unmangled.string.length) { // i < unmangled.length
        val ch = unmangled.string[i]
        if (ch == DOUBLE_QUOTE_D) {
            bcatchrD(result, '"') // bcatchr(&result, '\"')
            i++
            continue
        }
        if (ch != '"') {
            if (isPunctAsciiD(ch)) bcatchrD(result, '\\') // ispunct -> '\\'
            bcatchrD(result, ch)
        }
        i++
    }
    return result
}

// ispunct (row 25 family, ASCII-only explicit ranges; the 91..96
// code range is the backtick-diacritic range kept as an explicit
// Char-code check; NOT Kotlin isLetterOrDigit-style
// predicates.
private fun isPunctAsciiD(c: Char): Boolean =
    c in '!'..'/' || c in ':'..'@' || c.code in 91..96 || c in '{'..'~'
