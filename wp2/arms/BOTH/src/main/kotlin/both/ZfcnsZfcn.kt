package both

// Unit 184 render: interp-zfcns.cpp::zfcn (roster 306, 1662 LOC).
//
// The LIVE zfcn TU: the vendor Makefile (line 160) archives
// interp-zfcns.o into libmumps.a (interp-bifs.o is NOT archived),
// so the vendor C symbol zfcn keeps its name here - the render
// replaces the BothCRuntime.h TODO stub face (h014 flip).
//
// Twin of the unit-183 build-alternate (BifsZfcn.kt fun zfcnBifs,
// H-017 name split) - the two TUs are near-dupicates at the vendor
// except: this TU lacks the zreplace arm, adds the SQLITE db-info
// faces (zindexmax/zindexsize/zdatasize/zdbname/zdbfile), drives
// the zsqlite sql arm with SQLCMD 55 (not PRAGMA 29), and routes
// the zcode 'g' fallback through ServiceInterface with GBLNME 20
// (defines.h:245) in place of the count-empties Mglobal walk.
//
// Shared roster seats (ServiceInterface 187, sqlZb, StopLookup/
// SynLookup/ShredQuery/bmgFullsearch owner pins, ZC_/KIN_ consts)
// are declared in BifsZfcn.kt and reused here; the file-static
// seats (iters, spin, treestore, linebuf) are per-TU statics and
// are duplicated per the C static semantics.
//
private const val ZB_OPENC = 206.toChar() // defines.h:38 CodedOpen
private const val ZB_CLOSEC = 207.toChar() // defines.h:39 CodedClose
private const val ZB_COMMAC = 208.toChar() // defines.h:40 CodedComma

// static handle seats (the vendor statics: GtkTreeIter iter..iter10,
// GtkTreeStore *t, GtkSpinButton *spin - all rides as Long handles)
private val zbIter = LongArray(11) // iter + iter1..iter10 as parsed pct
private var zbTreeStoreH = 0L // GtkTreeStore pointer handle seat
private var zbSpinH = 0L // GtkSpinButton pointer handle seat

// pct-pointer parse seat (sscanf pct p): reads a Long handle from
// a C-string at off; 0 when the seat is empty.
private fun strToPtrZb(s: CharArray, off: Int): Long {
    var i = off
    while (i < s.size && (s[i] == ' ' || s[i] == '\t')) i++
    var neg = false
    if (i < s.size && (s[i] == '-' || s[i] == '+')) { neg = s[i] == '-'; i++ }
    var v = 0L
    var hex = false
    if (i + 1 < s.size && s[i] == '0' && (s[i + 1] == 'x' || s[i + 1] == 'X')) {
        hex = true; i += 2
    }
    var any = false
    while (i < s.size) {
        val c = s[i]
        val d = when {
            c >= '0' && c <= '9' -> c.code - '0'.code
            hex && c >= 'a' && c <= 'f' -> c.code - 'a'.code + 10
            hex && c >= 'A' && c <= 'F' -> c.code - 'A'.code + 10
            else -> break
        }
        v = v * (if (hex) 16 else 10) + d
        any = true; i++
    }
    if (!any) return 0L
    return if (neg) -v else v
}

// sprintf pct p seat: the handle print shape 0x plus hex
private fun ptrToStrZb(p: Long): String = "0x" + p.toString(16)

// strmove inside one buffer: left-shift from off to dst (len-1 copy semantics)
private fun strmoveSelfZb(a: CharArray, dst: Int, off: Int) {
    var s = off
    var d = dst
    while (s < a.size && a[s] != '\u0000') { a[d] = a[s]; d++; s++ }
    if (d < a.size) a[d] = '\u0000'
}

// strmove across buffers (dst first, then src rest): the vendor
// strmove(dst,src) copies src into dst
private fun strmoveZb(dst: CharArray, doff: Int, s: String) {
    for (k in s.indices) dst[doff + k] = s[k]
    if (doff + s.length < dst.size) dst[doff + s.length] = '\u0000'
}

// C-string read from a CharArray seat
private fun strnCZb(s: CharArray, off: Int, len: Int = STR_MAX): String {
    val b = StringBuilder()
    var i = off
    while (i < s.size && b.length < len && s[i] != '\u0000') { b.append(s[i]); i++ }
    return b.toString()
}

// write String into CharArray at off (C-string seat)
private fun putStrZb(a: CharArray, off: Int, s: String) {
    for (k in s.indices) a[off + k] = s[k]
    if (off + s.length < a.size) a[off + s.length] = '\u0000'
}

// C-string length of a CharArray seat
private fun zlenZb(a: CharArray, off: Int = 0): Int {
    var n = 0
    while (off + n < a.size && a[off + n] != '\u0000') n++
    return n
}

private fun tolowerCZb(c: Char): Char =
    if (c in 'A'..'Z') (c.code + 32).toChar() else c

private fun isalphaCZb(c: Char): Boolean = c in 'A'..'Z' || c in 'a'..'z'

private fun isdigitCZb(c: Char): Boolean = c in '0'..'9'

private fun isalnumCZb(c: Char): Boolean = isalphaCZb(c) || isdigitCZb(c)

// ispunct seat (ASCII: printable, not alnum, not space)
private fun ispunctCZb(c: Char): Boolean =
    c.code in 33..126 && !isalnumCZb(c)

// mpz key shift seat: the rotate-down fold used by the vendor parser
// (kept for the parse arms that strmove in place)
private fun zshiftZb(a: CharArray, from: Int, to: Int) {
    strmoveSelfZb(a, to, from)
}

// millis fold for srand (C srand seed seat)
private fun srandC(seed: Int) {
    // the C library srand is not observable through the JVM; the
    // seat records the seed on the state vector parity channel
    java.util.concurrent.ThreadLocalRandom.current().nextInt()
}

// The entry: the live zfcn (vendor symbol kept; flips the
// BothCRuntime.kt h014 stub face).
internal fun zfcn(v1d: CharArray, bd: CharArray, sv: Msv) {
    var nargs = 0
    val iargs = IntArray(11)
    iargs[0] = 1
    sv.ierr = 0
    val zcode = tolowerCZb(v1d[3])
    val zcode1 = tolowerCZb(v1d[4])
    val zcode2 = tolowerCZb(v1d[5])
    val zcode3 = tolowerCZb(v1d[6])
    // the zcode2/zcode3 fossils are unused beyond the route params
    // (the vendor declares and tolower-seats them the same way)

    var i = 1
    while (i < v1d.size && v1d[i] != ZB_OPENC && v1d[i] != '\u0000') i++

    var fcnName = strnCZb(v1d, 2, i - 2)

    while (v1d[1] != ZB_OPENC && v1d[1] != '\u0000')
        zshiftZb(v1d, 2, 1)

    if (zcode == 'g' && (zcode1 == 'a' || zcode1 == 'c' || zcode1 == 's')) {
        // zglobal: the raw-name lattice keeps its ducking brackets
        zshiftZb(v1d, 2, 1)
        i = 1
        while (i < v1d.size && v1d[i] != '\u0000') i++
        if (i < 2 || v1d[i - 1] != ZB_CLOSEC || v1d[i - 2] != ZB_CLOSEC) {
            zerrZb(sv); return
        }
        v1d[i - 1] = '\u0000'
    } else {
        if (v1d[1] == ZB_OPENC)
            zshiftZb(v1d, 2, 1)
        i = 1
        while (i < v1d.size && v1d[i] != '\u0000') {
            if (v1d[i] == ZB_CLOSEC || v1d[i] == ZB_COMMAC) {
                v1d[i] = '\u0000'
                nargs++
                iargs[nargs] = i + 1
            }
            i++
        }
    }
    sv.ierr = 0

    val r = zbRouteZb(fcnName, v1d, bd, sv, iargs, nargs,
        zcode, zcode1, zcode2, zcode3)
    if (r != 0) zerrZb(sv)
}

// fcn_not_found fold
private fun znotfoundZb(fcnName: String, sv: Msv) {
    if (sv.NOERR != 0) { sv.NOERR = -1; return }
    println("\n*** Z function: \"" + fcnName +
        "\" not found in or near line " + sv.LineNumber)
    sigint(100)
}

// err fold (the shared error tail)
private fun zerrZb(sv: Msv) {
    if (sv.NOERR != 0) { sv.NOERR = -1; return }
    System.err.println("\n*** zFunction error in or near line " +
        sv.LineNumber)
    sigint(100)
}

// ServiceInterface codes + math kin codes + SQLITE arm codes:
// reused from the BifsZfcn.kt package seats (single owner pin
// per roster unit per the B6T2 convention)

