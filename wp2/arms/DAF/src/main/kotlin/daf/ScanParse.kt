package daf

// Unit 229 (DAF B8 T2, core): interp-sym.cpp::ScanParse
// (25 LOC, score 14.7): the delimited-token walker of the symbol
// parser: scans tmp1 for the break position of the current parse
// token (vendor interp-sym.cpp:1410 body, the :81 prototype, both
// physically verified this session). SEAT RELEASE: the scanParseDaf
// named stub of DafInterpStubsT3 is released - the real body lands
// here under the stub's own name and signature (the levCheck
// delegate precedent; the Fcn.kt dispatcher call site resolves
// unchanged), the stub is deleted.
// Consults (2026-11-24T21:16:30Z, nominal): the scan loop is the
// walker idiom of the arm verbatim - quote handling kept (a
// double-quote starts a skip-to-next-quote inner walk), the paren
// depth counter p toggles the comma break (NEXT_D-family order
// codes ride their own consts), whitespace and NUL break, the
// depth-zero comma breaks; the int i/p locals ride Kotlin locals.
// Dafny: EXEMPT (buffer walk; journal row).

fun scanParseDaf(tmp1: CharArray): Int {

    var i = 0
    var p = 0

    while (true) {

        if (tmp1[i] == NUL_D) break // if (tmp1[i] == 0) break
        if (tmp1[i] == ' ') break
        if (tmp1[i] == ',' && p == 0) break
        if (tmp1[i] == '(') p++
        if (tmp1[i] == ')') p--
        if (tmp1[i] != '"') {
            ++i
            continue
        }
        ++i
        while (true) { // the quoted run
            if (tmp1[i] == NUL_D) break
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
