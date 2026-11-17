package mumps.mdh

import java.util.Locale
import mumps.interp.Msv
import mumps.interp.NUL
import mumps.interp.atoiC
import mumps.interp.mglobal
import mumps.interp.cstringAt
import mumps.interp.strlenC
import mumps.interp.strtodC

/**
 * PRO-arm translation of libmpscpp.cpp::<misc-9> (B4 unit 97,
 * rank 284). Translated independently from the pristine vendor
 * source (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe),
 * reference source lines 9693-9982 and 10390-10576 (units.csv:
 * 342 loc, score 161.2 - exact match to row 284; the residual
 * chunk begins at the function head of operator<=(float) at
 * vendor line 9693, whose first body line 9694 is the tool's
 * recorded chunk boundary).
 *
 * Slice contents: the mstring operator<= / == / != overload
 * families and operator const char*(); SymGet x3, SymPut x9 and
 * __symput; global::Flush; the '// BUILTIN FUNCTIONS' banner and
 * the svPtr-intrinsic wrappers Cosine, TermCorrelate,
 * DocCorrelate, Sim1, Jaccard, Dice, StopInit, StopLookup,
 * SynInit, SynLookup, Centroid, BMGSearch, Shred, ShredQuery.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h008 static_scratch   : the wrappers' per-function
 *    'static char tmp[32]' persist across calls - carried on the
 *    Misc9Scratch object, one field per vendor function (MaxScratch
 *    precedent, unit 88).
 *  - h009 format_string    : sprintf(tmp,"%g",x) rendered as
 *    java.util.String.format over the %g conversion (Max unit 88
 *    precedent); sprintf(tmp,"%d",i) likewise; the sscanf(arg4,
 *    "%lf",&x) becomes the local parser sscanfLf (Idf unit 49
 *    precedent; Locale.US pinned against locale drift).
 *  - h010 numeric_promotion: the float<=/==/!= overloads widen the
 *    rhs to the double domain (vendor atof both sides); atoi(arg2)
 *    widens to Int via the shared atoiC.
 *  - h012 unsigned_byte_domain: arg/tmp buffers are C-string
 *    domains (Char 0..255); the wrapper SOH sentinel compares
 *    against Char(1).
 *  - h013 dead_store        : the unused 'int i, j;' in StopInit/
 *    StopLookup/SynInit/SynLookup and the unused 'static char
 *    tmp[32]' in TermCorrelate/DocCorrelate/Centroid (declared,
 *    never written) are dropped with these comments, not
 *    translated.
 *  - h014 out_of_scope_call : sym_ (MdhStubs stub, owner symbol-
 *    table machinery), MumpsGlobalException (MdhStubs carrier),
 *    Mglobal->mglobal (InterpStubs stub), Global.readInto (NEW
 *    MdhStubs stub raised by this unit - owner libmpscpp.cpp class
 *    global operator>>(char*) machinery, globalClass.h:178); the
 *    misc-9 wrapper callees ride NEW owner-named MdhStubs stubs
 *    (Sim1 batch-7, Jaccard batch-9, Dice batch-6, global::
 *    Centroid batch-4, global::DocCorrelate batch-10, StopInit
 *    batch-6, SynInit batch-5, StopLookup/SYN/Shred/ShredQuery/
 *    bmg_fullsearch not corpus units); the previously-raised
 *    SymGet(String) stub (owner libmpscpp.cpp:9858) is REMOVED -
 *    this unit owns it now (Scan unit 81 precedent); Cosine
 *    (DONE, unit 71) and global::TermCorrelate (DONE, unit 14)
 *    are called directly; strtodC/atoiC/strlenC/cstringAt real
 *    helpers reused unmodified.
 *  - h015 grammar_collision : pre-push scan incl. cross-file
 *    top-level names: new top-level Cosine overload (Msv, CharArray?,
 *    CharArray, CharArray) - distinct from unit 71's Cosine(Global,
 *    Global) - plus Sim1/Jaccard/Dice/TermCorrelate-style names - no
 *    collisions; the C++ operator overloads fold per Kotlin
 *    conventions (see header notes); the vendor char-star, const
 *    char-star and std::string rhs overloads of <=/==/!= all map to Kotlin String
 *    and are FOLDED into one overload each (three vendor signatures
 *    per operator, one Kotlin signature - documented, not silently
 *    dropped); the vendor != families fold into Kotlin's negated
 *    equals (no separate translation); toString serves the vendor
 *    operator const char*() alongside the carrier's existing c_str().
 *  - h017 naming          : all function names keep vendor
 *    spellings; the wrapper signatures keep the vendor parameter
 *    order (svptr, ep, args).
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - operator<=(global)/==(global)/!=(global) extract via i >> t1
 *    (global::operator>>(string&)) - raised as Global.readInto over
 *    a CharArray buffer.
 *  - The ==/!= dispatch rides the Mstring.equals override (see the
 *    carrier, added by this unit): C++ == is value comparison of
 *    data against the rhs (atol/atof for numeric rhs, lexicographic
 *    for string-ish rhs) - equals mirrors exactly that, and a
 *    hashCode override accompanies it (Kotlin consistency
 *    requirement; the vendor has none - adaptation).
 *  - The wrapper body pattern 'for (i=0; arg1[i]!=0; i++) if
 *    (arg1[i]==1) break; arg1[i]=0; global a(&arg1[1]);' MUTATES
 *    the arg buffers in place (SOH replaced by NUL) - kept.
 *  - 'a(&arg1[i+1])' invokes the folded u31 carrier operator() -
 *    but the callees here are (Global, Global)-shaped (Cosine unit
 *    71, global::TermCorrelate unit 14 both take the A/B-side
 *    Global whose .ref carries the subscripted reference), so the
 *    wrappers mirror MakeGlobal's reference assembly directly
 *    (myname + SOH + subscript) instead of the invoke (whose
 *    makeGlobal side effect stays with the batch-7 owner);
 *    documented per the Max/global::Max unit 88 precedent.
 *  - BMGSearch: 'malloc(strlen(arg2+2))' reads the length from
 *    offset 2 and omits the +1 for the NUL - the buffer is 3 bytes
 *    short for the strcpy+strcat that follows (vendor heap
 *    overrun); the Kotlin copy loops throw IndexOutOfBounds where
 *    the vendor corrupts memory - kept as the honest observable.
 *    The vendor call bmg_fullsearch(arg1,p1) binds the (char*,
 *    global) overload at 8371 via the implicit char*->global
 *    conversion (no (char*,char*) overload exists) - the stub
 *    takes both sides as String and the conversion is noted.
 *  - Shred/ShredQuery return their callees' char* directly
 *    (Input unit 10 String-return precedent); the tmp-based
 *    wrappers return the NUL-terminated scratch CharArray (Max
 *    unit 88 precedent).
 */

