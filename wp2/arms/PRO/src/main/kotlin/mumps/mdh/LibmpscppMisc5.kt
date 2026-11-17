package mumps.mdh

import java.io.IOException
import java.io.RandomAccessFile
import java.util.Locale
import kotlin.system.exitProcess

/**
 * PRO-arm translation of libmpscpp.cpp::<misc-5> (B5 unit 124, rank 295,
 * complex stratum, 600 physLOC residue chunk). Translated independently
 * from the pristine vendor source (mumps-14-may-2026.tgz, sha256
 * 621deb25...ffdbe).
 *
 * Chunk composition (per the pre-registered chunker, tools/select_study_
 * subset.py): the residue of libmpscpp.cpp not claimed by >=15-LOC named
 * corpus functions, plus the file's <15-LOC function bodies appended in
 * encounter order, cut at 600 physLOC. Vendor anchors in the section
 * headers below are the true file lines of each element.
 *
 * FRONT-RUN content (already rendered by earlier units; verification
 * journal rows only, NOT redeclared here - the u43/u54 precedent):
 *  - the mstring operator<= / == / != families (vendor 9693-9838) were
 *    rendered by B4 unit 97 (Mstring.equals/hashCode in Mstring.kt and
 *    the compareTo extensions in LibmpscppMisc9.kt);
 *  - the Transpose(char-star, char-star) and Transpose(mstring, mstring)
 *    forwarders (vendor 2113-2134 region) fold into B4 unit 117's
 *    Transpose(String, String) per its roster note;
 *  - the mcvt(char-star) body (vendor 2677-2681) is CUT MID-BODY by the
 *    600-LOC chunk boundary - only its two head lines are in this unit's
 *    text; the whole mcvt family renders with its misc-6 owner. Roster
 *    note only, no partial code here.
 *
 * STUB RESOLUTIONS (h014 delegates landing now, stubs removed from
 * MdhStubs.kt): thrownMessage, __finish, lhash (all added as stubs by B1
 * unit 31), and GlobalRef.Count (added by B4 unit 118 - the real vendor
 * carrier is the Global member global::Count, not a GlobalRef extension;
 * the stub was TODO-dead, no live call sites).
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h008 fresh scratch  : vendor static buffers become fresh locals.
 *  - h009 sprintf collapse: sprintf/strcpy/strcat collapse to String
 *    composition and the local toCbuf/copyInto helpers.
 *  - h014 out_of_scope_call: wstem (owner interp-stem.cpp stem tables,
 *    batch-6), pm (owner interp-pm.cpp:43 pattern match, not a corpus
 *    unit), the gtk_* C library calls (owner libgtk, out of corpus),
 *    zfcn/xecute/mglobal/readInto/globalNumericValue/globalClose
 *    (existing stubs/carriers), and docCorrelate (owner
 *    libmpscpp.cpp::global::DocCorrelate(global,string,double) at
 *    vendor 1215, a batch-10 corpus unit).
 *  - h015 grammar_collision: vendor string literals containing dollar
 *    bytes are built with 36.toChar(); the vendor prefix-star operator
 *    has no Kotlin overload (rendered as indirectOf); overload folds
 *    documented per section.
 *  - h017 naming          : vendor spellings kept (thrownMessage,
 *    __finish with the double-underscore gate exception noted at the
 *    existing MdhStubs stub, ZSeek, Ztell, Getenv, pt, pmw, mcvt roster
 *    note).
 *
 * Vendor defects kept/quantized (PN-1 quirk ledger):
 *  - thrownMessage prints "Mumps has detetected an uncaught exception"
 *    (vendor typo kept verbatim, vendor 88);
 *  - pt (vendor 1462) declares an uninitialized int i and loops
 *    while(x[i]) from that garbage value - rendered from i=0 as the
 *    only deterministic reading (UB quantized);
 *  - Hash/lhash (vendor 2314/2335) build an unsigned long a then write
 *    only its low 4 bytes (the upper 4 stay indeterminate) - rendered
 *    with zero upper bytes (UB quantized); the 4-byte little-endian
 *    packing and the unsigned wraparound of the running sum are kept;
 *  - GlobalSet (vendor 2365+) carries a stray lone backslash line after
 *    the strcat block (a line-continuation artifact) - dropped with this
 *    note;
 *  - GlobalGet(char-star) (vendor 2463) strcpy's an empty string into r
 *    on the failure path, then unconditionally overwrites r with
 *    SymGet of the percent variable - the dead store is dropped, the
 *    observable (return value equals SymGet on BOTH paths) is kept.
 */

