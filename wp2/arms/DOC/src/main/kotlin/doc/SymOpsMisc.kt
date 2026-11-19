package doc

// Unit 120 (B4 tranche 3): libmpscpp.cpp::<misc-9> (batch 4, complex
// stratum; units.csv row 98: loc 342, ptr 19, ctrl 51, score 161.2,
// rank 284). The symbol and comparison tail of the class library:
// the non-member operator* x3 (string/global/char* times mstring via
// the mult seat), the mstring operator< x9 and operator> x9 ladders,
// the mstring const char* conversion witness, the SymGet x3 /
// SymPut x9 / __symput symbol-table family (sym_ seat, SYMTAB_ERROR
// throw per the arm convention), the global::Flush member wrapper,
// and the MSV-intrinsic wrapper battery x14 (Cosine, TermCorrelate,
// DocCorrelate, Sim1, Jaccard, Dice, StopInit, StopLookup, SynInit,
// SynLookup, Centroid, BMGSearch, Shred, ShredQuery).
//
// CHUNKER BOUNDARY (start): the chunk opens with the orphan
// tmp=t1; return-tmp tail of the mstring operator*(mstring,mstring)
// body whose opening lives in the previous chunk (misc-8 territory,
// an unrendered batch-10 chunk) - the orphan lines close a function
// this unit does not own and are LEDGERED HERE, NOT RENDERED (the
// unit-62 misc-2 opening-edge precedent). The chunk CLOSES cleanly
// (ShredQuery's final brace is the last line).
//
// Render decisions (consult rows logged 2026-11-19T20:53:08Z):
// the C operator* renders as Kotlin operator times per the unit-62
// word-operator convention (H-014 adapt), but the comparison ladders
// CANNOT: Kotlin admits one compareTo per receiver, so the 18
// consistent < / > pairs render as named lt / gt members (the
// unit-62 gte/lte precedent, H-014 adapt; the < / > and >= / <=
// families keep distinct names exactly as the vendor keeps distinct
// operators). The vendor char* and std::string overloads collapse
// onto one String seat where their bodies are identical (H-015,
// ledgered per member); the remaining distinct domains render
// per-type overloads (H-012): int and long both ride the C atol
// domain (the arm atollShared leading-integer seat), double and
// float both ride the arm C-protocol strtod (the C atof identity),
// with the float operand widened to double exactly as the C
// promotion. The mstring member field is the carrier str. mult and
// sym_ are H-014 stub seats; the Mglobal FLUSH=21 and libmpscpp
// symlen error SYMTAB_ERROR=9 consts are added to Carriers
// (EDIT-ONLY). The global >> string ingest idiom is the
// globalGetStrMax seat (unit-62 extraction precedent); static
// char tmp[32] wrapper returns fold to fresh String returns
// (H-008); sprintf percent-g / percent-d render as String.format
// Locale.US (H-009); the DocCorrelate sscanf percent-lf folds to
// the strtod parse (the sscanf-to-strtod identity, H-009 adjacent);
// the wrapper svptr and ep parameters are dead call-shape
// parameters kept for fidelity (H-013) and their unused i/j locals
// are not rendered (H-013); the wrappers keep the unit-99 Min/98
// Count soh-split shape (trailing caret bypass, in-place arg1/arg2
// nulling, keyed invoke seats) INCLUDING the data asymmetry that
// these wrappers have no j==NUL guard (the Min/Count quirk guard is
// NOT in this vendor family; kept as-is). The BMGSearch malloc
// window wart (strlen of the arg2+2 window sizing a buffer the
// strcpy then fills from arg2) folds to the String domain (H-008)
// with the wart ledgered - the String render cannot under-allocate
// and the observable search string is cstring(arg2) plus one space
// exactly as the vendor. bmg_fullsearch is the interp-bmgsubs.c free
// twin, NOT the unit-86 bmg_fsearch (H-015-style same-adjacent-name
// note); the two TermCorrelate/Centroid real members and the real
// Cosine(A,B) double are called directly (J-mirror asymmetry data
// kept). Naming per H-017 (mult, sym_, SYN kept verbatim).

