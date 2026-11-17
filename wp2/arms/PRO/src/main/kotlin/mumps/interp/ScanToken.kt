package mumps.interp

/**
 * PRO-arm translation of interp-rpm.cpp::scan_token (B5 unit 128,
 * rank 232). Translated independently from the pristine vendor
 * source (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe).
 *
 * BOUNDARY NOTE (pre-registered vs physical): the study tool's
 * recorded boundary for this unit is vendor lines 259-506 (units.csv
 * row 132: interp-rpm.cpp::scan_token, cpp, loc 39, comment_ratio
 * 0.025, pointer_marks 6, control_marks 26, score 45.3) - the
 * chunker's brace balancer does not close on the real function
 * (the '"' scan case), so the recorded span runs to end-of-file
 * and OVERLAPS unit 135 parse_count (recorded 360-506, real
 * 360-402) and the leaning_toothpickize batch-6 unit (recorded
 * 479-506, real 479-497). Both overlaps render per their own
 * pre-registered boundaries: this unit translates the REAL
 * scan_token body (vendor 259-297); the residue inside the
 * recorded span is already owned - take_char/new_parser/parse/
 * parse_pattern/parse_pattern_atom and accept/accept_it/
 * syntax_error (DONE, unit 127 InterpRpmMisc.kt), parse_count
 * (unit 135, rides the InterpStubs parseCount stub until it
 * lands), parse_pattern_element (DONE, unit 104) and
 * leaning_toothpickize (batch-6 stub) - verification only, no
 * redeclaration (u124 front-run precedent).
 *
 * scan_token(s): the one-token lexical step of the Mumps pattern
 * parser - digit runs scan as INT, quoted spans as STRING, the
 * six punctuation tokens, the seven pattern codes, NUL as EOT,
 * anything else the invalid-character error tail.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h007 char_buffer       : the switch reads *current_char;
 *    the C string terminator quantizes to NUL at end-of-input
 *    (private charAtC, same shape as unit 127's) - the EOT case
 *    rides it (h007 quirk note, not a bounds fix).
 *  - h012 unsigned_byte_domain: toupper/isdigit over the C
 *    unsigned-char domain - private toupperC/isdigitC (C locale
 *    ASCII folds; Parse_.kt:258 per-file private convention).
 *  - h014 out_of_scope_call : the InterpStubs scanToken stub is
 *    REMOVED - this unit is its owner (Scan unit 81 precedent);
 *    take_char and syntax_error are DONE (unit 127) - direct
 *    calls.
 *  - h015 grammar_collision : pre-push scan incl. cross-file
 *    top-level names: new top-level scanToken (the removed stub
 *    clears its seat); private toupperC/isdigitC/charAtC follow
 *    the arm's per-file convention (Parse_.kt:258, unit 127) -
 *    no public collisions.
 *  - h017 naming          : scan_token -> scanToken.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The STRING case scans to the closing quote with NO
 *    terminator check: an unterminated literal walks the C
 *    buffer unbounded (UB); the Kotlin carrier reads the
 *    quantized NUL forever - a deterministic non-return where
 *    the vendor had undefined behavior. Both builds fail to
 *    return normally; the quantization is documented, not fixed.
 *  - The default arm's take_char + return ERROR after
 *    syntax_error is unreachable in the vendor (sigint aborts);
 *    the Kotlin sigint stub throws - kept verbatim, still
 *    unreachable.
 *  - The do-while INT shape (consume at least one digit) is
 *    kept - the case label guarantees isdigit on entry.
 */

/** The vendor's read of *current_char at the C terminator: the
 *  byte at end-of-input quantizes to NUL (h007 quirk note -
 *  EOT rides it; same shape as unit 127). */
private fun charAtC(s: Scanner): Char =
    if (s.currentChar < s.input.length) s.input[s.currentChar] else NUL

/** C toupper over the byte domain (C locale ASCII fold). */
private fun toupperC(c: Char): Char = if (c in 'a'..'z') c - 32 else c

/** C isdigit over the byte domain (C locale ASCII fold). */
private fun isdigitC(c: Char): Boolean = c in '0'..'9'

/** vendor 259-297: enum token_type scan_token(struct scanner * s). */
fun scanToken(s: Scanner): TokenType {
    when (toupperC(charAtC(s))) {
        '0', '1', '2', '3', '4',
        '5', '6', '7', '8', '9' -> {
            do {
                takeChar(s) // DONE - unit 127
            } while (isdigitC(charAtC(s)))
            return TokenType.INT
        }
        '"' -> {
            takeChar(s)
            while (charAtC(s) != '"') {
                takeChar(s) // no terminator check (vendor quirk, see header)
            }
            takeChar(s)
            return TokenType.STRING
        }
        '(' -> {
            takeChar(s)
            return TokenType.LPAREN
        }
        ')' -> {
            takeChar(s)
            return TokenType.RPAREN
        }
        ',' -> {
            takeChar(s)
            return TokenType.COMMA
        }
        '.' -> {
            takeChar(s)
            return TokenType.DOT
        }
        'A', 'C', 'E', 'L',
        'N', 'P', 'U' -> {
            takeChar(s)
            return TokenType.PATTERN_CODE
        }
        NUL -> {
            return TokenType.EOT
        }
        else -> {
            syntaxError(
                "Syntax error in pattern: invalid character",
                s.input, 1 + s.currentChar
            ) // DONE - unit 127
            takeChar(s) // unreachable tail (sigint aborts in the vendor)
            return TokenType.ERROR
        }
    }
}
