package doc

// Unit 198 (B7 tranche 2): interp-sysfunc.cpp::GlobalData (batch 7,
// core stratum; LOC 43, score 14.3, rank 137; vendor
// interp-sysfunc.cpp:733, physically verified this session; nonblank
// 43 matches the roster row) - the INTERPRETER-SIDE (dollar)data
// probe over the Mglobal tree, distinct from the libmpscpp command
// builder renders of units 70/192 (GlobalData.kt; overload
// multiplicity ledgered per H-015 same-NAME rule - this row is the
// interp-sysfunc.cpp three-buffer intrinsic).
//
// H-018 preprocessor arms: the vendor carries a #if !(defined
// (SQLITE)) NEXT-probe arm and a #if defined(SQLITE) arm; the
// reference build config (config.h authority, cookbook H-018)
// defines SQLITE, so the SQLITE arm is LIVE and the non-SQLITE arm
// is dead (journaled here, not rendered; the mps_justify unit-73
// precedent). The live arm appends TWO 0x01 bytes at the probe root
// (tmp0[_i]=1; tmp0[_i + 1]=1; tmp0[_i + 2]=0) and reduces f1 by
// the strncmp descendant test kept verbatim below.
//
// Body: tmp3 backs up the caller's tmp0 across the RETRIEVE
// (Mglobal reads tmp0 and the SQLITE probe OVERWRITES it in place);
// the vendor restores tmp0 from tmp3 BEFORE the probes so the
// strncmp runs against the pristine key (kept verbatim - the Hitch
// in the vendor order). The four outcome branches write the
// two-character data/descendant answer into tmp2 (probe-result
// out-parameter, H-007 mutable-scratch role) with the C NUL domain
// kept (tmp2[1]=0 vs tmp2[1]='0' - the vendor mixes the INTEGER 0
// and the CHARACTER '0' here verbatim; the byte-domain mapping per
// H-012 keeps both).
//
// Consult H-018 hit-reuse; naming per H-017 (GlobalData kept; the
// CharArray,CharArray,Msv overload signature resolves the existing
// String/Mstring renders apart).

fun GlobalData(tmp0: CharArray, tmp2: CharArray, svPtr: Msv) {
    // unsigned char tmp1[STR_MAX], tmp3[STR_MAX];
    val tmp1 = CharArray(STR_MAX)
    val tmp3 = CharArray(STR_MAX)
    var f: Int
    var f1: Int
    var i: Int

    CstringOps.strcpyC(tmp3, tmp0)

    i = CstringOps.strlenC(tmp0)

    f = Stubs.Mglobal(RETRIEVE, tmp0, tmp1, svPtr)

    CstringOps.strcpyC(tmp0, tmp3)

    tmp0[i] = CHAR_1
    tmp0[i + 1] = CHAR_1
    tmp0[i + 2] = NUL
    f1 = Stubs.Mglobal(NEXT, tmp0, tmp1, svPtr) // descendant?
    if (f1 == 1 && CstringOps.strncmpC(tmp0, tmp3, i) == 0) {
        f1 = 1
    } else {
        f1 = 0 // descdant / no descendant
    }

    if (f == 0 && f1 == 0) { // no data; no descendant
        tmp2[0] = '0'
        tmp2[1] = '0'
        // vendor stores the INTEGER 0 at tmp2[1] here (byte 0x00):
        tmp2[1] = NUL
        return
    }

    if (f == 0 && f1 == 1) { // no data; has descendant
        tmp2[0] = '1'
        tmp2[1] = '0'
        // vendor stores the INTEGER 0 at tmp2[2] here (terminator
        // one past the two-character answer):
        tmp2[2] = NUL
        return
    }

    if (f == 1 && f1 == 0) { // has data; no descendant
        tmp2[0] = '1'
        // vendor stores the INTEGER 0 at tmp2[1] here:
        tmp2[1] = NUL
        return
    }

    if (f == 1 && f1 == 1) { // has data; has descendant
        tmp2[0] = '1'
        tmp2[1] = '1'
        // vendor stores the INTEGER 0 at tmp2[2] here (terminator
        // one past the two-character answer):
        tmp2[2] = NUL
        return
    }
}
