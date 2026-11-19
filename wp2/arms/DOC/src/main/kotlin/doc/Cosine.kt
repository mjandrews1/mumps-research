package doc

// Unit 85 (B3 tranche 3 / sub-push A): libmpscpp.cpp::Cosine (batch
// 3, complex stratum; LOC 62, score 42.4). The cosine document
// similarity over two ordered globals: walks the next-subscripted
// siblings of A (Mglobal XNEXT with strncmp prefix tests against
// A.ref), accumulates the A-side sum of squares (totD) and the
// cross-products (tot), then walks B the same way for totQ, finally
// returning tot / sqrt(totD * totQ) with the zero guards. The seat
// table: Mglobal rides the H-014 Stubs seat (XNEXT 8, the Carriers
// const), get_global rides its Stubs seat, the B.Data() probe rides
// the Stubs.globalData seat, strtod reuses the MpsExp render, and
// kotlin.math.sqrt is the numeric intrinsic per H-010. Adaptations
// noted: the vendor mutates B.ref IN PLACE across the walk
// (strcpy/strcat onto the passed global object's reference member)
// and restores it before the B-side pass - the DOC GlobalRef carrier
// is immutable (val ref), so the walk state renders in the local
// bref string with the SAME observable sequence of seat calls
// (documented divergence, the TermCorrelate unit-14 keyed-seat
// precedent); the double SUBSCRIPT-terminator carve is the vendor
// quirk of chopping a trailing two-NUL-subscript pair (i-- / j--
// with the NUL rewrite) kept verbatim; the x01[2]={1,0} and A2
// buffers and the bflg/dummy locals of the vendor are dead stores
// (H-013, kept as this note); tpx checks on get_global preserved
// with the GLOBAL_NOT_FOUND raise (GlobalExceptionCode + throw,
// libmpscpp.cpp:71 code 2 - the Carriers const). Naming per H-017.

fun Cosine(A: GlobalRef, B: GlobalRef): Double {
    val ttt = CharArray(STR_MAX)
    val ctmp = CharArray(STR_MAX)
    val btmp = CharArray(STR_MAX)
    val tmp = CharArray(STR_MAX)
    val tmp1 = CharArray(STR_MAX)
    val rslt = CharArray(STR_MAX)

    // vendor dead locals: x01[2]={1,0}, A2[STR_MAX], bflg, the dummy
    // strcpy(ttt,"") (H-013, not translated)

    var totD = 0.0
    var totQ = 0.0
    var tot = 0.0
    var i: Int
    var j: Int

    CstringOps.strcpyS(tmp, A.ref)
    i = A.ref.length
    j = B.ref.length
    CstringOps.strcpyS(ctmp, B.ref)

    // vendor subscript-terminator carve: chop a trailing {1,1} byte pair
    if (ctmp[j - 2] == CHAR_1 && ctmp[j - 1] == CHAR_1) {
        j--
        ctmp[j] = NUL
    }

    if (tmp[i - 2] == CHAR_1 && tmp[i - 1] == CHAR_1) {
        i--
        tmp[i] = NUL
    }

    while (true) {
        if (Stubs.Mglobal(XNEXT, CstringOps.cstring(tmp), ttt, Globals.svPtr) == 0) break

        if (CstringOps.strncmpS(A.ref, tmp, i) != 0) break

        CstringOps.copyAt(tmp1, 0, tmp)
        Stubs.get_global(tmp1, rslt)

        if (Globals.svPtr.tpx == 0L) {
            Globals.GlobalExceptionCode = GLOBAL_NOT_FOUND
            throw MumpsGlobalException()
        }

        val valA = strtod(CstringOps.cstring(rslt)) // first operand
        totD = totD + valA * valA

        // B.ref walk state: bref = ctmp + &tmp[i] (the in-place
        // strcpy/strcat of the vendor, local per the note above)
        val bsuffix = CstringOps.cstringAt(tmp, i)
        val bref = CstringOps.cstring(ctmp) + bsuffix
        CstringOps.strcpyS(btmp, bref)

        if (!Stubs.globalData(B, bref)) continue

        Stubs.get_global(btmp, rslt)

        if (Globals.svPtr.tpx == 0L) {
            Globals.GlobalExceptionCode = GLOBAL_NOT_FOUND
            throw MumpsGlobalException()
        }

        val valB = strtod(CstringOps.cstring(rslt))

        tot = tot + valA * valB
    }

    if (tot == 0.0) return 0.0

    // restore B.ref (the vendor rewrites its object member; the local
    // ctmp is the restored state per the divergence note)
    while (true) {
        if (Stubs.Mglobal(XNEXT, CstringOps.cstring(ctmp), ttt, Globals.svPtr) == 0) break

        if (CstringOps.strncmpS(B.ref, ctmp, j) != 0) break

        CstringOps.copyAt(tmp1, 0, ctmp)
        Stubs.get_global(tmp1, rslt)

        if (Globals.svPtr.tpx == 0L) {
            Globals.GlobalExceptionCode = GLOBAL_NOT_FOUND
            throw MumpsGlobalException()
        }

        val valB = strtod(CstringOps.cstring(rslt))
        totQ = totQ + valB * valB
    }

    val denom = kotlin.math.sqrt(totD * totQ)

    if (denom == 0.0) return 0.0

    return tot / denom
}
