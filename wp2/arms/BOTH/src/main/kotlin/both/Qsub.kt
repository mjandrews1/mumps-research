package both

// Unit 193 render: interp-sym.cpp::Qsub (roster 193, batch 7,
// 52 LOC). The subscript extractor: pushes a fresh state vector
// (AllocSV + whole-struct copy), parses the tmp0 reference with
// gpad suppressed, keyfixes, splits the normalized reference at
// the \u0001 / >128 separators, and extracts the j-th subscript
// into tmp1 (-1 = empty, -2 = the whole reference).
//
// Consults (rule base wp2/arms/BOTH/rules_v0.pl):
//   h014 out_of_scope_call - reused: allocSV stub (owner
//     interp-sysfunc.cpp::AllocSV, roster 284 - units.csv rank
//     285, batch 8) called direct (stub-blocked); the interp-
//     side parse_(sv) stub (roster 303) called direct; keyfixC
//     and writeC called direct (BothCRuntime seats); the
//     svcBindSvZb whole-struct copy widened private->internal at
//     this render (ONE field universe, TWO consumers - the
//     unit-172 universe is the single memcpy fold, journaled).
//   h015 grammar-collision - reused: ASCII only, no backticks,
//     no raw comment dollars, trailing newline.
//   h017 naming - reused: Qsub keeps vendor case (no Kotlin
//     collision).
//   h020 pointer_handle - reused: the &v1d[1] and &tmp0[1]
//     offsets seat through the strCpyAtZb offset-copy helper;
//     keyfix(&v1d[1]) folds to keyfixC(sv.v1d) (the
//     Interpret.kt v1d[1]-seat convention).
//
// Quirks kept (ledgered):
//   - the vendor "inefficient" comment on the memcpy is kept in
//     spirit: the whole-struct copy rides the explicit field
//     copy (val buffers cannot re-bind; the unit-172 universe).
//   - free(svPtr) -> GC (the push/pop has no other consumer of
//     the popped state; journaled).
//   - tmp2 == NULL: j stays 0, so the k-loop runs exactly once
//     (k = 0..0) and the m>=n arm guards the empty reference -
//     exact.
//   - the walk loop breaks on \u0001 or >128 by WRITING the NUL
//     into tmp0 (tmp0 is mutated in place - the vendor mutates
//     the caller's buffer; kept).
//   - i++ after the break/NUL landing: the past-NUL advance is
//     kept (the vendor leaves i one past the NUL it wrote or
//     found).
//   - atoi(tmp2) -> atolC().toInt() (the atolC seat; vendor atoi
//     is int-width on the same digits).

// strcpy(&dst[dstOff], &src[srcOff]) fold: the NUL-terminated
// copy with explicit offsets
private fun strCpyAtZb(dst: CharArray, dstOff: Int, src: CharArray, srcOff: Int) {
    var s = srcOff
    var d = dstOff
    while (s < src.size && src[s] != '\u0000') {
        dst[d] = src[s]
        s++
        d++
    }
    dst[d] = '\u0000'
}

// void Qsub(unsigned char *tmp0, unsigned char *tmp1,
//           unsigned char *tmp2, struct MSV *svPtrX) - interp-sym.cpp
internal fun Qsub(
    tmp0: CharArray,
    tmp1: CharArray,
    tmp2: CharArray?,
    svPtrX: Msv,
) {
    var i: Int
    var j = 0
    var gblflg = 0

    if (tmp0[0] == '^') gblflg = 1

    i = svPtrX.LineNumber.toInt()
    val svPtr = allocSV() // push statevector (stub-blocked, owner 284)
    svPtr.LineNumber = i.toLong()

    svcBindSvZb(svPtr, svPtrX) // memcpy whole-struct copy (unit-172 universe)
    svPtr.t0px = 0
    svPtr.t2 = 1
    writeC(svPtr.xd, cStringOf(tmp0)) // strcpy(svPtr->xd, tmp0)
    svPtr.xpx = 0
    j = svPtr.gpad
    svPtr.gpad = 0
    i = parse_(svPtr)
    keyfixC(svPtr.v1d) // keyfix(&v1d[1]) - the v1d[1] seat
    svPtr.gpad = j
    if (gblflg != 0) {
        strCpyAtZb(tmp0, 1, svPtr.v1d, 1) // strcpy(&tmp0[1], &v1d[1])
    } else {
        strCpyAtZb(tmp0, 0, svPtr.v1d, 1) // strcpy(tmp0, &v1d[1])
    }
    if (tmp2 != null) j = atolC(cStringOf(tmp2)).toInt() // atoi(tmp2)

    if (j == -1) {
        writeC(tmp1, "")
        // free(svPtr) -> GC
        return
    }

    if (j == -2) {
        writeC(tmp1, cStringOf(tmp0)) // strcpy(tmp1, tmp0)
        // free(svPtr) -> GC
        return
    }

    val n = strlenC(tmp0)
    i = 0
    var m = 0
    for (k in 0..j) {
        m = i
        if (m >= n) {
            writeC(tmp1, "")
            // free(svPtr) -> GC
            return
        }
        while (tmp0[i] != '\u0000') {
            if (tmp0[i] == '\u0001' || tmp0[i].code > 128) {
                tmp0[i] = '\u0000'
                break
            }
            i++
        }
        i++
    }
    strCpyAtZb(tmp1, 0, tmp0, m) // strcpy(tmp1, &tmp0[m])

    // free(svPtr) -> GC
}
