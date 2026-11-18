package mumps.mdh

import mumps.interp.Msv
import mumps.interp.add
import mumps.interp.add1
import mumps.interp.add3
import mumps.interp.cstringAt
import mumps.interp.divx
import mumps.interp.sub
import mumps.interp.writeCstring

/**
 * PRO-arm translation of libmpscpp.cpp::<misc-4> (batch 10 unit,
 * roster row 283, file rank 283). Translated independently from
 * the pristine vendor source (mumps-14-may-2026.tgz, sha256
 * 621deb25...ffdbe); the fourth 600-physLOC residual chunk of
 * libmpscpp.cpp (identity re-verified via the study tool: loc
 * 600, comment_ratio 0.008, pointer_marks 18, control_marks 18,
 * score 152.4 - exact match to wp1/study-subset/units.csv row
 * 283). Slice coverage (vendor lines 8760-9711): the mstring
 * operator= (global) tail, the operator! / increment/decrement
 * x4 / unary + and - set, the operator+ member family x9, the
 * concatenating operator|<dollar-vertical-bar>| family x8 with
 * its x7 friend forms, the operator- member family x9, the
 * operator% family x6, the operator/ family x8, the friend
 * %/+/-/// families (x5/x8/x8/x7), the mstring operator>= x9
 * and the operator<= int/long/double heads (the slice closes at
 * vendor 9711, the return-true line inside the <=(string) body;
 * the float/char-star/string/global/mstring forms of the <=
 * family carry with the LibmpscppMisc9 seats - verification
 * only here).
 *
 * STUB RESOLUTIONS this unit carries: the mPlus/mMinus/mDiv/
 * mRem per-file private stubs raised by LibmpscppMisc3.kt (row
 * 249, TAG CORRECTIONS - the compound assignments delegate to
 * the operator+/-////% member seats landed HERE) and the mTimes
 * family (owner libmpscpp.cpp operator* members rendered at
 * row 281, LibmpscppMisc8.kt); the Misc3 stubs are removed and
 * the compound-assignment call sites bind the real operators
 * (cleanLocks precedent, unit 56).
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h008 static_scratch   : no file-scope scratch in the chunk;
 *    every staging buffer is a per-call CharArray.
 *  - h010 numeric_promotion: the C truncations are kept: the
 *    long operator+ body calls add1 with (int)i, the string lhs
 *    of the friend or-concat family assigns int/long through
 *    std::string::operator=(char) - ONE char holding the C-wide
 *    truncation of i (vendor fossil, body noted) - while the
 *    float/double friends convert through the mstring ctor.
 *  - h012 unsigned_byte_domain: every char t1 staging buffer
 *    rides CharArray(64) exactly as the C array (the overflow is
 *    a bounded-array exception here); the global forms stage in
 *    STR_MAX buffers.
 *  - h013 dead_store       : none beyond the h010 fossils.
 *  - h014 out_of_scope_call: add/add3/add1/sub/divx and mult are
 *    the DONE interp seats (ArithMisc.kt); cvt is the Misc6 seat
 *    set; get_global is the RESOLVED macro seat; Global.toDouble
 *    is the u31 cast carrier the removable global-int implicit
 *    rides; setFrom is the Misc3 operator= seat the ++/--/ops
 *    re-use, exactly as the vendor re-uses operator=.
 *  - h015 grammar_collision: the || concat family has no Kotlin
 *    operator point - named orConcat (member + friend forms,
 *    overload-disjoint); the char-star and const char-star C
 *    overload twins fold to the CharArray form (noted per site);
 *    the >/<= Boolean families render as ge seats and compareTo
 *    extensions (unit-62 ge/le and Misc9 compareTo precedents);
 *    prefix/postfix increment-decrement fold to the inc/dec
 *    operator pair (Kotlin ++/-- give both C++ value shapes).
 *  - h017 naming          : setFrom/inc/dec/unaryPlus/
 *    unaryMinus/not/plus/minus/rem/div keep Kotlin operator
 *    tokens; the vendor line numbers ride every site comment.
 *
 * Vendor notes (translated faithfully, not \\\"fixed\\\"):
 *  - operator!/++/-- : Length()==0 or ==zero literal is checked
 *    over the data carrier; *this==\\\"0\\\" compares against the
 *    VALUE string exactly as string == const char*.
 *  - operator+(global)/-(global)/(global) read the operand into
 *    t1 and run add/sub/divx with t1 AS THE OUTPUT TOO (vendor
 *    aliasing, kept verbatim).
 *  - operator%(int/long) floor-truncate with C % semantics on
 *    the atol'ed value (Kotlin % is the same truncating form);
 *    the mstring/string/char-star forms use int j (vendor int,
 *    not long) - kept.
 *  - friend operator-(float/double) stage through a fresh
 *    mstring like the vendor; friend operator||(int/long)
 *    stage through the one-char string assignment fossil
 *    described at h010.
 *  - The unary minus body is 0-*this - the friend minus seats
 *    below carry it; the unary plus body is *this+0 through
 *    add1.
 */

