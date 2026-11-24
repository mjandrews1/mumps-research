package daf

// Unit 152 (DAF B5 T3, complex): interp-rpm.cpp::<misc>
// (units.csv row 132: loc 145, ctl 25, score ~49, rank ~342-360,
// complex): the rpm pattern-match operator harness: the ?-operator
// rpm() with its raw-regex '"~-prefix hack path, perlmatch/perlcpp
// relays, the scanner/parser new_/scan/take_char/parse carriers of
// the pattern grammar, the parse_pattern/parse_pattern_atom/
// parse_pattern_atom_list rules, and the accept/accept_it/
// syntax_error recovery frame. scan_token itself is the unit-147
// render; parse_count/parse_pattern_element are the unit-136/113
// renders of B5 T2/B4 T2 - all bind by name here.
// Consults: BASE-log row 68 hit-reuse (struct scanner/parser
// by-value constructions fold onto the merged DafParser class
// instance - new_parser's by-value scanner copy becomes a fresh
// instance with the field copies ledged), row 37 hit-adapt (the
// bstring ops new_bstring/bstrcat/bcatchr KEEP their roster-unit-22
// named-owner stubs: same-TU calls, out-of-scope owner; the seats
// bind when the DAF cycle reaches that batch cell), row 90
// hit-reuse (same-TU static cross-references resolve within this
// tranche: acceptItD/acceptD/syntaxErrorD/parsePatternAtomListD
// stub seats of RpmParserStubsD are DELETED here and their real
// bodies render in this file - the callers in ScanToken.kt,
// ParseCount.kt and ParsePatternElement.kt bind unchanged), row 59
// hit-adapt (the perl_pm relay folds onto the named-owner stub
// perlPmD: owner roster row 256 b9, outside the DAF batch
// assignment), row 83 hit-adapt (pm.c-derived KDoc banners of the
// 2026 pcre->clib-regex conversion note kept as the provenance
// header comment; DOUBLE_QUOTE \xd2 / MAX_BACKREFS 99 /
// OFFSET_BUF_LEN / min-macro are dead defines of the retired PCRE
// plumbing - KDoc ledger only, not emitted), row 46/33 no-touch
// (make_cstring is a bstring-family helper outside this unit's
// dump - named-owner stub makeCstringD below, roster sibling of
// unit 22; the stub gates the parse exit with the bstring ops).
// PM_DEBUG pinned 0 (pm.h:25): the debug printf of rpm() stays
// render-visible but constant-false (if (PM_DEBUG_D) with const 0).
// Dafny: EXEMPT (parser-harness relay over the stubbed bstring
// family; the T2 unit-136/113 exemption family).
// Quirk ledger (kept verbatim): rpm()'s '"~ raw-regex hack path
// strcpy's past the double quote pair and NULs the LAST char
// (dropping the closing quote) before perl_pm; perlmatch returns
// "1"/"0" STRINGS (not ints) and takes its string/pattern in the
// (ignored, string, pattern) arg order; syntax_error prints the
// caret line by the %*c pad and exits via sigint(100) (the T1
// sigintDaf seat); parse()'s trailing-kind check reports any
// residue as "Syntax error in pattern".

const val PM_DEBUG_D = 0 // #define PM_DEBUG 0 (pm.h:25)

