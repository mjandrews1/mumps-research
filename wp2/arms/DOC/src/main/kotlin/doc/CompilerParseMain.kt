package doc

// Unit 268 (B9 tranche 3, complex stratum) roster row 248:
// compiler-parse.c::parse_ - loc 1201, ptr 66, ctl 346, score
// 765.3, rank 118 of the batch-ninth DOC arm - the compiler-side
// expression parser and code generator: the operand/operator
// stack machine over pd1/sdlim (EMPTY/OPEN/OPENC/OPERAND
// foundations, CodedOpen/CodedComma/CodedClose joins, the
// \u0024select unification and the colon denouement), the
// nchar scan ladder (QUOTE remap through the code[] table, ]]
// FOLLOWS, the exponent test, the opcode insert with the
// ErrChar seat), scan_operand (patterns via the patrn1
// compiler twin, the %+lf numeric canonicalize with cannon,
// the escaped-quote literal walk, the variable-name walk with
// the goto start '(' recursion), un_nest/var1, the global
// reference builder with the \x0 extension and the #G#/#S#
// strcatx ledger, the full \u0024-intrinsic family, the
// has-array bracket emitters, the cvar/setname local seat, the
// nxt_expr operator emission ladder, and finish.
//
// Consult H-014 hit-adapt (2026-11-22T05:30:00Z): the zero-arg
// Stubs.parse_() seat (the u160 tag-correction family note at
// the seat: this is the COMPILER twin; the unit-93 interp twin
// int parse_(struct MSV *) of interp-sym.cpp:93 binds the
// one-arg overload) FLIPS to this render; IfCode.kt's
// CompilerState.ierr = Stubs.parse_() call site binds by
// arity unchanged. H-015: same vendor name, two DISTINCT
// bodies and signatures (int parse_() here vs the
// parse_(struct MSV *) there): the strcatx one-render rule
// does NOT apply; the overload family resolves by arity.
//
// CARRIERS (landed seats, cited): CompilerState.xpx/XpxIdx
// (compiler-globals-ext.h:38; every mutation syncs), t2 (:35),
// ierr (:40), sdlim (:42), pd1len (:36), pd1/xd/v1d/bd, tpx/
// t0px, iiitmp, setname (CompilerCarriers, the
// compiler-globals.h:32 seat), symlen, ErrChar (Msv.ErrChar,
// stateVector.h:95 owner; the globals-ext.h:54 twin), Out =
// CompilerState.Out/emit (the fprintf emitter),
// patrn1(jpx,ernbr) (Patrn1Sys1, the compiler twin by arity;
// it syncs xpx = jpx-1 on exit), Extract/Extract2
// (CompilerParseExtract; the char* Extract2 family through
// the Stubs delegate), ExpandText/Order/Data/Name/QS/Next/Get,
// memcheck/ConCheck/cvar (CompilerParseMisc), fixstr, cannon,
// MyFunc (the builtin \u0024\u0024 table render), CstringOps.
// CodedOpen/CodedComma/CodedClose/RETRIEVE ride Carriers.kt;
// the operator codes (DIVIDE 1 ... EXPONEN 29, OPERAND 5,
// OPENC 6, NOT 12, PATTERN 17, NOTEQ 18, INDIRECT 28) and
// QUOTE/PREDICATEVAR ride InterpH.kt (compiler-parse.c
// includes interp.h). OPEN is compiler-macros.h:98 ('o') and
// EMPTY is :62 (5) - the VALUE COINCIDENCE with OPERAND 5 is
// the vendor's own (both feed s1p), kept verbatim: the _CP
// suffix distinguishes; naming per H-017.
// These five static tables are mechanically derived from the dump
// (the generator asserted integer counts and rows: the vendor
// DECLARES opcode/ncode/dcode[256] and operand[256] but INITIALIZES
// only the first 128 (140 for operand) entries - C zero-fills the
// tail, the ZERO TAIL is kept byte-for-byte here; the /*N*/ dump
// row markers map to the ten-a-row layout. H-017: the name suffix
// _CP keeps the file-private scope, no collision with the landed
// Parse_.kt POPEN_93 file twins).


// compiler-macros.h:98 #define OPEN 'o' and :62 #define EMPTY 5
// (the -CP suffix distinguishes these from the InterpH OPERAND 5
// value coincidence, the vendor's own)
private const val OPEN_CP = 'o'.code
private const val EMPTY_CP = 5

private val CODE_CP = intArrayOf(
    99, 99, 99, 99, 99, 99, 99, 99, 18, 19,
    99, 20, 18, 99, 99, 21, 22, 23, 99, 99,
    99, 99, 99, 99, 26, 27
)

private val OPCODE_CP = intArrayOf(
    99, 99, 99, 99, 99, 99, 99, 99, 99, 99,
    99, 99, 99, 99, 99, 99, 99, 99, 99, 99,
    99, 99, 99, 99, 99, 99, 99, 99, 99, 99,
    99, 99, 99, 25, 99, 14, 99, 99, 24, 12,
    0, 99, 2, 4, 99, 3, 99, 1, 99, 99,
    99, 99, 99, 99, 99, 99, 99, 99, 99, 99,
    11, 8, 9, 17, 28, 99, 99, 99, 99, 99,
    99, 99, 99, 99, 99, 99, 99, 99, 99, 99,
    99, 99, 99, 99, 99, 99, 99, 99, 99, 99,
    99, 15, 13, 16, 99, 7, 99, 99, 99, 99,
    99, 99, 99, 99, 99, 99, 99, 99, 99, 99,
    99, 99, 99, 99, 99, 99, 99, 99, 99, 99,
    99, 99, 99, 99, 99, 99, 99, 99, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    0, 0, 0, 0, 0, 0
)

private val NCODE_CP = intArrayOf(
    99, 99, 99, 99, 99, 99, 99, 99, 99, 99,
    99, 99, 99, 99, 99, 99, 99, 99, 99, 99,
    99, 99, 99, 99, 99, 99, 99, 99, 99, 99,
    99, 99, 99, 99, 99, 99, 99, 99, 99, 99,
    99, 99, 99, 99, 99, 99, 10, 99, 10, 10,
    10, 10, 10, 10, 10, 10, 10, 10, 99, 99,
    99, 99, 99, 99, 99, 99, 99, 99, 99, 99,
    99, 99, 99, 99, 99, 99, 99, 99, 99, 99,
    99, 99, 99, 99, 99, 99, 99, 99, 99, 99,
    99, 99, 99, 99, 99, 99, 99, 99, 99, 99,
    99, 99, 99, 99, 99, 99, 99, 99, 99, 99,
    99, 99, 99, 99, 99, 99, 99, 99, 99, 99,
    99, 99, 99, 99, 99, 99, 99, 99, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    0, 0, 0, 0, 0, 0
)

private val DCODE_CP = intArrayOf(
    10, 99, 99, 99, 99, 99, 99, 99, 99, 99,
    99, 99, 99, 99, 99, 99, 99, 99, 99, 99,
    99, 99, 99, 99, 99, 99, 99, 99, 99, 99,
    99, 99, 10, 10, 99, 10, 99, 99, 10, 10,
    99, 10, 10, 10, 10, 10, 99, 10, 99, 99,
    99, 99, 99, 99, 99, 99, 99, 99, 10, 99,
    10, 10, 10, 10, 10, 99, 99, 99, 99, 99,
    99, 99, 99, 99, 99, 99, 99, 99, 99, 99,
    99, 99, 99, 99, 99, 99, 99, 99, 99, 99,
    99, 10, 10, 10, 99, 10, 99, 99, 99, 99,
    99, 99, 99, 99, 99, 99, 99, 99, 99, 99,
    99, 99, 99, 99, 99, 99, 99, 99, 99, 99,
    99, 99, 99, 99, 99, 99, 99, 99, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    0, 0, 0, 0, 0, 0
)

private val OPERAND_CP = intArrayOf(
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    0, 0, 0, 2, 1, 0, 1, 1, 2, 0,
    0, 0, 0, 0, 0, 0, 1, 0, 1, 1,
    1, 1, 1, 1, 1, 1, 1, 1, 0, 0,
    2, 2, 2, 2, 0, 1, 1, 1, 1, 1,
    1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
    1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
    1, 2, 0, 2, 1, 0, 0, 1, 1, 1,
    1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
    1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
    1, 1, 1, 0, 0, 0, 0, 0, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    0, 0, 0, 0, 0, 0
)

// The goto-web fold (H-016): the vendor's single-function label web
// (start / nchar / scan_operand / un_nest / var1 / exec / nxt_expr /
// dec_stk / nxt_operator / finish, plus the fcn-family no_arg /
// fcn_proc_done labels) renders as the mutually-recursive label
// functions of the ParseCPCtx holder below; the nchar scan stays a
// loop (its gotos to nchar are continues), the phase labels are tail
// calls (call depth bounded by the operand count of one expression,
// the same bound the C re-entrant goto start carries per nesting).
// The 40-entry stack s1p[] is the vendor's function-static carrier:
// a single ctx array shared by every phase, exactly as the C static
// is. H-013 ledger (dead locals kept as this comment, no equivalents
// rendered): short kkk; unsigned char bbtyp; unsigned char
// Order[128]; unsigned char deferedRef[2048]; int deferedRslt; int
// deferedType; int OrderArg; short symflg (the =11 store in
// no_array is dead); short adx/jpx are live per-arm locals; the dollar
// compile-check whose goto-exec and fall-through paths are
// identical; the INDIRECT switch arm's tail after its break
// (spx--; strcat(bp,&xd[xpx+1]); strcpy(&xd[1],bp); xpx=0; the
// goto nchar); the g = RETRIEVE store; the \u0024text arm's ExpandText
// call after the return 33; the int flg of the other-functions
// block. The case-18 (no break) fallthrough INTO the NOT arm is LIVE
// vendor semantics and renders sequentially (both emissions run).
private class ParseCPCtx {

