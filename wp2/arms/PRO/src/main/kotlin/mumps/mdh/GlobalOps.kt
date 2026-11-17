package mumps.mdh

import mumps.interp.cstringAt
import mumps.interp.strlenC
import mumps.interp.strncmpC
import mumps.interp.strtodC
import kotlin.math.floor

/**
 * PRO-arm translation of libmpscpp.cpp::<misc-2> (pilot B2, unit 62,
 * provisional under deviation C-003). Translated independently from
 * the pristine vendor source (mumps-14-may-2026.tgz, sha256
 * 621deb25...ffdbe), the second 600-LOC residual chunk of
 * libmpscpp.cpp (units.csv: loc 600, score 381.8, rank 296; original
 * source lines 4680-6155 plus the chunk's closing head line): the
 * class global arithmetic operator family - the 32 compound
 * assignments (+=, -=, *=, /= over int/unsigned int/short/unsigned
 * short/long/unsigned long/float/double), the numeric +, -, / casts
 * in both operand orders, the global+global / -global / /global
 * forms, the four ++/-- forms, and the >= and <= comparison families.
 *
 * The chunk boundary cuts mid-family: the friend operator<=(short,
 * global) head is the chunk's last line and its body belongs to the
 * misc-3 chunk (u31 boundary precedent, noted below); the friend
 * <= family renders here only through unsigned int, exactly as the
 * chunk does.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h010 numeric_promotion  : the vendor's unsigned C types map to
 *    the Kotlin unsigned carriers UInt/UShort/ULong so each vendor
 *    overload keeps a distinct signature; the ++/-- integral store
 *    keeps the vendor's (long) truncation via toLong().
 *  - h011 control_flow       : the one-line if/return bodies render
 *    as single-expression ifs, shape kept.
 *  - h014 out_of_scope_call  : the numeric extraction family
 *    (global::operator>>(T&) / (char*)) and insertion family
 *    (operator<<(long)/(double)) are owned by the misc-3 residual
 *    chunk and become owner-named MdhStubs stubs
 *    (extractInt/extractUInt/extractShort/extractUShort/extractLong/
 *    extractULong/extractFloat/extractDouble/extractCstring/
 *    insertLong/insertDouble); the compound assigns' *this = i goes
 *    through the class global operator= machinery (libmpscpp.h, not
 *    a corpus unit) as assignInt/assignUInt/assignShort/assignUShort/
 *    assignLong/assignULong/assignFloat/assignDouble stubs. strtodC
 *    is the real B1 reuse (MpsAcos.kt).
 *  - h015 kotlin_keyword    : the vendor parameter names all survive
 *    except where noted (s, a, b, i are legal).
 *  - h017 naming            : operator overloads use Kotlin operator
 *    conventions where a convention exists (plus/minus/div/
 *    plusAssign/minusAssign/timesAssign/divAssign); >= and <= have
 *    no Kotlin function-name form and render as ge/le (vendor names
 *    in comments); ++/-- return the numeric value, not the object,
 *    so they cannot use Kotlin inc/dec conventions and render as
 *    incrPre/incrPost/decrPre/decrPost.
 *
 * Vendor notes (translated faithfully, not fixed):
 *  - global::operator-(T s) returns s - i, i.e. the MEMBER minus
 *    computes rhs - this-value, exactly as the friend form does -
 *    so a - b and b - a yield the same value; kept.
 *  - global::operator/(T s) returns i/s (value / rhs) while the
 *    friend operator/(T s, global a) returns s/i (rhs / value);
 *    kept.
 *  - global >= global and global <= global compare the STRING forms
 *    (strcmp), not numeric values; kept.
 *  - the compound assignments return the arithmetic result; as
 *    Kotlin assignment operators must return Unit, the vendor return
 *    value is documented here and discarded.
 *  - the post ++/-- forms return the OLD value after storing the
 *    new one, as there.
 */

private const val STR_MAX = 32000 // vendor strmax.h:3

/** strcmp over two NUL-terminated CharArray regions. */
private fun strcmpA(a: CharArray, b: CharArray): Int {
    val bs = cstringAt(b)
    return strncmpC(a, 0, bs, strlenC(b) + 1)
}

// ---------------------------------------------------------------------------
// Compound assignments: int, unsigned int, short, unsigned short, long,
// unsigned long, float, double. Vendor: i = (*this)+i; *this = i; return i.
// ---------------------------------------------------------------------------