private const val STR_MAX = 32000 // vendor strmax.h:3 (file-private, u44 precedent)
private const val GLOBAL_NOT_FOUND = 2 // vendor libmpscpp.h:71

private fun toCbuf(s: String): CharArray {
    val b = CharArray(s.length + 1)
    writeCstring(b, s)
    return b
}

/** atol over the String value (Mstring.kt atol_ precedent). */
private fun atol_(s: String): Long = s.trim().toLongOrNull() ?: 0L

// ---------------------------------------------------------------------------
// vendor 8760: mstring & mstring::operator=(global i) - the operator=
// tail that closes the Misc3 setFrom family (TAG CORRECTION: the seat
// lands here, the Mstring.kt/GlobalCarrier binds unchanged).
// ---------------------------------------------------------------------------

/** vendor 8760: mstring::operator=(global) - fetch + gate + assign. */
fun Mstring.setFrom(i: Global) {
    val svPtr: Msv = MdhState.svPtr
    val tmp = CharArray(STR_MAX)
    get_global(toCbuf(i.ref), tmp)
    if (svPtr.tpx == 0L) {
        MdhState.globalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }
    value = cstringAt(tmp) // vendor: data=tmp
}

// ---------------------------------------------------------------------------
// vendor 8773-8818: operator! , increment/decrement x4, unary +/-. The
// prefix and postfix C++ pairs fold to the Kotlin inc/dec operators
// (a++ yields the old value, ++a the new - both vendor shapes ride).
// ---------------------------------------------------------------------------

/** vendor 8773: mstring mstring::operator!() - 0/1 NOT ladder. */
operator fun Mstring.not(): Mstring {
    val tmp = Mstring()
    tmp.setFrom(if (value.isEmpty() || value == "0") "1" else "0") // vendor: Length()==0 || *this=="0"
    return tmp
}

/** vendor 8786: mstring mstring::operator++() - *this=*this+1. */
operator fun Mstring.inc(): Mstring {
    setFrom(this + 1)
    return this
}

/** vendor 8791: mstring mstring::operator--() - *this=*this-1. */
operator fun Mstring.dec(): Mstring {
    setFrom(this - 1)
    return this
}

// vendor 8796/8802: the postfix ++/-- bodies take a copy first, bump,
// and return the copy - exactly the Kotlin++/-- inc/dec shapes above
// (h015 fold).

/** vendor 8807: mstring mstring::operator+() - *this+0 (add1 form). */
operator fun Mstring.unaryPlus(): Mstring = this + 0

/** vendor 8813: mstring mstring::operator-() - 0-*this (friend form). */
operator fun Mstring.unaryMinus(): Mstring = 0 - this

// ---------------------------------------------------------------------------
// vendor 8820-8891: the operator+ member family. char t1[64] staging
// kept; add1 takes the long operand, the global form aliases t1.
// ---------------------------------------------------------------------------

/** vendor 8820: mstring mstring::operator+(int i) - add1(data, i, t1). */
operator fun Mstring.plus(i: Int): Mstring {
    val tmp = Mstring()
    val t1 = CharArray(64)
    add1(toCbuf(value), 0, i.toLong(), t1, 0)
    tmp.value = cstringAt(t1)
    return tmp
}

/** vendor 8827: mstring mstring::operator+(long i) - add1(data,(int)i,t1):
 *  the vendor (int) cast truncates - kept (h010). */
