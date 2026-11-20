package doc

// Unit 154 (B5 tranche 3): libmpscpp.cpp::<misc-5> (batch 5,
// complex stratum; units.csv row 154: loc 600, score 68.4,
// rank 254) - the scalar-comparison ladders tail, the builtin
// runtime helpers (thrownMessage/__finish/Stem/Token/
// Getenv/ZSeek/Ztell/Hash/lhash), the mdh GTK wrapper tier,
// Dump/Restore, the global-member Conversions/Count/Transpose/
// DocCorrelate twins, pt, stem x3, indirect-star, Perl, the
// GlobalSet/GlobalGet xecute bridges and pmw. All derived from
// the pristine vendor source this session (sha256 621deb...
// ffdbe).
//
// H-004 boundaries: the <=(string) scalar head and its opening
// else-arm are the chunk-edge fragment of the MISCELLANY-4 cut
// (the "else return false;" opening line is the TAIL of
// mstring::operator<=(string), whose owner is row 283, batch
// 10) - documented here, NOT rendered; the whole-file chunker
// overlap is ledgered in the journal front-run. word-operator
// precedent: the lt/gt ladder of SymOpsMisc; the const char* /
// char* / string twins of the == and != ladders collapse onto
// the String seat (ledger: 9 vendor overloads x2 fold to 7
// Kotlin overloads x2). lt/gt reuse the arm-wide atol/atof
// protocol seats (atollShared/strtod). global::DocCorrelate
// (char*/mstring twins) land here as delegates into the
// Stubs.globalDocCorrelate seat which stays with its batch-10
// owner (row 288, the string twin with the working body):
// flipping the seat to these twins would delegate in a circle
// (the twins call the string body), so the seat is NOT flipped
// this tranche - front-run note ledgered. Stubs.lhash flips
// to the real lhash body here; Stubs.globalCount flips to the
// real global::Count body; Stubs.globalRefTranspose flips to
// the real global::Transpose member. H-010 note: the seat
// retypes global::Count's long walk as Double; the render
// casts back at the return. New H-014 seats raised: Stubs.pm
// (owner interp-pm.cpp::pm, not a roster unit), Stubs.wstem
// (owner libmpscpp.cpp::wstem, not a roster unit). The GTK
// pass-through wrappers ride per-file stub carriers (the
// Out-of-scope GTK-library precedent of Stubs.gtkTreeStore
// Append); %p pointer formatting renders identity-hex
// (LibMisc1/MdhTreeLevelAdd precedent); the coded bytes
// (ce/cf/d2) are built programmatically per H-015; every
// vendor "\u0024z..." command lead rides the \u0024 escape
// per the arm's raw-dollar scan rule. sprintf conversions map
// via String.format(java.util.Locale.US,...) (H-009); cout
// maps to System.out (H-001); statics become per-file
// carrier objects (H-008); exit/_exit map to
// kotlin.system.exitProcess (LibMisc1 Hash precedent). The
// trailing "mstring tmp;" line of the chunk is the front-edge
// of mcvt, owned by misc-6 (batch 7 - not rendered here).
// Naming per H-017; the SKU of this file is exactly the
// chunker's cut line for line.

// vendor lhash/Hash 4-byte accumulator: chars widen to their
// LSB-ordered quad; the zero-init = {0} carrier keeps the
// upper bytes NUL exactly as the vendor's tail-quandrant
// islands off the string end.
private const val QUAD_STEP = 4

// defines.h:87 define CLOSE idem-partner of MumpsMainUnit150
// (GlobalClose expansion opcode).
private const val CLOSE = 4

// libmpscpp.h:79 define INPUT_LENGTH 10 idem-partner of the
// Getstr1 render (file-private, H-004 keep-first).
private const val INPUT_LENGTH = 10

// ---- chunk-edge fragment ledger: "else return false;" is the
// tail of mstring::operator<=(string) whose head lives in the
// misc-4 chunk (batch-10 row 283 owner). Not rendered.

// bool mstring::operator<=(global i) - the global twin of the
// ladder tail; string t1 = i>>t1 reads the global unkeyed.
fun Mstring.le(i: GlobalRef): Boolean {
    val t1 = Stubs.getGlobalString(i)
    return if (str.compareTo(t1) <= 0) true else false
}

// bool mstring::operator<=(mstring i)
fun Mstring.le(i: Mstring): Boolean {
    return if (str.compareTo(i.str) <= 0) true else false
}

// bool mstring::operator==(int i)
fun Mstring.eq(i: Int): Boolean {
    val j = atollShared(str)
    return if (j == i.toLong()) true else false
}

// bool mstring::operator==(long i)
fun Mstring.eq(i: Long): Boolean {
    val j = atollShared(str)
    return if (j == i) true else false
}

