package mumps.interp

/**
 * PRO-arm translation of interp-parse.cpp::patrn1 (pilot B1, unit 30).
 * Translated independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), reference source
 * lines 1335-1400 (units.csv: 34 SLOC).
 *
 * patrn1(jpx, ernbr, svPtr): "pattern argument [right side) set up" -
 * interp-side twin of the compiler's compiler-sys1.c::patrn1 (B1 unit
 * 26): scans svPtr.xd from *jpx, appending pattern text into
 * svPtr.bd[0..] (210 marker for doubled quotes), until a charset
 * terminator; *jpx ends one before the first unconsumed char, *ernbr
 * gets 2 (unterminated quote) or 0 (normal end). Unlike the compiler
 * twin, the append base is &bd[0] and there is no paren-depth counter.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h002 simple_output_macro : strcat((char*)&svPtr->bd[0], ...) ->
 *    local append helpers (strcatCS/strcatCC) preserving the C-string
 *    append at bd[0]; xindex's literal arg becomes fresh CharArray.
 *  - h006 out_param_holder   : vendor short *jpx, *ernbr ->
 *    ShortArray(1) holders (both read AND written by the callee).
 *  - h008 static_scratch     : vendor's const static cod210[2]={210,0}
 *    and the per-call tmp1[2]={0,0} become fresh per-call buffers.
 *  - h011 goto               : backward gotos a1285/a1283 become a
 *    labeled outer loop and an inner while; the forward goto a1281
 *    becomes a plain break out of the inner loop.
 *  - h012 unsigned_byte_domain: xd/bd/tmp1 byte domains; cod210 holds
 *    Char(210).
 *  - h014 out_of_scope_call  : xindex (InterpStubs stub, owner
 *    interp-strmanip.cpp::xindex, not a corpus unit).
 *  - h015 grammar_collision  : pre-push scan incl. cross-file top-level
 *    names: this patrn1 lives in mumps.interp while the unit-26 twin
 *    lives in mumps.compiler - different packages, no collision; new
 *    helpers private.
 *  - h017 naming             : patrn1 keeps the vendor name.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The header comment reads "pattern argument [right side) set up" -
 *    mismatched bracket kept in the comment only.
 *  - The a1281 block stores tmp1[0] = xd[*jpx] unconditionally (even
 *    when the following test is skipped at NUL); the port keeps the
 *    store before the guard, same as the vendor.
 */
fun patrn1(jpx: ShortArray, ernbr: ShortArray, svPtr: Msv) {
    val tmp1 = CharArray(2) // vendor: unsigned char tmp1[2] = { 0, 0 }
    val cod210 = charArrayOf(210.toChar(), '\u0000') // vendor: const static {210, 0}
    val xset = CharArray(20) // fresh scratch for the literal arg
    val literal = " ,&:!''><=[)+-*/#_"
    for (i in literal.indices) xset[i] = literal[i]
    xset[literal.length] = '\u0000'

    outer@ while (true) {
        // a1285
        val j = jpx[0].toInt()

        if (svPtr.xd[j] == '"') {
            strcatCS(svPtr.bd, "\"")

            while (true) { // a1283
                jpx[0] = (jpx[0] + 1).toShort()
                val k = jpx[0].toInt()

                if (svPtr.xd[k] == '\u0000') {
                    ernbr[0] = 2
                    return
                }

                if (svPtr.xd[k] == '"') {
                    if (svPtr.xd[k + 1] != '"') break // goto a1281
                    strcatCC(svPtr.bd, cod210)
                    jpx[0] = (jpx[0] + 1).toShort()
                } else {
                    tmp1[0] = svPtr.xd[k]
                    strcatCC(svPtr.bd, tmp1)
                }
                // goto a1283
            }
        }

        // a1281
        val k = jpx[0].toInt()
        tmp1[0] = svPtr.xd[k] // vendor stores before the guard

        if (svPtr.xd[k] != '\u0000') {
            if (xindex(xset, tmp1, 1) == 0) {
                strcatCC(svPtr.bd, tmp1)
                jpx[0] = (jpx[0] + 1).toShort()
                continue@outer // goto a1285
            }
        }

        svPtr.xpx = jpx[0] - 1 // stateVector.h:56 int xpx (redundant .toInt() dropped, u30 warning fix folded into u31 push)
        ernbr[0] = 0
        return
    }
}

/** C strcat((char*)dst, s) - appends s to the c-string at dst[0]. */
private fun strcatCS(dst: CharArray, s: String) {
    var e = 0
    while (dst[e] != '\u0000') e++
    for (i in s.indices) {
        dst[e] = s[i]
        e++
    }
    dst[e] = '\u0000'
}

/** C strcat((char*)dst, (char*)src) - appends the c-string src. */
private fun strcatCC(dst: CharArray, src: CharArray) {
    var e = 0
    while (dst[e] != '\u0000') e++
    var i = 0
    while (src[i] != '\u0000') {
        dst[e] = src[i]
        e++
        i++
    }
    dst[e] = '\u0000'
}
