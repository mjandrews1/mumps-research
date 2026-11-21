package doc

// Unit 229 (B8 tranche 2): interp-sym.cpp::ScanParse (batch 8, core
// stratum; LOC 25, score 14.7, rank 142; units.csv row 234; vendor
// interp-sym.cpp:81 prototype, :1410 body, physically verified this
// session: the dump asserts physical loc 25 = the roster row). The
// delimited-token walker of the symbol parser: scans tmp1 for the
// break position of the current parse token. Consult H-007
// hit-reuse: the scan loop is the walker idiom of the arm (quote
// handling kept verbatim: a double-quote starts a skip-to-next-
// quote inner walk; the paren depth counter p toggles the comma
// break; whitespace, NUL and depth-zero commas break). The
// stub seat flips to this real (Stubs.ScanParse, the batch-8
// ledger named by the unit-93 parse render's call site note).
// Naming per H-017 (vendor verbatim).

fun ScanParse(tmp1: CharArray): Int {
    var i = 0
    var p = 0

    while (true) {

        if (tmp1[i] == NUL) break
        if (tmp1[i] == ' ') break
        if (tmp1[i] == ',' && p == 0) break
        if (tmp1[i] == '(') p++
        if (tmp1[i] == ')') p--
        if (tmp1[i] != '"') {
            ++i
            continue
        }
        ++i
        while (true) {
            if (tmp1[i] == NUL) break
            if (tmp1[i] != '"') {
                ++i
                continue
            }
            ++i
            break
        }
    }
    return i
}