    val cs = CompilerState
    val pd1 = cs.pd1
    val xd = cs.xd
    val v1d = cs.v1d
    val bd = cs.bd
    val s1p = IntArray(40) // static unsigned char s1p[40]

    var spx = 0
    var sdlim = 0
    var xpxOrg = 0
    var symlen = 0
    var t2 = 0

    // extern long xpx - every mutation syncs through the seat
    var xpx: Int
        get() = cs.xpxIdx()
        set(v) { cs.xpx = v.toLong() }

    // bp == &bd[1] throughout; the offset-1 adapter over bd
    fun bpSet(s: String) = CstringOps.copyAtS(bd, 1, s)
    fun bpGet(): String = CstringOps.cstringAt(bd, 1)
    // &bd[1] CharArray view for the fixstr/cannon/ConCheck seats
    fun bdT(): CharArray = bd.copyOfRange(1, bd.size)
    fun bdTail(t: CharArray) = System.arraycopy(t, 0, bd, 1, t.size)
    fun emit(t: String) { cs.emit(t) }

    fun run(): Int {
        cs.ierr = 0
        xpxOrg = xpx
        xpx--
        spx = 0
        pd1[cs.pd1len + 2] = NUL
        pd1[cs.pd1len + 3] = NUL
        sdlim = cs.pd1len + 3
        cs.sdlim = sdlim
        symlen = cs.symlen
        t2 = cs.t2
        return start()
    }

    // start: main recursive internal entry point
    fun start(): Int {
        spx++
        s1p[spx] = EMPTY_CP // stack foundation
        return nchar()
    }

    fun nchar(): Int {
        ncharLoop@ while (true) {

            xpx++
            val nxtchr = xd[xpx]

            if (nxtchr == NUL) return finish()

            if (OPERAND_CP[nxtchr.code] == 1) return scanOperand()

            if (nxtchr == CodedOpen.toChar()) {

                spx++

                // while (pd1[sdlim++]);
                while (pd1[sdlim] != NUL) sdlim++
                sdlim++
                if (sdlim >= symlen) return 31

                // p1 = &v1d[1]; p2 = &pd1[sdlim];
                // while (*p2++ = *p1++); *p2 = 0; p2--;
                // *p2 = CodedOpen;
                var p1 = 1
                var p2 = sdlim
                while (true) {
                    pd1[p2] = v1d[p1]
                    val c = v1d[p1]
                    p1++
                    p2++
                    if (c == NUL) break
                }
                pd1[p2] = NUL
                pd1[p2 - 1] = CodedOpen.toChar()
                s1p[spx] = OPENC
                continue@ncharLoop
            }

            if (nxtchr == ',') {

                if (xd[xpx + 1] == ',' && xd[xpxOrg] == '\u0024' &&
                    xd[xpxOrg + 1] == '\u0024') {
                    // char tmp[2048]; strcpy(tmp,&xd[xpx+1]);
                    // xd[xpx+1]='0'; strcpy(&xd[xpx+2],tmp);
                    val tail = CstringOps.cstringAt(xd, xpx + 1)
                    xd[xpx + 1] = '0'
                    CstringOps.copyAtS(xd, xpx + 2, tail)
                }

                if (s1p[spx - 1] != OPENC) {
                    if (t2 > 0) return finish()
                    return 16
                }

                // concatenate stack tops and add coded comma
                sdlim--
                var i = sdlim
                while (true) { // for (i=--sdlim;(pd1[i]=pd1[i+1]);i++);
                    pd1[i] = pd1[i + 1]
                    if (pd1[i] == NUL) break
                    i++
                }
                pd1[i] = CodedComma.toChar()
                pd1[i + 1] = NUL // coded comma
                sdlim--
                var c = pd1[sdlim]
                sdlim--
                while (c != NUL) { // while (pd1[sdlim--]);
                    c = pd1[sdlim]
                    sdlim--
                }
                sdlim += 2 // find new stack top start
                spx-- // stack pointer
                continue@ncharLoop
            }

            if (nxtchr == ')') {

                if (s1p[spx - 1] != OPEN_CP) {
                    if (s1p[spx - 1] != OPENC) {
                        if (s1p[spx] == OPENC) { // fcn() type ref
                            return unNest()
                        } else return 16
                    }

                    // fcn/array return section
                    sdlim--
                    var i = sdlim
                    while (true) {
                        pd1[i] = pd1[i + 1]
                        if (pd1[i] == NUL) break
                        i++
                    }
                    pd1[i] = CodedClose.toChar()
                    pd1[i + 1] = NUL // coded close
                    sdlim--
                    var c = pd1[sdlim]
                    sdlim--
                    while (c != NUL) {
                        c = pd1[sdlim]
                        sdlim--
                    }
                    sdlim += 2 // find new stack top start
                    spx-- // stack pointer
                    return unNest()
                }

                // precedence close paren
                if (s1p[spx] != OPERAND) return 12

                // extract value from stack top
                // strcpy(bp, &pd1[sdlim]);
                bpSet(CstringOps.cstringAt(pd1, sdlim))

                // do sdlim--; while (pd1[sdlim - 1]);
                do {
                    sdlim--
                } while (pd1[sdlim - 1] != NUL) // compress stack
                cs.sdlim = sdlim

                // goto dec_stk (spx -= 2) then nxt_operator
                spx -= 2
                return nxtOperator()
            }

            if (nxtchr == ':') {

                var i = sdlim

                do {
                    i--
                } while (pd1[i - 1] != NUL)

                if (pd1[i] != '\u0024' ||
                    pd1[i + 1].lowercaseChar() != 's') return finish()
                sdlim--

                // SSS = &pd1[sdlim];
                // do { *SSS = *(SSS+1); SSS++ } while (*SSS);
                // shift string down one and unify the select open
                var sss = sdlim
                do {
                    pd1[sss] = pd1[sss + 1]
                    sss++
                } while (pd1[sss] != NUL)

                var c = pd1[sdlim]
                sdlim--
                while (c != NUL) { // while (pd1[sdlim--]);
                    c = pd1[sdlim]
                    sdlim--
                }

                sdlim += 2
                // strcat(&pd1[sdlim], cod209); static {209,0}
                CstringOps.catAtS(pd1, sdlim, Char(209).toString())

                spx--
                continue@ncharLoop
            }

            if (nxtchr == ' ' || nxtchr == '\t') return finish()

            if (nxtchr == '^' && t2 == 2 && s1p[spx] == OPERAND) {
                return finish()
            }

            // check for delimiters in list
            spx++

            if (nxtchr == ']') {
                if (xd[xpx + 1] == ']') {
                    xpx++
                    s1p[spx] = FOLLOWS // ] and ]] do the same
                    continue@ncharLoop
                }
            }

            if (nxtchr == QUOTE) { // insert not based operator code
                xpx++
                s1p[spx] = OPCODE_CP[xd[xpx].code]

                if (s1p[spx] != 99) {
                    s1p[spx] = CODE_CP[OPCODE_CP[xd[xpx].code]]
                    if (xd[xpx + 1] == ']') xpx++ // bypass second ]
                    if (s1p[spx] != 99) continue@ncharLoop
                }

                xpx--
            }

            if (nxtchr == '*' && xd[xpx + 1] == '*') { // exponentiation
                xpx++
                s1p[spx] = EXPONEN
                continue@ncharLoop
            }

            // insert code
            s1p[spx] = OPCODE_CP[nxtchr.code]
            if (s1p[spx] != 99) continue@ncharLoop

            // ErrChar = nxtchr (the Msv.ErrChar seat,
            // stateVector.h:95 owner, globals-ext.h:54 twin)
            Globals.svPtr.ErrChar = nxtchr
            return 12
        }
    }

