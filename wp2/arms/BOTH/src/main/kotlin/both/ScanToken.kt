package both

// Unit 147 (BOTH B5 T3, complex): interp-rpm.cpp::scan_token (39
// LOC). Batch_plan ordinal 147: complex, LOC 39, score 45.3. The
// RPM scanner's single-character token classifier: the pattern
// grammar's tokens come off the scanner one character class at a
// time.
//
// Vendor behavior: the switch keys on toupper(*current_char): the
// ten digits take_char while isdigit and yield INT; a double quote
// takes the opening quote, consumes until the next quote, takes the
// closing quote and yields STRING (an unterminated literal runs off
// the end of the C string - the pointer walks the NUL, which does
// not equal a quote: the vendor loop is unbounded; quantized below
// at the same seat); '(' ')', ',' and '.' take one char and yield
// LPAREN, RPAREN, COMMA, DOT; the seven pattern codes ACELNPU (the
// pattern_codes table order ACL list kept by the case shape) take
// one char and yield PATTERN_CODE; NUL yields EOT; everything else
// calls syntax_error (which sigint(100)s in the vendor - the stub
// throws), then take_char and ERROR.
//
// Quirks kept (ledgered):
//   - toupper(*p) EMPTY-CHARACTER truth: the vendor calls
//     toupper(*(s->current_char)) on the char domain; the toupper
//     of the NUL byte selects no case and falls to default - but the
//     EOT case precedes it ('case 0'), so the NUL test is the
//     char-code test (kept exact through the Char domain fold).
//   - the three-case 'A','C','E','L','N','P','U' listing folds to
//     the single membership test over the same seven codes.
//   - the unsigned-char vs char of toupper: the input domain here is
//     the ASCII pattern text; h012 char domain kept.
//
// BOTH consults (find_rule before render, events logged):
//   h007 char_buffer - reused: current_char is the offset walk over
//     the scanner input String (the pointer-into-buffer fold).
//   h012 unsigned_byte_domain - reused: the char-code compares stay
//     in the Char(0..255) domain.
//   h014 out_of_scope_call - reused: take_char and syntax_error are
//     the SAME-TU chunks of interp-rpm.cpp::<misc> (roster 271,
//     rendered this tranche); the calls ride the RpmMisc seats.
//   h017 naming - hit: scan_token/take_char/syntax_error keep vendor
//     snake shape per the arm mapping (h017 keyword note).

internal fun scanToken(s: Scanner): TokenType {
    val c = s.currentChar() // *(s->current_char)
    val up = Character.toUpperCase(c)

    return when {
        up in '0'..'9' -> {
            do {
                takeChar(s)
            } while (Character.isDigit(s.currentChar()))
            TokenType.INT
        }
        up == '"' -> {
            takeChar(s)
            while (s.currentChar() != '"')
                takeChar(s)
            takeChar(s)
            TokenType.STRING
        }
        up == '(' -> {
            takeChar(s)
            TokenType.LPAREN
        }
        up == ')' -> {
            takeChar(s)
            TokenType.RPAREN
        }
        up == ',' -> {
            takeChar(s)
            TokenType.COMMA
        }
        up == '.' -> {
            takeChar(s)
            TokenType.DOT
        }
        up == 'A' || up == 'C' || up == 'E' || up == 'L' ||
            up == 'N' || up == 'P' || up == 'U' -> {
            takeChar(s)
            TokenType.PATTERN_CODE
        }
        c == 0.toChar() -> TokenType.EOT
        else -> {
            syntaxError(
                "Syntax error in pattern: invalid character",
                s.input, 1 + s.pos,
            )
            takeChar(s)
            TokenType.ERROR
        }
    }
}
