package both

// libmpscpp.cpp::<misc-8> - roster 306, batch B10, both_seq 183,
// cpp, complex stratum, loc 600, score 283.0. The misc residue
// chunk 8 of libmpscpp.cpp: the global shift-in/shift-out family
// (operator<< at 6652-6697, operator>> at 6699-6887), the xecute
// trio and WriteMumps1 (7053-7093), the Pattern wrappers
// (7095-7180), the Piece/Justify/Extract/Find member wrappers
// (7284-7690), __command__ (7711), the StopLookup/SYN lookup
// wrappers (7763-7826), ShredQuery (7962-7988), nodash/Score
// (8041-8056), the bmg_fullsearch overload heads (8355-8386),
// the mstring constructor block (8421-8487), c_str/s_str
// (8488-8497), the stream operators (8707-8717), and the
// mstring operator* family (member 9125-9188, friend 9388-9417
// partial). The chunk is residue, NOT vendor-contiguous: claimed
// functions (sw at 7850, the global::operator= block, the
// Piece/Justify/Extract/Find big bodies already seated by their
// own units) interleave between these faces.
//
// Consults: h019 hit (the mstring/global class members render
// package-level with the receiver state explicit); h014 hit (the
// flip set below: extractValue, xecute, stopLookupZb and the five
// LibmpscppMisc3 mstring backend stubs land with this unit; the
// kernel stubs pm/_find/sw/add/sub/mult stay stubbed, owners
// pending); h007 reuse (the aliased t1,t1 buffer in operator*
// (global) 9173); h013 hit (global::Pattern(const char *) 7169
// throws WITHOUT setting GlobalExceptionCode - the string/mstring
// twins set it; the fold keeps the code-set variant and journals
// the quirk); h016 reuse (buffer sizes kept: s[32] scratch,
// STR_MAX fetch buffers); h017 reuse (vendor names kept where
// free: WriteMumps1, StopInit, SynInit, Shred, sw, Piece; the
// extension overloads seat next to their B9 twins); h004 hit (the
// GAP/MISMATCH/MATCH TU globals and the ShredQuery static cursor
// seat as file-scope carriers); h020 reuse (the malloc/fail path
// of bmg_fullsearch(string,string) 8377 quantizes onto GC; the
// sscanf strict-parse carriers below distinguish no-conversion
// from a parsed zero).
//
// Boundary ledgers:
// - The chunk OPENS with a stray closing brace: the tail of
//   global::operator<<(unsigned int) (vendor 6643-6647), whose
//   head belongs to the previous chunk. Not rendered here.
// - The chunk CLOSES mid-face inside friend operator*(float i,
//   mstring k) (vendor 9412-9417): the else-less mult body is
//   unambiguous and rendered whole; the *(string)/(global)/
//   (const char *) friends (9420-9444) and the whole < and >
//   comparator families (9504-9615) belong to roster 120
//   (libmpscpp.cpp::<misc-9>, batch 4 pending).
//
// COVERAGE FINDING for the PI (cross-batch observation): the B9
// misc-3 render seated mstring::operator=(double) (vendor 8731,
// sprintf percent-lf) as mstringFromDouble via the cFormatG
// percent-g fold. THIS unit's mstring ctor(double) at 8457 uses
// the same percent-lf format; it renders verbatim as the %f
// String.format idiom. The two vendor sites share a format; the
// two arm seats differ (percent-g vs percent-f). Journaled for
// the PI's cross-batch consistency review, no silent change made
// to the closed B9 seat.
//
// Fold ledgers (cross-cited seats, no new Kotlin):
// - StopInit(char *) 7720 / StopInit(mstring) 7725 fold to the
//   unit-166 seat StopInit(file: String) (StopInit.kt:52; the
//   delegating heads were ledgered there at that render).
// - SynInit(char *) 7810 / SynInit(mstring) 7815 fold to the
//   unit-146 seat SynInit(file: String) (SynInit.kt:70).
// - Shred(string,int) 7899 / Shred(mstring,int) 7903 fold to the
//   unit-182 seat Shred(str,size) (Shred.kt:58).
// - sw(mstring,mstring,...) 8145 / sw(string,string,...) 8149
//   fold to the sw seat (BothStubs.kt:468, roster 61).
// - bmg_fullsearch(mstring,mstring) 8355 (the s_str pair) folds
//   to the unit-302 seat bmgFullsearch (InterpBmgsubsMisc.kt:115);
//   bmg_fullsearch(string,string) 8377 (the malloc/ErrorMessage
//   choreography) folds to the same seat with the h020
//   quantization ledger above.

