package daf

import kotlin.system.exitProcess

// Unit 154 (DAF B5 T3, complex): libmpscpp.cpp::<misc-5> (719 raw /
// 600 phys LOC): the C++ wrapper-library residue mid-file: the
// mstring operator<= / operator== / operator!= overload sets, the
// thrownMessage/__finish exception covers, the global::Int/
// Double/Mstring/Char read members, the Stem/TokenInit/Token/
// Dump/Restore service families, the mdh_* GTK wrapper family (row
// 35 external GUI types), Getenv, ZSeek/Ztell, the Perl passthrough
// stubs, the global::DocCorrelate member relays, the pt debug print,
// global::Count, global::Transpose + the two free Transpose
// overloads, the stem trio, Hash/lhash, the indirect operator*
// fold, and the GlobalSet/GlobalGet/pmw overload sets.
// CHUNK-BOUNDARY (BASE-log row 83 hit-adapt, the B4 misc-9
// adjudication style): the dump opens mid-body - the leading
// "else return false; }" completes the previous chunk's last
// NUMERIC operator<= body (the <int)/<(long)/<(double)/<(float)/
// <(char*)/<(string) set lives in the preceding chunk); the render
// starts at the first complete body, operator<=(global), and the
// missing leading set is ledged as chunk-cut residue of the
// neighboring unit. Likewise >= belongs to the preceding chunk (the
// B4 operator</operator> ledger of unit 120 is the sibling
// precedent) and is NOT re-rendered here. The dump CLOSES mid-body
// of mstring mcvt(char*) and the sin dump that follows it - mcvt
// renders as a named-owner stub below, carry-over holder of roster
// row 187 (<misc-6>, batch 7; the row-83 cut disclosed).
// Consults: rows 98/99 hit-reuse (the == and != overload sets
// collapse onto the numeric atol/atof families and the
// lexicographic String family: (const char*)/(char*)/(string) are
// one String seat; the global operand reads through the node value
// seat; the mstring operand keeps the dedicated M-suffixed seat),
// row 28 hit-reuse (atolD of LibmpscppMisc9), row 18 hit-adapt (the
// global::Int/Double members ride the operator>> read family whose
// bodies are outside this dump: numeric conversion folded onto
// atol/atof DOCUMENTED DIVERGENCE, ledger per seat), row 92
// hit-reuse (thrownMessage reads GlobalExceptionCodeD with the
// pinned codes INVALID_CONVERSION 1 / GLOBAL_NOT_FOUND 2 /
// INPUT_LENGTH 10 of libmpscpp.h:70-79), row 56 hit-adapt
// (Hash's static char result[32] return folds onto a shared module
// String seat with the shared-buffer hazard ledgered; the %
// formatting divergence for unsigned long below), row 111/106
// hit-reuse (the 4-byte little-endian ULong accumulation of Hash/
// lhash - chars accumulate b0 + b1*256 + b2*65536 + b3*2^24 with the
// zero-padded char reads past NUL riding the zero-initialized
// carrier), rows 6/22 hit-reuse (the TU svPtr singleton), row 59
// hit-adapt (the mdh_* wrappers relay the GTK C library: named
// gtk seat stubs with external-owner discipline - no GTK on the
// JVM; the dialog_new_with_buttons build is the z-call shape of
// the MdhTreeLevelAdd precedent with the %p handle fold of unit
// 126-128), row 60 hit-adapt (Getenv -> System.getenv DOCUMENTED
// DIVERGENCE; ZSeek/Ztell ride the FILE* position API the JVM
// Reader lacks - named stubs with the fpos divergence ledger),
// row 90 hit-reuse (pmw relays the interp-pm.cpp::pm body - not a
// corpus unit; the pmD named stub), row 98 hit-reuse (the GlobalSet
// x7 / GlobalGet x3 / Dump x3 / Restore x3 overload sets collapse
// onto the String seats), row 83 hit-adapt (the vendor stray
// backslash token line inside every GlobalSet body (a lone "\"
// after the strcat chain, vendor source ~line 567) is kept
// comment-only - it is not a C statement), row 92 hit-adapt (the
// indirect operator*(string a) fold: the indirect class has no
// other seat in the DAF corpus - minimal carrier class + creator).
// Seat releases: the docCorrelateD member seat raised by unit 120
// (LibmpscppMisc9.kt) is RELEASED here (its owner, the
// global::DocCorrelate member char*/mstring pair, renders below);
// the docCorrelate free-function relay becomes the named stub
// docCorrelateFreeD.
// Quirk ledger (kept verbatim): thrownMessage's vendor typo
// "detetected"; __finish's static flg latches _exit(0) on re-entry
// after GlobalClose+exit(0) (Kotlin exitProcess fold, GC no-op
// ledger); global::Count's tmp dual-role (XNEXT walk reg and
// strncmp ref prefix); global::Transposes B().Kill() before the
// transposition; TokenInit reads its result from svPtr->bd while
// Token reads tmp2; Perl's perlcpp call is COMMENTED OUT in the
// vendor (kept verbatim - the body only returns tpx); the Ztell
// sprintf passes &offset (a vendor Address-of quirk, kept as the
// offset value itself in the seat).
// Dafny: EXEMPT (wrapper/residue cluster over stub and node seats;
// the unit-120 exemption family).

