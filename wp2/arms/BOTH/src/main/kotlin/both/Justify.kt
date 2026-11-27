package both

// Unit 207 render: interp-sym.cpp::_justify (roster 207,
// batch 7, 15 LOC per units.csv; vendor body interp-sym.cpp).
// The \u0024justify engine: the -1 arm left-pads the input to
// width w; the numeric arm formats the atof input with the
// built %<l>.<k>lf format.
//
// Consults (rule base wp2/arms/BOTH/rules_v0.pl):
//   h014 out_of_scope_call - reused: lpad called direct (the
//     StrmanipMisc seat, interp-strmanip.cpp::lpad); strtodC/
//     atolC/strlenC/writeC/cStringOf direct.
//   h009 float_format - reused: the %<l>.<k>lf double format
//     pins to Locale.US (the sprintf fold; the arm's float
//     seat).
//   h015 grammar-collision - reused: ASCII only, no backticks,
//     no raw comment dollars, trailing newline; the intrinsic
//     name's dollar rides the \u0024 escape.
//   h017 naming - reused: _justify keeps the vendor name (the
//     leading underscore is a valid Kotlin identifier).
//
// Quirks kept (ledgered):
//   - the -1 arm: k>=l passes the INPUT through untouched;
//     l>255 clamps to 255 BEFORE lpad (kept order).
//   - sprintf(tmp2,"%c%d%c%dlf",'%',l,'.',k): the format
//     string is built by pieces - folds to the Java format
//     string; the double-printf (tmp2 as format) has no
//     format-processing surprises (no %% pairs).
//   - k (arg 3) is used only inside the format (the comment
//     /* arg 3 */ kept).
//   - the 32-byte tmp2 and STR_MAX bd ride local scratch; the
//     strncpy(...,STR_MAX) calls fold to the writeC full
//     copies (NUL-terminated sources; journaled).

// void _justify(unsigned char out[], unsigned char in[],
//               unsigned char w[], unsigned char p[]) -
// interp-sym.cpp
internal fun _justify(out: CharArray, ind: CharArray, w: CharArray, p: CharArray) {
    val bd = CharArray(STR_MAX)
    var l = atolC(cStringOf(w)).toInt() // atoi(w)
    writeC(bd, cStringOf(ind)) // strncpy(bd,in,STR_MAX)

    if (cStringOf(p) == "-1") {
        val k = strlenC(bd)

        if (k >= l) {
            writeC(out, cStringOf(ind)) // strncpy(out,in,STR_MAX)
            return
        }

        if (l > 255) l = 255
        lpad(bd, bd, l) // lpad(bd,bd,(short) l)
        writeC(out, cStringOf(bd)) // strncpy(out,bd,STR_MAX)
        return
    }

    val k = atolC(cStringOf(p)).toInt() /* arg 3 */
    // sprintf(tmp2, "%c%d%c%dlf", '%', l, '.', k) -> the
    // Locale.US format string (h009)
    val t1 = strtodC(cStringOf(ind)) // atof(in)
    val s = String.format(java.util.Locale.US, "%" + l + "." + k + "f", t1)
    writeC(out, s) // sprintf(bd,tmp2,t1); strncpy(out,bd,STR_MAX)
}
