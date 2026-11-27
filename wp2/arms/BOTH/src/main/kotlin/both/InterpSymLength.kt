package both

// Unit 281 (BOTH B10, simple): interp-sym.cpp::_length (16
// LOC). Batch_plan ordinal 281; units.csv row 305. The
// \\u0024length helper twin: count the non-overlapping
// leftmost occurrences of key in in (1 + the match walk), or
// the plain byte length when the key is empty. The match walk
// rides the unit-260 xindex engine seat (interp-strmanip.cpp
// body; the interp-sym TU binds the same call).
//
// BOTH consults (find_rule before render, events logged):
//   h007 char_buffer - reused: out/in/key are the C byte
//     domain; the sprintf folds to the digit string copied
//     into out with a NUL (one copy-in).
//   h009 format_string - reused: "%d" is a plain decimal;
//     Kotlin Int.toString gives the same digit shape.
//   h010 numeric_promotion - reused: the (short) i cast in
//     the vendor call narrows to the C short domain; the
//     xindex seat carries Int (the same-body fold, the
//     unit-260 precedent) and the values stay in range for
//     the STR_MAX walk.
//   h014 out_of_scope_call - reused: xindex rides its
//     unit-260 seat (Xindex.kt).
//   h017 naming - reused: _length keeps the vendor name; out/
//     in/key/i/j/k keep vendor names.
//   Dafny: exempt (byte-domain scan; the length-count spec
//     family carries the shape, the unit-159 precedent).

// void _length(unsigned char out[], unsigned char in[],
//              unsigned char key[]) - interp-sym.cpp
internal fun _length(out: CharArray, ind: CharArray, key: CharArray) {
    val keyStr = cStringOf(key)
    val inStr = cStringOf(ind)

    if (keyStr.isNotEmpty()) {
        var i = 1
        var j = 0
        val k = keyStr.length

        // while ((i = xindex(in, key, (short) i)) != 0)
        while (true) {
            i = xindex(ind, key, i)
            if (i == 0) break
            j++
            i += k
        }

        // sprintf(out, "%d", j + 1);
        val s = (j + 1).toString()
        for (n in s.indices) out[n] = s[n]
        out[s.length] = 0.toChar()
        return
    }

    // sprintf(out, "%d", strlen(in));
    val s = inStr.length.toString()
    for (n in s.indices) out[n] = s[n]
    out[s.length] = 0.toChar()
}
