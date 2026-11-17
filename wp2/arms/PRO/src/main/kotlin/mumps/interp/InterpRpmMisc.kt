package mumps.interp

/**
 * PRO-arm translation of interp-rpm.cpp::<misc> (B5 unit 127,
 * rank 271). Translated independently from the pristine vendor
 * source (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe),
 * reference source: the file-header residue (lines 1-118: banner,
 * includes, the sym_/sigint forward decls, the DOUBLE_QUOTE /
 * MAX_BACKREFS / OFFSET_BUF_LEN / min defines, the token_type /
 * token / scanner / parser declarations, the static prototypes
 * and the pattern_codes / char_classes tables) plus the
 * sub-15-LOC functions rpm (120-138), perlmatch (143-145),
 * perlcpp (147-149), new_scanner (235-244), scan (250-253),
 * take_char (302-304), new_parser (310-316), parse (322-331),
 * parse_pattern (337-344), parse_pattern_atom (350-354),
 * parse_pattern_atom_list (447-457), accept (461-466),
 * accept_it (472-475) and syntax_error (501-505).
 *
 * Identity verified against the study tool (tools/select_study_
 * subset.py): interp-rpm.cpp::<misc>, cpp, loc 145,
 * comment_ratio 0.406, pointer_marks 26, control_marks 9, score
 * 92.5 - exact match to wp1/study-subset/units.csv row 127.
 * The >=15-LOC named units stay out of this render: scan_token
 * (unit 128), parse_count (unit 135), parse_pattern_element
 * (DONE, B4 unit 104 ParsePatternElement.kt), perl_pm (batch 9)
 * and leaning_toothpickize (batch 6).
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h002 simple_output_macro: syntax_error's printf trio ->
 *    System.out; the caret line printf("%*c\n", pos, '^') is the
 *    pos-1 space padding plus the caret (keep printf shape).
 *  - h004 globals_ext       : the file-scope carriers raised
 *    provisionally by unit 104 (ParsePatternElement.kt) are
 *    RECONCILED here - PATTERN_CODES, CHAR_CLASSES, newBstringLit
 *    and toupperC move to this file (their misc-chunk home) as
 *    internal; the enum/struct carriers (TokenType, Token,
 *    Scanner, Parser) stay raised in ParsePatternElement.kt per
 *    the arm's first-rpm-unit convention, unchanged.
 *  - h007 char_buffer       : the scanner's const char* walk is
 *    the String + Int-offset carrier (unit 104 Scanner); the
 *    C string terminator read past the end (take_char/scan
 *    dereferencing the NUL) quantizes to NUL at end-of-input via
 *    the private charAtC accessor - the vendor's EOT case rides
 *    it (documented quirk, not a bounds fix); rpm's
 *    pcre_pattern[STR_MAX] is a fresh CharArray(STR_MAX).
 *  - h012 unsigned_byte_domain: pattern bytes Char(0..255);
 *    toupper is the C-locale ASCII fold (toupperC).
 *  - h013 dead_store        : MAX_BACKREFS (54), OFFSET_BUF_LEN
 *    (56) and the min macro (58) are dead after the vendor's
 *    2026 PCRE-to-clib-regex conversion (no remaining uses in
 *    the file) - carried as anchor comments, not translated.
 *  - h014 out_of_scope_call : perl_pm (batch-9 corpus unit),
 *    scan_token (unit 128) and parse_count (unit 135) get NEW
 *    owner-named InterpStubs stubs; sigint and bstrcat reuse
 *    their existing stubs; the previously-raised InterpStubs
 *    stubs rpm / accept / acceptIt / syntaxError /
 *    parsePatternAtomList are RESOLVED and REMOVED (Scan unit 81
 *    precedent) - this misc chunk owns their vendor lines;
 *    makeCstring and bcatchr are the REAL B1 unit 22 helpers,
 *    reused unmodified.
 *  - h015 grammar_collision : pre-push scan incl. cross-file
 *    top-level names: new top-level rpm/perlmatch/perlcpp/
 *    newScanner/scan/takeChar/newParser/parse/parsePattern/
 *    parsePatternAtom/parsePatternAtomList/accept/acceptIt/
 *    syntaxError/DOUBLE_QUOTE - the removed same-named stubs
 *    clear their seats; private STR_MAX/charAtC repeat the
 *    arm's file-private convention (no public collisions).
 *  - h017 naming          : mechanical snake->camel per arm
 *    mapping (new_scanner -> newScanner, take_char -> takeChar,
 *    parse_pattern -> parsePattern, accept_it -> acceptIt,
 *    syntax_error -> syntaxError); rpm/perlmatch/perlcpp/scan/
 *    accept keep vendor spelling.
 *  - h018 preprocessor_conditional: rpm's "if (PM_DEBUG)
 *    printf" (vendor 135) is dead in the reference build
 *    (include/mumpsc/pm.h:25 defines PM_DEBUG 0) - the debug
 *    print is carried as a comment, not translated (u125
 *    precedent for resolved-dead arms).
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - rpm's "shortcut" arm: pattern[0]=='"' && pattern[1]=='~'
 *    copies from &pattern[2] and then strips the LAST byte
 *    ("hack d-quotes", vendor 126) - the trailing quote is
 *    assumed, not checked; kept verbatim (an unterminated quote
 *    eats a real character - vendor bug kept as observable).
 *  - perlmatch's (MSV*, char* ignored, ...) shape: the ignored
 *    pointer param is kept as the nullable CharArray seat the
 *    C prototype gives it.
 *  - syntax_error's "%*c" with pos<1 would print just the
 *    caret in C; the Kotlin padding quantizes negative width to
 *    zero spaces (UB quantized, documented) - all live call
 *    sites pass pos>=1.
 *  - accept() falls through to accept_it even after
 *    syntax_error (vendor relies on sigint never returning;
 *    the Kotlin sigint stub throws, keeping the tail
 *    unreachable - same observable).
 *  - parse's non-EOT tail: syntax_error is the last statement
 *    before make_cstring in the vendor, but the C falls THROUGH
 *    only because sigint aborts - rendered verbatim: the
 *    makeCstring call stays after the error call, unreachable
 *    in both builds.
 */

