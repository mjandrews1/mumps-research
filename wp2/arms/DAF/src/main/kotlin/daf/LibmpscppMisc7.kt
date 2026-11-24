package daf

// Unit 245 (DAF B8 T3, complex): libmpscpp.cpp::<misc-7> (600
// LOC, score 288.4): the Concat(x7) member families (the
// operator& backend of both class carriers), the mstring
// intrinsic-command members (Soundex, the one-arg Translate,
// Reverse x2, Qsubscript x2, Qlength x2, Query x2), the global
// conversion-operator ladder (the *this >> typed extractions),
// the operator* scalar-star family, the gt/lt comparison ladders,
// and the operator<<(global) write-assign family up through
// operator<<(short).
// Consults (2026-11-24T21:24:30Z, nominal): rows 18/54 hit-adapt
// (the *this >> reads ride the node-value model of the unit-154
// LibmpscppMisc5 precedent - NOT the Mglobal RETRIEVE, which the
// Concat members call explicitly per vendor), row 56 hit-adapt
// (the numeric Concat conversions ride the Locale.US format
// carrier of the unit-214 mcvt family), H-014 hit-reuse (every
// seat named below), H-015 seat collapse (char*/string twins
// collapse onto the String seat per family), H-016 name-adapt
// (Kotlin has no operator&/operator*/operator<</conversion
// operators: amp relays, mul free families, shl family, the
// globalAs* family).
// Dafny: EXEMPT (wrapper/service cluster over retrieval, fcn/
// zfcn/eval and node seats; journal row).

// CHUNK BOUNDARY ORPHANS: the dump HEAD is the orphan tail of
// global::Concat(double b) (the signature line lives at the
// misc-6 chunk cut; ledgered there as the Concat batch-10 tag);
// this chunk owns the body text so Concat(double) renders WHOLE
// here (the B7 misc-6 head/tail ledger precedent). The dump TAIL
// closes at the global::operator<<(short) close brace; the
// unsigned short/long/unsigned long/double shl members that
// follow in the vendor file are the NEXT chunk's text - ledger
// only, not rendered here.

// VENDOR ASYMMETRY kept: the > ladder has NO mstring member while
// the < ladder does (member at :693 plus friend at :781); both
// ledgers below.

// SEAT FLIPS this unit owns (TAG CORRECTION each: the misc-6
// "batch-10 ledger" forward tags on the Concat seats were
// forward-tags - the Concat member owners ARE this row; the
// u119 GlobalQuery tag-correction precedent): concatDaf (the
// H-015 collapsed stub of the twelve overload seats) is DELETED
// and its LibmpscppMisc6 amp relays resolve to the member bodies
// below (the relays gained the sv carrier the collapsed stub
// could not see; the edits ride this unit).

private const val M7_CE_D: Char = '\u00CE' // vendor "\xce"
private const val M7_CF_D: Char = '\u00CF' // vendor "\xcf"
private const val M7_D0_D: Char = '\u00D0' // vendor "\xd0"

// shared RETRIEVE walk of the global::Concat/Reverse members (the
// globalZblanksD precedent of unit 214).
private fun concatRetrieveD(ref: String, sv: DafMsv): String {
    val gtmp = CharArray(STR_MAX_DAF)

    val f = MglobalDaf(RETRIEVE_D, ref.toCharArray(), gtmp, sv)
    if (f == 0) { // if (f==0) { GlobalExceptionCode=GLOBAL_NOT_FOUND; throw
        GlobalExceptionCodeD = GLOBAL_NOT_FOUND_D
        throw MumpsGlobalExceptionD("global.Concat: global not found")
    }
    return DafCRuntime.cstring(gtmp)
}

// ---------------- global::Concat(x6) -------------------------------

fun globalConcatDblD(ref: String, b: Double, sv: DafMsv): String {
    val g = concatRetrieveD(ref, sv) // char gtmp[STR_MAX]; Mglobal(RETRIEVE)
    return g + String.format(java.util.Locale.US, "%g", b)
    // rslt = b; strcat(gtmp,rslt.data.c_str()); rslt=gtmp;
}