// ------------------------------------------------------------------------
// TU global carriers (h004)
// ------------------------------------------------------------------------

// int GAP / MISMATCH / MATCH (libmpscpp.cpp:8036-8038, the
// Smith-Waterman penalty globals; the definition lines sit in the
// inter-chunk gap before this chunk's nodash/Score residue, but
// Score at 8049 - IN this chunk - is their reader and the sw unit
// (roster 61) owns the writes; seated here as the definition-site
// carrier, zero-initialized like the vendor's static storage).
internal var GAP = 0 // gap penalty
internal var MISMATCH = 0 // mis-match penalty
internal var MATCH = 0 // match reward

// ------------------------------------------------------------------------
// global::operator<< - the shift-in family (set_global writes)
// ------------------------------------------------------------------------

// global & global::operator<<(unsigned short i) - 6652. The
// set_global macro fold seats at GlobalRef.assignG (the
// LibmpscppMisc3.kt:276 setGlobalC3 delegate); the %hu format
// folds to UShort.toString.
internal fun GlobalRef.globalShlUShort(i: UShort): GlobalRef =
    assignG(i.toString())

// global & global::operator<<(long i) - 6659 (%ld)
internal fun GlobalRef.globalShlLong(i: Long): GlobalRef =
    assignG(i.toString())

// global & global::operator<<(unsigned long i) - 6666 (%lu)
internal fun GlobalRef.globalShlULong(i: ULong): GlobalRef =
    assignG(i.toString())

// global & global::operator<<(float i) - 6673 (%f; the float
// promotes to double at the vararg edge, kept via the toDouble)
internal fun GlobalRef.globalShlFloat(i: Float): GlobalRef =
    assignG(String.format(java.util.Locale.US, "%f", i.toDouble()))

// global & global::operator<<(double i) - 6680 (%f; the
// LibmpscppMisc6.kt:469 String.format idiom)
internal fun GlobalRef.globalShlDouble(i: Double): GlobalRef =
    assignG(String.format(java.util.Locale.US, "%f", i))

// global & global::operator<<(string s) 6687 / operator<<(mstring
// s) 6692 - folded (identical set_global(ref, s.c_str()) bodies
// after the String carrier).
internal fun GlobalRef.globalShlString(s: String): GlobalRef =
    assignG(s)

// ------------------------------------------------------------------------
// global::operator>> - the shift-out family. Every variant does
// get_global (RETRIEVE via getGlobalC) with the GLOBAL_NOT_FOUND
// fail-throw, then either returns the string or strict-parses it
// (sscanf; a no-conversion parse throws INVALID_CONVERSION -
// the parse carriers below distinguish that from a parsed zero,
// which atolC cannot).
// ------------------------------------------------------------------------

// The >> (char *) 6699, >> (string &) 6859 and >> (mstring &) 6872
// variants fold into the GlobalRef.extractValue seat (BothStubs)
// - FLIPPED real this batch (see that file); this file's typed
// readers share its kernel shape.

// global & global::operator>>(global g) - libmpscpp.cpp:6710:
// fetch own value (fail-throw), then set_global into g.
internal fun GlobalRef.globalShrGlobal(g: GlobalRef): GlobalRef {
    val s = extractValue()
    g.assignG(s)
    return this
}

// sscanf carriers (private): C %ld semantics - skip leading
// whitespace, optional sign, digits; null when no conversion.
private fun parseLongZb(s: String): Long? {
    var i = 0
    while (i < s.length && s[i].isWhitespace()) i++
    val sign = when {
        i < s.length && (s[i] == '+' || s[i] == '-') -> {
            val neg = s[i] == '-'
            i++
            neg
        }
        else -> false
    }
    val start = i
    var v = 0L
    while (i < s.length && s[i] in '0'..'9') {
        v = v * 10 + (s[i] - '0')
        i++
    }
    if (i == start) return null
    return if (sign) -v else v
}

