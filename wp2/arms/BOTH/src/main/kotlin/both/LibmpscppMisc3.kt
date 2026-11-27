package both

// Unit 276 (BOTH B9, giant): libmpscpp.cpp::<misc-3> (600
// LOC). Batch_plan ordinal 276: giant. The scalar/global
// comparison ladders (the operator<= friend tail, the == and
// != ladders member AND friend sides), the global write-assign
// family (global::operator= x11), the mid-file Smith-Waterman
// banner/globals region, and the mstring ctor block (the
// operator[] x4, the +=/-=/*=///=/%= assign families, the
// operator= scalar ctors).
//
// CHUNK-EDGE ORPHAN: the dump HEAD is the orphan TAIL of
// operator<=(short,global b) - the signature line lives at the
// misc-2 chunk's cut (the unit-217 LibmpscppMisc7 render).
// This chunk owns the body text, so globalLe(Short, GlobalRef)
// renders WHOLE here (the Concat head/tail precedent).
//
// The char*/string twins of every ladder fold onto the String
// seat (h015, the mstring.lt ladder precedent). VENDOR SIGN
// QUIRK kept byte-identical and ledgered: the string friend
// forms compare with the REVERSED sign - operator==(string)
// friend reads strcmp(a.c_str(),tmp1)>=0, operator!=(string)
// friend the same >=0, operator<=(string) friend >=0 - while
// the char* twins read the signed direction. The vendor bytes
// stand: the >=0 renders verbatim below.
//
// The global >> typed read rides the landed GlobalRef.Int()/
// Double()/extractValue() seats (LibmpscppMisc5; the
// unsigned/short arms are same-body folds, the h021 ledger of
// the Misc7 cast block); the write-assign globals ride the
// set_global macro fold of the shlG family (LibmpscppMisc7:
// tpx = Mglobal(STORE, ...)), INCLUDING the =(global s)
// read-through WITHOUT the tpx check - the vendor =(global)
// body has no tpx probe, unlike the shlG(global) body; kept
// verbatim. The %ud/%uhd/%lud sprintf quirks of the unsigned
// assign arms: the printf %u lead consumes the value and the
// trailing "d" is a LITERAL (h009 byte note; the shlGU
// precedent), so the renders emit the unsigned decimal plus
// the literal "d" tail. The %hd/%ld arms fold to the plain
// decimal. The float/double assign arms ride the %g fold at
// precision 15 (the gcvt_wrapper(s,15,tmp) call; the cFormatG
// seat, h009).
//
// The +=/-=/*=///=/%= bodies depend on the mstring LIBRARY
// binary backends (operator+/-/*//% - batch-10 roster rows:
// the operator* family lands with libmpscpp.cpp::<misc-8>
// (global unit 306, both_seq 183); the +/-//% families with
// libmpscpp.cpp::<misc-4> (global unit 304, both_seq 181),
// both batch 10 of this arm's cycle): the dependent bodies
// route through the NEW h014 stubs raised this render
// (mstringPlusZb/mstringMinusZb/mstringTimesZb/
// mstringDivZb/mstringRemZb; seats stay with their owners,
// flip on owner landing). The vendor +=(mstring) arm returns
// *this+i WITHOUT assigning (*this) - the quirk kept verbatim
// (h013 note). The operator[] x4 ctor family is the vendor
// *(this+i) POINTER-ARITHMETIC idiom (indexing past the object
// header - UB in the vendor); no JVM pointer-arithmetic carrier
// exists, so the arms fold inert returning the receiver with
// the idiom ledgered (h012/h018 shape, the dead-store family).
// The = scalar ctors render as the mstringFrom* folds (the
// String-receiver class_member_carrier MISS precedent: the
// vendor data= assignment lands at the call site as the
// returned String); the (long) arm keeps the vendor
// %d-for-long quirk byte-identical (h009; the decimal text is
// byte-equal by construction).
//
// The mid-file Smith-Waterman region: the "shift in"/"shift
// out" section banners, the StopList stop file-scope var
// (ledger - the struct lives in libmpscpp.h, its arm render
// lands with its owner row), the extern-C cmpr, the Syn
// syn[SYNMAX]/synx DEFINITION site (the landed SynTable holder,
// SynInit.kt; one-owner, no second definition, cited), the
// #undef kill fossil, the sw banner (kept verbatim in the
// ledger below), the mid-file include block (banner ledger
// only, h004), MAX_ALIGNS/EMPTY_CELL defines, and the sw
// file-scope globals (the SwGlobals holder below; their
// consumers are the later batch rows).
//
// Quirks kept (ledgered):
//   - the vendor string-friend reversed-sign comparisons (the
//     header ledger above), byte-identical.
//   - the +=(mstring) no-assign return; the operator[] UB
//     idiom; the =(global) tpx-less read-through; the %ud
//     literal-d tails - all above.
//
// BOTH consults (find_rule before render, events logged):
//   h004 include_only - reused: the sw banner/include ledger.
//   h009 numeric_format - hit: the %ud/%hd/%ld/%g folds above.
//   h012 numeric_domain - reused: the unsigned arms are
//     same-body folds (the h021 Misc7 ledger); EMPTY_CELL is
//     the Short domain.
//   h013 dead_store - hit: the +=(mstring) no-assign quirk;
//     the operator[] UB idiom; the sx=0 initializer.
//   h014 out_of_scope_call - reused: the typed reads cite the
//     Misc5 seats; the store folds cite the shlG precedent;
//     the binary backends ride the NEW stubs below; cmpr cites
//     the SynInit Syn carrier.
//   h015 grammar_collision - reused: ASCII only, no backticks,
//     no raw comment dollars, trailing newline.
//   h017 naming - hit: globalLe/globalEq/globalNe free friend
//     families and GlobalRef.globalLe/Eq/Ne members (the landed
//     globalLt/globalGt precedent); assignG for the = family;
//     the mstring folds per the String-receiver family
//     (mstringFrom*/mstringBracket*/addAssign family on the
//     String receiver). Journaled.
//   h018 preprocessor_conditional - reused: the operator[]
//     pointer-UB inert fold (no JVM pointer-arithmetic
//     carrier).
//   h019 class_member_carrier - reused: the GlobalRef receiver
//     idiom; the SwGlobals holder (the SqlRuntime precedent).

