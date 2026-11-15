package mumps.mdh

/**
 * Kotlin translation of libmpscpp.cpp::GlobalOrder (Study Subset unit 39,
 * Batch 2, arm BASE; translated 2026-11-15).
 *
 * C: char * GlobalOrder(char * a, int i) (libmpscpp.cpp:2538-2556) —
 * $ORDER on a (possibly global) subscript reference: builds the one-line
 * MUMPS "s %=$order(a)" and XECUTEs it, reading the result back from
 * the local variable % via SymGet.
 *
 * QUIRK PRESERVED: the parameter i is UNUSED in all three C++ overloads
 * (char * / mstring / string, 2538-2600) — the command always uses the
 * forward-direction $order(a) regardless of i. The parameter is kept
 * in the Kotlin signature and flagged here rather than silently
 * dropped (faithful-bugs policy).
 *
 * OVERLOAD SET COLLAPSE (heuristic 93): the three C++ overloads differ
 * only in string/return types (char* with a static r[STR_MAX]; mstring;
 * string with a static rr) — all collapse to a single Kotlin String
 * entry point with String locals; the C statics become plain locals
 * (no cross-call persistence is observable: r/rr are fully overwritten
 * before each return).
 *
 * The "s %=$order(" literal contains MUMPS dollars — escaped per
 * heuristic 89. svPtr->tpx is set from the xecute outcome (MsvState
 * tpx: Long; one-owner unit 8 via MdhRuntime.svPtr).
 */

/** C: int xecute(char *) (libmpscpp.cpp:7073) — one-line XECUTE; body
 *  outside batch scope. */
fun xecute(exp: String): Int =
    TODO("xecute not yet translated (lives in libmpscpp.cpp:7073, outside batch scope)")

/** C: mstring SymGet(char *) (libmpscpp.cpp:9858) — symbol-table read;
 *  body outside batch scope. */
fun symGet(sym: String): String =
    TODO("SymGet not yet translated (lives in libmpscpp.cpp:9858, outside batch scope)")

/** C: char * GlobalOrder(char * a, int i) */
fun globalOrder(a: String, i: Int): String {
    // C: char tmp[STR_MAX]="s %=$order("; strcat(tmp,a); strcat(tmp,")");
    val tmp = "s %=\$order($a)"

    if (xecute(tmp) == 0) {
        MdhRuntime.svPtr.tpx = 1 // C: svPtr->tpx=1;
        // C: t=SymGet((char *) "%"); strcpy(r,t.c_str()); return r;
        return symGet("%")
    }

    MdhRuntime.svPtr.tpx = 0 // C: svPtr->tpx=0;
    return "" // C: strcpy(r,""); return r;
}