// C %f/%lf semantics - whitespace, sign, digits, optional
// fraction, optional exponent; null when no conversion.
private fun parseDoubleZb(s: String): Double? {
    var i = 0
    while (i < s.length && s[i].isWhitespace()) i++
    var neg = false
    if (i < s.length && (s[i] == '+' || s[i] == '-')) {
        neg = s[i] == '-'
        i++
    }
    val start = i
    var v = 0.0
    while (i < s.length && s[i] in '0'..'9') {
        v = v * 10 + (s[i] - '0')
        i++
    }
    if (i < s.length && s[i] == '.') {
        i++
        var f = 0.1
        while (i < s.length && s[i] in '0'..'9') {
            v += (s[i] - '0') * f
            f /= 10
            i++
        }
    }
    if (i == start) return null
    // the C exponent tail (e/E, sign, digits) - strtod accepts it;
    // sscanf %f accepts it too
    if (i < s.length && (s[i] == 'e' || s[i] == 'E')) {
        val save = i
        i++
        var eneg = false
        if (i < s.length && (s[i] == '+' || s[i] == '-')) {
            eneg = s[i] == '-'
            i++
        }
        val estart = i
        var e = 0
        while (i < s.length && s[i] in '0'..'9') {
            e = e * 10 + (s[i] - '0')
            i++
        }
        if (i > estart) {
            v *= Math.pow(10.0, (if (eneg) -e else e).toDouble())
        } else {
            i = save // no exponent digits: the tail is not consumed
        }
    }
    return if (neg) -v else v
}

private fun GlobalRef.fetchOrThrow(): String {
    val s = CharArray(STR_MAX)
    val f = getGlobalC(this.ref, s, svPtr)
    if (f == 0) {
        GlobalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }
    return cStringOf(s)
}

private fun convOrThrow(v: Long?): Long {
    if (v == null) {
        GlobalExceptionCode = INVALID_CONVERSION
        throw MumpsGlobalException()
    }
    return v
}

private fun convOrThrowD(v: Double?): Double {
    if (v == null) {
        GlobalExceptionCode = INVALID_CONVERSION
        throw MumpsGlobalException()
    }
    return v
}

// int global::operator>>(int & i) - libmpscpp.cpp:6723 (sscanf %d;
// the parsed long truncates to the 32-bit int, kept).
internal fun GlobalRef.globalShrInt(): Int =
    convOrThrow(parseLongZb(fetchOrThrow())).toInt()

// unsigned int global::operator>>(unsigned int & i) - 6740.
// VENDOR QUIRK (kept): the format is %d - SIGNED - parsed into an
// unsigned int; the parsed long truncates to 32 bits then wraps
// unsigned.
internal fun GlobalRef.globalShrUInt(): UInt =
    convOrThrow(parseLongZb(fetchOrThrow())).toInt().toUInt()

// long global::operator>>(long & i) - libmpscpp.cpp:6757 (%ld)
internal fun GlobalRef.globalShrLong(): Long =
    convOrThrow(parseLongZb(fetchOrThrow()))

// unsigned long global::operator>>(unsigned long & i) - 6774
// (%lu; a negative parse wraps modulo 2^64, the C two's-complement
// wrap kept by the toULong fold).
internal fun GlobalRef.globalShrULong(): ULong =
    convOrThrow(parseLongZb(fetchOrThrow())).toULong()

// short global::operator>>(short & i) - libmpscpp.cpp:6791 (%hd;
// the 16-bit truncation kept)
internal fun GlobalRef.globalShrShort(): Short =
    convOrThrow(parseLongZb(fetchOrThrow())).toInt().toShort()

// unsigned short global::operator>>(unsigned short & i) - 6808
// (%hu; low-16-bits wrap kept)
internal fun GlobalRef.globalShrUShort(): UShort =
    (convOrThrow(parseLongZb(fetchOrThrow())).toInt() and 0xFFFF).toUShort()

