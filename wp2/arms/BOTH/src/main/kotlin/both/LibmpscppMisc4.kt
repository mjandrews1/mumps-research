package both

// libmpscpp.cpp::<misc-4> - roster 304, batch B10, both_seq 181,
// cpp, complex stratum, loc 600, score 152.4. Vendor lines
// 8760-9713 of libmpscpp.cpp: the mstring operator lattice (misc
// residue chunk 4 - every face below is an mstring class member or
// friend too small for function extraction, so the chunk carries
// them as residue; the dump ends mid-face at operator<=(string),
// see the boundary ledger below).
//
// Consults: h019 hit (class_member_carrier: the mstring class
// layout has no base rule representation in this arm, roster unit
// mstringClass.h::<misc> 149 pending, so EVERY face renders
// package-level with the receiver state explicit - the String
// data carrier stands in for *this); h014 hit (the add1/add3/divx
// stub raises below); h007 reuse (the aliased in/out buffer t1 in
// the global operand faces - the vendor passes the same char array
// as input and output); h013 hit (the std::string int/long
// narrowing quirk in friend operator||(int/long, mstring),
// preserved verbatim below); h016 reuse (buffer sizes kept: 64-byte
// scratch for the small faces, STR_MAX for the global faces);
// h017 reuse (face names carry the mstring prefix; the vendor
// operator names live in the per-face pins).
//
// Folding ledger (same-body overload twins, Sim1W precedent - one
// Kotlin face, the vendor signatures named in its pin):
// - mstringPlus(String) folds operator+(mstring) 8852,
//   operator+(string) 8860, operator+(const char *) 8877 and
//   operator+(char *) 8885 (identical add() bodies after the
//   String carrier).
// - mstringConcat(String) folds operator||(mstring) 8893,
//   operator||(string) 8931 and operator||(const char *) 8945
//   (identical data+X bodies).
// - mstringMinus(String) folds operator-(mstring) 9035,
//   operator-(string) 9043, operator-(char *) 9051 and
//   operator-(const char *) 9059.
// - mstringMod(String) folds operator%(mstring) 9092,
//   operator%(string) 9100 and operator%(const char *) 9117
//   (the int-arithmetic members; see the long-vs-int ledger).
// - mstringDiv(String) folds operator/(mstring) 9222,
//   operator/(string) 9230 and operator/(const char *) 9247.
// - mstringGe(String) folds the char */const char */string/
//   mstring compare faces (the data<X lexicographic bodies; the
//   >=(mstring) variant 9667 IS in this unit).
// - mstringLe(String) folds only operator<=(char *) 9700,
//   operator<=(const char *) 9705 and operator<=(string) 9710:
//   the <=(global) and <=(mstring) faces fall OUTSIDE the unit
//   (roster 120, see the crosswalk below).
// - The Rev folds: mstringPlusRev(String) 9306/9314/9318,
//   mstringMinusRev(String) 9354/9372/9380,
//   mstringDivRev(String) 9478/9496,
//   mstringModRev(String) 9269/9283,
//   mstringConcatRev(String) 8951/8997.
//
// Lattice ownership crosswalk (dump verified against the
// regenerated chunk): the operator* family (member faces 9125-,
// friend faces 9388-9444 partial) is NOT in this chunk - it
// belongs to libmpscpp.cpp::<misc-8>, roster 306, rendered this
// batch in LibmpscppMisc8.kt (the mstringTimes faces); the
// operator< family (9504-9559), the operator> family (9560-9615)
// and the <=(global)/<=(mstring) tails belong to
// libmpscpp.cpp::<misc-9>, roster 120, batch 4 pending in this
// arm - NONE of them render here.
//
// long-vs-int ledger (member %): operator%(int) 9076 and
// operator%(long) 9084 compute in LONG (long j=atol(data); j=j%i)
// while the mstring/string/global/const char * members 9092-9117
// compute in INT (int j=atol(data); j=j%atol(...)) - the narrow
// members silently truncate on values past 32 bits. Kept: the
// Int/Long overloads run Long arithmetic, the String overload
// runs Int arithmetic.
//
// h013 vendor quirk (friend operator||(int i, mstring s) 8965 and
// operator||(long i, mstring s) 8973): the body declares
// std::string t1; t1=i; - std::string has no operator=(int), so
// the C++ compiler applies the implicit int/long-to-char
// conversion and assigns a ONE-CHARACTER string holding the low
// byte, then concatenates. The author almost certainly intended
// the numeric form (the float/double twins at 8981/8989 use an
// mstring carrier and the numeric add), but the compiled vendor
// behavior is the narrowing. Preserved verbatim: the Char(i and
// 0xFF) fold and the low-byte comment.
//
// Global operand faces: the vendor pattern is
// get_global(i.get(),t1) into a STR_MAX buffer, then the ALIASED
// call add/sub/divx(data, t1, t1) - the same buffer is input and
// output (h007). The operator=(global) face 8760 is the only one
// that checks svPtr->tpx and throws MumpsGlobalException with
// GlobalExceptionCode=GLOBAL_NOT_FOUND; the arithmetic faces
// +(global) 8868, -(global) 9067, /(global) 9238 skip the check
// (kept).
//
// Boundary ledger: the 600-loc chunk ends mid-face inside
// operator<=(string) at vendor 9710 (the dump's last line is the
// if (data<=i) return true; line; the else/closing braces belong
// to the next misc chunk). The face's semantics are unambiguous
// and it is rendered whole; <=(global) 9715 and <=(mstring) are
// NOT in this unit (roster 120 per the crosswalk above).
//
// Post-increment/decrement (h019 externalization): the vendor
// operator++(int) 8796 / operator--(int) 8802 mutate the receiver
// AND return the pre-value. The String carrier can express only
// the return, so the faces return the old value and the rebind
// (mstringPlus(data,1) / mstringMinus(data,1)) is the call
// site's duty; journaled for the capture-candidate promotion
// review at batch close.