// Note: the vendor *= family computes i = (*this)*i, but no
// global::operator*(T) is defined anywhere in the vendor sources
// (an undefined reference in the vendor itself); the product is
// rendered directly, value * i.
operator fun Global.plusAssign(i: Int) { assignInt(this + i) }
operator fun Global.minusAssign(i: Int) { assignInt(this - i) }
operator fun Global.timesAssign(i: Int) { assignInt(extractInt() * i) }
operator fun Global.divAssign(i: Int) { assignInt(extractInt() / i) }

operator fun Global.plusAssign(i: UInt) { assignUInt(this + i) }
operator fun Global.minusAssign(i: UInt) { assignUInt(this - i) }
operator fun Global.timesAssign(i: UInt) { assignUInt(extractUInt() * i) }
operator fun Global.divAssign(i: UInt) { assignUInt(extractUInt() / i) }

operator fun Global.plusAssign(i: Short) { assignShort((this + i).toShort()) }
operator fun Global.minusAssign(i: Short) { assignShort((this - i).toShort()) }
operator fun Global.timesAssign(i: Short) { assignShort((extractShort() * i).toShort()) }
operator fun Global.divAssign(i: Short) { assignShort((extractShort() / i).toShort()) }

operator fun Global.plusAssign(i: UShort) { assignUShort(this + i) }
operator fun Global.minusAssign(i: UShort) { assignUShort(this - i) }
operator fun Global.timesAssign(i: UShort) { assignUShort((extractUShort() * i).toUShort()) }
operator fun Global.divAssign(i: UShort) { assignUShort((extractUShort() / i).toUShort()) }

operator fun Global.plusAssign(i: Long) { assignLong(this + i) }
operator fun Global.minusAssign(i: Long) { assignLong(this - i) }
operator fun Global.timesAssign(i: Long) { assignLong(extractLong() * i) }
operator fun Global.divAssign(i: Long) { assignLong(extractLong() / i) }

operator fun Global.plusAssign(i: ULong) { assignULong(this + i) }
operator fun Global.minusAssign(i: ULong) { assignULong(this - i) }
operator fun Global.timesAssign(i: ULong) { assignULong(extractULong() * i) }
operator fun Global.divAssign(i: ULong) { assignULong(extractULong() / i) }

operator fun Global.plusAssign(i: Float) { assignFloat(this + i) }
operator fun Global.minusAssign(i: Float) { assignFloat(this - i) }
operator fun Global.timesAssign(i: Float) { assignFloat(extractFloat() * i) }
operator fun Global.divAssign(i: Float) { assignFloat(extractFloat() / i) }

operator fun Global.plusAssign(i: Double) { assignDouble(this + i) }
operator fun Global.minusAssign(i: Double) { assignDouble(this - i) }
operator fun Global.timesAssign(i: Double) { assignDouble(extractDouble() * i) }
operator fun Global.divAssign(i: Double) { assignDouble(extractDouble() / i) }

// ---------------------------------------------------------------------------
// Numeric casts, member order: vendor global::operator+(T s) -> s+i,
// operator-(T s) -> s-i, operator/(T s) -> i/s.
// ---------------------------------------------------------------------------

operator fun Global.plus(s: Int): Int = s + extractInt()
operator fun Global.minus(s: Int): Int = s - extractInt()
operator fun Global.div(s: Int): Int = extractInt() / s

operator fun Global.plus(s: UInt): UInt = s + extractUInt()
operator fun Global.minus(s: UInt): UInt = s - extractUInt()
operator fun Global.div(s: UInt): UInt = extractUInt() / s

operator fun Global.plus(s: Short): Short = (s + extractShort()).toShort()
operator fun Global.minus(s: Short): Short = (s - extractShort()).toShort()
operator fun Global.div(s: Short): Short = (extractShort() / s).toShort()

operator fun Global.plus(s: UShort): UShort = (s + extractUShort()).toUShort()
operator fun Global.minus(s: UShort): UShort = (s - extractUShort()).toUShort()
operator fun Global.div(s: UShort): UShort = (extractUShort() / s).toUShort()

operator fun Global.plus(s: Long): Long = s + extractLong()
operator fun Global.minus(s: Long): Long = s - extractLong()
operator fun Global.div(s: Long): Long = extractLong() / s

operator fun Global.plus(s: ULong): ULong = s + extractULong()
operator fun Global.minus(s: ULong): ULong = s - extractULong()
operator fun Global.div(s: ULong): ULong = extractULong() / s

operator fun Global.plus(s: Float): Float = s + extractFloat()
operator fun Global.minus(s: Float): Float = s - extractFloat()
operator fun Global.div(s: Float): Float = extractFloat() / s

