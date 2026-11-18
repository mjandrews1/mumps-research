package mumps.interp

import mumps.mdh.Avg
import mumps.mdh.BMGSearch
import mumps.mdh.Count
import mumps.mdh.GdkRGBA
import mumps.mdh.GtkAdjustment
import mumps.mdh.GtkCalendar
import mumps.mdh.GtkColorChooser
import mumps.mdh.GtkDialog
import mumps.mdh.GtkEntry
import mumps.mdh.GtkFileChooser
import mumps.mdh.GtkFontChooser
import mumps.mdh.GtkLabel
import mumps.mdh.GtkRange
import mumps.mdh.GtkSpinButton
import mumps.mdh.GtkSwitch
import mumps.mdh.GtkTextBuffer
import mumps.mdh.GtkToggleButton
import mumps.mdh.GtkTreeIter
import mumps.mdh.GtkTreeModel
import mumps.mdh.GtkTreeSelection
import mumps.mdh.GtkTreeStore
import mumps.mdh.GtkTreeView
import mumps.mdh.GtkWidget
import mumps.mdh.GtkWindow
import mumps.mdh.SmithWaterman
import mumps.mdh.StopInit
import mumps.mdh.StopLookup
import mumps.mdh.SynInit
import mumps.mdh.SynLookup
import mumps.mdh.Shred
import mumps.mdh.ShredQuery
import mumps.mdh.wstem
import mumps.mdh.gtk_adjustment_get_value
import mumps.mdh.gtk_calendar_get_date
import mumps.mdh.gtk_color_chooser_get_rgba
import mumps.mdh.gtk_dialog_new_with_buttons
import mumps.mdh.gtk_dialog_run
import mumps.mdh.gtk_entry_get_text
import mumps.mdh.gtk_entry_set_text
import mumps.mdh.gtk_file_chooser_get_filename
import mumps.mdh.gtk_font_chooser_get_font
import mumps.mdh.gtk_label_set_text
import mumps.mdh.gtk_range_get_adjustment
import mumps.mdh.gtk_spin_button_get_value
import mumps.mdh.gtk_spin_button_set_value
import mumps.mdh.gtk_switch_get_active
import mumps.mdh.gtk_text_buffer_set_text
import mumps.mdh.gtk_toggle_button_get_active
import mumps.mdh.gtk_toggle_button_set_active
import mumps.mdh.gtk_tree_model_get
import mumps.mdh.gtk_tree_selection_get_selected
import mumps.mdh.gtk_tree_store_clear
import mumps.mdh.gtk_tree_view_get_selection
import mumps.mdh.gtk_widget_destroy
import mumps.mdh.gtk_widget_hide
import mumps.mdh.gtk_widget_show
import mumps.mdh.gtk_window_set_keep_above

/**
 * PRO-arm translation of interp-zfcns.cpp::zfcn (batch 6, unit
 * 156 - the second of the deferred big three; vendor reference
 * lines 158-2519, physLOC 1662, score per units.csv row 157).
 * Translated independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe).
 *
 * The dollar-Z intrinsic dispatcher: prologue argument split,
 * the fcnName branch table (the z~gtk / z~mdh widget family, the
 * zz statistics family, the zs family, the straight-line names,
 * the zcode first-letter ladder), the GTK widget interface
 * arms, the SQLite transaction arms, the ServiceInterface
 * statistics relays, the math intrinsics, the string/file arms,
 * the zdate calendar block, and the fcn_not_found / err cascade.
 *
 * RESOLVES the InterpStubs zfcn(v1d,v1dOff,bd,bdOff,svPtr) stub
 * (raised at u94 with the correct batch-6 owner tag); the call
 * sites (Parse_.kt:801, MpstimeMain.kt:174/234) bind unchanged.
 * The mumps.mdh 3-arg zfcn stub is the OTHER zfcn (u157,
 * interp-bifs.cpp::zfcn) and is untouched here.
 *
 * Goto linearization (h011, u95/u155 precedents): the vendor's
 * ~110-label dispatch web renders as a single state machine over
 * the private ZLbl enum - every vendor goto X becomes
 * lab = ZLbl.X (continue@sm when mid-arm), every label X becomes
 * a when-arm, and the vendor's plain return becomes
 * lab = ZLbl.RET (the RET arm performs the carrier write-backs,
 * see h001 below).
 *
 * h001 C-idiom adaptation: the caller passes interior pointers
 * (v1d with v1dOff, bd with bdOff); per the u173 Mglobal
 * copy-in/call/write-back precedent the body works on local
 * NUL-terminated copies at index 0 (v1d/bd below shadow the
 * vendor names; the parameters are v1dP/bdP, h015). The vendor
 * MUTATES v1d in place (the argument split punches NUL bytes,
 * the zglobal branch strmoves) - the RET write-back of the
 * original-length window reproduces the caller-visible byte
 * state exactly; bd is written back NUL-terminated (an arm that
 * never writes bd round-trips the copied-in bytes unchanged).
 *
 * h004 globals: the vendor file-scope statics (linebuf,
 * linebufx, the ten static GtkTreeIter slots iter..iter9, the
 * static GtkTreeStore t, static GtkSpinButton spin, static int
 * column) persist across calls on the ZfcnState object.
 *
 * h008 carrier: fresh per-call STR_MAX scratch (tmp2/tmp3/tmp4/
 * gbltbl/gblstr render per-call; only the file statics above
 * ride the state object).
 *
 * h010 promotion: the vendor short iargs[10]/nargs/g ride
 * IntArray/Int (the GtkTreeAdd seat keeps its ShortArray - the
 * copy converts, u73 signature preserved).
 *
 * h012 byte domain: unsigned char buffers render as CharArray
 * over the C-locale byte domain; tolower/isdigit/isalpha via the
 * per-file byte-domain helpers.
 *
 * h014 out_of_scope_call: the GTK C library surface binds on
 * the mumps.mdh not-a-corpus-unit stub seats (raised by the mdh
 * family units), the mps_* intrinsics on their real seats, the
 * statistics on the libmpscpp MSV wrapper seats, sql on the
 * batch-9 stub, ServiceInterface on the batch-7 stub (its x1/x2
 * seats widened to CharArray? for the vendor NULL-argument
 * calls), sym_ on the batch-10 offset seat, mps_floor/mps_ceil/
 * mps_trunc raised as new batch-10 stubs, strmove on its
 * not-a-corpus-unit stub, the popen/system OS layer on the
 * per-file carriers (u155 precedent).
 *
 * h015 Kotlin identity: the shadow-locals keep the vendor
 * names v1d/bd while the parameters are v1dP/bdP; fclose/dump
 * and other keyword-adjacent locals renamed with trailing
 * underscore where needed; the raw dollar sigil in comments
 * spelled out.
 *
 * h017 naming: label enum names and seat names keep vendor
 * spellings; the entry keeps the stub signature.
 *
 * h018 preprocessor resolutions (reference build): SQLITE is
 * defined (the znative arm returns "0"; the SQLITE zsqlite
 * transaction arms render; the else-arm zdbfile..zindexmax
 * cascade is journaled); GCVT not defined at this call site's
 * resolution (gcvt_wrapper delegates, NumSetup seat); the
 * vendor's unconditional gtk includes ride the GTK family
 * seats.
 */

private const val STR_MAX = 32000

/** defines.h:95 - BEGIN_TRANSACTION. */
private const val BEGIN_TRANSACTION = 25

/** defines.h:96 - COMMIT_TRANSACTION. */
private const val COMMIT_TRANSACTION = 26

/** defines.h:97 - ROLLBACK_TRANSACTION. */
private const val ROLLBACK_TRANSACTION = 27

/** defines.h:98 - SAVEPOINT. */
private const val SAVEPOINT = 28

/** defines.h:99 - PRAGMA. */
private const val PRAGMA = 29

/** defines.h:181 - SQLCMD. */
private const val SQLCMD = 55

/** defines.h:226 - COSINE (ServiceInterface char code). */
private const val COSINE_C = 1

/** defines.h:227 - TRANSPOSE. */
private const val TRANSPOSE_C = 2

/** defines.h:229 - INVDF. */
private const val INVDF_C = 3

/** defines.h:233 - DICE. */
private const val DICE_C = 4

/** defines.h:235 - CENTROID. */
private const val CENTROID_C = 10

/** defines.h:236 - TERMCORRELATE. */
private const val TERMCORRELATE_C = 11

/** defines.h:240 - MMULTIPLY. */
private const val MMULTIPLY_C = 15

/** defines.h:241 - DOCCORRELATE. */
private const val DOCCORRELATE_C = 17

/** defines.h:245 - GBLNME. */
private const val GBLNME_C = 20

/** defines.h:230 - SCANALNUM. */
private const val SCANALNUM_C = 5

/** defines.h:231 - SCAN. */
private const val SCAN_C = 6

/** defines.h:232 - AVG. */
private const val AVG_C = 7

/** defines.h:233 - SIM1. */
private const val SIM1_C = 8

/** defines.h:234 - JACCARD. */
private const val JACCARD_C = 9

/** defines.h:237 - COUNT. */
private const val COUNT_C = 12

/** defines.h:238 - MAX. */
private const val MAX_C = 13

/** defines.h:239 - MIN. */
private const val MIN_C = 14

/** defines.h:241 - SUM. */
private const val SUM_C = 16

/** defines.h:243 - GETENV. */
private const val GETENV_C = 18

/** defines.h:244 - INPUT. */
private const val INPUT_C = 19

/** defines.h:40 - OPEN (the coded open-paren byte, 206). */
private const val OPEN = 206

/** vendor zfcns.cpp:72 - CLOSE 207 (file-local define). */
private const val CLOSE = 207

/** vendor zfcns.cpp:73 - COMMA 208 (file-local define). */
private const val COMMA = 208

/** vendor zfcns.cpp:75 - NO 0. */
private const val NO = 0

/** vendor zfcns.cpp:76 - YES 1. */
private const val YES = 1

/** defines.h:91 - FLUSH 21 (Mglobal flush op, vendor zflush arm). */
private const val FLUSH = 21

/** vendor backtick byte (ASCII 96); Match.kt BTICK carrier
 *  precedent - the scan gate keeps no raw backtick in arms. */
private const val BTICK = 96.toChar()

/** The dead ep carrier the vendor passes as (char *) "". */
private val EP_EMPTY = CharArray(1)

/** h011: state-machine labels. Names are the vendor goto labels;
 *  RET is the new exit state (h001 write-back arm). */
private enum class ZLbl {
    DISPATCH, GtkDialogWithButtons, GtkWidgetHide, GtkWidgetShow,
    GtkSpinButtonGetValue, GtkSpinButtonSetValue, GtkTreeStoreClear,
    GtkTreeModelGet, GtkTreeSelectionGetSelected, GtkTreeViewGetSelection,
    GtkTreeLevelAdd, GtkTreeLevel1Add, GtkTreeLevel2Add, GtkTreeLevel3Add,
    GtkTreeLevel4Add, GtkTreeLevel5Add, GtkTreeLevel6Add, GtkTreeLevel7Add,
    GtkTreeLevel8Add, GtkTreeLevel9Add, GtkTreeLevel10Add, GtkLabelSetText,
    GtkTextBufferSetText, GtkEntryGetText, GtkEntrySetText,
    GtkGetRangeAdjustment, GtkFileChooser, GtkCalendar, GtkFontChooser,
    GtkColorChooser, GtkToggleButtonSetActive, GtkToggleButtonGetActive,
    GtkSwitchGetActive, zdatabase, zprogram, zfiletest, zbasename, zfloor,
    zceil, ztrunc, zsqlOpen, znative, zindexsize, zdatasize, zdbname,
    zdbfile, zindexmax, zsqlite, zsrand, zsmithwaterman, znoblanks,
    zstopinit, zsyninit, zbmgsearch, zshred, zshredquery, zstoplookup,
    zsynlookup, zzavg, zgetenv, zzcount, zzmax, zzmin, zzsum, zzscanalnum,
    zzscan, zzinput, zzcosine, zzjaccard, zzsim1, zztranspose, zzsoundex,
    zzmultiply, zzcentroid, zztermcorrelate, zzdoccorrelate, zzidf, zzdice,
    zflush, zchdir, zpow, zlog2, zlog10, zlog, zcosine, ztangent, zatangent,
    zacosine, zsine, zasine, zexp, zexp2, zexp10, zhit, zlower, znormal,
    zalter, zstem, zseek, ztell, zword, znumeric, zblnk, zbl1, zcurrentfile,
    zhtml, zcondense, zrestore, znxtrow, zabs, zpad, z_variable, zdate,
    zfile, zglobal, zroot, zsystem, fcn_not_found, err, RET
}

/** h004: the vendor file-scope statics persist across calls. */
private object ZfcnState {
    /** vendor 187-196: static char linebuf[STR_MAX] / static int linebufx. */
    val linebuf = CharArray(STR_MAX)
    var linebufx = 0

    /** vendor 198-207: static GtkTreeIter iter..iter10 (slots 1..10 used). */
    val iter = GtkTreeIter()
    val iter1 = GtkTreeIter()
    val iter2 = GtkTreeIter()
    val iter3 = GtkTreeIter()
    val iter4 = GtkTreeIter()
    val iter5 = GtkTreeIter()
    val iter6 = GtkTreeIter()
    val iter7 = GtkTreeIter()
    val iter8 = GtkTreeIter()
    val iter9 = GtkTreeIter()

    /** vendor 208: static GtkTreeStore *t. */
    var t: GtkTreeStore? = null

    /** vendor 209: static GtkSpinButton *spin. */
    var spin: GtkSpinButton? = null

