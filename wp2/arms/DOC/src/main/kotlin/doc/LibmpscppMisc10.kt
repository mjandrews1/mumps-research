package doc

// Unit 301 (B10 tranche 3): libmpscpp.cpp::<misc-8> (batch 10,
// complex stratum; units.csv row 281: loc 600, cr 0.029, ptr 58,
// ctl 38, score 283.0, rank 291). The global operator<< scalar
// tail (the unsigned short/long, unsigned long, float, double and
// string/mstring store arms), the ENTIRE operator>> read family
// (char*, global, the eight typed sscanf out-arms, string and
// mstring), the free xecute/WriteMumps1 pair, the mstring/global
// Pattern members, the top-level Piece and the mstring
// Piece/Justify/Extract/Find members, __command__, the
// StopInit/StopLookup/SYN/SynInit/Shred/ShredQuery adapters, the
// nodash/Score/sw alignment helpers, the bmg_fullsearch C++
// overload set, the mstring scalar constructors with c_str/s_str,
// the ostream/istream pair, and the entire operator* family (the
// seven member arms and the four scalar friend arms). All derived
// from the pristine vendor source this session (sha256
// 621deb...ffdbe).
//
// CHUNK BOUNDARY ORPHANS (the misc-chunk template): the dump HEAD
// is the orphan close brace of global::operator<<(short i) - its
// signature and body live at the misc-7 dump tail (ledgered there
// as the CHUNK TAIL note: the unsigned short/long stored arms are
// "the NEXT chunk's text"); this chunk opens with those very
// arms, rendered WHOLE here (the unit-202 head/tail precedent).
// The dump TAIL cuts global operator*(float, mstring k) AFTER its
// mult line - the arm is rendered WHOLE here (one-render rule);
// the vendor continuation (the const char* friend arms and beyond)
// is the NEXT chunk's text, ledger only.
//
// FOLDS per the arm rules: the std::string/char* twins of every
// family collapse onto the String seat (H-015); the mstring ctors
// with a file-scope static (the atexit/set_terminate comment
// block) ride the plain/data init H-013 ledger; the (string,string)
// bmg_fullsearch C++ arm's malloc/free dance folds onto the
// landed unit-305 C-core adapter (H-013, the bmgFullSearchStrM10
// cross-cite); Score is the landed SeqAlignExterns seat resolve
// (flip below); the typed operator>> sscanf conversions render
// via the scan helpers (H-019, the vendor sscanf lexes the
// leading C numeric token; zero conversions raise
// INVALID_CONVERSION). Consult H-014 hit-reuse,
// timestamp 2026-11-22T06:21:00Z.
//
// SEAT FLIPS this unit owns (the misc-chunk precedent, the
// unit-305 bmgFullSearch flip): Stubs.xecute(CharArray),
// Stubs.mstringTimes x7, Stubs.stopLookup, Stubs.SYN,
// Stubs.shredQuery and Stubs.nodash all flip to the renders
// below; Stubs.stopInit/Stubs.synInit already delegate to the
// landed String seats and stay unchanged.

// static char* literal adapter for the set_global/get_global
// (key, rslt) CharArray seats (the czM7 precedent, per-file here).
private fun czM10(s: String): CharArray {
    val b = CharArray(STR_MAX)
    CstringOps.copyAtS(b, 0, s)
    return b
}

// sscanf("%d"/"%hd"/"%ld"/"%lu"...) - the leading C integer token
// lex (H-019): optional whitespace, optional sign, digits. Zero
// digits -> null (the vendor sscanf zero-conversion count).
private fun scanIntM10(s: CharArray): Long? {
    var i = 0

    while (s[i] == ' ' || s[i] == '\t' || s[i] == '\n') i++

    var sign = 1L
    if (s[i] == '+' || s[i] == '-') {
        if (s[i] == '-') sign = -1L
        i++
    }

    var digits = 0
    var v = 0L
    while (s[i] in '0'..'9') {
        v = v * 10 + (s[i] - '0')
        digits++
        i++
    }

    if (digits == 0) return null
    return sign * v
}

