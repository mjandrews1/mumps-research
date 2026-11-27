package both

// Unit 187 (BOTH B7 T1, complex): libmpscpp.cpp::<misc-6> (600
// physical LOC, score 319.2). The residual C++ binding chunk: the
// mcvt/cvt conversion families, the count helper, HitRatio, the
// EncodeHTML mstring forwarder, global::Name/__test, the ReadLine
// family, the mstring cast-operator lattice, zBlanks, the Ascii
// families, the Length families, Horolog, global::get, the stream
// insert, Merge, the Lock/UnLock pair, CleanLocks/CleanAllLocks,
// the global Set/Get overloads, Kill, Order/C_Order/Order_Next/
// Order_Prior/Data, the mstring Assign family, and the SQL connect
// envelopes.
//
// CHUNK BOUNDARY NOTE: the physical chunk opens with the tail of
// mcvt(char*) ("tmp = i; return i; }") - those lines are the
// ALREADY-RENDERED tail of the unit-154 boundary fragment in
// LibmpscppMisc5.kt (mcvt(CharArray)); NOT re-rendered here (a
// duplicate seat would collide, h015). The chunk closes at the end
// of SQL_Connect(string). Both boundaries journaled.
//
// Quirks kept (ledgered):
//   - Sref: global::Name() reads the vendor Sref member (the name
//     twin of ref); the arm GlobalRef carrier folds Sref onto ref
//     (T1 carrier precedent), journaled here.
//   - __test(): vendor returns int from the long tpx; the fold
//     narrows (Long.toInt modulo fold, ledger).
//   - ReadLine EOF: on fgets EOF the vendor strips tmp[strlen-1]
//     at index -1 (C UB) and still Mglobal-STOREs the stale buffer.
//     Quantized: EOF parks data as the empty line, the STORE fold
//     executes with it, and i!=0 returns false (the only
//     deterministic choice; journaled like the misc-5 ZSeek UB).
//   - member scratch: global::Get/C_Order read the vendor global
//     class member tmp; the arm carrier has no such member, so each
//     call seats a fresh scratch buffer (the b5 carrier notes), and
//     C_Order's return of member tmp folds to the fresh scratch.
//   - zBlanks(quoting? no) - the vendor builds its zfcn envelope
//     at &tmp[1] leaving tmp[0] UNWRITTEN (stack residue); the
//     CharArray fold writes a NUL at tmp[0] (zeroed-array default),
//     journaled.
//   - casts: C atoi/atof semantics over the int/unsigned families
//     ride the atolC/strtodC folds (modular narrowing on overflow,
//     ledgered); the unsigned forms are the same parse with the C
//     implicit re-interpretation noted, not wrapped.
//   - Data(): the vendor dead-stores i=strlen(ref) then folds
//     GlobalData+atol; the arm's globalDataC seat already IS the
//     atol-folded contract, so Data() rides it directly (fold
//     journaled; the strlen dead store kept in this comment).
//   - CleanAllLocks: vendor remove("./Mumps.Locks") rides java.io
//     File.delete (boolean result discarded).
//
// BOTH consults (find_rule before render, events logged):
//   h007 char_buffer - reused: strcat chains fold to String builds
//     (Assign/SQL envelopes); the marker lattice stays Char.
//   h008 static_scratch - hit: the cvt/Horolog static seats dump
//     fresh per call (caller-copies flow; no cross-call aliasing).
//   h012 unsigned_byte_domain - reused: the 0xCE/0xCF markers seat
//     as u-escaped Char domain values.
//   h013 dead_store - reused: Data() strlen, SQL f returns.
//   h014 out_of_scope_call - reused: TODO seats below pin pending
//     owners (EncodeHTML 26, KiLL 59, Merge 211, _horolog 277,
//     makeLock non-roster helper).
//   h015 grammar-collision - reused: all seats ASCII, no backticks,
//     no raw comment dollars, trailing newline; the System-in seat
//     rides the FileDescriptor.in face (no in-keyword escape).
//   h017 naming - reused: vendor member names kept at the folded
//     package seats (ReadLine/Ascii/Length/zBlanks/Set/Get/Kill/
//     Order/Assign/Merge/Lock/UnLock/CleanLocks/HitRatio/Horolog).
//   h021 struct_carrier - reused: receiver state passes explicitly
//     (GlobalRef / String data), the unit-149 member-carrier lead.
//   h001 io_stream_param - reused: the ReadLine faces take the
//     java.io.Reader edge; the stdin binding rides the MumpsMain
//     FileDescriptor.in precedent (a second buffered reader on fd 0
//     is a disclosed ADJUDICATION FLAG - the vendor shares fd 0
//     between mumps main and the member faces, the arm seats two).

