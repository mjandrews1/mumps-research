package mumps.mdh

import mumps.interp.Msv
import mumps.interp.NUL
import mumps.interp._extract
import mumps.interp._find
import mumps.interp._justify
import mumps.interp.atoiC
import mumps.interp.cstringAt
import mumps.interp.interpret
import mumps.interp.mglobal
import mumps.interp.mult
import mumps.interp.strlenC
import mumps.interp.writeCstring

/**
 * PRO-arm translation of libmpscpp.cpp::<misc-8> (batch 10 unit,
 * roster row 281, file rank 291). Translated independently from
 * the pristine vendor source (mumps-14-may-2026.tgz, sha256
 * 621deb25...ffdbe); the eighth 600-physLOC residual chunk of
 * libmpscpp.cpp (identity re-verified via the study tool: loc 600,
 * comment_ratio 0.029, pointer_marks 58, control_marks 38, score
 * 283.0 - exact match to wp1/study-subset/units.csv row 281).
 * The residual line list interleaves the non-claimed lines and
 * the under-min_loc small bodies of libmpscpp.cpp (selection
 * order preserved); the chunk carries: the shift-in /
 * shift-out (insertion and extraction) operator tails of the
 * interpreter class global, the xecute and WriteMumps1 bodies,
 * the mstring::Pattern x4 and global::Pattern x3 families, the
 * free Piece and the mstring::Piece x3 / Justify / Extract /
 * Find x2 wrappers, __command__, the StopInit/StopLookup/SYN/
 * SynInit/Shred/ShredQuery/nodash/Score/sw/bmg_fullsearch
 * wrapper families, the mstring constructor / c_str / s_str /
 * stream operator set, and the mstring operator* member and
 * friend families (chunk-boundary fossils per the u62
 * precedent: the slice opens at the residual closing brace of
 * global::operator<<(short) and closes inside the friend
 * operator*(float, mstring) body, complete construct rendered).
 *
 * STUB RESOLUTIONS this unit carries (MdhStubs): xecute
 * (TAG CORRECTION: the \\\"not a corpus unit\\\" tag in the stub
 * note was wrong - the three vendor bodies at 7053-7082 are
 * this chunk's carrying lines; the String overload renders the
 * core), StopLookup x3 (7777 set-membership family), SYN
 * (7788 bsearch over the SynInit.kt syn/synx carriers, the
 * extern-C cmpr at 7773 folded inline), ShredQuery (7970 core
 * with the persistent static-index state), nodash (8041), and
 * Score (8049, binding the AlignState gap/match/mismatch
 * seats); the bmg_fullsearch (String,String) stub STAYS - the
 * real search machine remains out of corpus and the chunk's
 * (string,string) malloc-copy-free wrapper body renders as an
 * identity fold over it (h013 note).
 *
 * VERIFICATION SEATS this chunk shares with row 249: the
 * global shift-in / shift-out families (vendor 6650-6882) were
 * front-run by LibmpscppMisc3.kt (insertUShort through
 * insertMstring, readInto x4 and extractInt through
 * readInto(Mstring)); the bodies are verified line-for-line
 * against this chunk's carrying lines - no redeclaration
 * (one-owner policy h004; tag-correction note above the
 * Misc3 header belongs to this roster row). The mstring
 * constructor owner is THIS unit: the Mstring.kt numeric
 * constructor seats are corrected here (Double/Float to the
 * vendor %lf/%f fixed-decimal emitters; h014 minimal-binding
 * notes superseded), and the copy/char-star constructor bodies
 * land there with it (TAG CORRECTION, batch-10 row 281).
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h008 static_scratch   : the vendor static char rslt of
 *    __command__ and static string tmp of SYN(string) return
 *    by static buffer - rendered as fresh String values (the
 *    C caller-aliasing dance is unobserved in the corpus; Max
 *    unit 88 precedent); ShredQuery's persistent static int i
 *    (7972) stays file-scope state (shredQueryI below), the
 *    only true cross-call carrier in the chunk.
 *  - h011 control_flow     : the one-line membership ladders
 *    render as single-expression bodies keeping the vendor
 *    0/1 returns; the if/else shapes are not re-associated.
 *  - h012 unsigned_byte_domain: every vendor char* parameter
 *    rides a CharArray byte view; string identity is folded
 *    at the Kotlin String domain with a verification note.
 *  - h013 dead_store       : the unused i, j=1, k, x locals of
 *    mstring::Justify, the unused j of ShredQuery, the bmg
 *    malloc/free copy dance and the commented atexit/
 *    set_terminate fossils of the constructor bodies are
 *    dropped with these notes, not translated.
 *  - h014 out_of_scope_call: _piece (MdhStubs), _extract and
 *    _find (interp seats), _justify (interp seat), Mltoa
 *    (MdhStubs stub, owner roster row 292 lands this batch),
 *    pm (MdhStubs stub), get_global (resolved macro seat),
 *    mult (interp seat), cvt (Misc6 seats), interpret (interp
 *    seat) and the bmg_fullsearch core stub ride unchanged;
 *    the sw core is the Sw.kt owner.
 *  - h015 grammar_collision: bsearch(word, syn, ...) renders
 *    as an inline binary search with the vendor cmpr folded;
 *    the friend operator*(T, mstring) forms have Kotlin
 *    extension-operator points and are kept as operators;
 *    (string &) g - the global's string base - folds to the
 *    ref buffer read as a C string.
 *  - h017 naming          : Piece/Justify/Extract/Find/
 *    Pattern/StopInit/StopLookup/SYN/SynInit/Shred/
 *    ShredQuery/nodash/Score/sw/bmg_fullsearch/xecute/
 *    WriteMumps1/__command__ keep the vendor names
 *    (__command__/__symput precedent, unit 97); the shift
 *    families keep the Misc3 insert/readInto seats.
 *
 * Vendor notes (translated faithfully, not \\\"fixed\\\"):
 *  - global::Pattern(const char *) throws without setting
 *    GlobalExceptionCode (the other Pattern bodies do) - kept
 *    verbatim.
 *  - mstring::operator*(global i) multiplies into the same
 *    t1 buffer it just read the global into (mult(data, t1,
 *    t1) aliasing) - kept verbatim, fossil note.
 *  - The vendor 64-byte t1 staging buffers of the scalar
 *    operator* bodies are kept at 64 chars (the C overflow is
 *    a bounded-array exception here, h012 note).
 *  - The free Piece and the mstring::Piece family clamp
 *    jj<0 to jj=ii before the %d emitters, exactly as there.
 *  - mstring::Find reads its _find result through atoi; the
 *    unsigned copies of the wrappers are not re-typed.
 *  - StopLookup returns int 0/1 in C; the resolved stub seat
 *    is Boolean (u97 fold precedent) - both shapes noted.
 */

