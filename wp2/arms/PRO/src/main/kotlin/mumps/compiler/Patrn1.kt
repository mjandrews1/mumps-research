package mumps.compiler

import mumps.interp.xindex

/**
 * PRO-arm translation of compiler-sys1.c::patrn1 (pilot B1, unit 26).
 * Translated independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), reference source
 * lines 742-790 (vendor K&R-style definitions, line 743 comment).
 *
 * patrn1(jpx, ernbr): "pattern argument (right side) set up" - scans
 * xd from *jpx, appending the pattern text into bd[1..] (with the 210
 * marker for doubled quotes), until the pattern ends; *jpx is left
 * one before the first char not consumed; *ernbr gets 2 (unterminated
 * quote) or 0 (normal end).
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h002 simple_output_macro : strcat(&bd[1], ...) libc calls ->
 *    local offset-aware helpers (strcatOff/strcatOffC) preserving the
 *    C-string append at &bd[1]; xindex's literal arg becomes a fresh
 *    CharArray.
 *  - h004 globals_ext        : file-scope xd, bd, xpx -> CompilerState
 *    (bd added with this unit, compiler-globals-ext.h:33, 2048).
 *  - h006 out_param_holder   : vendor short *jpx, *ernbr ->
 *    ShortArray(1) holders (both are read AND written by the callee).
 *  - h008 static_scratch     : vendor's static unsigned char tmp1[2],
 *    cod210[2] = {210,0} -> fresh per-call buffers.
 *  - h011 goto               : backward gotos a1285/a1283 become a
 *    labeled outer loop and an inner while; the forward goto a1281
 *    (exit the quote scan, fall into the terminator check) becomes a
 *    plain break out of the inner loop.
 *  - h012 unsigned_byte_domain: xd/bd/tmp1 byte domains; cod210 holds
 *    Char(210).
 *  - h014 out_of_scope_call  : xindex (InterpStubs stub, owner
 *    interp-strmanip.cpp::xindex, not a corpus unit).
 *  - h015 grammar_collision  : pre-push scan incl. cross-file top-level
 *    names (new top-level: patrn1 + two private helpers - no package
 *    collisions). The xindex charset literal contains the chars '*' and
 *    '/' adjacent - inside a Kotlin string literal, not a comment.
 *  - h017 naming             : patrn1 keeps the vendor name (lowercase
 *    in the vendor).
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The paren-depth counter m is file-body-local (int m=0) and is
 *    not reset between scan iterations - only the initial assignment
 *    is per-call; the Kotlin local keeps that meaning.
 *  - *jpx is advanced before each quote-body char check; the doubled
 *    quote escape ("" -> 210 marker + skip) is preserved exactly.
 *  - The m!=0 test suppresses the charset terminator check inside
 *    parens, so the pattern runs to the matching close paren.
 */
fun patrn1(jpx: ShortArray, ernbr: ShortArray) {
    var m = 0
    val tmp1 = CharArray(2) // h008: vendor static unsigned char tmp1[2]
    val cod210 = charArrayOf(210.toChar(), '\u0000') // h008: {210,0}
    val xset = CharArray(20) // h008: fresh scratch for the literal arg
    val literal = " ,&:!''><=[)+-*/#_"
    for (i in literal.indices) xset[i] = literal[i]
    xset[literal.length] = '\u0000'

    outer@ while (true) {
        // a1285
        val j = jpx[0].toInt()

        if (CompilerState.xd[j] == '"') {
            strcatOff(CompilerState.bd, 1, "\"")

            while (true) { // a1283
                jpx[0] = (jpx[0] + 1).toShort()
                val k = jpx[0].toInt()

                if (CompilerState.xd[k] == '\u0000') {
                    ernbr[0] = 2
                    return
                }

                if (CompilerState.xd[k] == '"') {
                    if (CompilerState.xd[k + 1] != '"') break // goto a1281
                    strcatOffC(CompilerState.bd, 1, cod210)
                    jpx[0] = (jpx[0] + 1).toShort()
                } else {
                    tmp1[0] = CompilerState.xd[k]
                    strcatOffC(CompilerState.bd, 1, tmp1)
                }
                // goto a1283
            }
        }

        // a1281
        val k = jpx[0].toInt()
        if (CompilerState.xd[k] != '\u0000') {
            tmp1[0] = CompilerState.xd[k]
            if (m != 0 || xindex(xset, tmp1, 1) == 0) {
                if (CompilerState.xd[k] == '(') m++
                if (CompilerState.xd[k] == ')') m--
                strcatOffC(CompilerState.bd, 1, tmp1)
                jpx[0] = (jpx[0] + 1).toShort()
                continue@outer // goto a1285
            }
        }

        CompilerState.xpx = (jpx[0] - 1).toLong()
        ernbr[0] = 0
        return
    }
}

/** C strcat(&dst[off], s) - appends s to the c-string at dst+off. */
private fun strcatOff(dst: CharArray, off: Int, s: String) {
    var e = off
    while (dst[e] != '\u0000') e++
    for (i in s.indices) {
        dst[e] = s[i]
        e++
    }
    dst[e] = '\u0000'
}

/** C strcat(&dst[off], src) - appends the c-string src at dst+off. */
private fun strcatOffC(dst: CharArray, off: Int, src: CharArray) {
    var e = off
    while (dst[e] != '\u0000') e++
    var i = 0
    while (src[i] != '\u0000') {
        dst[e] = src[i]
        e++
        i++
    }
    dst[e] = '\u0000'
}