// bool mstring::operator==(double i)
fun Mstring.eq(i: Double): Boolean {
    val j = strtod(str)
    return if (j == i) true else false
}

// bool mstring::operator==(float i) - the double-promoted twin
fun Mstring.eq(i: Float): Boolean {
    val j = strtod(str)
    return if (j == i.toDouble()) true else false
}

// bool mstring::operator==(const char*)/(char*)/(string) -
// the three C twins fold onto the String seat (ledger).
fun Mstring.eq(i: String): Boolean {
    return if (str == i) true else false
}

// bool mstring::operator==(global i)
fun Mstring.eq(i: GlobalRef): Boolean {
    val t1 = Stubs.getGlobalString(i)
    return if (str == t1) true else false
}

// bool mstring::operator==(mstring i)
fun Mstring.eq(i: Mstring): Boolean {
    return if (str == i.str) true else false
}

// bool mstring::operator!=(int i)
fun Mstring.ne(i: Int): Boolean {
    val j = atollShared(str)
    return if (j != i.toLong()) true else false
}

// bool mstring::operator!=(long i)
fun Mstring.ne(i: Long): Boolean {
    val j = atollShared(str)
    return if (j != i) true else false
}

// bool mstring::operator!=(double i)
fun Mstring.ne(i: Double): Boolean {
    val j = strtod(str)
    return if (j != i) true else false
}

// bool mstring::operator!=(float i)
fun Mstring.ne(i: Float): Boolean {
    val j = strtod(str)
    return if (j != i.toDouble()) true else false
}

// bool mstring::operator!=(char*)/(const char*)/(string) -
// three C twins fold onto the String seat (ledger).
fun Mstring.ne(i: String): Boolean {
    return if (str != i) true else false
}

// bool mstring::operator!=(global i)
fun Mstring.ne(i: GlobalRef): Boolean {
    val t1 = Stubs.getGlobalString(i)
    return if (str != t1) true else false
}

// bool mstring::operator!=(mstring i)
fun Mstring.ne(i: Mstring): Boolean {
    return if (str != i.str) true else false
}

// -------------------------------------------------------
// BUILTIN FUNCTIONS

// void thrownMessage() - the uncaught-exception banner; the
// vendor "detetected" typo is kept verbatim.
fun thrownMessage() {
    System.out.print("Mumps has detetected an uncaught exception: \n")

    if (Globals.GlobalExceptionCode == INVALID_CONVERSION)
        System.out.print("Invalid conversion from global to numeric\n")

    if (Globals.GlobalExceptionCode == GLOBAL_NOT_FOUND)
        System.out.print("Global not found\n")

    if (Globals.GlobalExceptionCode == INPUT_LENGTH)
        System.out.print("Input Line Too Long\n")
}

// void __finish() - static int flg rides the per-file carrier
// (H-008); GlobalClose expands inline (libmpscpp.h:119); the
// _exit(0) immediate-terminate arm precedes the flg store and
// exitProcess carries both arms (H-001).
private object FinishStatic {
    var flg: Int = 0
}

fun __finish() {
    if (FinishStatic.flg != 0) kotlin.system.exitProcess(0)

    // GlobalClose;
    Stubs.Mglobal(CLOSE, CharArray(1), CharArray(1), Globals.svPtr)
    FinishStatic.flg = 1
    kotlin.system.exitProcess(0)
}

// int global::Int() - (*this) >> i conversion member; the
// globalGetInt seat (global:x member conversion family, unit
// 31) carries the >> int read.
fun GlobalRef.Int(): Int {
    return Stubs.globalGetInt(this)
}

// double global::Double()
fun GlobalRef.Double(): Double {
    return Stubs.globalGetDouble(this)
}

// mstring global::Mstring()
fun GlobalRef.Mstring(): Mstring {
    return Mstring(Stubs.getGlobalString(this))
}

// char * global::Char(char * i, int max) - the >> tmp carrier
// read then the strncpy the-max clamp (n max semantics kept;
// no NUL pad beyond max, the vendor strncpy shape).
fun GlobalRef.Char(i: CharArray, max: Int): CharArray {
    val tmp = CharArray(STR_MAX)
    val src = Stubs.getGlobalString(this)
    var n = 0
    while (n < max && n < src.length && n < i.size) {
        tmp[n] = src[n]
        n++
    }
    n = 0
    while (n < max && n < i.size) {
        i[n] = tmp[n]
        n++
    }
    return i
}

// mstring Stem(mstring &arg) - the wstem wrapper (the in-place
// write lands in the fresh carriers).
fun Stem(arg: Mstring): Mstring {
    val tmp = CharArray(STR_MAX)
    CstringOps.strcpyS(tmp, arg.c_str())
    Stubs.wstem(tmp, CstringOps.strlenC(tmp))
    return Mstring(CstringOps.cstring(tmp))
}

