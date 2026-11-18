package mumps.mdh

import java.io.BufferedReader
import java.io.InputStream
import java.io.Reader
import java.util.Locale
import java.util.Scanner
import mumps.interp.ErrorMessage
import mumps.interp.NUL
import mumps.interp.cstringAt
import mumps.interp.mglobal
import mumps.interp.strlenC
import mumps.interp.writeCstring

/**
 * PRO-arm translation of libmpscpp.cpp::&lt;misc-6&gt; (B7 tranche 2,
 * unit 188, rank 294). Translated independently from the pristine
 * vendor source (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe),
 * reference source lines 2677-3466 (the 600-physLOC residue chunk
 * after misc-5's mid-mcvt cutoff; the mcvt family opens it and the
 * global constructor closes it).
 *
 * Identity verified against the study tool: libmpscpp.cpp::&lt;misc-6&gt;,
 * cpp, loc 600, comment_ratio 0.003, pointer_marks 62,
 * control_marks 58, score 319.2 - exact match to
 * wp1/study-subset/units.csv row 188 (batch 7).
 *
 * Chunk contents and dispositions (one-owner policy):
 *  - mcvt x5 + cvt x4 (vendor 2677-2738): RENDERED here (no prior
 *    seat; the u131 numeric ctor family in Mstring.kt is the
 *    mstringClass.h declaration surface, these are the free
 *    conversion helpers).
 *  - global::operator() family (vendor 2739-2931, 30+ overloads):
 *    FRONT-RUN by u31 (GlobalCarrier.kt:87-127) - the string,
 *    mstring and const-char-star families fold into the vararg
 *    invoke (carrier fold, u31 journal), the indirect overload is
 *    the u31 Indirect invoke, and the () form's names="" value=""
 *    quirk is carried. VERIFICATION ONLY (u150 precedent): the
 *    fold's names list ("a".."a,b,c,d,e,f,g,h,i,j") and value order
 *    match the vendor line for line; no redeclaration. The const
 *    long overload (vendor 2871) had NO prior seat - rendered here
 *    as the Long invoke extension (sprintf %d then the "a" call).
 *  - count (2932) + HitRatio (2953): RENDERED (free functions).
 *  - EncodeHTML mstring twin (2961): FRONT-RUN by u5 (EncodeHtml.kt:
 *    30, the char-star body) and u28 (roster row 28, batch 1) - the
 *    twin folds onto the Mstring carrier (EncodeHtml.kt:30); no
 *    redeclaration.
 *  - global::Name (2991), __test (2999): RENDERED.
 *  - global::ReadLine x3 (vendor 3003-3069): RESOLVED by
 *    GlobalReadLine.kt (roster row 236, batch 8 - the former
 *    deferral owner landed; this file's mstring family above
 *    is receiver-distinct, both seats coexist),
 *    formerly DEFERRED - corpus row
 *    236 (batch 8, libmpscpp.cpp::global::ReadLine) owns the family;
 *    the misc-5 pm precedent (future-batch owner, stub/deferral
 *    note only). No seat raised here.
 *  - mstring::ReadLine x3 (vendor 3071-3111): RENDERED (residue; no
 *    corpus row owns them).
 *  - mstring cast operators x9 (vendor 3113-3168): RESOLVES the u131
 *    h014 stub seats in Mstring.kt (toInt/toFloat/toDouble/toChar/
 *    toShort/toLong/toULong/toUInt/toUByte/toUShort) - bodies edited
 *    in place there; this file carries the ownership note only.
 *  - zBlanks x2 (3170-3204), Ascii x4 (3206-3255), Length() x2
 *    (3257-3273): RENDERED (first seats).
 *  - global::Length(global needle) (3274): FRONT-RUN by u145
 *    (GlobalLength.kt) - verification only. global::Length(mstring
 *    needle) (3306): FRONT-RUN by u181 - verification only.
 *  - global::Length(string needle) (3330) + global::Length(char*
 *    needle) (3354): RENDERED as one String-carrier overload (the
 *    two C++ argument types fold, u209 precedent).
 *  - mstring::Length(char* needle) (3378) + mstring::Length(string
 *    needle) (3392): RENDERED as one String overload;
 *    mstring::Length(global needle) (3406): FRONT-RUN by u149
 *    (MstringLength.kt) - verification only;
 *    mstring::Length(mstring needle) (3429): RENDERED.
 *  - Horolog (3443): RENDERED; raises the _horolog stub in
 *    InterpStubs (owner interp-sysfunc.cpp, batch-9 corpus unit).
 *  - global constructor (3451-3466): FRONT-RUN by u31
 *    (GlobalCarrier.kt:61-77 init block: Eflg guard, atexit
 *    shutdown-hook carrier, set_terminate left unwired) -
 *    verification only.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h004 globals_ext       : svPtr rides the MdhState carrier
 *    (u31/Count/Cosine convention); the operator()/ctor front-runs
 *    are h004 verification rows.
 *  - h008 static_scratch    : cvt's per-overload static char buffers
 *    (vendor 2715-2738) persist across C calls and alias; the fresh
 *    String returns cannot alias - divergence journaled (no caller
 *    holds two cvt results in the corpus); all other buffers are
 *    auto locals - fresh CharArray scratch.
 *  - h009 format_string     : sprintf %ld/%g/%d with Locale.US
 *    (u148 ImplicitDefaultLocale lesson).
 *  - h010 numeric_promotion : char is signed on the reference
 *    platform - mstring::Ascii sign-extends bytes >= 0x80
 *    (documented); global::Ascii reads unsigned char (0..255);
 *    __test narrows the vendor long tpx to int (.toInt()).
 *  - h012 unsigned_byte_domain : strlen/strcat/strstr walks over
 *    NUL-terminated CharArray byte domains (per-file privates).
 *  - h013 dead_code         : mcvt(char*) assigns tmp then returns
 *    the input i (vendor bug kept - tmp is a dead store, the
 *    function returns its argument); count's register inits are C
 *    declaration artifacts.
 *  - h014 out_of_scope_call : zfcn (ZfcnBifs.kt:382, u156), mglobal
 *    (InterpStubs, u161), ErrorMessage (InterpSysfuncMisc.kt:102),
 *    atoiC (CstringOps.kt:35) reused; _horolog raised (batch-9
 *    owner).
 *  - h015 grammar_collision : new top-levels mcvt/cvt/count/
 *    HitRatio/Horolog/__test scanned clean cross-file (__test is a
 *    documented vendor name, the u124 __finish exception family);
 *    the ReadLine/Ascii/Length/zBlanks/Name members are
 *    receiver-scoped overloads; "$zblanks" rebuilt from the DOLLAR
 *    carrier (36.toChar(), u34/u39 precedent - Kotlin string
 *    template hazard).
 *  - h017 naming           : all names keep the vendor spellings.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - HitRatio divides svPtr->hit / svPtr->count as two C longs -
 *    INTEGER division, then widens; rendered as Long/Long then
 *    toDouble (the 0<x<1 ratios truncate to 0 - vendor behavior).
 *  - count consumes the quote-skip i++ before the closing-quote
 *    test (a back-to-back quote skips one char - kept).
 *  - mstring::ReadLine strips the fgets newline unconditionally
 *    (tmp[strlen-1]=0) then strips a second char if < ' '
 *    (CR-LF); readLine() already excludes the terminator (u126
 *    built-in-strip precedent) - a final unterminated line, where
 *    the vendor strips the last real character, is quantized to
 *    the newline-terminated behavior (u174 StopInit degenerate
 *    case, documented).
 *  - mstring::zBlanks builds the command at &tmp[1]; tmp[0] is
 *    uninitialized in the vendor and never read by zfcn - the
 *    fresh CharArray quantizes it to NUL (documented).
 *  - global::zBlanks returns the mstring conversion of -1 on an
 *    empty global (Mstring("-1")).
 */