private const val M3_GCVT_DIGITS = 15 // the =(double/float) width

// -------------------------------------------------------------------------
// operator<=(scalar, global) - the friend <= family (the orphan
// head is operator<=(short,global); rendered whole here).

internal fun globalLeR(a: Short, b: GlobalRef): Int =
    if (a <= b.Int()) 1 else 0

internal fun globalLeR(a: UShort, b: GlobalRef): Int {
    val i = b.Int()
    return if (a.toInt() <= i) 1 else 0
}

internal fun globalLeR(a: Long, b: GlobalRef): Int {
    val i = atolC(b.extractValue())
    return if (a <= i) 1 else 0
}

internal fun globalLeR(a: ULong, b: GlobalRef): Int {
    val i = atolC(b.extractValue())
    return if (a <= i.toULong()) 1 else 0
}

internal fun globalLeR(a: Float, b: GlobalRef): Int =
    if (a <= b.Double().toFloat()) 1 else 0

internal fun globalLeR(a: Double, b: GlobalRef): Int =
    if (a <= b.Double()) 1 else 0

// int operator<=(char * a,global b)
internal fun globalLeR(a: String, b: GlobalRef): Int =
    if (a.compareTo(b.extractValue()) <= 0) 1 else 0

// int operator<=(string a,global b) - VENDOR SIGN QUIRK: the
// string twin compares with >=0 (byte-identical keep, header
// ledger); NOT the char* twin's direction.
internal fun globalLeStrM3(a: String, b: GlobalRef): Int =
    if (a.compareTo(b.extractValue()) >= 0) 1 else 0

// -------------------------------------------------------------------------
// global::operator== ladder - the member forms.

internal fun GlobalRef.globalEq(a: GlobalRef): Int =
    if (extractValue() == a.extractValue()) 1 else 0

internal fun GlobalRef.globalEq(a: Int): Int = if (Int() == a) 1 else 0

internal fun GlobalRef.globalEqU(a: UInt): Int {
    // the unsigned arm is a same-body fold onto the Int seat (h021)
    return if (Int().toUInt() == a) 1 else 0
}

