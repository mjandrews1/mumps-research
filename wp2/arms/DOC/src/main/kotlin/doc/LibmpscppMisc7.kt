package doc

// Unit 238 (B8 tranche 3): libmpscpp.cpp::<misc-7> (batch 8, complex
// stratum; units.csv row 218: loc 600, score 288.4, rank 292). The
// Concat(x7) member families (the operator+ backend of both class
// carriers), the mstring intrinsic-command members (Soundex, the
// one-arg Translate, Reverse x2, Qsubscript x2, Qlength x2, Query
// x2), the global conversion-operator ladder, the operator*>
// scalar-star family, the gt/lt comparison ladders, and the
// operator<<(global) write-assign family. All derived from the
// pristine vendor source this session (sha256 621deb...ffdbe).
//
// CHUNK BOUNDARY ORPHANS (the misc-chunk template): the dump HEAD
// is the orphan TAIL of global::Concat(double b) - its signature
// line lives at the misc-6 dump tail cut (ledgered there as "the
// Concat(x7) member bodies are out of this chunk"); this chunk
// owns the body text, so Concat(double) is rendered WHOLE here
// (the unit-202 head/tail ledger precedent) with this cross-cut
// note. The dump TAIL closes at the global::operator<<(short)
// close brace; the vendor continuation (the unsigned short/long/
// unsigned long/float/double shl members that follow in the vendor
// file) is the NEXT chunk's text - ledger only, not rendered here.
//
// SEAT FLIPS this unit owns (TAG CORRECTION on each: the
// "batch-10 unit" owner tags raised by the unit-214 Stubs seats
// were forward-tags; the Concat member owners ARE this row - the
// u119 GlobalQuery tag-correction precedent): the twelve
// Stubs.Concat seats flip to the member renders below; the
// operator& amp extensions of LibmpscppMisc6 route through them
// unchanged. The global.global arm stays the landed unit-65
// render (GlobalRef.concat(GlobalRef), not regraded).
//
// FOLDS per the arm rules: the char*/std::string sqrt twins of
// every family collapse onto the String seat (H-015, the
// mstring.lt ladder precedent - a *_BT byte note per family lead);
// the > ladder has NO mstring member in the vendor while the <
// ladder does (vendor asymmetry kept, ledger below); the numeric
// Concat/ctor conversions ride the mcvt Locale.US carrier (H-009);
// i = m.Eval() int-conversions ride atollShared (H-010); strcmp
// string comparisons render as String.compareTo (ASCII byte
// domain per H-012); sprintf %d keeps String.format(Locale.US).
// The unused char xtmp[STR_MAX] of Concat(string) is a dead decl
// (H-013, ledger). The <<(global) body checks svPtr->tpx (the
// long tpx carrier) and the get_global/set_global rides their
// Stubs seats. New H-014 seat raised: Stubs.fixGblRef (owner
// libmpscpp.cpp::FixGblRef, declared in the unit-149 header
// ledger, no roster row). Dollars ride the DOLLAR_M7 const per
// H-015 (no raw dollar in source). Naming per H-017: member
// verbs lowercase (concat/soundex/translate/reverse/qsubscript/
// qlength/query), the C++ conversion operators as the to* family
// (H-010), operator* member forms as the Kotlin GlobalRef.times
// operator extensions and their friend forms as the globalTimes
// free family, operator<< as the GlobalRef.shl family (all H-016
// name-adaptations; the Kotlin infix conventions cannot reach the
// friend/prefix forms, so the free fns are plain, the globalPlus
// precedent), the friend orderings as the globalGt/globalLt free
// family (the same precedent).
// Consult H-014 hit-reuse.

private const val DOLLAR_M7 = '$'

// static char* literal adapter for the set_global/get_global
// (key, rslt) CharArray seats (the toZb precedent, per-file here).
private fun czM7(s: String): CharArray {
    val b = CharArray(STR_MAX)
    CstringOps.copyAtS(b, 0, s)
    return b
}

// ------------------------------------------------------------------
// global::Concat(x6 arms beyond the landed unit-65 global-global
// member) - the Mglobal(RETRIEVE) read of THIS global, string cat
// with the operand, MumpsGlobalException on GLOBAL_NOT_FOUND.
// ------------------------------------------------------------------

