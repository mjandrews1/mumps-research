package doc

// Unit 152 (B5 tranche 3): interp-rpm.cpp::<misc> (batch 5,
// complex stratum; units.csv row 152: loc 145, score 68.1,
// rank 252). The pattern-matcher driver tier of the Lockner
// pm.c layer over rpm: rpm() and perlmatch()/perlcpp() delegate
// into perl_pm; beneath them the scanner/parser cores
// (new_scanner, scan, new_parser, parse, parse_pattern,
// parse_pattern_atom, parse_pattern_atom_list, accept,
// accept_it, take_char, syntax_error). The parser's
// parse_pattern_element/parse_count tiers are the earlier
// landed batch-5 units (ParsePatternElement.kt, ParseCount.kt)
// - H-004 keep-first. All derived from the pristine vendor
// source this session (sha256 621deb...ffdbe).
//
// H-004 boundary: perl_pm itself is batch-9 roster row 257
// (interp-rpm.cpp::perl_pm) - the rpm/perlmatch/perlcpp bodies
// here call the NEW Stubs.perlPm seat raised in this tranche
// (tagged to its batch-9 owner); leaning_toothpickize stays the
// Stubs.leaning_toothpickize seat (batch-6 owner); bstrcat stays
// the Stubs.bstrcat seat (batch-9 owner). The pm.h:25 define
// PM_DEBUG 0 rides here as a file-private const (its only chunk
// read is the dead rpm printf arm - kept as the vendor comment
// ledger); the perl_pm-owned defines (MAX_BACKREFS 99,
// OFFSET_BUF_LEN 149, DOUBLE_QUOTE 0xd2 of the embedded-quote
// hack) stay with their batch-9 owner unit and are NOT seated
// here. H-014 stubs flipped this unit: rpm, accept, accept_it,
// syntax_error, parse_pattern_atom_list (all previously Stubs
// throwers) and the take_char seat raised by unit 147 - all now
// delegates to this file. H-015: the whole-token C assignments
// p->current_token = scan(...) and t = s->current_token copy
// through a copyTokenInto helper (the val Bstring field of the
// token carrier precludes whole-object reassignment); the String
// input walk quantizes end-of-index as NUL exactly at
// take_char/take-note parity with the scan_token render.

// pm.h:25 define PM_DEBUG 0 (file-private: the only chunk read
// is the dead rpm printf arm below).
private const val PM_DEBUG = 0

// int rpm(unsigned char string[], const unsigned char
// pattern[], struct MSV *svPtr); interp-rpm.cpp:118. The
// pattern-match driver: the '"' '"'-tilt direct-perl arm keeps
// the pcre_pattern[strlen-1]=0 trailing-quote strip hack
// verbatim (the minimum three-char "x" walk keeps the write
// in-carrier; the vendor index arithmetic is width-kept).
fun rpm(string_: CharArray, pattern: CharArray, svPtr: Msv): Int {
    val pcrePattern = CharArray(STR_MAX)

    if (pattern[0] == '"' && pattern[1] == '~') {
        CstringOps.strcpyS(pcrePattern, CstringOps.cstringAt(pattern, 2))
        val len = CstringOps.cstring(pcrePattern).length
        pcrePattern[len - 1] = NUL // hack d-quotes
        return Stubs.perlPm(CstringOps.cstring(string_),
            CstringOps.cstring(pcrePattern), 0, svPtr)
    }

    val p = new_parser(new_scanner(CstringOps.cstring(pattern)))

    parse(p, pcrePattern)

    // if (PM_DEBUG) printf("%s\n%s\n", pattern, pcre_pattern);
    // - dead arm (PM_DEBUG 0), kept as the vendor comment ledger.

    return Stubs.perlPm(CstringOps.cstring(string_),
        CstringOps.cstring(pcrePattern), 0, svPtr)
}

// const char * perlmatch(struct MSV *svPtr, char *ignored,
// char *string, char *pattern); interp-rpm.cpp:139. The \u0024?
// direct-perl entry; the Boolean result folds to the "1"/"0"
// literal pair exactly as the vendor.
fun perlmatch(svPtr: Msv, ignored: String, string_: String,
        pattern: String): String {
    return if (Stubs.perlPm(string_, pattern, 1, svPtr) != 0) "1" else "0"
}

// int perlcpp(char *ignored, const char *string,
// const char *pattern, struct MSV *svPtr); interp-rpm.cpp:143
fun perlcpp(ignored: String, string_: String, pattern: String,
        svPtr: Msv): Int {
    return Stubs.perlPm(string_, pattern, 1, svPtr)
}

