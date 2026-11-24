package daf

// Unit 214 (DAF B7 T3, complex): libmpscpp.cpp::<misc-6> (roster row
// 187; phys 600 LOC, dump 740 raw lines): the C++ wrapper-library
// residue tail-chunk: the mcvt/cvt numeric ladder, the free count
// key-tally (count at libmpscpp.cpp:2932), HitRatio, EncodeHTML's
// mstring cover, global::Name/__test, the global/mstring ReadLine
// family, the mstring conversion-operator ladder, the zBlanks pair,
// the Ascii family, the Length members, Horolog, global::get, the
// global<< ostream fold, global::Merge, global::Lock/UnLock,
// CleanLocks/CleanAllLocks, the global::Set/Get families,
// global::Kill, the global::Order family, global::Data, the
// mstring::Assign family, the SQL_* relay family, and the
// operator& Concat forwards.
//
// TAG CORRECTION (unit 245, DAF B8): the CHUNK BOUNDARY TAIL tag
// below says the Concat(x7) member bodies sit in the batch-10
// ledger - the member bodies are the misc-7 chunk of THIS batch
// (unit 245, LibmpscppMisc7.kt, rendered; the u119 tag-correction
// precedent). The collapsed concatDaf stub is DELETED and the amp
// relays below resolve to the member real bodies (release note in
// place).
//
// CHUNK BOUNDARY (row 83 hit-adapt, the misc-5 disclosed cut):
// the dump HEAD is the orphan tail "tmp = i; return i; }" of the
// mstring mcvt(char *) body that OPENS in the misc-5 chunk (that
// chunk's ledger, LibmpscppMisc5.kt line 648); the opening lines
// are NOT in this chunk, so the char* twin stays the Misc-5
// carry-over named stub (cross-arm kin: the DOC unit-154/214
// boundary adjudication, identical on both arms). The dump TAIL
// cuts at the mstring global::Concat(double b) OPENER: the
// Concat(x7) member bodies are out of the cut; the twelve
// operator& forwards of this chunk relay to stub seats below
// (owner libmpscpp.cpp::Concat member family; the landed kin is
// the DOC-arm unit-65 GlobalConcat render - cross-arm cite).
//
// Consults (nominal UTC 2026-11-24T20:14:00Z): BASE-log row 56/112
// hit-reuse (the cvt static char[] return buffers and the Get
// family class-static tmp become fresh Strings/locals; the
// Get(char*) stale-scratch return reads the FILLED carrier - the
// H-012 divergence, ledgered inline), row 83 hit-adapt (misc-chunk
// boundary, both edges), row 92 hit-reuse (retrieve/store-then-
// throw: GlobalExceptionCode = GLOBAL_NOT_FOUND_D then
// MumpsGlobalExceptionD), row 101 hit-reuse (no Kotlin mstring
// class: receiver data and ref are explicit String/MpsGlobalNode
// params; the mstring members return the new data String), row 33
// family hit-adapt (strstr -> String indexOf; fgets -> the
// kotlin.io readLine seat of MumpsMainD; the FILE*/istream twins
// fold onto java.io.Reader, H-016 fold), row 98 hit-reuse (the
// std::string/(char*) overload twins collapse onto the String
// seat, H-015), row 26 hit-adapt (sprintf formats kept verbatim
// over Locale.US; %ld renders as %d of the Long), row 8 seats.
// count: SEAT RELEASE - the countDaf named-owner stub raised at
// MakeGlobal.kt (owner libmpscpp.cpp:2932, raised at the unit-199
// render) is released here (the levCheck precedent): the real
// body lands below and the MakeGlobal stub is deleted with a
// release note; the makeGlobalD call site resolves unchanged.
// EncodeHTML(char *) is a corpus row rendered on another arm
// (DOC unit 5); not yet drawn here - the mstring cover delegates
// to a named stub. KiLL likewise not yet drawn (DOC unit 37 kin).
// Horolog's _horolog :3446 kin likewise (DOC roster b9 row 278).
// Dafny: EXEMPT (wrapper/service-family residue over stub seats;
// journal row).

private const val ORDERPREV_M6D = 13 // vendor ORDERPREV (ORDERNEXT_D = 12 seated)

// ---------------- mcvt ladder (mstring covers of the sprintf) --

fun mcvtD(i: Long): String = String.format(java.util.Locale.US, "%d", i)

fun mcvtD(i: Double): String = String.format(java.util.Locale.US, "%g", i)