// float global::operator>>(float & i) - libmpscpp.cpp:6825 (%f)
internal fun GlobalRef.globalShrFloat(): Float =
    convOrThrowD(parseDoubleZb(fetchOrThrow())).toFloat()

// double global::operator>>(double & i) - libmpscpp.cpp:6842 (%lf)
internal fun GlobalRef.globalShrDouble(): Double =
    convOrThrowD(parseDoubleZb(fetchOrThrow()))

// ------------------------------------------------------------------------
// xecute / WriteMumps1 - the Interpret thin heads
// ------------------------------------------------------------------------

// int xecute(string) 7053 / xecute(mstring) 7063 / xecute(char *)
// 7073 - identical bodies; the fold lands in the FLIPPED
// BothCRuntime.kt xecute seat (TODO-stub removed this batch; the
// old pin said "not a corpus unit" - WRONG, the family is this
// unit's residue; corrected at the flip). Cross-cited here, no
// second definition.

// int WriteMumps1(struct MSV * svPtr, char * exp) - 7083: the
// write(...) macro target; tmp = "write " + exp (the char
// tmp[STR_MAX] init + strcat quantizes onto the String concat;
// the STR_MAX cap is the h020 note), then the xecute shape.
internal fun WriteMumps1(sv: Msv, exp: String): Int {
    val tmp = "write " + exp
    sv.NOERR = 1
    val k = Interpret(tmp, sv)
    sv.ERROR = k
    return k
}

// ------------------------------------------------------------------------
// Pattern wrappers (the pm kernel stub, BothStubs.kt:204)
// ------------------------------------------------------------------------

// int mstring::Pattern(string p) 7095 / Pattern(mstring & p) 7099 /
// Pattern(const char * p) 7115 - folded (identical pm(data, p)
// bodies after the String carrier).
internal fun mstringPattern(data: String, p: String): Int {
    return pm(carr8(data), carr8(p), svPtr)
}

// int mstring::Pattern(global p) - libmpscpp.cpp:7103: fetch the
// pattern global (RETRIEVE; f==0 throws GLOBAL_NOT_FOUND), then
// pm(data, tmp).
internal fun mstringPattern(data: String, p: GlobalRef): Int {
    val tmp = CharArray(STR_MAX)
    val f = Mglobal(RETRIEVE, p.ref.toCharArray(), tmp, svPtr)
    if (f == 0) {
        GlobalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }
    return pm(carr8(data), tmp, svPtr)
}

// int global::Pattern(string p) 7142 / Pattern(mstring & p) 7156 /
// Pattern(const char * p) 7169 - folded. The receiver global's
// OWN value is the pm subject. h013 quirk kept in the ledger: the
// const char * variant at 7169 throws WITHOUT setting
// GlobalExceptionCode (the string/mstring twins set it); the fold
// keeps the code-set variant and journals the difference (the
// throw is observable either way; the code only matters to a
// catcher).
internal fun GlobalRef.globalPattern(p: String): Int {
    val tmp = CharArray(STR_MAX)
    val f = Mglobal(RETRIEVE, ref.toCharArray(), tmp, svPtr)
    if (f == 0) {
        GlobalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }
    return pm(tmp, carr8(p), svPtr)
}

// ------------------------------------------------------------------------
// Piece / Justify / Extract / Find member wrappers
// ------------------------------------------------------------------------

// char * Piece(const char * source, char * rslt, const char * pat,
// int ii, int jj) - libmpscpp.cpp:7284 (the free function; the
// rslt out-param quantizes into the return). jj<0 -> jj=ii BEFORE
// the counter format; _pieceZb is the roster-114 stub.
internal fun Piece(source: String, pat: String, ii: Int, jj: Int): String {
    val jj2 = if (jj < 0) ii else jj
    val rslt = CharArray(STR_MAX)
    _pieceZb(
        rslt, carr8(source), pat, ii.toString(), jj2.toString(),
        0, "", svPtr,
    )
    return cStringOf(rslt)
}