fun globalConcatLngD(ref: String, b: Long, sv: DafMsv): String =
    concatRetrieveD(ref, sv) + String.format(java.util.Locale.US, "%ld", b)

fun globalConcatIntD(ref: String, b: Int, sv: DafMsv): String =
    concatRetrieveD(ref, sv) + String.format(java.util.Locale.US, "%d", b)

// global::Concat(char*)/:Concat(string): the H-015 String seat.
fun globalConcatStrD(ref: String, b: String, sv: DafMsv): String =
    concatRetrieveD(ref, sv) + b // strcat(gtmp,b); rslt=gtmp;

// global::Concat(mstring): the String-seat mstring twin (same
// H-015 collapse, the mstring.Count precedent).
fun globalConcatMSD(ref: String, b: String, sv: DafMsv): String =
    concatRetrieveD(ref, sv) + b

// ---------------- mstring::Concat(x7) ------------------------------

fun mstringConcatDblD(data: String, b: Double): String =
    data + String.format(java.util.Locale.US, "%g", b)
    // mstring rslt=b; rslt = data + rslt.data;

fun mstringConcatLngD(data: String, b: Long): String =
    data + String.format(java.util.Locale.US, "%ld", b)

fun mstringConcatIntD(data: String, b: Int): String =
    data + String.format(java.util.Locale.US, "%d", b)

// mstring::Concat(char*)/Concat(string): the String seat.
fun mstringConcatStrD(data: String, b: String): String = data + b

// mstring::Concat(mstring): the String-seat twin of the above.
fun mstringConcatMD(data: String, b: String): String = data + b

// mstring::Concat(global b): the RETRIEVE-on-the-ARG arm (the
// only mstring member that retrieves - b, not this).
fun mstringConcatNodeD(data: String, b: MpsGlobalNode, sv: DafMsv): String {
    val g = concatRetrieveD(b.ref, sv) // Mglobal(RETRIEVE, b.ref, gtmp, svPtr)
    return data + g // rslt=data+gtmp;
}

// ---------------- Soundex / Translate -----------------------------

// mstring mstring::Soundex() - the zfcn route (NOTE: the LEADING
// BLANK before the fcn name is the vendor's own - " \u0024zzsoundex";
// result rides the local tmp2 out-buffer, offset 0 of this body
// vs the v1d[1] carriage of the fcn family).
fun mstringSoundexD(data: String, sv: DafMsv): String {
    // strcpy(tmp," \u0024zzsoundex"); strcat "\xce"; data; "\xcf";
    val m = StringBuilder(" \u0024zzsoundex")
    m.append(M7_CE_D)
    m.append(data)
    m.append(M7_CF_D)

    val tmp = CharArray(STR_MAX_DAF)
    for (q in m.indices) tmp[q] = m[q]
    tmp[m.length] = NUL_D

    val tmp2 = CharArray(STR_MAX_DAF)
    zfcnDaf(tmp, tmp2, sv) // zfcn(tmp, tmp2, svPtr)

    return DafCRuntime.cstring(tmp2) // m = (char *) tmp2;
}

// mstring mstring::Translate(mstring a1) - the ONE-ARG member
// (the two-arg/argless kin are the landed unit-191
// MstringTranslate.kt / unit-131 GlobalTranslate renders). The
// envelope is the argless-plus-a1 form: \u0024translate, \xce,
// data, \xd0, a1, \xcf.
fun mstringTranslate1D(data: String, a1: String, sv: DafMsv): String {
    val m = StringBuilder("\u0024translate")
    m.append(M7_CE_D)
    m.append(data)
    m.append(M7_D0_D)
    m.append(a1)
    m.append(M7_CF_D)

    // strcpy((char *)&svPtr->v1d[1], m.data.c_str());
    var q = 0
    while (q < m.length && q + 1 < sv.v1d.size) {
        sv.v1d[q + 1] = m[q]; q++
    }
    sv.v1d[q + 1] = NUL_D

    dafFcn(sv) // fcn(svPtr)

    return DafCRuntime.cstringAt(sv.bd, 1) // m = (char *)&svPtr->bd[1];
}

// ---------------- Reverse / Qsubscript / Qlength / Query ----------