// mstring global::Concat(double b) - the chunk-edge whole render
// (see the KDoc head: the signature lives at the misc-6 cut).
fun GlobalRef.concat(b: Double): Mstring {
    val gtmp = CharArray(STR_MAX)

    var f = Stubs.Mglobal(RETRIEVE, ref, gtmp, Globals.svPtr)

    if (f == 0) {
        Globals.GlobalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }

    // rslt = b (the mstring(double) ctor sugar = mcvt, the %g
    // Locale.US carrier); strcat(gtmp, rslt.data.c_str());
    val rslt = mcvt(b)
    CstringOps.catAt(gtmp, 0, rslt.str)

    // rslt = gtmp;
    return Mstring(CstringOps.cstring(gtmp))
}

// mstring global::Concat(long b)
fun GlobalRef.concat(b: Long): Mstring {
    val gtmp = CharArray(STR_MAX)

    var f = Stubs.Mglobal(RETRIEVE, ref, gtmp, Globals.svPtr)

    if (f == 0) {
        Globals.GlobalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }

    val rslt = mcvt(b) // rslt = b (the mstring(long) ctor sugar, %d)
    CstringOps.catAt(gtmp, 0, rslt.str)

    return Mstring(CstringOps.cstring(gtmp))
}

// mstring global::Concat(int b)
fun GlobalRef.concat(b: Int): Mstring {
    val gtmp = CharArray(STR_MAX)

    var f = Stubs.Mglobal(RETRIEVE, ref, gtmp, Globals.svPtr)

    if (f == 0) {
        Globals.GlobalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }

    val rslt = mcvt(b) // rslt = b (the mstring(int) ctor sugar, %d)
    CstringOps.catAt(gtmp, 0, rslt.str)

    return Mstring(CstringOps.cstring(gtmp))
}

// mstring global::Concat(char * b) - the char* arm of the fold (the
// std::string twin below is the same body; H-015 String seat).
fun GlobalRef.concat(b: CharArray): Mstring {
    val gtmp = CharArray(STR_MAX)

    var f = Stubs.Mglobal(RETRIEVE, ref, gtmp, Globals.svPtr)

    if (f == 0) {
        Globals.GlobalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }

    CstringOps.catAt(gtmp, 0, b) // strcat(gtmp, b);
    return Mstring(CstringOps.cstring(gtmp))
}

// mstring global::Concat(string b) - the String seat (the vendor
// char xtmp[STR_MAX] decl is dead, H-013 ledger).
fun GlobalRef.concat(b: String): Mstring {
    val gtmp = CharArray(STR_MAX)

    var f = Stubs.Mglobal(RETRIEVE, ref, gtmp, Globals.svPtr)

    if (f == 0) {
        Globals.GlobalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }

    CstringOps.catAt(gtmp, 0, b) // strcat(gtmp, b.c_str());
    return Mstring(CstringOps.cstring(gtmp))
}

// mstring global::Concat(mstring b)
fun GlobalRef.concat(b: Mstring): Mstring {
    val gtmp = CharArray(STR_MAX)

    var f = Stubs.Mglobal(RETRIEVE, ref, gtmp, Globals.svPtr)

    if (f == 0) {
        Globals.GlobalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }

    CstringOps.catAt(gtmp, 0, b.str) // strcat(gtmp, b.c_str());
    return Mstring(CstringOps.cstring(gtmp))
}

// ------------------------------------------------------------------
// mstring::Concat(x7) - this->data cat with the operand; only the
// (global) arm touches the store (Mglobal RETRIEVE of the operand
// reference, GLOBAL_NOT_FOUND throw).
// ------------------------------------------------------------------

// mstring mstring::Concat(global b)
fun Mstring.concat(b: GlobalRef): Mstring {
    val gtmp = CharArray(STR_MAX)

    val f = Stubs.Mglobal(RETRIEVE, b.ref, gtmp, Globals.svPtr)

    if (f == 0) {
        Globals.GlobalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }

    // rslt = data + gtmp;
    return Mstring(str + CstringOps.cstring(gtmp))
}