// mstring mstring::Piece(const char * pat, ii, jj) 7301 /
// Piece(string pat, ii, jj) 7317 / Piece(mstring & pat, ii, jj)
// 7355 - folded (identical _piece bodies; the B9 unit-283 face
// mstringPiece(data, pat: GlobalRef, ii, jj) keeps the global
// variant; this overload seats beside it).
internal fun mstringPiece(data: String, pat: String, ii: Int, jj: Int): String {
    val jj2 = if (jj < 0) ii else jj
    val tmp = CharArray(STR_MAX)
    _pieceZb(
        tmp, carr8(data), pat, ii.toString(), jj2.toString(),
        0, "", svPtr,
    )
    return cStringOf(tmp)
}

// mstring mstring::Justify(int c1, int c2) - libmpscpp.cpp:7397:
// sprintf %d both counters, then the _justify seat (Justify.kt:38,
// the roster-278 walk this batch released).
internal fun mstringJustify(data: String, c1: Int, c2: Int): String {
    val tmp = CharArray(STR_MAX)
    _justify(tmp, carr8(data), carr8(c1.toString()), carr8(c2.toString()))
    return cStringOf(tmp)
}

// mstring mstring::Extract(int st, int ln) - libmpscpp.cpp:7535:
// Mltoa both bounds (the roster-295 seat this batch released),
// then the _extract seat (Extract.kt:43).
internal fun mstringExtract(data: String, st: Int, ln: Int): String {
    val a = CharArray(40)
    val b = CharArray(40)
    Mltoa(st.toLong(), a)
    Mltoa(ln.toLong(), b)
    val r = CharArray(STR_MAX)
    _extract(r, data, cStringOf(a), cStringOf(b))
    return cStringOf(r)
}

// int mstring::Find(const char * key, int st) 7638 / Find(mstring
// key, int st) 7671 - folded (identical bodies; the Find(global)
// variant at 7649 is NOT in this chunk). Mltoa the start, _find
// (roster-6 stub), atoi the result.
internal fun mstringFind(data: String, key: String, st: Int): Int {
    val a = CharArray(40)
    val r = CharArray(20)
    Mltoa(st.toLong(), a)
    _find(r, carr8(data), carr8(key), a)
    return atolC(cStringOf(r)).toInt()
}

// ------------------------------------------------------------------------
// __command__ / StopLookup / SYN / ShredQuery / nodash / Score
// ------------------------------------------------------------------------

// char * __command__(char * str) - libmpscpp.cpp:7711: strip the
// first and last byte (the quote pair the command(ref) and
// Xecute macros pass through). The static rslt buffer quantizes
// onto the String return (the reuse hazard is the caller's, h020
// ledger); a str shorter than two bytes hits the
// IndexOutOfBounds quantization of the vendor UB.
internal fun commandZb(str: String): String {
    return str.substring(1, str.length - 1)
}

// int StopLookup(mstring str) 7763 / StopLookup(string str) 7767 /
// StopLookup(char * str) 7777 - folded (the mstring twin's s_str()
// is the identity on the String carrier; the char * twin's tmp
// coercion is the carrier itself). The stop set is the unit-166
// stopWords carrier (StopInit.kt:50). Vendor returns 0/1; the
// Boolean fold is journaled.
internal fun stopLookupC(str: String): Boolean {
    return str in stopWords
}

// char * SYN(char * word) 7788 / SYN(mstring word) 7798 /
// SYN(string word) 7804 - folded (the string twin's STATIC local
// tmp is a reentrancy hazard journaled away by the String
// return). bsearch over SynTable.syn[0..synx-1] (the unit-146
// carriers, SynInit.kt:66-67) with cmpr (LibmpscppMisc3.kt:327);
// a miss returns the word itself, a hit returns the alias.
internal fun synC(word: String): String {
    var lo = 0L
    var hi = SynTable.synx - 1
    while (lo <= hi) {
        val mid = (lo + hi) / 2
        val e = SynTable.syn[mid.toInt()] ?: break
        val c = cmpr(word, e)
        if (c == 0) return e.alias
        if (c < 0) hi = mid - 1 else lo = mid + 1
    }
    return word
}

