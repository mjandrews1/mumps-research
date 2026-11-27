package both

// Unit 120 (BOTH batch 4, complex stratum):
// libmpscpp.cpp::<misc-9> (units.csv row 98: loc 342, ptr 19,
// ctrl 51, score 161.2, rank 284; physical LOC 342 verified
// this session against the vendor chunk replay) - the ninth
// libmpscpp residual chunk. RENDERED at the 2026-11-27 PI
// adjudication (ruling 3: the misc-9 angle families rendered
// now in a follow-up BOTH tranche; the B10 close paragraph's
// "DEFERRED" note is superseded by that ruling).
//
// CHUNK-BOUNDARY DISCLOSURE (the B4 misc-5 adjudication style):
// the dump opens mid-body ("tmp=t1; return tmp; }" - the tail
// of operator*(float,mstring) at 9412, the chunk's
// boundary-cut face RENDERED WHOLE at the misc-8 owner as
// mstringTimesRev(Float); the leading tail stays with the
// misc-8 owner, not re-rendered here).
//
// COVERED-ELSEWHERE LEDGER (chunk members rendered at their own
// owners; cross-cited, not re-rendered):
// - char * Sim1(...) 10431 - sim1FaceZb (Sim1.kt, the roster-198
//   dual-seat adjudication).
// - char * ShredQuery(...) 10570 - ShredQuery (BifsZfcn.kt:785,
//   the thin forward onto the unit-182 Shred seat).
// - char * SynLookup(...) 10520 - the synLookupZb stub
//   (BifsZfcn.kt:779) STAYS: its callee is the SYN core
//   (non-roster cross-TU helper), parked with the C-001
//   backfill population.
// - the StopLookup Zfcn dispatch arms (BifsZfcn.kt:1122,
//   ZfcnsZfcn.kt:1061) carry the "1"/"0" return shape of the
//   10504 face; the char* face itself IS rendered here
//   (stopLookupFaceZb) as a chunk member.
//
// Consults (rule base wp2/arms/BOTH/rules_v0.pl):
//   h007 string_carrier - reused and hit: the mstring class
//     folds to String throughout (the roster-149 layout owner
//     has not landed in this arm); the operator const char*()
//     conversion at 9840 IS the carrier identity (rendered as
//     the explicit mstringCStrZb seat below).
//   h009 format_string - reused: the faces' sprintf("%g") rides
//     fmtG (cFormatG 6-digit family, the sim1FaceZb/mcvt
//     precedent); sprintf("%d") rides toString.
//   h013 dead_store - hit: the wrapper faces' svptr/ep/i/j
//     parameters and locals are unread (kept as the signature
//     ledger); BMGSearch's strlen(arg2+2) malloc quirk (the
//     +2 counts the NUL the vendor never accounts for)
//     ledgered at the seat.
//   h014 out_of_scope_call - reused and hit: mult/sym_/Mglobal/
//     MakeGlobal/getGlobalC/bmgFullsearch and the kernel
//     seats (Cosine/Jaccard/Dice/Shred/StopInit/SynInit/
//     stopLookupC) direct; the two PENDING callee members
//     (global::TermCorrelate roster 14 batch 1, global::
//     Centroid roster 112 batch 4 - the InterpService C-015
//     pins) raise named-owner TODO stubs at the seats below.
//   h017 naming - reused: mstringLt/mstringGt (the mstringGe
//     precedent, LibmpscppMisc4.kt), xxFaceZb faces (the
//     sim1FaceZb precedent), symputM9/symGetM9 carriers.
//   h019 class_member_carrier - reused: global-class members
//     (flushZb, the TermCorrelate/Centroid stubs) ride the
//     GlobalRef receiver state explicitly (layout owner
//     roster 89 untranslated).
//
// Dafny: EXEMPT (all B4 units EXEMPT per the DAF B6 T3
// precedent; no new spec).

