package doc

// Unit 175 (B6 tranche 2): libmpscpp.cpp::Dice (batch 6, core
// stratum; roster row 175: loc 42, score 26.8, batch_plan strata
// order). The Dice document coefficient over two ordered globals:
// the Cosine family shape minus the second (B-restored) walk. A
// single XNEXT loop walks the A-side siblings, accumulates totD
// from the A values, probes the B-side sibling at ctmp + the A
// subscript suffix (the in-place strcpy/strcat onto B.ref), and
// on a B hit accumulates totQ and the cross-product tot, finally
// returning 2*tot / (totD+totQ) with the zero guard val==0.
// Seat table: Mglobal rides the Stubs seat (XNEXT 8, the Carriers
// const), get_global rides its Stubs seat with the tpx
// GLOBAL_NOT_FOUND raise (Cosine precedent), the B.Data() probe
// rides the Stubs.globalData seat, strtod reuses the MpsExp
// render. Vendor quirk kept and noted: the vendor writes B.ref in
// place INSIDE the loop and at loop exit the object member still
// carries the LAST concatenated key, never restored (Dice, unlike
// Cosine, has no restore pass); the immutable DOC GlobalRef
// renders that walk state in the local bref string with the SAME
// observable seat-call sequence, the documented divergence per
// the Cosine unit-85 / TermCorrelate unit-14 precedent. The
// vendor carve order (B pair trimmed via j, then A pair via i,
// each with the NUL rewrite) kept verbatim; j is dead after the
// carve (only i feeds strncmp) - kept as a mutable local for the
// faithful shape. The val/val1 vendor locals widened to value/
// value1 (H-015 identifier hygiene). Naming per H-017.

fun Dice(A: GlobalRef, B: GlobalRef): Double {
    val ttt = CharArray(STR_MAX)
    val ctmp = CharArray(STR_MAX)
    val btmp = CharArray(STR_MAX)
    val tmp = CharArray(STR_MAX)
    val tmp1 = CharArray(STR_MAX)
    val rslt = CharArray(STR_MAX)

    var totD = 0.0
    var totQ = 0.0
    var tot = 0.0

    CstringOps.strcpyS(tmp, A.ref)
    var i = A.ref.length
    var j = B.ref.length
    CstringOps.strcpyS(ctmp, B.ref)

    // vendor subscript-terminator carve: chop a trailing Char_1
    // pair on B (via j), then on A (via i), NUL rewrite each
    if (ctmp[j - 2] == CHAR_1 && ctmp[j - 1] == CHAR_1) {
        j--
        ctmp[j] = NUL
    }

    if (tmp[i - 2] == CHAR_1 && tmp[i - 1] == CHAR_1) {
        i--
        tmp[i] = NUL
    }

    while (true) {
        if (Stubs.Mglobal(XNEXT, CstringOps.cstring(tmp), ttt, Globals.svPtr) == 0) break

        if (CstringOps.strncmpS(A.ref, tmp, i) != 0) break

        CstringOps.copyAt(tmp1, 0, tmp)
        Stubs.get_global(tmp1, rslt)

        if (Globals.svPtr.tpx == 0L) {
            Globals.GlobalExceptionCode = GLOBAL_NOT_FOUND
            throw MumpsGlobalException()
        }

        val value = strtod(CstringOps.cstring(rslt))
        totD = totD + value

        // B side: vendor strcpy(B.ref,ctmp); strcat(B.ref,&tmp[i]);
        // the object member carries the last concatenated key at
        // loop exit (never restored; quirk noted in the header)
        val bref = CstringOps.cstring(ctmp) + CstringOps.cstringAt(tmp, i)
        CstringOps.strcpyS(btmp, bref)

        if (!Stubs.globalData(B, bref)) continue

        Stubs.get_global(btmp, rslt)

        if (Globals.svPtr.tpx == 0L) {
            Globals.GlobalExceptionCode = GLOBAL_NOT_FOUND
            throw MumpsGlobalException()
        }

        val value1 = strtod(CstringOps.cstring(rslt))
        totQ = totQ + value1

        tot = tot + value * value1
    }

    val value = totD + totQ

    if (value == 0.0) return 0.0

    return 2 * tot / value
}
