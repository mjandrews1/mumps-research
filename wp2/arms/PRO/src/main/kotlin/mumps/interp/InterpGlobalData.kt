package mumps.interp

/**
 * PRO-arm translation of interp-sysfunc.cpp::GlobalData (B7
 * tranche 2, unit 204, rank 137). Translated independently
 * from the pristine vendor source (mumps-14-may-2026.tgz,
 * sha256 621deb25...ffdbe), reference source lines 733-780.
 *
 * Identity verified against the study tool:
 * interp-sysfunc.cpp::GlobalData, cpp, loc 43,
 * comment_ratio 0.023, pointer_marks 0, control_marks 7,
 * score 14.3 - exact match to wp1/study-subset/units.csv
 * row 204 (batch 7). Signature disambiguation: this is the
 * three-argument runtime Data helper (tmp0, tmp2, svPtr) -
 * DISTINCT from the one-argument intrinsic mstring
 * Global::Data() (B3 unit 86, mdh/GlobalData.kt; the
 * CppsysfuncH/SysfuncH KDocs carry the cross-file note).
 *
 * GlobalData(tmp0, tmp2, svPtr): the dollar-data probe. A
 * RETRIEVE on the bare name tests for data; a NEXT on the
 * name suffixed SOH (and, under SQLITE, a second SOH) tests
 * for a descendant. The four f/f1 cases map tmp2 to 0, 1,
 * 10, or 11.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h007 char_buffer       : tmp1/tmp3 STR_MAX scratch (fresh
 *    locals; no statics in the vendor body).
 *  - h012 unsigned_byte_domain: tmp0..tmp3 Char(0..255)
 *    domains; the suffix separator bytes are SOH Char(1).
 *  - h014 out_of_scope_call : Mglobal via the mglobal
 *    InterpStubs seat with RETRIEVE=0/NEXT=2 (defines.h:83-85);
 *    strcpy/strlen/strncmp via the interp CstringOps seats
 *    (strcpyC/strlenC/strncmpC per-file precedent).
 *  - h015 grammar_collision : pre-push scan incl. cross-file
 *    top-level names: new top-level GlobalData(CharArray,
 *    CharArray, Msv) - distinct from the mdh Global.Data()
 *    intrinsic seats (different package and shape) - no
 *    collisions.
 *  - h018 preprocessor_conditional: the reference build
 *    DEFINES SQLITE - the single-SOH arm (non-SQLITE) is
 *    EXCLUDED, and the SQLITE arm is LIVE: tmp0 gets the
 *    SOH,SOH,NUL tail and the descendant probe additionally
 *    requires the strncmp prefix match (descendant key must
 *    start with the parent name).
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - tmp3 saves tmp0 BEFORE the RETRIEVE and tmp0 is restored
 *    from it after (Mglobal may advance tmp0 in place) - kept.
 *  - The SQLITE arm's f1 = 1/else 0 fold re-assigns f1 after
 *    the NEXT return, NOT returning early - kept.
 *  - The tmp2 outputs are literal cstrings: "0", "1", "10",
 *    "11" (the four arms) - kept exactly, including the
 *    f==0/f1==1 arm's three-byte "10" (leading '1', then
 *    '0', then NUL).
 *  - The stray comment "DATA Data data" is roster text (kept
 *    here, not emitted).
 */
private const val RETRIEVE_GD = 0 // vendor defines.h:83
private const val NEXT_GD = 2 // vendor defines.h:85
private const val STR_MAX_GD = 32000 // vendor strmax.h:3 (unit-44 precedent)

private val SOH = 1.toChar() // the descendant-suffix separator byte

fun GlobalData(tmp0: CharArray, tmp2: CharArray, svPtr: Msv) {
    val tmp1 = CharArray(STR_MAX_GD)
    val tmp3 = CharArray(STR_MAX_GD)

    strcpyC(tmp3, tmp0) // strcpy(tmp3, tmp0)

    val i = strlenC(tmp0) // _i=strlen(tmp0)

    val f = mglobal(RETRIEVE_GD, tmp0, tmp1, svPtr)

    strcpyC(tmp0, tmp3) // strcpy(tmp0, tmp3)

    // SQLITE arm (LIVE in the reference build): SOH,SOH,NUL tail.
    tmp0[i] = SOH
    tmp0[i + 1] = SOH
    tmp0[i + 2] = NUL
    var f1 = mglobal(NEXT_GD, tmp0, tmp1, svPtr) // descendant?
    if (f1 == 1 && strncmpArr(tmp0, tmp3, i) == 0) {
        f1 = 1
    } else {
        f1 = 0 // descendant / no descendant
    }

    if (f == 0 && f1 == 0) { // no data; no descendant
        tmp2[0] = '0'
        tmp2[1] = NUL
        return
    }

    if (f == 0 && f1 == 1) { // no data; has descendant
        tmp2[0] = '1'
        tmp2[1] = '0'
        tmp2[2] = NUL
        return
    }

    if (f == 1 && f1 == 0) { // has data; no descendant
        tmp2[0] = '1'
        tmp2[1] = NUL
        return
    }

    if (f == 1 && f1 == 1) { // has data; has descendant
        tmp2[0] = '1'
        tmp2[1] = '1'
        tmp2[2] = NUL
        return
    }
}

/** C strncmp(a, b, n) over the byte domains (per-file precedent). */
private fun strncmpArr(a: CharArray, b: CharArray, n: Int): Int {
    for (k in 0 until n) {
        val ca = a[k]
        val cb = b[k]
        if (ca != cb) return if (ca < cb) -1 else 1
        if (ca == NUL) return 0
    }
    return 0
}