// sscanf("%f"/"%lf") - the leading C float token lex (H-019):
// the integer lex plus an optional .frac and an optional e/E
// exponent. No digits at all -> null.
private fun scanDoubleM10(s: CharArray): Double? {
    var i = 0

    while (s[i] == ' ' || s[i] == '\t' || s[i] == '\n') i++

    var sign = 1.0
    if (s[i] == '+' || s[i] == '-') {
        if (s[i] == '-') sign = -1.0
        i++
    }

    var digits = 0
    val sb = StringBuilder()
    while (s[i] in '0'..'9') {
        sb.append(s[i])
        digits++
        i++
    }

    if (s[i] == '.') {
        sb.append(s[i])
        i++
        while (s[i] in '0'..'9') {
            sb.append(s[i])
            digits++
            i++
        }
    }

    var exponent = false
    if (s[i] == 'e' || s[i] == 'E') {
        val save = i
        exponent = true
        sb.append(s[i])
        i++
        if (s[i] == '+' || s[i] == '-') {
            sb.append(s[i])
            i++
        }
        var ed = 0
        while (s[i] in '0'..'9') {
            sb.append(s[i])
            ed++
            i++
        }
        if (ed == 0) {
            exponent = false
            sb.setLength(save)
        }
    }

    if (digits == 0) return null
    return sign * sb.toString().toDouble()
}

// ------------------------------------------------------------------
// global::operator<< scalar tail (the misc-7 CHUNK TAIL handoff)
// ------------------------------------------------------------------

// global & global::operator<<(unsigned short i) - VENDOR QUIRK:
// the sprintf format is "%hu" (kept); the unsigned short folds to
// the plain decimal digits (the M7 %ud precedent).
fun GlobalRef.shl(i: UShort): GlobalRef {
    Stubs.set_global(czM10(ref), czM10(i.toString()))
    return this
}

// global & global::operator<<(long i)
fun GlobalRef.shl(i: Long): GlobalRef {
    Stubs.set_global(czM10(ref), czM10(String.format(java.util.Locale.US, "%ld", i)))
    return this
}

// global & global::operator<<(unsigned long i) - the "%lu" fold
// to the unsigned decimal digits (the M7 precedent).
fun GlobalRef.shl(i: ULong): GlobalRef {
    Stubs.set_global(czM10(ref), czM10(i.toString()))
    return this
}

// global & global::operator<<(float i)
fun GlobalRef.shl(i: Float): GlobalRef {
    Stubs.set_global(czM10(ref), czM10(String.format(java.util.Locale.US, "%f", i)))
    return this
}

// global & global::operator<<(double i) - the vendor "%f"
// double-precision sprintf.
fun GlobalRef.shl(i: Double): GlobalRef {
    Stubs.set_global(czM10(ref), czM10(String.format(java.util.Locale.US, "%f", i)))
    return this
}

// global & global::operator<<(string s) - LEDGER: the String
// store arm IS the misc-7 shl(s: String) seat (one-render; the
// char*/string twins already collapsed there, H-015); not
// re-defined here.

// global & global::operator<<(mstring s)
fun GlobalRef.shl(s: Mstring): GlobalRef {
    Stubs.set_global(czM10(ref), czM10(s.str))
    return this
}

// ------------------------------------------------------------------
// global::operator>> - the read family
// ------------------------------------------------------------------

// shared read prefix: get_global of THIS global's Get() content
// with the tpx zero check (the M7 shl(g) precedent body, H-013
// factored only across the >> arms - each vendor arm repeats it).
private fun readCheckedM10(g: GlobalRef, s: CharArray) {
    Stubs.get_global(czM10(g.get()), s)
    if (Globals.svPtr.tpx == 0L) {
        Globals.GlobalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }
}

// char * global::operator>>(char * s)
fun GlobalRef.shr(s: CharArray): CharArray {
    readCheckedM10(this, s)
    return s
}

// global & global::operator>>(global g)
fun GlobalRef.shr(g: GlobalRef): GlobalRef {
    val s = CharArray(STR_MAX)
    readCheckedM10(this, s)
    Stubs.set_global(czM10(g.ref), s)
    return this
}

// the typed out-param holders (H-006 idiom): the C reference
// argument shape rendered as a mutable holder object.
class ShrIntM10 { var v: Int = 0 }
class ShrUIntM10 { var v: UInt = 0u }
class ShrLongM10 { var v: Long = 0 }
class ShrULongM10 { var v: ULong = 0u }
class ShrShortM10 { var v: Short = 0 }
class ShrUShortM10 { var v: UShort = 0u }
class ShrFloatM10 { var v: Float = 0f }
class ShrDoubleM10 { var v: Double = 0.0 }

