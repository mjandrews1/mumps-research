package daf

// Unit 160 (DAF B6 T1, simple): libmpscpp.cpp::Sum
// (17 LOC, score 8.1, rank 70): the free char* wrapper of the Sum
// service: the one-arg bsv form that decodes the caret-bypass slice,
// finds the coded-1 boundary, and sums either the whole node tree
// (the boundary-at-end case) or the named subtree (the slice after
// the boundary).
// Consults (2026-11-22T15:47:30Z, nominal): BASE-log rows 80/54
// hit-reuse (the bsv wrapper shape of the LibmpscppMisc9 family; the
// global a(&arg1[1]) ctor-bypass folds onto a fresh MpsGlobalNode
// over the cstringAt(arg1, 1) slice, the row-54 node model), rows
// 111/81 hit-reuse (the coded-1 probe over the CharArray carrier
// with the at() NUL end-guard: the vendor arg1[i-2] read when i==1
// is the C UB read that the at() guard secures, row 81 fold), rows
// 90/98 hit-reuse (the member call relays to the unit-143 member
// render globalSumD of Sum.kt: the member body stays there; this
// wrapper only decodes and relays), rows 33/56 hit-adapt (the
// sprintf(tmp, \"%g\", x) over static char tmp[32] return folds to a
// fresh per-call String (row 56) via DafCRuntime.fmtG, single-buffer
// hazard ledgered as at the unit-158 render).
// MANUAL-DECISION fold (row 54/68 family): the vendor a(\"\").Sum()
// call invokes operator() with the empty subscript; the empty
// subscript extends the receiver ref by nothing, so the render
// passes the receiver node itself to the member seat; the nonempty
// branch passes the arg1[i+1] slice node. Both branches keep the
// member-call shape (no inlined walk).
// Vendor quirk kept: the arg1[i-1]==1 && arg1[i-2]==1 tail-trim
// happens BEFORE the boundary probe and mutates the caller's buffer
// in place (row 80 in-place split discipline, same as split1).
// Dafny: EXEMPT (wrapper over the member-seat walk; journal row).

fun SumW(svptr: DafMsv, ep: CharArray, arg1: CharArray): String {

    var i = DafCRuntime.cstring(arg1).length // i = strlen(arg1)

    // if (i > 0 && arg1[i-1]==1 && arg1[i-2]==1) arg1[i-2]=0;
    if (i > 0 && at(arg1, i - 1) == FCN_MARK1_D && at(arg1, i - 2) == FCN_MARK1_D) {
        arg1[i - 2] = NUL_D
    }

    // for (i=0; arg1[i]!=0; i++) if (arg1[i]==1) break;
    i = 0
    while (at(arg1, i) != NUL_D) {
        if (arg1[i] == FCN_MARK1_D) break
        i++
    }

    val j = at(arg1, i) // j = arg1[i]
    arg1[i] = NUL_D     // arg1[i]=0 (in-place split, row 80)

    val a = MpsGlobalNode(DafCRuntime.cstringAt(arg1, 1)) // global a(&arg1[1])

    val x = if (j == NUL_D) {
        globalSumD(a, svptr) // a("").Sum() - empty-subscript fold
    } else {
        // a(&arg1[i+1]).Sum()
        globalSumD(MpsGlobalNode(DafCRuntime.cstringAt(arg1, i + 1)), svptr)
    }

    return DafCRuntime.fmtG(x) // sprintf(tmp, "%g", x); return tmp
}