// mstring operator*(string i, mstring k) - and the const char*
// overload, whose body is identical (H-015 collapse onto the
// String seat, ledgered). The char t1[64] quirk is kept (64, not
// STR_MAX - vendor buffer); the mstring tmp / tmp = t1 operator=
// (char*) assignment folds to the Mstring constructor (H-008).
operator fun String.times(k: Mstring): Mstring {
    val t1 = CharArray(64)
    val ia = CharArray(this.length + 1)
    CstringOps.strcpyS(ia, this)
    Stubs.mult(ia, k.str, t1)
    return Mstring(CstringOps.cstring(t1))
}

// mstring operator*(global i, mstring k): the i >> t2 global ingest
// is the globalGetStrMax seat before the same mult call.
operator fun GlobalRef.times(k: Mstring): Mstring {
    val t1 = CharArray(64)
    val t2 = Stubs.globalGetStrMax(this)
    val ia = CharArray(t2.length + 1)
    CstringOps.strcpyS(ia, t2)
    Stubs.mult(ia, k.str, t1)
    return Mstring(CstringOps.cstring(t1))
}

// ---------------- mstring operator< ladder (x9) ----------------

// int: long j = atol(data); return j < i with the int widened to
// long exactly as the C comparison.
fun Mstring.lt(i: Int): Boolean = atollShared(str) < i.toLong()

// long: same atol domain (H-012 distinct seat, identical body).
fun Mstring.lt(i: Long): Boolean = atollShared(str) < i

// double: double j = atof(data); C atof identity is strtod.
fun Mstring.lt(i: Double): Boolean = strtod(str) < i

// float: double j = atof(data); the float operand widens to double
// (the C promotion).
fun Mstring.lt(i: Float): Boolean = strtod(str) < i.toDouble()

// char* and std::string (H-015 collapse onto the String seat):
// data < i is the C++ string lexicographic comparison.
fun Mstring.lt(i: String): Boolean = str.compareTo(i) < 0

// global: the i >> t1 global ingest then the same lexicographic
// compare.
fun Mstring.lt(i: GlobalRef): Boolean =
    str.compareTo(Stubs.globalGetStrMax(i)) < 0

// mstring: data < i.data.
fun Mstring.lt(i: Mstring): Boolean = str.compareTo(i.str) < 0

// ---------------- mstring operator> ladder (x9) ----------------

fun Mstring.gt(i: Int): Boolean = atollShared(str) > i.toLong()

fun Mstring.gt(i: Long): Boolean = atollShared(str) > i

fun Mstring.gt(i: Double): Boolean = strtod(str) > i

fun Mstring.gt(i: Float): Boolean = strtod(str) > i.toDouble()

fun Mstring.gt(i: String): Boolean = str.compareTo(i) > 0

fun Mstring.gt(i: GlobalRef): Boolean =
    str.compareTo(Stubs.globalGetStrMax(i)) > 0

fun Mstring.gt(i: Mstring): Boolean = str.compareTo(i.str) > 0

// mstring::operator const char*(): no new code - the carrier
// c_str() seat IS the conversion witness (Carriers.kt; noted here
// because the conversion is chunk content, ledgered).

// ---------------- SymGet family (x3) ----------------

// mstring SymGet(mstring str): sym_(11) fetch; the NULL return maps
// to the SYMTAB_ERROR throw (the arm MumpsGlobalException
// convention, GlobalMin precedent).
fun SymGet(str: Mstring): Mstring {
    val name = CharArray(STR_MAX)
    CstringOps.strcpyS(name, str.str)
    return symGetFetch(name)
}

// mstring SymGet(char * str) (the String seat; H-012).
fun SymGet(str: String): Mstring {
    val name = CharArray(STR_MAX)
    CstringOps.strcpyS(name, str)
    return symGetFetch(name)
}

// mstring SymGet(global &str): the str >> tmp2 ingest first.
fun SymGet(str: GlobalRef): Mstring {
    val t2 = Stubs.globalGetStrMax(str)
    val tmp2 = CharArray(STR_MAX)
    CstringOps.strcpyS(tmp2, t2)
    return symGetFetch(tmp2)
}

// the shared fetch tail of the three SymGet overloads (the vendor
// repeats the body verbatim; the single Kotlin seat is the H-014
// reuse of the identical tail - both locals are vendor-verbatim).
private fun symGetFetch(name: CharArray): Mstring {
    val tmp = CharArray(STR_MAX)
    val p1 = Stubs.sym_(11, name, tmp, Globals.svPtr)

    if (p1 == null) {
        Globals.GlobalExceptionCode = SYMTAB_ERROR
        throw MumpsGlobalException()
    }

    return Mstring(CstringOps.cstring(tmp))
}

