package doc

// Unit 62: libmpscpp.cpp::<misc-2> (batch 2, complex stratum; row 33:
// loc 600, ptr 136, ctrl 38, score 381.8, rank 296). The class-
// global arithmetic operator support: the compound assignment
// families (+=, -=, *=, /= over 8 C numeric types), the cast
// operator families (member this op s and friend s op global for
// +, -, and /), the global-vs-global double arithmetic via strtod,
// the pre/post increment/decrement stores, and the >= / <=
// comparison ladders. CHUNKER BOUNDARY (start): the pre-registered
// chunk opens with the orphan return-tmp tail of the previous
// chunk's function (owned by the misc-1 chunk; not rendered here).
// CHUNKER BOUNDARY (end): the chunk closes mid-signature inside
// int operator<=(short a,global b) - the incomplete <= friend is
// kept as the comment boundary note below. Render decisions per the
// PRO u62 lessons: the unsigned domains ride Kotlin UInt/UShort/ULong
// for signature distinctness (H-012); each this >> value /
// this = value idiom rides its per-type H-014 stub seat; the
// char tmp[STR_MAX] strtod parse buffers of the global-vs-global
// family are elided (transient parse-buffer role, H-007) in favor
// of the arm-wide C-protocol strtod; the int 1/0 returns of the
// comparisons render as Boolean; the two string-form >=/<= member
// overloads (char*, std::string) collapse onto one String seat
// (H-015 collision, ledgered). VENDOR QUIRKS kept: the member
// operator- returns s - this-value (operand order REVERSED
// relative to member operator+), the member operator/ returns
// value / s (opposite direction of the subtraction), and unsigned
// underflow wraps in C but throws on the JVM (documented
// divergence). The member operator* body lives in another chunk
// (not this unit's text): the *= family computes value * i
// directly with this ledger note. The increment/decrement floor
// check stores via the Long seat when the double is integral,
// exactly as the vendor.

// ---------------- compound assignment families ----------------

// global::operator+=(int)
fun GlobalRef.addAssignInt(i0: Int): Int {
    var i = i0
    i = this.plus(i) // member operator+ per the vendor
    Stubs.globalSetInt(this, i)
    return i
}

// global::operator+=(unsigned int)
fun GlobalRef.addAssignUInt(i0: UInt): UInt {
    var i = i0
    i = this.plus(i) // member operator+ per the vendor
    Stubs.globalSetUInt(this, i)
    return i
}

// global::operator+=(short)
fun GlobalRef.addAssignShort(i0: Short): Short {
    var i = i0
    i = this.plus(i) // member operator+ per the vendor
    Stubs.globalSetShort(this, i)
    return i
}

// global::operator+=(unsigned short)
fun GlobalRef.addAssignUShort(i0: UShort): UShort {
    var i = i0
    i = this.plus(i) // member operator+ per the vendor
    Stubs.globalSetUShort(this, i)
    return i
}

// global::operator+=(long)
fun GlobalRef.addAssignLong(i0: Long): Long {
    var i = i0
    i = this.plus(i) // member operator+ per the vendor
    Stubs.globalSetLong(this, i)
    return i
}

// global::operator+=(unsigned long)
fun GlobalRef.addAssignULong(i0: ULong): ULong {
    var i = i0
    i = this.plus(i) // member operator+ per the vendor
    Stubs.globalSetULong(this, i)
    return i
}

// global::operator+=(float)
fun GlobalRef.addAssignFloat(i0: Float): Float {
    var i = i0
    i = this.plus(i) // member operator+ per the vendor
    Stubs.globalSetFloat(this, i)
    return i
}

// global::operator+=(double)
fun GlobalRef.addAssignDouble(i0: Double): Double {
    var i = i0
    i = this.plus(i) // member operator+ per the vendor
    Stubs.globalSetDouble(this, i)
    return i
}

// global::operator-=(int)
fun GlobalRef.subAssignInt(i0: Int): Int {
    var i = i0
    val z = Stubs.globalGetInt(this)
    i = z - i
    Stubs.globalSetInt(this, i)
    return i
}

// global::operator-=(unsigned int)
fun GlobalRef.subAssignUInt(i0: UInt): UInt {
    var i = i0
    val z = Stubs.globalGetUInt(this)
    i = z - i
    Stubs.globalSetUInt(this, i)
    return i
}

