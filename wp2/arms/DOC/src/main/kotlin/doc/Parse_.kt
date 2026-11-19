package doc

// Unit 93 (B3 tranche 3 / sub-push C): interp-parse.cpp::parse_ (batch 3,
// complex stratum; units.csv row 303: loc 584, score 1110.0, rank 303; the
// vendor body spans interp-parse.cpp:260-1328). The recursive-descent
// expression parser of the interpreter core: walks xd from xpx, maintains
// the s1p parse-level marker stack (40 levels) and the pd1 stack of
// operands, handles paren/argument-list nesting (the CodedOpen 206 /
// CodedClose 207 / CodedComma 208 coded bytes), indirect recursion (goto
// start reusing the frame), the pattern arms (patrn1 staging into bd),
// numeric/literal/variable operand scanning, global and dollar-function
// dispatch (the dollar byte is written as the word dollar in these
// comments per the scan-gate), and the full binary/unary operator
// evaluation switch. The goto web (start, nchar, scan_operand, un_nest,
// var1, exec, nxt_expr, dec_stk, nxt_operator, sym1, finish, idxin)
// linearizes per H-011 as a section state machine: each vendor label is
// one state below in vendor order, each vendor goto is a pc state
// transfer, and fall-through maps to the next state in declaration
// order. The same-name compiler twin compiler-parse.c::parse_ (roster
// row 302, batch-9 unit; the zero-argument shape) is a DISTINCT unit:
// the existing zero-arg Stubs.parse_() seat raised for the IfCode call
// site is RETAGGED to that owner in this push (TAG CORRECTION, the
// u160-Interpret mis-tag family; same-name units resolve by overload
// arity, H-015). The one-argument Stubs delegate binds this render
// (Extract2 seat-conversion precedent).
//
// Quirks kept and noted:
// - The five const tables: opcode/ncode/dcode/operand DECLARE 256
//   entries but INITIALIZE only 128 in the vendor - the C zero-pad
//   tail renders as explicit zeros (byte-faithful).
// - The code[26] table is indexed in the quote-operator arm by opcode
//   outputs that can reach 28 (the at-sign maps to 28) - an
//   out-of-bounds C read; the H-007 bounds-walk guard reads it as 0
//   (quantized deterministic UB, the u128 precedent, ledger here).
// - s1p[40]: parse nesting deeper than the array overruns in C
//   silently; the Kotlin carrier throws (automatic bounds, H-007).
// - The CONTAINS arm test pd1[adx] == 0 and bp == 0 compares the
//   POINTER bp (never zero in this frame) - the second clause is dead
//   in the vendor and renders as the literal pd1 top test plus false
//   (H-013 ledger; the left clause carries the byte logic).
// - The rpm forward declaration sits inside the vendor body
//   (interp-parse.cpp:1190, mid-switch); the arm reads it as the
//   H-014 Stubs.rpm seat call naming interp-rpm.cpp::rpm (not a
//   corpus unit; the interp-rpm.cpp roster rows are the parse_ trio).
// - The dead locals tmp1[25] and tmp2[4] are declared and never used
//   in the vendor body (H-013, ledger here, not declared; nxtchr and
//   bbtyp are live).
// - The leading-zero strip and the lone-dot rejection (return 9) are
//   kept verbatim (scan arms below).
// - The SQLITE pair in the dollar-data arm resolves per H-018
//   (reference build: SQLITE UNDEFINED; the non-SQLITE 1-byte child
//   walk is the live arm).
// - The ErrChar store precedes the return 12 (error path marker).
// - jpx and ernbr are the patrn1 out-params (ShortArray holders,
//   H-006).
//
// Seats: patrn1 rides the batch-1 delegate (the two holders feed the
// interp three-arg seat; the ernbr read follows); zfcn, fcn, sym_,
// xindex ride the Stubs seats; strmove reads parse as the offset-aware
// per-file copy helpers below (the Stubs.strmove seat names
// inline.h:38; the &pd1[n] / &bd[1] / &v1d[1] pointer shifts ride the
// per-file At forms, the H-014 pointer-shift ledger, the substr
// precedent), the arithmetic family (add, sub, divx, divi, modulo,
// expx plus the CharArray mult overload) are NEW Stubs seats naming
// the interp-arith.cpp owners (expx is a batch-5 corpus unit, modulo
// a batch-6 corpus unit; add/sub/mult/divx/divi are not roster rows),
// Mglobal the existing seat, BuildGlobal/BuildLocal NEW seats naming
// the batch-8 corpus units, numcomp the direct call to the landed
// batch-1 render. Carriers: bd/v1d/xd/xpx/pd1/pd1len/sdlim/t2/
// symflg/tpx/ErrChar already live on Msv; setname, kflg, t0px and
// ierr are added with this unit (stateVector.h:46/48/55/50). Naming
// per H-017 (the trailing underscore name parse_ is kept verbatim;
// the bp/v1dp pointers render as the offset forms of the At helpers,
// BD offset 1, V1D offset 1).

