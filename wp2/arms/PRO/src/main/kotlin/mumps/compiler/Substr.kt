package mumps.compiler

import mumps.interp.NUL

/**
 * PRO-arm translation of compiler-sys1.c::substr (B7 unit 213,
 * units.csv data row 213: loc 17, score 6.7, complexity_rank 41,
 * simple stratum; vendor compiler-sys1.c:396-416). RESOLVES the
 * compiler Stubs.kt h014 substr stub (raised by the B3 patrn
 * tranche, called by Match.kt at :186 - the u190-era call was
 * made knowingly against this owner); the stub is removed - call
 * sites bind unchanged on the raised (src, srcOff, dst, start,
 * len) signature. The interp-strmanip.cpp::substr twin is unit
 * 212 (interp package) - this compiler body clamps len at 255
 * instead of the realloc residue and takes short parameters.
 *
 * Vendor shape: substr(in, out, start, len) - len==0 empties out
 * and returns; len<0 OR len>255 clamps to 255; then start--/len--
 * and the loop copies out[i] = in[start++] for i=0..len, closing
 * with out[len+1] = 0 (start=1 convention, as the interp twin).
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h003 state_macro      : none (no state vector).
 *  - h007 char_buffer      : in/out are NUL-terminated byte
 *    buffers (h012 domain).
 *  - h009 format_string    : none (no sprintf in this body).
 *  - h012 unsigned_byte_domain: the 255 clamp and byte copy loop
 *    ride Char code points.
 *  - h013 dead_store       : none - i, start, len all live.
 *  - h014 out_of_scope_call: self-contained.
 *  - h015 grammar_collision: pre-push scan clean - no backtick,
 *    double-underscore, em dash or !! in this file.
 *  - h017 naming           : substr keeps the vendor name; the
 *    package split (mumps.compiler vs mumps.interp) separates
 *    the two same-named corpus twins; srcOff is the stub-era
 *    offset seat (the &ad[ptr]/&bd[1] caller pointers fold into
 *    it, GlobalQuery precedent).
 *  - h018 reference_build_visibility: the NOTABS arm of the
 *    neighboring readf_ body does not touch this unit - no
 *    conditionals beyond the runtime len tests here.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The K&R short parameters mean start/len ride Short in C;
 *    Kotlin keeps Int carriers (Char indexing) - the 255 clamp
 *    bounds the domain identically.
 */
fun substr(src: CharArray, srcOff: Int, dst: CharArray, start: Int, lenIn: Int) {
    var len = lenIn
    var start = start

    if (len == 0) {
        dst[0] = NUL
        return
    }

    if (len < 0) len = 255
    else if (len > 255) len = 255

    start--
    len--
    var i = 0
    while (i <= len) {
        dst[i] = src[srcOff + start]
        start++
        i++
    }
    dst[len + 1] = NUL
    return
}