private const val STR_MAX = 32000 // vendor strmax.h:3 (file-private, cf. unit 22/97)
private const val RETRIEVE = 0 // vendor defines.h:83
private const val GLOBAL_NOT_FOUND = 2 // vendor libmpscpp.h:71
private const val DOLLAR = 36.toChar() // vendor '$' (h015 carrier)
private const val XCE = 206.toChar() // vendor \xce marker (defines.h:38)
private const val XCF = 207.toChar() // vendor \xcf marker (defines.h:39)

private fun toCbuf(s: String): CharArray {
    val b = CharArray(s.length + 1)
    for (i in s.indices) b[i] = s[i]
    b[s.length] = NUL
    return b
}

private fun strcatC(dst: CharArray, s: String) {
    var n = 0
    while (dst[n] != NUL) n++
    for (i in s.indices) dst[n + i] = s[i]
    dst[n + s.length] = NUL
}

// ---------------------------------------------------------------
// vendor 2677-2714: the mcvt family
// ---------------------------------------------------------------

/**
 * vendor 2677: mstring mcvt(char * i) - tmp = i; return i. The
 * vendor returns the INPUT (tmp is a dead store, h013 note above).
 */
fun mcvt(i: String): Mstring {
    val tmp = Mstring(i) // vendor: tmp = i;
    return Mstring(i) // vendor bug kept: return i; (not tmp)
}