// ---- pinned exception codes (libmpscpp.h:70-79; GlobalLength/
// Getstr1 raised the other two) ----
const val INVALID_CONVERSION_D = 1 // #define INVALID_CONVERSION 1

// ---- operator<=(global) / operator<=(mstring): the two complete
// <= bodies of this chunk (the numeric tail of the set lives in the
// preceding chunk; row 83 ledger above) ----

fun mstringLeD(a: String, i: MpsGlobalNode): Boolean = // operator<=(global)
    a <= nodeValueE5D(i) // string t1; i >> t1; data <= t1

fun mstringLeM(a: String, i: String): Boolean = // operator<=(mstring)
    a <= i // data <= i.data

// ---- operator== family (rows 98/99 collapse) ----

fun mstringEqD(a: String, i: Int): Boolean = atolD(a) == i.toLong()
fun mstringEqD(a: String, i: Long): Boolean = atolD(a) == i
fun mstringEqD(a: String, i: Double): Boolean = atofD(a) == i
fun mstringEqD(a: String, i: Float): Boolean = atofD(a) == i.toDouble()
fun mstringEqD(a: String, i: CharArray): Boolean = // (char*/const char*)
    a == DafCRuntime.cstring(i)
fun mstringEqD(a: String, i: String): Boolean = a == i // (string)
fun mstringEqD(a: String, i: MpsGlobalNode): Boolean = // (global)
    a == nodeValueE5D(i)
fun mstringEqM(a: String, i: String): Boolean = a == i // (mstring)

// ---- operator!= family (rows 98/99 collapse) ----

fun mstringNeD(a: String, i: Int): Boolean = atolD(a) != i.toLong()
fun mstringNeD(a: String, i: Long): Boolean = atolD(a) != i
fun mstringNeD(a: String, i: Double): Boolean = atofD(a) != i
fun mstringNeD(a: String, i: Float): Boolean = atofD(a) != i.toDouble()
fun mstringNeD(a: String, i: CharArray): Boolean = // (char*/const char*)
    a != DafCRuntime.cstring(i)
fun mstringNeD(a: String, i: String): Boolean = a != i
fun mstringNeD(a: String, i: MpsGlobalNode): Boolean =
    a != nodeValueE5D(i)
fun mstringNeM(a: String, i: String): Boolean = a != i

// ---- BUILTIN FUNCTIONS ----

// void thrownMessage() - the uncaught-exception cover print
// (vendor typo "detetected" kept verbatim).
fun thrownMessageD() {
    print("Mumps has detetected an uncaught exception: \n")
    if (GlobalExceptionCodeD == INVALID_CONVERSION_D)
        print("Invalid conversion from global to numeric\n")
    if (GlobalExceptionCodeD == GLOBAL_NOT_FOUND_D)
        print("Global not found\n")
    if (GlobalExceptionCodeD == INPUT_LENGTH_D)
        print("Input Line Too Long\n")
}

// __finish()s file-scope static int flg latch.
private var finishFlgE5D = 0

// void __finish() - the terminate cover: latched _exit(0) on
// re-entry. exit()/_exit() fold onto exitProcess (GC finalizers
// absent - divergence ledger above).
fun finishD() {
    if (finishFlgE5D != 0) { // if (flg) _exit(0)
        exitProcess(0)
    }
    MglobalDaf(CLOSE_D, // GlobalClose (= Mglobal(CLOSE,"","",sv))
        CharArray(0), CharArray(0), DafInterpState.svPtr)
    finishFlgE5D = 1 // flg = 1
    exitProcess(0) // exit(0)
}