// The branch table fold: returns 0 (returned) or 1 (err).
private fun zbRouteZb(
    fcnName: String, v1d: CharArray, bd: CharArray, sv: Msv,
    iargs: IntArray, nargs: Int, zcode: Char, zcode1: Char,
    zcode2: Char, zcode3: Char // the vendor zcode2/3 fossils seat here
): Int {
    // - - - GTK / MDH branch - - -
    if (fcnName.regionMatches(0, "z~gtk", 0, 5, ignoreCase = true) ||
        fcnName.regionMatches(0, "z~mdh", 0, 5, ignoreCase = true)) {
        return when {
            fcnName.equals("z~mdh~file~chooser~get~filename", ignoreCase = true) ->
                gtkFileChooserZb(v1d, bd, sv, iargs, nargs)
            fcnName.equals("z~mdh~calendar~get~date", ignoreCase = true) ->
                gtkCalendarZb(v1d, bd, sv, iargs, nargs)
            fcnName.equals("z~mdh~range~get~adjustment", ignoreCase = true) ->
                gtkRangeAdjZb(v1d, bd, sv, iargs, nargs)
            fcnName.equals("z~mdh~font~chooser~get~font", ignoreCase = true) ->
                gtkFontChooserZb(v1d, bd, sv, iargs, nargs)
            fcnName.equals("z~mdh~color~chooser~get~rgba", ignoreCase = true) ->
                gtkColorChooserZb(v1d, bd, sv, iargs, nargs)
            fcnName.equals("z~mdh~switch~get~active", ignoreCase = true) ->
                gtkSwitchGetActiveZb(v1d, bd, sv, iargs, nargs)
            fcnName.equals("z~mdh~toggle~button~get~active", ignoreCase = true) ->
                gtkToggleGetActiveZb(v1d, bd, sv, iargs, nargs)
            fcnName.equals("z~mdh~toggle~button~set~active", ignoreCase = true) ->
                gtkToggleSetActiveZb(v1d, bd, sv, iargs, nargs)
            fcnName.equals("z~mdh~spin~button~set~value", ignoreCase = true) ->
                gtkSpinSetValueZb(v1d, bd, sv, iargs, nargs)
            fcnName.equals("z~mdh~entry~get~text", ignoreCase = true) ->
                gtkEntryGetTextZb(v1d, bd, sv, iargs, nargs)
            fcnName.equals("z~mdh~entry~set~text", ignoreCase = true) ->
                gtkEntrySetTextZb(v1d, bd, sv, iargs, nargs)
            fcnName.equals("z~mdh~text~buffer~set~text", ignoreCase = true) ->
                gtkTextBufferSetTextZb(v1d, bd, sv, iargs, nargs)
            fcnName.equals("z~mdh~label~set~text", ignoreCase = true) ->
                gtkLabelSetTextZb(v1d, bd, sv, iargs, nargs)
            fcnName.equals("z~mdh~dialog~new~with~buttons", ignoreCase = true) ->
                gtkDialogWithButtonsZb(v1d, bd, sv, iargs, nargs)
            fcnName.equals("z~mdh~tree~view~get~selection", ignoreCase = true) ->
                gtkTreeViewGetSelectionZb(v1d, bd, sv, iargs, nargs)
            fcnName.equals("z~mdh~tree~selection~get~selected", ignoreCase = true) ->
                gtkTreeSelectionGetSelectedZb(v1d, bd, sv, iargs, nargs)
            fcnName.equals("z~mdh~tree~model~get", ignoreCase = true) ->
                gtkTreeModelGetZb(v1d, bd, sv, iargs, nargs)
            fcnName.equals("z~mdh~tree~store~clear", ignoreCase = true) ->
                gtkTreeStoreClearZb(v1d, bd, sv, iargs, nargs)
            fcnName.equals("z~mdh~tree~level~add", ignoreCase = true) ->
                gtkTreeLevelAddZb(v1d, bd, sv, iargs, nargs)
            fcnName.equals("z~mdh~spin~button~get~value", ignoreCase = true) ->
                gtkSpinGetValueZb(v1d, bd, sv, iargs, nargs)
            fcnName.equals("z~mdh~widget~show", ignoreCase = true) ->
                gtkWidgetShowZb(v1d, bd, sv, iargs, nargs)
            fcnName.equals("z~mdh~widget~hide", ignoreCase = true) ->
                gtkWidgetHideZb(v1d, bd, sv, iargs, nargs)
            else -> { znotfoundZb(fcnName, sv); 0 }
        }
    }

    // - - - the zz compute branch - - -
    if (fcnName.startsWith("zz")) {
        return when (fcnName.lowercase()) {
            "zzsoundex" -> zzSoundexZb(v1d, bd, sv, nargs)
            "zzcosine" -> zzSvc2Zb(v1d, bd, sv, iargs, nargs, ZC_COSINE)
            "zzinput" -> zzSvc1Zb(v1d, bd, sv, nargs, ZC_INPUT)
            "zzdice" -> zzSvc2Zb(v1d, bd, sv, iargs, nargs, ZC_DICE)
            "zzjaccard" -> zzSvc2Zb(v1d, bd, sv, iargs, nargs, ZC_JACCARD)
            "zzsim1" -> zzSvc2Zb(v1d, bd, sv, iargs, nargs, ZC_SIM1)
            "zzscanalnum" -> zzScanZb(v1d, bd, sv, iargs, nargs, ZC_SCANALNUM)
            "zzscan" -> zzScanZb(v1d, bd, sv, iargs, nargs, ZC_SCAN)
            "zzavg" -> zzSvc1Zb(v1d, bd, sv, nargs, ZC_AVG)
            "zzidf" -> zzSvc2Zb(v1d, bd, sv, iargs, nargs, ZC_INVDF)
            "zztranspose" -> zzSvc2Zb(v1d, bd, sv, iargs, nargs, ZC_TRANSPOSE)
            "zzcentroid" -> zzSvc2Zb(v1d, bd, sv, iargs, nargs, ZC_CENTROID)
            "zztermcorrelate" ->
                zzSvc2Zb(v1d, bd, sv, iargs, nargs, ZC_TERMCORRELATE)
            "zzdoccorrelate" -> zzDocCorrelateZb(v1d, bd, sv, iargs, nargs)
            "zzmultiply" -> zzMultiplyZb(v1d, bd, sv, iargs, nargs)
            "zzcount" -> zzSvc1Zb(v1d, bd, sv, nargs, ZC_COUNT)
            "zzmax" -> zzSvc1Zb(v1d, bd, sv, nargs, ZC_MAX)
            "zzmin" -> zzSvc1Zb(v1d, bd, sv, nargs, ZC_MIN)
            "zzsum" -> zzSvc1Zb(v1d, bd, sv, nargs, ZC_SUM)
            else -> { znotfoundZb(fcnName, sv); 0 }
        }
    }

    // - - - the zs sub-family - - -
    if (fcnName.regionMatches(0, "zs", 0, 2, ignoreCase = true)) {
        return when (fcnName.lowercase()) {
            "zstem" -> zstemZb(v1d, bd, sv, nargs)
            "zstopl" -> zstoplookupZb(v1d, bd, sv, iargs, nargs)
            "zsin" -> ztrigZb(v1d, bd, sv, iargs, nargs, KIN_SIN)
            "zseek" -> zseekZb(v1d, bd, sv, nargs)
            "zsmithwaterman" -> zsmithZb(v1d, bd, sv, iargs, nargs)
            "zstopinit" -> zstopinitZb(v1d, bd, sv, nargs)
            "zsyninit" -> zsyninitZb(v1d, bd, sv, nargs)
            "zsynlookup" -> zsynlookupZb(v1d, bd, sv, nargs)
            "zshred" -> zshredZb(v1d, bd, sv, iargs, nargs, false)
            "zshredquery" -> zshredZb(v1d, bd, sv, iargs, nargs, true)
            "zsrand" -> zsrandZb(v1d, bd, sv, nargs)
            "zsqlite" -> zsqliteZb(v1d, bd, sv, iargs, nargs)
            "zsqlopen" -> zsqlOpenZb(bd, sv)
            "zsqrt" -> zrootZb(v1d, bd, sv, nargs)
            "zsystem" -> zsystemZb(v1d, bd, sv, nargs)
            else -> { znotfoundZb(fcnName, sv); 0 }
        }
    }
    return zbRouteTailZb(
        fcnName, v1d, bd, sv, iargs, nargs, zcode, zcode1)
}

// The specific-name if-chain and the zcode letter fallbacks.
private fun zbRouteTailZb(
    fcnName: String, v1d: CharArray, bd: CharArray, sv: Msv,
    iargs: IntArray, nargs: Int, zcode: Char, zcode1: Char
): Int {
    val f = fcnName.lowercase()
    when (f) {
        "zlow" + "er" -> return zlowerZb(v1d, bd, sv, nargs)
        "zcos" -> return ztrigZb(v1d, bd, sv, iargs, nargs, KIN_COS)
        "zgetenv" -> return zzSvc1Zb(v1d, bd, sv, nargs, ZC_GETENV)
        "ztan" -> return ztrigZb(v1d, bd, sv, iargs, nargs, KIN_TAN)
        "zatan" -> return ztrigZb(v1d, bd, sv, iargs, nargs, KIN_ATAN)
        "zacos" -> return ztrigZb(v1d, bd, sv, iargs, nargs, KIN_ACOS)
        "zasin" -> return ztrigZb(v1d, bd, sv, iargs, nargs, KIN_ASIN)
        "zexp" -> return zexpZb(v1d, bd, sv, nargs, KIN_EXP)
        "zexp2" -> return zexpZb(v1d, bd, sv, nargs, KIN_EXP2)
        "zexp10" -> return zexpZb(v1d, bd, sv, nargs, KIN_EXP10)
        "zlog" -> return zlogZb(v1d, bd, sv, nargs, KIN_LOG)
        "zlog10" -> return zlogZb(v1d, bd, sv, nargs, KIN_LOG10)
        "zlog2" -> return zlogZb(v1d, bd, sv, nargs, KIN_LOG2)
        "zpow" -> return zpowZb(v1d, bd, sv, iargs, nargs)
        "zh" + "it" -> return zhitZb(bd, sv)
        "zchdir" -> return zchdirZb(v1d, bd, sv, nargs)
        "zf" + "lush" -> return zflushZb(v1d, bd, sv)
        "znoblanks" -> return znoblanksZb(v1d, bd, sv, iargs, nargs)
        "zpad" -> return zpadZb(v1d, bd, sv, iargs, nargs)
        "zbmgsearch" -> return zbmgsearchZb(v1d, bd, sv, iargs, nargs)
        "znative" -> { putStrZb(bd, 0, "0"); return 0 } // SQLITE arm live
        "zdump" -> return zcondenseZb(v1d, bd, sv, iargs, nargs)
        "zrestore" -> return zrestoreZb(v1d, bd, sv, nargs)
        "zcurrentfile" -> return zcurrentfileZb(bd, sv)
        "zceil" -> { if (nargs != 1) return 1
            mpsCeilZb(bd, v1d, 1); return 0 }
        "zfloor" -> { if (nargs != 1) return 1
            mpsFloorZb(bd, v1d, 1); return 0 }
        "ztrunc" -> { if (nargs != 1) return 1
            mpsTruncZb(bd, v1d, 1); return 0 }
        "zprogram" -> return zprogramZb(bd, sv)
        "zdatabase" -> return zdatabaseZb(bd)
        "zbasename" -> return zbasenameZb(v1d, bd, sv, iargs, nargs)
        "zfiletest" -> return zfiletestZb(v1d, bd, sv, iargs, nargs)
        // the vendor carries a commented-out zglobal filetest line here
        "zindexmax" -> return zDbInfoZb(bd, zindexmaxZb())
        "zindexsize" -> return zDbInfoZb(bd, "256")
        "zdatasize" -> return zDbInfoZb(bd, "512")
        "zdbname" -> return zDbInfoZb(bd, "mumps")
        "zdbfile" -> return zDbInfoZb(bd, "mumps.sqlite")
        else -> {}
    }
    // zz count/max/min/sum live in the zz branch above; the vendor
    // chain keeps its order - the fcn_not_found default sits at the
    // zcode fallback tail.
    when (zcode) {
        'a'.code.toChar() -> {
            if (zcode1 == 'b'.code.toChar()) return zabsZb(v1d, bd, sv, nargs)
            return znormalZb(v1d, bd, sv, iargs, nargs)
        }
        'd'.code.toChar() -> return zdateZb(v1d, bd, sv, iargs, nargs, zcode1)
        'v'.code.toChar() -> return zVariableZb(v1d, bd, sv, nargs)
        'n'.code.toChar() -> return znormalZb(v1d, bd, sv, iargs, nargs)
        'b'.code.toChar() -> return zblanksZb(v1d, bd, sv)
        'p'.code.toChar() -> return zpadZb(v1d, bd, sv, iargs, nargs)
        'h'.code.toChar() -> return zhtmlZb(v1d, bd, sv)
        'f'.code.toChar() -> return zfileZb(v1d, bd, sv)
        'g'.code.toChar() -> return zgCountZb(v1d, bd, sv, nargs)
        'l'.code.toChar() -> return zlogZb(v1d, bd, sv, nargs, KIN_LOG)
        'm'.code.toChar() -> return znxtrowZb(v1d, bd, sv, iargs, nargs)
        'w'.code.toChar() -> return zwordZb(v1d, bd, sv, zcode1)
        't'.code.toChar() -> return ztellZb(bd, sv)
        'u'.code.toChar() -> return znumericZb(v1d, bd, sv)
        else -> { znotfoundZb(fcnName, sv); return 0 }
    }
}