// ---------------- SymPut family (x8 kernel) ----------------

// bool SymPut(mstring &name, mstring &value)
fun SymPut(name: Mstring, value: Mstring): Boolean {
    __symput(name.str, value.str)
    return true
}

// bool SymPut(mstring &name, global &value): value >> tmp ingest.
fun SymPut(name: Mstring, value: GlobalRef): Boolean {
    __symput(name.str, Stubs.globalGetStrMax(value))
    return true
}

// bool SymPut(mstring &name, char *value) (String seat)
fun SymPut(name: Mstring, value: String): Boolean {
    __symput(name.str, value)
    return true
}

// bool SymPut(char *name, mstring &value)
fun SymPut(name: String, value: Mstring): Boolean {
    __symput(name, value.str)
    return true
}

// bool SymPut(char *name, global &value): name keeps the C c_str()
// of the mstring (the String seat) but the global ingests first.
fun SymPut(name: String, value: GlobalRef): Boolean {
    __symput(name, Stubs.globalGetStrMax(value))
    return true
}

// bool SymPut(char *name, char *value)
fun SymPut(name: String, value: String): Boolean {
    __symput(name, value)
    return true
}

// bool SymPut(global &name, char *value): name >> tmp ingest first.
fun SymPut(name: GlobalRef, value: String): Boolean {
    __symput(Stubs.globalGetStrMax(name), value)
    return true
}

// bool SymPut(global &name, mstring &value)
fun SymPut(name: GlobalRef, value: Mstring): Boolean {
    __symput(Stubs.globalGetStrMax(name), value.str)
    return true
}

// bool SymPut(global &name, global &value): both operands ingest.
fun SymPut(name: GlobalRef, value: GlobalRef): Boolean {
    __symput(Stubs.globalGetStrMax(name), Stubs.globalGetStrMax(value))
    return true
}

// void __symput(const char * str, const char * val): the sym_(0)
// store; the NULL-return throw mirrors the fetch tail.
fun __symput(str: String, value: String) {
    val name = CharArray(STR_MAX)
    CstringOps.strcpyS(name, str)
    val valArr = CharArray(STR_MAX)
    CstringOps.strcpyS(valArr, value)

    val p1 = Stubs.sym_(0, name, valArr, Globals.svPtr)

    if (p1 == null) {
        Globals.GlobalExceptionCode = SYMTAB_ERROR
        throw MumpsGlobalException()
    }
}

// void global::Flush(): the t1/t2 16-scratch Mglobal(FLUSH) call;
// the vendor 16-byte empties are the H-013 dead-role carriers kept.
fun GlobalRef.Flush() {
    val t1 = CharArray(16)
    val t2 = CharArray(16)
    Stubs.Mglobal(FLUSH, t1, t2, Globals.svPtr)
}

// ---------------- the MSV-intrinsic wrapper battery (x14) ----------------

// char * Cosine(struct MSV* svptr, char * ep, char * arg1,
// char * arg2): the two-argument soh-split shape; the class-global
// Cosine(A,B) double IS in scope (unit u12 Cosine.kt) and is called
// directly here.
fun Cosine(svptr: Msv, ep: String, arg1: CharArray, arg2: CharArray): String {
    var i = 0
    while (arg1[i] != NUL) {
        if (arg1[i] == CHAR_1) break
        i++
    }
    arg1[i] = NUL
    val a = GlobalRef(CstringOps.cstringAt(arg1, 1)) // bypass caret

    var j = 0
    while (arg2[j] != NUL) {
        if (arg2[j] == CHAR_1) break
        j++
    }
    arg2[j] = NUL
    val b = GlobalRef(CstringOps.cstringAt(arg2, 1)) // bypass caret

    val x = Cosine(
        a(CstringOps.cstringAt(arg1, i + 1)),
        b(CstringOps.cstringAt(arg2, j + 1))
    )

    return String.format(java.util.Locale.US, "%g", x)
}