// ------------------------------------------------------------------
// mcvt family (mstring returns; the %ld/%g/%g/%d sprintf folds)

internal fun mcvt(i: Long): String = i.toString() // sprintf(a,"%ld",i)
internal fun mcvt(i: Double): String = fmtG(i)
internal fun mcvt(i: Float): String = fmtG(i.toDouble())
internal fun mcvt(i: Int): String = i.toString() // sprintf(a,"%d",i)

// cvt family (C static char a[...]; fresh per call per h008)
internal fun cvt(i: Long): String = i.toString()
internal fun cvt(i: Double): String = fmtG(i)
internal fun cvt(i: Float): String = fmtG(i.toDouble())
internal fun cvt(i: Int): String = i.toString()

// int count(char * key) - comma tally with the quoted-field skip
internal fun countKeyZb(key: String): Int {
    var j = 1
    var i = 0
    while (i < key.length && key[i] != '\u0000') {
        if (key[i] == ',') { j++; i++; continue }
        if (key[i] != '"') { i++; continue }
        i++ // enter the quoted field
        while (i < key.length && key[i] != '"' && key[i] != '\u0000') i++
        if (i >= key.length || key[i] == '\u0000')
            errorMessage("mis-matched quote", 0) // ErrorMessage(...,0)
        i++ // past the closing quote (C UB guard kept explicit)
    }
    return j
}

// double HitRatio(void) - the svPtr.hit/svPtr->count coupon
internal fun hitRatioZb(): Double {
    val ratio = svPtr.hit.toDouble() / svPtr.count.toDouble()
    svPtr.hit = 0
    svPtr.count = 0
    return ratio
}

// ------------------------------------------------------------------
// pending-owner TODO seats (h014)

private fun encodeHtmlC(s: String): String {
    TODO("stub: owner libmpscpp.cpp::EncodeHTML (roster 26, batch 1)")
}
private fun killC(ref: String, sv: Msv) {
    TODO("stub: owner libmpscpp.cpp::KiLL (roster 59, batch 2)")
}
private fun mergeSvZb(a: CharArray, b: CharArray, sv: Msv): Int {
    TODO("stub: owner interp-sysfunc.cpp::Merge (roster 211, batch 8)")
}
private fun horologC(t: CharArray) {
    TODO("stub: owner interp-sym.cpp::_horolog (roster 277, batch 9)")
}
private fun makeLockMisc6Zb(
    a: String, b: String, c: String, lf: Char, d: String?
): String {
    // the Interpret render seats its own private face of the same
    // non-roster helper (libmpscpp.cpp::makeLock); this seat pins
    // the same owner for the member faces below.
    TODO("stub: owner libmpscpp.cpp::makeLock (non-roster cross-TU helper)")
}

// ------------------------------------------------------------------
// EncodeHTML forwarder and global::Name/__test

// mstring EncodeHTML(mstring & str) { return EncodeHTML(str); }
internal fun encodeHtmlM(str: String): String = encodeHtmlC(str)

// mstring global::Name() - Sref quirk (header ledger)
internal fun GlobalRef.nameM(): String = ref

// int __test() { return svPtr->tpx; }
internal fun testZb(): Int = svPtr.tpx.toInt()

// ------------------------------------------------------------------
// ReadLine family. The fgets fold reads up to STR_MAX-1 chars from
// the Reader; EOF quantization per the header ledger.

// the carried result (ok = vendor i!=0; line = the stripped tmp)
private fun fgetsFoldZb(u1: java.io.Reader): Pair<Boolean, String> {
    val sb = StringBuilder()
    var n = 0
    while (n < STR_MAX - 1) {
        val c = u1.read()
        if (c < 0) return Pair(sb.isNotEmpty() || n > 0, "")
        if (c == '\n'.code) break
        sb.append(c.toChar())
        n++
    }
    return Pair(true, sb.toString())
}

