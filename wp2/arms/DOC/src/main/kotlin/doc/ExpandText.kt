package doc

// Unit 228 (B8 tranche 2): compiler-parse.c::ExpandText (batch 8,
// core stratum; LOC 23, score 12.1, rank 117; units.csv row 235;
// vendor compiler-parse.c:2374, physically verified this session:
// the dump asserts physical loc 23 = the roster row). The
// dollar-text expander of the compiler emit family (the unit-135
// ExpandData / unit-11 ExpandNext sibling): terminates the coded
// v1d payload at the first high bit or SOH byte past the current
// position, then emits the runtime source lines that build the
// gtmp carrier and dispatch through fcn. Consult H-015 hit-adapt:
// every fprintf(Out,...) maps to CompilerState.emit with the
// emitted text byte-verbatim (the dollar byte of the emitted
// text escaped per H-015; the vendor printf percent-x of the
// CodedOpen/CodedClose ints renders the lowercase hex WITHOUT the
// 0x prefix per the C percent-x contract - CodedOpen 206 -> ce,
// CodedClose 207 -> cf - precomputed hex strings here); memcheck
// rides the Stubs.memcheck seat (H-014), fcn is the landed unit-123
// real, the trailing sprintf d of iiitmp rides the tmpN naming
// seat of the family. Naming per H-017.

fun ExpandText(v1d: CharArray, iIn: Int, bp: CharArray) {
    var i = iIn

    i++

    var j = i
    while (v1d[j] != NUL) {
        if (v1d[j] > Char(128) || v1d[j] == Char(1)) {
            v1d[j] = NUL
            break
        }
        j++
    }

    CompilerState.emit("\tstrcpy((char *) &gtmp[1], \"\u0024text\u005cx" +
            CodedOpen.toString(16) + "\"); /* CodedOpen */\n")

    CompilerState.emit("\tstrcat((char *) &gtmp[1], (const char *) " +
            CstringOps.cstring(extract2a(v1d, i)) + "); \n")

    CompilerState.emit("\tstrcat((char *) &gtmp[1], \"\u005cx" +
            CodedClose.toString(16) + "\"); /* CodedClose */\n")

    CompilerState.emit("\tstrmove((unsigned char *) &svPtr->v1d[1], " +
            "(unsigned char *) &gtmp[1]); \n")

    Stubs.memcheck(CompilerState.iiitmp)

    CompilerState.emit("\tfcn(svPtr);\n")

    CompilerState.emit("\tstrmove((unsigned char *) tmp" + CompilerState.iiitmp +
            ",(unsigned char *) &svPtr->bd[1]); \n")

    CstringOps.strcpyS(bp, "tmp" + CompilerState.iiitmp)

    CompilerState.iiitmp++
    Stubs.memcheck(CompilerState.iiitmp)
}
