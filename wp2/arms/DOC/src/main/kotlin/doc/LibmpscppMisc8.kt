package doc

// Unit 269 (B9 tranche 3): libmpscpp.cpp::<misc-3> (batch 9,
// complex stratum; units.csv row 249: loc 600, score 472.2, rank
// 300; physLOC 600 physically verified this session) - the
// scalar/global comparison ladders (operator<= tail, operator==
// x2 ladders, operator!= x2 ladders), the global write-assign
// family (global::operator= x11), the mid-file Smith-Waterman
// banner/globals region, and the mstring ctor block (operator[]
// x4, the +=/-=/*=///=/%= assign families, the operator= scalar
// ctors). All derived from the pristine vendor source this
// session (sha256 621deb...ffdbe).
//
// CHUNK-EDGE ORPHAN (the misc-chunk template): the dump HEAD is
// the orphan TAIL of operator<=(short,global b) - the signature
// line lives at the misc-2 chunk's cut. This chunk owns the
// body text, so globalLe(Short,GlobalRef) renders WHOLE here
// (the unit-238 Concat head/tail precedent).
//
// The char*/string twins of every ladder fold onto the String
// seat (H-015, the mstring.lt ladder precedent). VENDOR SIGN
// QUIRK kept byte-identical and ledgered: the string friend
// forms compare with the REVERSED sign - operator==(string)
// friend reads strcmp(a.c_str(),tmp1)>=0, operator!=(string)
// friend the same >=0, operator<=(string) friend >=0 - while
// the char* twins read the signed direction. The vendor bytes
// stand: the >=0 renders verbatim below.
//
// The global >> typed read rides the landed Stubs.globalGet*
// seats (ArithOpsMisc2); the string reads ride
// Stubs.getGlobalString; the write-assign globals ride
// Stubs.set_global (the LibmpscppMisc7 shl family precedent,
// including the =(global s) read-through WITHOUT the tpx check
// - the vendor =(global) body has no tpx probe, unlike the
// shl(global) body; kept verbatim). The %ud/%uhd/%lud sprintf
// quirks of the unsigned assign arms: the printf %u lead
// consumes the value and the trailing "d" is a LITERAL (H-009
// byte note; String.format(Locale.US) would reject the pair),
// so the renders emit the unsigned decimal plus the literal
// "d" tail. The %hd/%ld arms fold to the plain decimal
// (Locale.US). The float assign arms ride the landed gcvt
// (CompilerSys1Misc, the numSetup gcvtWrapper family ruling,
// %g per H-009).
//
// The +=/-=/*=///=/%= bodies depend on the mstring LIBRARY
// binary backends (operator+/-/*//% - batch-10 roster rows per
// the MstringClassMisc.kt header ledger): the dependent bodies
// route through the NEW H-014 Stubs.mstringPlus/mstringMinus/
// mstringTimes/mstringDiv/mstringRem seats raised this tranche
// (the unit-154 globalDocCorrelate front-run precedent: seats
// stay with their owners, flip on owner landing). The vendor
// +=(mstring) arm returns *this+i WITHOUT assigning (*this) -
// the quirk kept verbatim (H-013 note below). The operator[]
// x4 ctor family is the vendor *(this+i) POINTER-ARITHMETIC
// idiom (indexing past the object header - UB in the vendor);
// no JVM pointer-arithmetic carrier exists, so the arms fold
// inert returning this with the idiom ledgered (H-012/H-018
// shape, the dead-store family). The = scalar ctors render as
// the invoke2 = seat (the Hash.invoke2 precedent; the existing
// invoke(String) member IS the string/char* = seat already,
// H-015 fold, no second definition); the (long) arm keeps the
// vendor %d-for-long quirk byte-identical (H-009 note).
//
// The mid-file Smith-Waterman region: the "shift in"/"shift
// out" section banners, StopList stop file-scope var, the
// extern-C cmpr, the Syn syn[SYNMAX]/synx DEFINITION site (the
// landed SynStatic owner - H-004 one-owner, no second
// definition, cited), the #undef kill fossil, the sw banner
// (kept verbatim in the ledger below), the in-mid-file include
// block (iostream/stdio/time/signal/string/stdlib + using
// namespace std - banner ledger only, H-004), MAX_ALIGNS/
// EMPTY_CELL defines, and the sw file-scope globals (__a,
// S_RESULT, Size, Size_s, Size_t, GAP, MISMATCH, MATCH, sx -
// the H-004 holder object below; their consumers are the later
// batch rows).
// Consult row logged 2026-11-22T05:32:00Z (H-004 hit-reuse,
// the banner/global-pull precedent). Naming per H-017/H-016
// (globalLe/globalEq/globalNe free friend families, the landed
// globalLt/globalGt precedent; member verbs GlobalRef.eq/ne/
// assign; Mstring.addAssign/subAssign/mulAssign/divAssign/
// remAssign per the ArithOpsMisc2 addAssignInt family;
// Mstring.get/invoke2 per the Hash precedents).

