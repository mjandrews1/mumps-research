package both

// Unit 171 (BOTH B6 T2, core): libmpscpp.cpp::global::Max (26
// LOC). Batch_plan ordinal 171: core, LOC 26, score 14.8,
// complexity rank 144. Vendor body libmpscpp.cpp:1651-1682. The
// roster row is the member global::Max() of the Max/Min family
// (the -next Max twins at 1684+ and unit-134's global::Minus-era
// seats are separate corpus rows); h019 seats this as the
// package-level fold globalMax with the receiver state explicit
// (the globalLength precedent).
//
// Vendor behavior: the ref key is copied; a trailing pair of
// <32 control bytes (the coded-subscript tail) is stripped (the
// pre-decrement seat); the XNEXT walk mutates the key forward;
// each next key still matching ref over the FIRST i bytes stays
// in the family; get_global retrieves the value (the tpx fold:
// a 0 read sets GlobalExceptionCode=GLOBAL_NOT_FOUND and throws
// MumpsGlobalException); the FIRST value initializes max (flg
// arm), the rest keep max only when val > max; the walk ends at
// XNEXT 0 or the family break; the return is max.
//
// Quirks kept (ledgered):
//   - the strip reads tmp[i-2]/tmp[i-1] with no i>=2 guard (the
//     C index walks out of bounds for a ref shorter than 2
//     bytes); quantized with the length>=2 guard (the unit-130/
//     136 Avg/globalSum precedent), the divergence journaled.
//   - max=0.0 is a DEAD initializer: flg makes the first walked
//     value the max even when every value is negative (h013
//     dead-store scan; the fold keeps the flg arm).
//   - an empty family (no XNEXT children) returns the vendor
//     0.0 residue verbatim.
//
// BOTH consults (find_rule before render, events logged):
//   h007 char_buffer - reused and hit: the key mutation scratch
//     (tmp) rides the CharArray XNEXT carrier (the unit-136
//     globalSum seat shape); the value scratch folds via
//     cStringOf.
//   h014 out_of_scope_call - reused and hit: the XNEXT walk and
//     the tpx/exception idiom ride the B6-T2-flipped Mglobal
//     seat (unit 167) and strtodC (BothNumRuntime).
//   h019 class_member_carrier - reused and hit: member of the
//     global class (layout owner roster 89, untranslated in
//     this arm); receiver ref passed explicitly, the fold named
//     globalMax keeping the vendor member name.

internal fun globalMax(ref: String): Double {
    var tmp = ref
    var i = tmp.length

    // if (tmp[i-1] < 32 && tmp[i-2] < 32) tmp[--i] = 0; (i>=2 guard)
    if (i >= 2 && tmp[i - 1] < ' ' && tmp[i - 2] < ' ') {
        tmp = tmp.substring(0, i - 1)
        i--
    }

    var max = 0.0 // dead init (flg arm makes the first walk value max)
    var flg = 1
    val key = CharArray(STR_MAX) // the XNEXT-mutated scratch (h007)
    val ttt = CharArray(STR_MAX)
    writeC(key, tmp)

    while (true) {
        if (Mglobal(XNEXT, key, ttt, svPtr) == 0) break

        tmp = cStringOf(key) // the rewritten key

        if (strncmpC(ref, tmp, i) != 0) break

        val rslt = CharArray(STR_MAX)
        val f = Mglobal(RETRIEVE, tmp.toCharArray(), rslt, svPtr)
        svPtr.tpx = f.toLong()

        if (f == 0) {
            GlobalExceptionCode = GLOBAL_NOT_FOUND
            throw MumpsGlobalException()
        }

        val v = strtodC(cStringOf(rslt))

        if (flg != 0) { // the first walked value initializes max
            max = v
            flg = 0
            continue
        }

        if (v > max) max = v
    }

    return max
}
