package mumps.compiler

import java.io.IOException
import java.util.Locale

/**
 * PRO-arm translation of compiler-parse.c::parse_ (batch 9 unit,
 * roster row 248, file rank 302). Translated independently from
 * the pristine vendor source (mumps-14-may-2026.tgz, sha256
 * 621deb25...ffdbe), reference source lines 203-1998.
 *
 * Identity verified against the study tool (tools/select_study_
 * subset.py): compiler-parse.c::parse_, c, loc 1201,
 * comment_ratio 0.134, pointer_marks 66, control_marks 346,
 * score 765.3 - exact match to wp1/study-subset/units.csv row 248,
 * the largest unit of the batch. The B1-era Stubs.kt parse_ stub
 * is RESOLVED with this landing (EveryCode/SetCode/KillCode/
 * Getstr1/WriteCode and the emitted-C text call sites bind
 * unchanged); its vendor file-axis helpers landed ahead of it:
 * ParseMisc (u220 banner residue, the SSS/TTT carriers),
 * Extract2 (u15), Extract2a (u231), ExpandText (u235),
 * ExpandName/ExpandOrder/ExpandData/ExpandQS/ExpandNext/ExpandGet
 * (earlier batches), Extract (u198), MyFunc, ConCheck (rendered
 * here as the unit's private helper, cannon strshift precedent),
 * patrn1, fixstr (FixStr), cannon (Cannon.kt, unit 72 shape) and
 * the memcheck/cvar/Extract1 Stubs stubs.
 *
 * parse_(): the expression parser/code generator at the heart of
 * the compiler - the operator-stack (s1p) state machine over the
 * xd text: coded-paren recursion (CodedOpen 206 rewinds xpx and
 * pushes v1d onto the pd1 stack), comma join of stack tops
 * (CodedComma 208), close-paren fcn/array return (CodedClose
 * 207), the <dollar>select ':' unification arm, numeric/literal/variable
 * operand scans, global array reference build (the gtmp emission
 * ladder), the built-in intrinsic ladder (<dollar>test <dollar>io <dollar>principal
 * <dollar>x <dollar>y <dollar>horolog <dollar>char <dollar>fnumber <dollar>extract <dollar>length <dollar>find <dollar>ascii
 * <dollar>justify <dollar>piece <dollar>select <dollar>random and the zfcn/MyFunc/fcn
 * closure), the local-array 206/207/208 bracket-insertion walk,
 * the unary INDIRECT/MINUS/not-not/NOT/PLUS postfix ladder, the
 * binary operator emission switch (divx expx mult sub add
 * numcomp strcmp strstr pm moduli) and the push/return tails.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h001 io_stream_param : Out is the java.io.Writer on
 *    CompilerState (fprintf(Out,...) -> st.out.write).
 *  - h004 globals_ext     : iiitmp/setname/ErrChar/SSS/TTT ride
 *    their DONE CompilerState carriers (u220/u221); the s1p stack
 *    is the vendor local char s1p[40] kept function-local.
 *  - h007 char_buffer     : the code/opcode/ncode/dcode/operand
 *    tables are private file-level arrays built from the vendor
 *    initializers verbatim; cod209 is the 2-byte colon cluster.
 *  - h009 format_string   : every emitted C conversion keeps its
 *    text; the embedded compiler side emits tmp refs by integer
 *    (sprintf(bp,"tmp%d") -> String + writeCstring, ExpandText
 *    precedent), and "..%d.." emissions use Java Integer
 *    toString; the %2x hex pair sprintf (vendor 1347) rides
 *    String.format(java.util.Locale...) - u182 locale carrier.
 *  - h011 goto            : nchar (hot backward target) -> the
 *    outer while(nchar)+continue; start (recursion entry) ->
 *    an outer loop pass per recursion; exec/nxt_expr/nxt_operator
 *    dec_stk/finish/un_nest/var1/scan_operand/skip_numeric ->
 *    labeled-run state machine over an enum-free Int phase
 *    cursor with while loops, every vendor gotos kept one-to-
 *    one in comments at its jump site (the h011 roster notes
 *    the fold; the fall-through switch of case 18 -> NOT keeps
 *    the vendor missing break as a pass-through flag).
 *  - h012 unsigned_byte_domain: xd/bd/v1d/pd1/... byte domains
 *    (Char 0..255); the 206/207/208/209 coded bytes are Char
 *    literals; xpx is the Long carrier indexed via toInt() at
 *    every use (h010 Long<->Int narrowing at every index site).
 *  - h013 dead_store      : the vendor unreachable break/break/
 *    goto-nchar fossil block after the INDIRECT case (lines
 *    1540-1548 - three statements after break, unreachable)
 *    is kept as a comment; 'int kkk' and the unused r in the
 *    intrinsic arms are dropped; the _MSC_VER <dollar>job DOS arm is
 *    excluded (h018).
 *  - h014 out_of_scope_call: memcheck/cvar/Extract1 (Stubs
 *    stubs, not corpus units); patrn1/fixstr/cannon/Extract(/
 *    Extract2)/MyFunc/Expand* are the DONE seats; ConCheck
 *    (compiler-parse.c:188, 14 physLOC, below min_loc - not a
 *    corpus unit, missed by the u220 residue ledger) renders
 *    here as a private helper (cannon strshift precedent).
 *  - h015 grammar_collision: the Kotlin keyword 'in' appears in
 *    no identifier; s1p/SSS/TTT keep vendor names (SSS/TTT see
 *    the u220 gate-exception note).
 *  - h017 naming          : parse_ keeps its vendor name (the
 *    trailing underscore, Doty precedent); the numeric %.15g
 *    reformat and scan are private helpers rescan15g/... at the
 *    file bottom.
 *  - h018 preprocessor_conditional: the _MSC_VER arms are
 *    excluded (reference build is the UNIX arm; the DOS <dollar>job
 *    emission is the excluded side).
 *
 * Vendor notes (translated faithfully, not \"fixed\"):
 *  - The ','-arm <dollar><dollar> double-dollar zero-arg insert shifts the
 *    xd suffix right by one in place (buffer in place, vendor
 *    comment) - kept via the single 2048 slab copy.
 *  - The ')'-arm precedence section reads s1p[spx-1] OPEN and
 *    never pushes OPEN anywhere in the file (the vendor OPEN 0
 *    vs OPENC 6 distinction) - the dead check is kept.
 *  - The ':' \"<dollar>select\" unification arm tests pd1[i]=='<dollar>' with
 *    a raw dollar byte and shifts the string down one to unify
 *    with the cod209 colon suffix; the raw dollar byte in CODE
 *    is emitted as the 36.toChar() comparison carrier (no raw
 *    dollar in Kotlin source, gate rule).
 *  - The not-not case 18 (NOTEQ 18 == NOT_NOT) falls through
 *    into the NOT case (no break in the vendor): the emitted C
 *    text contains BOTH negation snippets - kept with the same
 *    fall-through.
 *  - <dollar>x and <dollar>y both emit ver[io] (the vendor <dollar>x ships the ver
 *    array as well - kept verbatim).
 *  - The \<dollar>text arm has a return 33 BEFORE the ExpandText call
 *    (dead call, vendor) - kept as the immediate return.
 *  - The global reference ladder appends '\\x0' plus '0'/'1'
 *    (naked-indicator byte) exactly; the ZZ no-eval interface
 *    test reads pd1[sdlim+1]/[sdlim+2] via toupper.
 *  - The local array walk emits the 206/207/208 byte values as
 *    %d of the Char code (tmp%d[i]=%d with k) - kept.
 *  - numeric operands: sscanf %lf then sprintf %.15g - the
 *    rescan drops trailing junk the vendor drops (sscanf
 *    prefix semantics, u249 scanIntPrefix family note); the
 *    literal-quote arm keeps the vendor doubled-quote
 *    escape fold (xd[++xpx] pre-increment read past the quote).
 *  - EMPTY is 10 IN THIS TU (the compiler-parse.c local
 *    define block overrides) - not the interp.h EMPTY(5):
 *    the table below comments the local arithmetic.
 */

// --- vendor compiler-parse.c local defines (88-127) ---
private const val OPEN = 0 // vendor :99 '= OPEN 0'
private const val DIVIDE = 1
private const val MULTIPLY = 2
private const val MINUS = 3
private const val PLUS = 4
private val OPERAND = 5.toChar()
private val OPENC = 6.toChar()
private const val CONCAT = 7
private const val EQUALS = 8
private const val GREATER = 9
private val EMPTY = 10.toChar() // vendor :107 (TU-local, NOT interp.h EMPTY 5)
private const val LESSTHAN = 11
private val NOT = 12.toChar()
private const val INTDIVIDE = 13
private const val MODULO = 14
private const val CONTAINS = 15
private const val FOLLOWS = 16
private val PATTERN = 17.toChar()
private const val NOTEQ = 18 // NOT_NOT; falls through to NOT in the unary switch
private const val NOTGREATER = 19
private const val NOTLESS = 20
private const val NOTCONTAINS = 21
private const val NOTFOLLOWS = 22
private val NOTPATTERN = 23.toChar()
private const val AND = 24
private const val OR = 25
private const val NOTAND = 26
private const val NOTOR = 27
private const val INDIRECT = 28
private const val EXPONEN = 29
private const val PREDICATEVAR = '~'

private const val QUOTE = '\'' // vendor compiler-parse.c:120
private const val CodedOpen = 206 // vendor compiler-parse.c:38
private val CodedComma = 208.toChar()
private val CodedClose = 207.toChar()
private const val DOLLAR = 36 // the raw-dollar byte carrier (gate rule)

private val cod209 = charArrayOf(209.toChar(), NUL) // vendor static cod209[2] = { 209, 0 }

// vendor static unsigned char code[26] (optcode -> token map), exact initializer
private val code = CharArray(26) { 99.toChar() }.also { c ->
    c[8] = 18.toChar(); c[9] = 19.toChar(); c[11] = 20.toChar(); c[12] = 18.toChar()
    c[15] = 21.toChar(); c[16] = 22.toChar(); c[17] = 23.toChar(); c[24] = 26.toChar(); c[25] = 27.toChar()
}