private const val STR_MAX = 32000 // vendor strmax.h:3 (file-private, cf. unit 22/97)
private const val INVALID_CONVERSION = 1 // vendor libmpscpp.h:70
private const val GLOBAL_NOT_FOUND = 2  // vendor libmpscpp.h:71
private const val INPUT_LENGTH = 10     // vendor libmpscpp.h:79
private const val XNEXT = 8             // vendor defines.h:88 (Mglobal order-next)

/** vendor xce argument marker byte (TokenInit/z-call convention). */
private val XCE = 0xCE.toChar()

/** vendor xcf end-of-argument marker byte. */
private val XCF = 0xCF.toChar()

/** vendor: the static int flg of __finish (file-scope). */
private var finishFlg = 0

/** String -> NUL-terminated C-string buffer (call-shape helper, h009). */
private fun toCbuf(s: String): CharArray {
    val b = CharArray(s.length + 1)
    for (k in s.indices) b[k] = s[k]
    b[s.length] = mumps.interp.NUL
    return b
}

/** strcpy(dst, src) analog into an existing buffer (h009). */
private fun copyInto(dst: CharArray, src: String) {
    for (k in src.indices) dst[k] = src[k]
    if (src.length < dst.size) dst[src.length] = mumps.interp.NUL
}

/** strlen((char*) b) analog (h009). */
private fun strlenC(b: CharArray): Int {
    var i = 0
    while (i < b.size && b[i] != mumps.interp.NUL) i++
    return i
}

// -----------------------------------------------
//  thrownMessage (vendor 86-98) - resolves the MdhStubs h014 stub
// -----------------------------------------------

/**
 * vendor 86: void thrownMessage() - set_terminate target installed by
 * the global constructor (wired in GlobalCarrier; the ctor note keeps
 * set_terminate unwired per B1 unit 31). cout output to stdout.
 */
fun thrownMessage() {
    println("Mumps has detetected an uncaught exception: ") // vendor typo kept

    if (MdhState.globalExceptionCode == INVALID_CONVERSION)
        println("Invalid conversion from global to numeric")

    if (MdhState.globalExceptionCode == GLOBAL_NOT_FOUND)
        println("Global not found")

    if (MdhState.globalExceptionCode == INPUT_LENGTH)
        println("Input Line Too Long")
}

// -----------------------------------------------
//  __finish (vendor 102-107) - resolves the MdhStubs h014 stub
// -----------------------------------------------

/**
 * vendor 102: void __finish() - atexit target of the global
 * constructor. static int flg re-entry guard; GlobalClose then a second
 * call terminates immediately (h008 flg is the file-scope analog of the
 * vendor's function-static). _exit/exit -> exitProcess.
 */
fun __finish() {
    if (finishFlg != 0) exitProcess(0) // vendor: _exit(0), immediate terminate

    mumps.interp.globalClose(MdhState.svPtr) // vendor: GlobalClose (h014 stub)
    finishFlg = 1
    exitProcess(0)
}

// -----------------------------------------------
//  global conversion members (vendor 112-135)
// -----------------------------------------------

/**
 * vendor 112: int global::Int() - (*this) >> i; return i. The per-type
 * extraction operators collapse to the Double-valued globalNumericValue
 * stub (B1 unit 31 convention); the Int carrier folds to toInt (h015).
 */
fun Global.Int(): Int = globalNumericValue(this).toInt()

/** vendor 118: double global::Double(). */
fun Global.Double(): Double = globalNumericValue(this)

/** vendor 124: mstring global::Mstring() - extraction into a fresh mstring. */
fun Global.Mstring(): Mstring {
    val tmp = CharArray(STR_MAX)
    readInto(tmp) // vendor: (*this) >> i (h014 extraction carrier)
    return Mstring(mumps.interp.cstringAt(tmp))
}