private const val POPEN_93 = 0 // interp.h:64
private const val DIVIDE_93 = 1 // interp.h:65
private const val MULTIPLY_93 = 2 // interp.h:66
private const val MINUS_93 = 3 // interp.h:67
private const val PLUS_93 = 4 // interp.h:68
private const val OPERAND_93 = 5 // interp.h:69
private const val OPENC_93 = 6 // interp.h:70
private const val CONCAT_93 = 7 // interp.h:71
private const val EQUALS_93 = 8 // interp.h:72
private const val GREATER_93 = 9 // interp.h:73
private const val PEMPTY_93 = 10 // interp.h:74
private const val LESSTHAN_93 = 11 // interp.h:75
private const val NOT_93 = 12 // interp.h:76
private const val INTDIVIDE_93 = 13 // interp.h:77
private const val MODULO_93 = 14 // interp.h:78
private const val CONTAINS_93 = 15 // interp.h:79
private const val FOLLOWS_93 = 16 // interp.h:80
private const val PATTERN_93 = 17 // interp.h:81
private const val NOTEQ_93 = 18 // interp.h:82
private const val NOT_NOT_93 = 18 // interp.h:83 (duplicates NOTEQ, kept)
private const val NOTGREATER_93 = 19 // interp.h:84
private const val NOTLESS_93 = 20 // interp.h:85
private const val NOTCONTAINS_93 = 21 // interp.h:86
private const val NOTFOLLOWS_93 = 22 // interp.h:87
private const val NOTPATTERN_93 = 23 // interp.h:88
private const val AND_93 = 24 // interp.h:89
private const val OR_93 = 25 // interp.h:90
private const val NOTAND_93 = 26 // interp.h:91
private const val NOTOR_93 = 27 // interp.h:92
private const val INDIRECT_93 = 28 // interp.h:93
private const val EXPONEN_93 = 29 // interp.h:94
private const val PREDICATEVAR_93: Char = '~' // interp.h:95
private const val QUOTE_93: Char = '\'' // interp.h:63
private const val TAB_93: Char = '\t' // interp.h:56
private const val OPEN_PAREN_93 = 206 // defines.h OPEN_PAREN (the order arm check)
// defines.h:44-50 SymRetrieve 1, SymNext 3, SymData 6, SymName 10;
// defines.h:170-171 ORDERNEXT 12, ORDERPREV 13
private const val SYMRETRIEVE_93 = 1
private const val SYMNEXT_93 = 3
private const val SYMDATA_93 = 6
private const val SYMNAME_93 = 10
private const val ORDERNEXT_93 = 12
private const val ORDERPREV_93 = 13

// vendor local: const static unsigned char cod209[2] = { 209, 0 }
private val COD209_93 = charArrayOf(209.toChar(), NUL)

// unsigned char table read: the C unsigned char wrap of the byte read
// (H-010; masks the Kotlin Char domain to the vendor byte domain)
private fun b93(ch: Char): Int = ch.code and 0xFF

private fun isAlpha93(ch: Char): Boolean =
    ch in 'a'..'z' || ch in 'A'..'Z' // C isalpha, C locale

private fun isAlnum93(ch: Char): Boolean =
    isAlpha93(ch) || ch in '0'..'9' // C isalnum, C locale

private fun up93(ch: Char): Char = Character.toUpperCase(ch)

// per-file At helpers (the &pd1[n] / &bd[1] / &v1d[1] pointer shifts;
// H-014 pointer-shift ledger, substr precedent)

private fun strlenAt93(a: CharArray, off: Int): Int {
    var n = 0
    while (a[off + n] != NUL) n++
    return n
}

private fun copyAt93(dst: CharArray, dstOff: Int, src: CharArray, srcOff: Int) {
    var d = dstOff
    var s = srcOff
    while (src[s] != NUL) {
        dst[d] = src[s]
        d++
        s++
    }
    dst[d] = NUL
}

private fun copyStrAt93(dst: CharArray, off: Int, s: String) {
    var d = off
    var k = 0
    while (k < s.length) {
        dst[d] = s[k]
        d++
        k++
    }
    dst[d] = NUL
}

private fun catAtFrom93(dst: CharArray, dstOff: Int, src: CharArray, srcOff: Int) {
    var d = dstOff
    while (dst[d] != NUL) d++
    var s = srcOff
    while (src[s] != NUL) {
        dst[d] = src[s]
        d++
        s++
    }
    dst[d] = NUL
}

private fun catStrAt93(dst: CharArray, dstOff: Int, s: String) {
    var d = dstOff
    while (dst[d] != NUL) d++
    var k = 0
    while (k < s.length) {
        dst[d] = s[k]
        d++
        k++
    }
    dst[d] = NUL
}

private fun strcmpAt93(a: CharArray, aOff: Int, b: CharArray, bOff: Int): Int {
    var i = aOff
    var j = bOff
    while (true) {
        if (a[i] != b[j]) return if (a[i] < b[j]) -1 else 1
        if (a[i] == NUL) return 0
        i++
        j++
    }
}

// section register of the H-011 state machine (vendor label order)
private enum class Pc93 { START, NCHAR, SCAN, UNNEST, VAR1, EXEC, NXTEXPR, DECSTK, NXTOPR, SYM1 }

object Parse93Static {
    // file-scope scratch for the numcomp offset adapter (const static
    // fold precedent, Patrn1Sys1Static)
    val sliceA = CharArray(STR_MAX)
    val sliceB = CharArray(STR_MAX)
}

private fun numcompAt93(l: CharArray, lOff: Int, r: CharArray, rOff: Int): Int {
    // numcomp(&l[lOff], &r[rOff]): slice both C strings to offset-0
    // copies then call the landed numcomp render (H-014 pointer-shift
    // adapter; the slices reconstruct the exact C operands)
    var d = 0
    var s = lOff
    while (l[s] != NUL) {
        Parse93Static.sliceA[d] = l[s]
        d++
        s++
    }
    Parse93Static.sliceA[d] = NUL
    d = 0
    s = rOff
    while (r[s] != NUL) {
        Parse93Static.sliceB[d] = r[s]
        d++
        s++
    }
    Parse93Static.sliceB[d] = NUL
    return numcomp(Parse93Static.sliceA, Parse93Static.sliceB)
}
// vendor interp-parse.cpp const static unsigned char code[26] (verbatim)
private val CODE_93 = intArrayOf(
    99, 99, 99, 99, 99, 99, 99, 99, 18, 19,
    99, 20, 18, 99, 99, 21, 22, 23, 99, 99,
    99, 99, 99, 99, 26, 27,
)

// vendor interp-parse.cpp const static unsigned char opcode[256] (only the first 128 entries are initialized in the vendor; the C zero-pad tail renders as explicit zeros - quirk ledger in the unit KDoc)
private val OPCODE_93 = intArrayOf(
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
    0, 0, 0, 0, 0, 0,
)

