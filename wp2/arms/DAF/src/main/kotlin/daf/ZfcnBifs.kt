package daf

// Unit 183 (batch-6 big three): interp-bifs.cpp::zfcn (physLOC
// 1622; core-adjacent stratum; score and rank ride the units.csv
// row). The DEAD build-alternate twin of the live unit-184
// interp-zfcns TU: the vendor Makefile links interp-zfcns.o ONLY
// (interp-bifs.o is NOT compiled into libmumps.a), so this render
// is the H-017 build-alternate - the vendor C symbol zfcn renders
// as fun zfcnBifsDaf (the zfcns.h _INTERP_ selector analog), the
// recursion seat resolves to the LIVE unit-184 render zfcnDaf.
// The body carries the zreplace arm the live TU lacks (the
// replaceDaf seat raised at this file), the SQLITE index arms are
// absent (grep-verified in the vendor #if lattice of this TU), the
// generic sql arm passes PRAGMA (29) where the live twin passes
// SQLCMD (55), and the zglobal arm runs the g=5 count-empties
// Mglobal probe where the live twin relays ServiceInterface GBLNME
// (20).
//
// H-011: the goto ladder renders as the ZLblB enum state machine
// (label per goto target, one when-arm per vendor label, one
// continue@machine per goto, plain return per C return); the
// private object ZfcnBifsState carries the five C statics
// (H-004); the struct tm carrier renders as TmZb (the H-017
// per-file class-name rule).
//
// H-018: this TU is NOT compiled with SQLITE defined at the
// reference build either (the TU is not compiled at all), so the
// SQLITE arms render with the live-twin layout notes.
//
// H-014 DAF seat map (mirrors the unit-184 Zfcns.kt release):
// raises at this file - replaceDaf (builtin.h:40 API entry, NOT a
// corpus unit; the zreplace-arm ledger), plus the bifs-only
// doubly-owned seats resolved at Zfcns.kt (serviceInterfaceDaf,
// gtkTreeAddD, the gtk*D library glue family, the mps*D family
// raises) are SHARED package seats - this file calls them, it
// does not re-raise them (the H-012 same-name family rule).
// Gate delegations - sqlDaf, wstemD, symRetDaf, stopLookupD,
// sigintDaf, stopInitD, SynInitW/SynLookupW/BMGSearchW/ShredW/
// ShredQueryW, SmithWatermanW, mpsLog2D/mpsLog10D/mpsAbsD/
// mpsSinD/mpsAsin, expxD, MglobalDaf.
//
// Consult: units.csv roster 183 (2026-11-22T16:30:30Z), the
// big-three session band.

// shell command-substitution backtick (the H-015 scan bans the
// backtick literal; the echo-command strings carry it by concat)
private const val BT_ZB: Char = '\u0060'
private const val OPEN_ZB: Char = '\u00CE' // defines.h:169 OPEN 206 (per-file seat; H-012 rule)
private const val CLOSE_ZB: Char = '\u00CF' // defines.h:170 CLOSE 207
private const val COMMA_ZB: Char = '\u00D0' // defines.h:171 COMMA 208

// defines.h:226-244 ServiceInterface control codes (per-file seats
// of this TU; the unit-184 twin re-renders its own copies, H-012
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

// defines.h:95-99 sql codes (per-file seats of this TU)
private const val BEGIN_TRANSACTION_ZB = 25
private const val COMMIT_TRANSACTION_ZB = 26
private const val ROLLBACK_TRANSACTION_ZB = 27
private const val SAVEPOINT_ZB = 28
private const val PRAGMA_ZB = 29