// mstring TokenInit(mstring arg) - the \u0024zwi tokenizer seed.
// The 0xce/0xcf coded bytes are built programmatically
// (H-015); the result reads the svPtr.bd carrier (zfcn side
// effect stateVector.h:42).
fun TokenInit(arg: Mstring): Mstring {
    val tmp = CharArray(STR_MAX)
    val tmp2 = CharArray(STR_MAX)
    CstringOps.strcpyS(tmp, " \u0024zwi")
    CstringOps.catAt(tmp, 0, Char(0x00ce))
    CstringOps.catAt(tmp, 0, arg.c_str())
    CstringOps.catAt(tmp, 0, Char(0x00cf))
    Stubs.zfcn(tmp, tmp2, Globals.svPtr)
    return Mstring(CstringOps.cstring(Globals.svPtr.bd))
}

// mstring Token() - the \u0024zwp next-token pull.
fun Token(): Mstring {
    val tmp = CharArray(STR_MAX)
    val tmp2 = CharArray(STR_MAX)
    CstringOps.strcpyS(tmp, " \u0024zwp")
    Stubs.zfcn(tmp, tmp2, Globals.svPtr)
    return Mstring(CstringOps.cstring(tmp2))
}

// -------------------------------------------------------
// mdh GTK wrapper tier

// int mdh_dialog_new_with_buttons(GtkWindow *, char *) - the
// zfcn dialing arm writes from index 1 (the mdhTreeLevelAdd
// offset-1 domain); tmp[0] stays NUL-uninitialized as the
// vendor carrier quirk (quantized, noted); %p renders
// identity-hex (documented divergence, only stability
// matters in the emitted command).
fun mdhDialogNewWithButtons(win: Any?, text: String): Int {
    val tmp = CharArray(4096)
    val tmp1 = CharArray(256)
    CstringOps.copyAtS(tmp, 1, "\u0024z~mdh~dialog~new~with~buttons")
    CstringOps.catAt(tmp, 1, Char(0x00ce))

    CstringOps.strcpyS(tmp1,
        java.lang.System.identityHashCode(win).toString(16))
    CstringOps.catAt(tmp, 1, CstringOps.cstring(tmp1))
    CstringOps.catAt(tmp, 1, Char(0x00d0))

    CstringOps.catAt(tmp, 1, text)
    CstringOps.catAt(tmp, 1, Char(0x00cf))

    Stubs.zfcn(tmp, tmp1, Globals.svPtr)

    if (tmp1[0] == '0') return 0

    return 1
}

// Per-file GTK pass-through stub carriers (H-014): every
// direct gtk_* call of the mdh wrappers names its GTK-library
// owner; widget pointers ride the String identity-hex seat
// (the gtkTreeStoreAppend precedent).
private object GtkPassThrough {
    fun gtkToggleButtonGetActive(b: String?): Int =
        throw NotImplementedError("gtk_toggle_button_get_active: out-of-scope owner (GTK library)")
    fun gtkSwitchGetActive(b: String?): Int =
        throw NotImplementedError("gtk_switch_get_active: out-of-scope owner (GTK library)")
    fun gtkToggleButtonSetActive(b: String?, x: Boolean): Unit =
        throw NotImplementedError("gtk_toggle_button_set_active: out-of-scope owner (GTK library)")
    fun gtkEntryGetText(e: String?): String =
        throw NotImplementedError("gtk_entry_get_text: out-of-scope owner (GTK library)")
    fun gtkRangeGetAdjustmentValue(e: String?): Double =
        throw NotImplementedError("gtk_range_get_adjustment/gtk_adjustment_get_value: out-of-scope owner (GTK library)")
    fun gtkCalendarGetDate(e: String?, yr: IntArray, mn: IntArray,
            dy: IntArray): Unit =
        throw NotImplementedError("gtk_calendar_get_date: out-of-scope owner (GTK library)")
    fun gtkEntrySetText(e: String?, txt: String): Unit =
        throw NotImplementedError("gtk_entry_set_text: out-of-scope owner (GTK library)")
    fun gtkTextBufferSetText(t: String?, txt: String): Unit =
        throw NotImplementedError("gtk_text_buffer_set_text: out-of-scope owner (GTK library)")
    fun gtkFontChooserGetFont(b: String?): String =
        throw NotImplementedError("gtk_font_chooser_get_font: out-of-scope owner (GTK library)")
    fun gtkColorChooserGetRgba(c: String?): GdkRgba =
        throw NotImplementedError("gtk_color_chooser_get_rgba: out-of-scope owner (GTK library)")
    fun gtkLabelSetText(l: String?, txt: String): Unit =
        throw NotImplementedError("gtk_label_set_text: out-of-scope owner (GTK library)")
    fun gtkWidgetHide(w: String?): Unit =
        throw NotImplementedError("gtk_widget_hide: out-of-scope owner (GTK library)")
    fun gtkWidgetShow(w: String?): Unit =
        throw NotImplementedError("gtk_widget_show: out-of-scope owner (GTK library)")
    fun gtkTreeSelectionGetSelected(t: String?): Boolean =
        throw NotImplementedError("gtk_tree_selection_get_selected: out-of-scope owner (GTK library)")
    fun gtkTreeModelGet(col: Int): String =
        throw NotImplementedError("gtk_tree_model_get: out-of-scope owner (GTK library)")
    fun gtkTreeStoreClear(t: String?): Unit =
        throw NotImplementedError("gtk_tree_store_clear: out-of-scope owner (GTK library)")
    fun gtkSpinButtonGetValue(s: String?): Double =
        throw NotImplementedError("gtk_spin_button_get_value: out-of-scope owner (GTK library)")
    fun gtkSpinButtonSetValue(s: String?, v: Double): Unit =
        throw NotImplementedError("gtk_spin_button_set_value: out-of-scope owner (GTK library)")
}

