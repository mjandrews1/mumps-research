package doc

// Unit 77 (B3 tranche 2): libmpscpp.cpp::ScanAlnum (batch 3, core
// stratum; LOC 17, score 13.1). The MSV* overload of the ScanAlnum
// restartable word filter: repeat-draw words from Scan (unit 75, now
// the real function - the H-008 fresh-String carrier basis) until one
// starts with an alphanumeric byte, then fold the lowercased
// alphanumerics into the result and set tpx. Vendor quirks kept and
// noted: ep and the defaulted toklist (NULL) parameters are never
// read (H-013 dead parameters, kept verbatim in the signature); the
// k local is dead (H-013); the "for (i = 0; i < strlen(tmp1); i++)"
// loop recomputes strlen each iteration in C (kept in shape: the
// String render walks a fixed-length copy, the recompute quirk
// ledgered); the byte filters isalnum/tolower render as the ASCII
// domain helpers (H-012 byte-domain mapping; the vendor feeds signed
// char to isalnum where high bytes are UB in C - the render sees only
// the non-negative Kotlin Char domain, divergence noted). The goto
// again renders as the H-011 while(true) + continue structure. The
// vendor static char tmp/tmp1[1024] carriers fold per H-008 (the
// result is copied out before the next Scan call - no aliasing).
// The FILE*/istream ScanAlnum overloads in the same vendor scope are
// not corpus units and are not rendered (ledgered). Naming per H-017.

fun ScanAlnum(svptr: Msv, ep: CharArray, toklist: CharArray?): String {
    // vendor dead locals: k (H-013)
    var tmp1: String

    while (true) {
        tmp1 = Scan(svptr)

        if (tmp1.isEmpty()) {
            svptr.tpx = 0
            return tmp1
        }

        if (!isAlnumByte(tmp1[0])) continue

        val tmp = StringBuilder()
        for (element in tmp1) {
            if (isAlnumByte(element)) tmp.append(toLowerByte(element))
        }

        svptr.tpx = 1
        return tmp.toString()
    }
}

// C isalnum over the byte domain (ASCII letters and digits; H-012);
// the vendor signed-char UB for high bytes is absent in this domain.
private fun isAlnumByte(c: Char): Boolean =
    c in 'a'..'z' || c in 'A'..'Z' || c in '0'..'9'

// C tolower over the byte domain (ASCII only; no locale tail).
private fun toLowerByte(c: Char): Char =
    if (c in 'A'..'Z') (c.code + 32).toChar() else c

// Unit 104 (B4 tranche 2): libmpscpp.cpp::ScanAlnum (batch 4, core
// stratum; row: LOC 18, score 10.6, rank 105; the FILE* overload).
// TAG CORRECTION on the unit-77 ledger note above: the FILE* (and,
// for a later batch, the istream) ScanAlnum overloads ARE corpus
// roster rows - this unit renders the FILE* one (overload
// disambiguation by parameter shape: the 23-LOC twin rule of the
// Transpose family does not apply here; this overload is pinned by
// its FILE* first parameter against the MSV* unit-77 render, whose
// ep/Char carrier keeps the three-argument shape). The FILE* renders
// per the arm-established Reader carrier (unit-48 getstr2 / unit-75
// Scan precedent; H-001 io-stream-param). The Sharp wlan: fscanf
// u,8191s,word reads the next whitespace-delimited run capped at
// 8191 chars; the max > 511 guard returns the empty string; the
// word filter keeps alphanumerics lowercased and restarts (the C
// goto repeat renders as the H-011 while(true) + continue) while
// the word is empty, under min, over max, or starts with a digit.
// The C static char word/tmp1[8192] carriers fold per H-008 to
// per-call CharArrays (no caller can alias a static between calls;
// the restart re-runs are within the single call). The byte filters
// render per H-012 as the ASCII domain helpers (the isAlnumByte /
// toLowerByte seats of unit 77 ride here per file - the helpers are
// duplicated per-file per the one-owner rule; signed-char UB of the
// C isalnum domain is absent in the Char carrier, divergence
// noted). return tmp1 shape: the vendor assigns the mstring from
// the NUL-terminated buffer; the render returns Mstring(cstring).
// Kotlin name ScanAlnum overloads the unit-77 MSV* render by
// parameter types (arity-and-shape resolution at call sites).
// Naming per H-017.

fun ScanAlnum(u: java.io.Reader, min: Int, max: Int): Mstring {
    val word = CharArray(8192)
    val tmp1 = CharArray(8192)

    while (true) { // repeat:
        // fscanf(u, "%8191s", word) or max > 511 -> empty mstring
        val tok = scanWord8k(u)
        if (tok == null || max > 511) {
            return Mstring("")
        }
        CstringOps.strcpyS(word, tok)

        var j = 0
        var i = 0
        while (word[i] != NUL) {
            if (isAlnum8(i, word)) tmp1[j++] = toLower8(word[i])
            i++
        }

        // j == 0 || j < min || j > max || isdigit(tmp1[0]) -> goto repeat
        if (j == 0 || j < min || j > max || isDigit8(tmp1[0])) continue

        tmp1[j] = NUL
        return Mstring(CstringOps.cstring(tmp1))
    }
}

// fscanf "%8191s": skip C whitespace (space, tab, newline, vertical
// tab, form feed, carriage return), read the next non-white run
// capped at 8191 chars; null at EOF before any char (the EOF arm
// of the vendor's fscanf comparison).
private fun scanWord8k(reader: java.io.Reader): String? {
    fun isWs(c: Int): Boolean = c == 32 || (c in 9..13)

    var c = reader.read()
    while (c != -1 && isWs(c)) c = reader.read()

    if (c == -1) return null

    val sb = StringBuilder()
    while (c != -1 && !isWs(c) && sb.length < 8191) {
        sb.append(c.toChar())
        c = reader.read()
    }
    return sb.toString()
}

// per-file isalnum/tolower/isdigit ASCII-domain helpers (H-012;
// the unit-77 family renders these per one-owner file).
private fun isAlnum8(idx: Int, a: CharArray): Boolean {
    val c = a[idx].code
    return (c in 48..57) || (c in 65..90) || (c in 97..122)
}

private fun toLower8(c: Char): Char =
    if (c.code in 65..90) (c.code + 32).toChar() else c

private fun isDigit8(c: Char): Boolean = c.code in 48..57