// char * ShredQuery(char * str, int size) 7970 / ShredQuery(string)
// 7962 / ShredQuery(mstring) 7966 - the char * body carries the
// STATIC i cursor (TU-persistent walk state; seated as the
// file-scope carrier below), the string/mstring twins delegate to
// it (fold). NOTE: the BifsZfcn 4-arg mdh wrapper ShredQuery(sv,
// cmd, s, size) is a DIFFERENT vendor face (kept there, cited).
private var shredQueryI = 0 // static int i = 0 (function-local static)

internal fun shredQueryC(str: String, size: Int): String {
    val p1 = Shred(str.substring(shredQueryI), size)
    if (p1.isEmpty()) {
        shredQueryI++
        if (shredQueryI < size) {
            return Shred(str.substring(shredQueryI), size)
        }
        shredQueryI = 0
        return ""
    }
    return p1
}

// int nodash(char * a) - libmpscpp.cpp:8041: count the non-dash
// bytes.
internal fun nodashC(a: String): Int {
    var i = 0
    for (c in a) if (c != '-') i++
    return i
}

// inline int Score(char a, char b) - libmpscpp.cpp:8049: the
// Smith-Waterman score cell over the TU globals above.
internal fun scoreC(a: Char, b: Char): Int {
    if (a == '-' || b == '-') return GAP
    if (a == b) return MATCH
    return MISMATCH
}

// ------------------------------------------------------------------------
// bmg_fullsearch overload heads (the native seat is the unit-302
// bmgFullsearch, InterpBmgsubsMisc.kt:115)
// ------------------------------------------------------------------------

// int bmg_fullsearch(mstring a, global g) 8359 / (char * a,
// global g) 8365 / (string a, global g) 8371 - folded: the
// (string &) g cast folds to extractValue (the roster-306 flipped
// seat), then the native call.
internal fun bmgFullsearchG(a: String, g: GlobalRef): Int =
    bmgFullsearch(a, g.extractValue())

// ------------------------------------------------------------------------
// mstring constructor block (8421-8487). The atexit(__finish) /
// set_terminate(thrownMessage) registration lines are COMMENTED
// OUT in the vendor source (kept as dead comments, not rendered).
// The name member of the copy ctor has no carrier (h019): the
// mstring name field is ledgered at MstringClassMisc.kt; the copy
// face returns data and journals the name pass-through.
// ------------------------------------------------------------------------

// mstring::mstring(const char * a) 8421 / mstring(const string & a)
// 8434 - folded (identical data = a bodies).
internal fun mstringCtorC(a: String): String = a

// mstring::mstring(const mstring & a) - libmpscpp.cpp:8427 (name =
// a.name; data = a.data; the name member is the h019 ledger
// above).
internal fun mstringCtorCopy(data: String): String = data

// mstring::mstring(long a) - libmpscpp.cpp:8441 (sprintf %ld)
internal fun mstringCtorLong(a: Long): String = a.toString()

// mstring::mstring(int a) - libmpscpp.cpp:8449 (sprintf %d)
internal fun mstringCtorInt(a: Int): String = a.toString()

// mstring::mstring(double a) - libmpscpp.cpp:8457 (sprintf %lf -
// the %f idiom; see the COVERAGE FINDING ledger in the header for
// the mstringFromDouble percent-g divergence).
internal fun mstringCtorDouble(a: Double): String =
    String.format(java.util.Locale.US, "%f", a)

// mstring::mstring(float a) - libmpscpp.cpp:8465 (sprintf %f)
internal fun mstringCtorFloat(a: Float): String =
    String.format(java.util.Locale.US, "%f", a.toDouble())

// mstring::mstring(char a) - libmpscpp.cpp:8473 (tmp[0]=a; tmp[1]=0)
internal fun mstringCtorChar(a: Char): String = a.toString()

// mstring::mstring() - libmpscpp.cpp:8482 (data = "")
internal fun mstringCtor(): String = ""

// const char * mstring::c_str() - libmpscpp.cpp:8488 (returns
// data; the identity face, pinned for the lattice crosswalk).
internal fun mstringCStr(data: String): String = data

// const string mstring::s_str() - libmpscpp.cpp:8492: THE s_str
// seat (the LibmpscppMisc4 || faces call it; the identity body
// makes their String-carrier round trip exact).
internal fun mstringSStrZb(data: String): String = data

