package both

// Unit 154 (BOTH B5 T3, complex): libmpscpp.cpp::<misc-5> (600
// physical LOC, 719 raw chunk lines, score 319.2). The residual
// C++-binding chunk: the mstring comparison operator tails, the
// builtin service wrappers (thrownMessage/__finish), the global
// member extract family (Int/Double/Mstring/Char), the Stem and
// token wrappers, the zfcn envelope family (Dump/Restore/Token),
// the mdh GTK bridge head, the file-position seats (ZSeek/Ztell),
// the Transpose wrappers and the global::Count/Transpose member,
// the stem/Hash/pm family, the GlobalSet/GlobalGet xecute
// envelopes, and the mcvt fragment at the chunk boundary.
//
// CHUNK BOUNDARY NOTE: the physical chunk opens mid-function (the
// tail of the previous misc-4 operator, "else return false; }" -
// the operator limbs that begin in misc-4 are NOT re-rendered
// here) and closes mid-function (mstring mcvt (char * i) { ... }
// - the visible half renders below as a documented fragment; the
// continuation lands with libmpscpp.cpp::<misc-6>, roster 294,
// batch 7). Both boundaries journaled.
//
// Quirks kept (ledgered):
//   - GlobalSet: the vendor carries a stray line-splice
//     backslash after every envelope build ("\\\\" at end of
//     line: a C line continuation, a no-op); kept as this note,
//     not reproduced. The envelope performs NO quote escaping on
//     x/y (vendor-identical; an embedded quote in the value is
//     UB-shaped in the vendor too - journaled, not fixed).
//   - pt(char*, char*): the vendor index i is UNINITIALIZED in
//     the both-condition ("while(x[i])"); the read is C UB. The
//     fold quantizes to a 0 start (the only deterministic
//     choice; journaled).
//   - Perl: both vendor bodies are commented out; the live body
//     is "return svPtr->tpx" only. Kept exactly.
//   - TokenInit: the vendor assigns its mstring result from
//     svPtr->bd (NOT from the zfcn output buffer tmp2) - kept
//     faithfully below.
//   - ZSeek (mstring arm): on sscanf failure the vendor leaves
//     fpos_t offset UNINITIALIZED (UB); the fold quantizes to a
//     0 offset (journaled).
//   - mcvt fragment: the vendor returns i (the argument), not
//     the tmp it just built - the visible half keeps the return
//     shape exactly (tmp is dead past the assignment).
//   - mstring::operator== / != over char*: char*, const char*
//     and string all fold onto the String arm below (three
//     vendor overloads, identical bodies - the fold mapping is
//     journaled per h007).
//
// BOTH consults (find_rule before render, events logged):
//   h007 char_buffer - reused: every strcat chain folds onto a
//     String build; the zfcn envelopes build CharArray inputs.
//   h008 static_scratch - hit: the static seats dump to fresh
//     values (Hash result, GlobalGet char* r, the calendar and
//     chooser statics, the indirect carrier).
//   h012 unsigned_byte_domain - reused: the coded markers are
//     Char-domain values (0xCE/0xCF/0xD0 kept as u-escapes).
//   h013 dead_store - reused: the Perl bodies' commented-out
//     call lines stay comments; the mcvt tmp assignment keeps
//     its dead-shape note.
//   h014 out_of_scope_call - reused: zfcn/xecute/SymGet/Mglobal
//     ride the BothCRuntime seats; wstem/pm/alloc-family and the
//     global-class member seats (extractValue, assignValue,
//     order/kill/valuation) ride the BothStubs carriers (layout
//     owner globalClass.h::<misc> roster 259, pending).
//   h015 grammar_collision - hit: the vendor operator*(string)
//     seat would collide with String.times (repetition); the
//     fold names it indirectOf (journaled).
//   h017 naming - hit: vendor names kept (thrownMessage,
//     __finish, Stem, TokenInit, Token, Dump, Restore, Getenv,
//     ZSeek, Ztell, Perl, DocCorrelate, pt, Count, Transpose,
//     stem, Hash, lhash, GlobalSet, GlobalGet, pmw, mcvt,
//     mdh_*); the comparison-operator members fold to the
//     mstringLessEqual/mstringEquals/mstringNotEquals family
//     (mstringLength precedent).
//   MISS class_member_carrier (recurrence): every mstring/global
//     member below passes its receiver state explicitly (T1
//     recurrence; capture candidate at batch close).
//
// -------------------------------------------------------------------------
// mstring comparison operator tails (the chunk-complete limbs;
// the int/long/double/float/char*/string <= limbs opened in the
// misc-4 chunk and stay there).

