package both

// Unit 130 (BOTH B5 T1, simple): libmpscpp.cpp::Avg (17 LOC). Batch_plan
// ordinal 130: simple, score 8.1. The free (char*) wrapper of the \$avg
// service entry: parse the coded argument (byte 1 = subscript
// separator; vendor trailing 1,1-pair strip), split name/subscript,
// then the subscripted global-node Avg() invoke; the sprintf(tmp,"%g",x)
// over the static char tmp[32] return becomes a fresh String.
//
// BOTH consults (find_rule before render, events logged):
//   h008 static_scratch - reused: static char tmp[32] -> fresh String
//     per call (no caller aliases the buffer across calls).
//   h009 format_string - reused: "%g" via the Locale.US formatter seat
//     (fmtG in BothNumRuntime.kt), exact digit shape kept.
//   h012 unsigned_byte_domain - reused: the coded-argument comparisons
//     (arg1[i]==1 etc.) stay in the Char byte domain.
//   h014 out_of_scope_call - reused: the global-class carrier
//     (GlobalRef/GlobalNode, BothStubs.kt) seats name the layout owner
//     (roster 89) and the avg() service owner (roster 301); the Msv
//     carrier seats the stateVector owner (roster 172).
//   h017 naming - reused (arg1/ep keep vendor names; the fun keeps the
//     vendor name Avg).
//   Dafny: exempt (the unit's core is the stubbed global-node service
//     plus the %g round-trip; no decidable numeric core this batch).
//
// Vendor quirks kept (ledger):
//   - the trailing 1,1 strip reads arg1[i-2] only when arg1[i-1]==1;
//     at strlen==1 with arg1[0]==1 the vendor reads arg1[-1] (outside
//     the buffer). Guarded here (i >= 2 required); divergence noted.
//   - the same GLOBAL_NOT_FOUND code semantics delegate to the global
//     machinery (owner seats below).

internal fun Avg(svptr: Msv, ep: String, arg1: String): String {
    val a = arg1.toCharArray()
    var len = a.size // C: i = strlen(arg1)

    // C: if (i>0 && arg1[i-1]==1 && arg1[i-2]==1) arg1[i-2]=0;
    if (len >= 2 && a[len - 1] == 1.toChar() && a[len - 2] == 1.toChar()) {
        a[len - 2] = 0.toChar()
        len -= 2
    }

    // C: for (i=0; arg1[i]!=0; i++) if (arg1[i]==1) break;
    //    j = arg1[i]; arg1[i]=0;
    var k = 0
    while (k < len && a[k] != 0.toChar() && a[k] != 1.toChar()) k++
    val j = if (k < len) a[k] else 0.toChar()
    if (k < len) a[k] = 0.toChar()

    // C: global a(&arg1[1]); // bypass ^
    val node = GlobalRef(String(a, 1, (k - 1).coerceAtLeast(0)))

    // C: if (!j) x = a("").Avg(); else x = a(&arg1[i+1]).Avg();
    val x: Double = if (j == 0.toChar()) {
        node("").avg()
    } else {
        node(String(a, k + 1, (len - k - 1).coerceAtLeast(0))).avg()
    }

    // static char tmp[32] return -> fresh String (h008)
    return fmtG(x)
}
