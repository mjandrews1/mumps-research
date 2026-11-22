package both

// Unit 173 (BOTH B6 T2, core): compiler-parse.c::ExpandOrder (50
// LOC). Batch_plan ordinal 173: core, LOC 50, score 9.8,
// complexity rank 185. Vendor body compiler-parse.c:2208-2258.
// The compiler's ORDER(...) coder: past the argument ordinal, a
// "#G#" tag emits a GlobalOrder(...) translation line and a
// "#S#" tag emits a LocalOrder(...) line; in both arms arg2 is
// written back into the caller's bp and the function returns.
// The sibling of unit 135 ExpandData (B5 T2); the emitted third
// argument rides the CodedComma lattice: a coded third argument
// (another "#G#"/"#S#" tag) is expanded through Extract2, a bare
// third argument is copied to its end, and no third argument
// emits the literal "NULL".
//
// Vendor behavior: i is advanced past the argument index first;
// the tag is matched with strncmp over 3 bytes; arg1/arg2 are
// scanned to the closing '#'; the third-argument seat reads the
// byte at the current position against CodedComma (char 208,
// defines.h:40) and never advances for the absent case; the
// fprintf writes one line to the TU output FILE Out; strcpy
// writes arg2 back into bp.
//
// Quirks kept (ledgered):
//   - the vendor copies into char arg1[32]/arg2[32]/arg3[32]
//     with no bounds check; a field longer than 31 bytes is
//     silent overflow UB in C (the ExpandData precedent). The
//     Kotlin fold accumulates unbounded Strings, quantizing the
//     seat the same way.
//   - the '#'-scans and the bare-argument scan (<128 and not
//     NUL) have no bound on the v1d buffer; malformed input
//     walks past the C buffer (UB). The Kotlin scans stop at
//     the CharArray end; the '\0' sentinel rides the buffer's
//     NUL byte (writeC lands NUL; the walk also respects the
//     CharArray length).
//   - CodedComma (208) sits outside ASCII; the seat compares the
//     unsigned-byte value verbatim (Char 208), h015-safe because
//     the literal is written as the escape form.
//   - unknown directives fall through silently: no emit, no bp
//     write, kept verbatim (arg3 is unused in that path).
//
// BOTH consults (find_rule before render, events logged):
//   h004 globals_ext - reused and hit: the TU-global FILE Out
//     (extern at compiler-parse.c:166) stays the
//     CompilerOutState.out singleton property (B5 T2 seat).
//   h007 char_buffer - reused and hit: arg1..arg3 are scan-only
//     String accumulators; bp is the caller CharArray written
//     with writeC (lands NUL, the strcpy fold).
//   h009 format_string - reused and hit: the two fprintf shapes
//     are exact (leading tab, the verbatim "(unsigned char *)"
//     casts, the // p2236 comment on the GlobalOrder line, the
//     trailing space then newline on the LocalOrder line).
//     NOTE: the vendor's C string literals are six separate
//     concatenated pieces; the emitted bytes are identical.
//   h014 out_of_scope_call - reused and hit: Extract2 (owner
//     compiler-parse.c::Extract2, roster 171, untranslated in
//     this arm) is the named-owner stub extract2C; no other
//     cross-TU bodies sit in this unit.

internal const val CodedComma = 208.toChar() // defines.h:40 (T2-add)

internal fun ExpandOrder(v1d: CharArray, i0: Int, bp: CharArray) {
    var n = i0 + 1 // vendor: i++ first
    val bufsz = v1d.size

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
                while (n < bufsz && v1d[n] < ' ' + 96 && v1d[n] != '\u0000') {
                    arg3.append(v1d[n]); n++
                }
            }
        } else {
            arg3.append("NULL")
        }

        // fprintf(Out, GlobalOrder line with // p2236 comment)
        val line = "\tGlobalOrder((unsigned char *) " + arg1 +
            ", (unsigned char *) " + arg2 + ", \n" +
            "\t\t(unsigned char *) " + arg3 + ", svPtr); // p2236\n"
        try {
            CompilerOutState.out.write(line)
        } catch (e: java.io.IOException) {
            throw RuntimeException(e)
        }

        writeC(bp, arg2.toString()) // strcpy (bp, arg2)
        return
    }

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
            val tag = n + 2 < bufsz && v1d[n] == '#'
            val isS = tag && v1d[n + 1] == 'S' && v1d[n + 2] == '#'
            val isG = tag && v1d[n + 1] == 'G' && v1d[n + 2] == '#'
            if (isS || isG) {
                arg3.append(extract2C(v1d, n))
            } else {
                while (n < bufsz && v1d[n] < ' ' + 96 && v1d[n] != '\u0000') {
                    arg3.append(v1d[n]); n++
                }
            }
        } else {
            arg3.append("NULL")
        }

        // fprintf(Out, LocalOrder line)
        val line = "\tLocalOrder((unsigned char *) " + arg1 +
            ", (unsigned char *) " + arg2 + ", " +
            "(unsigned char *) " + arg3 + ", svPtr); \n"
        try {
            CompilerOutState.out.write(line)
        } catch (e: java.io.IOException) {
            throw RuntimeException(e)
        }

        writeC(bp, arg2.toString()) // strcpy (bp, arg2)
        return
    }
}