fun mcvtD(i: Float): String = String.format(java.util.Locale.US, "%g", i)

fun mcvtD(i: Int): String = String.format(java.util.Locale.US, "%d", i)

// ---------------- cvt ladder (char* static-buffer twins) -------
// The C static char a[32]/a[128] return buffers become fresh
// Strings per call (H-018 JVM divergence: no shared-state alias
// between call sites; row 56 static-buffer -> value fold).

fun cvtD(i: Long): String = String.format(java.util.Locale.US, "%d", i)

fun cvtD(i: Double): String = String.format(java.util.Locale.US, "%g", i)

fun cvtD(i: Float): String = String.format(java.util.Locale.US, "%g", i)

fun cvtD(i: Int): String = String.format(java.util.Locale.US, "%d", i)

// ---------------- free count (libmpscpp.cpp:2932) --------------

// int count(char * key) - SEAT RELEASE (dump line 52): the
// MakeGlobal key tally. Quotes guard the comma walk; the
// mis-matched-quote ErrorMessage(msg,0) ride seat errorMessageDaf.
fun countDaf(key: CharArray): Int {
    var i = 0
    var j = 1

    while (i < key.size && key[i] != NUL_D) { // for (i=0; key[i]!='\0'; i++)
        if (key[i] == ',') {
            j++
            i++
            continue
        }

        if (key[i] != '"') { // if (key[i]!='\"') continue;
            i++
            continue
        }

        i++ // i++

        while (i < key.size && key[i] != '"' && key[i] != NUL_D) i++
        // while(key[i]!='\"'&&key[i]) i++;

        if (i >= key.size || key[i] == NUL_D) // if (key[i]=='\0')
            errorMessageDaf("mis-matched quote", 0)

        i++ // the for-loop increment of the outer walk
    }

    return j
}

// ---------------- HitRatio --------------------------------------

// double HitRatio(void) - vendor quirk kept: hit/count is INTEGER
// division (both unsigned long state fields) widened to double
// AFTER the divide; count==0 is the vendor divide-by-zero crash
// shape (Kotlin ArithmeticException - ledger).
fun hitRatioD(sv: DafMsv): Double {
    val ratio = (sv.hit / sv.count).toDouble() // ratio=svPtr->hit/svPtr->count
    sv.hit = 0
    sv.count = 0
    return ratio
}

// ---------------- EncodeHTML mstring cover ---------------------

// mstring EncodeHTML(mstring &str) - the cover of the (char*)
// body; the body is a corpus row rendered on the DOC arm (DOC
// unit 5) and not yet drawn on this arm: named stub seat.
fun encodeHtmlDaf(s: String): String =
    throw NotImplementedError(
        "EncodeHTML: out-of-scope owner (libmpscpp.cpp::EncodeHTML(char*); DOC-arm corpus unit 5 kin, not yet drawn on the DAF arm)")

fun mstringEncodeHtmlD(data: String): String = encodeHtmlDaf(data)

// ---------------- global::Name / __test ------------------------

fun globalNameD(n: MpsGlobalNode): String = n.ref

fun testD(sv: DafMsv): Int = sv.tpx

// ---------------- ReadLine family ------------------------------

// The fgets(tmp,STR_MAX,stdin) + double-trim ladder (the newline
// strip, then the CR-LF < ' ' strip) over the kotlin.io readLine
// seat (MumpsMainD precedent). VENDOR TRUTH kept: the global
// twins STORE the (possibly empty) scratch even on EOF, then
// return i!=0; the mstring twin's EOF arm reads uninitialized
// tmp (vendor UB) - the render returns null without a store on
// that arm, ledger.

private fun readLineTrimM6(): String? {
    val s0 = readLine() ?: return null
    var s = s0
    // tmp[strlen(tmp)-1]=0; if (tmp[strlen(tmp)-1] < ' ') trim
    if (s.isNotEmpty() && s[s.length - 1] < ' ') s = s.substring(0, s.length - 1)
    return s
}

// bool global::ReadLine() - stdin arm.
fun globalReadLineD(n: MpsGlobalNode, sv: DafMsv): Boolean {
    val i = readLineTrimM6()
    val buf = CharArray(STR_MAX_DAF)
    val s = i ?: ""
    for (q in s.indices) buf[q] = s[q]

    val f = MglobalDaf(STORE_D, n.ref.toCharArray(), buf, sv)

    if (f == 0) {
        GlobalExceptionCodeD = GLOBAL_NOT_FOUND_D
        throw MumpsGlobalExceptionD("global.ReadLine: global not found")
    }

    return i != null
}