// vendor static unsigned char opcode[256] (char -> op token), exact initializer
private val opcode = CharArray(256) { 99.toChar() }.also { op ->
    op[33] = 25.toChar() // '!' OR
    op[35] = 14.toChar() // '#' MODULO
    op[38] = 24.toChar() // '&' AND
    op[39] = 12.toChar() // apostrophe NOT
    op[40] = 0.toChar()  // '(' OPEN
    op[42] = 2.toChar()  // '*' MULTIPLY
    op[43] = 4.toChar()  // '+' PLUS
    op[45] = 3.toChar()  // '-' MINUS
    op[47] = 1.toChar()  // '/' DIVIDE
    op[60] = 11.toChar() // '<' LESSTHAN
    op[61] = 8.toChar()  // '=' EQUALS
    op[62] = 9.toChar()  // '>' GREATER
    op[63] = 17.toChar() // '?' PATTERN
    op[64] = 28.toChar() // '@' INDIRECT
    op[91] = 15.toChar() // '[' CONTAINS
    op[92] = 13.toChar() // '' INTDIVIDE
    op[93] = 16.toChar() // ']' FOLLOWS
    op[95] = 7.toChar()  // '_' CONCAT
}

// vendor static unsigned char ncode[256] (numeric-start test, ==10)
private val ncode = CharArray(256) { 99.toChar() }.also { n ->
    n[46] = 10.toChar() // '.'
    for (d in 48..57) n[d] = 10.toChar() // digits
}

// vendor static unsigned char dcode[256] (variable-name delimiter test, ==10)
private val dcode = CharArray(256) { 99.toChar() }.also { d ->
    for (ix in intArrayOf(0, 32, 33, 35, 38, 39, 41, 42, 43, 44, 45, 47, 58, 60, 61, 62, 63, 64, 91, 92, 93, 95)) {
        d[ix] = 10.toChar()
    }
}

// vendor static unsigned char operand[256] (operand-start test, ==1)
private val operand = CharArray(256).also { o ->
    for (ix in intArrayOf(34, 36, 37, 46, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 94, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122)) {
        o[ix] = 1.toChar()
    }
    for (ix in intArrayOf(33, 38, 60, 61, 62, 63, 91, 93)) {
        o[ix] = 2.toChar()
    }
}

/** h014 helper: vendor ConCheck (compiler-parse.c:188, below min_loc;
 *  omitted from the u220 residue ledger) - the constant-quoted check
 *  that also strips the quotes in place via the SSS shift. */
private fun ConCheck(b: CharArray): Int {
    val st = CompilerState
    val bs = cstringAt(b)
    if (bs.isEmpty()) return 0
    if (b[0] != '"') return 0
    if (bs[bs.length - 1] != '"') return 0
    for (i in 1 until bs.length) {
        if (b[i] == '"') break
        if (!b[i].isDigit()) return 0
    }
    // vendor: for (SSS=b; *SSS != 0; SSS++) *SSS = *(SSS+1);
    var sss = 0
    while (b[sss] != NUL) {
        b[sss] = b[sss + 1]
        sss++
    }
    b[bs.length - 1] = NUL // b[strlen(b)-1]=0
    st.SSS = b
    st.SSSOff = sss // cursor residue kept on the carrier
    return 1
}

