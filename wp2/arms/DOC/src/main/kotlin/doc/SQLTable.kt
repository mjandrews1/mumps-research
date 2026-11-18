package doc

// Unit 34: libmpscpp.cpp::SQL_Table (batch 2, simple stratum).
// Vendor quirks kept: the ztabzize spelling (not tabsize) and the reuse of
// the GLOBAL_NOT_FOUND error code for a size error. cout emission becomes
// a console write (documented divergence: no observable difference for the
// arm build; both command strings use escaped dollar bytes per cookbook
// H-015).

fun SQL_Table(s: String, c: Int): Mstring {
    val cmd = CharArray(STR_MAX)
    val tab = Mstring(s)

    if (c > 10) {
        println("Table size too large.")
        Globals.GlobalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }

    CstringOps.strcpyS(cmd, "set \$table=\"")
    CstringOps.strcatS(cmd, s)
    CstringOps.strcatS(cmd, "\"")
    Stubs.Interpret(CstringOps.cstring(cmd), Globals.svPtr)
    // sprintf formatting emitted "set" plus ztabzize (vendor spelling kept)
    CstringOps.strcpyS(cmd, "set \$ztabzize=" + c.toString())
    Stubs.Interpret(CstringOps.cstring(cmd), Globals.svPtr)
    return tab
}
