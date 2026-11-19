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
