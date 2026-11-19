package doc

// Unit 22 (B1 tranche 4): interp-bstring.cpp::<misc> (batch 1,
// complex stratum; units.csv row 22: loc 41). The "better string"
// library residue: Lockner banner, the include ledger, the bstrcat /
// new_bstring / bcatchr / make_cstring / checked_strlen banner
// headers, and the four bodies that live inside the chunk
// (new_bstring, bcatchr, make_cstring, checked_strlen). bstrcat's
// definition is OUTSIDE the chunk (below the chunker cut) - it stays
// banner-only here with that ledger note; no corpus unit calls it.
// struct bstring (bstring.h:33: int length; char string[STR_MAX])
// renders as the Bstring carrier class (H-004 owner object is the
// file-level entity). fprintf(stderr,...) maps to System.err
// (H-001); sigint is the H-014 stub. Vendor quirks kept: the
// checked_strlen loop runs i <= STR_MAX (reading one element past
// the string carrier - the walk is bounds-guarded here, note that
// the vendor's final-element read is the quirk being preserved in
// shape); bcatchr at length == STR_MAX+1 writes string[STR_MAX] as
// terminator beyond the carrier (guarded here with the divergence
// noted); the C dummy return 0 after sigint is unreachable in the
// vendor and unreachable here for the same reason (no Kotlin
// reachability claim is made). Naming per H-017.

class Bstring {
    // bstring.h int length
    var length: Int = 0
    // bstring.h char string[STR_MAX]
    val string = CharArray(STR_MAX)
}

// checked_strlen (below the static declaration line, same file)
fun checkedStrlen(cstring: CharArray): Int {
    var i = 0
    while (i <= STR_MAX && i < cstring.size) {
        if (cstring[i] == NUL) {
            return i
        }
        i++
    }
    System.err.print("String too long - aborting.\n")
    Stubs.sigint(100)
    return 0
}

// new_bstring(initial)
fun newBstring(initial: CharArray): Bstring {
    val result = Bstring()
    result.length = checkedStrlen(initial)
    var i = 0
    while (i < result.length) {
        result.string[i] = initial[i]
        i++
    }
    return result
}

// bcatchr(str, c)
fun bcatchr(str: Bstring, c: Char): Bstring {
    str.length++
    if (str.length > STR_MAX) {
        System.err.print("String too long, aborting.\n")
        Stubs.sigint(100)
    }
    if (str.length - 1 < str.string.size) {
        str.string[str.length - 1] = c
    }
    if (str.length < str.string.size) {
        str.string[str.length] = NUL
    } else {
        // vendor quirk: the length == STR_MAX terminator write lands
        // beyond the carrier; guarded here (divergence noted)
        str.string[str.string.size - 1] = NUL
    }
    return str
}

// make_cstring(dest, src)
fun makeCstring(dest: CharArray, src: Bstring) {
    var i = 0
    while (i < src.length && i < dest.size) {
        dest[i] = src.string[i]
        i++
    }
    if (i < dest.size) dest[i] = NUL
}
