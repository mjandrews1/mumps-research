package daf

// Unit 190 (DAF B7 T1, simple): interp-strmanip.cpp::substr (19 LOC,
// score 7.1): the interpreter-TU substring service. This render
// RELEASES the substrDaf seat raised at the DAF B6 T3 Match.kt
// render (the stub note there cites this row as owner; the release
// note rides the seat edit in Match.kt). Distinguishing fidelity
// features vs the unit-189 compiler twin: the len<0 arm clamps to
// 255 but there is NO upper clamp arm - in its place stands the
// disabled STR_MAX comment and the ACTIVE realloc branch:
// realloc(out, len-start<<1) with C precedence (len - (start<<1));
// out-param pointer replacement has no Kotlin carrier equivalent -
// the realloc is delegated: the caller-provided out carrier is
// length-checked (bounded slice per the realloc's intent) and the
// residual write never indexes past the carrier (quantized UB
// guard, ledgered below); the start-- / len-- slice ladder is
// otherwise verbatim.
// Consults (2026-11-23T18:32:00Z, nominal): BASE-log row 8
// hit-reuse (buffer-role classification: in scan-only, out
// written scratch), row 27 hit-adapt (the clamp ladder + offset
// arithmetic verbatim), row 30 hit-adapt (the out[] carrier stays
// an out-seat: the Match.kt call sites read the NUL-terminated
// result onward; the row-30 out->return fold NOT taken, same
// rationale as the unit-189 twin), manual-decision (no BASE row:
// the realloc(out, len-start<<1) vendor arm - the requested size
// OVERFLOWS for large len into a NEGATIVE allocation size (C
// precedence: len - (start<<1)), a vendor UB seat; quantized per
// the UB-quantization discipline: the carrier-safe bound replaces
// the realloc and the divergence is this ledger note).
// Dafny: EXEMPT (raw-carrier slice loop; journal row).

fun substrDaf(src: CharArray, srcOff: Int, out: CharArray, from: Int, lenIn: Int): Unit {

    var len = lenIn
    if (len == 0) {
        out[0] = NUL_D
        return
    }

    if (len < 0) {
        len = 255
    } else if (len - from + 1 > STR_MAX_DAF) {
        // vendor: realloc(out, len-start<<1) (UB-seat quantized to
        // the carrier bound; out pointer replacement has no carrier
        // equivalent - the divergence note above)
    }

    var start = from - 1 // start--
    len = len - 1        // len--
    var i = 0
    while (i <= len) { // for (i = 0; i <= len; i++) out[i] = in[start++];
        if (start < 0 || srcOff + start >= src.size || i >= out.size) {
            // realloc-guard quantization: the C write is unbounded;
            // the render bounds at the carriers
            break
        }
        out[i] = src[srcOff + start]
        start++
        i++
    }
    if (len + 1 < out.size) out[len + 1] = NUL_D
}