// ---- global read members (row 18: the operator>> read family is
// outside this dump; numeric folds atol/atof DOCUMENTED
// DIVERGENCE) ----

private fun nodeValueE5D(n: MpsGlobalNode): String =
    n.value // operator>> reads the node value (row 54 node-model)

fun globalIntD(n: MpsGlobalNode): Int = // int global::Int()
    atolD(nodeValueE5D(n)).toInt()

fun globalDoubleD(n: MpsGlobalNode): Double = // double global::Double()
    atofD(nodeValueE5D(n))

fun globalMstringD(n: MpsGlobalNode): String = // mstring global::Mstring()
    nodeValueE5D(n)

// char * global::Char(char * i, int max): (*this)>>
// tmp then strncpy(i,tmp,max).
fun globalCharD(n: MpsGlobalNode, i: CharArray, max: Int): CharArray {
    val tmp = CharArray(STR_MAX_DAF)
    copyE5D(tmp, nodeValueE5D(n)) // char tmp[STR_MAX]; (*this)>>tmp
    var k = 0
    while (k < i.size && k < tmp.size && k < max && tmp[k] != '\u0000') {
        i[k] = tmp[k] // strncpy(i, tmp, max)
        k++
    }
    i[k.coerceAtMost(i.size - 1)] = '\u0000'
    return i
}

// ---- Stem/TokenInit/Token/Dump/Restore service families ----

// int wstem(char *, int) - the stemming entry (interp-stem.cpp:
// family; InterpBifsMisc keeps it prototype-only) - the MUMPS
// runtime sibling of the shred seats.
// wstemD SEAT RELEASED (row 292, batch 10): the real body lands
// at InterpStemWstem.kt (roster row 292, interp-stem.cpp::wstem; the
// stub body is deleted with this release note).

// mstring Stem(mstring &arg): wstem over the arg, readback fold
// (rows 101/6: the buffer plumb is str strcpy -> wstem -> mstring).
fun StemD(arg: String): String {
    val tmp = CharArray(STR_MAX_DAF)
    copyE5D(tmp, arg)
    wstemD(tmp, lenE5D(tmp)) // wstem((char*)tmp, strlen(tmp))
    return DafCRuntime.cstring(tmp) // tmp3 = tmp
}

// mstring TokenInit(mstring &arg): the " \$zwi\u00CE" open + arg +
// "\u00CF" close z-call relay (row 80 coded splits kept); the
// result reads svPtr->bd (kept verbatim).
fun TokenInitD(arg: String): String {
    val msg = " \$zwi\u00CE" + arg + "\u00CF"
    val tmp2 = CharArray(STR_MAX_DAF)
    zfcnRelayD(msg, tmp2, DafInterpState.svPtr)
    return DafCRuntime.cstring(DafInterpState.svPtr.bd) // rslt = svPtr->bd
}

// mstring Token(): the " \$zwp" poll; result reads tmp2.
fun TokenD(): String {
    val msg = " \$zwp"
    val tmp2 = CharArray(STR_MAX_DAF)
    zfcnRelayD(msg, tmp2, DafInterpState.svPtr)
    return DafCRuntime.cstring(tmp2) // rslt = tmp2
}

// void Dump(...) x3 (mstring/string/char*): the "\$zcd\u00CE" +
// arg + "\u00CF" relay; rows 98/99 collapse onto the String seat
// with the CharArray variant kept for the char* call shape.
fun DumpD(arg: String) {
    val tmp2 = CharArray(STR_MAX_DAF)
    zfcnRelayD("\$zcd\u00CE" + arg + "\u00CF", tmp2, DafInterpState.svPtr)
}
fun DumpD(arg: CharArray) = DumpD(DafCRuntime.cstring(arg))

// void Restore(...) x3: same shape with "\$zcl\u00CE".
fun RestoreD(arg: String) {
    val tmp2 = CharArray(STR_MAX_DAF)
    zfcnRelayD("\$zcl\u00CE" + arg + "\u00CF", tmp2, DafInterpState.svPtr)
}
fun RestoreD(arg: CharArray) = RestoreD(DafCRuntime.cstring(arg))

// ---- mdh_* GTK wrapper family (row 35: external GUI types fold
// onto opaque Int handles; the GTK C library relays are named-
// owner stubs; the dialog build follows the MdhTreeLevelAdd z-call
// precedent with the %p handle fold) ----

