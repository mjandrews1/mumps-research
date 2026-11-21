package daf

// Unit 133 (DAF B5 T1, simple): libmpscpp.cpp::global::Length
// (23 LOC): the global-class member that counts the occurrences of
// one global's value as substrings of another global's value (same
// \$Length service kernel as unit 129, reached through the node
// receiver instead of an mstring).
// Consults: BASE-log row 92 hit-reuse (BOTH Mglobal(RETRIEVE)
// calls render the retrieve-then-throw pattern; the ref global, then
// the needle global, each with GlobalExceptionCode=GLOBAL_NOT_FOUND
// and the MumpsGlobalException fold), row 54 hit-reuse (the C++
// global node receiver folds onto the MpsGlobalNode ref String; the
// two member calls keep the node-call shape via explicit params per
// the row-101 fold), row 33 family hit-adapt (strstr -> String
// indexOf; the two gtmp/gtmp1 out-buffers are the Mglobal cstring
// carriers), rows 72/10 hit-adapt (the while(1)+break walk).
// VENDOR QUIRK preserved exactly (ledgered, no repair; identical to
// the unit-129 quirk): empty needle value = vendor nontermination;
// the Dafny kernel spec guards needle != empty. The ref-global
// retrieve happens FIRST (the vendor retrieves ref then needle),
// so an empty ref global with a live needle global returns 0.
// Dafny: the pure counting kernel is spec'd at spec/length_count.dfy
// (shared with unit 129; see MstringLength.kt).

fun globalLengthD(ref: String, needleRef: String, sv: DafMsv): Int {

    val gtmp = CharArray(STR_MAX_DAF)
    val gtmp1 = CharArray(STR_MAX_DAF)

    var f = MglobalDaf(RETRIEVE_D, nameCarriage(ref), gtmp, sv)
    if (f == 0) {
        GlobalExceptionCodeD = GLOBAL_NOT_FOUND_D
        throw MumpsGlobalExceptionD("global.Length: global not found (ref)")
    }

    f = MglobalDaf(RETRIEVE_D, nameCarriage(needleRef), gtmp1, sv)
    if (f == 0) {
        GlobalExceptionCodeD = GLOBAL_NOT_FOUND_D
        throw MumpsGlobalExceptionD("global.Length: global not found (needle)")
    }

    val p1 = DafCRuntime.cstring(gtmp)
    if (p1.isEmpty()) return 0

    var i = 1
    val needle = DafCRuntime.cstring(gtmp1)
    var from = 0
    while (true) {
        val idx = p1.indexOf(needle, from)
        if (idx < 0) return i

        i++
        from = idx + needle.length
    }
}

// name-carrier helper (row 33 family): the C caller passes
// (unsigned char *) ref straight to Mglobal; the render copies the
// ref String into a STR_MAX carriage with NUL padding.
private fun nameCarriage(src: String): CharArray {
    val dst = CharArray(STR_MAX_DAF)
    var q = 0
    while (q < src.length) { dst[q] = src[q]; q++ }
    return dst
}