operator fun Mstring.plus(i: Long): Mstring {
    val tmp = Mstring()
    val t1 = CharArray(64)
    add1(toCbuf(value), 0, i.toInt().toLong(), t1, 0) // vendor: (int)i
    tmp.value = cstringAt(t1)
    return tmp
}

/** vendor 8834: mstring mstring::operator+(double i) - add3 data form. */
operator fun Mstring.plus(i: Double): Mstring {
    val tmp = Mstring()
    val t1 = CharArray(64)
    add3(toCbuf(value), 0, i, t1, 0)
    tmp.value = cstringAt(t1)
    return tmp
}

/** vendor 8841: mstring mstring::operator+(float i) - add3(double)i. */
operator fun Mstring.plus(i: Float): Mstring {
    val tmp = Mstring()
    val t1 = CharArray(64)
    add3(toCbuf(value), 0, i.toDouble(), t1, 0) // vendor: (double)i
    tmp.value = cstringAt(t1)
    return tmp
}

/** vendor 8848: mstring mstring::operator+(mstring i) - add pair. */
operator fun Mstring.plus(i: Mstring): Mstring {
    val tmp = Mstring()
    val t1 = CharArray(64)
    add(toCbuf(value), 0, toCbuf(i.value), 0, t1, 0)
    tmp.value = cstringAt(t1)
    return tmp
}

/** vendor 8856: mstring mstring::operator+(string i). */
operator fun Mstring.plus(i: String): Mstring {
    val tmp = Mstring()
    val t1 = CharArray(64)
    add(toCbuf(value), 0, toCbuf(i), 0, t1, 0)
    tmp.value = cstringAt(t1)
    return tmp
}

/** vendor 8863: mstring mstring::operator+(global i) - fetch into t1
 *  and add data x t1 INTO t1 (vendor aliasing kept). */
operator fun Mstring.plus(i: Global): Mstring {
    val tmp = Mstring()
    val t1 = CharArray(STR_MAX)
    get_global(toCbuf(i.ref), t1)
    add(toCbuf(value), 0, t1, 0, t1, 0)
    tmp.value = cstringAt(t1)
    return tmp
}

/** vendor 8874/8880: operator+(const char*)/(char*) twins (fold, h015). */
operator fun Mstring.plus(i: CharArray): Mstring {
    val tmp = Mstring()
    val t1 = CharArray(64)
    add(toCbuf(value), 0, i, 0, t1, 0)
    tmp.value = cstringAt(t1)
    return tmp
}

// ---------------------------------------------------------------------------
// vendor 8893-9002: the concatenating operator|| family (Kotlin has no
// || operator for values - orConcat seats, h015). Numeric lhs forms
// stage through the mstring ctor; the int/long FRIENDS stage through
// std::string = char (ONE char, the C truncation of i - vendor fossil
// kept per h010); the float/double friends stage through mstring.
// ---------------------------------------------------------------------------

/** vendor 8893: mstring mstring::operator||(mstring i). */
fun Mstring.orConcat(i: Mstring): Mstring {
    val a = Mstring()
    a.value = value + i.value // vendor: a.data=data+i.data
    return a
}

/** vendor 8899: mstring mstring::operator||(int i) - mstring(int) staging. */
fun Mstring.orConcat(i: Int): Mstring {
    val a = Mstring()
    val t1 = Mstring()
    t1.setFrom(i) // vendor mstring(int) ctor ("%d", row-281 owner)
    a.value = value + t1.value
    return a
}

/** vendor 8906: mstring mstring::operator||(long i). */
fun Mstring.orConcat(i: Long): Mstring {
    val a = Mstring()
    val t1 = Mstring()
    t1.setFrom(i)
    a.value = value + t1.value
    return a
}

/** vendor 8913: mstring mstring::operator||(float i). */
fun Mstring.orConcat(i: Float): Mstring {
    val a = Mstring()
    val t1 = Mstring()
    t1.setFrom(i)
    a.value = value + t1.value
    return a
}

/** vendor 8920: mstring mstring::operator||(double i). */
fun Mstring.orConcat(i: Double): Mstring {
    val a = Mstring()
    val t1 = Mstring()
    t1.setFrom(i)
    a.value = value + t1.value
    return a
}

