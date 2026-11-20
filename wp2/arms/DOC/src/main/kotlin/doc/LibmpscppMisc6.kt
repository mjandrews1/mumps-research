package doc

// Unit 214 (B7 tranche 3): libmpscpp.cpp::<misc-6> (batch 7,
// complex stratum; physical chunk 740 dump lines, roster loc 600).
// CHUNK BOUNDARY ORPHANS (the misc-chunk template, SymOpsMisc
// precedent): the dump HEAD is the orphan tail of the prior
// chunk's last body: tmp = i; return i; } - the chunk cut
// lands after its return; not translated, ledger); the dump TAIL
// cuts at the mstring global::Concat(double b) OPENER - the
// Concat(x7) member bodies are OUT of this chunk (beyond the
// cut; the one landed member is the unit-65 GlobalConcat render,
// GlobalRef.concat(GlobalRef)). The operator& forwards of this
// chunk call that family: the twelve un-landed overloads become
// Stubs.Concat H-014 seats below (owner named per overload,
// batch-10 ledger). EncodeHTML(mstring &) IS the landed
// EncodeHTML.kt render (hit-reuse, no new top-level here).
// std::string/(char *) overloads fold onto the String seat
// (H-015 collapse, the mstring.lt ladder precedent); the
// FILE* / istream ReadLine twins fold onto one Reader overload
// (H-016, the H-001 Reader domain note); the C++ conversion
// operator ladder renders as the to* extension family (H-010):
// atoi domains (atollShared) for int/short/long/unsigned, atof
// (strtod) for float/double; vendor operator char() is atoi-into-
// a-char (a NUMBER, not a codepoint read). sprintf formats keep
// Locale.US. cvt's C static buffers become fresh Strings (H-018
// JVM divergence, no shared-state alias). cout/ostream emission
// (operator<< for global) has NO Kotlin seat: console-write
// family ledger only, not translated. svPtr is the Globals.svPtr
// file-global carrier; the file-static tmp of global::Get is a
// stale-state vendor quirk (returns the LAST scratch content,
// not the s it just filled) - noted inline. dollars escaped
// per H-015. Naming per H-017 (vendor verbatim).

import java.io.BufferedReader

private const val ORDERNEXT_M6 = 12
private const val ORDERPREV_M6 = 13

// stdin FILE*: one shared buffered reader (fresh-per-call would
// buffer-discard unpiped input; H-018 JVM fold, H-001 domain).
// H-015: reuse of the Getstr2 STDIN_READER (the unit-48 FileDescriptor
// domain; the vendor stdin FILE is the same stream).
private object StdinHolder {
    val br: BufferedReader = java.io.BufferedReader(STDIN_READER)
}

// getpid() of the lock arms: JVM process id (Fcn.kt getpid
// precedent, per-file dup).
private fun getpidM6(): Int = ProcessHandle.current().pid().toInt()

// fgets(tmp,STR_MAX,stream) with the vendor double-trim: the
// newline strip, then the CR-LF < ' ' strip.
private fun fgetsTrimM6(r: BufferedReader): String? {
    val line = r.readLine() ?: return null
    var s = line
    if (s.isNotEmpty() && s[s.length - 1] < ' ') s = s.dropLast(1)
    return s
}

// ---------------- mcvt ladder (vendor :5860-era free fns) ------

fun mcvt(i: Long): Mstring = Mstring(String.format(java.util.Locale.US, "%d", i))

fun mcvt(i: Double): Mstring = Mstring(String.format(java.util.Locale.US, "%g", i))

fun mcvt(i: Float): Mstring = Mstring(String.format(java.util.Locale.US, "%g", i))

fun mcvt(i: Int): Mstring = Mstring(String.format(java.util.Locale.US, "%d", i))

// ---------------- cvt ladder (char* static-buffer twins) ------

fun cvt(i: Long): String = String.format(java.util.Locale.US, "%d", i)

fun cvt(i: Double): String = String.format(java.util.Locale.US, "%g", i)

fun cvt(i: Float): String = String.format(java.util.Locale.US, "%g", i)

fun cvt(i: Int): String = String.format(java.util.Locale.US, "%d", i)

// ---------------- free count (RESOLVES Stubs.count -----------