// bool global::ReadLine(FILE *u1) - the Reader-domain twin
// (H-016 fold; the vendor FILE handle family is the java.io.Reader
// seat).
fun globalReadLineRD(n: MpsGlobalNode, u1: java.io.Reader, sv: DafMsv): Boolean {
    val br = u1 as? java.io.BufferedReader ?: java.io.BufferedReader(u1)
    val s0 = br.readLine()
    var i: String? = s0
    if (i != null && i.isNotEmpty() && i[i.length - 1] < ' ')
        i = i.substring(0, i.length - 1)
    val buf = CharArray(STR_MAX_DAF)
    val s = i ?: ""
    for (q in s.indices) buf[q] = s[q]

    val f = MglobalDaf(STORE_D, n.ref.toCharArray(), buf, sv)

    if (f == 0) {
        GlobalExceptionCodeD = GLOBAL_NOT_FOUND_D
        throw MumpsGlobalExceptionD("global.ReadLine: global not found")
    }

    return i != null
}

// bool mstring::ReadLine() - data = tmp.
fun mstringReadLineD(): String? {
    val s = readLineTrimM6() ?: return null // EOF: vendor UB arm, ledger
    return s
}

// bool mstring::ReadLine(FILE *) / (istream &) twins folded
// (H-016).
fun mstringReadLineRD(u1: java.io.Reader): String? {
    val br = u1 as? java.io.BufferedReader ?: java.io.BufferedReader(u1)
    val s0 = br.readLine() ?: return null
    var s = s0
    if (s.isNotEmpty() && s[s.length - 1] < ' ') s = s.substring(0, s.length - 1)
    return s
}

// ---------------- mstring conversion operators (H-010 fold) ----
// The C++ implicit-conversion ladder; the atoi domain (atolD seat)
// serves int/short/long/unsigned*, atofD (strtod) serves
// float/double. VENDOR QUIRK kept: operator char() is atoi-into-
// a-char (a NUMBER narrowed to a char, not a codepoint read).

fun mstringToIntD(data: String): Int = atoi(data)

fun mstringToFloatD(data: String): Float = atofD(data).toFloat()

fun mstringToDoubleD(data: String): Double = atofD(data)

fun mstringToCharD(data: String): Char = atoi(data).toChar()

fun mstringToShortD(data: String): Short = atolD(data).toShort()

fun mstringToLongD(data: String): Long = atolD(data)

fun mstringToULongD(data: String): ULong = atolD(data).toULong()

fun mstringToUIntD(data: String): UInt = atolD(data).toUInt()

fun mstringToUByteD(data: String): UByte = atolD(data).toUByte()

fun mstringToUShortD(data: String): UShort = atolD(data).toUShort()

// ---------------- zBlanks pair ---------------------------------

private const val ZB_CE_D: Char = '\u00CE' // vendor strcat("\xce")
private const val ZB_CF_D: Char = '\u00CF' // vendor strcat("\xcf")

// mstring mstring::zBlanks() - the zblanks zfcn relay; the
// envelope builds at the v1d[1] offset-1 carriage (the row-101
// fold) and the tmp2 in/out rides the bd seat (the data-in half
// is copied in verbatim).
fun mstringZblanksD(data: String, sv: DafMsv): String {
    // strcpy(&v1d[1],the u0024zblanks literal); strcat(&v1d[1],"\xce")
    val m = StringBuilder("\u0024zblanks")
    m.append(ZB_CE_D)
    m.append(data) // sprintf(tmp2,"%s",data); strcat(&v1d[1],tmp2)
    m.append(ZB_CF_D) // strcat(&v1d[1],"\xcf")

    var q = 0
    while (q < m.length && q + 1 < sv.v1d.size) {
        sv.v1d[q + 1] = m[q]; q++
    }
    sv.v1d[q + 1] = NUL_D

    // tmp2 in half: bd carries the data into the call
    for (k in data.indices) sv.bd[k] = data[k]
    sv.bd[data.length] = NUL_D

    zfcnDaf(sv.v1d, sv.bd, sv) // zfcn(tmp,tmp2,svPtr)

    return DafCRuntime.cstring(sv.bd) // return (mstring) tmp2
}

