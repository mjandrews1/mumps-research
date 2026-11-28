package daf

// R1 stub-completion (2026-11-28, C-020 erratum session, tranche 6):
// interp-sym.cpp::_find (roster row 27, loc 15, simple): the
// substring finder of the \u0024find service: begin parses as
// the 0-floored start offset; a non-empty key runs xindex from
// that offset and, on a hit (position > 0), advances the
// offset past the key; an offset beyond strlen(in)+1 resets to
// 0; the result prints %d into the out carrier.
// Consults (2026-11-28T00:00:00Z, nominal): BASE-log row 28
// hit-reuse (atoi -> the DafCRuntimeT2 atoi seat); row 40
// hit-reuse (strlen -> the cstring fold); rows 81/56
// hit-adapt (the unsigned char *out out-param keeps the
// CharArray carrier of the three call sites - GlobalFind.kt,
// GlobalFindB7.kt, MstringFind.kt; sprintf "%d" renders as the
// Int.toString template); row 2 family hit-reuse (xindex
// resolves to the InterpStrmanipXindex render - the vendor
// xindex body lives at interp-strmanip.cpp:87, the same TU
// family _find declares against; called with the (short) i
// cast folded to the Int from-domain).
// Vendor quirks kept: (1) a negative begin floors to 0 (the
// atoi of a non-numeric begin gives 0); (2) the hit advance
// adds strlen(key) - the result is 1-based past the END of the
// match, not the match start (the vendor \u0024find semantics);
// (3) an EMPTY key leaves i at the begin offset (the xindex
// call is guarded by key[0] != 0) - the result is then begin
// itself unless beyond strlen+1; (4) the beyond-reset compares
// i > strlen(in) + 1 (an offset exactly one past the end is
// kept, not reset).
// Dafny: EXEMPT (string search over the index-family seats; the
// InterpStrmanipXindex reason; journal row).

fun findDaf(out: CharArray, inBuf: CharArray, key: CharArray,
        begin: CharArray): Unit {

    // i = atoi((char *) begin); if (i < 0) i = 0;
    var i = atoi(DafCRuntime.cstring(begin))
    if (i < 0) i = 0

    // if (key[0] != 0) { if ((i = xindex(in, key, (short) i)) > 0)
    //      i += strlen((char *) key); }
    if (key[0] != NUL_D) {
        val x = xindexDaf(inBuf, 0, key, 0, i)
        if (x > 0) {
            i = x + DafCRuntime.cstring(key).length
        }
    }

    // if (i > strlen((char *) in) + 1) i = 0;
    if (i > DafCRuntime.cstring(inBuf).length + 1) i = 0

    // sprintf((char *) out, "%d", i);
    val r = i.toString()
    var w = 0
    while (w < r.length) {
        out[w] = r[w]
        w++
    }
    out[w] = NUL_D
}
