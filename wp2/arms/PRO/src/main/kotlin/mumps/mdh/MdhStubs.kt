package mumps.mdh

import mumps.interp.Msv

/**
 * Opaque PRO-arm stand-in for the GTK type GtkTreeStore (GUI glue;
 * the mdh functions only forward an opaque handle to the $z dispatcher).
 */
class GtkTreeStore : GtkHandle

/**
 * PRO-arm mdh-side owner object for libmpscpp.cpp's file-scope globals
 * (h004 globals_ext). svPtr is the shared interpreter state pointer.
 */
object MdhState {
    lateinit var svPtr: Msv

    /** Owner: libmpscpp.cpp file-scope residue (B1 unit 31, misc-1): short ierr1. */
    var ierr1: Short = 0

    /**
     * Owner: libmpscpp.cpp file-scope residue (B1 unit 31, misc-1):
     * int GlobalExceptionCode = UNSPECIFIED (libmpscpp.h:69: 0).
     */
    var globalExceptionCode: Int = 0
}

/**
 * PRO-arm mdh-side out-of-scope stubs (h014 out_of_scope_call).
 * Each stub names its owner; delegate when the owner lands.
 */

/** Owner: interp-bifs.cpp::zfcn - RESOLVED by ZfcnBifs.kt (unit
 * 157; TAG CORRECTION, u141/u148/u169/u172 family: the "not a
 * corpus unit" tag was a mis-tag, roster row 158 IS a batch-6
 * corpus unit, score 1173.4, rank 305); stub removed, the
 * LibmpscppMisc5.kt:220/235/267/407 and MdhTreeLevelAdd.kt:117
 * call sites bind unchanged on the 3-arg signature. */

/** Owner: builtin.h:40 Replace (char * Replace(MSV*, char * ep,
 *  char *arg1, char *arg2, char *arg3); not a corpus unit) - seat
 *  raised by u157 (the interp-bifs.cpp::zfcn zreplace arm, vendor
 *  1029-1034). */
fun Replace(svPtr: Msv, ep: CharArray?, arg1: CharArray, arg2: CharArray,
            arg3: CharArray): CharArray {
    TODO("h014 stub: owner builtin.h:40 Replace (not a corpus unit)")
}
/**
 * Owner: interp-sym.cpp / sym.c symbol-table primitive
 * sym_(fn,key,bd,svPtr) (not a corpus unit).
 * Return type promoted from Unit to CharArray? (B1 unit 25,
 * LocalGet tests sym_(11,...)==NULL; owner semantics: NULL when the
 * variable is absent, bd pointer otherwise).
 */
fun sym_(fn: Int, key: CharArray, bd: CharArray, svPtr: Msv): CharArray? {
    TODO("h014 stub: owner sym_ symbol-table machinery (not a corpus unit)")
}

/** Owner: interp-strmanip.cpp::strcatx - RESOLVED by
 *  InterpStrmanipMisc.kt (unit 136; tag corrected: libmpscpp.cpp
 *  only calls it at 6978+, the single C definition is strmanip);
 *  stub removed, the InterpSysfuncMisc call sites rebind via the
 *  mumps.interp import. */


/**
 * makeGlobal (libmpscpp.cpp::global::MakeGlobal, units.csv row
 * 203) was stubbed here from B1 unit 31 until B7 unit 203
 * landed; the owner-level definition now lives in MakeGlobal.kt
 * as a Global extension (the member-method adaptation), and
 * the stub has been removed.
 */



/**
 * Owner: libmpscpp.cpp global implicit-conversion operators
 * (operator int/long/... via globalClass.h; not in the B1 corpus).
 * The C++ per-type conversions collapse to one Double-valued stub
 * (B1 unit 31, see journal).
 */
fun globalNumericValue(j: Global): Double {
    TODO("h014 stub: owner libmpscpp.cpp global implicit conversion operators (not a corpus unit)")
}

/** Owner: libmpscpp.cpp/globalClass.h global arithmetic - global + int (not in the B1 corpus; B1 unit 31 unary operator+). */
fun globalPlusInt(g: Global, i: Int): Mstring {
    TODO("h014 stub: owner globalClass.h global arithmetic operators (not a corpus unit)")
}