private const val STR_MAX = 32000 // vendor strmax.h:3 (file-private, cf. unit 22)
private const val SYMGET = 11     // vendor libmpscpp.h sym_ fn code for get (cf. sym_(11,...) call shape)
private const val SYMSTORE = 0    // vendor libmpscpp.h:78 __symput uses sym_(0,...)
private const val SYMTAB_ERROR = 9 // vendor libmpscpp.h:78
private const val FLUSH = 21      // vendor defines.h (Mglobal FLUSH)

/** The vendor's x01 subscript separator byte (Cosine unit 71 precedent). */
private val SOH = 1.toChar()

/** String -> NUL-terminated C-string buffer (call-shape helper). */
private fun toCbuf(s: String): CharArray {
    val b = CharArray(s.length + 1)
    for (k in s.indices) b[k] = s[k]
    b[s.length] = NUL
    return b
}

/** atof(data.c_str()) over the Kotlin String value (strtodC reuse, h014). */
private fun atofS(s: String): Double = strtodC(toCbuf(s))

/** sscanf(arg4, "%lf", &x) analog (Idf unit 49 sscanfLf precedent). */
private fun sscanfLf(s: CharArray): Double? {
    var i = 0
    while (i < s.size && (s[i] == ' ' || s[i] == '\t')) i++
    val start = i
    if (i < s.size && (s[i] == '+' || s[i] == '-')) i++
    var digits = 0
    while (i < s.size && s[i] in '0'..'9') {
        i++; digits++
    }
    if (i < s.size && s[i] == '.') {
        i++
        while (i < s.size && s[i] in '0'..'9') {
            i++; digits++
        }
    }
    if (digits == 0) return null // sscanf: 0 conversions
    return strtodC(s.copyOfRange(start, i))
}