// mstring global::zBlanks() - the RETRIEVE walk then the member;
// the empty-global return is the C++ implicit mstring(int -1)
// constructor conversion (renders as "-1", quirk ledger).
fun globalZblanksD(n: MpsGlobalNode, sv: DafMsv): String {
    val gtmp = CharArray(STR_MAX_DAF)

    val f = MglobalDaf(RETRIEVE_D, n.ref.toCharArray(), gtmp, sv)

    if (f == 0) {
        GlobalExceptionCodeD = GLOBAL_NOT_FOUND_D
        throw MumpsGlobalExceptionD("global.zBlanks: global not found")
    }

    if (DafCRuntime.cstring(gtmp).isEmpty()) return "-1"

    return mstringZblanksD(DafCRuntime.cstring(gtmp), sv)
}

// ---------------- Ascii family ---------------------------------
// Signed-char domain note (H-012 divergence, ledger): the vendor
// (int)*(char*) / gtmp[0] reads SIGNED chars (codepoints above
// 127 come out negative there); the Kotlin Char.code read is the
// 0..65535 domain.

fun mstringAsciiD(data: String): Int {
    if (data == "") return -1
    return data[0].code
}

fun globalAsciiD(n: MpsGlobalNode, sv: DafMsv): Int {
    val gtmp = CharArray(STR_MAX_DAF)

    val f = MglobalDaf(RETRIEVE_D, n.ref.toCharArray(), gtmp, sv)

    if (f == 0) {
        GlobalExceptionCodeD = GLOBAL_NOT_FOUND_D
        throw MumpsGlobalExceptionD("global.Ascii: global not found")
    }

    if (DafCRuntime.cstring(gtmp).isEmpty()) return -1

    return gtmp[0].code
}

fun globalAsciiAtD(n: MpsGlobalNode, i: Int, sv: DafMsv): Int {
    if (i <= 0) return -1

    val gtmp = CharArray(STR_MAX_DAF)

    val f = MglobalDaf(RETRIEVE_D, n.ref.toCharArray(), gtmp, sv)

    if (f == 0) {
        GlobalExceptionCodeD = GLOBAL_NOT_FOUND_D
        throw MumpsGlobalExceptionD("global.Ascii: global not found")
    }

    if (DafCRuntime.cstring(gtmp).length < i) return -1

    return gtmp[i - 1].code
}

fun mstringAsciiAtD(data: String, i: Int): Int {
    if (i <= 0) return -1

    if (data.length < i) return -1

    if (data == "") return -1

    return data[i - 1].code
}

// ---------------- Length family -------------------------------
// The needle member triple (char*/string/mstring arms) IS the
// landed unit-129 mstringLengthD render (String/CharArray needles
// collapsed onto the String seat, H-015) - no new body here; this
// chunk lands only the no-needle members.

// int mstring::Length()
fun mstringLenD(data: String): Int = data.length

// int global::Length() - vendor QUIRK kept: NO f==0 raise on this
// member (the failed retrieve leaves the scratch empty and the
// strlen returns 0).
fun globalLenD(n: MpsGlobalNode, sv: DafMsv): Int {
    val gtmp = CharArray(STR_MAX_DAF)

    MglobalDaf(RETRIEVE_D, n.ref.toCharArray(), gtmp, sv)

    return DafCRuntime.cstring(gtmp).length
}

// ---------------- Horolog (free fn) ----------------------------

// mstring Horolog() - the _horolog((unsigned char*)t) relay at
// libmpscpp.cpp:3446; the helper is a DOC-arm roster row (DOC b9
// row 278) not yet drawn here: named stub seat.
fun horologDaf(t: CharArray): Unit =
    throw NotImplementedError(
        "_horolog: out-of-scope owner (libmpscpp.cpp:3446; DOC roster b9 row 278 kin, not yet drawn on the DAF arm)")

fun horologD(): String {
    val t = CharArray(64)
    horologDaf(t)
    return DafCRuntime.cstring(t)
}

// ---------------- global::get / operator<< ---------------------

fun globalGetD(n: MpsGlobalNode): String = n.ref

// ostream& operator<<(ostream&, global): no Kotlin seat (the
// console-write fold; disposition ledger only, not translated -
// the cross-arm DOC adjudication).

// ---------------- global::Merge --------------------------------