// vendor interp-parse.cpp const static unsigned char ncode[256] (only the first 128 entries are initialized in the vendor; the C zero-pad tail renders as explicit zeros - quirk ledger in the unit KDoc)
private val NCODE_93 = intArrayOf(
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
    0, 0, 0, 0, 0, 0,
)

// vendor interp-parse.cpp const static unsigned char dcode[256] (only the first 128 entries are initialized in the vendor; the C zero-pad tail renders as explicit zeros - quirk ledger in the unit KDoc)
private val DCODE_93 = intArrayOf(
    10, 99, 99, 99, 99, 99, 99, 99, 99, 99,
    99, 99, 99, 99, 99, 99, 99, 99, 99, 99,
    99, 99, 99, 99, 99, 99, 99, 99, 99, 99,
    99, 99, 10, 10, 99, 10, 99, 99, 10, 10,
    99, 10, 10, 10, 10, 10, 99, 10, 99, 99,
    99, 99, 99, 99, 99, 99, 99, 99, 10, 99,
    10, 10, 10, 10, 10, 99, 99, 99, 99, 99,
    99, 99, 99, 99, 99, 99, 99, 99, 99, 99,
    99, 99, 99, 99, 99, 99, 99, 99, 99, 99,
    99, 10, 10, 10, 10, 10, 99, 99, 99, 99,
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
    0, 0, 0, 0, 0, 0,
)

// vendor interp-parse.cpp const static unsigned char operand[256] (only the first 128 entries are initialized in the vendor; the C zero-pad tail renders as explicit zeros - quirk ledger in the unit KDoc)
private val OPERAND_T93 = intArrayOf(
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
    0, 0, 0, 0, 0, 0,
)