private const val M8_GCVT_DIGITS = 15 // the =(double/float) width

// static char* literal adapter for the set_global/get_global
// (key, rslt) CharArray seats (the LibmpscppMisc7 czM7 twin,
// per-file here).
private fun czM8(s: String): CharArray {
    val b = CharArray(STR_MAX)
    CstringOps.copyAtS(b, 0, s)
    return b
}

// ------------------------------------------------------------------
// operator<=(scalar, global) - the friend <= family (the orphan
// head is operator<=(short,global); rendered whole here).
// ------------------------------------------------------------------

// int operator<=(short a,global b) - chunk-edge whole render
fun globalLe(a: Short, b: GlobalRef): Boolean {
    val i = Stubs.globalGetShort(b)
    return a <= i
}

// int operator<=(unsigned short a,global b)
fun globalLe(a: UShort, b: GlobalRef): Boolean {
    val i = Stubs.globalGetUShort(b)
    return a <= i
}

// int operator<=(long a,global b)
fun globalLe(a: Long, b: GlobalRef): Boolean {
    val i = Stubs.globalGetLong(b)
    return a <= i
}

// int operator<=(unsigned long a,global b)
fun globalLe(a: ULong, b: GlobalRef): Boolean {
    val i = Stubs.globalGetULong(b)
    return a <= i
}

// int operator<=(float a,global b)
fun globalLe(a: Float, b: GlobalRef): Boolean {
    val i = Stubs.globalGetFloat(b)
    return a <= i
}

// int operator<=(double a,global b)
fun globalLe(a: Double, b: GlobalRef): Boolean {
    val i = Stubs.globalGetDouble(b)
    return a <= i
}

// int operator<=(char * a,global b)
fun globalLe(a: String, b: GlobalRef): Boolean {
    val tmp1 = Stubs.getGlobalString(b)
    // if (strcmp(a,tmp1)<=0) return 1;
    return a.compareTo(tmp1) <= 0
}

// int operator<=(string a,global b) - VENDOR SIGN QUIRK: the
// string twin compares with >=0 (byte-identical keep, header
// ledger); NOT the char* twin's direction.
fun globalLeStrM8(a: String, b: GlobalRef): Boolean {
    val tmp1 = Stubs.getGlobalString(b)
    // if (strcmp(a.c_str(),tmp1)>=0) return 1;
    return a.compareTo(tmp1) >= 0
}

// ------------------------------------------------------------------
// global::operator== ladder - the member forms.
// ------------------------------------------------------------------

// int global::operator==(global a)
fun GlobalRef.eq(a: GlobalRef): Boolean {
    val tmp1 = Stubs.getGlobalString(this)
    val tmp2 = Stubs.getGlobalString(a)
    return tmp1 == tmp2
}

// int global::operator==(int a)
fun GlobalRef.eq(a: Int): Boolean {
    val i = Stubs.globalGetInt(this)
    return i == a
}