/** Owner: libmpscpp.cpp/globalClass.h global arithmetic - int - global (not in the B1 corpus; B1 unit 31 unary operator-). */
fun intMinusGlobal(i: Int, g: Global): Mstring {
    TODO("h014 stub: owner globalClass.h global arithmetic operators (not a corpus unit)")
}

private const val MUMPS_GLOBAL_EXCEPTION_MESSAGE = "Global reference not found"

/**
 * Carrier for the vendor header class MumpsGlobalException
 * (libmpscpp.h:332; not a corpus unit). Thrown by the global members
 * when Mglobal cannot find the reference (B1 unit 31 added the
 * globalExceptionCode carrier; B2 units 32+ throw this).
 */
class MumpsGlobalException : RuntimeException(MUMPS_GLOBAL_EXCEPTION_MESSAGE) {
    /** Vendor: char *what(). */
    fun what(): String = MUMPS_GLOBAL_EXCEPTION_MESSAGE
}

/** Owner: interp-arith.cpp::Mltoa (batch-10 unit). */
fun Mltoa(n: Long, dst: CharArray) {
    TODO("h014 stub: owner interp-arith.cpp::Mltoa (batch-10 unit)")
}

/** Owner: interp-sym.cpp::_extract - RESOLVED by Extract.kt
 *  (unit 172, vendor 1235-1277; roster row 173, batch 6 - the
 *  former "batch-10 unit" tag was a mis-tag, u141/u148/u169
 *  tag-correction family); stub removed (the GlobalExtract.kt
 *  call site rebinds to mumps.interp._extract, u136 strcatx
 *  import precedent). */

/** Owner: include/mumpsc/keyfix.h inline keyfix (not a corpus unit). */
fun keyfix(key: CharArray) {
    TODO("h014 stub: owner keyfix.h inline keyfix (not a corpus unit)")
}

/** Owner: include/mumpsc/inline.h:38 strmove (not a corpus unit). */
fun strmove(a: CharArray, b: CharArray) {
    TODO("h014 stub: owner inline.h:38 strmove (not a corpus unit)")
}

/**
 * Owner: interp-piece.cpp:97 _piece (not a corpus unit; the
 * interp-piece.cpp::<misc> batch-4 unit may claim its neighborhood).
 * Signature: out, in, key, start, end, setpiece, lhs, svPtr.
 */
fun _piece(
    out: CharArray, in_: CharArray, key: CharArray, start: CharArray,
    end: CharArray, setpiece: Int, lhs: CharArray, svPtr: Msv,
) {
    TODO("h014 stub: owner interp-piece.cpp:97 _piece (not a corpus unit)")
}

/** Owner: libmpscpp.cpp:7073 xecute (not a corpus unit). */
fun xecute(exp: String): Int {
    TODO("h014 stub: owner libmpscpp.cpp:7073 xecute (not a corpus unit)")
}

// --- added by B4 unit 97 (libmpscpp.cpp::<misc-9>) ---
//
// The misc-9 svPtr-intrinsic wrappers bind these callees. The
// SymGet(String) stub previously raised here (owner libmpscpp.cpp:
// 9858) is REMOVED - unit 97 owns SymGet now (Scan unit 81
// precedent). Wrapper/callee name pairs (Cosine, Sim1, ...) are
// distinct overloads on parameter shape (h015 scan clean).

/**
 * Owner: libmpscpp.cpp class global operator>>(char*) machinery
 * (globalClass.h:178; not a corpus unit). The mstring <=/==/!=
 * global overloads and SymGet/SymPut(global) extract through it.
 */
fun Global.readInto(dst: CharArray) {
    TODO("h014 stub: owner libmpscpp.cpp class global operator>> machinery (not a corpus unit)")
}

/**
 * Sim1 (libmpscpp.cpp::Sim1, units.csv row 199) was stubbed
 * here until B7 unit 199 landed; the owner-level class-global
 * definition now lives in Sim1.kt (distinct overload from the
 * MSV wrapper in LibmpscppMisc9.kt, which binds unchanged) and
 * the stub has been removed.
 */

/** Owner: libmpscpp.cpp::Jaccard - RESOLVED by Jaccard.kt (roster
 * row 256, batch 9, vendor 1376-1459); stub removed (the
 * ServiceInterface JACCARD-arm call site binds unchanged). */

