package daf

// Unit 280 (DAF B10 T3, complex): libmpscpp.cpp::<misc-8> (units.csv
// row 280: 600 physical LOC, the eighth residual chunk of the
// libmpscpp TU): the global stream operator lattices + the relay
// service family that rides them:
// (a) global::operator<< overloads (unsigned short/long/float/
//     double/string/mstring - the chunk opens mid-body on the
//     unsigned-short arm): store via set_global; LEDGERS to the
//     globalSet* lattice of LibmpscppMisc6 (globalSetSx/Int/Long/
//     Dbl are the rendered << arms; this row MATERIALIZES the
//     three missing kin cells: UShort %hu, ULong %lu, Float %f
//     via the Dbl %f twin);
// (b) global::operator>> overloads (char*/global/int/unsigned int/
//     long/unsigned long/short/unsigned short/float/double/string/
//     mstring): get_global + tpx guard + the sscanf conversion
//     ladder with INVALID_CONVERSION throws - rendered HERE as the
//     globalShr* typed family (the C reference out-params fold
//     onto the typed returns, DISCLOSED);
// (c) xecute(string/mstring/char*): the interpret-service triple:
//     RELEASES the xecuteDaf stub (GlobalQS.kt:32; the b4 blame
//     "not a corpus unit" was wrong - these bodies are row-280
//     cells; NOERR=1, Interpret, svPtr->ERROR=k);
// (d) WriteMumps1: the "write " + exp driver, rendered here;
// (e) mstring::Pattern overloads (string/mstring/global/char*):
//     rendered here relaying to the RAISED pmD seat (the row-306
//     pattern-cell discipline); global::Pattern overloads LEDGER
//     to the row-306 globalPatternD seats (LibmpscppGlobalPattern);
// (f) char* Piece free + mstring::Piece overloads: LEDGER to the
//     pieceDaf kernel (unit 114) and the row-304 mstringPieceD
//     (MstringPiece.kt); the free char* Piece renders here as
//     pieceCharD over the same kernel;
// (g) mstring::Justify/Extract/Find: mstringJustifyD/materialized
//     here over the justifyDaf (unit 194) and extractD kernels;
//     Mltoa via the released mltoaDaf (unit 291); Find LEDGERS to
//     the mstringFindD seat (MstringFind.kt, the unit-166 kin);
// (h) __command__: the strip-brackets wrapper rendered here;
// (i) StopInit relays LEDGER to stopInitD (StopInit.kt);
//     StopLookup triple: RELEASES the stopLookupD stub
//     (LibmpscppMisc9.kt:346; the selftext blame was wrong - the
//     char* body is a row-280 cell; the DafStop.stop carrier of
//     the unit-166 render holds the set);
// (j) SYN triple: RELEASES the synD stub (LibmpscppMisc9.kt:355;
//     the char* bsearch body is a row-280 cell over the sorted
//     DafSyn table of the unit-146 SynInit render; cmpr folds onto
//     the String compareTo);
// (k) SynInit/Shred/ShredQuery relays: synInitD (unit 146),
//     shredD (unit 176) LEDGER; ShredQuery(char*,int) RELEASES the
//     shredQueryD stub (LibmpscppMisc9.kt:368; the static-i rescue
//     walker is a row-280 cell) and the string/mstring relays;
// (l) nodash + the inline Score char pair: rendered here (Score
//     GAP/MATCH/MISMATCH pends on the Smith-Waterman consts of
//     the unit kin); sw relays LEDGER to swD (SmithWaterman.kt);
//     bmg_fullsearch relays LEDGER to the bmgFullsearchD stub seat
//     (the char* core owns selftext.cpp, outside the corpus
//     chunk; the (string,string) malloc twin folds to GC);
// (m) the mstring ctor family closes the chunk: the String data
//     carrier is the row-101 fold (MstringClassHMisc precedent);
//     the commented atexit/set_terminate lines kept comment-only,
//     LEDGER.
// Consults (2026-11-25T15:01:00Z, nominal): rows 6/22 hit-reuse
// (file-scope DafSyn/DafStop singletons reused), row 37
// hit-adapt (residue discipline: stub releases carry body + note),
// rows 54/68 hit-adapt (MpsGlobalNode value carriers; the C++
// reference out-param (int&, string&) lattice folds onto typed
// returns - DISCLOSED divergence), row 30 hit-adapt (get_global
// through the released getGlobalDaf TU service; set_global through
// the setGlobalDaf named stub - stub-mediated honesty), row 56
// hit-adapt (sscanf %d/%ld/%lu/%hd/%hu/%f/%lf fold to the typed
// toX parses with the ==0 throw arm), row 33 hit-reuse (sprintf
// format folds), row 92 hit-reuse (GlobalExceptionCodeD +
// MumpsGlobalExceptionD), row 98 hit-reuse (overload collapse onto
// String seats), row 99 return-fold, row 101 hit-reuse (the
// mstring String fold), row 103 hit-reuse (RETRIEVE_D MglobalDaf
// fetch + tpx guard), row 105 family hit-reuse (bsearch over the
// sorted table; the vendor requires SynInit's ascending sort -
// keep, quirk: unsorted-table lookup after partial init is
// vendor UB, ledgered).
// VENDOR QUIRKS kept: (1) the >> char*/global/string/mstring arms
// return s / i after the guard (the read value, not a status);
// (2) the unsigned-int >> arm parses with %d (signed parse on an
// unsigned cell, kept verbatim); (3) operator>>(global) copies
// through the char scratch; (4) SYN returns the input word
// unchanged on bsearch miss; (5) ShredQuery's static i cursor
// persists across calls (file-scope var here, vendor static);
// (6) __command__ strips exactly one leading and one trailing
// byte (strlen-1 overwrite); (7) the mstring(int/long/float/
// double/char/...) ctors format per vendor (%ld/%d/%lf/%f).
// Dafny: EXEMPT (relay lattice over stub/released seats + string
// formatting; journal row).