fun GlobalRef.eq(a: UInt): Boolean {
    val i = Stubs.globalGetUInt(this)
    return i == a
}

fun GlobalRef.eq(a: Short): Boolean {
    val i = Stubs.globalGetShort(this)
    return i == a
}

fun GlobalRef.eq(a: UShort): Boolean {
    val i = Stubs.globalGetUShort(this)
    return i == a
}

fun GlobalRef.eq(a: Long): Boolean {
    val i = Stubs.globalGetLong(this)
    return i == a
}

fun GlobalRef.eq(a: ULong): Boolean {
    val i = Stubs.globalGetULong(this)
    return i == a
}

fun GlobalRef.eq(a: Float): Boolean {
    val i = Stubs.globalGetFloat(this)
    return i == a
}

fun GlobalRef.eq(a: Double): Boolean {
    val i = Stubs.globalGetDouble(this)
    return i == a
}

// int global::operator==(char * a) / (string a) - the String
// seat fold (H-015 x2)
fun GlobalRef.eq(a: String): Boolean {
    val tmp1 = Stubs.getGlobalString(this)
    // if (strcmp(tmp1,a)==0) return 1;
    return tmp1 == a
}

// ------------------------------------------------------------------
// friends: operator==(scalar, global)
// ------------------------------------------------------------------

fun globalEq(a: Int, b: GlobalRef): Boolean {
    val i = Stubs.globalGetInt(b)
    return a == i
}

fun globalEq(a: UInt, b: GlobalRef): Boolean {
    val i = Stubs.globalGetUInt(b)
    return a == i
}

fun globalEq(a: Short, b: GlobalRef): Boolean {
    val i = Stubs.globalGetShort(b)
    return a == i
}

fun globalEq(a: UShort, b: GlobalRef): Boolean {
    val i = Stubs.globalGetUShort(b)
    return a == i
}

fun globalEq(a: Long, b: GlobalRef): Boolean {
    val i = Stubs.globalGetLong(b)
    return a == i
}

fun globalEq(a: ULong, b: GlobalRef): Boolean {
    val i = Stubs.globalGetULong(b)
    return a == i
}

fun globalEq(a: Float, b: GlobalRef): Boolean {
    val i = Stubs.globalGetFloat(b)
    return a == i
}

fun globalEq(a: Double, b: GlobalRef): Boolean {
    val i = Stubs.globalGetDouble(b)
    return a == i
}

// int operator==(char * a,global b)
fun globalEq(a: String, b: GlobalRef): Boolean {
    val tmp1 = Stubs.getGlobalString(b)
    // if (strcmp(a,tmp1)==0) return 1;
    return a == tmp1
}

// int operator==(string a,global b) - VENDOR SIGN QUIRK (the
// string friend form reads >=0 here too - byte-identical keep,
// header ledger)
fun globalEqStrM8(a: String, b: GlobalRef): Boolean {
    val tmp1 = Stubs.getGlobalString(b)
    // if (strcmp(a.c_str(),tmp1)>=0) return 1;
    return a.compareTo(tmp1) >= 0
}

// ------------------------------------------------------------------
// global::operator!= ladder + friends (the != family).
// ------------------------------------------------------------------

fun GlobalRef.ne(a: GlobalRef): Boolean {
    val tmp1 = Stubs.getGlobalString(this)
    val tmp2 = Stubs.getGlobalString(a)
    return tmp1 != tmp2
}

fun GlobalRef.ne(a: Int): Boolean {
    val i = Stubs.globalGetInt(this)
    return i != a
}

fun GlobalRef.ne(a: UInt): Boolean {
    val i = Stubs.globalGetUInt(this)
    return i != a
}

fun GlobalRef.ne(a: Short): Boolean {
    val i = Stubs.globalGetShort(this)
    return i != a
}

fun GlobalRef.ne(a: UShort): Boolean {
    val i = Stubs.globalGetUShort(this)
    return i != a
}

