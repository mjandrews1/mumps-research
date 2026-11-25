package daf

// Unit 282 (DAF B10 T3, complex): libmpscpp.cpp::<misc-4> (units.csv
// row 282: 600 physical LOC, the fourth residual chunk of the
// libmpscpp TU): the mstring operator lattice, member + friend.
// Structure of the vendor chunk (bodies grep-verified):
// (a) mstring::operator=(global): get_global + tpx guard throw +
//     data=tmp;
// (b) operator!: the M truth test (Length()==0 || data=="0" -> "1"
//     else "0");
// (c) operator++/-- pre + post, unary +/-;
// (d) operator+ arith lattice (int/long via add1, double/float via
//     add3, mstring/string/char* via add, global via get_global);
// (e) operator|| concat lattice (member + friend).
// (f) operator- sub lattice (cvt for scalars; global arm reads into
//     the SAME t1 buffer then sub(data,t1,t1) - the vendor b/c
//     alias quirk kept, DISCLOSED);
// (g) operator% mod lattice (atol parses; int j on the mstring/
//     string/global/char* arms, long j on the int/long arms);
// (h) operator/ divx lattice + friends;
// (i) operator>= / <= families (numeric atol/atof for scalars,
//     string compares for string/mstring, global via >>).
// Consults (2026-11-25T15:02:00Z, nominal): row 98 hit-reuse
// (overload collapse onto String seats - the char*/string/mstring
// twins fold), row 101 hit-reuse (mstring is the String data fold;
// every member renders as data:String in, String out), row 103
// hit-reuse (get_global + tpx guard via the row-280
// globalShrFetchD TU service, promoted internal for this chunk -
// single-owner carrier), row 92 hit-reuse (GLOBAL_NOT_FOUND throw
// lives in the fetch seat), rows 54/68 hit-adapt (the C++
// reference/mutation receiver *this folds onto returned strings -
// DISCLOSED: the post-inc/decr arms return the OLD value per
// vendor; the caller seat owns the assignment), row 56 hit-adapt
// (atol/atof parses via atolD/atofD; cvt via cvtD of Misc6),
// row 37 hit-adapt (add/sub ride the raised stub seats addDaf/
// subDaf; add1/add3/divx RAISED HERE as named-owner stubs -
// owners interp-arith.cpp:632/647/682, not roster cells),
// row 33 hit-reuse (char t1[64] folding via DafCRuntime.cstring).
// VENDOR QUIRKS kept: (1) the +:global and -:global arms pass the
// fetched buffer as BOTH the b operand and the c result of add/sub
// (b/c alias); (2) the % mstring/string/global/char* arms use int
// j while the int/long arms use long j; (3) >= and <= compare
// scalars numerically but strings lexicographically (kept split);
// (4) unary + is data+0 through add1, unary - is 0-data; (5) the
// || int/long arms route via an mstring tmp that formats with the
// mstring ctor (%d), while || float/double route via a member
// mstring tmp promoted through + (which uses add3 %f) - kept.
// Dafny: EXEMPT (operator-relay lattice over raised kernels;
// journal row).

// ---------------------------------------------------------------------
// raised kernels (owners outside the chunk; named-owner stubs)
// ---------------------------------------------------------------------

// char * add1(const char *a, long b, char *c) - interp-arith.cpp:632
fun add1Daf(a: String, b: Long, c: CharArray): Unit =
    throw NotImplementedError(
        "add1: out-of-scope owner (interp-arith.cpp:632; not a roster cell)")

// char * add3(const char *a, double b, char *c) - interp-arith.cpp:647
fun add3Daf(a: String, b: Double, c: CharArray): Unit =
    throw NotImplementedError(
        "add3: out-of-scope owner (interp-arith.cpp:647; not a roster cell)")

// void divx(const char *a, const char *b, char *c) - interp-arith.cpp:682
fun divxDaf(a: String, b: String, c: CharArray): Unit =
    throw NotImplementedError(
        "divx: out-of-scope owner (interp-arith.cpp:682; not a roster cell)")

// ---------------------------------------------------------------------
// private kernel helpers (char t1[64] fold)
// ---------------------------------------------------------------------

// add1(data, i) -> String (the t1 buffer fold)
private fun add1S(data: String, i: Long): String {
    val t1 = CharArray(64)
    add1Daf(data, i, t1)
    return DafCRuntime.cstring(t1)
}

// add3(data, d) -> String
private fun add3S(data: String, d: Double): String {
    val t1 = CharArray(64)
    add3Daf(data, d, t1)
    return DafCRuntime.cstring(t1)
}

// add(data, s) -> String
private fun addS(data: String, s: String): String {
    val t1 = CharArray(64)
    addDaf(data, s, t1)
    return DafCRuntime.cstring(t1)
}

// sub(a, b) -> String
private fun subS(a: String, b: String): String {
    val t1 = CharArray(64)
    subDaf(a, b, t1)
    return DafCRuntime.cstring(t1)
}