// int global::Merge(global rhs) - the 128-quirk tail walks of the
// two caret-prefixed refs; ::Merge relays to the mergeDaf seat
// (RELEASED at unit 237 B8; real body at Merge.kt).
fun globalMergeD(n: MpsGlobalNode, rhs: MpsGlobalNode, sv: DafMsv): Int {
    val tmp0 = CharArray(STR_MAX_DAF)
    val tmp1 = CharArray(STR_MAX_DAF)
    tmp0[0] = '^'
    tmp1[0] = '^'

    val r0 = n.ref.toCharArray()
    for (q in r0.indices) tmp0[q + 1] = r0[q]
    val r1 = rhs.ref.toCharArray()
    for (q in r1.indices) tmp1[q + 1] = r1[q]

    var i = 0
    while (tmp0[i] != NUL_D) { // for (i=0; tmp0[i]!='\0'; i++)
        if (tmp0[i].code < 30) tmp0[i] = '\u0080'
        i++
    }
    if (tmp0[i - 2] == '\u0080' && tmp0[i - 1] == '\u0080') tmp0[i - 1] = NUL_D

    i = 0
    while (tmp1[i] != NUL_D) {
        if (tmp1[i].code < 30) tmp1[i] = '\u0080'
        i++
    }
    if (tmp1[i - 2] == '\u0080' && tmp1[i - 1] == '\u0080') tmp1[i - 1] = NUL_D

    return mergeDaf(tmp0, tmp1, sv)
}

// ---------------- Lock / UnLock / CleanLocks -------------------

// int global::Lock() / UnLock(): Mltoa(getpid(),atmp) over the
// mltoaDaf stub seat; makeLock is the makeLockDaf stub seat
// (DafInterpStubsT3; the third arg is the pid digits, the fourth
// the '+'/'-' operation char).
fun globalLockD(n: MpsGlobalNode, sv: DafMsv): Int {
    val atmp = CharArray(32)
    mltoaDaf(getpidD(), atmp)

    if (makeLockDaf(n.ref, "1", DafCRuntime.cstring(atmp), ' ', "") == "1") sv.tpx = 1
    else sv.tpx = 0

    return sv.tpx
}

fun globalUnLockD(n: MpsGlobalNode, sv: DafMsv): Int {
    val atmp = CharArray(32)
    mltoaDaf(getpidD(), atmp)

    if (makeLockDaf(n.ref, "1", DafCRuntime.cstring(atmp), '-', "") == "1") sv.tpx = 1
    else sv.tpx = 0

    return sv.tpx
}

// void CleanLocks(void) - free all locks of this job first.
fun cleanLocksAllD(sv: DafMsv) {
    val atmp = CharArray(32)
    mltoaDaf(getpidD(), atmp)

    if (sv.LockDone != 0) cleanLocksDaf(DafCRuntime.cstring(atmp))
}

// void CleanAllLocks(void) - remove() folds to the JVM delete.
fun cleanAllLocksD() {
    java.io.File("./Mumps.Locks").delete()
}

// ---------------- global::Set family ---------------------------
// set_global(ref,s) is the TU value-store service (get_global's
// store twin, kin of the 16-call-site family Sum.kt ledger): not
// a corpus unit - named stub seat.

fun setGlobalDaf(ref: CharArray, s: CharArray): Unit =
    throw NotImplementedError(
        "set_global: out-of-scope owner (libmpscpp.cpp set_global TU service; not a corpus unit)")

fun globalSetSxD(n: MpsGlobalNode, s: String): String {
    setGlobalDaf(n.ref.toCharArray(), s.toCharArray())
    return s
}

fun globalSetLongD(n: MpsGlobalNode, s: Long): Long {
    setGlobalDaf(n.ref.toCharArray(),
        String.format(java.util.Locale.US, "%d", s).toCharArray())
    return s
}

fun globalSetIntD(n: MpsGlobalNode, s: Int): Int {
    setGlobalDaf(n.ref.toCharArray(),
        String.format(java.util.Locale.US, "%d", s).toCharArray())
    return s
}

fun globalSetDblD(n: MpsGlobalNode, s: Double): Double {
    setGlobalDaf(n.ref.toCharArray(),
        String.format(java.util.Locale.US, "%f", s).toCharArray())
    return s
}

// ---------------- global::Get family ---------------------------

// char * global::Get(char * s) - vendor QUIRK (ledger): the body
// returns the CLASS-STATIC tmp scratch, not the s it just filled;
// the render returns the carrier it filled (H-012 divergence:
// the stale-state read is not reproducible on the JVM seat).
private fun globalGetSharedD(n: MpsGlobalNode, rslt: CharArray, sv: DafMsv): Unit =
    getGlobalDaf(n.ref.toCharArray(), rslt)

private fun globalGetRaiseD() {
    GlobalExceptionCodeD = GLOBAL_NOT_FOUND_D
    throw MumpsGlobalExceptionD("global.Get: global not found")
}