// GdkRGBA parameter quad (gdk/gdkcolor.h; per-file render -
// the only fields the wrapper prints).
class GdkRgba {
    var red: Double = 0.0
    var green: Double = 0.0
    var blue: Double = 0.0
    var alpha: Double = 0.0
}

// int mdh_toggle_button_get_active(GtkToggleButton *b)
fun mdhToggleButtonGetActive(b: String?): Int {
    return GtkPassThrough.gtkToggleButtonGetActive(b)
}

// int mdh_switch_get_active(GtkSwitch *b)
fun mdhSwitchGetActive(b: String?): Int {
    return GtkPassThrough.gtkSwitchGetActive(b)
}

// void mdh_toggle_button_set_active(GtkToggleButton *, int v)
fun mdhToggleButtonSetActive(b: String?, v: Int) {
    val x = (v != 0) // if (v==0) x=FALSE; else x=TRUE;
    GtkPassThrough.gtkToggleButtonSetActive(b, x)
}

// char * mdh_entry_get_text(GtkEntry *e, char * txt)
fun mdhEntryGetText(e: String?, txt: CharArray): CharArray {
    CstringOps.strcpyS(txt, GtkPassThrough.gtkEntryGetText(e))
    return txt
}

// double mdh_range_get_adjustment(GtkRange *e) - the
// adjustment fold (gtk_range_get_adjustment +
// gtk_adjustment_get_value chain; carrier stub above).
fun mdhRangeGetAdjustment(e: String?): Double {
    return GtkPassThrough.gtkRangeGetAdjustmentValue(e)
}

// calendar date static scratch (H-008: the vendor static char
// tmp[256] rides the per-file carrier).
private object MdCalendarStatic {
    val tmp = CharArray(256)
}

// char * mdh_calendar_get_date(GtkCalendar *e) - the
// sprintf "%d %d %d" trail (H-009 Locale.US).
fun mdhCalendarGetDate(e: String?): CharArray {
    val yr = IntArray(1)
    val mn = IntArray(1)
    val dy = IntArray(1)
    GtkPassThrough.gtkCalendarGetDate(e, yr, mn, dy)
    val dated = String.format(java.util.Locale.US, "%d %d %d",
        yr[0], mn[0], dy[0])
    CstringOps.strcpyS(MdCalendarStatic.tmp, dated)
    return MdCalendarStatic.tmp
}

// void mdh_entry_set_text(GtkEntry *e, char * txt)
fun mdhEntrySetText(e: String?, txt: String) {
    GtkPassThrough.gtkEntrySetText(e, txt)
}

// void mdh_text_buffer_set_text(GtkTextBuffer *t, char * txt)
// - the vendor -1 length limb of gtk_text_buffer_set_text maps
// to the String pass-through stub.
fun mdhTextBufferSetText(t: String?, txt: String) {
    GtkPassThrough.gtkTextBufferSetText(t, txt)
}

// font chooser static scratch (H-008).
private object MdFontStatic {
    val tmp = CharArray(256)
}

// char * mdh_font_chooser_get_font(GtkFontChooser *b)
fun mdhFontChooserGetFont(b: String?): CharArray {
    CstringOps.strcpyS(MdFontStatic.tmp,
        String.format(java.util.Locale.US, "%s",
            GtkPassThrough.gtkFontChooserGetFont(b)))
    return MdFontStatic.tmp
}

// color chooser static scratch (H-008).
private object MdColorStatic {
    val tmp = CharArray(256)
}