// ------------------------------------------------------------------------
// mstring operator* - the friend family (reversed; the misc-8
// continuation the 306 header deferred to this owner).
// ------------------------------------------------------------------------

// mstring operator*(string i, mstring k) - libmpscpp.cpp:9420
internal fun mstringTimesRev(i: String, k: String): String {
    val t1 = CharArray(MS_SMALL_M9)
    mult(carrM9(i), carrM9(k), t1)
    return cStringOf(t1)
}

// mstring operator*(global i, mstring k) - 9428: the i >> t2
// extract (the operator>> throw-on-missing fold rides
// extractValue), then the same mult kernel.
internal fun mstringTimesRev(i: GlobalRef, k: String): String {
    val t2 = i.extractValue()
    val t1 = CharArray(MS_SMALL_M9)
    mult(carrM9(t2), carrM9(k), t1)
    return cStringOf(t1)
}

// mstring operator*(const char * i, mstring k) - 9438: folded
// with the 9420 String twin (identical body after the h007
// String carrier).

// ------------------------------------------------------------------------
// bool mstring::operator< - the family (9504-9559). Numeric
// faces: long j=atol(data) for int/long, double j=atof(data)
// for double/float (the strtodC atof fold, BothNumRuntime
// carrier; the float promotes at the compare edge).
// ------------------------------------------------------------------------

internal fun mstringLt(data: String, i: Int): Boolean =
    atolC(data) < i.toLong()

internal fun mstringLt(data: String, i: Long): Boolean =
    atolC(data) < i

internal fun mstringLt(data: String, i: Double): Boolean =
    strtodC(data) < i

internal fun mstringLt(data: String, i: Float): Boolean =
    strtodC(data) < i.toDouble()

// operator<(char * i) 9532 / (const char * i) 9537 / (string i)
// 9542 / (mstring i) 9555 - folded (the std::string
// lexicographic data<i comparison folds to Kotlin String
// comparison; the mstring twin carries i.data).
internal fun mstringLt(data: String, i: String): Boolean =
    data < i

// operator<(global i) - 9547: the i >> t1 extract then the
// compare.
internal fun mstringLt(data: String, i: GlobalRef): Boolean =
    data < i.extractValue()

// ------------------------------------------------------------------------
// bool mstring::operator> - the family (9560-9615); the same
// face shapes as the < family, the compare edge flipped.
// ------------------------------------------------------------------------

internal fun mstringGt(data: String, i: Int): Boolean =
    atolC(data) > i.toLong()

internal fun mstringGt(data: String, i: Long): Boolean =
    atolC(data) > i

internal fun mstringGt(data: String, i: Double): Boolean =
    strtodC(data) > i

internal fun mstringGt(data: String, i: Float): Boolean =
    strtodC(data) > i.toDouble()

// operator>(char * i) 9592 / (const char * i) 9597 / (string i)
// 9602 / (mstring i) 9611 - folded.
internal fun mstringGt(data: String, i: String): Boolean =
    data > i

// operator>(global i) - 9607.
internal fun mstringGt(data: String, i: GlobalRef): Boolean =
    data > i.extractValue()

// ------------------------------------------------------------------------
// mstring::operator const char *() - 9840: the conversion IS the
// h007 String carrier (explicit identity seat; the call sites
// bind the carrier direct).
// ------------------------------------------------------------------------
internal fun mstringCStrZb(data: String): String = data

// ------------------------------------------------------------------------
// SymGet / SymPut / __symput (9844-9953)
// ------------------------------------------------------------------------

// the shared sym_(11) retrieve (the 9844 mstring / 9858 char *
// bodies are identical after the String carrier - folded).
internal fun symGetM9(str: String): String {
    val tmp = CharArray(STR_MAX)
    val p1 = sym_(11, str, tmp, svPtr)
    if (p1 == null) {
        GlobalExceptionCode = SYMTAB_ERROR
        throw MumpsGlobalException()
    }
    return cStringOf(tmp)
}