private const val STR_MAX = 32000 // vendor strmax.h:3 (file-private, u44 precedent)
private const val GLOBAL_NOT_FOUND = 2 // vendor libmpscpp.h:71
private const val RETRIEVE = 0 // vendor defines.h:83

private fun toCbuf(s: String): CharArray {
    val b = CharArray(s.length + 1)
    writeCstring(b, s)
    return b
}

// ---------------------------------------------------------------------------
// verification section: the global shift-in / shift-out families
// (vendor 6650-6882) were front-run by LibmpscppMisc3.kt (row 249
// render: insertUShort/insertLong/insertULong/insertFloat/
// insertDouble/insertString/insertMstring, readInto x4, extractInt
// through readInto(Mstring)) - the carrying lines of THIS chunk are
// verified line-for-line against those seats; no redeclaration
// (h004 one-owner, TAG CORRECTION journaled).
// ---------------------------------------------------------------------------

// ---------------------------------------------------------------------------
// vendor 7053-7082: xecute x3 (the MdhStubs String stub resolves
// here; Mstring and char* overloads add around it), 7083: WriteMumps1.
// ---------------------------------------------------------------------------

/** vendor 7053: int xecute(string exp) - NOERR=1; Interpret; ERROR=k. */
fun xecute(exp: String): Int {
    val svPtr: Msv = MdhState.svPtr
    svPtr.NOERR = 1
    val k = interpret(exp, svPtr)
    svPtr.ERROR = k
    return k
}

/** vendor 7063: int xecute(mstring exp) - delegates via c_str(). */
fun xecute(exp: Mstring): Int = xecute(exp.value)

/** vendor 7073: int xecute(char * exp) - the char* core form. */
fun xecute(exp: CharArray): Int = xecute(cstringAt(exp))