// int count(char *); libmpscpp.cpp:2932 - the MakeGlobal
// varargs key tally (quotes guard the comma walk; the
// mis-matched-quote ErrorMessage at line 0 kept).
fun count(key: String): Int {
    var i = 0
    var j = 1
    val k = key.toCharArray()

    while (i < k.size && k[i] != NUL) {
        if (k[i] == ',') {
            j++
            i++
            continue
        }

        if (k[i] != '"') {
            i++
            continue
        }

        i++

        while (i < k.size && k[i] != '"' && k[i] != NUL) i++

        if (i >= k.size || k[i] == NUL) Stubs.ErrorMessage("mis-matched quote", 0)
    }

    return j
}

// ---------------- HitRatio ------------------------------------

// double HitRatio(void) - vendor quirk: hit/count is INTEGER
// division (both unsigned long state fields) widened to double
// AFTER the divide; kept. count==0 is the vendor UB / Kotlin
// ArithmeticException crash-shape (ledger).
fun HitRatio(): Double {
    val ratio = (Globals.svPtr.hit / Globals.svPtr.count).toDouble()
    Globals.svPtr.hit = 0
    Globals.svPtr.count = 0
    return ratio
}

// EncodeHTML(mstring &) - hit-reuse: the landed EncodeHTML.kt
// render (no new body here).

// ---------------- global::Name / __test -----------------------

fun GlobalRef.Name(): Mstring = Mstring(Sref)

fun __test(): Int = Globals.svPtr.tpx.toInt()

// ---------------- ReadLine family (global + mstring) ---------

// bool global::ReadLine() - stdin carrier; the Mglobal(STORE)
// happens even on EOF (vendor stores the empty scratch; kept).
fun GlobalRef.ReadLine(): Boolean {
    val tmp = StdinHolder.br.readLine() ?: return false
    var s = tmp
    if (s.isNotEmpty() && s[s.length - 1] < ' ') s = s.dropLast(1)

    val buf = CharArray(STR_MAX)
    CstringOps.strcpyS(buf, s)

    val f = Stubs.Mglobal(STORE, ref, buf, Globals.svPtr)

    if (f == 0) {
        Globals.GlobalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }

    return true
}

// bool global::ReadLine(FILE *u1) / (istream &) twins folded
// onto the Reader domain (H-016, H-001).
fun GlobalRef.ReadLine(u1: java.io.Reader): Boolean {
    val br = u1 as? BufferedReader ?: BufferedReader(u1)
    val s = fgetsTrimM6(br) ?: return false

    val buf = CharArray(STR_MAX)
    CstringOps.strcpyS(buf, s)

    val f = Stubs.Mglobal(STORE, ref, buf, Globals.svPtr)

    if (f == 0) {
        Globals.GlobalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }

    return true
}

// bool mstring::ReadLine() - data = tmp.
fun Mstring.ReadLine(): Boolean {
    val s = StdinHolder.br.readLine() ?: return false
    var t = s
    if (t.isNotEmpty() && t[t.length - 1] < ' ') t = t.dropLast(1)
    str = t
    return true
}

// bool mstring::ReadLine(FILE *) / (istream &) twins folded.
fun Mstring.ReadLine(u1: java.io.Reader): Boolean {
    val br = u1 as? BufferedReader ?: BufferedReader(u1)
    val s = fgetsTrimM6(br) ?: return false
    str = s
    return true
}

// ---------------- mstring conversion operators ----------------
// (H-010 to* fold of the implicit-conversion ladder)

fun Mstring.toInt(): Int = atollShared(str).toInt()

fun Mstring.toFloat(): Float = strtod(str).toFloat()

fun Mstring.toDouble(): Double = strtod(str)

fun Mstring.toChar(): Char = atollShared(str).toInt().toChar()

fun Mstring.toShort(): Short = atollShared(str).toShort()

fun Mstring.toLong(): Long = atollShared(str)

fun Mstring.toULong(): ULong = atollShared(str).toULong()

fun Mstring.toUInt(): UInt = atollShared(str).toUInt()

fun Mstring.toUByte(): UByte = atollShared(str).toUByte()

fun Mstring.toUShort(): UShort = atollShared(str).toUShort()

// ---------------- zBlanks twins -------------------------------