// ------------------------------------------------------------------------
// Kernel and carrier seats (no mstring stub raises: every class
// member the lattice reaches already seats in the arm)
// ------------------------------------------------------------------------

// The add/sub/mult kernels: TODO-stubs at MpstimeMain.kt:364
// (add/sub) and Cannon.kt:44 (mult), owner the interp-arith.cpp
// residue (non-roster, the roster-67 renumber family). The
// add1/add3/divx kernels had NO seats - raised below with the same
// owner family. cvt: LibmpscppMisc6.kt:85-88. get_global: the
// getGlobalC macro fold (Dice.kt:62). The mstring numeric Assign
// family (operator=(int/long/float/double), vendor 8717-8745)
// seats at the mstringFrom* faces (LibmpscppMisc3.kt:510-519, the
// B9 misc-3 render); the || faces route t1=i through them.
// mstring::s_str (vendor 8492) belongs to roster 306 and renders
// real in LibmpscppMisc8.kt (mstringSStrZb); this unit's ||
// faces call it (the identity body makes the String-carrier
// round trip exact).

// void add1(const char *, int, char *) / void add3(char *, double,
// char *) / void divx(const char *, const char *, char *) - the
// interp-arith.cpp numeric kernels (the numSetup.h macro bodies of
// this operator family), residue of the interp-arith.cpp owner
// (non-roster; the add/sub/mult stub twins live at
// MpstimeMain.kt:364 and Cannon.kt:44 with the same owner family).
// NOTE: libmpscpp.h declares add1(char*, long, char*) while the
// mstring operator call sites pass int (the long narrows at the
// call edge; this stub carries the int form of the sites).
// TODO-stubs (h014), CharArray carriers to match those twins.
internal fun add1(a: CharArray, i: Int, rslt: CharArray) {
    TODO("stub: owner interp-arith.cpp::add1 (residue, non-roster)")
}

internal fun add3(a: CharArray, i: Double, rslt: CharArray) {
    TODO("stub: owner interp-arith.cpp::add3 (residue, non-roster)")
}

internal fun divx(a: CharArray, b: CharArray, rslt: CharArray) {
    TODO("stub: owner interp-arith.cpp::divx (residue, non-roster)")
}

// ------------------------------------------------------------------------
// Carriers (private)
// ------------------------------------------------------------------------