/** vendor 7083: int WriteMumps1(struct MSV * svPtr, char * exp) -
 *  "write " + exp through Interpret. */
fun WriteMumps1(svPtr: Msv, exp: CharArray): Int {
    val tmp = cstringAt(exp) // h012: char tmp[STR_MAX]="write "; strcat
    val k = interpret("write " + tmp, svPtr)
    svPtr.ERROR = k
    return k
}

// ---------------------------------------------------------------------------
// vendor 7095-7179: the Pattern families. mstring receiver x4, then
// the global receiver x3 (each global form retrieves via
// Mglobal(RETRIEVE,...) and gates GLOBAL_NOT_FOUND before pm()).
// ---------------------------------------------------------------------------

/** vendor 7095: int mstring::Pattern(string p). */
fun Mstring.Pattern(p: String): Int = pm(toCbuf(value), toCbuf(p), MdhState.svPtr)

/** vendor 7099: int mstring::Pattern(mstring & p). */
fun Mstring.Pattern(p: Mstring): Int = pm(toCbuf(value), toCbuf(p.value), MdhState.svPtr)

/** vendor 7103: int mstring::Pattern(global p) - retrieve + gate. */
fun Mstring.Pattern(p: Global): Int {
    val svPtr: Msv = MdhState.svPtr
    val tmp = CharArray(STR_MAX)
    val refBuf = CharArray(STR_MAX)
    writeCstring(refBuf, p.ref)
    val f = mglobal(RETRIEVE, refBuf, tmp, svPtr)
    if (f == 0) {
        MdhState.globalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }
    return pm(toCbuf(value), tmp, svPtr)
}

/** vendor 7115: int mstring::Pattern(const char * p). */
fun Mstring.Pattern(p: CharArray): Int = pm(toCbuf(value), p, MdhState.svPtr)

/** vendor 7142: int global::Pattern(string p) - retrieve + gate. */
fun Global.Pattern(p: String): Int {
    val svPtr: Msv = MdhState.svPtr
    val tmp = CharArray(STR_MAX)
    val refBuf = CharArray(STR_MAX)
    writeCstring(refBuf, ref)
    val f = mglobal(RETRIEVE, refBuf, tmp, svPtr)
    if (f == 0) {
        MdhState.globalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }
    return pm(tmp, toCbuf(p), svPtr)
}

/** vendor 7156: int global::Pattern(mstring & p) - retrieve + gate. */
fun Global.Pattern(p: Mstring): Int {
    val svPtr: Msv = MdhState.svPtr
    val tmp = CharArray(STR_MAX)
    val refBuf = CharArray(STR_MAX)
    writeCstring(refBuf, ref)
    val f = mglobal(RETRIEVE, refBuf, tmp, svPtr)
    if (f == 0) {
        MdhState.globalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }
    return pm(tmp, toCbuf(p.value), svPtr)
}

/** vendor 7169: int global::Pattern(const char * p) - retrieve,
 *  gate; NOTE the vendor throws here WITHOUT setting
 *  GlobalExceptionCode (kept verbatim). */
fun Global.Pattern(p: CharArray): Int {
    val svPtr: Msv = MdhState.svPtr
    val tmp = CharArray(STR_MAX)
    val refBuf = CharArray(STR_MAX)
    writeCstring(refBuf, ref)
    val f = mglobal(RETRIEVE, refBuf, tmp, svPtr)
    if (f == 0) throw MumpsGlobalException()
    return pm(tmp, p, svPtr)
}

// ---------------------------------------------------------------------------
// vendor 7170-7231: the free Piece and the mstring::Piece wrappers
// (the global::Piece x4 family is separate study units - not carried);
// then Justify (7397), Extract (7535) and Find x2 (7638, 7671).
// ---------------------------------------------------------------------------

/** vendor 7170: char * Piece(source, rslt, pat, ii, jj) - jj<0 clamps
 *  to ii; %d emitters collapse; _piece with setpiece 0 and the empty lhs. */
fun Piece(source: CharArray, rslt: CharArray, pat: CharArray, ii: Int, jj: Int): CharArray {
    val jj2 = if (jj < 0) ii else jj // vendor: if (jj<0) jj=ii
    val cc1 = toCbuf(ii.toString()) // vendor char cc1[65]
    val cc2 = toCbuf(jj2.toString()) // vendor char cc2[64]
    val lhs = CharArray(1) // vendor (unsigned char *) ""
    _piece(rslt, source, pat, cc1, cc2, 0, lhs, MdhState.svPtr)
    return rslt
}