// mstring mstring::Concat(double b) - rslt=b then data cat
fun Mstring.concat(b: Double): Mstring = Mstring(str + mcvt(b).str)

// mstring mstring::Concat(long b)
fun Mstring.concat(b: Long): Mstring = Mstring(str + mcvt(b).str)

// mstring mstring::Concat(int b)
fun Mstring.concat(b: Int): Mstring = Mstring(str + mcvt(b).str)

// mstring mstring::Concat(char * b) - the char* arm of the fold
// (the std::string twin is the same body; H-015 String seat).
fun Mstring.concat(b: CharArray): Mstring = Mstring(str + CstringOps.cstring(b))

// mstring mstring::Concat(string b)
fun Mstring.concat(b: String): Mstring = Mstring(str + b)

// mstring mstring::Concat(mstring b)
fun Mstring.concat(b: Mstring): Mstring = Mstring(str + b.str)

// ------------------------------------------------------------------
// mstring intrinsic-command members
// ------------------------------------------------------------------

// mstring mstring::Soundex() - the zfcn route (NOTE: the LEADING
// BLANK before the fcn name is the vendor's own - " \u0024zzsoundex",
// unlike the global member of the landed unit-193 render).
fun Mstring.soundex(): Mstring {
    val open = Char(0x00ce)
    val close = Char(0x00cf)
    val tmp = CharArray(STR_MAX)
    val tmp2 = CharArray(STR_MAX)

    // strcpy(tmp, " \u0024zzsoundex"); strcat "\xce"; data; "\xcf";
    CstringOps.strcpyS(tmp, " " + DOLLAR_M7 + "zzsoundex")
    CstringOps.catAt(tmp, 0, open)
    CstringOps.catAt(tmp, 0, str)
    CstringOps.catAt(tmp, 0, close)

    // zfcn(tmp, tmp2, svPtr);
    Stubs.zfcn(tmp, tmp2, Globals.svPtr)

    // m = (char *) tmp2;
    return Mstring(CstringOps.cstring(tmp2))
}

// mstring mstring::Translate(mstring a1) - the ONE-ARG member
// (the two-arg member is the landed unit-191 render; the global
// one-arg member is the landed unit-131 GlobalTranslate render;
// this sibling rides the same fcn v1d/bd offset-1 convention).
fun Mstring.translate(a1: Mstring): Mstring {
    val open = Char(0x00ce)
    val comma = Char(0x00d0)
    val close = Char(0x00cf)
    val tmp = CharArray(STR_MAX)

    // m = "\u0024translate"; m = m.Concat("\xce"); m = m.Concat(data);
    // m = m.Concat("\xd0"); m = m.Concat(a1); m = m.Concat("\xcf");
    CstringOps.strcpyS(tmp, DOLLAR_M7 + "translate")
    CstringOps.catAt(tmp, 0, open)
    CstringOps.catAt(tmp, 0, str)
    CstringOps.catAt(tmp, 0, comma)
    CstringOps.catAt(tmp, 0, a1.str)
    CstringOps.catAt(tmp, 0, close)

    // strcpy(&svPtr->v1d[1], m.data.c_str()); fcn(svPtr);
    CstringOps.copyAt(Globals.svPtr.v1d, 1, tmp)
    Stubs.fcn(Globals.svPtr)

    // m = (char *) &svPtr->bd[1];
    return Mstring(CstringOps.cstringAt(Globals.svPtr.bd, 1))
}

// mstring global::Reverse() - the Mglobal RETRIEVE writes into
// gtmp AT OFFSET 9 (the "\u0024reverse(" prefix is 9 bytes; the
// &gtmp[9] pointer-shift folds to the offset-9 copy, H-012 keyfix
// offset-1 family precedent).
fun GlobalRef.reverse(): Mstring {
    val gtmp = CharArray(STR_MAX)
    val scratch = CharArray(STR_MAX)

    // char gtmp[STR_MAX] = "\u0024reverse(";
    CstringOps.strcpyS(gtmp, DOLLAR_M7 + "reverse(")

    // int f = Mglobal(RETRIEVE, ref, &gtmp[9], svPtr);
    val f = Stubs.Mglobal(RETRIEVE, ref, scratch, Globals.svPtr)
    CstringOps.copyAt(gtmp, 9, scratch)

    if (f == 0) {
        Globals.GlobalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }

    // strcat(gtmp, ")"); m = gtmp; m = m.Eval();
    CstringOps.catAt(gtmp, 0, ")")
    return Mstring(CstringOps.cstring(gtmp)).eval()
}