// char t1[64] / char t1[STR_MAX] scratch, NUL-terminated C-string
// style to match the add/sub/mult kernel stubs.
private const val MS_SMALL = 64

private fun carr(s: String): CharArray {
    val a = CharArray(MS_SMALL)
    writeC(a, s)
    return a
}

// ------------------------------------------------------------------------
// Member faces
// ------------------------------------------------------------------------

// mstring& mstring::operator=(global i) - libmpscpp.cpp:8760. The
// get_global macro fold (svPtr->tpx=Mglobal(RETRIEVE,...)) seats at
// getGlobalC (Dice.kt:62); the fail-throw idiom is the Dice.kt:105
// one. Face returns the new data (the assignment result).
internal fun mstringAssignGlobal(i: GlobalRef): String {
    val tmp = CharArray(STR_MAX)
    val f = getGlobalC(i.ref, tmp, svPtr)
    if (f == 0) {
        GlobalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }
    return cStringOf(tmp)
}

// mstring mstring::operator!() - libmpscpp.cpp:8773. Length()==0
// folds to data.isEmpty(); *this=="0" is the mstring==(const
// char *) compare folding to String equality.
internal fun mstringNot(data: String): String {
    return if (data.isEmpty() || data == "0") "1" else "0"
}

// mstring mstring::operator++() - libmpscpp.cpp:8786
// (*this = *this + 1 seats through the int plus face).
internal fun mstringIncrPre(data: String): String {
    return mstringPlus(data, 1)
}

// mstring mstring::operator--() - libmpscpp.cpp:8791
internal fun mstringDecrPre(data: String): String {
    return mstringMinus(data, 1)
}

// mstring mstring::operator++(int) - libmpscpp.cpp:8796. Returns
// the pre-value; the receiver rebind (data = mstringPlus(data,1))
// is the call site's duty (h019 externalization, see header).
internal fun mstringIncrPost(data: String): String {
    return data
}

// mstring mstring::operator--(int) - libmpscpp.cpp:8802
internal fun mstringDecrPost(data: String): String {
    return data
}

// mstring mstring::operator+() (unary plus) - libmpscpp.cpp:8808
// (tmp = *this + 0).
internal fun mstringUnaryPlus(data: String): String {
    return mstringPlus(data, 0)
}

// mstring mstring::operator-() (unary minus) - libmpscpp.cpp:8814
// (tmp = 0 - *this seats through the friend minus: sub(cvt(0),k)).
internal fun mstringUnaryMinus(data: String): String {
    val t1 = CharArray(MS_SMALL)
    sub(carr(data), carr(cvt(0)), t1)
    return cStringOf(t1)
}

// mstring mstring::operator+(int i) - libmpscpp.cpp:8820
// (add1 kernel).
internal fun mstringPlus(data: String, i: Int): String {
    val t1 = CharArray(MS_SMALL)
    add1(carr(data), i, t1)
    return cStringOf(t1)
}

// mstring mstring::operator+(long i) - libmpscpp.cpp:8828. The
// vendor narrows (int)i before add1 (Long-to-Int truncation kept).
internal fun mstringPlus(data: String, i: Long): String {
    val t1 = CharArray(MS_SMALL)
    add1(carr(data), i.toInt(), t1)
    return cStringOf(t1)
}

// mstring mstring::operator+(double i) - libmpscpp.cpp:8836
// (add3 kernel).
internal fun mstringPlus(data: String, i: Double): String {
    val t1 = CharArray(MS_SMALL)
    add3(carr(data), i, t1)
    return cStringOf(t1)
}

// mstring mstring::operator+(float i) - libmpscpp.cpp:8844
// ((double)i widening kept).
internal fun mstringPlus(data: String, i: Float): String {
    val t1 = CharArray(MS_SMALL)
    add3(carr(data), i.toDouble(), t1)
    return cStringOf(t1)
}

// mstring mstring::operator+(mstring i) 8852 / operator+(string i)
// 8860 / operator+(const char * i) 8877 / operator+(char * i) 8885
// - folded (identical add() bodies after the String carrier).
internal fun mstringPlus(data: String, i: String): String {
    val t1 = CharArray(MS_SMALL)
    add(carr(data), carr(i), t1)
    return cStringOf(t1)
}

