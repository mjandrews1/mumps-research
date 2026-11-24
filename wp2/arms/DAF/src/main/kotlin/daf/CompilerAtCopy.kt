package daf

// Unit 222 (DAF B8 T1, simple): compiler-mumps.c::atCopy
// (15 LOC, score 6.5): the at-copy helper of the compiler emitter:
// copies the xd scan buffer from xpx up to the at-flg bound into
// tmp[2...] with quote and backslash escaping, NUL-terminating at
// the write cursor (vendor compiler-mumps.c:1956, physically
// verified this session). SEAT RELEASE: the atCopyDaf named stub of
// DafInterpStubsT3 is released - the real body lands here, the
// SetCode/WriteCode call sites resolve unchanged (the levCheck
// release precedent of batch 7). TAG CORRECTION: the stub blame
// tagged the owner a non-roster compiler helper - WRONG; the
// physical owner compiler-mumps.c::atCopy is THIS roster row.
// Consults (2026-11-24T21:13:00Z, nominal): the j=2 contract is a
// CALLER prefill: every call site (SetCode, WriteCode, bound
// through the atCopyDaf seat) writes tmp[0] and tmp[1] before the
// call; this body keeps the vendor start index verbatim. The xpx
// walk and the xd buffer ride the DafCompilerState carrier
// (C.xpx/C.xd read access); the quote and backslash escape arms
// are verbatim; the vendor declares int i, j (both drive the loop,
// no dead locals). The tmp carrier stays the CharArray the callers
// prefill.
// Dafny: EXEMPT (state-buffer walk over the compiler carrier;
// journal row).

fun atCopyDaf(atFlg: Int, tmp: CharArray) {
    val C = DafCompilerState
    val xd = C.xd
    var i = C.xpx
    var j = 2

    // for (i = xpx, j = 2; xd[i] != 0 && i < at_flg; i++)
    while (xd[i] != NUL_D && i < atFlg) {

        if (xd[i] != '\"' && xd[i] != '\\') {
            tmp[j++] = xd[i]
        } else if (xd[i] == '\"') {
            tmp[j++] = '\\'
            tmp[j++] = '\"'
        } else if (xd[i] == '\\') {
            tmp[j++] = '\\'
            tmp[j++] = '\\'
        }

        i++
    }
    tmp[j] = NUL_D // tmp[j] = 0
}