// ostream & operator<<(ostream &, mstring) 8707 / istream &
// operator>>(istream &, mstring &) 8712 - NO Kotlin faces: the
// iostream carriers have no arm seats (h001; the MstringClassMisc
// ReadLine precedent). The ostream face writes i.data to the
// stream; the istream face reads a token into i.data. Ledgered
// for the promotion review, not rendered.

// ------------------------------------------------------------------------
// mstring operator* - the member family (the * lattice belongs to
// THIS chunk, not misc-4; see the LibmpscppMisc4 crosswalk)
// ------------------------------------------------------------------------

// mstring mstring::operator*(int i) 9125 / *(long i) 9133 /
// *(double i) 9141 / *(float i) 9149 - cvt then the mult kernel
// stub (Cannon.kt:44).
internal fun mstringTimes(data: String, i: Int): String {
    val t1 = CharArray(MS_SMALL)
    mult(carr8(data), carr8(cvt(i)), t1)
    return cStringOf(t1)
}

internal fun mstringTimes(data: String, i: Long): String {
    val t1 = CharArray(MS_SMALL)
    mult(carr8(data), carr8(cvt(i)), t1)
    return cStringOf(t1)
}

internal fun mstringTimes(data: String, i: Double): String {
    val t1 = CharArray(MS_SMALL)
    mult(carr8(data), carr8(cvt(i)), t1)
    return cStringOf(t1)
}

internal fun mstringTimes(data: String, i: Float): String {
    val t1 = CharArray(MS_SMALL)
    mult(carr8(data), carr8(cvt(i)), t1)
    return cStringOf(t1)
}

// mstring mstring::operator*(mstring i) 9157 / *(string i) 9165 /
// *(const char * i) 9182 - folded.
internal fun mstringTimes(data: String, i: String): String {
    val t1 = CharArray(MS_SMALL)
    mult(carr8(data), carr8(i), t1)
    return cStringOf(t1)
}

// mstring mstring::operator*(global i) - libmpscpp.cpp:9173: the
// ALIASED call mult(data, t1, t1) (h007); the STR_MAX fetch buffer
// is both input and output; no tpx check (kept).
internal fun mstringTimes(data: String, i: GlobalRef): String {
    val t1 = CharArray(STR_MAX)
    getGlobalC(i.ref, t1, svPtr)
    mult(carr8(data), t1, t1)
    return cStringOf(t1)
}

// ------------------------------------------------------------------------
// mstring operator* - the friend family (reversed). *(int) 9388 /
// *(long) 9396 / *(double) 9404 complete; *(float) 9412 is the
// chunk's closing BOUNDARY-CUT face (rendered whole, see the
// header ledger); the *(string)/(global)/(const char *) friends
// belong to roster 120.
// ------------------------------------------------------------------------

internal fun mstringTimesRev(i: Int, k: String): String {
    val t1 = CharArray(MS_SMALL)
    mult(carr8(cvt(i)), carr8(k), t1)
    return cStringOf(t1)
}

internal fun mstringTimesRev(i: Long, k: String): String {
    val t1 = CharArray(MS_SMALL)
    mult(carr8(cvt(i)), carr8(k), t1)
    return cStringOf(t1)
}

internal fun mstringTimesRev(i: Double, k: String): String {
    val t1 = CharArray(MS_SMALL)
    mult(carr8(cvt(i)), carr8(k), t1)
    return cStringOf(t1)
}

// mstring operator*(float i, mstring k) - libmpscpp.cpp:9412
// (boundary-cut face; the body's mult(cvt(i), k.data) tail is
// unambiguous).
internal fun mstringTimesRev(i: Float, k: String): String {
    val t1 = CharArray(MS_SMALL)
    mult(carr8(cvt(i)), carr8(k), t1)
    return cStringOf(t1)
}

// ------------------------------------------------------------------------
// Carriers (private)
// ------------------------------------------------------------------------

private const val MS_SMALL = 64

private fun carr8(s: String): CharArray {
    val a = CharArray(MS_SMALL)
    writeC(a, s)
    return a
}