// mstring mstring::Reverse()
fun Mstring.reverse(): Mstring {
    // m = "\u0024reverse(" + data + ")"; m = m.Eval();
    return Mstring(DOLLAR_M7 + "reverse(" + str + ")").eval()
}

// mstring mstring::Qsubscript(int j)
fun Mstring.qsubscript(j: Int): Mstring {
    // sprintf(i, "%d", j);
    val i = String.format(java.util.Locale.US, "%d", j)

    // m = "\u0024qsubscript(" + data + "," + i + ")"; m = m.Eval();
    return Mstring(DOLLAR_M7 + "qsubscript(" + str + "," + i + ")").eval()
}

// mstring global::Qsubscript(int j) - the Sref arm (the vendor
// reads the quoted-reference member, not ref).
fun GlobalRef.qsubscript(j: Int): Mstring {
    val i = String.format(java.util.Locale.US, "%d", j)

    // m = "\u0024qsubscript(" + Sref + "," + i + ")"; m = m.Eval();
    return Mstring(DOLLAR_M7 + "qsubscript(" + Sref + "," + i + ")").eval()
}

// int mstring::Qlength() - the FixGblRef route (the NEW Stubs
// seat of this unit; the m & chain rides the amp extensions).
fun Mstring.qlength(): Int {
    // m = "\u0024qlength("; m = m & (*this).FixGblRef() & ")";
    var m = Mstring(DOLLAR_M7 + "qlength(")
    m = m.amp(Stubs.fixGblRef(this))
    m = m.amp(")")

    // i = m.Eval(); (the operator int() conversion, H-010)
    return atollShared(m.eval().str).toInt()
}

// int global::Qlength() - the "^" + Sref arm.
fun GlobalRef.qlength(): Int {
    // m = "\u0024qlength(^"; m = m & Sref & ")";
    var m = Mstring(DOLLAR_M7 + "qlength(^")
    m = m.amp(Sref)
    m = m.amp(")")

    // i = m.Eval(); (the operator int() conversion, H-010)
    return atollShared(m.eval().str).toInt()
}

// mstring global::Query()
fun GlobalRef.query(): Mstring {
    // m = "\u0024query(^"; m = m & Sref & ")"; return m.Eval();
    var m = Mstring(DOLLAR_M7 + "query(^")
    m = m.amp(Sref)
    m = m.amp(")")
    return m.eval()
}

// mstring mstring::Query() - the FixGblRef route again.
fun Mstring.query(): Mstring {
    // m = "\u0024query("; m = m & (*this).FixGblRef() & ")";
    var m = Mstring(DOLLAR_M7 + "query(")
    m = m.amp(Stubs.fixGblRef(this))
    m = m.amp(")")
    return m.eval()
}

// ------------------------------------------------------------------
// global conversion operators - the *this >> value typed
// extractions (the GlobalClassMisc operator>> x11 ledger; each
// rides its Stubs.globalGet* seat) as the to* family (H-010).
// ------------------------------------------------------------------

// global::operator char*() - *this >> tmp; return tmp; (the char*
// scratch read; toCz names the char*-domain seat per H-016).
fun GlobalRef.toCz(): String = Stubs.globalGetStrMax(this)

// global::operator int()
fun GlobalRef.toInt(): Int = Stubs.globalGetInt(this)

// global::operator unsigned int()
fun GlobalRef.toUInt(): UInt = Stubs.globalGetUInt(this)

// global::operator long()
fun GlobalRef.toLong(): Long = Stubs.globalGetLong(this)

// global::operator unsigned long()
fun GlobalRef.toULong(): ULong = Stubs.globalGetULong(this)

// global::operator short()
fun GlobalRef.toShort(): Short = Stubs.globalGetShort(this)

