package mumps.interp

import mumps.mdh.keyfix

/**
 * PRO-arm translation of interp-sysfunc.cpp::Merge
 * (batch-8 unit 226, units.csv data line 226, rank 206).
 * Translated independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), reference
 * source lines 650-729 (the function head is at vendor 650).
 *
 * Identity verified against the study tool (tools/select_study_
 * subset.py): interp-sysfunc.cpp::Merge, cpp, loc 59,
 * comment_ratio 0.017, pointer_marks 0, control_marks 15,
 * score 30.1, rank 206, batch 8 - exact match to
 * wp1/study-subset/units.csv line 226.
 *
 * Merge(left, right): the MERGE command runtime - copy the top
 * reference, then walk the right global's siblings with XNEXT
 * (the key buffer is in/out: Mglobal writes each next key back
 * into it) and STORE each retrieved value under the merged
 * left prefix.
 *
 * This unit RESOLVES the InterpStubs Merge() h014 stub raised
 * at u155 (h014 out_of_scope_call; the stub lives in this
 * file's slot above).  The misc-7 global::Merge member (u218)
 * and the MERGE command arm (u155) call sites bind unchanged.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h007 char_buffer : tmp1..tmp4[STR_MAX] scratch rebuilt per
 *    call; the FIX macro is a plain (char *) cast (vendor
 *    interp-sysfunc.cpp:31) - it disappears at the seat.
 *  - h012 unsigned_byte_domain: key buffers are Char(0..255)
 *    domains; the >127 sentinel scan reads raw bytes.
 *  - h014 out_of_scope_call: Mglobal (global-array machinery
 *    stub, RETRIEVE=0/STORE=1/XNEXT=8, defines.h:83-88) and
 *    keyfix (mdh stub) reused unmodified.
 *  - h015 grammar_collision: top-level Merge on the interp side
 *    (stub removed) - no collisions.
 *  - h017 naming      : Merge keeps the vendor name; the
 *    signature keeps the u155 stub shape (offset params).
 *  - h018 preprocessor_conditional: the MDEBUG arms are
 *    EXCLUDED (undefined on the reference build); the
 *    "#if defined(SQLITE) #endif" arm is EMPTY in the vendor
 *    (kept as note - live SQLITE build changes nothing here).
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The >127 sentinel scan starts at strlen-1 and stops at
 *    j==0 exclusive; when no sentinel is found the else arm
 *    appends SOH (1) and re-points j at the NEW length - kept.
 *  - The XNEXT key buffer is in/out (Mglobal writes the next
 *    sibling key back into it) - the rbuf local mirrors the
 *    vendor's in-place mutation of right (adaptation, the
 *    caller's buffer is not aliased).
 *  - The strcpy(tmp4,tmp3)/STORE/restore dance around both
 *    STORE calls is verbatim (tmp4 is the save slot).
 *  - strncmp(right,tmp2,j) is checked AFTER the tmp3[k]
 *    append but governs only the break - kept.
 */

private const val STR_MAX = 32000 // vendor strmax.h:3 (file-private, cf. unit 22)
private const val RETRIEVE = 0 // vendor defines.h:83
private const val STORE = 1 // vendor defines.h:84
private const val XNEXT = 8 // vendor defines.h:87

/** strcpy(dst+dstOff, src+srcOff) incl. the NUL (Cannon strshift precedent). */
private fun strshiftAt(dst: CharArray, dstOff: Int, src: CharArray, srcOff: Int) {
    var d = dstOff
    var s = srcOff
    while (src[s] != NUL) {
        dst[d] = src[s]
        d++
        s++
    }
    dst[d] = NUL
}

/** strncmp(a+0, b+0, n): 0 when the first n bytes match. */
private fun strncmpC(a: CharArray, b: CharArray, n: Int): Int {
    for (x in 0 until n) {
        val ca = a[x]
        val cb = b[x]
        if (ca != cb) return if (ca > cb) 1 else -1
        if (ca == NUL) return 0
    }
    return 0
}

