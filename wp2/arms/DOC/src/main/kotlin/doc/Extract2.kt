package doc

// Unit 15 (B1 tranche 3): compiler-parse.c::Extract2 (batch 1, core
// stratum; units.csv row 15 lander closes the Stubs.Extract2 seat).
// Splits a coded #G#/#S#name#target# string: tmp1 collects the name
// field and tmp2 the target field, and the #S# / #G# comparisons
// decide which runtime lookup text is emitted into Out. The static
// char tmp1[64]/tmp2[64] sit in one holder object per H-006
// (Extract2Static). Vendor quirks kept and noted: tmp2's terminator
// is written as the multicharacter constant ' \0' (a C multi-char
// literal; the char assignment truncates to the low byte NUL - same
// effect, kept as the NUL write with this note); the nonzero-offset
// entry idiom (&v1d[i]) is the offset parameter (the Stubs seat
// contract). Bounds walks are carrier-guarded (H-007). The #less early
// return of the whole carrier renders as a defensive copy (the vendor
// returns the caller's pointer itself; copy-avoidance has no safe
// analog in the seat contract). fprintf maps to CompilerState.emit
// per H-001, emission text byte-verbatim. Naming per H-017.

object Extract2Static {
    // compiler-parse.c static char tmp1[64]
    val tmp1 = CharArray(64)
    // compiler-parse.c static char tmp2[64]
    val tmp2 = CharArray(64)
}

fun extract2(str: CharArray, offset: Int): CharArray {
    if (str[offset] != '#') return str.copyOf()

    var i = offset + 3
    while (i < str.size && str[i] != '#' && str[i] != NUL) i++

    val t1 = Extract2Static.tmp1
    var n = 0
    while (n < i - (offset + 3)) {
        t1[n] = str[offset + 3 + n]
        n++
    }
    t1[n] = NUL

    val j = i + 1
    i = j
    while (i < str.size && str[i] != '#' && str[i] != NUL) i++

    val t2 = Extract2Static.tmp2
    val t1s = CstringOps.cstring(t1)
    var n2 = 0
    while (n2 < i - j) {
        t2[n2] = str[j + n2]
        n2++
    }
    // vendor writes the multicharacter constant ' \0' here; the char
    // truncation lands on NUL
    t2[n2] = NUL
    val t2s = CstringOps.cstring(t2)

    if (CstringOps.strncmpAt(str, offset, "#S#", 3) == 0) {
        CompilerState.emit("\tif (sym_(SYMGET, (UNS) " + t1s +
                ", (UNS) " + t2s + ", svPtr) == NULL) // get value \n")
        CompilerState.emit("\t\tVariableNotFound1(svPtr->LineNumber, (UNS) " +
                t1s + "); // not found\n")
    } else if (CstringOps.strncmpAt(str, offset, "#G#", 3) == 0) {
        CompilerState.emit("\tif (!Mglobal(RETRIEVE, " + t1s + ", " + t2s +
                ", svPtr)) // retrieve global\n" +
                "\t\tVariableNotFound(svPtr->LineNumber, " + t1s + ");\n")
    }

    return t2.copyOf()
}
