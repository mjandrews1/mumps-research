package both

// Unit 215 render: libmpscpp.cpp::global::Find (roster 215,
// batch 7, 16 LOC; units.csv rank 4). The MSTRING overload of
// the global::Find family, vendor :7617: the receiver global
// is the haystack, the mstring key IS the needle (passed
// directly, NOT retrieved - the unit-156 global-overload
// retrieves the key global's value; this overload differs
// exactly there).
//
// MULTI-ROW NAME (the Find family, journaled per the unit-156
// header): the global overload :7587 is the roster-156 render
// (globalFind, B6 T1); the char* overload :7566 is roster 61
// (b2 pending); THIS mstring overload :7617 is the unit-215
// corpus seat. mstring::Find and the _find engine
// (interp-sym.cpp, roster 6 b1) stay with their owners.
//
// Consults (rule base wp2/arms/BOTH/rules_v0.pl):
//   h014 out_of_scope_call - reused: Mglobal RETRIEVE direct;
//     Mltoa TODO stub direct (owner interp-arith.cpp::Mltoa,
//     units.csv unit 291 rank 292 - the old pin "roster 292"
//     is the RANK, same family, cosmetic fix queued); _find
//     TODO stub direct (owner interp-sym.cpp::_find, roster 6,
//     batch 1 pending - the B5-raised BothStubs seat);
//     atolC/cStringOf direct.
//   h015 grammar-collision - reused: ASCII only, no backticks,
//     no raw comment dollars, trailing newline.
//   h017 naming - reused: globalFindM (the unit-156
//     globalFind precedent + the mstring-arm qualifier; the
//     FILE is GlobalFindM.kt - GlobalFind.kt is taken by the
//     unit-156 render, the unit-197 collision lesson).
//   h019 class_member_carrier - reused: the member rides the
//     GlobalRef receiver (ref the haystack; the mstring key
//     the needle), the unit-156 precedent.
//
// Quirks kept (ledgered):
//   - the needle is the mstring CONTENT direct (the unit-156
//     needle-retrieve divergence is the family's distinction;
//     journaled).
//   - fail-throw BEFORE the Mltoa/_find tail (kept first).
//   - the char a[40]/r[20] carriers sized as in the vendor.

// int global::Find(mstring key, int st) - libmpscpp.cpp:7617
internal fun GlobalRef.globalFindM(key: String, st: Int): Int {
    val a = CharArray(40)
    val r = CharArray(20)
    val tmp = CharArray(STR_MAX)

    val f = Mglobal(RETRIEVE, ref.toCharArray(), tmp, svPtr)

    if (f == 0) {
        GlobalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }

    Mltoa(st.toLong(), a) // Mltoa(st, a)
    // the needle is the mstring CONTENT direct (not retrieved)
    _find(r, tmp, key.toCharArray(), a)
    return atolC(cStringOf(r)).toInt() // rr = atoi(r)
}