fun Merge(left: CharArray, leftOff: Int, right: CharArray, rightOff: Int,
          svPtr: Msv): Int {
    val tmp1 = CharArray(STR_MAX)
    val tmp2 = CharArray(STR_MAX)
    val tmp3 = CharArray(STR_MAX)
    val tmp4 = CharArray(STR_MAX)

    // h018: MDEBUG arms EXCLUDED (reference build); the SQLITE
    // arm is empty in the vendor - nothing emitted.

    writeCstring(tmp2, cstringAt(right, rightOff)) // vendor: strcpy(tmp2,right);
    writeCstring(tmp3, cstringAt(left, leftOff)) // vendor: strcpy(tmp3,left);

    // vendor: i = Mglobal(RETRIEVE, tmp2, tmp1, svPtr);
    //         if (i != 0) Mglobal(STORE, tmp3, tmp1, svPtr);
    val iTop = mglobal(RETRIEVE, tmp2, tmp1, svPtr) // copy top refs
    if (iTop != 0) mglobal(STORE, tmp3, tmp1, svPtr)

    // vendor: strcpy(tmp2,right); i=strlen(tmp2);
    //         for (j=i-1; j>0; j--) if (tmp2[j]>127) break;
    //         if (j!=0) tmp2[j+1]='\0';
    //         else { tmp2[i++]=1; tmp2[i]=0; j=i; }
    writeCstring(tmp2, cstringAt(right, rightOff))
    var i = strlenC(tmp2)
    var j = i - 1
    while (j > 0) {
        if (tmp2[j] > '\u007F') break // vendor: >127 sentinel
        j--
    }
    if (j != 0) {
        tmp2[j + 1] = NUL
    } else {
        tmp2[i] = 1.toChar()
        i++
        tmp2[i] = NUL
        j = i
    }
    keyfix(tmp2)
    j = strlenC(tmp2)

    // vendor: strcpy(tmp3,left); ... same scan -> k ...
    writeCstring(tmp3, cstringAt(left, leftOff))
    var i3 = strlenC(tmp3)
    var k = i3 - 1
    while (k > 0) {
        if (tmp3[k] > '\u007F') break
        k--
    }
    if (k != 0) {
        tmp3[k + 1] = NUL
    } else {
        tmp3[i3] = 1.toChar()
        i3++
        tmp3[i3] = NUL
        k = i3
    }
    keyfix(tmp3)
    k = strlenC(tmp3)

    // h018: MDEBUG arm EXCLUDED.

    // vendor: if (Mglobal(RETRIEVE,right,tmp1,svPtr) != 0) {
    //   handle first case - may not exist
    //   strcpy(tmp4,tmp3); Mglobal(STORE,tmp3,tmp1,svPtr);
    //   strcpy(tmp3,tmp4); }
    // rbuf mirrors the vendor's in/out `right` (adaptation).
    val rbuf = CharArray(STR_MAX)
    writeCstring(rbuf, cstringAt(right, rightOff))
    if (mglobal(RETRIEVE, rbuf, tmp1, svPtr) != 0) {
        writeCstring(tmp4, cstringAt(tmp3))
        mglobal(STORE, tmp3, tmp1, svPtr)
        writeCstring(tmp3, cstringAt(tmp4))
    }

    while (true) {
        // vendor: if (Mglobal(XNEXT,right,tmp1,svPtr)==0) break;
        if (mglobal(XNEXT, rbuf, tmp1, svPtr) == 0) break

        // vendor: strcpy(FIX &tmp3[k], FIX &right[j]);  [right mutated]
        strshiftAt(tmp3, k, rbuf, j)

        // vendor: if (strncmp(right,tmp2,j)!=0) break;
        if (strncmpC(rbuf, tmp2, j) != 0) break

        // vendor: Mglobal(RETRIEVE,right,tmp1,svPtr);
        mglobal(RETRIEVE, rbuf, tmp1, svPtr)

        // vendor: strcpy(tmp4,tmp3); Mglobal(STORE,tmp3,tmp1,svPtr);
        //         strcpy(tmp3,tmp4);
        writeCstring(tmp4, cstringAt(tmp3))
        mglobal(STORE, tmp3, tmp1, svPtr)
        writeCstring(tmp3, cstringAt(tmp4))
    }

    return 0
}