// shared sscanf tail: the leading integer token with the
// INVALID_CONVERSION raise on zero conversions.
private fun shrConvertM10(s: CharArray): Long {
    val parsed = scanIntM10(s)
    if (parsed == null) {
        Globals.GlobalExceptionCode = INVALID_CONVERSION
        throw MumpsGlobalException()
    }
    return parsed
}

// int global::operator>>(int & i)
fun GlobalRef.shr(i: ShrIntM10): Int {
    val s = CharArray(STR_MAX)
    readCheckedM10(this, s)
    i.v = shrConvertM10(s).toInt()
    return i.v
}

// unsigned int global::operator>>(unsigned int & i) - VENDOR
// QUIRK: the sscanf format is "%d" (the SIGNED fmt on the
// unsigned out-param, kept byte-verbatim).
fun GlobalRef.shr(i: ShrUIntM10): UInt {
    val s = CharArray(STR_MAX)
    readCheckedM10(this, s)
    i.v = shrConvertM10(s).toUInt()
    return i.v
}

// long global::operator>>(long & i)
fun GlobalRef.shr(i: ShrLongM10): Long {
    val s = CharArray(STR_MAX)
    readCheckedM10(this, s)
    i.v = shrConvertM10(s)
    return i.v
}

// unsigned long global::operator>>(unsigned long & i)
fun GlobalRef.shr(i: ShrULongM10): ULong {
    val s = CharArray(STR_MAX)
    readCheckedM10(this, s)
    i.v = shrConvertM10(s).toULong()
    return i.v
}

// short global::operator>>(short & i)
fun GlobalRef.shr(i: ShrShortM10): Short {
    val s = CharArray(STR_MAX)
    readCheckedM10(this, s)
    i.v = shrConvertM10(s).toShort()
    return i.v
}

// unsigned short global::operator>>(unsigned short & i) - VENDOR
// QUIRK: the sscanf format is "%hd" (the SIGNED short fmt on the
// unsigned out-param, kept byte-verbatim).
fun GlobalRef.shr(i: ShrUShortM10): UShort {
    val s = CharArray(STR_MAX)
    readCheckedM10(this, s)
    i.v = shrConvertM10(s).toUShort()
    return i.v
}

// float global::operator>>(float & i)
fun GlobalRef.shr(i: ShrFloatM10): Float {
    val s = CharArray(STR_MAX)
    readCheckedM10(this, s)
    val d = scanDoubleM10(s)
    if (d == null) {
        Globals.GlobalExceptionCode = INVALID_CONVERSION
        throw MumpsGlobalException()
    }
    i.v = d.toFloat()
    return i.v
}

// double global::operator>>(double & i)
fun GlobalRef.shr(i: ShrDoubleM10): Double {
    val s = CharArray(STR_MAX)
    readCheckedM10(this, s)
    val d = scanDoubleM10(s)
    if (d == null) {
        Globals.GlobalExceptionCode = INVALID_CONVERSION
        throw MumpsGlobalException()
    }
    i.v = d
    return i.v
}

// string global::operator>>(string & i) - the out-param fold to
// the String return seat (H-015).
fun GlobalRef.shrStr(): String {
    val s = CharArray(STR_MAX)
    readCheckedM10(this, s)
    return CstringOps.cstring(s)
}

// mstring global::operator>>(mstring & i)
fun GlobalRef.shrM(): Mstring {
    val s = CharArray(STR_MAX)
    readCheckedM10(this, s)
    return Mstring(CstringOps.cstring(s))
}

// ------------------------------------------------------------------
// xecute / WriteMumps1
// ------------------------------------------------------------------

// int xecute(string exp) - the char* arm folds here (the identical
// body, H-015); the Stubs.xecute CharArray seat rides this via
// cstring.
fun xecute(exp: String): Int {
    val svPtr = Globals.svPtr
    svPtr.NOERR = 1
    val k = Stubs.Interpret(exp, svPtr)
    svPtr.ERROR = k
    return k
}

// int xecute(mstring exp)
fun xecute(exp: Mstring): Int {
    val svPtr = Globals.svPtr
    svPtr.NOERR = 1
    val k = Stubs.Interpret(exp.str, svPtr)
    svPtr.ERROR = k
    return k
}