// global::operator unsigned short()
fun GlobalRef.toUShort(): UShort = Stubs.globalGetUShort(this)

// global::operator float()
fun GlobalRef.toFloat(): Float = Stubs.globalGetFloat(this)

// global::operator double()
fun GlobalRef.toDouble(): Double = Stubs.globalGetDouble(this)

// global::operator mstring() - the mstring extraction arm rides the
// char* content seat (the extraction family folds its mstring
// member onto the string read, H-010 ledger).
fun GlobalRef.toMstring(): Mstring = Mstring(Stubs.globalGetStrMax(this))

// ------------------------------------------------------------------
// operator*> scalar-star family - s * (*this)-value both ways.
// The unsigned long/unsigned short members compute in the C
// long/short domain (the vendor's own implicit conversions;
// quirk ledgers inline).
// ------------------------------------------------------------------

// int global::operator*(int s)
operator fun GlobalRef.times(s: Int): Int = s * Stubs.globalGetInt(this)

// int operator*(int s, global a) - the friend family renders as
// the globalTimes free fns (H-016 name adaptation; Kotlin operator
// syntax cannot reach the friend form, the globalPlus precedent).
fun globalTimes(s: Int, a: GlobalRef): Int = s * Stubs.globalGetInt(a)

// unsigned int global::operator*(unsigned int s)
operator fun GlobalRef.times(s: UInt): UInt = s * Stubs.globalGetUInt(this)

// unsigned int operator*(unsigned int s, global a)
fun globalTimes(s: UInt, a: GlobalRef): UInt = s * Stubs.globalGetUInt(a)

// double global::operator*(global s) - the strtod domain of both
// reads, a = a*b (the H-007 transient-parse-buffer elision of the
// arm-wide C-protocol strtod precedent).
operator fun GlobalRef.times(s: GlobalRef): Double {
    val b = strtod(Stubs.globalGetStrMax(s))
    val a = strtod(Stubs.globalGetStrMax(this))
    return a * b // a = a * b;
}

// float global::operator*(float s)
operator fun GlobalRef.times(s: Float): Float = s * Stubs.globalGetFloat(this)

// float operator*(float s, global a)
fun globalTimes(s: Float, a: GlobalRef): Float = s * Stubs.globalGetFloat(a)

// double global::operator*(double s)
operator fun GlobalRef.times(s: Double): Double = s * Stubs.globalGetDouble(this)

// double operator*(double s, global a)
fun globalTimes(s: Double, a: GlobalRef): Double = s * Stubs.globalGetDouble(a)

// long global::operator*(long s)
operator fun GlobalRef.times(s: Long): Long = s * Stubs.globalGetLong(this)

// long operator*(long s, global a)
fun globalTimes(s: Long, a: GlobalRef): Long = s * Stubs.globalGetLong(a)

// unsigned long global::operator*(unsigned long s) - long-domain
// compute (the vendor reads a LONG i; the ulong/l mix widens back).
operator fun GlobalRef.times(s: ULong): ULong =
    (s.toLong() * Stubs.globalGetLong(this)).toULong()

// unsigned long operator*(unsigned long s, global a) - long-domain
// compute (same vendor mix).
fun globalTimes(s: ULong, a: GlobalRef): ULong =
    (s.toLong() * Stubs.globalGetLong(a)).toULong()

// short global::operator*(short s) - the C int-domain product
// truncates back to short (kept).
operator fun GlobalRef.times(s: Short): Short =
    (s * Stubs.globalGetShort(this)).toShort()

// short operator*(short s, global a)
fun globalTimes(s: Short, a: GlobalRef): Short =
    (s * Stubs.globalGetShort(a)).toShort()

// unsigned short global::operator*(unsigned short s) - short-domain
// compute (the vendor reads a SHORT i; the ushort/short mix
// widens back).
operator fun GlobalRef.times(s: UShort): UShort =
    (s.toInt() * Stubs.globalGetShort(this).toInt()).toUShort()

// unsigned short operator*(unsigned short s, global a) - short-
// domain compute (same vendor mix).
fun globalTimes(s: UShort, a: GlobalRef): UShort =
    (s.toInt() * Stubs.globalGetShort(a).toInt()).toUShort()