// mstring SymGet(global &str) - 9872: the str >> tmp2 extract
// then the same retrieve.
internal fun SymGet(str: GlobalRef): String =
    symGetM9(str.extractValue())

// void __symput(const char *, const char *) - 9943: the sym_(0)
// store with the SYMTAB_ERROR throw on the NULL return.
internal fun symputM9(str: String, value: String) {
    val v = CharArray(STR_MAX)
    writeC(v, value)
    val p1 = sym_(0, str, v, svPtr)
    if (p1 == null) {
        GlobalExceptionCode = SYMTAB_ERROR
        throw MumpsGlobalException()
    }
}

// The nine SymPut bodies fold to four carrier shapes (the
// mstring/char * name and value twins are identical after the
// h007 String carrier; the global operands ride the operator>>
// extract):
//   (mstring,mstring) 9887 / (mstring,char *) 9899 /
//   (char *,mstring) 9904 / (char *,char *) 9916 - the
//   (String,String) fold;
//   (mstring,global) 9892 - (String,GlobalRef);
//   (global,char *) 9921 / (global,mstring) 9928 -
//   (GlobalRef,String);
//   (global,global) 9935 - (GlobalRef,GlobalRef).
internal fun SymPut(name: String, value: String): Boolean {
    symputM9(name, value)
    return true
}

internal fun SymPut(name: String, value: GlobalRef): Boolean {
    symputM9(name, value.extractValue())
    return true
}

internal fun SymPut(name: GlobalRef, value: String): Boolean {
    symputM9(name.extractValue(), value)
    return true
}

internal fun SymPut(name: GlobalRef, value: GlobalRef): Boolean {
    symputM9(name.extractValue(), value.extractValue())
    return true
}

// ------------------------------------------------------------------------
// void global::Flush() - 9955: the Mglobal(FLUSH,...) flush with
// the two 16-byte "" scratch buffers.
// ------------------------------------------------------------------------
internal fun GlobalRef.flushZb() {
    val t1 = CharArray(16)
    val t2 = CharArray(16)
    Mglobal(FLUSH, t1, t2, svPtr)
}

// ------------------------------------------------------------------------
// The builtin-table faces (9962-10576; the Sim1/ShredQuery/
// SynLookup dispositions in the header ledger). The arg1/arg2
// name/subscript split is byte-exact per the sim1FaceZb
// precedent: truncate at the first 0x01, bypass ^ with &arg[1],
// subscript-paste &arg[i+1] via the roster-202 MakeGlobal
// operator() fold.
// ------------------------------------------------------------------------

// char * Cosine(struct MSV*, char *, char *, char *) - 9962.
// CALLEE PIN: the Cosine(A,B) kernel is the parked roster-85
// stub (BothStubs.kt; batch 3, ahead of the committed order) -
// the face renders whole and binds the stub direct.
internal fun cosineFaceZb(sv: Msv, ep: String, arg1: String, arg2: String): String {
    var i = 0
    while (i < arg1.length && arg1[i] != '\u0000') {
        if (arg1[i] == '\u0001') break
        i++
    }
    val nameA = arg1.substring(1, i) // global a(&arg1[1]) - bypass ^

    var j = 0
    while (j < arg2.length && arg2[j] != '\u0000') {
        if (arg2[j] == '\u0001') break
        j++
    }
    val nameB = arg2.substring(1, j)

    val a = MakeGlobal(0, nameA, "a", listOf(arg1.substring(i + 1)))
    val b = MakeGlobal(0, nameB, "a", listOf(arg2.substring(j + 1)))

    val x = Cosine(a, b)
    return fmtG(x) // sprintf(tmp,"%g",x); return tmp;
}

