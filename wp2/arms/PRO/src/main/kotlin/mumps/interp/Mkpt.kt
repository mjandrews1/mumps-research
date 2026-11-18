package mumps.interp

/**
 * PRO-arm translation of interp-sym.cpp::mkpt (B7 unit 207, rank
 * 9.0? - units.csv data row 207: score 9.0, complexity_rank 82,
 * simple stratum, loc 16; vendor interp-sym.cpp:970-985, declared
 * at :185). No vendor callers exist outside interp-sym.cpp (the
 * prototype at :185 is the only other mention); rendered as a
 * standalone byte-printable helper per the roster.
 *
 * Vendor shape: mkpt(str) walks the NUL-terminated str and
 * copies each byte into a static 2048-byte buf, mapping bytes
 * that are not printable ASCII: <32 becomes '-', and three
 * specific control bytes are re-mapped after that (\\x1f -> 'F',
 * \\x1e -> 'E', \\x01 -> '.'), 32..128 passes through unchanged.
 * The result is NUL-terminated at the walk index and returned.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h003 state_macro      : none (no state vector).
 *  - h007 char_buffer      : str is a NUL-terminated byte
 *    buffer; the returned buf likewise (h012 domain).
 *  - h009 format_string    : none (no sprintf).
 *  - h012 unsigned_byte_domain: the >=32 && <=128 range test
 *    and the <32 control test compare byte code points 0..255.
 *  - h013 dead_store       : none - i is both the walk index
 *    and the terminator index (live).
 *  - h014 out_of_scope_call: none (self-contained).
 *  - h015 grammar_collision: pre-push scan clean - no backtick,
 *    double-underscore, em dash or !! in this file; the hex
 *    escapes are translated as Char code points, not text.
 *  - h017 naming           : mkpt keeps the vendor name.
 *  - h018 reference_build_visibility: none - no conditionals.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The re-map tests run AFTER the <32 -> '-' assignment, so
 *    \\x1f/\\x1e/\\x01 override the dash - kept as three
 *    independent ifs in the same order (no else chain).
 *  - Bytes >128 match NO branch in the vendor body; the static
 *    buf then keeps its previous-call content at that index.
 *    Kotlin divergence: the returned buffer is a fresh
 *    allocation (the C static-buf reuse rides GC), so >128
 *    bytes are left as the fresh NUL fill - the vendor's stale
 *    static memory has no portable carrier (documented here,
 *    not invented).
 *  - No bounds check against 2048 - the vendor trusts the
 *    caller; kept (a longer str would overflow in C).
 */
fun mkpt(str: CharArray): CharArray {
    val buf = CharArray(2048)
    var i = 0
    while (str[i] != NUL) {
        if (str[i] >= 32.toChar() && str[i] <= 128.toChar()) {
            buf[i] = str[i]
            i++
            continue
        }
        if (str[i] < 32.toChar()) buf[i] = '-'
        if (str[i] == 31.toChar()) buf[i] = 'F'
        if (str[i] == 30.toChar()) buf[i] = 'E'
        if (str[i] == 1.toChar()) buf[i] = '.'
        i++
    }
    buf[i] = NUL
    return buf
}