// bool mstring::operator<=(global i) - data <= (i >> t1).
internal fun String.mstringLessEqual(i: GlobalRef): Boolean {
    val t1 = i.extractValue() // i >> t1
    return this <= t1 // if (data<=t1) return true; else return false;
}

// bool mstring::operator<=(mstring i)
internal fun String.mstringLessEqual(i: String): Boolean = this <= i

// bool mstring::operator==(int i) / (long i): long j=atol(data).
internal fun String.mstringEquals(i: Int): Boolean =
    this.mstringEquals(i.toLong())

// bool mstring::operator==(long i)
internal fun String.mstringEquals(i: Long): Boolean = atolC(this) == i

// bool mstring::operator==(double i)
internal fun String.mstringEquals(i: Double): Boolean = strtodC(this) == i

// bool mstring::operator==(float i): double j=atof(data)
internal fun String.mstringEquals(i: Float): Boolean = mstringEquals(i.toDouble())

// bool mstring::operator==(const char */char */string i):
// char*, const char* and string fold onto the String arm.
internal fun String.mstringEquals(i: String): Boolean = this == i

// bool mstring::operator==(global i)
internal fun String.mstringEquals(i: GlobalRef): Boolean {
    val t1 = i.extractValue() // i >> t1
    return this == t1
}

// bool mstring::operator!=(int i) / (long i): long j=atol(data).
internal fun String.mstringNotEquals(i: Int): Boolean =
    this.mstringNotEquals(i.toLong())

internal fun String.mstringNotEquals(i: Long): Boolean = atolC(this) != i

// bool mstring::operator!=(double i)
internal fun String.mstringNotEquals(i: Double): Boolean = strtodC(this) != i

// bool mstring::operator!=(float i)
internal fun String.mstringNotEquals(i: Float): Boolean =
    mstringNotEquals(i.toDouble())

// bool mstring::operator!=(char */const char */string i)
internal fun String.mstringNotEquals(i: String): Boolean = this != i

// bool mstring::operator!=(global i)
internal fun String.mstringNotEquals(i: GlobalRef): Boolean {
    val t1 = i.extractValue() // i >> t1
    return this != t1
}

// atol(str) - stdlib fold (not a corpus unit): optional leading
// sign + digits; junk quantizes to 0 (vendor atol contract).
// atolC promoted internal at B6 T1 (2026-11-22): the atoi/atol
// quiet-parse seats of the unit-156 (globalFind) and unit-158
// (SmithWaterman) renders ride this fold; the body is unchanged.
internal fun atolC(s0: String): Long {
    var s = s0.trim { it == ' ' }
    var sign = 1L
    if (s.startsWith("-")) { sign = -1L; s = s.substring(1) }
    else if (s.startsWith("+")) s = s.substring(1)
    var j = 0L
    var k = 0
    while (k < s.length && s[k] in '0'..'9') { j = j * 10 + (s[k] - '0'); k++ }
    return sign * j
}

// -------------------------------------------------------------------------
// BUILTIN FUNCTIONS