/** vendor 8927: mstring mstring::operator||(string i). */
fun Mstring.orConcat(i: String): Mstring {
    val a = Mstring()
    a.value = value + i
    return a
}

/** vendor 8934: mstring mstring::operator||(global i) - i>>t1. */
fun Mstring.orConcat(i: Global): Mstring {
    val a = Mstring()
    val t1 = i.readInto() // vendor: string t1; i >> t1 (Misc3 seat)
    a.value = value + t1
    return a
}

/** vendor 8941: mstring mstring::operator||(const char * i). */
fun Mstring.orConcat(i: CharArray): Mstring {
    val a = Mstring()
    a.value = value + cstringAt(i)
    return a
}

/** vendor 8948: mstring operator||(string i, mstring s). */
fun orConcat(i: String, s: Mstring): Mstring {
    val a = Mstring()
    a.value = i + s.value
    return a
}

/** vendor 8956: mstring operator||(global i, mstring s). */
fun orConcat(i: Global, s: Mstring): Mstring {
    val a = Mstring()
    val t1 = i.readInto() // vendor: i >> t1
    a.value = t1 + s.value
    return a
}

/** vendor 8964: mstring operator||(int i, mstring s) - the
 *  std::string=char one-char staging fossil (h010). */
fun orConcat(i: Int, s: Mstring): Mstring {
    val a = Mstring()
    val t1 = String(charArrayOf(i.toInt().toChar())) // vendor: t1=i (char low byte)
    a.value = t1 + s.value
    return a
}

/** vendor 8972: mstring operator||(long i, mstring s) - same fossil. */
fun orConcat(i: Long, s: Mstring): Mstring {
    val a = Mstring()
    val t1 = String(charArrayOf(i.toInt().toChar())) // vendor: t1=(char)i
    a.value = t1 + s.value
    return a
}

/** vendor 8980: mstring operator||(float i, mstring s) - mstring form. */
fun orConcat(i: Float, s: Mstring): Mstring {
    val a = Mstring()
    val t1 = Mstring()
    t1.setFrom(i)
    return t1.plus(s) // vendor: a=t1+s (operator+ member)
}

/** vendor 8988: mstring operator||(double i, mstring s) - mstring form. */
fun orConcat(i: Double, s: Mstring): Mstring {
    val a = Mstring()
    val t1 = Mstring()
    t1.setFrom(i)
    return t1.plus(s)
}

/** vendor 8996: mstring operator||(const char * i, mstring s). */
fun orConcat(i: CharArray, s: Mstring): Mstring {
    val a = Mstring()
    a.value = cstringAt(i) + s.value
    return a
}

// ---------------------------------------------------------------------------
// vendor 9003-9075: the operator- member family.
// ---------------------------------------------------------------------------

/** vendor 9003: mstring mstring::operator-(int i) - sub(data,cvt(i),t1). */
operator fun Mstring.minus(i: Int): Mstring {
    val tmp = Mstring()
    val t1 = CharArray(64)
    sub(toCbuf(value), 0, toCbuf(cvt(i)), 0, t1, 0)
    tmp.value = cstringAt(t1)
    return tmp
}

/** vendor 9010: mstring mstring::operator-(long i). */
operator fun Mstring.minus(i: Long): Mstring {
    val tmp = Mstring()
    val t1 = CharArray(64)
    sub(toCbuf(value), 0, toCbuf(cvt(i)), 0, t1, 0)
    tmp.value = cstringAt(t1)
    return tmp
}

/** vendor 9017: mstring mstring::operator-(double i). */
operator fun Mstring.minus(i: Double): Mstring {
    val tmp = Mstring()
    val t1 = CharArray(64)
    sub(toCbuf(value), 0, toCbuf(cvt(i)), 0, t1, 0)
    tmp.value = cstringAt(t1)
    return tmp
}

/** vendor 9024: mstring mstring::operator-(float i). */
operator fun Mstring.minus(i: Float): Mstring {
    val tmp = Mstring()
    val t1 = CharArray(64)
    sub(toCbuf(value), 0, toCbuf(cvt(i)), 0, t1, 0)
    tmp.value = cstringAt(t1)
    return tmp
}