// global::operator-=(short)
fun GlobalRef.subAssignShort(i0: Short): Short {
    var i = i0
    val z = Stubs.globalGetShort(this)
    i = (z - i).toShort()
    Stubs.globalSetShort(this, i)
    return i
}

// global::operator-=(unsigned short)
fun GlobalRef.subAssignUShort(i0: UShort): UShort {
    var i = i0
    val z = Stubs.globalGetUShort(this)
    i = (z - i).toUShort()
    Stubs.globalSetUShort(this, i)
    return i
}

// global::operator-=(long)
fun GlobalRef.subAssignLong(i0: Long): Long {
    var i = i0
    val z = Stubs.globalGetLong(this)
    i = z - i
    Stubs.globalSetLong(this, i)
    return i
}

// global::operator-=(unsigned long)
fun GlobalRef.subAssignULong(i0: ULong): ULong {
    var i = i0
    val z = Stubs.globalGetULong(this)
    i = z - i
    Stubs.globalSetULong(this, i)
    return i
}

// global::operator-=(float)
fun GlobalRef.subAssignFloat(i0: Float): Float {
    var i = i0
    val z = Stubs.globalGetFloat(this)
    i = z - i
    Stubs.globalSetFloat(this, i)
    return i
}

// global::operator-=(double)
fun GlobalRef.subAssignDouble(i0: Double): Double {
    var i = i0
    val z = Stubs.globalGetDouble(this)
    i = z - i
    Stubs.globalSetDouble(this, i)
    return i
}

// global::operator*=(int) - member operator* seat lives
// in another chunk; value * i computed directly (ledger note)
fun GlobalRef.mulAssignInt(i0: Int): Int {
    var i = i0
    i = Stubs.globalGetInt(this) * i
    Stubs.globalSetInt(this, i)
    return i
}

// global::operator*=(unsigned int) - member operator* seat lives
// in another chunk; value * i computed directly (ledger note)
fun GlobalRef.mulAssignUInt(i0: UInt): UInt {
    var i = i0
    i = Stubs.globalGetUInt(this) * i
    Stubs.globalSetUInt(this, i)
    return i
}

// global::operator*=(short) - member operator* seat lives
// in another chunk; value * i computed directly (ledger note)
fun GlobalRef.mulAssignShort(i0: Short): Short {
    var i = i0
    i = ( Stubs.globalGetShort(this) * i).toShort()
    Stubs.globalSetShort(this, i)
    return i
}

// global::operator*=(unsigned short) - member operator* seat lives
// in another chunk; value * i computed directly (ledger note)
fun GlobalRef.mulAssignUShort(i0: UShort): UShort {
    var i = i0
    i = ( Stubs.globalGetUShort(this) * i).toUShort()
    Stubs.globalSetUShort(this, i)
    return i
}

// global::operator*=(long) - member operator* seat lives
// in another chunk; value * i computed directly (ledger note)
fun GlobalRef.mulAssignLong(i0: Long): Long {
    var i = i0
    i = Stubs.globalGetLong(this) * i
    Stubs.globalSetLong(this, i)
    return i
}

// global::operator*=(unsigned long) - member operator* seat lives
// in another chunk; value * i computed directly (ledger note)
fun GlobalRef.mulAssignULong(i0: ULong): ULong {
    var i = i0
    i = Stubs.globalGetULong(this) * i
    Stubs.globalSetULong(this, i)
    return i
}

// global::operator*=(float) - member operator* seat lives
// in another chunk; value * i computed directly (ledger note)
fun GlobalRef.mulAssignFloat(i0: Float): Float {
    var i = i0
    i = Stubs.globalGetFloat(this) * i
    Stubs.globalSetFloat(this, i)
    return i
}

// global::operator*=(double) - member operator* seat lives
// in another chunk; value * i computed directly (ledger note)
fun GlobalRef.mulAssignDouble(i0: Double): Double {
    var i = i0
    i = Stubs.globalGetDouble(this) * i
    Stubs.globalSetDouble(this, i)
    return i
}

// global::operator/=(int) - result is value / i per the
// member operator/ direction quirk
fun GlobalRef.divAssignInt(i0: Int): Int {
    var i = i0
    i = Stubs.globalGetInt(this) / i
    Stubs.globalSetInt(this, i)
    return i
}