// char * mdh_color_chooser_get_rgba(GtkColorChooser *c) - the
// "%f %f %f %f" quad over the GdkRgba carrier.
fun mdhColorChooserGetRgba(c: String?): CharArray {
    val r = GtkPassThrough.gtkColorChooserGetRgba(c)
    val rgba = String.format(java.util.Locale.US, "%f %f %f %f",
        r.red, r.green, r.blue, r.alpha)
    CstringOps.strcpyS(MdColorStatic.tmp, rgba)
    return MdColorStatic.tmp
}

// void mdh_label_set_text(GtkLabel *l, char * txt)
fun mdhLabelSetText(l: String?, txt: String) {
    GtkPassThrough.gtkLabelSetText(l, txt)
}

// void mdh_widget_hide(GtkWidget *w)
fun mdhWidgetHide(w: String?) {
    GtkPassThrough.gtkWidgetHide(w)
}

// void mdh_widget_show(GtkWidget *w)
fun mdhWidgetShow(w: String?) {
    GtkPassThrough.gtkWidgetShow(w)
}

// char * mdh_tree_selection_get_selected(GtkTreeSelection *,
// int col, char *txt) - the model/iter locals are dead beyond
// the arm bounds below (the m/iter sit in the stub seat); the
// pre-clear strcpy(txt,"") floor and the FALSE empty return
// are kept.
fun mdhTreeSelectionGetSelected(t: String?, col: Int,
        txt: CharArray): CharArray {
    CstringOps.strcpyS(txt, "")

    if (!GtkPassThrough.gtkTreeSelectionGetSelected(t))
        return txt

    val value = GtkPassThrough.gtkTreeModelGet(col)

    CstringOps.strcpyS(txt, value)

    return txt
}

// void mdh_tree_store_clear(GtkTreeStore *t)
fun mdhTreeStoreClear(t: String?) {
    GtkPassThrough.gtkTreeStoreClear(t)
}

// double mdh_spin_button_get_value(GtkSpinButton *s)
fun mdhSpinButtonGetValue(s: String?): Double {
    return GtkPassThrough.gtkSpinButtonGetValue(s)
}

// void mdh_spin_button_set_value(GtkSpinButton *s, double v)
fun mdhSpinButtonSetValue(s: String?, v: Double) {
    GtkPassThrough.gtkSpinButtonSetValue(s, v)
}

// -------------------------------------------------------
// Dump / Restore tiers - the mstring/string/char* overloads
// fold onto the Mstring and String seats (collapse ledger: 3
// vendor overloads each render 2 Kotlin overloads).

// void Dump(mstring &arg)
fun Dump(arg: Mstring) {
    val tmp = CharArray(256)
    val tmp2 = CharArray(256)
    CstringOps.strcpyS(tmp, "\u0024zcd")
    CstringOps.catAt(tmp, 0, Char(0x00ce))
    CstringOps.catAt(tmp, 0, arg.c_str())
    CstringOps.catAt(tmp, 0, Char(0x00cf))
    Stubs.zfcn(tmp, tmp2, Globals.svPtr)
}

// void Dump(string &arg) / void Dump(char * arg) - folds
fun Dump(arg: String) {
    val tmp = CharArray(256)
    val tmp2 = CharArray(256)
    CstringOps.strcpyS(tmp, "\u0024zcd")
    CstringOps.catAt(tmp, 0, Char(0x00ce))
    CstringOps.catAt(tmp, 0, arg)
    CstringOps.catAt(tmp, 0, Char(0x00cf))
    Stubs.zfcn(tmp, tmp2, Globals.svPtr)
}

// void Restore(mstring &arg)
fun Restore(arg: Mstring) {
    val tmp = CharArray(256)
    val tmp2 = CharArray(256)
    CstringOps.strcpyS(tmp, "\u0024zcl")
    CstringOps.catAt(tmp, 0, Char(0x00ce))
    CstringOps.catAt(tmp, 0, arg.c_str())
    CstringOps.catAt(tmp, 0, Char(0x00cf))
    Stubs.zfcn(tmp, tmp2, Globals.svPtr)
}

// void Restore(string &arg) / void Restore(char * arg) - folds
fun Restore(arg: String) {
    val tmp = CharArray(256)
    val tmp2 = CharArray(256)
    CstringOps.strcpyS(tmp, "\u0024zcl")
    CstringOps.catAt(tmp, 0, Char(0x00ce))
    CstringOps.catAt(tmp, 0, arg)
    CstringOps.catAt(tmp, 0, Char(0x00cf))
    Stubs.zfcn(tmp, tmp2, Globals.svPtr)
}

// void Getenv(unsigned char *out, unsigned char *in) - the
// System.getenv mapping (H-001); the in keyword is a Kotlin keyword,
// rendered in_ (H-017).
fun Getenv(out: CharArray, in_: CharArray) {
    val p1 = System.getenv(CstringOps.cstring(in_))

    if (p1 == null) {
        CstringOps.strcpyS(out, "")
        return
    }

    CstringOps.strcpyS(out, p1)
}