internal fun GlobalRef.globalEqS(a: Short): Int = if (Int() == a.toInt()) 1 else 0

internal fun GlobalRef.globalEqUS(a: UShort): Int = if (Int() == a.toInt()) 1 else 0

internal fun GlobalRef.globalEq(a: Long): Int =
    if (atolC(extractValue()) == a) 1 else 0

internal fun GlobalRef.globalEq(a: ULong): Int =
    if (atolC(extractValue()).toULong() == a) 1 else 0

internal fun GlobalRef.globalEq(a: Float): Int = if (Double() == a.toDouble()) 1 else 0

internal fun GlobalRef.globalEq(a: Double): Int = if (Double() == a) 1 else 0

// int global::operator==(char * a) / (string a) - the String
// seat fold (h015 x2)
internal fun GlobalRef.globalEq(a: String): Int = if (extractValue() == a) 1 else 0

// -------------------------------------------------------------------------
// friends: operator==(scalar, global)

internal fun globalEqR(a: Int, b: GlobalRef): Int = if (a == b.Int()) 1 else 0

internal fun globalEqRU(a: UInt, b: GlobalRef): Int = if (a.toInt() == b.Int()) 1 else 0

internal fun globalEqRS(a: Short, b: GlobalRef): Int = if (a.toInt() == b.Int()) 1 else 0

internal fun globalEqRUS(a: UShort, b: GlobalRef): Int = if (a.toInt() == b.Int()) 1 else 0

internal fun globalEqRL(a: Long, b: GlobalRef): Int = if (a == atolC(b.extractValue())) 1 else 0

internal fun globalEqRUL(a: ULong, b: GlobalRef): Int =
    if (a == atolC(b.extractValue()).toULong()) 1 else 0

internal fun globalEqRF(a: Float, b: GlobalRef): Int = if (a == b.Double().toFloat()) 1 else 0

internal fun globalEqRD(a: Double, b: GlobalRef): Int = if (a == b.Double()) 1 else 0

// int operator==(char * a,global b)
internal fun globalEqR(a: String, b: GlobalRef): Int = if (a == b.extractValue()) 1 else 0

// int operator==(string a,global b) - VENDOR SIGN QUIRK (the
// string friend form reads >=0 here too - byte-identical keep,
// header ledger)
internal fun globalEqStrM3(a: String, b: GlobalRef): Int =
    if (a.compareTo(b.extractValue()) >= 0) 1 else 0

// -------------------------------------------------------------------------
// global::operator!= ladder + friends (the != family).

internal fun GlobalRef.globalNe(a: GlobalRef): Int =
    if (extractValue() != a.extractValue()) 1 else 0

internal fun GlobalRef.globalNe(a: Int): Int = if (Int() != a) 1 else 0

internal fun GlobalRef.globalNeU(a: UInt): Int = if (Int().toUInt() != a) 1 else 0

internal fun GlobalRef.globalNeS(a: Short): Int = if (Int() != a.toInt()) 1 else 0

internal fun GlobalRef.globalNeUS(a: UShort): Int = if (Int() != a.toInt()) 1 else 0

internal fun GlobalRef.globalNe(a: Long): Int = if (atolC(extractValue()) != a) 1 else 0

internal fun GlobalRef.globalNe(a: ULong): Int =
    if (atolC(extractValue()).toULong() != a) 1 else 0

internal fun GlobalRef.globalNe(a: Float): Int = if (Double() != a.toDouble()) 1 else 0

internal fun GlobalRef.globalNe(a: Double): Int = if (Double() != a) 1 else 0

internal fun GlobalRef.globalNe(a: String): Int = if (extractValue() != a) 1 else 0

internal fun globalNeR(a: Int, b: GlobalRef): Int = if (a != b.Int()) 1 else 0

internal fun globalNeRU(a: UInt, b: GlobalRef): Int = if (a.toInt() != b.Int()) 1 else 0

internal fun globalNeRS(a: Short, b: GlobalRef): Int = if (a.toInt() != b.Int()) 1 else 0

internal fun globalNeRUS(a: UShort, b: GlobalRef): Int = if (a.toInt() != b.Int()) 1 else 0