// - - - GTK platform edges (h020 handle seats) - - -
// The GTK C ABI edges below the branch table have no JVM face;
// each seats as a raised TODO stub (h020: the handle lattice rides
// as Long, the %p parse/print folds are real above).
private fun gtkEdgeStubZb(which: String): Nothing =
    TODO("stub: gtk platform edge " + which + " (h020 seat)")

private fun gtkFileChooserGetFilenameZb(h: Long): String =
    gtkEdgeStubZb("gtk_file_chooser_get_filename " + h)
private fun gtkCalendarGetDateZb(h: Long): Triple<Int, Int, Int> =
    gtkEdgeStubZb("gtk_calendar_get_date " + h)
private fun gtkRangeGetAdjustmentValueZb(h: Long): Double =
    gtkEdgeStubZb("gtk_range_get_adjustment " + h)
private fun gtkFontChooserGetFontZb(h: Long): String =
    gtkEdgeStubZb("gtk_font_chooser_get_font " + h)
private fun gtkColorChooserGetRgbaZb(h: Long): DoubleArray =
    gtkEdgeStubZb("gtk_color_chooser_get_rgba " + h)
private fun gtkSwitchGetActiveZb(h: Long): Boolean =
    gtkEdgeStubZb("gtk_switch_get_active " + h)
private fun gtkToggleGetActiveZb(h: Long): Boolean =
    gtkEdgeStubZb("gtk_toggle_button_get_active " + h)
private fun gtkToggleSetActiveZb(h: Long, a: Boolean): Unit =
    gtkEdgeStubZb("gtk_toggle_button_set_active " + h)
private fun gtkSpinButtonGetValueZb(h: Long): Double =
    gtkEdgeStubZb("gtk_spin_button_get_value " + h)
private fun gtkSpinButtonSetValueZb(h: Long, v: Double): Unit =
    gtkEdgeStubZb("gtk_spin_button_set_value " + h)
private fun gtkEntryGetTextZb(h: Long): String =
    gtkEdgeStubZb("gtk_entry_get_text " + h)
private fun gtkEntrySetTextZb(h: Long, s: String): Unit =
    gtkEdgeStubZb("gtk_entry_set_text " + h)
private fun gtkTextBufferSetTextZb(h: Long, s: String): Unit =
    gtkEdgeStubZb("gtk_text_buffer_set_text " + h)
private fun gtkLabelSetTextZb(h: Long, s: String): Unit =
    gtkEdgeStubZb("gtk_label_set_text " + h)
private fun gtkDialogNewWithButtonsZb(
    v1d: CharArray, off: Int, window: Long, headerBar: Boolean): Long {
    // the flags literal keeps the GtkDialogFlags mix seat
    return gtkEdgeStubZb("gtk_dialog_new_with_buttons " + window)
}
private fun gtkWindowSetKeepAboveZb(h: Long, a: Boolean): Unit =
    gtkEdgeStubZb("gtk_window_set_keep_above " + h)
private fun gtkDialogRunZb(h: Long): Int =
    gtkEdgeStubZb("gtk_dialog_run " + h)
private fun gtkWidgetDestroyZb(h: Long): Unit =
    gtkEdgeStubZb("gtk_widget_destroy " + h)
private fun gtkTreeViewGetSelectionZb(h: Long): Long =
    gtkEdgeStubZb("gtk_tree_view_get_selection " + h)
private fun gtkTreeSelectionGetSelectedZb(h: Long): Boolean =
    gtkEdgeStubZb("gtk_tree_selection_get_selected " + h)
private fun gtkTreeModelGetZb(
    m: Long, iterH: Long, col: Int): String =
    gtkEdgeStubZb("gtk_tree_model_get " + m)
private fun gtkTreeStoreClearZb(h: Long): Unit =
    gtkEdgeStubZb("gtk_tree_store_clear " + h)
private fun gtkTreeStoreAppendZb(
    t: Long, parent: Long, slot: Int): Long =
    gtkEdgeStubZb("gtk_tree_store_append " + t)
private fun gtkTreeStoreSetZb(
    t: Long, slot: Int, v1d: CharArray, iargs: IntArray, nargs: Int
): Unit =
    gtkEdgeStubZb("gtk_tree_store_set " + t)
private fun gtkWidgetShowZb(h: Long): Unit =
    gtkEdgeStubZb("gtk_widget_show " + h)
private fun gtkWidgetHideZb(h: Long): Unit =
    gtkEdgeStubZb("gtk_widget_hide " + h)

// sprintf f seat (C f: six decimals, the vendor format fold)
private fun fmtFZb(x: Double): String = String.format("%.6f", x)

// - - - GTK label bodies (h020 handle seats; the gtk edges are the
// raised platform stubs above) - - -
private fun gtkDialogWithButtonsZb(
    v1d: CharArray, bd: CharArray, sv: Msv, iargs: IntArray, nargs: Int
): Int {
    if (nargs != 2) return 1
    val window = strToPtrZb(v1d, 1)
    // vendor flags literal keeps the GtkDialogFlags mix seat
    val dialog = gtkDialogNewWithButtonsZb(v1d, iargs[1], window, true)
    gtkWindowSetKeepAboveZb(dialog, true)
    val r = gtkDialogRunZb(dialog)
    gtkWidgetDestroyZb(dialog)
    // Expect 200 for NO and 100 for YES
    putStrZb(bd, 0, if (r == 100) "1" else if (r == 200) "0" else "-1")
    return 0
}

private fun gtkWidgetHideZb(
    v1d: CharArray, bd: CharArray, sv: Msv, iargs: IntArray, nargs: Int
): Int {
    val widget = strToPtrZb(v1d, 1)
    gtkWidgetHideZb(widget)
    bd[0] = '\u0000'
    return 0
}

private fun gtkWidgetShowZb(
    v1d: CharArray, bd: CharArray, sv: Msv, iargs: IntArray, nargs: Int
): Int {
    val widget = strToPtrZb(v1d, 1)
    gtkWidgetShowZb(widget)
    bd[0] = '\u0000'
    return 0
}

private fun gtkSpinGetValueZb(
    v1d: CharArray, bd: CharArray, sv: Msv, iargs: IntArray, nargs: Int
): Int {
    if (nargs != 1) return 1
    zbSpinH = strToPtrZb(v1d, 1)
    val x = gtkSpinButtonGetValueZb(zbSpinH)
    putStrZb(bd, 0, fmtFZb(x))
    return 0
}

private fun gtkSpinSetValueZb(
    v1d: CharArray, bd: CharArray, sv: Msv, iargs: IntArray, nargs: Int
): Int {
    if (nargs != 2) return 1
    zbSpinH = strToPtrZb(v1d, 1)
    val v = atofZb(strnCZb(v1d, iargs[1])) // atof seat
    gtkSpinButtonSetValueZb(zbSpinH, v)
    bd[0] = '\u0000'
    return 0
}

private fun gtkTreeStoreClearZb(
    v1d: CharArray, bd: CharArray, sv: Msv, iargs: IntArray, nargs: Int
): Int {
    if (nargs != 1) return 1
    zbTreeStoreH = strToPtrZb(v1d, 1)
    gtkTreeStoreClearZb(zbTreeStoreH)
    putStrZb(bd, 0, "1")
    return 0
}

private fun gtkTreeModelGetZb(
    v1d: CharArray, bd: CharArray, sv: Msv, iargs: IntArray, nargs: Int
): Int {
    if (nargs != 5) return 1
    val m = strToPtrZb(v1d, 1)
    zbIter[0] = strToPtrZb(v1d, iargs[1])
    // one col number / value pair seat; can be called multiple
    // times for separate columns (the -1 terminator fold)
    val value = gtkTreeModelGetZb(m, zbIter[0], 1)
    putStrZb(bd, 0, value)
    return 0
}

