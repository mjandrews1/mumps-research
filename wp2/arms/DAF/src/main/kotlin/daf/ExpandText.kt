package daf

// Unit 228 (DAF B8 T2, core): compiler-parse.c::ExpandText
// (23 LOC, score 12.1): the dollar-text expander of the compiler
// emit family (the ExpandData/ExpandNext siblings): terminates the
// coded v1d payload at the first high-bit or SOH byte past the
// current position, then emits the runtime source lines that build
// the gtmp carrier and dispatch through fcn (vendor
// compiler-parse.c:2374, physically verified this session).
// Consults (2026-11-24T21:16:00Z, nominal): the unit-203 CompilerExtract
// consult set applies (Out (FILE*) -> the C.emit house fold; the
// row-30 pointer fold carries v1d/bp as CharArrays; iiitmp seated
// on the compiler carrier; memcheckDaf seat cited, called twice
// verbatim), the row-27 hit-reuse (the sprintf(bp,"tmp%d",iiitmp)
// naming fold rides the file-local sprintfIntD helper replicated
// from the unit-203 precedent), the row-33 hit-adapt family (the
// vendor printf percent-x of the CodedOpen/CodedClose ints renders
// as the lowercase hex WITHOUT the 0x prefix per the C percent-x
// contract: CodedOpen 206 -> ce, CodedClose 208 -> d0; the dollar
// byte of the emitted text rides the unicode escape), Extract2a
// resolves to the unit-232 real of THIS batch (extract2aD at
// Extract2a.kt; the &v1d[i] offset idiom is that seat's contract).
// Dafny: EXEMPT (emit-side compiler plumbing; journal row).

fun expandTextD(st: DafCompilerState, v1d: CharArray, iIn: Int, bp: CharArray) {
    val C = st

    var i = iIn
    i++ // i++

    // for (j=i; v1d[j]!='\0'; j++) if (v1d[j]>128||v1d[j]==1) ...
    var j = i
    while (v1d[j] != NUL_D) {
        if (v1d[j] > Char(128) || v1d[j] == '\u0001') {
            v1d[j] = NUL_D // v1d[j]='\0'
            break
        }
        j++
    }

    C.emit("\tstrcpy((char *) &gtmp[1], \"\u0024text\u005cx" +
            CODED_OPEN_D.code.toString(16) + "\"); /* CodedOpen */\n")

    C.emit("\tstrcat((char *) &gtmp[1], (const char *) " +
            extract2aD(C, v1d, i) + "); \n")

    C.emit("\tstrcat((char *) &gtmp[1], \"\u005cx" +
            CODED_CLOSE_D.code.toString(16) + "\"); /* CodedClose */\n")

    C.emit("\tstrmove((unsigned char *) &svPtr->v1d[1], " +
            "(unsigned char *) &gtmp[1]); \n")

    memcheckDaf(C.iiitmp) // memcheck (iiitmp)

    C.emit("\tfcn(svPtr);\n")

    C.emit("\tstrmove((unsigned char *) tmp" + C.iiitmp +
            ",(unsigned char *) &svPtr->bd[1];\n")

    sprintfInt228D(bp, C.iiitmp) // sprintf (bp, "tmp%d", iiitmp)

    C.iiitmp++
    memcheckDaf(C.iiitmp)
}

// sprintf(bp, "tmp%d", iiitmp) - the unit-203 sprintfIntD naming
// helper replicated per the per-file private-helper convention.
private fun sprintfInt228D(dst: CharArray, v: Int) {
    val s = "tmp$v"
    var q = 0
    while (q < s.length) {
        dst[q] = s[q]
        q++
    }
    dst[q] = NUL_D
}
