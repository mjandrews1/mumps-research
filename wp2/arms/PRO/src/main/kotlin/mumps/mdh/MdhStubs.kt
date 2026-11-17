package mumps.mdh

import mumps.interp.Msv

/**
 * Opaque PRO-arm stand-in for the GTK type GtkTreeStore (GUI glue;
 * the mdh functions only forward an opaque handle to the $z dispatcher).
 */
class GtkTreeStore

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

/** Owner: interp-bifs.cpp::zfcn (not a corpus unit). */
fun zfcn(v1d: CharArray, bd: CharArray, svPtr: Msv) {
    TODO("h014 stub: owner interp-bifs.cpp::zfcn (not a corpus unit)")
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
 * Owner: libmpscpp.cpp::global::MakeGlobal (batch-7 unit).
 * C variadic MakeGlobal(int, char *gblnme, char *key, ...) ->
 * vararg values (B1 unit 31).
 */
fun makeGlobal(x: Int, gblnme: String, key: String, vararg vals: String) {
    TODO("h014 stub: owner libmpscpp.cpp::global::MakeGlobal (batch-7 unit)")
}



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

/** Owner: interp-sym.cpp::_extract (batch-10 unit). */
fun _extract(r: CharArray, src: CharArray, a: CharArray, b: CharArray) {
    TODO("h014 stub: owner interp-sym.cpp::_extract (batch-10 unit)")
}

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

/** Owner: libmpscpp.cpp::Sim1 (batch-7 corpus unit, units.csv row 199). */
fun Sim1(a: Global, b: Global): Double {
    TODO("h014 stub: owner libmpscpp.cpp::Sim1 (batch-7 unit)")
}

/** Owner: libmpscpp.cpp::Jaccard (batch-9 corpus unit, units.csv row 256). */
fun Jaccard(a: Global, b: Global): Double {
    TODO("h014 stub: owner libmpscpp.cpp::Jaccard (batch-9 unit)")
}

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


/** Owner: libmpscpp.cpp::StopInit (batch-6 corpus unit, row 175; the wrapper binds the char* overload at 7720). */
fun StopInit(file: String) {
    TODO("h014 stub: owner libmpscpp.cpp::StopInit (batch-6 unit)")
}

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

/** Owner: libmpscpp.cpp::get_global (not a corpus unit; called by Cosine). */
fun get_global(src: CharArray, rslt: CharArray) {
    TODO("h014 stub: owner libmpscpp.cpp::get_global (not a corpus unit)")
}

// (u81) The Scan stub raised by unit 79 (ScanAlnum) is RESOLVED:
// B3 unit 81 translated libmpscpp.cpp::Scan - see Scan.kt (the
// stub was removed, cleanLocks precedent, unit 56).

// --- added by B3 unit 88 (libmpscpp.cpp::Max) ---

/** Owner: libmpscpp.cpp::global::Max (libmpscpp.cpp:1651, not a corpus unit; sibling of B2 unit 44's global::Min). */
fun GlobalRef.Max(): Double {
    TODO("h014 stub: owner libmpscpp.cpp::global::Max (not a corpus unit)")
}

// --- added by B5 unit 148 (libmpscpp.cpp::Avg) ---

/**
 * Owner: libmpscpp.cpp::global::Avg (libmpscpp.cpp:1726, corpus
 * unit roster row 262, batch 9 - out of B5 scope; resolves when
 * batch 9 lands). TAG NOTE (u141 precedent): the sibling
 * global::Max above was tagged "not a corpus unit" by u88 but IS
 * roster row 170 (batch 6) - superseded note, kept verbatim here.
 */
fun GlobalRef.Avg(): Double {
    TODO("h014 stub: owner libmpscpp.cpp::global::Avg (corpus unit row 262, batch 9)")
}


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

class GtkWindow
class GtkToggleButton
class GtkSwitch
class GtkEntry
class GtkRange
class GtkCalendar
class GtkTextBuffer
class GtkFontChooser
class GtkColorChooser
class GtkLabel
class GtkWidget
class GtkTreeSelection
class GtkTreeIter
class GtkTreeModel
class GtkAdjustment
class GtkSpinButton

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