// divx(a, b) -> String
private fun divxS(a: String, b: String): String {
    val t1 = CharArray(64)
    divxDaf(a, b, t1)
    return DafCRuntime.cstring(t1)
}

// ---------------------------------------------------------------------
// (a) mstring& mstring::operator=(global i)
// ---------------------------------------------------------------------

fun mstringAssignGD(i: MpsGlobalNode): String {
    // get_global(i.get(),tmp); if (!svPtr->tpx) throw
    val tmp = globalShrFetchD(i)
    return tmp // data = tmp; return *this
}

// ---------------------------------------------------------------------
// (b) mstring mstring::operator!()
// ---------------------------------------------------------------------

fun mstringNotD(data: String): String =
    if (data.isEmpty() || data == "0") "1" else "0"

// ---------------------------------------------------------------------
// (c) ++/-- and unary +/-
// ---------------------------------------------------------------------

// mstring mstring::operator++() { *this = *this + 1; }
fun mstringPreIncrD(data: String): String = add1S(data, 1)

fun mstringPreDecrD(data: String): String = subS(data, "1")

// mstring mstring::operator++(int) { tmp = *this; *this = *this+1;
// return tmp; } - the post arms return the OLD value (row 54/68
// receiver fold: the *this mutation belongs to the caller seat).
fun mstringPostIncrOldD(data: String): String = data
fun mstringPostDecrOldD(data: String): String = data

// unary + : *this + 0 (add1 %ld path)
fun mstringUnaryPlusD(data: String): String = add1S(data, 0)

// unary - : 0 - *this
fun mstringUnaryMinusD(data: String): String = subS("0", data)

// ---------------------------------------------------------------------
// (d) operator+ lattice
// ---------------------------------------------------------------------

// +int / +long: add1((const char*)data, i, t1)  (long cast)
fun mstringAddIntD(data: String, i: Long): String = add1S(data, i)

// +double / +float: add3(data, i, t1)
fun mstringAddDoubleD(data: String, i: Double): String = add3S(data, i)

// +mstring / +string / +const char* / +char*: add(data, i, t1)
fun mstringAddStrD(data: String, i: String): String = addS(data, i)

// +global: get_global into t1 then add(data,t1,t1) - the vendor
// b/c alias quirk (1); the buffer is shared here as in vendor.
fun mstringAddGlobalD(data: String, i: MpsGlobalNode): String {
    val t1 = globalShrFetchD(i) // get_global(i.get(),t1)
    return addS(data, t1)       // add(data,t1,t1)
}

// friend operator+(scalar/string/global/char*, mstring k): the
// commutative delegate return k+i (folded onto the member seats).
fun mstringFriendAddIntD(i: Long, k: String): String = mstringAddIntD(k, i)
fun mstringFriendAddDoubleD(i: Double, k: String): String = mstringAddDoubleD(k, i)
fun mstringFriendAddStrD(i: String, k: String): String = mstringAddStrD(k, i)
fun mstringFriendAddGlobalD(i: MpsGlobalNode, k: String): String = mstringAddGlobalD(k, i)

// ---------------------------------------------------------------------
// (e) operator|| concat lattice (member, then friend)
// ---------------------------------------------------------------------

// ||mstring / ||string / ||const char*: plain data concat
fun mstringCatD(data: String, s: String): String = data + s

// ||int: t1=i (mstring ctor %d) then a.data = data + t1.s_str()
fun mstringCatIntD(data: String, i: Int): String = data + mstringFromIntD(i)
fun mstringCatLongD(data: String, i: Long): String = data + mstringFromLongD(i)
// ||float / ||double route via the ctor %f (quirk 5 first half)
fun mstringCatFloatD(data: String, i: Float): String = data + mstringFromFloatD(i)
fun mstringCatDoubleD(data: String, i: Double): String = data + mstringFromDoubleD(i)

// ||global: i >> t1 (the >> string global arm of row 280)
fun mstringCatGlobalD(data: String, i: MpsGlobalNode): String =
    data + globalShrStrD(i)

// friend operator||(scalar/string/global/char*, mstring s)
fun mstringFriendCatStrD(i: String, s: String): String = i + s
fun mstringFriendCatIntD(i: Int, s: String): String = mstringFromIntD(i) + s
fun mstringFriendCatLongD(i: Long, s: String): String = mstringFromLongD(i) + s
// friend ||float/double (quirk 5 second half: mstring a; t1=i;
// a=t1+s promotes t1 via operator+ (the add3 %f path) before the
// || concat - kept through the add kernel):
fun mstringFriendCatFloatD(i: Float, s: String): String =
    mstringAddStrD(mstringFromFloatD(i), s)
fun mstringFriendCatDoubleD(i: Double, s: String): String =
    mstringAddStrD(mstringFromDoubleD(i), s)

fun mstringFriendCatGlobalD(i: MpsGlobalNode, s: String): String =
    globalShrStrD(i) + s

