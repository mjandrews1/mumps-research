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

// Unit 192 (B7 tranche 1): libmpscpp.cpp::GlobalData (batch 7,
// simple stratum; LOC 17, score 8.7, rank 79) - the MSTRING
// needle overload (vendor libmpscpp.cpp:2616, physically verified
// this session; nonblank 17 matches the roster row). Roster
// adjudication (same-file, same-score 8.7 twin ranks 78/79 across
// batches 7/8): this row takes the mstring form at :2616, the
// file-order-first of the two unlanded siblings after the
// batch-3 unit-70 char-star render above, with the string form
// at :2636 as the batch-8 row (the fold note: the Kotlin String
// body shape covers either carrier; each roster row keeps its
// seat). Body: the same s-percent-equals-dollar-data command
// build as unit 70 (H-015: the dollar byte rides the unicode
// escape; the a.c_str() needle is the mstring str carrier),
// xecute/SymGet H-014 stubs, tpx 1 on success, atoi tail on the
// same Find.kt seat; the vendor's dead tmp2-style locals do not
// appear in this overload (the mstring t and int i ride the
// local val seats).

fun GlobalData(a: Mstring): Int {
    var tmp = "s %=\u0024data("
    tmp += a.c_str()
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