/** vendor 9031: mstring mstring::operator-(mstring i). */
operator fun Mstring.minus(i: Mstring): Mstring {
    val tmp = Mstring()
    val t1 = CharArray(64)
    sub(toCbuf(value), 0, toCbuf(i.value), 0, t1, 0)
    tmp.value = cstringAt(t1)
    return tmp
}

/** vendor 9038: mstring mstring::operator-(string i). */
operator fun Mstring.minus(i: String): Mstring {
    val tmp = Mstring()
    val t1 = CharArray(64)
    sub(toCbuf(value), 0, toCbuf(i), 0, t1, 0)
    tmp.value = cstringAt(t1)
    return tmp
}

/** vendor 9045/9052: operator-(char*)/(const char*) twins (fold). */
operator fun Mstring.minus(i: CharArray): Mstring {
    val tmp = Mstring()
    val t1 = CharArray(64)
    sub(toCbuf(value), 0, i, 0, t1, 0)
    tmp.value = cstringAt(t1)
    return tmp
}

/** vendor 9059: mstring mstring::operator-(global i) - fetch + alias. */
operator fun Mstring.minus(i: Global): Mstring {
    val tmp = Mstring()
    val t1 = CharArray(STR_MAX)
    get_global(toCbuf(i.ref), t1)
    sub(toCbuf(value), 0, t1, 0, t1, 0)
    tmp.value = cstringAt(t1)
    return tmp
}

// ---------------------------------------------------------------------------
// vendor 9076-9189: the operator% family (C truncating % on the
// atol'ed carriers; the mstring/string/char-star forms use int j -
// vendor int, kept).
// ---------------------------------------------------------------------------

/** vendor 9076: mstring mstring::operator%(int i) - long j. */
operator fun Mstring.rem(i: Int): Mstring {
    var j = atol_(value) // vendor: long j=atol(data.c_str())
    j %= i.toLong()
    return Mstring(j.toString()) // vendor: tmp=j ("%ld" ctor)
}

/** vendor 9083: mstring mstring::operator%(long i). */
operator fun Mstring.rem(i: Long): Mstring {
    var j = atol_(value)
    j %= i
    return Mstring(j.toString())
}

/** vendor 9090: mstring mstring::operator%(mstring i) - int j. */
operator fun Mstring.rem(i: Mstring): Mstring {
    var j = atol_(value).toInt() // vendor: int j=atol(...)
    j %= atol_(i.value).toInt()
    return Mstring(j.toString()) // vendor: tmp=j ("%d" ctor)
}

/** vendor 9097: mstring mstring::operator%(string i). */
operator fun Mstring.rem(i: String): Mstring {
    var j = atol_(value).toInt()
    j %= atol_(i).toInt()
    return Mstring(j.toString())
}

/** vendor 9104: mstring mstring::operator%(global i) - int k=i. */
operator fun Mstring.rem(i: Global): Mstring {
    var j = atol_(value).toInt()
    val k = i.Int() // vendor: int k=i (global::Int() conversion seat, h014)
    j %= k
    return Mstring(j.toString())
}

/** vendor 9111: mstring mstring::operator%(const char * i). */
operator fun Mstring.rem(i: CharArray): Mstring {
    var j = atol_(value).toInt()
    j %= atol_(cstringAt(i)).toInt()
    return Mstring(j.toString())
}

// ---------------------------------------------------------------------------
// vendor 9190-9256: the operator/ member family (divx core).
// ---------------------------------------------------------------------------

/** vendor 9190: mstring mstring::operator/(int i). */
operator fun Mstring.div(i: Int): Mstring {
    val tmp = Mstring()
    val t1 = CharArray(64)
    divx(toCbuf(value), 0, toCbuf(cvt(i)), 0, t1, 0)
    tmp.value = cstringAt(t1)
    return tmp
}

/** vendor 9197: mstring mstring::operator/(long i). */
operator fun Mstring.div(i: Long): Mstring {
    val tmp = Mstring()
    val t1 = CharArray(64)
    divx(toCbuf(value), 0, toCbuf(cvt(i)), 0, t1, 0)
    tmp.value = cstringAt(t1)
    return tmp
}

