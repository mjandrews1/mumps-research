package both

// Unit 231 render: interp-sym.cpp::BuildLocal (roster 231,
// batch 8, 30 LOC). int BuildLocal(int code, int g, unsigned
// char * str, unsigned char * rslt, struct MSV * svPtr) - the
// local-symbol order-builder stack: code<0 resets, code 0
// pushes, code 2 pops, the default pops-and-orders (the
// LocalOrder dispatch on NEXT/ORDERNEXT/ORDERPREV).
//
// Consults (rule base wp2/arms/BOTH/rules_v0.pl):
//   h014 out_of_scope_call - reused: sigint(100) direct (the
//     in-band abort); LocalOrder is a NEW TODO stub raised here
//     (owner interp-sysfunc.cpp::LocalOrder, sysfunc.h:46 pin,
//     batch 9/10 pending); NEXT direct (the GlobalData:46 seat);
//     ORDERNEXT/ORDERPREV direct (the CompilerParseDefs ledger,
//     record 219).
//   h015 grammar-collision - reused: ASCII only, no backticks,
//     no raw comment dollars, trailing newline.
//   h017 naming - reused: BuildLocal keeps vendor name/case
//     (the zfcns.h commented-prototype ledger, record 241, is
//     this unit's header twin - the record 232 GlobalOrder
//     family is distinct).
//   h019 class_member_carrier - reused: the TU-static stack
//     (static unsigned char tmp[TSTACK][STR_MAX], static int
//     stk) rides file-level privates; svPtr is the param.
//
// Quirks kept (ledgered):
//   - the overflow printf fires BEFORE sigint(100) and the push
//     STILL proceeds after (the vendor does not return on the
//     overflow - kept).
//   - the default path decrements stk BEFORE the dispatch, so
//     the invalid-g return 0 has ALREADY popped (kept).
//   - the code 2 pop reads tmp[--stk] (the pre-decrement
//     shape; the code 0 push is tmp[stk++] - post-increment).
//   - the dead "fff" debug printf line stays out (ledgered;
//     the vendor comment-out kept as a note).

// void LocalOrder(unsigned char *, unsigned char *,
//                 unsigned char *, struct MSV *) - owner:
// interp-sysfunc.cpp::LocalOrder (sysfunc.h:46). TODO-stub
// (h014; the BuildLocal dispatch seat).
internal fun LocalOrder(
    tmp0: CharArray, tmp1: CharArray, tmp2: CharArray, sv: Msv,
) {
    TODO("stub: owner interp-sysfunc.cpp::LocalOrder (sysfunc.h:46, batch 9/10 pending)")
}

private val blTmp = Array(TSTACK) { CharArray(STR_MAX) } // static tmp
private var blStk = 0 // static int stk

// int BuildLocal(int, int, unsigned char *, unsigned char *,
//                struct MSV *) - interp-sym.cpp
internal fun BuildLocal(
    code: Int, g: Int, str: CharArray, rslt: CharArray, svPtr: Msv,
): Int {
    if (code < 0) {
        blStk = 0
        return 1
    }

    if (code == 0) {
        if (blStk >= TSTACK) {
            print("*** Build Stack Overflow " + svPtr.LineNumber + "\n\n")
            sigint(100)
        }
        writeC(blTmp[blStk++], cStringOf(str))
        return 1
    }

    if (code == 2) {
        if (blStk == 0) {
            rslt[0] = '\u0000'
        } else {
            writeC(rslt, cStringOf(blTmp[--blStk]))
        }
        return 1
    }

    blStk--

    if (g == NEXT) {
        LocalOrder(blTmp[blStk], rslt, charArrayOf('1', '\u0000'), svPtr)
    } else if (g == CompilerParseDefs.ORDERNEXT) {
        LocalOrder(blTmp[blStk], rslt, charArrayOf('1', '\u0000'), svPtr)
    } else if (g == CompilerParseDefs.ORDERPREV) {
        LocalOrder(blTmp[blStk], rslt, charArrayOf('-', '1', '\u0000'), svPtr)
    } else {
        return 0
    }

    return 1
}
