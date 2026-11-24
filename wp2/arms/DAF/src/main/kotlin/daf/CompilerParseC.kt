package daf

// Unit 277 (DAF B9 T3, complex): compiler-parse.c::parse_ (1201
// nonblank LOC, the batch-9 closer): the compiler-side expression
// parser proper - the code[26]/opcode[256]/ncode[256]/dcode[256]/
// operand[256] table lattice verbatim, the short-int local frame
// (xpxOrg/symflg/ernbr/f/spx/adx/jpx/j/g, s1p[40], cod209, 
// bp=&bd[1], v1dp=&v1d[1], Order/deferedRef/OrderArg carriers),
// the goto web (start/nchar/scan_operand/skip_numeric/un_nest/
// var1/no_arg/fcn_proc_done/has_array/no_array/no_array1/exec/
// nxt_expr/dec_stk/nxt_operator/finish), and the compile-time
// emitters (globals build, \u0024-INtrinsic ladder through the
// builtin \u0024-function family, unary/binary operator folds) onto
// the DafCompilerState Out writer.
//
// SEAT NOTE (row 277 ledger, corrected this batch): the parseDaf
// stub blame "interp-parse.cpp::parse_(584)" belongs to roster B3
// (cycle 9 text corrected to b3) and STAYS RAISED for the
// interpreter body; THIS row owns compiler-parse.c::parse_ and
// renders on its OWN seat parseCDaf over the DafCompilerState
// globals (C.xd/C.xpx/C.pd1/C.sdlim/C.v1d/C.iiitmp/emit). The one
// in-arm call site that vendor-wise calls this parse_
// (CompilerZcommandsCode zseek: "parse_() sv-drive fold") still
// binds parseDaf(DafInterpState.svnPtr); whether that site rebinds
// to parseCDaf is a PI adjudication flagged in the seats row and
// fragment (compiler-parse.c is the compiler TU's own parse_, so
// the rebind looks likely, but the sv-drive contract of the site
// predates this render and is NOT changed here).
//
// Consults (2026-11-24T22:30:00Z + 30s/unit nominal, unit 277 of
// batch 9): table transcription cross-checked line-for-line
// against the dump at ~/b4/'compiler-parse.c::parse_.c.txt' (the
// /*N*/ row markers kept); CodedOpen/CodedClose/CodedComma/
// CodedColon map to the ARM const family per the C-012
// adjudication (name-to-name: vendor CodedClose 207 -> CODED_CLOSE_D
// 208, vendor CodedComma 208 -> CODED_COMMA_D 207; the emitted
// %d of the array-setup lines will therefore print the
// arm-inverted pair, arm-internal symmetric per C-012); QUOTE is
// the \u0060 char vendor define rendered here to keep this file
// backtick-free (H-015); Order[128]/deferedRef[2048]/deferedRslt/
// deferedType/OrderArg and kkk/bbtyp are declared by the vendor
// but never read or written in the body - not carried, dead-local
// ledger note (row 46 discipline).
//
// STRUCTURAL FOLD (H-010 pointer folds; BtreeX/KillCode goto-web
// precedent): the goto web renders as a single label-driven state
// walk (a private label const lattice + when dispatch). Pointer
// pairs against fixed buffers fold to index arithmetic: bd-bp carrier pair
// carriers keep the vendor's bd[1-onward] content at bp[0-onward]
// (bp[0] == vendor bd[1]) so seat signatures (conCheckD/cannonD/
// extractD sprintf targets) read index 0; v1dp folds to v1d
// offset +1. The pd1 stack ops render as explicit NUL-run walks.
// Emitted Out text keeps the arm convention: the vendor's C
// escape sequences ("\t", "\n", "\"" pairs) emit as literal
// backslash sequences, per the CompilerExtract seat precedent
// (the ExpandText seat emits real whitespace; the arm's two
// conventions and this render's choice of the literal-escape
// side are disclosed for PI adjudication in the fragment).
//
// SEATS CONSUMED: cannonD (Cannon.kt; cannon(bp) compile-time
// literal canonicalize), conCheckD/memcheckDaf/cvarDaf/
// strcasecmpD-family (CompilerParseMisc.kt; fold onto the
// offset-carrier helpers below), extract1/extract2Daf
// (DafInterpStubsT3 raised seats), extractD (CompilerExtract.kt;
// the vendor Extract(v1d,bp) \u0024extract fold), expandTextD/
// expandOrderD/expandDataD/expandNameD (landed), numcomp emit
// text only, DafCRuntime.fmtG (the %.15g canonicalized literal,
// see scanLfPC/sprintfG15PC below), DafMsv carriers pd1len/
// symlen/ErrChar/Rargs/Rargsx/tpx (the shared-extern fold per
// the unit-94/95/107 disposition ledger; the init seed
// pd1[pd1len+2..3]=0 reads svPtr->pd1len per the vendor extern,
// and tpx=f writes svPtr.tpx - disclosed folds).
//
// RAISES OF THIS RENDER (named-owner stubs, prnterrDaf
// precedent; all throw at call sites inside parseCDaf):
// - patrn1Daf (vendor patrn1(&jpx,&ernbr); owner the patrn
//   family roster seat, no corpus cell rendered yet)
// - fixstrDaf (vendor fixstr(&bd[1]); Doty.kt-family fold, no
//   landed seat)
// - expandQSDaf / expandNextDaf / expandGetDaf (vendor ExpandQS/
//   ExpandNext/ExpandGet; no landed seat - the ExpandText/
//   Order/Data/Name four have landed, these three have not)
// - myFuncDaf (vendor MyFunc(&v1d[3],iiitmp) compile-time
//   user-function fold; no corpus cell rendered)
// Dafny: EXEMPT (emit-side compiler parser plumbing; journal
// row; suite stays 70/0).

// ---- operator lattice (defines.h / interp.h twin; per-file
// private pins per H-012) ----
private const val OPEN_P277 = 0
private const val DIVIDE_P277 = 1
private const val MULTIPLY_P277 = 2
private const val MINUS_P277 = 3
private const val PLUS_P277 = 4
private const val OPERAND_P277 = 5
private const val OPENC_P277 = 6
private const val CONCAT_P277 = 7
private const val EQUALS_P277 = 8
private const val GREATER_P277 = 9
private const val EMPTY_P277 = 10
private const val LESSTHAN_P277 = 11
private const val NOT_P277 = 12
private const val INTDIVIDE_P277 = 13
private const val MODULO_P277 = 14
private const val CONTAINS_P277 = 15
private const val FOLLOWS_P277 = 16
private const val PATTERN_P277 = 17
private const val NOTEQ_P277 = 18
private const val NOTGREATER_P277 = 19
private const val NOTLESS_P277 = 20
private const val NOTCONTAINS_P277 = 21
private const val NOTFOLLOWS_P277 = 22
private const val NOTPATTERN_P277 = 23
private const val AND_P277 = 24
private const val OR_P277 = 25
private const val NOTAND_P277 = 26
private const val NOTOR_P277 = 27
private const val INDIRECT_P277 = 28
private const val EXPONEN_P277 = 29
private const val PREDICATEVAR_P277: Char = '~'
private const val QUOTE_P277: Char = '\u0060'   // vendor define QUOTE (the \u0060 char)
private const val CODED_COLON_P277: Char = '\u00D1' // 209 CodedColon

// ---- state-walk labels (the goto web fold) ----
private const val L_START = 1
private const val L_NCHAR = 2
private const val L_SCAN_OPERAND = 3
private const val L_SKIP_NUMERIC = 4
private const val L_UN_NEST = 5
private const val L_VAR1 = 6
private const val L_NO_ARG = 7
private const val L_FCN_PROC_DONE = 8
private const val L_EXEC = 9
private const val L_NXT_EXPR = 10
private const val L_DEC_STK = 11
private const val L_NXT_OPERATOR = 12
private const val L_FINISH = 13

// ---- raises of this render (named-owner stubs) ----

// int patrn1(int *, int *); owner: the patrn pattern family
// (compiler-parse prototype roster; no corpus cell rendered in the
// DAF arm yet). Fold contract: returns ernbr (0 = clean) and the
// jpx cursor advance is owner-defined until the family lands.
fun patrn1Daf(jpx: Int, ernbr: Int): Int =
    throw NotImplementedError(
        "patrn1: out-of-scope owner (patrn pattern family, no corpus cell rendered)")

// void fixstr(unsigned char *); Doty.kt-family fold; no landed seat.
fun fixstrDaf(b: CharArray): Unit =
    throw NotImplementedError(
        "fixstr: out-of-scope owner (Doty-family pattern string fixer, no landed seat)")

// void ExpandQS/ExpandNext/ExpandGet(unsigned char *, int, unsigned
// char *); compiler-parse extern ladder; no landed seats.
fun expandQSDaf(v1d: CharArray, i0: Int, bp: CharArray): Unit =
    throw NotImplementedError(
        "ExpandQS: out-of-scope owner (compiler-parse Expand* ladder, no landed seat)")
fun expandNextDaf(v1d: CharArray, i0: Int, bp: CharArray): Unit =
    throw NotImplementedError(
        "ExpandNext: out-of-scope owner (compiler-parse Expand* ladder, no landed seat)")
fun expandGetDaf(v1d: CharArray, i0: Int, bp: CharArray): Unit =
    throw NotImplementedError(
        "ExpandGet: out-of-scope owner (compiler-parse Expand* ladder, no landed seat)")

// int MyFunc(char *, int); compile-time user-function fold.
fun myFuncDaf(name: String, iiitmp: Int): Int =
    throw NotImplementedError(
        "MyFunc: out-of-scope owner (compiler user-function fold, no corpus cell)")

// ---- the parse_ tables (verbatim; the /*N*/ row markers kept) ----

private val CODE_P277 = intArrayOf(
    /*0*/ 99, 99, 99, 99, 99, 99, 99, 99, 18, 19,
    /*10*/ 99, 20, 18, 99, 99, 21, 22, 23, 99, 99,
    /*20*/ 99, 99, 99, 99, 26, 27
)

