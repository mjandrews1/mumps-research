package doc

// Unit 115 (B4 tranche 3): interp-parse.cpp::readf_ (batch 4,
// complex stratum; LOC 55, score 52.9, rank 238) - the dotted
// indented source-file reader of the interpreter's ZLOAD path:
// opens zd as a file, reads bytes at pd1 offset pd1org,
// folding CR/T/Z (CRTN 13, CTRLZ 26, Carriers), the
// backslash-newline continuation, and the high-byte fold
// (k > 127 -> 32). CurrentFile (stateVector.h:96) and symlen
// (stateVector.h:59) are the NEW Msv seats added by this unit
// (EDIT-ONLY Carriers addition, vendor anchors); the read folds
// onto the stateVector pd1 (H-004 one-owner, :43). fopen "r"
// NULL maps per H-001 to the FileReader try/catch
// (FileNotFOund/IOException quantized to the null arm); getc
// rides Reader.read (H-012: the Int char domain keeps the C
// signed-char-at-EOF folds: k>127 is taken BEFORE the
// continuation check, so EOF (-1) fails it and hits the k<1
// terminator arm). printf maps to System.out (H-001); sigint is
// the H-014 seat; ErrorMessage is the delegate seat (the
// interp-sysfunc owner). Vendor quirks kept and noted: the
// l = j local is dead after its assignment (H-013 dead local);
// the while (k = getc(infile)) continuation-idiom loop only
// breaks on k<1 (an EOF -1 would keep looping in C were the
// k<1 break absent - the break catches it here exactly);
// pd1len is written i - 1 (one short of the NUL - vendor
// arithmetic kept); the final ErrorMessage after the symlen
// loop has no return (vendor falls off the void). The vendor's
// svPtr->pd1[i + 1] = 0 double terminator kept. Naming per
// H-017 (readf_ underscore verbatim).

fun readf_(zd: CharArray, pd1org: Short, svPtr: Msv) {
    var i: Int
    var k: Int
    val j = pd1org.toInt()

    svPtr.CurrentFile = CstringOps.cstring(zd)
    val infile: java.io.Reader? = try {
        java.io.FileReader(java.io.File(CstringOps.cstring(zd)))
    } catch (e: java.io.IOException) {
        null
    }

    if (infile == null) {
        zd[0] = NUL
        return
    }

    @Suppress("UNUSED_VARIABLE") // vendor dead local (H-013)
    val l = j

    i = j
    while (i < svPtr.symlen) {
        k = infile.read()

        if (k > 127) k = 32

        if (k == '\\'.code) { // backslash at end of line is continuation
            k = infile.read()

            if (k < 1) {
                System.out.print("\n*** Input error in readf_()\n")
                Stubs.sigint(100)
            }

            if (k == '\n'.code) {
                while (true) {
                    k = infile.read()
                    if (k == 0) break // while (k = getc) idiom: stop at NUL
                    if (k == ' '.code || k == '\t'.code) continue
                    if (k == '\r'.code) continue
                    if (k < 1) break
                    if (k > 0) svPtr.pd1[i] = k.toChar()
                    break
                }
            } else {
                svPtr.pd1[i++] = '\\'
                svPtr.pd1[i] = k.toChar()
                i++
                continue
            }
        }

        if (k == CRTN || k == CTRLZ) {
            i--
        } else {
            if (k > 0) svPtr.pd1[i] = k.toChar()
        }

        if (k == LINEFEED.code) {
            svPtr.pd1[i] = NUL
            i++
            continue
        }

        if (k < 1) {
            infile.close()
            svPtr.pd1[i] = NUL
            svPtr.pd1[i + 1] = NUL
            svPtr.pd1len = (i - 1).toLong()
            return
        }

        i++
    }

    if (i >= svPtr.symlen) {
        errorMessage(
            "Interpreter cache overflow\n*** Increase ibuf in configure",
            svPtr.LineNumber
        )
    }
}
