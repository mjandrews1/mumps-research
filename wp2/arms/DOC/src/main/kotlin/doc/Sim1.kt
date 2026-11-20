package doc

// Unit 204 (B7 tranche 2): libmpscpp.cpp::Sim1 (batch 7, core
// stratum; LOC 40, score 24.2, rank 187; vendor libmpscpp.cpp:1565,
// physically verified this session; nonblank 40 matches the roster
// row) - the free document-similarity double over two globals
// (sum of A(ai,bj)*B(bk,bm) products along the XNEXT walk). Consult
// H-014 hit-adapt: the walks ride the existing seats and the
// batch-5 unit-120 wrapper's Stubs.sim1 seat FLIPS to this real on
// this render (the SymOpsMisc.kt MSV wrapper call sites bind
// unchanged). Seat map: Mglobal(XNEXT) is the resolved Mglobal
// delegate; get_global is the out-of-scope two-buffer member stub
// (the vendor tpx checks kept AFTER each call per the stub
// contract - the stub's own throw supersedes the check at runtime,
// flagged-untested ledger note per H-014); strtod is the internal
// MpsExp.kt unit-73 render (String seat, decimal-prefix C
// contract); B.Data() unsubscripted probe rides the globalData seat
// over the locally-bound ref fold (below).
//
// PASS-BY-VALUE LEDGER (the adapt): the vendor takes global A,
// global B BY VALUE and writes B.ref mid-walk
// (strcpy(B.ref,ctmp); strcat(B.ref,&tmp[i]);) - the writes live
// on the C value copy and never escape the call. The arm GlobalRef
// is a shared reference carrier: the fold renders the vendor's
// value-copy semantics on the LOCAL CharArray bRefLocal (the
// (unsigned char *)B.ref content, rebuilt each pass), and the
// unsubscripted B.Data() probe binds through a fresh GlobalRef over
// that local carrier - the caller's B object is never clobbered,
// observable behavior identical to the C value copy. The two
// trailing-0x01 root-marker trims kept verbatim (the 1,1 root
// suffix of a bare global name); tmp1 = tmp per vendor; the
// GLOBAL_NOT_FOUND throws route through the Globals carriers.
// Naming per H-017.

fun Sim1(a: GlobalRef, b: GlobalRef): Double {
    val ttt = CharArray(STR_MAX)
    val ctmp = CharArray(STR_MAX)
    val btmp = CharArray(STR_MAX)
    val tmp = CharArray(STR_MAX)
    val tmp1 = CharArray(STR_MAX)
    val rslt = CharArray(STR_MAX)
    var i: Int
    var j: Int
    var tot = 0.0
    var vall: Double
    val bRefLocal = CharArray(STR_MAX)

    CstringOps.strcpyS(tmp, a.ref)
    i = a.ref.length
    j = b.ref.length
    CstringOps.strcpyS(ctmp, b.ref)

    if (ctmp[j - 2] == CHAR_1 && ctmp[j - 1] == CHAR_1) {
        j--
        ctmp[j] = NUL
    }

    if (tmp[i - 2] == CHAR_1 && tmp[i - 1] == CHAR_1) {
        i--
        tmp[i] = NUL
    }

    while (true) {
        if (Stubs.Mglobal(XNEXT, tmp, ttt, Globals.svPtr) == 0) break

        if (CstringOps.strncmpS(a.ref, tmp, i) != 0) break

        // strcpy(B.ref,ctmp); strcat(B.ref,&tmp[i]); - the by-value
        // local fold (see the pass-by-value ledger above)
        CstringOps.strcpyC(bRefLocal, ctmp)
        CstringOps.catAtS(bRefLocal, 0, CstringOps.cstringAt(tmp, i))
        CstringOps.strcpyC(btmp, bRefLocal) // in case this and B are the same

        if (!Stubs.globalData(GlobalRef(CstringOps.cstring(bRefLocal)), "")) continue

        CstringOps.strcpyC(tmp1, tmp)
        Stubs.get_global(tmp1, rslt)

        if (Globals.svPtr.tpx == 0L) {
            Globals.GlobalExceptionCode = GLOBAL_NOT_FOUND
            throw MumpsGlobalException()
        }

        vall = strtod(CstringOps.cstring(rslt))

        Stubs.get_global(btmp, rslt)

        if (Globals.svPtr.tpx == 0L) {
            Globals.GlobalExceptionCode = GLOBAL_NOT_FOUND
            throw MumpsGlobalException()
        }

        val val1 = strtod(CstringOps.cstring(rslt))

        tot = tot + vall * val1
    }

    return tot
}
