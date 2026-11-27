package both

// Unit 145 (BOTH B5 T3, complex): interp-sym.cpp::Qlength (18 LOC).
// Batch_plan ordinal 145: complex, LOC 18, score 35.0. The dollar-
// qlength intrinsic: parse a global name expression to a pushed
// state-vector, then count its subscript markers in v1d.
//
// Vendor behavior: the commented ErrorMessage guard is dead (kept
// below as a comment verbatim); the LineNumber transfers to the
// pushed copy; AllocSV pushes the state vector and the memcpy is the
// vendor's own documented inefficient copy (a shallow member copy
// fold here, journaled); t0px=1 and t2=1 mark the plumbed parse
// mode; strcpy lands tmp0 in xd, xpx=0 zeroes the parse field;
// parse_ does the parse (roster owner below); keyfix(key) normalizes
// the v1d global reference in place (the leading caret is stripped
// and the high coded subscripts become 1); the count loop walks
// v1d from index 1 counting the 1 markers and the >128 high codes;
// j==0 writes "0", else sprintf writes j-1 (the LAST subscript
// position is the closing delimiter, not a subscript); free drops.
//
// Quirks kept (ledgered):
//   - the commented-out ErrorMessage guard stays verbatim as a
//     comment (the vendor left it commented).
//   - memcpy(svPtr, svPtrX, sizeof(struct MSV)) is a full-struct
//     shallow copy: the Kotlin fold copies the carrier references
//     (Arrays are shared with the donor - the vendor's byte copy has
//     no observable difference for the parse this body runs: xd and
//     v1d are overwritten before any read on this path; journaled).
//   - free(svPtr) has no Kotlin seat (GC).
//
// BOTH consults (find_rule before render, events logged):
//   h004 globals_ext - reused: the MSV carrier extension adds the
//     parse plumbing fields (xd :45, t0px :55, xpx :56, t2 :67, all
//     pinned in include/mumpsc/stateVector.h this session).
//   h007 char_buffer - reused: tmp0 is the scan-only parameter
//     (String seat); tmp2 is the caller's out buffer (CharArray via
//     the writeC seat, the strcpy fold).
//   h009 format_string - reused: the "%d" fold is the exact decimal
//     render of j-1.
//   h012 unsigned_byte_domain - reused: the >128 compare and the 0x80
//     masks stay in the Char(0..255) byte domain.
//   h014 out_of_scope_call - reused: parse_ rides its new TODO stub
//     (owner interp-parse.cpp::parse_, roster 93, batch 3 - C-015 renumber) and
//     AllocSV rides its new TODO stub (owner interp-sysfunc.cpp::
//     AllocSV, roster 285, batch 8); keyfix is the
//     include/mumpsc/keyfix.h static inline (a non-unit): rendered
//     real here, the T2 errorMessage precedent.
//   h017 naming - hit: Qlength/svPtrX/svPtr/keyfix keep vendor
//     shape.

internal fun Qlength(tmp0: String, tmp2: CharArray, svPtrX: Msv): Int {
    // if (tmp0[0] != '^') ErrorMessage(...) - commented out in the
    // vendor body (kept verbatim as this comment); int i, j = 0.

    val i0 = svPtrX.LineNumber
    val svPtr = allocSV() // push statevector (stub-blocked)
    svPtr.LineNumber = i0

    // memcpy(svPtr, svPtrX, sizeof(struct MSV)) - shallow fold (see
    // quirks): the pushed carrier shares the donor arrays; xd and
    // v1d are written before any read on this path.
    svPtr.t0px = 1
    svPtr.t2 = 1
    writeC(svPtr.xd, tmp0) // strcpy((char *) svPtr->xd, tmp0)
    svPtr.xpx = 0
    parse_(svPtr) // i = parse_(svPtr); i unused past (h013 dead store)
    keyfixV1d(svPtr) // keyfix(&svPtr->v1d[1])

    var j = 0
    for (i in 1 until svPtr.v1d.size) {
        val c = svPtr.v1d[i]
        if (c == 0.toChar()) break
        if (c == '\u0001' || c.code > 128) j++
    }

    if (j == 0) {
        writeC(tmp2, "0") // strcpy(tmp2, "0")
    } else {
        // sprintf(tmp2, "%d", j-1)
        val s = (j - 1).toString()
        for (k in s.indices) tmp2[k] = s[k]
        tmp2[s.length] = 0.toChar()
    }

    // free(svPtr) - no seat (GC).
    return if (j == 0) 0 else j - 1
}

// static inline void keyfix(unsigned char *vxd) - include/mumpsc/
// keyfix.h, non-unit static inline. NOTE: the vendor call site is
// keyfix(&v1d[1]) - the pointer walk starts at v1d[1], so the fold
// passes the state vector and walks v1d[1] .. NUL (index arithmetic
// identical to the C pointer domain).
internal fun keyfixV1d(sv: Msv) {
    if (sv.v1d[1] == '^') { // leading-caret arm
        var t = 1
        while (sv.v1d[t] != 0.toChar()) {
            val nxt = sv.v1d[t + 1]
            sv.v1d[t] = if (nxt.code and 0x80 != 0) '\u0001' else nxt
            t++
        }
        return
    }
    var t = 1 // no-caret arm (map high codes to 1)
    while (sv.v1d[t] != 0.toChar()) {
        if (sv.v1d[t].code and 0x80 != 0) sv.v1d[t] = '\u0001'
        t++
    }
}
