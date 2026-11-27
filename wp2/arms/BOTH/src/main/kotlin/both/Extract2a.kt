package both

// Unit 230 render: compiler-parse.c::Extract2a (roster 230,
// batch 8, 22 LOC). char * Extract2a(char * str) - the
// two-field #-envelope unwrap: splits the #S#/#G# label into
// tmp1 (name) and tmp2 (destination), then EMITS the retrieval
// guard line to Out (the sym_ or Mglobal RETRIEVE ternary with
// the VariableNotFound rider; the S form carries the E21000
// comment). Returns tmp2 (the static-buffer shape kept local).
//
// Consults (rule base wp2/arms/BOTH/rules_v0.pl):
//   h009 format_string - reused: the emission keeps the vendor
//     fprintf argument order exactly (tmp1, tmp2, tmp2, tmp1);
//     no C format specifiers ride the literals (the %s slots
//     are the splices).
//   h013 dead_store - hit: the static tmp1[64]/tmp2[64] pair is
//     fully used here (unlike the Extract1 twin); the 64-byte
//     caps clamp the memcpy (UB quantization family).
//   h014 out_of_scope_call - reused: the emitted TEXT cites
//     sym_/Mglobal/VariableNotFound/VariableNotFound1 (the
//     MpstimeMain stub seats and the Mglobal carrier) - but
//     emission only writes the line; no call. The fprintf(Out)
//     rides the local outp fold (the WriteCode outp precedent;
//     the ExpandOrder IOException pass-through).
//   h015 grammar-collision - reused: ASCII only, no backticks,
//     no raw comment dollars, trailing newline.
//   h017 naming - reused: Extract2a keeps vendor name (the
//     compiler-parse prototype ledger pin; the Extract1/Extract2
//     family seats are distinct).
//
// Quirks kept (ledgered):
//   - the second field scan starts from ++i (the first '#'
//     consumed, the second field between the two '#'s - kept).
//   - the S-form emission embeds the E21000 comment and the
//     "(UNS)" casts verbatim; the G-form has neither cast nor
//     comment (kept exact).
//   - the return is tmp2 ONLY (tmp1 rides the emitted line;
//     the static shape keeps the last-call value, quantized to
//     the local).

// The fprintf(Out, ...) fold (the WriteCode outp precedent).
private fun outpE2a(s: String): Int {
    try {
        CompilerOutState.out.write(s)
    } catch (e: java.io.IOException) {
        throw RuntimeException(e)
    }
    return s.length
}

// char * Extract2a (char * str) - compiler-parse.c (static
// tmp1[64], tmp2[64] locals; the 64-cap clamp quantizes the C
// memcpy overflow).
internal fun Extract2a(str: String): String {
    if (str.isEmpty() || str[0] != '#') return str

    var i = 3
    while (i < str.length && str[i] != '#' && str[i] != '\u0000') i++
    val tmp1 = str.substring(3, minOf(i, 67))

    var j = i + 1 // ++i (the first '#' consumed)
    i = j
    while (i < str.length && str[i] != '#' && str[i] != '\u0000') i++
    val tmp2 = str.substring(j, minOf(i, j + 64))

    if (str.startsWith("#S#")) {
        outpE2a(
            "\tsym_(1, (UNS) " + tmp1 + ", (UNS) " + tmp2 + "," +
                "svPtr) ? " + tmp2 +
                " : VariableNotFound1(svPtr->LineNumber, (UNS) " +
                tmp1 + "); //E21000\n"
        )
    } else if (str.startsWith("#G#")) {
        outpE2a(
            "\tMglobal(RETRIEVE, " + tmp1 + ", " + tmp2 +
                ", svPtr) ?" + tmp2 +
                " : VariableNotFound(svPtr->LineNumber, " + tmp1 +
                ");\n"
        )
    }
    return tmp2
}