/** vendor 2683: mstring mcvt(long i) - sprintf(a, "%ld", i); tmp = a. */
fun mcvt(i: Long): Mstring {
    val a = String.format(Locale.US, "%ld", i)
    return Mstring(a)
}

/** vendor 2691: mstring mcvt(double i) - sprintf(a, "%g", i); tmp = a. */
fun mcvt(i: Double): Mstring {
    val a = String.format(Locale.US, "%g", i)
    return Mstring(a)
}

/** vendor 2699: mstring mcvt(float i) - sprintf(a, "%g", i); tmp = a. */
fun mcvt(i: Float): Mstring {
    val a = String.format(Locale.US, "%g", i)
    return Mstring(a)
}

/** vendor 2707: mstring mcvt(int i) - sprintf(a, "%d", i); tmp = a. */
fun mcvt(i: Int): Mstring {
    val a = String.format(Locale.US, "%d", i)
    return Mstring(a)
}

// ---------------------------------------------------------------
// vendor 2715-2738: the cvt family (static buffers, h008 note)
// ---------------------------------------------------------------

/** vendor 2715: char * cvt(long i) - static char a[32]; sprintf "%ld". */
fun cvt(i: Long): String = String.format(Locale.US, "%ld", i)

/** vendor 2721: char * cvt(double i) - static char a[128]; sprintf "%g". */
fun cvt(i: Double): String = String.format(Locale.US, "%g", i)

/** vendor 2727: char * cvt(float i) - static char a[128]; sprintf "%g". */
fun cvt(i: Float): String = String.format(Locale.US, "%g", i)

/** vendor 2733: char * cvt(int i) - static char a[32]; sprintf "%d". */
fun cvt(i: Int): String = String.format(Locale.US, "%d", i)

// ---------------------------------------------------------------
// vendor 2871: global::operator()(const long int a)
// (the one operator() overload without a prior u31 seat)
// ---------------------------------------------------------------

/**
 * vendor 2871: sprintf(x, "%d", a); MakeGlobal(0, myname, "a", x).
 * The string/char-star families stay folded in the u31 vararg
 * invoke (GlobalCarrier.kt:87, verification above); this Long form
 * renders as its own overload because the vendor converts through a
 * fresh buffer first.
 */
operator fun Global.invoke(a: Long): GlobalRef {
    val x = String.format(Locale.US, "%d", a)
    makeGlobal(0, myname, "a", x)
    return GlobalRef(this, listOf(x))
}