/** vendor: the per-function static char tmp[32] of the wrappers (h008). */
object Misc9Scratch {
    val cosineTmp: CharArray = CharArray(32)
    val sim1Tmp: CharArray = CharArray(32)
    val jaccardTmp: CharArray = CharArray(32)
    val diceTmp: CharArray = CharArray(32)
    val bmgTmp: CharArray = CharArray(32)
}

// -----------------------------------------------
//  mstring operator<= family (vendor 9693-9724)
// -----------------------------------------------

/** vendor 9693: bool mstring::operator<=(float i). */
operator fun Mstring.compareTo(i: Float): Int =
    atofS(value).compareTo(i.toDouble()) // h010: float rhs into the double domain

/** vendor 9699/9704/9709: operator<=(char*)/(const char*)/(string) - folded (h015). */
operator fun Mstring.compareTo(i: String): Int = value.compareTo(i)

/** vendor 9714: bool mstring::operator<=(global i). */
operator fun Mstring.compareTo(i: Global): Int {
    val t1 = CharArray(STR_MAX)
    i.readInto(t1) // vendor: i >> t1 (h014 stub)
    return value.compareTo(cstringAt(t1))
}

/** vendor 9720: bool mstring::operator<=(mstring i). */
operator fun Mstring.compareTo(i: Mstring): Int = value.compareTo(i.value)

// -----------------------------------------------
//  SymGet x3 (vendor 9844-9885)
// -----------------------------------------------

/** vendor 9844: mstring SymGet(mstring str). */
fun SymGet(str: Mstring): Mstring {
    val tmp = CharArray(STR_MAX)
    val p1 = sym_(SYMGET, toCbuf(str.value), tmp, MdhState.svPtr)
    if (p1 == null) {
        MdhState.globalExceptionCode = SYMTAB_ERROR
        throw MumpsGlobalException()
    }
    return Mstring(cstringAt(tmp)) // rslt = tmp
}

/**
 * vendor 9858: mstring SymGet(char * str). Replaces the B3-era
 * MdhStubs stub raised for this owner (removed with this landing).
 */
fun SymGet(str: String): Mstring {
    val tmp = CharArray(STR_MAX)
    val p1 = sym_(SYMGET, toCbuf(str), tmp, MdhState.svPtr)
    if (p1 == null) {
        MdhState.globalExceptionCode = SYMTAB_ERROR
        throw MumpsGlobalException()
    }
    return Mstring(cstringAt(tmp)) // rslt = tmp
}

/** vendor 9872: mstring SymGet(global &str). */
fun SymGet(str: Global): Mstring {
    val tmp = CharArray(STR_MAX)
    val tmp2 = CharArray(STR_MAX)
    str.readInto(tmp2) // vendor: str >> tmp2 (h014 stub)
    val p1 = sym_(SYMGET, tmp2, tmp, MdhState.svPtr)
    if (p1 == null) {
        MdhState.globalExceptionCode = SYMTAB_ERROR
        throw MumpsGlobalException()
    }
    return Mstring(cstringAt(tmp)) // rslt = tmp
}