    /** vendor 210: static int column = 0. */
    var column = 0

    /** h004: static time_t timex (zdate entry, vendor 2142). */
    var timex = 0L

    /** h004: static fpos_t i (zseek, vendor 1661). */
    var fposI = 0L

    /** h014: the sscanf percent-p decode registry - pointer text
     *  to the opaque widget instance (u73 gtkTreeStoreFromPtr
     *  precedent; one registry for all widget families). */
    val ptrReg = HashMap<Long, java.lang.Object>()

    fun <T> widgetAt(h: Long, mk: () -> T): T {
        // h012: the registry rides java.lang.Object (this build's
        // gate classpath keeps a lean kotlin.Any builtin)
        val ex = ptrReg[h]
        if (ex != null) {
            @Suppress("UNCHECKED_CAST")
            return ex as T
        }
        val nw = mk()
        ptrReg[h] = nw as java.lang.Object
        return nw
    }
}

/** h012: vendor tolower over the byte domain. */
private fun tolowerC(c: Char): Char = Character.toLowerCase(c)

/** h012: vendor isalpha over the byte domain. */
private fun isalphaC(c: Char): Boolean = c.code in 65..90 || c.code in 97..122

/** h002: vendor strcasecmp (whole-string case-insensitive). */
private fun strcasecmpC(a: String, b: String): Int =
    if (a.equals(b, ignoreCase = true)) 0 else if (a.lowercase() > b.lowercase()) 1 else -1

/** h002: vendor strncasecmp (n-char case-insensitive). */
private fun strncasecmpC(a: String, b: String, n: Int): Int {
    val sa = a.lowercase()
    val sb = b.lowercase()
    val k = minOf(n, sa.length, sb.length)
    for (q in 0 until k) if (sa[q] != sb[q]) return if (sa[q] > sb[q]) 1 else -1
    return if (sa.length >= n || sb.length >= n) 0
    else if (sa.length > sb.length) 1 else if (sa.length < sb.length) -1 else 0
}

/** h014/h002: vendor sscanf("%p") decode - pointer text to the
 *  opaque handle (the GTK arms' widget plumbing; u73 precedent). */
private fun ptrDecodeC(text: String): Long {
    val t = text.trim()
    val h = if (t.startsWith("0x") || t.startsWith("0X")) t.substring(2).toLongOrNull(16) else null
    return h ?: t.toLongOrNull() ?: 0L
}

/** h014/h002: vendor sprintf("%p") encode - handle to pointer
 *  text (the GtkTreeViewGetSelection arm hands the selection
 *  back to the script layer). */
private fun ptrEncodeC(h: Long): String = "0x" + h.toString(16)

/** h014: vendor popen(cmd, "r") read edge (u155 fopen/popen
 *  carrier precedent); returns the first fgets line or null. */
private fun popenReadLine(cmd: String): String? =
    try {
        val p = ProcessBuilder("sh", "-c", cmd).start()
        p.inputStream.bufferedReader().readLine().also { p.waitFor() }
    } catch (e: Exception) { null }

/** h014: vendor system(buf) - exit status, negative on failure. */
private fun systemC(cmd: String): Int =
    try {
        ProcessBuilder("sh", "-c", cmd).start().waitFor()
    } catch (e: Exception) { -1 }

/** h014: vendor chdir(dir) - 0 ok, -1 failure (errno carrier). */
private fun chdirC(dir: String): Int =
    try {
        val d = java.io.File(dir)
        if (d.isDirectory) 0 else -1
    } catch (e: Exception) { -1 }

/** h014: vendor getenv - null when unset (Kotlin null for the
 *  C NULL; the zgetenv arm checks non-NULL first). */
private fun getenvC(name: String): String? = System.getenv(name)

/** h014: vendor srand(seed) - per-file no-op carrier (the JVM
 *  RNG seeding is process-level; u128 deterministic-UB
 *  quantization: the vendor seed call cannot reach a JVM
 *  engine, journaled). */
private fun srandC(seed: Int) { /* seeded at process level; no-op carrier */ }

/**
 * vendor 158: void zfcn(unsigned char v1d[], unsigned char bd[],
 * struct MSV * svPtr). Parameters v1dP/bdP (h015); the body
 * works on the index-0 copies v1d/bd (h001).
 */