internal fun globalNeRL(a: Long, b: GlobalRef): Int = if (a != atolC(b.extractValue())) 1 else 0

internal fun globalNeRUL(a: ULong, b: GlobalRef): Int =
    if (a != atolC(b.extractValue()).toULong()) 1 else 0

internal fun globalNeRF(a: Float, b: GlobalRef): Int = if (a != b.Double().toFloat()) 1 else 0

internal fun globalNeRD(a: Double, b: GlobalRef): Int = if (a != b.Double()) 1 else 0

internal fun globalNeR(a: String, b: GlobalRef): Int = if (a != b.extractValue()) 1 else 0

// int operator!=(string a,global b) - VENDOR SIGN QUIRK (the
// >=0 read, byte-identical keep, header ledger)
internal fun globalNeStrM3(a: String, b: GlobalRef): Int =
    if (a.compareTo(b.extractValue()) >= 0) 1 else 0

// -------------------------------------------------------------------------
// global & global::operator=(T s) - the write-assign family
// (the set_global macro fold of the shlG precedent; returns
// the global reference itself).

private fun setGlobalC3(g: GlobalRef, s: String): GlobalRef {
    // set_global(ref,s): tpx = Mglobal(STORE, ...)
    val buf = CharArray(STR_MAX)
    writeC(buf, s)
    svPtr.tpx = Mglobal(STORE, g.ref.toCharArray(), buf, svPtr).toLong()
    return g
}

// =(const char *) / =(string) / =(mstring) - the String seat
// fold (h015 x3)
internal fun GlobalRef.assignG(s: String): GlobalRef = setGlobalC3(this, s)

// =(int s) - sprintf(tmp,"%d",s)
internal fun GlobalRef.assignG(s: Int): GlobalRef = setGlobalC3(this, s.toString())

// =(double s) - gcvt_wrapper(s,15,tmp): the %g precision-15
// fold (h009; the NUL rides the sprintf semantics)
internal fun GlobalRef.assignG(s: Double): GlobalRef =
    setGlobalC3(this, cFormatG(s, M3_GCVT_DIGITS))

// =(global s) - read-through WITHOUT the tpx probe (the shlG
// twin HAS the probe; kept verbatim, header ledger)
internal fun GlobalRef.assignG(s: GlobalRef): GlobalRef {
    val tmp1 = CharArray(STR_MAX)
    svPtr.tpx = Mglobal(RETRIEVE, s.ref.toCharArray(), tmp1, svPtr).toLong()
    return setGlobalC3(this, cStringOf(tmp1))
}

// =(unsigned int s) - %ud: the %u lead + literal "d" tail
internal fun GlobalRef.assignG(s: UInt): GlobalRef = setGlobalC3(this, s.toString() + "d")

// =(float s) - gcvt_wrapper(s,15,tmp) on the promoted float
internal fun GlobalRef.assignG(s: Float): GlobalRef =
    setGlobalC3(this, cFormatG(s.toDouble(), M3_GCVT_DIGITS))

// =(short s) - %hd
internal fun GlobalRef.assignG(s: Short): GlobalRef = setGlobalC3(this, s.toString())

// =(unsigned short s) - %uhd: the %uh lead + literal "d" tail
internal fun GlobalRef.assignG(s: UShort): GlobalRef = setGlobalC3(this, s.toString() + "d")

// =(long s) - %ld
internal fun GlobalRef.assignG(s: Long): GlobalRef = setGlobalC3(this, s.toString())

// =(unsigned long s) - %lud: the %lu lead + literal "d" tail
internal fun GlobalRef.assignG(s: ULong): GlobalRef = setGlobalC3(this, s.toString() + "d")

// -------------------------------------------------------------------------
// The mid-file Smith-Waterman region (h004 banner/globals
// ledger; the consumers are later batch rows).
// -------------------------------------------------------------------------

// //=========== shift in ============================================
// //================ shift out =======================================
// (the empty section banners, verbatim)

// StopList stop; - the file-scope var names the StopList
// carrier (the struct lives in libmpscpp.h; its arm render
// lands with its owner row) - ledger, not rendered here.

// extern "C" int cmpr(const char *a, const Syn *b)
internal fun cmpr(a: String, b: Syn): Int {
    // return strcmp((const char *) a, (*b).word);
    return a.compareTo(b.word)
}