// int xecute(char * exp)
fun xecute(exp: CharArray): Int =
    xecute(CstringOps.cstring(exp))

// int WriteMumps1(struct MSV * svPtr, char * exp) - the "write "
// prefix strcat; the passed svPtr (not the global one).
fun WriteMumps1(svPtr: Msv, exp: String): Int {
    val tmp = "write " + exp
    svPtr.NOERR = 1
    val k = Stubs.Interpret(tmp, svPtr)
    svPtr.ERROR = k
    return k
}

// ------------------------------------------------------------------
// Pattern members
// ------------------------------------------------------------------

// int mstring::Pattern(string p) - the const char* arm folds here
// (identical body, H-015).
fun Mstring.pattern(p: String): Int =
    Stubs.pm(czM10(str), czM10(p), Globals.svPtr)

// int mstring::Pattern(mstring & p)
fun Mstring.pattern(p: Mstring): Int =
    Stubs.pm(czM10(str), czM10(p.str), Globals.svPtr)

// int mstring::Pattern(global p) - the RETRIEVE of p's ref with
// the GLOBAL_NOT_FOUND throw, then the match against the buffer.
fun Mstring.pattern(p: GlobalRef): Int {
    val tmp = CharArray(STR_MAX)
    val f = Stubs.Mglobal(RETRIEVE, czM10(p.ref), tmp, Globals.svPtr)
    if (f == 0) {
        Globals.GlobalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }
    return Stubs.pm(czM10(str), tmp, Globals.svPtr)
}

// int global::Pattern(string p) - the RECEIVER is the probed
// global; the match pairs the retrieved buffer with p.
fun GlobalRef.pattern(p: String): Int {
    val tmp = CharArray(STR_MAX)
    val f = Stubs.Mglobal(RETRIEVE, czM10(ref), tmp, Globals.svPtr)
    if (f == 0) {
        Globals.GlobalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }
    return Stubs.pm(tmp, czM10(p), Globals.svPtr)
}

// int global::Pattern(mstring & p)
fun GlobalRef.pattern(p: Mstring): Int {
    val tmp = CharArray(STR_MAX)
    val f = Stubs.Mglobal(RETRIEVE, czM10(ref), tmp, Globals.svPtr)
    if (f == 0) {
        Globals.GlobalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }
    return Stubs.pm(tmp, czM10(p.str), Globals.svPtr)
}

// int global::Pattern(const char * p) - VENDOR QUIRK seat (the
// only Pattern arm of either class whose f==0 throw does NOT set
// GlobalExceptionCode first; kept byte-verbatim, hence the
// distinct pattern0 seat rather than the String fold).
fun GlobalRef.pattern0(p: CharArray): Int {
    val tmp = CharArray(STR_MAX)
    val f = Stubs.Mglobal(RETRIEVE, czM10(ref), tmp, Globals.svPtr)
    if (f == 0) throw MumpsGlobalException()
    return Stubs.pm(tmp, p, Globals.svPtr)
}

// ------------------------------------------------------------------
// Piece family
// ------------------------------------------------------------------

// char * Piece(const char * source, char * rslt, ...) - the
// top-level free Piece; jj<0 collapses to ii, the sprintf %d
// crumbs via the Locale.US digits (H-009), _piece with the empty
// modes and wild=0.
fun Piece(source: CharArray, rslt: CharArray, pat: CharArray, ii: Int, jjIn: Int): CharArray {
    var jj = jjIn

    if (jj < 0) jj = ii

    val cc1 = czM10(String.format(java.util.Locale.US, "%d", ii))
    val cc2 = czM10(String.format(java.util.Locale.US, "%d", jj))

    Stubs.Mpiece(rslt, source, pat, cc1, cc2, 0, czM10(""), Globals.svPtr)

    return rslt
}

// mstring mstring::Piece(const char *pat, int ii, int jj) - the
// char* arm IS this body (the String arm folds onto it, H-015;
// the two arms differ only in the needle fold); the receiver's
// own data is the SOURCE.
fun Mstring.piece(pat: String, ii: Int, jjIn: Int): Mstring {
    var jj = jjIn
    if (jj < 0) jj = ii

    val tmp = CharArray(STR_MAX)
    val cc1 = czM10(String.format(java.util.Locale.US, "%d", ii))
    val cc2 = czM10(String.format(java.util.Locale.US, "%d", jj))

    Stubs.Mpiece(tmp, czM10(str), czM10(pat), cc1, cc2, 0, czM10(""), Globals.svPtr)

    return Mstring(CstringOps.cstring(tmp))
}

