package mumps.interp

/**
 * PRO-arm translation of interp-rpm.cpp::parse_pattern_element (B4
 * unit 104, rank 196). Translated independently from the pristine
 * vendor source (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe),
 * reference source lines 406-442 (the pattern-match parser's
 * element rule).
 *
 * Identity verified against the study tool (tools/select_study_
 * subset.py): interp-rpm.cpp::parse_pattern_element, cpp, loc 35,
 * comment_ratio 0.028, pointer_marks 7, control_marks 5, score
 * 27.1 - exact match to wp1/study-subset/units.csv row 105.
 *
 * The EBNF element rule of the Mumps pattern parser: a run of
 * pattern codes becomes the bracketed POSIX class list "[...]",
 * a STRING token becomes a leaning-toothpickized group "(...)",
 * a LPAREN becomes the parenthesized alternation of
 * parse_pattern_atom_list, anything else is the empty string plus
 * the syntax-error tail.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h004 globals_ext       : the file-scope struct carriers of
 *    interp-rpm.cpp (enum token_type, struct token/scanner/parser)
 *    are raised here because this is the arm's first rpm unit -
 *    the batch-5 interp-rpm.cpp::<misc> unit owns their file
 *    definitions and will reconcile; each carrier registers its
 *    vendor source line in its doc.
 *  - h007 char_buffer       : struct token.text is the bstring
 *    carrier (Bstring, B1 unit 22); the scanner's const char*
 *    input/current_char ride a String plus an Int offset into it.
 *  - h012 unsigned_byte_domain: pattern text bytes Char(0..255);
 *    the vendor toupper() over the byte domain is the C-locale
 *    ASCII fold (private toupperC).
 *  - h014 out_of_scope_call : accept_it/accept/syntax_error/
 *    parse_pattern_atom_list (interp-rpm.cpp file statics, not
 *    corpus units) and bstrcat (interp-bstring.cpp::bstrcat,
 *    batch-9 corpus unit) and leaning_toothpickize (interp-
 *    rpm.cpp::leaning_toothpickize, batch-6 corpus unit) are
 *    owner-named InterpStubs stubs; new_bstring/bcatchr are the
 *    REAL B1 unit 22 helpers, reused unmodified.
 *  - h015 grammar_collision: the vendor strchr(pattern_codes, c)
 *    index idiom renders as an indexOf lookup; if the byte is not
 *    in the table the vendor computes an out-of-range pointer and
 *    reads wild memory - the Kotlin char_classes indexing throws
 *    IndexOutOfBounds at the same point (honest observable, kept).
 *  - h017 naming            : parse_pattern_element ->
 *    parsePatternElement; the enum constants keep vendor spelling.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The vendor comment "Grouping needed due to precedence issues"
 *    on the STRING arm is kept.
 *  - new_bstring is called with C string literals ("[", "(", "")
 *    in this unit; the CharArray-literal carrier in Bstring.kt
 *    takes a CharArray, so a file-private String-overload builds
 *    the literal buffer (call-shape helper, h015 checked - no
 *    public collision).
 *  - The default arm's result is the EMPTY bstring and the syntax
 *    error tail then aborts through sigint; the vendor returns
 *    result after the tail in the dead path - kept as the
 *    unconditional return of the arm.
 */
enum class TokenType {
    /** vendor enum token_type (interp-rpm.cpp:62). */
    INT, STRING, LPAREN, RPAREN, COMMA, DOT, PATTERN_CODE, EOT, ERROR
}

/** vendor struct token (interp-rpm.cpp:66-69): kind + bstring text. */
class Token {
    var kind: TokenType = TokenType.EOT
    var text: Bstring = Bstring()
}

/** vendor struct scanner (interp-rpm.cpp:71-75); input is the
 *  pattern String, currentChar the Int offset into it (h007). */
class Scanner(
    /** vendor const char* input (the whole pattern). */
    val input: String,
    /** vendor const char* current_char - offset into input. */
    var currentChar: Int = 0,
    /** vendor struct token current_token. */
    var currentToken: Token = Token()
)

/** vendor struct parser (interp-rpm.cpp:77-80). */
class Parser(
    var currentToken: Token = Token(),
    var s: Scanner = Scanner("")
)

/** vendor static const char pattern_codes[] (interp-rpm.cpp:109). */
private val PATTERN_CODES = "ACELNPU"

/** vendor static const char * const char_classes[] (interp-rpm.cpp:110-114). */
private val CHAR_CLASSES = arrayOf(
    "[:alpha:]", "[:cntrl:]", "[:print:][:cntrl:]",
    "[:lower:]", "[:digit:]", "[:punct:]", "[:upper:]"
)

/** new_bstring over a C string literal (call-shape helper, h015 private). */
private fun newBstringLit(s: String): Bstring {
    val buf = CharArray(s.length + 1)
    for (i in s.indices) buf[i] = s[i]
    buf[s.length] = NUL
    return newBstring(buf)
}

/** C toupper over the byte domain (C locale ASCII fold). */
private fun toupperC(c: Char): Char = if (c in 'a'..'z') c - 32 else c

/** vendor interp-rpm.cpp:406-442 - struct bstring parse_pattern_element(struct parser*). */
fun parsePatternElement(p: Parser): Bstring {
    var result: Bstring

    when (p.currentToken.kind) {
        TokenType.PATTERN_CODE -> {
            result = newBstringLit("[")
            do {
                // vendor: int i = strchr(pattern_codes,
                //        toupper(p->current_token.text.string[0])) - pattern_codes;
                val i = PATTERN_CODES.indexOf(toupperC(p.currentToken.text.string[0]))
                result = bstrcat(result, newBstringLit(CHAR_CLASSES[i]))
                acceptIt(p)
            } while (p.currentToken.kind == TokenType.PATTERN_CODE)
            bcatchr(result, ']')
        }
        TokenType.STRING -> {
            // Grouping needed due to precedence issues (vendor comment)
            result = bstrcat(newBstringLit("("), leaningToothpickize(p.currentToken.text))
            bcatchr(result, ')')
            acceptIt(p)
        }
        TokenType.LPAREN -> {
            acceptIt(p)
            result = bstrcat(newBstringLit("("), parsePatternAtomList(p))
            accept(p, TokenType.RPAREN)
            bcatchr(result, ')')
        }
        else -> {
            result = newBstringLit("")
            syntaxError("Syntax error in pattern", p.s.input, p.s.currentChar)
        }
    }
    return result
}