// int mdh_dialog_new_with_buttons(GtkWindow *win, char *text)
fun mdhDialogNewWithButtonsD(win: Int, text: String): Int {
    // strcpy(&tmp[1],"\$z~mdh~dialog~new~with~buttons") + "\xce" +
    // sprintf(tmp1,"%p",win) + "\xd0" + text + "\xcf"
    val msg = " \$z~mdh~dialog~new~with~buttons\u00CE" +
        fmtPtrE5D(win) + "\u00D0" + text + "\u00CF"
    val tmp1 = CharArray(STR_MAX_DAF)
    zfcnRelayD(msg, tmp1, DafInterpState.svPtr) // tmp[0] caret bypass
    return if (tmp1[0] == '0') 0 else 1 // if (tmp1[0]=='0') return 0
}

// gtk C library relay seats (non-corpus external owners).
fun gtkToggleButtonGetActiveD(b: Int): Int =
    throw NotImplementedError(
        "gtk_toggle_button_get_active: external C library (GTK); non-corpus owner")
fun gtkSwitchGetActiveD(b: Int): Int =
    throw NotImplementedError(
        "gtk_switch_get_active: external C library (GTK); non-corpus owner")
fun gtkToggleButtonSetActiveD(b: Int, v: Boolean) {
    throw NotImplementedError(
        "gtk_toggle_button_set_active: external C library (GTK); non-corpus owner")
}
fun gtkEntryGetTextD(e: Int): String =
    throw NotImplementedError(
        "gtk_entry_get_text: external C library (GTK); non-corpus owner")
fun gtkRangeGetAdjustmentValueD(e: Int): Double =
    throw NotImplementedError(
        "gtk_range_get_adjustment + gtk_adjustment_get_value: external C library (GTK); non-corpus owner")
fun gtkCalendarGetDateYmdD(e: Int): IntArray =
    throw NotImplementedError(
        "gtk_calendar_get_date: external C library (GTK); non-corpus owner")
fun gtkEntrySetTextD(e: Int, txt: String) {
    throw NotImplementedError(
        "gtk_entry_set_text: external C library (GTK); non-corpus owner")
}
fun gtkTextBufferSetTextD(t: Int, txt: String) {
    throw NotImplementedError(
        "gtk_text_buffer_set_text: external C library (GTK); non-corpus owner")
}
fun gtkFontChooserGetFontD(b: Int): String =
    throw NotImplementedError(
        "gtk_font_chooser_get_font: external C library (GTK); non-corpus owner")
fun gtkColorChooserGetRgbaD(c: Int): DoubleArray =
    throw NotImplementedError(
        "gtk_color_chooser_get_rgba: external C library (GTK); non-corpus owner")
fun gtkLabelSetTextD(l: Int, txt: String) {
    throw NotImplementedError(
        "gtk_label_set_text: external C library (GTK); non-corpus owner")
}
fun gtkWidgetHideD(w: Int) {
    throw NotImplementedError(
        "gtk_widget_hide: external C library (GTK); non-corpus owner")
}
fun gtkWidgetShowD(w: Int) {
    throw NotImplementedError(
        "gtk_widget_show: external C library (GTK); non-corpus owner")
}
fun gtkTreeSelectionGetSelectedD(t: Int, col: Int): String? =
    throw NotImplementedError(
        "gtk_tree_selection_get_selected + gtk_tree_model_get: external C library (GTK); non-corpus owner")
fun gtkTreeStoreClearD(t: Int) {
    throw NotImplementedError(
        "gtk_tree_store_clear: external C library (GTK); non-corpus owner")
}
fun gtkSpinButtonGetValueD(s: Int): Double =
    throw NotImplementedError(
        "gtk_spin_button_get_value: external C library (GTK); non-corpus owner")
fun gtkSpinButtonSetValueD(s: Int, v: Double) {
    throw NotImplementedError(
        "gtk_spin_button_set_value: external C library (GTK); non-corpus owner")
}