// void thrownMessage() - libmpscpp.cpp builtin diagnostic.
internal fun thrownMessage() {
    System.out.print("Mumps has detetected an uncaught exception: " + "\n")
    // the vendor misspells "detetected"; kept byte-faithful.

    if (GlobalExceptionCode == INVALID_CONVERSION)
        System.out.print("Invalid conversion from global to numeric" + "\n")

    if (GlobalExceptionCode == GLOBAL_NOT_FOUND)
        System.out.print("Global not found" + "\n")

    if (GlobalExceptionCode == INPUT_LENGTH)
        System.out.print("Input Line Too Long" + "\n")
}

// The file-scope flg seat for __finish (vendor: static int flg).
private var finishFlg = 0

// void __finish() - terminate: immediate exit when re-entered,
// else GlobalClose + record + exit(0).
internal fun __finish() {
    if (finishFlg != 0) {
        kotlin.system.exitProcess(0) // _exit(0): immediate terminate
    }

    val buf0 = CharArray(STR_MAX) // GlobalClose buffer pair
    val buf1 = CharArray(STR_MAX)
    Mglobal(CLOSE, buf0, buf1, svPtr) // GlobalClose macro seat
    finishFlg = 1
    kotlin.system.exitProcess(0) // exit(0)
}

// -------------------------------------------------------------------------
// global member family (receiver: the GlobalRef carrier).

// int global::Int(): int i; (*this) >> i; return i;
internal fun GlobalRef.Int(): Int {
    return extractValue().let { atolC(it).toInt() }
}

// double global::Double()
internal fun GlobalRef.Double(): Double = strtodC(extractValue())

// mstring global::Mstring() - mstring result folds to String
// (miss class_member_carrier recurrence).
internal fun GlobalRef.Mstring(): String = extractValue()

// char * global::Char(char * i, int max): tmp extract + strncpy.
internal fun GlobalRef.Char(i: CharArray, max: Int): CharArray {
    val tmp = extractValue() // char tmp[STR_MAX]; (*this)>>tmp;
    strncpyC(i, tmp, max) // strncpy(i, tmp, max)
    return i
}

// -------------------------------------------------------------------------
// Stem / token wrappers.

// mstring Stem(mstring &arg): wstem over the C buffer.
internal fun Stem(arg: String): String {
    val tmp = CharArray(STR_MAX)
    writeC(tmp, arg) // strcpy(tmp, arg.c_str())
    wstem(tmp, cLenOf(tmp)) // wstem(tmp, strlen(tmp))
    return cStringOf(tmp) // mstring tmp3; tmp3 = tmp;
}

// strlen on a NUL-terminated CharArray (private helper).
private fun cLenOf(buf: CharArray): Int {
    var n = 0
    while (n < buf.size && buf[n] != 0.toChar()) n++
    return n
}

// mstring TokenInit(mstring &arg): the \$zwi envelope.
internal fun TokenInit(arg: String): String {
    val tmp = CharArray(STR_MAX)
    writeC(tmp, " \$zwi\u00CE") // char tmp[STR_MAX] = " \$zwi\xce"
    val tmpS = cStringOf(tmp) + arg + "\u00CF" // strcat arg + "\xcf"
    val input = CharArray(STR_MAX)
    writeC(input, tmpS)
    val tmp2 = CharArray(STR_MAX)
    zfcn(input, tmp2, svPtr)
    return cStringOf(svPtr.bd) // rslt = (char *) svPtr->bd (quirk)
}

// mstring Token(): the \$zwp envelope.
internal fun Token(): String {
    val tmp = CharArray(STR_MAX)
    writeC(tmp, " \$zwp")
    val tmp2 = CharArray(STR_MAX)
    zfcn(tmp, tmp2, svPtr)
    return cStringOf(tmp2) // mstring rslt; rslt = tmp2;
}

// -------------------------------------------------------------------------
// mdh bridge head. The zfcn-based dialog arm renders here; the
// direct gtk_* wrappers ride the BothStubs platform stubs.

