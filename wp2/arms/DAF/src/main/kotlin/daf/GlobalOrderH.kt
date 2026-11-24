package daf

/*
 * DAF batch-9 unit 259 (roster row 259; include/mumpsc/
 * globalOrder.h::GlobalOrder, core, LOC 16, score 11.2). Consult
 * 2026-11-24T22:36:30Z.
 *
 * The header-inline \u0024order direction dispatcher: the high-bit
 * scratch strip, then the Mglobal g-arm latch (ORDERNEXT for the
 * no-direction and "1" arms, ORDERPREV for the "-1" arm, the
 * ErrorMessage tail otherwise). The g-arm constants and the
 * MglobalDaf seat are the unit-246 btree lattice (ORDERNEXT_D=12 /
 * ORDERPREV_D=13); the char* body form of the free wrapper
 * (GlobalOrder(mstring,int)) is the unit-251 twin.
 *
 * Quirks kept: the > 127 scratch byte strip runs IN PLACE on the
 * key carrier (tmp0) before the walk; the error tail relays to the
 * errorMessageDaf seat (the B1 raise; the abort-on-error vendor
 * truth is the seat's release note, not this body's).
 */

fun globalOrderHD(tmp0: CharArray, tmp1: CharArray,
                  tmp2: CharArray?, sv: DafMsv) {
    // for (i=0; tmp0[i]; i++) if (tmp0[i] > 127) tmp0[i] = 1;
    var i = 0
    while (tmp0[i] != NUL_D) {
        if (tmp0[i].code > 127) tmp0[i] = '\u0001'
        i++
    }

    if (tmp2 == null) { // direction arg
        MglobalDaf(ORDERNEXT_D, tmp0, tmp1, sv)
        return
    }
    val d = DafCRuntime.cstring(tmp2)
    if (d == "1") {
        MglobalDaf(ORDERNEXT_D, tmp0, tmp1, sv)
        return
    }
    // vendor literal is "-" "1" ("\u0024order() second arg..." tail)
    if (d == "-1") {
        MglobalDaf(ORDERPREV_D, tmp0, tmp1, sv)
        return
    }
    errorMessageDaf("\u0024order() second argument incorrect",
        DafInterpState.svPtr.LineNumber)
}