// int mdh_toggle_button_get_active(GtkToggleButton *b)
fun mdhToggleButtonGetActiveD(b: Int): Int = gtkToggleButtonGetActiveD(b)
// int mdh_switch_get_active(GtkSwitch *b)
fun mdhSwitchGetActiveD(b: Int): Int = gtkSwitchGetActiveD(b)
// void mdh_toggle_button_set_active(GtkToggleButton *b, int v)
fun mdhToggleButtonSetActiveD(b: Int, v: Int) {
    gtkToggleButtonSetActiveD(b, v != 0) // x = (v==0) ? FALSE : TRUE
}
// char * mdh_entry_get_text(GtkEntry *e, char *txt)
fun mdhEntryGetTextD(e: Int, txt: CharArray): CharArray {
    copyE5D(txt, gtkEntryGetTextD(e)) // strcpy(txt, gtk_entry_get_text)
    return txt
}
// double mdh_range_get_adjustment(GtkRange *e)
fun mdhRangeGetAdjustmentD(e: Int): Double = gtkRangeGetAdjustmentValueD(e)
// char * mdh_calendar_get_date(GtkCalendar *e): static tmp return
// folds onto a fresh String (row 56) with the yr/mn/dy %d pad.
fun mdhCalendarGetDateD(e: Int): String {
    val d = gtkCalendarGetDateYmdD(e) // guint mn,dy,yr out-params
    return "${d[0]} ${d[1]} ${d[2]}" // sprintf(tmp,"%d %d %d",yr,mn,dy)
}
// void mdh_entry_set_text(GtkEntry *e, char *txt)
fun mdhEntrySetTextD(e: Int, txt: String) = gtkEntrySetTextD(e, txt)
// void mdh_text_buffer_set_text(GtkTextBuffer *t, char *txt)
fun mdhTextBufferSetTextD(t: Int, txt: String) =
    gtkTextBufferSetTextD(t, txt)
// char * mdh_font_chooser_get_font(GtkFontChooser *b)
fun mdhFontChooserGetFontD(b: Int): String = gtkFontChooserGetFontD(b)
// char * mdh_color_chooser_get_rgba(GtkColorChooser *c): %f x4 pad;
// the Locale-independent fold rides String.format (row 33).
fun mdhColorChooserGetRgbaD(c: Int): String {
    val r = gtkColorChooserGetRgbaD(c)
    return String.format(java.util.Locale.ROOT,
        "%f %f %f %f", r[0], r[1], r[2], r[3])
}
// void mdh_label_set_text(GtkLabel *l, char *txt)
fun mdhLabelSetTextD(l: Int, txt: String) = gtkLabelSetTextD(l, txt)
// void mdh_widget_hide(GtkWidget *w)
fun mdhWidgetHideD(w: Int) = gtkWidgetHideD(w)
// void mdh_widget_show(GtkWidget *w)
fun mdhWidgetShowD(w: Int) = gtkWidgetShowD(w)
// char * mdh_tree_selection_get_selected(GtkTreeSelection *t,
//                                         int col, char *txt)
fun mdhTreeSelectionGetSelectedD(t: Int, col: Int, txt: CharArray):
    CharArray {
    copyE5D(txt, "") // strcpy(txt,"")
    val value = gtkTreeSelectionGetSelectedD(t, col)
    if (value == null) return txt // == FALSE: return txt
    copyE5D(txt, value) // strcpy(txt, value)
    return txt
}
// void mdh_tree_store_clear(GtkTreeStore *t)
fun mdhTreeStoreClearD(t: Int) = gtkTreeStoreClearD(t)
// double mdh_spin_button_get_value(GtkSpinButton *s)
fun mdhSpinButtonGetValueD(s: Int): Double = gtkSpinButtonGetValueD(s)
// void mdh_spin_button_set_value(GtkSpinButton *s, double v)
fun mdhSpinButtonSetValueD(s: Int, v: Double) = gtkSpinButtonSetValueD(s, v)

// ---- Getenv / ZSeek / Ztell / Perl ----

// void Getenv(unsigned char *out, unsigned char *in)
fun GetenvD(out: CharArray, in_: String) {
    val p1 = System.getenv(in_) // getenv DOCUMENTED DIVERGENCE (row 60)
    if (p1 == null) { // if (p1 == NULL)
        copyE5D(out, "") // strcpy(out,"")
        return
    }
    copyE5D(out, p1)
}

// FILE *fpos seats (row 60 divergence named stubs): the JVM Reader
// has no fpos_t; the named-owner stubs with fsetpos/fgetpos owners.
fun zseekD(u: Int, s: String): Boolean {
    throw NotImplementedError(
        "ZSeek: out-of-scope fsetpos seat (FILE* position API; JVM divergence)")
}
fun ztellD(u: Int, s: StringBuilder): Boolean {
    throw NotImplementedError(
        "Ztell: out-of-scope fgetpos seat (FILE* position API; JVM divergence)")
}