// vendor statics, H-004 notes in the file header
private object ZfcnBifsState {
    // static char linebuf[STR_MAX_DAF]; static int linebufx; (zword)
    val linebuf = CharArray(STR_MAX_DAF)
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
private enum class ZLblB {
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
    ZSQLOPEN, ZNATIVE, ZSQLITE, ZSRAND, ZSMITHWATERMAN, ZREPLACE,
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
private class TmZb {
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

private fun mktime(date: TmZb) {
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
    val s = DafCRuntime.cstring(buf)
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
    while (buf[s] != NUL_D) { buf[d] = buf[s]; d++; s++ }
    buf[d] = NUL_D
}

// strcpy/strmove of a slice into a buffer at an offset
private fun strcpyAt(dst: CharArray, doff: Int, src: CharArray, soff: Int) {
    var d = doff
    var s = soff
    while (src[s] != NUL_D) { dst[d] = src[s]; d++; s++ }
    dst[d] = NUL_D
}

private fun strcpyS_B(dst: CharArray, src: String) {
    strcpySZc(dst, src)
}

// bounded N-slice as a String (strlen-bounded window)
private fun sliceStr(buf: CharArray, off: Int, max: Int): String {
    val sb = StringBuilder()
    var i = off
    var n = 0
    while (buf[i] != NUL_D && n < max) { sb.append(buf[i]); i++; n++ }
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
    while (e < buf.size && buf[e] != NUL_D) e++
    return buf.copyOfRange(off, e)
}

// String into a zero-terminated CharArray carrier
private fun toZb(src: String): CharArray {
    val b = CharArray(STR_MAX_DAF)
    copyAtSZc(b, 0, src)
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
    ZfcnBifsState.srandSeed = seed
}

fun zfcnBifsDaf(v1d: CharArray, bd: CharArray, svPtr: DafMsv) {
    // unsigned char tmp2[STR_MAX_DAF], tmp3[STR_MAX_DAF], tmp4[STR_MAX_DAF]
    val tmp2 = CharArray(STR_MAX_DAF)
    val tmp3 = CharArray(STR_MAX_DAF)
    val tmp4 = CharArray(STR_MAX_DAF)
    // gbltbl[128]/gblstr[STR_MAX_DAF] drop per H-013 (only the dead
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

    var lab = ZLblB.DISPATCH
    machine@ while (true) {
        when (lab) {
            // setup + the branch table (v1d/OPEN parse)
            ZLblB.DISPATCH -> {
                svPtr.ierr = 0
                iargs[0] = 1
                val zcode = tolowerC(v1d[3])
                val zcode1 = tolowerC(v1d[4])
                // zcode2/zcode3 drop per H-013 (parsed, never read)

                i = 1L
                while (v1d[i.toInt()] != OPEN_ZB && v1d[i.toInt()] != NUL_D) i++
                // strncpy(fcnName, &v1d[2], i-2): the 64-buffer bound
                // quantizes a >61-char name (the dead C overflow
                // edge, H-013 note); vendor parity for all live names
                var ci = 2
                var cn = 0
                while (cn < (i - 2).toInt() && cn < 63) {
                    fcnName[cn] = v1d[ci]
                    ci++; cn++
                }
                if (cn < 63) fcnName[cn] = NUL_D
                val fcn = DafCRuntime.cstring(fcnName)

                while (v1d[1] != OPEN_ZB && v1d[1] != NUL_D) {
                    strmoveAt(v1d, 1, 2)
                }

                if (zcode == 'g' && (zcode1 == 'a' || zcode1 == 'c'
                        || zcode1 == 's')) {
                    // zglobal
                    strmoveAt(v1d, 1, 2)
                    i = 1L
                    while (v1d[i.toInt()] != NUL_D) i++
                    if (v1d[(i - 1).toInt()] != CLOSE_ZB
                            || v1d[(i - 2).toInt()] != CLOSE_ZB) {
                        lab = ZLblB.ERR; continue@machine
                    }
                    v1d[(i - 1).toInt()] = NUL_D
                } else {
                    if (v1d[1] == OPEN_ZB) strmoveAt(v1d, 1, 2)
                    i = 1L
                    while (v1d[i.toInt()] != NUL_D) {
                        if (v1d[i.toInt()] == CLOSE_ZB
                                || v1d[i.toInt()] == COMMA_ZB) {
                            v1d[i.toInt()] = NUL_D
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
                    if (strncasecmpC(fcn, "z~mdh~file~chooser~get~filename", 31)) { lab = ZLblB.GTK_FILE_CHOOSER; continue@machine }
                    if (strncasecmpC(fcn, "z~mdh~calendar~get~date", 23)) { lab = ZLblB.GTK_CALENDAR; continue@machine }
                    if (strncasecmpC(fcn, "z~mdh~range~get~adjustment", 26)) { lab = ZLblB.GTK_GET_RANGE_ADJUSTMENT; continue@machine }
                    if (strncasecmpC(fcn, "z~mdh~font~chooser~get~font", 27)) { lab = ZLblB.GTK_FONT_CHOOSER; continue@machine }
                    if (strncasecmpC(fcn, "z~mdh~color~chooser~get~rgba", 28)) { lab = ZLblB.GTK_COLOR_CHOOSER; continue@machine }
                    if (strncasecmpC(fcn, "z~mdh~switch~get~active", 23)) { lab = ZLblB.GTK_SWITCH_GET_ACTIVE; continue@machine }
                    if (strncasecmpC(fcn, "z~mdh~toggle~button~get~active", 30)) { lab = ZLblB.GTK_TOGGLE_BUTTON_GET_ACTIVE; continue@machine }
                    if (strncasecmpC(fcn, "z~mdh~toggle~button~set~active", 30)) { lab = ZLblB.GTK_TOGGLE_BUTTON_SET_ACTIVE; continue@machine }
                    if (strncasecmpC(fcn, "z~mdh~spin~button~set~value", 27)) { lab = ZLblB.GTK_SPIN_BUTTON_SET_VALUE; continue@machine }
                    if (strncasecmpC(fcn, "z~mdh~entry~get~text", 20)) { lab = ZLblB.GTK_ENTRY_GET_TEXT; continue@machine }
                    if (strncasecmpC(fcn, "z~mdh~entry~set~text", 20)) { lab = ZLblB.GTK_ENTRY_SET_TEXT; continue@machine }
                    if (strncasecmpC(fcn, "z~mdh~text~buffer~set~text", 22)) { lab = ZLblB.GTK_TEXT_BUFFER_SET_TEXT; continue@machine }
                    if (strncasecmpC(fcn, "z~mdh~label~set~text", 20)) { lab = ZLblB.GTK_LABEL_SET_TEXT; continue@machine }
                    if (strncasecmpC(fcn, "z~mdh~dialog~new~with~buttons", 29)) { lab = ZLblB.GTK_DIALOG_WITH_BUTTONS; continue@machine }
                    if (strncasecmpC(fcn, "z~mdh~tree~view~get~selection", 29)) { lab = ZLblB.GTK_TREE_VIEW_GET_SELECTION; continue@machine }
                    if (strncasecmpC(fcn, "z~mdh~tree~selection~get~selected", 33)) { lab = ZLblB.GTK_TREE_SELECTION_GET_SELECTED; continue@machine }
                    if (strncasecmpC(fcn, "z~mdh~tree~model~get", 20)) { lab = ZLblB.GTK_TREE_MODEL_GET; continue@machine }
                    if (strncasecmpC(fcn, "z~mdh~tree~store~clear", 22)) { lab = ZLblB.GTK_TREE_STORE_CLEAR; continue@machine }
                    if (strncasecmpC(fcn, "z~mdh~tree~level~add", 20)) { lab = ZLblB.GTK_TREE_LEVEL_ADD; continue@machine }
                    if (strncasecmpC(fcn, "z~mdh~spin~button~get~value", 27)) { lab = ZLblB.GTK_SPIN_BUTTON_GET_VALUE; continue@machine }
                    if (strncasecmpC(fcn, "z~mdh~widget~show", 17)) { lab = ZLblB.GTK_WIDGET_SHOW; continue@machine }
                    if (strncasecmpC(fcn, "z~mdh~widget~hide", 17)) { lab = ZLblB.GTK_WIDGET_HIDE; continue@machine }
                    lab = ZLblB.FCN_NOT_FOUND; continue@machine
                }
                if (strncasecmpC(fcn, "zz", 2)) {
                    if (strcasecmpC(fcn, "zzsoundex")) { lab = ZLblB.ZZSOUNDEX; continue@machine }
                    if (strcasecmpC(fcn, "zzcosine")) { lab = ZLblB.ZZCOSINE; continue@machine }
                    if (strcasecmpC(fcn, "zzinput")) { lab = ZLblB.ZZINPUT; continue@machine }
                    if (strcasecmpC(fcn, "zzdice")) { lab = ZLblB.ZZDICE; continue@machine }
                    if (strcasecmpC(fcn, "zzjaccard")) { lab = ZLblB.ZZJACCARD; continue@machine }
                    if (strcasecmpC(fcn, "zzsim1")) { lab = ZLblB.ZZSIM1; continue@machine }
                    if (strcasecmpC(fcn, "zzscanalnum")) { lab = ZLblB.ZZSCANALNUM; continue@machine }
                    if (strcasecmpC(fcn, "zzscan")) { lab = ZLblB.ZZSCAN; continue@machine }
                    if (strcasecmpC(fcn, "zzavg")) { lab = ZLblB.ZZAVG; continue@machine }
                    if (strcasecmpC(fcn, "zzidf")) { lab = ZLblB.ZZIDF; continue@machine }
                    if (strcasecmpC(fcn, "zztranspose")) { lab = ZLblB.ZZTRANSPOSE; continue@machine }
                    if (strcasecmpC(fcn, "zzcentroid")) { lab = ZLblB.ZZCENTROID; continue@machine }
                    if (strcasecmpC(fcn, "zztermcorrelate")) { lab = ZLblB.ZZTERMCORRELATE; continue@machine }
                    if (strcasecmpC(fcn, "zzdoccorrelate")) { lab = ZLblB.ZZDOCCORRELATE; continue@machine }
                    if (strcasecmpC(fcn, "zzcount")) { lab = ZLblB.ZZCOUNT; continue@machine }
                    if (strcasecmpC(fcn, "zzmax")) { lab = ZLblB.ZZMAX; continue@machine }
                    if (strcasecmpC(fcn, "zzmin")) { lab = ZLblB.ZZMIN; continue@machine }
                    if (strcasecmpC(fcn, "zzsum")) { lab = ZLblB.ZZSUM; continue@machine }
                    if (strcasecmpC(fcn, "zzmultiply")) { lab = ZLblB.ZZMULTIPLY; continue@machine }
                    lab = ZLblB.FCN_NOT_FOUND; continue@machine
                }
                if (strncasecmpC(fcn, "zs", 2)) {
                    if (strcasecmpC(fcn, "zstem")) { lab = ZLblB.ZSTEM; continue@machine }
                    if (strncasecmpC(fcn, "zstopl", 6)) { lab = ZLblB.ZSTOPLOOKUP; continue@machine }
                    if (strcasecmpC(fcn, "zsin")) { lab = ZLblB.ZSINE; continue@machine }
                    if (strcasecmpC(fcn, "zseek")) { lab = ZLblB.ZSEEK; continue@machine }
                    if (strcasecmpC(fcn, "zsmithwaterman")) { lab = ZLblB.ZSMITHWATERMAN; continue@machine }
                    if (strcasecmpC(fcn, "zstopinit")) { lab = ZLblB.ZSTOPINIT; continue@machine }
                    if (strcasecmpC(fcn, "zsyninit")) { lab = ZLblB.ZSYNINIT; continue@machine }
                    if (strcasecmpC(fcn, "zsynlookup")) { lab = ZLblB.ZSYNLOOKUP; continue@machine }
                    if (strcasecmpC(fcn, "zshred")) { lab = ZLblB.ZSHRED; continue@machine }
                    if (strcasecmpC(fcn, "zshredquery")) { lab = ZLblB.ZSHREDQUERY; continue@machine }
                    if (strcasecmpC(fcn, "zsrand")) { lab = ZLblB.ZSRAND; continue@machine }
                    if (strcasecmpC(fcn, "zsqlite")) { lab = ZLblB.ZSQLITE; continue@machine }
                    if (strcasecmpC(fcn, "zsqlopen")) { lab = ZLblB.ZSQLOPEN; continue@machine }
                    if (strcasecmpC(fcn, "zsqrt")) { lab = ZLblB.ZROOT; continue@machine }
                    if (strcasecmpC(fcn, "zsystem")) { lab = ZLblB.ZSYSTEM; continue@machine }
                    lab = ZLblB.FCN_NOT_FOUND; continue@machine
                }
                if (strncasecmpC(fcn, "zlow", 4)) { lab = ZLblB.ZLOWER; continue@machine }
                if (strcasecmpC(fcn, "zcos")) { lab = ZLblB.ZCOSINE; continue@machine }
                if (strcasecmpC(fcn, "zgetenv")) { lab = ZLblB.ZGETENV; continue@machine }
                if (strcasecmpC(fcn, "ztan")) { lab = ZLblB.ZTANGENT; continue@machine }
                if (strcasecmpC(fcn, "zatan")) { lab = ZLblB.ZATANGENT; continue@machine }
                if (strcasecmpC(fcn, "zacos")) { lab = ZLblB.ZACOSINE; continue@machine }
                if (strcasecmpC(fcn, "zasin")) { lab = ZLblB.ZASINE; continue@machine }
                if (strcasecmpC(fcn, "zexp")) { lab = ZLblB.ZEXP; continue@machine }
                if (strcasecmpC(fcn, "zexp2")) { lab = ZLblB.ZEXP2; continue@machine }
                if (strcasecmpC(fcn, "zexp10")) { lab = ZLblB.ZEXP10; continue@machine }
                if (strcasecmpC(fcn, "zlog")) { lab = ZLblB.ZLOG; continue@machine }
                if (strcasecmpC(fcn, "zlog10")) { lab = ZLblB.ZLOG10; continue@machine }
                if (strcasecmpC(fcn, "zlog2")) { lab = ZLblB.ZLOG2; continue@machine }
                if (strcasecmpC(fcn, "zpow")) { lab = ZLblB.ZPOW; continue@machine }
                if (strcasecmpC(fcn, "zhit")) { lab = ZLblB.ZHIT; continue@machine }
                if (strcasecmpC(fcn, "zchdir")) { lab = ZLblB.ZCHDIR; continue@machine }
                if (strcasecmpC(fcn, "zflush")) { lab = ZLblB.ZFLUSH; continue@machine }
                if (strcasecmpC(fcn, "znoblanks")) { lab = ZLblB.ZNOBLANKS; continue@machine }
                if (strcasecmpC(fcn, "zpad")) { lab = ZLblB.ZPAD; continue@machine }
                if (strcasecmpC(fcn, "zbmgsearch")) { lab = ZLblB.ZBMGSEARCH; continue@machine }
                if (strcasecmpC(fcn, "zreplace")) { lab = ZLblB.ZREPLACE; continue@machine }
                if (strcasecmpC(fcn, "znative")) { lab = ZLblB.ZNATIVE; continue@machine }
                if (strcasecmpC(fcn, "zdump")) { lab = ZLblB.ZCONDENSE; continue@machine }
                if (strcasecmpC(fcn, "zrestore")) { lab = ZLblB.ZRESTORE; continue@machine }
                if (strcasecmpC(fcn, "zcurrentfile")) { lab = ZLblB.ZCURRENTFILE; continue@machine }
                if (strcasecmpC(fcn, "zceil")) { lab = ZLblB.ZCEIL; continue@machine }
                if (strcasecmpC(fcn, "zfloor")) { lab = ZLblB.ZFLOOR; continue@machine }
                if (strcasecmpC(fcn, "ztrunc")) { lab = ZLblB.ZTRUNC; continue@machine }
                if (strcasecmpC(fcn, "zprogram")) { lab = ZLblB.ZPROGRAM; continue@machine }
                if (strcasecmpC(fcn, "zdatabase")) { lab = ZLblB.ZDATABASE; continue@machine }
                if (strcasecmpC(fcn, "zbasename")) { lab = ZLblB.ZBASENAME; continue@machine }
                if (strcasecmpC(fcn, "zfiletest")) { lab = ZLblB.ZFILETEST; continue@machine }
                if (zcode == 'a') {
                    if (zcode1 == 'b') { lab = ZLblB.ZABS; continue@machine }
                    lab = ZLblB.ZNORMAL; continue@machine // zalter
                }
                if (zcode == 'd') { lab = ZLblB.ZDATE; continue@machine }
                if (zcode == 'v') { lab = ZLblB.Z_VARIABLE; continue@machine }
                if (zcode == 'n') { lab = ZLblB.ZNORMAL; continue@machine }
                if (zcode == 'b') { lab = ZLblB.ZBLNK; continue@machine }
                if (zcode == 'p') { lab = ZLblB.ZPAD; continue@machine }
                if (zcode == 'h') { lab = ZLblB.ZHTML; continue@machine }
                if (zcode == 'f') { lab = ZLblB.ZFILE; continue@machine }
                if (zcode == 'g') { lab = ZLblB.ZGLOBAL; continue@machine }
                if (zcode == 'l') { lab = ZLblB.ZLOG; continue@machine }
                if (zcode == 'm') { lab = ZLblB.ZNXTROW; continue@machine }
                if (zcode == 'w') { lab = ZLblB.ZWORD; continue@machine }
                if (zcode == 't') { lab = ZLblB.ZTELL; continue@machine }
                if (zcode == 'u') { lab = ZLblB.ZNUMERIC; continue@machine }
                lab = ZLblB.FCN_NOT_FOUND
            }

            ZLblB.GTK_DIALOG_WITH_BUTTONS -> {
                if (nargs != 2.toShort()) { lab = ZLblB.ERR; continue@machine }
                // sscanf("%p") handle recovery (the unit-83 GtkTreeAdd
                // percent-p divergence note precedent)
                val window = DafCRuntime.cstringAt(v1d, 1)
                // flags: (GtkDialogFlags)(GTK_DIALOG_DESTROY_WITH_PARENT
                // | GTK_MESSAGE_ERROR | GTK_DIALOG_MODAL
                // | GTK_DIALOG_USE_HEADER_BAR) = 7, the C compile-time
                // constant kept
                val flags = 7
                val dialog = gtkDialogNewWithButtonsD(
                    DafCRuntime.cstringAt(v1d, iargs[1].toInt()), window,
                    flags, arrayOf(kotlin.Pair("Yes", 100), kotlin.Pair("No", 200)))
                gtkWindowSetKeepAboveD(dialog, true)
                val r = gtkDialogRunD(dialog)
                gtkWidgetDestroyD(dialog)
                if (r == 100) {
                    strcpyS_B(bd, "1")
                } else if (r == 200) {
                    strcpyS_B(bd, "0")
                } else {
                    strcpyS_B(bd, "-1")
                }
                return
            }

            ZLblB.GTK_WIDGET_HIDE -> {
                val widget = DafCRuntime.cstringAt(v1d, 1)
                gtkWidgetHideD(widget)
                bd[0] = NUL_D
                return
            }

            ZLblB.GTK_WIDGET_SHOW -> {
                val widget = DafCRuntime.cstringAt(v1d, 1)
                gtkWidgetShowD(widget)
                bd[0] = NUL_D
                return
            }

            ZLblB.GTK_SPIN_BUTTON_GET_VALUE -> {
                if (nargs != 1.toShort()) { lab = ZLblB.ERR; continue@machine }
                val spin = DafCRuntime.cstringAt(v1d, 1)
                val xv = gtkSpinButtonGetValueD(spin)
                strcpyS_B(bd, formatF(xv))
                return
            }

            ZLblB.GTK_SPIN_BUTTON_SET_VALUE -> {
                if (nargs != 2.toShort()) { lab = ZLblB.ERR; continue@machine }
                val spin = DafCRuntime.cstringAt(v1d, 1)
                val valx = atofC(sliceAtZb(v1d, iargs[1].toInt()))
                gtkSpinButtonSetValueD(spin, valx)
                bd[0] = NUL_D
                return
            }

            ZLblB.GTK_TREE_STORE_CLEAR -> {
                if (nargs != 1.toShort()) { lab = ZLblB.ERR; continue@machine }
                val t = DafCRuntime.cstringAt(v1d, 1)
                gtkTreeStoreClearD(t)
                strcpyS_B(bd, "1")
                return
            }

            ZLblB.GTK_TREE_MODEL_GET -> {
                if (nargs != 5.toShort()) { lab = ZLblB.ERR; continue@machine }
                val m = DafCRuntime.cstringAt(v1d, 1)
                // sscanf(&v1d[iargs[1]], "%p", &iter): opaque handle
                ZfcnBifsState.iter[0] = pctToZb(v1d, iargs[1].toInt())
                val value = gtkTreeModelGetD(m, ZfcnBifsState.iter, 1)
                strcpyS_B(bd, value)
                return
            }

            ZLblB.GTK_TREE_SELECTION_GET_SELECTED -> {
                if (nargs != 2.toShort()) { lab = ZLblB.ERR; continue@machine }
                val s = DafCRuntime.cstringAt(v1d, 1)
                val col = atoiC(sliceAtZb(v1d, iargs[1].toInt()))
                val m = StrHolderZc()
                if (!gtkTreeSelectionGetSelectedD(s, m, ZfcnBifsState.iter)) {
                    strcpyS_B(bd, "0")
                    return
                }
                bd[0] = NUL_D
                val value = gtkTreeModelGetD(m.value, ZfcnBifsState.iter, col)
                strcpyS_B(bd, value)
                return
            }

            ZLblB.GTK_TREE_VIEW_GET_SELECTION -> {
                if (nargs != 1.toShort()) { lab = ZLblB.ERR; continue@machine }
                val t = DafCRuntime.cstringAt(v1d, 1)
                val s = gtkTreeViewGetSelectionD(t)
                // sprintf("%p") handle text
                strcpyS_B(bd, s)
                return
            }

            ZLblB.GTK_TREE_LEVEL_ADD -> {
                if (nargs < 4.toShort()) { lab = ZLblB.ERR; continue@machine }
                val depth = atoiC(sliceAtZb(v1d, iargs[1].toInt()))
                // vendor fall-through quirk: a depth outside 1-10
                // falls through the if-chain into the
                // GtkTreeLevel1Add label; kept verbatim
                lab = when (depth) {
                    1 -> ZLblB.GTK_TREE_LEVEL1_ADD
                    2 -> ZLblB.GTK_TREE_LEVEL2_ADD
                    3 -> ZLblB.GTK_TREE_LEVEL3_ADD
                    4 -> ZLblB.GTK_TREE_LEVEL4_ADD
                    5 -> ZLblB.GTK_TREE_LEVEL5_ADD
                    6 -> ZLblB.GTK_TREE_LEVEL6_ADD
                    7 -> ZLblB.GTK_TREE_LEVEL7_ADD
                    8 -> ZLblB.GTK_TREE_LEVEL8_ADD
                    9 -> ZLblB.GTK_TREE_LEVEL9_ADD
                    10 -> ZLblB.GTK_TREE_LEVEL10_ADD
                    else -> ZLblB.GTK_TREE_LEVEL1_ADD
                }
            }

            ZLblB.GTK_TREE_LEVEL1_ADD -> {
                gtkTreeAddD(ZfcnBifsState.iter, null, sliceAtZb(v1d, 1), v1d, iargs, nargs)
                strcpyS_B(bd, "0")
                return
            }

            ZLblB.GTK_TREE_LEVEL2_ADD -> {
                gtkTreeAddD(ZfcnBifsState.iter1, ZfcnBifsState.iter, sliceAtZb(v1d, 1), v1d, iargs, nargs)
                strcpyS_B(bd, "0")
                return
            }

            ZLblB.GTK_TREE_LEVEL3_ADD -> {
                gtkTreeAddD(ZfcnBifsState.iter2, ZfcnBifsState.iter1, sliceAtZb(v1d, 1), v1d, iargs, nargs)
                strcpyS_B(bd, "0")
                return
            }

            ZLblB.GTK_TREE_LEVEL4_ADD -> {
                gtkTreeAddD(ZfcnBifsState.iter3, ZfcnBifsState.iter2, sliceAtZb(v1d, 1), v1d, iargs, nargs)
                strcpyS_B(bd, "0")
                return
            }

            ZLblB.GTK_TREE_LEVEL5_ADD -> {
                gtkTreeAddD(ZfcnBifsState.iter4, ZfcnBifsState.iter3, sliceAtZb(v1d, 1), v1d, iargs, nargs)
                strcpyS_B(bd, "0")
                return
            }

            ZLblB.GTK_TREE_LEVEL6_ADD -> {
                gtkTreeAddD(ZfcnBifsState.iter5, ZfcnBifsState.iter4, sliceAtZb(v1d, 1), v1d, iargs, nargs)
                strcpyS_B(bd, "0")
                return
            }

            ZLblB.GTK_TREE_LEVEL7_ADD -> {
                gtkTreeAddD(ZfcnBifsState.iter6, ZfcnBifsState.iter5, sliceAtZb(v1d, 1), v1d, iargs, nargs)
                strcpyS_B(bd, "0")
                return
            }

            ZLblB.GTK_TREE_LEVEL8_ADD -> {
                gtkTreeAddD(ZfcnBifsState.iter7, ZfcnBifsState.iter6, sliceAtZb(v1d, 1), v1d, iargs, nargs)
                strcpyS_B(bd, "0")
                return
            }

            ZLblB.GTK_TREE_LEVEL9_ADD -> {
                gtkTreeAddD(ZfcnBifsState.iter8, ZfcnBifsState.iter7, sliceAtZb(v1d, 1), v1d, iargs, nargs)
                strcpyS_B(bd, "0")
                return
            }

            ZLblB.GTK_TREE_LEVEL10_ADD -> {
                gtkTreeAddD(ZfcnBifsState.iter9, ZfcnBifsState.iter8, sliceAtZb(v1d, 1), v1d, iargs, nargs)
                strcpyS_B(bd, "0")
                return
            }

            ZLblB.GTK_LABEL_SET_TEXT -> {
                if (nargs != 2.toShort()) { lab = ZLblB.ERR; continue@machine }
                val l = DafCRuntime.cstringAt(v1d, 1)
                gtkLabelSetTextD(l, DafCRuntime.cstringAt(v1d, iargs[1].toInt()))
                strcpyS_B(bd, "1")
                return
            }

            ZLblB.GTK_TEXT_BUFFER_SET_TEXT -> {
                if (nargs != 2.toShort()) { lab = ZLblB.ERR; continue@machine }
                val b = DafCRuntime.cstringAt(v1d, 1)
                // (gint) -1 length passes as -1 verbatim
                gtkTextBufferSetTextD(b, DafCRuntime.cstringAt(v1d, iargs[1].toInt()), -1)
                strcpyS_B(bd, "1")
                return
            }

            ZLblB.GTK_ENTRY_GET_TEXT -> {
                if (nargs != 1.toShort()) { lab = ZLblB.ERR; continue@machine }
                val e = DafCRuntime.cstringAt(v1d, 1)
                strcpyS_B(bd, gtkEntryGetTextD(e))
                return
            }

            ZLblB.GTK_ENTRY_SET_TEXT -> {
                if (nargs != 2.toShort()) { lab = ZLblB.ERR; continue@machine }
                val e = DafCRuntime.cstringAt(v1d, 1)
                gtkEntrySetTextD(e, DafCRuntime.cstringAt(v1d, iargs[1].toInt()))
                strcpyS_B(bd, "0")
                return
            }

            ZLblB.GTK_GET_RANGE_ADJUSTMENT -> {
                if (nargs != 1.toShort()) { lab = ZLblB.ERR; continue@machine }
                val e = DafCRuntime.cstringAt(v1d, 1)
                val adj = gtkRangeGetAdjustmentD(e)
                val v = gtkAdjustmentGetValueD(adj)
                strcpyS_B(bd, formatF(v))
                return
            }

            ZLblB.GTK_FILE_CHOOSER -> {
                if (nargs != 1.toShort()) { lab = ZLblB.ERR; continue@machine }
                val e = DafCRuntime.cstringAt(v1d, 1)
                // strncpy(bd, filename, STR_MAX_DAF) count-bound copy
                val name = gtkFileChooserGetFilenameD(e) ?: ""
                var ci2 = 0
                while (ci2 < name.length && ci2 < STR_MAX_DAF - 1) {
                    bd[ci2] = name[ci2]; ci2++
                }
                bd[ci2] = NUL_D
                return
            }

            ZLblB.GTK_CALENDAR -> {
                if (nargs != 1.toShort()) { lab = ZLblB.ERR; continue@machine }
                val e = DafCRuntime.cstringAt(v1d, 1)
                val yr = IntArray(1)
                val mn = IntArray(1)
                val dy = IntArray(1)
                gtkCalendarGetDateD(e, yr, mn, dy)
                strcpyS_B(bd, (yr[0].toString() + " " + mn[0].toString()
                    + " " + dy[0].toString()))
                return
            }

            ZLblB.GTK_FONT_CHOOSER -> {
                if (nargs != 1.toShort()) { lab = ZLblB.ERR; continue@machine }
                val e = DafCRuntime.cstringAt(v1d, 1)
                strcpyS_B(bd, gtkFontChooserGetFontD(e) ?: "")
                return
            }

            ZLblB.GTK_COLOR_CHOOSER -> {
                if (nargs != 1.toShort()) { lab = ZLblB.ERR; continue@machine }
                val c = DafCRuntime.cstringAt(v1d, 1)
                val r = GdkRgbaZc()
                // intentional unused write: the vendor sets neither carrier (H-013 note)
                gtkColorChooserGetRgbaD(c, r)
                bd[0] = NUL_D
                return
            }

            ZLblB.GTK_TOGGLE_BUTTON_SET_ACTIVE -> {
                if (nargs != 2.toShort()) { lab = ZLblB.ERR; continue@machine }
                val t = DafCRuntime.cstringAt(v1d, 1)
                val ref = sliceAtZb(v1d, iargs[1].toInt())
                if (strncmpCZc(ref, "TRUE".toCharArray(), 4) == 0
                    && strlenCZc(ref) == 4) {
                    gtkToggleButtonSetActiveD(t, true)
                    strcpyS_B(bd, "1")
                    return
                }
                if (strncmpCZc(ref, "FALSE".toCharArray(), 5) == 0
                    && strlenCZc(ref) == 5) {
                    gtkToggleButtonSetActiveD(t, false)
                    strcpyS_B(bd, "1")
                    return
                }
                // vendor control flow: no match falls past the block
                // to strcpy(bd, "0") and goto err; kept verbatim
                strcpyS_B(bd, "0")
                lab = ZLblB.ERR
            }

            ZLblB.GTK_TOGGLE_BUTTON_GET_ACTIVE -> {
                if (nargs != 1.toShort()) { lab = ZLblB.ERR; continue@machine }
                val t = DafCRuntime.cstringAt(v1d, 1)
                if (gtkToggleButtonGetActiveD(t)) {
                    strcpyS_B(bd, "1")
                } else {
                    strcpyS_B(bd, "0")
                }
                return
            }

            ZLblB.GTK_SWITCH_GET_ACTIVE -> {
                if (nargs != 1.toShort()) { lab = ZLblB.ERR; continue@machine }
                val t = DafCRuntime.cstringAt(v1d, 1)
                if (gtkSwitchGetActiveD(t)) {
                    strcpyS_B(bd, "1")
                } else {
                    strcpyS_B(bd, "0")
                }
                return
            }

            ZLblB.ZDATABASE -> {
                // vendor dead strcmp("sql", "sql") constant-true
                // literal: kept as this note (H-013); the 0==1
                // dead arm drops with the same note
                if (nargs != 0.toShort()) { lab = ZLblB.ERR; continue@machine }
                strcpyS_B(bd, "2")
                return
            }

            ZLblB.ZPROGRAM -> {
                if (nargs != 0.toShort()) { lab = ZLblB.ERR; continue@machine }
                strcpyCZc(bd, svPtr.program)
                return
            }

            ZLblB.ZFILETEST -> {
                if (nargs != 2.toShort()) { lab = ZLblB.ERR; continue@machine }
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

            ZLblB.ZBASENAME -> {
                if (!(nargs == 1.toShort() || nargs == 2.toShort())) { lab = ZLblB.ERR; continue@machine }
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

            ZLblB.ZFLOOR -> {
                if (nargs != 1.toShort()) { lab = ZLblB.ERR; continue@machine }
                mpsFloorD(bd, DafCRuntime.cstringAt(v1d, 1))
                return
            }

            ZLblB.ZCEIL -> {
                if (nargs != 1.toShort()) { lab = ZLblB.ERR; continue@machine }
                mpsCeilD(bd, DafCRuntime.cstringAt(v1d, 1))
                return
            }

            ZLblB.ZTRUNC -> {
                if (nargs != 1.toShort()) { lab = ZLblB.ERR; continue@machine }
                mpsTruncD(bd, DafCRuntime.cstringAt(v1d, 1))
                return
            }

            ZLblB.ZSQLOPEN -> {
                if (svPtr.ConOpen != 0) bd[0] = '1' else bd[0] = '0'
                bd[1] = NUL_D
                return
            }

            ZLblB.ZNATIVE -> {
                // SQLITE live: znative returns "0"; the #else arm
                // drops per H-018 with this note
                strcpyS_B(bd, "0")
                return
            }

            ZLblB.ZSQLITE -> {
                // SQLITE live sub-dispatch (defines.h sql codes 25-29)
                if (nargs == 0.toShort()) {
                    strcpyS_B(bd, "1")
                    return
                }
                val XTAB = "\t"
                if (strcasecmpC(DafCRuntime.cstringAt(v1d, 1), "begin transaction")
                        && nargs == 1.toShort()) {
                    val filename = "begin transaction;"
                    svPtr.tpx = sqlDaf(BEGIN_TRANSACTION_ZB, svPtr,
                        "", filename,
                        "", XTAB)
                    strcpyS_B(bd, svPtr.tpx.toString())
                    return
                }
                if (strcasecmpC(DafCRuntime.cstringAt(v1d, 1), "commit transaction")
                        && nargs == 1.toShort()) {
                    val filename = "commit transaction;"
                    svPtr.tpx = sqlDaf(COMMIT_TRANSACTION_ZB, svPtr,
                        "", filename,
                        "", XTAB)
                    strcpyS_B(bd, svPtr.tpx.toString())
                    return
                }
                if (strcasecmpC(DafCRuntime.cstringAt(v1d, 1), "savepoint")
                        && (nargs == 1.toShort() || nargs == 2.toShort())) {
                    var filename = "savepoint "
                    if (nargs == 2.toShort()) {
                        filename = filename + DafCRuntime.cstringAt(v1d, iargs[1].toInt()) + ";"
                    } else {
                        filename += " default;"
                    }
                    svPtr.tpx = sqlDaf(SAVEPOINT_ZB, svPtr,
                        "", filename,
                        "", XTAB)
                    strcpyS_B(bd, svPtr.tpx.toString())
                    return
                }
                if (strcasecmpC(DafCRuntime.cstringAt(v1d, 1), "rollback")
                        && (nargs == 1.toShort() || nargs == 2.toShort())) {
                    var filename = "rollback to "
                    if (nargs == 2.toShort()) {
                        filename = filename + DafCRuntime.cstringAt(v1d, iargs[1].toInt()) + ";"
                    } else {
                        filename += " default;"
                    }
                    svPtr.tpx = sqlDaf(ROLLBACK_TRANSACTION_ZB, svPtr,
                        "", filename,
                        "", XTAB)
                    strcpyS_B(bd, svPtr.tpx.toString())
                    return
                }
                if (strcasecmpC(DafCRuntime.cstringAt(v1d, 1), "pragma")
                        && nargs == 2.toShort()) {
                    val filename = "pragma " + DafCRuntime.cstringAt(v1d, iargs[1].toInt()) + ";"
                    val dmy = CharArray(1024)
                    svPtr.tpx = sqlDaf(PRAGMA_ZB, svPtr,
                        "", filename, DafCRuntime.cstring(dmy), XTAB)
                    if (strlenCZc(dmy) != 0) {
                        strcpyCZc(bd, dmy)
                    } else {
                        strcpyS_B(bd, svPtr.tpx.toString())
                    }
                    return
                }
                if (strcasecmpC(DafCRuntime.cstringAt(v1d, 1), "sql")
                        && nargs == 2.toShort()) {
                    val filename = DafCRuntime.cstringAt(v1d, iargs[1].toInt()) + ";"
                    val dmy = CharArray(1024)
                    svPtr.tpx = sqlDaf(PRAGMA_ZB, svPtr,
                        "", filename, DafCRuntime.cstring(dmy), XTAB)
                    if (strlenCZc(dmy) != 0) {
                        strcpyCZc(bd, dmy)
                    } else {
                        strcpyS_B(bd, svPtr.tpx.toString())
                    }
                    return
                }
                System.err.print("sqlite arguments not recognized\n")
                lab = ZLblB.ERR
            }

            ZLblB.ZSRAND -> {
                if (nargs != 1.toShort()) { lab = ZLblB.ERR; continue@machine }
                i = atolC(sliceAtZb(v1d, 1))
                srandC(i.toInt())
                return
            }

            ZLblB.ZSMITHWATERMAN -> {
                if (nargs != 7.toShort()) { lab = ZLblB.ERR; continue@machine }
                val r = SmithWatermanW(svPtr, CharArray(1),
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

            ZLblB.ZREPLACE -> {
                if (nargs != 3.toShort()) { lab = ZLblB.ERR; continue@machine }
                val r = replaceDaf(svPtr, "",
                    DafCRuntime.cstringAt(v1d, 1),
                    DafCRuntime.cstringAt(v1d, iargs[1].toInt()),
                    DafCRuntime.cstringAt(v1d, iargs[2].toInt()))
                strcpyS_B(bd, r)
                return
            }

            ZLblB.ZNOBLANKS -> {
                if (nargs != 1.toShort()) { lab = ZLblB.ERR; continue@machine }
                var ii = 1
                var jj = 0
                while (v1d[ii] != NUL_D) {
                    if (v1d[ii] != ' ') {
                        bd[jj] = v1d[ii]
                        jj++
                    }
                    ii++
                }
                bd[jj] = NUL_D
                return
            }

            ZLblB.ZSTOPINIT -> {
                if (nargs != 1.toShort()) { lab = ZLblB.ERR; continue@machine }
                stopInitD(DafCRuntime.cstringAt(v1d, 1))
                return
            }

            ZLblB.ZSYNINIT -> {
                if (nargs != 1.toShort()) { lab = ZLblB.ERR; continue@machine }
                // SynInit side effect only; the return handle is
                // void-shaped in the vendor call
                SynInitW(svPtr, CharArray(1), sliceAtZb(v1d, 1))
                return
            }

            ZLblB.ZBMGSEARCH -> {
                if (nargs != 2.toShort()) { lab = ZLblB.ERR; continue@machine }
                val r = BMGSearchW(svPtr, CharArray(1),
                    sliceAtZb(v1d, 1),
                    sliceAtZb(v1d, iargs[1].toInt()))
                strcpyS_B(bd, r)
                return
            }

            ZLblB.ZSHRED -> {
                if (nargs != 2.toShort()) { lab = ZLblB.ERR; continue@machine }
                val r = ShredW(svPtr, CharArray(1),
                    sliceAtZb(v1d, 1),
                    sliceAtZb(v1d, iargs[1].toInt()))
                strcpyS_B(bd, r)
                return
            }

            ZLblB.ZSHREDQUERY -> {
                if (nargs != 2.toShort()) { lab = ZLblB.ERR; continue@machine }
                val r = ShredQueryW(svPtr, CharArray(1),
                    sliceAtZb(v1d, 1),
                    sliceAtZb(v1d, iargs[1].toInt()))
                strcpyS_B(bd, r)
                return
            }

            ZLblB.ZSTOPLOOKUP -> {
                if (nargs != 1.toShort()) { lab = ZLblB.ERR; continue@machine }
                if (stopLookupD(DafCRuntime.cstringAt(v1d, 1))) {
                    bd[0] = '1'
                } else {
                    bd[0] = '0'
                }
                bd[1] = NUL_D
                return
            }

            ZLblB.ZSYNLOOKUP -> {
                if (nargs != 1.toShort()) { lab = ZLblB.ERR; continue@machine }
                val r = SynLookupW(svPtr, CharArray(1), sliceAtZb(v1d, 1))
                strcpyS_B(bd, r)
                return
            }

            ZLblB.ZZAVG -> {
                if (nargs != 1.toShort()) { lab = ZLblB.ERR; continue@machine }
                val tmp1 = CharArray(STR_MAX_DAF)
                tmp1[0] = NUL_D
                tmp2[0] = NUL_D
                strcpyAt(tmp1, 0, v1d, 1)
                serviceInterfaceDaf(tmp1, tmp2, null, null, tmp3, AVG_ZB.toChar(), svPtr)
                strcpyCZc(bd, tmp3)
                return
            }

            ZLblB.ZGETENV -> {
                if (nargs != 1.toShort()) { lab = ZLblB.ERR; continue@machine }
                val tmp1 = CharArray(STR_MAX_DAF)
                tmp1[0] = NUL_D
                tmp2[0] = NUL_D
                strcpyAt(tmp1, 0, v1d, 1)
                serviceInterfaceDaf(tmp1, tmp2, null, null, tmp3, GETENV_ZB.toChar(), svPtr)
                strcpyCZc(bd, tmp3)
                return
            }

            ZLblB.ZZCOUNT -> {
                if (nargs != 1.toShort()) { lab = ZLblB.ERR; continue@machine }
                val tmp1 = CharArray(STR_MAX_DAF)
                tmp1[0] = NUL_D
                tmp2[0] = NUL_D
                strcpyAt(tmp1, 0, v1d, 1)
                serviceInterfaceDaf(tmp1, tmp2, null, null, tmp3, COUNT_ZB.toChar(), svPtr)
                strcpyCZc(bd, tmp3)
                return
            }

            ZLblB.ZZMAX -> {
                if (nargs != 1.toShort()) { lab = ZLblB.ERR; continue@machine }
                val tmp1 = CharArray(STR_MAX_DAF)
                tmp1[0] = NUL_D
                tmp2[0] = NUL_D
                strcpyAt(tmp1, 0, v1d, 1)
                serviceInterfaceDaf(tmp1, tmp2, null, null, tmp3, MAX_ZB.toChar(), svPtr)
                strcpyCZc(bd, tmp3)
                return
            }

            ZLblB.ZZMIN -> {
                if (nargs != 1.toShort()) { lab = ZLblB.ERR; continue@machine }
                val tmp1 = CharArray(STR_MAX_DAF)
                tmp1[0] = NUL_D
                tmp2[0] = NUL_D
                strcpyAt(tmp1, 0, v1d, 1)
                serviceInterfaceDaf(tmp1, tmp2, null, null, tmp3, MIN_ZB.toChar(), svPtr)
                strcpyCZc(bd, tmp3)
                return
            }

            ZLblB.ZZSUM -> {
                if (nargs != 1.toShort()) { lab = ZLblB.ERR; continue@machine }
                val tmp1 = CharArray(STR_MAX_DAF)
                tmp1[0] = NUL_D
                tmp2[0] = NUL_D
                strcpyAt(tmp1, 0, v1d, 1)
                serviceInterfaceDaf(tmp1, tmp2, null, null, tmp3, SUM_ZB.toChar(), svPtr)
                strcpyCZc(bd, tmp3)
                return
            }

            ZLblB.ZZSCANALNUM -> {
                if (nargs != 0.toShort() && nargs != 1.toShort()) { lab = ZLblB.ERR; continue@machine }
                val tmp1 = CharArray(STR_MAX_DAF)
                tmp1[0] = NUL_D
                tmp2[0] = NUL_D
                serviceInterfaceDaf(tmp1, tmp2, null, null, tmp3, SCANALNUM_ZB.toChar(), svPtr)
                strcpyCZc(bd, tmp3)
                if (nargs == 1.toShort()) {
                    i = atolC(sliceAtZb(v1d, iargs[1].toInt()))
                    // bd[i]=0 index quantized into bounds (H-018 note)
                    val ib = i.toInt()
                    if (ib in 0 until bd.size) bd[ib] = NUL_D
                }
                return
            }

            ZLblB.ZZSCAN -> {
                if (nargs != 0.toShort() && nargs != 1.toShort()) { lab = ZLblB.ERR; continue@machine }
                val tmp1 = CharArray(STR_MAX_DAF)
                tmp1[0] = NUL_D
                tmp2[0] = NUL_D
                serviceInterfaceDaf(tmp1, tmp2, null, null, tmp3, SCAN_ZB.toChar(), svPtr)
                strcpyCZc(bd, tmp3)
                if (nargs == 1.toShort()) {
                    i = atolC(sliceAtZb(v1d, iargs[1].toInt()))
                    val ib = i.toInt()
                    if (ib in 0 until bd.size) bd[ib] = NUL_D
                }
                return
            }

            ZLblB.ZZINPUT -> {
                if (nargs != 1.toShort()) { lab = ZLblB.ERR; continue@machine }
                val tmp1 = CharArray(STR_MAX_DAF)
                tmp1[0] = NUL_D
                tmp2[0] = NUL_D
                strcpyAt(tmp1, 0, v1d, 1)
                serviceInterfaceDaf(tmp1, tmp2, null, null, tmp3, INPUT_ZB.toChar(), svPtr)
                strcpyCZc(bd, tmp3)
                return
            }

            ZLblB.ZZCOSINE -> {
                if (nargs != 2.toShort()) { lab = ZLblB.ERR; continue@machine }
                val tmp1 = CharArray(STR_MAX_DAF)
                tmp1[0] = NUL_D
                tmp2[0] = NUL_D
                strcpyAt(tmp1, 0, v1d, 1)
                strcpyAt(tmp2, 0, v1d, iargs[1].toInt())
                serviceInterfaceDaf(tmp1, tmp2, null, null, tmp3, COSINE_ZB.toChar(), svPtr)
                strcpyCZc(bd, tmp3)
                return
            }

            ZLblB.ZZJACCARD -> {
                if (nargs != 2.toShort()) { lab = ZLblB.ERR; continue@machine }
                val tmp1 = CharArray(STR_MAX_DAF)
                tmp1[0] = NUL_D
                tmp2[0] = NUL_D
                strcpyAt(tmp1, 0, v1d, 1)
                strcpyAt(tmp2, 0, v1d, iargs[1].toInt())
                serviceInterfaceDaf(tmp1, tmp2, null, null, tmp3, JACCARD_ZB.toChar(), svPtr)
                strcpyCZc(bd, tmp3)
                return
            }

            ZLblB.ZZSIM1 -> {
                if (nargs != 2.toShort()) { lab = ZLblB.ERR; continue@machine }
                val tmp1 = CharArray(STR_MAX_DAF)
                tmp1[0] = NUL_D
                tmp2[0] = NUL_D
                strcpyAt(tmp1, 0, v1d, 1)
                strcpyAt(tmp2, 0, v1d, iargs[1].toInt())
                serviceInterfaceDaf(tmp1, tmp2, null, null, tmp3, SIM1_ZB.toChar(), svPtr)
                strcpyCZc(bd, tmp3)
                return
            }

            ZLblB.ZZTRANSPOSE -> {
                if (nargs != 2.toShort()) { lab = ZLblB.ERR; continue@machine }
                val tmp1 = CharArray(STR_MAX_DAF)
                tmp1[0] = NUL_D
                tmp2[0] = NUL_D
                strcpyAt(tmp1, 0, v1d, 1)
                strcpyAt(tmp2, 0, v1d, iargs[1].toInt())
                serviceInterfaceDaf(tmp1, tmp2, null, null, tmp3, TRANSPOSE_ZB.toChar(), svPtr)
                strcpyCZc(bd, tmp3)
                return
            }

            ZLblB.ZZSOUNDEX -> {
                if (nargs != 1.toShort()) { lab = ZLblB.ERR; continue@machine }
                bd[0] = v1d[1]
                var jj = 1
                var ii = 2
                while (v1d[ii] != NUL_D) {
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
                bd[jj] = NUL_D
                ii = 1
                while (bd[ii] != NUL_D) {
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
                bd[4] = NUL_D
                return
            }

            ZLblB.ZZMULTIPLY -> {
                if (nargs != 3.toShort()) { lab = ZLblB.ERR; continue@machine }
                val tmp1 = CharArray(STR_MAX_DAF)
                tmp3[0] = NUL_D
                tmp1[0] = NUL_D
                tmp2[0] = NUL_D
                strcpyAt(tmp1, 0, v1d, 1)
                strcpyAt(tmp2, 0, v1d, iargs[1].toInt())
                strcpyAt(tmp3, 0, v1d, iargs[2].toInt())
                serviceInterfaceDaf(tmp1, tmp2, tmp3, null, tmp4, MMULTIPLY_ZB.toChar(), svPtr)
                strcpyCZc(bd, tmp4)
                return
            }

            ZLblB.ZZCENTROID -> {
                if (nargs != 2.toShort()) { lab = ZLblB.ERR; continue@machine }
                val tmp1 = CharArray(STR_MAX_DAF)
                tmp1[0] = NUL_D
                tmp2[0] = NUL_D
                strcpyAt(tmp1, 0, v1d, 1)
                strcpyAt(tmp2, 0, v1d, iargs[1].toInt())
                serviceInterfaceDaf(tmp1, tmp2, null, null, tmp3, CENTROID_ZB.toChar(), svPtr)
                strcpyCZc(bd, tmp3)
                return
            }

            ZLblB.ZZTERMCORRELATE -> {
                if (nargs != 2.toShort()) { lab = ZLblB.ERR; continue@machine }
                val tmp1 = CharArray(STR_MAX_DAF)
                tmp1[0] = NUL_D
                tmp2[0] = NUL_D
                strcpyAt(tmp1, 0, v1d, 1)
                strcpyAt(tmp2, 0, v1d, iargs[1].toInt())
                serviceInterfaceDaf(tmp1, tmp2, null, null, tmp3, TERMCORRELATE_ZB.toChar(), svPtr)
                strcpyCZc(bd, tmp3)
                return
            }

            ZLblB.ZZDOCCORRELATE -> {
                if (nargs != 4.toShort()) { lab = ZLblB.ERR; continue@machine }
                val tmp1 = CharArray(STR_MAX_DAF)
                val tmp2a = CharArray(STR_MAX_DAF)
                val tmp2b = CharArray(STR_MAX_DAF)
                tmp1[0] = NUL_D
                tmp2[0] = NUL_D
                strcpyAt(tmp1, 0, v1d, 1)
                strcpyAt(tmp2, 0, v1d, iargs[1].toInt())
                strcpyAt(tmp2a, 0, v1d, iargs[2].toInt())
                strcpyAt(tmp2b, 0, v1d, iargs[3].toInt())
                serviceInterfaceDaf(tmp1, tmp2, tmp2a, tmp2b, tmp3, DOCCORRELATE_ZB.toChar(), svPtr)
                strcpyCZc(bd, tmp3)
                return
            }

            ZLblB.ZZIDF -> {
                if (nargs != 2.toShort()) { lab = ZLblB.ERR; continue@machine }
                val tmp1 = CharArray(STR_MAX_DAF)
                tmp1[0] = NUL_D
                tmp2[0] = NUL_D
                strcpyAt(tmp1, 0, v1d, 1)
                strcpyAt(tmp2, 0, v1d, iargs[1].toInt())
                serviceInterfaceDaf(tmp1, tmp2, null, null, tmp3, INVDF_ZB.toChar(), svPtr)
                strcpyCZc(bd, tmp3)
                return
            }

            ZLblB.ZZDICE -> {
                if (nargs != 2.toShort()) { lab = ZLblB.ERR; continue@machine }
                val tmp1 = CharArray(STR_MAX_DAF)
                tmp1[0] = NUL_D
                tmp2[0] = NUL_D
                strcpyAt(tmp1, 0, v1d, 1)
                strcpyAt(tmp2, 0, v1d, iargs[1].toInt())
                serviceInterfaceDaf(tmp1, tmp2, null, null, tmp3, DICE_ZB.toChar(), svPtr)
                strcpyCZc(bd, tmp3)
                return
            }

            ZLblB.ZFLUSH -> {
                MglobalDaf(FLUSH_D, tmp2, tmp3, svPtr)
                strcpyS_B(bd, "")
                return
            }

            ZLblB.ZCHDIR -> {
                if (nargs != 1.toShort()) { lab = ZLblB.ERR; continue@machine }
                i = chdirC(DafCRuntime.cstringAt(v1d, 1)).toLong()
                if (i == 0L) {
                    strcpyS_B(bd, "0")
                } else {
                    strcpyS_B(bd, "1")
                }
                return
            }

            ZLblB.ZPOW -> {
                if (nargs != 2.toShort()) { lab = ZLblB.ERR; continue@machine }
                strcpyS_B(bd, expxD(toZb(DafCRuntime.cstringAt(v1d, 1)),
                    toZb(DafCRuntime.cstringAt(v1d, iargs[1].toInt()))))
                return
            }

            ZLblB.ZLOG2 -> {
                if (nargs != 1.toShort()) { lab = ZLblB.ERR; continue@machine }
                strcpyS_B(bd, mpsLog2D(toZb(DafCRuntime.cstringAt(v1d, 1))))
                return
            }

            ZLblB.ZLOG10 -> {
                if (nargs != 1.toShort()) { lab = ZLblB.ERR; continue@machine }
                strcpyS_B(bd, mpsLog10D(toZb(DafCRuntime.cstringAt(v1d, 1))))
                return
            }

            ZLblB.ZLOG -> {
                if (nargs != 1.toShort()) { lab = ZLblB.ERR; continue@machine }
                mpsLogD(bd, DafCRuntime.cstringAt(v1d, 1))
                return
            }

            ZLblB.ZCOSINE -> {
                if (nargs != 1.toShort()) { lab = ZLblB.ERR; continue@machine }
                mpsCosD(bd, DafCRuntime.cstringAt(v1d, 1))
                return
            }

            ZLblB.ZTANGENT -> {
                if (nargs != 1.toShort()) { lab = ZLblB.ERR; continue@machine }
                mpsTanD(bd, DafCRuntime.cstringAt(v1d, 1))
                return
            }

            ZLblB.ZATANGENT -> {
                if (nargs != 1.toShort()) { lab = ZLblB.ERR; continue@machine }
                mpsAtanD(bd, sliceAtZb(v1d, 1))
                return
            }

            ZLblB.ZACOSINE -> {
                if (nargs != 1.toShort()) { lab = ZLblB.ERR; continue@machine }
                strcpyS_B(bd, mpsAcosD(toZb(DafCRuntime.cstringAt(v1d, 1))))
                return
            }

            ZLblB.ZSINE -> {
                if (nargs != 1.toShort()) { lab = ZLblB.ERR; continue@machine }
                strcpyS_B(bd, mpsSinD(toZb(DafCRuntime.cstringAt(v1d, 1))))
                return
            }

            ZLblB.ZASINE -> {
                if (nargs != 1.toShort()) { lab = ZLblB.ERR; continue@machine }
                strcpyS_B(bd, mpsAsin(sliceAtZb(v1d, 1)))
                return
            }

            ZLblB.ZEXP -> {
                if (nargs != 1.toShort()) { lab = ZLblB.ERR; continue@machine }
                mpsExpD(bd, DafCRuntime.cstringAt(v1d, 1))
                return
            }

            ZLblB.ZEXP2 -> {
                if (nargs != 1.toShort()) { lab = ZLblB.ERR; continue@machine }
                mpsExp2D(bd, DafCRuntime.cstringAt(v1d, 1))
                return
            }

            ZLblB.ZEXP10 -> {
                if (nargs != 1.toShort()) { lab = ZLblB.ERR; continue@machine }
                strcpyS_B(bd, mpsExp10D(toZb(DafCRuntime.cstringAt(v1d, 1))))
                return
            }

            ZLblB.ZHIT -> {
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

            ZLblB.ZLOWER -> {
                if (nargs != 1.toShort()) { lab = ZLblB.ERR; continue@machine }
                // char bdx tracks the last copied char; the pointer
                // walk renders as the index walk
                var src = 1
                var dst = 0
                while (v1d[src] != NUL_D) {
                    bd[dst] = tolowerC(v1d[src])
                    dst++
                    src++
                }
                bd[dst] = NUL_D
                return
            }

            // znormal: / zalter: share this arm (vendor adjacent
            // labels, H-011 note in the file header)
            ZLblB.ZNORMAL -> {
                if (nargs != 1.toShort() && nargs != 2.toShort()) { lab = ZLblB.ERR; continue@machine }
                var jj = 0
                bd[0] = NUL_D
                var ii = 1
                while (v1d[ii] != NUL_D) {
                    if (isalnumC(v1d[ii])) {
                        bd[jj] = tolowerC(v1d[ii])
                        jj++
                    }
                    ii++
                }
                bd[jj] = NUL_D
                if (nargs == 2.toShort()) {
                    wordTrunc = atoiC(sliceAtZb(v1d, iargs[1].toInt()))
                } else {
                    wordTrunc = 25
                }
                // bd[WordTrunc]=0 index quantized into bounds
                // (H-018 note)
                if (wordTrunc in 0 until bd.size) bd[wordTrunc] = NUL_D
                return
            }

            ZLblB.ZSTEM -> {
                if (nargs != 1.toShort()) { lab = ZLblB.ERR; continue@machine }
                // j = 0 drop per H-013 (overwritten before read)
                bd[0] = NUL_D
                var ii = 1
                while (v1d[ii] != NUL_D) {
                    bd[ii - 1] = tolowerC(v1d[ii])
                    ii++
                }
                bd[ii - 1] = NUL_D
                // remove English language suffixes (wstem seat)
                wstemD(bd, strlenCZc(bd))
                return
            }

            ZLblB.ZSEEK -> {
                if (nargs != 1.toShort()) { lab = ZLblB.ERR; continue@machine }
                if (svPtr.io < 0 || svPtr.io > 5
                        || svPtr.in_file[svPtr.io] == 0) {
                    bd[0] = NUL_D
                    return
                }
                // fsetpos seat: the DAF DafMsv carries in_file as
                // the Int handle domain (no JVM fpos_t); the seat
                // raises per the LibmpscppMisc5 ZSeek precedent;
                // the fposZb store keeps the vendor-side state
                ZfcnBifsState.fposZb = atolC(sliceAtZb(v1d, 1))
                throw NotImplementedError(
                    "zseek: out-of-scope fsetpos seat (FILE* position API; JVM divergence)")
            }

            ZLblB.ZTELL -> {
                if (svPtr.io < 0 || svPtr.io > 5) {
                    strcpyS_B(bd, "-1")
                    return
                }
                // fgetpos seat: the DAF DafMsv carries in_file as
                // the Int handle domain (no JVM fpos_t); the seat
                // raises per the LibmpscppMisc5 Ztell precedent
                throw NotImplementedError(
                    "ztell: out-of-scope fgetpos seat (FILE* position API; JVM divergence)")
            }

            ZLblB.ZWORD -> {
                // the zword parse-buffer helper (zwi/zwn/zwp/zws)
                when {
                    zcodeChar1(v1d) == 's' -> {
                        strcpyAt(ZfcnBifsState.linebuf, 0, v1d, 1)
                        ZfcnBifsState.linebufx = 0
                        bd[0] = NUL_D
                        return
                    }
                    zcodeChar1(v1d) == 'i' -> {
                        strcpyAt(ZfcnBifsState.linebuf, 0, v1d, 1)
                        ZfcnBifsState.linebufx = 0
                        var pi = 0
                        while (ZfcnBifsState.linebuf[pi] != NUL_D) {
                            ZfcnBifsState.linebuf[pi] =
                                tolowerC(ZfcnBifsState.linebuf[pi])
                            pi++
                        }
                        bd[0] = NUL_D
                        return
                    }
                    tolowerC(zcodeChar1(v1d)) == 'n' -> {
                        val lbuf = ZfcnBifsState.linebuf
                        if (lbuf[ZfcnBifsState.linebufx] == NUL_D) {
                            bd[0] = NUL_D
                            return
                        }
                        while (lbuf[ZfcnBifsState.linebufx] != NUL_D
                                && lbuf[ZfcnBifsState.linebufx] == ' ') {
                            ZfcnBifsState.linebufx++
                        }
                        if (lbuf[ZfcnBifsState.linebufx] == NUL_D) {
                            bd[0] = NUL_D
                            return
                        }
                        i = 0L
                        while (lbuf[ZfcnBifsState.linebufx] == '-'
                                || lbuf[ZfcnBifsState.linebufx] == '_') {
                            ZfcnBifsState.linebufx++
                        }
                        while (true) {
                            bd[i.toInt()] = lbuf[ZfcnBifsState.linebufx]
                            if (lbuf[ZfcnBifsState.linebufx] == NUL_D
                                    || lbuf[ZfcnBifsState.linebufx] == ' ') {
                                bd[i.toInt()] = NUL_D
                                return
                            }
                            i++
                            ZfcnBifsState.linebufx++
                        }
                    }
                    zcodeChar1(v1d) == 'p' -> {
                        val lbuf = ZfcnBifsState.linebuf
                        if (lbuf[ZfcnBifsState.linebufx] == NUL_D) {
                            bd[0] = NUL_D
                            return
                        }
                        while (lbuf[ZfcnBifsState.linebufx] != NUL_D
                                && lbuf[ZfcnBifsState.linebufx] == ' ') {
                            ZfcnBifsState.linebufx++
                        }
                        if (lbuf[ZfcnBifsState.linebufx] == NUL_D) {
                            bd[0] = NUL_D
                            return
                        }
                        val c0 = lbuf[ZfcnBifsState.linebufx]
                        if (c0 != '*' && c0 != '-'
                                && ispunctC(c0)) {
                            bd[0] = c0
                            bd[1] = NUL_D
                            ZfcnBifsState.linebufx++
                            return
                        }
                        i = 0L
                        while (true) {
                            bd[i.toInt()] = lbuf[ZfcnBifsState.linebufx]
                            val cc = lbuf[ZfcnBifsState.linebufx]
                            if (cc == NUL_D || cc == ' '
                                    || (cc != '*' && cc != '-' && ispunctC(cc))) {
                                bd[i.toInt()] = NUL_D
                                return
                            }
                            i++
                            ZfcnBifsState.linebufx++
                        }
                    }
                    else -> { }
                }
                // vendor #endif falls to goto err
                lab = ZLblB.ERR
            }

            ZLblB.ZNUMERIC -> {
                if (nargs != 1.toShort()) { lab = ZLblB.ERR; continue@machine }
                // k = 0 drop per H-013 (never read)
                var ii = 1
                while (v1d[ii] != NUL_D) {
                    val c = v1d[ii]
                    if (isdigitC(c) || c == '.' || c == '+' || c == '-') {
                        ii++
                        continue
                    }
                    bd[0] = '0'
                    bd[1] = NUL_D
                    return
                }
                bd[0] = '1'
                bd[1] = NUL_D
                return
            }

            ZLblB.ZBLNK -> {
                if (nargs != 1.toShort()) { lab = ZLblB.ERR; continue@machine }
                // the zbl1 intra-arm goto renders inline (H-011
                // file header note)
                var src = 1
                var dst = 0
                var bdx: Char = NUL_D
                var zbl1 = false
                while (v1d[src] != NUL_D) {
                    if (v1d[src] != ' ') {
                        zbl1 = true
                        break
                    }
                    src++
                }
                if (!zbl1) {
                    bd[dst] = NUL_D
                    return
                }
                while (v1d[src] != NUL_D) {
                    if (bdx == ' ' && v1d[src] == ' ') {
                        src++
                        continue
                    }
                    bdx = v1d[src]
                    bd[dst] = v1d[src]
                    dst++
                    src++
                }
                bd[dst] = NUL_D
                return
            }

            ZLblB.ZCURRENTFILE -> {
                strcpyS_B(bd, DafCRuntime.cstring(svPtr.CurrentFile))
                return
            }

            ZLblB.ZHTML -> {
                if (nargs != 1.toShort()) { lab = ZLblB.ERR; continue@machine }
                // k = 1 drop per H-013 (never read)
                var ii = 1
                while (v1d[ii] != NUL_D) {
                    if (v1d[ii] == ' ') {
                        v1d[ii] = '+'
                    } else if (!isalnumC(v1d[ii])) {
                        // sprintf(t1, "%%%2X") of the char
                        // sprintf(t1, "%%%2X") of the char (the
                        // trailing format NUL_D rides the note)
                        val t1 = "%" + java.lang.String.format(
                            java.util.Locale.US, "%2X", v1d[ii].code)
                        v1d[ii] = NUL_D
                        val t2 = (DafCRuntime.cstringAt(v1d, 1) + t1
                            + DafCRuntime.cstringAt(v1d, ii + 1))
                        // strmove(&v1d[1], t2) into the src buffer
                        val tb = CharArray(STR_MAX_DAF)
                        copyAtSZc(tb, 0, t2)
                        strcpyAt(v1d, 1, tb, 0)
                        ii = ii + 2
                    }
                    ii++
                }
                val t3 = CharArray(STR_MAX_DAF)
                strcpyAt(t3, 0, v1d, 1)
                strcpyCZc(bd, t3)
                return
            }

            ZLblB.ZCONDENSE -> {
                // SQLITE live: return; the dump body drops per the
                // H-018 file header note
                return
            }

            ZLblB.ZRESTORE -> {
                // SQLITE live: return; the load body drops per the
                // H-018 file header note
                return
            }

            ZLblB.ZNXTROW -> {
                // SQLITE-live arm: banner then return; the walking
                // body behind it drops per the H-018 file header note
                System.out.print("\u0024zm not available\n")
                return
            }

            ZLblB.ZABS -> {
                if (nargs != 1.toShort()) { lab = ZLblB.ERR; continue@machine }
                // the mpsAbs int return is the always-0 success code of
                // the intrinsic family (MpsAbs row 43 note); the
                // !=0 ERR arm is Unreachable and folds
                strcpyS_B(bd, mpsAbsD(toZb(DafCRuntime.cstringAt(v1d, 1))))
                return
            }

            ZLblB.ZPAD -> {
                if (nargs != 2.toShort()) { lab = ZLblB.ERR; continue@machine }
                i = atolC(sliceAtZb(v1d, iargs[1].toInt()))
                strcpyAt(bd, 0, v1d, 1)
                // for (j = strlen(&v1d[1]); j < i; j++) bd[j] = ' '
                // (the pad index bound is the C buffer; quantized
                // in-bounds, H-018 note)
                var jj = strlenCZc(sliceAtZb(v1d, 1))
                val il = i.toInt()
                while (jj < il) {
                    if (jj >= bd.size) break
                    bd[jj] = ' '
                    jj++
                }
                bd[jj] = NUL_D
                return
            }

            ZLblB.Z_VARIABLE -> {
                if (nargs != 1.toShort()) { lab = ZLblB.ERR; continue@machine }
                if (symRetDaf(1, sliceAtZb(v1d, 1), 0, bd, 0, svPtr) == null) {
                    bd[0] = NUL_D
                }
                return
            }

            ZLblB.ZDATE -> {
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
                    val cbuf = CharArray(STR_MAX_DAF)
                    copyAtSZc(cbuf, 0, ctime(timex))
                    strcpyCZc(bd, cbuf)
                    bd[24] = NUL_D
                } else if (zcode1c == '3') {
                    if (nargs != 3.toShort()) { lab = ZLblB.ERR; continue@machine }
                    val year = atolC(sliceAtZb(v1d, 1)).toInt()
                    val m = atolC(sliceAtZb(v1d, iargs[1].toInt())).toInt()
                    val d = atolC(sliceAtZb(v1d, iargs[2].toInt())).toInt()
                    if (year < 1900 || m < 1 || m > 12 || d < 1 || d > 31) {
                        lab = ZLblB.ERR; continue@machine
                    }
                    val date = TmZb()
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
                    if (nargs != 2.toShort()) { lab = ZLblB.ERR; continue@machine }
                    var year = atolC(sliceAtZb(v1d, 1)).toInt()
                    if (year > 1900) year = year - 1900
                    val d = atolC(sliceAtZb(v1d, iargs[1].toInt())).toInt()
                    if (year < 0 || d > 366 || d < 1) { lab = ZLblB.ERR; continue@machine }
                    var dd = d - 1
                    var m = 0
                    var ddm = 1
                    val date = TmZb()
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
                        else if (m == 11 && ddm > 31) { lab = ZLblB.ERR; continue@machine }
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
                    if (nargs != 3.toShort()) { lab = ZLblB.ERR; continue@machine }
                    val year = atolC(sliceAtZb(v1d, 1)).toInt()
                    val m = atolC(sliceAtZb(v1d, iargs[1].toInt())).toInt()
                    val d = atolC(sliceAtZb(v1d, iargs[2].toInt())).toInt()
                    if (year < 1900 || m < 1 || m > 12 || d < 1 || d > 31) {
                        lab = ZLblB.ERR; continue@machine
                    }
                    val date = TmZb()
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
                    copyAtSZc(bd, 0, ct.substring(11, ct.length))
                    bd[5] = NUL_D
                } else if (zcode1c == '7' || zcode1c == '8') {
                    if (nargs == 1.toShort()) {
                        timex = atolC(sliceAtZb(v1d, 1))
                    }
                    copyAtSZc(tmp2, 0, ctime(timex))
                    bd[1] = tmp2[20]
                    bd[2] = tmp2[21]
                    bd[3] = tmp2[22]
                    bd[4] = tmp2[23]
                    bd[5] = '-'
                    tmp2[7] = NUL_D
                    val mon = when (DafCRuntime.cstringAt(tmp2, 4)) {
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
                    bd[11] = NUL_D
                    for (jj in 0 until 11) {
                        if (bd[jj] == ' ') bd[jj] = '0'
                    }
                    if (zcode1c == '8') {
                        bd[11] = ','
                        copyAtSZc(bd, 12, ctime(timex).substring(11))
                        bd[17] = NUL_D
                    }
                    strmoveAt(bd, 0, 1)
                } else {
                    if (nargs != 0.toShort()) { lab = ZLblB.ERR; continue@machine }
                    val cbuf = CharArray(STR_MAX_DAF)
                    copyAtSZc(cbuf, 0, ctime(timex))
                    strcpyCZc(bd, cbuf)
                    bd[24] = NUL_D
                }
                return
            }

            ZLblB.ZFILE -> {
                // long fd = open(path, 0); x = fd; close(fd) ->
                // the openC exists() quantization (H-018 note)
                val x = openC(DafCRuntime.cstringAt(v1d, 1))
                if (x == -1) {
                    bd[0] = '0'
                    bd[1] = NUL_D
                    return
                }
                bd[0] = '1'
                bd[1] = NUL_D
                return
            }

            ZLblB.ZGLOBAL -> {
                val g = 5 // count empties
                MglobalDaf(g, v1d, bd, svPtr)
                return
            }

            ZLblB.ZROOT -> {
                if (nargs != 1.toShort()) { lab = ZLblB.ERR; continue@machine }
                strcpyS_B(bd, mpsSqrtD(sliceAtZb(v1d, 1)))
                return
            }

            ZLblB.ZSYSTEM -> {
                if (nargs != 1.toShort()) { lab = ZLblB.ERR; continue@machine }
                val x = systemC(DafCRuntime.cstringAt(v1d, 1))
                strcpyS_B(bd, x.toString())
                return
            }

            ZLblB.FCN_NOT_FOUND -> {
                if (svPtr.NOERR != 0) {
                    svPtr.NOERR = -1
                    return
                }
                System.out.print("\n*** Z function: \"" + DafCRuntime.cstring(fcnName)
                    + "\" not found in or near line "
                    + svPtr.LineNumber.toString() + "\n")
                sigintDaf(100)
                // vendor falls through into err; same observable here
                if (svPtr.NOERR != 0) {
                    svPtr.NOERR = -1
                    return
                }
                System.err.print("\n*** zFunction error in or near line "
                    + svPtr.LineNumber.toString() + "\n")
                sigintDaf(100)
            }

            ZLblB.ERR -> {
                if (svPtr.NOERR != 0) {
                    svPtr.NOERR = -1
                    return
                }
                System.err.print("\n*** zFunction error in or near line "
                    + svPtr.LineNumber.toString() + "\n")
                sigintDaf(100)
                // end of vendor body
            }
        }
    }
}

// char *Replace(struct MSV *, const char *, const char *, const
// char *, const char *) declared builtin.h:40, API forward of the
// zReplace entry; NOT a corpus unit (builtin API seat owner, the
// Replace machinery of libmpscpp; raised per the zreplace-arm
// ledger - the BIFS-ONLY seat, grep-verified absent from the live
// unit-184 TU).
fun replaceDaf(sv: DafMsv, ep: String, arg1: String, arg2: String,
        arg3: String): String =
    throw NotImplementedError(
        "Replace: out-of-scope owner (builtin.h:40 API entry, not a corpus unit)")

// private CstringOps-family helper copies of this file (H-017 rule:
// the twin file's *Zc privates are file-private, this FILE keeps
// its own copies; the atoiC precedent for same-name private funs)

private fun strlenCZc(buf: CharArray): Int {
    var i = 0
    while (i < buf.size && buf[i] != NUL_D) i++
    return i
}

private fun strcpyCZc(dst: CharArray, src: CharArray) {
    var i = 0
    while (i < src.size && src[i] != NUL_D) {
        dst[i] = src[i]
        i++
    }
    dst[i] = NUL_D
}

private fun strcpySZc(dst: CharArray, src: String) {
    var i = 0
    while (i < src.length) {
        dst[i] = src[i]
        i++
    }
    dst[i] = NUL_D
}

private fun strncmpCZc(a: CharArray, b: CharArray, n: Int): Int {
    var i = 0
    while (i < n) {
        val ca = a[i]
        val cb = b[i]
        if (ca == NUL_D && cb == NUL_D) return 0
        if (ca < cb) return -1
        if (ca > cb) return 1
        i++
    }
    return 0
}

private fun copyAtSZc(dst: CharArray, offset: Int, src: String) {
    var i = 0
    while (i < src.length) {
        dst[offset + i] = src[i]
        i++
    }
    dst[offset + i] = NUL_D
}

// ctime helper copy of this file (the Zfcns.kt ctime twin; the
// zdate zd1/zd2 arms read it)
private fun ctime(t: Long): String {
    val cal = java.util.Calendar.getInstance(java.util.TimeZone.getTimeZone("UTC"))
    val mons = arrayOf("Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec")
    val days = arrayOf("Sun","Mon","Tue","Wed","Thu","Fri","Sat")
    cal.timeInMillis = t
    val f = java.text.SimpleDateFormat("HH:mm:ss", java.util.Locale.US)
    f.timeZone = java.util.TimeZone.getTimeZone("UTC")
    val w = String.format(
        java.util.Locale.US, "%s %s %3d %s %d",
        days[cal.get(java.util.Calendar.DAY_OF_WEEK) - 1],
        mons[cal.get(java.util.Calendar.MONTH)],
        cal.get(java.util.Calendar.DAY_OF_MONTH),
        f.format(java.util.Date(t)),
        cal.get(java.util.Calendar.YEAR)
    )
    return w + "\n"
}