/**
 * libmpscpp.cpp::Dice was stubbed here until B6 unit 165
 * landed; the owner-level definition now lives in Dice.kt and
 * the stub has been removed (the MSV wrapper's call binds
 * unchanged).
 */

// (B4 unit 105) The Global.Centroid stub raised by B4 unit 97
// (LibmpscppMisc9 wrapper) is RESOLVED: B4 unit 105 translated
// libmpscpp.cpp::global::Centroid - see Centroid.kt (the stub was
// removed, cleanLocks precedent, unit 56); the u97 wrapper's call
// site now binds the real owner.


/** Owner: libmpscpp.cpp::StopInit - RESOLVED by StopInit.kt
 *  (unit 174, vendor 7729-7761; roster row 175, batch 6 - the
 *  string-owning overload with the char-star/mstring forwarders
 *  collapsed; the h008 stop-set carrier lives in the unit file);
 *  stub removed (the LibmpscppMisc9.kt:529 MSV wrapper call site
 *  binds unchanged). */

/** Owner: libmpscpp.cpp::StopLookup (char* overload at 7777; not a corpus unit). */
fun StopLookup(str: String): Boolean {
    TODO("h014 stub: owner libmpscpp.cpp::StopLookup (not a corpus unit)")
}

/** Owner: libmpscpp.cpp::SYN (7788; not a corpus unit).
 *  Carrier note: the syn table and synx now live in SynInit.kt
 *  (unit 126, h008 carrier) - when this stub resolves it reads
 *  them; bsearch/cmpr at vendor 7790-7791. */
fun SYN(word: String): String {
    TODO("h014 stub: owner libmpscpp.cpp::SYN (not a corpus unit)")
}

/**
 * libmpscpp.cpp::Shred was stubbed here until B6 unit 164
 * landed; the owner-level definition now lives in Shred.kt and
 * the stub has been removed (the MSV wrapper's call binds
 * unchanged).
 */

/** Owner: libmpscpp.cpp::ShredQuery (7970; not a corpus unit). */
fun ShredQuery(str: String, size: Int): String {
    TODO("h014 stub: owner libmpscpp.cpp::ShredQuery (not a corpus unit)")
}

/**
 * Owner: libmpscpp.cpp::bmg_fullsearch (not a corpus unit; the
 * wrapper's (char*, char*) call binds the (char*, global) overload
 * at 8371 via the implicit char*->global conversion - noted per
 * the LibmpscppMisc9 header).
 */
fun bmg_fullsearch(a: String, buf: String): Int {
    TODO("h014 stub: owner libmpscpp.cpp::bmg_fullsearch (not a corpus unit)")
}

/**
 * (u45) Vendor file-scope globals of the libmpscpp.cpp Smith-Waterman
 * alignment block (h004: file-scope globals -> singleton object
 * properties, source lines libmpscpp.cpp:8031-8040):
 *   short **__a -> a (owner init, libmpscpp.cpp:8100, batch-4 unit,
 *   allocates; empty array before init, mirroring the vendor's NULL)
 *   string S_RESULT -> sResult
 *   int GAP -> gap, int MISMATCH -> mismatch, int MATCH -> match
 * The state persists across prnt/sw/init calls by design (the
 * alignment block's cross-call state, not per-call scratch).
 */
object AlignState {
    var a: Array<ShortArray> = arrayOf()
    var sResult: String = ""
    var gap: Int = 0
    var mismatch: Int = 0
    var match: Int = 0

    // (u61) libmpscpp.cpp:8033-8039 file-scope alignment globals.
    /** :8033 - int Size. */
    var Size: Int = 0
    /** :8034 - int Size_s. */
    var Size_s: Int = 0
    /** :8035 - int Size_t. */
    var Size_t: Int = 0
    /** :8039 - int sx = 0. */
    var sx: Int = 0
}

// (u82) The Cost stub raised by unit 61 (Sw) is RESOLVED: B3 unit
// 82 translated libmpscpp.cpp::Cost - see Cost.kt (the stub was
// removed, cleanLocks precedent, unit 56).