// int Perl(mstring &a, mstring &b) / (mstring &, char *b): the
// perlcpp calls are COMMENTED OUT in the vendor (kept verbatim);
// the body only returns the file-scope tpx.
fun perlD(a: String, b: String): Int {
    return DafInterpState.svPtr.tpx
}

// ---- global::DocCorrelate member pair (char* / mstring fcn):
// real bodies; both relay to the free DocCorrelate(B,s,threshold)
// (named stub below). VENDOR QUIRK: the free call passes only
// (B, s, threshold) - the member receiver drops out of the call
// shape (kept verbatim; ledger). ----

// docCorrelateFreeD SEAT RELEASED (row 287, batch 10): the real
// body lands at LibmpscppDocCorrelate.kt (roster row 287,
// libmpscpp.cpp::global::DocCorrelate string-threshold member; the
// stub body is deleted with this release note).

// void global::DocCorrelate(global B, char *fcn, double threshold)
fun docCorrelateD(a: MpsGlobalNode, b: MpsGlobalNode, fcn: String,
    threshold: Double) { // (seat release: unit 120 raised the stub)
    // string s; s = fcn;
    docCorrelateFreeD(b, fcn, threshold) // return DocCorrelate(B,s,thr)
}
// void global::DocCorrelate(global B, mstring fcn, double
// threshold): the mstring fcn seat folds onto the same String body
// (row 101 mstring-String collapse; s = fcn.s_str()) - not a
// separate Kotlin overload.

// ---- pt / global::Count / Transpose / stem / Hash ----

// void pt(char *t, char *x) - debug print (vendor iterates from
// UNINITIALIZED i: kept shape with the i=0 guard ledged).
fun ptD(t: String, x: CharArray) {
    print("$t  ")
    var i = 0
    while (i < x.size && x[i] != '\u0000') { // while (x[i])
        print(" ${x[i]} ")
        i++
    }
    print("\n")
}

// long global::Count(): the XNEXT walk with the tmp dual-role
// (walk register AND strncmp prefix against ref).
fun globalCountD(n: MpsGlobalNode, sv: DafMsv): Long {
    val ttt = CharArray(STR_MAX_DAF)
    val tmp = CharArray(STR_MAX_DAF)
    copyE5D(tmp, n.ref) // strcpy(tmp, ref)
    var i = lenE5D(tmp) // i = strlen(tmp)
    // if (tmp[i-1]<32 && tmp[i-2]<32) tmp[--i]=0;
    if (i >= 2 && tmp[i - 1] < ' ' && tmp[i - 2] < ' ') {
        i--
        tmp[i] = '\u0000'
    }
    var j = 0L // long j = 0
    while (true) {
        if (MglobalDaf(XNEXT_D, tmp, ttt, sv) == 0) break // XNEXT==0
        if (!startsWithE5D(tmp, n.ref, i)) break // strncmp(ref,tmp,i)
        j++
    }
    return j
}

// void global::Transpose(global B)
fun globalTransposeD(a: MpsGlobalNode, b: MpsGlobalNode, sv: DafMsv) {
    b.invoke().kill() // B().Kill()
    val i = a
    var aa = ""
    while (true) {
        aa = i.invoke(aa).order(1) // aa = (*this)(aa).Order(1)
        if (aa == "") break
        var bb = ""
        while (true) {
            bb = i.invoke(aa, bb).order(1) // (*this)(aa,bb).Order(1)
            if (bb == "") break
            b.invoke(bb, aa).value = i.invoke(aa, bb).value // B(bb,aa)
        }
    }
}

// void Transpose(char *a, char *b)
fun TransposeD(a: String, b: String, sv: DafMsv) {
    val A = MpsGlobalNode(a) // global A(a), B(b)
    val B = MpsGlobalNode(b)
    var aa = ""
    while (true) {
        aa = A.invoke(aa).order(1)
        if (aa == "") break
        var bb = ""
        while (true) {
            bb = A.invoke(aa, bb).order(1)
            if (bb == "") break
            B.invoke(bb, aa).value = A.invoke(aa, bb).value
        }
    }
}
// void Transpose(mstring a, mstring b): same body over the
// c_str()-folded refs (rows 98/99 collapse; kept a separate seat
// because the vendor declares it separately).
fun TransposeM(a: String, b: String, sv: DafMsv) = TransposeD(a, b, sv)