    // scan for operand
    fun scanOperand(): Int {

        // jpx = xpx; patrn1(&jpx, &ernbr);
        val jpxArr = shortArrayOf(xpx.toShort())
        if (s1p[spx] == NOTPATTERN || s1p[spx] == PATTERN) {
            bd[1] = NUL
            val ernbrArr = shortArrayOf(0)
            patrn1(jpxArr, ernbrArr)
            if (ernbrArr[0].toInt() != 0) return ernbrArr[0].toInt()
            return exec()
        }

        // numeric operand
        if (NCODE_CP[xd[xpx].code] == 10) {

            if (Character.isLetter(xd[xpx + 1]) || xd[xpx + 1] == '%') { // call by ref
                // char Rargs[32] built from &xd[xpx+1] to , ) or NUL
                var i = xpx + 1
                val rargs = StringBuilder()
                while (xd[i] != ',' && xd[i] != NUL && xd[i] != ')') {
                    rargs.append(xd[i])
                    i++
                }
                emit("\tstrcpy((char *) svPtr->Rargs[svPtr->Rargsx]," +
                    "(const char *) \"" + rargs + "\"); \n")
                emit("\tsvPtr->Rargsx++; \n")
                xpx++
                // goto skip_numeric: falls into the literal test below
            } else {

                var i = 1
                while (true) { // for (i=1;(bd[i]=xd[xpx])&&ncode[bd[i]]==10;(i++,xpx++));
                    bd[i] = xd[xpx]
                    if (bd[i] == NUL || NCODE_CP[bd[i].code] != 10) break
                    i++
                    xpx++
                }

                if (i == 1 && bd[1] == '.') return 9

                if (bd[i].lowercaseChar() == 'e') {
                    i++
                    xpx++
                    // e type requires +/- or digit
                    if (xd[xpx] != '+' && xd[xpx] != '-' &&
                        !Character.isDigit(xd[xpx])) return 9
                    bd[i] = xd[xpx]
                    i++
                    xpx++
                    // e type requires an exponent
                    if (!Character.isDigit(xd[xpx - 1]) &&
                        !Character.isDigit(xd[xpx])) return 9
                    while (true) { // scan rest of exponent, if any
                        if (!Character.isDigit(xd[xpx])) break
                        bd[i] = xd[xpx]
                        i++
                        xpx++
                    }
                }

                bd[i] = NUL
                // if (sscanf(&bd[1],"%lf",&temp)==0) return 12;
                // (the %lf leading-decimal-prefix parse, H-012 fold:
                // the leading numeric prefix of the token, per C
                // sscanf semantics, not toDoubleOrNull of the whole)
                val dstr = CstringOps.cstringAt(bd, 1)
                val dm = Regex("""^[+-]?([0-9]+\.?[0-9]*|\.[0-9]+)([eE][+-]?[0-9]+)?""")
                    .find(dstr)
                if (dm == null) return 12
                val temp = dm.value.toDouble()
                // sprintf(&bd[1],"%.15g",temp); (H-009)
                bpSet(String.format(java.util.Locale.US, "%.15g", temp))
                // cannon(bp);
                val canonT = bdT()
                cannon(canonT)
                bdTail(canonT)

                // the 512 quote wrap: t[0]='"'; strcpy(&t[1],&bd[1]);
                // strcat(t,"\""); strcpy(&bd[1],t);
                bpSet("\"" + CstringOps.cstringAt(bd, 1) + "\"")

                xpx--
                return exec()
            }
        }

        // literal operand
        if (xd[xpx] == '"') {

            var j = 0
            j++
            bd[j] = '\"'

            litLoop@ while (true) {

                while (true) { // while (xd[++xpx] != '"')
                    xpx++
                    if (xd[xpx] == '\"') break

                    if (xd[xpx] == '\\') {
                        j++
                        bd[j] = '\\'
                        j++
                        bd[j] = '\\'
                        continue
                    }

                    if (xd[xpx] == '\'') {
                        j++
                        bd[j] = '\\'
                        j++
                        bd[j] = '\''
                        continue
                    }

                    j++
                    bd[j] = xd[xpx]
                    if (bd[j] == NUL) return 2
                }

                if (xd[xpx + 1] != '\"') {
                    j++
                    bd[j] = '\"'
                    j++
                    bd[j] = NUL
                    return exec()
                }

                xpx++
                j++
                bd[j] = '\\'
                j++
                bd[j] = '\"'
            }
        }

        // variable name
        v1d[1] = xd[xpx]
        xpx++
        var j = 2

        while (true) {

            if (DCODE_CP[xd[xpx].code] == 10) {
                v1d[j] = NUL
                xpx--
                return var1()
            }

            v1d[j] = xd[xpx]
            j++

            if (xd[xpx] == '(') {
                j--
                v1d[j] = NUL
                xpx--
                xd[xpx] = CodedOpen.toChar()
                return start() // recurse
            }
            xpx++
        }
    }

    // un_nest: copy answer from stack
    fun unNest(): Int {
        // while (pd1[--sdlim]);
        while (true) {
            sdlim--
            if (pd1[sdlim] == NUL) break
        }
        // strcpy(v1dp, &pd1[sdlim + 1]);
        CstringOps.copyAtS(v1d, 1, CstringOps.cstringAt(pd1, sdlim + 1))
        while (true) {
            sdlim--
            if (pd1[sdlim] == NUL) break
        }
        sdlim++
        spx -= 2
        return var1()
    }

    fun var1(): Int {

        if (cs.t0px == 1 && spx == 1) {
            spx--
            cs.t0px = 0
            return 0
        }

        if (v1d[1] == '^') { // global var
            return var1Global()
        }

        if (v1d[1] == '\u0024') return dollarFamily()

        if (v1d[1] == PREDICATEVAR) {
            // strcpy(bp, v1dp);
            bpSet(CstringOps.cstringAt(v1d, 1))
            return exec()
        }

        return var1ArrayRef()
    }

    // the global var arm of var1 (v1d[1] == '^')
    fun var1Global(): Int {

        if ((!Character.isLetter(v1d[2])) && (v1d[2] == '(' /* naked case */)) {
            return 9001 // not valid array reference
        }

        // g = RETRIEVE; - dead store (H-013 ledger, no later read)

        // build global array reference
        val t = CharArray(1024)
        var j = 0
        var i = 1
        while (v1d[i] != NUL) {
            if (v1d[i] >= ' ' && v1d[i] <= Char(127)) {
                t[j] = v1d[i]
                j++
            } else break
            i++
        }
        t[j] = '\\'
        j++
        t[j] = 'x'
        j++
        t[j] = '0'
        j++
        t[j] = if (v1d[i] == NUL) NUL else '1'
        j++
        t[j] = NUL

        emit("\tstrmove((UNS) tmp" + cs.iiitmp + ", (UNS) \"" +
            CstringOps.cstring(t) + "\"); // init p674\n")

        if (v1d[i] != NUL) {

            i++

            while (true) {
                j = 0

                while (v1d[i] <= Char(127) && v1d[i] >= ' ') {
                    t[j] = v1d[i]
                    j++
                    i++
                }
                t[j] = NUL

                if (CstringOps.cstring(t).isEmpty()) {
                    // if (strlen(t)==0) strcpy(t,"\"\"");
                    CstringOps.copyAtS(t, 0, "\"\"")
                }

                val ts = CstringOps.cstring(t)
                // if (strncmp(t,"#G#",3)==0 || strncmp(t,"#S#",3)==0)
                if (ts.startsWith("#G#") || ts.startsWith("#S#")) {
                    emit("\tstrcatx((UNS) tmp" + cs.iiitmp + ", (UNS) " +
                        CstringOps.cstring(Stubs.Extract2(t, 0)) + ");\n")
                } else {
                    emit("\tstrcatx((UNS) tmp" + cs.iiitmp + ", (UNS) " + ts + ");\n")
                }

                i++
                if (v1d[i] != NUL) continue
                break
            }
        }

        cs.iiitmp++
        memcheck(cs.iiitmp)

        memcheck(cs.iiitmp)
        // sprintf(setname,"tmp%d",iiitmp-1);
        CstringOps.copyAtS(cs.setname, 0, "tmp" + (cs.iiitmp - 1))
        // sprintf(bp, "#G#tmp%d#tmp%d#", iiitmp-1, iiitmp, iiitmp);
        // - the THREE conversions list feeds TWO percents: the
        // trailing extra is ignored (C vararg quirk, H-009 ledger)
        bpSet("#G#tmp" + (cs.iiitmp - 1) + "#tmp" + cs.iiitmp + "#")

        if (pd1[sdlim + 1].uppercaseChar() == 'Z' &&
            pd1[sdlim + 2].uppercaseChar() == 'Z') { // NO EVAL GLOBAL ZZ INTERFACE
            // sprintf(bp, "tmp%d", iiitmp - 1);
            bpSet("tmp" + (cs.iiitmp - 1))
        }

        cs.iiitmp++
        memcheck(cs.iiitmp)

        // f = 1; tpx = f;
        val f = 1
        cs.tpx = f
        return exec()
    }