// mstring mstring::operator+(global i) - libmpscpp.cpp:8868. The
// ALIASED call add(data, t1, t1): the STR_MAX fetch buffer is both
// input and output (h007). No tpx check in the vendor (kept).
internal fun mstringPlus(data: String, i: GlobalRef): String {
    val t1 = CharArray(STR_MAX)
    getGlobalC(i.ref, t1, svPtr)
    add(carr(data), t1, t1)
    return cStringOf(t1)
}

// mstring mstring::operator||(mstring i) 8893 / operator||(string
// i) 8931 / operator||(const char * i) 8945 - folded (plain
// std::string concatenation, no s_str normalization on these
// three).
internal fun mstringConcat(data: String, i: String): String {
    return data + i
}

// mstring mstring::operator||(int i) - libmpscpp.cpp:8899. The
// vendor builds an mstring t1 via operator=(int) (the mstringFromInt
// seat) then reads t1.s_str() (the roster-306 mstringSStrZb seat).
internal fun mstringConcat(data: String, i: Int): String {
    val t1 = mstringFromInt(i)
    return data + mstringSStrZb(t1)
}

// mstring mstring::operator||(long i) - libmpscpp.cpp:8907
internal fun mstringConcat(data: String, i: Long): String {
    val t1 = mstringFromLong(i)
    return data + mstringSStrZb(t1)
}

// mstring mstring::operator||(float i) - libmpscpp.cpp:8915
internal fun mstringConcat(data: String, i: Float): String {
    val t1 = mstringFromFloat(i)
    return data + mstringSStrZb(t1)
}

// mstring mstring::operator||(double i) - libmpscpp.cpp:8923
internal fun mstringConcat(data: String, i: Double): String {
    val t1 = mstringFromDouble(i)
    return data + mstringSStrZb(t1)
}

// mstring mstring::operator||(global i) - libmpscpp.cpp:8937. The
// i >> t1 global extract (globalClass.h operator>>) seats at
// GlobalRef.extractValue (the roster-306 flipped seat).
internal fun mstringConcat(data: String, i: GlobalRef): String {
    val t1 = i.extractValue()
    return data + t1
}

// mstring mstring::operator-(int i) 9003 / (long i) 9011 /
// (double i) 9019 / (float i) 9027 - the cvt() normalization then
// the sub kernel (identical bodies; kept as typed overloads).
internal fun mstringMinus(data: String, i: Int): String {
    val t1 = CharArray(MS_SMALL)
    sub(carr(data), carr(cvt(i)), t1)
    return cStringOf(t1)
}

internal fun mstringMinus(data: String, i: Long): String {
    val t1 = CharArray(MS_SMALL)
    sub(carr(data), carr(cvt(i)), t1)
    return cStringOf(t1)
}

internal fun mstringMinus(data: String, i: Double): String {
    val t1 = CharArray(MS_SMALL)
    sub(carr(data), carr(cvt(i)), t1)
    return cStringOf(t1)
}

internal fun mstringMinus(data: String, i: Float): String {
    val t1 = CharArray(MS_SMALL)
    sub(carr(data), carr(cvt(i)), t1)
    return cStringOf(t1)
}

// mstring mstring::operator-(mstring i) 9035 / (string i) 9043 /
// (char * i) 9051 / (const char * i) 9059 - folded.
internal fun mstringMinus(data: String, i: String): String {
    val t1 = CharArray(MS_SMALL)
    sub(carr(data), carr(i), t1)
    return cStringOf(t1)
}

// mstring mstring::operator-(global i) - libmpscpp.cpp:9067
// (aliased sub(data, t1, t1), h007; no tpx check, kept).
internal fun mstringMinus(data: String, i: GlobalRef): String {
    val t1 = CharArray(STR_MAX)
    getGlobalC(i.ref, t1, svPtr)
    sub(carr(data), t1, t1)
    return cStringOf(t1)
}

