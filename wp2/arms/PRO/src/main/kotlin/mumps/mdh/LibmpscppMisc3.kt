package mumps.mdh

import java.util.Locale
import mumps.interp.atoiC
import mumps.interp.cstringAt
import mumps.interp.gcvtWrapper
import mumps.interp.strcpyC
import mumps.interp.writeCstring

/**
 * PRO-arm translation of libmpscpp.cpp::<misc-3> (batch 9 unit,
 * roster row 249, file rank 300). Translated independently from
 * the pristine vendor source (mumps-14-may-2026.tgz, sha256
 * 621deb25...ffdbe); the third 600-physLOC residual chunk of
 * libmpscpp.cpp (identity re-verified via the study tool: loc 600,
 * comment_ratio 0.061, pointer_marks 165, control_marks 54, score
 * 472.2 - exact match to wp1/study-subset/units.csv row 249).
 * Chunk coverage: vendor lines 6156-8758 (opens INSIDE the friend
 * operator<=(short,global) body whose head closed the misc-2
 * chunk, u62 boundary precedent; closes at the mstring
 * operator=(mstring) tail) - the friend <= tail family, the
 * global == and != families (member and friend), the shift-in /
 * shift-out (insertion << and extraction >>) operator families of
 * the interpreter class global, the global operator= family, the
 * mstring operator[] and compound-assignment (+= -= *= /= %=)
 * families, and the mstring operator= family, interleaved with
 * the chunk's comment fossils and the sw/Syn/StopList
 * carrier-once ledger sections (7718-8039).
 *
 * STUB RESOLUTIONS this unit carries (MdhStubs, u62-raised):
 * the nine extractT stubs + insertLong/insertDouble (owners the
 * shift families below, TAG CORRECTION: the stubs' "not-yet-
 * translated misc-3" tags close here), the eight assignT stubs
 * (TAG CORRECTION: the definitions are libmpscpp.cpp:6885-6961
 * corpus lines of THIS chunk, not libmpscpp.h machinery - the
 * GlobalOps call sites bind unchanged), and Global.readInto
 * (owner global::operator>>(char*), vendor 6699; the Misc9-era
 * seat note superseded, u141 tag-correction family).
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h008 static_scratch   : the extraction/insertion families
 *    use per-call CharArray(STR_MAX) locals (u159 fold notes);
 *    no file-scope scratch is added in Kotlin.
 *  - h010 numeric_promotion: unsigned C types map to UInt/UShort/
 *    ULong so every vendor overload keeps a distinct signature;
 *    the extract-side %d-for-unsigned-int scans parse an Int and
 *    .toUInt() the C wraparound (vendor uses %d there; kept).
 *  - h011 control_flow     : the one-line if/return bodies render
 *    as single-expression ifs; the >> GLOBAL_NOT_FOUND/
 *    INVALID_CONVERSION ladders keep their vendor order.
 *  - h012 unsigned_byte_domain: every emitted/stored text rides
 *    CharArray byte views; cstring views only at comparisons.
 *  - h013 dead_store       : none in the chunk bodies.
 *  - h014 out_of_scope_call: the mstring compound assignments
 *    delegate to the operator+/-///% seats that live in the
 *    misc-4 residual chunk (roster row 283, batch 10) - rendered
 *    as per-file private TODO stubs so the delegation shape is
 *    kept and the owners land unchanged next batch; set_global/
 *    get_global are the DONE u218 macro seats; gcvtWrapper is
 *    the DONE interp seat.
 *  - h015 grammar_collision: no Kotlin function-name form exists
 *    for == / != returning Int - named eq/ne (ge/le precedent,
 *    unit 62); the insertion family names insertT and the
 *    mstring operator= names setFrom, both h017 adaptations.
 *  - h017 naming          : operator conventions adopted where a
 *    token exists (Mstring plusAssign/minusAssign/timesAssign/
 *    divAssign/remAssign, Mstring get for operator[]); the 38 raised
 *    mPlus/mMinus/mTimes/mDiv/mRem stubs are RESOLVED (u283: the
 *    LibmpscppMisc4 plus/minus/div/rem families + LibmpscppMisc8
 *    times family).
 *  - h018 preprocessor_conditional: the #undef kill (7990) and
 *    the sw-section defines (MAX_ALIGNS/EMPTY_CELL, 8028-8039)
 *    are fossil text whose carriers were front-run by unit 45
 *    (MdhStubs) and the SynInit/StopInit/Sw seats - verification
 *    rows only, no redeclaration.
 *
 * Vendor notes (translated faithfully, not \"fixed\"):
 *  - The friend operator<=(string,global) body tests strcmp >= 0
 *    (a >= used in a <= family - vendor fossil), and the friend
 *    operator==(string,global) and operator!=(string,global)
 *    bodies BOTH test strcmp >= 0 (the != uses >=, not !=) - kept
 *    verbatim, u62 fossil family.
 *  - The operator=(unsigned int/short/long-family) printf texts
 *    \"%%ud\", \"%%uhd\", \"%%lud\" are parsed by C as %u + literal
 *    'd'/'hd'/'lud tails - the stored strings carry the literal
 *    tails (\"5d\", \"5hd\", \"5d\") exactly as the vendor emits
 *    them; so does the insertion family's << (%ud). The %hd/%hu/
 *    %ld/%lu forms are legal C and keep only the number.
 *  - The << ushort insertion emits %hu and the ushort operator=
 *    emits %uhd (the buggy family member above).
 *  - mstring::operator[] returns *(this+i) - pointer arithmetic
 *    off a single object, vendor UB; quantized to the receiver
 *    (offset 0) so the atoi conversions are kept and the
 *    dereference shape documented (pt i=0 precedent, unit 124).
 *  - mstring::operator=(long) prints with %d, truncating a wide
 *    long on the vendor's LP64 build - kept as the same text via
 *    Int.toString(); operator=(double/float) use %g.
 *  - The vendor sscanf %d/%ld/%lu/%hd/%hu parsers accept a
 *    leading numeric prefix; the renders use toIntOrNull-style
 *    conversion with a leading-sign/digits prefix trim, and
 *    throw INVALID_CONVERSION where sscanf returns 0 (u262
 *    stricter-scan precedent, noted per form).
 *  - plain sections of the chunk (\"shift in\" at 6611, \"shift
 *    out\" at 6697, the MSTRING/GLOBAL PATTERN and piece/justify
 *    comment banners 7093-7370, the \"????????\" block 7155, the
 *    mstring constructors banner 8417-8419) are comment fossils;
 *    StopList stop; (7718), Syn syn[SYNMAX]/synx (7785-7786) and
 *    extern \"C\" cmpr (7773) are DONE carriers (StopInit/SynInit/
 *    SynCompare seats) - verification only.
 */