// global::operator/=(unsigned int) - result is value / i per the
// member operator/ direction quirk
fun GlobalRef.divAssignUInt(i0: UInt): UInt {
    var i = i0
    i = Stubs.globalGetUInt(this) / i
    Stubs.globalSetUInt(this, i)
    return i
}

// global::operator/=(short) - result is value / i per the
// member operator/ direction quirk
fun GlobalRef.divAssignShort(i0: Short): Short {
    var i = i0
    i = (Stubs.globalGetShort(this) / i).toShort()
    Stubs.globalSetShort(this, i)
    return i
}

// global::operator/=(unsigned short) - result is value / i per the
// member operator/ direction quirk
fun GlobalRef.divAssignUShort(i0: UShort): UShort {
    var i = i0
    i = (Stubs.globalGetUShort(this) / i).toUShort()
    Stubs.globalSetUShort(this, i)
    return i
}

// global::operator/=(long) - result is value / i per the
// member operator/ direction quirk
fun GlobalRef.divAssignLong(i0: Long): Long {
    var i = i0
    i = Stubs.globalGetLong(this) / i
    Stubs.globalSetLong(this, i)
    return i
}

// global::operator/=(unsigned long) - result is value / i per the
// member operator/ direction quirk
fun GlobalRef.divAssignULong(i0: ULong): ULong {
    var i = i0
    i = Stubs.globalGetULong(this) / i
    Stubs.globalSetULong(this, i)
    return i
}

// global::operator/=(float) - result is value / i per the
// member operator/ direction quirk
fun GlobalRef.divAssignFloat(i0: Float): Float {
    var i = i0
    i = Stubs.globalGetFloat(this) / i
    Stubs.globalSetFloat(this, i)
    return i
}

// global::operator/=(double) - result is value / i per the
// member operator/ direction quirk
fun GlobalRef.divAssignDouble(i0: Double): Double {
    var i = i0
    i = Stubs.globalGetDouble(this) / i
    Stubs.globalSetDouble(this, i)
    return i
}

// ---------------- cast operator families ----------------

// member global::operator+(int): s + this-value
operator fun GlobalRef.plus(s: Int): Int =
    s + Stubs.globalGetInt(this)

// member global::operator+(unsigned int): s + this-value
operator fun GlobalRef.plus(s: UInt): UInt =
    s + Stubs.globalGetUInt(this)

// member global::operator+(short): s + this-value
operator fun GlobalRef.plus(s: Short): Short =
    (s + Stubs.globalGetShort(this)).toShort()

// member global::operator+(unsigned short): s + this-value
operator fun GlobalRef.plus(s: UShort): UShort =
    (s + Stubs.globalGetUShort(this)).toUShort()

// member global::operator+(long): s + this-value
operator fun GlobalRef.plus(s: Long): Long =
    s + Stubs.globalGetLong(this)

// member global::operator+(unsigned long): s + this-value
operator fun GlobalRef.plus(s: ULong): ULong =
    s + Stubs.globalGetULong(this)

// member global::operator+(float): s + this-value
operator fun GlobalRef.plus(s: Float): Float =
    s + Stubs.globalGetFloat(this)

// member global::operator+(double): s + this-value
operator fun GlobalRef.plus(s: Double): Double =
    s + Stubs.globalGetDouble(this)

// member global::operator-(int): s - this-value (operand
// order quirk kept verbatim)
operator fun GlobalRef.minus(s: Int): Int =
    s - Stubs.globalGetInt(this)

// member global::operator-(unsigned int): s - this-value (operand
// order quirk kept verbatim)
operator fun GlobalRef.minus(s: UInt): UInt =
    s - Stubs.globalGetUInt(this)

// member global::operator-(short): s - this-value (operand
// order quirk kept verbatim)
operator fun GlobalRef.minus(s: Short): Short =
    (s - Stubs.globalGetShort(this)).toShort()

// member global::operator-(unsigned short): s - this-value (operand
// order quirk kept verbatim)
operator fun GlobalRef.minus(s: UShort): UShort =
    (s - Stubs.globalGetUShort(this)).toUShort()

