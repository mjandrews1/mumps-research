package daf

// Unit 230 (DAF B8 T2, core): interp-parse.cpp::BuildGlobal
// (27 LOC, score 16.1): the global build-stack helper of the
// parser MERGE/order walk: the three-arm code dispatch over the
// function-static TSTACK x STR_MAX scratch and stk counter (vendor
// interp-parse.cpp:1836, physically verified this session). SEAT
// RELEASE: the buildGlobalDaf named stub of DafInterpStubsT3 is
// released - the real body lands here under the stub's own name
// and signature (the Fcn.kt call sites resolve unchanged), the
// stub is deleted.
// Consults (2026-11-24T21:17:00Z, nominal): code<0 resets stk
// (returns 1), code==0 pushes str (the overflow banner prints
// svPtr.LineNumber and calls sigintD(100), the unit-211 real, the
// pinned 100..133 codes), code==2 pops into rslt (the empty write
// rslt[0]=0 when the stack is empty), and the trailing code-1 arm
// decrements stk and hands the walk to the four-arg globalOrderDaf
// stub seat (owner libmpscpp.cpp::GlobalOrder, roster b2/b3/b9 -
// the roster family the unit-31 ledger cites; this render is
// another in-arm caller, the seat goes home unchanged): the
// NEXT/ORDERNEXT branches pass the "1" direction, ORDERPREV the
// "-1" one; other g values return 0 per vendor. The function-
// statics render as the holder object BuildGlobalStateD (the
// ZfcnsState holder precedent; separate state from the unit-231
// BuildLocal twin - the vendor keeps two independent static
// stacks). The vendor's // code 1 comment kept verbatim.
// Dafny: EXEMPT (stack dispatch over order seats; journal row).

private const val TSTACK_BG_D = 10 // #define TSTACK 10 (interp.h ledger)

private object BuildGlobalStateD {
    // static unsigned char tmp[TSTACK][STR_MAX]
    val tmp = Array(TSTACK_BG_D) { CharArray(STR_MAX_DAF) }
    // static int stk
    var stk = 0
}

fun buildGlobalDaf(code: Int, g: Int, a: CharArray, out: CharArray,
        sv: DafMsv): Int {
    val T = BuildGlobalStateD

    if (code < 0) {
        T.stk = 0
        return 1
    }

    if (code == 0) {

        if (T.stk >= TSTACK_BG_D) {
            System.out.print("*** Build Stack Overflow " + sv.LineNumber + "\n\n")
            sigintD(100)
        }

        // strcpy((char *) tmp[stk++],(const char *) str)
        val src = a
        var q = 0
        while (src[q] != NUL_D) {
            T.tmp[T.stk][q] = src[q]
            q++
        }
        T.tmp[T.stk][q] = NUL_D
        T.stk++
        return 1
    }

    if (code == 2) {
        if (T.stk == 0) {
            out[0] = NUL_D // if (stk==0) rslt[0]=0;
        } else {
            // else strcpy((char *) rslt,(const char *) tmp[--stk])
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

    // code 1
    T.stk--

    if (g == NEXT_D) { // GlobalOrder(tmp[stk],rslt, "1", svPtr)
        globalOrderDaf(T.tmp[T.stk], out, "1".toCharArray(), sv)
    } else if (g == ORDERNEXT_D) {
        globalOrderDaf(T.tmp[T.stk], out, "1".toCharArray(), sv)
    } else if (g == ORDERPREV_D) {
        globalOrderDaf(T.tmp[T.stk], out, "-1".toCharArray(), sv)
    } else return 0

    return 1
}
