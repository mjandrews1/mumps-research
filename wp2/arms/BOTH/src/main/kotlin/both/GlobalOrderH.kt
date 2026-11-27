package both

// Unit 259 (BOTH B9, core): include/mumpsc/globalOrder.h::
// GlobalOrder (16 LOC). Batch_plan ordinal 259; units.csv row
// 265. The HEADER face of the \\u0024order dispatch (inline in
// globalOrder.h; the mstring face of unit 251 is the distinct
// libmpscpp.cpp overload): wash the key's high bytes in place,
// then dispatch Mglobal ORDERNEXT/ORDERPREV on the direction
// argument, with the ErrorMessage fallthrough for a bad
// second argument. The B8 BuildGlobal.kt TODO stub (raised at
// the unit-232 close for this owner) releases here: the h014
// delegate-when-owner-lands flip, this file owns the body.
//
// BOTH consults (find_rule before render, events logged):
//   h012 unsigned_byte_domain - reused: the key wash loop
//     (tmp0[i] > 127 -> tmp0[i] = 1) stays in the Char byte
//     domain, in-place over the caller's buffer, exact vendor
//     comparison (> 127 on the unsigned byte value).
//   h014 out_of_scope_call - reused: Mglobal ORDERNEXT/
//     ORDERPREV ride the BothCRuntime seat with the
//     package consts ORDERNEXT=12/ORDERPREV=13 (defines.h:170/
//     171, the LibmpscppMisc6 seat); ErrorMessage rides the
//     non-unit seat seated real at this render
//     (BothCRuntime.kt, interp-sysfunc.cpp:251 provenance;
//     sigint(100) termination).
//   h017 naming - reused: GlobalOrder keeps the vendor name
//     (distinct from the unit-251 mstring face by signature);
//     tmp0/tmp1/tmp2/svPtr keep vendor names.
//   Dafny: exempt (Mglobal side effect; the byte wash is
//     covered by the keyfix family precedent).
//
// Vendor quirks kept (ledgered):
//   - the header body is inline (C99 inline, external
//     definition in the TU that includes it); the Kotlin seat
//     is the single package-level body, call sites bind
//     unchanged (BuildGlobal.kt).
//   - the vendor passes tmp2 as a real pointer that may be
//     NULL; the Kotlin face keeps the nullable CharArray and
//     compares by C string content (strcmp semantics).
//   - the wash happens BEFORE the NULL check (a NULL tmp2 with
//     high bytes still washes tmp0); order kept verbatim.

internal fun GlobalOrder(
    tmp0: CharArray,
    tmp1: CharArray,
    tmp2: CharArray?,
    svPtr: Msv,
) {
    // for (int i = 0; tmp0[i] != 0; i++) if (tmp0[i] > 127) tmp0[i] = 1;
    var i = 0
    while (tmp0[i] != 0.toChar()) {
        if (tmp0[i].code > 127) tmp0[i] = 1.toChar()
        i++
    }

    // if (tmp2 == NULL) { Mglobal(ORDERNEXT, tmp0, tmp1, svPtr); return; }
    if (tmp2 == null) {
        Mglobal(ORDERNEXT, tmp0, tmp1, svPtr)
        return
    }

    // if (strcmp((char *) tmp2, "1") == 0) { ORDERNEXT; return; }
    if (cStringOf(tmp2) == "1") {
        Mglobal(ORDERNEXT, tmp0, tmp1, svPtr)
        return
    }

    // if (strcmp((char *) tmp2, "-1") == 0) { ORDERPREV; return; }
    if (cStringOf(tmp2) == "-1") {
        Mglobal(ORDERPREV, tmp0, tmp1, svPtr)
        return
    }

    // ErrorMessage("\\u0024order() second argument incorrect", svPtr->LineNumber);
    ErrorMessage("\u0024order() second argument incorrect", svPtr.LineNumber)
}
