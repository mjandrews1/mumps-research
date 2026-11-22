package daf

// Unit 168 (DAF B6 T2, core): interp-sym.cpp::_extract
// (34 LOC, score 13.0, rank 126): the \$EXTRACT kernel of the
// interpreter symbol service: from-1 adjust, the to-1 adjust, the
// inverted-range and out-of-range empties, the i==0&&j==0
// first-char special, and the inclusive slice copy.
// Consults (2026-11-22T16:01:30Z, nominal): BASE-log row 28
// hit-reuse (the two atoi parses over the start/len carriers), rows
// 72/44 hit-adapt (the out-param write renders as the copy into the
// out carrier; the render returns Unit and writes outBuf exactly - a
// conditional-write fold is unnecessary because each return path
// writes), rows 81/15 hit-reuse (the in[i] reads ride the at()
// end-guard; the C strmove(out, \"\") folds onto out[0]=NUL_D exactly
// as at the Escan strmove precedent), row 10 hit-reuse (the
// early-return ladder keeps the vendor order: inverted-range empty,
// the i==0&&j==0 special, the i>n empty, the j>n clamp).
// VENDOR QUIRKS kept: (1) the C i--/j-- adjust converts the 1-based
// MUMPS range to the 0-based slice but a NEGATIVE i clamps to 0
// while a negative j becomes j=i (the else branch) - the asymmetry
// is the vendor shape; (2) the i==0&&j==0 special returns in[0]
// EVEN when in is empty (the C reads garbage; the render reads the
// at() NUL guard char and the write is out[0]=NUL when empty - the
// row-81 end-guard divergence ledger); (3) j>n clamps to n before
// the copy so the range past the end reads the string prefix; (4)
// the copy loop is inclusive of BOTH endpoints (i<=j), unlike the
// usual half-open Kotlin slice - the inclusive form is kept; (5) a
// start of "0" makes the adjust produce i = -1 (the C then reads
// in[-1]; the render rides the at() NUL guard instead - the
// OOB-read divergence ledger).
// SEAT RESOLUTION: DOC's Stubs.Mextract precedent named this owner;
// at DAF there was no prior seat - this render is the first binding,
// no stub to release.
// Dafny: spec/extract.dfy (the slice-copy kernel contract over the
// walked range; the parse/adjust ladder out of scope - journal row).

fun extractD(outBuf: CharArray, inBuf: CharArray,
             startBuf: CharArray, lenBuf: CharArray): Unit {

    var i = atoi(DafCRuntime.cstring(startBuf)) // i = atoi(start)
    var j = atoi(DafCRuntime.cstring(lenBuf))   // j = atoi(len)

    // if (i < 0) i = 0; else i--;
    if (i < 0) i = 0 else i--

    // if (j < 0) j = i; else j--;
    if (j < 0) j = i else j--

    // if (j < i) { strmove(out, ""); return; }
    if (j < i) {
        outBuf[0] = NUL_D // strmove(out,"") fold (row 15)
        return
    }

    // if (i == 0 && j == 0) { out[0]=in[0]; out[1]='\0'; return; }
    if (i == 0 && j == 0) {
        outBuf[0] = at(inBuf, 0)
        outBuf[1] = NUL_D
        return
    }

    // n = strlen(in);
    val n = DafCRuntime.cstring(inBuf).length

    // if (i > n) { strmove(out, ""); return; }
    if (i > n) {
        outBuf[0] = NUL_D
        return
    }

    // if (j > n) j = n;
    if (j > n) j = n

    // for (k = 0; i <= j; i++) out[k++] = in[i];
    // out[k] = 0;
    var k = 0
    while (i <= j) {
        outBuf[k++] = at(inBuf, i)
        i++
    }
    outBuf[k] = NUL_D
}