fun GlobalRef.ne(a: Long): Boolean {
    val i = Stubs.globalGetLong(this)
    return i != a
}

fun GlobalRef.ne(a: ULong): Boolean {
    val i = Stubs.globalGetULong(this)
    return i != a
}

fun GlobalRef.ne(a: Float): Boolean {
    val i = Stubs.globalGetFloat(this)
    return i != a
}

fun GlobalRef.ne(a: Double): Boolean {
    val i = Stubs.globalGetDouble(this)
    return i != a
}

fun GlobalRef.ne(a: String): Boolean {
    val tmp1 = Stubs.getGlobalString(this)
    return tmp1 != a
}

fun globalNe(a: Int, b: GlobalRef): Boolean {
    val i = Stubs.globalGetInt(b)
    return a != i
}

fun globalNe(a: UInt, b: GlobalRef): Boolean {
    val i = Stubs.globalGetUInt(b)
    return a != i
}

fun globalNe(a: Short, b: GlobalRef): Boolean {
    val i = Stubs.globalGetShort(b)
    return a != i
}

fun globalNe(a: UShort, b: GlobalRef): Boolean {
    val i = Stubs.globalGetUShort(b)
    return a != i
}

fun globalNe(a: Long, b: GlobalRef): Boolean {
    val i = Stubs.globalGetLong(b)
    return a != i
}

fun globalNe(a: ULong, b: GlobalRef): Boolean {
    val i = Stubs.globalGetULong(b)
    return a != i
}

fun globalNe(a: Float, b: GlobalRef): Boolean {
    val i = Stubs.globalGetFloat(b)
    return a != i
}

fun globalNe(a: Double, b: GlobalRef): Boolean {
    val i = Stubs.globalGetDouble(b)
    return a != i
}

fun globalNe(a: String, b: GlobalRef): Boolean {
    val tmp1 = Stubs.getGlobalString(b)
    return a != tmp1
}

// int operator!=(string a,global b) - VENDOR SIGN QUIRK (the
// >=0 read, byte-identical keep, header ledger)
fun globalNeStrM8(a: String, b: GlobalRef): Boolean {
    val tmp1 = Stubs.getGlobalString(b)
    // if (strcmp(a.c_str(),tmp1)>=0) return 1;
    return a.compareTo(tmp1) >= 0
}

// ------------------------------------------------------------------
// global & global::operator=(T s) - the write-assign family
// (set_global through the CharArray seats; returns the global
// reference itself).
// ------------------------------------------------------------------

// =(const char *) / =(string) / =(mstring) - the String seat
// fold (H-015 x3)
fun GlobalRef.assign(s: String): GlobalRef {
    // set_global(ref,s);
    Stubs.set_global(czM8(ref), czM8(s))
    return this
}

// =(int s)
fun GlobalRef.assign(s: Int): GlobalRef {
    val tmp = CharArray(128)
    // sprintf(tmp,\"%d\",s);
    CstringOps.copyAtS(tmp, 0, String.format(java.util.Locale.US, "%d", s))
    Stubs.set_global(czM8(ref), tmp)
    return this
}

// =(double s) - gcvt_wrapper(s,15,tmp)
fun GlobalRef.assign(s: Double): GlobalRef {
    val tmp = CharArray(128)
    gcvt(s, M8_GCVT_DIGITS, tmp)
    Stubs.set_global(czM8(ref), tmp)
    return this
}

// =(global s) - read-through WITHOUT the tpx probe (the shl
// twin HAS the probe; kept verbatim, header ledger)
fun GlobalRef.assign(s: GlobalRef): GlobalRef {
    val tmp1 = CharArray(STR_MAX)
    Stubs.get_global(czM8(s.get()), tmp1)
    Stubs.set_global(czM8(ref), tmp1)
    return this
}

// =(unsigned int s) - %ud: the %u lead + literal \"d\" tail
fun GlobalRef.assign(s: UInt): GlobalRef {
    val tmp = CharArray(128)
    CstringOps.copyAtS(tmp, 0, s.toString() + "d")
    Stubs.set_global(czM8(ref), tmp)
    return this
}