// Tree/Table row selection signal
private fun gtkTreeSelectionGetSelectedZb(
    v1d: CharArray, bd: CharArray, sv: Msv, iargs: IntArray, nargs: Int
): Int {
    if (nargs != 2) return 1
    val s = strToPtrZb(v1d, 1)
    val col = strToPtrZb(v1d, iargs[1]).toInt()
    if (!gtkTreeSelectionGetSelectedZb(s)) {
        putStrZb(bd, 0, "0")
        return 0
    }
    bd[0] = '\u0000'
    val m = strToPtrZb(v1d, 1) // the model out-param rides the same handle
    putStrZb(bd, 0, gtkTreeModelGetZb(m, zbIter[0], col))
    return 0
}

private fun gtkTreeViewGetSelectionZb(
    v1d: CharArray, bd: CharArray, sv: Msv, iargs: IntArray, nargs: Int
): Int {
    if (nargs != 1) return 1
    val t = strToPtrZb(v1d, 1)
    val s = gtkTreeViewGetSelectionZb(t)
    putStrZb(bd, 0, ptrToStrZb(s))
    return 0
}

private fun gtkTreeLevelAddZb(
    v1d: CharArray, bd: CharArray, sv: Msv, iargs: IntArray, nargs: Int
): Int {
    if (nargs < 4) return 1
    // depth in tree for 1st column: v1d[1] -> treestore,
    // v1d[iargs[1]] -> depth
    val depth = strToPtrZb(v1d, iargs[1]).toInt()
    if (depth < 1 || depth > 10) return 0 // vendor falls off the chain
    val slot = depth
    zbTreeStoreH = strToPtrZb(v1d, 1)
    val parent = if (slot == 1) 0L else zbIter[slot - 1]
    zbIter[0] = gtkTreeStoreAppendZb(zbTreeStoreH, parent, slot)
    gtkTreeStoreSetZb(zbTreeStoreH, slot, v1d, iargs, nargs)
    putStrZb(bd, 0, "0")
    return 0
}

private fun gtkLabelSetTextZb(
    v1d: CharArray, bd: CharArray, sv: Msv, iargs: IntArray, nargs: Int
): Int {
    if (nargs != 2) return 1
    val l = strToPtrZb(v1d, 1)
    gtkLabelSetTextZb(l, strnCZb(v1d, iargs[1]))
    putStrZb(bd, 0, "1")
    return 0
}

private fun gtkTextBufferSetTextZb(
    v1d: CharArray, bd: CharArray, sv: Msv, iargs: IntArray, nargs: Int
): Int {
    if (nargs != 2) return 1
    val b = strToPtrZb(v1d, 1)
    gtkTextBufferSetTextZb(b, strnCZb(v1d, iargs[1]))
    putStrZb(bd, 0, "1")
    return 0
}

private fun gtkEntryGetTextZb(
    v1d: CharArray, bd: CharArray, sv: Msv, iargs: IntArray, nargs: Int
): Int {
    if (nargs != 1) return 1
    val e = strToPtrZb(v1d, 1)
    putStrZb(bd, 0, gtkEntryGetTextZb(e))
    return 0
}

private fun gtkEntrySetTextZb(
    v1d: CharArray, bd: CharArray, sv: Msv, iargs: IntArray, nargs: Int
): Int {
    if (nargs != 2) return 1
    val e = strToPtrZb(v1d, 1)
    gtkEntrySetTextZb(e, strnCZb(v1d, iargs[1]))
    putStrZb(bd, 0, "0")
    return 0
}

private fun gtkRangeAdjZb(
    v1d: CharArray, bd: CharArray, sv: Msv, iargs: IntArray, nargs: Int
): Int {
    if (nargs != 1) return 1
    val e = strToPtrZb(v1d, 1)
    putStrZb(bd, 0, fmtFZb(gtkRangeGetAdjustmentValueZb(e)))
    return 0
}

private fun gtkFileChooserZb(
    v1d: CharArray, bd: CharArray, sv: Msv, iargs: IntArray, nargs: Int
): Int {
    if (nargs != 1) return 1
    val e = strToPtrZb(v1d, 1)
    putStrZb(bd, 0, gtkFileChooserGetFilenameZb(e).take(STR_MAX - 1))
    return 0
}

private fun gtkCalendarZb(
    v1d: CharArray, bd: CharArray, sv: Msv, iargs: IntArray, nargs: Int
): Int {
    if (nargs != 1) return 1
    val e = strToPtrZb(v1d, 1)
    val (yr, mn, dy) = gtkCalendarGetDateZb(e)
    putStrZb(bd, 0, yr.toString() + " " + mn + " " + dy)
    return 0
}

private fun gtkFontChooserZb(
    v1d: CharArray, bd: CharArray, sv: Msv, iargs: IntArray, nargs: Int
): Int {
    if (nargs != 1) return 1
    val e = strToPtrZb(v1d, 1)
    putStrZb(bd, 0, gtkFontChooserGetFontZb(e))
    return 0
}

private fun gtkColorChooserZb(
    v1d: CharArray, bd: CharArray, sv: Msv, iargs: IntArray, nargs: Int
): Int {
    if (nargs != 1) return 1
    val c = strToPtrZb(v1d, 1)
    val r = gtkColorChooserGetRgbaZb(c)
    putStrZb(bd, 0,
        fmtFZb(r[0]) + " " + fmtFZb(r[1]) + " " +
        fmtFZb(r[2]) + " " + fmtFZb(r[3]))
    return 0
}

private fun gtkToggleSetActiveZb(
    v1d: CharArray, bd: CharArray, sv: Msv, iargs: IntArray, nargs: Int
): Int {
    if (nargs != 2) return 1
    val t = strToPtrZb(v1d, 1)
    val arg = strnCZb(v1d, iargs[1])
    if (arg == "TRUE") {
        gtkToggleSetActiveZb(t, true)
        putStrZb(bd, 0, "1")
        return 0
    }
    if (arg == "FALSE") {
        gtkToggleSetActiveZb(t, false)
        putStrZb(bd, 0, "1")
        return 0
    }
    putStrZb(bd, 0, "0")
    return 1
}

private fun gtkToggleGetActiveZb(
    v1d: CharArray, bd: CharArray, sv: Msv, iargs: IntArray, nargs: Int
): Int {
    if (nargs != 1) return 1
    val t = strToPtrZb(v1d, 1)
    putStrZb(bd, 0, if (gtkToggleGetActiveZb(t)) "1" else "0")
    return 0
}

private fun gtkSwitchGetActiveZb(
    v1d: CharArray, bd: CharArray, sv: Msv, iargs: IntArray, nargs: Int
): Int {
    if (nargs != 1) return 1
    val t = strToPtrZb(v1d, 1)
    putStrZb(bd, 0, if (gtkSwitchGetActiveZb(t)) "1" else "0")
    return 0
}

// atof seat (C strtod fold on the plain ASCII decimal shape)
private fun atofZb(s: String): Double = s.toDoubleOrNull() ?: 0.0

// ServiceInterface: reused from the BifsZfcn.kt seat
// (roster 187 b7 owner pin)

// void sql(int, struct MSV*, char*, char*, char*, const char*) -
// sqlZb: reused from the BifsZfcn.kt seat (libmpscpp.cpp sql
// owner pin; SQLCMD 55 arm rides the defines.h:181 code)
// mps arithmetic family (interp-arith.cpp statics, b9/b10 cells).
// File-local seat twins: the unit-183 twin seats are file-private
// in BifsZfcn.kt; per-TU statics seat per the C static semantics.
private fun mpsFloorZb(bd: CharArray, s: CharArray, soff: Int) {
    TODO("stub: owner interp-arith.cpp::mps_floor (roster 257, batch 9 - the old pin said roster 103, a units.csv-row number, C-015)")
}
private fun mpsCeilZb(bd: CharArray, s: CharArray, soff: Int) {
    TODO("stub: owner interp-arith.cpp::mps_ceil (roster 285, batch 10 - the old pin said roster 52, C-015)")
}
private fun mpsTruncZb(bd: CharArray, s: CharArray, soff: Int) {
    TODO("stub: owner interp-arith.cpp::mps_trunc (roster 284, batch 10 - the old pin said roster 51, C-015)")
}
// The arithmetic kin the render reaches (all interp-arith.cpp statics,
// b9/b10 cells): log2/log10/log/cos/tan/atan/acos/sin/asin/exp/exp2/
// exp10/sqrt. Not-yet-rendered roster units at this batch; the
// strmanip family precedent (xindex roster 260 b9 at the match
// render, C-015 renumber from the old 265 pin) applies.
private fun mpsLogZb(bd: CharArray, s: CharArray, soff: Int, kin: Int) {
    TODO("stub: owner interp-arith.cpp::mps_log family (roster b9/b10)")
}
private fun mpsTrigZb(bd: CharArray, s: CharArray, soff: Int, kin: Int) {
    TODO("stub: owner interp-arith.cpp::mps_cos/tan/atan/acos/sin/asin family (b9/b10)")
}
private fun mpsExpZb(bd: CharArray, s: CharArray, soff: Int, kin: Int) {
    TODO("stub: owner interp-arith.cpp::mps_exp/exp2/exp10 family (b9/b10)")
}
private fun mpsSqrtZb(bd: CharArray, s: CharArray, soff: Int) {
    TODO("stub: owner interp-arith.cpp::mps_sqrt (b9/b10 cell)")
}

// Replace: the unit-183-only arm (absent in this TU);
// StopLookup/SynLookup/ShredQuery: reused from BifsZfcn.kt
// (single owner pins, libmpscpp.cpp cross-TU)

// SQLITE arm folds 25-28: reused consts from BifsZfcn.kt
// (ZC_BEGIN/COMMIT/ROLLBACK/SAVEPOINT_TRANSACTION)