/** Owner: libmpscpp.cpp::init (batch-4 corpus unit, not yet translated). */
// (B4 unit 109) The init stub raised by unit 61 (Sw) is RESOLVED:
// B4 unit 109 translated libmpscpp.cpp::init - see Init.kt (the
// stub was removed, cleanLocks precedent, unit 56); Sw.kt's init()
// call site now binds the real owner.

/** Owner: libmpscpp.cpp:8041 nodash (not a corpus unit). */
fun nodash(a: CharArray): Int {
    TODO("h014 stub: owner libmpscpp.cpp::nodash (not a corpus unit)")
}

/** Owner: libmpscpp.cpp:8049 inline int Score(char,char) (not a corpus unit). */
fun Score(a: Char, b: Char): Int {
    TODO("h014 stub: owner libmpscpp.cpp:8049 inline Score (not a corpus unit)")
}

// --- (u62) global numeric carrier stubs (owners: the operator>>/<<
// extraction and insertion families and the class global operator=
// machinery; the extraction/insertion owners live in the misc-3
// residual chunk, not yet translated) ---

/** Owner: libmpscpp.cpp global::operator>>(int&) (misc-3 unit, not yet translated). */
fun Global.extractInt(): Int {
    TODO("h014 stub: owner libmpscpp.cpp global::operator>>(int&) (misc-3 unit)")
}

/** Owner: libmpscpp.cpp global::operator>>(unsigned int&) (misc-3 unit, not yet translated). */
fun Global.extractUInt(): UInt {
    TODO("h014 stub: owner libmpscpp.cpp global::operator>>(unsigned int&) (misc-3 unit)")
}

/** Owner: libmpscpp.cpp global::operator>>(short&) (misc-3 unit, not yet translated). */
fun Global.extractShort(): Short {
    TODO("h014 stub: owner libmpscpp.cpp global::operator>>(short&) (misc-3 unit)")
}

/** Owner: libmpscpp.cpp global::operator>>(unsigned short&) (misc-3 unit, not yet translated). */
fun Global.extractUShort(): UShort {
    TODO("h014 stub: owner libmpscpp.cpp global::operator>>(unsigned short&) (misc-3 unit)")
}

/** Owner: libmpscpp.cpp global::operator>>(long&) (misc-3 unit, not yet translated). */
fun Global.extractLong(): Long {
    TODO("h014 stub: owner libmpscpp.cpp global::operator>>(long&) (misc-3 unit)")
}

/** Owner: libmpscpp.cpp global::operator>>(unsigned long&) (misc-3 unit, not yet translated). */
fun Global.extractULong(): ULong {
    TODO("h014 stub: owner libmpscpp.cpp global::operator>>(unsigned long&) (misc-3 unit)")
}

/** Owner: libmpscpp.cpp global::operator>>(float&) (misc-3 unit, not yet translated). */
fun Global.extractFloat(): Float {
    TODO("h014 stub: owner libmpscpp.cpp global::operator>>(float&) (misc-3 unit)")
}

/** Owner: libmpscpp.cpp global::operator>>(double&) (misc-3 unit, not yet translated). */
fun Global.extractDouble(): Double {
    TODO("h014 stub: owner libmpscpp.cpp global::operator>>(double&) (misc-3 unit)")
}

/** Owner: libmpscpp.cpp global::operator>>(char*) (misc-3 unit, not yet translated). */
fun Global.extractCstring(): CharArray {
    TODO("h014 stub: owner libmpscpp.cpp global::operator>>(char*) (misc-3 unit)")
}

/** Owner: libmpscpp.cpp global::operator<<(long) (misc-3 unit, not yet translated). */
fun Global.insertLong(v: Long) {
    TODO("h014 stub: owner libmpscpp.cpp global::operator<<(long) (misc-3 unit)")
}

/** Owner: libmpscpp.cpp global::operator<<(double) (misc-3 unit, not yet translated). */
fun Global.insertDouble(v: Double) {
    TODO("h014 stub: owner libmpscpp.cpp global::operator<<(double) (misc-3 unit)")
}

/** Owner: libmpscpp.h class global operator= machinery (not a corpus unit). */
fun Global.assignInt(v: Int) {
    TODO("h014 stub: owner libmpscpp.h class global::operator=(int) machinery (not a corpus unit)")
}