// shared eval relay (the SQL_Table precedent of unit 214).
private fun evalStrD(m: String, sv: DafMsv): String {
    val out = CharArray(STR_MAX_DAF)
    val c = CharArray(STR_MAX_DAF)
    for (q in m.indices) c[q] = m[q]
    c[m.length] = NUL_D
    evalDaf(c, out, sv) // m = m.Eval();
    return DafCRuntime.cstring(out)
}

// mstring global::Reverse() - the Mglobal RETRIEVE writes into
// gtmp AT OFFSET 9 (the "\u0024reverse(" prefix is 9 bytes; the
// vendor passes &gtmp[9] - a suffix-of-buffer retrieve that
// Kotlin arrays model as a fresh buffer then prefix
// reconstruction; H-010 buffer-fold note).
fun globalReverseD(n: MpsGlobalNode, sv: DafMsv): String {
    val r = concatRetrieveD(n.ref, sv) // Mglobal(RETRIEVE,...,&gtmp[9])
    return evalStrD("\u0024reverse(" + r + ")", sv) // m = m.Eval();
}

fun mstringReverseD(data: String, sv: DafMsv): String =
    evalStrD("\u0024reverse(" + data + ")", sv)
    // m = "\u0024reverse("+data+")"; m = m.Eval();

// mstring mstring::Qsubscript(int j)
fun mstringQsubscriptD(data: String, j: Int, sv: DafMsv): String {
    val i = String.format(java.util.Locale.US, "%d", j) // sprintf(i,"%d",j)
    // m = "\u0024qsubscript("+data+","+i+")";
    return evalStrD("\u0024qsubscript(" + data + "," + i + ")", sv)
}

// mstring global::Qsubscript(int j) - the Sref arm: DAF's
// MpsGlobalNode models ref in the Sref domain (the caret-free
// display carrier; the unit-211 MakeGlobal srefD ledger), so the
// vendor Sref reads fold onto n.ref here (row 6/22 hit-adapt).
fun globalQsubscriptD(n: MpsGlobalNode, j: Int, sv: DafMsv): String {
    val i = String.format(java.util.Locale.US, "%d", j)
    return evalStrD("\u0024qsubscript(" + n.ref + "," + i + ")", sv)
}

// (*this).FixGblRef() - RAISED here as the named-owner stub of the
// two FixGblRef arms below (owner libmpscpp.cpp::FixGblRef, not a
// corpus unit; the MstringClassHMisc unit-150 owner-named ledger).
fun fixGblRefD(ms: String): String =
    throw NotImplementedError(
        "FixGblRef: out-of-scope owner (libmpscpp.cpp::FixGblRef; not a corpus unit)")

// int mstring::Qlength() - the FixGblRef route (the m & chain
// rides the String + fold, H-010).
fun mstringQlengthFixD(data: String, sv: DafMsv): Int =
    evalDaf(toEnvC("\u0024qlength(" + fixGblRefD(data) + ")"),
        CharArray(STR_MAX_DAF), sv) // i = m.Eval();

// int global::Qlength() - the "^" + Sref arm (Sref folds onto
// n.ref, ledger above).
fun globalQlengthD(n: MpsGlobalNode, sv: DafMsv): Int =
    evalDaf(toEnvC("\u0024qlength(^" + n.ref + ")"),
        CharArray(STR_MAX_DAF), sv)

// mstring global::Query()
fun globalQueryD(n: MpsGlobalNode, sv: DafMsv): String =
    evalStrD("\u0024query(^" + n.ref + ")", sv)

// mstring mstring::Query() - the FixGblRef route again.
fun mstringQueryFixD(data: String, sv: DafMsv): String =
    evalStrD("\u0024query(" + fixGblRefD(data) + ")", sv)

private fun toEnvC(m: String): CharArray {
    val c = CharArray(STR_MAX_DAF)
    for (q in m.indices) c[q] = m[q]
    c[m.length] = NUL_D
    return c
}

// ---------------- global conversion operators (H-010 fold) ---------
// The *this >> typed extractions; the operator>> read family
// bodies are outside this dump - the row-18 fold of the unit-154
// precedent reads the node value; numeric conversions ride
// atolD/atofD (DOCUMENTED DIVERGENCE, ledger per seat there).