fun globalGetOutD(n: MpsGlobalNode, s: CharArray, sv: DafMsv): String {
    globalGetSharedD(n, s, sv)

    if (sv.tpx == 0) globalGetRaiseD()

    return DafCRuntime.cstring(s)
}

// string global::Get(string &s) - the out-param kept as the
// StringBuilder carrier.
fun globalGetSbD(n: MpsGlobalNode, s: StringBuilder, sv: DafMsv): String {
    val rslt = CharArray(STR_MAX_DAF)
    globalGetSharedD(n, rslt, sv)

    if (sv.tpx == 0) globalGetRaiseD()

    val x = DafCRuntime.cstring(rslt)
    s.setLength(0)
    s.append(x)
    return x
}

// char * global::Get()
fun globalGetNewD(n: MpsGlobalNode, sv: DafMsv): String {
    val rslt = CharArray(STR_MAX_DAF)
    globalGetSharedD(n, rslt, sv)

    if (sv.tpx == 0) globalGetRaiseD()

    return DafCRuntime.cstring(rslt)
}

// double global::Get(double *)
fun globalGetDblD(n: MpsGlobalNode, x: DoubleArray, sv: DafMsv): Double {
    val rslt = CharArray(STR_MAX_DAF)
    globalGetSharedD(n, rslt, sv)

    if (sv.tpx == 0) globalGetRaiseD()

    x[0] = atofD(DafCRuntime.cstring(rslt))
    return x[0]
}

// long global::Get(long *) - the SECOND tpx ErrorMessage guard
// is dead belt after the throw raise (H-013, kept verbatim).
fun globalGetLngD(n: MpsGlobalNode, x: LongArray, sv: DafMsv): Long {
    val rslt = CharArray(STR_MAX_DAF)
    globalGetSharedD(n, rslt, sv)

    if (sv.tpx == 0) globalGetRaiseD()

    if (sv.tpx == 0) errorMessageDaf("global not found", -1)

    x[0] = atolD(DafCRuntime.cstring(rslt))
    return x[0]
}

// ---------------- global::Kill ---------------------------------

// KiLL(ref,svPtr) free service: a corpus row (DOC unit 37 kin)
// not yet drawn on this arm - named stub seat.
fun killDaf(ref: CharArray, sv: DafMsv): Unit =
    throw NotImplementedError(
        "KiLL: out-of-scope owner (libmpscpp.cpp::KiLL; DOC unit-37 kin, not yet drawn on the DAF arm)")

fun globalKillD(n: MpsGlobalNode, sv: DafMsv) {
    if (n.ref.isEmpty()) {
        GlobalExceptionCodeD = GLOBAL_NOT_FOUND_D
        println("Incomplete global reference - no parens ")
        throw MumpsGlobalExceptionD("global.Kill: incomplete global reference")
    }

    killDaf(n.ref.toCharArray(), sv)
}

// ---------------- global::Order family --------------------------

fun globalOrderD(n: MpsGlobalNode, dir: Int, sv: DafMsv): String {
    return cOrderD(n, dir, sv)
}

fun cOrderD(n: MpsGlobalNode, dirIn: Int, sv: DafMsv): String {
    var dir = dirIn

    if (dir != 1 && dir != -1) errorMessageDaf("Unknown parameter to Order", 0)

    if (dir == 1) dir = ORDERNEXT_D
    else dir = ORDERPREV_M6D

    val rslt = CharArray(STR_MAX_DAF)
    sv.tpx = MglobalDaf(dir, n.ref.toCharArray(), rslt, sv)

    return DafCRuntime.cstring(rslt)
}

fun globalOrderNextD(n: MpsGlobalNode, s: CharArray, sv: DafMsv): Int {
    sv.tpx = MglobalDaf(ORDERNEXT_D, n.ref.toCharArray(), s, sv)
    return sv.tpx
}

fun globalOrderPriorD(n: MpsGlobalNode, s: CharArray, sv: DafMsv): Int {
    sv.tpx = MglobalDaf(ORDERPREV_M6D, n.ref.toCharArray(), s, sv)
    return sv.tpx
}

// ---------------- global::Data ----------------------------------

// int global::Data() - GlobalData is the LANDED unit render
// (globalDataD, GlobalData.kt: the xecute-based probe that
// returns the atol digits); the i=strlen(ref) pre-read is the dead
// store the vendor overwrites (H-013, note-only); the static
// char tmp[4] is the fresh carrier fold. The MpsGlobalNode.data()
// stub keeps its tagged owner (out-of-scope class member, a
// different signature family).
fun globalDataAtD(n: MpsGlobalNode, sv: DafMsv): Int {
    return globalDataD(n.ref, sv)
}

