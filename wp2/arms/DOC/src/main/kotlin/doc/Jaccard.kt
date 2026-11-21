package doc

// Unit 276 (B9 tranche 3): libmpscpp.cpp::Jaccard (batch 9,
// complex stratum; units.csv row 256: loc 58, score 24.9, rank
// 340; physLOC 58 physically verified this session) - the
// Jaccard similarity double-walk over two globals (the Sim1
// single-walk motor: tot over the A-walk minus the shared
// product, normalized by totD + totQ - tot). Consult H-015
// hit-adapt (2026-11-22T05:38:00Z): the two walks ride the
// Sim1.kt template seats (Mglobal(XNEXT) resolved delegate,
// get_global two-buffer member stub with the vendor tpx checks
// kept AFTER each call per the stub contract, globalData
// unsubscripted probe, strtod the internal MpsExp.kt unit-73
// render).
//
// PASS-BY-VALUE LEDGER (the Sim1 hit-adapt core): the vendor
// takes global A, global B BY VALUE and rebuilds B.ref
// mid-walk (strcpy(B.ref,ctmp); strcat(B.ref,&tmp[i]);) - the
// writes live on the C value copy and never escape. The arm
// GlobalRef carrier is shared: the fold renders the value-copy
// semantics on the LOCAL CharArray bRefLocal, the B.Data()
// probe binds through a fresh GlobalRef over that local
// carrier, and the caller's B object is never clobbered
// (observable behavior identical to the C value copy). The two
// trailing-0x01 root-marker trims kept verbatim (the 1,1
// suffix of a bare global name); B.ref is restored to the
// trimmed OriginalB between the walks (the vendor
// strcpy(B.ref,OriginalB) - same local-carrier fold, restored
// into ctmp per the bRefLocal twin); the GLOBAL_NOT_FOUND
// throws route through the Globals carriers. Naming per
// H-017/H-016 (Stubs.jaccard seat FLIPS to this render; the
// unit-120 wrapper call sites bind unchanged).

fun Jaccard(a: GlobalRef, b: GlobalRef): Double {
    val ttt = CharArray(STR_MAX)
    val ctmp = CharArray(STR_MAX)
    val btmp = CharArray(STR_MAX)
    val tmp = CharArray(STR_MAX)
    val tmp1 = CharArray(STR_MAX)
    val rslt = CharArray(STR_MAX)
    val originalB = CharArray(STR_MAX)
    var i: Int
    var j: Int
    var totD = 0.0
    var totQ = 0.0
    var tot = 0.0
    val bRefLocal = CharArray(STR_MAX)

    // strcpy(tmp, A.ref); i=strlen(A.ref);
    CstringOps.strcpyS(tmp, a.ref)
    i = a.ref.length

    // j=strlen(B.ref); strcpy(ctmp,B.ref);
    j = b.ref.length
    CstringOps.strcpyS(ctmp, b.ref)

    // if (ctmp[j-2]==1&&ctmp[j-1]==1) { j--; ctmp[j]=0; }
    if (j >= 2 && ctmp[j - 2] == CHAR_1 && ctmp[j - 1] == CHAR_1) {
        j--
        ctmp[j] = NUL
    }

    // strcpy(OriginalB,ctmp);
    CstringOps.strcpyC(originalB, ctmp)

    // if (tmp[i-2]==1&&tmp[i-1]==1) { i--; tmp[i]=0; }
    if (i >= 2 && tmp[i - 2] == CHAR_1 && tmp[i - 1] == CHAR_1) {
        i--
        tmp[i] = NUL
    }

    // A-walk: totD = sum of A(j,k); tot = sum of A*B products
    while (true) {
        if (Stubs.Mglobal(XNEXT, tmp, ttt, Globals.svPtr) == 0) break

        if (CstringOps.strncmpS(a.ref, tmp, i) != 0) break

        // strcpy(tmp1,tmp); get_global(tmp1,rslt);
        CstringOps.strcpyC(tmp1, tmp)
        Stubs.get_global(tmp1, rslt)

        if (Globals.svPtr.tpx == 0L) {
            Globals.GlobalExceptionCode = GLOBAL_NOT_FOUND
            throw MumpsGlobalException()
        }

        // val=strtod(rslt,NULL); totD=totD+val;
        val vall = strtod(CstringOps.cstring(rslt))
        totD = totD + vall

        // strcpy(B.ref,ctmp); strcat(B.ref,&tmp[i]);
        // strcpy(btmp,B.ref); - the by-value local fold
        CstringOps.strcpyC(bRefLocal, ctmp)
        CstringOps.catAtS(bRefLocal, 0, CstringOps.cstringAt(tmp, i))
        CstringOps.strcpyC(btmp, bRefLocal)

        // if (!B.Data()) continue;
        if (!Stubs.globalData(GlobalRef(CstringOps.cstring(bRefLocal)), "")) continue

        Stubs.get_global(btmp, rslt)

        if (Globals.svPtr.tpx == 0L) {
            Globals.GlobalExceptionCode = GLOBAL_NOT_FOUND
            throw MumpsGlobalException()
        }

        val val1 = strtod(CstringOps.cstring(rslt))

        tot = tot + vall * val1
    }

    // strcpy(B.ref,OriginalB); - the restore between walks (the
    // local-carrier fold: ctmp re-primed from OriginalB)
    CstringOps.strcpyC(ctmp, originalB)

    // B-walk: totQ = sum of B(j,k)
    while (true) {
        if (Stubs.Mglobal(XNEXT, ctmp, ttt, Globals.svPtr) == 0) break

        if (CstringOps.strncmpS(b.ref, ctmp, j) != 0) break

        // strcpy(tmp1,ctmp); get_global(tmp1,rslt);
        CstringOps.strcpyC(tmp1, ctmp)
        Stubs.get_global(tmp1, rslt)

        if (Globals.svPtr.tpx == 0L) {
            Globals.GlobalExceptionCode = GLOBAL_NOT_FOUND
            throw MumpsGlobalException()
        }

        val val1 = strtod(CstringOps.cstring(rslt))
        totQ = totQ + val1
    }

    // val=totD+totQ-tot; if (val==0.0) return 0.0; return tot/val;
    val denom = totD + totQ - tot

    if (denom == 0.0) return 0.0

    return tot / denom
}
