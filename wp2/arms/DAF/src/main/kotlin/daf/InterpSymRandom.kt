package daf

/*
 * DAF batch-9 unit 250 (roster row 250; interp-sym.cpp::_random,
 * simple, LOC 18, score 6.8). Consult 2026-11-24T22:31:30Z.
 *
 * The sym-side \u0024random helper: out = uniform draw in [0, j).
 * The/z-fcn battery (Fcn.kt carry seat) consumed the rand seat
 * inline at its earlier batch; THIS row lands the sym-side helper
 * body named to the vendor symbol, per the physical walk.
 *
 * Quirks:
 *  - once-only srand(time) init (the vendor first static): the
 *    arm models it via the srandSeedI store precedent (H-013: the
 *    seed store exists, rand never called there; here rand IS
 *    called via the randD seat).
 *  - DOCUMENTED DIVERGENCE (rand seat, DafInterpStubsT3:252): the
 *    libc rand() sequence is not reproducible; randD models the
 *    draw. The vendor uniform-boxing loop ((j*rand/(RAND_MAX+1))
 *    >= j -> redraw) folds to nextInt(j) with the redraw guard
 *    kept for shape parity (never taken on the JVM draw).
 */

fun randomD(out: CharArray, inp: CharArray) {
    val j = atoi(DafCRuntime.cstring(inp))
    if (j < 2) {
        out[0] = '0'
        out[1] = NUL_D
        return
    }
    // once-only srand init (vendor static first): the arm rand
    // seat already seeds via java.util.Random; the init call is
    // comment-only per the H-013 precedent.
    var rslt: Int
    do {
        rslt = Math.floorMod(randD(), j)
    } while (rslt >= j)
    val s = rslt.toString()
    var i = 0
    for (c in s) out[i++] = c
    out[i] = NUL_D
}