private fun stripCrZb(s: String): String {
    // tmp[strlen(tmp)-1]=0; if (tmp[strlen-1] < ' ') tmp[strlen-1]=0
    var t = s
    if (t.isNotEmpty() && t[t.length - 1] == '\n') t = t.dropLast(1)
    if (t.isNotEmpty() && t[t.length - 1] < ' ') t = t.dropLast(1)
    return t
}

// bool global::ReadLine() / ReadLine(FILE*) - one face, the stdin
// overloads share it (the vendor bodies are identical)
internal fun GlobalRef.readLineG(u1: java.io.Reader): Boolean {
    val (ok, raw) = fgetsFoldZb(u1)
    val tmp = stripCrZb(raw)
    val buf = CharArray(STR_MAX)
    writeC(buf, tmp)
    val f = Mglobal(STORE, ref.toCharArray(), buf, svPtr)
    if (f == 0) {
        GlobalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }
    return ok
}

// the stdin face (vendor binds stdin): the FileDescriptor.in seat
// (MumpsMain precedent; the double-buffer adjudication flag rides
// the header ledger)
internal fun GlobalRef.readLineGStdin(): Boolean = readLineG(consoleInZb())

private fun consoleInZb(): java.io.Reader {
    // reflection seat (the MumpsMain stdFd precedent: no keyword-
    // shaped member access, h017)
    val stdFd = java.io.FileDescriptor::class.java
        .getField("in").get(null) as java.io.FileDescriptor
    return java.io.BufferedReader(
        java.io.InputStreamReader(java.io.FileInputStream(stdFd))
    )
}

// bool mstring::ReadLine() / ReadLine(FILE*) - receiver data
// returned as the new state (h021 explicit receiver)
internal fun readLineM(u1: java.io.Reader): Pair<Boolean, String> {
    val (ok, raw) = fgetsFoldZb(u1)
    return Pair(ok, stripCrZb(raw)) // EOF quantization: ""
}

// bool mstring::ReadLine(istream &u1) - the getline(char-form) edge
internal fun readLineMIstream(u1: java.io.Reader): Pair<Boolean, String> {
    val sb = StringBuilder()
    var n = 0
    var eof = false
    while (n < STR_MAX - 1) {
        val c = u1.read() // istream::get
        if (c < 0) { eof = true; break }
        sb.append(c.toChar())
        n++
        if (sb.isNotEmpty() && sb[sb.length - 1] == '\n') break
    }
    if (eof && sb.isEmpty()) return Pair(false, "") // data=""
    var t = sb.toString()
    if (t.isNotEmpty() && t[t.length - 1] == '\n') t = t.dropLast(1) // delim
    if (t.isNotEmpty() && t[t.length - 1] < ' ') t = t.dropLast(1) // CR
    return Pair(true, t)
}

// ------------------------------------------------------------------
// the mstring cast-operator lattice (receivers pass data state)

internal fun String.mstringCastInt(): Int = atolC(this).toInt()
internal fun String.mstringCastFloat(): Float = strtodC(this).toFloat()
internal fun String.mstringCastDouble(): Double = strtodC(this)
internal fun String.mstringCastChar(): Char = atolC(this).toInt().toChar()
internal fun String.mstringCastShort(): Short = atolC(this).toInt().toShort()
internal fun String.mstringCastLong(): Long = atolC(this)
internal fun String.mstringCastULong(): Long = atolC(this) // unsigned ledger
internal fun String.mstringCastUInt(): Int = atolC(this).toInt() // unsigned ledger
internal fun String.mstringCastUChar(): Char = atolC(this).toInt().toChar()
internal fun String.mstringCastUShort(): Short = atolC(this).toInt().toShort()

// ------------------------------------------------------------------
// zBlanks (the 0xCE/0xCF marker lattice, h012 domains)

private const val ZBLNK_OPEN = '\u00CE' // vendor "\xce"
private const val ZBLNK_CLOSE = '\u00CF' // vendor "\xcf"

// defines.h:170/171 (the Mglobal ORDERNEXT/ORDERPREV prepare ops;
// package consts first seated here, owner-pin release note with
// the Mglobal prepare-block owner)
internal const val ORDERNEXT = 12
internal const val ORDERPREV = 13