/** vendor 7301: mstring mstring::Piece(const char *pat, int ii, int jj). */
fun Mstring.Piece(pat: CharArray, ii: Int, jj: Int): Mstring {
    val jj2 = if (jj < 0) ii else jj
    val tmp = CharArray(STR_MAX)
    val cc1 = toCbuf(ii.toString())
    val cc2 = toCbuf(jj2.toString())
    val lhs = CharArray(1)
    _piece(tmp, toCbuf(value), pat, cc1, cc2, 0, lhs, MdhState.svPtr)
    return Mstring(cstringAt(tmp)) // vendor: tmp1=tmp
}

/** vendor 7317: mstring mstring::Piece(string pat, int ii, int jj). */
fun Mstring.Piece(pat: String, ii: Int, jj: Int): Mstring {
    val jj2 = if (jj < 0) ii else jj
    val tmp = CharArray(STR_MAX)
    val cc1 = toCbuf(ii.toString())
    val cc2 = toCbuf(jj2.toString())
    val lhs = CharArray(1)
    _piece(tmp, toCbuf(value), toCbuf(pat), cc1, cc2, 0, lhs, MdhState.svPtr)
    return Mstring(cstringAt(tmp))
}

/** vendor 7355: mstring mstring::Piece(mstring &pat, int ii, int jj). */
fun Mstring.Piece(pat: Mstring, ii: Int, jj: Int): Mstring {
    val jj2 = if (jj < 0) ii else jj
    val tmp = CharArray(STR_MAX)
    val cc1 = toCbuf(ii.toString())
    val cc2 = toCbuf(jj2.toString())
    val lhs = CharArray(1)
    _piece(tmp, toCbuf(value), toCbuf(pat.value), cc1, cc2, 0, lhs, MdhState.svPtr)
    return Mstring(cstringAt(tmp))
}

/** vendor 7397: mstring mstring::Justify(int c1, int c2) - the
 *  unused i, j=1, k, x locals are h013 dead stores (dropped). */
fun Mstring.Justify(c1: Int, c2: Int): Mstring {
    val tmp = CharArray(STR_MAX)
    val cc1 = toCbuf(c1.toString()) // vendor char cc1[32]
    val cc2 = toCbuf(c2.toString()) // vendor char cc2[32]
    _justify(tmp, toCbuf(value), cc1, cc2)
    return Mstring(cstringAt(tmp)) // vendor: tmp1=tmp
}

/** vendor 7535: mstring mstring::Extract(int st, int ln) - Mltoa
 *  stub rides until row 292 lands (h014). */
fun Mstring.Extract(st: Int, ln: Int): Mstring {
    val a = CharArray(40)
    val b = CharArray(40)
    val r = CharArray(STR_MAX)
    Mltoa(st.toLong(), a)
    Mltoa(ln.toLong(), b)
    _extract(r, toCbuf(value), a, b)
    return Mstring(cstringAt(r)) // vendor: rr=r
}

/** vendor 7638: int mstring::Find(const char * key, int st). */
fun Mstring.Find(key: CharArray, st: Int): Int {
    val a = CharArray(40)
    val r = CharArray(20)
    Mltoa(st.toLong(), a)
    _find(r, toCbuf(value), key, a)
    return atoiC(r)
}

/** vendor 7671: int mstring::Find(mstring key, int st). */
fun Mstring.Find(key: Mstring, st: Int): Int {
    val a = CharArray(40)
    val r = CharArray(20)
    Mltoa(st.toLong(), a)
    _find(r, toCbuf(value), toCbuf(key.value), a)
    return atoiC(r)
}

// ---------------------------------------------------------------------------
// vendor 7711-8057: the command/stoplist/synonym/shred/sw/bmg block.
// ---------------------------------------------------------------------------

/** vendor 7711: char * __command__(char * str) - drops the first
 *  and last char; the static rslt buffer is an h008 fold. */
fun __command__(str: CharArray): String {
    val s = cstringAt(str, 1) // vendor: strcpy(rslt, str+1)
    return s.substring(0, s.length - 1) // vendor: rslt[strlen-1]=0
}