private val OPCODE_P277 = IntArray(256).also {
    val rows = arrayOf(
        intArrayOf(0, 99, 99, 99, 99, 99, 99, 99, 99, 99),
        intArrayOf(10, 99, 99, 99, 99, 99, 99, 99, 99, 99),
        intArrayOf(20, 99, 99, 99, 99, 99, 99, 99, 99, 99),
        intArrayOf(30, 99, 99, 99, 99, 25, 99, 14, 99, 24),
        intArrayOf(40, 0, 99, 2, 4, 99, 3, 99, 1, 99),
        intArrayOf(50, 99, 99, 99, 99, 99, 99, 99, 99, 99),
        intArrayOf(60, 11, 8, 9, 17, 28, 99, 99, 99, 99),
        intArrayOf(70, 99, 99, 99, 99, 99, 99, 99, 99, 99),
        intArrayOf(80, 99, 99, 99, 99, 99, 99, 99, 99, 99),
        intArrayOf(90, 99, 15, 13, 16, 99, 7, 99, 99, 99),
        intArrayOf(100, 99, 99, 99, 99, 99, 99, 99, 99, 99),
        intArrayOf(110, 99, 99, 99, 99, 99, 99, 99, 99, 99),
        intArrayOf(120, 99, 99, 99, 99, 99, 99, 99)
    )
    for (r in rows) { val base = r[0]; for (k in 1 until r.size) it[base + k - 1] = r[k] }
}

private val NCODE_P277 = IntArray(256).also {
    val rows = arrayOf(
        intArrayOf(0, 99, 99, 99, 99, 99, 99, 99, 99, 99),
        intArrayOf(10, 99, 99, 99, 99, 99, 99, 99, 99, 99),
        intArrayOf(20, 99, 99, 99, 99, 99, 99, 99, 99, 99),
        intArrayOf(30, 99, 99, 99, 99, 99, 99, 99, 99, 99),
        intArrayOf(40, 99, 10, 99, 10, 10, 10, 99, 10, 10),
        intArrayOf(50, 10, 10, 10, 10, 10, 10, 10, 10, 99),
        intArrayOf(60, 99, 99, 99, 99, 99, 99, 99, 99, 99), // E
        intArrayOf(70, 99, 99, 99, 99, 99, 99, 99, 99, 99),
        intArrayOf(80, 99, 99, 99, 99, 99, 99, 99, 99, 99),
        intArrayOf(90, 99, 99, 99, 99, 99, 99, 99, 99, 99),
        intArrayOf(100, 99, 99, 99, 99, 99, 99, 99, 99, 99), // e
        intArrayOf(110, 99, 99, 99, 99, 99, 99, 99, 99, 99),
        intArrayOf(120, 99, 99, 99, 99, 99, 99, 99)
    )
    for (r in rows) { val base = r[0]; for (k in 1 until r.size) it[base + k - 1] = r[k] }
}

private val DCODE_P277 = IntArray(256).also {
    val rows = arrayOf(
        intArrayOf(0, 10, 99, 99, 99, 99, 99, 99, 99, 99),
        intArrayOf(10, 99, 99, 99, 99, 99, 99, 99, 99, 99),
        intArrayOf(20, 99, 99, 99, 99, 99, 99, 99, 99, 99),
        intArrayOf(30, 99, 10, 10, 99, 10, 99, 99, 10, 10),
        intArrayOf(40, 99, 10, 10, 10, 10, 10, 99, 10, 99),
        intArrayOf(50, 99, 99, 99, 99, 99, 99, 99, 99, 10),
        intArrayOf(60, 99, 10, 10, 10, 10, 10, 99, 99, 99),
        intArrayOf(70, 99, 99, 99, 99, 99, 99, 99, 99, 99),
        intArrayOf(80, 99, 99, 99, 99, 99, 99, 99, 99, 99),
        intArrayOf(90, 99, 10, 10, 10, 99, 10, 99, 99, 99),
        intArrayOf(100, 99, 99, 99, 99, 99, 99, 99, 99, 99),
        intArrayOf(110, 99, 99, 99, 99, 99, 99, 99, 99, 99),
        intArrayOf(120, 99, 99, 99, 99, 99, 99, 99)
    )
    for (r in rows) { val base = r[0]; for (k in 1 until r.size) it[base + k - 1] = r[k] }
}

private val OPERAND_TAB_P277 = IntArray(256).also {
    val vals = intArrayOf(
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 1, 0, 1, 1, 2, 0,
        0, 0, 0, 0, 0, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0,
        2, 2, 2, 2, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
        1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 0, 2, 1, 0, 0, 1, 1, 1,
        1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
        1, 1, 1, 0, 0, 0, 0, 0
    )
    for (k in vals.indices) it[k] = vals[k]
}

// ---- file-local carrier helpers (H-010 folds; NUL-run
// convention on the CharArray carriers) ----

// strlen fold at an offset
private fun lenAt(a: CharArray, off: Int): Int {
    var n = 0
    while (a[off + n] != NUL_D) n++
    return n
}

// strcpy(dst[off], src-string); NUL-terminated
private fun putAt(dst: CharArray, off: Int, s: String) {
    for (k in s.indices) dst[off + k] = s[k]
    dst[off + s.length] = NUL_D
}

// strcat(dst[off], s)
private fun catAt(dst: CharArray, off: Int, s: String) {
    putAt(dst, off + lenAt(dst, off), s)
}

// the C-string at buf[off] as a Kotlin string
private fun strAt(a: CharArray, off: Int): String =
    buildString { for (k in 0 until lenAt(a, off)) append(a[off + k]) }

// strcasecmp(a[off], s) == 0 (the seat's convention: equality
// iff the walk ends on a NUL with no mismatch; ASCII-fold)
private fun eqCIAt(a: CharArray, off: Int, s: String): Boolean {
    var i = 0
    while (i < s.length) {
        if (a[off + i].uppercaseChar() != s[i].uppercaseChar()) return false
        if (a[off + i] == NUL_D) return true
        i++
    }
    return a[off + i] == NUL_D
}

// strncasecmp(a[off], s, n) == 0 (the seat's walk + early-NUL
// quirk kept: a NUL inside the n-window reads as equal)
private fun startsCIAt(a: CharArray, off: Int, s: String, n: Int): Boolean {
    var i = 0
    while (i < n && a[off + i].uppercaseChar() == s[i].uppercaseChar() &&
        a[off + i] != NUL_D) {
        i++
    }
    return n - i == 0 || a[off + i] == NUL_D
}

// sscanf("%f","%lf")-style longest-prefix double scan; null when
// no assignment (the avgScanD precedent, Locale.US strtod)
private fun scanLfPC(s: String): Double? {
    val m = REGEX_LF_P277.find(s) ?: return null
    return m.value.toDoubleOrNull()
}
private val REGEX_LF_P277 = Regex(
    "^[+-]?(((0|[1-9][0-9]*)(\\.[0-9]*)?)|(\\.[0-9]+))([eE][+-]?[0-9]+)?")

// sprintf("%lf" -> "%.15g") fold: String.format(Locale.US) %g;
// disclosed divergence: the JVM %g pads the exponent to two
// digits exactly as C's does for a %.15g double (mantissa kept
// to 15 significant digits both sides); the CRLF/inf/edge
//literals do not occur on this path (digits-only input folds)
private fun sprintfG15PC(v: Double): String =
    String.format(java.util.Locale.US, "%.15g", v)

// strncmp(a[off], s, 3) == 0
private fun pfx3At(a: CharArray, off: Int, s: String): Boolean {
    if (off + 2 >= a.size) return false
    if (a[off] != s[0] || a[off + 1] != s[1] || a[off + 2] != s[2]) return false
    return true
}

private fun isDigitPC(c: Char): Boolean = c in '0'..'9'

