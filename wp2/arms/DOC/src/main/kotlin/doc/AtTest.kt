package doc

// Unit 109 (B4 tranche 2): compiler-at-test.c::atTest (batch 4,
// core stratum; LOC 24, score 16.8, rank 153) - the embedded @-
// operator liveliness probe of the SET line compiler: scans xd
// from xpx to the end of the line for an at-sign outside quoted
// strings. Landing this unit converts the Stubs.atTest seat to a
// delegate (H-014 Extract2 delegate precedent; IfCode binds
// unchanged at its Stubs.atTest call site). pd1Last is the H-006
// holder: the vendor signature takes short *pd1Last and READS it
// for the imbalanced-quote diagnostic (this render reads
// pd1Last[0]; it does not write - any earlier ledger note that
// atTest writes the holder is superseded by the vendor body).
// printf maps to System.out per H-001. The doubled-quote escape
// walk is byte-faithful: while (k++) renders as the loop-top
// increment, so the doubled-quote continue path advances twice
// (the body k++ plus the loop-top k++), matching the vendor. The
// one-past-NUL read of xd[k + 1] when a quote is the buffer's
// final non-NUL byte is C-obscured UB; the Char carrier bounds it
// with an explicit size guard (divergence noted, H-007/H-012
// shape). Vendor quirks kept: the at_flg local is zero-initialized
// per the vendor initializer; the return is k on at_flg==1 else 0;
// the -1 diagnostic return keeps the three-banner sequence.
// Naming per H-017.

fun atTest(
    xd: CharArray,
    pd1: CharArray,
    pd1Last: ShortArray,
    lines: Int,
    xpx: Long
): Int {
    var at_flg = 0

    var k = xpx
    while (xd[k.toInt()] != NUL && xd[k.toInt()] != ' ') {

        if (xd[k.toInt()] == '@') at_flg = 1

        if (xd[k.toInt()] == '"') {

            while (true) {
                k++

                if (xd[k.toInt()] == NUL) {
                    System.out.print("*** Imbalanced quotes\n")
                    System.out.print("*** In or near line " + lines + "\n")
                    System.out.print(
                        "*** " + CstringOps.cstringAt(pd1, pd1Last[0].toInt()) + "\n"
                    )
                    return -1
                }

                if (xd[k.toInt()] == '"' &&
                    k + 1 < xd.size && xd[(k + 1).toInt()] == '"'
                ) {
                    k++
                    continue
                }

                if (xd[k.toInt()] == '"') break
            }
        }

        k++
    }

    return if (at_flg == 1) k.toInt() else 0
}