// member global::operator-(long): s - this-value (operand
// order quirk kept verbatim)
operator fun GlobalRef.minus(s: Long): Long =
    s - Stubs.globalGetLong(this)

// member global::operator-(unsigned long): s - this-value (operand
// order quirk kept verbatim)
operator fun GlobalRef.minus(s: ULong): ULong =
    s - Stubs.globalGetULong(this)

// member global::operator-(float): s - this-value (operand
// order quirk kept verbatim)
operator fun GlobalRef.minus(s: Float): Float =
    s - Stubs.globalGetFloat(this)

// member global::operator-(double): s - this-value (operand
// order quirk kept verbatim)
operator fun GlobalRef.minus(s: Double): Double =
    s - Stubs.globalGetDouble(this)

// member global::operator/(int): this-value / s
operator fun GlobalRef.div(s: Int): Int =
    Stubs.globalGetInt(this) / s

// member global::operator/(unsigned int): this-value / s
operator fun GlobalRef.div(s: UInt): UInt =
    Stubs.globalGetUInt(this) / s

// member global::operator/(short): this-value / s
operator fun GlobalRef.div(s: Short): Short =
    (Stubs.globalGetShort(this) / s).toShort()

// member global::operator/(unsigned short): this-value / s
operator fun GlobalRef.div(s: UShort): UShort =
    (Stubs.globalGetUShort(this) / s).toUShort()

// member global::operator/(long): this-value / s
operator fun GlobalRef.div(s: Long): Long =
    Stubs.globalGetLong(this) / s

// member global::operator/(unsigned long): this-value / s
operator fun GlobalRef.div(s: ULong): ULong =
    Stubs.globalGetULong(this) / s

// member global::operator/(float): this-value / s
operator fun GlobalRef.div(s: Float): Float =
    Stubs.globalGetFloat(this) / s

// member global::operator/(double): this-value / s
operator fun GlobalRef.div(s: Double): Double =
    Stubs.globalGetDouble(this) / s

// friend operator+(int, global)
fun globalPlus(a: Int, g: GlobalRef): Int =
    a + Stubs.globalGetInt(g)

// friend operator+(unsigned int, global)
fun globalPlus(a: UInt, g: GlobalRef): UInt =
    a + Stubs.globalGetUInt(g)

// friend operator+(short, global)
fun globalPlus(a: Short, g: GlobalRef): Short =
    (a + Stubs.globalGetShort(g)).toShort()

// friend operator+(unsigned short, global)
fun globalPlus(a: UShort, g: GlobalRef): UShort =
    (a + Stubs.globalGetUShort(g)).toUShort()

// friend operator+(long, global)
fun globalPlus(a: Long, g: GlobalRef): Long =
    a + Stubs.globalGetLong(g)

// friend operator+(unsigned long, global)
fun globalPlus(a: ULong, g: GlobalRef): ULong =
    a + Stubs.globalGetULong(g)

// friend operator+(float, global)
fun globalPlus(a: Float, g: GlobalRef): Float =
    a + Stubs.globalGetFloat(g)

// friend operator+(double, global)
fun globalPlus(a: Double, g: GlobalRef): Double =
    a + Stubs.globalGetDouble(g)

// friend operator-(int, global)
fun globalMinus(a: Int, g: GlobalRef): Int =
    a - Stubs.globalGetInt(g)

// friend operator-(unsigned int, global)
fun globalMinus(a: UInt, g: GlobalRef): UInt =
    a - Stubs.globalGetUInt(g)

// friend operator-(short, global)
fun globalMinus(a: Short, g: GlobalRef): Short =
    (a - Stubs.globalGetShort(g)).toShort()

// friend operator-(unsigned short, global)
fun globalMinus(a: UShort, g: GlobalRef): UShort =
    (a - Stubs.globalGetUShort(g)).toUShort()

// friend operator-(long, global)
fun globalMinus(a: Long, g: GlobalRef): Long =
    a - Stubs.globalGetLong(g)

// friend operator-(unsigned long, global)
fun globalMinus(a: ULong, g: GlobalRef): ULong =
    a - Stubs.globalGetULong(g)

// friend operator-(float, global)
fun globalMinus(a: Float, g: GlobalRef): Float =
    a - Stubs.globalGetFloat(g)

