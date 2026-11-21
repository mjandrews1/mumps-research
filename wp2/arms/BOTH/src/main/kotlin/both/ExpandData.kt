package both

// Unit 135 (BOTH B5 T2, core): compiler-parse.c::ExpandData (24 LOC).
// Batch_plan ordinal 135: core, LOC 24, score 11.4. The compiler's
// indirection-expansion coder: at the position past the argument
// index, a "#G#" tag emits a GlobalData(...) translation line (the
// global path), a "#S#" tag emits a LocalData(...) line (the local
// path), and anything else expands nothing.
//
// Vendor behavior: i is advanced past the argument ordinal first;
// the tag is matched with strncmp over 3 bytes; each arm then copies
// bytes into char arg1[32]/arg2[32] until the closing '#'; emits one
// line to the TU output FILE* Out; and writes arg2 back into the
// caller's bp buffer with strcpy._bp copy back.
//
// Quirks kept (ledgered):
//   - the vendor copies into char arg1[32]/arg2[32] with no bounds
//     check; a field longer than 31 bytes is silent overflow UB in
//     C. The Kotlin fold accumulates an unbounded String, which is
//     the deterministic quantization (no truncation seat exists in
//     the vendor to preserve).
//   - the scan to the closing '#' has no bound on the v1d buffer;
//     a malformed directive walks the C buffer unbounded (UB). The
//     Kotlin scan stops at the CharArray end, quantizing the seat.
//   - the second '#'-scan consumes the tag's trailing '#' but the
//     arg2 scan does skip the field separator exactly as the vendor
//     (i++ after arg1; arg2 runs to the next '#').
//   - unknown directives fall through silently: no emit, no bp
//     write, kept verbatim.
//
// BOTH consults (find_rule before render, events logged):
//   h004 globals_ext - reused: the TU-global FILE* Out (extern at
//     compiler-parse.c:166) becomes the CompilerOutState.out
//     singleton property, registered with its source header line.
//   h007 char_buffer - reused: arg1/arg2 are scan-only String
//     accumulators.
//   h009 format_string - reused: the two fprintf shapes are exact
//     (tab, the verbatim "(unsigned char *)" casts in the emitted
//     text, the trailing newline).
//   h017 naming - hit: ExpandData/o names shaped as vendor.
internal object CompilerOutState {
    // FILE *Out - extern "FILE *Out" at compiler-parse.c:166; the
    // definition lives in the compiler driver TU (not a corpus unit).
    lateinit var out: java.io.Writer
}

internal fun ExpandData(v1d: CharArray, i: Int, bp: CharArray) {
    var n = i + 1 // vendor: i++ first

    if (n + 2 < v1d.size && v1d[n] == '#' && v1d[n + 1] == 'G' && v1d[n + 2] == '#') {
        n += 3
        val arg1 = StringBuilder()
        while (n < v1d.size && v1d[n] != '#') { arg1.append(v1d[n]); n++ }
        n++
        val arg2 = StringBuilder()
        while (n < v1d.size && v1d[n] != '#') { arg2.append(v1d[n]); n++ }
        // fprintf(Out,"\tGlobalData(%s,%s,svPtr);\n",arg1,arg2)
        try {
            CompilerOutState.out.write("\tGlobalData(${arg1},${arg2},svPtr);\n")
        } catch (e: java.io.IOException) {
            // vendor ignores the fprintf return (failure is silent)
        }
        writeC(bp, arg2.toString()) // strcpy (bp, arg2)
        return
    }

    if (n + 2 < v1d.size && v1d[n] == '#' && v1d[n + 1] == 'S' && v1d[n + 2] == '#') {
        n += 3
        val arg1 = StringBuilder()
        while (n < v1d.size && v1d[n] != '#') { arg1.append(v1d[n]); n++ }
        n++
        val arg2 = StringBuilder()
        while (n < v1d.size && v1d[n] != '#') { arg2.append(v1d[n]); n++ }
        // fprintf(Out,"\tLocalData((unsigned char *) %s,(unsigned char *) %s,svPtr);\n",arg1,arg2)
        try {
            CompilerOutState.out.write("\tLocalData((unsigned char *) ${arg1},(unsigned char *) ${arg2},svPtr);\n")
        } catch (e: java.io.IOException) {
            // vendor ignores the fprintf return (failure is silent)
        }
        writeC(bp, arg2.toString()) // strcpy (bp, arg2)
        return
    }
}