    // the dollar family ladder (v1d[1] == '\u0024')
    fun dollarFamily(): Int {

        v1d[2] = v1d[2].lowercaseChar()

        if (v1d[2] == '\u0024') return noArg() // goto no_arg

        // \u00241 -> \u002499
        if ((Character.isDigit(v1d[2]) && v1d[3] == NUL) ||
            (Character.isDigit(v1d[3]) && Character.isDigit(v1d[4]) &&
                v1d[5] == NUL)) {
            v1d[1] = '_' // change \u0024 to _ and revisit
            return var1()
        }

        val name = CstringOps.cstringAt(v1d, 1) // &v1d[1]
        fun eq(lit: String): Boolean = name.equals(lit, ignoreCase = true)
        fun pfx(lit: String, n: Int): Boolean =
            name.regionMatches(0, lit, 0, n, ignoreCase = true)

        // the SSS/TTT squeeze drop of the fcn name (params tail to 0)
        fun squeezeArgs(iAfterName: Int) {
            var sss = 0
            var ttt = iAfterName + 1
            while (v1d[ttt] != NUL) {
                v1d[sss] = v1d[ttt]
                sss++
                ttt++
            }
            v1d[sss] = NUL
        }

        fun fcnNameEnd(): Int {
            var i = 1
            while (v1d[i] >= ' ' && v1d[i] <= Char(127)) i++ // (blank) fcn nme
            return i
        }

        // \u0024test
        if (eq("\u0024test") || eq("\u0024t")) {
            memcheck(cs.iiitmp)
            emit("\tif (svPtr->tpx) tmp" + cs.iiitmp +
                "[0]='1'; /* \u0024test */\n")
            emit("\telse tmp" + cs.iiitmp + "[0]='0';\n")
            emit("\ttmp" + cs.iiitmp + "[1]='\\0';\n")
            bpSet("tmp" + cs.iiitmp)
            cs.iiitmp++
            memcheck(cs.iiitmp)
            return exec()
        }

        // \u0024io
        if (eq("\u0024io") || eq("\u0024i")) {
            memcheck(cs.iiitmp)
            emit("\tsprintf((char *) tmp" + cs.iiitmp +
                ", \"%d\", svPtr->io); /* \u0024io */\n")
            bpSet("tmp" + cs.iiitmp)
            cs.iiitmp++
            memcheck(cs.iiitmp)
            return exec()
        }

        // \u0024principal
        if (eq("\u0024principal") || eq("\u0024p")) {
            memcheck(cs.iiitmp)
            emit("\tsprintf((char *) tmp" + cs.iiitmp +
                ",\"%d\", 5); /* \u0024principal */\n")
            bpSet("tmp" + cs.iiitmp)
            cs.iiitmp++
            memcheck(cs.iiitmp)
            return exec()
        }

        // \u0024x
        if (eq("\u0024x")) {
            memcheck(cs.iiitmp)
            emit("\tsprintf((char *) tmp" + cs.iiitmp +
                ",\"%d\",svPtr->hor[svPtr->io]); /* \u0024x */\n")
            bpSet("tmp" + cs.iiitmp)
            cs.iiitmp++
            memcheck(cs.iiitmp)
            return exec()
        }

        // \u0024y
        if (eq("\u0024y")) {
            memcheck(cs.iiitmp)
            emit("\tsprintf((char *) tmp" + cs.iiitmp +
                ",\"%d\",svPtr->ver[svPtr->io]); /* \u0024y */\n")
            bpSet("tmp" + cs.iiitmp)
            cs.iiitmp++
            memcheck(cs.iiitmp)
            return exec()
        }

        // \u0024horolog
        if (eq("\u0024horolog") || eq("\u0024h")) {
            memcheck(cs.iiitmp)
            emit("\t_horolog(tmp" + cs.iiitmp + "); /* \u0024h */\n")
            bpSet("tmp" + cs.iiitmp)
            cs.iiitmp++
            memcheck(cs.iiitmp)
            return exec()
        }

        // \u0024char
        if (pfx("\u0024char", 5) || pfx("\u0024c", 2)) {

            var i = fcnNameEnd()
            squeezeArgs(i)
            emit("\t_j=0;\n")
            i = 0

            while (true) {
                val j = i
                while (v1d[i] >= ' ' && v1d[i] <= Char(127)) i++

                if (v1d[i] == NUL) {
                    memcheck(cs.iiitmp)
                    emit("\ttmp" + (cs.iiitmp + 1) + "[_j]=0;\n")
                    bpSet("tmp" + (cs.iiitmp + 1))
                    cs.iiitmp++
                    memcheck(cs.iiitmp)
                    cs.iiitmp++
                    memcheck(cs.iiitmp)
                    return exec()
                }

                v1d[i] = NUL
                i++

                emit("\tstrmove((UNS) tmp" + cs.iiitmp + ", (UNS) " +
                    CstringOps.cstring(Stubs.Extract2(v1d, j)) + ");\n")
                emit("\t_i=atol((char *) tmp" + cs.iiitmp + ");\n")
                emit("\tif (_i>0) ")
                emit("\ttmp" + (cs.iiitmp + 1) + "[_j++]=_i;\n")
            }
        }

        // \u0024fnumber
        if (pfx("\u0024fnumber", 5) || pfx("\u0024fn", 3)) {

            var i = fcnNameEnd()
            squeezeArgs(i)

            i = 0
            var a = 0
            var tmp = ""

            while (true) {
                val j = i
                while (v1d[i] >= ' ' && v1d[i] <= Char(127)) i++
                if (v1d[i] == NUL) {
                    if (a == 0 || a > 3) return a
                    memcheck(cs.iiitmp)
                    emit("\t_fnumber( (UNS) tmp" + cs.iiitmp +
                        ", (UNS) " + tmp + ", svPtr);\n")
                    bpSet("tmp" + cs.iiitmp)
                    cs.iiitmp++
                    memcheck(cs.iiitmp)
                    return exec()
                }
                v1d[i] = NUL
                i++
                if (a != 0) tmp += ", (unsigned char *)"
                a++
                tmp += CstringOps.cstring(Stubs.Extract2(v1d, j))
            }
        }

        // \u0024extract
        if (pfx("\u0024extract", 5) || pfx("\u0024e", 2)) {
            // if (Extract(v1d,bp)) return 9003; (the stray vendor
            // double-semicolon noted)
            val tail = bdT()
            val rc = Extract(v1d, tail)
            bdTail(tail)
            if (rc != 0) return 9003
            return exec()
        }

        // \u0024length
        if (pfx("\u0024length", 5) || pfx("\u0024l", 2)) {
            var i = fcnNameEnd()
            squeezeArgs(i)
            i = 0
            var a = 0
            var tmp = ""

            while (true) {
                val j = i
                while (v1d[i] >= ' ' && v1d[i] <= Char(127)) i++

                if (v1d[i] == NUL) {
                    if (a == 0 || a > 3) return a
                    if (a == 1) tmp += ", (UNS) \"\""
                    memcheck(cs.iiitmp)
                    emit("\t_length((UNS) tmp" + cs.iiitmp +
                        ", (UNS) " + tmp + ");\n")
                    bpSet("tmp" + cs.iiitmp)
                    cs.iiitmp++
                    memcheck(cs.iiitmp)
                    return exec()
                }
                v1d[i] = NUL
                i++
                if (a != 0) tmp += ", (UNS) "
                a++
                tmp += CstringOps.cstring(Stubs.Extract2(v1d, j))
            }
        }

        // \u0024find
        if (pfx("\u0024find", 5) || pfx("\u0024f", 2)) {
            var i = fcnNameEnd()
            squeezeArgs(i)
            i = 0
            var a = 0
            var tmp = ""

            while (true) {
                val j = i
                while (v1d[i] >= ' ' && v1d[i] <= Char(127)) i++
                if (v1d[i] == NUL) {
                    if (a == 0 || a > 3) return a
                    if (a == 2) tmp += ", (UNS) \"-1\""
                    memcheck(cs.iiitmp)
                    emit("\t_find( (UNS) tmp" + cs.iiitmp +
                        ", (UNS) " + tmp + ");\n")
                    bpSet("tmp" + cs.iiitmp)
                    cs.iiitmp++
                    memcheck(cs.iiitmp)
                    return exec()
                }
                v1d[i] = NUL
                i++
                if (a != 0) tmp += ", (UNS)"
                a++
                tmp += CstringOps.cstring(Stubs.Extract2(v1d, j))
            }
        }

        // \u0024ascii
        if (pfx("\u0024ascii", 5) || pfx("\u0024a", 2)) {
            var i = fcnNameEnd()
            squeezeArgs(i)
            i = 0
            var a = 0
            var tmp = ""

            while (true) {
                val j = i
                while (v1d[i] >= ' ' && v1d[i] <= Char(127)) i++

                if (v1d[i] == NUL) {
                    if (a == 0 || a > 2) return a
                    if (a == 1) tmp += ", (UNS) \"-1\""
                    memcheck(cs.iiitmp)
                    emit("\t_ascii( (UNS) tmp" + cs.iiitmp +
                        ", (UNS) " + tmp + ");\n")
                    bpSet("tmp" + cs.iiitmp)
                    cs.iiitmp++
                    memcheck(cs.iiitmp)
                    return exec()
                }
                v1d[i] = NUL
                i++
                if (a != 0) tmp += ", (UNS)"
                a++
                tmp += CstringOps.cstring(Stubs.Extract2(v1d, j))
            }
        }

        // \u0024justify
        if (pfx("\u0024justify", 5) || pfx("\u0024job", 4) || pfx("\u0024j", 2)) {
            var i = fcnNameEnd()

            if (v1d[i] == NUL) {
                CstringOps.copyAtS(v1d, 0, "")
            } else {
                squeezeArgs(i)
            }

            i = 0
            var a = 0
            var tmp = ""

            while (true) {
                val j = i
                while (v1d[i] >= ' ' && v1d[i] <= Char(127)) i++

                if (v1d[i] == NUL) {
                    if (a == 0) { // \u0024job
                        // the non-_MSC_VER arm (H-018: the
                        // _MSC_VER _getpid DOS arm is the excluded
                        // build window, ledger)
                        emit("\tsprintf((char *) tmp" + cs.iiitmp +
                            ",\"%d\",getpid()); //\u0024job linux\n")
                    } else {
                        if (a == 1 || a > 3) return a
                        if (a == 2) tmp += ", (UNS) \"-1\""
                        emit("\t_justify( (UNS) tmp" + cs.iiitmp +
                            ", (UNS) " + tmp + ");\n")
                    }
                    memcheck(cs.iiitmp)
                    bpSet("tmp" + cs.iiitmp)
                    cs.iiitmp++
                    memcheck(cs.iiitmp)
                    return exec()
                }
                v1d[i] = NUL
                i++
                if (a != 0) tmp += ", (UNS)"
                a++
                tmp += CstringOps.cstring(Stubs.Extract2(v1d, j))
            }
        }

        // \u0024piece
        if (pfx("\u0024piece", 6) || pfx("\u0024p", 2)) {
            var i = fcnNameEnd()

            if (v1d[i] == NUL) {
                CstringOps.copyAtS(v1d, 0, "")
            } else {
                squeezeArgs(i)
            }

            i = 0
            var a = 0
            var tmp = ""

            while (true) {
                val j = i
                while (v1d[i] >= ' ' && v1d[i] <= Char(127)) i++

                if (v1d[i] == NUL) {
                    if (a == 1 || a > 4) return a
                    if (a == 2) tmp += ", (UNS) \"-1\""
                    if (a < 4) tmp += ", (UNS) \"-1\""
                    memcheck(cs.iiitmp)
                    emit("\t_piece((UNS) tmp" + cs.iiitmp + ", \n" +
                        "\t\t(UNS) " + CstringOps.cstring(Stubs.Extract2(tmp.toCharArray(), 0)) +
                        ", \n" +
                        "\t\t0, (UNS) \"\", svPtr); // p1136\n")
                    bpSet("tmp" + cs.iiitmp)
                    cs.iiitmp++
                    memcheck(cs.iiitmp)
                    return exec()
                }
                v1d[i] = NUL
                i++
                if (a != 0) tmp += ",(UNS) "
                a++
                tmp += CstringOps.cstring(Stubs.Extract2(v1d, j))
            }
        }

        // \u0024select / \u0024storage
        if (pfx("\u0024s", 2)) {
            if (pfx("\u0024select", 5) || pfx("\u0024storage", 4)) {

                var i = fcnNameEnd()

                if (v1d[i] == NUL) {
                    CstringOps.copyAtS(v1d, 0, "")
                } else {
                    squeezeArgs(i)
                }

                i = 0
                var a = 0
                var k = 0
                var tmp = ""

                while (true) {
                    k++
                    val j = i
                    memcheck(cs.iiitmp)
                    while (v1d[i] >= ' ' && v1d[i] <= Char(127)) i++

                    if (v1d[i] == NUL) {
                        if (a == 0) { // \u0024storage
                            emit("\tstrmove((UNS) tmp" + cs.iiitmp +
                                ", (UNS) \"999\");\n")
                        } else {
                            if (k > 20) return 99
                            while (k < 21) {
                                tmp += ","
                                tmp += "NULL"
                                k++
                            }
                            emit("\t_select( (UNS) tmp" + cs.iiitmp +
                                ", (UNS) " + tmp + ", svPtr);\n")
                        }
                        bpSet("tmp" + cs.iiitmp)
                        cs.iiitmp++
                        memcheck(cs.iiitmp)
                        return exec()
                    }
                    v1d[i] = NUL
                    i++
                    if (a != 0) tmp += ",(UNS)"
                    a++
                    tmp += CstringOps.cstring(Stubs.Extract2(v1d, j))
                }
            } else {
                cs.ierr = 30 // function not found
                // printf("\n*** %s\n", &v1d[1]); - STDOUT sideprint
                print("\n*** " + CstringOps.cstringAt(v1d, 1) + "\n")
                return cs.ierr
            }
        }

        // \u0024random
        if (pfx("\u0024random", 5) ||
            (pfx("\u0024r", 2) && !pfx("\u0024re", 3))) {
            var i = fcnNameEnd()

            if (v1d[i] == NUL) {
                CstringOps.copyAtS(v1d, 0, "")
            } else {
                squeezeArgs(i)
            }

            i = 0
            var a = 0
            var tmp = ""

            while (true) {
                val j = i
                while (v1d[i] >= ' ' && v1d[i] <= Char(127)) i++

                if (v1d[i] == NUL) {
                    memcheck(cs.iiitmp)
                    emit("\t_random((UNS) tmp" + cs.iiitmp +
                        ", (UNS) " + tmp + ");\n")
                    bpSet("tmp" + cs.iiitmp)
                    cs.iiitmp++
                    memcheck(cs.iiitmp)
                    return exec()
                }
                v1d[i] = NUL
                i++
                if (a != 0) tmp += ","
                a++
                tmp += CstringOps.cstring(Stubs.Extract2(v1d, j))
            }
        }

        return otherFcns()
    }