// ------------------------------------------------------------------
// operator> ladder (member + friend forms). The char*/std::string
// member pair folds onto the String seat (H-015); the vendor has
// NO mstring member in the > ladder (the asymmetry against the <
// ladder below is the vendor's own, kept as this ledger).
// ------------------------------------------------------------------

// int global::operator>(global a) - both reads into STR_MAX
// scratches, strcmp(tmp1,tmp2) > 0 (String.compareTo, ASCII byte
// domain H-012).
fun GlobalRef.gt(a: GlobalRef): Boolean =
    Stubs.globalGetStrMax(this) > Stubs.globalGetStrMax(a)

// int global::operator>(int a)
fun GlobalRef.gt(a: Int): Boolean = Stubs.globalGetInt(this) > a

// int global::operator>(unsigned int a)
fun GlobalRef.gt(a: UInt): Boolean = Stubs.globalGetUInt(this) > a

// int global::operator>(short a)
fun GlobalRef.gt(a: Short): Boolean = Stubs.globalGetShort(this) > a

// int global::operator>(unsigned short a)
fun GlobalRef.gt(a: UShort): Boolean = Stubs.globalGetUShort(this) > a

// int global::operator>(long a)
fun GlobalRef.gt(a: Long): Boolean = Stubs.globalGetLong(this) > a

// int global::operator>(unsigned long a)
fun GlobalRef.gt(a: ULong): Boolean = Stubs.globalGetULong(this) > a

// int global::operator>(float a)
fun GlobalRef.gt(a: Float): Boolean = Stubs.globalGetFloat(this) > a

// int global::operator>(double a)
fun GlobalRef.gt(a: Double): Boolean = Stubs.globalGetDouble(this) > a

// int global::operator>(char * a) / (string a) - String seat fold.
fun GlobalRef.gt(a: String): Boolean = Stubs.globalGetStrMax(this) > a

// int operator>(int a, global b) - the friend family
// (globalPlus precedent: globalGt free fns).
fun globalGt(a: Int, b: GlobalRef): Boolean = a > Stubs.globalGetInt(b)

fun globalGt(a: UInt, b: GlobalRef): Boolean = a > Stubs.globalGetUInt(b)

fun globalGt(a: Short, b: GlobalRef): Boolean = a > Stubs.globalGetShort(b)

fun globalGt(a: UShort, b: GlobalRef): Boolean = a > Stubs.globalGetUShort(b)

fun globalGt(a: Long, b: GlobalRef): Boolean = a > Stubs.globalGetLong(b)

fun globalGt(a: ULong, b: GlobalRef): Boolean = a > Stubs.globalGetULong(b)

fun globalGt(a: Float, b: GlobalRef): Boolean = a > Stubs.globalGetFloat(b)

fun globalGt(a: Double, b: GlobalRef): Boolean = a > Stubs.globalGetDouble(b)

// int operator>(char * a, global b) / (string a, global b) - String
// seat fold (strcmp(a, tmp1) > 0).
fun globalGt(a: String, b: GlobalRef): Boolean = a > Stubs.globalGetStrMax(b)

// ------------------------------------------------------------------
// operator< ladder - mirrors the > family plus the mstring member
// (char*/string fold onto the String seat, mstring kept).
// ------------------------------------------------------------------

// int global::operator<(global a)
fun GlobalRef.lt(a: GlobalRef): Boolean =
    Stubs.globalGetStrMax(this) < Stubs.globalGetStrMax(a)

fun GlobalRef.lt(a: Int): Boolean = Stubs.globalGetInt(this) < a

fun GlobalRef.lt(a: UInt): Boolean = Stubs.globalGetUInt(this) < a

fun GlobalRef.lt(a: Short): Boolean = Stubs.globalGetShort(this) < a

fun GlobalRef.lt(a: UShort): Boolean = Stubs.globalGetUShort(this) < a

fun GlobalRef.lt(a: Long): Boolean = Stubs.globalGetLong(this) < a

fun GlobalRef.lt(a: ULong): Boolean = Stubs.globalGetULong(this) < a