// mstring mstring::zBlanks() - the zblanks zfcn relay; xCE/xF
// delimiters kept as codepoints (byte 0xCE/0xCF carriers).
fun Mstring.zBlanks(): Mstring {
    val tmp = CharArray(STR_MAX)
    val tmp2 = CharArray(STR_MAX)

    CstringOps.copyAtS(tmp, 1, "\$zblanks")
    CstringOps.catAt(tmp, CstringOps.strlenC(tmp), '\u00CE')
    CstringOps.strcpyS(tmp2, str)
    CstringOps.catAt(tmp, CstringOps.strlenC(tmp), tmp2)
    CstringOps.catAt(tmp, CstringOps.strlenC(tmp), '\u00CF')

    Stubs.zfcn(tmp, tmp2, Globals.svPtr)

    return Mstring(CstringOps.cstring(tmp2))
}

// mstring global::zBlanks() - RETRIEVE walk then the member; the
// empty-global return is the C++ implicit mstring(int -1) ctor
// conversion (renders as "-1", quirk ledger).
fun GlobalRef.zBlanks(): Mstring {
    val gtmp = CharArray(STR_MAX)

    val f = Stubs.Mglobal(RETRIEVE, ref, gtmp, Globals.svPtr)

    if (f == 0) {
        Globals.GlobalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }

    if (CstringOps.strlenC(gtmp) == 0) return Mstring("-1") // implicit ctor quirk

    return Mstring(CstringOps.cstring(gtmp)).zBlanks()
}

// ---------------- Ascii family --------------------------------
// Signed-char domain note: the vendor (int)*(char *) reads SIGNED
// chars (codepoints above 127 come out negative there); the
// Kotlin Char.code read is the 0..65535 domain (H-012 divergence,
// ledger).

fun Mstring.Ascii(): Int {
    if (str == "") return -1
    return str[0].code
}

fun GlobalRef.Ascii(): Int {
    val gtmp = CharArray(STR_MAX)

    val f = Stubs.Mglobal(RETRIEVE, ref, gtmp, Globals.svPtr)

    if (f == 0) {
        Globals.GlobalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }

    if (CstringOps.strlenC(gtmp) == 0) return -1

    return gtmp[0].code
}

fun GlobalRef.Ascii(i: Int): Int {
    if (i <= 0) return -1

    val gtmp = CharArray(STR_MAX)

    val f = Stubs.Mglobal(RETRIEVE, ref, gtmp, Globals.svPtr)

    if (f == 0) {
        Globals.GlobalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }

    if (CstringOps.strlenC(gtmp) < i) return -1

    return gtmp[i - 1].code
}

fun Mstring.Ascii(i: Int): Int {
    if (i <= 0) return -1

    if (str.length < i) return -1

    if (str == "") return -1

    return str[i - 1].code
}

// ---------------- Length family -------------------------------

fun Mstring.Length(): Int = str.length

// global::Length() - vendor QUiRK: NO f==0 raise on this member
// (the retrieval-fail scratch reads empty and strlen returns 0);
// kept.
fun GlobalRef.Length(): Int {
    val gtmp = CharArray(STR_MAX)

    Stubs.Mglobal(RETRIEVE, ref, gtmp, Globals.svPtr)

    return CstringOps.strlenC(gtmp)
}

// mstring::Length(char *) / Length(string) folded onto the
// String seat (H-015); the mstring needle keeps its seat.
fun Mstring.Length(needle: String): Int {
    val p = needle
    var i = 1

    if (str.isEmpty()) return 0

    var at = str.indexOf(p)
    while (true) {
        if (at < 0) return i

        i++
        at = str.indexOf(p, at + p.length)
    }
}

fun Mstring.Length(needle: Mstring): Int = Length(needle.str)

// ---------------- Horolog (free fn) ---------------------------

// mstring Horolog() - _horolog has no corpus owner; the body is
// the Fcn.kt zhorolog intrinsic math (H-018 inline precedent,
// 47116 epoch ladder): day,seconds.
fun Horolog(): Mstring {
    var timex = System.currentTimeMillis() / 1000L
    var dayH = timex / 86400
    timex -= dayH * 86400
    dayH = 47116 + dayH
    val fdH = dayH + 1
    return Mstring(fdH.toString() + "," + timex.toString())
}

// char * global::get()
fun GlobalRef.get(): String = ref

// ostream& operator<< (global): no Kotlin seat (console-write
// family ledger; not translated).