// -------------------------------------------------------
// ZSeek/Ztell - the FILE* seat rides the RandomAccessFile
// carrier (H-001 FILE* divergence ledger; fsetpos/fgetpos map
// to seek/filePointer - the fpos_t void* payload of the
// vendor is the 64-bit offset domain in both).

// bool ZSeek(FILE *u, mstring &s)
fun ZSeek(u: java.io.RandomAccessFile, s: Mstring): Boolean {
    val offset = s.c_str().toLong()
    u.seek(offset)
    return true // !fsetpos
}

// bool Ztell(FILE *u, mstring &s)
fun ZTell(u: java.io.RandomAccessFile, s: Mstring): Boolean {
    // i = fgetpos(u,&offset)
    val offset = u.filePointer
    s.str = String.format(java.util.Locale.US, "%d", offset)
    return true // !i
}

// bool ZSeek(FILE *u, global &s)
fun ZSeek(u: java.io.RandomAccessFile, s: GlobalRef): Boolean {
    val tmp1 = CharArray(STR_MAX)
    CstringOps.strcpyS(tmp1, Stubs.getGlobalString(s))
    val offset = CstringOps.cstring(tmp1).toLong()
    u.seek(offset)
    return true // !fsetpos
}

// bool Ztell(FILE *u, global &s)
fun ZTell(u: java.io.RandomAccessFile, s: GlobalRef): Boolean {
    val offset = u.filePointer
    val tmp = String.format(java.util.Locale.US, "%d", offset)
    Stubs.globalSetKeyed(s, tmp)
    return true // !i
}

// -------------------------------------------------------
// Perl x2 - the perlcpp call is commented out in the vendor;
// both bodies return svPtr->tpx as staged (kept verbatim).

// int Perl(mstring &a, mstring &b)
fun Perl(a: Mstring, b: Mstring): Int {
    //      svPtr->tpx=perlcpp((char *) "",(char *) a.c_str(), (char *) b.c_str(),svPtr);
    return Globals.svPtr.tpx.toInt()
}

// int Perl(mstring &a, char *b)
fun Perl(a: Mstring, b: String): Int {
    //      svPtr->tpx=perlcpp((char *) "",(char *) a.c_str(), (char *) b,svPtr);
    return Globals.svPtr.tpx.toInt()
}

// -------------------------------------------------------
// global::DocCorrelate member twins - see H-004 note in the
// header ledger: these delegate into the Stubs.globalDoc
// Correlate seat whose owner (the string twin with the
// working body) is batch-10 roster row 288.

// void global::DocCorrelate(global B, char * fcn, double
// threshold)
fun GlobalRef.DocCorrelate(B: GlobalRef, fcn: String,
        threshold: Double) {
    val s = fcn
    Stubs.globalDocCorrelate(this, B, s, threshold)
}

// void global::DocCorrelate(global B, mstring fcn, double
// threshold) - s=fcn.s_str() reads the mstring bridge
fun GlobalRef.DocCorrelate(B: GlobalRef, fcn: Mstring,
        threshold: Double) {
    val s = fcn.c_str() // s=fcn.s_str(); the u149 ledger folds
    // the std::string extraction witness onto the c_str seat
    Stubs.globalDocCorrelate(this, B, s, threshold)
}

// void pt(char *t, char *x) - the two-space pack banner; the
// vendor int i is UNINITIALIZED before the walk (UB):
// quantized to 0 (C-protocol precedent, ledger; the read is
// in-bounds here for every NUL-carried input either way).
fun pt(t: CharArray, x: CharArray) {
    var i = 0
    System.out.print(CstringOps.cstring(t) + "  ")

    while (x[i] != NUL) {
        System.out.print(" " + x[i] + " ")
        i++
    }

    System.out.print("\n")
}

// long global::Count() - the real XNEXT sibling walk (the
// Min-family idiom); H-010: the Double-seat twin casts the
// long walk at the return; Stubs.globalCount flips here.
fun GlobalRef.Count(): Double {
    val ttt = CharArray(STR_MAX)
    val tmp = CharArray(STR_MAX)
    var j = 0L
    CstringOps.strcpyS(tmp, ref)
    var i = CstringOps.strlenC(tmp)

    // if (tmp[i-1]<32 && tmp[i-2]<32) tmp[--i]=0; - the
    // trailing-separator trim kept in place (ref-suffix walk
    // bound below).
    if (tmp[i - 1] < Char(32) && tmp[i - 2] < Char(32))
        tmp[--i] = NUL

    while (true) {
        if (Stubs.Mglobal(XNEXT, tmp, ttt, Globals.svPtr) == 0) break

        if (CstringOps.strncmpS(ref, tmp, i) != 0) break

        j++
    }

    return j.toDouble()
}

