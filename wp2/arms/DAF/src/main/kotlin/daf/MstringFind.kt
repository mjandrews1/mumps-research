package daf

// Unit 217 (DAF B8 T1, simple): libmpscpp.cpp::mstring::Find
// (16 LOC, score 4.2): the GLOBAL-KEY member overload
// int mstring::Find(global key, int st) (vendor libmpscpp.cpp:7649,
// physically verified this session). Same-file family adjudication:
// the char*-key sibling at :7638 is the landed unit-186
// globalFindKeyD seat's kin; the mstring-key sibling at :7671 is a
// later-batch row; THIS row is the global-key row. The receiver is
// the mstring data (the row-101 fold: no Kotlin mstring class, the
// receiver data.c_str() is the explicit String seat).
// Consults (2026-11-24T21:10:30Z, nominal): BASE-log row 37
// hit-reuse (Mltoa -> the mltoaDaf SysDump seat; _find stays the
// findDaf named-owner stub of the unit-156/186 renders - owner
// interp-sym.cpp:1282, batch-9 roster family; this render is
// another in-arm caller, the seat goes home unchanged), row 92
// hit-reuse (the Mglobal(RETRIEVE) retrieve-then-throw on the KEY
// global ref with GlobalExceptionCodeD = GLOBAL_NOT_FOUND_D and
// the MumpsGlobalExceptionD fold), rows 33/28 hit-adapt (the char
// a[40]/r[20] stack scratch and the tmp1[STR_MAX] carriage ride
// fresh CharArrays per invocation; the atoi tail -> the
// DafCRuntimeT2 atoi seat). VENDOR ORDER QUIRK kept: Mltoa(st, a)
// runs BEFORE the Mglobal retrieve (the unit-156 render noted the
// same order divergence from its seat-shaped twin; the vendor body
// is copied verbatim here).
// Dafny: EXEMPT (stub-relay chain over global-access seats; journal
// row).

fun mstringFindD(data: String, key: MpsGlobalNode, st: Int, sv: DafMsv): Int {

    val a = CharArray(40) // char a[40] carrier (Mltoa target)
    val r = CharArray(20) // char r[20] carrier (_find out)

    mltoaDaf(st.toLong(), a) // Mltoa(st,(unsigned char *)a)

    // int f = Mglobal(RETRIEVE, key.ref, tmp1, svPtr)
    val tmp1 = CharArray(STR_MAX_DAF)
    val keyCarriage = find217Name(key.ref)
    val f = MglobalDaf(RETRIEVE_D, keyCarriage, tmp1, sv)
    if (f == 0) {
        GlobalExceptionCodeD = GLOBAL_NOT_FOUND_D
        throw MumpsGlobalExceptionD("mstring.Find: global not found (key)")
    }

    // _find(r, data.c_str(), tmp1, a) - the receiver data is the
    // haystack carriage
    findDaf(r, find217Name(data), tmp1, a)

    val rr = atoi(DafCRuntime.cstring(r)) // rr = atoi(r)
    return rr
}

// name-carrier helper (row-33 family, the unit-156/186 refCarriage
// precedent replicated per the per-file private-helper convention):
// copies the String into a STR_MAX carriage with NUL padding.
private fun find217Name(src: String): CharArray {
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