// mstring mstring::operator%(int i) - libmpscpp.cpp:9076. LONG
// arithmetic (long j=atol(data); j=j%i), see the long-vs-int
// ledger in the header.
internal fun mstringMod(data: String, i: Int): String {
    val j = atolC(data)
    return (j % i).toString()
}

// mstring mstring::operator%(long i) - libmpscpp.cpp:9084 (LONG)
internal fun mstringMod(data: String, i: Long): String {
    val j = atolC(data)
    return (j % i).toString()
}

// mstring mstring::operator%(mstring i) 9092 / %(string i) 9100 /
// %(const char * i) 9117 - folded. INT arithmetic (int j=atol...),
// the narrow-member truncation kept.
internal fun mstringMod(data: String, i: String): String {
    val j = atolC(data).toInt()
    return (j % atolC(i).toInt()).toString()
}

// mstring mstring::operator%(global i) - libmpscpp.cpp:9108. The
// int k=i global cast folds to atol of the extracted value (the
// globalClass.h operator int member is untranslated, roster 89
// owner; the extractValue seat carries it). INT arithmetic.
internal fun mstringMod(data: String, i: GlobalRef): String {
    val j = atolC(data).toInt()
    val k = atolC(i.extractValue()).toInt()
    return (j % k).toString()
}

// mstring mstring::operator/(int i) 9190 / /(long i) 9198 /
// /(double i) 9206 / /(float i) 9214 - cvt then the divx kernel.
internal fun mstringDiv(data: String, i: Int): String {
    val t1 = CharArray(MS_SMALL)
    divx(carr(data), carr(cvt(i)), t1)
    return cStringOf(t1)
}

internal fun mstringDiv(data: String, i: Long): String {
    val t1 = CharArray(MS_SMALL)
    divx(carr(data), carr(cvt(i)), t1)
    return cStringOf(t1)
}

internal fun mstringDiv(data: String, i: Double): String {
    val t1 = CharArray(MS_SMALL)
    divx(carr(data), carr(cvt(i)), t1)
    return cStringOf(t1)
}

internal fun mstringDiv(data: String, i: Float): String {
    val t1 = CharArray(MS_SMALL)
    divx(carr(data), carr(cvt(i)), t1)
    return cStringOf(t1)
}

// mstring mstring::operator/(mstring i) 9222 / /(string i) 9230 /
// /(const char * i) 9247 - folded.
internal fun mstringDiv(data: String, i: String): String {
    val t1 = CharArray(MS_SMALL)
    divx(carr(data), carr(i), t1)
    return cStringOf(t1)
}

// mstring mstring::operator/(global i) - libmpscpp.cpp:9238
// (aliased divx(data, t1, t1), h007).
internal fun mstringDiv(data: String, i: GlobalRef): String {
    val t1 = CharArray(STR_MAX)
    getGlobalC(i.ref, t1, svPtr)
    divx(carr(data), t1, t1)
    return cStringOf(t1)
}

// ------------------------------------------------------------------------
// Member comparator faces (>= at 9616-9671, <= at 9672-9710
// partial; the < and > families belong to roster 120, see the
// ownership crosswalk ledger in the header). Numeric faces:
// long j=atol(data) for int/long, double j=atof(data) for
// double/float (the atof fold seats at strtodC, BothNumRuntime
// carrier). String faces: std::string lexicographic comparison
// (data<X) folds to Kotlin String comparison. Global faces: the
// i >> t1 extract then the compare.
// ------------------------------------------------------------------------

// >= family
internal fun mstringGe(data: String, i: Int): Boolean =
    atolC(data) >= i.toLong()

internal fun mstringGe(data: String, i: Long): Boolean =
    atolC(data) >= i

internal fun mstringGe(data: String, i: Double): Boolean =
    strtodC(data) >= i

internal fun mstringGe(data: String, i: Float): Boolean =
    strtodC(data) >= i.toDouble()

// operator>=(char * i) 9644 / (const char * i) 9649 / (string i)
// 9654 / (mstring i) 9667 - folded.
internal fun mstringGe(data: String, i: String): Boolean =
    data >= i

internal fun mstringGe(data: String, i: GlobalRef): Boolean {
    val t1 = i.extractValue()
    return data >= t1
}

