package both

// Unit 161 (BOTH B6 T1, simple): libmpscpp.cpp::ScanAlnum (18
// LOC) - the ISTREAM overload. Batch_plan ordinal 161: simple,
// LOC 18, score 8.4, complexity rank 73. OVERLOAD
// DISAMBIGUATION (the units.csv duplicate-id ledger, metrics not
// file order): the vendor file carries three ScanAlnum bodies;
// the FILE* overload (vendor line 10060) carries a pointer mark
// (FILE *u) and matches the batch-4 rank-105 row (unit 104); the
// MSV* interpreter-lexical overload (line 10112, 17 LOC) is the
// batch-3 rank-127 row; THIS render is the istream overload
// (vendor line 10086, signature "mstring ScanAlnum(istream u,
// int min, int max)"), the 18-LOC zero-pointer-marks body the
// batch-6 rank-73 row names.
//
// Vendor behavior: a whitespace-delimited token is extracted
// from the stream ("u >> word", the 8191-byte word carrier); a
// failed extraction OR max > 511 returns the empty mstring; the
// alnum bytes of the word are collected lowercased into tmp1; a
// token is rejected and the scan RETRIES when tmp1 is empty,
// shorter than min, longer than max, or starts with a digit; the
// accepted token is returned.
//
// Quirks kept (ledgered):
//   - the locale quantization: C isalnum/tolower over the
//     single-byte default locale fold to the ASCII letter/digit
//     domain (C locale semantics; high bytes are not alnum and
//     pass through unchanged), the divergence journaled.
//   - the max > 511 guard discards a SUCCESSFULLY extracted
//     word (the extraction runs first; the short-circuit order
//     is kept: extract, then guard, then return).
//   - the mstring return folds to String (the mstringLength
//     precedent, h019 naming).
//   - the 8191-byte word cap: the C extraction silently
//     truncates at 8191 bytes; the reader seat below extracts
//     the token unbounded then applies the alnum filter (the
//     length cap rides the tmp1 max test, not the extraction);
//     noted as the seat divergence.
//
// The scanner's char domain (DB.BothCRuntime) seats: the istream
// carrier folds onto java.io.Reader (the h020 stream-handle
// family; Reader.read feet are the extraction walk).
//
// BOTH consults (find_rule before render, events logged):
//   h015 grammar_collision - reused and hit: the ASCII alnum
//     domain quantization is seat-local (isAlnumC/lowerC below);
//     no carrier name collides with the arm namespace.
//   h017 naming - reused and hit: ScanAlnum keeps the vendor
//     name (house capitalized style); the goto-repeat labels fold
//     to the while loop.
//   h020 pointer_handle - reused and hit (first consult-as-rule):
//     the C++ istream stream has no JVM pointer counterpart and
//     no address semantics; the carrier is the java.io.Reader
//     handle seat per the h020 action, journalseated with the
//     FILE*-family provenance (fgetcC seat precedent,
//     BothCRuntime.kt).

internal fun ScanAlnum(u: java.io.Reader, min: Int, max: Int): String {
    while (true) {
        // u >> word: skip whitespace, then read the token
        var ch = u.read()
        while (ch != -1 && isWsC(ch.toChar())) ch = u.read()
        if (ch == -1) return "" // !(u >> word)
        val word = StringBuilder()
        while (ch != -1 && !isWsC(ch.toChar())) {
            word.append(ch.toChar())
            ch = u.read()
        }
        if (max > 511) return "" // the extracted word is discarded

        val tmp1 = StringBuilder()
        for (c in word) if (isAlnumC(c)) tmp1.append(lowerC(c))

        if (tmp1.isEmpty() || tmp1.length < min || tmp1.length > max ||
            isDigitC(tmp1[0])) continue // goto repeat

        return tmp1.toString()
    }
}

// C locale seats (locale quantization ledgered above):
// whitespace = space, tab, newline, vertical tab, form feed,
// carriage return (the C++ default extraction skip set).
private fun isWsC(c: Char): Boolean =
    c == ' ' || c == '\u0009' || c == '\u000A' || c == '\u000B' ||
    c == '\u000C' || c == '\u000D'

private fun isAlnumC(c: Char): Boolean =
    (c in 'a'..'z') || (c in 'A'..'Z') || isDigitC(c)

private fun isDigitC(c: Char): Boolean = c in '0'..'9'

private fun lowerC(c: Char): Char =
    if (c in 'A'..'Z') (c + ('a' - 'A')) else c