// ---------------------------------------------------------------
// vendor 2932-2951: count
// ---------------------------------------------------------------

/**
 * vendor 2932: int count(char * key) - the CSV argument counter:
 * j starts at 1, each comma outside quotes bumps j; a quoted field
 * is skipped to its closing quote (the quote-skip i++ consumes the
 * opening quote before the walk, and the walk's own i++ consumes
 * the closing quote - both kept); an unterminated quote calls
 * ErrorMessage("mis-matched quote", 0).
 */
fun count(key: String): Int {
    var i = 0
    var j = 1
    val b = toCbuf(key)
    while (b[i] != NUL) {
        if (b[i] == ',') {
            j++
            i++
            continue
        }
        if (b[i] != '"') {
            i++
            continue
        }
        i++ // vendor: i++ before the closing-quote walk
        while (b[i] != '"' && b[i] != NUL) i++
        if (b[i] == NUL) {
            val msg = CharArray(64)
            writeCstring(msg, "mis-matched quote")
            ErrorMessage(msg, 0)
        }
        i++
    }
    return j
}

// ---------------------------------------------------------------
// vendor 2953-2959: HitRatio
// ---------------------------------------------------------------

/**
 * vendor 2953: double HitRatio(void) - ratio = svPtr->hit /
 * svPtr->count (C integer division of two longs, kept); both
 * counters reset to 0 AFTER the read, on every call.
 */
fun HitRatio(): Double {
    val ratio = (MdhState.svPtr.hit / MdhState.svPtr.count).toDouble()
    MdhState.svPtr.hit = 0
    MdhState.svPtr.count = 0
    return ratio
}

// ---------------------------------------------------------------
// vendor 2991-3001: global::Name + __test
// ---------------------------------------------------------------

/** vendor 2991: mstring global::Name() - tmp1 = Sref.c_str(). */
fun Global.Name(): Mstring = Mstring(Sref)

/**
 * vendor 2997: int __test() - return svPtr->tpx. The double
 * underscore is a vendor name (u124 __finish exception family,
 * journaled); narrows the vendor long tpx to int (h010).
 */
fun __test(): Int = MdhState.svPtr.tpx.toInt()

// ---------------------------------------------------------------
// vendor 3071-3111: the mstring::ReadLine family
// (global::ReadLine x3 deferred to the batch-8 row-236 owner)
// ---------------------------------------------------------------

/** stdin carrier (u158 Getstr2 reflective-fetch precedent). */
private fun stdinReader(): BufferedReader =
    BufferedReader(java.io.InputStreamReader(
        System::class.java.getField("in").get(null) as InputStream))

/**
 * vendor 3071: bool mstring::ReadLine() - fgets(tmp, STR_MAX,
 * stdin); strip the trailing newline; strip a second char if < ' '
 * (CR-LF form); data = tmp; return i != 0.
 */
fun Mstring.ReadLine(): Boolean {
    val line = stdinReader().readLine() ?: return false
    var t = line // h001: readLine excludes the terminator (built-in strip)
    if (t.isNotEmpty() && t[t.length - 1] < ' ') t = t.dropLast(1)
    value = t
    return true
}

/**
 * vendor 3083: bool mstring::ReadLine(FILE *u1) - the file-twin of
 * the stdin form (FILE* -> Reader, u113 ScanAlnum precedent).
 */
fun Mstring.ReadLine(u1: Reader): Boolean {
    val line = (u1 as? BufferedReader ?: BufferedReader(u1)).readLine() ?: return false
    var t = line
    if (t.isNotEmpty() && t[t.length - 1] < ' ') t = t.dropLast(1)
    value = t
    return true
}

/**
 * vendor 3095: bool mstring::ReadLine(istream &u1) - getline(tmp,
 * STR_MAX, newline); eof arm: data = ""; return false (kept BEFORE
 * the CR-LF strip, which only runs on a live line).
 */