operator fun Global.plus(s: Double): Double = s + extractDouble()
operator fun Global.minus(s: Double): Double = s - extractDouble()
operator fun Global.div(s: Double): Double = extractDouble() / s

// ---------------------------------------------------------------------------
// Numeric casts, friend order: vendor operator+(T s, global a) -> i+s,
// operator-(T s, global a) -> s-i, operator/(T s, global a) -> s/i.
// ---------------------------------------------------------------------------

operator fun Int.plus(a: Global): Int = a.extractInt() + this
operator fun Int.minus(a: Global): Int = this - a.extractInt()
operator fun Int.div(a: Global): Int = this / a.extractInt()

operator fun UInt.plus(a: Global): UInt = a.extractUInt() + this
operator fun UInt.minus(a: Global): UInt = this - a.extractUInt()
operator fun UInt.div(a: Global): UInt = this / a.extractUInt()

operator fun Short.plus(a: Global): Short = (a.extractShort() + this).toShort()
operator fun Short.minus(a: Global): Short = (this - a.extractShort()).toShort()
operator fun Short.div(a: Global): Short = (this / a.extractShort()).toShort()

operator fun UShort.plus(a: Global): UShort = (a.extractUShort() + this).toUShort()
operator fun UShort.minus(a: Global): UShort = (this - a.extractUShort()).toUShort()
operator fun UShort.div(a: Global): UShort = (this / a.extractUShort()).toUShort()

operator fun Long.plus(a: Global): Long = a.extractLong() + this
operator fun Long.minus(a: Global): Long = this - a.extractLong()
operator fun Long.div(a: Global): Long = this / a.extractLong()

operator fun ULong.plus(a: Global): ULong = a.extractULong() + this
operator fun ULong.minus(a: Global): ULong = this - a.extractULong()
operator fun ULong.div(a: Global): ULong = this / a.extractULong()

operator fun Float.plus(a: Global): Float = a.extractFloat() + this
operator fun Float.minus(a: Global): Float = this - a.extractFloat()
operator fun Float.div(a: Global): Float = this / a.extractFloat()

operator fun Double.plus(a: Global): Double = a.extractDouble() + this
operator fun Double.minus(a: Global): Double = this - a.extractDouble()
operator fun Double.div(a: Global): Double = this / a.extractDouble()

// ---------------------------------------------------------------------------
// global vs global: vendor extracts both string forms, strtod's them,
// and returns this op s (a = this-value, b = s-value).
// ---------------------------------------------------------------------------

operator fun Global.plus(s: Global): Double {
    val tmp1 = s.extractCstring()
    val b = strtodC(tmp1)
    val tmp2 = this.extractCstring()
    var a = strtodC(tmp2)
    a = a + b
    return a
}

operator fun Global.minus(s: Global): Double {
    val tmp1 = s.extractCstring()
    val b = strtodC(tmp1)
    val tmp2 = this.extractCstring()
    var a = strtodC(tmp2)
    a = a - b
    return a
}

operator fun Global.div(s: Global): Double {
    val tmp1 = s.extractCstring()
    val b = strtodC(tmp1)
    val tmp2 = this.extractCstring()
    var a = strtodC(tmp2)
    a = a / b
    return a
}

// ---------------------------------------------------------------------------
// ++ and -- (vendor returns the numeric value, so no Kotlin inc/dec
// convention is possible; the integral store keeps the (long) cast).
// ---------------------------------------------------------------------------

fun Global.incrPre(): Double {
    var a = extractDouble()
    a++
    if (floor(a) == a) insertLong(a.toLong()) else insertDouble(a)
    return a
}

fun Global.decrPre(): Double {
    var a = extractDouble()
    a--
    if (floor(a) == a) insertLong(a.toLong()) else insertDouble(a)
    return a
}

fun Global.incrPost(): Double {
    val a = extractDouble()
    val b = a + 1
    if (floor(b) == b) insertLong(b.toLong()) else insertDouble(b)
    return a
}

fun Global.decrPost(): Double {
    val a = extractDouble()
    val b = a - 1
    if (floor(b) == b) insertLong(b.toLong()) else insertDouble(b)
    return a
}

// ---------------------------------------------------------------------------
// >= family (vendor operator>=). ge: no Kotlin function-name form
// for the token; the global-global and char-pointer/string forms
// compare the STRING forms via strcmp, as there.
// ---------------------------------------------------------------------------