// Syn syn[SYNMAX]; long synx; - the DEFINITION site of the
// landed SynTable holder (SynInit.kt; one-owner rule, no
// second definition; cited).

// #undef kill  // also used in signal.h (the fossil, verbatim)

// The sw banner (kept verbatim in this ledger):
// IMPORTANT: sequences MUST have a blank in array position 0;
// to invoke: int sw (char *, char *, int, int, int, int, int);
// score = sw(seq1, seq2 [,flg1, flg2, gap, mismatch, match]);
// gap penalty default -1; mismatch penalty default -1; match
// reward default 2 (the full vendor banner preserved in the
// journal fragment).

// #include <iostream> <stdio.h> <time.h> <signal.h> <string.h>
// <stdlib.h>; using namespace std; - the mid-file include
// block rides the h004 banner ledger (arm-wide io carriers).

// #define MAX_ALIGNS 5000000
private const val MAX_ALIGNS_M3 = 5000000

// #define EMPTY_CELL -32768 (short domain, h012)
private const val EMPTY_CELL_M3: Short = -32768

// short **__a; string S_RESULT; int Size; int Size_s; int
// Size_t; int GAP; int MISMATCH; int MATCH; int sx=0; - the sw
// file-scope globals (the SwGlobals holder; consumers are the
// later batch rows; the short** matrix rides the row-of-rows
// shape)
internal object SwGlobals {
    var a: Array<ShortArray>? = null // short **__a
    var S_RESULT: String = ""
    var Size = 0
    var Size_s = 0
    var Size_t = 0
    var GAP = 0 // gap penalty
    var MISMATCH = 0 // mis-match penalty
    var MATCH = 0 // match reward
    var sx = 0
}

// -------------------------------------------------------------------------
// mstring constructors block (operator[] x4, the assign-operator
// families, the scalar = ctors). The String receiver carries
// the vendor data member (the class_member_carrier MISS
// precedent; the mutation lands as the returned String).
// -------------------------------------------------------------------------

// mstring & mstring::operator[](int i) - the *(this+i) pointer
// idiom (UB in the vendor): no JVM pointer-arithmetic carrier;
// the arms fold inert keeping the body ledgers (h012/h018
// shape). x4: (int)/(mstring)/(const char*)/(string) - the
// atoi forms coerce then fold identically.
internal fun String.mstringBracket(i: Int): String {
    // return *(this+i);
    return this
}

internal fun String.mstringBracket(x: String): String {
    // int i; i=atoi(x.c_str()); return *(this+i);
    return this
}

// the mstring binary backends (h014 DELEGATE FLIP at the B10
// close: the owner units 304/306 landed; these seats now route to
// the real renders in LibmpscppMisc4.kt / LibmpscppMisc8.kt):
// the +=/-=/*=///=/%= families seat through them.
internal fun mstringPlusZb(a: String, b: String): String {
    return mstringPlus(a, b) // owner roster 304 (LibmpscppMisc4.kt)
}

internal fun mstringMinusZb(a: String, b: String): String {
    return mstringMinus(a, b) // owner roster 304 (LibmpscppMisc4.kt)
}

internal fun mstringTimesZb(a: String, b: String): String {
    return mstringTimes(a, b) // owner roster 306 (LibmpscppMisc8.kt)
}

internal fun mstringDivZb(a: String, b: String): String {
    return mstringDiv(a, b) // owner roster 304 (LibmpscppMisc4.kt)
}

internal fun mstringRemZb(a: String, b: String): String {
    return mstringMod(a, b) // owner roster 304 (LibmpscppMisc4.kt)
}

// mstring mstring::operator+=(mstring i) - VENDOR QUIRK: the
// body returns *this+i WITHOUT assigning (*this) (h013 note);
// the fold returns the unassigned sum (the call-site
// reassignment is the += discipline of the String carrier).
internal fun String.addAssignM3(i: String): String {
    // return *this+i;
    return mstringPlusZb(this, i)
}

internal fun String.addAssignM3(i: Int): String = mstringPlusZb(this, i.toString())

internal fun String.addAssignM3(i: Long): String = mstringPlusZb(this, i.toString())

