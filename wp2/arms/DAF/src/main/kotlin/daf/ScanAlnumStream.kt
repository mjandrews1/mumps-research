package daf

// Unit 161 (DAF B6 T1, simple): libmpscpp.cpp::ScanAlnum(istream, ...)
// (18 LOC, score 8.4, rank 73): the ISTREAM overload of the mstring
// word scanner: identical filter kernel to the unit-104 FILE*
// overload rendered at ScanAlnum.kt in B4 T2, but the read is the
// u >> word stream extraction instead of the fscanf token read.
// DUMP DISAMBIGUATION ledgered (row 83-adjacent evidence check): the
// dump4 first-body-by-loc extraction returned the FILE* overload
// body for this row (both overloads are 18 physical LOC); the units
// table carries three ScanAlnum rows (the units.csv overloads: the
// score-13.1 MSV form, the score-10.6 FILE* form rendered at unit 104,
// and this score-8.4 istream form, ptr_marks 0, the simple stratum).
// The vendor source read directly (libmpscpp.cpp:10086) fixes this
// row's body as the istream form: the unit-104 render stays the FILE*
// owner and this render is the istream owner, no shared body.
// Consults (2026-11-22T15:48:00Z, nominal): BASE-log row 101
// hit-reuse (the mstring return folds to String; the unit-104
// precedent), row 60 family MANUAL-DECISION (the istream u param,
// TAKEN BY VALUE in the vendor, folds onto the same PushbackReader
// stream seat as the FILE* overload; u >> word is the
// whitespace-delimited extraction whose seat is fscanfS of
// DafCRuntimeT2 - the 8191 token cap of fscanfS stands in for the
// vendor's unbounded 8192-buffer write whose overflow is UB on the
// reference build; the cap is the seat divergence, ledgered), rows
// 25/28 hit-reuse (isalnum -> isAlnumAscii, tolower -> toLowerAscii,
// isdigit tmp1[0] probe -> the ASCII digit range over the valid
// tail, with the C short-circuit || order kept: the digit probe is
// reached only when j > 0), row 10 hit-reuse (the goto repeat walk
// -> while(true)+continue; the EOF/test-first shape shared with the
// unit-104 render).
// Vendor quirk kept: the max > 511 guard shares the same EOF branch
// (empty-string return), evaluated at every retry, so a token stream
// paired with max > 511 never yields a word; the vendor shape, kept.
// Dafny: EXEMPT (stream-bound filter loop, the unit-104 exemption
// applies to the overload family; journal row).

fun ScanAlnumStreamD(u: java.io.PushbackReader, min: Int, max: Int): String {

    val tmp1 = StringBuilder() // char tmp1[8192] seat
    var j = 0

    while (true) { // repeat:

        tmp1.setLength(0)
        j = 0

        val word = fscanfS(u) // if (!(u >> word) ...)
        if (word == null || max > 511) return "" // ... || max > 511

        for (ch in word) { // for (int i=0; word[i]; i++)
            if (isAlnumAscii(ch)) {
                tmp1.append(toLowerAscii(ch))
                j++
            }
        }

        if (j == 0 || j < min || j > max) continue // goto repeat
        if (tmp1.isNotEmpty() && tmp1[0] in '0'..'9') continue // isdigit(tmp1[0])

        return tmp1.toString() // tmp1[j]=0; tmp=tmp1; return tmp
    }
}