fun zfcn(v1dP: CharArray, v1dOff: Int, bdP: CharArray, bdOff: Int, svPtr: Msv) {
    // h001 copy-in (u173 precedent): NUL-terminated window at 0.
    val v1d = CharArray(STR_MAX)
    val origLen = strlenC(v1dP, v1dOff)
    for (q in 0..origLen) v1d[q] = v1dP[v1dOff + q]
    val bd = CharArray(STR_MAX)
    val bdOrigLen = strlenC(bdP, bdOff)
    for (q in 0..bdOrigLen) bd[q] = bdP[bdOff + q]

    // vendor 161-175 locals (fd/dtime/timex/tmp4/gbltbl/gblstr/x/g/rt
    // and the zcode2/zcode3 prologue stores are h013 dead in this
    // unit - journaled; Kotlin would warn)
    val tmp2 = CharArray(STR_MAX)
    val tmp3 = CharArray(STR_MAX)
    var j: Int
    val iargs = IntArray(11) // vendor: short int iargs[10] (h010)
    var nargs: Int
    var WordTrunc = 25
    val fcnName = CharArray(64)
    var i: Long

    // vendor 203-211: prologue
    svPtr.ierr = 0
    nargs = 0
    iargs[0] = 1
    val zcode = tolowerC(v1d[3])
    val zcode1 = tolowerC(v1d[4])

    i = 1
    while (v1d[i.toInt()] != OPEN.toChar() && v1d[i.toInt()] != NUL) i++

    // vendor: strncpy(fcnName, &v1d[2], i-2) - no NUL pad in C
    val fnLen = (i - 2).toInt().coerceAtMost(63)
    for (q in 0 until fnLen) fcnName[q] = v1d[2 + q]
    val fn = cstringAt(fcnName)

    while (v1d[1] != OPEN.toChar() && v1d[1] != NUL)
        strmove(v1d, 1, v1d, 2)

    var lab = ZLbl.DISPATCH
sm@ while (true) when (lab) {
        ZLbl.DISPATCH -> {
            // vendor 218-231: zglobal gac/s vs the arg split
            if (zcode == 'g' && (zcode1 == 'a' || zcode1 == 'c' || zcode1 == 's')) {
                strmove(v1d, 1, v1d, 2)
                i = 1
                while (v1d[i.toInt()] != NUL) i++
                if (v1d[(i - 1).toInt()] != CLOSE.toChar() ||
                    v1d[(i - 2).toInt()] != CLOSE.toChar()) {
                    lab = ZLbl.err
                    continue@sm
                }
                v1d[(i - 1).toInt()] = NUL
            } else {
                if (v1d[1] == OPEN.toChar()) strmove(v1d, 1, v1d, 2)
                i = 1
                while (v1d[i.toInt()] != NUL) { // locate arguments
                    if (v1d[i.toInt()] == CLOSE.toChar() || v1d[i.toInt()] == COMMA.toChar()) {
                        v1d[i.toInt()] = NUL
                        nargs++
                        iargs[nargs] = i.toInt() + 1
                    }
                    i++
                }
            }
            svPtr.ierr = 0

            // vendor 246-263: the z~gtk / z~mdh widget family
            if (strncasecmpC(fn, "z~gtk", 5) == 0 || strncasecmpC(fn, "z~mdh", 5) == 0) {
                if (strncasecmpC(fn, "z~mdh~file~chooser~get~filename", 31) == 0) { lab = ZLbl.GtkFileChooser; continue@sm }
                if (strncasecmpC(fn, "z~mdh~calendar~get~date", 23) == 0) { lab = ZLbl.GtkCalendar; continue@sm }
                if (strncasecmpC(fn, "z~mdh~range~get~adjustment", 26) == 0) { lab = ZLbl.GtkGetRangeAdjustment; continue@sm }
                if (strncasecmpC(fn, "z~mdh~font~chooser~get~font", 27) == 0) { lab = ZLbl.GtkFontChooser; continue@sm }
                if (strncasecmpC(fn, "z~mdh~color~chooser~get~rgba", 28) == 0) { lab = ZLbl.GtkColorChooser; continue@sm }
                if (strncasecmpC(fn, "z~mdh~switch~get~active", 23) == 0) { lab = ZLbl.GtkSwitchGetActive; continue@sm }
                if (strncasecmpC(fn, "z~mdh~toggle~button~get~active", 30) == 0) { lab = ZLbl.GtkToggleButtonGetActive; continue@sm }
                if (strncasecmpC(fn, "z~mdh~toggle~button~set~active", 30) == 0) { lab = ZLbl.GtkToggleButtonSetActive; continue@sm }
                if (strncasecmpC(fn, "z~mdh~spin~button~set~value", 27) == 0) { lab = ZLbl.GtkSpinButtonSetValue; continue@sm }
                if (strncasecmpC(fn, "z~mdh~entry~get~text", 20) == 0) { lab = ZLbl.GtkEntryGetText; continue@sm }
                if (strncasecmpC(fn, "z~mdh~entry~set~text", 20) == 0) { lab = ZLbl.GtkEntrySetText; continue@sm }
                if (strncasecmpC(fn, "z~mdh~text~buffer~set~text", 22) == 0) { lab = ZLbl.GtkTextBufferSetText; continue@sm }
                if (strncasecmpC(fn, "z~mdh~label~set~text", 20) == 0) { lab = ZLbl.GtkLabelSetText; continue@sm }
                if (strncasecmpC(fn, "z~mdh~dialog~new~with~buttons", 29) == 0) { lab = ZLbl.GtkDialogWithButtons; continue@sm }
                if (strncasecmpC(fn, "z~mdh~tree~view~get~selection", 29) == 0) { lab = ZLbl.GtkTreeViewGetSelection; continue@sm }
                if (strncasecmpC(fn, "z~mdh~tree~selection~get~selected", 33) == 0) { lab = ZLbl.GtkTreeSelectionGetSelected; continue@sm }
                if (strncasecmpC(fn, "z~mdh~tree~model~get", 20) == 0) { lab = ZLbl.GtkTreeModelGet; continue@sm }
                if (strncasecmpC(fn, "z~mdh~tree~store~clear", 22) == 0) { lab = ZLbl.GtkTreeStoreClear; continue@sm }
                if (strncasecmpC(fn, "z~mdh~tree~level~add", 20) == 0) { lab = ZLbl.GtkTreeLevelAdd; continue@sm }
                if (strncasecmpC(fn, "z~mdh~widget~show", 17) == 0) { lab = ZLbl.GtkWidgetShow; continue@sm }
                if (strncasecmpC(fn, "z~mdh~widget~hide", 17) == 0) { lab = ZLbl.GtkWidgetHide; continue@sm }
                lab = ZLbl.fcn_not_found
                continue@sm
            }

            // vendor 265-285: the zz statistics family
            if (strncasecmpC(fn, "zz", 2) == 0) {
                if (strcasecmpC(fn, "zzsoundex") == 0) { lab = ZLbl.zzsoundex; continue@sm }
                if (strcasecmpC(fn, "zzcosine") == 0) { lab = ZLbl.zzcosine; continue@sm }
                if (strcasecmpC(fn, "zzinput") == 0) { lab = ZLbl.zzinput; continue@sm }
                if (strcasecmpC(fn, "zzdice") == 0) { lab = ZLbl.zzdice; continue@sm }
                if (strcasecmpC(fn, "zzjaccard") == 0) { lab = ZLbl.zzjaccard; continue@sm }
                if (strcasecmpC(fn, "zzsim1") == 0) { lab = ZLbl.zzsim1; continue@sm }
                if (strcasecmpC(fn, "zzscanalnum") == 0) { lab = ZLbl.zzscanalnum; continue@sm }
                if (strcasecmpC(fn, "zzscan") == 0) { lab = ZLbl.zzscan; continue@sm }
                if (strcasecmpC(fn, "zzavg") == 0) { lab = ZLbl.zzavg; continue@sm }
                if (strcasecmpC(fn, "zzidf") == 0) { lab = ZLbl.zzidf; continue@sm }
                if (strcasecmpC(fn, "zztranspose") == 0) { lab = ZLbl.zztranspose; continue@sm }
                if (strcasecmpC(fn, "zzcentroid") == 0) { lab = ZLbl.zzcentroid; continue@sm }
                if (strcasecmpC(fn, "zztermcorrelate") == 0) { lab = ZLbl.zztermcorrelate; continue@sm }
                if (strcasecmpC(fn, "zzdoccorrelate") == 0) { lab = ZLbl.zzdoccorrelate; continue@sm }
                if (strcasecmpC(fn, "zzcount") == 0) { lab = ZLbl.zzcount; continue@sm }
                if (strcasecmpC(fn, "zzmax") == 0) { lab = ZLbl.zzmax; continue@sm }
                if (strcasecmpC(fn, "zzmin") == 0) { lab = ZLbl.zzmin; continue@sm }
                if (strcasecmpC(fn, "zzsum") == 0) { lab = ZLbl.zzsum; continue@sm }
                if (strcasecmpC(fn, "zzmultiply") == 0) { lab = ZLbl.zzmultiply; continue@sm }
                lab = ZLbl.fcn_not_found
                continue@sm
            }

            // vendor 287-308: the zs family
            if (strncasecmpC(fn, "zs", 2) == 0) {
                if (strcasecmpC(fn, "zstem") == 0) { lab = ZLbl.zstem; continue@sm }
                if (strncasecmpC(fn, "zstopl", 6) == 0) { lab = ZLbl.zstoplookup; continue@sm }
                if (strcasecmpC(fn, "zsin") == 0) { lab = ZLbl.zsine; continue@sm }
                if (strcasecmpC(fn, "zseek") == 0) { lab = ZLbl.zseek; continue@sm }
                if (strcasecmpC(fn, "zsmithwaterman") == 0) { lab = ZLbl.zsmithwaterman; continue@sm }
                if (strcasecmpC(fn, "zstopinit") == 0) { lab = ZLbl.zstopinit; continue@sm }
                if (strcasecmpC(fn, "zsyninit") == 0) { lab = ZLbl.zsyninit; continue@sm }
                if (strcasecmpC(fn, "zsynlookup") == 0) { lab = ZLbl.zsynlookup; continue@sm }
                if (strcasecmpC(fn, "zshred") == 0) { lab = ZLbl.zshred; continue@sm }
                if (strcasecmpC(fn, "zshredquery") == 0) { lab = ZLbl.zshredquery; continue@sm }
                if (strcasecmpC(fn, "zsrand") == 0) { lab = ZLbl.zsrand; continue@sm }
                if (strcasecmpC(fn, "zsqlite") == 0) { lab = ZLbl.zsqlite; continue@sm }
                if (strcasecmpC(fn, "zsqlopen") == 0) { lab = ZLbl.zsqlOpen; continue@sm }
                if (strcasecmpC(fn, "zsqrt") == 0) { lab = ZLbl.zroot; continue@sm }
                if (strcasecmpC(fn, "zsystem") == 0) { lab = ZLbl.zsystem; continue@sm }
                lab = ZLbl.fcn_not_found
                continue@sm
            }

            // vendor 310-341: the straight-line names
            if (strncasecmpC(fn, "zlow", 4) == 0) { lab = ZLbl.zlower; continue@sm }
            if (strcasecmpC(fn, "zcos") == 0) { lab = ZLbl.zcosine; continue@sm }
            if (strcasecmpC(fn, "zgetenv") == 0) { lab = ZLbl.zgetenv; continue@sm }
            if (strcasecmpC(fn, "ztan") == 0) { lab = ZLbl.ztangent; continue@sm }
            if (strcasecmpC(fn, "zatan") == 0) { lab = ZLbl.zatangent; continue@sm }
            if (strcasecmpC(fn, "zacos") == 0) { lab = ZLbl.zacosine; continue@sm }
            if (strcasecmpC(fn, "zasin") == 0) { lab = ZLbl.zasine; continue@sm }
            if (strcasecmpC(fn, "zexp") == 0) { lab = ZLbl.zexp; continue@sm }
            if (strcasecmpC(fn, "zexp2") == 0) { lab = ZLbl.zexp2; continue@sm }
            if (strcasecmpC(fn, "zexp10") == 0) { lab = ZLbl.zexp10; continue@sm }
            if (strcasecmpC(fn, "zlog") == 0) { lab = ZLbl.zlog; continue@sm }
            if (strcasecmpC(fn, "zlog10") == 0) { lab = ZLbl.zlog10; continue@sm }
            if (strcasecmpC(fn, "zlog2") == 0) { lab = ZLbl.zlog2; continue@sm }
            if (strcasecmpC(fn, "zpow") == 0) { lab = ZLbl.zpow; continue@sm }
            if (strcasecmpC(fn, "zhit") == 0) { lab = ZLbl.zhit; continue@sm }
            if (strcasecmpC(fn, "zchdir") == 0) { lab = ZLbl.zchdir; continue@sm }
            if (strcasecmpC(fn, "zflush") == 0) { lab = ZLbl.zflush; continue@sm }
            if (strcasecmpC(fn, "znoblanks") == 0) { lab = ZLbl.znoblanks; continue@sm }
            if (strcasecmpC(fn, "zpad") == 0) { lab = ZLbl.zpad; continue@sm }
            if (strcasecmpC(fn, "zbmgsearch") == 0) { lab = ZLbl.zbmgsearch; continue@sm }
            if (strcasecmpC(fn, "znative") == 0) { lab = ZLbl.znative; continue@sm }
            if (strcasecmpC(fn, "zdump") == 0) { lab = ZLbl.zcondense; continue@sm }
            if (strcasecmpC(fn, "zrestore") == 0) { lab = ZLbl.zrestore; continue@sm }
            if (strcasecmpC(fn, "zcurrentfile") == 0) { lab = ZLbl.zcurrentfile; continue@sm }
            if (strcasecmpC(fn, "zceil") == 0) { lab = ZLbl.zceil; continue@sm }
            if (strcasecmpC(fn, "zfloor") == 0) { lab = ZLbl.zfloor; continue@sm }
            if (strcasecmpC(fn, "ztrunc") == 0) { lab = ZLbl.ztrunc; continue@sm }
            if (strcasecmpC(fn, "zprogram") == 0) { lab = ZLbl.zprogram; continue@sm }
            if (strcasecmpC(fn, "zdatabase") == 0) { lab = ZLbl.zdatabase; continue@sm }
            if (strcasecmpC(fn, "zbasename") == 0) { lab = ZLbl.zbasename; continue@sm }
            if (strcasecmpC(fn, "zfiletest") == 0) { lab = ZLbl.zfiletest; continue@sm }
            // vendor 341: the commented-out zglobal->zfiletest
            // fossil kept as a roster note
            if (strcasecmpC(fn, "zindexmax") == 0) { lab = ZLbl.zindexmax; continue@sm }
            if (strcasecmpC(fn, "zindexsize") == 0) { lab = ZLbl.zindexsize; continue@sm }
            if (strcasecmpC(fn, "zdatasize") == 0) { lab = ZLbl.zdatasize; continue@sm }
            if (strcasecmpC(fn, "zdbname") == 0) { lab = ZLbl.zdbname; continue@sm }
            if (strcasecmpC(fn, "zdbfile") == 0) { lab = ZLbl.zdbfile; continue@sm }

            // vendor 343-357: the zcode first-letter ladder
            if (zcode == 'a') {
                if (zcode1 == 'b') { lab = ZLbl.zabs; continue@sm }
                lab = ZLbl.zalter
                continue@sm
            }
            if (zcode == 'd') { lab = ZLbl.zdate; continue@sm }
            if (zcode == 'v') { lab = ZLbl.z_variable; continue@sm }
            if (zcode == 'n') { lab = ZLbl.znormal; continue@sm }
            if (zcode == 'b') { lab = ZLbl.zblnk; continue@sm }
            if (zcode == 'p') { lab = ZLbl.zpad; continue@sm }
            if (zcode == 'h') { lab = ZLbl.zhtml; continue@sm }
            if (zcode == 'f') { lab = ZLbl.zfile; continue@sm }
            if (zcode == 'g') { lab = ZLbl.zglobal; continue@sm }
            if (zcode == 'l') { lab = ZLbl.zlog; continue@sm }
            if (zcode == 'm') { lab = ZLbl.znxtrow; continue@sm }
            if (zcode == 'w') { lab = ZLbl.zword; continue@sm }
            if (zcode == 't') { lab = ZLbl.ztell; continue@sm }
            if (zcode == 'u') { lab = ZLbl.znumeric; continue@sm }

            lab = ZLbl.fcn_not_found
        }

        ZLbl.GtkDialogWithButtons -> {
            // vendor 372-407
            if (nargs != 2) { lab = ZLbl.err; continue@sm }
            val window = ZfcnState.widgetAt(ptrDecodeC(cstringAt(v1d, 1))) { GtkWindow() }
            // vendor: GtkDialogFlags or-combination (the numeric
            // flags are GTK enum constants; carried as the Int)
            val flags = 0
            val dialog = gtk_dialog_new_with_buttons(
                cstringAt(v1d, iargs[1]), window, flags)
            // Expect 200 for NO and 100 for YES (vendor comment kept)
            gtk_window_set_keep_above(dialog, true)
            val r = gtk_dialog_run(dialog)
            gtk_widget_destroy(dialog)
            if (r == 100) strcpyC(bd, "1")
            else if (r == 200) strcpyC(bd, "0")
            else strcpyC(bd, "-1")
            lab = ZLbl.RET
        }

        ZLbl.GtkWidgetHide -> {
            // vendor 409-417
            val widget = ZfcnState.widgetAt(ptrDecodeC(cstringAt(v1d, 1))) { GtkWidget() }
            gtk_widget_hide(widget)
            bd[0] = NUL
            lab = ZLbl.RET
        }

        ZLbl.GtkWidgetShow -> {
            // vendor 419-427
            val widget = ZfcnState.widgetAt(ptrDecodeC(cstringAt(v1d, 1))) { GtkWidget() }
            gtk_widget_show(widget)
            bd[0] = NUL
            lab = ZLbl.RET
        }

        ZLbl.GtkSpinButtonGetValue -> {
            // vendor 429-437
            if (nargs != 1) { lab = ZLbl.err; continue@sm }
            val sp = ZfcnState.widgetAt(ptrDecodeC(cstringAt(v1d, 1))) { GtkSpinButton() }
            ZfcnState.spin = sp
            val xv = gtk_spin_button_get_value(sp)
            writeCstring(bd, fmtF(xv))
            lab = ZLbl.RET
        }

        ZLbl.GtkSpinButtonSetValue -> {
            // vendor 440-451
            if (nargs != 2) { lab = ZLbl.err; continue@sm }
            val sp = ZfcnState.widgetAt(ptrDecodeC(cstringAt(v1d, 1))) { GtkSpinButton() }
            ZfcnState.spin = sp
            val value = atofC(v1d, iargs[1])
            gtk_spin_button_set_value(sp, value)
            bd[0] = NUL
            lab = ZLbl.RET
        }

        ZLbl.GtkTreeStoreClear -> {
            // vendor 454-463
            if (nargs != 1) { lab = ZLbl.err; continue@sm }
            val ts = ZfcnState.widgetAt(ptrDecodeC(cstringAt(v1d, 1))) { GtkTreeStore() }
            ZfcnState.t = ts
            gtk_tree_store_clear(ts)
            strcpyC(bd, "1")
            lab = ZLbl.RET
        }

        ZLbl.GtkTreeModelGet -> {
            // vendor 466-488
            if (nargs != 5) { lab = ZLbl.err; continue@sm }
            val m = ZfcnState.widgetAt(ptrDecodeC(cstringAt(v1d, 1))) { GtkTreeModel() }
            // one col number / value pair; can be called multiple
            // times for separate columns (vendor comment kept)
            gtk_tree_model_get(m, ZfcnState.iter, 1).let { strcpyC(bd, it) }
            lab = ZLbl.RET
        }

        ZLbl.GtkTreeSelectionGetSelected -> {
            // vendor 490-513 - Tree/Table row selection signal
            if (nargs != 2) { lab = ZLbl.err; continue@sm }
            val s = ZfcnState.widgetAt(ptrDecodeC(cstringAt(v1d, 1))) { GtkTreeSelection() }
            val col = atoiC(v1d, iargs[1])
            val m = arrayOf<GtkTreeModel?>(GtkTreeModel())
            if (!gtk_tree_selection_get_selected(s, m, ZfcnState.iter)) {
                strcpyC(bd, "0")
                lab = ZLbl.RET
                continue@sm
            }
            bd[0] = NUL
            val mm = m[0] ?: GtkTreeModel() // h012 null-carrier (u155 precedent)
            gtk_tree_model_get(mm, ZfcnState.iter, col).let { strcpyC(bd, it) }
            lab = ZLbl.RET
        }

        ZLbl.GtkTreeViewGetSelection -> {
            // vendor 515-526
            if (nargs != 1) { lab = ZLbl.err; continue@sm }
            val t = ZfcnState.widgetAt(ptrDecodeC(cstringAt(v1d, 1))) { GtkTreeView() }
            val s = gtk_tree_view_get_selection(t)
            writeCstring(bd, ptrEncodeC(System.identityHashCode(s).toLong()))
            lab = ZLbl.RET
        }

        ZLbl.GtkTreeLevelAdd -> {
            // vendor 528-550: the depth dispatch
            if (nargs < 4) { lab = ZLbl.err; continue@sm }
            val depth = atoiC(v1d, iargs[1])
            lab = when (depth) {
                1 -> ZLbl.GtkTreeLevel1Add
                2 -> ZLbl.GtkTreeLevel2Add
                3 -> ZLbl.GtkTreeLevel3Add
                4 -> ZLbl.GtkTreeLevel4Add
                5 -> ZLbl.GtkTreeLevel5Add
                6 -> ZLbl.GtkTreeLevel6Add
                7 -> ZLbl.GtkTreeLevel7Add
                8 -> ZLbl.GtkTreeLevel8Add
                9 -> ZLbl.GtkTreeLevel9Add
                10 -> ZLbl.GtkTreeLevel10Add
                else -> ZLbl.err
            }
            continue@sm
            // vendor 546-548: the v1d[1] -> treestore /
            // v1d[iargs[1]] -> depth header comment kept (u73)
        }

        ZLbl.GtkTreeLevel1Add -> {
            GtkTreeAdd(ZfcnState.iter, null, tail(v1d, 1), v1d,
                ShortArray(14) { q -> if (q < 11) iargs[q].toShort() else 0 }, nargs)
            strcpyC(bd, "0")
            lab = ZLbl.RET
        }

        ZLbl.GtkTreeLevel2Add -> {
            GtkTreeAdd(ZfcnState.iter1, ZfcnState.iter, tail(v1d, 1), v1d,
                ShortArray(14) { q -> if (q < 11) iargs[q].toShort() else 0 }, nargs)
            strcpyC(bd, "0")
            lab = ZLbl.RET
        }

        ZLbl.GtkTreeLevel3Add -> {
            GtkTreeAdd(ZfcnState.iter2, ZfcnState.iter1, tail(v1d, 1), v1d,
                ShortArray(14) { q -> if (q < 11) iargs[q].toShort() else 0 }, nargs)
            strcpyC(bd, "0")
            lab = ZLbl.RET
        }

        ZLbl.GtkTreeLevel4Add -> {
            GtkTreeAdd(ZfcnState.iter3, ZfcnState.iter2, tail(v1d, 1), v1d,
                ShortArray(14) { q -> if (q < 11) iargs[q].toShort() else 0 }, nargs)
            strcpyC(bd, "0")
            lab = ZLbl.RET
        }

        ZLbl.GtkTreeLevel5Add -> {
            GtkTreeAdd(ZfcnState.iter4, ZfcnState.iter3, tail(v1d, 1), v1d,
                ShortArray(14) { q -> if (q < 11) iargs[q].toShort() else 0 }, nargs)
            strcpyC(bd, "0")
            lab = ZLbl.RET
        }

        ZLbl.GtkTreeLevel6Add -> {
            GtkTreeAdd(ZfcnState.iter5, ZfcnState.iter4, tail(v1d, 1), v1d,
                ShortArray(14) { q -> if (q < 11) iargs[q].toShort() else 0 }, nargs)
            strcpyC(bd, "0")
            lab = ZLbl.RET
        }

        ZLbl.GtkTreeLevel7Add -> {
            GtkTreeAdd(ZfcnState.iter6, ZfcnState.iter5, tail(v1d, 1), v1d,
                ShortArray(14) { q -> if (q < 11) iargs[q].toShort() else 0 }, nargs)
            strcpyC(bd, "0")
            lab = ZLbl.RET
        }

        ZLbl.GtkTreeLevel8Add -> {
            GtkTreeAdd(ZfcnState.iter7, ZfcnState.iter6, tail(v1d, 1), v1d,
                ShortArray(14) { q -> if (q < 11) iargs[q].toShort() else 0 }, nargs)
            strcpyC(bd, "0")
            lab = ZLbl.RET
        }

        ZLbl.GtkTreeLevel9Add -> {
            GtkTreeAdd(ZfcnState.iter8, ZfcnState.iter7, tail(v1d, 1), v1d,
                ShortArray(14) { q -> if (q < 11) iargs[q].toShort() else 0 }, nargs)
            strcpyC(bd, "0")
            lab = ZLbl.RET
        }

        ZLbl.GtkTreeLevel10Add -> {
            GtkTreeAdd(ZfcnState.iter9, ZfcnState.iter8, tail(v1d, 1), v1d,
                ShortArray(14) { q -> if (q < 11) iargs[q].toShort() else 0 }, nargs)
            strcpyC(bd, "0")
            lab = ZLbl.RET
        }

        ZLbl.GtkLabelSetText -> {
            // vendor 633-643
            if (nargs != 2) { lab = ZLbl.err; continue@sm }
            val l = ZfcnState.widgetAt(ptrDecodeC(cstringAt(v1d, 1))) { GtkLabel() }
            gtk_label_set_text(l, cstringAt(v1d, iargs[1]))
            strcpyC(bd, "1")
            lab = ZLbl.RET
        }

        ZLbl.GtkTextBufferSetText -> {
            // vendor 645-655
            if (nargs != 2) { lab = ZLbl.err; continue@sm }
            val b = ZfcnState.widgetAt(ptrDecodeC(cstringAt(v1d, 1))) { GtkTextBuffer() }
            gtk_text_buffer_set_text(b, cstringAt(v1d, iargs[1]), -1)
            strcpyC(bd, "1")
            lab = ZLbl.RET
        }

        ZLbl.GtkEntryGetText -> {
            // vendor 657-666
            if (nargs != 1) { lab = ZLbl.err; continue@sm }
            val e = ZfcnState.widgetAt(ptrDecodeC(cstringAt(v1d, 1))) { GtkEntry() }
            strcpyC(bd, gtk_entry_get_text(e))
            lab = ZLbl.RET
        }

        ZLbl.GtkEntrySetText -> {
            // vendor 668-677
            if (nargs != 2) { lab = ZLbl.err; continue@sm }
            val e = ZfcnState.widgetAt(ptrDecodeC(cstringAt(v1d, 1))) { GtkEntry() }
            gtk_entry_set_text(e, cstringAt(v1d, iargs[1]))
            strcpyC(bd, "0")
            lab = ZLbl.RET
        }

        ZLbl.GtkGetRangeAdjustment -> {
            // vendor 680-690
            if (nargs != 1) { lab = ZLbl.err; continue@sm }
            val e = ZfcnState.widgetAt(ptrDecodeC(cstringAt(v1d, 1))) { GtkRange() }
            val adj = gtk_range_get_adjustment(e)
            writeCstring(bd, fmtF(gtk_adjustment_get_value(adj)))
            lab = ZLbl.RET
        }

        ZLbl.GtkFileChooser -> {
            // vendor 692-701 (the unused static char tmp[4096]
            // is dropped, h013 note)
            if (nargs != 1) { lab = ZLbl.err; continue@sm }
            val e = ZfcnState.widgetAt(ptrDecodeC(cstringAt(v1d, 1))) { GtkFileChooser() }
            // vendor: strncpy(bd, filename, STR_MAX)
            val fnm = gtk_file_chooser_get_filename(e) ?: ""
            val n = minOf(fnm.length, STR_MAX - 1)
            for (q in 0 until n) bd[q] = fnm[q]
            bd[n] = NUL
            lab = ZLbl.RET
        }

        ZLbl.GtkCalendar -> {
            // vendor 703-714
            if (nargs != 1) { lab = ZLbl.err; continue@sm }
            val e = ZfcnState.widgetAt(ptrDecodeC(cstringAt(v1d, 1))) { GtkCalendar() }
            val yr = LongArray(1); val mn = LongArray(1); val dy = LongArray(1)
            gtk_calendar_get_date(e, yr, mn, dy)
            writeCstring(bd, yr[0].toString() + " " + mn[0] + " " + dy[0])
            lab = ZLbl.RET
        }

        ZLbl.GtkFontChooser -> {
            // vendor 716-725
            if (nargs != 1) { lab = ZLbl.err; continue@sm }
            val e = ZfcnState.widgetAt(ptrDecodeC(cstringAt(v1d, 1))) { GtkFontChooser() }
            strcpyC(bd, gtk_font_chooser_get_font(e))
            lab = ZLbl.RET
        }

        ZLbl.GtkColorChooser -> {
            // vendor 727-739
            if (nargs != 1) { lab = ZLbl.err; continue@sm }
            val c = ZfcnState.widgetAt(ptrDecodeC(cstringAt(v1d, 1))) { GtkColorChooser() }
            val r = GdkRGBA()
            gtk_color_chooser_get_rgba(c, r)
            writeCstring(bd, fmtF(r.red) + " " + fmtF(r.green) + " " +
                fmtF(r.blue) + " " + fmtF(r.alpha))
            lab = ZLbl.RET
        }

        ZLbl.GtkToggleButtonSetActive -> {
            // vendor 741-765
            if (nargs != 2) { lab = ZLbl.err; continue@sm }
            val t = ZfcnState.widgetAt(ptrDecodeC(cstringAt(v1d, 1))) { GtkToggleButton() }
            if (cstringAt(v1d, iargs[1]) == "TRUE") {
                gtk_toggle_button_set_active(t, true)
                strcpyC(bd, "1")
                lab = ZLbl.RET
                continue@sm
            }
            if (cstringAt(v1d, iargs[1]) == "FALSE") {
                gtk_toggle_button_set_active(t, false)
                strcpyC(bd, "1")
                lab = ZLbl.RET
                continue@sm
            }
            strcpyC(bd, "0")
            lab = ZLbl.err
        }

        ZLbl.GtkToggleButtonGetActive -> {
            // vendor 767-779
            if (nargs != 1) { lab = ZLbl.err; continue@sm }
            val t = ZfcnState.widgetAt(ptrDecodeC(cstringAt(v1d, 1))) { GtkToggleButton() }
            if (gtk_toggle_button_get_active(t)) strcpyC(bd, "1") else strcpyC(bd, "0")
            lab = ZLbl.RET
        }

        ZLbl.GtkSwitchGetActive -> {
            // vendor 781-793
            if (nargs != 1) { lab = ZLbl.err; continue@sm }
            val t = ZfcnState.widgetAt(ptrDecodeC(cstringAt(v1d, 1))) { GtkSwitch() }
            if (gtk_switch_get_active(t)) strcpyC(bd, "1") else strcpyC(bd, "0")
            lab = ZLbl.RET
        }

        ZLbl.zdatabase -> {
            // vendor 795-806
            if (nargs != 0) { lab = ZLbl.err; continue@sm }
            // vendor: the constant test strcmp("sql","sql") is
            // always true - kept verbatim
            if ("sql" == "sql") {
                if (0 == 1) strcpyC(bd, "3") // sql memory resident
                else strcpyC(bd, "2") // sql disk resident
                lab = ZLbl.RET
                continue@sm
            }
            strcpyC(bd, "1") // native database disk resident
            lab = ZLbl.RET
        }

        ZLbl.zprogram -> {
            // vendor 808-812
            if (nargs != 0) { lab = ZLbl.err; continue@sm }
            strcpyC(bd, cstringAt(svPtr.program))
            lab = ZLbl.RET
        }

        ZLbl.zfiletest -> {
            // vendor 814-836
            if (nargs != 2) { lab = ZLbl.err; continue@sm }
            val tmp = CharArray(1024)
            strcpyC(tmp, "echo " + BTICK + "if [ ")
            strncatC(tmp, cstringAt(v1d, iargs[1]), 500)
            strcatC(tmp, " \"")
            strncatC(tmp, cstringAt(v1d, 1), 500)
            strcatC(tmp, "\" ]; then echo \"1\"; fi" + BTICK)
            val line = popenReadLine(cstringAt(tmp)) ?: ""
            // vendor: fgets line then strip the trailing newline
            val stripped = if (line.isNotEmpty()) line.dropLast(1) else line
            if (stripped == "1") strcpyC(bd, "1") else strcpyC(bd, "0")
            lab = ZLbl.RET
        }

        ZLbl.zbasename -> {
            // vendor 838-872
            if (!(nargs == 1 || nargs == 2)) { lab = ZLbl.err; continue@sm }
            if (nargs == 1) {
                val tmp = CharArray(1024)
                strcpyC(tmp, "echo " + BTICK + "basename \"")
                strncatC(tmp, cstringAt(v1d, 1), 1000)
                strcatC(tmp, "\"" + BTICK)
                val line = popenReadLine(cstringAt(tmp)) ?: ""
                val stripped = if (line.isNotEmpty()) line.dropLast(1) else line
                strcpyC(bd, stripped)
                lab = ZLbl.RET
            } else {
                val tmp = CharArray(1024)
                strcpyC(tmp, "echo " + BTICK + "basename \"")
                strncatC(tmp, cstringAt(v1d, 1), 900)
                strcatC(tmp, "\" \"")
                strncatC(tmp, cstringAt(v1d, iargs[1]), 32)
                strcatC(tmp, "\"" + BTICK)
                val line = popenReadLine(cstringAt(tmp)) ?: ""
                // vendor: the nargs==2 arm does NOT strip (kept)
                strcpyC(bd, line)
                lab = ZLbl.RET
            }
        }

        ZLbl.zfloor -> {
            // vendor 874-878
            if (nargs != 1) { lab = ZLbl.err; continue@sm }
            mpsFloor(bd, tail(v1d, 1))
            lab = ZLbl.RET
        }

        ZLbl.zceil -> {
            // vendor 880-884
            if (nargs != 1) { lab = ZLbl.err; continue@sm }
            mpsCeil(bd, tail(v1d, 1))
            lab = ZLbl.RET
        }

        ZLbl.ztrunc -> {
            // vendor 886-890
            if (nargs != 1) { lab = ZLbl.err; continue@sm }
            mpsTrunc(bd, tail(v1d, 1))
            lab = ZLbl.RET
        }

        ZLbl.zsqlOpen -> {
            // vendor 892-897
            if (svPtr.ConOpen != 0) bd[0] = '1' else bd[0] = '0'
            bd[1] = NUL
            lab = ZLbl.RET
        }

        ZLbl.znative -> {
            // vendor 900-910: SQLITE defined - the "0" arm renders;
            // the excluded non-SQLITE "1" arm is journaled (h018)
            strcpyC(bd, "0")
            lab = ZLbl.RET
        }

        ZLbl.zindexsize -> {
            // vendor 916-919 (SQLITE arm)
            strcpyC(bd, "256")
            lab = ZLbl.RET
        }

        ZLbl.zdatasize -> {
            // vendor 920-923 (SQLITE arm)
            strcpyC(bd, "512")
            lab = ZLbl.RET
        }

        ZLbl.zdbname -> {
            // vendor 924-927 (SQLITE arm)
            strcpyC(bd, "mumps")
            lab = ZLbl.RET
        }

        ZLbl.zdbfile -> {
            // vendor 928-931 (SQLITE arm; the #else cascade at
            // 941-947 is journaled, h018)
            strcpyC(bd, "mumps.sqlite")
            lab = ZLbl.RET
        }

        ZLbl.zindexmax -> {
            // vendor 930-939 (SQLITE arm)
            val tmp = CharArray(32)
            i = 14
            writeCstring(tmp, (i - 2).toString())
            strcpyC(bd, cstringAt(tmp))
            lab = ZLbl.RET
        }

        ZLbl.zsqlite -> {
            // vendor 951-1048: the SQLITE transaction surface
            if (nargs == 0) {
                strcpyC(bd, "1")
                lab = ZLbl.RET
                continue@sm
            }
            val XTAB = charArrayOf('\t', NUL)
            val first = cstringAt(v1d, 1)
            if (strcasecmpC(first, "begin transaction") == 0 && nargs == 1) {
                val filename = CharArray(64)
                val dmy = CharArray(8)
                strcpyC(filename, "begin transaction;")
                svPtr.tpx = sql(BEGIN_TRANSACTION, svPtr, EP_EMPTY, filename, dmy, XTAB).toLong()
                writeCstring(bd, svPtr.tpx.toString())
                lab = ZLbl.RET
                continue@sm
            }
            if (strcasecmpC(first, "commit transaction") == 0 && nargs == 1) {
                val filename = CharArray(64)
                val dmy = CharArray(8)
                strcpyC(filename, "commit transaction;")
                svPtr.tpx = sql(COMMIT_TRANSACTION, svPtr, EP_EMPTY, filename, dmy, XTAB).toLong()
                writeCstring(bd, svPtr.tpx.toString())
                lab = ZLbl.RET
                continue@sm
            }
            if (strcasecmpC(first, "savepoint") == 0 && (nargs == 1 || nargs == 2)) {
                val filename = CharArray(512)
                val dmy = CharArray(8)
                strcpyC(filename, "savepoint ")
                if (nargs == 2) {
                    strcatC(filename, cstringAt(v1d, iargs[1]))
                    strcatC(filename, ";")
                } else strcatC(filename, " default;")
                svPtr.tpx = sql(SAVEPOINT, svPtr, EP_EMPTY, filename, dmy, XTAB).toLong()
                writeCstring(bd, svPtr.tpx.toString())
                lab = ZLbl.RET
                continue@sm
            }
            if (strcasecmpC(first, "rollback") == 0 && (nargs == 1 || nargs == 2)) {
                val filename = CharArray(512)
                val dmy = CharArray(8)
                strcpyC(filename, "rollback to ")
                if (nargs == 2) {
                    strcatC(filename, cstringAt(v1d, iargs[1]))
                    strcatC(filename, ";")
                } else strcatC(filename, " default;")
                svPtr.tpx = sql(ROLLBACK_TRANSACTION, svPtr, EP_EMPTY, filename, dmy, XTAB).toLong()
                writeCstring(bd, svPtr.tpx.toString())
                lab = ZLbl.RET
                continue@sm
            }
            if (strcasecmpC(first, "pragma") == 0 && nargs == 2) {
                val filename = CharArray(512)
                val dmy = CharArray(1024)
                strcpyC(filename, "pragma ")
                strcatC(filename, cstringAt(v1d, iargs[1]))
                strcatC(filename, ";")
                svPtr.tpx = sql(PRAGMA, svPtr, EP_EMPTY, filename, dmy, XTAB).toLong()
                if (strlenC(dmy) > 0) strcpyC(bd, cstringAt(dmy)) // result returned
                else writeCstring(bd, svPtr.tpx.toString())
                lab = ZLbl.RET
                continue@sm
            }
            if (strcasecmpC(first, "sql") == 0 && nargs == 2) {
                val filename = CharArray(512)
                val dmy = CharArray(1024)
                strcpyC(filename, cstringAt(v1d, iargs[1]))
                strcatC(filename, ";")
                svPtr.tpx = sql(SQLCMD, svPtr, EP_EMPTY, filename, dmy, XTAB).toLong()
                if (strlenC(dmy) > 0) strcpyC(bd, cstringAt(dmy)) // result returned
                else writeCstring(bd, svPtr.tpx.toString())
                lab = ZLbl.RET
                continue@sm
            }
            System.err.printf("sqlite arguments not recognized\n")
            lab = ZLbl.err
        }

        ZLbl.zsrand -> {
            // vendor 1053-1058
            if (nargs != 1) { lab = ZLbl.err; continue@sm }
            i = atoiC(v1d, 1).toLong()
            srandC(i.toInt())
            lab = ZLbl.RET
        }

        ZLbl.zsmithwaterman -> {
            // vendor 1061-1073
            if (nargs != 7) { lab = ZLbl.err; continue@sm }
            val res = SmithWaterman(svPtr, EP_EMPTY,
                tail(v1d, 1), tail(v1d, iargs[1]), tail(v1d, iargs[2]),
                tail(v1d, iargs[3]), tail(v1d, iargs[4]),
                tail(v1d, iargs[5]), tail(v1d, iargs[6]))
            strcpyC(bd, cstringAt(res))
            lab = ZLbl.RET
        }

        ZLbl.znoblanks -> {
            // vendor 1076-1084
            if (nargs != 1) { lab = ZLbl.err; continue@sm }
            var jj = 0
            var ii = 1
            while (v1d[ii] != NUL) {
                if (v1d[ii] != ' ') bd[jj++] = v1d[ii]
                ii++
            }
            bd[jj] = NUL
            lab = ZLbl.RET
        }

        ZLbl.zstopinit -> {
            // vendor 1087-1090
            if (nargs != 1) { lab = ZLbl.err; continue@sm }
            StopInit(svPtr, EP_EMPTY, tail(v1d, 1))
            lab = ZLbl.RET
        }

        ZLbl.zsyninit -> {
            // vendor 1093-1096
            if (nargs != 1) { lab = ZLbl.err; continue@sm }
            SynInit(svPtr, EP_EMPTY, tail(v1d, 1))
            lab = ZLbl.RET
        }

        ZLbl.zbmgsearch -> {
            // vendor 1099-1102
            if (nargs != 2) { lab = ZLbl.err; continue@sm }
            val res = BMGSearch(svPtr, EP_EMPTY, tail(v1d, 1), tail(v1d, iargs[1]))
            strcpyC(bd, cstringAt(res))
            lab = ZLbl.RET
        }

        ZLbl.zshred -> {
            // vendor 1105-1108
            if (nargs != 2) { lab = ZLbl.err; continue@sm }
            val res = Shred(svPtr, EP_EMPTY, tail(v1d, 1), tail(v1d, iargs[1]))
            strcpyC(bd, res)
            lab = ZLbl.RET
        }

        ZLbl.zshredquery -> {
            // vendor 1111-1114
            if (nargs != 2) { lab = ZLbl.err; continue@sm }
            val res = ShredQuery(svPtr, EP_EMPTY, tail(v1d, 1), tail(v1d, iargs[1]))
            strcpyC(bd, res)
            lab = ZLbl.RET
        }

        ZLbl.zstoplookup -> {
            // vendor 1117-1123
            if (nargs != 1) { lab = ZLbl.err; continue@sm }
            if (StopLookup(svPtr, EP_EMPTY, tail(v1d, 1)).isNotEmpty()) bd[0] = '1'
            else bd[0] = '0'
            bd[1] = NUL
            lab = ZLbl.RET
        }

        ZLbl.zsynlookup -> {
            // vendor 1126-1129
            if (nargs != 1) { lab = ZLbl.err; continue@sm }
            val res = SynLookup(svPtr, EP_EMPTY, tail(v1d, 1))
            strcpyC(bd, res)
            lab = ZLbl.RET
        }

        ZLbl.zzavg -> {
            // vendor 1132-1142: ServiceInterface relay
            if (nargs != 1) { lab = ZLbl.err; continue@sm }
            val tmp1 = CharArray(STR_MAX)
            tmp1[0] = NUL
            tmp2[0] = NUL
            strcpyC(tmp1, cstringAt(v1d, 1))
            ServiceInterface(null, 0, null, 0, tmp3, 0, tmp1, 0, tmp2, 0,
                AVG_C.toChar(), svPtr)
            strcpyC(bd, cstringAt(tmp3))
            lab = ZLbl.RET
        }

        ZLbl.zgetenv -> {
            // vendor 1145-1156
            if (nargs != 1) { lab = ZLbl.err; continue@sm }
            val tmp1 = CharArray(STR_MAX)
            tmp1[0] = NUL
            tmp2[0] = NUL
            strcpyC(tmp1, cstringAt(v1d, 1))
            ServiceInterface(null, 0, null, 0, tmp3, 0, tmp1, 0, tmp2, 0,
                GETENV_C.toChar(), svPtr)
            strcpyC(bd, cstringAt(tmp3))
            lab = ZLbl.RET
        }

        ZLbl.zzcount -> {
            // vendor 1159-1170
            if (nargs != 1) { lab = ZLbl.err; continue@sm }
            val tmp1 = CharArray(STR_MAX)
            tmp1[0] = NUL
            tmp2[0] = NUL
            strcpyC(tmp1, cstringAt(v1d, 1))
            ServiceInterface(null, 0, null, 0, tmp3, 0, tmp1, 0, tmp2, 0,
                COUNT_C.toChar(), svPtr)
            strcpyC(bd, cstringAt(tmp3))
            lab = ZLbl.RET
        }

        ZLbl.zzmax -> {
            // vendor 1172-1183
            if (nargs != 1) { lab = ZLbl.err; continue@sm }
            val tmp1 = CharArray(STR_MAX)
            tmp1[0] = NUL
            tmp2[0] = NUL
            strcpyC(tmp1, cstringAt(v1d, 1))
            ServiceInterface(null, 0, null, 0, tmp3, 0, tmp1, 0, tmp2, 0,
                MAX_C.toChar(), svPtr)
            strcpyC(bd, cstringAt(tmp3))
            lab = ZLbl.RET
        }

        ZLbl.zzmin -> {
            // vendor 1185-1196
            if (nargs != 1) { lab = ZLbl.err; continue@sm }
            val tmp1 = CharArray(STR_MAX)
            tmp1[0] = NUL
            tmp2[0] = NUL
            strcpyC(tmp1, cstringAt(v1d, 1))
            ServiceInterface(null, 0, null, 0, tmp3, 0, tmp1, 0, tmp2, 0,
                MIN_C.toChar(), svPtr)
            strcpyC(bd, cstringAt(tmp3))
            lab = ZLbl.RET
        }

        ZLbl.zzsum -> {
            // vendor 1198-1209
            if (nargs != 1) { lab = ZLbl.err; continue@sm }
            val tmp1 = CharArray(STR_MAX)
            tmp1[0] = NUL
            tmp2[0] = NUL
            strcpyC(tmp1, cstringAt(v1d, 1))
            ServiceInterface(null, 0, null, 0, tmp3, 0, tmp1, 0, tmp2, 0,
                SUM_C.toChar(), svPtr)
            strcpyC(bd, cstringAt(tmp3))
            lab = ZLbl.RET
        }

        ZLbl.zzscanalnum -> {
            // vendor 1211-1226
            if (nargs != 0 && nargs != 1) { lab = ZLbl.err; continue@sm }
            val tmp1 = CharArray(STR_MAX)
            tmp1[0] = NUL
            tmp2[0] = NUL
            ServiceInterface(null, 0, null, 0, tmp3, 0, tmp1, 0, tmp2, 0,
                SCANALNUM_C.toChar(), svPtr)
            strcpyC(bd, cstringAt(tmp3))
            if (nargs == 1) {
                i = atolC(v1d, iargs[1]).toLong()
                bd[i.toInt()] = NUL
            }
            lab = ZLbl.RET
        }

        ZLbl.zzscan -> {
            // vendor 1229-1244
            if (nargs != 0 && nargs != 1) { lab = ZLbl.err; continue@sm }
            val tmp1 = CharArray(STR_MAX)
            tmp1[0] = NUL
            tmp2[0] = NUL
            ServiceInterface(null, 0, null, 0, tmp3, 0, tmp1, 0, tmp2, 0,
                SCAN_C.toChar(), svPtr)
            strcpyC(bd, cstringAt(tmp3))
            if (nargs == 1) { // truncation length
                i = atolC(v1d, iargs[1]).toLong()
                bd[i.toInt()] = NUL
            }
            lab = ZLbl.RET
        }

        ZLbl.zzinput -> {
            // vendor 1247-1258
            if (nargs != 1) { lab = ZLbl.err; continue@sm }
            val tmp1 = CharArray(STR_MAX)
            tmp1[0] = NUL
            tmp2[0] = NUL
            strcpyC(tmp1, cstringAt(v1d, 1))
            ServiceInterface(null, 0, null, 0, tmp3, 0, tmp1, 0, tmp2, 0,
                INPUT_C.toChar(), svPtr)
            strcpyC(bd, cstringAt(tmp3))
            lab = ZLbl.RET
        }

        ZLbl.zzcosine -> {
            // vendor 1260-1272
            if (nargs != 2) { lab = ZLbl.err; continue@sm }
            val tmp1 = CharArray(STR_MAX)
            tmp1[0] = NUL
            tmp2[0] = NUL
            strcpyC(tmp1, cstringAt(v1d, 1))
            strcpyC(tmp2, cstringAt(v1d, iargs[1]))
            ServiceInterface(null, 0, null, 0, tmp3, 0, tmp1, 0, tmp2, 0,
                COSINE_C.toChar(), svPtr)
            strcpyC(bd, cstringAt(tmp3))
            lab = ZLbl.RET
        }

        ZLbl.zzjaccard -> {
            // vendor 1274-1286
            if (nargs != 2) { lab = ZLbl.err; continue@sm }
            val tmp1 = CharArray(STR_MAX)
            tmp1[0] = NUL
            tmp2[0] = NUL
            strcpyC(tmp1, cstringAt(v1d, 1))
            strcpyC(tmp2, cstringAt(v1d, iargs[1]))
            ServiceInterface(null, 0, null, 0, tmp3, 0, tmp1, 0, tmp2, 0,
                JACCARD_C.toChar(), svPtr)
            strcpyC(bd, cstringAt(tmp3))
            lab = ZLbl.RET
        }

        ZLbl.zzsim1 -> {
            // vendor 1288-1300
            if (nargs != 2) { lab = ZLbl.err; continue@sm }
            val tmp1 = CharArray(STR_MAX)
            tmp1[0] = NUL
            tmp2[0] = NUL
            strcpyC(tmp1, cstringAt(v1d, 1))
            strcpyC(tmp2, cstringAt(v1d, iargs[1]))
            ServiceInterface(null, 0, null, 0, tmp3, 0, tmp1, 0, tmp2, 0,
                SIM1_C.toChar(), svPtr)
            strcpyC(bd, cstringAt(tmp3))
            lab = ZLbl.RET
        }

        ZLbl.zztranspose -> {
            // vendor 1302-1314
            if (nargs != 2) { lab = ZLbl.err; continue@sm }
            val tmp1 = CharArray(STR_MAX)
            tmp1[0] = NUL
            tmp2[0] = NUL
            strcpyC(tmp1, cstringAt(v1d, 1))
            strcpyC(tmp2, cstringAt(v1d, iargs[1]))
            ServiceInterface(null, 0, null, 0, tmp3, 0, tmp1, 0, tmp2, 0,
                TRANSPOSE_C.toChar(), svPtr)
            strcpyC(bd, cstringAt(tmp3))
            lab = ZLbl.RET
        }

        ZLbl.zzsoundex -> {
            // vendor 1316-1395
            if (nargs != 1) { lab = ZLbl.err; continue@sm }
            bd[0] = v1d[1]
            j = 1
            i = 2
            while (v1d[i.toInt()] != NUL) {
                if (!isalphaC(v1d[i.toInt()])) {
                    i++
                    continue
                }
                v1d[i.toInt()] = tolowerC(v1d[i.toInt()])
                if (v1d[i.toInt()] == 'a' || v1d[i.toInt()] == 'e' ||
                    v1d[i.toInt()] == 'h' || v1d[i.toInt()] == 'i' ||
                    v1d[i.toInt()] == 'o' || v1d[i.toInt()] == 'w' ||
                    v1d[i.toInt()] == 'u' || v1d[i.toInt()] == 'y') {
                    i++
                    continue
                }
                if (bd[j - 1] != v1d[i.toInt()]) bd[j++] = v1d[i.toInt()]
                i++
            }
            bd[j] = NUL
            i = 1
            while (bd[i.toInt()] != NUL) {
                when (bd[i.toInt()]) {
                    'b', 'f', 'p', 'v' -> bd[i.toInt()] = '1'
                    'c', 'g', 'j', 'k', 'q', 's', 'x', 'z' -> bd[i.toInt()] = '2'
                    'd', 't' -> bd[i.toInt()] = '3'
                    'l' -> bd[i.toInt()] = '4'
                    'm', 'n' -> bd[i.toInt()] = '5'
                    'r' -> bd[i.toInt()] = '6'
                    else -> { /* vendor: no default arm */ }
                }
                i++
            }
            bd[4] = NUL
            lab = ZLbl.RET
        }

        ZLbl.zzmultiply -> {
            // vendor 1397-1410
            if (nargs != 3) { lab = ZLbl.err; continue@sm }
            val tmp1 = CharArray(STR_MAX)
            tmp1[0] = NUL
            tmp2[0] = NUL
            tmp3[0] = NUL
            val tmp4b = CharArray(STR_MAX)
            strcpyC(tmp1, cstringAt(v1d, 1))
            strcpyC(tmp2, cstringAt(v1d, iargs[1]))
            strcpyC(tmp3, cstringAt(v1d, iargs[2]))
            ServiceInterface(tmp3, 0, null, 0, tmp4b, 0, tmp1, 0, tmp2, 0,
                MMULTIPLY_C.toChar(), svPtr)
            strcpyC(bd, cstringAt(tmp4b))
            lab = ZLbl.RET
        }

        ZLbl.zzcentroid -> {
            // vendor 1412-1424
            if (nargs != 2) { lab = ZLbl.err; continue@sm }
            val tmp1 = CharArray(STR_MAX)
            tmp1[0] = NUL
            tmp2[0] = NUL
            strcpyC(tmp1, cstringAt(v1d, 1))
            strcpyC(tmp2, cstringAt(v1d, iargs[1]))
            ServiceInterface(null, 0, null, 0, tmp3, 0, tmp1, 0, tmp2, 0,
                CENTROID_C.toChar(), svPtr)
            strcpyC(bd, cstringAt(tmp3))
            lab = ZLbl.RET
        }

        ZLbl.zztermcorrelate -> {
            // vendor 1426-1438
            if (nargs != 2) { lab = ZLbl.err; continue@sm }
            val tmp1 = CharArray(STR_MAX)
            tmp1[0] = NUL
            tmp2[0] = NUL
            strcpyC(tmp1, cstringAt(v1d, 1))
            strcpyC(tmp2, cstringAt(v1d, iargs[1]))
            ServiceInterface(null, 0, null, 0, tmp3, 0, tmp1, 0, tmp2, 0,
                TERMCORRELATE_C.toChar(), svPtr)
            strcpyC(bd, cstringAt(tmp3))
            lab = ZLbl.RET
        }

        ZLbl.zzdoccorrelate -> {
            // vendor 1440-1454
            if (nargs != 4) { lab = ZLbl.err; continue@sm }
            val tmp1 = CharArray(STR_MAX)
            tmp2[0] = NUL
            val tmp2a = CharArray(STR_MAX)
            val tmp2b = CharArray(STR_MAX)
            tmp1[0] = NUL
            strcpyC(tmp1, cstringAt(v1d, 1))
            strcpyC(tmp2, cstringAt(v1d, iargs[1]))
            strcpyC(tmp2a, cstringAt(v1d, iargs[2]))
            strcpyC(tmp2b, cstringAt(v1d, iargs[3]))
            ServiceInterface(tmp2a, 0, tmp2b, 0, tmp3, 0, tmp1, 0, tmp2, 0,
                DOCCORRELATE_C.toChar(), svPtr)
            strcpyC(bd, cstringAt(tmp3))
            lab = ZLbl.RET
        }

        ZLbl.zzidf -> {
            // vendor 1456-1468: $zzIDF rides INVDF
            if (nargs != 2) { lab = ZLbl.err; continue@sm }
            val tmp1 = CharArray(STR_MAX)
            tmp1[0] = NUL
            tmp2[0] = NUL
            strcpyC(tmp1, cstringAt(v1d, 1))
            strcpyC(tmp2, cstringAt(v1d, iargs[1]))
            ServiceInterface(null, 0, null, 0, tmp3, 0, tmp1, 0, tmp2, 0,
                INVDF_C.toChar(), svPtr)
            strcpyC(bd, cstringAt(tmp3))
            lab = ZLbl.RET
        }

        ZLbl.zzdice -> {
            // vendor 1470-1482
            if (nargs != 2) { lab = ZLbl.err; continue@sm }
            val tmp1 = CharArray(STR_MAX)
            tmp1[0] = NUL
            tmp2[0] = NUL
            strcpyC(tmp1, cstringAt(v1d, 1))
            strcpyC(tmp2, cstringAt(v1d, iargs[1]))
            ServiceInterface(null, 0, null, 0, tmp3, 0, tmp1, 0, tmp2, 0,
                DICE_C.toChar(), svPtr)
            strcpyC(bd, cstringAt(tmp3))
            lab = ZLbl.RET
        }

        ZLbl.zflush -> {
            // vendor 1484-1487: flush native btree records to disk
            Mglobal(FLUSH, tmp2, 0, tmp3, svPtr)
            strcpyC(bd, "")
            lab = ZLbl.RET
        }

        ZLbl.zchdir -> {
            // vendor 1489-1496
            if (nargs != 1) { lab = ZLbl.err; continue@sm }
            i = chdirC(cstringAt(v1d, 1)).toLong()
            if (i == 0L) strcpyC(bd, "0") else strcpyC(bd, "1")
            lab = ZLbl.RET
        }

        ZLbl.zpow -> {
            // vendor 1498-1502
            if (nargs != 2) { lab = ZLbl.err; continue@sm }
            expx(v1d, 1, v1d, iargs[1], bd, 0)
            lab = ZLbl.RET
        }

        ZLbl.zlog2 -> {
            // vendor 1504-1508
            if (nargs != 1) { lab = ZLbl.err; continue@sm }
            mpsLog2(bd, tail(v1d, 1))
            lab = ZLbl.RET
        }

        ZLbl.zlog10 -> {
            // vendor 1510-1514
            if (nargs != 1) { lab = ZLbl.err; continue@sm }
            mpsLog10(bd, tail(v1d, 1))
            lab = ZLbl.RET
        }

        ZLbl.zlog -> {
            // vendor 1516-1520
            if (nargs != 1) { lab = ZLbl.err; continue@sm }
            mpsLog(bd, tail(v1d, 1))
            lab = ZLbl.RET
        }

        ZLbl.zcosine -> {
            // vendor 1522-1526
            if (nargs != 1) { lab = ZLbl.err; continue@sm }
            mpsCos(bd, tail(v1d, 1))
            lab = ZLbl.RET
        }

        ZLbl.ztangent -> {
            // vendor 1528-1532
            if (nargs != 1) { lab = ZLbl.err; continue@sm }
            mpsTan(bd, tail(v1d, 1))
            lab = ZLbl.RET
        }

        ZLbl.zatangent -> {
            // vendor 1534-1538
            if (nargs != 1) { lab = ZLbl.err; continue@sm }
            mpsAtan(bd, tail(v1d, 1))
            lab = ZLbl.RET
        }

        ZLbl.zacosine -> {
            // vendor 1540-1544
            if (nargs != 1) { lab = ZLbl.err; continue@sm }
            mpsAcos(bd, tail(v1d, 1))
            lab = ZLbl.RET
        }

        ZLbl.zsine -> {
            // vendor 1546-1550
            if (nargs != 1) { lab = ZLbl.err; continue@sm }
            mpsSin(bd, tail(v1d, 1))
            lab = ZLbl.RET
        }

        ZLbl.zasine -> {
            // vendor 1552-1556
            if (nargs != 1) { lab = ZLbl.err; continue@sm }
            mpsAsin(bd, tail(v1d, 1))
            lab = ZLbl.RET
        }

        ZLbl.zexp -> {
            // vendor 1558-1562
            if (nargs != 1) { lab = ZLbl.err; continue@sm }
            mpsExp(bd, tail(v1d, 1))
            lab = ZLbl.RET
        }

        ZLbl.zexp2 -> {
            // vendor 1564-1568
            if (nargs != 1) { lab = ZLbl.err; continue@sm }
            mpsExp2(bd, tail(v1d, 1))
            lab = ZLbl.RET
        }

        ZLbl.zexp10 -> {
            // vendor 1570-1574
            if (nargs != 1) { lab = ZLbl.err; continue@sm }
            mpsExp10(bd, tail(v1d, 1))
            lab = ZLbl.RET
        }

        ZLbl.zhit -> {
            // vendor 1576-1583: btree hit ratio
            if (svPtr.count == 0L) strcpyC(bd, "0")
            else writeCstring(bd, fmtLF(svPtr.hit.toDouble() / svPtr.count))
            svPtr.count = 0
            svPtr.hit = 0
            lab = ZLbl.RET
        }

        ZLbl.zlower -> {
            // vendor 1585-1598: convert to lower case
            if (nargs != 1) { lab = ZLbl.err; continue@sm }
            var bdtmp = 0
            var v1dtmp = 1
            while (v1d[v1dtmp] != NUL) {
                bd[bdtmp++] = tolowerC(v1d[v1dtmp++])
            }
            bd[bdtmp] = NUL
            lab = ZLbl.RET
        }

        ZLbl.znormal, ZLbl.zalter -> {
            // vendor 1604-1628: znormal falls into zalter (the
            // label cascade kept); the trailing dead goto err
            // after the block is h013-dropped
            if (nargs != 1 && nargs != 2) { lab = ZLbl.err; continue@sm }
            j = 0
            bd[0] = NUL
            i = 1
            while (v1d[i.toInt()] != NUL) {
                if (isalnumC(v1d[i.toInt()])) bd[j++] = tolowerC(v1d[i.toInt()])
                i++
            }
            bd[j] = NUL
            if (nargs == 2) WordTrunc = atoiC(v1d, iargs[1])
            else WordTrunc = 25
            bd[WordTrunc] = NUL
            lab = ZLbl.RET
        }

        ZLbl.zstem -> {
            // vendor 1630-1651: use Stem now; remove English
            // language suffixes (vendor comment kept)
            if (nargs != 1) { lab = ZLbl.err; continue@sm }
            j = 0
            bd[0] = NUL
            i = 1
            while (v1d[i.toInt()] != NUL) {
                bd[(i - 1).toInt()] = tolowerC(v1d[i.toInt()])
                i++
            }
            bd[(i - 1).toInt()] = NUL
            wstem(bd, strlenC(bd))
            lab = ZLbl.RET
        }

        ZLbl.zseek -> {
            // vendor 1654-1678: move to designated byte
            if (nargs != 1) { lab = ZLbl.err; continue@sm }
            if (svPtr.io < 0 || svPtr.io > 5 || svPtr.in_file[svPtr.io] == null) {
                bd[0] = NUL
                lab = ZLbl.RET
                continue@sm
            }
            // h004: the static fpos_t i persists (ZfcnState.fposI);
            // h018: reference build is 64-bit - the "%lld" arm
            // renders, the sizeof==4 "%ld" arm journaled
            ZfcnState.fposI = atolC(v1d, 1).toLong()
            fsetposC(svPtr.in_file[svPtr.io], ZfcnState.fposI)
            strcpyC(bd, "0")
            lab = ZLbl.RET
        }

        ZLbl.ztell -> {
            // vendor 1680-1706: return byte offset in current file
            if (svPtr.io < 0 || svPtr.io > 5) {
                strcpyC(bd, "-1")
                lab = ZLbl.RET
                continue@sm
            }
            val p: java.io.Reader? = if (svPtr.in_file[svPtr.io] != null)
                svPtr.in_file[svPtr.io]
            else if (svPtr.out_file[svPtr.io] != null)
                null // the Writer carrier takes this branch (vendor p)
            else {
                strcpyC(bd, "-1")
                lab = ZLbl.RET
                continue@sm
            }
            val ii: Long
            if (p != null) ii = fgetposC(p)
            else ii = fgetposW(svPtr.out_file[svPtr.io] ?: error(
                "ztell: out_file slot not open")) // h012 null-carrier
            // h018: 64-bit build - the "%lld" arm renders
            writeCstring(bd, ii.toString())
            lab = ZLbl.RET
        }

        ZLbl.zword -> {
            // vendor 1719-1821: the IS&R line-buffer word parser
            // ($zws initialize / $zwi initialize+lower / $zwn next
            // word / $zwp next token; the ifdef BUF_PARSE arms are
            // unconditional in the reference build, journaled)
            if (zcode1 == 's') { // initialize
                strmove(ZfcnState.linebuf, 0, v1d, 1)
                ZfcnState.linebufx = 0
                bd[0] = NUL
                lab = ZLbl.RET
                continue@sm
            }
            if (zcode1 == 'i') { // initialize
                strmove(ZfcnState.linebuf, 0, v1d, 1)
                ZfcnState.linebufx = 0
                for (p1 in ZfcnState.linebuf.indices) {
                    if (ZfcnState.linebuf[p1] == NUL) break
                    ZfcnState.linebuf[p1] = tolowerC(ZfcnState.linebuf[p1])
                }
                bd[0] = NUL
                lab = ZLbl.RET
                continue@sm
            }
            if (tolowerC(zcode1) == 'n') {
                if (ZfcnState.linebuf[ZfcnState.linebufx] == NUL) { // empty
                    bd[0] = NUL
                    lab = ZLbl.RET
                    continue@sm
                }
                while (ZfcnState.linebuf[ZfcnState.linebufx] != NUL &&
                       ZfcnState.linebuf[ZfcnState.linebufx] == ' ') ZfcnState.linebufx++
                if (ZfcnState.linebuf[ZfcnState.linebufx] == NUL) { // end of buffer
                    bd[0] = NUL
                    lab = ZLbl.RET
                    continue@sm
                }
                i = 0
                while (ZfcnState.linebuf[ZfcnState.linebufx] == '-' ||
                       ZfcnState.linebuf[ZfcnState.linebufx] == '_') ZfcnState.linebufx++
                while (true) { // extract next word
                    bd[i.toInt()] = ZfcnState.linebuf[ZfcnState.linebufx]
                    if (ZfcnState.linebuf[ZfcnState.linebufx] == NUL ||
                        ZfcnState.linebuf[ZfcnState.linebufx] == ' ') {
                        bd[i.toInt()] = NUL
                        lab = ZLbl.RET
                        break
                    }
                    i++
                    ZfcnState.linebufx++
                }
                continue@sm
            }
            if (zcode1 == 'p') { // parse
                if (ZfcnState.linebuf[ZfcnState.linebufx] == NUL) { // empty
                    bd[0] = NUL
                    lab = ZLbl.RET
                    continue@sm
                }
                while (ZfcnState.linebuf[ZfcnState.linebufx] != NUL &&
                       ZfcnState.linebuf[ZfcnState.linebufx] == ' ') ZfcnState.linebufx++
                if (ZfcnState.linebuf[ZfcnState.linebufx] == NUL) { // end of buffer
                    bd[0] = NUL
                    lab = ZLbl.RET
                    continue@sm
                }
                if (ZfcnState.linebuf[ZfcnState.linebufx] != '*' &&
                    ZfcnState.linebuf[ZfcnState.linebufx] != '-' &&
                    ispunctC(ZfcnState.linebuf[ZfcnState.linebufx])) {
                    bd[0] = ZfcnState.linebuf[ZfcnState.linebufx]
                    bd[1] = NUL
                    ZfcnState.linebufx++
                    lab = ZLbl.RET
                    continue@sm
                }
                i = 0
                while (true) { // extract next word
                    bd[i.toInt()] = ZfcnState.linebuf[ZfcnState.linebufx]
                    if (ZfcnState.linebuf[ZfcnState.linebufx] == NUL ||
                        (ZfcnState.linebuf[ZfcnState.linebufx] != '*' &&
                         ZfcnState.linebuf[ZfcnState.linebufx] != '-' &&
                         ispunctC(ZfcnState.linebuf[ZfcnState.linebufx])) ||
                        ZfcnState.linebuf[ZfcnState.linebufx] == ' ') {
                        bd[i.toInt()] = NUL
                        lab = ZLbl.RET
                        break
                    }
                    i++
                    ZfcnState.linebufx++
                }
                continue@sm
            }
            lab = ZLbl.err
        }

        ZLbl.znumeric -> {
            // vendor 1824-1848: is string non-numeric?
            if (nargs != 1) { lab = ZLbl.err; continue@sm }
            // h013: the vendor k=0 dead store journaled
            i = 1
            while (v1d[i.toInt()] != NUL) {
                if (isdigitC(v1d[i.toInt()]) || v1d[i.toInt()] == '.' ||
                    v1d[i.toInt()] == '+' || v1d[i.toInt()] == '-') {
                    i++
                    continue
                }
                bd[0] = '0'
                bd[1] = NUL
                lab = ZLbl.RET
                continue@sm
            }
            bd[0] = '1'
            bd[1] = NUL
            lab = ZLbl.RET
        }

        ZLbl.zblnk -> {
            // vendor 1851-1883: remove leading and double blanks
            if (nargs != 1) { lab = ZLbl.err; continue@sm }
            var v1dtmp = 1
            while (v1d[v1dtmp] != NUL) {
                if (v1d[v1dtmp] != ' ') {
                    lab = ZLbl.zbl1
                    break
                }
                v1dtmp++
            }
            if (v1d[v1dtmp] == NUL && lab == ZLbl.zblnk) {
                bd[0] = NUL
                lab = ZLbl.RET
            }
        }

        ZLbl.zbl1 -> {
            // vendor 1864-1881 (the goto zbl1 target)
            var bdtmp = 0
            var v1dtmp = 1
            var bdx: Char = 0.toChar()
            while (v1d[v1dtmp] != NUL) {
                if (bdx == ' ' && v1d[v1dtmp] == ' ') { // bd has a blank?
                    v1dtmp++ // yes
                    continue
                }
                bdx = v1d[v1dtmp]
                bd[bdtmp++] = v1d[v1dtmp]
                v1dtmp++
            }
            bd[bdtmp] = NUL
            lab = ZLbl.RET
        }

        ZLbl.zcurrentfile -> {
            // vendor 1885-1895 (vendor's "currnt" comment typo kept)
            strcpyC(bd, cstringAt(svPtr.currentFile))
            lab = ZLbl.RET
        }

        ZLbl.zhtml -> {
            // vendor 1897-1930: convert codes for html transmission
            if (nargs != 1) { lab = ZLbl.err; continue@sm }
            // h013: the vendor k=1 dead store journaled
            val t1 = CharArray(10)
            val t2 = CharArray(STR_MAX)
            i = 1
            while (v1d[i.toInt()] != NUL) {
                if (v1d[i.toInt()] == ' ') v1d[i.toInt()] = '+'
                else if (!isalnumC(v1d[i.toInt()])) {
                    // vendor: sprintf(t1, "%%%2X\0", v1d[i])
                    writeCstring(t1, "%" + String.format(
                        java.util.Locale.ROOT, "%2X", v1d[i.toInt()].code))
                    v1d[i.toInt()] = NUL
                    strmove(t2, 0, v1d, 1)
                    strcatC(t2, cstringAt(t1))
                    strcatC(t2, cstringAt(v1d, (i + 1).toInt()))
                    strmove(v1d, 1, t2, 0)
                    i += 2
                }
                i++
            }
            strmove(bd, 0, v1d, 1)
            lab = ZLbl.RET
        }

        ZLbl.zcondense -> {
            // vendor 1932-1986: dump global arrays to disk.
            // h018: SQLITE IS defined - the vendor "return; // not
            // avail in sqlite" arm renders and the unreachable
            // dump walk is h013-dropped (Kotlin would warn)
            lab = ZLbl.RET
        }

        ZLbl.zrestore -> {
            // vendor 1988-2041 (same SQLITE shape, h018/h013)
            lab = ZLbl.RET
        }

        ZLbl.znxtrow -> {
            // vendor 2043-2085: printf then return; the gblstr
            // walk after the return is vendor dead code (h013,
            // journaled)
            println("\$zm not available")
            lab = ZLbl.RET
        }

        ZLbl.zabs -> {
            // vendor 2092-2100: absolute value fcn
            if (nargs != 1) { lab = ZLbl.err; continue@sm }
            if (mpsAbs(tail(v1d, 1), bd) != 0) {
                lab = ZLbl.err
                continue@sm
            }
            lab = ZLbl.RET
        }

        ZLbl.zpad -> {
            // vendor 2106-2124: pad a string with blanks
            if (nargs != 2) { lab = ZLbl.err; continue@sm }
            i = atolC(v1d, iargs[1]).toLong()
            strmove(bd, 0, v1d, 1)
            j = strlenC(v1d, 1)
            while (j < i) {
                bd[j] = ' '
                j++
            }
            bd[j] = NUL
            lab = ZLbl.RET
        }

        ZLbl.z_variable -> {
            // vendor 2126-2138: get value from symbol table
            if (nargs != 1) { lab = ZLbl.err; continue@sm }
            if (sym_(1, v1d, 1, bd, 0, svPtr) == null) bd[0] = NUL
            lab = ZLbl.RET
        }

        ZLbl.zdate -> {
            // vendor 2140-2425: the $zd1..$zd8 date surfaces.
            // h004: static time_t timex persists (ZfcnState.timex);
            // h018: reference build is 64-bit - the "%ld"/"%lld"
            // long arms render, the "%d" arms journaled
            ZfcnState.timex = System.currentTimeMillis() / 1000L

            if (zcode1 == '1') { // internal code
                writeCstring(bd, ZfcnState.timex.toString())
            } else if (zcode1 == '2') { // internal -> printable
                ZfcnState.timex = atolLongC(v1d, 1)
                writeCstring(bd, ctimeC(ZfcnState.timex))
                bd[24] = NUL
            } else if (zcode1 == '3') { // $zd3(year,month,day) -> DayOfYear
                if (nargs != 3) { lab = ZLbl.err; continue@sm }
                val year = atolLongC(v1d, 1).toInt()
                val m = atolLongC(v1d, iargs[1]).toInt()
                val d = atolLongC(v1d, iargs[2]).toInt()
                if (year < 1900 || m < 1 || m > 12 || d < 1 || d > 31) {
                    lab = ZLbl.err
                    continue@sm
                }
                val date = Tm()
                if (year > 1900) date.tmYear = year - 1900
                else date.tmYear = year
                date.tmMon = m - 1 // range is 0 to 11
                date.tmMday = d
                date.tmHour = 12
                date.tmMin = 0
                date.tmSec = 0
                date.tmYday = 0
                date.tmIsdst = 1
                mktimeC(date)
                writeCstring(bd, (date.tmYday + 1).toString()) // range is 0-365
            } else if (zcode1 == '4') { // $zd4(year,dayofyear) -> gregorian
                if (nargs != 2) { lab = ZLbl.err; continue@sm }
                var year = atolLongC(v1d, 1).toInt()
                if (year > 1900) year = year - 1900
                var d = atolLongC(v1d, iargs[1]).toInt()
                if (year < 0 || d > 366 || d < 1) { lab = ZLbl.err; continue@sm }
                d--
                var m = 0
                var dd = 1
                val date = Tm()
                while (true) {
                    date.tmYear = year
                    date.tmMon = m
                    date.tmMday = dd
                    date.tmHour = 12
                    date.tmMin = 0
                    date.tmSec = 0
                    date.tmYday = 0
                    date.tmIsdst = 1
                    mktimeC(date)
                    if (date.tmYday == d) break
                    dd++
                    if (m == 0 && dd > 31) {
                        m++
                        dd = 1
                    } // jan
                    else if (m == 1 && (year % 4) == 0) {
                        if (dd > 29) {
                            m++
                            dd = 1
                        } // feb
                    } else if (m == 1 && (year % 4) != 0) {
                        if (dd > 28) {
                            m++
                            dd = 1
                        } // feb
                    } else if (m == 2 && dd > 31) {
                        m++
                        dd = 1
                    } // mar
                    else if (m == 3 && dd > 30) {
                        m++
                        dd = 1
                    } // apr
                    else if (m == 4 && dd > 31) {
                        m++
                        dd = 1
                    } // may
                    else if (m == 5 && dd > 30) {
                        m++
                        dd = 1
                    } // jun
                    else if (m == 6 && dd > 31) {
                        m++
                        dd = 1
                    } // jul
                    else if (m == 7 && dd > 31) {
                        m++
                        dd = 1
                    } // aug
                    else if (m == 8 && dd > 30) {
                        m++
                        dd = 1
                    } // sep
                    else if (m == 9 && dd > 31) {
                        m++
                        dd = 1
                    } // oct
                    else if (m == 10 && dd > 30) {
                        m++
                        dd = 1
                    } // nov
                    else if (m == 11 && dd > 31) {
                        lab = ZLbl.err
                        continue@sm // dec
                    }
                }
                if (date.tmYear < 100) writeCstring(bd, "19" + date.tmYear +
                    " " + (date.tmMon + 1) + " " + date.tmMday)
                else writeCstring(bd, "20" + String.format(
                    java.util.Locale.ROOT, "%02d", date.tmYear - 100) +
                    " " + (date.tmMon + 1) + " " + date.tmMday)
            } else if (zcode1 == '5') {
                // $zd5(year,mn,dy) -> yr,dy,dw (days since sunday)
                if (nargs != 3) { lab = ZLbl.err; continue@sm }
                val year = atolLongC(v1d, 1).toInt()
                val m = atolLongC(v1d, iargs[1]).toInt()
                val d = atolLongC(v1d, iargs[2]).toInt()
                if (year < 1900 || m < 1 || m > 12 || d < 1 || d > 31) {
                    lab = ZLbl.err
                    continue@sm
                }
                val date = Tm()
                if (year > 1900) date.tmYear = year - 1900
                else date.tmYear = year
                date.tmMon = m - 1 // range is 0 to 11
                date.tmMday = d
                date.tmHour = 12
                date.tmMin = 0
                date.tmSec = 0
                date.tmYday = 0
                date.tmWday = 0
                date.tmIsdst = 0
                mktimeC(date)
                if (date.tmYear < 100) writeCstring(bd, "19" + date.tmYear +
                    "," + (date.tmYday + 1) + "," + date.tmWday)
                else writeCstring(bd, "20" + String.format(
                    java.util.Locale.ROOT, "%02d", date.tmYear - 100 - 1) +
                    "," + (date.tmYday + 2) + "," + date.tmWday)
            } else if (zcode1 == '6') { // $zd6 -> HH:SS
                writeCstring(tmp2, ctimeC(ZfcnState.timex))
                strmove(bd, 0, tmp2, 11)
                bd[5] = NUL
            } else if (zcode1 == '7' || zcode1 == '8') { // current as year,month,day
                if (nargs == 1) ZfcnState.timex = atolLongC(v1d, 1)
                writeCstring(tmp2, ctimeC(ZfcnState.timex))
                bd[1] = tmp2[20]
                bd[2] = tmp2[21]
                bd[3] = tmp2[22]
                bd[4] = tmp2[23]
                bd[5] = '-'
                tmp2[7] = NUL
                if (strcmpC(tmp2, 4, "Jan") == 0) {
                    bd[6] = '0'; bd[7] = '1'
                } else if (strcmpC(tmp2, 4, "Feb") == 0) {
                    bd[6] = '0'; bd[7] = '2'
                } else if (strcmpC(tmp2, 4, "Mar") == 0) {
                    bd[6] = '0'; bd[7] = '3'
                } else if (strcmpC(tmp2, 4, "Apr") == 0) {
                    bd[6] = '0'; bd[7] = '4'
                } else if (strcmpC(tmp2, 4, "May") == 0) {
                    bd[6] = '0'; bd[7] = '5'
                } else if (strcmpC(tmp2, 4, "Jun") == 0) {
                    bd[6] = '0'; bd[7] = '6'
                } else if (strcmpC(tmp2, 4, "Jul") == 0) {
                    bd[6] = '0'; bd[7] = '7'
                } else if (strcmpC(tmp2, 4, "Aug") == 0) {
                    bd[6] = '0'; bd[7] = '8'
                } else if (strcmpC(tmp2, 4, "Sep") == 0) {
                    bd[6] = '0'; bd[7] = '9'
                } else if (strcmpC(tmp2, 4, "Oct") == 0) {
                    bd[6] = '1'; bd[7] = '0'
                } else if (strcmpC(tmp2, 4, "Nov") == 0) {
                    bd[6] = '1'; bd[7] = '1'
                } else if (strcmpC(tmp2, 4, "Dec") == 0) {
                    bd[6] = '1'; bd[7] = '2'
                }
                bd[8] = '-'
                bd[9] = tmp2[8]
                bd[10] = tmp2[9]
                bd[11] = NUL
                j = 0
                while (j < 11) {
                    if (bd[j] == ' ') bd[j] = '0'
                    j++
                }
                if (zcode1 == '8') {
                    bd[11] = ','
                    strmove(bd, 12, tmp2, 11)
                    bd[17] = NUL
                }
                strmove(bd, 0, bd, 1)
            } else {
                if (nargs != 0) { lab = ZLbl.err; continue@sm }
                writeCstring(bd, ctimeC(ZfcnState.timex))
                bd[24] = NUL
            }
            lab = ZLbl.RET
        }

        ZLbl.zfile -> {
            // vendor 2444-2456: does file exist?
            val ffd = openExistsC(cstringAt(v1d, 1))
            if (ffd == -1) {
                bd[0] = '0'
                bd[1] = NUL
                lab = ZLbl.RET
            } else {
                bd[0] = '1'
                bd[1] = NUL
                lab = ZLbl.RET
            }
        }

        ZLbl.zglobal -> {
            // vendor 2470-2482: file system status
            // h018: SQLITE IS defined - the ServiceInterface arm
            // renders, the strcpy("") arm journaled
            if (nargs != 1) { lab = ZLbl.err; continue@sm }
            val tmp1 = CharArray(STR_MAX)
            val tmp2g = CharArray(STR_MAX)
            val tmp3g = CharArray(STR_MAX)
            tmp1[0] = NUL
            tmp2g[0] = NUL
            strmove(tmp1, 0, v1d, 1)
            ServiceInterface(null, 0, null, 0, tmp3g, 0, tmp1, 0, tmp2g, 0,
                GBLNME_C.toChar(), svPtr)
            strmove(bd, 0, tmp3g, 0)
            lab = ZLbl.RET
        }

        ZLbl.zroot -> {
            // vendor 2495-2498: square root
            if (nargs != 1) { lab = ZLbl.err; continue@sm }
            mpsSqrt(bd, tail(v1d, 1))
            lab = ZLbl.RET
        }

        ZLbl.zsystem -> {
            // vendor 2511-2516: spawn shell and exec command
            if (nargs != 1) { lab = ZLbl.err; continue@sm }
            val x = systemC(cstringAt(v1d, 1))
            writeCstring(bd, x.toString())
            lab = ZLbl.RET
        }

        ZLbl.fcn_not_found -> {
            // vendor 2530-2538
            if (svPtr.NOERR != 0) {
                svPtr.NOERR = -1
                lab = ZLbl.RET
            } else {
                printfC("\n*** Z function: \"" + fn + "\" not found in or near line " +
                    svPtr.LineNumber)
                sigint(100)
            }
            continue@sm
        }

        ZLbl.err -> {
            // vendor 2540-2546
            if (svPtr.NOERR != 0) {
                svPtr.NOERR = -1
                lab = ZLbl.RET
            } else {
                System.err.printf(
                    "\n*** zFunction error in or near line %d\n", svPtr.LineNumber)
                sigint(100)
            }
            continue@sm
        }

        ZLbl.RET -> {
            // h001 write-back: v1d window (origLen bytes) and bd
            // (NUL-terminated, min(origLen+1) window)
            for (q in 0..origLen) v1dP[v1dOff + q] = v1d[q]
            val bl = strlenC(bd, 0)
            for (q in 0..bl) bdP[bdOff + q] = bd[q]
            return
        }
    }
}

