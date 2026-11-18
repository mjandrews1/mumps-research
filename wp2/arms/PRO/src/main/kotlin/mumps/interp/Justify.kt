package mumps.interp

/**
 * PRO-arm translation of interp-sym.cpp::_justify (B7 unit 208,
 * units.csv data row 208: loc 26, score 8.8, complexity_rank 81,
 * simple stratum; vendor interp-sym.cpp:1328-1354; the banner
 * comment above the body reads "may be dead" - no in-file caller
 * exists, kept as a roster unit). NOT the same unit as the B3
 * interp-arith.cpp::mps_justify (MpsJustify.kt): that is the
 * $JUSTIFY formatting core taking the full format string as its
 * first argument; this one takes width w and precision p
 * separately and builds the format at run time.
 *
 * Vendor shape: _justify(out, in, w, p) - if p is "-1" the
 * width-only arm runs: k = strlen(bd after strncpy of in); k>=l
 * copies in to out; else l is clamped to 255 and lpad right-
 * shifts the copy. Otherwise k = atoi(p) and the format
 * "%<l>.<k>lf" is built into tmp2, then t1 = atof(in) is
 * sprintf'd into bd and strncpy'd to out.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h003 state_macro      : none (no state vector).
 *  - h007 char_buffer      : out/in/w/p are NUL-terminated byte
 *    buffers; bd STR_MAX; tmp2 32 (h012 domain).
 *  - h009 format_string    : the "%c%d%c%dlf" build produces
 *    "%<l>.<k>lf" at RUN TIME (the %c carriers are the quote
 *    escapes for '%' and '.'); the second sprintf consumes the
 *    built format - rendered as java.util.String.format over
 *    the built string (the MpsJustify precedent).
 *  - h012 unsigned_byte_domain: atoi/atof/strlen on byte
 *    buffers via atoiC/strtodC/strlenC.
 *  - h013 dead_store       : none - l, k, t1 all live.
 *  - h014 out_of_scope_call: atoi -> atoiC, atof -> strtodC,
 *    strcmp -> strncmpC == 0 (real reuse), lpad REAL REUSE
 *    (InterpStrmanipMisc.kt, offset carriers 0), strncpy/strcpy
 *    -> strncpyC per-file carriers.
 *  - h015 grammar_collision: pre-push scan clean - no backtick,
 *    double-underscore, em dash or !! in this file; the single
 *    leading underscore is the vendor name (single underscore OK
 *    per the RsltCallback precedent).
 *  - h017 naming           : _justify keeps the vendor name
 *    (distinct from mpsJustify by the leading underscore).
 *  - h018 reference_build_visibility: none - no conditionals
 *    beyond the p=="-1" runtime test.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - lpad's strout-untouched-when-k<=0 quirk means a short bd
 *    with l <= len+1 leaves bd as the plain strncpy of in - the
 *    vendor then still strncpy's bd to out; kept.
 *  - l is clamped to 255 only inside the p=="-1" arm AFTER the
 *    k>=l test - kept in that order.
 *  - tmp2[32] trusts the built format to fit (l<=255, k<=atoi
 *    width) - kept, no bounds check added.
 */
fun _justify(out: CharArray, in_: CharArray, w: CharArray, p: CharArray) {
    val bd = CharArray(STR_MAX)
    val tmp2 = CharArray(32)

    val l = atoiC(w)
    strncpyC(bd, in_) // strncpy(bd, in, STR_MAX)

    if (strncmpC(p, 0, "-1", 2) == 0 && strlenC(p) == 2) {
        val k = strlenC(bd)

        if (k >= l) {
            strncpyC(out, in_) // strncpy(out, in, STR_MAX)
            return
        }

        val lc = if (l > 255) 255 else l
        lpad(bd, 0, bd, 0, lc.toShort())
        strncpyC(out, bd) // strncpy(out, bd, STR_MAX)
        return
    }

    val k = atoiC(p) /* arg 3 */
    // sprintf(tmp2, "%c%d%c%dlf", '%', l, '.', k) - %c are escapes
    val fmt = "%" + l + "." + k + "lf"
    for (i in fmt.indices) tmp2[i] = fmt[i]
    tmp2[fmt.length] = NUL

    val t1 = strtodC(in_) // atof(in)
    val s = String.format(cstringAt(tmp2), t1) // sprintf(bd, tmp2, t1)
    for (i in s.indices) bd[i] = s[i]
    bd[s.length] = NUL
    strncpyC(out, bd) // strncpy(out, bd, STR_MAX)
    return
}

/** strncpy(dst, src, STR_MAX) - byte-buffer copy incl. NUL (h014). */
private fun strncpyC(dst: CharArray, src: CharArray) {
    var i = 0
    while (i < STR_MAX) {
        dst[i] = src[i]
        if (src[i] == NUL) return
        i++
    }
}

private const val STR_MAX = 32000 // vendor strmax.h:3 (file-private, cf. Fcn.kt)