// <= family (partial at the chunk boundary: <=(global) 9715 and
// <=(mstring) are outside the unit, roster 120 per the ownership
// crosswalk ledger in the header).
internal fun mstringLe(data: String, i: Int): Boolean =
    atolC(data) <= i.toLong()

internal fun mstringLe(data: String, i: Long): Boolean =
    atolC(data) <= i

internal fun mstringLe(data: String, i: Double): Boolean =
    strtodC(data) <= i

internal fun mstringLe(data: String, i: Float): Boolean =
    strtodC(data) <= i.toDouble()

// operator<=(char * i) 9700 / (const char * i) 9705 / (string i)
// 9710 (the chunk cuts this face mid-body; the semantics are
// unambiguous) - folded.
internal fun mstringLe(data: String, i: String): Boolean =
    data <= i

// ------------------------------------------------------------------------
// Friend faces (operator@(T i, mstring k), the reversed lattice)
// ------------------------------------------------------------------------

// mstring operator||(string i, mstring s) 8951 / ||(const char * i,
// mstring s) 8997 - folded: i + s.s_str() (s_str seats at the
// roster-306 mstringSStrZb).
internal fun mstringConcatRev(i: String, s: String): String {
    return i + mstringSStrZb(s)
}

// mstring operator||(global i, mstring s) - libmpscpp.cpp:8957
internal fun mstringConcatRev(i: GlobalRef, s: String): String {
    val t1 = i.extractValue()
    return t1 + mstringSStrZb(s)
}

// mstring operator||(int i, mstring s) - libmpscpp.cpp:8965. h013
// vendor quirk: std::string t1; t1=i narrows the int to char (a
// one-character string holding the low byte) - preserved verbatim,
// see the header ledger.
internal fun mstringConcatRev(i: Int, s: String): String {
    val t1 = Char(i and 0xFF).toString()
    return t1 + mstringSStrZb(s)
}

// mstring operator||(long i, mstring s) - libmpscpp.cpp:8973 (same
// h013 narrowing quirk; the Long low-byte fold).
internal fun mstringConcatRev(i: Long, s: String): String {
    val t1 = Char((i and 0xFFL).toInt()).toString()
    return t1 + mstringSStrZb(s)
}

// mstring operator||(float i, mstring s) - libmpscpp.cpp:8981.
// Unlike the int/long twins this one builds an mstring t1 (the
// mstringFromFloat seat) and uses the numeric operator+(mstring)
// member: a = t1 + s seats through mstringPlus.
internal fun mstringConcatRev(i: Float, s: String): String {
    val t1 = mstringFromFloat(i)
    return mstringPlus(t1, s)
}

// mstring operator||(double i, mstring s) - libmpscpp.cpp:8989
internal fun mstringConcatRev(i: Double, s: String): String {
    val t1 = mstringFromDouble(i)
    return mstringPlus(t1, s)
}

// mstring operator%(int i, mstring k) - libmpscpp.cpp:9257. LONG
// arithmetic: i % atol(k) promotes the int to long.
internal fun mstringModRev(i: Int, k: String): String {
    val j = i.toLong() % atolC(k)
    return j.toString()
}

// mstring operator%(long i, mstring k) - libmpscpp.cpp:9263
internal fun mstringModRev(i: Long, k: String): String {
    val j = i % atolC(k)
    return j.toString()
}

// mstring operator%(string i, mstring k) 9269 / %(const char * i,
// mstring k) 9283 - folded (atol(i) % atol(k), both long).
internal fun mstringModRev(i: String, k: String): String {
    val j = atolC(i) % atolC(k)
    return j.toString()
}

// mstring operator%(global i, mstring k) - libmpscpp.cpp:9275.
// long m; i >> m (the global long-extract fold through
// extractValue + atol).
internal fun mstringModRev(i: GlobalRef, k: String): String {
    val m = atolC(i.extractValue())
    val j = m % atolC(k)
    return j.toString()
}

