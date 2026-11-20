package doc

// Unit 195 (B7 tranche 1): interp-sym.cpp::mkpt (batch 7, simple
// stratum; LOC 16, score 9.0, rank 82; physLOC 970, physically
// verified this session, nonblank 16). The one-pass printable
// filter over the 2048-byte static buffer: in-range ASCII
// (32..128 decimal) copies verbatim (the vendor's inclusive
// 128), below 32 maps to hyphen, then the three specific
// control bytes overwrite: 0x1f becomes F, 0x1e becomes E, 0x01
// becomes period (the vendor's separate ifs fall through, so
// 0x1f/0x1e/0x01 take the hyphen and then their own byte - the
// render keeps the same statement order). Bytes ABOVE 128 hit
// no vendor arm: the static buffer keeps its stale byte there
// (H-013 quantize note: the unassigned buffer position is left
// untouched exactly as the C leaves it; no fill is invented).
// The static buf rides the MkptState object per H-004 (single
// file-scope buffer, returned by reference in the vendor), so
// the return hands back the SAME carrier each call, matching
// the C aliasing. Naming per H-017 kept (mkpt is the C symbol;
// the underscore-leading _find/_justify seats are the sibling
// precedent, this one has no leading underscore).

private object MkptState {
    // static unsigned char buf[2048]
    val buf = CharArray(2048)
}

fun mkpt(str: CharArray): CharArray {
    val buf = MkptState.buf
    var i = 0
    while (str[i] != '\u0000') {
        if (str[i] >= ' ' && str[i] <= Char(128)) {
            buf[i] = str[i]
            i++
            continue
        }
        if (str[i] < ' ') buf[i] = '-'
        if (str[i] == Char(0x1f)) buf[i] = 'F'
        if (str[i] == Char(0x1e)) buf[i] = 'E'
        if (str[i] == Char(0x01)) buf[i] = '.'
        i++
    }
    buf[i] = '\u0000'
    return buf
}