/**
 * vendor 130: char-star global::Char(char-star i, int max) - extraction
 * into tmp then strncpy(i, tmp, max); returns i. The strncpy is a capped
 * copy (h009 collapse; no NUL guarantee beyond max - kept).
 */
fun Global.Char(i: CharArray, max: Int): CharArray {
    val tmp = CharArray(STR_MAX)
    readInto(tmp)
    var k = 0
    while (k < max && k < tmp.size && tmp[k] != mumps.interp.NUL) {
        i[k] = tmp[k]; k++
    }
    if (k < i.size && k == max) i[k] = mumps.interp.NUL
    return i
}

// -----------------------------------------------
//  Stem / TokenInit / Token (vendor 137-163)
// -----------------------------------------------

/**
 * vendor 137: mstring Stem(mstring and arg) - wstem over a scratch copy;
 * the stemmed word comes back as a new mstring.
 */
fun Stem(arg: Mstring): Mstring {
    val tmp = CharArray(STR_MAX) // vendor: static-char -> fresh (h008)
    copyInto(tmp, arg.value)
    wstem(tmp, strlenC(tmp)) // h014 stub: owner interp-stem.cpp stem tables (batch-6)
    return Mstring(mumps.interp.cstringAt(tmp))
}

/**
 * vendor 146: mstring TokenInit(mstring and arg) - builds the
 * dollar-zwi z-call: space + dollar + zwi + xce + arg + xcf; result read from
 * svPtr.bd.
 */
fun TokenInit(arg: Mstring): Mstring {
    val tmp = CharArray(STR_MAX)
    var n = 0
    val head = " " + 36.toChar() + "zwi"
    for (c in head) tmp[n++] = c
    tmp[n++] = XCE
    for (c in arg.value) tmp[n++] = c
    tmp[n++] = XCF
    val tmp2 = CharArray(STR_MAX)
    zfcn(tmp, tmp2, MdhState.svPtr) // h014 stub: owner interp-bifs.cpp::zfcn
    return Mstring(mumps.interp.cstringAt(MdhState.svPtr.bd)) // vendor: rslt=(char*)svPtr->bd
}

/**
 * vendor 157: mstring Token() - the dollar-zwp token pull; result is
 * tmp2 (NOT svPtr->bd - differs from TokenInit; kept).
 */
fun Token(): Mstring {
    val tmp = CharArray(STR_MAX)
    var n = 0
    val head = " " + 36.toChar() + "zwp"
    for (c in head) tmp[n++] = c
    tmp[n++] = XCF // vendor: no explicit xce here; the head ends and zfcn is called
    val tmp2 = CharArray(STR_MAX)
    zfcn(tmp, tmp2, MdhState.svPtr)
    return Mstring(mumps.interp.cstringAt(tmp2))
}

// -----------------------------------------------
//  GTK glue (vendor 596-818)
// -----------------------------------------------

/**
 * vendor 596: int mdh_dialog_new_with_buttons(GtkWindow and win,
 * char-star text) - builds the dollar-z~mdh~dialog~new~with~buttons
 * z-call with the window POINTER formatted via percent-p. The pointer
 * print has no Kotlin analog; the opaque handle renders as its
 * identity hash string (h015 adaptation, journaled).
 */
fun mdh_dialog_new_with_buttons(win: GtkWindow, text: String): Int {
    val tmp = CharArray(4096)
    val tmp1 = CharArray(256)
    var n = 1
    val head = 36.toChar() + "z~mdh~dialog~new~with~buttons"
    for (c in head) tmp[n++] = c
    tmp[n++] = XCE

    // vendor: sprintf(tmp1, "%p", win) - opaque handle, identity hash (h015)
    val p = System.identityHashCode(win).toString(16)
    for (k in p.indices) tmp1[k] = p[k]
    tmp1[p.length] = mumps.interp.NUL
    tmp[n++] = 0xD0.toChar() // vendor xd0 byte after the pointer

    for (c in text) tmp[n++] = c
    tmp[n++] = XCF

    zfcn(tmp, tmp1, MdhState.svPtr) // h014 stub: owner interp-bifs.cpp::zfcn

    if (tmp1[0] == '0') return 0

    return 1
}

