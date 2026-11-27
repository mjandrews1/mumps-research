package both

// Unit 234 render: compiler-parse.c::ExpandText (roster 234,
// batch 8, 29 LOC). void ExpandText(unsigned char * v1d, int i,
// unsigned char * bp) - the \u0024text directive expander:
// terminates the operand scan at the first high/SOH byte, then
// emits the strcpy/strcat/strmove/fcn line family to Out (the
// CodedOpen/CodedClose envelope over the Extract2a splice) and
// seats bp at the tmp counter.
//
// Consults (rule base wp2/arms/BOTH/rules_v0.pl):
//   h004 globals_ext - reused: the FILE* Out rides the
//     CompilerOutState.out singleton (the ExpandData
//     precedent); iiitmp/MaxTmp ride the CompilerGlobalsExt
//     seats.
//   h009 format_string - reused: the \u0024text line carries
//     the CodedOpen %x splice (206 -> "ce" via the hex
//     formatter, the ptrHex precedent idiom); the tmp%d splices
//     ride string concatenation; the emitted text keeps the
//     verbatim casts and the /* CodedOpen */ comments.
//   h013 dead_store - consulted, no hits here (the record 222
//     cannon dead-guard is the family note).
//   h014 out_of_scope_call - reused: Extract2a direct (the
//     record 230 render, this batch); memcheck direct (the
//     ExtractCompiler seat); writeC direct.
//   h015 grammar-collision - reused: ASCII only, no backticks,
//     no raw comment dollars, trailing newline; the emitted
//     \u0024text dollar rides the \\u0024 escape (the Kotlin
//     template hazard; the MstringTranslate precedent).
//   h017 naming - reused: ExpandText keeps vendor name/case
//     (the compiler-parse prototype ledger pin).
//
// Quirks kept (ledgered):
//   - the operand terminator test is v1d[j]>128 OR v1d[j]==1
//     (>128, not >=128 - the 128 byte itself passes; kept).
//   - the j scan starts at the post-increment i (i++ first).
//   - the memcheck(iiitmp) call sits BEFORE the fcn emission
//     (the vendor order kept); the SECOND memcheck after the
//     increment is the ExtractCompiler twin shape.
//   - bp seats at "tmp<iiitmp>" BEFORE the increment (the
//     sprintf-then-increment order kept).

// The fprintf(Out, ...) fold (the WriteCode outp precedent).
private fun outpEt(s: String): Int {
    try {
        CompilerOutState.out.write(s)
    } catch (e: java.io.IOException) {
        throw RuntimeException(e)
    }
    return s.length
}

// &v1d[i] .. NUL (the strAt seat; the Extract2a splice input).
private fun strAtEt(v1d: CharArray, off: Int): String {
    val sb = StringBuilder()
    var p = off
    while (v1d[p] != '\u0000') {
        sb.append(v1d[p])
        p++
    }
    return sb.toString()
}

// void ExpandText(unsigned char * v1d, int i, unsigned char * bp)
// - compiler-parse.c
internal fun ExpandText(v1d: CharArray, i0: Int, bp: CharArray) {
    val g = CompilerGlobalsExt
    val i = i0 + 1 // i++;

    var j = i
    while (v1d[j] != '\u0000') {
        if (v1d[j] > '\u0080' || v1d[j] == '\u0001') {
            v1d[j] = '\u0000'
            break
        }
        j++
    }

    outpEt(
        "\tstrcpy((char *) &gtmp[1], \"\u0024text\\x" +
            java.lang.Integer.toHexString(206) + "\"); /* CodedOpen */\n"
    )

    outpEt(
        "\tstrcat((char *) &gtmp[1], (const char *) " +
            Extract2a(strAtEt(v1d, i)) + "); \n"
    )

    outpEt(
        "\tstrcat((char *) &gtmp[1], \"\\x" +
            java.lang.Integer.toHexString(207) + "\"); /* CodedClose */\n"
    )

    outpEt(
        "\tstrmove((unsigned char *) &svPtr->v1d[1], " +
            "(unsigned char *) &gtmp[1]); \n"
    )

    memcheck(g.iiitmp)

    outpEt("\tfcn(svPtr);\n")

    outpEt(
        "\tstrmove((unsigned char *) tmp" + g.iiitmp +
            ",(unsigned char *) &svPtr->bd[1]);\n"
    )

    writeC(bp, "tmp" + g.iiitmp) // sprintf(bp, "tmp%d", iiitmp)

    g.iiitmp++
    memcheck(g.iiitmp)
}
