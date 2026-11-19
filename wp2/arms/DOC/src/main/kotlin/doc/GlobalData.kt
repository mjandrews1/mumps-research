package doc

// Unit 70 (B3 tranche 1): libmpscpp.cpp::GlobalData (batch 3, simple
// stratum; LOC 18, score 8.6) - the (dollar)data intrinsic over
// globals, char* form. Overload notes: the mstring/string forms are
// separate vendor overloads not in this batch's rows; this render is
// the char* row. Vendor dead local kept as a comment per H-013 (the
// char r[STR_MAX]="" buffer is never read); the command text
// "s %=(dollar)data(...)" builds as a String (u39 GlobalOrder
// precedent); the dollar byte is escaped per H-015; xecute/SymGet
// are the H-014 stubs; the atoi parse reuses the Find.kt seat.
// -1 on execution failure with tpx 0/1 by outcome.

fun GlobalData(a: String): Int {
    // vendor declares char r[STR_MAX]="" here; dead per H-013 (kept
    // as this comment, the drop documented)

    var tmp = "s %=\u0024data("
    tmp += a
    tmp += ")"

    val i: Int
    if (Stubs.xecute(tmp.toCharArray()) == 0) {
        Globals.svPtr.tpx = 1
        val t = Stubs.SymGet("%".toCharArray())
        i = atoi(t.c_str())
    } else {
        Globals.svPtr.tpx = 0
        i = -1
    }

    return i
}