// mstring operator+(int i, mstring k) 9290 / (long i) 9294 /
// (double i) 9298 / (float i) 9302 / (string i) 9306 /
// (const char * i) 9314 / (char * i) 9318 / (global i) 9310 - the
// vendor bodies are all the single line return k+i, so every
// friend plus delegates to the matching member face (kept as
// typed overloads; the three String-operand signatures fold).
internal fun mstringPlusRev(i: Int, k: String): String =
    mstringPlus(k, i)

internal fun mstringPlusRev(i: Long, k: String): String =
    mstringPlus(k, i)

internal fun mstringPlusRev(i: Double, k: String): String =
    mstringPlus(k, i)

internal fun mstringPlusRev(i: Float, k: String): String =
    mstringPlus(k, i)

internal fun mstringPlusRev(i: String, k: String): String =
    mstringPlus(k, i)

internal fun mstringPlusRev(i: GlobalRef, k: String): String =
    mstringPlus(k, i)

// mstring operator-(int i, mstring k) 9322 / (long i) 9330 /
// (double i) 9338 / (float i) 9346 - sub(cvt(i), k.data).
internal fun mstringMinusRev(i: Int, k: String): String {
    val t1 = CharArray(MS_SMALL)
    sub(carr(cvt(i)), carr(k), t1)
    return cStringOf(t1)
}

internal fun mstringMinusRev(i: Long, k: String): String {
    val t1 = CharArray(MS_SMALL)
    sub(carr(cvt(i)), carr(k), t1)
    return cStringOf(t1)
}

internal fun mstringMinusRev(i: Double, k: String): String {
    val t1 = CharArray(MS_SMALL)
    sub(carr(cvt(i)), carr(k), t1)
    return cStringOf(t1)
}

internal fun mstringMinusRev(i: Float, k: String): String {
    val t1 = CharArray(MS_SMALL)
    sub(carr(cvt(i)), carr(k), t1)
    return cStringOf(t1)
}

// mstring operator-(string i, mstring k) 9354 / (const char * i,
// mstring k) 9372 / (char * i, mstring k) 9380 - folded
// (sub(i, k.data)).
internal fun mstringMinusRev(i: String, k: String): String {
    val t1 = CharArray(MS_SMALL)
    sub(carr(i), carr(k), t1)
    return cStringOf(t1)
}

// mstring operator-(global i, mstring k) - libmpscpp.cpp:9362
// (i >> t2 extract, then sub(t2, k.data)).
internal fun mstringMinusRev(i: GlobalRef, k: String): String {
    val t2 = i.extractValue()
    val t1 = CharArray(MS_SMALL)
    sub(carr(t2), carr(k), t1)
    return cStringOf(t1)
}

// mstring operator/(int i, mstring k) 9446 / (long i) 9454 /
// (double i) 9462 / (float i) 9470 - divx(cvt(i), k).
internal fun mstringDivRev(i: Int, k: String): String {
    val t1 = CharArray(MS_SMALL)
    divx(carr(cvt(i)), carr(k), t1)
    return cStringOf(t1)
}

internal fun mstringDivRev(i: Long, k: String): String {
    val t1 = CharArray(MS_SMALL)
    divx(carr(cvt(i)), carr(k), t1)
    return cStringOf(t1)
}

internal fun mstringDivRev(i: Double, k: String): String {
    val t1 = CharArray(MS_SMALL)
    divx(carr(cvt(i)), carr(k), t1)
    return cStringOf(t1)
}

internal fun mstringDivRev(i: Float, k: String): String {
    val t1 = CharArray(MS_SMALL)
    divx(carr(cvt(i)), carr(k), t1)
    return cStringOf(t1)
}

// mstring operator/(string i, mstring k) 9478 / (const char * i,
// mstring k) 9496 - folded (divx(i, k)).
internal fun mstringDivRev(i: String, k: String): String {
    val t1 = CharArray(MS_SMALL)
    divx(carr(i), carr(k), t1)
    return cStringOf(t1)
}

// mstring operator/(global i, mstring k) - libmpscpp.cpp:9486
// (i >> t2 extract, then divx(t2, k)).
internal fun mstringDivRev(i: GlobalRef, k: String): String {
    val t2 = i.extractValue()
    val t1 = CharArray(MS_SMALL)
    divx(carr(t2), carr(k), t1)
    return cStringOf(t1)
}