/**
 * vendor 621: int mdh_toggle_button_get_active(GtkToggleButton and b).
 */
fun mdh_toggle_button_get_active(b: GtkToggleButton): Int =
    if (gtk_toggle_button_get_active(b)) 1 else 0

/** vendor 631: int mdh_switch_get_active(GtkSwitch and b). */
fun mdh_switch_get_active(b: GtkSwitch): Int =
    if (gtk_switch_get_active(b)) 1 else 0

/** vendor 641: void mdh_toggle_button_set_active(GtkToggleButton and b, int v). */
fun mdh_toggle_button_set_active(b: GtkToggleButton, v: Int) {
    val x = v != 0
    gtk_toggle_button_set_active(b, x)
}

/**
 * vendor 656: char-star mdh_entry_get_text(GtkEntry and e, char-star
 * txt) - strcpy(txt, gtk_entry_get_text(e)); returns txt.
 */
fun mdh_entry_get_text(e: GtkEntry, txt: CharArray): CharArray {
    val s = gtk_entry_get_text(e)
    copyInto(txt, s)
    return txt
}

/** vendor 667: double mdh_range_get_adjustment(GtkRange and e). */
fun mdh_range_get_adjustment(e: GtkRange): Double {
    val adj = gtk_range_get_adjustment(e)
    return gtk_adjustment_get_value(adj)
}

/**
 * vendor 676: char-star mdh_calendar_get_date(GtkCalendar and e) -
 * static tmp (fresh, h008); the guint out-params fold to the Long
 * domain (h015 - the arm keeps warning-clean, GlobalOps precedent);
 * percent-d format via String.format Locale.US (h009).
 */
fun mdh_calendar_get_date(e: GtkCalendar): String {
    val yr = LongArray(1); val mn = LongArray(1); val dy = LongArray(1)
    gtk_calendar_get_date(e, yr, mn, dy)
    return String.format(
        Locale.US, "%d %d %d",
        yr[0].toLong(), mn[0].toLong(), dy[0].toLong()
    )
}

/** vendor 691: void mdh_entry_set_text(GtkEntry and e, char-star txt). */
fun mdh_entry_set_text(e: GtkEntry, txt: String) = gtk_entry_set_text(e, txt)

/** vendor 701: void mdh_text_buffer_set_text(GtkTextBuffer and t, char-star txt). */
fun mdh_text_buffer_set_text(t: GtkTextBuffer, txt: String) =
    gtk_text_buffer_set_text(t, txt, -1)

/** vendor 711: char-star mdh_font_chooser_get_font(GtkFontChooser and b). */
fun mdh_font_chooser_get_font(b: GtkFontChooser): String =
    gtk_font_chooser_get_font(b)

/**
 * vendor 721: char-star mdh_color_chooser_get_rgba(GtkColorChooser and
 * c) - percent-f formats via String.format Locale.US (h009).
 */
fun mdh_color_chooser_get_rgba(c: GtkColorChooser): String {
    val r = GdkRGBA()
    gtk_color_chooser_get_rgba(c, r)
    return String.format(
        Locale.US, "%f %f %f %f", r.red, r.green, r.blue, r.alpha
    )
}

/** vendor 734: void mdh_label_set_text(GtkLabel and l, char-star txt). */
fun mdh_label_set_text(l: GtkLabel, txt: String) = gtk_label_set_text(l, txt)

/** vendor 744: void mdh_widget_hide(GtkWidget and w). */
fun mdh_widget_hide(w: GtkWidget) = gtk_widget_hide(w)

/** vendor 754: void mdh_widget_show(GtkWidget and w). */
fun mdh_widget_show(w: GtkWidget) = gtk_widget_show(w)

/**
 * vendor 765: char-star mdh_tree_selection_get_selected(GtkTreeSelection
 * and t, int col, char-star txt).
 */
