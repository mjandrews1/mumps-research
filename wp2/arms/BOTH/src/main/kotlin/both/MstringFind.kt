package both

// Unit 245 render: libmpscpp.cpp::mstring::Find (roster 245,
// batch 8, 17 LOC). int mstring::Find(global key, int st) - the
// receiver's content searched for the retrieved key global from
// position st; the _find service does the scan, the result
// parses back to an int.
//
// Consults (rule base wp2/arms/BOTH/rules_v0.pl):
//   h014 out_of_scope_call - reused: Mltoa direct (the
//     BothCRuntime seat); Mglobal RETRIEVE direct; the _find
//     TODO stub called direct (owner interp-sym.cpp::_find,
//     roster 6, batch 1 pending - the word find service behind
//     unit 156); GlobalExceptionCode/MumpsGlobalException idiom
//     direct.
//   h015 grammar-collision - reused: ASCII only, no backticks,
//     no raw comment dollars, trailing newline.
//   h017 naming - reused: the mstring member renders as the
//     String extension mstringFind (the mstringTranslate/
//     mstringLessEqual precedent; the global::Find sibling is
//     the GlobalRef.globalFindM seat, record 208 family -
//     distinct receiver family, no fold).
//   h019 class_member_carrier - reused: the data member is the
//     receiver's own content (the String extension receiver,
//     data.c_str() in the vendor); the TU-static svPtr is the
//     BothCRuntime singleton.
//
// Quirks kept (ledgered):
//   - the Mltoa width is 40 (char a[40]) while the vendor
//     _find begin buffer is the same array - kept exact.
//   - the r[20] result buffer parses with atoi (the leading
//     digits only - the _find contract).
//   - the fail-throw sits between the retrieve and the scan
//     (kept first, the globalFindM idiom).

// int mstring::Find(global key, int st) - libmpscpp.cpp (member)
internal fun String.mstringFind(key: GlobalRef, st: Int): Int {
    val a = CharArray(40)
    val r = CharArray(20)
    val tmp1 = CharArray(STR_MAX)

    Mltoa(st.toLong(), a)

    val f = Mglobal(RETRIEVE, key.ref.toCharArray(), tmp1, svPtr)

    if (f == 0) {
        GlobalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }

    _find(r, toCharArray(), tmp1, a)

    return atolC(cStringOf(r)).toInt() // rr = atoi((char *) r)
}
