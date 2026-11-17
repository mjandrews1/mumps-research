package mumps.interp

/**
 * PRO-arm translation of interp-parse.cpp::parse_ (pilot B3, unit 63,
 * rank 303, provisional under deviation C-003). Translated
 * independently from the pristine vendor source (mumps-14-may-2026.tgz,
 * sha256 621deb25...ffdbe), reference source lines 260-843 (study tool:
 * loc 584, score 1110.0, exact units.csv match).
 *
 * parse_(svPtr): the interpreter's recursive-descent expression
 * parser/evaluator over svPtr.xd - operands, operators, coded
 * parens/comma/colon (206/207/208/209), globals and dollar-functions
 * via the Mglobal/BuildGlobal/BuildLocal machinery, symbol lookup via
 * sym_, and binary/unary operator application over the pd1 stack.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h011 goto : the vendor's label web (start/nchar/scan_operand/
 *    idxin/var1/un_nest/sym1/exec/nxt_expr/nxt_operator/dec_stk/
 *    finish) becomes an explicit state machine over a private enum -
 *    each vendor label is one state, each goto a state assignment.
 *  - h012 unsigned_byte_domain : xd/bd/v1d/pd1 byte domains; the four
 *    const static 256-entry tables are 256-entry int arrays indexed
 *    by Char.code (values transcribed programmatically from the
 *    vendor initializer; C partial initialization zero-fills 128-255,
 *    kept exactly); s1p is the 40-entry token stack.
 *  - h010 promotions : vendor short locals (ernbr,f,spx,adx,jpx,j,i,g)
 *    are Int; s1p codes are small non-negative values.
 *  - h008 static_scratch : the const static code/opcode/ncode/dcode/
 *    operand tables and cod209 become private top-level vals (one
 *    instance, as const static); tmp3 is per-call scratch.
 *  - h014 out_of_scope_call : strmove (interp-strmanip.cpp::strmove,
 *    not a corpus unit), mult/add/sub/divx/divi (interp-arith.cpp, not
 *    corpus units), expx (batch-5 unit), modulo (batch-6 unit), zfcn
 *    (batch-6 unit), sym_ (batch-10 unit), BuildGlobal (batch-8 unit),
 *    BuildLocal (batch-8 unit), rpm (interp-rpm.cpp::rpm, not a
 *    corpus unit) - all InterpStubs owner-named stubs added with this
 *    unit; patrn1, numcomp, xindex, fcn, mglobal are existing arm
 *    members (patrn1/numcomp real, xindex/fcn/mglobal stubs).
 *  - h015 grammar_collision : the vendor's dollar char literals are
 *    36.toChar() (dollar byte kept out of this file, prnterr
 *    precedent); private helpers below avoid cross-file collisions.
 *  - h017 naming : parse_ keeps the vendor name.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - tmp1[25] and tmp2[4] are declared and never used (h013 drop).
 *  - CONTAINS: the first test reads "pd1[adx] == 0 && bp == 0" - bp
 *    is &bd[1] and never a null pointer, so the arm is provably dead
 *    (h013) and dropped.
 *  - A mid-function prototype "int rpm(...)" sits inside the operator
 *    switch between FOLLOWS and PATTERN (kept as a comment).
 *  - NOT_NOT and NOTEQ share the value 18 (interp.h:82-83); the
 *    second switch has no NOT_NOT label, so 18 falls to default there
 *    - rendered exactly.
 *  - h018 (preprocessor_conditional): the reference build is SQLITE,
 *    so the dollar-data child check takes the SQLITE arm (vendor
 *    lines 529-541); the non-SQLITE arm is documented as the excluded
 *    comment.
 */

// --- interp.h token codes (lines 64-95); private to this file ---

private const val POPEN = 0
private const val DIVIDE = 1
private const val MULTIPLY = 2
private const val MINUS = 3
private const val PLUS = 4
private const val OPERAND = 5
private const val OPENC = 6
private const val CONCAT = 7
private const val EQUALS = 8
private const val GREATER = 9
private const val PEMPTY = 10
private const val LESSTHAN = 11
private const val NOT = 12
private const val INTDIVIDE = 13
private const val MODULO = 14
private const val CONTAINS = 15
private const val FOLLOWS = 16
private const val PATTERN = 17
private const val NOTEQ = 18
// vendor interp.h:82-83: NOT_NOT aliases NOTEQ's value 18 (the
// unary-not switch uses NOT_NOT as the token label)
private const val NOT_NOT = 18
private const val NOTGREATER = 19
private const val NOTLESS = 20
private const val NOTCONTAINS = 21
private const val NOTFOLLOWS = 22
private const val NOTPATTERN = 23
private const val AND = 24
private const val OR = 25
private const val NOTAND = 26
private const val NOTOR = 27
private const val INDIRECT = 28
private const val EXPONEN = 29

// (build fix) OPEN_PAREN was REMOVED here: the package-level
// OPEN_PAREN (InterpInterpMisc.kt, == CodedOpen 206) is the single
// definition now (conflicting declarations, cleanLocks precedent).
// interp.h:63
private const val QUOTEC = '\''
// interp.h:56
private const val TABC = '\t'
// vendor dollar-char literals, h015 dollar-byte rule
private val DOLLARC = 36.toChar()

// cod209 (vendor line 272): { 209, 0 }
private val cod209 = charArrayOf(209.toChar(), NUL)

