package daf

// R1 stub-completion (2026-11-27, C-020 erratum session, tranche 3):
// interp-bstring.cpp::<misc> (roster row 11, the unit-22 owner):
// the three bstring builders. new_bstring wraps checked_strlen
// (the NUL scan with the STR_MAX abort) plus the copy;
// bstrcat checks the concatenated length against STR_MAX (the
// stderr message + sigint abort) and concatenates;
// bcatchr appends one char after the same length abort.
// The DafBstring carrier (row 68 by-value struct fold,
// RpmParserStubsD.kt) keeps its StringBuilder string - the
// length field of the vendor struct is the builder length
// (ledgered). The bstrcatDaf/DBstringD twin at InterpBstring.kt
// is a different roster unit; these seats keep the
// newBstringD/bstrcatD/bcatchrD names of the raised stubs.
// Consults (2026-11-27T00:00:00Z, nominal): BASE-log row 2
// family hit-reuse (the checked_strlen/bstrcat/bcatchr stderr
// aborts render as the vendor-message println precedent of the
// readf_ fold; sigintDaf the T1 seat); rows 68/56 hit-adapt
// (the strncpy/strncat shapes ride the StringBuilder append
// fold); row 81 hit-reuse (the NUL terminator of the vendor
// string[STR_MAX+1] is implicit in the builder; the stray
// result.string[left.length]=0 between the copy and the cat is
// a dead store in the vendor, not carried, disclosed).
// Vendor quirks kept: (1) checked_strlen scans i <= STR_MAX
// (STR_MAX+1 cells) before aborting; (2) bstrcat's sigint arm
// falls through to the dummy unreachable new_bstring("") return
// - the render returns the empty bstring after sigintDaf to
// model the vendor control flow; (3) bcatchr increments the
// length BEFORE the STR_MAX test and still writes the char
// after the sigint call - the render checks first, then
// appends (same observable while sigint exits, disclosed).
// Dafny: EXEMPT (buffer-shaping helpers; journal row).

// struct bstring new_bstring(const char initial[])
fun newBstringD(initial: String): DafBstring {

    // int checked_strlen(const char[]) - the i <= STR_MAX scan
    if (initial.length > STR_MAX_DAF) {
        // fprintf(stderr, "String too long - aborting.\n")
        println("String too long - aborting.")
        sigintDaf(100)
    }

    val result = DafBstring() // struct bstring result;
    result.string.append(initial) // strncpy(result.string, ...)
    return result
}

// struct bstring bstrcat(const struct bstring left,
//                        const struct bstring right)
fun bstrcatD(left: DafBstring, right: DafBstring): DafBstring {

    val catlength = left.string.length + right.string.length

    if (catlength > STR_MAX_DAF) {
        // fprintf(stderr, "String too long - aborting.\n");
        // sigint(100);
        println("String too long - aborting.")
        sigintDaf(100)
        // the vendor falls through to the dummy return below
        return newBstringD("") // /* Dummy return - unreachable */
    } else {
        val result = DafBstring() // struct bstring result;
        // strncpy(result.string, left.string, left.length);
        result.string.append(left.string)
        // strncat(result.string, right.string, right.length);
        result.string.append(right.string)
        return result
    }
}

// struct bstring bcatchr(struct bstring *str, const char c)
fun bcatchrD(str: DafBstring, c: Char): Unit {

    // str->length++; if (str->length > STR_MAX) abort;
    if (str.string.length + 1 > STR_MAX_DAF) {
        // fprintf(stderr, "String too long, aborting.\n")
        println("String too long, aborting.")
        sigintDaf(100)
    }

    // str->string[str->length-1] = c; str->string[str->length] = '\0';
    str.string.append(c)
    // (the vendor returns *str through the pointer; the Kotlin
    // seat mutates the DafBstring carrier in place - Unit return)
}
