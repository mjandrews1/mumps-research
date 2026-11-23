package daf

// Unit 186 (DAF B7 T1, simple): libmpscpp.cpp::global::Find
// (16 LOC, score 5.7): the C-string-key overload twin of the unit-156
// Find render of batch 6 (the 22-LOC mstring-family body); same-file
// same-name family adjudication: unit 156 took the node-carrier body
// (the retrieved-twice shape), this row is the char*-key form whose
// body retrieves ONLY the receiver ref and passes the raw key pointer
// to _find (the KEY-RETRIEVE ABSENCE is the twin's distinguishing
// fidelity feature; no second Mglobal call exists to reuse).
// Consults (2026-11-23T18:30:00Z, nominal): BASE-log row 92
// hit-reuse (the Mglobal(RETRIEVE) retrieve-then-throw on the ref,
// GlobalExceptionCode = GLOBAL_NOT_FOUND, MumpsGlobalException fold
// onto MumpsGlobalExceptionD - the unit-156 same-seat reuse), row 54
// hit-reuse (the node receiver ref String carried through the
// name-carriage carrier, the unit-156 refCarriage156 precedent
// replicated locally here per the private-helper convention), row 37
// hit-reuse (the out-of-scope _find service stays the findDaf
// named-owner stub seat raised at the unit-156 render - owner
// interp-sym.cpp:1282; this render is the SECOND in-arm caller, seat
// goes home unchanged; Mltoa likewise rides the mltoaDaf seat
// already raised by SysDump.kt), rows 33/28 hit-adapt (the char
// a[40]/r[20] stack scratch buffers ride fresh CharArrays per
// invocation; the atoi tail -> the DafCRuntimeT2 atoi of the C-runtime
// seats). Overlap kept verbatim: no key retrieve on failure sides.
// Dafny: EXEMPT (stub-relay chain over global-access seats; journal
// row).

fun globalFindKeyD(n: MpsGlobalNode, key: MpsGlobalNode, st: Int, sv: DafMsv): Int {

    val a = CharArray(40) // char a[40] carrier (Mltoa target)
    val r = CharArray(20) // char r[20] carrier (_find out)
    val tmp = CharArray(STR_MAX_DAF)

    val refCarriage = refCarriage186(n.ref)
    val f = MglobalDaf(RETRIEVE_D, refCarriage, tmp, sv)
    if (f == 0) {
        GlobalExceptionCodeD = GLOBAL_NOT_FOUND_D
        throw MumpsGlobalExceptionD("global.Find: global not found (ref)")
    }

    mltoaDaf(st.toLong(), a) // Mltoa(st, a) - named-stub seat
    findDaf(r, tmp, keyCarriage186(key.ref), a) // _find(r, tmp, key, a)

    return atoi(DafCRuntime.cstring(r)) // rr = atoi(r)
}

// name-carrier helper (row 33 family, the unit-156 refCarriage156
// precedent replicated per the per-file private-helper convention):
// copies the ref String into a STR_MAX carriage with NUL padding.
private fun refCarriage186(src: String): CharArray {
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

// key carriage (row 33 family): the vendor passes (unsigned char *)key
// raw - a plain NUL-terminated carriage, no node semantics.
private fun keyCarriage186(src: String): CharArray {
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