// int mdh_dialog_new_with_buttons(GtkWindow *win, char * text):
// the \$z~mdh~dialog~new~with~buttons envelope over zfcn. The
// GtkWidget*/window pointer seats as a String handle (the sprintf
// "%p" pin seat - GTK pointer domain absent in the JVM fold;
// journaled).
internal fun mdh_dialog_new_with_buttons(win: String, text: String): Int {
    val tmp = " \$z~mdh~dialog~new~with~buttons\u00CE" + win + "\u00D0" +
        text + "\u00CF"
    val tmp1 = CharArray(STR_MAX)
    zfcn(tmp.toCharArray(), tmp1, svPtr) // CharArray input (h007)

    if (tmp1[0] == '0') return 0
    return 1
}

// The direct gtk_* mdh wrappers seat in BothStubs (platform
// stubs, named owners): mdh_toggle_button_get_active,
// mdh_switch_get_active, mdh_toggle_button_set_active,
// mdh_entry_get_text, mdh_range_get_adjustment,
// mdh_calendar_get_date, mdh_entry_set_text,
// mdh_text_buffer_set_text, mdh_font_chooser_get_font,
// mdh_color_chooser_get_rgba, mdh_label_set_text,
// mdh_widget_hide, mdh_widget_show,
// mdh_tree_selection_get_selected, mdh_tree_store_clear,
// mdh_spin_button_get_value, mdh_spin_button_set_value.

// -------------------------------------------------------------------------
// zfcn envelope family.

// void Dump(mstring &arg) / Dump(string &arg): the \$zcd envelope.
internal fun Dump(arg: String) {
    val tmp = "\$zcd\u00CE" + arg + "\u00CF"
    val tmp2 = CharArray(STR_MAX)
    zfcn(tmp.toCharArray(), tmp2, svPtr)
}

// void Dump(char * arg) - char* arm.
internal fun Dump(arg: CharArray) {
    val tmp = "\$zcd\u00CE" + cStringOf(arg) + "\u00CF"
    val tmp2 = CharArray(STR_MAX)
    zfcn(tmp.toCharArray(), tmp2, svPtr)
}

// void Restore(mstring &arg) / Restore(string &arg): \$zcl.
internal fun Restore(arg: String) {
    val tmp = "\$zcl\u00CE" + arg + "\u00CF"
    val tmp2 = CharArray(STR_MAX)
    zfcn(tmp.toCharArray(), tmp2, svPtr)
}

// void Restore(char * arg)
internal fun Restore(arg: CharArray) {
    val tmp = "\$zcl\u00CE" + cStringOf(arg) + "\u00CF"
    val tmp2 = CharArray(STR_MAX)
    zfcn(tmp.toCharArray(), tmp2, svPtr)
}

// -------------------------------------------------------------------------
// environment + file-position seats.

// void Getenv(unsigned char * out, unsigned char * in)
internal fun Getenv(out: CharArray, inName: String) {
    val p1 = System.getenv(inName) // char * p1 = getenv(in)

    if (p1 == null) {
        writeC(out, "") // strcpy(out,"")
        return
    }
    writeC(out, p1) // strcpy(out, p1)
}

// FILE *u seats on RandomAccessFile: the fpos_t get/set pair maps
// onto getFilePointer/seek (the fpos_t domain; h001 note: the
// DAF big-three used an IntArray handle domain, THIS arm seats
// the C stdio FILE* on RandomAccessFile - the fold is journaled
// as an arm-local decision, divergence recorded in the fragment).

// bool ZSeek(FILE *u, mstring &s): sscanf "%lld" + fsetpos.
internal fun ZSeek(u: java.io.RandomAccessFile, s: String): Boolean {
    val offset = s.toLongOrNull() ?: 0L // sscanf-fail UB quantized to 0
    return try {
        u.seek(offset) // !fsetpos(u, &offset)
        true
    } catch (e: java.io.IOException) {
        false
    }
}

// bool Ztell(FILE *u, mstring &s): fgetpos + "%lld" onto s.
// Fold: the in-out mstring out-param returns the value String;
// the vendor bool is constant true on the fgetpos-0 path (the
// error arm returns false - kept via the try).
internal fun Ztell(u: java.io.RandomAccessFile): String {
    // i = fgetpos(u,&offset); sprintf(tmp,"%lld",offset); s = tmp;
    val pos = u.filePointer
    return pos.toString() // "%lld" (i==0 on the good path)
}