// string stem(string &word) / mstring stem(mstring &word): the
// by-reference mutation folds onto the return value (row 101
// DOCUMENTED DIVERGENCE: Kotlin strings are immutable; the caller
// rebinds on the returned stem).
fun stemD(word: String): String {
    val tmp = CharArray(STR_MAX_DAF) // static char tmp[STR_MAX]
    copyE5D(tmp, word)
    wstemD(tmp, word.length) // (int)word.length()
    return DafCRuntime.cstring(tmp) // word = tmp; return word
}
// char* stem(char* word): in-place carry, the true mutable seat.
fun stemD(word: CharArray): CharArray {
    wstemD(word, lenE5D(word)) // (int)strlen(word)
    return word
}

// Hash's static char result[32] return: shared module seat with
// the row-56 hazard ledger (successive calls clobber).
private var hashResultE5D: String = ""

// char * Hash(const char *str): 4-byte little-endian accumulation
// into an unsigned long (rows 111/106; %lu sprintf divergence:
// Kotlin ULong.toString renders the same digits for the decimal %lu).
fun HashD(str: String): String {
    val inn = CharArray(STR_MAX_DAF) // char in[STR_MAX] = {0}
    copyE5D(inn, str)
    var x = 0UL // unsigned long a, x = 0
    var i = 0
    while (inn[i] != '\u0000') { // for (i=0; in[i]!=0; i+=4)
        var a = 0UL
        // zero-padded reads past NUL (row 111 guard: reading past
        // the terminator yields 0x00 as in the zero-init carrier)
        a += inn[i].code.toULong()                    // *p1 = in[i]
        a += (inn.getOrNull(i + 1)?.code?.toULong() ?: 0UL) shl 8 // *(p1+1)
        a += (inn.getOrNull(i + 2)?.code?.toULong() ?: 0UL) shl 16 // *(p1+2)
        a += (inn.getOrNull(i + 3)?.code?.toULong() ?: 0UL) shl 24 // *(p1+3)
        x += a // x = x + a
        i += 4
    }
    hashResultE5D = x.toString() // sprintf(result,"%lu",x)
    return hashResultE5D
}

// unsigned long lhash(const char *str): same accumulation, raw.
fun lhashD(str: String): ULong {
    val inn = CharArray(STR_MAX_DAF)
    copyE5D(inn, str)
    var x = 0UL
    var i = 0
    while (inn[i] != '\u0000') {
        var a = 0UL
        a += inn[i].code.toULong()
        a += (inn.getOrNull(i + 1)?.code?.toULong() ?: 0UL) shl 8
        a += (inn.getOrNull(i + 2)?.code?.toULong() ?: 0UL) shl 16
        a += (inn.getOrNull(i + 3)?.code?.toULong() ?: 0UL) shl 24
        x += a
        i += 4
    }
    return x
}

// ---- indirect operator*(string a): minimal carrier fold (the
// indirect class has no other DAF seat; rows 18/37) ----
class DafIndirect(var reference: String)

fun indirectD(a: String): DafIndirect {
    val b = DafIndirect(a) // static indirect b; b.reference = a
    return b
}

// ---- GlobalSet (x7 -> strings collapse) / GlobalGet (x3) ----

// int GlobalSet(x,y): builds "s x=\"y\"" and XECUTEs it. VENDOR
// QUIRK: every GlobalSet body carries a lone stray backslash line
// after the strcat chain (vendor source ~l.567; not a C statement -
// comment-only fold kept below verbatim). xecuteDaf relays
// (GlobalQS seat); the file-scope tpx latch is kept exact.
fun GlobalSetD(x: String, y: String): Int {
    val tmp = "s $x=\"$y\"" // char tmp[STR_MAX]="s "; strcat chain
    // \\
    if (xecuteDaf(tmp) == 0) DafInterpState.svPtr.tpx = 1
    else DafInterpState.svPtr.tpx = 0
    return DafInterpState.svPtr.tpx
}
// overloads collapsed onto the String seats (rows 98/99):
// (mstring,mstring),(char*,mstring),(mstring,char*),
// (string,string),(char*,string),(string,char*),(char*,char*)
fun GlobalSetD(x: CharArray, y: String): Int =
    GlobalSetD(DafCRuntime.cstring(x), y)
fun GlobalSetD(x: String, y: CharArray): Int =
    GlobalSetD(x, DafCRuntime.cstring(y))
fun GlobalSetD(x: CharArray, y: CharArray): Int =
    GlobalSetD(DafCRuntime.cstring(x), DafCRuntime.cstring(y))