// ---------------------------------------------------------------------
// (f) operator- sub lattice
// ---------------------------------------------------------------------

// -int/-long/-double/-float: sub(data, cvt(i), t1)
fun mstringSubScalarD(data: String, i: Long): String = subS(data, cvtD(i))
fun mstringSubDoubleD(data: String, i: Double): String = subS(data, cvtD(i))
fun mstringSubFloatD(data: String, i: Float): String = subS(data, cvtD(i))

// -mstring / -string / -char* / -(const char*): sub(data, i, t1)
fun mstringSubStrD(data: String, i: String): String = subS(data, i)

// -global: get_global into t1 then sub(data,t1,t1) (alias quirk 1)
fun mstringSubGlobalD(data: String, i: MpsGlobalNode): String =
    subS(data, globalShrFetchD(i))

// friend operator-(scalar/string/global/char*, mstring k)
fun mstringFriendSubIntD(i: Long, k: String): String = subS(cvtD(i), k)
fun mstringFriendSubDoubleD(i: Double, k: String): String = subS(cvtD(i), k)
fun mstringFriendSubFloatD(i: Float, k: String): String = subS(cvtD(i), k)
fun mstringFriendSubStrD(i: String, k: String): String = subS(i, k)
fun mstringFriendSubGlobalD(i: MpsGlobalNode, k: String): String =
    subS(globalShrStrD(i), k)

// ---------------------------------------------------------------------
// (g) operator% mod lattice
// ---------------------------------------------------------------------

// %int / %long: long j = atol(data); j = j % i;
fun mstringModIntD(data: String, i: Long): String =
    mstringFromLongD(atolD(data) % i)

// %mstring / %string / %char*: int j (quirk 2)
fun mstringModStrD(data: String, i: String): String =
    mstringFromIntD(atoi(data) % atoi(i))

// %global: int j; int k = i (the int >> global arm); j = j % k
fun mstringModGlobalD(data: String, i: MpsGlobalNode): String =
    mstringFromIntD(atoi(data) % globalShrIntD(i))

// friend operator%(scalar/string/global/char*, mstring k)
fun mstringFriendModIntD(i: Long, k: String): String =
    mstringFromLongD(i % atolD(k))
fun mstringFriendModStrD(i: String, k: String): String =
    mstringFromIntD(atoi(i) % atoi(k))
fun mstringFriendModGlobalD(i: MpsGlobalNode, k: String): String =
    mstringFromLongD(globalShrLongD(i) % atolD(k))

// ---------------------------------------------------------------------
// (h) operator/ divx lattice + friends
// ---------------------------------------------------------------------

// /int / /long / /double / /float: divx(data, cvt(i), t1)
fun mstringDivScalarD(data: String, i: Long): String = divxS(data, cvtD(i))
fun mstringDivDoubleD(data: String, i: Double): String = divxS(data, cvtD(i))
fun mstringDivFloatD(data: String, i: Float): String = divxS(data, cvtD(i))

// /mstring / /string / /char*
fun mstringDivStrD(data: String, i: String): String = divxS(data, i)

// /global: get_global + divx(data, t1, t1) (alias quirk 1)
fun mstringDivGlobalD(data: String, i: MpsGlobalNode): String =
    divxS(data, globalShrFetchD(i))

// friend operator/(scalar/string/global/char*, mstring k)
fun mstringFriendDivIntD(i: Long, k: String): String = divxS(cvtD(i), k)
fun mstringFriendDivDoubleD(i: Double, k: String): String = divxS(cvtD(i), k)
fun mstringFriendDivFloatD(i: Float, k: String): String = divxS(cvtD(i), k)
fun mstringFriendDivStrD(i: String, k: String): String = divxS(i, k)
fun mstringFriendDivGlobalD(i: MpsGlobalNode, k: String): String =
    divxS(globalShrStrD(i), k)

// ---------------------------------------------------------------------
// (i) operator>= / <= families
// ---------------------------------------------------------------------

// >= scalars: numeric (atol/atof)
fun mstringGeIntD(data: String, i: Long): Boolean = atolD(data) >= i
fun mstringGeDoubleD(data: String, i: Double): Boolean = atofD(data) >= i
// >= string/mstring/char*: lexicographic
fun mstringGeStrD(data: String, i: String): Boolean = data >= i
// >= global
fun mstringGeGlobalD(data: String, i: MpsGlobalNode): Boolean =
    data >= globalShrStrD(i)

// <= scalars: numeric
fun mstringLeIntD(data: String, i: Long): Boolean = atolD(data) <= i
fun mstringLeDoubleD(data: String, i: Double): Boolean = atofD(data) <= i
// <= string/mstring/char*: lexicographic
fun mstringLeStrD(data: String, i: String): Boolean = data <= i
// <= global
fun mstringLeGlobalD(data: String, i: MpsGlobalNode): Boolean =
    data <= globalShrStrD(i)