// =(float s) - gcvt_wrapper(s,15,tmp)
fun GlobalRef.assign(s: Float): GlobalRef {
    val tmp = CharArray(128)
    gcvt(s.toDouble(), M8_GCVT_DIGITS, tmp)
    Stubs.set_global(czM8(ref), tmp)
    return this
}

// =(short s) - %hd
fun GlobalRef.assign(s: Short): GlobalRef {
    val tmp = CharArray(128)
    CstringOps.copyAtS(tmp, 0, String.format(java.util.Locale.US, "%d", s))
    Stubs.set_global(czM8(ref), tmp)
    return this
}

// =(unsigned short s) - %uhd: the %uh lead + literal \"d\" tail
fun GlobalRef.assign(s: UShort): GlobalRef {
    val tmp = CharArray(128)
    CstringOps.copyAtS(tmp, 0, s.toString() + "d")
    Stubs.set_global(czM8(ref), tmp)
    return this
}

// =(long s) - %ld
fun GlobalRef.assign(s: Long): GlobalRef {
    val tmp = CharArray(128)
    CstringOps.copyAtS(tmp, 0, String.format(java.util.Locale.US, "%d", s))
    Stubs.set_global(czM8(ref), tmp)
    return this
}

// =(unsigned long s) - %lud: the %lu lead + literal \"d\" tail
fun GlobalRef.assign(s: ULong): GlobalRef {
    val tmp = CharArray(128)
    CstringOps.copyAtS(tmp, 0, s.toString() + "d")
    Stubs.set_global(czM8(ref), tmp)
    return this
}

// ------------------------------------------------------------------
// The mid-file Smith-Waterman region (H-004 banner/globals
// ledger; the consumers are later batch rows).
// ------------------------------------------------------------------

// //=========== shift in ============================================
// //================ shift out =======================================
// (the empty section banners, verbatim)

// StopList stop; - the files-scope var names the StopList
// carrier (the struct lives in libmpscpp.h; its arm render
// lands with its owner row) - ledger, not rendered here.

// extern \"C\" int cmpr(const char *a, const Syn *b)
fun cmpr(a: String, b: Syn): Int {
    // return strcmp((const char *) a, (*b).word);
    return a.compareTo(b.word)
}

// Syn syn[SYNMAX]; long synx; - the DEFINITION site of the
// landed SynStatic holder (SynInit.kt; H-004 one-owner rule,
// no second definition; cited).

// #undef kill  // also used in signal.h (the fossil, verbatim)

// The sw banner (kept verbatim, trimmed to the ledger below):
// IMPORTANT: sequences MUST have a blank in array position 0;
// to invoke: int sw (char *, char *, int, int, int, int, int);
// score = sw(seq1, seq2 [,flg1, flg2, gap, mismatch, match]);
// gap penalty default -1; mismatch penalty default -1; match
// reward default 2 (the full vendor banner preserved in the
// journal fragment).

// #include <iostream> <stdio.h> <time.h> <signal.h> <string.h>
// <stdlib.h>; using namespace std; - the mid-file include
// block rides the H-004 banner ledger (arm-wide io carriers).

// #define MAX_ALIGNS 5000000
private const val MAX_ALIGNS_M8 = 5000000

// #define EMPTY_CELL -32768 (short domain, H-012)
private const val EMPTY_CELL_M8: Short = -32768

// short **__a; string S_RESULT; int Size; int Size_s; int
// Size_t; int GAP; int MISMATCH; int MATCH; int sx=0; - the sw
// file-scope globals (H-004 holder; consumers are the later
// batch rows; the short** matrix rides the row-of-rows shape)
object SwGlobalsM8 {
    var __a: Array<ShortArray>? = null
    var S_RESULT: String = ""
    var Size = 0
    var Size_s = 0
    var Size_t = 0
    var GAP = 0 // gap penalty
    var MISMATCH = 0 // mis-match penalty
    var MATCH = 0 // match reward
    var sx = 0
}