/** vendor 9204: mstring mstring::operator/(double i). */
operator fun Mstring.div(i: Double): Mstring {
    val tmp = Mstring()
    val t1 = CharArray(64)
    divx(toCbuf(value), 0, toCbuf(cvt(i)), 0, t1, 0)
    tmp.value = cstringAt(t1)
    return tmp
}

/** vendor 9211: mstring mstring::operator/(float i). */
operator fun Mstring.div(i: Float): Mstring {
    val tmp = Mstring()
    val t1 = CharArray(64)
    divx(toCbuf(value), 0, toCbuf(cvt(i)), 0, t1, 0)
    tmp.value = cstringAt(t1)
    return tmp
}

/** vendor 9218: mstring mstring::operator/(mstring i). */
operator fun Mstring.div(i: Mstring): Mstring {
    val tmp = Mstring()
    val t1 = CharArray(64)
    divx(toCbuf(value), 0, toCbuf(i.value), 0, t1, 0)
    tmp.value = cstringAt(t1)
    return tmp
}

/** vendor 9225: mstring mstring::operator/(string i). */
operator fun Mstring.div(i: String): Mstring {
    val tmp = Mstring()
    val t1 = CharArray(64)
    divx(toCbuf(value), 0, toCbuf(i), 0, t1, 0)
    tmp.value = cstringAt(t1)
    return tmp
}

/** vendor 9232: mstring mstring::operator/(global i) - fetch + alias. */
operator fun Mstring.div(i: Global): Mstring {
    val tmp = Mstring()
    val t1 = CharArray(STR_MAX)
    get_global(toCbuf(i.ref), t1)
    divx(toCbuf(value), 0, t1, 0, t1, 0)
    tmp.value = cstringAt(t1)
    return tmp
}

/** vendor 9239: mstring mstring::operator/(const char * i). */
operator fun Mstring.div(i: CharArray): Mstring {
    val tmp = Mstring()
    val t1 = CharArray(64)
    divx(toCbuf(value), 0, i, 0, t1, 0)
    tmp.value = cstringAt(t1)
    return tmp
}

// ---------------------------------------------------------------------------
// vendor 9257-9408: the friend operator families.
// ---------------------------------------------------------------------------

/** vendor 9257: mstring operator%(int i, mstring k). */
operator fun Int.rem(k: Mstring): Mstring = (this % atol_(k.value)).let { Mstring(it.toString()) }

/** vendor 9264: mstring operator%(long i, mstring k). */
operator fun Long.rem(k: Mstring): Mstring = (this % atol_(k.value)).let { Mstring(it.toString()) }

/** vendor 9271: mstring operator%(string i, mstring k). */
operator fun String.rem(k: Mstring): Mstring =
    (atol_(this).toInt() % atol_(k.value).toInt()).let { Mstring(it.toString()) }

/** vendor 9278: mstring operator%(global i, mstring k) - i>>m. */
operator fun Global.rem(k: Mstring): Mstring {
    val m = this.readInto() // readInto(): String (the >> string seat)
    return (atol_(m).toInt() % atol_(k.value).toInt()).let { Mstring(it.toString()) }
}

/** vendor 9285: mstring operator%(const char * i, mstring k). */
operator fun CharArray.rem(k: Mstring): Mstring =
    (atol_(cstringAt(this)).toInt() % atol_(k.value).toInt()).let { Mstring(it.toString()) }

/** vendor 9290: mstring operator+(int i, mstring k) - reversed. */
operator fun Int.plus(k: Mstring): Mstring = k + this

/** vendor 9297: mstring operator+(long i, mstring k). */
operator fun Long.plus(k: Mstring): Mstring = k + this

/** vendor 9304: mstring operator+(double i, mstring k). */
operator fun Double.plus(k: Mstring): Mstring = k + this

/** vendor 9311: mstring operator+(float i, mstring k). */
operator fun Float.plus(k: Mstring): Mstring = k + this

/** vendor 9318: mstring operator+(string i, mstring k) - plusM
 *  seat: the Kotlin String.plus member shadows any extension of the
 *  same name (h015 grammar_collision, orConcat precedent). */
fun String.plusM(k: Mstring): Mstring = k + this

/** vendor 9325: mstring operator+(global i, mstring k). */
operator fun Global.plus(k: Mstring): Mstring = k + this

