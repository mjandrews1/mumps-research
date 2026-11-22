package both

// Unit 156 (BOTH B6 T1, simple): libmpscpp.cpp::global::Find (22
// LOC). Batch_plan ordinal 156: simple, LOC 22, score 6.2,
// complexity rank 37. The roster row is the global-overload of
// the Find family (vendor line 7587, signature
// "int global::Find(global key, int st)"); the unit-7 char
// overload (line 7566, 16 LOC, batch 2) and the unit-4 mstring
// overload (line 7617, 16 LOC, batch 7) are their own roster
// rows and stay out of this render (h019 naming: the member
// folds to globalFind keeping the vendor member name, the
// unit-133 globalLength precedent).
//
// Vendor behavior: the OWN global is retrieved into tmp (the
// haystack), failing -> GlobalExceptionCode=GLOBAL_NOT_FOUND and
// throw MumpsGlobalException (the shared retrieval idiom); the
// NEEDLE global (the key receiver) is retrieved into tmp1, with
// the same fail-throw; st is formatted via Mltoa into the char
// a[40] carrier; _find(r, tmp, tmp1, a) computes the find result
// into the char r[20] carrier; the int tail is atoi(r).
//
// BOTH consults (find_rule before render, events logged):
//   h007 char_buffer - reused and hit: the Mglobal out-buffer
//     roles (tmp/tmp1) return as Strings via the mglobalRetrieve
//     seat; the _find in/out/carrier roles stay CharArrays per
//     the C boundary idiom (r[20]/a[40] sized as in the vendor).
//   h014 out_of_scope_call - reused and hit: Mglobal/exception
//     idiom rides the mglobalRetrieve seat (owner roster 167);
//     Mltoa rides its roster-292 stub (raised at B5 T2,
//     batch 10 pending); _find is a NEW stub raised in
//     BothStubs.kt naming its owner interp-sym.cpp::_find,
//     roster 6, batch 1 (the B1 pilot chapters precede this
//     arm's render tree; the stub releases when the owner lands
//     per the h014 contract).
//   h019 class_member_carrier - reused and hit (first
//     consult-as-rule): member of the global class (layout owner
//     include/mumpsc/globalClass.h::<misc>, roster 89,
//     untranslated in this arm); the render passes the receiver
//     state explicitly (ref/key as parameters, key on the
//     GlobalRef carrier) per the h019 action; atolC quiet parse
//     promoted internal (B6 T1 seat edit, LibmpscppMisc5.kt)
//     seats the atoi tail.

internal fun globalFind(ref: String, key: GlobalRef, st: Int): Int {
    val a = CharArray(40)
    val r = CharArray(20)
    val tmp = mglobalRetrieve(ref) // the haystack: own value
    val tmp1 = mglobalRetrieve(key.ref) // the needle value
    Mltoa(st.toLong(), a)
    _find(r, tmp.toCharArray(), tmp1.toCharArray(), a)
    return atolC(cStringOf(r)).toInt()
}