fun Mstring.ReadLine(u1: Scanner): Boolean {
    if (!u1.hasNextLine()) {
        value = ""
        return false
    }
    var t = u1.nextLine()
    if (t.isNotEmpty() && t[t.length - 1] < ' ') t = t.dropLast(1)
    value = t
    return true
}

// ---------------------------------------------------------------
// vendor 3170-3204: zBlanks
// ---------------------------------------------------------------

/**
 * vendor 3170: mstring mstring::zBlanks() - build
 * "$zblanks" + xce + value + xcf at &tmp[1], zfcn(tmp, tmp2), and
 * return tmp2 (the zfcn result buffer). tmp[0] is never read
 * (quantized NUL, journal note).
 */
fun Mstring.zBlanks(): Mstring {
    val tmp = CharArray(STR_MAX)
    val tmp2 = CharArray(STR_MAX)

    writeCstring(tmp, DOLLAR.toString() + "zblanks", 1) // strcpy(&tmp[1], "$zblanks")
    strcatC(tmp, XCE.toString())
    val vb = CharArray(STR_MAX) // vendor: sprintf(tmp2, "%s", data.c_str())
    writeCstring(vb, value)
    strcatC(tmp, cstringAt(vb))
    strcatC(tmp, XCF.toString())

    zfcn(tmp, tmp2, MdhState.svPtr)
    return Mstring(cstringAt(tmp2))
}

/**
 * vendor 3186: mstring global::zBlanks() - RETRIEVE the global
 * (GLOBAL_NOT_FOUND throw, Cosine precedent); an EMPTY value
 * returns the mstring of -1; else the retrieved value's
 * mstring::zBlanks() seat above.
 */
fun Global.zBlanks(): Mstring {
    val gtmp = CharArray(STR_MAX)

    val f = mglobal(RETRIEVE, toCbuf(ref), gtmp, MdhState.svPtr)
    if (f == 0) {
        MdhState.globalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }

    if (strlenC(gtmp) == 0) return Mstring("-1")

    return Mstring(cstringAt(gtmp)).zBlanks()
}

// ---------------------------------------------------------------
// vendor 3206-3255: Ascii
// ---------------------------------------------------------------

/**
 * vendor 3206: int mstring::Ascii() - empty data returns -1; else
 * (int) *data.c_str() - SIGNED char on the reference platform
 * (bytes >= 0x80 sign-extend, h010).
 */
fun Mstring.Ascii(): Int {
    if (value == "") return -1
    val c = value[0].code
    return if (c > 127) c - 256 else c
}

/**
 * vendor 3212: int global::Ascii() - RETRIEVE (GLOBAL_NOT_FOUND
 * throw); empty returns -1; else (int) gtmp[0] over the UNSIGNED
 * char domain (0..255).
 */
fun Global.Ascii(): Int {
    val gtmp = CharArray(STR_MAX)

    val f = mglobal(RETRIEVE, toCbuf(ref), gtmp, MdhState.svPtr)
    if (f == 0) {
        MdhState.globalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }

    if (strlenC(gtmp) == 0) return -1
    return gtmp[0].code
}

/**
 * vendor 3228: int global::Ascii(int i) - i<=0 returns -1 BEFORE
 * the retrieve (kept); RETRIEVE (GLOBAL_NOT_FOUND throw); a value
 * shorter than i returns -1; else gtmp[i-1] (unsigned domain).
 */
fun Global.Ascii(i: Int): Int {
    if (i <= 0) return -1

    val gtmp = CharArray(STR_MAX)
    val f = mglobal(RETRIEVE, toCbuf(ref), gtmp, MdhState.svPtr)
    if (f == 0) {
        MdhState.globalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }

    if (strlenC(gtmp) < i) return -1
    return gtmp[i - 1].code
}