private fun nodeValue7D(n: MpsGlobalNode): String =
    n.value // *this >> tmp

// global::operator char*()
fun globalAsCharD(n: MpsGlobalNode): String = nodeValue7D(n)

fun globalAsIntD(n: MpsGlobalNode): Int = atolD(nodeValue7D(n)).toInt()
fun globalAsUIntD(n: MpsGlobalNode): UInt = atolD(nodeValue7D(n)).toUInt()
fun globalAsLongD(n: MpsGlobalNode): Long = atolD(nodeValue7D(n))
fun globalAsULongD(n: MpsGlobalNode): ULong = atolD(nodeValue7D(n)).toULong()
fun globalAsShortD(n: MpsGlobalNode): Short = atolD(nodeValue7D(n)).toShort()
fun globalAsUShortD(n: MpsGlobalNode): UShort = atolD(nodeValue7D(n)).toUShort()
fun globalAsFloatD(n: MpsGlobalNode): Float = atofD(nodeValue7D(n)).toFloat()
fun globalAsDoubleD(n: MpsGlobalNode): Double = atofD(nodeValue7D(n))

// global::operator mstring() - the mstring extraction arm folds
// onto the char* content seat (H-010 ledger).
fun globalAsMstringD(n: MpsGlobalNode): String = nodeValue7D(n)

// ---------------- operator* scalar-star family --------------------

fun globalMulIntD(n: MpsGlobalNode, s: Int): Int = s * globalAsIntD(n)
fun intMulGlobalD(s: Int, n: MpsGlobalNode): Int = s * globalAsIntD(n)

fun globalMulUIntD(n: MpsGlobalNode, s: UInt): UInt = s * globalAsUIntD(n)
fun uintMulGlobalD(s: UInt, n: MpsGlobalNode): UInt = s * globalAsUIntD(n)

// double global::operator*(global s) - the ONLY member-member
// star: both operands fetch and strtod (the s >> tmp1 arm first,
// then *this >> tmp2; a = a*b in that order, kept).
fun globalMulGlobalD(n: MpsGlobalNode, s: MpsGlobalNode): Double {
    val b = DafCRuntime.strtod(nodeValue7D(s)) // s >> tmp1; b=strtod(tmp1)
    val a = DafCRuntime.strtod(nodeValue7D(n)) // *this >> tmp2; a=strtod(tmp2)
    return a * b
}

fun globalMulFloatD(n: MpsGlobalNode, s: Float): Float = s * globalAsFloatD(n)
fun floatMulGlobalD(s: Float, n: MpsGlobalNode): Float = s * globalAsFloatD(n)

fun globalMulDblD(n: MpsGlobalNode, s: Double): Double = s * globalAsDoubleD(n)
fun dblMulGlobalD(s: Double, n: MpsGlobalNode): Double = s * globalAsDoubleD(n)

fun globalMulLongD(n: MpsGlobalNode, s: Long): Long = s * globalAsLongD(n)
fun longMulGlobalD(s: Long, n: MpsGlobalNode): Long = s * globalAsLongD(n)

// VENDOR QUIRK (ledger): the unsigned long bodies declare the
// local as plain long (long i;) inside both unsigned long stars.
fun globalMulULongD(n: MpsGlobalNode, s: ULong): Long {
    val i = globalAsLongD(n) // long i; *this >> i;
    return s.toLong() * i
}

fun ulongMulGlobalD(s: ULong, n: MpsGlobalNode): Long {
    val i = globalAsLongD(n) // long i; a >> i;
    return s.toLong() * i
}

fun globalMulShortD(n: MpsGlobalNode, s: Short): Short = (s * globalAsShortD(n)).toShort()
fun shortMulGlobalD(s: Short, n: MpsGlobalNode): Short = (s * globalAsShortD(n)).toShort()

// QUIRK twin arm: unsigned short bodies declare short locals.
fun globalMulUShortD(n: MpsGlobalNode, s: UShort): Short {
    val i = globalAsShortD(n)
    return (s.toShort() * i).toShort()
}

