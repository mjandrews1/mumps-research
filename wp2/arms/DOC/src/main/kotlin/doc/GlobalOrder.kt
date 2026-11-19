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
    var tmp = "s %=\u0024order("
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

// Unit 69 (B3 tranche 1): libmpscpp.cpp::GlobalOrder (batch 3, simple
// stratum; LOC 16, score 8.2) - the std::string overload. NAMING
// ADAPTATION (H-015): the vendor's char* and std::string overloads
// collapse to IDENTICAL Kotlin signatures after the unit-39 String
// carrier, so this overload renders as the named seat globalOrderString
// (vendor name kept here). VENDOR DIVERGENCE from the unit-39 body:
// this overload declares the dead scratch char r[STR_MAX] (kept as a
// comment per H-013) and folds the vendor's static std::string rr to a
// fresh String per call (H-008, the unit-39 fold); the success arm's
// (string)SymGet("%") cast rides the carrier's c_str(); the int
// parameter i is again a dead parameter (H-013, signature kept).

fun globalOrderString(a: String, i: Int): String {
    // vendor declares char r[STR_MAX]; dead per H-013 (drop documented)

    var tmp = "s %=\u0024order("
    tmp += a
    tmp += ")"

    val rr: String
    if (Stubs.xecute(tmp.toCharArray()) == 0) {
        Globals.svPtr.tpx = 1
        val t = Stubs.SymGet("%".toCharArray())
        rr = t.c_str()
    } else {
        Globals.svPtr.tpx = 0
        rr = ""
    }

    return rr
}