// mstring mstring::zBlanks()
internal fun String.zBlanksM(sv: Msv): String {
    // sprintf(tmp2, "%s", data) is the vendor's second scratch; the
    // fold concatenates directly (the scratch never survives)
    val tmpA = CharArray(STR_MAX) // tmp[0] NUL quirk (header ledger)
    writeCStr(tmpA, 1, "\u0024zblanks" + ZBLNK_OPEN + this + ZBLNK_CLOSE)
    val tmp2 = CharArray(STR_MAX)
    zfcn(tmpA, tmp2, sv) // zfcn(tmp, tmp2, svPtr)
    return cStringOf(tmp2)
}

// mstring global::zBlanks()
internal fun GlobalRef.zBlanksG(sv: Msv): String {
    val gtmp = CharArray(STR_MAX)
    val f = Mglobal(RETRIEVE, ref.toCharArray(), gtmp, svPtr)
    if (f == 0) {
        GlobalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }
    val s = cStringOf(gtmp)
    if (s.isEmpty()) return "-1" // mstring(int -1) ctor fold: "-1"
    return s.zBlanksM(sv)
}

// strcpy(&dst[off], src) string form
private fun writeCStr(dst: CharArray, off: Int, src: String) {
    for (i in src.indices) dst[off + i] = src[i]
    dst[off + src.length] = '\u0000'
}

// ------------------------------------------------------------------
// Ascii families

// int mstring::Ascii()
internal fun asciiM(data: String): Int =
    if (data.isEmpty()) -1 else data[0].code

// int global::Ascii()
internal fun GlobalRef.asciiG(sv: Msv): Int {
    val gtmp = CharArray(STR_MAX)
    val f = Mglobal(RETRIEVE, ref.toCharArray(), gtmp, svPtr)
    if (f == 0) {
        GlobalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }
    val s = cStringOf(gtmp)
    if (s.isEmpty()) return -1
    return gtmp[0].code
}

// int global::Ascii(int i)
internal fun GlobalRef.asciiG(i: Int, sv: Msv): Int {
    if (i <= 0) return -1
    val gtmp = CharArray(STR_MAX)
    val f = Mglobal(RETRIEVE, ref.toCharArray(), gtmp, svPtr)
    val s = cStringOf(gtmp)
    if (f == 0) {
        GlobalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }
    if (s.length < i) return -1
    return gtmp[i - 1].code
}

// int mstring::Ascii(int i)
internal fun asciiM(data: String, i: Int): Int {
    if (i <= 0) return -1
    if (data.length < i) return -1
    if (data.isEmpty()) return -1
    return data[i - 1].code // i--; return *(data.c_str()+i)
}

// ------------------------------------------------------------------
// Length families

// int mstring::Length()
internal fun lengthM(data: String): Int = data.length // strlen fold

// int global::Length()
internal fun GlobalRef.lengthG(sv: Msv): Int {
    val gtmp = CharArray(STR_MAX)
    val f = Mglobal(RETRIEVE, ref.toCharArray(), gtmp, svPtr)
    if (f == 0) {
        GlobalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }
    return cStringOf(gtmp).length
}

// int mstring::Length(needle) - the three vendor overloads (char*,
// string, mstring) fold onto the String needle face (h007 fold per
// the misc-5 operator== precedent)
internal fun lengthNM(data: String, needle: String): Int {
    if (data.isEmpty()) return 0
    var i = 1
    var p = 0
    while (true) {
        val hit = data.indexOf(needle, p)
        if (hit < 0) return i // strstr miss -> one piece
        i++
        p = hit + needle.length
    }
}

// ------------------------------------------------------------------
// Horolog

// mstring Horolog() - static char t[64] per h008
internal fun horologM(): String {
    val t = CharArray(64)
    horologC(t)
    return cStringOf(t)
}

// ------------------------------------------------------------------
// global::get, stream insert, Merge

// char * global::get() { return ref; }
internal fun GlobalRef.get(): String = ref

// ostream& operator<<(ostream& cout, global x) - print fold
internal fun printGlobalZb(x: GlobalRef, out: java.lang.StringBuilder) {
    val tmp = CharArray(STR_MAX)
    svPtr.tpx = Mglobal(RETRIEVE, x.get().toCharArray(), tmp, svPtr).toLong()
    if (svPtr.tpx == 0.toLong()) {
        System.out.print("\n*** Global not found: " + x.ref + "\n")
        GlobalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }
    out.append(cStringOf(tmp))
}