private const val STR_MAX = 32000 // vendor strmax.h:3 (unit-44 precedent)
private const val GLOBAL_NOT_FOUND = 2 // vendor libmpscpp.h:71
private const val INVALID_CONVERSION = 1 // vendor libmpscpp.h:70 (u262 precedent)

private fun toCbuf(s: String): CharArray {
    val b = CharArray(s.length + 1)
    writeCstring(b, s)
    return b
}

/** private strcmp fold over the ASCII byte domain (GlobalOps strcmpA precedent). */
private fun strcmpA(a: CharArray, b: CharArray): Int {
    val sa = cstringAt(a)
    val sb = cstringAt(b)
    if (sa == sb) return 0
    return if (sa < sb) -1 else 1
}

/**
 * Leading-sign/digits prefix trim, then conversion - the C sscanf
 * %d/%ld/%hd family accepts a numeric prefix and skips trailing
 * garbage; returns null where sscanf would return 0 (u262 note).
 */
private fun scanIntPrefix(s: String): Long? {
    var i = 0
    while (i < s.length && s[i].code <= 32) i++ // leading blanks
    var j = i
    if (j < s.length && (s[j] == '+' || s[j] == '-')) j++
    var k = j
    while (k < s.length && s[k] >= '0' && s[k] <= '9') k++
    if (k == j) return null // no digits: sscanf returns 0
    return s.substring(i, k).toLongOrNull()
}

