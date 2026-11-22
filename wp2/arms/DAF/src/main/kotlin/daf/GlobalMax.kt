package daf

// Unit 171 (DAF B6 T2, core): libmpscpp.cpp::global::Max
// (26 LOC, score 14.8, rank 144): the class member that maxes the
// sibling subscript values of this global: the XNEXT walk with the
// ref-prefix guard, the tpx fetch with the GLOBAL_NOT_FOUND throw,
// the flg-first-value pattern, the val>max keep.
// Consults (2026-11-22T16:03:00Z, nominal): BASE-log row 103
// hit-reuse (the XNEXT sibling-walk skeleton reused from the
// unit-143 Sum render (the sibling of this member; the walk is
// identical apart from the fold body): tmp dual-role carrier, the
// Mglobal==0 break, the prefix-mismatch break - the Sum/Min
// precedents), row 92 hit-reuse (the tpx retrieve-then-throw:
// GlobalExceptionCodeD = GLOBAL_NOT_FOUND_D then
// MumpsGlobalExceptionD), row 40 hit-reuse (strtod over the rslt
// carrier), row 91 family hit-reuse (the MglobalDaf out-param
// buffers ride CharArrays; MglobalDaf is the REAL dispatcher of the
// unit-167 render this tranche), rows 54/68 hit-reuse (the receiver
// is the MpsGlobalNode ref carrier; the walk matches Sum exactly).
// Vendor quirk kept: the trailing <32 pair trim before the walk
// (tmp[i-1] < 32 && tmp[i-2] < 32 - the CONTROL-CHAR pair trim of
// the Sum family, NOT the coded-1 pair of the bsv wrappers) and the
// flg-first-value pattern (the first fetched value becomes max
// unconditional, all later compare - an all-negative subtree still
// maxes correctly; an empty subtree returns the vendor 0.0
// initial - kept: the empty-walk return is 0.0 exactly).
// Dafny: EXEMPT (global-access walk over backend seats; journal
// row).

fun globalMaxD(node: MpsGlobalNode, svPtr: DafMsv): Double {

    val ttt = CharArray(STR_MAX_DAF)
    val tmp = CharArray(STR_MAX_DAF)
    val tmp1 = CharArray(STR_MAX_DAF)
    val rslt = CharArray(STR_MAX_DAF)
    var max = 0.0 // double max=0.0, val
    var flg = true // int flg=1

    val ref = node.ref.toCharArray()
    // strcpy(tmp, ref)
    var i = 0
    while (i < ref.size) {
        tmp[i] = ref[i]
        i++
    }
    tmp[i] = NUL_D
    // i = strlen(tmp)
    i = DafCRuntime.cstring(tmp).length

    // if (tmp[i-1] < 32 && tmp[i-2] < 32) tmp[--i] = 0;
    if (i >= 2 && tmp[i - 1] < ' ' && tmp[i - 2] < ' ') {
        i--
        tmp[i] = NUL_D
    }

    while (true) { // while(1)

        // if (Mglobal(XNEXT,tmp,ttt,svPtr) == 0) break;
        if (MglobalDaf(XNEXT_D, tmp, ttt, svPtr) == 0) break

        // if (strncmp(ref,tmp,i) != 0) break;
        var mismatch = false
        if (DafCRuntime.cstring(tmp).length < i) mismatch = true
        else {
            var k = 0
            while (k < i) {
                if (tmp[k] != (if (k < ref.size) ref[k] else NUL_D)) {
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

        // if (!svPtr->tpx) { throw }
        if (svPtr.tpx == 0) {
            GlobalExceptionCodeD = GLOBAL_NOT_FOUND_D
            throw MumpsGlobalExceptionD(
                "global::Max: symbol/global not found")
        }

        // val = strtod(rslt,NULL);
        val v = DafCRuntime.strtod(DafCRuntime.cstring(rslt))

        // if (flg) { max=val; flg=0; continue; }
        if (flg) {
            max = v
            flg = false
            continue
        }

        // if (val > max) max = val;
        if (v > max) max = v
    }

    return max
}
