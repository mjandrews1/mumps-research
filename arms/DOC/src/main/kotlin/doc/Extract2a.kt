package doc

// Unit 231 (B8 tranche 2): compiler-parse.c::Extract2a (batch 8,
// core stratum; LOC 21, score 18.3, rank 158; units.csv row 231;
// vendor compiler-parse.c:2129, physically verified this session:
// the dump asserts physical loc 21 = the roster row). The 'a'
// sibling of the unit-15 Extract2 render: splits the same coded
// #S#/#G#name#target# payload and emits the TERNARY lookup text
// (sym_(1, ...) ? target : VariableNotFound1(...), the //E21000
// tag; the #G# arm the Mglobal(RETRIEVE) ? : VariableNotFound
// ternary). Consult H-014 hit-reuse: the Extract2 seat family
// verbatim - the static char tmp1[64]/tmp2[64] function-scratch
// pair rides its own H-006 holder object (Extract2aStatic,
// separate state from Extract2Static); fprintf maps to
// CompilerState.emit per H-001, emission text byte-verbatim
// including the (UNS)/(UNS) casts; the offset-parameter entry
// idiom (&str[i] of the ExpandText call site) is the u15 seat
// contract (str[0] -> str[offset]); the #less early return
// renders as the defensive copy per the u15 note. The vendor
// memcpy fills fold to explicit loop writes on the unit-15
// pattern. Naming per H-017 (Extract2a -> extract2a).

object Extract2aStatic {
    // compiler-parse.c:2131 static char tmp1[64]
    val tmp1 = CharArray(64)
    // compiler-parse.c:2131 static char tmp2[64]
    val tmp2 = CharArray(64)
}

fun extract2a(str: CharArray, offset: Int): CharArray {
    if (str[offset] != '#') return str.copyOf()

    var i = offset + 3
    while (i < str.size && str[i] != '#' && str[i] != NUL) i++

    val t1 = Extract2aStatic.tmp1
    var n = 0
    while (n < i - (offset + 3)) {
        t1[n] = str[offset + 3 + n]
        n++
    }
    t1[n] = NUL

    val j = i + 1
    i = j
    while (i < str.size && str[i] != '#' && str[i] != NUL) i++

    val t2 = Extract2aStatic.tmp2
    var n2 = 0
    while (n2 < i - j) {
        t2[n2] = str[j + n2]
        n2++
    }
    t2[n2] = NUL
    val t1s = CstringOps.cstring(t1)
    val t2s = CstringOps.cstring(t2)

    if (CstringOps.strncmpAt(str, offset, "#S#", 3) == 0) {

        CompilerState.emit("\tsym_(1, (UNS) " + t1s + ", (UNS) " + t2s + "," +
                "svPtr) ? " + t2s +
                " : VariableNotFound1(svPtr->LineNumber, (UNS) " + t1s +
                "); //E21000\n")

    } else if (CstringOps.strncmpAt(str, offset, "#G#", 3) == 0) {

        CompilerState.emit("\tMglobal(RETRIEVE, " + t1s + ", " + t2s + ", svPtr) ?" +
                t2s + " : VariableNotFound(svPtr->LineNumber, " + t1s + ");\n")
    }

    return t2.copyOf()
}