fun mdh_tree_selection_get_selected(t: GtkTreeSelection, col: Int, txt: CharArray): CharArray {
    copyInto(txt, "")

    val m = arrayOfNulls<GtkTreeModel>(1)
    val iter = GtkTreeIter()
    if (!gtk_tree_selection_get_selected(t, m, iter)) return txt

    val model = m[0] ?: return txt // vendor: gtk_tree_model_get(m,&iter,col,&value,-1)
    val value = gtk_tree_model_get(model, iter, col)
    copyInto(txt, value)
    return txt
}

/** vendor 788: void mdh_tree_store_clear(GtkTreeStore and t). */
fun mdh_tree_store_clear(t: GtkTreeStore) = gtk_tree_store_clear(t)

/** vendor 796: double mdh_spin_button_get_value(GtkSpinButton and s). */
fun mdh_spin_button_get_value(s: GtkSpinButton): Double = gtk_spin_button_get_value(s)

/** vendor 810: void mdh_spin_button_set_value(GtkSpinButton and s, double v). */
fun mdh_spin_button_set_value(s: GtkSpinButton, v: Double) = gtk_spin_button_set_value(s, v)

// -----------------------------------------------
//  Dump / Restore (vendor 818-870)
// -----------------------------------------------

/**
 * vendor 818/827/836: void Dump(mstring and)/(string and)/(char-star) -
 * the dollar-zcd z-call. The three overloads fold to the Mstring and
 * String carriers (the char-star and string shapes collapse after the
 * String carrier, h015 - Sw.kt wrapper convention).
 */
fun Dump(arg: Mstring) = dumpRestoreCmd(36.toChar() + "zcd", arg.value)
fun Dump(arg: String) = dumpRestoreCmd(36.toChar() + "zcd", arg)

/**
 * vendor 845/854/863: void Restore(mstring and)/(string and)/(char-star) -
 * the dollar-zcl z-call; same folding as Dump.
 */
fun Restore(arg: Mstring) = dumpRestoreCmd(36.toChar() + "zcl", arg.value)
fun Restore(arg: String) = dumpRestoreCmd(36.toChar() + "zcl", arg)

private fun dumpRestoreCmd(cmd: String, arg: String) {
    val tmp = CharArray(256)
    var n = 0
    for (c in cmd) tmp[n++] = c
    tmp[n++] = XCE
    for (c in arg) tmp[n++] = c
    tmp[n++] = XCF
    val tmp2 = CharArray(256)
    zfcn(tmp, tmp2, MdhState.svPtr) // h014 stub: owner interp-bifs.cpp::zfcn
}

// -----------------------------------------------
//  Getenv (vendor 872-882)
// -----------------------------------------------

/**
 * vendor 872: void Getenv(unsigned char-star out, unsigned char-star
 * in) - writes the env value or the empty string into out. The
 * unsigned-char carrier folds to CharArray/String (h015); in is a
 * Kotlin keyword - the parameter is in_ (u24 precedent).
 */
fun Getenv(out: CharArray, in_: String) {
    val p1 = System.getenv(in_)

    if (p1 == null) {
        out[0] = mumps.interp.NUL
        return
    }

    copyInto(out, p1)
}

// -----------------------------------------------
//  ZSeek / Ztell (vendor 885-922)
// -----------------------------------------------

/**
 * vendor 885/903: bool ZSeek(FILE-star u, mstring and s / global and s).
 * The FILE-star carrier is RandomAccessFile (B2 unit 56 precedent).
 * sscanf percent-lld -> String.toLong on the trimmed prefix (h009);
 * fsetpos err -> seek IOException (h001-family carrier adaptation).
 * The mstring and global overloads fold to String (global extracts
 * first via readInto).
 */
fun ZSeek(u: RandomAccessFile, s: String): Boolean {
    val offset = s.trim().toLongOrNull() ?: 0L
    return try {
        u.seek(offset)
        true // vendor: return !fsetpos(u, and offset)
    } catch (e: IOException) {
        false
    }
}