// int global::Merge(global rhs)
internal fun GlobalRef.mergeG(rhs: GlobalRef, sv: Msv): Int {
    val tmp0 = CharArray(STR_MAX)
    val tmp1 = CharArray(STR_MAX)
    tmp0[0] = '^'
    tmp1[0] = '^'
    writeCStr(tmp0, 1, ref)
    writeCStr(tmp1, 1, rhs.ref)
    var i = 0
    while (tmp0[i] != '\u0000') { if (tmp0[i].code < 30) tmp0[i] = 128.toChar(); i++ }
    if (tmp0[i - 2] == 128.toChar() && tmp0[i - 1] == 128.toChar()) tmp0[i - 1] = '\u0000'
    i = 0
    while (tmp1[i] != '\u0000') { if (tmp1[i].code < 30) tmp1[i] = 128.toChar(); i++ }
    if (tmp1[i - 2] == 128.toChar() && tmp1[i - 1] == 128.toChar()) tmp1[i - 1] = '\u0000'
    return mergeSvZb(tmp0, tmp1, sv)
}

// ------------------------------------------------------------------
// Lock family

// int global::Lock() / UnLock()
internal fun GlobalRef.lockG(): Int {
    val atmp = CharArray(32)
    Mltoa(ProcessHandle.current().pid(), atmp) // GETPID()
    val r = makeLockMisc6Zb(ref, "1", cStringOf(atmp), ' ', null)
    svPtr.tpx = (if (r == "1") 1 else 0).toLong()
    return svPtr.tpx.toInt()
}
internal fun GlobalRef.unLockG(): Int {
    val atmp = CharArray(32)
    Mltoa(ProcessHandle.current().pid(), atmp)
    val r = makeLockMisc6Zb(ref, "1", cStringOf(atmp), '-', null)
    svPtr.tpx = (if (r == "1") 1 else 0).toLong()
    return svPtr.tpx.toInt()
}

// void CleanLocks(void)
internal fun cleanLocksZb() {
    val atmp = CharArray(32)
    Mltoa(ProcessHandle.current().pid(), atmp) // free all locks first
    if (svPtr.LockDone != 0) { // remove all locks this job
        cleanLocks(atmp) // the MumpsMain seat (roster 292 family)
    }
}

// void CleanAllLocks(void)
internal fun cleanAllLocksZb() {
    java.io.File("./Mumps.Locks").delete() // remove(); result discarded
}

// ------------------------------------------------------------------
// global Set/Get overloads (the get_global/set_global macros are
// libmpscpp.h:117-118 Mglobal folds with the tpx side-effect; both
// seats mirror the macro expansion exactly)

private fun getGlobalMacroZb(nm: String, rslt: CharArray) {
    svPtr.tpx = Mglobal(RETRIEVE, nm.toCharArray(), rslt, svPtr).toLong()
}
private fun setGlobalMacroZb(nm: String, rslt: CharArray) {
    svPtr.tpx = Mglobal(STORE, nm.toCharArray(), rslt, svPtr).toLong()
}

internal fun GlobalRef.setG(s: String): String {
    val b = CharArray(STR_MAX).also { writeC(it, s) }
    setGlobalMacroZb(ref, b)
    return s
}
internal fun GlobalRef.setG(s: Long): Long {
    val b = CharArray(STR_MAX).also { writeC(it, s.toString()) } // %ld
    setGlobalMacroZb(ref, b)
    return s
}
internal fun GlobalRef.setG(s: Int): Int {
    val b = CharArray(STR_MAX).also { writeC(it, s.toString()) } // %d
    setGlobalMacroZb(ref, b)
    return s
}
internal fun GlobalRef.setG(s: Double): Double {
    val b = CharArray(STR_MAX)
    writeC(b, String.format(java.util.Locale.US, "%f", s)) // h009 seat
    setGlobalMacroZb(ref, b)
    return s
}

internal fun GlobalRef.getG(): String {
    val tmp = CharArray(STR_MAX)
    getGlobalMacroZb(get(), tmp)
    if (svPtr.tpx == 0.toLong()) {
        GlobalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }
    return cStringOf(tmp)
}
internal fun GlobalRef.getGDouble(): Double {
    val tmp = CharArray(STR_MAX)
    getGlobalMacroZb(get(), tmp)
    if (svPtr.tpx == 0.toLong()) {
        GlobalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }
    return strtodC(cStringOf(tmp)) // { *x=atof(tmp); return *x; }
}
internal fun GlobalRef.getGLong(): Long {
    val tmp = CharArray(STR_MAX)
    getGlobalMacroZb(get(), tmp)
    if (svPtr.tpx == 0.toLong()) {
        GlobalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }
    if (svPtr.tpx == 0.toLong()) errorMessage("global not found", -1)
    return atolC(cStringOf(tmp)) // { *x=atol(tmp); return *x; }
}