/** vendor 9332: mstring operator+(const char * i, mstring k). */
operator fun CharArray.plus(k: Mstring): Mstring = k + this

// vendor 9339: operator+(char*, mstring) - the const char* twin folds
// into the CharArray seat above (h015).

/** vendor 9346: mstring operator-(int i, mstring k) - sub(cvt(i),k,t1). */
operator fun Int.minus(k: Mstring): Mstring {
    val tmp = Mstring()
    val t1 = CharArray(64)
    sub(toCbuf(cvt(this)), 0, toCbuf(k.value), 0, t1, 0)
    tmp.value = cstringAt(t1)
    return tmp
}

/** vendor 9353: mstring operator-(long i, mstring k). */
operator fun Long.minus(k: Mstring): Mstring {
    val tmp = Mstring()
    val t1 = CharArray(64)
    sub(toCbuf(cvt(this)), 0, toCbuf(k.value), 0, t1, 0)
    tmp.value = cstringAt(t1)
    return tmp
}

/** vendor 9360: mstring operator-(double i, mstring k). */
operator fun Double.minus(k: Mstring): Mstring {
    val tmp = Mstring()
    val t1 = CharArray(64)
    sub(toCbuf(cvt(this)), 0, toCbuf(k.value), 0, t1, 0)
    tmp.value = cstringAt(t1)
    return tmp
}

/** vendor 9367: mstring operator-(float i, mstring k). */
operator fun Float.minus(k: Mstring): Mstring {
    val tmp = Mstring()
    val t1 = CharArray(64)
    sub(toCbuf(cvt(this)), 0, toCbuf(k.value), 0, t1, 0)
    tmp.value = cstringAt(t1)
    return tmp
}

/** vendor 9374: mstring operator-(string i, mstring k). */
operator fun String.minus(k: Mstring): Mstring {
    val tmp = Mstring()
    val t1 = CharArray(64)
    sub(toCbuf(this), 0, toCbuf(k.value), 0, t1, 0)
    tmp.value = cstringAt(t1)
    return tmp
}

/** vendor 9381: mstring operator-(global i, mstring k) - i>>t2. */
operator fun Global.minus(k: Mstring): Mstring {
    val tmp = Mstring()
    val t1 = CharArray(64)
    val t2 = this.readInto() // vendor: string t2; i >> t2
    sub(toCbuf(t2), 0, toCbuf(k.value), 0, t1, 0)
    tmp.value = cstringAt(t1)
    return tmp
}

/** vendor 9394: mstring operator-(const char * i, mstring k). */
operator fun CharArray.minus(k: Mstring): Mstring {
    val tmp = Mstring()
    val t1 = CharArray(64)
    sub(this, 0, toCbuf(k.value), 0, t1, 0)
    tmp.value = cstringAt(t1)
    return tmp
}

// vendor 9401: operator-(char*, mstring) - the const char* twin folds
// into the CharArray seat above (h015).

/** vendor 9446: mstring operator/(int i, mstring k) - divx(cvt(i),k,t1). */
operator fun Int.div(k: Mstring): Mstring {
    val tmp = Mstring()
    val t1 = CharArray(64)
    divx(toCbuf(cvt(this)), 0, toCbuf(k.value), 0, t1, 0)
    tmp.value = cstringAt(t1)
    return tmp
}

/** vendor 9453: mstring operator/(long i, mstring k). */
operator fun Long.div(k: Mstring): Mstring {
    val tmp = Mstring()
    val t1 = CharArray(64)
    divx(toCbuf(cvt(this)), 0, toCbuf(k.value), 0, t1, 0)
    tmp.value = cstringAt(t1)
    return tmp
}

/** vendor 9460: mstring operator/(double i, mstring k). */
operator fun Double.div(k: Mstring): Mstring {
    val tmp = Mstring()
    val t1 = CharArray(64)
    divx(toCbuf(cvt(this)), 0, toCbuf(k.value), 0, t1, 0)
    tmp.value = cstringAt(t1)
    return tmp
}

/** vendor 9467: mstring operator/(float i, mstring k). */
operator fun Float.div(k: Mstring): Mstring {
    val tmp = Mstring()
    val t1 = CharArray(64)
    divx(toCbuf(cvt(this)), 0, toCbuf(k.value), 0, t1, 0)
    tmp.value = cstringAt(t1)
    return tmp
}