fun ZSeek(u: RandomAccessFile, s: Global): Boolean {
    val tmp1 = CharArray(STR_MAX)
    s.readInto(tmp1) // vendor: s >> tmp1 (h014 extraction carrier)
    return ZSeek(u, mumps.interp.cstringAt(tmp1))
}

/**
 * vendor 892/913: bool Ztell(FILE-star u, mstring and s / global and
 * s) - writes the fgetpos offset as a decimal string back into s.
 * The mstring writeback sets value (by-ref analog, h015). The global
 * writeback rides the numeric assign surface (GlobalRef.assign) -
 * the globalClass string-assign operator is out of corpus and the
 * offset is always numeric, so the observable is equal (h015 fold,
 * journaled).
 */
fun Ztell(u: RandomAccessFile, s: Mstring): Boolean {
    return try {
        s.value = u.filePointer.toString() // vendor: sprintf(tmp,"%lld", and offset)
        true
    } catch (e: IOException) {
        false
    }
}

fun Ztell(u: RandomAccessFile, s: Global): Boolean {
    return try {
        s().assign(u.filePointer.toDouble())
        true
    } catch (e: IOException) {
        false
    }
}

// -----------------------------------------------
//  Perl (vendor 924-933)
// -----------------------------------------------

/**
 * vendor 924/929: int Perl(mstring and a, mstring and b / char-star b).
 * The perlcpp call is COMMENTED OUT in the vendor source - the body is
 * just the return of svPtr tpx (kept verbatim, not a h013 drop: the vendor
 * ships it live). The char-star overload folds to String (h015).
 */
fun Perl(a: Mstring, b: Mstring): Int = MdhState.svPtr.tpx.toInt()
fun Perl(a: Mstring, b: String): Int = MdhState.svPtr.tpx.toInt()

// -----------------------------------------------
//  global::DocCorrelate wrappers (vendor 1203-1213)
// -----------------------------------------------

/**
 * vendor 1203/1209: void global::DocCorrelate(global B, char-star fcn
 * / mstring fcn, double threshold) - both forward to the
 * DocCorrelate(B, string, double) member (vendor 1215, a batch-10
 * corpus unit) after widening fcn to std::string. The two overloads
 * fold to the String carrier (h015); the delegation rides the h014
 * stub for the batch-10 owner.
 */
fun Global.DocCorrelate(B: Global, fcn: String, threshold: Double) {
    docCorrelate(B, fcn, threshold) // h014 stub: owner libmpscpp.cpp::global::DocCorrelate(global,string,double), batch-10
}

// -----------------------------------------------
//  pt (vendor 1462-1472)
// -----------------------------------------------

/**
 * vendor 1462: void pt(char-star t, char-star x) - printf debug helper.
 * VENDOR DEFECT (kept, quantized): int i is uninitialized and the
 * loop reads x[i] from garbage - rendered from i=0, the only
 * deterministic reading (PN-1).
 */
fun pt(t: CharArray, x: CharArray) {
    print(mumps.interp.cstringAt(t) + "  ")

    var i = 0
    while (x[i] != mumps.interp.NUL) {
        print(" " + x[i] + " ")
        i++
    }

    println()
}

// -----------------------------------------------
//  global::Count (vendor 1811) - resolves the MdhStubs GlobalRef.Count stub
// -----------------------------------------------

/**
 * vendor 1811: long global::Count() - XNEXT walk over the siblings of
 * this reference's prefix, counting while the first i bytes still match
 * ref. Quirks kept: the trailing-delimiter trim writes tmp[i]=0 only
 * after the pre-decrement (vendor tmp pre-decrement store); the ref compare uses
 * strncmp over i bytes.
 */
fun Global.Count(): Long {
    val ttt = CharArray(STR_MAX)
    val tmp = CharArray(STR_MAX)
    var j = 0L
    copyInto(tmp, ref)
    var i = strlenC(tmp)

    if (tmp[i - 1] < ' ' && tmp[i - 2] < ' ') tmp[--i] = mumps.interp.NUL

    while (true) {
        if (mumps.interp.mglobal(XNEXT, tmp, ttt, MdhState.svPtr) == 0) break

        val refB = toCbuf(ref)
        var same = true
        for (k in 0 until i) {
            if (refB[k] != tmp[k]) { same = false; break }
        }
        if (!same) break // vendor: strncmp(ref,tmp,i)!=0

        j++
    }

    return j
}