// mstring mstring::Piece(mstring &pat, int ii, int jj)
fun Mstring.piece(pat: Mstring, ii: Int, jjIn: Int): Mstring =
    piece(pat.str, ii, jjIn)

// ------------------------------------------------------------------
// mstring Justify/Extract/Find members
// ------------------------------------------------------------------

// mstring mstring::Justify(int c1, int c2) - the unused locals
// i/j/k/x drop per H-013; _justify over the cc crumbs.
fun Mstring.justify(c1: Int, c2: Int): Mstring {
    val tmp = CharArray(STR_MAX)
    val cc1 = czM10(String.format(java.util.Locale.US, "%d", c1))
    val cc2 = czM10(String.format(java.util.Locale.US, "%d", c2))

    _justify(tmp, czM10(str), cc1, cc2)

    return Mstring(CstringOps.cstring(tmp))
}

// mstring mstring::Extract(int st, int ln) - the Mltoa crumbs and
// the _extract call.
fun Mstring.extract(st: Int, ln: Int): Mstring {
    val a = CharArray(40)
    val b = CharArray(40)
    val r = CharArray(STR_MAX)

    Stubs.Mltoa(st, a)
    Stubs.Mltoa(ln, b)

    _extract(r, czM10(str), a, b)

    return Mstring(CstringOps.cstring(r))
}

// int mstring::Find(const char * key, int st) - the char* arm IS
// this body (the mstring-key sibling folds onto it, H-015); the
// GLOBAL-key sibling is the landed MstringFind.kt seat and stays
// separate.
fun Mstring.find(key: String, st: Int): Int {
    val a = CharArray(40)
    val r = CharArray(20)

    Stubs.Mltoa(st, a)

    _find(r, czM10(str), czM10(key), a)

    return atoi(CstringOps.cstring(r))
}

// int mstring::Find(mstring key, int st)
fun Mstring.find(key: Mstring, st: Int): Int =
    find(key.str, st)

// ------------------------------------------------------------------
// __command__
// ------------------------------------------------------------------

// char * __command__(char * str) - strips the first and last byte
// (the enclosing quote pair); the static rslt is the return- by-
// value fold (H-013, the GC copy).
fun `__command__`(str: CharArray): String {
    val s = CstringOps.cstring(str)
    return s.substring(1, s.length - 1)
}

// ------------------------------------------------------------------
// StopInit / StopLookup / SYN / SynInit adapters
// ------------------------------------------------------------------

// void StopInit(char * file)
fun stopInitAdapterM10(file: CharArray) {
    stopInit(CstringOps.cstring(file))
}

// void StopInit(mstring file)
fun stopInitAdapterM10(file: Mstring) {
    stopInit(file.s_str())
}

// int StopLookup(mstring str)
fun stopLookupM10(str: Mstring): Int =
    stopLookupM10(str.s_str())

// int StopLookup(string str) - the stop.find idiom over the landed
// stopWords carrier.
fun stopLookupM10(str: String): Int =
    if (stopWords.contains(str)) 1 else 0

// int StopLookup(char * str)
fun stopLookupM10(str: CharArray): Int =
    stopLookupM10(CstringOps.cstring(str))

// char * SYN(char *word) - the bsearch over SynStatic.syn[0 until
// synx] via the landed cmpr (the manual binary search for the
// comparator bridge, H-014); a miss returns word unchanged.
fun synLookupM10(word: CharArray): CharArray {
    val w = CstringOps.cstring(word)
    var low = 0
    var high = SynStatic.synx - 1

    while (low <= high) {
        val mid = (low + high) / 2
        val c = cmpr(w, SynStatic.syn[mid])
        if (c == 0) return czM10(SynStatic.syn[mid].alias)
        if (c < 0) low = mid + 1 else high = mid - 1
    }

    return word
}

// mstring SYN(mstring word)
fun synLookupM10(word: Mstring): Mstring =
    Mstring(CstringOps.cstring(synLookupM10(czM10(word.str))))

// string SYN(string word) - the static tmp is the return-by-value
// fold (H-013).
fun synLookupM10(word: String): String =
    CstringOps.cstring(synLookupM10(czM10(word)))

