package doc

// Unit 39: libmpscpp.cpp::GlobalOrder (batch 2, simple stratum) - the
// char* overload. Per cookbook H-008 the C static scratch r[STR_MAX]
// becomes a fresh String per call (no caller can alias the old buffer
// because every call overwrites it from the start). The dollar byte in
// the command text is escaped per H-015. Vendor quirk kept: the int
// parameter i is unused (left in the signature, documented as a dead
// parameter per H-013 with the drop diverged to preserve call-site shape).

fun GlobalOrder(a: String, i: Int): String {
    val cmd = CharArray(STR_MAX)
    var tmp = "s %=\$order("
    tmp += a
    tmp += ")"

    val r: String
    if (Stubs.xecute(tmp.toCharArray()) == 0) {
        Globals.svPtr.tpx = 1
        val t = Stubs.SymGet("%".toCharArray())
        r = t.c_str()
    } else {
        Globals.svPtr.tpx = 0
        r = ""
    }

    return r
}