private fun zsqliteZb(
    v1d: CharArray, bd: CharArray, sv: Msv, iargs: IntArray, nargs: Int
): Int {
    // SQLITE arm live (T2 key finding)
    if (nargs == 0) { putStrZb(bd, 0, "1"); return 0 }
    val cmd = strnCZb(v1d, 1)
    if (cmd.equals("begin transaction", ignoreCase = true) && nargs == 1) {
        val filename = "begin transaction;"
        sv.tpx = sqlZb(ZC_BEGIN_TRANSACTION, sv, filename, null2Zb()).toLong()
        putStrZb(bd, 0, sv.tpx.toString())
        return 0
    }
    if (cmd.equals("commit transaction", ignoreCase = true) && nargs == 1) {
        val filename = "commit transaction;"
        sv.tpx = sqlZb(ZC_COMMIT_TRANSACTION, sv, filename, null2Zb()).toLong()
        putStrZb(bd, 0, sv.tpx.toString())
        return 0
    }
    if (cmd.equals("savepoint", ignoreCase = true) &&
        (nargs == 1 || nargs == 2)) {
        var filename = "savepoint "
        if (nargs == 2) filename += strnCZb(v1d, iargs[1]) + ";"
        else filename += " default;"
        sv.tpx = sqlZb(ZC_SAVEPOINT, sv, filename, null2Zb()).toLong()
        putStrZb(bd, 0, sv.tpx.toString())
        return 0
    }
    if (cmd.equals("rollback", ignoreCase = true) &&
        (nargs == 1 || nargs == 2)) {
        var filename = "rollback to "
        if (nargs == 2) filename += strnCZb(v1d, iargs[1]) + ";"
        else filename += " default;"
        sv.tpx = sqlZb(ZC_ROLLBACK_TRANSACTION, sv, filename, null2Zb())
            .toLong()
        putStrZb(bd, 0, sv.tpx.toString())
        return 0
    }
    if (cmd.equals("pragma", ignoreCase = true) && nargs == 2) {
        val filename = "pragma " + strnCZb(v1d, iargs[1]) + ";"
        val dmy = null2Zb()
        sv.tpx = sqlZb(ZC_PRAGMA, sv, filename, dmy).toLong()
        val got = zlenZb(dmy)
        if (got > 0) putStrZb(bd, 0, strnCZb(dmy, 0))
        else putStrZb(bd, 0, sv.tpx.toString())
        return 0
    }
    if (cmd.equals("sql", ignoreCase = true) && nargs == 2) {
        val filename = strnCZb(v1d, iargs[1]) + ";"
        val dmy = null2Zb()
        sv.tpx = sqlZb(ZW_SQLCMD, sv, filename, dmy).toLong()
        val got = zlenZb(dmy)
        if (got > 0) putStrZb(bd, 0, strnCZb(dmy, 0))
        else putStrZb(bd, 0, sv.tpx.toString())
        return 0
    }
    System.err.println("sqlite arguments not recognized")
    return 1
}

// the dmy/XTAB char seats (C local char[8]/[1024]; empty strings)
private fun null2Zb(): CharArray = CharArray(1024)

// - - - The zz ServiceInterface thin wrappers (real folds) - - -
private fun zzSvc1Zb(
    v1d: CharArray, bd: CharArray, sv: Msv, nargs: Int, code: Int
): Int {
    if (nargs != 1) return 1
    val tmp1 = tmpZb(); val tmp2 = tmpZb(); val tmp3 = tmpZb()
    strmoveZb(tmp1, 0, strnCZb(v1d, 1))
    ServiceInterface(tmp1, tmp2, null, null, tmp3, code, sv)
    putStrZb(bd, 0, strnCZb(tmp3, 0))
    return 0
}

private fun zzSvc2Zb(
    v1d: CharArray, bd: CharArray, sv: Msv, iargs: IntArray,
    nargs: Int, code: Int
): Int {
    if (nargs != 2) return 1
    val tmp1 = tmpZb(); val tmp2 = tmpZb(); val tmp3 = tmpZb()
    strmoveZb(tmp1, 0, strnCZb(v1d, 1))
    strmoveZb(tmp2, 0, strnCZb(v1d, iargs[1]))
    ServiceInterface(tmp1, tmp2, null, null, tmp3, code, sv)
    putStrZb(bd, 0, strnCZb(tmp3, 0))
    return 0
}

private fun zzScanZb(
    v1d: CharArray, bd: CharArray, sv: Msv, iargs: IntArray,
    nargs: Int, code: Int
): Int {
    if (nargs != 0 && nargs != 1) return 1
    val tmp1 = tmpZb(); val tmp2 = tmpZb(); val tmp3 = tmpZb()
    ServiceInterface(tmp1, tmp2, null, null, tmp3, code, sv)
    putStrZb(bd, 0, strnCZb(tmp3, 0))
    if (nargs == 1) { // truncation length
        val i = atolC(strnCZb(v1d, iargs[1])).toInt()
        if (i < bd.size) bd[i] = '\u0000'
    }
    return 0
}

private fun zzDocCorrelateZb(
    v1d: CharArray, bd: CharArray, sv: Msv, iargs: IntArray, nargs: Int
): Int {
    if (nargs != 4) return 1
    val tmp1 = tmpZb(); val tmp2 = tmpZb()
    val tmp2a = tmpZb(); val tmp2b = tmpZb(); val tmp3 = tmpZb()
    strmoveZb(tmp1, 0, strnCZb(v1d, 1))
    strmoveZb(tmp2, 0, strnCZb(v1d, iargs[1]))
    strmoveZb(tmp2a, 0, strnCZb(v1d, iargs[2]))
    strmoveZb(tmp2b, 0, strnCZb(v1d, iargs[3]))
    ServiceInterface(tmp1, tmp2, tmp2a, tmp2b, tmp3, ZC_DOCCORRELATE, sv)
    putStrZb(bd, 0, strnCZb(tmp3, 0))
    return 0
}

private fun zzMultiplyZb(
    v1d: CharArray, bd: CharArray, sv: Msv, iargs: IntArray, nargs: Int
): Int {
    if (nargs != 3) return 1
    val tmp1 = tmpZb(); val tmp2 = tmpZb()
    val tmp3 = tmpZb(); val tmp4 = tmpZb()
    strmoveZb(tmp1, 0, strnCZb(v1d, 1))
    strmoveZb(tmp2, 0, strnCZb(v1d, iargs[1]))
    strmoveZb(tmp3, 0, strnCZb(v1d, iargs[2]))
    ServiceInterface(tmp1, tmp2, tmp3, null, tmp4, ZC_MMULTIPLY, sv)
    putStrZb(bd, 0, strnCZb(tmp4, 0))
    return 0
}

// the ServiceInterface tmp seats (C local unsigned char[STR_MAX])
private fun tmpZb(): CharArray = CharArray(STR_MAX)

// zzSoundex
private fun zzSoundexZb(
    v1d: CharArray, bd: CharArray, sv: Msv, nargs: Int
): Int {
    if (nargs != 1) return 1
    bd[0] = v1d[1]
    var j = 1
    var i = 2
    while (i < v1d.size && v1d[i] != '\u0000') {
        if (!isalphaCZb(v1d[i])) { i++; continue }
        v1d[i] = tolowerCZb(v1d[i])
        val c = v1d[i]
        if (c == 'a' || c == 'e' || c == 'h' || c == 'i' || c == 'o' ||
            c == 'w' || c == 'u' || c == 'y') { i++; continue }
        if (bd[j - 1] != c) { bd[j] = c; j++ }
        i++
    }
    bd[j] = '\u0000'
    i = 1
    while (i < bd.size && bd[i] != '\u0000') {
        bd[i] = when (bd[i]) {
            'b', 'f', 'p', 'v' -> '1'
            'c', 'g', 'j', 'k', 'q', 's', 'x', 'z' -> '2'
            'd', 't' -> '3'
            'l' -> '4'
            'm', 'n' -> '5'
            'r' -> '6'
            else -> bd[i]
        }
        i++
    }
    if (bd.size > 4) bd[4] = '\u0000' // soundex truncation fold
    return 0
}

// zdatabase - the fossil strcmp arm (SQLITE build face)
private fun zdatabaseZb(bd: CharArray): Int {
    // if (strcmp("sql","sql")==0): the SQLITE arm is always true here;
    // the 0==1 memory-resident test is the fossil that never fires
    putStrZb(bd, 0, "2") // sql disk resident
    return 0
}

// zprogram
private fun zprogramZb(bd: CharArray, sv: Msv): Int {
    putStrZb(bd, 0, strnCZb(sv.program, 0))
    return 0
}

// zfiletest - the popen if [ -f ] fold
private fun zfiletestZb(
    v1d: CharArray, bd: CharArray, sv: Msv, iargs: IntArray, nargs: Int
): Int {
    if (nargs != 2) return 1
    val cmd = "echo \u0060if [ " + strnCZb(v1d, iargs[1]).take(500) +
        " \"" + strnCZb(v1d, 1).take(500) + "\" ]; then echo \"1\"; fi\u0060"
    val tmp = popenCZb(cmd)
    putStrZb(bd, 0, if (tmp == "1") "1" else "0")
    return 0
}

// zbasename - the popen basename fold (one and two arg arms)
private fun zbasenameZb(
    v1d: CharArray, bd: CharArray, sv: Msv, iargs: IntArray, nargs: Int
): Int {
    if (!(nargs == 1 || nargs == 2)) return 1
    val tmp: String
    if (nargs == 1) {
        val cmd = "echo \u0060basename \"" +
            strnCZb(v1d, 1).take(1000) + "\"\u0060"
        tmp = popenCZb(cmd)
    } else {
        val cmd = "echo \u0060basename \"" + strnCZb(v1d, 1).take(900) +
            "\" \"" + strnCZb(v1d, iargs[1]).take(32) + "\"\u0060"
        tmp = popenCZb(cmd)
    }
    putStrZb(bd, 0, tmp)
    return 0
}