// ------------------------------------------------------------------
// mstring constructors block (operator[] x4, the assign-operator
// families, the scalar = ctors).
// ------------------------------------------------------------------

// mstring & mstring::operator[](int i) - the *(this+i) pointer
// idiom (UB in the vendor): no JVM pointer-arithmetic carrier;
// the arm folds inert keeping the body ledgers (H-012/H-018
// shape). x4: (int)/(mstring)/(const char*)/(string) - the
// atoi forms coerce then fold identically.
operator fun Mstring.get(i: Int): Mstring {
    // return *(this+i);
    return this
}

operator fun Mstring.get(x: Mstring): Mstring {
    // int i; i=atoi(x.c_str()); return *(this+i);
    return this
}

operator fun Mstring.get(x: String): Mstring {
    // int i; i=atoi(x.c_str()); return *(this+i);
    return this
}

// mstring mstring::operator+=(mstring i) - VENDOR QUIRK: the
// body returns *this+i WITHOUT assigning (*this) (H-013 note);
// the binary backend rides the raised H-014 seat.
fun Mstring.addAssign(i: Mstring): Mstring {
    // return *this+i;
    return Stubs.mstringPlus(this, i)
}

fun Mstring.addAssign(i: Int): Mstring {
    // (*this)=(*this)+i; return (*this);
    val r = Stubs.mstringPlus(this, i)
    str = r.str
    return this
}

fun Mstring.addAssign(i: Long): Mstring {
    val r = Stubs.mstringPlus(this, i)
    str = r.str
    return this
}

fun Mstring.addAssign(i: Double): Mstring {
    val r = Stubs.mstringPlus(this, i)
    str = r.str
    return this
}

fun Mstring.addAssign(i: Float): Mstring {
    val r = Stubs.mstringPlus(this, i)
    str = r.str
    return this
}

// (string)/(const char*) - the String seat fold (H-015 x2)
fun Mstring.addAssign(i: String): Mstring {
    val r = Stubs.mstringPlus(this, i)
    str = r.str
    return this
}

fun Mstring.addAssign(i: GlobalRef): Mstring {
    val r = Stubs.mstringPlus(this, i)
    str = r.str
    return this
}

// mstring operator-= family (x8 - the same arm roster)
fun Mstring.subAssign(i: Mstring): Mstring {
    // (*this)=(*this)-i; return (*this);
    val r = Stubs.mstringMinus(this, i)
    str = r.str
    return this
}

fun Mstring.subAssign(i: Int): Mstring {
    val r = Stubs.mstringMinus(this, i)
    str = r.str
    return this
}

fun Mstring.subAssign(i: Long): Mstring {
    val r = Stubs.mstringMinus(this, i)
    str = r.str
    return this
}

fun Mstring.subAssign(i: Double): Mstring {
    val r = Stubs.mstringMinus(this, i)
    str = r.str
    return this
}

fun Mstring.subAssign(i: Float): Mstring {
    val r = Stubs.mstringMinus(this, i)
    str = r.str
    return this
}

fun Mstring.subAssign(i: String): Mstring {
    val r = Stubs.mstringMinus(this, i)
    str = r.str
    return this
}

fun Mstring.subAssign(i: GlobalRef): Mstring {
    val r = Stubs.mstringMinus(this, i)
    str = r.str
    return this
}

// mstring operator*= family (x8)
fun Mstring.mulAssign(i: Mstring): Mstring {
    // (*this)=(*this)*i; return (*this);
    val r = Stubs.mstringTimes(this, i)
    str = r.str
    return this
}

fun Mstring.mulAssign(i: Int): Mstring {
    val r = Stubs.mstringTimes(this, i)
    str = r.str
    return this
}

fun Mstring.mulAssign(i: Long): Mstring {
    val r = Stubs.mstringTimes(this, i)
    str = r.str
    return this
}