// void global::Kill()
internal fun GlobalRef.killG(sv: Msv) {
    if (ref.isEmpty()) {
        GlobalExceptionCode = GLOBAL_NOT_FOUND
        System.out.print("Incomplete global reference - no parens \n")
        throw MumpsGlobalException()
    }
    killC(ref, sv) // KiLL(ref, svPtr)
}

// ------------------------------------------------------------------
// Order family (ORDERNEXT/ORDERPREV prepare per the Mglobal block)

// char * global::C_Order(int dir)
internal fun GlobalRef.cOrderG(dir: Int, sv: Msv): CharArray {
    var d = dir
    if (d != 1 && d != -1) errorMessage("Unknown parameter to Order\n", 0)
    d = if (d == 1) ORDERNEXT else ORDERPREV
    val tmp = CharArray(STR_MAX) // member-scratch fold (header ledger)
    svPtr.tpx = Mglobal(d, ref.toCharArray(), tmp, sv).toLong()
    return tmp
}

// mstring global::Order(int dir)
internal fun GlobalRef.orderG(dir: Int, sv: Msv): String =
    cStringOf(cOrderG(dir, sv))

// int global::Order_Next(char * s)
internal fun GlobalRef.orderNextG(s: CharArray, sv: Msv): Int {
    svPtr.tpx = Mglobal(ORDERNEXT, ref.toCharArray(), s, sv).toLong()
    return svPtr.tpx.toInt()
}

// int global::Order_Prior(char * s)
internal fun GlobalRef.orderPriorG(s: CharArray, sv: Msv): Int {
    svPtr.tpx = Mglobal(ORDERPREV, ref.toCharArray(), s, sv).toLong()
    return svPtr.tpx.toInt()
}

// int global::Data() - the globalDataC atol contract (header ledger)
internal fun GlobalRef.dataG(): Int =
    globalDataC(ref, svPtr) // i=strlen(ref) dead store kept in ledger

// ------------------------------------------------------------------
// the mstring Assign family (the "set <name>=\"value\"" envelope)

private fun assignEnvZb(data: String, b: String): String {
    // tmp = "set " + data + "=\"" + b + "\""
    return "set " + data + "=\"" + b + "\""
}

internal fun mstringAssign(data: String, b: String, sv: Msv) {
    val f = Interpret(assignEnvZb(data, b), sv)
    if (f != 0) {
        GlobalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }
}

// void mstring::Assign(global b) - RETRIEVE then char* assign
internal fun mstringAssignG(data: String, b: GlobalRef, sv: Msv) {
    val gtmp = CharArray(STR_MAX)
    val f = Mglobal(RETRIEVE, b.ref.toCharArray(), gtmp, svPtr)
    if (f == 0) {
        GlobalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }
    mstringAssign(data, cStringOf(gtmp), sv)
}

// void mstring::Assign(m/string) - the string/mstring/char*
// overloads fold onto the String face (h007)
internal fun mstringAssign(data: String, b: GlobalRef, sv: Msv) =
    mstringAssignG(data, b, sv)
internal fun mstringAssign(data: String, b: Int, sv: Msv) =
    mstringAssign(data, b.toString(), sv) // tmpsprintf %d
internal fun mstringAssign(data: String, b: Double, sv: Msv) =
    mstringAssign(data, String.format(java.util.Locale.US, "%f", b), sv)
internal fun mstringAssign(data: String, b: Long, sv: Msv) =
    mstringAssign(data, b.toString(), sv) // %ld

// ------------------------------------------------------------------
// SQL envelopes

// int SQL_Disconnect()
internal fun sqlDisconnect(sv: Msv): Int =
    Interpret("sql/c", sv)

// int SQL_Connect(char * s) / SQL_Connect(string s)
internal fun sqlConnect(s: String, sv: Msv): Int {
    val cmd = "sql/d " + s // strcpy/strcat fold
    return Interpret(cmd, sv)
}
