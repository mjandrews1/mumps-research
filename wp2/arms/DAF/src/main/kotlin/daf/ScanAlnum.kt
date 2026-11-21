package daf

// Unit 104 (DAF B4 T2, core stratum): libmpscpp.cpp::ScanAlnum (18 LOC,
// the units.csv batch-4 row is the score-10.6 overload of three; the
// pointer_marks=1 candidate rendered here): the mstring word scanner
// that reads whitespace-delimited tokens and returns the lowercased
// alphanumeric residue of the first acceptable one.
// Consults: BASE-log rows 25/28 (libc ctype + libc helpers -> CRT facts:
// isalnum via the ASCII-only range check isAlnumAscii, tolower via
// toLowerAscii, both adapted into DafCRuntimeT2.kt), row 10 (backward
// goto repeat -> structured while(true)+continue), row 56 family (the
// 8192-byte word/tmp1 scratch buffers and the mstring tmp become fresh
// per-invocation values), fscanf -> the manual-decision fscanfS seat
// in DafCRuntimeT2.kt (PushbackReader = FILE* seat; EOF returns null,
// giving the empty-mstring EOF return of the vendor guard).
// Vendor quirk preserved: the max > 511 guard shares the same EOF
// branch (empty string return); isdigit(tmp1[0]) reads tmp1[0] only
// when j > 0 (C short-circuit || order kept by the Kotlin || chain).
// Dafny: EXEMPT (stream-bound filter loop; journal row).

fun ScanAlnum(u: java.io.PushbackReader, min: Int, max: Int): String {

    val word = StringBuilder() // char word[8192] seat (row 56 family)
    val tmp1 = StringBuilder() // char tmp1[8192] seat

    while (true) { // repeat:

        word.setLength(0); tmp1.setLength(0)

        val token = fscanfS(u)
        if (token == null || max > 511) return "" // EOF or max cap
        word.append(token)

        for (i in 0 until word.length) { // for (i=0; word[i]; i++)
            val c = word[i]
            if (isAlnumAscii(c)) tmp1.append(toLowerAscii(c))
        }

        val j = tmp1.length
        if (j == 0 || j < min || j > max) continue // goto repeat
        if (tmp1[0] in '0'..'9') continue          // isdigit(tmp1[0])

        return tmp1.toString() // tmp = tmp1; mstring value return
    }
}