// void global::Transpose(global B) - the member; the vendor
// B().Kill() empty-invoke arm writes through the seat (the
// globalKill owner); the Order/read/assign seats carry the
// keyed walk (unit-69 family); Stubs.globalRefTranspose flips
// here.
fun GlobalRef.Transpose(B: GlobalRef) {
    var aa = ""
    var bb = ""

    Stubs.globalKill(B) // B().Kill();

    while (true) {
        aa = Stubs.globalOrder(this, aa)

        if (aa == "") break

        bb = ""

        while (true) {
            bb = Stubs.globalOrder2(this, aa, bb)

            if (bb == "") break

            Stubs.globalSetKeyed(B,
                    Stubs.getGlobalString(this, aa, bb), bb, aa)
        }
    }
}

// void Transpose(char *a, char *b) - the char* overload; the
// CharArray carriers keep it distinct from the unit-100 String
// overload (H-015 same-lexeme overload family rule).
fun Transpose(a: CharArray, b: CharArray) {
    val aRef = GlobalRef(CstringOps.cstring(a))
    val bRef = GlobalRef(CstringOps.cstring(b))
    var aa = ""
    var bb = ""

    while (true) {
        aa = Stubs.globalOrder(aRef, aa)

        if (aa == "") break

        bb = ""

        while (true) {
            bb = Stubs.globalOrder2(aRef, aa, bb)

            if (bb == "") break

            Stubs.globalSetKeyed(bRef,
                    Stubs.getGlobalString(aRef, aa, bb), bb, aa)
        }
    }
}

// void Transpose(mstring a, mstring b) - the mstring overload
fun Transpose(a: Mstring, b: Mstring) {
    val aRef = GlobalRef(a.c_str())
    val bRef = GlobalRef(b.c_str())
    var aa = ""
    var bb = ""

    while (true) {
        aa = Stubs.globalOrder(aRef, aa)

        if (aa == "") break

        bb = ""

        while (true) {
            bb = Stubs.globalOrder2(aRef, aa, bb)

            if (bb == "") break

            Stubs.globalSetKeyed(bRef,
                    Stubs.getGlobalString(aRef, aa, bb), bb, aa)
        }
    }
}

// -------------------------------------------------------
// stem triples - the static char tmp[STR_MAX] scratch rides
// the per-file carrier (H-008).

private object StemStatic {
    val tmp = CharArray(STR_MAX)
}

// string stem(string &word) - the string& in-place contract
// lands on the returned String (documented divergence: C++
// mutates in place; callers rebind) - ledger.
fun stem(word: String): String {
    CstringOps.strcpyS(StemStatic.tmp, word)
    Stubs.wstem(StemStatic.tmp, word.length)
    return CstringOps.cstring(StemStatic.tmp)
}

// mstring stem(mstring &word) - the mstring seat mutates its
// carrier in place exactly as the vendor.
fun stem(word: Mstring): Mstring {
    CstringOps.strcpyS(StemStatic.tmp, word.c_str())
    Stubs.wstem(StemStatic.tmp, CstringOps.strlenC(StemStatic.tmp))
    word.str = CstringOps.cstring(StemStatic.tmp)
    return word
}

// char* stem(char* word) - the in-place char carrier walk
fun stem(word: CharArray): CharArray {
    Stubs.wstem(word, CstringOps.strlenC(word))
    return word
}

// -------------------------------------------------------
// Hash / lhash - the 4-byte little-word sum; the = {0} tail
// quads read NUL (zero) within the carrier exactly as the C
// zero-init; the ULong domain keeps the wraparound (H-012 -
// the sign bit never leaves the unsigned domain, ledgered at
// the lhash return cast).

// char * Hash(const char *str) - static result: H-008 carrier
private object HashStatic {
    val result = CharArray(32)
}

fun Hash(str: String): CharArray {
    val in_ = CharArray(STR_MAX) // = {0}
    var x = 0UL
    CstringOps.strcpyS(in_, str)

    var i = 0
    while (in_[i] != NUL) {
        var a = in_[i].code.toULong()
        a = a or (in_[i + 1].code.toULong() shl 8)
        a = a or (in_[i + 2].code.toULong() shl 16)
        a = a or (in_[i + 3].code.toULong() shl 24)
        x = x + a
        i += QUAD_STEP
    }

    // sprintf(result,"%lu",x) - unsigned decimal (H-009)
    CstringOps.strcpyS(HashStatic.result, x.toString())
    return HashStatic.result
}