// ---------------------------------------------------------------------
// (a) operator<< kin cells (the three missing arms; the Sx/Int/
// Long/Dbl arms are the LibmpscppMisc6 lattice)
// ---------------------------------------------------------------------

// global & global::operator<<(unsigned short i) - sprintf "%hu"
fun globalSetUShortD(n: MpsGlobalNode, s: Int): Int {
    setGlobalDaf(n.ref.toCharArray(),
        String.format(java.util.Locale.US, "%d", s).toCharArray())
    return s
}

// global & global::operator<<(unsigned long i) - sprintf "%lu"
fun globalSetULongD(n: MpsGlobalNode, s: Long): Long {
    setGlobalDaf(n.ref.toCharArray(),
        String.format(java.util.Locale.US, "%d", s).toCharArray())
    return s
}

// global & global::operator<<(float i) - sprintf "%f": the Float
// arm formats identically to the Double arm; folds onto the
// Dbl %f twin body (the JVM Float formats through Double).
fun globalSetFloatD(n: MpsGlobalNode, s: Float): Float {
    setGlobalDaf(n.ref.toCharArray(),
        String.format(java.util.Locale.US, "%f", s.toDouble()).toCharArray())
    return s
}

// ---------------------------------------------------------------------
// (b) operator>> typed family (get_global + tpx guard + parse)
// ---------------------------------------------------------------------

// the shared fetch + tpx guard of every >> arm
internal fun globalShrFetchD(n: MpsGlobalNode): String {
    val tmp = CharArray(STR_MAX_DAF)
    val rslt = CharArray(STR_MAX_DAF)
    // get_global((*this).get(), s); - (*this).get() returns ref
    for (q in n.ref.indices) tmp[q] = n.ref[q]
    getGlobalDaf(tmp, rslt)

    if (DafInterpState.svPtr.tpx == 0) { // if (!svPtr->tpx)
        GlobalExceptionCodeD = GLOBAL_NOT_FOUND_D
        throw MumpsGlobalExceptionD("operator>>: global not found")
    }
    return DafCRuntime.cstring(rslt)
}

// char * global::operator>>(char * s) : string arm; the mstring
// twin folds onto the same String seat (row 98).
fun globalShrStrD(n: MpsGlobalNode): String = globalShrFetchD(n)