// -----------------------------------------------
//  SymPut x9 + __symput (vendor 9887-9953)
// -----------------------------------------------

/** vendor 9887: bool SymPut(mstring &name, mstring &value). */
fun SymPut(name: Mstring, value: Mstring): Boolean {
    __symput(name.value, value.value)
    return true
}

/** vendor 9892: bool SymPut(mstring &name, global &value). */
fun SymPut(name: Mstring, value: Global): Boolean {
    val tmp = CharArray(STR_MAX)
    value.readInto(tmp) // vendor: value >> tmp
    __symput(name.value, cstringAt(tmp))
    return true
}

/** vendor 9899: bool SymPut(mstring &name, char *value). */
fun SymPut(name: Mstring, value: String): Boolean {
    __symput(name.value, value)
    return true
}

/** vendor 9904: bool SymPut(char *name, mstring &value). */
fun SymPut(name: String, value: Mstring): Boolean {
    __symput(name, value.value)
    return true
}

/** vendor 9909: bool SymPut(char *name, global &value). */
fun SymPut(name: String, value: Global): Boolean {
    val tmp = CharArray(STR_MAX)
    value.readInto(tmp) // vendor: value >> tmp
    __symput(name, cstringAt(tmp))
    return true
}

/** vendor 9916: bool SymPut(char *name, char *value). */
fun SymPut(name: String, value: String): Boolean {
    __symput(name, value)
    return true
}

/** vendor 9921: bool SymPut(global &name, char *value). */
fun SymPut(name: Global, value: String): Boolean {
    val tmp = CharArray(STR_MAX)
    name.readInto(tmp) // vendor: name >> tmp
    __symput(cstringAt(tmp), value)
    return true
}

/** vendor 9928: bool SymPut(global &name, mstring &value). */
fun SymPut(name: Global, value: Mstring): Boolean {
    val tmp = CharArray(STR_MAX)
    name.readInto(tmp) // vendor: name >> tmp
    __symput(cstringAt(tmp), value.value)
    return true
}

/** vendor 9935: bool SymPut(global &name, global &value). */
fun SymPut(name: Global, value: Global): Boolean {
    val tmp1 = CharArray(STR_MAX)
    val tmp2 = CharArray(STR_MAX)
    name.readInto(tmp1)  // vendor: name >> tmp1
    value.readInto(tmp2) // vendor: value >> tmp2
    __symput(cstringAt(tmp1), cstringAt(tmp2))
    return true
}

/** vendor 9943: void __symput(const char *str, const char *val). */
fun __symput(str: String, value: String) {
    val p1 = sym_(SYMSTORE, toCbuf(str), toCbuf(value), MdhState.svPtr)
    if (p1 == null) {
        MdhState.globalExceptionCode = SYMTAB_ERROR
        throw MumpsGlobalException()
    }
}

// -----------------------------------------------
//  global::Flush (vendor 9955-9958)
// -----------------------------------------------

/** vendor 9955: void global::Flush() - unsigned char t1[16]="", t2[16]="". */
fun Global.Flush() {
    val t1 = CharArray(16)
    val t2 = CharArray(16)
    mglobal(FLUSH, t1, t2, MdhState.svPtr)
}

// BUILTIN FUNCTIONS  (vendor banner line 9960)

/**
 * vendor 9962: char * Cosine(struct MSV* svptr, char* ep, char* arg1,
 * char* arg2) - the svPtr intrinsic wrapper over the free
 * Cosine(global,global) (DONE, unit 71).
 */
