package doc

// Unit 147 (B5 tranche 3): interp-rpm.cpp::scan_token (batch 5,
// complex stratum; units.csv row 147: loc 39, score 45.3, rank
// 232). The single-token scanner of the rpm pattern grammar: the
// leading character classifies the token - digit run; quoted
// STRING through its closing quote; single-character tokens
// LPAREN/RPAREN/COMMA/DOT; the seven PATTERN_CODE letters; EOT on
// the NUL; everything else is the syntax_error ERROR arm. The
// token/scanner carriers are the unit-113 renders
// (ParsePatternElement.kt; the String + index domain of the
// scanner holds s.currentChar as the walk offset - the byte
// domain of the C pointer, H-012/H-007).
//
// H-014 seats: take_char is a sub-min-loc function of
// interp-rpm.cpp owned by the batch-5 rpm misc unit (u152 this
// same tranche) - the NEW Stubs.take_char seat raised here binds
// the two call arms, and the u152 render flips it to the real
// body delegate; syntax_error rides the existing stub seat
// (owner the same rpm misc chunk, same flip at u152). The
// vendor do-while on the INT arm keeps the take-then-retest
// order; the STRING arm walks to the closing quote INCLUSIVE
// (the final take_char consumes the quote, vendor shape).
// toupper/isdigit render on the ASCII domain (the C locale
// byte filter, H-012). Naming per H-017 (scan_token kept snake
// per the vendor name fidelity convention of this family).

fun scan_token(s: scanner): token_type {
    // The C current_char walk can run past the NUL on an
    // unterminated STRING token (vendor UB); the String-domain
    // walk treats the index at/after the terminator as the NUL
    // (deterministic quantization, u128 precedent).
    val cur = if (s.currentChar < s.input.length) s.input[s.currentChar] else NUL
    return when (Character.toUpperCase(cur)) {
        '0', '1', '2', '3', '4',
        '5', '6', '7', '8', '9' -> {
            do {
                Stubs.take_char(s)
            } while (s.currentChar < s.input.length &&
                    isdigitSC(s.input[s.currentChar]))
            token_type.INT
        }
        '"' -> {
            Stubs.take_char(s)
            while (s.currentChar < s.input.length &&
                    s.input[s.currentChar] != '"') {
                Stubs.take_char(s)
            }
            Stubs.take_char(s)
            token_type.STRING
        }
        '(' -> {
            Stubs.take_char(s)
            token_type.LPAREN
        }
        ')' -> {
            Stubs.take_char(s)
            token_type.RPAREN
        }
        ',' -> {
            Stubs.take_char(s)
            token_type.COMMA
        }
        '.' -> {
            Stubs.take_char(s)
            token_type.DOT
        }
        'A', 'C', 'E', 'L',
        'N', 'P', 'U' -> {
            Stubs.take_char(s)
            token_type.PATTERN_CODE
        }
        '\u0000' -> {
            token_type.EOT
        }
        else -> {
            Stubs.syntax_error(
                "Syntax error in pattern: invalid character",
                s.input, 1 + s.currentChar)
            Stubs.take_char(s)
            token_type.ERROR
        }
    }
}

// isdigit on the ASCII byte domain (the C locale filter).
private fun isdigitSC(c: Char): Boolean = c in '0'..'9'