/** Shared core get_global + GLOBAL_NOT_FOUND gate (vendor every >> body). */
private fun Global.retrieveOrFail(): CharArray {
    val s = CharArray(STR_MAX)
    get_global(toCbuf(ref), s)
    if (MdhState.svPtr.tpx == 0L) {
        MdhState.globalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }
    return s
}

/** Shared INVALID_CONVERSION throw where sscanf returns 0. */
private fun failConversion(): Nothing {
    MdhState.globalExceptionCode = INVALID_CONVERSION
    throw MumpsGlobalException()
}

// ---------------------------------------------------------------------------
// shift in: vendor global::operator<< inserts (6613-6682; the %ud
// fossil noted above; the << global form re-stores via get_global).
// ---------------------------------------------------------------------------

/** vendor 6613: global & global::operator<<(char *). */
fun Global.insertCstring(s: CharArray) {
    set_global(toCbuf(ref), s)
}

/** vendor 6618: global & global::operator<<(global g) - get + gate + set. */
fun Global.insertGlobal(g: Global) {
    val s = g.retrieveOrFail()
    set_global(toCbuf(ref), s)
}

/** vendor 6625: operator<<(int) - sprintf %d. */
fun Global.insertInt(i: Int) {
    set_global(toCbuf(ref), toCbuf(i.toString()))
}

/** vendor 6630: operator<<(unsigned int) - sprintf %ud (u-conversion + 'd' tail kept). */
fun Global.insertUInt(i: UInt) {
    set_global(toCbuf(ref), toCbuf(i.toString() + "d"))
}

/** vendor 6635: operator<<(short) - sprintf %hd. */
fun Global.insertShort(i: Short) {
    set_global(toCbuf(ref), toCbuf(i.toString()))
}

/** vendor 6640: operator<<(unsigned short) - sprintf %hu. */
fun Global.insertUShort(i: UShort) {
    set_global(toCbuf(ref), toCbuf(i.toString()))
}

/** vendor 6645: operator<<(long) - sprintf %ld. */
fun Global.insertLong(i: Long) {
    set_global(toCbuf(ref), toCbuf(i.toString()))
}

/** vendor 6650: operator<<(unsigned long) - sprintf %lu. */
fun Global.insertULong(i: ULong) {
    set_global(toCbuf(ref), toCbuf(i.toString()))
}

/** vendor 6655: operator<<(float) - sprintf %f. */
fun Global.insertFloat(i: Float) {
    val tmp = CharArray(32)
    String.format(Locale.getDefault(), "%f", i).let { writeCstring(tmp, it) } // h018 u182 locale carrier
    set_global(toCbuf(ref), tmp)
}

/** vendor 6660: operator<<(double) - sprintf %f. */
fun Global.insertDouble(i: Double) {
    val tmp = CharArray(32)
    String.format(Locale.getDefault(), "%f", i).let { writeCstring(tmp, it) } // h018 u182 locale carrier
    set_global(toCbuf(ref), tmp)
}

/** vendor 6665: operator<<(string). */
fun Global.insertString(s: String) {
    set_global(toCbuf(ref), toCbuf(s))
}

/** vendor 6670: operator<<(mstring). */
fun Global.insertMstring(s: Mstring) {
    set_global(toCbuf(ref), toCbuf(s.value))
}

// ---------------------------------------------------------------------------
// shift out: vendor global::operator>> extracts (6699-6879), each
// get_global + GLOBAL_NOT_FOUND gate, numeric forms sscanf-convert.
// ---------------------------------------------------------------------------

/** vendor 6699: char * global::operator>>(char * s). readInto resolved. */
fun Global.readInto(dst: CharArray): CharArray {
    val s = retrieveOrFail()
    strcpyC(dst, s)
    return dst
}

