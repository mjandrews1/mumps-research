package both

// Unit 137 (BOTH B5 T2, core): libmpscpp.cpp::Transpose (23 LOC).
// Batch_plan ordinal 137: core, LOC 23, score 14.1. The file-level
// Transpose wrapper: parse the two coded global arguments (byte-1
// subscript markers), reject subscripted names (top-level globals
// only), and dispatch the member Transpose on the global carriers.
//
// Vendor behavior, args shaped "^name" + byte-1-coded subscripts:
// scan arg1 to the first byte-1 marker or the terminator; the
// no-marker arm writes the terminator back (a no-op); the marker arm
// prints the diagnostic and sigint(100)s; the marked tail is then
// truncated and the name past the caret constructs the global. Same
// for arg2 (scan index j), then dispatch a(tail).Transpose(b(tail))
// on the member carriers and return "".
//
// Quirks kept (ledgered):
//   - VENDOR COPY-PASTE BUG: the arg2 diagnostic tests arg2[i] with
//     i left over from the ARG1 scan, not the j just computed (and
//     Multiply repeats it twice). Kept exactly; i may be off arg2's
//     bounds, in which case the C read is UB - quantized here to a
//     no-diagnostic read (documented, not fixed).
//   - the "array name only" arms write argX[k+1]=0 past-the-end in
//     C: a no-op on the string content; not reproduced in Kotlin
//     (nothing observable changes), journaled.
//   - sigint(100) never returns in the vendor (process abort); the
//     stub throws, so the arg1[i]=0 truncation after the marker arm
//     is dead code in the vendor too - kept unreachable, journaled.
//   - the file-level char* return is "" (vendor): the static char
//     tmp[32] and double x are dead (h013) - dropped with comments.
//
// BOTH consults (find_rule before render, events logged):
//   h007 char_buffer - reused: the argument parses read scan-only
//     Strings; the static tmp seat is dropped under h013 instead.
//   h012 unsigned_byte_domain - reused: the byte-1 marker tests are
//     Char comparisons in the unsigned-byte domain.
//   h013 dead_store - reused: tmp/x dropped.
//   h014 out_of_scope_call - reused: sigint rides its roster-254
//     stub; the GlobalRef/GlobalNode carrier seats and the member
//     Transpose TODO seat live in BothStubs.kt.
//   h009 format_string - reused: the diagnostic literal is exact
//     ("\n*** Arg N must be top level global\n", System.out).
//   h017 naming - hit: Transpose->transpose member fold, journaled.
//   MISS class_member_carrier recurrence: the member dispatch rides
//     the carrier seats below (T1 recurrence; T2 +1).
internal fun Transpose(sv: Msv, ep: String, arg1: String, arg2: String): String {
    // static char tmp[32]; double x; - dead stores (h013), dropped.

    // --- arg 1 ---
    var i = 0
    while (i < arg1.length && arg1[i] != '\u0001') i++
    // if (arg2[j]==0 ... no: arg1[i]==0) - "array name only": the
    // vendor no-op write is not observable (see header).

    if (i < arg1.length && arg1[i] == '\u0001') {
        System.out.print("\n*** Arg 1 must be top level global\n")
        sigint(100) // vendor aborts here; stub throws
    }
    // arg1[i]=0; - unreachable after sigint in the vendor; the
    // truncation fold below covers the reachable path.

    val a = GlobalRef(arg1.substring(1, i.coerceAtMost(arg1.length))) // bypass ^ (arg1[i]=0 fold)

    // --- arg 2 ---
    var j = 0
    while (j < arg2.length && arg2[j] != '\u0001') j++

    // VENDOR BUG kept: tests arg2[i], not arg2[j] (i from arg1's scan).
    if (i < arg2.length && arg2[i] == '\u0001') {
        System.out.print("\n*** Arg 2 must be top level global\n")
        sigint(100)
    }

    val b = GlobalRef(arg2.substring(1, j.coerceAtMost(arg2.length)))

    // a(&arg1[i+1]).Transpose(b(&arg2[j+1]));
    val aTail = if (i + 1 <= arg1.length) arg1.substring(i + 1) else ""
    val bTail = if (j + 1 <= arg2.length) arg2.substring(j + 1) else ""
    a(aTail).transpose(b(bTail))

    return ""
}