/**
 * Call-shape delegate: the vendor's global::operator() returns a
 * global VALUE, so vendor call sites like a("").Count() bind the
 * member on a subscripted global; the Kotlin carrier for that value
 * is GlobalRef, so the B4 unit-118 call sites resolve through this
 * extension (the u118 stub seat becomes a delegate - resolves the
 * MdhStubs GlobalRef.Count stub with the real owner body above).
 */
fun GlobalRef.Count(): Long = g.Count()

// -----------------------------------------------
//  global::Transpose (vendor 2069)
// -----------------------------------------------

/**
 * vendor 2069: void global::Transpose(global B) - kills B then writes
 * the two-level transpose of this into B. Same loop shape as B4 unit
 * 117's free Transpose(String, String) (which does NOT kill its
 * target - kept distinct). Assignment rides the u117 toDouble+assign
 * convention.
 */
fun Global.Transpose(B: Global) {
    B().Kill()

    var aa = ""
    var bb = ""

    while (true) {
        aa = this(aa).Order(1)
        if (aa == "") break

        bb = ""
        while (true) {
            bb = this(aa, bb).Order(1)
            if (bb == "") break

            B(bb, aa).assign(this(aa, bb).toDouble())
        }
    }
}

// -----------------------------------------------
//  stem (vendor 2293-2312)
// -----------------------------------------------

/**
 * vendor 2293: string stem(string and word) - wstem in place, returns
 * word. The C by-ref writeback of a String has no analog; the stemmed
 * value is the return (h015, journaled). static tmp -> fresh (h008).
 */
fun stem(word: String): String {
    val tmp = CharArray(STR_MAX)
    copyInto(tmp, word)
    wstem(tmp, word.length) // h014 stub: owner interp-stem.cpp stem tables (batch-6)
    return mumps.interp.cstringAt(tmp)
}

/** vendor 2301: mstring stem(mstring and word) - same shape, mstring carrier. */
fun stem(word: Mstring): Mstring {
    val tmp = CharArray(STR_MAX)
    copyInto(tmp, word.value)
    wstem(tmp, strlenC(tmp)) // vendor: strlen(tmp), NOT word.length() (kept)
    return Mstring(mumps.interp.cstringAt(tmp))
}

/** vendor 2309: char-star stem(char-star word) - in-place wstem; returns word. */
fun stem(word: CharArray): CharArray {
    wstem(word, strlenC(word))
    return word
}

// -----------------------------------------------
//  Hash / lhash (vendor 2314-2352) - lhash resolves its MdhStubs stub
// -----------------------------------------------

/**
 * vendor 2314: char-star Hash(const char-star str) - the 4-byte sum
 * hash, result as a decimal string. UB quantized (see header): the
 * unsigned-long a has only its low 4 bytes written - upper bytes zero
 * here; the running sum keeps unsigned wraparound (ULong math, h010).
 */
fun Hash(str: String): String {
    val inb = CharArray(STR_MAX) // vendor: char in[STR_MAX] = {0}
    copyInto(inb, str)

    var x = 0UL
    var i = 0
    while (i < inb.size && inb[i] != mumps.interp.NUL) {
        val b0 = (inb[i].code and 0xFF).toULong()
        val b1 = if (i + 1 < inb.size) (inb[i + 1].code and 0xFF).toULong() else 0u
        val b2 = if (i + 2 < inb.size) (inb[i + 2].code and 0xFF).toULong() else 0u
        val b3 = if (i + 3 < inb.size) (inb[i + 3].code and 0xFF).toULong() else 0u
        val a = b0 or (b1 shl 8) or (b2 shl 16) or (b3 shl 24)
        x = x + a
        i += 4
    }

    return x.toString()
}

/**
 * vendor 2335: unsigned long lhash(const char-star str) - same 4-byte
 * sum, returning the raw value. Resolves the B1 unit-31 stub; the Long
 * stub signature is kept so the unit-31 Hash-class call sites bind
 * unchanged (unsigned domain folded to Long, journaled).
 */