/** vendor 6708: global & global::operator>>(global g) - copy-through. */
fun Global.readInto(g: Global) {
    val s = retrieveOrFail()
    set_global(toCbuf(g.ref), s)
}

/** vendor 6719: int operator>>(int & i) - sscanf %d. */
fun Global.extractInt(): Int {
    val s = retrieveOrFail()
    val v = scanIntPrefix(cstringAt(s).trim()) // h012 String fold of the scan
    if (v == null) failConversion()
    return v?.toInt() ?: 0
}

/** vendor 6734: unsigned int operator>>(unsigned int & i) - sscanf %d (vendor uses %d; wrap kept). */
fun Global.extractUInt(): UInt {
    val s = retrieveOrFail()
    val v = scanIntPrefix(cstringAt(s).trim())
    if (v == null) failConversion()
    return v?.toInt()?.toUInt() ?: 0u
}

/** vendor 6749: long operator>>(long & i) - sscanf %ld. */
fun Global.extractLong(): Long {
    val s = retrieveOrFail()
    val v = scanIntPrefix(cstringAt(s).trim())
    if (v == null) failConversion()
    return v ?: 0L
}

/** vendor 6764: unsigned long operator>>(unsigned long & i) - sscanf %lu. */
fun Global.extractULong(): ULong {
    val s = retrieveOrFail()
    val v = scanIntPrefix(cstringAt(s).trim())
    if (v == null) failConversion()
    return v?.toULong() ?: 0u
}

/** vendor 6779: short operator>>(short & i) - sscanf %hd. */
fun Global.extractShort(): Short {
    val s = retrieveOrFail()
    val v = scanIntPrefix(cstringAt(s).trim())
    if (v == null) failConversion()
    return v?.toShort() ?: 0
}

/** vendor 6794: unsigned short operator>>(unsigned short & i) - sscanf %hu. */
fun Global.extractUShort(): UShort {
    val s = retrieveOrFail()
    val v = scanIntPrefix(cstringAt(s).trim())
    if (v == null) failConversion()
    return v?.toInt()?.toUShort() ?: 0u
}

/** vendor 6809: float operator>>(float & i) - sscanf %f (u262 precedent). */
fun Global.extractFloat(): Float {
    val s = retrieveOrFail()
    val v = cstringAt(s).trim().toFloatOrNull()
    if (v == null) failConversion()
    return v ?: 0.0f
}

/** vendor 6824: double operator>>(double & i) - sscanf %lf (u262 precedent). */
fun Global.extractDouble(): Double {
    val s = retrieveOrFail()
    val v = cstringAt(s).trim().toDoubleOrNull()
    if (v == null) failConversion()
    return v ?: 0.0
}

/** vendor 6839: string operator>>(string & i). */
fun Global.readInto(): String {
    val s = retrieveOrFail()
    return cstringAt(s)
}

/** vendor 6854: mstring operator>>(mstring & i). */
fun Global.readInto(i: Mstring) {
    val s = retrieveOrFail()
    i.value = cstringAt(s)
}

/** vendor 6719 char* form helper: extractCstring (the GlobalOps extraction
 *  call sites bind here; the readInto(dst) overload is the copy-out form). */
fun Global.extractCstring(): CharArray = retrieveOrFail()

// ---------------------------------------------------------------------------
// global operator= family (6885-6961): the assignT stubs resolve here
// (TAG CORRECTION: definitions are corpus lines of this chunk; the
// GlobalOps compound-assignment call sites bind unchanged).
// ---------------------------------------------------------------------------

/** vendor 6885: global & global::operator=(const char *). */
fun Global.assignCstring(s: CharArray) {
    set_global(toCbuf(ref), s)
}

/** vendor 6890: operator=(string). */
fun Global.assignString(s: String) {
    set_global(toCbuf(ref), toCbuf(s))
}

/** vendor 6895: operator=(mstring). */
fun Global.assignMstring(s: Mstring) {
    set_global(toCbuf(ref), toCbuf(s.value))
}