// int perl_pm(const char[], const char[], const int, struct MSV *)
// - RELEASED batch 9 unit 268 (roster row 268; interp-rpm.cpp::
// perl_pm, core, LOC 51): TAG CORRECTION - the raised stub's
// blame read "roster row 256"; the b9 roster cell is row 268.
// Vendor shape kept: reg <- "^" + pattern + "\u0024" (the anchor wrap
// over the char reg[512] carrier - a bounds-checked copy, the
// DOCUMENTED DIVERGENCE for patterns over 509 bytes), the
// regcomp ladder of POSIX error codes folds onto the JVM regex:
// POSIX ERE -> java.util.regex.Pattern (the PatternSyntaxException
// arm prints the regcomp parse-error banner then the
// sigint(100) exit fold of the syntax_error precedent), regexec
// over the anchored string, regfree the GC no-op (row 22), and
// the vendor return inversion kept verbatim: no-match
// (REG_NOMATCH 1) returns 0, match returns 1; allow_partial
// stays the vendor's dead param (H-013, never read).
fun perlPmD(string: String, pattern: String, flag: Int, svPtr: DafMsv): Int {
    val reg = StringBuilder("^")
    for (q in pattern.indices) {
        if (reg.length >= 511) break
        reg.append(pattern[q])
    }
    if (reg.length < 512) reg.append('$')

    val p: java.util.regex.Pattern
    try {
        p = java.util.regex.Pattern.compile(reg.toString())
    } catch (e: java.util.regex.PatternSyntaxException) {
        println("\n*** regcomp() parse error on regex")
        sigintD(100)
        return 0 // unreachable in the vendor: sigint exits; JVM keep
    }

    return if (p.matcher(string).matches()) 1 else 0
}

// void make_cstring(char *, struct bstring) - bstring-family TU
// helper outside this unit's dump; named-owner stub (roster
// sibling of unit 22's cell).
fun makeCstringD(out: CharArray, b: DafBstring): Unit =
    throw NotImplementedError(
        "make_cstring: out-of-scope owner (bstring-family TU helper; not a corpus unit)")

// struct scanner new_scanner(const char[]) - the scanner carrier;
// the parser class DafParser merges the vendor struct
// scanner/parser pair (row 68 collapse; the T2 RpmParserStubsD
// precedent).
fun newScannerD(input: CharArray): DafParser {
    val s = DafParser()
    s.currentTokenKind = TOKEN_ERROR_D // t.kind = ERROR
    s.currentTokenText = newBstringD("") // t.text = new_bstring("")
    s.input = input // s.input = input
    s.currentChar = 0 // s.current_char = input (index 0)
    return s
}

// struct token scan(struct scanner*) - refresh the current token.
fun scanD(s: DafParser) {
    s.currentTokenText = newBstringD("")
    s.currentTokenKind = scanTokenD(s)
}

// void take_char(struct scanner*) - append the char under the
// cursor to the token text and advance (the unit-147 scan_token
// caller seat).
fun takeCharD(s: DafParser) {
    bcatchrD(s.currentTokenText, at(s.input, s.currentChar))
    s.currentChar++
}

// struct parser new_parser(struct scanner) - the by-value scanner
// copy (row 68 ledger: the merged-class fresh instance carries the
// copy semantics; input rides the array reference).
fun newParserD(sc: DafParser): DafParser {
    val p = DafParser()
    p.currentTokenKind = TOKEN_ERROR_D
    p.currentTokenText = newBstringD("")
    p.input = sc.input
    p.currentChar = sc.currentChar
    return p
}

// void parse(struct parser*, char*) - the grammar entry: scan the
// first token, parse the pattern, demand EOT, copy the result out.
fun parseRpmD(p: DafParser, out: CharArray) {
    scanD(p) // p->current_token = scan(&p->s)
    val result = parsePatternD(p)
    if (p.currentTokenKind != TOKEN_EOT_D) { // if kind != EOT
        syntaxErrorD("Syntax error in pattern",
            p.input, p.currentChar) // current_char - input (index)
    }
    makeCstringD(out, result)
}

// struct bstring parse_pattern(struct parser*) - {pattern_atom}*
// over the INT/DOT heads.
fun parsePatternD(p: DafParser): DafBstring {
    var result = newBstringD("")
    while (p.currentTokenKind == TOKEN_INT_D ||
        p.currentTokenKind == TOKEN_DOT_D) {
        result = bstrcatD(result, parsePatternAtomD(p))
    }
    return result
}

// struct bstring parse_pattern_atom(struct parser*) - the
// {count}{element} pair; NOTE the vendor concatenation order is
// element-then-count (kept verbatim).
fun parsePatternAtomD(p: DafParser): DafBstring {
    val count = parseCountD(p)
    val element = parsePatternElementD(p)
    return bstrcatD(element, count)
}

