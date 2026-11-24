package daf

// Unit 231 (DAF B8 T2, core): interp-sym.cpp::BuildLocal
// (29 LOC, score 16.7): the local-symbol build-stack sibling of
// the unit-230 BuildGlobal render: identical three-arm code
// dispatch over its OWN function-static TSTACK x STR_MAX scratch
// and stk counter (the vendor keeps two independent static stacks;
// vendor interp-sym.cpp:97, physically verified this session).
// SEAT RELEASE: the buildLocalDaf named stub of DafInterpStubsT3
// is released - the real body lands here under the stub's own name
// and signature, the stub is deleted.
// Consults (2026-11-24T21:17:30Z, nominal): the unit-230 consult
// set minus the global-order walk: the same overflow banner
// (LineNumber + sigintD(100), the unit-211 real), the same
// push/pop/reset contract, the holder object BuildLocalStateD
// (separate state from the BuildGlobal twin per the vendor
// function-statics), the vendor fff printf debug fossils kept
// verbatim as comments (the dead-code discipline). The code-1
// tail hands rslt to the RAISED localOrderDaf stub seat (owner
// interp-sym.cpp::LocalOrder, NOT a corpus unit - raised this
// render per the findDaf named-seat precedent; the "1"/"-1"
// direction carriages kept), other g values return 0 per vendor.
// Dafny: EXEMPT (stack dispatch over order seats; journal row).

private const val TSTACK_BL_D = 10 // #define TSTACK 10 (interp.h ledger)

private object BuildLocalStateD {
    // static unsigned char tmp[TSTACK][STR_MAX]
    val tmp = Array(TSTACK_BL_D) { CharArray(STR_MAX_DAF) }
    // static int stk
    var stk = 0
}

// LocalOrder(gb, prior, dir, svPtr) - named-owner stub raised by
// this render (owner interp-sym.cpp::LocalOrder; not a corpus
// unit).
fun localOrderDaf(gb: CharArray, prior: CharArray, dir: CharArray,
        sv: DafMsv): Int =
    throw NotImplementedError(
        "LocalOrder: out-of-scope owner (interp-sym.cpp::LocalOrder; not a corpus unit)")

fun buildLocalDaf(code: Int, g: Int, a: CharArray, out: CharArray,
        sv: DafMsv): Int {
    val T = BuildLocalStateD

    if (code < 0) {
        T.stk = 0
        return 1
    }

    if (code == 0) {

        if (T.stk >= TSTACK_BL_D) {
            System.out.print("*** Build Stack Overflow " + sv.LineNumber + "\n\n")
            sigintD(100)
        }

        // strcpy((char *) tmp[stk++],(const char *) str)
        var q = 0
        while (a[q] != NUL_D) {
            T.tmp[T.stk][q] = a[q]
            q++
        }
        T.tmp[T.stk][q] = NUL_D
        T.stk++
        // fff printf("4444444 str=%s\n", str); - debug fossil, H-013
        return 1
    }

    if (code == 2) {
        if (T.stk == 0) {
            out[0] = NUL_D // if (stk == 0) { rslt[0]=0; }
        } else {
            // else strcpy((char *) rslt, (const char *) tmp[--stk])
            T.stk--
            var q = 0
            while (T.tmp[T.stk][q] != NUL_D) {
                out[q] = T.tmp[T.stk][q]
                q++
            }
            out[q] = NUL_D
        }

        return 1
    }

    T.stk--

    // fff printf("555555 stack=%s g=%d ORDERNEXT=%d\n", tmp[stk],
    // g, ORDERNEXT); - debug fossil, H-013

    if (g == NEXT_D) { // LocalOrder(tmp[stk], rslt, "1", svPtr)
        localOrderDaf(T.tmp[T.stk], out, "1".toCharArray(), sv)
    } else if (g == ORDERNEXT_D) {
        localOrderDaf(T.tmp[T.stk], out, "1".toCharArray(), sv)
    } else if (g == ORDERPREV_D) {
        localOrderDaf(T.tmp[T.stk], out, "-1".toCharArray(), sv)
    } else return 0

    return 1
}
