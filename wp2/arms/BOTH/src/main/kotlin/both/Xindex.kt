package both

// Unit 260 (BOTH B9, core): interp-strmanip.cpp::xindex (25
// LOC). Batch_plan ordinal 260; units.csv row 264. The \\u0024x
// index kernel (1-based): scan source from the start position
// for the first full pattern match, return the 1-based index
// or 0. Pure byte-domain search, no seats beyond the carriers.
//
// BOTH consults (find_rule before render, events logged):
//   h012 unsigned_byte_domain - reused: source/ptrn stay
//     CharArrays in the C byte domain; the NUL-terminated
//     strlen scans and the byte comparisons are exact.
//   h016 comma_operator - consulted, no trigger (no comma
//     operators in the body; the for(;;) empty bodies are the
//     strlen idiom, not comma chains).
//   h017 naming - reused: xindex keeps the vendor name;
//     flg/i/j/k/l/istrt/strt keep vendor names.
//   Dafny: EXEMPT per the emitter/procedural family precedent
//     (the DAF B6 T3 ruling; the loop-nest shape is the same
//     family as the batch-8 renders) - no new spec.
//
// Vendor quirks kept (ledgered):
//   - the empty-pattern case (ptrn[0]==0 -> i=-1) runs the
//     inner loop zero times, so the FIRST k returns k+1:
//     the vendor returns istrt+1 for a nonempty source with
//     an empty pattern; kept verbatim (no special case).
//   - short arithmetic rides the vendor's short domain (Int in
//     Kotlin; the 16-bit overflow seat is unreachable at
//     STR_MAX scale, journaled quantization).
//   - istrt = strt - 1 with the strt <= 1 clamp to 0 kept in
//     the vendor order (istrt is computed, then clamped).

internal fun xindex(source: CharArray, ptrn: CharArray, strt: Int): Int {
    var flg = 0
    var i = -1
    var j = -1

    // for (i = 0; ptrn[i] != 0; i++); --i;
    i = 0
    while (ptrn[i] != 0.toChar()) i++
    i--

    // for (j = 0; source[j] != 0; j++); --j;
    j = 0
    while (source[j] != 0.toChar()) j++
    j--

    // istrt = strt - 1; if (strt <= 1) istrt = 0;
    var istrt = strt - 1
    if (strt <= 1) istrt = 0

    // for (k = istrt; k <= j; k++) { inner scan; }
    var k = istrt
    while (k <= j) {
        var l = 0
        while (l <= i) {
            if (ptrn[l] != source[k + l]) {
                flg = 1
                break
            }
            l++
        }

        if (flg == 1) {
            flg = 0
            k++
            continue
        }

        return k + 1
    }
    return 0
}
