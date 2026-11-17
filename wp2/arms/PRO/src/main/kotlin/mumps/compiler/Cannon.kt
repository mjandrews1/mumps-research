package mumps.compiler

import mumps.interp.NUL
import mumps.interp.atoiC
import mumps.interp.mult

/**
 * PRO-arm translation of compiler-sys1.c::cannon (B3 unit 72, rank
 * 224). Translated independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), reference
 * source lines 971-1052 (definition; prototype compiler-sys1.c:63).
 *
 * Identity verified against the study tool (tools/select_study_
 * subset.py): compiler-sys1.c::cannon, c, loc 71, comment_ratio
 * 0.014, pointer_marks 0, control_marks 27, score 40.9, batch 3 -
 * exact match to wp1/study-subset/units.csv row 224.
 *
 * cannon(a): canonicalizes the numeric string in place - strips
 * leading +/- signs (tracking net sign), trailing zeros, leading
 * zeros, and rewrites E-exponents by repeated multiplication
 * (mult(a,"10",a) / mult(a,".1",a)).
 *
 * This unit RESOLVES the compiler-side cannon h014 stub raised by
 * unit 68 (removed from Stubs.kt; the definition lives here). The
 * interp twin (interp-parse.cpp:215 cannon) is owned by the
 * interp-parse.cpp::<misc> unit (B3 unit 75's residue family) -
 * separately owned, not resolved here.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h007 char_buffer       : tmp[25] exponent scratch; the "10"
 *    and ".1" multiply operands are fresh CharArray literals.
 *  - h012 unsigned_byte_domain: a/tmp byte domains.
 *  - h014 out_of_scope_call : mult is the interp-side real
 *    implementation (B3 unit 66, ArithMisc.kt) - reused via
 *    package import (the vendor compiler links the same library
 *    function); atoi -> atoiC; strcpy/strncpy -> local
 *    offset-aware helpers.
 *  - h015 grammar_collision : pre-push scan incl. cross-file
 *    top-level names: cannon moves from Stubs.kt (stub removed
 *    there) to this owner file - no collisions.
 *  - h017 naming            : cannon keeps the vendor name.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The dangling-else at the exponent multiply (vendor lines
 *    1013-1016): "if (i > 0) if (j == 0) for ... mult(a,"10",a);
 *    else for ... mult(a,".1",a);" - the else binds to the inner
 *    if, so j==1 (negative exponent) takes the .1 branch; kept with
 *    explicit braces.
 *  - The leading-zero strip "while (a[j] == '0') strcpy(&a[j],
 *    &a[j+1]);" shifts the string left one byte at a time - kept.
 *  - i is NOT reset between the sign loop and the digit scan - the
 *    digit scan starts right after the signs; kept.
 *  - The backscan "for (i--; a[i] == '0'; i--) a[i] = 0;" can reach
 *    index 0 and relies on the '.' stop; at i==0 the whole string
 *    collapses to "0" - kept (no underflow for well-formed inputs;
 *    vendor reads a[-1] only on malformed ones).
 */
fun cannon(a: CharArray) {
    val tmp = CharArray(25)
    var i: Int
    var j: Int

    i = 0
    j = 0
    while (a[i] == '+' || a[i] == '-') {
        if (a[i] == '-') {
            if (j == 0) j = 1 else j = 0
        }
        i++
    }

    if (i > 0) {
        if (j == 0) strcpyOff(a, 0, a, i)
        else {
            a[0] = '-'
            if (i > 1) strcpyOff(a, 1, a, i)
        }
    }

    j = -1
    while (a[i] == '.' || (a[i] >= '0' && a[i] <= '9')) {
        if (a[i] == '.') j = i
        i++
    }

    if (i == 0) {
        a[0] = '0'
        a[1] = NUL
        return
    }

    if (a[i] != 'E' && a[i] != 'e') a[i] = NUL

    if (a[i] == NUL) {
        if (j >= 0) {
            i--
            while (a[i] == '0') {
                a[i] = NUL
                i--
            }
            if (a[i] == '.') a[i] = NUL
            if (i == 0) {
                a[0] = '0'
                a[1] = NUL
            }
        }

        if (a[0] == '-') j = 1 else j = 0

        while (a[j] == '0') strcpyOff(a, j, a, j + 1)

        if (a[j] == NUL) {
            a[0] = '0'
            a[1] = NUL
        }

        if (a[j] == '.' && a[j + 1] == NUL) a[j] = '0'
        return
    }

    if (a[i] != 'e' && a[i] != 'E') return

    a[i] = NUL
    i++
    if (a[i] == '-') {
        i++
        j = 1
    } else {
        j = 0
        if (a[i] == '+') i++
    }

    var k = 0
    while (true) {
        tmp[k] = a[i]
        if (tmp[k] == NUL || !(tmp[k] >= '0' && tmp[k] <= '9')) break
        k++
        i++
    }

    tmp[k] = NUL
    i = atoiC(tmp)
    if (i > 0) {
        val ten = charArrayOf('1', '0', NUL)
        val tenth = charArrayOf('.', '1', NUL)
        if (j == 0) {
            for (t in 1..i) mult(a, 0, ten, 0, a, 0)
        } else {
            for (t in 1..i) mult(a, 0, tenth, 0, a, 0)
        }
    }
    return
}

/** C strcpy(dst+dstOff, src+srcOff) - overlapping shifts left. */
private fun strcpyOff(dst: CharArray, dstOff: Int, src: CharArray, srcOff: Int) {
    var p = srcOff
    var e = dstOff
    while (src[p] != NUL) {
        dst[e] = src[p]
        e++
        p++
    }
    dst[e] = NUL
}