// unsigned long lhash(const char *str) - the real body flips
// the Stubs.lhash seat (the Long cast keeps the H-012 seat
// domain).
fun lhash(str: String): Long {
    val in_ = CharArray(STR_MAX) // = {0}
    var x = 0UL
    CstringOps.strcpyS(in_, str)

    var i = 0
    while (in_[i] != NUL) {
        var a = in_[i].code.toULong()
        a = a or (in_[i + 1].code.toULong() shl 8)
        a = a or (in_[i + 2].code.toULong() shl 16)
        a = a or (in_[i + 3].code.toULong() shl 24)
        x = x + a
        i += QUAD_STEP
    }

    return x.toLong()
}

// indirect operator*(string a) - the star-construct of the
// Indirect carrier; the vendor the static indirect b rides the
// per-file carrier (H-008); the word-operator rename is the
// H-014 arm adaptation (indirectStar - the operator cannot be
// a Kotlin star).
private object IndirectStatic {
    val b = Indirect("")
}

fun indirectStar(a: String): Indirect {
    IndirectStatic.b.reference = a
    return IndirectStatic.b
}

// -------------------------------------------------------
// GlobalSet/GlobalGet - the xecute bridge family; the vendor
// stray backslash line-splice artifact inside each GlobalSet
// body is noted once here (H-013 fossil, no code effect). The
// seven/five C overloads fold onto the Mstring/String seat
// pairs (collapse ledger).

// the "s " + x + "=\"" + y + "\"" build and the tpx bridge
private fun globalSetBridge(x: String, y: String): Int {
    val tmp = "s " + x + "=\"" + y + "\""

    if (Stubs.xecute(tmp.toCharArray()) == 0) Globals.svPtr.tpx = 1L
    else Globals.svPtr.tpx = 0L

    return Globals.svPtr.tpx.toInt()
}

// int GlobalSet(mstring x, mstring y)
fun GlobalSet(x: Mstring, y: Mstring): Int {
    return globalSetBridge(x.c_str(), y.c_str())
}

// int GlobalSet(char *x, mstring y) / int GlobalSet(string x,
// mstring y) - folds
fun GlobalSet(x: String, y: Mstring): Int {
    return globalSetBridge(x, y.c_str())
}

// int GlobalSet(mstring x, char *y)
fun GlobalSet(x: Mstring, y: String): Int {
    return globalSetBridge(x.c_str(), y)
}

// int GlobalSet(string x, string y) / GlobalSet(char *x,
// string y) / GlobalSet(string x, char *y) / GlobalSet(char *x,
// char *y) - folds
fun GlobalSet(x: String, y: String): Int {
    return globalSetBridge(x, y)
}

// char * GlobalGet(char *a) - the static result rides the
// per-file carrier (H-008); the vendor strcpy(r,"") failure
// store is overwritten unconditionally by the t read below
// (the quirk is unreachable, kept in shape); the string-twin's
// conditional shape folds here (ledger).
private object GlobalGetStatic {
    val r = CharArray(STR_MAX)
}

fun GlobalGet(a: String): CharArray {
    val tmp = "set %=" + a

    if (Stubs.xecute(tmp.toCharArray()) == 0) Globals.svPtr.tpx = 1L
    else {
        Globals.svPtr.tpx = 0L
        CstringOps.strcpyS(GlobalGetStatic.r, "")
    }

    val t = SymGet("%")
    CstringOps.strcpyS(GlobalGetStatic.r, t.c_str())
    return GlobalGetStatic.r
}

// mstring GlobalGet(mstring a)
fun GlobalGet(a: Mstring): Mstring {
    var rr = Mstring("")
    val tmp = "set %=" + a.c_str()

    if (Stubs.xecute(tmp.toCharArray()) == 0) {
        Globals.svPtr.tpx = 1L
        rr = SymGet("%")
    } else {
        Globals.svPtr.tpx = 0L
        rr = Mstring("")
    }

    return rr
}

// -------------------------------------------------------
// pmw - the pm driver wrappers; the five C overloads fold
// onto the String pair and the Mstring pair (collapse
// ledger); Stubs.pm is the NEW seat (interp-pm.cpp owner).

// int pmw(char *a, char *b, struct MSV * svPtr) and the
// string/char* lattice folds
fun pmw(a: String, b: String, svPtr: Msv): Int {
    return Stubs.pm(a.toCharArray(), b.toCharArray(), svPtr)
}

// int pmw(mstring a, mstring b, struct MSV *svPtr)
fun pmw(a: Mstring, b: Mstring, svPtr: Msv): Int {
    return Stubs.pm(a.c_str().toCharArray(), b.c_str().toCharArray(),
        svPtr)
}

// chunk-end ledger: "mstring tmp;" opens mstring mcvt(char *i),
// the front edge of the misc-6 chunk (batch-7 owner row) -
// not rendered here.