// global & global::operator>>(global g): the copy-through arm
// (reads this, stores into g via the set_global stub seat).
fun globalShrGlobalD(n: MpsGlobalNode, g: MpsGlobalNode): MpsGlobalNode {
    val s = globalShrFetchD(n)
    setGlobalDaf(g.ref.toCharArray(), s.toCharArray())
    return n // return *this
}

// int global::operator>>(int & i) - the out-param folds onto the
// typed return (disclosed); sscanf %d == 0 throws
// INVALID_CONVERSION (libmpscpp.h:70).
fun globalShrIntD(n: MpsGlobalNode): Int {
    val s = globalShrFetchD(n)
    return try { s.trim().toInt() } catch (e: NumberFormatException) {
        GlobalExceptionCodeD = INVALID_CONVERSION_D
        throw MumpsGlobalExceptionD("operator>>: invalid conversion")
    }
}

// unsigned int global::operator>>(unsigned int & i): VENDOR
// QUIRK 2: the unsigned cell parses with the SIGNED %d (kept).
fun globalShrUIntD(n: MpsGlobalNode): Int = globalShrIntD(n)

// long global::operator>>(long & i) - sscanf %ld
fun globalShrLongD(n: MpsGlobalNode): Long {
    val s = globalShrFetchD(n)
    return try { s.trim().toLong() } catch (e: NumberFormatException) {
        GlobalExceptionCodeD = INVALID_CONVERSION_D
        throw MumpsGlobalExceptionD("operator>>: invalid conversion")
    }
}

// unsigned long global::operator>>(unsigned long & i) - %lu
fun globalShrULongD(n: MpsGlobalNode): Long = globalShrLongD(n)

// short global::operator>>(short & i) - sscanf %hd
fun globalShrShortD(n: MpsGlobalNode): Int {
    val s = globalShrFetchD(n)
    val v = try { s.trim().toInt() } catch (e: NumberFormatException) {
        GlobalExceptionCodeD = INVALID_CONVERSION_D
        throw MumpsGlobalExceptionD("operator>>: invalid conversion")
    }
    return v // (the C short truncates at the caller seat, kept)
}

// unsigned short global::operator>>(unsigned short & i) - %hu
fun globalShrUShortD(n: MpsGlobalNode): Int = globalShrShortD(n)

// float global::operator>>(float & i) - sscanf %f
fun globalShrFloatD(n: MpsGlobalNode): Float {
    val s = globalShrFetchD(n)
    return try { s.trim().toFloat() } catch (e: NumberFormatException) {
        GlobalExceptionCodeD = INVALID_CONVERSION_D
        throw MumpsGlobalExceptionD("operator>>: invalid conversion")
    }
}

// double global::operator>>(double & i) - sscanf %lf
fun globalShrDoubleD(n: MpsGlobalNode): Double {
    val s = globalShrFetchD(n)
    return try { s.trim().toDouble() } catch (e: NumberFormatException) {
        GlobalExceptionCodeD = INVALID_CONVERSION_D
        throw MumpsGlobalExceptionD("operator>>: invalid conversion")
    }
}

// ---------------------------------------------------------------------
// (c) xecute triple: RELEASES xecuteDaf (GlobalQS.kt stub)
// ---------------------------------------------------------------------

// int xecute(char * exp) - the char* cell: NOERR=1, Interpret,
// ERROR=k. The string/mstring twins fold onto the String seat
// (row 98 overload collapse).
fun xecuteDaf(exp: String): Int {
    val svPtr = DafInterpState.svPtr
    val tmp = CharArray(STR_MAX_DAF)
    var q = 0
    while (q < exp.length && q < tmp.size - 1) { tmp[q] = exp[q]; q++ }
    tmp[q] = NUL_D
    svPtr.NOERR = 1
    val k = InterpretDaf(DafCRuntime.cstring(tmp), svPtr)
    svPtr.ERROR = k
    return k
}

// ---------------------------------------------------------------------
// (d) WriteMumps1
// ---------------------------------------------------------------------