fun Mstring.mulAssign(i: Double): Mstring {
    val r = Stubs.mstringTimes(this, i)
    str = r.str
    return this
}

fun Mstring.mulAssign(i: Float): Mstring {
    val r = Stubs.mstringTimes(this, i)
    str = r.str
    return this
}

fun Mstring.mulAssign(i: String): Mstring {
    val r = Stubs.mstringTimes(this, i)
    str = r.str
    return this
}

fun Mstring.mulAssign(i: GlobalRef): Mstring {
    val r = Stubs.mstringTimes(this, i)
    str = r.str
    return this
}

// mstring operator/= family (x8)
fun Mstring.divAssign(i: Mstring): Mstring {
    // (*this)=(*this)/i; return (*this);
    val r = Stubs.mstringDiv(this, i)
    str = r.str
    return this
}

fun Mstring.divAssign(i: Int): Mstring {
    val r = Stubs.mstringDiv(this, i)
    str = r.str
    return this
}

fun Mstring.divAssign(i: Long): Mstring {
    val r = Stubs.mstringDiv(this, i)
    str = r.str
    return this
}

fun Mstring.divAssign(i: Double): Mstring {
    val r = Stubs.mstringDiv(this, i)
    str = r.str
    return this
}

fun Mstring.divAssign(i: Float): Mstring {
    val r = Stubs.mstringDiv(this, i)
    str = r.str
    return this
}

fun Mstring.divAssign(i: String): Mstring {
    val r = Stubs.mstringDiv(this, i)
    str = r.str
    return this
}

fun Mstring.divAssign(i: GlobalRef): Mstring {
    val r = Stubs.mstringDiv(this, i)
    str = r.str
    return this
}

// mstring operator%= family (x6: mstring/int/long/string/
// const char*/global)
fun Mstring.remAssign(i: Mstring): Mstring {
    // (*this)=(*this)%i; return (*this);
    val r = Stubs.mstringRem(this, i)
    str = r.str
    return this
}

fun Mstring.remAssign(i: Int): Mstring {
    val r = Stubs.mstringRem(this, i)
    str = r.str
    return this
}

fun Mstring.remAssign(i: Long): Mstring {
    val r = Stubs.mstringRem(this, i)
    str = r.str
    return this
}

fun Mstring.remAssign(i: String): Mstring {
    val r = Stubs.mstringRem(this, i)
    str = r.str
    return this
}

fun Mstring.remAssign(i: GlobalRef): Mstring {
    val r = Stubs.mstringRem(this, i)
    str = r.str
    return this
}

// mstring& mstring::operator=(T i) - the scalar = ctors (the
// invoke2 = seat, the Hash precedent). The char*/string arms
// ARE the existing invoke(String) member (H-015 fold; no second
// definition); the (long) arm keeps the vendor %d-for-long
// quirk byte-identical (H-009 note; a Kotlin Long renders the
// same decimal text so the fold is byte-equal by construction).

// =(int i)
fun Mstring.invoke2(i: Int): Mstring {
    // char tmp[32]=\"\"; sprintf(tmp,\"%d\",i); data=tmp;
    str = String.format(java.util.Locale.US, "%d", i)
    return this
}

// =(long i) - the %d quirk (see header)
fun Mstring.invoke2(i: Long): Mstring {
    str = String.format(java.util.Locale.US, "%d", i)
    return this
}

// =(double i) - %g (H-009; the gcvt shortest-form family)
fun Mstring.invoke2(i: Double): Mstring {
    str = String.format(java.util.Locale.US, "%g", i)
    return this
}

// =(float i) - %g on the promoted float
fun Mstring.invoke2(i: Float): Mstring {
    str = String.format(java.util.Locale.US, "%g", i.toDouble())
    return this
}

// =(mstring i) - data=i.data
fun Mstring.invoke2(i: Mstring): Mstring {
    str = i.str
    return this
}