// bool ZSeek(FILE *u, global &s): global extract arm.
internal fun ZSeek(u: java.io.RandomAccessFile, s: GlobalRef): Boolean {
    val tmp1 = s.extractValue() // char tmp1[STR_MAX]; s >> tmp1;
    val offset = tmp1.toLongOrNull() ?: 0L
    return try {
        u.seek(offset)
        true
    } catch (e: java.io.IOException) {
        false
    }
}

// bool Ztell(FILE *u, global &s): position import into the global.
internal fun Ztell(u: java.io.RandomAccessFile, s: GlobalRef): Boolean {
    val pos = u.filePointer
    s.assignValue(pos.toString()) // s = tmp (global-value seat)
    return true
}

// -------------------------------------------------------------------------
// Perl seats.

// int Perl(mstring &a, mstring &b) / (mstring &a, char *b):
// both vendor bodies are commented out; the live return below is
// exactly the vendor's. The char* arm folds onto this String arm.
internal fun Perl(a: String, b: String): Int {
    // svPtr->tpx = perlcpp("", a, b, svPtr); - commented in vendor
    return svPtr.tpx.toInt()
}

// -------------------------------------------------------------------------
// DocCorrelate member family.

// void global::DocCorrelate(global B, char * fcn, double threshold)
// / (global B, mstring fcn, double threshold): both arms fold the
// fcn to String and delegate to the string-arm member stub.
internal fun GlobalRef.DocCorrelate(B: GlobalRef, fcn: String, threshold: Double) {
    docCorrelate(B, fcn, threshold) // layout-owner seat (roster 259)
}

// -------------------------------------------------------------------------
// pt: debug print helper.

// void pt(char *t, char *x) - UNINITIALIZED i quirk: quantized 0.
internal fun pt(t: String, x: CharArray) {
    System.out.print(t + "  ") // printf("%s  ", t)

    var i = 0 // vendor leaves i uninitialized (UB); 0-quantized
    while (i < x.size && x[i] != 0.toChar()) {
        System.out.print(" " + x[i] + " ")
        i++
    }
    System.out.print("\n")
}

// -------------------------------------------------------------------------
// global::Count - REAL BODY: the XNEXT order walk under the
// receiver's subscript family (strncmp prefix boundary kept).

// long global::Count()
internal fun GlobalRef.Count(): Long {
    // char ttt[STR_MAX], tmp[STR_MAX]; long j=0; strcpy(tmp, ref);
    val ttt = CharArray(STR_MAX)
    val tmp = CharArray(STR_MAX)
    writeC(tmp, ref)
    var i = cLenOf(tmp) // i = strlen(tmp)

    // if (tmp[i-1]<32 && tmp[i-2]<32) tmp[--i]=0; (kept exactly)
    if (i >= 2 && tmp[i - 1].code < 32 && tmp[i - 2].code < 32) {
        tmp[i - 1] = 0.toChar()
        i--
    }

    var j = 0L
    while (true) {
        // if (Mglobal(XNEXT, tmp, ttt, svPtr)==0) break
        if (Mglobal(XNEXT, tmp, ttt, svPtr) == 0) break

        // if (strncmp(ref, tmp, i)!=0) break
        if (strncmpC(ref, cStringOf(tmp), i) != 0) break

        j++
    }

    return j
}

// -------------------------------------------------------------------------
// Transpose member and wrappers. The T2 BothStubs transpose
// stub flipped in this push (the member body lives there, over
// the order/kill/valuation carrier seats); the file wrappers
// below ride the GlobalRef carriers.

// void Transpose(char * a, char * b): the char* wrapper.
internal fun Transpose(a: CharArray, b: CharArray) {
    val A = GlobalRef(cStringOf(a)) // global A(a), B(b)
    val B = GlobalRef(cStringOf(b))
    transposeWalk(A, B)
}