// char * TermCorrelate(struct MSV*, char *, char *, char *) -
// 10390. CALLEE: global::TermCorrelate is roster 14 batch 1
// (the InterpService C-015 pin) - the named-owner stub below.
internal fun termCorrelateFaceZb(sv: Msv, ep: String, arg1: String, arg2: String): String {
    var i = 0
    while (i < arg1.length && arg1[i] != '\u0000') {
        if (arg1[i] == '\u0001') break
        i++
    }
    val nameA = arg1.substring(1, i)

    var j = 0
    while (j < arg2.length && arg2[j] != '\u0000') {
        if (arg2[j] == '\u0001') break
        j++
    }
    val nameB = arg2.substring(1, j)

    val a = MakeGlobal(0, nameA, "a", listOf(arg1.substring(i + 1)))
    val b = MakeGlobal(0, nameB, "a", listOf(arg2.substring(j + 1)))

    a.termCorrelateZb(b)
    return ""
}

// char * DocCorrelate(struct MSV*, char *, char *, char *,
// char *, char *) - 10409. CALLEE: the global::DocCorrelate
// member is the RELEASED roster-299 render (BothStubs.kt:278,
// the B10 delegate flip).
internal fun docCorrelateFaceZb(
    sv: Msv, ep: String, arg1: String, arg2: String, arg3: String, arg4: String
): String {
    var i = 0
    while (i < arg1.length && arg1[i] != '\u0000') {
        if (arg1[i] == '\u0001') break
        i++
    }
    val nameA = arg1.substring(1, i)

    var j = 0
    while (j < arg2.length && arg2[j] != '\u0000') {
        if (arg2[j] == '\u0001') break
        j++
    }
    val nameB = arg2.substring(1, j)

    // sscanf(arg4,"%lf",&x)
    val x = strtodC(arg4)

    val a = MakeGlobal(0, nameA, "a", listOf(arg1.substring(i + 1)))
    val b = MakeGlobal(0, nameB, "a", listOf(arg2.substring(j + 1)))

    a.docCorrelate(b, arg3, x)
    return ""
}

// char * Jaccard(struct MSV*, char *, char *, char *) - 10453;
// the Jaccard kernel seat (Jaccard.kt).
internal fun jaccardFaceZb(sv: Msv, ep: String, arg1: String, arg2: String): String {
    var i = 0
    while (i < arg1.length && arg1[i] != '\u0000') {
        if (arg1[i] == '\u0001') break
        i++
    }
    val nameA = arg1.substring(1, i)

    var j = 0
    while (j < arg2.length && arg2[j] != '\u0000') {
        if (arg2[j] == '\u0001') break
        j++
    }
    val nameB = arg2.substring(1, j)

    val a = MakeGlobal(0, nameA, "a", listOf(arg1.substring(i + 1)))
    val b = MakeGlobal(0, nameB, "a", listOf(arg2.substring(j + 1)))

    val x = Jaccard(a, b)
    return fmtG(x)
}

// char * Dice(struct MSV*, char *, char *, char *) - 10475; the
// Dice kernel seat (Dice.kt).
internal fun diceFaceZb(sv: Msv, ep: String, arg1: String, arg2: String): String {
    var i = 0
    while (i < arg1.length && arg1[i] != '\u0000') {
        if (arg1[i] == '\u0001') break
        i++
    }
    val nameA = arg1.substring(1, i)

    var j = 0
    while (j < arg2.length && arg2[j] != '\u0000') {
        if (arg2[j] == '\u0001') break
        j++
    }
    val nameB = arg2.substring(1, j)

    val a = MakeGlobal(0, nameA, "a", listOf(arg1.substring(i + 1)))
    val b = MakeGlobal(0, nameB, "a", listOf(arg2.substring(j + 1)))

    val x = Dice(a, b)
    return fmtG(x)
}

// char * StopInit(struct MSV*, char *, char *) - 10497; the
// StopInit(file) core seat (StopInit.kt).
internal fun stopInitFaceZb(sv: Msv, ep: String, arg1: String): String {
    StopInit(arg1)
    return ""
}