/**
 * vendor 3246: int mstring::Ascii(int i) - i<=0 returns -1;
 * strlen(data) < i returns -1; the data == "" test is DEAD after
 * the length test (a non-empty-length data is non-empty) - kept
 * verbatim as a comment-only note; else the i-1 byte, SIGNED char
 * (h010).
 */
fun Mstring.Ascii(i: Int): Int {
    if (i <= 0) return -1
    if (value.length < i) return -1
    // vendor: if (data == "") return -1;  [dead after the length test]
    val c = value[i - 1].code
    return if (c > 127) c - 256 else c
}

// ---------------------------------------------------------------
// vendor 3257-3273: the no-arg Length pair
// ---------------------------------------------------------------

/** vendor 3257: int mstring::Length() - strlen(data.c_str()). */
fun Mstring.Length(): Int = value.length

/**
 * vendor 3261: int global::Length() - RETRIEVE (GLOBAL_NOT_FOUND
 * throw); strlen of the retrieved value.
 */
fun Global.Length(): Int {
    val gtmp = CharArray(STR_MAX)

    val f = mglobal(RETRIEVE, toCbuf(ref), gtmp, MdhState.svPtr)
    if (f == 0) {
        MdhState.globalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }

    return strlenC(gtmp)
}

// ---------------------------------------------------------------
// vendor 3330/3354: global::Length needle twins (String carrier)
// ---------------------------------------------------------------

/**
 * vendor 3330 (string needle) + 3354 (char* needle): the two
 * argument types fold onto one Kotlin String seat (u209
 * GlobalData precedent). u145 walk (GlobalLength.kt): RETRIEVE
 * (GLOBAL_NOT_FOUND throw); empty haystack returns 0; the strstr
 * count walk - i starts at 1 BEFORE the loop (zero hits on a
 * non-empty haystack returns 1, "one piece"), advances by the
 * needle LENGTH (overlaps counted) - both kept verbatim.
 */
fun Global.Length(needle: String): Int {
    val gtmp = CharArray(STR_MAX)

    val f = mglobal(RETRIEVE, toCbuf(ref), gtmp, MdhState.svPtr)
    if (f == 0) {
        MdhState.globalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }

    val hay = cstringAt(gtmp)
    var i = 1
    if (hay.isEmpty()) return 0

    var p = 0
    while (true) {
        val hit = hay.indexOf(needle, p)
        if (hit < 0) return i
        i++
        p = hit + needle.length
    }
}

// ---------------------------------------------------------------
// vendor 3378/3392 + 3429: mstring::Length needle twins
// ---------------------------------------------------------------

/**
 * vendor 3378 (char* needle) + 3392 (string needle): folded onto
 * one String seat; the haystack is THIS mstring's value; the same
 * i-starts-at-1 / advance-by-needle-length walk as the u149
 * sibling (MstringLength.kt, which owns the Global-needle form).
 */
fun Mstring.Length(needle: String): Int {
    val hay = value
    var i = 1
    if (hay.isEmpty()) return 0

    var p = 0
    while (true) {
        val hit = hay.indexOf(needle, p)
        if (hit < 0) return i
        i++
        p = hit + needle.length
    }
}

/**
 * vendor 3429: mstring::Length(mstring needle) - the vendor's
 * strstr(p1, needle) applies the implicit mstring -> const char*
 * conversion (needle.c_str() carrier, u131 fold note); otherwise
 * the walk above.
 */
fun Mstring.Length(needle: Mstring): Int = Length(needle.value)

// ---------------------------------------------------------------
// vendor 3443-3449: Horolog
// ---------------------------------------------------------------

/**
 * vendor 3443: mstring Horolog() - _horolog(t); tmp = t. Raises
 * the _horolog stub (owner interp-sysfunc.cpp, batch-9 corpus
 * unit; the static char t[64] is fully re-seeded by _horolog, so
 * fresh scratch - h008).
 */
fun Horolog(): Mstring {
    val t = CharArray(64)
    mumps.interp._horolog(t)
    return Mstring(cstringAt(t))
}