// char * TermCorrelate(struct MSV* svptr, char * ep, char * arg1,
// char * arg2): the member IS in scope (unit-60 globalTermCorrelate)
// and delegates through the keyed invoke pair.
fun TermCorrelate(
    svptr: Msv,
    ep: String,
    arg1: CharArray,
    arg2: CharArray
): String {
    var i = 0
    while (arg1[i] != NUL) {
        if (arg1[i] == CHAR_1) break
        i++
    }
    arg1[i] = NUL
    val a = GlobalRef(CstringOps.cstringAt(arg1, 1)) // bypass caret

    var j = 0
    while (arg2[j] != NUL) {
        if (arg2[j] == CHAR_1) break
        j++
    }
    arg2[j] = NUL
    val b = GlobalRef(CstringOps.cstringAt(arg2, 1)) // bypass caret

    a(CstringOps.cstringAt(arg1, i + 1))
        .globalTermCorrelate(b(CstringOps.cstringAt(arg2, j + 1)))
    return ""
}

// char * DocCorrelate(struct MSV* svptr, char * ep, char * arg1,
// char * arg2, char * arg3, char * arg4): the member is out of
// scope (batch-10 unit 288) - the H-014 seat; the percent-lf
// sscanf widens through the strtod parse.
fun DocCorrelate(
    svptr: Msv,
    ep: String,
    arg1: CharArray,
    arg2: CharArray,
    arg3: CharArray,
    arg4: CharArray
): String {
    var i = 0
    while (arg1[i] != NUL) {
        if (arg1[i] == CHAR_1) break
        i++
    }
    arg1[i] = NUL
    val a = GlobalRef(CstringOps.cstringAt(arg1, 1)) // bypass caret

    var j = 0
    while (arg2[j] != NUL) {
        if (arg2[j] == CHAR_1) break
        j++
    }
    arg2[j] = NUL
    val b = GlobalRef(CstringOps.cstringAt(arg2, 1)) // bypass caret

    val x = strtod(CstringOps.cstring(arg4))

    Stubs.globalDocCorrelate(
        a(CstringOps.cstringAt(arg1, i + 1)),
        b(CstringOps.cstringAt(arg2, j + 1)),
        CstringOps.cstring(arg3),
        x
    )
    return ""
}

// char * Sim1(struct MSV* svptr, char * ep, char * arg1,
// char * arg2): the free Sim1(global,global) double is batch-7
// unit 199 - H-014 seat.
fun Sim1(svptr: Msv, ep: String, arg1: CharArray, arg2: CharArray): String {
    var i = 0
    while (arg1[i] != NUL) {
        if (arg1[i] == CHAR_1) break
        i++
    }
    arg1[i] = NUL
    val a = GlobalRef(CstringOps.cstringAt(arg1, 1)) // bypass caret

    var j = 0
    while (arg2[j] != NUL) {
        if (arg2[j] == CHAR_1) break
        j++
    }
    arg2[j] = NUL
    val b = GlobalRef(CstringOps.cstringAt(arg2, 1)) // bypass caret

    val x = Stubs.sim1(
        a(CstringOps.cstringAt(arg1, i + 1)),
        b(CstringOps.cstringAt(arg2, j + 1))
    )

    return String.format(java.util.Locale.US, "%g", x)
}

// char * Jaccard(struct MSV* svptr, char * ep, char * arg1,
// char * arg2): batch-9 unit 256 seat.
fun Jaccard(svptr: Msv, ep: String, arg1: CharArray, arg2: CharArray): String {
    var i = 0
    while (arg1[i] != NUL) {
        if (arg1[i] == CHAR_1) break
        i++
    }
    arg1[i] = NUL
    val a = GlobalRef(CstringOps.cstringAt(arg1, 1)) // bypass caret

    var j = 0
    while (arg2[j] != NUL) {
        if (arg2[j] == CHAR_1) break
        j++
    }
    arg2[j] = NUL
    val b = GlobalRef(CstringOps.cstringAt(arg2, 1)) // bypass caret

    val x = Stubs.jaccard(
        a(CstringOps.cstringAt(arg1, i + 1)),
        b(CstringOps.cstringAt(arg2, j + 1))
    )

    return String.format(java.util.Locale.US, "%g", x)
}