private fun printfC(s: String) {
    // h014 OS-layer carrier: vendor printf to stdout
    print(s + "\n")
}

/** h012: vendor strcmp over the byte domain (buffer + offset). */
private fun strcmpC(b: CharArray, off: Int, s: String): Int {
    for (k in s.indices) {
        val bc = b[off + k]
        if (bc != s[k]) return bc.code - s[k].code
        if (bc == NUL) return 0
    }
    return if (b[off + s.length] == NUL) 0 else b[off + s.length].code
}

/** h012: vendor strncat(dst, src, n) - append at most n-1 chars. */
private fun strncatC(dst: CharArray, s: String, n: Int) {
    var k = 0
    while (dst[k] != NUL) k++
    var m = 0
    while (m < n - 1 && m < s.length) {
        dst[k++] = s[m]
        m++
    }
    dst[k] = NUL
}

/** h012: vendor atol over the byte domain. */
private fun atolC(b: CharArray, off: Int = 0): Int {
    var k = off
    while (b[k] == ' ') k++
    var neg = false
    if (b[k] == '+' || b[k] == '-') {
        neg = b[k] == '-'
        k++
    }
    var v = 0L
    while (b[k] in '0'..'9') {
        v = v * 10 + (b[k].code - 48)
        k++
    }
    return (if (neg) -v else v).toInt()
}

