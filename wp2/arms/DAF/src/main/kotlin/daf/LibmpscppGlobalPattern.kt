package daf

/*
 * DAF batch-10 unit 306 (roster row 306; libmpscpp.cpp::global::
 * Pattern, LOC 15, ptr 0, score 5.1). Consult 2026-11-25T15:14:00Z.
 *
 * The global receiver side of the MSTRING PATTERN family: the
 * C++ global::Pattern overload lattice (global/string/mstring/
 * const char star twins) collapses to two rendered bodies - the
 * global-needle twin (two retrieves) and the c-string-needle twin
 * (one retrieve); the string and mstring twins share the const
 * char star body verbatim (vendor c_str() relay; ledgered).
 *
 * Fold notes: Mglobal(RETRIEVE, ...) rides MglobalDaf with the
 * nameCarriage pattern (GlobalLength.kt precedent); the
 * GlobalExceptionCode/throw pattern rides GlobalExceptionCodeD +
 * MumpsGlobalExceptionD (row 92 family); pm(tmp, tmp1, svPtr)
 * relays onto the pmD named-stub seat (interp-pm.cpp::pm, not a
 * corpus unit - the raise is the disclosed boundary, Dice.kt
 * relay precedent).
 *
 * H-015: ASCII; no raw dollars; trailing newline.
 * Dafny: EXEMPT (service wrapper over retrieves; journal row).
 */

// int global::Pattern(global p) - the global-needle twin
fun globalPatternD(n: MpsGlobalNode, p: MpsGlobalNode, sv: DafMsv): Int {

    val tmp = CharArray(STR_MAX_DAF)
    var f = MglobalDaf(RETRIEVE_D, nameCarriageP(n.ref), tmp, sv)
    if (f == 0) {
        GlobalExceptionCodeD = GLOBAL_NOT_FOUND_D
        throw MumpsGlobalExceptionD("global.Pattern: global not found (ref)")
    }

    val tmp1 = CharArray(STR_MAX_DAF)
    f = MglobalDaf(RETRIEVE_D, nameCarriageP(p.ref), tmp1, sv)
    if (f == 0) {
        GlobalExceptionCodeD = GLOBAL_NOT_FOUND_D
        throw MumpsGlobalExceptionD("global.Pattern: global not found (needle)")
    }

    return pmD(DafCRuntime.cstring(tmp), DafCRuntime.cstring(tmp1), sv)
}

// int global::Pattern(const char *p) - the string/mstring twins
// share this body (c_str() relay, ledgered header note)
fun globalPatternD(n: MpsGlobalNode, p: String, sv: DafMsv): Int {

    val tmp = CharArray(STR_MAX_DAF)
    val f = MglobalDaf(RETRIEVE_D, nameCarriageP(n.ref), tmp, sv)
    if (f == 0) {
        GlobalExceptionCodeD = GLOBAL_NOT_FOUND_D
        throw MumpsGlobalExceptionD("global.Pattern: global not found (ref)")
    }

    return pmD(DafCRuntime.cstring(tmp), p, sv)
}

// name-carrier helper (row 33 family; GlobalLength.kt precedent)
private fun nameCarriageP(src: String): CharArray {
    val out = CharArray(STR_MAX_DAF)
    val s = src.toCharArray()
    var i = 0
    while (i < s.size) {
        out[i] = s[i]
        i++
    }
    out[i] = NUL_D
    return out
}
