package doc

// Unit 33: libmpscpp.cpp::global::Find (batch 2, simple stratum). The
// unit is the char* overload per the pre-registered chunker slice.

fun GlobalRef.find(key: String, st: Int): Int {
    val a = CharArray(40)
    val r = CharArray(20)
    val tmp = CharArray(STR_MAX)

    val f = Stubs.Mglobal(RETRIEVE, ref, tmp, Globals.svPtr)

    if (f == 0) {
        Globals.GlobalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }

    Stubs.Mltoa(st, a)
    Stubs.Mfind(r, tmp, key.toCharArray(), a)
    val rr = atoi(CstringOps.cstring(r))
    return rr
}

// strtol-style int conversion of a decimal prefix, per the C atoi contract
// (skips whitespace, optional sign, stops at first non-digit).
fun atoi(s: String): Int {
    var i = 0
    while (i < s.length && (s[i] == ' ' || s[i] == '\t' || s[i] == '\n')) i++
    var sign = 1
    if (i < s.length && (s[i] == '+' || s[i] == '-')) {
        if (s[i] == '-') sign = -1
        i++
    }
    var v = 0
    while (i < s.length && s[i] in '0'..'9') {
        v = v * 10 + (s[i] - '0')
        i++
    }
    return sign * v
}

// Unit 156 (B6 tranche 1): libmpscpp.cpp::global::Find (batch 6,
// simple stratum; LOC 22, score 6.2) - the GLOBAL-key overload
// (vendor at libmpscpp.cpp:7587): double Mglobal(RETRIEVE)
// walk - this global's value, then the key global's - with the
// MumpsGlobalException throw on GLOBAL_NOT_FOUND at either
// retrieval (H-014; the unit-33 single-retrieve shape applies
// twice), then the Mltoa/Mfind/atoi tail of the unit-33 render
// verbatim. Tag correction ledger: the unit-33 KDoc tagged its
// row the char-star overload; the batch-2 roster row (score
// 4.2) is the mstring-key form at :7617 - both non-global
// needle forms fold into the unit-33 String render (the String
// parameter covers either carrier); this unit is the GlobalRef
// needle twin and the overload disambiguation is by parameter
// type (H-004; both renders keep their roster seats). Naming
// per H-017 (Find -> find).

fun GlobalRef.find(key: GlobalRef, st: Int): Int {
    val a = CharArray(40)
    val r = CharArray(20)
    val tmp = CharArray(STR_MAX)
    val tmp1 = CharArray(STR_MAX)

    var f = Stubs.Mglobal(RETRIEVE, ref, tmp, Globals.svPtr)

    if (f == 0) {
        Globals.GlobalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }

    f = Stubs.Mglobal(RETRIEVE, key.ref, tmp1, Globals.svPtr)

    if (f == 0) {
        Globals.GlobalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }

    Stubs.Mltoa(st, a)
    Stubs.Mfind(r, tmp, tmp1, a)
    val rr = atoi(CstringOps.cstring(r))
    return rr
}

// Unit 186 (B7 tranche 1): libmpscpp.cpp::global::Find (batch 7,
// simple stratum; LOC 16, nonblank 12, score 4.0, rank 4, the
// lowest-score row of the batch) - the CHAR-STAR needle overload
// (vendor libmpscpp.cpp:7566, physically verified this session).
// Fold note (the unit-156 ledger update of the unit-33 fold
// decision): the vendor char-star, mstring and string non-global
// needles share the identical Mltoa/_find/atoi tail - the
// unit-33 String render already folds the mstring/string forms
// (its KDoc tag correction), so this seat renders the C carrier
// edge only: CharArray key converted to its C string per the
// SymGet CharArray-overload precedent (H-012), delegating to the
// unit-33 body unchanged. The unit-156 GlobalRef needle stays the
// sibling seat above. Naming per H-017 (Find -> find).

fun GlobalRef.find(key: CharArray, st: Int): Int =
    find(CstringOps.cstring(key), st)
