package mumps.interp

/**
 * PRO-arm translation of include/mumpsc/globalOrder.h::GlobalOrder
 * (roster row 266, batch 9; units.csv: loc 16, comment_ratio 0.059,
 * pointer_marks 1, control_marks 5, score 11.2, rank 111).
 * Translated independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), reference
 * source lines 8-30 (the inline after the April 29 2024 date
 * comment; three includes map h014: btree.h -> the u50/u130
 * native-btree seat family, global.h -> the GlobalRef carrier,
 * sym.h -> the Msv carrier) - RESOLVES the InterpStubs
 * GlobalOrder stub with TAG CORRECTION (u141 family: the "not a
 * corpus unit" tag superseded; the Fcn.kt :1150/:1169/:1183 call
 * sites bind unchanged on the offset-carrying signature).
 *
 * GlobalOrder(tmp0, tmp0Off, tmp1, tmp1Off, tmp2, tmp2Off, svPtr):
 * the $order() global walk - map every key byte >127 to SOH
 * (1) in place, then ORDERNEXT on a null or "1" direction arg,
 * ORDERPREV on "-1", ErrorMessage otherwise. The two three-char
 * literal tails live in the Fcn.kt call sites' tmp2 arrays
 * (ONE_C/MINUS1_C, h014 note).
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h001 interior_pointer   : the two Mglobal calls pass &tmp0
 *    [tmp0Off] - the h001 copy-in/write-back fold rides the
 *    shared mglobalAt offset seat (u173 precedent).
 *  - h012 unsigned_byte_domain: the >127 fold compares .code
 *    and stores Char(1) (the vendor SOH byte); the direction
 *    tests are strcmp against the NUL-terminated tmp2 window.
 *  - h014 out_of_scope_call : mglobalAt (owner interp-globalb.cpp
 *    ::Mglobal u173/Mglobal.kt); ErrorMessage (InterpSysfuncMisc
 *    .kt:102, takes the CharArray + LineNumber Long shapes,
 *    unit-162 note); the falls-through-else arm returns WITHOUT
 *    the Mglobal call (kept verbatim).
 *  - h015 grammar_collision : GlobalOrder replaces the removed
 *    stub one-for-one (same signature); the mdh-side wrapper
 *    family (row 274) is receiver-scoped in mumps.mdh - no
 *    collisions.
 *  - h017 naming            : GlobalOrder keeps the vendor name
 *    verbatim.
 */
fun GlobalOrder(tmp0: CharArray, tmp0Off: Int, tmp1: CharArray, tmp1Off: Int,
                tmp2: CharArray?, tmp2Off: Int, svPtr: Msv) {
    // vendor: for (i=0; tmp0[i]!=0; i++) if (tmp0[i]>127) tmp0[i]=1;
    // (>127 bytes on BOTH sides of the write - keyfix in place)
    var i = tmp0Off
    while (tmp0[i] != NUL) {
        if (tmp0[i].code > 127) tmp0[i] = 1.toChar()
        i++
    }

    if (tmp2 == null) { // direction arg absent
        // vendor: Mglobal(ORDERNEXT,tmp0,tmp1,svPtr); return;
        mglobalAt(ORDERNEXT, tmp0, tmp0Off, tmp1, tmp1Off, svPtr)
        return
    }

    if (strcmpWin(tmp2, tmp2Off, "1") == 0) {
        mglobalAt(ORDERNEXT, tmp0, tmp0Off, tmp1, tmp1Off, svPtr)
        return
    }

    if (strcmpWin(tmp2, tmp2Off, "-1") == 0) {
        mglobalAt(ORDERPREV, tmp0, tmp0Off, tmp1, tmp1Off, svPtr)
        return
    }

    // vendor: ErrorMessage(<dollar>order() second argument incorrect",
    //                     svPtr->LineNumber); // <dollar> per KDoc convention
    // DOLLAR carrier: no raw dollar in code (Match.kt precedent)
    val msg = 36.toChar() + "order() second argument incorrect"
    val msgBuf = CharArray(msg.length + 1)
    var m = 0
    while (m < msg.length) {
        msgBuf[m] = msg[m]
        m++
    }
    msgBuf[m] = NUL
    ErrorMessage(msgBuf, svPtr.LineNumber)
    // vendor falls through and returns (no Mglobal call) - kept
}

/** C strcmp against the NUL-terminated window of buf at off. */
private fun strcmpWin(buf: CharArray, off: Int, s: String): Int {
    var k = 0
    while (true) {
        val ca = buf[off + k]
        val cb = if (k < s.length) s[k] else NUL
        if (ca != cb) return if (ca < cb) -1 else 1
        if (ca == NUL) return 0
        k++
    }
}

private const val ORDERNEXT = 12 // vendor defines.h:170
private const val ORDERPREV = 13 // vendor defines.h:171