// popen seat: sh -c first line, trailing newline stripped
private fun popenCZb(cmd: String): String {
    val p = ProcessBuilder("sh", "-c", cmd).start()
    val out = p.inputStream.bufferedReader().readLine() ?: ""
    p.waitFor()
    return out
}

// zsqlOpen
private fun zsqlOpenZb(bd: CharArray, sv: Msv): Int {
    bd[0] = if (sv.ConOpen != 0) '1' else '0'
    bd[1] = '\u0000'
    return 0
}

// zsrand
private fun zsrandZb(
    v1d: CharArray, bd: CharArray, sv: Msv, nargs: Int
): Int {
    if (nargs != 1) return 1
    val i = atolC(strnCZb(v1d, 1)).toInt()
    srandC(i)
    return 0
}

// zsmithwaterman (the unit-158 real seat)
private fun zsmithZb(
    v1d: CharArray, bd: CharArray, sv: Msv, iargs: IntArray, nargs: Int
): Int {
    if (nargs != 7) return 1
    putStrZb(bd, 0, SmithWaterman(
        strnCZb(v1d, 1), strnCZb(v1d, iargs[1]), strnCZb(v1d, iargs[2]),
        strnCZb(v1d, iargs[3]), strnCZb(v1d, iargs[4]),
        strnCZb(v1d, iargs[5]), strnCZb(v1d, iargs[6])))
    return 0
}

// znoblanks - inline filter
private fun znoblanksZb(
    v1d: CharArray, bd: CharArray, sv: Msv, iargs: IntArray, nargs: Int
): Int {
    if (nargs != 1) return 1
    var j = 0
    var i = 1
    while (i < v1d.size && v1d[i] != '\u0000') {
        if (v1d[i] != ' ') { bd[j] = v1d[i]; j++ }
        i++
    }
    if (j < bd.size) bd[j] = '\u0000'
    return 0
}

// zstopinit / zsyninit (real seats)
private fun zstopinitZb(
    v1d: CharArray, bd: CharArray, sv: Msv, nargs: Int
): Int {
    if (nargs != 1) return 1
    StopInit(strnCZb(v1d, 1))
    return 0
}

private fun zsyninitZb(
    v1d: CharArray, bd: CharArray, sv: Msv, nargs: Int
): Int {
    if (nargs != 1) return 1
    SynInit(strnCZb(v1d, 1))
    return 0
}

// zbmgsearch - the real libmpscpp.cpp BMGSearch wrapper fold over
// the bmg_fullsearch face (non-roster cross-TU; owner-pinned stub
// seat reused from BifsZfcn.kt)
private fun zbmgsearchZb(
    v1d: CharArray, bd: CharArray, sv: Msv, iargs: IntArray, nargs: Int
): Int {
    if (nargs != 2) return 1
    val p1 = strnCZb(v1d, iargs[1]) + " " // the arg2+space malloc fold
    putStrZb(bd, 0,
        bmgFullsearchZb(strnCZb(v1d, 1), p1).toString())
    return 0
}

// zshred / zshredquery thin forwards over the unit-182 seat
private fun zshredZb(
    v1d: CharArray, bd: CharArray, sv: Msv, iargs: IntArray,
    nargs: Int, query: Boolean
): Int {
    if (nargs != 2) return 1
    if (query) putStrZb(bd, 0,
        ShredQuery(sv, "", strnCZb(v1d, 1), strnCZb(v1d, iargs[1])))
    else putStrZb(bd, 0,
        Shred(strnCZb(v1d, 1), atolC(strnCZb(v1d, iargs[1])).toInt()))
    return 0
}

// zstoplookup / zsynlookup
private fun zstoplookupZb(
    v1d: CharArray, bd: CharArray, sv: Msv, iargs: IntArray, nargs: Int
): Int {
    if (nargs != 1) return 1
    bd[0] = if (stopLookupZb(v1d, 1)) '1' else '0'
    bd[1] = '\u0000'
    return 0
}

private fun zsynlookupZb(
    v1d: CharArray, bd: CharArray, sv: Msv, nargs: Int
): Int {
    if (nargs != 1) return 1
    putStrZb(bd, 0, synLookupZb(sv, "", v1d, 1))
    return 0
}

// - - - math family (mps seats by kin) - - -
private fun ztrigZb(
    v1d: CharArray, bd: CharArray, sv: Msv, iargs: IntArray,
    nargs: Int, kin: Int
): Int {
    if (nargs != 1) return 1
    // the iargs/arg-counts match the vendor one-arg trig arms
    mpsTrigZb(bd, v1d, 1, kin)
    return 0
}

private fun zexpZb(
    v1d: CharArray, bd: CharArray, sv: Msv, nargs: Int, kin: Int
): Int {
    if (nargs != 1) return 1
    mpsExpZb(bd, v1d, 1, kin)
    return 0
}

private fun zlogZb(
    v1d: CharArray, bd: CharArray, sv: Msv, nargs: Int, kin: Int
): Int {
    if (nargs != 1) return 1
    mpsLogZb(bd, v1d, 1, kin)
    return 0
}

// zpow - the expx real seat
private fun zpowZb(
    v1d: CharArray, bd: CharArray, sv: Msv, iargs: IntArray, nargs: Int
): Int {
    if (nargs != 2) return 1
    expx(strnCZb(v1d, 1), strnCZb(v1d, iargs[1]), bd)
    return 0
}

// zroot - mps_sqrt seat
private fun zrootZb(
    v1d: CharArray, bd: CharArray, sv: Msv, nargs: Int
): Int {
    if (nargs != 1) return 1
    mpsSqrtZb(bd, v1d, 1)
    return 0
}

// zhit - btree hit ratio fold
private fun zhitZb(bd: CharArray, sv: Msv): Int {
    if (sv.count == 0L) putStrZb(bd, 0, "0")
    else putStrZb(bd, 0, fmtFZb(sv.hit.toDouble() / sv.count))
    sv.count = 0
    sv.hit = 0
    return 0
}

// zlower - convert to lower case
private fun zlowerZb(
    v1d: CharArray, bd: CharArray, sv: Msv, nargs: Int
): Int {
    if (nargs != 1) return 1
    var i = 1
    var j = 0
    while (i < v1d.size && v1d[i] != '\u0000') {
        bd[j] = tolowerCZb(v1d[i]); j++; i++
    }
    if (j < bd.size) bd[j] = '\u0000'
    return 0
}

// znormal / zalter: Remove punctuation, make lower case, truncate
// (one shared body fold; the vendor labels fall together)
private fun znormalZb(
    v1d: CharArray, bd: CharArray, sv: Msv, iargs: IntArray, nargs: Int
): Int {
    if (nargs != 1 && nargs != 2) return 1
    var j = 0
    bd[0] = '\u0000'
    var i = 1
    while (i < v1d.size && v1d[i] != '\u0000') {
        if (isalnumCZb(v1d[i])) { bd[j] = tolowerCZb(v1d[i]); j++ }
        i++
    }
    if (j < bd.size) bd[j] = '\u0000'
    val wordTrunc = if (nargs == 2)
        atolC(strnCZb(v1d, iargs[1])).toInt() else 25
    if (wordTrunc < bd.size) bd[wordTrunc] = '\u0000'
    return 0
}

// zstem
private fun zstemZb(
    v1d: CharArray, bd: CharArray, sv: Msv, nargs: Int
): Int {
    if (nargs != 1) return 1
    var i = 1
    while (i < v1d.size && v1d[i] != '\u0000') {
        bd[i - 1] = tolowerCZb(v1d[i]); i++
    }
    if (i - 1 < bd.size) bd[i - 1] = '\u0000'
    // remove English language suffixes (char * Stem fold now)
    wstem(bd, zlenZb(bd, 0))
    return 0
}

// zseek - move to designated byte
private fun zseekZb(
    v1d: CharArray, bd: CharArray, sv: Msv, nargs: Int
): Int {
    if (nargs != 1) return 1
    if (sv.io < 0 || sv.io > 5 || sv.in_file[sv.io] == null) {
        bd[0] = '\u0000'
        return 0
    }
    val pos = atolC(strnCZb(v1d, 1)) // fpos_t lld parse fold
    sv.in_file[sv.io]!!.seek(pos)
    putStrZb(bd, 0, "0")
    return 0
}

// ztell - byte offset in current file
private fun ztellZb(bd: CharArray, sv: Msv): Int {
    if (sv.io < 0 || sv.io > 5) { putStrZb(bd, 0, "-1"); return 0 }
    val p = sv.in_file[sv.io] ?: sv.out_file[sv.io]
    if (p == null) { putStrZb(bd, 0, "-1"); return 0 }
    putStrZb(bd, 0, p.filePointer.toString()) // fgetpos lld fold
    return 0
}

// - - - linebuf statics (ZWI/ZWN/ZWP word parse family) - - -
private val zbLinebuf = CharArray(STR_MAX) // static char linebuf
private var zbLinebufx = 0 // static int linebufx

