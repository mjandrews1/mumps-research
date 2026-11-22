package both

// Unit 152 (BOTH B5 T3, complex): interp-rpm.cpp::<misc> (145 LOC
// residue). Batch_plan ordinal 152: complex, LOC 145, score 92.5.
// Content: the RPM TU's residue chunk - the banner, the include
// block, the pattern-double-quote and backreference macros, the
// enum/carrier declarations and the pattern-parser body family:
// rpm, perlmatch, perlcpp, new_scanner, scan, take_char,
// new_parser, parse, parse_pattern, parse_pattern_atom,
// parse_pattern_atom_list, accept, accept_it, syntax_error (the
// separated scan_token, parse_count, parse_pattern_element,
// leaning_toothpickize and perl_pm are their own roster units).
//
// THIS RENDER FLIPS THE acceptIt SEAT: the T2 ParseCount.kt stub
// (owner interp-rpm.cpp::<misc>, pending) lands its owner with this
// unit; the ParseCount.kt acceptIt body flips to the delegate
// scan cycle below (the h014 delegate-when-owner-lands mechanism).
//
// Vendor behavior: the macros seat as constants (DOUBLE_QUOTE is
// the ASCII-210 embedded-quote byte per the h015 domain; the
// backref and offset-buffer macros ride the leaning_toothpickize
// owner unit); the enum token_type and the token carriers already
// seat at the T2 ParseCount.kt declarations (grammar shared; the
// parser struct's scanner field seats as the Parser.s extension
// below - the T2 carrier carried current_token only because no
// parser body existed yet); new_scanner seeds the ERROR token over
// the input; scan reuses the ScanToken.kt classifier and rebuilds
// the text bstring per token; take_char appends through the
// bstring family and advances; parse scans once then concatenates
// the parsed pattern and make_cstring-s the result onto the caller
// buffer; parse_pattern loops the atom concatenation over INT/DOT;
// parse_pattern_atom is count-after-element (the vendor returns
// bstrcat(element, count) - the ELEMENT text precedes the count
// braces in the pattern order but the vendor concatenates
// element-then-count); parse_pattern_atom_list separates
// alternation atoms with the pipe byte; accept checks and advances;
// accept_it advances unconditionally; syntax_error prints the
// three faces and sigint(100)s.
//
// rpm behavior: the perl-direct hack (pattern starts quote-tilde)
// strips the wrapper quotes and delegates to perl_pm directly;
// the parse route builds the regex through a fresh parser; the
// PM_DEBUG face is dead (pm.h:25 pins PM_DEBUG 0 - h018/h013
// note); perlmatch and perlcpp wrap perl_pm in the two vendor
// shapes.
//
// Quirks kept (ledgered):
//   - the PM_DEBUG printf stays as the dead conditional comment
//     (the vendor face kept verbatim; the reference build config
//     pins the macro 0).
//   - parse_pattern's loop condition token order kept; the atom
//     concatenation order kept (element, count) even though the
//     grammar reads count-first.
//   - min macro and NITEMS: not exercised by any body of this
//     chunk (ledger note; h002 machinery applies at their future
//     call sites).
//
// BOTH consults (find_rule before render, events logged):
//   h002 simple_output_macro - consulted: no output macro occurs
//     in this chunk (ledger above).
//   h007 char_buffer - reused: pcre_pattern is the STR_MAX scratch
//     (String fold); the bstring carriers ride the T2 seats.
//   h015 grammar_collision - reused: DOUBLE_QUOTE is the ASCII 210
//     byte (Char(210) domain fold); the pipe and brace text bytes
//     are plain literals; no star-slash or dollar bytes occur.
//   h013 dead_store - hit: the PM_DEBUG face dropped (macro 0).
//   h014 out_of_scope_call - reused: perl_pm rides its new TODO
//     stub (owner interp-rpm.cpp::perl_pm, roster 214, batch 9);
//     parse_pattern_element rides its new TODO stub (owner
//     interp-rpm.cpp::parse_pattern_element, roster 196, batch 4);
//     leaning_toothpickize rides its new TODO stub (owner
//     interp-rpm.cpp::leaning_toothpickize, roster 157, batch 6);
//     make_cstring rides its new TODO stub (owner the
//     interp-bstring.cpp residue, roster 212, batch 1); the
//     bstring family rides the T2 ParseCount.kt stubs; sigint
//     rides the BothCRuntime seat; scan_token rides the same-TU
//     ScanToken.kt render; parse_count rides the T2 render.
//   h017 naming - reused: snake->camel only (take_char->take_char
//     keyword keeps the arm mapping note: the identifier is a
//     multiword snake name kept per the h017 always-document rule;
//     new_scanner->newScanner, new_parser->newParser,
//     parse_pattern->parsePattern, accept_it->acceptIt).