    // other functions: build parameter list string
    fun otherFcns(): Int {
        val t = CharArray(1024)
        val h = CharArray(6)
        // int flg=0; - dead local (H-013 ledger)
        var j = 0
        var i = 1
        while (v1d[i] != NUL) {
            if (v1d[i] >= ' ' && v1d[i] <= Char(127)) {
                t[j] = v1d[i]
                j++
            } else break
            i++
        }

        if (v1d[i] == NUL) {
            t[j] = NUL
            emit("\tstrcpy((char *) &gtmp[1],(const char *) \"" +
                CstringOps.cstring(t) + "\");\n")
            return noArg()
        }

        t[j] = NUL

        // if (strncasecmp(&t[1],"text",4) == 0) { return 33; ...
        // ExpandText(v1d,i,bp); the ExpandText call after the
        // return is vendor dead code (H-013 ledger); \u0024text is not
        // permitted
        val ts1 = t[1]
        if (CstringOps.cstring(t).regionMatches(1, "text", 0, 4, ignoreCase = true)) {
            return 33 // \u0024text not permitted
        }

        // if (tolower(t[1]) == 'o')
        if (ts1.lowercaseChar() == 'o') { // \u0024order() function
            val tail = bdT()
            ExpandOrder(v1d, i, tail)
            bdTail(tail)
            return fcnProcDone()
        }

        if (ts1.lowercaseChar() == 'd') { // \u0024data() function
            val tail = bdT()
            ExpandData(v1d, i, tail)
            bdTail(tail)
            return fcnProcDone()
        }

        if (ts1.lowercaseChar() == 'n' && t[2].lowercaseChar() == 'a') { // \u0024name()
            val tail = bdT()
            ExpandName(v1d, i, tail)
            bdTail(tail)
            return fcnProcDone()
        }

        if (ts1.lowercaseChar() == 'q' && t[2].lowercaseChar() == 's') { // \u0024qsubscript()
            val tail = bdT()
            ExpandQS(v1d, i, tail)
            bdTail(tail)
            return fcnProcDone()
        }

        if (ts1.lowercaseChar() == 'n') { // \u0024next() function
            val tail = bdT()
            ExpandNext(v1d, i, tail)
            bdTail(tail)
            return fcnProcDone()
        }

        if (ts1.lowercaseChar() == 'g') { // \u0024get() function
            val tail = bdT()
            ExpandGet(v1d, i, tail)
            bdTail(tail)
            return fcnProcDone()
        }

        emit("\tstrmove( (UNS) &gtmp[1], (UNS) \"\");\n")

        // sprintf(h, "%2x", v1d[i]); (H-009)
        val hx = String.format(java.util.Locale.US, "%2x", v1d[i].code)
        h[0] = hx[0]
        h[1] = hx[1]
        t[j] = '\\'
        j++
        t[j] = 'x'
        j++
        t[j] = h[0]
        j++
        t[j] = h[1]
        j++
        t[j] = NUL
        emit("\tcatx = strxcat((char *) &gtmp[1],\"" +
            CstringOps.cstring(t) + "\");\n")
        i++

        while (true) {
            j = 0
            while (v1d[i] <= Char(127) && v1d[i] >= ' ') {
                t[j] = v1d[i]
                j++
                i++
            }
            t[j] = NUL
            emit("\tstrxcat((char *) catx ,(char *) " +
                CstringOps.cstring(Stubs.Extract2(t, 0)) + ");\n")
            j = 0
            val hx2 = String.format(java.util.Locale.US, "%2x", v1d[i].code)
            h[0] = hx2[0]
            h[1] = hx2[1]
            t[j] = '\\'
            j++
            t[j] = 'x'
            j++
            t[j] = h[0]
            j++
            t[j] = h[1]
            j++
            t[j] = NUL
            emit("\tstrxcat((char *) catx,\"" + CstringOps.cstring(t) + "\");\n")
            i++
            if (v1d[i] != NUL) continue
            break
        }

        // falls into the no_arg label
        return noArg()
    }