/** Owner: libmpscpp.h class global operator= machinery (not a corpus unit). */
fun Global.assignUInt(v: UInt) {
    TODO("h014 stub: owner libmpscpp.h class global::operator=(unsigned int) machinery (not a corpus unit)")
}

/** Owner: libmpscpp.h class global operator= machinery (not a corpus unit). */
fun Global.assignShort(v: Short) {
    TODO("h014 stub: owner libmpscpp.h class global::operator=(short) machinery (not a corpus unit)")
}

/** Owner: libmpscpp.h class global operator= machinery (not a corpus unit). */
fun Global.assignUShort(v: UShort) {
    TODO("h014 stub: owner libmpscpp.h class global::operator=(unsigned short) machinery (not a corpus unit)")
}

/** Owner: libmpscpp.h class global operator= machinery (not a corpus unit). */
fun Global.assignLong(v: Long) {
    TODO("h014 stub: owner libmpscpp.h class global::operator=(long) machinery (not a corpus unit)")
}

/** Owner: libmpscpp.h class global operator= machinery (not a corpus unit). */
fun Global.assignULong(v: ULong) {
    TODO("h014 stub: owner libmpscpp.h class global::operator=(unsigned long) machinery (not a corpus unit)")
}

/** Owner: libmpscpp.h class global operator= machinery (not a corpus unit). */
fun Global.assignFloat(v: Float) {
    TODO("h014 stub: owner libmpscpp.h class global::operator=(float) machinery (not a corpus unit)")
}

/** Owner: libmpscpp.h class global operator= machinery (not a corpus unit). */
fun Global.assignDouble(v: Double) {
    TODO("h014 stub: owner libmpscpp.h class global::operator=(double) machinery (not a corpus unit)")
}

// --- added by B3 unit 71 (libmpscpp.cpp::Cosine) ---

/**
 * (u218) RESOLVED: libmpscpp.cpp::get_global is the
 * libmpscpp.h:117 macro, expanded inline per its definition -
 * svPtr->tpx = Mglobal(RETRIEVE, ref, rslt, svPtr).  The macro
 * value (rslt) is used only as a statement at every call site
 * (Cosine/Dice/GlobalMax precedent, u218 Set/Get machinery),
 * so the seat stays Unit.  RETRIEVE = 0 (defines.h:83).
 */
fun get_global(src: CharArray, rslt: CharArray) {
    MdhState.svPtr.tpx = mumps.interp.mglobal(0, src, rslt, MdhState.svPtr).toLong()
}

/**
 * (u218) libmpscpp.h:118 macro set_global(ref,rslt) - same
 * expansion with STORE = 1 (defines.h:84); seat raised and
 * resolved by the misc-7 Set members (call sites bind here).
 */
fun set_global(ref: CharArray, rslt: CharArray) {
    MdhState.svPtr.tpx = mumps.interp.mglobal(1, ref, rslt, MdhState.svPtr).toLong()
}

// (u81) The Scan stub raised by unit 79 (ScanAlnum) is RESOLVED:
// B3 unit 81 translated libmpscpp.cpp::Scan - see Scan.kt (the
// stub was removed, cleanLocks precedent, unit 56).

// --- added by B3 unit 88 (libmpscpp.cpp::Max) ---

/** Owner: libmpscpp.cpp::global::Max - RESOLVED by GlobalMax.kt
 *  (unit 169, vendor 1651-1688; roster row 170, batch 6 - the
 *  u148 tag-correction family: u88's "not a corpus unit" tag
 *  superseded, u141 precedent); stub removed (the Max.kt wrapper
 *  call sites a("").Max() / a(subs).Max() bind unchanged). */

// --- added by B5 unit 148 (libmpscpp.cpp::Avg) ---

/**
 * Owner: libmpscpp.cpp::global::Avg (libmpscpp.cpp:1726, corpus
 * unit roster row 262, batch 9) - RESOLVED by GlobalAvg.kt
 * (unit 262, batch 9, vendor 1726-1756; roster row 262; the
 * a("").Avg() call sites in Avg.kt bind unchanged). TAG NOTE
 * (u141 precedent): the sibling global::Max above was tagged
 * "not a corpus unit" by u88 but IS roster row 170 (batch 6) -
 * superseded note, kept verbatim here.
 */