// ---------------- global::Merge --------------------------------

// int global::Merge(global rhs) - the 128-quirk tail walk of the
// two refs; ::Merge is the batch-8 roster seat (H-014 stub,
// owner named).
fun GlobalRef.Merge(rhs: GlobalRef): Int {
    val tmp0 = CharArray(STR_MAX)
    val tmp1 = CharArray(STR_MAX)
    tmp0[0] = '^'
    tmp1[0] = '^'
    CstringOps.copyAtS(tmp0, 1, ref)
    CstringOps.copyAtS(tmp1, 1, rhs.ref)

    var i = 0
    while (tmp0[i] != NUL) {
        if (tmp0[i].code < 30) tmp0[i] = '\u0080'
        i++
    }

    if (tmp0[i - 2] == '\u0080' && tmp0[i - 1] == '\u0080') tmp0[i - 1] = NUL

    i = 0
    while (tmp1[i] != NUL) {
        if (tmp1[i].code < 30) tmp1[i] = '\u0080'
        i++
    }

    if (tmp1[i - 2] == '\u0080' && tmp1[i - 1] == '\u0080') tmp1[i - 1] = NUL

    return Stubs.Merge(tmp0, tmp1, Globals.svPtr)
}

// ---------------- Lock / UnLock / CleanLocks -------------------

fun GlobalRef.Lock(): Int {
    val atmp = CharArray(32)
    Stubs.Mltoa(getpidM6(), atmp)

    if (makeLock(ref, "1", CstringOps.cstring(atmp), ' ', "") == "1") Globals.svPtr.tpx = 1
    else Globals.svPtr.tpx = 0

    return Globals.svPtr.tpx.toInt()
}

fun GlobalRef.UnLock(): Int {
    val atmp = CharArray(32)
    Stubs.Mltoa(getpidM6(), atmp)

    if (makeLock(ref, "1", CstringOps.cstring(atmp), '-', "") == "1") Globals.svPtr.tpx = 1
    else Globals.svPtr.tpx = 0

    return Globals.svPtr.tpx.toInt()
}

// void CleanLocks(void) - free all locks of this job first.
fun CleanLocks() {
    val atmp = CharArray(32)
    Stubs.Mltoa(getpidM6(), atmp)

    if (Globals.svPtr.LockDone != 0) cleanLocks(CstringOps.cstring(atmp))
}

// void CleanAllLocks(void) - remove() folds to the JVM delete.
fun CleanAllLocks() {
    java.io.File("./Mumps.Locks").delete()
}

// ---------------- global::Set / Get family ---------------------

fun GlobalRef.Set(s: String): String {
    Stubs.set_global(ref.toCharArray(), s.toCharArray())
    return s
}

fun GlobalRef.Set(s: Long): Long {
    val tmp = CharArray(64)
    CstringOps.strcpyS(tmp, String.format(java.util.Locale.US, "%d", s))
    Stubs.set_global(ref.toCharArray(), tmp)
    return s
}

fun GlobalRef.Set(s: Int): Int {
    val tmp = CharArray(64)
    CstringOps.strcpyS(tmp, String.format(java.util.Locale.US, "%d", s))
    Stubs.set_global(ref.toCharArray(), tmp)
    return s
}

fun GlobalRef.Set(s: Double): Double {
    val tmp = CharArray(64)
    CstringOps.strcpyS(tmp, String.format(java.util.Locale.US, "%f", s))
    Stubs.set_global(ref.toCharArray(), tmp)
    return s
}

// char * global::Get(char * s) - vendor QUIRK: returns the class-
// static tmp (stale scratch), NOT the s it filled; the render
// returns the s buffer read (H-012 stale-state not reproducible,
// ledger).
fun GlobalRef.Get(s: CharArray): String {
    Stubs.get_global(ref.toCharArray(), s)

    if (Globals.svPtr.tpx == 0L) {
        Globals.GlobalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }

    return CstringOps.cstring(s)
}

// string global::Get(string &s) - the out-param kept as the
// StringBuilder carrier.
fun GlobalRef.Get(s: StringBuilder): String {
    val rslt = CharArray(STR_MAX)
    Stubs.get_global(ref.toCharArray(), rslt)

    if (Globals.svPtr.tpx == 0L) {
        Globals.GlobalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }

    val x = CstringOps.cstring(rslt)
    s.setLength(0)
    s.append(x)
    return x
}

