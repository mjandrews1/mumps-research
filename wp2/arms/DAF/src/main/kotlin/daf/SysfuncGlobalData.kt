package daf

// Unit 198 (DAF B7 T2, core): interp-sysfunc.cpp::GlobalData (43 LOC,
// score 14.3): the \u0024data intrinsic kernel, the 2-arg svPtr service
// form (retrieved value presence + descendant probe over the
// NEXT-modified key). TU state: interp-sysfunc.cpp:52 defines SQLITE
// (verified this session - consistent with the interp-globalb.cpp:45
// T2 KEY FINDING), so the SQLITE arm is the LIVE reference-build arm
// and carries the descendant-prefix strncmp test; the non-SQLITE arm
// is the dead lattice arm - BOTH arms render per the GlobalQuery
// precedent (rendered but gated behind the SQLITE_D const with the
// fold noted), the dead arm's probe bytes 1,0 against the live arm's
// 1,1,0 kept distinct.
// Consults (2026-11-24T16:11:00Z, nominal): BASE-log row 96
// hit-reuse (the dual-role carrier: tmp0 is read by RETRIEVE, then
// byte-extended by the copy + NEXT walk of the KiLL XNEXT family;
// the NEXT_D opcode seat), row 8 hit-reuse (buffer roles: tmp0 the
// read+mutated key, tmp2 the two-digit out carrier), row 103/96
// family hit-adapt (the descendant probe = the KiLL sibling-walk
// reuse; the strncmp prefix compare folds onto a String
// startsWith-equivalent byte compare over the _i prefix), rows
// 14/15 hit-reuse (the strncmp sentinel-family compare), manual-
// decision (no BASE row: the strncpy/strcpy save-restore of tmp0
// across the RETRIEVE call is the vendor guarding Mglobal's carrier
// mutation - kept verbatim as the local tmp3 snapshot the vendor
// names; tmp0 restored BEFORE the probe append per line order).
// Dafny: EXEMPT (Mglobal-carrier walk over seats; journal row).

fun sysfuncGlobalDataD(tmp0: CharArray, tmp2: CharArray, svPtr: DafMsv) {

    val tmp1 = CharArray(STR_MAX_DAF)
    val tmp3 = CharArray(STR_MAX_DAF)

    copyCarrier(tmp3, tmp0)                    // strcpy(tmp3, tmp0)
    val ii = cstrlen(tmp0)                     // _i = strlen(tmp0)

    val f = MglobalDaf(RETRIEVE_D, tmp0, tmp1, svPtr)

    copyCarrier(tmp0, tmp3)                    // strcpy(tmp0, tmp3)

    tmp0[ii] = CodedByte1D                     // tmp0[_i] = 1;
    var f1: Int
    if (!SQLITE_D) {
        // #if !(defined(SQLITE)) - the DEAD lattice arm, gated
        tmp0[ii + 1] = NUL_D                   // tmp0[_i + 1] = 0;
        f1 = MglobalDaf(NEXT_D, tmp0, tmp1, svPtr)
    } else {
        // #if defined(SQLITE) - the LIVE reference-build arm
        tmp0[ii + 1] = CodedByte1D              // tmp0[_i + 1] = 1;
        tmp0[ii + 2] = NUL_D                   // tmp0[_i + 2] = 0;
        f1 = MglobalDaf(NEXT_D, tmp0, tmp1, svPtr) // descendant?
        if (f1 == 1 && prefixEq(tmp0, tmp3, ii)) f1 = 1 else f1 = 0
        // strncmp(tmp0, tmp3, _i) == 0 prefix compare fold
    }

    // the four outcome lattices, each NUL-terminated digit pair
    if (f == 0 && f1 == 0) {          // no data; no descendant
        tmp2[0] = '0'; tmp2[1] = NUL_D
        return
    }
    if (f == 0 && f1 == 1) {          // no data; has descendant
        tmp2[0] = '1'; tmp2[1] = '0'; tmp2[2] = NUL_D
        return
    }
    if (f == 1 && f1 == 0) {          // has data; no descendant
        tmp2[0] = '1'; tmp2[1] = NUL_D
        return
    }
    // f == 1 && f1 == 1               // has data; has descendant
    tmp2[0] = '1'; tmp2[1] = '1'; tmp2[2] = NUL_D
}

private const val CodedByte1D: Char = '\u0001' // vendor literal byte 1

private fun cstrlen(buf: CharArray): Int {
    var n = 0
    while (n < buf.size && buf[n] != NUL_D) n++
    return n
}

private fun copyCarrier(dst: CharArray, src: CharArray) {
    var n = 0
    while (n < src.size && src[n] != NUL_D) { dst[n] = src[n]; n++ }
    if (n < dst.size) dst[n] = NUL_D
}

// strncmp(a, b, n) == 0 fold over the two carriers
private fun prefixEq(a: CharArray, b: CharArray, n: Int): Boolean {
    var i = 0
    while (i < n) {
        if (a[i] != b[i]) return false
        i++
    }
    return true
}