/** vendor 6900: operator=(int) - sprintf %d. */
fun Global.assignInt(s: Int) {
    val tmp = CharArray(128)
    writeCstring(tmp, s.toString())
    set_global(toCbuf(ref), tmp)
}

/** vendor 6907: operator=(double) - gcvt_wrapper(s,15,tmp) (kept, not %g). */
fun Global.assignDouble(s: Double) {
    val tmp = CharArray(128)
    gcvtWrapper(s, tmp)
    set_global(toCbuf(ref), tmp)
}

/** vendor 6914: operator=(global s). */
fun Global.assignGlobal(s: Global) {
    val tmp1 = s.retrieveOrFail()
    set_global(toCbuf(ref), tmp1)
}

/** vendor 6921: operator=(unsigned int) - sprintf %ud ('d' tail kept). */
fun Global.assignUInt(s: UInt) {
    val tmp = CharArray(128)
    writeCstring(tmp, s.toString() + "d")
    set_global(toCbuf(ref), tmp)
}

/** vendor 6928: operator=(float) - gcvt_wrapper(s,15,tmp). */
fun Global.assignFloat(s: Float) {
    val tmp = CharArray(128)
    gcvtWrapper(s.toDouble(), tmp)
    set_global(toCbuf(ref), tmp)
}

/** vendor 6935: operator=(short) - sprintf %hd. */
fun Global.assignShort(s: Short) {
    val tmp = CharArray(128)
    writeCstring(tmp, s.toString())
    set_global(toCbuf(ref), tmp)
}

/** vendor 6942: operator=(unsigned short) - sprintf %uhd ('hd' tail kept). */
fun Global.assignUShort(s: UShort) {
    val tmp = CharArray(128)
    writeCstring(tmp, s.toString() + "hd")
    set_global(toCbuf(ref), tmp)
}

/** vendor 6949: operator=(long) - sprintf %ld. */
fun Global.assignLong(s: Long) {
    val tmp = CharArray(128)
    writeCstring(tmp, s.toString())
    set_global(toCbuf(ref), tmp)
}

/** vendor 6956: operator=(unsigned long) - sprintf %lud ('d' tail kept). */
fun Global.assignULong(s: ULong) {
    val tmp = CharArray(128)
    writeCstring(tmp, s.toString() + "d")
    set_global(toCbuf(ref), tmp)
}

// ---------------------------------------------------------------------------
// == family (6227-6417): member and friend; named eq (h015).
// ---------------------------------------------------------------------------

fun Global.eq(a: Global): Int {
    val tmp1 = this.extractCstring()
    val tmp2 = a.extractCstring()
    if (strcmpA(tmp1, tmp2) == 0) return 1
    return 0
}

fun Global.eq(a: Int): Int { if (extractInt() == a) return 1; return 0 }
fun Global.eq(a: UInt): Int { if (extractUInt() == a) return 1; return 0 }
fun Global.eq(a: Short): Int { if (extractShort() == a) return 1; return 0 }
fun Global.eq(a: UShort): Int { if (extractUShort() == a) return 1; return 0 }
fun Global.eq(a: Long): Int { if (extractLong() == a) return 1; return 0 }
fun Global.eq(a: ULong): Int { if (extractULong() == a) return 1; return 0 }
fun Global.eq(a: Float): Int { if (extractFloat() == a) return 1; return 0 }
fun Global.eq(a: Double): Int { if (extractDouble() == a) return 1; return 0 }

fun Global.eq(a: CharArray): Int {
    val tmp1 = this.extractCstring()
    if (strcmpA(tmp1, a) == 0) return 1
    return 0
}

/** vendor 6318: strcmp(tmp1,a.c_str())==0 - String.compareTo fold, u62 note. */
fun Global.eq(a: String): Int {
    val tmp1 = this.extractCstring()
    if (cstringAt(tmp1) == a) return 1
    return 0
}

// ---- == friends (vendor 6329-6417)