/** vendor 7741: void StopInit(char * file) - String-owning core
 *  is the StopInit.kt seat (unit 174). */
fun StopInit(file: CharArray) {
    StopInit(cstringAt(file))
}

/** vendor 7745: void StopInit(mstring file). */
fun StopInit(file: Mstring) {
    StopInit(file.s_str())
}

/** vendor 7763: int StopLookup(mstring str). */
fun StopLookup(str: Mstring): Boolean = StopLookup(str.s_str())

/** vendor 7767: int StopLookup(string str) - the MdhStubs stub
 *  resolves here (find/end 0/1 - Boolean u97 fold noted). */
fun StopLookup(str: String): Boolean = stop.contains(str)

/** vendor 7777: int StopLookup(char * str) - same membership shape. */
fun StopLookup(str: CharArray): Boolean = stop.contains(cstringAt(str))

/**
 * vendor 7773 cmpr folded inline (extern "C" int cmpr(const char * a,
 * const Syn * b) { return strcmp(a, (*b).word); }); bsearch over
 * the SynInit.kt syn[0..synx) carriers, h015.
 */
private fun synBsearch(word: String): SynEntry? {
    var lo = 0
    var hi = synx.toInt() - 1
    while (lo <= hi) {
        val mid = (lo + hi) / 2
        val c = word.compareTo(syn[mid].word) // the cmpr strcmp
        if (c == 0) return syn[mid]
        if (c < 0) hi = mid - 1 else lo = mid + 1
    }
    return null
}

/** vendor 7788: char * SYN(char * word) - core resolve of the
 *  MdhStubs stub (returns the word itself when bsearch misses). */
fun SYN(word: String): String {
    val p = synBsearch(word) ?: return word
    return p.alias
}

/** vendor 7798: mstring SYN(mstring word). */
fun SYN(word: Mstring): Mstring {
    val tmp = Mstring()
    tmp.value = SYN(word.value) // vendor: tmp=SYN((char*)word.c_str())
    return tmp
}

/** vendor 7806: string SYN(string word) - static tmp is an h008 fold. */
fun SYN(word: CharArray): String = SYN(cstringAt(word))

/** vendor 7810: int SynInit(char * file) - String core is the
 *  SynInit.kt seat (unit 126). */
fun SynInit(file: CharArray): Int = SynInit(cstringAt(file))

/** vendor 7814: int SynInit(mstring file). */
fun SynInit(file: Mstring): Int = SynInit(file.s_str())

/** vendor 7951: string Shred(string, int) - the Shred.kt String seat
 *  IS the core form (unit 164); verification note, no redeclaration. */

/** vendor 7955: mstring Shred(mstring str, int size). */
fun Shred(str: Mstring, size: Int): Mstring {
    val tmp = Shred(str.value, size)
    return Mstring(tmp)
}

/** vendor 7959: string ShredQuery(string, int) - delegates to the
 *  core below (identity fold in the String domain). */
fun ShredQuery(str: String, size: Int): String = ShredQueryCore(str, size)

/** vendor 7963: mstring ShredQuery(mstring str, int size). */
fun ShredQuery(str: Mstring, size: Int): Mstring {
    val tmp = ShredQuery(str.value, size)
    return Mstring(tmp)
}

/** vendor 7970: char * ShredQuery(char * str, int size) - the
 *  persistent static int i rides as shredQueryI (h008); the unused
 *  static j is an h013 dead store. The MdhStubs (String,Int) stub
 *  resolves via the String overload above. */
private var shredQueryI = 0 // vendor 7972: static int i=0

private fun ShredQueryCore(str: String, size: Int): String {
    val p1 = Shred(str.substring(shredQueryI), size) // vendor: Shred(&str[i],size)
    if (p1.isEmpty()) { // vendor: strlen(p1)==0
        shredQueryI++
        if (shredQueryI < size) {
            return Shred(str.substring(shredQueryI), size)
        }
        shredQueryI = 0
        return ""
    }
    return p1
}

/** vendor 8041: int nodash(char * a) - the stub resolves here. */
fun nodash(a: CharArray): Int {
    var i = 0
    var k = 0
    while (a[k] != NUL) { // vendor: for (; *a!=0; a++)
        if (a[k] != '-') i++
        k++
    }
    return i
}

