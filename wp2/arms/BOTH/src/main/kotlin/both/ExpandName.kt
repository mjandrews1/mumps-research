package both

// Unit 265 (BOTH B9, core): compiler-parse.c::ExpandName (47
// LOC). Batch_plan ordinal 265; units.csv row 259. The
// compiler's NAME coder (the ExpandOrder/ExpandData sibling
// shape): past the argument ordinal, a "#G#" tag emits a
// GlobalName(...) translation line and a "#S#" tag emits a
// LocalName(...) line; in both arms arg2 is written back into
// the caller's bp and the function returns. The emitted third
// argument rides the CodedComma lattice: a coded third
// argument (another "#G#"/"#S#" tag) is expanded through
// Extract2, a bare third argument is copied to its end, and no
// third argument emits the literal "NULL".
//
// BOTH consults (find_rule before render, events logged):
//   h004 globals_ext - reused: the TU-global FILE Out (extern
//     at compiler-parse.c:166) stays the CompilerOutState.out
//     singleton property (the ExpandOrder seat).
//   h007 char_buffer - reused: arg1..arg3 are scan-only String
//     accumulators (the vendor char[32] overflow UB quantizes
//     unbounded, the ExpandOrder precedent); bp is the caller
//     CharArray written with writeC (strcpy fold, NUL landed).
//   h009 format_string - reused: the two fprintf shapes are
//     exact - "\tGlobalName(%s, %s, %s, svPtr); \n" (single
//     space before the newline, no casts) and the LocalName
//     line with the verbatim "(unsigned char *)" casts and the
//     line-split string concatenation.
//   h012 unsigned_byte_domain - reused: the bare-argument scan
//     bound is v1d[i] < 128 && v1d[i] != 0 (the C unsigned-byte
//     comparison; Char 128 rendered as the literal escape).
//   h013 dead_store - reused: the #S# arm's THIRD-ARGUMENT TAG
//     TEST reads strncmp("#S#") twice (the vendor's duplicated
//     condition, "#G#" never tested in the LocalName arm -
//     first operand dead); kept verbatim, ledgered below.
//   h014 out_of_scope_call - reused: Extract2 is the named-owner
//     stub extract2C (owner compiler-parse.c::Extract2,
//     units.csv row 171, untranslated in this arm);
//     GlobalName/LocalName are EMITTED TEXT ONLY (the runtime
//     owners are roster rows 149/121 families, not called).
//   h017 naming - reused: ExpandName keeps the vendor name;
//     arg1/arg2/arg3/i/j keep vendor names.
//   Dafny: exempt (compiler emitter; the DAF B6 T3 family
//     precedent).
//
// Quirks kept (ledgered):
//   - the duplicated "#S#" || "#S#" tag test in the LocalName
//     arm (vendor typo, dead first operand); a "#G#" third
//     argument in the LocalName arm therefore takes the BARE
//     copy path in the vendor - kept verbatim.
//   - the #G# arm tests "#G#" || "#S#" for the coded third
//     argument; the #S# arm tests "#S#" || "#S#" - the family
//     asymmetry is the vendor's, kept.
//   - no bounds checks on the arg1..arg3 copies (31-byte C
//     buffers; overflow UB quantized by the String fold); the
//     v1d scans respect the CharArray end (the ExpandOrder
//     precedent).
//   - unknown directives (neither "#G#" nor "#S#") fall through
//     silently: no emit, no bp write, kept verbatim.

internal fun ExpandName(v1d: CharArray, i0: Int, bp: CharArray) {
    var n = i0 + 1 // vendor: i++ first
    val bufsz = v1d.size

    // if (strncmp(&v1d[i], "#G#", 3) == 0) { ... GlobalName ... }
    if (n + 2 < bufsz && v1d[n] == '#' && v1d[n + 1] == 'G' && v1d[n + 2] == '#') {
        n += 3
        val arg1 = StringBuilder()
        while (n < bufsz && v1d[n] != '#') { arg1.append(v1d[n]); n++ }
        n++
        val arg2 = StringBuilder()
        while (n < bufsz && v1d[n] != '#') { arg2.append(v1d[n]); n++ }
        n++

        val arg3 = StringBuilder()
        if (n < bufsz && v1d[n] == CodedComma) {
            n++
            val tag = n + 2 < bufsz && v1d[n] == '#'
            val isG = tag && v1d[n + 1] == 'G' && v1d[n + 2] == '#'
            val isS = tag && v1d[n + 1] == 'S' && v1d[n + 2] == '#'
            if (isG || isS) {
                arg3.append(extract2C(v1d, n))
            } else {
                while (n < bufsz && v1d[n] < '\u0080' && v1d[n] != '\u0000') {
                    arg3.append(v1d[n]); n++
                }
            }
        } else {
            arg3.append("NULL")
        }

        // fprintf(Out, "\tGlobalName(%s, %s, %s, svPtr); \n", ...)
        val line = "\tGlobalName(" + arg1 + ", " + arg2 + ", " + arg3 +
            ", svPtr); \n"
        try {
            CompilerOutState.out.write(line)
        } catch (e: java.io.IOException) {
            throw RuntimeException(e)
        }

        writeC(bp, arg2.toString()) // strcpy(bp, arg2)
        return
    }

    // if (strncmp(&v1d[i], "#S#", 3) == 0) { ... LocalName ... }
    if (n + 2 < bufsz && v1d[n] == '#' && v1d[n + 1] == 'S' && v1d[n + 2] == '#') {
        n += 3
        val arg1 = StringBuilder()
        while (n < bufsz && v1d[n] != '#') { arg1.append(v1d[n]); n++ }
        n++
        val arg2 = StringBuilder()
        while (n < bufsz && v1d[n] != '#') { arg2.append(v1d[n]); n++ }
        n++

        val arg3 = StringBuilder()
        if (n < bufsz && v1d[n] == CodedComma) {
            n++
            // vendor: strncmp("#S#") || strncmp("#S#") - the
            // duplicated condition (dead first operand, h013)
            val tag = n + 2 < bufsz && v1d[n] == '#'
            val isS = tag && v1d[n + 1] == 'S' && v1d[n + 2] == '#'
            if (isS || isS) {
                arg3.append(extract2C(v1d, n))
            } else {
                while (n < bufsz && v1d[n] < '\u0080' && v1d[n] != '\u0000') {
                    arg3.append(v1d[n]); n++
                }
            }
        } else {
            arg3.append("NULL")
        }

        // fprintf(Out, the LocalName line with verbatim casts)
        val line = "\tLocalName( (unsigned char *) " + arg1 +
            ", (unsigned char *) " + arg2 + ", " +
            "(unsigned char *) " + arg3 + ", svPtr); \n"
        try {
            CompilerOutState.out.write(line)
        } catch (e: java.io.IOException) {
            throw RuntimeException(e)
        }

        writeC(bp, arg2.toString()) // strcpy(bp, arg2)
        return
    }
}
