package daf

/*
 * DAF batch-9 unit 269 (roster row 269; libmpscpp.cpp::Jaccard,
 * core, LOC 58, score 11.2). Consult 2026-11-24T22:46:00Z.
 *
 * SEAT RELEASE: the jaccardD named stub raised at
 * LibmpscppMisc9.kt:334 is deleted with this release note; the
 * real body lands at the same (a, b) signature the JaccardW
 * wrapper binds.
 *
 * Vendor shape kept (the unit-175 Dice sibling + the B-side
 * second walk): the coded-1 pair trims at both refs (the
 * FCN_MARK1 lattice, NO guard at the vendor - rendered guarded,
 * the Dice OOB-read divergence ledger), strcpy(OriginalB,ctmp)
 * the B-ref restore carrier (the val String fold: OriginalB is
 * the pre-walk trimmed B ref, restored before the second walk),
 * the FIRST XNEXT walk over the A-side prefix with the per-key
 * A fetch (totD accumulator) and the B-row fetch of the A-sibling
 * key (tot += val*val1) riding the B.Data() path-gated
 * data() seat (Dice quirk 2 precedent), then strcpy(B.ref,
 * OriginalB) and the SECOND XNEXT walk over the B-side prefix
 * (ctmp the walking key, strncmp(B.ref,ctmp,j) the prefix bound)
 * with the per-key B fetch (totQ accumulator), and the Jaccard
 * result tot/(totD+totQ-tot) with the val==0.0 early return
 * (Dice quirk 3 precedent). All three fetches guard the tpx
 * retrieve-then-throw MumpsGlobalExceptionD at GLOBAL_NOT_FOUND_D
 * (row 92 fold). svPtr: DafInterpState.svPtr per the wrapper rule.
 * The immutable-MpsGlobalNode ref fold: B.ref rewrites render as
 * local String rebinds (row 54/68, Dice precedent).
 * H-015 hygiene: no raw dollars ride the comments.
 * Dafny: EXEMPT (global-access walk over Mglobal seat; journal
 * row).
 */

fun jaccardD(a: MpsGlobalNode, b: MpsGlobalNode): Double {
    val svPtr = DafInterpState.svPtr

    val ttt = CharArray(STR_MAX_DAF)
    val tmp = CharArray(STR_MAX_DAF)
    val rslt = CharArray(STR_MAX_DAF)
    var totD = 0.0
    var totQ = 0.0
    var tot = 0.0

    // strcpy(tmp, A.ref); i=strlen(A.ref);
    val aref = a.ref.toCharArray()
    val bref0 = b.ref
    var i = 0
    while (i < aref.size) {
        tmp[i] = aref[i]
        i++
    }
    tmp[i] = NUL_D
    i = DafCRuntime.cstring(tmp).length
    var j = bref0.length

    var ctmp = bref0
    // if (ctmp[j-2]==1&&ctmp[j-1]==1) { j--; ctmp[j]=0; }
    if (j >= 2 && ctmp[j - 2] == FCN_MARK1_D && ctmp[j - 1] == FCN_MARK1_D) {
        ctmp = ctmp.substring(0, j - 1)
        j--
    }

    // strcpy(OriginalB,ctmp);
    val originalB = ctmp

    // if (tmp[i-2]==1&&tmp[i-1]==1) { i--; tmp[i]=0; }
    if (i >= 2 && tmp[i - 2] == FCN_MARK1_D && tmp[i - 1] == FCN_MARK1_D) {
        i--
        tmp[i] = NUL_D
    }

    // ---- first walk: the A-side prefix ----
    while (true) { // while(1)
        if (MglobalDaf(XNEXT_D, tmp, ttt, svPtr) == 0) break

        // if (strncmp(A.ref,tmp,i)!=0) break;
        var mismatch = false
        if (DafCRuntime.cstring(tmp).length < i) mismatch = true
        else {
            var k = 0
            while (k < i) {
                if (tmp[k] != (if (k < aref.size) aref[k] else NUL_D)) {
                    mismatch = true
                    break
                }
                k++
            }
        }
        if (mismatch) break

        // strcpy(tmp1,tmp); get_global(tmp1,rslt);
        val tmp1 = CharArray(STR_MAX_DAF)
        var t = 0
        while (at(tmp, t) != NUL_D) {
            tmp1[t] = tmp[t]
            t++
        }
        tmp1[t] = NUL_D
        getGlobalDaf(tmp1, rslt)

        if (svPtr.tpx == 0) {
            GlobalExceptionCodeD = GLOBAL_NOT_FOUND_D
            throw MumpsGlobalExceptionD("Jaccard: A-row global not found")
        }

        val v = DafCRuntime.strtod(DafCRuntime.cstring(rslt)) // A(j,k)
        totD += v

        // strcpy(B.ref,ctmp); strcat(B.ref,&tmp[i]);
        val bnode = MpsGlobalNode(ctmp + DafCRuntime.cstring(tmp).substring(i))

        if (!bnode.data()) continue // if (!B.Data()) continue;

        val btmp = CharArray(STR_MAX_DAF)
        val bs = bnode.ref.toCharArray()
        t = 0
        while (t < bs.size) {
            btmp[t] = bs[t]
            t++
        }
        btmp[t] = NUL_D
        getGlobalDaf(btmp, rslt)

        if (svPtr.tpx == 0) {
            GlobalExceptionCodeD = GLOBAL_NOT_FOUND_D
            throw MumpsGlobalExceptionD("Jaccard: B-row global not found")
        }

        val v1 = DafCRuntime.strtod(DafCRuntime.cstring(rslt)) // B(j,k)
        tot += v * v1
    }

    // strcpy(B.ref,OriginalB);
    var bref = originalB

    // ---- second walk: the B-side prefix ----
    val ctmpArr = CharArray(STR_MAX_DAF)
    var c2 = 0
    while (c2 < ctmp.length) {
        ctmpArr[c2] = ctmp[c2]
        c2++
    }
    ctmpArr[c2] = NUL_D

    while (true) { // while(1)
        if (MglobalDaf(XNEXT_D, ctmpArr, ttt, svPtr) == 0) break

        // if (strncmp(B.ref,ctmp,j)!=0) break;
        var mismatch = false
        if (DafCRuntime.cstring(ctmpArr).length < j) mismatch = true
        else {
            var k = 0
            while (k < j) {
                if (ctmpArr[k] != (if (k < bref.length) bref[k] else NUL_D)) {
                    mismatch = true
                    break
                }
                k++
            }
        }
        if (mismatch) break

        // strcpy(tmp1,ctmp); get_global(tmp1,rslt);
        val tmp1 = CharArray(STR_MAX_DAF)
        var t = 0
        while (at(ctmpArr, t) != NUL_D) {
            tmp1[t] = ctmpArr[t]
            t++
        }
        tmp1[t] = NUL_D
        getGlobalDaf(tmp1, rslt)

        if (svPtr.tpx == 0) {
            GlobalExceptionCodeD = GLOBAL_NOT_FOUND_D
            throw MumpsGlobalExceptionD("Jaccard: B-second-walk global not found")
        }

        val v1 = DafCRuntime.strtod(DafCRuntime.cstring(rslt))
        totQ += v1
    }

    // val=totD+totQ-tot; if (val==0.0) return 0.0;
    val denom = totD + totQ - tot
    if (denom == 0.0) return 0.0

    return tot / denom
}