fun Global.ge(a: Global): Int {
    val tmp1 = this.extractCstring()
    val tmp2 = a.extractCstring()
    if (strcmpA(tmp1, tmp2) >= 0) return 1
    return 0
}

fun Global.ge(a: Int): Int { if (extractInt() >= a) return 1; return 0 }
fun Global.ge(a: UInt): Int { if (extractUInt() >= a) return 1; return 0 }
fun Global.ge(a: Short): Int { if (extractShort() >= a) return 1; return 0 }
fun Global.ge(a: UShort): Int { if (extractUShort() >= a) return 1; return 0 }
fun Global.ge(a: Long): Int { if (extractLong() >= a) return 1; return 0 }
fun Global.ge(a: ULong): Int { if (extractULong() >= a) return 1; return 0 }
fun Global.ge(a: Float): Int { if (extractFloat() >= a) return 1; return 0 }
fun Global.ge(a: Double): Int { if (extractDouble() >= a) return 1; return 0 }

fun Global.ge(a: CharArray): Int {
    val tmp1 = this.extractCstring()
    if (strcmpA(tmp1, a) >= 0) return 1
    return 0
}

fun Global.ge(a: String): Int {
    val tmp1 = this.extractCstring()
    if (strncmpC(tmp1, 0, a, a.length + 1) >= 0) return 1
    return 0
}

// ---- >= friends: vendor operator>=(T a, global b).

fun Int.ge(b: Global): Int { if (this >= b.extractInt()) return 1; return 0 }
fun UInt.ge(b: Global): Int { if (this >= b.extractUInt()) return 1; return 0 }
fun Short.ge(b: Global): Int { if (this >= b.extractShort()) return 1; return 0 }
fun UShort.ge(b: Global): Int { if (this >= b.extractUShort()) return 1; return 0 }
fun Long.ge(b: Global): Int { if (this >= b.extractLong()) return 1; return 0 }
fun ULong.ge(b: Global): Int { if (this >= b.extractULong()) return 1; return 0 }
fun Float.ge(b: Global): Int { if (this >= b.extractFloat()) return 1; return 0 }
fun Double.ge(b: Global): Int { if (this >= b.extractDouble()) return 1; return 0 }

fun CharArray.ge(b: Global): Int {
    val tmp1 = b.extractCstring()
    if (strcmpA(this, tmp1) >= 0) return 1
    return 0
}

fun String.ge(b: Global): Int {
    // vendor: strcmp(a.c_str(), tmp1) - String.compareTo matches the
    // strcmp byte order over the ASCII domain (adaptation note).
    val tmp1 = b.extractCstring()
    if (this.compareTo(cstringAt(tmp1)) >= 0) return 1
    return 0
}

// ---------------------------------------------------------------------------
// <= family (vendor operator<=). The chunk boundary cuts here: the
// friend operator<=(short, global) head is the chunk's last line and
// its body belongs to the misc-3 chunk (u31 boundary precedent), so
// the friend family renders only through unsigned int, as the chunk
// does.
// ---------------------------------------------------------------------------

fun Global.le(a: Global): Int {
    val tmp1 = this.extractCstring()
    val tmp2 = a.extractCstring()
    if (strcmpA(tmp1, tmp2) <= 0) return 1
    return 0
}

fun Global.le(a: Int): Int { if (extractInt() <= a) return 1; return 0 }
fun Global.le(a: UInt): Int { if (extractUInt() <= a) return 1; return 0 }
fun Global.le(a: Short): Int { if (extractShort() <= a) return 1; return 0 }
fun Global.le(a: UShort): Int { if (extractUShort() <= a) return 1; return 0 }
fun Global.le(a: Long): Int { if (extractLong() <= a) return 1; return 0 }
fun Global.le(a: ULong): Int { if (extractULong() <= a) return 1; return 0 }
fun Global.le(a: Float): Int { if (extractFloat() <= a) return 1; return 0 }
fun Global.le(a: Double): Int { if (extractDouble() <= a) return 1; return 0 }

fun Global.le(a: CharArray): Int {
    val tmp1 = this.extractCstring()
    if (strcmpA(tmp1, a) <= 0) return 1
    return 0
}

fun Global.le(a: String): Int {
    val tmp1 = this.extractCstring()
    if (strncmpC(tmp1, 0, a, a.length + 1) <= 0) return 1
    return 0
}

// ---- <= friends: only int and unsigned int fall inside the chunk.

fun Int.le(b: Global): Int { if (this <= b.extractInt()) return 1; return 0 }
fun UInt.le(b: Global): Int { if (this <= b.extractUInt()) return 1; return 0 }
