package doc

// Unit 184 (batch-6 big three): interp-zfcns.cpp::zfcn (batch 6
// pending; core-adjacent stratum; physLOC 1662, score and rank ride
// the units.csv row). The Mumps intrinsic Z-function dispatcher of
// the interp-zfcns TU: the ~75-label goto ladder over the same
// bifs-family arms (GTK glue, zz text-metrics services, mps
// decimal-family trig, zdate family, zsqlite transaction controls,
// file-ish helpers) plus the SQLITE-only zindex/zdb introspection
// arms and the zglobal GBLNME relay.
//
// TU-LEVEL H-017 NOTE: the vendor Makefile compiles interp-zfcns.o
// (NOT interp-bifs.o) into libmumps.a, so THIS unit is the LIVE
// reference-build TU: it keeps the vendor C symbol zfcn, renders
// it as fun zfcn, and owns the Stubs.zfcn seat flip; the bifs twin
// (unit 183) keeps its vendor C symbol but renders it as zfcnBifs
// (the build-alternate TU, the zfcns.h _INTERP_ selector analog),
// so both renders live in the same package.
//
// H-011: the goto ladder renders as the ZLblZ enum state machine
// below (label per goto target, one when-arm per vendor label, one
// continue@machine per goto, plain return per C return). znormal:
// and zalter: are the same label-sport in the vendor (adjacent
// labels) and share one arm here; the zbl1 label renders inline in
// the zblnk arm (an intra-arm goto quantized to structured flow,
// the only non-machine goto of the body).
//
// H-004: the five C statics render as the private object
// ZfcnsState (linebuf/linebufx the zword parse state; the GTK
// iter holders; the zseek fpos carrier; the srand seed store).
// iter10 and the column scalar are dead in this TU (grep-verified:
// column is never read or written past its initializer) and drop
// per H-013 with this note; t/spin are static in the vendor but are
// written and read within a single arm (no cross-call state), so
// they render as arm locals per the H-004 liveness rule.
//
// H-018: the reference build defines SQLITE, so the #if defined
// (SQLITE) arms are live and the #else arms are dead; the dead
// zcondense/zrestore dump/load bodies (each a whole #if-block
// return-first under SQLITE), the znxtrow dead body after the
// banner return, the GnuDos zd4 stub arm, and the znative #else
// arm drop per H-013 with this note. srand's seed stores but rand()
// is never called anywhere in this TU (grep-verified), so the store
// renders without a rand seat.
//
// H-014: the out-of-scope C seats ride the Stubs object (the GTK
// library glue family shared with the bifs twin, Stubs.mps floor/
// ceil/trunc/log/cos/tan/exp2 of the batch-9/-10 interp-arith
// roster, Stubs.ServiceInterface with the GBLNME 20 relay,
// Stubs.sql,
// Stubs.wstem, Stubs.stopLookup, Stubs.sym_, Stubs.sigint). The
// landed real renders are called directly (mpsLog2/mpsLog10/
// mpsExp/mpsExp10/mpsAbs/mpsSin/mps_asin/mps_acos/mps_atan/
// mps_sqrt, expx, Mglobal, ctime, stopInit, SynInit, SynLookup,
// BMGSearch, Shred, ShredQuery, SmithWaterman, GtkTreeAdd).
//
// locals dropped with H-013 notes (dead in TU, grep-verified):
// zcode2/zcode3 (parsed, never read), int rand() (dead prototype),
// float sq, FILE *dump (dead SQLITE arms), time_t dtime, tmp-based
// gbltbl[128]/gblstr[STR_MAX] (only the dead znxtrow body reads
// them), the k=0 of znumeric and k=1 of zhtml, and iargv scratch
// never declared. WordTrunc is live (znormal reads/writes it).
//
// H-011 consult hit-reuse (2026-11-21T18:02Z): the state machine of
// the unit-183 twin is reused whole (the H-012 same-name family
// rule; ZLblZ/ZfcnsState re-declared per-file like the vendor
// per-TU labels/statics).
//
// DELTA LEDGER vs the unit-183 bifs twin (the vendor dump diff is
// exactly these; everything else line-identical): (1) this TU has
// no zreplace arm and no zreplace dispatch line (grep-verified
// absent in the vendor source; Stubs.Replace stays a bifs-only
// seat); (2) the commented-out zcase=zglobal goto zfiletest fossil
// line rides right after the live zfiletest dispatch line (kept as
// this ledger note, the dead comment itself drops); (3) five
// SQLITE-only arms zindexsize/zdatasize/zdbname/zdbfile/zindexmax
// ride between znative and zsqlite per the vendor layout (the
// #else twins return "" and drop per H-018); (4) the generic sql
// arm passes SQLCMD (55) where bifs passes PRAGMA (29); (5) the
// zglobal arm is the ServiceInterface GBLNME (20) relay where
// bifs runs the g=5 count-empties Mglobal probe.
//
// Naming note (H-017 gate fix): private top-level funs may share a
// name across files of the package (the atoiC precedent), but
// private top-level classes may not, so this file's struct tm
// carrier renders as TmZz (the bifs twin keeps TmZb).

// shell command-substitution backtick (the H-015 scan bans the
// backtick literal; the echo-command strings carry it by concat)
private const val BT_ZB: Char = '\u0060'
private const val OPEN_ZB: Char = '\u00CE' // defines.h:169 OPEN 206 (per-file seat; H-012 rule)
private const val CLOSE_ZB: Char = '\u00CF' // defines.h:170 CLOSE 207
private const val COMMA_ZB: Char = '\u00D0' // defines.h:171 COMMA 208

// defines.h:226-244 ServiceInterface control codes (per-file seats
// of this TU; the unit-183 twin re-renders its own copies, H-012
// same-name family rule)
private const val COSINE_ZB = 1
private const val TRANSPOSE_ZB = 2
private const val INVDF_ZB = 3
private const val DICE_ZB = 4
private const val SCANALNUM_ZB = 5
private const val SCAN_ZB = 6
private const val AVG_ZB = 7
private const val SIM1_ZB = 8
private const val JACCARD_ZB = 9
private const val CENTROID_ZB = 10
private const val TERMCORRELATE_ZB = 11
private const val MAX_ZB = 13
private const val MIN_ZB = 14
private const val MMULTIPLY_ZB = 15
private const val SUM_ZB = 16
private const val DOCCORRELATE_ZB = 17
private const val GETENV_ZB = 18
private const val INPUT_ZB = 19
private const val COUNT_ZB = 12
private const val GBLNME_ZB = 20 // defines.h:245 (zglobal ServiceInterface relay)

// defines.h:95-99 sql codes (per-file seats of this TU)
private const val BEGIN_TRANSACTION_ZB = 25
private const val COMMIT_TRANSACTION_ZB = 26
private const val ROLLBACK_TRANSACTION_ZB = 27
private const val SAVEPOINT_ZB = 28
private const val PRAGMA_ZB = 29
private const val SQLCMD_ZB = 55 // defines.h:181 (generic sql arm; bifs twin passes PRAGMA 29)

// vendor statics, H-004 notes in the file header
private object ZfcnsState {
    // static char linebuf[STR_MAX]; static int linebufx; (zword)
    val linebuf = CharArray(STR_MAX)
    var linebufx = 0

    // static GtkTreeIter iter, iter1..iter9 (the GtkTreeLevelAdd
    // column war and the tree model/selection reads); iter10 and
    // column drop per H-013 (file header note)
    val iter = IntArray(1)
    val iter1 = IntArray(1)
    val iter2 = IntArray(1)
    val iter3 = IntArray(1)
    val iter4 = IntArray(1)
    val iter5 = IntArray(1)
    val iter6 = IntArray(1)
    val iter7 = IntArray(1)
    val iter8 = IntArray(1)
    val iter9 = IntArray(1)

    // zseek static fpos_t carrier
    var fposZb: Long = 0L

    // srand seed store (rand never called, H-013 header note)
    var srandSeed: Int = 0
}

// vendor goto labels, H-011 machine states
private enum class ZLblZ {
    DISPATCH,
    GTK_DIALOG_WITH_BUTTONS, GTK_WIDGET_HIDE, GTK_WIDGET_SHOW,
    GTK_SPIN_BUTTON_GET_VALUE, GTK_SPIN_BUTTON_SET_VALUE,
    GTK_TREE_STORE_CLEAR, GTK_TREE_MODEL_GET,
    GTK_TREE_SELECTION_GET_SELECTED, GTK_TREE_VIEW_GET_SELECTION,
    GTK_TREE_LEVEL_ADD,
    GTK_TREE_LEVEL1_ADD, GTK_TREE_LEVEL2_ADD, GTK_TREE_LEVEL3_ADD,
    GTK_TREE_LEVEL4_ADD, GTK_TREE_LEVEL5_ADD, GTK_TREE_LEVEL6_ADD,
    GTK_TREE_LEVEL7_ADD, GTK_TREE_LEVEL8_ADD, GTK_TREE_LEVEL9_ADD,
    GTK_TREE_LEVEL10_ADD,
    GTK_LABEL_SET_TEXT, GTK_TEXT_BUFFER_SET_TEXT,
    GTK_ENTRY_GET_TEXT, GTK_ENTRY_SET_TEXT, GTK_GET_RANGE_ADJUSTMENT,
    GTK_FILE_CHOOSER, GTK_CALENDAR, GTK_FONT_CHOOSER, GTK_COLOR_CHOOSER,
    GTK_TOGGLE_BUTTON_SET_ACTIVE, GTK_TOGGLE_BUTTON_GET_ACTIVE,
    GTK_SWITCH_GET_ACTIVE,
    ZDATABASE, ZPROGRAM, ZFILETEST, ZBASENAME, ZFLOOR, ZCEIL, ZTRUNC,
    ZSQLOPEN, ZNATIVE, ZINDEXSIZE, ZDATASIZE, ZDBNAME, ZDBFILE,
    ZINDEXMAX, ZSQLITE, ZSRAND, ZSMITHWATERMAN,
    ZNOBLANKS, ZSTOPINIT, ZSYNINIT, ZBMGSEARCH, ZSHRED, ZSHREDQUERY,
    ZSTOPLOOKUP, ZSYNLOOKUP,
    ZZAVG, ZGETENV, ZZCOUNT, ZZMAX, ZZMIN, ZZSUM, ZZSCANALNUM, ZZSCAN,
    ZZINPUT, ZZCOSINE, ZZJACCARD, ZZSIM1, ZZTRANSPOSE, ZZSOUNDEX,
    ZZMULTIPLY, ZZCENTROID, ZZTERMCORRELATE, ZZDOCCORRELATE, ZZIDF,
    ZZDICE,
    ZFLUSH, ZCHDIR, ZPOW, ZLOG2, ZLOG10, ZLOG, ZCOSINE, ZTANGENT,
    ZATANGENT, ZACOSINE, ZSINE, ZASINE, ZEXP, ZEXP2, ZEXP10,
    ZHIT, ZLOWER, ZNORMAL, ZSTEM, ZSEEK, ZTELL, ZWORD, ZNUMERIC,
    ZBLNK, ZCURRENTFILE, ZHTML, ZCONDENSE, ZRESTORE, ZNXTROW,
    ZABS, ZPAD, Z_VARIABLE, ZDATE, ZFILE, ZGLOBAL, ZROOT, ZSYSTEM,
    FCN_NOT_FOUND, ERR
}

// vendor struct tm carrier (the zdate zd3-zd5 arms); mktime renders
// as the Calendar normalization below (the field set the vendor
// passes is day/month/year only, so the UTC year/month/day-walk
// matches the glibc normalize observable: yday and wday reread
// from the normalized instant, tm_isdst forced to the vendor-set
// 1/0 parity, documented divergence: no local timezone is applied,
// the reference build walks UTC)
private class TmZz {
    var tm_year = 0
    var tm_mon = 0
    var tm_mday = 0
    var tm_hour = 0
    var tm_min = 0
    var tm_sec = 0
    var tm_yday = 0
    var tm_wday = 0
    var tm_isdst = 0
}

private fun mktime(date: TmZz) {
    val cal = java.util.Calendar.getInstance(
        java.util.TimeZone.getTimeZone("UTC"), java.util.Locale.US)
    cal.clear()
    cal.isLenient = true
    cal.set(java.util.Calendar.YEAR, date.tm_year + 1900)
    cal.set(java.util.Calendar.MONTH, date.tm_mon)
    cal.set(java.util.Calendar.DAY_OF_MONTH, date.tm_mday)
    cal.set(java.util.Calendar.HOUR_OF_DAY, date.tm_hour)
    cal.set(java.util.Calendar.MINUTE, date.tm_min)
    cal.set(java.util.Calendar.SECOND, date.tm_sec)
    // reread the normalized fields back, the glibc mktime out-args
    date.tm_year = cal.get(java.util.Calendar.YEAR) - 1900
    date.tm_mon = cal.get(java.util.Calendar.MONTH)
    date.tm_mday = cal.get(java.util.Calendar.DAY_OF_MONTH)
    date.tm_yday = cal.get(java.util.Calendar.DAY_OF_YEAR) - 1
    date.tm_wday = (cal.get(java.util.Calendar.DAY_OF_WEEK)
        - java.util.Calendar.SUNDAY)
}