fun writeMumps1D(svPtr: DafMsv, exp: String): Int {
    // char tmp[STR_MAX] = "write "; strcat(tmp, exp)
    val tmp = "write " + exp
    svPtr.NOERR = 1
    val k = InterpretDaf(tmp, svPtr)
    svPtr.ERROR = k
    return k
}

// ---------------------------------------------------------------------
// (e) mstring::Pattern overloads (relays to the raised pmD seat)
// ---------------------------------------------------------------------

// int mstring::Pattern(string p) / Pattern(mstring & p) /
// Pattern(const char * p): the receiver data relays as the
// needle; the char*/string/mstring twins collapse onto the String
// seat (row 98).
fun mstringPatternD(data: String, p: String, svPtr: DafMsv): Int =
    pmD(data, p, svPtr)

// int mstring::Pattern(global p): retrieve + guard + relay
fun mstringPatternGD(data: String, p: MpsGlobalNode, svPtr: DafMsv): Int {
    val tmp = CharArray(STR_MAX_DAF)
    val f = MglobalDaf(RETRIEVE_D, p.ref.toCharArray(), tmp, svPtr)
    if (f == 0) {
        GlobalExceptionCodeD = GLOBAL_NOT_FOUND_D
        throw MumpsGlobalExceptionD("mstring::Pattern: global not found")
    }
    return pmD(data, DafCRuntime.cstring(tmp), svPtr)
}

// ---------------------------------------------------------------------
// (f) char* Piece free arm (the mstring::Piece overloads ledger
// to the row-304 mstringPieceD seat)
// ---------------------------------------------------------------------

// char * Piece(const char * source, char * rslt, const char * pat,
//               int ii, int jj)
fun pieceCharD(source: String, pat: String, ii: Int, jjIn: Int): String {
    var jj = jjIn
    if (jj < 0) jj = ii // if (jj<0) jj=ii (the row-304 quirk kin)
    val cc1 = "%d".format(java.util.Locale.US, ii)  // sprintf(cc1,"%d",ii)
    val cc2 = "%d".format(java.util.Locale.US, jj)  // sprintf(cc2,"%d",jj)
    // setpiece==0 read fold: the row-97 seat returns the piece
    return pieceDaf("", source, pat, cc1, cc2, 0, "", DafInterpState.svPtr)
}

// ---------------------------------------------------------------------
// (g) mstring::Justify / Extract members
// ---------------------------------------------------------------------

// mstring mstring::Justify(int c1, int c2)
fun mstringJustifyD(data: String, c1: Int, c2: Int): String {
    val cc1 = "%d".format(java.util.Locale.US, c1)
    val cc2 = "%d".format(java.util.Locale.US, c2)
    val tmp = CharArray(STR_MAX_DAF)
    justifyDaf(tmp, data.toCharArray(), cc1.toCharArray(), cc2.toCharArray())
    return DafCRuntime.cstring(tmp)
}

// mstring mstring::Extract(int st, int ln) - _extract over the
// Mltoa-formatted bounds (the unit-291 release carries Mltoa)
fun mstringExtractD(data: String, st: Int, ln: Int): String {
    val a = CharArray(40)
    val b = CharArray(40)
    mltoaDaf(st.toLong(), a)
    mltoaDaf(ln.toLong(), b)
    val r = CharArray(STR_MAX_DAF)
    extractD(r, data.toCharArray(), a, b)
    return DafCRuntime.cstring(r)
}

// ---------------------------------------------------------------------
// (h) __command__: drop the enclosing brackets (one byte each)
// ---------------------------------------------------------------------

// char * __command__(char * str)
fun commandD(str: String): String =
    if (str.length >= 2) str.substring(1, str.length - 1) else ""

// ---------------------------------------------------------------------
// (i) StopLookup triple: RELEASES stopLookupD (the DafStop set
// carrier of the unit-166 StopInit render holds the state)
// ---------------------------------------------------------------------

// int StopLookup(char * str)
fun stopLookupD(str: String): Boolean =
    DafStop.stop.contains(str)

// int StopLookup(mstring str) / int StopLookup(string str):
// the relay twins fold onto the String seat (row 98); the
// vendor's map find/end compare folds onto the set contains.