/** h012: vendor atol on time_t (h018: 64-bit build). */
private fun atolLongC(b: CharArray, off: Int = 0): Long {
    var k = off
    while (b[k] == ' ') k++
    var neg = false
    if (b[k] == '+' || b[k] == '-') {
        neg = b[k] == '-'
        k++
    }
    var v = 0L
    while (b[k] in '0'..'9') {
        v = v * 10 + (b[k].code - 48)
        k++
    }
    return if (neg) -v else v
}

/** h012: vendor atof over the byte domain. */
private fun atofC(b: CharArray, off: Int = 0): Double =
    cstringAt(b, off).trim().toDouble()

/** h012: vendor isalnum over the byte domain. */
private fun isalnumC(c: Char): Boolean =
    c in '0'..'9' || c.code in 65..90 || c.code in 97..122

/** h012: vendor isdigit over the byte domain. */
private fun isdigitC(c: Char): Boolean = c in '0'..'9'

/** h012: vendor ispunct over the byte domain (ASCII 33-47,
 *  58-64, 91-96, 123-126; the 91-96 band includes the backtick). */
private fun ispunctC(c: Char): Boolean {
    val v = c.code
    return (v in 33..47) || (v in 58..64) || (v in 91..96) || (v in 123..126)
}

/** h001/h012: fresh NUL-terminated CharArray for the C-string at
 *  buf+off (many seats take a buffer at index 0). */