// --- C helper seats of this TU (file-private; the same-name
// private helpers of other files are file-scoped per Kotlin rules,
// the package does not collide) ---

// tolower/ctype family (C locale, ASCII)
private fun tolowerC(c: Char): Char = if (c in 'A'..'Z') c + 32 else c

private fun isalphaC(c: Char): Boolean =
    c in 'A'..'Z' || c in 'a'..'z'

private fun isdigitC(c: Char): Boolean = c in '0'..'9'

private fun isalnumC(c: Char): Boolean = isalphaC(c) || isdigitC(c)

// ispunct of the vendor (POSIX: printable, not alnum, not space)
private fun ispunctC(c: Char): Boolean =
    c in ' '..'~' && !isalnumC(c) && c != ' '

// C atoi/atol family on the zero-terminated slice contract
private fun atoiC(buf: CharArray): Int {
    var v = 0L
    var i = 0
    var neg = false
    if (i < buf.size && (buf[i] == '-' || buf[i] == '+')) {
        neg = buf[i] == '-'
        i++
    }
    while (i < buf.size && isdigitC(buf[i])) {
        v = v * 10 + (buf[i] - '0')
        i++
        // int wrap clamp (the C truncation observable)
        if (v > 2147483647L) v = 2147483647L
    }
    return (if (neg) -v else v).toInt()
}

private fun atolC(buf: CharArray): Long {
    var v = 0L
    var i = 0
    var neg = false
    while (i < buf.size && (buf[i] == ' ' || buf[i] == '\t')) i++
    if (i < buf.size && (buf[i] == '-' || buf[i] == '+')) {
        neg = buf[i] == '-'
        i++
    }
    while (i < buf.size && isdigitC(buf[i])) {
        v = v * 10 + (buf[i] - '0')
        i++
    }
    return if (neg) -v else v
}

// C atof on the decimal-prefix contract (the %2X/mon-ladder arms
// never see exponents; exponent parse dropped per H-013,
// grep-verified: no caller passes e/E text)
private fun atofC(buf: CharArray): Double {
    val s = CstringOps.cstring(buf)
    if (s.isEmpty()) return 0.0
    var i = 0
    var neg = false
    if (s[i] == '-' || s[i] == '+') { neg = s[i] == '-'; i++ }
    var v = 0.0
    while (i < s.length && isdigitC(s[i])) { v = v * 10 + (s[i] - '0'); i++ }
    if (i < s.length && s[i] == '.') {
        i++
        var scale = 0.1
        while (i < s.length && isdigitC(s[i])) {
            v += (s[i] - '0') * scale
            scale /= 10.0
            i++
        }
    }
    return if (neg) -v else v
}

// strcasecmp family (ASCII fold)
private fun strcasecmpC(a: String, b: String): Boolean {
    if (a.length != b.length) return false
    for (i in a.indices) if (tolowerC(a[i]) != tolowerC(b[i])) return false
    return true
}

private fun strncasecmpC(a: String, b: String, n: Int): Boolean {
    val k = minOf(n, a.length, b.length)
    for (i in 0 until k) if (tolowerC(a[i]) != tolowerC(b[i])) return false
    return true
}

// strmove within one buffer at offsets (the vendor left-shift uses;
// safe for doff < soff exactly as the C overlapping forward copy)
private fun strmoveAt(buf: CharArray, doff: Int, soff: Int) {
    var d = doff
    var s = soff
    while (buf[s] != NUL) { buf[d] = buf[s]; d++; s++ }
    buf[d] = NUL
}

// strcpy/strmove of a slice into a buffer at an offset
private fun strcpyAt(dst: CharArray, doff: Int, src: CharArray, soff: Int) {
    var d = doff
    var s = soff
    while (src[s] != NUL) { dst[d] = src[s]; d++; s++ }
    dst[d] = NUL
}

private fun strcpyS_B(dst: CharArray, src: String) {
    CstringOps.strcpyS(dst, src)
}

// bounded N-slice as a String (strlen-bounded window)
private fun sliceStr(buf: CharArray, off: Int, max: Int): String {
    val sb = StringBuilder()
    var i = off
    var n = 0
    while (buf[i] != NUL && n < max) { sb.append(buf[i]); i++; n++ }
    return sb.toString()
}

// sprintf("%f") C locale percent-f (6 fixed decimals, H-009
// locale-fixed note)
private fun formatF(x: Double): String =
    java.lang.String.format(java.util.Locale.US, "%f", x)

// zero-terminated slice as a fresh CharArray (the vendor passes
// (char *) &v1d[k] tails; slice copies are the C aliasing quantized
// to value carriers)
private fun sliceAtZb(buf: CharArray, off: Int): CharArray {
    var e = off
    while (e < buf.size && buf[e] != NUL) e++
    return buf.copyOfRange(off, e)
}

// String into a zero-terminated CharArray carrier
private fun toZb(src: String): CharArray {
    val b = CharArray(STR_MAX)
    CstringOps.copyAtS(b, 0, src)
    return b
}

// sscanf("%d") opaque handle recovery from the handle text
private fun pctToZb(buf: CharArray, off: Int): Int = atoiC(sliceAtZb(buf, off))

// the zcode1 parse reused by the zword arms (function-scope local
// in the vendor)
private fun zcodeChar1(v1d: CharArray): Char = tolowerC(v1d[4])


// open/close/fsetpos/fgetpos/chdir/system/popen OS carriers,
// H-014 quantizations of the libc edges (notes in-arm at each use):
// open()/fstat quantizes to the File.exists() observable (the
// vendor only tests fd == -1 or not, close() return never read);
// fsetpos/fgetpos have no JVM domain on the Reader carrier, so both
// raise (the DirectRead read(2) precedent family); chdir has no JVM
// domain, quantized to the success "0" path (the caller prints 0/1
// from i alone, so the observable is the constant-0 carrier);
// system/popen run the shell via Runtime exec sh -c (the DirectRead
// stty precedent family); fgets reads the first line bounded; the
// pclose status is never read by the caller (H-013 note).
private fun openC(s: String): Int {
    return if (java.io.File(s).exists()) 0 else -1
}

private fun fsetposC(in_: java.io.Reader?, pos: Long): Unit =
    throw NotImplementedError("fsetpos: out-of-scope owner (OS FILE* domain, Reader carrier)")

private fun fgetposC(in_: java.io.Reader?): Long =
    throw NotImplementedError("fgetpos: out-of-scope owner (OS FILE* domain, Reader carrier)")

private fun chdirC(s: String): Int {
    return 0
}

private fun systemC(s: String): Int {
    return try {
        val p = java.lang.Runtime.getRuntime()
            .exec(arrayOf("sh", "-c", s))
        p.waitFor()
        p.exitValue()
    } catch (e: java.io.IOException) {
        -1
    }
}

private fun popenC(s: String): java.io.BufferedReader? {
    return try {
        val p = java.lang.Runtime.getRuntime()
            .exec(arrayOf("sh", "-c", s))
        java.io.BufferedReader(java.io.InputStreamReader(p.inputStream))
    } catch (e: java.io.IOException) {
        null
    }
}

// fgets(line, max, stream): first line bounded to max-1 chars,
// newline kept as the C fgets does, null at EOF
private fun fgetsC(rd: java.io.BufferedReader?, max: Int): String? {
    if (rd == null) return null
    val sb = StringBuilder()
    var n = 0
    while (n < max - 1) {
        val c = rd.read()
        if (c == -1) break
        sb.append(c.toChar())
        n++
        if (c == '\n'.code) break
    }
    if (n == 0) return null
    return sb.toString()
}

private fun pcloseC(rd: java.io.BufferedReader?) {
    rd?.close()
}

// srand stores only (rand never called in this TU, H-013 note)
private fun srandC(seed: Int) {
    ZfcnsState.srandSeed = seed
}