private const val STR_MAX = 32000 // vendor strmax.h:3 (file-private, cf. unit 22)

/** vendor 52: #define DOUBLE_QUOTE '\xd2' - ASCII 210, the
 *  embedded-quote sentinel; used by leaning_toothpickize
 *  (batch-6 corpus unit). */
internal const val DOUBLE_QUOTE = 210.toChar()

// vendor 53-54: #define MAX_BACKREFS 99 - dead after the 2026
// PCRE-to-clib-regex conversion; no remaining uses. Not
// translated (h013).
// vendor 56: #define OFFSET_BUF_LEN (MAX_BACKREFS * 3 / 2 + 1) -
// the perl_pm offsets buffer length; dead in the conversion,
// not translated (h013).
// vendor 58: #define min(x,y) - unused anywhere in the file;
// not translated (h013).

/** vendor 109: static const char pattern_codes[] = "ACELNPU";
 *  (reconciled here from unit 104's provisional raise, h004). */
internal val PATTERN_CODES = "ACELNPU"

/** vendor 110-114: static const char * const char_classes[] -
 *  the POSIX class each pattern code maps to (reconciled, h004). */
internal val CHAR_CLASSES = arrayOf(
    "[:alpha:]", "[:cntrl:]", "[:print:][:cntrl:]",
    "[:lower:]", "[:digit:]", "[:punct:]", "[:upper:]"
)

/** new_bstring over a C string literal (call-shape helper;
 *  reconciled here from unit 104, h004). */
internal fun newBstringLit(s: String): Bstring {
    val buf = CharArray(s.length + 1)
    for (i in s.indices) buf[i] = s[i]
    buf[s.length] = NUL
    return newBstring(buf)
}