// const static tables (vendor lines 273-350) - programmatic
// transcription; 256 declared, 128 initialized, 128-255 zero-filled.

private val code = intArrayOf(
    99, 99, 99, 99, 99, 99, 99, 99, 18, 19, 99, 20, 18, 99, 99, 21, 22, 23, 99, 99, 99, 99, 99, 99, 26, 27
)

private val opcode = intArrayOf(
    99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99,
    99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99,
    99, 25, 99, 14, 99, 99, 24, 12, 0, 99, 2, 4, 99, 3, 99, 1,
    99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 11, 8, 9, 17,
    28, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99,
    99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 15, 13, 16, 99, 7,
    99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99,
    99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99,
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
)

private val ncode = intArrayOf(
    99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99,
    99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99,
    99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 10, 99,
    10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 99, 99, 99, 99, 99, 99,
    99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99,
    99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99,
    99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99,
    99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99,
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
)

private val dcode = intArrayOf(
    10, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99,
    99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99,
    10, 10, 99, 10, 99, 99, 10, 10, 99, 10, 10, 10, 10, 10, 99, 10,
    99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 10, 99, 10, 10, 10, 10,
    10, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99,
    99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 10, 10, 10, 10, 10,
    99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99,
    99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99,
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
)

private val operand = intArrayOf(
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    0, 2, 1, 0, 1, 1, 2, 0, 0, 0, 0, 0, 0, 0, 1, 0,
    1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 2, 2, 2, 2,
    0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
    1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 0, 2, 1, 0,
    0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
    1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
)
// --- the parse_ state machine (vendor labels -> states) ---

private enum class PState { START, NCHAR, SCAN_OPERAND, VAR1, UN_NEST, SYM1, EXEC, NXT_EXPR, NXT_OPERATOR, NXT_OPERATOR_U, DEC_STK, FINISH }

// vendor CodedOpen/CodedClose/CodedComma (defines.h:38-40)
private val COD_OPEN = 206.toChar()
private val COD_CLOSE = 207.toChar()
private val COD_COMMA = 208.toChar()
// vendor SymRetrieve/SymNext/SymData/SymName (defines.h:44-50),
// RETRIEVE/NEXT (defines.h:168 area), ORDERNEXT/ORDERPREV (170-171)
private const val SYM_RETRIEVE = 1
private const val SYM_NEXT = 3
private const val SYM_DATA = 6
private const val SYM_NAME = 10
private const val RETRIEVE_C = 0
private const val NEXT_C = 2
private const val ORDERNEXT_C = 12
private const val ORDERPREV_C = 13

/** strcpy(dst+dstOff, src+srcOff) over the byte domain. */
private fun strcpyAC(dst: CharArray, dstOff: Int, src: CharArray, srcOff: Int) {
    var k = dstOff
    var s = srcOff
    while (true) {
        val c = src[s]
        dst[k] = c
        if (c == NUL) break
        k++; s++
    }
}

/** strcat(dst+dstOff, src+srcOff): appends the src cstring. */
private fun strcatAC(dst: CharArray, dstOff: Int, src: CharArray, srcOff: Int) {
    var k = dstOff + strlenC(dst, dstOff)
    var s = srcOff
    while (true) {
        val c = src[s]
        dst[k] = c
        if (c == NUL) break
        k++; s++
    }
}

/** strcmp over two cstrings at offsets (byte/strcmp order). */
private fun strcmpAC(a: CharArray, aOff: Int, b: CharArray, bOff: Int): Int {
    var i = aOff
    var j = bOff
    while (true) {
        if (a[i] != b[j]) return a[i].code - b[j].code
        if (a[i] == NUL) return 0
        i++; j++
    }
}

/** copy the cstring at src+off into a fresh NUL-terminated array. */
private fun copyOfC(src: CharArray, off: Int): CharArray {
    val n = strlenC(src, off)
    val out = CharArray(n + 1)
    System.arraycopy(src, off, out, 0, n + 1)
    return out
}

/** numcomp(a+ aOff, b + bOff) via the real numcomp on fresh copies. */
private fun numcmpAC(a: CharArray, aOff: Int, b: CharArray, bOff: Int): Int =
    numcomp(copyOfC(a, aOff), copyOfC(b, bOff))

/** vendor toupper over the byte domain (ASCII). */
private fun toupperC(c: Char): Char = c.uppercaseChar()

/** vendor isalpha over the byte domain (ASCII). */
private fun isAlphaC(c: Char): Boolean =
    (c.code in 65..90) || (c.code in 97..122)

/** vendor isalnum over the byte domain (ASCII). */
private fun isAlnumC(c: Char): Boolean = isAlphaC(c) || (c.code in 48..57)