fun Int.eq(b: Global): Int { if (this == b.extractInt()) return 1; return 0 }
fun UInt.eq(b: Global): Int { if (this == b.extractUInt()) return 1; return 0 }
fun Short.eq(b: Global): Int { if (this == b.extractShort()) return 1; return 0 }
fun UShort.eq(b: Global): Int { if (this == b.extractUShort()) return 1; return 0 }
fun Long.eq(b: Global): Int { if (this == b.extractLong()) return 1; return 0 }
fun ULong.eq(b: Global): Int { if (this == b.extractULong()) return 1; return 0 }
fun Float.eq(b: Global): Int { if (this == b.extractFloat()) return 1; return 0 }
fun Double.eq(b: Global): Int { if (this == b.extractDouble()) return 1; return 0 }

fun CharArray.eq(b: Global): Int {
    val tmp1 = b.extractCstring()
    if (strcmpA(this, tmp1) == 0) return 1
    return 0
}

/** vendor 6410-6417: the body tests strcmp >= 0 (fossil, kept verbatim). */
fun String.eq(b: Global): Int {
    val tmp1 = b.extractCstring()
    if (this.compareTo(cstringAt(tmp1)) >= 0) return 1
    return 0
}

// ---------------------------------------------------------------------------
// != family (6419-6609): member and friend; named ne (h015).
// ---------------------------------------------------------------------------

fun Global.ne(a: Global): Int {
    val tmp1 = this.extractCstring()
    val tmp2 = a.extractCstring()
    if (strcmpA(tmp1, tmp2) != 0) return 1
    return 0
}

fun Global.ne(a: Int): Int { if (extractInt() != a) return 1; return 0 }
fun Global.ne(a: UInt): Int { if (extractUInt() != a) return 1; return 0 }
fun Global.ne(a: Short): Int { if (extractShort() != a) return 1; return 0 }
fun Global.ne(a: UShort): Int { if (extractUShort() != a) return 1; return 0 }
fun Global.ne(a: Long): Int { if (extractLong() != a) return 1; return 0 }
fun Global.ne(a: ULong): Int { if (extractULong() != a) return 1; return 0 }
fun Global.ne(a: Float): Int { if (extractFloat() != a) return 1; return 0 }
fun Global.ne(a: Double): Int { if (extractDouble() != a) return 1; return 0 }

fun Global.ne(a: CharArray): Int {
    val tmp1 = this.extractCstring()
    if (strcmpA(tmp1, a) != 0) return 1
    return 0
}

/** vendor 6510: strcmp(tmp1,a.c_str())!=0. */
fun Global.ne(a: String): Int {
    val tmp1 = this.extractCstring()
    if (cstringAt(tmp1) != a) return 1
    return 0
}

// ---- != friends (vendor 6521-6609)

fun Int.ne(b: Global): Int { if (this != b.extractInt()) return 1; return 0 }
fun UInt.ne(b: Global): Int { if (this != b.extractUInt()) return 1; return 0 }
fun Short.ne(b: Global): Int { if (this != b.extractShort()) return 1; return 0 }
fun UShort.ne(b: Global): Int { if (this != b.extractUShort()) return 1; return 0 }
fun Long.ne(b: Global): Int { if (this != b.extractLong()) return 1; return 0 }
fun ULong.ne(b: Global): Int { if (this != b.extractULong()) return 1; return 0 }
fun Float.ne(b: Global): Int { if (this != b.extractFloat()) return 1; return 0 }
fun Double.ne(b: Global): Int { if (this != b.extractDouble()) return 1; return 0 }

fun CharArray.ne(b: Global): Int {
    val tmp1 = b.extractCstring()
    if (strcmpA(this, tmp1) != 0) return 1
    return 0
}

/** vendor 6602-6609: the body tests strcmp >= 0 (fossil, kept verbatim). */
fun String.ne(b: Global): Int {
    val tmp1 = b.extractCstring()
    if (this.compareTo(cstringAt(tmp1)) >= 0) return 1
    return 0
}