fun Cosine(svptr: Msv, ep: CharArray?, arg1: CharArray, arg2: CharArray): CharArray {
    val tmp = Misc9Scratch.cosineTmp

    var i = 0
    while (arg1[i] != NUL) { // vendor: for (i=0; arg1[i]!=0; i++)
        if (arg1[i] == SOH) break
        i++
    }
    arg1[i] = NUL
    val a = Global(cstringAt(arg1, 1)) // vendor: global a(&arg1[1]); // bypass ^

    var j = 0
    while (arg2[j] != NUL) {
        if (arg2[j] == SOH) break
        j++
    }
    arg2[j] = NUL
    val b = Global(cstringAt(arg2, 1)) // vendor: global b(&arg2[1]); // bypass ^

    // vendor: x = Cosine(a(&arg1[i+1]), b(&arg2[j+1]));
    // the subscripted-reference assembly mirrors MakeGlobal
    // (myname + SOH + subscript; see header note)
    val x = Cosine(
        Global(a.myname + SOH + cstringAt(arg1, i + 1)),
        Global(b.myname + SOH + cstringAt(arg2, j + 1))
    )

    // vendor: sprintf(tmp,"%g",x);
    val s = String.format(Locale.US, "%g", x) // h009: JVM %g shape
    for (k in s.indices) tmp[k] = s[k]
    tmp[s.length] = NUL

    return tmp
}

/**
 * vendor 10390: char * TermCorrelate(...) - walks the A-side
 * reference; global::TermCorrelate is DONE (unit 14).
 */
fun TermCorrelate(svptr: Msv, ep: CharArray?, arg1: CharArray, arg2: CharArray): String {
    var i = 0
    while (arg1[i] != NUL) {
        if (arg1[i] == SOH) break
        i++
    }
    arg1[i] = NUL
    val a = Global(cstringAt(arg1, 1)) // vendor: global a(&arg1[1]); // bypass ^

    var j = 0
    while (arg2[j] != NUL) {
        if (arg2[j] == SOH) break
        j++
    }
    arg2[j] = NUL
    val b = Global(cstringAt(arg2, 1)) // vendor: global b(&arg2[1]); // bypass ^

    // vendor: a(&arg1[i+1]).TermCorrelate(b(&arg2[j+1]));
    Global(a.myname + SOH + cstringAt(arg1, i + 1))
        .TermCorrelate(Global(b.myname + SOH + cstringAt(arg2, j + 1))) // unit 14 (h014 DONE)

    return ""
}

/** vendor 10409: char * DocCorrelate(..., arg3, arg4). */
fun DocCorrelate(
    svptr: Msv, ep: CharArray?, arg1: CharArray, arg2: CharArray,
    arg3: CharArray, arg4: CharArray
): String {
    var i = 0
    while (arg1[i] != NUL) {
        if (arg1[i] == SOH) break
        i++
    }
    arg1[i] = NUL
    val a = Global(cstringAt(arg1, 1)) // vendor: global a(&arg1[1]); // bypass ^

    var j = 0
    while (arg2[j] != NUL) {
        if (arg2[j] == SOH) break
        j++
    }
    arg2[j] = NUL
    val b = Global(cstringAt(arg2, 1)) // vendor: global b(&arg2[1]); // bypass ^

    val x = sscanfLf(arg4) // vendor: sscanf(arg4,"%lf",&x)

    // vendor: a(&arg1[i+1]).DocCorrelate(b(&arg2[j+1]),arg3,x);
    // the null-check throw mirrors the vendor's uninitialized x on a
    // 0-conversion sscanf failure - UB in C; an explicit Kotlin
    // failure here, cf. the BMGSearch overrun note (an explicit
    // not-null assertion would carry the same semantics).
    val xv =
        if (x != null) x
        else throw NullPointerException("DocCorrelate: vendor uninitialized x (see KDoc)")
    Global(a.myname + SOH + cstringAt(arg1, i + 1))
        .DocCorrelate(Global(b.myname + SOH + cstringAt(arg2, j + 1)), cstringAt(arg3), xv)

    return ""
}