// friend operator-(double, global)
fun globalMinus(a: Double, g: GlobalRef): Double =
    a - Stubs.globalGetDouble(g)

// friend operator/(int, global)
fun globalDiv(a: Int, g: GlobalRef): Int =
    a / Stubs.globalGetInt(g)

// friend operator/(unsigned int, global)
fun globalDiv(a: UInt, g: GlobalRef): UInt =
    a / Stubs.globalGetUInt(g)

// friend operator/(short, global)
fun globalDiv(a: Short, g: GlobalRef): Short =
    (a / Stubs.globalGetShort(g)).toShort()

// friend operator/(unsigned short, global)
fun globalDiv(a: UShort, g: GlobalRef): UShort =
    (a / Stubs.globalGetUShort(g)).toUShort()

// friend operator/(long, global)
fun globalDiv(a: Long, g: GlobalRef): Long =
    a / Stubs.globalGetLong(g)

// friend operator/(unsigned long, global)
fun globalDiv(a: ULong, g: GlobalRef): ULong =
    a / Stubs.globalGetULong(g)

// friend operator/(float, global)
fun globalDiv(a: Float, g: GlobalRef): Float =
    a / Stubs.globalGetFloat(g)

// friend operator/(double, global)
fun globalDiv(a: Double, g: GlobalRef): Double =
    a / Stubs.globalGetDouble(g)

// ---------------- global vs global (double via strtod) -------

// double global::operator+(global s) - both operands
// read through the string seat and strtod; result double
operator fun GlobalRef.plus(s: GlobalRef): Double {
    val b = strtod(Stubs.globalGetStrMax(s))
    val aa = strtod(Stubs.globalGetStrMax(this))
    return aa + b
}

// double global::operator-(global s) - both operands
// read through the string seat and strtod; result double
operator fun GlobalRef.minus(s: GlobalRef): Double {
    val b = strtod(Stubs.globalGetStrMax(s))
    val aa = strtod(Stubs.globalGetStrMax(this))
    return aa - b
}

// double global::operator/(global s) - both operands
// read through the string seat and strtod; result double
operator fun GlobalRef.div(s: GlobalRef): Double {
    val b = strtod(Stubs.globalGetStrMax(s))
    val aa = strtod(Stubs.globalGetStrMax(this))
    return aa / b
}

// ---------------- pre/post increment-decrement ----------------

// double global::operator++() - prefix
fun GlobalRef.preInc(): Double {
    var aa = Stubs.globalGetDouble(this)
    aa++
    if (kotlin.math.floor(aa) == aa)
        Stubs.globalSetLong(this, aa.toLong())
    else
        Stubs.globalSetDouble(this, aa)
    return aa
}

// double global::operator--() - prefix
fun GlobalRef.preDec(): Double {
    var aa = Stubs.globalGetDouble(this)
    aa--
    if (kotlin.math.floor(aa) == aa)
        Stubs.globalSetLong(this, aa.toLong())
    else
        Stubs.globalSetDouble(this, aa)
    return aa
}

// double global::operator++(int) - postfix stores b, returns a
fun GlobalRef.postInc(): Double {
    val aa = Stubs.globalGetDouble(this)
    val b = aa + 1
    if (kotlin.math.floor(b) == b)
        Stubs.globalSetLong(this, b.toLong())
    else
        Stubs.globalSetDouble(this, b)
    return aa
}

// double global::operator--(int) - postfix stores b, returns a
fun GlobalRef.postDec(): Double {
    val aa = Stubs.globalGetDouble(this)
    val b = aa - 1
    if (kotlin.math.floor(b) == b)
        Stubs.globalSetLong(this, b.toLong())
    else
        Stubs.globalSetDouble(this, b)
    return aa
}

// ---------------- comparison ladders ----------------

// member >= and <=: the global seat
fun GlobalRef.gte(a: GlobalRef): Boolean =
    Stubs.globalGetStrMax(this).compareTo(Stubs.globalGetStrMax(a)) >= 0

fun GlobalRef.lte(a: GlobalRef): Boolean =
    Stubs.globalGetStrMax(this).compareTo(Stubs.globalGetStrMax(a)) <= 0

// member global::operator>=(int)
fun GlobalRef.gte(a: Int): Boolean = Stubs.globalGetInt(this) >= a