// ---------------------------------------------------------------------
// (j) SYN triple: RELEASES synD (bsearch over the sorted DafSyn
// table; the unit-146 SynInit sort makes cmpr valid)
// ---------------------------------------------------------------------

// char * SYN(char * word)
fun synD(word: String): String {
    // bsearch(word, syn, synx, sizeof(struct Syn), cmpr)
    var lo = 0
    var hi = DafSyn.synx.toInt() - 1
    while (lo <= hi) { // the bsearch binary step over the sorted word
        val mid = (lo + hi) / 2
        val c = DafSyn.syn[mid]!!.first.compareTo(word)
        if (c == 0) return DafSyn.syn[mid]!!.second // (*p).alias
        if (c < 0) lo = mid + 1 else hi = mid - 1
    }
    return word // if (p==NULL) return word (quirk 4)
}

// mstring SYN(mstring word) / string SYN(string word): relay
// twins fold onto the String seat (row 98).

// ---------------------------------------------------------------------
// (k) ShredQuery(char *, int): RELEASES shredQueryD (the
// static-i rescue walker; the string/mstring Shred and
// ShredQuery twins ledger to shredD/(this seat))
// ---------------------------------------------------------------------

// the vendor's static int i cursor (file-scope var, vendor static)
private var shredQueryI = 0

// char * ShredQuery(char * str, int size)
fun shredQueryD(str: String, size: Int): String {
    // p1 = Shred(&str[i], size)
    var p1 = if (shredQueryI < str.length) shredD(str.substring(shredQueryI), size) else shredD("", size)
    if (p1.isEmpty()) {
        shredQueryI++
        if (shredQueryI < size) {
            return if (shredQueryI < str.length) shredD(str.substring(shredQueryI), size) else shredD("", size)
        }
        shredQueryI = 0
        return ""
    }
    return p1
}

// ---------------------------------------------------------------------
// (l) nodash + Score (+ the sw/bmg relay ledgers)
// ---------------------------------------------------------------------

// int nodash(char * a): counts the non-dash bytes
fun nodashD(a: String): Int {
    var i = 0
    for (c in a) if (c != '-') i++
    return i
}

// the vendor libmpscpp.cpp:8036 file-scope mutable ints (they are
// NOT defines: sw() assigns them at :8157 from its tuning args;
// zero-initialised until sw runs, vendor int-default quirk kept).
// The raised swD stub is the only assigner; this carrier is the
// Score reader (row 6/22 file-scope singleton fold).
private object DafSwScore {
    var GAP = 0       // int GAP; // gap penalty
    var MATCH = 0     // int MATCH;
    var MISMATCH = 0  // int MISMATCH;
}


// inline int Score(char a, char b): the Smith-Waterman cell pair
// scorer (vendor libmpscpp.cpp:8050); reads the mutable carrier
// above (the swD raise at SmithWaterman.kt is the assigner seat).
fun scoreCharD(a: Char, b: Char): Int {
    if (a == '-' || b == '-') return DafSwScore.GAP
    if (a == b) return DafSwScore.MATCH
    return DafSwScore.MISMATCH
}

// ---------------------------------------------------------------------
// (m) mstring ctor family: the String-data carrier is the
// established row-101 fold; LEDGER rows only (the commented
// atexit/set_terminate lines kept comment-only). The C++ name/
// data two-field node stays the MstringClassHMisc seat.
// ---------------------------------------------------------------------

// mstring::mstring(const char *) / (const mstring &) /
// (const string &) / (long) / (int) / (double) / (float) / (char):
// the String fold renders the formatting cells:
fun mstringFromLongD(a: Long): String =
    "%d".format(java.util.Locale.US, a)      // sprintf "%ld"
fun mstringFromIntD(a: Int): String =
    "%d".format(java.util.Locale.US, a)      // sprintf "%d"
fun mstringFromDoubleD(a: Double): String =
        String.format(java.util.Locale.US, "%f", a) // sprintf "%lf"
fun mstringFromFloatD(a: Float): String =
        String.format(java.util.Locale.US, "%f", a) // sprintf "%f"
fun mstringFromCharD(a: Char): String = a.toString() // tmp[0]=a; tmp[1]=0
