package daf

/*
 * DAF batch-9 unit 251 (roster row 251; libmpscpp.cpp::GlobalOrder,
 * simple, LOC 15, score 8.1). Consult 2026-11-24T22:32:00Z.
 *
 * mstring GlobalOrder(mstring a, int i): the free wrapper-library
 * member that runs the sym engine's \u0024order over a global through
 * the XECUTE probe, exactly the GlobalGet(mstring) family shape of
 * the unit-191 render (LibmpscppMisc5). The char* form of the row
 * and the globalOrder.h::GlobalOrder twin are the unit-259 kin
 * (the header twin renders next); the unused i parameter is kept
 * verbatim (vendor signature; every overload carries it).
 */

fun GlobalOrderD(a: String, i: Int): String {
    // char tmp[STR_MAX]="s %=\u0024order("; strcat(tmp,a); strcat(tmp,")")
    val tmp = "s %=\u0024order(" + a + ")"
    val rr: String
    if (xecuteDaf(tmp) == 0) {
        DafInterpState.svPtr.tpx = 1
        rr = SymGetD("%")
    } else {
        DafInterpState.svPtr.tpx = 0
        rr = ""
    }
    return rr
}