/** vendor 8049: inline int Score(char a, char b) - the stub
 *  resolves here; GAP/MATCH/MISMATCH bind the AlignState seats. */
fun Score(a: Char, b: Char): Int {
    if (a == '-' || b == '-') return AlignState.gap
    if (a == b) return AlignState.match
    return AlignState.mismatch
}

/** vendor 8062: int sw(mstring, mstring, ...) - core is the Sw.kt seat. */
fun sw(
    s: Mstring,
    t: Mstring,
    ShowAlign: Int,
    ShowMatrix: Int,
    gap: Int,
    mismatch: Int,
    match: Int,
): Int = sw(toCbuf(s.value), toCbuf(t.value), ShowAlign, ShowMatrix, gap, mismatch, match)

/** vendor 8066: int sw(string, string, ...). */
fun sw(
    s: String,
    t: String,
    ShowAlign: Int,
    ShowMatrix: Int,
    gap: Int,
    mismatch: Int,
    match: Int,
): Int = sw(toCbuf(s), toCbuf(t), ShowAlign, ShowMatrix, gap, mismatch, match)

/** vendor 8373: int bmg_fullsearch(mstring a, mstring buf) - the
 *  (String,String) MdhStubs stub carries the out-of-corpus search. */
fun bmg_fullsearch(a: Mstring, buf: Mstring): Int = bmg_fullsearch(a.s_str(), buf.s_str())

/** vendor 8377: int bmg_fullsearch(mstring a, global g) - (string &)
 *  g reads the global string base ref (String carrier). */
fun bmg_fullsearch(a: Mstring, g: Global): Int =
    bmg_fullsearch(a.s_str(), g.ref)

/** vendor 8382: int bmg_fullsearch(char * a, global g). */
fun bmg_fullsearch(a: CharArray, g: Global): Int =
    bmg_fullsearch(cstringAt(a), g.ref)

/** vendor 8387: int bmg_fullsearch(string a, global g). */
fun bmg_fullsearch(a: String, g: Global): Int =
    bmg_fullsearch(a, g.ref)

// (vendor 8392) int bmg_fullsearch(string a, string buf): the C body
// malloc-copies both strings, calls the char* core and frees - an
// identity fold over the (String,String) stub in the Kotlin String
// domain; the malloc/NULL/ErrorMessage(__LINE__)/free dance is h013,
// and the stub stays owner-of-the-core (verification note).

// ---------------------------------------------------------------------------
// vendor 8417-8758 tail of this chunk: the mstring constructor set
// (owner correction in Mstring.kt - TAG CORRECTION batch-10 row 281;
// the atexit/set_terminate fossils are h013), c_str/s_str seats
// (verification; Mstring.kt), and the stream operator pair.
// ---------------------------------------------------------------------------

/** vendor 8519: ostream & operator<<(ostream & c, mstring i) -
 *  mirror of the Global.streamInsert fold (unit-31 precedent). */
fun Mstring.streamInsert(out: java.io.PrintStream): java.io.PrintStream {
    out.print(value) // vendor: c << i.data
    return out
}

/** vendor 8523: istream & operator>>(istream & c, mstring & i) -
 *  token extraction into data (h001 fold; the BufferedReader
 *  plays the istream edge). */
fun Mstring.streamExtract(c: java.io.BufferedReader): java.io.BufferedReader {
    val sb = StringBuilder()
    var ch = c.read()
    while (ch == ' '.code || ch == '\n'.code || ch == '\t'.code || ch == '\r'.code) ch = c.read()
    while (ch >= 0 && ch != ' '.code && ch != '\n'.code && ch != '\t'.code && ch != '\r'.code) {
        sb.append(ch.toChar())
        ch = c.read()
    }
    value = sb.toString() // vendor: c >> i.data
    return c
}

// ---------------------------------------------------------------------------
// vendor 9233-9418: the mstring operator* member family (char
// t1[64] staging kept at 64, h012 note; the global form's
// read-and-multiply aliasing kept verbatim) and the friend
// operator*(T, mstring) x4 family (the chunk closes inside the
// float friend's body; the complete construct renders, u62
// boundary precedent).
// ---------------------------------------------------------------------------

/** vendor 9233: mstring mstring::operator*(int i) - cvt(Int) + mult. */
operator fun Mstring.times(i: Int): Mstring {
    val tmp = Mstring()
    val t1 = CharArray(64)
    mult(toCbuf(value), 0, toCbuf(cvt(i)), 0, t1, 0)
    tmp.value = cstringAt(t1)
    return tmp
}

