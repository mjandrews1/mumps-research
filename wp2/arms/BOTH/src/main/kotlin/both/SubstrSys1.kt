package both

// Unit 212 render: compiler-sys1.c::substr (roster 212,
// batch 7, 17 LOC; units.csv rank 41; vendor body
// compiler-sys1.c). The compiler-side 1-based substring
// extract - the interpreter-side twin (unit 211) with a
// DIFFERENT clamp: len<0 and len>255 BOTH clamp to 255 (no
// realloc arm; the patrn call shapes ride it).
//
// DUAL-SEAT NOTE (the Sim1W family, journaled): the two
// vendor substr twins are NOT behaviorally identical - the
// interp-strmanip body (unit 211) has the realloc/stale arm
// and copies beyond 255 when len-start+1 <= STR_MAX, while
// the sys1 body clamps len>255 to 255. DISTINCT KERNELS -
// this render seats its own body (no delegate; the unit-189
// matchSys1 identical-body fold does NOT apply here).
//
// Consults (rule base wp2/arms/BOTH/rules_v0.pl):
//   h014 out_of_scope_call - reused: no callables (pure buffer
//     walk).
//   h015 grammar-collision - reused: ASCII only, no backticks,
//     no raw comment dollars, trailing newline.
//   h017 naming - reused: substrSys1 (the matchSys1
//     precedent; the package substr seat is the unit-211
//     render - the signatures would collide).
//   h020 pointer_handle - reused: the &bd[1] / &in[start]
//     offsets seat through the inOff slice-index adaptation
//     (the unit-211 signature family).
//
// Quirks kept (ledgered):
//   - the two clamps (len<0, len>255) fold to one 255 clamp.
//   - the raw walk reads buffer bytes past the NUL (no NUL
//     check in the vendor); the CharArray IS the memory; only
//     the array end is guarded (vendor OOB read UB
//     quantized).
//   - short types: the vendor short walk folds to Int (the
//     values fit; journaled).

// void substr(unsigned char in[], unsigned char out[],
//             short start, short len) - compiler-sys1.c
internal fun substrSys1(
    input: CharArray,
    inOff: Int,
    out: CharArray,
    start: Int,
    len0: Int,
) {
    if (len0 == 0) {
        out[0] = '\u0000'
        return
    }

    // if (len < 0) len = 255; else if (len > 255) len = 255;
    val len = if (len0 < 0 || len0 > 255) 255 else len0

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