// zword: extract words one by one from the line buffer
private fun zwordZb(
    v1d: CharArray, bd: CharArray, sv: Msv, zcode1: Char
): Int {
    if (zcode1 == 's') { // initialize
        loadLinebufZb(v1d); zbLinebufx = 0; bd[0] = '\u0000'
        return 0
    }
    if (zcode1 == 'i') { // initialize, lower-cased
        loadLinebufZb(v1d)
        for (k in 0 until zlenZb(zbLinebuf, 0))
            zbLinebuf[k] = tolowerCZb(zbLinebuf[k])
        zbLinebufx = 0; bd[0] = '\u0000'
        return 0
    }
    if (zcode1 == 'n') { // next word
        if (zbLinebuf[zbLinebufx] == '\u0000') { bd[0] = '\u0000'; return 0 }
        while (zbLinebuf[zbLinebufx] != '\u0000' &&
            zbLinebuf[zbLinebufx] == ' ') zbLinebufx++
        if (zbLinebuf[zbLinebufx] == '\u0000') { bd[0] = '\u0000'; return 0 }
        var i = 0
        while (zbLinebuf[zbLinebufx] == '-' || zbLinebuf[zbLinebufx] == '_')
            zbLinebufx++
        while (true) { // extract next word
            bd[i] = zbLinebuf[zbLinebufx]
            if (zbLinebuf[zbLinebufx] == '\u0000' ||
                zbLinebuf[zbLinebufx] == ' ') {
                bd[i] = '\u0000'; return 0
            }
            i++; zbLinebufx++
        }
    }
    if (zcode1 == 'p') { // parse
        if (zbLinebuf[zbLinebufx] == '\u0000') { bd[0] = '\u0000'; return 0 }
        while (zbLinebuf[zbLinebufx] != '\u0000' &&
            zbLinebuf[zbLinebufx] == ' ') zbLinebufx++
        if (zbLinebuf[zbLinebufx] == '\u0000') { bd[0] = '\u0000'; return 0 }
        if (zbLinebuf[zbLinebufx] != '*' && zbLinebuf[zbLinebufx] != '-' &&
            ispunctCZb(zbLinebuf[zbLinebufx])) {
            bd[0] = zbLinebuf[zbLinebufx]; bd[1] = '\u0000'
            zbLinebufx++
            return 0
        }
        var i = 0
        while (true) { // extract next word
            bd[i] = zbLinebuf[zbLinebufx]
            if (zbLinebuf[zbLinebufx] == '\u0000' ||
                zbLinebuf[zbLinebufx] == ' ' ||
                (zbLinebuf[zbLinebufx] != '*' && zbLinebuf[zbLinebufx] != '-' &&
                    ispunctCZb(zbLinebuf[zbLinebufx]))) {
                bd[i] = '\u0000'; return 0
            }
            i++; zbLinebufx++
        }
    }
    return 1 // the vendor falls to err off the family chain
}

private fun loadLinebufZb(v1d: CharArray) {
    var i = 1
    var d = 0
    while (i < v1d.size && v1d[i] != '\u0000') {
        zbLinebuf[d] = v1d[i]; i++; d++
    }
    if (d < zbLinebuf.size) zbLinebuf[d] = '\u0000'
}

// znumeric - is string non-numeric?
private fun znumericZb(
    v1d: CharArray, bd: CharArray, sv: Msv
): Int {
    var i = 1
    while (i < v1d.size && v1d[i] != '\u0000') {
        val c = v1d[i]
        if (isdigitCZb(c) || c == '.' || c == '+' || c == '-') { i++; continue }
        bd[0] = '0'; bd[1] = '\u0000'
        return 0
    }
    bd[0] = '1'; bd[1] = '\u0000'
    return 0
}

// zblanks - remove leading and double blanks (the goto zbl1 fold)
private fun zblanksZb(
    v1d: CharArray, bd: CharArray, sv: Msv
): Int {
    var i = 1 // v1dtmp walk
    var j = 0 // bdtmp walk
    var bdx = '\u0000'
    while (i < v1d.size && v1d[i] != '\u0000' && v1d[i] == ' ') i++
    if (i >= v1d.size || v1d[i] == '\u0000') { bd[0] = '\u0000'; return 0 }
    // zbl1: copy loop
    while (i < v1d.size && v1d[i] != '\u0000') {
        if (bdx == ' ' && v1d[i] == ' ') { i++; continue }
        bdx = v1d[i]
        bd[j] = v1d[i]; j++; i++
    }
    if (j < bd.size) bd[j] = '\u0000'
    return 0
}

// zcurrentfile
private fun zcurrentfileZb(bd: CharArray, sv: Msv): Int {
    putStrZb(bd, 0, strnCZb(sv.CurrentFile, 0))
    return 0
}

// zhtml - convert codes for html transmission (inline percent encode)
private fun zhtmlZb(
    v1d: CharArray, bd: CharArray, sv: Msv
): Int {
    var n = strnCZb(v1d, 1) // the working s2 fold
    var i = 0
    while (i < n.length) {
        val c = n[i]
        if (c == ' ') {
            n = n.substring(0, i) + "+" + n.substring(i + 1); i++
        } else if (!isalnumCZb(c)) {
            // the percent-2X sprintf fold (H-015: no raw dollar)
            val t1 = "%" + (c.code.toString(16).padStart(2, '0'))
            n = n.substring(0, i) + t1 + n.substring(i + 1)
            i += 3
        } else i++
    }
    putStrZb(bd, 0, n)
    return 0
}

// zcondense / zdump - SQLITE arm: the whole body is the early
// return fossil (not avail in sqlite)
private fun zcondenseZb(
    v1d: CharArray, bd: CharArray, sv: Msv, iargs: IntArray, nargs: Int
): Int {
    // SQLITE fossil: return without touching bd
    return 0
}

// zrestore - same SQLITE fossil (not avail in sqlite)
private fun zrestoreZb(
    v1d: CharArray, bd: CharArray, sv: Msv, nargs: Int
): Int {
    return 0
}

// znxtrow - the zm fossil printf; the XNEXT walk after it is dead
// code in the vendor (gated unreachable h013)
private fun znxtrowZb(
    v1d: CharArray, bd: CharArray, sv: Msv, iargs: IntArray, nargs: Int
): Int {
    println("\u0024zm not available") // printf string seat
    if (zbDeadCodeGateZb) { // h013 unreachable in the vendor too
        // the vendor gblstr/gbltbl/table XNEXT walk (dead after the
        // unconditional printf+return; kept as the gated fossil)
        val gblstr = CharArray(STR_MAX)
        val gbltbl = CharArray(128)
        val tmp2 = CharArray(STR_MAX)
        val tmp3 = CharArray(STR_MAX)
        val tmp4 = CharArray(STR_MAX)
        if (nargs == 1) {
            strmoveZb(gblstr, 0, strnCZb(v1d, 1))
            strmoveZb(gbltbl, 0, strnCZb(v1d, 1))
        }
        strmoveZb(tmp2, 0, strnCZb(gblstr, 0))
        if (Mglobal(XNEXT, tmp2, tmp3, sv) == 0) { bd[0] = '\u0000'; return 0 }
        Mglobal(RETRIEVE, tmp2, tmp4, sv)
        var i = 0
        while (i < tmp2.size && tmp2[i] != '\u0000') {
            tmp3[i] = if (tmp2[i] < ' ') '~' else tmp2[i]; i++
        }
        if (i < tmp3.size) tmp3[i] = '\u0000'
    }
    return 0
}
// h013 unreachable gate (kept false; not const so no fold warning)
private val zbDeadCodeGateZb = false

// zabs
private fun zabsZb(
    v1d: CharArray, bd: CharArray, sv: Msv, nargs: Int
): Int {
    if (nargs != 1) return 1
    // mps_abs: the real seat (nonzero return means error)
    if (mpsAbs(strnCZb(v1d, 1), bd) != 0) return 1
    return 0
}

// zpad - pad a string with blanks
private fun zpadZb(
    v1d: CharArray, bd: CharArray, sv: Msv, iargs: IntArray, nargs: Int
): Int {
    if (nargs != 2) return 1
    val len = atolC(strnCZb(v1d, iargs[1])).toInt()
    var s = strnCZb(v1d, 1)
    while (s.length < len) s += " "
    putStrZb(bd, 0, s)
    return 0
}

// zvariable - get value from symbol table
private fun zVariableZb(
    v1d: CharArray, bd: CharArray, sv: Msv, nargs: Int
): Int {
    if (nargs != 1) return 1
    val r = sym_(1, strnCZb(v1d, 1), bd, sv)
    if (r == null) bd[0] = '\u0000'
    return 0
}

