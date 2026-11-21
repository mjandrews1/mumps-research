package daf

// Unit 115 (DAF B4 T3, complex): interp-parse.cpp::readf_ (55 LOC): the
// runtime READ-file loader: streams a MUMPS source file into the pd1
// program buffer from the pd1org offset, folding backslash line
// continuations, mapping >127 bytes to spaces, and trimming CR/TAB
// runs after a continuation.
// Consults: BASE-log row 2 family hit-adapt (FILE* fopen/getc/fclose ->
// a java.io Reader seat read through a buffered wrapper; getc returns
// the Int char code, -1 at EOF exactly like the vendor contract), row 8
///81 hit-adapt (the pd1/i/zd buffers keep CharArray carrier roles;
// zd[] is NUL-terminated in place: zd[0]=0 on open failure), rows 69/2
// hit-adapt (the printf input-error diagnostic on stdout; sigintDaf
// relay), row 30 family (the FILE open failure folds onto the NUL write
// + bare return exactly as vendor), row 10 (the nested getc-loops of
// the continuation trim render as their break/continue structure),
// row 46 hit-adapt (the buggy-looking vendor getc-truncation
// points kept verbatim; no fix applied - documented divergence ledger:
// vendor quirks are not repaired on any arm).
// Seats: errorMessageDaf (interpreter diagnostic; the cache-overflow
// message text kept verbatim), sigintDaf (T1). CRTN/CTRLZ/TAB/LINEFEED
// consts pinned from defines.h.
// Dafny: EXEMPT (stream-bound loader; journal row).

fun readfD(zd: CharArray, pd1org: Short, sv: DafMsv) {

    var i: Int
    var k: Int
    var l: Int

    val j = pd1org.toInt()
    // strcpy(svPtr->CurrentFile, zd)
    var ci = 0
    while (at(zd, ci) != NUL_D) {
        sv.CurrentFile[ci] = zd[ci]
        ci++
    }
    sv.CurrentFile[ci] = NUL_D

    val infile = java.io.File(DafCRuntime.cstring(zd)) // fopen(zd, "r")
    if (!infile.canRead()) {
        zd[0] = NUL_D // infile == NULL
        return
    }
    val reader = infile.bufferedReader()

    l = j

    i = j
    while (i < sv.symlen) {
        k = reader.read() // getc

        if (k > 127) k = 32 // >127 maps to space

        if (k == '\\'.code) { // \ at the end of line is continuation
            k = reader.read()

            if (k < 1) {
                print("\n*** Input error in readf_()\n")
                sigintDaf(100)
            }

            if (k == LINEFEED_D.code) {
                while (reader.read().also { k = it } != 0) { // while (k=getc())
                    if (k == ' '.code || k == TAB_D.code) continue
                    if (k == '\r'.code) continue
                    if (k < 1) break
                    if (k > 0) sv.pd1[i] = k.toChar()
                    break
                }
            } else {
                sv.pd1[i++] = '\\'
                sv.pd1[i] = k.toChar()
                i++
                continue
            }
        }

        if (k == CRTN_D.code || k == CTRLZ_D.code) {
            i--
        } else {
            if (k > 0) sv.pd1[i] = k.toChar()
        }

        if (k == LINEFEED_D.code) {
            sv.pd1[i] = NUL_D
            i++ // the for-loop increment
            continue
        }

        if (k < 1) { // EOF
            reader.close() // fclose
            sv.pd1[i] = NUL_D
            sv.pd1[i + 1] = NUL_D
            sv.pd1len = i - 1
            return
        }

        i++
    }

    if (i >= sv.symlen) {
        errorMessageDaf(
            "Interpreter cache overflow\n*** Increase ibuf in configure",
            sv.LineNumber)
    }
}
