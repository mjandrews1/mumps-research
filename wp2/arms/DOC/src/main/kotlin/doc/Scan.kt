package doc

// Unit 75 (B3 tranche 2): libmpscpp.cpp::Scan (batch 3, core stratum;
// LOC 16, score 12.6). The whitespace-delimited word reader over the
// in_file Reader at the current io lun (H-001 io-stream-param: the
// input direction of the FILE* map rides the arm-established Reader
// carrier, the getstr2 unit-48 precedent). H-008 application: the
// C static char tmp[1024] is a per-call scratch here - the sibling
// ScanAlnum (unit 77) strcpy-copies the result before any further Scan
// call, so no caller aliases the buffer - which clears the H-008
// condition and the render returns a fresh String per call (EOF NULL
// in returns the empty string). Vendor quirks kept and noted: j is
// initialized and never used (H-013 dead local); the "word too long"
// test compares the fscanf ITEM COUNT against 1023 - fscanf returns 1
// here, so the branch can never fire and the INPUT_ERROR raise it
// guards is unreachable in the vendor too (H-013 dead check, kept as
// this ledger note, NOT translated); tmp[1023]=0 after the 1024-char
// clamp is a redundant terminator in C and a no-op against the String
// render (also ledgered); the underlying C fscanf "%1024s" can write
// 1024 chars + NUL into the 1024 buffer (one-byte overflow by the
// vendor, absent here by the String carrier, divergence ledgered per
// H-007). tpx 0/1 set per the vendor; a closed lun is a vendor NULL
// dereference, rendered as the raised MumpsGlobalException (Sw.kt
// outw precedent). Naming per H-017.

fun Scan(svptr: Msv): String {
    val reader = svptr.in_file[svptr.io]
        ?: throw MumpsGlobalException("Scan: in_file[io] not open")

    // fscanf(svptr->in_file[io], "%1024s", tmp): skip whitespace, read
    // the next non-white run capped at 1024 chars; null at EOF before
    // any char (the EOF return of the vendor).
    val tok = scanToken(reader, 1024)

    if (tok == null) {
        svptr.tpx = 0
        return ""
    }

    svptr.tpx = 1
    return tok
}

private fun scanToken(reader: java.io.Reader, maxChars: Int): String? {
    // C whitespace domain (isspace): space, tab, newline, vertical
    // tab, form feed, carriage return.
    fun isWs(c: Int): Boolean =
        c == 32 || (c in 9..13)

    var c = reader.read()
    while (c != -1 && isWs(c)) c = reader.read()

    if (c == -1) return null

    val sb = StringBuilder()
    while (c != -1 && !isWs(c) && sb.length < maxChars) {
        sb.append(c.toChar())
        c = reader.read()
    }
    return sb.toString()
}