// char * global::Get()
fun GlobalRef.Get(): String {
    val rslt = CharArray(STR_MAX)
    Stubs.get_global(ref.toCharArray(), rslt)

    if (Globals.svPtr.tpx == 0L) {
        Globals.GlobalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }

    return CstringOps.cstring(rslt)
}

// double global::Get(double *)
fun GlobalRef.Get(x: DoubleArray): Double {
    val rslt = CharArray(STR_MAX)
    Stubs.get_global(ref.toCharArray(), rslt)

    if (Globals.svPtr.tpx == 0L) {
        Globals.GlobalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }

    x[0] = strtod(CstringOps.cstring(rslt))
    return x[0]
}

// long global::Get(long *) - the SECOND tpx ErrorMessage guard
// (dead belt after the raise throws; H-013 dead-branch, kept).
fun GlobalRef.Get(x: LongArray): Long {
    val rslt = CharArray(STR_MAX)
    Stubs.get_global(ref.toCharArray(), rslt)

    if (Globals.svPtr.tpx == 0L) {
        Globals.GlobalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }

    if (Globals.svPtr.tpx == 0L) Stubs.ErrorMessage("global not found", -1)

    x[0] = atollShared(CstringOps.cstring(rslt))
    return x[0]
}

// ---------------- global::Kill ---------------------------------

fun GlobalRef.Kill() {
    if (ref.isEmpty()) {
        Globals.GlobalExceptionCode = GLOBAL_NOT_FOUND
        println("Incomplete global reference - no parens ")
        throw MumpsGlobalException()
    }

    KiLL(ref.toCharArray(), Globals.svPtr)
}

// ---------------- global::Order family --------------------------

fun GlobalRef.Order(dir: Int): Mstring {
    return Mstring(C_Order(dir))
}

fun GlobalRef.C_Order(dirIn: Int): String {
    var dir = dirIn

    if (dir != 1 && dir != -1) Stubs.ErrorMessage("Unknown parameter to Order\n", 0)

    if (dir == 1) dir = ORDERNEXT_M6
    else dir = ORDERPREV_M6

    val rslt = CharArray(STR_MAX)
    Globals.svPtr.tpx = Stubs.Mglobal(dir, ref, rslt, Globals.svPtr).toLong()

    return CstringOps.cstring(rslt)
}

fun GlobalRef.Order_Next(s: CharArray): Int {
    Globals.svPtr.tpx = Stubs.Mglobal(ORDERNEXT_M6, ref, s, Globals.svPtr).toLong()
    return Globals.svPtr.tpx.toInt()
}

fun GlobalRef.Order_Prior(s: CharArray): Int {
    Globals.svPtr.tpx = Stubs.Mglobal(ORDERPREV_M6, ref, s, Globals.svPtr).toLong()
    return Globals.svPtr.tpx.toInt()
}

// int global::Data() - GlobalData is the landed T2 render; the
// static char tmp[4] vendor buffer kept (H-012); i=strlen(ref)
// is the dead pre-read (H-013, kept, overwritten by atol).
fun GlobalRef.Data(): Int {
    val tmp = CharArray(4)
    var i: Long = ref.length.toLong()

    GlobalData(ref.toCharArray(), tmp, Globals.svPtr)
    i = atollShared(CstringOps.cstring(tmp))
    return i.toInt()
}

// ---------------- mstring::Assign x7 ----------------------------

// Assign(char *) - the backend: set <data>="<b>" through the
// interpreter (the operator= route; SymGet-side effect).
fun Mstring.Assign(b: String) {
    val tmp = CharArray(STR_MAX)
    CstringOps.strcpyS(tmp, "set ")
    CstringOps.copyAtS(tmp, 4, str)
    CstringOps.catAtS(tmp, CstringOps.strlenC(tmp), "=\"")
    CstringOps.catAtS(tmp, CstringOps.strlenC(tmp), b)
    CstringOps.catAtS(tmp, CstringOps.strlenC(tmp), "\"")
    val f = Stubs.Interpret(CstringOps.cstring(tmp), Globals.svPtr)

    if (f != 0) {
        Globals.GlobalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }
}