/** vendor 10431: char * Sim1(...). */
fun Sim1(svptr: Msv, ep: CharArray?, arg1: CharArray, arg2: CharArray): CharArray {
    val tmp = Misc9Scratch.sim1Tmp

    var i = 0
    while (arg1[i] != NUL) {
        if (arg1[i] == SOH) break
        i++
    }
    arg1[i] = NUL
    val a = Global(cstringAt(arg1, 1)) // vendor: global a(&arg1[1]); // bypass ^

    var j = 0
    while (arg2[j] != NUL) {
        if (arg2[j] == SOH) break
        j++
    }
    arg2[j] = NUL
    val b = Global(cstringAt(arg2, 1)) // vendor: global b(&arg2[1]); // bypass ^

    // vendor: x = Sim1(a(&arg1[i+1]), b(&arg2[j+1]));
    val x = Sim1(
        Global(a.myname + SOH + cstringAt(arg1, i + 1)),
        Global(b.myname + SOH + cstringAt(arg2, j + 1))
    ) // h014 stub (batch-7 owner)

    // vendor: sprintf(tmp,"%g",x);
    val s = String.format(Locale.US, "%g", x) // h009: JVM %g shape
    for (k in s.indices) tmp[k] = s[k]
    tmp[s.length] = NUL

    return tmp
}

/** vendor 10453: char * Jaccard(...). */
fun Jaccard(svptr: Msv, ep: CharArray?, arg1: CharArray, arg2: CharArray): CharArray {
    val tmp = Misc9Scratch.jaccardTmp

    var i = 0
    while (arg1[i] != NUL) {
        if (arg1[i] == SOH) break
        i++
    }
    arg1[i] = NUL
    val a = Global(cstringAt(arg1, 1)) // vendor: global a(&arg1[1]); // bypass ^

    var j = 0
    while (arg2[j] != NUL) {
        if (arg2[j] == SOH) break
        j++
    }
    arg2[j] = NUL
    val b = Global(cstringAt(arg2, 1)) // vendor: global b(&arg2[1]); // bypass ^

    // vendor: x = Jaccard(a(&arg1[i+1]), b(&arg2[j+1]));
    val x = Jaccard(
        Global(a.myname + SOH + cstringAt(arg1, i + 1)),
        Global(b.myname + SOH + cstringAt(arg2, j + 1))
    ) // h014 stub (batch-9 owner)

    // vendor: sprintf(tmp,"%g",x);
    val s = String.format(Locale.US, "%g", x) // h009: JVM %g shape
    for (k in s.indices) tmp[k] = s[k]
    tmp[s.length] = NUL

    return tmp
}

/** vendor 10475: char * Dice(...). */
fun Dice(svptr: Msv, ep: CharArray?, arg1: CharArray, arg2: CharArray): CharArray {
    val tmp = Misc9Scratch.diceTmp

    var i = 0
    while (arg1[i] != NUL) {
        if (arg1[i] == SOH) break
        i++
    }
    arg1[i] = NUL
    val a = Global(cstringAt(arg1, 1)) // vendor: global a(&arg1[1]); // bypass ^

    var j = 0
    while (arg2[j] != NUL) {
        if (arg2[j] == SOH) break
        j++
    }
    arg2[j] = NUL
    val b = Global(cstringAt(arg2, 1)) // vendor: global b(&arg2[1]); // bypass ^

    // vendor: x = Dice(a(&arg1[i+1]), b(&arg2[j+1]));
    val x = Dice(
        Global(a.myname + SOH + cstringAt(arg1, i + 1)),
        Global(b.myname + SOH + cstringAt(arg2, j + 1))
    ) // h014 stub (batch-6 owner)

    // vendor: sprintf(tmp,"%g",x);
    val s = String.format(Locale.US, "%g", x) // h009: JVM %g shape
    for (k in s.indices) tmp[k] = s[k]
    tmp[s.length] = NUL

    return tmp
}

/** vendor 10497: char * StopInit(struct MSV* svptr, char* ep, char* arg1). */
fun StopInit(svptr: Msv, ep: CharArray?, arg1: CharArray): String {
    StopInit(cstringAt(arg1)) // h014 stub (batch-6 owner; char* overload at 7720)
    return ""
}

