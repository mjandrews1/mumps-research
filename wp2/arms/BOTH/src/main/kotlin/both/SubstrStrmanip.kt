package both

// Unit 211 render: interp-strmanip.cpp::substr (roster 211,
// batch 7, 19 LOC; units.csv rank 45; vendor body
// interp-strmanip.cpp). The 1-based substring extract: the
// len==0 empty seat, the len<0 -> 255 clamp, the 0-based walk,
// and the NUL terminator at out[len].
//
// SEAT RELEASE: the Match.kt substr TODO stub (raised at the
// unit-181 render) RELEASES at this render - release note
// there; the stub's 5-arg slice-index signature (input,
// inOff, out, start, len) is KEPT (its two Match call sites
// and the &ad[ptr] / &bd[1] call shapes ride it). The old
// stub pin said "roster unit 212" - off by one (units.csv
// line 212 is unit 211, rank 45; unit 212 is the DISTINCT
// compiler-sys1.c::substr, the NEXT unit) - corrected in the
// release note.
//
// Consults (rule base wp2/arms/BOTH/rules_v0.pl):
//   h014 out_of_scope_call - reused: no callables (pure
//     buffer walk).
//   h015 grammar-collision - reused: ASCII only, no backticks,
//     no raw comment dollars, trailing newline.
//   h017 naming - reused: substr keeps the vendor name (the
//     released stub's seat); the FILE is SubstrStrmanip.kt
//     (the compiler-sys1.c::substr twin renders next unit).
//   h020 pointer_handle - reused: the &in[start] offsets seat
//     through the inOff slice-index adaptation (the stub's
//     signature).
//
// Quirks kept (ledgered):
//   - the realloc arm (len-start+1 > STR_MAX): the vendor
//     reallocs the LOCAL out pointer and writes the copy
//     THERE - the CALLER's buffer stays stale (the realloc'd
//     pointer is discarded; the vendor wart). QUANTIZED: the
//     arm performs no copy at all (out left untouched - the
//     caller-observable state matches), journaled.
//   - the in[] walk reads RAW buffer bytes past the NUL (the
//     C loop has no NUL check - the copy continues into the
//     buffer residue); the CharArray IS the memory, so the
//     raw reads are kept; only the array END is guarded
//     (vendor OOB read UB quantized).
//   - len-- then loop i<=len then out[len+1]=0: L bytes copied
//     from start-1, NUL at index L (kept exact).

// void substr(unsigned char in[], unsigned char out[],
//             int start, int len) - interp-strmanip.cpp
// (inOff: the &in[start] slice-index adaptation, stub-seat
// signature kept)
internal fun substr(
    input: CharArray,
    inOff: Int,
    out: CharArray,
    start: Int,
    len0: Int,
) {
    var len = len0

    if (len == 0) {
        out[0] = '\u0000'
        return
    }

    if (len < 0) {
        len = 255
    } else if (len - start + 1 > STR_MAX) {
        // realloc arm - the caller's buffer stays stale (the
        // vendor writes into the discarded realloc'd pointer);
        // quantized to no copy (journaled)
        return
    }

    var st = start - 1 + inOff // start--
    val l = len - 1 // len--
    var i = 0
    while (i <= l) {
        if (st < input.size && i < out.size) out[i] = input[st]
        st++
        i++
    }
    if (l + 1 < out.size) out[l + 1] = '\u0000'
}