// void mstring::Assign(string b) - folded onto the String seat.
// void mstring::Assign(mstring b)
fun Mstring.Assign(b: Mstring) {
    Assign(b.str)
}

// void mstring::Assign(global b)
fun Mstring.Assign(b: GlobalRef) {
    val gtmp = CharArray(STR_MAX)

    val f = Stubs.Mglobal(RETRIEVE, b.ref, gtmp, Globals.svPtr)

    if (f == 0) {
        Globals.GlobalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }

    Assign(CstringOps.cstring(gtmp))
}

fun Mstring.Assign(b: Int) {
    Assign(String.format(java.util.Locale.US, "%d", b))
}

fun Mstring.Assign(b: Double) {
    Assign(String.format(java.util.Locale.US, "%f", b))
}

fun Mstring.Assign(b: Long) {
    Assign(String.format(java.util.Locale.US, "%d", b))
}

// ---------------- SQL relay family ------------------------------

fun SQL_Disconnect(): Int {
    return Stubs.Interpret("sql/c", Globals.svPtr)
}

// SQL_Connect(char *) - string/mstring twins folded (String seat).
fun SQL_Connect(s: String): Int {
    return Stubs.Interpret("sql/d " + s, Globals.svPtr)
}

fun SQL_Connect(s: Mstring): Int {
    return SQL_Connect(s.str)
}

fun SQL_Format(): Int {
    return Stubs.Interpret("sql/c ", Globals.svPtr)
}

fun SQL_Format(s: String): Int {
    return Stubs.Interpret("sql/c " + s, Globals.svPtr)
}

fun SQL_Format(s: Mstring): Int {
    return SQL_Format(s.str)
}

fun SQL_Command(s: String): Int {
    return Stubs.Interpret("sql " + s, Globals.svPtr)
}

fun SQL_Command(s: Mstring): Int {
    return SQL_Command(s.str)
}

// mstring SQL_Table() - \$ztable Eval relay.
fun SQL_Table(): Mstring {
    var m = Mstring("\$ztable")
    m = m.eval()
    return m
}

// SQL_Table(mstring) forward to the landed unit-34 render.
fun SQL_Table(s: Mstring, c: Int): Mstring = SQL_Table(s.str, c)

fun SQL_Message(): Mstring {
    var m = Mstring("\$zsql")
    m = m.eval()
    return m
}

fun SQL_Open(): Boolean {
    var m = Mstring("\$zsqlOpen")
    m = m.eval()

    if (m.toInt() == 1) return true
    else return false
}

fun SQL_Native(): Boolean {
    var m = Mstring("\$znative")
    m = m.eval()

    if (m.toInt() == 1) return true
    else return false
}

// ---------------- operator& forwards (Concat family) -----------
// Kotlin has no & operator (H-016): amp names the vendor
// operator& forward; the bodies are the vendor one-line
// (*this).Concat(b) relays to the Concat(x7) member family. The
// global.global arm of Concat IS the landed unit-65 render; the
// other twelve overload seats are raised in Stubs (owner
// libmpscpp.cpp::Concat, out of this chunk's cut; batch-10
// ledger).

fun GlobalRef.amp(b: Double): Mstring = Stubs.Concat(this, b)

fun GlobalRef.amp(b: Long): Mstring = Stubs.Concat(this, b)

fun GlobalRef.amp(b: Int): Mstring = Stubs.Concat(this, b)

fun GlobalRef.amp(b: GlobalRef): Mstring = this.concat(b)

fun GlobalRef.amp(b: CharArray): Mstring = Stubs.Concat(this, b)

fun GlobalRef.amp(b: String): Mstring = Stubs.Concat(this, b)

fun GlobalRef.amp(b: Mstring): Mstring = Stubs.Concat(this, b)

fun Mstring.amp(b: Double): Mstring = Stubs.Concat(this, b)

fun Mstring.amp(b: Long): Mstring = Stubs.Concat(this, b)

fun Mstring.amp(b: Int): Mstring = Stubs.Concat(this, b)

fun Mstring.amp(b: Mstring): Mstring = Stubs.Concat(this, b)

fun Mstring.amp(b: CharArray): Mstring = Stubs.Concat(this, b)

fun Mstring.amp(b: GlobalRef): Mstring = Stubs.Concat(this, b)

fun Mstring.amp(b: String): Mstring = Stubs.Concat(this, b)
