package daf

/*
 * DAF batch-9 unit 263 (roster row 263; libmpscpp.cpp::global::Avg,
 * core, LOC 28, score 11.2). Consult 2026-11-24T22:41:00Z.
 *
 * SEAT RELEASE: the global-class Avg member raised in
 * GlobalNodeStub.kt (the "not a corpus unit / permanent stub"
 * TAG CORRECTION: the chunker's row 263 owns it) renders here;
 * GlobalNodeStub.avg now delegates to globalAvgD. The unit-130
 * free wrapper AvgD (Avg.kt) rides unchanged through
 * avgOfNode -> node.avg(sub).
 *
 * Vendor shape kept: prefix the member ref (the node ref IS the
 * base ref per the row-54 node model; the sub argument is the
 * ctor/invoke tail the unit-130 wrapper already bound, so the
 * walk ref is the bare ref of the receiver), the trailing
 * two-bytes-under-32 trim (tmp[i-1]<32 && tmp[i-2]<32 ->
 * tmp[--i]=0; the vendor reads i-1/i-2 with no i>=2 guard - the
 * render guards, OOB-read divergence ledgered, the Dice
 * precedent), then the XNEXT sibling walk of the Dice/Sim1
 * skeleton: ttt <- Mglobal(XNEXT,tmp,ttt,svPtr); break when the
 * next key leaves the ref prefix (strncmp(ref,tmp,i)); each hit
 * get_global -> the tpx==0 GLOBAL_NOT_FOUND exception arm ->
 * sscanf("%lf") fold strtod with the INVALID_CONVERSION arm (both
 * throws via MumpsGlobalExceptionD, the Dice row-40 precedent);
 * tot/j with the j==0 -> 0.0 early return kept.
 * sv binding: DafInterpState.svPtr per the LibmpscppMisc9
 * wrapper rule (the vendor member reads the TU svPtr).
 * H-015 hygiene: no raw dollars ride the comments.
 * Dafny: EXEMPT (global-walk service member over Mglobal seat;
 * journal row).
 */

fun globalAvgD(n: MpsGlobalNode): Double {
    val svPtr = DafInterpState.svPtr

    // strcpy(tmp,ref); i=strlen(ref);
    var tmp = CharArray(STR_MAX_DAF)
    var i = 0
    while (i < n.ref.length) {
        tmp[i] = n.ref[i]
        i++
    }
    tmp[i] = NUL_D

    // if (tmp[i-1]<32&&tmp[i-2]<32) tmp[--i]=0;  (guarded trim)
    if (i >= 2 && tmp[i - 1] < ' ' && tmp[i - 2] < ' ') {
        i--
        tmp[i] = NUL_D
    }

    var j = 0L
    var tot = 0.0
    val ttt = CharArray(STR_MAX_DAF)
    val tmp1 = CharArray(STR_MAX_DAF)
    val rslt = CharArray(STR_MAX_DAF)

    while (true) { // while(1)
        if (MglobalDaf(XNEXT_D, tmp, ttt, svPtr) == 0) break
        // if (strncmp(ref,tmp,i)!=0) break;
        val s = DafCRuntime.cstring(tmp)
        var mismatch = false
        if (s.length < i) mismatch = true
        else {
            var k = 0
            while (k < i) {
                if (tmp[k] != n.ref[k]) {
                    mismatch = true
                    break
                }
                k++
            }
        }
        if (mismatch) break

        // strcpy(tmp1,tmp); get_global(tmp1,rslt);
        var t = 0
        while (at(tmp, t) != NUL_D) {
            tmp1[t] = tmp[t]
            t++
        }
        tmp1[t] = NUL_D
        getGlobalDaf(tmp1, rslt)

        if (svPtr.tpx == 0) { // if (!svPtr->tpx)
            GlobalExceptionCodeD = GLOBAL_NOT_FOUND_D
            throw MumpsGlobalExceptionD("Avg: global not found")
        }

        // if (sscanf(rslt,"%lf",&val)==0) - the sscanf fold: the
        // vendor counts 0 on total match-failure; the render
        // probes the leading numeric prefix (the Dice row-33
        // full-scan note), then strtod reads the value.
        val rs = DafCRuntime.cstring(rslt)
        if (!avgScanD(rs)) {
            GlobalExceptionCodeD = INVALID_CONVERSION_D
            throw MumpsGlobalExceptionD("Avg: invalid conversion")
        }
        val v = DafCRuntime.strtod(rs)
        tot += v
        j++
    }

    if (j == 0L) return 0.0
    return tot / j
}

// sscanf("%lf") match probe: skip blanks, optional sign, then at
// least one digit (the strtod carrier of DafCRuntime parses the
// same prefix shape).
private fun avgScanD(s: String): Boolean {
    var i = 0
    while (i < s.length && (s[i] == ' ' || s[i] == '\u0009')) i++
    if (i < s.length && (s[i] == '+' || s[i] == '-')) i++
    return i < s.length && s[i] in '0'..'9'
}
