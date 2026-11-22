package both

// Unit 168 (BOTH B6 T2, core): interp-sym.cpp::_extract (34 LOC).
// Batch_plan ordinal 168: core, LOC 34, score 13.0, complexity
// rank 126. Vendor body interp-sym.cpp:1235-1277 (the batch-6
// TAG-note ledger keeps _extract in this batch; the strmanip
// \$EXTRACT service routes here).
//
// Vendor behavior: the MUMPS \$EXTRACT(from:to) piece semantics:
// start/len are atoi-parsed (the quiet-parse fold); start is
// 1-based (i-- after the <0 clamp); len is the through-position
// (j--); j<i yields the empty string; the (1,1) fast path copies
// the single first byte (including its NUL); start past the end
// yields empty; the through index clamps below strlen (the
// exclusive-end fold of the vendor's through-N behavior); the
// loop walks inclusive i..j onto out with the NUL terminator.
//
// Quirks kept (ledgered):
//   - the (i==0 && j==0) fast path reads in[0] with NO length
//     check on the source: an empty source reads the C buffer's
//     residue byte (UB). Quantized with the empty-source check
//     (empty in yields the empty string; the divergence
//     journaled).
//   - i==0/j==0 after the decrements is the (start=1,len=1) case
//     ONLY when the un-decremented values were exactly 1 - a
//     start of 0 decrements PAST zero (i = -1, the else arm of
//     the i<0 clamp): the C copy loop reads in[-1] out of bounds
//     (UB). Quantized to the empty piece (see the i<0 guard).
//   - j<0 (the else of the len<0 test) takes j=i (len=start:
//     single piece) - the negative-len quirk is kept by the
//     branch structure verbatim.
//
// BOTH consults (find_rule before render, events logged):
//   h007 char_buffer - reused and hit: out is the caller's
//     CharArray via writeC; in_ is the String source (the vendor
//     unsigned char* walk over the same byte domain).
//   h012 unsigned_byte_domain - reused and hit: the atoi seats
//     ride the internal atolC fold (B6 T1 promotion); the index
//     domain stays the clamped Int walk.
//   h017 naming - hit: _extract keeps the vendor leading-
//     underscore form (the _find stub naming precedent).

internal fun _extract(out: CharArray, in_: String, start: String, len: String) {
    var i = atolC(start).toInt()
    var j = atolC(len).toInt()

    if (i < 0) i = 0 else i-- // 1-based -> 0-based

    if (j < 0) j = i else j-- // j is the THROUGH position

    if (j < i) { // empty piece
        writeC(out, "")
        return
    }

    if (i == 0 && j == 0) { // the single-first-byte fast path
        if (in_.isEmpty()) { // quantized: C reads the residue byte
            writeC(out, "")
            return
        }
        out[0] = in_[0]
        out[1] = '\u0000'
        return
    }

    val n = in_.length

    // vendor quirk: a start of 0 decrements to i = -1 (the else
    // arm of the i<0 clamp walks below zero); the C walk then
    // reads in[-1] out of bounds (UB). Quantized: the negative
    // through-index family yields the empty piece (the
    // deterministic quantization; the divergence journaled).
    if (i < 0) {
        writeC(out, "")
        return
    }

    if (i > n) {
        writeC(out, "")
        return
    }

    // vendor: if (j > n) j = n; the through index may reach the
    // source NUL (in[n] copies a NUL into out ahead of the final
    // out[k]=0). The observable contents are i..min(j,n-1): the
    // exclusive-end clamp j = n - 1 lands the identical byte
    // sequence (writeC appends the NUL); the divergence is
    // journaled.
    if (j >= n) j = n - 1
    val piece = StringBuilder()
    while (i <= j) {
        piece.append(in_[i])
        i++
    }
    writeC(out, piece.toString())
    // writeC lands the NUL (the C out[k]=0 tail)
}