// int SynInit(char * file)
fun synInitAdapterM10(file: CharArray): Int =
    synInit(CstringOps.cstring(file))

// int SynInit(mstring file)
fun synInitAdapterM10(file: Mstring): Int =
    synInit(file.s_str())

// ------------------------------------------------------------------
// Shred / ShredQuery adapters
// ------------------------------------------------------------------

// string Shred(string str, int size) and char* Shred(char*, int):
// BOTH arms are the landed batch-6 Shred.kt owner (the String
// signature IS its seat; H-015 one-render fold - no second
// definition here).

// mstring Shred(mstring str, int size) - the s_str ride over the
// landed Shred render.
fun shredAdapterM10(str: Mstring, size: Int): Mstring =
    Mstring(shred(str.s_str(), size))

// char * ShredQuery(char * str, int size) - the String seat holds
// the char* body (H-015): the retained file-scope cursor i (the
// vendor static int), the Shred probe on the advancing tail, the
// skip-ahead on the empty fragment.
private var shredQueryCursorM10 = 0

fun shredQueryM10(str: String, size: Int): String {
    var p1 = shred(str.substring(shredQueryCursorM10), size)

    if (p1.isEmpty()) {
        shredQueryCursorM10++

        if (shredQueryCursorM10 < size) {
            p1 = shred(str.substring(shredQueryCursorM10), size)
            return p1
        }

        shredQueryCursorM10 = 0
        return ""
    }

    return p1
}

// string ShredQuery(string str, int size) - folds onto the char*
// seat above (identical body, H-015); the Stubs.shredQuery String
// seat rides it.

// mstring ShredQuery(mstring str, int size)
fun shredQueryM10(str: Mstring, size: Int): Mstring =
    Mstring(shredQueryM10(str.s_str(), size))

// ------------------------------------------------------------------
// alignment helpers
// ------------------------------------------------------------------

// int nodash(char * a) - the non-dash count.
fun nodashAdapterM10(a: CharArray): Int {
    var i = 0
    var j = 0

    while (a[j] != NUL) {
        if (a[j] != '-') i++
        j++
    }

    return i
}

// inline int Score(char a, char b) - the flip lands in the
// SeqAlignExterns.kt seat itself (the raised forward-tag stub
// resolves to THIS unit's owner; the GAP/MATCH/MISMATCH file
// scope lives right below it).

// int sw(mstring s, mstring t, ...) and the string twin - the
// ShowAlign/ShowMatrix ints ride the landed Boolean flags (H-007);
// the C core is the landed unit-61 Sw.kt render.
fun swAdapterM10(
    s: Mstring, t: Mstring,
    ShowAlign: Int, ShowMatrix: Int,
    gap: Int, mismatch: Int, match: Int
): Int =
    sw(czM10(s.str), czM10(t.str), ShowAlign != 0, ShowMatrix != 0, gap, mismatch, match)

fun swAdapterM10(
    s: String, t: String,
    ShowAlign: Int, ShowMatrix: Int,
    gap: Int, mismatch: Int, match: Int
): Int =
    sw(czM10(s), czM10(t), ShowAlign != 0, ShowMatrix != 0, gap, mismatch, match)

// ------------------------------------------------------------------
// bmg_fullsearch C++ overload set - the (string,string) arm with
// its malloc/ErrorMessage dance folds onto the landed unit-305
// adapter bmgFullSearchStrM10 (H-013); every arm below ends at
// that same C-core seat.
// ------------------------------------------------------------------

// int bmg_fullsearch(mstring a, mstring buf)
fun bmgFullSearchAdapterM10(a: Mstring, buf: Mstring): Int =
    bmgFullSearchStrM10(a.s_str(), buf.s_str())

// int bmg_fullsearch(mstring a, global g) - the (string&)g cast
// rides the Stubs.getGlobalString seat (H-014, raised there).
fun bmgFullSearchAdapterM10(a: Mstring, g: GlobalRef): Int =
    bmgFullSearchStrM10(a.s_str(), Stubs.getGlobalString(g))

// int bmg_fullsearch(char * a, global g)
fun bmgFullSearchAdapterM10(a: CharArray, g: GlobalRef): Int =
    bmgFullSearchStrM10(CstringOps.cstring(a), Stubs.getGlobalString(g))

// int bmg_fullsearch(string a, global g)
fun bmgFullSearchAdapterM10(a: String, g: GlobalRef): Int =
    bmgFullSearchStrM10(a, Stubs.getGlobalString(g))