// ---------------- mstring::Assign family ------------------------
// The (string)/(mstring)/(int)/(double)/(long) covers fold onto
// the Assign(char*) backend (H-015 String-seat collapse); the
// backend builds "set <data>=\"<b>\"" and Interprets it (the
// operator= route).

fun mstringAssignD(data: String, b: String, sv: DafMsv): String {
    val tmp = StringBuilder("set ") // char tmp[STR_MAX]="set "
    tmp.append(data)
    tmp.append("=\"")
    tmp.append(b)
    tmp.append("\"")
    val f = InterpretDaf(tmp.toString(), sv)

    if (f != 0) {
        GlobalExceptionCodeD = GLOBAL_NOT_FOUND_D
        throw MumpsGlobalExceptionD("mstring.Assign: interpret failed")
    }

    return b
}

// void mstring::Assign(global b): the RETRIEVE-then-assign arm.
fun mstringAssignGD(data: String, b: MpsGlobalNode, sv: DafMsv): String {
    val gtmp = CharArray(STR_MAX_DAF)

    val f = MglobalDaf(RETRIEVE_D, b.ref.toCharArray(), gtmp, sv)

    if (f == 0) {
        GlobalExceptionCodeD = GLOBAL_NOT_FOUND_D
        throw MumpsGlobalExceptionD("mstring.Assign: global not found")
    }

    return mstringAssignD(data, DafCRuntime.cstring(gtmp), sv)
}

fun mstringAssignIntD(data: String, b: Int, sv: DafMsv): String =
    mstringAssignD(data, String.format(java.util.Locale.US, "%d", b), sv)

fun mstringAssignDblD(data: String, b: Double, sv: DafMsv): String =
    mstringAssignD(data, String.format(java.util.Locale.US, "%f", b), sv)

fun mstringAssignLngD(data: String, b: Long, sv: DafMsv): String =
    mstringAssignD(data, String.format(java.util.Locale.US, "%d", b), sv)

// ---------------- SQL relay family -----------------------------
// Every arm is the Interpret(cmd,svPtr) relay of the sql command
// language; the mstring/string/(char*) twins collapse onto the
// String seat (H-015). The SQL_Table(s,c) twin pair forwards to
// the (char*,int) body, which sits OUT this chunk's cut (owner
// named in the stub seat).

fun sqlDisconnectD(sv: DafMsv): Int {
    return InterpretDaf("sql/c", sv)
}

fun sqlConnectD(s: String, sv: DafMsv): Int {
    return InterpretDaf("sql/d " + s, sv)
}

fun sqlFormatD(sv: DafMsv): Int {
    return InterpretDaf("sql/c ", sv)
}

fun sqlFormatSD(s: String, sv: DafMsv): Int {
    return InterpretDaf("sql/c " + s, sv)
}

fun sqlCommandD(s: String, sv: DafMsv): Int {
    return InterpretDaf("sql " + s, sv)
}

// mstring SQL_Table() - the \u0024ztable Eval relay (evalDaf stub
// seat, roster b9).
fun sqlTableD(sv: DafMsv): String {
    val out = CharArray(STR_MAX_DAF)
    val m = CharArray(STR_MAX_DAF)
    val sb = StringBuilder("\u0024ztable")
    for (q in sb.indices) m[q] = sb[q]
    m[sb.length] = NUL_D

    evalDaf(m, out, sv) // m = m.Eval()

    return DafCRuntime.cstring(out)
}

// mstring SQL_Table(char *, int): outside this chunk's cut - the
// (string,int)/(mstring,int) twins forward to the named stub.
fun sqlTableXD(s: String, c: Int, sv: DafMsv): String =
    sqlTableXDaf(s, c, sv)

fun sqlMessageD(sv: DafMsv): String {
    val out = CharArray(STR_MAX_DAF)
    val m = CharArray(STR_MAX_DAF)
    val sb = StringBuilder("\u0024zsql")
    for (q in sb.indices) m[q] = sb[q]
    m[sb.length] = NUL_D

    evalDaf(m, out, sv)

    return DafCRuntime.cstring(out)
}

