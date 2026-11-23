package daf

// Unit 195 (DAF B7 T1, simple): interp-sym.cpp::mkpt (16 LOC, score
// 9.0): the non-printable-byte cleanup helper of the symbol TU:
// walk the NUL-terminated input, keep the printable range 32..128,
// substitute '-' for every byte below 32, then the three
// code-point-specific re-substitutions IN VENDOR ORDER (0x1f ->
// 'F', 0x1e -> 'E', 0x01 -> '.'), NUL-terminate and return.
// Consults (2026-11-23T18:34:30Z, nominal): BASE-log row 56
// hit-reuse (the static return buffer buf[2048] folds onto a
// FRESH per-call String: no C caller aliases the static across
// calls as a write target - the row-56/112 family fresh-per-call
// discipline, divergence ledgered: the C static persists across
// calls and any aliasing caller would observe the prior call's
// bytes through the returned pointer; the callers of this TU pass
// the result straight to print-family reads, so the fold is
// byte-observable-equivalent), row 27 hit-adapt (the loop ladder
// kept verbatim: the three re-substitutions run AFTER the '-' write
// for the same byte, in file order (0x1f, 0x1e, 0x01 - each is
// below 32 so the '-' write happens FIRST and the specific
// substitution overwrites it; order kept)), manual-decision (no
// BASE row: bytes ABOVE 128 fall through every branch and the
// vendor leaves buf[i] UNWRITTEN (an uninitialized static read,
// UB at first call) - quantized per the UB discipline: the fresh
// per-call carrier writes the byte as NUL-tail (the fold note
// replaces garbage with the fresh-carrier default, ledgered), and
// the same >128 quirk of the unbounded strlen-vs-buf walk is
// bounded by the input length exactly as the C loop is bounded by
// the NUL.
// Dafny: EXEMPT (byte-map walk; journal row).

fun mkptDaf(str: CharArray): String {
    val buf = StringBuilder()
    var i = 0
    while (i < str.size && str[i] != NUL_D) { // for (i=0; str[i]!=0; i++)
        val c = str[i]
        if (c.code >= 32 && c.code <= 128) { // printable kept
            buf.append(c)
            i++
            continue
        }
        if (c.code < 32) {
            buf.append('-')      // if (str[i]<32) buf[i]='-';
            if (c.code == 0x1f) buf.setCharAt(buf.length - 1, 'F') // '\x1f'
            if (c.code == 0x1e) buf.setCharAt(buf.length - 1, 'E') // '\x1e'
            if (c.code == 0x01) buf.setCharAt(buf.length - 1, '.') // '\x01'
        }
        // bytes above 128: buf[i] left unwritten in the vendor (UB
        // quantized: omitted from the fresh carrier)
        i++
    }
    return buf.toString() // buf[i]=0; return buf - NUL implicit
}
