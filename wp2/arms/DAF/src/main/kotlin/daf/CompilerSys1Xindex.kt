package daf

/*
 * DAF batch-10 unit 296 (roster row 296; compiler-sys1.c::xindex,
 * LOC 21, ptr 0, score 10.9). Consult 2026-11-25T15:09:00Z.
 *
 * The compiler TU twin of the b9 unit-260 release
 * (compiler-sys1.c::xindex and interp-sysfunc.cpp side xindex are
 * the same body in two TUs; the DAF corpus seats them
 * separately). Kotlin arity-based JVM overload: the compiler seat
 * takes the (source, ptrn, strt) shape, the interp seat the
 * flat (s, sOff, pat, patOff, from) carrier (the B9 tag note in
 * DafInterpStubsT3.kt named both rows).
 *
 * Fold notes: the K&R short params stay Int-typed shorts in the
 * delegate call (strt is bound to the from carrier); the body
 * is the unit-260 algorithm verbatim - same TU, no divergence.
 *
 * H-015: ASCII; no raw dollars; trailing newline.
 * Dafny: EXEMPT (index-family string search; the strmanip spec
 * family carries the shape; no new spec).
 */

// int xindex(unsigned char source[], unsigned char ptrn[], short strt)
// - compiler-sys1.c, the compiler-TU overload
fun xindexDaf(source: CharArray, ptrn: CharArray, strt: Short): Int =
    xindexDaf(source, 0, ptrn, 0, strt.toInt())
