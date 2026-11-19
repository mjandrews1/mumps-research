package doc

// Unit 30 (B1 tranche 5): interp-parse.cpp::patrn1 (batch 1,
// complex stratum; units.csv row 30). The interpreter-side pattern
// parser: same a1285/a1283/a1281 goto web as the compiler-side
// unit 26, but over the MSV carriers (svPtr->xd/bd/xpx,
// stateVector.h:45/42/56) and WITHOUT the m paren-nesting guard.
// goto linearization per H-011 (outer loop, string-walk break, the
// a1281 tail continue); xindex is the H-014 stub; the bd appends
// run at &bd[0] (the interpreter build target, no offset -1);
// cod210 rides the holder (H-006); the bin that expected a second
// is_html guard is noted. The Stubs.patrn1 seat closes to the
// delegate; the compiler-side unit 26 resolves the name by
// overload arity (H-015). Quirk kept: the a1281 block assigns
// tmp1[0] BEFORE the nonzero test (the write is unconditional in
// the vendor; kept). Naming per H-017.

object Patrn1InterpStatic {
    // interp-parse.cpp static unsigned char cod210[2] = {210, 0}
    val cod210 = charArrayOf(210.toChar(), NUL)
}

// interp-parse.cpp patrn1 separator set (byte-verbatim)
private val PATRN1_SEP_I = " ,&:!''><=[)+-*/#_".toCharArray()

fun patrn1(jpx: ShortArray, ernbr: ShortArray, svPtr: Msv) {
    val tmp1 = charArrayOf(NUL, NUL)

    // a1285 (outer loop)
    outer@ while (true) {
        if (svPtr.xd[jpx[0].toInt()] == '"') {
            CstringOps.catAt(svPtr.bd, 0, "\"")

            // a1283 (string literal walk)
            while (true) {
                jpx[0] = (jpx[0] + 1).toShort()
                if (svPtr.xd[jpx[0].toInt()] == NUL) {
                    ernbr[0] = 2
                    return
                }

                if (svPtr.xd[jpx[0].toInt()] == '"') {
                    if (svPtr.xd[jpx[0].toInt() + 1] != '"') {
                        // closing quote: goto a1281
                        break
                    }
                    // paired quote: the coded 210 byte
                    CstringOps.catAt(svPtr.bd, 0,
                            Patrn1InterpStatic.cod210)
                    jpx[0] = (jpx[0] + 1).toShort()
                } else {
                    tmp1[0] = svPtr.xd[jpx[0].toInt()]
                    CstringOps.catAt(svPtr.bd, 0, tmp1)
                }
            }
            // string walk over: fall into the a1281 block below
        }

        // a1281
        tmp1[0] = svPtr.xd[jpx[0].toInt()]

        if (svPtr.xd[jpx[0].toInt()] != NUL) {
            if (Stubs.xindex(PATRN1_SEP_I, tmp1, 1) == 0) {
                CstringOps.catAt(svPtr.bd, 0, tmp1)
                jpx[0] = (jpx[0] + 1).toShort()
                continue@outer // goto a1285
            }
        }

        svPtr.xpx = jpx[0].toInt() - 1
        ernbr[0] = 0
        return
    }
}