private fun tail(buf: CharArray, off: Int): CharArray {
    var k = 0
    while (buf[off + k] != NUL) k++
    val out = CharArray(k + 1)
    for (m in 0..k) out[m] = buf[off + m]
    return out
}

/** h012: sprintf(out, "%f", x) carrier (detekt: explicit locale). */
private fun fmtF(x: Double): String =
    String.format(java.util.Locale.ROOT, "%f", x)

/** h012: sprintf(out, "%lf", x) carrier (same "%f" result). */
private fun fmtLF(x: Double): String =
    String.format(java.util.Locale.ROOT, "%f", x)

/** h012: vendor struct tm carrier (per-file; the date fcns are
 *  the only users in this unit). */
private class Tm {
    var tmSec = 0
    var tmMin = 0
    var tmHour = 0
    var tmMday = 1
    var tmMon = 0
    var tmYear = 0
    var tmWday = 0
    var tmYday = 0
    var tmIsdst = 0
}

/** h012/h014: vendor mktime - normalize the broken-down time and
 *  set tmYday/tmWday (java.util.Calendar carrier; DAY_OF_YEAR is
 *  1-based vs tm_yday 0-based, DAY_OF_WEEK SUNDAY==1 vs tm_wday 0). */
private fun mktimeC(date: Tm): Long {
    val cal = java.util.Calendar.getInstance()
    cal.isLenient = true
    cal.set(date.tmYear + 1900, date.tmMon, date.tmMday,
        date.tmHour, date.tmMin, date.tmSec)
    date.tmYday = cal.get(java.util.Calendar.DAY_OF_YEAR) - 1
    date.tmWday = cal.get(java.util.Calendar.DAY_OF_WEEK) - 1
    return cal.timeInMillis / 1000L
}

