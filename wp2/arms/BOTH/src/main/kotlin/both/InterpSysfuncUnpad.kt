package both

// Unit 298 (BOTH B10, core): interp-sysfunc.cpp::unpad (16
// LOC). Batch_plan ordinal 298; units.csv row 288. The gpad
// entry decoder: when the gpad mode is off the key passes
// through untouched; when on, a key whose first byte is the
// 0x1f marker decodes as the padded numeric form - the sign
// byte after the marker is normalized ('0' -> '-', anything
// else -> '+'), the next GPADWIDTH (40, defines.h:215) bytes
// copy into scratch, NUL-terminate, quiet-parse as double
// (sscanf %lf), and re-emit into the key buffer as the %lg
// shortest form.
//
// BOTH consults (find_rule before render, events logged):
//   h007 char_buffer - reused: tmp is the 64-byte local
//     scratch; the memcpy+set-NUL pair folds to the bounded
//     slice, and the sprintf writes back into the caller's
//     key with one copy-in and the NUL.
//   h009 format_string - reused: "%lf" is the strtod
//     quiet-parse seat (the sscanf fold) and "%lg" is the
//     Locale.US shortest-double formatter (the gcvt family
//     shape, precision band of the %lg contract).
//   h012 unsigned_byte_domain - reused: the 0x1f marker and
//     the sign normalization stay in the byte domain
//     (CharArray compare/write, 31.toChar()).
//   h014 out_of_scope_call - reused: svPtr->gpad rides the
//     Msv field seat (stateVector.h:70, BothCRuntime.kt).
//   h017 naming - reused: unpad keeps the vendor name; r/tmp/
//     x keep vendor names.
//   Dafny: EXEMPT (the %lg re-format is the gcvt family
//     envelope; the quiet-parse face is the family exemption).
//
// Vendor quirks kept (ledgered):
//   - the sign byte is OVERWRITTEN in place ('0' -> '-',
//     else '+') BEFORE the copy, so the scratch carries the
//     normalized sign; kept verbatim.
//   - memcpy copies GPADWIDTH bytes with no bound check
//     against the key's remaining length (a short key would
//     read past its NUL in the C domain); the Kotlin slice
//     stops at the buffer end - journaled quantization, the
//     reference gpad keys are full-width.
//   - sscanf failure leaves x untouched (UB read of an
//     uninitialized double in the vendor); the fold seeds 0.0
//     before the parse - divergence journaled (the reference
//     inputs are well-formed gpad keys).

// void unpad(unsigned char *key, struct MSV * svPtr)
//   - interp-sysfunc.cpp:949
internal fun unpad(key: CharArray, svPtr: Msv) {
    if (svPtr.gpad == 0) return

    if (key[0] == 31.toChar()) { // a gpad entry found
        var r = 1

        // if (*r=='0') *r='-'; else *r='+';
        key[r] = if (key[r] == '0') '-' else '+'

        // memcpy(tmp, r, GPADWIDTH); tmp[GPADWIDTH]='\0';
        val tmp = CharArray(64)
        var n = 0
        while (n < GPADWIDTH && r + n < key.size && key[r + n] != 0.toChar()) {
            tmp[n] = key[r + n]
            n++
        }
        // tmp[n] stays 0 (the array zero-fill is the NUL)

        // sscanf(tmp, "%lf", &x);
        val x = strtodC(cStringOf(tmp)) // quiet parse (0.0 seed on failure)

        // sprintf(key, "%lg", x);
        val s = fmtG(x) // the %lg default-precision form (h009 seat)
        for (i in s.indices) key[i] = s[i]
        key[s.length] = 0.toChar()
    }
}

// #define GPADWIDTH 40 - defines.h:215 (the gpad key width).
private const val GPADWIDTH = 40