// char * StopLookup(struct MSV*, char *, char *) - 10504; the
// int StopLookup(char *) core is the released stopLookupC
// membership face (LibmpscppMisc8.kt:448).
internal fun stopLookupFaceZb(sv: Msv, ep: String, arg1: String): String {
    if (stopLookupC(arg1)) return "1"
    return "0"
}

// char * SynInit(struct MSV*, char *, char *) - 10513; the
// SynInit(file) core seat (SynInit.kt).
internal fun synInitFaceZb(sv: Msv, ep: String, arg1: String): String {
    SynInit(arg1)
    return ""
}

// char * Centroid(struct MSV*, char *, char *, char *) - 10526.
// CALLEE: global::Centroid is roster 112 batch 4 (the
// InterpService C-015 pin) - the named-owner stub below.
internal fun centroidFaceZb(sv: Msv, ep: String, arg1: String, arg2: String): String {
    var i = 0
    while (i < arg1.length && arg1[i] != '\u0000') {
        if (arg1[i] == '\u0001') break
        i++
    }
    val nameA = arg1.substring(1, i)

    var j = 0
    while (j < arg2.length && arg2[j] != '\u0000') {
        if (arg2[j] == '\u0001') break
        j++
    }
    val nameB = arg2.substring(1, j)

    val a = MakeGlobal(0, nameA, "a", listOf(arg1.substring(i + 1)))
    val b = MakeGlobal(0, nameB, "a", listOf(arg2.substring(j + 1)))

    a.centroidZb(b)
    return ""
}

// char * BMGSearch(struct MSV*, char *, char *, char *) - 10547:
// the malloc(strlen(arg2+2))/strcpy/strcat(p1," ") triple folds
// onto the String concat (the +2-vs-+1 vendor malloc quirk
// ledgered, h013); bmg_fullsearch is the unit-bmgsubs seat
// (InterpBmgsubsMisc.kt).
internal fun bmgSearchFaceZb(sv: Msv, ep: String, arg1: String, arg2: String): String {
    val p1 = arg2 + " "
    val i = bmgFullsearch(arg1, p1)
    return i.toString() // sprintf(tmp,"%d",i); return tmp;
}

// char * Shred(struct MSV*, char *, char *, char *) - 10562; the
// atoi fold onto the unit-182 Shred(str,size) seat.
internal fun shredFaceZb(sv: Msv, ep: String, arg1: String, arg2: String): String {
    return Shred(arg1, atolC(arg2).toInt())
}

// ------------------------------------------------------------------------
// Named-owner TODO stubs raised by this render (the h014
// pending-callee precedent; crosswalk-true pins per C-015).
// ------------------------------------------------------------------------

// mstring global::TermCorrelate(global &) - the 10390 face's
// callee. Owner: libmpscpp.cpp::global::TermCorrelate, roster
// 14, batch 1 (the InterpService termCorrelateSvcZb pin).
internal fun GlobalRef.termCorrelateZb(B: GlobalRef) {
    TODO("stub: owner libmpscpp.cpp::global::TermCorrelate (roster 14, batch 1)")
}

// void global::Centroid(global &) - the 10526 face's callee.
// Owner: libmpscpp.cpp::global::Centroid, roster 112, batch 4
// (the InterpService centroidSvcZb pin).
internal fun GlobalRef.centroidZb(B: GlobalRef) {
    TODO("stub: owner libmpscpp.cpp::global::Centroid (roster 112, batch 4)")
}

// ------------------------------------------------------------------------
// Carriers (private)
// ------------------------------------------------------------------------

private const val MS_SMALL_M9 = 64 // the vendor char t1[64]

// the String -> NUL-terminated CharArray copy (the carr8 fold)
private fun carrM9(s: String): CharArray {
    val a = CharArray(MS_SMALL_M9)
    var i = 0
    while (i < s.length && i < MS_SMALL_M9 - 1) {
        a[i] = s[i]
        i++
    }
    a[i] = '\u0000'
    return a
}
