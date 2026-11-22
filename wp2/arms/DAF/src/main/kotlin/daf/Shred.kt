package daf

// Unit 176 (DAF B6 T3, complex): libmpscpp.cpp::Shred
// (units.csv row: loc 40, score 37.4, rank 241, complex): the
// \$ZWI/\$ZWP token generator of the bsv family: the FIRST call
// filters the input to lowercased alphanumerics into a static
// buffer; every call after that emits the next fixed-size
// alphanumeric fragment; the fragment source exhausted resets the
// static state for the next re-arm. SEAT RELEASE: this render
// releases the shredD named-owner stub of LibmpscppMisc9.kt (the
// bsv blame "selftext.cpp Shred" is corrected below: grep-verified,
// THIS batch-6 roster row IS the Shred at libmpscpp.cpp - the
// ShredQuery sibling stub keeps its selftext.cpp blame) - the
// wrapper ShredW call keeps the shredD(String, Int) signature.
// Consults (2026-11-22T16:20:00Z, nominal): BASE-log rows 6/22
// hit-reuse (the C function-static lattice p1/p2/flg/string/word
// folds onto the ShredStateD singleton carrier - the file-scope
// static state of the vendor function, rows 6/22 singleton fold),
// row 25 hit-reuse (isalnum -> isAlnumAscii, tolower ->
// toLowerAscii - the ASCII-only ctype fold), row 59/60-adjacent
// hit-adapt (cout << \"...\" << endl folds onto println), row 59
// hit-reuse (sigint(100) rides the sigintDaf seat of T1), rows
// 99/100 hit-adapt (the malloc/free/free(NULL) heap dance of the
// C string carrier folds onto the Kotlin String - the GC owns the
// buffer; the malloc-NULL out-of-memory branch is UNREACHABLE on
// the JVM, logged divergence), row 101-family hit-adapt (strncpy
// word[size]='\0' folds onto substring - the NUL-pad of a short
// tail has no string observable), row 90 exact-shape (the static
// cursor pair p1/p2 over one buffer models as an Int cursor over
// the singleton String; the copy loop keeps the vendor walk order).
// VENDOR QUIRKS kept: (1) the size>=128 guard prints the
// \"Shred fragment size too large\" message and calls sigint(100)
// but then FALLS THROUGH - the fragment loop would overrun the
// static word[128] in C; the render keeps the guard verbatim (the
// sigint seat exits/throws, so the fall-through is unreachable in
// practice - ledger); (2) a final PARTIAL fragment (fewer than
// size chars left) is DISCARDED: the vendor writes it into word,
// then p1 advances past p2 and the empty-string return RESETS -
// the render folds the discarded write; (3) a final EXACT-size
// fragment returns normally and the reset happens on the NEXT call
// (which reads the empty remainder and returns \"\") - the exact
// call sequence is kept by the cursor-fold; (4) the string consumed
// callback frees and NULs the static string BUT NOT p1/p2/word -
// the flg flag alone gates the re-arm (unused-between-values
// ledger).
// Dafny: EXEMPT (stateful token iterator over the sigint/cout
// seats; journal row).

// The C function-static lattice of Shred - the singleton carrier.
private object ShredStateD {
    // static char *p1=NULL, *p2; static int flg=0; static char *string
    var filtered: String? = null // string: the filtered token source
    var flg = 0
    var cursor = 0 // p1 (index into string; p2 folds to filtered.length)
}

// The 128-byte fragment cap of the vendor word buffer.
private const val SHRED_WORD_CAP_D = 128

fun shredD(a: String, i: Int): String {

    // if (size>=128) { cout << "Shred fragment size too large";
    //                  sigint(100); }
    if (i >= SHRED_WORD_CAP_D) {
        println("Shred fragment size too large")
        sigintDaf(100)
    }

    // if (flg==0) { ... the re-arm block ... }
    if (ShredStateD.flg == 0) {

        // the isalnum+tolower filter walk (the malloc/free folds
        // onto the String build; the p2==string empty guard kept)
        val b = StringBuilder()
        var p1 = 0 // p1=str (read cursor over the argument)
        while (p1 < a.length) {
            val c = a[p1]
            if (isAlnumAscii(c)) b.append(toLowerAscii(c))
            p1++
        }
        if (b.isEmpty()) {
            return "" // if (p2==string) return "";
        }
        ShredStateD.filtered = b.toString() // *p2=0, string armed
        ShredStateD.flg = 1 // don't reenter until consumed
        ShredStateD.cursor = 0 // p1=string
    }

    while (true) { // while (1)
        val src = ShredStateD.filtered ?: ""

        // strncpy(word,p1,size); word[size]=0; p1=p1+size;
        val end = minOf(ShredStateD.cursor + i, src.length)

        // if (p1>p2) { flg=0; free(string); string=NULL; return ""; }
        if (ShredStateD.cursor + i > src.length) {
            ShredStateD.flg = 0 // reset for the next re-arm
            ShredStateD.filtered = null
            return ""
        }

        // return word; (the full-size fragment)
        val word = src.substring(ShredStateD.cursor, end)
        ShredStateD.cursor += i
        return word
    }
}