/** C toupper over the byte domain (C locale ASCII fold; h015:
 *  file-private - Parse_.kt:258 carries its own private
 *  toupperC, the arm's per-file convention for this helper). */
private fun toupperC(c: Char): Char = if (c in 'a'..'z') c - 32 else c

/**
 * The vendor's read of *current_char at the C terminator: a
 * Kotlin String has no NUL byte, so the byte at end-of-input
 * quantizes to NUL (h007 quirk note - the EOT case rides it).
 */
private fun charAtC(s: Scanner): Char =
    if (s.currentChar < s.input.length) s.input[s.currentChar] else NUL

/**
 * vendor 120-138: int rpm(unsigned char string[], const unsigned
 * char pattern[], struct MSV * svPtr) - the Mumps pattern-match
 * operator driver: the quoted-Perl shortcut arms go straight to
 * perl_pm, everything else parses the pattern into a regex
 * first. Signature keeps the offset-carrier shape the InterpStubs
 * stub gave it (Parse_.kt call sites bind unchanged).
 */
fun rpm(ad: CharArray, adOff: Int, bd: CharArray, bdOff: Int, svPtr: Msv): Int {
    val pcrePattern = CharArray(STR_MAX) // vendor: char pcre_pattern[STR_MAX]

    if (bd[bdOff] == '"' && bd[bdOff + 1] == '~') {
        // strcpy(pcre_pattern, &pattern[2]);
        var len = 0
        while (bd[bdOff + 2 + len] != NUL) {
            pcrePattern[len] = bd[bdOff + 2 + len]
            len++
        }
        pcrePattern[len] = NUL

        // hack d-quotes (vendor 126): strip the assumed trailing
        // quote - kept verbatim even when no quote is present
        pcrePattern[len - 1] = NUL

        return perlPm(ad, adOff, pcrePattern, 0, 0, svPtr)
    }

    val p = newParser(newScanner(cstringAt(bd, bdOff)))

    parse(p, pcrePattern)

    // vendor 135: if (PM_DEBUG) printf("%s\n%s\n", pattern,
    // pcre_pattern); - PM_DEBUG is 0 in the reference build
    // (include/mumpsc/pm.h:25), dead arm not translated (h018)

    return perlPm(ad, adOff, pcrePattern, 0, 0, svPtr)
}

/**
 * vendor 143-145: const char * perlmatch(struct MSV * svPtr,
 * char * ignored, char * string, char * pattern) - the direct
 * Perl-regex entry; returns "1"/"0" as the C literal shape.
 */
fun perlmatch(svPtr: Msv, ignored: CharArray?, string: CharArray, pattern: CharArray): String {
    return if (perlPm(string, 0, pattern, 0, 1, svPtr) != 0) "1" else "0"
}

/**
 * vendor 147-149: int perlcpp(char * ignored, const char * string,
 * const char * pattern, struct MSV * svPtr).
 */
fun perlcpp(ignored: CharArray?, string: CharArray, pattern: CharArray, svPtr: Msv): Int {
    return perlPm(string, 0, pattern, 0, 1, svPtr)
}

/**
 * vendor 235-244: struct scanner new_scanner(const char input[]) -
 * the fresh scanner over the pattern; the seed token is
 * kind=ERROR with an empty bstring text.
 */
fun newScanner(input: String): Scanner {
    val t = Token()
    t.kind = TokenType.ERROR
    t.text = newBstringLit("")
    val s = Scanner(input)
    s.currentToken = t
    return s
}

/**
 * vendor 250-253: struct token scan(struct scanner * s) -
 * resets the token text and rescans; returns the aliased
 * current_token (h007 reference semantics).
 */
fun scan(s: Scanner): Token {
    s.currentToken.text = newBstringLit("")
    s.currentToken.kind = scanToken(s) // h014 stub (owner unit 128)
    return s.currentToken
}

