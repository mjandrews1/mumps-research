package daf

// Unit 159 (DAF B6 T1, simple): libmpscpp.cpp::global::Length(mstring)
// (17 LOC, score 8.1, rank 65): the mstring-needle overload of the
// global-class piece counter: retrieve this node's value, then stride
// the needle along it counting the occurrences (the \$Length
// service kernel).
// Consults (2026-11-22T15:47:00Z, nominal): BASE-log row 92
// hit-reuse (the single Mglobal(RETRIEVE)-then-throw site: ref
// retrieve, GlobalExceptionCode = GLOBAL_NOT_FOUND, the
// MumpsGlobalExceptionD fold), row 54 hit-reuse (the C++ node
// receiver folds onto the MpsGlobalNode ref String), rows 98/101
// hit-reuse (the mstring needle param folds to a plain String; the
// member-overload family keeps the distinct seat name globalLengthMD
// next to the unit-133 globalLengthD), rows 33/40 hit-reuse (strchr
// family -> String indexOf; the counting kernel is IDENTICAL to the
// unit-129/133 renders and spec/length_count.dfy already models it:
// kernel reuse, no new spec), rows 72/10 hit-adapt (the while(1)
// walk with the strstr NULL exit and the needle stride).
// VENDOR QUIRK preserved exactly (the unit-129/133 quirk again):
// an empty-needle input is vendor nontermination (strstr finds the
// empty needle at every position and the stride is zero); the render
// keeps the shape, the Dafny kernel spec guards needle != empty.
// The single-retrieve shape differs from the unit-133 two-global
// form: the needle here is a value, not a global reference, so no
// second retrieve-then-throw site exists.
// Dafny: EXEMPT-by-reuse (the counting kernel is spec'd at
// spec/length_count.dfy, verified in the suite at 6 detected; this
// overload reuses that kernel spec; journal row).

fun globalLengthMD(n: MpsGlobalNode, needle: String, sv: DafMsv): Int {

    val gtmp = CharArray(STR_MAX_DAF)

    var f = MglobalDaf(RETRIEVE_D, refCarriage159(n.ref), gtmp, sv)
    if (f == 0) {
        GlobalExceptionCodeD = GLOBAL_NOT_FOUND_D
        throw MumpsGlobalExceptionD("global.Length: global not found (ref)")
    }

    val p1 = DafCRuntime.cstring(gtmp)
    var i = 1
    if (p1.isEmpty()) return 0 // if (strlen(p1)==0) return 0

    var from = 0
    while (true) { // while (1)
        val idx = p1.indexOf(needle, from) // strstr(p1, needle)
        if (idx < 0) return i // one piece

        i++
        from = idx + needle.length // p1 += strlen(needle)
    }
}

// name-carrier helper (row 33 family, the unit-133 nameCarriage
// precedent).
private fun refCarriage159(src: String): CharArray {
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