fun parse_(svPtr: Msv): Int {
    // vendor locals (H-010 Int render of the short set)
    var ernbr = 0 // read back through the patrn1 holder below
    var f = 0
    var spx = 0
    var adx = 0
    var i = 0
    var j = 0
    var g = 0
    var bbtyp: Char = NUL
    var nxtchr: Char = NUL
    var FcnArgToggle = 0
    val s1p = IntArray(40) // unsigned char s1p[40] (H-007 auto bounds)
    val tmp3 = CharArray(STR_MAX)
    val jpx93 = ShortArray(1) // patrn1 out-param holders (H-006)
    val ernb93 = ShortArray(1)
    val bd = svPtr.bd
    val v1d = svPtr.v1d
    val pd1 = svPtr.pd1
    val xd = svPtr.xd
    var pc = Pc93.START

    /*
     * initialization (vendor: svPtr->bd[0] = 0; v1dp = &svPtr->v1d[1];
     * svPtr->xpx--; spx = 0; the two NUL stores at pd1len + 2/3 and
     * the sdlim base)
     */
    bd[0] = NUL
    svPtr.xpx--
    spx = 0
    pd1[svPtr.pd1len.toInt() + 2] = NUL
    pd1[svPtr.pd1len.toInt() + 3] = NUL
    svPtr.sdlim = svPtr.pd1len.toInt() + 3

    // H-011 state machine: one branch per vendor label, vendor order.
    machine@ while (true) {
        when (pc) {
            Pc93.START -> {
                // start: stack foundation (also the recursion reentry)
                spx++
                s1p[spx] = PEMPTY_93
                pc = Pc93.NCHAR
            }

            Pc93.NCHAR -> {
                // nchar:
                svPtr.xpx++
                nxtchr = xd[svPtr.xpx]
                if (nxtchr == NUL) {
                    // goto finish
                    // finish: exit sequence
                    if (s1p[spx - 1] != PEMPTY_93 || s1p[spx] != OPERAND_93) return 16
                    return 0
                }

                if (OPERAND_T93[b93(nxtchr)] == 1) { pc = Pc93.SCAN; continue@machine }

                if (nxtchr == CodedOpen.toChar()) {
                    spx++
                    while (true) { // while (pd1[sdlim++]);
                        val sh = svPtr.sdlim
                        svPtr.sdlim++
                        if (pd1[sh] == NUL) break
                    }
                    // copy v1d[1..] onto the stack top, mark CodedOpen
                    var p1 = 1
                    var p2 = svPtr.sdlim
                    while (v1d[p1] != NUL) {
                        pd1[p2] = v1d[p1]
                        p1++
                        p2++
                    }
                    pd1[p2] = NUL
                    p2--
                    pd1[p2] = CodedOpen.toChar()
                    s1p[spx] = OPENC_93
                    pc = Pc93.NCHAR
                    continue@machine
                }

                if (nxtchr == ',') {
                    if (s1p[spx - 1] != OPENC_93) {
                        if (svPtr.t2 > 0) {
                            // goto finish
                            if (s1p[spx - 1] != PEMPTY_93 || s1p[spx] != OPERAND_93) return 16
                            return 0
                        }
                        return 16
                    }

                    // join tops
                    svPtr.sdlim--
                    i = svPtr.sdlim
                    while (true) { // for (i = --sdlim; (pd1[i] = pd1[i+1]); i++);
                        pd1[i] = pd1[i + 1]
                        if (pd1[i] == NUL) break
                        i++
                    }
                    pd1[i] = CodedComma.toChar() // coded comma
                    pd1[i + 1] = NUL
                    svPtr.sdlim--

                    while (true) { // while (pd1[sdlim--]); find new top
                        val sh = svPtr.sdlim
                        svPtr.sdlim--
                        if (pd1[sh] == NUL) break
                    }

                    svPtr.sdlim += 2
                    spx-- // stack pointer
                    pc = Pc93.NCHAR
                    continue@machine
                }

                if (nxtchr == ')') {
                    if (s1p[spx - 1] != POPEN_93) {
                        if (s1p[spx - 1] != OPENC_93) return 16

                        // fcn/array return section
                        svPtr.sdlim--
                        i = svPtr.sdlim
                        while (true) { // join tops
                            pd1[i] = pd1[i + 1]
                            if (pd1[i] == NUL) break
                            i++
                        }
                        pd1[i] = CodedClose.toChar() // coded close
                        pd1[i + 1] = NUL
                        svPtr.sdlim--

                        while (true) { // find new stack top start
                            val sh = svPtr.sdlim
                            svPtr.sdlim--
                            if (pd1[sh] == NUL) break
                        }

                        svPtr.sdlim += 2
                        spx-- // stack pointer
                        pc = Pc93.UNNEST // goto un_nest
                        continue@machine
                    }

                    // precedence close paren
                    if (s1p[spx] != OPERAND_93) return 12

                    // extract value from stack top
                    copyAt93(bd, 1, pd1, svPtr.sdlim) // strmove(bp, &pd1[sdlim])

                    // compress stack (do sdlim--; while (pd1[sdlim-1]);)
                    do {
                        svPtr.sdlim--
                    } while (pd1[svPtr.sdlim - 1] != NUL)

                    pc = Pc93.DECSTK // goto dec_stk
                    continue@machine
                }

                if (nxtchr == ':') {
                    i = svPtr.sdlim
                    do {
                        i--
                    } while (pd1[i - 1] != NUL)

                    if (pd1[i] != '$' ||
                        (pd1[i + 1] != 's' && pd1[i + 1] != 'S')
                    ) {
                        // goto finish
                        if (s1p[spx - 1] != PEMPTY_93 || s1p[spx] != OPERAND_93) return 16
                        return 0
                    }

                    svPtr.sdlim--
                    copyAt93(pd1, svPtr.sdlim, pd1, svPtr.sdlim + 1)
                    // strmove(&pd1[sdlim], &pd1[sdlim + 1])

                    while (true) { // while (pd1[sdlim--]);
                        val sh = svPtr.sdlim
                        svPtr.sdlim--
                        if (pd1[sh] == NUL) break
                    }

                    svPtr.sdlim += 2
                    catAtFrom93(pd1, svPtr.sdlim, COD209_93, 0)
                    // strcat(&pd1[sdlim], cod209)
                    spx--
                    pc = Pc93.NCHAR
                    continue@machine
                }

                if (nxtchr == ' ' || nxtchr == TAB_93) {
                    // goto finish
                    if (s1p[spx - 1] != PEMPTY_93 || s1p[spx] != OPERAND_93) return 16
                    return 0
                }

                if (nxtchr == '^' && svPtr.t2 == 2 && s1p[spx] == OPERAND_93) {
                    // goto finish
                    if (s1p[spx - 1] != PEMPTY_93 || s1p[spx] != OPERAND_93) return 16
                    return 0
                }

                // check for delimiters in list
                spx++

                if (nxtchr == QUOTE_93) {
                    // insert not based operator code
                    svPtr.xpx++
                    s1p[spx] = OPCODE_93[b93(xd[svPtr.xpx])]

                    if (s1p[spx] != 99) {
                        // code[opcode[xd[xpx]]] - the code[26] OOB guard
                        val cv = OPCODE_93[b93(xd[svPtr.xpx])]
                        s1p[spx] = if (cv < CODE_93.size) CODE_93[cv] else 0

                        // patch for the bracket-follows-quote pair
                        if (xd[svPtr.xpx + 1] == ']') svPtr.xpx++

                        if (s1p[spx] != 99) {
                            pc = Pc93.NCHAR
                            continue@machine
                        }
                    }

                    svPtr.xpx--
                }

                if (nxtchr == '*' && xd[svPtr.xpx + 1] == '*') {
                    // exponentiation
                    svPtr.xpx++
                    s1p[spx] = EXPONEN_93
                    pc = Pc93.NCHAR
                    continue@machine
                }

                if (nxtchr == ']' && xd[svPtr.xpx + 1] == ']') {
                    // collates after - becomes follows
                    svPtr.xpx++
                    s1p[spx] = FOLLOWS_93
                    pc = Pc93.NCHAR
                    continue@machine
                }

                // insert code
                s1p[spx] = OPCODE_93[b93(nxtchr)]

                if (s1p[spx] != 99) {
                    pc = Pc93.NCHAR
                    continue@machine
                }

                svPtr.ErrChar = nxtchr
                return 12
            }

            Pc93.SCAN -> {
                // scan_operand:
                jpx93[0] = svPtr.xpx.toShort()

                if (s1p[spx] == PATTERN_93 || s1p[spx] == NOTPATTERN_93) {
                    bd[1] = NUL
                    Stubs.patrn1(jpx93, ernb93, svPtr)

                    ernbr = ernb93[0].toInt()
                    if (ernbr != 0) return ernbr

                    pc = Pc93.EXEC // goto exec
                    continue@machine
                }

                // numeric operand
                if (NCODE_93[b93(xd[svPtr.xpx])] == 10) {
                    if (svPtr.t0px > 0 && xd[svPtr.xpx] == '.' &&
                        (isAlpha93(xd[svPtr.xpx + 1]) ||
                            xd[svPtr.xpx + 1] == '_' ||
                            xd[svPtr.xpx + 1] == '%')
                    ) {
                        bd[1] = '.'
                        svPtr.xpx++

                        i = 2
                        while (true) { // for (i = 2; isalnum(bd[i] = xd[xpx]); i++, xpx++)
                            bd[i] = xd[svPtr.xpx]
                            if (!isAlnum93(bd[i])) break
                            i++
                            svPtr.xpx++
                        }

                        bd[i] = NUL
                        svPtr.xpx--
                        pc = Pc93.EXEC
                        continue@machine
                    }

                    // remove leading zeros
                    while (xd[svPtr.xpx] == '0') svPtr.xpx++

                    // all zeros
                    if (NCODE_93[b93(xd[svPtr.xpx])] != 10) svPtr.xpx--

                    i = 1
                    while (true) { // for (i = 1; (bd[i] = xd[xpx]) && ncode[bd[i]] == 10; i++, xpx++)
                        bd[i] = xd[svPtr.xpx]
                        if (bd[i] == NUL) break
                        if (NCODE_93[b93(bd[i])] != 10) break
                        i++
                        svPtr.xpx++
                    }

                    if (i == 1 && bd[1] == '.') return 9

                    bd[i] = NUL
                    svPtr.xpx--
                    pc = Pc93.EXEC
                    continue@machine
                }

                // literal operand
                if (xd[svPtr.xpx] == '"') {
                    j = 0

                    literal@ while (true) {
                        inner@ while (true) { // while (xd[++xpx] != '"')
                            svPtr.xpx++
                            if (xd[svPtr.xpx] != '"') {
                                j++
                                bd[j] = xd[svPtr.xpx]
                                if (bd[j] == NUL) return 2
                                continue@inner
                            }
                            break@inner
                        }

                        if (xd[svPtr.xpx + 1] != '"') {
                            j++
                            bd[j] = NUL
                            pc = Pc93.EXEC
                            continue@machine
                        }

                        svPtr.xpx++
                        j++
                        bd[j] = '"'
                        continue@literal
                    }
                }

                // variable name
                v1d[1] = xd[svPtr.xpx]
                svPtr.xpx++
                j = 2

                varname@ while (true) {
                    // indirect name and indirect indices case
                    if (xd[svPtr.xpx] == '@' && xd[svPtr.xpx + 1] == '(' && s1p[spx] != INDIRECT_93) {
                        // strcpy(&xd[xpx], &xd[xpx+1]) left-shift
                        var sh = svPtr.xpx
                        var dh = svPtr.xpx
                        while (sh + 1 < xd.size && xd[sh + 1] != NUL) {
                            xd[dh] = xd[sh + 1]
                            dh++
                            sh++
                        }
                        xd[dh] = NUL
                        // goto idxin
                    } else if (DCODE_93[b93(xd[svPtr.xpx])] == 10) {
                        v1d[j] = NUL
                        svPtr.xpx--
                        pc = Pc93.VAR1 // goto var1
                        continue@machine
                    }

                    // idxin:
                    v1d[j] = xd[svPtr.xpx]
                    j++

                    if (xd[svPtr.xpx] == '(') {
                        j--
                        v1d[j] = NUL
                        svPtr.xpx--
                        xd[svPtr.xpx] = CodedOpen.toChar()
                        pc = Pc93.START // goto start: recurse
                        continue@machine
                    }

                    svPtr.xpx++
                }
            }

            Pc93.UNNEST -> {
                // un_nest: copy answer from stack
                while (true) { // while (pd1[--sdlim]);
                    svPtr.sdlim--
                    if (pd1[svPtr.sdlim] == NUL) break
                }

                copyAt93(v1d, 1, pd1, svPtr.sdlim + 1)
                // strmove(v1dp, &pd1[sdlim + 1])

                while (true) {
                    svPtr.sdlim--
                    if (pd1[svPtr.sdlim] == NUL) break
                }

                svPtr.sdlim++
                spx -= 2
                pc = Pc93.VAR1 // fall-through into var1
                continue@machine
            }

            Pc93.VAR1 -> {
                // var1:
                if (svPtr.t0px == 1 && spx == 1) {
                    spx--
                    svPtr.t0px = 0
                    return 0
                }

                // the piece interface: setname capture when the stack
                // top names the piece function (dollar byte literals)
                if (pd1[svPtr.sdlim] == '$' && svPtr.t0px == 1 &&
                    (pd1[svPtr.sdlim + 1] == 'P' || pd1[svPtr.sdlim + 1] == 'p')
                ) {
                    if (svPtr.setname[0] == NUL) copyAt93(svPtr.setname, 0, v1d, 1)
                }

                // the extract interface
                if (pd1[svPtr.sdlim] == '$' && svPtr.t0px == 1 &&
                    (pd1[svPtr.sdlim + 1] == 'E' || pd1[svPtr.sdlim + 1] == 'e')
                ) {
                    if (svPtr.setname[0] == NUL) {
                        copyAt93(svPtr.setname, 0, v1d, 1)
                    }
                }

                if (v1d[1] == '^') { // global var
                    g = RETRIEVE

                    // the no-eval ZZ interface (bytes above 127 fold to 1)
                    if (up93(pd1[svPtr.sdlim + 1]) == 'Z' &&
                        up93(pd1[svPtr.sdlim + 2]) == 'Z'
                    ) {
                        var zi = 0
                        while (v1d[1 + zi] != NUL) {
                            if (v1d[1 + zi] > '\u007f') v1d[1 + zi] = CHAR_1
                            zi++
                        }
                        copyAt93(bd, 1, v1d, 1) // strcpy(&bd[1], v1dp)
                        pc = Pc93.EXEC
                        continue@machine
                    }

                    if (pd1[svPtr.sdlim] == '$' && s1p[spx] != INDIRECT_93) {
                        FcnArgToggle++
                        FcnArgToggle %= 2

                        // some global references must be passed without
                        // evaluation: hide the ref behind the gbl token
                        if ((up93(pd1[svPtr.sdlim + 1]) == 'N' &&
                                up93(pd1[svPtr.sdlim + 1]) != 'A') ||
                            up93(pd1[svPtr.sdlim + 1]) == 'Q'
                        ) {
                            g = NEXT
                            // hide global in a static array for later
                            // extraction
                            Stubs.BuildGlobal(0, g, v1d, null, svPtr)
                            copyStrAt93(bd, 1, "<gbl>")
                            pc = Pc93.EXEC
                            continue@machine
                        }

                        // the order function
                        if (up93(pd1[svPtr.sdlim + 1]) == 'O') {
                            i = svPtr.sdlim
                            while (pd1[i] != NUL) i++
                            i--

                            if (pd1[i] == OPEN_PAREN_93.toChar()) {
                                // first argument
                                copyAt93(bd, 1, v1d, 1)
                                var bi = 1
                                while (bd[bi] != NUL) {
                                    if (bd[bi] > '\u007f') bd[bi] = CHAR_1
                                    bi++
                                }
                                pc = Pc93.EXEC
                                continue@machine
                            } else {
                                f = Stubs.Mglobal(g, v1d, bd, svPtr)
                                pc = Pc93.EXEC
                                continue@machine
                            }
                        }

                        if (up93(pd1[svPtr.sdlim + 1]) == 'B') {
                            g = ORDERPREV_93
                            Stubs.BuildGlobal(0, g, v1d, null, svPtr)
                            copyStrAt93(bd, 1, "<gbl>")
                            pc = Pc93.EXEC
                            continue@machine
                        }
                    }

                    f = Stubs.Mglobal(g, v1d, bd, svPtr)

                    // fix the order end string
                    if (up93(pd1[svPtr.sdlim + 1]) == 'O') {
                        // strcmp(bp, "-1") == 0
                        if (bd[1] == '-' && bd[2] == '1' && bd[3] == NUL) bd[1] = NUL
                    }

                    // check for the data function
                    if (pd1[svPtr.sdlim] == '$' && s1p[spx] != INDIRECT_93 &&
                        (pd1[svPtr.sdlim + 1] == 'D' || pd1[svPtr.sdlim + 1] == 'd')
                    ) {
                        bbtyp = if (f == 0) '0' else '1'

                        // i = strlen(v1dp) with v1dp = &v1d[1]
                        i = strlenAt93(v1d, 1)

                        // child? (the SQLITE-resolved live arm; the
                        // 1-byte child walk). v1dp[i-1] reads the LAST
                        // char of the running name: v1d[1+i-1].
                        if (v1d[i] != CHAR_1) {
                            // v1dp[i++] = 1 twice then 0, base 1
                            v1d[1 + i] = CHAR_1
                            i++
                            v1d[1 + i] = CHAR_1
                            i++
                            v1d[1 + i] = NUL
                            i++
                        }

                        v1d[1 + i] = '\u0002'
                        v1d[1 + i + 1] = CHAR_1
                        v1d[1 + i + 2] = NUL

                        f = Stubs.Mglobal(NEXT, v1d, bd, svPtr)

                        bd[2] = bbtyp

                        bd[1] = if (f == 1) '1' else '0'

                        if (bd[1] == '0') {
                            bd[1] = bd[2]
                            bd[2] = NUL
                        }

                        bd[3] = NUL
                        pc = Pc93.EXEC
                        continue@machine
                    }

                    svPtr.tpx = f.toLong()
                    pc = Pc93.EXEC
                    continue@machine
                }

                // normal global follows: the dollar built-in arms
                if (v1d[1] == '$') {
                    if (v1d[2] == 'd' || v1d[2] == 'D') {
                        // open
                        i = 1
                        while (v1d[i] != CodedOpen.toChar()) i++

                        j = 0

                        // close
                        while (true) { // while ((bd[++j] = v1d[++i]) != CodedClose)
                            j++
                            i++
                            bd[j] = v1d[i]
                            if (bd[j] == CodedClose.toChar()) break
                        }

                        bd[j] = NUL
                        pc = Pc93.EXEC
                        continue@machine
                    }

                    if (v1d[2] == 'z' || v1d[2] == 'Z') {
                        Stubs.zfcn(v1d, bd, svPtr)
                    } else {
                        Stubs.fcn(svPtr)
                    }

                    if (svPtr.ierr == 0) {
                        pc = Pc93.EXEC
                        continue@machine
                    }

                    return svPtr.ierr
                }

                if (pd1[svPtr.sdlim] == '$' && s1p[spx] == OPENC_93 &&
                    (pd1[svPtr.sdlim + 1] == 'G' || pd1[svPtr.sdlim + 1] == 'g')
                ) {
                    svPtr.symflg = if (pd1[svPtr.sdlim + 1] == 'G' || pd1[svPtr.sdlim + 1] == 'g') SYMNAME_93 else SYMNEXT_93
                    // vendor: toupper(...) == 'G' -> SymName else SymNext;
                    // the original letter decides, lowercase maps SymNext

                    Stubs.sym_(svPtr.symflg, v1d, bd, svPtr)

                    if (up93(pd1[svPtr.sdlim + 1]) == 'N' && bd[1] == NUL) {
                        copyStrAt93(bd, 1, "-1")
                    }

                    if (svPtr.symflg != 0 && s1p[spx] != INDIRECT_93) {
                        pc = Pc93.EXEC
                        continue@machine
                    }

                    return 27
                }

                // retrieve look-up
                if (v1d[1] == PREDICATEVAR_93) {
                    copyAt93(bd, 1, v1d, 1) // strmove(bp, v1dp)
                    pc = Pc93.EXEC
                    continue@machine
                }

                if (pd1[svPtr.sdlim] == '$' && s1p[spx] == OPENC_93) {
                    if (up93(pd1[svPtr.sdlim + 1]) == 'Q') { // query, qlength, qsubscript
                        g = NEXT
                        Stubs.BuildLocal(0, g, v1d, null, svPtr)
                        copyStrAt93(bd, 1, "<lcl>")
                        pc = Pc93.EXEC
                        continue@machine
                    }

                    if (up93(pd1[svPtr.sdlim + 1]) == 'N') {
                        g = NEXT
                        Stubs.BuildLocal(0, g, v1d, null, svPtr)
                        copyStrAt93(bd, 1, "<lcl>")
                        pc = Pc93.EXEC
                        continue@machine
                    }

                    if (up93(pd1[svPtr.sdlim + 1]) == 'O') {
                        FcnArgToggle++
                        FcnArgToggle %= 2

                        if (FcnArgToggle == 1) {
                            g = ORDERNEXT_93
                            Stubs.BuildLocal(0, g, v1d, null, svPtr)
                            copyStrAt93(bd, 1, "<lcl>")
                            pc = Pc93.EXEC
                            continue@machine
                        }
                    }
                }

                svPtr.symflg = SYMRETRIEVE_93

                // normal variable lookup
                if (Stubs.sym_(svPtr.symflg, v1d, bd, svPtr) == null) {
                    svPtr.symflg = 0
                } else {
                    svPtr.symflg = 1
                }

                if (pd1[svPtr.sdlim] != '$') {
                    pc = Pc93.SYM1 // goto sym1
                    continue@machine
                }

                // local variable data function
                if (s1p[spx] == OPENC_93 &&
                    (pd1[svPtr.sdlim + 1] == 'd' || pd1[svPtr.sdlim + 1] == 'D')
                ) {
                    svPtr.symflg = SYMDATA_93
                    Stubs.sym_(svPtr.symflg, v1d, bd, svPtr)
                    pc = Pc93.EXEC
                    continue@machine
                }

                // fall-through to sym1 (the not-found check)
                pc = Pc93.SYM1
                continue@machine
            }

            Pc93.SYM1 -> {
                // sym1: check for variable not found
                if (svPtr.symflg == 0) {
                    var si = 1
                    var sj: Int
                    var k = 1

                    labelscan@ while (true) { // look for a label of the same name
                        sj = si
                        while (pd1[sj] != TAB_93 && pd1[sj] != ' ') sj++

                        // strncmp(&pd1[si], v1dp, sj - si) == 0 &&
                        // strlen(v1dp) == sj - si
                        var eq = true
                        var ci = 0
                        while (ci < sj - si) {
                            if (pd1[si + ci] != v1d[1 + ci]) { eq = false; break }
                            ci++
                        }
                        if (eq && (sj - si) > 0) {
                            // full length match of the name (the v1dp
                            // string must END at sj - si too: the name
                            // stored at v1d[1..] is NUL-terminated at
                            // its own length)
                            if (sj - si == strlenAt93(v1d, 1)) {
                                // sprintf(bp, "%d", k)
                                val bs = k.toString()
                                copyStrAt93(bd, 1, bs)
                                pc = Pc93.EXEC
                                continue@machine
                            }
                        }

                        si = si + strlenAt93(pd1, si) + 1

                        if (si > svPtr.pd1len) break@labelscan

                        k++
                    }

                    return 17
                }

                // symflg held: fall through to exec
                pc = Pc93.EXEC
                continue@machine
            }

            Pc93.EXEC -> {
                // exec:
                if (s1p[spx] != PEMPTY_93 && s1p[spx] != POPEN_93 && s1p[spx] != OPENC_93) {
                    pc = Pc93.NXTEXPR // goto nxt_expr
                    continue@machine
                }

                spx++

                while (true) { // while (pd1[sdlim++]);
                    val sh = svPtr.sdlim
                    svPtr.sdlim++
                    if (pd1[sh] == NUL) break
                }

                copyAt93(pd1, svPtr.sdlim, bd, 1)
                // strmove(&pd1[sdlim], bp)
                s1p[spx] = OPERAND_93
                pc = Pc93.NCHAR
                continue@machine
            }

            Pc93.NXTEXPR -> {
                // nxt_expr: process expression - check for operator on
                // stack top
                if (s1p[spx] == POPEN_93 || s1p[spx] == OPERAND_93) return 11

                if (s1p[spx - 1] != OPERAND_93) {
                    when (s1p[spx]) {
                        INDIRECT_93 -> {
                            // strcat(bp, &xd[xpx + 1]); strmove(&xd[1], bp)
                            catAtFrom93(bd, 1, xd, svPtr.xpx + 1)
                            copyAt93(xd, 1, bd, 1)
                            spx--
                            svPtr.xpx = 0

                            if (svPtr.kflg != 0 && spx <= 1) {
                                svPtr.xpx = 1
                                return 0
                            }

                            pc = Pc93.NCHAR
                            continue@machine
                        }

                        MINUS_93 -> { // unary minus
                            copyStrAt93(tmp3, 0, "-1")
                            Stubs.mult(bd, tmp3, bd)
                        }

                        NOT_NOT_93 -> { // not not
                            // numcomp(bp, "0")
                            copyStrAt93(tmp3, 0, "0")
                            bd[1] = if (numcompAt93(bd, 1, tmp3, 0) != 0) '1' else '0'
                            bd[2] = NUL
                        }

                        NOT_93 -> { // unary not
                            copyStrAt93(tmp3, 0, "0")
                            bd[1] = if (numcompAt93(bd, 1, tmp3, 0) != 0) '0' else '1'
                            bd[2] = NUL
                        }

                        PLUS_93 -> { // unary plus
                            copyStrAt93(tmp3, 0, "0")
                            Stubs.add(bd, tmp3, bd)
                        }

                        else -> return 1
                    }

                    spx--
                    pc = Pc93.NXTOPR // fall to nxt_operator
                    continue@machine
                }

                // extract value under operator
                // for (adx = sdlim--; pd1[sdlim - 1]; sdlim--);
                adx = svPtr.sdlim
                svPtr.sdlim--
                while (pd1[svPtr.sdlim - 1] != NUL) {
                    svPtr.sdlim--
                }

                // branch depending upon operator
                when (s1p[spx]) {
                    DIVIDE_93 -> Stubs.divx(pd1, bd, bd) // op(&pd1[adx], bp&bd[1], bp), shift ledger

                    EXPONEN_93 -> Stubs.expx(pd1, bd, bd) // op(&pd1[adx], bp&bd[1], bp), shift ledger

                    MULTIPLY_93 -> Stubs.mult(pd1, bd, bd) // op(&pd1[adx], bp&bd[1], bp), shift ledger

                    MINUS_93 -> Stubs.sub(pd1, bd, bd) // op(&pd1[adx], bp&bd[1], bp), shift ledger

                    PLUS_93 -> Stubs.add(pd1, bd, bd) // op(&pd1[adx], bp&bd[1], bp), shift ledger

                    CONCAT_93 -> {
                        var to = adx // start of left operand
                        while (pd1[to] != NUL) to++ // find end of left
                        to-- // retreat from the NUL
                        var from = 1 // bp base
                        while (true) { // concatenate
                            pd1[to] = bd[from]
                            if (pd1[to] == NUL) break
                            to++
                            from++
                        }
                        if (to - adx >= STR_MAX) return 35 // too long
                        // copy result to bp
                        to = 1
                        from = adx
                        while (true) {
                            bd[to] = pd1[from]
                            if (bd[to] == NUL) break
                            to++
                            from++
                        }
                    }

                    EQUALS_93 -> {
                        bd[1] = if (strcmpAt93(pd1, adx, bd, 1) != 0) '0' else '1'
                        bd[2] = NUL
                    }

                    NOTEQ_93 -> {
                        bd[1] = if (strcmpAt93(pd1, adx, bd, 1) != 0) '1' else '0'
                        bd[2] = NUL
                    }

                    GREATER_93 -> {
                        bd[1] = if (numcompAt93(pd1, adx, bd, 1) > 0) '1' else '0'
                        bd[2] = NUL
                    }

                    LESSTHAN_93 -> {
                        bd[1] = if (numcompAt93(pd1, adx, bd, 1) < 0) '1' else '0'
                        bd[2] = NUL
                    }

                    INTDIVIDE_93 -> Stubs.divi(pd1, bd, bd) // op(&pd1[adx], bp&bd[1], bp), shift ledger

                    MODULO_93 -> Stubs.modulo(pd1, bd, bd) // op(&pd1[adx], bp&bd[1], bp), shift ledger

                    CONTAINS_93 -> {
                        // vendor: pd1[adx] == 0 && bp == 0 (the pointer
                        // test is dead; the literal empty-and-empty arm)
                        if (pd1[adx] == NUL) {
                            bd[1] = '1'
                            bd[2] = NUL
                        } else {
                            // xindex(&pd1[adx], bp, 1)
                            bd[1] = if (Stubs.xindex(pd1, bd, 1) != 0) '0' else '1' // aOff adx, bOff 1
                            bd[2] = NUL
                        }
                    }

                    FOLLOWS_93 -> {
                        bd[1] = if (strcmpAt93(pd1, adx, bd, 1) <= 0) '0' else '1'
                        bd[2] = NUL
                    }

                    PATTERN_93 -> {
                        if (Stubs.rpm(pd1, bd, svPtr) != 0) bd[1] = '0' else bd[1] = '1' // stringOff adx, patternOff 0, ledger == 0
                        bd[2] = NUL
                    }

                    NOTPATTERN_93 -> {
                        if (Stubs.rpm(pd1, bd, svPtr) != 0) bd[1] = '1' else bd[1] = '0' // stringOff adx, patternOff 0, ledger == 0
                        bd[2] = NUL
                    }

                    NOTGREATER_93 -> {
                        bd[1] = if (numcompAt93(pd1, adx, bd, 1) <= 0) '1' else '0'
                        bd[2] = NUL
                    }

                    NOTLESS_93 -> {
                        bd[1] = if (numcompAt93(pd1, adx, bd, 1) >= 0) '1' else '0'
                        bd[2] = NUL
                    }

                    NOTCONTAINS_93 -> {
                        bd[1] = if (Stubs.xindex(pd1, bd, 1) != 0) '1' else '0' // aOff adx, bOff 1
                        bd[2] = NUL
                    }

                    NOTFOLLOWS_93 -> {
                        bd[1] = if (strcmpAt93(pd1, adx, bd, 1) <= 0) '1' else '0'
                        bd[2] = NUL
                    }

                    AND_93 -> {
                        copyStrAt93(tmp3, 0, "0")
                        bd[1] = if (numcompAt93(bd, 1, tmp3, 0) != 0 &&
                            numcompAt93(pd1, adx, tmp3, 0) != 0) '1' else '0'
                        bd[2] = NUL
                    }

                    OR_93 -> {
                        copyStrAt93(tmp3, 0, "0")
                        bd[1] = if (numcompAt93(bd, 1, tmp3, 0) != 0 ||
                            numcompAt93(pd1, adx, tmp3, 0) != 0) '1' else '0'
                        bd[2] = NUL
                    }

                    NOTAND_93 -> {
                        copyStrAt93(tmp3, 0, "0")
                        if (numcompAt93(bd, 1, tmp3, 0) != 0 && numcompAt93(pd1, adx, tmp3, 0) != 0) {
                            bd[1] = '0'
                        } else {
                            bd[1] = '1'
                        }
                        bd[2] = NUL
                    }

                    NOTOR_93 -> {
                        copyStrAt93(tmp3, 0, "0")
                        if (numcompAt93(bd, 1, tmp3, 0) != 0 || numcompAt93(pd1, adx, tmp3, 0) != 0) {
                            bd[1] = '0'
                        } else {
                            bd[1] = '1'
                        }
                        bd[2] = NUL
                    }

                    else -> return 12 // OPERAND, PEMPTY, NOT
                }

                // dec_stk:
                spx -= 2
                pc = Pc93.NXTOPR
                continue@machine
            }

            Pc93.DECSTK -> {
                // unreachable-state guard: DECSTK is the dec_stk tail
                spx -= 2
                pc = Pc93.NXTOPR
                continue@machine
            }

            Pc93.NXTOPR -> {
                // nxt_operator:
                if (s1p[spx] != POPEN_93 && s1p[spx] != OPENC_93 && s1p[spx] != PEMPTY_93) {
                    pc = Pc93.NXTEXPR // goto nxt_expr
                    continue@machine
                }

                // push answer
                spx++
                svPtr.sdlim = svPtr.sdlim + strlenAt93(pd1, svPtr.sdlim) + 1

                var to = svPtr.sdlim
                var from = 1 // bp base
                while (true) { // while (*(to++) = *(from++));
                    pd1[to] = bd[from]
                    if (pd1[to] == NUL) break
                    to++
                    from++
                }
                pd1[to] = NUL

                s1p[spx] = OPERAND_93
                pc = Pc93.NCHAR
                continue@machine
            }
        }
    }
}