fun ushortMulGlobalD(s: UShort, n: MpsGlobalNode): Short {
    val i = globalAsShortD(n)
    return (s.toShort() * i).toShort()
}

// ---------------- operator> ladder --------------------------------

// strcmp string comparisons render as String.compareTo (ASCII
// byte domain per H-012).
fun globalGtNodeD(n: MpsGlobalNode, a: MpsGlobalNode): Boolean =
    nodeValue7D(n).compareTo(nodeValue7D(a)) > 0

fun globalGtIntD(n: MpsGlobalNode, a: Int): Boolean = globalAsIntD(n) > a
fun globalGtUIntD(n: MpsGlobalNode, a: UInt): Boolean = globalAsUIntD(n) > a
fun globalGtShortD(n: MpsGlobalNode, a: Short): Boolean = globalAsShortD(n) > a
fun globalGtUShortD(n: MpsGlobalNode, a: UShort): Boolean = globalAsUShortD(n) > a
fun globalGtLongD(n: MpsGlobalNode, a: Long): Boolean = globalAsLongD(n) > a
fun globalGtULongD(n: MpsGlobalNode, a: ULong): Boolean = globalAsLongD(n) > a.toLong()
fun globalGtFloatD(n: MpsGlobalNode, a: Float): Boolean = globalAsFloatD(n) > a
fun globalGtDoubleD(n: MpsGlobalNode, a: Double): Boolean = globalAsDoubleD(n) > a

// global::operator>(char*)/(string): the H-015 String seat.
fun globalGtStrD(n: MpsGlobalNode, a: String): Boolean =
    nodeValue7D(n).compareTo(a) > 0

// friend forms (s > global): the scalar reads the node, the
// comparison order flips per vendor (a > i).
fun intGtGlobalD(s: Int, n: MpsGlobalNode): Boolean = s > globalAsIntD(n)
fun uintGtGlobalD(s: UInt, n: MpsGlobalNode): Boolean = s > globalAsUIntD(n)
fun shortGtGlobalD(s: Short, n: MpsGlobalNode): Boolean = s > globalAsShortD(n)
fun ushortGtGlobalD(s: UShort, n: MpsGlobalNode): Boolean = s > globalAsUShortD(n)
fun longGtGlobalD(s: Long, n: MpsGlobalNode): Boolean = s > globalAsLongD(n)
fun ulongGtGlobalD(s: ULong, n: MpsGlobalNode): Boolean = s.toLong() > globalAsLongD(n)
fun floatGtGlobalD(s: Float, n: MpsGlobalNode): Boolean = s > globalAsFloatD(n)
fun dblGtGlobalD(s: Double, n: MpsGlobalNode): Boolean = s > globalAsDoubleD(n)
fun strGtGlobalD(s: String, n: MpsGlobalNode): Boolean = s.compareTo(nodeValue7D(n)) > 0

// ---------------- operator< ladder --------------------------------
// The mstring member :693 and friend :781 arms (the vendor >/<
// asymmetry, ledger above).

fun globalLtNodeD(n: MpsGlobalNode, a: MpsGlobalNode): Boolean =
    nodeValue7D(n).compareTo(nodeValue7D(a)) < 0

fun globalLtIntD(n: MpsGlobalNode, a: Int): Boolean = globalAsIntD(n) < a
fun globalLtUIntD(n: MpsGlobalNode, a: UInt): Boolean = globalAsUIntD(n) < a
fun globalLtShortD(n: MpsGlobalNode, a: Short): Boolean = globalAsShortD(n) < a
fun globalLtUShortD(n: MpsGlobalNode, a: UShort): Boolean = globalAsUShortD(n) < a
fun globalLtLongD(n: MpsGlobalNode, a: Long): Boolean = globalAsLongD(n) < a
fun globalLtULongD(n: MpsGlobalNode, a: ULong): Boolean = globalAsLongD(n) < a.toLong()
fun globalLtFloatD(n: MpsGlobalNode, a: Float): Boolean = globalAsFloatD(n) < a
fun globalLtDoubleD(n: MpsGlobalNode, a: Double): Boolean = globalAsDoubleD(n) < a