internal const val RPM_DOUBLE_QUOTE = '\u00D2' // '\xd2' ASCII 210 (h015 domain)
internal const val RPM_MAX_BACKREFS = 99 // pm.h-adjacent macro ledger
internal val RPM_OFFSET_BUF_LEN = RPM_MAX_BACKREFS * 3 / 2 + 1 // macro

// struct scanner (interp-rpm.cpp:81-85): the input walk.
internal class Scanner(
    val input: String,
    var pos: Int,
    var currentToken: RpmToken,
)

// *(s->current_char) fold: the NUL past the end (the C string
// terminator walk keeps the EOT contract of the classifier).
internal fun Scanner.currentChar(): Char {
    return if (pos < input.length) input[pos] else 0.toChar()
}

// take_char (interp-rpm.cpp:169-172): takeChar per h017: append the pointed char to
// the current token text and advance the walk.
internal fun takeChar(s: Scanner) { // h017: multiword snake kept
    s.currentToken = RpmToken(
        s.currentToken.kind,
        bcatchr(s.currentToken.text, s.currentChar()), // bstring seat
    )
    s.pos++
}

// struct scanner new_scanner(const char[]) - real body (this unit).
internal fun newScanner(input: String): Scanner {
    return Scanner(input, 0, RpmToken(TokenType.ERROR, newBstring("")))
}

// struct token scan(struct scanner *) - real body (this unit).
internal fun scan(s: Scanner): RpmToken {
    val kind = scanToken(s) // the unit-147 classifier (same TU)
    s.currentToken = RpmToken(kind, newBstring("")) // text rebuild
    return s.currentToken
}

// struct parser new_parser(struct scanner) - real body (this unit).
internal fun newParser(s: Scanner): Parser {
    val result = Parser(RpmToken(TokenType.ERROR, newBstring(""))) // carrier
    result.s = s
    return result
}

// void parse(struct parser *, char *) - the bstring result folds to
// the String return (make_cstring seats the caller buffer write).
internal fun parse(p: Parser): String {
    p.currentToken = scan(p.s!!) // p->current_token = scan(&p->s)
    val result = parsePattern(p)
    if (p.currentToken.kind != TokenType.EOT) {
        syntaxError(
            "Syntax error in pattern",
            p.s!!.input, p.s!!.pos,
        )
    }
    return make_cstring(result)
}

// struct bstring parse_pattern(struct parser *) - real body.
internal fun parsePattern(p: Parser): Bstring {
    var result: Bstring = newBstring("")
    while (p.currentToken.kind == TokenType.INT ||
        p.currentToken.kind == TokenType.DOT
    ) {
        result = bstrcat(result, parsePatternAtom(p))
    }
    return result
}

// struct bstring parse_pattern_atom(struct parser *) - real body.
internal fun parsePatternAtom(p: Parser): Bstring {
    val count = parseCount(p) // the T2 unit-143 render (same TU)
    val element = parse_pattern_element(p) // stub (roster 196, b4)
    return bstrcat(element, count)
}

// struct bstring parse_pattern_atom_list(struct parser *) - real.
internal fun parsePatternAtomList(p: Parser): Bstring {
    var result = parsePatternAtom(p)
    while (p.currentToken.kind == TokenType.COMMA) {
        acceptIt(p)
        result = bcatchr(result, '|') // the pipe byte seat
        result = bstrcat(result, parsePatternAtom(p))
    }
    return result
}