fun lhash(str: String): Long {
    val inb = CharArray(STR_MAX)
    copyInto(inb, str)

    var x = 0UL
    var i = 0
    while (i < inb.size && inb[i] != mumps.interp.NUL) {
        val b0 = (inb[i].code and 0xFF).toULong()
        val b1 = if (i + 1 < inb.size) (inb[i + 1].code and 0xFF).toULong() else 0u
        val b2 = if (i + 2 < inb.size) (inb[i + 2].code and 0xFF).toULong() else 0u
        val b3 = if (i + 3 < inb.size) (inb[i + 3].code and 0xFF).toULong() else 0u
        val a = b0 or (b1 shl 8) or (b2 shl 16) or (b3 shl 24)
        x = x + a
        i += 4
    }

    return x.toLong()
}

// -----------------------------------------------
//  operator*(string) (vendor 2354)
// -----------------------------------------------

/**
 * vendor 2354: indirect operator*(string a) - the prefix-star
 * conversion helper building an indirect reference. Kotlin has no
 * prefix-star operator overload point; rendered as the named helper
 * indirectOf (h015 adaptation, journaled).
 */
fun indirectOf(a: String): Indirect {
    val b = Indirect()
    b.reference = a
    return b
}

// -----------------------------------------------
//  GlobalSet (vendor 2365-2461)
// -----------------------------------------------

/**
 * vendor 2365-2461: int GlobalSet(...) x7 - builds "s x=\"y\"" and
 * xecutes it; tpx 1 on rc==0 else 0. All seven overloads fold to the
 * (String, String) signature (h015 - char-star/string/mstring collapse
 * after the String carrier; the Sw.kt wrapper convention). The vendor
 * stray lone-backslash line after the strcat block is dropped (see
 * header, PN-1).
 */
fun GlobalSet(x: String, y: String): Int {
    val tmp = "s " + x + "=\"" + y + "\""

    if (xecute(tmp) == 0) MdhState.svPtr.tpx = 1
    else MdhState.svPtr.tpx = 0

    return MdhState.svPtr.tpx.toInt()
}

// -----------------------------------------------
//  GlobalGet (vendor 2463-2509)
// -----------------------------------------------

/**
 * vendor 2463: char-star GlobalGet(char-star a) - "set percent=a" then
 * SymGet of the percent variable. VENDOR BUG kept: on the xecute
 * failure path the vendor strcpy's an empty string into r but then
 * UNCONDITIONALLY overwrites r with SymGet(percent) - the return value
 * equals SymGet on both paths; the dead store is dropped (h013) with
 * the observable preserved. tpx side effect kept.
 */
fun GlobalGet(a: String): String {
    val tmp = "set %=" + a

    if (xecute(tmp) == 0) MdhState.svPtr.tpx = 1
    else MdhState.svPtr.tpx = 0

    return SymGet("%").value
}

/**
 * vendor 2480: mstring GlobalGet(mstring a) - returns the SymGet value
 * on success, the empty mstring on failure (genuinely different from
 * the char-star overload - kept separate). The vendor 2497 std::string
 * overload folds here (h015; its static-string lifetime divergence is
 * documented: the Kotlin render has no cross-call static).
 */
fun GlobalGet(a: Mstring): Mstring {
    val tmp = "set %=" + a.value
    val rr = Mstring()

    if (xecute(tmp) == 0) {
        MdhState.svPtr.tpx = 1
        rr.value = SymGet("%").value
    } else {
        MdhState.svPtr.tpx = 0
        rr.value = ""
    }

    return rr
}

// -----------------------------------------------
//  pmw (vendor 2656-2675)
// -----------------------------------------------

/**
 * vendor 2656-2675: int pmw(...) x5 - pattern-match wrappers over pm.
 * All five overloads fold to (String, String, Msv) (h015); the h014
 * stub names interp-pm.cpp:43 as the pm owner (not a corpus unit).
 */
fun pmw(a: String, b: String, svPtr: mumps.interp.Msv): Int =
    pm(toCbuf(a), toCbuf(b), svPtr)