fun sqlOpenD(sv: DafMsv): Boolean {
    val out = CharArray(STR_MAX_DAF)
    val m = CharArray(STR_MAX_DAF)
    val sb = StringBuilder("\u0024zsqlOpen")
    for (q in sb.indices) m[q] = sb[q]
    m[sb.length] = NUL_D

    evalDaf(m, out, sv)

    return atoi(DafCRuntime.cstring(out)) == 1 // if (m==1)
}

fun sqlNativeD(sv: DafMsv): Boolean {
    val out = CharArray(STR_MAX_DAF)
    val m = CharArray(STR_MAX_DAF)
    val sb = StringBuilder("\u0024znative")
    for (q in sb.indices) m[q] = sb[q]
    m[sb.length] = NUL_D

    evalDaf(m, out, sv)

    return atoi(DafCRuntime.cstring(out)) == 1
}

fun sqlTableXDaf(s: String, c: Int, sv: DafMsv): String =
    throw NotImplementedError(
        "SQL_Table(char*,int): outside the chunk cut (libmpscpp.cpp SQL family; owner named in the batch-10 ledger)")

// ---------------- operator& forwards (Concat family) -----------
// Kotlin has no operator& (H-016): ampD names the vendor one-line
// (*this).Concat(b) relays. concatDaf seat RELEASED at unit 245
// (B8): the twelve overload seats resolve to the member bodies in
// LibmpscppMisc7.kt (the misc-6 batch-10 tag was a forward-tag;
// the u119 tag-correction precedent); the relays below gained the
// sv carrier where the vendor member retrievs (global relays and
// the mstring-node relay) - no external callers, the reshape is
// this unit's cell, ledger here.

fun globalAmpDblD(a: MpsGlobalNode, b: Double, sv: DafMsv): String =
    globalConcatDblD(a.ref, b, sv)

fun globalAmpLngD(a: MpsGlobalNode, b: Long, sv: DafMsv): String =
    globalConcatLngD(a.ref, b, sv)

fun globalAmpIntD(a: MpsGlobalNode, b: Int, sv: DafMsv): String =
    globalConcatIntD(a.ref, b, sv)

fun globalAmpNodeD(a: MpsGlobalNode, b: MpsGlobalNode, sv: DafMsv): String =
    globalConcatStrD(a.ref, b.ref, sv)

fun globalAmpStrD(a: MpsGlobalNode, b: String, sv: DafMsv): String =
    globalConcatStrD(a.ref, b, sv)

fun mstringAmpDblD(a: String, b: Double): String = mstringConcatDblD(a, b)

fun mstringAmpLngD(a: String, b: Long): String = mstringConcatLngD(a, b)

fun mstringAmpIntD(a: String, b: Int): String = mstringConcatIntD(a, b)

fun mstringAmpStrD(a: String, b: String): String = mstringConcatStrD(a, b)

fun mstringAmpNodeD(a: String, b: MpsGlobalNode, sv: DafMsv): String =
    mstringConcatNodeD(a, b, sv)

// ---------------- Unit 227 row-note (DAF B8 T1) -----------------
//
// Unit 227 (DAF B8 T1, core): libmpscpp.cpp::global::ReadLine(
// istream &u1) (21 LOC, score 10.7; units.csv row 236; vendor
// :3039 per the DOC arm's ledger, the istream overload of the
// family this file renders) is a VERIFICATION-ONLY row against the
// landed globalReadLineRD (Reader) twin above. Line-for-line
// against the vendor istream body: the getline(tmp,STR_MAX,'\n')
// call is the BufferedReader readLine of the fold (the STR_MAX
// scratch is the CharArray seat), the CR-LF strip
// (tmp[strlen-1] < ' ' -> 0) is the fold's < ' ' trim,
// byte-equivalent, and the Mglobal(STORE) with the f==0 ->
// GLOBAL_NOT_FOUND throw is verbatim the fold body. The EOF arm:
// the vendor zeroes tmp[0] and RUNS the Mglobal(STORE) over the
// empty scratch before returning false - the landed fold stores
// the empty buffer too (i = tmp ?: "", the store runs, the return
// is i != null), so the DAF fold MATCHES the vendor EOF arm with
// NO divergence to disclose (the DOC arm disclosed a pre-store
// return divergence on its own fold; the DAF fold has none).
// THIS ROW RENDERS NO NEW BODY: the globalReadLineRD seat carries
// it, each roster row keeps its seat.
// Consults (2026-11-24T21:15:30Z, nominal): verification-only
// ledger append (the u186 fold precedent; the u214 file's own
// FILE*/istream twin note above anticipated this fold).
// Dafny: EXEMPT (verification row; journal row).