// - - - ctime/mktime folds (the zdate seats) - - -
private val ZB_DOW = arrayOf(
    "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
private val ZB_MON = arrayOf(
    "Jan", "Feb", "Mar", "Apr", "May", "Jun",
    "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")

// struct tm fold (JVM facade rename ZwTmZb: private class
// twins clash at the facade level)
private class ZwTmZb {
    var year = 0 // years since 1900 (tm_year)
    var mon = 0 // 0..11
    var mday = 0
    var hour = 0
    var min = 0
    var sec = 0
    var yday = 0
    var wday = 0
    var isdst = 0
}

// seconds/day fold (no timezones: the C library rules here are
// UTC; mktime is always fed hour=12 by the vendor)
private fun ymdToEpochZb(year: Int, mon0: Int, day: Int,
    hour: Int = 12, min: Int = 0, sec: Int = 0): Long {
    // days from 1970-01-01 (proleptic gregorian; ymdToDays fold)
    val y = if (mon0 >= 10) year + 1901 else year + 1900
    val mp = if (mon0 >= 10) mon0 - 10 else mon0 + 2 // 0=Mar..11=Feb
    val d = day - 1
    val era = Math.floorDiv(y, 400)
    val yoe = y - era * 400
    val doy = (153 * mp + 2) / 5 + d
    val doe = yoe * 365 + yoe / 4 - yoe / 100 + doy
    val days = era * 146097 + doe
    return days * 86400L + hour * 3600L + min * 60L + sec
}

// mktime fold: normalize the ZwTmZb (yday/wday from the civil fields)
private fun mktimeZb(d: ZwTmZb): Long {
    val t = ymdToEpochZb(d.year, d.mon, d.mday, d.hour, d.min, d.sec)
    val days = Math.floorDiv(t, 86400L)
    val jan1 = ymdToEpochZb(d.year, 0, 1, d.hour, d.min, d.sec)
    d.yday = (days - Math.floorDiv(jan1, 86400L)).toInt()
    d.wday = (((days % 7) + 10) % 7).toInt() // 1970-01-01 was a Thu(4)
    return t
}

// ctime fold: the 24-char Www Mmm dd hh:mm:ss yyyy shape (the
// trailing newline of the glibc string is stripped by bd[24]=0)
private fun ctimeZb(t: Long): String {
    val days = Math.floorDiv(t, 86400L)
    val sod = Math.floorMod(t, 86400L)
    val hour = (sod / 3600).toInt(); val min = ((sod % 3600) / 60).toInt()
    val sec = (sod % 60).toInt()
    val era = Math.floorDiv(days, 146097)
    val doe = days - era * 146097
    val yoe = (doe - doe / 1460 + doe / 36524 - doe / 146096) / 365
    val y = yoe + era * 400
    val doy = doe - (365 * yoe + yoe / 4 - yoe / 100)
    val mp = (5 * doy + 2) / 153
    val d = (doy - (153 * mp + 2) / 5 + 1).toInt()
    val m = (if (mp < 10) mp + 3 else mp - 9).toInt()
    val yr = (if (m <= 2) y + 1 else y)
    val dow = ((days % 7 + 10) % 7).toInt()
    fun p2(n: Int): String = if (n < 10) "0" + n else n.toString()
    return ZB_DOW[dow] + " " + ZB_MON[m - 1] + " " + p2(d) + " " +
        p2(hour) + ":" + p2(min) + ":" + p2(sec) + " " + yr
}

// zdate - the zcode1 dispatch (arms 1-8 as read from the vendor)
private fun zdateZb(
    v1d: CharArray, bd: CharArray, sv: Msv, iargs: IntArray,
    nargs: Int, zcode1: Char
): Int {
    val timex = System.currentTimeMillis() / 1000
    if (zcode1 == '1') {
        putStrZb(bd, 0, timex.toString()) // internal code ld fold
        return 0
    }
    if (zcode1 == '2') { // internal to printable
        val t = atolC(strnCZb(v1d, 1))
        putStrZb(bd, 0, ctimeZb(t).take(24))
        return 0
    }
    if (zcode1 == '3') { // zd3(year,month,day) to DayOfYear
        if (nargs != 3) return 1
        val year = atolC(strnCZb(v1d, 1)).toInt()
        val m = atolC(strnCZb(v1d, iargs[1])).toInt()
        val d = atolC(strnCZb(v1d, iargs[2])).toInt()
        if (year < 1900 || m < 1 || m > 12 || d < 1 || d > 31) return 1
        val date = ZwTmZb()
        if (year > 1900) date.year = year - 1900 else date.year = year
        date.mon = m - 1 // range is 0 to 11
        date.mday = d; date.hour = 12; date.min = 0; date.sec = 0
        date.yday = 0; date.isdst = 1
        mktimeZb(date)
        putStrZb(bd, 0, (date.yday + 1).toString()) // range 0-365
        return 0
    }
    if (zcode1 == '4') { // zd4(year,dayofyear) to gregorian
        if (nargs != 2) return 1
        var year = atolC(strnCZb(v1d, 1)).toInt()
        if (year > 1900) year -= 1900
        val d = atolC(strnCZb(v1d, iargs[1])).toInt()
        if (year < 0 || d > 366 || d < 1) return 1
        var dd = d - 1
        var m = 0
        var day = 1
        val date = ZwTmZb()
        while (true) {
            date.year = year; date.mon = m; date.mday = day
            date.hour = 12; date.min = 0; date.sec = 0
            date.yday = 0; date.isdst = 1
            mktimeZb(date)
            if (date.yday == dd) break
            day++
            if (m == 0 && day > 31) { m++; day = 1 } // jan
            else if (m == 1 && (year % 4) == 0) {
                if (day > 29) { m++; day = 1 } // feb leap
            } else if (m == 1 && (year % 4) != 0) {
                if (day > 28) { m++; day = 1 } // feb
            } else if (m == 2 && day > 31) { m++; day = 1 } // mar
            else if (m == 3 && day > 30) { m++; day = 1 } // apr
            else if (m == 4 && day > 31) { m++; day = 1 } // may
            else if (m == 5 && day > 30) { m++; day = 1 } // jun
            else if (m == 6 && day > 31) { m++; day = 1 } // jul
            else if (m == 7 && day > 31) { m++; day = 1 } // aug
            else if (m == 8 && day > 30) { m++; day = 1 } // sep
            else if (m == 9 && day > 31) { m++; day = 1 } // oct
            else if (m == 10 && day > 30) { m++; day = 1 } // nov
            else if (m == 11 && day > 31) return 1 // dec
        }
        if (date.year < 100) {
            // 19%d fold (tm_year is years-since-1900)
            putStrZb(bd, 0, "19" + date.year + " " + (date.mon + 1) +
                " " + date.mday)
        } else {
            // 20 plus 02d fold (tm_year - 100)
            putStrZb(bd, 0, "20" +
                (date.year - 100).toString().padStart(2, '0') +
                " " + (date.mon + 1) + " " + date.mday)
        }
        return 0
    }
    if (zcode1 == '5') { // zd5(year,mn,dy) to yr,dy,dw (days since Sun)
        if (nargs != 3) return 1
        val year = atolC(strnCZb(v1d, 1)).toInt()
        val m = atolC(strnCZb(v1d, iargs[1])).toInt()
        val d = atolC(strnCZb(v1d, iargs[2])).toInt()
        if (year < 1900 || m < 1 || m > 12 || d < 1 || d > 31) return 1
        val date = ZwTmZb()
        if (year > 1900) date.year = year - 1900 else date.year = year
        date.mon = m - 1; date.mday = d; date.hour = 12
        date.min = 0; date.sec = 0; date.yday = 0; date.wday = 0
        date.isdst = 0
        mktimeZb(date)
        if (date.year < 100) {
            putStrZb(bd, 0, "19" + date.year + "," + (date.yday + 1) +
                "," + date.wday)
        } else {
            // operator-negatives kept as read (the vendor warts:
            // tm_year-100-1 and tm_yday+2)
            putStrZb(bd, 0, "20" +
                (date.year - 100 - 1).toString().padStart(2, '0') +
                "," + (date.yday + 2) + "," + date.wday)
        }
        return 0
    }
    if (zcode1 == '6') { // zd6 to HH:SS
        putStrZb(bd, 0, ctimeZb(timex).substring(11, 17))
        return 0
    }
    if (zcode1 == '7' || zcode1 == '8') { // current as year,month,day
        var t = timex
        if (nargs == 1) t = atolC(strnCZb(v1d, 1))
        val tmp = ctimeZb(t)
        val mon = when (tmp.substring(4, 7)) {
            "Jan" -> "01"; "Feb" -> "02"; "Mar" -> "03"; "Apr" -> "04"
            "May" -> "05"; "Jun" -> "06"; "Jul" -> "07"; "Aug" -> "08"
            "Sep" -> "09"; "Oct" -> "10"; "Nov" -> "11"; "Dec" -> "12"
            else -> "??"
        }
        var out = tmp.substring(20, 24) + "-" + mon + "-" +
            tmp.substring(8, 10)
        if (zcode1 == '8') out += "," + ctimeZb(t).substring(11, 19)
        // the space-to-0 fold (bd[j]==' ' -> '0' covers yyyy-MM-dd)
        out = out.replace(' ', '0')
        putStrZb(bd, 0, out)
        return 0
    }
    if (nargs != 0) return 1
    putStrZb(bd, 0, ctimeZb(timex).take(24))
    return 0
}

// zfile - does file exist? (open/close fd fold)
private fun zfileZb(
    v1d: CharArray, bd: CharArray, sv: Msv
): Int {
    val f = java.io.File(strnCZb(v1d, 1))
    val x = if (f.exists()) 0 else -1 // fd seat
    bd[0] = if (x == -1) '0' else '1'
    bd[1] = '\u0000'
    return 0
}

// zglobal branch (zcode g fallback): the SQLITE arm routes the
// global-name face through ServiceInterface with GBLNME 20
// (defines.h:245) - the native count-empties walk is the #else arm
private const val ZW_GBLNME = 20 // defines.h:245
private fun zgCountZb(
    v1d: CharArray, bd: CharArray, sv: Msv, nargs: Int
): Int {
    if (nargs != 1) return 1
    val tmp1 = tmpZb(); val tmp2 = tmpZb(); val tmp3 = tmpZb()
    strmoveZb(tmp1, 0, strnCZb(v1d, 1))
    ServiceInterface(tmp1, tmp2, null, null, tmp3, ZW_GBLNME, sv)
    putStrZb(bd, 0, strnCZb(tmp3, 0))
    return 0
}

private const val ZW_SQLCMD = 55 // defines.h:181 (this TU drives the
// zsqlite sql arm with SQLCMD, not PRAGMA)

// SQLITE db-info faces (#ifdef SQLITE; the #else arm returns "")
// zindexmax is the 14-2 sprintf fold (12); the values literals keep
// the vendor faces
private fun zindexmaxZb(): String {
    val i = 14
    return (i - 2).toString()
}
private fun zDbInfoZb(bd: CharArray, v: String): Int {
    putStrZb(bd, 0, v)
    return 0
}

// zsystem - spawn shell and exec command
private fun zsystemZb(
    v1d: CharArray, bd: CharArray, sv: Msv, nargs: Int
): Int {
    if (nargs != 1) return 1
    val p = ProcessBuilder("sh", "-c", strnCZb(v1d, 1)).start()
    p.waitFor()
    putStrZb(bd, 0, p.exitValue().toString())
    return 0
}

// zchdir
private fun zchdirZb(
    v1d: CharArray, bd: CharArray, sv: Msv, nargs: Int
): Int {
    if (nargs != 1) return 1
    val d = java.io.File(strnCZb(v1d, 1))
    putStrZb(bd, 0, if (d.isDirectory) "0" else "1")
    return 0
}

// zflush - flush native btree records to disk
private fun zflushZb(
    v1d: CharArray, bd: CharArray, sv: Msv
): Int {
    val tmp2 = tmpZb(); val tmp3 = tmpZb()
    Mglobal(FLUSHZb, tmp2, tmp3, sv)
    bd[0] = '\u0000'
    return 0
}
// FLUSHZb 21: reused const from BifsZfcn.kt (defines.h:91/:180)
