package daf

// Unit 156 (DAF B6 T1, simple): libmpscpp.cpp::global::Find
// (22 LOC, score 6.2, rank 37): the global-class member that takes a
// key global and a start ordinal, retrieves both values, and relays
// to the _find table-search service; the atoi result is the vendor's
// returned ordinal.
// Consults (2026-11-22T15:45:30Z, nominal): BASE-log row 92
// hit-reuse (the two Mglobal(RETRIEVE)-then-throw sites: the ref
// global is retrieved FIRST, then the key global, each with
// GlobalExceptionCode = GLOBAL_NOT_FOUND and the MumpsGlobalException
// fold onto MumpsGlobalExceptionD), row 54 hit-reuse (the C++ global
// node receivers fold onto MpsGlobalNode ref Strings carried through
// cstring carriages like the unit-133 GlobalLength precedent), rows
// 33/56 hit-adapt (the char a[40]/r[20] scratch buffers ride fresh
// CharArrays per invocation; the static-char fold does not apply
// here because the vendor buffers are stack-local), row 37
// hit-reuse (the two out-of-scope services become named-owner stub
// seats: _find at interp-sym.cpp:1282, owner of the batch-9 roster
// family, raised below as findDaf; Mltoa at the mltoaDaf seat already
// raised by SysDump.kt, owner interp-arith.cpp::Mltoa), row 28
// hit-reuse (atoi -> the DafCRuntimeT2 atoi of the C-runtime seats).
// Vendor quirk kept: the key retrieve-then-throw happens only after
// the ref retrieve succeeded (the vendor order, no reordering).
// Dafny: EXEMPT (stub-relay chain over global-access seats; journal
// row).

// void _find(unsigned char *out, unsigned char *in,
//            unsigned char *key, unsigned char *begin) - named-owner
// stub (owner interp-sym.cpp:1282; the batch-9 roster family; not a
// batch-6 T1 cell).
fun findDaf(out: CharArray, inBuf: CharArray, key: CharArray, begin: CharArray): Unit =
    throw NotImplementedError(
        "_find: out-of-scope owner (interp-sym.cpp::_find at line 1282; not a batch-6 T1 cell)")

fun globalFindD(n: MpsGlobalNode, key: MpsGlobalNode, st: Int, sv: DafMsv): Int {

    val a = CharArray(40)  // char a[40] carrier (Mltoa target)
    val r = CharArray(20)  // char r[20] carrier (_find out)
    val tmp = CharArray(STR_MAX_DAF)
    val tmp1 = CharArray(STR_MAX_DAF)

    val refCarriage = refCarriage156(n.ref)
    var f = MglobalDaf(RETRIEVE_D, refCarriage, tmp, sv)
    if (f == 0) {
        GlobalExceptionCodeD = GLOBAL_NOT_FOUND_D
        throw MumpsGlobalExceptionD("global.Find: global not found (ref)")
    }

    val keyCarriage = refCarriage156(key.ref)
    f = MglobalDaf(RETRIEVE_D, keyCarriage, tmp1, sv)
    if (f == 0) {
        GlobalExceptionCodeD = GLOBAL_NOT_FOUND_D
        throw MumpsGlobalExceptionD("global.Find: global not found (key)")
    }

    mltoaDaf(st.toLong(), a) // Mltoa(st, a) - named-stub seat
    findDaf(r, tmp, tmp1, a) // _find(r, tmp, tmp1, a)

    return atoi(DafCRuntime.cstring(r)) // rr = atoi(r)
}

// name-carrier helper (row 33 family, the unit-133 nameCarriage
// precedent): copies the ref String into a STR_MAX carriage with NUL
// padding.
private fun refCarriage156(src: String): CharArray {
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