// struct bstring parse_pattern_atom_list(struct parser*) - the
// COMMA-separated alternation; real body (the RpmParserStubsD seat
// is released here; the ParsePatternElement.kt caller binds
// unchanged).
fun parsePatternAtomListD(p: DafParser): DafBstring {
    var result = parsePatternAtomD(p)
    while (p.currentTokenKind == TOKEN_COMMA_D) {
        acceptItD(p)
        bcatchrD(result, '|')
        result = bstrcatD(result, parsePatternAtomD(p))
    }
    return result
}

// void accept(struct parser*, const enum token_type) - expect and
// advance; real body (the RpmParserStubsD seat is released here).
fun acceptD(p: DafParser, expectedKind: Int) {
    if (p.currentTokenKind != expectedKind) {
        syntaxErrorD("Syntax error in pattern",
            p.input, p.currentChar)
    }
    acceptItD(p)
}

// void accept_it(struct parser*) - advance; real body (the
// RpmParserStubsD seat is released here).
fun acceptItD(p: DafParser) {
    scanD(p)
}

// static void syntax_error(const char[], const char[], const int)
// - the diagnostic; real body (seat released here): the %*c caret
// pad folds onto a space fill; sigint(100) via the T1 sigintDaf.
fun syntaxErrorD(msg: String, input: CharArray, offset: Int) {
    print("*** $msg\n") // printf("*** %s\n", msg)
    print("${DafCRuntime.cstring(input)}\n") // printf("%s\n", line)
    val pad = " ".repeat(if (offset > 0) offset - 1 else 0)
    print("$pad^\n") // printf("%*c\n", pos, '^')
    sigintDaf(100)
}

// int rpm(unsigned char [], const unsigned char [], struct MSV *)
// - the '?' operator: hack path for '"~'-prefixed raw regexes; else
// the parse-and-relay. PM_DEBUG printout constant-false (pin 0).
fun rpmD(string: CharArray, pattern: CharArray, svPtr: DafMsv): Int {
    val pcrePattern = CharArray(STR_MAX_DAF) // char pcre_pattern[STR_MAX]

    if (pattern[0] == '"' && at(pattern, 1) == '~' ) {
        // strcpy(pcre_pattern, &pattern[2]) then drop the closing
        // quote: copy up to the last char (row 56 explicit-bound
        // copy; the vendor NULs the final quote position)
        var i = 2
        while (i < pattern.size && pattern[i] != '\u0000' && i - 2 < STR_MAX_DAF) {
            pcrePattern[i - 2] = pattern[i]
            i++
        }
        if (i - 2 > 0) pcrePattern[i - 2 - 1] = '\u0000'
        return perlPmD(DafCRuntime.cstring(string),
            DafCRuntime.cstring(pcrePattern), 0, svPtr)
    }

    val p = newParserD(newScannerD(pattern))
    parseRpmD(p, pcrePattern)

    if (PM_DEBUG_D != 0) { // if (PM_DEBUG) ... (pin 0, dead)
        print("${DafCRuntime.cstring(pattern)}\n")
        print("${DafCRuntime.cstring(pcrePattern)}\n")
    }

    return perlPmD(DafCRuntime.cstring(string),
        DafCRuntime.cstring(pcrePattern), 0, svPtr)
}

// const char *perlmatch(struct MSV *, char*, char*, char*) - the
// direct-Perl-regex relay; NOTE the arg order (svPtr, ignored,
// string, pattern) and the "1"/"0" STRING returns.
fun perlmatchD(svPtr: DafMsv, ignored: String, string: String,
    pattern: String): String {
    return if (perlPmD(string, pattern, 1, svPtr) != 0) "1" else "0"
}

// int perlcpp(char*, const char*, const char*, struct MSV *) - the
// cpp-side relay (flag 1).
fun perlcppD(ignored: String, string: String, pattern: String,
    svPtr: DafMsv): Int {
    return perlPmD(string, pattern, 1, svPtr)
}