// member global::operator<=(int)
fun GlobalRef.lte(a: Int): Boolean = Stubs.globalGetInt(this) <= a

// member global::operator>=(unsigned int)
fun GlobalRef.gte(a: UInt): Boolean = Stubs.globalGetUInt(this) >= a

// member global::operator<=(unsigned int)
fun GlobalRef.lte(a: UInt): Boolean = Stubs.globalGetUInt(this) <= a

// member global::operator>=(short)
fun GlobalRef.gte(a: Short): Boolean = Stubs.globalGetShort(this) >= a

// member global::operator<=(short)
fun GlobalRef.lte(a: Short): Boolean = Stubs.globalGetShort(this) <= a

// member global::operator>=(unsigned short)
fun GlobalRef.gte(a: UShort): Boolean = Stubs.globalGetUShort(this) >= a

// member global::operator<=(unsigned short)
fun GlobalRef.lte(a: UShort): Boolean = Stubs.globalGetUShort(this) <= a

// member global::operator>=(long)
fun GlobalRef.gte(a: Long): Boolean = Stubs.globalGetLong(this) >= a

// member global::operator<=(long)
fun GlobalRef.lte(a: Long): Boolean = Stubs.globalGetLong(this) <= a

// member global::operator>=(unsigned long)
fun GlobalRef.gte(a: ULong): Boolean = Stubs.globalGetULong(this) >= a

// member global::operator<=(unsigned long)
fun GlobalRef.lte(a: ULong): Boolean = Stubs.globalGetULong(this) <= a

// member global::operator>=(float)
fun GlobalRef.gte(a: Float): Boolean = Stubs.globalGetFloat(this) >= a

// member global::operator<=(float)
fun GlobalRef.lte(a: Float): Boolean = Stubs.globalGetFloat(this) <= a

// member global::operator>=(double)
fun GlobalRef.gte(a: Double): Boolean = Stubs.globalGetDouble(this) >= a

// member global::operator<=(double)
fun GlobalRef.lte(a: Double): Boolean = Stubs.globalGetDouble(this) <= a

// member >= and <= (char*, string): both string-form overloads
// collapse onto the String seat (H-015 collision, ledgered)
fun GlobalRef.gte(a: String): Boolean =
    Stubs.globalGetStrMax(this).compareTo(a) >= 0

fun GlobalRef.lte(a: String): Boolean =
    Stubs.globalGetStrMax(this).compareTo(a) <= 0

// friend operator>=(int, global)
fun gte(a: Int, g: GlobalRef): Boolean = a >= Stubs.globalGetInt(g)

// friend operator>=(unsigned int, global)
fun gte(a: UInt, g: GlobalRef): Boolean = a >= Stubs.globalGetUInt(g)

// friend operator>=(short, global)
fun gte(a: Short, g: GlobalRef): Boolean = a >= Stubs.globalGetShort(g)

// friend operator>=(unsigned short, global)
fun gte(a: UShort, g: GlobalRef): Boolean = a >= Stubs.globalGetUShort(g)

// friend operator>=(long, global)
fun gte(a: Long, g: GlobalRef): Boolean = a >= Stubs.globalGetLong(g)

// friend operator>=(unsigned long, global)
fun gte(a: ULong, g: GlobalRef): Boolean = a >= Stubs.globalGetULong(g)

// friend operator>=(float, global)
fun gte(a: Float, g: GlobalRef): Boolean = a >= Stubs.globalGetFloat(g)

// friend operator>=(double, global)
fun gte(a: Double, g: GlobalRef): Boolean = a >= Stubs.globalGetDouble(g)

// friend operator>=(string, global) - the char* friend collapses
// onto the same String seat (H-015 collision)
fun gte(a: String, g: GlobalRef): Boolean =
    a.compareTo(Stubs.globalGetStrMax(g)) >= 0

// friend operator<=(int, global) and <=(unsigned int, global); the
// <=(short, global) friend is CUT at the chunker boundary (its
// signature opens the truncated final line) and belongs to the
// next chunk (misc-3) - boundary glitch ledgered, not rendered
fun lte(a: Int, g: GlobalRef): Boolean = a <= Stubs.globalGetInt(g)

fun lte(a: UInt, g: GlobalRef): Boolean = a <= Stubs.globalGetUInt(g)