// ------------------------------------------------------------------
// mstring scalar constructors, c_str/s_str, ostream/istream
// ------------------------------------------------------------------

// mstring::mstring(const char*/const string&/const mstring&) and
// the default: the Carriers Mstring(content)/Mstring() constructor
// seats ARE these arms (H-015 one-render; the atexit/
// set_terminate comment block is dead text, H-013).

// mstring::mstring(long a)
fun mstringCtorM10(a: Long): Mstring =
    Mstring(String.format(java.util.Locale.US, "%ld", a))

// mstring::mstring(int a)
fun mstringCtorM10(a: Int): Mstring =
    Mstring(String.format(java.util.Locale.US, "%d", a))

// mstring::mstring(double a) - VENDOR QUIRK: the sprintf format is
// "%lf" (the l length modifier is a no-op for f in printf; the
// "%f" fold, kept noted).
fun mstringCtorM10(a: Double): Mstring =
    Mstring(String.format(java.util.Locale.US, "%f", a))

// mstring::mstring(float a)
fun mstringCtorM10(a: Float): Mstring =
    Mstring(String.format(java.util.Locale.US, "%f", a))

// mstring::mstring(char a)
fun mstringCtorM10(a: Char): Mstring =
    Mstring(a.toString())

// const char * mstring::c_str() - the Carriers toString/c_str
// seats ARE this arm (one-render, not re-defined).

// const string mstring::s_str()
fun Mstring.s_str(): String = str

// ostream & operator<<(ostream & c, mstring i) - the stream seat
// renders as the System.out print (H-012, the ostream carrier
// fold); the operator>> istream twin has NO reader seat and lands
// as a ledger row only (H-004).
fun ostreamAdapterM10(i: Mstring) {
    System.out.print(i.str)
}

// ------------------------------------------------------------------
// operator* - the seven member arms and the four scalar friends
// ------------------------------------------------------------------

// shared member tail: mult of the receiver with the cvt/str
// operand into the 64-byte product (the STR_MAX arms aside).
private fun timesM10(a: String, b: String): Mstring {
    val t1 = CharArray(64)

    mult(czM10(a), czM10(b), t1)

    return Mstring(CstringOps.cstring(t1))
}

// mstring mstring::operator*(int i) - the const char* arm folds
// here (identical body, H-015).
fun Mstring.times(i: Int): Mstring = timesM10(str, cvt(i))

// mstring mstring::operator*(long i)
fun Mstring.times(i: Long): Mstring = timesM10(str, cvt(i))

// mstring mstring::operator*(double i)
fun Mstring.times(i: Double): Mstring = timesM10(str, cvt(i))

// mstring mstring::operator*(float i)
fun Mstring.times(i: Float): Mstring = timesM10(str, cvt(i))

// mstring mstring::operator*(mstring i)
fun Mstring.times(i: Mstring): Mstring = timesM10(str, i.str)

// mstring mstring::operator*(string i)
fun Mstring.times(i: String): Mstring = timesM10(str, i)

// mstring mstring::operator*(global i) - VENDOR QUIRK: NO tpx
// check between the get_global and the mult (the only global-
// operand arm of the family without the raise); the mult takes
// the read buffer as BOTH operands (the vendor t1 aliasing, kept
// H-012), so the fold rides the STR_MAX product.
fun Mstring.times(i: GlobalRef): Mstring {
    val t1 = CharArray(STR_MAX)

    Stubs.get_global(czM10(i.get()), t1)

    mult(czM10(str), t1, t1)

    return Mstring(CstringOps.cstring(t1))
}

// mstring operator*(int i, mstring k)
fun mstringTimesM10(i: Int, k: Mstring): Mstring = timesM10(cvt(i), k.str)

// mstring operator*(long i, mstring k)
fun mstringTimesM10(i: Long, k: Mstring): Mstring = timesM10(cvt(i), k.str)

// mstring operator*(double i, mstring k)
fun mstringTimesM10(i: Double, k: Mstring): Mstring = timesM10(cvt(i), k.str)

// mstring operator*(float i, mstring k) - the dump TAIL cuts this
// arm after its mult line; rendered WHOLE (the one-render rule).
fun mstringTimesM10(i: Float, k: Mstring): Mstring = timesM10(cvt(i), k.str)