// global::operator<(char*)/(string)/(mstring): one String seat
// (the H-015 collapse covers the mstring operand - a.data).
fun globalLtStrD(n: MpsGlobalNode, a: String): Boolean =
    nodeValue7D(n).compareTo(a) < 0

// friend forms (s < global), int/unsigned int/short/unsigned
// short/long/unsigned long/float/double/char*/string:
fun intLtGlobalD(s: Int, n: MpsGlobalNode): Boolean = s < globalAsIntD(n)
fun uintLtGlobalD(s: UInt, n: MpsGlobalNode): Boolean = s < globalAsUIntD(n)
fun shortLtGlobalD(s: Short, n: MpsGlobalNode): Boolean = s < globalAsShortD(n)
fun ushortLtGlobalD(s: UShort, n: MpsGlobalNode): Boolean = s < globalAsUShortD(n)
fun longLtGlobalD(s: Long, n: MpsGlobalNode): Boolean = s < globalAsLongD(n)
fun ulongLtGlobalD(s: ULong, n: MpsGlobalNode): Boolean = s.toLong() < globalAsLongD(n)
fun floatLtGlobalD(s: Float, n: MpsGlobalNode): Boolean = s < globalAsFloatD(n)
fun dblLtGlobalD(s: Double, n: MpsGlobalNode): Boolean = s < globalAsDoubleD(n)
fun strLtGlobalD(s: String, n: MpsGlobalNode): Boolean = s.compareTo(nodeValue7D(n)) < 0

// ---------------- operator<< write-assign family -----------------

// global & global::operator<<(char * s) - set_global(relay).
fun globalShlStrD(n: MpsGlobalNode, s: String, sv: DafMsv): MpsGlobalNode {
    setGlobalDaf(n.ref.toCharArray(), toEnvC(s)) // set_global(ref,s);
    return n
}

// global & global::operator<<(global g) - the get_global
// retrieve-then-throw arm (the unit-214 operator<< ledger): the
// source g.get() is the string-form getter (the Misc6 globalGetD
// seat returns the ref carrier of the node model, row 54), and
// the svPtr->tpx check rides the get_global seat semantics.
fun globalShlNodeD(n: MpsGlobalNode, g: MpsGlobalNode, sv: DafMsv): MpsGlobalNode {
    val s = CharArray(STR_MAX_DAF)
    getGlobalDaf(globalGetD(g).toCharArray(), s) // get_global(g.get(),s);

    if (sv.tpx == 0) { // if (!svPtr->tpx) { raise }
        GlobalExceptionCodeD = GLOBAL_NOT_FOUND_D
        throw MumpsGlobalExceptionD("global.Shl: global not found")
    }

    setGlobalDaf(n.ref.toCharArray(), s) // set_global(ref,s);
    return n
}

fun globalShlIntD(n: MpsGlobalNode, i: Int, sv: DafMsv): MpsGlobalNode {
    val s = String.format(java.util.Locale.US, "%d", i) // sprintf(s,"%d",i)
    setGlobalDaf(n.ref.toCharArray(), toEnvC(s))
    return n
}

// global & global::operator<<(unsigned int i) - VENDOR QUIRK: the
// vendor sprintf format is "%ud" (a %u-then-D literal, not a %u
// conversion; glibc prints %u then a D). The JVM Formatter has no
// %u, so the arm folds to "%d" (DOCUMENTED DIVERGENCE, ledger).
fun globalShlUIntD(n: MpsGlobalNode, i: UInt, sv: DafMsv): MpsGlobalNode {
    val s = String.format(java.util.Locale.US, "%d", i.toLong())
    setGlobalDaf(n.ref.toCharArray(), toEnvC(s))
    return n
}

// global & global::operator<<(short i) - the dump closes at this
// body's close brace (the unsigned short/long/unsigned long/
// double shl twins are the next chunk's text; head/tail ledger
// above).
fun globalShlShortD(n: MpsGlobalNode, i: Short, sv: DafMsv): MpsGlobalNode {
    val s = String.format(java.util.Locale.US, "%d", i.toInt()) // "%hd"
    setGlobalDaf(n.ref.toCharArray(), toEnvC(s))
    return n
}