/** vendor 9474: mstring operator/(string i, mstring k). */
operator fun String.div(k: Mstring): Mstring {
    val tmp = Mstring()
    val t1 = CharArray(64)
    divx(toCbuf(this), 0, toCbuf(k.value), 0, t1, 0)
    tmp.value = cstringAt(t1)
    return tmp
}

/** vendor 9481: mstring operator/(global i, mstring k) - i>>t2. */
operator fun Global.div(k: Mstring): Mstring {
    val tmp = Mstring()
    val t1 = CharArray(64)
    val t2 = this.readInto()
    divx(toCbuf(t2), 0, toCbuf(k.value), 0, t1, 0)
    tmp.value = cstringAt(t1)
    return tmp
}

/** vendor 9488: mstring operator/(const char * i, mstring k). */
operator fun CharArray.div(k: Mstring): Mstring {
    val tmp = Mstring()
    val t1 = CharArray(64)
    divx(this, 0, toCbuf(k.value), 0, t1, 0)
    tmp.value = cstringAt(t1)
    return tmp
}

// ---------------------------------------------------------------------------
// vendor 9616-9711: the mstring operator>= family (bool, ge seats -
// unit-62 ge/le naming precedent) and the operator<= int/long/double
// heads (the float/char-star/string/global/mstring forms of <= are
// the LibmpscppMisc9 compareTo seats - verification only, no
// redeclaration; the slice closes at the <=(string) return-true line).
// ---------------------------------------------------------------------------

/** vendor 9616: bool mstring::operator>=(int i) - atol ladder. */
fun Mstring.ge(i: Int): Boolean {
    val j = atol_(value) // vendor: long j=atol(data.c_str())
    return j >= i
}

/** vendor 9622: bool mstring::operator>=(long i). */
fun Mstring.ge(i: Long): Boolean {
    val j = atol_(value)
    return j >= i
}

/** vendor 9628: bool mstring::operator>=(double i) - atof ladder. */
fun Mstring.ge(i: Double): Boolean {
    val j = value.trim().toDoubleOrNull() ?: 0.0 // vendor: double j=atof(...)
    return j >= i
}

/** vendor 9634: bool mstring::operator>=(float i) - atof lhs. */
fun Mstring.ge(i: Float): Boolean {
    val j = value.trim().toDoubleOrNull() ?: 0.0
    return j >= i
}

/** vendor 9640/9646: operator>=(char*)/(const char*) twins (fold). */
fun Mstring.ge(i: CharArray): Boolean = value >= cstringAt(i)

/** vendor 9652: bool mstring::operator>=(string i). */
fun Mstring.ge(i: String): Boolean = value >= i

/** vendor 9693-note: the Misc9 seat renders <=(float) as compareTo. */

/** vendor 9658: bool mstring::operator>=(global i) - i>>t1. */
fun Mstring.ge(i: Global): Boolean {
    val t1 = i.readInto()
    return value >= t1
}

/** vendor 9664: bool mstring::operator>=(mstring i). */
fun Mstring.ge(i: Mstring): Boolean = value >= i.value

/** vendor 9672: bool mstring::operator<=(int i) - atol ladder. */
operator fun Mstring.compareTo(i: Int): Int {
    val j = atol_(value)
    return if (j > i) 1 else if (j < i) -1 else 0 // <=(int) Boolean as cmp
}

/** vendor 9679: bool mstring::operator<=(long i). */
operator fun Mstring.compareTo(i: Long): Int {
    val j = atol_(value)
    return if (j > i) 1 else if (j < i) -1 else 0
}

/** vendor 9686: bool mstring::operator<=(double i) - atof ladder. */
operator fun Mstring.compareTo(i: Double): Int {
    val j = value.trim().toDoubleOrNull() ?: 0.0
    return if (j > i) 1 else if (j < i) -1 else 0
}

// The remaining <= forms (float, char*, const char*, string, global,
// mstring) are the DONE LibmpscppMisc9 compareTo seats - verification
// only; the slice's final carrier line (9711) is the return-true arm
// of the <=(string) body the Misc9 seat owns.