fun GlobalRef.lt(a: Float): Boolean = Stubs.globalGetFloat(this) < a

fun GlobalRef.lt(a: Double): Boolean = Stubs.globalGetDouble(this) < a

// int global::operator<(char * a) / (string a) - String seat fold.
fun GlobalRef.lt(a: String): Boolean = Stubs.globalGetStrMax(this) < a

// int global::operator<(mstring a) - strcmp domain (ASCII H-012).
fun GlobalRef.lt(a: Mstring): Boolean = Stubs.globalGetStrMax(this) < a.str

// friend family
fun globalLt(a: Int, b: GlobalRef): Boolean = a < Stubs.globalGetInt(b)

fun globalLt(a: UInt, b: GlobalRef): Boolean = a < Stubs.globalGetUInt(b)

fun globalLt(a: Short, b: GlobalRef): Boolean = a < Stubs.globalGetShort(b)

fun globalLt(a: UShort, b: GlobalRef): Boolean = a < Stubs.globalGetUShort(b)

fun globalLt(a: Long, b: GlobalRef): Boolean = a < Stubs.globalGetLong(b)

fun globalLt(a: ULong, b: GlobalRef): Boolean = a < Stubs.globalGetULong(b)

fun globalLt(a: Float, b: GlobalRef): Boolean = a < Stubs.globalGetFloat(b)

fun globalLt(a: Double, b: GlobalRef): Boolean = a < Stubs.globalGetDouble(b)

// int operator<(char * a, global b) / (string a, global b) - String
// seat fold.
fun globalLt(a: String, b: GlobalRef): Boolean = a < Stubs.globalGetStrMax(b)

// int operator<(mstring a, global b) - strcmp domain (ASCII H-012).
fun globalLt(a: Mstring, b: GlobalRef): Boolean = a.str < Stubs.globalGetStrMax(b)

// ------------------------------------------------------------------
// operator<<(global) write-assign family - the set_global/
// get_global Stubs seats; returns the global reference itself.
// ------------------------------------------------------------------

// global & global::operator<<(char * s) - the Kotlin shl seat is
// the plain fun (the H-016 prefix form has no infix syntax for the
// << convention on the left operand; globalTimes twin note).
// the char* arm of the fold
// (the std::string twins ride the same seat; H-015 String seat).
fun GlobalRef.shl(s: String): GlobalRef {
    // set_global(ref, s);
    Stubs.set_global(czM7(ref), czM7(s))
    return this
}

// global & global::operator<<(global g) - read-through-assign: the
// get_global of g's Get() content, the tpx zero check with the
// GLOBAL_NOT_FOUND throw, then the set_global of the buffer.
fun GlobalRef.shl(g: GlobalRef): GlobalRef {
    val s = CharArray(STR_MAX)

    // get_global(g.get(), s);
    Stubs.get_global(czM7(g.get()), s)

    if (Globals.svPtr.tpx == 0L) {
        Globals.GlobalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }

    // set_global(ref, s);
    Stubs.set_global(czM7(ref), s)
    return this
}

// global & global::operator<<(int i)
fun GlobalRef.shl(i: Int): GlobalRef {
    // sprintf(s, "%d", i);
    Stubs.set_global(czM7(ref), czM7(String.format(java.util.Locale.US, "%d", i)))
    return this
}

// global & global::operator<<(unsigned int i) - VENDOR QUIRK: the
// sprintf format is "%ud" (a percent-u-D literal, NOT %u; kept
// byte-verbatim, only the % hardcode noted).
fun GlobalRef.shl(i: UInt): GlobalRef {
    Stubs.set_global(czM7(ref), czM7(String.format(java.util.Locale.US, "%ud", i)))
    return this
}

// global & global::operator<<(short i)
fun GlobalRef.shl(i: Short): GlobalRef {
    Stubs.set_global(czM7(ref), czM7(String.format(java.util.Locale.US, "%hd", i)))
    return this
}

// CHUNK TAIL: the dump closes at the operator<<(short) close
// brace; the unsigned short/long/unsigned long/float/double shl
// members that follow in the vendor file are the next chunk's
// text (ledger only here, per the misc-chunk template).