// The C whole-token assignments of this tier copy kind+text
// through this helper (the token.text carrier is a val).
private fun copyTokenInto(dst: token, src: token) {
    dst.kind = src.kind
    dst.text.length = src.text.length
    var i = 0
    while (i < src.text.length) {
        dst.text.string[i] = src.text.string[i]
        i++
    }
}

// struct scanner new_scanner(const char input[]);
// interp-rpm.cpp:149. The fresh-ERROR token shape of the class
// defaults is the vendor init spelled out.
fun new_scanner(input: String): scanner {
    val s = scanner()

    // t.kind = ERROR; t.text = new_bstring("");
    s.input = input
    s.currentChar = 0 // s.current_char = input;
    return s
}

// struct token scan(struct scanner *s); interp-rpm.cpp:161
fun scan(s: scanner): token {
    // s->current_token.text = new_bstring("");
    val scanned = scan_token(s)
    s.currentToken.kind = scanned // s->current_token.kind = scan_token(s);
    return s.currentToken
}

// static void take_char(struct scanner *s); interp-rpm.cpp:168
fun take_char(s: scanner) {
    val ch = if (s.currentChar < s.input.length) s.input[s.currentChar]
             else NUL
    bcatchr(s.currentToken.text, ch)
    s.currentChar++
}

// struct parser new_parser(struct scanner s); interp-rpm.cpp:174
fun new_parser(s: scanner): parser {
    val result = parser()

    // result.current_token.kind = ERROR;
    // result.current_token.text = new_bstring("");
    result.s.input = s.input
    result.s.currentChar = s.currentChar
    return result
}

// void parse(struct parser *p, char *s); interp-rpm.cpp:186.
// The EOT check keeps the vendor missing-return shape: after
// the syntax_error escalation (sigint inside) the vendor falls
// through to make_cstring; sigint terminates before the fall
// resumes (no Kotlin reachability claim beyond the kamikaze
// seat semantics, arm precedent).
fun parse(p: parser, s: CharArray) {
    scan(p.s)
    copyTokenInto(p.currentToken, p.s.currentToken)
    val result = parse_pattern(p)
    if (p.currentToken.kind != token_type.EOT) {
        syntax_error("Syntax error in pattern",
            p.s.input, p.s.currentChar)
    }
    makeCstring(s, result)
}

// struct bstring parse_pattern(struct parser *p);
// interp-rpm.cpp:198
fun parse_pattern(p: parser): Bstring {
    var result = newBstring(CharArray(1))

    while (p.currentToken.kind == token_type.INT ||
            p.currentToken.kind == token_type.DOT) {
        result = Stubs.bstrcat(result, parse_pattern_atom(p))
    }
    return result
}

// struct bstring parse_pattern_atom(struct parser *p);
// interp-rpm.cpp:208 (count rides beneath element, the vendor
// bstrcat(element, count) argument order kept)
fun parse_pattern_atom(p: parser): Bstring {
    val count = parse_count(p)
    val element = parse_pattern_element(p)

    return Stubs.bstrcat(element, count)
}

// struct bstring parse_pattern_atom_list(struct parser *p);
// interp-rpm.cpp:214
fun parse_pattern_atom_list(p: parser): Bstring {
    var result = parse_pattern_atom(p)

    while (p.currentToken.kind == token_type.COMMA) {
        accept_it(p)
        bcatchr(result, '|')
        result = Stubs.bstrcat(result, parse_pattern_atom(p))
    }
    return result
}

// void accept(struct parser *p, const enum token_type
// expected_kind); interp-rpm.cpp:223
fun accept(p: parser, expectedKind: token_type) {
    if (p.currentToken.kind != expectedKind) {
        syntax_error("Syntax error in pattern",
            p.s.input, p.s.currentChar)
    }
    accept_it(p)
}

// static void accept_it(struct parser *p); interp-rpm.cpp:230
fun accept_it(p: parser) {
    scan(p.s)
    copyTokenInto(p.currentToken, p.s.currentToken)
}

// static void syntax_error(const char msg[], const char line[],
// const int pos); interp-rpm.cpp:235. The %*c caret banner
// renders right-justified by the pos-1 space walk (H-001 printf
// mapping); the vendor "pos" is the character offset (the
// current_char - input walk of the call sites).
fun syntax_error(msg: String, line: String, pos: Int) {
    System.out.print("*** " + msg + "\n")
    System.out.print(line + "\n")
    var i = 1
    while (i < pos) {
        System.out.print(" ")
        i++
    }
    System.out.print("^\n")
    Stubs.sigint(100)
}
