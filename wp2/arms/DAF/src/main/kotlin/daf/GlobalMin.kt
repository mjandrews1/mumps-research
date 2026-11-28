package daf

// R1 stub-completion (2026-11-28, C-020 erratum session, tranche 5):
// libmpscpp.cpp::global::Min (roster row 51, loc 26 per the
// units.csv member metrics, complex): the class member that
// mins the sibling subscript values of this global: the XNEXT
// walk with the ref-prefix guard, the tpx fetch with the
// GLOBAL_NOT_FOUND throw, the flg-first-value pattern, the
// val<min keep. The GlobalMax.kt unit-171 sibling render is
// the row-103 skeleton precedent (the walk is identical apart
// from the fold body - the Sum/Max family discipline).
// Consults (2026-11-28T00:00:00Z, nominal): BASE-log row 103
// hit-reuse (the XNEXT sibling-walk skeleton reused from the
// unit-143 Sum render: tmp dual-role carrier, the Mglobal==0
// break, the prefix-mismatch break); row 92 hit-reuse (the tpx
// retrieve-then-throw: GlobalExceptionCodeD = GLOBAL_NOT_FOUND_D
// then MumpsGlobalExceptionD); row 40 hit-reuse (strtod over
// the rslt carrier); row 91 family hit-reuse (the MglobalDaf
// out-param buffers ride CharArrays; MglobalDaf is the real
// dispatcher); rows 54/68 hit-reuse (the receiver is the
// MpsGlobalNode ref carrier); the sv binding DafInterpState
// .svPtr per the LibmpscppGlobalAvg member rule (the vendor
// member reads the TU svPtr).
// Vendor quirks kept: (1) the trailing <32 pair trim before
// the walk (the CONTROL-CHAR pair trim of the Sum family, NOT
// the coded-1 pair of the bsv wrappers); (2) the
// flg-first-value pattern (the first fetched value becomes min
// unconditional - an all-positive subtree still mins correctly;
// an empty subtree returns the vendor 0.0 initial, kept); (3)
// the Min.kt unit-99 free wrapper reaches THIS member through
// the raised a("") invoke - the member release keeps that
// call-shape (the invoke family owner unchanged).
// Dafny: EXEMPT (global-access walk over backend seats; the
// Max-family precedent; journal row).

fun globalMinD(node: MpsGlobalNode): Double {

    val svPtr = DafInterpState.svPtr // the vendor TU svPtr

    val ttt = CharArray(STR_MAX_DAF)
    val tmp = CharArray(STR_MAX_DAF)
    val tmp1 = CharArray(STR_MAX_DAF)
    val rslt = CharArray(STR_MAX_DAF)
    var min = 0.0 // double min=0.0, val
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

    // if (tmp[i-1]<32 && tmp[i-2]<32) tmp[--i]=0;
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
                "global::Min: symbol/global not found")
        }

        // val = strtod(rslt,NULL);
        val v = DafCRuntime.strtod(DafCRuntime.cstring(rslt))

        // if (flg) { min=val; flg=0; continue; }
        if (flg) {
            min = v
            flg = false
            continue
        }

        // if (val < min) min = val;
        if (v < min) min = v
    }

    return min
}