    // no_arg (the z/ZCN and MyFunc dispatches; the <dollar><dollar> goto target)
    fun noArg(): Int {

        if (v1d[2] == 'Z' || v1d[2] == 'z') {
            memcheck(cs.iiitmp)
            emit("\tzfcn(gtmp,tmp" + cs.iiitmp + ",svPtr);\n")
            bpSet("tmp" + cs.iiitmp)
            cs.iiitmp++
            memcheck(cs.iiitmp)
        } else if (v1d[2] == '\u0024') {
            memcheck(cs.iiitmp)
            // if (MyFunc(&v1d[3], iiitmp)!=0) return 9002;
            if (MyFunc(v1d.copyOfRange(3, v1d.size), cs.iiitmp) != 0) {
                return 9002
            }
            bpSet("tmp" + cs.iiitmp)
            cs.iiitmp++
            memcheck(cs.iiitmp)
        } else {
            emit("\tstrmove((UNS) &svPtr->v1d[1], (UNS) &gtmp[1]);\n")
            memcheck(cs.iiitmp)
            emit("\tfcn(svPtr);\n")
            emit("\tstrmove((UNS) tmp" + cs.iiitmp +
                ", (UNS) &svPtr->bd[1]);\n")
            bpSet("tmp" + cs.iiitmp)
            cs.iiitmp++
            memcheck(cs.iiitmp)
        }

        return fcnProcDone()
    }

    // fcn_proc_done
    fun fcnProcDone(): Int {
        if (cs.ierr == 0) return exec()
        return cs.ierr
    }

    // the retrieve look-up tail of var1 (array references,
    // local-variable seats)
    fun var1ArrayRef(): Int {

        val v1dpS = CstringOps.cstringAt(v1d, 1) // v1dp

        // for (i = 0; v1dp[i] != 0; i++) if (v1dp[i] > 127)
        // goto has_array; goto no_array;
        var hasArray = false
        for (c in v1dpS) {
            if (c > Char(127)) {
                hasArray = true
                break
            }
        }

        if (hasArray) {

            emit("{ // local array setup\n\tint i;\n")

            // for (i = 0; v1dp[i] != 0; i++) - the in-place walk
            // over the v1dp carrier (indexcoded 206/207/208 seats)
            var i = 0
            var closed = false
            while (!closed && v1d[1 + i] != NUL) {
                val c = v1d[1 + i]

                if (c == Char(206)) { // Open

                    v1d[1 + i] = NUL
                    emit("\tstrmove((UNS) tmp" + cs.iiitmp + ", (UNS) \"" +
                        CstringOps.cstringAt(v1d, 1) + "\");\n")
                    emit("\ti=strlen((const char *) tmp" + cs.iiitmp + ");\n")
                    emit("\ttmp" + cs.iiitmp + "[i]=206;\n")
                    emit("\ttmp" + cs.iiitmp + "[i+1]=0;\n")
                    v1d[1 + i] = Char(206)

                    var j = i + 1
                    while (v1d[1 + j] != NUL &&
                        v1d[1 + j] != Char(207) && v1d[1 + j] != Char(208)) j++
                    val k = v1d[1 + j].code
                    v1d[1 + j] = NUL

                    // if (strncmp(&v1dp[i+1],"#S#",3)==0 ||
                    //     strncmp(&v1dp[i+1],"#G#",3)==0)
                    if (v1dpS.regionMatches(i + 1, "#S#", 0, 3) ||
                        v1dpS.regionMatches(i + 1, "#G#", 0, 3)) {
                        emit("\tstrcat((char *) tmp" + cs.iiitmp + ",(char *) " +
                            CstringOps.cstring(Stubs.Extract2(v1d, i + 2)) + ");\n")
                    } else {
                        emit("\tstrcat((char *) tmp" + cs.iiitmp +
                            ",(char *) " + CstringOps.cstringAt(v1d, i + 2) + ");\n")
                    }

                    emit("\ti=strlen((const char *) tmp" + cs.iiitmp + ");\n")
                    emit("\ttmp" + cs.iiitmp + "[i]=" + k + ";\n")
                    emit("\ttmp" + cs.iiitmp + "[i+1]=0;\n")

                    v1d[1 + j] = k.toChar()
                    i = j - 1

                } else if (c == Char(207)) { // Close

                    memcheck(cs.iiitmp)

                    // sprintf(setname,"tmp%d",iiitmp);
                    CstringOps.copyAtS(cs.setname, 0, "tmp" + cs.iiitmp)
                    // sprintf(bp, "#S#tmp%d#tmp%d#", iiitmp, iiitmp+1);
                    bpSet("#S#tmp" + cs.iiitmp + "#tmp" + (cs.iiitmp + 1) + "#")
                    cs.iiitmp = cs.iiitmp + 2
                    memcheck(cs.iiitmp)

                    emit("\t}\n")
                    closed = true

                } else if (c == Char(208)) { // comma

                    var j = i + 1
                    while (v1d[1 + j] != NUL &&
                        v1d[1 + j] != Char(207) && v1d[1 + j] != Char(208)) j++
                    val k = v1d[1 + j].code
                    v1d[1 + j] = NUL

                    if (v1dpS.regionMatches(i + 1, "#S#", 0, 3)) {
                        emit("\tstrcat((char *) tmp" + cs.iiitmp + ",(char *) " +
                            CstringOps.cstring(Stubs.Extract2(v1d, i + 2)) + ");\n")
                    } else {
                        emit("\tstrcat((char *) tmp" + cs.iiitmp +
                            ",(char *) " + CstringOps.cstringAt(v1d, i + 2) + ");\n")
                    }

                    emit("\ti=strlen((const char *) tmp" + cs.iiitmp + ");\n")
                    emit("\ttmp" + cs.iiitmp + "[i]=" + k + ";\n")
                    emit("\ttmp" + cs.iiitmp + "[i+1]=0;\n")

                    v1d[1 + j] = k.toChar()
                    i = j - 1
                }
                i++
            }

        } else {

            // no_array: symflg=11; (dead store, H-013 ledger)
            memcheck(cs.iiitmp)
            if (cvar(v1dpS) == 0) {
                // strcpy(setname,"\""); strcat(setname,v1dp);
                // strcat(setname,"\""); (setname the 128 carrier)
                var sn = "\""
                sn += v1dpS
                sn += "\""
                CstringOps.copyAtS(cs.setname, 0, sn)
                memcheck(cs.iiitmp)
                // sprintf(bp, "#S#%s#tmp%d#", setname, iiitmp);
                bpSet("#S#" + sn + "#tmp" + cs.iiitmp + "#")
                cs.iiitmp++
                memcheck(cs.iiitmp)
            }
        }

        // no_array1:
        if (cvar(v1dpS) != 0) {
            // strcpy(&bd[1], v1dp);
            bpSet(v1dpS)
        }

        // if (pd1[sdlim] != '\u0024') goto exec; - the fall-through
        // and the goto paths are identical here (H-013 fossil, kept
        // as this ledger; the exec label directly follows)
        return exec()
    }

    // exec: process value in bb or bd
    fun exec(): Int {

        if (s1p[spx] != EMPTY_CP && s1p[spx] != OPEN_CP &&
            s1p[spx] != OPENC) return nxtExpr()

        spx++

        // while (pd1[sdlim++]);
        while (pd1[sdlim] != NUL) sdlim++
        sdlim++
        if (sdlim >= symlen) return 31

        // strcpy(&pd1[sdlim], bp);
        CstringOps.copyAtS(pd1, sdlim, bpGet())

        s1p[spx] = OPERAND
        return nchar()
    }