internal fun String.addAssignM3(i: Double): String = mstringPlusZb(this, cFormatG(i, 6))

internal fun String.addAssignM3(i: Float): String = mstringPlusZb(this, cFormatG(i.toDouble(), 6))

// (string)/(const char*) - the String seat fold (h015 x2)
internal fun String.addAssignS3(i: String): String = mstringPlusZb(this, i)

internal fun String.addAssignG3(i: GlobalRef): String =
    mstringPlusZb(this, i.extractValue())

// mstring operator-= family (x8 - the same arm roster)
internal fun String.subAssignM3(i: String): String = mstringMinusZb(this, i)

internal fun String.subAssignM3(i: Int): String = mstringMinusZb(this, i.toString())

internal fun String.subAssignM3(i: Long): String = mstringMinusZb(this, i.toString())

internal fun String.subAssignM3(i: Double): String = mstringMinusZb(this, cFormatG(i, 6))

internal fun String.subAssignM3(i: Float): String = mstringMinusZb(this, cFormatG(i.toDouble(), 6))

internal fun String.subAssignS3(i: String): String = mstringMinusZb(this, i)

internal fun String.subAssignG3(i: GlobalRef): String =
    mstringMinusZb(this, i.extractValue())

// mstring operator*= family (x8)
internal fun String.mulAssignM3(i: String): String = mstringTimesZb(this, i)

internal fun String.mulAssignM3(i: Int): String = mstringTimesZb(this, i.toString())

internal fun String.mulAssignM3(i: Long): String = mstringTimesZb(this, i.toString())

internal fun String.mulAssignM3(i: Double): String = mstringTimesZb(this, cFormatG(i, 6))

internal fun String.mulAssignM3(i: Float): String = mstringTimesZb(this, cFormatG(i.toDouble(), 6))

internal fun String.mulAssignS3(i: String): String = mstringTimesZb(this, i)

internal fun String.mulAssignG3(i: GlobalRef): String =
    mstringTimesZb(this, i.extractValue())

// mstring operator/= family (x8)
internal fun String.divAssignM3(i: String): String = mstringDivZb(this, i)

internal fun String.divAssignM3(i: Int): String = mstringDivZb(this, i.toString())

internal fun String.divAssignM3(i: Long): String = mstringDivZb(this, i.toString())

internal fun String.divAssignM3(i: Double): String = mstringDivZb(this, cFormatG(i, 6))

internal fun String.divAssignM3(i: Float): String = mstringDivZb(this, cFormatG(i.toDouble(), 6))

internal fun String.divAssignS3(i: String): String = mstringDivZb(this, i)

internal fun String.divAssignG3(i: GlobalRef): String =
    mstringDivZb(this, i.extractValue())

// mstring operator%= family (x6: mstring/int/long/string/
// const char*/global)
internal fun String.remAssignM3(i: String): String = mstringRemZb(this, i)

internal fun String.remAssignM3(i: Int): String = mstringRemZb(this, i.toString())

internal fun String.remAssignM3(i: Long): String = mstringRemZb(this, i.toString())

internal fun String.remAssignS3(i: String): String = mstringRemZb(this, i)

internal fun String.remAssignG3(i: GlobalRef): String =
    mstringRemZb(this, i.extractValue())

// mstring& mstring::operator=(T i) - the scalar = ctors (the
// mstringFrom* folds; the String-receiver assignment lands at
// the call site as the returned String). The char*/string arms
// ARE the identity (h015 fold, no second definition); the
// (long) arm keeps the vendor %d-for-long quirk byte-identical
// (h009; the decimal text is byte-equal by construction).

// =(int i): char tmp[32]=""; sprintf(tmp,"%d",i); data=tmp;
internal fun mstringFromInt(i: Int): String = i.toString()

// =(long i) - the %d quirk (see header)
internal fun mstringFromLong(i: Long): String = i.toString()

// =(double i) - %g (h009; the gcvt shortest-form family)
internal fun mstringFromDouble(i: Double): String = cFormatG(i, 6)

// =(float i) - %g on the promoted float
internal fun mstringFromFloat(i: Float): String = cFormatG(i.toDouble(), 6)

// =(mstring i) - data=i.data (the identity fold, cited)