// --- added by B5 unit 124 (libmpscpp.cpp::<misc-5>) ---

/** Owner: interp-stem.cpp stem tables (batch-6 unit owns the tables; wstem itself is not a corpus unit). */
fun wstem(word: CharArray, len: Int): Int {
    TODO("h014 stub: owner interp-stem.cpp::wstem (stem tables, batch-6)")
}

/** Owner: interp-pm.cpp:43 pm (pattern match engine, not a corpus unit). */
fun pm(string: CharArray, pattern: CharArray, svPtr: mumps.interp.Msv): Int {
    TODO("h014 stub: owner interp-pm.cpp::pm (not a corpus unit)")
}

/** Owner: libmpscpp.cpp::global::DocCorrelate(global,string,double) (vendor 1215, batch-10 corpus unit). */
fun docCorrelate(B: Global, fcn: String, threshold: Double) {
    TODO("h014 stub: owner libmpscpp.cpp::global::DocCorrelate(global,string,double) (batch-10 unit)")
}

// --- opaque GTK carriers (GUI glue; the mdh wrappers only forward
//     opaque handles to the underlying libgtk calls) ---

/** u156/u157 gate-fix carrier: the shared marker supertype of the
 *  opaque GTK widget stand-ins. The ZfcnState ptrReg registry (the
 *  vendor sscanf percent-p decode of a widget pointer) needs one
 *  type-erased value type; the first draft used java.lang.Object,
 *  which the gradle gate flags ("not recommended in Kotlin"), and
 *  kotlin.Any subtyping is unreliable under the lean local gate
 *  toolchain - the marker interface keeps both gates clean and the
 *  stand-ins opaque. */
interface GtkHandle

open class GtkWindow : GtkWidget(), GtkHandle
class GtkToggleButton : GtkHandle
class GtkSwitch : GtkHandle
class GtkEntry : GtkHandle
class GtkRange : GtkHandle
class GtkCalendar : GtkHandle
class GtkTextBuffer : GtkHandle
class GtkFontChooser : GtkHandle
class GtkColorChooser : GtkHandle
class GtkLabel : GtkHandle
open class GtkWidget : GtkHandle
class GtkTreeSelection : GtkHandle
class GtkTreeIter
class GtkTreeModel : GtkHandle
class GtkAdjustment : GtkHandle
class GtkSpinButton : GtkHandle

/** u156 (interp-zfcns.cpp::zfcn): the dialog and chooser carriers;
 *  GtkDialog : GtkWindow : GtkWidget mirrors the libgtk hierarchy
 *  (the dialog arm passes its dialog to gtk_window_set_keep_above
 *  and gtk_widget_destroy). */
class GtkDialog : GtkWindow(), GtkHandle
class GtkFileChooser : GtkHandle
class GtkTreeView : GtkHandle

/** vendor struct GdkRGBA (gtk color carrier for mdh_color_chooser_get_rgba). */
class GdkRGBA {
    var red: Double = 0.0
    var green: Double = 0.0
    var blue: Double = 0.0
    var alpha: Double = 0.0
}

// --- underlying libgtk C calls (h014: owner libgtk, out of corpus) ---

fun gtk_toggle_button_get_active(b: GtkToggleButton): Boolean {
    TODO("h014 stub: owner libgtk gtk_toggle_button_get_active (out of corpus)")
}

fun gtk_toggle_button_set_active(b: GtkToggleButton, x: Boolean) {
    TODO("h014 stub: owner libgtk gtk_toggle_button_set_active (out of corpus)")
}

fun gtk_switch_get_active(b: GtkSwitch): Boolean {
    TODO("h014 stub: owner libgtk gtk_switch_get_active (out of corpus)")
}

fun gtk_entry_get_text(e: GtkEntry): String {
    TODO("h014 stub: owner libgtk gtk_entry_get_text (out of corpus)")
}

fun gtk_entry_set_text(e: GtkEntry, txt: String) {
    TODO("h014 stub: owner libgtk gtk_entry_set_text (out of corpus)")
}

fun gtk_range_get_adjustment(e: GtkRange): GtkAdjustment {
    TODO("h014 stub: owner libgtk gtk_range_get_adjustment (out of corpus)")
}

