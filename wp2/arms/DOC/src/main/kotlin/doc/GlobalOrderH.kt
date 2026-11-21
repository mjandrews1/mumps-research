package doc

// Unit 265 (B9 tranche 2): include/mumpsc/globalOrder.h::GlobalOrder
// (batch 9, core stratum; units.csv row 266: loc 16, ptr 1, ctl
// 5, score 5.6, rank 111; physLOC 16 physically verified this
// session; globalOrder.h:8 inline). The FOUR-ARG interpreter
// inline (the Int carrier family distinct from the landed
// unit-39/69 String GlobalOrder renders per the same-name
// H-015 overload rule; the Stubs.GlobalOrder inline seat flips
// to delegate below with the tmp2 carrier corrected NULLABLE -
// the vendor NULL direction-argument arm is real: tmp2==NULL
// routes to OrderNext). Consult H-014 hit-reuse: Mglobal
// (ORDERNEXT 12 / ORDERPREV 13, defines.h:170/171, added to the
// Carriers consts this push; XNEXT 8 already seated) and
// ErrorMessage (SYM.h:41 family seat). The >127 byte
// quantization loop is the unsigned-char domain fold kept
// verbatim (Char vs the vendor high-bit bytes; the u188
// adjudication domain, H-012). Naming kept verbatim
// (GlobalOrder.kt owns the String family; this inline rides the
// CharArray signature disambiguation).

fun GlobalOrder(tmp0: CharArray, tmp1: CharArray, tmp2: String?,
        svPtr: Msv): Unit {
    // for (int i = 0; tmp0[i] != 0; i++) if (tmp0[i] > 127) tmp0[i] = 1;
    var i = 0
    while (i < tmp0.size && tmp0[i] != NUL) {
        if (tmp0[i] > '\u007f') tmp0[i] = '\u0001'
        i++
    }

    if (tmp2 == null) { // direction arg
        Stubs.Mglobal(ORDERNEXT, tmp0, tmp1, svPtr)
        return
    }

    if (tmp2 == "1") {
        Stubs.Mglobal(ORDERNEXT, tmp0, tmp1, svPtr)
        return
    }

    if (tmp2 == "-1") {
        Stubs.Mglobal(ORDERPREV, tmp0, tmp1, svPtr)
        return
    }

    Stubs.ErrorMessage("\u0024order() second argument incorrect",
            svPtr.LineNumber)
}
