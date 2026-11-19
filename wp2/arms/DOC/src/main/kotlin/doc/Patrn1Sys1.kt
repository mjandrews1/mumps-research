package doc

// Unit 26 (B1 tranche 4): compiler-sys1.c::patrn1 (batch 1, complex
// stratum; units.csv row 26: loc 37). The pattern-argument parser:
// alternates between a string-literal walk (through closing double
// quote, with the paired-quote 210 coded byte) and the delimiter
// walk that stops on the Mumps separator set, tracking the
// paren-nesting m; each accepted byte appends to the bd scratch at
// offset 1 (the &bd[1] idiom; bd rides CompilerState, added with
// this unit). The goto web (a1285 > a1283 > a1281, with a1281 able
// to branch back to a1285) linearizes per H-011 as the outer loop,
// the string-walk exit flag, and the continue/return tail. xd is
// the CompilerState.xd classifier buffer; xindex is the H-014 stub;
// the compiler extern xpx is CompilerState.xpx (the *jpx-1 store,
// H-010 widened via the Long carrier); jpx/ernbr are the short*
// out-param seats (ShortArray(1), H-006). The static tmp1[2] and
// cod210[2] = {210, 0} ride the holder object (H-006). The
// separator set string is kept byte-verbatim (it embeds two single
// quotes and a minus). H-015: the distinct same-named vendor unit
// interp-parse.cpp::patrn1 (unit 30) resolves by overload arity
// when it lands; this compiler-side unit owns the two-argument
// shape. Naming per H-017.

object Patrn1Sys1Static {
    // compiler-sys1.c static unsigned char tmp1[2]
    val tmp1 = CharArray(2)
    // compiler-sys1.c static unsigned char cod210[2] = {210, 0}
    val cod210 = charArrayOf(210.toChar(), NUL)
}

// compiler-sys1.c patrn1 separator set (" ,&:!''><=[)+-*/#_")
private val PATRN1_SEP = " ,&:!''><=[)+-*/#_".toCharArray()

fun patrn1(jpx: ShortArray, ernbr: ShortArray) {
    var m = 0
    var jp = jpx[0].toInt()
    Patrn1Sys1Static.tmp1[0] = NUL
    Patrn1Sys1Static.tmp1[1] = NUL

    val tmp1 = Patrn1Sys1Static.tmp1
    val cod210 = Patrn1Sys1Static.cod210

    // a1285 (outer loop)
    outer@ while (true) {
        if (CompilerState.xd[jp] == '"') {
            CstringOps.catAt(CompilerState.bd, 1, "\"")

            // a1283 (string literal walk)
            while (true) {
                jp++
                jpx[0] = jp.toShort()
                if (CompilerState.xd[jp] == NUL) {
                    ernbr[0] = 2
                    return
                }
                if (CompilerState.xd[jp] == '"') {
                    if (CompilerState.xd[jp + 1] != '"') {
                        // closing quote: goto a1281
                        break
                    }
                    // paired quote: append the coded 210 byte
                    CstringOps.catAt(CompilerState.bd, 1, cod210)
                    jp++
                    jpx[0] = jp.toShort()
                } else {
                    tmp1[0] = CompilerState.xd[jp]
                    CstringOps.catAt(CompilerState.bd, 1, tmp1)
                }
            }
            // string walk over: fall into the a1281 block below
        }

        // a1281
        if (CompilerState.xd[jp] != NUL) {
            tmp1[0] = CompilerState.xd[jp]
            if (m != 0 || Stubs.xindex(PATRN1_SEP, tmp1, 1) == 0) {
                if (CompilerState.xd[jp] == '(') m++
                if (CompilerState.xd[jp] == ')') m--
                CstringOps.catAt(CompilerState.bd, 1, tmp1)
                jp++
                jpx[0] = jp.toShort()
                continue@outer // goto a1285
            }
        }

        CompilerState.xpx = (jp - 1).toLong()
        jpx[0] = jp.toShort()
        ernbr[0] = 0
        return
    }
}
