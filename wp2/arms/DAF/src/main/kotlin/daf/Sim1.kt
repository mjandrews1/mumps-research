package daf

// Unit 204 (DAF B7 T2, core): libmpscpp.cpp::Sim1 (40 LOC,
// score 30.3): the product-moment similarity walk; the Dice
// kin of the same TU (the Dice.kt render of batch 6 is the
// walk scaffold precedent; the Sim1 divergence map is recorded
// in its KDoc ledger row). C takes 'global A, global B' BY
// VALUE and mutates B.ref in the loop (strcpy(B.ref,ctmp)+
// strcat of the row's suffix): the DAF fold models the two
// refs as local mutable Strings + a fresh MpsGlobalNode per
// rebuilt B ref (Dice.kt precedent, rows 54/68 adaptation;
// divergence ledger: the C++ value-copy aliasing of A.ref rows
// is replaced by the local String copies aRef/bRef, since
// Kotlin has no by-value object copy of the carrier).
// The trailing coded-suffix probes (ref ends with the two raw
// (char)1 bytes) truncate the copy exactly as in Dice/Sum/Min.
// getGlobalDaf seat: Sum.kt:36 (named-owner stub). get_global
// is the libmpscpp TU service; sv.tpx guards throw
// MumpsGlobalExceptionD at GLOBAL_NOT_FOUND_D (twice). XNEXT_D
// walk via MglobalDaf (Mglobal.kt:43); DafCRuntime.strtod.
// Consults (2026-11-24T16:14:00Z, nominal): BASE-log row 54
// hit-adapt (node model, fresh node per rebuilt ref), row 31
// hit-reuse (the raw (char)1 coded-suffix truncation probes of
// Sum/Min/Count family), row 40 hit-reuse (strtod fold), row 92
// hit-reuse (GlobalExceptionCodeD + MumpsGlobalExceptionD
// carrier), row 56 hit-adapt (the char* scratch folds onto
// CharArray STR_MAX_DAF carriers as in Sum.kt),
// manual-decision (no BASE row: the strncmp(A.ref,tmp,i) prefix
// break keeps the Sum.kt mismatch fold verbatim).
// Dafny: EXEMPT (global-access walk over stub seats; journal row).

fun sim1Daf(A: MpsGlobalNode, B: MpsGlobalNode, sv: DafMsv): Double {

    val ttt = CharArray(STR_MAX_DAF)
    val tmp = CharArray(STR_MAX_DAF)
    val tmp1 = CharArray(STR_MAX_DAF)
    val btmp = CharArray(STR_MAX_DAF)
    val rslt = CharArray(STR_MAX_DAF)
    var tot = 0.0 // double tot=0.0, val, val1;

    var aRef = A.ref // local mutable copies (divergence ledger)
    var bRef = B.ref

    // strcpy(ctmp, B.ref); j = strlen(B.ref)
    var ctmp = bRef
    var j = ctmp.length

    // if (ctmp[j-2]==1 && ctmp[j-1]==1) { j--; ctmp[j]=0; }
    if (j >= 2 && ctmp[j - 2] == FCN_MARK1_D && ctmp[j - 1] == FCN_MARK1_D) {
        j--
        ctmp = ctmp.substring(0, j)
    }

    // strcpy(tmp, A.ref); i = strlen(A.ref)
    var i = 0
    val ac = aRef.toCharArray()
    while (i < ac.size) { tmp[i] = ac[i]; i++ }
    if (i < tmp.size) tmp[i] = NUL_D

    // if (tmp[i-2]==1 && tmp[i-1]==1) { i--; tmp[i]=0; }
    if (i >= 2 && tmp[i - 2] == FCN_MARK1_D && tmp[i - 1] == FCN_MARK1_D) {
        i--
        tmp[i] = NUL_D
    }

    while (true) { // while(1)
        if (MglobalDaf(XNEXT_D, tmp, ttt, sv) == 0) break

        // if (strncmp(A.ref, tmp, i) != 0) break;
        var mismatch = false
        if (DafCRuntime.cstring(tmp).length < i) mismatch = true
        else {
            var k = 0
            while (k < i) {
                if (tmp[k] != (if (k < ac.size) ac[k] else NUL_D)) {
                    mismatch = true
                    break
                }
                k++
            }
        }
        if (mismatch) break

        // strcpy(B.ref,ctmp); strcat(B.ref,&tmp[i]);
        val row = DafCRuntime.cstring(tmp)
        bRef = ctmp + row.substring(i)
        val bnode = MpsGlobalNode(bRef) // fresh node (rows 54/68)

        // if (!B.Data()) continue; btmp guard copy:
        // "in case this and B are the same"
        if (!bnode.data()) continue

        // strcpy(tmp1,tmp); get_global(tmp1,rslt);
        var t = 0
        while (t < tmp.size && tmp[t] != NUL_D) { tmp1[t] = tmp[t]; t++ }
        tmp1[t] = if (t < tmp1.size) NUL_D else tmp1[tmp1.size - 1]
        getGlobalDaf(tmp1, rslt)

        if (sv.tpx == 0) { // if (!svPtr->tpx)
            GlobalExceptionCodeD = GLOBAL_NOT_FOUND_D
            throw MumpsGlobalExceptionD("global::Sim1: A-row global not found")
        }

        val v = DafCRuntime.strtod(DafCRuntime.cstring(rslt))

        // strcpy(btmp,B.ref); get_global(btmp,rslt);
        t = 0
        val bc = bnode.ref.toCharArray()
        while (t < bc.size && t < btmp.size) { btmp[t] = bc[t]; t++ }
        if (t < btmp.size) btmp[t] = NUL_D
        getGlobalDaf(btmp, rslt)

        if (sv.tpx == 0) { // if (!svPtr->tpx) (second guard)
            GlobalExceptionCodeD = GLOBAL_NOT_FOUND_D
            throw MumpsGlobalExceptionD("global::Sim1: B-row global not found")
        }

        val v1 = DafCRuntime.strtod(DafCRuntime.cstring(rslt))

        tot = tot + v * v1 // tot=tot+val*val1;
    }

    // aRef/bRef locals flow out of scope; the by-value carrier
    // divergence is recorded in the KDoc ledger above.
    return tot
}