// static char r[STR_MAX] of the char*-form GlobalGet (row 56
// shared-buffer hazard ledger as for Hash).
private var globalGetSharedE5D: String = ""

// char * GlobalGet(char *a)
fun GlobalGetD(a: String): String {
    val tmp = "set %=$a"
    if (xecuteDaf(tmp) == 0) {
        DafInterpState.svPtr.tpx = 1
    } else {
        DafInterpState.svPtr.tpx = 0
        globalGetSharedE5D = "" // strcpy(r,"")
    }
    globalGetSharedE5D = SymGetD("%") // t = SymGet("%"); strcpy(r,t)
    return globalGetSharedE5D
}
// mstring GlobalGet(mstring a)
fun GlobalGetM(a: String): String {
    val tmp = "set %=$a"
    val rr: String
    if (xecuteDaf(tmp) == 0) {
        DafInterpState.svPtr.tpx = 1
        rr = SymGetD("%")
    } else {
        DafInterpState.svPtr.tpx = 0
        rr = ""
    }
    return rr
}
// string GlobalGet(string a): the static rr of the vendor
// (successive-call clobber hazard ledger, row 56).
private var globalGetSSharedE5D: String = ""
fun GlobalGetS(a: String): String {
    val tmp = "set %=$a"
    if (xecuteDaf(tmp) == 0) {
        DafInterpState.svPtr.tpx = 1
        globalGetSSharedE5D = SymGetD("%")
    } else {
        DafInterpState.svPtr.tpx = 0
        globalGetSSharedE5D = ""
    }
    return globalGetSSharedE5D
}

// ---- pmw x5 -> pmD relay ----

// int pm(...) - the body lives at interp-pm.cpp:43 (not a corpus
// unit): named stub (row 37/90).
fun pmD(a: String, b: String, svPtr: DafMsv): Int =
    throw NotImplementedError(
        "pm: out-of-scope owner (interp-pm.cpp::pm, l.43; not a corpus unit)")

// int pmw(char *a, char *b, struct MSV *): the runtime pattern-match
// wrapper family (rows 98/99 collapse onto the String seats).
fun pmwD(a: String, b: String, svPtr: DafMsv): Int = pmD(a, b, svPtr)
fun pmwD(a: CharArray, b: String, svPtr: DafMsv): Int =
    pmD(DafCRuntime.cstring(a), b, svPtr)

// ---- mcvt: CHUNK-BOUNDARY CARRY (row 83): the dump closes inside
// "mstring mcvt (char * i) { mstring tmp;" - the body belongs to
// <misc-6> (roster row 187, batch 7, outside the DAF batch cycle);
// named-owner stub. ----
fun mcvtD(i: String): String =
    throw NotImplementedError(
        "mcvt: chunk-boundary carry-over owner (libmpscpp.cpp::<misc-6>, roster row 187, batch 7)")

// ---------------------------------------------------------------------
// unit-local helper seats (row 8 buffer-role folds; private per-file)
// ---------------------------------------------------------------------

// strcpy analog over the NUL-terminated CharArray seat.
private fun copyE5D(dst: CharArray, s: String) {
    var i = 0
    val n = dst.size - 1
    while (i < s.length && i < n) {
        dst[i] = s[i]
        i++
    }
    dst[i] = '\u0000'
}

// strlen analog.
private fun lenE5D(buf: CharArray): Int {
    var i = 0
    while (i < buf.size && buf[i] != '\u0000') i++
    return i
}

// strncmp prefix probe: tmp starts with ref's first i chars.
private fun startsWithE5D(buf: CharArray, ref: String, n: Int):
    Boolean {
    for (k in 0 until n) {
        if (k >= buf.size || k >= ref.length) return k >= ref.length
        if (buf[k] != ref[k]) return false
    }
    return true
}

// sprintf("%p", win) fold (unit 126-128 precedent: opaque Int
// handle prints in the C %p hex shape).
private fun fmtPtrE5D(handle: Int): String =
    "0x" + Integer.toHexString(handle)

// zfcn relay seat (owner: interp-zfcns.cpp::zfcn, roster b6; the
// MdhTreeLevelAdd private-stub convention).
private fun zfcnRelayD(msg: String, out: CharArray, sv: DafMsv): Unit =
    throw NotImplementedError(
        "zfcn: out-of-scope owner (interp-zfcns.cpp::zfcn, roster b6)")