@Throws(IOException::class)
fun parse_(): Int {
    val st = CompilerState

    var xpxOrg = 0 // vendor short xpxOrg
    var symflg = 0 // vendor short symflg (11 in the no_array arm)
    var ernbr = 0
    var f = 0 // vendor short f
    var spx = 0 // the operator-stack pointer
    var adx = 0
    var jpx = 0
    var j = 0
    var i = 0
    var nxtchr: Char
    val s1p = CharArray(40) // vendor static unsigned char s1p[40]
    val bpBuf = CharArray(2048) // LOCAL bp view (bp == &bd[1] in the vendor); sync'd via bpSet
    val jpxH = ShortArray(1)
    val ernbrH = ShortArray(1)

    var sdLim = 0 // the pd1 stack-limit cursor mirror (st.sdlim carrier below)

    // bp == &bd[1]: every read/write rides the bd buffer at offset 1.
    fun bpGet(): String = cstringAt(st.bd, 1)
    fun bpSet(s: String) { strcpyC(st.bd, s, 1) }
    fun bpSetSlab(src: CharArray) { // strcpy(bp,&pd1[...]) view over a slab buffer
        var k = 0
        while (src[k] != NUL) k++
        System.arraycopy(src, 0, bpBuf, 0, k)
        bpBuf[k] = NUL
        // mirror to bd[1..] (bp aliases bd) for downstream bd-side reads
        var m = 1
        for (q in 0 until k) { st.bd[m++] = src[q] }
        st.bd[m] = NUL
    }

    fun pushBpOntoStack() { // strcpy(&pd1[sdlim],bp) + OPERAND mark, shared tails
        strcpyC(st.pd1, bpGet(), sdLim)
        s1p[spx] = OPERAND
    }


    fun sdFlushName(): Int { // while (pd1[sdlim]) sdlim++; - flush to open slot
        while (st.pd1[sdLim] != NUL) sdLim++
        return sdLim
    }

    fun bpSetImpl() { // sprintf(bp,"tmp%d",iiitmp); iiitmp++; memcheck(iiitmp)
        bpSet("tmp" + st.iiitmp)
        st.iiitmp++
        memcheck(st.iiitmp)
    }

    fun fillBpBuf() { // bp == and bd[1]: load the bpBuf slab from bd[1..]
        var n = 0
        while (st.bd[n + 1] != NUL) { bpBuf[n] = st.bd[n + 1]; n++ }
        bpBuf[n] = NUL
    }

    fun flushBpBuf() { // sync the bp slab back into bd[1..]
        var m = 0
        while (bpBuf[m] != NUL) m++
        var w = 1
        for (q in 0 until m) { st.bd[w++] = bpBuf[q] }
        st.bd[w] = NUL
    }

    fun cannonAt() { // vendor: cannon(bp) over the bp == and bd[1] alias
        fillBpBuf()
        cannon(bpBuf)
        flushBpBuf()
    }


    fun conCheckBp(): Boolean { // vendor ConCheck(bp) (may mutate bp; sync kept)
        fillBpBuf()
        val r = ConCheck(bpBuf) != 0
        flushBpBuf()
        return r
    }

    fun shiftArgsDown(iAt: Int) { // for (SSS=v1d,TTT=&v1d[i+1]; *TTT; SSS++,TTT++) *SSS=*TTT; *SSS=0
        var s = 0
        var tt = iAt + 1
        while (st.v1d[tt] != NUL) {
            st.v1d[s] = st.v1d[tt]
            s++; tt++
        }
        st.v1d[s] = NUL
    }

    fun fixstrBp() { // vendor fixstr(and bd[1]) slab form
        fillBpBuf()
        FixStr.fixstr(bpBuf)
        flushBpBuf()
    }

    fun callExpandBp(block: (CharArray) -> Int): Int { // Expand*/Extract seats take a bp CharArray
        fillBpBuf()
        val r = block(bpBuf)
        flushBpBuf()
        return r
    }

    // phase cursor: the h011 fold of the vendor goto web
    var phase = 0
    val P_START = 1; val P_NCHAR = 2; val P_SCAN_OPERAND = 3; val P_SKIP_NUMERIC = 4
    val P_VAR1 = 5; val P_EXEC = 6; val P_NXT_EXPR = 7; val P_NXT_OPERATOR = 8
    val P_FINISH = 9; val P_NOARG = 10

    // ---------------- initialization (before start:) ----------------
    st.ierr = 0 // vendor short ierr mirror at the state carrier
    xpxOrg = st.xpx.toInt()
    st.xpx--
    spx = 0
    st.pd1[st.pd1len.toInt() + 2] = NUL
    st.pd1[st.pd1len.toInt() + 3] = NUL
    sdLim = st.pd1len.toInt() + 3
    st.sdlim = sdLim.toShort()

    phase = P_START

    outer@ while (true) {

        when (phase) {

        P_START -> { // start:
            spx++
            s1p[spx] = EMPTY // stack foundation
            phase = P_NCHAR
        }

        P_NCHAR -> { // nchar:
            st.xpx++
            nxtchr = st.xd[st.xpx.toInt()]
            if (nxtchr == NUL) { phase = P_FINISH; continue@outer }

            if (operand[nxtchr.code] == 1.toChar()) { phase = P_SCAN_OPERAND; continue@outer }

            ////-----------------------------------------------------------------------------------------------------------------
            // CodedOpen recursion arm 206
            ////-----------------------------------------------------------------------------------------------------------------
            if (nxtchr == CodedOpen.toChar()) {
                spx++
                while (st.pd1[sdLim++] != NUL) { } // slide sdlim past the current top
                if (sdLim >= st.symlen) { st.sdlim = sdLim.toShort(); return 31 }
                var p1 = 1
                var p2 = sdLim
                while (true) { // while (*p2++ = *p1++);
                    st.pd1[p2] = st.v1d[p1]
                    val done = st.v1d[p1] == NUL
                    p1++; p2++
                    if (done) break
                }
                p2--
                st.pd1[p2] = CodedOpen.toChar()
                s1p[spx] = OPENC
                phase = P_NCHAR; continue@outer
            }

            ////-----------------------------------------------------------------------------------------------------------------
            // comma arm
            ////-----------------------------------------------------------------------------------------------------------------
            if (nxtchr == ',') {

                if (st.xd[st.xpx.toInt() + 1] == ',' &&
                    st.xd[xpxOrg] == DOLLAR.toChar() && st.xd[xpxOrg + 1] == DOLLAR.toChar()) {
                    // <dollar><dollar>^a(a,,b) case -> insert zero arg (shift the xd suffix right one)
                    val tmpS = CharArray(2048)
                    var k = st.xpx.toInt() + 1
                    var q = 0
                    while (st.xd[k] != NUL) { tmpS[q++] = st.xd[k]; k++ }
                    tmpS[q] = NUL
                    st.xd[st.xpx.toInt() + 1] = '0'
                    var w = st.xpx.toInt() + 2
                    q = 0
                    while (tmpS[q] != NUL) { st.xd[w++] = tmpS[q]; q++ }
                    st.xd[w] = NUL
                }

                if (s1p[spx - 1] != OPENC) {
                    if (st.t2 > 0) { phase = P_FINISH; continue@outer }
                    return 16
                }

                // concatenate stack tops and add coded comma
                sdLim--
                i = sdLim
                while (true) { // for (i = --sdlim; (pd1[i] = pd1[i+1]); i++);
                    st.pd1[i] = st.pd1[i + 1]
                    if (st.pd1[i] == NUL) break
                    i++
                }
                st.pd1[i] = CodedComma
                st.pd1[i + 1] = NUL
                sdLim--
                while (st.pd1[sdLim--] != NUL) { } // find new stack top start
                sdLim += 2
                spx--
                phase = P_NCHAR; continue@outer
            }

            ////-----------------------------------------------------------------------------------------------------------------
            // close paren arm
            ////-----------------------------------------------------------------------------------------------------------------
            if (nxtchr == ')') {

                if (s1p[spx - 1] != OPEN.toChar()) {
                    if (s1p[spx - 1] != OPENC) {
                        if (s1p[spx] == OPENC) { // fcn() type ref
                            // un_nest: copy answer from stack
                            while (st.pd1[--sdLim] != NUL) { }
                            strcpyC(st.v1d, cstringAt(st.pd1, sdLim + 1), 1)
                            while (st.pd1[--sdLim] != NUL) { }
                            sdLim++
                            spx -= 2
                            phase = P_VAR1; continue@outer
                        } else return 16
                    }

                    // fcn/array return section: join tops, coded close
                    sdLim--
                    i = sdLim
                    while (true) {
                        st.pd1[i] = st.pd1[i + 1]
                        if (st.pd1[i] == NUL) break
                        i++
                    }
                    st.pd1[i] = CodedClose
                    st.pd1[i + 1] = NUL
                    sdLim--
                    while (st.pd1[sdLim--] != NUL) { }
                    sdLim += 2
                    spx--
                    // un_nest: copy answer from stack
                    while (st.pd1[--sdLim] != NUL) { }
                    strcpyC(st.v1d, cstringAt(st.pd1, sdLim + 1), 1)
                    while (st.pd1[--sdLim] != NUL) { }
                    sdLim++
                    spx -= 2
                    phase = P_VAR1; continue@outer
                }

                // precedence close paren
                if (s1p[spx] != OPERAND) return 12

                // extract value from stack top
                bpSet(cstringAt(st.pd1, sdLim))
                do { sdLim-- } while (st.pd1[sdLim - 1] != NUL) // compress stack

                // dec_stk:
                spx -= 2
                phase = P_NXT_OPERATOR; continue@outer
            }

            ////-----------------------------------------------------------------------------------------------------------------
            // colon arm (<dollar>select unification)
            ////-----------------------------------------------------------------------------------------------------------------
            if (nxtchr == ':') {
                i = sdLim
                do { i-- } while (st.pd1[i - 1] != NUL)

                if (st.pd1[i] != DOLLAR.toChar() || st.pd1[i + 1].lowercaseChar() != 's') {
                    phase = P_FINISH; continue@outer
                }

                sdLim--
                st.SSS = st.pd1; st.SSSOff = sdLim
                do { // shift string down one and unify with the <dollar>select colon
                    st.pd1[st.SSSOff] = st.pd1[st.SSSOff + 1]
                    st.SSSOff++
                } while (st.pd1[st.SSSOff] != NUL)

                while (st.pd1[sdLim--] != NUL) { }
                sdLim += 2
                var e = sdLim
                while (st.pd1[e] != NUL) e++
                var ci = 0
                while (cod209[ci] != NUL) { st.pd1[e++] = cod209[ci]; ci++ }
                st.pd1[e] = NUL

                spx--
                phase = P_NCHAR; continue@outer
            }

            if (nxtchr == ' ' || nxtchr == '\t') { phase = P_FINISH; continue@outer }

            if (nxtchr == '^' && st.t2 == 2.toShort() && s1p[spx] == OPERAND) {
                phase = P_FINISH; continue@outer
            }

            ////-----------------------------------------------------------------------------------------------------------------
            // check for delimiters in list
            ////-----------------------------------------------------------------------------------------------------------------
            spx++

            if (nxtchr == ']') {
                if (st.xd[st.xpx.toInt() + 1] == ']') {
                    st.xpx++
                    s1p[spx] = FOLLOWS.toChar() // ] and ]] do the same for this version
                    phase = P_NCHAR; continue@outer
                }
            }

            if (nxtchr == QUOTE) { // insert not-based operator code
                st.xpx++
                s1p[spx] = opcode[st.xd[st.xpx.toInt()].code]

                if (s1p[spx] != 99.toChar()) {
                    s1p[spx] = code[opcode[st.xd[st.xpx.toInt()].code].code]
                    if (st.xd[st.xpx.toInt() + 1] == ']') st.xpx++ // bypass second ] in ]]
                    if (s1p[spx] != 99.toChar()) { phase = P_NCHAR; continue@outer }
                }
                st.xpx--
            }

            if (nxtchr == '*' && st.xd[st.xpx.toInt() + 1] == '*') { // exponentiation code
                st.xpx++
                s1p[spx] = EXPONEN.toChar()
                phase = P_NCHAR; continue@outer
            }

            // insert code
            s1p[spx] = opcode[nxtchr.code]
            if (s1p[spx] != 99.toChar()) { phase = P_NCHAR; continue@outer }
            st.ErrChar = nxtchr
            return 12
        }

        P_SCAN_OPERAND -> { // scan_operand:
            jpx = st.xpx.toInt()
            jpxH[0] = jpx.toShort()

            if (s1p[spx] == NOTPATTERN || s1p[spx] == PATTERN) {
                st.bd[1] = NUL
                patrn1(jpxH, ernbrH)
                ernbr = ernbrH[0].toInt()
                if (ernbr != 0) return ernbr
                jpx = jpxH[0].toInt()
                phase = P_EXEC; continue@outer
            }

            ////-----------------------------------------------------------------------------------------------------------------
            // numeric operand
            ////-----------------------------------------------------------------------------------------------------------------
            if (ncode[st.xd[st.xpx.toInt()].code] == 10.toChar()) {

                if (st.xd[st.xpx.toInt() + 1].isLetter() || st.xd[st.xpx.toInt() + 1] == '%') {
                    // call by ref argument
                    val rargs = CharArray(32)
                    var r = st.xpx.toInt() + 1
                    var q = 0
                    while (st.xd[r] != ',' && st.xd[r] != NUL && st.xd[r] != ')') {
                        rargs[q++] = st.xd[r]; r++
                    }
                    rargs[q] = NUL
                    st.out.write("\tstrcpy((char *) svPtr->Rargs[svPtr->Rargsx],(const char *) \"" + cstringAt(rargs) + "\"); \n")
                    st.out.write("\tsvPtr->Rargsx++; \n")
                    st.xpx++
                    phase = P_SKIP_NUMERIC; continue@outer
                }

                i = 1
                while (true) { // for (i=1; (bd[i]=xd[xpx]) && ncode[bd[i]]==10; i++,xpx++);
                    st.bd[i] = st.xd[st.xpx.toInt()]
                    if (st.bd[i] == NUL || ncode[st.bd[i].code] != 10.toChar()) break
                    i++; st.xpx++
                }

                if (i == 1 && st.bd[1] == '.') return 9

                if (st.bd[i].lowercaseChar() == 'e') { // e-type exponent arm
                    i++
                    st.xpx++
                    if (st.xd[st.xpx.toInt()] != '+' && st.xd[st.xpx.toInt()] != '-' &&
                        !st.xd[st.xpx.toInt()].isDigit()) return 9
                    st.bd[i] = st.xd[st.xpx.toInt()]
                    i++
                    st.xpx++
                    if (!st.xd[st.xpx.toInt() - 1].isDigit() && !st.xd[st.xpx.toInt()].isDigit()) return 9
                    while (true) { // scan rest of exponent
                        if (!st.xd[st.xpx.toInt()].isDigit()) break
                        st.bd[i] = st.xd[st.xpx.toInt()]
                        i++; st.xpx++
                    }
                }

                st.bd[i] = NUL
                val temp = cstringAt(st.bd, 1).trim().toDoubleOrNull() // sscanf(&bd[1],"%lf")
                if (temp == null) return 12
                strcpyC(st.bd, String.format(Locale.getDefault(), "%.15g", temp), 1) // sprintf(&bd[1],"%.15g")

                cannonAt() // vendor: cannon(bp) over the bp==&bd[1] alias

                val tS = CharArray(512)
                tS[0] = '"'
                var q = 1
                var k = 1
                while (st.bd[k] != NUL) { tS[q++] = st.bd[k]; k++ }
                tS[q++] = '"'
                tS[q] = NUL
                strcpyC(st.bd, cstringAt(tS), 1) // strcpy(&bd[1],t)

                st.xpx--
                phase = P_EXEC; continue@outer
            }

            phase = P_SKIP_NUMERIC
        }

        P_SKIP_NUMERIC -> { // skip_numeric:

            ////-----------------------------------------------------------------------------------------------------------------
            // literal operand
            ////-----------------------------------------------------------------------------------------------------------------
            if (st.xd[st.xpx.toInt()] == '"') {
                j = 0
                j++; st.bd[j] = '"' // bd[++j] = '"'

                while (true) {

                    while (true) { // while (xd[++xpx] != '"')
                        st.xpx++
                        if (st.xd[st.xpx.toInt()] == '"') break

                        if (st.xd[st.xpx.toInt()] == '\\') {
                            j++; st.bd[j] = '\\'
                            j++; st.bd[j] = '\\'
                            continue
                        }

                        if (st.xd[st.xpx.toInt()] == '\'') {
                            j++; st.bd[j] = '\\'
                            j++; st.bd[j] = '\''
                            continue
                        }

                        j++
                        st.bd[j] = st.xd[st.xpx.toInt()]
                        if (st.bd[j] == NUL) return 2
                    }

                    if (st.xd[st.xpx.toInt() + 1] != '"') {
                        j++; st.bd[j] = '"'
                        j++; st.bd[j] = NUL
                        phase = P_EXEC; continue@outer
                    }

                    st.xpx++
                    j++; st.bd[j] = '\\'
                    j++; st.bd[j] = '"'
                }
            }

            ////-----------------------------------------------------------------------------------------------------------------
            // variable name
            ////-----------------------------------------------------------------------------------------------------------------
            st.v1d[1] = st.xd[st.xpx.toInt()]
            st.xpx++
            j = 2

            while (true) {

                if (dcode[st.xd[st.xpx.toInt()].code] == 10.toChar()) {
                    st.v1d[j] = NUL
                    st.xpx--
                    phase = P_VAR1; continue@outer
                }

                st.v1d[j] = st.xd[st.xpx.toInt()]
                j++

                if (st.xd[st.xpx.toInt()] == '(') {
                    j--
                    st.v1d[j] = NUL
                    st.xd[st.xpx.toInt()] = CodedOpen.toChar()
                    st.xpx--
                    phase = P_START; continue@outer // recurse
                }
                st.xpx++
            }
        }

        P_VAR1 -> { // var1:

            if (st.t0px == 1.toShort() && spx == 1) {
                spx--
                st.t0px = 0
                st.sdlim = sdLim.toShort()
                return 0
            }

            ////-----------------------------------------------------------------------------------------------------------------
            // global var
            ////-----------------------------------------------------------------------------------------------------------------
            if (st.v1d[1] == '^') {

                if (!st.v1d[2].isLetter() && st.v1d[2] == '(') { // naked case
                    return 9001 // not valid array reference
                }

                // build global array reference
                val tS = CharArray(1024)
                var a = 0
                i = 1
                while (st.v1d[i] != NUL) {
                    if (st.v1d[i].code in 32..127) { tS[a++] = st.v1d[i]; i++ } else break
                }

                tS[a++] = '\\'
                tS[a++] = 'x'
                tS[a++] = '0'
                tS[a++] = if (st.v1d[i] == NUL) NUL else '1'
                tS[a] = NUL

                st.out.write("\tstrmove((UNS) tmp" + st.iiitmp + ", (UNS) \"" + cstringAt(tS) + "\"); // init p674\n")

                if (st.v1d[i] != NUL) {
                    i++
                    var walk = true
                    while (walk) {
                        j = 0
                        while (st.v1d[i] <= Char(127) && st.v1d[i] >= ' ') { tS[j++] = st.v1d[i]; i++ }
                        tS[j] = NUL

                        if (strlenC(tS) == 0) strcpyC(tS, "\"\"")

                        if (strncmpC(tS, 0, "#G#", 3) == 0 || strncmpC(tS, 0, "#S#", 3) == 0) {
                            st.out.write("\tstrcatx((UNS) tmp" + st.iiitmp + ", (UNS) " + Extract2(tS) + ");\n")
                        } else {
                            st.out.write("\tstrcatx((UNS) tmp" + st.iiitmp + ", (UNS) " + cstringAt(tS) + ");\n")
                        }

                        i++
                        if (st.v1d[i] != NUL) continue // walk on
                        walk = false // else break
                    }
                }

                st.iiitmp++
                memcheck(st.iiitmp)

                memcheck(st.iiitmp)
                strcpyC(st.setname, "tmp" + (st.iiitmp - 1))
                bpSet("#G#tmp" + (st.iiitmp - 1) + "#tmp" + st.iiitmp + "#" + st.iiitmp)

                if (st.pd1[sdLim + 1].uppercaseChar() == 'Z' && st.pd1[sdLim + 2].uppercaseChar() == 'Z') {
                    // NO EVAL GLOBAL ZZ INTERFACE
                    bpSet("tmp" + (st.iiitmp - 1))
                }

                st.iiitmp++
                memcheck(st.iiitmp)

                f = 1
                st.tpx = f.toShort()
                phase = P_EXEC; continue@outer
            } // normal global

            ////-----------------------------------------------------------------------------------------------------------------
            // built-in variables/fcns
            ////-----------------------------------------------------------------------------------------------------------------
            if (st.v1d[1] == DOLLAR.toChar()) {

                st.v1d[2] = st.v1d[2].lowercaseChar()

                if (st.v1d[2] == DOLLAR.toChar()) { phase = P_NOARG; continue@outer }

                // <dollar>1 -> <dollar>99
                if ((st.v1d[2].isDigit() && st.v1d[3] == NUL) ||
                    (st.v1d[3].isDigit() && st.v1d[4].isDigit() && st.v1d[5] == NUL)) {
                    st.v1d[1] = '_' // change dollar to _ and revisit
                    phase = P_VAR1; continue@outer
                }

                val cs = cstringAt(st.v1d, 1).lowercase() // strcasecmp view

                // <dollar>test / <dollar>t
                if (cs == "\u0024test" || cs == "\u0024t") {

                    memcheck(st.iiitmp)
                    st.out.write("\tif (svPtr->tpx) tmp" + st.iiitmp + "[0]='1'; /* \u0024test */\n")
                    st.out.write("\telse tmp" + st.iiitmp + "[0]='0';\n")
                    st.out.write("\ttmp" + st.iiitmp + "[1]='\\0';\n")
                    bpSetImpl()
                    phase = P_EXEC; continue@outer
                }

                // <dollar>io / <dollar>i
                if (cs == "\u0024io" || cs == "\u0024i") {
                    memcheck(st.iiitmp)
                    st.out.write("\tsprintf((char *) tmp" + st.iiitmp + ", \"%d\", svPtr->io); /* \u0024io */\n")
                    bpSetImpl()
                    phase = P_EXEC; continue@outer
                }

                // <dollar>principal / <dollar>p
                if (cs == "\u0024principal" || cs == "\u0024p") {
                    memcheck(st.iiitmp)
                    st.out.write("\tsprintf((char *) tmp" + st.iiitmp + ",\"%d\", 5); /* \u0024principal */\n")
                    bpSetImpl()
                    phase = P_EXEC; continue@outer
                }

                // <dollar>x (the duplicated vendor comparison is kept: strcasecmp "<dollar>x" twice)
                if (cs == "\u0024x" || cs == "\u0024x") {
                    memcheck(st.iiitmp)
                    st.out.write("\t" + "sprintf((char *) tmp" + st.iiitmp + ",\"%d\",svPtr->hor[svPtr->io]); /* \u0024x */\n")
                    bpSetImpl()
                    phase = P_EXEC; continue@outer
                }

                // <dollar>y
                if (cs == "\u0024y" || cs == "\u0024y") {
                    memcheck(st.iiitmp)
                    st.out.write("\t" + "sprintf((char *) tmp" + st.iiitmp + ",\"%d\",svPtr->ver[svPtr->io]); /* \u0024y */\n")
                    bpSetImpl()
                    phase = P_EXEC; continue@outer
                }

                // <dollar>horolog / <dollar>h
                if (cs == "\u0024horolog" || cs == "\u0024h") {
                    memcheck(st.iiitmp)
                    st.out.write("\t_horolog(tmp" + st.iiitmp + "); /* \u0024h */\n") // _horolog stub remains (batch-10/11)
                    bpSetImpl()
                    phase = P_EXEC; continue@outer
                }

                // <dollar>char / <dollar>c (strncasecmp "<dollar>char"=5 and "<dollar>c"=2)
                if (cs.startsWith("\u0024char") || cs.startsWith("\u0024c")) {
                    i = 1
                    while (st.v1d[i].code in 32..127) i++ // fcn nme
                                        shiftArgsDown(i)
                    st.out.write("\t_j=0;\n")
                    i = 0
                    while (true) {
                        j = i
                        while (st.v1d[i].code in 32..127) i++
                        if (st.v1d[i] == NUL) {
                            memcheck(st.iiitmp)
                            st.out.write("\ttmp" + (st.iiitmp + 1) + "[_j]=0;\n")
                            bpSet("tmp" + (st.iiitmp + 1))
                            st.iiitmp++
                            memcheck(st.iiitmp)
                            st.iiitmp++
                            memcheck(st.iiitmp)
                            phase = P_EXEC; continue@outer
                        }
                        st.v1d[i] = NUL
                        // vendor: v1d[i++] = 0 (the i++ is folded to the loop tail)
                        st.out.write("\tstrmove((UNS) tmp" + st.iiitmp + ", (UNS) " + Extract2(st.v1d, j) + ");\n")
                        st.out.write("\t_i=atol((char *) tmp" + st.iiitmp + ");\n")
                        st.out.write("\tif (_i>0) ")
                        st.out.write("\ttmp" + (st.iiitmp + 1) + "[_j++]=_i;\n")
                        i++
                    }
                }

                // <dollar>fnumber / <dollar>fn (strncasecmp "<dollar>fnumber"=5 effectively "\u0024fnum", "<dollar>fn"=2)
                if (cs.startsWith("\u0024fnum") || cs.startsWith("\u0024fn")) {
                    i = 1
                    while (st.v1d[i].code in 32..127) i++ // fcn nme
                                        shiftArgsDown(i)
                    i = 0
                    var a = 0
                    val tmpB = CharArray(1024)
                    strcpyC(tmpB, "")
                    while (true) {
                        j = i
                        while (st.v1d[i].code in 32..127) i++
                        if (st.v1d[i] == NUL) {
                            if (a == 0 || a > 3) { st.sdlim = sdLim.toShort(); return a }
                            memcheck(st.iiitmp)
                            st.out.write("\t_fnumber( (UNS) tmp" + st.iiitmp + ", (UNS) " + cstringAt(tmpB) + ", svPtr);\n")
                            bpSetImpl()
                            phase = P_EXEC; continue@outer
                        }
                        st.v1d[i] = NUL
                        if (a > 0) strcatC(tmpB, ", (unsigned char *)")
                        strcatC(tmpB, Extract2(st.v1d, j))
                        a++
                        i++
                    }
                }

                // <dollar>extract / <dollar>e (strncasecmp "<dollar>extract"=5 effectively "\u0024extr", "<dollar>e"=2)
                if (cs.startsWith("\u0024extr") || cs.startsWith("\u0024e")) {
                    if (callExpandBp { Extract(st.v1d, it) } != 0) { st.sdlim = sdLim.toShort(); return 9003 }
                    phase = P_EXEC; continue@outer
                }

                // <dollar>length / <dollar>l (strncasecmp "<dollar>length"=5 effectively "\u0024leng", "<dollar>l"=2)
                if (cs.startsWith("\u0024leng") || cs.startsWith("\u0024l")) {
                    i = 1
                    while (st.v1d[i].code in 32..127) i++ // fcn nme
                                        shiftArgsDown(i)
                    i = 0
                    var a = 0
                    val tmpB = CharArray(1024)
                    strcpyC(tmpB, "")
                    while (true) {
                        j = i
                        while (st.v1d[i].code in 32..127) i++
                        if (st.v1d[i] == NUL) {
                            if (a == 0 || a > 3) { st.sdlim = sdLim.toShort(); return a }
                            if (a == 1) strcatC(tmpB, ", (UNS) \"\"")
                            memcheck(st.iiitmp)
                            st.out.write("\t_length((UNS) tmp" + st.iiitmp + ", (UNS) " + cstringAt(tmpB) + ");\n")
                            bpSetImpl()
                            phase = P_EXEC; continue@outer
                        }
                        st.v1d[i] = NUL
                        if (a > 0) strcatC(tmpB, ", (UNS) ")
                        strcatC(tmpB, Extract2(st.v1d, j))
                        a++
                        i++
                    }
                }

                // <dollar>find / <dollar>f (strncasecmp "<dollar>find"=5, "<dollar>f"=2)
                if (cs.startsWith("\u0024find") || cs.startsWith("\u0024f")) {
                    i = 1
                    while (st.v1d[i].code in 32..127) i++ // fcn name
                                        shiftArgsDown(i)
                    i = 0
                    var a = 0
                    val tmpB = CharArray(1024)
                    strcpyC(tmpB, "")
                    while (true) {
                        j = i
                        while (st.v1d[i].code in 32..127) i++
                        if (st.v1d[i] == NUL) {
                            if (a == 0 || a > 3) { st.sdlim = sdLim.toShort(); return a }
                            if (a == 2) strcatC(tmpB, ", (UNS) \"-1\"")
                            memcheck(st.iiitmp)
                            st.out.write("\t_find( (UNS) tmp" + st.iiitmp + ", (UNS) " + cstringAt(tmpB) + ");\n")
                            bpSetImpl()
                            phase = P_EXEC; continue@outer
                        }
                        st.v1d[i] = NUL
                        if (a > 0) strcatC(tmpB, ", (UNS)")
                        strcatC(tmpB, Extract2(st.v1d, j))
                        a++
                        i++
                    }
                }

                // <dollar>ascii / <dollar>a (strncasecmp "<dollar>ascii"=5 effectively "\u0024asci", "<dollar>a"=2)
                if (cs.startsWith("\u0024asci") || cs.startsWith("\u0024a")) {
                    i = 1
                    while (st.v1d[i].code in 32..127) i++ // fcn nme
                                        shiftArgsDown(i)
                    i = 0
                    var a = 0
                    val tmpB = CharArray(1024)
                    strcpyC(tmpB, "")
                    while (true) {
                        j = i
                        while (st.v1d[i].code in 32..127) i++
                        if (st.v1d[i] == NUL) {
                            if (a == 0 || a > 2) { st.sdlim = sdLim.toShort(); return a }
                            if (a == 1) strcatC(tmpB, ", (UNS) \"-1\"")
                            memcheck(st.iiitmp)
                            st.out.write("\t_ascii( (UNS) tmp" + st.iiitmp + ", (UNS) " + cstringAt(tmpB) + ");\n")
                            bpSetImpl()
                            phase = P_EXEC; continue@outer
                        }
                        st.v1d[i] = NUL
                        if (a > 0) strcatC(tmpB, ", (UNS)")
                        strcatC(tmpB, Extract2(st.v1d, j))
                        a++
                        i++
                    }
                }

                // <dollar>justify / <dollar>job / <dollar>j (strncasecmp "<dollar>justify"=5 effectively "\u0024just", "<dollar>job"=4, "<dollar>j"=2)
                if (cs.startsWith("\u0024just") || cs.startsWith("\u0024job") || cs.startsWith("\u0024j")) {
                    i = 1
                    while (st.v1d[i].code in 32..127) i++ // fcn name
                    if (st.v1d[i] == NUL) {
                        strcpyC(st.v1d, "")
                    } else {
                                                shiftArgsDown(i)
                    }
                    i = 0
                    var a = 0
                    val tmpB = CharArray(1024)
                    strcpyC(tmpB, "")
                    while (true) {
                        j = i
                        while (st.v1d[i].code in 32..127) i++
                        if (st.v1d[i] == NUL) {
                            if (a == 0) { // <dollar>job (the _MSC_VER DOS arm is excluded; linux arm kept)
                                st.out.write("\t" + "sprintf((char *) tmp" + st.iiitmp + ",\"%d\",getpid()); //\u0024job linux\n")
                            } else {
                                if (a == 1 || a > 3) { st.sdlim = sdLim.toShort(); return a }
                                if (a == 2) strcatC(tmpB, ", (UNS) \"-1\"")
                                st.out.write("\t_justify( (UNS) tmp" + st.iiitmp + ", (UNS) " + cstringAt(tmpB) + ");\n")
                            }
                            bpSetImpl()
                            phase = P_EXEC; continue@outer
                        }
                        st.v1d[i] = NUL
                        if (a > 0) strcatC(tmpB, ", (UNS)")
                        strcatC(tmpB, Extract2(st.v1d, j))
                        a++
                        i++
                    }
                }

                // <dollar>piece / <dollar>p (strncasecmp "<dollar>piece"=6, "<dollar>p"=2)
                if (cs.startsWith("\u0024piece") || cs.startsWith("\u0024p")) {
                    i = 1
                    while (st.v1d[i].code in 32..127) i++ // fcn name
                    if (st.v1d[i] == NUL) {
                        strcpyC(st.v1d, "")
                    } else {
                                                shiftArgsDown(i)
                    }
                    i = 0
                    var a = 0
                    val tmpB = CharArray(1024)
                    strcpyC(tmpB, "")
                    while (true) {
                        j = i
                        while (st.v1d[i].code in 32..127) i++
                        if (st.v1d[i] == NUL) {
                            if (a == 1 || a > 4) { st.sdlim = sdLim.toShort(); return a }
                            if (a == 2) strcatC(tmpB, ", (UNS) \"-1\"")
                            if (a < 4) strcatC(tmpB, ", (UNS) \"-1\"")
                            memcheck(st.iiitmp)
                            st.out.write("\t_piece((UNS) tmp" + st.iiitmp + ", \n")
                            st.out.write("\t\t(UNS) " + Extract2(tmpB) + ", \n")
                            st.out.write("\t\t0, (UNS) \"\", svPtr); // p1136\n")
                            bpSetImpl()
                            phase = P_EXEC; continue@outer
                        }
                        st.v1d[i] = NUL
                        if (a > 0) strcatC(tmpB, ",(UNS) ")
                        strcatC(tmpB, Extract2(st.v1d, j))
                        a++
                        i++
                    }
                }

                // <dollar>select / <dollar>storage (<dollar>select strncasecmp=5 effectively "\u0024sele"; <dollar>storage=4 "\u0024stor")
                if (cs.startsWith("\u0024s")) {
                    if (cs.startsWith("\u0024sele") || cs.startsWith("\u0024stor")) {
                        i = 1
                        while (st.v1d[i].code in 32..127) i++ // fcn name
                        if (st.v1d[i] == NUL) {
                            strcpyC(st.v1d, "")
                        } else {
                                                        shiftArgsDown(i)
                        }
                        i = 0
                        var a = 0
                        var k = 0
                        val tmpB = CharArray(1024)
                        strcpyC(tmpB, "")
                        while (true) {
                            k++
                            j = i
                            memcheck(st.iiitmp)
                            while (st.v1d[i].code in 32..127) i++
                            if (st.v1d[i] == NUL) {
                                if (a == 0) { // <dollar>storage
                                    st.out.write("\tstrmove((UNS) tmp" + st.iiitmp + ", (UNS) \"999\");\n")
                                } else {
                                    if (k > 20) { st.sdlim = sdLim.toShort(); return 99 }
                                    while (k < 21) {
                                        strcatC(tmpB, ",")
                                        strcatC(tmpB, "NULL")
                                        k++
                                    }
                                    st.out.write("\t_select( (UNS) tmp" + st.iiitmp + ", (UNS) " + cstringAt(tmpB) + ", svPtr);\n")
                                }
                                bpSetImpl()
                                phase = P_EXEC; continue@outer
                            }
                            st.v1d[i] = NUL
                            if (a > 0) strcatC(tmpB, ",(UNS)")
                            strcatC(tmpB, Extract2(st.v1d, j))
                            a++
                            i++
                        }
                    } else {
                        // function not found
                        st.ierr = 30
                        println("\n*** " + cstringAt(st.v1d, 1))
                        st.sdlim = sdLim.toShort()
                        return st.ierr.toInt()
                    }
                }

                // <dollar>random / <dollar>r (strncasecmp "<dollar>random"=5 effectively "\u0024rand"; "<dollar>r"=2 and "<dollar>re" excluded)
                if (cs.startsWith("\u0024rand") ||
                    (cs.startsWith("\u0024r") && !cs.startsWith("\u0024re"))) {
                    i = 1
                    while (st.v1d[i].code in 32..127) i++ // fcn nme
                    if (st.v1d[i] == NUL) {
                        strcpyC(st.v1d, "")
                    } else {
                                                shiftArgsDown(i)
                    }
                    i = 0
                    var a = 0
                    val tmpB = CharArray(1024)
                    strcpyC(tmpB, "")
                    while (true) {
                        j = i
                        while (st.v1d[i].code in 32..127) i++
                        if (st.v1d[i] == NUL) {
                            memcheck(st.iiitmp)
                            st.out.write("\t_random((UNS) tmp" + st.iiitmp + ", (UNS) " + cstringAt(tmpB) + ");\n")
                            bpSetImpl()
                            phase = P_EXEC; continue@outer
                        }
                        st.v1d[i] = NUL
                        if (a > 0) strcatC(tmpB, ",")
                        strcatC(tmpB, Extract2(st.v1d, j))
                        a++
                        i++
                    }
                }

                //===============================================
                // other functions: build parameter list string
                //===============================================
                run {
                    val t = CharArray(1024)
                    var tj = 0
                    i = 1
                    while (st.v1d[i] != NUL) {
                        if (st.v1d[i].code in 32..127) { t[tj] = st.v1d[i]; tj++; i++ } else break
                    }

                    if (st.v1d[i] == NUL) {
                        t[tj] = NUL
                        st.out.write("\tstrcpy((char *) &gtmp[1],(const char *) \"" + cstringAt(t) + "\");\n")
                        phase = P_NOARG; return@run
                    }
                    t[tj] = NUL
                    val tName = cstringAt(t, 1).lowercase() // t[0] is the dollar; the name begins at t[1]

                    if (tName.length >= 4 && tName.substring(0, 4) == "text") {
                        st.sdlim = sdLim.toShort()
                        return 33 // \u0024text not permitted; the vendor ExpandText call is dead code (h013)
                    }
                    if (tName.isNotEmpty() && tName[0] == 'o') { // \u0024order() function
                        callExpandBp { ExpandOrder(st.v1d, i, it); 0 }
                        if (st.ierr.toInt() == 0) { phase = P_EXEC; return@run } else { st.sdlim = sdLim.toShort(); return st.ierr.toInt() }
                    }
                    if (tName.isNotEmpty() && tName[0] == 'd') { // \u0024data() function
                        callExpandBp { ExpandData(st.v1d, i, it); 0 }
                        if (st.ierr.toInt() == 0) { phase = P_EXEC; return@run } else { st.sdlim = sdLim.toShort(); return st.ierr.toInt() }
                    }
                    if (tName.length >= 2 && tName[0] == 'n' && tName[1] == 'a') { // \u0024name() function
                        callExpandBp { ExpandName(st.v1d, i, it); 0 }
                        if (st.ierr.toInt() == 0) { phase = P_EXEC; return@run } else { st.sdlim = sdLim.toShort(); return st.ierr.toInt() }
                    }
                    if (tName.length >= 2 && tName[0] == 'q' && tName[1] == 's') { // \u0024qsubscript() function
                        callExpandBp { ExpandQS(st.v1d, i, it); 0 }
                        if (st.ierr.toInt() == 0) { phase = P_EXEC; return@run } else { st.sdlim = sdLim.toShort(); return st.ierr.toInt() }
                    }
                    if (tName.isNotEmpty() && tName[0] == 'n') { // \u0024next() function
                        callExpandBp { ExpandNext(st.v1d, i, it); 0 }
                        if (st.ierr.toInt() == 0) { phase = P_EXEC; return@run } else { st.sdlim = sdLim.toShort(); return st.ierr.toInt() }
                    }
                    if (tName.isNotEmpty() && tName[0] == 'g') { // \u0024get() function
                        callExpandBp { ExpandGet(st.v1d, i, it); 0 }
                        if (st.ierr.toInt() == 0) { phase = P_EXEC; return@run } else { st.sdlim = sdLim.toShort(); return st.ierr.toInt() }
                    }
                    // generic name and subscript emission ladder

                    st.out.write("\tstrmove( (UNS) &gtmp[1], (UNS) \"\");\n")
                    var h = String.format(Locale.getDefault(), "%2x", st.v1d[i].code)
                    t[tj] = '\\'; tj++
                    t[tj] = 'x'; tj++
                    t[tj] = h[0]; tj++
                    t[tj] = h[1]; tj++
                    t[tj] = NUL
                    st.out.write("\tcatx = strxcat((char *) &gtmp[1],\"" + cstringAt(t) + "\");\n")
                    i++

                    while (true) {
                        tj = 0
                        while (st.v1d[i].code <= 127 && st.v1d[i].code >= 32) { t[tj] = st.v1d[i]; tj++; i++ }
                        t[tj] = NUL
                        st.out.write("\tstrxcat((char *) catx ,(char *) " + Extract2(t) + ");\n")
                        tj = 0
                        h = String.format(Locale.getDefault(), "%2x", st.v1d[i].code)
                        t[tj] = '\\'; tj++
                        t[tj] = 'x'; tj++
                        t[tj] = h[0]; tj++
                        t[tj] = h[1]; tj++
                        t[tj] = NUL
                        st.out.write("\tstrxcat((char *) catx,\"" + cstringAt(t) + "\");\n")
                        i++
                        if (st.v1d[i] != NUL) continue
                        break
                    }
                    phase = P_NOARG; return@run
                }
            } // built-in variables and fcns (vendor "<dollar>" block end)

            //===============================================================
            // retrieve look-up
            //===============================================================
            if (st.v1d[1] == PREDICATEVAR.toChar()) {
                bpSet(cstringAt(st.v1d, 1)) // strcpy (bp, v1dp)
                phase = P_EXEC; continue@outer
            }

            run { // array references - insert brackets (v1dp == and v1d[1])
                var vv = 0 // v1dp index; v1dp[k] == st.v1d[k + 1]
                var hasArray = false
                while (st.v1d[vv + 1] != NUL) {
                    if (st.v1d[vv + 1].code > 127) { hasArray = true; break }
                    vv++
                }

                if (hasArray) { // has_array:
                    st.out.write("{ // local array setup\n\tint i;\n")
                    vv = 0
                    walk@ while (st.v1d[vv + 1] != NUL) {
                        when (st.v1d[vv + 1].code) {
                            206 -> { // Open
                                st.v1d[vv + 1] = NUL
                                st.out.write("\tstrmove((UNS) tmp" + st.iiitmp + ", (UNS) \"" + cstringAt(st.v1d, 1) + "\");\n")
                                st.out.write("\ti=strlen((const char *) tmp" + st.iiitmp + ");\n")
                                st.out.write("\ttmp" + st.iiitmp + "[i]=206;\n")
                                st.out.write("\ttmp" + st.iiitmp + "[i+1]=0;\n")
                                st.v1d[vv + 1] = CodedOpen.toChar()
                                var jj = vv + 1
                                while (st.v1d[jj + 1] != NUL) {
                                    if (st.v1d[jj + 1].code == CodedClose.code || st.v1d[jj + 1].code == CodedComma.code) break
                                    jj++
                                }
                                val k = st.v1d[jj + 1]
                                st.v1d[jj + 1] = NUL
                                if (strncmpC(st.v1d, vv + 2, "#S#", 3) == 0 || strncmpC(st.v1d, vv + 2, "#G#", 3) == 0) {
                                    st.out.write("\tstrcat((char *) tmp" + st.iiitmp + ",(char *) " + Extract2(st.v1d, vv + 2) + ");\n")
                                } else {
                                    st.out.write("\tstrcat((char *) tmp" + st.iiitmp + ",(char *) " + cstringAt(st.v1d, vv + 2) + ");\n")
                                }
                                st.out.write("\ti=strlen((const char *) tmp" + st.iiitmp + ");\n")
                                st.out.write("\ttmp" + st.iiitmp + "[i]=" + k.code + ";\n")
                                st.out.write("\ttmp" + st.iiitmp + "[i+1]=0;\n")
                                st.v1d[jj + 1] = k
                                vv = jj - 1
                            }
                            207 -> { // Close
                                memcheck(st.iiitmp)
                                strcpyC(st.setname, "tmp" + st.iiitmp)
                                bpSet("#S#tmp" + st.iiitmp + "#tmp" + (st.iiitmp + 1) + "#")
                                st.iiitmp = st.iiitmp + 2
                                memcheck(st.iiitmp)
                                st.out.write("\t}\n")
                                break@walk
                            }
                            208 -> { // comma
                                var jj = vv + 1
                                while (st.v1d[jj + 1] != NUL) {
                                    if (st.v1d[jj + 1].code == CodedClose.code || st.v1d[jj + 1].code == CodedComma.code) break
                                    jj++
                                }
                                val k = st.v1d[jj + 1]
                                st.v1d[jj + 1] = NUL
                                if (strncmpC(st.v1d, vv + 2, "#S#", 3) == 0) {
                                    st.out.write("\tstrcat((char *) tmp" + st.iiitmp + ",(char *) " + Extract2(st.v1d, vv + 2) + ");\n")
                                } else {
                                    st.out.write("\tstrcat((char *) tmp" + st.iiitmp + ",(char *) " + cstringAt(st.v1d, vv + 2) + ");\n")
                                }
                                st.out.write("\ti=strlen((const char *) tmp" + st.iiitmp + ");\n")
                                st.out.write("\ttmp" + st.iiitmp + "[i]=" + k.code + ";\n")
                                st.out.write("\ttmp" + st.iiitmp + "[i+1]=0;\n")
                                st.v1d[jj + 1] = k
                                vv = jj - 1
                            }
                        }
                        vv++
                    }
                    // goto no_array1 (fold below)
                } else { // no_array:
                    symflg = 11
                    memcheck(st.iiitmp)
                    if (!cvar(cstringAt(st.v1d, 1))) {
                        // vendor: strcpy(setname,"\""); strcat(setname,v1dp); strcat(setname,"\"");
                        strcpyC(st.setname, "\"" + cstringAt(st.v1d, 1) + "\"")
                        memcheck(st.iiitmp)
                        bpSet("#S#" + cstringAt(st.setname) + "#tmp" + st.iiitmp + "#")
                        st.iiitmp++
                        memcheck(st.iiitmp)
                    }
                }

                // no_array1:
                if (cvar(cstringAt(st.v1d, 1))) {
                    bpSet(cstringAt(st.v1d, 1)) // strcpy(and bd[1], v1dp)
                }
                // vendor fossil: if (pd1[sdlim] != '<dollar>') goto exec; - falls into exec either way (h013)
            }
            phase = P_EXEC
        }

        P_NOARG -> { // no_arg:
            if (st.v1d[2] == 'Z' || st.v1d[2] == 'z') {
                memcheck(st.iiitmp)
                st.out.write("\tzfcn(gtmp,tmp" + st.iiitmp + ",svPtr);\n")
                bpSetImpl()
            } else if (st.v1d[2] == DOLLAR.toChar()) {
                memcheck(st.iiitmp)
                var e = 3
                while (st.v1d[e] != NUL) e++
                if (MyFunc(st.v1d.copyOfRange(3, e + 1), st.iiitmp) != 0) {
                    st.sdlim = sdLim.toShort(); return 9002
                }
                bpSet("tmp" + st.iiitmp)
                st.iiitmp++
                memcheck(st.iiitmp)
            } else {
                st.out.write("\tstrmove((UNS) &svPtr->v1d[1], (UNS) &gtmp[1]);\n")
                memcheck(st.iiitmp)
                st.out.write("\tfcn(svPtr);\n")
                st.out.write("\tstrmove((UNS) tmp" + st.iiitmp + ", (UNS) &svPtr->bd[1]);\n")
                bpSetImpl()
            }
            // fcn_proc_done:
            if (st.ierr.toInt() == 0) { phase = P_EXEC; continue@outer }
            st.sdlim = sdLim.toShort()
            return st.ierr.toInt()
        }

        P_EXEC -> { // exec:
            if (s1p[spx] != EMPTY.toChar() && s1p[spx] != OPEN.toChar() && s1p[spx] != OPENC.toChar()) {
                phase = P_NXT_EXPR; continue@outer
            }
            spx++
            while (st.pd1[sdLim] != NUL) sdLim++
            sdLim++
            if (sdLim >= st.symlen.toInt()) { st.sdlim = sdLim.toShort(); return 31 }
            strcpyC(st.pd1, bpGet(), sdLim)
            s1p[spx] = OPERAND.toChar()
            phase = P_NCHAR; continue@outer
        }

        P_NXT_EXPR -> { // nxt_expr:
            if (s1p[spx] == OPEN.toChar() || s1p[spx] == OPERAND.toChar()) {
                st.sdlim = sdLim.toShort(); return 11
            }

            //=============================================================
            // check for number under operator
            //=============================================================
            if (s1p[spx - 1] != OPERAND.toChar()) {
                when (s1p[spx].code) {
                    INDIRECT -> {
                        st.out.write("\tstrmove( (UNS) xd, (UNS) " + Extract2(st.bd, 1) + ");\n")
                        st.out.write("\tsvPtr->xpx=0;\n")
                        st.out.write("\tsvPtr->t0px=0;\n")
                        st.out.write("\t_i=parse_(); \n")
                        st.out.write("\tif (_i!=0) ErrorMessage(\"Indirect expression error\",svPtr->LineNumber);\n")
                        memcheck(st.iiitmp)
                        st.out.write("\tstrmove( (UNS) tmp" + st.iiitmp + ", (UNS) &pd1[sdlim]);\n")
                        bpSetImpl()
                        // DEAD vendor residue (h013): spx--; strcat(bp,&xd[xpx + 1]); break;
                        //      strcpy(&xd[1],bp); xpx = 0; goto nchar; break;
                    }
                    MINUS -> { // unary minus
                        memcheck(st.iiitmp)
                        st.out.write("\tmult((char *) " + Extract2(st.bd, 1) + ",\"-1\",(char *) tmp" + st.iiitmp + ");\n")
                        bpSetImpl()
                    }
                    18 -> { // not not: falls through to NOT in the vendor switch (no break)
                        memcheck(st.iiitmp)
                        st.out.write("\tif (numcomp1( (UNS) " + Extract2(st.bd, 1) + ",0) == 0) strmove( (UNS) tmp" + st.iiitmp + ", (UNS) \"1\");\n")
                        st.out.write("\telse strmove( (UNS) tmp" + st.iiitmp + ", (UNS) \"0\");\n")
                        bpSetImpl()
                        // deliberate fall-through to NOT (vendor case 18 has no break)
                        memcheck(st.iiitmp)
                        st.out.write("\tif (numcomp1( (UNS) " + Extract2(st.bd, 1) + ", 0) == 0) \n")
                        st.out.write("\t\tstrmove( (UNS) tmp" + st.iiitmp + ", (UNS) \"1\");\n")
                        st.out.write("\telse strmove( (UNS) tmp" + st.iiitmp + ", (UNS) \"0\");\n")
                        bpSetImpl()
                    }
                    NOT.code -> { // unary not
                        memcheck(st.iiitmp)
                        st.out.write("\tif (numcomp1( (UNS) " + Extract2(st.bd, 1) + ", 0) == 0) \n")
                        st.out.write("\t\tstrmove( (UNS) tmp" + st.iiitmp + ", (UNS) \"1\");\n")
                        st.out.write("\telse strmove( (UNS) tmp" + st.iiitmp + ", (UNS) \"0\");\n")
                        bpSetImpl()
                    }
                    PLUS -> { // unary plus
                        memcheck(st.iiitmp)
                        st.out.write("\tadd( (char *) " + Extract2(st.bd, 1) + ", (char *) \"0\", (char *) tmp" + st.iiitmp + ");\n")
                        bpSetImpl()
                    }
                    else -> {
                        st.sdlim = sdLim.toShort(); return 1
                    }
                }
                spx--
                phase = P_NXT_OPERATOR; continue@outer
            }

            //=============================================================
            // extract value under operator
            //=============================================================
            adx = sdLim
            sdLim--
            while (st.pd1[sdLim - 1] != NUL) sdLim--

            if (strncmpC(st.pd1, adx, "#G#", 3) == 0) strcpyC(st.pd1, Extract2(st.pd1, adx), adx)
            if (bpGet().startsWith("#G#")) bpSet(Extract2(st.bd, 1))
            if (strncmpC(st.pd1, adx, "#S#", 3) == 0) strcpyC(st.pd1, Extract2(st.pd1, adx), adx)
            if (bpGet().startsWith("#S#")) bpSet(Extract2(st.bd, 1))

            // branch depending upon operator
            when (s1p[spx].code) {
                DIVIDE -> {
                    memcheck(st.iiitmp)
                    st.out.write("\tdivx((char *) " + cstringAt(st.pd1, adx) + ",(char *) " + bpGet() + ",(char *) tmp" + st.iiitmp + ");\n")
                    bpSetImpl()
                }
                EXPONEN -> {
                    memcheck(st.iiitmp)
                    st.out.write("\texpx((const char *) " + cstringAt(st.pd1, adx) + ", (const char *) " + bpGet() + ", (char *) tmp" + st.iiitmp + ");\n")
                    bpSetImpl()
                }
                MULTIPLY -> {
                    memcheck(st.iiitmp)
                    if (conCheckBp()) {
                        st.out.write("\tmult( (const char *) " + cstringAt(st.pd1, adx) + ", \"" + bpGet() + "\", (char *) tmp" + st.iiitmp + ");\n")
                    } else {
                        st.out.write("\tmult((char *) " + cstringAt(st.pd1, adx) + ", (char *) " + bpGet() + ", (char *) tmp" + st.iiitmp + ");\n")
                    }
                    bpSetImpl()
                }
                MINUS -> {
                    memcheck(st.iiitmp)
                    st.out.write("\tsub((char *) " + cstringAt(st.pd1, adx) + ",(char *) " + bpGet() + ",(char *) tmp" + st.iiitmp + ");\n")
                    bpSetImpl()
                }
                PLUS -> {
                    memcheck(st.iiitmp)
                    if (conCheckBp()) {
                        st.out.write("\tadd( (char *) " + cstringAt(st.pd1, adx) + ", \"" + bpGet() + "\", (char *) tmp" + st.iiitmp + " ); \n")
                    } else {
                        st.out.write("\tadd((char *) " + cstringAt(st.pd1, adx) + ", (char *) " + bpGet() + ", (char *) tmp" + st.iiitmp + "); \n")
                    }
                    bpSetImpl()
                }
                CONCAT -> {
                    memcheck(st.iiitmp)
                    st.out.write("\tif (strlen((const char *) " + cstringAt(st.pd1, adx) + ") +\n")
                    st.out.write("\tstrlen((const char *) " + bpGet() + ") + 1 \n")
                    st.out.write("\t> STR_MAX)\n")
                    st.out.write("\tErrorMessage(\"String too long\", svPtr->LineNumber);\n")
                    st.out.write("\tstrmove( (UNS) tmp" + st.iiitmp + ", (UNS) " + cstringAt(st.pd1, adx) + ");\n")
                    st.out.write("\tstrcat( (char *) tmp" + st.iiitmp + ", (char *) " + bpGet() + ");\n")
                    bpSetImpl()
                }
                EQUALS, NOTEQ -> {
                    if (s1p[spx].code != NOTEQ) { // EQUALS
                        memcheck(st.iiitmp)
                        st.out.write("\tif (strcmp((const char *) " + cstringAt(st.pd1, adx) + ",(const char *) " + bpGet() + ")==0) \n")
                        st.out.write("\tstrmove( (UNS) tmp" + st.iiitmp + ", (UNS) \"1\");\n")
                        st.out.write("\telse strmove( (UNS) tmp" + st.iiitmp + ", (UNS) \"0\");\n")
                        bpSetImpl()
                    } else { // NOTEQ
                        memcheck(st.iiitmp)
                        st.out.write("\tif (strcmp((const char *) " + cstringAt(st.pd1, adx) + ", (const char *) " + bpGet() + ") != 0)\n")
                        st.out.write("\t\tstrmove((UNS) tmp" + st.iiitmp + ", (UNS) \"1\");\n")
                        st.out.write("\telse strmove((UNS) tmp" + st.iiitmp + ", (UNS) \"0\");\n")
                        bpSetImpl()
                    }
                }
                GREATER -> {
                    memcheck(st.iiitmp)
                    if (conCheckBp()) {
                        st.out.write("\tif (numcomp1((UNS) " + cstringAt(st.pd1, adx) + ", " + bpGet() + ") > 0) strmove((UNS) tmp" + st.iiitmp + ", (UNS) \"1\");\n")
                        st.out.write("\telse strmove((UNS) tmp" + st.iiitmp + ", (UNS) \"0\");\n")
                    } else {
                        st.out.write("\tif (numcomp((UNS) " + cstringAt(st.pd1, adx) + ", (UNS) " + bpGet() + ") > 0) strmove((UNS) tmp" + st.iiitmp + ", (UNS) \"1\");\n")
                        st.out.write("\telse strmove((UNS) tmp" + st.iiitmp + ", (UNS) \"0\");\n")
                    }
                    bpSetImpl()
                }
                LESSTHAN -> {
                    memcheck(st.iiitmp)
                    if (conCheckBp()) {
                        st.out.write("\tif (numcomp1((UNS) " + cstringAt(st.pd1, adx) + "," + bpGet() + ")<0) strmove((UNS) tmp" + st.iiitmp + ", (UNS) \"1\");\n")
                        st.out.write("\telse strmove((UNS) tmp" + st.iiitmp + ", (UNS) \"0\");\n")
                    } else {
                        st.out.write("\tif (numcomp((UNS) " + cstringAt(st.pd1, adx) + ", (UNS) " + bpGet() + ") < 0) strmove((UNS) tmp" + st.iiitmp + ", (UNS) \"1\");\n")
                        st.out.write("\telse strmove((UNS) tmp" + st.iiitmp + ", (UNS) \"0\");\n")
                    }
                    bpSetImpl()
                }
                INTDIVIDE -> {
                    memcheck(st.iiitmp)
                    st.out.write("\tdivi((const char *) " + cstringAt(st.pd1, adx) + ",(const char *) " + bpGet() + ",(char *) tmp" + st.iiitmp + ");\n")
                    bpSetImpl()
                }
                MODULO -> {
                    memcheck(st.iiitmp)
                    st.out.write("\t_i=atoi((char *) " + bpGet() + ");\n")
                    st.out.write("\t_j=atoi((char *) " + cstringAt(st.pd1, adx) + ");\n")
                    st.out.write("\t_i=_j%_i;\n")
                    st.out.write("\tMltoa(_i, tmp" + st.iiitmp + ");\n")
                    bpSetImpl()
                }
                CONTAINS -> {
                    memcheck(st.iiitmp)
                    st.out.write("\tif (strstr((const char *) " + cstringAt(st.pd1, adx) + ", (const char *)" + bpGet() + ")) strmove((UNS) tmp" + st.iiitmp + ", (UNS) \"1\"); else strmove((UNS) tmp" + st.iiitmp + ", (UNS) \"0\");\n")
                    bpSetImpl()
                }
                FOLLOWS -> {
                    memcheck(st.iiitmp)
                    st.out.write("\tif (strcmp((const char *) " + cstringAt(st.pd1, adx) + ",(const char *) " + bpGet() + ")>0) strmove((UNS) tmp" + st.iiitmp + ", (UNS) \"1\"); else strmove((UNS) tmp" + st.iiitmp + ", (UNS) \"0\");\n")
                    bpSetImpl()
                }
                PATTERN.code -> {
                    fixstrBp() // vendor fixstr(and bd[1])
                    memcheck(st.iiitmp)
                    st.out.write("\tif (pm( (unsigned char *) " + cstringAt(st.pd1, adx) + ", (const unsigned char *) \"" + cstringAt(st.bd, 1) + "\", svPtr)) \n")
                    st.out.write("\tstrmove((UNS) tmp" + st.iiitmp + ", (UNS) \"1\"); // pattern match")
                    st.out.write("\n\telse strmove((UNS) tmp" + st.iiitmp + ",(UNS) \"0\");\n")
                    bpSet("tmp" + st.iiitmp)
                    st.iiitmp++
                    memcheck(st.iiitmp)
                }
                NOTPATTERN.code -> {
                    memcheck(st.iiitmp)
                    fixstrBp()
                    st.out.write("\tif (pm( (unsigned char *) " + cstringAt(st.pd1, adx) + ", (const unsigned char *) \"" + cstringAt(st.bd, 1) + "\", svPtr)) \n")
                    st.out.write("\tstrmove( (UNS) tmp" + st.iiitmp + ", (UNS) \"0\"); // pattern match")
                    st.out.write("\n\telse strmove( (UNS) tmp" + st.iiitmp + ", (UNS) \"1\");\n")
                    bpSet("tmp" + st.iiitmp)
                    st.iiitmp++
                    memcheck(st.iiitmp)
                }
                NOTGREATER -> {
                    memcheck(st.iiitmp)
                    st.out.write("\tif (numcomp((UNS) " + cstringAt(st.pd1, adx) + ", (UNS) " + bpGet() + ") <= 0) strmove((UNS) tmp" + st.iiitmp + ", (UNS) \"1\");\n")
                    st.out.write("\telse strmove((UNS) tmp" + st.iiitmp + ", (UNS) \"0\");\n")
                    bpSetImpl()
                }
                NOTLESS -> {
                    memcheck(st.iiitmp)
                    st.out.write("\tif (numcomp( (UNS) " + cstringAt(st.pd1, adx) + ", (UNS) " + bpGet() + ")>=0) strmove( (UNS) tmp" + st.iiitmp + ", (UNS) \"1\");\n")
                    st.out.write("\telse strmove( (UNS) tmp" + st.iiitmp + ", (UNS) \"0\");\n")
                    bpSetImpl()
                }
                NOTCONTAINS -> {
                    memcheck(st.iiitmp)
                    st.out.write("\tif (!strstr( (const char *)" + cstringAt(st.pd1, adx) + ", (const char *)" + bpGet() + ")) strmove( (UNS) tmp" + st.iiitmp + ", (UNS) \"1\"); else strmove( (UNS) tmp" + st.iiitmp + ", (UNS) \"0\");\n")
                    bpSetImpl()
                }
                NOTFOLLOWS -> {
                    memcheck(st.iiitmp)
                    st.out.write("\tif (strcmp((const char *) " + cstringAt(st.pd1, adx) + ",(const char *) " + bpGet() + ")<=0) strmove( (UNS) tmp" + st.iiitmp + ", (UNS) \"1\"); else strmove( (UNS) tmp" + st.iiitmp + ", (UNS) \"0\");\n")
                    bpSetImpl()
                }
                AND -> {
                    memcheck(st.iiitmp)
                    st.out.write("\tif (numcomp( (UNS) " + cstringAt(st.pd1, adx) + ", (UNS) \"0\") && numcomp( (UNS) " + bpGet() + ", (UNS) \"0\")) strmove( (UNS) tmp" + st.iiitmp + ", (UNS) \"1\"); else strmove( (UNS) tmp" + st.iiitmp + ", (UNS) \"0\");\n")
                    bpSetImpl()
                }
                OR -> {
                    memcheck(st.iiitmp)
                    st.out.write("\tif (numcomp1( (UNS) " + cstringAt(st.pd1, adx) + ", 0) || numcomp1( (UNS) " + bpGet() + ", 0)) strmove( (UNS) tmp" + st.iiitmp + ", (UNS) \"1\"); else strmove( (UNS) tmp" + st.iiitmp + ", (UNS) \"0\");\n")
                    bpSetImpl()
                }
                NOTAND -> {
                    memcheck(st.iiitmp)
                    st.out.write("\tif (numcomp1( (UNS) " + cstringAt(st.pd1, adx) + ", 0) && numcomp1( (UNS) " + bpGet() + ", 0)) strmove( (UNS) tmp" + st.iiitmp + ", (UNS) \"0\"); else strmove( (UNS) tmp" + st.iiitmp + ", (UNS) \"1\");\n")
                    bpSetImpl()
                }
                NOTOR -> {
                    memcheck(st.iiitmp)
                    st.out.write("\tif (numcomp1( (UNS) " + cstringAt(st.pd1, adx) + ", 0) || numcomp1( (UNS) " + bpGet() + ", 0)) \n")
                    st.out.write("\t\tstrmove( (UNS) tmp" + st.iiitmp + ", (UNS) \"0\"); \n")
                    st.out.write("\telse strmove( (UNS) tmp" + st.iiitmp + ", (UNS) \"1\"); \n")
                    bpSetImpl()
                }
                else -> { // OPERAND, EMPTY, NOT, default
                    st.sdlim = sdLim.toShort(); return 12
                }
            } // switch

            // dec_stk:
            spx -= 2

            phase = P_NXT_OPERATOR
        }

        P_NXT_OPERATOR -> { // nxt_operator:
            if (s1p[spx] != OPEN.toChar() && s1p[spx] != OPENC.toChar() && s1p[spx] != EMPTY.toChar()) {
                phase = P_NXT_EXPR; continue@outer
            }

            //===============================================================
            // push answer
            //===============================================================
            spx++
            sdLim += strlenC(st.pd1, sdLim) + 1
            if (sdLim >= st.symlen.toInt()) { st.sdlim = sdLim.toShort(); return 31 }
            strcpyC(st.pd1, bpGet(), sdLim)
            s1p[spx] = OPERAND.toChar()
            phase = P_NCHAR; continue@outer
        }

        P_FINISH -> { // finish:
            st.sdlim = sdLim.toShort()
            if (s1p[spx - 1] != EMPTY.toChar() || s1p[spx] != OPERAND.toChar()) return 16
            return 0
        }

        else -> { } // unreachable phase guard
        } // when (phase)
    } // outer while

    st.sdlim = sdLim.toShort()
    return st.ierr.toInt()
}