// void accept(struct parser *, const enum token_type) - real.
internal fun accept(p: Parser, expectedKind: TokenType) {
    if (p.currentToken.kind != expectedKind) {
        syntaxError("Syntax error in pattern", p.s!!.input, p.s!!.pos)
    }
    acceptIt(p)
}

// void syntaxError(const char[],
//                  const char *, const int) - real body (this unit).
internal fun syntaxError(msg: String, line: String, pos: Int) {
    System.out.print("*** " + msg + "\n")
    System.out.print(line + "\n")
    System.out.print(" ".repeat(pos - 1) + "^\n") // printf("%*c",pos,'^')
    sigint(100) // the BothCRuntime seat (never returns in vendor)
}

// int perl_pm(const char[], const char[], const int, MSV *) -
// owner: interp-rpm.cpp::perl_pm, roster 214 (batch 9, pending).
// TODO-stub (h014).
internal fun perl_pm(string: String, pattern: String, flg: Int, sv: Msv): Int {
    TODO("stub: owner interp-rpm.cpp::perl_pm (roster 214, batch 9)")
}

// struct bstring parse_pattern_element(struct parser *) - owner:
// interp-rpm.cpp::parse_pattern_element, roster 196 (batch 4,
// pending). TODO-stub (h014).
internal fun parse_pattern_element(p: Parser): Bstring {
    TODO("stub: owner interp-rpm.cpp::parse_pattern_element (roster 196, batch 4)")
}

// struct bstring leaning_toothpickize(const struct bstring) -
// owner: interp-rpm.cpp::leaning_toothpickize, roster 157 (batch
// 6, pending). TODO-stub (h014).
internal fun leaning_toothpickize(b: Bstring): Bstring {
    TODO("stub: owner interp-rpm.cpp::leaning_toothpickize (roster 157, batch 6)")
}

// void make_cstring(char *, const struct bstring) - owner: the
// interp-bstring.cpp residue, roster 212 (batch 1, pending). The
// out-param fold returns the String. TODO-stub (h014).
internal fun make_cstring(b: Bstring): String {
    TODO("stub: owner interp-bstring.cpp::<misc> (roster 212) make_cstring")
}

// int rpm(unsigned char [], const unsigned char [], MSV *) - the
// pattern-match entry (this unit). The tilde-quote perl-direct hack
// and the parse route both land on perl_pm.
internal fun rpm(string: String, pattern: String, svPtr: Msv): Int {
    if (pattern.length >= 2 && pattern[0] == '"' && pattern[1] == '~') {
        var pcrePattern = pattern.substring(2) // strcpy(&pcre_pattern, &pattern[2])
        // pcre_pattern[strlen - 1] = 0 hack (strip the closing
        // quote; the empty-after-strip case cannot occur: the
        // vendor strip is unconditional - kept)
        if (pcrePattern.isNotEmpty()) {
            pcrePattern = pcrePattern.substring(0, pcrePattern.length - 1)
        }
        return perl_pm(string, pcrePattern, 0, svPtr)
    }

    val p = newParser(newScanner(pattern))
    val pcrePattern = parse(p)

    // if (PM_DEBUG) printf("%s\n%s\n", pattern, pcre_pattern);
    //   - dead face (pm.h:25 pins PM_DEBUG 0; h013/h018 note).

    return perl_pm(string, pcrePattern, 0, svPtr)
}

// const char * perlmatch(MSV *, char *, char *, char *) - wrapper.
internal fun perlmatch(svPtr: Msv, ignored: String, string: String, pattern: String): String {
    return if (perl_pm(string, pattern, 1, svPtr) != 0) "1" else "0"
}

// int perlcpp(char *, const char *, const char *, MSV *) - wrapper.
internal fun perlcpp(ignored: String, string: String, pattern: String, svPtr: Msv): Int {
    return perl_pm(string, pattern, 1, svPtr)
}
