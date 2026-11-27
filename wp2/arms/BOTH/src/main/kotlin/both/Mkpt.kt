package both

// Unit 206 render: interp-sym.cpp::mkpt (roster 206, batch 7;
// units.csv 23 LOC region; vendor body interp-sym.cpp). The
// displayable-key mapper: printable bytes copy through; <32
// becomes '-', with the \u001f->'F', \u001e->'E', \u0001->'.'
// overwrite cascade (the vendor ifs are a cascade, not
// else-if: '-' lands first, then the specific code overwrites).
//
// Consults (rule base wp2/arms/BOTH/rules_v0.pl):
//   h014 out_of_scope_call - reused: no callables (pure local
//     walk; cStringOf folds the return).
//   h015 grammar-collision - reused: ASCII only, no backticks,
//     no raw comment dollars, trailing newline.
//   h017 naming - reused: mkpt keeps vendor name/case.
//
// Quirks kept (ledgered):
//   - static unsigned char buf[2048]: the vendor STATIC is
//     kept as the file-scope buffer below (NOT a value return):
//     a byte >128 matches NO arm, so the previous call's
//     residue byte stays in the window before the NUL - the
//     persistent buffer reproduces the vendor residue exactly
//     (journaled; a value-return fold would have quantized
//     it away).
//   - >=32 && <=128: the 128 boundary is INCLUSIVE (vendor
//     exact; 128 is not ASCII-128 but the vendor's bound).
//   - a str longer than 2047 overflows the vendor buffer (UB);
//     quantized with the bounds guard, journaled.

// static unsigned char buf[2048] - the vendor static kept as
// the file-scope carrier (the residue quirk rides it)
private val mkptBuf = CharArray(2048)

// unsigned char * mkpt(unsigned char *str) - interp-sym.cpp
internal fun mkpt(str: String): String {
    var i = 0
    while (i < str.length && str[i] != '\u0000' && i < mkptBuf.size) {
        val c = str[i].code
        if (c >= 32 && c <= 128) {
            mkptBuf[i] = str[i]
            i++
            continue
        }
        if (c < 32) mkptBuf[i] = '-' // the cascade base
        if (c == 31) mkptBuf[i] = 'F' // \u001f
        if (c == 30) mkptBuf[i] = 'E' // \u001e
        if (c == 1) mkptBuf[i] = '.' // \u0001
        // >128: no arm matches - the static residue byte stays
        i++
    }
    if (i < mkptBuf.size) mkptBuf[i] = '\u0000'
    return cStringOf(mkptBuf)
}