fun gtk_adjustment_get_value(adj: GtkAdjustment): Double {
    TODO("h014 stub: owner libgtk gtk_adjustment_get_value (out of corpus)")
}

fun gtk_calendar_get_date(e: GtkCalendar, yr: LongArray, mn: LongArray, dy: LongArray) {
    TODO("h014 stub: owner libgtk gtk_calendar_get_date (out of corpus)")
}

fun gtk_text_buffer_set_text(t: GtkTextBuffer, txt: String, len: Int) {
    TODO("h014 stub: owner libgtk gtk_text_buffer_set_text (out of corpus)")
}

fun gtk_font_chooser_get_font(b: GtkFontChooser): String {
    TODO("h014 stub: owner libgtk gtk_font_chooser_get_font (out of corpus)")
}

fun gtk_color_chooser_get_rgba(c: GtkColorChooser, r: GdkRGBA) {
    TODO("h014 stub: owner libgtk gtk_color_chooser_get_rgba (out of corpus)")
}

fun gtk_label_set_text(l: GtkLabel, txt: String) {
    TODO("h014 stub: owner libgtk gtk_label_set_text (out of corpus)")
}

fun gtk_widget_hide(w: GtkWidget) {
    TODO("h014 stub: owner libgtk gtk_widget_hide (out of corpus)")
}

fun gtk_widget_show(w: GtkWidget) {
    TODO("h014 stub: owner libgtk gtk_widget_show (out of corpus)")
}

fun gtk_tree_selection_get_selected(
    t: GtkTreeSelection,
    m: Array<GtkTreeModel?>,
    iter: GtkTreeIter
): Boolean {
    TODO("h014 stub: owner libgtk gtk_tree_selection_get_selected (out of corpus)")
}

fun gtk_tree_model_get(m: GtkTreeModel, iter: GtkTreeIter, col: Int): String {
    TODO("h014 stub: owner libgtk gtk_tree_model_get (out of corpus)")
}

fun gtk_tree_store_clear(t: GtkTreeStore) {
    TODO("h014 stub: owner libgtk gtk_tree_store_clear (out of corpus)")
}

fun gtk_spin_button_get_value(s: GtkSpinButton): Double {
    TODO("h014 stub: owner libgtk gtk_spin_button_get_value (out of corpus)")
}

fun gtk_spin_button_set_value(s: GtkSpinButton, v: Double) {
    TODO("h014 stub: owner libgtk gtk_spin_button_set_value (out of corpus)")
}

// --- added by u156 (interp-zfcns.cpp::zfcn GTK arms; owner libgtk,
//     out of corpus) ---

fun gtk_dialog_new_with_buttons(title: String, parent: GtkWindow?, flags: Int): GtkDialog {
    TODO("h014 stub: owner libgtk gtk_dialog_new_with_buttons (out of corpus)")
}

fun gtk_dialog_run(d: GtkDialog): Int {
    TODO("h014 stub: owner libgtk gtk_dialog_run (out of corpus)")
}

fun gtk_window_set_keep_above(w: GtkWindow, b: Boolean) {
    TODO("h014 stub: owner libgtk gtk_window_set_keep_above (out of corpus)")
}

fun gtk_widget_destroy(w: GtkWidget) {
    TODO("h014 stub: owner libgtk gtk_widget_destroy (out of corpus)")
}

fun gtk_tree_view_get_selection(t: GtkTreeView): GtkTreeSelection {
    TODO("h014 stub: owner libgtk gtk_tree_view_get_selection (out of corpus)")
}

fun gtk_file_chooser_get_filename(c: GtkFileChooser): String? {
    TODO("h014 stub: owner libgtk gtk_file_chooser_get_filename (out of corpus)")
}

// --- added by B7 unit 187 (ServiceInterface INVDF relay) ---

/**
 * Owner: libmpscpp.cpp::IDF - RESOLVED by IdfWrapper.kt
 * (unit 240, vendor 10370-10388; roster row 240, batch 8); the
 * stub raised at u187 (the INVDF ServiceInterface relay,
 * interp-service.cpp:303-312, is the only in-arm caller) is
 * removed - the relay binds to the real IDF seat unchanged.
 */

