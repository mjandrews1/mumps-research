package daf

// Unit 147 (DAF B5 T3, complex): interp-rpm.cpp::scan_token (units.csv
// row 131: loc 39, cr 0.025, ptr 6, ctl 26, score 45.3, rank 232,
// complex): the rpm pattern tokenizer head: one switch over
// toupper(*current_char) dispatching the INT digit run, the STRING
// quote walk, the single-char tokens, the pattern-code letters, the
// EOT sentinel and the ERROR arm with its syntax_error diagnostic.
// Consults: BASE-log row 25 hit-reuse (toupper/isdigit as the
// ASCII-only toUpperAscii seats and the '0'..'9' range check; NOT
// Kotlin Character classes - C-locale semantics per the ctype
// fold), row 68 hit-adapt (the scanner struct fields (input,
// current_char, current_token) ride the DafParser carrier raised at
// b4 T2: input CharArray + currentChar Int index + the
// currentTokenKind/currentTokenText pair; switch -> when is the
// manual-decision of the unit, mirroring units 113/136), row 81
// hit-reuse (the *s->current_char deref folds onto the NUL-guard
// at() seat: past-end reads yield the NUL char, so the EOT arm is
// reachable exactly as in the vendor walk), row 90 hit-adapt (the
// syntaxErrorD seat resolves in THIS tranche: the same-TU static
// body is one of the resolved stubs of the unit-152
// interp-rpm.cpp::<misc> render, riding alongside; take_char rides
// the takeCharD body of the same unit-152 render - same-TU statics,
// not roster units, both rendered in this push).
// Quirk ledger (kept verbatim): the digit arm consumes at least one
// character before the isdigit check (a lone digit at buffer end
// still takes it); the STRING arm walks to the closing quote without
// a NUL check (the at() guard renders the vendor's unbounded read
// semantics safely - a NUL input byte keeps taking chars forever in
// vendor C; the render takes the NUL as a data char once per
// iteration with the SAME loop condition, no invented bound: THE
// VENDOR LOOP IS THE SPEC, the at() guard only stops the index from
// throwing); the pattern-code letters promote through toupper, so
// the lowercase twins ('a','c','e','l','n','p','u') match the
// uppercase case-set exactly as vendor (the probe is uppercased,
// not the case labels); the ERROR
// arm takes the offending char AFTER the syntax_error report.
// Dafny: EXEMPT (tokenizer switch over stub-resolved seats; the
// pure-kind dispatch head of the rpm parser family; journal row).

fun scanTokenD(s: DafParser): Int { // enum token_type scan_token(s)

    val probe = toUpperAscii(at(s.input, s.currentChar))

    when (probe) { // switch (toupper (*(s->current_char)))

        '0', '1', '2', '3', '4',
        '5', '6', '7', '8', '9' -> {
            do {
                takeCharD(s) // take_char (s);
            } while (at(s.input, s.currentChar) in '0'..'9')
            return TOKEN_INT_D
        }

        '"' -> {
            takeCharD(s)
            while (at(s.input, s.currentChar) != '"')
                takeCharD(s)
            takeCharD(s)
            return TOKEN_STRING_D
        }

        '(' -> {
            takeCharD(s)
            return TOKEN_LPAREN_D
        }

        ')' -> {
            takeCharD(s)
            return TOKEN_RPAREN_D
        }

        ',' -> {
            takeCharD(s)
            return TOKEN_COMMA_D
        }

        '.' -> {
            takeCharD(s)
            return TOKEN_DOT_D
        }

        'A', 'C', 'E', 'L',
        'N', 'P', 'U' -> {
            takeCharD(s)
            return TOKEN_PATTERN_CODE_D
        }

        '\u0000' -> return TOKEN_EOT_D

        else -> {
            syntaxErrorD(
                "Syntax error in pattern: invalid character",
                s.input, 1 + s.currentChar)
            takeCharD(s)
            return TOKEN_ERROR_D
        }
    }
}