fun parse_(svPtr: Msv): Int {
    // vendor locals; short -> Int (h010)
    var ernbr: Int
    var f: Int
    var spx: Int
    var adx: Int
    var jpx: Int
    var j: Int
    var i: Int
    var g: Int
    var bbtyp: Char
    var nxtchr: Char
    var FcnArgToggle = 0
    val s1p = IntArray(40) // vendor unsigned char s1p[40]
    val tmp3 = CharArray(32000) // vendor tmp3[STR_MAX]; tmp1[25]/tmp2[4] dead (h013)
    val zeroC = charArrayOf('0', NUL) // vendor literal "0" for numcomp
    val jpxH = ShortArray(1) // patrn1 out-holders (h006, Patrn1.kt precedent)
    val ernbrH = ShortArray(1)
    val bOff = 1 // bp = &svPtr->bd[1]
    val vOff = 1 // v1dp = &svPtr->v1d[1]

    // prologue (vendor lines 88-96)
    svPtr.bd[0] = NUL
    svPtr.xpx--
    spx = 0
    svPtr.pd1[(svPtr.pd1len + 2).toInt()] = NUL
    svPtr.pd1[(svPtr.pd1len + 3).toInt()] = NUL
    svPtr.sdlim = (svPtr.pd1len + 3).toInt()

    var state = PState.START
    machine@ while (true) {
        when (state) {

        PState.START -> {
            // start:
            spx++
            s1p[spx] = PEMPTY // stack foundation
            state = PState.NCHAR
        }

        PState.NCHAR -> {
            // nchar:
            svPtr.xpx++
            nxtchr = svPtr.xd[svPtr.xpx]
            if (nxtchr == NUL) { state = PState.FINISH; continue@machine }

            if (operand[nxtchr.code] == 1) { state = PState.SCAN_OPERAND; continue@machine }

            if (nxtchr == COD_OPEN) {
                spx++
                while (svPtr.pd1[svPtr.sdlim] != NUL) svPtr.sdlim++
                svPtr.sdlim++ // while (pd1[sdlim++]);
                // p1 = &v1d[1]; p2 = &pd1[sdlim]; while ((*p2++ = *p1++));
                var p1 = vOff
                var p2 = svPtr.sdlim
                while (true) {
                    val c = svPtr.v1d[p1]
                    svPtr.pd1[p2] = c
                    p1++; p2++
                    if (c == NUL) break
                }
                svPtr.pd1[p2] = NUL // *p2 = 0
                p2--
                svPtr.pd1[p2] = COD_OPEN // *p2 = CodedOpen
                s1p[spx] = OPENC
                state = PState.NCHAR
                continue@machine
            }

            if (nxtchr == ',') {
                if (s1p[spx - 1] != OPENC) {
                    if (svPtr.t2 > 0) { state = PState.FINISH; continue@machine }
                    return 16
                }
                // concatenate stack tops and add coded comma
                svPtr.sdlim-- // for (i = --sdlim; ...)
                i = svPtr.sdlim
                while (true) {
                    svPtr.pd1[i] = svPtr.pd1[i + 1]
                    if (svPtr.pd1[i] == NUL) break
                    i++
                }
                svPtr.pd1[i] = COD_COMMA
                svPtr.pd1[i + 1] = NUL // coded comma
                svPtr.sdlim--
                while (svPtr.pd1[svPtr.sdlim] != NUL) svPtr.sdlim--
                svPtr.sdlim-- // while (pd1[sdlim--]);
                svPtr.sdlim += 2
                spx-- // stack pointer
                state = PState.NCHAR
                continue@machine
            }

            if (nxtchr == ')') {
                if (s1p[spx - 1] != POPEN) {
                    if (s1p[spx - 1] != OPENC) return 16

                    // fcn/array return section
                    svPtr.sdlim--
                    i = svPtr.sdlim
                    while (true) {
                        svPtr.pd1[i] = svPtr.pd1[i + 1]
                        if (svPtr.pd1[i] == NUL) break
                        i++
                    }
                    // join tops
                    svPtr.pd1[i] = COD_CLOSE
                    svPtr.pd1[i + 1] = NUL // coded close
                    svPtr.sdlim--
                    while (svPtr.pd1[svPtr.sdlim] != NUL) svPtr.sdlim--
                    svPtr.sdlim-- // while (pd1[sdlim--]);
                    svPtr.sdlim += 2
                    spx-- // stack pointer
                    state = PState.UN_NEST
                    continue@machine
                }

                // precedence close paren
                if (s1p[spx] != OPERAND) return 12

                // extract value from stack top
                strmove(svPtr.bd, bOff, svPtr.pd1, svPtr.sdlim)
                do {
                    svPtr.sdlim--
                } while (svPtr.pd1[svPtr.sdlim - 1] != NUL) // compress stack
                spx -= 2
                state = PState.NXT_OPERATOR
                continue@machine
            }

            if (nxtchr == ':') {
                i = svPtr.sdlim
                do {
                    i--
                } while (svPtr.pd1[i - 1] != NUL)

                if (svPtr.pd1[i] != DOLLARC ||
                    (svPtr.pd1[i + 1] != 's' && svPtr.pd1[i + 1] != 'S')
                ) {
                    state = PState.FINISH
                    continue@machine
                }

                svPtr.sdlim--
                strmove(svPtr.pd1, svPtr.sdlim, svPtr.pd1, svPtr.sdlim + 1)

                while (svPtr.pd1[svPtr.sdlim] != NUL) svPtr.sdlim--
                svPtr.sdlim-- // while (pd1[sdlim--]);

                svPtr.sdlim += 2
                strcatAC(svPtr.pd1, svPtr.sdlim, cod209, 0)
                spx--
                state = PState.NCHAR
                continue@machine
            }

            if (nxtchr == ' ' || nxtchr == TABC) {
                state = PState.FINISH
                continue@machine
            }

            if (nxtchr == '^' && svPtr.t2 == 2 && s1p[spx] == OPERAND) {
                state = PState.FINISH
                continue@machine
            }

            // check for delimiters in list
            spx++

            if (nxtchr == QUOTEC) {
                // insert not based operator code
                svPtr.xpx++
                s1p[spx] = opcode[svPtr.xd[svPtr.xpx].code]

                if (s1p[spx] != 99) {
                    s1p[spx] = code[opcode[svPtr.xd[svPtr.xpx].code]]

                    // patch for ']]
                    if (svPtr.xd[svPtr.xpx + 1] == ']') svPtr.xpx++

                    if (s1p[spx] != 99) {
                        state = PState.NCHAR
                        continue@machine
                    }
                }
                svPtr.xpx--
            }

            if (nxtchr == '*' && svPtr.xd[svPtr.xpx + 1] == '*') {
                // exponentiation
                svPtr.xpx++
                s1p[spx] = EXPONEN
                state = PState.NCHAR
                continue@machine
            }

            if (nxtchr == ']' && svPtr.xd[svPtr.xpx + 1] == ']') {
                // collates after - becomes follows
                svPtr.xpx++
                s1p[spx] = FOLLOWS
                state = PState.NCHAR
                continue@machine
            }

            // insert code
            s1p[spx] = opcode[nxtchr.code]

            if (s1p[spx] != 99) {
                state = PState.NCHAR
                continue@machine
            }

            svPtr.ErrChar = nxtchr
            return 12
        }

        PState.SCAN_OPERAND -> {
            // scan_operand:
            jpx = svPtr.xpx

            if (s1p[spx] == NOTPATTERN || s1p[spx] == PATTERN) {
                svPtr.bd[1] = NUL
                jpxH[0] = jpx.toShort()
                patrn1(jpxH, ernbrH, svPtr)
                ernbr = ernbrH[0].toInt()

                if (ernbr != 0) return ernbr

                state = PState.EXEC
                continue@machine
            }

            // numeric operand
            if (ncode[svPtr.xd[svPtr.xpx].code] == 10) {
                if (svPtr.t0px > 0 && svPtr.xd[svPtr.xpx] == '.' &&
                    (isAlphaC(svPtr.xd[svPtr.xpx + 1]) ||
                        svPtr.xd[svPtr.xpx + 1] == '_' ||
                        svPtr.xd[svPtr.xpx + 1] == '%')
                ) {
                    svPtr.bd[1] = '.'
                    svPtr.xpx++

                    i = 2
                    while (true) {
                        svPtr.bd[i] = svPtr.xd[svPtr.xpx]
                        if (!isAlnumC(svPtr.bd[i])) break
                        i++
                        svPtr.xpx++
                    }

                    svPtr.bd[i] = NUL
                    svPtr.xpx--
                    state = PState.EXEC
                    continue@machine
                }

                // remove leading zeros
                while (svPtr.xd[svPtr.xpx] == '0') svPtr.xpx++

                if (ncode[svPtr.xd[svPtr.xpx].code] != 10) svPtr.xpx-- // all zeros

                i = 1
                while (true) {
                    svPtr.bd[i] = svPtr.xd[svPtr.xpx]
                    if (svPtr.bd[i] == NUL || ncode[svPtr.bd[i].code] != 10) break
                    i++
                    svPtr.xpx++
                }

                if (i == 1 && svPtr.bd[1] == '.') return 9

                svPtr.bd[i] = NUL
                svPtr.xpx--
                state = PState.EXEC
                continue@machine
            }

            // literal operand
            if (svPtr.xd[svPtr.xpx] == '"') {
                j = 0

                while (true) {
                    while (true) {
                        svPtr.xpx++
                        if (svPtr.xd[svPtr.xpx] == '"') break
                        j++
                        svPtr.bd[j] = svPtr.xd[svPtr.xpx]
                        if (svPtr.bd[j] == NUL) return 2
                    }

                    if (svPtr.xd[svPtr.xpx + 1] != '"') {
                        j++
                        svPtr.bd[j] = NUL
                        state = PState.EXEC
                        continue@machine
                    }

                    svPtr.xpx++
                    j++
                    svPtr.bd[j] = '"'
                }
            }

            // variable name
            svPtr.v1d[1] = svPtr.xd[svPtr.xpx]
            svPtr.xpx++
            j = 2

            var skipDcode: Boolean
            while (true) {
                skipDcode = false
                // indirect name and indirect indices case @x@(1,1)
                if (svPtr.xd[svPtr.xpx] == '@' &&
                    svPtr.xd[svPtr.xpx + 1] == '(' &&
                    s1p[spx] != INDIRECT
                ) {
                    // strcpy(&xd[xpx], &xd[xpx+1]): overlapping left shift
                    var k = svPtr.xpx
                    while (true) {
                        val c = svPtr.xd[k + 1]
                        svPtr.xd[k] = c
                        if (c == NUL) break
                        k++
                    }
                    skipDcode = true
                }

                if (!skipDcode) {
                    if (dcode[svPtr.xd[svPtr.xpx].code] == 10) {
                        svPtr.v1d[j] = NUL
                        svPtr.xpx--
                        state = PState.VAR1
                        break
                    }
                }

                // idxin:
                svPtr.v1d[j] = svPtr.xd[svPtr.xpx]
                j++

                if (svPtr.xd[svPtr.xpx] == '(') {
                    j--
                    svPtr.v1d[j] = NUL
                    svPtr.xd[svPtr.xpx] = COD_OPEN
                    svPtr.xpx--
                    state = PState.START // goto start /* recurse */
                    break
                }

                svPtr.xpx++
            }
            continue@machine
        }

        PState.UN_NEST -> {
            // un_nest: copy answer from stack
            while (true) {
                svPtr.sdlim--
                if (svPtr.pd1[svPtr.sdlim] == NUL) break
            }
            strmove(svPtr.v1d, vOff, svPtr.pd1, svPtr.sdlim + 1)

            while (true) {
                svPtr.sdlim--
                if (svPtr.pd1[svPtr.sdlim] == NUL) break
            }
            svPtr.sdlim++
            spx -= 2
            state = PState.VAR1
        }

        PState.VAR1 -> {
            // var1:
            if (svPtr.t0px == 1 && spx == 1) {
                spx--
                svPtr.t0px = 0
                return 0
            }

            // dollar-piece interface
            if (svPtr.pd1[svPtr.sdlim] == DOLLARC && svPtr.t0px == 1 &&
                (svPtr.pd1[svPtr.sdlim + 1] == 'P' || svPtr.pd1[svPtr.sdlim + 1] == 'p')
            ) {
                if (svPtr.setname[0] == NUL) strmove(svPtr.setname, 0, svPtr.v1d, vOff)
            }

            // dollar-extract interface
            if (svPtr.pd1[svPtr.sdlim] == DOLLARC && svPtr.t0px == 1 &&
                (svPtr.pd1[svPtr.sdlim + 1] == 'E' || svPtr.pd1[svPtr.sdlim + 1] == 'e')
            ) {
                if (svPtr.setname[0] == NUL) {
                    strmove(svPtr.setname, 0, svPtr.v1d, vOff)
                }
            }

            if (svPtr.v1d[1] == '^') { // global var
                g = RETRIEVE_C

                // NO EVAL GLOBAL ZZ INTERFACE
                if (toupperC(svPtr.pd1[svPtr.sdlim + 1]) == 'Z' &&
                    toupperC(svPtr.pd1[svPtr.sdlim + 2]) == 'Z'
                ) {
                    var k = vOff
                    while (svPtr.v1d[k] != NUL) {
                        if (svPtr.v1d[k].code > 127) svPtr.v1d[k] = 1.toChar()
                        k++
                    }
                    strcpyAC(svPtr.bd, bOff, svPtr.v1d, vOff)
                    state = PState.EXEC
                    continue@machine
                }

                if (svPtr.pd1[svPtr.sdlim] == DOLLARC && s1p[spx] != INDIRECT) {
                    FcnArgToggle++
                    FcnArgToggle %= 2

                    // some global references must be passed as references
                    // to subroutines without evaluation: hidden in a static
                    // array behind the <gbl> token (dollar-name/-query/
                    // -qlength/-qsubscript handling; the second toupper
                    // test is the vendor's own tautology, kept)
                    if ((toupperC(svPtr.pd1[svPtr.sdlim + 1]) == 'N' &&
                            toupperC(svPtr.pd1[svPtr.sdlim + 1]) != 'A') ||
                        toupperC(svPtr.pd1[svPtr.sdlim + 1]) == 'Q'
                    ) {
                        g = NEXT_C
                        // hide global for later extraction in BuildGlobal
                        BuildGlobal(0, g, svPtr.v1d, vOff, null, svPtr)
                        strcpyC(svPtr.bd, "<gbl>", bOff)
                        state = PState.EXEC
                        continue@machine
                    }

                    // dollar-ORDER FCN
                    if (toupperC(svPtr.pd1[svPtr.sdlim + 1]) == 'O') {
                        var k = svPtr.sdlim
                        while (svPtr.pd1[k] != NUL) k++
                        k--

                        if (svPtr.pd1[k] == OPEN_PAREN.toChar()) { // first argument (build fix: OPEN_PAREN is the Int 206 token code; the pd1 byte domain holds the Char form)
                            strcpyAC(svPtr.bd, bOff, svPtr.v1d, vOff)
                            var m = bOff
                            while (svPtr.bd[m] != NUL) {
                                if (svPtr.bd[m].code > 127) svPtr.bd[m] = 1.toChar()
                                m++
                            }
                            state = PState.EXEC
                            continue@machine
                        } else {
                            f = mglobalAt(g, svPtr.v1d, vOff, svPtr.bd, bOff, svPtr)
                            state = PState.EXEC
                            continue@machine
                        }
                    }

                    if (toupperC(svPtr.pd1[svPtr.sdlim + 1]) == 'B') {
                        g = ORDERPREV_C
                        BuildGlobal(0, g, svPtr.v1d, vOff, null, svPtr)
                        strcpyC(svPtr.bd, "<gbl>", bOff)
                        state = PState.EXEC
                        continue@machine
                    }
                }

                f = mglobalAt(g, svPtr.v1d, vOff, svPtr.bd, bOff, svPtr)

                // fix dollar-order end string
                if (toupperC(svPtr.pd1[svPtr.sdlim + 1]) == 'O') {
                    if (strcmpAC(svPtr.bd, bOff, charArrayOf('-', '1', NUL), 0) == 0) {
                        svPtr.bd[1] = NUL // empty
                    }
                }

                // check for dollar-data
                if (svPtr.pd1[svPtr.sdlim] == DOLLARC && s1p[spx] != INDIRECT &&
                    (svPtr.pd1[svPtr.sdlim + 1] == 'D' ||
                        svPtr.pd1[svPtr.sdlim + 1] == 'd')
                ) {
                    bbtyp = if (f == 0) '0' else '1'

                    i = strlenC(svPtr.v1d, vOff)

                    // h018: SQLITE is the reference build arm; the
                    // non-SQLITE arm (child marker at v1d[i], then
                    // the 2/1 probe) is the excluded arm.
                    svPtr.v1d[i++] = 1.toChar()
                    svPtr.v1d[i++] = 1.toChar()
                    svPtr.v1d[i++] = NUL
                    f = mglobalAt(NEXT_C, svPtr.v1d, vOff, svPtr.bd, 0, svPtr)

                    svPtr.bd[2] = bbtyp

                    if (f == 1) svPtr.bd[1] = '1' else svPtr.bd[1] = '0'

                    if (svPtr.bd[1] == '0') {
                        svPtr.bd[1] = svPtr.bd[2]
                        svPtr.bd[2] = NUL
                    }

                    svPtr.bd[3] = NUL
                    state = PState.EXEC
                    continue@machine
                }

                svPtr.tpx = f.toLong()
                state = PState.EXEC
                continue@machine
            }

            // normal global - built-in variables/fcns
            if (svPtr.v1d[1] == DOLLARC) {
                if (svPtr.v1d[2] == 'd' ||
                    // commented-out vendor alternatives kept as comment
                    // v1d[2] == 'n' || 'o' || 'O' || 'b' || 'B' || 'N'
                    svPtr.v1d[2] == 'D'
                ) {
                    // open
                    i = 1
                    while (svPtr.v1d[i] != COD_OPEN) i++

                    j = 0
                    // close
                    while (true) {
                        i++
                        j++
                        svPtr.bd[j] = svPtr.v1d[i]
                        if (svPtr.bd[j] == COD_CLOSE) break
                    }
                    svPtr.bd[j] = NUL
                    state = PState.EXEC
                    continue@machine
                }

                if (svPtr.v1d[2] == 'z' || svPtr.v1d[2] == 'Z') {
                    zfcn(svPtr.v1d, vOff, svPtr.bd, bOff, svPtr)
                } else {
                    fcn(svPtr)
                }

                if (svPtr.ierr == 0) {
                    state = PState.EXEC
                    continue@machine
                }

                return svPtr.ierr
            }

            if (svPtr.pd1[svPtr.sdlim] == DOLLARC && s1p[spx] == OPENC &&
                (svPtr.pd1[svPtr.sdlim + 1] == 'G' ||
                    svPtr.pd1[svPtr.sdlim + 1] == 'g')
            ) {
                svPtr.symflg =
                    if (toupperC(svPtr.pd1[svPtr.sdlim + 1]) == 'G') SYM_NAME
                    else SYM_NEXT

                sym_(svPtr.symflg, svPtr.v1d, vOff, svPtr.bd, bOff, svPtr)

                if (toupperC(svPtr.pd1[svPtr.sdlim + 1]) == 'N' &&
                    svPtr.bd[1] == NUL
                ) {
                    strcpyC(svPtr.bd, "-1", bOff)
                }

                if (svPtr.symflg != 0 && s1p[spx] != INDIRECT) {
                    state = PState.EXEC
                    continue@machine
                }

                return 27
            }

            // retrieve look-up
            if (svPtr.v1d[1] == '~') { // PREDICATEVAR
                strmove(svPtr.bd, bOff, svPtr.v1d, vOff)
                state = PState.EXEC
                continue@machine
            }

            if (svPtr.pd1[svPtr.sdlim] == DOLLARC && s1p[spx] == OPENC) {
                // query qlength qsubscript
                if (toupperC(svPtr.pd1[svPtr.sdlim + 1]) == 'Q') {
                    g = NEXT_C
                    BuildLocal(0, g, svPtr.v1d, vOff, null, svPtr)
                    strcpyC(svPtr.bd, "<lcl>", bOff)
                    state = PState.EXEC
                    continue@machine
                }

                if (toupperC(svPtr.pd1[svPtr.sdlim + 1]) == 'N') {
                    g = NEXT_C
                    BuildLocal(0, g, svPtr.v1d, vOff, null, svPtr)
                    strcpyC(svPtr.bd, "<lcl>", bOff)
                    state = PState.EXEC
                    continue@machine
                }

                if (toupperC(svPtr.pd1[svPtr.sdlim + 1]) == 'O') {
                    FcnArgToggle++
                    FcnArgToggle %= 2

                    if (FcnArgToggle == 1) {
                        g = ORDERNEXT_C
                        BuildLocal(0, g, svPtr.v1d, vOff, null, svPtr)
                        strcpyC(svPtr.bd, "<lcl>", bOff)
                        state = PState.EXEC
                        continue@machine
                    }
                }
            }

            // normal variable lookup (the vendor's commented-out
            // LHS-dollar-extract no-resolve branch stays a comment)
            svPtr.symflg = SYM_RETRIEVE

            if (sym_(svPtr.symflg, svPtr.v1d, vOff, svPtr.bd, bOff, svPtr) == null) {
                svPtr.symflg = 0
            } else {
                svPtr.symflg = 1
            }

            if (svPtr.pd1[svPtr.sdlim] != DOLLARC) {
                state = PState.SYM1
                continue@machine
            }

            // local variable dollar-DATA
            if (s1p[spx] == OPENC &&
                (svPtr.pd1[svPtr.sdlim + 1] == 'd' ||
                    svPtr.pd1[svPtr.sdlim + 1] == 'D')
            ) {
                svPtr.symflg = SYM_DATA
                sym_(svPtr.symflg, svPtr.v1d, vOff, svPtr.bd, bOff, svPtr)
                state = PState.EXEC
                continue@machine
            }

            state = PState.SYM1
        }

        PState.SYM1 -> {
            // sym1: check for variable not found
            if (svPtr.symflg == 0) {
                i = 1
                var k = 1

                while (true) { // look for a label of the same name
                    var j2 = i
                    while (svPtr.pd1[j2] != TABC && svPtr.pd1[j2] != ' ') j2++

                    if (strncmpC(svPtr.pd1, i, cstringAt(svPtr.v1d, vOff), j2 - i) == 0 &&
                        strlenC(svPtr.v1d, vOff) == j2 - i
                    ) {
                        writeCstring(svPtr.bd, k.toString(), bOff) // sprintf(bp,"%d",k)
                        state = PState.EXEC
                        break
                    }

                    i = i + strlenC(svPtr.pd1, i) + 1

                    if (i > svPtr.pd1len) {
                        return 17
                    }

                    k++
                }
                continue@machine
            }

            state = PState.EXEC
        }

        PState.EXEC -> {
            // exec: process value in bb or bd
            if (s1p[spx] != PEMPTY && s1p[spx] != POPEN && s1p[spx] != OPENC) {
                state = PState.NXT_EXPR
                continue@machine
            }

            spx++

            while (svPtr.pd1[svPtr.sdlim] != NUL) svPtr.sdlim++
            svPtr.sdlim++ // while (pd1[sdlim++]);

            strmove(svPtr.pd1, svPtr.sdlim, svPtr.bd, bOff)
            s1p[spx] = OPERAND
            state = PState.NCHAR
        }

        PState.NXT_EXPR -> {
            // nxt_expr: check for operator on stack top
            if (s1p[spx] == POPEN || s1p[spx] == OPERAND) {
                return 11
            }

            // check for number under operator
            if (s1p[spx - 1] != OPERAND) {
                when (s1p[spx]) {
                    INDIRECT -> {
                        strcatAC(svPtr.bd, bOff, svPtr.xd, svPtr.xpx + 1)
                        strmove(svPtr.xd, 1, svPtr.bd, bOff)
                        spx--
                        svPtr.xpx = 0

                        if (svPtr.kflg != 0 && spx <= 1) {
                            svPtr.xpx = 1
                            return 0
                        }

                        state = PState.NCHAR
                    }

                    MINUS -> { // unary minus
                        writeCstring(tmp3, "-1")
                        mult(svPtr.bd, bOff, tmp3, 0, svPtr.bd, bOff)
                        state = PState.NXT_OPERATOR_U
                    }

                    NOT_NOT -> { // not not
                        svPtr.bd[1] = if (numcmpAC(svPtr.bd, bOff, zeroC, 0) != 0) '1' else '0'
                        svPtr.bd[2] = NUL
                        state = PState.NXT_OPERATOR_U
                    }

                    NOT -> { // unary not
                        svPtr.bd[1] = if (numcmpAC(svPtr.bd, bOff, zeroC, 0) != 0) '0' else '1'
                        svPtr.bd[2] = NUL
                        state = PState.NXT_OPERATOR_U
                    }

                    PLUS -> { // unary plus
                        writeCstring(tmp3, "0")
                        add(svPtr.bd, bOff, tmp3, 0, svPtr.bd, bOff)
                        state = PState.NXT_OPERATOR_U
                    }

                    else -> return 1
                }
                continue@machine
            }

            // extract value under operator
            adx = svPtr.sdlim
            svPtr.sdlim--
            while (svPtr.pd1[svPtr.sdlim - 1] != NUL) svPtr.sdlim--

            // branch depending upon operator
            when (s1p[spx]) {
                OPERAND, PEMPTY, NOT -> return 12

                DIVIDE -> divx(svPtr.pd1, adx, svPtr.bd, bOff, svPtr.bd, bOff)

                EXPONEN -> expx(svPtr.pd1, adx, svPtr.bd, bOff, svPtr.bd, bOff)

                MULTIPLY -> mult(svPtr.pd1, adx, svPtr.bd, bOff, svPtr.bd, bOff)

                MINUS -> sub(svPtr.pd1, adx, svPtr.bd, bOff, svPtr.bd, bOff)

                PLUS -> add(svPtr.pd1, adx, svPtr.bd, bOff, svPtr.bd, bOff)

                CONCAT -> {
                    // start of left operand; find end; append bp;
                    // length check; copy result back to bp
                    var to = adx
                    while (svPtr.pd1[to] != NUL) to++
                    var from = bOff
                    while (true) {
                        val c = svPtr.bd[from]
                        svPtr.pd1[to] = c
                        to++
                        from++
                        if (c == NUL) break
                    }
                    if (to - adx >= 32000) return 35 // too long
                    svPtr.pd1[to] = NUL
                    var t2 = bOff
                    var f2 = adx
                    while (true) {
                        val c = svPtr.pd1[f2]
                        svPtr.bd[t2] = c
                        t2++
                        f2++
                        if (c == NUL) break
                    }
                }

                EQUALS -> {
                    svPtr.bd[1] = if (strcmpAC(svPtr.pd1, adx, svPtr.bd, bOff) != 0) '0' else '1'
                    svPtr.bd[2] = NUL
                }

                NOTEQ -> {
                    svPtr.bd[1] = if (strcmpAC(svPtr.pd1, adx, svPtr.bd, bOff) != 0) '1' else '0'
                    svPtr.bd[2] = NUL
                }

                GREATER -> {
                    svPtr.bd[1] = if (numcmpAC(svPtr.pd1, adx, svPtr.bd, bOff) > 0) '1' else '0'
                    svPtr.bd[2] = NUL
                }

                LESSTHAN -> {
                    svPtr.bd[1] = if (numcmpAC(svPtr.pd1, adx, svPtr.bd, bOff) < 0) '1' else '0'
                    svPtr.bd[2] = NUL
                }

                INTDIVIDE -> divi(svPtr.pd1, adx, svPtr.bd, bOff, svPtr.bd, bOff)

                MODULO -> modulo(svPtr.pd1, adx, svPtr.bd, bOff, svPtr.bd, bOff)

                CONTAINS -> {
                    // vendor's first arm tested "pd1[adx] == 0 && bp == 0";
                    // bp is never null, so the arm is dead (h013) - dropped.
                    svPtr.bd[1] =
                        if (xindex(copyOfC(svPtr.pd1, adx), copyOfC(svPtr.bd, bOff), 1) == 0) '0'
                        else '1'
                    svPtr.bd[2] = NUL
                }

                FOLLOWS -> {
                    svPtr.bd[1] =
                        if (strcmpAC(svPtr.pd1, adx, svPtr.bd, bOff) <= 0) '0' else '1'
                    svPtr.bd[2] = NUL
                }

                // vendor mid-switch prototype kept as comment:
                // int rpm(unsigned char [], const unsigned char [], struct MSV *);

                PATTERN -> {
                    svPtr.bd[1] = if (rpm(svPtr.pd1, adx, svPtr.bd, 0, svPtr) == 0) '0' else '1'
                    svPtr.bd[2] = NUL
                }

                NOTPATTERN -> {
                    svPtr.bd[1] = if (rpm(svPtr.pd1, adx, svPtr.bd, 0, svPtr) == 0) '1' else '0'
                    svPtr.bd[2] = NUL
                }

                NOTGREATER -> {
                    svPtr.bd[1] =
                        if (numcmpAC(svPtr.pd1, adx, svPtr.bd, bOff) <= 0) '1' else '0'
                    svPtr.bd[2] = NUL
                }

                NOTLESS -> {
                    svPtr.bd[1] =
                        if (numcmpAC(svPtr.pd1, adx, svPtr.bd, bOff) >= 0) '1' else '0'
                    svPtr.bd[2] = NUL
                }

                NOTCONTAINS -> {
                    svPtr.bd[1] =
                        if (xindex(copyOfC(svPtr.pd1, adx), copyOfC(svPtr.bd, bOff), 1) == 0) '1'
                        else '0'
                    svPtr.bd[2] = NUL
                }

                NOTFOLLOWS -> {
                    svPtr.bd[1] =
                        if (strcmpAC(svPtr.pd1, adx, svPtr.bd, bOff) <= 0) '1' else '0'
                    svPtr.bd[2] = NUL
                }

                AND -> {
                    svPtr.bd[1] =
                        if (numcmpAC(svPtr.bd, bOff, zeroC, 0) != 0 &&
                            numcmpAC(svPtr.pd1, adx, zeroC, 0) != 0) '1' else '0'
                    svPtr.bd[2] = NUL
                }

                OR -> {
                    svPtr.bd[1] =
                        if (numcmpAC(svPtr.bd, bOff, zeroC, 0) != 0 ||
                            numcmpAC(svPtr.pd1, adx, zeroC, 0) != 0) '1' else '0'
                    svPtr.bd[2] = NUL
                }

                NOTAND -> {
                    svPtr.bd[1] =
                        if (numcmpAC(svPtr.bd, bOff, zeroC, 0) != 0 &&
                            numcmpAC(svPtr.pd1, adx, zeroC, 0) != 0) '0' else '1'
                    svPtr.bd[2] = NUL
                }

                NOTOR -> {
                    svPtr.bd[1] =
                        if (numcmpAC(svPtr.bd, bOff, zeroC, 0) != 0 ||
                            numcmpAC(svPtr.pd1, adx, zeroC, 0) != 0) '0' else '1'
                    svPtr.bd[2] = NUL
                }

                else -> return 12 // vendor default
            }

            state = PState.DEC_STK
        }

        PState.NXT_OPERATOR_U -> {
            // the unary-operator common tail: spx--; goto nxt_operator
            spx--
            state = PState.NXT_OPERATOR
        }

        PState.DEC_STK -> {
            // dec_stk:
            spx -= 2
            state = PState.NXT_OPERATOR
        }

        PState.NXT_OPERATOR -> {
            // nxt_operator: push answer
            if (s1p[spx] != POPEN && s1p[spx] != OPENC && s1p[spx] != PEMPTY) {
                state = PState.NXT_EXPR
                continue@machine
            }

            spx++
            svPtr.sdlim += strlenC(svPtr.pd1, svPtr.sdlim) + 1

            var t2 = svPtr.sdlim
            var f2 = bOff
            while (true) {
                val c = svPtr.bd[f2]
                svPtr.pd1[t2] = c
                t2++
                f2++
                if (c == NUL) break
            }
            svPtr.pd1[t2] = NUL

            s1p[spx] = OPERAND
            state = PState.NCHAR
        }

        PState.FINISH -> {
            // finish:
            if (s1p[spx - 1] != PEMPTY || s1p[spx] != OPERAND) return 16
            return 0
        }
        }
    }
}