// ---------------------------------------------------------------------------
// friend <= tail family (6156-6225) - the u62 chunk-boundary bodies:
// named le, receiver-side (GlobalOps friend precedent).
// ---------------------------------------------------------------------------

fun Short.le(b: Global): Int { if (this <= b.extractShort()) return 1; return 0 }
fun UShort.le(b: Global): Int { if (this <= b.extractUShort()) return 1; return 0 }
fun Long.le(b: Global): Int { if (this <= b.extractLong()) return 1; return 0 }
fun ULong.le(b: Global): Int { if (this <= b.extractULong()) return 1; return 0 }
fun Float.le(b: Global): Int { if (this <= b.extractFloat()) return 1; return 0 }
fun Double.le(b: Global): Int { if (this <= b.extractDouble()) return 1; return 0 }

fun CharArray.le(b: Global): Int {
    val tmp1 = b.extractCstring()
    if (strcmpA(this, tmp1) <= 0) return 1
    return 0
}

/** vendor 6218-6225: the body tests strcmp >= 0 (fossil, kept verbatim). */
fun String.le(b: Global): Int {
    val tmp1 = b.extractCstring()
    if (this.compareTo(cstringAt(tmp1)) >= 0) return 1
    return 0
}

// ---------------------------------------------------------------------------
// mstring operator[] (8496-8516): vendor returns *(this+i) - UB
// quantized to the receiver (KDoc note); the atoi conversions kept.
// ---------------------------------------------------------------------------

/** vendor 8496: mstring & mstring::operator[](mstring x) { atoi(x); return *(this+i); } */
operator fun Mstring.get(x: Mstring): Mstring {
    val i = atoiC(toCbuf(x.value)) // atoi(x.c_str())
    return this // *(this+i): UB quantized to offset 0 (h013 note)
}

/** vendor 8502: mstring & mstring::operator[](int i). */
operator fun Mstring.get(i: Int): Mstring {
    return this // *(this+i): UB quantized (h013 note)
}

/** vendor 8506: mstring & mstring::operator[](const char * x). */
operator fun Mstring.get(x: CharArray): Mstring {
    val i = atoiC(x) // atoi(x)
    return this // *(this+i): UB quantized (h013 note)
}

/** vendor 8512: mstring & mstring::operator[](string x). */
operator fun Mstring.get(x: String): Mstring {
    val i = atoiC(toCbuf(x)) // atoi(x.c_str())
    return this // *(this+i): UB quantized (h013 note)
}

// ---------------------------------------------------------------------------
// mstring compound assignments (8518-8705): every body is
// (*this)=(*this) op i; return (*this). (u283 TAG CORRECTION: the 38
// per-file mPlus/mMinus/mTimes/mDiv/mRem TODO stubs raised here in
// batch-9 are RESOLVED - plus/minus/div/rem seats are the
// LibmpscppMisc4.kt row-283 operator families; the times seats are
// the LibmpscppMisc8.kt row-281 operator* family. The +=/*= calls
// below now bind those seats directly.)
// ---------------------------------------------------------------------------

// += family (8518-8555)

operator fun Mstring.plusAssign(i: Mstring) { this.setFrom(this + i) }
operator fun Mstring.plusAssign(i: Int) { this.setFrom(this + i) }
operator fun Mstring.plusAssign(i: Long) { this.setFrom(this + i) }
operator fun Mstring.plusAssign(i: Double) { this.setFrom(this + i) }
operator fun Mstring.plusAssign(i: Float) { this.setFrom(this + i) }
operator fun Mstring.plusAssign(i: String) { this.setFrom(this + i) }
operator fun Mstring.plusAssign(i: CharArray) { this.setFrom(this + i) }
operator fun Mstring.plusAssign(i: Global) { this.setFrom(this + i) }

// -= family (8557-8595)