// void Transpose(mstring a, mstring b): the mstring wrapper
// (folds onto the same walk; both vendor wrappers are identical).
internal fun Transpose(a: String, b: String) {
    val A = GlobalRef(a)
    val B = GlobalRef(b)
    transposeWalk(A, B)
}

// The shared vendor wrapper body (kept once; h007 fold):
//   string aa="", bb="";
//   while (1) { aa = A(aa).Order(1); if (aa=="") break;
//     bb=""; while (1) { bb = A(aa,bb).Order(1); if (bb=="") break;
//       B(bb,aa) = A(aa,bb); } }
private fun transposeWalk(A: GlobalRef, B: GlobalRef) {
    var aa = ""
    while (true) {
        aa = A(aa).order(1) // aa = (string) A(aa).Order(1)
        if (aa == "") break

        var bb = ""
        while (true) {
            bb = A(aa, bb).order(1) // bb = (string) A(aa,bb).Order(1)
            if (bb == "") break
            // B(bb,aa) = A(aa,bb)
            B(bb, aa).setValue(A(aa, bb).value())
        }
    }
}

// -------------------------------------------------------------------------
// stem family (wstem seats in BothStubs, roster 164 batch 10).

// string stem(string &word) / mstring stem(mstring &word): the
// in-out arms rebuild the caller buffer AND return the result.
internal fun stem(word: StringBuilder): String {
    // static char tmp[STR_MAX]; strcpy(tmp, word.c_str());
    val tmp = CharArray(STR_MAX)
    writeC(tmp, word.toString())
    wstem(tmp, cLenOf(tmp)) // int i = wstem(tmp, word.length())
    val res = cStringOf(tmp)
    word.setLength(0) // word = tmp (the in-out import)
    word.append(res)
    return res
}

// char * stem(char * word): in-place char buffer arm.
internal fun stem(word: CharArray): CharArray {
    wstem(word, cLenOf(word))
    return word
}

// -------------------------------------------------------------------------
// Hash family. The vendor static char result[32] / static fold is
// h008: every return below is a fresh value.

// char * Hash(const char * str): the 4-byte chunk walk with
// "%lu" formatting.
internal fun Hash(str: String): String {
    val inB = str // copy in (vendor strcpy into zeroed buffer)
    var x = 0UL // unsigned long x=0

    var i = 0
    while (i < inB.length && inB[i] != 0.toChar()) {
        // a = the 4 bytes at in[i..i+3] little-endian; past-NUL
        // reads see the zeroed buffer (fold: 0).
        var a = 0UL
        for (k in 0..3) {
            val idx = i + k
            val c = if (idx < inB.length && inB[idx] != 0.toChar())
                inB[idx].code.toULong() else 0UL
            a = a or (c shl (8 * k))
        }
        x = x + a
        i += 4
    }

    return x.toString() // sprintf(result, "%lu", x)
}

// unsigned long lhash(const char * str): same walk, raw return.
internal fun lhash(str: String): ULong {
    val inB = str
    var x = 0UL

    var i = 0
    while (i < inB.length && inB[i] != 0.toChar()) {
        var a = 0UL
        for (k in 0..3) {
            val idx = i + k
            val c = if (idx < inB.length && inB[idx] != 0.toChar())
                inB[idx].code.toULong() else 0UL
            a = a or (c shl (8 * k))
        }
        x = x + a
        i += 4
    }

    return x
}

// -------------------------------------------------------------------------
// indirect operator* seat.

// indirect operator*(string a): vendor builds a static indirect
// and returns it by value; fresh fold (h008). The Kotlin times
// operator is reserved for repetition, so the seat is named
// (h015 grammar_collision, journaled).
internal fun indirectOf(a: String): Indirect {
    val b = Indirect(a) // b.reference = a
    return b
}

// -------------------------------------------------------------------------
// GlobalSet family (7 vendor overloads fold to 4 Kotlin arms:
// mstring/string -> String, char* -> CharArray).