// char * Dice(struct MSV* svptr, char * ep, char * arg1,
// char * arg2): batch-6 unit 166 seat.
fun Dice(svptr: Msv, ep: String, arg1: CharArray, arg2: CharArray): String {
    var i = 0
    while (arg1[i] != NUL) {
        if (arg1[i] == CHAR_1) break
        i++
    }
    arg1[i] = NUL
    val a = GlobalRef(CstringOps.cstringAt(arg1, 1)) // bypass caret

    var j = 0
    while (arg2[j] != NUL) {
        if (arg2[j] == CHAR_1) break
        j++
    }
    arg2[j] = NUL
    val b = GlobalRef(CstringOps.cstringAt(arg2, 1)) // bypass caret

    val x = Stubs.dice(
        a(CstringOps.cstringAt(arg1, i + 1)),
        b(CstringOps.cstringAt(arg2, j + 1))
    )

    return String.format(java.util.Locale.US, "%g", x)
}

// char * StopInit(struct MSV* svptr, char * ep, char * arg1):
// batch-6 unit 175 seat; the vendor i/j dead locals not rendered.
fun StopInit(svptr: Msv, ep: String, arg1: CharArray): String {
    Stubs.stopInit(CstringOps.cstring(arg1))
    return ""
}

// char * StopLookup(struct MSV* svptr, char * ep, char * arg1):
// the truthiness of the int (char*)""-vs-"1" split kept verbatim.
fun StopLookup(svptr: Msv, ep: String, arg1: CharArray): String {
    if (Stubs.stopLookup(CstringOps.cstring(arg1)) != 0) return "1"

    return "0"
}

// char * SynInit(struct MSV* svptr, char * ep, char * arg1):
// batch-5 unit 133 seat; the int return is discarded by the vendor.
fun SynInit(svptr: Msv, ep: String, arg1: CharArray): String {
    Stubs.synInit(CstringOps.cstring(arg1))
    return ""
}

// char * SynLookup(struct MSV* svptr, char * ep, char * arg1):
// the free SYN(char*) synonym lookup seat (not a corpus unit).
fun SynLookup(svptr: Msv, ep: String, arg1: CharArray): String {
    return Stubs.SYN(CstringOps.cstring(arg1))
}

// char * Centroid(struct MSV*, char * ep, char * arg1,
// char * arg2): the member IS in scope (unit-112 Centroid.kt).
fun Centroid(svptr: Msv, ep: String, arg1: CharArray, arg2: CharArray): String {
    var i = 0
    while (arg1[i] != NUL) {
        if (arg1[i] == CHAR_1) break
        i++
    }
    arg1[i] = NUL
    val a = GlobalRef(CstringOps.cstringAt(arg1, 1)) // bypass caret

    var j = 0
    while (arg2[j] != NUL) {
        if (arg2[j] == CHAR_1) break
        j++
    }
    arg2[j] = NUL
    val b = GlobalRef(CstringOps.cstringAt(arg2, 1)) // bypass caret

    a(CstringOps.cstringAt(arg1, i + 1))
        .Centroid(b(CstringOps.cstringAt(arg2, j + 1)))

    return ""
}

// char * BMGSearch(struct MSV*, char * ep, char * arg1,
// char * arg2): the malloc-window wart (strlen of the arg2+2
// window) folds to the String domain - ledgered in the banner; the
// trailing-space cat and the percent-d return are vendor-verbatim.
fun BMGSearch(svptr: Msv, ep: String, arg1: CharArray, arg2: CharArray): String {
    val p1 = CstringOps.cstring(arg2) + " "

    val i = Stubs.bmgFullSearch(CstringOps.cstring(arg1), p1)

    return String.format(java.util.Locale.US, "%d", i)
}

// char * Shred(struct MSV*, char * ep, char * arg1, char * arg2):
// batch-6 unit 165 seat; atoi is the arm Find.kt seat.
fun Shred(svptr: Msv, ep: String, arg1: CharArray, arg2: CharArray): String {
    val i = atoi(CstringOps.cstring(arg2))
    return Stubs.shred(CstringOps.cstring(arg1), i)
}

// char * ShredQuery(struct MSV* svPtr, char * ep, char * arg1,
// char * arg2): the shred query twin seat (not a corpus unit).
fun ShredQuery(svptr: Msv, ep: String, arg1: CharArray, arg2: CharArray): String {
    val i = atoi(CstringOps.cstring(arg2))
    return Stubs.shredQuery(CstringOps.cstring(arg1), i)
}