    // nxt_expr: process expression - check operator on stack top
    fun nxtExpr(): Int {

        if (s1p[spx] == OPEN_CP || s1p[spx] == OPERAND) return 11

        // check for number under operator
        if (s1p[spx - 1] != OPERAND) {
            when (s1p[spx]) {

                INDIRECT -> {
                    emit("\tstrmove( (UNS) xd, (UNS) " +
                        CstringOps.cstring(Stubs.Extract2(bd, 1)) + ");\n")
                    emit("\tsvPtr->xpx=0;\n")
                    emit("\tsvPtr->t0px=0;\n")
                    emit("\t_i=parse_(); \n")
                    emit("\tif (_i!=0) ErrorMessage(\"Indirect expression error\"," +
                        "svPtr->LineNumber);\n")
                    memcheck(cs.iiitmp)
                    emit("\tstrmove( (UNS) tmp" + cs.iiitmp +
                        ", (UNS) &pd1[sdlim]);\n")
                    bpSet("tmp" + cs.iiitmp)
                    cs.iiitmp++
                    memcheck(cs.iiitmp)
                    // H-013 ledger: the vendor tail after this
                    // arm's break is DEAD C (spx--;
                    // strcat(bp,&xd[xpx+1]); break;
                    // strcpy(&xd[1],bp); xpx=0; goto nchar;
                    // break) - after the unconditional break, kept
                    // as this citation only.
                }

                MINUS -> { // unary minus
                    memcheck(cs.iiitmp)
                    emit("\tmult((char *) " +
                        CstringOps.cstring(Stubs.Extract2(bd, 1)) +
                        ",\"-1\",(char *) tmp" + cs.iiitmp + ");\n")
                    bpSet("tmp" + cs.iiitmp)
                    cs.iiitmp++
                    memcheck(cs.iiitmp)
                }

                18 -> {
                    // case 18: not not - LIVE FALLTHROUGH into the
                    // NOT arm below (the vendor emits both blocks;
                    // the 0/1 assignments of this arm are dead
                    // text, the NOT arm's sprintf/updates come
                    // after; kept sequential, the not-not here and
                    // the not emission below run in that order)
                    memcheck(cs.iiitmp)
                    emit("\tif (numcomp1( (UNS) " +
                        CstringOps.cstring(Stubs.Extract2(bd, 1)) +
                        ",0) == 0) strmove( (UNS) tmp" + cs.iiitmp +
                        ", (UNS) \"1\");\n\telse strmove( (UNS) tmp" +
                        cs.iiitmp + ", (UNS) \"0\");\n")

                    // case NOT: unary not (the fallthrough target)
                    memcheck(cs.iiitmp)
                    emit("\tif (numcomp1( (UNS) " +
                        CstringOps.cstring(Stubs.Extract2(bd, 1)) +
                        ", 0) == 0) \n\t\tstrmove( (UNS) tmp" + cs.iiitmp +
                        ", (UNS) \"1\");\n\telse strmove( (UNS) tmp" +
                        cs.iiitmp + ", (UNS) \"0\");\n")
                    bpSet("tmp" + cs.iiitmp)
                    cs.iiitmp++
                    memcheck(cs.iiitmp)
                }

                NOT -> { // unary not
                    memcheck(cs.iiitmp)
                    emit("\tif (numcomp1( (UNS) " +
                        CstringOps.cstring(Stubs.Extract2(bd, 1)) +
                        ", 0) == 0) \n\t\tstrmove( (UNS) tmp" + cs.iiitmp +
                        ", (UNS) \"1\");\n\telse strmove( (UNS) tmp" +
                        cs.iiitmp + ", (UNS) \"0\");\n")
                    bpSet("tmp" + cs.iiitmp)
                    cs.iiitmp++
                    memcheck(cs.iiitmp)
                }

                PLUS -> { // unary plus
                    memcheck(cs.iiitmp)
                    emit("\tadd( (char *) " +
                        CstringOps.cstring(Stubs.Extract2(bd, 1)) +
                        ", (char *) \"0\", (char *) tmp" + cs.iiitmp + ");\n")
                    bpSet("tmp" + cs.iiitmp)
                    cs.iiitmp++
                    memcheck(cs.iiitmp)
                }

                else -> return 1
            }

            spx--
            return nxtOperator()
        }

        // extract value under operator
        // for (adx = sdlim--; pd1[sdlim - 1]; sdlim--);
        val adx = sdlim
        sdlim--
        while (pd1[sdlim - 1] != NUL) sdlim--
        cs.sdlim = sdlim

        val adxS = CstringOps.cstringAt(pd1, adx)
        val bpS = bpGet()

        // if (strncmp(&pd1[adx],"#G#",3)==0)
        //   strcpy(&pd1[adx],Extract2(&pd1[adx]));
        if (adxS.startsWith("#G#")) {
            CstringOps.copyAtS(pd1, adx,
                CstringOps.cstring(Stubs.Extract2(pd1, adx)))
        }
        if (bpS.startsWith("#G#")) {
            bpSet(CstringOps.cstring(Stubs.Extract2(bd, 1)))
        }
        if (CstringOps.cstringAt(pd1, adx).startsWith("#S#")) {
            CstringOps.copyAtS(pd1, adx,
                CstringOps.cstring(Stubs.Extract2(pd1, adx)))
        }
        if (bpGet().startsWith("#S#")) {
            bpSet(CstringOps.cstring(Stubs.Extract2(bd, 1)))
        }

        val below = CstringOps.cstringAt(pd1, adx) // &pd1[adx] after fixups

        // branch depending upon operator
        when (s1p[spx]) {

            DIVIDE -> {
                memcheck(cs.iiitmp)
                emit("\tdivx((char *) " + below + ",(char *) " + bpGet() +
                    ",(char *) tmp" + cs.iiitmp + ");\n")
                bpSet("tmp" + cs.iiitmp)
                cs.iiitmp++
                memcheck(cs.iiitmp)
            }

            EXPONEN -> {
                memcheck(cs.iiitmp)
                emit("\texpx((const char *) " + below + ", (const char *) " +
                    bpGet() + ", (char *) tmp" + cs.iiitmp + ");\n")
                bpSet("tmp" + cs.iiitmp)
                cs.iiitmp++
                memcheck(cs.iiitmp)
            }

            MULTIPLY -> {
                memcheck(cs.iiitmp)
                if (ConCheck(bdT()) != 0) {
                    emit("\tmult( (const char *) " + below + ", \"" +
                        bpGet() + "\", (char *) tmp" + cs.iiitmp + ");\n")
                } else {
                    emit("\tmult((char *) " + below + ", (char *) " +
                        bpGet() + ", (char *) tmp" + cs.iiitmp + ");\n")
                }
                bpSet("tmp" + cs.iiitmp)
                cs.iiitmp++
                memcheck(cs.iiitmp)
            }

            MINUS -> {
                memcheck(cs.iiitmp)
                emit("\tsub((char *) " + below + ",(char *) " + bpGet() +
                    ",(char *) tmp" + cs.iiitmp + ");\n")
                bpSet("tmp" + cs.iiitmp)
                cs.iiitmp++
                memcheck(cs.iiitmp)
            }

            PLUS -> {
                memcheck(cs.iiitmp)
                if (ConCheck(bdT()) != 0) {
                    emit("\tadd( (char *) " + below + ", \"" + bpGet() +
                        "\", (char *) tmp" + cs.iiitmp + " ); \n")
                } else {
                    emit("\tadd((char *) " + below + ", (char *) " +
                        bpGet() + ", (char *) tmp" + cs.iiitmp + "); \n")
                }
                bpSet("tmp" + cs.iiitmp)
                cs.iiitmp++
                memcheck(cs.iiitmp)
            }

            CONCAT -> {
                memcheck(cs.iiitmp)
                emit("\tif (strlen((const char *) " + below + ") +\n" +
                    "\tstrlen((const char *) " + bpGet() + ") + 1 \n" +
                    "\t> STR_MAX)\n")
                emit("\tErrorMessage(\"String too long\", svPtr->LineNumber);\n")
                emit("\tstrmove( (UNS) tmp" + cs.iiitmp + ", (UNS) " +
                    below + ");\n")
                emit("\tstrcat( (char *) tmp" + cs.iiitmp + ", (char *) " +
                    bpGet() + ");\n")
                bpSet("tmp" + cs.iiitmp)
                cs.iiitmp++
                memcheck(cs.iiitmp)
            }

            EQUALS, NOTEQ -> {
                if (s1p[spx] != NOTEQ) { // EQUALS
                    memcheck(cs.iiitmp)
                    emit("\tif (strcmp((const char *) " + below +
                        ",(const char *) " + bpGet() + ")==0) \n" +
                        "\tstrmove( (UNS) tmp" + cs.iiitmp +
                        ", (UNS) \"1\");\n" +
                        "\telse strmove( (UNS) tmp" + cs.iiitmp +
                        ", (UNS) \"0\");\n")
                    bpSet("tmp" + cs.iiitmp)
                    cs.iiitmp++
                    memcheck(cs.iiitmp)
                } else {
                    memcheck(cs.iiitmp)
                    emit("\tif (strcmp((const char *) " + below +
                        ", (const char *) " + bpGet() + ") != 0)\n" +
                        "\t\tstrmove((UNS) tmp" + cs.iiitmp +
                        ", (UNS) \"1\");\n" +
                        "\telse strmove((UNS) tmp" + cs.iiitmp +
                        ", (UNS) \"0\");\n")
                    bpSet("tmp" + cs.iiitmp)
                    cs.iiitmp++
                    memcheck(cs.iiitmp)
                }
            }

            GREATER -> {
                memcheck(cs.iiitmp)
                if (ConCheck(bdT()) != 0) {
                    emit("\tif (numcomp1((UNS) " + below + ", " + bpGet() +
                        ") > 0) strmove((UNS) " +
                        "tmp" + cs.iiitmp + ", (UNS) \"1\");\n" +
                        "\telse strmove((UNS) tmp" + cs.iiitmp +
                        ", (UNS) \"0\");\n")
                } else {
                    emit("\tif (numcomp((UNS) " + below + ", (UNS) " +
                        bpGet() + ") > 0) " +
                        "strmove((UNS) tmp" + cs.iiitmp +
                        ", (UNS) \"1\");\n" +
                        "\telse strmove((UNS) tmp" + cs.iiitmp +
                        ", (UNS) \"0\");\n")
                }
                bpSet("tmp" + cs.iiitmp)
                cs.iiitmp++
                memcheck(cs.iiitmp)
            }

            LESSTHAN -> {
                memcheck(cs.iiitmp)
                if (ConCheck(bdT()) != 0) {
                    emit("\tif (numcomp1((UNS) " + below + "," + bpGet() +
                        ")<0) strmove((UNS) tmp" + cs.iiitmp +
                        ", (UNS) \"1\");\n" +
                        "\telse strmove((UNS) tmp" + cs.iiitmp +
                        ", (UNS) \"0\");\n")
                } else {
                    emit("\tif (numcomp((UNS) " + below + ", (UNS) " +
                        bpGet() + ") < 0) strmove((UNS) tmp" + cs.iiitmp +
                        ", (UNS) \"1\");\n" +
                        "\telse strmove((UNS) tmp" + cs.iiitmp +
                        ", (UNS) \"0\");\n")
                }
                bpSet("tmp" + cs.iiitmp)
                cs.iiitmp++
                memcheck(cs.iiitmp)
            }

            INTDIVIDE -> {
                memcheck(cs.iiitmp)
                emit("\tdivi((const char *) " + below + ",(const char *) " +
                    bpGet() + ",(char *) tmp" + cs.iiitmp + ");\n")
                bpSet("tmp" + cs.iiitmp)
                cs.iiitmp++
                memcheck(cs.iiitmp)
            }

            MODULO -> {
                memcheck(cs.iiitmp)
                emit("\t_i=atoi((char *) " + bpGet() + ");\n")
                emit("\t_j=atoi((char *) " + below + ");\n")
                emit("\t_i=_j%_i;\n")
                emit("\tMltoa(_i, tmp" + cs.iiitmp + ");\n")
                bpSet("tmp" + cs.iiitmp)
                cs.iiitmp++
                memcheck(cs.iiitmp)
            }

            CONTAINS -> {
                memcheck(cs.iiitmp)
                emit("\tif (strstr((const char *) " + below +
                    ", (const char *)" + bpGet() + ")) strmove((UNS) tmp" +
                    cs.iiitmp + ", (UNS) \"1\");" +
                    " else strmove((UNS) tmp" + cs.iiitmp +
                    ", (UNS) \"0\");\n")
                bpSet("tmp" + cs.iiitmp)
                cs.iiitmp++
                memcheck(cs.iiitmp)
            }

            FOLLOWS -> {
                memcheck(cs.iiitmp)
                emit("\tif (strcmp((const char *) " + below +
                    ",(const char *) " + bpGet() + ")>0) strmove((UNS) tmp" +
                    cs.iiitmp + ", (UNS) \"1\");" +
                    " else strmove((UNS) tmp" + cs.iiitmp +
                    ", (UNS) \"0\");\n")
                bpSet("tmp" + cs.iiitmp)
                cs.iiitmp++
                memcheck(cs.iiitmp)
            }

            PATTERN -> {
                // fixstr(&bd[1]);
                val fx = bdT()
                fixstr(fx)
                bdTail(fx)
                memcheck(cs.iiitmp)
                emit("\tif (pm( (unsigned char *) " + below +
                    ", (const unsigned char *) \"" +
                    CstringOps.cstringAt(bd, 1) + "\", svPtr)) " +
                    "\n\tstrmove((UNS) tmp" + cs.iiitmp +
                    ", (UNS) \"1\"); // pattern match" +
                    "\n\telse strmove((UNS) tmp" + cs.iiitmp +
                    ",(UNS) \"0\");\n")
                // sprintf(&bd[1], "tmp%d", iiitmp);
                bpSet("tmp" + cs.iiitmp)
                cs.iiitmp++
                memcheck(cs.iiitmp)
            }

            NOTGREATER -> {
                memcheck(cs.iiitmp)
                emit("\tif (numcomp((UNS) " + below + ", (UNS) " + bpGet() +
                    ") <= 0) strmove((UNS) tmp" + cs.iiitmp +
                    ", (UNS) \"1\");\n" +
                    "\telse strmove((UNS) tmp" + cs.iiitmp +
                    ", (UNS) \"0\");\n")
                bpSet("tmp" + cs.iiitmp)
                cs.iiitmp++
                memcheck(cs.iiitmp)
            }

            NOTLESS -> {
                memcheck(cs.iiitmp)
                emit("\tif (numcomp( (UNS) " + below + ", (UNS) " + bpGet() +
                    ")>=0) " +
                    "strmove( (UNS) tmp" + cs.iiitmp +
                    ", (UNS) \"1\");\n" +
                    "\telse strmove( (UNS) tmp" + cs.iiitmp +
                    ", (UNS) \"0\");\n")
                bpSet("tmp" + cs.iiitmp)
                cs.iiitmp++
                memcheck(cs.iiitmp)
            }

            NOTCONTAINS -> {
                memcheck(cs.iiitmp)
                emit("\tif (!strstr( (const char *)" + below +
                    ", (const char *)" + bpGet() + ")) " +
                    "strmove( (UNS) tmp" + cs.iiitmp +
                    ", (UNS) \"1\");" +
                    " else strmove( (UNS) tmp" + cs.iiitmp +
                    ", (UNS) \"0\");\n")
                bpSet("tmp" + cs.iiitmp)
                cs.iiitmp++
                memcheck(cs.iiitmp)
            }

            NOTFOLLOWS -> {
                memcheck(cs.iiitmp)
                emit("\tif (strcmp((const char *) " + below +
                    ",(const char *) " + bpGet() + ")<=0) " +
                    "strmove( (UNS) tmp" + cs.iiitmp +
                    ", (UNS) \"1\");" +
                    " else strmove( (UNS) tmp" + cs.iiitmp +
                    ", (UNS) \"0\");\n")
                bpSet("tmp" + cs.iiitmp)
                cs.iiitmp++
                memcheck(cs.iiitmp)
            }

            NOTPATTERN -> {
                memcheck(cs.iiitmp)
                // fixstr(&bd[1]); - the vendor order: memcheck then
                // fixstr (kept verbatim, differs from PATTERN arm)
                val fx = bdT()
                fixstr(fx)
                bdTail(fx)
                emit("\tif (pm( (unsigned char *) " + below +
                    ", (const unsigned char *) \"" +
                    CstringOps.cstringAt(bd, 1) + "\", " +
                    "svPtr)) \n\tstrmove( (UNS) tmp" + cs.iiitmp +
                    ", (UNS) \"0\"); // pattern match" +
                    "\n\telse strmove( (UNS) tmp" + cs.iiitmp +
                    ", (UNS) \"1\");\n")
                bpSet("tmp" + cs.iiitmp)
                cs.iiitmp++
                memcheck(cs.iiitmp)
            }

            AND -> {
                memcheck(cs.iiitmp)
                emit("\tif (numcomp( (UNS) " + below +
                    ", (UNS) \"0\") && numcomp( (UNS) " + bpGet() + ", " +
                    "(UNS) \"0\")) strmove( (UNS) tmp" + cs.iiitmp +
                    ", (UNS) \"1\"); else strmove( (UNS) tmp" +
                    cs.iiitmp + ", (UNS) \"0\");\n")
                bpSet("tmp" + cs.iiitmp)
                cs.iiitmp++
                memcheck(cs.iiitmp)
            }

            OR -> {
                memcheck(cs.iiitmp)
                emit("\tif (numcomp1( (UNS) " + below + ", 0) || " +
                    "numcomp1( (UNS) " + bpGet() + ", 0))" +
                    " strmove( (UNS) tmp" + cs.iiitmp +
                    ", (UNS) \"1\"); else strmove( (UNS) tmp" +
                    cs.iiitmp + ", (UNS) \"0\");\n")
                bpSet("tmp" + cs.iiitmp)
                cs.iiitmp++
                memcheck(cs.iiitmp)
            }

            NOTAND -> {
                memcheck(cs.iiitmp)
                emit("\tif (numcomp1( (UNS) " + below + ", 0) && " +
                    "numcomp1( (UNS) " + bpGet() + ", 0))" +
                    " strmove( (UNS) tmp" + cs.iiitmp +
                    ", (UNS) \"0\"); else strmove( (UNS) tmp" +
                    cs.iiitmp + ", (UNS) \"1\");\n")
                bpSet("tmp" + cs.iiitmp)
                cs.iiitmp++
                memcheck(cs.iiitmp)
            }

            NOTOR -> {
                memcheck(cs.iiitmp)
                emit("\tif (numcomp1( (UNS) " + below + ", 0) || " +
                    "numcomp1( (UNS) " + bpGet() + ", 0)) \n" +
                    "\t\tstrmove( (UNS) tmp" + cs.iiitmp +
                    ", (UNS) \"0\"); \n" +
                    "\telse strmove( (UNS) tmp" + cs.iiitmp +
                    ", (UNS) \"1\"); \n")
                bpSet("tmp" + cs.iiitmp)
                cs.iiitmp++
                memcheck(cs.iiitmp)
            }

            else -> return 12 // OPERAND, EMPTY, NOT, default
        }

        // dec_stk:
        spx -= 2

        return nxtOperator()
    }

    // nxt_operator: push answer
    fun nxtOperator(): Int {

        if (s1p[spx] != OPEN_CP && s1p[spx] != OPENC &&
            s1p[spx] != EMPTY_CP) return nxtExpr()

        spx++
        sdlim += CstringOps.cstringAt(pd1, sdlim).length + 1
        if (sdlim >= symlen) return 31
        // strcpy(&pd1[sdlim], bp);
        CstringOps.copyAtS(pd1, sdlim, bpGet())
        s1p[spx] = OPERAND
        return nchar()
    }

    // finish: exit sequence
    fun finish(): Int {
        if (s1p[spx - 1] != EMPTY_CP || s1p[spx] != OPERAND) return 16
        return 0
    }
}

// The zero-arg entry: the extern int parse_() (compiler-parse.c:198;
// extern long xpx seated at CompilerState.xpx; init xpxOrg, spx, the
// pd1[sdlim] stack base, symlen/t2 reads, then the start/nchar web
// above; the timing-equivalent locals stay in the ctx).
fun parse_(): Int = ParseCPCtx().run()
