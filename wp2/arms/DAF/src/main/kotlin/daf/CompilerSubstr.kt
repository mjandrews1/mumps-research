package daf

// Unit 189 (DAF B7 T1, simple): compiler-sys1.c::substr (17 LOC,
// score 6.7): the compiler-TU substring service, the short-domain
// twin of the unit-190 interp-strmanip.cpp::substr body of this same
// batch (the same-name family sliced per the KDoc-envelope discipline
// the mdh_tree_level_add triple of the cross-arm notes documents):
// this row's body clamps len to 255 in BOTH arms, uses short-domain
// locals and carries NO realloc arm (the strmanip twin's realloc
// quirk is absent here); the in-place slice loop copies from the
// (start-1) offset and NUL-terminates.
// Consults (2026-11-23T18:31:30Z, nominal): BASE-log row 8
// hit-reuse (buffer-role classification: in scan-only source, out
// written scratch), row 27 hit-adapt (the 255 clamp ladder and the
// pre-decrement offsets fold onto the index arithmetic verbatim -
// the C is the specification), row 30 hit-adapt (the out[] carrier
// stays an out-seat: the compiler call sites (the DAF Match.kt
// pattern slices) write into tmp carriages read onward; the
// row-30 out->return fold is NOT taken so the seat keeps the vendor
// in/out shape; the DOC unit-59 inout precedent binds the
// compiler-side call sites to this TU-local twin - the
// compiler-sys1.c match renders bind here, the Match.kt substrDaf
// seat rebind rides this render), manual-decision (no BASE row:
// start is 1-based per the callers; a 0 or negative start would read
// out[i] with negative index in C (UB) - the vendor does not guard
// and the render keeps the same unguarded arithmetic over the
// carrier, divergence ledgered at the callers' lengths).
// Dafny: EXEMPT (raw-carrier slice loop over the out seat; journal
// row).

fun compilerSubstrD(inBuf: CharArray, inOff: Int, out: CharArray, start: Int, lenIn: Int) {

    var len = lenIn
    if (len == 0) {
        out[0] = NUL_D
        return
    }

    if (len < 0) len = 255
    else if (len > 255) len = 255

    var st = start - 1 // start--
    len = len - 1      // len--
    var i = 0
    while (i <= len) { // for (i = 0; i <= len; i++) out[i] = in[start++];
        out[i] = inBuf[inOff + st]
        st++
        i++
    }
    out[len + 1] = NUL_D
}
