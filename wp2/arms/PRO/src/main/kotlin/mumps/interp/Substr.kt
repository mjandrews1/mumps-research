package mumps.interp

/**
 * PRO-arm translation of interp-strmanip.cpp::substr (B7 unit
 * 212, units.csv data row 212: loc 19, score 7.1,
 * complexity_rank 45, simple stratum; vendor
 * interp-strmanip.cpp:59-82). RESOLVES the InterpStubs h014
 * substr stub (raised by the B3 patrn tranche); the stub is
 * removed - the InterpPiece/Match call sites bind unchanged via
 * the (src, srcOff, dst, start, len) signature. The
 * compiler-sys1.c::substr twin is unit 213 (this batch,
 * compiler package - a different body).
 *
 * Vendor shape: substr(in, out, start, len) - len==0 empties out
 * and returns; len<0 clamps to 255; the len-start+1 > STR_MAX
 * arm realloc's out (see notes); then start--/len-- and the
 * loop copies out[i] = in[start++] for i=0..len, closing with
 * out[len+1] = 0. First character is start=1 (MUMPS $EXTRACT
 * convention).
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h003 state_macro      : none (no state vector).
 *  - h007 char_buffer      : in/out are NUL-terminated byte
 *    buffers (h012 domain).
 *  - h009 format_string    : none (no sprintf in this body).
 *  - h012 unsigned_byte_domain: the 0..255 clamp and the byte
 *    copy loop ride Char code points.
 *  - h013 dead_store       : none - i, start, len all live.
 *  - h014 out_of_scope_call: self-contained (realloc does not
 *    exist as a corpus callee; see the vendor note below).
 *  - h015 grammar_collision: pre-push scan clean - no backtick,
 *    double-underscore, em dash or !! in this file.
 *  - h017 naming           : substr keeps the vendor name; the
 *    srcOff carrier (absent in the vendor signature) is the
 *    stub-era offset seat - the caller's &in[off] pointers fold
 *    into it (GlobalQuery precedent).
 *  - h018 reference_build_visibility: none - no conditionals
 *    beyond the runtime len/start tests.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The len-start+1 > STR_MAX arm calls realloc(out, len-start<<1)
 *    - C precedence parses the size as len - (start<<1) - and
 *    reassigns only the LOCAL out pointer, so the caller's
 *    buffer is never enlarged (the vendor's UB/leak residue).
 *    Quantized to a no-op with this note (the commented-out
 *    len = STR_MAX-1 line right below is the vendor's own
 *    disabled fix, kept as this comment).
 *  - The loop's short i rides an Int in Kotlin; i <= len with
 *    the pre-decremented len means len+1 bytes copy (start=1,
 *    len=1 copies exactly in[0]) - kept exactly.
 */
fun substr(src: CharArray, srcOff: Int, dst: CharArray, start: Int, lenIn: Int) {
    var len = lenIn
    var start = start

    if (len == 0) {
        dst[0] = NUL
        return
    }

    if (len < 0) {
        len = 255
    } else if (len - start + 1 > STR_MAX) {
        // vendor: out = realloc(out, len - (start << 1)); - the
        // caller's buffer is never enlarged (local-pointer
        // reassign); quantized to a no-op (h018 note above).
    }

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

private const val STR_MAX = 32000 // vendor strmax.h:3 (file-private, cf. Fcn.kt)