fun zfcn(v1d: CharArray, bd: CharArray, svPtr: Msv) {
    // unsigned char tmp2[STR_MAX], tmp3[STR_MAX], tmp4[STR_MAX]
    val tmp2 = CharArray(STR_MAX)
    val tmp3 = CharArray(STR_MAX)
    val tmp4 = CharArray(STR_MAX)
    // gbltbl[128]/gblstr[STR_MAX] drop per H-013 (only the dead
    // znxtrow body reads them; file header note)
    // j/k/x/dtime/g ride their arm scopes (the C function-scope
    // scalars only live in single arms here; kept name-verbatim
    // per arm, H-013 liveness notes in the arms)
    // short int iargs[10], nargs
    val iargs = ShortArray(10)
    var nargs: Short = 0
    var wordTrunc = 25
    // char fcnName[64]
    val fcnName = CharArray(64)
    var i: Long = 1

    var lab = ZLblZ.DISPATCH
    machine@ while (true) {
        when (lab) {
            // setup + the branch table (v1d/OPEN parse)
            ZLblZ.DISPATCH -> {
                svPtr.ierr = 0
                iargs[0] = 1
                val zcode = tolowerC(v1d[3])
                val zcode1 = tolowerC(v1d[4])
                // zcode2/zcode3 drop per H-013 (parsed, never read)

                i = 1L
                while (v1d[i.toInt()] != OPEN_ZB && v1d[i.toInt()] != NUL) i++
                // strncpy(fcnName, &v1d[2], i-2): the 64-buffer bound
                // quantizes a >61-char name (the dead C overflow
                // edge, H-013 note); vendor parity for all live names
                var ci = 2
                var cn = 0
                while (cn < (i - 2).toInt() && cn < 63) {
                    fcnName[cn] = v1d[ci]
                    ci++; cn++
                }
                if (cn < 63) fcnName[cn] = NUL
                val fcn = CstringOps.cstring(fcnName)

                while (v1d[1] != OPEN_ZB && v1d[1] != NUL) {
                    strmoveAt(v1d, 1, 2)
                }

                if (zcode == 'g' && (zcode1 == 'a' || zcode1 == 'c'
                        || zcode1 == 's')) {
                    // zglobal
                    strmoveAt(v1d, 1, 2)
                    i = 1L
                    while (v1d[i.toInt()] != NUL) i++
                    if (v1d[(i - 1).toInt()] != CLOSE_ZB
                            || v1d[(i - 2).toInt()] != CLOSE_ZB) {
                        lab = ZLblZ.ERR; continue@machine
                    }
                    v1d[(i - 1).toInt()] = NUL
                } else {
                    if (v1d[1] == OPEN_ZB) strmoveAt(v1d, 1, 2)
                    i = 1L
                    while (v1d[i.toInt()] != NUL) {
                        if (v1d[i.toInt()] == CLOSE_ZB
                                || v1d[i.toInt()] == COMMA_ZB) {
                            v1d[i.toInt()] = NUL
                            nargs = (nargs + 1).toShort()
                            iargs[nargs.toInt()] =
                                (i + 1).toShort()
                        }
                        i++
                    }
                }

                svPtr.ierr = 0

                // branch table (the fcnName ladder)
                if (strncasecmpC(fcn, "z~gtk", 5)
                        || strncasecmpC(fcn, "z~mdh", 5)) {
                    if (strncasecmpC(fcn, "z~mdh~file~chooser~get~filename", 31)) { lab = ZLblZ.GTK_FILE_CHOOSER; continue@machine }
                    if (strncasecmpC(fcn, "z~mdh~calendar~get~date", 23)) { lab = ZLblZ.GTK_CALENDAR; continue@machine }
                    if (strncasecmpC(fcn, "z~mdh~range~get~adjustment", 26)) { lab = ZLblZ.GTK_GET_RANGE_ADJUSTMENT; continue@machine }
                    if (strncasecmpC(fcn, "z~mdh~font~chooser~get~font", 27)) { lab = ZLblZ.GTK_FONT_CHOOSER; continue@machine }
                    if (strncasecmpC(fcn, "z~mdh~color~chooser~get~rgba", 28)) { lab = ZLblZ.GTK_COLOR_CHOOSER; continue@machine }
                    if (strncasecmpC(fcn, "z~mdh~switch~get~active", 23)) { lab = ZLblZ.GTK_SWITCH_GET_ACTIVE; continue@machine }
                    if (strncasecmpC(fcn, "z~mdh~toggle~button~get~active", 30)) { lab = ZLblZ.GTK_TOGGLE_BUTTON_GET_ACTIVE; continue@machine }
                    if (strncasecmpC(fcn, "z~mdh~toggle~button~set~active", 30)) { lab = ZLblZ.GTK_TOGGLE_BUTTON_SET_ACTIVE; continue@machine }
                    if (strncasecmpC(fcn, "z~mdh~spin~button~set~value", 27)) { lab = ZLblZ.GTK_SPIN_BUTTON_SET_VALUE; continue@machine }
                    if (strncasecmpC(fcn, "z~mdh~entry~get~text", 20)) { lab = ZLblZ.GTK_ENTRY_GET_TEXT; continue@machine }
                    if (strncasecmpC(fcn, "z~mdh~entry~set~text", 20)) { lab = ZLblZ.GTK_ENTRY_SET_TEXT; continue@machine }
                    if (strncasecmpC(fcn, "z~mdh~text~buffer~set~text", 22)) { lab = ZLblZ.GTK_TEXT_BUFFER_SET_TEXT; continue@machine }
                    if (strncasecmpC(fcn, "z~mdh~label~set~text", 20)) { lab = ZLblZ.GTK_LABEL_SET_TEXT; continue@machine }
                    if (strncasecmpC(fcn, "z~mdh~dialog~new~with~buttons", 29)) { lab = ZLblZ.GTK_DIALOG_WITH_BUTTONS; continue@machine }
                    if (strncasecmpC(fcn, "z~mdh~tree~view~get~selection", 29)) { lab = ZLblZ.GTK_TREE_VIEW_GET_SELECTION; continue@machine }
                    if (strncasecmpC(fcn, "z~mdh~tree~selection~get~selected", 33)) { lab = ZLblZ.GTK_TREE_SELECTION_GET_SELECTED; continue@machine }
                    if (strncasecmpC(fcn, "z~mdh~tree~model~get", 20)) { lab = ZLblZ.GTK_TREE_MODEL_GET; continue@machine }
                    if (strncasecmpC(fcn, "z~mdh~tree~store~clear", 22)) { lab = ZLblZ.GTK_TREE_STORE_CLEAR; continue@machine }
                    if (strncasecmpC(fcn, "z~mdh~tree~level~add", 20)) { lab = ZLblZ.GTK_TREE_LEVEL_ADD; continue@machine }
                    if (strncasecmpC(fcn, "z~mdh~spin~button~get~value", 27)) { lab = ZLblZ.GTK_SPIN_BUTTON_GET_VALUE; continue@machine }
                    if (strncasecmpC(fcn, "z~mdh~widget~show", 17)) { lab = ZLblZ.GTK_WIDGET_SHOW; continue@machine }
                    if (strncasecmpC(fcn, "z~mdh~widget~hide", 17)) { lab = ZLblZ.GTK_WIDGET_HIDE; continue@machine }
                    lab = ZLblZ.FCN_NOT_FOUND; continue@machine
                }
                if (strncasecmpC(fcn, "zz", 2)) {
                    if (strcasecmpC(fcn, "zzsoundex")) { lab = ZLblZ.ZZSOUNDEX; continue@machine }
                    if (strcasecmpC(fcn, "zzcosine")) { lab = ZLblZ.ZZCOSINE; continue@machine }
                    if (strcasecmpC(fcn, "zzinput")) { lab = ZLblZ.ZZINPUT; continue@machine }
                    if (strcasecmpC(fcn, "zzdice")) { lab = ZLblZ.ZZDICE; continue@machine }
                    if (strcasecmpC(fcn, "zzjaccard")) { lab = ZLblZ.ZZJACCARD; continue@machine }
                    if (strcasecmpC(fcn, "zzsim1")) { lab = ZLblZ.ZZSIM1; continue@machine }
                    if (strcasecmpC(fcn, "zzscanalnum")) { lab = ZLblZ.ZZSCANALNUM; continue@machine }
                    if (strcasecmpC(fcn, "zzscan")) { lab = ZLblZ.ZZSCAN; continue@machine }
                    if (strcasecmpC(fcn, "zzavg")) { lab = ZLblZ.ZZAVG; continue@machine }
                    if (strcasecmpC(fcn, "zzidf")) { lab = ZLblZ.ZZIDF; continue@machine }
                    if (strcasecmpC(fcn, "zztranspose")) { lab = ZLblZ.ZZTRANSPOSE; continue@machine }
                    if (strcasecmpC(fcn, "zzcentroid")) { lab = ZLblZ.ZZCENTROID; continue@machine }
                    if (strcasecmpC(fcn, "zztermcorrelate")) { lab = ZLblZ.ZZTERMCORRELATE; continue@machine }
                    if (strcasecmpC(fcn, "zzdoccorrelate")) { lab = ZLblZ.ZZDOCCORRELATE; continue@machine }
                    if (strcasecmpC(fcn, "zzcount")) { lab = ZLblZ.ZZCOUNT; continue@machine }
                    if (strcasecmpC(fcn, "zzmax")) { lab = ZLblZ.ZZMAX; continue@machine }
                    if (strcasecmpC(fcn, "zzmin")) { lab = ZLblZ.ZZMIN; continue@machine }
                    if (strcasecmpC(fcn, "zzsum")) { lab = ZLblZ.ZZSUM; continue@machine }
                    if (strcasecmpC(fcn, "zzmultiply")) { lab = ZLblZ.ZZMULTIPLY; continue@machine }
                    lab = ZLblZ.FCN_NOT_FOUND; continue@machine
                }
                if (strncasecmpC(fcn, "zs", 2)) {
                    if (strcasecmpC(fcn, "zstem")) { lab = ZLblZ.ZSTEM; continue@machine }
                    if (strncasecmpC(fcn, "zstopl", 6)) { lab = ZLblZ.ZSTOPLOOKUP; continue@machine }
                    if (strcasecmpC(fcn, "zsin")) { lab = ZLblZ.ZSINE; continue@machine }
                    if (strcasecmpC(fcn, "zseek")) { lab = ZLblZ.ZSEEK; continue@machine }
                    if (strcasecmpC(fcn, "zsmithwaterman")) { lab = ZLblZ.ZSMITHWATERMAN; continue@machine }
                    if (strcasecmpC(fcn, "zstopinit")) { lab = ZLblZ.ZSTOPINIT; continue@machine }
                    if (strcasecmpC(fcn, "zsyninit")) { lab = ZLblZ.ZSYNINIT; continue@machine }
                    if (strcasecmpC(fcn, "zsynlookup")) { lab = ZLblZ.ZSYNLOOKUP; continue@machine }
                    if (strcasecmpC(fcn, "zshred")) { lab = ZLblZ.ZSHRED; continue@machine }
                    if (strcasecmpC(fcn, "zshredquery")) { lab = ZLblZ.ZSHREDQUERY; continue@machine }
                    if (strcasecmpC(fcn, "zsrand")) { lab = ZLblZ.ZSRAND; continue@machine }
                    if (strcasecmpC(fcn, "zsqlite")) { lab = ZLblZ.ZSQLITE; continue@machine }
                    if (strcasecmpC(fcn, "zsqlopen")) { lab = ZLblZ.ZSQLOPEN; continue@machine }
                    if (strcasecmpC(fcn, "zsqrt")) { lab = ZLblZ.ZROOT; continue@machine }
                    if (strcasecmpC(fcn, "zsystem")) { lab = ZLblZ.ZSYSTEM; continue@machine }
                    lab = ZLblZ.FCN_NOT_FOUND; continue@machine
                }
                if (strncasecmpC(fcn, "zlow", 4)) { lab = ZLblZ.ZLOWER; continue@machine }
                if (strcasecmpC(fcn, "zcos")) { lab = ZLblZ.ZCOSINE; continue@machine }
                if (strcasecmpC(fcn, "zgetenv")) { lab = ZLblZ.ZGETENV; continue@machine }
                if (strcasecmpC(fcn, "ztan")) { lab = ZLblZ.ZTANGENT; continue@machine }
                if (strcasecmpC(fcn, "zatan")) { lab = ZLblZ.ZATANGENT; continue@machine }
                if (strcasecmpC(fcn, "zacos")) { lab = ZLblZ.ZACOSINE; continue@machine }
                if (strcasecmpC(fcn, "zasin")) { lab = ZLblZ.ZASINE; continue@machine }
                if (strcasecmpC(fcn, "zexp")) { lab = ZLblZ.ZEXP; continue@machine }
                if (strcasecmpC(fcn, "zexp2")) { lab = ZLblZ.ZEXP2; continue@machine }
                if (strcasecmpC(fcn, "zexp10")) { lab = ZLblZ.ZEXP10; continue@machine }
                if (strcasecmpC(fcn, "zlog")) { lab = ZLblZ.ZLOG; continue@machine }
                if (strcasecmpC(fcn, "zlog10")) { lab = ZLblZ.ZLOG10; continue@machine }
                if (strcasecmpC(fcn, "zlog2")) { lab = ZLblZ.ZLOG2; continue@machine }
                if (strcasecmpC(fcn, "zpow")) { lab = ZLblZ.ZPOW; continue@machine }
                if (strcasecmpC(fcn, "zhit")) { lab = ZLblZ.ZHIT; continue@machine }
                if (strcasecmpC(fcn, "zchdir")) { lab = ZLblZ.ZCHDIR; continue@machine }
                if (strcasecmpC(fcn, "zflush")) { lab = ZLblZ.ZFLUSH; continue@machine }
                if (strcasecmpC(fcn, "znoblanks")) { lab = ZLblZ.ZNOBLANKS; continue@machine }
                if (strcasecmpC(fcn, "zpad")) { lab = ZLblZ.ZPAD; continue@machine }
                if (strcasecmpC(fcn, "zbmgsearch")) { lab = ZLblZ.ZBMGSEARCH; continue@machine }
                if (strcasecmpC(fcn, "znative")) { lab = ZLblZ.ZNATIVE; continue@machine }
                if (strcasecmpC(fcn, "zdump")) { lab = ZLblZ.ZCONDENSE; continue@machine }
                if (strcasecmpC(fcn, "zrestore")) { lab = ZLblZ.ZRESTORE; continue@machine }
                if (strcasecmpC(fcn, "zcurrentfile")) { lab = ZLblZ.ZCURRENTFILE; continue@machine }
                if (strcasecmpC(fcn, "zceil")) { lab = ZLblZ.ZCEIL; continue@machine }
                if (strcasecmpC(fcn, "zfloor")) { lab = ZLblZ.ZFLOOR; continue@machine }
                if (strcasecmpC(fcn, "ztrunc")) { lab = ZLblZ.ZTRUNC; continue@machine }
                if (strcasecmpC(fcn, "zprogram")) { lab = ZLblZ.ZPROGRAM; continue@machine }
                if (strcasecmpC(fcn, "zdatabase")) { lab = ZLblZ.ZDATABASE; continue@machine }
                if (strcasecmpC(fcn, "zbasename")) { lab = ZLblZ.ZBASENAME; continue@machine }
                if (strcasecmpC(fcn, "zfiletest")) { lab = ZLblZ.ZFILETEST; continue@machine }
                // vendor fossil rides here: the commented-out
                // zcase=zglobal goto zfiletest line (delta ledger 2)
                if (strcasecmpC(fcn, "zindexmax")) { lab = ZLblZ.ZINDEXMAX; continue@machine }
                if (strcasecmpC(fcn, "zindexsize")) { lab = ZLblZ.ZINDEXSIZE; continue@machine }
                if (strcasecmpC(fcn, "zdatasize")) { lab = ZLblZ.ZDATASIZE; continue@machine }
                if (strcasecmpC(fcn, "zdbname")) { lab = ZLblZ.ZDBNAME; continue@machine }
                if (strcasecmpC(fcn, "zdbfile")) { lab = ZLblZ.ZDBFILE; continue@machine }
                if (zcode == 'a') {
                    if (zcode1 == 'b') { lab = ZLblZ.ZABS; continue@machine }
                    lab = ZLblZ.ZNORMAL; continue@machine // zalter
                }
                if (zcode == 'd') { lab = ZLblZ.ZDATE; continue@machine }
                if (zcode == 'v') { lab = ZLblZ.Z_VARIABLE; continue@machine }
                if (zcode == 'n') { lab = ZLblZ.ZNORMAL; continue@machine }
                if (zcode == 'b') { lab = ZLblZ.ZBLNK; continue@machine }
                if (zcode == 'p') { lab = ZLblZ.ZPAD; continue@machine }
                if (zcode == 'h') { lab = ZLblZ.ZHTML; continue@machine }
                if (zcode == 'f') { lab = ZLblZ.ZFILE; continue@machine }
                if (zcode == 'g') { lab = ZLblZ.ZGLOBAL; continue@machine }
                if (zcode == 'l') { lab = ZLblZ.ZLOG; continue@machine }
                if (zcode == 'm') { lab = ZLblZ.ZNXTROW; continue@machine }
                if (zcode == 'w') { lab = ZLblZ.ZWORD; continue@machine }
                if (zcode == 't') { lab = ZLblZ.ZTELL; continue@machine }
                if (zcode == 'u') { lab = ZLblZ.ZNUMERIC; continue@machine }
                lab = ZLblZ.FCN_NOT_FOUND
            }

            ZLblZ.GTK_DIALOG_WITH_BUTTONS -> {
                if (nargs != 2.toShort()) { lab = ZLblZ.ERR; continue@machine }
                // sscanf("%p") handle recovery (the unit-83 GtkTreeAdd
                // percent-p divergence note precedent)
                val window = CstringOps.cstringAt(v1d, 1)
                // flags: (GtkDialogFlags)(GTK_DIALOG_DESTROY_WITH_PARENT
                // | GTK_MESSAGE_ERROR | GTK_DIALOG_MODAL
                // | GTK_DIALOG_USE_HEADER_BAR) = 7, the C compile-time
                // constant kept
                val flags = 7
                val dialog = Stubs.gtkDialogNewWithButtons(
                    CstringOps.cstringAt(v1d, iargs[1].toInt()), window,
                    flags, arrayOf(kotlin.Pair("Yes", 100), kotlin.Pair("No", 200)))
                Stubs.gtkWindowSetKeepAbove(dialog, true)
                val r = Stubs.gtkDialogRun(dialog)
                Stubs.gtkWidgetDestroy(dialog)
                if (r == 100) {
                    strcpyS_B(bd, "1")
                } else if (r == 200) {
                    strcpyS_B(bd, "0")
                } else {
                    strcpyS_B(bd, "-1")
                }
                return
            }

            ZLblZ.GTK_WIDGET_HIDE -> {
                val widget = CstringOps.cstringAt(v1d, 1)
                Stubs.gtkWidgetHide(widget)
                bd[0] = NUL
                return
            }

            ZLblZ.GTK_WIDGET_SHOW -> {
                val widget = CstringOps.cstringAt(v1d, 1)
                Stubs.gtkWidgetShow(widget)
                bd[0] = NUL
                return
            }

            ZLblZ.GTK_SPIN_BUTTON_GET_VALUE -> {
                if (nargs != 1.toShort()) { lab = ZLblZ.ERR; continue@machine }
                val spin = CstringOps.cstringAt(v1d, 1)
                val xv = Stubs.gtkSpinButtonGetValue(spin)
                strcpyS_B(bd, formatF(xv))
                return
            }

            ZLblZ.GTK_SPIN_BUTTON_SET_VALUE -> {
                if (nargs != 2.toShort()) { lab = ZLblZ.ERR; continue@machine }
                val spin = CstringOps.cstringAt(v1d, 1)
                val valx = atofC(sliceAtZb(v1d, iargs[1].toInt()))
                Stubs.gtkSpinButtonSetValue(spin, valx)
                bd[0] = NUL
                return
            }

            ZLblZ.GTK_TREE_STORE_CLEAR -> {
                if (nargs != 1.toShort()) { lab = ZLblZ.ERR; continue@machine }
                val t = CstringOps.cstringAt(v1d, 1)
                Stubs.gtkTreeStoreClear(t)
                strcpyS_B(bd, "1")
                return
            }

            ZLblZ.GTK_TREE_MODEL_GET -> {
                if (nargs != 5.toShort()) { lab = ZLblZ.ERR; continue@machine }
                val m = CstringOps.cstringAt(v1d, 1)
                // sscanf(&v1d[iargs[1]], "%p", &iter): opaque handle
                ZfcnsState.iter[0] = pctToZb(v1d, iargs[1].toInt())
                val value = Stubs.gtkTreeModelGet(m, ZfcnsState.iter, 1)
                strcpyS_B(bd, value)
                return
            }

            ZLblZ.GTK_TREE_SELECTION_GET_SELECTED -> {
                if (nargs != 2.toShort()) { lab = ZLblZ.ERR; continue@machine }
                val s = CstringOps.cstringAt(v1d, 1)
                val col = atoiC(sliceAtZb(v1d, iargs[1].toInt()))
                val m = StrHolderZb()
                if (!Stubs.gtkTreeSelectionGetSelected(s, m, ZfcnsState.iter)) {
                    strcpyS_B(bd, "0")
                    return
                }
                bd[0] = NUL
                val value = Stubs.gtkTreeModelGet(m.value, ZfcnsState.iter, col)
                strcpyS_B(bd, value)
                return
            }

            ZLblZ.GTK_TREE_VIEW_GET_SELECTION -> {
                if (nargs != 1.toShort()) { lab = ZLblZ.ERR; continue@machine }
                val t = CstringOps.cstringAt(v1d, 1)
                val s = Stubs.gtkTreeViewGetSelection(t)
                // sprintf("%p") handle text
                strcpyS_B(bd, s)
                return
            }

            ZLblZ.GTK_TREE_LEVEL_ADD -> {
                if (nargs < 4.toShort()) { lab = ZLblZ.ERR; continue@machine }
                val depth = atoiC(sliceAtZb(v1d, iargs[1].toInt()))
                // vendor fall-through quirk: a depth outside 1-10
                // falls through the if-chain into the
                // GtkTreeLevel1Add label; kept verbatim
                lab = when (depth) {
                    1 -> ZLblZ.GTK_TREE_LEVEL1_ADD
                    2 -> ZLblZ.GTK_TREE_LEVEL2_ADD
                    3 -> ZLblZ.GTK_TREE_LEVEL3_ADD
                    4 -> ZLblZ.GTK_TREE_LEVEL4_ADD
                    5 -> ZLblZ.GTK_TREE_LEVEL5_ADD
                    6 -> ZLblZ.GTK_TREE_LEVEL6_ADD
                    7 -> ZLblZ.GTK_TREE_LEVEL7_ADD
                    8 -> ZLblZ.GTK_TREE_LEVEL8_ADD
                    9 -> ZLblZ.GTK_TREE_LEVEL9_ADD
                    10 -> ZLblZ.GTK_TREE_LEVEL10_ADD
                    else -> ZLblZ.GTK_TREE_LEVEL1_ADD
                }
            }

            ZLblZ.GTK_TREE_LEVEL1_ADD -> {
                GtkTreeAdd(ZfcnsState.iter, null, sliceAtZb(v1d, 1), v1d, iargs, nargs)
                strcpyS_B(bd, "0")
                return
            }

            ZLblZ.GTK_TREE_LEVEL2_ADD -> {
                GtkTreeAdd(ZfcnsState.iter1, ZfcnsState.iter, sliceAtZb(v1d, 1), v1d, iargs, nargs)
                strcpyS_B(bd, "0")
                return
            }

            ZLblZ.GTK_TREE_LEVEL3_ADD -> {
                GtkTreeAdd(ZfcnsState.iter2, ZfcnsState.iter1, sliceAtZb(v1d, 1), v1d, iargs, nargs)
                strcpyS_B(bd, "0")
                return
            }

            ZLblZ.GTK_TREE_LEVEL4_ADD -> {
                GtkTreeAdd(ZfcnsState.iter3, ZfcnsState.iter2, sliceAtZb(v1d, 1), v1d, iargs, nargs)
                strcpyS_B(bd, "0")
                return
            }

            ZLblZ.GTK_TREE_LEVEL5_ADD -> {
                GtkTreeAdd(ZfcnsState.iter4, ZfcnsState.iter3, sliceAtZb(v1d, 1), v1d, iargs, nargs)
                strcpyS_B(bd, "0")
                return
            }

            ZLblZ.GTK_TREE_LEVEL6_ADD -> {
                GtkTreeAdd(ZfcnsState.iter5, ZfcnsState.iter4, sliceAtZb(v1d, 1), v1d, iargs, nargs)
                strcpyS_B(bd, "0")
                return
            }

            ZLblZ.GTK_TREE_LEVEL7_ADD -> {
                GtkTreeAdd(ZfcnsState.iter6, ZfcnsState.iter5, sliceAtZb(v1d, 1), v1d, iargs, nargs)
                strcpyS_B(bd, "0")
                return
            }

            ZLblZ.GTK_TREE_LEVEL8_ADD -> {
                GtkTreeAdd(ZfcnsState.iter7, ZfcnsState.iter6, sliceAtZb(v1d, 1), v1d, iargs, nargs)
                strcpyS_B(bd, "0")
                return
            }

            ZLblZ.GTK_TREE_LEVEL9_ADD -> {
                GtkTreeAdd(ZfcnsState.iter8, ZfcnsState.iter7, sliceAtZb(v1d, 1), v1d, iargs, nargs)
                strcpyS_B(bd, "0")
                return
            }

            ZLblZ.GTK_TREE_LEVEL10_ADD -> {
                GtkTreeAdd(ZfcnsState.iter9, ZfcnsState.iter8, sliceAtZb(v1d, 1), v1d, iargs, nargs)
                strcpyS_B(bd, "0")
                return
            }

            ZLblZ.GTK_LABEL_SET_TEXT -> {
                if (nargs != 2.toShort()) { lab = ZLblZ.ERR; continue@machine }
                val l = CstringOps.cstringAt(v1d, 1)
                Stubs.gtkLabelSetText(l, CstringOps.cstringAt(v1d, iargs[1].toInt()))
                strcpyS_B(bd, "1")
                return
            }

            ZLblZ.GTK_TEXT_BUFFER_SET_TEXT -> {
                if (nargs != 2.toShort()) { lab = ZLblZ.ERR; continue@machine }
                val b = CstringOps.cstringAt(v1d, 1)
                // (gint) -1 length passes as -1 verbatim
                Stubs.gtkTextBufferSetText(b, CstringOps.cstringAt(v1d, iargs[1].toInt()), -1)
                strcpyS_B(bd, "1")
                return
            }

            ZLblZ.GTK_ENTRY_GET_TEXT -> {
                if (nargs != 1.toShort()) { lab = ZLblZ.ERR; continue@machine }
                val e = CstringOps.cstringAt(v1d, 1)
                strcpyS_B(bd, Stubs.gtkEntryGetText(e))
                return
            }

            ZLblZ.GTK_ENTRY_SET_TEXT -> {
                if (nargs != 2.toShort()) { lab = ZLblZ.ERR; continue@machine }
                val e = CstringOps.cstringAt(v1d, 1)
                Stubs.gtkEntrySetText(e, CstringOps.cstringAt(v1d, iargs[1].toInt()))
                strcpyS_B(bd, "0")
                return
            }

            ZLblZ.GTK_GET_RANGE_ADJUSTMENT -> {
                if (nargs != 1.toShort()) { lab = ZLblZ.ERR; continue@machine }
                val e = CstringOps.cstringAt(v1d, 1)
                val adj = Stubs.gtkRangeGetAdjustment(e)
                val v = Stubs.gtkAdjustmentGetValue(adj)
                strcpyS_B(bd, formatF(v))
                return
            }

            ZLblZ.GTK_FILE_CHOOSER -> {
                if (nargs != 1.toShort()) { lab = ZLblZ.ERR; continue@machine }
                val e = CstringOps.cstringAt(v1d, 1)
                // strncpy(bd, filename, STR_MAX) count-bound copy
                val name = Stubs.gtkFileChooserGetFilename(e) ?: ""
                var ci2 = 0
                while (ci2 < name.length && ci2 < STR_MAX - 1) {
                    bd[ci2] = name[ci2]; ci2++
                }
                bd[ci2] = NUL
                return
            }

            ZLblZ.GTK_CALENDAR -> {
                if (nargs != 1.toShort()) { lab = ZLblZ.ERR; continue@machine }
                val e = CstringOps.cstringAt(v1d, 1)
                val yr = IntArray(1)
                val mn = IntArray(1)
                val dy = IntArray(1)
                Stubs.gtkCalendarGetDate(e, yr, mn, dy)
                strcpyS_B(bd, (yr[0].toString() + " " + mn[0].toString()
                    + " " + dy[0].toString()))
                return
            }

            ZLblZ.GTK_FONT_CHOOSER -> {
                if (nargs != 1.toShort()) { lab = ZLblZ.ERR; continue@machine }
                val e = CstringOps.cstringAt(v1d, 1)
                strcpyS_B(bd, Stubs.gtkFontChooserGetFont(e) ?: "")
                return
            }

            ZLblZ.GTK_COLOR_CHOOSER -> {
                if (nargs != 1.toShort()) { lab = ZLblZ.ERR; continue@machine }
                val c = CstringOps.cstringAt(v1d, 1)
                val r = GdkRgbaZb()
                // intentional unused write: the vendor sets neither carrier (H-013 note)
                Stubs.gtkColorChooserGetRgba(c, r)
                bd[0] = NUL
                return
            }

            ZLblZ.GTK_TOGGLE_BUTTON_SET_ACTIVE -> {
                if (nargs != 2.toShort()) { lab = ZLblZ.ERR; continue@machine }
                val t = CstringOps.cstringAt(v1d, 1)
                val ref = sliceAtZb(v1d, iargs[1].toInt())
                if (CstringOps.strncmpC(ref, "TRUE".toCharArray(), 4) == 0
                    && CstringOps.strlenC(ref) == 4) {
                    Stubs.gtkToggleButtonSetActive(t, true)
                    strcpyS_B(bd, "1")
                    return
                }
                if (CstringOps.strncmpC(ref, "FALSE".toCharArray(), 5) == 0
                    && CstringOps.strlenC(ref) == 5) {
                    Stubs.gtkToggleButtonSetActive(t, false)
                    strcpyS_B(bd, "1")
                    return
                }
                // vendor control flow: no match falls past the block
                // to strcpy(bd, "0") and goto err; kept verbatim
                strcpyS_B(bd, "0")
                lab = ZLblZ.ERR
            }

            ZLblZ.GTK_TOGGLE_BUTTON_GET_ACTIVE -> {
                if (nargs != 1.toShort()) { lab = ZLblZ.ERR; continue@machine }
                val t = CstringOps.cstringAt(v1d, 1)
                if (Stubs.gtkToggleButtonGetActive(t)) {
                    strcpyS_B(bd, "1")
                } else {
                    strcpyS_B(bd, "0")
                }
                return
            }

            ZLblZ.GTK_SWITCH_GET_ACTIVE -> {
                if (nargs != 1.toShort()) { lab = ZLblZ.ERR; continue@machine }
                val t = CstringOps.cstringAt(v1d, 1)
                if (Stubs.gtkSwitchGetActive(t)) {
                    strcpyS_B(bd, "1")
                } else {
                    strcpyS_B(bd, "0")
                }
                return
            }

            ZLblZ.ZDATABASE -> {
                // vendor dead strcmp("sql", "sql") constant-true
                // literal: kept as this note (H-013); the 0==1
                // dead arm drops with the same note
                if (nargs != 0.toShort()) { lab = ZLblZ.ERR; continue@machine }
                strcpyS_B(bd, "2")
                return
            }

            ZLblZ.ZPROGRAM -> {
                if (nargs != 0.toShort()) { lab = ZLblZ.ERR; continue@machine }
                CstringOps.strcpyC(bd, svPtr.program)
                return
            }

            ZLblZ.ZFILETEST -> {
                if (nargs != 2.toShort()) { lab = ZLblZ.ERR; continue@machine }
                // char tmp[1024] build bounded 500/500 (vendor counts)
                val tmp = ("echo " + BT_ZB + "if [ "
                    + sliceStr(v1d, iargs[1].toInt(), 500)
                    + " \""
                    + sliceStr(v1d, 1, 500)
                    + "\" ]; then echo \"1\"; fi" + BT_ZB)
                val f1 = popenC(tmp)
                var line = fgetsC(f1, 1000) ?: ""
                // vendor trims the trailing newline unconditionally;
                // the empty-output edge is UB in C (index -1),
                // quantized to the no-trim path (H-018)
                if (line.isNotEmpty() && line[line.length - 1] == '\n') {
                    line = line.substring(0, line.length - 1)
                    if (line.contains('\n')) {
                        line = line.substring(0, line.indexOf('\n'))
                    }
                }
                pcloseC(f1)
                if (line == "1") {
                    strcpyS_B(bd, "1")
                } else {
                    strcpyS_B(bd, "0")
                }
                return
            }

            ZLblZ.ZBASENAME -> {
                if (!(nargs == 1.toShort() || nargs == 2.toShort())) { lab = ZLblZ.ERR; continue@machine }
                if (nargs == 1.toShort()) {
                    val tmp = ("echo " + BT_ZB + "basename \""
                        + sliceStr(v1d, 1, 1000) + "\"" + BT_ZB)
                    val f1 = popenC(tmp)
                    var line = fgetsC(f1, 1000) ?: ""
                    // one-arg arm trims the newline (two-arg arm does
                    // not; kept verbatim)
                    if (line.isNotEmpty() && line[line.length - 1] == '\n') {
                        line = line.substring(0, line.length - 1)
                        if (line.contains('\n')) {
                            line = line.substring(0, line.indexOf('\n'))
                        }
                    }
                    pcloseC(f1)
                    strcpyS_B(bd, line)
                    return
                } else {
                    val tmp = ("echo " + BT_ZB + "basename \""
                        + sliceStr(v1d, 1, 900)
                        + "\" \"" + sliceStr(v1d, iargs[1].toInt(), 32)
                        + "\"" + BT_ZB)
                    val f1 = popenC(tmp)
                    val line = fgetsC(f1, 1000) ?: ""
                    pcloseC(f1)
                    strcpyS_B(bd, line) // newline kept, vendor verbatim
                    return
                }
            }

            ZLblZ.ZFLOOR -> {
                if (nargs != 1.toShort()) { lab = ZLblZ.ERR; continue@machine }
                Stubs.mpsFloor(bd, CstringOps.cstringAt(v1d, 1))
                return
            }

            ZLblZ.ZCEIL -> {
                if (nargs != 1.toShort()) { lab = ZLblZ.ERR; continue@machine }
                Stubs.mpsCeil(bd, CstringOps.cstringAt(v1d, 1))
                return
            }

            ZLblZ.ZTRUNC -> {
                if (nargs != 1.toShort()) { lab = ZLblZ.ERR; continue@machine }
                Stubs.mpsTrunc(bd, CstringOps.cstringAt(v1d, 1))
                return
            }

            ZLblZ.ZSQLOPEN -> {
                if (svPtr.ConOpen != 0) bd[0] = '1' else bd[0] = '0'
                bd[1] = NUL
                return
            }

            ZLblZ.ZNATIVE -> {
                // SQLITE live: znative returns "0"; the #else arm
                // drops per H-018 with this note
                strcpyS_B(bd, "0")
                return
            }

            ZLblZ.ZINDEXSIZE -> {
                // SQLITE-only arm (delta ledger 3); the #else twin
                // returns "" and drops per H-018
                strcpyS_B(bd, "256")
                return
            }

            ZLblZ.ZDATASIZE -> {
                strcpyS_B(bd, "512")
                return
            }

            ZLblZ.ZDBNAME -> {
                strcpyS_B(bd, "mumps")
                return
            }

            ZLblZ.ZDBFILE -> {
                strcpyS_B(bd, "mumps.sqlite")
                return
            }

            ZLblZ.ZINDEXMAX -> {
                // vendor arm-block: i = 14; sprintf(tmp, "%d", i - 2)
                i = 14L
                strcpyS_B(bd, (i - 2).toString())
                return
            }

            ZLblZ.ZSQLITE -> {
                // SQLITE live sub-dispatch (defines.h sql codes 25-29)
                if (nargs == 0.toShort()) {
                    strcpyS_B(bd, "1")
                    return
                }
                val XTAB = "\t"
                if (strcasecmpC(CstringOps.cstringAt(v1d, 1), "begin transaction")
                        && nargs == 1.toShort()) {
                    val filename = "begin transaction;"
                    svPtr.tpx = Stubs.sql(BEGIN_TRANSACTION_ZB, svPtr,
                        toZb(""), toZb(filename),
                        toZb(""), XTAB).toLong()
                    strcpyS_B(bd, svPtr.tpx.toString())
                    return
                }
                if (strcasecmpC(CstringOps.cstringAt(v1d, 1), "commit transaction")
                        && nargs == 1.toShort()) {
                    val filename = "commit transaction;"
                    svPtr.tpx = Stubs.sql(COMMIT_TRANSACTION_ZB, svPtr,
                        toZb(""), toZb(filename),
                        toZb(""), XTAB).toLong()
                    strcpyS_B(bd, svPtr.tpx.toString())
                    return
                }
                if (strcasecmpC(CstringOps.cstringAt(v1d, 1), "savepoint")
                        && (nargs == 1.toShort() || nargs == 2.toShort())) {
                    var filename = "savepoint "
                    if (nargs == 2.toShort()) {
                        filename = filename + CstringOps.cstringAt(v1d, iargs[1].toInt()) + ";"
                    } else {
                        filename += " default;"
                    }
                    svPtr.tpx = Stubs.sql(SAVEPOINT_ZB, svPtr,
                        toZb(""), toZb(filename),
                        toZb(""), XTAB).toLong()
                    strcpyS_B(bd, svPtr.tpx.toString())
                    return
                }
                if (strcasecmpC(CstringOps.cstringAt(v1d, 1), "rollback")
                        && (nargs == 1.toShort() || nargs == 2.toShort())) {
                    var filename = "rollback to "
                    if (nargs == 2.toShort()) {
                        filename = filename + CstringOps.cstringAt(v1d, iargs[1].toInt()) + ";"
                    } else {
                        filename += " default;"
                    }
                    svPtr.tpx = Stubs.sql(ROLLBACK_TRANSACTION_ZB, svPtr,
                        toZb(""), toZb(filename),
                        toZb(""), XTAB).toLong()
                    strcpyS_B(bd, svPtr.tpx.toString())
                    return
                }
                if (strcasecmpC(CstringOps.cstringAt(v1d, 1), "pragma")
                        && nargs == 2.toShort()) {
                    val filename = "pragma " + CstringOps.cstringAt(v1d, iargs[1].toInt()) + ";"
                    val dmy = CharArray(1024)
                    svPtr.tpx = Stubs.sql(PRAGMA_ZB, svPtr,
                        toZb(""), toZb(filename), dmy, XTAB).toLong()
                    if (CstringOps.strlenC(dmy) != 0) {
                        CstringOps.strcpyC(bd, dmy)
                    } else {
                        strcpyS_B(bd, svPtr.tpx.toString())
                    }
                    return
                }
                if (strcasecmpC(CstringOps.cstringAt(v1d, 1), "sql")
                        && nargs == 2.toShort()) {
                    // SQLCMD 55 where bifs passes PRAGMA 29 (delta
                    // ledger 4)
                    val filename = CstringOps.cstringAt(v1d, iargs[1].toInt()) + ";"
                    val dmy = CharArray(1024)
                    svPtr.tpx = Stubs.sql(SQLCMD_ZB, svPtr,
                        toZb(""), toZb(filename), dmy, XTAB).toLong()
                    if (CstringOps.strlenC(dmy) != 0) {
                        CstringOps.strcpyC(bd, dmy)
                    } else {
                        strcpyS_B(bd, svPtr.tpx.toString())
                    }
                    return
                }
                System.err.print("sqlite arguments not recognized\n")
                lab = ZLblZ.ERR
            }

            ZLblZ.ZSRAND -> {
                if (nargs != 1.toShort()) { lab = ZLblZ.ERR; continue@machine }
                i = atolC(sliceAtZb(v1d, 1))
                srandC(i.toInt())
                return
            }

            ZLblZ.ZSMITHWATERMAN -> {
                if (nargs != 7.toShort()) { lab = ZLblZ.ERR; continue@machine }
                val r = SmithWaterman(svPtr, "",
                    sliceAtZb(v1d, 1),
                    sliceAtZb(v1d, iargs[1].toInt()),
                    sliceAtZb(v1d, iargs[2].toInt()),
                    sliceAtZb(v1d, iargs[3].toInt()),
                    sliceAtZb(v1d, iargs[4].toInt()),
                    sliceAtZb(v1d, iargs[5].toInt()),
                    sliceAtZb(v1d, iargs[6].toInt()))
                strcpyS_B(bd, r)
                return
            }

            ZLblZ.ZNOBLANKS -> {
                if (nargs != 1.toShort()) { lab = ZLblZ.ERR; continue@machine }
                var ii = 1
                var jj = 0
                while (v1d[ii] != NUL) {
                    if (v1d[ii] != ' ') {
                        bd[jj] = v1d[ii]
                        jj++
                    }
                    ii++
                }
                bd[jj] = NUL
                return
            }

            ZLblZ.ZSTOPINIT -> {
                if (nargs != 1.toShort()) { lab = ZLblZ.ERR; continue@machine }
                stopInit(CstringOps.cstringAt(v1d, 1))
                return
            }

            ZLblZ.ZSYNINIT -> {
                if (nargs != 1.toShort()) { lab = ZLblZ.ERR; continue@machine }
                // SynInit side effect only; the return handle is
                // void-shaped in the vendor call
                SynInit(svPtr, "", sliceAtZb(v1d, 1))
                return
            }

            ZLblZ.ZBMGSEARCH -> {
                if (nargs != 2.toShort()) { lab = ZLblZ.ERR; continue@machine }
                val r = BMGSearch(svPtr, "",
                    sliceAtZb(v1d, 1),
                    sliceAtZb(v1d, iargs[1].toInt()))
                strcpyS_B(bd, r)
                return
            }

            ZLblZ.ZSHRED -> {
                if (nargs != 2.toShort()) { lab = ZLblZ.ERR; continue@machine }
                val r = Shred(svPtr, "",
                    sliceAtZb(v1d, 1),
                    sliceAtZb(v1d, iargs[1].toInt()))
                strcpyS_B(bd, r)
                return
            }

            ZLblZ.ZSHREDQUERY -> {
                if (nargs != 2.toShort()) { lab = ZLblZ.ERR; continue@machine }
                val r = ShredQuery(svPtr, "",
                    sliceAtZb(v1d, 1),
                    sliceAtZb(v1d, iargs[1].toInt()))
                strcpyS_B(bd, r)
                return
            }

            ZLblZ.ZSTOPLOOKUP -> {
                if (nargs != 1.toShort()) { lab = ZLblZ.ERR; continue@machine }
                if (Stubs.stopLookup(CstringOps.cstringAt(v1d, 1)) != 0) {
                    bd[0] = '1'
                } else {
                    bd[0] = '0'
                }
                bd[1] = NUL
                return
            }

            ZLblZ.ZSYNLOOKUP -> {
                if (nargs != 1.toShort()) { lab = ZLblZ.ERR; continue@machine }
                val r = SynLookup(svPtr, "", sliceAtZb(v1d, 1))
                strcpyS_B(bd, r)
                return
            }

            ZLblZ.ZZAVG -> {
                if (nargs != 1.toShort()) { lab = ZLblZ.ERR; continue@machine }
                val tmp1 = CharArray(STR_MAX)
                tmp1[0] = NUL
                tmp2[0] = NUL
                strcpyAt(tmp1, 0, v1d, 1)
                Stubs.ServiceInterface(tmp1, tmp2, null, null, tmp3, AVG_ZB.toChar(), svPtr)
                CstringOps.strcpyC(bd, tmp3)
                return
            }

            ZLblZ.ZGETENV -> {
                if (nargs != 1.toShort()) { lab = ZLblZ.ERR; continue@machine }
                val tmp1 = CharArray(STR_MAX)
                tmp1[0] = NUL
                tmp2[0] = NUL
                strcpyAt(tmp1, 0, v1d, 1)
                Stubs.ServiceInterface(tmp1, tmp2, null, null, tmp3, GETENV_ZB.toChar(), svPtr)
                CstringOps.strcpyC(bd, tmp3)
                return
            }

            ZLblZ.ZZCOUNT -> {
                if (nargs != 1.toShort()) { lab = ZLblZ.ERR; continue@machine }
                val tmp1 = CharArray(STR_MAX)
                tmp1[0] = NUL
                tmp2[0] = NUL
                strcpyAt(tmp1, 0, v1d, 1)
                Stubs.ServiceInterface(tmp1, tmp2, null, null, tmp3, COUNT_ZB.toChar(), svPtr)
                CstringOps.strcpyC(bd, tmp3)
                return
            }

            ZLblZ.ZZMAX -> {
                if (nargs != 1.toShort()) { lab = ZLblZ.ERR; continue@machine }
                val tmp1 = CharArray(STR_MAX)
                tmp1[0] = NUL
                tmp2[0] = NUL
                strcpyAt(tmp1, 0, v1d, 1)
                Stubs.ServiceInterface(tmp1, tmp2, null, null, tmp3, MAX_ZB.toChar(), svPtr)
                CstringOps.strcpyC(bd, tmp3)
                return
            }

            ZLblZ.ZZMIN -> {
                if (nargs != 1.toShort()) { lab = ZLblZ.ERR; continue@machine }
                val tmp1 = CharArray(STR_MAX)
                tmp1[0] = NUL
                tmp2[0] = NUL
                strcpyAt(tmp1, 0, v1d, 1)
                Stubs.ServiceInterface(tmp1, tmp2, null, null, tmp3, MIN_ZB.toChar(), svPtr)
                CstringOps.strcpyC(bd, tmp3)
                return
            }

            ZLblZ.ZZSUM -> {
                if (nargs != 1.toShort()) { lab = ZLblZ.ERR; continue@machine }
                val tmp1 = CharArray(STR_MAX)
                tmp1[0] = NUL
                tmp2[0] = NUL
                strcpyAt(tmp1, 0, v1d, 1)
                Stubs.ServiceInterface(tmp1, tmp2, null, null, tmp3, SUM_ZB.toChar(), svPtr)
                CstringOps.strcpyC(bd, tmp3)
                return
            }

            ZLblZ.ZZSCANALNUM -> {
                if (nargs != 0.toShort() && nargs != 1.toShort()) { lab = ZLblZ.ERR; continue@machine }
                val tmp1 = CharArray(STR_MAX)
                tmp1[0] = NUL
                tmp2[0] = NUL
                Stubs.ServiceInterface(tmp1, tmp2, null, null, tmp3, SCANALNUM_ZB.toChar(), svPtr)
                CstringOps.strcpyC(bd, tmp3)
                if (nargs == 1.toShort()) {
                    i = atolC(sliceAtZb(v1d, iargs[1].toInt()))
                    // bd[i]=0 index quantized into bounds (H-018 note)
                    val ib = i.toInt()
                    if (ib in 0 until bd.size) bd[ib] = NUL
                }
                return
            }

            ZLblZ.ZZSCAN -> {
                if (nargs != 0.toShort() && nargs != 1.toShort()) { lab = ZLblZ.ERR; continue@machine }
                val tmp1 = CharArray(STR_MAX)
                tmp1[0] = NUL
                tmp2[0] = NUL
                Stubs.ServiceInterface(tmp1, tmp2, null, null, tmp3, SCAN_ZB.toChar(), svPtr)
                CstringOps.strcpyC(bd, tmp3)
                if (nargs == 1.toShort()) {
                    i = atolC(sliceAtZb(v1d, iargs[1].toInt()))
                    val ib = i.toInt()
                    if (ib in 0 until bd.size) bd[ib] = NUL
                }
                return
            }

            ZLblZ.ZZINPUT -> {
                if (nargs != 1.toShort()) { lab = ZLblZ.ERR; continue@machine }
                val tmp1 = CharArray(STR_MAX)
                tmp1[0] = NUL
                tmp2[0] = NUL
                strcpyAt(tmp1, 0, v1d, 1)
                Stubs.ServiceInterface(tmp1, tmp2, null, null, tmp3, INPUT_ZB.toChar(), svPtr)
                CstringOps.strcpyC(bd, tmp3)
                return
            }

            ZLblZ.ZZCOSINE -> {
                if (nargs != 2.toShort()) { lab = ZLblZ.ERR; continue@machine }
                val tmp1 = CharArray(STR_MAX)
                tmp1[0] = NUL
                tmp2[0] = NUL
                strcpyAt(tmp1, 0, v1d, 1)
                strcpyAt(tmp2, 0, v1d, iargs[1].toInt())
                Stubs.ServiceInterface(tmp1, tmp2, null, null, tmp3, COSINE_ZB.toChar(), svPtr)
                CstringOps.strcpyC(bd, tmp3)
                return
            }

            ZLblZ.ZZJACCARD -> {
                if (nargs != 2.toShort()) { lab = ZLblZ.ERR; continue@machine }
                val tmp1 = CharArray(STR_MAX)
                tmp1[0] = NUL
                tmp2[0] = NUL
                strcpyAt(tmp1, 0, v1d, 1)
                strcpyAt(tmp2, 0, v1d, iargs[1].toInt())
                Stubs.ServiceInterface(tmp1, tmp2, null, null, tmp3, JACCARD_ZB.toChar(), svPtr)
                CstringOps.strcpyC(bd, tmp3)
                return
            }

            ZLblZ.ZZSIM1 -> {
                if (nargs != 2.toShort()) { lab = ZLblZ.ERR; continue@machine }
                val tmp1 = CharArray(STR_MAX)
                tmp1[0] = NUL
                tmp2[0] = NUL
                strcpyAt(tmp1, 0, v1d, 1)
                strcpyAt(tmp2, 0, v1d, iargs[1].toInt())
                Stubs.ServiceInterface(tmp1, tmp2, null, null, tmp3, SIM1_ZB.toChar(), svPtr)
                CstringOps.strcpyC(bd, tmp3)
                return
            }

            ZLblZ.ZZTRANSPOSE -> {
                if (nargs != 2.toShort()) { lab = ZLblZ.ERR; continue@machine }
                val tmp1 = CharArray(STR_MAX)
                tmp1[0] = NUL
                tmp2[0] = NUL
                strcpyAt(tmp1, 0, v1d, 1)
                strcpyAt(tmp2, 0, v1d, iargs[1].toInt())
                Stubs.ServiceInterface(tmp1, tmp2, null, null, tmp3, TRANSPOSE_ZB.toChar(), svPtr)
                CstringOps.strcpyC(bd, tmp3)
                return
            }

            ZLblZ.ZZSOUNDEX -> {
                if (nargs != 1.toShort()) { lab = ZLblZ.ERR; continue@machine }
                bd[0] = v1d[1]
                var jj = 1
                var ii = 2
                while (v1d[ii] != NUL) {
                    if (!isalphaC(v1d[ii])) { ii++; continue }
                    v1d[ii] = tolowerC(v1d[ii])
                    if (v1d[ii] == 'a' || v1d[ii] == 'e' || v1d[ii] == 'h'
                            || v1d[ii] == 'i' || v1d[ii] == 'o'
                            || v1d[ii] == 'w' || v1d[ii] == 'u'
                            || v1d[ii] == 'y') { ii++; continue }
                    if (bd[jj - 1] != v1d[ii]) {
                        bd[jj] = v1d[ii]
                        jj++
                    }
                    ii++
                }
                bd[jj] = NUL
                ii = 1
                while (bd[ii] != NUL) {
                    when (bd[ii]) {
                        'b', 'f', 'p', 'v' -> bd[ii] = '1'
                        'c', 'g', 'j', 'k', 'q', 's', 'x', 'z' -> bd[ii] = '2'
                        'd', 't' -> bd[ii] = '3'
                        'l' -> bd[ii] = '4'
                        'm', 'n' -> bd[ii] = '5'
                        'r' -> bd[ii] = '6'
                    }
                    ii++
                }
                bd[4] = NUL
                return
            }

            ZLblZ.ZZMULTIPLY -> {
                if (nargs != 3.toShort()) { lab = ZLblZ.ERR; continue@machine }
                val tmp1 = CharArray(STR_MAX)
                tmp3[0] = NUL
                tmp1[0] = NUL
                tmp2[0] = NUL
                strcpyAt(tmp1, 0, v1d, 1)
                strcpyAt(tmp2, 0, v1d, iargs[1].toInt())
                strcpyAt(tmp3, 0, v1d, iargs[2].toInt())
                Stubs.ServiceInterface(tmp1, tmp2, tmp3, null, tmp4, MMULTIPLY_ZB.toChar(), svPtr)
                CstringOps.strcpyC(bd, tmp4)
                return
            }

            ZLblZ.ZZCENTROID -> {
                if (nargs != 2.toShort()) { lab = ZLblZ.ERR; continue@machine }
                val tmp1 = CharArray(STR_MAX)
                tmp1[0] = NUL
                tmp2[0] = NUL
                strcpyAt(tmp1, 0, v1d, 1)
                strcpyAt(tmp2, 0, v1d, iargs[1].toInt())
                Stubs.ServiceInterface(tmp1, tmp2, null, null, tmp3, CENTROID_ZB.toChar(), svPtr)
                CstringOps.strcpyC(bd, tmp3)
                return
            }

            ZLblZ.ZZTERMCORRELATE -> {
                if (nargs != 2.toShort()) { lab = ZLblZ.ERR; continue@machine }
                val tmp1 = CharArray(STR_MAX)
                tmp1[0] = NUL
                tmp2[0] = NUL
                strcpyAt(tmp1, 0, v1d, 1)
                strcpyAt(tmp2, 0, v1d, iargs[1].toInt())
                Stubs.ServiceInterface(tmp1, tmp2, null, null, tmp3, TERMCORRELATE_ZB.toChar(), svPtr)
                CstringOps.strcpyC(bd, tmp3)
                return
            }

            ZLblZ.ZZDOCCORRELATE -> {
                if (nargs != 4.toShort()) { lab = ZLblZ.ERR; continue@machine }
                val tmp1 = CharArray(STR_MAX)
                val tmp2a = CharArray(STR_MAX)
                val tmp2b = CharArray(STR_MAX)
                tmp1[0] = NUL
                tmp2[0] = NUL
                strcpyAt(tmp1, 0, v1d, 1)
                strcpyAt(tmp2, 0, v1d, iargs[1].toInt())
                strcpyAt(tmp2a, 0, v1d, iargs[2].toInt())
                strcpyAt(tmp2b, 0, v1d, iargs[3].toInt())
                Stubs.ServiceInterface(tmp1, tmp2, tmp2a, tmp2b, tmp3, DOCCORRELATE_ZB.toChar(), svPtr)
                CstringOps.strcpyC(bd, tmp3)
                return
            }

            ZLblZ.ZZIDF -> {
                if (nargs != 2.toShort()) { lab = ZLblZ.ERR; continue@machine }
                val tmp1 = CharArray(STR_MAX)
                tmp1[0] = NUL
                tmp2[0] = NUL
                strcpyAt(tmp1, 0, v1d, 1)
                strcpyAt(tmp2, 0, v1d, iargs[1].toInt())
                Stubs.ServiceInterface(tmp1, tmp2, null, null, tmp3, INVDF_ZB.toChar(), svPtr)
                CstringOps.strcpyC(bd, tmp3)
                return
            }

            ZLblZ.ZZDICE -> {
                if (nargs != 2.toShort()) { lab = ZLblZ.ERR; continue@machine }
                val tmp1 = CharArray(STR_MAX)
                tmp1[0] = NUL
                tmp2[0] = NUL
                strcpyAt(tmp1, 0, v1d, 1)
                strcpyAt(tmp2, 0, v1d, iargs[1].toInt())
                Stubs.ServiceInterface(tmp1, tmp2, null, null, tmp3, DICE_ZB.toChar(), svPtr)
                CstringOps.strcpyC(bd, tmp3)
                return
            }

            ZLblZ.ZFLUSH -> {
                Mglobal(FLUSH, tmp2, tmp3, svPtr)
                strcpyS_B(bd, "")
                return
            }

            ZLblZ.ZCHDIR -> {
                if (nargs != 1.toShort()) { lab = ZLblZ.ERR; continue@machine }
                i = chdirC(CstringOps.cstringAt(v1d, 1)).toLong()
                if (i == 0L) {
                    strcpyS_B(bd, "0")
                } else {
                    strcpyS_B(bd, "1")
                }
                return
            }

            ZLblZ.ZPOW -> {
                if (nargs != 2.toShort()) { lab = ZLblZ.ERR; continue@machine }
                expx(CstringOps.cstringAt(v1d, 1),
                    CstringOps.cstringAt(v1d, iargs[1].toInt()), bd)
                return
            }

            ZLblZ.ZLOG2 -> {
                if (nargs != 1.toShort()) { lab = ZLblZ.ERR; continue@machine }
                mpsLog2(bd, CstringOps.cstringAt(v1d, 1))
                return
            }

            ZLblZ.ZLOG10 -> {
                if (nargs != 1.toShort()) { lab = ZLblZ.ERR; continue@machine }
                mpsLog10(bd, CstringOps.cstringAt(v1d, 1))
                return
            }

            ZLblZ.ZLOG -> {
                if (nargs != 1.toShort()) { lab = ZLblZ.ERR; continue@machine }
                Stubs.mpsLog(bd, CstringOps.cstringAt(v1d, 1))
                return
            }

            ZLblZ.ZCOSINE -> {
                if (nargs != 1.toShort()) { lab = ZLblZ.ERR; continue@machine }
                Stubs.mpsCos(bd, CstringOps.cstringAt(v1d, 1))
                return
            }

            ZLblZ.ZTANGENT -> {
                if (nargs != 1.toShort()) { lab = ZLblZ.ERR; continue@machine }
                Stubs.mpsTan(bd, CstringOps.cstringAt(v1d, 1))
                return
            }

            ZLblZ.ZATANGENT -> {
                if (nargs != 1.toShort()) { lab = ZLblZ.ERR; continue@machine }
                mps_atan(bd, sliceAtZb(v1d, 1))
                return
            }

            ZLblZ.ZACOSINE -> {
                if (nargs != 1.toShort()) { lab = ZLblZ.ERR; continue@machine }
                mps_acos(bd, sliceAtZb(v1d, 1))
                return
            }

            ZLblZ.ZSINE -> {
                if (nargs != 1.toShort()) { lab = ZLblZ.ERR; continue@machine }
                mpsSin(bd, CstringOps.cstringAt(v1d, 1))
                return
            }

            ZLblZ.ZASINE -> {
                if (nargs != 1.toShort()) { lab = ZLblZ.ERR; continue@machine }
                mps_asin(bd, sliceAtZb(v1d, 1))
                return
            }

            ZLblZ.ZEXP -> {
                if (nargs != 1.toShort()) { lab = ZLblZ.ERR; continue@machine }
                mpsExp(bd, CstringOps.cstringAt(v1d, 1))
                return
            }

            ZLblZ.ZEXP2 -> {
                if (nargs != 1.toShort()) { lab = ZLblZ.ERR; continue@machine }
                Stubs.mpsExp2(bd, CstringOps.cstringAt(v1d, 1))
                return
            }

            ZLblZ.ZEXP10 -> {
                if (nargs != 1.toShort()) { lab = ZLblZ.ERR; continue@machine }
                mpsExp10(bd, CstringOps.cstringAt(v1d, 1))
                return
            }

            ZLblZ.ZHIT -> {
                if (svPtr.count == 0L) {
                    strcpyS_B(bd, "0")
                } else {
                    // sprintf("%lf") C locale (H-009 note)
                    strcpyS_B(bd, java.lang.String.format(
                        java.util.Locale.US, "%f",
                        svPtr.hit.toDouble() / svPtr.count.toDouble()))
                }
                svPtr.count = 0L
                svPtr.hit = 0L
                return
            }

            ZLblZ.ZLOWER -> {
                if (nargs != 1.toShort()) { lab = ZLblZ.ERR; continue@machine }
                // char bdx tracks the last copied char; the pointer
                // walk renders as the index walk
                var src = 1
                var dst = 0
                while (v1d[src] != NUL) {
                    bd[dst] = tolowerC(v1d[src])
                    dst++
                    src++
                }
                bd[dst] = NUL
                return
            }

            // znormal: / zalter: share this arm (vendor adjacent
            // labels, H-011 note in the file header)
            ZLblZ.ZNORMAL -> {
                if (nargs != 1.toShort() && nargs != 2.toShort()) { lab = ZLblZ.ERR; continue@machine }
                var jj = 0
                bd[0] = NUL
                var ii = 1
                while (v1d[ii] != NUL) {
                    if (isalnumC(v1d[ii])) {
                        bd[jj] = tolowerC(v1d[ii])
                        jj++
                    }
                    ii++
                }
                bd[jj] = NUL
                if (nargs == 2.toShort()) {
                    wordTrunc = atoiC(sliceAtZb(v1d, iargs[1].toInt()))
                } else {
                    wordTrunc = 25
                }
                // bd[WordTrunc]=0 index quantized into bounds
                // (H-018 note)
                if (wordTrunc in 0 until bd.size) bd[wordTrunc] = NUL
                return
            }

            ZLblZ.ZSTEM -> {
                if (nargs != 1.toShort()) { lab = ZLblZ.ERR; continue@machine }
                // j = 0 drop per H-013 (overwritten before read)
                bd[0] = NUL
                var ii = 1
                while (v1d[ii] != NUL) {
                    bd[ii - 1] = tolowerC(v1d[ii])
                    ii++
                }
                bd[ii - 1] = NUL
                // remove English language suffixes (wstem seat)
                Stubs.wstem(bd, CstringOps.strlenC(bd))
                return
            }

            ZLblZ.ZSEEK -> {
                if (nargs != 1.toShort()) { lab = ZLblZ.ERR; continue@machine }
                if (svPtr.io < 0 || svPtr.io > 5
                        || svPtr.in_file[svPtr.io] == null) {
                    bd[0] = NUL
                    return
                }
                ZfcnsState.fposZb = atolC(sliceAtZb(v1d, 1))
                fsetposC(svPtr.in_file[svPtr.io], ZfcnsState.fposZb)
                strcpyS_B(bd, "0")
                return
            }

            ZLblZ.ZTELL -> {
                if (svPtr.io < 0 || svPtr.io > 5) {
                    strcpyS_B(bd, "-1")
                    return
                }
                // the C FILE* is one domain on both the Reader and
                // Writer edges; the JVM Writer carries no fpos
                // domain, so the Writer arm passes the same raising
                // fgetpos carrier (H-014 OS-layer quantization)
                val p: java.io.Reader?
                if (svPtr.in_file[svPtr.io] != null) {
                    p = svPtr.in_file[svPtr.io]
                } else if (svPtr.out_file[svPtr.io] != null) {
                    p = null // the Writer edge (note above)
                } else {
                    strcpyS_B(bd, "-1")
                    return
                }
                val pv = fgetposC(p)
                strcpyS_B(bd, pv.toString())
                return
            }

            ZLblZ.ZWORD -> {
                // the zword parse-buffer helper (zwi/zwn/zwp/zws)
                when {
                    zcodeChar1(v1d) == 's' -> {
                        strcpyAt(ZfcnsState.linebuf, 0, v1d, 1)
                        ZfcnsState.linebufx = 0
                        bd[0] = NUL
                        return
                    }
                    zcodeChar1(v1d) == 'i' -> {
                        strcpyAt(ZfcnsState.linebuf, 0, v1d, 1)
                        ZfcnsState.linebufx = 0
                        var pi = 0
                        while (ZfcnsState.linebuf[pi] != NUL) {
                            ZfcnsState.linebuf[pi] =
                                tolowerC(ZfcnsState.linebuf[pi])
                            pi++
                        }
                        bd[0] = NUL
                        return
                    }
                    tolowerC(zcodeChar1(v1d)) == 'n' -> {
                        val lbuf = ZfcnsState.linebuf
                        if (lbuf[ZfcnsState.linebufx] == NUL) {
                            bd[0] = NUL
                            return
                        }
                        while (lbuf[ZfcnsState.linebufx] != NUL
                                && lbuf[ZfcnsState.linebufx] == ' ') {
                            ZfcnsState.linebufx++
                        }
                        if (lbuf[ZfcnsState.linebufx] == NUL) {
                            bd[0] = NUL
                            return
                        }
                        i = 0L
                        while (lbuf[ZfcnsState.linebufx] == '-'
                                || lbuf[ZfcnsState.linebufx] == '_') {
                            ZfcnsState.linebufx++
                        }
                        while (true) {
                            bd[i.toInt()] = lbuf[ZfcnsState.linebufx]
                            if (lbuf[ZfcnsState.linebufx] == NUL
                                    || lbuf[ZfcnsState.linebufx] == ' ') {
                                bd[i.toInt()] = NUL
                                return
                            }
                            i++
                            ZfcnsState.linebufx++
                        }
                    }
                    zcodeChar1(v1d) == 'p' -> {
                        val lbuf = ZfcnsState.linebuf
                        if (lbuf[ZfcnsState.linebufx] == NUL) {
                            bd[0] = NUL
                            return
                        }
                        while (lbuf[ZfcnsState.linebufx] != NUL
                                && lbuf[ZfcnsState.linebufx] == ' ') {
                            ZfcnsState.linebufx++
                        }
                        if (lbuf[ZfcnsState.linebufx] == NUL) {
                            bd[0] = NUL
                            return
                        }
                        val c0 = lbuf[ZfcnsState.linebufx]
                        if (c0 != '*' && c0 != '-'
                                && ispunctC(c0)) {
                            bd[0] = c0
                            bd[1] = NUL
                            ZfcnsState.linebufx++
                            return
                        }
                        i = 0L
                        while (true) {
                            bd[i.toInt()] = lbuf[ZfcnsState.linebufx]
                            val cc = lbuf[ZfcnsState.linebufx]
                            if (cc == NUL || cc == ' '
                                    || (cc != '*' && cc != '-' && ispunctC(cc))) {
                                bd[i.toInt()] = NUL
                                return
                            }
                            i++
                            ZfcnsState.linebufx++
                        }
                    }
                    else -> { }
                }
                // vendor #endif falls to goto err
                lab = ZLblZ.ERR
            }

            ZLblZ.ZNUMERIC -> {
                if (nargs != 1.toShort()) { lab = ZLblZ.ERR; continue@machine }
                // k = 0 drop per H-013 (never read)
                var ii = 1
                while (v1d[ii] != NUL) {
                    val c = v1d[ii]
                    if (isdigitC(c) || c == '.' || c == '+' || c == '-') {
                        ii++
                        continue
                    }
                    bd[0] = '0'
                    bd[1] = NUL
                    return
                }
                bd[0] = '1'
                bd[1] = NUL
                return
            }

            ZLblZ.ZBLNK -> {
                if (nargs != 1.toShort()) { lab = ZLblZ.ERR; continue@machine }
                // the zbl1 intra-arm goto renders inline (H-011
                // file header note)
                var src = 1
                var dst = 0
                var bdx: Char = NUL
                var zbl1 = false
                while (v1d[src] != NUL) {
                    if (v1d[src] != ' ') {
                        zbl1 = true
                        break
                    }
                    src++
                }
                if (!zbl1) {
                    bd[dst] = NUL
                    return
                }
                while (v1d[src] != NUL) {
                    if (bdx == ' ' && v1d[src] == ' ') {
                        src++
                        continue
                    }
                    bdx = v1d[src]
                    bd[dst] = v1d[src]
                    dst++
                    src++
                }
                bd[dst] = NUL
                return
            }

            ZLblZ.ZCURRENTFILE -> {
                strcpyS_B(bd, svPtr.CurrentFile)
                return
            }

            ZLblZ.ZHTML -> {
                if (nargs != 1.toShort()) { lab = ZLblZ.ERR; continue@machine }
                // k = 1 drop per H-013 (never read)
                var ii = 1
                while (v1d[ii] != NUL) {
                    if (v1d[ii] == ' ') {
                        v1d[ii] = '+'
                    } else if (!isalnumC(v1d[ii])) {
                        // sprintf(t1, "%%%2X") of the char
                        // sprintf(t1, "%%%2X") of the char (the
                        // trailing format NUL rides the note)
                        val t1 = "%" + java.lang.String.format(
                            java.util.Locale.US, "%2X", v1d[ii].code)
                        v1d[ii] = NUL
                        val t2 = (CstringOps.cstringAt(v1d, 1) + t1
                            + CstringOps.cstringAt(v1d, ii + 1))
                        // strmove(&v1d[1], t2) into the src buffer
                        val tb = CharArray(STR_MAX)
                        CstringOps.copyAtS(tb, 0, t2)
                        strcpyAt(v1d, 1, tb, 0)
                        ii = ii + 2
                    }
                    ii++
                }
                val t3 = CharArray(STR_MAX)
                strcpyAt(t3, 0, v1d, 1)
                CstringOps.strcpyC(bd, t3)
                return
            }

            ZLblZ.ZCONDENSE -> {
                // SQLITE live: return; the dump body drops per the
                // H-018 file header note
                return
            }

            ZLblZ.ZRESTORE -> {
                // SQLITE live: return; the load body drops per the
                // H-018 file header note
                return
            }

            ZLblZ.ZNXTROW -> {
                // SQLITE-live arm: banner then return; the walking
                // body behind it drops per the H-018 file header note
                System.out.print("\u0024zm not available\n")
                return
            }

            ZLblZ.ZABS -> {
                if (nargs != 1.toShort()) { lab = ZLblZ.ERR; continue@machine }
                if (mpsAbs(CstringOps.cstringAt(v1d, 1), bd) != 0) {
                    lab = ZLblZ.ERR; continue@machine
                }
                return
            }

            ZLblZ.ZPAD -> {
                if (nargs != 2.toShort()) { lab = ZLblZ.ERR; continue@machine }
                i = atolC(sliceAtZb(v1d, iargs[1].toInt()))
                strcpyAt(bd, 0, v1d, 1)
                // for (j = strlen(&v1d[1]); j < i; j++) bd[j] = ' '
                // (the pad index bound is the C buffer; quantized
                // in-bounds, H-018 note)
                var jj = CstringOps.strlenC(sliceAtZb(v1d, 1))
                val il = i.toInt()
                while (jj < il) {
                    if (jj >= bd.size) break
                    bd[jj] = ' '
                    jj++
                }
                bd[jj] = NUL
                return
            }

            ZLblZ.Z_VARIABLE -> {
                if (nargs != 1.toShort()) { lab = ZLblZ.ERR; continue@machine }
                if (Stubs.sym_(1, sliceAtZb(v1d, 1), bd, svPtr) == null) {
                    bd[0] = NUL
                }
                return
            }

            ZLblZ.ZDATE -> {
                // time_t dtime, timex; only the timex clock is live
                // (the dtime dump-file clock rides the dead zcondense
                // arm, H-018 header note); time(&timex) renders as the
                // epoch-second clock
                var timex = System.currentTimeMillis() / 1000L
                val zcode1c = tolowerC(v1d[4])
                if (zcode1c == '1') {
                    strcpyS_B(bd, timex.toString())
                } else if (zcode1c == '2') {
                    timex = atolC(sliceAtZb(v1d, 1))
                    val cbuf = CharArray(STR_MAX)
                    CstringOps.copyAtS(cbuf, 0, ctime(timex))
                    CstringOps.strcpyC(bd, cbuf)
                    bd[24] = NUL
                } else if (zcode1c == '3') {
                    if (nargs != 3.toShort()) { lab = ZLblZ.ERR; continue@machine }
                    val year = atolC(sliceAtZb(v1d, 1)).toInt()
                    val m = atolC(sliceAtZb(v1d, iargs[1].toInt())).toInt()
                    val d = atolC(sliceAtZb(v1d, iargs[2].toInt())).toInt()
                    if (year < 1900 || m < 1 || m > 12 || d < 1 || d > 31) {
                        lab = ZLblZ.ERR; continue@machine
                    }
                    val date = TmZz()
                    if (year > 1900) date.tm_year = year - 1900 else date.tm_year = year
                    date.tm_mon = m - 1
                    date.tm_mday = d
                    date.tm_hour = 12
                    date.tm_min = 0
                    date.tm_sec = 0
                    date.tm_yday = 0
                    date.tm_isdst = 1
                    mktime(date)
                    strcpyS_B(bd, (date.tm_yday + 1).toString())
                } else if (zcode1c == '4') {
                    // GnuDos stub arm drops per H-018 header note
                    if (nargs != 2.toShort()) { lab = ZLblZ.ERR; continue@machine }
                    var year = atolC(sliceAtZb(v1d, 1)).toInt()
                    if (year > 1900) year = year - 1900
                    val d = atolC(sliceAtZb(v1d, iargs[1].toInt())).toInt()
                    if (year < 0 || d > 366 || d < 1) { lab = ZLblZ.ERR; continue@machine }
                    var dd = d - 1
                    var m = 0
                    var ddm = 1
                    val date = TmZz()
                    while (true) {
                        date.tm_year = year
                        date.tm_mon = m
                        date.tm_mday = ddm
                        date.tm_hour = 12
                        date.tm_min = 0
                        date.tm_sec = 0
                        date.tm_yday = 0
                        date.tm_isdst = 1
                        mktime(date)
                        if (date.tm_yday == dd) break
                        ddm++
                        if (m == 0 && ddm > 31) { m++; ddm = 1 }
                        else if (m == 1 && (year % 4) == 0 && ddm > 29) { m++; ddm = 1 }
                        else if (m == 1 && (year % 4) != 0 && ddm > 28) { m++; ddm = 1 }
                        else if (m == 2 && ddm > 31) { m++; ddm = 1 }
                        else if (m == 3 && ddm > 30) { m++; ddm = 1 }
                        else if (m == 4 && ddm > 31) { m++; ddm = 1 }
                        else if (m == 5 && ddm > 30) { m++; ddm = 1 }
                        else if (m == 6 && ddm > 31) { m++; ddm = 1 }
                        else if (m == 7 && ddm > 31) { m++; ddm = 1 }
                        else if (m == 8 && ddm > 30) { m++; ddm = 1 }
                        else if (m == 9 && ddm > 31) { m++; ddm = 1 }
                        else if (m == 10 && ddm > 30) { m++; ddm = 1 }
                        else if (m == 11 && ddm > 31) { lab = ZLblZ.ERR; continue@machine }
                    }
                    if (date.tm_year < 100) {
                        strcpyS_B(bd, "19" + date.tm_year.toString() + " "
                            + (date.tm_mon + 1).toString() + " "
                            + date.tm_mday.toString())
                    } else {
                        // "20%02d" of tm_year - 100
                        strcpyS_B(bd, "20" + (date.tm_year - 100)
                            .toString().padStart(2, '0') + " "
                            + (date.tm_mon + 1).toString() + " "
                            + date.tm_mday.toString())
                    }
                } else if (zcode1c == '5') {
                    if (nargs != 3.toShort()) { lab = ZLblZ.ERR; continue@machine }
                    val year = atolC(sliceAtZb(v1d, 1)).toInt()
                    val m = atolC(sliceAtZb(v1d, iargs[1].toInt())).toInt()
                    val d = atolC(sliceAtZb(v1d, iargs[2].toInt())).toInt()
                    if (year < 1900 || m < 1 || m > 12 || d < 1 || d > 31) {
                        lab = ZLblZ.ERR; continue@machine
                    }
                    val date = TmZz()
                    if (year > 1900) date.tm_year = year - 1900 else date.tm_year = year
                    date.tm_mon = m - 1
                    date.tm_mday = d
                    date.tm_hour = 12
                    date.tm_min = 0
                    date.tm_sec = 0
                    date.tm_yday = 0
                    date.tm_wday = 0
                    date.tm_isdst = 0
                    mktime(date)
                    if (date.tm_year < 100) {
                        strcpyS_B(bd, "19" + date.tm_year.toString() + ","
                            + (date.tm_yday + 1).toString() + ","
                            + date.tm_wday.toString())
                    } else {
                        // oddball vendor arms: 20%02d of
                        // (tm_year - 100 - 1), tm_yday + 2
                        strcpyS_B(bd, "20" + (date.tm_year - 100 - 1)
                            .toString().padStart(2, '0') + ","
                            + (date.tm_yday + 2).toString() + ","
                            + date.tm_wday.toString())
                    }
                } else if (zcode1c == '6') {
                    val ct = ctime(timex)
                    // strmove(bd, ctime+11); bd[5]=0
                    CstringOps.copyAtS(bd, 0, ct.substring(11, ct.length))
                    bd[5] = NUL
                } else if (zcode1c == '7' || zcode1c == '8') {
                    if (nargs == 1.toShort()) {
                        timex = atolC(sliceAtZb(v1d, 1))
                    }
                    CstringOps.copyAtS(tmp2, 0, ctime(timex))
                    bd[1] = tmp2[20]
                    bd[2] = tmp2[21]
                    bd[3] = tmp2[22]
                    bd[4] = tmp2[23]
                    bd[5] = '-'
                    tmp2[7] = NUL
                    val mon = when (CstringOps.cstringAt(tmp2, 4)) {
                        "Jan" -> "01"; "Feb" -> "02"; "Mar" -> "03"
                        "Apr" -> "04"; "May" -> "05"; "Jun" -> "06"
                        "Jul" -> "07"; "Aug" -> "08"; "Sep" -> "09"
                        "Oct" -> "10"; "Nov" -> "11"; else -> "12"
                    }
                    bd[6] = mon[0]
                    bd[7] = mon[1]
                    bd[8] = '-'
                    bd[9] = tmp2[8]
                    bd[10] = tmp2[9]
                    bd[11] = NUL
                    for (jj in 0 until 11) {
                        if (bd[jj] == ' ') bd[jj] = '0'
                    }
                    if (zcode1c == '8') {
                        bd[11] = ','
                        CstringOps.copyAtS(bd, 12, ctime(timex).substring(11))
                        bd[17] = NUL
                    }
                    strmoveAt(bd, 0, 1)
                } else {
                    if (nargs != 0.toShort()) { lab = ZLblZ.ERR; continue@machine }
                    val cbuf = CharArray(STR_MAX)
                    CstringOps.copyAtS(cbuf, 0, ctime(timex))
                    CstringOps.strcpyC(bd, cbuf)
                    bd[24] = NUL
                }
                return
            }

            ZLblZ.ZFILE -> {
                // long fd = open(path, 0); x = fd; close(fd) ->
                // the openC exists() quantization (H-018 note)
                val x = openC(CstringOps.cstringAt(v1d, 1))
                if (x == -1) {
                    bd[0] = '0'
                    bd[1] = NUL
                    return
                }
                bd[0] = '1'
                bd[1] = NUL
                return
            }

            ZLblZ.ZGLOBAL -> {
                // SQLITE live arm (delta ledger 5): ServiceInterface
                // GBLNME 20 relay (the bifs twin runs the g=5
                // count-empties Mglobal probe; the #else arm returns
                // "" and drops per H-018); vendor declares arm-local
                // tmp1 over the fun-scope tmp2/tmp3 relay carriers
                if (nargs != 1.toShort()) { lab = ZLblZ.ERR; continue@machine }
                val gtmp1 = CharArray(STR_MAX)
                gtmp1[0] = NUL
                tmp2[0] = NUL
                strcpyAt(gtmp1, 0, v1d, 1)
                Stubs.ServiceInterface(gtmp1, tmp2, null, null, tmp3, GBLNME_ZB.toChar(), svPtr)
                CstringOps.strcpyC(bd, tmp3)
                return
            }

            ZLblZ.ZROOT -> {
                if (nargs != 1.toShort()) { lab = ZLblZ.ERR; continue@machine }
                mps_sqrt(bd, sliceAtZb(v1d, 1))
                return
            }

            ZLblZ.ZSYSTEM -> {
                if (nargs != 1.toShort()) { lab = ZLblZ.ERR; continue@machine }
                val x = systemC(CstringOps.cstringAt(v1d, 1))
                strcpyS_B(bd, x.toString())
                return
            }

            ZLblZ.FCN_NOT_FOUND -> {
                if (svPtr.NOERR != 0) {
                    svPtr.NOERR = -1
                    return
                }
                System.out.print("\n*** Z function: \"" + CstringOps.cstring(fcnName)
                    + "\" not found in or near line "
                    + svPtr.LineNumber.toString() + "\n")
                Stubs.sigint(100)
                // vendor falls through into err; same observable here
                if (svPtr.NOERR != 0) {
                    svPtr.NOERR = -1
                    return
                }
                System.err.print("\n*** zFunction error in or near line "
                    + svPtr.LineNumber.toString() + "\n")
                Stubs.sigint(100)
            }

            ZLblZ.ERR -> {
                if (svPtr.NOERR != 0) {
                    svPtr.NOERR = -1
                    return
                }
                System.err.print("\n*** zFunction error in or near line "
                    + svPtr.LineNumber.toString() + "\n")
                Stubs.sigint(100)
                // end of vendor body
            }
        }
    }
}