// =====================================================================
// int parse_()  -- the row-277 body
// =====================================================================
fun parseCDaf(C: DafCompilerState, svPtr: DafMsv): Int {

    var xpxOrg: Int
    var symflg: Int
    var ernbr: Int
    var f: Int
    var spx: Int
    var adx: Int
    var jpx: Int
    var j: Int
    var i: Int
    var g: Int
    val bp = CharArray(STR_MAX_DAF)      // unsigned char *bp = &bd[1]
    val s1p = IntArray(40)               // static unsigned char s1p[40]
    val cod209 = charArrayOf(CODED_COLON_P277, NUL_D)  // {209, 0}

    // initialization
    C.ierr = 0
    xpxOrg = C.xpx
    C.xpx--
    spx = 0
    C.pd1[svPtr.pd1len + 2] = NUL_D
    C.pd1[svPtr.pd1len + 3] = NUL_D
    C.sdlim = svPtr.pd1len + 3

    var label = L_START
    walkPC@ while (true) {
        when (label) {

        L_START -> {
            spx++
            s1p[spx] = EMPTY_P277    // stack foundation
            label = L_NCHAR
        }

        L_NCHAR -> {
            // if ((nxtchr = xd[++xpx]) == 0) goto finish
            C.xpx++
            val nxtchr = C.xd[C.xpx]
            if (nxtchr == NUL_D) { label = L_FINISH; continue@walkPC }

            if (OPERAND_TAB_P277[nxtchr.code] == 1) {
                label = L_SCAN_OPERAND; continue@walkPC
            }

            if (nxtchr == CODED_OPEN_D) {
                spx++
                while (C.pd1[C.sdlim++] != NUL_D) { }
                if (C.sdlim >= svPtr.symlen) return 31
                // while (*p2++ = *p1++); *p2 = 0; p2--; *p2 = CodedOpen
                var p2i = C.sdlim
                var p1i = 1
                while (true) {
                    val ch = C.v1d[p1i++]
                    C.pd1[p2i] = ch
                    if (ch == NUL_D) break
                    p2i++
                }
                C.pd1[p2i + 1] = NUL_D
                C.pd1[p2i] = CODED_OPEN_D
                s1p[spx] = OPENC_P277
                label = L_NCHAR; continue@walkPC
            }

            if (nxtchr == ',') {
                if (C.xd[C.xpx + 1] == ',' && C.xd[xpxOrg] == '\u0024' &&
                    C.xd[xpxOrg + 1] == '\u0024') {
                    // \u0024\u0024^a(a,,b) case -> insert zero arg
                    val tmp = strAt(C.xd, C.xpx + 1) // strcpy(tmp,&xd[xpx+1])
                    C.xd[C.xpx + 1] = '0'
                    putAt(C.xd, C.xpx + 2, tmp)     // strcpy(&xd[xpx+2],tmp)
                }
                if (s1p[spx - 1] != OPENC_P277) {
                    if (C.t2 > 0) { label = L_FINISH; continue@walkPC }
                    return 16
                }
                // concatenate stack tops and add coded comma
                C.sdlim--
                var ii = C.sdlim
                while (true) {
                    val ch = C.pd1[ii + 1]
                    C.pd1[ii] = ch
                    if (ch == NUL_D) break
                    ii++
                }
                C.pd1[ii] = CODED_COMMA_D
                C.pd1[ii + 1] = NUL_D
                C.sdlim--
                while (C.pd1[C.sdlim--] != NUL_D) { }
                C.sdlim += 2
                spx--
                label = L_NCHAR; continue@walkPC
            }

            if (nxtchr == ')') {
                if (s1p[spx - 1] != OPEN_P277) {
                    if (s1p[spx - 1] != OPENC_P277) {
                        if (s1p[spx] == OPENC_P277) {
                            // fcn() type ref
                            label = L_UN_NEST; continue@walkPC
                        } else return 16
                    }
                    // fcn/array return section
                    C.sdlim--
                    var ii = C.sdlim
                    while (true) {
                        val ch = C.pd1[ii + 1]
                        C.pd1[ii] = ch
                        if (ch == NUL_D) break
                        ii++
                    }
                    C.pd1[ii] = CODED_CLOSE_D
                    C.pd1[ii + 1] = NUL_D
                    C.sdlim--
                    while (C.pd1[C.sdlim--] != NUL_D) { }
                    C.sdlim += 2
                    spx--
                    label = L_UN_NEST; continue@walkPC
                }
                // precedence close paren
                if (s1p[spx] != OPERAND_P277) return 12
                // extract value from stack top
                putAt(bp, 0, strAt(C.pd1, C.sdlim))   // strcpy(bp, &pd1[sdlim])
                do { C.sdlim-- } while (C.pd1[C.sdlim - 1] != NUL_D)
                label = L_DEC_STK; continue@walkPC
            }

            if (nxtchr == ':') {
                i = C.sdlim
                do { i-- } while (C.pd1[i - 1] != NUL_D)
                if (C.pd1[i] != '\u0024' ||
                    Character.toLowerCase(C.pd1[i + 1]) != 's') {
                    label = L_FINISH; continue@walkPC
                }
                C.sdlim--
                // shift string down one and unify with \u0024select(
                var sss = C.sdlim
                while (true) {
                    val ch = C.pd1[sss + 1]
                    C.pd1[sss] = ch
                    sss++
                    if (C.pd1[sss] == NUL_D) break
                }
                while (C.pd1[C.sdlim--] != NUL_D) { }
                C.sdlim += 2
                catAt(C.pd1, C.sdlim, cod209[0].toString()) // strcat(cod209)
                spx--
                label = L_NCHAR; continue@walkPC
            }

            if (nxtchr == ' ' || nxtchr == '\t') {
                label = L_FINISH; continue@walkPC
            }

            if (nxtchr == '^' && C.t2 == 2 && s1p[spx] == OPERAND_P277) {
                label = L_FINISH; continue@walkPC
            }

            // check for delimiters in list
            spx++

            if (nxtchr == ']') {
                if (C.xd[C.xpx + 1] == ']') {
                    C.xpx++
                    s1p[spx] = FOLLOWS_P277
                    // ] and ]] do the same for this version
                    label = L_NCHAR; continue@walkPC
                }
            }

            if (nxtchr == QUOTE_P277) {   // insert not based operator code
                C.xpx++
                s1p[spx] = OPCODE_P277[C.xd[C.xpx].code]
                if (s1p[spx] != 99) {
                    val op = OPCODE_P277[C.xd[C.xpx].code]
                    // code[] is vendor-size 26; an opcode of 26+ reads
                    // out of bounds in the vendor (the '@' QUOTE pair);
                    // guarded to 99 here (disclosed divergence)
                    s1p[spx] = if (op < CODE_P277.size) CODE_P277[op] else 99
                    if (C.xd[C.xpx + 1] == ']') C.xpx++
                    if (s1p[spx] != 99) {
                        label = L_NCHAR; continue@walkPC
                    }
                }
                C.xpx--
            }

            if (nxtchr == '*' && C.xd[C.xpx + 1] == '*') {
                // exponentiation code
                C.xpx++
                s1p[spx] = EXPONEN_P277
                label = L_NCHAR; continue@walkPC
            }

            // insert code
            s1p[spx] = OPCODE_P277[nxtchr.code]
            if (s1p[spx] != 99) { label = L_NCHAR; continue@walkPC }
            svPtr.ErrChar = nxtchr
            return 12
        }

        L_SCAN_OPERAND -> {
            jpx = C.xpx

            if (s1p[spx] == NOTPATTERN_P277 || s1p[spx] == PATTERN_P277) {
                bp[0] = NUL_D    // bd[1] = 0
                // patrn1 (&jpx, &ernbr) - raised seat
                ernbr = patrn1Daf(jpx, 0)
                if (ernbr != 0) return ernbr  // if (ernbr) return (ernbr)
                label = L_EXEC; continue@walkPC
            }

            // numeric operand
            if (NCODE_P277[C.xd[C.xpx].code] == 10) {

                if (Character.isLetter(C.xd[C.xpx + 1]) ||
                    C.xd[C.xpx + 1] == '%') {  // call by ref argument
                    val RargsB = StringBuilder()
                    var ti = C.xpx + 1
                    while (!(C.xd[ti] == ',' || C.xd[ti] == NUL_D ||
                             C.xd[ti] == ')')) {
                        RargsB.append(C.xd[ti])
                        ti++
                    }
                    val Rargs = RargsB.toString()
                    C.emit("\\tstrcpy((char *) svPtr->Rargs[svPtr->Rargsx]," +
                        "(const char *) \"" + Rargs + "\"); \\n")
                    C.emit("\\tsvPtr->Rargsx++; \\n")
                    C.xpx++
                    label = L_SKIP_NUMERIC; continue@walkPC
                }

                // for (i=1; (bd[i]=xd[xpx]) && ncode[bd[i]]==10; i++,xpx++)
                i = 1
                while (true) {
                    bp[i - 1] = C.xd[C.xpx]
                    if (bp[i - 1] == NUL_D || NCODE_P277[bp[i - 1].code] != 10) break
                    i++; C.xpx++
                }

                if (i == 1 && bp[0] == '.') return 9

                if (Character.toLowerCase(bp[i - 1]) == 'e') {
                    i++
                    C.xpx++
                    if (C.xd[C.xpx] != '+' && C.xd[C.xpx] != '-' &&
                        !isDigitPC(C.xd[C.xpx])) return 9 // e type requires +/- or digit
                    bp[i - 1] = C.xd[C.xpx]
                    i++
                    C.xpx++
                    if (!isDigitPC(C.xd[C.xpx - 1]) &&
                        !isDigitPC(C.xd[C.xpx])) return 9 // e type requires an exponent
                    while (true) { // scan rest of exponent, if any
                        if (!isDigitPC(C.xd[C.xpx])) break
                        bp[i - 1] = C.xd[C.xpx]
                        i++
                        C.xpx++
                    }
                }

                bp[i - 1] = NUL_D
                // if (sscanf(&bd[1],"%lf",&temp)==0) return 12
                val temp = scanLfPC(strAt(bp, 0)) ?: return 12
                // sprintf(&bd[1],"%.15g",temp)
                putAt(bp, 0, sprintfG15PC(temp))
                cannonD(bp)

                // { char t[512]; t[0]='"'; strcpy(&t[1],&bd[1]);
                //   strcat(t,"\""); strcpy(&bd[1],t); }
                putAt(bp, 0, "\"" + strAt(bp, 0) + "\"")

                C.xpx--
                label = L_EXEC; continue@walkPC
            }

            label = L_SKIP_NUMERIC
        }

        L_SKIP_NUMERIC -> {
            // literal operand
            if (C.xd[C.xpx] == '"') {
                j = 0
                j++; bp[j - 1] = '"'   // bd[++j]='"'
                litPC@ while (true) {
                    scanPC@ while (true) {
                        C.xpx++
                        if (C.xd[C.xpx] == '"') break@scanPC
                        if (C.xd[C.xpx] == '\\') {
                            j++; bp[j - 1] = '\\'; j++; bp[j - 1] = '\\'
                            continue@scanPC
                        }
                        if (C.xd[C.xpx] == '\'') {
                            j++; bp[j - 1] = '\\'; j++; bp[j - 1] = '\''
                            continue@scanPC
                        }
                        j++; bp[j - 1] = C.xd[C.xpx]
                        if (C.xd[C.xpx] == NUL_D) return 2
                    }
                    if (C.xd[C.xpx + 1] != '"') {
                        j++; bp[j - 1] = '"'
                        j++; bp[j - 1] = NUL_D
                        label = L_EXEC; break@litPC
                    }
                    C.xpx++
                    j++; bp[j - 1] = '\\'
                    j++; bp[j - 1] = '"'
                }
                continue@walkPC
            }

            // variable name
            C.v1d[1] = C.xd[C.xpx]; C.xpx++
            j = 2

            varPC@ while (true) {
                if (DCODE_P277[C.xd[C.xpx].code] == 10) {
                    C.v1d[j] = NUL_D
                    C.xpx--
                    label = L_VAR1; break@varPC
                }
                C.v1d[j] = C.xd[C.xpx]
                j++
                if (C.xd[C.xpx] == '(') {
                    j--
                    C.v1d[j] = NUL_D
                    C.xd[C.xpx] = CODED_OPEN_D
                    C.xpx--          // xd[xpx--] = CodedOpen
                    label = L_START; break@varPC // recurse
                }
                C.xpx++
            }
        }

        L_UN_NEST -> {   // copy answer from stack
            // while (pd1[--sdlim]);
            while (true) { C.sdlim--; if (C.pd1[C.sdlim] == NUL_D) break }
            // strcpy (v1dp, &pd1[sdlim + 1])
            putAt(C.v1d, 1, strAt(C.pd1, C.sdlim + 1))
            // while (pd1[--sdlim]);
            while (true) { C.sdlim--; if (C.pd1[C.sdlim] == NUL_D) break }
            C.sdlim++
            spx -= 2
            label = L_VAR1
        }

        L_VAR1 -> {
            if (C.t0px == 1 && spx == 1) {
                spx--
                C.t0px = 0
                return 0
            }

            if (C.v1d[1] == '^') {  // global var

                if (!Character.isLetter(C.v1d[2]) &&
                    C.v1d[2] == '(') {
                    return 9001 // not valid array reference (naked case)
                }

                // g = RETRIEVE; (g is then never read - dead local,
                // note-only per the row 46 discipline)

                // build global array reference
                run {
                    val t = CharArray(1024)
                    var j2 = 0
                    var ii = 1
                    while (C.v1d[ii] != NUL_D) {
                        if (C.v1d[ii].code in 32..127) {
                            t[j2] = C.v1d[ii]; j2++
                        } else break
                        ii++
                    }
                    t[j2++] = '\\'
                    t[j2++] = 'x'
                    t[j2++] = '0'
                    t[j2++] = if (C.v1d[ii] == NUL_D) NUL_D else '1'
                    t[j2] = NUL_D

                    C.emit("\\tstrmove((UNS) tmp" + C.iiitmp +
                        ", (UNS) \"" + strAt(t, 0) + "\"); // init p674\\n")

                    if (C.v1d[ii] != NUL_D) {
                        ii++
                        while (true) {
                            j2 = 0
                            while (C.v1d[ii].code in 32..127) {
                                t[j2] = C.v1d[ii]; j2++; ii++
                            }
                            t[j2] = NUL_D
                            val tstr = strAt(t, 0)
                            if (tstr.isEmpty()) putAt(t, 0, "\"\"") // strcpy(t,"\"\"")
                            val useArg = if (tstr.startsWith("#G#") ||
                                             tstr.startsWith("#S#"))
                                             extract2Daf(strAt(t, 0))
                                         else strAt(t, 0)
                            C.emit("\\tstrcatx((UNS) tmp" + C.iiitmp +
                                ", (UNS) " + useArg + ");\\n")
                            if (C.v1d[++ii] != NUL_D) continue else break
                        }
                    }

                    C.iiitmp++
                    memcheckDaf(C.iiitmp)
                }

                memcheckDaf(C.iiitmp)
                // sprintf(setname,"tmp%d",iiitmp-1)
                putAt(C.setname, 0, "tmp" + (C.iiitmp - 1))
                // vendor sprintf passes a THIRD arg the format ignores;
                // quirk kept note-only
                putAt(bp, 0, "#G#tmp" + (C.iiitmp - 1) + "#tmp" + C.iiitmp + "#")

                // NO EVAL GLOBAL ZZ INTERFACE
                if (Character.toUpperCase(C.pd1[C.sdlim + 1]) == 'Z' &&
                    Character.toUpperCase(C.pd1[C.sdlim + 2]) == 'Z') {
                    putAt(bp, 0, "tmp" + (C.iiitmp - 1))
                }

                C.iiitmp++
                memcheckDaf(C.iiitmp)

                f = 1
                svPtr.tpx = f   // tpx = f ( disclosed sv-carrier fold)
                label = L_EXEC; continue@walkPC
            }  // normal global

            // built-in variables/fcns
            if (C.v1d[1] == '\u0024') {

                C.v1d[2] = Character.toLowerCase(C.v1d[2])

                if (C.v1d[2] == '\u0024') {
                    label = L_NO_ARG; continue@walkPC // goto no_arg
                }

                // \u00241 -> \u002499
                if ((isDigitPC(C.v1d[2]) && C.v1d[3] == NUL_D) ||
                    (isDigitPC(C.v1d[3]) && isDigitPC(C.v1d[4]) &&
                     C.v1d[5] == NUL_D)) {
                    C.v1d[1] = '_'   // change \u0024 to _ and revisit
                    label = L_VAR1; continue@walkPC
                }

                // \u0024test
                if (eqCIAt(C.v1d, 1, "\u0024test") || eqCIAt(C.v1d, 1, "\u0024t")) {
                    memcheckDaf(C.iiitmp)
                    C.emit("\\tif (svPtr->tpx) tmp" + C.iiitmp +
                        "[0]='1'; " + "/* \u0024test */\\n")
                    C.emit("\\telse tmp" + C.iiitmp + "[0]='0';\\n")
                    C.emit("\\ttmp" + C.iiitmp + "[1]='\\0';\\n")
                    putAt(bp, 0, "tmp" + C.iiitmp)
                    C.iiitmp++; memcheckDaf(C.iiitmp)
                    label = L_EXEC; continue@walkPC
                }

                // \u0024io
                if (eqCIAt(C.v1d, 1, "\u0024io") || eqCIAt(C.v1d, 1, "\u0024i")) {
                    memcheckDaf(C.iiitmp)
                    C.emit("\\tsprintf((char *) tmp" + C.iiitmp +
                        ", \"%d\", svPtr->io);" + " /* \u0024io */\\n")
                    putAt(bp, 0, "tmp" + C.iiitmp)
                    C.iiitmp++; memcheckDaf(C.iiitmp)
                    label = L_EXEC; continue@walkPC
                }

                // \u0024principal
                if (eqCIAt(C.v1d, 1, "\u0024principal") ||
                    eqCIAt(C.v1d, 1, "\u0024p")) {
                    memcheckDaf(C.iiitmp)
                    C.emit("\\tsprintf((char *) tmp" + C.iiitmp +
                        ",\"%d\", 5);" + " /* \u0024principal */\\n")
                    putAt(bp, 0, "tmp" + C.iiitmp)
                    C.iiitmp++; memcheckDaf(C.iiitmp)
                    label = L_EXEC; continue@walkPC
                }

                // \u0024x (the vendor duplicates the identical test
                // twice in the source pair; kept verbatim)
                if (eqCIAt(C.v1d, 1, "\u0024x") || eqCIAt(C.v1d, 1, "\u0024x")) {
                    memcheckDaf(C.iiitmp)
                    C.emit("\\tsprintf((char *) tmp" + C.iiitmp +
                        ",\"%d\",svPtr->hor[svPtr->io]);" + " /* \u0024x */\\n")
                    putAt(bp, 0, "tmp" + C.iiitmp)
                    C.iiitmp++; memcheckDaf(C.iiitmp)
                    label = L_EXEC; continue@walkPC
                }

                // \u0024y (same duplicated-test quirk)
                if (eqCIAt(C.v1d, 1, "\u0024y") || eqCIAt(C.v1d, 1, "\u0024y")) {
                    memcheckDaf(C.iiitmp)
                    C.emit("\\tsprintf((char *) tmp" + C.iiitmp +
                        ",\"%d\",svPtr->ver[svPtr->io]);" + " /* \u0024y */\\n")
                    putAt(bp, 0, "tmp" + C.iiitmp)
                    C.iiitmp++; memcheckDaf(C.iiitmp)
                    label = L_EXEC; continue@walkPC
                }

                // \u0024horolog
                if (eqCIAt(C.v1d, 1, "\u0024horolog") ||
                    eqCIAt(C.v1d, 1, "\u0024h")) {
                    memcheckDaf(C.iiitmp)
                    C.emit("\\t_horolog(tmp" + C.iiitmp + ");" +
                        " /* \u0024h */\\n")
                    putAt(bp, 0, "tmp" + C.iiitmp)
                    C.iiitmp++; memcheckDaf(C.iiitmp)
                    label = L_EXEC; continue@walkPC
                }

                // \u0024char
                if (startsCIAt(C.v1d, 1, "\u0024char", 5) ||
                    startsCIAt(C.v1d, 1, "\u0024c", 2)) {
                    var ii = 1
                    while (C.v1d[ii] >= ' ' && C.v1d[ii] <= '\u007F') ii++ // fcn nme
                    // for (SSS=v1d,TTT=&v1d[i+1]; ... ) shift-down
                    var s = 0; var t2 = ii + 1
                    while (C.v1d[t2] != NUL_D) { C.v1d[s] = C.v1d[t2]; s++; t2++ }
                    C.v1d[s] = NUL_D

                    C.emit("\\t_j=0;\\n")
                    ii = 0

                    chainPC@ while (true) {
                        j = ii
                        while (C.v1d[ii] >= ' ' && C.v1d[ii] <= '\u007F') ii++

                        if (C.v1d[ii] == NUL_D) {
                            memcheckDaf(C.iiitmp)
                            C.emit("\\ttmp" + (C.iiitmp + 1) + "[_j]=0;\\n")
                            putAt(bp, 0, "tmp" + (C.iiitmp + 1))
                            C.iiitmp++; memcheckDaf(C.iiitmp)
                            C.iiitmp++; memcheckDaf(C.iiitmp)
                            label = L_EXEC; break@chainPC
                        }

                        C.v1d[ii] = NUL_D
                        ii++
                        C.emit("\\tstrmove((UNS) tmp" + C.iiitmp +
                            ", (UNS) " + extract2Daf(strAt(C.v1d, j)) + ");\\n")
                        C.emit("\\t_i=atol((char *) tmp" + C.iiitmp + ");\\n")
                        C.emit("\\tif (_i>0) ")
                        C.emit("\\ttmp" + (C.iiitmp + 1) + "[_j++]=_i;\\n")
                    }
                    continue@walkPC
                }

                // \u0024FNumber
                if (startsCIAt(C.v1d, 1, "\u0024fnumber", 5) ||
                    startsCIAt(C.v1d, 1, "\u0024fn", 3)) {
                    var ii = 1
                    while (C.v1d[ii] >= ' ' && C.v1d[ii] <= '\u007F') ii++ // fcn nme
                    var s = 0; var t2 = ii + 1
                    while (C.v1d[t2] != NUL_D) { C.v1d[s] = C.v1d[t2]; s++; t2++ }
                    C.v1d[s] = NUL_D

                    ii = 0
                    var a = 0
                    val tmp = StringBuilder()

                    fnPC@ while (true) {
                        j = ii
                        while (C.v1d[ii] >= ' ' && C.v1d[ii] <= '\u007F') ii++
                        if (C.v1d[ii] == NUL_D) {
                            if (a == 0 || a > 3) return a
                            memcheckDaf(C.iiitmp)
                            C.emit("\\t_fnumber(" + " (UNS) tmp" + C.iiitmp +
                                ", (UNS) " + tmp.toString() + ", svPtr);\\n")
                            putAt(bp, 0, "tmp" + C.iiitmp)
                            C.iiitmp++; memcheckDaf(C.iiitmp)
                            label = L_EXEC; break@fnPC
                        }
                        C.v1d[ii] = NUL_D
                        ii++
                        if (a != 0) tmp.append(", (unsigned char *)")
                        a++
                        tmp.append(extract2Daf(strAt(C.v1d, j)))
                    }
                    continue@walkPC
                }

                // \u0024extract
                if (startsCIAt(C.v1d, 1, "\u0024extract", 5) ||
                    startsCIAt(C.v1d, 1, "\u0024e", 2)) {
                    if (extractD(C, C.v1d, bp) != 0) return 9003
                    label = L_EXEC; continue@walkPC
                }

                // \u0024length
                if (startsCIAt(C.v1d, 1, "\u0024length", 5) ||
                    startsCIAt(C.v1d, 1, "\u0024l", 2)) {
                    var ii = 1
                    while (C.v1d[ii] >= ' ' && C.v1d[ii] <= '\u007F') ii++
                    var s = 0; var t2 = ii + 1
                    while (C.v1d[t2] != NUL_D) { C.v1d[s] = C.v1d[t2]; s++; t2++ }
                    C.v1d[s] = NUL_D
                    ii = 0
                    var a = 0
                    val tmp = StringBuilder()
                    lenPC@ while (true) {
                        j = ii
                        while (C.v1d[ii] >= ' ' && C.v1d[ii] <= '\u007F') ii++
                        if (C.v1d[ii] == NUL_D) {
                            if (a == 0 || a > 3) return a
                            if (a == 1) tmp.append(", (UNS) \"\"")
                            memcheckDaf(C.iiitmp)
                            C.emit("\\t_length(" + "(UNS) tmp" + C.iiitmp +
                                ", (UNS) " + tmp.toString() + ");\\n")
                            putAt(bp, 0, "tmp" + C.iiitmp)
                            C.iiitmp++; memcheckDaf(C.iiitmp)
                            label = L_EXEC; break@lenPC
                        }
                        C.v1d[ii] = NUL_D
                        ii++
                        if (a != 0) tmp.append(", (UNS) ")
                        a++
                        tmp.append(extract2Daf(strAt(C.v1d, j)))
                    }
                    continue@walkPC
                }

                // \u0024find
                if (startsCIAt(C.v1d, 1, "\u0024find", 5) ||
                    startsCIAt(C.v1d, 1, "\u0024f", 2)) {
                    var ii = 1
                    while (C.v1d[ii] >= ' ' && C.v1d[ii] <= '\u007F') ii++
                    var s = 0; var t2 = ii + 1
                    while (C.v1d[t2] != NUL_D) { C.v1d[s] = C.v1d[t2]; s++; t2++ }
                    C.v1d[s] = NUL_D
                    ii = 0
                    var a = 0
                    val tmp = StringBuilder()
                    findPC@ while (true) {
                        j = ii
                        while (C.v1d[ii] >= ' ' && C.v1d[ii] <= '\u007F') ii++
                        if (C.v1d[ii] == NUL_D) {
                            if (a == 0 || a > 3) return a
                            if (a == 2) tmp.append(", (UNS) \"-1\"")
                            memcheckDaf(C.iiitmp)
                            C.emit("\\t_find(" + " (UNS) tmp" + C.iiitmp +
                                ", (UNS) " + tmp.toString() + ");\\n")
                            putAt(bp, 0, "tmp" + C.iiitmp)
                            C.iiitmp++; memcheckDaf(C.iiitmp)
                            label = L_EXEC; break@findPC
                        }
                        C.v1d[ii] = NUL_D
                        ii++
                        if (a != 0) tmp.append(", (UNS)")
                        a++
                        tmp.append(extract2Daf(strAt(C.v1d, j)))
                    }
                    continue@walkPC
                }

                // \u0024ascii
                if (startsCIAt(C.v1d, 1, "\u0024ascii", 5) ||
                    startsCIAt(C.v1d, 1, "\u0024a", 2)) {
                    var ii = 1
                    while (C.v1d[ii] >= ' ' && C.v1d[ii] <= '\u007F') ii++
                    var s = 0; var t2 = ii + 1
                    while (C.v1d[t2] != NUL_D) { C.v1d[s] = C.v1d[t2]; s++; t2++ }
                    C.v1d[s] = NUL_D
                    ii = 0
                    var a = 0
                    val tmp = StringBuilder()
                    asciiPC@ while (true) {
                        j = ii
                        while (C.v1d[ii] >= ' ' && C.v1d[ii] <= '\u007F') ii++
                        if (C.v1d[ii] == NUL_D) {
                            if (a == 0 || a > 2) return a
                            if (a == 1) tmp.append(", (UNS) \"-1\"")
                            memcheckDaf(C.iiitmp)
                            C.emit("\\t_ascii(" + " (UNS) tmp" + C.iiitmp +
                                ", (UNS) " + tmp.toString() + ");\\n")
                            putAt(bp, 0, "tmp" + C.iiitmp)
                            C.iiitmp++; memcheckDaf(C.iiitmp)
                            label = L_EXEC; break@asciiPC
                        }
                        C.v1d[ii] = NUL_D
                        ii++
                        if (a != 0) tmp.append(", (UNS)")
                        a++
                        tmp.append(extract2Daf(strAt(C.v1d, j)))
                    }
                    continue@walkPC
                }

                // \u0024justify
                if (startsCIAt(C.v1d, 1, "\u0024justify", 5) ||
                    startsCIAt(C.v1d, 1, "\u0024job", 4) ||
                    startsCIAt(C.v1d, 1, "\u0024j", 2)) {
                    var ii = 1
                    while (C.v1d[ii] >= ' ' && C.v1d[ii] <= '\u007F') ii++
                    if (C.v1d[ii] == NUL_D) {
                        putAt(C.v1d, 0, "")    // strcpy (v1d, "")
                    } else {
                        var s = 0; var t2 = ii + 1
                        while (C.v1d[t2] != NUL_D) {
                            C.v1d[s] = C.v1d[t2]; s++; t2++
                        }
                        C.v1d[s] = NUL_D
                    }
                    ii = 0
                    var a = 0
                    val tmp = StringBuilder()
                    justPC@ while (true) {
                        j = ii
                        while (C.v1d[ii] >= ' ' && C.v1d[ii] <= '\u007F') ii++
                        if (C.v1d[ii] == NUL_D) {
                            if (a == 0) { // \u0024job
                                // the _MSC_VER DOS arm is inert on the
                                // reference build (UNIX != 0; the
                                // H-018 fossil ledger); the linux arm:
                                C.emit("\\tsprintf((char *) tmp" + C.iiitmp +
                                    ",\"%d\",getpid()); //\u0024job linux\\n")
                            } else {
                                if (a == 1 || a > 3) return a
                                if (a == 2) tmp.append(", (UNS) \"-1\"")
                                C.emit("\\t_justify(" + " (UNS) tmp" +
                                    C.iiitmp + ", (UNS) " +
                                    tmp.toString() + ");\\n")
                            }
                            memcheckDaf(C.iiitmp)
                            putAt(bp, 0, "tmp" + C.iiitmp)
                            C.iiitmp++; memcheckDaf(C.iiitmp)
                            label = L_EXEC; break@justPC
                        }
                        C.v1d[ii] = NUL_D
                        ii++
                        if (a != 0) tmp.append(", (UNS)")
                        a++
                        tmp.append(extract2Daf(strAt(C.v1d, j)))
                    }
                    continue@walkPC
                }

                // \u0024piece
                if (startsCIAt(C.v1d, 1, "\u0024piece", 6) ||
                    startsCIAt(C.v1d, 1, "\u0024p", 2)) {
                    var ii = 1
                    while (C.v1d[ii] >= ' ' && C.v1d[ii] <= '\u007F') ii++
                    if (C.v1d[ii] == NUL_D) {
                        putAt(C.v1d, 0, "")
                    } else {
                        var s = 0; var t2 = ii + 1
                        while (C.v1d[t2] != NUL_D) {
                            C.v1d[s] = C.v1d[t2]; s++; t2++
                        }
                        C.v1d[s] = NUL_D
                    }
                    ii = 0
                    var a = 0
                    val tmp = StringBuilder()
                    piecePC@ while (true) {
                        j = ii
                        while (C.v1d[ii] >= ' ' && C.v1d[ii] <= '\u007F') ii++
                        if (C.v1d[ii] == NUL_D) {
                            if (a == 1 || a > 4) return a
                            if (a == 2) tmp.append(", (UNS) \"-1\"")
                            if (a < 4) tmp.append(", (UNS) \"-1\"")
                            memcheckDaf(C.iiitmp)
                            C.emit("\\t_piece((UNS) tmp" + C.iiitmp + ", \\n" +
                                "\\t\\t(UNS) " + extract2Daf(tmp.toString()) +
                                ", \\n" +
                                "\\t\\t0, (UNS) \"\", svPtr); // p1136\\n")
                            putAt(bp, 0, "tmp" + C.iiitmp)
                            C.iiitmp++; memcheckDaf(C.iiitmp)
                            label = L_EXEC; break@piecePC
                        }
                        C.v1d[ii] = NUL_D
                        ii++
                        if (a != 0) tmp.append(",(UNS) ")
                        a++
                        tmp.append(extract2Daf(strAt(C.v1d, j)))
                    }
                    continue@walkPC
                }

                // \u0024select
                if (startsCIAt(C.v1d, 1, "\u0024s", 2)) {
                    if (startsCIAt(C.v1d, 1, "\u0024select", 5) ||
                        startsCIAt(C.v1d, 1, "\u0024storage", 4)) {
                        var ii = 1
                        while (C.v1d[ii] >= ' ' && C.v1d[ii] <= '\u007F') ii++
                        if (C.v1d[ii] == NUL_D) {
                            putAt(C.v1d, 0, "")
                        } else {
                            var s = 0; var t2 = ii + 1
                            while (C.v1d[t2] != NUL_D) {
                                C.v1d[s] = C.v1d[t2]; s++; t2++
                            }
                            C.v1d[s] = NUL_D
                        }
                        ii = 0
                        var a = 0
                        var k = 0
                        val tmp = StringBuilder()
                        selPC@ while (true) {
                            k++
                            j = ii
                            memcheckDaf(C.iiitmp)
                            while (C.v1d[ii] >= ' ' && C.v1d[ii] <= '\u007F') ii++
                            if (C.v1d[ii] == NUL_D) {
                                if (a == 0) { // \u0024storage
                                    C.emit("\\tstrmove((UNS) tmp" + C.iiitmp +
                                        ", (UNS) " + "\"999\");\\n")
                                } else {
                                    if (k > 20) return 99
                                    while (k < 21) {
                                        tmp.append(",")
                                        tmp.append("NULL")
                                        k++
                                    }
                                    C.emit("\\t_select( (UNS) tmp" +
                                        C.iiitmp + ", (UNS) " +
                                        tmp.toString() + ", svPtr);\\n")
                                }
                                putAt(bp, 0, "tmp" + C.iiitmp)
                                C.iiitmp++; memcheckDaf(C.iiitmp)
                                label = L_EXEC; break@selPC
                            }
                            C.v1d[ii] = NUL_D
                            ii++
                            if (a != 0) tmp.append(",(UNS)")
                            a++
                            tmp.append(extract2Daf(strAt(C.v1d, j)))
                        }
                        continue@walkPC
                    } else {
                        C.ierr = 30 // function not found
                        // printf("\n*** %s\n", &v1d[1]) - console
                        // printf folds to println (ASCII, locale-free)
                        println("\n*** " + strAt(C.v1d, 1))
                        return C.ierr
                    }
                }

                // \u0024random
                if (startsCIAt(C.v1d, 1, "\u0024random", 5) ||
                    (startsCIAt(C.v1d, 1, "\u0024r", 2) &&
                     !startsCIAt(C.v1d, 1, "\u0024re", 3))) {
                    var ii = 1
                    while (C.v1d[ii] >= ' ' && C.v1d[ii] <= '\u007F') ii++
                    if (C.v1d[ii] == NUL_D) {
                        putAt(C.v1d, 0, "")
                    } else {
                        var s = 0; var t2 = ii + 1
                        while (C.v1d[t2] != NUL_D) {
                            C.v1d[s] = C.v1d[t2]; s++; t2++
                        }
                        C.v1d[s] = NUL_D
                    }
                    ii = 0
                    var a = 0
                    val tmp = StringBuilder()
                    randPC@ while (true) {
                        j = ii
                        while (C.v1d[ii] >= ' ' && C.v1d[ii] <= '\u007F') ii++
                        if (C.v1d[ii] == NUL_D) {
                            memcheckDaf(C.iiitmp)
                            C.emit("\\t_random((UNS) tmp" + C.iiitmp +
                                ", (UNS) " + tmp.toString() + ");\\n")
                            putAt(bp, 0, "tmp" + C.iiitmp)
                            C.iiitmp++; memcheckDaf(C.iiitmp)
                            label = L_EXEC; break@randPC
                        }
                        C.v1d[ii] = NUL_D
                        ii++
                        if (a != 0) tmp.append(",")
                        a++
                        tmp.append(extract2Daf(strAt(C.v1d, j)))
                    }
                    continue@walkPC
                }

                // other functions: build parameter list string
                othPC@ while (true) {
                    val t = CharArray(1024)
                    var j2 = 0
                    var ii = 1
                    while (C.v1d[ii] != NUL_D) {
                        if (C.v1d[ii].code in 32..127) {
                            t[j2] = C.v1d[ii]; j2++
                        } else break
                        ii++
                    }

                    if (C.v1d[ii] == NUL_D) {
                        t[j2] = NUL_D
                        C.emit("\\tstrcpy((char *) &gtmp[1],(const char *) \"" +
                            strAt(t, 0) + "\");\\n")
                        label = L_NO_ARG; break@othPC
                    }
                    t[j2] = NUL_D

                    val tstr0 = strAt(t, 0)
                    if (tstr0.length >= 5 && tstr0.substring(1, 5)
                            .equals("text", ignoreCase = true)) {
                        // \u0024text() function
                        return 33 // \u0024text not permitted
                        // the vendor's ExpandText(v1d,i,bp); goto
                        // fcn_proc_done pair is unreachable (dead code
                        // after the return; note-only, row 46)
                    }

                    val c1 = Character.toLowerCase(t[1])
                    if (c1 == 'o') { // \u0024order() function
                        // ExpandOrder(v1d,i,bp) - the landed seat returns
                        // the bp content (null leaves bp untouched, the
                        // unmatched-marker fold); disclosed
                        val r = expandOrderD(C.v1d, ii)
                        if (r != null) putAt(bp, 0, r)
                        label = L_FCN_PROC_DONE; break@othPC
                    }

                    if (c1 == 'd') { // \u0024data() function
                        val r = expandDataD(C.v1d, ii)
                        if (r != null) putAt(bp, 0, r)
                        label = L_FCN_PROC_DONE; break@othPC
                    }

                    if (c1 == 'n' &&
                        Character.toLowerCase(t[2]) == 'a') { // \u0024name()
                        expandNameD(C, C.v1d, ii, bp)
                        label = L_FCN_PROC_DONE; break@othPC
                    }

                    if (c1 == 'q' &&
                        Character.toLowerCase(t[2]) == 's') { // \u0024qsubscript()
                        expandQSDaf(C.v1d, ii, bp) // raised seat
                        label = L_FCN_PROC_DONE; break@othPC
                    }

                    if (c1 == 'n') { // \u0024next() function
                        expandNextDaf(C.v1d, ii, bp) // raised seat
                        label = L_FCN_PROC_DONE; break@othPC
                    }

                    if (c1 == 'g') { // \u0024get() function
                        expandGetDaf(C.v1d, ii, bp) // raised seat
                        label = L_FCN_PROC_DONE; break@othPC
                    }

                    C.emit("\\tstrmove( (UNS) &gtmp[1], (UNS) \"\");\\n")

                    // sprintf (h, "%2x", v1d[i]) glue
                    var hx = String.format(java.util.Locale.US, "%2x",
                        C.v1d[ii].code)
                    t[j2++] = '\\'
                    t[j2++] = 'x'
                    t[j2++] = hx[0]
                    t[j2++] = hx[1]
                    t[j2] = NUL_D
                    C.emit("\\tcatx = strxcat((char *) &gtmp[1],\"" +
                        strAt(t, 0) + "\");\\n")
                    ii++

                    while (true) {
                        j2 = 0
                        while (C.v1d[ii].code in 32..127) {
                            t[j2] = C.v1d[ii]; j2++; ii++
                        }
                        t[j2] = NUL_D
                        C.emit("\\tstrxcat((char *) catx ,(char *) " +
                            extract2Daf(strAt(t, 0)) + ");\\n")
                        j2 = 0
                        hx = String.format(java.util.Locale.US, "%2x",
                            C.v1d[ii].code)
                        t[j2++] = '\\'
                        t[j2++] = 'x'
                        t[j2++] = hx[0]
                        t[j2++] = hx[1]
                        t[j2] = NUL_D
                        C.emit("\\tstrxcat((char *) catx,\"" +
                            strAt(t, 0) + "\");\\n")
                        if (C.v1d[++ii] != NUL_D) continue else break
                    }

                    // falls into no_arg
                    label = L_NO_ARG; break@othPC
                }
                continue@walkPC
            }

            // retrieve look-up
            if (C.v1d[1] == PREDICATEVAR_P277) {
                putAt(bp, 0, strAt(C.v1d, 1)) // strcpy (bp, v1dp)
                label = L_EXEC; continue@walkPC
            }

            // array references - insert brackets (v1dp == v1d[1..])
            run {
                var hasArr = false
                var arrI = 0
                while (C.v1d[arrI + 1] != NUL_D) {
                    if (C.v1d[arrI + 1].code > 127) {
                        hasArr = true; break
                    }
                    arrI++
                }
                if (hasArr) {
                    C.emit("{ // local array setup\\n\\tint i;\\n")
                    arrI = 0
                    arrScanPC@ while (C.v1d[arrI + 1] != NUL_D) {
                        val chArr = C.v1d[arrI + 1]
                        if (chArr == CODED_OPEN_D) { // 206 Open
                            C.v1d[arrI + 1] = NUL_D
                            C.emit("\\tstrmove((UNS) tmp" + C.iiitmp +
                                ", (UNS) \"" + strAt(C.v1d, 1) + "\");\\n")
                            C.emit("\\ti=strlen((const char *) tmp" +
                                C.iiitmp + ");\\n")
                            C.emit("\\ttmp" + C.iiitmp + "[i]=206;\\n")
                            C.emit("\\ttmp" + C.iiitmp + "[i+1]=0;\\n")
                            C.v1d[arrI + 1] = CODED_OPEN_D
                            var jj = arrI + 1
                            while (C.v1d[jj + 1] != NUL_D) {
                                if (C.v1d[jj + 1] == CODED_CLOSE_D ||
                                    C.v1d[jj + 1] == CODED_COMMA_D) break
                                jj++
                            }
                            val kk = C.v1d[jj + 1]
                            C.v1d[jj + 1] = NUL_D
                            val seg = strAt(C.v1d, arrI + 2) // &v1dp[i+1]
                            val useArg =
                                if (seg.startsWith("#S#") || seg.startsWith("#G#"))
                                    extract2Daf(seg) else seg
                            C.emit("\\tstrcat((char *) tmp" + C.iiitmp +
                                ",(char *) " + useArg + ");\\n")
                            C.emit("\\ti=strlen((const char *) tmp" +
                                C.iiitmp + ");\\n")
                            C.emit("\\ttmp" + C.iiitmp + "[i]=" +
                                kk.code + ";\\n")
                            C.emit("\\ttmp" + C.iiitmp + "[i+1]=0;\\n")
                            C.v1d[jj + 1] = kk
                            arrI = jj - 1
                        } else if (chArr == CODED_CLOSE_D) {
                            // 207 Close (vendor value; arm const name
                            // inverted per the C-012 adjudication)
                            memcheckDaf(C.iiitmp)
                            putAt(C.setname, 0, "tmp" + C.iiitmp)
                            putAt(bp, 0, "#S#tmp" + C.iiitmp + "#tmp" +
                                (C.iiitmp + 1) + "#")
                            C.iiitmp = C.iiitmp + 2
                            memcheckDaf(C.iiitmp)
                            C.emit("\\t}\\n")
                            break@arrScanPC
                        } else if (chArr == CODED_COMMA_D) {
                            // 208 comma (vendor value; arm const name
                            // inverted per the C-012 adjudication)
                            var jj = arrI + 1
                            while (C.v1d[jj + 1] != NUL_D) {
                                if (C.v1d[jj + 1] == CODED_CLOSE_D ||
                                    C.v1d[jj + 1] == CODED_COMMA_D) break
                                jj++
                            }
                            val kk = C.v1d[jj + 1]
                            C.v1d[jj + 1] = NUL_D
                            val seg = strAt(C.v1d, arrI + 2)
                            val useArg = if (seg.startsWith("#S#"))
                                             extract2Daf(seg) else seg
                            C.emit("\\tstrcat((char *) tmp" + C.iiitmp +
                                ",(char *) " + useArg + ");\\n")
                            C.emit("\\ti=strlen((const char *) tmp" +
                                C.iiitmp + ");\\n")
                            C.emit("\\ttmp" + C.iiitmp + "[i]=" +
                                kk.code + ";\\n")
                            C.emit("\\ttmp" + C.iiitmp + "[i+1]=0;\\n")
                            C.v1d[jj + 1] = kk
                            arrI = jj - 1
                        }
                        arrI++
                    }
                } else {
                    // no_array
                    symflg = 11
                    memcheckDaf(C.iiitmp)
                    if (!cvarDaf(strAt(C.v1d, 1))) { // if (!cvar(v1dp))
                        putAt(C.setname, 0, "\"") // ??? (vendor comment)
                        catAt(C.setname, 0, strAt(C.v1d, 1))
                        catAt(C.setname, 0, "\"")
                        memcheckDaf(C.iiitmp)
                        putAt(bp, 0, "#S#" + strAt(C.setname, 0) +
                            "#tmp" + C.iiitmp + "#")
                        C.iiitmp++
                        memcheckDaf(C.iiitmp)
                    }
                }
                // no_array1
            }
            if (cvarDaf(strAt(C.v1d, 1))) {
                putAt(bp, 0, strAt(C.v1d, 1)) // strcpy(&bd[1], v1dp)
            }
            // if (pd1[sdlim] != '\u0024') goto exec; - the two arms
            // both reach exec (fall-in or jump), single transfer
            label = L_EXEC
        }

        L_NO_ARG -> {
            if (C.v1d[2] == 'Z' || C.v1d[2] == 'z') {
                memcheckDaf(C.iiitmp)
                C.emit("\\tzfcn(gtmp,tmp" + C.iiitmp + ",svPtr);\\n")
                putAt(bp, 0, "tmp" + C.iiitmp)
                C.iiitmp++; memcheckDaf(C.iiitmp)
            } else if (C.v1d[2] == '\u0024') {
                memcheckDaf(C.iiitmp)
                // MyFunc (&v1d[3], iiitmp) - raised seat
                if (myFuncDaf(strAt(C.v1d, 3), C.iiitmp) != 0) return 9002
                putAt(bp, 0, "tmp" + C.iiitmp)
                C.iiitmp++; memcheckDaf(C.iiitmp)
            } else {
                C.emit("\\tstrmove((UNS) &svPtr->v1d[1], (UNS) &gtmp[1]);\\n")
                memcheckDaf(C.iiitmp)
                C.emit("\\tfcn(svPtr);\\n")
                C.emit("\\tstrmove((UNS) tmp" + C.iiitmp +
                    ", (UNS) &svPtr->bd[1]);\\n")
                putAt(bp, 0, "tmp" + C.iiitmp)
                C.iiitmp++; memcheckDaf(C.iiitmp)
            }
            label = L_FCN_PROC_DONE
        }

        L_FCN_PROC_DONE -> {
            if (C.ierr == 0) label = L_EXEC else return C.ierr
        }

        L_EXEC -> {
            if (s1p[spx] != EMPTY_P277 && s1p[spx] != OPEN_P277 &&
                s1p[spx] != OPENC_P277) {
                label = L_NXT_EXPR; continue@walkPC
            }
            spx++
            while (C.pd1[C.sdlim++] != NUL_D) { }
            if (C.sdlim >= svPtr.symlen) return 31
            putAt(C.pd1, C.sdlim, strAt(bp, 0))
            s1p[spx] = OPERAND_P277
            label = L_NCHAR
        }

        L_NXT_EXPR -> {
            if (s1p[spx] == OPEN_P277 || s1p[spx] == OPERAND_P277) return 11

            // check for number under operator
            if (s1p[spx - 1] != OPERAND_P277) {
                when (s1p[spx]) {

                    INDIRECT_P277 -> {
                        C.emit("\\tstrmove( (UNS) xd, (UNS) " +
                            extract2Daf(strAt(bp, 0)) + ");\\n")
                        C.emit("\\tsvPtr->xpx=0;\\n")
                        C.emit("\\tsvPtr->t0px=0;\\n")
                        C.emit("\\t_i=parse_(); \\n")
                        C.emit("\\tif (_i!=0) ErrorMessage(" +
                            "\"Indirect expression error\",svPtr->LineNumber);\\n")
                        memcheckDaf(C.iiitmp)
                        C.emit("\\tstrmove( (UNS) tmp" + C.iiitmp +
                            ", (UNS) &pd1[sdlim]);\\n")
                        putAt(bp, 0, "tmp" + C.iiitmp)
                        C.iiitmp++; memcheckDaf(C.iiitmp)
                        // the three vendor statements after the
                        // first break (spx--; strcat/strcpy re-walk)
                        // are DEAD code - row 46 note-only
                    }

                    MINUS_P277 -> { // unary minus
                        memcheckDaf(C.iiitmp)
                        C.emit("\\tmult((char *) " + extract2Daf(strAt(bp, 0)) +
                            ",\"-1\",(char *) tmp" + C.iiitmp + ");\\n")
                        putAt(bp, 0, "tmp" + C.iiitmp)
                        C.iiitmp++; memcheckDaf(C.iiitmp)
                    }

                    18 -> { // not not - NO break in the vendor: falls
                        // into the NOT arm below (quirk kept by
                        // executing both bodies in sequence)
                        memcheckDaf(C.iiitmp)
                        C.emit("\\tif (numcomp1( (UNS) " +
                            extract2Daf(strAt(bp, 0)) + ",0) == 0) " +
                            "strmove( (UNS) tmp" + C.iiitmp +
                            ", (UNS) \"1\");\\n" +
                            "\\telse strmove( (UNS) tmp" + C.iiitmp +
                            ", (UNS) \"0\");\\n")
                        putAt(bp, 0, "tmp" + C.iiitmp)
                        C.iiitmp++; memcheckDaf(C.iiitmp)
                        // fallthrough into unary NOT (vend quirk)
                        memcheckDaf(C.iiitmp)
                        C.emit("\\tif (numcomp1( (UNS) " +
                            extract2Daf(strAt(bp, 0)) + ", 0) == 0) \\n" +
                            "\\t\\tstrmove( (UNS) tmp" + C.iiitmp +
                            ", (UNS) \"1\");\\n" +
                            "\\telse strmove( (UNS) tmp" + C.iiitmp +
                            ", (UNS) \"0\");\\n")
                        putAt(bp, 0, "tmp" + C.iiitmp)
                        C.iiitmp++; memcheckDaf(C.iiitmp)
                    }

                    NOT_P277 -> { // unary not
                        memcheckDaf(C.iiitmp)
                        C.emit("\\tif (numcomp1( (UNS) " +
                            extract2Daf(strAt(bp, 0)) + ", 0) == 0) \\n" +
                            "\\t\\tstrmove( (UNS) tmp" + C.iiitmp +
                            ", (UNS) \"1\");\\n" +
                            "\\telse strmove( (UNS) tmp" + C.iiitmp +
                            ", (UNS) \"0\");\\n")
                        putAt(bp, 0, "tmp" + C.iiitmp)
                        C.iiitmp++; memcheckDaf(C.iiitmp)
                    }

                    PLUS_P277 -> { // unary plus
                        memcheckDaf(C.iiitmp)
                        C.emit("\\tadd( (char *) " + extract2Daf(strAt(bp, 0)) +
                            ", (char *) \"0\", (char *) tmp" + C.iiitmp +
                            ");\\n")
                        putAt(bp, 0, "tmp" + C.iiitmp)
                        C.iiitmp++; memcheckDaf(C.iiitmp)
                    }

                    else -> return 1 // default
                }
                spx--
                label = L_NXT_OPERATOR; continue@walkPC
            }

            // extract value under operator
            adx = C.sdlim
            C.sdlim--
            while (C.pd1[C.sdlim - 1] != NUL_D) C.sdlim--

            // #G#/#S# marker fixups via Extract2 (the ad-hoc file
            // local nx3 checks == strncmp(...,3))
            if (pfx3At(C.pd1, adx, "#G#")) {
                putAt(C.pd1, adx, extract2Daf(strAt(C.pd1, adx)))
            }
            if (pfx3At(bp, 0, "#G#")) {
                putAt(bp, 0, extract2Daf(strAt(bp, 0)))
            }
            if (pfx3At(C.pd1, adx, "#S#")) {
                putAt(C.pd1, adx, extract2Daf(strAt(C.pd1, adx)))
            }
            if (pfx3At(bp, 0, "#S#")) {
                putAt(bp, 0, extract2Daf(strAt(bp, 0)))
            }

            val valA = strAt(C.pd1, adx)   // &pd1[adx] as string
            val valB = strAt(bp, 0)        // bp as string

            // repeated emit + bp-tmp + iiitmp fold (each binary arm's
            // closing trio; the memcheck precedes per vendor)
            fun binFold(line: String) {
                C.emit(line)
                putAt(bp, 0, "tmp" + C.iiitmp)
                C.iiitmp++
                memcheckDaf(C.iiitmp)
            }

            when (s1p[spx]) {

                OPERAND_P277, EMPTY_P277, NOT_P277 -> return 12

                DIVIDE_P277 -> {
                    memcheckDaf(C.iiitmp)
                    binFold("\\tdivx((char *) " + valA + ",(char *) " + valB +
                        ",(char *) tmp" + C.iiitmp + ");\\n")
                }

                EXPONEN_P277 -> {
                    memcheckDaf(C.iiitmp)
                    binFold("\\texpx((const char *) " + valA +
                        ", (const char *) " + valB + ", (char *) tmp" +
                        C.iiitmp + ");\\n")
                }

                MULTIPLY_P277 -> {
                    memcheckDaf(C.iiitmp)
                    if (conCheckD(bp) != 0) {
                        binFold("\\tmult( (const char *) " + valA + ", \"" +
                            valB + "\", (char *) tmp" + C.iiitmp + ");\\n")
                    } else {
                        binFold("\\tmult((char *) " + valA + ", (char *) " +
                            valB + ", (char *) tmp" + C.iiitmp + ");\\n")
                    }
                }

                MINUS_P277 -> {
                    memcheckDaf(C.iiitmp)
                    binFold("\\tsub((char *) " + valA + ",(char *) " + valB +
                        ",(char *) tmp" + C.iiitmp + ");\\n")
                }

                PLUS_P277 -> {
                    memcheckDaf(C.iiitmp)
                    if (conCheckD(bp) != 0) {
                        binFold("\\tadd( (char *) " + valA + ", \"" + valB +
                            "\", (char *) tmp" + C.iiitmp + " ); \\n")
                    } else {
                        binFold("\\tadd((char *) " + valA + ", (char *) " +
                            valB + ", (char *) tmp" + C.iiitmp + "); \\n")
                    }
                }

                CONCAT_P277 -> {
                    memcheckDaf(C.iiitmp)
                    C.emit("\\tif (strlen((const char *) " + valA + ") +\\n" +
                        "\\tstrlen((const char *) " + valB + ") + 1 \\n" +
                        "\\t > STR_MAX)\\n")
                    C.emit("\\tErrorMessage(\"String too long\", svPtr->LineNumber);\\n")
                    C.emit("\\tstrmove( (UNS) tmp" + C.iiitmp + ", (UNS) " +
                        valA + ");\\n")
                    C.emit("\\tstrcat( (char *) tmp" + C.iiitmp +
                        ", (char *) " + valB + ");\\n")
                    putAt(bp, 0, "tmp" + C.iiitmp)
                    C.iiitmp++; memcheckDaf(C.iiitmp)
                }

                EQUALS_P277, NOTEQ_P277 -> {
                    if (s1p[spx] != NOTEQ_P277) { // EQUALS
                        memcheckDaf(C.iiitmp)
                        binFold("\\tif (strcmp((const char *) " + valA +
                            ",(const char *) " + valB + ")==0) \\n" +
                            "\\tstrmove( (UNS) tmp" + C.iiitmp +
                            ", (UNS) \"1\");\\n" +
                            "\\telse strmove( (UNS) tmp" + C.iiitmp +
                            ", (UNS) \"0\");\\n")
                    } else { // NOTEQ
                        memcheckDaf(C.iiitmp)
                        binFold("\\tif (strcmp((const char *) " + valA +
                            ", (const char *) " + valB + ") != 0)\\n" +
                            "\\t\\tstrmove((UNS) tmp" + C.iiitmp +
                            ", (UNS) \"1\");\\n" +
                            "\\telse strmove((UNS) tmp" + C.iiitmp +
                            ", (UNS) \"0\");\\n")
                    }
                }

                GREATER_P277 -> {
                    memcheckDaf(C.iiitmp)
                    if (conCheckD(bp) != 0) {
                        binFold("\\tif (numcomp1((UNS) " + valA + ", " + valB +
                            ") > 0) strmove((UNS) tmp" + C.iiitmp +
                            ", (UNS) \"1\");\\n" +
                            "\\telse strmove((UNS) tmp" + C.iiitmp +
                            ", (UNS) \"0\");\\n")
                    } else {
                        binFold("\\tif (numcomp((UNS) " + valA + ", (UNS) " +
                            valB + ") > 0) " +
                            "strmove((UNS) tmp" + C.iiitmp +
                            ", (UNS) \"1\");\\n" +
                            "\\telse strmove((UNS) tmp" + C.iiitmp +
                            ", (UNS) \"0\");\\n")
                    }
                }

                LESSTHAN_P277 -> {
                    memcheckDaf(C.iiitmp)
                    if (conCheckD(bp) != 0) {
                        binFold("\\tif (numcomp1((UNS) " + valA + "," + valB +
                            ")<0) strmove((UNS) tmp" + C.iiitmp +
                            ", (UNS) \"1\");\\n" +
                            "\\telse strmove((UNS) tmp" + C.iiitmp +
                            ", (UNS) \"0\");\\n")
                    } else {
                        binFold("\\tif (numcomp((UNS) " + valA + ", (UNS) " +
                            valB + ") < 0) strmove((UNS) tmp" + C.iiitmp +
                            ", (UNS) \"1\");\\n" +
                            "\\telse strmove((UNS) tmp" + C.iiitmp +
                            ", (UNS) \"0\");\\n")
                    }
                }

                INTDIVIDE_P277 -> {
                    memcheckDaf(C.iiitmp)
                    binFold("\\tdivi((const char *) " + valA +
                        ",(const char *) " + valB + ",(char *) tmp" +
                        C.iiitmp + ");\\n")
                }

                MODULO_P277 -> {
                    memcheckDaf(C.iiitmp)
                    C.emit("\\t_i=atoi((char *) " + valB + ");\\n")
                    C.emit("\\t_j=atoi((char *) " + valA + ");\\n")
                    C.emit("\\t_i=_j%_i;\\n")
                    C.emit("\\tMltoa(_i, tmp" + C.iiitmp + ");\\n")
                    putAt(bp, 0, "tmp" + C.iiitmp)
                    C.iiitmp++; memcheckDaf(C.iiitmp)
                }

                CONTAINS_P277 -> {
                    memcheckDaf(C.iiitmp)
                    binFold("\\tif (strstr((const char *) " + valA +
                        ", (const char *)" + valB + ")) strmove((UNS) tmp" +
                        C.iiitmp + ", (UNS) \"1\");" +
                        " else strmove((UNS) tmp" + C.iiitmp +
                        ", (UNS) \"0\");\\n")
                }

                FOLLOWS_P277 -> {
                    memcheckDaf(C.iiitmp)
                    binFold("\\tif (strcmp((const char *) " + valA +
                        ",(const char *) " + valB + ")>0) strmove((UNS) tmp" +
                        C.iiitmp + ", (UNS) \"1\");" +
                        " else strmove((UNS) tmp" + C.iiitmp +
                        ", (UNS) \"0\");\\n")
                }

                PATTERN_P277 -> {
                    fixstrDaf(bp) // fixstr (&bd[1]) - raised seat
                    memcheckDaf(C.iiitmp)
                    C.emit("\\tif (pm( (unsigned char *) " + valA +
                        ", (const unsigned char *) \"" + valB +
                        "\", svPtr)) " +
                        "\\n\\tstrmove((UNS) tmp" + C.iiitmp +
                        ", (UNS) \"1\"); // pattern match" +
                        "\\n\\telse strmove((UNS) tmp" + C.iiitmp +
                        ",(UNS) \"0\");\\n")
                    putAt(bp, 0, "tmp" + C.iiitmp) // sprintf (&bd[1], ...)
                    C.iiitmp++; memcheckDaf(C.iiitmp)
                }

                NOTGREATER_P277 -> {
                    memcheckDaf(C.iiitmp)
                    binFold("\\tif (numcomp((UNS) " + valA + ", (UNS) " +
                        valB + ") <= 0) strmove((UNS) tmp" + C.iiitmp +
                        ", (UNS) \"1\");\\n" +
                        "\\telse strmove((UNS) tmp" + C.iiitmp +
                        ", (UNS) \"0\");\\n")
                }

                NOTLESS_P277 -> {
                    memcheckDaf(C.iiitmp)
                    binFold("\\tif (numcomp( (UNS) " + valA + ", (UNS) " +
                        valB + ")>=0) " +
                        "strmove( (UNS) tmp" + C.iiitmp +
                        ", (UNS) \"1\");\\n" +
                        "\\telse strmove( (UNS) tmp" + C.iiitmp +
                        ", (UNS) \"0\");\\n")
                }

                NOTCONTAINS_P277 -> {
                    memcheckDaf(C.iiitmp)
                    binFold("\\tif (!strstr( (const char *)" + valA +
                        ", (const char *)" + valB + ")) " +
                        "strmove( (UNS) tmp" + C.iiitmp +
                        ", (UNS) \"1\");" +
                        " else strmove( (UNS) tmp" + C.iiitmp +
                        ", (UNS) \"0\");\\n")
                }

                NOTFOLLOWS_P277 -> {
                    memcheckDaf(C.iiitmp)
                    binFold("\\tif (strcmp((const char *) " + valA +
                        ",(const char *) " + valB + ")<=0) " +
                        "strmove( (UNS) tmp" + C.iiitmp +
                        ", (UNS) \"1\");" +
                        " else strmove( (UNS) tmp" + C.iiitmp +
                        ", (UNS) \"0\");\\n")
                }

                NOTPATTERN_P277 -> {
                    memcheckDaf(C.iiitmp)
                    fixstrDaf(bp) // fixstr (&bd[1]) - raised seat
                    C.emit("\\tif (pm( (unsigned char *) " + valA +
                        ", (const unsigned char *) \"" + valB + "\", " +
                        "svPtr)) \\n\\tstrmove( (UNS) tmp" + C.iiitmp +
                        ", (UNS) \"0\"); // pattern match" +
                        "\\n\\telse strmove( (UNS) tmp" + C.iiitmp +
                        ", (UNS) \"1\");\\n")
                    putAt(bp, 0, "tmp" + C.iiitmp)
                    C.iiitmp++; memcheckDaf(C.iiitmp)
                }

                AND_P277 -> {
                    memcheckDaf(C.iiitmp)
                    binFold("\\tif (numcomp( (UNS) " + valA +
                        ", (UNS) \"0\") && numcomp( (UNS) " + valB + ", " +
                        "(UNS) \"0\"))" +
                        " strmove( (UNS) tmp" + C.iiitmp +
                        ", (UNS) \"1\"); " +
                        "else strmove( (UNS) tmp" + C.iiitmp +
                        ", (UNS) \"0\");\\n")
                }

                OR_P277 -> {
                    memcheckDaf(C.iiitmp)
                    binFold("\\tif (numcomp1( (UNS) " + valA +
                        ", 0) || numcomp1( (UNS) " + valB + ", 0))" +
                        " strmove( (UNS) tmp" + C.iiitmp +
                        ", (UNS) \"1\"); " +
                        "else strmove( (UNS) tmp" + C.iiitmp +
                        ", (UNS) \"0\");\\n")
                }

                NOTAND_P277 -> {
                    memcheckDaf(C.iiitmp)
                    binFold("\\tif (numcomp1( (UNS) " + valA +
                        ", 0) && numcomp1( (UNS) " + valB + ", 0))" +
                        " strmove( (UNS) tmp" + C.iiitmp +
                        ", (UNS) \"0\"); " +
                        "else strmove( (UNS) tmp" + C.iiitmp +
                        ", (UNS) \"1\");\\n")
                }

                NOTOR_P277 -> {
                    memcheckDaf(C.iiitmp)
                    binFold("\\tif (numcomp1( (UNS) " + valA +
                        ", 0) || numcomp1( (UNS) " + valB + ", 0)) \\n" +
                        "\\t\\tstrmove( (UNS) tmp" + C.iiitmp +
                        ", (UNS) \"0\"); \\n" +
                        "\\t\\telse strmove( (UNS) tmp" + C.iiitmp +
                        ", (UNS) \"1\"); \\n")
                }

                else -> return 12
            } // switch

            label = L_DEC_STK
        }

        L_DEC_STK -> {
            spx -= 2
            label = L_NXT_OPERATOR
        }

        L_NXT_OPERATOR -> {
            if (s1p[spx] != OPEN_P277 && s1p[spx] != OPENC_P277 &&
                s1p[spx] != EMPTY_P277) {
                label = L_NXT_EXPR; continue@walkPC
            }
            // push answer
            spx++
            C.sdlim += lenAt(C.pd1, C.sdlim) + 1
            if (C.sdlim >= svPtr.symlen) return 31
            putAt(C.pd1, C.sdlim, strAt(bp, 0))
            s1p[spx] = OPERAND_P277
            label = L_NCHAR
        }

        L_FINISH -> {
            // exit sequence
            if (s1p[spx - 1] != EMPTY_P277 || s1p[spx] != OPERAND_P277) {
                return 16
            } else return 0
        }

        } // when
    } // while
}

