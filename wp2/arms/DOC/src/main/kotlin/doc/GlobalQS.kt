package doc

// Unit 134 (B5 tranche 2): libmpscpp.cpp::GlobalQS (batch 5, core
// stratum; LOC 19, score 11.3) - the quoted-subscript runtime query,
// the unit-39 GlobalOrder family shape. VENDOR DEAD DECLARATION kept
// as a ledger note per H-013: the static char r[STR_MAX] is never
// read or written (drop documented, not initialized). The dollar byte
// of the percent-qs command text is escaped per H-015 (the unit-39
// percent-order escape); the unsigned char* params render on the
// CstringOps byte domain per H-012; xecute and SymGet ride the H-014
// stub seats; tpx success/failure flag on svPtr kept verbatim; the
// vendor's return with no value maps to Unit. Naming per H-017.

fun GlobalQS(a: CharArray, b: CharArray, c: CharArray, svPtr: Msv) {
    // vendor declares static char r[STR_MAX]; dead per H-013 (see header)

    var tmp = "s %=\u0024qs("
    tmp += CstringOps.cstring(a)
    tmp += ","
    tmp += CstringOps.cstring(c)
    tmp += ")"

    if (Stubs.xecute(tmp.toCharArray()) == 0) {
        svPtr.tpx = 1
        val t = Stubs.SymGet("%".toCharArray())
        CstringOps.strcpyS(b, t.c_str())
    } else {
        svPtr.tpx = 0
        CstringOps.strcpyS(b, "")
    }
}