/** h012/h014: vendor ctime - "Www Mmm dd hh:mm:ss yyyy\n" (day
 *  space-padded to width 2, C locale). */
private fun ctimeC(t: Long): String {
    val wd = arrayOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
    val mo = arrayOf("Jan", "Feb", "Mar", "Apr", "May", "Jun",
        "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
    val cal = java.util.Calendar.getInstance()
    cal.timeInMillis = t * 1000L
    val dd = String.format(java.util.Locale.ROOT, "%2d",
        cal.get(java.util.Calendar.DAY_OF_MONTH))
    return wd[cal.get(java.util.Calendar.DAY_OF_WEEK) - 1] + " " +
        mo[cal.get(java.util.Calendar.MONTH)] + " " + dd + " " +
        String.format(java.util.Locale.ROOT, "%02d:%02d:%02d %d\n",
            cal.get(java.util.Calendar.HOUR_OF_DAY),
            cal.get(java.util.Calendar.MINUTE),
            cal.get(java.util.Calendar.SECOND),
            cal.get(java.util.Calendar.YEAR))
}

/** h014 OS-layer carriers (u161 precedent): fsetpos/fgetpos on the
 *  Reader edge, fgetpos on the Writer edge. */
private fun fsetposC(r: java.io.Reader?, pos: Long) {
    TODO("h014 stub: C library fsetpos (not a corpus unit; Reader edge)")
}

private fun fgetposC(r: java.io.Reader): Long {
    TODO("h014 stub: C library fgetpos (not a corpus unit; Reader edge)")
}

private fun fgetposW(w: java.io.Writer): Long {
    TODO("h014 stub: C library fgetpos (not a corpus unit; Writer edge)")
}

/** h014: vendor open(filename, 0) existence probe -> fd or -1. */
private fun openExistsC(name: String): Int =
    if (java.io.File(name).exists()) 3 else -1
