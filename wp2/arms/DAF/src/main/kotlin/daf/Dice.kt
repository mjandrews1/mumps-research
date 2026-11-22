package daf

// Unit 175 (DAF B6 T2, core): libmpscpp.cpp::Dice
// (units.csv row 175: loc 44, score 22.4, rank 219, core): the
// Dice coefficient of two sibling-subscripted globals: the coded-1
// pair trims at both refs, the XNEXT walk over the A-side keys,
// the per-key fetches of the A row value and the B row value with
// the tpx retrieve-then-throw guards, and the 2*tot/val similarity
// result. Fifth member of the node-walk family after Count, Min,
// Avg and Sum: this render RELEASES the diceD named-owner stub of
// LibmpscppMisc9.kt (the b6 wrapper rank row; the wrapper call at
// LibmpscppMisc9.kt keeps its bsv globalASeat carriers).
// Consults (2026-11-22T16:05:00Z, nominal): rows 92/103/40/91
// family hit-reuse (the XNEXT sibling-walk skeleton reused from
// the Sum/Min family: MglobalDaf is the REAL dispatcher released
// this tranche by the unit-167 Mglobal.kt render, so Dice rides
// the released seat - the stub-release chain ledger), row 92
// hit-reuse (both fetches guard with the tpx check and throw
// MumpsGlobalExceptionD at GLOBAL_NOT_FOUND_D), row 40 hit-reuse
// (each rslt carrier parses with strtod), row 54/68 hit-adapt
// (the C++ reference args A/B pass by value-but-ref-mutating: the
// vendor body REWRITES A.ref/B.ref buffers through the coded-1
// trims and the B.ref rebuild; MpsGlobalNode.ref is a val String,
// so the refs fold to local String rebinds and a fresh
// MpsGlobalNode per B-side fetch - the immutable-node divergence
// ledgered), row 111-adapt (the coded-1 pair checks read ctmp[j-2]
// ==1 and ctmp[j-1]==1 as the raw \u0001 FCN_MARK1 bytes, NOT the
// Sum-family <32 control-char trim - a DIFFERENT trim lattice;
// read as Char == FCN_MARK1_D at the CharArray carriers).
// VENDOR QUIRKS kept: (1) the coded-1 trim checks j-2/j-1 with NO
// j>=2 guard (a short single-subscript ref reads index -1; the
// render guards i>=2/j>=2 and ledger diverges the OOB read);
// (2) B.Data() is the query member returning nonzero when data
// exists - rendered as the path-gated MpsGlobalNode.data() stub
// (its NotImplementedError only fires on the live-data path);
// (3) the final 2*tot/val divides AFTER the zero check on SUM of
// the abs accumulators (val==0.0 first tested) so no zero divide
// reaches the result.
// Dafny: EXEMPT (global-access walk over stub seats; journal row).

// (the kept two-arg wrapper signature; the svPtr singleton rides
// DafInterpState.svPtr per the LibmpscppMisc9 wrapper rule.)
fun diceD(a: MpsGlobalNode, b: MpsGlobalNode): Double {
    val svPtr = DafInterpState.svPtr

    val ttt = CharArray(STR_MAX_DAF)
    val tmp = CharArray(STR_MAX_DAF)
    val rslt = CharArray(STR_MAX_DAF)
    var totD = 0.0
    var totQ = 0.0
    var tot = 0.0

    // strcpy(tmp, A.ref); i=strlen(A.ref); strlen(B.ref)
    val aref = a.ref.toCharArray()
    val bref0 = b.ref
    var i = 0
    while (i < aref.size) {
        tmp[i] = aref[i]
        i++
    }
    tmp[i] = NUL_D
    i = DafCRuntime.cstring(tmp).length // i = strlen(A.ref)
    var j = bref0.length // j = strlen(B.ref)

    // char ctmp[STR_MAX]: the B-side carrier copy of B.ref
    // (fold: the local owns the trimmed B ref as a String).
    var ctmp = bref0

    // if (ctmp[j-2]==1&&ctmp[j-1]==1) { j--; ctmp[j]=0; }
    // (the coded-1 pair trim; NO j>=2 guard at the vendor - the
    // render guards, the OOB-read divergence ledgered.)
    if (j >= 2 && ctmp[j - 2] == FCN_MARK1_D && ctmp[j - 1] == FCN_MARK1_D) {
        ctmp = ctmp.substring(0, j - 1)
        j--
    }

    // if (tmp[i-2]==1&&tmp[i-1]==1) { i--; tmp[i]=0; }
    if (i >= 2 && tmp[i - 2] == FCN_MARK1_D && tmp[i - 1] == FCN_MARK1_D) {
        i--
        tmp[i] = NUL_D
    }

    while (true) { // while(1)
        if (MglobalDaf(XNEXT_D, tmp, ttt, svPtr) == 0) break
        // if (!Mglobal(XNEXT,tmp,ttt,svPtr)) break; (the released
        // unit-167 dispatcher seat)

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

        if (svPtr.tpx == 0) { // if (!svPtr->tpx)
            GlobalExceptionCodeD = GLOBAL_NOT_FOUND_D
            throw MumpsGlobalExceptionD(
                "Dice: A-row global not found")
        }

        // val=strtod(rslt,NULL); totD=totD+val;
        val v = DafCRuntime.strtod(DafCRuntime.cstring(rslt))
        totD += v

        // strcpy(B.ref,ctmp); strcat(B.ref,&tmp[i]);
        // (the val-ref fold: a fresh MpsGlobalNode on the rebuilt
        // B ref, row 54/68 adaptation.)
        val bnode = MpsGlobalNode(ctmp + DafCRuntime.cstring(tmp).substring(i))

        // if (!B.Data()) continue; (the query member is the
        // path-gated data() stub.)
        if (!bnode.data()) continue

        val btmp = CharArray(STR_MAX_DAF)
        val bs = bnode.ref.toCharArray()
        t = 0
        while (t < bs.size) {
            btmp[t] = bs[t]
            t++
        }
        btmp[t] = NUL_D
        getGlobalDaf(btmp, rslt)

        if (svPtr.tpx == 0) { // if (!svPtr->tpx) (second guard)
            GlobalExceptionCodeD = GLOBAL_NOT_FOUND_D
            throw MumpsGlobalExceptionD(
                "Dice: B-row global not found")
        }

        // val1=strtod(rslt,NULL); totQ=totQ+val1;
        // tot=tot+val*val1;
        val v1 = DafCRuntime.strtod(DafCRuntime.cstring(rslt))
        totQ += v1
        tot += v * v1
    }

    // val=totD+totQ; if (val==0.0) return 0.0;
    val valSum = totD + totQ
    if (valSum == 0.0) return 0.0

    // return 2*tot/val;
    return 2 * tot / valSum
}
