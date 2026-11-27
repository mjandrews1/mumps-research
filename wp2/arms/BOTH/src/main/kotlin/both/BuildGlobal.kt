package both

// Unit 232 render: interp-parse.cpp::BuildGlobal (roster 232,
// batch 8, 30 LOC). int BuildGlobal(int code, int g, unsigned
// char * str, unsigned char * rslt, struct MSV * svPtr) - the
// global order-builder stack: the record 231 BuildLocal twin
// modulo the dispatch callee (GlobalOrder over LocalOrder) and
// the TU (interp-parse.cpp over interp-sym.cpp; a SEPARATE
// static stack - the two statics are distinct in the vendor).
//
// Consults (rule base wp2/arms/BOTH/rules_v0.pl):
//   h014 out_of_scope_call - reused: sigint(100) direct; NEXT
//     direct (GlobalData:46); ORDERNEXT/ORDERPREV direct (the
//     CompilerParseDefs ledger, record 219); GlobalOrder is a
//     NEW TODO stub raised here (owner libmpscpp.cpp::
//     GlobalOrder, the libmpscpp.h prototype family).
//   h015 grammar-collision - reused: ASCII only, no backticks,
//     no raw comment dollars, trailing newline.
//   h017 naming - reused: BuildGlobal keeps vendor name/case;
//     the file-level statics carry the bg- prefix (the record
//     231 bl- twins; no package-scope collision).
//   h019 class_member_carrier - reused: the TU-static stack
//     rides file-level privates, distinct from BuildLocal's
//     (kept - two stacks, two seats).
//
// Quirks kept (ledgered):
//   - the "// code 1" comment sits between the code 2 block and
//     the fall-through (the default IS code 1 - kept in the
//     header note).
//   - the overflow printf + sigint(100) pair still pushes after
//     (the 231 twin quirk).
//   - the invalid-g return 0 has already popped (kept).
//   - the GlobalNext commented-out calls in the vendor
//     (//GlobalNext(tmp,rslt,svPtr); the LocalOrder dispatch
//     twin comments) stay ledgered here.

// void GlobalOrder(unsigned char *, unsigned char *,
//                  unsigned char *, struct MSV *) - owner:
// include/mumpsc/globalOrder.h::GlobalOrder (unit 259, batch 9;
// the B8 raise named the libmpscpp.h prototype family). LANDED:
// the h014 delegate-when-owner-lands flip - the body lives in
// GlobalOrderH.kt, this seat removed (call sites bind
// unchanged; the stub's tmp2 was non-null CharArray, the
// owner face widens it to nullable per the vendor NULL arm).

private val bgTmp = Array(TSTACK) { CharArray(STR_MAX) } // static tmp
private var bgStk = 0 // static int stk

// int BuildGlobal(int, int, unsigned char *, unsigned char *,
//                 struct MSV *) - interp-parse.cpp
internal fun BuildGlobal(
    code: Int, g: Int, str: CharArray, rslt: CharArray, svPtr: Msv,
): Int {
    if (code < 0) {
        bgStk = 0
        return 1
    }

    if (code == 0) {
        if (bgStk >= TSTACK) {
            print("*** Build Stack Overflow " + svPtr.LineNumber + "\n\n")
            sigint(100)
        }
        writeC(bgTmp[bgStk++], cStringOf(str))
        return 1
    }

    if (code == 2) {
        if (bgStk == 0) {
            rslt[0] = '\u0000'
        } else {
            writeC(rslt, cStringOf(bgTmp[--bgStk]))
        }
        return 1
    }

    // code 1

    bgStk--

    if (g == NEXT) {
        GlobalOrder(bgTmp[bgStk], rslt, charArrayOf('1', '\u0000'), svPtr)
    } else if (g == CompilerParseDefs.ORDERNEXT) {
        GlobalOrder(bgTmp[bgStk], rslt, charArrayOf('1', '\u0000'), svPtr)
    } else if (g == CompilerParseDefs.ORDERPREV) {
        GlobalOrder(bgTmp[bgStk], rslt, charArrayOf('-', '1', '\u0000'), svPtr)
    } else {
        return 0
    }

    return 1
}
