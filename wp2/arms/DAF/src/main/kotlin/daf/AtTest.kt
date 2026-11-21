package daf

// Unit 109 (DAF B4 T2, core): compiler-at-test.c::atTest (24 LOC): the
// compiler @-test scanner: walks xd from xpx to the terminating
// space/NUL, sets at_flg on an unquoted '@', walks quoted spans with
// the doubled-quote escape, and returns the terminal index (at_flg)
// else 0; -1 on an imbalanced (unterminated) quote.
// Consults: BASE-log row 73 hit-reuse (the quote-scan phase of the
// goto mesh: the vendor while(k++) post-test walk renders as the
// labeled inner while loop, the outer walk as the labeled outer
// while; the for-k walk with k++ fallthrough kept verbatim), row 81
// hit-reuse (end-guard reads: xd[k]/xd[k+1] past the logical end
// yield NUL via the at() seat), rows 69/2 family hit-adapt (the
// printf error diagnostic lines kept verbatim on stdout via print,
// matching the vendor target), row 30 family (pd1Last is read-only
// here: value param, not an out-seat).
// Dafny: VERIFIED SPEC AT spec/at_test.dfy (journal row): the return
// contract (k or 0 when at_flg is unset, -1 on an unterminated quoted
// span) is modeled and verified there.

fun atTest(xd: CharArray, pd1: CharArray, pd1Last: Int, lines: Short, xpx: Int): Int {

    var k = xpx
    var atFlg = 0

    outer@ while (at(xd, k) != NUL_D && at(xd, k) != ' ') {

        if (at(xd, k) == '@') atFlg = 1

        if (at(xd, k) == '"') {

            k++ // while (k++) : the walk starts past the opening quote
            while (true) {
                val ch = at(xd, k)

                if (ch == NUL_D) {
                    print("*** Imbalanced quotes\n")
                    print("*** In or near line $lines\n")
                    print("*** ${DafCRuntime.cstringAt(pd1, pd1Last)}\n")
                    return -1
                }

                if (ch == '"' && at(xd, k + 1) == '"') {
                    k += 2 // k++ skips the doubled quote; the loop head
                    continue // adds the second increment
                }

                if (ch == '"') break

                k++
            }
        }

        k++
    }

    if (atFlg == 1) return k
    return 0
}