/**
 * vendor 302-304: void take_char(struct scanner * s) - append the
 * byte at current_char to the token text and advance.
 */
fun takeChar(s: Scanner) {
    bcatchr(s.currentToken.text, charAtC(s))
    s.currentChar++
}

/**
 * vendor 310-316: struct parser new_parser(struct scanner s) -
 * the vendor copies the scanner by value; Kotlin aliases it
 * (the caller never touches its copy afterwards - observably
 * equivalent, h007 note).
 */
fun newParser(s: Scanner): Parser {
    val result = Parser()
    result.currentToken.kind = TokenType.ERROR
    result.currentToken.text = newBstringLit("")
    result.s = s
    return result
}

/**
 * vendor 322-331: void parse(struct parser * p, char * s) - the
 * top parse: one pattern, then the EOT check and the C-string
 * landing in the caller's buffer.
 */
fun parse(p: Parser, s: CharArray) {
    p.currentToken = scan(p.s)
    val result = parsePattern(p)
    if (p.currentToken.kind != TokenType.EOT) {
        syntaxError("Syntax error in pattern", p.s.input, p.s.currentChar)
    }
    makeCstring(s, result)
}

/**
 * vendor 337-344: struct bstring parse_pattern(struct parser * p) -
 * {pattern_atom} while the head token is INT or DOT.
 */
fun parsePattern(p: Parser): Bstring {
    var result = newBstringLit("")

    while (p.currentToken.kind == TokenType.INT ||
        p.currentToken.kind == TokenType.DOT
    ) {
        result = bstrcat(result, parsePatternAtom(p)) // h014 stub (batch-9 owner)
    }
    return result
}

/**
 * vendor 350-354: struct bstring parse_pattern_atom(struct parser *
 * p) - count := parse_count, element := parse_pattern_element;
 * returns bstrcat(element, count): the element followed by its
 * quantifier (regex order; vendor argument order kept verbatim).
 */
fun parsePatternAtom(p: Parser): Bstring {
    val count = parseCount(p) // h014 stub (owner unit 135)
    val element = parsePatternElement(p) // DONE - unit 104

    return bstrcat(element, count) // h014 stub (batch-9 owner)
}

/**
 * vendor 447-457: struct bstring parse_pattern_atom_list(struct
 * parser * p) - atom, then a run of ',' atoms joined by '|'.
 */
fun parsePatternAtomList(p: Parser): Bstring {
    var result = parsePatternAtom(p)

    while (p.currentToken.kind == TokenType.COMMA) {
        acceptIt(p)
        bcatchr(result, '|')
        result = bstrcat(result, parsePatternAtom(p)) // h014 stub (batch-9 owner)
    }
    return result
}

/**
 * vendor 461-466: void accept(struct parser * p, const enum
 * token_type expected_kind) - the checked consume.
 */
fun accept(p: Parser, expectedKind: TokenType) {
    if (p.currentToken.kind != expectedKind) {
        syntaxError("Syntax error in pattern", p.s.input, p.s.currentChar)
    }
    acceptIt(p)
}

/**
 * vendor 472-475: void accept_it(struct parser * p) - the
 * unchecked consume: one token ahead.
 */
fun acceptIt(p: Parser) {
    p.currentToken = scan(p.s)
}

/**
 * vendor 501-505: void syntax_error(const char * msg, const char *
 * line, const int pos) - the caret trio on stdout, then
 * sigint(100) (owner interp-sysfunc.cpp, batch-7 stub).
 */
fun syntaxError(msg: String, line: String, pos: Int) {
    System.out.print("*** " + msg + "\n")
    System.out.print(line + "\n")
    // printf("%*c\n", pos, '^'): pos-1 spaces then the caret;
    // a negative padding quantizes to zero spaces (UB note)
    System.out.print(" ".repeat((pos - 1).coerceAtLeast(0)) + "^\n")
    sigint(100) // h014 stub (owner interp-sysfunc.cpp, batch-7)
}
