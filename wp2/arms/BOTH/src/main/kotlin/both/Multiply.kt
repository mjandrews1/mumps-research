package both

// Unit 138 (BOTH B5 T2, core): libmpscpp.cpp::Multiply (28 LOC;
// batch_plan bullet says 26, units.csv and the vendor extraction
// say 28 - batch_close adjudication note). Batch_plan ordinal 138:
// core, score 21.4. The file-level Multiply wrapper: three coded
// global arguments, top-level-only checks, then the member
// a.Multiply(b,c) dispatch.
//
// Vendor behavior: same coded-appearance parse as unit 137 per
// argument (arg1 index i, arg2 index j, arg3 index k - all marker
// scans); each marked arm prints its diagnostic and sigint(100)s;
// the names past the carets construct the globals; the vendor then
// calls a.Multiply(b,c) - the subscripted form lives as a comment.
//
// Quirks kept (ledgered):
//   - VENDOR COPY-PASTE BUG (x2): the arg2 and arg3 diagnostics test
//     arg2[i]/arg3[i] with the ARG1 scan index i, not the j/k just
//     computed. Kept exactly; out-of-range reads are quantized to
//     no-diagnostic (documented, not fixed).
//   - the commented-out subscripted dispatch
//     (a(&arg1[i+1]).Multiply(...)) stays a comment, verbatim.
//   - NOTE: unlike 137, this vendor body has NO "array name only"
//     no-op writes (the loop is the scan alone); missing-marker
//     args pass (j+1)/k-tail truncation only at the scans.
//   - static char tmp[32] and double x are dead (h013), dropped.
//   - svptr/ep unused in the vendor, parameters kept.
//
// BOTH consults (find_rule before render, events logged):
//   h007 char_buffer - reused / h012 unsigned_byte_domain - reused /
//     h013 dead_store - reused / h014 out_of_scope_call - reused /
//     h009 format_string - reused / h017 naming - hit: the rule set
//     mirrors unit 137 (same vendor kernel); see Transpose.kt for
//     the per-rule outcome lines and the events CSV for this unit's
//     rows.
//   MISS class_member_carrier recurrence: the a.Multiply(b,c) seat
//     rides GlobalRef.multiply (owner roster 213 batch 2, pending).
internal fun Multiply(sv: Msv, ep: String, arg1: String, arg2: String, arg3: String): String {
    // static char tmp[32]; double x; - dead stores (h013), dropped.

    // --- arg 1 (index i) ---
    var i = 0
    while (i < arg1.length && arg1[i] != '\u0001') i++
    if (i < arg1.length && arg1[i] == '\u0001') {
        System.out.print("\n*** Arg 1 must be top level global\n")
        sigint(100)
    }
    // arg1[i]=0; - unreachable after sigint in the vendor (truncation
    // folded into the substring below for the reachable path).
    val a = GlobalRef(arg1.substring(1, i.coerceAtMost(arg1.length))) // bypass ^

    // --- arg 2 (index j; VENDOR BUG: diagnostic tests arg2[i]) ---
    var j = 0
    while (j < arg2.length && arg2[j] != '\u0001') j++
    if (i < arg2.length && arg2[i] == '\u0001') {
        System.out.print("\n*** Arg 2 must be top level global\n")
        sigint(100)
    }
    val b = GlobalRef(arg2.substring(1, j.coerceAtMost(arg2.length)))

    // --- arg 3 (index k; VENDOR BUG: diagnostic tests arg3[i]) ---
    var k = 0
    while (k < arg3.length && arg3[k] != '\u0001') k++
    if (i < arg3.length && arg3[i] == '\u0001') {
        System.out.print("\n*** Arg 3 must be top level global\n")
        sigint(100)
    }
    val c = GlobalRef(arg3.substring(1, k.coerceAtMost(arg3.length)))

    // a(&arg1[i+1]).Multiply(b(&arg2[j+1]),c(&arg3[k+1])); - vendor
    // comment kept; the live call is the un-subscripted form:
    a.multiply(b, c)

    return ""
}
