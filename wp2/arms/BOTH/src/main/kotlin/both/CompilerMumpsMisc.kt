package both

// Unit 195 render: compiler-mumps.c::<misc> (roster 195,
// batch 7, 57 LOC). The compiler-mumps TU residue rendered
// here: EP (the subroutine entry-point router, :1978) and
// LevCheck (:1997). The remaining compiler-mumps.c material
// (main at :1372-scale) is the roster-303/304 batch-10 unit,
// NOT this residue.
//
// Consults (rule base wp2/arms/BOTH/rules_v0.pl):
//   h014 out_of_scope_call - reused: epi called direct (the
//     roster-188 render); the roster-191 EPZb TODO stub
//     RELEASED at this render (release note in Zcommands.kt;
//     its two call sites there now call EP direct). No other
//     callables.
//   h015 grammar-collision - reused: ASCII only, no backticks,
//     no raw comment dollars, trailing newline.
//   h017 naming - reused: EP and LevCheck keep vendor names
//     and case (no collisions).
//   h019 class_member_carrier - reused: the p1/LabelsStart
//     label-chain walk rides the CompilerGlobalsExt seats
//     (unit-151 owners); MAIN_flg the :51 seat.
//   h020 pointer_handle - reused: the p1 chain walk is the
//     established next-hop pattern (the epi render precedent).
//
// Quirks kept (ledgered):
//   - EP's 'offset' is EMITTED TEXT, not a seat: the generated
//     program's local - the arm emits the emission verbatim.
//   - the flg!=0 arm's ErrorMessage emission is text (the
//     generated program's call, not the arm's seat).
//   - EP resets MAIN_flg after epi regardless of flg (the
//     fall-through shape is exact: the epi + reset run in both
//     arms).
//   - LevCheck returns 0 on match, 1 on no match (the inverted
//     boolean convention kept).

// fprintf(Out, ...) fold
private fun epmOutZb(s: String) {
    try {
        CompilerOutState.out.write(s)
    } catch (e: java.io.IOException) {
        // vendor ignores the fprintf return (failure is silent)
    }
}

// void EP(int flg, short ExecName, long pid) - the subroutine
// entry point router, compiler-mumps.c:1978
internal fun EP(flg: Int, ExecName: Short, pid: Long) {
    val g = CompilerGlobalsExt
    if (flg != 0) {
        epmOutZb("\tgoto _epilogue;\n")
        epmOutZb("_eps:\tif (strlen((const char *) offset)==0) goto _fcn;\n")
        if (g.LabelsStart != null) {
            g.p1 = g.LabelsStart
            while (g.p1 != null) {
                epmOutZb(
                    "\tif (strcmp((const char *) offset,(const char *) \"" +
                        g.p1!!.name + "\")==0) goto " + g.p1!!.name + ";\n",
                )
                g.p1 = g.p1!!.next
            }
        }
        epmOutZb("\tErrorMessage(\"Error - entry point label not found\", svPtr->LineNumber);\n")
    }
    epi(ExecName, pid) // terminate prev function
    g.MAIN_flg = 0.toShort() // not in main function
}

// int LevCheck(short ExecName, short DoRetX, short DoRet[]) -
// compiler-mumps.c:1997
internal fun LevCheck(ExecName: Short, DoRetX: Short, DoRet: ShortArray): Int {
    for (i in 0 until DoRetX.toInt()) {
        if (DoRet[i] == ExecName) return 0
    }
    return 1
}