/** vendor 9240: mstring mstring::operator*(long i) - cvt(Long). */
operator fun Mstring.times(i: Long): Mstring {
    val tmp = Mstring()
    val t1 = CharArray(64)
    mult(toCbuf(value), 0, toCbuf(cvt(i)), 0, t1, 0)
    tmp.value = cstringAt(t1)
    return tmp
}

/** vendor 9247: mstring mstring::operator*(double i) - cvt(Double). */
operator fun Mstring.times(i: Double): Mstring {
    val tmp = Mstring()
    val t1 = CharArray(64)
    mult(toCbuf(value), 0, toCbuf(cvt(i)), 0, t1, 0)
    tmp.value = cstringAt(t1)
    return tmp
}

/** vendor 9254: mstring mstring::operator*(float i) - cvt(Float). */
operator fun Mstring.times(i: Float): Mstring {
    val tmp = Mstring()
    val t1 = CharArray(64)
    mult(toCbuf(value), 0, toCbuf(cvt(i)), 0, t1, 0)
    tmp.value = cstringAt(t1)
    return tmp
}

/** vendor 9261: mstring mstring::operator*(mstring i). */
operator fun Mstring.times(i: Mstring): Mstring {
    val tmp = Mstring()
    val t1 = CharArray(64)
    mult(toCbuf(value), 0, toCbuf(i.value), 0, t1, 0)
    tmp.value = cstringAt(t1)
    return tmp
}

/** vendor 9268: mstring mstring::operator*(string i). */
operator fun Mstring.times(i: String): Mstring {
    val tmp = Mstring()
    val t1 = CharArray(64)
    mult(toCbuf(value), 0, toCbuf(i), 0, t1, 0)
    tmp.value = cstringAt(t1)
    return tmp
}

/** vendor 9275: mstring mstring::operator*(global i) - fetches the
 *  operand into t1[STR_MAX] then multiplies data x t1 INTO t1
 *  (vendor aliasing kept verbatim, fossil note). */
operator fun Mstring.times(i: Global): Mstring {
    val tmp = Mstring()
    val t1 = CharArray(STR_MAX)
    get_global(toCbuf(i.ref), t1) // vendor: get_global(i.get(), t1)
    mult(toCbuf(value), 0, t1, 0, t1, 0)
    tmp.value = cstringAt(t1)
    return tmp
}

/** vendor 9282: mstring mstring::operator*(const char * i). */
operator fun Mstring.times(i: CharArray): Mstring {
    val tmp = Mstring()
    val t1 = CharArray(64)
    mult(toCbuf(value), 0, i, 0, t1, 0)
    tmp.value = cstringAt(t1)
    return tmp
}

/** vendor 9289: mstring operator*(int i, mstring k). */
operator fun Int.times(k: Mstring): Mstring {
    val tmp = Mstring()
    val t1 = CharArray(64)
    mult(toCbuf(cvt(this)), 0, toCbuf(k.value), 0, t1, 0)
    tmp.value = cstringAt(t1)
    return tmp
}

/** vendor 9296: mstring operator*(long i, mstring k). */
operator fun Long.times(k: Mstring): Mstring {
    val tmp = Mstring()
    val t1 = CharArray(64)
    mult(toCbuf(cvt(this)), 0, toCbuf(k.value), 0, t1, 0)
    tmp.value = cstringAt(t1)
    return tmp
}

/** vendor 9303: mstring operator*(double i, mstring k). */
operator fun Double.times(k: Mstring): Mstring {
    val tmp = Mstring()
    val t1 = CharArray(64)
    mult(toCbuf(cvt(this)), 0, toCbuf(k.value), 0, t1, 0)
    tmp.value = cstringAt(t1)
    return tmp
}

/** vendor 9310: mstring operator*(float i, mstring k) - the chunk's
 *  closing construct (slice ends at its mult call line; body complete). */
operator fun Float.times(k: Mstring): Mstring {
    val tmp = Mstring()
    val t1 = CharArray(64)
    mult(toCbuf(cvt(this)), 0, toCbuf(k.value), 0, t1, 0)
    tmp.value = cstringAt(t1)
    return tmp
}