/** vendor 10504: char * StopLookup(struct MSV* svptr, char* ep, char* arg1). */
fun StopLookup(svptr: Msv, ep: CharArray?, arg1: CharArray): String {
    if (StopLookup(cstringAt(arg1))) return "1" // h014 stub (not a corpus unit)
    return "0"
}

/** vendor 10513: char * SynInit(struct MSV* svptr, char* ep, char* arg1). */
fun SynInit(svptr: Msv, ep: CharArray?, arg1: CharArray): String {
    SynInit(cstringAt(arg1)) // unit 126 (DONE - real body in SynInit.kt)
    return ""
}

/** vendor 10520: char * SynLookup(struct MSV* svptr, char* ep, char* arg1). */
fun SynLookup(svptr: Msv, ep: CharArray?, arg1: CharArray): String {
    return SYN(cstringAt(arg1)) // h014 stub (not a corpus unit)
}

/** vendor 10526: char * Centroid(struct MSV*, char* ep, char* arg1, char* arg2). */
fun Centroid(svptr: Msv, ep: CharArray?, arg1: CharArray, arg2: CharArray): String {
    var i = 0
    while (arg1[i] != NUL) {
        if (arg1[i] == SOH) break
        i++
    }
    arg1[i] = NUL
    val a = Global(cstringAt(arg1, 1)) // vendor: global a(&arg1[1]); // bypass ^

    var j = 0
    while (arg2[j] != NUL) {
        if (arg2[j] == SOH) break
        j++
    }
    arg2[j] = NUL
    val b = Global(cstringAt(arg2, 1)) // vendor: global b(&arg2[1]); // bypass ^

    // vendor: a(&arg1[i+1]).Centroid(b(&arg2[j+1]));
    Global(a.myname + SOH + cstringAt(arg1, i + 1))
        .Centroid(Global(b.myname + SOH + cstringAt(arg2, j + 1))) // h014 stub (batch-4 owner)

    return ""
}

/** vendor 10547: char * BMGSearch(struct MSV*, char* ep, char* arg1, char* arg2). */
fun BMGSearch(svptr: Msv, ep: CharArray?, arg1: CharArray, arg2: CharArray): CharArray {
    val tmp = Misc9Scratch.bmgTmp

    // vendor: char *p1=(char *)malloc(strlen(arg2+2)); strcpy(p1,arg2);
    // strcat(p1," ");  - the allocation is 3 bytes short (see header);
    val p1 = CharArray(strlenC(arg2, 2))
    var d = 0
    while (arg2[d] != NUL) { // strcpy(p1, arg2)
        p1[d] = arg2[d]
        d++
    }
    p1[d] = NUL
    p1[d] = ' ' // strcat(p1, " ")
    p1[d + 1] = NUL

    val i = bmg_fullsearch(cstringAt(arg1), cstringAt(p1)) // h014 stub (see header)

    // vendor: sprintf(tmp,"%d",i);
    val s = String.format(Locale.US, "%d", i) // h009: JVM %d shape
    for (k in s.indices) tmp[k] = s[k]
    tmp[s.length] = NUL

    return tmp
}

/** vendor 10562: char * Shred(struct MSV*, char* ep, char* arg1, char* arg2). */
fun Shred(svptr: Msv, ep: CharArray?, arg1: CharArray, arg2: CharArray): String {
    val i = atoiC(arg2) // vendor: i = atoi(arg2)
    return Shred(cstringAt(arg1), i) // h014 stub (batch-6 owner)
}

/** vendor 10570: char * ShredQuery(struct MSV* svPtr, char* ep, char* arg1, char* arg2). */
fun ShredQuery(svptr: Msv, ep: CharArray?, arg1: CharArray, arg2: CharArray): String {
    val i = atoiC(arg2) // vendor: i = atoi(arg2)
    return ShredQuery(cstringAt(arg1), i) // h014 stub (not a corpus unit)
}
