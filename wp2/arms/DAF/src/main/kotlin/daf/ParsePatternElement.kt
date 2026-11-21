package daf

// Unit 113 (DAF B4 T2, core): interp-rpm.cpp::parse_pattern_element
// (35 LOC): the rpm pattern-grammar rule that renders one pattern
// element: a run of pattern-code letters expands into its bracketed
// character-class list, a STRING token renders as its toothpickized
// parenthesized literal, a parenthesized group recurses into
// parse_pattern_atom_list, and anything else is a syntax error.
// Consults: BASE-log row 68 hit-reuse (struct bstring by-value
// returns -> the DafBstring class instance carrier; struct pointer
// params -> object references), row 25 family (toupper via the
// ASCII-only toUpperAscii seat; the strchr over pattern_codes renders
// as the explicit index scan, the zero-switch finding of the BASE
// corpus has no row: switch->when is the manual-decision of this
// unit, logged), row 33 family (strchr's pointer arithmetic collapses
// to the found-index Int), rows 69/2 family (the syntax_error
// diagnostic relay; carried by the stub seat with the vendor message
// text kept verbatim).
// Seats: carriers and named-owner stubs in RpmParserStubsD.kt (see
// its header for the owner roster homes).
// Dafny: EXEMPT (parser-framework relay over stub seats; journal row).

fun parsePatternElementD(p: DafParser): DafBstring {

    var result: DafBstring

    when (p.currentTokenKind) { // switch (p->current_token.kind)

        TOKEN_PATTERN_CODE_D -> {
            result = newBstringD("[")
            do {
                // int i = strchr(pattern_codes,
                //     toupper(...text[0])) - pattern_codes
                val sought = toUpperAscii(p.currentTokenText.string[0])
                var i = 0
                while (i < PATTERN_CODES_D.length && PATTERN_CODES_D[i] != sought) i++
                result = bstrcatD(result, newBstringD(CHAR_CLASSES_D[i]))
                acceptItD(p)
            } while (p.currentTokenKind == TOKEN_PATTERN_CODE_D)
            bcatchrD(result, ']')
        }

        TOKEN_STRING_D -> {
            // Grouping needed due to precedence issues (vendor comment)
            result = bstrcatD(newBstringD("("),
                leaningToothpickizeD(p.currentTokenText))
            bcatchrD(result, ')')
            acceptItD(p)
        }

        TOKEN_LPAREN_D -> {
            acceptItD(p)
            result = bstrcatD(newBstringD("("),
                parsePatternAtomListD(p))
            acceptD(p, TOKEN_RPAREN_D)
            bcatchrD(result, ')')
        }

        else -> {
            result = newBstringD("")
            syntaxErrorD("Syntax error in pattern", p.input, p.currentChar)
        }
    }

    return result
}