// int GlobalSet(mstring x, mstring y) / (string x, string y)
internal fun GlobalSet(x: String, y: String): Int = globalSetCore(x, y)

// int GlobalSet(char * x, mstring y) / (char * x, string y)
internal fun GlobalSet(x: CharArray, y: String): Int =
    globalSetCore(cStringOf(x), y)

// int GlobalSet(mstring x, char * y) / (string x, char * y)
internal fun GlobalSet(x: String, y: CharArray): Int =
    globalSetCore(x, cStringOf(y))

// int GlobalSet(char * x, char * y)
internal fun GlobalSet(x: CharArray, y: CharArray): Int =
    globalSetCore(cStringOf(x), cStringOf(y))

// The shared envelope (the stray vendor line-splice backslash
// after the build is a no-op; kept as the header note).
private fun globalSetCore(x: String, y: String): Int {
    val tmp = "s " + x + "=\"" + y + "\"" // strcat chain (h007)

    if (xecute(tmp) == 0) {
        svPtr.tpx = 1
    } else {
        svPtr.tpx = 0
    }

    return svPtr.tpx.toInt() // return svPtr->tpx
}

// -------------------------------------------------------------------------
// GlobalGet family (3 vendor overloads fold to 2 arms; the
// static char r[STR_MAX] in the char* arm is h008-fresh).

// mstring GlobalGet(mstring a) / string GlobalGet(string a)
internal fun GlobalGet(a: String): String {
    val tmp = "set %=" + a // char tmp[STR_MAX]="set %=" + strcat a

    if (xecute(tmp) == 0) {
        svPtr.tpx = 1
        return SymGet("%") // rr = SymGet("%")
    }
    svPtr.tpx = 0
    return "" // rr = ""
}

// char * GlobalGet(char * a) - the char-buffer arm (fresh r).
internal fun GlobalGet(a: CharArray): CharArray {
    val tmp = "set %=" + cStringOf(a)
    val r = CharArray(STR_MAX) // static char r -> h008 fresh

    if (xecute(tmp) == 0) {
        svPtr.tpx = 1
    } else {
        svPtr.tpx = 0
    }
    val t = SymGet("%") // t = SymGet("%") (both arms read %)
    writeC(r, t) // strcpy(r, t.c_str())
    return r
}

// -------------------------------------------------------------------------
// pmw family (5 vendor overloads fold to 4 arms: mstring -> String
// merges with string; char* stays CharArray; pm is the non-unit
// interp-pm.cpp body seated in BothStubs).

// int pmw(char *a, char *b, struct MSV *svPtr)
internal fun pmw(a: CharArray, b: CharArray, svPtr: Msv): Int =
    pm(a, b, svPtr)

// int pmw(string a, char * b, struct MSV *svPtr)
internal fun pmw(a: String, b: CharArray, svPtr: Msv): Int =
    pm(a.toCharArray(), b, svPtr) // (unsigned char*)a.c_str()

// int pmw(string a, string b, ...) / pmw(mstring a, mstring b, ...)
internal fun pmw(a: String, b: String, svPtr: Msv): Int =
    pm(a.toCharArray(), b.toCharArray(), svPtr)

// int pmw(char *a, string b, struct MSV *svPtr)
internal fun pmw(a: CharArray, b: String, svPtr: Msv): Int =
    pm(a, b.toCharArray(), svPtr)

// -------------------------------------------------------------------------
// mcvt: the CHUNK-BOUNDARY FRAGMENT. The visible vendor half is
// mstring mcvt (char * i) { mstring tmp; tmp = i; return i; }
// - the continuation (mcvt(long i) and the remaining overloads)
// lands with libmpscpp.cpp::<misc-6>, roster 294, batch 7. The
// vendor returns i, NOT the tmp it just built (quirk above).

internal fun mcvt(i: CharArray): String {
    val tmp = cStringOf(i) // mstring tmp; tmp = i;
    return cStringOf(i) // return i; (tmp dead past the assign)
}