operator fun Mstring.minusAssign(i: Mstring) { this.setFrom(this - i) }
operator fun Mstring.minusAssign(i: Int) { this.setFrom(this - i) }
operator fun Mstring.minusAssign(i: Long) { this.setFrom(this - i) }
operator fun Mstring.minusAssign(i: Double) { this.setFrom(this - i) }
operator fun Mstring.minusAssign(i: Float) { this.setFrom(this - i) }
operator fun Mstring.minusAssign(i: String) { this.setFrom(this - i) }
operator fun Mstring.minusAssign(i: CharArray) { this.setFrom(this - i) }
operator fun Mstring.minusAssign(i: Global) { this.setFrom(this - i) }

// *= family (8597-8635)

operator fun Mstring.timesAssign(i: Mstring) { this.setFrom(this * i) }
operator fun Mstring.timesAssign(i: Int) { this.setFrom(this * i) }
operator fun Mstring.timesAssign(i: Long) { this.setFrom(this * i) }
operator fun Mstring.timesAssign(i: Double) { this.setFrom(this * i) }
operator fun Mstring.timesAssign(i: Float) { this.setFrom(this * i) }
operator fun Mstring.timesAssign(i: String) { this.setFrom(this * i) }
operator fun Mstring.timesAssign(i: CharArray) { this.setFrom(this * i) }
operator fun Mstring.timesAssign(i: Global) { this.setFrom(this * i) }

// /= family (8637-8675)

operator fun Mstring.divAssign(i: Mstring) { this.setFrom(this / i) }
operator fun Mstring.divAssign(i: Int) { this.setFrom(this / i) }
operator fun Mstring.divAssign(i: Long) { this.setFrom(this / i) }
operator fun Mstring.divAssign(i: Double) { this.setFrom(this / i) }
operator fun Mstring.divAssign(i: Float) { this.setFrom(this / i) }
operator fun Mstring.divAssign(i: String) { this.setFrom(this / i) }
operator fun Mstring.divAssign(i: CharArray) { this.setFrom(this / i) }
operator fun Mstring.divAssign(i: Global) { this.setFrom(this / i) }

// %= family (8677-8705; no double/float forms in the vendor)

operator fun Mstring.remAssign(i: Mstring) { this.setFrom(this % i) }
operator fun Mstring.remAssign(i: Int) { this.setFrom(this % i) }
operator fun Mstring.remAssign(i: Long) { this.setFrom(this % i) }
operator fun Mstring.remAssign(i: String) { this.setFrom(this % i) }
operator fun Mstring.remAssign(i: CharArray) { this.setFrom(this % i) }
operator fun Mstring.remAssign(i: Global) { this.setFrom(this % i) }

// ---------------------------------------------------------------------------
// mstring operator= family (8717-8758): setFrom (h017 - Kotlin has no
// assignment-operator point); the %g and %d/%ld emissions kept.
// ---------------------------------------------------------------------------

/** vendor 8717: mstring & mstring::operator=(int) - sprintf %d. */
fun Mstring.setFrom(i: Int) {
    val tmp = "" + i // sprintf(tmp,"%d",i)
    value = tmp
}

/** vendor 8724: operator=(long) - sprintf %d (the vendor truncates a wide long; kept). */
fun Mstring.setFrom(i: Long) {
    val tmp = "" + i.toInt() // h010 narrowing, vendor %d
    value = tmp
}

/** vendor 8731: operator=(double) - sprintf %g. */
fun Mstring.setFrom(i: Double) {
    val tmp = String.format(Locale.getDefault(), "%g", i) // h018 u182 locale carrier
    value = tmp
}

/** vendor 8738: operator=(float) - sprintf %g. */
fun Mstring.setFrom(i: Float) {
    val tmp = String.format(Locale.getDefault(), "%g", i) // h018 u182 locale carrier
    value = tmp
}

/** vendor 8745: operator=(string). */
fun Mstring.setFrom(i: String) {
    value = i
}

/** vendor 8750: operator=(const char *). */
fun Mstring.setFrom(i: CharArray) {
    value = cstringAt(i)
}

/** vendor 8755: operator=(mstring). */
fun Mstring.setFrom(i: Mstring) {
    value = i.value
}
